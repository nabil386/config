package curam.ca.gc.bdm.correspondenceframework.impl;

import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;

public class BDMCorrespondenceValidationSampleHandler
  implements BDMCorrespondenceValidationInterface {

  /**
   * This method is to perform custom validations on the data.
   */
  @Override
  public InformationalManager validateData(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final Object pojoObject,
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput,
    final InformationalManager informationalManager)
    throws AppException, InformationalException {

    // Perform mandatory checks
    final BDMCorrespondenceMasterData correspondenceMasterData =
      (BDMCorrespondenceMasterData) pojoObject;

    // Example: check if it received date is zero
    if (correspondenceMasterData.dateOfBirth.isZero()) {
      final LocalisableString localisableString =
        new LocalisableString(BDMBPOCCT.ERR_PERSON_DOB_IS_MISSING);
      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    }

    return informationalManager;
  }

  /**
   * This method is to perform custom validations in general without retrieving
   * the data.
   */
  @Override
  public InformationalManager validateTemplateContext(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput,
    final InformationalManager informationalManager)
    throws AppException, InformationalException {

    // Perform general validations
    // Example Get the data based on
    // correspondenceRecipientClientInput.clientConcernRoleID

    final boolean addressPresentInd = false; // isAddressPresentForClient(correspondenceRecipientClientInput.clientConcernRoleID)
    if (!addressPresentInd) {

      final LocalisableString localisableString =
        new LocalisableString(BDMBPOCCT.ERR_ADDRESS_IS_MISSING);
      informationalManager.addInformationalMsg(localisableString, "",
        InformationalElement.InformationalType.kError);
    }

    return informationalManager;
  }
}
