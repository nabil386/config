package curam.ca.gc.bdm.facade.appeals.impl;

import curam.appeal.facade.fact.AppealFactory;
import curam.appeal.facade.struct.AppealAllRelatedDetailsList;
import curam.appeal.facade.struct.AppealCaseKey;
import curam.appeal.facade.struct.AppealCaseKey_fo;
import curam.appeal.facade.struct.AppealListCombinedDetails;
import curam.appeal.facade.struct.DecisionAndAttachmentListDetailsForIC;
import curam.appeal.sl.struct.AppealAllRelatedDetails;
import curam.appeal.sl.struct.RelatedAppealDetails;
import curam.ca.gc.bdm.facade.appeals.struct.BDMAppealListCombinedDetails;
import curam.ca.gc.bdm.facade.appeals.struct.BDMRelatedAppealDetails;
import curam.codetable.APPEALCASERESOLUTION;
import curam.codetable.HEARINGDECISIONRESOLUTION;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMAppeal extends curam.ca.gc.bdm.facade.appeals.base.BDMAppeal {

  @Override
  public DecisionAndAttachmentListDetailsForIC
    readDecisionAndAttachmentListForIC(final AppealCaseKey key)
      throws AppException, InformationalException {

    final DecisionAndAttachmentListDetailsForIC decisionAndAttachmentListDetailsForIC =
      AppealFactory.newInstance().readDecisionAndAttachmentListForIC(key);

    final AppealAllRelatedDetailsList list =
      AppealFactory.newInstance().listAllRelatedByAppealCase(key);

    for (final AppealAllRelatedDetails appealAllRelatedDetails : list.relatedDetails.dtls) {
      // Loop once as only one appeal item is expected

      decisionAndAttachmentListDetailsForIC.decisionDetails.decisionResolutionCode =
        getEquivalentResolutionCode(appealAllRelatedDetails.resolutionCode);

      break;

    }

    return decisionAndAttachmentListDetailsForIC;
  }

  private String
    getEquivalentResolutionCode(final String appealResolutionCode) {

    if (HEARINGDECISIONRESOLUTION.ACCEPTED.equals(appealResolutionCode)) {
      return APPEALCASERESOLUTION.ACCEPTED;
    } else if (HEARINGDECISIONRESOLUTION.NOTDECIDED
      .equals(appealResolutionCode)) {
      return APPEALCASERESOLUTION.NOTDECIDED;
    } else if (HEARINGDECISIONRESOLUTION.APPROVEDINPART
      .equals(appealResolutionCode)) {
      return APPEALCASERESOLUTION.ACCEPTEDINPART;
    } else if (HEARINGDECISIONRESOLUTION.DECISIONMAINTAINED
      .equals(appealResolutionCode)) {
      return APPEALCASERESOLUTION.DECISIONMAINTAINED;
    } else if (HEARINGDECISIONRESOLUTION.NOTPERFORMED
      .equals(appealResolutionCode)) {
      return APPEALCASERESOLUTION.NOTPERFORMED;
    } else if (HEARINGDECISIONRESOLUTION.REJECTED
      .equals(appealResolutionCode)) {
      return APPEALCASERESOLUTION.REJECTED;
    } else {
      return APPEALCASERESOLUTION.NOTDECIDED;
    }
  }

  /**
   * This method calls ootb facade and sets decision resolution code to display
   * on the screen.
   *
   * @param key Appeal Case Key contains case ID
   * @return BDMAppealListCombinedDetails List of appeal details
   * @throws AppException Generic Exception signature
   * @throws InformationalException Generic Exception signature
   */
  @Override
  public BDMAppealListCombinedDetails listAppealCombinedByCase(
    final AppealCaseKey_fo key) throws AppException, InformationalException {

    final BDMAppealListCombinedDetails bdmAppealListDetails =
      new BDMAppealListCombinedDetails();
    final AppealListCombinedDetails appealListDetails =
      AppealFactory.newInstance().listAppealCombinedByCase(key);

    for (final RelatedAppealDetails appealDetails : appealListDetails.listAppealCaseDetails.relatedAppealDetailsList.relatedAppealDetails) {
      final BDMRelatedAppealDetails bdmAppealDetails =
        new BDMRelatedAppealDetails();
      bdmAppealDetails.assign(appealDetails);

      final AppealCaseKey caseKey = new AppealCaseKey();
      caseKey.appealCaseKey.caseID = appealDetails.appealCaseID;
      final AppealAllRelatedDetailsList list =
        AppealFactory.newInstance().listAllRelatedByAppealCase(caseKey);

      bdmAppealDetails.decisionResolutionCode = getEquivalentResolutionCode(
        list.relatedDetails.dtls.item(0).resolutionCode);
      bdmAppealListDetails.dtls.add(bdmAppealDetails);
    }

    return bdmAppealListDetails;
  }

}
