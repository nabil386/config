package curam.ca.gc.bdm.sl.eligibility.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationImpl;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.DelayedEligibilityAssessmentFactory;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.intf.CaseHeader;
import curam.core.intf.DelayedEligibilityAssessment;
import curam.core.intf.WMInstanceData;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.WMInstanceDataDtls;
import curam.core.struct.WMInstanceDataKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;

public class BDMDelayedEligibilityAssessment extends
  curam.ca.gc.bdm.sl.eligibility.base.BDMDelayedEligibilityAssessment {

  @Inject
  BDMCommunicationImpl communicationImpl;

  public BDMDelayedEligibilityAssessment() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void reassessCase(final long ticketID, final long inst_data_id,
    final boolean flag) throws AppException, InformationalException {

    final DelayedEligibilityAssessment delayedEligibilityAssessmentObj =
      DelayedEligibilityAssessmentFactory.newInstance();

    delayedEligibilityAssessmentObj.reassessCase(ticketID, inst_data_id,
      flag);

    final WMInstanceDataKey wmInstanceDataKey = new WMInstanceDataKey();
    wmInstanceDataKey.wm_instDataID = inst_data_id;

    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();

    final WMInstanceDataDtls wmInstanceDataDtls =
      wmInstanceDataObj.read(wmInstanceDataKey);

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
        communicationImpl.createDisentitlementLetterByCaseID(
          caseHeaderKey.caseID, caseHeaderDtls.concernRoleID);
      }

      // Task-16432 If applicable, create Dependent Benefit Entitlement Issues
      // letter
      communicationImpl
        .createDependentBenefitEntitlementIssuesLetter(caseHeaderKey.caseID);

      // System.out.println("BDMDelayedEligi --reassessCase");
      communicationImpl.createRecalculationLetter(caseHeaderKey.caseID);
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(e.getLocalizedMessage());
    }
  }

}
