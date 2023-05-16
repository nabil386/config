package curam.ca.gc.bdmoas.facade.product.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASBenefitStatus;
import curam.ca.gc.bdmoas.facade.product.struct.BDMOASProductDisplayContextDetails;
import curam.core.facade.infrastructure.assessment.fact.CaseDeterminationFactory;
import curam.core.facade.infrastructure.assessment.intf.CaseDetermination;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationDecisionDetailsList;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationDecisionListDetails;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationHeaderDetails;
import curam.core.facade.infrastructure.assessment.struct.CaseDeterminationIDAndDecisionDateKey;
import curam.core.facade.infrastructure.assessment.struct.CaseIDDeterminationIDKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.intf.CaseHeader;
import curam.core.intf.ConcernRole;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

public class BDMOASProductDisplay
  extends curam.ca.gc.bdmoas.facade.product.base.BDMOASProductDisplay {

  private static final String PERIOD_SEPARATOR = " - ";

  @Override
  public BDMOASProductDisplayContextDetails
    getContextDetails(final CaseDeterminationIDAndDecisionDateKey key)
      throws AppException, InformationalException {

    final CaseDetermination caseDetermination =
      CaseDeterminationFactory.newInstance();
    final CaseDeterminationHeaderDetails caseDeterminationHeaderDetails =
      caseDetermination.viewDecisionHeaderDetails(key);

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseDeterminationHeaderDetails.caseID;
    final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);

    final ConcernRole concernRole = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = caseHeaderDtls.concernRoleID;
    final ConcernRoleDtls concernRoleDtls = concernRole.read(concernRoleKey);

    final BDMOASProductDisplayContextDetails contextDetails =
      new BDMOASProductDisplayContextDetails();

    contextDetails.concernRoleID = concernRoleDtls.concernRoleID;
    contextDetails.concernRoleName = concernRoleDtls.concernRoleName;
    contextDetails.caseReference = caseHeaderDtls.caseReference;
    contextDetails.productDeliveryType =
      caseDeterminationHeaderDetails.productDeliveryType;

    // TODO: Add back benefit status logic once display rule set is merged.
    contextDetails.displayBenefitStatus = true;
    contextDetails.benefitStatus = BDMOASBenefitStatus.ACTIVE;

    this.populateDateFields(contextDetails, key,
      caseDeterminationHeaderDetails.caseID);

    return contextDetails;

  }

  private void populateDateFields(
    final BDMOASProductDisplayContextDetails contextDetails,
    final CaseDeterminationIDAndDecisionDateKey key, final long caseID)
    throws AppException, InformationalException {

    final CaseIDDeterminationIDKey decisionKey =
      new CaseIDDeterminationIDKey();
    decisionKey.determinationID = key.determinationID;
    decisionKey.caseID = caseID;

    final CaseDetermination caseDetermination =
      CaseDeterminationFactory.newInstance();

    final CaseDeterminationDecisionDetailsList list =
      caseDetermination.listDecisionPeriodsForDetermination(decisionKey);

    Date previousDisplayDate = Date.kZeroDate;
    Date previousToDate = Date.kZeroDate;
    Date nextDisplayDate = Date.fromISO8601("99991231");
    Date nextToDate = Date.kZeroDate;

    for (final CaseDeterminationDecisionListDetails details : list.dtls) {

      if (key.decisionDate.equals(details.decisionFromDate)) {
        contextDetails.currentPeriod =
          key.decisionDate + PERIOD_SEPARATOR + details.decisionToDate;
      }

      if (details.decisionFromDate.before(key.decisionDate)
        && details.decisionFromDate.after(previousDisplayDate)) {
        previousDisplayDate = details.decisionFromDate;
        previousToDate = details.decisionToDate;
      }

      if (details.decisionFromDate.after(key.decisionDate)
        && details.decisionFromDate.before(nextDisplayDate)) {
        nextDisplayDate = details.decisionFromDate;
        nextToDate = details.decisionToDate;
      }

    }

    contextDetails.hasPreviousPeriod =
      !Date.kZeroDate.equals(previousDisplayDate);
    contextDetails.hasNextPeriod =
      !Date.fromISO8601("99991231").equals(nextDisplayDate);

    if (contextDetails.hasPreviousPeriod) {
      contextDetails.previousPeriod =
        previousDisplayDate + PERIOD_SEPARATOR + previousToDate;
      contextDetails.previousDisplayDate = previousDisplayDate;
    }

    if (contextDetails.hasNextPeriod) {
      contextDetails.nextPeriod =
        nextDisplayDate + PERIOD_SEPARATOR + nextToDate;
      contextDetails.nextDisplayDate = nextDisplayDate;
    } else {
      contextDetails.nextDisplayDate = Date.kZeroDate;
    }

  }

}
