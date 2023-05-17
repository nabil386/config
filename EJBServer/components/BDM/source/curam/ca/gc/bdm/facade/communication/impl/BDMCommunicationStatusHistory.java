package curam.ca.gc.bdm.facade.communication.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.communication.struct.BDMCommStatusHistoryDtls;
import curam.ca.gc.bdm.facade.communication.struct.BDMCommunicationStatusHistoryDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCommunicationStatusHistoryDetailsList;
import curam.ca.gc.bdm.sl.commstatushistory.impl.BDMCommStatusHistoryDAO;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.UsersFactory;
import curam.core.sl.struct.CommunicationIDKey;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.UsersDtls;
import curam.core.struct.UsersKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BDMCommunicationStatusHistory extends
  curam.ca.gc.bdm.facade.communication.base.BDMCommunicationStatusHistory {

  @Inject
  BDMCommStatusHistoryDAO bdmStatusHistoryDAO;

  protected BDMCommunicationStatusHistory() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public BDMCommunicationStatusHistoryDetailsList
    readHistoryByCommunicationID(final CommunicationIDKey commKey)
      throws AppException, InformationalException {

    // list sorting comparator by status date time
    class BDMSortByStatusDateTimeAscending
      implements Comparator<BDMCommStatusHistoryDtls> {

      @Override
      public int compare(final BDMCommStatusHistoryDtls o1,
        final BDMCommStatusHistoryDtls o2) {

        return o1.statusDateTime.compareTo(o2.statusDateTime);
      }

    }

    final BDMCommunicationStatusHistoryDetailsList returnList =
      new BDMCommunicationStatusHistoryDetailsList();
    final List<BDMCommStatusHistoryDtls> historylist = bdmStatusHistoryDAO
      .listAllCommunicationStatusHistory(commKey.communicationID);

    final List<BDMCommStatusHistoryDtls> recordStatushistorylist =
      new ArrayList<BDMCommStatusHistoryDtls>();

    // sort list by status datetime in ascending order
    Collections.sort(historylist, new BDMSortByStatusDateTimeAscending());
    // add communication status entry
    for (final BDMCommStatusHistoryDtls statusHistoryDtls : historylist) {

      if (statusHistoryDtls.statusCTTableName
        .equals(COMMUNICATIONSTATUS.TABLENAME)) {
        final BDMCommunicationStatusHistoryDetails commStatusHistory =
          new BDMCommunicationStatusHistoryDetails();
        commStatusHistory.communicationStatus = statusHistoryDtls.statusCode;
        commStatusHistory.statusDateTime = statusHistoryDtls.statusDateTime;
        final UsersKey usersKey = new UsersKey();
        usersKey.userName = statusHistoryDtls.userName;
        final UsersDtls usersDtls = UsersFactory.newInstance().read(usersKey);
        commStatusHistory.userName = usersDtls.fullName;
        returnList.statusHistoryList.add(commStatusHistory);
      } else if (statusHistoryDtls.statusCTTableName
        .equals(RECORDSTATUS.TABLENAME)) {
        recordStatushistorylist.add(statusHistoryDtls);
      }
    }

    // add record status entry
    for (final BDMCommStatusHistoryDtls recordStatusHistoryDetails : recordStatushistorylist) {
      if (recordStatushistorylist.indexOf(recordStatusHistoryDetails) != 0) {
        final BDMCommunicationStatusHistoryDetails commStatusHistory =
          new BDMCommunicationStatusHistoryDetails();
        final ConcernRoleCommunicationKey crcKey =
          new ConcernRoleCommunicationKey();
        crcKey.communicationID = recordStatusHistoryDetails.communicationID;
        final ConcernRoleCommunicationDtls crcDtls =
          ConcernRoleCommunicationFactory.newInstance().read(crcKey);
        commStatusHistory.communicationStatus = crcDtls.communicationStatus;
        commStatusHistory.statusDateTime =
          recordStatusHistoryDetails.statusDateTime;
        commStatusHistory.recordStatus =
          recordStatusHistoryDetails.statusCode;
        final UsersKey usersKey = new UsersKey();
        usersKey.userName = recordStatusHistoryDetails.userName;
        final UsersDtls usersDtls = UsersFactory.newInstance().read(usersKey);
        commStatusHistory.userName = usersDtls.fullName;
        returnList.statusHistoryList.add(commStatusHistory);
      }
    }

    // populate status entry
    for (final BDMCommStatusHistoryDtls recordStatusHistoryDetails : recordStatushistorylist) {
      for (final BDMCommunicationStatusHistoryDetails statusHistoryDetails : returnList.statusHistoryList
        .items()) {
        if (statusHistoryDetails.statusDateTime
          .compareTo(recordStatusHistoryDetails.statusDateTime) >= 0) {
          statusHistoryDetails.recordStatus =
            recordStatusHistoryDetails.statusCode;
        }
      }
    }

    return returnList;
  }

}
