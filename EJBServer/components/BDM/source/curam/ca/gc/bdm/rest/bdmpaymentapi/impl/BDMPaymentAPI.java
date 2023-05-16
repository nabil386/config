package curam.ca.gc.bdm.rest.bdmpaymentapi.impl;

import com.google.inject.Inject;
import com.ibm.icu.util.Calendar;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPFORMTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPTYPE;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataRL1Factory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataT4AFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataRL1;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataT4A;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Dtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Key;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4ADtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4AKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipsForDisplayKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMViewTaxSlipDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMViewTaxSlipDetailsList;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipDataKey;
import curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipInlineDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAPayment;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAPaymentDetail;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAPaymentList;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAPaymentSearchKey;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipAttrDetails;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipDataKey;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipPDFData;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAViewTaxSlipDetails;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAViewTaxSlipInlineDetails;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAViewTaxSlipList;
import curam.ca.gc.bdm.sl.financial.managetaxslips.impl.BDMGenerateTaxSlipPDFImpl;
import curam.ca.gc.bdm.sl.financial.managetaxslips.intf.BDMManageTaxSlips;
import curam.citizenaccount.externalservices.fact.ExternalPaymentBreakdownFactory;
import curam.citizenaccount.externalservices.fact.ExternalPaymentFactory;
import curam.citizenaccount.externalservices.intf.ExternalPayment;
import curam.citizenaccount.externalservices.struct.ExternalPaymentBreakdownDtls;
import curam.citizenaccount.externalservices.struct.ExternalPaymentBreakdownDtlsList;
import curam.citizenaccount.externalservices.struct.ExternalPaymentDtls;
import curam.citizenaccount.externalservices.struct.ExternalPaymentKey;
import curam.citizenaccount.facade.fact.CitizenAccountFactory;
import curam.citizenaccount.facade.intf.CitizenAccount;
import curam.citizenaccount.facade.struct.CitizenPaymentInstDetailsList;
import curam.citizenaccount.facade.struct.CitizenPaymentInstructionDetails;
import curam.citizenaccount.facade.struct.CitizenPaymentInstrumentDetails;
import curam.citizenaccount.facade.struct.ReadPaymentDetailsKey;
import curam.citizenaccount.security.impl.CitizenAccountSecurityInternal;
import curam.citizenworkspace.rest.message.impl.RESTAPIERRORMESSAGESExceptionCreator;
import curam.codetable.BANKACCOUNTSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CANCELLATIONREQUESTEntry;
import curam.codetable.impl.CREDITDEBITEntry;
import curam.codetable.impl.DEDUCTIONNAMEEntry;
import curam.codetable.impl.FININSTRUCTIONSTATUSEntry;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.facade.fact.CaseHeaderFactory;
import curam.core.facade.fact.OrganizationFactory;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.intf.CaseHeader;
import curam.core.facade.intf.Organization;
import curam.core.facade.struct.CaseHeaderDetails;
import curam.core.facade.struct.CaseIDDetails;
import curam.core.fact.BankAccountFactory;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.FinancialInstructionFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.fact.PaymentCancellationRequestFactory;
import curam.core.fact.PaymentInstructionFactory;
import curam.core.fact.PaymentInstrumentFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.BankAccount;
import curam.core.intf.CaseDeductionItem;
import curam.core.intf.FinancialInstruction;
import curam.core.intf.InstructionLineItem;
import curam.core.intf.PaymentCancellationRequest;
import curam.core.intf.PaymentInstruction;
import curam.core.intf.PaymentInstrument;
import curam.core.struct.BankAccountDtls;
import curam.core.struct.CaseDeductionItemFinCompID;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.DeductionName;
import curam.core.struct.FinInstructionID;
import curam.core.struct.FinancialInstructionDtls;
import curam.core.struct.FinancialInstructionKey;
import curam.core.struct.ILIFinInstructID;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.InstructionLineItemDtlsList;
import curam.core.struct.PaymentCancellationRequestDtls;
import curam.core.struct.PaymentDateDetails;
import curam.core.struct.PaymentInstrumentDtls;
import curam.core.struct.PaymentInstrumentKey;
import curam.core.struct.PmtInstrumentID;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.SortCodeAccountNumStruct;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.workspaceservices.localization.fact.TextTranslationFactory;
import curam.workspaceservices.localization.intf.TextTranslation;
import curam.workspaceservices.localization.struct.ReadByLocalizableTextIDandLocaleKey;
import curam.workspaceservices.localization.struct.TextTranslationDtls;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BDMPaymentAPI
  extends curam.ca.gc.bdm.rest.bdmpaymentapi.base.BDMPaymentAPI {

  // Begin - PN - imports for re-implementation of OOTB PaymentsAPI
  final CitizenAccount citizenAccountFacade =
    CitizenAccountFactory.newInstance();

  final PaymentInstrument paymentInstrumentFacade =
    PaymentInstrumentFactory.newInstance();

  final PaymentInstruction paymentInstructionFacade =
    PaymentInstructionFactory.newInstance();

  final FinancialInstruction financialInstructionFacade =
    FinancialInstructionFactory.newInstance();

  final InstructionLineItem instructionLineItemFacade =
    InstructionLineItemFactory.newInstance();

  final ExternalPayment externalPaymentFacade =
    ExternalPaymentFactory.newInstance();

  final Organization organizationFacade = OrganizationFactory.newInstance();

  final CaseHeader caseHeaderFacade = CaseHeaderFactory.newInstance();

  @Inject
  private CitizenAccountSecurityInternal citizenAccountSecurity;

  @Inject
  private BDMGenerateTaxSlipPDFImpl generateTaxSlipPDFImpl;

  // End - PN - imports for re-implementation of OOTB PaymentsAPI

  public BDMPaymentAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  // The v2 implementation for the ua/payments API
  @Override
  public BDMUAPaymentList
    bdmListPayments(final BDMUAPaymentSearchKey bdmPaymentSearchKey)
      throws AppException, InformationalException {

    // Begin - PN - Get OOTB payment list details
    final BDMUAPaymentList bdmUAPaymentList =
      this.listPayments(bdmPaymentSearchKey);

    for (final BDMUAPayment bdmUAPayment : bdmUAPaymentList.data) {

      // Begin - PN - Grab bank account nickname
      if (!bdmUAPayment.uaPayment.bankAccountNumber.isEmpty()) {
        final BankAccount bankAccountObj = BankAccountFactory.newInstance();
        final SortCodeAccountNumStruct sortCodeAccountNumStruct =
          new SortCodeAccountNumStruct();

        sortCodeAccountNumStruct.accountNumber =
          bdmUAPayment.uaPayment.bankAccountNumber;
        sortCodeAccountNumStruct.bankSortCode =
          bdmUAPayment.uaPayment.bankSortCode;

        sortCodeAccountNumStruct.statusCode = RECORDSTATUS.DEFAULTCODE;
        sortCodeAccountNumStruct.bankAccountStatus = BANKACCOUNTSTATUS.OPEN;
        final BankAccountDtls bankAccountDtls =
          bankAccountObj.readBySortCodeAndNumber(sortCodeAccountNumStruct);

        bdmUAPayment.bankAccountNickName = bankAccountDtls.name;
      }
      // End - PN - Grab bank account nickname

      // Begin - PN - Grab Cancellation Reason
      if (bdmUAPayment.uaPayment.status.equals("CAN")) {
        final PaymentCancellationRequest paymentCancellationRequestObj =
          PaymentCancellationRequestFactory.newInstance();
        final PmtInstrumentID pmtInstrumentID = new PmtInstrumentID();
        pmtInstrumentID.pmtInstrumentID = bdmUAPayment.uaPayment.payment_id;
        final PaymentCancellationRequestDtls paymentCancellationRequestDtls =
          paymentCancellationRequestObj
            .readByPmtInstrumentID(pmtInstrumentID);

        bdmUAPayment.cancelationReason =
          determineCancelationReason(paymentCancellationRequestDtls.typeCode);
      }
      // End - PN - Grab Cancellation Reason
    }

    return bdmUAPaymentList;
  }

  public static String
    determineCancelationReason(final String cancellationCode) {

    String cancellationReason = new String();
    final String tableName = CANCELLATIONREQUESTEntry.TABLENAME;
    final String code =
      CANCELLATIONREQUESTEntry.get(cancellationCode).getCode();
    cancellationReason =
      getCodeTableDescriptionForUserLocale(tableName, code);

    return cancellationReason;
  }

  public static String getCodeTableDescriptionForUserLocale(
    final String tableName, final String code) {

    String codeTableItemDescription = new String();

    // try get user locale
    try {
      codeTableItemDescription =
        CodeTable.getOneItemForUserLocale(tableName, code);
    } catch (AppRuntimeException | AppException | InformationalException e) {

      Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
        + "Couldn't find the User Locale description for codetable: "
        + tableName + " code: " + code
        + ". Providing default description, if found");

      // try get the default version instead
      try {
        codeTableItemDescription = CodeTable.getOneItem(tableName, code);
      } catch (AppRuntimeException | AppException
        | InformationalException e1) {

        Trace.kTopLevelLogger.warn(BDMConstants.BDM_LOGS_PREFIX
          + "Couldn't find the default Locale description for codetable: "
          + tableName + " code: " + code + ". Providing empty String");
      }
    }
    return codeTableItemDescription;
  }

  public BDMUAPaymentList
    listPayments(final BDMUAPaymentSearchKey paymentSearchKey)
      throws AppException, InformationalException {

    this.citizenAccountSecurity.performDefaultSecurityChecks();
    final BDMUAPaymentList payments = new BDMUAPaymentList();
    if (paymentSearchKey == null || paymentSearchKey.payment_id == 0L) {
      final CitizenPaymentInstDetailsList list =
        this.citizenAccountFacade.listCitizenPayments();
      for (final CitizenPaymentInstrumentDetails record : list.pmtInstrumentDtls) {
        final BDMPaymentInstrument bdmPaymentInstrumentObj =
          BDMPaymentInstrumentFactory.newInstance();
        final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
          new BDMPaymentInstrumentKey();

        final BDMUAPayment bdmUAPayment = new BDMUAPayment();
        bdmUAPayment.uaPayment.payedByName = record.agency;
        if (record.isExternalPayment) {
          bdmPaymentInstrumentKey.pmtInstrumentID =
            record.pmtInstructionIndDtls.externalPaymentID;
          final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
            bdmPaymentInstrumentObj.read(bdmPaymentInstrumentKey);

          // Only Work with payments that have a SPS response
          if (!bdmPaymentInstrumentDtls.spsPmtRefNo.isEmpty()) {
            populateExternalPayment(bdmUAPayment,
              record.pmtInstructionIndDtls.externalPaymentID);
            bdmUAPayment.bdmStatus =
              bdmPaymentInstrumentDtls.reconcilStatusCode;
            payments.data.add(bdmUAPayment);

          }
        } else {
          bdmPaymentInstrumentKey.pmtInstrumentID =
            record.participantDtls.pmtInstrumentID;
          final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
            bdmPaymentInstrumentObj.read(bdmPaymentInstrumentKey);

          // Only Work with payments that have a SPS response
          if (!bdmPaymentInstrumentDtls.spsPmtRefNo.isEmpty()) {
            populatePaymentInstrument(bdmUAPayment,
              record.participantDtls.pmtInstrumentID);
            bdmUAPayment.bdmStatus =
              bdmPaymentInstrumentDtls.reconcilStatusCode;
            payments.data.add(bdmUAPayment);

          }
        }

      }
    } else {
      final BDMUAPayment bdmUAPayment = new BDMUAPayment();
      try {
        final BDMPaymentInstrument bdmPaymentInstrumentObj =
          BDMPaymentInstrumentFactory.newInstance();
        final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
          new BDMPaymentInstrumentKey();
        if (paymentSearchKey.external) {
          bdmPaymentInstrumentKey.pmtInstrumentID =
            paymentSearchKey.payment_id;
          final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
            bdmPaymentInstrumentObj.read(bdmPaymentInstrumentKey);

          // Only Work with payments that have a SPS response
          if (!bdmPaymentInstrumentDtls.spsPmtRefNo.isEmpty()) {
            populateExternalPayment(bdmUAPayment,
              paymentSearchKey.payment_id);
            bdmUAPayment.bdmStatus =
              bdmPaymentInstrumentDtls.reconcilStatusCode;
            payments.data.add(bdmUAPayment);

          }
        } else {
          bdmPaymentInstrumentKey.pmtInstrumentID =
            paymentSearchKey.payment_id;
          final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
            bdmPaymentInstrumentObj.read(bdmPaymentInstrumentKey);

          // Only Work with payments that have a SPS response
          if (!bdmPaymentInstrumentDtls.spsPmtRefNo.isEmpty()) {
            populatePaymentInstrument(bdmUAPayment,
              paymentSearchKey.payment_id);
            bdmUAPayment.bdmStatus =
              bdmPaymentInstrumentDtls.reconcilStatusCode;
            payments.data.add(bdmUAPayment);

          }
        }
      } catch (final RecordNotFoundException e) {
        throw RESTAPIERRORMESSAGESExceptionCreator
          .HTTP_404_ERROR_PAYMENT_RECORD_NOT_FOUND(
            Long.valueOf(paymentSearchKey.payment_id));
      }

    }
    Collections.sort((List<BDMUAPayment>) payments.data,
      new UAPaymentsComparator());
    return payments;
  }

  private void populatePaymentInstrument(final BDMUAPayment payment,
    final long paymentInstrumentId)
    throws AppException, InformationalException {

    final PaymentInstrumentKey paymentInstrumentKey =
      new PaymentInstrumentKey();
    paymentInstrumentKey.pmtInstrumentID = paymentInstrumentId;
    final PaymentInstrumentDtls paymentInstrument =
      this.paymentInstrumentFacade.read(paymentInstrumentKey);
    final CitizenPaymentInstructionDetails paymentInstruction =
      this.citizenAccountFacade
        .readPaymentInstructionByInstrumentID(paymentInstrumentKey);

    payment.uaPayment.payment_id = paymentInstrument.pmtInstrumentID;
    payment.uaPayment.paymentDate = paymentInstrument.effectiveDate;
    payment.uaPayment.amount = paymentInstrument.amount;
    payment.uaPayment.method = paymentInstrument.deliveryMethodType;
    payment.uaPayment.status = paymentInstrument.reconcilStatusCode;
    payment.uaPayment.currency = paymentInstrument.currencyTypeCode;
    payment.uaPayment.dueDate = paymentInstruction.dueDate;
    payment.uaPayment.nominatedPayeeName = paymentInstruction.nomineeName;
    payment.uaPayment.nominatedPayeeAddress =
      paymentInstruction.nomineeAddress;
    payment.checkNumber = paymentInstruction.chequeNumber;
    payment.uaPayment.bankSortCode = paymentInstruction.bankAccountSortCode;
    payment.uaPayment.bankAccountNumber =
      paymentInstruction.bankAccountNumber;
    payment.uaPayment.voucherNumber = paymentInstruction.voucherNumber;

    // Begin - PN - Update statusOther to account for requested processed and
    // cancelled

    final PaymentInstruction paymentInstructionObj =
      PaymentInstructionFactory.newInstance();
    final PmtInstrumentID pmtInsturmentID = new PmtInstrumentID();
    pmtInsturmentID.pmtInstrumentID = paymentInstrumentId;

    final FinInstructionID finInstructionID =
      paymentInstructionObj.readByPmtInstrumentID(pmtInsturmentID);

    final FinancialInstruction financialInstructionObj =
      FinancialInstructionFactory.newInstance();
    final FinancialInstructionKey bdmFinancialInstructionKey =
      new FinancialInstructionKey();
    bdmFinancialInstructionKey.finInstructionID =
      finInstructionID.finInstructionID;

    final FinancialInstructionDtls financialInstructionDtls =
      financialInstructionObj.read(bdmFinancialInstructionKey);

    if (financialInstructionDtls.statusCode
      .equals(FININSTRUCTIONSTATUSEntry.CANCELLED.getCode())) {
      payment.uaPayment.statusOther =
        FININSTRUCTIONSTATUSEntry.CANCELLED.getCode();
    } else if (financialInstructionDtls.statusCode
      .equals(FININSTRUCTIONSTATUSEntry.ISSUED.getCode())) {

      payment.uaPayment.statusOther =
        FININSTRUCTIONSTATUSEntry.PROCESSED.getCode();
    } else {
      payment.uaPayment.statusOther = paymentInstruction.statusCode;
    }

    // End - PN - Update statusOther to account for requested processed and
    // cancelled

    final PmtInstrumentID paymentInstrumentID = new PmtInstrumentID();
    paymentInstrumentID.pmtInstrumentID = paymentInstrument.pmtInstrumentID;
    final FinInstructionID financialInstructionID =
      this.paymentInstructionFacade
        .readByPmtInstrumentID(paymentInstrumentID);
    final PaymentDateDetails paymentDateDetails =
      this.financialInstructionFacade
        .readPaymentDateDetailsByInstructionID(financialInstructionID);
    payment.uaPayment.coverStartDate = paymentDateDetails.coverPeriodFrom;
    payment.uaPayment.coverEndDate = paymentDateDetails.coverPeriodTo;
    final ILIFinInstructID instructionLineItemFinancialInstructionID =
      new ILIFinInstructID();
    instructionLineItemFinancialInstructionID.finInstructionID =
      financialInstructionID.finInstructionID;
    final InstructionLineItemDtlsList instructionLineItems =
      this.instructionLineItemFacade
        .searchByFinInstructID(instructionLineItemFinancialInstructionID);
    for (final InstructionLineItemDtls instructionLineItem : instructionLineItems.dtls) {
      final BDMUAPaymentDetail paymentDetail = new BDMUAPaymentDetail();
      paymentDetail.caseId = instructionLineItem.caseID;
      paymentDetail.coverStartDate = instructionLineItem.coverPeriodFrom;
      paymentDetail.coverEndDate = instructionLineItem.coverPeriodTo;
      paymentDetail.entitlement = instructionLineItem.instructionLineItemType;
      if (CREDITDEBITEntry.DEBIT.getCode()
        .equalsIgnoreCase(instructionLineItem.creditDebitType)) {
        paymentDetail.creditAmount = instructionLineItem.amount;
      } else {
        paymentDetail.debitAmount = instructionLineItem.amount;
        // Begin - PN - Use financialCompID to grab deduction name

        paymentDetail.deductionName = getDeductionName(instructionLineItem);

        // End - PN - Use financialCompID to grab deduction name
      }
      final CaseIDDetails key = new CaseIDDetails();
      key.caseID = instructionLineItem.caseID;
      final CaseHeaderDetails caseHeaderDetails =
        this.caseHeaderFacade.readCaseHeader(key);
      paymentDetail.caseReference = caseHeaderDetails.dtls.caseReference;
      final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
      productDeliveryKey.caseID = instructionLineItem.caseID;
      paymentDetail.benefitName =
        CodeTable.getOneItem("ProductType", ProductDeliveryFactory
          .newInstance().readProductType(productDeliveryKey).productType);
      payment.paymentDetails.add(paymentDetail);
    }
  }

  /*
   * Gather and Format payment Deduction Name
   */

  public String
    getDeductionName(final InstructionLineItemDtls instructionLineItemDtls)
      throws AppException, InformationalException {

    String deductionNameString = new String();
    final CaseDeductionItem caseDeductionItemObj =
      CaseDeductionItemFactory.newInstance();
    final CaseDeductionItemFinCompID caseDeductionItemFinCompID =
      new CaseDeductionItemFinCompID();
    caseDeductionItemFinCompID.financialCompID =
      instructionLineItemDtls.financialCompID;
    final DeductionName deductionCode = caseDeductionItemObj
      .readNameByFinancialCompID(caseDeductionItemFinCompID);

    final String tableName = DEDUCTIONNAMEEntry.TABLENAME;

    deductionNameString = getCodeTableDescriptionForUserLocale(tableName,
      DEDUCTIONNAMEEntry.get(deductionCode.deductionName).getCode());
    deductionNameString = " - " + deductionNameString;

    return deductionNameString;
  }

  private void populateExternalPayment(final BDMUAPayment payment,
    final long externalPaymentId)
    throws AppException, InformationalException {

    final ExternalPaymentKey externalPaymentKey = new ExternalPaymentKey();
    externalPaymentKey.externalPaymentID = externalPaymentId;
    final ExternalPaymentDtls externalPayment =
      this.externalPaymentFacade.read(externalPaymentKey);
    payment.uaPayment.amount = externalPayment.amount;
    payment.uaPayment.coverStartDate = externalPayment.coverPeriodStartDate;
    payment.uaPayment.coverEndDate = externalPayment.coverPeriodEndDate;
    payment.uaPayment.currency = externalPayment.currency;
    payment.uaPayment.dueDate = externalPayment.dueDate;
    payment.uaPayment.method = externalPayment.paymentMethod;
    payment.uaPayment.nominatedPayeeAddress = externalPayment.address;
    payment.uaPayment.nominatedPayeeName = externalPayment.payeeName;
    payment.uaPayment.payment_id = externalPayment.externalPaymentID;
    payment.uaPayment.status = externalPayment.status;
    payment.uaPayment.paymentDate = externalPayment.effectiveDate;
    payment.uaPayment.external = true;
    if (METHODOFDELIVERYEntry.CHEQUE.getCode()
      .equalsIgnoreCase(externalPayment.paymentMethod))
      payment.checkNumber = externalPayment.referenceNumber;
    if (METHODOFDELIVERYEntry.EFT.getCode()
      .equalsIgnoreCase(externalPayment.paymentMethod)) {
      payment.uaPayment.bankAccountNumber = externalPayment.bankAccountNumber;
      payment.uaPayment.bankSortCode = externalPayment.bankSortCode;
    }
    final ReadPaymentDetailsKey pmtDetailsKey = new ReadPaymentDetailsKey();
    pmtDetailsKey.externalPaymentID = externalPaymentId;
    pmtDetailsKey.isExternalPayment = true;
    final CitizenPaymentInstructionDetails paymentDetails =
      this.citizenAccountFacade.readPaymentInstructionDetails(pmtDetailsKey);
    payment.uaPayment.statusOther = paymentDetails.statusCode;
    final ExternalPaymentBreakdownDtlsList results =
      ExternalPaymentBreakdownFactory.newInstance()
        .searchByPayment(externalPaymentKey);
    for (final ExternalPaymentBreakdownDtls detailsRow : results.dtls) {
      final BDMUAPaymentDetail paymentDetail = new BDMUAPaymentDetail();
      paymentDetail.benefitName = detailsRow.caseName;
      paymentDetail.coverStartDate = detailsRow.coverPeriodStartDate;
      paymentDetail.coverEndDate = detailsRow.coverPeriodEndDate;
      paymentDetail.creditAmount = detailsRow.creditAmount;
      paymentDetail.debitAmount = detailsRow.debitAmount;
      paymentDetail.entitlement = detailsRow.component;
      payment.paymentDetails.add(paymentDetail);
    }
  }

  protected static class UAPaymentsComparator
    implements Comparator<BDMUAPayment> {

    @Override
    public int compare(final BDMUAPayment o1, final BDMUAPayment o2) {

      return o1.uaPayment.paymentDate.compareTo(o2.uaPayment.paymentDate)
        * -1;
    }
  }

  @Override
  public BDMUAViewTaxSlipList listTaxSlips(final ConcernRoleKey key)
    throws AppException, InformationalException {

    final int showLastXYears = Configuration
      .getIntProperty(EnvVars.BDM_ENV_BDMPAYMENTAPI_TAXSLIPS_XYEARS);
    final BDMTaxSlipDataT4A bdmTaxSlipDataT4AObj =
      BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipsForDisplayKey bdmTaxSlipsForDisplayKey =
      new BDMTaxSlipsForDisplayKey();
    bdmTaxSlipsForDisplayKey.concernRoleID = key.concernRoleID;
    bdmTaxSlipsForDisplayKey.taxSlipStatusDeleted = BDMTAXSLIPSTATUS.DELETED;
    final BDMViewTaxSlipDetailsList t4AList =
      bdmTaxSlipDataT4AObj.searchTaxSlipsForDisplay(bdmTaxSlipsForDisplayKey);

    // get the year that tax slips should not show anymore
    final int cutoffYear =
      Date.getCurrentDate().getCalendar().get(Calendar.YEAR) - showLastXYears;

    final BDMUAViewTaxSlipList bdmUAViewTaxSlipList =
      new BDMUAViewTaxSlipList();

    for (final BDMViewTaxSlipDetails bdmViewTaxSlipDetails : t4AList.dtls) {
      // only want issued tax slips and tax slips within the last 7 years
      if (!BDMTAXSLIPSTATUS.ISSUED
        .equals(bdmViewTaxSlipDetails.taxSlipStatusCode)
        || bdmViewTaxSlipDetails.taxYear <= cutoffYear) {
        continue;
      }
      final BDMUAViewTaxSlipDetails taxSlipDetails =
        new BDMUAViewTaxSlipDetails();
      taxSlipDetails.taxSlipDataID = bdmViewTaxSlipDetails.taxSlipDataID;
      taxSlipDetails.taxYear = bdmViewTaxSlipDetails.taxYear;
      taxSlipDetails.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
      taxSlipDetails.taxSlipStatusCode =
        bdmViewTaxSlipDetails.taxSlipStatusCode;
      taxSlipDetails.duplicateInd = bdmViewTaxSlipDetails.duplicateInd;
      if (BDMTAXSLIPTYPE.CANCELLED
        .equals(bdmViewTaxSlipDetails.slipTypeCode)) {
        taxSlipDetails.cancelledTypeInd = true;
      }
      taxSlipDetails.programType = BDMBENEFITPROGRAMTYPE.getDefaultCode();
      bdmUAViewTaxSlipList.dtls.add(taxSlipDetails);

    }

    final BDMTaxSlipDataRL1 bdmTaxSlipDataRL1Obj =
      BDMTaxSlipDataRL1Factory.newInstance();
    final BDMViewTaxSlipDetailsList rL1List =
      bdmTaxSlipDataRL1Obj.searchTaxSlipsForDisplay(bdmTaxSlipsForDisplayKey);

    for (final BDMViewTaxSlipDetails bdmViewTaxSlipDetails : rL1List.dtls) {
      // only want issued tax slips and tax slips within the last 7 years
      if (!BDMTAXSLIPSTATUS.ISSUED
        .equals(bdmViewTaxSlipDetails.taxSlipStatusCode)
        || bdmViewTaxSlipDetails.taxYear <= cutoffYear) {
        continue;
      }
      final BDMUAViewTaxSlipDetails taxSlipDetails =
        new BDMUAViewTaxSlipDetails();
      taxSlipDetails.taxSlipDataID = bdmViewTaxSlipDetails.taxSlipDataID;
      taxSlipDetails.taxYear = bdmViewTaxSlipDetails.taxYear;
      taxSlipDetails.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
      taxSlipDetails.taxSlipStatusCode =
        bdmViewTaxSlipDetails.taxSlipStatusCode;
      taxSlipDetails.duplicateInd = bdmViewTaxSlipDetails.duplicateInd;
      if (BDMTAXSLIPTYPE.CANCELLED
        .equals(bdmViewTaxSlipDetails.slipTypeCode)) {
        taxSlipDetails.cancelledTypeInd = true;
      }

      taxSlipDetails.programType = BDMBENEFITPROGRAMTYPE.getDefaultCode();
      bdmUAViewTaxSlipList.dtls.add(taxSlipDetails);
    }

    Collections.sort(bdmUAViewTaxSlipList.dtls,
      new Comparator<BDMUAViewTaxSlipDetails>() {

        @Override
        public int compare(final BDMUAViewTaxSlipDetails o1,
          final BDMUAViewTaxSlipDetails o2) {

          if (o1.taxYear == o2.taxYear) {
            return 0;
          } else if (o1.taxYear > o2.taxYear) {
            return 1;
          } else {
            return -1;
          }
        }
      });

    return bdmUAViewTaxSlipList;
  }

  @Override
  public BDMUAViewTaxSlipInlineDetails
    viewTaxSlipDetails(final BDMUATaxSlipDataKey key)
      throws AppException, InformationalException {

    final BDMManageTaxSlips bdmManageTaxSlipsObj =
      new curam.ca.gc.bdm.sl.financial.managetaxslips.impl.BDMManageTaxSlips();
    final BDMTaxSlipDataKey bdmTaxSlipDataKey = new BDMTaxSlipDataKey();
    bdmTaxSlipDataKey.taxSlipDataID = key.taxSlipDataID;
    final BDMViewTaxSlipInlineDetails slDetails =
      bdmManageTaxSlipsObj.viewTaxSlipInline(bdmTaxSlipDataKey);

    final BDMUAViewTaxSlipInlineDetails details =
      new BDMUAViewTaxSlipInlineDetails();
    details.assign(slDetails);

    final String locale = TransactionInfo.getProgramLocale();
    int index = 0;
    // Grab text from Text ID
    for (final BDMUATaxSlipAttrDetails bdmUATaxSlipAttrDetails : details.dtls) {
      final TextTranslation textTranslation =
        TextTranslationFactory.newInstance();
      final ReadByLocalizableTextIDandLocaleKey readByLocalizableTextIDandLocaleKey =
        new ReadByLocalizableTextIDandLocaleKey();

      readByLocalizableTextIDandLocaleKey.localizableTextID =
        bdmUATaxSlipAttrDetails.nameTextID;
      readByLocalizableTextIDandLocaleKey.localeCode = locale;
      final TextTranslationDtls textTranslationDtls =
        textTranslation.readByLocalizableTextIDandLocale(
          readByLocalizableTextIDandLocaleKey);
      details.dtls.get(index).nameText = textTranslationDtls.text;
      index++;
    }

    if (BDMTAXSLIPFORMTYPE.T4A.equals(slDetails.formTypeCode)) {
      final BDMTaxSlipDataT4A bdmTaxSlipDataT4AObj =
        BDMTaxSlipDataT4AFactory.newInstance();
      final BDMTaxSlipDataT4AKey bdmTaxSlipDataT4AKey =
        new BDMTaxSlipDataT4AKey();
      bdmTaxSlipDataT4AKey.taxSlipDataID = key.taxSlipDataID;
      final BDMTaxSlipDataT4ADtls t4ADtls =
        bdmTaxSlipDataT4AObj.read(bdmTaxSlipDataT4AKey);
      details.taxYear = t4ADtls.taxYear;
      details.slipTypeCode = t4ADtls.slipTypeCode;
      details.taxSlipStatusCode = t4ADtls.taxSlipStatusCode;
      details.duplicateInd = t4ADtls.duplicateInd;
      if (BDMTAXSLIPTYPE.CANCELLED.equals(t4ADtls.slipTypeCode)) {
        details.cancelledTypeInd = true;
      }
    }

    if (BDMTAXSLIPFORMTYPE.RL1.equals(slDetails.formTypeCode)) {
      final BDMTaxSlipDataRL1 bdmTaxSlipDataRL1Obj =
        BDMTaxSlipDataRL1Factory.newInstance();
      final BDMTaxSlipDataRL1Key bdmTaxSlipDataRL1Key =
        new BDMTaxSlipDataRL1Key();
      bdmTaxSlipDataRL1Key.taxSlipDataID = key.taxSlipDataID;
      final BDMTaxSlipDataRL1Dtls rL1Dtls =
        bdmTaxSlipDataRL1Obj.read(bdmTaxSlipDataRL1Key);
      details.taxYear = rL1Dtls.taxYear;
      details.slipTypeCode = rL1Dtls.slipTypeCode;
      details.taxSlipStatusCode = rL1Dtls.taxSlipStatusCode;
      details.duplicateInd = rL1Dtls.duplicateInd;

      if (BDMTAXSLIPTYPE.CANCELLED.equals(rL1Dtls.slipTypeCode)) {
        details.cancelledTypeInd = true;
      }
    }
    details.programType = BDMBENEFITPROGRAMTYPE.getDefaultCode();
    return details;
  }

  @Override
  public BDMUATaxSlipPDFData getTaxSlipPDF(final BDMUATaxSlipDataKey key)
    throws AppException, InformationalException {

    BDMUATaxSlipPDFData uaTaxSlipData = new BDMUATaxSlipPDFData();
    final BDMManageTaxSlips bdmManageTaxSlipsObj =
      new curam.ca.gc.bdm.sl.financial.managetaxslips.impl.BDMManageTaxSlips();
    final BDMTaxSlipDataKey bdmTaxSlipDataKey = new BDMTaxSlipDataKey();
    bdmTaxSlipDataKey.taxSlipDataID = key.taxSlipDataID;
    final BDMViewTaxSlipInlineDetails slDetails =
      bdmManageTaxSlipsObj.viewTaxSlipInline(bdmTaxSlipDataKey);

    if (BDMTAXSLIPFORMTYPE.T4A.equals(slDetails.formTypeCode)) {
      uaTaxSlipData = generateTaxSlipPDFImpl.getT4APDF(key);
    } else if (BDMTAXSLIPFORMTYPE.RL1.equals(slDetails.formTypeCode)) {
      uaTaxSlipData = generateTaxSlipPDFImpl.getRL1PDF(key);
    }
    uaTaxSlipData.contentType = BDMConstants.kApplicationPDF;

    return uaTaxSlipData;
  }

}
