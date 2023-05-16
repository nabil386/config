package curam.ca.gc.bdm.rest.bdmcancelpaymentapi.impl;

import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMCancelPaymentStageFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMCancelPaymentStageDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmcancelpaymentapi.struct.BDMCancelPaymentDetails;
import curam.ca.gc.bdm.sl.financial.maintainpaymentinstrument.impl.MaintainPaymentInstrument;
import curam.citizenworkspace.rest.message.impl.RESTAPIERRORMESSAGESExceptionCreator;
import curam.core.facade.fact.UniqueIDFactory;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;

public class BDMCancelPaymentAPI implements
  curam.ca.gc.bdm.rest.bdmcancelpaymentapi.intf.BDMCancelPaymentAPI {

  public BDMCancelPaymentAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void listCancelPaymentData(final BDMCancelPaymentDetails details)
    throws AppException, InformationalException {

    // Receive a payment to cancel
    // For each cancellation, verify all required fields are present
    // If cancellation is ok, add it to a staging table
    // Scheduled batch will process cancellations

    CheckRequiredFields(details);
    cancelPayment(details);
    // AddToStaging(details);
    return;
  }

  private void CheckRequiredFields(final BDMCancelPaymentDetails in)
    throws AppException {

    // Check input for completeness
    // All fields are required or an exception is thrown
    if (in.payeeAccountNumber.isEmpty())
      throw RESTAPIERRORMESSAGESExceptionCreator
        .HTTP_400_QUERY_PARAMS_NOT_VALID();
    if (in.paymentReferenceNumber.isEmpty())
      throw RESTAPIERRORMESSAGESExceptionCreator
        .HTTP_400_QUERY_PARAMS_NOT_VALID();
    if (in.requisitionTypeCode.isEmpty())
      throw RESTAPIERRORMESSAGESExceptionCreator
        .HTTP_400_QUERY_PARAMS_NOT_VALID();
    if (in.fileName.isEmpty())
      throw RESTAPIERRORMESSAGESExceptionCreator
        .HTTP_400_QUERY_PARAMS_NOT_VALID();
  }

  // Begin-task-42330: Convert to real-time from batch
  private void cancelPayment(final BDMCancelPaymentDetails dtls)
    throws AppException, InformationalException {

    final MaintainPaymentInstrument mpi = new MaintainPaymentInstrument();

    if (dtls.requisitionTypeCode.equals(BDMConstants.kREQUISITION_TYPE_2)) {
      mpi.cancelPaymentByPRN(dtls.payeeAccountNumber,
        dtls.paymentReferenceNumber);
    }

  }
  // End-task-42330: Convert to real-time from batch

  private void AddToStaging(final BDMCancelPaymentDetails in)
    throws AppException, InformationalException {

    final BDMCancelPaymentStageDtls stagingDataDtls =
      new BDMCancelPaymentStageDtls();

    stagingDataDtls.recordID =
      UniqueIDFactory.newInstance().getNextID().uniqueID;
    stagingDataDtls.paymentReferenceNumber = in.paymentReferenceNumber;
    stagingDataDtls.payeeAccountNumber = in.payeeAccountNumber;
    stagingDataDtls.requisitionTypeCode = in.requisitionTypeCode;
    stagingDataDtls.fileName = in.fileName;
    stagingDataDtls.createdOn = Date.getCurrentDate();
    stagingDataDtls.transactionIdentificationID =
      in.transactionControlIdentificationID;

    stagingDataDtls.assign(in);
    BDMCancelPaymentStageFactory.newInstance().insert(stagingDataDtls);
  }
}
