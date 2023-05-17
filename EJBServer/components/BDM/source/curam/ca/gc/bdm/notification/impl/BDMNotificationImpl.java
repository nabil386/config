package curam.ca.gc.bdm.notification.impl;

import com.google.inject.Inject;
import curam.advisor.impl.Parameter;
import curam.advisor.impl.ParameterDAO;
import curam.ca.gc.bdm.codetable.BDMGCNotifyTemplateType;
import curam.ca.gc.bdm.entity.fact.BDMWMInstanceDataFactory;
import curam.ca.gc.bdm.entity.struct.BDMWMInstanceDataDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.impl.PRODUCTTYPEEntry;
import curam.core.fact.ProductDeliveryFactory;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.WMInstanceData;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.ProductDeliveryTypeDetails;
import curam.core.struct.WMInstanceDataDtls;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.participantmessages.codetable.impl.ParticipantMessageTypeEntry;
import curam.participantmessages.impl.ParticipantMessageLink;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.participantmessages.persistence.impl.ParticipantMessageDAO;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.DatabaseException;
import curam.util.exception.InformationalException;
import curam.util.fact.DeferredProcessingFactory;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.workspaceservices.util.impl.ResourceStoreHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BDMNotificationImpl implements BDMNotification {

  @Inject
  private ParticipantMessageDAO participantMessageDAO;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private ParameterDAO parameterDAO;

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private ResourceStoreHelper resourceStoreHelper;

  public BDMNotificationImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Create and insert Participant Message of Claim Established notification
   *
   * @return ParticipantMessage
   * @param concernRoleID
   */
  @Override
  public ParticipantMessage createClaimEstablishedNotificationMessage(
    final long concernRoleID, final long caseID)
    throws InformationalException, AppException {

    final ConcernRole concernRole = concernRoleDAO.get(concernRoleID);
    final List<ParticipantMessage> messageList =
      getMessageByConcernRoleTypeAndRelatedID(concernRole,
        ParticipantMessageTypeEntry.APPLICATION, caseID);
    final curam.participantmessages.persistence.impl.ParticipantMessage participantMessage =
      participantMessageDAO.newInstance();
    if (messageList.size() > 0) {

      messageList.get(0);
      setClaimEstablishedMessage(participantMessage);
      participantMessage.modify();
    } else {

      // set concern role, case ID
      participantMessage.setConcernRole(concernRole);
      participantMessage.setRelatedID(caseID);

      setClaimEstablishedMessage(participantMessage);
      participantMessage.insert();

      if (citizenWorkspaceAccountManager.hasLinkedAccount(concernRole)) {
        // if GC notify is true, send the GC notify Alert
        if (Configuration.getBooleanProperty(
          EnvVars.BDM_ENV_NOTIFICATION_CLAIMESTABLISHED_GCNOTIFY_IND)) {
          startDPGCNotifyAlert(concernRoleID,
            BDMGCNotifyTemplateType.BDM_GC_NF);
        }
      }
    }
    return participantMessage;

  }

  /**
   * create and Insert Claim Denied Notification Message for client portal
   *
   * @return ParticipantMessage
   * @param concernRoleID
   */
  @Override
  public ParticipantMessage createClaimDeniedNotificationMessage(
    final long concernRoleID, final long caseID)
    throws InformationalException, AppException {

    final ConcernRole concernRole = concernRoleDAO.get(concernRoleID);
    final List<ParticipantMessage> messageList =
      getMessageByConcernRoleTypeAndRelatedID(concernRole,
        ParticipantMessageTypeEntry.APPLICATION, caseID);
    curam.participantmessages.persistence.impl.ParticipantMessage participantMessage =
      participantMessageDAO.newInstance();
    if (messageList.size() > 0) {
      participantMessage = messageList.get(0);
      setClaimDeniedMessage(participantMessage);
      participantMessage.modify();
    } else {

      // set Concern Role instance
      participantMessage.setConcernRole(concernRole);
      participantMessage.setRelatedID(caseID);
      setClaimDeniedMessage(participantMessage);
      participantMessage.insert();

      // if user is linked
      if (citizenWorkspaceAccountManager.hasLinkedAccount(concernRole)) {
        // if GC notify is true, send the GC notify Alert
        if (Configuration.getBooleanProperty(
          EnvVars.BDM_ENV_NOTIFICATION_CLAIMDENIED_GCNOTIFY_IND)) {
          startDPGCNotifyAlert(concernRoleID,
            BDMGCNotifyTemplateType.BDM_GC_NF);
        }
      }
    }
    return participantMessage;
  }

  /**
   * Create new instance of Unread Message with the unread count
   *
   * @return ParticipantMessage
   * @param concernRoleID
   */
  @Override
  public curam.participantmessages.impl.ParticipantMessage
    createUnreadInformationalMessage(final long concernRoleID,
      final int unreadCount) throws InformationalException, AppException {

    final curam.participantmessages.impl.ParticipantMessage partMsg =
      new curam.participantmessages.impl.ParticipantMessage(
        ParticipantMessageTypeEntry.SYSTEM);

    // set essential details for correspondence unread information message
    partMsg.setEffectiveDateTime(DateTime.getCurrentDateTime());

    if (unreadCount == 1) {
      partMsg.setMessageBody(resourceStoreHelper.getPropertyValue(
        BDMNotificationConstants.kBDMNotificationPropertyFile,
        BDMNotificationConstants.kCorrespondenceUnreadMessageBodySingular));
      partMsg.setMessageTitle(resourceStoreHelper.getPropertyValue(
        BDMNotificationConstants.kBDMNotificationPropertyFile,
        BDMNotificationConstants.kCorrespondenceUnreadMessageTitleSingular));
    } else {
      partMsg.setMessageBody(resourceStoreHelper.getPropertyValue(
        BDMNotificationConstants.kBDMNotificationPropertyFile,
        BDMNotificationConstants.kCorrespondenceUnreadMessageBodyPlural));
      partMsg.setMessageTitle(resourceStoreHelper.getPropertyValue(
        BDMNotificationConstants.kBDMNotificationPropertyFile,
        BDMNotificationConstants.kCorrespondenceUnreadMessageTitlePlural));
    }

    final Parameter unreadCountParam = createUnreadCountParam(unreadCount);

    partMsg.addParameter(unreadCountParam.getName(),
      unreadCountParam.getValue());

    final ParticipantMessageLink pmLink = new ParticipantMessageLink(false,
      resourceStoreHelper.getPropertyValue(
        BDMNotificationConstants.kBDMNotificationPropertyFile,
        BDMNotificationConstants.kCorrespondenceUnreadMessageTarget),
      resourceStoreHelper.getPropertyValue(
        BDMNotificationConstants.kBDMNotificationPropertyFile,
        BDMNotificationConstants.kCorrespondenceUnreadMessageLink));

    partMsg.addLink(pmLink);
    return partMsg;
  }

  /**
   * create new instance of Unread mail count parameter
   *
   * @return Parameter
   * @param unreadCount
   */
  @Override
  public Parameter createUnreadCountParam(final int unreadCount)
    throws InformationalException {

    // create new unread mail count parameter
    final Parameter partMsgParam = parameterDAO.newInstance();
    partMsgParam.setName(BDMConstants.kNotificationUnreadMailCountAttr);
    partMsgParam.setValue(unreadCount);
    return partMsgParam;
  }

  // @Override
  public ParticipantMessage setClaimEstablishedMessage(
    final curam.participantmessages.persistence.impl.ParticipantMessage participantMessage)
    throws InformationalException, AppException {

    // set effect data time
    participantMessage.setEffectiveDateTime(DateTime.getCurrentDateTime());
    // read duration property and set the expiry day
    final int durationDay = Configuration
      .getIntProperty(EnvVars.BDM_ENV_NOTIFICATION_DURATION_CLAIMESTABLISHED);
    participantMessage
      .setExpiryDateTime(participantMessage.getEffectiveDateTime()
        .addTime(durationDay * CuramConst.gkNumberOfHoursInADay, 0, 0));
    // set property file, message title and body
    participantMessage.setMessageProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_BODY_CLAIMESTABLISHED));
    participantMessage.setPropertyFileName(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_FILENAME_CLAIMESTABLISHED));
    participantMessage.setTitleProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_TITLE_CLAIMESTABLISHED));
    // set message type to Application and display it on the notification card
    participantMessage
      .setMessageType(ParticipantMessageTypeEntry.APPLICATION);

    return participantMessage;

  }

  public ParticipantMessage setClaimDeniedMessage(
    final curam.participantmessages.persistence.impl.ParticipantMessage participantMessage)
    throws InformationalException, AppException {

    // set effective date time
    participantMessage.setEffectiveDateTime(DateTime.getCurrentDateTime());
    // read duration property and set the expiry day
    final int durationDay = Configuration
      .getIntProperty(EnvVars.BDM_ENV_NOTIFICATION_DURATION_CLAIMDENIED);
    participantMessage
      .setExpiryDateTime(participantMessage.getEffectiveDateTime()
        .addTime(durationDay * CuramConst.gkNumberOfHoursInADay, 0, 0));
    // set property file, message title and body
    participantMessage.setMessageProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_BODY_CLAIMDENIED));
    participantMessage.setPropertyFileName(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_FILENAME_CLAIMDENIED));
    participantMessage.setTitleProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_TITLE_CLAIMDENIED));
    // set message type to Application and display it on the notification card
    participantMessage
      .setMessageType(ParticipantMessageTypeEntry.APPLICATION);

    return participantMessage;

  }

  /**
   * expire the Claim Denied notification message for the citizen portal to
   * remove the message out of the notification card
   *
   * @param Concern Role Persistent Object
   */
  // @Override
  public void expireClaimDeniedNotificationMessage(
    final ParticipantMessage participantMessage)
    throws InformationalException {

    participantMessage.setExpiryDateTime(DateTime.getCurrentDateTime());
    participantMessage.modify();

  }

  /**
   * Look through the entire participant message list and return a list of
   * messages match the concern Role, message Type and relatedID
   *
   * @param Concern Role Persistent Object
   * @param Participant Message Type
   * @param relateID
   */
  @Override
  public List<ParticipantMessage> getMessageByConcernRoleTypeAndRelatedID(
    final ConcernRole concernRole,
    final ParticipantMessageTypeEntry messageType, final long relatedID)
    throws InformationalException, AppException {

    // get all current Application type messages
    final List<ParticipantMessage> participantMessageList =
      participantMessageDAO.searchByParticipantAndMessageType(concernRole,
        messageType);
    final List<ParticipantMessage> returnList =
      new ArrayList<ParticipantMessage>();

    // Sort through all current messages and filter message by relatedID
    for (final ParticipantMessage participantMessage : participantMessageList) {
      if (participantMessage.getRelatedID() == relatedID) {
        returnList.add(participantMessage);
      }
    }

    return returnList;
  }

  /**
   * Look through the entire participant message list and return a list of
   * messages match the concern Role, message Type and relatedID
   *
   * @param Concern Role Persistent Object
   * @param Participant Message Type
   * @param relateID
   */
  @Override
  public List<ParticipantMessage>
    searchActiveClaimEstablishDeniedParticipantMessages(
      final ConcernRole concernRole)
      throws InformationalException, AppException {

    // get all current Application type messages
    final List<ParticipantMessage> participantMessageList =
      participantMessageDAO.searchByParticipantAndMessageType(concernRole,
        ParticipantMessageTypeEntry.APPLICATION);

    final List<ParticipantMessage> participantMessageList1 =
      new ArrayList<>();

    // Sort through all current messages and filter message by relatedID
    for (final ParticipantMessage participantMessage : participantMessageList) {
      if (participantMessage.getExpiryDateTime()
        .after(DateTime.getCurrentDateTime())
        && (participantMessage.getMessageProperty()
          .equals(Configuration
            .getProperty(EnvVars.BDM_ENV_NOTIFICATION_BODY_CLAIMESTABLISHED))
          || participantMessage.getMessageProperty().equals(Configuration
            .getProperty(EnvVars.BDM_ENV_NOTIFICATION_BODY_CLAIMDENIED)))) {
        participantMessageList1.add(participantMessage);
      }
    }

    return participantMessageList1;

  }

  /**
   * create WMInstanceData entry and start DP process for creating GC Notify
   * Alert
   *
   * @param Concern Role ID
   * @param template ID property name
   */
  public void startDPGCNotifyAlert(final long concernRoleID,
    final String templateType) throws DatabaseException, AppRuntimeException,
    AppException, InformationalException {

    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();
    final WMInstanceDataDtls wmInstanceDataDtls = new WMInstanceDataDtls();
    // add caseid, concernroleid to the wmInstanceDataDtls struct
    wmInstanceDataDtls.wm_instDataID =
      curam.util.type.UniqueID.nextUniqueID(BDMConstants.kDPTICKET);
    wmInstanceDataDtls.enteredByID = TransactionInfo.getProgramUser();
    wmInstanceDataDtls.concernRoleID = concernRoleID;
    wmInstanceDataDtls.comments = templateType;
    wmInstanceDataObj.insert(wmInstanceDataDtls);

    final BDMWMInstanceDataDtls bdmWMInstanceDataDtls =
      new BDMWMInstanceDataDtls();
    bdmWMInstanceDataDtls.wm_instDataID = wmInstanceDataDtls.wm_instDataID;
    bdmWMInstanceDataDtls.gcNotifyTemplateType = templateType;
    BDMWMInstanceDataFactory.newInstance().insert(bdmWMInstanceDataDtls);

    // start the dp process to create correspondence
    DeferredProcessingFactory.newInstance().startProcess(
      BDMConstants.kCREATEGCNOTIFYALERTDP, wmInstanceDataDtls.wm_instDataID,
      BDMCreateNotificationDPErrorHandler.class.getCanonicalName());

  }

  @Override
  public ParticipantMessage createSuspendBenefitCaseNotificationMessage(
    final long concernRoleID, final long caseID)
    throws InformationalException, AppException {

    final ConcernRole concernRole = concernRoleDAO.get(concernRoleID);
    List<ParticipantMessage> messageList =
      getMessageByConcernRoleTypeAndRelatedID(concernRole,
        ParticipantMessageTypeEntry.PAYMENT, caseID);
    curam.participantmessages.persistence.impl.ParticipantMessage participantMessage =
      participantMessageDAO.newInstance();

    // set concern role, case ID
    participantMessage.setConcernRole(concernRole);
    if (messageList.size() == 0) {
      participantMessage.setRelatedID(caseID);

    } else {
      messageList.get(0).setExpiryDateTime(DateTime.getCurrentDateTime());
      messageList.get(0).modify();

      final List<ParticipantMessage> subMessageList =
        getMessageByConcernRoleTypeAndRelatedID(concernRole,
          ParticipantMessageTypeEntry.PAYMENT, messageList.get(0).getID());
      // if existing message present for the payment message assign it to the
      // message
      if (subMessageList.size() > 0) {
        participantMessage = subMessageList.get(0);
      } else {
        participantMessage.setRelatedID(messageList.get(0).getID());
      }
    }

    final ProductDeliveryKey pdKey = new ProductDeliveryKey();
    pdKey.caseID = caseID;
    final ProductDeliveryTypeDetails pdTypeDetail =
      ProductDeliveryFactory.newInstance().readProductType(pdKey);

    final Map<String, String> suspendBenefitMessageMap =
      new HashMap<String, String>();
    suspendBenefitMessageMap.put(
      BDMNotificationConstants.kSuspendBenefitNotiticationBenefitName,
      PRODUCTTYPEEntry.get(pdTypeDetail.productType).toUserLocaleString());
    suspendBenefitMessageMap.put(
      BDMNotificationConstants.kSuspendBenefitNotiticationDate,
      Date.getCurrentDate().toString(TransactionInfo.getProgramLocale()));


    setSuspendBenefitdMessage(participantMessage);
    try {
      participantMessage.insert();
    } catch (final Exception e) {
      participantMessage.modify();
    }

    createOrModifySuspendmessageParameters(participantMessage,
      suspendBenefitMessageMap);

    if (citizenWorkspaceAccountManager.hasLinkedAccount(concernRole)) {
      // if GC notify is true, send the GC notify Alert
      if (Configuration.getBooleanProperty(
        EnvVars.BDM_ENV_NOTIFICATION_SUSPENDBENEFIT_GCNOTIFY_IND)) {
        startDPGCNotifyAlert(concernRoleID,
          BDMGCNotifyTemplateType.BDM_GC_NF);
      }
    }
    return participantMessage;

  }

  private void createOrModifySuspendmessageParameters(
    final ParticipantMessage participantMessage,
    final Map<String, String> suspendBenefitMessageMap)
    throws AppException, InformationalException {

    final List<Parameter> parameterList =
      participantMessage.getMessageParameters();

    for (final String key : suspendBenefitMessageMap.keySet()) {
      Parameter parameter = null;
      for (final Parameter _parameter : parameterList) {
        if (_parameter.getName().equals(key)) {
          parameter = _parameter;
          parameter.setValue(suspendBenefitMessageMap.get(key));
          parameter.modify(parameter.getVersionNo());
        }
      }
      if (parameter == null) {
        parameter = parameterDAO.newInstance();
        parameter.setName(key);
        parameter.setValue(suspendBenefitMessageMap.get(key));
        parameter.insert();
        participantMessage.addParameter(parameter);
      }
    }

  }

  private ParticipantMessage
    setSuspendBenefitdMessage(final ParticipantMessage participantMessage) {

    // set effect data time

    participantMessage.setEffectiveDateTime(DateTime.getCurrentDateTime());
    // read duration property and set the expiry day
    final int durationDay = Configuration
      .getIntProperty(EnvVars.BDM_ENV_NOTIFICATION_DURATION_SUSPENDBENEFIT);
    participantMessage
      .setExpiryDateTime(participantMessage.getEffectiveDateTime()
        .addTime(durationDay * CuramConst.gkNumberOfHoursInADay, 0, 0));
    // set property file, message title and body
    participantMessage.setMessageProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_BODY_SUSPENDBENEFIT));
    participantMessage.setPropertyFileName(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_FILENAME_CLAIMESTABLISHED));
    participantMessage.setTitleProperty(Configuration
      .getProperty(EnvVars.BDM_ENV_NOTIFICATION_TITLE_SUSPENDBENEFIT));
    // set message type to Application and display it on the notification card
    participantMessage.setMessageType(ParticipantMessageTypeEntry.PAYMENT);

    return participantMessage;
  }

  @Override
  public List<ParticipantMessage>
    searchSuspendBenefitParticipantMessages(final ConcernRole concernRole) {

    // get all current Application type messages
    final List<ParticipantMessage> participantMessageList =
      participantMessageDAO.searchByParticipantAndMessageType(concernRole,
        ParticipantMessageTypeEntry.PAYMENT);
    final List<ParticipantMessage> returnList =
      new ArrayList<ParticipantMessage>();

    // Sort through all current messages and filter message by relatedID
    for (final ParticipantMessage participantMessage : participantMessageList) {
      if (participantMessage.getMessageProperty().equals(Configuration
        .getProperty(EnvVars.BDM_ENV_NOTIFICATION_BODY_SUSPENDBENEFIT))) {
        returnList.add(participantMessage);
      }
    }

    return returnList;
  }

  @Override
  public List<ParticipantMessage> searchActiveParticipantMessagesByType(
    final ConcernRole concernRole, final ParticipantMessageTypeEntry type) {

    // get all current Application type messages
    final List<ParticipantMessage> participantMessageList =
      participantMessageDAO.searchByParticipantAndMessageType(concernRole,
        type);
    final List<ParticipantMessage> returnList =
      new ArrayList<ParticipantMessage>();

    // Sort through all current messages and filter message by expiry date
    for (final ParticipantMessage participantMessage : participantMessageList) {
      if (participantMessage.getExpiryDateTime()
        .after(DateTime.getCurrentDateTime())) {
        returnList.add(participantMessage);
      }
    }

    return returnList;
  }

  @Override
  public void triggerUnsuspendBenefitCaseNotificationMessage(
    final long concernRoleID, final long caseID)
    throws AppException, InformationalException {

    final ConcernRole concernRole = concernRoleDAO.get(concernRoleID);

    List<ParticipantMessage> messageList =
      getMessageByConcernRoleTypeAndRelatedID(concernRole,
        ParticipantMessageTypeEntry.PAYMENT, caseID);

    if (messageList.size() > 0
      && messageList.get(0).getMessageProperty().equals(Configuration
        .getProperty(EnvVars.BDM_ENV_NOTIFICATION_BODY_SUSPENDBENEFIT)))
      messageList.get(0).remove();
    else {
      final ParticipantMessage existingMessage = messageList.get(0);
      messageList = getMessageByConcernRoleTypeAndRelatedID(concernRole,
        ParticipantMessageTypeEntry.PAYMENT, existingMessage.getID());
      if (messageList.size() > 0)
        messageList.get(0).remove();

    }

  }
}
