package curam.ca.gc.bdm.test.batch.corrspondence.bulkprint;

import curam.ca.gc.bdm.batch.correspondences.bulkprint.fact.BDMCorrespondenceBulkPrintBatchFactory;
import curam.ca.gc.bdm.batch.correspondences.bulkprint.fact.BDMCorrespondenceBulkPrintBatchStreamFactory;
import curam.ca.gc.bdm.batch.correspondences.bulkprint.intf.BDMCorrespondenceBulkPrintBatch;
import curam.ca.gc.bdm.batch.correspondences.bulkprint.intf.BDMCorrespondenceBulkPrintBatchStream;
import curam.ca.gc.bdm.batch.correspondences.bulkprint.struct.BDMCorrespondenceBulkPrintBatchKey;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.BATCHPROCESSNAME;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import org.junit.Test;

public class BDMCorrespondenceBulkPrintBatchTest
  extends CuramServerTestJUnit4 {

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
  }

  public BatchProcessingIDList getBatchProccessingList()
    throws AppException, InformationalException {

    class InnerBDMCorrespondenceBulkPrintBatch1 extends
      curam.ca.gc.bdm.batch.correspondences.bulkprint.impl.BDMCorrespondenceBulkPrintBatch {

      InnerBDMCorrespondenceBulkPrintBatch1() {

        super();
      }

      public BatchProcessingIDList _getBatchProcessingIDList()
        throws AppException, InformationalException {

        return this.getBatchProcessingIDList();
      }
    }
    final InnerBDMCorrespondenceBulkPrintBatch1 innerBatchClass =
      new InnerBDMCorrespondenceBulkPrintBatch1();
    final BatchProcessingIDList processidList =
      innerBatchClass._getBatchProcessingIDList();
    Trace.kTopLevelLogger.info("Processing ID List: ");
    for (final BatchProcessingID processid : processidList.dtls.items()) {
      Trace.kTopLevelLogger.info(processid.recordID);

    }
    return processidList;
  }

  @Test
  public void testbatchProcess() throws AppException, InformationalException {

    Configuration.setProperty(
      EnvVars.BDM_CORRESPONDENCE__BULK_PRINT_BATCH_DONT_RUN_STREAM, "false");

    final BDMCorrespondenceBulkPrintBatch batchStreamObj =
      BDMCorrespondenceBulkPrintBatchFactory.newInstance();

    final BDMCorrespondenceBulkPrintBatchKey batchChunkKey =
      new BDMCorrespondenceBulkPrintBatchKey();
    batchChunkKey.instanceID =
      BATCHPROCESSNAME.BDM_CORRESPONDENCE_BULK_PRINT_BATCH;
    batchStreamObj.process(batchChunkKey);
  }

  @Test
  public void testbatchProcessSynchronous() throws IllegalStateException,
    SecurityException, HeuristicMixedException, HeuristicRollbackException,
    RollbackException, SystemException, Exception {

    final BatchProcessingIDList processingList = getBatchProccessingList();
    final BDMCorrespondenceBulkPrintBatchStream batchStreamObj =
      BDMCorrespondenceBulkPrintBatchStreamFactory.newInstance();
    for (final BatchProcessingID processingID : processingList.dtls.items()) {
      batchStreamObj.processRecord(processingID);
    }

  }

  @Test
  public void testBulkPrintBatchCommit() throws IllegalStateException,
    SecurityException, HeuristicMixedException, HeuristicRollbackException,
    RollbackException, SystemException, Exception {

    testbatchProcess();

  }

}
