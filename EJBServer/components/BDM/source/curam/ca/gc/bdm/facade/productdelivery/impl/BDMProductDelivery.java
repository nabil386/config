package curam.ca.gc.bdm.facade.productdelivery.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONUSERTYPE;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDetails;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.ca.gc.bdm.events.BDMNOTIFICATION;
import curam.ca.gc.bdm.facade.productdelivery.struct.BDMViewCaseFinancialInstructionDetailsList;
import curam.ca.gc.bdm.facade.productdelivery.struct.BDMViewCaseInstructionDetails;
import curam.ca.gc.bdm.message.BDMDEDUCTIONS;
import curam.ca.gc.bdm.sl.financial.impl.BDMFinancial;
import curam.ca.gc.bdm.sl.productdelivery.impl.MaintainDeductionDetails;
import curam.codetable.DEDUCTIONNAME;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.intf.ProductDelivery;
import curam.core.facade.struct.CancelDeductionKey;
import curam.core.facade.struct.CaseIDKey;
import curam.core.facade.struct.DeductionActivationDetails;
import curam.core.facade.struct.DeductionNameList;
import curam.core.facade.struct.DeductionNameListDetails;
import curam.core.facade.struct.InformationMsgDtlsList;
import curam.core.facade.struct.ListCaseFinancialsKey;
import curam.core.facade.struct.ModifyCaseDeductionItemDetails1;
import curam.core.facade.struct.ModifyThirdPartyDeductionDetails;
import curam.core.facade.struct.ProductDeliverySuspensionKey;
import curam.core.facade.struct.ProductDeliveryUnsuspensionKey;
import curam.core.facade.struct.ReadDeductionKey;
import curam.core.facade.struct.ThirdPartyDeductionActivationStruct;
import curam.core.facade.struct.ViewCaseFinancialInstructionList;
import curam.core.fact.CachedCaseHeaderFactory;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PaymentInstructionFactory;
import curam.core.intf.CachedCaseHeader;
import curam.core.intf.PaymentInstruction;
import curam.core.sl.entity.fact.DeductionFactory;
import curam.core.sl.entity.struct.DeductionDtls;
import curam.core.sl.entity.struct.DeductionNameStatus;
import curam.core.sl.struct.DeductionName;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.FinInstructionID;
import curam.core.struct.PaymentInstructionDtls;
import curam.core.struct.ViewCaseInstructionDetails;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.CodeTable;
import curam.util.type.NotFoundIndicator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class BDMProductDelivery
  extends curam.ca.gc.bdm.facade.productdelivery.base.BDMProductDelivery {

  @Inject
  BDMFinancial bdmFinancialObj;

  @Inject
  MaintainDeductionDetails maintainDeductionDetailsObj;

  public BDMProductDelivery() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public InformationMsgDtlsList
    userActionFixedDeduction(final DeductionActivationDetails details)
      throws AppException, InformationalException {

    final CaseDeductionItemDtls caseDeductionItemDtls =
      new CaseDeductionItemDtls();
    caseDeductionItemDtls.assign(details.deductionDtls);

    final int priority =
      maintainDeductionDetailsObj.calculatePriority(caseDeductionItemDtls);
    details.deductionDtls.priority = priority;

    checkAccessToDeductionName(caseDeductionItemDtls);

    return super.userActionFixedDeduction(details);
  }

  @Override
  public InformationMsgDtlsList
    userActionVariableDeduction(final DeductionActivationDetails details)
      throws AppException, InformationalException {

    final CaseDeductionItemDtls caseDeductionItemDtls =
      new CaseDeductionItemDtls();
    caseDeductionItemDtls.assign(details.deductionDtls);

    checkAccessToDeductionName(caseDeductionItemDtls);

    final int priority =
      maintainDeductionDetailsObj.calculatePriority(caseDeductionItemDtls);
    details.deductionDtls.priority = priority;

    return super.userActionVariableDeduction(details);
  }

  @Override
  public InformationMsgDtlsList
    modifyCaseDeductionItem1(final ModifyCaseDeductionItemDetails1 details)
      throws AppException, InformationalException {

    final CaseDeductionItemKey key = new CaseDeductionItemKey();
    key.caseDeductionItemID = details.deductionDtls.caseDeductionItemID;
    final CaseDeductionItemDtls caseDeductionItemDtls =
      CaseDeductionItemFactory.newInstance().read(key);
    details.modifyDtls.priority = caseDeductionItemDtls.priority;

    // no need to calculate priority again if start date didn't change - the
    // only modification that could affect priority
    if (!caseDeductionItemDtls.startDate
      .equals(details.deductionDtls.startDate)) {
      // if the start date has been modified, set the new start date to be used
      // in
      // calculating priority
      caseDeductionItemDtls.startDate = details.deductionDtls.startDate;

      final int priority =
        maintainDeductionDetailsObj.calculatePriority(caseDeductionItemDtls);
      details.modifyDtls.priority = priority;
    }

    final InformationMsgDtlsList msgs =
      super.modifyCaseDeductionItem1(details);

    maintainDeductionDetailsObj
      .resequencePriorities(details.deductionDtls.caseDeductionItemID);
    return msgs;

  }

  @Override
  public InformationMsgDtlsList processThirdPartyFixedDeduction(
    final ThirdPartyDeductionActivationStruct details)
    throws AppException, InformationalException {

    final CaseDeductionItemDtls caseDeductionItemDtls =
      new CaseDeductionItemDtls();
    caseDeductionItemDtls.assign(details.deductionDtls);

    // calculate priority
    final int priority =
      maintainDeductionDetailsObj.calculatePriority(caseDeductionItemDtls);
    details.deductionDtls.priority = priority;

    // auto determine the third party details
    maintainDeductionDetailsObj.determineThirdPartyDetails(details);

    checkAccessToDeductionName(caseDeductionItemDtls);

    return super.processThirdPartyFixedDeduction(details);
  }

  @Override
  public InformationMsgDtlsList processThirdPartyVariableDeduction(
    final ThirdPartyDeductionActivationStruct details)
    throws AppException, InformationalException {

    final CaseDeductionItemDtls caseDeductionItemDtls =
      new CaseDeductionItemDtls();
    caseDeductionItemDtls.assign(details.deductionDtls);

    // calculate priority
    final int priority =
      maintainDeductionDetailsObj.calculatePriority(caseDeductionItemDtls);
    details.deductionDtls.priority = priority;

    // auto determine the third party details
    maintainDeductionDetailsObj.determineThirdPartyDetails(details);

    // Fixing issue 15023
    final DeductionName deductionName = new DeductionName();
    deductionName.deductionName = details.deductionDtls.deductionName;

    final curam.core.sl.struct.ReadDeductionDetails readDeductionDetails =
      curam.core.sl.fact.DeductionFactory.newInstance()
        .readDeductionDetailsByName(deductionName);

    final BDMDeductionKey deductionKey = new BDMDeductionKey();
    deductionKey.deductionID = readDeductionDetails.dtls.deductionID;

    // End issue fix 15023

    final BDMDeductionDetails bdmDeductionDtls =
      BDMDeductionFactory.newInstance().readByDeductionID(deductionKey);

    if (!bdmDeductionDtls.managedBy
      .equalsIgnoreCase(BDMDEDUCTIONUSERTYPE.AGENT)) {

      final AppException exceededDeductionLimitErr = new AppException(
        BDMDEDUCTIONS.ERR_DEDUCTION_TYPE_CREATION_NOT_ALLOWED);

      final String deductionNameStr = CodeTable.getOneItem(
        DEDUCTIONNAME.TABLENAME, caseDeductionItemDtls.deductionName);
      exceededDeductionLimitErr.arg(deductionNameStr);

      throw exceededDeductionLimitErr;
    }

    return super.processThirdPartyVariableDeduction(details);
  }

  @Override
  public InformationMsgDtlsList modifyThirdPartyDeductionItem(
    final ModifyThirdPartyDeductionDetails details)
    throws AppException, InformationalException {

    final CaseDeductionItemKey key = new CaseDeductionItemKey();
    key.caseDeductionItemID = details.deductionDtls.caseDeductionItemID;
    final CaseDeductionItemDtls caseDeductionItemDtls =
      CaseDeductionItemFactory.newInstance().read(key);
    details.modifyDtls.priority = caseDeductionItemDtls.priority;

    // no need to calculate priority again if start date didn't change - the
    // only modification that could affect priority
    if (!caseDeductionItemDtls.startDate
      .equals(details.deductionDtls.startDate)) {
      // if the start date has been modified, set the new start date to be used
      // in calculating priority
      caseDeductionItemDtls.startDate = details.deductionDtls.startDate;

      final int priority =
        maintainDeductionDetailsObj.calculatePriority(caseDeductionItemDtls);
      details.modifyDtls.priority = priority;
    }

    final InformationMsgDtlsList msgs =
      super.modifyThirdPartyDeductionItem(details);

    maintainDeductionDetailsObj
      .resequencePriorities(details.deductionDtls.caseDeductionItemID);
    return msgs;
  }

  @Override
  public void cancelDeduction(final CancelDeductionKey key)
    throws AppException, InformationalException {

    super.cancelDeduction(key);

    maintainDeductionDetailsObj.resequencePriorities(key.caseDeductionItemID);
  }

  /**
   * Modifies financial instruction list to include status and processed date
   * and
   * sorts by processed date in descending order
   */
  @Override
  public BDMViewCaseFinancialInstructionDetailsList
    listFinInstruction(final ListCaseFinancialsKey key)
      throws AppException, InformationalException {

    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();
    final ViewCaseFinancialInstructionList viewCaseFinancialInstructionList =
      productDeliveryObj.listCaseFinancialInstruction(key);

    // filter out any FIs that are linked to a reissued payments
    filterOutLinkedCancelledAndReversedPayments(
      viewCaseFinancialInstructionList.dtls);

    // initialize return struct
    final BDMViewCaseFinancialInstructionDetailsList bdmViewCaseFinancialInstructionDetailsList =
      new BDMViewCaseFinancialInstructionDetailsList();

    // assign details
    bdmViewCaseFinancialInstructionDetailsList
      .assign(viewCaseFinancialInstructionList);

    for (int i = 0; i < viewCaseFinancialInstructionList.dtls.dtlsList
      .size(); i++) {

      final ViewCaseInstructionDetails viewCaseInstructionDetails =
        viewCaseFinancialInstructionList.dtls.dtlsList.item(i);
      final BDMViewCaseInstructionDetails bdmViewCaseInstructionDetails =
        new BDMViewCaseInstructionDetails();

      bdmViewCaseInstructionDetails.viewCaseInstructionDetails
        .assign(viewCaseInstructionDetails);
      bdmViewCaseFinancialInstructionDetailsList.dtls.dtlsList.set(i,
        bdmViewCaseInstructionDetails);

      // check if it's invalidated
      final boolean isInvalidated = BDMViewPaymentsUtil.isInvalidated(
        bdmViewCaseInstructionDetails.viewCaseInstructionDetails.finInstructDtls.finInstructionID);

      // START - Task 21028 - Added changes for code refactoring after removing
      // BDMFinInstruction entity.
      final PaymentInstruction paymentInstructionObj =
        PaymentInstructionFactory.newInstance();
      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      final FinInstructionID finInstructionIDKey = new FinInstructionID();
      finInstructionIDKey.finInstructionID =
        bdmViewCaseInstructionDetails.viewCaseInstructionDetails.finInstructDtls.finInstructionID;
      final PaymentInstructionDtls paymentInstructionDtls =
        paymentInstructionObj.readByFinInstructionID(notFoundIndicator,
          finInstructionIDKey);

      String statusCode = "";
      if (!notFoundIndicator.isNotFound()) {
        statusCode = BDMViewPaymentsUtil.getBDMPaymentInstructionStatusCode(
          paymentInstructionDtls.pmtInstrumentID);
        bdmViewCaseInstructionDetails.bdmStatusCode =
          BDMViewPaymentsUtil.getPaymentStatus(statusCode, isInvalidated);

        // START update method of delivery
        final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
          new BDMPaymentInstrumentKey();
        bdmPaymentInstrumentKey.pmtInstrumentID =
          paymentInstructionDtls.pmtInstrumentID;
        final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
          BDMPaymentInstrumentFactory.newInstance()
            .read(bdmPaymentInstrumentKey);
        if (!StringUtil
          .isNullOrEmpty(bdmPaymentInstrumentDtls.spsDeliveryMethodType)) {
          bdmViewCaseInstructionDetails.viewCaseInstructionDetails.deliveryMethodType =
            bdmPaymentInstrumentDtls.spsDeliveryMethodType;
        }
        // END - update method of delivery - JSHAH
      } else {
        statusCode = BDMViewPaymentsUtil.getFinInstructionStatusCode(
          bdmViewCaseInstructionDetails.viewCaseInstructionDetails.finInstructDtls.finInstructionID);
        bdmViewCaseInstructionDetails.bdmStatusCode = BDMViewPaymentsUtil
          .getPaymentStatusForFinInstruction(statusCode, isInvalidated);
      }
      // END Task - 21028 - JSHAH

      bdmViewCaseInstructionDetails.viewCaseInstructionDetails.displayIndicators =
        BDMViewPaymentsUtil.modifyPaymentDisplayIndicators(
          bdmViewCaseInstructionDetails.viewCaseInstructionDetails.displayIndicators,
          statusCode, isInvalidated);
    }

    // sort by processed date descending
    bdmViewCaseFinancialInstructionDetailsList.dtls.dtlsList
      .sort(new BDMProcessedDateComparator());

    return bdmViewCaseFinancialInstructionDetailsList;
  }

  /**
   * Filters out any cancelled payments or reversals that are linked to another
   * payment as they do not want to be displayed
   *
   * @param viewCaseFinancialInstructionList
   * @throws AppException
   * @throws InformationalException
   */
  private void filterOutLinkedCancelledAndReversedPayments(
    final curam.core.struct.ViewCaseFinancialInstructionList viewCaseFinancialInstructionList)
    throws AppException, InformationalException {

    final Set<Long> totalLinkedFIs = new HashSet<Long>();

    // iterate through current list of financials
    for (final ViewCaseInstructionDetails caseFinancialDetails : viewCaseFinancialInstructionList.dtlsList) {
      bdmFinancialObj.addLinkedFIsToRemove(totalLinkedFIs,
        caseFinancialDetails.finInstructDtls.typeCode,
        caseFinancialDetails.finInstructDtls.statusCode,
        caseFinancialDetails.finInstructDtls.finInstructionID);

    }

    final Predicate<ViewCaseInstructionDetails> shouldRemove =
      details -> totalLinkedFIs
        .contains(new Long(details.finInstructDtls.finInstructionID));

    // remove linked FIs
    viewCaseFinancialInstructionList.dtlsList.removeIf(shouldRemove);

  }

  @Override
  public DeductionNameList listAppliedDeductionName(final CaseIDKey key)
    throws AppException, InformationalException {

    final DeductionNameList deductionList =
      super.listAppliedDeductionName(key);

    final DeductionNameList deductionNameList = new DeductionNameList();

    for (final DeductionNameListDetails deductionName : deductionList.dtlsList) {

      final long deductionId = deductionName.dtls.dtls.deductionID;

      final BDMDeductionKey deductionKey = new BDMDeductionKey();
      deductionKey.deductionID = deductionId;
      final BDMDeductionDetails bdmDeductionDtls =
        BDMDeductionFactory.newInstance().readByDeductionID(deductionKey);

      if (bdmDeductionDtls.managedBy
        .equalsIgnoreCase(BDMDEDUCTIONUSERTYPE.AGENT)) {
        deductionNameList.dtlsList.add(deductionName);
      }

    }

    return deductionNameList;
  }

  @Override
  public DeductionNameList listThirdPartyDeductionName(final CaseIDKey key)
    throws AppException, InformationalException {

    final DeductionNameList deductionList =
      super.listThirdPartyDeductionName(key);

    final DeductionNameList deductionNameList = new DeductionNameList();

    for (final DeductionNameListDetails deductionName : deductionList.dtlsList) {

      final long deductionId = deductionName.dtls.dtls.deductionID;

      final BDMDeductionKey deductionKey = new BDMDeductionKey();
      deductionKey.deductionID = deductionId;
      final BDMDeductionDetails bdmDeductionDtls =
        BDMDeductionFactory.newInstance().readByDeductionID(deductionKey);

      if (bdmDeductionDtls.managedBy
        .equalsIgnoreCase(BDMDEDUCTIONUSERTYPE.AGENT)) {
        deductionNameList.dtlsList.add(deductionName);
      }

    }

    return deductionNameList;

  }

  @Override
  public DeductionNameList listUnappliedDeductionName(final CaseIDKey key)
    throws AppException, InformationalException {

    final DeductionNameList deductionList =
      super.listUnappliedDeductionName(key);

    final DeductionNameList deductionNameList = new DeductionNameList();

    for (final DeductionNameListDetails deductionName : deductionList.dtlsList) {

      final long deductionId = deductionName.dtls.dtls.deductionID;

      final BDMDeductionKey deductionKey = new BDMDeductionKey();
      deductionKey.deductionID = deductionId;
      final BDMDeductionDetails bdmDeductionDtls =
        BDMDeductionFactory.newInstance().readByDeductionID(deductionKey);

      if (bdmDeductionDtls.managedBy
        .equalsIgnoreCase(BDMDEDUCTIONUSERTYPE.AGENT)) {
        deductionNameList.dtlsList.add(deductionName);
      }

    }

    return deductionNameList;
  }

  public void checkAccessToDeductionName(
    final CaseDeductionItemDtls caseDeductionItemDtls)
    throws AppException, InformationalException {

    final DeductionNameStatus deductionAndStatusKey =
      new DeductionNameStatus();
    deductionAndStatusKey.deductionName = caseDeductionItemDtls.deductionName;
    deductionAndStatusKey.recordStatus = RECORDSTATUS.NORMAL;
    final DeductionDtls deduDtls = DeductionFactory.newInstance()
      .readActiveDeductionByName(deductionAndStatusKey);

    final BDMDeductionKey deductionKey = new BDMDeductionKey();
    deductionKey.deductionID = deduDtls.deductionID;
    final BDMDeductionDetails bdmDeductionDtls =
      BDMDeductionFactory.newInstance().readByDeductionID(deductionKey);

    if (!bdmDeductionDtls.managedBy
      .equalsIgnoreCase(BDMDEDUCTIONUSERTYPE.AGENT)) {

      final AppException exceededDeductionLimitErr = new AppException(
        BDMDEDUCTIONS.ERR_DEDUCTION_TYPE_CREATION_NOT_ALLOWED);

      final String deductionNameStr = CodeTable.getOneItem(
        DEDUCTIONNAME.TABLENAME, caseDeductionItemDtls.deductionName);
      exceededDeductionLimitErr.arg(deductionNameStr);

      throw exceededDeductionLimitErr;
    }
  }

  /**
   * Task 9424 Generate a Suspend Notification Message for Client portal on
   * Benefit Case Suspend
   * {@inheritDoc}
   *
   * @param ProductDeliverySuspensionKey
   * @return
   */
  @Override
  public void suspend(final ProductDeliverySuspensionKey key)
    throws AppException, InformationalException {

    // Call Super class method to suspend Benefits
    super.suspend(key);
    // Create a new Event
    final Event event = new Event();
    event.eventKey = BDMNOTIFICATION.CREATESUSPENDBENEFITCASE;
    // Assign caseHeaderKey to ProductDeliveryUnsuspensionKey to read
    // CaseHeaderDtls
    final CachedCaseHeader cachedCaseHeader =
      CachedCaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseID;

    final CaseHeaderDtls caseHeaderDtls =
      cachedCaseHeader.read(caseHeaderKey);
    // Set concernRoleKey to caseHeaderDtls concernRoleID to tead
    // ConcernRoleDtls
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final curam.core.intf.ConcernRole concernRoleObj =
      ConcernRoleFactory.newInstance();
    concernRoleKey.concernRoleID = caseHeaderDtls.concernRoleID;
    final ConcernRoleDtls concernRoleDtls =
      concernRoleObj.read(concernRoleKey);
    // Set primary and secondary event data
    event.primaryEventData = concernRoleDtls.concernRoleID;
    event.secondaryEventData = key.caseID;
    // Raise event to generate Suspend Notification
    EventService.raiseEvent(event);
  }

  /**
   * Task 9425 Generate a Notification on case Unsuspend
   *
   * @param key
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void unsuspend(final ProductDeliveryUnsuspensionKey key)
    throws AppException, InformationalException {

    super.unsuspend(key);
    // Create a new event
    final Event event = new Event();
    event.eventKey = BDMNOTIFICATION.CREATEUNSUSPENDBENEFITCASE;

    final CachedCaseHeader cachedCaseHeader =
      CachedCaseHeaderFactory.newInstance();
    // Assign caseHeaderKey to ProductDeliveryUnsuspensionKey to read
    // CaseHeaderDtls
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseID;

    final CaseHeaderDtls caseHeaderDtls =
      cachedCaseHeader.read(caseHeaderKey);
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final curam.core.intf.ConcernRole concernRoleObj =
      ConcernRoleFactory.newInstance();
    // Set concernRoleKey to caseHeaderDtls concernRoleID to tead
    // ConcernRoleDtls
    concernRoleKey.concernRoleID = caseHeaderDtls.concernRoleID;
    final ConcernRoleDtls concernRoleDtls =
      concernRoleObj.read(concernRoleKey);
    // Set primary and secondary event data
    event.primaryEventData = concernRoleDtls.concernRoleID;
    event.secondaryEventData = key.caseID;

    // Raise unsuspend event
    EventService.raiseEvent(event);
  }

  @Override
  public curam.ca.gc.bdm.facade.productdelivery.struct.BDMDeductionDetails
    readDeductionDetails(final ReadDeductionKey key)
      throws AppException, InformationalException {

    final curam.ca.gc.bdm.facade.productdelivery.struct.BDMDeductionDetails details =
      new curam.ca.gc.bdm.facade.productdelivery.struct.BDMDeductionDetails();

    details.dtls = super.readDeduction1(key);
    details.dojDtls =
      maintainDeductionDetailsObj.readDOJDeductionDetails(key);
    return details;
  }

}
