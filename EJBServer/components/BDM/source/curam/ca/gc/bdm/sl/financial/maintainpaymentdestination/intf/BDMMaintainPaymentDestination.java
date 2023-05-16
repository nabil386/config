package curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf;

import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMAddEFTDestinationDetails;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMBankAccountForSelectionList;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMPaymentDestinationIDVersionNo;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMReadEFTDestinationDetails;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMUpdateEFTDestinationDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMSetDestinationByLifeEventKey;
import curam.ca.gc.bdm.sl.struct.BDMSubmitApplicationAddDDLinkDetails;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.VersionNumberDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface BDMMaintainPaymentDestination {

  public BDMSearchEFTDestinationDetailsList listEFTDestinations(
    ConcernRoleKey key) throws AppException, InformationalException;

  public BDMBankAccountForSelectionList listBankAccountForSelection(
    ConcernRoleKey key) throws AppException, InformationalException;

  public BDMPaymentDestinationKey
    addEFTDestination(BDMAddEFTDestinationDetails details)
      throws AppException, InformationalException;

  public BDMReadEFTDestinationDetails readEFTDestinationDetails(
    BDMPaymentDestinationKey key) throws AppException, InformationalException;

  public void modifyEFTDestination(BDMUpdateEFTDestinationDetails details)
    throws AppException, InformationalException;

  public VersionNumberDetails
    cancelEFTDestination(BDMPaymentDestinationIDVersionNo key)
      throws AppException, InformationalException;

  public void syncPaymentDestination(final ConcernRoleKey concernRoleKey)
    throws AppException, InformationalException;

  public BDMPaymentDestinationKey addDestinationOnSubmitApplication(
    BDMSubmitApplicationAddDDLinkDetails details)
    throws AppException, InformationalException;

  public void
    updateCaseParticipantDestinationsOnly(CaseParticipantRoleKey cprKey)
      throws AppException, InformationalException;

  public void syncDestinationsOnAddressChange(ConcernRoleAddressKey key)
    throws AppException, InformationalException;

  public void deleteOnAddressDeletion(final ConcernRoleAddressKey key)
    throws AppException, InformationalException;

  public void setDestinationByLifeEvent(BDMSetDestinationByLifeEventKey key)
    throws AppException, InformationalException;

}
