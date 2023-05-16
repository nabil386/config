package curam.ca.gc.bdm.sl.commstatushistory.impl;

import com.google.inject.ImplementedBy;
import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryDtls;
import java.util.List;

/**
 * Data access interface for {@link BankAccount}.
 *
 * @curam .nonimplementable
 * @since 6.0
 */
@ImplementedBy(BDMCommStatusHistoryDAOImpl.class)
public interface BDMCommStatusHistoryDAO {

  public List<BDMCommStatusHistoryDtls>
    listAllCommunicationStatusHistory(long communicationID);

  public List<BDMCommStatusHistoryDtls>
    listCommunicationStatusHistory(long communicationID);

  List<BDMCommStatusHistoryDtls>
    listCommunicationRecordStatusHistory(long communicationID);

  long createCommunicationStatusHistory(long communicationID,
    String newStatusCode, String userName);

  long createCommunicationRecordStatusHistory(long communicationID,
    String newStatusCode, String userName);

}
