package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.util.impl.BDMAddressUtil;
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
public class CustomFunctionUpdateAddress
  extends CustomFunctionValidateAddress {

  /** The PO Box address prefix. */
  public static final String PO_BOX_PREFIX = "PO ";

  final BDMAddressUtil addressUtil = new BDMAddressUtil();

  /**
   * Instantiates a new custom function validate residential address.
   */
  public CustomFunctionUpdateAddress() {

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

    final Entity personEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];
    // Update Address into Address Entity
    addAddressDetails(applicationEntity, datastore, personEntity, addressType,
      step);

    personEntity.update();
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
    final Datastore datastore, final Entity personEntity,
    final String addressType, final String step)
    throws AppException, InformationalException {

    if (BDMDatastoreConstants.STEP_1.equalsIgnoreCase(step)) {

      resetIfCountryChanged(datastore, personEntity, addressType);

      final Entity[] entities = personEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.SEARCH_ADDRESS));
      // Residential Address - AT1
      for (final Entity searchAddress : entities) {
        // Process REsidential if it is active and is selected
        if (BDMDatastoreConstants.RESIDENTIAL.equalsIgnoreCase(addressType)) {
          if (IEGYESNO.YES.equalsIgnoreCase(
            searchAddress.getAttribute(BDMDatastoreConstants.IS_ACTIVE))
            && IEGYESNO.YES.equalsIgnoreCase(searchAddress
              .getAttribute(BDMDatastoreConstants.IS_RESIDENTIAL_ADDRESS))
            && "true"
              .equalsIgnoreCase(searchAddress.getAttribute("isSelected"))) {
            addResidentialAddress(applicationEntity, datastore, personEntity,
              searchAddress);
          }
        }
        // Process Mailing Address if it is active and is selected
        if (BDMDatastoreConstants.MAILING.equalsIgnoreCase(addressType)) {
          if (IEGYESNO.YES.equalsIgnoreCase(
            searchAddress.getAttribute(BDMDatastoreConstants.IS_ACTIVE))
            && IEGYESNO.YES.equalsIgnoreCase(searchAddress
              .getAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS))
            && "true".equalsIgnoreCase(searchAddress
              .getAttribute(BDMDatastoreConstants.IS_SELECTED))) {
            addMailingAddress(applicationEntity, datastore, personEntity,
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
      if (BDMDatastoreConstants.RESIDENTIAL.equalsIgnoreCase(addressType)) {

        entities = personEntity.getChildEntities(
          datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));
        countryName = personEntity.getAttribute("residentialCountry");
      }
      if (BDMDatastoreConstants.MAILING.equalsIgnoreCase(addressType)) {

        entities = personEntity.getChildEntities(
          datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));
        countryName = personEntity.getAttribute("mailingCountry");
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
    final Entity personEntity, final String addressType) {

    final String personResCountry =
      personEntity.getAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY);

    if (BDMDatastoreConstants.RESIDENTIAL.equalsIgnoreCase(addressType)) {
      final Entity[] resAddresses = personEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));
      for (final Entity storedResAddress : resAddresses) {
        final String resCountry = storedResAddress
          .getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY);
        if (!personResCountry.equals(resCountry)) {
          resetAddressEntity(storedResAddress);
          storedResAddress.update();
        }
      }
      final Entity[] intlResAddresses =
        personEntity.getChildEntities(datastore
          .getEntityType(BDMDatastoreConstants.INTL_RESIDENTIAL_ADDRESS));
      for (final Entity storedIntlResAddress : intlResAddresses) {
        final String intlResCountry = storedIntlResAddress
          .getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY);
        if (!personResCountry.equals(intlResCountry)) {
          resetIntlAddressEntity(storedIntlResAddress);
          storedIntlResAddress.update();
        }
      }
    }

    final String personMailingCountry =
      personEntity.getAttribute(BDMDatastoreConstants.MAILING_COUNTRY);

    if (BDMDatastoreConstants.MAILING.equalsIgnoreCase(addressType)) {
      final Entity[] mailingAddresses = personEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));
      for (final Entity storedMailingAddress : mailingAddresses) {
        final String mailingCountry = storedMailingAddress
          .getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY);
        if (!personMailingCountry.equals(mailingCountry)) {
          resetAddressEntity(storedMailingAddress);
          storedMailingAddress.update();
        }
      }
      final Entity[] intlMailingAddresses = personEntity.getChildEntities(
        datastore.getEntityType(BDMDatastoreConstants.INTL_MAILING_ADDRESS));
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
  private void addResidentialAddress(final Entity applicationEntity,
    final Datastore datastore, final Entity personEntity,
    final Entity searchAddress) throws AppException, InformationalException {

    // Read Address data from SearchAddress for processing and updating entity

    final Entity[] residentialAddressArrya = personEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));
    Entity residentialAddress;
    if (residentialAddressArrya == null
      || residentialAddressArrya.length == 0) {
      residentialAddress = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.RESIDENTIAL_ADDRESS));
    } else {
      residentialAddress = residentialAddressArrya[0];
    }
    final String addressData =
      searchAddress.getAttribute(BDMDatastoreConstants.ADDRESS_DATA1);
    setAddressFromSearchAddress(residentialAddress, addressData);
    residentialAddress.update();
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
    final Entity searchAddress) throws AppException, InformationalException {

    // Read Address data from SearchAddress for processing and updating entity
    final Entity[] mailingAddressArrya = personEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));
    Entity mailingAddress;
    if (mailingAddressArrya == null || mailingAddressArrya.length == 0) {
      mailingAddress = datastore.newEntity(
        datastore.getEntityType(BDMDatastoreConstants.MAILING_ADDRESS));
    } else {
      mailingAddress = mailingAddressArrya[0];
    }
    final String addressData =
      searchAddress.getAttribute(BDMDatastoreConstants.ADDRESS_DATA1);
    setAddressFromSearchAddress(mailingAddress, addressData);
    mailingAddress.update();

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
      } else {
        // if Address Data is empty reset entity to empty fields// same for
        // international
        Trace.kTopLevelLogger
          .info("Address Data is Empty reset Address Entity");
        resetAddressEntity(addressEntity);
      }
    } catch (final AppException e) {
      e.printStackTrace();
    } catch (final InformationalException e) {
      e.printStackTrace();
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
