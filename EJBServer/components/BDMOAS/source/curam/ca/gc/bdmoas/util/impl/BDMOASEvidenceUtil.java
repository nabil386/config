package curam.ca.gc.bdmoas.util.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.codetable.CASECATTYPECODE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESEARCHSTATUS;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.ConcernRoleIDStatusCodeKey;
import curam.core.fact.AddressFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.intf.Address;
import curam.core.intf.CaseHeader;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.intf.CaseParticipantRole;
import curam.core.sl.entity.struct.ParticipantRoleIDTypeCodesStatusKey;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDParticipantIDStatusCode;
import curam.core.sl.infrastructure.entity.struct.CorrectionSetIDAndStatusKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorIDRelatedIDAndEvidenceType;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorIDRelatedIDAndEvidenceTypeList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.SharedInstanceIDCaseIDStatusCode;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.sl.struct.CaseIDTypeAndStatusKey;
import curam.core.sl.struct.CaseParticipantRoleIDAndNameDtlsList;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseHeaderKeyList;
import curam.core.struct.ConcernRoleKey;
import curam.dynamicevidence.facade.fact.DynamicEvidenceMaintenanceFactory;
import curam.dynamicevidence.facade.intf.DynamicEvidenceMaintenance;
import curam.dynamicevidence.facade.struct.DynEvdModifyDetails;
import curam.dynamicevidence.facade.struct.DynamicEvidenceData;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.DateRange;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BDMOAS Utility Class for Evidence functionality.
 */
public class BDMOASEvidenceUtil {

  public static CaseHeaderKeyList
    readCaseIDsForActivePriAndMemRolesByParticipantRoleID(
      final Long participantRoleID)
      throws AppException, InformationalException {

    final CaseParticipantRole cprObj =
      CaseParticipantRoleFactory.newInstance();
    final ParticipantRoleIDTypeCodesStatusKey participantRoleIDTypeCodesStatusKey =
      new ParticipantRoleIDTypeCodesStatusKey();
    participantRoleIDTypeCodesStatusKey.participantRoleID = participantRoleID;
    participantRoleIDTypeCodesStatusKey.recordStatus = RECORDSTATUS.NORMAL;
    participantRoleIDTypeCodesStatusKey.typeCode1 =
      CASEPARTICIPANTROLETYPE.PRIMARY;
    participantRoleIDTypeCodesStatusKey.typeCode2 =
      CASEPARTICIPANTROLETYPE.MEMBER;
    return cprObj.searchCaseIDByPRIDTypeCodesAndStatus(
      participantRoleIDTypeCodesStatusKey);

  }

  /**
   * Read open application and integrated case's case header record by
   * concern role ID
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static List<CaseHeaderDtls>
    readOpenApplicationAndIntegratedCasesByConcernRoleID(
      final Long concernRoleID) throws AppException, InformationalException {

    final CaseHeaderKeyList allCaseList =
      readCaseIDsForActivePriAndMemRolesByParticipantRoleID(concernRoleID);

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final List<CaseHeaderDtls> applicationAndIntegratedCaseHeaderList =
      new ArrayList<CaseHeaderDtls>();

    for (final CaseHeaderKey caseHeaderKey : allCaseList.dtls) {
      final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);
      if (caseHeaderDtls.caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)
        && caseHeaderDtls.statusCode.equals(CASESTATUS.OPEN)) {
        applicationAndIntegratedCaseHeaderList.add(caseHeaderDtls);
      } else if (caseHeaderDtls.caseTypeCode
        .equals(CASETYPECODE.INTEGRATEDCASE)
        && caseHeaderDtls.statusCode.equals(CASESTATUS.OPEN)
        && caseHeaderDtls.integratedCaseType
          .equals(CASECATTYPECODE.OAS_OLD_AGE_SECURITY)) {
        applicationAndIntegratedCaseHeaderList.add(caseHeaderDtls);
      }
    }
    return applicationAndIntegratedCaseHeaderList;
  }

  /**
   * Get Date Attribute value as Date Object
   *
   * @param evidence
   * @return
   */
  public static Date getDateAttrValue(
    final DynamicEvidenceDataDetails evidence,
    final String dateAttributeName) {

    final String dateString =
      evidence.getAttribute(dateAttributeName).getValue();

    final Date date =
      dateString.isEmpty() ? Date.kZeroDate : Date.fromISO8601(dateString);

    return date;
  }

  /**
   * Checks if is date range overlap.
   *
   * @param firstRangeStartDate the first range start date
   * @param firstRangeEndDate the first range end date
   * @param secondRangeStartDate the second range start date
   * @param secondRangeEndDate the second range end date
   * @return the boolean
   */
  public static Boolean isDateRangeOverlap(final Date firstRangeStartDate,
    final Date firstRangeEndDate, final Date secondRangeStartDate,
    final Date secondRangeEndDate) {

    final DateRange firstDateRange =
      new DateRange(firstRangeStartDate, firstRangeEndDate);
    final DateRange secondDateRange =
      new DateRange(secondRangeStartDate, secondRangeEndDate);

    return firstDateRange.overlapsWith(secondDateRange);
  }

  /**
   * Read active evidence record data for correction set.
   *
   * @param correctionSetID the correction set ID
   * @return the dynamic evidence data details
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public static DynamicEvidenceDataDetails
    readActiveEvidenceRecordDataForCorrectionSetID(
      final String correctionSetID)
      throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EvidenceDescriptor edObj = EvidenceDescriptorFactory.newInstance();
    final CorrectionSetIDAndStatusKey correctionSetIDStatusKey =
      new CorrectionSetIDAndStatusKey();
    correctionSetIDStatusKey.correctionSetID = correctionSetID;
    correctionSetIDStatusKey.statusCode = EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    final EvidenceDescriptorIDRelatedIDAndEvidenceTypeList edList =
      edObj.searchByCorrectionSetIDAndStatus(correctionSetIDStatusKey);

    if (edList.dtls.isEmpty()) {
      return null;
    }

    final EvidenceDescriptorIDRelatedIDAndEvidenceType activeED =
      edList.dtls.get(0);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = activeED.relatedID;
    evidenceKey.evidenceType = activeED.evidenceType;

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerInterface.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    return evidenceData;
  }

  /**
   * Gets the residential address date range country code map by concern role
   * ID.
   *
   * @param concernRoleID the concern role ID
   * @return the residential address date range country code map by concern role
   * ID
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public static Map<DateRange, String>
    getResidentialAddressDateRangeCountryCodeMapByConcernRoleID(
      final long concernRoleID) throws AppException, InformationalException {

    final Map<DateRange, String> DateRangeCountryMap =
      new HashMap<DateRange, String>();

    // PDC case key
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final Long caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    // Search Active Residential Address records
    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
      new CaseIDParticipantIDStatusCode();
    paramCaseIDParticipantIDStatusCode.caseID = caseID;
    paramCaseIDParticipantIDStatusCode.participantID = concernRoleID;
    paramCaseIDParticipantIDStatusCode.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    final EvidenceDescriptorDtlsList allActiveEvdList = evidenceDescriptorObj
      .searchActiveByCaseIDParticipantID(paramCaseIDParticipantIDStatusCode);

    final List<EvidenceDescriptorDtls> activeAddressListForParticipant =
      allActiveEvdList.dtls.stream()
        .filter(ed -> ed.evidenceType.equals(CASEEVIDENCE.BDMADDRESS))
        .collect(Collectors.toList());

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    for (final EvidenceDescriptorDtls evidenceDescriptorDtls : activeAddressListForParticipant) {

      // Read address evidence data
      final EIEvidenceKey evidenceKey = new EIEvidenceKey();
      evidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
      evidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;
      final EIEvidenceReadDtls evidenceReadDtls =
        evidenceControllerInterface.readEvidence(evidenceKey);

      final DynamicEvidenceDataDetails evidenceData =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      final Boolean isResidentialAddress =
        evidenceData.getAttribute("addressType").getValue()
          .equals(CONCERNROLEADDRESSTYPE.PRIVATE);

      if (isResidentialAddress) {
        // Read residential address attributes

        final Date startDate = getDateAttrValue(evidenceData, "fromDate");
        final Date endDate = getDateAttrValue(evidenceData, "toDate");
        final DateRange dr = new DateRange(startDate, endDate);

        // Read Address Country
        final Address addressObj = AddressFactory.newInstance();
        final AddressKey addressKey = new AddressKey();
        addressKey.addressID =
          Long.valueOf(evidenceData.getAttribute("address").getValue());
        final AddressDtls addressDtls = addressObj.read(addressKey);

        DateRangeCountryMap.put(dr, addressDtls.countryCode);
      }
    }

    return DateRangeCountryMap;
  }

  /**
   * Gets the marital status date range marital status code map by concern role
   * ID.
   *
   * @param concernRoleID the concern role ID
   * @return the marital status date range marital status code map by concern
   * role ID
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public static Map<DateRange, String>
    getMaritalStatusDateRangeMaritalStatusCodeMapByConcernRoleID(
      final long concernRoleID) throws AppException, InformationalException {

    final Map<DateRange, String> DateRangeMaritalStatusCodeMap =
      new HashMap<DateRange, String>();

    // PDC case key
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final Long caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    // Search Active Marital Status records
    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
      new CaseIDParticipantIDStatusCode();
    paramCaseIDParticipantIDStatusCode.caseID = caseID;
    paramCaseIDParticipantIDStatusCode.participantID = concernRoleID;
    paramCaseIDParticipantIDStatusCode.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    final EvidenceDescriptorDtlsList allActiveEvdList = evidenceDescriptorObj
      .searchActiveByCaseIDParticipantID(paramCaseIDParticipantIDStatusCode);

    final List<EvidenceDescriptorDtls> activeMaritalStatusListForParticipant =
      allActiveEvdList.dtls.stream()
        .filter(ed -> ed.evidenceType.equals(CASEEVIDENCE.BDM_MARITAL_STATUS))
        .map(c -> {
          if (c.effectiveFrom == null)
            c.effectiveFrom = Date.kZeroDate;
          return c;
        }).sorted((a, b) -> a.effectiveFrom.compareTo(b.effectiveFrom))
        .collect(Collectors.toList());

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    DateRange previousDateRange = null;
    for (final EvidenceDescriptorDtls evidenceDescriptorDtls : activeMaritalStatusListForParticipant) {

      // Read marital status evidence data
      final EIEvidenceKey evidenceKey = new EIEvidenceKey();
      evidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
      evidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;
      final EIEvidenceReadDtls evidenceReadDtls =
        evidenceControllerInterface.readEvidence(evidenceKey);

      final DynamicEvidenceDataDetails evidenceData =
        (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

      final Date rangeEndDate = getDateAttrValue(evidenceData, "endDate");

      Date rangeStartDate = Date.kZeroDate;
      if (previousDateRange == null) {
        rangeStartDate = getDateAttrValue(evidenceData, "startDate");
      } else {
        // set the start date as effectiveFrom date
        rangeStartDate = evidenceDescriptorDtls.effectiveFrom;

        // Also, set the previous record end date to effectiveFrom - 1 day
        final String previousMaritalStatus =
          DateRangeMaritalStatusCodeMap.get(previousDateRange);
        DateRangeMaritalStatusCodeMap.remove(previousDateRange);
        final DateRange newPreviousDateRange = previousDateRange
          .newEndDate(evidenceDescriptorDtls.effectiveFrom.addDays(-1));
        DateRangeMaritalStatusCodeMap.put(newPreviousDateRange,
          previousMaritalStatus);
      }

      final DateRange dr = new DateRange(rangeStartDate, rangeEndDate);

      previousDateRange = dr;

      // Read Marital Status code
      final String maritalStatusCode =
        evidenceData.getAttribute("maritalStatus").getValue();

      DateRangeMaritalStatusCodeMap.put(dr, maritalStatusCode);

    }

    return DateRangeMaritalStatusCodeMap;
  }

  /**
   * Checks if is date lies between two dates inclusive.
   *
   * @param testDate the test date
   * @param startDate the start date
   * @param endDate the end date
   * @return the boolean
   */
  public static Boolean isDateLiesBetweenTwoDatesInclusive(
    final Date testDate, final Date startDate, Date endDate) {

    if (endDate.isZero()) {
      endDate = Date.fromISO8601("99991231");
    }

    return (testDate.equals(startDate) || testDate.after(startDate))
      && (testDate.equals(endDate) || testDate.before(endDate));
  }

  /**
   * Creates dynamic evidence on application case..
   *
   * @param participantRoleID
   * @param evidenceData
   * @param evidenceType
   * @param evidenceChangeReason
   * @param caseID
   * @return ReturnEvidenceDetails
   */
  public static ReturnEvidenceDetails createACDynamicEvidence(
    final long participantRoleID, final HashMap<String, String> evidenceData,
    final String evidenceType, final String evidenceChangeReason,
    final long caseID) throws AppException, InformationalException {

    ReturnEvidenceDetails returnEvidenceDetails = new ReturnEvidenceDetails();

    Long acCaseID = 0L;
    if (caseID != 0) {
      acCaseID = caseID;
    } else {
      // find active AC case
      final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
        new ConcernRoleIDStatusCodeKey();
      concernRoleIDStatusCodeKey.dtls.concernRoleID = participantRoleID;
      concernRoleIDStatusCodeKey.dtls.statusCode = CASESEARCHSTATUS.OPEN;
      final CaseHeaderDtlsList caseheaderDtlsList =
        curam.core.facade.fact.CaseHeaderFactory.newInstance()
          .searchByConcernRoleID(concernRoleIDStatusCodeKey);

      if (!caseheaderDtlsList.dtlsList.dtls.isEmpty()) {
        for (final CaseHeaderDtls dtl : caseheaderDtlsList.dtlsList.dtls) {
          if (dtl.caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)
            && dtl.statusCode.equals(CASESTATUS.OPEN)) {
            acCaseID = dtl.caseID;
            break;
          }
        }
      }
    }
    if (acCaseID != 0L) {
      final long caseParticipantRoleID = getCaseParticipantRoleIDByType(
        acCaseID, CASEPARTICIPANTROLETYPE.PRIMARY);

      if (evidenceType.equals(CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP)) {
        evidenceData.put("caseParticipantRoleID",
          String.valueOf(caseParticipantRoleID));
      } else {
        evidenceData.put("participant",
          String.valueOf(caseParticipantRoleID));
      }

      final DynamicEvidenceMaintenance dynObj =
        DynamicEvidenceMaintenanceFactory.newInstance();
      final DynamicEvidenceData dynamicEvidenceData =
        new DynamicEvidenceData();
      final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

      dynamicEvidenceData.data = BDMEvidenceUtil
        .setDynamicEvidenceDetailsByEvidenceType(evidenceData, evidenceType);
      dynamicEvidenceData.caseIDKey.caseID = acCaseID;
      dynamicEvidenceData.descriptor.evidenceType = evidenceType;
      dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
      dynamicEvidenceData.descriptor.changeReason = evidenceChangeReason;
      dynMod.effectiveDateUsed =
        BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

      // create the evidence
      returnEvidenceDetails =
        dynObj.createEvidence(dynamicEvidenceData, dynMod);

      updateChangeReason(evidenceType, evidenceChangeReason,
        returnEvidenceDetails);
    }
    return returnEvidenceDetails;
  }

  // OOTB code does not update the Change reason even if we pass the
  // change reason to OOTB Code.
  // Therefore this code is written to update the change reason.
  private static EvidenceDescriptorDtls updateChangeReason(
    final String evidenceType, final String evidenceChangeReason,
    final ReturnEvidenceDetails returnEvidenceDetails)
    throws AppException, InformationalException {

    final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndEvidenceTypeKey.relatedID =
      returnEvidenceDetails.evidenceKey.evidenceID;
    relatedIDAndEvidenceTypeKey.evidenceType = evidenceType;
    final curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor descriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      descriptorObj.readByRelatedIDAndType(relatedIDAndEvidenceTypeKey);
    final EvidenceDescriptorKey descriptorKey = new EvidenceDescriptorKey();
    descriptorKey.evidenceDescriptorID =
      evidenceDescriptorDtls.evidenceDescriptorID;
    evidenceDescriptorDtls.changeReason = evidenceChangeReason;
    descriptorObj.modify(descriptorKey, evidenceDescriptorDtls);
    return evidenceDescriptorDtls;
  }

  /**
   * Method to get the case participant role ID.
   *
   * @param caseID
   * @return caseParticipantRoleID
   */
  public static long getCaseParticipantRoleIDByType(final long caseID,
    final String typeCode) throws AppException, InformationalException {

    long caseParticipantRoleID = 0;
    final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
      new CaseIDTypeAndStatusKey();
    caseIDTypeAndStatusKey.key.caseID = caseID;
    caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
    caseIDTypeAndStatusKey.key.typeCode = typeCode; // CASEPARTICIPANTROLETYPE.PRIMARY;
    final CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleNameDetails =
      curam.core.sl.fact.CaseParticipantRoleFactory.newInstance()
        .listCaseParticipantRolesByTypeCaseIDAndStatus(
          caseIDTypeAndStatusKey);

    if (caseParticipantRoleNameDetails.dtls.dtls.size() > 0) {
      caseParticipantRoleID =
        caseParticipantRoleNameDetails.dtls.dtls.get(0).caseParticipantRoleID;
    }

    return caseParticipantRoleID;
  }

  public static EIEvidenceKey createEvidence(final long caseID,
    final long concernRoleID, final CASEEVIDENCEEntry evidenceType,
    final Map<String, String> attributes, final Date effectiveFrom)
    throws AppException, InformationalException {

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evidenceType.getCode(),
        Date.getCurrentDate());

    for (final String key : attributes.keySet()) {
      dynamicEvidenceDataDetails.getAttribute(key)
        .setValue(attributes.get(key));
    }

    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();
    eiEvidenceInsertDtls.descriptor.caseID = caseID;
    eiEvidenceInsertDtls.descriptor.evidenceType = evidenceType.getCode();
    eiEvidenceInsertDtls.descriptor.participantID = concernRoleID;
    eiEvidenceInsertDtls.descriptor.receivedDate = Date.getCurrentDate();
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.INITIAL;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    return evidenceController.insertEvidence(eiEvidenceInsertDtls);

  }

  /**
   * Gets the participant ID for evidence.
   *
   * @param evidenceKey the evidence key
   * @return the participant ID for evidence
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public static Long
    getParticipantIDForEvidence(final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();

    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);

    return descriptorDtls.participantID;
  }

  /**
   * Read active evidence descriptor list by case ID, participant ID and
   * evidence type.
   *
   * @param caseID the case ID
   * @param participantID the participant ID
   * @param evidenceType the evidence type
   * @return the list
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public static List<EvidenceDescriptorDtls>
    readActiveEvidenceDescriptorListByCaseIDParticipantIDEvidenceType(
      final long caseID, final long participantID, final String evidenceType)
      throws AppException, InformationalException {

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
      new CaseIDParticipantIDStatusCode();
    paramCaseIDParticipantIDStatusCode.caseID = caseID;
    paramCaseIDParticipantIDStatusCode.participantID = participantID;
    paramCaseIDParticipantIDStatusCode.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    final EvidenceDescriptorDtlsList allActiveEvdList = evidenceDescriptorObj
      .searchActiveByCaseIDParticipantID(paramCaseIDParticipantIDStatusCode);

    final List<EvidenceDescriptorDtls> evidenceDescriptorDtlsList =
      allActiveEvdList.dtls.stream()
        .filter(ed -> ed.evidenceType.equals(evidenceType))
        .collect(Collectors.toList());

    return evidenceDescriptorDtlsList;
  }

  // BEGIN 119238 DEV: Implement Conditional Verification RuleSet
  /**
   * Checks if legal status verification is required.
   *
   * @param evidenceDescriptorID the evidence descriptor ID
   * @return the boolean
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public static Boolean
    isLegalStatusVerificationRequired(final long evidenceDescriptorID)
      throws AppException, InformationalException {

    boolean isLegalStatusVerificationRequired = true;

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID = evidenceDescriptorID;

    // read target evidence descriptor
    final EvidenceDescriptorDtls targetEvidenceDescriptorDtls =
      evidenceDescriptorObj.read(evidenceDescriptorKey);

    final long sourceCaseID = targetEvidenceDescriptorDtls.sourceCaseID;

    if (targetEvidenceDescriptorDtls.sharedInstanceID != 0L
      && sourceCaseID != 0L) {

      // read source evidence descriptor
      final SharedInstanceIDCaseIDStatusCode sharedInstanceIDCaseIDStatusCode =
        new SharedInstanceIDCaseIDStatusCode();
      sharedInstanceIDCaseIDStatusCode.caseID = sourceCaseID;
      sharedInstanceIDCaseIDStatusCode.sharedInstanceID =
        targetEvidenceDescriptorDtls.sharedInstanceID;
      sharedInstanceIDCaseIDStatusCode.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;
      final EvidenceDescriptorDtlsList sourceEvidenceDescriptorList =
        evidenceDescriptorObj.searchBySharedInstanceIDCaseIDStatusCode(
          sharedInstanceIDCaseIDStatusCode);

      if (!sourceEvidenceDescriptorList.dtls.isEmpty()) {

        final EvidenceControllerInterface evidenceControllerInterface =
          (EvidenceControllerInterface) EvidenceControllerFactory
            .newInstance();
        final EvidenceDescriptorDtls sourceEvidenceDescriptor =
          sourceEvidenceDescriptorList.dtls.get(0);

        // read source evidence data
        final EIEvidenceKey sourceEvidenceKey = new EIEvidenceKey();
        sourceEvidenceKey.evidenceID = sourceEvidenceDescriptor.relatedID;
        sourceEvidenceKey.evidenceType =
          sourceEvidenceDescriptor.evidenceType;
        final EIEvidenceReadDtls sourceEvidenceReadDtls =
          evidenceControllerInterface.readEvidence(sourceEvidenceKey);

        final DynamicEvidenceDataDetails sourceEvidenceData =
          (DynamicEvidenceDataDetails) sourceEvidenceReadDtls.evidenceObject;

        final String sourceLegalStatus =
          sourceEvidenceData.getAttribute("legalStatus").getValue();
        final String sourceOtherLegalStatus = sourceEvidenceData
          .getAttribute("otherLegalStatusDetails").getValue();
        final Date sourceStartDate = BDMOASEvidenceUtil
          .getDateAttrValue(sourceEvidenceData, "startDate");
        final Date sourceEndDate =
          BDMOASEvidenceUtil.getDateAttrValue(sourceEvidenceData, "endDate");

        // read target evidence data
        final EIEvidenceKey targetEvidenceKey = new EIEvidenceKey();
        targetEvidenceKey.evidenceID = targetEvidenceDescriptorDtls.relatedID;
        targetEvidenceKey.evidenceType =
          targetEvidenceDescriptorDtls.evidenceType;
        final EIEvidenceReadDtls targetEvidenceReadDtls =
          evidenceControllerInterface.readEvidence(targetEvidenceKey);

        final DynamicEvidenceDataDetails targetEvidenceData =
          (DynamicEvidenceDataDetails) targetEvidenceReadDtls.evidenceObject;

        final String targetLegalStatus =
          targetEvidenceData.getAttribute("legalStatus").getValue();
        final String targetOtherLegalStatus = targetEvidenceData
          .getAttribute("otherLegalStatusDetails").getValue();
        final Date targetStartDate = BDMOASEvidenceUtil
          .getDateAttrValue(targetEvidenceData, "startDate");
        final Date targetEndDate =
          BDMOASEvidenceUtil.getDateAttrValue(targetEvidenceData, "endDate");

        // check for evidence data match
        if (sourceLegalStatus.equals(targetLegalStatus)
          && sourceOtherLegalStatus.equals(targetOtherLegalStatus)
          && sourceStartDate.equals(targetStartDate)
          && sourceEndDate.equals(targetEndDate)) {

          // verification not required if source and target data matches
          isLegalStatusVerificationRequired = false;
        }
      }

    }

    return isLegalStatusVerificationRequired;
  }
  // END 119238 DEV: Implement Conditional Verification RuleSet
}
