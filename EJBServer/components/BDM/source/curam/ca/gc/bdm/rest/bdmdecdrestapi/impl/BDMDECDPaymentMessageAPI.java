package curam.ca.gc.bdm.rest.bdmdecdrestapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkDetails;
import curam.ca.gc.bdm.rest.bdmdecdrestapi.struct.BDMDECDGuidKey;
import curam.ca.gc.bdm.rest.bdmdecdrestapi.struct.BDMDECDPaymentMessageKey;
import curam.citizenworkspace.message.CITIZENPORTALEXTERNALPARTYLINK;
import curam.citizenworkspace.rest.facade.struct.UAPayment;
import curam.citizenworkspace.rest.facade.struct.UAPaymentMessage;
import curam.citizenworkspace.rest.facade.struct.UAPaymentMessageList;
import curam.citizenworkspace.rest.message.RESTAPIMESSAGES;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountInfo;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.PRODUCTTYPE;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.fact.SimulatePaymentFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.fact.TabDetailFormatterFactory;
import curam.core.sl.struct.AmountDetail;
import curam.core.struct.CaseID;
import curam.core.struct.DateStruct;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.SimulateInd;
import curam.core.struct.SimulatePaymentResult;
import curam.participant.impl.ConcernRole;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.message.CatEntry;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * REST API to retrieve the payment messages when accessed from Digital Channel
 * (DECD)
 *
 */
public class BDMDECDPaymentMessageAPI
  extends curam.ca.gc.bdm.rest.bdmdecdrestapi.base.BDMDECDPaymentMessageAPI {

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  public BDMDECDPaymentMessageAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public UAPaymentMessageList
    readPaymentMessages(final BDMDECDPaymentMessageKey key)
      throws AppException, InformationalException {

    final BDMDECDGuidKey guidKey = new BDMDECDGuidKey();
    guidKey.on_Behalf_Of = key.on_Behalf_Of;
    final BDMUsernameGuidLinkDetails guidUserNameDetail =
      new BDMDECDApplicationAPI().getUAUserNameByGuid(guidKey);

    final UAPaymentMessageList paymentMessagesList =
      new UAPaymentMessageList();
    final ConcernRole concernRole =
      getConcernRoleBy(guidUserNameDetail.username);
    if (CuramConst.NEXT_ACTION.equalsIgnoreCase(key.type)) {
      final List<curam.piwrapper.caseheader.impl.CaseHeader> caseHeaderList =
        this.caseHeaderDAO.searchByPrimaryParticipant(concernRole);
      final Iterator caseHeaderIt = caseHeaderList.iterator();
      while (caseHeaderIt.hasNext()) {
        final curam.piwrapper.caseheader.impl.CaseHeader caseHeader =
          (curam.piwrapper.caseheader.impl.CaseHeader) caseHeaderIt.next();
        if (CASETYPECODEEntry.PRODUCTDELIVERY.getCode()
          .equalsIgnoreCase(caseHeader.getCaseType().getCode())) {
          final CaseID caseID = new CaseID();
          caseID.caseID = caseHeader.getID();
          final List<UAPayment> paymentDetailsList =
            this.getNextPaymentDetails(caseID);
          if (!paymentDetailsList.isEmpty()) {
            final UAPayment paymentDetails = paymentDetailsList.get(0);
            final UAPaymentMessage paymentMessage =
              this.getPaymentMessageDetails(paymentDetails, key.type);
            paymentMessagesList.data.addRef(paymentMessage);
          }
        }
      }
    }

    return paymentMessagesList;
  }

  /**
   *
   * @param caseID
   * @return
   */
  private List<UAPayment> getNextPaymentDetails(final CaseID caseID) {

    final List<UAPayment> nextPaymentDetailsList = new ArrayList<UAPayment>();
    final DateStruct simulationDate = new DateStruct();
    final SimulateInd simulateInd = new SimulateInd();
    simulateInd.simulateInd = false;
    SimulatePaymentResult simulatePaymentResult = null;
    try {
      simulatePaymentResult = SimulatePaymentFactory.newInstance()
        .simulatePayment(caseID, simulationDate, simulateInd);
      if (null != simulatePaymentResult) {
        final UAPayment paymentDetails = new UAPayment();
        paymentDetails.payment_id = caseID.caseID;
        paymentDetails.paymentDate = simulatePaymentResult.enteredDate;
        paymentDetails.amount = simulatePaymentResult.totalPayment;
        nextPaymentDetailsList.add(paymentDetails);
      }
    } catch (final Exception ex) {
    }
    return nextPaymentDetailsList;
  }

  /**
   *
   * @param paymentDetails
   * @param messageType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected UAPaymentMessage getPaymentMessageDetails(
    final UAPayment paymentDetails, final String messageType)
    throws AppException, InformationalException {

    final UAPaymentMessage paymentMessage = new UAPaymentMessage();
    final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
    productDeliveryKey.caseID = paymentDetails.payment_id;
    final String benefitName =
      CodeTable.getOneItem(PRODUCTTYPE.TABLENAME, ProductDeliveryFactory
        .newInstance().readProductType(productDeliveryKey).productType);
    final AmountDetail amountDetail = new AmountDetail();
    amountDetail.amount = paymentDetails.amount;
    final String nextPaymentAmount = TabDetailFormatterFactory.newInstance()
      .formatCurrencyAmount(amountDetail).amount;
    CatEntry messageCatEntry = null;
    CatEntry titleCatEntry = null;
    if (CuramConst.NEXT_ACTION.equalsIgnoreCase(messageType)) {
      messageCatEntry = RESTAPIMESSAGES.NEXT_SINGLE_PAYMENT_TEXT;
      titleCatEntry = RESTAPIMESSAGES.NEXT_PAYMENT_TITLE;
    }

    final LocalisableString paymentMsgText =
      new LocalisableString(messageCatEntry).arg(benefitName)
        .arg(nextPaymentAmount).arg(paymentDetails.paymentDate);
    final LocalisableString paymentMsgTitle =
      new LocalisableString(titleCatEntry).arg(benefitName);
    paymentMessage.paymentDate = paymentDetails.paymentDate;
    paymentMessage.messageText =
      paymentMsgText.getMessage(TransactionInfo.getProgramLocale());
    paymentMessage.title =
      paymentMsgTitle.getMessage(TransactionInfo.getProgramLocale());
    return paymentMessage;
  }

  /**
   *
   * @param citizenWorkspaceUsername
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ConcernRole getConcernRoleBy(final String citizenWorkspaceUsername)
    throws AppException, InformationalException {

    final CitizenWorkspaceAccountInfo cwAccountInfo =
      this.citizenWorkspaceAccountManager
        .readAccountBy(citizenWorkspaceUsername);
    final ConcernRole userConcernRole = cwAccountInfo.getConcernRole();
    if (userConcernRole == null) {
      final AppException appEx = new AppException(
        CITIZENPORTALEXTERNALPARTYLINK.ERR_NOT_A_LINKED_USER);
      appEx.arg(citizenWorkspaceUsername);
      throw appEx;
    } else {
      return userConcernRole;
    }
  }

}
