package curam.ca.gc.bdm.test.data;

import curam.codetable.CREDITDEBIT;
import curam.codetable.CURRENCY;
import curam.codetable.FINANCIALINSTRUCTION;
import curam.codetable.FINCOMPONENTCATEGORY;
import curam.codetable.FINCOMPONENTSTATUS;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.facade.intf.UniqueID;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.FinancialComponentFactory;
import curam.core.fact.FinancialInstructionFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.fact.InstructionLineItemRelationFactory;
import curam.core.fact.PaymentInstructionFactory;
import curam.core.fact.PaymentInstrumentFactory;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.FinancialComponentDtls;
import curam.core.struct.FinancialInstructionDtls;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.InstructionLineItemRelationDtls;
import curam.core.struct.PaymentInstructionDtls;
import curam.core.struct.PaymentInstrumentDtls;
import curam.core.struct.PrimaryAddressDetails;
import curam.paymentgroup.entity.fact.PaymentGroupFactory;
import curam.paymentgroup.entity.intf.PaymentGroup;
import curam.paymentgroup.entity.struct.PaymentGroupDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.Money;

public class BDMFinancialTestData {

  private final UniqueID uniqueIDObj;

  public BDMFinancialTestData() {

    uniqueIDObj = UniqueIDFactory.newInstance();
  }

  /**
   * Creates a payment instrument
   *
   * @param amount
   * @param invalidatedInd
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long createPaymentInstrument(final Money amount,
    final boolean invalidatedInd, final long caseNomineeID,
    final long concernRoleID) throws AppException, InformationalException {

    final PaymentInstrumentDtls pmtInstrumentDtls =
      new PaymentInstrumentDtls();

    pmtInstrumentDtls.amount = amount;
    pmtInstrumentDtls.caseNomineeID = caseNomineeID;
    pmtInstrumentDtls.concernRoleID = concernRoleID;
    pmtInstrumentDtls.invalidatedInd = invalidatedInd;
    pmtInstrumentDtls.pmtInstrumentID = uniqueIDObj.getNextID().uniqueID;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = concernRoleID;
    final PrimaryAddressDetails addressDetails =
      ConcernRoleFactory.newInstance().readPrimaryAddress(crKey);

    pmtInstrumentDtls.addressID = addressDetails.primaryAddressID;
    pmtInstrumentDtls.versionNo = 1;

    PaymentInstrumentFactory.newInstance().insert(pmtInstrumentDtls);

    return pmtInstrumentDtls.pmtInstrumentID;
  }

  /**
   * Creates a payment instrument
   *
   * @param amount
   * @param invalidatedInd
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long createPaymentInstrument(final Money amount,
    final boolean invalidatedInd, final String reconcilStatusCode,
    final long caseNomineeID, final long concernRoleID)
    throws AppException, InformationalException {

    final PaymentInstrumentDtls pmtInstrumentDtls =
      new PaymentInstrumentDtls();

    pmtInstrumentDtls.amount = amount;
    pmtInstrumentDtls.caseNomineeID = caseNomineeID;
    pmtInstrumentDtls.concernRoleID = concernRoleID;
    pmtInstrumentDtls.invalidatedInd = invalidatedInd;
    pmtInstrumentDtls.pmtInstrumentID = uniqueIDObj.getNextID().uniqueID;
    pmtInstrumentDtls.reconcilStatusCode = reconcilStatusCode;

    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = concernRoleID;
    final PrimaryAddressDetails addressDetails =
      ConcernRoleFactory.newInstance().readPrimaryAddress(crKey);

    pmtInstrumentDtls.addressID = addressDetails.primaryAddressID;
    pmtInstrumentDtls.versionNo = 1;

    PaymentInstrumentFactory.newInstance().insert(pmtInstrumentDtls);

    return pmtInstrumentDtls.pmtInstrumentID;
  }

  /**
   * Creates a payment instruction
   *
   * @param amount
   * @param finID
   * @param pmtID
   * @throws AppException
   * @throws InformationalException
   */
  public long createPaymentInstruction(final Money amount, final long finID,
    final long pmtID, final long concernRoleID)
    throws AppException, InformationalException {

    final PaymentInstructionDtls pmtInstructionDtls =
      new PaymentInstructionDtls();
    pmtInstructionDtls.pmtInstructionID = uniqueIDObj.getNextID().uniqueID;
    pmtInstructionDtls.amount = amount;
    pmtInstructionDtls.concernRoleID = concernRoleID;
    pmtInstructionDtls.finInstructionID = finID;
    pmtInstructionDtls.versionNo = 1;

    if (pmtID != 0L) {
      pmtInstructionDtls.pmtInstrumentID = pmtID;
    }

    PaymentInstructionFactory.newInstance().insert(pmtInstructionDtls);

    return pmtInstructionDtls.pmtInstructionID;

  }

  /**
   * Creates an ILI
   *
   * @param amount
   * @param finInstructionID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long createILI(final Money amount, final long finInstructionID,
    final long caseID, final long finCompID, final long caseNomineeID,
    final long concernRoleID) throws AppException, InformationalException {

    return createILI(amount, finInstructionID, 0L, caseID, finCompID,
      caseNomineeID, concernRoleID);
  }

  public long createILIWithCaseID(final Money amount,
    final long finInstructionID, final long caseID, final long finCompID,
    final long caseNomineeID, final long concernRoleID)
    throws AppException, InformationalException {

    final InstructionLineItemDtls iliDtls = new InstructionLineItemDtls();
    iliDtls.instructLineItemID = uniqueIDObj.getNextID().uniqueID;
    iliDtls.amount = amount;
    iliDtls.financialCompID = finCompID;
    iliDtls.caseID = caseID;
    iliDtls.concernRoleID = concernRoleID;
    iliDtls.caseNomineeID = caseNomineeID;
    iliDtls.primaryClientID = concernRoleID;
    iliDtls.deliveryMethodType = METHODOFDELIVERY.CHEQUE;
    iliDtls.finInstructionID = finInstructionID;
    iliDtls.versionNo = 1;

    InstructionLineItemFactory.newInstance().insert(iliDtls);

    return iliDtls.instructLineItemID;
  }

  /**
   * Creates an ILI with a payment group
   *
   * @param amount
   * @param finInstructionID
   * @param paymentGroupID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long createILI(final Money amount, final long finInstructionID,
    final long paymentGroupID, final long caseID, final long finCompID,
    final long caseNomineeID, final long concernRoleID)
    throws AppException, InformationalException {

    final InstructionLineItemDtls iliDtls = new InstructionLineItemDtls();
    iliDtls.instructLineItemID = uniqueIDObj.getNextID().uniqueID;
    iliDtls.amount = amount;
    iliDtls.financialCompID = finCompID;
    iliDtls.caseID = caseID;
    iliDtls.concernRoleID = concernRoleID;
    iliDtls.caseNomineeID = caseNomineeID;
    iliDtls.primaryClientID = concernRoleID;
    iliDtls.deliveryMethodType = METHODOFDELIVERY.CHEQUE;
    iliDtls.finInstructionID = finInstructionID;
    iliDtls.versionNo = 1;

    if (paymentGroupID != 0) {
      iliDtls.paymentGroupID = paymentGroupID;
    }
    InstructionLineItemFactory.newInstance().insert(iliDtls);

    return iliDtls.instructLineItemID;
  }

  /**
   * Creates an ILI relationship with ili1 being the main ILI, ili2 being the
   * relatedILI, and the typeCode being the relation type
   *
   * @param ili1
   * @param ili2
   * @param typeCode
   * @throws AppException
   * @throws InformationalException
   */
  public long createILIRelation(final long ili1, final long ili2,
    final String typeCode) throws AppException, InformationalException {

    final InstructionLineItemRelationDtls iliRelationDtls =
      new InstructionLineItemRelationDtls();
    iliRelationDtls.instructLineItemRelatID =
      uniqueIDObj.getNextID().uniqueID;
    iliRelationDtls.instructLineItemID = ili1;
    iliRelationDtls.relatedLineItemID = ili2;
    iliRelationDtls.typeCode = typeCode;
    iliRelationDtls.versionNo = 1;
    iliRelationDtls.creationDate = Date.getCurrentDate();

    InstructionLineItemRelationFactory.newInstance().insert(iliRelationDtls);

    return iliRelationDtls.instructLineItemRelatID;
  }

  /**
   * Creates a financial instruction item and adds it to the viewCaseFinInsList
   *
   * @param amount
   * @param typeCode
   * @param statusCode
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public long createFinancialInstruction(final Money amount,
    final String typeCode, final String statusCode, final long concernRoleID)
    throws AppException, InformationalException {

    final FinancialInstructionDtls financialInstructionDtls =
      new FinancialInstructionDtls();
    financialInstructionDtls.finInstructionID =
      uniqueIDObj.getNextID().uniqueID;
    financialInstructionDtls.concernRoleID = concernRoleID;
    financialInstructionDtls.amount = amount;
    financialInstructionDtls.typeCode = typeCode;
    financialInstructionDtls.statusCode = statusCode;
    financialInstructionDtls.creditDebitType = CREDITDEBIT.DEBIT;
    financialInstructionDtls.currencyTypeCode = CURRENCY.DEFAULTCODE;
    financialInstructionDtls.instrumentGenInd = true;
    financialInstructionDtls.versionNo = 1;
    financialInstructionDtls.effectiveDate = Date.getCurrentDate();

    FinancialInstructionFactory.newInstance()
      .insert(financialInstructionDtls);

    return financialInstructionDtls.finInstructionID;
  }

  /**
   * Creates payment groups
   *
   * @param id
   * @throws AppException
   * @throws InformationalException
   */
  private long createPaymentGroup(final long id)
    throws AppException, InformationalException {

    final PaymentGroup paymentGroupObj = PaymentGroupFactory.newInstance();
    final PaymentGroupDtls paymentGroupDtls = new PaymentGroupDtls();
    paymentGroupDtls.nameID = id;
    paymentGroupDtls.recordStatus = RECORDSTATUS.NORMAL;
    paymentGroupDtls.versionNo = 1;
    paymentGroupObj.insert(paymentGroupDtls);

    return paymentGroupDtls.paymentGroupID;
  }

  /**
   * Creates financial component.
   *
   * @param id
   * @throws AppException
   * @throws InformationalException
   */
  public long createFinancialComponent(final long caseID,
    final long caseNomineeID, final long concernRoleID)
    throws AppException, InformationalException {

    // create a financial component
    final FinancialComponentDtls finCompDtls = new FinancialComponentDtls();

    finCompDtls.financialCompID = uniqueIDObj.getNextID().uniqueID;
    finCompDtls.amount = new Money(100.00);
    finCompDtls.concernRoleID = concernRoleID;
    finCompDtls.caseID = caseID;
    finCompDtls.caseNomineeID = caseNomineeID;
    finCompDtls.primaryClientID = concernRoleID;
    finCompDtls.categoryCode = FINCOMPONENTCATEGORY.CLAIM;
    finCompDtls.statusCode = FINCOMPONENTSTATUS.LIVE;
    finCompDtls.versionNo = 1;

    FinancialComponentFactory.newInstance().insert(finCompDtls);

    return finCompDtls.financialCompID;
  }

  /**
   * Creates payment instructions, financial instructions, and a payment
   * instrument (if specified)
   *
   * @param finInstructionStatus
   * @param bdmFinInstructionStatus
   * @param paymentGroupID
   * @param shouldCreatePmtInstrument
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMFinancialTestDataDetails createPayment(
    final String finInstructionStatus, final long caseNomineeID,
    final long concernRoleID, final long caseID)
    throws AppException, InformationalException {

    final BDMFinancialTestDataDetails bdmFinancialTestDataDetails =
      new BDMFinancialTestDataDetails();

    final Money amount = new Money(100.00);
    final String statusCode = finInstructionStatus;
    final String typeCode = FINANCIALINSTRUCTION.PAYMENT;

    final long paymentGroupID = this.createPaymentGroup(100L);
    bdmFinancialTestDataDetails.paymentGroupID = paymentGroupID;

    final long financialCompID =
      this.createFinancialComponent(caseID, caseNomineeID, concernRoleID);
    bdmFinancialTestDataDetails.financialCompID = financialCompID;

    final long finInstructionID = this.createFinancialInstruction(amount,
      typeCode, statusCode, concernRoleID);
    bdmFinancialTestDataDetails.finInstructionID = finInstructionID;

    final long instructLineItemID = this.createILI(amount, finInstructionID,
      paymentGroupID, caseID, 0, caseNomineeID, concernRoleID);
    bdmFinancialTestDataDetails.instructLineItemID = instructLineItemID;

    final long pmtInstrumentID = this.createPaymentInstrument(amount, false,
      caseNomineeID, concernRoleID);
    bdmFinancialTestDataDetails.pmtInstrumentID = pmtInstrumentID;

    final long pmtInstructionID = this.createPaymentInstruction(amount,
      finInstructionID, pmtInstrumentID, concernRoleID);
    bdmFinancialTestDataDetails.pmtInstructionID = pmtInstructionID;

    return bdmFinancialTestDataDetails;
  }
}
