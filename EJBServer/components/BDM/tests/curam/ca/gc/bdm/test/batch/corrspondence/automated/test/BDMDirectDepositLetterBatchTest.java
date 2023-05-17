package curam.ca.gc.bdm.test.batch.corrspondence.automated.test;

import curam.ca.gc.bdm.batch.correspondences.automated.fact.BDMDirectDepositLetterBatchStreamFactory;
import curam.ca.gc.bdm.batch.correspondences.automated.intf.BDMDirectDepositLetterBatchStream;
import curam.ca.gc.bdm.test.framework.CuramServerTest;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;

public class BDMDirectDepositLetterBatchTest extends CuramServerTest {

  public BDMDirectDepositLetterBatchTest(final String arg0) {

    super(arg0);
    // TODO Auto-generated constructor stub
  }

  @Test
  public void testbatchProcessList()
    throws AppException, InformationalException {

    class InnerBDMCreateDirectDepositLetterBatch1 extends
      curam.ca.gc.bdm.batch.correspondences.automated.impl.BDMDirectDepositLetterBatch {

      InnerBDMCreateDirectDepositLetterBatch1() {

        super();
      }

      public BatchProcessingIDList _getBatchProcessingIDList()
        throws AppException, InformationalException {

        return this.getBatchProcessingIDList();
      }
    }
    final InnerBDMCreateDirectDepositLetterBatch1 innerBatchClass =
      new InnerBDMCreateDirectDepositLetterBatch1();
    final BatchProcessingIDList processidList =
      innerBatchClass._getBatchProcessingIDList();

    for (final BatchProcessingID processid : processidList.dtls.items()) {
      // System.out.println(processid.recordID);

    }
  }

  @Test
  public void testbatchTest() throws AppException, InformationalException {

    final BDMDirectDepositLetterBatchStream batchStreamObj =
      BDMDirectDepositLetterBatchStreamFactory.newInstance();
    final BatchProcessingID processId = new BatchProcessingID();
    processId.recordID = 108;
    batchStreamObj.processRecord(processId);
  }

}
