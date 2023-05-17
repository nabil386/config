package curam.rules.functions;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.intf.AddressElement;
import curam.core.intf.ConcernRoleAddress;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressReadMultiDtls;
import curam.core.struct.AddressReadMultiDtlsList;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.piwrapper.impl.Address;
import curam.piwrapper.impl.AddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * BDMOAS FEATURE 52093: Class Added
 * Custom function to pre-populate the application details.
 *
 * @author SMisal
 *
 */
public class CustomFunctionPrepopulatePartnerAddressDetails
  extends BDMFunctor {

  @Inject
  private AddressDAO addressDAO;

  public CustomFunctionPrepopulatePartnerAddressDetails() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Custom function to pre populate the application details.
   *
   * @param rulesParameters the rules parameters
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    try {

      // get the datastore root entity identifier from the rules parameters
      final IEG2Context ieg2context = (IEG2Context) rulesParameters;
      final long rootEntityID = ieg2context.getRootEntityID();

      final IEG2ExecutionContext ieg2ExecutionContext =
        new IEG2ExecutionContext(rulesParameters);

      final Datastore datastore = ieg2ExecutionContext.getDatastore();

      final Entity applicationEntity = datastore.readEntity(rootEntityID);

      final Entity personEntity_Applicant =
        applicationEntity.getChildEntities(
          datastore.getEntityType(BDMDatastoreConstants.PERSON),
          "isPrimaryParticipant==true")[0];

      final Entity personEntity_Partner = applicationEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.PERSON),
        "isPrimaryParticipant==false")[0];

      // Get applicants residential address.
      final AddressKey addressKey =
        this.getApplicantAddressDetails(personEntity_Applicant);

      // set Address Entity Attribute
      if (addressKey != null && addressKey.addressID != 0) {
        addResidentialAddress(applicationEntity, datastore,
          personEntity_Partner, addressKey);
      }

    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + BDMOASConstants.ERROR_PREPOPULATING_PART_ADDRESS + e.getMessage());
    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
  }

  /**
   *
   * @param personEntity
   * @throws AppException
   * @throws InformationalException
   */
  private AddressKey getApplicantAddressDetails(final Entity personEntity)
    throws AppException, InformationalException {

    final String concenrRoleID = personEntity.getAttribute("localID");

    final ConcernRoleAddress concernroleAddressObj =
      ConcernRoleAddressFactory.newInstance();
    AddressKey addressKey = null;

    final ConcernRoleIDStatusCodeKey concernRoleIDStatusKey =
      new ConcernRoleIDStatusCodeKey();

    concernRoleIDStatusKey.concernRoleID = Long.parseLong(concenrRoleID);
    concernRoleIDStatusKey.statusCode = RECORDSTATUS.NORMAL;

    final AddressReadMultiDtlsList concernRoleAddressList =
      concernroleAddressObj
        .searchAddressesByConcernRoleIDAndStatus(concernRoleIDStatusKey);

    for (final AddressReadMultiDtls concernRoleAddressDtls : concernRoleAddressList.dtls) {
      // Residential Address - AT1
      if (concernRoleAddressDtls.typeCode
        .equals(CONCERNROLEADDRESSTYPE.PRIVATE)) {
        addressKey = new AddressKey();
        addressKey.addressID = concernRoleAddressDtls.addressID;
        break;
      }
    }
    return addressKey;
  }

  /**
   *
   * @param applicationEntity
   * @param datastore
   * @param personEntity
   * @param addressKey
   * @throws AppException
   * @throws InformationalException
   */
  private void addResidentialAddress(final Entity applicationEntity,
    final Datastore datastore, final Entity personEntity,
    final AddressKey addressKey) throws AppException, InformationalException {

    final AddressElement addressElementObj =
      AddressElementFactory.newInstance();

    // Read Address Elements for a given Address ID
    final AddressElementDtlsList addressElementDtlsList =
      addressElementObj.readAddressElementDetails(addressKey);

    /*
     * final Entity residentialAddress =
     * datastore.newEntity(datastore.getEntityType("ResidentialAddress"));
     */

    final Entity[] residentialAddressEntityList =
      personEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));

    Entity residentialAddress = null;
    String residentialCountry = "";

    if (residentialAddressEntityList != null
      && residentialAddressEntityList.length > 0) {
      residentialAddress = residentialAddressEntityList[0];
    } else {
      residentialAddress = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));
      personEntity.addChildEntity(residentialAddress);
      personEntity.update();
    }

    for (final AddressElementDtls addressElement : addressElementDtlsList.dtls) {

      if (addressElement.elementType.equals(ADDRESSELEMENTTYPE.APT)) {
        residentialAddress.setAttribute(BDMDatastoreConstants.SUITE_NUM,
          addressElement.elementValue);
      } else if (addressElement.elementType.equals(ADDRESSELEMENTTYPE.CITY)) {

        residentialAddress.setAttribute(BDMDatastoreConstants.CITY,
          addressElement.elementValue);
      }
      // province for CA, and state for INTL
      else if (addressElement.elementType.equals(ADDRESSELEMENTTYPE.STATE)) {

        residentialAddress.setAttribute(BDMDatastoreConstants.STATE,
          addressElement.elementValue);

      } else if (addressElement.elementType
        .equals(ADDRESSELEMENTTYPE.COUNTRY)) {

        personEntity.setTypedAttribute(
          BDMDatastoreConstants.RESIDENTIAL_COUNTRY,
          addressElement.elementValue);
        residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
          addressElement.elementValue);
        residentialCountry = addressElement.elementValue;

      } else if (addressElement.elementType
        .equals(ADDRESSELEMENTTYPE.PROVINCE)) {

        residentialAddress.setAttribute(BDMDatastoreConstants.PROVINCE,
          addressElement.elementValue);

      } else if (addressElement.elementType
        .equals(ADDRESSELEMENTTYPE.LINE1)) {

        residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1,
          addressElement.elementValue);

      } else if (addressElement.elementType
        .equals(ADDRESSELEMENTTYPE.LINE2)) {

        residentialAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
          addressElement.elementValue);

      } else if (addressElement.elementType
        .equals(ADDRESSELEMENTTYPE.POSTCODE)) {

        residentialAddress.setAttribute(BDMDatastoreConstants.POSTAL_CODE,
          addressElement.elementValue);

      } else if (addressElement.elementType.equals(ADDRESSELEMENTTYPE.ZIP)) {

        residentialAddress.setAttribute(BDMDatastoreConstants.ZIP_CODE,
          addressElement.elementValue);
      }

    }

    final Address addressObj = addressDAO.get(addressKey.addressID);

    // set Online Address
    residentialAddress.setAttribute(BDMDatastoreConstants.ONE_LINE_ADDRESS,
      addressObj.getOneLineAddressString());
    residentialAddress.update();

    final Entity searchAddress = datastore.newEntity(
      datastore.getEntityType(BDMOASDatastoreConstants.kSearchAddress));
    personEntity.addChildEntity(searchAddress);

    final Entity searchCriteriaResi = datastore.newEntity(
      datastore.getEntityType(BDMOASDatastoreConstants.kSearchCriteriaResi));
    searchCriteriaResi.setTypedAttribute(
      BDMOASDatastoreConstants.kResidCountry, residentialCountry);
    personEntity.addChildEntity(searchCriteriaResi);

    personEntity.update();
  }
}
