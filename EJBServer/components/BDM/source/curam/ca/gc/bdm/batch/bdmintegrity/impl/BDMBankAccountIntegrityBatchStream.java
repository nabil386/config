package curam.ca.gc.bdm.batch.bdmintegrity.impl;

import curam.ca.gc.bdm.entity.fact.BDMBankAccountIntegrityCounterFactory;
import curam.ca.gc.bdm.entity.intf.BDMBankAccountIntegrityCounter;
import curam.ca.gc.bdm.entity.struct.BDMAccountNumberSortCodeKey;
import curam.ca.gc.bdm.entity.struct.BDMBankAccountIntegrityCounterDtls;
import curam.ca.gc.bdm.entity.struct.BDMBankAccountIntegrityCounterKey;
import curam.ca.gc.bdm.util.integrity.impl.BDMIntegrityRulesUtil;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.TASKSTATUS;
import curam.core.fact.BankAccountFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.EnvVars;
import curam.core.intf.BankAccount;
import curam.core.struct.BankAccountDtls;
import curam.core.struct.BankAccountKey;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.TaskKey;

public class BDMBankAccountIntegrityBatchStream extends
  curam.ca.gc.bdm.batch.bdmintegrity.base.BDMBankAccountIntegrityBatchStream {

  private final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    if (batchProcessStreamKey.instanceID.isEmpty()) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_BANK_ACCOUNT_INEGRITY_BATCH;
    }

    final BDMBankAccountIntegrityBatchStreamWrapper streamWrapper =
      new BDMBankAccountIntegrityBatchStreamWrapper(this);

    batchStreamHelper.runStream(batchProcessStreamKey, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    // Get the Bank Account Details from Batch Record ID
    final BankAccount bankAccountObj = BankAccountFactory.newInstance();
    final BankAccountKey bankAccountKey = new BankAccountKey();
    bankAccountKey.bankAccountID = batchProcessingID.recordID;
    final BankAccountDtls bankAccoutnDtls =
      bankAccountObj.read(bankAccountKey);

    // Search for use of the bank account details in the whole system , the
    // return list will have the size of distinct concernrole ids(handled in
    // SQL),
    final int currentCount =
      BDMIntegrityRulesUtil.getBankAccountNumberUseCount(
        bankAccoutnDtls.accountNumber, bankAccoutnDtls.bankSortCode);

    // Call read method on the BDMBankAccountIntegrityCounter entity
    final BDMBankAccountIntegrityCounter bdmBankCounterObj =
      BDMBankAccountIntegrityCounterFactory.newInstance();
    final BDMAccountNumberSortCodeKey accountNumberSortCodeKey =
      new BDMAccountNumberSortCodeKey();
    accountNumberSortCodeKey.accountNumber = bankAccoutnDtls.accountNumber;
    accountNumberSortCodeKey.bankSortCode = bankAccoutnDtls.bankSortCode;
    final NotFoundIndicator nfi = new NotFoundIndicator();
    final BDMBankAccountIntegrityCounterDtls bdmBankAccountIntegrityCounterDtls =
      bdmBankCounterObj.readBySortCodeAccountNumber(nfi,
        accountNumberSortCodeKey);
    final int inegrityReviewLimit = Integer.valueOf(Configuration.getProperty(
      EnvVars.BDM_ENV_INDIVIDUALS_PER_BANK_ACCOUNT_FOR_INTEGRITY_REVIEW));
    Trace.kTopLevelLogger.info("Current Count = " + currentCount
      + "and Threshold for review = " + inegrityReviewLimit);
    if (currentCount >= inegrityReviewLimit) {
      // if record not found or the override flag is set to true, create a
      // task
      Trace.kTopLevelLogger
        .info("Current Count is more than threshold for review");
      if (nfi.isNotFound()) {// no entries
        // create task, create entry with processInstancID for the task.
        final long processInstanceID =
          BDMIntegrityRulesUtil.createTaskForBankAccountIntegrity(
            bankAccoutnDtls.accountNumber, bankAccoutnDtls.bankSortCode);
        createBankAccountIntegrityEntry(bankAccoutnDtls, currentCount,
          bdmBankCounterObj, processInstanceID);
        Trace.kTopLevelLogger
          .info("Task Created Successfully with process instance id =  "
            + processInstanceID);
      } else {
        // Call task creation logic if flag is set to true or the current count
        // is greater than than the stored count
        if (bdmBankAccountIntegrityCounterDtls.overrideFlag
          || currentCount > bdmBankAccountIntegrityCounterDtls.count) {
          createTaskWithCondition(bankAccoutnDtls, currentCount,
            bdmBankCounterObj, bdmBankAccountIntegrityCounterDtls);
          Trace.kTopLevelLogger.info("Task Created with condition ");
        }
        // check for the flag or count to modify the entity
        if (currentCount != bdmBankAccountIntegrityCounterDtls.count
          || bdmBankAccountIntegrityCounterDtls.overrideFlag) {
          // modify the count and flag
          bdmBankAccountIntegrityCounterDtls.count = (short) currentCount;
          bdmBankAccountIntegrityCounterDtls.overrideFlag = false;
          modifyBDMBankAccountIntegrity(bdmBankCounterObj,
            bdmBankAccountIntegrityCounterDtls);
          Trace.kTopLevelLogger
            .info("Modify bdmBankAccountIntegrityCounterDtls Done.");
        }
      }
    } else {// count is less than limit
      Trace.kTopLevelLogger
        .info("Current Count is less than threshold for review");
      // if record is found , change flag and count
      if (!nfi.isNotFound()) {
        // check for the flag or count
        if (currentCount != bdmBankAccountIntegrityCounterDtls.count
          || bdmBankAccountIntegrityCounterDtls.overrideFlag) {
          // call modify on BDMBankAccountIntegrityCounter
          bdmBankAccountIntegrityCounterDtls.count = (short) currentCount;
          bdmBankAccountIntegrityCounterDtls.overrideFlag = false;
          modifyBDMBankAccountIntegrity(bdmBankCounterObj,
            bdmBankAccountIntegrityCounterDtls);
        }
      }
    }
    return null;
  }

  /**
   * Entry was found in the table BDMBankAccountIntegrityCounter , check the
   * status of task, if the task is closed or completed then create a new task
   *
   * @param bankAccoutnDtls
   * @param currentCount
   * @param bdmBankCounterObj
   * @param bdmBankAccountIntegrityCounterDtls
   * @throws AppException
   * @throws InformationalException
   */
  private void createTaskWithCondition(final BankAccountDtls bankAccoutnDtls,
    final int currentCount,
    final BDMBankAccountIntegrityCounter bdmBankCounterObj,
    final BDMBankAccountIntegrityCounterDtls bdmBankAccountIntegrityCounterDtls)
    throws AppException, InformationalException {

    // Get the task details
    TaskDtls taskDetails = new TaskDtls();
    if (bdmBankAccountIntegrityCounterDtls.linkedProcessInstanceID != 0) {
      final TaskKey taskKey = new TaskKey();
      taskKey.taskID = BDMIntegrityRulesUtil.getTaskIDFromProcessInstanceID(
        bdmBankAccountIntegrityCounterDtls.linkedProcessInstanceID);
      if (taskKey.taskID != 0l) {
        taskDetails = TaskFactory.newInstance().read(taskKey);
      }
    }
    // Check for task status
    if (bdmBankAccountIntegrityCounterDtls.linkedProcessInstanceID == 0l
      || taskDetails.taskID == 0l
      || taskDetails.status.equals(TASKSTATUS.CLOSED)
      || taskDetails.status.equals(TASKSTATUS.COMPLETED)) {
      // create a new task and set the linked task id.
      final long processInstanceID =
        BDMIntegrityRulesUtil.createTaskForBankAccountIntegrity(
          bankAccoutnDtls.accountNumber, bankAccoutnDtls.bankSortCode);
      bdmBankAccountIntegrityCounterDtls.linkedProcessInstanceID =
        processInstanceID;
    }
  }

  /**
   *
   * @param bdmBankCounterObj
   * @param bdmBankAccountIntegrityCounterDtls
   * @throws AppException
   * @throws InformationalException
   */
  private void modifyBDMBankAccountIntegrity(
    final BDMBankAccountIntegrityCounter bdmBankCounterObj,
    final BDMBankAccountIntegrityCounterDtls bdmBankAccountIntegrityCounterDtls)
    throws AppException, InformationalException {

    // call modify on entity
    final BDMBankAccountIntegrityCounterKey bdmBankAccountIntegrityCounterKey =
      new BDMBankAccountIntegrityCounterKey();
    bdmBankAccountIntegrityCounterKey.bdmBankAccountIntegrityCounterID =
      bdmBankAccountIntegrityCounterDtls.bdmBankAccountIntegrityCounterID;
    bdmBankCounterObj.modify(bdmBankAccountIntegrityCounterKey,
      bdmBankAccountIntegrityCounterDtls);
  }

  /**
   *
   * @param bankAccoutnDtls
   * @param currentCount
   * @param bdmBankCounterObj
   * @param processInstanceID
   * @throws AppException
   * @throws InformationalException
   */
  private void createBankAccountIntegrityEntry(
    final BankAccountDtls bankAccoutnDtls, final int currentCount,
    final BDMBankAccountIntegrityCounter bdmBankCounterObj,
    final long processInstanceID)
    throws AppException, InformationalException {

    // set entity details and call insert method
    final BDMBankAccountIntegrityCounterDtls dtls =
      new BDMBankAccountIntegrityCounterDtls();
    dtls.bdmBankAccountIntegrityCounterID =
      UniqueIDFactory.newInstance().getNextID();
    dtls.accountNumber = bankAccoutnDtls.accountNumber;
    dtls.bankSortCode = bankAccoutnDtls.bankSortCode;
    dtls.count = (short) currentCount;
    dtls.overrideFlag = false;
    dtls.linkedProcessInstanceID = processInstanceID;
    bdmBankCounterObj.insert(dtls);
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

}
