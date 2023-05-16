package curam.rules.functions;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidenceVO;
import curam.ca.gc.bdm.util.impl.BDMDataStoreEntityUtil;
import curam.ca.gc.bdmoas.application.impl.BDMOASApplicationUtil;
import curam.ca.gc.bdmoas.application.impl.BDMOASDataStoreEntityUtil;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.PersonFactory;
import curam.core.intf.AddressElement;
import curam.core.intf.ConcernRoleAddress;
import curam.core.intf.Person;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressReadMultiDtls;
import curam.core.struct.AddressReadMultiDtlsList;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.piwrapper.impl.Address;
import curam.piwrapper.impl.AddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.type.Date;
import java.util.List;

/**
 * BDMOAS FEATURE 52093: Class Added
 * Custom function to pre-populate the application details.
 *
 * @author SMisal
 *
 */
public class CustomFunctionPrepopulateDetails extends BDMFunctor {

  @Inject
  private AddressDAO addressDAO;

  @Inject
  private BDMDataStoreEntityUtil bdmDataStoreEntityUtil;

  @Inject
  private BDMOASDataStoreEntityUtil bdmoasDataStoreEntityUtil;

  public CustomFunctionPrepopulateDetails() {

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

      final Entity personEntity = applicationEntity
        .getChildEntities(datastore.getEntityType("Person"))[0];
      // set Person Entity Attribute
      setPersonEntityAttributes(personEntity);
      // set Address Entity Attribute
      addAddressDetails(applicationEntity, datastore, personEntity);
      final long concernRoleID =
        Long.parseLong(personEntity.getAttribute("localID"));
      // set Communication Details Entity Attribute
      bdmDataStoreEntityUtil.getCommunicationDetailsEnity(personEntity,
        datastore, concernRoleID, "placeHolder");
      // set Communication Pref Entity Attribute
      bdmDataStoreEntityUtil.getCommunicationPrefEntity(personEntity,
        datastore, concernRoleID);
      bdmDataStoreEntityUtil.getPaymentBanksEntity(personEntity, datastore,
        concernRoleID);
      // Set LegalStatusEntity attributes
      bdmoasDataStoreEntityUtil.getAndSetLegalStatusEntity(personEntity,
        datastore, concernRoleID);

      personEntity.update();

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + BDMOASConstants.ERROR_PREPOPULATING_INTAKE_DETAILS
        + e.getMessage());
    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
  }

  /**
   *
   * @param personEntity
   * @throws AppException
   * @throws InformationalException
   */
  private void setPersonEntityAttributes(final Entity personEntity)
    throws AppException, InformationalException {

    final String concernRoleID = personEntity.getAttribute("localID");

    // Get PersonDtls
    final Person personObj = PersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    PersonDtls personDtls = new PersonDtls();
    personKey.concernRoleID = Long.parseLong(concernRoleID);

    personDtls = personObj.read(personKey);

    // set last name at birth
    personEntity.setTypedAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH,
      personDtls.personBirthName);

    // set parents last name at birth
    personEntity.setTypedAttribute(
      BDMDatastoreConstants.PARENT_LAST_NAME_AT_BIRTH,
      personDtls.motherBirthSurname);

    // set marital status
    personEntity.setTypedAttribute(BDMDatastoreConstants.MARITAL_STATUS,
      personDtls.maritalStatusCode);

    // Set SIN, check if null
    final String sinNumber =
      BDMUtil.getAlternateIDByConcernRoleIDType(Long.parseLong(concernRoleID),
        CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);

    personEntity.setTypedAttribute(BDMDatastoreConstants.SIN, sinNumber);
    personEntity.setTypedAttribute(BDMOASDatastoreConstants.SIN_RAW_AP,
      sinNumber);

    // Set OAS account number.
    final String oasAccountNumber =
      BDMOASApplicationUtil.getActiveAlternateIDByConcernRoleIDAndType(
        Long.parseLong(concernRoleID),
        CONCERNROLEALTERNATEID.BDM_ACCOUNT_NUMBER);

    personEntity.setTypedAttribute(
      BDMOASDatastoreConstants.OAS_ACCOUNT_NUM_ATTR, oasAccountNumber);

    // Set person title.
    final String title =
      BDMOASApplicationUtil.getPersonTitle(Long.parseLong(concernRoleID));

    personEntity.setTypedAttribute(BDMOASDatastoreConstants.kPersonTitle,
      title);

    // Assuming Addresss are different REs and Mail
    // BEGIN BUG 110413
    /*
     * personEntity.setTypedAttribute(
     * BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT, IEGYESNO.YES);
     */
    // END BUG 110413

    // set date of death.
    personEntity.setTypedAttribute(BDMOASDatastoreConstants.DATE_OF_DEATH,
      personDtls.dateOfDeath);

    // set country of birth.
    if (!StringUtil.isNullOrEmpty(personDtls.countryOfBirth)) {
      personEntity.setTypedAttribute(
        BDMOASDatastoreConstants.COUNTRY_OF_BIRTH, personDtls.countryOfBirth);
    } else {
      // get the date of birth evidence data for concern role
      final List<BDMDateofBirthEvidenceVO> getDOBEvidenceValueObjectList =
        new BDMDateofBirthEvidence()
          .getDOBEvidenceValueObject(personDtls.concernRoleID);

      if (!getDOBEvidenceValueObjectList.isEmpty()) {
        final BDMDateofBirthEvidenceVO dobEvidenceVO =
          getDOBEvidenceValueObjectList.get(0);
        personEntity.setTypedAttribute(
          BDMOASDatastoreConstants.COUNTRY_OF_BIRTH,
          dobEvidenceVO.getCountryOfBirth());
      }
    }

    personEntity.update();
  }

  /**
   *
   * @param personEntity
   * @throws AppException
   * @throws InformationalException
   */
  private void addAddressDetails(final Entity applicationEntity,
    final Datastore datastore, final Entity personEntity)
    throws AppException, InformationalException {

    final String concenrRoleID = personEntity.getAttribute("localID");

    final ConcernRoleAddress concernroleAddressObj =
      ConcernRoleAddressFactory.newInstance();
    final AddressKey addressKey = new AddressKey();

    final ConcernRoleIDStatusCodeKey concernRoleIDStatusKey =
      new ConcernRoleIDStatusCodeKey();

    concernRoleIDStatusKey.concernRoleID = Long.parseLong(concenrRoleID);
    concernRoleIDStatusKey.statusCode = RECORDSTATUS.NORMAL;

    final AddressReadMultiDtlsList concernRoleAddressList =
      concernroleAddressObj
        .searchAddressesByConcernRoleIDAndStatus(concernRoleIDStatusKey);

    for (final AddressReadMultiDtls concernRoleAddressDtls : concernRoleAddressList.dtls) {

      addressKey.addressID = concernRoleAddressDtls.addressID;

      // Residential Address - AT1
      if (concernRoleAddressDtls.typeCode
        .equals(CONCERNROLEADDRESSTYPE.PRIVATE)
        && (concernRoleAddressDtls.endDate.isZero()
          || !concernRoleAddressDtls.endDate.before(Date.getCurrentDate()))) {
        addResidentialAddress(applicationEntity, datastore, personEntity,
          addressKey);
      }

      // Mailing Address - AT4
      if (concernRoleAddressDtls.typeCode
        .equals(CONCERNROLEADDRESSTYPE.MAILING)
        && (concernRoleAddressDtls.endDate.isZero()
          || !concernRoleAddressDtls.endDate.before(Date.getCurrentDate()))) {
        addMailingAddress(applicationEntity, datastore, personEntity,
          addressKey);
      }

    }
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
    boolean existingAddress = false;
    String residentialCountry = "";

    if (residentialAddressEntityList != null
      && residentialAddressEntityList.length > 0) {
      residentialAddress = residentialAddressEntityList[0];
      existingAddress = true;
    } else {
      residentialAddress = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));
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

    final Entity searchAddress = datastore.newEntity(
      datastore.getEntityType(BDMOASDatastoreConstants.kSearchAddress));
    // set Online Address
    residentialAddress.setAttribute(BDMDatastoreConstants.ONE_LINE_ADDRESS,
      addressObj.getOneLineAddressString());
    personEntity.addChildEntity(searchAddress);

    final Entity searchCriteriaResi = datastore.newEntity(
      datastore.getEntityType(BDMOASDatastoreConstants.kSearchCriteriaResi));

    searchCriteriaResi.setTypedAttribute(
      BDMOASDatastoreConstants.kResidCountry, residentialCountry);

    personEntity.addChildEntity(searchCriteriaResi);

    if (!existingAddress) {
      personEntity.addChildEntity(residentialAddress);
    } else {
      residentialAddress.update();
    }
    personEntity.setTypedAttribute(
      BDMOASDatastoreConstants.kHasActiveResidAddress, true);
    personEntity.update();
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
  private void addMailingAddress(final Entity applicationEntity,
    final Datastore datastore, final Entity personEntity,
    final AddressKey addressKey) throws AppException, InformationalException {

    final AddressElement addressElementObj =
      AddressElementFactory.newInstance();

    // Read Address Elements for a given Address ID
    final AddressElementDtlsList addressElementDtlsList =
      addressElementObj.readAddressElementDetails(addressKey);

    final Entity[] mailingAddressEntityList = personEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));

    Entity mailingAddress = null;
    boolean existingAddress = false;
    String mailingCountry = "";

    if (mailingAddressEntityList != null
      && mailingAddressEntityList.length > 0) {
      mailingAddress = mailingAddressEntityList[0];
      existingAddress = true;
    } else {
      mailingAddress =
        datastore.newEntity(datastore.getEntityType("MailingAddress"));
    }

    for (final AddressElementDtls addressElement : addressElementDtlsList.dtls) {

      if (addressElement.elementType.equals(ADDRESSELEMENTTYPE.APT)) {
        mailingAddress.setAttribute(BDMDatastoreConstants.SUITE_NUM,
          addressElement.elementValue);
      } else if (addressElement.elementType.equals(ADDRESSELEMENTTYPE.CITY)) {

        mailingAddress.setAttribute(BDMDatastoreConstants.CITY,
          addressElement.elementValue);
      }
      // province for CA, and state for INTL
      else if (addressElement.elementType.equals(ADDRESSELEMENTTYPE.STATE)
        || addressElement.elementType.equals(ADDRESSELEMENTTYPE.STATEPROV)) {

        mailingAddress.setAttribute(BDMDatastoreConstants.STATE,
          addressElement.elementValue);

      } else if (addressElement.elementType
        .equals(ADDRESSELEMENTTYPE.COUNTRY)) {

        personEntity.setTypedAttribute(BDMDatastoreConstants.MAILING_COUNTRY,
          addressElement.elementValue);
        mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
          addressElement.elementValue);
        mailingCountry = addressElement.elementValue;

      } else if (addressElement.elementType
        .equals(ADDRESSELEMENTTYPE.PROVINCE)) {

        mailingAddress.setAttribute(BDMDatastoreConstants.PROVINCE,
          addressElement.elementValue);

      } else if (addressElement.elementType
        .equals(ADDRESSELEMENTTYPE.LINE1)) {

        mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1,
          addressElement.elementValue);

      } else if (addressElement.elementType
        .equals(ADDRESSELEMENTTYPE.LINE2)) {

        mailingAddress.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
          addressElement.elementValue);

      } else if (addressElement.elementType
        .equals(ADDRESSELEMENTTYPE.POSTCODE)) {

        mailingAddress.setAttribute(BDMDatastoreConstants.POSTAL_CODE,
          addressElement.elementValue);

      } else if (addressElement.elementType.equals(ADDRESSELEMENTTYPE.ZIP)) {

        mailingAddress.setAttribute(BDMDatastoreConstants.ZIP_CODE,
          addressElement.elementValue);
      }

    }
    // set Online Address
    final Address addressObj = addressDAO.get(addressKey.addressID);
    mailingAddress.setAttribute(BDMDatastoreConstants.ONE_LINE_ADDRESS,
      addressObj.getOneLineAddressString());

    if (!existingAddress) {
      personEntity.addChildEntity(mailingAddress);
    } else {
      mailingAddress.update();
    }

    final Entity searchCriteriaMail = datastore.newEntity(
      datastore.getEntityType(BDMOASDatastoreConstants.kSearchCriteriaMail));
    searchCriteriaMail.setTypedAttribute(
      BDMOASDatastoreConstants.kMailCountry, mailingCountry);
    personEntity.addChildEntity(searchCriteriaMail);

    personEntity.setTypedAttribute(
      BDMOASDatastoreConstants.kHasActiveMailingAddress, true);
    personEntity.update();

  }

}
