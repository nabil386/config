package curam.ca.gc.bdm.facade.address.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.entity.fact.BDMAddressFactory;
import curam.ca.gc.bdm.entity.struct.BDMAddressEvdOverLapStartAndEndDateDtls;
import curam.ca.gc.bdm.entity.struct.BDMOverLapAddressEvdKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceValidationResult;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceWizardDetails;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceWizardDetailsResult;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceWizardKey;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceWizardResult;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceWizardStp2DataDetailsResult;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceWizardStp3DataDetails;
import curam.ca.gc.bdm.facade.address.struct.BDMAddressEvidenceDtls;
import curam.ca.gc.bdm.facade.address.struct.InfoMessage;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOADDRESS;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.sl.address.fact.BDMPDCAddressFactory;
import curam.ca.gc.bdm.sl.address.intf.BDMPDCAddress;
import curam.ca.gc.bdm.sl.address.struct.BDMParticipantAddressDetails;
import curam.ca.gc.bdm.sl.bdmaddress.fact.BDMAddressSearchFactory;
import curam.ca.gc.bdm.sl.bdmaddress.intf.BDMAddressSearch;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressDetailsStruct;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchDetails;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.BDMWSAddressInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos.WsaddrValidationRequest;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.intf.BDMWSAddressInterfaceIntf;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRY;
import curam.codetable.IEGYESNO;
import curam.codetable.LOCALE;
import curam.core.facade.pdt.struct.InformationalMessage;
import curam.core.facade.pdt.struct.InformationalMessageList;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AddressFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.Address;
import curam.core.intf.AddressData;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.ElementDetails;
import curam.core.struct.OtherAddressData;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.internal.codetable.fact.CodeTableFactory;
import curam.util.internal.codetable.struct.CTItemKey;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.GeneralConstants;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ListIterator;
import static java.net.URLDecoder.decode;

/**
 * @author amod.gole
 *
 */
public class BDMAddressEvidenceWizard
  extends curam.ca.gc.bdm.facade.address.base.BDMAddressEvidenceWizard {

  BDMUtil bdmUtil = new BDMUtil();

  @Inject
  BDMEvidenceUtil bdmEvidenceUtil;

  public BDMAddressEvidenceWizard() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * @param wizardStateID
   */
  @Override
  public AddressEvidenceWizardResult getAddressEvidenceWizardDetails(
    final AddressEvidenceWizardKey wizardStateKey)
    throws AppException, InformationalException {

    final AddressEvidenceWizardResult addressEvidenceWizardResult =
      new AddressEvidenceWizardResult();
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    // If the wizard state is specified then read the record, otherwise
    // create a wizard record.
    if (wizardStateKey.wizardStateID != 0) {

      final AddressEvidenceWizardDetailsResult addressEvidenceWizardDetailsResult =
        (AddressEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardStateKey.wizardStateID);

      addressEvidenceWizardResult.dtls
        .assign(addressEvidenceWizardDetailsResult);

      // clear postal code and address list if we are not just searched
      if (addressEvidenceWizardDetailsResult.currentStepNum != 5) {
        addressEvidenceWizardResult.dtls.step2DataDtls.postalCode = "";
        addressEvidenceWizardResult.dtls.step2DataDtls.addressList.clear();
      }

      addressEvidenceWizardResult.wizardKey.wizardStateID =
        wizardStateKey.wizardStateID;
    } else {

      Date fromDate = Date.kZeroDate;
      Date toDate = Date.kZeroDate;

      if (wizardStateKey.evidenceID != 0) {

        final EvidenceControllerInterface evidenceControllerObj =
          (EvidenceControllerInterface) EvidenceControllerFactory
            .newInstance();
        final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
        eiEvidenceKey.evidenceID = wizardStateKey.evidenceID;
        eiEvidenceKey.evidenceType = PDCConst.PDCADDRESS;

        final EIEvidenceReadDtls evidenceReadDtls =
          evidenceControllerObj.readEvidence(eiEvidenceKey);
        final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
          (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;
        fromDate = Date.fromISO8601(
          dynamicEvidenceDataDetails.getAttribute("fromDate").getValue());
        final String toDateStr =
          dynamicEvidenceDataDetails.getAttribute("toDate").getValue();
        toDate = toDateStr.isEmpty() ? Date.kZeroDate : Date.fromISO8601(
          dynamicEvidenceDataDetails.getAttribute("toDate").getValue());

      }

      addressEvidenceWizardResult.wizardKey.wizardStateID =
        wizardPersistentState
          .create(new AddressEvidenceWizardDetailsResult());
      addressEvidenceWizardResult.dtls.step1DataDtls.receiveDate =
        Date.getCurrentDate();

      addressEvidenceWizardResult.dtls.step1DataDtls.fromDate =
        fromDate.isZero() ? Date.getCurrentDate() : fromDate;
      if (!toDate.isZero()) {
        addressEvidenceWizardResult.dtls.step1DataDtls.toDate = toDate;
      }
      addressEvidenceWizardResult.dtls.step2DataDtls.country = "CA";
      addressEvidenceWizardResult.dtls.step1DataDtls.editInd = "YN1";

    }

    // Task 60485: DEV: Implement- Informational message
    addressEvidenceWizardResult.dtls.participantRoleID =
      addressEvidenceWizardResult.dtls.caseParticipantRoleID;

    return addressEvidenceWizardResult;

  }

  @Override
  public void setAddressEvidenceWizardDetails(
    final WizardStateID wizardStateID, final ActionIDProperty actionID,
    final AddressEvidenceWizardDetails wizardDtls)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final InformationalMessageList informationalMessageList =
      new InformationalMessageList();

    final BDMParticipantAddressDetails details =
      new BDMParticipantAddressDetails();

    details.assign(wizardDtls.step1DataDtls);
    details.concernRoleID = wizardDtls.caseParticipantRoleID;
    details.typeCode = wizardDtls.step1DataDtls.addressType;
    details.startDate = wizardDtls.step1DataDtls.fromDate;
    details.endDate = wizardDtls.step1DataDtls.toDate;

    final BDMPDCAddress bdmpdcAddress = BDMPDCAddressFactory.newInstance();
    bdmpdcAddress.validateSourceFields(details);

    // validate overlap address
    validateAddressOverlap(details, wizardDtls);
    if (wizardDtls.step1DataDtls.isUseResAddAsMailing) {
      details.typeCode = CONCERNROLEADDRESSTYPE.MAILING;
      validateAddressOverlap(details, wizardDtls);
    }

    TransactionInfo.getInformationalManager().failOperation();
    TransactionInfo.getInformationalManager().failOperation();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    AddressEvidenceWizardDetailsResult addressEvidenceWizardDetailsResult =
      null;
    if (actionID.actionIDProperty.equals("SEARCH")) {

      addressEvidenceWizardDetailsResult =
        (AddressEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      // When we are doing search, we use a special number to indicate that we
      // want to retain the search criteria when coming back to step 2
      addressEvidenceWizardDetailsResult.currentStepNum = 5;

      // the search action is triggered in step 2, so preserve the step 2
      // info
      addressEvidenceWizardDetailsResult.step2DataDtls
        .assign(wizardDtls.step2DataDtls);

      final BDMAddressDetailsStruct addressDetailsStruct =
        new BDMAddressDetailsStruct();

      if (wizardDtls.step2DataDtls.postalCode.isEmpty()
        && wizardDtls.step2DataDtls.country.equals("CA")) {

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMEVIDENCE.ERR_POSTAL_CODE_MANDATORY), null,
          InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      } else if (wizardDtls.step2DataDtls.country.equals("CA")) {

        if (!bdmUtil.isValidPostalCode(wizardDtls.step2DataDtls.postalCode)) {

          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(BDMPERSON.ERR_ADDR_CA_INVALID_POSTAL), null,
            InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
            0);

          TransactionInfo.getInformationalManager().failOperation();
        } else {

          // Postal code present
          addressDetailsStruct.postCode = wizardDtls.step2DataDtls.postalCode;
          final BDMAddressSearch addressSearchObj =
            BDMAddressSearchFactory.newInstance();

          final BDMAddressSearchDetails bdmAddressSearchDetails =
            new BDMAddressSearchDetails();
          bdmAddressSearchDetails.addressData = getEmptyAddressData();
          bdmAddressSearchDetails.formattedAddressData =
            new LocalisableString(BDMBPOADDRESS.INF_NEW_ADDRESS_LABEL)
              .getMessage(TransactionInfo.getProgramLocale());
          addressEvidenceWizardDetailsResult.step2DataDtls.addressList
            .clear();
          addressEvidenceWizardDetailsResult.step2DataDtls.addressList
            .addAll(addressSearchObj
              .searchAddressData(addressDetailsStruct).detailsList);
          addressEvidenceWizardDetailsResult.step2DataDtls.addressList
            .add(bdmAddressSearchDetails);

          wizardPersistentState.modify(wizardStateID.wizardStateID,
            addressEvidenceWizardDetailsResult);
        }
      }
    } else if (actionID.actionIDProperty.equals("RESETPAGE")) {
      addressEvidenceWizardDetailsResult =
        (AddressEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardStateID.wizardStateID);
      addressEvidenceWizardDetailsResult.step2DataDtls.postalCode = "";
      addressEvidenceWizardDetailsResult.step2DataDtls.addressList.clear();
      wizardPersistentState.modify(wizardStateID.wizardStateID,
        addressEvidenceWizardDetailsResult);
    } else if (actionID.actionIDProperty.equals("SAVE")) {
      addressEvidenceWizardDetailsResult =
        (AddressEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardStateID.wizardStateID);
      processAddressEvdAndValidateAddress(addressEvidenceWizardDetailsResult);

    } else if (actionID.actionIDProperty.equals("NEXTPAGE")) {
      // Task 60485: DEV: Implement- Informational message
      nextPageOperation(addressEvidenceWizardDetailsResult, wizardStateID,
        wizardPersistentState, wizardDtls, informationalManager,
        informationalMessageList);

    } else if (actionID.actionIDProperty.equals("SAVENCLOSE")) {
      // Task 60485: DEV: Implement- Informational message
      saveAndCloseOperation(addressEvidenceWizardDetailsResult, wizardStateID,
        wizardPersistentState, wizardDtls, informationalManager,
        informationalMessageList);

    }
  }

  /**
   * for action id - NEXTPAGE
   * Task 60485: DEV: Implement- Informational message.
   *
   * @param addressEvidenceWizardDetailsResult
   * @param wizardStateID
   * @param wizardPersistentState
   * @param wizardDtls
   * @param informationalManager
   * @param informationalMessageList
   * @throws AppException
   * @throws InformationalException
   */
  private void nextPageOperation(
    AddressEvidenceWizardDetailsResult addressEvidenceWizardDetailsResult,
    final WizardStateID wizardStateID,
    final WizardPersistentState wizardPersistentState,
    final AddressEvidenceWizardDetails wizardDtls,
    final InformationalManager informationalManager,
    final InformationalMessageList informationalMessageList)
    throws AppException, InformationalException {

    addressEvidenceWizardDetailsResult =
      (AddressEvidenceWizardDetailsResult) wizardPersistentState
        .read(wizardStateID.wizardStateID);
    if (wizardDtls.currentStepNum == 1) {
      addressEvidenceWizardDetailsResult.currentStepNum =
        wizardDtls.currentStepNum;
      addressEvidenceWizardDetailsResult.assign(wizardDtls);
      if (!addressEvidenceWizardDetailsResult.step1DataDtls.toDate.isZero()
        && addressEvidenceWizardDetailsResult.step1DataDtls.fromDate
          .after(addressEvidenceWizardDetailsResult.step1DataDtls.toDate)) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMBPOADDRESS.TO_FROM_DATE_ERR), null,
          InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      }

      // End Date the address
      // if (wizardDtls.step1DataDtls.editInd.equals(IEGYESNO.NO)) {

      // START Bug 111293: Address evidence details are not saved after the
      // edit

      // Commented the following lines of code, as the business requirement
      // update in the BUG.

      // new BDMEvidenceUtil();
      // BDMEvidenceUtil.modifyDateEvidenceDates(wizardDtls.evidenceID,
      // wizardDtls.step1DataDtls.fromDate, wizardDtls.step1DataDtls.toDate,
      // CASEEVIDENCE.BDMADDRESS, EVIDENCECHANGEREASON.REPORTEDBYCLIENT);

      // }

      // START Bug 111293: Address evidence details are not saved after the edit
      if (wizardDtls.step1DataDtls.editInd.equals(IEGYESNO.NO)) {

        if (addressEvidenceWizardDetailsResult.evidenceID != 0) {

          final Long concernRoleID =
            addressEvidenceWizardDetailsResult.caseParticipantRoleID;
          final String typeCode =
            addressEvidenceWizardDetailsResult.step1DataDtls.addressType;
          final Boolean isPreferredAddress =
            addressEvidenceWizardDetailsResult.step1DataDtls.preferredInd;
          final String comments =
            addressEvidenceWizardDetailsResult.step1DataDtls.comments;
          final Date startDate =
            addressEvidenceWizardDetailsResult.step1DataDtls.fromDate;
          final Date endDate =
            addressEvidenceWizardDetailsResult.step1DataDtls.toDate;
          // BEGIN TASK-28473 Change Reason Field Is missing on Edit Evidence
          final String changeReason =
            addressEvidenceWizardDetailsResult.step1DataDtls.changeReason;

          final String bdmReceivedFrom =
            addressEvidenceWizardDetailsResult.step1DataDtls.bdmReceivedFrom;

          final String bdmReceivedFromCountry =
            addressEvidenceWizardDetailsResult.step1DataDtls.bdmReceivedFromCountry;

          final String bdmModeOfReceipt =
            addressEvidenceWizardDetailsResult.step1DataDtls.bdmModeOfReceipt;

          if (addressEvidenceWizardDetailsResult.evidenceID != 0) {

            bdmEvidenceUtil.modifyAddressEvidenceStep1(concernRoleID,
              startDate, typeCode, endDate, isPreferredAddress, comments,
              addressEvidenceWizardDetailsResult.evidenceID, changeReason,
              bdmReceivedFrom, bdmReceivedFromCountry, bdmModeOfReceipt);

          }

        }

      }

    } else if (wizardDtls.currentStepNum == 2) {

      // BEGIN TASK-55637 Postal Code Must be entered
      if (wizardDtls.step2DataDtls.postalCode.isEmpty()
        && wizardDtls.step2DataDtls.country.equals("CA")) {

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMBPOADDRESS.ERR_POSTAL_CODE_MUST_BE_ENTERED_CANADIANADDRESS),
          null, InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      } else if (wizardDtls.step2DataDtls.country.equals("CA")) {

        if (!bdmUtil.isValidPostalCode(wizardDtls.step2DataDtls.postalCode)) {

          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(BDMPERSON.ERR_ADDR_CA_INVALID_POSTAL), null,
            InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
            0);

          TransactionInfo.getInformationalManager().failOperation();
        }
        // END TASK- 55637
      }

      addressEvidenceWizardDetailsResult.currentStepNum =
        wizardDtls.currentStepNum;

      addressEvidenceWizardDetailsResult.step2DataDtls
        .assign(wizardDtls.step2DataDtls);

      if (wizardDtls.step2DataDtls.addressData.isEmpty()
        && addressEvidenceWizardDetailsResult.step2DataDtls.country
          .equals("CA")) {

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMEVIDENCE.ERR_ADDRESS_SELECT), null,
          InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      } else {

        if (!wizardDtls.step2DataDtls.addressData
          .equals(getEmptyAddressData())) {

          addressEvidenceWizardDetailsResult.step2DataDtls.addressSelectedInd =
            true;
        }

        final OtherAddressData otherAddressData = new OtherAddressData();
        otherAddressData.addressData = wizardDtls.step2DataDtls.addressData;

        final String streetNumber =
          this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.LINE1);

        final String streetName =
          this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.LINE2);

        final String apt =
          this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.APT);

        // Postal Code
        final String postalCode = this.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.POSTCODE);

        final String poBox = this.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.POBOXNO);

        // city
        final String city =
          this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.CITY);

        // province
        final String province = this.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.PROVINCE);

        wizardDtls.step3DataDtls.clientSelectedAddressLine1 = streetNumber;
        wizardDtls.step3DataDtls.clientSelectedAddressLine2 = streetName;
        wizardDtls.step3DataDtls.clientSelectedCity = city;
        wizardDtls.step3DataDtls.clientSelectedProvince = province;
        wizardDtls.step3DataDtls.clientSelectedPostalCode = postalCode;
        wizardDtls.step3DataDtls.clientSelectedPOBox = poBox;
        wizardDtls.step3DataDtls.clientSelectedUnitNumber = apt;

      }
      addressEvidenceWizardDetailsResult.step3DataDtls
        .assign(wizardDtls.step3DataDtls);

    } else if (wizardDtls.currentStepNum == 3) {

      final InformationalManager infoManager =
        TransactionInfo.getInformationalManager();

      addressEvidenceWizardDetailsResult =
        (AddressEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      // BEGIN TASK-26215 Validation message saying City,Town, or Village must
      // be entered, even though it has a value
      wizardDtls.step3DataDtls.clientSelectedAddressLine1 =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedAddressLine1);

      wizardDtls.step3DataDtls.clientSelectedAddressLine2 =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedAddressLine2);

      wizardDtls.step3DataDtls.clientSelectedCity =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedCity);

      wizardDtls.step3DataDtls.clientSelectedPOBox =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedPOBox);

      wizardDtls.step3DataDtls.clientSelectedZipCode =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedZipCode);

      wizardDtls.step3DataDtls.clientSelectedPostalCode =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedPostalCode);

      wizardDtls.step3DataDtls.clientSelectedRegion =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedRegion);

      wizardDtls.step3DataDtls.clientSelectedUnitNumber =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedUnitNumber);

      // END TASK -26215

      final BDMUtil bdmUtil = new BDMUtil();
      bdmUtil.validateAddress(wizardDtls.step3DataDtls,
        addressEvidenceWizardDetailsResult.step2DataDtls.country,
        addressEvidenceWizardDetailsResult.step1DataDtls.addressType,
        infoManager);
      infoManager.failOperation();

      addressEvidenceWizardDetailsResult.currentStepNum =
        wizardDtls.currentStepNum;

      addressEvidenceWizardDetailsResult.step3DataDtls
        .assign(wizardDtls.step3DataDtls);

      // START: Task 93506: DEV: Address Format Validations
      validateAddressFormatLength(
        addressEvidenceWizardDetailsResult.step2DataDtls,
        wizardDtls.step3DataDtls);
      // END: Task 93506: DEV: Address Format Validations

      // format address
      final AddressData addressDataObj = AddressDataFactory.newInstance();
      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
      addressFieldDetails.countryCode =
        addressEvidenceWizardDetailsResult.step2DataDtls.country;

      addressFieldDetails.addressLine1 =
        wizardDtls.step3DataDtls.clientSelectedAddressLine1;
      addressFieldDetails.addressLine2 =
        wizardDtls.step3DataDtls.clientSelectedAddressLine2;
      addressFieldDetails.city = wizardDtls.step3DataDtls.clientSelectedCity;
      addressFieldDetails.province =
        wizardDtls.step3DataDtls.clientSelectedProvince;
      addressFieldDetails.postalCode =
        wizardDtls.step3DataDtls.clientSelectedPostalCode;
      addressFieldDetails.zipCode =
        wizardDtls.step3DataDtls.clientSelectedZipCode;
      // BEGIN TASK- 26215
      addressFieldDetails.addressLine3 =
        wizardDtls.step3DataDtls.clientSelectedPOBox;
      addressFieldDetails.suiteNum =
        wizardDtls.step3DataDtls.clientSelectedUnitNumber;
      // END TASK-26215 Below Mapping is incorrect
      addressFieldDetails.addressLine4 =
        wizardDtls.step3DataDtls.clientSelectedUnitNumber;
      final OtherAddressData otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);
      final Address addressObj = AddressFactory.newInstance();
      final OtherAddressData formattedAddressData =
        addressObj.getShortFormat(otherAddressData);
      addressEvidenceWizardDetailsResult.step4DataDtls.formattedAddressData =
        formattedAddressData.addressData;

      addressEvidenceWizardDetailsResult.isValidAddress =
        addressFieldDetails.countryCode.equals("CA")
          ? isValidAddress(addressFieldDetails) : true;

      if (addressEvidenceWizardDetailsResult.isValidAddress) {
        processAddressEvdAndValidateAddress(
          addressEvidenceWizardDetailsResult);
      }

    }

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {
      final InformationalMessage informationalMessage =
        new InformationalMessage();
      // Add Warning Message to informational Message
      informationalMessage.message = warnings[i];
      informationalMessageList.dtls.addRef(informationalMessage);
      addressEvidenceWizardDetailsResult.dtls2 = informationalMessageList;

    }
    wizardPersistentState.modify(wizardStateID.wizardStateID,
      addressEvidenceWizardDetailsResult);

  }

  /**
   * for action id - SAVEANDCLOSE
   * Task 60485: DEV: Implement- Informational message.
   *
   * @param addressEvidenceWizardDetailsResult
   * @param wizardStateID
   * @param wizardPersistentState
   * @param wizardDtls
   * @param informationalManager
   * @param informationalMessageList
   * @throws AppException
   * @throws InformationalException
   */
  private void saveAndCloseOperation(
    AddressEvidenceWizardDetailsResult addressEvidenceWizardDetailsResult,
    final WizardStateID wizardStateID,
    final WizardPersistentState wizardPersistentState,
    final AddressEvidenceWizardDetails wizardDtls,
    final InformationalManager informationalManager,
    final InformationalMessageList informationalMessageList)
    throws AppException, InformationalException {

    addressEvidenceWizardDetailsResult =
      (AddressEvidenceWizardDetailsResult) wizardPersistentState
        .read(wizardStateID.wizardStateID);
    if (wizardDtls.currentStepNum == 1) {
      addressEvidenceWizardDetailsResult.currentStepNum =
        wizardDtls.currentStepNum;
      addressEvidenceWizardDetailsResult.assign(wizardDtls);

      // End Date the address
      if (wizardDtls.step1DataDtls.editInd.equals(IEGYESNO.YES)
        || wizardDtls.step1DataDtls.editInd.equals(IEGYESNO.NO)) {

        // START Bug 111293: Address evidence details are not saved after the
        // edit

        // Commented the following lines of code, as the business requirement
        // update in the BUG.

        // new BDMEvidenceUtil();
        // BDMEvidenceUtil.modifyDateEvidenceDates(wizardDtls.evidenceID,
        // wizardDtls.step1DataDtls.fromDate, wizardDtls.step1DataDtls.toDate,
        // CASEEVIDENCE.BDMADDRESS, EVIDENCECHANGEREASON.REPORTEDBYCLIENT);

        // START Bug 111293: Address evidence details are not saved after the
        // edit

        if (addressEvidenceWizardDetailsResult.evidenceID != 0) {

          final Long concernRoleID =
            addressEvidenceWizardDetailsResult.caseParticipantRoleID;
          final String typeCode =
            addressEvidenceWizardDetailsResult.step1DataDtls.addressType;
          final Boolean isPreferredAddress =
            addressEvidenceWizardDetailsResult.step1DataDtls.preferredInd;
          final String comments =
            addressEvidenceWizardDetailsResult.step1DataDtls.comments;
          final Date startDate =
            addressEvidenceWizardDetailsResult.step1DataDtls.fromDate;
          final Date endDate =
            addressEvidenceWizardDetailsResult.step1DataDtls.toDate;
          // BEGIN TASK-28473 Change Reason Field Is missing on Edit Evidence
          final String changeReason =
            addressEvidenceWizardDetailsResult.step1DataDtls.changeReason;

          final String bdmReceivedFrom =
            addressEvidenceWizardDetailsResult.step1DataDtls.bdmReceivedFrom;

          final String bdmReceivedFromCountry =
            addressEvidenceWizardDetailsResult.step1DataDtls.bdmReceivedFromCountry;

          final String bdmModeOfReceipt =
            addressEvidenceWizardDetailsResult.step1DataDtls.bdmModeOfReceipt;

          if (addressEvidenceWizardDetailsResult.evidenceID != 0) {

            bdmEvidenceUtil.modifyAddressEvidenceStep1(concernRoleID,
              startDate, typeCode, endDate, isPreferredAddress, comments,
              addressEvidenceWizardDetailsResult.evidenceID, changeReason,
              bdmReceivedFrom, bdmReceivedFromCountry, bdmModeOfReceipt);

          }

        }

      }

    } else if (wizardDtls.currentStepNum == 2) {

      // BEGIN TASK-55637 Postal Code Must be entered
      if (wizardDtls.step2DataDtls.postalCode.isEmpty()
        && wizardDtls.step2DataDtls.country.equals("CA")) {

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMBPOADDRESS.ERR_POSTAL_CODE_MUST_BE_ENTERED_CANADIANADDRESS),
          null, InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      } else if (wizardDtls.step2DataDtls.country.equals("CA")) {

        if (!bdmUtil.isValidPostalCode(wizardDtls.step2DataDtls.postalCode)) {

          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(BDMPERSON.ERR_ADDR_CA_INVALID_POSTAL), null,
            InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
            0);

          TransactionInfo.getInformationalManager().failOperation();
        }
        // END TASK- 55637
      }

      addressEvidenceWizardDetailsResult.currentStepNum =
        wizardDtls.currentStepNum;

      addressEvidenceWizardDetailsResult.step2DataDtls
        .assign(wizardDtls.step2DataDtls);

      if (wizardDtls.step2DataDtls.addressData.isEmpty()
        && addressEvidenceWizardDetailsResult.step2DataDtls.country
          .equals("CA")) {

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMEVIDENCE.ERR_ADDRESS_SELECT), null,
          InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      } else {

        if (!wizardDtls.step2DataDtls.addressData
          .equals(getEmptyAddressData())) {

          addressEvidenceWizardDetailsResult.step2DataDtls.addressSelectedInd =
            true;
        }

        final OtherAddressData otherAddressData = new OtherAddressData();
        otherAddressData.addressData = wizardDtls.step2DataDtls.addressData;

        final String streetNumber =
          this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.LINE1);

        final String streetName =
          this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.LINE2);

        final String apt =
          this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.APT);

        // Postal Code
        final String postalCode = this.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.POSTCODE);

        final String poBox = this.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.POBOXNO);

        // city
        final String city =
          this.getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.CITY);

        // province
        final String province = this.getAddressDetails(otherAddressData,
          ADDRESSELEMENTTYPE.PROVINCE);

        wizardDtls.step3DataDtls.clientSelectedAddressLine1 = streetNumber;
        wizardDtls.step3DataDtls.clientSelectedAddressLine2 = streetName;
        wizardDtls.step3DataDtls.clientSelectedCity = city;
        wizardDtls.step3DataDtls.clientSelectedProvince = province;
        wizardDtls.step3DataDtls.clientSelectedPostalCode = postalCode;
        wizardDtls.step3DataDtls.clientSelectedPOBox = poBox;
        wizardDtls.step3DataDtls.clientSelectedUnitNumber = apt;

      }
      addressEvidenceWizardDetailsResult.step3DataDtls
        .assign(wizardDtls.step3DataDtls);

    } else if (wizardDtls.currentStepNum == 3) {

      final InformationalManager infoManager =
        TransactionInfo.getInformationalManager();

      addressEvidenceWizardDetailsResult =
        (AddressEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      // BEGIN TASK-26215 Validation message saying City,Town, or Village must
      // be entered, even though it has a value
      wizardDtls.step3DataDtls.clientSelectedAddressLine1 =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedAddressLine1);

      wizardDtls.step3DataDtls.clientSelectedAddressLine2 =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedAddressLine2);

      wizardDtls.step3DataDtls.clientSelectedCity =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedCity);

      wizardDtls.step3DataDtls.clientSelectedPOBox =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedPOBox);

      wizardDtls.step3DataDtls.clientSelectedZipCode =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedZipCode);

      wizardDtls.step3DataDtls.clientSelectedPostalCode =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedPostalCode);

      wizardDtls.step3DataDtls.clientSelectedRegion =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedRegion);

      wizardDtls.step3DataDtls.clientSelectedUnitNumber =
        trimAddressField(wizardDtls.step3DataDtls.clientSelectedUnitNumber);

      // END TASK -26215

      final BDMUtil bdmUtil = new BDMUtil();
      bdmUtil.validateAddress(wizardDtls.step3DataDtls,
        addressEvidenceWizardDetailsResult.step2DataDtls.country,
        addressEvidenceWizardDetailsResult.step1DataDtls.addressType,
        infoManager);
      infoManager.failOperation();

      addressEvidenceWizardDetailsResult.currentStepNum =
        wizardDtls.currentStepNum;

      addressEvidenceWizardDetailsResult.step3DataDtls
        .assign(wizardDtls.step3DataDtls);

      // format address
      final AddressData addressDataObj = AddressDataFactory.newInstance();
      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
      addressFieldDetails.countryCode =
        addressEvidenceWizardDetailsResult.step2DataDtls.country;

      addressFieldDetails.addressLine1 =
        wizardDtls.step3DataDtls.clientSelectedAddressLine1;
      addressFieldDetails.addressLine2 =
        wizardDtls.step3DataDtls.clientSelectedAddressLine2;
      addressFieldDetails.city = wizardDtls.step3DataDtls.clientSelectedCity;
      addressFieldDetails.province =
        wizardDtls.step3DataDtls.clientSelectedProvince;
      addressFieldDetails.postalCode =
        wizardDtls.step3DataDtls.clientSelectedPostalCode;
      addressFieldDetails.zipCode =
        wizardDtls.step3DataDtls.clientSelectedZipCode;
      // BEGIN TASK- 26215
      addressFieldDetails.addressLine3 =
        wizardDtls.step3DataDtls.clientSelectedPOBox;
      addressFieldDetails.suiteNum =
        wizardDtls.step3DataDtls.clientSelectedUnitNumber;
      // END TASK-26215 Below Mapping is incorrect
      addressFieldDetails.addressLine4 =
        wizardDtls.step3DataDtls.clientSelectedUnitNumber;
      final OtherAddressData otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);
      final Address addressObj = AddressFactory.newInstance();
      final OtherAddressData formattedAddressData =
        addressObj.getShortFormat(otherAddressData);
      addressEvidenceWizardDetailsResult.step4DataDtls.formattedAddressData =
        formattedAddressData.addressData;

      addressEvidenceWizardDetailsResult.isValidAddress =
        addressFieldDetails.countryCode.equals("CA")
          ? isValidAddress(addressFieldDetails) : true;

      if (addressEvidenceWizardDetailsResult.isValidAddress) {
        processAddressEvdAndValidateAddress(
          addressEvidenceWizardDetailsResult);
      }

    }

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {
      final InformationalMessage informationalMessage =
        new InformationalMessage();
      // Add Warning Message to informational Message
      informationalMessage.message = warnings[i];
      informationalMessageList.dtls.addRef(informationalMessage);
      addressEvidenceWizardDetailsResult.dtls2 = informationalMessageList;

    }
    wizardPersistentState.modify(wizardStateID.wizardStateID,
      addressEvidenceWizardDetailsResult);

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
   * util method to assign individual attributes to address data and
   * create/modify
   * evidence
   *
   * @param addressEvidenceWizardDetailsResult
   * @throws AppException
   * @throws InformationalException
   */
  private void processAddressEvdAndValidateAddress(
    final AddressEvidenceWizardDetailsResult addressEvidenceWizardDetailsResult)
    throws AppException, InformationalException {

    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();

    final Long concernRoleID =
      addressEvidenceWizardDetailsResult.caseParticipantRoleID;

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();

    // Address layout is INTL for all addresses
    addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.countryCode =
      addressEvidenceWizardDetailsResult.step2DataDtls.country;

    // assign common attribute values
    addressFieldDetails.suiteNum =
      addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedUnitNumber;
    addressFieldDetails.addressLine1 =
      addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedAddressLine1;
    addressFieldDetails.addressLine2 =
      addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedAddressLine2;
    addressFieldDetails.city =
      addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedCity;
    addressFieldDetails.addressLine3 =
      addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedPOBox;

    // assigning special attributes based on country
    if (addressEvidenceWizardDetailsResult.step2DataDtls.country
      .equals("CA")) {

      addressFieldDetails.province =
        addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedProvince;
      addressFieldDetails.postalCode =
        addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedPostalCode;

    } else if (addressEvidenceWizardDetailsResult.step2DataDtls.country
      .equals("US")) {

      addressFieldDetails.stateProvince =
        addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedState;
      addressFieldDetails.zipCode =
        addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedZipCode;

    } else {

      addressFieldDetails.stateProvince =
        addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedRegion;
      // BEGIN TASK-56098 State/Province/REgion Mapping for intl Address
      addressFieldDetails.addressLine4 =
        addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedRegion;
      // END TASK-56098
      addressFieldDetails.zipCode =
        addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedZipCode;

    }

    final String typeCode =
      addressEvidenceWizardDetailsResult.step1DataDtls.addressType;
    final Boolean isPreferredAddress =
      addressEvidenceWizardDetailsResult.step1DataDtls.preferredInd;
    final Boolean isUseResAddAsMailing =
      addressEvidenceWizardDetailsResult.step1DataDtls.isUseResAddAsMailing;
    final String comments =
      addressEvidenceWizardDetailsResult.step1DataDtls.comments;
    final Date startDate =
      addressEvidenceWizardDetailsResult.step1DataDtls.fromDate;
    final Date endDate =
      addressEvidenceWizardDetailsResult.step1DataDtls.toDate;
    // BEGIN TASK-28473 Change Reason Field Is missing on Edit Evidence
    final String changeReason =
      addressEvidenceWizardDetailsResult.step1DataDtls.changeReason;
    // END TASK-28473
    // call Util class to create address evidence
    final AddressData addressDataObj = AddressDataFactory.newInstance();

    final OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);

    final String bdmReceivedFrom =
      addressEvidenceWizardDetailsResult.step1DataDtls.bdmReceivedFrom;

    final String bdmReceivedFromCountry =
      addressEvidenceWizardDetailsResult.step1DataDtls.bdmReceivedFromCountry;

    final String bdmModeOfReceipt =
      addressEvidenceWizardDetailsResult.step1DataDtls.bdmModeOfReceipt;

    if (addressEvidenceWizardDetailsResult.evidenceID != 0) {
      // BEGIN TASK-28473 Change Reason Field Is missing on Edit Evidence
      bdmEvidenceUtil.modifyAddressEvidence(concernRoleID,
        otherAddressData.addressData, startDate, typeCode, endDate,
        isPreferredAddress, comments,
        addressEvidenceWizardDetailsResult.evidenceID, changeReason,
        bdmReceivedFrom, bdmReceivedFromCountry, bdmModeOfReceipt,
        addressEvidenceWizardDetailsResult.step2DataDtls.country);
      // END TASK-28473
    } else {
      // Start 103325 294-204 Add client home address and apply to mailing
      // address
      if (typeCode.equalsIgnoreCase(CONCERNROLEADDRESSTYPE.PRIVATE)) {
        bdmEvidenceUtil.endDateAnAddressAndCreateNewAddress(concernRoleID,
          otherAddressData.addressData, startDate, typeCode, endDate,
          isPreferredAddress, comments, isUseResAddAsMailing,
          addressEvidenceWizardDetailsResult.step2DataDtls.country);
      } else {
        bdmEvidenceUtil.createAddressEvidence(concernRoleID,
          otherAddressData.addressData, startDate, typeCode, endDate,
          isPreferredAddress, comments);
      }
      // END 103325 294-204 Add client home address and apply to mailing address
    }

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

  @Override
  public AddressEvidenceValidationResult
    isValidAddressEvidence(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final AddressEvidenceValidationResult addressEvidenceValidationResult =
      new AddressEvidenceValidationResult();
    boolean isValidAddress = true;
    try {
      if (wizardStateID.wizardStateID != 0) {

        final WizardPersistentState wizardPersistentState =
          new WizardPersistentState();
        AddressEvidenceWizardDetailsResult addressEvidenceWizardDetailsResult =
          null;
        addressEvidenceWizardDetailsResult =
          (AddressEvidenceWizardDetailsResult) wizardPersistentState
            .read(wizardStateID.wizardStateID);

        // run validation service only for canadian address
        if (addressEvidenceWizardDetailsResult.step2DataDtls.country
          .equals("CA")) {

          // If user enter wrong data after address selection

          if (!StringUtil.isNullOrEmpty(
            addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedPOBox)) {
            if (!StringUtil.isNullOrEmpty(
              addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedAddressLine1)
              || !StringUtil.isNullOrEmpty(
                addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedAddressLine2)) {

              /*
               * ValidationManagerFactory.getManager()
               * .addInfoMgrExceptionWithLookup(new AppException(
               * BDMRESTAPIERRORMESSAGE.
               * BDM_WSADDRESS_SEARCH_BY_POSTALCODE_REST_API_ERR_EXCEPTION_INVALID_ADDRESS
               * ),
               * null, InformationalType.kError,
               * curam.core.sl.infrastructure.impl.ValidationManagerConst.
               * kSetNine,
               * 0);
               */

              final AppException exceptionErr = new AppException(
                BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_SEARCH_BY_POSTALCODE_REST_API_ERR_EXCEPTION_INVALID_ADDRESS);
              exceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);

              if (Trace.atLeast(Trace.kTraceVerbose)) {
                // If tracing is enabled, exception shown on log
                Trace.kTopLevelLogger.debug(
                  "WEBSERVICE: Address Can not contain Both postal Code and Street Information");
                // Trace.kTopLevelLogger.debug(exceptionErr.getStackTrace());
              }
              throw exceptionErr;
              // TransactionInfo.getInformationalManager().failOperation();
            }
          }

          final String postalCode =
            addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedPostalCode;
          final String streetNumber =
            addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedAddressLine1;
          final String streetName =
            addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedAddressLine2;

          final String city =
            addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedCity;
          final String province =
            addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedProvince;

          if (Configuration.getBooleanProperty(
            EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT)) {

            final BDMWSAddressInterfaceIntf wsAddressImplObj =
              new BDMWSAddressInterfaceImpl();
            final WsaddrValidationRequest addressKey =
              new WsaddrValidationRequest();
            addressKey.setCanProvinceCode(province);
            addressKey.setNcAddressCityName(city);
            addressKey.setNcAddressPostalCode(postalCode);
            addressKey.setNcCountryCode("CAN");
            addressKey.setNcLanguageName(getLanguage());
            addressKey.setNcAddressFullText(postalCode + CuramConst.gkSpace
              + streetNumber + CuramConst.gkSpace + streetName);
            isValidAddress = wsAddressImplObj.validateAddress(addressKey);

          } else {// if WS is disabled then

            isValidAddress = true;
          }

        } else {// if country is anything else return as valid address

          isValidAddress = true;
        }
      } else {// if wizardID is not present return valid Address as true

        isValidAddress = true;
      }
    } catch (final AppException e) {
      isValidAddress = false;
      // BEGIN TASK-23721 BUG 21593 -AgentPortal Warning Message to User when WS
      // is down
      TransactionInfo.getInformationalManager().addInformationalMsg(e,
        GeneralConstants.kEmpty, InformationalType.kWarning);
      // END TASK-23721
    } catch (final NullPointerException e) {
      isValidAddress = false;
    }
    addressEvidenceValidationResult.isValidAddress = isValidAddress;

    // BEGIN TASK-23721 BUG 21593 -AgentPortal Warning Message to User when WS
    // is down
    final InformationalMessageList informationalMessageList =
      new InformationalMessageList();
    final String[] warnings =
      TransactionInfo.getInformationalManager().obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {
      final InformationalMessage informationalMessage =
        new InformationalMessage();
      informationalMessage.message = warnings[i];
      informationalMessageList.dtls.addRef(informationalMessage);
    }
    addressEvidenceValidationResult.dtls = informationalMessageList;
    // END TASK -23721
    return addressEvidenceValidationResult;
  }

  /**
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getEmptyAddressData()
    throws AppException, InformationalException {

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final AddressData addressDataObj = AddressDataFactory.newInstance();

    addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.countryCode = COUNTRY.CA;

    addressFieldDetails.suiteNum = "";
    addressFieldDetails.addressLine1 = "";
    addressFieldDetails.addressLine2 = "";
    addressFieldDetails.province = "";
    addressFieldDetails.city = "";
    addressFieldDetails.postalCode = "";
    addressFieldDetails.countryCode = COUNTRY.CA;
    final OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);

    return otherAddressData.addressData;
  }

  @Override
  public BDMAddressEvidenceDtls
    getExistingAddressDtls(final BDMAddressEvidenceDtls key)
      throws AppException, InformationalException {

    final BDMAddressEvidenceDtls bdmAddressEvidenceDtls =
      new BDMAddressEvidenceDtls();

    String decodedAddressData;
    try {
      if (key.evidenceID != 0) {
        decodedAddressData =
          decode(bdmEvidenceUtil.getExistingAddress(key.evidenceID,
            bdmAddressEvidenceDtls), StandardCharsets.UTF_8.toString());
        bdmAddressEvidenceDtls.existingAddressDtls = decodedAddressData;
      }
    } catch (final UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return bdmAddressEvidenceDtls;
  }

  /**
   * @param wizardStateID
   * actionID
   * wizardDtls
   *
   * @since ADO-19904
   *
   * Main method to set Address Wizard Details in the Wizard State
   *
   */
  @Override
  public InformationalMessageList searchAddressWizardDetails(
    final WizardStateID wizardStateID, final ActionIDProperty actionID,
    final AddressEvidenceWizardDetails wizardDtls)
    throws AppException, InformationalException {

    // BEGIN TASK-23721 BUG 21593 -AgentPortal Warning Message to User when WS
    // is down
    final InformationalMessageList informationalMessageList =
      new InformationalMessageList();
    // END TASk-23721
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    AddressEvidenceWizardDetailsResult addressEvidenceWizardDetailsResult =
      null;
    if (actionID.actionIDProperty.equals("SEARCH")) {

      addressEvidenceWizardDetailsResult =
        (AddressEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      // When we are doing search, we use a special number to indicate that we
      // want to retain the search criteria when coming back to step 2
      addressEvidenceWizardDetailsResult.currentStepNum = 5;

      // the search action is triggered in step 2, so preserve the step 2
      // info
      addressEvidenceWizardDetailsResult.step2DataDtls
        .assign(wizardDtls.step2DataDtls);

      final BDMAddressDetailsStruct addressDetailsStruct =
        new BDMAddressDetailsStruct();

      if (wizardDtls.step2DataDtls.postalCode.isEmpty()
        && wizardDtls.step2DataDtls.country.equals("CA")) {

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMEVIDENCE.ERR_POSTAL_CODE_MANDATORY), null,
          InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      } else if (wizardDtls.step2DataDtls.country.equals("CA")) {

        if (!bdmUtil.isValidPostalCode(wizardDtls.step2DataDtls.postalCode)) {

          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(BDMPERSON.ERR_ADDR_CA_INVALID_POSTAL), null,
            InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
            0);

          TransactionInfo.getInformationalManager().failOperation();
        } else {

          wizardDtls.step2DataDtls.postalCode.replace("\\s", "");
          // Postal code present
          addressDetailsStruct.postCode = wizardDtls.step2DataDtls.postalCode;
          final BDMAddressSearch addressSearchObj =
            BDMAddressSearchFactory.newInstance();

          final BDMAddressSearchDetails bdmAddressSearchDetails =
            new BDMAddressSearchDetails();
          bdmAddressSearchDetails.addressData = getEmptyAddressData();
          bdmAddressSearchDetails.formattedAddressData =
            new LocalisableString(BDMBPOADDRESS.INF_NEW_ADDRESS_LABEL)
              .getMessage(TransactionInfo.getProgramLocale());
          addressEvidenceWizardDetailsResult.step2DataDtls.addressList
            .clear();

          try {

            addressEvidenceWizardDetailsResult.step2DataDtls.addressList
              .addAll(addressSearchObj
                .searchAddressData(addressDetailsStruct).detailsList);

          } catch (final Exception e) {

            final AppException appexception =
              new AppException(BDMPERSON.WAR_ADDRESS_LOOK_UP);

            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(appexception, "a",
                InformationalType.kWarning);
          }

          addressEvidenceWizardDetailsResult.step2DataDtls.addressList
            .add(bdmAddressSearchDetails);

          wizardPersistentState.modify(wizardStateID.wizardStateID,
            addressEvidenceWizardDetailsResult);

          // BEGIN TASK-23721 BUG 21593 -AgentPortal Warning Message to User
          // when WS is down
          final String[] warnings = TransactionInfo.getInformationalManager()
            .obtainInformationalAsString();

          for (int i = 0; i < warnings.length; i++) {
            final InformationalMessage informationalMessage =
              new InformationalMessage();
            // Add Warning Message to informational Message
            informationalMessage.message = warnings[i];
            informationalMessageList.dtls.addRef(informationalMessage);
          }
          // END TASK-23721

        }
        // BEGIN TASK-27355 If Address is empty throw Mandatory Error
      } else if (wizardDtls.step2DataDtls.country.equals("")) {
        // Add Warning Message to informational Message

        final InformationalManager informationalManager =
          TransactionInfo.getInformationalManager();
        final LocalisableString localString =
          new LocalisableString(BDMBPOADDRESS.ERR_COUNTRY_MUST_BE_ENTERED);

        informationalManager.addInformationalMsg(localString,
          CuramConst.gkEmpty, InformationalType.kError);

        final String[] warnings =
          informationalManager.obtainInformationalAsString();

        for (int i = 0; i < warnings.length; i++) {
          final InformationalMessage informationalMessage =
            new InformationalMessage();
          // Add Warning Message to informational Message
          informationalMessage.message = warnings[i];
          informationalMessageList.dtls.addRef(informationalMessage);
        }
      }
      // END TASK-27355
    } else if (actionID.actionIDProperty.equals("RESETPAGE")) {
      addressEvidenceWizardDetailsResult =
        (AddressEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardStateID.wizardStateID);
      addressEvidenceWizardDetailsResult.step2DataDtls.postalCode = "";
      addressEvidenceWizardDetailsResult.step2DataDtls.addressList.clear();
      wizardPersistentState.modify(wizardStateID.wizardStateID,
        addressEvidenceWizardDetailsResult);
    } else if (actionID.actionIDProperty.equals("SAVE")) {
      addressEvidenceWizardDetailsResult =
        (AddressEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardStateID.wizardStateID);
      processAddressEvdAndValidateAddress(addressEvidenceWizardDetailsResult);

    } else if (actionID.actionIDProperty.equals("NEXTPAGE")) {

      if (!wizardDtls.step2DataDtls.country.equals("")) {
        addressEvidenceWizardDetailsResult =
          (AddressEvidenceWizardDetailsResult) wizardPersistentState
            .read(wizardStateID.wizardStateID);

        if (wizardDtls.currentStepNum == 1) {
          // BEGIN TASK-55637 Postal Code Must be entered
          if (wizardDtls.step2DataDtls.postalCode.isEmpty()
            && wizardDtls.step2DataDtls.country.equals("CA")) {

            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(new AppException(
                BDMBPOADDRESS.ERR_POSTAL_CODE_MUST_BE_ENTERED_CANADIANADDRESS),
                null, InformationalType.kError,
                curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
                0);

            TransactionInfo.getInformationalManager().failOperation();
          } else if (wizardDtls.step2DataDtls.country.equals("CA")) {

            if (!bdmUtil
              .isValidPostalCode(wizardDtls.step2DataDtls.postalCode)) {

              ValidationManagerFactory.getManager()
                .addInfoMgrExceptionWithLookup(
                  new AppException(BDMPERSON.ERR_ADDR_CA_INVALID_POSTAL),
                  null, InformationalType.kError,
                  curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
                  0);

              TransactionInfo.getInformationalManager().failOperation();
            }
          }
          // END TASK- 55637

          addressEvidenceWizardDetailsResult.currentStepNum =
            wizardDtls.currentStepNum;

          addressEvidenceWizardDetailsResult.step2DataDtls
            .assign(wizardDtls.step2DataDtls);

          if (wizardDtls.step2DataDtls.addressData.isEmpty()
            && addressEvidenceWizardDetailsResult.step2DataDtls.country
              .equals("CA")) {

            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(
                new AppException(BDMEVIDENCE.ERR_ADDRESS_SELECT), null,
                InformationalType.kError,
                curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
                0);

            TransactionInfo.getInformationalManager().failOperation();

          } else {

            if (!wizardDtls.step2DataDtls.addressData
              .equals(getEmptyAddressData())) {

              if (addressEvidenceWizardDetailsResult.step2DataDtls.country
                .equals("US")) {
                addressEvidenceWizardDetailsResult.step2DataDtls.addressSelectedInd =
                  false;
                addressEvidenceWizardDetailsResult.step2DataDtls.isCAAddress =
                  false;
                addressEvidenceWizardDetailsResult.step2DataDtls.isIntlAddress =
                  false;
                addressEvidenceWizardDetailsResult.step2DataDtls.isUSAddress =
                  true;
              } else if (!addressEvidenceWizardDetailsResult.step2DataDtls.country
                .equals("US")
                && !addressEvidenceWizardDetailsResult.step2DataDtls.country
                  .equals("CA")) {
                addressEvidenceWizardDetailsResult.step2DataDtls.addressSelectedInd =
                  false;
                addressEvidenceWizardDetailsResult.step2DataDtls.isCAAddress =
                  false;
                addressEvidenceWizardDetailsResult.step2DataDtls.isIntlAddress =
                  true;
                addressEvidenceWizardDetailsResult.step2DataDtls.isUSAddress =
                  false;
              } else if (wizardDtls.step2DataDtls.addressData.equals("")
                && addressEvidenceWizardDetailsResult.step2DataDtls.country
                  .equals("CA")) {
                addressEvidenceWizardDetailsResult.step2DataDtls.addressSelectedInd =
                  false;
                addressEvidenceWizardDetailsResult.step2DataDtls.isCAAddress =
                  true;
                addressEvidenceWizardDetailsResult.step2DataDtls.isIntlAddress =
                  false;
                addressEvidenceWizardDetailsResult.step2DataDtls.isUSAddress =
                  false;
              } else {
                addressEvidenceWizardDetailsResult.step2DataDtls.addressSelectedInd =
                  true;
                addressEvidenceWizardDetailsResult.step2DataDtls.isCAAddress =
                  false;
                addressEvidenceWizardDetailsResult.step2DataDtls.isIntlAddress =
                  false;
                addressEvidenceWizardDetailsResult.step2DataDtls.isUSAddress =
                  false;
              }

            } // Add NEW CA
            else if (addressEvidenceWizardDetailsResult.step2DataDtls.country
              .equals("CA")) {
              addressEvidenceWizardDetailsResult.step2DataDtls.addressSelectedInd =
                false;
              addressEvidenceWizardDetailsResult.step2DataDtls.isCAAddress =
                true;
              addressEvidenceWizardDetailsResult.step2DataDtls.isIntlAddress =
                false;
              addressEvidenceWizardDetailsResult.step2DataDtls.isUSAddress =
                false;
            }

            final OtherAddressData otherAddressData = new OtherAddressData();
            otherAddressData.addressData =
              wizardDtls.step2DataDtls.addressData;

            final String streetNumber = this
              .getAddressDetails(otherAddressData, ADDRESSELEMENTTYPE.LINE1);

            final String streetName = this.getAddressDetails(otherAddressData,
              ADDRESSELEMENTTYPE.LINE2);

            final String apt = this.getAddressDetails(otherAddressData,
              ADDRESSELEMENTTYPE.APT);

            // Postal Code
            final String postalCode = this.getAddressDetails(otherAddressData,
              ADDRESSELEMENTTYPE.POSTCODE);

            final String poBox = this.getAddressDetails(otherAddressData,
              ADDRESSELEMENTTYPE.POBOXNO);

            // city
            final String city = this.getAddressDetails(otherAddressData,
              ADDRESSELEMENTTYPE.CITY);

            // province
            final String province = this.getAddressDetails(otherAddressData,
              ADDRESSELEMENTTYPE.PROVINCE);

            wizardDtls.step3DataDtls.clientSelectedAddressLine1 =
              streetNumber;
            wizardDtls.step3DataDtls.clientSelectedAddressLine2 = streetName;
            wizardDtls.step3DataDtls.clientSelectedCity = city;
            wizardDtls.step3DataDtls.clientSelectedProvince = province;
            wizardDtls.step3DataDtls.clientSelectedPostalCode = postalCode;
            wizardDtls.step3DataDtls.clientSelectedPOBox = poBox;
            wizardDtls.step3DataDtls.clientSelectedUnitNumber = apt;

          }
          addressEvidenceWizardDetailsResult.step3DataDtls
            .assign(wizardDtls.step3DataDtls);

        } else if (wizardDtls.currentStepNum == 2) {

          final InformationalManager infoManager =
            TransactionInfo.getInformationalManager();

          addressEvidenceWizardDetailsResult =
            (AddressEvidenceWizardDetailsResult) wizardPersistentState
              .read(wizardStateID.wizardStateID);
          // BEGIN TASK -25724 APT field is not populated Correctly
          wizardDtls.step3DataDtls.clientSelectedAddressLine1 =
            trimAddressField(
              wizardDtls.step3DataDtls.clientSelectedAddressLine1);

          wizardDtls.step3DataDtls.clientSelectedAddressLine2 =
            trimAddressField(
              wizardDtls.step3DataDtls.clientSelectedAddressLine2);

          wizardDtls.step3DataDtls.clientSelectedCity =
            trimAddressField(wizardDtls.step3DataDtls.clientSelectedCity);

          wizardDtls.step3DataDtls.clientSelectedPOBox =
            trimAddressField(wizardDtls.step3DataDtls.clientSelectedPOBox);

          wizardDtls.step3DataDtls.clientSelectedZipCode =
            trimAddressField(wizardDtls.step3DataDtls.clientSelectedZipCode);

          wizardDtls.step3DataDtls.clientSelectedPostalCode =
            trimAddressField(
              wizardDtls.step3DataDtls.clientSelectedPostalCode);

          wizardDtls.step3DataDtls.clientSelectedRegion =
            trimAddressField(wizardDtls.step3DataDtls.clientSelectedRegion);

          wizardDtls.step3DataDtls.clientSelectedUnitNumber =
            trimAddressField(
              wizardDtls.step3DataDtls.clientSelectedUnitNumber);

          // END TASK -25723

          // START: Task 93506: DEV: Address Format Validations
          validateAddressFormatLength(
            addressEvidenceWizardDetailsResult.step2DataDtls,
            wizardDtls.step3DataDtls);
          // END: Task 93506: DEV: Address Format Validations

          final BDMUtil bdmUtil = new BDMUtil();
          bdmUtil.validateAddress(wizardDtls.step3DataDtls,
            addressEvidenceWizardDetailsResult.step2DataDtls.country,
            addressEvidenceWizardDetailsResult.step1DataDtls.addressType,
            infoManager);
          infoManager.failOperation();

          addressEvidenceWizardDetailsResult.currentStepNum =
            wizardDtls.currentStepNum;

          addressEvidenceWizardDetailsResult.step3DataDtls
            .assign(wizardDtls.step3DataDtls);

          // format address
          final AddressData addressDataObj = AddressDataFactory.newInstance();
          final AddressFieldDetails addressFieldDetails =
            new AddressFieldDetails();
          addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
          addressFieldDetails.countryCode = wizardDtls.step2DataDtls.country;

          addressFieldDetails.addressLine1 =
            wizardDtls.step3DataDtls.clientSelectedAddressLine1;
          addressFieldDetails.addressLine2 =
            wizardDtls.step3DataDtls.clientSelectedAddressLine2;
          addressFieldDetails.city =
            wizardDtls.step3DataDtls.clientSelectedCity;
          // assigning special attributes based on country
          if (addressEvidenceWizardDetailsResult.step2DataDtls.country
            .equals("CA")) {

            addressFieldDetails.province =
              wizardDtls.step3DataDtls.clientSelectedProvince;
            addressFieldDetails.postalCode =
              addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedPostalCode;

          } else if (addressEvidenceWizardDetailsResult.step2DataDtls.country
            .equals("US")) {

            addressFieldDetails.stateProvince =
              wizardDtls.step3DataDtls.clientSelectedState;
            addressFieldDetails.zipCode =
              addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedZipCode;

          } else {

            addressFieldDetails.stateProvince =
              addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedRegion;
            addressFieldDetails.zipCode =
              addressEvidenceWizardDetailsResult.step3DataDtls.clientSelectedZipCode;

          }

          addressFieldDetails.addressLine3 =
            wizardDtls.step3DataDtls.clientSelectedPOBox;
          addressFieldDetails.suiteNum =
            wizardDtls.step3DataDtls.clientSelectedUnitNumber;
          final OtherAddressData otherAddressData =
            addressDataObj.parseFieldsToData(addressFieldDetails);
          final Address addressObj = AddressFactory.newInstance();
          final OtherAddressData formattedAddressData =
            addressObj.getShortFormat(otherAddressData);
          addressEvidenceWizardDetailsResult.step4DataDtls.formattedAddressData =
            formattedAddressData.addressData;

          addressEvidenceWizardDetailsResult.formattedAddressData =
            formattedAddressData.addressData;
          addressEvidenceWizardDetailsResult.addressData =
            otherAddressData.addressData;
          addressEvidenceWizardDetailsResult.countryCode =
            wizardDtls.step2DataDtls.country;

        }
        wizardPersistentState.modify(wizardStateID.wizardStateID,
          addressEvidenceWizardDetailsResult);
      } else if (wizardDtls.step2DataDtls.country.equals("")) {
        // Add Warning Message to informational Message

        final InformationalManager informationalManager =
          TransactionInfo.getInformationalManager();
        final LocalisableString localString =
          new LocalisableString(BDMBPOADDRESS.ERR_COUNTRY_MUST_BE_ENTERED);

        informationalManager.addInformationalMsg(localString,
          CuramConst.gkEmpty, InformationalType.kError);

        final String[] warnings =
          informationalManager.obtainInformationalAsString();

        for (int i = 0; i < warnings.length; i++) {
          final InformationalMessage informationalMessage =
            new InformationalMessage();
          // Add Warning Message to informational Message
          informationalMessage.message = warnings[i];
          informationalMessageList.dtls.addRef(informationalMessage);
        }
        TransactionInfo.getInformationalManager().failOperation();
      }
    }
    // BEGIN TASK-23721 BUG 21593 -AgentPortal Warning Message to User when WS
    // is down
    // Change return type for Warning Message
    return informationalMessageList;
    // END TASK-23721
  }

  /**
   * BEGIN TASK -25724 APT field is not populated Correctly
   *
   * @param string data to be trimmed
   * @return trimmed data
   *
   * util method to fetch Address Details from the wizard or to create a wizard
   * State
   */
  public String trimAddressField(final String addDatafield) {

    if (addDatafield != null) {
      if (addDatafield.indexOf("\t") > 0) {
        return addDatafield.substring(0, addDatafield.indexOf("\t"));
      } else {
        return addDatafield;
      }
    }
    return "";
  }
  // END TASK 25724

  /**
   * @param wizardStateID
   *
   * util method to fetch Address Details from the wizard or to create a wizard
   * State
   */
  @Override
  public AddressEvidenceWizardResult
    getAddressSearchWizardDetails(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final AddressEvidenceWizardResult addressEvidenceWizardResult =
      new AddressEvidenceWizardResult();
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    // If the wizard state is specified then read the record, otherwise
    // create a wizard record.
    if (wizardStateID.wizardStateID != 0) {

      final AddressEvidenceWizardDetailsResult addressEvidenceWizardDetailsResult =
        (AddressEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      addressEvidenceWizardResult.dtls
        .assign(addressEvidenceWizardDetailsResult);

      // clear postal code and address list if we are not just searched
      if (addressEvidenceWizardDetailsResult.currentStepNum != 5) {
        addressEvidenceWizardResult.dtls.step2DataDtls.postalCode = "";
        addressEvidenceWizardResult.dtls.step2DataDtls.addressList.clear();
      }

      addressEvidenceWizardResult.wizardKey.wizardStateID =
        wizardStateID.wizardStateID;
    } else {
      addressEvidenceWizardResult.wizardKey.wizardStateID =
        wizardPersistentState
          .create(new AddressEvidenceWizardDetailsResult());
      addressEvidenceWizardResult.dtls.step1DataDtls.receiveDate =
        Date.getCurrentDate();
      addressEvidenceWizardResult.dtls.step1DataDtls.fromDate =
        Date.getCurrentDate();

    }
    return addressEvidenceWizardResult;

  }

  /**
   *
   * @param addressFieldDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Boolean
    isValidAddress(final AddressFieldDetails addressFieldDetails)
      throws AppException, InformationalException {

    boolean isValidAddress = true;

    try {

      final String postalCode = addressFieldDetails.postalCode;
      final String streetNumber = addressFieldDetails.addressLine1;
      final String streetName = addressFieldDetails.addressLine2;

      final String city = addressFieldDetails.city;
      final String province = addressFieldDetails.province;
      // if (addressFieldDetails.countryCode) {
      if (!StringUtil.isNullOrEmpty(addressFieldDetails.addressLine3)) {
        if (!StringUtil.isNullOrEmpty(addressFieldDetails.addressLine1)
          || !StringUtil.isNullOrEmpty(addressFieldDetails.addressLine2)) {

          final AppException exceptionErr = new AppException(
            BDMRESTAPIERRORMESSAGE.BDM_WSADDRESS_SEARCH_BY_POSTALCODE_REST_API_ERR_EXCEPTION_INVALID_ADDRESS);
          exceptionErr.arg(BDMConstants.kREST_API_WSADDRESS);

          if (Trace.atLeast(Trace.kTraceVerbose)) {
            // If tracing is enabled, exception shown on log
            Trace.kTopLevelLogger.debug(
              "WEBSERVICE: Address Can not contain Both postal Code and Street Information");
            // Trace.kTopLevelLogger.debug(exceptionErr.getStackTrace());
          }
          throw exceptionErr;
          // TransactionInfo.getInformationalManager().failOperation();
        }
      }

      if (Configuration.getBooleanProperty(
        EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT)) {

        final BDMWSAddressInterfaceIntf wsAddressImplObj =
          new BDMWSAddressInterfaceImpl();
        final WsaddrValidationRequest addressKey =
          new WsaddrValidationRequest();
        addressKey.setCanProvinceCode(province);
        addressKey.setNcAddressCityName(city);
        addressKey.setNcAddressPostalCode(postalCode);
        addressKey.setNcCountryCode("CAN");
        addressKey.setNcLanguageName(getLanguage());
        addressKey.setNcAddressFullText(postalCode + CuramConst.gkSpace
          + streetNumber + CuramConst.gkSpace + streetName);
        isValidAddress = wsAddressImplObj.validateAddress(addressKey);

      } else {// if WS is disabled then

        isValidAddress = true;
      }
    } catch (

    final AppException e) {
      isValidAddress = false;
      TransactionInfo.getInformationalManager().addInformationalMsg(e,
        GeneralConstants.kEmpty, InformationalType.kWarning);
    } catch (

    final NullPointerException e) {
      isValidAddress = false;
    }

    return isValidAddress;
  }

  /**
   * BEGIN TASK-23721 BUG 21593 -AgentPortal Warning Message to User when WS is
   * down
   * New Method to map Error message from the page back to informational page
   *
   * @param wizardStateID
   * @param messageText
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public AddressEvidenceWizardResult getAddressSearchWizardDetailsWithMessage(
    final WizardStateID wizardStateID, final InfoMessage messageText)
    throws AppException, InformationalException {

    // Call Existing method to get the details
    final AddressEvidenceWizardResult addressEvidenceWizardResult =
      getAddressSearchWizardDetails(wizardStateID);
    // Map Error Message
    if (messageText != null) {
      final InformationalMessage informationalMessage =
        new InformationalMessage();
      informationalMessage.message = messageText.message;
      final InformationalMessageList informationalMessageList =
        new InformationalMessageList();
      informationalMessageList.dtls.addRef(informationalMessage);

      addressEvidenceWizardResult.dtls1 = informationalMessageList;
    }
    return addressEvidenceWizardResult;
  }

  /**
   * BEGIN TASK-23721 BUG 21593 -AgentPortal Warning Message to User when WS is
   * down
   * New Method to map Error message from the page back to informational page
   *
   * @param wizardStateID
   * @param messageText
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public AddressEvidenceWizardResult
    getAddressEvidenceWizardDetailsWithMessage(
      final WizardStateID wizardStateID, final InfoMessage messageText)
      throws AppException, InformationalException {

    final AddressEvidenceWizardKey evidenceWizardKey =
      new AddressEvidenceWizardKey();
    evidenceWizardKey.wizardStateID = wizardStateID.wizardStateID;
    // Call Existing method to get the details
    final AddressEvidenceWizardResult addressEvidenceWizardResult =
      getAddressEvidenceWizardDetails(evidenceWizardKey);
    // Map Error Message
    if (messageText != null) {
      final InformationalMessage informationalMessage =
        new InformationalMessage();
      informationalMessage.message = messageText.message;
      final InformationalMessageList informationalMessageList =
        new InformationalMessageList();
      informationalMessageList.dtls.addRef(informationalMessage);

      addressEvidenceWizardResult.dtls1 = informationalMessageList;
    }

    if (!addressEvidenceWizardResult.dtls.step2DataDtls.addressSelectedInd) {
      // Task 63396 to fix bug#56315
      if ("CA"
        .equals(addressEvidenceWizardResult.dtls.step2DataDtls.country)) {
        addressEvidenceWizardResult.dtls.step2DataDtls.isCAAddress = true;
        addressEvidenceWizardResult.dtls.step2DataDtls.isUSAddress = false;
        addressEvidenceWizardResult.dtls.step2DataDtls.isIntlAddress = false;
      } else if ("US"
        .equals(addressEvidenceWizardResult.dtls.step2DataDtls.country)) {
        addressEvidenceWizardResult.dtls.step2DataDtls.isCAAddress = false;
        addressEvidenceWizardResult.dtls.step2DataDtls.isUSAddress = true;
        addressEvidenceWizardResult.dtls.step2DataDtls.isIntlAddress = false;
      } else {
        addressEvidenceWizardResult.dtls.step2DataDtls.isCAAddress = false;
        addressEvidenceWizardResult.dtls.step2DataDtls.isUSAddress = false;
        addressEvidenceWizardResult.dtls.step2DataDtls.isIntlAddress = true;
      }
    }
    return addressEvidenceWizardResult;
  }

  // START: Task 93506: DEV: Address Format Validations
  private void validateAddressFormatLength(
    final AddressEvidenceWizardStp2DataDetailsResult addressEvidenceWizardStp2DataDetailsResult,
    final AddressEvidenceWizardStp3DataDetails addressEvidenceWizardStp3DataDetails)
    throws AppException, InformationalException {

    final String apt =
      addressEvidenceWizardStp3DataDetails.clientSelectedUnitNumber.trim();
    final String streetNumber =
      addressEvidenceWizardStp3DataDetails.clientSelectedAddressLine1.trim();
    final String streetName =
      addressEvidenceWizardStp3DataDetails.clientSelectedAddressLine2.trim();

    final Boolean isCAAddress =
      addressEvidenceWizardStp2DataDetailsResult.country.equals(COUNTRY.CA);
    final Boolean isUSAddress =
      addressEvidenceWizardStp2DataDetailsResult.country.equals(COUNTRY.US);
    final Boolean isIntlAddress =
      !addressEvidenceWizardStp2DataDetailsResult.country.equals(COUNTRY.CA)
        && !addressEvidenceWizardStp2DataDetailsResult.country
          .equals(COUNTRY.US);

    final String unitStreetNumberAndName = streetNumber + CuramConst.gkSpace
      + streetName + CuramConst.gkSpace + apt;

    final Integer addresLineLimit = Integer
      .parseInt(Configuration.getProperty(EnvVars.BDM_ADDRESSLINE_LENGTH));

    // BEGIN BUG 95938 - Include US Address for validation
    if ((isCAAddress || isUSAddress
      || addressEvidenceWizardStp2DataDetailsResult.isUSAddress)
      && !(unitStreetNumberAndName.length() < addresLineLimit)) {
      // END BUG 95938 - Include US Address for validation

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMBPOADDRESS.ADDRESS_LINE_APT_STREET_NAME_NUMBER_MUST_BE_LESS_THAN_LIMIT),
        null, InformationalType.kError,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

      TransactionInfo.getInformationalManager().failOperation();

    } else if (isIntlAddress
      && !(unitStreetNumberAndName.length() < addresLineLimit)) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMBPOADDRESS.ADDRESS_LINE_INTL_STREET_NAME_NUMBER_MUST_BE_LESS_THAN_LIMIT),
        null, InformationalType.kError,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

      TransactionInfo.getInformationalManager().failOperation();
    }

    final String postBox =
      addressEvidenceWizardStp3DataDetails.clientSelectedPOBox.trim();

    if (!(postBox.length() < addresLineLimit)) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMBPOADDRESS.ADDRESS_LINE_POBOX_MUST_BE_LESS_THAN_LIMIT),
        null, InformationalType.kError,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

      TransactionInfo.getInformationalManager().failOperation();
    }

    final String city =
      addressEvidenceWizardStp3DataDetails.clientSelectedCity.trim();
    final String province =
      addressEvidenceWizardStp3DataDetails.clientSelectedProvince.trim();
    final String postalCode =
      addressEvidenceWizardStp3DataDetails.clientSelectedPostalCode.trim()
        .replaceAll(CuramConst.gkSpace, CuramConst.gkEmpty);

    final String cityProvinceAndPostal = city + CuramConst.gkSpace + province
      + CuramConst.gkSpace + CuramConst.gkSpace + postalCode;

    // START: Task 97311: BUG: Address Length validation fix
    final String usProvince =
      addressEvidenceWizardStp3DataDetails.clientSelectedState.trim();
    final String usZipCode =
      addressEvidenceWizardStp3DataDetails.clientSelectedZipCode.trim()
        .replaceAll(CuramConst.gkSpace, CuramConst.gkEmpty);

    final String intlProvince =
      addressEvidenceWizardStp3DataDetails.clientSelectedRegion.trim();
    final String intlZipCode =
      addressEvidenceWizardStp3DataDetails.clientSelectedZipCode.trim()
        .replaceAll(CuramConst.gkSpace, CuramConst.gkEmpty);

    final String usCityProvinceAndPostal = city + CuramConst.gkSpace
      + intlProvince + CuramConst.gkSpace + CuramConst.gkSpace + usZipCode;

    final String intlCityProvinceAndPostal = city + CuramConst.gkSpace
      + usProvince + CuramConst.gkSpace + CuramConst.gkSpace + intlZipCode;
    // END: Task 97311: BUG: Address Length validation fix

    if (isCAAddress && !(cityProvinceAndPostal.length() < addresLineLimit)) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMBPOADDRESS.ADDRESS_LINE_CITY_PROVINCE_POSTALCODE_MUST_BE_LESS_THAN_LIMIT),
        null, InformationalType.kError,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

      TransactionInfo.getInformationalManager().failOperation();

    } else if (isUSAddress
      // START: Task 97311: BUG: Address Length validation fix
      && !(usCityProvinceAndPostal.length() < addresLineLimit)) {
      // END: Task 97311: BUG: Address Length validation fix
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMBPOADDRESS.ADDRESS_LINE_US_CITY_PROVINCE_POSTALCODE_MUST_BE_LESS_THAN_LIMIT),
        null, InformationalType.kError,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

      TransactionInfo.getInformationalManager().failOperation();

    } else if (isIntlAddress
      // START: Task 97311: BUG: Address Length validation fix
      && !(intlCityProvinceAndPostal.length() < addresLineLimit)) {
      // END: Task 97311: BUG: Address Length validation fix
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMBPOADDRESS.ADDRESS_LINE_INTL_CITY_PROVINCE_POSTALCODE_MUST_BE_LESS_THAN_LIMIT),
        null, InformationalType.kError,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

      TransactionInfo.getInformationalManager().failOperation();
    }

  }
  // END: Task 93506: DEV: Address Format Validations

  // 103325 294-204 Add client home address and apply to mailing address
  // Validate address for the overlap.

  private void validateAddressOverlap(
    final BDMParticipantAddressDetails details,
    final AddressEvidenceWizardDetails wizardDtls)
    throws AppException, InformationalException {

    final long evidenceID = wizardDtls.evidenceID;

    final BDMOverLapAddressEvdKey key = new BDMOverLapAddressEvdKey();
    key.addressType = details.typeCode;
    key.participantID = details.concernRoleID;
    final ListIterator<BDMAddressEvdOverLapStartAndEndDateDtls> listItr =
      BDMAddressFactory.newInstance()
        .readOverLappingEvdForAddressByTypeAndParticipantID(key).dtls
          .listIterator();

    details.endDate = details.endDate.equals(Date.kZeroDate)
      ? Date.getDate("99991231") : details.endDate;
    while (listItr.hasNext()) {
      final BDMAddressEvdOverLapStartAndEndDateDtls dtls = listItr.next();
      if (evidenceID == dtls.evidenceID) {
        continue;
      }
      dtls.endDate = dtls.endDate.equals(Date.kZeroDate)
        ? Date.getDate("99991231") : dtls.endDate;
      if (dtls.startDate.after(details.startDate)
        && dtls.startDate.before(details.endDate)
        || dtls.endDate.after(details.startDate)
          && dtls.endDate.before(details.endDate)) {
        final CTItemKey ctItemKey = new CTItemKey();
        ctItemKey.code = details.typeCode;
        ctItemKey.locale = TransactionInfo.getProgramLocale();
        ctItemKey.tableName = "AddressType";

        final String addressType = CodeTableFactory.newInstance()
          .getOneItemForUserLocale(ctItemKey).description;

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMEVIDENCE.ERR_EVD_ADDRESS_OVERLAPPING_PERIOD)
            .arg(addressType),
          null, InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);
        break;
      }

    }

  }
}
