package curam.ca.gc.bdm.batch.bdms34stagingdatabatch.impl;

import com.ibm.icu.util.Calendar;
import curam.ca.gc.bdm.batch.bdmbatchutil.impl.BDMBatchUtil;
import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.GeneratePaymentsFileKey;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPaymentStagingData;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataDtls;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentStagingDataKey;
import curam.ca.gc.bdm.entity.fact.BDMBatchFilenameFactory;
import curam.ca.gc.bdm.entity.fact.BDMS34StagingDataFactory;
import curam.ca.gc.bdm.entity.intf.BDMBatchFilename;
import curam.ca.gc.bdm.entity.intf.BDMS34StagingData;
import curam.ca.gc.bdm.entity.struct.BDMBatchFilenameKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMS34StagingDataDtls;
import curam.ca.gc.bdm.entity.struct.BDMS34StagingDataDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMS34StagingDataKey;
import curam.ca.gc.bdm.entity.struct.BDMS34StagingDataKeyStruct1;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.DateTime;
import org.apache.commons.lang3.StringUtils;

public class BDMS34StagingDataBatchStream extends
  curam.ca.gc.bdm.batch.bdms34stagingdatabatch.base.BDMS34StagingDataBatchStream {

  private BDMS34StagingDataBatchStreamWrapper streamWrapper;

  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_GENERATE_S34_REPORT_DATA_BATCH;
    }

    final GeneratePaymentsFileKey key = new GeneratePaymentsFileKey();
    key.instanceID = batchProcessStreamKey.instanceID;
    streamWrapper = new BDMS34StagingDataBatchStreamWrapper(this, key);
    streamWrapper.setInstanceID(batchProcessStreamKey.instanceID);

    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID,
    final GeneratePaymentsFileKey generatePaymentsFileKey)
    throws AppException, InformationalException {

    final BDMPaymentStagingData pmtStagingDataObj =
      BDMPaymentStagingDataFactory.newInstance();
    final BDMPaymentStagingDataKey key = new BDMPaymentStagingDataKey();
    key.recordID = batchProcessingID.recordID;

    // Instance of S34 Summary Report Staging Table
    final BDMS34StagingData stagingTable =
      BDMS34StagingDataFactory.newInstance();

    // Record to be inserted into staging table
    final BDMS34StagingDataDtls summDtls = new BDMS34StagingDataDtls();
    summDtls.recordID = UniqueIDFactory.newInstance().getNextID().uniqueID;

    // read payment staging table
    final BDMPaymentStagingDataDtls pmtDtls = pmtStagingDataObj.read(key);

    final BDMS34StagingDataKeyStruct1 searchKey =
      new BDMS34StagingDataKeyStruct1();
    searchKey.paymentDueDate = pmtDtls.paymentDueDate;
    searchKey.requisitionNumber = pmtDtls.requisitionNumber;

    final BDMBatchFilename filenameObj =
      BDMBatchFilenameFactory.newInstance();
    final BDMBatchFilenameKeyStruct1 reqNumberKey =
      new BDMBatchFilenameKeyStruct1();
    reqNumberKey.requisitionNumber = pmtDtls.requisitionNumber;
    reqNumberKey.instanceID = generatePaymentsFileKey.instanceID;

    if (filenameObj.searchByReqNumberAndInstanceId(reqNumberKey).dtls
      .isEmpty()) {
      final String fileName =
        generatePaymentsFileKey.filename + "_" + pmtDtls.requisitionNumber;
      final BDMBatchUtil util = new BDMBatchUtil();
      final DateTime datetime = DateTime.getCurrentDateTime();
      util.insertFilename(datetime, fileName,
        generatePaymentsFileKey.instanceID, generatePaymentsFileKey.runID,
        pmtDtls.requisitionNumber);
    }

    final BDMS34StagingDataDtlsList detailsList =
      stagingTable.searchByReqNumAndDueDate(searchKey);

    final java.util.Calendar cal = Date.getCurrentDate().getCalendar();

    if (detailsList.dtls.size() == 0) {

      if (pmtDtls.paymentInstrumentType == '1') {
        summDtls.chequePaymentCount = 1;
        summDtls.chequePaymentAmount = pmtDtls.paymentAmount.getValue();
      }
      if (pmtDtls.paymentInstrumentType == '2') {
        summDtls.ddPaymentCount = 1;
        summDtls.ddPaymentAmount = pmtDtls.paymentAmount.getValue();
      }

      // Accounting period logic
      summDtls.accountingPeriod = String.valueOf(cal.get(Calendar.YEAR))
        + String.valueOf(cal.get(Calendar.YEAR))
        + String.valueOf(cal.get(Calendar.MONTH) + 1);
      summDtls.ddPlusCPAmount = pmtDtls.paymentAmount.getValue();
      summDtls.ddPlusCPCount = 1;
      summDtls.paymentDueDate = pmtDtls.paymentDueDate;
      summDtls.paymentRunDate = Date.getCurrentDate();
      summDtls.productCode = pmtDtls.productCode;
      summDtls.regionCode = pmtDtls.regionCode;
      summDtls.requisitionNumber = pmtDtls.requisitionNumber;
      summDtls.runID = generatePaymentsFileKey.runID;

      stagingTable.insert(summDtls);
    } else {

      final BDMS34StagingDataDtls details = detailsList.dtls.get(0);

      if (pmtDtls.paymentInstrumentType == '1') {
        summDtls.chequePaymentCount = details.chequePaymentCount + 1;
        summDtls.chequePaymentAmount =
          details.chequePaymentAmount + pmtDtls.paymentAmount.getValue();
        summDtls.ddPaymentCount = details.ddPaymentCount;
        summDtls.ddPaymentAmount = details.ddPaymentAmount;
      }
      if (pmtDtls.paymentInstrumentType == '2') {
        summDtls.ddPaymentCount = details.ddPaymentCount + 1;
        summDtls.ddPaymentAmount =
          details.ddPaymentAmount + pmtDtls.paymentAmount.getValue();
        summDtls.chequePaymentCount = details.chequePaymentCount;
        summDtls.chequePaymentAmount = details.chequePaymentAmount;
      }

      // Accounting period logic
      summDtls.accountingPeriod = String.valueOf(cal.get(Calendar.YEAR))
        + String.valueOf(cal.get(Calendar.YEAR))
        + String.valueOf(cal.get(Calendar.MONTH) + 1);
      summDtls.ddPlusCPAmount =
        details.ddPlusCPAmount + pmtDtls.paymentAmount.getValue();
      summDtls.ddPlusCPCount = details.ddPlusCPCount + 1;
      summDtls.paymentRunDate = Date.getCurrentDate();
      summDtls.productCode = pmtDtls.productCode;
      summDtls.regionCode = pmtDtls.regionCode;
      summDtls.requisitionNumber = details.requisitionNumber;
      summDtls.paymentDueDate = details.paymentDueDate;

      final BDMS34StagingDataKey summKey = new BDMS34StagingDataKey();
      summKey.recordID = details.recordID;
      summDtls.runID = generatePaymentsFileKey.runID;
      stagingTable.modify(summKey, summDtls);

    }

    return null;

  }

  public void setWrapper(final BDMS34StagingDataBatchStreamWrapper wrapper) {

    this.streamWrapper = wrapper;

  }

}
