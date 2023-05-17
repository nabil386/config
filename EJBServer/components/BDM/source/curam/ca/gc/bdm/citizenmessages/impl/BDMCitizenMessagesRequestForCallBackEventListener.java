package curam.ca.gc.bdm.citizenmessages.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMTIMEZONE;
import curam.ca.gc.bdm.entity.fact.BDMRequestForCallBackFactory;
import curam.ca.gc.bdm.entity.struct.BDMRequestForCallBackDtls;
import curam.ca.gc.bdm.entity.struct.BDMRequestForCallBackKey;
import curam.ca.gc.bdm.notification.impl.BDMNotificationConstants;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.core.impl.CuramConst;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.EntityType;
import curam.datastore.impl.NoSuchSchemaException;
import curam.participant.impl.ConcernRole;
import curam.participantmessages.codetable.impl.ParticipantMessageTypeEntry;
import curam.participantmessages.events.impl.CitizenMessagesEvent;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.participantmessages.persistence.impl.ParticipantMessageDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.workflow.impl.ProcessInstanceAdmin;
import curam.util.workflow.struct.ProcessInstanceFullDetails;
import curam.workspaceservices.util.impl.ResourceStoreHelper;
import java.util.List;

public class BDMCitizenMessagesRequestForCallBackEventListener
  extends CitizenMessagesEvent {

  @Inject
  private ParticipantMessageDAO participantMessageDAO;

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private ResourceStoreHelper resourceStoreHelper;

  public BDMCitizenMessagesRequestForCallBackEventListener() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void userRequestsMessages(
    final curam.participantmessages.impl.ParticipantMessages participantMessages)
    throws AppException, InformationalException {

    final ConcernRole concernRole =
      this.citizenWorkspaceAccountManager.getLoggedInUserConcernRoleID();

    // get all current payment messages
    final List<ParticipantMessage> participantMessageList =
      this.participantMessageDAO.searchByParticipantAndMessageType(
        concernRole, ParticipantMessageTypeEntry.APPLICATION);

    for (final ParticipantMessage participantMessage : participantMessageList) {
      if (BDMNotificationConstants.kCallBackRequestMessageBody
        .equals(participantMessage.getMessageProperty())
        || BDMNotificationConstants.kCallBackRequestTitle
          .equals(participantMessage.getTitleProperty())) {

        final BDMRequestForCallBackKey requestForCallBackKey =
          new BDMRequestForCallBackKey();
        requestForCallBackKey.requestForCallBackID =
          participantMessage.getRelatedID();
        final BDMRequestForCallBackDtls requestForCallBackDtls =
          BDMRequestForCallBackFactory.newInstance()
            .read(requestForCallBackKey);
        String bestTimeTocall = "";
        try {
          final Datastore datastore = DatastoreFactory.newInstance()
            .openDatastore("BDMRequestForCallBack");
          final Entity rootEntity =
            datastore.readEntity(requestForCallBackDtls.dataStoreID);

          final EntityType personEntityType =
            rootEntity.getDatastore().getEntityType("Person");

          final Entity[] personEntity =
            rootEntity.getChildEntities(personEntityType);

          if (Boolean.valueOf(personEntity[0].getAttribute("isMor"))) {
            bestTimeTocall = "Morning";
          }

          if (Boolean.valueOf(personEntity[0].getAttribute("isAftr"))) {
            bestTimeTocall =
              bestTimeTocall + CuramConst.gkSpace + "Afternoon";
          }

          if (Boolean.valueOf(personEntity[0].getAttribute("isEve"))) {
            bestTimeTocall = bestTimeTocall + CuramConst.gkSpace + "Evening";
          }

          bestTimeTocall = bestTimeTocall + CuramConst.gkSpace
            + BDMUtil.getCodeTableDescriptionForUserLocale(
              BDMTIMEZONE.TABLENAME,
              personEntity[0].getAttribute("timeZone"));

        } catch (final NoSuchSchemaException e) {
          // Ignore Exception
        }

        final curam.util.workflow.struct.ProcessInstanceID processInstanceID =
          new curam.util.workflow.struct.ProcessInstanceID();

        processInstanceID.processInstanceID =
          requestForCallBackDtls.processInstanceID;
        final ProcessInstanceFullDetails processInstanceFullDetails =
          ProcessInstanceAdmin.readProcessInstance(processInstanceID);

        if ("INPROGRESS".equals(processInstanceFullDetails.status)) {

          final curam.participantmessages.impl.ParticipantMessage partMsg =
            new curam.participantmessages.impl.ParticipantMessage(
              participantMessage.getMessageType());

          // set essential details for correspondence unread information message

          partMsg.setMessageBody(resourceStoreHelper.getPropertyValue(
            participantMessage.getPropertyFileName(),
            participantMessage.getMessageProperty()));
          partMsg.setMessageTitle(resourceStoreHelper.getPropertyValue(
            participantMessage.getPropertyFileName(),
            participantMessage.getTitleProperty()));

          partMsg.addParameter(BDMNotificationConstants.kCallBackRequestDate,
            processInstanceFullDetails.startDateTime.toString());

          partMsg.addParameter(BDMNotificationConstants.kCallBackRequestTime,
            bestTimeTocall);

          participantMessages.addMessage(partMsg);

        }

      }
    }

  }
}
