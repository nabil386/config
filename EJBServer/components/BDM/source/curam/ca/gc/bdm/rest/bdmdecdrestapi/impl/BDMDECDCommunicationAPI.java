package curam.ca.gc.bdm.rest.bdmdecdrestapi.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkDetails;
import curam.ca.gc.bdm.rest.bdmdecdrestapi.struct.BDMDECDGuidKey;
import curam.citizenaccount.impl.CitizenCommunicationsStrategy;
import curam.citizenworkspace.rest.facade.struct.UACommunication;
import curam.citizenworkspace.rest.facade.struct.UACommunicationList;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountInfo;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.core.fact.UsersFactory;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationDtlsList;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.UsersKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import java.util.Iterator;

/**
 *
 * REST API to retrieve the communications when accessed from Digital Channel
 * (DECD)
 *
 */
public class BDMDECDCommunicationAPI
  extends curam.ca.gc.bdm.rest.bdmdecdrestapi.base.BDMDECDCommunicationAPI {

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private Provider<CitizenCommunicationsStrategy> citizenCommunicationsStrategyProvider;

  public BDMDECDCommunicationAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public UACommunicationList listCommunications(final BDMDECDGuidKey guidKey)
    throws AppException, InformationalException {

    final BDMUsernameGuidLinkDetails guidUserNameDetail =
      new BDMDECDApplicationAPI().getUAUserNameByGuid(guidKey);
    final CitizenWorkspaceAccountInfo cwAccountInfo =
      this.citizenWorkspaceAccountManager
        .readAccountBy(guidUserNameDetail.username);

    final UACommunicationList uaCommunicationList = new UACommunicationList();

    if (cwAccountInfo.isLinkedToInternalCuramSystem()) {
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      final CitizenCommunicationsStrategy citizenCommunicationsStrategy =
        this.citizenCommunicationsStrategyProvider.get();
      concernRoleKey.concernRoleID = cwAccountInfo.getConcernRole().getID();
      final ConcernRoleCommunicationDtlsList concernRoleCommunicationDtlsList =
        citizenCommunicationsStrategy
          .listCitizenCommunications(concernRoleKey);
      final Iterator var6 = concernRoleCommunicationDtlsList.dtls.iterator();

      while (var6.hasNext()) {
        final ConcernRoleCommunicationDtls dtls =
          (ConcernRoleCommunicationDtls) var6.next();
        final UACommunication uaCommunication = new UACommunication();
        uaCommunication.communication_id = dtls.communicationID;
        uaCommunication.communicationDate = dtls.communicationDate;
        uaCommunication.subjectText = dtls.subjectText;
        uaCommunication.attachmentInd = dtls.attachmentInd;
        uaCommunication.incomingInd = dtls.incomingInd;
        uaCommunication.userName = this.getUserNameDetails(dtls.userName,
          dtls.incomingInd, dtls.correspondentName);
        uaCommunicationList.data.add(uaCommunication);
      }
    }

    return uaCommunicationList;
  }

  /**
   *
   * @param userName
   * @param incomingInd
   * @param correspondentName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getUserNameDetails(final String userName,
    final boolean incomingInd, final String correspondentName)
    throws AppException, InformationalException {

    if (incomingInd) {
      return correspondentName;
    } else {
      final UsersKey usersKey = new UsersKey();
      usersKey.userName = userName;
      return UsersFactory.newInstance().getFullName(usersKey).fullname;
    }
  }
}
