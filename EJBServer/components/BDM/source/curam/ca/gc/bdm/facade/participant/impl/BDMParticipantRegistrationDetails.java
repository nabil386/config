package curam.ca.gc.bdm.facade.participant.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMCORRESDELIVERY;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.creole.impl.Statics;
import curam.ca.gc.bdm.entity.communication.struct.BDMSearchByTrackingNumberKey;
import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNum;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNumList;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonSearchWizardKey;
import curam.ca.gc.bdm.facade.participant.struct.BDMProspectPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMRegisterPersonWizardSearchDetails;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchDetailsResult;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchKey1;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.message.BDMIDENTIFICATIONINFO;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.ca.gc.bdm.message.BDMTHIRDPARTYCONTACT;
import curam.ca.gc.bdm.sl.externalparty.impl.BDMMaintainExternalParty;
import curam.ca.gc.bdm.sl.fec.fact.BDMMaintainForeignEngagementCaseFactory;
import curam.ca.gc.bdm.sl.fec.intf.BDMMaintainForeignEngagementCase;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.COUNTRYCODE;
import curam.codetable.PHONETYPE;
import curam.core.facade.fact.ParticipantRegistrationFactory;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.fact.ProspectPersonFactory;
import curam.core.facade.intf.ParticipantRegistration;
import curam.core.facade.intf.Person;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.AddProspectPersonState;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.PersonSearchDetailsResult;
import curam.core.facade.struct.PersonSearchWizardKey;
import curam.core.facade.struct.ProspectPersonRegistrationDetails;
import curam.core.facade.struct.ProspectPersonRegistrationResult;
import curam.core.facade.struct.RegisterPersonState;
import curam.core.facade.struct.RegisterPersonWizardSearchDetails;
import curam.core.facade.struct.WizardProperties;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.AddressData;
import curam.core.intf.ConcernRoleAlternateID;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.AlternateIDReadmultiDtls;
import curam.core.struct.AlternateIDReadmultiDtlsList;
import curam.core.struct.ConcernRoleAlternateIDRMKey;
import curam.core.struct.ElementDetails;
import curam.core.struct.OtherAddressData;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static curam.ca.gc.bdm.util.impl.BDMSinNumberUtil.isValidSearchCombination;
import static curam.ca.gc.bdm.util.impl.BDMSinNumberUtil.isValidSearchCriteria;
import static curam.ca.gc.bdm.util.impl.BDMSinNumberUtil.maskSinNumber;

/**
 *
 * BUG 13283 02/01/2022 Agole Unable to Register Client with International
 * Address. Modified to read state from BDMSTPROV_X for international Address
 *
 */
public class BDMParticipantRegistrationDetails extends
  curam.ca.gc.bdm.facade.participant.base.BDMParticipantRegistrationDetails {

  private final BDMUtil bdmUtil = new BDMUtil();

  public BDMParticipantRegistrationDetails() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  BDMIdentificationEvidence bdmIdentificationEvidence;

  /**
   * added method to add custom validations
   *
   * @since ADO-7089
   * @param bdmRegistrationDtls
   */

  @Override
  public ParticipantRegistrationWizardResult setRegisterPersonForPDCDetails(
    final BDMPersonRegistrationDetails bdmRegistrationDtls,
    final WizardStateID stateID, final ActionIDProperty actionID)
    throws AppException, InformationalException {

    // START TASK 118752 : add a logic to set the BDMUNPARSE to 1 for the INTL
    // Address
    bdmRegistrationDtls.dtls.addressData = bdmUtil
      .setBDMUnparsedIndForINTLAddress(bdmRegistrationDtls.dtls.addressData);
    bdmRegistrationDtls.dtls.mailingAddressData =
      bdmUtil.setBDMUnparsedIndForINTLAddress(
        bdmRegistrationDtls.dtls.mailingAddressData);
    // END TASK 118752

    // ADO-7089 -Start Tkonda added Validations on person registration screen
    validateRegistrationDetails(bdmRegistrationDtls);
    // ADO-7089 -End Tkonda added Validations on person registration screen
    String commMethod = "";
    String phoneType = "";
    // OOTB IMPLEMENTATION
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.assign(stateID);
    final ParticipantRegistrationWizardResult participantRegistrationWizardResult =
      new ParticipantRegistrationWizardResult();
    participantRegistrationWizardResult.wizardStateID = wizardStateID;
    if (actionID.actionIDProperty.equalsIgnoreCase("Save")) {

      final RegisterPersonState registerPersonState =
        (RegisterPersonState) wizardPersistentState
          .read(stateID.wizardStateID);

      // ADO-7089 -Start Tkonda added Validations on person registration screen
      // unset this values to temp variables to bypass OOTB Logic
      if (!bdmRegistrationDtls.dtls.prefCommMethod.isEmpty()) {

        commMethod = bdmRegistrationDtls.dtls.prefCommMethod;
        bdmRegistrationDtls.dtls.prefCommMethod = "";
      }

      if (!bdmRegistrationDtls.dtls.phoneType.isEmpty()) {

        phoneType = bdmRegistrationDtls.dtls.phoneType;
        bdmRegistrationDtls.dtls.phoneType = "";
      }
      // ADO-7089 -End Tkonda added Validations on person registration screen

      // if mailing address is the same
      // set residential address values the same as the mailing address values
      if (bdmRegistrationDtls.isMailingAddSame) {

        // setting Address data to be the same as mailing address data
        bdmRegistrationDtls.dtls.mailingAddressData =
          bdmRegistrationDtls.dtls.addressData;
      }

      // OOTB Logic for registration

      registerPersonState.registrationDtls = bdmRegistrationDtls.dtls;
      final PersonRegistrationDetails details =
        new PersonRegistrationDetails();
      details.personRegistrationDetails =
        registerPersonState.registrationDtls;
      final PersonRegistrationResult registrationResult =
        PersonFactory.newInstance().register(details);

      // ADO-7089 -Start Tkonda added Validations on person registration screen
      try {

        // implemented custom logic to support BDM Customizations
        if (!bdmRegistrationDtls.preferredOralLang.isEmpty()
          || !bdmRegistrationDtls.preferredWrittenLang.isEmpty()
          || !commMethod.isEmpty()) {
          // create ContactPreference
          bdmUtil.createContactPreferenceEvidence(
            registrationResult.registrationIDDetails.concernRoleID,
            bdmRegistrationDtls.preferredOralLang,
            bdmRegistrationDtls.preferredWrittenLang, commMethod);
        }

        // implemented custom logic as OOTB Phone Country code is hardcoded to
        // be string
        if (phoneType.length() > 0
          && bdmRegistrationDtls.dtls.phoneNumber.length() > 0) {

          final ParticipantPhoneDetails phoneDetails =
            new ParticipantPhoneDetails();
          phoneDetails.phoneAreaCode =
            details.personRegistrationDetails.phoneAreaCode;
          phoneDetails.phoneNumber =
            details.personRegistrationDetails.phoneNumber;
          phoneDetails.phoneExtension =
            details.personRegistrationDetails.phoneExtension;
          phoneDetails.typeCode = phoneType;
          phoneDetails.concernRoleID =
            registrationResult.registrationIDDetails.concernRoleID;
          phoneDetails.startDate = TransactionInfo.getSystemDate();
          phoneDetails.typeCode = phoneType;
          phoneDetails.endDate = Date.kZeroDate;
          phoneDetails.comments = "";

          bdmUtil.createPhoneNumberEvidence(phoneDetails,
            bdmRegistrationDtls.phoneCountryCode);

        }
      } catch (final Exception e) {

        Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
          + " Exception Occured during Registration and creation of Evidences :"
          + e);

      }
      // ADO-7089 -End Tkonda added Validations on person registration screen

      participantRegistrationWizardResult.registrationResult =
        registrationResult.registrationIDDetails;
      wizardPersistentState.remove(stateID.wizardStateID);
    }

    // ---------------------------------------------//

    return participantRegistrationWizardResult;
  }

  /**
   * util method to add validations
   *
   * @since ADO-7089
   * @param bdmRegistrationDtls
   */
  private void validateRegistrationDetails(
    final BDMPersonRegistrationDetails bdmRegistrationDtls)
    throws InformationalException, AppException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // Address validations
    validateAddress(bdmRegistrationDtls.dtls.addressData,
      informationalManager);

    // Validation-1
    if (!bdmRegistrationDtls.isMailingAddSame
      && isAddressEmpty(bdmRegistrationDtls.dtls.mailingAddressData)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_MAILING_ADDRESS_MANDATORY);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    } else if (!bdmRegistrationDtls.isMailingAddSame
      && !isAddressEmpty(bdmRegistrationDtls.dtls.mailingAddressData)) {

      validateAddress(bdmRegistrationDtls.dtls.mailingAddressData,
        informationalManager);
    }

    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();
    final CodeTableItemUniqueKey codeTableItemUniqueKey =
      new CodeTableItemUniqueKey();

    codeTableItemUniqueKey.code = bdmRegistrationDtls.phoneCountryCode;
    codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
    codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;

    final Boolean isPlusOneCountry =
      bdmRegistrationDtls.phoneCountryCode.isEmpty() ? false
        : codeTableAdminObj.readCTIDetailsForLocaleOrLanguage(
          codeTableItemUniqueKey).annotation.trim()
            .equals(BDMConstants.kphonePrefix);

    // ADO-101208 - Phone number not appearing in persons evidences if it is
    // added without selecting type.

    // phone info is supplied
    final boolean isPhoneDtlsEntered =
      !bdmRegistrationDtls.dtls.phoneAreaCode.trim().isEmpty()
        || !bdmRegistrationDtls.phoneCountryCode.trim().isEmpty()
        || !bdmRegistrationDtls.dtls.phoneExtension.trim().isEmpty()
        || !bdmRegistrationDtls.dtls.phoneNumber.trim().isEmpty();

    if (isPhoneDtlsEntered
      && bdmRegistrationDtls.dtls.phoneType.trim().isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMTHIRDPARTYCONTACT.ERR_PHONE_TYPE_MISSING_FOR_PHONE_NUMBER),
        "", InformationalType.kError);
    }

    // validation-2 if country code is +1, then area code is mandatory
    if (isPlusOneCountry
      && bdmRegistrationDtls.dtls.phoneAreaCode.isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_AREA_CODE);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    } else if (isPlusOneCountry
      && (!isNumeric(bdmRegistrationDtls.dtls.phoneAreaCode)
        || bdmRegistrationDtls.dtls.phoneAreaCode.length() != 3)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_AREA_CODE_3DIGIT);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    // validation-4 if country code is +1 then phone Number must be 7 digits and
    // numeric
    else if (isPlusOneCountry
      && bdmRegistrationDtls.dtls.phoneNumber.isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_MISSING);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    } else if (isPlusOneCountry
      && (!isNumeric(bdmRegistrationDtls.dtls.phoneNumber)
        || bdmRegistrationDtls.dtls.phoneNumber.length() != 7)) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_7DIGIT);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }
    // validation-5 if country code is not +1 then phone Number and Area code
    // must be numeric
    else if (!isPlusOneCountry
      && (!isNumeric(bdmRegistrationDtls.dtls.phoneAreaCode)
        || !isNumeric(bdmRegistrationDtls.dtls.phoneNumber))) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_NUMBER_OTHER_COUNTRY);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }
    // validation-6 if Phone Type is Personal or Business Mobile then extension
    // cannot be added
    else if ((bdmRegistrationDtls.dtls.phoneType
      .equals(PHONETYPE.PERSONAL_MOBILE)
      || bdmRegistrationDtls.dtls.phoneType.equals(PHONETYPE.BUSINESS_MOBILE))
      && !bdmRegistrationDtls.dtls.phoneExtension.isEmpty()) {

      final LocalisableString localisableString =
        new LocalisableString(BDMPERSON.ERR_PHONE_INVALID_TYPE);

      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);

    }

    // validation-7 Extension can only have numeric values
    else if (!isNumeric(bdmRegistrationDtls.dtls.phoneExtension)) {

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
    String state = this.getAddressDetails(otherAddressData, "STATEPROV");

    this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.POBOXNO);

    // city
    final String city =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.CITY);

    // province
    final String province =
      this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.PROVINCE);

    String zipCode =
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

      // BEGIN BUG 13283 International Address Validation Error
      // state is stored in the BDMSTPROV_X for international Address
      // 17746/17535 : Unable to Register Person with US Address format
      if (country.equalsIgnoreCase(COUNTRYCODE.USCODE)) {
        state = this.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.STATEPROV);
      } else {
        state = this.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.BDMSTPROV_X);
      }

      zipCode =
        this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.BDMZIP_X);
      // END BUG 13283

      // TASK 118752 The below validation will be checked only for US address
      if (state.isEmpty() && country.equalsIgnoreCase(COUNTRYCODE.USCODE)) {

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
   * Facade class for Person Search Screen on Person Registration Page
   *
   * @param searchKey
   * @param stateID
   * @param actionID
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public RegisterPersonWizardSearchDetails
    setRegisterPersonSearchCriteriaDetails(
      final PersonSearchWizardKey searchKey, final WizardStateID stateID,
      final ActionIDProperty actionID)
      throws AppException, InformationalException {

    String addressLine1 = "";

    if (CuramConst.kSearchAction
      .equalsIgnoreCase(actionID.actionIDProperty)) {

      addressLine1 = searchKey.searchKey.addressDtls.addressLine2;

      searchKey.searchKey.addressDtls.addressLine2 = "";

    }

    final ParticipantRegistration participantRegistration =
      ParticipantRegistrationFactory.newInstance();
    RegisterPersonWizardSearchDetails registerPersonWizardSearchDetails =
      participantRegistration
        .setRegisterPersonSearchCriteriaDetails(searchKey, stateID, actionID);

    if (CuramConst.kSearchAction.equalsIgnoreCase(actionID.actionIDProperty)
      && !addressLine1.isEmpty()
      && registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
        .size() > 0) {

      registerPersonWizardSearchDetails = bdmUtil.filterAddress(
        registerPersonWizardSearchDetails, addressLine1.toUpperCase());

    }

    return registerPersonWizardSearchDetails;
  }

  /**
   *
   * Facade class for Person Registration Screen on Prospect Registration Page
   *
   * @since ADO-7089
   * @param searchKey
   * @param stateID
   * @param actionID
   * @throws AppException
   * @throws InformationalException
   *
   */
  @Override
  public RegisterPersonWizardSearchDetails
    setAddProspectPersonSearchCriteriaDetails(
      final PersonSearchWizardKey searchKey, final WizardStateID stateID,
      final ActionIDProperty actionID)
      throws AppException, InformationalException {

    final String addressLine2 = searchKey.searchKey.addressDtls.addressLine2;

    searchKey.searchKey.addressDtls.addressLine2 = "";

    final ParticipantRegistration participantRegistration =
      ParticipantRegistrationFactory.newInstance();
    RegisterPersonWizardSearchDetails registerPersonWizardSearchDetails =
      participantRegistration.setAddProspectPersonSearchCriteriaDetails(
        searchKey, stateID, actionID);
    if (CuramConst.kSearchAction.equalsIgnoreCase(actionID.actionIDProperty)
      && !addressLine2.isEmpty()
      && registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
        .size() > 0) {

      registerPersonWizardSearchDetails = bdmUtil.filterAddress(
        registerPersonWizardSearchDetails, addressLine2.toUpperCase());

    }

    return registerPersonWizardSearchDetails;
  }

  /**
   *
   * Facade class for Person Registration Screen on Prospect Registration Page
   *
   * @since ADO-7089
   * @param registrationDtls
   * @param stateID
   * @param actionID
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public ParticipantRegistrationWizardResult setAddProspectPersonDetails(
    final BDMProspectPersonRegistrationDetails registrationDtls,
    final WizardStateID stateID, final ActionIDProperty actionID)
    throws AppException, InformationalException {

    // OOTB implementation
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    final WizardStateID wizardStateID = new WizardStateID();

    wizardStateID.assign(stateID);

    // START TASK 118752 : add a logic to set the BDMUNPARSE to 1 for the INTL
    // Address
    registrationDtls.dtls.prospectPersonRegistrationDtls.addressData =
      bdmUtil.setBDMUnparsedIndForINTLAddress(
        registrationDtls.dtls.prospectPersonRegistrationDtls.addressData);
    registrationDtls.dtls.prospectPersonRegistrationDtls.mailingAddressData =
      bdmUtil.setBDMUnparsedIndForINTLAddress(
        registrationDtls.dtls.prospectPersonRegistrationDtls.mailingAddressData);
    // END TASK 118752 :

    final ParticipantRegistrationWizardResult participantRegistrationWizardResult =
      new ParticipantRegistrationWizardResult();
    participantRegistrationWizardResult.wizardStateID = wizardStateID;

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final ParticipantPhoneDetails phoneDetails =
      new ParticipantPhoneDetails();
    phoneDetails.phoneAreaCode =
      registrationDtls.dtls.prospectPersonRegistrationDtls.phoneAreaCode;
    phoneDetails.phoneNumber =
      registrationDtls.dtls.prospectPersonRegistrationDtls.phoneNumber;
    phoneDetails.phoneExtension =
      registrationDtls.dtls.prospectPersonRegistrationDtls.phoneExtension;
    // ADO-21276 -Start vmadichetty added Validations on prospect person
    // registration screen
    final BDMPersonRegistrationDetails dtls =
      new BDMPersonRegistrationDetails();
    dtls.assign(registrationDtls);
    // BEGIN TASK-27207 Validation Error For State In Canadian Address
    dtls.dtls.assign(registrationDtls.dtls.prospectPersonRegistrationDtls);
    // END TASK-27207
    validateRegistrationDetails(dtls);
    // ADO-21276 -Start vmadichetty added Validations on prospect person
    // registration screen
    if (!isAddressEmpty(
      registrationDtls.dtls.prospectPersonRegistrationDtls.addressData)) {
      validateAddress(
        registrationDtls.dtls.prospectPersonRegistrationDtls.addressData,
        informationalManager);
    }
    if (actionID.actionIDProperty.equalsIgnoreCase(CuramConst.kSaveAction)) {

      String commMethod = "";
      String phoneType = "";
      String phoneNumber = "";
      // ADO-7089 -Start Tkonda added Validations on person registration screen
      // unset this values to temp variables to bypass OOTB Logic
      if (!registrationDtls.dtls.prospectPersonRegistrationDtls.prefCommMethod
        .isEmpty()) {

        commMethod =
          registrationDtls.dtls.prospectPersonRegistrationDtls.prefCommMethod;
        registrationDtls.dtls.prospectPersonRegistrationDtls.prefCommMethod =
          "";
      }

      if (!registrationDtls.dtls.prospectPersonRegistrationDtls.phoneType
        .isEmpty()) {

        phoneType =
          registrationDtls.dtls.prospectPersonRegistrationDtls.phoneType;
        registrationDtls.dtls.prospectPersonRegistrationDtls.phoneType = "";

        phoneNumber =
          registrationDtls.dtls.prospectPersonRegistrationDtls.phoneNumber;
        registrationDtls.dtls.prospectPersonRegistrationDtls.phoneNumber = "";
        registrationDtls.dtls.prospectPersonRegistrationDtls.phoneAreaCode =
          "";
        registrationDtls.dtls.prospectPersonRegistrationDtls.phoneCountryCode =
          "";
        registrationDtls.dtls.prospectPersonRegistrationDtls.phoneExtension =
          "";

      }
      // ADO-7089 -End Tkonda added Validations on person registration screen

      AddProspectPersonState addProspectPersonState =
        new AddProspectPersonState();
      try {
        addProspectPersonState =
          (AddProspectPersonState) wizardPersistentState
            .read(stateID.wizardStateID);
      } catch (final ClassCastException cs) {
        final RegisterPersonState registerPersonState =
          (RegisterPersonState) wizardPersistentState
            .read(stateID.wizardStateID);
        addProspectPersonState.registrationDtls.prospectPersonRegistrationDtls
          .assign(registerPersonState.registrationDtls);

      }

      // if mailing address is the same
      // set residential address values the same as the mailing address values
      if (registrationDtls.isMailingAddSame && !isAddressEmpty(
        registrationDtls.dtls.prospectPersonRegistrationDtls.addressData)) {

        // setting Address data to be the same as mailing address data
        registrationDtls.dtls.prospectPersonRegistrationDtls.mailingAddressData =
          registrationDtls.dtls.prospectPersonRegistrationDtls.addressData;
      }

      // Calling OOTB implementation
      addProspectPersonState.registrationDtls = registrationDtls.dtls;

      ProspectPersonRegistrationDetails details =
        new ProspectPersonRegistrationDetails();

      details = addProspectPersonState.registrationDtls;

      final ProspectPersonRegistrationResult registrationResult =
        ProspectPersonFactory.newInstance().registerProspectPerson(details);

      participantRegistrationWizardResult.registrationResult =
        registrationResult.registrationIDDetails;

      // ADO-7089 -Start Tkonda added Validations on person registration screen
      try {

        // implemented custom logic to support BDM Customizations
        if (!registrationDtls.preferredOralLang.isEmpty()
          || !registrationDtls.preferredWrittenLang.isEmpty()
          || !commMethod.isEmpty()) {
          // create ContactPreference
          bdmUtil.createContactPreferenceEvidence(
            registrationResult.registrationIDDetails.concernRoleID,
            registrationDtls.preferredOralLang,
            registrationDtls.preferredWrittenLang, commMethod);
        }

        // implemented custom logic as OOTB Phone Country code is hardcoded to
        // be string
        if (phoneType.length() > 0 && phoneNumber.length() > 0) {
          phoneDetails.concernRoleID =
            registrationResult.registrationIDDetails.concernRoleID;
          phoneDetails.startDate = TransactionInfo.getSystemDate();
          phoneDetails.typeCode = phoneType;
          phoneDetails.endDate = Date.kZeroDate;
          phoneDetails.comments = "";

          bdmUtil.createPhoneNumberEvidence(phoneDetails,
            registrationDtls.phoneCountryCode);

        }
      } catch (final Exception e) {

        Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
          + " Exception Occured during Registration and creation of Evidences :"
          + e);

      }
      // ADO-7089 -End Tkonda added Validations on person registration screen

      wizardPersistentState.remove(stateID.wizardStateID);
    }

    return participantRegistrationWizardResult;
  }

  /**
   *
   * Person wizard search - 8914
   * This method calls the OOTB person search and then filters search result by
   * using additional search criteria of province, postal code and country
   */
  @Override
  public BDMRegisterPersonWizardSearchDetails
    setRegisterPersonSearchCriteriaDetailsExt(
      final BDMPersonSearchWizardKey searchKey, final WizardStateID stateID,
      final ActionIDProperty actionID)
      throws AppException, InformationalException {

    boolean isPersonNotFound = false;
    if (!isValidSearchCriteria(searchKey.dtls.searchKey.referenceNumber,
      searchKey.correspondenceTrackingNumber,
      searchKey.dtls.searchKey.forename, searchKey.dtls.searchKey.surname,
      searchKey.dtls.searchKey.birthSurname)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_INVALID_PERSON_SEARCH_CRITERIA), "",
        InformationalType.kError);
    }
    if (!isValidSearchCombination(searchKey.dtls.searchKey.referenceNumber,
      searchKey.correspondenceTrackingNumber,
      searchKey.dtls.searchKey.forename, searchKey.dtls.searchKey.surname,
      searchKey.dtls.searchKey.dateOfBirth, searchKey.stateProvince,
      searchKey.postalCode, searchKey.countryCode,
      searchKey.dtls.searchKey.birthSurname)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_SEARCH_ADDITIONAL_MANDATORY_MISSING),
        "", InformationalType.kError);
    }
    BDMRegisterPersonWizardSearchDetails bdmRegisterPersonWizardSearchDetails =
      new BDMRegisterPersonWizardSearchDetails();

    final PersonSearchWizardKey personSearchWizardKey =
      new PersonSearchWizardKey();
    personSearchWizardKey.assign(searchKey.dtls);

    final ParticipantRegistration participantRegistration =
      ParticipantRegistrationFactory.newInstance();

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();
    // Task - 554832 2022-06-11 Mohan
    if (CuramConst.kSearchAction
      .equalsIgnoreCase(actionID.actionIDProperty)) {
      if (searchKey.dtls.searchKey.referenceNumber.isEmpty()
        && searchKey.correspondenceTrackingNumber.isEmpty()
        && searchKey.dtls.searchKey.forename.isEmpty()
        && searchKey.dtls.searchKey.surname.isEmpty()) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMPERSON.ERR_SEARCH_MANDATORY_MISSING), "",
          InformationalType.kError);
      }

      if (!searchKey.dtls.searchKey.addressDtls.city.isEmpty()
        && searchKey.dtls.searchKey.addressDtls.city.length() < 2) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMPERSON.ERR_CITY_2CHARS), "",
          InformationalType.kError);
      }

      if (!searchKey.stateProvince.isEmpty()
        && searchKey.stateProvince.length() < 3) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMPERSON.ERR_PROVINCE_3CHARS), "",
          InformationalType.kError);
      }
      if (!searchKey.postalCode.isEmpty()
        && searchKey.postalCode.length() < 3) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMPERSON.ERR_POSTALCODE_3CHARS), "",
          InformationalType.kError);
      }

      if (informationalManager.operationHasInformationals()) {
        bdmRegisterPersonWizardSearchDetails.isNextButtonEnabled = false;
      } else {
        bdmRegisterPersonWizardSearchDetails.isNextButtonEnabled = true;
      }

      informationalManager.failOperation();
    }
    String altId = null;

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    final RegisterPersonWizardSearchDetails registerPersonWizardSearchResult =
      new RegisterPersonWizardSearchDetails();
    registerPersonWizardSearchResult.wizardStateID = stateID;
    RegisterPersonState registerPersonState;
    if (CuramConst.kNextPageAction
      .equalsIgnoreCase(actionID.actionIDProperty)) {
      registerPersonState = (RegisterPersonState) wizardPersistentState
        .read(stateID.wizardStateID);
      registerPersonState.searchKey = personSearchWizardKey;
      registerPersonState.searchKey.stateID = stateID;
      wizardPersistentState.modify(stateID.wizardStateID,
        registerPersonState);
    } else if (CuramConst.kSearchAction
      .equalsIgnoreCase(actionID.actionIDProperty)) {
      try {
        if (!searchKey.correspondenceTrackingNumber.isEmpty()
          && !searchKey.dtls.searchKey.referenceNumber.isEmpty()) {
          // Perform OOTB serach
          final PersonSearchDetailsResult refSearchResults =
            participantRegistration
              .searchPersonDetails(personSearchWizardKey);

          altId = getAlternateIDByTrackingNumber(searchKey);

          // When only tracking number is specified
          if (!Objects.isNull(altId)) {
            personSearchWizardKey.searchKey.referenceNumber = altId;
          }
          final PersonSearchDetailsResult trackingNumSearchResults =
            participantRegistration
              .searchPersonDetails(personSearchWizardKey);

          registerPersonWizardSearchResult.searchResult =
            compareResults(refSearchResults, trackingNumSearchResults);

        } else {
          // Perform search when either reference number or tracking number is
          // provided
          if (!searchKey.correspondenceTrackingNumber.isEmpty()
            && searchKey.dtls.searchKey.referenceNumber.isEmpty()) {
            altId = getAlternateIDByTrackingNumber(searchKey);
          }

          // When only tracking number is specified
          if (!Objects.isNull(altId)) {
            personSearchWizardKey.searchKey.referenceNumber = altId;
          }
          registerPersonWizardSearchResult.searchResult =
            participantRegistration
              .searchPersonDetails(personSearchWizardKey);
        }

      } catch (final Exception e) {
        isPersonNotFound = true;
      }
    } else if (CuramConst.kResetAction
      .equalsIgnoreCase(actionID.actionIDProperty)) {
      registerPersonState = (RegisterPersonState) wizardPersistentState
        .read(stateID.wizardStateID);
      registerPersonState.searchKey = new PersonSearchWizardKey();
      wizardPersistentState.modify(stateID.wizardStateID,
        registerPersonState);
    }

    bdmRegisterPersonWizardSearchDetails
      .assign(registerPersonWizardSearchResult);

    // call filtering logic
    if (CuramConst.kSearchAction.equalsIgnoreCase(actionID.actionIDProperty)
      && bdmRegisterPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
        .size() > 0) {

      bdmRegisterPersonWizardSearchDetails =
        bdmUtil.filterAddressPersonWizardSearch(
          bdmRegisterPersonWizardSearchDetails, searchKey);

    }
    if (isPersonNotFound) {
      bdmRegisterPersonWizardSearchDetails.isNextButtonEnabled = true;
    } else if (informationalManager.operationHasInformationals()) {
      bdmRegisterPersonWizardSearchDetails.isNextButtonEnabled = false;
    } else {
      bdmRegisterPersonWizardSearchDetails.isNextButtonEnabled = true;
    }
    maskSinNumber(bdmRegisterPersonWizardSearchDetails.searchResult);

    final BDMMaintainExternalParty bDMMaintainExternalParty =
      new BDMMaintainExternalParty();
    bDMMaintainExternalParty.collectInformationalMsgs(
      bdmRegisterPersonWizardSearchDetails.searchResult.informationalMsgDtls);
    return bdmRegisterPersonWizardSearchDetails;
  }

  /**
   * util method to get person search criteria using wizardStateID
   *
   * @since ADO-7089
   * @param wizardStateID
   */
  @Override
  public BDMPersonSearchWizardKey
    getRegisterPersonSearchCriteria(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();

    final ParticipantRegistration participantRegistration =
      ParticipantRegistrationFactory.newInstance();
    PersonSearchWizardKey personSearchWizardKey = new PersonSearchWizardKey();
    // PersonSearchWizardKey getRegisterPersonSearchCriteria(WizardStateID
    // wizardStateID)
    personSearchWizardKey =
      participantRegistration.getRegisterPersonSearchCriteria(wizardStateID);

    bdmPersonSearchWizardKey.dtls.assign(personSearchWizardKey);

    return bdmPersonSearchWizardKey;
  }

  /**
   * Prospect search person - 8914
   * This method calls the OOTB person search and then filters search result by
   * using additional search criteria of province, postal code and country
   */
  @Override
  public BDMRegisterPersonWizardSearchDetails
    setAddProspectPersonSearchCriteriaDetailsExt(
      final BDMPersonSearchWizardKey searchKey, final WizardStateID stateID,
      final ActionIDProperty actionID)
      throws AppException, InformationalException {

    boolean isProspectPersonNotFound = false;
    if (!isValidSearchCriteria(searchKey.dtls.searchKey.referenceNumber,
      searchKey.correspondenceTrackingNumber,
      searchKey.dtls.searchKey.forename, searchKey.dtls.searchKey.surname,
      searchKey.dtls.searchKey.birthSurname)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_INVALID_PERSON_SEARCH_CRITERIA), "",
        InformationalType.kError);
    }

    if (!isValidSearchCombination(searchKey.dtls.searchKey.referenceNumber,
      searchKey.correspondenceTrackingNumber,
      searchKey.dtls.searchKey.forename, searchKey.dtls.searchKey.surname,
      searchKey.dtls.searchKey.dateOfBirth, searchKey.stateProvince,
      searchKey.postalCode, searchKey.countryCode,
      searchKey.dtls.searchKey.birthSurname)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_SEARCH_ADDITIONAL_MANDATORY_MISSING),
        "", InformationalType.kError);
    }

    BDMRegisterPersonWizardSearchDetails bdmRegisterPersonWizardSearchDetails =
      new BDMRegisterPersonWizardSearchDetails();

    final ParticipantRegistration participantRegistration =
      ParticipantRegistrationFactory.newInstance();

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (searchKey.dtls.searchKey.referenceNumber.isEmpty()
      && searchKey.correspondenceTrackingNumber.isEmpty()
      && searchKey.dtls.searchKey.forename.isEmpty()
      && searchKey.dtls.searchKey.surname.isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_SEARCH_MANDATORY_MISSING), "",
        InformationalType.kError);
    }

    if (!searchKey.dtls.searchKey.addressDtls.city.isEmpty()
      && searchKey.dtls.searchKey.addressDtls.city.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_CITY_2CHARS), "",
        InformationalType.kError);
    }

    if (!searchKey.stateProvince.isEmpty()
      && searchKey.stateProvince.length() < 3) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_PROVINCE_3CHARS), "",
        InformationalType.kError);
    }
    if (!searchKey.postalCode.isEmpty()
      && searchKey.postalCode.length() < 3) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_POSTALCODE_3CHARS), "",
        InformationalType.kError);
    }

    if (informationalManager.operationHasInformationals()) {
      bdmRegisterPersonWizardSearchDetails.isNextButtonEnabled = false;
    } else {
      bdmRegisterPersonWizardSearchDetails.isNextButtonEnabled = true;
    }
    informationalManager.failOperation();

    final PersonSearchWizardKey personSearchWizardKey =
      new PersonSearchWizardKey();
    personSearchWizardKey.assign(searchKey.dtls);
    String altId = null;
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    final WizardStateID wizardStateIDObj = new WizardStateID();
    wizardStateIDObj.assign(stateID);
    final RegisterPersonWizardSearchDetails registerPersonWizardSearchResult =
      new RegisterPersonWizardSearchDetails();
    registerPersonWizardSearchResult.wizardStateID = stateID;
    if (CuramConst.kNextPageAction
      .equalsIgnoreCase(actionID.actionIDProperty)) {

      final AddProspectPersonState addProspectPersonState =
        (AddProspectPersonState) wizardPersistentState
          .read(stateID.wizardStateID);
      addProspectPersonState.searchKey = personSearchWizardKey;
      addProspectPersonState.searchKey.stateID = stateID;
      wizardPersistentState.modify(wizardStateIDObj.wizardStateID,
        addProspectPersonState);
    } else if (CuramConst.kSearchAction
      .equalsIgnoreCase(actionID.actionIDProperty)) {
      try {
        if (!searchKey.correspondenceTrackingNumber.isEmpty()
          && !searchKey.dtls.searchKey.referenceNumber.isEmpty()) {
          // Perform OOTB serach
          final PersonSearchDetailsResult refSearchResults =
            participantRegistration
              .searchPersonDetails(personSearchWizardKey);

          altId = getAlternateIDByTrackingNumber(searchKey);

          // When only tracking number is specified
          if (!Objects.isNull(altId)) {
            personSearchWizardKey.searchKey.referenceNumber = altId;
          }
          final PersonSearchDetailsResult trackingNumSearchResults =
            participantRegistration
              .searchPersonDetails(personSearchWizardKey);

          registerPersonWizardSearchResult.searchResult =
            compareResults(refSearchResults, trackingNumSearchResults);

        } else {
          // Perform search when either reference number or tracking number is
          // provided
          if (!searchKey.correspondenceTrackingNumber.isEmpty()
            && searchKey.dtls.searchKey.referenceNumber.isEmpty()) {
            altId = getAlternateIDByTrackingNumber(searchKey);
          }

          // When only tracking number is specified
          if (!Objects.isNull(altId)) {
            personSearchWizardKey.searchKey.referenceNumber = altId;
          }
          registerPersonWizardSearchResult.searchResult =
            participantRegistration
              .searchPersonDetails(personSearchWizardKey);
        }

      } catch (final Exception e) {
        isProspectPersonNotFound = true;
      }
      final int personSrchRsltSize =
        registerPersonWizardSearchResult.searchResult.personSearchResult.dtlsList
          .size();

      String concernRoleReferenceNumber = "";

      int strStartIndex = 0;
      int strEndIndex = 0;
      String subStringContentPanelxmlPersonData1 = "";
      String subStringContentPanelxmlPersonData2 = "";

      final ConcernRoleAlternateID concernRoleAlternateID =
        ConcernRoleAlternateIDFactory.newInstance();

      final ConcernRoleAlternateIDRMKey concernRoleAlternateIDRMKey =
        new ConcernRoleAlternateIDRMKey();

      AlternateIDReadmultiDtlsList alternateIDReadmultiDtlsList =
        new AlternateIDReadmultiDtlsList();
      AlternateIDReadmultiDtls alternateIDReadmultiDtls = null;

      if (personSrchRsltSize > 0) {

        for (int i = 0; i < personSrchRsltSize; i++) {

          if (registerPersonWizardSearchResult.searchResult.personSearchResult.dtlsList
            .item(0).concernRoleID != CuramConst.kLongZero) {

            concernRoleAlternateIDRMKey.concernRoleID =
              registerPersonWizardSearchResult.searchResult.personSearchResult.dtlsList
                .item(0).concernRoleID;

            alternateIDReadmultiDtlsList = concernRoleAlternateID
              .searchByConcernRole(concernRoleAlternateIDRMKey);

            final int alternateIDListSize =
              alternateIDReadmultiDtlsList.dtls.size();
            if (alternateIDListSize > 0) {
              for (int j = 0; j < alternateIDListSize; j++) {
                alternateIDReadmultiDtls = new AlternateIDReadmultiDtls();
                alternateIDReadmultiDtls =
                  alternateIDReadmultiDtlsList.dtls.item(j);
                if (alternateIDReadmultiDtls.typeCode
                  .equals(CONCERNROLEALTERNATEID.REFERENCE_NUMBER)) {
                  concernRoleReferenceNumber =
                    alternateIDReadmultiDtls.alternateID.toString();
                  break;
                }
              }
            }

            if (registerPersonWizardSearchResult.searchResult.personSearchResult.dtlsList
              .item(i).xmlPersonData
                .indexOf(concernRoleReferenceNumber) != -1) {
              continue;
            } else {

              strStartIndex =
                registerPersonWizardSearchResult.searchResult.personSearchResult.dtlsList
                  .item(i).xmlPersonData.indexOf("|");

              subStringContentPanelxmlPersonData1 =
                registerPersonWizardSearchResult.searchResult.personSearchResult.dtlsList
                  .item(i).xmlPersonData.substring(0, strStartIndex);

              strEndIndex =
                registerPersonWizardSearchResult.searchResult.personSearchResult.dtlsList
                  .item(i).xmlPersonData.indexOf(")</displayText>");

              subStringContentPanelxmlPersonData2 =
                registerPersonWizardSearchResult.searchResult.personSearchResult.dtlsList
                  .item(i).xmlPersonData.substring(strEndIndex + 1,
                    registerPersonWizardSearchResult.searchResult.personSearchResult.dtlsList
                      .item(i).xmlPersonData.length());

              registerPersonWizardSearchResult.searchResult.personSearchResult.dtlsList
                .item(i).xmlPersonData =
                  subStringContentPanelxmlPersonData1 + "|"
                    + concernRoleReferenceNumber + ")"
                    + subStringContentPanelxmlPersonData2;

            }

          }

        }
      }

    } else if (CuramConst.kResetAction
      .equalsIgnoreCase(actionID.actionIDProperty)) {
      final AddProspectPersonState addProspectPersonState =
        (AddProspectPersonState) wizardPersistentState
          .read(stateID.wizardStateID);
      addProspectPersonState.searchKey = new PersonSearchWizardKey();
      wizardPersistentState.modify(stateID.wizardStateID,
        addProspectPersonState);
    }

    bdmRegisterPersonWizardSearchDetails
      .assign(registerPersonWizardSearchResult);

    // call filtering logic
    if (CuramConst.kSearchAction.equalsIgnoreCase(actionID.actionIDProperty)
      && bdmRegisterPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
        .size() > 0) {

      bdmRegisterPersonWizardSearchDetails =
        bdmUtil.filterAddressPersonWizardSearch(
          bdmRegisterPersonWizardSearchDetails, searchKey);

    }

    if (isProspectPersonNotFound) {
      bdmRegisterPersonWizardSearchDetails.isNextButtonEnabled = true;
    } else if (informationalManager.operationHasInformationals()) {
      bdmRegisterPersonWizardSearchDetails.isNextButtonEnabled = false;
    } else {
      bdmRegisterPersonWizardSearchDetails.isNextButtonEnabled = true;
    }
    maskSinNumber(bdmRegisterPersonWizardSearchDetails.searchResult);
    final BDMMaintainExternalParty bDMMaintainExternalParty =
      new BDMMaintainExternalParty();
    bDMMaintainExternalParty.collectInformationalMsgs(
      bdmRegisterPersonWizardSearchDetails.searchResult.informationalMsgDtls);

    return bdmRegisterPersonWizardSearchDetails;
  }

  @Override
  public BDMPersonSearchWizardKey
    getAddProspectPersonSearchCriteria(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final BDMPersonSearchWizardKey bdmPersonSearchWizardKey =
      new BDMPersonSearchWizardKey();

    final ParticipantRegistration participantRegistration =
      ParticipantRegistrationFactory.newInstance();
    PersonSearchWizardKey personSearchWizardKey = new PersonSearchWizardKey();

    personSearchWizardKey = participantRegistration
      .getAddProspectPersonSearchCriteria(wizardStateID);

    bdmPersonSearchWizardKey.dtls.assign(personSearchWizardKey);

    return bdmPersonSearchWizardKey;
  }

  /**
   * Retrive alternate id of a person when correspondence tracking number is
   * provided
   *
   * @param key
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  private String
    getAlternateIDByTrackingNumber(final BDMPersonSearchWizardKey key)
      throws AppException, InformationalException {

    List<String> alternalteIDList = new ArrayList<String>();
    String altId = "";
    long trackingNum = 0;
    try {
      trackingNum = Long.parseLong(key.correspondenceTrackingNumber);

      final BDMSearchByTrackingNumberKey trackingKey =
        new BDMSearchByTrackingNumberKey();

      trackingKey.trackingNumber = trackingNum;

      final BDMPersonSearchResultByTrackingNumList bdmPersonSearchResultByTrackingNumList =
        BDMPersonFactory.newInstance().searchByTrackingNumber(trackingKey);

      if (!bdmPersonSearchResultByTrackingNumList.dtls.isEmpty()) {
        alternalteIDList =
          getPersonSearchResult(bdmPersonSearchResultByTrackingNumList);
      } else
        altId = String.valueOf(0);

      if (!alternalteIDList.isEmpty() && alternalteIDList.size() == 1) {
        altId = alternalteIDList.get(0);
      }
    } catch (final NumberFormatException exception) {
      Trace.kTopLevelLogger
        .error("Invalid Tracking Number:" + exception.getMessage());
      trackingNum = 0;
      altId = String.valueOf(0);
    }
    return altId;
  }

  private List<String>
    getPersonSearchResult(final BDMPersonSearchResultByTrackingNumList str) {

    final List<String> alternateIDList = new ArrayList<>();
    for (int i = 0; i < str.dtls.size(); i++) {

      final BDMPersonSearchResultByTrackingNum obj = str.dtls.get(i);

      alternateIDList.add(obj.primaryAlternateID);

    }
    return alternateIDList;

  }

  /**
   * Performs OOTB serach and filter results based on addition search criteria
   * Comment : Extractd OOTB search code to reduce Complexity of method
   */
  private BDMPersonSearchDetailsResult
    performOOTBSearch(final BDMPersonSearchKey1 personSearchKey1)
      throws AppException, InformationalException {

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();
    // OOTB Logic
    final Person personObj = PersonFactory.newInstance();
    final PersonSearchDetailsResult personSearchDetailsResult =
      personObj.searchPerson(personSearchKey1.dtls);

    bdmPersonSearchDetailsResult.assign(personSearchDetailsResult);

    // call filtering logic
    if (bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() > 0) {

      bdmPersonSearchDetailsResult = bdmUtil.filterAddressForPerson(
        bdmPersonSearchDetailsResult, personSearchKey1);

    }
    return bdmPersonSearchDetailsResult;
  }

  /**
   * Compare person search result list
   */

  public static PersonSearchDetailsResult compareResults(
    final PersonSearchDetailsResult refSearchResult,
    final PersonSearchDetailsResult trackingNumberSearchResult) {

    final PersonSearchDetailsResult personSearchDetailsResult =
      new PersonSearchDetailsResult();
    if (refSearchResult.personSearchResult.dtlsList
      .size() == trackingNumberSearchResult.personSearchResult.dtlsList
        .size()) {

      if (trackingNumberSearchResult.personSearchResult.dtlsList
        .get(0).concernRoleID == refSearchResult.personSearchResult.dtlsList
          .get(0).concernRoleID) {

        personSearchDetailsResult.assign(trackingNumberSearchResult);
      }

    }
    return personSearchDetailsResult;
  }

  /**
   * Returns the wizard properties file.
   *
   * @return The wizard properties file.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public WizardProperties getBDMIORegisterPersonWizard()
    throws AppException, InformationalException {

    final WizardProperties wizardProperties = new WizardProperties();

    wizardProperties.wizardMenu =
      BDMConstants.kBDMIORegisterPersonForPDCWizardProperties;
    // END, CR00356064
    return wizardProperties;
  }

  /*
   * This method is used to register client and create FEC case for IO Users
   *
   *
   */

  @Override
  public ParticipantRegistrationWizardResult registerClientandCreateFEC(
    final BDMPersonRegistrationDetails bdmRegistrationDtls,
    final WizardStateID stateID, final ActionIDProperty actionID)
    throws AppException, InformationalException {

    ParticipantRegistrationWizardResult participantRegistrationWizardResult =
      new ParticipantRegistrationWizardResult();

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (actionID.actionIDProperty.equalsIgnoreCase("Save")) {
      // validate SIN Number if entered
      final String sinNumber = bdmRegistrationDtls.dtls.socialSecurityNumber;
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
          informationalManager.failOperation();
        }
      }

      // set the sinNumber null so system generate Reference Number evidence
      bdmRegistrationDtls.dtls.socialSecurityNumber = "";

      /*
       * The data field Preferred method of Correspondence (including tax slips)
       * is hidden from Register Client and Create Foreign Engagement Case
       * modal.
       * System must pass a value for Preferred method of Correspondence
       * (including tax slips)
       * with a value selected as ‘Digital and hard copy by mail’ upon
       * successful saving
       */
      bdmRegistrationDtls.preferredCorrDeliveryMethod =
        BDMCORRESDELIVERY.POSTALDIG;

      final boolean isCountryRestricted =
        BDMUtil.isCountryRestricted(bdmRegistrationDtls.countryCode);

      /*
       * If Country entered= Restricted Country AND Received From=Foreign
       * Government, then register the participant as a Prospect Person
       *
       * If Country entered = Restricted Country AND Received From= Client
       * Reported or Representative, then register the participant as Person
       *
       * If Country entered= Non - Restricted Country AND Received From=Foreign
       * Government, then register the participant as a Person
       *
       * If Country entered = Non- Restricted Country AND Received From= Client
       * Reported or Representative, then register the participant as Person
       */
      if (isCountryRestricted && bdmRegistrationDtls.bdmReceivedFrom
        .equalsIgnoreCase(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT)) {
        // register the participant as a Prospect Person

        final BDMProspectPersonRegistrationDetails bdmProspectPersonRegistrationDetails =
          new BDMProspectPersonRegistrationDetails();

        bdmProspectPersonRegistrationDetails.isMailingAddSame =
          bdmRegistrationDtls.isMailingAddSame;
        bdmProspectPersonRegistrationDetails.phoneCountryCode =
          bdmRegistrationDtls.phoneCountryCode;
        bdmProspectPersonRegistrationDetails.preferredOralLang =
          bdmRegistrationDtls.preferredOralLang;
        bdmProspectPersonRegistrationDetails.preferredWrittenLang =
          bdmRegistrationDtls.preferredWrittenLang;

        bdmProspectPersonRegistrationDetails.dtls.prospectPersonRegistrationDtls
          .assign(bdmRegistrationDtls.dtls);

        // START BUG - 113480
        // set Gender value to "empty" s it's creating the Evidence with
        // "UNKNOWN"

        bdmProspectPersonRegistrationDetails.dtls.prospectPersonRegistrationDtls.gender =
          BDMConstants.EMPTY_STRING;

        // END BUG - 113480

        participantRegistrationWizardResult = setAddProspectPersonDetails(
          bdmProspectPersonRegistrationDetails, stateID, actionID);

        createFECCase(bdmRegistrationDtls.countryCode,
          participantRegistrationWizardResult.registrationResult.concernRoleID);

        // call SIN-SIR Check
        if (!StringUtil.isNullOrEmpty(sinNumber)) {
          bdmUtil.callSINSIR(sinNumber,
            participantRegistrationWizardResult.registrationResult.concernRoleID);
        }

        // Bug 103734, Bug 104207 - FIXED
        // modify evidence for receivedFrom, Received From Country and Mode Of
        // receipt
        bdmUtil.updateEvidenceAttributes(bdmRegistrationDtls.bdmReceivedFrom,
          bdmRegistrationDtls.countryCode,
          participantRegistrationWizardResult.registrationResult.concernRoleID);

      } else if (isCountryRestricted && (bdmRegistrationDtls.bdmReceivedFrom
        .equalsIgnoreCase(BDMRECEIVEDFROM.CLIENT_REPORTED)
        || bdmRegistrationDtls.bdmReceivedFrom
          .equalsIgnoreCase(BDMRECEIVEDFROM.REPRESENTATIVE))) {
        // register the participant as a Person
        participantRegistrationWizardResult = setRegisterPersonForPDCDetails(
          bdmRegistrationDtls, stateID, actionID);
        // create FEC Case
        createFECCase(bdmRegistrationDtls.countryCode,
          participantRegistrationWizardResult.registrationResult.concernRoleID);

        // call SIN-SIR Check
        if (!StringUtil.isNullOrEmpty(sinNumber)) {
          bdmUtil.callSINSIR(sinNumber,
            participantRegistrationWizardResult.registrationResult.concernRoleID);
        }

        // Bug 103734, Bug 104207 - FIXED
        // modify evidence for receivedFrom, Received From Country and Mode Of
        // receipt
        bdmUtil.updateEvidenceAttributes(bdmRegistrationDtls.bdmReceivedFrom,
          CuramConst.gkEmpty,
          participantRegistrationWizardResult.registrationResult.concernRoleID);

      } else if (!isCountryRestricted && bdmRegistrationDtls.bdmReceivedFrom
        .equalsIgnoreCase(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT)) {
        // register the participant as a Person
        participantRegistrationWizardResult = setRegisterPersonForPDCDetails(
          bdmRegistrationDtls, stateID, actionID);
        // create FEC Case
        createFECCase(bdmRegistrationDtls.countryCode,
          participantRegistrationWizardResult.registrationResult.concernRoleID);

        // call SIN-SIR Check
        if (!StringUtil.isNullOrEmpty(sinNumber)) {
          bdmUtil.callSINSIR(sinNumber,
            participantRegistrationWizardResult.registrationResult.concernRoleID);
        }

        // Bug 103734, Bug 104207 - FIXED
        // modify evidence for receivedFrom, Received From Country and Mode Of
        // receipt
        bdmUtil.updateEvidenceAttributes(bdmRegistrationDtls.bdmReceivedFrom,
          bdmRegistrationDtls.countryCode,
          participantRegistrationWizardResult.registrationResult.concernRoleID);

      } else if (!isCountryRestricted && (bdmRegistrationDtls.bdmReceivedFrom
        .equalsIgnoreCase(BDMRECEIVEDFROM.CLIENT_REPORTED)
        || bdmRegistrationDtls.bdmReceivedFrom
          .equalsIgnoreCase(BDMRECEIVEDFROM.REPRESENTATIVE))) {
        // register the participant as a Person
        participantRegistrationWizardResult = setRegisterPersonForPDCDetails(
          bdmRegistrationDtls, stateID, actionID);
        // create FEC Case
        createFECCase(bdmRegistrationDtls.countryCode,
          participantRegistrationWizardResult.registrationResult.concernRoleID);

        // call SIN-SIR Check
        if (!StringUtil.isNullOrEmpty(sinNumber)) {
          bdmUtil.callSINSIR(sinNumber,
            participantRegistrationWizardResult.registrationResult.concernRoleID);
        }

        // Bug 103734, Bug 104207 - FIXED
        // modify evidence for receivedFrom, Received From Country and Mode Of
        // receipt
        bdmUtil.updateEvidenceAttributes(bdmRegistrationDtls.bdmReceivedFrom,
          CuramConst.gkEmpty,
          participantRegistrationWizardResult.registrationResult.concernRoleID);

      }
      // End: Task 83103

    } // Save if loop

    return participantRegistrationWizardResult;
  }

  /**
   *
   * create FEC Case for IO person/prospect person
   *
   * @param countryCode
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  private void createFECCase(final String countryCode,
    final long concernRoleID) throws AppException, InformationalException {

    final BDMMaintainForeignEngagementCase bdmMaintainForeignEngagementCase =
      BDMMaintainForeignEngagementCaseFactory.newInstance();

    final BDMFECaseDetails bdmfeCaseDetails = new BDMFECaseDetails();
    bdmfeCaseDetails.countryCode = countryCode;
    bdmfeCaseDetails.concernRoleID = concernRoleID;
    bdmMaintainForeignEngagementCase.createFEIntegratedCase(bdmfeCaseDetails);
  }

  @Override
  public WizardProperties getBDMIOAddProspectPersonWizard()
    throws AppException, InformationalException {

    final WizardProperties wizardProperties = new WizardProperties();

    wizardProperties.wizardMenu =
      BDMConstants.kBDMIOAddProspectPersonForPDCWizardProperties;
    return wizardProperties;
  }

}
