/**
 *
 */
package curam.ca.gc.bdm.facade.productdelivery.impl;

/**
 * @author sivakumar.kalyanasun
 * @version 1.0 Task 19383 - Urgent Flag for PDC Context Panel This process
 * class
 * provides the functionality for the PDC Case section tab
 * details service layer.
 *
 */

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetailsList;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMCaseID;
import curam.ca.gc.bdm.facade.bdmmanualoverpayment.fact.BDMManualOverPaymentFactory;
import curam.ca.gc.bdm.facade.bdmmanualoverpayment.struct.BDMSearchManualOPByBenefitCaseIDDetailsList;
import curam.ca.gc.bdm.message.BDMRFRSISSUE;
import curam.ca.gc.bdm.sl.bdmcaseurgentflag.impl.BDMMaintainCaseUrgentFlag;
import curam.cefwidgets.docbuilder.impl.ContentPanelBuilder;
import curam.cefwidgets.docbuilder.impl.ImageBuilder;
import curam.cefwidgets.docbuilder.impl.LinkBuilder;
import curam.cefwidgets.docbuilder.impl.LinksPanelBuilder;
import curam.cefwidgets.docbuilder.impl.ListBuilder;
import curam.cefwidgets.docbuilder.impl.TooltipBuilder;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.FININSTRUCTIONSTATUS;
import curam.codetable.ISSUECONFIGURATIONTYPE;
import curam.codetable.LOCATIONACCESSTYPE;
import curam.codetable.ORGOBJECTTYPE;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.PRODUCTNAME;
import curam.codetable.REASSESSMENTRESULT;
import curam.codetable.RESOLUTIONSTATUSCODE;
import curam.commonintake.message.BDMAPPLICATIONCASECONTEXTPANEL;
import curam.core.facade.struct.CaseIDDetails;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.fact.ProductDeliveryFactory;
import curam.core.fact.SimulatePaymentFactory;
import curam.core.impl.DataBasedSecurity;
import curam.core.impl.EnvVars;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.intf.CaseHeader;
import curam.core.intf.ProductDelivery;
import curam.core.sl.entity.fact.IssueDeliveryFactory;
import curam.core.sl.entity.struct.IssueRollOverDetailKey;
import curam.core.sl.entity.struct.IssueRollOverDetailList;
import curam.core.sl.entity.struct.OrgObjectLinkKey;
import curam.core.sl.fact.CaseTabDetailsHelperFactory;
import curam.core.sl.fact.CaseUserRoleFactory;
import curam.core.sl.fact.TabDetailFormatterFactory;
import curam.core.sl.impl.CaseTabDetailsHelper;
import curam.core.sl.infrastructure.paymentcorrection.entity.fact.PaymentCorrectionEvidenceFactory;
import curam.core.sl.infrastructure.paymentcorrection.entity.intf.PaymentCorrectionEvidence;
import curam.core.sl.infrastructure.paymentcorrection.entity.struct.CountByRelatedCaseAndTypeKey;
import curam.core.sl.infrastructure.paymentcorrection.impl.PaymentCorrection;
import curam.core.sl.infrastructure.paymentcorrection.impl.PaymentCorrectionEvidenceDAO;
import curam.core.sl.intf.CaseUserRole;
import curam.core.sl.intf.TabDetailFormatter;
import curam.core.sl.struct.AmountDetail;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.CaseMemberTabDetails;
import curam.core.sl.struct.CaseNameDetails;
import curam.core.sl.struct.CaseOwnerDetails;
import curam.core.sl.struct.ParticipantSecurityCheckKey;
import curam.core.sl.struct.ProductDeliveryPaymentTabDetails;
import curam.core.sl.struct.ProductDeliveryTabDetails;
import curam.core.struct.AmountEffectiveDate;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseID;
import curam.core.struct.CaseKey;
import curam.core.struct.DataBasedSecurityResult;
import curam.core.struct.DateStruct;
import curam.core.struct.ILICaseStatusAndTypeKey;
import curam.core.struct.IntegratedCaseKey;
import curam.core.struct.IntegratedCaseType;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.ProductDeliveryTabDtls;
import curam.core.struct.ProductDeliveryTabKey;
import curam.core.struct.ProductDeliveryTypeDetails;
import curam.core.struct.SimulateInd;
import curam.core.struct.SimulatePaymentResult;
import curam.core.struct.UsersKey;
import curam.message.BPOCASETAB;
import curam.message.BPOREFLECTION;
import curam.message.GENERALCASE;
import curam.message.GENERALCONCERN;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.DatabaseException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.CodeTableItemIdentifier;
import curam.util.type.Date;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BDMProductDeliveryTabHelper
  extends curam.core.sl.base.ProductDeliveryTab {

  @Inject
  protected PaymentCorrectionEvidenceDAO paymentCorrectionEvidenceDAO;

  @Inject
  protected PaymentCorrection paymentCorrection;

  protected static final int numProductDeliveryColumnLinks = 4;

  @Inject
  private BDMMaintainCaseUrgentFlag bmdMaintainCaseUrgentFlag;

  public BDMProductDeliveryTabHelper() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public ProductDeliveryTabDetails readProductDeliveryTabDetails(
    final CaseIDKey key) throws AppException, InformationalException {

    final ProductDeliveryTabDetails productDeliveryTabDetails =
      new ProductDeliveryTabDetails();

    final ProductDeliveryTabKey productDeliveryTabKey =
      new ProductDeliveryTabKey();

    productDeliveryTabKey.caseID = key.caseID;

    final ProductDeliveryTabDtls productDeliveryTabDtls =
      ProductDeliveryFactory.newInstance()
        .readProductDeliveryTabDetails(productDeliveryTabKey);

    int paymentCorrectionOverpayments = 0;
    int paymentCorrectionUnderpayments = 0;

    final PaymentCorrectionEvidence paymentCorrectionEvidenceObj =
      PaymentCorrectionEvidenceFactory.newInstance();

    final CountByRelatedCaseAndTypeKey countKey =
      new CountByRelatedCaseAndTypeKey();

    countKey.relatedCaseID = key.caseID;
    countKey.reassessmentResultType = REASSESSMENTRESULT.OVERPAYMENT;

    paymentCorrectionOverpayments = (int) paymentCorrectionEvidenceObj
      .countByRelatedCaseAndType(countKey).count;

    countKey.reassessmentResultType = REASSESSMENTRESULT.UNDERPAYMENT;

    paymentCorrectionUnderpayments = (int) paymentCorrectionEvidenceObj
      .countByRelatedCaseAndType(countKey).count;

    productDeliveryTabDtls.overpaymentCasesCount +=
      paymentCorrectionOverpayments;

    productDeliveryTabDtls.underpaymentCasesCount +=
      paymentCorrectionUnderpayments;

    productDeliveryTabDtls.underpaymentsCount +=
      paymentCorrectionUnderpayments;

    productDeliveryTabDtls.overpaymentsCount += paymentCorrectionOverpayments;

    final BDMCaseID bdmCaseID = new BDMCaseID();
    bdmCaseID.caseID = key.caseID;
    final BDMSearchManualOPByBenefitCaseIDDetailsList manualOverPaymentList =
      BDMManualOverPaymentFactory.newInstance()
        .searchManualOPByBenefitCaseID(bdmCaseID);
    productDeliveryTabDtls.overpaymentsCount +=
      manualOverPaymentList.dtls.size();

    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();

    final ParticipantSecurityCheckKey participantKey =
      new ParticipantSecurityCheckKey();

    participantKey.participantID = productDeliveryTabDtls.concernRoleID;

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
    } else {
      final CaseUserRole caseUserRoleObj = CaseUserRoleFactory.newInstance();
      final OrgObjectLinkKey orgObjectLinkKey = new OrgObjectLinkKey();

      orgObjectLinkKey.orgObjectLinkID =
        productDeliveryTabDtls.ownerOrgObjectLinkID;

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

      final CaseMemberTabDetails caseMemberTabDetails =
        new CaseMemberTabDetails();

      caseMemberTabDetails.assign(productDeliveryTabDtls);

      IssueRollOverDetailList issueDetailsList =
        new IssueRollOverDetailList();

      if (productDeliveryTabDtls.issuesCount > 0) {

        final IssueRollOverDetailKey issueRollOverDetailKey =
          new IssueRollOverDetailKey();

        issueRollOverDetailKey.cancelledCaseStatus = CASESTATUS.CANCELED;
        issueRollOverDetailKey.closedCaseStatus = CASESTATUS.CLOSED;
        issueRollOverDetailKey.relatedCaseID = productDeliveryTabDtls.caseID;
        issueRollOverDetailKey.resolutionCancelledCode =
          RESOLUTIONSTATUSCODE.CANCELED;

        issueDetailsList = IssueDeliveryFactory.newInstance()
          .searchActiveByRelatedCaseID(issueRollOverDetailKey);

      }

      SimulatePaymentResult simulatePaymentResult =
        new SimulatePaymentResult();

      new AmountEffectiveDate();
      final ProductDeliveryPaymentTabDetails productDeliveryPaymentTabDetails =
        new ProductDeliveryPaymentTabDetails();

      if (productDeliveryTabDtls.paymentILICount > 0) {

        final CaseID caseID = new CaseID();

        caseID.caseID = key.caseID;

        final DateStruct simulationDate = new DateStruct();

        final SimulateInd simulateInd = new SimulateInd();

        simulateInd.simulateInd = false;

        try {
          simulatePaymentResult = SimulatePaymentFactory.newInstance()
            .simulatePayment(caseID, simulationDate, simulateInd);

        } catch (final AppException var31) {
          ;
        }

        final ILICaseStatusAndTypeKey iLICaseStatusAndTypeKey =
          new ILICaseStatusAndTypeKey();

        iLICaseStatusAndTypeKey.caseID = key.caseID;
        iLICaseStatusAndTypeKey.statusCode = FININSTRUCTIONSTATUS.PROCESSED;

        final AmountEffectiveDate lastPaymentDetails =
          InstructionLineItemFactory.newInstance()
            .readLastPaymentAndEffectiveDate(iLICaseStatusAndTypeKey);

        productDeliveryPaymentTabDetails.nextPaymentAmount =
          simulatePaymentResult.totalPayment;

        productDeliveryPaymentTabDetails.nextPaymentDate =
          simulatePaymentResult.enteredDate;

        productDeliveryPaymentTabDetails.lastPaymentAmount =
          lastPaymentDetails.amount;

        productDeliveryPaymentTabDetails.lastPaymentDate =
          lastPaymentDetails.effectiveDate;

      }

      final ContentPanelBuilder containerPanel =
        ContentPanelBuilder.createPanel("pd-container-panel");

      final ContentPanelBuilder imagePanel = CaseTabDetailsHelper
        .getCaseMemberThumbnailDetails(caseMemberTabDetails, false);

      containerPanel.addWidgetItem(imagePanel, "style", "content-panel",
        "content-panel-detail case-participant-panel");

      final ContentPanelBuilder detailsPanel =
        this.getProductDeliveryCaseDetails(productDeliveryTabDtls,
          productDeliveryPaymentTabDetails);

      // RFR Changes : Add RFR message on the context panel
      if (BDMUtil.countAppealCasesByParticipantID(
        productDeliveryTabDtls.concernRoleID) > 0) {
        final LocalisableString apString =
          new LocalisableString(BDMRFRSISSUE.INFO_BDM_RFR_CONTEXT_MESSAGE);
        detailsPanel.addlocalisableStringItem(
          apString.toClientFormattedText(), "content-bdm-rfr-info-dtls");
      }

      final ContentPanelBuilder linksPanel = this.getProductDeliveryCaseLinks(
        productDeliveryTabDtls, issueDetailsList, caseOwnerDetails);

      int numLinks = 2;

      if (productDeliveryTabDtls.underpaymentsCount > 0) {
        ++numLinks;
      }
      if (productDeliveryTabDtls.overpaymentsCount > 0) {
        ++numLinks;
      }
      if (productDeliveryTabDtls.issuesCount > 0) {
        ++numLinks;
      }

      if (Configuration.getBooleanProperty(EnvVars.ENV_APPEALS_ISINSTALLED)) {

        final CaseIDKey caseIDKey = new CaseIDKey();

        caseIDKey.caseID = key.caseID;

        String methodName = "countActiveAppealsByCaseID";

        final long appealsCount =
          this.getCountWithReflection(methodName, caseIDKey);

        if (appealsCount > 0L) {
          ++numLinks;

        }

        methodName = "countActiveLegalActionsByCaseID";

        final long legalActionsCount =
          this.getCountWithReflection(methodName, caseIDKey);

        if (legalActionsCount > 0L) {
          ++numLinks;
        }
      }

      if (numLinks <= 4) {

        containerPanel.addWidgetItem(detailsPanel, "style", "content-panel",
          "content-panel-detail pd-content-panel-onecol");

        containerPanel.addWidgetItem(linksPanel, "style", "content-panel",
          "content-panel-detail pd-links-panel-onecol");

      } else {
        containerPanel.addWidgetItem(detailsPanel, "style", "content-panel",
          "content-panel-detail pd-content-panel");

        containerPanel.addWidgetItem(linksPanel, "style", "content-panel",
          "content-panel-detail pd-links-panel");

      }

      productDeliveryTabDetails.xmlPanelData = containerPanel.toString();

      productDeliveryTabDetails.personName =
        productDeliveryTabDtls.concernRoleName;

      productDeliveryTabDetails.productDeliveryRef =
        productDeliveryTabDtls.caseReference;

      productDeliveryTabDetails.productDeliveryName = CodeTable.getOneItem(
        PRODUCTNAME.TABLENAME, productDeliveryTabDtls.productName,
        TransactionInfo.getProgramLocale());

      return productDeliveryTabDetails;
    }
  }

  protected ContentPanelBuilder getProductDeliveryCaseDetails(
    final ProductDeliveryTabDtls details,
    final ProductDeliveryPaymentTabDetails paymentDetails)
    throws AppException, InformationalException {

    final ContentPanelBuilder contentPanelBuilder =
      ContentPanelBuilder.createPanel("pd-details");

    contentPanelBuilder.addRoundedCorners();

    final curam.core.sl.intf.CaseTabDetailsHelper caseTabDetailsHelperObj =
      CaseTabDetailsHelperFactory.newInstance();
    final CaseNameDetails caseNameDetails = new CaseNameDetails();

    caseNameDetails.assign(details);

    contentPanelBuilder.addlocalisableStringItem(
      caseTabDetailsHelperObj.formatCaseName(caseNameDetails).caseName,
      "pd-reference");

    contentPanelBuilder.addStringItem(caseNameDetails.caseReference,
      "pd-reference-id");
    String caseReviewAltText;
    LinkBuilder reviewLabel;
    LocalisableString notRecorded;
    try {
      final CaseHeader caseHeader = CaseHeaderFactory.newInstance();

      final CaseKey caseKey = new CaseKey();

      caseKey.caseID = details.caseID;

      final IntegratedCaseKey integratedCaseKey =
        caseHeader.readIntegratedCaseIDByCaseID(caseKey);

      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

      caseHeaderKey.caseID = integratedCaseKey.integratedCaseID;

      final CaseHeaderDtls caseHeaderDtls = caseHeader.read(caseHeaderKey);

      caseReviewAltText = new LocalisableString(BPOCASETAB.INF_CASE_NAME_REF)
        .arg(new CodeTableItemIdentifier(PRODUCTCATEGORY.TABLENAME,
          caseHeaderDtls.integratedCaseType))
        .arg(caseHeaderDtls.caseReference).toClientFormattedText();

      reviewLabel = LinkBuilder.createLocalizableLink(caseReviewAltText,
        "Case_resolveCaseHomePage.do",
        contentPanelBuilder.getWidgetDocument());

      reviewLabel.addParameter("caseID",
        String.valueOf(integratedCaseKey.integratedCaseID));

      notRecorded = new LocalisableString(BPOCASETAB.INF_RELATEDCASE_TITLE);

      reviewLabel.addLinkTitle(notRecorded);

      contentPanelBuilder.addLinkItem(reviewLabel, "pd-related-ic-case");

    } catch (final RecordNotFoundException var14) {
      ;
    }

    contentPanelBuilder
      .addlocalisableStringItem(new LocalisableString(BPOCASETAB.INF_STARTED)
        .arg(details.startDate).toClientFormattedText(), "started");

    if (!details.certifiedToDate.isZero()) {
      contentPanelBuilder
        .addlocalisableStringItem(
          new LocalisableString(BPOCASETAB.INF_CERTIFIED)
            .arg(details.certifiedToDate).toClientFormattedText(),
          "certified");

    } else {
      contentPanelBuilder.addlocalisableStringItem(
        new LocalisableString(BPOCASETAB.INF_CERTIFICATION_NOT_RECORDED)
          .toClientFormattedText(),
        "certified");

    }

    final ListBuilder paymentList = ListBuilder.createHorizontalList(2);

    paymentList.addRow();

    paymentList.addEntry(1, 1,
      new LocalisableString(BPOCASETAB.INF_LAST_PAYMENT_LABEL));

    AmountDetail amountDetail = new AmountDetail();

    amountDetail.amount = paymentDetails.lastPaymentAmount;

    final String lastPayment = TabDetailFormatterFactory.newInstance()
      .formatCurrencyAmount(amountDetail).amount;

    if (!paymentDetails.lastPaymentAmount.isZero()) {
      paymentList.addEntry(2, 1,
        new LocalisableString(BPOCASETAB.INF_LAST_PAYMENT).arg(lastPayment)
          .arg(paymentDetails.lastPaymentDate));

    }

    paymentList.addRow();
    paymentList.addEntry(1, 2,
      new LocalisableString(BPOCASETAB.INF_NEXT_PAYMENT_LABEL));

    if (!paymentDetails.nextPaymentAmount.isZero()) {

      amountDetail = new AmountDetail();
      amountDetail.amount = paymentDetails.nextPaymentAmount;

      final String nextPayment = TabDetailFormatterFactory.newInstance()
        .formatCurrencyAmount(amountDetail).amount;

      paymentList.addEntry(2, 2,
        new LocalisableString(BPOCASETAB.INF_NEXT_PAYMENT).arg(nextPayment)
          .arg(paymentDetails.nextPaymentDate));

    }

    if (!details.latestCaseDecisionDate.isZero()) {
      paymentList.addRow();
      paymentList.addEntry(1, 3,
        new LocalisableString(BPOCASETAB.INF_LATEST_DECISION_LABEL));

      paymentList.addEntry(2, 3, details.latestCaseDecisionDate);

    }

    contentPanelBuilder.addSingleListItem(paymentList, "pd-details-table");

    final ContentPanelBuilder contactContent =
      ContentPanelBuilder.createPanel("decision-content");

    final ImageBuilder reviewImageBuilder =
      ImageBuilder.createImage("IconScheduleBlue", "");

    reviewImageBuilder
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");
    caseReviewAltText = new LocalisableString(BPOCASETAB.INF_CASE_REVIEW)
      .toClientFormattedText();

    reviewImageBuilder.setImageAltText(caseReviewAltText);

    contactContent.addImageItem(reviewImageBuilder);
    LocalisableString reviewLabel1;
    if (!details.reviewScheduledStartDate.isZero()) {

      reviewLabel1 = null;

      if (details.reviewScheduledStartDate.before(Date.getCurrentDate())) {
        reviewLabel1 = new LocalisableString(
          BPOCASETAB.INF_REVIEW_DUE_FOR_COMPLETION_LABEL);

      } else {
        reviewLabel1 =
          new LocalisableString(BPOCASETAB.INF_NEXT_CASE_REVIEW_DATE_LABEL);

      }

      contactContent.addlocalisableStringItem(
        reviewLabel1.toClientFormattedText(), "next-review-label");

      contactContent.addDateItem(details.reviewScheduledStartDate,
        "next-review");

    } else {
      reviewLabel1 =
        new LocalisableString(BPOCASETAB.INF_NEXT_CASE_REVIEW_DATE_LABEL);

      contactContent.addlocalisableStringItem(
        reviewLabel1.toClientFormattedText(), "next-review-label");

      notRecorded = new LocalisableString(BPOCASETAB.INF_NOT_RECORDED);

      contactContent.addlocalisableStringItem(
        notRecorded.toClientFormattedText(), "next-review");

    }

    contentPanelBuilder.addWidgetItem(contactContent, "style",
      "content-panel");

    return contentPanelBuilder;
  }

  protected ContentPanelBuilder getProductDeliveryCaseLinks(
    final ProductDeliveryTabDtls details,
    final IssueRollOverDetailList issueList,
    final CaseOwnerDetails caseOwnerDetails)
    throws AppException, InformationalException {

    final LinksPanelBuilder linksPanelBuilder =
      LinksPanelBuilder.createLinksPanel(4);

    final ContentPanelBuilder caseStatus =
      ContentPanelBuilder.createPanel("pd-case-status");

    final String status = new LocalisableString(BPOCASETAB.INF_STATUS)
      .arg(CodeTable.getOneItem(CASESTATUS.TABLENAME, details.statusCode))
      .toClientFormattedText();

    caseStatus.addlocalisableStringItem(status, "pd-status");

    linksPanelBuilder.addContentPanel(caseStatus);

    if (details.verificationsCount > 0) {
      addVerificationLinkItem(linksPanelBuilder, details.verificationsCount,
        details.caseID, true);

    }

    if (details.inEditEvidenceCount > 0) {
      addEvidenceInEditLinkItem(linksPanelBuilder,
        details.inEditEvidenceCount, details.caseID, true);

    }

    addUnderpaymentLinkItem(linksPanelBuilder, details.underpaymentsCount,
      details.caseID);

    addOverpaymentLinkItem(linksPanelBuilder, details.overpaymentsCount,
      details.caseID);

    final CaseIDDetails caseIDDetails = new CaseIDDetails();
    caseIDDetails.caseID = details.caseID;
    final BDMCaseUrgentFlagDetailsList urgentFlagListDetails =
      bmdMaintainCaseUrgentFlag.listCurrentUrgentFlags(caseIDDetails);
    final int urgentFlagSize = urgentFlagListDetails.dtls.size();

    if (urgentFlagSize > 0) {
      // Add Urgent Flag link
      addUrgentLinkItem(linksPanelBuilder, details.caseID, urgentFlagSize);
    }

    if (issueList.dtls.size() > 0) {
      addIssueLinkItem(linksPanelBuilder, issueList, details.caseID, true);
    }

    if (Configuration.getBooleanProperty(EnvVars.ENV_APPEALS_ISINSTALLED)) {

      final CaseIDKey caseIDKey = new CaseIDKey();

      caseIDKey.caseID = details.caseID;

      String methodName = "countActiveAppealsByCaseID";

      final long appealsCount =
        this.getCountWithReflection(methodName, caseIDKey);

      if (appealsCount > 0L) {
        addAppealLinkItem(linksPanelBuilder, appealsCount, details.caseID);

      }

      methodName = "countActiveLegalActionsByCaseID";

      final long legalActionsCount =
        this.getCountWithReflection(methodName, caseIDKey);

      if (legalActionsCount > 0L) {
        addLegalActionLinkItem(linksPanelBuilder, legalActionsCount,
          details.caseID);

      }
    }

    final ContentPanelBuilder caseOwnerPanelBuilder =
      ContentPanelBuilder.createPanel("pd-case-owner-panel");

    final ImageBuilder iconCaseOwner =
      ImageBuilder.createImage("IconCaseOwner", "");

    iconCaseOwner
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");
    final LocalisableString altTextLocalizedMessage =
      new LocalisableString(BPOCASETAB.INF_CASE_OWNER);

    iconCaseOwner
      .setImageAltText(altTextLocalizedMessage.toClientFormattedText());

    caseOwnerPanelBuilder.addImageItem(iconCaseOwner, "pd-case-owner-icon");

    final LinkBuilder linkBuilder =
      LinkBuilder.createLink(caseOwnerDetails.orgObjectReferenceName,
        "Case_resolveOrgObjectTypeHomePage.do");

    linkBuilder.openAsModal();
    linkBuilder.addParameter("userName",
      String.valueOf(caseOwnerDetails.userName));

    linkBuilder.addParameter("orgObjectReference",
      String.valueOf(caseOwnerDetails.orgObjectReference));

    linkBuilder.addParameter("orgObjectType",
      String.valueOf(caseOwnerDetails.orgObjectType));

    final LocalisableString caseOwnerTitle =
      new LocalisableString(BPOCASETAB.INF_CASEOWNER_TITLE);

    linkBuilder.addLinkTitle(caseOwnerTitle);

    caseOwnerPanelBuilder.addLinkItem(linkBuilder, "pd-case-owner");

    linksPanelBuilder.addContentPanel(caseOwnerPanelBuilder);

    return linksPanelBuilder.getLinksPanel();
  }

  public static void addVerificationLinkItem(
    final LinksPanelBuilder linksPanelBuilder, final long verificationCount,
    final long caseID, final boolean PDCaseInd)
    throws AppException, InformationalException {

    final LocalisableString altTextTooltipMessage =
      new LocalisableString(BPOCASETAB.INF_VERIFICATIONS_TOOLTIP);

    final ImageBuilder imageBuilder =
      ImageBuilder.createImage("IconVerification", "");

    imageBuilder
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");
    imageBuilder
      .setImageAltText(altTextTooltipMessage.toClientFormattedText());

    final LocalisableString verification =
      new LocalisableString(BPOCASETAB.INF_NUM_VERIFICATIONS)
        .arg(verificationCount);

    LinkBuilder linkBuilder;
    if (PDCaseInd) {

      final ProductDelivery productDeliveryObj =
        ProductDeliveryFactory.newInstance();
      final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();

      new ProductDeliveryTypeDetails();
      productDeliveryKey.caseID = caseID;

      final ProductDeliveryTypeDetails productDeliveryTypeDetails =
        productDeliveryObj.readProductType(productDeliveryKey);

      linkBuilder = LinkBuilder.createLocalizableLink(
        verification.toClientFormattedText(),
        "Case_resolveVerificationLinkHomePage.do");

      linkBuilder.addParameter("caseID", String.valueOf(caseID));

      linkBuilder.addParameter("productType",
        productDeliveryTypeDetails.productType);

    } else {
      final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
      final CaseKey caseKey = new CaseKey();

      caseKey.caseID = caseID;

      final IntegratedCaseType integratedCaseType =
        caseHeaderObj.readIntegratedCaseType(caseKey);

      linkBuilder = LinkBuilder.createLocalizableLink(
        verification.toClientFormattedText(),
        "IntegratedCase_resolveVerificationLinkHomePage.do");

      linkBuilder.addParameter("caseID", String.valueOf(caseID));

      linkBuilder.addParameter("icType",
        integratedCaseType.integratedCaseType);
    }

    final LocalisableString verificationTitle =
      new LocalisableString(BPOCASETAB.INF_VERIFICATIONS_TITLE);

    linkBuilder.addLinkTitle(verificationTitle);

    linksPanelBuilder.addLinkEntry(imageBuilder, linkBuilder,
      "icon-verifications");

  }

  public static void addEvidenceInEditLinkItem(
    final LinksPanelBuilder linksPanelBuilder, final long evidenceInEditCount,
    final long caseID, final boolean PDCaseInd)
    throws AppException, InformationalException {

    final LocalisableString altTextTooltipMessage =
      new LocalisableString(BPOCASETAB.INF_EVIDENCE_IN_EDIT_TOOLTIP);

    final ImageBuilder imageBuilder =
      ImageBuilder.createImage("IconEvidenceInEdit", "");

    imageBuilder
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");
    imageBuilder
      .setImageAltText(altTextTooltipMessage.toClientFormattedText());

    final String inEditEvidence =
      new LocalisableString(BPOCASETAB.INF_NUM_EVIDENCE_IN_EDIT)
        .arg(evidenceInEditCount).toClientFormattedText();

    LinkBuilder linkBuilder;
    if (PDCaseInd) {

      final ProductDelivery productDeliveryObj =
        ProductDeliveryFactory.newInstance();
      final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();

      new ProductDeliveryTypeDetails();
      productDeliveryKey.caseID = caseID;

      final ProductDeliveryTypeDetails productDeliveryTypeDetails =
        productDeliveryObj.readProductType(productDeliveryKey);

      linkBuilder = LinkBuilder.createLocalizableLink(inEditEvidence,
        "Case_resolveEvidenceInEditLinkHomePage.do");

      linkBuilder.addParameter("caseID", String.valueOf(caseID));

      linkBuilder.addParameter("productType",
        productDeliveryTypeDetails.productType);

    } else {
      final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
      final CaseKey caseKey = new CaseKey();

      caseKey.caseID = caseID;

      final IntegratedCaseType integratedCaseType =
        caseHeaderObj.readIntegratedCaseType(caseKey);

      linkBuilder = LinkBuilder.createLocalizableLink(inEditEvidence,
        "IntegratedCase_resolveEvidenceInEditLinkHomePage.do");

      linkBuilder.addParameter("caseID", String.valueOf(caseID));

      linkBuilder.addParameter("icType",
        integratedCaseType.integratedCaseType);

    }

    final LocalisableString inEditEvidenceTitle =
      new LocalisableString(BPOCASETAB.INF_INEDITEVIDENCE_TITLE);

    linkBuilder.addLinkTitle(inEditEvidenceTitle);

    linksPanelBuilder.addLinkEntry(imageBuilder, linkBuilder,
      "icon-evidence-in-edit");

  }

  protected static void addUnderpaymentLinkItem(
    final LinksPanelBuilder linksPanelBuilder, final long underpaymentCount,
    final long caseID) throws AppException, InformationalException {

    final LocalisableString altTextTooltipMessage =
      new LocalisableString(BPOCASETAB.INF_UNDERPAYMENTS_TOOLTIP);

    final ImageBuilder imageBuilder =
      ImageBuilder.createImage("IconUnderpayments", "");

    imageBuilder
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");
    imageBuilder
      .setImageAltText(altTextTooltipMessage.toClientFormattedText());

    final String underpaymentLink =
      new LocalisableString(BPOCASETAB.INF_NUM_UNDERPAYMENTS)
        .arg(underpaymentCount).toClientFormattedText();

    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();
    final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();

    new ProductDeliveryTypeDetails();
    productDeliveryKey.caseID = caseID;

    final ProductDeliveryTypeDetails productDeliveryTypeDetails =
      productDeliveryObj.readProductType(productDeliveryKey);

    final LinkBuilder linkBuilder = LinkBuilder.createLocalizableLink(
      underpaymentLink, "Case_resolveOverUnderPaymentLinkHomePage.do");

    linkBuilder.addParameter("caseID", String.valueOf(caseID));

    linkBuilder.addParameter("productType",
      productDeliveryTypeDetails.productType);

    final LocalisableString underpaymentTitle =
      new LocalisableString(BPOCASETAB.INF_UNDERPAYMENT_TITLE);

    linkBuilder.addLinkTitle(underpaymentTitle);

    linksPanelBuilder.addLinkEntry(imageBuilder, linkBuilder,
      "icon-underpayments");

  }

  protected static void addOverpaymentLinkItem(
    final LinksPanelBuilder linksPanelBuilder, final long overpaymentCount,
    final long caseID) throws AppException, InformationalException {

    final LocalisableString altTextTooltipMessage =
      new LocalisableString(BPOCASETAB.INF_OVERPAYMENTS_TOOLTIP);

    final ImageBuilder imageBuilder =
      ImageBuilder.createImage("IconOverpayments", "");

    imageBuilder
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");
    imageBuilder
      .setImageAltText(altTextTooltipMessage.toClientFormattedText());

    final String underpaymentLink =
      new LocalisableString(BPOCASETAB.INF_NUM_OVERPAYMENTS)
        .arg(overpaymentCount).toClientFormattedText();

    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();
    final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();

    new ProductDeliveryTypeDetails();
    productDeliveryKey.caseID = caseID;

    final ProductDeliveryTypeDetails productDeliveryTypeDetails =
      productDeliveryObj.readProductType(productDeliveryKey);

    final LinkBuilder linkBuilder = LinkBuilder.createLocalizableLink(
      underpaymentLink, "Case_resolveOverUnderPaymentLinkHomePage.do");

    linkBuilder.addParameter("caseID", String.valueOf(caseID));

    linkBuilder.addParameter("productType",
      productDeliveryTypeDetails.productType);

    final LocalisableString overpaymentTitle =
      new LocalisableString(BPOCASETAB.INF_OVERPAYMENT_TITLE);

    linkBuilder.addLinkTitle(overpaymentTitle);

    linksPanelBuilder.addLinkEntry(imageBuilder, linkBuilder,
      "icon-overpayments");

  }

  public static void addIssueLinkItem(
    final LinksPanelBuilder linksPanelBuilder,
    final IssueRollOverDetailList issueList, final long caseID,
    final boolean PDCaseInd) throws AppException, InformationalException {

    final LocalisableString altTextTooltipMessage =
      new LocalisableString(BPOCASETAB.INF_ISSUE_CASES_TOOLTIP);

    final ImageBuilder imageBuilder =
      ImageBuilder.createImage("IconIssue", "");

    imageBuilder
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");
    imageBuilder
      .setImageAltText(altTextTooltipMessage.toClientFormattedText());

    if (issueList.dtls.size() > 0) {

      final TooltipBuilder tooltipBuilder =
        TooltipBuilder.createTooltip(imageBuilder.getImageIdentifier());

      final ListBuilder listBuilder = ListBuilder.createList(1);

      for (int i = 0; i < issueList.dtls.size(); ++i) {
        listBuilder.addRow();

        listBuilder.addEntry(1, i + 1,
          new LocalisableString(BPOCASETAB.INF_ISSUE)
            .arg(new CodeTableItemIdentifier(ISSUECONFIGURATIONTYPE.TABLENAME,
              issueList.dtls.item(i).issueType))
            .arg(issueList.dtls.item(i).startDate));

      }

      tooltipBuilder.addWidgetItem(listBuilder, "style", "single-list", "");

      linksPanelBuilder.addTooltipItem(tooltipBuilder);
    }

    final String issueLink = new LocalisableString(BPOCASETAB.INF_NUM_ISSUES)
      .arg(issueList.dtls.size()).toClientFormattedText();

    LinkBuilder linkBuilder;
    if (PDCaseInd) {

      final ProductDelivery productDeliveryObj =
        ProductDeliveryFactory.newInstance();
      final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();

      new ProductDeliveryTypeDetails();
      productDeliveryKey.caseID = caseID;

      final ProductDeliveryTypeDetails productDeliveryTypeDetails =
        productDeliveryObj.readProductType(productDeliveryKey);

      linkBuilder = LinkBuilder.createLocalizableLink(issueLink,
        "Case_resolveIssueLinkHomePage.do");

      linkBuilder.addParameter("caseID", String.valueOf(caseID));

      linkBuilder.addParameter("productType",
        productDeliveryTypeDetails.productType);

    } else {
      final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
      final CaseKey caseKey = new CaseKey();

      caseKey.caseID = caseID;

      final IntegratedCaseType integratedCaseType =
        caseHeaderObj.readIntegratedCaseType(caseKey);

      linkBuilder = LinkBuilder.createLocalizableLink(issueLink,
        "IntegratedCase_resolveIssueLinkHomePage.do");

      linkBuilder.addParameter("caseID", String.valueOf(caseID));

      linkBuilder.addParameter("icType",
        integratedCaseType.integratedCaseType);

    }

    final LocalisableString issuesTitle =
      new LocalisableString(BPOCASETAB.INF_ISSUES_TITLE);

    linkBuilder.addLinkTitle(issuesTitle);

    linksPanelBuilder.addLinkEntry(imageBuilder, linkBuilder, "icon-issues");

  }

  public static void addAppealLinkItem(
    final LinksPanelBuilder linksPanelBuilder, final long appealCount,
    final long caseID) {

    final LocalisableString altTextTooltipMessage =
      new LocalisableString(BPOCASETAB.INF_APPEALS_TOOLTIP);

    final ImageBuilder imageBuilder =
      ImageBuilder.createImage("IconAppeal", "");

    imageBuilder
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");
    imageBuilder
      .setImageAltText(altTextTooltipMessage.toClientFormattedText());

    final String appealsLink =
      new LocalisableString(BPOCASETAB.INF_NUM_APPEALS).arg(appealCount)
        .toClientFormattedText();

    final LinkBuilder linkBuilder = LinkBuilder.createLocalizableLink(
      appealsLink, "Appeal_listAppealCaseCombinedListPage.do");

    linkBuilder.addParameter("caseID", String.valueOf(caseID));

    final LocalisableString appealsTitle =
      new LocalisableString(BPOCASETAB.INF_APPEALS_TITLE);

    linkBuilder.addLinkTitle(appealsTitle);

    linksPanelBuilder.addLinkEntry(imageBuilder, linkBuilder, "icon-appeals");
  }

  protected static void addLegalActionLinkItem(
    final LinksPanelBuilder linksPanelBuilder, final long legalActionCount,
    final long caseID) {

    final LocalisableString altTextTooltipMessage =
      new LocalisableString(BPOCASETAB.INF_LEGAL_ACTIONS_TOOLTIP);

    final ImageBuilder imageBuilder =
      ImageBuilder.createImage("IconLegalAction", "");

    imageBuilder
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");
    imageBuilder
      .setImageAltText(altTextTooltipMessage.toClientFormattedText());

    final String legalActionLink =
      new LocalisableString(BPOCASETAB.INF_NUM_LEGAL_ACTIONS)
        .arg(legalActionCount).toClientFormattedText();

    final LinkBuilder linkBuilder = LinkBuilder.createLocalizableLink(
      legalActionLink, "LegalAction_listLegalActionsPage.do");

    linkBuilder.addParameter("caseID", String.valueOf(caseID));

    final LocalisableString legalActionsTitle =
      new LocalisableString(BPOCASETAB.INF_LEGALACTIONS_TITLE);

    linkBuilder.addLinkTitle(legalActionsTitle);

    linksPanelBuilder.addLinkEntry(imageBuilder, linkBuilder,
      "icon-legal-actions");

  }

  protected long getCountWithReflection(final String methodName,
    final CaseIDKey caseIDKey) throws DatabaseException, AppRuntimeException,
    AppException, InformationalException {

    long numAppeals = 0L;

    Class cls = null;
    final String var6 =
      "curam.appeal.facade.impl.CoreToAppealDelegateWrapper";

    AppException exc;
    try {
      cls =
        Class.forName("curam.appeal.facade.impl.CoreToAppealDelegateWrapper");

      final Method constructorMethod = cls.getMethod("newInstance");

      final Object classObj = constructorMethod.invoke(cls);

      final Object[] arguments = new Object[]{caseIDKey };

      final Class[] parameterTypes = new Class[]{CaseIDKey.class };

      final Method method =
        classObj.getClass().getMethod(methodName, parameterTypes);

      numAppeals = (Long) method.invoke(classObj, arguments);

    } catch (final IllegalAccessException var12) {
      ;
    } catch (final ClassNotFoundException var13) {

      exc = new AppException(BPOREFLECTION.ERR_REFLECTION_CLASS_NOT_FOUND);

      exc.arg("curam.appeal.facade.impl.CoreToAppealDelegateWrapper");
      exc.arg(this.getClass().toString());
      exc.arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME,
        CASETYPECODE.APPEAL, TransactionInfo.getProgramLocale()));

      throw new RuntimeException(exc);

    } catch (final NoSuchMethodException var14) {

      exc = new AppException(BPOREFLECTION.ERR_REFLECTION_NO_SUCH_METHOD);

      exc.arg("curam.appeal.facade.impl.CoreToAppealDelegateWrapper");

      exc.arg(this.getClass().toString() + "." + methodName);
      exc.arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME,
        CASETYPECODE.APPEAL, TransactionInfo.getProgramLocale()));

      throw new RuntimeException(exc);

    } catch (final IllegalArgumentException var15) {

      exc = new AppException(BPOREFLECTION.ERR_ILLEGAL_FUNCTION_ARGUMENTS);

      exc.arg(this.getClass().toString() + "." + methodName);
      exc.arg("curam.appeal.facade.impl.CoreToAppealDelegateWrapper");
      exc.arg(CodeTable.getOneItem(CASETYPECODE.TABLENAME,
        CASETYPECODE.APPEAL, TransactionInfo.getProgramLocale()));

      throw new RuntimeException(exc);

    } catch (final InvocationTargetException var16) {

      exc = new AppException(BPOREFLECTION.ERR_BPO_MESSAGE_RETURNED);

      exc.arg(var16.getTargetException().getMessage());
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
    final long caseID, final int urgentFlagCounts)
    throws AppException, InformationalException {

    final LocalisableString altTextTooltipMessage = new LocalisableString(
      BDMAPPLICATIONCASECONTEXTPANEL.INF_URGENTFLAG_TOOLTIP);
    final ImageBuilder urgentFlagIcon =
      ImageBuilder.createImage("Warning.Icon", "");

    urgentFlagIcon.setImageResource("cefwidgets.Image");
    urgentFlagIcon
      .setImageAltText(altTextTooltipMessage.toClientFormattedText());

    final LocalisableString urgentFlagString =
      new LocalisableString(BDMAPPLICATIONCASECONTEXTPANEL.INFO_FLAG_URGENT);

    urgentFlagString.arg(urgentFlagCounts);

    final LinkBuilder urgentFlagLinkbuilder = LinkBuilder
      .createLocalizableLink(urgentFlagString.toClientFormattedText(),
        "BDM_PDC_UrgentFlag_listForCasePage.do");

    urgentFlagLinkbuilder.addParameter("caseID", String.valueOf(caseID));

    linksPanelBuilder.addLinkEntry(urgentFlagIcon, urgentFlagLinkbuilder,
      "icon-warning");

  }

}
