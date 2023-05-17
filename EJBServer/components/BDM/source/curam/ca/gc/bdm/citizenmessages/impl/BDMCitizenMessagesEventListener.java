package curam.ca.gc.bdm.citizenmessages.impl;

import com.google.inject.Inject;
import curam.advisor.impl.Parameter;
import curam.advisor.impl.ParameterDAO;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.impl.CASESTATUSEntry;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.codetable.impl.FINCOMPONENTSTATUSEntry;
import curam.core.facade.fact.CaseHeaderFactory;
import curam.core.facade.intf.CaseHeader;
import curam.core.facade.struct.ConcernRoleIDStatusCodeKey;
import curam.core.fact.FinancialComponentFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.FinancialComponent;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseAndConcernRoleKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.FCDueDate;
import curam.participant.impl.ConcernRole;
import curam.participantmessages.codetable.impl.ParticipantMessageTypeEntry;
import curam.participantmessages.events.impl.CitizenMessagesEvent;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.participantmessages.persistence.impl.ParticipantMessageDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.type.DateTime;
import curam.util.type.Money;
import java.util.Iterator;
import java.util.List;

/**
 * This event interface is used to gather messages for display in the Citizen
 * Workspace home page.
 *
 * @since 6.0
 */
public class BDMCitizenMessagesEventListener extends CitizenMessagesEvent {

  @Inject
  private ParticipantMessageDAO participantMessageDAO;

  @Inject
  private ParameterDAO parameterDAO;

  public BDMCitizenMessagesEventListener() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void userRequestsMessages(
    final curam.participantmessages.impl.ParticipantMessages participantMessages)
    throws AppException, InformationalException {

    // Creates the Payment messages for the NIL and UnderThreshold Statuses
    updateBDMPaymentMessage(participantMessages);
  }

  /**
   * Creates the Payment messages for the NIL and UnderThreshold Statuses
   *
   * @param participantMessages
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private void updateBDMPaymentMessage(
    final curam.participantmessages.impl.ParticipantMessages participantMessages)
    throws AppException, InformationalException {

    final ConcernRole concernRole = participantMessages.getConcernRole();

    // get all current payment messages
    final List<ParticipantMessage> participantMessageList =
      this.participantMessageDAO.searchByParticipantAndMessageType(
        concernRole, ParticipantMessageTypeEntry.PAYMENT);

    // remove old messages if multiple exist
    // participantMessageList =
    // expireOldParticipantMessages(participantMessageList);

    // Sort through all current payment messages
    for (final ParticipantMessage participantMessage : participantMessageList) {
      final List<Parameter> participantMessageParams =
        participantMessage.getMessageParameters();

      final String messageProperty = participantMessage.getMessageProperty();
      final String messageTitle = participantMessage.getTitleProperty();

      // if Payment Message title is already a NIL or Under threshold type then
      // ignore processing
      if (messageTitle.equals(BDMConstants.kPaymentUnderThresholdMessageTitle)
        || messageTitle.equals(BDMConstants.kPaymentNilMessageTitle)) {
        break;
      }

      String bdmMessageType = messageProperty;
      String bdmMessageTitle = messageTitle;

      for (final Parameter parameter : participantMessageParams) {
        // Check if PaymentAmount is less that min Amount or 0 and update to Nil
        // or under threshold

        if (parameter.getName()
          .equals(BDMConstants.kPaymentMessagePaymentDueDate)
          && bdmMessageType.equals(BDMConstants.kPaymentDueMessage)) {
          // Update Next Payment Date

          final FinancialComponent financialComponentObj =
            FinancialComponentFactory.newInstance();
          final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();

          final ConcernRoleIDStatusCodeKey concernRoleIdStatusCodeKey =
            new ConcernRoleIDStatusCodeKey();
          concernRoleIdStatusCodeKey.dtls.concernRoleID = concernRole.getID();
          concernRoleIdStatusCodeKey.dtls.statusCode =
            CASESTATUSEntry.ACTIVE.getCode();
          final CaseHeaderDtlsList caseHeaderDtlsList =
            caseHeaderObj.searchByConcernRoleID(concernRoleIdStatusCodeKey);

          for (final CaseHeaderDtls caseHeaderDtls : caseHeaderDtlsList.dtlsList.dtls) {
            if (caseHeaderDtls.caseTypeCode
              .equals(CASETYPECODEEntry.PRODUCTDELIVERY.getCode())) {
              final CaseAndConcernRoleKey caseAndConcernRoleKey =
                new CaseAndConcernRoleKey();
              caseAndConcernRoleKey.concernRoleID = concernRole.getID();
              caseAndConcernRoleKey.caseID = caseHeaderDtls.caseID;
              caseAndConcernRoleKey.statusCode =
                FINCOMPONENTSTATUSEntry.LIVE.getCode();
              final FCDueDate nextPaymentDate = financialComponentObj
                .readNextPaymentDate(caseAndConcernRoleKey);
              parameter.setValue(nextPaymentDate.dueDate);
            }

          }

        }
        if (parameter.getName()
          .equals(BDMConstants.kPaymentMessagePaymentAmount)) {
          final String minIssueAmtStr = Configuration
            .getProperty(EnvVars.BDM_ENV_BDM_MINIMUM_ISSUE_AMOUNT);
          final Money minIssueAmt = new Money(minIssueAmtStr);
          // convert Payment amount to money value
          String paymentAmtStr = parameter.getValue();
          final char paymentAmtStrChar = paymentAmtStr.toCharArray()[0];
          // remove the first char in the payment amount if it is not a digit
          // this assumes currency symbols will always precede the currency
          // value
          if (!Character.isDigit(paymentAmtStrChar)) {
            paymentAmtStr = paymentAmtStr.replace(paymentAmtStrChar,
              BDMConstants.kStringSpace.toCharArray()[0]).trim();
          }
          final Money paymentAmt = new Money(paymentAmtStr);
          // NIL Payment
          if (paymentAmt.isZero()) {
            bdmMessageType = BDMConstants.kPaymentNilMessage;
            bdmMessageTitle = BDMConstants.kPaymentNilMessageTitle;
          }
          // UnderThreshold
          else if (paymentAmt.compareTo(minIssueAmt) <= 0) {
            bdmMessageType = BDMConstants.kPaymentUnderThresholdMessage;
            bdmMessageTitle = BDMConstants.kPaymentUnderThresholdMessageTitle;
          }
          break;
        }
      }

      // if Payment Message title is already a BDM type the ignore processing
      if (bdmMessageTitle.equals(BDMConstants.kPaymentCancelledMessageTitle)
        || bdmMessageTitle.equals(BDMConstants.kPaymentSuspendedMessageTitle)
        || bdmMessageTitle
          .equals(BDMConstants.kPaymentUnsuspendedMessageTitle)
        || bdmMessageTitle.equals(BDMConstants.kPaymentLatestMessageTitle)) {
        break;
      }
      // update payment title for other OOTB message types
      if (bdmMessageType.equals(BDMConstants.kPaymentCancelledMessage)) {
        bdmMessageTitle = BDMConstants.kPaymentCancelledMessageTitle;
      } else if (bdmMessageType
        .equals(BDMConstants.kPaymentSuspendedMessage)) {
        bdmMessageTitle = BDMConstants.kPaymentSuspendedMessageTitle;
      } else if (bdmMessageType
        .equals(BDMConstants.kPaymentUnsuspendedMessage)) {
        bdmMessageTitle = BDMConstants.kPaymentUnsuspendedMessageTitle;
      } else if (bdmMessageType.equals(BDMConstants.kPaymentLatestMessage)
        || bdmMessageType.equals(BDMConstants.kPaymentLastMessage)
        || bdmMessageType.equals(BDMConstants.kPaymentReissueMessage)) {
        bdmMessageTitle = BDMConstants.kPaymentLatestMessageTitle;

      }
      // Update the BDM Payment Message

      participantMessage.setTitleProperty(bdmMessageTitle);
      participantMessage.setMessageProperty(bdmMessageType);
      try {
        participantMessage.modify();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Takes a list of messages of the same messageType and will expire all but
   * the most recent messages
   *
   * @param List<ParticipantMessage>
   * @return List<ParticipantMessage>
   * @throws AppException
   * @throws InformationalException
   */
  private List<ParticipantMessage> expireOldParticipantMessages(
    final List<ParticipantMessage> participantMessageList)
    throws AppException, InformationalException {

    if (participantMessageList.isEmpty()
      || participantMessageList.size() == 1) {
      return participantMessageList;
    }
    // Sort through all current messages
    final Iterator<ParticipantMessage> participantMessageListIter =
      participantMessageList.iterator();
    ParticipantMessage oldestParticipantMessage =
      participantMessageListIter.next();
    while (participantMessageListIter.hasNext()) {
      final ParticipantMessage participantMessage =
        participantMessageListIter.next();
      if (participantMessage.getCreatedDateTime()
        .before(oldestParticipantMessage.getCreatedDateTime())) {
        expireParticipantMessage(participantMessage);
      } else {
        expireParticipantMessage(oldestParticipantMessage);
        oldestParticipantMessage = participantMessage;
      }
    }
    return participantMessageList;
  }

  /**
   * Expire Participant message for the citizen portal to remove the message
   *
   * @param participantMessage
   * @throws InformationalException
   */
  private void
    expireParticipantMessage(final ParticipantMessage participantMessage)
      throws InformationalException {

    participantMessage
      .setExpiryDateTime(DateTime.getCurrentDateTime().addTime(-24, 0, 0));
    participantMessage.modify();
  }

}
