package curam.ca.gc.bdm.test.batch.bdmgeneratepaymentstagingsapdata.impl;

import curam.ca.gc.bdm.batch.bdmgeneratepaymentsapjv.fact.BDMGeneratePaymentSAPJVBatchFactory;
import curam.ca.gc.bdm.batch.bdmgeneratepaymentsapjv.intf.BDMGeneratePaymentSAPJVBatch;
import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.GeneratePaymentsFileKey;
import curam.ca.gc.bdm.codetable.BDMCDOCODE;
import curam.ca.gc.bdm.codetable.BDMESDCCODE;
import curam.ca.gc.bdm.codetable.BDMREGIONCODE;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtls;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtlsList;
import curam.ca.gc.bdm.entity.fact.BDMILIStageDataFactory;
import curam.ca.gc.bdm.entity.intf.BDMILIStageData;
import curam.ca.gc.bdm.entity.struct.BDMILIStageDataDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.test.framework.CuramServerTest;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.CURRENCY;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Money;
import org.junit.Test;

/**
 * This Test class tests the BDM Payment Return Batch
 * Process Stream Batch.
 *
 * @author Chandan
 *
 */
public class BDMGeneratePaymentSAPDataBatchTest extends CuramServerTest {

  public BDMGeneratePaymentSAPDataBatchTest(final String arg0) {

    super(arg0);

  }

  public void testPaymentData() throws AppException, InformationalException {

    final BDMPaymentStagingDataDtls dataDtls =
      new BDMPaymentStagingDataDtls();

    createJViliFinancialCode();
    createPaymentStaging();

    // TransactionInfo.getInfo().commit();

    BDMPaymentStagingDataDtlsList dataDtlsList =
      new BDMPaymentStagingDataDtlsList();

    dataDtlsList = BDMPaymentStagingDataFactory.newInstance().nkreadmulti();
    assertEquals(1, dataDtlsList.dtls.size());

    /*
     * final String amount = "428";
     * final Money money = new Money(amount);
     *
     * dataDtls.alternateID = "30000001";
     * dataDtls.recordID = UniqueIDFactory.newInstance().getNextID().uniqueID;
     * dataDtls.cdoCode = BDMCDOCODEEntry.BDMCDOCODE.getCode();
     * dataDtls.productName = "DPN80000";
     * dataDtls.payeeFirstForename = "John";
     * dataDtls.payeeSurname = "Doe";
     * dataDtls.pmtInstrumentID = 2948731856020832256L;
     * dataDtls.reconcileStatusCode = "PRO";
     * dataDtls.languageCode = '1';
     * dataDtls.createdBy = "test";
     * dataDtls.paymentAmount = money;
     * dataDtls.concernRoleID =
     * UniqueIDFactory.newInstance().getNextID().uniqueID;
     * dataDtls.recordType = "PSDT1";
     * dataDtls.bankBranchNum = "A11";
     * dataDtls.createdOn = Date.getCurrentDate().getDateTime();
     * dataDtls.financialInstitutionNum = "F10";
     * dataDtls.creditDebitType = "CR";
     * dataDtls.paymentInstrumentType = '1';
     * dataDtls.spsPmtRefNo = "SP";
     * dataDtls.regionCode = "gc";
     * dataDtls.postalCode = "08536";
     * // dataDtls.versionNo = 1;
     *
     * BDMPaymentStagingDataFactory.newInstance().insert(dataDtls);
     *
     * final String dedAmount = "60";
     * final Money dedmMoney = new Money(dedAmount);
     *
     * final BDMDeductionStagingDataDtls dedStagingDataDtls =
     * new BDMDeductionStagingDataDtls();
     * dedStagingDataDtls.recordID =
     * UniqueIDFactory.newInstance().getNextID().uniqueID;
     * dedStagingDataDtls.alternateID = "30000001";
     * dedStagingDataDtls.cdoCode = BDMCDOCODEEntry.BDMCDOCODE.getCode();
     * dedStagingDataDtls.concernRoleID =
     * UniqueIDFactory.newInstance().getNextID().uniqueID;
     * dedStagingDataDtls.deductionAmount = dedmMoney;
     * dedStagingDataDtls.esdcCode = BDMESDCCODE.BDMESDCCODE;
     * dedStagingDataDtls.paymentInstrumentType = '1';
     * dedStagingDataDtls.reconcileStatusCode = "PRO";
     * dedStagingDataDtls.pmtInstumentID = 2948731856020832256L;
     * dedStagingDataDtls.paymentDueDate = Date.getCurrentDate();
     * BDMDeductionStagingDataFactory.newInstance().insert(dedStagingDataDtls);
     */

  }

  private void createJViliFinancialCode()
    throws AppException, InformationalException {

    final BDMILIStageData stageDataObj = BDMILIStageDataFactory.newInstance();
    final BDMILIStageDataDtls bdmiliStageDataDtls = new BDMILIStageDataDtls();
    bdmiliStageDataDtls.pmtInstrumentID = 3029233699360079872l;
    bdmiliStageDataDtls.creationDate = Date.getCurrentDate();
    bdmiliStageDataDtls.instructionLineItemType = "C04";
    bdmiliStageDataDtls.paymentAmount = new Money(10.65);
    bdmiliStageDataDtls.instructLineItemID = -5329447209039560704l;
    bdmiliStageDataDtls.companyCode = "0140";
    bdmiliStageDataDtls.costCentreCode = "212190";
    bdmiliStageDataDtls.functionalAreaCode = "0140-0000";
    bdmiliStageDataDtls.fundCode = "R300";
    bdmiliStageDataDtls.gLCode = "211586";
    bdmiliStageDataDtls.regionTypeCd = "FCR01";
    bdmiliStageDataDtls.financialItemType = "FIT01";
    bdmiliStageDataDtls.financialCategoryType = "BDMFI02";
    bdmiliStageDataDtls.programCode = "BPT01";
    bdmiliStageDataDtls.requisitionNumber = "01476142205GC1";
    bdmiliStageDataDtls.underPaymentIND = false;
    bdmiliStageDataDtls.recordID = -5293418412020596736l;
    stageDataObj.insert(bdmiliStageDataDtls);

    bdmiliStageDataDtls.pmtInstrumentID = 3029233699360079872l;
    bdmiliStageDataDtls.creationDate = Date.getCurrentDate();
    bdmiliStageDataDtls.instructionLineItemType = "D02";
    bdmiliStageDataDtls.paymentAmount = new Money(171.42);
    bdmiliStageDataDtls.instructLineItemID = 4686558362232422400l;
    bdmiliStageDataDtls.companyCode = "0140";
    bdmiliStageDataDtls.costCentreCode = "212190";
    bdmiliStageDataDtls.functionalAreaCode = "0140-1912";
    bdmiliStageDataDtls.fundCode = "A327";
    bdmiliStageDataDtls.gLCode = "511001";
    bdmiliStageDataDtls.regionTypeCd = "FCR01";
    bdmiliStageDataDtls.financialItemType = "FIT01";
    bdmiliStageDataDtls.financialCategoryType = "BDMFI01";
    bdmiliStageDataDtls.programCode = "BPT01";
    bdmiliStageDataDtls.requisitionNumber = "01476142205GC1";
    bdmiliStageDataDtls.underPaymentIND = false;
    bdmiliStageDataDtls.recordID = 4722587159251386368l;

    stageDataObj.insert(bdmiliStageDataDtls);

  }

  private void createPaymentStaging()
    throws AppException, InformationalException {

    final BDMPaymentStagingDataDtls stagingDataDtls =
      new BDMPaymentStagingDataDtls();

    stagingDataDtls.recordID = 1912340991772196864l;

    stagingDataDtls.recordType = "PSDT1";
    stagingDataDtls.processingDateTime = DateTime.getCurrentDateTime();
    stagingDataDtls.concernRoleID = 6620854402188050432l;
    stagingDataDtls.dueDateYearMonth =
      BDMDateUtil.dateFormatToYYMM(Date.getCurrentDate().toString());
    stagingDataDtls.paymentAmount = new Money(160.77);
    stagingDataDtls.paymentInfo = "6620854402188050432|3029233699360079872";
    stagingDataDtls.alternateID = "30000004";

    stagingDataDtls.payeeName = "Test  Person";
    stagingDataDtls.deliveryAddress =
      "4994 Heatherleigh Cooksville Ontario ON";
    stagingDataDtls.postalCode = "L5A1V9";

    stagingDataDtls.pmtInstrumentID = 3029233699360079872l;
    stagingDataDtls.regionSequenceNumber = 1;
    stagingDataDtls.requisitionNumber = "01476142205GC1";
    stagingDataDtls.cdoCode = BDMCDOCODE.BDMCDOCODE;
    stagingDataDtls.esdcCode = BDMESDCCODE.BDMESDCCODE;
    stagingDataDtls.regionCode = BDMREGIONCODE.REGIONCODE;
    stagingDataDtls.currencyCode = CURRENCY.DEFAULTCODE;// Needed for future
    // Always 0 for foundation
    stagingDataDtls.paymentPriorityIND = BDMConstants.PARTY_PAY_IND;
    stagingDataDtls.payRunDate = Date.getCurrentDate();
    stagingDataDtls.expectedPayDate = Date.getCurrentDate();

    BDMPaymentStagingDataFactory.newInstance().insert(stagingDataDtls);

  }

  /*
   * public void testPaymentStagingData()
   * throws AppException, InformationalException {
   *
   * BDMPaymentStagingDataDtlsList dataDtlsList =
   * new BDMPaymentStagingDataDtlsList();
   *
   *
   *
   * }
   */

  @Test
  public void testbatchProcessList()
    throws AppException, InformationalException {

    final BDMGeneratePaymentSAPJVBatch batchObj =
      BDMGeneratePaymentSAPJVBatchFactory.newInstance();
    final GeneratePaymentsFileKey fileKey = new GeneratePaymentsFileKey();
    fileKey.instanceID = BATCHPROCESSNAME.BDM_GENERATE_PAYMENT_SAP_DATA_BATCH;
    batchObj.process(fileKey);

  }

  @Test
  public void testbatchTest() throws AppException, InformationalException {

    /*
     * final curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingsapdata.intf.
     * BDMGeneratePaymentSAPDataBatchStream batchStreamObj =
     * BDMGeneratePaymentSAPDataBatchStreamFactory.newInstance();
     * final BatchProcessingID processId = new BatchProcessingID();
     * processId.recordID = 8294504613709611008L;
     * batchStreamObj.processRecord(processId);
     */

  }
}
