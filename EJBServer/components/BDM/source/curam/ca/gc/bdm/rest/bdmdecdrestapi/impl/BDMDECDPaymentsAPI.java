package curam.ca.gc.bdm.rest.bdmdecdrestapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkDetails;
import curam.ca.gc.bdm.rest.bdmdecdrestapi.struct.BDMDECDGuidKey;
import curam.citizenaccount.externalservices.fact.ExternalPaymentBreakdownFactory;
import curam.citizenaccount.externalservices.fact.ExternalPaymentFactory;
import curam.citizenaccount.externalservices.impl.ExternalPaymentHelper;
import curam.citizenaccount.externalservices.struct.ExternalPaymentBreakdownDtls;
import curam.citizenaccount.externalservices.struct.ExternalPaymentBreakdownDtlsList;
import curam.citizenaccount.externalservices.struct.ExternalPaymentDtls;
import curam.citizenaccount.externalservices.struct.ExternalPaymentKey;
import curam.citizenaccount.facade.fact.CitizenAccountFactory;
import curam.citizenaccount.facade.intf.CitizenAccount;
import curam.citizenaccount.facade.struct.CitizenPaymentInstDetailsList;
import curam.citizenaccount.facade.struct.CitizenPaymentInstructionDetails;
import curam.citizenaccount.facade.struct.CitizenPaymentInstrumentDetails;
import curam.citizenaccount.facade.struct.PaymentInstructionMultiIndDetails;
import curam.citizenaccount.facade.struct.ReadPaymentDetailsKey;
import curam.citizenaccount.impl.CitizenPayments;
import curam.citizenworkspace.rest.facade.struct.UAPayment;
import curam.citizenworkspace.rest.facade.struct.UAPaymentDetail;
import curam.citizenworkspace.rest.facade.struct.UAPaymentList;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountInfo;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.PRODUCTTYPE;
import curam.codetable.impl.CREDITDEBITEntry;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.codetable.impl.PMTRECONCILIATIONSTATUSEntry;
import curam.core.facade.fact.CaseHeaderFactory;
import curam.core.facade.fact.OrganizationFactory;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.intf.Organization;
import curam.core.facade.struct.CaseHeaderDetails;
import curam.core.fact.FinancialInstructionFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.fact.PaymentInstructionFactory;
import curam.core.fact.PaymentInstrumentFactory;
import curam.core.struct.FinInstructionID;
import curam.core.struct.ILIFinInstructID;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.InstructionLineItemDtlsList;
import curam.core.struct.PIParticipantDetails;
import curam.core.struct.PISearchResultDtls;
import curam.core.struct.PISearchResultDtlsList;
import curam.core.struct.PaymentDateDetails;
import curam.core.struct.PaymentInstrumentDtls;
import curam.core.struct.PaymentInstrumentKey;
import curam.core.struct.PmtInstrumentID;
import curam.core.struct.ProductDeliveryKey;
import curam.piwrapper.financialmanager.impl.PaymentInstruction;
import curam.piwrapper.financialmanager.impl.PaymentInstructionDAO;
import curam.piwrapper.financialmanager.impl.PaymentInstrument;
import curam.piwrapper.financialmanager.impl.PaymentInstrumentDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.type.CodeTable;
import curam.workspaceservices.intake.impl.ProgramTypeDAO;
import java.util.Iterator;
import java.util.List;

/**
 *
 * REST API to retrieve the payment when accessed from Digital Channel (DECD)
 *
 */
public class BDMDECDPaymentsAPI
  extends curam.ca.gc.bdm.rest.bdmdecdrestapi.base.BDMDECDPaymentsAPI {

  @Inject
  ProgramTypeDAO programTypeDAO;

  @Inject
  private CitizenPayments paymentsHelper;

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private PaymentInstrumentDAO paymentInstrumentDAO;

  @Inject
  private PaymentInstructionDAO paymentInstructionDAO;

  @Inject
  private ExternalPaymentHelper externalPaymentHelper;

  final CitizenAccount citizenAccountFacade =
    CitizenAccountFactory.newInstance();

  final curam.core.intf.PaymentInstrument paymentInstrumentFacade =
    PaymentInstrumentFactory.newInstance();

  final curam.core.intf.PaymentInstruction paymentInstructionFacade =
    PaymentInstructionFactory.newInstance();

  final Organization organizationFacade = OrganizationFactory.newInstance();

  public BDMDECDPaymentsAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public UAPaymentList listPayments(final BDMDECDGuidKey guidKey)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(" Inside List Payment API");

    final BDMUsernameGuidLinkDetails guidUserNameDetail =
      new BDMDECDApplicationAPI().getUAUserNameByGuid(guidKey);

    Trace.kTopLevelLogger.info("Username : " + guidUserNameDetail.username);
    final UAPaymentList payments = new UAPaymentList();
    final CitizenPaymentInstDetailsList list =
      listCitizenPayments(guidUserNameDetail.username);

    for (final CitizenPaymentInstrumentDetails record : list.pmtInstrumentDtls) {
      final UAPayment payment = new UAPayment();
      payment.payedByName = record.agency;
      if (record.isExternalPayment) {
        this.populateExternalPayment(payment,
          record.pmtInstructionIndDtls.externalPaymentID);
      } else {
        this.populatePaymentInstrument(payment,
          record.participantDtls.pmtInstrumentID);
      }
      payments.data.add(payment);
    }

    Trace.kTopLevelLogger.info(" Payment API ends ");

    return payments;
  }

  /**
   *
   * @param userName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public CitizenPaymentInstDetailsList listCitizenPayments(
    final String userName) throws AppException, InformationalException {

    final CitizenWorkspaceAccountInfo cwAccountInfo =
      this.citizenWorkspaceAccountManager.readAccountBy(userName);

    /*
     * Trace.kTopLevelLogger.info("Person concernrole name  : "
     * + cwAccountInfo.getConcernRole().getName());
     */

    final CitizenPaymentInstDetailsList citizenPaymentInstDetailsList =
      new CitizenPaymentInstDetailsList();
    if (cwAccountInfo.isLinkedToInternalCuramSystem()) {
      final PISearchResultDtlsList piSearchResultDtlsList =
        this.paymentInstrumentDAO
          .searchSpecificPaymentInstrument(cwAccountInfo.getConcernRole());
      final Iterator iterator = piSearchResultDtlsList.dtls.iterator();

      while (iterator.hasNext()) {
        final PISearchResultDtls piSearchResultDtls =
          (PISearchResultDtls) iterator.next();
        final CitizenPaymentInstrumentDetails citizenPaymentInstrumentDetails =
          new CitizenPaymentInstrumentDetails();
        if (!piSearchResultDtls.reconcilStatusCode
          .equals(PMTRECONCILIATIONSTATUSEntry.SUSPENDED)) {
          citizenPaymentInstrumentDetails.participantDtls =
            this.assignParticipantPaymentsDetails(piSearchResultDtls);
          citizenPaymentInstrumentDetails.pmtInstructionIndDtls =
            this.getPaymentInstructionIndDetails(piSearchResultDtls);
          Trace.kTopLevelLogger.info("pmtInstructionID  : "
            + citizenPaymentInstrumentDetails.pmtInstructionIndDtls.pmtInstructionID);

          citizenPaymentInstDetailsList.pmtInstrumentDtls
            .add(citizenPaymentInstrumentDetails);
          citizenPaymentInstrumentDetails.agency = this.getAgencyName();
        }
      }
    }

    citizenPaymentInstDetailsList.pmtInstrumentDtls
      .addAll(this.externalPaymentHelper
        .listExternalPayments(cwAccountInfo).pmtInstrumentDtls);

    Trace.kTopLevelLogger.info("Number of payments found  : "
      + citizenPaymentInstDetailsList.pmtInstrumentDtls.size());

    return citizenPaymentInstDetailsList;
  }

  /**
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getAgencyName() throws AppException, InformationalException {

    final Organization organizationObj = OrganizationFactory.newInstance();
    return organizationObj
      .readOrganizationHomePage1().organisationDetails.name;
  }

  /**
   *
   * @param piSearchResultDtls
   * @return
   */
  protected PIParticipantDetails assignParticipantPaymentsDetails(
    final PISearchResultDtls piSearchResultDtls) {

    if (null == piSearchResultDtls) {
      return null;
    } else {
      final PIParticipantDetails participantPaymentDetails =
        new PIParticipantDetails();
      participantPaymentDetails.amount = piSearchResultDtls.amount;
      participantPaymentDetails.deliveryMethodType =
        piSearchResultDtls.deliveryMethodType;
      participantPaymentDetails.effectiveDate =
        piSearchResultDtls.effectiveDate;
      participantPaymentDetails.pmtInstrumentID =
        piSearchResultDtls.pmtInstrumentID;
      participantPaymentDetails.reconcilStatusCode =
        piSearchResultDtls.reconcilStatusCode;
      return participantPaymentDetails;
    }
  }

  /**
   *
   * @param piSearchResultDtls
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected PaymentInstructionMultiIndDetails getPaymentInstructionIndDetails(
    final PISearchResultDtls piSearchResultDtls)
    throws AppException, InformationalException {

    final PaymentInstructionMultiIndDetails pmtInstructionIndDtls =
      new PaymentInstructionMultiIndDetails();
    final PaymentInstrument paymentInstrument =
      this.paymentInstrumentDAO.get(piSearchResultDtls.pmtInstrumentID);
    final List<PaymentInstruction> paymentInstructionList =
      this.paymentInstructionDAO.searchByPaymentInstrument(paymentInstrument);
    if (paymentInstructionList.size() > 1) {
      pmtInstructionIndDtls.multiInstructionInd = true;
    } else if (paymentInstructionList.size() == 1) {
      pmtInstructionIndDtls.pmtInstructionID =
        paymentInstructionList.get(0).getID();
    }

    return pmtInstructionIndDtls;
  }

  /**
   *
   * @param payment
   * @param paymentInstrumentId
   * @throws AppException
   * @throws InformationalException
   */
  private void populatePaymentInstrument(final UAPayment payment,
    final long paymentInstrumentId)
    throws AppException, InformationalException {

    final PaymentInstrumentKey paymentInstrumentKey =
      new PaymentInstrumentKey();
    paymentInstrumentKey.pmtInstrumentID = paymentInstrumentId;
    final PaymentInstrumentDtls paymentInstrument =
      this.paymentInstrumentFacade.read(paymentInstrumentKey);
    final CitizenPaymentInstructionDetails paymentInstruction =
      this.paymentsHelper
        .readPaymentInstructionByInstrument(paymentInstrumentKey);

    Trace.kTopLevelLogger.info("Payment details   : pmtInstrumentID ="
      + paymentInstrument.pmtInstrumentID + " , Amount = "
      + paymentInstrument.amount + " , Method = "
      + paymentInstrument.deliveryMethodType + ", Currency = "
      + paymentInstrument.currencyTypeCode);

    payment.payment_id = paymentInstrument.pmtInstrumentID;
    payment.paymentDate = paymentInstrument.effectiveDate;
    payment.amount = paymentInstrument.amount;
    payment.method = paymentInstrument.deliveryMethodType;
    payment.status = paymentInstrument.reconcilStatusCode;
    payment.currency = paymentInstrument.currencyTypeCode;
    payment.statusOther = paymentInstruction.statusCode;
    payment.dueDate = paymentInstruction.dueDate;
    payment.nominatedPayeeName = paymentInstruction.nomineeName;
    payment.nominatedPayeeAddress = paymentInstruction.nomineeAddress;
    payment.checkNumber = paymentInstruction.chequeNumber;
    payment.bankSortCode = paymentInstruction.bankAccountSortCode;
    payment.bankAccountNumber = paymentInstruction.bankAccountNumber;
    payment.voucherNumber = paymentInstruction.voucherNumber;

    final PmtInstrumentID paymentInstrumentID = new PmtInstrumentID();
    paymentInstrumentID.pmtInstrumentID = paymentInstrument.pmtInstrumentID;
    final FinInstructionID financialInstructionID =
      this.paymentInstructionFacade
        .readByPmtInstrumentID(paymentInstrumentID);
    final PaymentDateDetails paymentDateDetails =
      FinancialInstructionFactory.newInstance()
        .readPaymentDateDetailsByInstructionID(financialInstructionID);
    payment.coverStartDate = paymentDateDetails.coverPeriodFrom;
    payment.coverEndDate = paymentDateDetails.coverPeriodTo;
    final ILIFinInstructID instructionLineItemFinancialInstructionID =
      new ILIFinInstructID();
    instructionLineItemFinancialInstructionID.finInstructionID =
      financialInstructionID.finInstructionID;
    final InstructionLineItemDtlsList instructionLineItems =
      InstructionLineItemFactory.newInstance()
        .searchByFinInstructID(instructionLineItemFinancialInstructionID);
    final Iterator var13 = instructionLineItems.dtls.iterator();

    while (var13.hasNext()) {
      final InstructionLineItemDtls instructionLineItem =
        (InstructionLineItemDtls) var13.next();
      final UAPaymentDetail paymentDetail = new UAPaymentDetail();
      paymentDetail.caseId = instructionLineItem.caseID;
      paymentDetail.coverStartDate = instructionLineItem.coverPeriodFrom;
      paymentDetail.coverEndDate = instructionLineItem.coverPeriodTo;
      paymentDetail.entitlement = instructionLineItem.instructionLineItemType;
      if (CREDITDEBITEntry.DEBIT.getCode()
        .equalsIgnoreCase(instructionLineItem.creditDebitType)) {
        paymentDetail.creditAmount = instructionLineItem.amount;
      } else {
        paymentDetail.debitAmount = instructionLineItem.amount;
      }

      final curam.core.facade.struct.CaseIDDetails key =
        new curam.core.facade.struct.CaseIDDetails();
      key.caseID = instructionLineItem.caseID;
      final CaseHeaderDetails caseHeaderDetails =
        CaseHeaderFactory.newInstance().readCaseHeader(key);
      paymentDetail.caseReference = caseHeaderDetails.dtls.caseReference;
      final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
      productDeliveryKey.caseID = instructionLineItem.caseID;
      paymentDetail.benefitName =
        CodeTable.getOneItem(PRODUCTTYPE.TABLENAME, ProductDeliveryFactory
          .newInstance().readProductType(productDeliveryKey).productType);
      payment.paymentDetails.add(paymentDetail);
    }

  }

  /**
   *
   * @param payment
   * @param externalPaymentId
   * @throws AppException
   * @throws InformationalException
   */
  private void populateExternalPayment(final UAPayment payment,
    final long externalPaymentId)
    throws AppException, InformationalException {

    final ExternalPaymentKey externalPaymentKey = new ExternalPaymentKey();
    externalPaymentKey.externalPaymentID = externalPaymentId;
    final ExternalPaymentDtls externalPayment =
      ExternalPaymentFactory.newInstance().read(externalPaymentKey);
    payment.amount = externalPayment.amount;
    payment.coverStartDate = externalPayment.coverPeriodStartDate;
    payment.coverEndDate = externalPayment.coverPeriodEndDate;
    payment.currency = externalPayment.currency;
    payment.dueDate = externalPayment.dueDate;
    payment.method = externalPayment.paymentMethod;
    payment.nominatedPayeeAddress = externalPayment.address;
    payment.nominatedPayeeName = externalPayment.payeeName;
    payment.payment_id = externalPayment.externalPaymentID;
    payment.status = externalPayment.status;
    payment.paymentDate = externalPayment.effectiveDate;
    payment.external = true;
    if (METHODOFDELIVERYEntry.CHEQUE.getCode()
      .equalsIgnoreCase(externalPayment.paymentMethod)) {
      payment.checkNumber = externalPayment.referenceNumber;
    }

    if (METHODOFDELIVERYEntry.EFT.getCode()
      .equalsIgnoreCase(externalPayment.paymentMethod)) {
      payment.bankAccountNumber = externalPayment.bankAccountNumber;
      payment.bankSortCode = externalPayment.bankSortCode;
    }

    final ReadPaymentDetailsKey pmtDetailsKey = new ReadPaymentDetailsKey();
    pmtDetailsKey.externalPaymentID = externalPaymentId;
    pmtDetailsKey.isExternalPayment = true;
    final CitizenPaymentInstructionDetails paymentDetails =
      this.citizenAccountFacade.readPaymentInstructionDetails(pmtDetailsKey);
    payment.statusOther = paymentDetails.statusCode;
    final ExternalPaymentBreakdownDtlsList results =
      ExternalPaymentBreakdownFactory.newInstance()
        .searchByPayment(externalPaymentKey);
    final Iterator var10 = results.dtls.iterator();

    while (var10.hasNext()) {
      final ExternalPaymentBreakdownDtls detailsRow =
        (ExternalPaymentBreakdownDtls) var10.next();
      final UAPaymentDetail paymentDetail = new UAPaymentDetail();
      paymentDetail.benefitName = detailsRow.caseName;
      paymentDetail.coverStartDate = detailsRow.coverPeriodStartDate;
      paymentDetail.coverEndDate = detailsRow.coverPeriodEndDate;
      paymentDetail.creditAmount = detailsRow.creditAmount;
      paymentDetail.debitAmount = detailsRow.debitAmount;
      paymentDetail.entitlement = detailsRow.component;
      payment.paymentDetails.add(paymentDetail);
    }

  }

}
