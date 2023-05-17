package curam.ca.gc.bdm.sl.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationImpl;
import curam.ca.gc.bdm.communication.impl.BDMConcernRoleDocumentsImpl;
import curam.ca.gc.bdm.sl.maintaincasedeductions.intf.MaintainCaseDeductions;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.DetermineEligibilityFactory;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.intf.CaseHeader;
import curam.core.intf.DetermineEligibility;
import curam.core.intf.WMInstanceData;
import curam.core.sl.struct.CaseIDKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.WMInstanceDataDtls;
import curam.core.struct.WMInstanceDataKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;

public class BDMDetermineEligibility
  extends curam.ca.gc.bdm.sl.base.BDMDetermineEligibility {

  @Inject
  BDMConcernRoleDocumentsImpl concernRoleDocuments;

  @Inject
  BDMCommunicationImpl communicationImpl;

  @Inject
  private MaintainCaseDeductions maintainCaseDeductions;

  public BDMDetermineEligibility() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Override OOTB assess Benefit case method to create Benefit denial letter
   * when determination failed
   *
   * @param ticketID
   * @param Inst_Data_ID
   * @param flag
   */
  @Override
  public void assessBenefitCase(final long ticketID, final long inst_data_id,
    final boolean flag) throws AppException, InformationalException {

    // START - sync/create case deduction item for case to create VTW, Federal
    // or Provincial Tax Deduction records.
    // Moved reading WMSInstanceDataKey so that caseID can be passsed to
    // createDeductionsForCase method.
    final WMInstanceDataKey wmInstanceDataKey = new WMInstanceDataKey();
    wmInstanceDataKey.wm_instDataID = inst_data_id;

    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();
    final WMInstanceDataDtls wmInstanceDataDtls =
      wmInstanceDataObj.read(wmInstanceDataKey);

    this.createDeductionsForCase(wmInstanceDataDtls.caseID);
    // END - JSHAH - sync/create case deduction item for case to create VTW,
    // Federal or Provincial Tax Deduction records.

    final DetermineEligibility detetmineEligibility =
      DetermineEligibilityFactory.newInstance();
    detetmineEligibility.assessBenefitCase(ticketID, inst_data_id, flag);

    final CaseHeader caseHeader = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = wmInstanceDataDtls.caseID;
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
        // if the case determination is eligible, then create the claim
        // established notification
        communicationImpl.createClaimEstablishedNotification(
          caseHeaderDtls.concernRoleID, caseHeaderKey.caseID);

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

  /**
   * This method will create the case deduction item for the VTW, Federal or
   * Prov Tax deduction items.
   *
   * @param caseID
   * @throws AppException
   * @throws InformationalException
   */
  private void createDeductionsForCase(final long caseID)
    throws AppException, InformationalException {

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseID;
    this.maintainCaseDeductions.syncBenefitCaseVTWDeductions(caseHeaderKey);

    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = caseID;
    // Create Federal and Provincial Tax deductions
    this.maintainCaseDeductions.createTaxDeductions(caseIDKey);
  }
}
