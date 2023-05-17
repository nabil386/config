package curam.ca.gc.bdmoas.facade.commonintake.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetailsList;
import curam.ca.gc.bdm.sl.bdmcaseurgentflag.impl.BDMMaintainCaseUrgentFlag;
import curam.casepcr.calculations.impl.CasePCR;
import curam.casepcr.calculations.impl.CasePCRDAO;
import curam.casepcr.calculations.impl.PCRTooltipBuilder;
import curam.casepcr.codetable.impl.CASEPCRSTATUSEntry;
import curam.cefwidgets.docbuilder.impl.ContentPanelBuilder;
import curam.cefwidgets.docbuilder.impl.ImageBuilder;
import curam.cefwidgets.docbuilder.impl.LinkBuilder;
import curam.cefwidgets.docbuilder.impl.LinksPanelBuilder;
import curam.cefwidgets.docbuilder.impl.ListBuilder;
import curam.cefwidgets.docbuilder.impl.RotatorBuilder;
import curam.cefwidgets.docbuilder.impl.StackContainerBuilder;
import curam.cefwidgets.docbuilder.impl.helper.impl.CodeTableItemEntry;
import curam.cefwidgets.docbuilder.impl.helper.impl.DocBuilderHelperFactory;
import curam.cefwidgets.utilities.impl.RendererConfig;
import curam.cefwidgets.utilities.impl.RendererConfig.RendererConfigType;
import curam.codetable.impl.ORGOBJECTTYPEEntry;
import curam.commonintake.codetable.impl.CURAMBOOLEANTYPEEntry;
import curam.commonintake.facade.struct.ProgramDetails;
import curam.commonintake.impl.ApplicationCase;
import curam.commonintake.impl.ApplicationCaseAdmin;
import curam.commonintake.message.APPLICATIONCASECONTEXTPANEL;
import curam.commonintake.message.BDMAPPLICATIONCASECONTEXTPANEL;
import curam.commonintake.message.COMMONINTAKEGENERAL;
import curam.core.facade.infrastructure.fact.EvidenceFactory;
import curam.core.facade.struct.CaseIDDetails;
import curam.core.sl.fact.TabDetailFormatterFactory;
import curam.core.sl.fact.TabDetailsHelperFactory;
import curam.core.sl.impl.CaseTabDetailsHelper;
import curam.core.sl.impl.TabDetailFormatterInterface;
import curam.core.sl.impl.TabDetailsHelper;
import curam.core.sl.intf.TabDetailFormatter;
import curam.core.sl.struct.CaseMemberTabDetails;
import curam.core.sl.struct.ContactTabDetails;
import curam.core.sl.struct.FormatPersonNameKey;
import curam.core.sl.struct.IncidentAndRoleTabDetailsList;
import curam.core.sl.struct.InvestigationAndRoleTabDetailsList;
import curam.core.sl.struct.PersonAgeDetails;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.message.casepcr.GENCASEPCR;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.PhoneNumber;
import curam.piwrapper.caseheader.impl.CaseHeader;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.casemanager.impl.CaseParticipantRole;
import curam.piwrapper.impl.AddressDAO;
import curam.piwrapper.impl.EmailAddress;
import curam.piwrapper.organization.impl.OrgObjectLink;
import curam.piwrapper.user.impl.User;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.StringHelper;
import curam.verification.facade.infrastructure.fact.VerificationApplicationFactory;
import curam.verification.facade.infrastructure.intf.VerificationApplication;
import curam.verification.facade.infrastructure.struct.CaseEvidenceVerificationDisplayDetailsList;
import curam.workspaceservices.codetable.impl.IntakeProgramApplicationStatusEntry;
import curam.workspaceservices.intake.impl.IntakeProgramApplication;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @version 1.0
 * @author naveen.garg
 * Added new override method for
 * readContextPanelDetails() for hiding application date.
 *
 */

class BDMOASApplicationCaseContextPanelHelperImpl {

  private static final int kCol5 = 5;

  private static final int kCol4 = 4;

  private static final int kCol3 = 3;

  private static final int kCol2 = 2;

  private static final int kCol1 = 1;

  private static final int kCol5MemberHeaderListWidth = 5;

  private static final int kCol4MemberHeaderListWidth = 5;

  private static final int kCol3MemberHeaderListWidth = 25;

  private static final int kCol2MemberHeaderListWidth = 65;

  private static final int kCol1MemberHeaderListWidth = 5;

  private static final int kCol3MemberListWidth = 30;

  private static final int kCol2MemberListWidth = 65;

  private static final int kCol1MemberListWidth = 5;

  private static final int kNumColsInMemberList = 4;

  private static final int kNumPhotoPanels = 3;

  @Inject
  private AddressDAO addressDAO;

  @Inject
  private CasePCRDAO casePCRDAO;

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  protected BDMMaintainCaseUrgentFlag bmdMaintainCaseUrgentFlag;

  public BDMOASApplicationCaseContextPanelHelperImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  public ContentPanelBuilder getCaseMemberThumbnailDetails(
    final CaseParticipantRole caseParticipantRole)
    throws AppException, InformationalException {

    final CaseMemberTabDetails caseMemberTabDetails =
      this.getCaseMemberTabDetails(caseParticipantRole);

    return CaseTabDetailsHelper
      .getCaseMemberThumbnailDetails(caseMemberTabDetails, false);
  }

  public StackContainerBuilder getCaseMemberThumbnailDetails(
    final List<CaseParticipantRole> caseParticipantRoleList)
    throws AppException, InformationalException {

    final StackContainerBuilder stackContainerBuilder =
      StackContainerBuilder.createStackContainer(
        "stackCon-threeimages-applicationcase", false, true);

    final RotatorBuilder rotatorBuilder =
      RotatorBuilder.createRotator("Rotator_Demo", 3);

    rotatorBuilder.setQuickParam(false);
    rotatorBuilder.setWrapParam(false);

    final ContentPanelBuilder memberListContentPanelBuilder =
      ContentPanelBuilder.createPanel("tab-case-list");

    final ListBuilder memberListBuilderHeader = ListBuilder.createList(5);

    memberListBuilderHeader.setColumnWidthPx(1, 40);
    memberListBuilderHeader.setColumnWidthPx(2, 120);
    memberListBuilderHeader.setColumnWidthPx(3, 120);
    memberListBuilderHeader.setColumnWidthPx(4, 120);
    memberListBuilderHeader.setColumnWidthPx(5, 5);

    memberListBuilderHeader.addColumnTitle(2,
      new LocalisableString(APPLICATIONCASECONTEXTPANEL.INFO_CLIENTS));

    memberListBuilderHeader.addColumnTitle(4,
      new LocalisableString(APPLICATIONCASECONTEXTPANEL.INFO_AGE));

    memberListContentPanelBuilder.addWidgetItem(memberListBuilderHeader,
      "style", "single-list");

    final ListBuilder memberListBuilder = ListBuilder.createList(4);

    memberListBuilder.setColumnWidthPx(1, 40);
    memberListBuilder.setColumnWidthPx(2, 120);
    memberListBuilder.setColumnWidthPx(3, 120);
    memberListBuilder.setColumnWidthPx(4, 120);

    for (int i = 0; i < caseParticipantRoleList.size(); ++i) {

      final CaseParticipantRole caseParticipantRole =
        caseParticipantRoleList.get(i);
      final CaseMemberTabDetails caseMemberTabDetails =
        this.getCaseMemberTabDetails(caseParticipantRole);

      final ContentPanelBuilder thumbnailContentPanelBuilder =
        CaseTabDetailsHelper
          .getCaseMemberThumbnailDetails(caseMemberTabDetails, false);

      rotatorBuilder.addWidgetItem(thumbnailContentPanelBuilder, "style",
        "content-panel");

      this.createMemberRow(memberListBuilder, caseMemberTabDetails, i + 1);

    }

    memberListContentPanelBuilder.addWidgetItem(memberListBuilder, "style",
      "single-list", "table-list");

    stackContainerBuilder.addWidgetItem(rotatorBuilder, "style", "rotator",
      "photo view", "stack-container-photo");

    stackContainerBuilder.addWidgetItem(memberListContentPanelBuilder,
      "style", "content-panel", "list view", "stack-container-list");

    return stackContainerBuilder;
  }

  protected CaseMemberTabDetails
    getCaseMemberTabDetails(final CaseParticipantRole caseParticipantRole) {

    final CaseMemberTabDetails caseMemberTabDetails =
      new CaseMemberTabDetails();
    final ConcernRole concernRole = caseParticipantRole.getConcernRole();
    caseMemberTabDetails.addressData =
      this.addressDAO.get(concernRole.getPrimaryAddressID()).getAddressData();

    caseMemberTabDetails.concernRoleID = concernRole.getID();
    caseMemberTabDetails.concernRoleName = concernRole.getName();
    final EmailAddress emailAddress = concernRole.getEmailAddress();
    if (null != emailAddress) {
      caseMemberTabDetails.emailAddress = emailAddress.getEmail();
    }

    final PhoneNumber primaryPhoneNumber =
      concernRole.getPrimaryPhoneNumber();
    if (null != primaryPhoneNumber) {
      caseMemberTabDetails.phoneNumberID = primaryPhoneNumber.getID();
    }

    caseMemberTabDetails.primaryAlternateID =
      concernRole.getPrimaryAlternateID();
    return caseMemberTabDetails;
  }

  public ContentPanelBuilder
    getContextPanelDetails(final ApplicationCase applicationCase) {

    final ContentPanelBuilder applicationCaseTabDetails =
      ContentPanelBuilder.createPanel("applicationcase-tab-details");

    applicationCaseTabDetails.addRoundedCorners();

    final ContentPanelBuilder applicationCaseDetails =
      ContentPanelBuilder.createPanel("applicationcase-details");

    applicationCaseDetails.addStringItem(
      applicationCase.getApplicationCaseAdmin().getName(), "name");

    applicationCaseDetails.addStringItem(applicationCase.getCaseReference(),
      "reference");

    final ListBuilder applicationListBuilder =
      ListBuilder.createHorizontalList(1);

    applicationCaseDetails.addSingleListItem(applicationListBuilder,
      "applicationcase-details");

    final List<IntakeProgramApplication> appliedProgramsList =
      new ArrayList();

    final Iterator appCaseProgramIterator =
      applicationCase.getPrograms().iterator();
    final Set<String> programNameSet = new HashSet<String>();

    while (appCaseProgramIterator.hasNext()) {
      final IntakeProgramApplication program =
        (IntakeProgramApplication) appCaseProgramIterator.next();
      if (!program.getLifecycleState()
        .equals(IntakeProgramApplicationStatusEntry.WITHDRAWN)) {

        // BEGIN Task 20200 - Adding only unique program names
        final String programName = program.getProgramType().name().getValue();
        if (!programNameSet.contains(programName)) {
          final ProgramDetails details = new ProgramDetails();
          details.name = program.getProgramType().name().getValue();
          appliedProgramsList.add(program);
          programNameSet.add(programName);
        }
        // END Task 20200

      }
    }

    final String programsRequested = this.getPrograms(appliedProgramsList);

    applicationCaseDetails.addlocalisableStringItem(new LocalisableString(
      BDMAPPLICATIONCASECONTEXTPANEL.INFO_BENEFITS_REQUESTED)
        .toClientFormattedText(),
      "application-case-programs");

    applicationCaseDetails.addStringItem(programsRequested,
      "application-case-programs-list");

    applicationCaseTabDetails.addWidgetItem(applicationCaseDetails, "style",
      "content-panel");

    return applicationCaseTabDetails;
  }

  protected ListBuilder
    getApplicationDetailsList(final ApplicationCase applicationCase) {

    int numberOfRows = 0;
    final ListBuilder applicationListBuilder =
      ListBuilder.createHorizontalList(3);

    applicationListBuilder.addRow();
    numberOfRows = numberOfRows + 1;

    applicationListBuilder.addEntry(1, numberOfRows, new LocalisableString(
      APPLICATIONCASECONTEXTPANEL.INFO_APPLICATION_DATE));

    applicationListBuilder.addEntry(2, numberOfRows,
      applicationCase.getApplicationDate());

    applicationListBuilder.addRow();
    ++numberOfRows;
    applicationListBuilder.addEntry(1, numberOfRows, new LocalisableString(
      APPLICATIONCASECONTEXTPANEL.INFO_PREFERRED_CONTACT));

    if (!StringHelper
      .isEmpty(applicationCase.getPreferredContact().getCode())) {

      final CodeTableItemEntry preferredContact =
        DocBuilderHelperFactory.getCodeTableItemEntry(
          applicationCase.getPreferredContact().getCode(),
          "APPLICATION_CASE_PREFERRED_CONTACT");

      applicationListBuilder.addEntry(2, numberOfRows, preferredContact);

    } else {
      applicationListBuilder.addEntry(2, numberOfRows,
        new LocalisableString(
          APPLICATIONCASECONTEXTPANEL.INFO_PREFERRED_CONTACT_NOT_SPECIFIED)
            .getMessage());

    }

    return applicationListBuilder;
  }

  protected LinkBuilder getOwnerLink(final OrgObjectLink ownerOrgObjectLink) {

    final LinkBuilder userLinkBuilder =
      LinkBuilder.createLink(ownerOrgObjectLink.getOrgObject().getName(),
        "Case_resolveOrgObjectTypeHomePage.do");

    if (ownerOrgObjectLink.getOrgObjectType()
      .equals(ORGOBJECTTYPEEntry.USER)) {
      userLinkBuilder.addParameter("userName",
        (String) ownerOrgObjectLink.getOrgObjectIdentifier());

    } else {
      userLinkBuilder.addParameter("orgObjectReference",
        (String) ownerOrgObjectLink.getOrgObjectIdentifier());

    }

    userLinkBuilder.addParameter("orgObjectType",
      ownerOrgObjectLink.getOrgObjectType().getCode());

    userLinkBuilder.openAsModal();

    return userLinkBuilder;
  }

  protected void createMemberRow(final ListBuilder memberListBuilder,
    final CaseMemberTabDetails caseMemberTabDetails, final int rowNo)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = caseMemberTabDetails.concernRoleID;

    final TabDetailFormatter tabDetailFormatterObj =
      TabDetailFormatterFactory.newInstance();
    final FormatPersonNameKey formatPersonNameKey = new FormatPersonNameKey();
    formatPersonNameKey.wrapNameInd = false;

    memberListBuilder.addRow();

    final LinkBuilder linkBuilder =
      this.getCCVLink(caseMemberTabDetails.concernRoleID);

    final RendererConfig rendererConfig =
      new RendererConfig(RendererConfigType.STYLE, "link");

    memberListBuilder.addEntry(1, rowNo, linkBuilder, rendererConfig);

    final String contentPanelID = "case-member-list-panel"
      + String.valueOf(caseMemberTabDetails.concernRoleID);

    final ContentPanelBuilder nameContentPanelBuilder =
      ContentPanelBuilder.createPanelWithID(contentPanelID);

    formatPersonNameKey.concernRoleID = caseMemberTabDetails.concernRoleID;

    final LinkBuilder memberLinkBuilder = LinkBuilder.createLink(
      tabDetailFormatterObj.formatPersonName(formatPersonNameKey).name,
      "Participant_resolvePage.do", memberListBuilder.getWidgetDocument());

    memberLinkBuilder.addParameter("concernRoleID",
      String.valueOf(caseMemberTabDetails.concernRoleID));

    nameContentPanelBuilder.addLinkItem(memberLinkBuilder,
      "link person-name");

    final ContactTabDetails contactTabDetails = new ContactTabDetails();
    contactTabDetails.assign(caseMemberTabDetails);

    final RendererConfig nameContentPanelConfig =
      new RendererConfig(RendererConfigType.STYLE, "content-panel");

    final ContentPanelBuilder personNameIconsPanel =
      ContentPanelBuilder.createPanel("person-name-icons");

    personNameIconsPanel.addWidgetItem(
      TabDetailsHelper.getSpecialCautionDetails(concernRoleKey), "style",
      "content-panel", "list-icon");

    final curam.core.sl.intf.TabDetailsHelper tabDetailsHelperObj =
      TabDetailsHelperFactory.newInstance();

    final IncidentAndRoleTabDetailsList incidentList =
      tabDetailsHelperObj.readIncidentTabDetails(concernRoleKey);
    if (incidentList.dtls.size() > 0) {
      personNameIconsPanel.addWidgetItem(CaseTabDetailsHelper
        .getMemberIncidentDetails(concernRoleKey, incidentList), "style",
        "content-panel", "list-icon");

    }

    final InvestigationAndRoleTabDetailsList investigationList =
      tabDetailsHelperObj.readInvestigationTabDetails(concernRoleKey);
    if (investigationList.dtls.size() > 0) {

      personNameIconsPanel.addWidgetItem(CaseTabDetailsHelper
        .getMemberInvestigationDetails(concernRoleKey, investigationList),
        "style", "content-panel", "list-icon");

    }

    memberListBuilder.addEntry(2, rowNo, nameContentPanelBuilder,
      nameContentPanelConfig);

    memberListBuilder.addEntry(3, rowNo, personNameIconsPanel,
      nameContentPanelConfig);

    final PersonAgeDetails personAgeDetails = new PersonAgeDetails();
    personAgeDetails.dateOfCalculation = Date.getCurrentDate();
    personAgeDetails.personID = caseMemberTabDetails.concernRoleID;
    personAgeDetails.indDisplayDateOfBirth = false;

    final TabDetailFormatterInterface tabDetailFormatterInterface =
      (TabDetailFormatterInterface) TabDetailFormatterFactory.newInstance();

    final LocalisableString ageString = tabDetailFormatterInterface
      .formatAge(personAgeDetails).localisableAgeString;

    memberListBuilder.addEntry(4, rowNo, ageString);

  }

  protected LinkBuilder getCCVLink(final long concernRoleID) {

    final LinkBuilder linkBuilder = LinkBuilder.createLink("",
      "../charts/Citizen_resolveCitizenViewer.jsp");

    linkBuilder.addParameter("concernRoleID", String.valueOf(concernRoleID));

    linkBuilder
      .setTextResource("curam.widget.render.infrastructure.i18n.Image");
    linkBuilder.addImageWithAltTextMessage("IconCCV",
      new LocalisableString(
        APPLICATIONCASECONTEXTPANEL.INFO_CITIZEN_CONTEXT_VIEWER)
          .toClientFormattedText());

    linkBuilder.addOnClickAttribute("return openContextViewer(this, event)");

    linkBuilder.addAriaLabel(new LocalisableString(
      APPLICATIONCASECONTEXTPANEL.INFO_CITIZEN_CONTEXT_VIEWER));

    linkBuilder
      .addLinkTitle(APPLICATIONCASECONTEXTPANEL.INFO_CITIZEN_CONTEXT_VIEWER
        .getMessageText());

    return linkBuilder;
  }

  public ContentPanelBuilder
    getContextRightPanelDetails(final ApplicationCase applicationCase)
      throws AppException, InformationalException {

    final LinksPanelBuilder linksPanelBuilder = LinksPanelBuilder
      .createLinksPanel(5, "application-right-panel-table-links");

    final ContentPanelBuilder applicationStatusContentPanelBuilder =
      ContentPanelBuilder.createPanel("application-case-outer-status");

    final ApplicationCaseAdmin applicationCaseAdmin =
      applicationCase.getApplicationCaseAdmin();

    applicationStatusContentPanelBuilder.addCodetableItem(
      applicationCase.getLifecycleState().getCode(),
      "APPLICATION_CASE_STATUS", "application-case-status");

    final CaseHeader caseHeader =
      this.caseHeaderDAO.get(applicationCase.getID());

    final CasePCR casePCR =
      this.casePCRDAO.readLatestBy(caseHeader, CASEPCRSTATUSEntry.CURRENT);

    if (CURAMBOOLEANTYPEEntry.TRUE.equals(applicationCaseAdmin.isPCREnabled())
      && casePCR != null) {

      final ContentPanelBuilder pcrTooltipContentPanelBuilder =
        ContentPanelBuilder.createPanel("");

      final PCRTooltipBuilder pcrTooltipBuilder = new PCRTooltipBuilder();
      final LocalisableString tooltipTitle =
        new LocalisableString(GENCASEPCR.GEN_PCR_TITLE);

      final LinkBuilder pcrLinkBuilder = LinkBuilder.createLocalizableLink(
        tooltipTitle.toClientFormattedText(),
        "CommonIntake_listCasePCRPage.do");

      pcrLinkBuilder.addParameter("caseID",
        String.valueOf(applicationCase.getID()));

      pcrTooltipBuilder.buildPCRTooltip(pcrTooltipContentPanelBuilder,
        caseHeader.getID(), pcrLinkBuilder);

      applicationStatusContentPanelBuilder.addWidgetItem(
        pcrTooltipContentPanelBuilder, "style", "content-panel",
        "intake-tooltip");
    }

    linksPanelBuilder.addContentPanel(applicationStatusContentPanelBuilder);

    final ImageBuilder verificationIcon =
      ImageBuilder.createImage("IconVerification", "");

    verificationIcon
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");

    final LocalisableString verificationsString = new LocalisableString(
      APPLICATIONCASECONTEXTPANEL.INFO_APPLICATION_VERIFICATIONS);

    final CaseKey applicationCaseKey = new CaseKey();
    applicationCaseKey.caseID = caseHeader.getID();

    final VerificationApplication verificationApplication =
      VerificationApplicationFactory.newInstance();

    final CaseEvidenceVerificationDisplayDetailsList outstandingVerificationDetailsForCaseEvidence =
      verificationApplication
        .listOutstandingVerificationDetailsForCaseEvidence(
          applicationCaseKey);

    verificationsString
      .arg(outstandingVerificationDetailsForCaseEvidence.dtls.size());

    final LinkBuilder verificationLinkBuilder = LinkBuilder
      .createLocalizableLink(verificationsString.toClientFormattedText(),
        "CommonIntake_resolveListVerificationsForCasePage.do");

    verificationLinkBuilder.addParameter("caseID",
      String.valueOf(applicationCase.getID()));

    linksPanelBuilder.addLinkEntry(verificationIcon, verificationLinkBuilder,
      "icon-verifications");

    final ImageBuilder issueIcon =
      ImageBuilder.createImage("Issues.Image", "");

    final CaseKey caseKey = new CaseKey();

    caseKey.caseID = applicationCase.getID();

    final int issuesOutstanding =
      EvidenceFactory.newInstance().listIssuesForCase(caseKey).dtls.size();

    issueIcon.setImageResource(
      "curam.widget.render.infrastructure.i18n.ApplicationContextPanelImage");
    final String issueText = new LocalisableString(
      APPLICATIONCASECONTEXTPANEL.INFO_APPLICATION_ISSUES)
        .arg(issuesOutstanding).toClientFormattedText();

    issueIcon.setImageAltText(issueText);
    final LocalisableString issueString = new LocalisableString(
      APPLICATIONCASECONTEXTPANEL.INFO_APPLICATION_ISSUES)
        .arg(issuesOutstanding);

    final LinkBuilder issueLinkBuilder =
      LinkBuilder.createLocalizableLink(issueString.toClientFormattedText(),
        "CommonIntake_listIssuesForCasePage.do");

    issueLinkBuilder.addParameter("caseID",
      String.valueOf(applicationCase.getID()));

    linksPanelBuilder.addLinkEntry(issueIcon, issueLinkBuilder,
      "icon-issues");

    final LocalisableString altTextTooltipMessage = new LocalisableString(
      BDMAPPLICATIONCASECONTEXTPANEL.INF_URGENTFLAG_TOOLTIP);
    final ImageBuilder urgentFlagIcon =
      ImageBuilder.createImage("Warning.Icon", "");

    urgentFlagIcon.setImageResource("cefwidgets.Image");
    urgentFlagIcon
      .setImageAltText(altTextTooltipMessage.toClientFormattedText());

    final LocalisableString urgentFlagString =
      new LocalisableString(BDMAPPLICATIONCASECONTEXTPANEL.INFO_URGENT_FLAF);

    final CaseIDDetails caseIDDetails = new CaseIDDetails();
    caseIDDetails.caseID = applicationCase.getID();
    final BDMCaseUrgentFlagDetailsList urgentFlagListDetails =
      bmdMaintainCaseUrgentFlag.listCurrentUrgentFlags(caseIDDetails);

    urgentFlagString.arg(urgentFlagListDetails.dtls.size());

    final LinkBuilder urgentFlagLinkbuilder = LinkBuilder
      .createLocalizableLink(urgentFlagString.toClientFormattedText(),
        "BDM_APP_UrgentFlag_listForCasePage.do");

    urgentFlagLinkbuilder.addParameter("caseID",
      String.valueOf(applicationCase.getID()));

    linksPanelBuilder.addLinkEntry(urgentFlagIcon, urgentFlagLinkbuilder,
      "icon-warning");

    final ContentPanelBuilder caseOwnerPanelBuilder =
      ContentPanelBuilder.createPanel("case-owner-panel");

    final ImageBuilder ownerImageBuilder =
      ImageBuilder.createImage("IconIntakeWorker", "");

    ownerImageBuilder.setImageResource("intake.i18n.Image");

    final LocalisableString altTextLocalizedMessage =
      new LocalisableString(APPLICATIONCASECONTEXTPANEL.INFO_CASE_OWNER);

    ownerImageBuilder
      .setImageAltText(altTextLocalizedMessage.toClientFormattedText());

    caseOwnerPanelBuilder.addImageItem(ownerImageBuilder, "case-owner-icon");

    final LinkBuilder caseOwnerLinkBuilder =
      this.getCaseOwnerLink(applicationCase.getOwnerOrgObjectLink());
    caseOwnerPanelBuilder.addLinkItem(caseOwnerLinkBuilder, "link");

    linksPanelBuilder.addContentPanel(caseOwnerPanelBuilder);

    return linksPanelBuilder.getLinksPanel();
  }

  protected LinkBuilder
    getCaseOwnerLink(final OrgObjectLink ownerOrgObjectLink) {

    final LinkBuilder userLinkBuilder =
      LinkBuilder.createLink(ownerOrgObjectLink.getOrgObject().getName(),
        "Case_resolveOrgObjectTypeHomePage.do");

    if (ownerOrgObjectLink.getOrgObjectType()
      .equals(ORGOBJECTTYPEEntry.USER)) {
      userLinkBuilder.addParameter("userName",
        (String) ownerOrgObjectLink.getOrgObjectIdentifier());

    } else {
      userLinkBuilder.addParameter("orgObjectReference",
        String.valueOf(ownerOrgObjectLink.getOrgObjectIdentifier()));

    }

    userLinkBuilder.addParameter("orgObjectType",
      ownerOrgObjectLink.getOrgObjectType().getCode());

    userLinkBuilder.openAsModal();

    return userLinkBuilder;
  }

  protected LinkBuilder getUserLink(final User user) {

    final LinkBuilder userLinkBuilder = LinkBuilder
      .createLink(user.getFullName(), "Organization_viewUserDetailsPage.do");

    userLinkBuilder.addParameter("userName", user.getUsername());

    userLinkBuilder.openAsModal();
    return userLinkBuilder;
  }

  protected String
    getPrograms(final List<IntakeProgramApplication> programList) {

    String programString = "";
    final LocalisableString localisableString =
      new LocalisableString(COMMONINTAKEGENERAL.CLIENT_NAME_SEPARATOR);

    final Iterator programListIterator = programList.iterator();
    while (programListIterator.hasNext()) {
      final IntakeProgramApplication program =
        (IntakeProgramApplication) programListIterator.next();
      if (programString.length() == 0) {
        programString =
          programString + program.getProgramType().name().getValue();

      } else {
        programString = programString
          + localisableString.getMessage(TransactionInfo.getProgramLocale())
          + program.getProgramType().name().getValue();
      }
    }

    return programString;
  }
}
