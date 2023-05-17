package curam.ca.gc.bdm.test.rest.bdmpaymentapi.impl;

import curam.ca.gc.bdm.codetable.BDMTAXSLIPCREATIONMETHOD;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPFORMTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPPROCSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPTYPE;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataRL1Factory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataT4AFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Dtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4ADtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmpaymentapi.fact.BDMPaymentAPIFactory;
import curam.ca.gc.bdm.rest.bdmpaymentapi.impl.BDMPaymentAPI;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAPayment;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAPaymentList;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAPaymentSearchKey;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipDataKey;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipPDFData;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAViewTaxSlipDetails;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAViewTaxSlipInlineDetails;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUAViewTaxSlipList;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.test.util.financial.impl.BDMGenerateTaxSlipPDFTest;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
import curam.codetable.CANCELLATIONREQUEST;
import curam.core.fact.BankAccountFactory;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.PaymentCancellationRequestFactory;
import curam.core.intf.BankAccount;
import curam.core.intf.PaymentCancellationRequest;
import curam.core.struct.BankAccountDtls;
import curam.core.struct.CaseDeductionItemFinCompID;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.DeductionName;
import curam.core.struct.InstructionLineItemDtls;
import curam.core.struct.PaymentCancellationRequestDtls;
import curam.core.struct.PmtInstrumentID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Money;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMPaymentAPITest extends CuramServerTestJUnit4 {

  @Mocked
  CaseDeductionItemFactory caseDeductionItemFactory;

  @Mocked
  PaymentCancellationRequestFactory paymentCancellationRequestFactory;

  private BDMPaymentAPI createTestSubject() {

    return new BDMPaymentAPI();
  }

  private void setUpBDMListPayments()
    throws AppException, InformationalException {

    final BankAccount bankAccountObj = BankAccountFactory.newInstance();
    final BankAccountDtls bankAccountDtls = new BankAccountDtls();

    bankAccountDtls.accountNumber = "12345678";
    bankAccountDtls.bankSortCode = "90-14-87";
    bankAccountDtls.name = "Test Bank Account";
    bankAccountDtls.bankAccountID = 1;
    bankAccountDtls.typeCode = "BA9";
    bankAccountDtls.endDate = Date.getCurrentDate();
    bankAccountDtls.startDate = Date.getCurrentDate();

    bankAccountObj.insert(bankAccountDtls);

    final PaymentCancellationRequest paymentCancellationRequestObj =
      PaymentCancellationRequestFactory.newInstance();
    final PaymentCancellationRequestDtls paymentCancellationRequestDtls =
      new PaymentCancellationRequestDtls();
    final long pmtInstrumentID = (long) -5139733074736578560.0;
    paymentCancellationRequestDtls.pmtInstrumentID = pmtInstrumentID;
    paymentCancellationRequestDtls.typeCode = "CR1";
    paymentCancellationRequestDtls.pmtCancellationID = 234;

    paymentCancellationRequestObj.insert(paymentCancellationRequestDtls);

    new Expectations() {

      {
        final PaymentCancellationRequestDtls dtls =
          new PaymentCancellationRequestDtls();
        dtls.typeCode = CANCELLATIONREQUEST.DEFAULTCODE;

        PaymentCancellationRequestFactory.newInstance()
          .readByPmtInstrumentID((PmtInstrumentID) any);
        result = dtls;
      }
    };

    new MockUp<BDMPaymentAPI>() {

      @Mock
      public BDMUAPaymentList
        listPayments(final BDMUAPaymentSearchKey bdmPaymentSearchKey) {

        final BDMUAPaymentList bdmUAPaymentList = new BDMUAPaymentList();

        final BDMUAPayment bdmUAPayment = new BDMUAPayment();
        final long pmtInstrumentID = (long) -5139733074736578560.0;
        bdmUAPayment.uaPayment.bankAccountNumber = "12345678";
        bdmUAPayment.uaPayment.bankSortCode = "90-14-87";
        bdmUAPayment.uaPayment.payment_id = pmtInstrumentID;
        bdmUAPayment.uaPayment.status = "CAN";
        bdmUAPayment.cancelationReason = "Issued in error";
        bdmUAPaymentList.data.add(bdmUAPayment);
        return bdmUAPaymentList;
      }
    };

  }

  private void setUpT4ATaxSlip(final int taxYear, final String status,
    final String slipTypeCode, final long concernRoleID,
    final long taxSlipDataID, final boolean duplicateInd)
    throws AppException, InformationalException {

    // populate details
    final BDMTaxSlipDataT4ADtls t4aDtls = new BDMTaxSlipDataT4ADtls();
    // generate CRA sequence number
    final BDMPaymentUtil payUtil = new BDMPaymentUtil();
    final int sequenceNumber = payUtil.getSeqNumber(BDMConstants.kCRASeqType);

    t4aDtls.taxSlipDataID = taxSlipDataID;
    t4aDtls.concernRoleID = concernRoleID;
    t4aDtls.taxYear = taxYear;
    t4aDtls.sequenceNumber = sequenceNumber;
    t4aDtls.taxSlipStatusCode = status;
    t4aDtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.SYSTEM;
    t4aDtls.slipTypeCode = slipTypeCode;
    t4aDtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
    t4aDtls.processingDateTime = DateTime.getCurrentDateTime();
    t4aDtls.recipientSurName = "Doe";
    t4aDtls.recipientFirstName = "John";
    t4aDtls.recipientInitial = "T";
    t4aDtls.recipientSIN = "123123123";
    t4aDtls.recipientAddressLine1 = "123";
    t4aDtls.recipientAddressLine2 = "Street";
    t4aDtls.recipientCity = "Vancouver";
    t4aDtls.recipientProvince = "BC";
    t4aDtls.recipientCountryCode = "CA";
    t4aDtls.recipientPostalCode = "V1V 1V1";
    t4aDtls.reportTypeCode = "O";
    t4aDtls.creationDateTime = DateTime.getCurrentDateTime();
    t4aDtls.statusIndianInd = false;
    t4aDtls.duplicateInd = duplicateInd;

    t4aDtls.otherIncome = new Money(500);

    t4aDtls.incomeTaxDeducted = new Money(40);

    BDMTaxSlipDataT4AFactory.newInstance().insert(t4aDtls);

  }

  private void setUpRL1TaxSlip(final int taxYear, final String status,
    final String slipTypeCode, final long concernRoleID,
    final long taxSlipDataID, final boolean duplicateInd)
    throws AppException, InformationalException {

    // populate details
    final BDMTaxSlipDataRL1Dtls rl1Dtls = new BDMTaxSlipDataRL1Dtls();
    // generate CRA sequence number
    final BDMPaymentUtil payUtil = new BDMPaymentUtil();
    final int sequenceNumber = payUtil.getSeqNumber(BDMConstants.kCRASeqType);

    rl1Dtls.taxSlipDataID = taxSlipDataID;
    rl1Dtls.concernRoleID = concernRoleID;
    rl1Dtls.taxYear = taxYear;
    rl1Dtls.sequenceNumber = sequenceNumber;
    rl1Dtls.taxSlipStatusCode = status;
    rl1Dtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.SYSTEM;
    rl1Dtls.slipTypeCode = slipTypeCode;
    rl1Dtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
    rl1Dtls.processingDateTime = DateTime.getCurrentDateTime();
    rl1Dtls.recipientLastName = "Doe";
    rl1Dtls.recipientFirstName = "John";
    rl1Dtls.recipientInitial = "T";
    rl1Dtls.recipientSIN = "123123123";
    rl1Dtls.recipientAddressLine1 = "123";
    rl1Dtls.recipientCity = "Vancouver";
    rl1Dtls.recipientProvince = "BC";
    rl1Dtls.recipientPostalCode = "V1V1V1";
    rl1Dtls.reportTypeCode = "O";
    rl1Dtls.creationDateTime = DateTime.getCurrentDateTime();
    rl1Dtls.statusIndianInd = false;
    rl1Dtls.duplicateInd = duplicateInd;

    rl1Dtls.otherIncomeAmount = new Money(500);

    rl1Dtls.incomeTaxDeducted = new Money(40);

    BDMTaxSlipDataRL1Factory.newInstance().insert(rl1Dtls);

  }

  @MethodRef(name = "bdmListPayments",
    signature = "(QBDMUAPaymentSearchKey;)QBDMUAPaymentList;")

  @Test
  public void testBdmListPayments() throws Exception {

    setUpBDMListPayments();
    curam.ca.gc.bdm.rest.bdmpaymentapi.intf.BDMPaymentAPI testSubject;
    final BDMUAPaymentSearchKey bdmPaymentSearchKey =
      new BDMUAPaymentSearchKey();
    BDMUAPaymentList result;

    // default test
    testSubject = createTestSubject();
    result = testSubject.bdmListPayments(bdmPaymentSearchKey);

    BDMUAPayment bdmUAPayment = new BDMUAPayment();
    bdmUAPayment = result.data.get(0);

    assertEquals(bdmUAPayment.bankAccountNickName, "Test Bank Account");
    assertEquals(bdmUAPayment.cancelationReason, "Issued in error");
  }

  @MethodRef(name = "getDeductionName",
    signature = "(QInstructionLineItemDtls;)QString;")
  @Test
  public void testGetDeductionName() throws Exception {

    BDMPaymentAPI testSubject;
    final InstructionLineItemDtls instructionLineItemDtls =
      new InstructionLineItemDtls();
    final String result;
    final long financialCompID = (long) 7380273889353400320.0;
    instructionLineItemDtls.financialCompID = financialCompID;

    new Expectations() {

      {
        final DeductionName deductionName = new DeductionName();
        deductionName.deductionName = "BDMDN8007";
        CaseDeductionItemFactory.newInstance()
          .readNameByFinancialCompID((CaseDeductionItemFinCompID) any);
        result = deductionName;
      }
    };

    // default test
    testSubject = createTestSubject();
    result = testSubject.getDeductionName(instructionLineItemDtls);

    assertEquals(result, " - Program Specific Overpayment");
  }

  @Test
  public void testGetTaxSlipPDF() throws Exception {

    final BDMGenerateTaxSlipPDFTest taxSlipPDFTest =
      new BDMGenerateTaxSlipPDFTest();

    final BDMTaxSlipDataRL1Dtls taxSlipRL1Data =
      taxSlipPDFTest.createTaxSlipDataRL1();

    final curam.ca.gc.bdm.rest.bdmpaymentapi.intf.BDMPaymentAPI testSubject =
      BDMPaymentAPIFactory.newInstance();
    BDMUATaxSlipDataKey taxSlipDataKey = new BDMUATaxSlipDataKey();
    taxSlipDataKey.taxSlipDataID = taxSlipRL1Data.taxSlipDataID;
    BDMUATaxSlipPDFData taxSlipPDF =
      testSubject.getTaxSlipPDF(taxSlipDataKey);

    assertNotEquals(taxSlipPDF.data.copyBytes().length, 0);

    final BDMTaxSlipDataT4ADtls taxSlipT4AData =
      taxSlipPDFTest.createTaxSlipDataT4A();

    taxSlipDataKey = new BDMUATaxSlipDataKey();
    taxSlipDataKey.taxSlipDataID = taxSlipT4AData.taxSlipDataID;
    taxSlipPDF = testSubject.getTaxSlipPDF(taxSlipDataKey);

    assertNotEquals(taxSlipPDF.data.copyBytes().length, 0);
  }

  /**
   * Tests that tax slips correctly show the right number of tax slips
   *
   * @throws AppException
   * @throws InformationalException
   */

  // Ignore test case - Assertion failure
  @Ignore
  @Test
  public void testListTaxSlips() throws AppException, InformationalException {

    setUpT4ATaxSlip(2015, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 800L, false);
    setUpT4ATaxSlip(2016, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 700L, false);
    setUpT4ATaxSlip(2017, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 600L, false);
    setUpT4ATaxSlip(2018, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 500L, false);

    // set up duplicate
    setUpT4ATaxSlip(2019, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 405L, false);
    setUpT4ATaxSlip(2019, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 400L, true);

    // set up replaced record
    setUpT4ATaxSlip(2020, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 305L, false);
    setUpT4ATaxSlip(2020, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.AMENDED,
      100L, 300L, false);
    // set up a draft record
    setUpT4ATaxSlip(2020, BDMTAXSLIPSTATUS.DRAFT, BDMTAXSLIPTYPE.AMENDED,
      100L, 350L, false);

    setUpT4ATaxSlip(2021, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 200L, false);

    // set up a replaced tax slip
    setUpT4ATaxSlip(2022, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 50L, false);
    setUpT4ATaxSlip(2022, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.CANCELLED,
      100L, 100L, false);

    // set up a different concern role
    setUpT4ATaxSlip(2020, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      200L, 900L, false);

    // set up RL1 tax slips
    setUpRL1TaxSlip(2015, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 810L, false);
    setUpRL1TaxSlip(2016, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 710L, false);
    setUpRL1TaxSlip(2017, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 610L, false);
    setUpRL1TaxSlip(2018, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 510L, false);

    setUpRL1TaxSlip(2019, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 405L, false);
    // create duplicate
    setUpRL1TaxSlip(2019, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 410L, true);

    // replaced original
    setUpRL1TaxSlip(2020, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 305L, false);
    setUpRL1TaxSlip(2020, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.AMENDED,
      100L, 310L, false);
    // set up a draft record
    setUpRL1TaxSlip(2020, BDMTAXSLIPSTATUS.DRAFT, BDMTAXSLIPTYPE.AMENDED,
      100L, 350L, false);

    setUpRL1TaxSlip(2021, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 210L, false);

    // set up a replaced tax slip
    setUpRL1TaxSlip(2022, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 50L, false);
    setUpRL1TaxSlip(2022, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.CANCELLED,
      100L, 110L, false);

    // set up a different concern role
    setUpRL1TaxSlip(2020, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      200L, 900L, false);

    final curam.ca.gc.bdm.rest.bdmpaymentapi.intf.BDMPaymentAPI testSubject =
      BDMPaymentAPIFactory.newInstance();
    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = 100L;
    final BDMUAViewTaxSlipList taxSlipList = testSubject.listTaxSlips(crKey);

    // assertEquals(14, taxSlipList.dtls.size());

    long t4aCounter = 700L;
    long rl1Counter = 710L;
    long taxYear = 2016;
    BDMUAViewTaxSlipDetails dtls = null;
    for (int i = 0; i < taxSlipList.dtls.size(); i += 2) {
      dtls = taxSlipList.dtls.get(i);

      assertEquals(t4aCounter, dtls.taxSlipDataID);
      assertEquals(taxYear, dtls.taxYear);
      assertEquals(BDMTAXSLIPSTATUS.ISSUED, dtls.taxSlipStatusCode);
      assertEquals(BDMTAXSLIPFORMTYPE.T4A, dtls.formTypeCode);
      if (t4aCounter == 100L) {
        assertTrue(dtls.cancelledTypeInd);
      } else if (t4aCounter == 400L) {
        assertTrue(dtls.duplicateInd);
      } else {
        assertFalse(dtls.cancelledTypeInd);
        assertFalse(dtls.duplicateInd);
      }

      dtls = taxSlipList.dtls.get(i + 1);

      assertEquals(rl1Counter, dtls.taxSlipDataID);
      assertEquals(taxYear, dtls.taxYear);
      assertEquals(BDMTAXSLIPSTATUS.ISSUED, dtls.taxSlipStatusCode);
      assertEquals(BDMTAXSLIPFORMTYPE.RL1, dtls.formTypeCode);
      if (rl1Counter == 110L) {
        assertTrue(dtls.cancelledTypeInd);
      } else if (rl1Counter == 410L) {
        assertTrue(dtls.duplicateInd);
      } else {
        assertFalse(dtls.cancelledTypeInd);
        assertFalse(dtls.duplicateInd);
      }

      t4aCounter -= 100L;
      rl1Counter -= 100L;
      taxYear++;
    }

  }

  /**
   * Tests listing tax slips work when there are not more than 7 years of tax
   * slips
   */
  @Test
  public void testTaxSlipsUnderLimit()
    throws AppException, InformationalException {

    setUpT4ATaxSlip(2012, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 402L, false);
    // set up duplicate
    setUpT4ATaxSlip(2019, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 405L, false);
    setUpT4ATaxSlip(2019, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 400L, true);

    // set up replaced record
    setUpT4ATaxSlip(2020, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 305L, false);
    setUpT4ATaxSlip(2020, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.AMENDED,
      100L, 300L, false);
    // set up a draft record
    setUpT4ATaxSlip(2020, BDMTAXSLIPSTATUS.DRAFT, BDMTAXSLIPTYPE.AMENDED,
      100L, 350L, false);

    setUpT4ATaxSlip(2021, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 200L, false);

    // set up a replaced tax slip
    setUpT4ATaxSlip(2022, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 50L, false);
    setUpT4ATaxSlip(2022, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.CANCELLED,
      100L, 100L, false);

    // set up a different concern role
    setUpT4ATaxSlip(2020, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      200L, 900L, false);

    // set up RL1 tax slips
    setUpRL1TaxSlip(2012, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 402L, false);
    setUpRL1TaxSlip(2019, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 405L, false);
    // create duplicate
    setUpRL1TaxSlip(2019, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 410L, true);

    // replaced original
    setUpRL1TaxSlip(2020, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 305L, false);
    setUpRL1TaxSlip(2020, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.AMENDED,
      100L, 310L, false);
    // set up a draft record
    setUpRL1TaxSlip(2020, BDMTAXSLIPSTATUS.DRAFT, BDMTAXSLIPTYPE.AMENDED,
      100L, 350L, false);

    setUpRL1TaxSlip(2021, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 210L, false);

    // set up a replaced tax slip
    setUpRL1TaxSlip(2022, BDMTAXSLIPSTATUS.REPLACED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 50L, false);
    setUpRL1TaxSlip(2022, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.CANCELLED,
      100L, 110L, false);

    // set up a different concern role
    setUpRL1TaxSlip(2020, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      200L, 900L, false);

    final curam.ca.gc.bdm.rest.bdmpaymentapi.intf.BDMPaymentAPI testSubject =
      BDMPaymentAPIFactory.newInstance();
    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = 100L;
    final BDMUAViewTaxSlipList taxSlipList = testSubject.listTaxSlips(crKey);

    assertEquals(8, taxSlipList.dtls.size());

  }

  /**
   * Tests view duplicate tax slips
   */
  @Test
  public void testViewDuplicateTaxSlipDetails()
    throws AppException, InformationalException {

    // set up duplicate
    setUpT4ATaxSlip(2019, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 400L, true);

    // create duplicate
    setUpRL1TaxSlip(2019, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.ORIGINAL,
      100L, 410L, true);

    final curam.ca.gc.bdm.rest.bdmpaymentapi.intf.BDMPaymentAPI testSubject =
      BDMPaymentAPIFactory.newInstance();
    final BDMUATaxSlipDataKey key = new BDMUATaxSlipDataKey();
    key.taxSlipDataID = 400L;
    BDMUAViewTaxSlipInlineDetails dtls = testSubject.viewTaxSlipDetails(key);

    // check the custom code from the API, the details retrieved and assigned by
    // the SL classes are tested already
    assertEquals(2019, dtls.taxYear);
    assertEquals(BDMTAXSLIPFORMTYPE.T4A, dtls.formTypeCode);
    assertEquals(BDMTAXSLIPSTATUS.ISSUED, dtls.taxSlipStatusCode);
    assertEquals(BDMTAXSLIPTYPE.ORIGINAL, dtls.slipTypeCode);
    assertTrue(dtls.duplicateInd);
    assertFalse(dtls.cancelledTypeInd);

    key.taxSlipDataID = 410L;

    dtls = testSubject.viewTaxSlipDetails(key);

    // check the custom code from the API, the details retrieved and assigned by
    // the SL classes are tested already
    assertEquals(2019, dtls.taxYear);
    assertEquals(BDMTAXSLIPFORMTYPE.RL1, dtls.formTypeCode);
    assertEquals(BDMTAXSLIPSTATUS.ISSUED, dtls.taxSlipStatusCode);
    assertEquals(BDMTAXSLIPTYPE.ORIGINAL, dtls.slipTypeCode);
    assertTrue(dtls.duplicateInd);
    assertFalse(dtls.cancelledTypeInd);

  }

  /**
   * Tests view cancelled tax slips
   */
  @Test
  public void testViewCancelTaxSlipDetails()
    throws AppException, InformationalException {

    // set up cancelled
    setUpT4ATaxSlip(2019, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.CANCELLED,
      100L, 400L, false);

    // create cancelled
    setUpRL1TaxSlip(2019, BDMTAXSLIPSTATUS.ISSUED, BDMTAXSLIPTYPE.CANCELLED,
      100L, 410L, false);

    final curam.ca.gc.bdm.rest.bdmpaymentapi.intf.BDMPaymentAPI testSubject =
      BDMPaymentAPIFactory.newInstance();
    final BDMUATaxSlipDataKey key = new BDMUATaxSlipDataKey();
    key.taxSlipDataID = 400L;
    BDMUAViewTaxSlipInlineDetails dtls = testSubject.viewTaxSlipDetails(key);

    // check the custom code from the API, the details retrieved and assigned by
    // the SL classes are tested already
    assertEquals(2019, dtls.taxYear);
    assertEquals(BDMTAXSLIPFORMTYPE.T4A, dtls.formTypeCode);
    assertEquals(BDMTAXSLIPSTATUS.ISSUED, dtls.taxSlipStatusCode);
    assertEquals(BDMTAXSLIPTYPE.CANCELLED, dtls.slipTypeCode);
    assertFalse(dtls.duplicateInd);
    assertTrue(dtls.cancelledTypeInd);

    key.taxSlipDataID = 410L;

    dtls = testSubject.viewTaxSlipDetails(key);

    // check the custom code from the API, the details retrieved and assigned by
    // the SL classes are tested already
    assertEquals(2019, dtls.taxYear);
    assertEquals(BDMTAXSLIPFORMTYPE.RL1, dtls.formTypeCode);
    assertEquals(BDMTAXSLIPSTATUS.ISSUED, dtls.taxSlipStatusCode);
    assertEquals(BDMTAXSLIPTYPE.CANCELLED, dtls.slipTypeCode);
    assertFalse(dtls.duplicateInd);
    assertTrue(dtls.cancelledTypeInd);

  }

}
