package curam.ca.gc.bdm.sl.bdmaddress.impl;

import curam.ca.gc.bdm.address.impl.BDMAddressFormatINTL;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOADDRESSVALIDATOR;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressDetailsStruct;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchDetails;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchDetailsList;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchResult;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.BDMWSAddressInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos.WsaddrValidationRequest;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WSAddress;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WsaddrAddressMatch;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WsaddrSearchRequest;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.intf.BDMWSAddressInterfaceIntf;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressConstants;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.COUNTRY;
import curam.codetable.LOCALE;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AddressFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.Address;
import curam.core.intf.AddressData;
import curam.core.sl.impl.Constants;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.ElementDetails;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.resources.Configuration;
import curam.util.resources.GeneralConstants;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;

/**
 * <p>
 * Implements methods used for address validation and verification.
 * </p>
 */
public abstract class BDMAddressSearch
  extends curam.ca.gc.bdm.sl.bdmaddress.base.BDMAddressSearch {

  /**
   * Constructor.
   */
  public BDMAddressSearch() {

    super();

  }

  /**
   * Search the address with the address register interface.
   *
   * @param details Contains address details for search.
   *
   * @return BDMAddressSearchResultDetails structure containing the search
   * address
   * result list of entered address and the address list verified through the
   * interface.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  @Override
  public BDMAddressSearchResult
    searchAddress(final BDMAddressDetailsStruct details)
      throws AppException, InformationalException {

    // Return structure.
    final BDMAddressSearchResult addressSearchResult =
      new BDMAddressSearchResult();
    addressSearchResult.searchAddresses = searchAddressData(details);

    return addressSearchResult;

  }

  /**
   * Validates the address details.
   *
   * @param addressDetails Contains address details for verification.
   *
   * @throws InformationalException
   * Generic exception signature.
   * @throws AppException
   * Generic exception signature.
   */
  @Override
  public void
    validateAddressData(final BDMAddressDetailsStruct addressDetails)
      throws InformationalException, AppException {

    boolean isAddressValid = false;
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // final checks whether the final BDMWSAddress.Search is enabled
    if (Configuration
      .getBooleanProperty(EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT)) {

      /* BEGIN - TASK 9862 - Expose WSAddressRESTAPI for Client portal */
      final BDMWSAddressInterfaceIntf wsAddressImplObj =
        new BDMWSAddressInterfaceImpl();
      WsaddrValidationRequest addressKey = new WsaddrValidationRequest();
      addressKey = setWSAddressDataForValidate(addressDetails.addressData);

      isAddressValid = wsAddressImplObj.validateAddress(addressKey);
    }
    // TODO Calling code must get boolean value
    /* END - TASK 9862 - Expose WSAddressRESTAPI for Client portal */

  }

  /**
   * Search the address details.
   *
   * @param addressDetails Contains address details for verification.
   *
   * @return BDMAddressDetailsStructList Search address list.
   *
   * @throws InformationalException
   * Generic exception signature.
   * @throws AppException
   * Generic exception signature.
   */
  @Override
  public BDMAddressSearchDetailsList

    searchAddressData(final BDMAddressDetailsStruct addressDetails)
      throws InformationalException, AppException {

    // Verified address return list
    BDMAddressSearchDetailsList addressSearchDetailsList =
      new BDMAddressSearchDetailsList();

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();
    WSAddress wsAddress = new WSAddress();
    // checks whether the BDMWSAddress.Search is enabled
    if (Configuration
      .getBooleanProperty(EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT)) {

      // BEGIN: CALL BAD Address REST API

      final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
      requestDetails.setNcAddressPostalCode(addressDetails.postCode);
      requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
      requestDetails.setNcLanguageName(getLanguage());

      try {
        final BDMWSAddressInterfaceIntf wsAddressService =
          new BDMWSAddressInterfaceImpl();
        wsAddress =
          wsAddressService.searchAddressesByPostalCode(requestDetails);
        // CONTINUE HERE
      } catch (final Exception e) {

        final LocalisableString localString = new LocalisableString(
          BDMBPOADDRESSVALIDATOR.INF_NO_SEARCH_ADDRESS_FOUND);
        // BEGIN TASK-23721 BUG 21593 -AgentPortal Warning Message to User when
        // WS is down
        // Map the read Exception Message to information Manager
        informationalManager.addInformationalMsg(localString,
          GeneralConstants.kEmpty, InformationalType.kWarning);
        Trace.kTopLevelLogger.error(e.getMessage(), e);
        e.printStackTrace();

        // END TASK-23721
      }

      // END : Call BAD Address REST API

    }
    // Return list of addresses.
    if (null != wsAddress) {
      addressSearchDetailsList = mapJsonPojoToStruct(wsAddress);
    }
    return addressSearchDetailsList;
  }

  /**
   * Gets the language. language of the transaction is return.
   *
   * @param addressSearchKey the address search key
   * @return the language
   */
  private String getLanguage() {

    final String programLocale =
      TransactionInfo.getProgramLocale().toLowerCase();
    String languageForWSAddress = "";
    if (programLocale.equalsIgnoreCase(LOCALE.ENGLISH)
      || programLocale.equalsIgnoreCase(LOCALE.ENGLISH_CA)) {
      languageForWSAddress = BDMConstants.kAddress_Search_Locale_en_CA;
    } else if (programLocale.equalsIgnoreCase(LOCALE.FRENCH)) {
      languageForWSAddress = BDMConstants.kAddress_Search_Locale_fr_CA;
    } else {
      languageForWSAddress = BDMConstants.kAddress_Search_Locale_en_CA;
    }
    return languageForWSAddress;

  }

  /**
   *
   * @param jsonPojo
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMAddressSearchDetailsList mapJsonPojoToStruct(
    final WSAddress jsonPojo) throws AppException, InformationalException {

    final BDMAddressSearchDetailsList addressSearchDetailsList =
      new BDMAddressSearchDetailsList();
    if (jsonPojo.getWsaddrSearchResults() != null) {
      for (final WsaddrAddressMatch wsAddressMatch : jsonPojo
        .getWsaddrSearchResults().getWsaddrAddressMatches()) {
        addressSearchDetailsList.detailsList
          .add(getAddressData(wsAddressMatch));

      }
    }
    return addressSearchDetailsList;

  }

  /**
   * Retrieves address details from the rest service.
   *
   * @param key Contains address details from the web service.
   * @return Structure containing address details.
   *
   * @throws InformationalException
   * Generic exception signature.
   * @throws AppException
   * Generic exception signature.
   */
  private BDMAddressSearchDetails
    getAddressData(final WsaddrAddressMatch wsAddressMatch)
      throws AppException, InformationalException {

    // Return structure
    final BDMAddressSearchDetails addressSearchDetails =
      new BDMAddressSearchDetails();

    final AddressData addressDataObj = AddressDataFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();

    // This is for CA address.
    addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.countryCode = COUNTRY.CA;

    final String addressCategoryText =
      wsAddressMatch.getNcAddressCategoryText();

    if (addressCategoryText
      .equalsIgnoreCase(BDMWSAddressConstants.kAddressTypeUrbanAddress)
      || addressCategoryText
        .equalsIgnoreCase(BDMWSAddressConstants.kAddressTypeUrbanRoute)) {

      final String suiteNumMin =
        wsAddressMatch.getWsaddrSuiteNumberMinimumText();
      final String suiteNumMax =
        wsAddressMatch.getWsaddrSuiteNumberMaximumText();
      String suiteNumber = "";
      final String streetNumMin =
        wsAddressMatch.getWsaddrStreetNumberMinimumText();
      final String streetNumMax =
        wsAddressMatch.getWsaddrStreetNumberMaximumText();
      String streetNumber = "";
      final String streetName = wsAddressMatch.getNcStreetName();
      final String streetType =
        null == wsAddressMatch.getCanStreetCategoryCode() ? ""
          : wsAddressMatch.getCanStreetCategoryCode();
      String streetDirection = CuramConst.gkEmpty;

      if (!StringUtil
        .isNullOrEmpty(wsAddressMatch.getCanStreetDirectionalCode())) {
        streetDirection = wsAddressMatch.getCanStreetDirectionalCode();
      }
      // setting the suite number
      if (suiteNumMin != null && suiteNumMax != null
        && !suiteNumMin.equalsIgnoreCase(suiteNumMax)) {
        suiteNumber = suiteNumMin + BDMWSAddressConstants.kDash + suiteNumMax;
      } else if (suiteNumMin != null && suiteNumMax != null
        && suiteNumMin.equalsIgnoreCase(suiteNumMax)) {
        suiteNumber = suiteNumMin;
      }

      // setting the street number
      if (streetNumMin != null && streetNumMax != null
        && !streetNumMin.equalsIgnoreCase(streetNumMax)) {
        streetNumber =
          streetNumMin + BDMWSAddressConstants.kDash + streetNumMax;
      } else if (streetNumMin != null && streetNumMax != null
        && streetNumMin.equalsIgnoreCase(streetNumMax)) {
        streetNumber = streetNumMin;
      }

      // setting the addressLine1 and suiteNum
      addressFieldDetails.addressLine1 = streetNumber;
      addressFieldDetails.suiteNum = suiteNumber;

      // setting addressLine2
      // BEGIN TASK-56285 Address line 2 from Client Portal and AgentPortal Does
      // not match
      addressFieldDetails.addressLine2 =
        streetName.concat(Constants.kSpace).concat(streetType)
          .concat(Constants.kSpace).concat(streetDirection).trim();
      // END TASK-56285

    } else if (addressCategoryText.equalsIgnoreCase(
      BDMWSAddressConstants.kAddressTypeRuralGeneralDelivery)) {
      // BEGIN 25554: PO BOX IS NOT POPULATED- Added a new field in Address
      // Format for PO BOX
      addressFieldDetails.addressLine3 = BDMWSAddressConstants.kGD;
    } else if (addressCategoryText
      .equalsIgnoreCase(BDMWSAddressConstants.kAddressTypeRuralRoute)) {

      String ruralRouteServiceType =
        wsAddressMatch.getWsaddrRuralRouteServiceCategory();
      final String ruralRouteServiceNumber =
        wsAddressMatch.getWsaddrRuralRouteServiceNumber();
      // Added a Mapping as per FDD for routeServiceCategory when Address
      // Category of type RuralRoute
      if (ruralRouteServiceType.equalsIgnoreCase(
        BDMWSAddressConstants.kRuralRouteServiceTypeValue)) {
        ruralRouteServiceType = BDMWSAddressConstants.kRR;
      }
      if (ruralRouteServiceType.equalsIgnoreCase(
        BDMWSAddressConstants.kRuralRouteServiceCategory_SuburbanRoute)) {
        ruralRouteServiceType =
          BDMWSAddressConstants.kRuralRouteServiceCategory_SuburbanRoute_Value;
      }
      if (ruralRouteServiceType.equalsIgnoreCase(
        BDMWSAddressConstants.kRuralRouteServiceCategory_MobileRoute)) {
        ruralRouteServiceType =
          BDMWSAddressConstants.kRuralRouteServiceCategory_MobileRoute;
      }
      if (ruralRouteServiceType.equalsIgnoreCase(
        BDMWSAddressConstants.kRuralRouteServiceCategory_GeneralDelivery)) {
        ruralRouteServiceType =
          BDMWSAddressConstants.kRuralRouteServiceCategory_GeneralDelivery;
      }

      addressFieldDetails.addressLine3 = ruralRouteServiceType
        .concat(Constants.kSpace).concat(ruralRouteServiceNumber).trim();
      // END 25554: PO BOX IS NOT POPULATED- Added a new field in Address Format
      // for PO BOX
    } else if (addressCategoryText
      .equalsIgnoreCase(BDMWSAddressConstants.kAddressTypeRuralLockBox)) {

      String deliveryInstallationDescription =
        wsAddressMatch.getWsaddrDeliveryInstallationDescription();
      if (deliveryInstallationDescription.equals(BDMWSAddressConstants.kPO)) {
        deliveryInstallationDescription = BDMWSAddressConstants.kPOBoxFull;
      }
      final String lockBoxMin =
        wsAddressMatch.getWsaddrLockBoxNumberMinimumText();
      final String lockBoxMax =
        wsAddressMatch.getWsaddrLockBoxNumberMaximumText();
      String lockBoxNumber = "";

      // setting the lockBoxNumber
      if (lockBoxMin != null && lockBoxMax != null
        && !lockBoxMin.equalsIgnoreCase(lockBoxMax)) {
        lockBoxNumber = lockBoxMin + BDMWSAddressConstants.kDash + lockBoxMax;
      } else if (lockBoxMin != null && lockBoxMax != null
        && lockBoxMin.equalsIgnoreCase(lockBoxMax)) {
        lockBoxNumber = lockBoxMin;
      }
      // SEt it to Address line 3 for PO box
      addressFieldDetails.addressLine3 = deliveryInstallationDescription
        .concat(Constants.kSpace).concat(lockBoxNumber).trim();
    }

    // setting the common address fields
    addressFieldDetails.city = wsAddressMatch.getNcAddressCityName();
    addressFieldDetails.province = wsAddressMatch.getCanProvinceCode();
    addressFieldDetails.postalCode = wsAddressMatch.getNcAddressPostalCode();

    // Add white space in the middle of the postal code
    addressFieldDetails.postalCode =
      new StringBuffer(addressFieldDetails.postalCode)
        .insert(addressFieldDetails.postalCode.length() - 3, Constants.kSpace)
        .toString();

    final OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);

    final Address addressObj = AddressFactory.newInstance();

    // provide otherAddressData in the order expected by the wsAddress fields
    final BDMAddressFormatINTL bdmAddressFormatINTLobj =
      new BDMAddressFormatINTL();
    final OtherAddressData bdmOrderedAddressData =
      bdmAddressFormatINTLobj.parseFieldsToData(addressFieldDetails);

    // Get formatted address data.
    final OtherAddressData formattedAddressData =
      addressObj.getShortFormat(otherAddressData);

    addressSearchDetails.addressData = bdmOrderedAddressData.addressData;
    addressSearchDetails.formattedAddressData =
      formattedAddressData.addressData;

    return addressSearchDetails;

  }

  /**
   * Converts addressData into WSAddress
   *
   * @param addressData
   * @return WsaddrValidationRequest
   */
  private WsaddrValidationRequest setWSAddressDataForValidate(
    final String addressData) throws AppException, InformationalException {

    final WsaddrValidationRequest wsAddrValidationRequestObj =
      new WsaddrValidationRequest();

    final OtherAddressData otherAddressData = new OtherAddressData();
    otherAddressData.addressData = addressData;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressMapList addressMapList =
      addressDataObj.parseDataToMap(otherAddressData);

    final AddressMap addressMap = new AddressMap();
    ElementDetails elementDetails;

    final StringBuffer addressFullText = new StringBuffer();
    addressMap.name = ADDRESSELEMENTTYPE.APT;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    addressFullText.append(elementDetails.elementValue);
    addressFullText.append(CuramConst.gkSpace);

    addressMap.name = ADDRESSELEMENTTYPE.STREET_NUMBER;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    addressFullText.append(elementDetails.elementValue);
    addressFullText.append(CuramConst.gkSpace);

    addressMap.name = ADDRESSELEMENTTYPE.LINE1;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    addressFullText.append(elementDetails.elementValue);
    addressFullText.append(CuramConst.gkSpace);

    addressMap.name = ADDRESSELEMENTTYPE.LINE2; // TODO Check if that works
                                                // during integration
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    addressFullText.append(elementDetails.elementValue);

    if (addressFullText != null) {
      wsAddrValidationRequestObj
        .setNcAddressFullText(addressFullText.toString());
    }

    addressMap.name = ADDRESSELEMENTTYPE.CITY;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    wsAddrValidationRequestObj
      .setNcAddressCityName(elementDetails.elementValue);

    addressMap.name = ADDRESSELEMENTTYPE.PROVINCE;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    wsAddrValidationRequestObj
      .setCanProvinceCode(elementDetails.elementValue);

    addressMap.name = ADDRESSELEMENTTYPE.COUNTRY;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    wsAddrValidationRequestObj.setNcCountryCode(elementDetails.elementValue);

    addressMap.name = ADDRESSELEMENTTYPE.POSTCODE;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    wsAddrValidationRequestObj
      .setNcAddressPostalCode(elementDetails.elementValue);

    return wsAddrValidationRequestObj;

  }
}
