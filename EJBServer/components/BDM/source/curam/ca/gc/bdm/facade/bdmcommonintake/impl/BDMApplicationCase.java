package curam.ca.gc.bdm.facade.bdmcommonintake.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMApplicationCaseDetails;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMApplicationCaseDetailsList;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMCaseID;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMMyApplicationsList;
import curam.ca.gc.bdm.message.BDMRFRSISSUE;
import curam.ca.gc.bdm.message.impl.BDMAPPLICATIONCASEExceptionCreator;
import curam.cefwidgets.docbuilder.impl.ContentPanelBuilder;
import curam.cefwidgets.docbuilder.impl.StackContainerBuilder;
import curam.codetable.impl.CASEPARTICIPANTROLETYPEEntry;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseFactory;
import curam.commonintake.facade.intf.ApplicationCase;
import curam.commonintake.facade.struct.ApplicationCaseContextPanelDetails;
import curam.commonintake.facade.struct.ApplicationCaseDetails;
import curam.commonintake.facade.struct.ApplicationCaseDetailsList;
import curam.commonintake.facade.struct.ApplicationCaseOwnerDetails;
import curam.commonintake.facade.struct.ApplicationCaseSearchCriteria;
import curam.commonintake.facade.struct.ApplicationClientDetails;
import curam.commonintake.facade.struct.ApplicationClientDetailsList;
import curam.commonintake.facade.struct.CaseParticipantRoleAndVersionNo;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.commonintake.impl.ApplicationCaseHelper;
import curam.commonintake.impl.ApplicationCaseRemoveClientEvent;
import curam.commonintake.message.APPLICATIONCASE;
import curam.commonintake.message.COMMONINTAKEGENERAL;
import curam.commonintake.message.impl.APPLICATIONCASEExceptionCreator;
import curam.core.sl.fact.ClientMergeFactory;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.CuramInd;
import curam.coreabstraction.impl.CICoreAbstraction;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.piwrapper.caseheader.impl.CaseHeader;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.casemanager.impl.CaseParticipantRole;
import curam.piwrapper.casemanager.impl.CaseParticipantRoleDAO;
import curam.piwrapper.impl.ClientURI;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.ValidationHelper;
import curam.util.persistence.helper.EventDispatcherFactory;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationStatusEntry;
import curam.workspaceservices.intake.impl.IntakeApplicationConcernRoleLink;
import curam.workspaceservices.intake.impl.IntakeApplicationConcernRoleLinkDAO;
import curam.workspaceservices.intake.impl.IntakeApplicationDAO;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BDMApplicationCase
  extends curam.ca.gc.bdm.facade.bdmcommonintake.base.BDMApplicationCase

{

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private CICoreAbstraction coreAbstraction;

  @Inject
  private EventDispatcherFactory<ApplicationCaseRemoveClientEvent> removeClientEventsDispatcher;

  @Inject
  private IntakeApplicationDAO intakeApplicationDAO;

  @Inject
  private IntakeApplicationConcernRoleLinkDAO intakeApplicationConcernRoleLinkDAO;

  @Inject
  private CaseParticipantRoleDAO caseParticipantRoleDAO;

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private BDMMyApplicationsHelperImpl myApplicationsHelper;

  @Inject
  private ApplicationCaseHelper applicationCaseHelper;

  /**
   * START 12319 Task Build Flag icon into case context panel by Siva 03/01/2022
   **/
  @Inject
  private BDMApplicationCaseContextPanelHelperImpl applicationCaseContextPanelHelper;

  /**
   * END 12319 Task Build Flag icon into case context panel by Siva 03/01/2022
   **/
  public BDMApplicationCase() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * @author Amod.Gole
   * @version: 1.0
   * @date: Jan. 11, 2022
   * @decription: Task 8401 New method to retrieve submission date for the
   * application.
   * @version 1.1
   * @Altered Jan 20th 2022 by sivakumar.kalyanasun
   * @decription : Task 9092 and 12510 Application Search Extensions and Add 2
   * new search results columns for Application Search
   * @altered 1.2 - 01st March 2022 by Siva
   * @ 12319 Task Build Flag icon into case context panel
   * Added new override method for readContextPanelDetails() for display for
   * UrgentFlag icon .
   * @param :@param key
   * @param :@return
   * @param :@throws AppException
   * @param :@throws InformationalException
   * @throws
   */
  @Override
  public BDMApplicationCaseDetailsList listApplicationCaseByConcernRole(
    final ConcernRoleKey key) throws AppException, InformationalException {

    final ApplicationCase applicationCase =
      ApplicationCaseFactory.newInstance();
    final ApplicationCaseDetailsList applicationCaseDetailsList =
      applicationCase.listApplicationCaseByConcernRole(key);

    final ConcernRole concernRole = concernRoleDAO.get(key.concernRoleID);
    final List<curam.commonintake.impl.ApplicationCase> applicationCaseList =
      this.applicationCaseDAO.listAllByConcernRole(concernRole);

    final BDMApplicationCaseDetailsList newBDMApplicationCaseDetailsList =
      new BDMApplicationCaseDetailsList();
    newBDMApplicationCaseDetailsList.assign(applicationCaseDetailsList);

    final Map<Long, BDMApplicationCaseDetails> applicationIDToApplicationCaseMap =
      new HashMap<Long, BDMApplicationCaseDetails>();
    for (int i = 0; i < applicationCaseDetailsList.dtls.size(); i++) {
      final BDMApplicationCaseDetails tempBDMApplicationCaseDetails =
        new BDMApplicationCaseDetails();
      tempBDMApplicationCaseDetails.dtls
        .assign(applicationCaseDetailsList.dtls.get(i));
      newBDMApplicationCaseDetailsList.dtls
        .add(tempBDMApplicationCaseDetails);
      applicationIDToApplicationCaseMap.put(
        tempBDMApplicationCaseDetails.dtls.caseID,
        tempBDMApplicationCaseDetails);
    }

    for (final curam.commonintake.impl.ApplicationCase tempApplicationCase : applicationCaseList) {
      applicationIDToApplicationCaseMap
        .get(tempApplicationCase.getID()).submissionDatetime =
          tempApplicationCase.getSubmittedDateTime();

    }
    newBDMApplicationCaseDetailsList.dtls.clear();
    newBDMApplicationCaseDetailsList.dtls
      .addAll(applicationIDToApplicationCaseMap.values());

    newBDMApplicationCaseDetailsList.displayNewFormLinkInd =
      myApplicationsHelper.allowApplicationFormCreationForConcernRoleInd(key);

    return newBDMApplicationCaseDetailsList;
  }

  /**
   * Overide OOTb implementation of removeClient method. This method removes
   * caseParticipant from application case.
   *
   * @param caseParticipantRoleVersionAndNo
   * @param caseID
   * @return void
   */
  @Override
  public void removeClient(
    final CaseParticipantRoleAndVersionNo caseParticipantRoleVersionAndNo,
    final BDMCaseID caseID) throws AppException, InformationalException {

    final curam.commonintake.impl.ApplicationCase applicationCase =
      applicationCaseDAO.get(caseID.caseID);

    // raised ApplicationCaseRemoveClientEvent prior to removing a client on
    // applicationcase on which remove client is requested
    this.removeClientEventsDispatcher
      .get(ApplicationCaseRemoveClientEvent.class)
      .preRemoveClient(applicationCase);
    final int internalVersionNo = caseParticipantRoleVersionAndNo.versionNo;

    final CaseParticipantRole caseParticipantRole = caseParticipantRoleDAO
      .get(caseParticipantRoleVersionAndNo.caseParticipantRoleID);

    validateRemoveClient(caseParticipantRole, applicationCase, caseID.caseID);

    // IF casePArticipant is primary client then display validation message
    if (caseParticipantRole.getType()
      .equals(CASEPARTICIPANTROLETYPEEntry.PRIMARY)) {

      ValidationHelper.addValidationError(BDMAPPLICATIONCASEExceptionCreator
        .ERR_PRIMARY_CLIENT_CANNOT_BE_REMOVED());
      ValidationHelper.failIfErrorsExist();
    }

    // Cancell the participant role prior to removing from application case
    coreAbstraction.cancelCaseParticipantRole(caseParticipantRole,
      internalVersionNo);

    if (applicationCase.getConcernRole().getID() == caseParticipantRole
      .getConcernRole().getID().longValue()) {
      CaseParticipantRole primaryCPT = null;
      for (final CaseParticipantRole cpt : applicationCase
        .listActiveCaseMembers()) {
        if (cpt.getType().equals(CASEPARTICIPANTROLETYPEEntry.PRIMARY)) {
          primaryCPT = cpt;
          break;
        }
      }
      if (primaryCPT != null)
        this.coreAbstraction.modifyCaseHeaderConcernRole((CaseHeader) this,
          primaryCPT.getConcernRole());
    }

    // List all concerrole on intake application
    final List<IntakeApplicationConcernRoleLink> intakeApplicationConcernRoleLinks =
      this.intakeApplicationConcernRoleLinkDAO
        .listByConcernRoleIntakeApplication(
          caseParticipantRole.getConcernRole(), this.intakeApplicationDAO
            .readByReference(applicationCase.getApplicationReference()));
    for (final IntakeApplicationConcernRoleLink intakeApplicationConcernRoleLink : intakeApplicationConcernRoleLinks)
      intakeApplicationConcernRoleLink.remove();

    // raised ApplicationCaseRemoveClientEvent after removing a client on
    // applicationcase on which remove client is requested
    this.removeClientEventsDispatcher
      .get(ApplicationCaseRemoveClientEvent.class)
      .postRemoveClient(applicationCase);

  }

  /**
   * This method validates caseparticipantreole prior to removal from
   * application case
   *
   *
   **/
  void validateRemoveClient(final CaseParticipantRole caseParticipantRole,
    final curam.commonintake.impl.ApplicationCase applicationCase,
    final long caseID) throws InformationalException, AppException {

    // Liat all active caseParticipants on application case
    final List<CaseParticipantRole> roles = caseParticipantRoleDAO
      .listActiveMembersByCase(caseHeaderDAO.get(caseID));

    // If only one particiant role exists, display validation message
    if (roles.size() == 1) {
      ValidationHelper.addValidationError(APPLICATIONCASEExceptionCreator
        .ERR_XRV_ONLY_CLIENT_CANNOT_BE_REMOVED());
    } else if (roles.size() > 1 && caseParticipantRole.getType()
      .equals(CASEPARTICIPANTROLETYPEEntry.PRIMARY)) {
      boolean memberFound = false;
      for (final CaseParticipantRole role : roles) {
        if (role.getID().longValue() != caseParticipantRole.getID()
          .longValue()
          && role.getType().equals(CASEPARTICIPANTROLETYPEEntry.MEMBER)) {
          memberFound = true;
          break;
        }
      }
      if (!memberFound)
        ValidationHelper.addValidationError(APPLICATIONCASEExceptionCreator
          .ERR_XRV_ONLY_CLIENT_CANNOT_BE_REMOVED());
    }

    // get list of programs associated to the ApplicationCase. caseparticipant
    // cannot be removed if applicationCase is Approved
    for (final IntakeProgramApplication program : applicationCase
      .getPrograms()) {
      if (program.getLifecycleState()
        .equals(IntakeProgramApplicationStatusEntry.APPROVED))
        ValidationHelper.addValidationError(APPLICATIONCASEExceptionCreator
          .ERR_XRV_CLIENT_CANNOT_BE_REMOVED_IF_ONE_OR_MORE_PROGRAMS_AUTHORISED());
    }
    ValidationHelper.failIfErrorsExist();
  }

  // START Tasks 9092 , 12510 , Search extension and adding 2 new columns for
  // the search results - Siva
  /**
   * This facade method is introduced to call the newly customized Application
   * Case Search functionality .
   *
   */
  @Override
  public BDMMyApplicationsList
    search(final ApplicationCaseSearchCriteria searchCriteria)
      throws AppException, InformationalException {

    final BDMMyApplicationsHelperImpl bdmHelperImpl =
      new BDMMyApplicationsHelperImpl();

    final BDMMyApplicationsList myApplicationsList =
      bdmHelperImpl.getSearchedApplications(searchCriteria);

    return myApplicationsList;
  }

  // END Tasks 9092 , 12510 , Search extension and adding 2 new columns for the
  /**
   * START 12319 Task Build Flag icon into case context panel by Siva 03/01/2022
   **/
  @Override
  public ApplicationCaseContextPanelDetails readContextPanelDetails(
    final CaseHeaderKey key) throws AppException, InformationalException {

    final ApplicationCaseContextPanelDetails applicationCaseContextPanelDetails =
      new ApplicationCaseContextPanelDetails();

    final curam.commonintake.impl.ApplicationCase applicationCase =
      this.applicationCaseDAO.get(key.caseID);

    final LocalisableString tabTitleInfo = new LocalisableString(
      APPLICATIONCASE.INF_APPLICATION_CASE_TYPE_AND_REFERENCE);

    tabTitleInfo.arg(applicationCase.getApplicationCaseAdmin().getName());

    tabTitleInfo.arg(applicationCase.getCaseReference());

    applicationCaseContextPanelDetails.name =
      tabTitleInfo.toClientFormattedText();

    final ContentPanelBuilder containerPanel =
      ContentPanelBuilder.createPanel("container-panel-applicationcase");

    final List<CaseParticipantRole> actionClients =
      applicationCase.listActiveCaseMembers();
    ContentPanelBuilder middlePanel;
    ContentPanelBuilder rightPanel;
    if (actionClients.size() > 1) {

      final StackContainerBuilder stackContainerBuilder =
        this.applicationCaseContextPanelHelper
          .getCaseMemberThumbnailDetails(actionClients);

      containerPanel.addWidgetItem(stackContainerBuilder, "style",
        "stack-container");

      middlePanel = this.applicationCaseContextPanelHelper
        .getContextPanelDetails(applicationCase);

      containerPanel.addWidgetItem(middlePanel, "style", "content-panel",
        "content-panel-detail applicationcase-content-panel applicationcase-content-panel-blue-footer");

    } else {
      rightPanel = this.applicationCaseContextPanelHelper
        .getCaseMemberThumbnailDetails(actionClients.get(0));

      containerPanel.addWidgetItem(rightPanel, "style", "content-panel",
        "content-panel-detail case-participant-panel");

      middlePanel = this.applicationCaseContextPanelHelper
        .getContextPanelDetails(applicationCase);

      containerPanel.addWidgetItem(middlePanel, "style", "content-panel",
        "content-panel-detail applicationcase-content-panel-medium applicationcase-content-panel-blue-footer");

    }

    // RFR Changes : Add RFR message on the context panel
    for (int i = 0; i < actionClients.size(); ++i) {
      final CaseParticipantRole caseParticipantRole = actionClients.get(i);
      if (BDMUtil.countAppealCasesByParticipantID(
        caseParticipantRole.getConcernRole().getID()) > 0) {
        final LocalisableString apString =
          new LocalisableString(BDMRFRSISSUE.INFO_BDM_RFR_CONTEXT_MESSAGE);
        middlePanel.addlocalisableStringItem(apString.toClientFormattedText(),
          "content-bdm-rfr-info-dtls");
        // break from loop as RFR message is added
        break;
      }
    }
    rightPanel = this.applicationCaseContextPanelHelper
      .getContextRightPanelDetails(applicationCase);

    containerPanel.addWidgetItem(rightPanel, "style", "content-panel",
      "content-panel-detail applicationcase-right-panel");

    applicationCaseContextPanelDetails.xmlPanelData =
      containerPanel.toString();

    return applicationCaseContextPanelDetails;
  }

  /**
   * END 12319 Task Build Flag icon into case context panel by Siva 03/01/2022
   **/

  /**
   * @author Amod.Gole
   * @version: 1.0
   * @date: Mar. 11, 2022
   * @decription: Task 19360 Disable 'Remove Client' for Primary Case
   * Participant
   * application.
   *
   * @param :@param ApplicationCaseKey
   * @param :@return ApplicationClientDetailsList
   * @param :@throws AppException
   * @param :@throws InformationalException
   * @throws
   */
  @Override
  public ApplicationClientDetailsList
    listClients(final ApplicationCaseKey key)
      throws AppException, InformationalException {

    final ApplicationCase applicationCase =
      ApplicationCaseFactory.newInstance();
    final ApplicationClientDetailsList applicationClientDetailsList =
      applicationCase.listClients(key);

    for (final ApplicationClientDetails applicationClientDetails : applicationClientDetailsList.dtls) {

      final CaseParticipantRole caseParticipantRole = caseParticipantRoleDAO
        .get(applicationClientDetails.caseParticipantRoleID);
      if (CASEPARTICIPANTROLETYPEEntry.PRIMARY
        .equals(caseParticipantRole.getType())) {
        applicationClientDetails.removeClientInd = false;
      }

    }
    return applicationClientDetailsList;
  }

  /**
   *
   **/
  @Override
  public ApplicationCaseDetailsList listReferralCaseByConcernRole(
    final ConcernRoleKey key) throws AppException, InformationalException {

    byte mask = 0;
    final ConcernRole concernRole =
      this.concernRoleDAO.get(key.concernRoleID);

    final List<curam.commonintake.impl.ApplicationCase> applicationCaseList =
      this.applicationCaseDAO.listAllReferralCaseByConcernRole(concernRole);
    final ApplicationCaseDetailsList applicationCaseDetailsList =
      this.assignApplicationCaseDetails(applicationCaseList);
    final boolean duplicateInd = isDuplicateClient(key);
    if (duplicateInd) {
      applicationCaseDetailsList.displayNewLinkInd = false;
      applicationCaseDetailsList.displayNewFormLinkInd = false;
    } else {
      applicationCaseDetailsList.displayNewLinkInd =
        myApplicationsHelper.allowDirectCreationInd();
      applicationCaseDetailsList.displayNewFormLinkInd = myApplicationsHelper
        .allowApplicationFormCreationForConcernRoleInd(key);
    }
    final Iterator var9 = applicationCaseDetailsList.dtls.iterator();
    while (var9.hasNext()) {
      final ApplicationCaseDetails applicationCaseDetails =
        (ApplicationCaseDetails) var9.next();
      if (applicationCaseDetails.casePrograms.contains(", ")) {
        mask = (byte) (mask | 1);
      }
      if (!applicationCaseDetails.expiryDate.equals(Date.kZeroDate)) {
        mask = (byte) (mask | 2);
      }
      if (3 == mask) {
        break;
      }
    }
    this.setReferalListIndicators(applicationCaseDetailsList, mask);

    return applicationCaseDetailsList;
  }

  /**
   *
   **/
  protected ApplicationCaseDetailsList assignApplicationCaseDetails(
    final List<curam.commonintake.impl.ApplicationCase> applicationCaseList) {

    final ApplicationCaseDetailsList applicationCaseDetailsList =
      new ApplicationCaseDetailsList();
    final Iterator<curam.commonintake.impl.ApplicationCase> ApplicationCaseIterator =
      applicationCaseList.iterator();
    while (ApplicationCaseIterator.hasNext()) {
      final curam.commonintake.impl.ApplicationCase applicationCase =
        ApplicationCaseIterator.next();
      final ApplicationCaseDetails applicationCaseDetails =
        new ApplicationCaseDetails();
      applicationCaseDetails.caseReference =
        applicationCase.getCaseReference();
      applicationCaseDetails.applicationReference =
        applicationCase.getApplicationReference();
      applicationCaseDetails.applicationDate =
        applicationCase.getApplicationDate();
      applicationCaseDetails.status =
        applicationCase.getLifecycleState().getCode();
      applicationCaseDetails.caseClients =
        this.getNames(applicationCase.listActiveCaseMembers());
      applicationCaseDetails.caseType =
        applicationCase.getAdminCaseConfiguration().getLinkText();
      applicationCaseDetails.ownerDetails =
        this.getOwnerDetails(this.caseHeaderDAO.get(applicationCase.getID()));
      applicationCaseDetails.casePrograms =
        this.getProgramsWithNoDuplicateNames(applicationCase.getPrograms());
      applicationCaseDetails.expiryDate =
        this.getEarliestProgramExpireDate(applicationCase.getPrograms());
      applicationCaseDetails.caseName =
        applicationCase.getApplicationCaseAdmin().getName();
      final ClientURI clientURI = this.getApplicationHomeURI(applicationCase);
      applicationCaseDetails.applicationHomeURL = clientURI.getInternalURI();
      applicationCaseDetails.caseID = applicationCase.getID();
      applicationCaseDetailsList.dtls.add(applicationCaseDetails);
    }
    return applicationCaseDetailsList;
  }

  /**
   *
   **/
  protected boolean isDuplicateClient(final ConcernRoleKey concernRoleKey) {

    CuramInd duplicateInd;
    try {
      duplicateInd = ClientMergeFactory.newInstance()
        .isConcernRoleDuplicate(concernRoleKey);
    } catch (final AppException var4) {
      throw new RuntimeException(var4);
    } catch (final InformationalException var5) {
      throw new RuntimeException(var5);
    }
    return duplicateInd.statusInd;
  }

  /**
   *
   **/
  protected String getNames(final List<CaseParticipantRole> roles) {

    String nameString = "";
    final LocalisableString localisableString =
      new LocalisableString(COMMONINTAKEGENERAL.CLIENT_NAME_SEPARATOR);
    final Iterator var4 = roles.iterator();
    while (var4.hasNext()) {
      final CaseParticipantRole caseParticipantRole =
        (CaseParticipantRole) var4.next();

      if (nameString.length() == 0) {
        nameString =
          nameString + caseParticipantRole.getConcernRole().getName();

      } else {
        nameString = nameString
          + localisableString.getMessage(TransactionInfo.getProgramLocale())
          + caseParticipantRole.getConcernRole().getName();
      }
    }

    return nameString;
  }

  /**
   *
   **/
  protected ApplicationCaseOwnerDetails
    getOwnerDetails(final CaseHeader caseDetails) {

    return this.applicationCaseHelper.getOwnerDetails(caseDetails);
  }

  /**
   *
   **/
  protected String getProgramsWithNoDuplicateNames(
    final List<IntakeProgramApplication> programs) {

    String programNameString = "";
    final LocalisableString localisableString =
      new LocalisableString(COMMONINTAKEGENERAL.PROGRAM_TYPE_SEPARATOR);

    final HashMap<String, List<IntakeProgramApplication>> programsAdded =
      new HashMap();

    IntakeProgramApplication intakeProgramApplication;
    for (final Iterator var5 = programs.iterator(); var5
      .hasNext(); programsAdded.put(
        intakeProgramApplication.getProgramType().name().getValue(),
        programs)) {
      intakeProgramApplication = (IntakeProgramApplication) var5.next();
      if (!programsAdded.containsKey(
        intakeProgramApplication.getProgramType().name().getValue())) {
        if (programNameString.length() == 0) {
          programNameString = programNameString
            + intakeProgramApplication.getProgramType().name().getValue();

        } else {
          programNameString = programNameString
            + localisableString.getMessage(TransactionInfo.getProgramLocale())
            + intakeProgramApplication.getProgramType().name().getValue();
        }
      }
    }
    return programNameString;
  }

  /**
   *
   **/
  protected Date getEarliestProgramExpireDate(
    final List<IntakeProgramApplication> programs) {

    return this.applicationCaseHelper.getEarliestExpiryDate(programs);
  }

  /**
   *
   **/
  protected ClientURI getApplicationHomeURI(
    final curam.commonintake.impl.ApplicationCase applicationCase) {

    final ClientURI clientURI = new ClientURI(
      applicationCase.getApplicationCaseAdmin().getHomePageWithDefault());
    clientURI.appendParam("caseID", String.valueOf(applicationCase.getID()));
    return clientURI;
  }

  /**
   *
   **/
  private void setReferalListIndicators(
    final ApplicationCaseDetailsList applicationCaseDetailsList,
    final byte mask) {

    switch (mask) {
      case 0:
        applicationCaseDetailsList.programsExpiryDateNotExistInd = true;
      break;
      case 1:
        applicationCaseDetailsList.programsOnlyInd = true;
      break;
      case 2:
        applicationCaseDetailsList.expiryDateOnlyInd = true;
      break;
      case 3:
        applicationCaseDetailsList.programsExpiryDateExistInd = true;
      break;
      default:
        applicationCaseDetailsList.programsExpiryDateNotExistInd = true;
    }
  }

}
