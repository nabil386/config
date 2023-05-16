package curam.ca.gc.bdm.sl.appeals.impl;

import curam.appeal.sl.entity.fact.AppealFactory;
import curam.appeal.sl.entity.fact.AppealRelationshipFactory;
import curam.appeal.sl.entity.struct.AllAppealParticipantsKey;
import curam.appeal.sl.entity.struct.AppealCaseID;
import curam.appeal.sl.entity.struct.AppealCaseIDCurrentDateKey;
import curam.appeal.sl.entity.struct.AppealCaseIDKey;
import curam.appeal.sl.entity.struct.AppealCaseParticipantTabDetail;
import curam.appeal.sl.entity.struct.AppealCaseParticipantTabDetailList;
import curam.appeal.sl.entity.struct.AppealCaseTabDetailKey;
import curam.appeal.sl.entity.struct.AppealID;
import curam.appeal.sl.entity.struct.AppealKey;
import curam.appeal.sl.entity.struct.AppealedCaseDetails;
import curam.appeal.sl.entity.struct.AppealedCaseDetailsList;
import curam.appeal.sl.entity.struct.CountAppealedItemsKey;
import curam.appeal.sl.entity.struct.OrganisationAppellantToolTipDetailList;
import curam.appeal.sl.fact.AppealedCaseFactory;
import curam.appeal.sl.struct.AppealCaseKey;
import curam.appeal.sl.struct.AppealCaseTabDetail;
import curam.appeal.sl.struct.CalculateAppealDeadlineDateDetails1;
import curam.appeal.sl.struct.DeadlineDate;
import curam.appeal.util.impl.GeneralAppealConstants;
import curam.ca.gc.bdm.entity.fact.BDMAppealFactory;
import curam.ca.gc.bdm.entity.struct.BDMAppealDtls;
import curam.ca.gc.bdm.entity.struct.BDMAppealKey;
import curam.cefwidgets.docbuilder.impl.ContentPanelBuilder;
import curam.cefwidgets.docbuilder.impl.ImageBuilder;
import curam.cefwidgets.docbuilder.impl.ImagePanelBuilder;
import curam.cefwidgets.docbuilder.impl.LinkBuilder;
import curam.cefwidgets.docbuilder.impl.LinksPanelBuilder;
import curam.cefwidgets.docbuilder.impl.ListBuilder;
import curam.cefwidgets.docbuilder.impl.RotatorBuilder;
import curam.cefwidgets.docbuilder.impl.StackContainerBuilder;
import curam.cefwidgets.docbuilder.impl.TooltipBuilder;
import curam.cefwidgets.utilities.impl.RendererConfig;
import curam.cefwidgets.utilities.impl.RendererConfig.RendererConfigType;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.APPEALRELATIONSHIPSTATUS;
import curam.codetable.APPEALTYPE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.GENDER;
import curam.codetable.HEARINGSTATUS;
import curam.codetable.ORGOBJECTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.APPEALCASERESOLUTIONEntry;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.LocationFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.ProspectPersonFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.struct.OrgObjectLinkKey;
import curam.core.sl.fact.CaseUserRoleFactory;
import curam.core.sl.fact.ConcernRoleImageFactory;
import curam.core.sl.fact.TabDetailFormatterFactory;
import curam.core.sl.fact.TabDetailsHelperFactory;
import curam.core.sl.impl.CaseTabDetailsHelper;
import curam.core.sl.impl.TabDetailFormatterInterface;
import curam.core.sl.intf.CaseUserRole;
import curam.core.sl.intf.ConcernRoleImage;
import curam.core.sl.intf.TabDetailFormatter;
import curam.core.sl.intf.TabDetailsHelper;
import curam.core.sl.struct.CaseOwnerDetails;
import curam.core.sl.struct.ContactTabDetails;
import curam.core.sl.struct.FormatPersonNameKey;
import curam.core.sl.struct.IncidentAndRoleTabDetailsList;
import curam.core.sl.struct.InvestigationAndRoleTabDetailsList;
import curam.core.sl.struct.PersonAgeDetails;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.AlternateNameReadByTypeKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRoleTypeDetails;
import curam.core.struct.LocationKey;
import curam.core.struct.LocationTabDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.ProspectPersonKey;
import curam.core.struct.ReadGenderDetails;
import curam.core.struct.ReadProspectPersonGenderDetails;
import curam.core.struct.UsersKey;
import curam.message.APPEALTAB;
import curam.message.BPOCASETAB;
import curam.message.IMAGEPANELBUILDER;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.resources.StringUtil;
import curam.util.type.CodeTable;
import curam.util.type.CodeTableItemIdentifier;
import curam.util.type.Date;
import curam.util.type.DateTime;
import org.apache.commons.lang.StringUtils;

public class BDMAppealTabHelper extends curam.appeal.sl.base.AppealTab {

  @Override
  public AppealCaseTabDetail readAppealCaseTabDetail(
    final AppealCaseTabDetailKey appealCaseTabDetailKey)
    throws AppException, InformationalException {

    final AppealCaseTabDetail appealCaseTabDetail = new AppealCaseTabDetail();

    final curam.appeal.sl.entity.intf.Appeal appealObj =
      AppealFactory.newInstance();

    appealCaseTabDetail.dtls =
      appealObj.readAppealCaseTabDetails(appealCaseTabDetailKey);

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

    caseHeaderKey.caseID = appealCaseTabDetailKey.caseID;

    // Reading the caseHeader details to get the primary clients concernRoleID
    final CaseHeaderDtls caseHeaderDtls =
      CaseHeaderFactory.newInstance().read(caseHeaderKey);

    // Setting the key for the concernRole Read
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = caseHeaderDtls.concernRoleID;

    // Reading the concernRole details
    final ConcernRoleDtls concernRoleDtls =
      ConcernRoleFactory.newInstance().read(concernRoleKey);

    appealCaseTabDetail.dtls.primaryClientName =
      concernRoleDtls.concernRoleName;

    // Return a count of the appealed cases (issues, product deliveries and
    // other Appeal cases).
    final CountAppealedItemsKey countAppealedItemsKey =
      new CountAppealedItemsKey();

    countAppealedItemsKey.caseID = appealCaseTabDetailKey.caseID;

    appealCaseTabDetail.appealedItemDtls =
      appealObj.readCountAppealedItemsTabDetail(countAppealedItemsKey);

    // BEGIN, CR00425681, DG
    // Get the correct number of items on the appeal case
    final AppealCaseKey appealCaseKey = new AppealCaseKey();

    appealCaseKey.caseID = appealCaseTabDetailKey.caseID;
    appealCaseTabDetail.appealedItemsTotal =
      curam.appeal.sl.fact.AppealFactory.newInstance()
        .listObjectsByAppealCase(appealCaseKey).dtls.size();
    // END, CR00425681

    // BEGIN, CR00288724, MC
    final AllAppealParticipantsKey appealParticipantsKey =
      new AllAppealParticipantsKey();

    appealParticipantsKey.caseID = appealCaseTabDetailKey.caseID;

    appealCaseTabDetail.participantDtlsList =
      appealObj.searchAllAppealParticipantTabDetails(appealParticipantsKey);
    // END, CR00288724

    // Process the list so that the primary client is first in the list and is
    // not repeated if they have multiple roles.
    AppealCaseParticipantTabDetailList participantList =
      appealCaseTabDetail.participantDtlsList;
    final AppealCaseParticipantTabDetail firstAppealCaseParticipant =
      new AppealCaseParticipantTabDetail();

    // BEGIN, CR00275714, ZV
    if (!participantList.dtls.isEmpty()) {
      firstAppealCaseParticipant.assign(participantList.dtls.item(0));
    }
    // END, CR00275714

    for (int i = 0; i < participantList.dtls.size(); i++) {

      if (participantList.dtls.item(i).primaryClientInd) {
        // Move this item to the top of the list
        // Swap the details from the original list to the new one.
        participantList.dtls.item(0).assign(participantList.dtls.item(i));
        participantList.dtls.item(i).assign(firstAppealCaseParticipant);
      }
    }

    for (int i = 0; i < participantList.dtls.size(); i++) {

      participantList.dtls.item(i).caseParticipantRoleText =
        getParticipantRole(participantList.dtls.item(i).primaryClientInd,
          participantList.dtls.item(i).caseParticipantTypeCode)
            .toClientFormattedText();

      // Get name of primary client, for tab display text
      if (participantList.dtls.item(i).primaryClientInd) {
        appealCaseTabDetail.dtls.primaryClientName =
          participantList.dtls.item(i).concernRoleName;
      }

    }

    // BEGIN, CR00287105, MC
    participantList = setOrganisationAppellant(participantList);
    // END, CR00287105

    // BEGIN, CR00267298, MC
    participantList = removeDuplicates(participantList);
    // END, CR00267298

    appealCaseTabDetail.participantDtlsList = participantList;

    // List the Appellants for the case that are organization and add them to
    // the list of participants.
    final AppealCaseIDCurrentDateKey appealCaseIDCurrentDateKey =
      new AppealCaseIDCurrentDateKey();

    appealCaseIDCurrentDateKey.caseID = appealCaseTabDetailKey.caseID;

    // BEGIN, CR00287105, MC
    final OrganisationAppellantToolTipDetailList orgAppellantsList = appealObj
      .searchOrganisationAppellantToolTipDetail(appealCaseIDCurrentDateKey);

    AppealCaseParticipantTabDetail appealCaseParticipantTabDetail;

    for (int i = 0; i < orgAppellantsList.dtls.size(); i++) {

      appealCaseParticipantTabDetail = new AppealCaseParticipantTabDetail();
      appealCaseParticipantTabDetail.assign(orgAppellantsList.dtls.item(i));

      if (orgAppellantsList.dtls.item(i).locationID == 0) {

        appealCaseParticipantTabDetail.concernRoleName =
          orgAppellantsList.dtls.item(i).orgUnitName;
        appealCaseParticipantTabDetail.addressData = AddressDataFactory
          .newInstance().getAddressDataForLocale().addressData;
      } else {
        final LocationKey locationKey = new LocationKey();

        locationKey.locationID = orgAppellantsList.dtls.item(i).locationID;
        final LocationTabDtls locationTabDtls =
          LocationFactory.newInstance().readLocationTabDetails(locationKey);

        // This not a concern and these are not concern details but for
        // constancy this participants details
        // will be displayed using the same mechanism as the one used for
        // concern roles.
        appealCaseParticipantTabDetail.concernRoleName =
          orgAppellantsList.dtls.item(i).orgUnitName;
        appealCaseParticipantTabDetail.addressData =
          locationTabDtls.addressData;
        appealCaseParticipantTabDetail.emailAddress =
          locationTabDtls.emailAddress;
        appealCaseParticipantTabDetail.phoneNumberID =
          locationTabDtls.phoneNumberID;
      }

      appealCaseParticipantTabDetail.caseParticipantTypeCode =
        CASEPARTICIPANTROLETYPE.APPELLANT;

      appealCaseTabDetail.participantDtlsList.dtls
        .addRef(appealCaseParticipantTabDetail);
    }
    // END, CR00287105

    appealCaseTabDetail.completionCountdownText =
      getCompletionCountdownText(appealCaseTabDetailKey, appealCaseTabDetail)
        .toClientFormattedText();

    final ContentPanelBuilder containerPanel = ContentPanelBuilder
      .createPanel(GeneralAppealConstants.gkAppealsHearingContainer);

    final StackContainerBuilder stackContainerBuilder =
      getCaseMemberThumbnailDetails(participantList);

    containerPanel.addWidgetItem(stackContainerBuilder, CuramConst.gkStyle,
      CuramConst.gkStyleStackContainer);

    // Read Hearing Details
    final ContentPanelBuilder contentPanelBuilder =
      getAppealHearingDetails(appealCaseTabDetailKey, appealCaseTabDetail);

    if (APPEALTYPE.HEARING.equals(appealCaseTabDetail.dtls.appealTypeCode)
      || APPEALTYPE.HEARINGREVIEW
        .equals(appealCaseTabDetail.dtls.appealTypeCode)) {
      containerPanel.addWidgetItem(contentPanelBuilder, CuramConst.gkStyle,
        CuramConst.gkContentPanel,
        GeneralAppealConstants.gkAppealHearingDetailsPanel);
    } else {
      // apply styling for judicial review details panel
      // (removes blue bar at bottom of panel)
      containerPanel.addWidgetItem(contentPanelBuilder, CuramConst.gkStyle,
        CuramConst.gkContentPanel,
        GeneralAppealConstants.gkJudicialHearingDetailsPanel);
    }

    final ContentPanelBuilder linksPanel =
      getAppealHearingLinks(appealCaseTabDetail);

    containerPanel.addWidgetItem(linksPanel, CuramConst.gkStyle,
      CuramConst.gkContentPanel,
      GeneralAppealConstants.gkAppealHearingLinksPanel);

    final AppealKey appealKey = new AppealKey();
    appealKey.appealID = appealCaseTabDetail.dtls.caseID;
    final AppealCaseIDKey appealKey1 = new AppealCaseIDKey();
    appealKey1.caseID = appealCaseTabDetail.dtls.caseID;
    final AppealID appealId =
      AppealFactory.newInstance().readAppealIDByCase(appealKey1);

    // modify the BDM Appeal details
    final BDMAppealKey bdmAppealKey = new BDMAppealKey();
    bdmAppealKey.appealID = appealId.appealID;

    final BDMAppealDtls bdmAppealDetails =
      BDMAppealFactory.newInstance().read(bdmAppealKey);

    if (bdmAppealDetails != null
      && StringUtils.isNotEmpty(bdmAppealDetails.admsReference)) {

      final ContentPanelBuilder linksPanel2 =
        getRFRDetails(appealCaseTabDetail, bdmAppealDetails);

      containerPanel.addWidgetItem(linksPanel2, CuramConst.gkStyle,
        CuramConst.gkContentPanel, "appeal-rfr-details-panel");
    }

    appealCaseTabDetail.hearingCaseTabDetails = containerPanel.toString();

    return appealCaseTabDetail;
  }

  protected LocalisableString getParticipantRole(
    final boolean primaryClientInd, final String caseParticipantTypeCode) {

    LocalisableString participantRole;

    // Add (Primary) to the role of the primary client
    // e.g. Appellant (Primary)
    if (primaryClientInd) {

      participantRole = new LocalisableString(
        curam.message.APPEALTAB.INF_APPEAL_ROLE_PRIMARY);
      participantRole.arg(new CodeTableItemIdentifier(
        curam.codetable.CASEPARTICIPANTROLETYPE.TABLENAME,
        caseParticipantTypeCode));
    } // If the respondent is not set the product provider is the respondent.
    else if (caseParticipantTypeCode
      .equals(CASEPARTICIPANTROLETYPE.PRODUCTPROVIDER)) {
      participantRole =
        new LocalisableString(curam.message.APPEALTAB.INF_APPEAL_ROLE);
      participantRole.arg(new CodeTableItemIdentifier(
        curam.codetable.CASEPARTICIPANTROLETYPE.TABLENAME,
        CASEPARTICIPANTROLETYPE.RESPONDENT));
    } else {
      participantRole =
        new LocalisableString(curam.message.APPEALTAB.INF_APPEAL_ROLE);
      participantRole.arg(new CodeTableItemIdentifier(
        curam.codetable.CASEPARTICIPANTROLETYPE.TABLENAME,
        caseParticipantTypeCode));
    }

    return participantRole;
  }

  /**
   * Reads the details of the appeal case links to be displayed on the details
   * panel
   * (right panel).
   *
   * @param appealCaseTabDetail
   * Appeals Case Details.
   *
   * @return ContentPanelBuilder.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  protected ContentPanelBuilder
    getAppealHearingLinks(final AppealCaseTabDetail appealCaseTabDetail)
      throws AppException, InformationalException {

    final LinksPanelBuilder linksPanelBuilder =
      LinksPanelBuilder.createLinksPanel(1);

    final ContentPanelBuilder caseStatus = ContentPanelBuilder
      .createPanel(GeneralAppealConstants.gkAppealCaseStatus);

    final String status = new LocalisableString(BPOCASETAB.INF_STATUS)
      .arg(CodeTable.getOneItem(CASESTATUS.TABLENAME,
        appealCaseTabDetail.dtls.caseStatusCode))
      .toClientFormattedText();

    caseStatus.addlocalisableStringItem(status,
      GeneralAppealConstants.gkAppealStatus);

    linksPanelBuilder.addContentPanel(caseStatus);

    final ImageBuilder appealItemsIcon =
      ImageBuilder.createImage(GeneralAppealConstants.gkAppealItemsBlue, "");

    appealItemsIcon.setImageResource(CuramConst.gkRendererImages);
    final LocalisableString numAppealedItemsString =
      new LocalisableString(APPEALTAB.INF_NUM_APPEALED_ITEMS);

    appealItemsIcon
      .setImageAltText(numAppealedItemsString.toClientFormattedText());

    final LocalisableString appealedItemsString =
      new LocalisableString(APPEALTAB.INF_APPEALED_ITEMS)
        .arg(appealCaseTabDetail.appealedItemsTotal);

    // BEGIN, 179767, AZ
    // Add "icon-appealed-item" CSS class to link entry
    linksPanelBuilder.addLinkEntry(appealItemsIcon,
      appealedItemsString.toClientFormattedText(),
      GeneralAppealConstants.gkAppealedItemIcon);
    // END, 179767

    final ContentPanelBuilder caseOwnerPanelBuilder = ContentPanelBuilder
      .createPanel(GeneralAppealConstants.gkAppealCaseOwnerPanel);

    final ImageBuilder iconImageBuilder =
      ImageBuilder.createImage(CuramConst.gkIconCaseOwner, "");

    iconImageBuilder.setImageResource(CuramConst.gkRendererImages);
    final LocalisableString altTextLocalizedMessage =
      new LocalisableString(BPOCASETAB.INF_CASE_OWNER);

    iconImageBuilder
      .setImageAltText(altTextLocalizedMessage.toClientFormattedText());

    caseOwnerPanelBuilder.addImageItem(iconImageBuilder,
      GeneralAppealConstants.gkAppealCaseOwnerIcon);

    // Get Case Owner
    final CaseUserRole caseUserRoleObj = CaseUserRoleFactory.newInstance();
    final OrgObjectLinkKey orgObjectLinkKey = new OrgObjectLinkKey();

    orgObjectLinkKey.orgObjectLinkID =
      appealCaseTabDetail.dtls.ownerOrgObjectLinkID;

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

    final LinkBuilder linkBuilder2 =
      LinkBuilder.createLink(caseOwnerDetails.orgObjectReferenceName,
        CuramConst.gkCaseOwnerHomePage);

    linkBuilder2.openAsModal();
    linkBuilder2.addParameter(CuramConst.gkPageParameterUserName,
      String.valueOf(caseOwnerDetails.userName));
    linkBuilder2.addParameter(CuramConst.gkPageParameterOrgObjectReference,
      String.valueOf(caseOwnerDetails.orgObjectReference));
    linkBuilder2.addParameter(CuramConst.gkPageParameterOrgObjectType,
      String.valueOf(caseOwnerDetails.orgObjectType));

    final LocalisableString caseOwnerTitle =
      new LocalisableString(APPEALTAB.INF_CASEOWNER_TITLE);

    linkBuilder2.addLinkTitle(caseOwnerTitle);

    caseOwnerPanelBuilder.addLinkItem(linkBuilder2,
      GeneralAppealConstants.gkAppealCaseOwner);

    linksPanelBuilder.addContentPanel(caseOwnerPanelBuilder);

    return linksPanelBuilder.getLinksPanel();
  }

  /**
   * Reads the details of the appeal case to be displayed on the details panel
   * (middle panel).
   *
   * @param appealCaseTabDetailKey
   * Appeals case key to read the tab details.
   * @param appealCaseTabDetail
   * Appeals Case Details.
   *
   * @return ContentPanelBuilder.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  protected ContentPanelBuilder getAppealHearingDetails(
    final AppealCaseTabDetailKey appealCaseTabDetailKey,
    final AppealCaseTabDetail appealCaseTabDetail)
    throws AppException, InformationalException {

    final ContentPanelBuilder contentPanelBuilder = ContentPanelBuilder
      .createPanel(GeneralAppealConstants.gkAppealHearingDetails);

    contentPanelBuilder.addRoundedCorners();

    final ContentPanelBuilder appealContent =
      ContentPanelBuilder.createPanel(GeneralAppealConstants.gkAppealContent);

    // Add Hearing Schedule Type
    appealContent.addCodetableItem(appealCaseTabDetail.dtls.appealTypeCode,
      GeneralAppealConstants.gkAppealsType,
      GeneralAppealConstants.gkHearingType);

    if (APPEALTYPE.HEARING.equals(appealCaseTabDetail.dtls.appealTypeCode)
      || APPEALTYPE.HEARINGREVIEW
        .equals(appealCaseTabDetail.dtls.appealTypeCode)) {

      final ListBuilder listBuilder = ListBuilder.createHorizontalList(2);

      listBuilder.addRow();

      // BEGIN, CR00267298, MC
      final DateTime hearingDateTime = new curam.util.type.DateTime(
        appealCaseTabDetail.dtls.hearingDateTime);
      final Date hearingDate =
        new curam.util.type.Date(hearingDateTime.getCalendar());

      if (!hearingDate.isZero()
        && hearingDateTime.after(DateTime.getCurrentDateTime())
        && HEARINGSTATUS.SCHEDULED
          .equals(appealCaseTabDetail.dtls.hearingStatusCode)) {

        listBuilder.addEntry(1, 1,
          new LocalisableString(APPEALTAB.INF_NEXT_SCHEDULED_HEARING));
        listBuilder.addEntry(2, 1, hearingDate);

      } else if (!hearingDate.isZero()
        && hearingDateTime.before(DateTime.getCurrentDateTime())) {

        listBuilder.addEntry(1, 1,
          new LocalisableString(APPEALTAB.INF_LAST_SCHEDULED_HEARING));
        listBuilder.addEntry(2, 1, hearingDate);
        // END, CR00267298
      } else {

        listBuilder.addEntry(1, 1,
          new LocalisableString(APPEALTAB.INF_SCHEDULED_HEARING));
        listBuilder.addEntry(2, 1,
          new LocalisableString(APPEALTAB.INF_NOT_SCHEDULED));
      }

      appealContent.addSingleListItem(listBuilder,
        GeneralAppealConstants.gkHearingDetailTable);

      final ContentPanelBuilder decisionContent = ContentPanelBuilder
        .createPanel(GeneralAppealConstants.gkDecisionContent);

      final ContentPanelBuilder decisionContentDetail = ContentPanelBuilder
        .createPanel(GeneralAppealConstants.gkDecisionContentDetail);

      final ImageBuilder imageBuilder =
        ImageBuilder.createImage(GeneralAppealConstants.gkIconGoodTimeBlue);

      imageBuilder.setImageResource(CuramConst.gkRendererImages);
      // BEGIN ,CR00286835 , DK
      // Add alt text
      final LocalisableString altTextLocalizedMessage =
        new LocalisableString(APPEALTAB.INF_DAYS_TO_COMPLETE);

      imageBuilder
        .setImageAltText(altTextLocalizedMessage.toClientFormattedText());
      // END ,CR00286835 , DK
      decisionContentDetail.addImageItem(imageBuilder);

      decisionContentDetail.addlocalisableStringItem(
        getCompletionCountdownText(appealCaseTabDetailKey,
          appealCaseTabDetail).toClientFormattedText(),
        GeneralAppealConstants.gkCompletionText);

      decisionContent.addWidgetItem(decisionContentDetail, CuramConst.gkStyle,
        CuramConst.gkContentPanel);

      appealContent.addWidgetItem(decisionContent, CuramConst.gkStyle,
        CuramConst.gkContentPanel);

    } else {

      final ListBuilder creationDetails = ListBuilder.createHorizontalList(2);

      creationDetails.addRow();
      creationDetails.addEntry(1, 1,
        new LocalisableString(APPEALTAB.INF_CREATED));
      creationDetails.addEntry(2, 1, appealCaseTabDetail.dtls.creationDate);

      appealContent.addSingleListItem(creationDetails,
        GeneralAppealConstants.gkHearingDetailTable);
    }

    contentPanelBuilder.addWidgetItem(appealContent, CuramConst.gkStyle,
      CuramConst.gkContentPanel);

    // BEGIN, 179767, AZ
    // Block should be executed if resolutionCode is _not_ null or empty.
    if (!StringUtil.isNullOrEmpty(appealCaseTabDetail.dtls.resolutionCode)
      && !APPEALCASERESOLUTIONEntry.NOTDECIDED.getCode()
        .equals(appealCaseTabDetail.dtls.resolutionCode)) {

      contentPanelBuilder.addCodetableItem(
        appealCaseTabDetail.dtls.resolutionCode,
        GeneralAppealConstants.gkAppealCaseResolutionDomain,
        GeneralAppealConstants.gkResolutionWaterMark);
    }
    // END, 179767

    return contentPanelBuilder;
  }

  /**
   * Sets the completion count down text to the appropriate value based on the
   * admin settings, the deadline date and the current date.
   *
   * @param appealCaseTabDetailKey
   * Contains appeal case ID.
   * @param appealCaseTabDetail
   * Contains appeal case tab details.
   *
   * @return Completion count down text.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  protected LocalisableString getCompletionCountdownText(
    final AppealCaseTabDetailKey appealCaseTabDetailKey,
    final AppealCaseTabDetail appealCaseTabDetail)
    throws AppException, InformationalException {

    // BEGIN, CR00295153, DG
    DeadlineDate deadlineDate = new DeadlineDate();
    final CalculateAppealDeadlineDateDetails1 calculateDeadlineDate =
      new CalculateAppealDeadlineDateDetails1();

    final AppealCaseID appealCaseID = new AppealCaseID();

    appealCaseID.appealCaseID = appealCaseTabDetailKey.caseID;
    final AppealedCaseDetailsList appealCaseDetailsList =
      AppealRelationshipFactory.newInstance()
        .searchAppealedCaseDetailsByAppealCase(appealCaseID);

    for (final AppealedCaseDetails appealedCaseDetails : appealCaseDetailsList.dtls
      .items()) {
      if (appealedCaseDetails.statusCode
        .equals(APPEALRELATIONSHIPSTATUS.APPROVED)) {
        calculateDeadlineDate.appealTypeCode =
          appealCaseTabDetail.dtls.appealTypeCode;
        calculateDeadlineDate.implCaseID = appealedCaseDetails.caseID;
        calculateDeadlineDate.receivedDate =
          appealCaseTabDetail.dtls.receivedDate;
        calculateDeadlineDate.appealCaseID = appealCaseTabDetail.dtls.caseID;
        // temp deadline used to get the shortest deadline
        final DeadlineDate deadlineDateTemp = AppealedCaseFactory
          .newInstance().calculateDeadlineDate1(calculateDeadlineDate);

        if (deadlineDate.date.equals(Date.kZeroDate)) {
          deadlineDate = deadlineDateTemp;
        } else if (deadlineDate.date.after(deadlineDateTemp.date)) {
          deadlineDate = deadlineDateTemp;
        }
      }
    }

    // END, CR00295153

    LocalisableString completionCountdownText;

    final int daysBetweenCurrentAndDeadlineDate =
      deadlineDate.date.subtract(Date.getCurrentDate());

    if (!deadlineDate.date.equals(Date.kZeroDate)) {
      if (Date.getCurrentDate().before(deadlineDate.date)
        && appealCaseTabDetail.dtls.decisionDate.isZero()) {

        // Appeals decision is due in the number of days.
        completionCountdownText = new LocalisableString(
          curam.message.APPEALTAB.INF_DECISION_DAYS_TO_COMPLETE);

        completionCountdownText.arg(daysBetweenCurrentAndDeadlineDate);
      } else if (Date.getCurrentDate().after(deadlineDate.date)
        && appealCaseTabDetail.dtls.decisionDate.isZero()) {

        // The Appeals decision is overdue
        completionCountdownText =
          new LocalisableString(curam.message.APPEALTAB.INF_DECISION_OVERDUE);
        completionCountdownText.arg(daysBetweenCurrentAndDeadlineDate);
      } else if (!appealCaseTabDetail.dtls.decisionDate.isZero()
        && appealCaseTabDetail.dtls.decisionDate.after(deadlineDate.date)) {

        // The Appeals decision was overdue when it was completed
        // Decision reached in %1n days (%2n days outside timely decision
        // period)
        completionCountdownText = new LocalisableString(
          curam.message.APPEALTAB.INF_DECISION_OVERDUE_COMPLETE);

        completionCountdownText.arg(appealCaseTabDetail.dtls.receivedDate
          .subtract(appealCaseTabDetail.dtls.decisionDate));

        completionCountdownText.arg(
          deadlineDate.date.subtract(appealCaseTabDetail.dtls.decisionDate));

      } else if (!appealCaseTabDetail.dtls.decisionDate.isZero()
        && appealCaseTabDetail.dtls.decisionDate.before(deadlineDate.date)) {

        // Timely decision reached in X days
        completionCountdownText =
          new LocalisableString(curam.message.APPEALTAB.INF_DECISION_TIMELY);
        completionCountdownText.arg(appealCaseTabDetail.dtls.decisionDate
          .subtract(appealCaseTabDetail.dtls.creationDate));

      } else {
        // Appeals decision is overdue by the number of days.
        completionCountdownText =
          new LocalisableString(curam.message.APPEALTAB.INF_DECISION_OVERDUE);

        completionCountdownText.arg(daysBetweenCurrentAndDeadlineDate);
      }

      return completionCountdownText;
    } else {
      completionCountdownText =
        new LocalisableString(curam.message.APPEALTAB.INF_AWAITING_APPROVAL);
      return completionCountdownText;
    }

  }

  /**
   * Formats XML data for an integrated case member details list.
   *
   * @param appealCaseParticipantTabDetailList
   * Integrated case member details list.
   *
   * @return Stack content panel builder which has case member details.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  protected StackContainerBuilder getCaseMemberThumbnailDetails(
    final AppealCaseParticipantTabDetailList appealCaseParticipantTabDetailList)
    throws AppException, InformationalException {

    final StackContainerBuilder stackContainerBuilder =
      StackContainerBuilder.createStackContainer(
        GeneralAppealConstants.gkIDStackContainerThreeImages, false, true);

    final RotatorBuilder rotatorBuilder = RotatorBuilder.createRotator(3);

    rotatorBuilder.setQuickParam(false);
    rotatorBuilder.setWrapParam(false);

    AppealCaseParticipantTabDetail appealCaseParticipantTabDetail;

    final ContentPanelBuilder memberListContentPanelBuilder =
      ContentPanelBuilder.createPanel(CuramConst.gkPanelCaseMemberList);

    final ListBuilder memberListBuilderHeader = ListBuilder.createList(6);

    // BEGIN, 179767, AZ
    // Updating column widths for new UI
    memberListBuilderHeader.setColumnWidthPx(1, 40);
    memberListBuilderHeader.setColumnWidthPx(2, 120);
    memberListBuilderHeader.setColumnWidthPx(3, 120);
    memberListBuilderHeader.setColumnWidthPx(4, 120);
    memberListBuilderHeader.setColumnWidthPx(5, 120);
    // END, 179767

    memberListBuilderHeader.addColumnTitle(2,
      new LocalisableString(BPOCASETAB.INF_MEMBERS_LABEL));
    memberListBuilderHeader.addColumnTitle(4,
      new LocalisableString(BPOCASETAB.INF_MEMBER_RELATIONSHIP_LABEL));
    memberListBuilderHeader.addColumnTitle(5,
      new LocalisableString(BPOCASETAB.INF_AGE_LABEL));

    memberListContentPanelBuilder.addWidgetItem(memberListBuilderHeader,
      CuramConst.gkStyle, CuramConst.gkStyleSingleList);

    final ListBuilder memberListBuilder = ListBuilder.createList(5);

    // BEGIN, 179767, AZ
    // Updating column widths for new UI
    memberListBuilder.setColumnWidthPx(1, 40);
    memberListBuilder.setColumnWidthPx(2, 120);
    memberListBuilder.setColumnWidthPx(3, 120);
    memberListBuilder.setColumnWidthPx(4, 120);
    memberListBuilder.setColumnWidthPx(5, 120);
    // END, 179767

    for (int i = 0; i < appealCaseParticipantTabDetailList.dtls.size(); i++) {

      appealCaseParticipantTabDetail = new AppealCaseParticipantTabDetail();

      appealCaseParticipantTabDetail =
        appealCaseParticipantTabDetailList.dtls.item(i);

      final ContentPanelBuilder thumbnailContentPanelBuilder =
        getCaseMemberThumbnailDetails(appealCaseParticipantTabDetail);

      rotatorBuilder.addWidgetItem(thumbnailContentPanelBuilder,
        CuramConst.gkStyle, CuramConst.gkContentPanel);

      createMemberRow(memberListBuilder, appealCaseParticipantTabDetail,
        i + CuramConst.gkOne);

    }

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

  /**
   * Formats the XML data for a case member details.
   *
   * @param appealCaseParticipantTabDetail
   * Appeal case participant details.
   *
   * @return Content panel builder.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  protected ContentPanelBuilder getCaseMemberThumbnailDetails(
    final AppealCaseParticipantTabDetail appealCaseParticipantTabDetail)
    throws AppException, InformationalException {

    // Create a content panel using the image panel builder to build the xml
    // required.
    final ImagePanelBuilder imagePanelBuilder =
      ImagePanelBuilder.createPanel();

    if (appealCaseParticipantTabDetail.concernRoleType
      .equals(CONCERNROLETYPE.PERSON)) {

      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

      concernRoleKey.concernRoleID =
        appealCaseParticipantTabDetail.concernRoleID;

      // format special caution details
      curam.core.sl.impl.TabDetailsHelper
        .getSpecialCautionDetailsForImagePanel(concernRoleKey,
          imagePanelBuilder);

      final FormatPersonNameKey formatPersonNameKey =
        new FormatPersonNameKey();

      formatPersonNameKey.concernRoleID =
        appealCaseParticipantTabDetail.concernRoleID;
      formatPersonNameKey.wrapNameInd = true;

      final curam.core.intf.AlternateName alternateNameObj =
        AlternateNameFactory.newInstance();
      final AlternateNameReadByTypeKey alternateNameReadByTypeKey =
        new AlternateNameReadByTypeKey();

      alternateNameReadByTypeKey.concernRoleID =
        appealCaseParticipantTabDetail.concernRoleID;
      alternateNameReadByTypeKey.nameStatus = RECORDSTATUS.NORMAL;
      alternateNameReadByTypeKey.nameType = ALTERNATENAMETYPE.REGISTERED;

      final AlternateNameDtls alternateNameDtls =
        alternateNameObj.readByType(alternateNameReadByTypeKey);

      // BEGIN, 179767, AZ
      // Add CCV link to person image.
      imagePanelBuilder.addCCVLink(concernRoleKey.concernRoleID);
      // END, 179767

      imagePanelBuilder.addNameAsLink(alternateNameDtls.firstForename,
        alternateNameDtls.surname,
        appealCaseParticipantTabDetail.concernRoleID);
    } else {
      imagePanelBuilder.addNameAsLink(
        appealCaseParticipantTabDetail.concernRoleName, CuramConst.gkEmpty,
        appealCaseParticipantTabDetail.concernRoleID);
    }

    // Check to see if Participant has an image
    final ConcernRoleImage concernRoleImageObj =
      ConcernRoleImageFactory.newInstance();
    String participantImage = CuramConst.gkEmpty;
    String imageResource = CuramConst.gkEmpty;

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID =
      appealCaseParticipantTabDetail.concernRoleID;
    ConcernRoleTypeDetails concernRoleTypeDetails =
      new ConcernRoleTypeDetails();

    if (concernRoleKey.concernRoleID == 0) {
      participantImage = GeneralAppealConstants.gkOrganisationIcon;
      imageResource = GeneralAppealConstants.gkRendererImages;
    } else {
      concernRoleTypeDetails =
        ConcernRoleFactory.newInstance().readConcernRoleType(concernRoleKey);

      if (concernRoleImageObj.hasImage(concernRoleKey).statusInd) {

        participantImage = CuramConst.gkCaseMemberFileDownLoadLink
          + String.valueOf(appealCaseParticipantTabDetail.concernRoleID);
        imageResource = CuramConst.gkEmpty;

      } else {

        if (concernRoleTypeDetails.concernRoleType
          .equals(CONCERNROLETYPE.PERSON)) {

          final PersonKey personKey = new PersonKey();

          personKey.concernRoleID =
            appealCaseParticipantTabDetail.concernRoleID;

          final ReadGenderDetails readGenderDetails =
            PersonFactory.newInstance().readGender(personKey);

          if (readGenderDetails.gender.equals(GENDER.MALE)) {
            participantImage = CuramConst.gkGenericMale;
          } else {
            participantImage = CuramConst.gkGenericFemale;
          }
          imageResource = CuramConst.gkRendererImages;

        } else if (concernRoleTypeDetails.concernRoleType
          .equals(CONCERNROLETYPE.PROSPECTPERSON)) {

          final ProspectPersonKey prospectPersonKey = new ProspectPersonKey();

          prospectPersonKey.concernRoleID =
            appealCaseParticipantTabDetail.concernRoleID;

          final ReadProspectPersonGenderDetails ppGenderDetails =
            ProspectPersonFactory.newInstance()
              .readProspectPersonGender(prospectPersonKey);

          if (ppGenderDetails.gender.equals(GENDER.MALE)) {
            participantImage = CuramConst.gkGenericMale;
          } else {
            participantImage = CuramConst.gkGenericFemale;
          }
          imageResource = CuramConst.gkRendererImages;
        } else {

          participantImage = GeneralAppealConstants.gkOrganisationIcon;
          imageResource = GeneralAppealConstants.gkRendererImages;
        }
      }
    }

    // BEGIN, 187556, DR
    // Add alt text to Person image in Appeals context panel
    final LocalisableString altText =
      new LocalisableString(curam.message.APPEALTAB.INF_IMAGE_ALT_TEXT);

    final String imageIdentifier = imagePanelBuilder.addParticipantImage(
      participantImage, altText.toClientFormattedText(), imageResource);
    // END, 187556

    if (appealCaseParticipantTabDetail.concernRoleType
      .equals(CONCERNROLETYPE.PERSON)) {

      // Display Person's age and relationship to appeal
      final PersonAgeDetails personAgeDetails = new PersonAgeDetails();

      personAgeDetails.dateOfCalculation = Date.getCurrentDate();
      personAgeDetails.personID =
        appealCaseParticipantTabDetail.concernRoleID;
      personAgeDetails.indDisplayDateOfBirth = false;

      final TabDetailFormatterInterface tabDetailFormatterInterface =
        (TabDetailFormatterInterface) TabDetailFormatterFactory.newInstance();

      final String ageString =
        tabDetailFormatterInterface.formatAge(personAgeDetails).ageString;

      // BEGIN, 179767, AZ
      // Swap order of age and relationship text, and add a divider between
      // them.
      imagePanelBuilder.addRelationshipText(
        getParticipantRole(appealCaseParticipantTabDetail.primaryClientInd,
          appealCaseParticipantTabDetail.caseParticipantTypeCode)
            .toClientFormattedText());

      imagePanelBuilder.addDivider(null);

      imagePanelBuilder.addAge(ageString, CuramConst.gkAge);
      // END, 179767
    } else {
      // just display relationship to appeal

      imagePanelBuilder.addRelationshipText(
        getParticipantRole(appealCaseParticipantTabDetail.primaryClientInd,
          appealCaseParticipantTabDetail.caseParticipantTypeCode)
            .toClientFormattedText(),
        GeneralAppealConstants.gkRelationshipOnly);
    }

    final ContactTabDetails contactTabDetails = new ContactTabDetails();

    contactTabDetails.addressData =
      appealCaseParticipantTabDetail.addressData;
    contactTabDetails.concernRoleName =
      appealCaseParticipantTabDetail.concernRoleName;
    contactTabDetails.emailAddress =
      appealCaseParticipantTabDetail.emailAddress;
    contactTabDetails.phoneNumberID =
      appealCaseParticipantTabDetail.phoneNumberID;
    contactTabDetails.primaryAlternateID =
      appealCaseParticipantTabDetail.primaryAlternateID;

    // BEGIN, CR00288724, MC
    TooltipBuilder contactTooltipBuilder;

    contactTooltipBuilder =
      curam.core.sl.impl.TabDetailsHelper.getParticipantContactDetailsTooltip(
        contactTabDetails, imageIdentifier);

    contactTooltipBuilder
      .setOrientation(CuramConst.gkPersonTooltipOrientation);

    imagePanelBuilder.addTooltipItem(contactTooltipBuilder);

    // END, CR00288724

    if (appealCaseParticipantTabDetail.concernRoleType
      .equals(CONCERNROLETYPE.PERSON)) {

      final TabDetailsHelper tabDetailsHelperObj =
        TabDetailsHelperFactory.newInstance();

      // read incident and role details
      final IncidentAndRoleTabDetailsList incidentList =
        tabDetailsHelperObj.readIncidentTabDetails(concernRoleKey);

      final InvestigationAndRoleTabDetailsList investigationList =
        tabDetailsHelperObj.readInvestigationTabDetails(concernRoleKey);

      if (incidentList.dtls.size() > 0) {

        CaseTabDetailsHelper.getMemberIncidentDetailsForImagePanel(
          concernRoleKey, incidentList, imagePanelBuilder);
      }

      if (investigationList.dtls.size() > 0) {
        CaseTabDetailsHelper.getMemberInvestigationDetailsForImagePanel(
          concernRoleKey, investigationList, imagePanelBuilder);
      }

    }

    return imagePanelBuilder.getImagePanel();
  }

  /**
   * Creates and populates case member list row with member details.
   *
   * @param memberListBuilder
   * Case member list builder to create and populate row.
   * @param appealCaseParticipantTabDetail
   * Appeal case participant tab details.
   * @param rowNo
   * Row number to insert into list.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  protected void createMemberRow(final ListBuilder memberListBuilder,
    final AppealCaseParticipantTabDetail appealCaseParticipantTabDetail,
    final int rowNo) throws AppException, InformationalException {

    final TabDetailFormatter tabDetailFormatterObj =
      TabDetailFormatterFactory.newInstance();

    // create member list row
    memberListBuilder.addRow();

    // BEGIN, CR00467295, PU
    // Add Persons Name
    final String contentPanelID = CuramConst.gkCaseMemberListContenPanelID
      + String.valueOf(appealCaseParticipantTabDetail.concernRoleID);

    final ContentPanelBuilder nameContentPanelBuilder =
      ContentPanelBuilder.createPanelWithID(contentPanelID);

    String name;

    // CR00467295 - Create citizen context viewer link only for person
    // participant roles, and not for product providers.
    if (CONCERNROLETYPE.PERSON
      .equals(appealCaseParticipantTabDetail.concernRoleType)) {
      // Add Citizen Context Viewer link
      final LinkBuilder ccvLinkBuilder = LinkBuilder.createCCVLink(
        CuramConst.gkEmpty, appealCaseParticipantTabDetail.concernRoleID);
      final LocalisableString altText =
        new LocalisableString(IMAGEPANELBUILDER.INF_CCV_VIEWER);

      ccvLinkBuilder.addImageWithAltTextMessage(CuramConst.gkIconCCV,
        altText.toClientFormattedText());
      ccvLinkBuilder.setTextResource(CuramConst.gkRendererImages);

      final RendererConfig linkRendererConfig =
        new RendererConfig(RendererConfigType.STYLE, CuramConst.gkLink);

      memberListBuilder.addEntry(1, rowNo, ccvLinkBuilder,
        linkRendererConfig);

      // END, CR00467295
      final FormatPersonNameKey formatPersonNameKey =
        new FormatPersonNameKey();

      formatPersonNameKey.wrapNameInd = false;
      formatPersonNameKey.concernRoleID =
        appealCaseParticipantTabDetail.concernRoleID;

      name = tabDetailFormatterObj.formatPersonName(formatPersonNameKey).name;

    } else {
      name = appealCaseParticipantTabDetail.concernRoleName;
    }

    final LinkBuilder memberLinkBuilder =
      LinkBuilder.createLink(name, CuramConst.gkParticipantHomePage,
        memberListBuilder.getWidgetDocument());

    memberLinkBuilder.addParameter(CuramConst.gkPageParameterConcernRoleID,
      String.valueOf(appealCaseParticipantTabDetail.concernRoleID));

    final LocalisableString caseMemberTitle =
      new LocalisableString(APPEALTAB.INF_CASEMEMBER_TITLE);

    memberLinkBuilder.addLinkTitle(caseMemberTitle);

    nameContentPanelBuilder.addLinkItem(memberLinkBuilder);

    final ContactTabDetails contactTabDetails = new ContactTabDetails();

    contactTabDetails.addressData =
      appealCaseParticipantTabDetail.addressData;
    contactTabDetails.concernRoleName =
      appealCaseParticipantTabDetail.concernRoleName;
    contactTabDetails.emailAddress =
      appealCaseParticipantTabDetail.emailAddress;
    contactTabDetails.phoneNumberID =
      appealCaseParticipantTabDetail.phoneNumberID;
    contactTabDetails.primaryAlternateID =
      appealCaseParticipantTabDetail.primaryAlternateID;

    // BEGIN, CR00288724, MC
    TooltipBuilder contactTooltipBuilder;

    contactTooltipBuilder = curam.core.sl.impl.TabDetailsHelper
      .getParticipantContactDetailsTooltip(contactTabDetails, contentPanelID);

    nameContentPanelBuilder.addTooltipItem(contactTooltipBuilder);
    // END, CR00288724

    final RendererConfig contentPanelRendererConfig =
      new RendererConfig(RendererConfigType.STYLE, CuramConst.gkContentPanel);

    memberListBuilder.addEntry(2, rowNo, nameContentPanelBuilder,
      contentPanelRendererConfig);

    final ContentPanelBuilder personNamePanel =
      ContentPanelBuilder.createPanel(CuramConst.gkPersonNameIcons);

    if (appealCaseParticipantTabDetail.concernRoleType
      .equals(CONCERNROLETYPE.PERSON)) {

      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

      concernRoleKey.concernRoleID =
        appealCaseParticipantTabDetail.concernRoleID;

      // BEGIN, 179767, AZ - Add "list-icon" CSS class
      // Add special caution details
      personNamePanel.addWidgetItem(
        curam.core.sl.impl.TabDetailsHelper
          .getSpecialCautionDetails(concernRoleKey),
        CuramConst.gkStyle, CuramConst.gkContentPanel, CuramConst.gkListIcon);
      // END, 179767

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
          CuramConst.gkStyle, CuramConst.gkContentPanel,
          CuramConst.gkListIcon);
      }

      if (investigationList.dtls.size() > CuramConst.gkZero) {

        personNamePanel.addWidgetItem(
          CaseTabDetailsHelper.getMemberInvestigationDetails(concernRoleKey,
            investigationList),
          CuramConst.gkStyle, CuramConst.gkContentPanel,
          CuramConst.gkListIcon);
      }

      memberListBuilder.addEntry(3, rowNo, personNamePanel,
        contentPanelRendererConfig);
    }

    memberListBuilder.addEntry(4, rowNo,
      getParticipantRole(appealCaseParticipantTabDetail.primaryClientInd,
        appealCaseParticipantTabDetail.caseParticipantTypeCode));

    final PersonAgeDetails personAgeDetails = new PersonAgeDetails();

    if (appealCaseParticipantTabDetail.concernRoleType
      .equals(CONCERNROLETYPE.PERSON)) {

      // Add age
      personAgeDetails.dateOfCalculation = Date.getCurrentDate();
      personAgeDetails.personID =
        appealCaseParticipantTabDetail.concernRoleID;
      personAgeDetails.indDisplayDateOfBirth = false;

      final TabDetailFormatterInterface tabDetailFormatterInterface =
        (TabDetailFormatterInterface) TabDetailFormatterFactory.newInstance();

      final LocalisableString ageString = tabDetailFormatterInterface
        .formatAge(personAgeDetails).localisableAgeString;

      memberListBuilder.addEntry(5, rowNo, ageString);
    }
  }

  @Override
  protected AppealCaseParticipantTabDetailList setOrganisationAppellant(
    final AppealCaseParticipantTabDetailList participantList) {

    boolean orgIsAppellant = false;

    for (int i = 0; i < participantList.dtls.size(); i++) {

      if (participantList.dtls.item(i).primaryClientInd
        && participantList.dtls.item(i).caseParticipantTypeCode
          .equals(CASEPARTICIPANTROLETYPE.RESPONDENT)) {
        orgIsAppellant = true;
      }
    }
    if (orgIsAppellant) {
      for (int i = 0; i < participantList.dtls.size(); i++) {
        if (participantList.dtls.item(i).caseParticipantTypeCode
          .equals(CASEPARTICIPANTROLETYPE.PRODUCTPROVIDER)) {

          // Set the value of the case participant role type for display
          participantList.dtls.item(i).caseParticipantTypeCode =
            CASEPARTICIPANTROLETYPE.APPELLANT;

        }
      }
    }
    return participantList;
  }

  @Override
  protected AppealCaseParticipantTabDetailList removeDuplicates(
    final AppealCaseParticipantTabDetailList participantList) {

    final int participantListSize = participantList.dtls.size();
    // BEGIN, CR00286404, DK
    // list of filtered participants
    final AppealCaseParticipantTabDetailList filteredParticipantList =
      new AppealCaseParticipantTabDetailList();

    for (int i = 0; i < participantListSize; i++) {
      // indicator to see if there is a duplicate
      boolean duplicateInd = false;

      for (int x = 0; x < filteredParticipantList.dtls.size(); x++) {
        if (participantList.dtls
          .item(i).concernRoleID == filteredParticipantList.dtls
            .item(x).concernRoleID) {
          duplicateInd = true;
          break;
        }
      } // end for filteredParticipantList
      if (!duplicateInd) {
        filteredParticipantList.dtls.add(participantList.dtls.item(i));
      }
    }
    return filteredParticipantList;
    // END, CR00286404
  }

  protected ContentPanelBuilder getRFRDetails(
    final AppealCaseTabDetail appealCaseTabDetail,
    final BDMAppealDtls bdmAppealDetails)
    throws AppException, InformationalException {

    final ContentPanelBuilder contentPanelBuilder =
      ContentPanelBuilder.createPanel("appeal-rfr-details-panel");

    contentPanelBuilder.addRoundedCorners();

    final ListBuilder creationDetails = ListBuilder.createHorizontalList(2);

    creationDetails.addRow();
    creationDetails.addEntry(1, 1, "ADMS Reference");
    creationDetails.addEntry(2, 1, bdmAppealDetails.admsReference);

    creationDetails.addRow();
    creationDetails.addEntry(1, 2,
      "Date Request of Reconsideration Received");
    creationDetails.addEntry(2, 2, bdmAppealDetails.dateRFRReceived);

    creationDetails.addRow();
    creationDetails.addEntry(1, 3, "Date of Decision Disputed");
    creationDetails.addEntry(2, 3, bdmAppealDetails.dateDecisionDisputed);

    contentPanelBuilder.addSingleListItem(creationDetails,
      "rfr-details-table");

    return contentPanelBuilder;
  }

}
