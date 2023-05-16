package curam.ca.gc.bdm.rest.impl;

import com.google.inject.Inject;
import curam.appeal.facade.struct.AppealCaseKey;
import curam.appeal.facade.struct.DecisionAndAttachmentListDetailsForIC;
import curam.appeal.sl.entity.fact.AppealOnlineRequestLinkFactory;
import curam.appeal.sl.entity.intf.AppealOnlineRequestLink;
import curam.appeal.sl.entity.struct.AppealOnlineRequestIDKey;
import curam.appeal.sl.entity.struct.AppealOnlineRequestLinkDetails;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.entity.fact.BDMOnlineAppealRequestFactory;
import curam.ca.gc.bdm.entity.intf.BDMOnlineAppealRequest;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestDtls;
import curam.ca.gc.bdm.entity.struct.BDMOnlineAppealRequestKey;
import curam.ca.gc.bdm.facade.appeals.impl.BDMAppeal;
import curam.ca.gc.bdm.rest.struct.BDMApplicationList;
import curam.ca.gc.bdm.rest.struct.BDMRFRApplicationIDKey;
import curam.ca.gc.bdm.rest.struct.BDMRFRDraftRequest;
import curam.ca.gc.bdm.rest.struct.BDMRFROnlineRequest;
import curam.ca.gc.bdm.rest.struct.BDMRFROnlineRequestList;
import curam.ca.gc.bdm.sl.appeals.impl.BDMAppealHelper;
import curam.citizenworkspace.codetable.impl.CitizenScriptStatusEntry;
import curam.citizenworkspace.codetable.impl.IntakeClientTypeEntry;
import curam.citizenworkspace.facade.struct.SetupAppealResult;
import curam.citizenworkspace.message.impl.CITIZENWORKSPACEExceptionCreator;
import curam.citizenworkspace.message.impl.PUBLICUSERSECURITYExceptionCreator;
import curam.citizenworkspace.rest.facade.fact.ApplicationAPIFactory;
import curam.citizenworkspace.rest.facade.intf.ApplicationAPI;
import curam.citizenworkspace.rest.facade.struct.UAAppealFormKey;
import curam.citizenworkspace.rest.facade.struct.UASubmittedApplicationList;
import curam.citizenworkspace.rest.message.impl.RESTAPIERRORMESSAGESExceptionCreator;
import curam.citizenworkspace.scriptinfo.impl.CitizenScriptInfo;
import curam.citizenworkspace.scriptinfo.impl.CitizenScriptInfoDAO;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccount;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountDAO;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.citizenworkspace.util.internal.impl.LogHelper;
import curam.citizenworkspace.util.internal.impl.LogHelper.CitizenWorkspaceLoggable;
import curam.codetable.APPEALCASERESOLUTION;
import curam.codetable.CZNAPPEALREQUESTSTATUS;
import curam.codetable.HEARINGDECISIONRESOLUTION;
import curam.codetable.ONLINEAPPEALREQUESTSTATUS;
import curam.codetable.impl.CASECATTYPECODEEntry;
import curam.commonintake.codetable.APPLICATIONCASESTATUS;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.impl.MyApplicationsHelper;
import curam.commonintake.facade.struct.ApplicationCaseSearchCriteria;
import curam.commonintake.facade.struct.MyApplicationsList;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.onlineappealrequest.impl.OnlineAppealRequest;
import curam.core.onlineappealrequest.impl.OnlineAppealRequestDAO;
import curam.core.sl.fact.OnlineAppealRequestFactory;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.OnlineAppealRequestDtls;
import curam.core.sl.struct.OnlineAppealRequestKey;
import curam.datastore.impl.NoSuchSchemaException;
import curam.datastore.message.impl.DATASTOREExceptionCreator;
import curam.participant.impl.ConcernRole;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.struct.ApplicationIDKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.workspaceservices.codetable.IntakeApplicationStatus;
import curam.workspaceservices.intake.fact.IntakeProgramAppCaseLinkFactory;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.fact.ProgramTypeFactory;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtls;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtlsList;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtls;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import curam.workspaceservices.intake.struct.ProgramTypeDtls;
import curam.workspaceservices.intake.struct.ProgramTypeKey;
import curam.workspaceservices.localization.fact.TextTranslationFactory;
import curam.workspaceservices.localization.intf.TextTranslation;
import curam.workspaceservices.localization.struct.ReadByLocalizableTextIDandLocaleKey;
import curam.workspaceservices.localization.struct.TextTranslationDtls;
import java.util.LinkedList;
import java.util.List;

/**
 * The request for reconsideration REST API.
 *
 */
public class BDMUARequestForReconsiderationAPI
  extends curam.ca.gc.bdm.rest.base.BDMUARequestForReconsiderationAPI {

  @Inject
  private BDMAppealHelper bdmAppealHelper;

  @Inject
  private CitizenWorkspaceAccountDAO citizenWorkspaceAccountDAO;

  @Inject
  private CitizenScriptInfoDAO citizenScriptInfoDAO;

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private MyApplicationsHelper myApplicationsHelper;

  @Inject
  private OnlineAppealRequestDAO onlineAppealRequestDAO;

  @Inject
  private PersonDAO personDAO;

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  BDMAppeal bdmAppeal;

  public BDMUARequestForReconsiderationAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Returns the list of requests for reconsideration.
   */
  @Override
  public BDMRFROnlineRequestList listOnlineRequests()
    throws AppException, InformationalException {

    final BDMRFROnlineRequestList bdmRFROnlineRequestList =
      new BDMRFROnlineRequestList();
    this.performDefaultSecurityChecks();
    final ConcernRole concernRole =
      this.bdmAppealHelper.getConcernRoleForUser();
    final Person person = this.personDAO.get(concernRole.getID());
    final List<OnlineAppealRequest> onlineAppealRequestList =
      this.onlineAppealRequestDAO.searchByCreatedBy(person);

    final String cancelledCitizenScriptStatus =
      CitizenScriptStatusEntry.CANCELLED.getCode();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    for (final OnlineAppealRequest onlineAppealRequest : onlineAppealRequestList) {
      final BDMRFROnlineRequest bdmRFROnlineRequest =
        new BDMRFROnlineRequest();
      bdmRFROnlineRequest.iegExecutionID =
        onlineAppealRequest.getIEGExecutionID();
      final CitizenScriptInfo citizenScriptInfo =
        this.citizenScriptInfoDAO.get(bdmRFROnlineRequest.iegExecutionID);
      bdmRFROnlineRequest.updatedDateTime =
        citizenScriptInfo.getLastUpdatedDate();
      final String citizenScriptStatus =
        citizenScriptInfo.getLifecycleState().getCode();
      if (!citizenScriptStatus.equals(cancelledCitizenScriptStatus)) {
        bdmRFROnlineRequest.onlineAppealRequestID =
          onlineAppealRequest.getID();
        try {
          bdmRFROnlineRequest.primaryAppellantName =
            onlineAppealRequest.getPrimaryAppellant().getName();
        } catch (final Exception e) {
          bdmRFROnlineRequest.primaryAppellantName = concernRole.getName();
        }
        bdmRFROnlineRequest.appealRequestStatus = getOnlineAppealStatusCode(
          bdmRFROnlineRequest.onlineAppealRequestID);
        bdmRFROnlineRequest.submittedDateTime =
          new Date(onlineAppealRequest.getSubmittedDateTime());
        bdmRFROnlineRequest.iegExecutionID =
          onlineAppealRequest.getIEGExecutionID();
        final curam.core.sl.intf.OnlineAppealRequest requestObj =
          OnlineAppealRequestFactory.newInstance();
        final OnlineAppealRequestKey onlineAppealRequesKey =
          new OnlineAppealRequestKey();
        onlineAppealRequesKey.onlineAppealRequestID =
          bdmRFROnlineRequest.onlineAppealRequestID;
        final OnlineAppealRequestDtls onlineAppealRequestDtls =
          requestObj.read(nfIndicator, onlineAppealRequesKey);
        if (!nfIndicator.isNotFound()) {
          bdmRFROnlineRequest.startDateTime =
            new Date(onlineAppealRequestDtls.createdDateTime);
          // Get the RFR Status that shows in UA
          bdmRFROnlineRequest.onlineAppealStatus =
            onlineAppealRequestDtls.status;
          if (onlineAppealRequestDtls.status
            .equals(ONLINEAPPEALREQUESTSTATUS.STARTED)
            || onlineAppealRequestDtls.status
              .equals(ONLINEAPPEALREQUESTSTATUS.INITIATED)) {
            bdmRFROnlineRequest.appealRequestStatus =
              CZNAPPEALREQUESTSTATUS.DRAFT;
          }
        }
        final BDMOnlineAppealRequestKey bdmOnlineAppealRequestKey =
          new BDMOnlineAppealRequestKey();
        bdmOnlineAppealRequestKey.onlineAppealRequestID =
          bdmRFROnlineRequest.onlineAppealRequestID;
        final BDMOnlineAppealRequest bdmOnlineAppealRequestObj =
          BDMOnlineAppealRequestFactory.newInstance();
        final BDMOnlineAppealRequestDtls bdmOnlineAppealRequestDtls =
          bdmOnlineAppealRequestObj.read(nfIndicator,
            bdmOnlineAppealRequestKey);
        if (!nfIndicator.isNotFound()) {
          bdmRFROnlineRequest.programName = casesRelatedToApplicationCase(
            bdmOnlineAppealRequestDtls.applicationCaseID,
            bdmOnlineAppealRequestDtls.programCode);
          bdmRFROnlineRequest.applicationName =
            String.valueOf(bdmOnlineAppealRequestDtls.applicationCaseID);
          bdmRFROnlineRequest.confirmation =
            bdmOnlineAppealRequestDtls.onlineAppealRequestReference;
          bdmRFROnlineRequest.benefit =
            bdmOnlineAppealRequestDtls.programCode;
        }
        bdmRFROnlineRequestList.requests.add(bdmRFROnlineRequest);
      }
    }
    return bdmRFROnlineRequestList;
  }

  /**
   * starts the request for reconsideration ieg..
   */
  @Override
  public BDMRFRDraftRequest
    startAppealForm(final BDMRFRApplicationIDKey bdmRFRapplicationIDKey)
      throws AppException, InformationalException {

    this.performDefaultSecurityChecks();
    final BDMRFRDraftRequest draftRequest = new BDMRFRDraftRequest();

    if (bdmRFRapplicationIDKey.iegExecutionID != 0) {
      try {
        // if the user already has a draft appeal, use that appeal
        final CitizenScriptInfo scriptInfo =
          citizenScriptInfoDAO.get(bdmRFRapplicationIDKey.iegExecutionID);
        draftRequest.iegExecutionID = scriptInfo.getID();
        return draftRequest;
      } catch (final Exception e) {
      }
    }
    try {
      final ApplicationIDKey applicationIDKey = new ApplicationIDKey();
      applicationIDKey.applicationID = bdmRFRapplicationIDKey.applicationID;
      final SetupAppealResult setUpAppealsResult =
        this.bdmAppealHelper.startAppeal(applicationIDKey);
      draftRequest.iegExecutionID = setUpAppealsResult.iegExecutionID;
    } catch (final AppException e) {
      if (e.getCatEntry().getEntry().equals(CITIZENWORKSPACEExceptionCreator
        .ERR_APPEAL_SCRIPT_NOT_DEFINED().getCatEntry().getEntry()))
        throw RESTAPIERRORMESSAGESExceptionCreator
          .HTTP_404_APPEAL_SCRIPT_NOT_DEFINED();
      if (e.getCatEntry().getEntry().equals(CITIZENWORKSPACEExceptionCreator
        .ERR_APPEAL_SCHEMA_NOT_DEFINED().getCatEntry().getEntry()))
        throw RESTAPIERRORMESSAGESExceptionCreator
          .HTTP_404_APPEAL_SCHEMA_NOT_DEFINED();
    }
    return draftRequest;
  }

  /**
   * Returns the requests for reconsideration request information after an
   * appeal ieg is exited.
   */
  @Override
  public BDMRFROnlineRequest
    exitAppealForm(final UAAppealFormKey appealFormKey)
      throws AppException, InformationalException {

    this.performDefaultSecurityChecks();
    BDMRFROnlineRequest submittedRequest = new BDMRFROnlineRequest();

    try {
      submittedRequest =
        this.bdmAppealHelper.finishAppeal(appealFormKey.execution_id);
    } catch (final NoSuchSchemaException e) {
      throw RESTAPIERRORMESSAGESExceptionCreator
        .HTTP_404_APPEAL_SCHEMA_NOT_DEFINED();
    } catch (final AppException e) {
      if (e.getCatEntry().getEntry().equals(DATASTOREExceptionCreator
        .RUN_NOSUCHSCHEMA(
          Configuration.getProperty(EnvVars.ENV_CW_APPEALS_DATASTORE_SCHEMA))
        .getCatEntry().getEntry()))
        throw RESTAPIERRORMESSAGESExceptionCreator
          .HTTP_404_APPEAL_SCHEMA_NOT_FOUND();
    }
    return submittedRequest;
  }

  /**
   * Returns the list of closed applications for that can be used for requests
   * for reconsideration.
   */
  @Override
  public BDMApplicationList listClosedApplications()
    throws AppException, InformationalException {

    final BDMApplicationList bdmApplicationList = new BDMApplicationList();
    this.performDefaultSecurityChecks();
    if (this.citizenWorkspaceAccountManager
      .readAccountBy(TransactionInfo.getProgramUser())
      .isLinkedToInternalCuramSystem()) {
      final ConcernRole person = this.citizenWorkspaceAccountManager
        .readAccountBy(TransactionInfo.getProgramUser()).getConcernRole();
      final ApplicationCaseSearchCriteria searchCriteria =
        new ApplicationCaseSearchCriteria();
      searchCriteria.concernRoleID = person.getID();
      searchCriteria.statusList = APPLICATIONCASESTATUS.CLOSED;
      try {
        final MyApplicationsList myApplicationsList =
          this.myApplicationsHelper.getSearchedApplications(searchCriteria);
        for (int i = 0; i < myApplicationsList.dtls.size(); i++) {
          bdmApplicationList.dtls.add(bdmAppealHelper.getMyAppDetails(
            myApplicationsList.dtls.get(i).applicationCaseID));
          // Get the Program Name from Codetable Code
          bdmApplicationList.dtls.get(i).programName =
            BDMUtil.getCodeTableDescriptionForUserLocale(
              CASECATTYPECODEEntry.TABLENAME,
              bdmApplicationList.dtls.get(i).programName);
        }
      } catch (final Exception e) {
      }
    } else {
      final ApplicationAPI applicationAPIObj =
        ApplicationAPIFactory.newInstance();
      final UASubmittedApplicationList uaSubmittedApplicationsList =
        applicationAPIObj.listSubmittedApplications();
      try {
        for (int i = 0; i < uaSubmittedApplicationsList.data.size(); i++) {
          if (uaSubmittedApplicationsList.data.get(i).status
            .equals(IntakeApplicationStatus.DISPOSED))
            bdmApplicationList.dtls.add(bdmAppealHelper.getMyAppDetails(
              uaSubmittedApplicationsList.data.get(i).applicationPrograms
                .get(0).case_id));
          // Get the Program Name from Codetable Code
          bdmApplicationList.dtls.get(i).programName =
            BDMUtil.getCodeTableDescriptionForUserLocale(
              CASECATTYPECODEEntry.TABLENAME,
              bdmApplicationList.dtls.get(i).programName);
        }
      } catch (final Exception e) {
      }
    }
    return bdmApplicationList;
  }

  /**
   * Returns the list of closed applications for that can be used for requests
   * for reconsideration.
   */
  private Boolean performDefaultSecurityChecks()
    throws AppException, InformationalException {

    final LogHelper logHelper = new LogHelper();
    final String publicUser =
      Configuration.getProperty(EnvVars.ENV_CW_DEFAULT_USERNAME);
    final String userName = TransactionInfo.getProgramUser();
    LinkedList<String> messageArguments;
    if (userName.equals(publicUser)) {
      messageArguments = new LinkedList<String>();
      messageArguments.add(TransactionInfo.getProgramName());
      logHelper.securityViolation(
        CitizenWorkspaceLoggable.PUBLIC_CITIZEN_UNAUTHORISED_METHOD_INVOKATION,
        messageArguments);
      throw PUBLICUSERSECURITYExceptionCreator
        .ERR_CITIZEN_WORKSPACE_UNAUTHORISED_METHOD_INVOKATION();
    } else {
      final CitizenWorkspaceAccount citizen =
        citizenWorkspaceAccountDAO.readByUserName(userName);
      if (!citizen.getType().equals(IntakeClientTypeEntry.LINKED)
        && !citizen.getType().equals(IntakeClientTypeEntry.NORMAL)) {
        messageArguments = new LinkedList<String>();
        messageArguments.add(userName);
        messageArguments.add(TransactionInfo.getProgramName());
        logHelper.securityViolation(
          CitizenWorkspaceLoggable.UNLINKED_USER_UNAUTHORISED_METHOD_INVOKATION,
          messageArguments);
        throw PUBLICUSERSECURITYExceptionCreator
          .ERR_CITIZEN_WORKSPACE_UNAUTHORISED_METHOD_INVOKATION();
      }
    }
    return true;
  }

  /**
   * Get string of active benefit names related to the program using the
   * application case id
   */
  private String casesRelatedToApplicationCase(final long applicationCaseID,
    final String icProgramCode) throws AppException, InformationalException {

    final ApplicationCaseKey applicationCaseKey = new ApplicationCaseKey();
    applicationCaseKey.applicationCaseID = applicationCaseID;
    String benefitList = "";
    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = applicationCaseID;
    try {
      final IntakeProgramAppCaseLinkDtlsList intakeProgramAppCaseLinkDtlsList =
        IntakeProgramAppCaseLinkFactory.newInstance()
          .searchByCaseID(caseIDKey);
      for (int j = 0; j < intakeProgramAppCaseLinkDtlsList.dtls.size(); j++) {
        final IntakeProgramAppCaseLinkDtls intakeProgramAppCaseLinkDtls =
          intakeProgramAppCaseLinkDtlsList.dtls.item(j);
        final IntakeProgramApplicationKey intakeProgramApplicationKey =
          new IntakeProgramApplicationKey();
        intakeProgramApplicationKey.intakeProgramApplicationID =
          intakeProgramAppCaseLinkDtls.intakeProgramApplicationID;
        final IntakeProgramApplicationDtls intakeProgramApplicationDtls =
          IntakeProgramApplicationFactory.newInstance()
            .read(intakeProgramApplicationKey);
        final ProgramTypeKey programTypeKey = new ProgramTypeKey();
        programTypeKey.programTypeID =
          intakeProgramApplicationDtls.programTypeID;
        final ProgramTypeDtls programTypeDtls =
          ProgramTypeFactory.newInstance().read(programTypeKey);
        final ReadByLocalizableTextIDandLocaleKey paramReadByLocalizableTextIDandLocaleKey =
          new ReadByLocalizableTextIDandLocaleKey();
        paramReadByLocalizableTextIDandLocaleKey.localeCode =
          TransactionInfo.getProgramLocale();
        paramReadByLocalizableTextIDandLocaleKey.localizableTextID =
          programTypeDtls.nameTextID;
        final TextTranslation textTranslationObj =
          TextTranslationFactory.newInstance();
        final TextTranslationDtls textTranslationDtls =
          textTranslationObj.readByLocalizableTextIDandLocale(
            paramReadByLocalizableTextIDandLocaleKey);
        final StringBuffer stringBuffer = new StringBuffer();
        if (benefitList.isEmpty()) {
          stringBuffer.append(textTranslationDtls.text);
          benefitList += stringBuffer.toString();
        } else {
          stringBuffer.append(CuramConst.gkComma);
          stringBuffer.append(CuramConst.gkSpace);
          stringBuffer.append(textTranslationDtls.text);
          benefitList += stringBuffer.toString();
        }
      }
    } catch (final Exception e) {
    }
    if (benefitList.isEmpty()) {
      benefitList = BDMUtil.getCodeTableDescriptionForUserLocale(
        CASECATTYPECODEEntry.TABLENAME, icProgramCode);
    }
    return benefitList;
  }

  /*
   * Get the RFR Status that shows in UA
   */
  private String getOnlineAppealStatusCode(final long onlineAppealRequestID)
    throws AppException, InformationalException {

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    // Find the AppealCaseID using the linking table between
    // ONLINEAPPEALREQUEST and AppealCase
    final AppealOnlineRequestIDKey appealOnlineRequestIDKey =
      new AppealOnlineRequestIDKey();
    appealOnlineRequestIDKey.onlineAppealRequestID = onlineAppealRequestID;
    final AppealOnlineRequestLink appealOnlineRequestLink =
      AppealOnlineRequestLinkFactory.newInstance();
    final AppealOnlineRequestLinkDetails appealOnlineRequestLinkDetails =
      appealOnlineRequestLink.readAppealCaseIDForRequest(nfIndicator,
        appealOnlineRequestIDKey);
    // Use the AppealCaseID to find the Appeal Case Status
    if (nfIndicator.isNotFound()) {
      return CZNAPPEALREQUESTSTATUS.SUBMITTED;
    }
    final AppealCaseKey bdmAppealKey = new AppealCaseKey();
    bdmAppealKey.appealCaseKey.caseID =
      appealOnlineRequestLinkDetails.appealCaseID;
    try {
      final DecisionAndAttachmentListDetailsForIC bdmAppealDtls =
        bdmAppeal.readDecisionAndAttachmentListForIC(bdmAppealKey);
      final String statusCode =
        bdmAppealDtls.decisionDetails.decisionResolutionCode;
      if (APPEALCASERESOLUTION.ACCEPTED.equals(statusCode)) {
        return CZNAPPEALREQUESTSTATUS.ACCEPTED;
      } else if (APPEALCASERESOLUTION.NOTDECIDED.equals(statusCode)) {
        return CZNAPPEALREQUESTSTATUS.UNDERCONSIDERATION;
      } else if (HEARINGDECISIONRESOLUTION.APPROVEDINPART
        .equals(statusCode)) {
        return CZNAPPEALREQUESTSTATUS.ACCEPTEDINPART;
      } else if (APPEALCASERESOLUTION.DECISIONMAINTAINED.equals(statusCode)) {
        return CZNAPPEALREQUESTSTATUS.MAINTAINEDWMODIFICATION;
      } else if (APPEALCASERESOLUTION.NOTPERFORMED.equals(statusCode)) {
        return CZNAPPEALREQUESTSTATUS.NOTPERFORMED;
      } else if (APPEALCASERESOLUTION.REJECTED.equals(statusCode)) {
        return CZNAPPEALREQUESTSTATUS.DENIED;
      } else if (APPEALCASERESOLUTION.REMANDED.equals(statusCode)) {
        return CZNAPPEALREQUESTSTATUS.DENIED;
      } else {
        return CZNAPPEALREQUESTSTATUS.UNDERCONSIDERATION;
      }
    } catch (final Exception e) {
      return CZNAPPEALREQUESTSTATUS.UNDERCONSIDERATION;
    }
  }
}
