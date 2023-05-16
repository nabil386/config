/**
 *
 */
package curam.ca.gc.bdm.facade.integratedcase.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetailsList;
import curam.ca.gc.bdm.message.BDMRFRSISSUE;
import curam.ca.gc.bdm.sl.bdmcaseurgentflag.impl.BDMMaintainCaseUrgentFlag;
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
import curam.codetable.CASECATTYPECODE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.LOCATIONACCESSTYPE;
import curam.codetable.ORGOBJECTTYPE;
import curam.codetable.RELATIONSHIPTYPECODE;
import curam.codetable.RESOLUTIONSTATUSCODE;
import curam.commonintake.message.BDMAPPLICATIONCASECONTEXTPANEL;
import curam.core.facade.struct.CaseIDDetails;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.DataBasedSecurity;
import curam.core.impl.EnvVars;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.fact.IssueDeliveryFactory;
import curam.core.sl.entity.struct.CaseParticipantTabDetailList;
import curam.core.sl.entity.struct.IssueRollOverDetailKey;
import curam.core.sl.entity.struct.IssueRollOverDetailList;
import curam.core.sl.entity.struct.OrgObjectLinkKey;
import curam.core.sl.entity.struct.SearchForIntegratedCaseTabDetailKey;
import curam.core.sl.fact.CaseUserRoleFactory;
import curam.core.sl.fact.TabDetailFormatterFactory;
import curam.core.sl.fact.TabDetailsHelperFactory;
import curam.core.sl.impl.CaseTabDetailsHelper;
import curam.core.sl.impl.ProductDeliveryTab;
import curam.core.sl.impl.TabDetailFormatterInterface;
import curam.core.sl.infrastructure.impl.GeneralConst;
import curam.core.sl.infrastructure.impl.ReflectionConst;
import curam.core.sl.infrastructure.impl.UimConst;
import curam.core.sl.intf.CaseUserRole;
import curam.core.sl.intf.TabDetailFormatter;
import curam.core.sl.intf.TabDetailsHelper;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.CaseMemberTabDetails;
import curam.core.sl.struct.CaseOwnerDetails;
import curam.core.sl.struct.ContactTabDetails;
import curam.core.sl.struct.FormatPersonNameKey;
import curam.core.sl.struct.IncidentAndRoleTabDetailsList;
import curam.core.sl.struct.IntegratedCaseTabDetail;
import curam.core.sl.struct.InvestigationAndRoleTabDetailsList;
import curam.core.sl.struct.ParticipantSecurityCheckKey;
import curam.core.sl.struct.PersonAgeDetails;
import curam.core.struct.CaseConcernRoleName;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseSecurityCheckKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.DataBasedSecurityResult;
import curam.core.struct.IntegratedCaseTabDtls;
import curam.core.struct.IntegratedCaseTabKey;
import curam.core.struct.ReadParticipantRoleIDDetails;
import curam.core.struct.UsersKey;
import curam.message.BPOCASETAB;
import curam.message.BPOREFLECTION;
import curam.message.GENERALCASE;
import curam.message.GENERALCONCERN;
import curam.message.IMAGEPANELBUILDER;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.DatabaseException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author sivakumar.kalyanasun
 * @version 1.0 Task 19383 - Urgent Flag for IC Context Panel This process class
 * provides the functionality for the Integrated Case section tab
 * details service layer.
 */
public class BDMIntegratedCaseTabHelper
  extends curam.core.sl.base.IntegratedCaseTab {

  private final BDMMaintainCaseUrgentFlag bmdMaintainCaseUrgentFlag =
    new BDMMaintainCaseUrgentFlag();

  // ___________________________________________________________________________
  /**
   * Read the details for the integrated case tab details panel.
   *
   * @param key Contains the case ID the other fields are are constants required
   * for the query. They are populated in this method.
   * @return IntegratedCaseTabDetail The details required for the integrated
   * case
   * tab details panel.
   *
   * @throws InformationalException Generic Exception Signature.
   * @throws AppException Generic Exception Signature.
   */
  @Override
  public IntegratedCaseTabDetail readIntegratedCaseTabDetail(
    final CaseIDKey key) throws AppException, InformationalException {

    final IntegratedCaseTabDetail integratedCaseTabDetail =
      new IntegratedCaseTabDetail();

    final IntegratedCaseTabKey integratedCaseTabKey =
      new IntegratedCaseTabKey();

    integratedCaseTabKey.caseID = key.caseID;

    final IntegratedCaseTabDtls integratedCaseTabDtls = CaseHeaderFactory
      .newInstance().readIntegratedCaseTabDetails(integratedCaseTabKey);

    integratedCaseTabDetail.caseReference =
      integratedCaseTabDtls.caseReference;

    // Read Integrated cases' owner name
    final CaseUserRole caseUserRoleObj = CaseUserRoleFactory.newInstance();
    final OrgObjectLinkKey orgObjectLinkKey = new OrgObjectLinkKey();

    orgObjectLinkKey.orgObjectLinkID =
      integratedCaseTabDtls.ownerOrgObjectLinkID;

    final CaseOwnerDetails caseOwnerDetails =
      caseUserRoleObj.readOwnerName(orgObjectLinkKey);

    if (caseOwnerDetails.orgObjectType.equals(ORGOBJECTTYPE.USER)) {

      final TabDetailFormatter tabDetailFormatterObj =
        TabDetailFormatterFactory.newInstance();
      final UsersKey usersKey = new UsersKey();

      usersKey.userName = caseOwnerDetails.userName;

      caseOwnerDetails.orgObjectReferenceName =
        tabDetailFormatterObj.formatUserFullName(usersKey).fullName;

    } else {
      caseOwnerDetails.orgObjectReferenceName =
        caseOwnerDetails.orgObjectReferenceName;
    }

    IssueRollOverDetailList issueDetailsList = new IssueRollOverDetailList();

    // if (integratedCaseTabDtls.issuesCount > CuramConst.gkZero) {

    // Read issue details list
    final IssueRollOverDetailKey issueRollOverDetailKey =
      new IssueRollOverDetailKey();

    issueRollOverDetailKey.cancelledCaseStatus = CASESTATUS.CANCELED;
    issueRollOverDetailKey.closedCaseStatus = CASESTATUS.CLOSED;
    issueRollOverDetailKey.relatedCaseID = integratedCaseTabDtls.caseID;
    issueRollOverDetailKey.resolutionCancelledCode =
      RESOLUTIONSTATUSCODE.CANCELED;

    issueDetailsList = IssueDeliveryFactory.newInstance()
      .searchActiveByRelatedCaseID(issueRollOverDetailKey);
    // }

    final SearchForIntegratedCaseTabDetailKey searchForIntegratedCaseTabDetailKey =
      new SearchForIntegratedCaseTabDetailKey();

    searchForIntegratedCaseTabDetailKey.caseID = key.caseID;

    final CaseParticipantTabDetailList caseParticipantTabDetailList =
      CaseParticipantRoleFactory.newInstance()
        .searchForIntegratedCaseTabDetail(
          searchForIntegratedCaseTabDetailKey);

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

    caseHeaderKey.caseID = key.caseID;

    final ReadParticipantRoleIDDetails readParticipantRoleIDDetails =
      CaseHeaderFactory.newInstance().readParticipantRoleID(caseHeaderKey);

    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();

    final CaseSecurityCheckKey caseSecurityCheckKey =
      new CaseSecurityCheckKey();

    caseSecurityCheckKey.caseID = key.caseID;
    caseSecurityCheckKey.type = DataBasedSecurity.kReadSecurityCheck;

    // This case security check will check primary participant as well
    final DataBasedSecurityResult dataBasedSecurityResult =
      dataBasedSecurity.checkCaseSecurity1(caseSecurityCheckKey);

    if (!dataBasedSecurityResult.result) {
      if (dataBasedSecurityResult.readOnly) {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);
      } else if (dataBasedSecurityResult.restricted) {
        throw new AppException(GENERALCONCERN.ERR_CONCERNROLE_FV_SENSITIVE);
      } else {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
      }
    }

    for (int i = 0; i < caseParticipantTabDetailList.dtls.size(); i++) {

      if (caseParticipantTabDetailList.dtls.item(
        i).concernRoleID == readParticipantRoleIDDetails.concernRoleID) {

        final curam.core.sl.entity.struct.CaseParticipantTabDetail primaryCaseParticipant =
          caseParticipantTabDetailList.dtls.item(i);

        // Remove primary case participant from list
        caseParticipantTabDetailList.dtls.remove(i);

        // Set the relationship type to be 'Self'
        primaryCaseParticipant.relationshipType = RELATIONSHIPTYPECODE.SELF;

        // Add to them to the start of the list
        caseParticipantTabDetailList.dtls.add(0, primaryCaseParticipant);
      }
    }

    // Create a container content panel
    String containerCSSClass =
      CuramConst.gkContainerPanelIC + CuramConst.gkSpace;
    if (caseParticipantTabDetailList.dtls.size() > CuramConst.gkOne) {
      containerCSSClass += CuramConst.gkMultiPerson;
    } else {
      containerCSSClass += CuramConst.gkSinglePerson;
    }
    final ContentPanelBuilder containerPanel =
      ContentPanelBuilder.createPanel(containerCSSClass);

    // RFR Changes : Add RFR message on the context panel
    for (int i = 0; i < caseParticipantTabDetailList.dtls.size(); i++) {
      if (BDMUtil.countAppealCasesByParticipantID(
        caseParticipantTabDetailList.dtls.item(i).concernRoleID) > 0) {
        final LocalisableString apString =
          new LocalisableString(BDMRFRSISSUE.INFO_BDM_RFR_CONTEXT_MESSAGE);
        containerPanel.addlocalisableStringItem(
          apString.toClientFormattedText(), "content-bdm-rfr-info-dtls");
        // break from loop as RFR message is added
        break;
      }
    }

    String personName = null;
    String sin = null;

    if (caseParticipantTabDetailList.dtls.size() > CuramConst.gkOne) {
      // Use Stack Container
      // Build left panel
      final StackContainerBuilder stackContainerBuilder =
        getCaseMemberThumbnailDetails(caseParticipantTabDetailList, 4);

      // Add the left panel
      containerPanel.addWidgetItem(stackContainerBuilder, CuramConst.gkStyle,
        CuramConst.gkStyleStackContainer);
    } else {

      // Skip constructing the image panel if a member has been ended.
      if (caseParticipantTabDetailList.dtls.size() != 0) {
        final CaseMemberTabDetails caseMemberTabDetails =
          new CaseMemberTabDetails();

        caseMemberTabDetails
          .assign(caseParticipantTabDetailList.dtls.item(0));

        final boolean displayRoles = Configuration
          .getBooleanProperty(EnvVars.ENV_DISPLAY_PARTICIPANT_ROLE);

        // Build left panel
        final ContentPanelBuilder imagePanel = CaseTabDetailsHelper
          .getCaseMemberThumbnailDetails(caseMemberTabDetails, displayRoles);

        // Add the left panel to the container panel
        containerPanel.addWidgetItem(imagePanel, CuramConst.gkStyle,
          CuramConst.gkContentPanel, CuramConst.gkCaseParticipantPanelSingle);

        personName = caseMemberTabDetails.concernRoleName;
        // BUG 103577 - START - MR
        final BDMUtil bdmUtil = new BDMUtil();
        sin =
          bdmUtil.getSINNumberForPerson(caseMemberTabDetails.concernRoleID);
        // BUG 103577 - END - MR
      }
    }

    // Build right panel
    final ContentPanelBuilder linksPanel = getIntegratedCaseLinks(
      integratedCaseTabDtls, issueDetailsList, caseOwnerDetails);

    // Add the right panel to the border container
    containerPanel.addWidgetItem(linksPanel, CuramConst.gkStyle,
      CuramConst.gkContentPanel, CuramConst.gkICLinksPanel);

    String xmlData = containerPanel.toString();

    if (personName != null) {
      final String personNameAndSin = String.format("%s %s %s", personName,
        sin != null && !sin.isEmpty() ? "|" : "",
        sin != null && !sin.isEmpty() ? sin : "");
      xmlData = xmlData.replaceFirst(
        "\"tooltip-title-nolink\"><data>" + personName + "</data>",
        "\"tooltip-title-nolink\"><data>" + personNameAndSin + "</data>");
    }

    integratedCaseTabDetail.xmlPanelData = xmlData;

    final CaseKey caseKey = new CaseKey();

    caseKey.caseID = key.caseID;
    final CaseConcernRoleName caseConcernRoleName =
      CaseHeaderFactory.newInstance().readCaseConcernRoleName(caseKey);

    // BEGIN, 201560 JJ
    final boolean hidePersonName = Configuration.getBooleanProperty(
      EnvVars.ENV_DISPLAY_PRIMARY_CLIENT_NAME_ON_IC_TAB_TITLE_HEADER,
      EnvVars.ENV_DISPLAY_PRIMARY_CLIENT_NAME_ON_IC_TAB_TITLE_HEADER_DEFAULT);

    // Format the integrated tab description
    integratedCaseTabDetail.caseType = CodeTable.getOneItem(
      CASECATTYPECODE.TABLENAME, integratedCaseTabDtls.integratedCaseType,
      TransactionInfo.getProgramLocale());

    // by default primary client name id hidden from tab title and header
    if (!hidePersonName) {

      final String conditionalFormedPersonName =
        CuramConst.kSeparator + new LocalisableString(BPOCASETAB.INF_PAYMENT)
          .arg(caseConcernRoleName.concernRoleName).getMessage();

      integratedCaseTabDetail.conditionalFormedPersonNameOpt =
        conditionalFormedPersonName;
    }
    // END, 201560
    return integratedCaseTabDetail;
  }

  // ___________________________________________________________________________
  /**
   * Format XML data for an Integrated case member details list.
   *
   * @param list Integrated case member details list.
   *
   * @return ContentPanelBuilder.
   */
  protected StackContainerBuilder getCaseMemberThumbnailDetails(
    final CaseParticipantTabDetailList list, final int numRotatorPhotos)
    throws AppException, InformationalException {

    // BEGIN, 194969 JJ
    final boolean displayRoles =
      Configuration.getBooleanProperty(EnvVars.ENV_DISPLAY_PARTICIPANT_ROLE,
        EnvVars.ENV_DISPLAY_PARTICIPANT_ROLE_DEFAULT);

    final StackContainerBuilder stackContainerBuilder =
      StackContainerBuilder.createStackContainer(
        CuramConst.gkIDStackContainerFourImages, false, true);

    final RotatorBuilder rotatorBuilder =
      RotatorBuilder.createRotator(CuramConst.gkIDRotator, numRotatorPhotos);

    rotatorBuilder.setQuickParam(false);
    rotatorBuilder.setWrapParam(false);

    CaseMemberTabDetails caseMemberTabDetails;

    final ContentPanelBuilder memberListContentPanelBuilder =
      ContentPanelBuilder.createPanel(CuramConst.gkPanelCaseMemberList);

    final ListBuilder memberListBuilderHeader = ListBuilder.createList(6);

    // set the column widths of the table
    memberListBuilderHeader.setColumnWidthPx(1, 40);
    memberListBuilderHeader.setColumnWidthPx(2, 166);
    memberListBuilderHeader.setColumnWidthPx(3, 166);
    memberListBuilderHeader.setColumnWidthPx(4, 166);
    memberListBuilderHeader.setColumnWidthPx(5, 166);

    // set the column titles
    memberListBuilderHeader.addColumnTitle(2,
      new LocalisableString(BPOCASETAB.INF_MEMBERS_LABEL));
    // if display participant's role configuration property is YES, then only
    // add relationship column
    if (displayRoles) {
      memberListBuilderHeader.addColumnTitle(4,
        new LocalisableString(BPOCASETAB.INF_MEMBER_RELATIONSHIP_LABEL));
    }
    memberListBuilderHeader.addColumnTitle(5,
      new LocalisableString(BPOCASETAB.INF_AGE_LABEL));

    memberListContentPanelBuilder.addWidgetItem(memberListBuilderHeader,
      CuramConst.gkStyle, CuramConst.gkStyleSingleList);

    final ListBuilder memberListBuilder = ListBuilder.createList(5);

    // set the column widths of the table
    memberListBuilder.setColumnWidthPx(1, 40);
    memberListBuilder.setColumnWidthPx(2, 166);
    memberListBuilder.setColumnWidthPx(3, 166);
    memberListBuilder.setColumnWidthPx(4, 166);
    memberListBuilder.setColumnWidthPx(5, 166);

    // BEGIN, 192609, JJ

    for (int i = 0; i < list.dtls.size(); i++) {

      caseMemberTabDetails = new CaseMemberTabDetails();

      caseMemberTabDetails.assign(list.dtls.item(i));

      // populate case member photo item
      final ContentPanelBuilder thumbnailContentPanelBuilder =
        CaseTabDetailsHelper
          .getCaseMemberThumbnailDetails(caseMemberTabDetails, displayRoles);

      rotatorBuilder.addWidgetItem(thumbnailContentPanelBuilder,
        CuramConst.gkStyle, CuramConst.gkContentPanel);

      // populate case member list row
      createMemberRow(memberListBuilder, caseMemberTabDetails,
        i + CuramConst.gkOne);

    }
    // END, 192609
    // END, 194969

    // Add the list to the content panel
    memberListContentPanelBuilder.addWidgetItem(memberListBuilder,
      CuramConst.gkStyle, CuramConst.gkStyleSingleList,
      CuramConst.gkTableList);

    stackContainerBuilder.addWidgetItem(rotatorBuilder, CuramConst.gkStyle,
      CuramConst.gkStyleRotator, CuramConst.gkStackContainerTitlePhoto,
      CuramConst.gkStyleStackContainerPhoto);

    stackContainerBuilder.addWidgetItem(memberListContentPanelBuilder,
      CuramConst.gkStyle, CuramConst.gkContentPanel,
      CuramConst.gkStackContainerTitleList,
      CuramConst.gkStyleStackContainerList);

    return stackContainerBuilder;
  }

  // ___________________________________________________________________________
  /**
   * Read the tab details links for an Integrated case.
   *
   * @param details Integrated Case details.
   * @param issueList Issue details list
   * @param caseOwnerDetails Integrated case owner details
   *
   * @return ContentPanelBuilder.
   */
  protected ContentPanelBuilder getIntegratedCaseLinks(
    final IntegratedCaseTabDtls details,
    final IssueRollOverDetailList issueList,
    final CaseOwnerDetails caseOwnerDetails)
    throws AppException, InformationalException {

    // Create a links panel with four links per column
    final LinksPanelBuilder linksPanelBuilder =
      LinksPanelBuilder.createLinksPanel(3);

    final ContentPanelBuilder caseStatus =
      ContentPanelBuilder.createPanel(CuramConst.gkICCaseStatus);

    final String status = new LocalisableString(BPOCASETAB.INF_STATUS)
      .arg(CodeTable.getOneItem(CASESTATUS.TABLENAME, details.statusCode))
      .toClientFormattedText();

    caseStatus.addlocalisableStringItem(status, CuramConst.gkICStatus);

    linksPanelBuilder.addContentPanel(caseStatus);

    // Add verification link
    ProductDeliveryTab.addVerificationLinkItem(linksPanelBuilder,
      details.verificationsCount, details.caseID, false);

    // Add evidence in edit link
    ProductDeliveryTab.addEvidenceInEditLinkItem(linksPanelBuilder,
      details.inEditEvidenceCount, details.caseID, false);

    // Added for urgentFlag
    // addUrgentFlagLinkItem(linksPanelBuilder, 0, details.caseID, false);

    // Add issues link
    if (issueList.dtls.size() > CuramConst.gkZero) {
      ProductDeliveryTab.addIssueLinkItem(linksPanelBuilder, issueList,
        details.caseID, false);
    }
    // Add Urgent Flag link
    addUrgentLinkItem(linksPanelBuilder, details.caseID);

    if (Configuration.getBooleanProperty(EnvVars.ENV_APPEALS_ISINSTALLED)) {
      // Add appeals link
      final CaseIDKey caseIDKey = new CaseIDKey();

      caseIDKey.caseID = details.caseID;

      final long appealsCount = getAppealsCount(caseIDKey);

      if (appealsCount > CuramConst.gkZero) {
        final LocalisableString altTextTooltipMessage =
          new LocalisableString(BPOCASETAB.INF_APPEALS_TOOLTIP);

        final ImageBuilder imageBuilder = ImageBuilder
          .createImage(CuramConst.gkIconAppeal, CuramConst.gkEmpty);

        imageBuilder.setImageResource(CuramConst.gkRendererImages);
        imageBuilder
          .setImageAltText(altTextTooltipMessage.toClientFormattedText());

        final String appealsLink =
          new LocalisableString(BPOCASETAB.INF_NUM_APPEALS).arg(appealsCount)
            .toClientFormattedText();

        final LinkBuilder linkBuilder = LinkBuilder.createLocalizableLink(
          appealsLink, UimConst.gkIntegratedCaseAppealListPage);

        linkBuilder.addParameter(CuramConst.gkPageParameterCaseID,
          String.valueOf(caseIDKey.caseID));

        final LocalisableString appealsTitle =
          new LocalisableString(BPOCASETAB.INF_APPEALS_TITLE);

        linkBuilder.addLinkTitle(appealsTitle);
        linksPanelBuilder.addLinkEntry(imageBuilder, linkBuilder);

      }
    }

    final ContentPanelBuilder caseOwnerPanelBuilder =
      ContentPanelBuilder.createPanel(CuramConst.gkICCaseOwnerPanel);

    final ImageBuilder ownerImageBuilder = ImageBuilder
      .createImage(CuramConst.gkIconCaseOwner, CuramConst.gkEmpty);

    ownerImageBuilder.setImageResource(CuramConst.gkRendererImages);
    final LocalisableString altTextLocalizedMessage =
      new LocalisableString(BPOCASETAB.INF_CASE_OWNER);

    ownerImageBuilder
      .setImageAltText(altTextLocalizedMessage.toClientFormattedText());

    caseOwnerPanelBuilder.addImageItem(ownerImageBuilder,
      CuramConst.gkICCaseOwnerIcon);

    final LinkBuilder linkBuilder =
      LinkBuilder.createLink(caseOwnerDetails.orgObjectReferenceName,
        CuramConst.gkCaseOwnerHomePage);

    linkBuilder.openAsModal();
    linkBuilder.addParameter(CuramConst.gkPageParameterUserName,
      String.valueOf(caseOwnerDetails.userName));
    linkBuilder.addParameter(CuramConst.gkPageParameterOrgObjectReference,
      String.valueOf(caseOwnerDetails.orgObjectReference));
    linkBuilder.addParameter(CuramConst.gkPageParameterOrgObjectType,
      String.valueOf(caseOwnerDetails.orgObjectType));

    final LocalisableString caseOwnerTitle =
      new LocalisableString(BPOCASETAB.INF_CASEOWNER_TITLE);

    linkBuilder.addLinkTitle(caseOwnerTitle);

    caseOwnerPanelBuilder.addLinkItem(linkBuilder, CuramConst.gkICCaseOwner);

    linksPanelBuilder.addContentPanel(caseOwnerPanelBuilder);

    return linksPanelBuilder.getLinksPanel();
  }

  // ___________________________________________________________________________
  /**
   * Read the tab details links for an Integrated case.
   *
   * @param details Integrated Case details.
   * @param issueList Issue details list
   * @param caseOwnerDetails Integrated case owner details
   *
   * @return ContentPanelBuilder.
   */
  protected ContentPanelBuilder getIntegratedCasePreviewLinks(
    final IntegratedCaseTabDtls details,
    final IssueRollOverDetailList issueList,
    final CaseOwnerDetails caseOwnerDetails)
    throws AppException, InformationalException {

    // Create a links panel with four links per column
    final LinksPanelBuilder linksPanelBuilder =
      LinksPanelBuilder.createLinksPanel(4);

    final ContentPanelBuilder caseStatus =
      ContentPanelBuilder.createPanel(CuramConst.gkICCaseStatus);

    final String status = new LocalisableString(BPOCASETAB.INF_STATUS)
      .arg(CodeTable.getOneItem(CASESTATUS.TABLENAME, details.statusCode))
      .toClientFormattedText();

    caseStatus.addlocalisableStringItem(status, CuramConst.gkICStatus);

    linksPanelBuilder.addContentPanel(caseStatus);

    // Add verification link

    ProductDeliveryTab.addVerificationLinkItem(linksPanelBuilder,
      details.verificationsCount, details.caseID, false);

    // Add evidence in edit link
    ProductDeliveryTab.addEvidenceInEditLinkItem(linksPanelBuilder,
      details.inEditEvidenceCount, details.caseID, false);

    // Add issues link
    if (issueList.dtls.size() > CuramConst.gkZero) {
      ProductDeliveryTab.addIssueLinkItem(linksPanelBuilder, issueList,
        details.caseID, false);
    }

    if (Configuration.getBooleanProperty(EnvVars.ENV_APPEALS_ISINSTALLED)) {
      // Add appeals link
      final CaseIDKey caseIDKey = new CaseIDKey();

      caseIDKey.caseID = details.caseID;

      final long appealsCount = getAppealsCount(caseIDKey);

      if (appealsCount > CuramConst.gkZero) {

        ProductDeliveryTab.addAppealLinkItem(linksPanelBuilder, appealsCount,
          details.caseID);
      }
    }

    final ContentPanelBuilder caseOwnerPanelBuilder =
      ContentPanelBuilder.createPanel(CuramConst.gkICCaseOwnerPanel);

    final ImageBuilder ownerImageBuilder = ImageBuilder
      .createImage(CuramConst.gkIconCaseOwner, CuramConst.gkEmpty);

    ownerImageBuilder.setImageResource(CuramConst.gkRendererImages);
    final LocalisableString altTextLocalizedMessage =
      new LocalisableString(BPOCASETAB.INF_CASE_OWNER);

    ownerImageBuilder
      .setImageAltText(altTextLocalizedMessage.toClientFormattedText());

    caseOwnerPanelBuilder.addImageItem(ownerImageBuilder,
      CuramConst.gkICCaseOwnerIcon);

    final LinkBuilder linkBuilder =
      LinkBuilder.createLink(caseOwnerDetails.orgObjectReferenceName,
        CuramConst.gkCaseOwnerHomePage);

    linkBuilder.openAsModal();
    linkBuilder.addParameter(CuramConst.gkPageParameterUserName,
      String.valueOf(caseOwnerDetails.userName));
    linkBuilder.addParameter(CuramConst.gkPageParameterOrgObjectReference,
      String.valueOf(caseOwnerDetails.orgObjectReference));
    linkBuilder.addParameter(CuramConst.gkPageParameterOrgObjectType,
      String.valueOf(caseOwnerDetails.orgObjectType));

    caseOwnerPanelBuilder.addLinkItem(linkBuilder, CuramConst.gkICCaseOwner);

    linksPanelBuilder.addContentPanel(caseOwnerPanelBuilder);

    return linksPanelBuilder.getLinksPanel();
  }

  @Override
  public IntegratedCaseTabDetail readIntegratedCasePreviewDetail(
    final CaseIDKey key) throws AppException, InformationalException {

    final IntegratedCaseTabDetail integratedCaseTabDetail =
      new IntegratedCaseTabDetail();

    final IntegratedCaseTabKey integratedCaseTabKey =
      new IntegratedCaseTabKey();

    integratedCaseTabKey.caseID = key.caseID;

    final IntegratedCaseTabDtls integratedCaseTabDtls = CaseHeaderFactory
      .newInstance().readIntegratedCaseTabDetails(integratedCaseTabKey);

    integratedCaseTabDetail.caseReference =
      integratedCaseTabDtls.caseReference;

    // Read Integrated cases' owner name
    final CaseUserRole caseUserRoleObj = CaseUserRoleFactory.newInstance();
    final OrgObjectLinkKey orgObjectLinkKey = new OrgObjectLinkKey();

    orgObjectLinkKey.orgObjectLinkID =
      integratedCaseTabDtls.ownerOrgObjectLinkID;

    final CaseOwnerDetails caseOwnerDetails =
      caseUserRoleObj.readOwnerName(orgObjectLinkKey);

    if (caseOwnerDetails.orgObjectType.equals(ORGOBJECTTYPE.USER)) {

      final TabDetailFormatter tabDetailFormatterObj =
        TabDetailFormatterFactory.newInstance();
      final UsersKey usersKey = new UsersKey();

      usersKey.userName = caseOwnerDetails.userName;

      caseOwnerDetails.orgObjectReferenceName =
        tabDetailFormatterObj.formatUserFullName(usersKey).fullName;

    } else {
      caseOwnerDetails.orgObjectReferenceName =
        caseOwnerDetails.orgObjectReferenceName;
    }

    IssueRollOverDetailList issueDetailsList = new IssueRollOverDetailList();

    // if (integratedCaseTabDtls.issuesCount > CuramConst.gkZero) {

    // Read issue details list
    final IssueRollOverDetailKey issueRollOverDetailKey =
      new IssueRollOverDetailKey();

    issueRollOverDetailKey.cancelledCaseStatus = CASESTATUS.CANCELED;
    issueRollOverDetailKey.closedCaseStatus = CASESTATUS.CLOSED;
    issueRollOverDetailKey.relatedCaseID = integratedCaseTabDtls.caseID;
    issueRollOverDetailKey.resolutionCancelledCode =
      RESOLUTIONSTATUSCODE.CANCELED;

    issueDetailsList = IssueDeliveryFactory.newInstance()
      .searchActiveByRelatedCaseID(issueRollOverDetailKey);
    // }

    final SearchForIntegratedCaseTabDetailKey searchForIntegratedCaseTabDetailKey =
      new SearchForIntegratedCaseTabDetailKey();

    searchForIntegratedCaseTabDetailKey.caseID = key.caseID;

    final CaseParticipantTabDetailList caseParticipantTabDetailList =
      CaseParticipantRoleFactory.newInstance()
        .searchForIntegratedCaseTabDetail(
          searchForIntegratedCaseTabDetailKey);

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

    caseHeaderKey.caseID = key.caseID;

    final ReadParticipantRoleIDDetails readParticipantRoleIDDetails =
      CaseHeaderFactory.newInstance().readParticipantRoleID(caseHeaderKey);

    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();
    // Concern Role Variables
    final ParticipantSecurityCheckKey participantKey =
      new ParticipantSecurityCheckKey();

    // perform concern role sensitivity check
    participantKey.participantID = readParticipantRoleIDDetails.concernRoleID;

    participantKey.type = LOCATIONACCESSTYPE.READ;

    final DataBasedSecurityResult dataBasedSecurityResult =
      dataBasedSecurity.checkParticipantSecurity(participantKey);

    if (!dataBasedSecurityResult.result) {
      if (dataBasedSecurityResult.readOnly) {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);
      } else if (dataBasedSecurityResult.restricted) {
        throw new AppException(GENERALCONCERN.ERR_CONCERNROLE_FV_SENSITIVE);
      } else {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
      }
    }

    for (int i = 0; i < caseParticipantTabDetailList.dtls.size(); i++) {

      if (caseParticipantTabDetailList.dtls.item(
        i).concernRoleID == readParticipantRoleIDDetails.concernRoleID) {

        final curam.core.sl.entity.struct.CaseParticipantTabDetail primaryCaseParticipant =
          caseParticipantTabDetailList.dtls.item(i);

        // Remove primary case participant from list
        caseParticipantTabDetailList.dtls.remove(i);

        // Set the relationship type to be 'Self'
        primaryCaseParticipant.relationshipType = RELATIONSHIPTYPECODE.SELF;

        // Add to them to the start of the list
        caseParticipantTabDetailList.dtls.add(0, primaryCaseParticipant);
      }
    }

    // Create a container content panel
    String containerCSSClass =
      CuramConst.gkContainerPanelIC + CuramConst.gkSpace;
    if (caseParticipantTabDetailList.dtls.size() > CuramConst.gkOne) {
      containerCSSClass += CuramConst.gkMultiPerson;
    } else {
      containerCSSClass += CuramConst.gkSinglePerson;
    }
    final ContentPanelBuilder containerPanel =
      ContentPanelBuilder.createPanel(containerCSSClass);

    if (caseParticipantTabDetailList.dtls.size() > CuramConst.gkOne) {
      // Use Stack Container
      // Build left panel
      final StackContainerBuilder stackContainerBuilder =
        getCaseMemberThumbnailDetails(caseParticipantTabDetailList, 3);

      // Add the left panel
      containerPanel.addWidgetItem(stackContainerBuilder, CuramConst.gkStyle,
        CuramConst.gkStyleStackContainer);
    } else {

      final CaseMemberTabDetails caseMemberTabDetails =
        new CaseMemberTabDetails();

      caseMemberTabDetails.assign(caseParticipantTabDetailList.dtls.item(0));

      // Build left panel
      // BEGIN,195855 JJ
      final boolean displayRoles = Configuration
        .getBooleanProperty(EnvVars.ENV_DISPLAY_PARTICIPANT_ROLE);

      final ContentPanelBuilder imagePanel = CaseTabDetailsHelper
        .getCaseMemberThumbnailDetails(caseMemberTabDetails, displayRoles);
      // END, 195855

      // Add the left panel to the container panel
      containerPanel.addWidgetItem(imagePanel, CuramConst.gkStyle,
        CuramConst.gkContentPanel, CuramConst.gkCaseParticipantPanelSingle);
    }

    // Build right panel
    final ContentPanelBuilder linksPanel = getIntegratedCasePreviewLinks(
      integratedCaseTabDtls, issueDetailsList, caseOwnerDetails);

    // Add the right panel to the border container
    containerPanel.addWidgetItem(linksPanel, CuramConst.gkStyle,
      CuramConst.gkContentPanel, CuramConst.gkICLinksPanel);

    integratedCaseTabDetail.xmlPanelData = containerPanel.toString();

    final CaseKey caseKey = new CaseKey();

    caseKey.caseID = key.caseID;
    final CaseConcernRoleName caseConcernRoleName =
      CaseHeaderFactory.newInstance().readCaseConcernRoleName(caseKey);

    // Format the integrated tab description
    integratedCaseTabDetail.caseType = CodeTable.getOneItem(
      CASECATTYPECODE.TABLENAME, integratedCaseTabDtls.integratedCaseType,
      TransactionInfo.getProgramLocale());
    integratedCaseTabDetail.personName = caseConcernRoleName.concernRoleName;

    return integratedCaseTabDetail;
  }

  // ___________________________________________________________________________
  // BEGIN, 194969 JJ
  /**
   * Create and populate case member list row with member details.
   *
   * @param memberListBuilder Case member list builder to create and populate
   * row
   * @param caseMemberTabDetails Case member details
   * @param rowNo Row number to insert into list
   *
   * @return ContentPanelBuilder.
   */
  protected void createMemberRow(final ListBuilder memberListBuilder,
    final CaseMemberTabDetails caseMemberTabDetails, final int rowNo)
    throws AppException, InformationalException {

    final boolean displayRoles =
      Configuration.getBooleanProperty(EnvVars.ENV_DISPLAY_PARTICIPANT_ROLE,
        EnvVars.ENV_DISPLAY_PARTICIPANT_ROLE_DEFAULT);

    final TabDetailFormatter tabDetailFormatterObj =
      TabDetailFormatterFactory.newInstance();
    final FormatPersonNameKey formatPersonNameKey = new FormatPersonNameKey();

    formatPersonNameKey.wrapNameInd = false;

    // create member list row
    memberListBuilder.addRow();

    final LinkBuilder ccvLinkBuilder = LinkBuilder
      .createCCVLink(CuramConst.gkEmpty, caseMemberTabDetails.concernRoleID);
    final LocalisableString altText =
      new LocalisableString(IMAGEPANELBUILDER.INF_CCV_VIEWER);

    ccvLinkBuilder.addImageWithAltTextMessage(CuramConst.gkIconCCV,
      altText.toClientFormattedText());
    ccvLinkBuilder.setTextResource(CuramConst.gkRendererImages);

    final RendererConfig linkRendererConfig =
      new RendererConfig(RendererConfigType.STYLE, CuramConst.gkLink);

    memberListBuilder.addEntry(1, rowNo, ccvLinkBuilder, linkRendererConfig);

    // Add Persons Name
    final String contentPanelID = CuramConst.gkCaseMemberListContenPanelID
      + String.valueOf(caseMemberTabDetails.concernRoleID);

    final ContentPanelBuilder nameContentPanelBuilder =
      ContentPanelBuilder.createPanelWithID(contentPanelID);

    formatPersonNameKey.concernRoleID = caseMemberTabDetails.concernRoleID;

    final LinkBuilder memberLinkBuilder = LinkBuilder.createLink(
      tabDetailFormatterObj.formatPersonName(formatPersonNameKey).name,
      CuramConst.gkParticipantHomePage,
      memberListBuilder.getWidgetDocument());

    memberLinkBuilder.addParameter(CuramConst.gkPageParameterConcernRoleID,
      String.valueOf(caseMemberTabDetails.concernRoleID));

    nameContentPanelBuilder.addLinkItem(memberLinkBuilder);

    final ContactTabDetails contactTabDetails = new ContactTabDetails();

    contactTabDetails.assign(caseMemberTabDetails);

    final RendererConfig contentPanelRendererConfig =
      new RendererConfig(RendererConfigType.STYLE, CuramConst.gkContentPanel);

    memberListBuilder.addEntry(2, rowNo, nameContentPanelBuilder,
      contentPanelRendererConfig);

    final ContentPanelBuilder personNamePanel =
      ContentPanelBuilder.createPanel(CuramConst.gkPersonNameIcons);

    // Add special caution details
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = caseMemberTabDetails.concernRoleID;

    personNamePanel.addWidgetItem(
      curam.core.sl.impl.TabDetailsHelper
        .getSpecialCautionDetails(concernRoleKey),
      CuramConst.gkStyle, CuramConst.gkContentPanel, CuramConst.gkListIcon);

    final TabDetailsHelper tabDetailsHelperObj =
      TabDetailsHelperFactory.newInstance();

    // read incident and role details
    final IncidentAndRoleTabDetailsList incidentList =
      tabDetailsHelperObj.readIncidentTabDetails(concernRoleKey);

    // read investigation and role details
    final InvestigationAndRoleTabDetailsList investigationList =
      tabDetailsHelperObj.readInvestigationTabDetails(concernRoleKey);

    if (incidentList.dtls.size() > CuramConst.gkZero) {

      personNamePanel.addWidgetItem(
        CaseTabDetailsHelper.getMemberIncidentDetails(concernRoleKey,
          incidentList),
        CuramConst.gkStyle, CuramConst.gkContentPanel, CuramConst.gkListIcon);
    }

    if (investigationList.dtls.size() > CuramConst.gkZero) {

      personNamePanel.addWidgetItem(
        CaseTabDetailsHelper.getMemberInvestigationDetails(concernRoleKey,
          investigationList),
        CuramConst.gkStyle, CuramConst.gkContentPanel, CuramConst.gkListIcon);
    }

    memberListBuilder.addEntry(3, rowNo, personNamePanel,
      contentPanelRendererConfig);

    // Add relationship
    // if display participant's role configuration property is YES, then only
    // add relationship row
    if (displayRoles) {
      if (caseMemberTabDetails.relationshipType
        .equals(RELATIONSHIPTYPECODE.SELF)) {
        memberListBuilder.addEntry(4, rowNo,
          new LocalisableString(BPOCASETAB.INF_PRIMARY_MEMBER_LABEL));
      } else {
        final CodeTableItemEntry relationshipType = DocBuilderHelperFactory
          .getCodeTableItemEntry(caseMemberTabDetails.relationshipType,
            CuramConst.gkDomainRelationshipType);

        memberListBuilder.addEntry(4, rowNo, relationshipType);
      }
    } else {
      memberListBuilder.addEntry(4, rowNo, "");
    }
    // END, 194969

    final PersonAgeDetails personAgeDetails = new PersonAgeDetails();

    // Add age
    personAgeDetails.dateOfCalculation = Date.getCurrentDate();
    personAgeDetails.personID = caseMemberTabDetails.concernRoleID;
    personAgeDetails.indDisplayDateOfBirth = false;

    final TabDetailFormatterInterface tabDetailFormatterInterface =
      (TabDetailFormatterInterface) TabDetailFormatterFactory.newInstance();

    final LocalisableString ageString = tabDetailFormatterInterface
      .formatAge(personAgeDetails).localisableAgeString;

    memberListBuilder.addEntry(5, rowNo, ageString);

  }

  // ___________________________________________________________________________
  /**
   * Access the delegator to call the appropriate method in the Appeals
   * component
   * if it is installed.
   *
   * @param caseIDKey
   * @return The number of Appeals on cases associated with this case.
   *
   * @throws DatabaseException
   * @throws InformationalException
   * @throws AppRuntimeException
   * @throws AppException
   */
  protected long getAppealsCount(final CaseIDKey caseIDKey)
    throws DatabaseException, AppRuntimeException, AppException,
    InformationalException {

    long numAppeals = 0;

    // Access the delegator to call the appropriate method in the Appeals
    // component
    // if it is installed.
    Class cls = null;
    final String className =
      ReflectionConst.coreToAppealDelegateWrapperClassName;

    final String methodName =
      ReflectionConst.countRelatedCasesActiveAppealsByCaseIDMethodName;

    try {
      // Load the class for AppealCase
      cls = Class.forName(className);

      // Create a method for the static newInstance constructor
      final Method constructorMethod =
        cls.getMethod(ReflectionConst.kNewInstance);
      final Object classObj = constructorMethod.invoke(cls);

      // Set the arguments to pass through
      final Object[] arguments = new Object[]{caseIDKey };

      // Set the passed class types to the method
      final Class[] parameterTypes = new Class[]{CaseIDKey.class };

      final Method method =
        classObj.getClass().getMethod(methodName, parameterTypes);

      // Now invoke the method
      numAppeals = (Long) method.invoke(classObj, arguments);

    } catch (final IllegalAccessException e) {// ignore - biz methods MUST be
      // public
    } catch (final ClassNotFoundException e) {

      final AppException ae =
        new AppException(BPOREFLECTION.ERR_REFLECTION_CLASS_NOT_FOUND);

      ae.arg(className);
      ae.arg(getClass().toString());
      ae.arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME, CASETYPECODE.APPEAL,
        TransactionInfo.getProgramLocale()));

      throw new RuntimeException(ae);

    } catch (final NoSuchMethodException e) {

      final AppException ae =
        new AppException(BPOREFLECTION.ERR_REFLECTION_NO_SUCH_METHOD);

      ae.arg(className);

      ae.arg(getClass().toString() + GeneralConst.gDot + methodName);
      ae.arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME, CASETYPECODE.APPEAL,
        TransactionInfo.getProgramLocale()));

      throw new RuntimeException(ae);

    } catch (final IllegalArgumentException e) {

      final AppException ae =
        new AppException(BPOREFLECTION.ERR_ILLEGAL_FUNCTION_ARGUMENTS);

      ae.arg(getClass().toString() + GeneralConst.gDot + methodName);
      ae.arg(className);
      ae.arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME, CASETYPECODE.APPEAL,
        TransactionInfo.getProgramLocale()));

      throw new RuntimeException(ae);

    } catch (final InvocationTargetException e) {

      final AppException exc =
        new AppException(BPOREFLECTION.ERR_BPO_MESSAGE_RETURNED);

      exc.arg(e.getTargetException().getMessage());
      throw exc;
    }

    return numAppeals;
  }

  /**
   * This method will create the CVV link with warning Icon and required
   * parameters.
   *
   * @param linksPanelBuilder
   * @param verificationCount
   * @param caseID
   * @param PDCaseInd
   * @throws AppException
   * @throws InformationalException
   */
  public void addUrgentLinkItem(final LinksPanelBuilder linksPanelBuilder,
    final long caseID) throws AppException, InformationalException {

    final LocalisableString altTextTooltipMessage = new LocalisableString(
      BDMAPPLICATIONCASECONTEXTPANEL.INF_URGENTFLAG_TOOLTIP);
    final ImageBuilder urgentFlagIcon =
      ImageBuilder.createImage("Warning.Icon", "");

    urgentFlagIcon.setImageResource("cefwidgets.Image");
    urgentFlagIcon
      .setImageAltText(altTextTooltipMessage.toClientFormattedText());

    final LocalisableString urgentFlagString =
      new LocalisableString(BDMAPPLICATIONCASECONTEXTPANEL.INFO_FLAG_URGENT);

    final CaseIDDetails caseIDDetails = new CaseIDDetails();
    caseIDDetails.caseID = caseID;
    final BDMCaseUrgentFlagDetailsList urgentFlagListDetails =
      bmdMaintainCaseUrgentFlag.listCurrentUrgentFlags(caseIDDetails);

    urgentFlagString.arg(urgentFlagListDetails.dtls.size());

    final LinkBuilder urgentFlagLinkbuilder = LinkBuilder
      .createLocalizableLink(urgentFlagString.toClientFormattedText(),
        "BDM_IC_UrgentFlag_listForCasePage.do");

    urgentFlagLinkbuilder.addParameter("caseID", String.valueOf(caseID));

    linksPanelBuilder.addLinkEntry(urgentFlagIcon, urgentFlagLinkbuilder,
      "icon-warning");

  }

}
