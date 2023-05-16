package curam.ca.gc.bdm.sl.commstatushistory.impl;

import curam.ca.gc.bdm.entity.communication.fact.BDMCommStatusHistoryFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMCommStatusHistory;
import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryDtlsList;
import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryKeyStruct1;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.UniqueIDFactory;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.InformationalException;
import curam.util.exception.MultipleRecordException;
import curam.util.exception.RecordNotFoundException;
import curam.util.exception.UnimplementedException;
import curam.util.persistence.EntityAdapter;
import curam.util.type.DateTime;

public class BDMCommStatusHistoryAdapter
  implements EntityAdapter<Long, BDMCommStatusHistoryDtls> {

  private BDMCommStatusHistory _wrappedEntity;

  private BDMCommStatusHistory getWrappedEntity() {

    if (this._wrappedEntity == null) {
      this._wrappedEntity = BDMCommStatusHistoryFactory.newInstance();
    }
    return this._wrappedEntity;
  }

  @Override
  public Class<BDMCommStatusHistoryDtls> getDtlsClass() {

    return BDMCommStatusHistoryDtls.class;
  }

  @Override
  public Long getKey(final BDMCommStatusHistoryDtls dtls) {

    return dtls.BDMCommStatusHistoryID;
  }

  @Override
  public int getVersionNo(final BDMCommStatusHistoryDtls dtls)
    throws UnimplementedException {

    return 0;
  }

  @Override
  public void insert(final BDMCommStatusHistoryDtls dtls) {

    try {
      this.getWrappedEntity().insert(dtls);
    } catch (final AppRuntimeException are) {
      throw are;
    } catch (final Exception e) {
      throw new AppRuntimeException(e);
    }

  }

  @Override
  public void modifyNoLock(final BDMCommStatusHistoryDtls dtls) {

    // N/A
  }

  @Override
  public void modifyWithOptimisticLock(final BDMCommStatusHistoryDtls dtls,
    final int versionNo) throws UnimplementedException {

    // N/A
  }

  @Override
  public BDMCommStatusHistoryDtls newDetails() {

    return new BDMCommStatusHistoryDtls();
  }

  @Override
  public BDMCommStatusHistoryDtls read(final Long id,
    final boolean forUpdate) {

    final BDMCommStatusHistoryKey key = new BDMCommStatusHistoryKey();
    key.BDMCommStatusHistoryID = id;
    try {
      return this.getWrappedEntity().read(key, forUpdate);
    } catch (final AppRuntimeException var5) {
      throw var5;
    } catch (final Exception var6) {
      throw new AppRuntimeException(var6);
    }
  }

  @Override
  public void remove(final Long id) {

    // N/A
  }

  public BDMCommStatusHistoryDtlsList readmultiByCommunicationID(
    final Long communicationID) throws MultipleRecordException {

    final BDMCommStatusHistoryKeyStruct1 key =
      new BDMCommStatusHistoryKeyStruct1();
    key.communicationID = communicationID;

    try {
      return this.getWrappedEntity().readmultiByCommID(key);
    } catch (final RecordNotFoundException var4) {
      return null;
    } catch (final AppRuntimeException var5) {
      throw var5;
    } catch (final Exception var6) {
      throw new AppRuntimeException(var6);
    }
  }

  public BDMCommStatusHistoryDtlsList readmultiByCommunicationIDAndCTName(
    final Long communicationID, final String statusCTTableName)
    throws MultipleRecordException {

    final BDMCommStatusHistoryKeyStruct1 key =
      new BDMCommStatusHistoryKeyStruct1();
    key.communicationID = communicationID;

    final BDMCommStatusHistoryDtlsList dtlsList =
      this.readmultiByCommunicationID(communicationID);
    for (final BDMCommStatusHistoryDtls commStatusHistoryDtls : dtlsList.dtls
      .items()) {
      if (commStatusHistoryDtls.statusCTTableName.equals(statusCTTableName)) {
        continue;
      } else {
        dtlsList.dtls.remove(dtlsList);
      }
    }

    return dtlsList;

  }

  public BDMCommStatusHistoryDtls newDefaultCommStatusHistoryDetails() {

    final BDMCommStatusHistoryDtls returnDtls = this.newDetails();
    populateDefaultCommunicationStatusHistoryDetails(returnDtls);

    return returnDtls;
  }

  public BDMCommStatusHistoryDtls newDefaultInsertCommStatusHistoryDetails()
    throws AppException, InformationalException {

    final BDMCommStatusHistoryDtls returnDtls =
      this.newDefaultCommStatusHistoryDetails();

    returnDtls.BDMCommStatusHistoryID =
      UniqueIDFactory.newInstance().getNextID();

    return returnDtls;
  }

  public BDMCommStatusHistoryDtls populateDefaultCommunicationDetails(
    final BDMCommStatusHistoryDtls returnDtls) {

    returnDtls.recordStatus = RECORDSTATUS.NORMAL;
    returnDtls.statusDateTime = DateTime.getCurrentDateTime();

    return returnDtls;
  }

  public BDMCommStatusHistoryDtls
    populateDefaultCommunicationStatusHistoryDetails(
      final BDMCommStatusHistoryDtls returnDtls) {

    populateDefaultCommunicationDetails(returnDtls);
    returnDtls.statusCTTableName = COMMUNICATIONSTATUS.TABLENAME;
    return returnDtls;
  }

  public BDMCommStatusHistoryDtls
    populateDefaultCommunicationRecordStatusHistoryDetails(
      final BDMCommStatusHistoryDtls returnDtls) {

    populateDefaultCommunicationDetails(returnDtls);
    returnDtls.statusCTTableName = RECORDSTATUS.TABLENAME;
    return returnDtls;
  }

}
