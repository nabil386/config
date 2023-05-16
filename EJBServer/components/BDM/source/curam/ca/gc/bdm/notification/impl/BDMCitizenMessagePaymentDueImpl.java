package curam.ca.gc.bdm.notification.impl;

import com.google.inject.Inject;
import curam.advisor.impl.Parameter;
import curam.advisor.impl.ParameterDAO;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.PRODUCTTYPE;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.fact.CaseNomineeFactory;
import curam.core.sl.entity.intf.CaseNominee;
import curam.core.sl.entity.struct.CaseNomineeKey;
import curam.core.struct.PaymentInstrumentDtls;
import curam.core.struct.ProductDeliveryKey;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.participantmessages.codetable.impl.ParticipantMessageTypeEntry;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.participantmessages.persistence.impl.ParticipantMessageDAO;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.impl.CaseNomineeDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.type.CodeTable;
import curam.util.type.DateTime;
import java.util.List;

/**
 * A new class to generate payment due message for Client Portal
 *
 */
public class BDMCitizenMessagePaymentDueImpl
  implements BDMCitizenMessagePaymentDue {

  @Inject
  private ParticipantMessageDAO participantMessageDAO;

  @Inject
  private ParameterDAO parameterDAO;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private CaseNomineeDAO caseNomineeDAO;

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  private final int durationDay = Configuration
    .getIntProperty(EnvVars.ENV_CITIZEN_ACCOUNT_PMT_MESSAGES_EXPIRY_DAYS);

  public BDMCitizenMessagePaymentDueImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Payment Due notification message for the citizen portal
   *
   * @return ParticipantMessage
   */
  @Override
  public void
    CreatePaymentDueMessage(final PaymentInstrumentDtls paymentInstrumentDtls)
      throws InformationalException, AppException {

    final ConcernRole concernRole = this.concernRoleDAO
      .get(Long.valueOf(paymentInstrumentDtls.concernRoleID));

    // get all current payment messages
    final List<ParticipantMessage> participantMessageList =
      this.participantMessageDAO.searchByParticipantAndMessageType(
        concernRole, ParticipantMessageTypeEntry.PAYMENT);
    // If no message currently exists create a message
    if (participantMessageList.isEmpty()) {
      final curam.participantmessages.persistence.impl.ParticipantMessage participantMessage =
        participantMessageDAO.newInstance();
      // set Concern Role instance
      participantMessage.setConcernRole(concernRole);
      participantMessage.setEffectiveDateTime(DateTime.getCurrentDateTime());
      participantMessage
        .setExpiryDateTime(participantMessage.getEffectiveDateTime()
          .addTime(durationDay * CuramConst.gkNumberOfHoursInADay, 0, 0));
      // set property file, message title and body
      participantMessage.setMessageProperty(BDMConstants.kPaymentDueMessage);
      participantMessage
        .setPropertyFileName(BDMConstants.kCitizenMessageMyPayments);
      participantMessage
        .setTitleProperty(BDMConstants.kPaymentDueMessageTitle);
      participantMessage.setMessageType(ParticipantMessageTypeEntry.PAYMENT);
      try {
        participantMessage.insert();
      } catch (final Exception e) {
        e.printStackTrace();
      }
      // Set Parameter
      try {
        final CaseNomineeKey caseNomineeKey = new CaseNomineeKey();
        caseNomineeKey.caseNomineeID = paymentInstrumentDtls.caseNomineeID;
        final CaseNominee caseNomineeObj = CaseNomineeFactory.newInstance();
        final ProductDeliveryKey productDeliveryKey =
          new ProductDeliveryKey();
        productDeliveryKey.caseID =
          caseNomineeObj.readCaseID(caseNomineeKey).caseID;
        final String benefitName =
          CodeTable.getOneItem(PRODUCTTYPE.TABLENAME, ProductDeliveryFactory
            .newInstance().readProductType(productDeliveryKey).productType);
        // Case Name
        participantMessage.addParameter(createParticipantMessageParam(
          BDMConstants.kCitizenMessageCaseNameAttr, benefitName));
        participantMessage.setRelatedID(productDeliveryKey.caseID);
        participantMessage.modify();
      } catch (final Exception e) {
        e.printStackTrace();
      }
      // If messages exist modify the current message
    } else {
      final ParticipantMessage participantMessage =
        participantMessageList.get(0);
      // if payment message is already payment due then skip processing
      if (participantMessage.getMessageProperty()
        .equals(BDMConstants.kPaymentDueMessage)) {
        return;
      }
      // if payment message is an curam issued message then convert to a
      // payment due message
      participantMessage.setEffectiveDateTime(DateTime.getCurrentDateTime());
      participantMessage
        .setExpiryDateTime(participantMessage.getEffectiveDateTime()
          .addTime(durationDay * CuramConst.gkNumberOfHoursInADay, 0, 0));
      // set property file, message title and body
      participantMessage.setMessageProperty(BDMConstants.kPaymentDueMessage);
      participantMessage
        .setTitleProperty(BDMConstants.kPaymentDueMessageTitle);
      try {
        participantMessage.modify();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * create new instance of parameter type for given parameter
   *
   * @return Parameter
   * @param unreadCount
   */
  @Override
  public Parameter createParticipantMessageParam(final String attrName,
    final Object value) throws InformationalException {

    final Parameter partMsgParam = parameterDAO.newInstance();
    partMsgParam.setName(attrName);
    if (value instanceof String) {
      partMsgParam.setValue((String) value);

    } else if (value instanceof curam.util.type.Date) {
      partMsgParam.setValue((curam.util.type.Date) value);

    } else if (value instanceof Number) {
      partMsgParam.setValue((Number) value);

    }
    partMsgParam.insert();
    return partMsgParam;
  }

  /**
   * expire the Suspend Benefit notification message for the citizen portal to
   * remove the message out of the notification card
   *
   * @param ParticipantMessage
   */
  @Override
  public void
    expirePaymentDueMessage(final ParticipantMessage participantMessage)
      throws InformationalException {

    participantMessage.setExpiryDateTime(DateTime.getCurrentDateTime());
    participantMessage.modify();
  }

  /**
   * Update Payment Due Message To Issued
   *
   * @param PaymentInstrumentDtls
   */
  @Override
  public void UpdatePaymentDueMessageToIssued(
    final PaymentInstrumentDtls paymentInstrumentDtls)
    throws InformationalException, AppException {

    // if payment message has a param for Payment.Next.Date
    // if true then convert type to Message.Payment.Latest
    // else convert to Message.Last.Payment
    final ConcernRole concernRole = this.concernRoleDAO
      .get(Long.valueOf(paymentInstrumentDtls.concernRoleID));
    // get all current payment messages
    final List<ParticipantMessage> participantMessageList =
      this.participantMessageDAO.searchByParticipantAndMessageType(
        concernRole, ParticipantMessageTypeEntry.PAYMENT);
    // Sort through all current payment messages
    final ParticipantMessage participantMessage =
      participantMessageList.get(0);
    final List<Parameter> participantMessageParams =
      participantMessage.getMessageParameters();
    final String messagePropertyType =
      participantMessage.getMessageProperty();
    final String messageTitle = participantMessage.getTitleProperty();
    String bdmMessageType = messagePropertyType;
    final String bdmMessageTitle = messageTitle;
    // check if there is a payment next date and then set the message type
    // accordingly
    bdmMessageType = BDMConstants.kPaymentLastMessage;
    for (final Parameter parameter : participantMessageParams) {
      if (parameter.getName()
        .equals(BDMConstants.kPaymentMessagePaymentNextDate)
        && parameter.getValue() != null) {
        bdmMessageType = BDMConstants.kPaymentLatestMessage;
      }
      // Update the BDM Payment Message
      participantMessage.setTitleProperty(bdmMessageTitle);
      participantMessage.setMessageProperty(bdmMessageType);
      participantMessage.setEffectiveDateTime(DateTime.getCurrentDateTime());
      participantMessage
        .setExpiryDateTime(participantMessage.getEffectiveDateTime()
          .addTime(durationDay * CuramConst.gkNumberOfHoursInADay, 0, 0));
      try {
        participantMessage.modify();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }
}
