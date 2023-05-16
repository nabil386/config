package curam.ca.gc.bdm.facade.person.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.entity.communication.struct.BDMSearchByTrackingNumberKey;
import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNum;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNumList;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonMergeTaskDetails;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonMergeValidations;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchDetailsResult;
import curam.ca.gc.bdm.facade.person.struct.BDMSearchNonDuplicatePersonKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.MERGEENTITY;
import curam.core.facade.fact.ClientMergeFactory;
import curam.core.facade.fact.ParticipantFactory;
import curam.core.facade.infrastructure.struct.EvidenceTypeAndParticipantIDDetails;
import curam.core.facade.intf.ClientMerge;
import curam.core.facade.intf.Participant;
import curam.core.facade.struct.AgendaDetails;
import curam.core.facade.struct.ClientMergeAgendaKey;
import curam.core.facade.struct.ClientMergeParticipantKey;
import curam.core.facade.struct.ConcernRoleDetailsForMerge;
import curam.core.facade.struct.EvidenceMergeList;
import curam.core.facade.struct.ListParticipantTaskKey_eo;
import curam.core.facade.struct.MarkDuplicateCreateDetails;
import curam.core.facade.struct.MarkDuplicateDetails;
import curam.core.facade.struct.PersonSearchDetailsResult;
import curam.core.facade.struct.SearchNonDuplicatePersonKey;
import curam.core.facade.struct.TasksForConcernAndCaseDetails;
import curam.core.fact.UsersFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.Users;
import curam.core.sl.entity.fact.MergedRecordFactory;
import curam.core.sl.entity.intf.MergedRecord;
import curam.core.sl.entity.struct.MergedRecordDtls;
import curam.core.sl.entity.struct.RecordIDAndType;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceTypeWorkspaceKey;
import curam.core.sl.infrastructure.struct.EvidenceTypeWorkspaceListDetails;
import curam.core.sl.struct.BooleanIndicator;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.ClientMergeEvidenceParticipantKey;
import curam.core.sl.struct.EvidenceCaseKey;
import curam.core.sl.struct.MergeTabList;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.EvidenceType;
import curam.core.struct.InformationalMsgDtls;
import curam.core.struct.UsersDtls;
import curam.core.struct.UsersKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.sl.entity.fact.DynamicEvidenceDataAttributeFactory;
import curam.dynamicevidence.sl.entity.intf.DynamicEvidenceDataAttribute;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeKey;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.ReadEvidenceDetails;
import curam.message.GENERALSEARCH;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.fact.ParticipantDataCaseFactory;
import curam.pdc.intf.PDCUtil;
import curam.pdc.intf.ParticipantDataCase;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.util.type.StringList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static curam.ca.gc.bdm.util.impl.BDMSinNumberUtil.isValidSearchCombination;
import static curam.ca.gc.bdm.util.impl.BDMSinNumberUtil.isValidSearchCriteria;
import static curam.ca.gc.bdm.util.impl.BDMSinNumberUtil.maskSinNumber;

/**
 *
 * custom Facade Wrapper class for ClientMerge
 *
 */
public class BDMClientMerge
  extends curam.ca.gc.bdm.facade.person.base.BDMClientMerge {

  private final BDMUtil bdmUtil = new BDMUtil();

  curam.core.sl.intf.ClientMerge clientMergeObj =
    curam.core.sl.fact.ClientMergeFactory.newInstance();

  public static final String SIN_START_WITH_9 = "9";

  public static final String SIN_START_WITH_0 = "0";

  private static final boolean PDCENABLEDIND =
    Configuration.getBooleanProperty(EnvVars.ENV_PDC_ENABLED);

  public static final String FRENCH_LOCALE = "fr";

  public BDMClientMerge() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Duplicate Person and Prospect search - 8914
   * This method calls the OOTB person search and then filters search result by
   * using additional search criteria of province, postal code and country
   */
  @Override
  public BDMPersonSearchDetailsResult searchNonDuplicatePersonDetailsExt(
    final BDMSearchNonDuplicatePersonKey key)
    throws AppException, InformationalException {

    if (!isValidSearchCriteria(
      key.personSearchKey.dtls.personSearchKey.referenceNumber,
      key.personSearchKey.corrTrackingNumber,
      key.personSearchKey.dtls.personSearchKey.forename,
      key.personSearchKey.dtls.personSearchKey.surname,
      key.personSearchKey.dtls.personSearchKey.birthSurname)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_INVALID_PERSON_SEARCH_CRITERIA), "",
        InformationalType.kError);
    }
    if (!isValidSearchCombination(
      key.personSearchKey.dtls.personSearchKey.referenceNumber,
      key.personSearchKey.corrTrackingNumber,
      key.personSearchKey.dtls.personSearchKey.forename,
      key.personSearchKey.dtls.personSearchKey.surname,
      key.personSearchKey.dtls.personSearchKey.dateOfBirth,
      key.personSearchKey.stateProvince, key.personSearchKey.postalCode,
      key.personSearchKey.countryCode,
      key.personSearchKey.dtls.personSearchKey.birthSurname)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_SEARCH_ADDITIONAL_MANDATORY_MISSING),
        "", InformationalType.kError);
    }

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final SearchNonDuplicatePersonKey searchNonDuplicatePersonKey =
      new SearchNonDuplicatePersonKey();

    searchNonDuplicatePersonKey.assign(key);
    searchNonDuplicatePersonKey.originalConcernRoleID =
      key.originalConcernRoleID;

    searchNonDuplicatePersonKey.personSearchKey
      .assign(key.personSearchKey.dtls.personSearchKey);

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (key.personSearchKey.dtls.personSearchKey.referenceNumber.isEmpty()
      && key.personSearchKey.corrTrackingNumber.isEmpty()
      && key.personSearchKey.dtls.personSearchKey.forename.isEmpty()
      && key.personSearchKey.dtls.personSearchKey.surname.isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_SEARCH_MANDATORY_MISSING), "",
        InformationalType.kError);
    }

    if (!key.personSearchKey.dtls.personSearchKey.addressDtls.city.isEmpty()
      && key.personSearchKey.dtls.personSearchKey.addressDtls.city
        .length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_CITY_2CHARS), "",
        InformationalType.kError);
    }

    if (!key.personSearchKey.stateProvince.isEmpty()
      && key.personSearchKey.stateProvince.length() < 3) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_PROVINCE_3CHARS), "",
        InformationalType.kError);
    }
    if (!key.personSearchKey.postalCode.isEmpty()
      && key.personSearchKey.postalCode.length() < 3) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_POSTALCODE_3CHARS), "",
        InformationalType.kError);
    }

    informationalManager.failOperation();

    String altId = null;

    // Perform search when both reference number and tracking number are
    // provided
    if (!key.personSearchKey.corrTrackingNumber.isEmpty()
      && !key.personSearchKey.dtls.personSearchKey.referenceNumber
        .isEmpty()) {

      // Perform OOTB serach for reference Number and additional search criteria
      final BDMPersonSearchDetailsResult refSearchResult =
        performOOTBSearch(key);

      // Perform serach by tracking Number
      altId = getAlternateIDByTrackingNumber(key);
      if (!Objects.isNull(altId)) {
        key.personSearchKey.dtls.personSearchKey.referenceNumber = altId;
      }

      // Perform serach by tracking Number and additional criteria
      final BDMPersonSearchDetailsResult trackingNumberSearchResult =
        performOOTBSearch(key);

      // comapre 2 results
      bdmPersonSearchDetailsResult =
        compareResults(refSearchResult, trackingNumberSearchResult);

    } else {
      // Perform search when either reference number or tracking number is
      // provided

      if (!key.personSearchKey.corrTrackingNumber.isEmpty()) {
        altId = getAlternateIDByTrackingNumber(key);
      }
      // When only tracking number is specified
      if (!Objects.isNull(altId)) {
        key.personSearchKey.dtls.personSearchKey.referenceNumber = altId;
      }

      // OOTB Logic
      bdmPersonSearchDetailsResult = performOOTBSearch(key);

    }

    if (bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(GENERALSEARCH.INF_SEARCH_NORECORDSFOUND), "",
        InformationalType.kError);
    }

    informationalManager.failOperation();
    maskSinNumber(bdmPersonSearchDetailsResult);
    return bdmPersonSearchDetailsResult;
  }

  /**
   * Retrive alternate id of a person when correspondence tracking number is
   * provided
   *
   * @param key
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  private String
    getAlternateIDByTrackingNumber(final BDMSearchNonDuplicatePersonKey key)
      throws AppException, InformationalException {

    List<String> alternalteIDList = new ArrayList<String>();
    String altId = "";
    long trackingNum = 0;
    try {
      trackingNum = Long.parseLong(key.personSearchKey.corrTrackingNumber);

      final BDMSearchByTrackingNumberKey trackingKey =
        new BDMSearchByTrackingNumberKey();

      trackingKey.trackingNumber = trackingNum;

      final BDMPersonSearchResultByTrackingNumList bdmPersonSearchResultByTrackingNumList =
        BDMPersonFactory.newInstance().searchByTrackingNumber(trackingKey);

      if (!bdmPersonSearchResultByTrackingNumList.dtls.isEmpty()) {
        alternalteIDList =
          getPersonSearchResult(bdmPersonSearchResultByTrackingNumList);
      } else
        altId = String.valueOf(0);

      if (!alternalteIDList.isEmpty() && alternalteIDList.size() == 1) {
        altId = alternalteIDList.get(0);
      }
    } catch (final NumberFormatException exception) {
      Trace.kTopLevelLogger
        .error("Invalid Tracking Number:" + exception.getMessage());
      trackingNum = 0;
      altId = String.valueOf(0);
    }
    return altId;
  }

  // STRAT : Bug 115707: Taxonomy - Incorrect french labels in Mark Duplicate
  // modal

  @Override
  public AgendaDetails getAgenda(final ClientMergeAgendaKey key)
    throws AppException, InformationalException {

    String kBDMMergeMaritalStatusEvdPage = "";
    String kBDMMergeMaritalStatusPage = "";
    String maritalstatusDesc = "";

    AgendaDetails agendaDetails = null;
    final ClientMerge clientMerge = ClientMergeFactory.newInstance();

    agendaDetails = clientMerge.getAgenda(key);

    if (checkForClientMaritalStatus(key.duplicateConcernRoleID)) {

      maritalstatusDesc = getMaritalStatusDescByLocale();

      final String corePages = agendaDetails.agenda;
      final String test1 =
        corePages.substring(0, corePages.indexOf(BDMConstants.kSummary));
      String maritalStatusPage = BDMConstants.EMPTY_STRING;
      if (PDCENABLEDIND) {

        kBDMMergeMaritalStatusEvdPage =
          BDMConstants.kBDMMergeMaritalStatusEvdPage1 + maritalstatusDesc
            + BDMConstants.kBDMMergeMaritalStatusEvdPage2;
        maritalStatusPage = test1.concat(kBDMMergeMaritalStatusEvdPage);
      } else {

        kBDMMergeMaritalStatusPage = BDMConstants.kBDMMergeMaritalStatusPage1
          + maritalstatusDesc + BDMConstants.kBDMMergeMaritalStatusPage2;
        maritalStatusPage = test1.concat(kBDMMergeMaritalStatusPage);
      }

      final String customXML = maritalStatusPage.concat(
        "\n" + corePages.substring(corePages.indexOf(BDMConstants.kSummary)));
      agendaDetails.agenda = customXML;
    }

    return agendaDetails;
  }

  /**
   * Get logged in Users Locale
   *
   */
  private String getUserLocale() throws AppException, InformationalException {

    final String currentLoggedInUser = TransactionInfo.getProgramUser();
    final Users usersObj = UsersFactory.newInstance();

    final UsersKey usersKey = new UsersKey();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    usersKey.userName = currentLoggedInUser;

    final UsersDtls usersDtls = usersObj.read(nfIndicator, usersKey);

    return usersDtls.defaultLocale;

  }

  /**
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getMaritalStatusDescByLocale()
    throws AppException, InformationalException {

    String maritalstatusDesc;
    final String userLocale = getUserLocale();

    if (userLocale.equalsIgnoreCase(FRENCH_LOCALE)) {
      maritalstatusDesc = BDMConstants.PAGE_DESC_MARTIAL_STATUS_FR;
    } else
      maritalstatusDesc = BDMConstants.PAGE_DESC_MARTIAL_STATUS_EN;
    return maritalstatusDesc;
  }

  protected boolean checkForClientMaritalStatus(final long concernRoleID)
    throws AppException, InformationalException {

    boolean displayMaritalStatus = false;
    if (PDCENABLEDIND) {
      final EvidenceTypeAndParticipantIDDetails evidenceTypeAndParticipantIDDetails =
        new EvidenceTypeAndParticipantIDDetails();

      evidenceTypeAndParticipantIDDetails.participantID = concernRoleID;
      evidenceTypeAndParticipantIDDetails.evidenceType =
        CASEEVIDENCE.BDM_MARITAL_STATUS;
      displayMaritalStatus =
        this.checkForClientEvidence(evidenceTypeAndParticipantIDDetails).flag
          && Configuration.getBooleanProperty(
            EnvVars.ENV_PARTICIPANT_CLIENTMERGE_MERGE_MARITAL_STATUS);
    }
    return displayMaritalStatus;
  }

  /**
   * Check if participant has evidence recorded.
   *
   * @param key Contains concern role identifier and evidence type code.
   *
   * @return boolean, true if there exists evidence for the concern role.
   *
   * @throws InformationalException Generic Exception Signature.
   * @throws AppException Generic Exception Signature.
   */
  protected BooleanIndicator
    checkForClientEvidence(final EvidenceTypeAndParticipantIDDetails key)
      throws AppException, InformationalException {

    final BooleanIndicator indicator = new BooleanIndicator();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = key.participantID;

    final ParticipantDataCase participantDataCaseObj =
      ParticipantDataCaseFactory.newInstance();

    final CaseIDKey pdcCaseKey =
      participantDataCaseObj.getParticipantDataCase(concernRoleKey);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EvidenceTypeWorkspaceKey evidenceTypeWorkspaceKey =
      new EvidenceTypeWorkspaceKey();

    evidenceTypeWorkspaceKey.evidenceType = key.evidenceType;
    evidenceTypeWorkspaceKey.showCancelledEvidence = false;
    evidenceTypeWorkspaceKey.participantID = key.participantID;
    evidenceTypeWorkspaceKey.caseID = pdcCaseKey.caseID;

    final EvidenceTypeWorkspaceListDetails evidenceListDetails =
      evidenceControllerObj.listEvidenceTypes(evidenceTypeWorkspaceKey);
    indicator.flag = !evidenceListDetails.list.isEmpty();
    return indicator;
  }

  @Override
  public EvidenceMergeList
    listMaritalStatusEvidenceForMerge(final ClientMergeParticipantKey key)
      throws AppException, InformationalException {

    final EvidenceMergeList evidenceMergeList = new EvidenceMergeList();

    final ClientMergeEvidenceParticipantKey clientMergeEvidenceParticipantKey =
      new ClientMergeEvidenceParticipantKey();
    clientMergeEvidenceParticipantKey.assign(key.key);
    clientMergeEvidenceParticipantKey.evidenceType =
      CASEEVIDENCE.BDM_MARITAL_STATUS;

    evidenceMergeList.evidenceList =
      clientMergeObj.listEvidenceForMerge(clientMergeEvidenceParticipantKey);

    return evidenceMergeList;
  }

  @Override
  public BDMPersonMergeTaskDetails
    isTaskExistsForDuplicatePerson(final ConcernRoleKey concernRoleKey)
      throws AppException, InformationalException {

    final BDMPersonMergeTaskDetails bdmPersonMergeDetails =
      new BDMPersonMergeTaskDetails();

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final ListParticipantTaskKey_eo key_eo = new ListParticipantTaskKey_eo();
    key_eo.concernRoleTasksKey.concernRoleID = concernRoleKey.concernRoleID;

    final Participant participant = ParticipantFactory.newInstance();
    final TasksForConcernAndCaseDetails tasksForConcernAndCaseDetails =
      participant.listParticipantTask(key_eo);

    if (!tasksForConcernAndCaseDetails.detailsList.dtls.dtls.isEmpty()) {
      bdmPersonMergeDetails.statusInd = true;
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_PERSON_MERGE_TASK_EXISTS), "",
        InformationalType.kWarning);
    }

    final String[] infos = informationalManager.obtainInformationalAsString();
    for (final String message : infos) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = message;
      bdmPersonMergeDetails.informationalMsgDtls.dtls
        .addRef(informationalMsgDtls);
    }

    return bdmPersonMergeDetails;
  }

  @Override
  public BDMPersonMergeValidations
    markDuplicate(final MarkDuplicateCreateDetails key)
      throws AppException, InformationalException {

    final BDMPersonMergeValidations bdmPersonMergeValidations =
      new BDMPersonMergeValidations();

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    String duplicateConcernSINNumber = "";
    String originalConcernSINNumber = "";

    // Get original person SIN Number
    if (key.markDuplicateCreateDetails.originalConcernRoleID != BDMConstants.kZeroLongValue) {
      originalConcernSINNumber = bdmUtil.getSINNumberForPerson(
        key.markDuplicateCreateDetails.originalConcernRoleID);
    }

    if (key.markDuplicateCreateDetails.duplicateConcernRoleID != BDMConstants.kZeroLongValue) {
      duplicateConcernSINNumber = bdmUtil.getSINNumberForPerson(
        key.markDuplicateCreateDetails.duplicateConcernRoleID);
    }

    // Person merge should not be possible if one client has a temporary SIN,
    // and the other has no SIN/a non-SIN identifier
    if ((originalConcernSINNumber.startsWith(SIN_START_WITH_0)
      || originalConcernSINNumber.startsWith(SIN_START_WITH_9))
      && StringUtil.isNullOrEmpty(duplicateConcernSINNumber)) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMPERSON.ERR_PERSON_MERGE_MASTER_PERSON_TEMPORARY_SIN),
        "", InformationalType.kError);
    }

    // Person merge should not be possible if both clients have a temporary SIN
    if ((originalConcernSINNumber.startsWith(SIN_START_WITH_0)
      || originalConcernSINNumber.startsWith(SIN_START_WITH_9))
      && (duplicateConcernSINNumber.startsWith(SIN_START_WITH_0)
        || duplicateConcernSINNumber.startsWith(SIN_START_WITH_9))) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_PERSON_MERGE_TEMPORARY_SIN_FOR_BOTH),
        "", InformationalType.kError);
    }

    // check Marital status evidence overlapping
    checkMaritalStatusEvidenceOverlapping(key);

    final ClientMergeParticipantKey clientMergeParticipantKey =
      new ClientMergeParticipantKey();
    clientMergeParticipantKey.key.assign(key.markDuplicateCreateDetails);
    informationalManager.failOperation();

    final ClientMerge clientMerge = ClientMergeFactory.newInstance();
    final MarkDuplicateDetails markDuplicateDetails =
      clientMerge.markDuplicate(key);
    bdmPersonMergeValidations.dtls.assign(markDuplicateDetails);

    return bdmPersonMergeValidations;
  }

  /*
   * validate marital status evidence overlapping
   *
   */
  private void checkMaritalStatusEvidenceOverlapping(
    final MarkDuplicateCreateDetails key)
    throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDtlsList originalConcernattributeDtlsList =
      bdmUtil.getMaritalStatusEvidenceForPerson(
        key.markDuplicateCreateDetails.originalConcernRoleID);

    String originalConcernStartDate = "";
    String duplicateConcernStartDate = "";
    String originalConcernendDate = "";
    String duplicateConcernendDate = "";
    java.util.Date originalConcernStartDt = null;
    java.util.Date originalConcernEndDt = null;
    java.util.Date duplicateConcernStartDt = null;
    java.util.Date duplicateConcernEndDt = null;

    if (!originalConcernattributeDtlsList.dtls.isEmpty()) {
      for (final DynamicEvidenceDataAttributeDtls originalConcernDtls : originalConcernattributeDtlsList.dtls) {
        if (originalConcernDtls.name.equalsIgnoreCase("startDate")) {
          originalConcernStartDate = originalConcernDtls.value;
        }
        if (originalConcernDtls.name.equalsIgnoreCase("endDate")) {
          originalConcernendDate = originalConcernDtls.value;
        }
      }
      originalConcernStartDt = BDMDateUtil.getDate(originalConcernStartDate);
      originalConcernEndDt = BDMDateUtil.getDate(originalConcernendDate);
    }

    final DynamicEvidenceDataAttributeDtlsList duplicateConcernattributeDtlsList =
      bdmUtil.getMaritalStatusEvidenceForPerson(
        key.markDuplicateCreateDetails.duplicateConcernRoleID);

    if (!duplicateConcernattributeDtlsList.dtls.isEmpty()) {
      for (final DynamicEvidenceDataAttributeDtls duplicateConcernDtls : duplicateConcernattributeDtlsList.dtls) {
        if (duplicateConcernDtls.name.equalsIgnoreCase("startDate")) {
          duplicateConcernStartDate = duplicateConcernDtls.value;
        }
        if (duplicateConcernDtls.name.equalsIgnoreCase("endDate")) {
          duplicateConcernendDate = duplicateConcernDtls.value;
        }
      }
      duplicateConcernStartDt =
        BDMDateUtil.getDate(duplicateConcernStartDate);
      duplicateConcernEndDt = BDMDateUtil.getDate(duplicateConcernendDate);
    }

    final boolean isOverlapping =
      BDMDateUtil.checkTimeOverlaps(originalConcernStartDt,
        originalConcernEndDt, duplicateConcernStartDt, duplicateConcernEndDt);

    // if marital status is overlapping for the same period then merge should
    // not be allowed
    if (isOverlapping) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_PERSON_MERGE_MARITAL_STATUS_OVERLAP),
        "", InformationalType.kError);
    }
  }

  @Override
  public void mergeMaritalStatusEvidence(
    final ConcernRoleDetailsForMerge details, final MergeTabList tabList)
    throws AppException, InformationalException {

    final EvidenceType evidenceType = new EvidenceType();

    evidenceType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    this.mergeEvidence(details.dtls, tabList, evidenceType);
  }

  /**
   * Method to merge marital status evidence
   *
   * @param details
   * @param tabList
   * @param evidenceType
   * @throws AppException
   * @throws InformationalException
   */
  private void mergeEvidence(
    final curam.core.sl.struct.ConcernRoleDetailsForMerge details,
    final MergeTabList tabList, final EvidenceType evidenceType)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = details.originalConcernRoleID;

    // Get the PDC case id
    final PDCUtil pdcUtilObj = PDCUtilFactory.newInstance();

    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtilObj.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();

    eiEvidenceKey.evidenceType = evidenceType.evidenceType;

    final MergedRecordDtls mergedRecordDtls = new MergedRecordDtls();

    // BEGIN, CR00364764, ZV
    mergedRecordDtls.recordType = MERGEENTITY.EVIDENCE;
    // END, CR00364764
    mergedRecordDtls.concernRoleMergeID = details.concernRoleMergeID;

    // Convert the tab delimited string to a list of strings
    final StringList stringList =
      StringUtil.tabText2StringList(tabList.mergeTabList);

    for (int i = 0; i < stringList.size(); i++) {

      eiEvidenceKey.evidenceID = Long.parseLong(stringList.item(i));
      // BEGIN, CR00362310, ZV
      final RecordIDAndType recordIDAndType = new RecordIDAndType();

      recordIDAndType.recordID = eiEvidenceKey.evidenceID;
      recordIDAndType.recordType = mergedRecordDtls.recordType;

      if (!clientMergeObj.isMergedRecord(recordIDAndType).statusInd) {

        final EIEvidenceReadDtls evidenceReadDtls =
          evidenceControllerObj.readEvidence(eiEvidenceKey);

        final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
          new EvidenceDescriptorInsertDtls();

        evidenceDescriptorInsertDtls.participantID =
          details.originalConcernRoleID;
        evidenceDescriptorInsertDtls.evidenceType =
          evidenceReadDtls.descriptor.evidenceType;
        evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
        evidenceDescriptorInsertDtls.caseID =
          pdcCaseIDCaseParticipantRoleID.caseID;

        // Evidence Interface details
        final EIEvidenceInsertDtls eiEvidenceInsertDtls =
          new EIEvidenceInsertDtls();

        eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
        eiEvidenceInsertDtls.descriptor.participantID =
          details.originalConcernRoleID;
        eiEvidenceInsertDtls.descriptor.changeReason =
          EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
        eiEvidenceInsertDtls.evidenceObject = evidenceReadDtls.evidenceObject;

        // Insert the evidence
        final EIEvidenceKey clonedEvidenceKey =
          evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);

        mergedRecordDtls.mergedFromRecordID = eiEvidenceKey.evidenceID;
        mergedRecordDtls.mergedToRecordID = clonedEvidenceKey.evidenceID;

        // create a new mergedRecord
        // MergedRecord manipulation variables
        final MergedRecord mergedRecordObj =
          MergedRecordFactory.newInstance();

        // create the new MergedRecord
        mergedRecordObj.insert(mergedRecordDtls);
      }
      // END, CR00362310
    }
  }

  private List<String>
    getPersonSearchResult(final BDMPersonSearchResultByTrackingNumList str) {

    final List<String> alternateIDList = new ArrayList<>();
    for (int i = 0; i < str.dtls.size(); i++) {

      final BDMPersonSearchResultByTrackingNum obj = str.dtls.get(i);

      alternateIDList.add(obj.primaryAlternateID);

    }
    return alternateIDList;

  }

  /**
   * Performs OOTB serach and filter results based on addition search criteria
   * Comment : Extractd OOTB search code to reduce Complexity of method
   */
  private BDMPersonSearchDetailsResult performOOTBSearch(
    final BDMSearchNonDuplicatePersonKey bdmSearchNonDuplicatePersonKey)
    throws AppException, InformationalException {

    final SearchNonDuplicatePersonKey searchNonDuplicatePersonKey =
      new SearchNonDuplicatePersonKey();
    searchNonDuplicatePersonKey.assign(bdmSearchNonDuplicatePersonKey);
    searchNonDuplicatePersonKey.originalConcernRoleID =
      bdmSearchNonDuplicatePersonKey.originalConcernRoleID;

    searchNonDuplicatePersonKey.personSearchKey.assign(
      bdmSearchNonDuplicatePersonKey.personSearchKey.dtls.personSearchKey);

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();
    // OOTB Logic
    final ClientMerge _clientMergeObj = ClientMergeFactory.newInstance();
    final PersonSearchDetailsResult personSearchDetailsResult =
      _clientMergeObj
        .searchNonDuplicatePersonDetails(searchNonDuplicatePersonKey);

    bdmPersonSearchDetailsResult.assign(personSearchDetailsResult);

    // call filtering logic
    if (bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() > 0) {

      bdmPersonSearchDetailsResult =
        bdmUtil.filterAddressForPerson(bdmPersonSearchDetailsResult,
          bdmSearchNonDuplicatePersonKey.personSearchKey);

    }
    return bdmPersonSearchDetailsResult;
  }

  /**
   * Compare person search result list
   */

  private BDMPersonSearchDetailsResult compareResults(
    final BDMPersonSearchDetailsResult refSearchResult,
    final BDMPersonSearchDetailsResult trackingNumberSearchResult) {

    final BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();
    if (refSearchResult.personSearchResult.dtlsList
      .size() == trackingNumberSearchResult.personSearchResult.dtlsList
        .size()) {

      if (trackingNumberSearchResult.personSearchResult.dtlsList
        .get(0).concernRoleID == refSearchResult.personSearchResult.dtlsList
          .get(0).concernRoleID) {
        bdmPersonSearchDetailsResult.assign(trackingNumberSearchResult);
      }
    }
    return bdmPersonSearchDetailsResult;
  }

  public void updateEvidenceCommentField(final long duplicateConcernRoleID,
    final String evidenceType) throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = duplicateConcernRoleID;

    final long participantDataCaseID = ParticipantDataCaseFactory
      .newInstance().getParticipantDataCase(concernRoleKey).caseID;

    // Get EvidenceID from the case for EvidenceType = Address
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = participantDataCaseID;

    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();
    final RelatedIDAndEvidenceTypeKeyList attestationRelatedId =
      bdmEvidenceUtil.getActiveEvidenceIDByEvidenceTypeAndCase(evidenceType,
        caseKey);

    for (final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey : attestationRelatedId.dtls) {
      final EvidenceServiceInterface evidenceServiceInterface =
        EvidenceGenericSLFactory
          .instance(relatedIDAndEvidenceTypeKey.relatedID);
      final EvidenceCaseKey evidenceCaseKey = new EvidenceCaseKey();
      evidenceCaseKey.caseIDKey = new CaseIDKey();
      evidenceCaseKey.caseIDKey.caseID = caseKey.caseID;
      evidenceCaseKey.evidenceKey.evidenceID =
        relatedIDAndEvidenceTypeKey.relatedID;
      evidenceCaseKey.evidenceKey.evType = evidenceType;

      // BUG 111066 - START
      final RecordIDAndType recordIDAndType = new RecordIDAndType();

      recordIDAndType.recordID = relatedIDAndEvidenceTypeKey.relatedID;
      recordIDAndType.recordType = MERGEENTITY.EVIDENCE;

      if (clientMergeObj.isMergedRecord(recordIDAndType).statusInd) {
        // BUG 111066 - END
        final DynamicEvidenceDataAttribute dynamicEvidenceDataAttribute =
          DynamicEvidenceDataAttributeFactory.newInstance();

        final ReadEvidenceDetails evdDtls =
          evidenceServiceInterface.readEvidence(evidenceCaseKey);
        final DynamicEvidenceDataAttributeDetails dedaDtls =
          evdDtls.dtls.getAttribute(BDMConstants.kBDMEvidenceComments);

        final DynamicEvidenceDataAttributeKey dynEDKey =
          new DynamicEvidenceDataAttributeKey();
        final DynamicEvidenceDataAttributeDtls dynEDDtls =
          new DynamicEvidenceDataAttributeDtls();
        dynEDDtls.attributeID = dedaDtls.getID();
        dynEDDtls.evidenceID = relatedIDAndEvidenceTypeKey.relatedID;
        dynEDDtls.name = BDMConstants.kBDMEvidenceComments;
        dynEDDtls.value =
          BDMConstants.kBDMAddEvidenceComments + dedaDtls.getValue();
        dynEDKey.attributeID = dedaDtls.getID();
        dynamicEvidenceDataAttribute.modify(dynEDKey, dynEDDtls);

      }
    }
  }

  /*
   * Customized OOTB method to fix bug 93659
   *
   */

  @Override
  public void completeMerge(final ConcernRoleDetailsForMerge details)
    throws AppException, InformationalException {

    final curam.core.sl.intf.ClientMerge clientMergeObj =
      curam.core.sl.fact.ClientMergeFactory.newInstance();
    clientMergeObj.completeMerge(details.dtls);
    final String[] evidenceList =
      BDMConstants.kBDMvidencesForMerge.split(CuramConst.gkComma);
    for (final String evidence : evidenceList) {
      updateEvidenceCommentField(details.dtls.originalConcernRoleID,
        evidence);
    }

  }

}
