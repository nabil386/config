package curam.ca.gc.bdm.sl.commstatushistory.impl;

import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryDtls;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.UniqueIDFactory;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.StandardDAOImpl;
import curam.util.type.DateTime;
import java.util.ArrayList;
import java.util.List;

public class BDMCommStatusHistoryDAOImpl
  extends StandardDAOImpl<BDMCommStatusHistory, BDMCommStatusHistoryDtls>
  implements BDMCommStatusHistoryDAO {

  protected static final BDMCommStatusHistoryAdapter adapter =
    new BDMCommStatusHistoryAdapter();

  /**
   * Constructor for use only by Guice.
   */
  protected BDMCommStatusHistoryDAOImpl() {

    super(adapter, BDMCommStatusHistory.class);
  }

  @Override
  public List<BDMCommStatusHistoryDtls>
    listAllCommunicationStatusHistory(final long communicationID) {

    final List<BDMCommStatusHistoryDtls> statusHistoryList =
      new ArrayList<BDMCommStatusHistoryDtls>();

    for (final BDMCommStatusHistoryDtls bsmStatusHistoryDtls : adapter
      .readmultiByCommunicationID(communicationID).dtls.items()) {
      if (bsmStatusHistoryDtls.recordStatus.equals(RECORDSTATUS.NORMAL))
        statusHistoryList.add(bsmStatusHistoryDtls);
    }
    return statusHistoryList;
  }

  @Override
  public List<BDMCommStatusHistoryDtls>
    listCommunicationStatusHistory(final long communicationID) {

    final List<BDMCommStatusHistoryDtls> statusHistoryList =
      new ArrayList<BDMCommStatusHistoryDtls>();

    for (final BDMCommStatusHistoryDtls bsmStatusHistoryDtls : adapter
      .readmultiByCommunicationIDAndCTName(communicationID,
        COMMUNICATIONSTATUS.TABLENAME).dtls.items()) {
      if (bsmStatusHistoryDtls.recordStatus.equals(RECORDSTATUS.NORMAL))
        statusHistoryList.add(bsmStatusHistoryDtls);
    }
    return statusHistoryList;
  }

  @Override
  public List<BDMCommStatusHistoryDtls>
    listCommunicationRecordStatusHistory(final long communicationID) {

    final List<BDMCommStatusHistoryDtls> statusHistoryList =
      new ArrayList<BDMCommStatusHistoryDtls>();

    for (final BDMCommStatusHistoryDtls bsmStatusHistoryDtls : adapter
      .readmultiByCommunicationIDAndCTName(communicationID,
        RECORDSTATUS.TABLENAME).dtls.items()) {
      if (bsmStatusHistoryDtls.recordStatus.equals(RECORDSTATUS.NORMAL))
        statusHistoryList.add(bsmStatusHistoryDtls);
    }
    return statusHistoryList;
  }

  @Override
  public long createCommunicationStatusHistory(final long communicationID,
    final String newStatusCode, final String userName) {

    long statusHistoryID = 0l;

    final BDMCommStatusHistoryDtls statusHistoryDtls =
      new BDMCommStatusHistoryDtls();
    try {
      statusHistoryDtls.BDMCommStatusHistoryID =
        UniqueIDFactory.newInstance().getNextID();

    } catch (final AppException e) {
      e.printStackTrace();
    } catch (final InformationalException e) {
      e.printStackTrace();
    }
    if (statusHistoryDtls.BDMCommStatusHistoryID == 0l)
      return 0l;

    adapter
      .populateDefaultCommunicationStatusHistoryDetails(statusHistoryDtls);
    statusHistoryDtls.communicationID = communicationID;
    statusHistoryDtls.statusCode = newStatusCode;
    statusHistoryDtls.userName = userName;
    adapter.insert(statusHistoryDtls);

    statusHistoryID = statusHistoryDtls.BDMCommStatusHistoryID;

    return statusHistoryID;
  }

  @Override
  public long createCommunicationRecordStatusHistory(
    final long communicationID, final String newStatusCode,
    final String userName) {

    long statusHistoryID = 0l;

    final BDMCommStatusHistoryDtls statusHistoryDtls =
      new BDMCommStatusHistoryDtls();
    try {
      statusHistoryDtls.BDMCommStatusHistoryID =
        UniqueIDFactory.newInstance().getNextID();

    } catch (final AppException e) {
      e.printStackTrace();
    } catch (final InformationalException e) {
      e.printStackTrace();
    }
    if (statusHistoryDtls.BDMCommStatusHistoryID == 0l)
      return 0l;

    adapter.populateDefaultCommunicationRecordStatusHistoryDetails(
      statusHistoryDtls);
    statusHistoryDtls.statusDateTime = DateTime.getCurrentDateTime();
    statusHistoryDtls.communicationID = communicationID;
    statusHistoryDtls.statusCode = newStatusCode;
    statusHistoryDtls.userName = userName;
    adapter.insert(statusHistoryDtls);

    statusHistoryID = statusHistoryDtls.BDMCommStatusHistoryID;

    return statusHistoryID;
  }
}
