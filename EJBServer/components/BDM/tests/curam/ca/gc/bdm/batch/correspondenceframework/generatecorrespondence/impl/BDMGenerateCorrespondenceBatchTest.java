package curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.impl;

import curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.struct.BDMGenerateCorrespondenceBatchKey;
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

public class BDMGenerateCorrespondenceBatchTest
  extends CuramServerTestJUnit4 {

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  public BatchProcessingIDList
    getBatchProccessingList(final BDMGenerateCorrespondenceBatchKey key)
      throws AppException, InformationalException {

    class InnerBDMGenerateCorrespondenceBatch1
      extends BDMGenerateCorrespondenceBatch {

      InnerBDMGenerateCorrespondenceBatch1() {

        super();
      }

      public BatchProcessingIDList
        _getBatchProcessingIDList(final BDMGenerateCorrespondenceBatchKey key)
          throws AppException, InformationalException {

        return this.getBatchProcessingIDList(key);
      }
    }
    final InnerBDMGenerateCorrespondenceBatch1 innerBatchClass =
      new InnerBDMGenerateCorrespondenceBatch1();

    final BatchProcessingIDList processidList =
      innerBatchClass._getBatchProcessingIDList(key);
    Trace.kTopLevelLogger.info("Processing ID List: ");
    for (final BatchProcessingID processid : processidList.dtls.items()) {
      Trace.kTopLevelLogger.info(processid.recordID);

    }
    return processidList;
  }

  @Test
  public void testbatchProcess() throws AppException, InformationalException {

    Configuration.setProperty(
      EnvVars.BDM_GENERATE_CORRESPONDENCE_BATCH_DONT_RUN_STREAM, "false");

    final curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.intf.BDMGenerateCorrespondenceBatch batchStreamObj =
      curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.fact.BDMGenerateCorrespondenceBatchFactory
        .newInstance();

    final curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.struct.BDMGenerateCorrespondenceBatchKey batchChunkKey =
      new curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.struct.BDMGenerateCorrespondenceBatchKey();
    batchChunkKey.instanceID =
      BATCHPROCESSNAME.BDM_GENERATE_CORRESPONDENCE_BATCH;
    batchStreamObj.process(batchChunkKey);
  }

  @Test
  public void testbatchProcessSynchronous() throws IllegalStateException,
    SecurityException, HeuristicMixedException, HeuristicRollbackException,
    RollbackException, SystemException, Exception {

    final BDMGenerateCorrespondenceBatchKey key =
      new BDMGenerateCorrespondenceBatchKey();
    final BatchProcessingIDList processingList = getBatchProccessingList(key);
    final curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.intf.BDMGenerateCorrespondenceBatchStream batchStreamObj =
      curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.fact.BDMGenerateCorrespondenceBatchStreamFactory
        .newInstance();
    for (final BatchProcessingID processingID : processingList.dtls.items()) {
      batchStreamObj.processRecord(processingID);
    }

  }

}
