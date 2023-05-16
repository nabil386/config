package curam.ca.gc.bdm.hooks.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationImpl;
import curam.ca.gc.bdm.entity.subclass.productdelivery.fact.BDMProductDeliveryDAFactory;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.BDMSearchEligBenefitsForExternalLbyDtls;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.BDMSearchEligBenefitsForExternalLbyDtlsList;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.BDMSearchEligBenefitsForExternalLbyKey;
import curam.ca.gc.bdm.sl.productdelivery.impl.MaintainDeductionDetails;
import curam.codetable.CASESTATUS;
import curam.codetable.DEDUCTIONITEMTYPE;
import curam.codetable.DEDUCTIONNAME;
import curam.codetable.REASSESSMENTRESULT;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.MaintainDeductionItemsFactory;
import curam.core.intf.CaseDeductionItem;
import curam.core.intf.CaseHeader;
import curam.core.intf.MaintainDeductionItems;
import curam.core.sl.entity.fact.DeductionFactory;
import curam.core.sl.entity.struct.DeductionDtls;
import curam.core.sl.entity.struct.DeductionNameStatus;
import curam.core.sl.fact.CaseStatusModeFactory;
import curam.core.sl.infrastructure.assessment.event.impl.AssessmentEngineEventListener;
import curam.core.sl.infrastructure.entity.struct.VersionNo;
import curam.core.struct.BeneficiaryDetails;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemIDVersionNo;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseStartDate;
import curam.core.struct.CompleteDecisionCreationList;
import curam.core.struct.MaintainDeductionItemDetails;
import curam.core.struct.NomineeReassessmentHeader;
import curam.core.struct.ObjectiveTotalDetailsList;
import curam.core.struct.OverUnderPaymentHeaderDtls;
import curam.core.struct.OverUnderPaymentIn;
import curam.core.struct.ProcessOverUnderPaymentInput;
import curam.core.struct.ReassessmentMode;
import curam.core.struct.StoreDecisionsInd;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;

public class BDMAssessmentEngineEventListener
  extends AssessmentEngineEventListener {

  @Inject
  MaintainDeductionDetails maintainDeductionDetailsObj;

  @Inject
  BDMCommunicationImpl communicationImpl;

  public BDMAssessmentEngineEventListener() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Automatically transfer active deductions on the benefit case to the
   * underpayment case and initiate straight through processing if the
   * conditions are met
   */
  @Override
  public void postProcessPaymentCorrection(
    final ProcessOverUnderPaymentInput input,
    final NomineeReassessmentHeader nomineeReassessmentHeader,
    final BeneficiaryDetails beneficiaryDetails,
    final OverUnderPaymentHeaderDtls overUnderPaymentHeaderDtls,
    final ObjectiveTotalDetailsList objectiveTotalDetailsList)
    throws AppException, InformationalException {

    final long paymentCorrectionCaseID =
      input.nomReassessHdr.reassessmentSummaryDetails.reassessmentResultCaseIDOpt;

    if (input.reassessmentResultType.equals(REASSESSMENTRESULT.OVERPAYMENT)
      && paymentCorrectionCaseID != 0l) {

      final CaseHeader chObj = CaseHeaderFactory.newInstance();

      // get the concern role of the person the original case belongs to
      final CaseHeaderKey chKey = new CaseHeaderKey();
      chKey.caseID = input.caseID;
      final CaseHeaderDtls caseHeaderDtls = chObj.read(chKey);

      chKey.caseID = paymentCorrectionCaseID;
      final CaseHeaderDtls liabilityCase = chObj.read(chKey);

      createOverpaymentDeductions(paymentCorrectionCaseID,
        liabilityCase.concernRoleID, caseHeaderDtls.concernRoleID);
    }

  }

  /**
   * Creates a variable applied deduction of 50% linked to the provided payment
   * correction and activates it on all PDC's associated with the individual
   *
   * @param paymentCorrectionCaseID
   * @param concernRoleID
   */
  private void createOverpaymentDeductions(final long paymentCorrectionCaseID,
    final long liabilityConcernRoleID, final long caseConcernRoleID)
    throws AppException, InformationalException {

    CaseStatusModeFactory.newInstance()
      .setDisableRegenFCsCaseStatusDPPCheck(true);

    final MaintainDeductionItemDetails deductionDtls =
      new MaintainDeductionItemDetails();

    // read the details associated with the deduction
    final DeductionNameStatus deductionNameKey = new DeductionNameStatus();
    deductionNameKey.deductionName = DEDUCTIONNAME.PROGRAM_OVERPAYMENT;
    deductionNameKey.recordStatus = RECORDSTATUS.NORMAL;
    final DeductionDtls deductionConfigDtls = DeductionFactory.newInstance()
      .readActiveDeductionByName(deductionNameKey);

    // deduction config details
    deductionDtls.actionType = deductionConfigDtls.actionType;
    deductionDtls.category = deductionConfigDtls.category;
    deductionDtls.deductionName = deductionConfigDtls.deductionName;
    deductionDtls.deductionType = DEDUCTIONITEMTYPE.VARIABLE;

    deductionDtls.rate = 50;
    deductionDtls.liabilityConcernRoleID = liabilityConcernRoleID;
    deductionDtls.relatedCaseID = paymentCorrectionCaseID;

    // look for any benefit cases that are not closed
    final BDMSearchEligBenefitsForExternalLbyKey pdKey =
      new BDMSearchEligBenefitsForExternalLbyKey();
    pdKey.caseStatus = CASESTATUS.CLOSED;
    pdKey.concernRoleID = caseConcernRoleID;
    pdKey.deductionType = BDMDEDUCTIONTYPE.PROGRAM_SPECIFIC_OVERPAYMENT;
    final BDMSearchEligBenefitsForExternalLbyDtlsList eligBenefitsList =
      BDMProductDeliveryDAFactory.newInstance()
        .searchEligBenefitsForExternalLby(pdKey);

    final CaseHeaderKey chKey = new CaseHeaderKey();
    final CaseDeductionItemDtls cdiDtls = new CaseDeductionItemDtls();
    final MaintainDeductionItems maintainDeductionItemsObj =
      MaintainDeductionItemsFactory.newInstance();
    final CaseDeductionItemIDVersionNo cdiVersionNoDetails =
      new CaseDeductionItemIDVersionNo();
    final CaseDeductionItem cdiObj = CaseDeductionItemFactory.newInstance();
    final CaseDeductionItemKey cdiKey = new CaseDeductionItemKey();

    for (final BDMSearchEligBenefitsForExternalLbyDtls benefitCase : eligBenefitsList.dtls) {
      deductionDtls.caseID = benefitCase.caseID;

      // get the case start date
      chKey.caseID = benefitCase.caseID;
      final CaseStartDate caseStartDate =
        CaseHeaderFactory.newInstance().readStartDate(chKey);
      deductionDtls.startDate = caseStartDate.startDate;

      // get the priority
      cdiDtls.assign(deductionDtls);
      deductionDtls.priority =
        maintainDeductionDetailsObj.calculatePriority(cdiDtls);

      maintainDeductionItemsObj.createDeductionItem1(deductionDtls);

      cdiKey.caseDeductionItemID = deductionDtls.caseDeductionItemID;
      final VersionNo cdiVersionNo = cdiObj.readVersionNo(cdiKey);

      cdiVersionNoDetails.caseDeductionItemID =
        deductionDtls.caseDeductionItemID;
      cdiVersionNoDetails.versionNo = cdiVersionNo.versionNo;

      maintainDeductionItemsObj.activateDeduction(cdiVersionNoDetails);
    }

  }

  /**
   * Task 3753: Generate the benefit denial template after reassessment.
   *
   * @param PDC CaseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void postReassessFCs(final CompleteDecisionCreationList newDecisions,
    final CompleteDecisionCreationList existingDecisions,
    final ReassessmentMode reassessmentMode,
    final OverUnderPaymentIn overUnderPaymentIn,
    final StoreDecisionsInd storeDecisionsInd)
    throws AppException, InformationalException {

    final CaseHeader caseHeader = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = overUnderPaymentIn.caseID;
    final CaseHeaderDtls caseHeaderDtls = caseHeader.read(caseHeaderKey);

    // check determination result display rule to get the determination result
    // Overall Eligibility=Fail
    // Create Claim Denied Notification
    try {
      final boolean isOverallEligibilityPassed =
        communicationImpl.determineEligibilityForPDC(caseHeaderKey.caseID);
      if (!isOverallEligibilityPassed) {
        communicationImpl.createDPBenefitDenialLetterByCaseID(
          caseHeaderKey.caseID, caseHeaderDtls.concernRoleID);
      } else {
        communicationImpl.createDisentitlementLetterByCaseID(
          caseHeaderKey.caseID, caseHeaderDtls.concernRoleID);
      }

      // Task-16432 If applicable, create Dependent Benefit Entitlement Issues
      // letter
      communicationImpl
        .createDependentBenefitEntitlementIssuesLetter(caseHeaderKey.caseID);
      communicationImpl.createRecalculationLetter(caseHeaderKey.caseID);
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(e.getLocalizedMessage());
    }
  }
}
