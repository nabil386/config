package curam.ca.gc.bdm.sl.financial.maintainpaymentinstrument.intf;

import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMCancelPaymentInstrumentDetails;
import curam.core.struct.FinInstructionID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface MaintainPaymentInstrument {

  public void cancelPaymentByPRN(final String memberID,
    final String paymentReferenceNumber)
    throws AppException, InformationalException;

  public void cancelBDMPayment(BDMCancelPaymentInstrumentDetails details)
    throws AppException, InformationalException;

  public void createTaskForNoPRNPersonMatch(String memberID,
    String paymentReferenceNumber, BDMPersonSearchResultDetails personDetails)
    throws AppException, InformationalException;

  public void triggerMatchingPaymentInstrumentEvent(
    final FinInstructionID finInstructionKey)
    throws AppException, InformationalException;

}
