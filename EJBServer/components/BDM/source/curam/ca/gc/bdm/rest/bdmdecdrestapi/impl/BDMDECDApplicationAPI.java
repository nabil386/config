package curam.ca.gc.bdm.rest.bdmdecdrestapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.fact.BDMUsernameGuidLinkFactory;
import curam.ca.gc.bdm.entity.struct.BDMGuidKey;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkDetails;
import curam.ca.gc.bdm.entity.struct.BDMUsernameGuidLinkDtls;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.rest.bdmdecdrestapi.struct.BDMDECDGuidKey;
import curam.citizenaccount.facade.struct.ExistingApplicationDetailsList;
import curam.citizenaccount.facade.struct.SubmittedApplicationDetails;
import curam.citizenworkspace.rest.facade.struct.UAApplicationProgramStatus;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplication;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplicationList;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplicationProgram;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountInfo;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.facade.intf.UniqueID;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.CodeTable;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationStatusEntry;
import curam.workspaceservices.intake.fact.IntakeApplicationFactory;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeApplicationDAO;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import curam.workspaceservices.intake.impl.IntakeProgramApplicationDAO;
import curam.workspaceservices.intake.impl.ProgramTypeDAO;
import curam.workspaceservices.intake.struct.IntakeApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeApplicationKey;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import curam.workspaceservices.util.impl.ResourceStoreHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * REST API to retrieve the Submitted application when accessed from Digital
 * Channel (DECD)
 *
 */
public class BDMDECDApplicationAPI
  extends curam.ca.gc.bdm.rest.bdmdecdrestapi.base.BDMDECDApplicationAPI {

  @Inject
  ProgramTypeDAO programTypeDAO;

  @Inject
  private IntakeProgramApplicationDAO intakeProgramApplicationDAO;

  @Inject
  private IntakeApplicationDAO intakeApplicationDAO;

  @Inject
  private ResourceStoreHelper resourceStoreHelper;

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  public BDMDECDApplicationAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * get the submitted Applications from UA
   *
   * @return UASubmittedApplicationList
   */
  @Override
  public UASubmittedApplicationList
    listSubmittedApplications(final BDMDECDGuidKey guidKey)
      throws AppException, InformationalException {

    Trace.kTopLevelLogger
      .info(" on_Behalf_Of  : Guid = " + guidKey.on_Behalf_Of);

    if (StringUtil.isNullOrEmpty(guidKey.on_Behalf_Of)) {
      Trace.kTopLevelLogger.info(" on_Behalf_Of Key must be populated");

      final AppException exceptionErr = new AppException(
        BDMRESTAPIERRORMESSAGE.BDM_ERR_DECD_GUID_NOT_POPULATED_EXCEPTION);
      throw exceptionErr;
    }

    // get the submitted application for the user
    final UASubmittedApplicationList uaSubmittedApplications =
      new UASubmittedApplicationList();
    final ExistingApplicationDetailsList existingApplications =
      listExistingApplications(getUAUserNameByGuid(guidKey).username);
    final ArrayList<SubmittedApplicationDetails> submittedApps =
      existingApplications.submittedAppDtls;

    for (final SubmittedApplicationDetails submittedApplicationDetails : submittedApps) {
      final UASubmittedApplication uaSubmittedApplication =
        new UASubmittedApplication();
      uaSubmittedApplication.application_id =
        submittedApplicationDetails.intakeApplicationID;
      uaSubmittedApplication.name =
        submittedApplicationDetails.applicationName;
      uaSubmittedApplication.submittedOn =
        submittedApplicationDetails.submittedDateTime;

      final IntakeApplicationKey intakeApplicationKey =
        new IntakeApplicationKey();
      intakeApplicationKey.intakeApplicationID =
        submittedApplicationDetails.intakeApplicationID;
      final IntakeApplicationDtls intakeApplicationDetails =
        IntakeApplicationFactory.newInstance().read(intakeApplicationKey);
      uaSubmittedApplication.status = intakeApplicationDetails.status;

      final IntakeProgramApplicationKey intakeProgramApplicationKey =
        new IntakeProgramApplicationKey();
      intakeProgramApplicationKey.intakeProgramApplicationID =
        submittedApplicationDetails.intakeProgramApplicationID;
      final IntakeProgramApplicationDtls intakeProgramApplicationDetails =
        IntakeProgramApplicationFactory.newInstance()
          .read(intakeProgramApplicationKey);
      final UASubmittedApplicationProgram applicationProgram =
        new UASubmittedApplicationProgram();
      applicationProgram.application_program_id =
        submittedApplicationDetails.intakeProgramApplicationID;
      applicationProgram.name = submittedApplicationDetails.programName;

      final IntakeProgramApplication intakeProgramApplication =
        this.intakeProgramApplicationDAO
          .get(submittedApplicationDetails.intakeProgramApplicationID);

      final List<Long> caseIDs =
        intakeProgramApplication.listAssociatedCases();
      final UAApplicationProgramStatus applicationProgramStatus =
        new UAApplicationProgramStatus();
      if (caseIDs.size() == 1) {
        applicationProgram.case_id = caseIDs.get(0);
      }
      if (submittedApplicationDetails.withdrawInd && caseIDs.size() == 0) {
        applicationProgramStatus.canBeWithdrawn = false;
      } else {
        applicationProgramStatus.canBeWithdrawn =
          submittedApplicationDetails.withdrawInd;
      }
      applicationProgramStatus.hasPendingWithdrawalRequest =
        intakeProgramApplication.getOpenWithdrawalRequest() != null;
      applicationProgramStatus.withdrawalRequestStatusMessage =
        applicationProgramStatus.hasPendingWithdrawalRequest
          ? submittedApplicationDetails.programStatus : CuramConst.gkEmpty;
      applicationProgramStatus.status =
        intakeProgramApplicationDetails.status;
      applicationProgram.programStatusDetails = applicationProgramStatus;
      uaSubmittedApplication.applicationPrograms.add(applicationProgram);
      uaSubmittedApplications.data.add(uaSubmittedApplication);
    }
    return uaSubmittedApplications;
  }

  /**
   * BDMUsernameGuidLink maps guid with username of UA , for Foundation Scope.
   * For a given guid, check whether a sample userName exists, If not insert
   * sample username
   *
   * @param guidKey
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMUsernameGuidLinkDetails
    getUAUserNameByGuid(final BDMDECDGuidKey guidKey)
      throws AppException, InformationalException {

    // get UA username from guid

    final curam.ca.gc.bdm.entity.intf.BDMUsernameGuidLink usernameGuidLinkObj =
      BDMUsernameGuidLinkFactory.newInstance();
    final NotFoundIndicator nf = new NotFoundIndicator();
    final BDMGuidKey guidKeyObj = new BDMGuidKey();
    guidKeyObj.guid = guidKey.on_Behalf_Of;

    BDMUsernameGuidLinkDetails guidUserNameDetail =
      new BDMUsernameGuidLinkDetails();
    guidUserNameDetail =
      usernameGuidLinkObj.readUserNameByGuid(nf, guidKeyObj);

    // If not found, insert new mapping (username <-> guid) into the table
    if (nf.isNotFound()) {
      Trace.kTopLevelLogger.info(
        " UA UserName Not Found , Insert new GUID <-> UserNAME mapping");

      final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
      final BDMUsernameGuidLinkDtls userNameGuidLinkDetailsObj =
        new BDMUsernameGuidLinkDtls();
      userNameGuidLinkDetailsObj.usernameGuidLinkID =
        uniqueIDObj.getNextID().uniqueID;
      userNameGuidLinkDetailsObj.guid = guidKeyObj.guid;
      userNameGuidLinkDetailsObj.username = Configuration
        .getProperty(EnvVars.BDM_ENV_BDM_DECD_SAMPLE_USER_NAME_FOR_GUID);
      userNameGuidLinkDetailsObj.createdOn = DateTime.getCurrentDateTime();
      usernameGuidLinkObj.insert(userNameGuidLinkDetailsObj);

      guidUserNameDetail = new BDMUsernameGuidLinkDetails();
      guidUserNameDetail.username = userNameGuidLinkDetailsObj.username;
      guidUserNameDetail.guid = guidKeyObj.guid;
    }
    return guidUserNameDetail;
  }

  /**
   *
   * @param userName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ExistingApplicationDetailsList listExistingApplications(
    final String userName) throws AppException, InformationalException {

    final ExistingApplicationDetailsList existingApplicationDetailsList =
      new ExistingApplicationDetailsList();
    final CitizenWorkspaceAccountInfo loggedInUserCWAccountInfo =
      this.citizenWorkspaceAccountManager.readAccountBy(userName);
    final List<IntakeApplication> intakeApplicationList =
      this.intakeApplicationDAO
        .listByCitizenWorkspaceAccount(loggedInUserCWAccountInfo);
    Collections.sort(intakeApplicationList,
      new Comparator<IntakeApplication>() {

        @Override
        public int compare(final IntakeApplication c1,
          final IntakeApplication c2) {

          return c1.getIntakeApplicationType().name().getValue()
            .compareTo(c2.getIntakeApplicationType().name().getValue());
        }
      });
    for (final IntakeApplication intakeApplication : intakeApplicationList) {
      existingApplicationDetailsList.submittedAppDtls
        .addAll(this.getSubmittedApplicationDetails(intakeApplication));
    }
    return existingApplicationDetailsList;

  }

  /**
   *
   * @param intakeApplication
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected List<SubmittedApplicationDetails>
    getSubmittedApplicationDetails(final IntakeApplication intakeApplication)
      throws AppException, InformationalException {

    final List<IntakeProgramApplication> intakeProgramApplicationList =
      intakeApplication.listIntakeProgramApplications();
    Collections.sort(intakeProgramApplicationList,
      new Comparator<IntakeProgramApplication>() {

        @Override
        public int compare(final IntakeProgramApplication p1,
          final IntakeProgramApplication p2) {

          return p1.getProgramType().name().getValue()
            .compareTo(p2.getProgramType().name().getValue());
        }
      });
    final List<SubmittedApplicationDetails> list =
      new ArrayList<SubmittedApplicationDetails>();
    for (final IntakeProgramApplication intakeProgramApplication : intakeProgramApplicationList) {
      final SubmittedApplicationDetails submittedApplicationDetails =
        new SubmittedApplicationDetails();
      String programStatus = CuramConst.gkEmpty;
      submittedApplicationDetails.applicationName =
        intakeApplication.getIntakeApplicationType().name().getValue();
      submittedApplicationDetails.applicationUrl =
        intakeApplication.getIntakeApplicationType().getUrl();
      submittedApplicationDetails.reference =
        intakeApplication.getReference();
      submittedApplicationDetails.intakeApplicationID =
        intakeProgramApplication.getIntakeApplication().getID();
      submittedApplicationDetails.programName =
        intakeProgramApplication.getProgramType().name().getValue();
      submittedApplicationDetails.programUrl =
        intakeProgramApplication.getProgramType().getUrl();
      submittedApplicationDetails.intakeProgramApplicationID =
        intakeProgramApplication.getID();
      submittedApplicationDetails.submittedDateTime =
        intakeProgramApplication.getSubmittedDateTime();
      final boolean isWithdrawn = this.isWithdrawn(intakeProgramApplication);
      if (this.isApproved(intakeProgramApplication)) {
        submittedApplicationDetails.withdrawInd = false;
        programStatus = CodeTable.getOneItemForUserLocale(
          IntakeProgramApplicationStatusEntry.TABLENAME,
          intakeProgramApplication.getLifecycleState().getCode());
      } else if (!this
        .isPendingAndHasOpenWithdrawalRequest(intakeProgramApplication)
        && !isWithdrawn) {
        submittedApplicationDetails.withdrawInd = true;
        programStatus = CodeTable.getOneItemForUserLocale(
          IntakeProgramApplicationStatusEntry.TABLENAME,
          intakeProgramApplication.getLifecycleState().getCode());
      } else {
        submittedApplicationDetails.withdrawInd = false;
        if (isWithdrawn) {
          programStatus = this.resourceStoreHelper.getPropertyValue(
            BDMDECDConst.intakeApplications, BDMDECDConst.withdrawn_Label);
        } else {
          programStatus = this.resourceStoreHelper.getPropertyValue(
            BDMDECDConst.intakeApplications,
            BDMDECDConst.withdrawal_pending_label);
        }
      }
      submittedApplicationDetails.programStatus = programStatus;
      submittedApplicationDetails.displayOtherActionInd = false;
      submittedApplicationDetails.otherActionURI =
        BDMDECDConst.submittedApplicationDetails;
      list.add(submittedApplicationDetails);
    }
    return list;
  }

  /**
   *
   * @param intakeProgramApplication
   * @return
   */
  private boolean isPendingAndHasOpenWithdrawalRequest(
    final IntakeProgramApplication intakeProgramApplication) {

    return intakeProgramApplication.getOpenWithdrawalRequest() != null
      && IntakeProgramApplicationStatusEntry.PENDING
        .equals(intakeProgramApplication.getLifecycleState());
  }

  /**
   *
   * @param intakeProgramApplication
   * @return
   */
  private boolean
    isWithdrawn(final IntakeProgramApplication intakeProgramApplication) {

    return IntakeProgramApplicationStatusEntry.WITHDRAWN
      .equals(intakeProgramApplication.getLifecycleState());
  }

  /**
   *
   * @param intakeProgramApplication
   * @return
   */
  private boolean
    isApproved(final IntakeProgramApplication intakeProgramApplication) {

    return IntakeProgramApplicationStatusEntry.APPROVED
      .equals(intakeProgramApplication.getLifecycleState());
  }

}
