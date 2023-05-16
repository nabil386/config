package curam.ca.gc.bdm.facade.bdmexternalliability.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardIDKey;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardSaveStep1Details;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardSaveStep2Details;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityBenefitDetailsList;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityDetailsList;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityHistoryDetailsList;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMModifyLiabilityDetails;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMReadLbyDetailsForEdit;
import curam.ca.gc.bdm.sl.maintainexternalliability.impl.MaintainExternalLiability;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.VersionNumberDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.wizardpersistence.impl.WizardPersistentState;

public class BDMExternalLiability extends
  curam.ca.gc.bdm.facade.bdmexternalliability.base.BDMExternalLiability {

  @Inject
  MaintainExternalLiability maintainExternalLiabilityObj;

  final private String kSave = "SAVE";

  final private String kBack = "BACK";

  public BDMExternalLiability() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public BDMExternalLbyWizardIDKey
    createLbySaveStep1Data(final BDMExternalLbyWizardSaveStep1Details key)
      throws AppException, InformationalException {

    return maintainExternalLiabilityObj.createLbySaveStep1Data(key);
  }

  @Override
  public BDMExternalLiabilityBenefitDetailsList
    listBenefitCases(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    return maintainExternalLiabilityObj.listBenefitCases(wizardStateID);
  }

  @Override
  public BDMExternalLiabilityKey createLbySaveStep2Data(
    final WizardStateID wizardStateID,
    final BDMExternalLbyWizardSaveStep2Details step2Details)
    throws AppException, InformationalException {

    BDMExternalLiabilityKey key = new BDMExternalLiabilityKey();
    if (step2Details.actionIDStr.equalsIgnoreCase(kSave)) {
      key = maintainExternalLiabilityObj.createLbySaveStep2Data(wizardStateID,
        step2Details);
    } else {
      BDMExternalLbyWizardSaveStep1Details details =
        new BDMExternalLbyWizardSaveStep1Details();
      final curam.core.facade.struct.WizardStateID wizKey =
        new curam.core.facade.struct.WizardStateID();
      wizKey.wizardStateID = wizardStateID.wizardStateID;
      details = readExtLbyWizardDetails(wizKey);
      details.dtls.assign(step2Details);
      final WizardPersistentState wizardPersistentState =
        new WizardPersistentState();
      wizardPersistentState.modify(wizardStateID.wizardStateID, details);
    }
    return key;
  }

  @Override
  public void deactivateLiability(final BDMExternalLiabilityKey key,
    final VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException {

    maintainExternalLiabilityObj.deactivateLiability(key,
      versionNumberDetails);

  }

  @Override
  public void deleteLiability(final BDMExternalLiabilityKey key,
    final VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException {

    maintainExternalLiabilityObj.deleteLiability(key, versionNumberDetails);

  }

  /**
   * Lists all the external liabilities a participant has
   */
  @Override
  public BDMExternalLiabilityDetailsList listLiabilities(
    final ConcernRoleKey key) throws AppException, InformationalException {

    return maintainExternalLiabilityObj.listLiabilities(key);
  }

  @Override
  public VersionNumberDetails
    readLiabilityVersion(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    return maintainExternalLiabilityObj.readLiabilityVersion(key);
  }

  @Override
  public void activateLiability(final BDMExternalLiabilityKey key,
    final VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException {

    maintainExternalLiabilityObj.activateLiability(key, versionNumberDetails);

  }

  @Override
  public BDMReadLbyDetailsForEdit
    readLbyDetailsForEdit(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    return maintainExternalLiabilityObj.readLbyDetailsForEdit(key);
  }

  @Override
  public void
    modifyLiabilityDetails(final BDMModifyLiabilityDetails modifyDtls)
      throws AppException, InformationalException {

    maintainExternalLiabilityObj.modifyLiabilityDetails(modifyDtls, true);

  }

  @Override
  public BDMExternalLiabilityHistoryDetailsList
    listLiabilityHistory(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    return maintainExternalLiabilityObj.listLiabilityHistory(key);
  }

  @Override
  public BDMExternalLiabilityBenefitDetailsList
    listBenefitCasesForModify(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    return maintainExternalLiabilityObj.listBenefitCasesForModify(key);

  }

  @Override
  public BDMExternalLbyWizardSaveStep1Details
    readExtLbyWizardDetails(final curam.core.facade.struct.WizardStateID key)
      throws AppException, InformationalException {

    BDMExternalLbyWizardSaveStep1Details details =
      new BDMExternalLbyWizardSaveStep1Details();
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    if (key.wizardStateID != 0) {
      details = (BDMExternalLbyWizardSaveStep1Details) wizardPersistentState
        .read(key.wizardStateID);
    }
    return details;
  }

  @Override
  public curam.core.facade.struct.WizardStateID
    createExtLbyWizardDetailsStep2(
      final BDMExternalLbyWizardSaveStep1Details details)
      throws AppException, InformationalException {

    final curam.core.facade.struct.WizardStateID wizardStateID =
      new curam.core.facade.struct.WizardStateID();
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    wizardStateID.wizardStateID = wizardPersistentState.create(details);

    return wizardStateID;
  }

}
