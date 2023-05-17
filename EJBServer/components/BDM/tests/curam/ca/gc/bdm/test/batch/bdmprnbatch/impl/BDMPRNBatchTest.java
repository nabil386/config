package curam.ca.gc.bdm.test.batch.bdmprnbatch.impl;

import curam.ca.gc.bdm.batch.bdmgeneratepaymentstagingdatabatch.struct.GeneratePaymentsFileKey;
import curam.ca.gc.bdm.batch.bdmpaymentreturn.fact.BDMPRNBatchFactory;
import curam.ca.gc.bdm.batch.bdmpaymentreturn.intf.BDMPRNBatch;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPRNStagingDataFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPRNStagingData;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPRNStagingDataDtls;
import curam.ca.gc.bdm.test.framework.CuramServerTest;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.PMTRECONCILIATIONSTATUS;
import curam.core.facade.fact.UniqueIDFactory;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import org.junit.Test;

/**
 * This Test class tests the Payment Return batch
 * Process Stream Batch.
 * Task-13471
 *
 * @author Chandan
 *
 */
public class BDMPRNBatchTest extends CuramServerTest {

  public BDMPRNBatchTest(final String arg0) {

    super(arg0);

  }

  public void testSetupData() throws AppException, InformationalException {

    final BDMPRNStagingData bdmprnStagingData =
      BDMPRNStagingDataFactory.newInstance();
    final BDMPRNStagingDataDtls dataDtls = new BDMPRNStagingDataDtls();
    dataDtls.recordID = UniqueIDFactory.newInstance().getNextID().uniqueID;
    dataDtls.concernRoleID =
      UniqueIDFactory.newInstance().getNextID().uniqueID;
    ;
    dataDtls.payeeName = "test payee";
    dataDtls.reconcileStatusCode = PMTRECONCILIATIONSTATUS.PROCESSED;
    dataDtls.paymentDate = Date.getCurrentDate();
    dataDtls.pmtInstrumentID =
      UniqueIDFactory.newInstance().getNextID().uniqueID;
    dataDtls.processingDateTime = Date.getCurrentDate().getDateTime();
    bdmprnStagingData.insert(dataDtls);

  }

  @Test
  public void testbatchProcessList()
    throws AppException, InformationalException {

    final BDMPRNBatch batchObj = BDMPRNBatchFactory.newInstance();
    final GeneratePaymentsFileKey fileKey = new GeneratePaymentsFileKey();
    fileKey.instanceID = BATCHPROCESSNAME.BDM_PAYMENT_RETURN_BATCH;
    batchObj.process(fileKey);

  }

}
