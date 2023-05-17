package curam.ca.gc.bdm.facade.bdmcaseurgentflag.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetailsList;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagIDKey;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagInitialDetails;
import curam.ca.gc.bdm.sl.bdmcaseurgentflag.impl.BDMMaintainCaseUrgentFlag;
import curam.core.facade.struct.CaseIDDetails;
import curam.core.fact.UsersFactory;
import curam.core.struct.UsersDtls;
import curam.core.struct.UsersKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import java.util.Iterator;

public class BDMCaseUrgentFlag
  extends curam.ca.gc.bdm.facade.bdmcaseurgentflag.base.BDMCaseUrgentFlag {

  @Inject
  protected BDMMaintainCaseUrgentFlag bmdMaintainCaseUrgentFlag;

  public BDMCaseUrgentFlag() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * List current case urgent flags
   * V 1.1 Altered to display Created Full Name for Bug 11395 by Siva 01/13/2022
   * V 1.2 Altered - Task 17910 - Method name is changed to meaningful and self
   * explanatory based on the TDD review from "readInitialDetails" to
   * "setCurrentDateAsDefaultStartDate" by Siva on 03/09/2022
   */
  @Override
  public BDMCaseUrgentFlagDetailsList listCurrentUrgentFlags(
    final CaseIDDetails key) throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetailsList bdmCaseUrgentFlagDetailsList =
      bmdMaintainCaseUrgentFlag.listCurrentUrgentFlags(key);
    // START - Bug 11395
    BDMCaseUrgentFlagDetails bdmCaseUrgentFlagDetails = null;
    UsersKey userKey = null;

    final Iterator<BDMCaseUrgentFlagDetails> it =
      bdmCaseUrgentFlagDetailsList.dtls.iterator();
    while (it.hasNext()) {

      bdmCaseUrgentFlagDetails = new BDMCaseUrgentFlagDetails();
      bdmCaseUrgentFlagDetails = it.next();

      userKey = new UsersKey();
      userKey.userName = bdmCaseUrgentFlagDetails.createdBy;

      final UsersDtls usersDtls = UsersFactory.newInstance().read(userKey);
      bdmCaseUrgentFlagDetails.createdByFullName = usersDtls.fullName;
    }
    // END - Bug 11395
    return bdmCaseUrgentFlagDetailsList;
  }

  /**
   * List previous case urgent flags
   */
  @Override
  public BDMCaseUrgentFlagDetailsList listPreviousUrgentFlags(
    final CaseIDDetails key) throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetailsList bdmCaseUrgentFlagDetailsList =
      bmdMaintainCaseUrgentFlag.listPreviousUrgentFlags(key);

    BDMCaseUrgentFlagDetails bdmCaseUrgentFlagDetails = null;
    UsersKey userKey = null;

    final Iterator<BDMCaseUrgentFlagDetails> it =
      bdmCaseUrgentFlagDetailsList.dtls.iterator();
    while (it.hasNext()) {

      bdmCaseUrgentFlagDetails = new BDMCaseUrgentFlagDetails();
      bdmCaseUrgentFlagDetails = it.next();

      userKey = new UsersKey();
      userKey.userName = bdmCaseUrgentFlagDetails.createdBy;

      final UsersDtls usersDtls = UsersFactory.newInstance().read(userKey);
      bdmCaseUrgentFlagDetails.createdByFullName = usersDtls.fullName;
    }

    return bdmCaseUrgentFlagDetailsList;
  }

  /**
   * Read case urgent flag
   */
  @Override
  public BDMCaseUrgentFlagDetails
    readCaseUrgentFlag(final BDMCaseUrgentFlagIDKey key)
      throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetails bdmCaseUrgentFlagDetails =
      bmdMaintainCaseUrgentFlag.readCaseUrgentFlag(key);

    return bdmCaseUrgentFlagDetails;
  }

  /**
   * Modify case urgent flag
   */
  @Override
  public void modifyCaseUrgentFlag(final BDMCaseUrgentFlagDetails key)
    throws AppException, InformationalException {

    bmdMaintainCaseUrgentFlag.validateModifyCaseUrgentFlag(key);

    bmdMaintainCaseUrgentFlag.modifyCaseUrgentFlag(key);

  }

  /**
   * Create case urgent flag
   */
  @Override
  public void createCaseUrgentFlag(final BDMCaseUrgentFlagDetails key)
    throws AppException, InformationalException {

    bmdMaintainCaseUrgentFlag.validateCreateCaseUrgentFlag(key);

    bmdMaintainCaseUrgentFlag.createCaseUrgentFlag(key);

  }

  @Override
  public void deleteCaseUrgentFlag(final BDMCaseUrgentFlagIDKey key)
    throws AppException, InformationalException {

    bmdMaintainCaseUrgentFlag.deleteCaseUrgentFlag(key);

  }

  @Override
  public BDMCaseUrgentFlagInitialDetails setCurrentDateAsDefaultStartDate()
    throws AppException, InformationalException {

    final BDMCaseUrgentFlagInitialDetails bdmCaseUrgentFlagInitialDetails =
      new BDMCaseUrgentFlagInitialDetails();

    bdmCaseUrgentFlagInitialDetails.startDate = Date.getCurrentDate();

    return bdmCaseUrgentFlagInitialDetails;
  }

}
