package curam.ca.gc.bdm.sl.financial.intf;

import curam.ca.gc.bdm.facade.financial.struct.BDMReadPaymentInstructionDetails1;
import curam.ca.gc.bdm.sl.financial.struct.BDMILIFinancialCodingDetailsList;
import curam.ca.gc.bdm.sl.financial.struct.BDMILIFinancialCodingKey;
import curam.ca.gc.bdm.sl.struct.BDMPostAddressChangeKey;
import curam.ca.gc.bdm.sl.struct.BDMViewCaseFinancialInstructionDetailsList;
import curam.core.facade.struct.CreateNomineeDetails;
import curam.core.facade.struct.InformationMsgDtlsList;
import curam.core.facade.struct.ModifyNomineeStatusDetails;
import curam.core.facade.struct.ReadPaymentInstructionKey;
import curam.core.facade.struct.SetDefaultNomineeKey;
import curam.core.sl.struct.AssignObjectiveAndDelPattKey;
import curam.core.sl.struct.CreateCaseNomineeProdDelPattDetails;
import curam.core.struct.CaseNomineeID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface BDMFinancial {

  public void addStatusReferenceNumber(final ReadPaymentInstructionKey key,
    final BDMReadPaymentInstructionDetails1 details)
    throws AppException, InformationalException;

  public BDMViewCaseFinancialInstructionDetailsList

    readLinkedCancelledReversedPayments(final long finInstructionID)
      throws AppException, InformationalException;

  /**
   * for given ILI return Financial code based on region.
   *
   * @param arg1
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMILIFinancialCodingDetailsList
    getCodesByILI(final BDMILIFinancialCodingKey arg1)
      throws AppException, InformationalException;

  public InformationMsgDtlsList assignObjective1(
    final AssignObjectiveAndDelPattKey assignObjectiveAndDelPattKey)
    throws AppException, InformationalException;

  public CaseNomineeID createCaseNominee1(final CreateNomineeDetails details)
    throws AppException, InformationalException;

  public InformationMsgDtlsList
    setDefaultNominee(final SetDefaultNomineeKey setDefaultNomineeKey)
      throws AppException, InformationalException;

  public InformationMsgDtlsList createCaseNomineeProdDelPattern(
    final CreateCaseNomineeProdDelPattDetails createCaseNomineeProdDelPattDetails)
    throws AppException, InformationalException;

  public void reactivateCaseNominee1(final ModifyNomineeStatusDetails details)
    throws AppException, InformationalException;

  // TODO: change struct
  public void postAddressChange(BDMPostAddressChangeKey key)
    throws AppException, InformationalException;

}
