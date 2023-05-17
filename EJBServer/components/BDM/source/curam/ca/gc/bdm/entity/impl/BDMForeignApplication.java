package curam.ca.gc.bdm.entity.impl;

import curam.ca.gc.bdm.entity.fact.BDMFAHistoryFactory;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryDtls;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.UniqueID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMForeignApplication
  extends curam.ca.gc.bdm.entity.base.BDMForeignApplication {

  final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();

  final curam.ca.gc.bdm.entity.intf.BDMFAHistory bdmfaHistory =
    BDMFAHistoryFactory.newInstance();

  BDMFAHistoryDtls bdmfaHistoryDtls = null;

  @Override
  protected void postinsert(final BDMForeignApplicationDtls details)
    throws AppException, InformationalException {

    bdmfaHistoryDtls = new BDMFAHistoryDtls();

    bdmfaHistoryDtls.assign(details);

    bdmfaHistoryDtls.fAppHistoryID = uniqueIDObj.getNextID();

    bdmfaHistory.insert(bdmfaHistoryDtls);

  }

  @Override
  protected void premodify(final BDMForeignApplicationKey key,
    final BDMForeignApplicationDtls details)
    throws AppException, InformationalException {

    final BDMForeignApplicationKey bdmfaKey = new BDMForeignApplicationKey();

    bdmfaKey.fApplicationID = details.fApplicationID;

    final BDMFAHistoryDtlsList bdmfaHistoryDtlsList =
      bdmfaHistory.readByFApplicationID(bdmfaKey);

    final int listSize = bdmfaHistoryDtlsList.dtls.size();

    boolean changeFoundInd = false;

    if (listSize > CuramConst.gkZero) {

      if (bdmfaHistoryDtlsList.dtls.item(0).bessInd != details.bessInd) {
        changeFoundInd = true;
      } else if (!bdmfaHistoryDtlsList.dtls.item(0).comments
        .equals(details.comments)) {
        changeFoundInd = true;
      } else if (!bdmfaHistoryDtlsList.dtls.item(0).createdBy
        .equals(details.createdBy)) {
        changeFoundInd = true;
      } else if (!bdmfaHistoryDtlsList.dtls.item(0).lastUpdatedBy
        .equals(details.lastUpdatedBy)) {
        changeFoundInd = true;
      } else if (!bdmfaHistoryDtlsList.dtls.item(0).faDeleteReason
        .equals(details.faDeleteReason)) {
        changeFoundInd = true;
      } else if (bdmfaHistoryDtlsList.dtls
        .item(0).fIdentifier != details.fIdentifier) {
        changeFoundInd = true;
      } else if (bdmfaHistoryDtlsList.dtls
        .item(0).fOfficeID != details.fOfficeID) {
        changeFoundInd = true;
      } else if (!bdmfaHistoryDtlsList.dtls.item(0).typeCode
        .equals(details.typeCode)) {
        changeFoundInd = true;
      } else if (!bdmfaHistoryDtlsList.dtls.item(0).faReferenceNumber
        .equals(details.faReferenceNumber)) {
        changeFoundInd = true;
      } else if (!bdmfaHistoryDtlsList.dtls.item(0).receiveDate
        .equals(details.receiveDate)) {
        changeFoundInd = true;
      } else if (!bdmfaHistoryDtlsList.dtls.item(0).recordStatus
        .equals(details.recordStatus)) {
        changeFoundInd = true;
      } else if (!bdmfaHistoryDtlsList.dtls.item(0).status
        .equals(details.status)) {
        changeFoundInd = true;
      }

    }

    if (changeFoundInd) {

      bdmfaHistoryDtls = new BDMFAHistoryDtls();

      bdmfaHistoryDtls.assign(details);

      bdmfaHistoryDtls.fAppHistoryID = uniqueIDObj.getNextID();

      bdmfaHistory.insert(bdmfaHistoryDtls);
    }

  }

}
