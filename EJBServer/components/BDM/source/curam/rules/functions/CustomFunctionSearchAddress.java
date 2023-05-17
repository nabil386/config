package curam.rules.functions;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.rest.bdmwsaddressapi.struct.BDMWSAddressSearchKey;
import curam.ca.gc.bdm.sl.bdmaddress.fact.BDMAddressSearchFactory;
import curam.ca.gc.bdm.sl.bdmaddress.intf.BDMAddressSearch;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressDetailsStruct;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchDetails;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchDetailsList;
import curam.ca.gc.bdm.util.impl.BDMAddressUtil;
import curam.codetable.COUNTRYCODE;
import curam.codetable.IEGYESNO;
import curam.core.impl.EnvVars;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;

/**
 * Custom Function to to Search Address using postal code Residential address.
 * - External service Search : Invokes WS Address to Fetch the address
 * provided.
 *
 * @curam.exclude
 */
public class CustomFunctionSearchAddress
  extends CustomFunctionValidateAddress {

  /** The PO Box address prefix. */
  public static final String PO_BOX_PREFIX = "PO ";

  public static final String ID_ADD_MANUAL = "-2";

  /**
   * Instantiates a new custom function validate residential address.
   */
  public CustomFunctionSearchAddress() {

    super();

  }

  /**
   * A custom function that will be called to fetch the address by using postal
   * Address.
   *
   * This Custom function will be used for Residential as well as mailing
   * address.
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

    final IEG2Context ieg2context = (IEG2Context) rulesParameters;
    final long rootEntityID = ieg2context.getRootEntityID();

    final IEG2ExecutionContext ieg2ExecutionContext =
      new IEG2ExecutionContext(rulesParameters);

    final Datastore datastore = ieg2ExecutionContext.getDatastore();

    final Entity applicationEntity = datastore.readEntity(rootEntityID);

    final Entity personEntity = applicationEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.PERSON))[0];

    int index = 0;
    final String country = getOptionalStringParam(rulesParameters, index++);
    final String addressType =
      getOptionalStringParam(rulesParameters, index++);
    final String postalCode =
      getOptionalStringParam(rulesParameters, index++);

    // The following verifications must be in order to avoid errors.
    if (!StringUtil.isNullOrEmpty(postalCode)
      && COUNTRYCODE.CACODE.equalsIgnoreCase(country)) {

      if (!Configuration.getBooleanProperty(
        EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT)) {
        // return response;
      }
      final BDMWSAddressSearchKey bdmWSAddressSearchKey =
        new BDMWSAddressSearchKey();

      bdmWSAddressSearchKey.postalCode = postalCode;

      final BDMAddressSearch bdmAddressSearch =
        BDMAddressSearchFactory.newInstance();
      final BDMAddressDetailsStruct addressDetails =
        new BDMAddressDetailsStruct();
      addressDetails.postCode = postalCode;
      final BDMAddressSearchDetailsList bdmAddressSearchDetailsList =
        bdmAddressSearch.searchAddressData(addressDetails);
      curam.util.resources.Trace.kTopLevelLogger.info(
        "Number of Address" + bdmAddressSearchDetailsList.detailsList.size());

      setAddressSearchData(datastore, personEntity,
        bdmAddressSearchDetailsList, addressType, country);
    }

    return AdaptorFactory.getBooleanAdaptor(true);

  }

  /**
   *
   * @param datastore
   * @param personEntity
   * @param bdmAddressSearchDetailsList
   * @param adddressType
   * @param country
   * @return
   * @throws AppException
   */

  private void setAddressSearchData(final Datastore datastore,
    final Entity personEntity,
    final BDMAddressSearchDetailsList bdmAddressSearchDetailsList,
    final String adddressType, final String country) {

    final Entity[] entities = personEntity.getChildEntities(
      datastore.getEntityType(BDMDatastoreConstants.SEARCH_ADDRESS));
    Entity searchAddressEntity;
    if (entities != null && entities.length > 0) {
      for (final Entity oldSearchEntity : entities) {
        // Reset Search Address to Inactive for old Search Address
        oldSearchEntity.setAttribute(BDMDatastoreConstants.IS_ACTIVE,
          IEGYESNO.NO);
        oldSearchEntity.update();

      }

    }
    if (COUNTRYCODE.CACODE.equalsIgnoreCase(country)) {
      Trace.kTopLevelLogger
        .info("Address Type" + adddressType + " No of Adddres Found :"
          + bdmAddressSearchDetailsList.detailsList.size());
      // Set Data from WS response
      for (final BDMAddressSearchDetails bdmAddressSearchDetails : bdmAddressSearchDetailsList.detailsList) {

        searchAddressEntity = datastore.newEntity(
          datastore.getEntityType(BDMDatastoreConstants.SEARCH_ADDRESS));
        searchAddressEntity.setAttribute(BDMDatastoreConstants.IS_ACTIVE,
          IEGYESNO.YES); // Set Address to Active
        searchAddressEntity.setAttribute(
          BDMDatastoreConstants.FORMATTED_ADDRESS_DATA,
          bdmAddressSearchDetails.formattedAddressData);
        searchAddressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_DATA1,
          bdmAddressSearchDetails.addressData);
        searchAddressEntity.setAttribute(
          BDMDatastoreConstants.ID_MAILING_ADDRESS, ID_ADD_MANUAL);
        if (BDMDatastoreConstants.RESIDENTIAL
          .equalsIgnoreCase(adddressType)) {
          searchAddressEntity.setAttribute(
            BDMDatastoreConstants.IS_RESIDENTIAL_ADDRESS, IEGYESNO.YES);
        } else if (BDMDatastoreConstants.MAILING
          .equalsIgnoreCase(adddressType)) {
          searchAddressEntity.setAttribute(
            BDMDatastoreConstants.IS_MAILING_ADDRESS, IEGYESNO.YES);
        }
        personEntity.addChildEntity(searchAddressEntity);
      }
    }
    // If no response found set empty address and add option to Add new address

    searchAddressEntity = datastore.newEntity(
      datastore.getEntityType(BDMDatastoreConstants.SEARCH_ADDRESS));
    searchAddressEntity.setAttribute(BDMDatastoreConstants.IS_ACTIVE,
      IEGYESNO.YES); // Set Address to Active
    searchAddressEntity
      .setAttribute(BDMDatastoreConstants.FORMATTED_ADDRESS_DATA, "Add New");
    searchAddressEntity.setAttribute(BDMDatastoreConstants.ID_MAILING_ADDRESS,
      ID_ADD_MANUAL);
    if (!COUNTRYCODE.CACODE.equalsIgnoreCase(country)) {
      // Select the empty address and proceed if country is not CA
      searchAddressEntity.setAttribute(BDMDatastoreConstants.IS_SELECTED,
        "true");
    }
    try {
      searchAddressEntity.setAttribute(BDMDatastoreConstants.ADDRESS_DATA1,
        new BDMAddressUtil().getEmptyAddressData());
    } catch (final Exception e) {

      e.printStackTrace();
    }
    if (BDMDatastoreConstants.RESIDENTIAL.equalsIgnoreCase(adddressType)) {
      searchAddressEntity.setAttribute(
        BDMDatastoreConstants.IS_RESIDENTIAL_ADDRESS, IEGYESNO.YES);

    } else if (BDMDatastoreConstants.MAILING.equalsIgnoreCase(adddressType)) {
      searchAddressEntity
        .setAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS, IEGYESNO.YES);
    }
    personEntity.addChildEntity(searchAddressEntity);

    personEntity.update();

    // TODO Auto-generated method stub

  }

}
