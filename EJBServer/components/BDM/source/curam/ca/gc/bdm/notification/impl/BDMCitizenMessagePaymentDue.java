package curam.ca.gc.bdm.notification.impl;

import com.google.inject.ImplementedBy;
import curam.advisor.impl.Parameter;
import curam.core.struct.PaymentInstrumentDtls;
import curam.participantmessages.persistence.impl.ParticipantMessage;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

@ImplementedBy(BDMCitizenMessagePaymentDueImpl.class)
public interface BDMCitizenMessagePaymentDue {

  void expirePaymentDueMessage(final ParticipantMessage participantMessage)
    throws InformationalException;

  Parameter createParticipantMessageParam(String attrName, Object value)
    throws InformationalException;

  void CreatePaymentDueMessage(PaymentInstrumentDtls paymentInstrumentDtls)
    throws InformationalException, AppException;

  void UpdatePaymentDueMessageToIssued(
    PaymentInstrumentDtls paymentInstrumentDtls)
    throws InformationalException, AppException;

}
