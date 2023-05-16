package curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMAddEFTDestinationDetails;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMBankAccountForSelection;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMBankAccountForSelectionList;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMPaymentDestinationIDVersionNo;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMReadEFTDestinationDetails;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMUpdateEFTDestinationDetails;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.codetable.impl.BDMBENEFITPROGRAMTYPEEntry;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentInstrumentDAFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMCountIssuedPaymentsForBankAccountKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentDestinationFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentDestination;
import curam.ca.gc.bdm.entity.financial.struct.BDMActiveDestinationByCaseParticipantKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMBankAccountForSelectionDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMBankAccountForSelectionDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMCaseParticipantsByConcernRoleDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMCaseParticipantsByConcernRoleDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMCaseParticipantsByConcernRoleKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMConcernRoleProgramTypeKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMConcernRoleRecordStatusKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMCurrentMailingAddressDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMCurrentMailingAddressDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMCurrentMailingAddressKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMDestinationByCaseParticipantDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMMailingAddressesKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMMatchingDeliveryMethodNomineeKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMMatchingDeliveryPatternKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMModifySyncIndicatorDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMNextActiveDestinationDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMNextActiveDestinationKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMNomineesForCaseParticipantKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationDtlsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMProductDeliveryPatternKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadByDestinationIDTypeKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchAllDestinationsKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchDestinationsPendingSyncKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchNomineesForSyncDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchNomineesForSyncDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchNomineesForSyncKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchObjectivesForSyncDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchObjectivesForSyncDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchObjectivesForSyncKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSetDestinationByLifeEventKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMPAYMENTDESTINATION;
import curam.ca.gc.bdm.sl.fact.MaintainFinancialComponentFactory;
import curam.ca.gc.bdm.sl.financial.intf.BDMFinancial;
import curam.ca.gc.bdm.sl.intf.MaintainFinancialComponent;
import curam.ca.gc.bdm.sl.maintaincasedeductions.impl.MaintainCaseDeductions;
import curam.ca.gc.bdm.sl.struct.BDMAdjustPaymentDestinationDatesDetails;
import curam.ca.gc.bdm.sl.struct.BDMFillInAddressDestinationsKey;
import curam.ca.gc.bdm.sl.struct.BDMRollupSuccessiveDestKey;
import curam.ca.gc.bdm.sl.struct.BDMSubmitApplicationAddDDLinkDetails;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.BANKACCOUNTTYPE;
import curam.codetable.CASENOMINEESTATUS;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.DESTINATIONTYPECODE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PMTRECONCILIATIONSTATUS;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.struct.SetDefaultNomineeKey;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.BankAccountFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleBankAccountFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.AddressData;
import curam.core.intf.BankAccount;
import curam.core.intf.ConcernRoleBankAccount;
import curam.core.sl.entity.fact.CaseNomineeDestinationFactory;
import curam.core.sl.entity.fact.CaseNomineeFactory;
import curam.core.sl.entity.fact.CaseNomineeObjectiveFactory;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.intf.CaseNominee;
import curam.core.sl.entity.intf.CaseNomineeDestination;
import curam.core.sl.entity.intf.CaseNomineeObjective;
import curam.core.sl.entity.struct.CaseNomineeCreationDetails;
import curam.core.sl.entity.struct.CaseNomineeDestinationDtls;
import curam.core.sl.entity.struct.CaseNomineeDestinationKey;
import curam.core.sl.entity.struct.CaseNomineeDtls;
import curam.core.sl.entity.struct.CaseNomineeKey;
import curam.core.sl.entity.struct.CaseNomineeKeyList;
import curam.core.sl.entity.struct.CaseNomineeObjectiveDateStatusKey;
import curam.core.sl.entity.struct.CaseNomineeObjectiveDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.entity.struct.DestinationTypeStatusCaseNomineeIDKey;
import curam.core.sl.fact.CaseStatusModeFactory;
import curam.core.sl.infrastructure.fact.GeneralUtilityFactory;
import curam.core.sl.infrastructure.intf.GeneralUtility;
import curam.core.sl.infrastructure.struct.CheckDatePeriodOverlapKey;
import curam.core.sl.infrastructure.struct.CheckDatePeriodOverlapResult;
import curam.core.sl.intf.CaseStatusMode;
import curam.core.sl.struct.AssignObjectiveAndDelPattKey;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.RecordCount;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.BankAccountDtls;
import curam.core.struct.BankAccountKey;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleBankAccountDtls;
import curam.core.struct.ConcernRoleBankAccountKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ElementDetails;
import curam.core.struct.FCCoverDate;
import curam.core.struct.GetLatestCoverPeriodToForCaseAndObjectiveKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.VersionNumberDetails;
import curam.piwrapper.impl.Address;
import curam.piwrapper.impl.AddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.CodeTableItemIdentifier;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Service layer class to handle payment destinations
 *
 */
public class BDMMaintainPaymentDestination implements
  curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf.BDMMaintainPaymentDestination {

  @Inject
  AddressDAO addressDAO;

  @Inject
  MaintainCaseDeductions maintainCaseDeductions;

  @Inject
  BDMFinancial bdmFinancialObj;

  public BDMMaintainPaymentDestination() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Lists the payment destination records for a participant
   */
  @Override
  public BDMSearchEFTDestinationDetailsList listEFTDestinations(
    final ConcernRoleKey key) throws AppException, InformationalException {

    final BDMSearchEFTDestinationKey searchEFTDestinationKey =
      new BDMSearchEFTDestinationKey();
    searchEFTDestinationKey.concernRoleID = key.concernRoleID;
    searchEFTDestinationKey.destinationType = DESTINATIONTYPECODE.BANKACCOUNT;
    searchEFTDestinationKey.recordStatusCode = RECORDSTATUS.NORMAL;
    final BDMSearchEFTDestinationDetailsList paymentDestinationList =
      BDMPaymentDestinationFactory.newInstance()
        .searchEFTPaymentDestination(searchEFTDestinationKey);
    return paymentDestinationList;
  }

  /**
   * Lists available bank accounts that can be linked to a benefit program
   */
  @Override
  public BDMBankAccountForSelectionList listBankAccountForSelection(
    final ConcernRoleKey key) throws AppException, InformationalException {

    // retrieve bank accounts
    final BDMConcernRoleRecordStatusKey bankAccountKey =
      new BDMConcernRoleRecordStatusKey();
    bankAccountKey.concernRoleID = key.concernRoleID;
    bankAccountKey.recordStatusCode = RECORDSTATUS.NORMAL;
    bankAccountKey.currentDate = Date.getCurrentDate();
    final BDMBankAccountForSelectionDetailsList bankAccountList =
      BDMPaymentDestinationFactory.newInstance()
        .searchBankAccountForSelection(bankAccountKey);

    final BDMBankAccountForSelectionList bankAccountDisplayList =
      new BDMBankAccountForSelectionList();

    final AddressData addressDataObj = AddressDataFactory.newInstance();

    // format the bank account details
    for (final BDMBankAccountForSelectionDetails bankAccountDetails : bankAccountList.dtls) {
      final String bankAccountDesc =
        formatBankAccountDisplay(addressDataObj, bankAccountDetails);

      final BDMBankAccountForSelection bankAccountForSelection =
        new BDMBankAccountForSelection();

      bankAccountForSelection.bankAccountDesc = bankAccountDesc;
      bankAccountForSelection.concernRoleBankAccountID =
        bankAccountDetails.concernRoleBankAccountID;
      bankAccountDisplayList.dtls.add(bankAccountForSelection);
    }

    return bankAccountDisplayList;

  }

  /**
   * Formats the bank account details into a displayable string
   *
   * @param addressDataObj
   * @param bankAccountDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String formatBankAccountDisplay(final AddressData addressDataObj,
    final BDMBankAccountForSelectionDetails bankAccountDetails)
    throws AppException, InformationalException {

    final StringBuffer bankAccountDesc = new StringBuffer();
    if (!StringUtil.isNullOrEmpty(bankAccountDetails.accountName)) {
      bankAccountDesc.append(bankAccountDetails.accountName);
    }
    if (!StringUtil.isNullOrEmpty(bankAccountDetails.accountTypeCode)) {
      if (bankAccountDesc.length() > 0) {
        bankAccountDesc.append(CuramConst.gkDash);
      }

      bankAccountDesc.append(CodeTable.getOneItem(BANKACCOUNTTYPE.TABLENAME,
        bankAccountDetails.accountTypeCode));
    }
    if (bankAccountDesc.length() > 0) {
      bankAccountDesc.append(CuramConst.gkComma + CuramConst.gkSpace);
    }
    bankAccountDesc.append(bankAccountDetails.branchName);

    final Address bankAddress =
      addressDAO.get(bankAccountDetails.branchAddressID);
    final OtherAddressData bankAddressData = new OtherAddressData();
    bankAddressData.addressData = bankAddress.getAddressData();

    // convert address data into a map
    final AddressMapList addressMapList =
      addressDataObj.parseDataToMap(bankAddressData);

    final AddressMap addressMap = new AddressMap();

    // fill out address details
    addressMap.name = ADDRESSELEMENTTYPE.LINE1;
    ElementDetails addressElem =
      addressDataObj.findElement(addressMapList, addressMap);

    if (addressElem.elementFound
      && !StringUtil.isNullOrEmpty(addressElem.elementValue)) {
      bankAccountDesc.append(CuramConst.gkComma);
      bankAccountDesc.append(CuramConst.gkSpace);
      bankAccountDesc.append(addressElem.elementValue);
    }

    addressMap.name = ADDRESSELEMENTTYPE.LINE2;
    addressElem = addressDataObj.findElement(addressMapList, addressMap);

    if (addressElem.elementFound
      && !StringUtil.isNullOrEmpty(addressElem.elementValue)) {
      bankAccountDesc.append(CuramConst.gkComma);
      bankAccountDesc.append(CuramConst.gkSpace);
      bankAccountDesc.append(addressElem.elementValue);
    }

    addressMap.name = ADDRESSELEMENTTYPE.CITY;
    addressElem = addressDataObj.findElement(addressMapList, addressMap);

    if (addressElem.elementFound
      && !StringUtil.isNullOrEmpty(addressElem.elementValue)) {
      bankAccountDesc.append(CuramConst.gkComma);
      bankAccountDesc.append(CuramConst.gkSpace);
      bankAccountDesc.append(addressElem.elementValue);
    }

    addressMap.name = ADDRESSELEMENTTYPE.PROVINCE;
    addressElem = addressDataObj.findElement(addressMapList, addressMap);

    if (addressElem.elementFound
      && !StringUtil.isNullOrEmpty(addressElem.elementValue)) {
      bankAccountDesc.append(CuramConst.gkComma);
      bankAccountDesc.append(CuramConst.gkSpace);
      bankAccountDesc.append(CodeTable.getOneItem(PROVINCETYPE.TABLENAME,
        addressElem.elementValue));
    }

    if (bankAccountDesc.length() > 0
      && !StringUtil.isNullOrEmpty(bankAccountDetails.accountNumber)) {
      bankAccountDesc.append(CuramConst.gkComma);
      bankAccountDesc.append(CuramConst.gkSpace);
      bankAccountDesc.append(bankAccountDetails.accountNumber);
    }
    return bankAccountDesc.toString();
  }

  /**
   * Add an EFT payment destination for a concern role and sync the destinations
   * to the case nominees if required
   */
  @Override
  public BDMPaymentDestinationKey
    addEFTDestination(final BDMAddEFTDestinationDetails details)
      throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // read bank account details
    final ConcernRoleBankAccountKey concernRoleBankAccountKey =
      new ConcernRoleBankAccountKey();
    concernRoleBankAccountKey.concernRoleBankAccountID =
      details.concernRoleBankAccountID;
    final ConcernRoleBankAccountDtls crBankAccountDtls =
      ConcernRoleBankAccountFactory.newInstance()
        .read(concernRoleBankAccountKey);
    final BankAccountKey bankAccountKey = new BankAccountKey();
    bankAccountKey.bankAccountID = crBankAccountDtls.bankAccountID;
    final BankAccountDtls bankAccountDtls =
      BankAccountFactory.newInstance().read(bankAccountKey);

    // check start date is after bank account start date
    if (bankAccountDtls.startDate.after(details.startDate)) {
      informationalManager.addInformationalMsg(
        new AppException(
          BDMPAYMENTDESTINATION.ERR_LINK_START_DATE_BEFORE_BANK_START_DATE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    // check end date is before bank account end date
    if (!bankAccountDtls.endDate.isZero() && (details.endDate.isZero()
      || details.endDate.after(bankAccountDtls.endDate))) {
      informationalManager.addInformationalMsg(
        new AppException(
          BDMPAYMENTDESTINATION.ERR_LINK_END_DATE_AFTER_BANK_END_DATE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    // check end date is after the start date
    if (!details.endDate.isZero()
      && details.endDate.before(details.startDate)) {
      informationalManager.addInformationalMsg(
        new AppException(
          BDMPAYMENTDESTINATION.ERR_END_DATE_BEFORE_START_DATE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    // check start date is not before current date
    if (details.startDate.before(Date.getCurrentDate())) {
      informationalManager.addInformationalMsg(
        new AppException(
          BDMPAYMENTDESTINATION.ERR_LINK_START_DATE_BEFORE_CURRENT_DATE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    // get current links

    final BDMSearchEFTDestinationKey searchEFTDestinationKey =
      new BDMSearchEFTDestinationKey();
    searchEFTDestinationKey.concernRoleID = details.concernRoleID;
    searchEFTDestinationKey.destinationType = DESTINATIONTYPECODE.BANKACCOUNT;
    searchEFTDestinationKey.recordStatusCode = RECORDSTATUS.NORMAL;
    BDMSearchEFTDestinationDetailsList paymentDestinationList =
      bdmPaymentDestinationObj
        .searchEFTPaymentDestination(searchEFTDestinationKey);

    final GeneralUtility generalUtilityObj =
      GeneralUtilityFactory.newInstance();
    final CheckDatePeriodOverlapKey checkDateOverlapKey =
      new CheckDatePeriodOverlapKey();
    // check to see if a link with the same program and bank account already
    // exists
    for (final BDMSearchEFTDestinationDetails paymentDestination : paymentDestinationList.dtls) {
      if (paymentDestination.destinationID == details.concernRoleBankAccountID) {
        if (paymentDestination.programType.equals(details.programType)
          || details.programType.equals(BDMConstants.kSelectAllCode)) {
          // check for overlap
          checkDateOverlapKey.startDateA = paymentDestination.startDate;
          checkDateOverlapKey.endDateA = paymentDestination.endDate;
          checkDateOverlapKey.startDateB = details.startDate;
          checkDateOverlapKey.endDateB = details.endDate;
          final CheckDatePeriodOverlapResult overlapResult =
            generalUtilityObj.checkDatePeriodOverlap(checkDateOverlapKey);

          if (overlapResult.datePeriodOverlapInd) {

            final AppException appException = new AppException(
              BDMPAYMENTDESTINATION.ERR_BANK_ACCOUNT_PROGRAM_TYPE_LINK_ALREADY_EXISTS);

            appException.arg(new CodeTableItemIdentifier(
              BDMBENEFITPROGRAMTYPE.TABLENAME, details.programType));

            informationalManager.addInformationalMsg(appException,
              CuramConst.gkEmpty,
              InformationalElement.InformationalType.kError);
          }
        }
      }
    }

    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

    // get all destination records
    final BDMSearchAllDestinationsKey bdmCRStatusKey =
      new BDMSearchAllDestinationsKey();
    bdmCRStatusKey.concernRoleID = details.concernRoleID;
    bdmCRStatusKey.recordStatusCode = RECORDSTATUS.NORMAL;
    BDMPaymentDestinationDtlsList allDestinationsList =
      bdmPaymentDestinationObj.searchAllDestinations(bdmCRStatusKey);

    // try to end date previous links
    for (final BDMPaymentDestinationDtls destination : allDestinationsList.dtls) {
      if (destination.programType.equals(details.programType)
        || details.programType.equals(BDMConstants.kSelectAllCode)) {

        if (destination.startDate.compareTo(details.startDate) <= 0
          && (destination.endDate.isZero()
            || destination.endDate.compareTo(details.startDate) >= 0)) {

          if (destination.endDate.isZero()) {
            destination.endDate = BDMConstants.kMaxDate;
          }
          if (details.endDate.isZero()) {
            details.endDate = BDMConstants.kMaxDate;
          }

          // if previous link's end date surpasses the new record's end date and
          // it is of type address, create a new record for it to fill in the
          // gap
          if (destination.endDate.after(details.endDate)
            && destination.destinationType
              .equals(DESTINATIONTYPECODE.ADDRESS)) {
            final BDMPaymentDestinationDtls paymentDestinationDtls =
              new BDMPaymentDestinationDtls();
            paymentDestinationDtls.assign(destination);
            paymentDestinationDtls.paymentDestinationID =
              UniqueID.nextUniqueID();
            paymentDestinationDtls.startDate = details.endDate.addDays(1);
            paymentDestinationDtls.endDate = destination.endDate;

            // let entity handle this
            paymentDestinationDtls.syncDestinationInd = false;

            insertRecord(paymentDestinationDtls);
          }

          final BDMAdjustPaymentDestinationDatesDetails adjustDetails =
            new BDMAdjustPaymentDestinationDatesDetails();

          // adjust the dates of previous links if they overlap
          adjustDetails.paymentDestinationID =
            destination.paymentDestinationID;
          adjustDetails.startDate = destination.startDate;
          adjustDetails.endDate = details.startDate.addDays(-1);
          adjustDestinationDates(adjustDetails);

          if (details.endDate.equals(BDMConstants.kMaxDate)) {
            details.endDate = Date.kZeroDate;
          }

        }
      }
    }

    // some records might have had their dates altered,
    // so reread
    allDestinationsList =
      bdmPaymentDestinationObj.searchAllDestinations(bdmCRStatusKey);

    // adjust address destinations that are after the input start date

    final BDMPaymentDestinationKey deleteKey = new BDMPaymentDestinationKey();

    for (final BDMPaymentDestinationDtls destination : allDestinationsList.dtls) {
      if (destination.programType.equals(details.programType)
        || details.programType.equals(BDMConstants.kSelectAllCode)) {

        // if it is cheque and it occurs after the new record, delete it
        if (destination.destinationType.equals(DESTINATIONTYPECODE.ADDRESS)
          && destination.startDate.after(details.startDate)
          && details.endDate.isZero()) {

          deleteKey.paymentDestinationID = destination.paymentDestinationID;
          deleteRecord(deleteKey);
        }
        // truncate any future address destinations that have overlap
        else if (destination.destinationType
          .equals(DESTINATIONTYPECODE.ADDRESS) && !details.endDate.isZero()
          && !destination.startDate.after(details.endDate)
          && (!destination.endDate.before(details.endDate)
            || destination.endDate.isZero())) {

          final BDMAdjustPaymentDestinationDatesDetails adjustDetails =
            new BDMAdjustPaymentDestinationDatesDetails();
          adjustDetails.endDate = destination.endDate;
          adjustDetails.startDate = details.endDate.addDays(1);
          adjustDetails.paymentDestinationID =
            destination.paymentDestinationID;

          adjustDestinationDates(adjustDetails);
        }
      }
    }

    paymentDestinationList = bdmPaymentDestinationObj
      .searchEFTPaymentDestination(searchEFTDestinationKey);

    // check for overlap with future links
    for (final BDMSearchEFTDestinationDetails paymentDestination : paymentDestinationList.dtls) {
      if (paymentDestination.programType.equals(details.programType)
        || details.programType.equals(BDMConstants.kSelectAllCode)) {

        // check for overlap
        checkDateOverlapKey.startDateA = paymentDestination.startDate;
        checkDateOverlapKey.endDateA = paymentDestination.endDate;
        checkDateOverlapKey.startDateB = details.startDate;
        checkDateOverlapKey.endDateB = details.endDate;
        final CheckDatePeriodOverlapResult overlapResult =
          generalUtilityObj.checkDatePeriodOverlap(checkDateOverlapKey);

        if (overlapResult.datePeriodOverlapInd) {

          final AppException appException = new AppException(
            BDMPAYMENTDESTINATION.ERR_NEW_LINK_OVERLAPS_EXISTING);

          appException.arg(paymentDestination.accountNumber);
          throw appException;
        }
      }
    }

    final BDMPaymentDestinationKey bdmPaymentDestinationKey =
      new BDMPaymentDestinationKey();
    if (details.programType.equals(BDMConstants.kSelectAllCode)) {
      final LinkedHashMap<String, String> programTypes =
        CodeTable.getAllEnabledItems(BDMBENEFITPROGRAMTYPE.TABLENAME,
          TransactionInfo.getProgramLocale());

      // insert entries for each program if it says select all
      for (final String programTypeCode : programTypes.keySet()) {
        final BDMPaymentDestinationDtls paymentDestinationDtls =
          new BDMPaymentDestinationDtls();
        paymentDestinationDtls.paymentDestinationID = UniqueID.nextUniqueID();
        paymentDestinationDtls.concernRoleID = details.concernRoleID;
        paymentDestinationDtls.destinationID =
          details.concernRoleBankAccountID;
        paymentDestinationDtls.destinationType =
          DESTINATIONTYPECODE.BANKACCOUNT;
        paymentDestinationDtls.startDate = details.startDate;
        paymentDestinationDtls.endDate = details.endDate;
        paymentDestinationDtls.programType = programTypeCode;
        paymentDestinationDtls.recordStatusCode = RECORDSTATUS.NORMAL;
        bdmPaymentDestinationObj.insert(paymentDestinationDtls);

        bdmPaymentDestinationKey.paymentDestinationID =
          paymentDestinationDtls.paymentDestinationID;
      }

    }

    else {
      final BDMPaymentDestinationDtls paymentDestinationDtls =
        new BDMPaymentDestinationDtls();
      paymentDestinationDtls.paymentDestinationID = UniqueID.nextUniqueID();
      paymentDestinationDtls.concernRoleID = details.concernRoleID;
      paymentDestinationDtls.destinationID = details.concernRoleBankAccountID;
      paymentDestinationDtls.destinationType =
        DESTINATIONTYPECODE.BANKACCOUNT;
      paymentDestinationDtls.startDate = details.startDate;
      paymentDestinationDtls.endDate = details.endDate;
      paymentDestinationDtls.programType = details.programType;
      paymentDestinationDtls.recordStatusCode = RECORDSTATUS.NORMAL;
      bdmPaymentDestinationObj.insert(paymentDestinationDtls);

      bdmPaymentDestinationKey.paymentDestinationID =
        paymentDestinationDtls.paymentDestinationID;

    }

    // roll up any address items as required
    final BDMRollupSuccessiveDestKey rollupKey =
      new BDMRollupSuccessiveDestKey();
    rollupKey.concernRoleID = details.concernRoleID;
    rollupKey.programType = details.programType;
    rollUpSuccessiveItems(rollupKey);

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = details.concernRoleID;
    syncPaymentDestinationByProgram(crKey,
      BDMBENEFITPROGRAMTYPEEntry.get(details.programType));

    return bdmPaymentDestinationKey;
  }

  /**
   * For a given payment destination, adjust the start date and end date
   *
   * @param paymentDestinationID
   * @param startDate
   * @param endDate
   */
  private void adjustDestinationDates(
    final BDMAdjustPaymentDestinationDatesDetails details)
    throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();
    final BDMPaymentDestinationKey key = new BDMPaymentDestinationKey();
    key.paymentDestinationID = details.paymentDestinationID;
    final BDMPaymentDestinationDtls dtls = bdmPaymentDestinationObj.read(key);

    // set end date to max value for convenience
    if (details.endDate.isZero()) {
      details.endDate = BDMConstants.kMaxDate;
    }

    if (!dtls.recordStatusCode.equals(RECORDSTATUS.NORMAL)) {
      return;
    }
    if (details.startDate.after(details.endDate)) {

      // if the start and end date pair is invalid and it is an address, just
      // delete it
      if (dtls.destinationType.equals(DESTINATIONTYPECODE.ADDRESS)) {
        deleteRecord(key);
        return;
      } else {
        // read bank account details
        final ConcernRoleBankAccountKey concernRoleBankAccountKey =
          new ConcernRoleBankAccountKey();
        concernRoleBankAccountKey.concernRoleBankAccountID =
          dtls.destinationID;
        final ConcernRoleBankAccountDtls crBankAccountDtls =
          ConcernRoleBankAccountFactory.newInstance()
            .read(concernRoleBankAccountKey);
        final BankAccountKey bankAccountKey = new BankAccountKey();
        bankAccountKey.bankAccountID = crBankAccountDtls.bankAccountID;
        final BankAccountDtls bankAccountDtls =
          BankAccountFactory.newInstance().read(bankAccountKey);

        final AppException appException = new AppException(
          BDMPAYMENTDESTINATION.ERR_ENDING_LINK_INVALID_END_DATE_WITH_ACCOUNT_NUM);
        appException.arg(bankAccountDtls.accountNumber);
        throw appException;
      }

    }
    // delete the record if the existing record's start date is after the new
    // adjusted end date
    if (dtls.startDate.after(details.endDate)) {
      dtls.recordStatusCode = RECORDSTATUS.CANCELLED;
    } else {
      dtls.startDate = details.startDate;
      dtls.endDate = details.endDate;
      if (dtls.endDate.equals(BDMConstants.kMaxDate)) {
        dtls.endDate = Date.kZeroDate;
      }
    }

    bdmPaymentDestinationObj.modify(key, dtls);

  }

  /**
   * Soft deletes a payment destination record
   *
   * @param paymentDestinationID
   */
  private void deleteRecord(final BDMPaymentDestinationKey key)
    throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    final BDMPaymentDestinationDtls dtls = bdmPaymentDestinationObj.read(key);

    dtls.recordStatusCode = RECORDSTATUS.CANCELLED;

    bdmPaymentDestinationObj.modify(key, dtls);

  }

  /**
   * For a given concern role, sync the payment destination with any existing
   * case nominees
   */
  @Override
  public void syncPaymentDestination(final ConcernRoleKey concernRoleKey)
    throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    final BDMSearchDestinationsPendingSyncKey syncKey =
      new BDMSearchDestinationsPendingSyncKey();
    syncKey.concernRoleID = concernRoleKey.concernRoleID;
    syncKey.recordStatusCode = RECORDSTATUS.NORMAL;
    syncKey.syncDestinationInd = true;
    // search for destinations that require syncing
    final BDMPaymentDestinationDtlsList destinationsPendingSyncList =
      bdmPaymentDestinationObj.searchDestinationPendingSync(syncKey);

    final Set<String> programList = new HashSet<>();
    final BDMPaymentDestinationKey destinationKey =
      new BDMPaymentDestinationKey();
    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = concernRoleKey.concernRoleID;

    for (final BDMPaymentDestinationDtls destination : destinationsPendingSyncList.dtls) {
      // if the destination in the future, we will not handle it now
      if (!destination.startDate.after(Date.getCurrentDate())) {

        // if the program hasn't been synced yet, we will sync the destination
        // for the program
        if (!programList.contains(destination.programType)) {
          programList.add(destination.programType);

          // sync by program, not by destination
          syncProgramPaymentDestination(crKey,
            BDMBENEFITPROGRAMTYPEEntry.get(destination.programType));

        }
        destination.syncDestinationInd = false;
        destinationKey.paymentDestinationID =
          destination.paymentDestinationID;
        bdmPaymentDestinationObj.modify(destinationKey, destination);

      }
    }

    final BDMModifySyncIndicatorDetails modifySyncDetails =
      new BDMModifySyncIndicatorDetails();
    final BDMNextActiveDestinationKey nextActiveDestKey =
      new BDMNextActiveDestinationKey();
    nextActiveDestKey.concernRoleID = concernRoleKey.concernRoleID;
    nextActiveDestKey.recordStatusCode = RECORDSTATUS.NORMAL;
    nextActiveDestKey.currentDate = Date.getCurrentDate();

    // for each program that has been synced, find the next destination that
    // requires syncing
    for (final String program : programList) {

      nextActiveDestKey.programType = program;

      try {
        // find the next upcoming active destination that needs to be updated
        final BDMNextActiveDestinationDetails nextActiveDestination =
          bdmPaymentDestinationObj
            .readNextActiveDestination(nextActiveDestKey);

        // set its sync indicator to true
        destinationKey.paymentDestinationID =
          nextActiveDestination.paymentDestinationID;

        modifySyncDetails.syncDestinationInd = true;
        modifySyncDetails.versionNo = nextActiveDestination.versionNo;

        bdmPaymentDestinationObj.modifySyncIndicator(destinationKey,
          modifySyncDetails);

      } catch (final RecordNotFoundException e) {
        // do nothing
      }

    }

  }

  /**
   * Sync the payment destination as needed for the given program type and the
   * given concern role
   *
   * @param crKey
   * @param programTypeEntry
   * @throws AppException
   * @throws InformationalException
   */
  private void syncProgramPaymentDestination(final ConcernRoleKey crKey,
    final BDMBENEFITPROGRAMTYPEEntry programTypeEntry)
    throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    // search for case participants by the given concern role that belong to
    // cases under the provided program type
    final BDMCaseParticipantsByConcernRoleKey searchCaseParticipantsKey =
      new BDMCaseParticipantsByConcernRoleKey();

    searchCaseParticipantsKey.concernRoleID = crKey.concernRoleID;
    searchCaseParticipantsKey.recordStatusCode = RECORDSTATUS.NORMAL;
    searchCaseParticipantsKey.caseStatusClosed = CASESTATUS.CLOSED;
    searchCaseParticipantsKey.typeCode = CASEPARTICIPANTROLETYPE.NOMINEE;
    searchCaseParticipantsKey.caseTypePDC = CASETYPECODE.PRODUCTDELIVERY;
    searchCaseParticipantsKey.programType = programTypeEntry.getCode();
    searchCaseParticipantsKey.currentDate = Date.getCurrentDate();

    final BDMCaseParticipantsByConcernRoleDetailsList caseParticipantsList =
      bdmPaymentDestinationObj
        .searchCaseParticipantsForSync(searchCaseParticipantsKey);

    // initiate objects for use later
    final BDMActiveDestinationByCaseParticipantKey activeDestinationByCaseParticipantKey =
      new BDMActiveDestinationByCaseParticipantKey();
    final CaseNomineeDestination caseNomineeDestinationObj =
      CaseNomineeDestinationFactory.newInstance();
    final CaseNominee caseNomineeObj = CaseNomineeFactory.newInstance();
    final MaintainFinancialComponent maintainFinancialComponentObj =
      MaintainFinancialComponentFactory.newInstance();
    final CaseNomineeObjective caseNomineeObjectiveObj =
      CaseNomineeObjectiveFactory.newInstance();
    final CaseStatusMode caseStatusModeObj =
      CaseStatusModeFactory.newInstance();

    boolean regenerateFinancialsInd = false;
    final SetDefaultNomineeKey defaultNomineeKey = new SetDefaultNomineeKey();

    // iterate through the case participants and determine which destination
    // is currently active for the participant
    for (final BDMCaseParticipantsByConcernRoleDetails caseParticipant : caseParticipantsList.dtls) {

      activeDestinationByCaseParticipantKey.caseParticipantRoleID =
        caseParticipant.caseParticipantRoleID;
      activeDestinationByCaseParticipantKey.recordStatusCode =
        RECORDSTATUS.NORMAL;
      activeDestinationByCaseParticipantKey.currentDate =
        Date.getCurrentDate();

      final Date destinationStartDate = Date.getCurrentDate();
      String destinationType = "";
      long destinationID = 0;
      Date destinationEndDate = Date.kZeroDate;
      try {
        // find the active destination
        final BDMDestinationByCaseParticipantDetails activeDestination =
          bdmPaymentDestinationObj.readActiveDestinationByCaseParticipant(
            activeDestinationByCaseParticipantKey);

        destinationType = activeDestination.destinationType;
        destinationID = activeDestination.destinationID;
        destinationEndDate = activeDestination.endDate;
      } catch (final RecordNotFoundException e) {
        // if no destination was found, we will set the destination to be
        // address
        destinationType = DESTINATIONTYPECODE.ADDRESS;

        final BDMCurrentMailingAddressKey mailingAddressKey =
          new BDMCurrentMailingAddressKey();
        mailingAddressKey.concernRoleID = crKey.concernRoleID;
        mailingAddressKey.statusCode = RECORDSTATUS.NORMAL;
        mailingAddressKey.typeCode = CONCERNROLEADDRESSTYPE.MAILING;
        mailingAddressKey.currentDate = Date.getCurrentDate();
        try {
          final BDMCurrentMailingAddressDetails mailingAddress =
            bdmPaymentDestinationObj
              .readCurrentMailingAddress(mailingAddressKey);

          destinationID = mailingAddress.concernRoleAddressID;
          destinationEndDate = mailingAddress.endDate;
        } catch (final RecordNotFoundException e2) {

          throw new AppException(
            BDMPAYMENTDESTINATION.ERR_NO_ACTIVE_DESTINATION_OR_ADDRESS);

        }

      }

      final BDMNomineesForCaseParticipantKey searchNomineesKey =
        new BDMNomineesForCaseParticipantKey();
      searchNomineesKey.caseParticipantRoleID =
        caseParticipant.caseParticipantRoleID;
      searchNomineesKey.recordStatusCode = RECORDSTATUS.NORMAL;
      searchNomineesKey.nomineeStatus = CASENOMINEESTATUS.OPERATIONAL;

      // search for nominees with the case participant ID
      final CaseNomineeKeyList nomineeList = bdmPaymentDestinationObj
        .searchNomineesByCaseParticipant(searchNomineesKey);

      final DestinationTypeStatusCaseNomineeIDKey readCaseNomineeDestKey =
        new DestinationTypeStatusCaseNomineeIDKey();

      for (final CaseNomineeKey nominee : nomineeList.dtls) {
        // try to find case nominee destination for nominee with the given
        // destination type
        readCaseNomineeDestKey.caseNomineeID = nominee.caseNomineeID;
        readCaseNomineeDestKey.destinationType = destinationType;
        readCaseNomineeDestKey.statusCode = RECORDSTATUS.NORMAL;

        final NotFoundIndicator nfIndicator = new NotFoundIndicator();
        CaseNomineeDestinationDtls caseNomineeDestinationDtls =
          caseNomineeDestinationObj.readByTypeStatusAndCaseNomineeID(
            nfIndicator, readCaseNomineeDestKey);

        if (destinationID != 0) {
          // add a case nominee destination for the nominee if one couldn't be
          // found
          if (nfIndicator.isNotFound()) {
            caseNomineeDestinationDtls = new CaseNomineeDestinationDtls();

            caseNomineeDestinationDtls.caseNomineeID = nominee.caseNomineeID;
            caseNomineeDestinationDtls.destinationID = destinationID;
            caseNomineeDestinationDtls.destinationType = destinationType;
            caseNomineeDestinationDtls.statusCode = RECORDSTATUS.NORMAL;

            caseNomineeDestinationObj.insert(caseNomineeDestinationDtls);

          } else {
            // otherwise, update the casenominee destination

            caseNomineeDestinationDtls.destinationID = destinationID;

            final CaseNomineeDestinationKey caseNomineeDestKey =
              new CaseNomineeDestinationKey();
            caseNomineeDestKey.caseNomineeDestinationID =
              caseNomineeDestinationDtls.caseNomineeDestinationID;

            caseNomineeDestinationObj.modify(caseNomineeDestKey,
              caseNomineeDestinationDtls);
          }
        } else {
          if (!nfIndicator.isNotFound()) {
            // if destinationID is blank, remove the destination so that payment
            // instruments won't be skipped and the wrong destination won't be
            // used
            caseNomineeDestinationDtls.statusCode = RECORDSTATUS.CANCELLED;
            final CaseNomineeDestinationKey caseNomineeDestKey =
              new CaseNomineeDestinationKey();
            caseNomineeDestKey.caseNomineeDestinationID =
              caseNomineeDestinationDtls.caseNomineeDestinationID;
            caseNomineeDestinationObj.modify(caseNomineeDestKey,
              caseNomineeDestinationDtls);
          }
        }
      }

      // Get all delivery patterns that are using old delivery method and create
      // delivery patterns with new method.

      final BDMSearchNomineesForSyncKey searchNomineePatternsKey =
        new BDMSearchNomineesForSyncKey();
      searchNomineePatternsKey.caseParticipantRoleID =
        caseParticipant.caseParticipantRoleID;
      searchNomineePatternsKey.recordStatusCode = RECORDSTATUS.NORMAL;
      searchNomineePatternsKey.nomineeStatus = CASENOMINEESTATUS.OPERATIONAL;
      searchNomineePatternsKey.fromDate = destinationStartDate;

      if (destinationType.equals(DESTINATIONTYPECODE.ADDRESS)) {
        searchNomineePatternsKey.methodOfDelivery = METHODOFDELIVERY.EFT;
      } else {
        searchNomineePatternsKey.methodOfDelivery = METHODOFDELIVERY.CHEQUE;
      }

      // find the old nominee delivery patterns
      final BDMSearchNomineesForSyncDetailsList nomineePatternList =
        bdmPaymentDestinationObj
          .searchNomineesPatternsForSync(searchNomineePatternsKey);

      final BDMMatchingDeliveryMethodNomineeKey matchingNomineeKey =
        new BDMMatchingDeliveryMethodNomineeKey();
      for (final BDMSearchNomineesForSyncDetails nominee : nomineePatternList.dtls) {

        // Check if there already exists nominee delivery pattern with the new
        // method of delivery that matches old delivery frequency.

        // For now not comparing the offsets.
        matchingNomineeKey.caseParticipantRoleID =
          caseParticipant.caseParticipantRoleID;
        matchingNomineeKey.recordStatusCode = RECORDSTATUS.NORMAL;
        matchingNomineeKey.nomineeStatus = CASENOMINEESTATUS.OPERATIONAL;
        matchingNomineeKey.fromDate = destinationStartDate;
        matchingNomineeKey.deliveryFrequency = nominee.deliveryFrequency;
        matchingNomineeKey.coverPattern = nominee.coverPattern;
        if (destinationType.equals(DESTINATIONTYPECODE.ADDRESS)) {
          matchingNomineeKey.methodOfDelivery = METHODOFDELIVERY.CHEQUE;
        } else {
          matchingNomineeKey.methodOfDelivery = METHODOFDELIVERY.EFT;
        }
        CaseNomineeKey matchingNominee = new CaseNomineeKey();

        try {
          // try to find a matching nominee with the new delivery pattern
          matchingNominee = bdmPaymentDestinationObj
            .readMatchingDeliveryMethodNominee(matchingNomineeKey);

        } catch (final RecordNotFoundException e) {
          // if it can't be found, try to find the delivery pattern
          final BDMMatchingDeliveryPatternKey matchingDeliveryPatternKey =
            new BDMMatchingDeliveryPatternKey();
          // create new delivery pattern
          matchingDeliveryPatternKey.caseID = caseParticipant.caseID;
          matchingDeliveryPatternKey.recordStatusCode = RECORDSTATUS.NORMAL;
          matchingDeliveryPatternKey.fromDate = destinationStartDate;
          matchingDeliveryPatternKey.deliveryFrequency =
            nominee.deliveryFrequency;
          matchingDeliveryPatternKey.coverPattern = nominee.coverPattern;

          if (destinationType.equals(DESTINATIONTYPECODE.ADDRESS)) {
            matchingDeliveryPatternKey.methodOfDelivery =
              METHODOFDELIVERY.CHEQUE;
          } else {
            matchingDeliveryPatternKey.methodOfDelivery =
              METHODOFDELIVERY.EFT;
          }

          try {
            // if a matching delivery pattern was found, we will create a case
            // nominee with the given delivery pattern
            final BDMProductDeliveryPatternKey matchingDeliveryPattern =
              bdmPaymentDestinationObj
                .readMatchingDeliveryPattern(matchingDeliveryPatternKey);

            final CaseNomineeKey caseNomineeKey = new CaseNomineeKey();
            caseNomineeKey.caseNomineeID = nominee.caseNomineeID;
            final CaseNomineeDtls existingCaseNomineeDetails =
              caseNomineeObj.read(caseNomineeKey);

            // create new case nominee
            final CaseNomineeDtls newCaseNomineeDetails =
              new CaseNomineeDtls();
            newCaseNomineeDetails.caseNomineeID = UniqueID.nextUniqueID();
            newCaseNomineeDetails.currencyType =
              existingCaseNomineeDetails.currencyType;
            newCaseNomineeDetails.caseParticipantRoleID =
              caseParticipant.caseParticipantRoleID;
            newCaseNomineeDetails.nomineeRelationship =
              existingCaseNomineeDetails.nomineeRelationship;

            final CaseNomineeCreationDetails caseNomineeCreationDetails =
              new CaseNomineeCreationDetails();
            caseNomineeCreationDetails.caseID = caseParticipant.caseID;
            caseNomineeCreationDetails.productDeliveryPatternID =
              matchingDeliveryPattern.productDeliveryPatternID;
            caseNomineeCreationDetails.fromDate = destinationStartDate;
            caseNomineeCreationDetails.concernRoleID = crKey.concernRoleID;

            if (destinationType.equals(DESTINATIONTYPECODE.ADDRESS)) {
              caseNomineeCreationDetails.participantRoleAddressID =
                destinationID;
            } else {
              caseNomineeCreationDetails.participantRoleBankAccountID =
                destinationID;
            }

            caseNomineeObj.insert(newCaseNomineeDetails,
              caseNomineeCreationDetails);

            matchingNominee.caseNomineeID =
              newCaseNomineeDetails.caseNomineeID;

          } catch (final RecordNotFoundException e2) {
            // do nothing if not found
          }

        }

        if (matchingNominee.caseNomineeID != 0) {
          // if the nominee (with the current delivery pattern) is the default,
          // set it to the matching nominee (it might have been created newly)
          if (nominee.defaultNomInd) {

            final CaseNomineeDtls caseNomineeDtls =
              caseNomineeObj.read(matchingNominee);

            defaultNomineeKey.key.caseID = caseParticipant.caseID;
            defaultNomineeKey.key.caseNomineeID =
              caseNomineeDtls.caseNomineeID;
            defaultNomineeKey.key.versionNo = caseNomineeDtls.versionNo;
          }

          // if the nominee has any objectives assigned, those will have to be
          // synced
          final BDMSearchObjectivesForSyncKey searchObjectivesKey =
            new BDMSearchObjectivesForSyncKey();
          searchObjectivesKey.caseNomineeID = nominee.caseNomineeID;
          searchObjectivesKey.recordStatusCode = RECORDSTATUS.NORMAL;
          final BDMSearchObjectivesForSyncDetailsList objectivesList =
            bdmPaymentDestinationObj
              .searchObjectivesForSync(searchObjectivesKey);

          for (final BDMSearchObjectivesForSyncDetails objective : objectivesList.dtls) {
            if (objective.toDate.isZero()) {
              objective.toDate = BDMConstants.kMaxDate;
            }
          }
          if (destinationEndDate.isZero()) {
            destinationEndDate = BDMConstants.kMaxDate;
          }

          for (final BDMSearchObjectivesForSyncDetails objective : objectivesList.dtls) {
            if (objective.toDate.before(destinationStartDate)
              || objective.fromDate.after(destinationEndDate)) {
              continue;
            }

            // find when the objective was last paid to
            final GetLatestCoverPeriodToForCaseAndObjectiveKey latestCoverPeriodKey =
              new GetLatestCoverPeriodToForCaseAndObjectiveKey();
            latestCoverPeriodKey.key.caseID = caseParticipant.caseID;
            latestCoverPeriodKey.key.rulesObjectiveID =
              objective.rulesObjectiveID;
            // check max paid date for objective
            final FCCoverDate fcCoverDate = maintainFinancialComponentObj
              .getLatestCoverPeriodToForCaseAndObjective(
                latestCoverPeriodKey);

            Date objectiveFromDate = destinationStartDate;
            // set the objective from date
            if (objective.fromDate.after(objectiveFromDate)) {
              objectiveFromDate = objective.fromDate;
            }
            if (fcCoverDate.coverDate.after(objectiveFromDate)) {
              objectiveFromDate = fcCoverDate.coverDate;
            }
            if (objectiveFromDate.after(objective.toDate)) {
              continue;
            }

            final CaseNomineeObjectiveDateStatusKey readCaseNomObjKey =
              new CaseNomineeObjectiveDateStatusKey();
            readCaseNomObjKey.caseID = caseParticipant.caseID;
            readCaseNomObjKey.effectiveDate = objectiveFromDate;
            readCaseNomObjKey.rulesObjectiveID = objective.rulesObjectiveID;
            readCaseNomObjKey.statusCode = RECORDSTATUS.NORMAL;

            // find the objective details
            final CaseNomineeObjectiveDtls caseNomineeObjectiveDtls =
              caseNomineeObjectiveObj.readByDateStatus(readCaseNomObjKey);

            // if the matching nominee (could be brand new) isn't assigned the
            // objectives, reassign them to the matching nominee
            if (caseNomineeObjectiveDtls.caseNomineeID != matchingNominee.caseNomineeID) {

              caseStatusModeObj.setDisableRegenFCsCaseStatusDPPCheck(true);

              final AssignObjectiveAndDelPattKey objectiveKey =
                new AssignObjectiveAndDelPattKey();
              objectiveKey.caseID = caseParticipant.caseID;
              objectiveKey.rulesObjectiveID = objective.rulesObjectiveID;
              objectiveKey.caseNomineeID = matchingNominee.caseNomineeID;
              objectiveKey.fromDate = objectiveFromDate;

              // assign objectives
              bdmFinancialObj.assignObjective1(objectiveKey);
              regenerateFinancialsInd = true;
            }
          }

        }
      }

      // if the default nominee needs to be set - set the default nominee
      if (defaultNomineeKey.key.caseNomineeID != 0) {
        caseStatusModeObj.setDisableRegenFCsCaseStatusDPPCheck(true);
        bdmFinancialObj.setDefaultNominee(defaultNomineeKey);
        regenerateFinancialsInd = true;
      }
      // regenerate financials if required
      if (regenerateFinancialsInd) {
        final CaseIDKey caseIDKey = new CaseIDKey();
        caseIDKey.caseID = caseParticipant.caseID;
        maintainCaseDeductions.regenerateCaseFinancials(caseIDKey);
      }

    }

  }

  /**
   * Looks at a particular concern role's list of payment destinations and rolls
   * up any destinations that have the exact same type and ID and are adjacent
   * on a timeline
   *
   * @param key
   * @throws AppException
   * @throws InformationalException
   */
  private void rollUpSuccessiveItems(final BDMRollupSuccessiveDestKey key)
    throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    final BDMConcernRoleProgramTypeKey searchKey =
      new BDMConcernRoleProgramTypeKey();
    searchKey.concernRoleID = key.concernRoleID;
    searchKey.recordStatusCode = RECORDSTATUS.NORMAL;
    searchKey.programType = key.programType;
    // search for all destinations (sorted by start date)
    final BDMPaymentDestinationDtlsList destinationList =
      bdmPaymentDestinationObj.searchAllProgramDestinations(searchKey);

    BDMPaymentDestinationDtls prev = null;
    final BDMPaymentDestinationKey prevKey = new BDMPaymentDestinationKey();

    if (destinationList.dtls.size() > 0) {
      // keep a pointer of the current merge
      prev = destinationList.dtls.get(0);

    }

    final BDMPaymentDestinationKey currKey = new BDMPaymentDestinationKey();

    for (int i = 1; i < destinationList.dtls.size(); i++) {
      // get the current item
      final BDMPaymentDestinationDtls curr = destinationList.dtls.get(i);

      // compare the previous item
      if (prev.destinationID == curr.destinationID
        && prev.destinationType.equals(curr.destinationType)
        && !prev.endDate.isZero()
        && prev.endDate.addDays(1).equals(curr.startDate)) {
        // merge the two destinations if they are the same and they are directly
        // adjacent to each other on a timeline
        currKey.paymentDestinationID = curr.paymentDestinationID;
        prevKey.paymentDestinationID = prev.paymentDestinationID;

        prev.recordStatusCode = RECORDSTATUS.CANCELLED;

        curr.startDate = prev.startDate;

        // cancel the first record
        bdmPaymentDestinationObj.modify(prevKey, prev);
        // adjust the start date for the next record
        bdmPaymentDestinationObj.modify(currKey, curr);

      }

      prev = curr;

    }

  }

  /**
   * method for external call to create a destination after an application is
   * submitted with direct deposit as preferred payment method
   */
  @Override
  public BDMPaymentDestinationKey addDestinationOnSubmitApplication(
    final BDMSubmitApplicationAddDDLinkDetails details)
    throws AppException, InformationalException {

    final BDMAddEFTDestinationDetails addDetails =
      new BDMAddEFTDestinationDetails();

    // if it an EFT destination, add EFT
    if (details.concernRoleBankAccountID != 0) {
      addDetails.assign(details);

      return addEFTDestination(addDetails);
    } else {
      // Task 63926 - if it is not,
      // either there already exists eft payment link record created manually
      // before
      // application submission, end date that payment link
      // or, simply use the address destination automatically created when a
      // mailing address is created
      final BDMPaymentDestination bdmPaymentDestinationObj =
        BDMPaymentDestinationFactory.newInstance();
      final BDMConcernRoleProgramTypeKey searchKey =
        new BDMConcernRoleProgramTypeKey();
      searchKey.concernRoleID = details.concernRoleID;
      searchKey.recordStatusCode = RECORDSTATUS.NORMAL;
      searchKey.programType = details.programType;
      // search for all payment destinations for the program
      final BDMPaymentDestinationDtlsList paymentDestinationList =
        bdmPaymentDestinationObj.searchAllProgramDestinations(searchKey);
      for (final BDMPaymentDestinationDtls paymentDestination : paymentDestinationList.dtls) {
        if (paymentDestination.destinationType
          .equals(DESTINATIONTYPECODE.BANKACCOUNT)) {
          final BDMUpdateEFTDestinationDetails modifyDetails =
            new BDMUpdateEFTDestinationDetails();
          modifyDetails.paymentDestinationID =
            paymentDestination.paymentDestinationID;
          modifyDetails.selDestinationID = paymentDestination.destinationID;
          modifyDetails.startDate = paymentDestination.startDate;
          modifyDetails.endDate = details.startDate.addDays(-1);
          modifyDetails.versionNo = paymentDestination.versionNo;
          // end date the existing eft destination
          modifyEFTDestination(modifyDetails);
        }
      }
      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = details.concernRoleID;

      syncProgramPaymentDestination(crKey,
        BDMBENEFITPROGRAMTYPEEntry.get(details.programType));
    }

    return new BDMPaymentDestinationKey();
  }

  /**
   * Reads existing details
   */
  @Override
  public BDMReadEFTDestinationDetails
    readEFTDestinationDetails(final BDMPaymentDestinationKey key)
      throws AppException, InformationalException {

    // read the details
    final BDMPaymentDestinationDtls bdmPaymentDestinationDtls =
      BDMPaymentDestinationFactory.newInstance().read(key);

    final BDMReadEFTDestinationDetails existingDetails =
      new BDMReadEFTDestinationDetails();
    existingDetails.versionNo = bdmPaymentDestinationDtls.versionNo;
    existingDetails.selDestinationID =
      bdmPaymentDestinationDtls.destinationID;
    existingDetails.startDate = bdmPaymentDestinationDtls.startDate;
    existingDetails.endDate = bdmPaymentDestinationDtls.endDate;
    existingDetails.programType = bdmPaymentDestinationDtls.programType;

    return existingDetails;
  }

  /**
   * Modifies the EFT destination
   */
  @Override
  public void
    modifyEFTDestination(final BDMUpdateEFTDestinationDetails details)
      throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();
    // get existing details
    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();
    final BDMPaymentDestinationKey pmtDestKey =
      new BDMPaymentDestinationKey();
    pmtDestKey.paymentDestinationID = details.paymentDestinationID;
    final BDMPaymentDestinationDtls pmtDestinationDtls =
      bdmPaymentDestinationObj.read(pmtDestKey);

    // get existing bank account details
    final ConcernRoleBankAccount crBankAccountObj =
      ConcernRoleBankAccountFactory.newInstance();
    final ConcernRoleBankAccountKey concernRoleBankAccountKey =
      new ConcernRoleBankAccountKey();
    concernRoleBankAccountKey.concernRoleBankAccountID =
      pmtDestinationDtls.destinationID;
    final ConcernRoleBankAccountDtls crBankAccountDtls =
      crBankAccountObj.read(concernRoleBankAccountKey);

    // check for issued payments
    final BDMCountIssuedPaymentsForBankAccountKey bankAccountStatusCodeKey =
      new BDMCountIssuedPaymentsForBankAccountKey();
    bankAccountStatusCodeKey.bankAccountID = crBankAccountDtls.bankAccountID;
    bankAccountStatusCodeKey.reconcilStatusCode =
      PMTRECONCILIATIONSTATUS.CANCELLED;
    final RecordCount issuedPaymentsCount =
      BDMPaymentInstrumentDAFactory.newInstance()
        .countIssuedPaymentsForBankAccount(bankAccountStatusCodeKey);

    if (issuedPaymentsCount.count > 0) {

      // cannot change start date if payments have been issued and the existing
      // start date was before current date or the start date is being changed
      // to the past
      if (!pmtDestinationDtls.startDate.equals(details.startDate)
        && (pmtDestinationDtls.startDate.before(Date.getCurrentDate())
          || details.startDate.before(Date.getCurrentDate()))) {
        informationalManager.addInformationalMsg(
          new AppException(
            BDMPAYMENTDESTINATION.ERR_START_DATE_MODIFY_PAYMENT_EXISTS),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }
      // cannot change bank account if payment has been issued
      if (pmtDestinationDtls.destinationID != details.selDestinationID
        && pmtDestinationDtls.startDate.before(Date.getCurrentDate())) {
        informationalManager.addInformationalMsg(
          new AppException(
            BDMPAYMENTDESTINATION.ERR_BANK_ACCOUNT_MODIFY_PAYMENT_EXISTS),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);

      }
      // cannot change end date if payment has been issued
      if (!pmtDestinationDtls.endDate.equals(details.endDate)
        && details.endDate.before(Date.getCurrentDate().addDays(-1))
        && !details.endDate.isZero()) {
        informationalManager.addInformationalMsg(
          new AppException(
            BDMPAYMENTDESTINATION.ERR_END_DATE_MODIFY_PAYMENT_EXISTS),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }
    } else {
      // if start date has been modified, it cannot be in the past
      if (pmtDestinationDtls.destinationID != details.selDestinationID
        && !pmtDestinationDtls.startDate.equals(details.startDate)
        && details.startDate.before(Date.getCurrentDate())) {
        informationalManager.addInformationalMsg(
          new AppException(
            BDMPAYMENTDESTINATION.ERR_START_DATE_MODIFY_PAST_DATE),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }
    }

    // fail if any of the above validations fail
    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

    // read bank account details
    final BankAccount bankAccountObj = BankAccountFactory.newInstance();
    final BankAccountKey bankAccountKey = new BankAccountKey();
    bankAccountKey.bankAccountID = crBankAccountDtls.bankAccountID;
    final BankAccountDtls bankAccountDtls =
      bankAccountObj.read(bankAccountKey);

    // check start date is after bank account start date
    if (bankAccountDtls.startDate.after(details.startDate)) {
      informationalManager.addInformationalMsg(
        new AppException(
          BDMPAYMENTDESTINATION.ERR_LINK_START_DATE_BEFORE_BANK_START_DATE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    // check end date is before bank account end date
    if (!bankAccountDtls.endDate.isZero() && (details.endDate.isZero()
      || details.endDate.after(bankAccountDtls.endDate))) {
      informationalManager.addInformationalMsg(
        new AppException(
          BDMPAYMENTDESTINATION.ERR_LINK_END_DATE_AFTER_BANK_END_DATE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    // check end date is after the start date
    if (!details.endDate.isZero()
      && details.endDate.before(details.startDate)) {
      informationalManager.addInformationalMsg(
        new AppException(
          BDMPAYMENTDESTINATION.ERR_END_DATE_BEFORE_START_DATE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }

    // set null end dates to a max date for easier comparison
    if (details.endDate.isZero()) {
      details.endDate = BDMConstants.kMaxDate;
    }
    if (pmtDestinationDtls.endDate.isZero()) {
      pmtDestinationDtls.endDate = BDMConstants.kMaxDate;
    }

    // get all destination records
    final BDMConcernRoleProgramTypeKey searchDestinationsKey =
      new BDMConcernRoleProgramTypeKey();
    searchDestinationsKey.concernRoleID = pmtDestinationDtls.concernRoleID;
    searchDestinationsKey.recordStatusCode = RECORDSTATUS.NORMAL;
    searchDestinationsKey.programType = pmtDestinationDtls.programType;
    final BDMPaymentDestinationDtlsList allDestinationsList =
      bdmPaymentDestinationObj
        .searchAllProgramDestinations(searchDestinationsKey);

    final GeneralUtility generalUtilityObj =
      GeneralUtilityFactory.newInstance();
    final CheckDatePeriodOverlapKey checkDateOverlapKey =
      new CheckDatePeriodOverlapKey();

    // check for overlaps
    for (final BDMPaymentDestinationDtls otherPmtDestinationDtls : allDestinationsList.dtls) {

      if (otherPmtDestinationDtls.endDate.isZero()) {
        otherPmtDestinationDtls.endDate = BDMConstants.kMaxDate;
      }
      // skip if it is the record we are trying to modify or if it is not EFT
      if (otherPmtDestinationDtls.paymentDestinationID == details.paymentDestinationID
        || !otherPmtDestinationDtls.destinationType
          .equals(DESTINATIONTYPECODE.BANKACCOUNT)) {
        continue;
      }

      // check for overlap
      checkDateOverlapKey.startDateA = otherPmtDestinationDtls.startDate;
      checkDateOverlapKey.endDateA = otherPmtDestinationDtls.endDate;
      checkDateOverlapKey.startDateB = details.startDate;
      checkDateOverlapKey.endDateB = details.endDate;
      final CheckDatePeriodOverlapResult overlapResult =
        generalUtilityObj.checkDatePeriodOverlap(checkDateOverlapKey);

      if (overlapResult.datePeriodOverlapInd) {
        // read bank account details
        concernRoleBankAccountKey.concernRoleBankAccountID =
          otherPmtDestinationDtls.destinationID;
        final ConcernRoleBankAccountDtls crOtherBankAccountDtls =
          crBankAccountObj.read(concernRoleBankAccountKey);
        bankAccountKey.bankAccountID = crOtherBankAccountDtls.bankAccountID;
        final BankAccountDtls otherBankAccountDtls =
          BankAccountFactory.newInstance().read(bankAccountKey);

        final AppException appException =
          new AppException(BDMPAYMENTDESTINATION.ERR_LINK_OVERLAPS_EXISTING);

        appException.arg(otherBankAccountDtls.accountNumber);

        informationalManager.addInformationalMsg(appException,
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }

    }

    // second check for date validations, fail operation if any of these fail
    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

    final List<BDMPaymentDestinationDtls> predecessorItems =
      new ArrayList<>();
    final List<BDMPaymentDestinationDtls> successorItems = new ArrayList<>();

    // sort destinations into predecessors and sucessors of the record being
    // modified (based off the old start and end dates)
    for (final BDMPaymentDestinationDtls destination : allDestinationsList.dtls) {
      if (destination.endDate.before(pmtDestinationDtls.startDate)) {
        predecessorItems.add(destination);
      } else if (pmtDestinationDtls.endDate.before(destination.startDate)) {
        successorItems.add(destination);
      }
    }

    // the start date has been pushed back, adjust any address type destinations
    // to remove overlaps
    if (details.startDate.before(pmtDestinationDtls.startDate)) {
      final BDMPaymentDestinationKey deleteKey =
        new BDMPaymentDestinationKey();
      for (final BDMPaymentDestinationDtls predecessor : predecessorItems) {
        if (predecessor.destinationType.equals(DESTINATIONTYPECODE.ADDRESS)) {
          // delete the predecessor if the start date has been pushed back to
          // before the predecessor's start date
          if (predecessor.startDate.compareTo(details.startDate) >= 0) {
            deleteKey.paymentDestinationID = predecessor.paymentDestinationID;
            deleteRecord(deleteKey);
          }
          // adjust the end date if the new date has not been pushed back before
          // the predecessor's start date
          else if (predecessor.startDate.before(details.startDate)
            && predecessor.endDate.compareTo(details.startDate) >= 0) {

            final BDMAdjustPaymentDestinationDatesDetails adjustDetails =
              new BDMAdjustPaymentDestinationDatesDetails();

            adjustDetails.paymentDestinationID =
              predecessor.paymentDestinationID;
            adjustDetails.startDate = predecessor.startDate;
            adjustDetails.endDate = details.startDate.addDays(-1);
            adjustDestinationDates(adjustDetails);
          }
        }
      }
    }
    // fill in the address gaps
    else if (details.startDate.after(pmtDestinationDtls.startDate)) {

      final BDMFillInAddressDestinationsKey fillInAddressDestinationsKey =
        new BDMFillInAddressDestinationsKey();

      fillInAddressDestinationsKey.concernRoleID =
        pmtDestinationDtls.concernRoleID;
      fillInAddressDestinationsKey.startDate = pmtDestinationDtls.startDate;
      fillInAddressDestinationsKey.endDate = details.startDate.addDays(-1);
      fillInAddressDestinationsKey.programType =
        pmtDestinationDtls.programType;

      fillInAddressDestinations(fillInAddressDestinationsKey);

    }
    if (details.endDate.after(pmtDestinationDtls.endDate)) {
      final BDMPaymentDestinationKey deleteKey =
        new BDMPaymentDestinationKey();
      for (final BDMPaymentDestinationDtls successor : successorItems) {
        // if the end date was pushed forward, adjust address type destinations
        // to remove overlaps
        if (successor.endDate.compareTo(details.endDate) <= 0) {
          deleteKey.paymentDestinationID = successor.paymentDestinationID;
          deleteRecord(deleteKey);
        }
        // adjust the start date if the new date has not been pushed back after
        // the predecessor's end date
        if (successor.startDate.compareTo(details.endDate) <= 0
          && successor.endDate.after(details.endDate)) {
          final BDMAdjustPaymentDestinationDatesDetails adjustDetails =
            new BDMAdjustPaymentDestinationDatesDetails();

          adjustDetails.paymentDestinationID = successor.paymentDestinationID;
          adjustDetails.startDate = details.endDate.addDays(1);
          adjustDetails.endDate = successor.endDate;
          adjustDestinationDates(adjustDetails);

        }
      }
    }
    // fill in address gaps
    else if (details.endDate.before(pmtDestinationDtls.endDate)) {

      final BDMFillInAddressDestinationsKey fillInAddressDestinationsKey =
        new BDMFillInAddressDestinationsKey();

      fillInAddressDestinationsKey.concernRoleID =
        pmtDestinationDtls.concernRoleID;
      fillInAddressDestinationsKey.startDate = details.endDate.addDays(1);
      fillInAddressDestinationsKey.endDate = pmtDestinationDtls.endDate;
      fillInAddressDestinationsKey.programType =
        pmtDestinationDtls.programType;

      fillInAddressDestinations(fillInAddressDestinationsKey);

    }

    pmtDestinationDtls.destinationID = details.selDestinationID;
    pmtDestinationDtls.startDate = details.startDate;
    pmtDestinationDtls.endDate = details.endDate;
    if (pmtDestinationDtls.endDate.equals(BDMConstants.kMaxDate)) {
      pmtDestinationDtls.endDate = Date.kZeroDate;
    }

    pmtDestinationDtls.versionNo = details.versionNo;

    bdmPaymentDestinationObj.modify(pmtDestKey, pmtDestinationDtls);

    // roll up items
    final BDMRollupSuccessiveDestKey rollupKey =
      new BDMRollupSuccessiveDestKey();
    rollupKey.concernRoleID = pmtDestinationDtls.concernRoleID;
    rollupKey.programType = pmtDestinationDtls.programType;
    rollUpSuccessiveItems(rollupKey);

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = pmtDestinationDtls.concernRoleID;

    // sync
    syncPaymentDestinationByProgram(crKey,
      BDMBENEFITPROGRAMTYPEEntry.get(pmtDestinationDtls.programType));
  }

  /**
   * Fill in the given period with address destinations
   *
   * @param details
   * @throws AppException
   * @throws InformationalException
   */
  private void
    fillInAddressDestinations(final BDMFillInAddressDestinationsKey details)
      throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    // search for all mailing addresses for a concern role
    final BDMMailingAddressesKey addressKey = new BDMMailingAddressesKey();
    addressKey.concernRoleID = details.concernRoleID;
    addressKey.recordStatusCode = RECORDSTATUS.NORMAL;
    addressKey.typeCode = CONCERNROLEADDRESSTYPE.MAILING;

    final BDMCurrentMailingAddressDetailsList addressList =
      bdmPaymentDestinationObj.searchAllMailingAddresses(addressKey);

    // set to a max date when end date is null for comparison convenience
    for (final BDMCurrentMailingAddressDetails address : addressList.dtls) {
      if (address.endDate.isZero()) {
        address.endDate = BDMConstants.kMaxDate;
      }
    }

    if (details.endDate.isZero()) {
      details.endDate = BDMConstants.kMaxDate;
    }

    for (final BDMCurrentMailingAddressDetails address : addressList.dtls) {
      // if the address overlaps with the given range - create an entry for a
      // destination
      if (!address.endDate.before(details.startDate)
        && !address.startDate.after(details.endDate)) {

        final BDMPaymentDestinationDtls destinationDtls =
          new BDMPaymentDestinationDtls();

        destinationDtls.paymentDestinationID = UniqueID.nextUniqueID();
        destinationDtls.concernRoleID = details.concernRoleID;
        destinationDtls.destinationID = address.concernRoleAddressID;
        destinationDtls.destinationType = DESTINATIONTYPECODE.ADDRESS;
        destinationDtls.programType = details.programType;
        destinationDtls.recordStatusCode = RECORDSTATUS.NORMAL;

        // set the start dates and end dates
        if (details.startDate.after(address.startDate)) {
          destinationDtls.startDate = details.startDate;
        } else {
          destinationDtls.startDate = address.startDate;
        }
        if (details.endDate.before(address.endDate)) {
          destinationDtls.endDate = details.endDate;
        } else {
          destinationDtls.endDate = address.endDate;
        }
        if (destinationDtls.endDate.equals(BDMConstants.kMaxDate)) {
          destinationDtls.endDate = Date.kZeroDate;
        }

        // insert the record
        bdmPaymentDestinationObj.insert(destinationDtls);
      }
    }

  }

  /**
   * Cancels an EFT destination
   */
  @Override
  public VersionNumberDetails
    cancelEFTDestination(final BDMPaymentDestinationIDVersionNo key)
      throws AppException, InformationalException {

    // get existing details
    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();
    final BDMPaymentDestinationKey pmtDestKey =
      new BDMPaymentDestinationKey();
    pmtDestKey.paymentDestinationID = key.paymentDestinationID;
    final BDMPaymentDestinationDtls pmtDestinationDtls =
      bdmPaymentDestinationObj.read(pmtDestKey);

    // get existing bank account details
    final ConcernRoleBankAccountKey concernRoleBankAccountKey =
      new ConcernRoleBankAccountKey();
    concernRoleBankAccountKey.concernRoleBankAccountID =
      pmtDestinationDtls.destinationID;
    final ConcernRoleBankAccountDtls crBankAccountDtls =
      ConcernRoleBankAccountFactory.newInstance()
        .read(concernRoleBankAccountKey);

    // check for issued payments
    final BDMCountIssuedPaymentsForBankAccountKey bankAccountStatusCodeKey =
      new BDMCountIssuedPaymentsForBankAccountKey();
    bankAccountStatusCodeKey.bankAccountID = crBankAccountDtls.bankAccountID;
    bankAccountStatusCodeKey.reconcilStatusCode =
      PMTRECONCILIATIONSTATUS.CANCELLED;
    final RecordCount issuedPaymentsCount =
      BDMPaymentInstrumentDAFactory.newInstance()
        .countIssuedPaymentsForBankAccount(bankAccountStatusCodeKey);

    if (issuedPaymentsCount.count > 0
      && pmtDestinationDtls.startDate.before(Date.getCurrentDate())) {
      throw new AppException(
        BDMPAYMENTDESTINATION.ERR_DELETE_LINK_PAYMENT_EXISTS);
    }

    pmtDestinationDtls.recordStatusCode = RECORDSTATUS.CANCELLED;
    pmtDestinationDtls.versionNo = key.versionNo;

    bdmPaymentDestinationObj.modify(pmtDestKey, pmtDestinationDtls);

    // fill in the delete record gap with an address destination record
    final BDMFillInAddressDestinationsKey fillAddressKey =
      new BDMFillInAddressDestinationsKey();
    fillAddressKey.concernRoleID = pmtDestinationDtls.concernRoleID;
    fillAddressKey.startDate = pmtDestinationDtls.startDate;
    fillAddressKey.endDate = pmtDestinationDtls.endDate;
    fillAddressKey.programType = pmtDestinationDtls.programType;

    fillInAddressDestinations(fillAddressKey);

    // rollup the items
    final BDMRollupSuccessiveDestKey rollUpKey =
      new BDMRollupSuccessiveDestKey();
    rollUpKey.concernRoleID = pmtDestinationDtls.concernRoleID;
    rollUpKey.programType = pmtDestinationDtls.programType;
    rollUpSuccessiveItems(rollUpKey);

    // sync the destinations
    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = pmtDestinationDtls.concernRoleID;
    syncPaymentDestinationByProgram(crKey,
      BDMBENEFITPROGRAMTYPEEntry.get(pmtDestinationDtls.programType));

    // return version number
    final BDMPaymentDestinationDtls updatedPmtDestDetails =
      bdmPaymentDestinationObj.read(pmtDestKey);

    final VersionNumberDetails versionNumberDetails =
      new VersionNumberDetails();
    versionNumberDetails.versionNo = updatedPmtDestDetails.versionNo;
    return versionNumberDetails;
  }

  /**
   * Update just the case nominee destination
   */
  @Override
  public void
    updateCaseParticipantDestinationsOnly(final CaseParticipantRoleKey cprKey)
      throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    // find the active destination
    final BDMActiveDestinationByCaseParticipantKey readActiveDestKey =
      new BDMActiveDestinationByCaseParticipantKey();
    readActiveDestKey.caseParticipantRoleID = cprKey.caseParticipantRoleID;
    readActiveDestKey.recordStatusCode = RECORDSTATUS.NORMAL;
    readActiveDestKey.currentDate = Date.getCurrentDate();

    String destinationType = "";
    long destinationID = 0;
    try {
      final BDMDestinationByCaseParticipantDetails activeDestinationDetails =
        bdmPaymentDestinationObj
          .readActiveDestinationByCaseParticipant(readActiveDestKey);

      destinationID = activeDestinationDetails.destinationID;
      destinationType = activeDestinationDetails.destinationType;
    } catch (final RecordNotFoundException e) {
      // if no destination was found, we will set the destination to be
      // address
      destinationType = DESTINATIONTYPECODE.ADDRESS;

      final CaseParticipantRoleDtls cprDetails =
        CaseParticipantRoleFactory.newInstance().read(cprKey);

      final BDMCurrentMailingAddressKey mailingAddressKey =
        new BDMCurrentMailingAddressKey();
      mailingAddressKey.concernRoleID = cprDetails.participantRoleID;
      mailingAddressKey.statusCode = RECORDSTATUS.NORMAL;
      mailingAddressKey.typeCode = CONCERNROLEADDRESSTYPE.MAILING;
      mailingAddressKey.currentDate = Date.getCurrentDate();
      try {
        final BDMCurrentMailingAddressDetails mailingAddress =
          bdmPaymentDestinationObj
            .readCurrentMailingAddress(mailingAddressKey);

        destinationID = mailingAddress.concernRoleAddressID;

      } catch (final RecordNotFoundException e2) {

        throw new AppException(
          BDMPAYMENTDESTINATION.ERR_NO_ACTIVE_DESTINATION_OR_ADDRESS);

      }
    }

    if (destinationID != 0) {

      final BDMNomineesForCaseParticipantKey searchNomineesKey =
        new BDMNomineesForCaseParticipantKey();
      searchNomineesKey.caseParticipantRoleID = cprKey.caseParticipantRoleID;
      searchNomineesKey.recordStatusCode = RECORDSTATUS.NORMAL;
      searchNomineesKey.nomineeStatus = CASENOMINEESTATUS.OPERATIONAL;
      // update destination for all nominees
      final CaseNomineeKeyList nomineeList = bdmPaymentDestinationObj
        .searchNomineesByCaseParticipant(searchNomineesKey);

      final CaseNomineeDestination cndObj =
        CaseNomineeDestinationFactory.newInstance();

      for (final CaseNomineeKey nominee : nomineeList.dtls) {
        final DestinationTypeStatusCaseNomineeIDKey readCNDKey =
          new DestinationTypeStatusCaseNomineeIDKey();
        readCNDKey.caseNomineeID = nominee.caseNomineeID;
        readCNDKey.destinationType = destinationType;
        readCNDKey.statusCode = RECORDSTATUS.NORMAL;

        // find existing case nominee destination for the nominee with the given
        // destination type
        final NotFoundIndicator nfIndicator = new NotFoundIndicator();
        CaseNomineeDestinationDtls caseNomineeDestinationDtls =
          cndObj.readByTypeStatusAndCaseNomineeID(nfIndicator, readCNDKey);

        if (nfIndicator.isNotFound()) {
          // create new case nominee destination if it could not be found
          caseNomineeDestinationDtls = new CaseNomineeDestinationDtls();
          caseNomineeDestinationDtls.caseNomineeDestinationID =
            nominee.caseNomineeID;
          caseNomineeDestinationDtls.destinationID = destinationID;
          caseNomineeDestinationDtls.destinationType = destinationType;
          caseNomineeDestinationDtls.statusCode = RECORDSTATUS.NORMAL;

          cndObj.insert(caseNomineeDestinationDtls);
        } else {
          // change the case nominee destination ID
          caseNomineeDestinationDtls.destinationID = destinationID;
          final CaseNomineeDestinationKey cndKey =
            new CaseNomineeDestinationKey();
          cndKey.caseNomineeDestinationID =
            caseNomineeDestinationDtls.caseNomineeDestinationID;
          cndObj.modify(cndKey, caseNomineeDestinationDtls);
        }
      }
    }

  }

  /**
   * When an address is changed, update any destinations linked to an address
   */
  @Override
  public void syncDestinationsOnAddressChange(final ConcernRoleAddressKey key)
    throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    final ConcernRoleAddressDtls concernRoleAddressDtls =
      ConcernRoleAddressFactory.newInstance().read(key);

    if (!concernRoleAddressDtls.typeCode
      .equals(CONCERNROLEADDRESSTYPE.MAILING)) {
      return;
    }

    if (concernRoleAddressDtls.endDate.isZero()) {
      concernRoleAddressDtls.endDate = BDMConstants.kMaxDate;
    }

    final LinkedHashMap<String, String> programMap =
      CodeTable.getAllEnabledItems(BDMBENEFITPROGRAMTYPE.TABLENAME,
        TransactionInfo.getProgramLocale());

    for (final String programCode : programMap.keySet()) {
      // first handle if an address has had its start/end date modified
      final BDMReadByDestinationIDTypeKey readDestinationKey =
        new BDMReadByDestinationIDTypeKey();
      readDestinationKey.concernRoleID = concernRoleAddressDtls.concernRoleID;
      readDestinationKey.destinationID =
        concernRoleAddressDtls.concernRoleAddressID;
      readDestinationKey.destinationType = DESTINATIONTYPECODE.ADDRESS;
      readDestinationKey.programType = programCode;
      readDestinationKey.recordStatusCode = RECORDSTATUS.NORMAL;

      final BDMPaymentDestinationDtlsList destinationList =
        bdmPaymentDestinationObj
          .readByProgramDestinationIDType(readDestinationKey);

      for (final BDMPaymentDestinationDtls destination : destinationList.dtls) {
        if (destination.endDate.isZero()) {
          destination.endDate = BDMConstants.kMaxDate;
        }

      }

      // Handle when start/end dates have been squeezed

      // adjust all matching destinations that need to be truncated or deleted.
      // If they are completely out of the new dates, then they get deleted
      for (final BDMPaymentDestinationDtls destination : destinationList.dtls) {
        if (destination.startDate.before(concernRoleAddressDtls.startDate)) {
          final BDMAdjustPaymentDestinationDatesDetails adjustDetails =
            new BDMAdjustPaymentDestinationDatesDetails();
          adjustDetails.paymentDestinationID =
            destination.paymentDestinationID;
          adjustDetails.startDate =
            destination.startDate = concernRoleAddressDtls.startDate;
          adjustDetails.endDate = destination.endDate;

          // delete the record if the new start date is after the end date
          // (meaning the destination is completely out of range now)
          if (adjustDetails.startDate.after(adjustDetails.endDate)) {
            final BDMPaymentDestinationKey paymentDestinationKey =
              new BDMPaymentDestinationKey();
            paymentDestinationKey.paymentDestinationID =
              destination.paymentDestinationID;
            deleteRecord(paymentDestinationKey);
            continue;
          } else {
            adjustDestinationDates(adjustDetails);
          }

        }
        if (destination.endDate.after(concernRoleAddressDtls.endDate)) {
          final BDMAdjustPaymentDestinationDatesDetails adjustDetails =
            new BDMAdjustPaymentDestinationDatesDetails();
          adjustDetails.paymentDestinationID =
            destination.paymentDestinationID;
          adjustDetails.startDate = destination.startDate;
          adjustDetails.endDate = concernRoleAddressDtls.endDate;

          // delete the record if the new start date is after the end date
          // (meaning the destination is completely out of range now)
          if (adjustDetails.startDate.after(adjustDetails.endDate)) {
            final BDMPaymentDestinationKey paymentDestinationKey =
              new BDMPaymentDestinationKey();
            paymentDestinationKey.paymentDestinationID =
              destination.paymentDestinationID;
            deleteRecord(paymentDestinationKey);
          } else {
            adjustDestinationDates(adjustDetails);
          }
        }
      }

      if (concernRoleAddressDtls.statusCode.equals(RECORDSTATUS.NORMAL)) {
        // find gaps and insert address destination
        final BDMConcernRoleProgramTypeKey searchDestinationsKey =
          new BDMConcernRoleProgramTypeKey();
        searchDestinationsKey.concernRoleID =
          concernRoleAddressDtls.concernRoleID;
        searchDestinationsKey.programType = programCode;
        searchDestinationsKey.recordStatusCode = RECORDSTATUS.NORMAL;
        final BDMPaymentDestinationDtlsList allDestinationsList =
          bdmPaymentDestinationObj
            .searchAllProgramDestinations(searchDestinationsKey);

        // create a list of all destinations that have overlap with the concern
        // role address
        final GeneralUtility generalUtilityObj =
          GeneralUtilityFactory.newInstance();
        final CheckDatePeriodOverlapKey checkDateOverlapKey =
          new CheckDatePeriodOverlapKey();
        final List<BDMPaymentDestinationDtls> overlapList = new ArrayList<>();
        for (final BDMPaymentDestinationDtls destination : allDestinationsList.dtls) {
          if (destination.endDate.isZero()) {
            destination.endDate = BDMConstants.kMaxDate;
          }

          checkDateOverlapKey.startDateA = destination.startDate;
          checkDateOverlapKey.endDateA = destination.endDate;
          checkDateOverlapKey.startDateB = concernRoleAddressDtls.startDate;
          checkDateOverlapKey.endDateB = concernRoleAddressDtls.endDate;
          final CheckDatePeriodOverlapResult overlapResult =
            generalUtilityObj.checkDatePeriodOverlap(checkDateOverlapKey);

          if (overlapResult.datePeriodOverlapInd) {
            overlapList.add(destination);
          }

        }
        // if there is no overlap, create one destination for the entirety of
        // the new address range
        if (overlapList.isEmpty()) {

          final BDMPaymentDestinationDtls paymentDestinationDtls =
            new BDMPaymentDestinationDtls();

          paymentDestinationDtls.paymentDestinationID =
            UniqueID.nextUniqueID();
          paymentDestinationDtls.concernRoleID =
            concernRoleAddressDtls.concernRoleID;
          paymentDestinationDtls.destinationID =
            concernRoleAddressDtls.concernRoleAddressID;
          paymentDestinationDtls.destinationType =
            DESTINATIONTYPECODE.ADDRESS;
          paymentDestinationDtls.startDate = concernRoleAddressDtls.startDate;
          paymentDestinationDtls.endDate = concernRoleAddressDtls.endDate;
          paymentDestinationDtls.programType = programCode;

          insertRecord(paymentDestinationDtls);
        } else {
          final BDMPaymentDestinationDtls firstRecord = overlapList.get(0);
          final BDMPaymentDestinationDtls lastRecord =
            overlapList.get(overlapList.size() - 1);

          // if there is an address record with a different destinationID, we
          // will have to truncate it
          if (firstRecord.startDate.before(concernRoleAddressDtls.startDate)
            && firstRecord.destinationType
              .equals(DESTINATIONTYPECODE.ADDRESS)) {
            // insert new record with same copy but for a shorter period
            final BDMPaymentDestinationDtls paymentDestinationDtls =
              new BDMPaymentDestinationDtls();
            paymentDestinationDtls.assign(firstRecord);
            paymentDestinationDtls.paymentDestinationID =
              UniqueID.nextUniqueID();
            paymentDestinationDtls.endDate =
              concernRoleAddressDtls.startDate.addDays(-1);

            insertRecord(paymentDestinationDtls);
            final BDMPaymentDestinationKey paymentDestinationKey =
              new BDMPaymentDestinationKey();
            paymentDestinationKey.paymentDestinationID =
              firstRecord.paymentDestinationID;
            // delete the old version
            deleteRecord(paymentDestinationKey);

          }
          // if there is an address record with a different destinationID, we
          // will have to truncate it
          if (lastRecord.endDate.after(concernRoleAddressDtls.endDate)
            && lastRecord.destinationType
              .equals(DESTINATIONTYPECODE.ADDRESS)) {
            // insert new record with same copy but for a shorter period
            final BDMPaymentDestinationDtls paymentDestinationDtls =
              new BDMPaymentDestinationDtls();
            paymentDestinationDtls.assign(lastRecord);
            paymentDestinationDtls.paymentDestinationID =
              UniqueID.nextUniqueID();
            paymentDestinationDtls.startDate =
              concernRoleAddressDtls.endDate.addDays(1);

            insertRecord(paymentDestinationDtls);
            final BDMPaymentDestinationKey paymentDestinationKey =
              new BDMPaymentDestinationKey();
            paymentDestinationKey.paymentDestinationID =
              lastRecord.paymentDestinationID;
            // delete the old version
            deleteRecord(paymentDestinationKey);
          }

          // remove any deleted records
          final Set<Long> toRemove = new HashSet<>();
          for (final BDMPaymentDestinationDtls destination : overlapList) {

            final BDMPaymentDestinationKey paymentDestinationKey =
              new BDMPaymentDestinationKey();

            paymentDestinationKey.paymentDestinationID =
              destination.paymentDestinationID;

            final BDMPaymentDestinationDtls newDetails =
              bdmPaymentDestinationObj.read(paymentDestinationKey);

            if (newDetails.recordStatusCode.equals(RECORDSTATUS.CANCELLED)) {
              toRemove.add(destination.paymentDestinationID);
            }
          }

          final Predicate<BDMPaymentDestinationDtls> shouldRemove =
            details -> toRemove.contains(details.paymentDestinationID);

          // all items in the overlap list now exist solely within the concern
          // role address dates
          overlapList.removeIf(shouldRemove);

          Date startDate = concernRoleAddressDtls.startDate;
          boolean endDateReached = false;

          for (final BDMPaymentDestinationDtls destination : overlapList) {

            // if the date matches and it is an address, change the
            // destinationID
            if (startDate.equals(destination.startDate)) {

              if (destination.destinationType
                .equals(DESTINATIONTYPECODE.ADDRESS)) {

                final BDMPaymentDestinationKey destinationKey =
                  new BDMPaymentDestinationKey();
                destinationKey.paymentDestinationID =
                  destination.paymentDestinationID;

                destination.destinationID =
                  concernRoleAddressDtls.concernRoleAddressID;

                if (destination.endDate.equals(BDMConstants.kMaxDate)) {
                  destination.endDate = Date.kZeroDate;
                }

                bdmPaymentDestinationObj.modify(destinationKey, destination);

                if (destination.endDate.equals(Date.kZeroDate)) {
                  destination.endDate = BDMConstants.kMaxDate;
                }
              }

            } else {
              // if the start date of the destination does not equal the current
              // start date variable, it means there is a gap from
              // the concern role role address start date to the next existing
              // destination. We will have to fill that gap
              final BDMPaymentDestinationDtls newDestinationDtls =
                new BDMPaymentDestinationDtls();

              newDestinationDtls.concernRoleID =
                concernRoleAddressDtls.concernRoleID;
              newDestinationDtls.destinationID =
                concernRoleAddressDtls.concernRoleAddressID;
              newDestinationDtls.destinationType =
                DESTINATIONTYPECODE.ADDRESS;
              newDestinationDtls.programType = programCode;
              newDestinationDtls.recordStatusCode = RECORDSTATUS.NORMAL;
              newDestinationDtls.startDate = startDate;
              newDestinationDtls.endDate = destination.startDate.addDays(-1);
              newDestinationDtls.paymentDestinationID =
                UniqueID.nextUniqueID();

              insertRecord(newDestinationDtls);

            }

            if (destination.endDate.equals(concernRoleAddressDtls.endDate)) {
              // we have completed all the gaps
              endDateReached = true;
            } else {
              startDate = destination.endDate.addDays(1);
            }

          }

          // if end date hasn't been reached by the time we have looped through
          // all the destinations, there is a gap from the last existing
          // destination and the concern role address end date. We need to fill
          // that gap
          if (!endDateReached) {
            final BDMPaymentDestinationDtls newDestinationDtls =
              new BDMPaymentDestinationDtls();
            newDestinationDtls.concernRoleID =
              concernRoleAddressDtls.concernRoleID;
            newDestinationDtls.destinationID =
              concernRoleAddressDtls.concernRoleAddressID;
            newDestinationDtls.destinationType = DESTINATIONTYPECODE.ADDRESS;
            newDestinationDtls.programType = programCode;
            newDestinationDtls.recordStatusCode = RECORDSTATUS.NORMAL;
            newDestinationDtls.startDate = startDate;
            newDestinationDtls.endDate = concernRoleAddressDtls.endDate;
            newDestinationDtls.paymentDestinationID = UniqueID.nextUniqueID();

            insertRecord(newDestinationDtls);
          }

        }

        // roll up items
        final BDMRollupSuccessiveDestKey rollupKey =
          new BDMRollupSuccessiveDestKey();
        rollupKey.concernRoleID = concernRoleAddressDtls.concernRoleID;
        rollupKey.programType = programCode;
        rollUpSuccessiveItems(rollupKey);

      }

    }

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = concernRoleAddressDtls.concernRoleID;
    syncPaymentDestination(crKey);

  }

  /**
   * When an address is deleted, delete any payment destinations with the same
   * addressID
   */
  @Override
  public void deleteOnAddressDeletion(final ConcernRoleAddressKey key)
    throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    final ConcernRoleAddressDtls concernRoleAddressDtls =
      ConcernRoleAddressFactory.newInstance().read(key);

    if (!concernRoleAddressDtls.typeCode
      .equals(CONCERNROLEADDRESSTYPE.MAILING)) {
      return;
    }

    if (concernRoleAddressDtls.endDate.isZero()) {
      concernRoleAddressDtls.endDate = BDMConstants.kMaxDate;
    }

    final LinkedHashMap<String, String> programMap =
      CodeTable.getAllEnabledItems(BDMBENEFITPROGRAMTYPE.TABLENAME,
        TransactionInfo.getProgramLocale());

    for (final String programCode : programMap.keySet()) {
      // read all destinations with the given type and destination id
      final BDMReadByDestinationIDTypeKey readDestinationKey =
        new BDMReadByDestinationIDTypeKey();
      readDestinationKey.concernRoleID = concernRoleAddressDtls.concernRoleID;
      readDestinationKey.destinationID =
        concernRoleAddressDtls.concernRoleAddressID;
      readDestinationKey.destinationType = DESTINATIONTYPECODE.ADDRESS;
      readDestinationKey.programType = programCode;
      readDestinationKey.recordStatusCode = RECORDSTATUS.NORMAL;

      final BDMPaymentDestinationDtlsList destinationList =
        bdmPaymentDestinationObj
          .readByProgramDestinationIDType(readDestinationKey);

      // delete all records
      final BDMPaymentDestinationKey pmtDestinationKey =
        new BDMPaymentDestinationKey();
      for (final BDMPaymentDestinationDtls destination : destinationList.dtls) {

        pmtDestinationKey.paymentDestinationID =
          destination.paymentDestinationID;
        deleteRecord(pmtDestinationKey);

      }
    }
  }

  /**
   * Insert a record
   *
   * @param dtls
   * @throws AppException
   * @throws InformationalException
   */
  private void insertRecord(final BDMPaymentDestinationDtls dtls)
    throws AppException, InformationalException {

    if (dtls.endDate.equals(BDMConstants.kMaxDate)) {
      dtls.endDate = Date.kZeroDate;
    }
    dtls.recordStatusCode = RECORDSTATUS.NORMAL;

    BDMPaymentDestinationFactory.newInstance().insert(dtls);

  }

  /**
   * Called when payment details are updated by a life event. Manages any
   * required payment destination updates.
   */
  @Override
  public void
    setDestinationByLifeEvent(final BDMSetDestinationByLifeEventKey key)
      throws AppException, InformationalException {

    if (key.destinationType.equals(DESTINATIONTYPECODE.BANKACCOUNT)
      && key.concernRoleBankAccountID == 0L) {
      throw new AppException(
        BDMPAYMENTDESTINATION.ERR_NO_BANK_ACCOUNT_FOR_EFT);
    }

    final BDMPaymentDestination paymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    final BDMConcernRoleProgramTypeKey searchKey =
      new BDMConcernRoleProgramTypeKey();
    searchKey.concernRoleID = key.concernRoleID;
    searchKey.programType = key.programType;
    searchKey.recordStatusCode = RECORDSTATUS.NORMAL;

    // search for all payment destinations for the program
    final BDMPaymentDestinationDtlsList paymentDestinationList =
      paymentDestinationObj.searchAllProgramDestinations(searchKey);

    for (final BDMPaymentDestinationDtls destination : paymentDestinationList.dtls) {
      // if the destination starts today or in the future, delete the record
      if (!destination.startDate.before(key.startDate)) {

        final BDMPaymentDestinationKey destinationKey =
          new BDMPaymentDestinationKey();
        destinationKey.paymentDestinationID =
          destination.paymentDestinationID;

        deleteRecord(destinationKey);
      }
    }

    BDMPaymentDestinationDtls existingDestination = null;
    for (final BDMPaymentDestinationDtls destination : paymentDestinationList.dtls) {

      // find overlapping existing destination
      if (destination.startDate.before(key.startDate)
        && (destination.endDate.isZero()
          || !destination.endDate.before(key.startDate))) {

        existingDestination = destination;
        break;

      }
    }

    if (existingDestination != null) {

      if (key.destinationType.equals(DESTINATIONTYPECODE.BANKACCOUNT)) {

        // if life event update and existing destinations are both bank account,
        // check if it is the same bank account or different
        if (existingDestination.destinationType
          .equals(DESTINATIONTYPECODE.BANKACCOUNT)) {

          // if the bank account is the same, we just need to remove an end date
          if (existingDestination.destinationID == key.concernRoleBankAccountID) {
            final BDMUpdateEFTDestinationDetails modifyDetails =
              new BDMUpdateEFTDestinationDetails();
            modifyDetails.paymentDestinationID =
              existingDestination.paymentDestinationID;
            modifyDetails.selDestinationID = key.concernRoleBankAccountID;
            modifyDetails.startDate = existingDestination.startDate;
            modifyDetails.versionNo = existingDestination.versionNo;

            // remove the end date and make the destination open ended
            modifyEFTDestination(modifyDetails);
          } else {
            final BDMUpdateEFTDestinationDetails modifyDetails =
              new BDMUpdateEFTDestinationDetails();
            modifyDetails.paymentDestinationID =
              existingDestination.paymentDestinationID;
            modifyDetails.selDestinationID =
              existingDestination.destinationID;
            modifyDetails.startDate = existingDestination.startDate;
            modifyDetails.endDate = key.startDate.addDays(-1);
            modifyDetails.versionNo = existingDestination.versionNo;

            // end date the existing destination
            modifyEFTDestination(modifyDetails);

            final BDMAddEFTDestinationDetails addDetails =
              new BDMAddEFTDestinationDetails();
            addDetails.concernRoleBankAccountID =
              key.concernRoleBankAccountID;
            addDetails.concernRoleID = key.concernRoleID;
            addDetails.startDate = key.startDate;
            addDetails.programType = key.programType;

            // add the new destination
            addEFTDestination(addDetails);

          }

        }
        // if it's an address, end date the address
        else {

          final BDMAdjustPaymentDestinationDatesDetails adjustDetails =
            new BDMAdjustPaymentDestinationDatesDetails();
          adjustDetails.paymentDestinationID =
            existingDestination.paymentDestinationID;
          adjustDetails.startDate = existingDestination.startDate;
          adjustDetails.endDate = key.startDate.addDays(-1);

          // end the address
          adjustDestinationDates(adjustDetails);

          final BDMAddEFTDestinationDetails addDetails =
            new BDMAddEFTDestinationDetails();
          addDetails.concernRoleBankAccountID = key.concernRoleBankAccountID;
          addDetails.concernRoleID = key.concernRoleID;
          addDetails.startDate = key.startDate;
          addDetails.programType = key.programType;

          // add the new destination
          addEFTDestination(addDetails);

        }

      }
      // if the new destination is an address
      else {
        final BDMFillInAddressDestinationsKey fillInAddressDestinationsKey =
          new BDMFillInAddressDestinationsKey();
        fillInAddressDestinationsKey.concernRoleID = key.concernRoleID;

        fillInAddressDestinationsKey.programType = key.programType;

        if (existingDestination.destinationType
          .equals(DESTINATIONTYPECODE.BANKACCOUNT)) {
          // modify EFT will auto add address to fill the gap, so fill in after
          // the original destination's end date
          fillInAddressDestinationsKey.startDate =
            existingDestination.endDate.addDays(1);
          // end date existing EFT destination
          final BDMUpdateEFTDestinationDetails modifyDetails =
            new BDMUpdateEFTDestinationDetails();
          modifyDetails.paymentDestinationID =
            existingDestination.paymentDestinationID;
          modifyDetails.selDestinationID = existingDestination.destinationID;
          modifyDetails.startDate = existingDestination.startDate;
          modifyDetails.endDate = key.startDate.addDays(-1);
          modifyDetails.versionNo = existingDestination.versionNo;

          // end date the existing destination
          modifyEFTDestination(modifyDetails);

        } else {
          // should fill in addresses from the start date
          fillInAddressDestinationsKey.startDate = key.startDate;
          // end date existing address
          final BDMAdjustPaymentDestinationDatesDetails adjustDetails =
            new BDMAdjustPaymentDestinationDatesDetails();
          adjustDetails.paymentDestinationID =
            existingDestination.paymentDestinationID;
          adjustDetails.startDate = existingDestination.startDate;
          adjustDetails.endDate = key.startDate.addDays(-1);

          // end the address
          adjustDestinationDates(adjustDetails);

        }

        // create new address destination(s) from the start date onwards
        fillInAddressDestinations(fillInAddressDestinationsKey);
      }
    }
    // if there is no existing destination, just create a new
    // destination
    else {
      if (key.destinationType.equals(DESTINATIONTYPECODE.BANKACCOUNT)) {
        final BDMAddEFTDestinationDetails addDetails =
          new BDMAddEFTDestinationDetails();
        addDetails.concernRoleBankAccountID = key.concernRoleBankAccountID;
        addDetails.concernRoleID = key.concernRoleID;
        addDetails.startDate = key.startDate;
        addDetails.programType = key.programType;

        // add the new destination
        addEFTDestination(addDetails);
      } else {
        final BDMFillInAddressDestinationsKey fillInAddressDestinationsKey =
          new BDMFillInAddressDestinationsKey();
        fillInAddressDestinationsKey.concernRoleID = key.concernRoleID;
        fillInAddressDestinationsKey.startDate = key.startDate;
        fillInAddressDestinationsKey.programType = key.programType;

        // create new address destination(s) from the start date onwards
        fillInAddressDestinations(fillInAddressDestinationsKey);
      }
    }

    final BDMRollupSuccessiveDestKey rollupKey =
      new BDMRollupSuccessiveDestKey();
    rollupKey.concernRoleID = key.concernRoleID;
    rollupKey.programType = key.programType;
    // rollup items
    rollUpSuccessiveItems(rollupKey);

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = key.concernRoleID;

    syncPaymentDestinationByProgram(crKey,
      BDMBENEFITPROGRAMTYPEEntry.get(key.programType));

  }

  /**
   * Sync payment destinations for a specific program only
   *
   * @param crKey
   * @param programType
   * @throws AppException
   * @throws InformationalException
   */
  private void syncPaymentDestinationByProgram(final ConcernRoleKey crKey,
    final BDMBENEFITPROGRAMTYPEEntry programType)
    throws AppException, InformationalException {

    final BDMPaymentDestination paymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();

    final BDMSearchDestinationsPendingSyncKey searchKey =
      new BDMSearchDestinationsPendingSyncKey();
    searchKey.concernRoleID = crKey.concernRoleID;
    searchKey.recordStatusCode = RECORDSTATUS.NORMAL;
    searchKey.syncDestinationInd = true;

    final BDMPaymentDestinationDtlsList paymentDestinationDtlsList =
      paymentDestinationObj.searchDestinationPendingSync(searchKey);

    boolean syncInd = false;
    for (final BDMPaymentDestinationDtls destination : paymentDestinationDtlsList.dtls) {

      // if the destination is before or on the current date and matches the
      // given program, we will sync the program
      if (!destination.startDate.after(Date.getCurrentDate())
        && destination.programType.equals(programType.getCode())) {
        if (syncInd == false) {
          syncProgramPaymentDestination(crKey, programType);
          syncInd = true;
        }

        destination.syncDestinationInd = false;
        final BDMPaymentDestinationKey modifyKey =
          new BDMPaymentDestinationKey();
        modifyKey.paymentDestinationID = destination.paymentDestinationID;

        // set sync to false
        paymentDestinationObj.modify(modifyKey, destination);
      }
    }

    if (syncInd == true) {
      final BDMNextActiveDestinationKey nextDestKey =
        new BDMNextActiveDestinationKey();
      nextDestKey.concernRoleID = crKey.concernRoleID;
      nextDestKey.currentDate = Date.getCurrentDate();
      nextDestKey.programType = programType.getCode();
      nextDestKey.recordStatusCode = RECORDSTATUS.NORMAL;
      // set sync indicator for next record
      try {
        final BDMNextActiveDestinationDetails nextActiveDestination =
          paymentDestinationObj.readNextActiveDestination(nextDestKey);

        final BDMPaymentDestinationKey modifyKey =
          new BDMPaymentDestinationKey();
        modifyKey.paymentDestinationID =
          nextActiveDestination.paymentDestinationID;

        final BDMModifySyncIndicatorDetails modifySyncDetails =
          new BDMModifySyncIndicatorDetails();
        modifySyncDetails.syncDestinationInd = true;
        modifySyncDetails.versionNo = nextActiveDestination.versionNo;

        // set sync indicator to true
        paymentDestinationObj.modifySyncIndicator(modifyKey,
          modifySyncDetails);

      } catch (final RecordNotFoundException e) {
        // do nothing if no record found
      }
    }

  }

}
