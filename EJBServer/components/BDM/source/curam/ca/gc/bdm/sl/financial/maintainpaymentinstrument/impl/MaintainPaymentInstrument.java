package curam.ca.gc.bdm.sl.financial.maintainpaymentinstrument.impl;

import curam.ca.gc.bdm.application.impl.BDMPersonMatch;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMPMTRECONCILIATIONSTATUS;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentInstrumentDAFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPaymentInstrumentDA;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMCountILIsKey;
import curam.ca.gc.bdm.entity.fact.BDMProductFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMCodeTableComboFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMInstructionLineItemFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMInstructionLineItem;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionDtlsKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentReferenceNumber;
import curam.ca.gc.bdm.entity.financial.struct.BDMPmtInstrumentDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMPmtInstrumentDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.ReadSubOrdCodeByGovernTableCodeSubOrdTable;
import curam.ca.gc.bdm.entity.intf.BDMProduct;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetails;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMProductDtls;
import curam.ca.gc.bdm.entity.struct.BDMProductKey;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.fact.BDMInstructionLineItemDAFactory;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.intf.BDMInstructionLineItemDA;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.BDMDeductionTypeDetails;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.ILIsByPaymentInstrumentDetails;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.ILIsByPaymentInstrumentDetailsList;
import curam.ca.gc.bdm.events.BDMPAYMENTTASK;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMCANCELLEDPAYMENTS;
import curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.struct.BDMUACaseWorkerVerificationEnactKey;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMVSGTaskCreateDetails;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.fact.MaintainLiabilityCaseFactory;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.intf.MaintainLiabilityCase;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.CreateNonReverseOverpaymentDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMCancelPaymentInstrumentDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMCancelledPaymentPersonDetails;
import curam.cefwidgets.sl.impl.CuramConst;
import curam.codetable.CANCELLATIONREQUEST;
import curam.codetable.ILICATEGORY;
import curam.codetable.PRODUCTTYPE;
import curam.codetable.SKILLTYPE;
import curam.codetable.TASKPRIORITY;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.facade.fact.FinancialFactory;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.facade.struct.CancelPaymentInstrumentDetails;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.PaymentInstructionFactory;
import curam.core.fact.PaymentInstrumentFactory;
import curam.core.fact.PaymentRegenerationRequestFactory;
import curam.core.intf.PaymentInstruction;
import curam.core.intf.PaymentInstrument;
import curam.core.sl.entity.fact.CaseNomineeFactory;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.intf.CaseNominee;
import curam.core.sl.entity.intf.CaseParticipantRole;
import curam.core.sl.entity.struct.CaseNomineeDtls;
import curam.core.sl.entity.struct.CaseNomineeKey;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.RecordCount;
import curam.core.sl.struct.TaskCreateDetails;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.FinInstructionID;
import curam.core.struct.InstructionLineItemKey;
import curam.core.struct.PIReconcilStatusCode;
import curam.core.struct.PaymentInstructionDtls;
import curam.core.struct.PaymentInstructionKey;
import curam.core.struct.PaymentInstrumentDtls;
import curam.core.struct.PaymentInstrumentKey;
import curam.core.struct.PaymentRegenerationRequestDtls;
import curam.core.struct.PmtInstrumentID;
import curam.core.struct.ProductDeliveryDtls;
import curam.core.struct.ProductDeliveryKey;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.impl.EnactmentService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MaintainPaymentInstrument implements
  curam.ca.gc.bdm.sl.financial.maintainpaymentinstrument.intf.MaintainPaymentInstrument {

  /**
   * Tries to cancel a payment via its reference number and creates a task. If
   * the reference number cannot be found, try to do a person match based off
   * memberID and create a task.
   */
  @Override
  public void cancelPaymentByPRN(final String memberID,
    final String paymentReferenceNumber)
    throws AppException, InformationalException {

    final BDMPaymentInstrumentReferenceNumber refNumKey =
      new BDMPaymentInstrumentReferenceNumber();
    refNumKey.spsPmtRefNo = paymentReferenceNumber;

    // find payment instrument ID by reference number.
    final BDMPmtInstrumentDetailsList bdmPmtInstrumentDetailsList =
      BDMPaymentInstrumentFactory.newInstance()
        .searchPmtInstrumentIDsByReferenceNumber(refNumKey);
    final PaymentInstrumentKey paymentInstrumentKey =
      new PaymentInstrumentKey();
    // if it is found, cancel the payment
    if (bdmPmtInstrumentDetailsList.dtls.size() > 0) {
      paymentInstrumentKey.pmtInstrumentID =
        bdmPmtInstrumentDetailsList.dtls.item(0).pmtInstrumentID;
      for (int i = 0; i < bdmPmtInstrumentDetailsList.dtls.size(); i++) {

        // if any payment is not issued, throw an error
        if (!bdmPmtInstrumentDetailsList.dtls.get(i).reconcilStatusCode
          .equals(BDMPMTRECONCILIATIONSTATUS.ISSUED)) {
          throw new AppException(BDMCANCELLEDPAYMENTS.ERR_PMT_NOT_ISSUED);
        }
        final CancelPaymentInstrumentDetails cancelPaymentDetails =
          new CancelPaymentInstrumentDetails();
        cancelPaymentDetails.pmtInstrumentID =
          bdmPmtInstrumentDetailsList.dtls.get(i).pmtInstrumentID;

        final BDMCancelPaymentInstrumentDetails bdmCancelDetails =
          new BDMCancelPaymentInstrumentDetails();
        bdmCancelDetails.spsPmtRefNo = paymentReferenceNumber;
        bdmCancelDetails.dtls = cancelPaymentDetails;
        cancelBDMPayment(bdmCancelDetails);
      }
      // Once all Payment Instruments are cancelled, now create the task.

      final BDMPersonSearchResultDetailsList personList =
        BDMPersonMatch.searchPersonBySIN(memberID);
      long concernRoleID = 0;
      String concernRoleName = CuramConst.gkEmpty;
      if (personList.dtls.size() >= 1) {
        concernRoleID = personList.dtls.item(0).concernRoleID;
        concernRoleName = personList.dtls.item(0).concernRoleName;

        final PaymentInstrument paymentInstrumentObj =
          PaymentInstrumentFactory.newInstance();
        final PaymentInstrumentDtls paymentInstrumentDtls =
          paymentInstrumentObj.read(paymentInstrumentKey);

        final CaseNominee caseNomineebj = CaseNomineeFactory.newInstance();
        final CaseNomineeKey caseNomineeKey = new CaseNomineeKey();
        caseNomineeKey.caseNomineeID = paymentInstrumentDtls.caseNomineeID;
        final CaseNomineeDtls caseNomineeDtls =
          caseNomineebj.read(caseNomineeKey);

        final CaseParticipantRole caseParticipantRoleObj =
          CaseParticipantRoleFactory.newInstance();
        final CaseParticipantRoleKey caseParticipantRoleKey =
          new CaseParticipantRoleKey();
        caseParticipantRoleKey.caseParticipantRoleID =
          caseNomineeDtls.caseParticipantRoleID;
        final CaseParticipantRoleDtls caseParticipantRoleDtls =
          caseParticipantRoleObj.read(caseParticipantRoleKey);

        final curam.core.intf.ProductDelivery productDeliveryObj =
          curam.core.fact.ProductDeliveryFactory.newInstance();
        final ProductDeliveryKey productDeliveryKey =
          new ProductDeliveryKey();
        productDeliveryKey.caseID = caseParticipantRoleDtls.caseID;
        final ProductDeliveryDtls productDeliveryDtls =
          productDeliveryObj.read(productDeliveryKey);

        final BDMProduct bdmProductObj = BDMProductFactory.newInstance();
        final BDMProductKey bdmProductKey = new BDMProductKey();
        bdmProductKey.productID = productDeliveryDtls.productID;
        final BDMProductDtls bdmProductDtls =
          bdmProductObj.read(bdmProductKey);
        String vsgType = "";
        final ReadSubOrdCodeByGovernTableCodeSubOrdTable governTableCodeSubOrdTable =
          new ReadSubOrdCodeByGovernTableCodeSubOrdTable();
        governTableCodeSubOrdTable.governTableName = PRODUCTTYPE.TABLENAME;
        governTableCodeSubOrdTable.governCode =
          productDeliveryDtls.productType;
        governTableCodeSubOrdTable.subOrdTableName = SKILLTYPE.TABLENAME;

        try {
          vsgType = BDMCodeTableComboFactory.newInstance()
            .readSubOrdCodeByGovernTableCodeSubOrdTable(
              governTableCodeSubOrdTable).subOrdCode;
        } catch (final RecordNotFoundException rfe) {
          rfe.printStackTrace();
          vsgType = SKILLTYPE.VSG03;
        }

        createCanceledPaymentTask(paymentReferenceNumber, concernRoleID,
          concernRoleName, vsgType, productDeliveryKey.caseID);
      } else {
        throw new AppException(BDMCANCELLEDPAYMENTS.ERR_NO_PERSON_MATCHES);
      }
    } else {
      throw new AppException(BDMCANCELLEDPAYMENTS.ERR_NO_RECORD_FOUND);
    }
  }

  /**
   * Cancels a payment instrument and creates a task
   */
  @Override
  public void
    cancelBDMPayment(final BDMCancelPaymentInstrumentDetails details)
      throws AppException, InformationalException {

    // if no cancellation reason was set, set to the default reason
    if (StringUtil.isNullOrEmpty(details.dtls.reasonCode)) {
      details.dtls.reasonCode = CANCELLATIONREQUEST.DEFAULTCODE;
    }

    // cancel the payment instrument
    FinancialFactory.newInstance().cancelPaymentInstrument(details.dtls);

    setTaxDetails(details);

    // BEGIN - Task 9900 - Reissue of Cancelled Payments - 2022-02-20
    this.setCancelledILIStatus(details.dtls.pmtInstrumentID);
    // END - Task 9900 - Reissue of Cancelled Payments - 2022-02-20

  }

  /**
   * Task 57834
   *
   * @param spsPmtRefNo
   * @param concernRoleID
   * @param concernRoleName
   * @param vsgType
   * @param pdcCaseID
   * @throws AppException
   * @throws InformationalException
   */
  private void createCanceledPaymentTask(final String spsPmtRefNo,
    final long concernRoleID, final String concernRoleName,
    final String vsgType, final long pdcCaseID)
    throws AppException, InformationalException {

    // Get target work queue ID using skill type
    final BDMTaskSkillTypeKey skillTypeKey = new BDMTaskSkillTypeKey();
    skillTypeKey.skillType = vsgType;
    final long targetWorkQueueID = BDMWorkAllocationTaskFactory.newInstance()
      .getWorkQueueIDBySkillType(skillTypeKey).workQueueID;

    // create task subject
    final LocalisableString taskSubject = new LocalisableString(
      curam.ca.gc.bdm.message.BDMPERSONCANCELLEDPAYMENTTASKSUBJECT.INF_PERSON_CANCELLED_PAYMENT_TASK_SUBJECT);
    taskSubject.arg(BDMUtil.getCodeTableDescriptionForUserLocale(
      SKILLTYPE.TABLENAME, skillTypeKey.skillType));
    // get client oral language preference
    String orallanuage = CuramConst.gkEmpty;
    final PDCPerson pdcPerson = PDCPersonFactory.newInstance();
    final PersonAndEvidenceTypeList personAndEvidenceTypeList =
      new PersonAndEvidenceTypeList();
    personAndEvidenceTypeList.concernRoleID = concernRoleID;
    personAndEvidenceTypeList.evidenceTypeList = "PDC0000263";
    final PDCEvidenceDetailsList evidenceDetailsList = pdcPerson
      .listCurrentParticipantEvidenceByTypes(personAndEvidenceTypeList);
    if (evidenceDetailsList.list.size() > 0) {
      final String oralLanugageCode = BDMEvidenceUtil.getDynEvdAttrValue(
        evidenceDetailsList.list.item(0).evidenceID, "PDC0000263",
        "preferredOralLanguage");
      if (BDMLANGUAGE.ENGLISHL.equals(oralLanugageCode)) {
        orallanuage = "EN";
      } else if (BDMLANGUAGE.FRENCHL.equals(oralLanugageCode)) {
        orallanuage = "FR";
      }
    } else {
      orallanuage = "EN";
    }

    taskSubject.arg(orallanuage);

    taskSubject.arg(spsPmtRefNo);

    final curam.core.struct.ConcernRoleKey crKey =
      new curam.core.struct.ConcernRoleKey();
    crKey.concernRoleID = concernRoleID;
    final ConcernRoleDtls crDtls =
      ConcernRoleFactory.newInstance().readConcernRole(crKey);
    taskSubject.arg(crDtls.concernRoleName);
    taskSubject.arg(crDtls.primaryAlternateID);

    // create enactment struct
    final BDMVSGTaskCreateDetails bdmVSGTaskCreateDetails =
      new BDMVSGTaskCreateDetails();
    bdmVSGTaskCreateDetails.participantRoleID = concernRoleID;
    bdmVSGTaskCreateDetails.caseID = pdcCaseID;
    bdmVSGTaskCreateDetails.priority = TASKPRIORITY.NORMAL;
    // bdmVSGTaskCreateDetails.subject = taskSubject.toString();
    // Task 65963 Fix task subject
    bdmVSGTaskCreateDetails.subject =
      taskSubject.getMessage(TransactionInfo.getProgramLocale());
    bdmVSGTaskCreateDetails.workQueueID = targetWorkQueueID;

    BDMTaskSkillTypeList bdmTaskSkillTypeList = new BDMTaskSkillTypeList();
    bdmTaskSkillTypeList = BDMWorkAllocationTaskFactory.newInstance()
      .searchBDMTaskSkillType(skillTypeKey);
    if (bdmTaskSkillTypeList.dtls.size() > 0) {
      bdmVSGTaskCreateDetails.bdmTaskClassificationID =
        bdmTaskSkillTypeList.dtls.get(0).bdmTaskClassificationID;
    }
    final curam.core.intf.CaseHeader caseHeaderObj =
      CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = pdcCaseID;
    final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);
    bdmVSGTaskCreateDetails.caseReference = caseHeaderDtls.caseReference;

    // set the payment specific details
    final BDMCancelledPaymentPersonDetails bdmCancelledPaymentDetails =
      new BDMCancelledPaymentPersonDetails();
    bdmCancelledPaymentDetails.concernRoleName = crDtls.concernRoleName;
    bdmCancelledPaymentDetails.memberID = crDtls.primaryAlternateID;
    // Confirmed with Maria that PRN is always 12 digit number.
    bdmCancelledPaymentDetails.spsPmtRefNo = Long.valueOf(spsPmtRefNo);

    // add structs to enactment struct list
    final List<Object> enactmentStructs = new ArrayList<Object>();
    enactmentStructs.add(bdmVSGTaskCreateDetails);
    enactmentStructs.add(bdmCancelledPaymentDetails);

    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();
    final int taskDeadline = 2;
    final long inputDateTimeInMills =
      Date.getCurrentDate().addDays(taskDeadline).asLong();
    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;
    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);
    // enact workflow to create cancelled payments task
    EnactmentService.startProcess(BDMConstants.kBDMCancelledPaymentsTask,
      enactmentStructs);

  }

  /**
   * Once a payment is cancelled, re-flag the payment instrument for tax
   * reporting
   *
   * @param details
   * @throws AppException
   * @throws InformationalException
   */
  public void setTaxDetails(final BDMCancelPaymentInstrumentDetails details)
    throws AppException, InformationalException {

    final BDMPaymentInstrumentDA bdmPaymentInstrumentDAObj =
      BDMPaymentInstrumentDAFactory.newInstance();
    final BDMPaymentInstrument bdmPIObj =
      BDMPaymentInstrumentFactory.newInstance();
    final BDMCountILIsKey countILIKey = new BDMCountILIsKey();

    // read the payment instrument details
    final BDMPaymentInstrumentKey bdmPIKey = new BDMPaymentInstrumentKey();
    bdmPIKey.pmtInstrumentID = details.dtls.pmtInstrumentID;
    final BDMPaymentInstrumentDtls bdmPIDtls = bdmPIObj.read(bdmPIKey);

    // check if there are ILIs that are eligible for tax reporting
    countILIKey.iliCategory = ILICATEGORY.PAYMENTINSTRUCTION;
    countILIKey.pmtInstrumentID = details.dtls.pmtInstrumentID;
    countILIKey.taxReportInd = true;
    final RecordCount iliCount =
      bdmPaymentInstrumentDAObj.countILIsForTaxReporting(countILIKey);

    if (iliCount.count > 0) {
      bdmPIDtls.addToTaxSlipInd = true;
      bdmPIObj.modify(bdmPIKey, bdmPIDtls);
    }

  }

  /**
   * Added the method to add post process to create a Non-Reversible Overpayment
   * Case for non-reversible deduction.
   */
  private void setCancelledILIStatus(final long pmtInstrumentID)
    throws AppException, InformationalException {

    // BEGIN - Task 9900 - Reissue of Cancelled Payments - 2022-02-20
    final BDMInstructionLineItemDA instructionLineItemDA =
      BDMInstructionLineItemDAFactory.newInstance();

    final PaymentInstrumentKey pmtInstrumentKey = new PaymentInstrumentKey();
    pmtInstrumentKey.pmtInstrumentID = pmtInstrumentID;

    final ILIsByPaymentInstrumentDetailsList iliList = instructionLineItemDA
      .searchBDMILIsByPaymentInstrument(pmtInstrumentKey);

    final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
      new BDMPaymentInstrumentKey();
    bdmPaymentInstrumentKey.pmtInstrumentID = pmtInstrumentID;
    final BDMPaymentInstrument bdmPaymentInstrument =
      BDMPaymentInstrumentFactory.newInstance();
    final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
      bdmPaymentInstrument.read(bdmPaymentInstrumentKey);
    final int currentYear =
      Date.getCurrentDate().getCalendar().get(Calendar.YEAR);
    final int paymentDueYear = bdmPaymentInstrumentDtls.paymentDueDate
      .getCalendar().get(Calendar.YEAR);

    for (final ILIsByPaymentInstrumentDetails instructionLineItemDtls : iliList.dtls) {
      final InstructionLineItemKey instructionLineItemKey =
        new InstructionLineItemKey();
      instructionLineItemKey.instructLineItemID =
        instructionLineItemDtls.instructLineItemID;
      BDMDeductionTypeDetails bdmDeductionTypeDetails = null;
      boolean isRecordFound = false;
      try {
        bdmDeductionTypeDetails = instructionLineItemDA
          .getILINonReversibleDed(instructionLineItemKey);
        isRecordFound = true;
      } catch (final RecordNotFoundException rnfex) {
        isRecordFound = false;
      }

      final CreateNonReverseOverpaymentDetails createNonReverseOverpaymentDetails =
        new CreateNonReverseOverpaymentDetails();

      if (isRecordFound) {
        if (bdmDeductionTypeDetails != null
          && (BDMDEDUCTIONTYPE.BASIC_FEDERAL_TAX
            .equals(bdmDeductionTypeDetails.deductionType)
            || BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX
              .equals(bdmDeductionTypeDetails.deductionType)
            || BDMDEDUCTIONTYPE.VOLUNTARY_TAX_WITHHOLD_FED
              .equals(bdmDeductionTypeDetails.deductionType)
            || BDMDEDUCTIONTYPE.VOLUNTARY_TAX_WITHHOLD_Prov
              .equals(bdmDeductionTypeDetails.deductionType))) {
          if (currentYear == paymentDueYear) {
            continue;
          }
        }
        createNonReverseOverpaymentDetails.caseStartDate =
          instructionLineItemDtls.coverPeriodFrom.after(
            TransactionInfo.getSystemDate()) ? TransactionInfo.getSystemDate()
              : instructionLineItemDtls.coverPeriodFrom;
        createNonReverseOverpaymentDetails.caseEndDate =
          instructionLineItemDtls.coverPeriodTo;
        createNonReverseOverpaymentDetails.concernRoleID =
          instructionLineItemDtls.primaryClientID;
        createNonReverseOverpaymentDetails.liabilityAmount =
          instructionLineItemDtls.amount;
        final MaintainLiabilityCase maintainLiabilityCaseObj =
          MaintainLiabilityCaseFactory.newInstance();
        final CaseIDKey caseIDKey = maintainLiabilityCaseObj
          .createNonReverseDedOverpayment(createNonReverseOverpaymentDetails);
        // UPDATE BDMInstructionLineItem SET
        // nonReverseRelatedCaseID=:lbyCaseID
        // WHERE instructLineItemID = iliList.ITEM.instructLineItemID
        final BDMInstructionLineItem bdmInstructionLineItem =
          BDMInstructionLineItemFactory.newInstance();
        final BDMInstructionDtlsKey bdmInstructionDtls =
          new BDMInstructionDtlsKey();

        final BDMInstructionLineItemKey bdmILIKey =
          new BDMInstructionLineItemKey();
        bdmILIKey.instructLineItemID =
          instructionLineItemDtls.instructLineItemID;
        final BDMInstructionLineItemDtls bdmILIDtls =
          bdmInstructionLineItem.read(bdmILIKey);

        bdmInstructionDtls.nonReverseRelatedCaseID = caseIDKey.caseID;
        bdmInstructionDtls.versionNo = bdmILIDtls.versionNo;
        bdmInstructionLineItem.updateCaseIDByInstructIlIID(bdmILIKey,
          bdmInstructionDtls);

      }
    }
    // END - Task 9900 - Reissue of Cancelled Payments - 2022-02-20
  }

  /**
   * Given a financial instruction ID, finds the associated payment instrument
   * and triggers the closing of a payment instrument task wher ethe reference
   * number matched
   */
  @Override
  public void triggerMatchingPaymentInstrumentEvent(
    final FinInstructionID finInstructionKey)
    throws AppException, InformationalException {

    final PaymentInstruction pmtInstrObj =
      PaymentInstructionFactory.newInstance();
    final PaymentInstructionDtls paymentInstructionDtls =
      pmtInstrObj.readByFinInstructionID(finInstructionKey);

    final PaymentInstructionKey pmtInstructionKey =
      new PaymentInstructionKey();
    pmtInstructionKey.pmtInstructionID =
      paymentInstructionDtls.pmtInstructionID;
    final NotFoundIndicator nfi = new NotFoundIndicator();
    final PaymentRegenerationRequestDtls regenDtls =
      PaymentRegenerationRequestFactory.newInstance()
        .readByPmtInstructionID(nfi, pmtInstructionKey);
    if (!nfi.isNotFound()) {
      final FinInstructionID regenKey = new FinInstructionID();
      regenKey.finInstructionID = regenDtls.regenFinInstructID;
      final PaymentInstructionDtls regenPmtInstrDtls =
        PaymentInstructionFactory.newInstance()
          .readByFinInstructionID(regenKey);
      final NotFoundIndicator nfi2 = new NotFoundIndicator();
      final PaymentInstrumentKey pmtInstrKey = new PaymentInstrumentKey();
      pmtInstrKey.pmtInstrumentID = regenPmtInstrDtls.pmtInstrumentID;
      final PaymentInstrumentDtls pmtInstrDtls =
        PaymentInstrumentFactory.newInstance().read(nfi2, pmtInstrKey);
      if (!nfi2.isNotFound()) {
        final PIReconcilStatusCode codeDtls = new PIReconcilStatusCode();
        codeDtls.reconcilStatusCode = pmtInstrDtls.reconcilStatusCode;
        codeDtls.versionNo = pmtInstrDtls.versionNo;
        PaymentInstrumentFactory.newInstance()
          .modifyReconcilStatusCode(pmtInstrKey, codeDtls);
      }
    }

    // Close PRN match cancelled payment task only if all associated Payment
    // Instruments were ReIssued or Invalidated.
    final BDMPaymentInstrumentKey bdmPmtKey = new BDMPaymentInstrumentKey();
    bdmPmtKey.pmtInstrumentID = paymentInstructionDtls.pmtInstrumentID;
    final BDMPaymentInstrument bdmPmtObj =
      BDMPaymentInstrumentFactory.newInstance();
    final NotFoundIndicator bdmPmtNfi = new NotFoundIndicator();
    final BDMPaymentInstrumentDtls bdmPmtDtls =
      bdmPmtObj.read(bdmPmtNfi, bdmPmtKey);
    if (!bdmPmtNfi.isNotFound()) {
      final BDMPaymentInstrumentReferenceNumber refNumKey =
        new BDMPaymentInstrumentReferenceNumber();
      refNumKey.spsPmtRefNo = bdmPmtDtls.spsPmtRefNo;
      final PaymentInstrumentKey pmtInstrumentKey =
        new PaymentInstrumentKey();
      pmtInstrumentKey.pmtInstrumentID = bdmPmtDtls.pmtInstrumentID;
      final PaymentInstrument pmtObj = PaymentInstrumentFactory.newInstance();
      PaymentInstrumentDtls paymentInstrumentDtls =
        pmtObj.read(pmtInstrumentKey);
      // find payment instrument ID by reference number.
      final BDMPmtInstrumentDetailsList bdmPmtInstrumentDetailsList =
        BDMPaymentInstrumentFactory.newInstance()
          .searchPmtInstrumentIDsByReferenceNumber(refNumKey);
      BDMPmtInstrumentDetails bdmPmtDetails;
      final PmtInstrumentID pmtInstrumentID = new PmtInstrumentID();
      FinInstructionID finInstructionID;
      boolean closeTask = true;
      if (bdmPmtInstrumentDetailsList.dtls.size() > 0) {
        for (int i = 0; i < bdmPmtInstrumentDetailsList.dtls.size(); i++) {
          bdmPmtDetails = bdmPmtInstrumentDetailsList.dtls.item(i);
          pmtInstrumentKey.pmtInstrumentID = bdmPmtDetails.pmtInstrumentID;
          paymentInstrumentDtls = pmtObj.read(pmtInstrumentKey);
          if (bdmPmtDetails.pmtInstrumentID == paymentInstructionDtls.pmtInstrumentID) {
            if (nfi.isNotFound() && !paymentInstrumentDtls.invalidatedInd) {
              closeTask = false;
              break;
            }
          } else {
            pmtInstrumentID.pmtInstrumentID = bdmPmtDetails.pmtInstrumentID;
            final NotFoundIndicator nfiFinInstr = new NotFoundIndicator();
            finInstructionID =
              pmtInstrObj.readByPmtInstrumentID(nfiFinInstr, pmtInstrumentID);
            if (!nfiFinInstr.isNotFound()) {
              final NotFoundIndicator nfiPmtInstr = new NotFoundIndicator();
              final PaymentInstructionDtls pmtInstrDtls = pmtInstrObj
                .readByFinInstructionID(nfiPmtInstr, finInstructionID);
              if (!nfiPmtInstr.isNotFound()) {
                final PaymentInstructionKey pmtInstrKey =
                  new PaymentInstructionKey();
                pmtInstrKey.pmtInstructionID = pmtInstrDtls.pmtInstructionID;
                final NotFoundIndicator nfiRegen = new NotFoundIndicator();
                PaymentRegenerationRequestFactory.newInstance()
                  .readByPmtInstructionID(nfiRegen, pmtInstrKey);
                if (nfiRegen.isNotFound()
                  && !paymentInstrumentDtls.invalidatedInd) {
                  closeTask = false;
                }
              }
            }
          }
        }
      } else {
        closeTask = false;
      }
      if (closeTask) {
        final Event event = new Event();
        event.eventKey = BDMPAYMENTTASK.PRNMATCHCLOSED;
        event.primaryEventData = Long.valueOf(bdmPmtDtls.spsPmtRefNo);
        EventService.raiseEvent(event);
      }
    }

  }

  /**
   * Creates a task for a caseworker when there is no matching PRN but a person
   * is found based off the memberID
   */
  @Override
  public void createTaskForNoPRNPersonMatch(final String memberID,
    final String paymentReferenceNumber,
    final BDMPersonSearchResultDetails personDetails)
    throws AppException, InformationalException {

    // set the concern role ID
    final TaskCreateDetails taskCreateDetails = new TaskCreateDetails();
    taskCreateDetails.participantRoleID = personDetails.concernRoleID;

    // set the payment specific details
    final BDMCancelledPaymentPersonDetails bdmCancelledPaymentDetails =
      new BDMCancelledPaymentPersonDetails();
    // Confirmed with Maria that PRN is always 12 digit number.
    bdmCancelledPaymentDetails.spsPmtRefNo =
      Long.valueOf(paymentReferenceNumber);
    bdmCancelledPaymentDetails.concernRoleName =
      personDetails.concernRoleName;
    bdmCancelledPaymentDetails.memberID = memberID;

    // add structs to enactment struct list
    final List<Object> enactmentStructs = new ArrayList<Object>();
    enactmentStructs.add(taskCreateDetails);
    enactmentStructs.add(bdmCancelledPaymentDetails);

    // enact workflow to create cancelled payments person match task
    EnactmentService.startProcess(
      BDMConstants.kBDMCancelledPaymentsPersonMatchTask, enactmentStructs);

  }

}
