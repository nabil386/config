package curam.ca.gc.bdm.communication.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.communication.struct.BDMProcessingLocation;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.ca.gc.bdm.sl.communication.struct.BDMProcessingCenterPhoneNumbers;
import curam.codetable.ADDRESSCOUNTRY;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.ADDRESSSTATE;
import curam.codetable.COUNTRY;
import curam.codetable.COUNTRYCODE;
import curam.codetable.LOCALE;
import curam.core.fact.AddressElementFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.LocaleStruct;
import curam.piwrapper.impl.AddressDAO;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemDetails;
import curam.util.administration.struct.CodeTableItemDetailsList;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.administration.struct.PropertyDescriptionDetails;
import curam.util.administration.struct.PropertyDescriptionDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BDMCorrespondenceProcessingCentersImpl
  implements BDMCorrespondenceProcessingCenters {

  @Inject
  private AddressDAO addressDAO;

  public BDMCorrespondenceProcessingCentersImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  // property that contains the name of processing centers
  @Override
  public String getProcessingCenterName() {

    return Configuration
      .getProperty(EnvVars.ENV_PROCESSINGCENTER_NAME) == null
        ? EnvVars.ENV_PROCESSINGCENTER_NAME_DEFAULT
        : Configuration.getProperty(EnvVars.ENV_PROCESSINGCENTER_NAME);
  }

  BDMPropertiesUtil propUtil = new BDMPropertiesUtil();

  ArrayList<BDMProcessingLocation> processingCenters =
    new ArrayList<BDMProcessingLocation>();

  @Override
  public ArrayList<BDMProcessingLocation> getAll() {

    return this.processingCenters;
  }

  // Read processing center location by property name
  public BDMProcessingLocation
    readProcessingCenterFromProperty(final String propertyNameKey) {

    final BDMProcessingLocation processingLocation =
      new BDMProcessingLocation();
    processingLocation.name = Configuration.getProperty(propertyNameKey);
    final String addressAttributePattern = Configuration
      .getProperty(EnvVars.ENV_PROCESSING_LOC_ADDRESS_ATTR_PATTERN);
    processingLocation.address = mapAddressElements(
      Configuration.getProperty(propertyNameKey + addressAttributePattern));
    processingLocation.propertyName = propertyNameKey;
    return processingLocation;
  }

  // Read processing center from Property List
  @Override
  public ArrayList<BDMProcessingLocation> readAllFromPropertyList()
    throws AppException, InformationalException {

    final String processingCenterPropertyPattern =
      Configuration.getProperty(EnvVars.ENV_PROCESSING_LOC_LIST);
    final PropertyDescriptionDetailsList propertyList =
      propUtil.readPropertyList(processingCenterPropertyPattern, "");
    final Map<String, PropertyDescriptionDetails> propertiesMap =
      propUtil.getPropertyMap(propertyList);
    final String[] propertyKeys = new String[propertiesMap.size()];
    propertiesMap.keySet().toArray(propertyKeys);
    for (final String propertyKey : propertyKeys) {
      final String propertyNameMatchingPattern =
        propertyKey.replace(processingCenterPropertyPattern, "");
      if (propertyNameMatchingPattern.contains("."))
        continue;
      else {
        final BDMProcessingLocation processingLocation =
          readProcessingCenterFromProperty(propertyKey);
        processingCenters.add(processingLocation);
      }
    }

    return processingCenters;
  }

  // read processing center location to postal code map
  @Override
  public Map<String, BDMProcessingLocation> getPostalCodeMap()
    throws AppException, InformationalException {

    final Map<String, BDMProcessingLocation> postalCodeMapResult =
      new HashMap<String, BDMProcessingLocation>();
    processingCenters = this.readAllFromPropertyList();
    final String processingCenterPropertyPattern = Configuration
      .getProperty(EnvVars.ENV_PROCESSING_LOC_POSTALCODE_MAP_KEY_LIST);
    final PropertyDescriptionDetailsList propertyList =
      propUtil.readPropertyList(processingCenterPropertyPattern, "");
    final Map<String, PropertyDescriptionDetails> propertiesMap =
      propUtil.getPropertyMap(propertyList);
    final String[] propertyKeys = new String[propertiesMap.size()];
    propertiesMap.keySet().toArray(propertyKeys);
    for (final String propertyKey : propertyKeys) {
      final String postalCodeString =
        propertyKey.replace(processingCenterPropertyPattern, "");
      final String processingLocationPropertyName =
        propertiesMap.get(propertyKey).value;
      for (final BDMProcessingLocation processingCenter : processingCenters) {
        if (processingCenter.propertyName
          .equalsIgnoreCase(processingLocationPropertyName)) {
          postalCodeMapResult.put(postalCodeString.toUpperCase(),
            processingCenter);
          break;
        }
      }
    }

    return postalCodeMapResult;
  }

  // Read processing center location by country
  @Override
  public Map<String, BDMProcessingLocation> getCountryMap()
    throws AppException, InformationalException {

    final Map<String, BDMProcessingLocation> countryCodeMapResult =
      new HashMap<String, BDMProcessingLocation>();
    processingCenters = this.readAllFromPropertyList();
    final String processingCenterPropertyPattern = Configuration
      .getProperty(EnvVars.ENV_PROCESSING_LOC_COUNTRY_MAP_KEY_LIST);
    final PropertyDescriptionDetailsList propertyList =
      propUtil.readPropertyList(processingCenterPropertyPattern, "");
    final Map<String, PropertyDescriptionDetails> propertiesMap =
      propUtil.getPropertyMap(propertyList);
    final String[] propertyKeys = new String[propertiesMap.size()];
    propertiesMap.keySet().toArray(propertyKeys);
    for (final String propertyKey : propertyKeys) {
      final String postalCodeString =
        propertyKey.replace(processingCenterPropertyPattern, "");
      final String processingLocationPropertyName =
        propertiesMap.get(propertyKey).value;
      for (final BDMProcessingLocation processingCenter : processingCenters) {
        if (processingCenter.propertyName
          .equalsIgnoreCase(processingLocationPropertyName)) {
          countryCodeMapResult.put(postalCodeString.toUpperCase(),
            processingCenter);
          break;
        }
      }
    }

    return countryCodeMapResult;
  }

  // Get Address Element List
  protected AddressElementDtlsList getAddressElementList(final long addressID)
    throws AppException, InformationalException {

    // Address object and access structures
    final AddressKey addressKey = new AddressKey();
    addressKey.addressID = addressID;
    final AddressElementDtlsList addressElmList = AddressElementFactory
      .newInstance().readAddressElementDetails(addressKey);
    return addressElmList;
  }

  // get address element map with element type to address element
  @Override
  public Map<String, String> getAddressElementMap(final long addressID)
    throws AppException, InformationalException {

    final AddressElementDtlsList addressElmList =
      getAddressElementList(addressID);
    final Map<String, String> addressMap = new HashMap<String, String>();
    for (final AddressElementDtls addrElmDtls : addressElmList.dtls.items()) {
      addressMap.put(addrElmDtls.elementType, addrElmDtls.elementValue);
    }

    if (addressMap.get(ADDRESSELEMENTTYPE.COUNTRY) == null)
      addressMap.put(ADDRESSELEMENTTYPE.COUNTRY,
        addressDAO.get(addressID).getAddressDetails().countryCode);

    return addressMap;
  }

  // Get address element type
  public String getAddressElementByType(final long addressID,
    final String elementType) throws AppException, InformationalException {

    return getAddressElementMap(addressID).get(elementType);
  }

  // Get processing center location by postal code
  @Override
  public BDMProcessingLocation getProcessingCenterByPostalCode(
    final String postal) throws AppException, InformationalException {

    BDMProcessingLocation processingLoc = null;
    final Map<String, BDMProcessingLocation> postalCodeMap =
      getPostalCodeMap();

    final String[] postalCodeKeys = new String[postalCodeMap.keySet().size()];
    postalCodeMap.keySet().toArray(postalCodeKeys);

    for (final String postalCode : postalCodeKeys) {
      if (postal.toUpperCase().startsWith(postalCode.toUpperCase()))
        processingLoc = postalCodeMap.get(postalCode);
    }

    if (processingLoc == null) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEVIDENCE.ERR_MAILING_ADDRESS_FORMAT_INCORRECT),
        null, InformationalType.kError,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

      TransactionInfo.getInformationalManager().failOperation();
    }

    return processingLoc;
  }

  // Get processing center location by country
  @Override
  public BDMProcessingLocation getProcessingCenterByCountryCode(
    final String country) throws AppException, InformationalException {

    final Map<String, BDMProcessingLocation> countryCodeMap = getCountryMap();
    BDMProcessingLocation processingLoc = readProcessingCenterFromProperty(
      EnvVars.ENV_PROCESSING_LOC_INTERNATIONAL);
    final String[] countryCodeKeys =
      new String[countryCodeMap.keySet().size()];
    countryCodeMap.keySet().toArray(countryCodeKeys);

    for (final String countryCode : countryCodeKeys) {
      if (country.toUpperCase().startsWith(countryCode.toUpperCase()))
        processingLoc = countryCodeMap.get(countryCode);
    }
    return processingLoc;
  }

  // map addressdata to address element
  public String mapAddressElements(final String addressData) {

    final String addressElementMappingDelimiter = Configuration
      .getProperty(EnvVars.ENV_PROCESSING_LOC_ADDR_ELM_DELIMITER);
    final String addressElementMapping =
      Configuration.getProperty(EnvVars.ENV_PROCESSING_LOC_ADDR_ELM_MAPPING);
    final String[] addressElements =
      addressElementMapping.split(addressElementMappingDelimiter);
    final String[] addressDataElements =
      addressData.split(addressElementMappingDelimiter);
    String addressElementData = "";
    for (int i = 0; i < addressElements.length; i++) {
      addressElementData += addressElements[i] + CuramConst.gkEqualsNoSpaces
        + addressDataElements[i] + CuramConst.gkNewLine;
    }
    addressElementData.substring(0, addressElementData.length() - 1);
    return addressElementData;
  }

  // Get Canada Post address format
  public String getCanadaPostAddressFormat(final String addressData)
    throws AppException, InformationalException {

    final LocaleStruct locale = new LocaleStruct();
    locale.localeIdentifier = LOCALE.ENGLISH;
    final Map<String, String> addressElementMap =
      new HashMap<String, String>();
    final String[] addressElements = addressData.split(CuramConst.gkNewLine);
    for (final String addressLine : addressElements) {
      if (!StringUtil.isNullOrEmpty(addressLine) && addressLine
        .contains(Character.toString(CuramConst.gkEqualsNoSpaces))) {
        final String[] elements =
          addressLine.split(Character.toString(CuramConst.gkEqualsNoSpaces));
        addressElementMap.put(elements[0], elements[1]);
      }
    }
    return mapAddrElmToCanadaPostAddressFormat(addressElementMap, locale);
  }

  // map address element to Canada post address format
  @Override
  public String mapAddrElmToCanadaPostAddressFormat(
    final Map<String, String> addressElmMap, final LocaleStruct locale)
    throws AppException, InformationalException {

    final StringBuffer formattedAddress = new StringBuffer();
    if (!StringUtil
      .isNullOrEmpty(addressElmMap.get(BDMConstants.kADDRESSELEMENT_POBOX))) {
      formattedAddress.append(BDMConstants.kADDRESS_POBOX_STRING);
      formattedAddress.append(CuramConst.gkSpace);
      formattedAddress
        .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_POBOX));
    } else {
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_APTUNITNUM))) {
        formattedAddress
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_APTUNITNUM));
        formattedAddress.append(BDMConstants.kHyphen);
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNUM))) {
        formattedAddress
          .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNUM));
        formattedAddress.append(CuramConst.gkSpace);
      }
      if (!StringUtil.isNullOrEmpty(
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_STREETNAME))) {
        formattedAddress.append(addressElmMap.get(ADDRESSELEMENTTYPE.LINE2));
      }
    }
    formattedAddress.append(CuramConst.gkNewLine);
    formattedAddress
      .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_CITY));
    formattedAddress.append(CuramConst.gkSpace);
    if (addressElmMap.get(ADDRESSELEMENTTYPE.COUNTRY)
      .equals(COUNTRYCODE.CACODE)) {
      formattedAddress.append(addressElmMap.get(ADDRESSELEMENTTYPE.PROVINCE));
      formattedAddress.append(CuramConst.gkSpace);
      formattedAddress
        .append(addressElmMap.get(BDMConstants.kADDRESSELEMENT_POSTALCODE));
    } else if (addressElmMap.get(ADDRESSELEMENTTYPE.COUNTRY)
      .equals(COUNTRYCODE.USCODE)) {
      final CodeTableItemUniqueKey ctiUniqueKey =
        new CodeTableItemUniqueKey();
      ctiUniqueKey.code = addressElmMap.get(ADDRESSELEMENTTYPE.STATEPROV);
      // BUG-99851, Start
      // query the code table only if code isn't empty
      if (!StringUtil.isNullOrEmpty(ctiUniqueKey.code)) {
        // Bug 26750 - fixed codetable name -JP
        ctiUniqueKey.tableName = ADDRESSSTATE.TABLENAME;
        // Bug 26750 - fixed codetable name -JP
        ctiUniqueKey.locale = LOCALE.DEFAULTCODE;
        final CodeTableItemDetails ctiDetail = CodeTableAdminFactory
          .newInstance().readCodeTableItemDetails(ctiUniqueKey);
        final CodeTableItemDetailsList ctiDetailsList = CodeTableAdminFactory
          .newInstance().searchByCodeTableItemDescription(
            ADDRESSSTATE.TABLENAME, ctiDetail.description);
        if (ctiDetailsList.dtls.size() > 0)
          formattedAddress.append(ctiDetailsList.dtls.get(0).code);
      }
      // BUG-99851, End
      formattedAddress.append(CuramConst.gkSpace + CuramConst.gkSpace);
      formattedAddress.append(addressElmMap.get(ADDRESSELEMENTTYPE.ZIP));
    }
    if (!addressElmMap.get(ADDRESSELEMENTTYPE.COUNTRY)
      .equals(COUNTRYCODE.CACODE)) {
      formattedAddress.append(CuramConst.gkNewLine);
      final CodeTableItemUniqueKey ctiDataKey = new CodeTableItemUniqueKey();
      ctiDataKey.code =
        addressElmMap.get(BDMConstants.kADDRESSELEMENT_COUNTRY);
      // BUG-99851, Start
      // query the code table only if code isn't empty
      if (!StringUtil.isNullOrEmpty(ctiDataKey.code)) {
        ctiDataKey.tableName = ADDRESSCOUNTRY.TABLENAME;
        ctiDataKey.locale =
          locale == null || StringUtil.isNullOrEmpty(locale.localeIdentifier)
            ? LOCALE.ENGLISH : locale.localeIdentifier;
        final CodeTableItemDetails ctiDetail = readCodeTableItem(ctiDataKey);
        formattedAddress.append(ctiDetail.description);
      }
      // BUG-99851, End
    }
    return formattedAddress.toString();
  }

  // read code table item
  private CodeTableItemDetails
    readCodeTableItem(final CodeTableItemUniqueKey ctiDataKey)
      throws AppException, InformationalException {

    final CodeTableAdmin ctAdminObj = CodeTableAdminFactory.newInstance();

    return ctAdminObj.readCodeTableItemDetails(ctiDataKey);
  }

  // Get processing center location by Address ID
  @Override
  public String getProcessingLocationByAddressID(final long addressID)
    throws AppException, InformationalException {

    try {
      final String countryCode =
        getAddressElementByType(addressID, ADDRESSELEMENTTYPE.COUNTRY);
      if (countryCode.equals(COUNTRY.CA)) {
        final String postalCode =
          getAddressElementByType(addressID, ADDRESSELEMENTTYPE.POSTCODE);
        String processingCenterAddressData =
          getProcessingCenterByPostalCode(postalCode).address;
        processingCenterAddressData =
          getCanadaPostAddressFormat(processingCenterAddressData);
        return processingCenterAddressData;
      } else {
        String processingCenterAddressData =
          getProcessingCenterByCountryCode(countryCode).address;
        processingCenterAddressData =
          getCanadaPostAddressFormat(processingCenterAddressData);
        return processingCenterAddressData;
      }
    } catch (final InformationalException e) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEVIDENCE.ERR_MAILING_ADDRESS_FORMAT_INCORRECT),
        null, InformationalType.kError,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

      TransactionInfo.getInformationalManager().failOperation();
    }

    return null;
  }

  @Override
  public BDMProcessingCenterPhoneNumbers readProcessingCenterPhoneNumbers() {

    final BDMProcessingCenterPhoneNumbers processingCenterPhoneNumbers =
      new BDMProcessingCenterPhoneNumbers();
    final String phoneNumEnquiries = Configuration
      .getProperty(EnvVars.ENV_PROCESSINGCENTER_PHONE_ENQUIRIES) == null
        ? EnvVars.ENV_PROCESSINGCENTER_PHONE_ENQUIRIES_DEFAULT : Configuration
          .getProperty(EnvVars.ENV_PROCESSINGCENTER_PHONE_ENQUIRIES);
    processingCenterPhoneNumbers.phoneNumberEnquiries = phoneNumEnquiries;

    final String phoneNumTTY = Configuration
      .getProperty(EnvVars.ENV_PROCESSINGCENTER_PHONE_TTY) == null
        ? EnvVars.ENV_PROCESSINGCENTER_PHONE_TTY_DEFAULT
        : Configuration.getProperty(EnvVars.ENV_PROCESSINGCENTER_PHONE_TTY);
    processingCenterPhoneNumbers.phoneNumberTTY = phoneNumTTY;

    final String phoneNumIntl = Configuration
      .getProperty(EnvVars.ENV_PROCESSINGCENTER_PHONE_INTL) == null
        ? EnvVars.ENV_PROCESSINGCENTER_PHONE_INTL_DEFAULT
        : Configuration.getProperty(EnvVars.ENV_PROCESSINGCENTER_PHONE_INTL);
    processingCenterPhoneNumbers.phoneNumIntl = phoneNumIntl;
    return processingCenterPhoneNumbers;
  }
}
