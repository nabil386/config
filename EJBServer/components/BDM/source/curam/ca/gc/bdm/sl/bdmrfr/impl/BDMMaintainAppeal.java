package curam.ca.gc.bdm.sl.bdmrfr.impl;

import curam.appeal.sl.struct.AppealCaseKey;
import curam.appeal.sl.struct.ReadAppealDetails;
import curam.ca.gc.bdm.entity.fact.BDMAppealFactory;
import curam.ca.gc.bdm.entity.struct.BDMAppealDtls;
import curam.ca.gc.bdm.entity.struct.BDMAppealKey;
import curam.ca.gc.bdm.sl.bdmrfr.struct.BDMAppealDetails;
import curam.core.sl.struct.CaseIDKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMMaintainAppeal
  extends curam.ca.gc.bdm.sl.bdmrfr.base.BDMMaintainAppeal {

  @Override
  public void createBDMAppeal(final BDMAppealDtls details)
    throws AppException, InformationalException {

    BDMAppealFactory.newInstance().insert(details);
  }

  @Override
  public void modifyBDMAppeal(final BDMAppealDtls details)
    throws AppException, InformationalException {

    final BDMAppealKey key = new BDMAppealKey();
    key.appealID = details.appealID;

    BDMAppealFactory.newInstance().modify(key, details);
  }

  /**
   *
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMAppealDetails readByHearingCaseID(final CaseIDKey caseIDKey)
    throws AppException, InformationalException {

    final curam.appeal.sl.intf.Appeal appealObj =
      curam.appeal.sl.fact.AppealFactory.newInstance();

    final AppealCaseKey appealCaseKey = new AppealCaseKey();
    appealCaseKey.caseID = caseIDKey.caseID;

    final ReadAppealDetails appealDetails =
      appealObj.readAppealDetails(appealCaseKey);

    final BDMAppealKey bdmAppealKey = new BDMAppealKey();
    bdmAppealKey.appealID = appealDetails.appealReadSummaryDetails.appealID;

    final BDMAppealDtls bdmAppealDetails =
      BDMAppealFactory.newInstance().read(bdmAppealKey);

    final BDMAppealDetails details = new BDMAppealDetails();
    details.assign(bdmAppealDetails);

    return details;
  }
}
