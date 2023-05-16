package curam.ca.gc.bdmoas.evidence.sl.impl;

import curam.advisor.facade.fact.AdvisorFactory;
import curam.advisor.facade.struct.AdviceKey;
import curam.ca.gc.bdm.sl.impl.BDMEvidenceControllerHookExtensionImpl;
import curam.ca.gc.bdmoas.entity.oasdeduction.fact.BDMOASDeductionFactory;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionCountDetails;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionCountKey;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionDtls;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionEvidenceIDRecordStatus;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.ca.gc.bdmoas.message.BDMOASEVIDENCEMESSAGE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.MaintainDeductionItemsFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.MaintainDeductionItems;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.struct.CaseDeductionItemID;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.MaintainDeductionItemDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;

public class BDMOASEvidenceControllerHookExtensionImpl
  extends BDMEvidenceControllerHookExtensionImpl {

  /**
   * BDMOAS component postInsertEvidence implementation
   */
  @Override
  public void postInsertEvidence(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    super.postInsertEvidence(caseKey, evidenceKey);

    if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP)) {

      generateAdvice(caseKey);

    }

    final BDMOASEligibilityEntitlementOverrideTaskHelper EEOverrideTaskHelper =
      new BDMOASEligibilityEntitlementOverrideTaskHelper();
    EEOverrideTaskHelper.createOverrideEvidenceTaskPostInsert(caseKey,
      evidenceKey);

    generateAdviceForApplicationCase(evidenceKey, caseKey);

  }

  /**
   * BDMOAS component postModifyEvidence implementation
   */
  @Override
  public void postModifyEvidence(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    super.postModifyEvidence(caseKey, evidenceKey);

    if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP)) {

      generateAdvice(caseKey);
    }

    final BDMOASEligibilityEntitlementOverrideTaskHelper EEOverrideTaskHelper =
      new BDMOASEligibilityEntitlementOverrideTaskHelper();
    EEOverrideTaskHelper.createOverrideEvidenceTaskPostModify(caseKey,
      evidenceKey);

    if (evidenceKey.evidenceType.equals(CASEEVIDENCE.OAS_RECOVERY_TAX)) {
      modifyAssociatedOASRecoveryTaxDeduction(evidenceKey);
    }

    generateAdviceForApplicationCase(evidenceKey, caseKey);

  }

  private void generateAdviceForApplicationCase(
    final EIEvidenceKey evidenceKey, final CaseKey caseKey)
    throws AppException, InformationalException {

    if (evidenceKey.evidenceType.equals(CASEEVIDENCE.OAS_LEGAL_STATUS)
      || evidenceKey.evidenceType
        .equals(CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA)
      || evidenceKey.evidenceType.equals(CASEEVIDENCE.OAS_RESIDENCE_PERIOD)
      || evidenceKey.evidenceType.equals(CASEEVIDENCE.OAS_FOREIGN_INCOME)
      || evidenceKey.evidenceType
        .equals(CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL)
      || evidenceKey.evidenceType.equals(CASEEVIDENCE.OAS_WORLD_INCOME)) {

      generateAdvice(caseKey);
    }

  }

  /**
   * This method will generate the advisor
   *
   * @param caseKey
   * Contains the case id.
   */
  public void generateAdvice(final CaseKey caseKey)
    throws AppException, InformationalException {

    // Check for the case type code
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseKey.caseID;
    final CaseHeaderDtls caseHeaderDtls =
      CaseHeaderFactory.newInstance().read(caseHeaderKey);

    if (CASETYPECODE.APPLICATION_CASE.equals(caseHeaderDtls.caseTypeCode)) {

      getAdvice(BDMOASConstants.BDM_OAS_APPLICATIONCASE_HOME_PAGE_ID,
        caseKey);

    } else if (CASETYPECODE.INTEGRATEDCASE
      .equals(caseHeaderDtls.caseTypeCode)) {

      getAdvice(BDMOASConstants.BDM_OAS_IC_CASE_HOME_PAGE_ID, caseKey);
    }
  }

  /**
   * Method will invoke the advisor
   *
   * @param pageName
   * Advice Context Page Name
   * @param caseKey
   * Contains the case id.
   */
  public void getAdvice(final String pageName, final CaseKey caseKey)
    throws AppException, InformationalException {

    final AdviceKey adviceKey = new AdviceKey();

    adviceKey.adviceContextKeyName = pageName;
    adviceKey.parameters = "caseID=" + caseKey.caseID;
    AdvisorFactory.newInstance().getAdvice(adviceKey);
  }

  /**
   * BDMOAS component preRemoveEvidence implementation
   */
  @Override
  public void preRemoveEvidence(final CaseKey paramCaseKey,
    final EIEvidenceKey paramEIEvidenceKey)
    throws AppException, InformationalException {

    super.preRemoveEvidence(paramCaseKey, paramEIEvidenceKey);

    // If the Evidence Type is OAS Recovery Tax
    if (paramEIEvidenceKey.evidenceType
      .equals(CASEEVIDENCE.OAS_RECOVERY_TAX)) {
      checkRecoveryTaxDeductionProcessed(paramEIEvidenceKey);
    } // if the Evidence T
    else if (paramEIEvidenceKey.evidenceType
      .equals(CASEEVIDENCE.OAS_NRT_CORRECTION)) {
      checkNRTDeductionProcessed(paramEIEvidenceKey);
    }

  }

  /**
   * Check if the OAS Recovery Tax is already used in the Payment
   * Processing.
   * If used then throw the exception message.
   *
   * @param paramEIEvidenceKey
   * Contains the evidence key value
   */
  private void
    checkRecoveryTaxDeductionProcessed(final EIEvidenceKey paramEIEvidenceKey)
      throws AppException, InformationalException {

    // Check if any deduction associated with this evidence is
    // included in the gross to net calculation.

    final BDMOASDeductionCountKey bdmOASDeductionCountKey =
      new BDMOASDeductionCountKey();
    bdmOASDeductionCountKey.evidenceID = paramEIEvidenceKey.evidenceID;

    // Check if the BDMOASDeduction is already processed.
    final BDMOASDeductionCountDetails bdmOASDeductionCountDetails =
      BDMOASDeductionFactory.newInstance()
        .countILIByEvidenceID(bdmOASDeductionCountKey);

    // If the deduction is already processed then throw the error.
    if (bdmOASDeductionCountDetails.numberOfRecords > CuramConst.gkZero) {

      throw new AppException(
        BDMOASEVIDENCEMESSAGE.ERR_OAS_RECOVERY_TAX_DEDUCTION_DELETE);
    }
  }

  /**
   * Update the existing associated OAS Recovery Tax deduction
   * record with the updated evidence record details.
   *
   * @param paramEIEvidenceKey
   * Contains the evidence key value
   * @throws AppException
   * Application Exception
   * @throws InformationalException
   * Informational Exception
   */
  private void modifyAssociatedOASRecoveryTaxDeduction(
    final EIEvidenceKey paramEIEvidenceKey)
    throws AppException, InformationalException {

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    final BDMOASDeductionEvidenceIDRecordStatus bdmOASDeductionKey =
      new BDMOASDeductionEvidenceIDRecordStatus();
    bdmOASDeductionKey.evidenceID = paramEIEvidenceKey.evidenceID;
    bdmOASDeductionKey.recordStatusCode = RECORDSTATUS.NORMAL;

    final BDMOASDeductionDtls bdmOASDeductionDtls = BDMOASDeductionFactory
      .newInstance().readByEvidenceID(notFoundIndicator, bdmOASDeductionKey);

    // Check if the existing OAS RCV tax evidence is already processed during
    // the financial
    // processing.

    if (!notFoundIndicator.isNotFound()) {

      // Get the modify Monthly Recovery Tax Amount and Start Date from the
      // evidence key.

      final EvidenceControllerInterface evidenceControllerObj =
        (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

      final EIEvidenceKey eviEvidenceKey = new EIEvidenceKey();
      eviEvidenceKey.evidenceID = paramEIEvidenceKey.evidenceID;
      eviEvidenceKey.evidenceType = CASEEVIDENCE.OAS_RECOVERY_TAX;

      final EIEvidenceReadDtls eiEvidenceReadDtls =
        evidenceControllerObj.readEvidence(eviEvidenceKey);

      final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
        (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

      final Money monthlyRecoveryTaxAmount = !dynamicEvidenceDataDetails
        .getAttribute("monthlyRecoveryTaxAmount").getValue().isEmpty()
          ? new Money(dynamicEvidenceDataDetails
            .getAttribute("monthlyRecoveryTaxAmount").getValue())
          : new Money(0.0D);

      final Date endDate = Date.getDate(
        dynamicEvidenceDataDetails.getAttribute("endDate").getValue());

      final MaintainDeductionItems maintainDeductionItemsObj =
        MaintainDeductionItemsFactory.newInstance();

      if (bdmOASDeductionDtls.caseDeductionItemID != CuramConst.gkZero) {

        final CaseDeductionItemID cdiIDKey = new CaseDeductionItemID();
        cdiIDKey.caseDeductionItemID =
          bdmOASDeductionDtls.caseDeductionItemID;

        // set modification details
        final MaintainDeductionItemDetails deductionItemDetails =
          maintainDeductionItemsObj.readDeductionItem(cdiIDKey);
        deductionItemDetails.amount = monthlyRecoveryTaxAmount;
        deductionItemDetails.endDate = endDate;
        maintainDeductionItemsObj.modifyDeduction(deductionItemDetails);

      }
    }
  }

  /**
   * Check if the NRT Tax is already used in the Payment
   * Processing.
   * If used then throw the exception message.
   *
   * @param paramEIEvidenceKey
   * Contains the evidence key value
   */
  private void
    checkNRTDeductionProcessed(final EIEvidenceKey paramEIEvidenceKey)
      throws AppException, InformationalException {

    // Check if any deduction associated with this evidence is
    // included in the gross to net calculation.

    final BDMOASDeductionCountKey bdmOASDeductionCountKey =
      new BDMOASDeductionCountKey();
    bdmOASDeductionCountKey.evidenceID = paramEIEvidenceKey.evidenceID;

    // Check if the BDMOASDeduction is already processed.
    final BDMOASDeductionCountDetails bdmOASDeductionCountDetails =
      BDMOASDeductionFactory.newInstance()
        .countILIByEvidenceID(bdmOASDeductionCountKey);

    // If the deduction is already processed then throw the error.
    if (bdmOASDeductionCountDetails.numberOfRecords > CuramConst.gkZero) {

      throw new AppException(
        BDMOASEVIDENCEMESSAGE.ERR_OAS_NRT_DEDUCTION_DELETE);
    }
  }

}
