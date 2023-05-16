package curam.ca.gc.bdm.entity.communication.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.notification.impl.BDMNotification;
import curam.ca.gc.bdm.sl.commstatushistory.impl.BDMCommStatusHistoryDAO;
import curam.ca.gc.bdm.util.impl.BDMCacheUtil;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.struct.ConcernRoleCommCancelDetails;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;

public class BDMConcernRoleCommunicationSub extends
  curam.ca.gc.bdm.entity.communication.base.BDMConcernRoleCommunicationSub {

  public BDMConcernRoleCommunicationSub() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  private BDMCommStatusHistoryDAO bdmStatusHistoryDAO;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private BDMNotification bdmNotification;

  /*
   * Cancels a concern role communication.
   *
   * @param key Contains the concern role communication identifier.
   *
   * @param details Cancellation details.
   */
  @Override
  protected void postmodify(final ConcernRoleCommunicationKey key,
    final ConcernRoleCommunicationDtls details)
    throws AppException, InformationalException {

    if (details.communicationFormat.equals(COMMUNICATIONFORMAT.PROFORMA)
      || details.communicationFormat.equals(COMMUNICATIONFORMAT.MSWORD)) {
      final BDMCommunicationHelper commHelper = new BDMCommunicationHelper();

      final ConcernRole concernRole =
        concernRoleDAO.get(details.correspondentConcernRoleID);
      final int unreadCount =
        commHelper.getUnreadCount(details.correspondentConcernRoleID);
    }

  }

  /*
   * Cancels a concern role communication.
   *
   * @param key Contains the concern role communication identifier.
   *
   * @param details Cancellation details.
   */
  @Override
  protected void premodify(final ConcernRoleCommunicationKey key,
    final ConcernRoleCommunicationDtls details)
    throws AppException, InformationalException {

    final ConcernRoleCommunication crCommunication =
      ConcernRoleCommunicationFactory.newInstance();

    ConcernRoleCommunicationDtls existingCRCDtls =
      new ConcernRoleCommunicationDtls();
    try {
      existingCRCDtls = crCommunication.read(key);
    } catch (AppException | InformationalException e) {
      Trace.kTopLevelLogger.info(e.getMessage());
    }
    if (!existingCRCDtls.communicationStatus
      .equals(details.communicationStatus)
      // BUG-96908, Start
      // Don't insert for cancelled since we are
      // inserting post cancel
      && !COMMUNICATIONSTATUS.CANCELLED
        .equalsIgnoreCase(details.communicationStatus)
      && !BDMCacheUtil.isWorkItemIDUpdatedThruAPI(details.communicationID)) {
      // BUG-96908, End
      bdmStatusHistoryDAO.createCommunicationStatusHistory(
        key.communicationID, details.communicationStatus,
        TransactionInfo.getProgramUser());
    }

  }

  /*
   * Cancels a concern role communication. pre cancel data access function
   *
   * @param key Contains the concern role communication identifier.
   *
   * @param details Cancellation details.
   */
  @Override
  protected void precancel(final ConcernRoleCommunicationKey key,
    final ConcernRoleCommCancelDetails details)
    throws AppException, InformationalException {

    // calls the OOTB precancel function
    // BUG-93875, Start
    // Call OOTB for validations other than Format Correspondence
    details.statusCode = curam.codetable.RECORDSTATUS.CANCELLED;
    final ConcernRoleCommunication crCommunication =
      ConcernRoleCommunicationFactory.newInstance();

    ConcernRoleCommunicationDtls existingCRCDtls =
      new ConcernRoleCommunicationDtls();
    try {
      existingCRCDtls = crCommunication.read(key);
    } catch (AppException | InformationalException e) {
      Trace.kTopLevelLogger.info(e.getMessage());
    }
    if (!COMMUNICATIONFORMAT.CORRESPONDENCE
      .equalsIgnoreCase(existingCRCDtls.communicationFormat)) {
      super.precancel(key, details);
    }
    // BUG-93875, End
  }

  /*
   * Cancels a concern role communication. post data access function
   *
   * @param key Contains the concern role communication identifier.
   *
   * @param details Cancellation details.
   */
  @Override
  protected void postcancel(final ConcernRoleCommunicationKey key,
    final ConcernRoleCommCancelDetails details)
    throws AppException, InformationalException {

    // post cancel function adds status change history
    bdmStatusHistoryDAO.createCommunicationRecordStatusHistory(
      key.communicationID, details.statusCode,
      TransactionInfo.getProgramUser());

  }

}
