package curam.ca.gc.bdm.bdmpaymentdestination.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMAddEFTDestinationDetails;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMBankAccountForSelectionList;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMPaymentDestinationIDVersionNo;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMReadEFTDestinationDetails;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMUpdateEFTDestinationDetails;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationDetailsList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMPAYMENTDESTINATION;
import curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf.BDMMaintainPaymentDestination;
import curam.core.facade.struct.CodetableCodeAndDescription;
import curam.core.facade.struct.CodetableCodeAndDescriptionList;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.VersionNumberDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import java.util.LinkedHashMap;

public class BDMPaymentDestination
  extends curam.ca.gc.bdm.bdmpaymentdestination.base.BDMPaymentDestination {

  @Inject
  BDMMaintainPaymentDestination maintainPaymentDestinationObj;

  public BDMPaymentDestination() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Lists all the EFT destinations for a concern role
   */
  @Override
  public BDMSearchEFTDestinationDetailsList listEFTDestinations(
    final ConcernRoleKey key) throws AppException, InformationalException {

    return maintainPaymentDestinationObj.listEFTDestinations(key);
  }

  /**
   * Lists non-end dated bank accounts for a concern role
   */
  @Override
  public BDMBankAccountForSelectionList listBankAccountForSelection(
    final ConcernRoleKey key) throws AppException, InformationalException {

    return maintainPaymentDestinationObj.listBankAccountForSelection(key);

  }

  /**
   * Lists all the benefit program types, along with the option to select "all"
   */
  @Override
  public CodetableCodeAndDescriptionList listProgramType()
    throws AppException, InformationalException {

    final CodetableCodeAndDescriptionList allProgramTypes =
      new CodetableCodeAndDescriptionList();

    // create select all option
    final CodetableCodeAndDescription selectAll =
      new CodetableCodeAndDescription();
    selectAll.code = BDMConstants.kSelectAllCode;
    selectAll.description =
      new LocalisableString(BDMPAYMENTDESTINATION.SELECT_ALL).getMessage();
    allProgramTypes.codetableCodeAndDescription.add(selectAll);

    // get all program types
    final LinkedHashMap<String, String> programTypes =
      CodeTable.getAllEnabledItems(BDMBENEFITPROGRAMTYPE.TABLENAME,
        TransactionInfo.getProgramLocale());

    for (final String programTypeCode : programTypes.keySet()) {
      final CodetableCodeAndDescription programType =
        new CodetableCodeAndDescription();
      programType.code = programTypeCode;
      programType.description = programTypes.get(programTypeCode);
      allProgramTypes.codetableCodeAndDescription.add(programType);

    }
    return allProgramTypes;
  }

  /**
   * Adds an EFT destination for a participant
   */
  @Override
  public BDMPaymentDestinationKey
    addEFTDestination(final BDMAddEFTDestinationDetails details)
      throws AppException, InformationalException {

    return maintainPaymentDestinationObj.addEFTDestination(details);
  }

  /**
   * Reads the existing EFT destination details for a given record
   */
  @Override
  public BDMReadEFTDestinationDetails
    readEFTDestinationDetails(final BDMPaymentDestinationKey key)
      throws AppException, InformationalException {

    return maintainPaymentDestinationObj.readEFTDestinationDetails(key);
  }

  /**
   * Modifies an EFT destination
   */
  @Override
  public void
    modifyEFTDestination(final BDMUpdateEFTDestinationDetails details)
      throws AppException, InformationalException {

    maintainPaymentDestinationObj.modifyEFTDestination(details);

  }

  /**
   * Cancels an EFT destination
   */
  @Override
  public VersionNumberDetails
    cancelEFTDestination(final BDMPaymentDestinationIDVersionNo key)
      throws AppException, InformationalException {

    return maintainPaymentDestinationObj.cancelEFTDestination(key);
  }

}
