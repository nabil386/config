package curam.ca.gc.bdm.facade.bdmcommonintake.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMSelectInternalIntakeApplication;
import curam.ca.gc.bdm.lifeevent.impl.BDMBankEvidence;
import curam.ca.gc.bdm.message.impl.BDMAPPLICATIONFORMExceptionCreator;
import curam.commonintake.facade.impl.ApplicationForm;
import curam.commonintake.facade.struct.ApplicationFormDetailsList;
import curam.commonintake.message.impl.APPLICATIONFORMExceptionCreator;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.ValidationHelper;
import curam.workspaceservices.intake.struct.IntakeApplicationTypeKey;

public class BDMApplicationForm
  implements curam.ca.gc.bdm.facade.bdmcommonintake.intf.BDMApplicationForm {

  @Inject
  BDMBankEvidence bdmBankEvidence;

  @Inject
  private ApplicationForm applicationForm;

  @Inject
  private BDMMyApplicationsHelperImpl myApplicationsHelper;

  public BDMApplicationForm() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public ApplicationFormDetailsList listApplicationFormsForConcernRole(
    final ConcernRoleKey key) throws AppException, InformationalException {

    final ApplicationFormDetailsList applicationFormDetailsList =
      applicationForm.listApplicationFormsForConcernRole(key);

    applicationFormDetailsList.displayNewLinkInd =
      myApplicationsHelper.allowApplicationFormCreationForConcernRoleInd(key);

    return applicationFormDetailsList;
  }

  @Override
  public IntakeApplicationTypeKey selectInternalIntakeApplication(
    final BDMSelectInternalIntakeApplication key)
    throws AppException, InformationalException {

    // If an in progress application form exists, disallow creation of another
    if (key.concernRoleID != 0L) {
      if (!myApplicationsHelper
        .allowApplicationFormCreationForConcernRoleInd(key)) {
        ValidationHelper.addValidationError(BDMAPPLICATIONFORMExceptionCreator
          .ERR_FV_INTAKE_APPLICATION_FORM_ALREADY_IN_PROGRESS());
      }
    }
    if (0L == key.intakeApplicationTypeID) {
      ValidationHelper.addValidationError(APPLICATIONFORMExceptionCreator
        .ERR_FV_INTAKE_APPLICATION_MUST_BE_SELECTED());
    }
    ValidationHelper.failIfErrorsExist();
    final IntakeApplicationTypeKey intakeApplicationTypeKey =
      new IntakeApplicationTypeKey();
    intakeApplicationTypeKey.intakeApplicationTypeID =
      key.intakeApplicationTypeID;
    return intakeApplicationTypeKey;
  }

}
