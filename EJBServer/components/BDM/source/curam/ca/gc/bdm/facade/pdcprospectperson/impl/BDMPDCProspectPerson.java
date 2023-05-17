package curam.ca.gc.bdm.facade.pdcprospectperson.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMCORRESDELIVERY;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.creole.impl.Statics;
import curam.ca.gc.bdm.entity.struct.BDMProspectPersonDtls;
import curam.ca.gc.bdm.facade.participant.struct.BDMProspectPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonDemographicPageDetails;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMReadPersonDemographicDetails;
import curam.ca.gc.bdm.facade.pdcprospectperson.struct.BDMContactPreferenceDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMIDENTIFICATIONINFO;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.ca.gc.bdm.sl.pdcprospectperson.fact.BDMMaintainPDCProspectPersonFactory;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.COUNTRYCODE;
import curam.codetable.PHONETYPE;
import curam.core.facade.struct.ConcernRoleIDKey;
import curam.core.fact.AddressDataFactory;
import curam.core.intf.AddressData;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.ElementDetails;
import curam.core.struct.OtherAddressData;
import curam.core.struct.ProspectPersonKey;
import curam.pdc.facade.fact.PDCProspectPersonFactory;
import curam.pdc.facade.intf.PDCProspectPerson;
import curam.pdc.facade.struct.PersonDemographicPageDetails;
import curam.pdc.facade.struct.ProspectPersonRegistrationDetails;
import curam.pdc.facade.struct.ProspectPersonRegistrationResult;
import curam.pdc.facade.struct.ReadPersonDemographicDetails;
import curam.pdc.facade.struct.ReadProspectPersonKey;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author teja.konda
 *
 */
public class BDMPDCProspectPerson
  extends curam.ca.gc.bdm.facade.pdcprospectperson.base.BDMPDCProspectPerson {

  private final BDMUtil bdmUtil = new BDMUtil();

  /**
   *
   * @param details
   * @throws InformationalException
   * @throws AppException
   */
  private void validateRegistrationDetails(
    final BDMProspectPersonRegistrationDetails details)
    throws InformationalException, AppException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // Address validations
    validateAddress(details.dtls.prospectPersonRegistrationDtls.addressData,
      informationalManager);

    // Validation-1
    if (!details.isMailingAddSame && isAddressEmpty(
      details.dtls.prospectPersonRegistrationDtls.mailingAddressData)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_MAILING_ADDRESS_MANDATORY);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    } else if (!details.isMailingAddSame && !isAddressEmpty(
      details.dtls.prospectPersonRegistrationDtls.mailingAddressData)) {

      validateAddress(
        details.dtls.prospectPersonRegistrationDtls.mailingAddressData,
        informationalManager);
    }

    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();
    final CodeTableItemUniqueKey codeTableItemUniqueKey =
      new CodeTableItemUniqueKey();

    codeTableItemUniqueKey.code = details.phoneCountryCode;
    codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
    codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;

    final Boolean isPlusOneCountry = details.phoneCountryCode.isEmpty()
      ? false
      : codeTableAdminObj
        .readCTIDetailsForLocaleOrLanguage(codeTableItemUniqueKey).annotation
          .trim().equals(BDMConstants.kphonePrefix);

    // validation-2 if country code is +1, then area code is mandatory
    if (isPlusOneCountry
      && details.dtls.prospectPersonRegistrationDtls.phoneAreaCode
        .isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_AREA_CODE);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    } else if (isPlusOneCountry && (!isNumeric(
      details.dtls.prospectPersonRegistrationDtls.phoneAreaCode)
      || details.dtls.prospectPersonRegistrationDtls.phoneAreaCode
        .length() != 3)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_AREA_CODE_3DIGIT);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    // validation-4 if country code is +1 then phone Number must be 7 digits and
    // numeric
    if (isPlusOneCountry
      && details.dtls.prospectPersonRegistrationDtls.phoneNumber.isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_MISSING);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    } else if (isPlusOneCountry
      && (!isNumeric(details.dtls.prospectPersonRegistrationDtls.phoneNumber)
        || details.dtls.prospectPersonRegistrationDtls.phoneNumber
          .length() != 7)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_7DIGIT);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }
    // validation-5 if country code is not +1 then phone Number and Area code
    // must be numeric
    if (!isPlusOneCountry && (!isNumeric(
      details.dtls.prospectPersonRegistrationDtls.phoneAreaCode)
      || !isNumeric(
        details.dtls.prospectPersonRegistrationDtls.phoneNumber))) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_OTHER_COUNTRY);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }
    // validation-6 if Phone Type is Personal or Business Mobile then extension
    // cannot be added
    if ((details.dtls.prospectPersonRegistrationDtls.phoneType
      .equals(PHONETYPE.PERSONAL_MOBILE)
      || details.dtls.prospectPersonRegistrationDtls.phoneType
        .equals(PHONETYPE.BUSINESS_MOBILE))
      && !details.dtls.prospectPersonRegistrationDtls.phoneExtension
        .isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_INVALID_TYPE);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    // validation-7 Extension can only have numeric values
    if (!isNumeric(
      details.dtls.prospectPersonRegistrationDtls.phoneExtension)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_EXTENSION_NUMERIC);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    informationalManager.failOperation();

  }

  /**
   * Util method to validate address fields
   *
   * @since ADO-7089
   * @param addressData
   * @param informationalManager
   * @throws InformationalException
   */
  private InformationalManager validateAddress(final String addressData,
    final InformationalManager informationalManager)
    throws InformationalException, AppException {

    final OtherAddressData otherAddressData = new OtherAddressData();
    otherAddressData.addressData = addressData;

    // get Country
    final String country =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.COUNTRY);

    // Postal Code
    final String postalCode =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.POSTCODE);

    // state
    final String state =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.STATE);

    // city
    final String city =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.CITY);

    // province
    final String province =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.PROVINCE);

    final String zipCode =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.ZIP);

    // validations for CA Address
    if (country.equalsIgnoreCase(COUNTRYCODE.CACODE)) {

      // City is Mandatory
      if (city.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_CITY_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }
      // Province is Mandatory
      if (province.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_PROV_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }
      // Postal Code is Mandatory
      if (postalCode.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_POSTALCODE_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      } else if (!bdmUtil.isValidPostalCode(postalCode)) {// Postal Code should
                                                          // be in valid
                                                          // Canadian Format

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_CA_INVALID_POSTAL);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }

    } else if (country.equalsIgnoreCase(COUNTRYCODE.USCODE)) {// validations for
                                                              // US Address

      if (zipCode.isEmpty()) {// Zip code is mandatory

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_US_ZIPCODE_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }

    }
    if (!country.equalsIgnoreCase(COUNTRYCODE.CACODE)) {// Validations for all
                                                        // other countries

      if (state.isEmpty()) {

        final LocalisableString localisableString =
          new LocalisableString(BDMPERSON.ERR_ADDR_STATE_MISSING);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }
    }

    return informationalManager;

  }

  /**
   *
   * @param mailingAddressData
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isAddressEmpty(final String mailingAddressData)
    throws AppException, InformationalException {

    final OtherAddressData otherAddressData = new OtherAddressData();
    otherAddressData.addressData = mailingAddressData;

    Boolean isEmptyAddress = false;

    // get Country
    final String country =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.COUNTRY);

    // city
    final String city =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.CITY);

    if (city.isEmpty() || country.isEmpty()) {
      isEmptyAddress = true;
    }

    return isEmptyAddress;
  }

  /**
   *
   * Util Method to convert Address Data
   *
   * @param otherAddressData
   * @param addressElementType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getAddressDetails(final OtherAddressData otherAddressData,
    final String addressElementType)
    throws AppException, InformationalException {

    final AddressData addressDataObj = AddressDataFactory.newInstance();

    final AddressMap addressMap = new AddressMap();

    addressMap.name = addressElementType;

    final AddressMapList addressMapList =
      addressDataObj.parseDataToMap(otherAddressData);

    final ElementDetails elementDetails =
      addressDataObj.findElement(addressMapList, addressMap);

    if (elementDetails.elementFound) {

      return elementDetails.elementValue;
    }

    return "";

  }

  /**
   * Util Method to validate if given string is a number
   *
   * @since TASK-9375
   * @param phoneNumber
   * @return
   */
  private boolean isNumeric(final String phoneNumber) {

    if (phoneNumber.isEmpty()) {

      return true;

    }
    // regex to check for Numeric Values
    final Pattern pattern = Pattern.compile("^[0-9]*$");
    final Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }

  /**
   * Read prospect person demographics details
   */
  @Override
  public BDMReadPersonDemographicDetails readBDMNonPDCDemographicData(
    final ProspectPersonKey key) throws AppException, InformationalException {

    final BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();

    ReadPersonDemographicDetails readPersonDemographicDetails =
      new ReadPersonDemographicDetails();
    final PDCProspectPerson pdcProspectPersonObj =
      PDCProspectPersonFactory.newInstance();

    readPersonDemographicDetails =
      pdcProspectPersonObj.readNonPDCDemographicData(key);

    bdmReadPersonDemographicDetails.assign(readPersonDemographicDetails);

    final BDMProspectPersonDtls bdmProspectPersonDtls =
      bdmUtil.readProspectPersonDemographicsDetails(key);

    bdmReadPersonDemographicDetails.educationLevel =
      bdmProspectPersonDtls.educationLevel;
    bdmReadPersonDemographicDetails.indigenousPersonType =
      bdmProspectPersonDtls.indigenousPersonType;
    bdmReadPersonDemographicDetails.memberMinorityGrpType =
      bdmProspectPersonDtls.memberMinorityGrpType;

    return bdmReadPersonDemographicDetails;
  }

  /**
   * Modify prospect person demographics details
   */
  @Override
  public void modifyBDMNonPDCDemographicData(
    final BDMPersonDemographicPageDetails details)
    throws AppException, InformationalException {

    final PersonDemographicPageDetails personDemographicPageDetails =
      new PersonDemographicPageDetails();

    personDemographicPageDetails.assign(details);

    final PDCProspectPerson pdcProspectPersonObj =
      PDCProspectPersonFactory.newInstance();

    pdcProspectPersonObj
      .modifyNonPDCDemographicData(personDemographicPageDetails);

    bdmUtil.modifyProspectPersonDemographicsDetails(details);
  }

  /**
   * Bug 103166: Fields Preferred Language of Written and Oral Communication
   * should be mandatory and pre-populated with the corresponding values
   */
  @Override
  public BDMContactPreferenceDetails readContactPreferencesEvidence(
    final ConcernRoleIDKey key) throws AppException, InformationalException {

    return BDMMaintainPDCProspectPersonFactory.newInstance()
      .readContactPreferencesEvidence(key);

  }

  @Override
  public ProspectPersonRegistrationResult registerProspectPersonAsPerson(
    final ReadProspectPersonKey key,
    final BDMProspectPersonRegistrationDetails details)
    throws AppException, InformationalException {

    validateRegistrationDetails(details);
    String commMethod = "";
    String phoneType = "";

    TransactionInfo.setFacadeScopeObject(BDMConstants.IS_MERGE_PROSPECT,
      true);
    if (!details.dtls.prospectPersonRegistrationDtls.prefCommMethod
      .isEmpty()) {

      commMethod = details.dtls.prospectPersonRegistrationDtls.prefCommMethod;
      details.dtls.prospectPersonRegistrationDtls.prefCommMethod = "";
    }

    if (!details.dtls.prospectPersonRegistrationDtls.phoneType.isEmpty()) {

      phoneType = details.dtls.prospectPersonRegistrationDtls.phoneType;
      details.dtls.prospectPersonRegistrationDtls.phoneType = "";
    }
    // ADO-7089 -End Tkonda added Validations on person registration screen

    // if mailing address is the same
    // set residential address values the same as the mailing address values
    if (details.isMailingAddSame) {

      // setting Address data to be the same as mailing address data
      details.dtls.prospectPersonRegistrationDtls.mailingAddressData =
        details.dtls.prospectPersonRegistrationDtls.addressData;
    }

    final PDCProspectPerson pdcProspectPerson =
      PDCProspectPersonFactory.newInstance();
    final ProspectPersonRegistrationDetails prospectPersonRegistrationDetails =
      new ProspectPersonRegistrationDetails();

    prospectPersonRegistrationDetails.dtls =
      details.dtls.prospectPersonRegistrationDtls;

    prospectPersonRegistrationDetails.dtls.registrationDate =
      details.dtls.prospectPersonRegistrationDtls.registrationDate;

    final ProspectPersonRegistrationResult prospectPersonRegistrationResult =
      pdcProspectPerson.registerProspectPersonAsPerson(key,
        prospectPersonRegistrationDetails);

    // ADO-7089 -Start Tkonda added Validations on person registration screen
    try {

      // implemented custom logic to support BDM Customizations
      if (!details.preferredOralLang.isEmpty()
        || !details.preferredWrittenLang.isEmpty() || !commMethod.isEmpty()) {
        // create ContactPreference
        bdmUtil.createContactPreferenceEvidence(
          prospectPersonRegistrationResult.dtls.registrationIDDetails.concernRoleID,
          details.preferredOralLang, details.preferredWrittenLang,
          commMethod);
      }

      // implemented custom logic as OOTB Phone Country code is hardcoded to
      // be string
      if (phoneType.length() > 0
        && details.dtls.prospectPersonRegistrationDtls.phoneNumber
          .length() > 0) {

        final ParticipantPhoneDetails phoneDetails =
          new ParticipantPhoneDetails();
        phoneDetails.phoneAreaCode =
          details.dtls.prospectPersonRegistrationDtls.phoneAreaCode;
        phoneDetails.phoneNumber =
          details.dtls.prospectPersonRegistrationDtls.phoneNumber;
        phoneDetails.phoneExtension =
          details.dtls.prospectPersonRegistrationDtls.phoneExtension;
        phoneDetails.typeCode = phoneType;
        phoneDetails.concernRoleID =
          prospectPersonRegistrationResult.dtls.registrationIDDetails.concernRoleID;
        phoneDetails.startDate = TransactionInfo.getSystemDate();
        phoneDetails.typeCode = phoneType;
        phoneDetails.endDate = Date.kZeroDate;
        phoneDetails.comments = "";

        bdmUtil.createPhoneNumberEvidence(phoneDetails,
          details.phoneCountryCode);

      }
    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " Exception Occured during Registration and creation of Evidences :"
        + e);

    }
    // ADO-7089 -End Tkonda added Validations on person registration screen

    return prospectPersonRegistrationResult;

  }

  @Override
  public ProspectPersonRegistrationResult registerIOProspectPersonAsPerson(
    final ReadProspectPersonKey key,
    final BDMProspectPersonRegistrationDetails details)
    throws AppException, InformationalException {

    key.dtls.concernRoleID =
      details.dtls.prospectPersonRegistrationDtls.relatedConcernRoleID;
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // validate SIN Number if entered
    final String sinNumber =
      details.dtls.prospectPersonRegistrationDtls.socialSecurityNumber;
    if (!StringUtil.isNullOrEmpty(sinNumber)) {
      // Bug 89706 : Unable to add 900 series SIN to persons - Commented
      // validation for sin starting with number 9
      if (Statics.validateSINMod10(null, sinNumber)) { // ||
        // Statics.isSINNumberStartsWith9Series(null,
        // details.dtls.sinNumber)

        final LocalisableString localisableString =
          new LocalisableString(BDMIDENTIFICATIONINFO.SIN_NUMBER_NOT_VALID);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }
    }

    if (details.bdmReceivedFrom
      .equalsIgnoreCase(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT)
      && BDMUtil.isCountryRestricted(details.countryCode)) {
      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_RESTRICTED_COUNTRY_SELECTED);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    }

    informationalManager.failOperation();

    // set the sinNumber null so system generate Reference Number evidence, not
    // the SIN Evidence initially
    details.dtls.prospectPersonRegistrationDtls.socialSecurityNumber = "";

    /*
     * The data field Preferred method of Correspondence (including tax slips)
     * is hidden from Register Client and Create Foreign Engagement Case
     * modal.
     * System must pass a value for Preferred method of Correspondence
     * (including tax slips)
     * with a value selected as ‘Digital and hard copy by mail’ upon
     * successful saving
     */
    details.preferredCorrDeliveryMethod = BDMCORRESDELIVERY.POSTALDIG;

    // update the evidences for bdmModeOfReceipt,bdmReceivedFrom,
    // bdmReceivedFromCountry fields
    bdmUtil.updateEvidenceAttributes(details.bdmReceivedFrom,
      details.countryCode,
      details.dtls.prospectPersonRegistrationDtls.relatedConcernRoleID);

    // register the prospect as person now..

    final ProspectPersonRegistrationResult prospectPersonRegistrationResult =
      this.registerProspectPersonAsPerson(key, details);

    // call SIN-SIR Check
    if (!StringUtil.isNullOrEmpty(sinNumber)) {
      bdmUtil.callSINSIR(sinNumber,
        prospectPersonRegistrationResult.dtls.registrationIDDetails.concernRoleID);
    }

    return prospectPersonRegistrationResult;

  }

}
