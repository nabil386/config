package curam.ca.gc.bdm.facade.bdmrfr.impl;

import curam.appeal.facade.struct.HearingCaseModifyKey;
import curam.ca.gc.bdm.entity.fact.BDMAppealFactory;
import curam.ca.gc.bdm.entity.struct.BDMAppealDtls;
import curam.ca.gc.bdm.entity.struct.BDMAppealKey;
import curam.ca.gc.bdm.facade.bdmrfr.struct.BDMHearingCaseModifyDetails;
import curam.ca.gc.bdm.facade.bdmrfr.struct.BDMModifyHearingCaseDetails;
import curam.ca.gc.bdm.message.BDMHEARINGCASE;
import curam.ca.gc.bdm.sl.bdmrfr.fact.BDMMaintainAppealFactory;
import curam.ca.gc.bdm.sl.bdmrfr.struct.BDMAppealDetails;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.intf.CaseHeader;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseStartDate;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;

public class BDMHearingCase
  extends curam.ca.gc.bdm.facade.bdmrfr.base.BDMHearingCase {

  @Override
  public BDMAppealDetails readBDMAppealDetailsByHearingCase(
    final curam.core.sl.struct.CaseIDKey caseIDKey)
    throws AppException, InformationalException {

    final BDMAppealDetails details =
      BDMMaintainAppealFactory.newInstance().readByHearingCaseID(caseIDKey);

    return details;
  }

  /**
   * Method to modify a hearing case
   *
   * @param details
   * The details of the hearing case being modified
   */
  @Override
  public void modifyHearingCase(final BDMModifyHearingCaseDetails details)
    throws AppException, InformationalException {

    validateModifyHearingCase(details);

    details.hearingCaseDetails.appealModifyDetails.appealModifyReadDetails.receivedDate =
      details.dateRFRReceived;

    // HearingCase object
    final curam.appeal.sl.intf.HearingCase hearingCaseObj =
      curam.appeal.sl.fact.HearingCaseFactory.newInstance();

    // register the security implementation
    SecurityImplementationFactory.register();

    // Update the hearing case
    hearingCaseObj
      .modifyHearingCase(details.hearingCaseDetails.appealModifyDetails);

    // modify the BDM Appeal details
    final BDMAppealKey bdmAppealKey = new BDMAppealKey();
    bdmAppealKey.appealID =
      details.hearingCaseDetails.appealModifyDetails.appealModifyReadDetails.appealID;

    final BDMAppealDtls bdmAppealDetails =
      BDMAppealFactory.newInstance().read(bdmAppealKey);

    bdmAppealDetails.admsReference = details.admsReference;
    bdmAppealDetails.dateDecisionDisputed = details.dateDecisionDisputed;
    bdmAppealDetails.dateRFRReceived = details.dateRFRReceived;

    BDMAppealFactory.newInstance().modify(bdmAppealKey, bdmAppealDetails);

  }

  /**
   * Validates the hearing case updated Date of Decision Disputed and Date
   * Request for Reconsideration Received with the case start date.
   *
   * @param details hearing case details
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  private void
    validateModifyHearingCase(final BDMModifyHearingCaseDetails details)
      throws AppException, InformationalException {

    // caseHeader variables
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

    caseHeaderKey.caseID =
      details.hearingCaseDetails.appealModifyDetails.appealModifyReadDetails.caseID;

    final CaseStartDate caseStartDate =
      caseHeaderObj.readStartDate(caseHeaderKey);

    final InformationalManager informationalManager =
      new InformationalManager();

    // validation Date of Decision Disputed cannot be after the hearing case
    // start date.
    if (details.dateDecisionDisputed.after(caseStartDate.startDate)) {

      informationalManager.addInformationalMsg(new AppException(
        BDMHEARINGCASE.ERR_FV_DATE_DECISION_DISPUTED_AFTER_HEARNIG_CASE_STARTDATE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }

    // validation Date Request for Reconsideration Received cannot be after the
    // hearing case start date.
    if (details.dateRFRReceived.after(caseStartDate.startDate)) {

      informationalManager.addInformationalMsg(new AppException(
        BDMHEARINGCASE.ERR_FV_DATE_RFR_RECEIVED_AFTER_HEARNIG_CASE_STARTDATE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }

    informationalManager.failOperation();
  }

  /**
   * Method to read hearing case details for modification
   *
   * @param key
   * The key of the hearing case being modified
   *
   * @return The hearing case details
   */
  @Override
  public BDMHearingCaseModifyDetails
    readHearingCaseForModify(final HearingCaseModifyKey key)
      throws AppException, InformationalException {

    // Return details
    final BDMHearingCaseModifyDetails hearingCaseModifyDetails =
      new BDMHearingCaseModifyDetails();

    // HearingCase object
    final curam.appeal.sl.intf.HearingCase hearingCaseObj =
      curam.appeal.sl.fact.HearingCaseFactory.newInstance();

    // register the security implementation
    SecurityImplementationFactory.register();

    // Read the hearing case
    hearingCaseModifyDetails.dtls.hearingCaseModifyDetails =
      hearingCaseObj.readForModify(key.hearingCaseModifyKey);

    // read the BDM Appeals details for modify
    final BDMAppealKey bdmAppealKey = new BDMAppealKey();
    bdmAppealKey.appealID =
      hearingCaseModifyDetails.dtls.hearingCaseModifyDetails.appealModifyDetails.appealModifyReadDetails.appealID;

    final BDMAppealDtls bdmAppealDetails =
      BDMAppealFactory.newInstance().read(bdmAppealKey);

    hearingCaseModifyDetails.admsReference = bdmAppealDetails.admsReference;
    hearingCaseModifyDetails.dateDecisionDisputed =
      bdmAppealDetails.dateDecisionDisputed;
    hearingCaseModifyDetails.dateRFRReceived =
      bdmAppealDetails.dateRFRReceived;
    hearingCaseModifyDetails.appealID = bdmAppealDetails.appealID;

    return hearingCaseModifyDetails;
  }
}
