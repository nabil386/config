package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.util.impl.BDMAddressUtil;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.IEGYESNO;
import curam.core.struct.OtherAddressData;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Custom Function to Update Residential/Mailing address.
 *
 * TASK -9418 Input Application for Client
 *
 *
 * @curam.exclude
 */
public class CustomFunctionUpdateWitnessAddress extends BDMFunctor {

  /** The PO Box address prefix. */
  public static final String PO_BOX_PREFIX = "PO ";

  final BDMAddressUtil addressUtil = new BDMAddressUtil();

  /**
   * Instantiates a new custom function validate residential address.
   */
  public CustomFunctionUpdateWitnessAddress() {

    super();

  }

  /**
   * A custom function that will be called to update the Address from the user
   * selected Address data
   *
   *
   * @param rulesParameters The rules parameters containing the object to
   * check. The first parameter in the list of parameters is checked.
   *
   * @return A rule adaptor indicating whether the address is valid.
   *
   * @throws AppException Exceptions that may occur when retrieving the
   * adaptor value.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    // Get All parameters
    final boolean returnValue = false;
    int index = 0;
    final String addressType =
      getOptionalStringParam(rulesParameters, index++);
    final String step = getOptionalStringParam(rulesParameters, index++);
    final String streetNumber =
      getOptionalStringParam(rulesParameters, index++);
    final IEG2Context ieg2Context = (IEG2Context) rulesParameters;
    final IEG2ExecutionContext ieg2ExecutionContext =
      new IEG2ExecutionContext(rulesParameters);
    // Read person Entity
    final Datastore datastore = ieg2ExecutionContext.getDatastore();
    final long rootEntityID = ieg2Context.getRootEntityID();
    final Entity applicationEntity = datastore.readEntity(rootEntityID);

    final Entity declarationEntity =
      applicationEntity.getChildEntities(datastore
        .getEntityType(BDMOASDatastoreConstants.DECLARATION_ENTITY))[0];

    // Update Address into Address Entity
    addAddressDetails(applicationEntity, datastore, declarationEntity,
      addressType, step);

    declarationEntity.update();
    return AdaptorFactory.getBooleanAdaptor(returnValue);

  }

  /**
   *
   * @param applicationEntity
   * @param datastore
   * @param personEntity
   * @param addressType
   * @param step This will decide from where we are calling this function
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void addAddressDetails(final Entity applicationEntity,
    final Datastore datastore, final Entity declarationEntity,
    final String addressType, final String step)
    throws AppException, InformationalException {

    if (BDMDatastoreConstants.STEP_1.equalsIgnoreCase(step)) {

      resetIfCountryChanged(datastore, declarationEntity, addressType);

      final Entity[] entities = declarationEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.SEARCH_ADDRESS));
      // Residential Address - AT1
      for (final Entity searchAddress : entities) {
        // Process Mailing Address if it is active and is selected
        if (BDMDatastoreConstants.MAILING.equalsIgnoreCase(addressType)) {
          if (IEGYESNO.YES.equalsIgnoreCase(
            searchAddress.getAttribute(BDMDatastoreConstants.IS_ACTIVE))
            && IEGYESNO.YES.equalsIgnoreCase(searchAddress
              .getAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS))
            && "true".equalsIgnoreCase(searchAddress
              .getAttribute(BDMDatastoreConstants.IS_SELECTED))) {
            addMailingAddress(applicationEntity, datastore, declarationEntity,
              searchAddress);
          }
        }

      }
    }

    // This step to get oneline address from newly enterd/Selected data for
    // Exception/Invalid Message
    String countryName = "";

    if (BDMDatastoreConstants.STEP_2.equalsIgnoreCase(step)) {

      Entity[] entities = null;
      if (BDMDatastoreConstants.MAILING.equalsIgnoreCase(addressType)) {

        entities = declarationEntity.getChildEntities(
          datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));
        countryName = declarationEntity.getAttribute("mailingCountry");
      }
      if (entities != null) {
        for (final Entity address : entities) {

          final String onlineAddress = addressUtil.getOneLineAddress(address);
          Trace.kTopLevelLogger.info("For " + addressType
            + " ::OnlineAddress set to  : " + onlineAddress);
          address.setAttribute("country", countryName);
          address.update();
        }
      }
    }

  }

  /**
   * @param datastore
   * @param personEntity
   * @param addressType
   */
  private void resetIfCountryChanged(final Datastore datastore,
    final Entity declarationEntity, final String addressType) {

    final String personMailingCountry =
      declarationEntity.getAttribute(BDMDatastoreConstants.MAILING_COUNTRY);

    if (BDMDatastoreConstants.MAILING.equalsIgnoreCase(addressType)) {
      final Entity[] mailingAddresses = declarationEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));
      for (final Entity storedMailingAddress : mailingAddresses) {
        final String mailingCountry = storedMailingAddress
          .getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY);
        if (!personMailingCountry.equals(mailingCountry)) {
          resetAddressEntity(storedMailingAddress);
          storedMailingAddress.update();
        }
      }
      final Entity[] intlMailingAddresses =
        declarationEntity.getChildEntities(datastore
          .getEntityType(BDMDatastoreConstants.INTL_MAILING_ADDRESS));
      for (final Entity storedIntlMailingAddress : intlMailingAddresses) {
        final String intlMailingCountry = storedIntlMailingAddress
          .getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY);
        if (!personMailingCountry.equals(intlMailingCountry)) {
          resetIntlAddressEntity(storedIntlMailingAddress);
          storedIntlMailingAddress.update();
        }
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
  private void addMailingAddress(final Entity applicationEntity,
    final Datastore datastore, final Entity declarationEntity,
    final Entity searchAddress) throws AppException, InformationalException {

    // Read Address data from SearchAddress for processing and updating entity
    final Entity[] mailingAddressList = declarationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));
    Entity mailingAddressEntity;
    if (mailingAddressList == null || mailingAddressList.length == 0) {
      mailingAddressEntity = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));
      declarationEntity.addChildEntity(mailingAddressEntity);
      declarationEntity.update();
    } else {
      mailingAddressEntity = mailingAddressList[0];
    }
    final String addressData =
      searchAddress.getAttribute(BDMDatastoreConstants.ADDRESS_DATA1);
    setAddressFromSearchAddress(mailingAddressEntity, addressData);
    mailingAddressEntity.update();

  }

  /**
   *
   * @param addressEntity
   * @param addressData
   * @throws AppException
   * @throws InformationalException
   */

  public void setAddressFromSearchAddress(final Entity addressEntity,
    final String addressData) {

    try {

      // Set Address Data if it is not empty
      if (!addressUtil.getEmptyAddressData().equalsIgnoreCase(addressData)) {

        final OtherAddressData otherAddressData = new OtherAddressData();
        otherAddressData.addressData = addressData;

        final String streetNumber = addressUtil
          .getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.LINE1);

        final String streetName = addressUtil
          .getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.LINE2);

        final String apt = addressUtil.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.APT);

        // Postal Code
        final String postalCode = addressUtil
          .getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.POSTCODE);

        final String poBox = addressUtil.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.POBOXNO);

        // city
        final String city = addressUtil.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.CITY);

        // province
        String province;

        province = addressUtil.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.PROVINCE);
        final String country = addressUtil.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.COUNTRY);

        addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1,
          streetNumber);
        addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
          streetName);
        addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE3,
          poBox);
        addressEntity.setAttribute(BDMDatastoreConstants.CITY, city);
        // province for CA, and state for INTL
        addressEntity.setAttribute(BDMDatastoreConstants.PROVINCE, province);
        addressEntity.setAttribute(BDMDatastoreConstants.STATE, province);
        addressEntity.setAttribute(BDMDatastoreConstants.POSTAL_CODE,
          postalCode);

        addressEntity.setAttribute(BDMDatastoreConstants.SUITE_NUM, apt);
        addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
          country);

        addressEntity.setAttribute(
          BDMOASDatastoreConstants.IS_ADDR_SELECTED_ENT_ATTR, "true");
      } else {
        // if Address Data is empty reset entity to empty fields// same for
        // international
        Trace.kTopLevelLogger
          .info("Address Data is Empty reset Address Entity");

        addressEntity.setAttribute(
          BDMOASDatastoreConstants.IS_ADDR_SELECTED_ENT_ATTR, "false");

        resetAddressEntity(addressEntity);
      }
    } catch (final AppException e) {
      Trace.kTopLevelLogger
        .info(BDMOASConstants.ERROR_OCCURED_SETTING_ADDRESS, e.getMessage());
    } catch (final InformationalException e) {
      Trace.kTopLevelLogger
        .info(BDMOASConstants.ERROR_OCCURED_SETTING_ADDRESS, e.getMessage());
    }

  }

  /**
   * Reset Address Entity
   *
   * @param addressEntity
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void resetAddressEntity(final Entity addressEntity) {

    // ADDRESSELEMENTTYPE.APT
    addressEntity.setAttribute(BDMDatastoreConstants.SUITE_NUM, "");
    // ADDRESSELEMENTTYPE.CITY
    addressEntity.setAttribute(BDMDatastoreConstants.CITY, "");
    // province for CA, and state for INTL
    // ADDRESSELEMENTTYPE.STATE
    addressEntity.setAttribute(BDMDatastoreConstants.STATE, "");
    // ADDRESSELEMENTTYPE.COUNTRY
    // personEntity.setTypedAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY,"");
    addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY, "");
    // ADDRESSELEMENTTYPE.PROVINCE
    addressEntity.setAttribute(BDMDatastoreConstants.PROVINCE, "");
    // ADDRESSELEMENTTYPE.LINE1
    addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1, "");
    // ADDRESSELEMENTTYPE.LINE2
    addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2, "");
    // ADDRESSELEMENTTYPE.LINE2
    addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE3, "");
    // ADDRESSELEMENTTYPE.POSTCODE
    addressEntity.setAttribute(BDMDatastoreConstants.POSTAL_CODE, "");
    // ADDRESSELEMENTTYPE.ZIP
    addressEntity.setAttribute(BDMDatastoreConstants.ZIP_CODE, "");
  }

  /**
   * Reset Address Entity
   *
   * @param addressEntity
   *
   * @throws AppException
   * @throws InformationalException
   */
  public void resetIntlAddressEntity(final Entity addressEntity) {

    // ADDRESSELEMENTTYPE.CITY
    addressEntity.setAttribute(BDMDatastoreConstants.CITY, "");
    // ADDRESSELEMENTTYPE.STATE
    addressEntity.setAttribute(BDMDatastoreConstants.STATE, "");
    // ADDRESSELEMENTTYPE.ZIP
    addressEntity.setAttribute(BDMDatastoreConstants.ZIP_CODE, "");
    // ADDRESSELEMENTTYPE.LINE1
    addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1, "");
    // ADDRESSELEMENTTYPE.LINE2
    addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2, "");
    // ADDRESSELEMENTTYPE.LINE3
    addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE3, "");
    // ADDRESSELEMENTTYPE.LINE4
    addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE4, "");
    // ADDRESSELEMENTTYPE.PROVINCE
    addressEntity.setAttribute(BDMDatastoreConstants.PROVINCE, "");
    // ADDRESSELEMENTTYPE.APT
    addressEntity.setAttribute(BDMDatastoreConstants.SUITE_NUM, "");
    // ADDRESSELEMENTTYPE.COUNTRY
    addressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY, "");

  }

}
