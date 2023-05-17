package curam.ca.gc.bdmoas.batch.bdmoasrecoverytaxoutbound.impl;

import curam.core.struct.BatchProcessChunkDtlsList;
import curam.core.struct.BatchProcessDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 *
 * @author develop.tongqi.liu
 *
 * This batch is to generate and send ‘OAS recovery tax’ request file monthly to
 * CRA for new OAS client.
 *
 */
public class BDMOASRecoveryTaxOutboundBatch implements
  curam.ca.gc.bdmoas.batch.bdmoasrecoverytaxoutbound.intf.BDMOASRecoveryTaxOutboundBatch {

  @Override
  public void process() throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  @Override
  public void sendBatchReport(final String instanceID,
    final BatchProcessDtls batchProcessDtls,
    final BatchProcessChunkDtlsList processedBatchProcessChunkDtlsList,
    final BatchProcessChunkDtlsList unprocessedBatchProcessChunkDtlsList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

}
