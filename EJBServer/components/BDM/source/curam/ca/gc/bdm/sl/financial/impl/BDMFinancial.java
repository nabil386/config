package curam.ca.gc.bdm.sl.financial.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.codetable.BDMCODETABLECOMBOREASON;
import curam.ca.gc.bdm.codetable.BDMFINCODEITEMTYPE;
import curam.ca.gc.bdm.codetable.BDMFINCODEREGION;
import curam.ca.gc.bdm.codetable.BDMPMTRECONCILIATIONSTATUS;
import curam.ca.gc.bdm.entity.bdminstructionlineitemrelation.fact.BDMInstructionLineItemRelationFactory;
import curam.ca.gc.bdm.entity.bdminstructionlineitemrelation.intf.BDMInstructionLineItemRelation;
import curam.ca.gc.bdm.entity.bdminstructionlineitemrelation.struct.BDMRelatedIDTypeCodeKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMCodeTableComboFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMFinancialCodeItemFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMInstructionLineItemFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentDestinationFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMCodeTableCombo;
import curam.ca.gc.bdm.entity.financial.intf.BDMFinancialCodeItem;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.struct.BDMDestinationByCaseParticipantDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemDtlsStruct1;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemKeyStruct1;
import curam.ca.gc.bdm.entity.financial.struct.BDMMatchingDestinationByNomineeDateKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.ca.gc.bdm.entity.financial.struct.ReadCodesByGroupRegionDetails;
import curam.ca.gc.bdm.entity.financial.struct.ReadCodesByGroupRegionKey;
import curam.ca.gc.bdm.entity.financial.struct.ReadSubOrdCodeByGovernTableCodeSubOrdTable;
import curam.ca.gc.bdm.entity.financial.struct.SubOrdCodeDetails;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.fact.BDMInstructionLineItemDAFactory;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.intf.BDMInstructionLineItemDA;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.BDMRelatedProductKey;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.DeductionDetailsByILI;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.ReadProductDetailsByILI;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.ReadProductDetailsByILIKey;
import curam.ca.gc.bdm.facade.financial.struct.BDMReadPaymentInstructionDetails1;
import curam.ca.gc.bdm.facade.productdelivery.impl.BDMViewPaymentsUtil;
import curam.ca.gc.bdm.facade.productdelivery.struct.BDMViewCaseInstructionDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMPAYMENTDESTINATION;
import curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf.BDMMaintainPaymentDestination;
import curam.ca.gc.bdm.sl.financial.struct.BDMILIFinancialCodingDetailsList;
import curam.ca.gc.bdm.sl.financial.struct.BDMILIFinancialCodingKey;
import curam.ca.gc.bdm.sl.financial.struct.ILIFinancialCodingDetails;
import curam.ca.gc.bdm.sl.maintaincasedeductions.intf.MaintainCaseDeductions;
import curam.ca.gc.bdm.sl.struct.BDMPostAddressChangeKey;
import curam.ca.gc.bdm.sl.struct.BDMViewCaseFinancialInstructionDetailsList;
import curam.codetable.CASERELATIONSHIPREASONCODE;
import curam.codetable.CREDITDEBIT;
import curam.codetable.DEDUCTIONNAME;
import curam.codetable.DESTINATIONTYPECODE;
import curam.codetable.FINANCIALINSTRUCTION;
import curam.codetable.FININSTRUCTIONSTATUS;
import curam.codetable.ILIRELATIONSHIP;
import curam.codetable.ILITYPE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PRODUCTNAME;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.CaseFactory;
import curam.core.facade.impl.FinancialListHelper;
import curam.core.facade.struct.CreateNomineeDetails;
import curam.core.facade.struct.InformationMsgDtlsList;
import curam.core.facade.struct.ModifyNomineeStatusDetails;
import curam.core.facade.struct.ReadPaymentInstructionKey;
import curam.core.facade.struct.SetDefaultNomineeKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.DeliveryMethodFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.fact.PaymentInstructionFactory;
import curam.core.fact.PaymentInstrumentFactory;
import curam.core.fact.ProductDeliveryPatternInfoFactory;
import curam.core.fact.ViewCaseAccountFactory;
import curam.core.intf.InstructionLineItem;
import curam.core.intf.PaymentInstruction;
import curam.core.sl.entity.fact.CaseNomineeFactory;
import curam.core.sl.entity.fact.CaseNomineeProdDelPatternFactory;
import curam.core.sl.entity.intf.CaseNomineeProdDelPattern;
import curam.core.sl.entity.struct.CaseNomineeDtls;
import curam.core.sl.entity.struct.CaseNomineeKey;
import curam.core.sl.entity.struct.CaseNomineeProdDelPatternDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.entity.struct.ReadEffectiveByDateKey;
import curam.core.sl.struct.AssignObjectiveAndDelPattKey;
import curam.core.sl.struct.CreateCaseNomineeProdDelPattDetails;
import curam.core.struct.CaseAccountIdentifier;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseNomineeID;
import curam.core.struct.CaseStartDate;
import curam.core.struct.DeliveryMethodDtls;
import curam.core.struct.DeliveryMethodKey;
import curam.core.struct.FinInstructionID;
import curam.core.struct.FinancialInstructionDetails;
import curam.core.struct.FinancialInstructionKey;
import curam.core.struct.ILIFinInstructID;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.InstructionLineItemDtlsList;
import curam.core.struct.InstructionLineItemKey;
import curam.core.struct.InstructionLineItemRelationDtls;
import curam.core.struct.InstructionLineItemRelationDtlsList;
import curam.core.struct.InvalidatedInd;
import curam.core.struct.PDPIByProdDelPatIDStatusAndDateKey;
import curam.core.struct.PaymentInstructionDtls;
import curam.core.struct.PaymentInstrumentKey;
import curam.core.struct.PrimaryILIidTypeCode;
import curam.core.struct.ProductDeliveryPatternInfoDtls;
import curam.core.struct.ViewCaseFinancialInstructionList;
import curam.core.struct.ViewCaseInstructionDetails;
import curam.message.BPOCASENOMINEEOBJECTIVE;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.util.type.ValueList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Service layer class for financial processing
 *
 */
public class BDMFinancial
  implements curam.ca.gc.bdm.sl.financial.intf.BDMFinancial {

  @Inject
  BDMMaintainPaymentDestination maintainPaymentDestinationObj;

  @Inject
  MaintainCaseDeductions maintainCaseDeductionsObj;

  public BDMFinancial() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  // Message will be populated for financial codes if the ILI type is not
  // payment instruction
  String FIN_CODE_LOOKUP_FAILURE_NON_PAYMENT_ILI =
    "Ili Item is not a Payment Instruction type";

  /**
   * Adds the BDMFinancialInstruction status and the SPS reference number to the
   * result
   */
  @Override
  public void addStatusReferenceNumber(final ReadPaymentInstructionKey key,
    final BDMReadPaymentInstructionDetails1 details)
    throws AppException, InformationalException {

    // find the associated payment instrument
    final FinInstructionID finInstructionID = new FinInstructionID();
    finInstructionID.finInstructionID = key.finInstructionID;
    final PaymentInstructionDtls paymentInstructionDtls =
      PaymentInstructionFactory.newInstance()
        .readByFinInstructionID(finInstructionID);

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    // determine if payment instrument is invalidated
    final PaymentInstrumentKey pmtInstrumentKey = new PaymentInstrumentKey();
    pmtInstrumentKey.pmtInstrumentID = paymentInstructionDtls.pmtInstrumentID;
    final InvalidatedInd invalidatedInd = PaymentInstrumentFactory
      .newInstance().readInvalidatedInd(nfIndicator, pmtInstrumentKey);

    // START
    // read the BDM Financial Instructions t
    final String statusCode =
      BDMViewPaymentsUtil.getBDMPaymentInstructionStatusCode(
        paymentInstructionDtls.pmtInstrumentID);

    final BDMPaymentInstrument bdmPaymentInstrumentObj =
      BDMPaymentInstrumentFactory.newInstance();
    final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
      new BDMPaymentInstrumentKey();
    bdmPaymentInstrumentKey.pmtInstrumentID =
      paymentInstructionDtls.pmtInstrumentID;
    final BDMPaymentInstrumentDtls bdmPaymentInstrumentDtls =
      bdmPaymentInstrumentObj.read(bdmPaymentInstrumentKey);

    // set the status
    if (!nfIndicator.isNotFound()) {
      details.bdmFinInstrStatus = BDMViewPaymentsUtil
        .getPaymentStatus(statusCode, invalidatedInd.invalidatedInd);
    } else {
      details.bdmFinInstrStatus =
        BDMViewPaymentsUtil.getPaymentStatus(statusCode, false);
    }

    // set the reference number
    details.referenceNumber = bdmPaymentInstrumentDtls.spsPmtRefNo;
    // determine if historical transactions should be displayed - ie. it is not
    // a cancelled payment or a reversal instruction
    if (statusCode.equals(BDMPMTRECONCILIATIONSTATUS.REISSUED)
      || statusCode.equals(BDMPMTRECONCILIATIONSTATUS.ISSUED)
      || statusCode.equals(BDMPMTRECONCILIATIONSTATUS.TRANSFERRED)) {
      details.displayHistoricalTransactionsInd = true;
    }

    // START update method of delivery
    if (!StringUtil
      .isNullOrEmpty(bdmPaymentInstrumentDtls.spsDeliveryMethodType)) {
      details.dtls.paymentHeaderDetails.methodOfDelivery =
        bdmPaymentInstrumentDtls.spsDeliveryMethodType;
    }
    // END - update method of delivery - JSHAH

  }

  /**
   * Gets a filtered list of cancelled payments and reversals for display that
   * are linked to the given financial instruction
   */
  @Override
  public BDMViewCaseFinancialInstructionDetailsList
    readLinkedCancelledReversedPayments(final long finInstructionID)
      throws AppException, InformationalException {

    final ILIFinInstructID iliFinInstructID = new ILIFinInstructID();
    iliFinInstructID.finInstructionID = finInstructionID;
    // retrieve all ILIs associated with teh FI
    final InstructionLineItemDtlsList instructionLineItemDtlsList =
      InstructionLineItemFactory.newInstance()
        .searchByFinInstructID(iliFinInstructID);

    if (instructionLineItemDtlsList.dtls.isEmpty()) {
      return new BDMViewCaseFinancialInstructionDetailsList();
    }

    final CaseAccountIdentifier caseAccountIdentifier =
      new CaseAccountIdentifier();
    caseAccountIdentifier.caseID =
      instructionLineItemDtlsList.dtls.get(0).caseID;
    // call ootb to retrieve list of FIs on the case
    final ViewCaseFinancialInstructionList caseFinInsList =
      ViewCaseAccountFactory.newInstance()
        .viewCaseFinancialInstructions(caseAccountIdentifier);

    final ValueList<ViewCaseInstructionDetails> finInstrDetailsList =
      caseFinInsList.dtlsList;

    // filter out any FIs that aren't in cancelled status or are of type
    // reversal
    Predicate<ViewCaseInstructionDetails> predicate =
      details -> !(details.finInstructDtls.statusCode
        .equals(FININSTRUCTIONSTATUS.CANCELLED)
        || details.finInstructDtls.typeCode
          .equals(FINANCIALINSTRUCTION.REVERSAL))
        || details.finInstructDtls.finInstructionID == finInstructionID;

    finInstrDetailsList.removeIf(predicate);

    // further filtering of FIs to only contain FIs linked to the financial
    // instruction
    final Set<Long> linkedFIs =
      determineLinkedFIs(finInstructionID, instructionLineItemDtlsList);

    predicate = details -> !linkedFIs
      .contains(details.finInstructDtls.finInstructionID);
    finInstrDetailsList.removeIf(predicate);

    setTypeStatus(caseFinInsList);

    final BDMViewCaseFinancialInstructionDetailsList bdmViewCaseFinancialInstructions =
      new BDMViewCaseFinancialInstructionDetailsList();
    bdmViewCaseFinancialInstructions.assign(caseFinInsList);

    ViewCaseInstructionDetails viewCaseInstructionDetails;
    BDMViewCaseInstructionDetails bdmViewCaseInstructionDetails;

    final FinancialInstructionKey finInsKey = new FinancialInstructionKey();
    // add processed date and bdm status
    for (int i = 0; i < caseFinInsList.dtlsList.size(); i++) {

      viewCaseInstructionDetails = caseFinInsList.dtlsList.get(i);
      bdmViewCaseInstructionDetails =
        bdmViewCaseFinancialInstructions.dtlsList.get(i);
      bdmViewCaseInstructionDetails.viewCaseInstructionDetails =
        viewCaseInstructionDetails;

      // read the status

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
      } else {
        statusCode = BDMViewPaymentsUtil.getFinInstructionStatusCode(
          bdmViewCaseInstructionDetails.viewCaseInstructionDetails.finInstructDtls.finInstructionID);
        bdmViewCaseInstructionDetails.bdmStatusCode = BDMViewPaymentsUtil
          .getPaymentStatusForFinInstruction(statusCode, isInvalidated);
      }
      // END Task - 21028 - JSHAH

    }
    return bdmViewCaseFinancialInstructions;
  }

  /**
   * Determine which FIs are linked to the financial instruction
   *
   * @param finInstructionID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public Set<Long> determineLinkedFIs(final long finInstructionID,
    final InstructionLineItemDtlsList instructionLineItemDtlsList)
    throws AppException, InformationalException {

    final Set<Long> linkedFIs = new HashSet<Long>();
    final ValueList<InstructionLineItemDtls> iliDtlsList =
      instructionLineItemDtlsList.dtls;
    // check each ILI to see if it has a RGN or REV relationship
    while (!iliDtlsList.isEmpty()) {
      final InstructionLineItemDtls iliDtls = iliDtlsList.get(0);
      addLinkedILI(linkedFIs, iliDtlsList, iliDtls.instructLineItemID);
      iliDtlsList.remove(0);
    }

    return linkedFIs;

  }

  /**
   * Finds linked RGN and REV linked FIs and adds them to list of linked FIs
   *
   * @param linkedFIs
   * @param iliDtlsList
   * @param instructLineItemID
   * @throws AppException
   * @throws InformationalException
   */
  private void addLinkedILI(final Set<Long> linkedFIs,
    final ValueList<InstructionLineItemDtls> iliDtlsList,
    final long instructLineItemID)
    throws AppException, InformationalException {

    final BDMInstructionLineItemRelation bdmIliRelationObj =
      BDMInstructionLineItemRelationFactory.newInstance();

    // search for any relationships where this ILI is the related ILI and the
    // code is RGN. This means the ILI will be the cancelled payment, which will
    // be added into iliDtlsList to be processed
    final BDMRelatedIDTypeCodeKey relatedItemIdTypeCode =
      new BDMRelatedIDTypeCodeKey();
    relatedItemIdTypeCode.relatedLineItemID = instructLineItemID;
    relatedItemIdTypeCode.typeCode = ILIRELATIONSHIP.REGENERATION;
    final InstructionLineItemRelationDtlsList searchByRelatedItemIDTypeCode =
      bdmIliRelationObj.searchByRelatedItemIDTypeCode(relatedItemIdTypeCode);

    final InstructionLineItem iliObj =
      InstructionLineItemFactory.newInstance();
    final InstructionLineItemKey iliKey = new InstructionLineItemKey();
    InstructionLineItemDtls iliDtls = null;
    for (final InstructionLineItemRelationDtls iliRgnRelation : searchByRelatedItemIDTypeCode.dtls) {

      iliKey.instructLineItemID = iliRgnRelation.instructLineItemID;
      iliDtls = iliObj.read(iliKey);
      // add to set of linked FIs
      linkedFIs.add(iliDtls.finInstructionID);
      // add to iliDtlsList for further processing
      iliDtlsList.add(iliDtls);
    }

    // search for any relationships where the relationship is REV, the related
    // item will be the reversal item.
    final PrimaryILIidTypeCode primaryILIidTypeCode =
      new PrimaryILIidTypeCode();
    primaryILIidTypeCode.instructLineItemID = instructLineItemID;
    primaryILIidTypeCode.typeCode = ILIRELATIONSHIP.REVERSALS;

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final InstructionLineItemRelationDtls iliRevRelationDtls =
      bdmIliRelationObj.readByInstLineItemIDTypeCode(nfIndicator,
        primaryILIidTypeCode);

    if (!nfIndicator.isNotFound()) {
      iliKey.instructLineItemID = iliRevRelationDtls.relatedLineItemID;
      iliDtls = iliObj.read(iliKey);
      // add to set of FIs
      linkedFIs.add(iliDtls.finInstructionID);
    }

  }

  /**
   * Sets the type status for each FI
   *
   * @param viewCaseFinancialInstructions
   * @throws AppException
   * @throws InformationalException
   */
  private void setTypeStatus(
    final ViewCaseFinancialInstructionList viewCaseFinancialInstructions)
    throws AppException, InformationalException {

    final FinancialListHelper financialListHelper = new FinancialListHelper();
    for (final ViewCaseInstructionDetails viewCaseInstructionDetails : viewCaseFinancialInstructions.dtlsList) {
      // Set instruction line item details
      FinancialInstructionDetails financialInstructionDetails =
        new FinancialInstructionDetails();

      financialInstructionDetails.assign(viewCaseInstructionDetails);
      financialInstructionDetails
        .assign(viewCaseInstructionDetails.finInstructDtls);

      financialInstructionDetails = financialListHelper
        .setListRowActionDisplayIndicators(financialInstructionDetails);

      financialInstructionDetails =
        financialListHelper.setTypeStatus(financialInstructionDetails);

      viewCaseInstructionDetails.assign(financialInstructionDetails);
    }
  }

  /**
   * Given a particular FI, if it is a non cancelled payment, add a set of
   * linked FIs to a set
   *
   * @param totalLinkedFIs
   * @param typeCode
   * @param statusCode
   * @param finInstructionID
   * @throws AppException
   * @throws InformationalException
   */
  public void addLinkedFIsToRemove(final Set<Long> totalLinkedFIs,
    final String typeCode, final String statusCode,
    final long finInstructionID) throws AppException, InformationalException {

    // if it is a non cancelled payment, check for linked payments
    if (typeCode.equals(FINANCIALINSTRUCTION.PAYMENT)
      && !statusCode.equals(FININSTRUCTIONSTATUS.CANCELLED)) {

      final ILIFinInstructID iliFinInstructIDKey = new ILIFinInstructID();
      iliFinInstructIDKey.finInstructionID = finInstructionID;
      // retrieve list of ILIs associated with the FI
      final InstructionLineItemDtlsList iliDtlsList =
        InstructionLineItemFactory.newInstance()
          .searchByFinInstructID(iliFinInstructIDKey);

      // retrieve set of FIs that are associated with the linked
      // cancelled/reversed payments
      final Set<Long> determineLinkedFIs =
        determineLinkedFIs(finInstructionID, iliDtlsList);

      // add to set of total FIs to remove
      totalLinkedFIs.addAll(determineLinkedFIs);
    }
  }

  /**
   * For a Given ILI, Get the corresponding codes based on Product type and
   * deduction type and Region code.
   */
  @Override
  public BDMILIFinancialCodingDetailsList
    getCodesByILI(final BDMILIFinancialCodingKey arg1)
      throws AppException, InformationalException {

    final BDMILIFinancialCodingDetailsList financialCodeList =
      new BDMILIFinancialCodingDetailsList();

    final InstructionLineItem lineItem =
      InstructionLineItemFactory.newInstance();

    final InstructionLineItemKey iliFinInstructIDKey =
      new InstructionLineItemKey();
    iliFinInstructIDKey.instructLineItemID = arg1.instructLineItemID;

    final InstructionLineItemDtls iliDtls =
      lineItem.read(iliFinInstructIDKey);

    String iliProductName = "";
    String iliProgramType = "";

    final BDMInstructionLineItemDA iliDA =
      BDMInstructionLineItemDAFactory.newInstance();

    if (iliDtls.caseID != 0) {
      // get product name and program type from ILI
      final ReadProductDetailsByILIKey readDetailsKey =
        new ReadProductDetailsByILIKey();
      readDetailsKey.instructLineItemID = iliDtls.instructLineItemID;
      final ReadProductDetailsByILI productDetails =
        iliDA.readBDMProductDetailsByILI(readDetailsKey);
      iliProductName = productDetails.productName;

      // if it is a payment correction, read the program type and name from the
      // related benefit
      if (productDetails.productName.equals(PRODUCTNAME.PAYMENTCORRECTION)) {
        final BDMRelatedProductKey relatedProductKey =
          new BDMRelatedProductKey();
        relatedProductKey.caseID = iliDtls.caseID;
        relatedProductKey.caseRelationPC =
          CASERELATIONSHIPREASONCODE.PAYMENTCORRECTIONCASE;
        final ReadProductDetailsByILI relatedProductDetails =
          iliDA.readRelatedProductDetails(relatedProductKey);
        iliProgramType = relatedProductDetails.programType;
      } else if (productDetails.productName
        .equals(PRODUCTNAME.BDM_OVERPAYMENT_MANUAL)) {
        iliProgramType = BDMBENEFITPROGRAMTYPE.DEFAULTCODE;
      } else {
        iliProgramType = productDetails.programType;
      }

    }

    final BDMFinancialCodeItem finCodeItemObj =
      BDMFinancialCodeItemFactory.newInstance();
    final ILIFinancialCodingDetails financialCodingDetails =
      new ILIFinancialCodingDetails();
    final ReadCodesByGroupRegionKey codeByRegionKey =
      new ReadCodesByGroupRegionKey();
    // process payment received financial codes
    if (arg1.processingType.equals(BDMConstants.kFinCodePaymentReceived)) {

      codeByRegionKey.financialItemType = BDMFINCODEITEMTYPE.CASH;
      // for now, sample program is used as deposits don't belong to any program
      // TODO: this may be changed in the future
      codeByRegionKey.programType = BDMBENEFITPROGRAMTYPE.getDefaultCode();
      codeByRegionKey.effectiveDate = iliDtls.effectiveDate;
      codeByRegionKey.provToRegionTypeCd = BDMFINCODEREGION.FINREGIONGC;

      // Get financial codes
      final ReadCodesByGroupRegionDetails regionDtls =
        finCodeItemObj.readCodesByGroupRegion(codeByRegionKey);

      financialCodingDetails.assign(regionDtls);

      financialCodingDetails.jvAmount = iliDtls.amount;
      financialCodingDetails.financialItemType = BDMFINCODEITEMTYPE.CASH;
      financialCodingDetails.programType = iliProgramType;

    } else {
      final ReadSubOrdCodeByGovernTableCodeSubOrdTable subOrdTableKey =
        new ReadSubOrdCodeByGovernTableCodeSubOrdTable();
      subOrdTableKey.governTableName = PRODUCTNAME.TABLENAME;
      subOrdTableKey.governCode = iliProductName;

      // is this a non-reversible liability
      final NotFoundIndicator nfIndicator = new NotFoundIndicator();
      final BDMInstructionLineItemKeyStruct1 caseKey =
        new BDMInstructionLineItemKeyStruct1();
      caseKey.nonReverseRelatedCaseID = iliDtls.caseID;
      final BDMInstructionLineItemDtlsStruct1 nonReverseILIDtls =
        BDMInstructionLineItemFactory.newInstance()
          .readByNonReverseCaseID(nfIndicator, caseKey);

      final BDMCodeTableCombo codeTableComboObj =
        BDMCodeTableComboFactory.newInstance();
      SubOrdCodeDetails subOrdCodeDetails = null;
      if (!nfIndicator.isNotFound()) {
        final InstructionLineItemKey iliKey = new InstructionLineItemKey();
        iliKey.instructLineItemID = nonReverseILIDtls.instructLineItemID;

        // Get deduction name and deduction type from ILI
        final DeductionDetailsByILI deductionILIDtls =
          BDMInstructionLineItemDAFactory.newInstance()
            .readBDMDeductionDetailsByILI(iliKey);
        subOrdTableKey.governTableName = DEDUCTIONNAME.TABLENAME;
        subOrdTableKey.governCode = deductionILIDtls.deductionName;
        subOrdTableKey.subOrdTableName = BDMFINCODEITEMTYPE.TABLENAME;
        subOrdTableKey.comboReasonCode =
          BDMCODETABLECOMBOREASON.NONREVERSEDFINCODE;

        subOrdCodeDetails = codeTableComboObj
          .readSubOrdCodeByGovernTableCodeSubOrdTableReason(subOrdTableKey);
      } else {
        if (iliDtls.instructionLineItemType.equals(ILITYPE.DEDUCTIONITEM)) {
          final InstructionLineItemKey iliKey = new InstructionLineItemKey();
          iliKey.instructLineItemID = arg1.instructLineItemID;

          // Get deduction name and deduction type from ILI
          final DeductionDetailsByILI deductionILIDtls =
            BDMInstructionLineItemDAFactory.newInstance()
              .readBDMDeductionDetailsByILI(iliKey);

          subOrdTableKey.governTableName = DEDUCTIONNAME.TABLENAME;
          subOrdTableKey.governCode = deductionILIDtls.deductionName;

        }
        subOrdTableKey.subOrdTableName = BDMFINCODEITEMTYPE.TABLENAME;
        subOrdCodeDetails = codeTableComboObj
          .readSubOrdCodeByGovernTableCodeSubOrdTable(subOrdTableKey);
      }

      codeByRegionKey.financialItemType = subOrdCodeDetails.subOrdCode;
      codeByRegionKey.programType = iliProgramType;
      codeByRegionKey.effectiveDate = iliDtls.effectiveDate;
      codeByRegionKey.provToRegionTypeCd = BDMFINCODEREGION.FINREGIONGC;

      // Get financial codes
      final ReadCodesByGroupRegionDetails regionDtls =
        finCodeItemObj.readCodesByGroupRegion(codeByRegionKey);

      financialCodingDetails.assign(regionDtls);

      financialCodingDetails.jvAmount = iliDtls.amount;
      financialCodingDetails.financialItemType = subOrdCodeDetails.subOrdCode;
      financialCodingDetails.programType = iliProgramType;

    }

    // set credit debit time
    if (arg1.processingType.equals(BDMConstants.kFinCodePayment)) {
      if (iliDtls.instructionLineItemType.equals(ILITYPE.DEDUCTIONITEM)) {
        financialCodingDetails.creditDebitType = CREDITDEBIT.DEBIT;
      } else {
        financialCodingDetails.creditDebitType = CREDITDEBIT.CREDIT;
      }
    } else if (arg1.processingType.equals(BDMConstants.kFinCodeLiability)
      || arg1.processingType.equals(BDMConstants.kFinCodeLiabilityReversal)) {
      financialCodingDetails.creditDebitType = CREDITDEBIT.CREDIT;
    } else if (arg1.processingType
      .equals(BDMConstants.kFinCodeLiabilityDeduction)) {
      financialCodingDetails.creditDebitType = CREDITDEBIT.DEBIT;
    } else if (arg1.processingType
      .equals(BDMConstants.kFinCodePaymentReversal)) {
      if (iliDtls.instructionLineItemType.equals(ILITYPE.DEDUCTIONITEM)) {
        financialCodingDetails.creditDebitType = CREDITDEBIT.CREDIT;
      } else {
        financialCodingDetails.creditDebitType = CREDITDEBIT.DEBIT;
      }
    } else if (arg1.processingType
      .equals(BDMConstants.kFinCodePaymentReceived)) {
      financialCodingDetails.creditDebitType = CREDITDEBIT.CREDIT;
    } else if (arg1.processingType
      .equals(BDMConstants.kFinCodePaymentReceivedAlloc)) {
      financialCodingDetails.creditDebitType = CREDITDEBIT.DEBIT;
    }

    financialCodeList.dtls.add(financialCodingDetails);

    // create financial code detail for cash JV

    if (arg1.addCashTypeInd == true && (arg1.processingType
      .equals(BDMConstants.kFinCodePayment)
      || arg1.processingType.equals(BDMConstants.kFinCodePaymentReversal))) {

      final ILIFinancialCodingDetails cashFinancialCodingDetails =
        new ILIFinancialCodingDetails();

      String financialItemType = "";

      if (arg1.utInd == true) {
        financialItemType = BDMFINCODEITEMTYPE.PMTOWING;
      } else {
        financialItemType = BDMFINCODEITEMTYPE.CASH;
      }

      codeByRegionKey.financialItemType = financialItemType;
      codeByRegionKey.programType = iliProgramType;
      codeByRegionKey.effectiveDate = iliDtls.effectiveDate;
      codeByRegionKey.provToRegionTypeCd = BDMFINCODEREGION.FINREGIONGC;

      // Get financial codes
      final ReadCodesByGroupRegionDetails regionDtls =
        finCodeItemObj.readCodesByGroupRegion(codeByRegionKey);

      cashFinancialCodingDetails.assign(regionDtls);

      // set financial code details
      cashFinancialCodingDetails.jvAmount = iliDtls.amount;
      cashFinancialCodingDetails.financialItemType = financialItemType;
      cashFinancialCodingDetails.programType = iliProgramType;

      if (arg1.processingType.equals(BDMConstants.kFinCodePayment)) {
        if (iliDtls.instructionLineItemType.equals(ILITYPE.DEDUCTIONITEM)) {
          cashFinancialCodingDetails.creditDebitType = CREDITDEBIT.CREDIT;
        } else {
          cashFinancialCodingDetails.creditDebitType = CREDITDEBIT.DEBIT;
        }
      } else if (arg1.processingType
        .equals(BDMConstants.kFinCodePaymentReversal)) {
        if (iliDtls.instructionLineItemType.equals(ILITYPE.DEDUCTIONITEM)) {
          cashFinancialCodingDetails.creditDebitType = CREDITDEBIT.DEBIT;
        } else {
          cashFinancialCodingDetails.creditDebitType = CREDITDEBIT.CREDIT;
        }
      }

      financialCodeList.dtls.add(cashFinancialCodingDetails);
    }

    // underthreshold to overthreshold JV
    if (arg1.utToOTInd == true
      && arg1.processingType.equals(BDMConstants.kFinCodePayment)) {
      final ILIFinancialCodingDetails utToOTFinancialCodingDetails =
        new ILIFinancialCodingDetails();

      codeByRegionKey.financialItemType = BDMFINCODEITEMTYPE.PMTOWING;
      codeByRegionKey.programType = iliProgramType;
      codeByRegionKey.effectiveDate = iliDtls.effectiveDate;
      codeByRegionKey.provToRegionTypeCd = BDMFINCODEREGION.FINREGIONGC;

      // Get financial codes
      final ReadCodesByGroupRegionDetails regionDtls =
        finCodeItemObj.readCodesByGroupRegion(codeByRegionKey);

      // set financial code details
      utToOTFinancialCodingDetails.assign(regionDtls);

      utToOTFinancialCodingDetails.jvAmount = iliDtls.amount;
      utToOTFinancialCodingDetails.financialItemType =
        BDMFINCODEITEMTYPE.PMTOWING;
      utToOTFinancialCodingDetails.programType = iliProgramType;

      if (iliDtls.instructionLineItemType.equals(ILITYPE.DEDUCTIONITEM)) {
        utToOTFinancialCodingDetails.creditDebitType = CREDITDEBIT.DEBIT;
      } else {
        utToOTFinancialCodingDetails.creditDebitType = CREDITDEBIT.CREDIT;
      }

      financialCodeList.dtls.add(utToOTFinancialCodingDetails);

    }

    if (arg1.processingType.equals(BDMConstants.kFinCodeLiability)
      || arg1.processingType.equals(BDMConstants.kFinCodeLiabilityReversal)) {
      final ILIFinancialCodingDetails lbyFinancialCodingDetails =
        new ILIFinancialCodingDetails();

      // is this a non-reversible liability
      final NotFoundIndicator nfIndicator = new NotFoundIndicator();
      final BDMInstructionLineItemKeyStruct1 caseKey =
        new BDMInstructionLineItemKeyStruct1();
      caseKey.nonReverseRelatedCaseID = iliDtls.caseID;
      final BDMInstructionLineItemDtlsStruct1 nonReverseILIDtls =
        BDMInstructionLineItemFactory.newInstance()
          .readByNonReverseCaseID(nfIndicator, caseKey);

      if (!nfIndicator.isNotFound()) {
        final BDMILIFinancialCodingKey financialCodingKey =
          new BDMILIFinancialCodingKey();
        financialCodingKey.instructLineItemID =
          nonReverseILIDtls.instructLineItemID;
        financialCodingKey.processingType = BDMConstants.kFinCodePayment;
        financialCodingKey.addCashTypeInd = false;
        final BDMILIFinancialCodingDetailsList codesByILI =
          getCodesByILI(financialCodingKey);

        // expect only one record
        financialCodeList.dtls.add(codesByILI.dtls.get(0));

      } else {

        final BDMRelatedProductKey relatedProductKey =
          new BDMRelatedProductKey();
        relatedProductKey.caseID = iliDtls.caseID;
        relatedProductKey.caseRelationPC =
          CASERELATIONSHIPREASONCODE.PAYMENTCORRECTIONCASE;

        boolean recordFound = false;
        ReadProductDetailsByILI relatedProductDetails =
          new ReadProductDetailsByILI();
        try {
          // see if there a related product
          relatedProductDetails =
            iliDA.readRelatedProductDetails(relatedProductKey);
          recordFound = true;
        } catch (final RecordNotFoundException e) {
          // do nothing
        }

        if (recordFound) {
          // get the fin code item type
          final BDMCodeTableCombo codeTableComboObj =
            BDMCodeTableComboFactory.newInstance();
          final ReadSubOrdCodeByGovernTableCodeSubOrdTable subOrdTableKey =
            new ReadSubOrdCodeByGovernTableCodeSubOrdTable();
          SubOrdCodeDetails subOrdCodeDetails = null;
          subOrdTableKey.governTableName = PRODUCTNAME.TABLENAME;
          subOrdTableKey.governCode = relatedProductDetails.productName;
          subOrdTableKey.subOrdTableName = BDMFINCODEITEMTYPE.TABLENAME;
          subOrdCodeDetails = codeTableComboObj
            .readSubOrdCodeByGovernTableCodeSubOrdTable(subOrdTableKey);

          codeByRegionKey.financialItemType = subOrdCodeDetails.subOrdCode;
          codeByRegionKey.programType = relatedProductDetails.programType;
          codeByRegionKey.effectiveDate = iliDtls.effectiveDate;
          codeByRegionKey.provToRegionTypeCd = BDMFINCODEREGION.FINREGIONGC;

          // Get financial codes
          final ReadCodesByGroupRegionDetails regionDtls =
            finCodeItemObj.readCodesByGroupRegion(codeByRegionKey);

          // set financial code details
          lbyFinancialCodingDetails.assign(regionDtls);
          lbyFinancialCodingDetails.jvAmount = iliDtls.amount;
          lbyFinancialCodingDetails.financialItemType =
            BDMFINCODEITEMTYPE.PMTOWING;
          lbyFinancialCodingDetails.programType = iliProgramType;
          lbyFinancialCodingDetails.creditDebitType = CREDITDEBIT.DEBIT;

          financialCodeList.dtls.add(lbyFinancialCodingDetails);

        }
      }

    }

    else if (arg1.processingType
      .equals(BDMConstants.kFinCodeLiabilityDeduction)) {

      // find related deduction payments
      final NotFoundIndicator nfIndicator = new NotFoundIndicator();
      final BDMRelatedIDTypeCodeKey relatedIDTypeCodeKey =
        new BDMRelatedIDTypeCodeKey();
      relatedIDTypeCodeKey.relatedLineItemID = arg1.instructLineItemID;
      relatedIDTypeCodeKey.typeCode = ILIRELATIONSHIP.DEDUCTIONPAYMENT;

      final InstructionLineItemRelationDtls iliRelationDtls =
        BDMInstructionLineItemRelationFactory.newInstance()
          .readByBDMRelatedInstLineItemIDTypeCode(nfIndicator,
            relatedIDTypeCodeKey);

      if (!nfIndicator.isNotFound()) {
        final BDMILIFinancialCodingKey financialCodingKey =
          new BDMILIFinancialCodingKey();

        financialCodingKey.instructLineItemID =
          iliRelationDtls.instructLineItemID;
        financialCodingKey.processingType = BDMConstants.kFinCodePayment;
        financialCodingKey.addCashTypeInd = false;
        final BDMILIFinancialCodingDetailsList codesByILI =
          getCodesByILI(financialCodingKey);

        // expect only one record
        codesByILI.dtls.get(0).creditDebitType = CREDITDEBIT.CREDIT;
        financialCodeList.dtls.add(codesByILI.dtls.get(0));
      }

    }

    return financialCodeList;
  }

  /**
   * Before assigning an objective, ensure that the nominee's delivery pattern
   * matches the method specified in payment destination
   */
  @Override
  public InformationMsgDtlsList assignObjective1(
    final AssignObjectiveAndDelPattKey assignObjectiveAndDelPattKey)
    throws AppException, InformationalException {

    if (assignObjectiveAndDelPattKey.fromDate.isZero()
      && assignObjectiveAndDelPattKey.fromCaseStartDateInd == false) {
      throw new AppException(
        BPOCASENOMINEEOBJECTIVE.ERR_CASENOMINEEOBJECTIVE_XFV_STARTDATE_AND_IND_EMPTY);
    }

    // set overlap date as the from date
    Date overlapDate = assignObjectiveAndDelPattKey.fromDate;

    // if it should be from the case start date, set the overlap date to the
    // start date
    if (assignObjectiveAndDelPattKey.fromCaseStartDateInd) {
      final CaseHeaderKey chKey = new CaseHeaderKey();
      chKey.caseID = assignObjectiveAndDelPattKey.caseID;

      final CaseStartDate readStartDate =
        CaseHeaderFactory.newInstance().readStartDate(chKey);
      overlapDate = readStartDate.startDate;
    }

    // validate delivery pattern matches method
    final boolean doesPatternMatchMethod = validateDelPattMatchesDelMethod(
      assignObjectiveAndDelPattKey.caseNomineeID, overlapDate,
      assignObjectiveAndDelPattKey.productDeliveryPatternID);

    if (!doesPatternMatchMethod) {
      throw new AppException(
        BDMPAYMENTDESTINATION.ERR_DELIVERY_PATTERN_DOES_NOT_MATCH_METHOD);
    }

    // call super method if validation passes
    return CaseFactory.newInstance()
      .assignObjective1(assignObjectiveAndDelPattKey);

  }

  /**
   * Validates that the delivery pattern for the case nominee at the given
   * overlap date matches the payment destination method specified for the given
   * overlap date
   *
   * @param caseNomineeID
   * @param overlapDate
   * @param productDeliveryPatternID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private boolean validateDelPattMatchesDelMethod(final long caseNomineeID,
    final Date overlapDate, final long productDeliveryPatternID)
    throws AppException, InformationalException {

    final BDMMatchingDestinationByNomineeDateKey readMatchingDestinationKey =
      new BDMMatchingDestinationByNomineeDateKey();
    readMatchingDestinationKey.caseNomineeID = caseNomineeID;
    readMatchingDestinationKey.overlapDate = overlapDate;
    readMatchingDestinationKey.recordStatusCode = RECORDSTATUS.NORMAL;

    String destinationType = "";
    try {
      // find the destination that exists for the given date
      final BDMDestinationByCaseParticipantDetails destinationDtls =
        BDMPaymentDestinationFactory.newInstance()
          .readMatchingDestinationByNomineeDate(readMatchingDestinationKey);

      destinationType = destinationDtls.destinationType;
    } catch (final RecordNotFoundException e) {
      // if it doesn't exist, then it should be address
      destinationType = DESTINATIONTYPECODE.ADDRESS;
    }

    try {
      CaseNomineeProdDelPatternDtls caseNomineeProdDelPatternDtls = null;
      if (productDeliveryPatternID == 0) {
        // try to get the delivery pattern from casenomineeproddelpattern table
        final CaseNomineeProdDelPattern caseNomineeProdDelPatternObj =
          CaseNomineeProdDelPatternFactory.newInstance();

        new CaseNomineeProdDelPatternDtls();
        final ReadEffectiveByDateKey readEffectiveByDateKey =
          new ReadEffectiveByDateKey();

        readEffectiveByDateKey.caseNomineeID = caseNomineeID;
        readEffectiveByDateKey.effectiveDate = overlapDate;
        readEffectiveByDateKey.statusCode = RECORDSTATUS.NORMAL;

        caseNomineeProdDelPatternDtls = caseNomineeProdDelPatternObj
          .readByEffectiveDate(readEffectiveByDateKey);
      }

      // get the delivery method information
      final PDPIByProdDelPatIDStatusAndDateKey readProdDelPatKey =
        new PDPIByProdDelPatIDStatusAndDateKey();

      if (caseNomineeProdDelPatternDtls == null) {
        readProdDelPatKey.productDeliveryPatternID = productDeliveryPatternID;
      } else {
        readProdDelPatKey.productDeliveryPatternID =
          caseNomineeProdDelPatternDtls.productDeliveryPatternID;
      }
      readProdDelPatKey.recordStatus = RECORDSTATUS.NORMAL;
      readProdDelPatKey.effectiveDate = overlapDate;

      final ProductDeliveryPatternInfoDtls prodDelPatInfo =
        ProductDeliveryPatternInfoFactory.newInstance()
          .readNearestProdDelPatInfo(readProdDelPatKey);

      final DeliveryMethodKey deliveryMethodKey = new DeliveryMethodKey();
      deliveryMethodKey.deliveryMethodID = prodDelPatInfo.deliveryMethodID;
      final DeliveryMethodDtls deliveryMethodDtls =
        DeliveryMethodFactory.newInstance().read(deliveryMethodKey);

      // check that the delivery method and destination type for the active
      // payment destination matches
      if (deliveryMethodDtls.name.equals(METHODOFDELIVERY.EFT)
        && !destinationType.equals(DESTINATIONTYPECODE.BANKACCOUNT)
        || deliveryMethodDtls.name.equals(METHODOFDELIVERY.CHEQUE)
          && !destinationType.equals(DESTINATIONTYPECODE.ADDRESS)) {
        return false;
      }
    } catch (final RecordNotFoundException e) {
      return false;
    }

    return true;

  }

  /**
   * When a case nominee is added, create the necessary case nominee
   * destinations
   */
  @Override
  public CaseNomineeID createCaseNominee1(final CreateNomineeDetails details)
    throws AppException, InformationalException {

    final CaseNomineeID caseNomineeID =
      CaseFactory.newInstance().createCaseNominee1(details);

    final CaseNomineeKey caseNomineeKey = new CaseNomineeKey();
    caseNomineeKey.caseNomineeID = caseNomineeID.caseNomineeID;
    final CaseNomineeDtls caseNomineeDtls =
      CaseNomineeFactory.newInstance().read(caseNomineeKey);

    final CaseParticipantRoleKey cprKey = new CaseParticipantRoleKey();
    cprKey.caseParticipantRoleID = caseNomineeDtls.caseParticipantRoleID;

    maintainPaymentDestinationObj
      .updateCaseParticipantDestinationsOnly(cprKey);

    return caseNomineeID;

  }

  /**
   * Before assigning a default nominee, ensure that the nominee's delivery
   * pattern
   * matches the method specified in payment destination
   */
  @Override
  public InformationMsgDtlsList
    setDefaultNominee(final SetDefaultNomineeKey setDefaultNomineeKey)
      throws AppException, InformationalException {

    final Date overlapDate = Date.getCurrentDate();

    final boolean doesPatternMatchMethod = validateDelPattMatchesDelMethod(
      setDefaultNomineeKey.key.caseNomineeID, overlapDate, 0);

    if (!doesPatternMatchMethod) {
      throw new AppException(
        BDMPAYMENTDESTINATION.ERR_NOMINEE_DELIVERY_PATTERN_DOES_NOT_MATCH_METHOD);
    }

    return CaseFactory.newInstance().setDefaultNominee(setDefaultNomineeKey);
  }

  /**
   * After adding a delivery pattern for a nominee, update its case nominee
   * destination
   */
  @Override
  public InformationMsgDtlsList createCaseNomineeProdDelPattern(
    final CreateCaseNomineeProdDelPattDetails createCaseNomineeProdDelPattDetails)
    throws AppException, InformationalException {

    final InformationMsgDtlsList informationMsgDtlsList =
      CaseFactory.newInstance()
        .createCaseNomineeProdDelPattern(createCaseNomineeProdDelPattDetails);

    final CaseNomineeKey cnKey = new CaseNomineeKey();
    cnKey.caseNomineeID = createCaseNomineeProdDelPattDetails.caseNomineeID;
    final CaseNomineeDtls caseNomineeDtls =
      CaseNomineeFactory.newInstance().read(cnKey);

    final CaseParticipantRoleKey cprKey = new CaseParticipantRoleKey();
    cprKey.caseParticipantRoleID = caseNomineeDtls.caseParticipantRoleID;

    maintainPaymentDestinationObj
      .updateCaseParticipantDestinationsOnly(cprKey);

    return informationMsgDtlsList;
  }

  /**
   * When a nominee is reactivated, update its destination
   */
  @Override
  public void reactivateCaseNominee1(final ModifyNomineeStatusDetails details)
    throws AppException, InformationalException {

    CaseFactory.newInstance().reactivateCaseNominee1(details);

    final CaseNomineeKey caseNomineeKey = new CaseNomineeKey();
    caseNomineeKey.caseNomineeID =
      details.modifyNomineeStatusDetails.modifyNomineeStatusDetails.caseNomineeID;
    final CaseNomineeDtls caseNomineeDtls =
      CaseNomineeFactory.newInstance().read(caseNomineeKey);

    final CaseParticipantRoleKey cprKey = new CaseParticipantRoleKey();
    cprKey.caseParticipantRoleID = caseNomineeDtls.caseParticipantRoleID;

    maintainPaymentDestinationObj
      .updateCaseParticipantDestinationsOnly(cprKey);

  }

  /**
   * post address changes
   */
  @Override
  public void postAddressChange(final BDMPostAddressChangeKey key)
    throws AppException, InformationalException {

    maintainCaseDeductionsObj.syncCaseDeductionsOnAddressChange(key);
  }

}
