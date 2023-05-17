package curam.ca.gc.bdm.sl.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMRFRSISSUE;
import curam.cefwidgets.docbuilder.impl.ContentPanelBuilder;
import curam.cefwidgets.docbuilder.impl.ImageBuilder;
import curam.cefwidgets.docbuilder.impl.LinkBuilder;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.LOCATIONACCESSTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.DataBasedSecurity;
import curam.core.impl.EnvVars;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.intf.AlternateName;
import curam.core.sl.fact.ClientMergeFactory;
import curam.core.sl.fact.TabDetailFormatterFactory;
import curam.core.sl.impl.TabDetailFormatterInterface;
import curam.core.sl.intf.ClientMerge;
import curam.core.sl.intf.TabDetailFormatter;
import curam.core.sl.struct.AddressTabDetails;
import curam.core.sl.struct.ConcernRoleIDKey;
import curam.core.sl.struct.FormatPersonNameKey;
import curam.core.sl.struct.ParticipantContentDetailsXML;
import curam.core.sl.struct.ParticipantSecurityCheckKey;
import curam.core.sl.struct.PersonAgeDetails;
import curam.core.sl.struct.PersonLinksDetails;
import curam.core.sl.struct.PersonNameString;
import curam.core.sl.struct.PersonTabDetails;
import curam.core.sl.struct.ReferenceDetailsKey;
import curam.core.struct.AlternateIDAndTypeCodeDtls;
import curam.core.struct.AlternateIDAndTypeCodeDtlsList;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.AlternateNameReadByTypeKey;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.CuramInd;
import curam.core.struct.DataBasedSecurityResult;
import curam.core.struct.MaintainConcernRoleKey;
import curam.core.struct.PersonTabDtls;
import curam.core.struct.PersonTabDtlsKey;
import curam.core.struct.PhoneNumberKey;
import curam.core.struct.ProspectPersonTabDtls;
import curam.core.struct.ProspectPersonTabDtlsKey;
import curam.message.BPOPARTICIPANT;
import curam.message.GENERALCASE;
import curam.message.GENERALCONCERN;
import curam.message.SUMMARYDETAILS;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import static java.net.URLDecoder.decode;

public class BDMParticipantTabProcess
  extends curam.ca.gc.bdm.sl.base.BDMParticipantTabProcess {

  @Override
  public ParticipantContentDetailsXML
    readPerson(final MaintainConcernRoleKey key)
      throws AppException, InformationalException {

    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();
    final ParticipantSecurityCheckKey participantSecurityCheckKey =
      new ParticipantSecurityCheckKey();
    participantSecurityCheckKey.participantID = key.concernRoleID;
    participantSecurityCheckKey.type = LOCATIONACCESSTYPE.READ;
    final DataBasedSecurityResult dataBasedSecurityResult =
      dataBasedSecurity.checkParticipantSecurity(participantSecurityCheckKey);

    if (!dataBasedSecurityResult.result) {
      if (dataBasedSecurityResult.readOnly)
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);
      if (dataBasedSecurityResult.restricted)
        throw new AppException(GENERALCONCERN.ERR_CONCERNROLE_FV_SENSITIVE);
      throw new AppException(
        GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
    }

    final ParticipantContentDetailsXML participantContentDetailsXML =
      new ParticipantContentDetailsXML();

    final PersonTabDetails personTabDetails = new PersonTabDetails();

    final TabDetailFormatter tabDetailFormatterObj =
      TabDetailFormatterFactory.newInstance();

    final PersonTabDtlsKey personTabDtlsKey = new PersonTabDtlsKey();
    personTabDtlsKey.concernRoleID = key.concernRoleID;

    final PersonTabDtls personTabDtls =
      ConcernRoleFactory.newInstance().readPerson(personTabDtlsKey);
    personTabDetails.assign(personTabDtls);

    final FormatPersonNameKey formatPersonNameKey = new FormatPersonNameKey();
    formatPersonNameKey.concernRoleID = key.concernRoleID;

    final PersonNameString personNameString =
      tabDetailFormatterObj.formatPersonName(formatPersonNameKey);
    personTabDetails.name = personNameString.name;
    participantContentDetailsXML.participantName = personNameString.name;

    final AddressTabDetails addressTabDetails = new AddressTabDetails();
    String decodedAddressData;
    try {
      decodedAddressData =
        decode(personTabDtls.addressData, StandardCharsets.UTF_8.toString());
    } catch (final UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    addressTabDetails.addressData = decodedAddressData;
    personTabDetails.address =
      tabDetailFormatterObj.formatAddress(addressTabDetails).addressString;
    if (personTabDtls.phoneNumberID != 0L) {
      final PhoneNumberKey phoneNumberKey = new PhoneNumberKey();
      phoneNumberKey.phoneNumberID = personTabDtls.phoneNumberID;
      personTabDetails.phoneNumberString = tabDetailFormatterObj
        .formatPhoneNumber(phoneNumberKey).phoneNumberString;
    }

    final ReferenceDetailsKey referenceDetailsKey = new ReferenceDetailsKey();
    referenceDetailsKey.concernRoleAlternateID =
      getReferenceAlternateIDUsingConcernRoleID(key.concernRoleID);
    personTabDetails.reference = tabDetailFormatterObj
      .formatReference(referenceDetailsKey).concernRoleAlternateIDDetails;

    final PersonAgeDetails personAgeDetails = new PersonAgeDetails();
    personAgeDetails.dateOfCalculation = Date.getCurrentDate();
    personAgeDetails.personID = key.concernRoleID;
    personAgeDetails.indDisplayDateOfBirth = true;

    final TabDetailFormatterInterface tabDetailFormatterInterface =
      (TabDetailFormatterInterface) TabDetailFormatterFactory.newInstance();
    personTabDetails.ageDetailString =
      tabDetailFormatterInterface.formatAge(personAgeDetails).ageString;

    final PersonLinksDetails personLinksDetails = new PersonLinksDetails();
    personLinksDetails.concernRoleID = key.concernRoleID;

    int numLinks = 0;
    if (Configuration.getBooleanProperty(EnvVars.ENV_APPEALS_ISINSTALLED)) {
      final ConcernRoleIDKey concernRoleIDKey = new ConcernRoleIDKey();
      concernRoleIDKey.concernRoleID = key.concernRoleID;
      personTabDetails.numAppeals = getCountWithReflection(
        "countActiveAppealsByRoleParticipantID", concernRoleIDKey);
      personTabDetails.numLegalActions = getCountWithReflection(
        "countActiveLegalActionsByParticipantID", concernRoleIDKey);
    } else {
      personTabDetails.numAppeals = 0;
      personTabDetails.numLegalActions = 0;
    }
    personLinksDetails.numAppeals = personTabDetails.numAppeals;
    if (personTabDetails.numAppeals != 0)
      numLinks++;
    personLinksDetails.numLegalActions = personTabDetails.numLegalActions;
    if (personTabDetails.numLegalActions != 0)
      numLinks++;
    personLinksDetails.numIncidents = personTabDetails.numIncidents;
    if (personTabDetails.numIncidents != 0)
      numLinks++;
    personLinksDetails.numInvestigations = personTabDetails.numInvestigations;
    if (personTabDetails.numInvestigations != 0)
      numLinks++;
    personLinksDetails.numIssues = personTabDetails.numIssues;
    if (personTabDetails.numIssues != 0)
      numLinks++;
    personLinksDetails.numLinks = numLinks;
    personTabDetails.concernRoleID = key.concernRoleID;

    final ContentPanelBuilder containerPanel =
      ContentPanelBuilder.createPanel("person-container-panel");

    final ContentPanelBuilder leftPanel =
      getPersonThumbnailDetails(personTabDetails);

    // START :TASK 5347 CONTEXT PANEL NAME CHANGE PREFFERED NAME :JP
    final ContentPanelBuilder centrePanel =
      getCustomPersonDetails(personTabDetails);

    // RFR Changes : Add RFR message on the context panel
    if (BDMUtil.countAppealCasesByParticipantID(key.concernRoleID) > 0) {
      final LocalisableString apString =
        new LocalisableString(BDMRFRSISSUE.INFO_BDM_RFR_CONTEXT_MESSAGE);
      centrePanel.addlocalisableStringItem(apString.toClientFormattedText(),
        "content-bdm-rfr-info-dtls");
    }

    // END : CONTEXT PANEL NAME CHANGE :JP
    final ClientMerge clientMergeObj = ClientMergeFactory.newInstance();

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = key.concernRoleID;

    final CuramInd duplicateInd =
      clientMergeObj.isConcernRoleDuplicate(concernRoleKey);
    if (duplicateInd.statusInd) {
      final LocalisableString duplicateString =
        new LocalisableString(BPOPARTICIPANT.INF_DUPLICATE);
      centrePanel.addlocalisableStringItem(
        duplicateString.toClientFormattedText(), "watermark");
    } else if (!personTabDtls.dateOfDeath.isZero()) {
      final LocalisableString deceasedString =
        new LocalisableString(BPOPARTICIPANT.INF_DECEASED);
      centrePanel.addlocalisableStringItem(
        deceasedString.toClientFormattedText(), "watermark");
    }

    containerPanel.addWidgetItem(leftPanel, "style", "content-panel",
      "content-panel-detail person-image-panel");
    if (personLinksDetails.numLinks != 0) {
      final ContentPanelBuilder linksPanel =
        getPersonLinks(personLinksDetails);
      if (personLinksDetails.numLinks <= 3) {
        containerPanel.addWidgetItem(centrePanel, "style", "content-panel",
          "content-panel-detail person-details-panel-onecol");
        containerPanel.addWidgetItem(linksPanel, "style", "content-panel",
          "content-panel-detail person-links-panel-onecol");
      } else {
        containerPanel.addWidgetItem(centrePanel, "style", "content-panel",
          "content-panel-detail person-details-panel");
        containerPanel.addWidgetItem(linksPanel, "style", "content-panel",
          "content-panel-detail person-links-panel");
      }
    } else {
      containerPanel.addWidgetItem(centrePanel, "style", "content-panel",
        "content-panel-detail person-details-panel-no-links");
    }
    participantContentDetailsXML.xmlPanelData = containerPanel.toString();
    return participantContentDetailsXML;

  }

  @Override
  public ParticipantContentDetailsXML
    readProspectPerson(final MaintainConcernRoleKey key)
      throws AppException, InformationalException {

    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();
    final ParticipantSecurityCheckKey participantSecurityCheckKey =
      new ParticipantSecurityCheckKey();

    participantSecurityCheckKey.participantID = key.concernRoleID;
    participantSecurityCheckKey.type = LOCATIONACCESSTYPE.READ;

    final DataBasedSecurityResult dataBasedSecurityResult =
      dataBasedSecurity.checkParticipantSecurity(participantSecurityCheckKey);

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

    final ParticipantContentDetailsXML participantContentDetailsXML =
      new ParticipantContentDetailsXML();

    final PersonTabDetails personTabDetails = new PersonTabDetails();
    final TabDetailFormatter tabDetailFormatterObj =
      TabDetailFormatterFactory.newInstance();

    final ProspectPersonTabDtlsKey prospectPersonTabDtlsKey =
      new ProspectPersonTabDtlsKey();

    prospectPersonTabDtlsKey.concernRoleID = key.concernRoleID;

    final ProspectPersonTabDtls prospectPersonTabDtls = ConcernRoleFactory
      .newInstance().readProspectPerson(prospectPersonTabDtlsKey);

    personTabDetails.assign(prospectPersonTabDtls);

    // format Person Name
    final FormatPersonNameKey formatPersonNameKey = new FormatPersonNameKey();

    formatPersonNameKey.concernRoleID = key.concernRoleID;

    final PersonNameString personNameString =
      tabDetailFormatterObj.formatPersonName(formatPersonNameKey);

    final String sinNumber = BDMUtil.getAlternateIDByConcernRoleIDType(
      key.concernRoleID, CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);

    final String prefNameAndSIN =
      String.format("%s %s %s", personNameString.name,
        Objects.nonNull(sinNumber) && !sinNumber.isEmpty() ? " | " : "",
        sinNumber);
    personTabDetails.name = prefNameAndSIN;
    participantContentDetailsXML.participantName = personNameString.name;

    // format Person Address
    final AddressTabDetails addressTabDetails = new AddressTabDetails();

    addressTabDetails.addressData = prospectPersonTabDtls.addressData;

    personTabDetails.address =
      tabDetailFormatterObj.formatAddress(addressTabDetails).addressString;

    // format Phone Number
    if (prospectPersonTabDtls.phoneNumberID != 0) {

      final PhoneNumberKey phoneNumberKey = new PhoneNumberKey();

      phoneNumberKey.phoneNumberID = prospectPersonTabDtls.phoneNumberID;

      personTabDetails.phoneNumberString = tabDetailFormatterObj
        .formatPhoneNumber(phoneNumberKey).phoneNumberString;
    }

    final ReferenceDetailsKey referenceDetailsKey = new ReferenceDetailsKey();
    referenceDetailsKey.concernRoleAlternateID =
      getReferenceAlternateIDUsingConcernRoleID(key.concernRoleID);
    personTabDetails.reference = tabDetailFormatterObj
      .formatReference(referenceDetailsKey).concernRoleAlternateIDDetails;

    // Set up Age Details String
    final PersonAgeDetails personAgeDetails = new PersonAgeDetails();

    personAgeDetails.dateOfCalculation = Date.getCurrentDate();
    personAgeDetails.personID = key.concernRoleID;
    personAgeDetails.indDisplayDateOfBirth = true;

    final TabDetailFormatterInterface tabDetailFormatterInterface =
      (TabDetailFormatterInterface) TabDetailFormatterFactory.newInstance();

    personTabDetails.ageDetailString =
      tabDetailFormatterInterface.formatAge(personAgeDetails).ageString;

    final PersonLinksDetails personLinksDetails = new PersonLinksDetails();

    personLinksDetails.concernRoleID = key.concernRoleID;
    personLinksDetails.numInvestigations = personTabDetails.numInvestigations;
    if (personTabDetails.numInvestigations != 0) {
      personLinksDetails.numLinks++;
    }

    personTabDetails.concernRoleID = key.concernRoleID;

    // Create a container content panel
    final ContentPanelBuilder containerPanel =
      ContentPanelBuilder.createPanel(CuramConst.gkContainerPanelPerson);

    // Build left panel
    final ContentPanelBuilder leftPanel =
      getPersonThumbnailDetails(personTabDetails);

    // Build center panel
    final ContentPanelBuilder centrePanel =
      getPersonDetails(personTabDetails);

    // Check to see if Prospect Person is a duplicate
    // Client merge manipulation variables
    final ClientMerge clientMergeObj = ClientMergeFactory.newInstance();

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    // Set the concern role key
    concernRoleKey.concernRoleID = key.concernRoleID;

    final CuramInd duplicateInd =
      clientMergeObj.isConcernRoleDuplicate(concernRoleKey);

    // Check if the concern role has been marked as a duplicate
    if (duplicateInd.statusInd) {

      final LocalisableString duplicateString =
        new LocalisableString(BPOPARTICIPANT.INF_PROSPECT_DUPLICATE);

      centrePanel.addlocalisableStringItem(
        duplicateString.toClientFormattedText(),
        CuramConst.gkProspectDupWaterMark);
    } else {
      // If not a duplicate, just mark as a prospect
      final LocalisableString prospectString =
        new LocalisableString(BPOPARTICIPANT.INF_PROSPECT);

      centrePanel.addlocalisableStringItem(
        prospectString.toClientFormattedText(), CuramConst.gkWaterMark);
    }

    // Add panels to container
    containerPanel.addWidgetItem(leftPanel, CuramConst.gkStyle,
      CuramConst.gkContentPanel, CuramConst.gkPersonImagePanel);

    if (personLinksDetails.numLinks != 0) {
      final ContentPanelBuilder linksPanel =
        getPersonLinks(personLinksDetails);

      // we have links so add middle panel with style that leaves room
      // for the links panel
      containerPanel.addWidgetItem(centrePanel, CuramConst.gkStyle,
        CuramConst.gkContentPanel, CuramConst.gkPersonDetailsPanel);

      containerPanel.addWidgetItem(linksPanel, CuramConst.gkStyle,
        CuramConst.gkContentPanel, CuramConst.gkPersonLinksPanel);

    } else {

      // we don't have links so add middle panel with style that spans the rest
      // of content panel
      containerPanel.addWidgetItem(centrePanel, CuramConst.gkStyle,
        CuramConst.gkContentPanel, CuramConst.gkPersonDetailsPanelNoLinks);
    }

    participantContentDetailsXML.xmlPanelData = containerPanel.toString();

    return participantContentDetailsXML;

  }

  /**
   * Create center panel for Person context panel
   *
   * @param PersonTabDetails
   * @return contentPanelBuilder
   * @throws AppException
   * @throws InformationalException
   */

  private ContentPanelBuilder

    getCustomPersonDetails(final PersonTabDetails details)
      throws AppException, InformationalException {

    final ContentPanelBuilder contentPanelBuilder =
      ContentPanelBuilder.createPanel("tab-details");
    contentPanelBuilder.addRoundedCorners();
    final ContentPanelBuilder namePanelBuilder =
      ContentPanelBuilder.createPanel("name-details");
    final LocalisableString nameLabel =
      new LocalisableString(SUMMARYDETAILS.INFO_NAME);
    namePanelBuilder.addlocalisableStringItem(
      nameLabel.toClientFormattedText(), "hidden-screenreader-label");

    // START :TASK 5347 CONTEXT PANEL NAME CHANGE PREFFERED NAME :JP

    // BUG 103577 - START - MR
    final BDMUtil bdmUtil = new BDMUtil();
    final String sinNumber =
      bdmUtil.getSINNumberForPerson(details.concernRoleID);
    // BUG 103577 - END - MR

    final String prefName = getPersonPreferredName(details);
    final String prefNameAndSIN = String.format("%s %s %s", prefName,
      Objects.nonNull(sinNumber) && !sinNumber.isEmpty() ? " | " : "",
      sinNumber);

    namePanelBuilder.addStringItem(prefNameAndSIN, "content-name");

    // END : TASK 5347 CONTEXT PANEL NAME CHANGE PREFFERED NAME :JP

    contentPanelBuilder.addWidgetItem(namePanelBuilder, "style",
      "content-panel", "content-name");
    final ContentPanelBuilder referencePanelBuilder =
      ContentPanelBuilder.createPanel("ref-details");
    final LocalisableString referenceLabel =
      new LocalisableString(GENERALCONCERN.INF_REFERENCE_NUMBER);
    referencePanelBuilder.addlocalisableStringItem(
      referenceLabel.toClientFormattedText(), "hidden-screenreader-label");
    referencePanelBuilder.addStringItem(details.reference,
      "content-participant-id");
    contentPanelBuilder.addWidgetItem(referencePanelBuilder, "style",
      "content-panel", "content-participant-id");
    contentPanelBuilder.addStringItem(details.address, "content-address");
    String mapEnabled =
      Configuration.getProperty(EnvVars.ENV_GEOCODE_ENABLED);
    if (mapEnabled == null)
      mapEnabled = "NO";
    if (mapEnabled.equalsIgnoreCase(EnvVars.ENV_VALUE_YES)) {
      final LocalisableString mapString =
        new LocalisableString(BPOPARTICIPANT.INF_MAP);
      final LinkBuilder googleLinkBuilder = LinkBuilder.createLocalizableLink(
        mapString.toClientFormattedText(), "Address_mapPage.do");
      googleLinkBuilder.addParameter("addressID",
        String.valueOf(details.addressID));
      googleLinkBuilder.openAsModal();
      googleLinkBuilder.addImage("IconMap", "");
      googleLinkBuilder
        .setTextResource("curam.widget.render.infrastructure.i18n.Image");
      contentPanelBuilder.addWidgetItem(googleLinkBuilder, "style", "link",
        "content-map");
    }
    contentPanelBuilder.addCodetableItem(details.gender, "GENDER_CODE",
      "content-sex");
    contentPanelBuilder.addlocalisableStringItem(details.ageDetailString,
      "content-age");
    final ContentPanelBuilder contactContents =
      ContentPanelBuilder.createPanel("content-contacts");
    final ContentPanelBuilder phoneContents =
      ContentPanelBuilder.createPanel("phone-details");
    final ImageBuilder phoneIconImageBuilder =
      ImageBuilder.createImage("IconPhone", "");
    phoneIconImageBuilder
      .setImageResource("curam.widget.render.infrastructure.i18n.Image");
    final String phoneAltText =
      new LocalisableString(BPOPARTICIPANT.INF_PHONE_NUMBER)
        .toClientFormattedText();
    phoneIconImageBuilder.setImageAltText(phoneAltText);
    phoneContents.addImageItem(phoneIconImageBuilder);
    final LocalisableString phoneLabel =
      new LocalisableString(BPOPARTICIPANT.INF_PHONE_NUMBER);
    phoneContents.addlocalisableStringItem(phoneLabel.toClientFormattedText(),
      "hidden-screenreader-label");
    if (details.phoneNumberString.length() != 0) {
      phoneContents.addStringItem(details.phoneNumberString, "phonenumber");
    } else {
      final LocalisableString detailsNotRecorded =
        new LocalisableString(BPOPARTICIPANT.INF_NOT_RECORDED);
      phoneContents.addlocalisableStringItem(
        detailsNotRecorded.toClientFormattedText(),
        "not-recorded phonenumber");
    }
    contactContents.addWidgetItem(phoneContents, "style", "content-panel");
    final ContentPanelBuilder emailContents =
      ContentPanelBuilder.createPanel("email-details");
    final ImageBuilder emailIconImageBuilder =
      ImageBuilder.createImage("IconEmail", "");
    final String emailAltText =
      new LocalisableString(BPOPARTICIPANT.INF_EMAIL_ADDRESS)
        .toClientFormattedText();
    final LocalisableString emailAddressLabel =
      new LocalisableString(BPOPARTICIPANT.INF_EMAIL_ADDRESS);
    emailContents.addlocalisableStringItem(
      emailAddressLabel.toClientFormattedText(), "hidden-screenreader-label");
    if (details.emailAddress.length() != 0) {
      final LinkBuilder linkBuilder = LinkBuilder
        .createLink(details.emailAddress, "mailto:" + details.emailAddress);
      final LocalisableString emailTitle =
        new LocalisableString(BPOPARTICIPANT.INF_EMAIL_TITLE);
      linkBuilder.addLinkTitle(emailTitle);
      linkBuilder.addImageWithAltTextMessageAndCssClass("IconEmail",
        emailAltText, "defaultIcon");
      linkBuilder.addImageWithAltTextMessageAndCssClass("IconEmailHover",
        emailAltText, "hoverIcon");
      linkBuilder
        .setTextResource("curam.widget.render.infrastructure.i18n.Image");
      linkBuilder.openInNewWindow();
      emailContents.addWidgetItem(linkBuilder, "style", "link", "email");
    } else {
      emailIconImageBuilder
        .setImageResource("curam.widget.render.infrastructure.i18n.Image");
      emailIconImageBuilder.setImageAltText(emailAltText);
      emailContents.addImageItem(emailIconImageBuilder);
      final LocalisableString detailsNotRecorded =
        new LocalisableString(BPOPARTICIPANT.INF_NOT_RECORDED);
      emailContents.addlocalisableStringItem(
        detailsNotRecorded.toClientFormattedText(), "not-recorded email");
    }
    contactContents.addWidgetItem(emailContents, "style", "content-panel");
    contentPanelBuilder.addWidgetItem(contactContents, "style",
      "content-panel");
    return contentPanelBuilder;
  }

  // START :TASK 5347 CONTEXT PANEL NAME CHANGE PREFFERED NAME :JP
  /**
   * Append Preferred name if available to Given Name
   *
   * @param details
   * @return preferedName
   * @throws AppException
   * @throws InformationalException
   */

  private String getPersonPreferredName(final PersonTabDetails details)
    throws AppException, InformationalException {

    String preferedName = details.name;
    final AlternateName preferredNameObj = AlternateNameFactory.newInstance();
    final AlternateNameReadByTypeKey preferedAlternateNameReadByTypeKey =
      new AlternateNameReadByTypeKey();
    preferedAlternateNameReadByTypeKey.concernRoleID = details.concernRoleID;
    preferedAlternateNameReadByTypeKey.nameStatus = RECORDSTATUS.NORMAL;
    preferedAlternateNameReadByTypeKey.nameType = ALTERNATENAMETYPE.PREFERRED;
    AlternateNameDtls preferedAlternateNameDtls = null;

    try {
      preferedAlternateNameDtls =
        preferredNameObj.readByType(preferedAlternateNameReadByTypeKey);
    } catch (final Exception e) {
      // Do nothing
    }

    if (null != preferedAlternateNameDtls) {
      // Sample Name -> John Doe (Preferred Name: Johnny Doe)
      preferedName +=
        CuramConst.gkSpace + CuramConst.gkRoundOpeningBracketChar
          + BDMConstants.kPreferredName + preferedAlternateNameDtls.fullName
          + CuramConst.gkRoundClosingBracketChar;
    }

    return preferedName;

  }
  // END :TASK 5347 CONTEXT PANEL NAME CHANGE PREFFERED NAME :JP

  /**
   * This method gets Reference Number alternate ID.
   *
   * @param concernRoleID
   * @return String
   * @throws AppException
   * @throws InformationalException
   */
  private String getReferenceAlternateIDUsingConcernRoleID(
    final long concernRoleID) throws AppException, InformationalException {

    // Instantiate object.
    String concernRoleAlternateID = "";
    final ConcernRoleIDStatusCodeKey codeKey =
      new ConcernRoleIDStatusCodeKey();

    // Assign key values.
    codeKey.concernRoleID = concernRoleID;
    codeKey.statusCode = RECORDSTATUS.NORMAL;

    // Get the list of all alternate IDs
    final AlternateIDAndTypeCodeDtlsList codeDtlsList =
      ConcernRoleAlternateIDFactory.newInstance()
        .searchActiveAlternateIDAndType(codeKey);

    // Loop through the list to get reference number alternate id.
    for (final AlternateIDAndTypeCodeDtls codeDtls : codeDtlsList.dtls) {
      if (CONCERNROLEALTERNATEID.REFERENCE_NUMBER.equals(codeDtls.typeCode)) {
        concernRoleAlternateID = codeDtls.alternateID;
        break;
      }
    }

    // Return alternate ID.
    return concernRoleAlternateID;
  }

}
