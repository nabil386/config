package curam.ca.gc.bdm.batch.bdmgeneratepaymentsapjv.impl;

import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.GeneratePaymentsFileKey;
import curam.ca.gc.bdm.codetable.BDMFINCODECATEGORYTYPE;
import curam.ca.gc.bdm.codetable.BDMFINCODEITEMTYPE;
import curam.ca.gc.bdm.codetable.BDMJVDOCUMENTTYPE;
import curam.ca.gc.bdm.codetable.BDMPAYMENTPROCESSCYCLE;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMSAPJVStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPaymentStagingData;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMSAPJVStagingData;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtls;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtlsList;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtlsStruct2;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtlsStruct2List;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataKeyStruct2;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMSAPJVStagingDataDtls;
import curam.ca.gc.bdm.entity.fact.BDMILIStageDataFactory;
import curam.ca.gc.bdm.entity.intf.BDMILIStageData;
import curam.ca.gc.bdm.entity.struct.BDMILIStageDataDtlsStruct1;
import curam.ca.gc.bdm.entity.struct.BDMILIStageDataDtlsStruct1List;
import curam.ca.gc.bdm.entity.struct.BDMILIStageDataKeyStruct1;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
import curam.codetable.CURRENCY;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.Money;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BDMGeneratePaymentSAPJVBatch extends
  curam.ca.gc.bdm.batch.bdmgeneratepaymentsapjv.base.BDMGeneratePaymentSAPJVBatch {

  @Override
  public void process(final GeneratePaymentsFileKey key)
    throws AppException, InformationalException {

    // Task-68105: Clear the data from staging
    deleteStagingTable();

    final Map<String, BDMILIStageDataDtlsStruct1List> map = new HashMap<>();
    final Set<String> requistionSet = new HashSet<String>();
    final BDMILIStageData bdmiliObj = BDMILIStageDataFactory.newInstance();
    BDMILIStageDataKeyStruct1 keyStruct1;

    final BDMPaymentStagingDataDtlsStruct2List reqNumList;
    final BDMPaymentStagingDataKeyStruct2 reqNumKey;
    final LinkedHashMap<String, String> finItemTypeList =
      CodeTable.getAllEnabledItems(BDMFINCODEITEMTYPE.TABLENAME,
        TransactionInfo.getProgramLocale());

    BDMILIStageDataDtlsStruct1List finCategoryTypeList1;

    final BDMPaymentStagingData bdmPMTStageObj =
      BDMPaymentStagingDataFactory.newInstance();
    final double paymentSum = 0;
    BDMPaymentStagingDataDtlsList dataDtlsList =
      new BDMPaymentStagingDataDtlsList();
    try {
      dataDtlsList = bdmPMTStageObj.nkreadmulti();
    } catch (final RecordNotFoundException e) {
      Trace.kTopLevelLogger.error(
        "Record Not Found in BDMPaymentStagingData table =" + e.getMessage());
      e.printStackTrace();
    }

    /* Each requisition id search for all financial Item type */

    for (final BDMPaymentStagingDataDtls pmtDtls : dataDtlsList.dtls) {

      for (final String code : finItemTypeList.keySet()) {

        keyStruct1 = new BDMILIStageDataKeyStruct1();
        keyStruct1.financialItemType = code;
        keyStruct1.requisitionNumber = pmtDtls.requisitionNumber;
        keyStruct1.underPaymentIND = Boolean.FALSE;
        final String mapKey = pmtDtls.requisitionNumber + code;

        finCategoryTypeList1 = new BDMILIStageDataDtlsStruct1List();
        try {
          finCategoryTypeList1 =
            bdmiliObj.searchByfinancialItemType(keyStruct1);
        } catch (AppException | InformationalException e) {
          Trace.kTopLevelLogger.error(
            "search financial item type record from BDMILIStageData table error ="
              + e.getMessage());
          e.printStackTrace();
        }
        if (finCategoryTypeList1.dtls.size() > 0) {
          map.put(mapKey, finCategoryTypeList1);
        }

      }
      // requistionSet.add(pmtDtls.requisitionNumber);
    }

    // It has final mapping
    int recordNum = 0;
    for (final Entry<String, BDMILIStageDataDtlsStruct1List> entry : map
      .entrySet()) {
      final BDMILIStageDataDtlsStruct1List dataValue = entry.getValue();
      double sum = 0;
      BDMILIStageDataDtlsStruct1 iliSummaryData = null;
      for (final BDMILIStageDataDtlsStruct1 iliData : dataValue.dtls) {

        iliSummaryData = iliData;
        sum = sum + iliData.paymentAmount.getValue();
      }

      createSAPJV(iliSummaryData, sum);
      Trace.kTopLevelLogger
        .info(++recordNum + " Record Inserted into SAP Staging");
    }

    /* Need to calculate sum of each requisition number in payment staging */
    /*
     * for (final String reqNum : requistionSet) {
     *
     * reqNumList = new BDMPaymentStagingDataDtlsStruct2List();
     * BDMPaymentStagingDataDtlsStruct2 pmtDetails = null;
     *
     * reqNumKey = new BDMPaymentStagingDataKeyStruct2();
     * reqNumKey.requisitionNumber = reqNum;
     *
     * try {
     * reqNumList = bdmPMTStageObj.searchByReqNum(reqNumKey);
     * } catch (AppException | InformationalException e) {
     * Trace.kTopLevelLogger.error(
     * "search record by req number from BDMPaymentStagingData table error ="
     * + e.getMessage());
     * e.printStackTrace();
     * }
     *
     * for (final BDMPaymentStagingDataDtlsStruct2 pmtDtls : reqNumList.dtls) {
     * pmtDetails = new BDMPaymentStagingDataDtlsStruct2();
     * pmtDetails = pmtDtls;
     * paymentSum = pmtDtls.paymentAmount.getValue() + paymentSum;
     * }
     *
     *
     * createSAPJVForCash(pmtDetails, paymentSum);
     * Trace.kTopLevelLogger
     * .info("Inserted the record for Cash into SAP Staging- Job Finish");
     *
     * }
     */

  }

  // Task:68105, Delete the SAP Staging table before load new data
  private void deleteStagingTable()
    throws AppException, InformationalException {

    if (BDMSAPJVStagingDataFactory.newInstance().nkreadmulti().dtls
      .size() > 0) {
      BDMSAPJVStagingDataFactory.newInstance().nkremove();
      Trace.kTopLevelLogger.info("Delete the SAP JV Staging table");
    }

  }

  // Insert data for SAP JV
  private void createSAPJV(final BDMILIStageDataDtlsStruct1 iliSummaryData,
    final double sum) throws AppException, InformationalException {

    final BDMSAPJVStagingDataDtls sapJVDtls = new BDMSAPJVStagingDataDtls();
    final BDMPaymentUtil pmtUtilObj = new BDMPaymentUtil();
    final BDMSAPJVStagingData sapjvObj =
      BDMSAPJVStagingDataFactory.newInstance();
    final Money pmtAmt = new Money(sum);

    sapJVDtls.recordID = UniqueIDFactory.newInstance().getNextID().uniqueID;

    sapJVDtls.accountingType = CodeTable.getOneItem(
      BDMFINCODECATEGORYTYPE.TABLENAME, iliSummaryData.financialCategoryType);

    sapJVDtls.glAccountNo = iliSummaryData.gLCode;
    sapJVDtls.costCode = iliSummaryData.costCentreCode;

    sapJVDtls.financialItemType = CodeTable.getOneItem(
      BDMFINCODEITEMTYPE.TABLENAME, iliSummaryData.financialItemType);

    sapJVDtls.fundManagementCode = iliSummaryData.fundCode;
    sapJVDtls.functionalArea = iliSummaryData.functionalAreaCode;
    sapJVDtls.companyID = iliSummaryData.companyCode;
    sapJVDtls.lineItemAmount = pmtAmt;

    // pmtStageDtls = readPaymentStage(iliSummaryData.pmtInstrumentID);

    sapJVDtls.requisitionNumber = iliSummaryData.requisitionNumber;
    sapJVDtls.assignmentID = iliSummaryData.requisitionNumber;
    sapJVDtls.businessTransactionRefID = iliSummaryData.requisitionNumber;
    sapJVDtls.sequenceNo = iliSummaryData.regionSequenceNumber;
    sapJVDtls.postingDate = iliSummaryData.paymentDueDate;

    sapJVDtls.transactionID = pmtUtilObj.getGuidNumber();
    sapJVDtls.accountingTransactionDate = Date.getCurrentDate();
    sapJVDtls.messageDateTime = Date.getCurrentDate().getDateTime();

    /*
     * For the foundation its having constant value but in future value will be
     * changed.
     */
    sapJVDtls.testIndicator = Boolean.FALSE;

    sapJVDtls.paymentProcessCycle = CodeTable.getOneItem(
      BDMFINCODECATEGORYTYPE.TABLENAME, BDMPAYMENTPROCESSCYCLE.BDM_WEEKLY);

    sapJVDtls.headerText = BDMConstants.JV_HEADER_TEXT;
    sapJVDtls.messageID = BDMConstants.JV_MESSAGE;
    sapJVDtls.systemID = BDMConstants.JV_MESSAGE;
    sapJVDtls.documentTypeCode = BDMJVDOCUMENTTYPE.DEFAULTCODE;
    sapJVDtls.currencyCode = CURRENCY.DEFAULTCODE;
    sapJVDtls.languageCode = BDMConstants.gkLocaleEN;

    try {
      sapjvObj.insert(sapJVDtls);
    } catch (AppException | InformationalException e) {
      Trace.kTopLevelLogger
        .error("Insert data for SAP JV error =" + e.getMessage());
      e.printStackTrace();
    }

  }

  // Insert data for SAP JV for cash and currently most of the value is
  // constant.
  private void createSAPJVForCash(
    final BDMPaymentStagingDataDtlsStruct2 pmtDetails,
    final double paymentSum) throws AppException, InformationalException {

    final BDMSAPJVStagingData sapjvObj =
      BDMSAPJVStagingDataFactory.newInstance();
    final Money pmtAmt = new Money(paymentSum);
    final BDMSAPJVStagingDataDtls sapJVDtls = new BDMSAPJVStagingDataDtls();
    final BDMPaymentUtil pmtUtilObj = new BDMPaymentUtil();
    sapJVDtls.recordID = UniqueIDFactory.newInstance().getNextID().uniqueID;

    sapJVDtls.glAccountNo = BDMConstants.kGLAccount;
    sapJVDtls.accountingType = CodeTable.getOneItem(
      BDMFINCODECATEGORYTYPE.TABLENAME, BDMFINCODECATEGORYTYPE.BDMCASH);

    sapJVDtls.requisitionNumber = pmtDetails.requisitionNumber;
    sapJVDtls.assignmentID = pmtDetails.requisitionNumber;
    sapJVDtls.businessTransactionRefID = pmtDetails.requisitionNumber;
    sapJVDtls.sequenceNo = pmtDetails.regionSequenceNumber;
    sapJVDtls.postingDate = pmtDetails.paymentDueDate;

    sapJVDtls.transactionID = pmtUtilObj.getGuidNumber();
    sapJVDtls.accountingTransactionDate = Date.getCurrentDate();
    sapJVDtls.messageDateTime = Date.getCurrentDate().getDateTime();
    sapJVDtls.financialItemType = BDMConstants.kCASH;
    sapJVDtls.lineItemAmount = pmtAmt;

    /*
     * For the foundation its having constant value but in future value will be
     * changed.
     */
    sapJVDtls.testIndicator = Boolean.FALSE;
    sapJVDtls.paymentProcessCycle = CodeTable.getOneItem(
      BDMFINCODECATEGORYTYPE.TABLENAME, BDMPAYMENTPROCESSCYCLE.BDM_WEEKLY);
    sapJVDtls.headerText = BDMConstants.JV_HEADER_TEXT;
    sapJVDtls.messageID = BDMConstants.JV_MESSAGE;
    sapJVDtls.systemID = BDMConstants.JV_MESSAGE;
    sapJVDtls.documentTypeCode = BDMJVDOCUMENTTYPE.DEFAULTCODE;
    sapJVDtls.currencyCode = CURRENCY.DEFAULTCODE;
    sapJVDtls.languageCode = BDMConstants.gkLocaleEN;

    sapJVDtls.recordID = UniqueIDFactory.newInstance().getNextID().uniqueID;

    try {
      sapjvObj.insert(sapJVDtls);
    } catch (AppException | InformationalException e) {
      Trace.kTopLevelLogger
        .error("Insert data for SAP JV for cash =" + e.getMessage());
      e.printStackTrace();
    }

  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info("SAP JV Batch ran succesfully");

  }

}
