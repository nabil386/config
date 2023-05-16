package curam.ca.gc.bdm.facade.financial.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.financial.struct.BDMReadPaymentInstructionDetails1;
import curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf.BDMMaintainPaymentDestination;
import curam.ca.gc.bdm.sl.financial.maintainpaymentinstrument.impl.MaintainPaymentInstrument;
import curam.ca.gc.bdm.sl.struct.BDMViewCaseFinancialInstructionDetailsList;
import curam.core.facade.fact.FinancialFactory;
import curam.core.facade.struct.CreateNomineeDetails;
import curam.core.facade.struct.InformationMsgDtlsList;
import curam.core.facade.struct.ModifyNomineeStatusDetails;
import curam.core.facade.struct.ReadPaymentInstructionDetails1;
import curam.core.facade.struct.ReadPaymentInstructionKey;
import curam.core.facade.struct.SetDefaultNomineeKey;
import curam.core.sl.struct.AssignObjectiveAndDelPattKey;
import curam.core.sl.struct.CreateCaseNomineeProdDelPattDetails;
import curam.core.struct.CancelRegenerateOrInvalidatePmtDetails;
import curam.core.struct.CaseNomineeID;
import curam.core.struct.FinInstructionID;
import curam.core.struct.FinancialInstructionKey;
import curam.core.struct.RegeneratePaymentForNomineeDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMFinancial
  extends curam.ca.gc.bdm.facade.financial.base.BDMFinancial {

  @Inject
  private curam.ca.gc.bdm.sl.financial.intf.BDMFinancial bdmFinancialObj;

  @Inject
  private MaintainPaymentInstrument maintainPaymentInstrumentObj;

  @Inject
  private BDMMaintainPaymentDestination maintainPaymentDestinationObj;

  public BDMFinancial() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Gathers the payment details from the finInstructionID
   */
  @Override
  public BDMReadPaymentInstructionDetails1
    readPaymentInstruction1(final ReadPaymentInstructionKey key)
      throws AppException, InformationalException {

    final BDMReadPaymentInstructionDetails1 details =
      new BDMReadPaymentInstructionDetails1();

    // call OOTB to get majority of the details
    final ReadPaymentInstructionDetails1 readPaymentInstruction1 =
      FinancialFactory.newInstance().readPaymentInstruction1(key);

    details.dtls = readPaymentInstruction1;

    bdmFinancialObj.addStatusReferenceNumber(key, details);

    return details;
  }

  /**
   * Gets list of all linked cancelled payments and reversals
   */
  @Override
  public BDMViewCaseFinancialInstructionDetailsList
    readLinkedCancelledReversedPayments(
      final FinancialInstructionKey caseIDFinInstrIDKey)
      throws AppException, InformationalException {

    final BDMViewCaseFinancialInstructionDetailsList viewCaseFinancialInstructions =
      bdmFinancialObj.readLinkedCancelledReversedPayments(
        caseIDFinInstrIDKey.finInstructionID);

    return viewCaseFinancialInstructions;
  }

  @Override
  public void regeneratePaymentForNominee(
    final RegeneratePaymentForNomineeDetails details)
    throws AppException, InformationalException {

    FinancialFactory.newInstance().regeneratePaymentForNominee(details);

    final FinInstructionID finInstructionKey = new FinInstructionID();
    finInstructionKey.finInstructionID = details.finInstructionID;

    maintainPaymentInstrumentObj
      .triggerMatchingPaymentInstrumentEvent(finInstructionKey);

  }

  @Override
  public InformationMsgDtlsList invalidateCanceledPayment(
    final CancelRegenerateOrInvalidatePmtDetails details)
    throws AppException, InformationalException {

    final InformationMsgDtlsList msgList =
      FinancialFactory.newInstance().invalidateCanceledPayment(details);

    final FinInstructionID finInstructionKey = new FinInstructionID();
    finInstructionKey.finInstructionID = details.finInstructionID;

    maintainPaymentInstrumentObj
      .triggerMatchingPaymentInstrumentEvent(finInstructionKey);

    return msgList;
  }

  @Override
  public InformationMsgDtlsList assignObjective1(
    final AssignObjectiveAndDelPattKey assignObjectiveAndDelPattKey)
    throws AppException, InformationalException {

    return bdmFinancialObj.assignObjective1(assignObjectiveAndDelPattKey);
  }

  @Override
  public CaseNomineeID createCaseNominee1(final CreateNomineeDetails details)
    throws AppException, InformationalException {

    return bdmFinancialObj.createCaseNominee1(details);
  }

  @Override
  public InformationMsgDtlsList
    setDefaultNominee(final SetDefaultNomineeKey setDefaultNomineeKey)
      throws AppException, InformationalException {

    return bdmFinancialObj.setDefaultNominee(setDefaultNomineeKey);
  }

  @Override
  public InformationMsgDtlsList createCaseNomineeProdDelPattern(
    final CreateCaseNomineeProdDelPattDetails createCaseNomineeProdDelPattDetails)
    throws AppException, InformationalException {

    return bdmFinancialObj
      .createCaseNomineeProdDelPattern(createCaseNomineeProdDelPattDetails);
  }

  @Override
  public void reactivateCaseNominee1(final ModifyNomineeStatusDetails details)
    throws AppException, InformationalException {

    bdmFinancialObj.reactivateCaseNominee1(details);

  }

}
