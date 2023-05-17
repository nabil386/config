
package curam.ca.gc.bdm.sl.bdmcaseurgentflag.impl;

import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDtls;
import curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDtlsList;
import curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagOverlapKey;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetailsList;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagIDKey;
import curam.ca.gc.bdm.message.BDMCASEURGENTFLAG;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.struct.CaseIDDetails;
import curam.core.impl.CuramConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;

/**
 * <p>
 * Service layer operations used for maintaining case urgent flags.
 * </p>
 */
public class BDMMaintainCaseUrgentFlag implements
  curam.ca.gc.bdm.sl.bdmcaseurgentflag.intf.BDMMaintainCaseUrgentFlag {

  @Override
  public BDMCaseUrgentFlagDetailsList listCurrentUrgentFlags(
    final CaseIDDetails key) throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetailsList returnList =
      new BDMCaseUrgentFlagDetailsList();
    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.intf.BDMCaseUrgentFlag enBDMCaseUrgentFlag =
      curam.ca.gc.bdm.entity.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory
        .newInstance();

    final curam.core.struct.CaseIDDetails enKey =
      new curam.core.struct.CaseIDDetails();
    enKey.assign(key);

    final BDMCaseUrgentFlagDtlsList bdmCaseUrgentFlagDtlsList =
      enBDMCaseUrgentFlag.searchCurrentUrgentFlagByCaseID(enKey);

    returnList.assign(bdmCaseUrgentFlagDtlsList);

    return returnList;
  }

  @Override
  public BDMCaseUrgentFlagDetailsList listPreviousUrgentFlags(
    final CaseIDDetails key) throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetailsList returnList =
      new BDMCaseUrgentFlagDetailsList();
    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.intf.BDMCaseUrgentFlag enBDMCaseUrgentFlag =
      curam.ca.gc.bdm.entity.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory
        .newInstance();

    final curam.core.struct.CaseIDDetails enKey =
      new curam.core.struct.CaseIDDetails();
    enKey.assign(key);

    final BDMCaseUrgentFlagDtlsList bdmCaseUrgentFlagDtlsList =
      enBDMCaseUrgentFlag.searchPreviousUrgentFlagByCaseID(enKey);

    returnList.assign(bdmCaseUrgentFlagDtlsList);

    return returnList;
  }

  @Override
  public BDMCaseUrgentFlagDetails
    readCaseUrgentFlag(final BDMCaseUrgentFlagIDKey key)
      throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetails returnDetails =
      new BDMCaseUrgentFlagDetails();
    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.intf.BDMCaseUrgentFlag enBDMCaseUrgentFlag =
      curam.ca.gc.bdm.entity.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory
        .newInstance();

    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagKey enKey =
      new curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagKey();
    enKey.bdmCaseUrgentFlagID = key.bdmCaseUrgentFlagID;

    final BDMCaseUrgentFlagDtls bdmCaseUrgentFlagDtls =
      enBDMCaseUrgentFlag.read(enKey);

    returnDetails.assign(bdmCaseUrgentFlagDtls);

    return returnDetails;
  }

  @Override
  public void modifyCaseUrgentFlag(final BDMCaseUrgentFlagDetails key)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.intf.BDMCaseUrgentFlag enBDMCaseUrgentFlag =
      curam.ca.gc.bdm.entity.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory
        .newInstance();

    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagKey enKey =
      new curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagKey();
    enKey.bdmCaseUrgentFlagID = key.bdmCaseUrgentFlagID;

    final BDMCaseUrgentFlagDtls bdmCaseUrgentFlagDtls =
      enBDMCaseUrgentFlag.read(enKey);

    bdmCaseUrgentFlagDtls.endDate = key.endDate;

    enBDMCaseUrgentFlag.modify(enKey, bdmCaseUrgentFlagDtls);

  }

  @Override
  public void createCaseUrgentFlag(final BDMCaseUrgentFlagDetails key)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.intf.BDMCaseUrgentFlag enBDMCaseUrgentFlag =
      curam.ca.gc.bdm.entity.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory
        .newInstance();

    final BDMCaseUrgentFlagDtls bdmCaseUrgentFlagDtls =
      new BDMCaseUrgentFlagDtls();

    bdmCaseUrgentFlagDtls.bdmCaseUrgentFlagID = UniqueID.nextUniqueID();
    bdmCaseUrgentFlagDtls.caseID = key.caseID;
    bdmCaseUrgentFlagDtls.recordStatus = RECORDSTATUS.NORMAL;
    bdmCaseUrgentFlagDtls.startDate = key.startDate;
    bdmCaseUrgentFlagDtls.endDate = key.endDate;
    bdmCaseUrgentFlagDtls.type = key.type;
    bdmCaseUrgentFlagDtls.versionNo = 1;

    enBDMCaseUrgentFlag.insert(bdmCaseUrgentFlagDtls);

  }

  /**
   * Validate create case urgent flags
   */
  @Override
  public void validateCreateCaseUrgentFlag(final BDMCaseUrgentFlagDetails key)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (key.startDate.after(Date.getCurrentDate())) {
      final AppException exception = new AppException(
        BDMCASEURGENTFLAG.ERR_CASEURGENTFLAG_XFV_START_DATE_GREATER_THAN_CURRENT_DATE);
      exception.arg(key.startDate);

      informationalManager.addInformationalMsg(exception, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (!key.endDate.isZero() && key.endDate.before(key.startDate)) {
      final AppException exception = new AppException(
        BDMCASEURGENTFLAG.ERR_CASEURGENTFLAG_XFV_END_DATE_EARLIER_THAN_START_DATE);
      exception.arg(key.endDate);
      exception.arg(key.startDate);

      informationalManager.addInformationalMsg(exception, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (!key.endDate.isZero() && key.endDate.before(Date.getCurrentDate())) {
      final AppException exception = new AppException(
        BDMCASEURGENTFLAG.ERR_CASEURGENTFLAG_XFV_END_DATE_EARLIER_THAN_CURRENT_DATE);
      exception.arg(key.endDate);

      informationalManager.addInformationalMsg(exception, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (!key.endDate.isZero() && key.endDate.after(Date.getCurrentDate())) {
      final AppException exception = new AppException(
        BDMCASEURGENTFLAG.ERR_CASEURGENTFLAG_XFV_END_DATE_GREATER_THAN_CURRENT_DATE);
      exception.arg(key.endDate);

      informationalManager.addInformationalMsg(exception, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.intf.BDMCaseUrgentFlag enBDMCaseUrgentFlag =
      curam.ca.gc.bdm.entity.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory
        .newInstance();

    final BDMCaseUrgentFlagOverlapKey bdmCaseUrgentFlagOverlapKey =
      new BDMCaseUrgentFlagOverlapKey();
    bdmCaseUrgentFlagOverlapKey.caseID = key.caseID;
    bdmCaseUrgentFlagOverlapKey.type = key.type;
    bdmCaseUrgentFlagOverlapKey.startDate = key.startDate;
    if (!key.endDate.isZero()) {
      bdmCaseUrgentFlagOverlapKey.endDate = key.endDate;
    } else {
      bdmCaseUrgentFlagOverlapKey.endDate = Date.getDate("99991231");
    }
    final BDMCaseUrgentFlagDtlsList bdmCaseUrgentFlagDtlsList =
      enBDMCaseUrgentFlag
        .searchOverlapByCaseIDTypeDates(bdmCaseUrgentFlagOverlapKey);

    if (bdmCaseUrgentFlagDtlsList.dtls.size() > 0) {
      final AppException exception = new AppException(
        BDMCASEURGENTFLAG.ERR_CASEURGENTFLAG_OVERLAPS_IN_SPECIFIED_DATE_RANGE);
      exception.arg(CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        key.type, TransactionInfo.getProgramLocale()));

      informationalManager.addInformationalMsg(exception, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

  }

  /**
   * Validate modify case urgent flags
   */
  @Override
  public void validateModifyCaseUrgentFlag(final BDMCaseUrgentFlagDetails key)
    throws AppException, InformationalException {

    boolean overlapInd = false;
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (!key.endDate.isZero() && key.endDate.before(key.startDate)) {
      final AppException exception = new AppException(
        BDMCASEURGENTFLAG.ERR_CASEURGENTFLAG_XFV_END_DATE_EARLIER_THAN_START_DATE);
      exception.arg(key.endDate);
      exception.arg(key.startDate);

      informationalManager.addInformationalMsg(exception, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (!key.endDate.isZero() && key.endDate.before(Date.getCurrentDate())) {
      final AppException exception = new AppException(
        BDMCASEURGENTFLAG.ERR_CASEURGENTFLAG_XFV_END_DATE_EARLIER_THAN_CURRENT_DATE);
      exception.arg(key.endDate);

      informationalManager.addInformationalMsg(exception, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (!key.endDate.isZero() && key.endDate.after(Date.getCurrentDate())) {
      final AppException exception = new AppException(
        BDMCASEURGENTFLAG.ERR_CASEURGENTFLAG_XFV_END_DATE_GREATER_THAN_CURRENT_DATE);
      exception.arg(key.endDate);

      informationalManager.addInformationalMsg(exception, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.intf.BDMCaseUrgentFlag enBDMCaseUrgentFlag =
      curam.ca.gc.bdm.entity.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory
        .newInstance();

    final BDMCaseUrgentFlagOverlapKey bdmCaseUrgentFlagOverlapKey =
      new BDMCaseUrgentFlagOverlapKey();
    bdmCaseUrgentFlagOverlapKey.caseID = key.caseID;
    bdmCaseUrgentFlagOverlapKey.type = key.type;
    bdmCaseUrgentFlagOverlapKey.startDate = key.startDate;
    if (!key.endDate.isZero()) {
      bdmCaseUrgentFlagOverlapKey.endDate = key.endDate;
    } else {
      bdmCaseUrgentFlagOverlapKey.endDate = Date.getDate("99991231");
    }
    final BDMCaseUrgentFlagDtlsList bdmCaseUrgentFlagDtlsList =
      enBDMCaseUrgentFlag
        .searchOverlapByCaseIDTypeDates(bdmCaseUrgentFlagOverlapKey);

    BDMCaseUrgentFlagDtls bdmCaseUrgentFlagDtls;
    for (int i = 0; i < bdmCaseUrgentFlagDtlsList.dtls.size(); i++) {
      bdmCaseUrgentFlagDtls = new BDMCaseUrgentFlagDtls();
      bdmCaseUrgentFlagDtls = bdmCaseUrgentFlagDtlsList.dtls.get(i);
      if (bdmCaseUrgentFlagDtls.bdmCaseUrgentFlagID != key.bdmCaseUrgentFlagID) {
        overlapInd = true;
        break;
      }
    }

    if (overlapInd) {

      final AppException exception = new AppException(
        BDMCASEURGENTFLAG.ERR_CASEURGENTFLAG_OVERLAPS_IN_SPECIFIED_DATE_RANGE);
      exception.arg(CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        key.type, TransactionInfo.getProgramLocale()));

      informationalManager.addInformationalMsg(exception, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

  }

  @Override
  public void deleteCaseUrgentFlag(final BDMCaseUrgentFlagIDKey key)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.intf.BDMCaseUrgentFlag enBDMCaseUrgentFlag =
      curam.ca.gc.bdm.entity.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory
        .newInstance();

    final curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagKey enKey =
      new curam.ca.gc.bdm.entity.bdmcaseurgentflag.struct.BDMCaseUrgentFlagKey();
    enKey.bdmCaseUrgentFlagID = key.bdmCaseUrgentFlagID;

    final BDMCaseUrgentFlagDtls bdmCaseUrgentFlagDtls =
      enBDMCaseUrgentFlag.read(enKey);

    bdmCaseUrgentFlagDtls.recordStatus = RECORDSTATUS.CANCELLED;
    TransactionInfo.getInfo();
    bdmCaseUrgentFlagDtls.lastUpdatedBy = TransactionInfo.getProgramUser();
    bdmCaseUrgentFlagDtls.lastUpdatedOn = DateTime.getCurrentDateTime();

    enBDMCaseUrgentFlag.modify(enKey, bdmCaseUrgentFlagDtls);

  }

}
