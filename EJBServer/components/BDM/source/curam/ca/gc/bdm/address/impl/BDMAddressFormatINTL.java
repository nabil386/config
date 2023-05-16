
package curam.ca.gc.bdm.address.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOADDRESS;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.ADDRESSSTATE;
import curam.codetable.COUNTRY;
import curam.codetable.PROVINCETYPE;
import curam.core.fact.AddressDataFactory;
import curam.core.impl.AddressUtil;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.struct.AddressString;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressLineList;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.AddressTagDetails;
import curam.core.struct.ElementDetails;
import curam.core.struct.FormatPostalCode;
import curam.core.struct.OtherAddressData;
import curam.core.struct.ValidateAddressResult;
import curam.message.BPOADDRESS;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import java.util.HashMap;
import java.util.Map;
import org.apache.xerces.impl.xpath.regex.RegularExpression;

/*
 *
 * Gets the address into different format.
 * This class can also validate postal code and address.
 *
 */
public class BDMAddressFormatINTL
  extends curam.core.address.impl.AddressFormat {

  // START CUSTOMISATION
  protected static final short kESDCCAAddressElementMinLength = 2;
  // END CUSTOMISATION

  /**
   * Order of tags for CA addresses.
   */
  private final String[] caTags = {
    /*
     * BEGIN: Task 19263 - Rearrange AddressElement Order to match expected UA
     * format
     */
    /* BEGIN: standard elements */
    ADDRESSELEMENTTYPE.POBOXNO, ADDRESSELEMENTTYPE.APT,
    ADDRESSELEMENTTYPE.LINE1, ADDRESSELEMENTTYPE.LINE2,
    ADDRESSELEMENTTYPE.CITY,
    /* END: standard elements */
    /* Canadian province element */
    ADDRESSELEMENTTYPE.PROVINCE,
    /* American state element */
    ADDRESSELEMENTTYPE.STATEPROV,
    /* International state/province element */
    ADDRESSELEMENTTYPE.BDMSTPROV_X,
    /* Country */
    ADDRESSELEMENTTYPE.COUNTRY,
    /* Canadian postal code element */
    ADDRESSELEMENTTYPE.POSTCODE,
    /* American zip code element */
    ADDRESSELEMENTTYPE.ZIP,
    /* International zip code element */
    ADDRESSELEMENTTYPE.BDMZIP_X,
    /*
     * END: Task 19263 - Rearrange AddressElement Order to match expected UA
     * format
     */
    // START: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
    /* Unparse element */
    ADDRESSELEMENTTYPE.BDMUNPARSE
    // END: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
  };

  @SuppressWarnings("serial")
  private final Map<String, String> elementToCodeTableMap =
    new HashMap<String, String>() {

      {
        put(ADDRESSELEMENTTYPE.COUNTRY, COUNTRY.TABLENAME);
        put(ADDRESSELEMENTTYPE.PROVINCE, PROVINCETYPE.TABLENAME);

      }
    };

  /**
   *
   * @return String tag
   **/
  @Override
  public String[] getTagArray() {

    return caTags;
  }

  @Override
  public AddressLineList
    getAddressAsList(final OtherAddressData otherAddressData)
      throws AppException, InformationalException {

    return getAddressAsList(otherAddressData, elementToCodeTableMap);
  }

  /**
   * This method validates the address.
   *
   * @param ValidateAddressResult
   * @param OtherAddressData
   **/
  @Override
  protected void validateAddress(
    final ValidateAddressResult validateAddressResult,
    final OtherAddressData otherAddressData)
    throws AppException, InformationalException {

    // address tag details
    AddressTagDetails addressTagDetails;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressMapList addressMapList =
      addressDataObj.parseDataToMap(otherAddressData);

    final AddressMap addressMap = new AddressMap();
    ElementDetails elementDetails;

    addressTagDetails = getAddressTagsForLayout();

    if (addressTagDetails.addressTags.length() > 0) {
      addressDataObj.validateTags(addressMapList, addressTagDetails);
    }

    addressMap.name = ADDRESSELEMENTTYPE.LINE1;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);

    addressMap.name = ADDRESSELEMENTTYPE.CITY;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);

    /*
     * if (elementDetails.elementValue.length() == 0) {
     * final AppException e =
     * new AppException(BPOADDRESS.ERR_CITY_UNAVAILABLE);
     *
     * e.arg(kAddressFirstLine);
     * curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
     * .throwWithLookup(e,
     * curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
     * 0);
     * }
     */
    addressMap.name = ADDRESSELEMENTTYPE.PROVINCE;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);

    final String province = elementDetails.elementValue;

    addressMap.name = ADDRESSELEMENTTYPE.POSTCODE;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);

    if (elementDetails.elementValue.length() > kMaxAddressLineSize) {
      final AppException e =
        new AppException(BPOADDRESS.ERR_ADDRESS_FV_ZIP_LONG);

      e.arg(elementDetails.elementValue.length());
      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(e,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    addressMap.name = ADDRESSELEMENTTYPE.COUNTRY;
    final ElementDetails countryCode =
      addressDataObj.findElement(addressMapList, addressMap);

    // IF country = CA

    if (countryCode.elementValue.equals(COUNTRY.CA)) {
      validateIfCountryIsCA(elementDetails, addressMapList, addressMap);
    } else {
      // CUSTOM validation for international Address

      /* BEGIN - CKwong - Task 5155 - Address Evidence */

      // state is separated into different fields for american and international
      // addresses
      if (countryCode.elementValue.equals(COUNTRY.US)) {
        addressMap.name = ADDRESSELEMENTTYPE.STATEPROV;
        elementDetails =
          addressDataObj.findElement(addressMapList, addressMap);

        if (elementDetails.elementValue.toString().trim().isEmpty()) {
          final AppException exception = new AppException(
            BDMBPOADDRESS.ERR_ADDRESS_STATE_MUST_BE_ENTERED_FOR_FOREIGNADDRESS);

          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().throwWithLookup(exception,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              0);

        }

        addressMap.name = ADDRESSELEMENTTYPE.ZIP;
        elementDetails =
          addressDataObj.findElement(addressMapList, addressMap);

        final String zip = elementDetails.elementValue.toString();

        if (zip.trim().isEmpty()) {
          final AppException exception = new AppException(
            BDMBPOADDRESS.ERR_ZIP_CODE_MUST_BE_ENTERED_USADDRESS);

          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().throwWithLookup(exception,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              0);

        }

      } // IF Country =US

      /*
       * if (!countryCode.elementValue.equals(COUNTRY.US)) {
       *
       * addressMap.name = ADDRESSELEMENTTYPE.BDMSTPROV_X;
       *
       * END - CKwong - Task 5155 - Address Evidence
       * elementDetails =
       * addressDataObj.findElement(addressMapList, addressMap);
       *
       * if (elementDetails.elementValue.trim().isEmpty()) {
       *
       * final AppException exception = new AppException(
       * BDMBPOADDRESS.ERR_ADDRESS_STATE_MUST_BE_ENTERED_FOR_FOREIGNADDRESS);
       *
       * curam.core.sl.infrastructure.impl.ValidationManagerFactory
       * .getManager().throwWithLookup(exception,
       * curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
       * 0);
       *
       * }
       *
       * // //CUSTOM validation for international Address END
       *
       * }
       */

      // CUSTOM- General validation

      addressMap.name = ADDRESSELEMENTTYPE.LINE1;
      elementDetails = addressDataObj.findElement(addressMapList, addressMap);

      addressMap.name = ADDRESSELEMENTTYPE.LINE2;
      elementDetails = addressDataObj.findElement(addressMapList, addressMap);

      addressMap.name = ADDRESSELEMENTTYPE.POBOXNO;
      elementDetails = addressDataObj.findElement(addressMapList, addressMap);

      validateAddressResult.addressMapList.assign(addressMapList);
    }

  }

  /**
   * Retrieves the formatted postal code in the pattern of ANA NAN.
   *
   * @param postalCode
   * Contains the postal code.
   *
   * @return The formatted postal code.
   */
  private void validateIfCountryIsCA(ElementDetails elementDetails,
    final AddressMapList addressMapList, final AddressMap addressMap)
    throws AppException, InformationalException {

    final String province = elementDetails.elementValue;
    final curam.core.intf.AddressData addressDataObj =
      AddressDataFactory.newInstance();

    // Postal Code must be entered for CA
    if (elementDetails.elementValue.isEmpty()) {

      final AppException excp = new AppException(
        BDMBPOADDRESS.ERR_POSTAL_CODE_MUST_BE_ENTERED_CANADIANADDRESS);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(excp,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Check postal code format to be A1A 1A1 or A1A1A1
    if (elementDetails.elementValue.length() != 0
      && !this.isValidPostalCode(elementDetails.elementValue.trim())) {
      final AppException excp = new AppException(
        BDMBPOADDRESS.ERR_POSTAL_CODE_FORMAT_FOR_CANADIANADDRESS);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(excp,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    final FormatPostalCode formatPostalCode =
      formatPostalCodeValue(elementDetails.elementValue.trim());

    if (!formatPostalCode.postalCode.equals(elementDetails.elementValue)) {
      for (int i = 0; i < addressMapList.dtls.size(); i++) {
        if (addressMapList.dtls.item(i).name
          .equals(ADDRESSELEMENTTYPE.POSTCODE)) {
          addressMapList.dtls.item(i).value = formatPostalCode.postalCode;
          break;
        }
      }
    }

    // CUSTOMIZATION ---- If province not supplied when Country = CA
    if (province.isEmpty()) {
      final AppException exception =
        new AppException(BDMBPOADDRESS.ERR_ADDRESS_PROVINCE_NOT_SUPPLIED);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(exception,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);

    }

    addressMap.name = ADDRESSELEMENTTYPE.LINE1;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    final String streetNumber = elementDetails.elementValue.toString();

    addressMap.name = ADDRESSELEMENTTYPE.LINE2;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);
    final String street1 = elementDetails.elementValue.toString();

    addressMap.name = ADDRESSELEMENTTYPE.POBOXNO;
    elementDetails = addressDataObj.findElement(addressMapList, addressMap);

    final String poBox = elementDetails.elementValue.toString();

    // 16343 Dev: Development Task for Bug 10563 -- added poBox.isEmpty() in
    // if condition
    if (streetNumber.trim().isEmpty() && street1.trim().isEmpty()
      && poBox.isEmpty()) {
      final AppException exception = new AppException(
        BDMBPOADDRESS.ERR_STRNUM_STRNAME_MUST_BE_COMPLETED_FOR_CANADIANADDRESS);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(exception,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);

    }

  }

  private FormatPostalCode formatPostalCodeValue(final String postalCode) {

    final short firstPostalCodePartLen = 3;
    final short postalCodeLen = 6;
    // CA postal code regular expression.
    final RegularExpression canadianREWithSpace =
      new RegularExpression(CuramConst.gkCanadianREWithSpace);
    final RegularExpression canadianREWithoutSpace =
      new RegularExpression(CuramConst.gkCanadianRE);

    final FormatPostalCode formatPostCode = new FormatPostalCode();

    if (canadianREWithSpace.matches(postalCode)) {
      formatPostCode.postalCode = postalCode.toUpperCase();
      return formatPostCode;
    } else if (canadianREWithoutSpace.matches(postalCode)) {
      formatPostCode.postalCode =
        (postalCode.substring(0, firstPostalCodePartLen) + CuramConst.gkSpace
          + postalCode.substring(firstPostalCodePartLen, postalCodeLen))
            .toUpperCase();
      return formatPostCode;

    }
    formatPostCode.postalCode = postalCode.toUpperCase();
    // If the postal code cannot be formated just return it in upper case.
    return formatPostCode;
  }

  /**
   * Gets the short format of the address.
   *
   * @param OtherAddressData
   * @return the short format of the OtherAddressData object.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public OtherAddressData
    getShortFormat(final OtherAddressData addressDataString)
      throws AppException, InformationalException {

    return getFormat(addressDataString, false);
  }

  /**
   * Gets you the short or long format of the address
   *
   * @param addressDataString
   * @param isLongFormat boolean that determines whether you chose the long or
   * short format
   * @return the address data string
   * @throws AppException
   * @throws InformationalException
   */
  public OtherAddressData getFormat(final OtherAddressData addressDataString,
    final boolean isLongFormat) throws AppException, InformationalException {

    final int k = 0;
    final curam.core.intf.AddressData addressDataObj =
      AddressDataFactory.newInstance();

    AddressString addressString = new AddressString();
    final AddressString addressFormatString = new AddressString();

    addressFormatString.addressString =
      Configuration.getProperty(EnvVars.ENV_ADDRESSSTRINGFORMAT);

    if (addressFormatString.addressString == null) {
      addressFormatString.addressString =
        Configuration.getProperty(EnvVars.ENV_ADDRESSSTRINGFORMAT_DEFAULT);
    }

    addressString.addressString = addressFormatString.addressString;
    final StringBuilder stringBuilder = new StringBuilder();

    if (isLongFormat) {
      stringBuilder
        .append(addressString.addressString.replace(CuramConst.gkComma,
          CuramConst.gkPipeDelimiter + CuramConst.gkNewLine));
    } else {
      stringBuilder.append(addressString.addressString
        .replace(CuramConst.gkComma, CuramConst.gkPipeComma));
    }

    stringBuilder.append(CuramConst.gkPipeDelimiter);
    addressString.addressString = stringBuilder.toString();

    // Convert address data string into <name><value> pairs vector.
    final AddressMapList addressMapList =
      addressDataObj.parseDataToMapWithDelimiter(addressDataString);

    // Changed the logic for replace PROV and STATEPROV contains same word PROV,
    // corrected the logic
    // BEGIN TASK -26500
    // Repalce | from address String
    addressString.addressString =
      addressString.addressString.replaceAll("\\|", "");

    // START: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
    addressString.addressString = addressString.addressString.replace(
      CuramConst.gkSpace + BDMConstants.kBDMUNPARSE + CuramConst.gkComma,
      CuramConst.gkEmpty);
    // END: Task 93505: DEV: Address Format Updates to add BDMUNPARSE

    for (int i = 0; i < addressMapList.dtls.size(); i++) {
      // Read the name from map list
      final String name =
        addressMapList.dtls.item(i).name.replaceAll("\\|", "");

      if (addressMapList.dtls.item(i).value.isEmpty()) {
        addressString =
          removeFormatTagForEmptyEntry(addressString, name, isLongFormat);
      } else {
        addressString = replaceFormatTag(addressString, name,
          addressMapList.dtls.item(i).value, isLongFormat);
      }

    }

    // Need to ensure that the storage variable used to indicate no available
    // address has been localized to the relevant language.
    if (addressString.addressString
      .contains(BPOADDRESS.TEXT_ADDRESS_UNAVAILABLE
        .getMessageText(TransactionInfo.getProgramLocale()))) {
      addressString.addressString =
        new LocalisableString(BPOADDRESS.TEXT_ADDRESS_UNAVAILABLE)
          .getMessage(TransactionInfo.getProgramLocale());
    }

    // START: Task 97062: BUG: Remove BDMUNPARSE from address display
    addressString.addressString = addressString.addressString
      .replace(BDMConstants.kBDMUNPARSE, CuramConst.gkEmpty);
    // END: Task 97062: BUG: Remove BDMUNPARSE from address display

    // Populate return struct
    final OtherAddressData otherAddressData = new OtherAddressData();

    otherAddressData.addressData = addressString.addressString;

    return otherAddressData;
  }

  private AddressString removeFormatTagForEmptyEntry(
    final AddressString addressString, final String name,
    final boolean isLongFormat) {

    int intTagPosStart = addressString.addressString.indexOf(name);

    if (intTagPosStart >= CuramConst.gkZero) {
      int intTagPosEnd = intTagPosStart;
      addressString.addressString =
        addressString.addressString.replaceFirst(name, CuramConst.gkEmpty);

      if (intTagPosEnd < addressString.addressString.length()) {
        while (!Character.isLetterOrDigit(
          addressString.addressString.charAt(intTagPosEnd))) {
          intTagPosEnd++;
        }
      } else {
        final char elementDelimiter = isLongFormat ? CuramConst.gkNewLineChar
          : CuramConst.gkCommaDelimiterChar;
        while (intTagPosStart > 0
          && (elementDelimiter == addressString.addressString
            .charAt(intTagPosStart - CuramConst.gkOne)
            || CuramConst.gkSpaceChar == addressString.addressString
              .charAt(intTagPosStart - CuramConst.gkOne))) {
          intTagPosStart--;
        }
      }

      addressString.addressString = addressString.addressString
        .substring(CuramConst.gkZero, intTagPosStart)
        + addressString.addressString.substring(intTagPosEnd);
    }

    return addressString;
  }

  private AddressString replaceFormatTag(final AddressString addressString,
    final String name, final String value, final boolean isLongFormat)
    throws AppException, InformationalException {

    if (ADDRESSELEMENTTYPE.COUNTRY.equals(name) && isLongFormat) {

      final String countryName =
        CodeTable.getOneItem(COUNTRY.TABLENAME, value);

      addressString.addressString =
        addressString.addressString.replace(name, countryName);

    } else if (ADDRESSELEMENTTYPE.PROVINCE// + CuramConst.gkPipeDelimiter)
      .equals(name) && isLongFormat) {
      final String provinceName =
        CodeTable.getOneItem(PROVINCETYPE.TABLENAME, value);
      // BEGIN TASK-27316 State Field On Canadian Address
      addressString.addressString =
        addressString.addressString.replaceFirst(name, provinceName);
      // END TASK-27316
      // BEGIN TASK -26500 New condition for US State for long format
    } else if (ADDRESSELEMENTTYPE.STATEPROV// + CuramConst.gkPipeDelimiter)
      .equals(name) && isLongFormat) {

      final String stateName =
        CodeTable.getOneItem(ADDRESSSTATE.TABLENAME, value);
      addressString.addressString =
        addressString.addressString.replace(name, stateName);
    } else if (name.equalsIgnoreCase(BDMConstants.kBDMUNPARSE)) {
      addressString.addressString =
        addressString.addressString.replace(name, CuramConst.gkEmpty);
    } else {
      // BEGIN TASK-27316 State Field On Canadian Address
      addressString.addressString =
        addressString.addressString.replaceFirst(name, value);
      // END TASK-27316

    }

    return addressString;
  }

  /**
   * This method returns a formatted Canadian address string which is typically
   * displayed over two or more lines of text for the user. This is in contrast
   * to the short format which is always displayed as one line of text.
   *
   * @param addressDataString
   * contains address data string.
   *
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  @Override
  public OtherAddressData
    getLongFormat(final OtherAddressData addressDataString)
      throws AppException, InformationalException {

    final OtherAddressData formattedAddressData =
      getFormat(addressDataString, true);
    addressDataString.assign(formattedAddressData);

    return addressDataString;

  }

  /**
   * Creates otherAddressData elements from the address fields details in the
   * INTL expected layout.
   *
   * @param addressFieldDetails
   * The details of the address field elements
   *
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  @Override
  public OtherAddressData
    parseFieldsToData(final AddressFieldDetails addressFieldDetails)
      throws AppException, InformationalException {

    final OtherAddressData addressData = new OtherAddressData();
    String sModifiableInd;
    String sPostCode;
    final StringBuilder formatBuffer = new StringBuilder();

    // get Postal code
    sPostCode = addressFieldDetails.postalCode;

    if (sPostCode == null) {
      sPostCode = CuramConst.gkEmpty;
    }

    if (addressFieldDetails.modifiableInd) {
      sModifiableInd = CuramConst.gkModifiableIndex;
    } else {
      sModifiableInd = CuramConst.gkStringZero;
    }

    // Just to ensure that the StringBuffer formatBuffer has a minimum capacity
    // to fit Canadian address.
    final int minimumCapacity = CuramConst.gkNewLine.length() * 11
      + CuramConst.gkStringZero.length() * 2 + kEqualsLen * 7
      + AddressUtil.kWidgetVersion.length()
      + ADDRESSLAYOUTTYPE.BDMINTL.length()
      + addressFieldDetails.countryCode.length() + sModifiableInd.length()
      + ADDRESSELEMENTTYPE.APT.length()
      + addressFieldDetails.suiteNum.length()

      // addressline3 is used for PO Box

      + ADDRESSELEMENTTYPE.POBOXNO.length()
      + addressFieldDetails.addressLine3.length()

      /* BEGIN - CKwong - Task 5155 - Address Evidence */

      // corresponds to street number
      + ADDRESSELEMENTTYPE.LINE1.length()
      + addressFieldDetails.addressLine1.length()
      // corresponds to street name
      + ADDRESSELEMENTTYPE.LINE2.length()
      + addressFieldDetails.addressLine2.length()

      /* END - CKwong - Task 5155 - Address Evidence */

      + ADDRESSELEMENTTYPE.CITY.length() + addressFieldDetails.city.length()
      + ADDRESSELEMENTTYPE.COUNTRY.length()
      + addressFieldDetails.countryCode.length()

      /* BEGIN - CKwong - Task 5155 - Address Evidence */

      // These are the non-common elements - which will all be part of the
      // address data. State and zip values will only be mapped once, either to
      // the US fields or international fields
      + ADDRESSELEMENTTYPE.PROVINCE.length()
      + addressFieldDetails.province.length()
      + ADDRESSELEMENTTYPE.POSTCODE.length() + sPostCode.length()
      + ADDRESSELEMENTTYPE.STATEPROV.length()
      + ADDRESSELEMENTTYPE.ZIP.length()
      + ADDRESSELEMENTTYPE.BDMSTPROV_X.length()
      + ADDRESSELEMENTTYPE.BDMZIP_X.length()
      + addressFieldDetails.stateProvince.length()
      + addressFieldDetails.zipCode.length()
      // START: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
      + ADDRESSELEMENTTYPE.BDMUNPARSE.length();
    // END: Task 93505: DEV: Address Format Updates to add BDMUNPARSE

    /* END - CKwong - Task 5155 - Address Evidence */

    formatBuffer.ensureCapacity(minimumCapacity);

    // Creating address header.
    formatBuffer.append(AddressUtil.kWidgetVersion);
    formatBuffer.append(CuramConst.gkNewLine);
    formatBuffer.append(addressFieldDetails.addressID); // CuramConst.gkStringZero
    formatBuffer.append(CuramConst.gkNewLine);
    formatBuffer.append(ADDRESSLAYOUTTYPE.BDMINTL);
    formatBuffer.append(CuramConst.gkNewLine);
    formatBuffer.append(addressFieldDetails.countryCode);
    formatBuffer.append(CuramConst.gkNewLine);
    formatBuffer.append(sModifiableInd);
    formatBuffer.append(CuramConst.gkNewLine);
    formatBuffer.append(addressFieldDetails.versionNo); // CuramConst.gkStringZero
    formatBuffer.append(CuramConst.gkNewLine);

    // Creating address body.
    formatBuffer.append(ADDRESSELEMENTTYPE.COUNTRY);
    formatBuffer.append(kEquals);
    formatBuffer.append(addressFieldDetails.countryCode);
    formatBuffer.append(CuramConst.gkNewLine);

    formatBuffer.append(ADDRESSELEMENTTYPE.POSTCODE);
    formatBuffer.append(kEquals);
    formatBuffer.append(sPostCode);
    formatBuffer.append(CuramConst.gkNewLine);

    // if the address is Canadian, all state and zip fields should be empty
    if (addressFieldDetails.countryCode.equals(COUNTRY.CA)) {
      formatBuffer.append(ADDRESSELEMENTTYPE.ZIP);
      formatBuffer.append(kEquals);
      formatBuffer.append(CuramConst.gkEmpty);
      formatBuffer.append(CuramConst.gkNewLine);
      formatBuffer.append(ADDRESSELEMENTTYPE.BDMZIP_X);
      formatBuffer.append(kEquals);
      formatBuffer.append(CuramConst.gkEmpty);
      formatBuffer.append(CuramConst.gkNewLine);
    }
    // if the address is American, the state and zip fields should map to the US
    // state and zip elements
    else if (addressFieldDetails.countryCode.equals(COUNTRY.US)) {
      formatBuffer.append(ADDRESSELEMENTTYPE.ZIP);
      formatBuffer.append(kEquals);
      formatBuffer.append(addressFieldDetails.zipCode);
      formatBuffer.append(CuramConst.gkNewLine);
      // map international fields to empty values
      formatBuffer.append(ADDRESSELEMENTTYPE.BDMZIP_X);
      formatBuffer.append(kEquals);
      formatBuffer.append(CuramConst.gkEmpty);
      formatBuffer.append(CuramConst.gkNewLine);
    }
    // if the address is international, the state and zip fields should map to
    // the international state and zip elements
    else {
      // map US fields to empty values
      formatBuffer.append(ADDRESSELEMENTTYPE.ZIP);
      formatBuffer.append(kEquals);
      formatBuffer.append(CuramConst.gkEmpty);
      formatBuffer.append(CuramConst.gkNewLine);
      formatBuffer.append(ADDRESSELEMENTTYPE.BDMZIP_X);
      formatBuffer.append(kEquals);
      formatBuffer.append(addressFieldDetails.zipCode);
      formatBuffer.append(CuramConst.gkNewLine);
    }

    formatBuffer.append(ADDRESSELEMENTTYPE.APT);
    formatBuffer.append(kEquals);
    formatBuffer.append(addressFieldDetails.suiteNum);
    formatBuffer.append(CuramConst.gkNewLine);

    formatBuffer.append(ADDRESSELEMENTTYPE.POBOXNO);
    formatBuffer.append(kEquals);
    formatBuffer.append(addressFieldDetails.addressLine3);
    formatBuffer.append(CuramConst.gkNewLine);

    // corresponds to street number
    formatBuffer.append(ADDRESSELEMENTTYPE.LINE1);
    formatBuffer.append(kEquals);
    formatBuffer.append(addressFieldDetails.addressLine1);
    formatBuffer.append(CuramConst.gkNewLine);

    // corresponds to street name
    formatBuffer.append(ADDRESSELEMENTTYPE.LINE2);
    formatBuffer.append(kEquals);
    formatBuffer.append(addressFieldDetails.addressLine2);
    formatBuffer.append(CuramConst.gkNewLine);

    formatBuffer.append(ADDRESSELEMENTTYPE.CITY);
    formatBuffer.append(kEquals);
    formatBuffer.append(addressFieldDetails.city);

    // The assumption is made that province and postal code will ONLY be filled
    // out if it is a Canadian address format
    formatBuffer.append(CuramConst.gkNewLine);
    formatBuffer.append(ADDRESSELEMENTTYPE.PROVINCE);
    formatBuffer.append(kEquals);
    formatBuffer.append(addressFieldDetails.province);
    formatBuffer.append(CuramConst.gkNewLine);

    /* BEGIN - CKwong - Task 5155 - Address Evidence */

    // if the address is Canadian, all state and zip fields should be empty
    if (addressFieldDetails.countryCode.equals(COUNTRY.CA)) {
      formatBuffer.append(ADDRESSELEMENTTYPE.STATEPROV);
      formatBuffer.append(kEquals);
      formatBuffer.append(CuramConst.gkEmpty);
      formatBuffer.append(CuramConst.gkNewLine);
      formatBuffer.append(ADDRESSELEMENTTYPE.BDMSTPROV_X);
      formatBuffer.append(kEquals);
      formatBuffer.append(CuramConst.gkEmpty);
    }
    // if the address is American, the state and zip fields should map to the US
    // state and zip elements
    else if (addressFieldDetails.countryCode.equals(COUNTRY.US)) {
      formatBuffer.append(ADDRESSELEMENTTYPE.STATEPROV);
      formatBuffer.append(kEquals);
      formatBuffer.append(addressFieldDetails.stateProvince);
      formatBuffer.append(CuramConst.gkNewLine);
      // map international fields to empty values
      formatBuffer.append(ADDRESSELEMENTTYPE.BDMSTPROV_X);
      formatBuffer.append(kEquals);
      formatBuffer.append(CuramConst.gkEmpty);
    }
    // if the address is international, the state and zip fields should map to
    // the international state and zip elements
    else {
      // map US fields to empty values
      formatBuffer.append(ADDRESSELEMENTTYPE.STATEPROV);
      formatBuffer.append(kEquals);
      formatBuffer.append(CuramConst.gkEmpty);
      // BEGIN TASK-56098 StateProvince is missing for Intl Address
      // formatBuffer.append(addressFieldDetails.stateProvince);
      // END TASK-56098
      formatBuffer.append(CuramConst.gkNewLine);
      formatBuffer.append(ADDRESSELEMENTTYPE.BDMSTPROV_X);
      formatBuffer.append(kEquals);
      // BEGIN TASK-56098 StateProvince is missing for Intl Address
      // task 68158 Fix stae/region for INTL address
      formatBuffer.append(addressFieldDetails.stateProvince);

      // formatBuffer.append(addressFieldDetails.addressLine4);
      // END TASK-56098
    }

    // START: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
    // Adding BDMUNPARSE to address data
    formatBuffer.append(CuramConst.gkNewLine);
    formatBuffer.append(ADDRESSELEMENTTYPE.BDMUNPARSE);
    formatBuffer.append(kEquals);
    formatBuffer.append(CuramConst.gkEmpty);
    // END: Task 93505: DEV: Address Format Updates to add BDMUNPARSE

    /* END - CKwong - Task 5155 - Address Evidence */
    addressData.addressData = formatBuffer.toString();

    return addressData;
  }

  /**
   * Creates address elements from the address data provided for the CA layout.
   *
   * @param addressMapList
   * The list of the address map
   * @param addressElementDtls
   * The details of the address elements
   *
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  @Override
  public void storeElements(final AddressDtls details)
    throws AppException, InformationalException {

    final AddressElementDtls addressElementDtls = new AddressElementDtls();

    // Address data object, map list and other address data structs.
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();
    AddressMapList addressMapList;
    final OtherAddressData otherAddressData = new OtherAddressData();

    addressElementDtls.addressID = details.addressID;
    otherAddressData.addressData = details.addressData;
    addressMapList = addressDataObj.parseDataToMap(otherAddressData);

    final AddressMap addressMap = new AddressMap();

    // corresponds to street number
    addressMap.name = ADDRESSELEMENTTYPE.LINE1;
    storeElement(addressMapList, addressElementDtls, addressMap);

    addressMap.name = ADDRESSELEMENTTYPE.CITY;
    storeElement(addressMapList, addressElementDtls, addressMap);

    addressMap.name = ADDRESSELEMENTTYPE.PROVINCE;
    storeElement(addressMapList, addressElementDtls, addressMap);

    addressMap.name = ADDRESSELEMENTTYPE.POSTCODE;
    storeElement(addressMapList, addressElementDtls, addressMap);

    addressMap.name = ADDRESSELEMENTTYPE.COUNTRY;
    storeElement(addressMapList, addressElementDtls, addressMap);

    addressMap.name = ADDRESSELEMENTTYPE.APT;
    storeElement(addressMapList, addressElementDtls, addressMap);
    // corresponds to street name
    addressMap.name = ADDRESSELEMENTTYPE.LINE2;
    storeElement(addressMapList, addressElementDtls, addressMap);
    addressMap.name = ADDRESSELEMENTTYPE.POBOXNO;
    storeElement(addressMapList, addressElementDtls, addressMap);
    addressMap.name = ADDRESSELEMENTTYPE.STATEPROV;
    storeElement(addressMapList, addressElementDtls, addressMap);

    /* BEGIN - CKwong - Task 5155 Address Evidence */
    addressMap.name = ADDRESSELEMENTTYPE.ZIP;
    storeElement(addressMapList, addressElementDtls, addressMap);

    addressMap.name = ADDRESSELEMENTTYPE.BDMSTPROV_X;
    storeElement(addressMapList, addressElementDtls, addressMap);

    addressMap.name = ADDRESSELEMENTTYPE.BDMZIP_X;
    storeElement(addressMapList, addressElementDtls, addressMap);

    // START : TASK 118752 : Store Address element for BDM Unparsed indicator
    addressMap.name = ADDRESSELEMENTTYPE.BDMUNPARSE;
    storeElement(addressMapList, addressElementDtls, addressMap);
    // END : TASK 118752 : Store Address element for BDM Unparsed indicator

    /* END - CKwong - Task 5155 Address Evidence */
  }

  private boolean isValidPostalCode(final String postalCode) {

    // CA postal code regular expression.
    final RegularExpression regExpWithSpace =
      new RegularExpression(CuramConst.gkCanadianREWithSpace);
    final RegularExpression regExpWithoutSpace =
      new RegularExpression(CuramConst.gkCanadianRE);

    final Boolean validCAPostalCode = regExpWithSpace.matches(postalCode)
      || regExpWithoutSpace.matches(postalCode);

    return validCAPostalCode;
  }
}
