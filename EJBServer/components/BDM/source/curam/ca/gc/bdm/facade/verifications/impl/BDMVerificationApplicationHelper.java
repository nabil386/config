/**
 *
 */
package curam.ca.gc.bdm.facade.verifications.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.ca.gc.bdm.facade.verifications.struct.BDMNewUserProvidedVerItemAndEvdDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMUserProvidedVerificationItemKey;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETRANSACTIONEVENTS;
import curam.codetable.CASETYPECODE;
import curam.codetable.DOCUMENTREVIEWSTATUS;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.LOCATIONACCESSTYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.REVERIFICATIONMODE;
import curam.codetable.SENSITIVITY;
import curam.codetable.VERIFIABLEITEMNAME;
import curam.codetable.VERIFICATIONITEMNAME;
import curam.codetable.VERIFICATIONITEMUSAGETYPE;
import curam.codetable.VERIFICATIONSTATUS;
import curam.codetable.VERIFICATIONTYPE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.codetable.impl.VERIFICATIONITEMNAMEEntry;
import curam.core.events.EVIDENCEBROKER;
import curam.core.facade.struct.RepresentativeID;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AttachmentFactory;
import curam.core.fact.CachedCaseHeaderFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.DataBasedSecurity;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.intf.AddressData;
import curam.core.intf.CaseHeader;
import curam.core.intf.ConcernRole;
import curam.core.sl.entity.struct.CaseIDParticipantIDTypeKey;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.fact.RepresentativeFactory;
import curam.core.sl.impl.CaseTransactionLogIntf;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CorrSetEvDescCaseIDStatusesKey;
import curam.core.sl.infrastructure.entity.struct.CorrectionSetIDDetails;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorIDAndStatusDetailsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorIDRelatedIDAndEvidenceType;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.ReadRelatedIDParticipantIDAndEvidenceTypeDetails;
import curam.core.sl.infrastructure.entity.struct.ReadStatusCodeDetails;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.ApplicationProgram;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceController;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.sl.intf.Representative;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.CaseParticipantRoleDetails;
import curam.core.sl.struct.ParticipantSecurityCheckKey;
import curam.core.sl.struct.RepresentativeRegistrationDetails;
import curam.core.sl.struct.ViewCaseParticipantRoleDetailsList;
import curam.core.struct.AttachmentHeaderDetails;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseSecurityCheckKey;
import curam.core.struct.CaseStatusCode;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRoleNameDetails;
import curam.core.struct.DataBasedSecurityResult;
import curam.core.struct.SecurityResult;
import curam.core.struct.UsersKey;
import curam.events.Verification;
import curam.message.BPOCASEEVENTS;
import curam.message.ENTVERIFICATIONITEMPROVISION;
import curam.message.GENERALCASE;
import curam.piwrapper.impl.AttachmentDAO;
import curam.piwrapper.user.impl.UserDAO;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.security.Authorisation;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.util.type.StringList;
import curam.verification.evidence.impl.VerificationEvidenceFactory;
import curam.verification.facade.infrastructure.struct.NewUserProvidedVerItemAndEvdDetails;
import curam.verification.sl.entity.fact.VerifiableDataItemFactory;
import curam.verification.sl.entity.fact.VerificationItemUtilizationFactory;
import curam.verification.sl.entity.fact.VerificationRequirementFactory;
import curam.verification.sl.entity.intf.VerifiableDataItem;
import curam.verification.sl.entity.struct.DataItemIDRecordStatusAndDatesDetails;
import curam.verification.sl.entity.struct.ItemUtilizationRequirementKey;
import curam.verification.sl.entity.struct.SearchByVerificationItem;
import curam.verification.sl.entity.struct.SecurityDetails;
import curam.verification.sl.entity.struct.UtlizationExpiryWarningAndDateFrom;
import curam.verification.sl.entity.struct.VerifiableDataItemDataItemNameAndIDDetails;
import curam.verification.sl.entity.struct.VerifiableDataItemDtls;
import curam.verification.sl.entity.struct.VerifiableDataItemKey;
import curam.verification.sl.entity.struct.VerifiableDataItemNameDetails;
import curam.verification.sl.entity.struct.VerificationItemUtilizationDtls;
import curam.verification.sl.entity.struct.VerificationItemUtilizationKey;
import curam.verification.sl.entity.struct.VerificationItemUtilizationUsageDetailsList;
import curam.verification.sl.entity.struct.VerificationRequirementDtls;
import curam.verification.sl.infrastructure.entity.fact.VDIEDLinkFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationAttachmentLinkFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationItemProvidedFactory;
import curam.verification.sl.infrastructure.entity.intf.VDIEDLink;
import curam.verification.sl.infrastructure.entity.intf.VerificationItemProvided;
import curam.verification.sl.infrastructure.entity.struct.AttachmentLinkAndFurtherInfoDetails;
import curam.verification.sl.infrastructure.entity.struct.AttachmentLinkAndFurtherInfoDetailsList;
import curam.verification.sl.infrastructure.entity.struct.EvidenceDescriptorDetails;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkAndDataItemIDDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkDtls;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKeyList;
import curam.verification.sl.infrastructure.entity.struct.VerificationDtls;
import curam.verification.sl.infrastructure.entity.struct.VerificationDtlsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedDtls;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedIDDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationRequirementIDDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationStatusDetails;
import curam.verification.sl.infrastructure.fact.OutstandingVerificationAttachmentLinkFactory;
import curam.verification.sl.infrastructure.impl.VerificationConfigurationCacheUtils;
import curam.verification.sl.infrastructure.impl.VerificationController;
import curam.verification.sl.infrastructure.impl.VerificationDeterminatorHelperUtils;
import curam.verification.sl.infrastructure.intf.OutstandingVerificationAttachmentLink;
import curam.verification.sl.infrastructure.struct.CaseIDAndVDIEDLinkIDKey;
import curam.verification.sl.infrastructure.struct.CreateVerificationAttachmentLinkDetails;
import curam.verification.sl.infrastructure.struct.NewUserProvidedVerificationItemDetails;
import curam.verification.sl.infrastructure.struct.SubmittedDocumentDetails;
import curam.verification.sl.infrastructure.struct.SubmittedDocumentDetailsList;
import java.util.Iterator;
import java.util.Set;

public class BDMVerificationApplicationHelper {

  @Inject
  private ApplicationProgram applicationProgram;

  @Inject
  protected Provider<CaseTransactionLogIntf> caseTransactionLogProvider;

  @Inject
  private AttachmentDAO attachmentDAO;

  @Inject
  private UserDAO userDAO;

  public BDMVerificationApplicationHelper() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  public void checkForSecurity(final VDIEDLinkKey vdIEDLinkKey,
    final short type) throws AppException, InformationalException {

    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();
    final CaseSecurityCheckKey caseSecurityCheckKey =
      new CaseSecurityCheckKey();

    final curam.verification.sl.infrastructure.entity.intf.Verification verification =
      VerificationFactory.newInstance();

    final ParticipantSecurityCheckKey participantSecurityCheckKey =
      new ParticipantSecurityCheckKey();

    DataBasedSecurityResult dataBasedSecurityResult =
      new DataBasedSecurityResult();

    final VerificationDtlsList verificationDtlsList =
      verification.readByVDIEDLinkID(vdIEDLinkKey);

    for (int i = 0; i < verificationDtlsList.dtls.size(); i++) {
      final VerificationDtls verificationDtls =
        verificationDtlsList.dtls.item(i);

      if (!verificationDtls.verificationLinkedType
        .equals(VERIFICATIONTYPE.NONCASEDATA)) {
        caseSecurityCheckKey.caseID = verificationDtls.verificationLinkedID;
        caseSecurityCheckKey.type = type;

        dataBasedSecurityResult =
          dataBasedSecurity.checkCaseSecurity1(caseSecurityCheckKey);

        if (!dataBasedSecurityResult.result) {
          if (dataBasedSecurityResult.readOnly)
            throw new AppException(
              GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);

          throw new AppException(
            GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);

        }
      }
      if (verificationDtls.verificationLinkedType
        .equals(VERIFICATIONTYPE.NONCASEDATA)) {

        participantSecurityCheckKey.participantID =
          verificationDtls.verificationLinkedID;

        if (type == 3) {
          participantSecurityCheckKey.type = LOCATIONACCESSTYPE.READ;
        } else {
          participantSecurityCheckKey.type = LOCATIONACCESSTYPE.MAINTAIN;

        }
        dataBasedSecurityResult = dataBasedSecurity
          .checkParticipantSecurity(participantSecurityCheckKey);

      }
      if (!dataBasedSecurityResult.result)
        throw new AppException(GENERALCASE.ERR_CASESECURITY_CHECK_RIGHTS);
    }
  }

  protected void propagateVerificationItem(
    final NewUserProvidedVerificationItemDetails details)
    throws AppException, InformationalException {

    final VerificationItemUtilizationDtls verificationItemUtilizationDtls =
      VerificationConfigurationCacheUtils.getVerificationItemUtilizationDtls(
        details.newUserProvidedVerificationInfo.verificationItemUtilizationID);

    final DataItemIDRecordStatusAndDatesDetails dataItemIDRecordStatusAndDatesDetails =
      new DataItemIDRecordStatusAndDatesDetails();

    dataItemIDRecordStatusAndDatesDetails.verifiableDataItemID =
      verificationItemUtilizationDtls.verifiableDataItemID;

    dataItemIDRecordStatusAndDatesDetails.fromDate = Date.getCurrentDate();
    dataItemIDRecordStatusAndDatesDetails.toDate =
      verificationItemUtilizationDtls.toDate;

    dataItemIDRecordStatusAndDatesDetails.recordStatus =
      RECORDSTATUS.CANCELLED;

    final Set<VerificationRequirementDtls> verificationRequirementDtlSet =
      VerificationConfigurationCacheUtils
        .fetchAllVerificationRequirementsByDates(
          dataItemIDRecordStatusAndDatesDetails);

    boolean dataItemValueChangedCheck = false;

    final Iterator<VerificationRequirementDtls> verificationRequirementDtlsIterator =
      verificationRequirementDtlSet.iterator();

    for (int l = 0; l < verificationRequirementDtlSet.size(); l++) {

      final VerificationRequirementDtls verificationRequirementDtls =
        verificationRequirementDtlsIterator.next();

      if (verificationRequirementDtls.reverificationMode
        .equals(REVERIFICATIONMODE.REVERIFYALWAYS))

        return;
      if (verificationRequirementDtls.reverificationMode
        .equals(REVERIFICATIONMODE.REVERIFYIFCHANGED))

        dataItemValueChangedCheck = true;

    }
    final VDIEDLink vdIEDLinkObj = VDIEDLinkFactory.newInstance();
    final VDIEDLinkKey vdIEDLinkKey = new VDIEDLinkKey();

    EvidenceDescriptorDetails evidenceDescriptorDetails =
      new EvidenceDescriptorDetails();

    vdIEDLinkKey.VDIEDLinkID =
      details.newUserProvidedVerificationInfo.VDIEDLinkID;

    evidenceDescriptorDetails =
      vdIEDLinkObj.readEvidenceDescriptorIDByVDIEDLink(vdIEDLinkKey);

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();

    CorrectionSetIDDetails correctionSetIDDetails =
      new CorrectionSetIDDetails();

    evidenceDescriptorKey.evidenceDescriptorID =
      evidenceDescriptorDetails.evidenceDescriptorID;

    correctionSetIDDetails = evidenceDescriptorObj
      .readCorrectionSetIDByDescriptorID(evidenceDescriptorKey);

    EvidenceDescriptorIDAndStatusDetailsList evidenceDescriptorIDAndStatusDetailsList =
      new EvidenceDescriptorIDAndStatusDetailsList();

    final CorrSetEvDescCaseIDStatusesKey corrSetEvDescCaseIDStatusesKey =
      new CorrSetEvDescCaseIDStatusesKey();

    corrSetEvDescCaseIDStatusesKey.caseID = correctionSetIDDetails.caseID;
    corrSetEvDescCaseIDStatusesKey.correctionSetID =
      correctionSetIDDetails.correctionSetID;

    corrSetEvDescCaseIDStatusesKey.evidenceDescriptorID =
      evidenceDescriptorKey.evidenceDescriptorID;

    corrSetEvDescCaseIDStatusesKey.statusCode1 =
      EVIDENCEDESCRIPTORSTATUS.CANCELED;

    corrSetEvDescCaseIDStatusesKey.statusCode2 =
      EVIDENCEDESCRIPTORSTATUS.SUPERSEDED;

    evidenceDescriptorIDAndStatusDetailsList =
      evidenceDescriptorObj.searchRelatedDescriptorIDByCorrSetStatuses(
        corrSetEvDescCaseIDStatusesKey);

    final VerifiableDataItemDtls verifiableDataItemDtls =
      VerificationConfigurationCacheUtils.getVerifiableDataItemDtls(
        verificationItemUtilizationDtls.verifiableDataItemID);

    final VerifiableDataItemNameDetails verifiableDataItemNameDetails =
      new VerifiableDataItemNameDetails();

    verifiableDataItemNameDetails.name = verifiableDataItemDtls.dataItem;

    int i = 0;
    for (; i < evidenceDescriptorIDAndStatusDetailsList.dtls.size(); i++) {

      if (dataItemValueChangedCheck
        && (evidenceDescriptorIDAndStatusDetailsList.dtls.item(i).statusCode
          .equals(EVIDENCEDESCRIPTORSTATUS.ACTIVE)
          || evidenceDescriptorIDAndStatusDetailsList.dtls.item(i).statusCode
            .equals(EVIDENCEDESCRIPTORSTATUS.INEDIT))) {

        final EvidenceDescriptorKey activeEvidenceDescriptorKey =
          new EvidenceDescriptorKey();

        activeEvidenceDescriptorKey

          .evidenceDescriptorID =
            evidenceDescriptorIDAndStatusDetailsList.dtls
              .item(i).evidenceDescriptorID;

        if (checkDataItemForValueChange(verifiableDataItemNameDetails,
          evidenceDescriptorKey, activeEvidenceDescriptorKey))

          continue;
      }
      final VDIEDLinkKey orgVDIEDLinkKey = new VDIEDLinkKey();

      evidenceDescriptorKey

        .evidenceDescriptorID = evidenceDescriptorIDAndStatusDetailsList.dtls
          .item(i).evidenceDescriptorID;

      final VDIEDLinkAndDataItemIDDetailsList vdIEDLinkAndDataItemIDDetailsList =
        vdIEDLinkObj.readByEvidenceDescriptor(evidenceDescriptorKey);

      orgVDIEDLinkKey.VDIEDLinkID =
        details.newUserProvidedVerificationInfo.VDIEDLinkID;

      int k = 0;
      for (; k < vdIEDLinkAndDataItemIDDetailsList.dtls.size(); k++) {
        details.newUserProvidedVerificationInfo.VDIEDLinkID =
          vdIEDLinkAndDataItemIDDetailsList.dtls.item(k).vdIEDLinkID;
        addVerificationItemToRequirement(details);
      }
      details.newUserProvidedVerificationInfo.VDIEDLinkID =
        orgVDIEDLinkKey.VDIEDLinkID;
      continue;
    }
  }

  private Set<VerificationItemProvidedDtls>
    convertToMultiVerificationItemProvidedDtls(
      final NewUserProvidedVerificationItemDetails details)
      throws AppException, InformationalException {

    return VerificationConfigurationCacheUtils
      .fetchVerificationItemProvidedDtlsList(details);
  }

  /**
   *
   * @param details
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public BDMUserProvidedVerificationItemKey newUserProvidedVerificationItem(
    final NewUserProvidedVerificationItemDetails details)
    throws AppException, InformationalException {

    String verificationItemUsageType = "";

    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final ConcernRoleNameDetails concernRoleNameDetails =
      new ConcernRoleNameDetails();

    final VDIEDLinkKey vDIEDLinkKey = new VDIEDLinkKey();

    final curam.verification.sl.infrastructure.intf.Verification verification =
      curam.verification.sl.infrastructure.fact.VerificationFactory
        .newInstance();

    final UsersKey userKey = new UsersKey();

    details.newUserProvidedVerificationInfo.recordStatus =
      RECORDSTATUS.DEFAULTCODE;

    userKey.userName = TransactionInfo.getProgramUser();
    details.newUserProvidedVerificationInfo.addedByUser = userKey.userName;
    details.newUserProvidedVerificationInfo.dateAdded = Date.getCurrentDate();

    final SecurityDetails securityDetails = new SecurityDetails();
    final SecurityResult securityResult = new SecurityResult();

    final BDMUserProvidedVerificationItemKey userProvidedVerificationItemKey =
      new BDMUserProvidedVerificationItemKey();

    final VerificationItemProvidedKey verificationItemProvidedKey =
      new VerificationItemProvidedKey();

    vDIEDLinkKey.VDIEDLinkID =
      details.newUserProvidedVerificationInfo.VDIEDLinkID;

    checkForSecurity(vDIEDLinkKey, (short) 1);

    final VerificationItemUtilizationDtls verificationItemUtilizationDtls =
      VerificationConfigurationCacheUtils.getVerificationItemUtilizationDtls(
        details.newUserProvidedVerificationInfo.verificationItemUtilizationID);

    verificationItemUsageType = verificationItemUtilizationDtls.usageType;

    securityDetails.addSID = verificationItemUtilizationDtls.addSID;
    securityDetails.removeSID = verificationItemUtilizationDtls.removeSID;

    if (securityDetails.addSID.length() != 0) {
      securityResult.result = Authorisation
        .isSIDAuthorised(securityDetails.addSID, userKey.userName);
      if (!securityResult.result) {
        final AppException appException = new AppException(
          ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_NO_APPROPRIATE_PRIVILEGES);

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          appException, "", InformationalElement.InformationalType.kError,
          "a", 0);

      }
    }
    if (details.itemProvidedDetailsdtls.caseParticipantConcernRoleID == 0L
      && details.itemProvidedDetailsdtls.participantConcernRoleID == 0L
      && details.itemProvidedDetailsdtls.providerName

        .length() == 0) {
      final AppException appException = new AppException(
        ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_VALUE_ENTERED_FOR_ONE_OF_CASE_PARTICIPANT);

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        appException, "", InformationalElement.InformationalType.kError, "a",
        0);

    } else if (details.itemProvidedDetailsdtls.caseParticipantConcernRoleID != 0L
      && details.itemProvidedDetailsdtls.participantConcernRoleID != 0L
      || details.itemProvidedDetailsdtls.participantConcernRoleID != 0L
        && details.itemProvidedDetailsdtls.providerName

          .length() != 0
      || details.itemProvidedDetailsdtls.caseParticipantConcernRoleID != 0L
        && details.itemProvidedDetailsdtls.providerName

          .length() != 0) {

      final AppException appException = new AppException(
        ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_VALUE_ENTERED_FOR_ONLY_ONE_OF_CASE_PARTICIPANT);

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        appException, "", InformationalElement.InformationalType.kError, "a",
        0);

    }
    final VDIEDLinkKey vdIEDLinkKey = new VDIEDLinkKey();

    vdIEDLinkKey.VDIEDLinkID =
      details.newUserProvidedVerificationInfo.VDIEDLinkID;

    final VDIEDLink vdIEDLinkObj = VDIEDLinkFactory.newInstance();

    final EvidenceDescriptorIDRelatedIDAndEvidenceType evidenceDescriptorIDRelatedIDAndEvidenceType =
      vdIEDLinkObj
        .readEDIDRelatedIDAndEvidenceTypeByVDIEDLinkID(vdIEDLinkKey);

    boolean isNonCaseEDForParticipantEvidence = false;

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    isNonCaseEDForParticipantEvidence =
      evidenceControllerObj.isNonCaseEDForParticipantEvidence(
        evidenceDescriptorIDRelatedIDAndEvidenceType);

    final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();

    evidenceTypeKey.evidenceType =
      evidenceDescriptorIDRelatedIDAndEvidenceType.evidenceType;

    final boolean isPDCEvidence =
      evidenceControllerObj.isPDCEvidence(evidenceTypeKey);

    if (!isNonCaseEDForParticipantEvidence && !isPDCEvidence) {

      final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

      caseHeaderKey.caseID = details.itemProvidedDetailsdtls.caseID;

      final CaseStatusCode caseStatusCode =
        caseHeaderObj.readCaseStatus(caseHeaderKey);

      if (caseStatusCode.statusCode.equals(CASESTATUS.CLOSED))

        ValidationManagerFactory.getManager()
          .throwWithLookup(new AppException(
            ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_CASE_CLOSED),
            "a", 1);

    }
    // final CaseParticipantRole caseParticipantRoleObj =
    // CaseParticipantRoleFactory.newInstance();

    registerProviderDetails1(details, concernRoleObj, concernRoleKey,
      isNonCaseEDForParticipantEvidence, isPDCEvidence);

    final Set<VerificationItemProvidedDtls> verificationItemProvidedDtlsSet =
      convertToMultiVerificationItemProvidedDtls(details);

    boolean attachmentCreated = false;
    long attachmentID = 0L;

    for (final VerificationItemProvidedDtls verificationItemProvidedDtls : verificationItemProvidedDtlsSet) {
      if (details.newUserProvidedVerificationInfo.VDIEDLinkID == verificationItemProvidedDtls.VDIEDLinkID) {

        final VerificationItemProvided verificationItemProvided =
          VerificationItemProvidedFactory.newInstance();

        verificationItemProvided.insert(verificationItemProvidedDtls);
        verificationItemProvidedKey.verificationItemProvidedID =
          verificationItemProvidedDtls.verificationItemProvidedID;

        userProvidedVerificationItemKey.bdmUserProvidedVerificationItemKey.userProvidedVerificationItemKey.verificationItemProvidedID =
          verificationItemProvidedDtls.verificationItemProvidedID;

        if (details.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName
          .trim().length() == 0) {
          if (details.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation
            .trim().length() != 0
            && details.createVerificationAttachmentLinkDetails.createLinkDtls.description

              .trim().length() == 0) {
            final AppException appException = new AppException(
              ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_ATTACHMENT_DESCRIPTION_MUST_ENTERED);

            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(appException, "",
                InformationalElement.InformationalType.kError, "a", 0);

          }
          if (details.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation
            .trim().length() == 0
            && details.createVerificationAttachmentLinkDetails.createLinkDtls.description

              .trim().length() != 0) {
            final AppException appException = new AppException(
              ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_PROVIDE_FILE_LOCATION_IF_DESCRIPTION_ENTERED);

            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(appException, "",
                InformationalElement.InformationalType.kError, "a", 1);

          }
        } else if (details.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation
          .trim().length() != 0
          || details.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference

            .trim().length() != 0) {
          final AppException appException = new AppException(
            ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_PROVIDE_FILE_LOCATION_IF_DESCRIPTION_ENTERED);

          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            appException, "", InformationalElement.InformationalType.kError,
            "a", 0);

        } else if (details.createVerificationAttachmentLinkDetails.createLinkDtls.description
          .trim().length() == 0) {
          final AppException appException = new AppException(
            ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_ATTACHMENT_DESCRIPTION_MUST_ENTERED);

          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            appException, "", InformationalElement.InformationalType.kError,
            "a", 1);

        }
        final VerificationController verificationControllerObj =
          new VerificationController();

        verificationControllerObj.checkForInformationals();
        final CreateVerificationAttachmentLinkDetails createVerificationAttachmentLinkDetails =
          new CreateVerificationAttachmentLinkDetails();

        createVerificationAttachmentLinkDetails.createLinkDtls.description =
          details.createVerificationAttachmentLinkDetails.createLinkDtls.description;

        createVerificationAttachmentLinkDetails.createLinkDtls.verificationItemProvidedID =
          verificationItemProvidedDtls.verificationItemProvidedID;

        createVerificationAttachmentLinkDetails.createAttachmentDtls =
          details.createVerificationAttachmentLinkDetails.createAttachmentDtls;

        final curam.verification.sl.infrastructure.intf.VerificationAttachmentLink verificationAttachmentLink =
          curam.verification.sl.infrastructure.fact.VerificationAttachmentLinkFactory
            .newInstance();

        if (!attachmentCreated) {
          verificationAttachmentLink.createVerificationAttachmentLink(
            createVerificationAttachmentLinkDetails);
          // Setting the verificationAttachmentLinkID
          userProvidedVerificationItemKey.verificationAtachmentLinkID =
            createVerificationAttachmentLinkDetails.createLinkDtls.verificationAttachmentLinkID;
          attachmentID =
            createVerificationAttachmentLinkDetails.createLinkDtls.attachmentID;

          if (attachmentID == 0) {
            attachmentID =
              createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID;
          }

          if (attachmentID != 0
            && userProvidedVerificationItemKey.verificationAtachmentLinkID == 0) {
            userProvidedVerificationItemKey.attachmentID = attachmentID;
          }

          if (attachmentID != 0L)
            attachmentCreated = true;

        } else {
          createVerificationAttachmentLinkDetails.createLinkDtls.attachmentID =
            attachmentID;

          createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
            RECORDSTATUS.DEFAULTCODE;

          VerificationAttachmentLinkFactory.newInstance()
            .insert(createVerificationAttachmentLinkDetails.createLinkDtls);

        }
        if (!isNonCaseEDForParticipantEvidence)
          propagateVerificationItem(details);

        if (StringUtil.isNullOrEmpty(verificationItemUsageType)
          || VERIFICATIONITEMUSAGETYPE.SHARED

            .equals(verificationItemUsageType))
          cloneItemProvided(details);

        details.newUserProvidedVerificationInfo.VDIEDLinkID =
          vdIEDLinkKey.VDIEDLinkID;

        raiseVerificationUpdatedEvent(verificationItemProvidedKey);
        final UtlizationExpiryWarningAndDateFrom utlizationExpiryWarningAndDateFrom =
          new UtlizationExpiryWarningAndDateFrom();

        utlizationExpiryWarningAndDateFrom.expiryDays =
          verificationItemUtilizationDtls.expiryDays;

        utlizationExpiryWarningAndDateFrom.warningDays =
          verificationItemUtilizationDtls.warningDays;

        utlizationExpiryWarningAndDateFrom.expiryDateFrom =
          verificationItemUtilizationDtls.expiryDateFrom;

        utlizationExpiryWarningAndDateFrom.verificationItemUtilizationID =
          verificationItemUtilizationDtls.verificationItemUtilizationID;

        if (utlizationExpiryWarningAndDateFrom.expiryDays > 0
          && !isNonCaseEDForParticipantEvidence && !isPDCEvidence)

          verificationControllerObj.raiseEvents(
            utlizationExpiryWarningAndDateFrom, verificationItemProvidedKey);

      }
    }
    vDIEDLinkKey.VDIEDLinkID =
      details.newUserProvidedVerificationInfo.VDIEDLinkID;

    verification.determineVerificationStatus(vDIEDLinkKey);

    if (details != null && details.itemProvidedDetailsdtls != null
      && details.itemProvidedDetailsdtls.caseID != 0L) {

      String dataItem = null;

      dataItem = CodeTable.getOneItem(VERIFIABLEITEMNAME.TABLENAME,
        VerificationConfigurationCacheUtils.getVerifiableDataItemDtls(
          verificationItemUtilizationDtls.verifiableDataItemID).name,

        TransactionInfo.getProgramLocale());

      final LocalisableString description =
        new LocalisableString(BPOCASEEVENTS.VERIFICATION_COMPLETED)
          .arg(dataItem);

      this.caseTransactionLogProvider.get().recordCaseTransaction(
        CASETRANSACTIONEVENTS.VERIFICATION_COMPLETED, description,
        details.itemProvidedDetailsdtls.caseID,
        evidenceDescriptorIDRelatedIDAndEvidenceType.evidenceDescriptorID);

    }
    if (details.itemProvidedDetailsdtls.caseID != 0L) {

      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = details.itemProvidedDetailsdtls.caseID;

      final CaseHeaderDtls caseHeaderDtls =
        CachedCaseHeaderFactory.newInstance().read(caseHeaderKey);

      final EvidenceDescriptorDetails descriptor =
        vdIEDLinkObj.readEvidenceDescriptorIDByVDIEDLink(vDIEDLinkKey);

      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final EvidenceDescriptorKey key = new EvidenceDescriptorKey();
      key.evidenceDescriptorID = descriptor.evidenceDescriptorID;

      final EvidenceDescriptorDtls evidenceDescriptorDtls =
        evidenceDescriptorObj.read(key);
      final EvidenceDescriptorDtlsList evidenceDescriptorDtlsList =
        new EvidenceDescriptorDtlsList();

      evidenceDescriptorDtlsList.dtls.add(evidenceDescriptorDtls);

      final EvidenceControllerInterface evidenceController =
        (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

      if (caseHeaderDtls.caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)
        && this.applicationProgram.isProgramAuthorized(caseHeaderDtls)
        && !evidenceController.doUnverifiedMandatoryVerificationsExist(
          evidenceDescriptorDtlsList)) {

        final Event event = new Event();
        event.eventKey = EVIDENCEBROKER.EVIDENCEACTIVATED;
        event.primaryEventData = evidenceDescriptorDtls.evidenceDescriptorID;
        EventService.raiseEvent(event);

      }
    }
    return userProvidedVerificationItemKey;
  }

  /**
   * @param details
   * @param concernRoleObj
   * @param concernRoleKey
   * @param isNonCaseEDForParticipantEvidence
   * @param isPDCEvidence
   * @param caseParticipantRoleObj
   * @throws AppException
   * @throws InformationalException
   */
  private String registerProviderDetails1(
    final NewUserProvidedVerificationItemDetails details,
    final ConcernRole concernRoleObj, final ConcernRoleKey concernRoleKey,
    final boolean isNonCaseEDForParticipantEvidence,
    final boolean isPDCEvidence) throws AppException, InformationalException {

    final CaseParticipantRole caseParticipantRoleObj =
      CaseParticipantRoleFactory.newInstance();

    ConcernRoleNameDetails concernRoleNameDetails;
    if (details.itemProvidedDetailsdtls.caseParticipantConcernRoleID != 0L) {

      ViewCaseParticipantRoleDetailsList viewCaseParticipantRole_boDetailsList =
        new ViewCaseParticipantRoleDetailsList();

      final CaseIDParticipantIDTypeKey caseIDParticipantIDTypeKey =
        new CaseIDParticipantIDTypeKey();

      caseIDParticipantIDTypeKey.caseID =
        details.itemProvidedDetailsdtls.caseID;

      caseIDParticipantIDTypeKey.participantRoleID =
        details.itemProvidedDetailsdtls.caseParticipantConcernRoleID;

      caseIDParticipantIDTypeKey.typeCode = CASEPARTICIPANTROLETYPE.PROVIDER;

      viewCaseParticipantRole_boDetailsList = caseParticipantRoleObj
        .viewCaseParticipantRoleListByTypeAndID(caseIDParticipantIDTypeKey);
      boolean participantRole = false;

      if (viewCaseParticipantRole_boDetailsList.dtls.size() > 0)
        participantRole = true;

      if (!participantRole) {

        final CaseParticipantRoleDetails caseParticipantRole_boDetails =
          new CaseParticipantRoleDetails();

        caseParticipantRole_boDetails.dtls.caseID =
          details.itemProvidedDetailsdtls.caseID;

        caseParticipantRole_boDetails.dtls.participantRoleID =
          details.itemProvidedDetailsdtls.caseParticipantConcernRoleID;

        caseParticipantRole_boDetails.dtls.fromDate = Date.getCurrentDate();

        caseParticipantRole_boDetails.dtls.typeCode =
          CASEPARTICIPANTROLETYPE.PROVIDER;

        caseParticipantRoleObj
          .insertCaseParticipantRole(caseParticipantRole_boDetails);

      }
      concernRoleKey.concernRoleID =
        details.itemProvidedDetailsdtls.caseParticipantConcernRoleID;

      concernRoleNameDetails =
        concernRoleObj.readConcernRoleName(concernRoleKey);
      details.newUserProvidedVerificationInfo.receivedFrom =
        concernRoleNameDetails.concernRoleName;

    } else if (details.itemProvidedDetailsdtls.participantConcernRoleID != 0L
      && !isNonCaseEDForParticipantEvidence && !isPDCEvidence) {

      ViewCaseParticipantRoleDetailsList viewCaseParticipantRole_boDetailsList =
        new ViewCaseParticipantRoleDetailsList();

      final CaseIDParticipantIDTypeKey caseIDParticipantIDTypeKey =
        new CaseIDParticipantIDTypeKey();

      caseIDParticipantIDTypeKey.caseID =
        details.itemProvidedDetailsdtls.caseID;

      caseIDParticipantIDTypeKey.participantRoleID =
        details.itemProvidedDetailsdtls.participantConcernRoleID;

      caseIDParticipantIDTypeKey.typeCode = CASEPARTICIPANTROLETYPE.PROVIDER;

      viewCaseParticipantRole_boDetailsList = caseParticipantRoleObj
        .viewCaseParticipantRoleListByTypeAndID(caseIDParticipantIDTypeKey);
      boolean participantRole = false;

      if (viewCaseParticipantRole_boDetailsList.dtls.size() > 0)
        participantRole = true;

      if (!participantRole) {
        final CaseParticipantRoleDetails caseParticipantRole_boDetails =
          new CaseParticipantRoleDetails();

        caseParticipantRole_boDetails.dtls.caseID =
          details.itemProvidedDetailsdtls.caseID;

        caseParticipantRole_boDetails.dtls.participantRoleID =
          details.itemProvidedDetailsdtls.participantConcernRoleID;

        caseParticipantRole_boDetails.dtls.fromDate = Date.getCurrentDate();
        caseParticipantRole_boDetails.dtls.typeCode =
          CASEPARTICIPANTROLETYPE.PROVIDER;

        caseParticipantRoleObj
          .insertCaseParticipantRole(caseParticipantRole_boDetails);

      }
      concernRoleKey.concernRoleID =
        details.itemProvidedDetailsdtls.participantConcernRoleID;

      concernRoleNameDetails =
        concernRoleObj.readConcernRoleName(concernRoleKey);
      details.newUserProvidedVerificationInfo.receivedFrom =
        concernRoleNameDetails.concernRoleName;

    } else if (details.itemProvidedDetailsdtls.providerName.length() > 0) {

      final Representative representativeObj =
        RepresentativeFactory.newInstance();

      final RepresentativeRegistrationDetails representativeRegistrationDetails =
        new RepresentativeRegistrationDetails();

      representativeRegistrationDetails.representativeDtls.representativeName =
        details.itemProvidedDetailsdtls.providerName;

      final AddressData addressDataObj = AddressDataFactory.newInstance();

      representativeRegistrationDetails.representativeRegistrationDetails.addressData =
        addressDataObj.getAddressDataForLocale().addressData;

      representativeRegistrationDetails.representativeRegistrationDetails.registrationDate =
        Date.getCurrentDate();
      representativeRegistrationDetails.representativeRegistrationDetails.sensitivity =
        SENSITIVITY.DEFAULTCODE;

      representativeObj
        .registerRepresentative(representativeRegistrationDetails);

      if (details.itemProvidedDetailsdtls.caseID != 0L) {

        final RepresentativeID representativeID = new RepresentativeID();

        representativeID.representativeID =
          representativeRegistrationDetails.representativeDtls.concernRoleID;

        final CaseParticipantRoleDetails caseParticipantRole_boDetails =
          new CaseParticipantRoleDetails();

        caseParticipantRole_boDetails.dtls.caseID =
          details.itemProvidedDetailsdtls.caseID;

        caseParticipantRole_boDetails.dtls.participantRoleID =
          representativeID.representativeID;

        caseParticipantRole_boDetails.dtls.fromDate = Date.getCurrentDate();
        caseParticipantRole_boDetails.dtls.typeCode =
          CASEPARTICIPANTROLETYPE.PROVIDER;

        caseParticipantRoleObj
          .insertCaseParticipantRole(caseParticipantRole_boDetails);
      }
      details.newUserProvidedVerificationInfo.receivedFrom =
        details.itemProvidedDetailsdtls.providerName;

    }
    return details.newUserProvidedVerificationInfo.receivedFrom;
  }

  protected boolean checkDataItemForValueChange(
    final VerifiableDataItemNameDetails dataItemNameDetails,
    final EvidenceDescriptorKey ineditDescriptorKey,
    final EvidenceDescriptorKey activeDescriptorKey)
    throws AppException, InformationalException {

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();

    final EIEvidenceKey inEditEvidenceKey = new EIEvidenceKey();
    final EIEvidenceKey activeEvidenceKey = new EIEvidenceKey();

    final EvidenceDescriptorDtls inEditEvidenceDescriptorDtls =
      evidenceDescriptorObj.read(ineditDescriptorKey);

    final EvidenceDescriptorDtls activeEvidenceDescriptorDtls =
      evidenceDescriptorObj.read(activeDescriptorKey);

    inEditEvidenceKey.evidenceID = inEditEvidenceDescriptorDtls.relatedID;
    activeEvidenceKey.evidenceID = activeEvidenceDescriptorDtls.relatedID;

    inEditEvidenceKey.evidenceType =
      inEditEvidenceDescriptorDtls.evidenceType;

    activeEvidenceKey.evidenceType =
      activeEvidenceDescriptorDtls.evidenceType;

    final EIEvidenceReadDtls eiEditEvidenceDtls = new EIEvidenceReadDtls();
    final EIEvidenceModifyDtls eiActiveEvidenceDtls =
      new EIEvidenceModifyDtls();

    final EvidenceMap map = EvidenceController.getEvidenceMap();

    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(inEditEvidenceKey.evidenceType);

    eiEditEvidenceDtls.evidenceObject =
      standardEvidenceInterface.readEvidence(inEditEvidenceKey);
    eiActiveEvidenceDtls.evidenceObject =
      standardEvidenceInterface.readEvidence(activeEvidenceKey);

    return VerificationEvidenceFactory
      .newInstance(
        CASEEVIDENCEEntry.get(inEditEvidenceDescriptorDtls.evidenceType))
      .checkDataItemValueChanged(dataItemNameDetails.name,
        eiActiveEvidenceDtls, eiEditEvidenceDtls,
        inEditEvidenceDescriptorDtls.evidenceType);
  }

  protected void addVerificationItemToRequirement(
    final NewUserProvidedVerificationItemDetails details)
    throws AppException, InformationalException {

    final VerificationItemProvidedKey verificationItemProvidedKey =
      new VerificationItemProvidedKey();

    try {
      final VerificationItemProvided verificationItemProvided =
        VerificationItemProvidedFactory.newInstance();

      final VerificationItemProvidedDtls verificationItemProvidedDtls =
        VerificationConfigurationCacheUtils
          .convertToVerificationItemProvidedDtls(details);

      verificationItemProvided.insert(verificationItemProvidedDtls);
      verificationItemProvidedKey.verificationItemProvidedID =
        verificationItemProvidedDtls.verificationItemProvidedID;

      if (details.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName
        .length() != 0) {

        final CreateVerificationAttachmentLinkDetails createVerificationAttachmentLinkDetails =
          new CreateVerificationAttachmentLinkDetails();

        createVerificationAttachmentLinkDetails.createLinkDtls.description =
          details.createVerificationAttachmentLinkDetails.createLinkDtls.description;

        createVerificationAttachmentLinkDetails.createAttachmentDtls =
          details.createVerificationAttachmentLinkDetails.createAttachmentDtls;

        final curam.verification.sl.infrastructure.intf.VerificationAttachmentLink verificationAttachmentLinkObj =
          curam.verification.sl.infrastructure.fact.VerificationAttachmentLinkFactory
            .newInstance();

        verificationAttachmentLinkObj.createVerificationAttachmentLink(
          createVerificationAttachmentLinkDetails);

      }
    } catch (final AppException e) {

      if (!e.getCatEntry().equals(
        ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_VERIFICATION_ITEM_ALREADY_ADDED))

        throw e;
    }
  }

  protected void
    cloneItemProvided(final NewUserProvidedVerificationItemDetails details)
      throws AppException, InformationalException {

    final VDIEDLinkKey vdIEDLinkKey = new VDIEDLinkKey();

    vdIEDLinkKey.VDIEDLinkID =
      details.newUserProvidedVerificationInfo.VDIEDLinkID;

    final CaseIDKey caseIDKey = new CaseIDKey();

    final VDIEDLink vdIEDLinkObj = VDIEDLinkFactory.newInstance();
    VDIEDLinkKeyList vdIEDLinkKeyList = new VDIEDLinkKeyList();
    VDIEDLinkKey vdIEDLinkListKey = new VDIEDLinkKey();
    VDIEDLinkDtls vdIEDLinkDtls = new VDIEDLinkDtls();

    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();

    ReadStatusCodeDetails readStatusCodeDetails = new ReadStatusCodeDetails();

    final EvidenceDescriptor evidenceDescriptor =
      EvidenceDescriptorFactory.newInstance();

    EvidenceDescriptorDetails evidenceDescriptorDetails =
      new EvidenceDescriptorDetails();

    evidenceDescriptorDetails =
      vdIEDLinkObj.readEvidenceDescriptorIDByVDIEDLink(vdIEDLinkKey);

    evidenceDescriptorKey.evidenceDescriptorID =
      evidenceDescriptorDetails.evidenceDescriptorID;

    final ReadRelatedIDParticipantIDAndEvidenceTypeDetails participantDetails =
      evidenceDescriptor
        .readRelatedIDParticipantIDAndEvidenceType(evidenceDescriptorKey);

    final EvidenceDescriptorIDRelatedIDAndEvidenceType evidenceDescriptorIDRelatedIDAndEvidenceTypeKey =
      new EvidenceDescriptorIDRelatedIDAndEvidenceType();

    evidenceDescriptorIDRelatedIDAndEvidenceTypeKey.evidenceDescriptorID =
      evidenceDescriptorDetails.evidenceDescriptorID;

    evidenceDescriptorIDRelatedIDAndEvidenceTypeKey.evidenceType =
      participantDetails.evidenceType;

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final boolean isNonCaseEDForParticipantEvidence =
      evidenceControllerObj.isNonCaseEDForParticipantEvidence(
        evidenceDescriptorIDRelatedIDAndEvidenceTypeKey);

    if (isNonCaseEDForParticipantEvidence) {

      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

      concernRoleKey.concernRoleID = participantDetails.participantID;

      vdIEDLinkKeyList =
        vdIEDLinkObj.searchVDIEDLinkIDsByConcernRoleID(concernRoleKey);

    } else {
      caseIDKey.caseID = details.itemProvidedDetailsdtls.caseID;
      vdIEDLinkKeyList = vdIEDLinkObj.searchVDIEDLinkIDsByCaseID(caseIDKey);

    }
    for (int i = 0; i < vdIEDLinkKeyList.dtls.size(); i++) {

      vdIEDLinkListKey = vdIEDLinkKeyList.dtls.item(i);

      final EvidenceDescriptorIDRelatedIDAndEvidenceType evidenceDescriptorIDRelatedIDAndEvidenceType =
        vdIEDLinkObj
          .readEDIDRelatedIDAndEvidenceTypeByVDIEDLinkID(vdIEDLinkListKey);

      if (evidenceDescriptorIDRelatedIDAndEvidenceType.evidenceType
        .equals(participantDetails.evidenceType)) {

        evidenceDescriptorKey.evidenceDescriptorID =
          evidenceDescriptorIDRelatedIDAndEvidenceType.evidenceDescriptorID;

        readStatusCodeDetails =
          evidenceDescriptor.readStatusCode(evidenceDescriptorKey);

        final ReadRelatedIDParticipantIDAndEvidenceTypeDetails otherParticipantDetails =
          evidenceDescriptor
            .readRelatedIDParticipantIDAndEvidenceType(evidenceDescriptorKey);

        if (!readStatusCodeDetails.statusCode
          .equals(EVIDENCEDESCRIPTORSTATUS.CANCELED) &&

          !readStatusCodeDetails.statusCode
            .equals(EVIDENCEDESCRIPTORSTATUS.SUPERSEDED)
          && vdIEDLinkListKey.VDIEDLinkID != vdIEDLinkKey.VDIEDLinkID
          && otherParticipantDetails.participantID == participantDetails.participantID) {

          vdIEDLinkDtls = vdIEDLinkObj.read(vdIEDLinkListKey);

          VerificationRequirementIDDetailsList reqtDetailsList =
            new VerificationRequirementIDDetailsList();

          final curam.verification.sl.infrastructure.entity.intf.Verification verificationObj =
            VerificationFactory.newInstance();

          final curam.verification.sl.entity.intf.VerificationRequirement verificationRequirementObj =
            VerificationRequirementFactory.newInstance();

          reqtDetailsList = verificationObj
            .readAllVerificationRequirementsByVDIEDLink(vdIEDLinkListKey);

          for (int j = 0; j < reqtDetailsList.dtls.size(); j++) {

            final VerificationKey checkCancelledverificationKey =
              new VerificationKey();

            VerificationStatusDetails verificaionStatusDtls =
              new VerificationStatusDetails();

            checkCancelledverificationKey.verificationID =
              reqtDetailsList.dtls.item(j).verificationID;

            verificaionStatusDtls = verificationObj
              .readVerificationStatus(checkCancelledverificationKey);

            if (!verificaionStatusDtls.verificationStatus
              .equals(VERIFICATIONSTATUS.CANCELLED)) {

              final VerificationRequirementDtls verificationRequirementDtls =
                VerificationConfigurationCacheUtils
                  .getVerificationRequirementDtls(
                    reqtDetailsList.dtls.item(j).verificationRequirementID,
                    RECORDSTATUS.CANCELLED);

              boolean verifiationItemProvidedInThisVerification = false;

              final VerificationItemProvidedIDDetailsList verificationItemProvidedIDDetailsList =
                VerificationDeterminatorHelperUtils
                  .getFilteredItemProvidedIDAndVDIEDLinkIDDetails(
                    vdIEDLinkListKey.VDIEDLinkID,
                    details.newUserProvidedVerificationInfo.verificationItemUtilizationID);

              if (verificationItemProvidedIDDetailsList.dtls.size() != 0)
                verifiationItemProvidedInThisVerification = true;

              if (!verifiationItemProvidedInThisVerification) {
                final ItemUtilizationRequirementKey itemUtilizationRequirementKey =
                  new ItemUtilizationRequirementKey();

                itemUtilizationRequirementKey.verifiableDataItemID =
                  vdIEDLinkDtls.verifiableDataItemID;

                itemUtilizationRequirementKey.level =
                  verificationRequirementDtls.verificationLevel;

                itemUtilizationRequirementKey.fromDate =
                  Date.getCurrentDate();
                itemUtilizationRequirementKey.toDate = Date.getCurrentDate();
                itemUtilizationRequirementKey.recordStatus =
                  RECORDSTATUS.CANCELLED;

                processForVerificationItem(details,
                  itemUtilizationRequirementKey, vdIEDLinkListKey);
              }
            }
          }
        }
      }
    }
  }

  private void processForVerificationItem(
    final NewUserProvidedVerificationItemDetails details,
    final ItemUtilizationRequirementKey itemUtilizationRequirementKey,
    final VDIEDLinkKey vdIEDLinkKey)
    throws AppException, InformationalException {

    final VerificationItemProvidedKey verificationItemProvidedKey =
      new VerificationItemProvidedKey();

    final VerificationItemProvided verificationItemProvided =
      VerificationItemProvidedFactory.newInstance();

    final Set<VerificationItemUtilizationDtls> verificationItemUtilizationDtlsSet =
      VerificationConfigurationCacheUtils
        .fetchAllVerificationItemUtilizationsByLevelAndDates(
          itemUtilizationRequirementKey);

    final Iterator<VerificationItemUtilizationDtls> verificationItemUtilizationDtlsIterator =
      verificationItemUtilizationDtlsSet.iterator();

    for (int k = 0; k < verificationItemUtilizationDtlsSet.size(); k++) {

      final VerificationItemUtilizationDtls verificationItemUtilization =
        verificationItemUtilizationDtlsIterator.next();

      if (details.newUserProvidedVerificationInfo.verificationItemUtilizationID == verificationItemUtilization.verificationItemUtilizationID) {

        details.newUserProvidedVerificationInfo.VDIEDLinkID =
          vdIEDLinkKey.VDIEDLinkID;

        try {
          final VerificationItemProvidedDtls verificationItemProvidedDtls =
            VerificationConfigurationCacheUtils
              .convertToVerificationItemProvidedDtls(details);

          verificationItemProvided.insert(verificationItemProvidedDtls);
          verificationItemProvidedKey.verificationItemProvidedID =
            verificationItemProvidedDtls.verificationItemProvidedID;

          if (details.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName
            .length() != 0) {

            final CreateVerificationAttachmentLinkDetails createVerificationAttachmentLinkDetails =
              new CreateVerificationAttachmentLinkDetails();

            createVerificationAttachmentLinkDetails.createLinkDtls.description =
              details.createVerificationAttachmentLinkDetails.createLinkDtls.description;

            createVerificationAttachmentLinkDetails.createLinkDtls.verificationItemProvidedID =
              verificationItemProvidedDtls.verificationItemProvidedID;

            createVerificationAttachmentLinkDetails.createAttachmentDtls =
              details.createVerificationAttachmentLinkDetails.createAttachmentDtls;

            final curam.verification.sl.infrastructure.intf.VerificationAttachmentLink verificationAttachmentLink =
              curam.verification.sl.infrastructure.fact.VerificationAttachmentLinkFactory
                .newInstance();

            verificationAttachmentLink.createVerificationAttachmentLink(
              createVerificationAttachmentLinkDetails);

          }
        } catch (final AppException e) {

          if (!e.getCatEntry().equals(
            ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_VERIFICATION_ITEM_ALREADY_ADDED))

            throw e;
        }
      }
    }
  }

  public void
    raiseVerificationUpdatedEvent(final VerificationItemProvidedKey key)
      throws AppException, InformationalException {

    final Event dueDateExpiredEvent = new Event();

    dueDateExpiredEvent.eventKey = Verification.VerificationUpdated;

    dueDateExpiredEvent.primaryEventData = key.verificationItemProvidedID;
    dueDateExpiredEvent.secondaryEventData = 2L;

    EventService.raiseEvent(dueDateExpiredEvent);
  }

  // START 56348-Task bug Dev bug 54842 Verification Reject Document Display
  // issue
  /**
   *
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public SubmittedDocumentDetailsList
    listReviewedAndRejectedDocuments(final CaseIDAndVDIEDLinkIDKey key)
      throws AppException, InformationalException {

    final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();
    vdiedLinkKey.VDIEDLinkID = key.vdiedLinkID;

    final VerificationDtlsList verificationDtlsList =
      VerificationFactory.newInstance().readByVDIEDLinkID(vdiedLinkKey);

    final SubmittedDocumentDetailsList result =
      new SubmittedDocumentDetailsList();

    final OutstandingVerificationAttachmentLink outstandingVerificationAttachmentLinkObj =
      OutstandingVerificationAttachmentLinkFactory.newInstance();

    for (final VerificationDtls dtls : verificationDtlsList.dtls) {
      if (!dtls.verificationStatus.equals(VERIFICATIONSTATUS.CANCELLED)
        && Long.compare(dtls.verificationLinkedID, key.caseID) == 0) {

        final VerificationKey vKey = new VerificationKey();
        vKey.verificationID = dtls.verificationID;

        final AttachmentLinkAndFurtherInfoDetailsList attachmentList =
          outstandingVerificationAttachmentLinkObj
            .listAttachmentsWithAnyReviewStatusForVerification(vKey);

        for (final AttachmentLinkAndFurtherInfoDetails attachmentDetails : attachmentList.dtls) {

          if (DOCUMENTREVIEWSTATUS.REJECTED
            .equals(attachmentDetails.reviewStatus)) {

            final AttachmentKey attachmentKey = new AttachmentKey();
            attachmentKey.attachmentID = attachmentDetails.attachmentID;

            final AttachmentHeaderDetails attachment =
              AttachmentFactory.newInstance().readHeader(attachmentKey);

            final SubmittedDocumentDetails document =
              new SubmittedDocumentDetails();

            document.relatedVerificationID = vKey.verificationID;
            document.attachmentLinkID = attachmentDetails.attachmentLinkID;
            document.reviewStatus = attachmentDetails.reviewStatus;
            document.attachmentID = attachment.attachmentID;
            document.name = attachment.attachmentName;
            document.receivedDate = attachment.receiptDate;

            final NotFoundIndicator notFoundIndicator =
              new NotFoundIndicator();

            final curam.core.struct.AttachmentInfo attachmentInfo =
              curam.core.fact.AttachmentInfoFactory.newInstance()
                .readByAttachmentID(notFoundIndicator, attachmentKey);
            if (!notFoundIndicator.isNotFound()) {

              document.providedBy =
                this.userDAO.get(attachmentInfo.createdBy).getFullName();

              try {
                document.type = VERIFICATIONITEMNAMEEntry
                  .get(attachmentInfo.documentClassification).getCode();
              } catch (final AppRuntimeException appRuntimeException) {
              }

            }
            result.dtls.add(document);
          }
        }
      }
    }
    return result;
  }
  // END 56348-Task bug Dev bug 54842 Verification Reject Document Display issue

  // Bug 97602 Unhandled exception while verifying from Wizard
  /**
   * Verify from Wizard
   *
   * @param BDMNewUserProvidedVerItemAndEvdDetails
   * @throws AppException, InformationalException
   */
  public
    curam.verification.sl.infrastructure.struct.UserProvidedVerificationItemKey
    newUserProvidedVerificationItemAndAssociatedEvidences(
      final BDMNewUserProvidedVerItemAndEvdDetails details)
      throws AppException, InformationalException {

    final StringList vDIEDLinkIDListToBeProcessed =
      StringUtil.tabText2StringListWithTrim(details.vDIEDLinkIDList);

    if (0 == vDIEDLinkIDListToBeProcessed.size()) {
      final AppException appException = new AppException(
        ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_EVIDENCE_MUST_SELECTED);

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        appException, "", InformationalType.kError, "a", 0);

      TransactionInfo.getInformationalManager().failOperation();
    }
    String verificationItemUsageType = "";

    final curam.verification.sl.entity.intf.VerificationItemUtilization verificationItemUtilization =
      curam.verification.sl.entity.fact.VerificationItemUtilizationFactory
        .newInstance();

    final VDIEDLinkKey vDIEDLinkKey = new VDIEDLinkKey();

    final curam.verification.sl.infrastructure.intf.Verification verification =
      curam.verification.sl.infrastructure.fact.VerificationFactory
        .newInstance();

    String codeValue = null;
    final UsersKey userKey = new UsersKey();

    details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus =
      RECORDSTATUS.DEFAULTCODE;
    userKey.userName = TransactionInfo.getProgramUser();

    details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.addedByUser =
      userKey.userName;

    details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded =
      Date.getCurrentDate();

    final curam.verification.sl.infrastructure.struct.UserProvidedVerificationItemKey userProvidedVerificationItemKey =
      new curam.verification.sl.infrastructure.struct.UserProvidedVerificationItemKey();

    final VerificationItemProvidedKey verificationItemProvidedKey =
      new VerificationItemProvidedKey();

    boolean attachmentCreated = false;
    long attachmentID = 0L;

    String recievedFrom = "";

    for (int i = 0; i < vDIEDLinkIDListToBeProcessed.size(); i++) {
      final String vDIEDLinkIDToBeProcessed =
        vDIEDLinkIDListToBeProcessed.get(i);
      final long vdiedLinkIDToBeProcessedNumber =
        Long.parseLong(vDIEDLinkIDToBeProcessed);
      vDIEDLinkKey.VDIEDLinkID = vdiedLinkIDToBeProcessedNumber;

      final VerifiableDataItemKey verifiableDataItemKey = VDIEDLinkFactory
        .newInstance().readDataItemIDByVDIEDLinkID(vDIEDLinkKey);
      final SearchByVerificationItem searchByVerificationItemObj =
        new SearchByVerificationItem();
      searchByVerificationItemObj.recordStatus = RECORDSTATUS.NORMAL;
      searchByVerificationItemObj.verifiableDataItemID =
        verifiableDataItemKey.verifiableDataItemID;
      searchByVerificationItemObj.verificationItemID =
        details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemIDOpt;

      final VerificationItemUtilizationUsageDetailsList verificationItemUtilizationUsageDetails =
        VerificationItemUtilizationFactory.newInstance()
          .searchByVerificationItemAndVerificableDataItem(
            searchByVerificationItemObj);
      details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo

        .verificationItemUtilizationID =
          verificationItemUtilizationUsageDetails.dtls
            .get(0).verificationItemUtilizationID;
      final VerificationItemUtilizationKey verificationItemUtilizationKey =
        new VerificationItemUtilizationKey();
      verificationItemUtilizationKey.verificationItemUtilizationID =
        details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID;
      final VerificationItemUtilizationDtls verificationItemUtilizationDtls =
        verificationItemUtilization.read(verificationItemUtilizationKey);
      final SecurityDetails securityDetails = new SecurityDetails();
      final SecurityResult securityResult = new SecurityResult();
      securityDetails.addSID = verificationItemUtilizationDtls.addSID;
      securityDetails.removeSID = verificationItemUtilizationDtls.removeSID;
      if (securityDetails.addSID.length() != 0) {
        securityResult.result = Authorisation
          .isSIDAuthorised(securityDetails.addSID, userKey.userName);
        if (!securityResult.result) {
          final AppException appException = new AppException(
            ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_NO_APPROPRIATE_PRIVILEGES);
          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            appException, "", InformationalElement.InformationalType.kError,
            "a", 0);
          TransactionInfo.getInformationalManager().failOperation();
        }
      }
      vDIEDLinkKey.VDIEDLinkID = vdiedLinkIDToBeProcessedNumber;
      checkForSecurity(vDIEDLinkKey, (short) 1);
      verificationItemUsageType = verificationItemUtilizationDtls.usageType;
      codeValue = verificationItemUtilization
        .readVerificationItemName(verificationItemUtilizationKey).name;
      final String result =
        VerificationDeterminatorHelperUtils.getOneItemForUserLocale(codeValue,
          TransactionInfo.getProgramLocale(), VERIFICATIONITEMNAME.TABLENAME);
      details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.verificationItemName =
        result;
      final VDIEDLinkKey vdIEDLinkKey = new VDIEDLinkKey();
      vdIEDLinkKey.VDIEDLinkID = vdiedLinkIDToBeProcessedNumber;
      final VDIEDLink vdIEDLinkObj = VDIEDLinkFactory.newInstance();
      final EvidenceDescriptorIDRelatedIDAndEvidenceType evidenceDescriptorIDRelatedIDAndEvidenceType =
        vdIEDLinkObj
          .readEDIDRelatedIDAndEvidenceTypeByVDIEDLinkID(vdIEDLinkKey);
      boolean isNonCaseEDForParticipantEvidence = false;
      final EvidenceControllerInterface evidenceControllerObj =
        (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
      isNonCaseEDForParticipantEvidence =
        evidenceControllerObj.isNonCaseEDForParticipantEvidence(
          evidenceDescriptorIDRelatedIDAndEvidenceType);
      final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();
      evidenceTypeKey.evidenceType =
        evidenceDescriptorIDRelatedIDAndEvidenceType.evidenceType;
      final boolean isPDCEvidence =
        evidenceControllerObj.isPDCEvidence(evidenceTypeKey);
      if (!isNonCaseEDForParticipantEvidence && !isPDCEvidence) {
        final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
        final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
        caseHeaderKey.caseID =
          details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseID;
        final CaseStatusCode caseStatusCode =
          caseHeaderObj.readCaseStatus(caseHeaderKey);
        if (caseStatusCode.statusCode.equals(CASESTATUS.CLOSED))
          ValidationManagerFactory.getManager()
            .throwWithLookup(new AppException(
              ENTVERIFICATIONITEMPROVISION.ERR_VERIFICATIONITEMPROVISION_FV_CASE_CLOSED),
              "a", 1);
      }
      if (recievedFrom.equals("")) {
        recievedFrom = registerProviderDetails1(
          details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails,
          null, null, isNonCaseEDForParticipantEvidence, isPDCEvidence);
        details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.receivedFrom =
          recievedFrom;
      }

      final NewUserProvidedVerItemAndEvdDetails newUserProvidedVerItemAndEvdDetails1 =
        populateNewUserProvidedVerItemAndEvdDetails(details);

      final NewUserProvidedVerificationItemDetails tempNewUserProvidedVerificationItemDetails =
        populateNewUserProvidedVerificationItemDetailsForCreationOfMultiVerificationItemPrvDtls(
          vdiedLinkIDToBeProcessedNumber,
          newUserProvidedVerItemAndEvdDetails1);

      final Set<VerificationItemProvidedDtls> verificationItemProvidedDtlsSet =
        convertToMultiVerificationItemProvidedDtls(
          tempNewUserProvidedVerificationItemDetails);
      for (final VerificationItemProvidedDtls verificationItemProvidedDtls : verificationItemProvidedDtlsSet) {
        if (vdiedLinkIDToBeProcessedNumber == verificationItemProvidedDtls.VDIEDLinkID) {
          final VerificationItemProvided verificationItemProvided =
            VerificationItemProvidedFactory.newInstance();
          verificationItemProvided.insert(verificationItemProvidedDtls);
          verificationItemProvidedKey.verificationItemProvidedID =
            verificationItemProvidedDtls.verificationItemProvidedID;
          userProvidedVerificationItemKey.verificationItemProvidedID =
            verificationItemProvidedDtls.verificationItemProvidedID;
          final VerificationController verificationControllerObj =
            new VerificationController();
          final CreateVerificationAttachmentLinkDetails createVerificationAttachmentLinkDetails =
            new CreateVerificationAttachmentLinkDetails();
          createVerificationAttachmentLinkDetails.createLinkDtls.description =
            details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description;
          createVerificationAttachmentLinkDetails.createLinkDtls.verificationItemProvidedID =
            verificationItemProvidedDtls.verificationItemProvidedID;
          createVerificationAttachmentLinkDetails.createAttachmentDtls =
            details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls;

          final curam.verification.sl.infrastructure.intf.VerificationAttachmentLink verificationAttachmentLink =
            curam.verification.sl.infrastructure.fact.VerificationAttachmentLinkFactory
              .newInstance();
          if (!attachmentCreated) {
            verificationAttachmentLink.createVerificationAttachmentLink(
              createVerificationAttachmentLinkDetails);
            attachmentID =
              createVerificationAttachmentLinkDetails.createLinkDtls.attachmentID;
            if (attachmentID != 0L)
              attachmentCreated = true;
          } else {
            createVerificationAttachmentLinkDetails.createLinkDtls.attachmentID =
              attachmentID;
            createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
              RECORDSTATUS.DEFAULTCODE;
            VerificationAttachmentLinkFactory.newInstance()
              .insert(createVerificationAttachmentLinkDetails.createLinkDtls);
          }
          final long tempCurrentVDIEDLinkID = vdiedLinkIDToBeProcessedNumber;
          if (!isNonCaseEDForParticipantEvidence) {
            final NewUserProvidedVerificationItemDetails newUserProvidedVerificationItemDetails =
              details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails;
            newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
              vdiedLinkIDToBeProcessedNumber;
            propagateVerificationItem(newUserProvidedVerificationItemDetails);
          }
          if (StringUtil.isNullOrEmpty(verificationItemUsageType)
            || VERIFICATIONITEMUSAGETYPE.SHARED

              .equals(verificationItemUsageType)) {
            final NewUserProvidedVerificationItemDetails newUserProvidedVerificationItemDetails =
              details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails;
            newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
              vdiedLinkIDToBeProcessedNumber;
            cloneItemProvided(newUserProvidedVerificationItemDetails);
          }
          details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
            tempCurrentVDIEDLinkID;
          raiseVerificationUpdatedEvent(verificationItemProvidedKey);
          final UtlizationExpiryWarningAndDateFrom utlizationExpiryWarningAndDateFrom =
            new UtlizationExpiryWarningAndDateFrom();
          utlizationExpiryWarningAndDateFrom.expiryDays =
            verificationItemUtilizationDtls.expiryDays;
          utlizationExpiryWarningAndDateFrom.warningDays =
            verificationItemUtilizationDtls.warningDays;
          utlizationExpiryWarningAndDateFrom.expiryDateFrom =
            verificationItemUtilizationDtls.expiryDateFrom;
          utlizationExpiryWarningAndDateFrom.verificationItemUtilizationID =
            verificationItemUtilizationDtls.verificationItemUtilizationID;
          if (utlizationExpiryWarningAndDateFrom.expiryDays > 0
            && !isNonCaseEDForParticipantEvidence && !isPDCEvidence)
            verificationControllerObj.raiseEvents(
              utlizationExpiryWarningAndDateFrom,
              verificationItemProvidedKey);
        }
      }
      vDIEDLinkKey.VDIEDLinkID = vdiedLinkIDToBeProcessedNumber;
      verification.determineVerificationStatus(vDIEDLinkKey);
      if (details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails != null
        && details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls != null
        && details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseID != 0L) {
        String dataItem = null;
        Long verifiableDataItemID = null;
        final VerifiableDataItem verifiableDataItemObj =
          VerifiableDataItemFactory.newInstance();
        verifiableDataItemID =
          Long.valueOf(verificationItemUtilizationDtls.verifiableDataItemID);
        final VerifiableDataItemKey varifiableDataItemKey =
          new VerifiableDataItemKey();
        varifiableDataItemKey.verifiableDataItemID =
          verifiableDataItemID.longValue();
        final VerifiableDataItemDataItemNameAndIDDetails verifiableDataItemDataItemNameAndIDDetails =
          verifiableDataItemObj.searchDataItemNameAndIDByVerifiableDataItem(
            varifiableDataItemKey);
        dataItem = CodeTable.getOneItem(VERIFIABLEITEMNAME.TABLENAME,
          verifiableDataItemDataItemNameAndIDDetails.name,

          TransactionInfo.getProgramLocale());
        final LocalisableString description =
          new LocalisableString(BPOCASEEVENTS.VERIFICATION_COMPLETED)
            .arg(dataItem);
        this.caseTransactionLogProvider.get().recordCaseTransaction(
          CASETRANSACTIONEVENTS.VERIFICATION_COMPLETED, description,
          details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseID,
          evidenceDescriptorIDRelatedIDAndEvidenceType.evidenceDescriptorID);
      }
    }
    return userProvidedVerificationItemKey;
  }

  /**
   * Populate populate NewUserProvidedVerItemAndEvdDetails Struct
   *
   * @param details
   * @return
   */
  private NewUserProvidedVerItemAndEvdDetails
    populateNewUserProvidedVerItemAndEvdDetails(
      final BDMNewUserProvidedVerItemAndEvdDetails details) {

    final NewUserProvidedVerItemAndEvdDetails newUserProvidedVerItemAndEvdDetails1 =
      new NewUserProvidedVerItemAndEvdDetails();
    newUserProvidedVerItemAndEvdDetails1.dtls.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails
      .assign(
        details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails);
    newUserProvidedVerItemAndEvdDetails1.dtls.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls
      .assign(
        details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls);
    newUserProvidedVerItemAndEvdDetails1.dtls.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo
      .assign(
        details.bdmNewUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo);
    newUserProvidedVerItemAndEvdDetails1.comments = details.comments;
    newUserProvidedVerItemAndEvdDetails1.vDIEDLinkIDList =
      details.vDIEDLinkIDList;
    return newUserProvidedVerItemAndEvdDetails1;
  }

  /**
   * Populate populate New Use rProvided VerificationI tem Details Struct
   *
   * @param long vdiedLinkIDToBeProcessedNumbe
   * @param NewUserProvidedVerItemAndEvdDetails details
   * @return NewUserProvidedVerificationItemDetails
   */

  private NewUserProvidedVerificationItemDetails
    populateNewUserProvidedVerificationItemDetailsForCreationOfMultiVerificationItemPrvDtls(
      final long vdiedLinkIDToBeProcessedNumber,
      final NewUserProvidedVerItemAndEvdDetails details) {

    final NewUserProvidedVerificationItemDetails newUserProvidedVerificationItemDetails =
      new NewUserProvidedVerificationItemDetails();
    newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.addedByUser =
      details.dtls.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.addedByUser;
    newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.comments =
      details.comments;
    newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded =
      details.dtls.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded;
    newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived =
      details.dtls.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived;
    newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.receivedFrom =
      details.dtls.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.receivedFrom;
    newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus =
      details.dtls.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus;
    newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
      vdiedLinkIDToBeProcessedNumber;
    newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
      details.dtls.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID;
    return newUserProvidedVerificationItemDetails;
  }

}
