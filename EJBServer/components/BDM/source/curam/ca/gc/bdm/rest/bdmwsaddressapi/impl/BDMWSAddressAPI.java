package curam.ca.gc.bdm.rest.bdmwsaddressapi.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOADDRESSVALIDATOR;
import curam.ca.gc.bdm.message.impl.BDMRESTAPIERRORMESSAGEExceptionCreator;
import curam.ca.gc.bdm.rest.bdmwsaddressapi.struct.BDMWSAddress;
import curam.ca.gc.bdm.rest.bdmwsaddressapi.struct.BDMWSAddressList;
import curam.ca.gc.bdm.rest.bdmwsaddressapi.struct.BDMWSAddressSearchKey;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.BDMWSAddressInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WSAddress;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WsaddrAddressMatch;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WsaddrSearchRequest;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.intf.BDMWSAddressInterfaceIntf;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressConstants;
import curam.codetable.LOCALE;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class BDMWSAddressAPI, provides a search of address by postal code
 * method.
 * if environment variable
 * <code> BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT</code>
 * is disable this api returns an empty list.
 */
public class BDMWSAddressAPI
  implements curam.ca.gc.bdm.rest.bdmwsaddressapi.intf.BDMWSAddressAPI {

  /** The Constant for French language. */
  private static final String LANGUAGE_FRENCH = "French";

  /** The Constant for English language. */
  private static final String LANGUAGE_ENGLISH = "English";

  /** The Constant for English locale. */
  private static final String LOCALE_EN = "en";

  /** The Constant for the Default Country. */
  private static final String DEFAULT_COUNTRY = "CAN";

  /**
   * Instantiates a new BDM Address API.
   */
  public BDMWSAddressAPI() {

    super();

  }

  /*
   * (non-Javadoc)
   *
   * @see
   * curam.ca.gc.bdm.sl.intf.BDMWSAddressAPI#searchAddress(curam.ca.gc.bdm.sl
   * .struct.BDMWSAddressSearchKey)
   */
  @Override
  public BDMWSAddressList
    searchAddress(final BDMWSAddressSearchKey addressSearchKey)
      throws AppException, InformationalException {

    final BDMWSAddressList response = new BDMWSAddressList();

    if (!Configuration
      .getBooleanProperty(EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT)) {
      return response;
    }

    final String country = StringUtil.isNullOrEmpty(addressSearchKey.country)
      ? DEFAULT_COUNTRY : addressSearchKey.country;
    final String language = getLanguage(addressSearchKey);
    final String postalCode = addressSearchKey.postalCode;

    validateQueryParamsForSearchAddress(postalCode, country, language);
    final List<BDMWSAddress> addressList =
      wsSearchAddress(postalCode, country, language);
    response.data.addAll(addressList);

    return response;
  }

  /**
   * Validate query params for search address.
   *
   * @param postalCode the postal code
   * @param country the country
   * @param language the language
   * @throws AppException the app exception
   */
  private void validateQueryParamsForSearchAddress(final String postalCode,
    final String country, final String language) throws AppException {

    if (StringUtil.isNullOrEmpty(postalCode)
      || StringUtil.isNullOrEmpty(country)
      || !country.toUpperCase().equals(DEFAULT_COUNTRY)
      || !(language.equals(BDMConstants.kAddress_Search_Locale_en_CA)
        || language.equals(BDMConstants.kAddress_Search_Locale_fr_CA))) {
      throw BDMRESTAPIERRORMESSAGEExceptionCreator
        .HTTP_400_SEARCH_ADDRESS_QUERY_PARAMS_NOT_VALID();
    }

  }

  /**
   * Gets the language. If the language is not provided language of the
   * transaction is
   * return.
   *
   * @param addressSearchKey the address search key
   * @return the language
   */
  private String getLanguage(final BDMWSAddressSearchKey addressSearchKey) {

    final String programLocale =
      TransactionInfo.getProgramLocale().toLowerCase();
    String searchAddressLanguageRequest = "";
    if (programLocale.equalsIgnoreCase(LOCALE.ENGLISH)
      || programLocale.equalsIgnoreCase(LOCALE.ENGLISH_CA)) {
      searchAddressLanguageRequest =
        BDMConstants.kAddress_Search_Locale_en_CA;
    } else if (programLocale.equalsIgnoreCase(LOCALE.FRENCH)) {
      searchAddressLanguageRequest =
        BDMConstants.kAddress_Search_Locale_fr_CA;
    } else {
      searchAddressLanguageRequest =
        BDMConstants.kAddress_Search_Locale_en_CA;
    }
    return StringUtil.isNullOrEmpty(addressSearchKey.language)
      ? searchAddressLanguageRequest : addressSearchKey.language;

  }

  /**
   *
   * @param postalCode
   * @param country
   * @param language
   * @return
   * @throws AppException
   */
  private List<BDMWSAddress> wsSearchAddress(final String postalCode,
    final String country, final String language) throws AppException {

    final List<BDMWSAddress> addressList = new ArrayList<BDMWSAddress>();
    BDMWSAddress bdmWSAddress;
    // BEGIN: CALL BAD Address REST API

    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode(postalCode);
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails.setNcLanguageName(language);

    try {
      final BDMWSAddressInterfaceIntf wsAddressService =
        new BDMWSAddressInterfaceImpl();
      final WSAddress wsAddress =
        wsAddressService.searchAddressesByPostalCode(requestDetails);

      if (null != wsAddress) {
        for (final WsaddrAddressMatch wsAddressMatch : wsAddress
          .getWsaddrSearchResults().getWsaddrAddressMatches()) {
          // BEGIN - Bug 20457 - same address appears multiple time
          bdmWSAddress = getAddressData(wsAddressMatch);
          if (addressList.isEmpty()) {
            addressList.add(bdmWSAddress);
          } else {
            for (final BDMWSAddress bdmWSAddressItem : addressList) {
              if (isDifferentAddress(bdmWSAddressItem, bdmWSAddress)) {
                addressList.add(bdmWSAddress);
                break;
              }
            }
          }
          // END - Bug 20457 - same address appears multiple time
        }
      }

      // CONTINUE HERE
    } catch (final Exception e) {
      final LocalisableString localString = new LocalisableString(
        BDMBPOADDRESSVALIDATOR.INF_NO_SEARCH_ADDRESS_FOUND);
    }

    // END : Call BAD Address REST API

    return addressList;
  }

  /**
   *
   * @param wsAddressMatch
   * @return BDMWSAddress
   */
  private BDMWSAddress getAddressData(final WsaddrAddressMatch wsAddress) {

    final BDMWSAddress addressObj = new BDMWSAddress();
    addressObj.city = wsAddress.getNcAddressCityName();
    addressObj.country = wsAddress.getNcCountryCode();
    addressObj.postalCode = wsAddress.getNcAddressPostalCode();
    addressObj.province = wsAddress.getCanProvinceCode();
    addressObj.streetName = wsAddress.getNcStreetName();
    addressObj.streetType = wsAddress.getCanStreetCategoryCode();
    addressObj.streetDirection = wsAddress.getCanStreetDirectionalCode();
    addressObj.deliveryInstallationDescription =
      wsAddress.getWsaddrDeliveryInstallationDescription();
    addressObj.lockboxNumberMaximum =
      wsAddress.getWsaddrLockBoxNumberMaximumText();
    addressObj.lockboxNumberMinimum =
      wsAddress.getWsaddrLockBoxNumberMinimumText();
    addressObj.routeServiceType = isRouralRouteAddress(wsAddress)
      ? wsAddress.getWsaddrRuralRouteServiceCategory()
      : wsAddress.getWsaddrRouteServiceCategory();
    addressObj.routeServiceNumber = isRouralRouteAddress(wsAddress)
      ? wsAddress.getWsaddrRuralRouteServiceNumber()
      : wsAddress.getWsaddrRouteServiceNumber();
    addressObj.addressType = wsAddress.getNcAddressCategoryText();
    addressObj.directoryAreaName = wsAddress.getWsaddrDirectoryAreaName();
    addressObj.streetNumberMaximum =
      wsAddress.getWsaddrStreetNumberMaximumText();
    addressObj.streetNumberMinimum =
      wsAddress.getWsaddrStreetNumberMinimumText();
    // BEGIN TASK- 26499 26497 Suit Number Mapping
    addressObj.suiteNumberMaximum =
      wsAddress.getWsaddrSuiteNumberMaximumText();
    addressObj.suiteNumberMinimum =
      wsAddress.getWsaddrSuiteNumberMinimumText();
    // END TASK-26499
    return addressObj;

  }

  /**
   * Checks if is roural route address.
   *
   * @param wsAddress the ws address
   * @return true, if is roural route address
   */
  private boolean isRouralRouteAddress(final WsaddrAddressMatch wsAddress) {

    return !StringUtil.isNullOrEmpty(wsAddress.getNcAddressCategoryText())
      && wsAddress.getNcAddressCategoryText()
        .equals(BDMWSAddressConstants.kAddressTypeRuralRoute);
  }

  /**
   * Checks if two addresses are different
   *
   * @param bdmWSAddress1
   * @param bdmWSAddress2
   * @return true, if the two addresses are different
   */
  private boolean isDifferentAddress(final BDMWSAddress bdmWSAddress1,
    final BDMWSAddress bdmWSAddress2) {

    // BEGIN 25554: PO BOX IS NOT POPULATED- Added a new field in Address Format
    // for PO BOX
    // Added a Null to include address where fields are null e.g PO BOX address
    return !bdmWSAddress1.addressType.equals(bdmWSAddress2.addressType)
      || !bdmWSAddress1.city.equals(bdmWSAddress2.city)
      || !bdmWSAddress1.directoryAreaName
        .equals(bdmWSAddress2.directoryAreaName)
      || !bdmWSAddress1.postalCode.equals(bdmWSAddress2.postalCode)
      || !bdmWSAddress1.province.equals(bdmWSAddress2.province)
      || !bdmWSAddress1.streetAddressSequence
        .equals(bdmWSAddress2.streetAddressSequence)
      || bdmWSAddress1.streetName != null && bdmWSAddress2.streetName != null
        && !bdmWSAddress1.streetName.equals(bdmWSAddress2.streetName)
      || bdmWSAddress1.streetNumberMaximum != null
        && bdmWSAddress2.streetNumberMaximum != null
        && !bdmWSAddress1.streetNumberMaximum
          .equals(bdmWSAddress2.streetNumberMaximum)
      || bdmWSAddress1.streetNumberMinimum != null
        && bdmWSAddress2.streetNumberMinimum != null
        && !bdmWSAddress1.streetNumberMinimum
          .equals(bdmWSAddress2.streetNumberMinimum)
      || bdmWSAddress1.streetType != null && bdmWSAddress2.streetType != null
        && !bdmWSAddress1.streetType.equals(bdmWSAddress2.streetType);

    // END TASK-25554
  }
}
