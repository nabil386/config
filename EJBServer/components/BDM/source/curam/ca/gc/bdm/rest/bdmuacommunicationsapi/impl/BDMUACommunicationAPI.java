package curam.ca.gc.bdm.rest.bdmuacommunicationsapi.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.notification.impl.BDMNotification;
import curam.ca.gc.bdm.rest.bdmuacommunicationsapi.struct.BDMCommunicationSearchKey;
import curam.ca.gc.bdm.rest.bdmuacommunicationsapi.struct.BDMUACommunication;
import curam.ca.gc.bdm.rest.bdmuacommunicationsapi.struct.BDMUACommunicationList;
import curam.citizenaccount.impl.CitizenCommunicationsStrategy;
import curam.citizenaccount.security.impl.CitizenAccountSecurityInternal;
import curam.citizenworkspace.rest.facade.fact.UACommunicationAPIFactory;
import curam.citizenworkspace.rest.facade.struct.UACommunication;
import curam.citizenworkspace.rest.facade.struct.UACommunicationDetails;
import curam.citizenworkspace.rest.facade.struct.UACommunicationKey;
import curam.citizenworkspace.rest.facade.struct.UACommunicationList;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountInfo;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.UsersFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationDtlsList;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.UsersKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

public class BDMUACommunicationAPI extends
  curam.ca.gc.bdm.rest.bdmuacommunicationsapi.base.BDMUACommunicationAPI {

  @Inject
  private CitizenAccountSecurityInternal citizenAccountSecurity;

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private Provider<CitizenCommunicationsStrategy> citizenCommunicationsStrategyProvider;

  @Inject
  private BDMNotification bdmNotification;

  BDMConcernRoleCommunication bdmCRCommObj =
    BDMConcernRoleCommunicationFactory.newInstance();

  BDMCommunicationHelper commHelper = new BDMCommunicationHelper();

  public BDMUACommunicationAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  // @Override
  public BDMUACommunication isNoticeRead(final long communicationID)
    throws AppException, InformationalException {

    final BDMUACommunication bdmCommDetails = new BDMUACommunication();

    final BDMConcernRoleCommunicationKey bdmCommKey =
      new BDMConcernRoleCommunicationKey();
    bdmCommKey.communicationID = communicationID;
    final NotFoundIndicator nfi = new NotFoundIndicator();
    final BDMConcernRoleCommunicationDtls bdmCommDtls =
      BDMConcernRoleCommunicationFactory.newInstance().read(nfi, bdmCommKey);

    if (nfi.isNotFound())
      return bdmCommDetails;
    if (!bdmCommDtls.cwFirstReadDateTime.equals(DateTime.kZeroDateTime)) {
      bdmCommDetails.isRead = true;
    }

    return bdmCommDetails;
  }

  // START : 10104 implement search Communication method JP
  @Override
  public UACommunicationList searchCommunication(
    final BDMCommunicationSearchKey bdmCommunicationSearchKey)
    throws AppException, InformationalException {

    UACommunicationList uaCommunicationList = new UACommunicationList();
    this.citizenAccountSecurity.performDefaultSecurityChecks();
    final CitizenWorkspaceAccountInfo cwAccountInfo =
      this.citizenWorkspaceAccountManager.getLoggedInUserCWAccountInfo();

    if (cwAccountInfo.isLinkedToInternalCuramSystem().booleanValue()) {
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      final CitizenCommunicationsStrategy citizenCommunicationsStrategy =
        this.citizenCommunicationsStrategyProvider.get();
      concernRoleKey.concernRoleID =
        cwAccountInfo.getConcernRole().getID().longValue();
      final ConcernRoleCommunicationDtlsList concernRoleCommunicationDtlsList =
        citizenCommunicationsStrategy
          .listCitizenCommunications(concernRoleKey);

      uaCommunicationList =
        searchComm(concernRoleKey, bdmCommunicationSearchKey,
          uaCommunicationList, concernRoleCommunicationDtlsList); // else

    } // if linkedaccount

    return uaCommunicationList;
  }

  /**
   * Check if Communiation date falls between searchCriteria toDate and fromDate
   * inclusive
   */

  public UACommunicationList searchComm(final ConcernRoleKey concernRoleKey,
    final BDMCommunicationSearchKey bdmCommunicationSearchKey,
    final UACommunicationList uaCommunicationList,
    final ConcernRoleCommunicationDtlsList concernRoleCommunicationDtlsList)
    throws AppException, InformationalException {

    final boolean fromDateFlag =
      isVAlidDate(bdmCommunicationSearchKey.fromDate);
    final boolean toDateFlag = isVAlidDate(bdmCommunicationSearchKey.toDate);

    if (!StringUtil.isNullOrEmpty(bdmCommunicationSearchKey.subject)) {

      for (final ConcernRoleCommunicationDtls dtls : concernRoleCommunicationDtlsList.dtls) {
        final UACommunication uaCommunication = new UACommunication();

        if (dtls.communicationDate
          .compareTo(bdmCommunicationSearchKey.fromDate) >= 0
          && dtls.communicationDate
            .compareTo(bdmCommunicationSearchKey.toDate) <= 0
          && stringMatch(bdmCommunicationSearchKey.subject,
            dtls.subjectText)) {

          uaCommunication.communication_id = dtls.communicationID;
          uaCommunication.communicationDate = dtls.communicationDate;
          uaCommunication.subjectText = dtls.subjectText;
          uaCommunication.attachmentInd = dtls.attachmentInd;
          uaCommunication.incomingInd = dtls.incomingInd;
          uaCommunication.userName = getUserNameDetails(dtls.userName,
            dtls.incomingInd, dtls.correspondentName);
          uaCommunicationList.data.add(uaCommunication);
        }

      } // for

    } else {

      for (final ConcernRoleCommunicationDtls dtls : concernRoleCommunicationDtlsList.dtls) {
        final UACommunication uaCommunication = new UACommunication();

        uaCommunication.communication_id = dtls.communicationID;
        uaCommunication.communicationDate = dtls.communicationDate;
        uaCommunication.subjectText = dtls.subjectText;
        uaCommunication.attachmentInd = dtls.attachmentInd;
        uaCommunication.incomingInd = dtls.incomingInd;
        uaCommunication.userName = getUserNameDetails(dtls.userName,
          dtls.incomingInd, dtls.correspondentName);
        uaCommunicationList.data.add(uaCommunication);
      } // for
    }

    final UACommunicationList sortedUACommunicationList = uaCommunicationList;
    // sort the returned list by the communicationDate
    try {
      Collections.sort(sortedUACommunicationList.data,
        new Comparator<UACommunication>() {

          @Override
          public int compare(final UACommunication uaComm1,
            final UACommunication uaComm2) {

            if (uaComm1.communicationDate.equals(uaComm2.communicationDate)) {
              return 0;
            } else if (uaComm1.communicationDate
              .before(uaComm2.communicationDate)) {
              return 1;
            } else {
              return -1;
            }
          }
        });
    } catch (final Exception e) {
      return uaCommunicationList;
    }
    return sortedUACommunicationList;
  }

  @Override
  public BDMUACommunicationList listCommunications()
    throws AppException, InformationalException {

    final BDMUACommunicationList returnList = new BDMUACommunicationList();

    final UACommunicationList commList =
      UACommunicationAPIFactory.newInstance().listCommunications();
    final ConcernRoleCommunication crCommObj =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey crcKey =
      new ConcernRoleCommunicationKey();

    // task-9394 Retention Period by Program for Correspondences display history
    final int retentionPeriod = Configuration.getIntProperty(
      EnvVars.BDM_CORRESPONDENCE_RETENTION_PERIOD_DISP_HISTORY);
    final LocalDate currentDate = LocalDate.now();
    final LocalDate displayHistoryRetentionCutOff =
      currentDate.minusYears(retentionPeriod);

    final java.util.Date d = java.util.Date.from(displayHistoryRetentionCutOff
      .atStartOfDay(ZoneId.systemDefault()).toInstant());
    d.setMonth(CuramConst.gkZero);
    d.setDate(CuramConst.gkZero);

    final Date cd = Date.getFromJavaUtilDate(d);

    for (final UACommunication uaCommDetails : commList.data.items()) {
      crcKey.communicationID = uaCommDetails.communication_id;
      final ConcernRoleCommunicationDtls crcDtls = crCommObj.read(crcKey);
      if (!crcDtls.communicationFormat.equals(COMMUNICATIONFORMAT.RECORDED)
        && crcDtls.communicationDate.after(cd)) {
        final BDMUACommunication bdmUACommDtls = new BDMUACommunication();
        bdmUACommDtls.assign(uaCommDetails);
        bdmUACommDtls.isRead =
          isNoticeRead(bdmUACommDtls.communication_id).isRead;
        returnList.dtls.add(bdmUACommDtls);
      }
    }
    returnList.retentionPeriod = retentionPeriod;

    // sort the returned list by the communicationDate
    final BDMUACommunicationList sortedReturnList = returnList;
    try {
      Collections.sort(sortedReturnList.dtls,
        new Comparator<BDMUACommunication>() {

          @Override
          public int compare(final BDMUACommunication bdmUAComm1,
            final BDMUACommunication bdmUAComm2) {

            if (bdmUAComm1.communicationDate
              .equals(bdmUAComm2.communicationDate)) {
              return 0;
            } else if (bdmUAComm1.communicationDate
              .before(bdmUAComm2.communicationDate)) {
              return 1;
            } else {
              return -1;
            }
          }
        });
    } catch (final Exception e) {
      return sortedReturnList;
    }
    return returnList;
  }

  @Override
  public UACommunicationDetails
    getCommunication(final UACommunicationKey uaCommunicationKey)
      throws AppException, InformationalException {

    final UACommunicationDetails uaCommDetails = UACommunicationAPIFactory
      .newInstance().getCommunication(uaCommunicationKey);

    final BDMConcernRoleCommunication bdmCommObj =
      BDMConcernRoleCommunicationFactory.newInstance();
    final BDMConcernRoleCommunicationKey bdmCommKey =
      new BDMConcernRoleCommunicationKey();
    bdmCommKey.communicationID = uaCommDetails.communication_id;
    final NotFoundIndicator nfi = new NotFoundIndicator();
    BDMConcernRoleCommunicationDtls bdmCommDtls =
      bdmCommObj.read(nfi, bdmCommKey);
    if (nfi.isNotFound()) {
      bdmCommDtls = new BDMConcernRoleCommunicationDtls();
      bdmCommDtls.communicationID = bdmCommKey.communicationID;
      bdmCommDtls.cwFirstReadDateTime = DateTime.getCurrentDateTime();
      bdmCommObj.insert(bdmCommDtls);
    } else {
      if (bdmCommDtls.cwFirstReadDateTime.equals(DateTime.kZeroDateTime)) {
        bdmCommDtls.cwFirstReadDateTime = DateTime.getCurrentDateTime();
        bdmCommObj.modify(bdmCommKey, bdmCommDtls);
      }
    }

    return uaCommDetails;
  }

  /**
   * Match specified subject with actual Subject using regular expression.Return
   * when match found ,false otherwise
   */
  private Boolean stringMatch(final String specifiedSubject,
    final String actualSubject) {

    // a REGEX
    final String REGEX = "(.*)(" + specifiedSubject + ")(.*)?";

    // use matches() method to check the match
    return Pattern.matches(REGEX, actualSubject);

  }

  /**
   * Check given Date is not null.
   */
  private Boolean isVAlidDate(final Date specifiedDate) {

    final Date currentDate = TransactionInfo.getSystemDate();

    if (null != specifiedDate && !specifiedDate.isZero()) {

      return true;

    }

    return false;

  }

  private String getUserNameDetails(final String userName,
    final boolean incomingInd, final String correspondentName)
    throws AppException, InformationalException {

    if (incomingInd)
      return correspondentName;
    final UsersKey usersKey = new UsersKey();
    usersKey.userName = userName;
    return UsersFactory.newInstance().getFullName(usersKey).fullname;

  }

  /**
   * Pulls role of the username.
   */
  private String getUserRole(final String userName)
    throws AppException, InformationalException {

    final UsersKey usersKey = new UsersKey();
    usersKey.userName = userName;
    return UsersFactory.newInstance().read(usersKey).upperRoleName;
  }

}
