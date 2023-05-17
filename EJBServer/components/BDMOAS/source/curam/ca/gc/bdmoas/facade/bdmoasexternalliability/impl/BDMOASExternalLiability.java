package curam.ca.gc.bdmoas.facade.bdmoasexternalliability.impl;

import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardIDKey;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityBenefitDetailsList;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASExternalLbyWizardSaveStep1Details;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASExternalLbyWizardSaveStep2Details;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASExternalLiabilityDetailsList;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASExternalLiabilityHistoryDetailsList;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASModifyLiabilityDetails;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASOverPaymentCaseDetailsList;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASOverPaymentHistoryDetailsList;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASReadLbyDetailsForEdit;
import curam.ca.gc.bdmoas.sl.bdmoasmaintainexternalliability.fact.BDMOASMaintainExternalLiabilityFactory;
import curam.ca.gc.bdmoas.sl.bdmoasmaintainexternalliability.intf.BDMOASMaintainExternalLiability;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.VersionNumberDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.wizardpersistence.impl.WizardPersistentState;

/**
 * This is a facade layer which will be used to maintain OAS External
 * Liabilities.
 *
 * @author pranav.agarwal
 */
public class BDMOASExternalLiability extends
  curam.ca.gc.bdmoas.facade.bdmoasexternalliability.base.BDMOASExternalLiability {

  final private String kSave = "SAVE";

  /**
   * This method will be used to create Outstanding Balance record along with
   * the deductions for Penalty liabilities.
   * 1. Penalty - OAS - Deduction will be created for this liability type.
   * 2. Penalty - OAS Non-Beneficiary - No deduction will be created for this
   * liability type.
   * Also if over payment case is selected then penalty record will be created
   * in DB as well to link external liability with over payment case ID.
   *
   * @param wizardStateID - This holds the wizard state ID.
   * @param step2Details - This holds the details to create outstanding balance
   * record and deduction.
   *
   * @return the external liability key
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMExternalLiabilityKey createLbySaveStep2Data(
    final WizardStateID wizardStateID,
    final BDMOASExternalLbyWizardSaveStep2Details step2Details)
    throws AppException, InformationalException {

    // instance of return object
    BDMExternalLiabilityKey key = new BDMExternalLiabilityKey();
    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();

    if (step2Details.dtls.actionIDStr.equalsIgnoreCase(kSave)) {
      // calling the service layer method
      key = bdmoasMaintainExternalLiability
        .createLbySaveStep2Data(wizardStateID, step2Details);
    } else {
      BDMOASExternalLbyWizardSaveStep1Details details =
        new BDMOASExternalLbyWizardSaveStep1Details();
      final curam.core.facade.struct.WizardStateID wizKey =
        new curam.core.facade.struct.WizardStateID();
      wizKey.wizardStateID = wizardStateID.wizardStateID;
      details = readExtLbyWizardDetails(wizKey);
      details.step2Dtls.assign(step2Details);
      final WizardPersistentState wizardPersistentState =
        new WizardPersistentState();
      wizardPersistentState.modify(wizardStateID.wizardStateID, details);
    }
    return key;
  }

  /**
   * This method will be used get the list of over payment cases by external
   * liability ID.
   *
   * @param key - This holds the liability external ID.
   *
   * @return the over payment cases list.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMOASOverPaymentCaseDetailsList
    listOverPaymentCasesByExternalLiabilityID(
      final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();

    // calling the service layer method
    return bdmoasMaintainExternalLiability
      .listOverPaymentCasesByExternalLiabilityID(key);
  }

  /**
   * This method is used to modify the external liability details.
   *
   * @param modifyDtls - This holds the details which needs to be modified.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void
    modifyLiabilityDetails(final BDMOASModifyLiabilityDetails modifyDtls)
      throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();

    // calling the service layer method
    bdmoasMaintainExternalLiability.modifyLiabilityDetails(modifyDtls);
  }

  /**
   * This method is used to display over payment cases by Wizard State ID
   *
   * @param wizardStateID - This holds the wizard state ID.
   *
   * @return the list of over payment cases.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMOASOverPaymentCaseDetailsList
    listOverPaymentCasesByWizardStateID(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();

    // calling the service layer method
    return bdmoasMaintainExternalLiability
      .listOverPaymentCasesByWizardStateID(wizardStateID);
  }

  /**
   * This method is used to display over payment history details by external
   * liability ID.
   *
   * @param key - This holds the external liability ID
   *
   * @return the over payment history details for liability
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMOASOverPaymentHistoryDetailsList
    listOverPaymentHistoryByExternalLiabilityID(
      final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();

    // calling the service layer method
    return bdmoasMaintainExternalLiability
      .listOverPaymentHistoryByExternalLiabilityID(key);
  }

  /**
   * This method is used to list liabilities for a recipient.
   */
  @Override
  public BDMOASExternalLiabilityDetailsList listLiabilities(
    final ConcernRoleKey key) throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();

    // calling the service layer method
    return bdmoasMaintainExternalLiability.listLiabilities(key);
  }

  /**
   * This method is used to persist the wizard step after first step.
   */
  @Override
  public BDMExternalLbyWizardIDKey
    createLbySaveStep1Data(final BDMOASExternalLbyWizardSaveStep1Details key)
      throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();

    // calling the service layer method
    return bdmoasMaintainExternalLiability.createLbySaveStep1Data(key);
  }

  /**
   * This method is used to read external liability wizard state details by
   * wizard state ID.
   */
  @Override
  public BDMOASExternalLbyWizardSaveStep1Details readExtLbyWizardDetails(
    final curam.core.facade.struct.WizardStateID wizardStateID)
    throws AppException, InformationalException {

    // persist the wizard state
    BDMOASExternalLbyWizardSaveStep1Details details =
      new BDMOASExternalLbyWizardSaveStep1Details();
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    if (wizardStateID.wizardStateID != 0) {
      details =
        (BDMOASExternalLbyWizardSaveStep1Details) wizardPersistentState
          .read(wizardStateID.wizardStateID);
    }
    return details;
  }

  /**
   * This method is used to read liability details for edit.
   */
  @Override
  public BDMOASReadLbyDetailsForEdit
    readLbyDetailsForEdit(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();

    // calling the service layer method
    return bdmoasMaintainExternalLiability.readLbyDetailsForEdit(key);
  }

  /**
   * This method is used to list liability history record by external liability
   * ID.
   */
  @Override
  public BDMOASExternalLiabilityHistoryDetailsList
    listLiabilityHistory(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();

    // calling the service layer method
    return bdmoasMaintainExternalLiability.listLiabilityHistory(key);
  }

  /**
   * This method is used to list benefit cases for a recipient.
   */
  @Override
  public BDMExternalLiabilityBenefitDetailsList
    listBenefitCases(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();
    // calling the service layer method
    return bdmoasMaintainExternalLiability.listBenefitCases(wizardStateID);
  }

  /**
   * This method is used to deactivate liability.
   */
  @Override
  public void deactivateLiability(final BDMExternalLiabilityKey key,
    final VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();
    // calling the service layer method
    bdmoasMaintainExternalLiability.deactivateLiability(key,
      versionNumberDetails);
  }

  /**
   * This method is used to delete liability.
   */
  @Override
  public void deleteLiability(final BDMExternalLiabilityKey key,
    final VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();
    // calling the service layer method
    bdmoasMaintainExternalLiability.deleteLiability(key,
      versionNumberDetails);
  }

  /**
   * This method is used to activate liability.
   */
  @Override
  public void activateLiability(final BDMExternalLiabilityKey key,
    final VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException {

    // instance of service layer
    final BDMOASMaintainExternalLiability bdmoasMaintainExternalLiability =
      BDMOASMaintainExternalLiabilityFactory.newInstance();
    // calling the service layer method
    bdmoasMaintainExternalLiability.activateLiability(key,
      versionNumberDetails);
  }

}
