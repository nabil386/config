package curam.ca.gc.bdmoas.evidence.sl.impl;

import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEVALUE;
import curam.ca.gc.bdmoas.codetable.impl.BDMOASOVERRIDEEVDTASKTYPEEntry;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASEligibilityEntitlementOverrideConstants;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASLivingApartforReasonsBeyondControlConstants;
import curam.ca.gc.bdmoas.evidence.constants.impl.OASSponsorshipConstants;
import curam.ca.gc.bdmoas.util.impl.BDMOASEvidenceUtil;
import curam.ca.gc.bdmoas.util.impl.BDMOASTaskUtil;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.core.fact.AddressFactory;
import curam.core.intf.Address;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDParticipantIDStatusCode;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.DateRange;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Class BDMOASEligibilityEntitlementOverrideTaskHelper.
 */
public class BDMOASEligibilityEntitlementOverrideTaskHelper {

  private static final String INCARCERATION_START_DATE_ATTR = "startDate";

  private static final String INCARCERATION_END_DATE_ATTR = "endDate";

  private static final String BIRTH_AND_DEATH_DOB_ATTR = "dateOfBirth";

  /**
   * Creates the override evidence task post insert.
   *
   * @param caseKey the case key
   * @param evidenceKey the evidence key
   * @return the map
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public Map<BDMOASOVERRIDEEVDTASKTYPEEntry, List<Long>>
    createOverrideEvidenceTaskPostInsert(final CaseKey caseKey,
      final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    final Map<BDMOASOVERRIDEEVDTASKTYPEEntry, List<Long>> overrideTaskTypeCaseListMap =
      new HashMap<BDMOASOVERRIDEEVDTASKTYPEEntry, List<Long>>();

    if (evidenceKey.evidenceType.equals(CASEEVIDENCE.BDMINCARCERATION)) {
      overrideTaskTypeCaseListMap.put(
        BDMOASOVERRIDEEVDTASKTYPEEntry.INCARCERATION,
        getOverrideEvidenceTaskCaseListForIncarceration(evidenceKey));
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL)) {
      overrideTaskTypeCaseListMap.put(
        BDMOASOVERRIDEEVDTASKTYPEEntry.LIVING_APART,
        getOverrideEvidenceTaskCaseListForLivingApart(evidenceKey));
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.OAS_SPONSORSHIP_AGREEMENT)) {
      overrideTaskTypeCaseListMap.put(
        BDMOASOVERRIDEEVDTASKTYPEEntry.SPONSORSHIP_AGREEMENT,
        getOverrideEvidenceTaskCaseListForSponsorshipAgreement(evidenceKey));
    } else if (evidenceKey.evidenceType.equals(CASEEVIDENCE.BDMADDRESS)) {
      overrideTaskTypeCaseListMap.put(BDMOASOVERRIDEEVDTASKTYPEEntry.COUNTRY,
        getOverrideEvidenceTaskCaseListForResidentialAddressChange(
          evidenceKey));
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.BDM_MARITAL_STATUS)) {
      overrideTaskTypeCaseListMap.put(
        BDMOASOVERRIDEEVDTASKTYPEEntry.MARITAL_STATUS,
        getOverrideEvidenceTaskCaseListForMaritalStatusChange(evidenceKey));
    }

    for (final BDMOASOVERRIDEEVDTASKTYPEEntry taskType : overrideTaskTypeCaseListMap
      .keySet()) {
      for (final Long caseID : overrideTaskTypeCaseListMap.get(taskType)) {

        BDMOASTaskUtil.createOverrideEvidenceTask(caseID, taskType);
      }
    }

    return overrideTaskTypeCaseListMap;
  }

  /**
   * Creates the override evidence task post modify.
   *
   * @param caseKey the case key
   * @param evidenceKey the evidence key
   * @return the map
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public Map<BDMOASOVERRIDEEVDTASKTYPEEntry, List<Long>>
    createOverrideEvidenceTaskPostModify(final CaseKey caseKey,
      final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    final Map<BDMOASOVERRIDEEVDTASKTYPEEntry, List<Long>> overrideTaskTypeCaseListMap =
      new HashMap<BDMOASOVERRIDEEVDTASKTYPEEntry, List<Long>>();

    if (evidenceKey.evidenceType.equals(CASEEVIDENCE.BDMINCARCERATION)) {
      overrideTaskTypeCaseListMap.put(
        BDMOASOVERRIDEEVDTASKTYPEEntry.INCARCERATION,
        getOverrideEvidenceTaskCaseListForIncarceration(evidenceKey));
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL)) {
      overrideTaskTypeCaseListMap.put(
        BDMOASOVERRIDEEVDTASKTYPEEntry.LIVING_APART,
        getOverrideEvidenceTaskCaseListForLivingApart(evidenceKey));
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.OAS_SPONSORSHIP_AGREEMENT)) {
      overrideTaskTypeCaseListMap.put(
        BDMOASOVERRIDEEVDTASKTYPEEntry.SPONSORSHIP_AGREEMENT,
        getOverrideEvidenceTaskCaseListForSponsorshipAgreement(evidenceKey));
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.OAS_ELG_ENTITLEMNT_OVERRIDE)) {
      overrideTaskTypeCaseListMap.put(BDMOASOVERRIDEEVDTASKTYPEEntry.FORTIETH,
        getOverrideEvidenceTaskCaseListForFortiethOverride(evidenceKey));
      overrideTaskTypeCaseListMap.put(
        BDMOASOVERRIDEEVDTASKTYPEEntry.DEFERRAL_MONTHS,
        getOverrideEvidenceTaskCaseListForDeferralMonthsOverride(
          evidenceKey));
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS)) {
      overrideTaskTypeCaseListMap.put(BDMOASOVERRIDEEVDTASKTYPEEntry.AGE,
        getOverrideEvidenceTaskCaseListForDOBChange(evidenceKey));
    } else if (evidenceKey.evidenceType.equals(CASEEVIDENCE.BDMADDRESS)) {
      overrideTaskTypeCaseListMap.put(BDMOASOVERRIDEEVDTASKTYPEEntry.COUNTRY,
        getOverrideEvidenceTaskCaseListForResidentialAddressChange(
          evidenceKey));
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.BDM_MARITAL_STATUS)) {
      overrideTaskTypeCaseListMap.put(
        BDMOASOVERRIDEEVDTASKTYPEEntry.MARITAL_STATUS,
        getOverrideEvidenceTaskCaseListForMaritalStatusChange(evidenceKey));
    }

    for (final BDMOASOVERRIDEEVDTASKTYPEEntry taskType : overrideTaskTypeCaseListMap
      .keySet()) {
      for (final Long caseID : overrideTaskTypeCaseListMap.get(taskType)) {

        BDMOASTaskUtil.createOverrideEvidenceTask(caseID, taskType);
      }
    }

    return overrideTaskTypeCaseListMap;
  }

  /**
   * Gets the override evidence task case list for incarceration.
   *
   * @param evidenceKey the evidence key
   * @return the override evidence task case list for incarceration
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private List<Long> getOverrideEvidenceTaskCaseListForIncarceration(
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final List<Long> caseIDList = new ArrayList<Long>();

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerInterface.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails incEvidenceData =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final Date incarcerationStartDate = BDMOASEvidenceUtil
      .getDateAttrValue(incEvidenceData, INCARCERATION_START_DATE_ATTR);
    final Date incarcerationEndDate = BDMOASEvidenceUtil
      .getDateAttrValue(incEvidenceData, INCARCERATION_END_DATE_ATTR);

    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);

    final Long participantID = descriptorDtls.participantID;

    // Read open application and integrated cases for the participant.
    final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(participantID);

    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderList) {
      final Long caseID = caseHeaderDtls.caseID;

      // Check if task already exists
      final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
      final Long existingTaskID =
        taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(caseID,
          BDMOASOVERRIDEEVDTASKTYPEEntry.INCARCERATION);

      if (existingTaskID != 0l) {
        continue;
      }

      // Read Eligibility Entitlement Override records for the concern role in
      // open application and integrated cases.
      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
        new CaseIDParticipantIDStatusCode();
      paramCaseIDParticipantIDStatusCode.caseID = caseID;
      paramCaseIDParticipantIDStatusCode.participantID = participantID;
      paramCaseIDParticipantIDStatusCode.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;
      final EvidenceDescriptorDtlsList allActiveEvdList =
        evidenceDescriptorObj.searchActiveByCaseIDParticipantID(
          paramCaseIDParticipantIDStatusCode);

      final List<EvidenceDescriptorDtls> overrideEvdEDList =
        allActiveEvdList.dtls.stream()
          .filter(ed -> ed.evidenceType
            .equals(CASEEVIDENCE.OAS_ELG_ENTITLEMNT_OVERRIDE))
          .collect(Collectors.toList());

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : overrideEvdEDList) {

        // Read override evidence data
        final EIEvidenceKey overrideEvidenceKey = new EIEvidenceKey();
        overrideEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
        overrideEvidenceKey.evidenceType =
          evidenceDescriptorDtls.evidenceType;
        final EIEvidenceReadDtls overrideEvidenceReadDtls =
          evidenceControllerInterface.readEvidence(overrideEvidenceKey);

        final DynamicEvidenceDataDetails overrideEvidenceData =
          (DynamicEvidenceDataDetails) overrideEvidenceReadDtls.evidenceObject;

        final Date overrideStartDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.START_DATE);
        final Date overrideEndDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.END_DATE);
        final Boolean isEntitlementAmountOverride = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
          .getValue().equals(BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);

        // create Override Evidence Task
        if (isEntitlementAmountOverride
          && BDMOASEvidenceUtil.isDateRangeOverlap(incarcerationStartDate,
            incarcerationEndDate, overrideStartDate, overrideEndDate)) {

          caseIDList.add(caseID);
        }

      }
    }

    return caseIDList;
  }

  /**
   * Gets the override evidence task case list for living apart.
   *
   * @param evidenceKey the evidence key
   * @return the override evidence task case list for living apart
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private List<Long> getOverrideEvidenceTaskCaseListForLivingApart(
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final List<Long> caseIDList = new ArrayList<Long>();

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerInterface.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails LivingApartEvidenceData =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final Date livingApartStartDate =
      BDMOASEvidenceUtil.getDateAttrValue(LivingApartEvidenceData,
        BDMOASLivingApartforReasonsBeyondControlConstants.START_DATE);
    final Date livingApartEndDate =
      BDMOASEvidenceUtil.getDateAttrValue(LivingApartEvidenceData,
        BDMOASLivingApartforReasonsBeyondControlConstants.END_DATE);

    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);

    final Long participantID = descriptorDtls.participantID;

    // Read open application and integrated cases for the participant.
    final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(participantID);

    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderList) {
      final Long caseID = caseHeaderDtls.caseID;

      // Check if task already exists
      final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
      final Long existingTaskID =
        taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(caseID,
          BDMOASOVERRIDEEVDTASKTYPEEntry.LIVING_APART);

      if (existingTaskID != 0l) {
        continue;
      }

      // Read Eligibility Entitlement Override records for the concern role in
      // open application and integrated cases.
      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
        new CaseIDParticipantIDStatusCode();
      paramCaseIDParticipantIDStatusCode.caseID = caseID;
      paramCaseIDParticipantIDStatusCode.participantID = participantID;
      paramCaseIDParticipantIDStatusCode.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;
      final EvidenceDescriptorDtlsList allActiveEvdList =
        evidenceDescriptorObj.searchActiveByCaseIDParticipantID(
          paramCaseIDParticipantIDStatusCode);

      final List<EvidenceDescriptorDtls> overrideEvdEDList =
        allActiveEvdList.dtls.stream()
          .filter(ed -> ed.evidenceType
            .equals(CASEEVIDENCE.OAS_ELG_ENTITLEMNT_OVERRIDE))
          .collect(Collectors.toList());

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : overrideEvdEDList) {

        // Read override evidence data
        final EIEvidenceKey overrideEvidenceKey = new EIEvidenceKey();
        overrideEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
        overrideEvidenceKey.evidenceType =
          evidenceDescriptorDtls.evidenceType;
        final EIEvidenceReadDtls overrideEvidenceReadDtls =
          evidenceControllerInterface.readEvidence(overrideEvidenceKey);

        final DynamicEvidenceDataDetails overrideEvidenceData =
          (DynamicEvidenceDataDetails) overrideEvidenceReadDtls.evidenceObject;

        final Date overrideStartDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.START_DATE);
        final Date overrideEndDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.END_DATE);
        final Boolean isEntitlementAmountOverride = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
          .getValue().equals(BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);
        final String benefitType = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE)
          .getValue();
        final Boolean isOverrideForGisAlwBenefits = benefitType
          .equals(BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT)
          || benefitType.equals(BDMOASOVERRIDEBENEFITTYPE.ALLOWANCE);

        // create Override Evidence Task
        if (isEntitlementAmountOverride && isOverrideForGisAlwBenefits
          && BDMOASEvidenceUtil.isDateRangeOverlap(livingApartStartDate,
            livingApartEndDate, overrideStartDate, overrideEndDate)) {

          caseIDList.add(caseID);
        }

      }
    }

    return caseIDList;
  }

  /**
   * Gets the override evidence task case list for sponsorship agreement.
   *
   * @param evidenceKey the evidence key
   * @return the override evidence task case list for sponsorship agreement
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private List<Long> getOverrideEvidenceTaskCaseListForSponsorshipAgreement(
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final List<Long> caseIDList = new ArrayList<Long>();

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerInterface.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails sponsorshipEvidenceData =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final Date sponsorshipStartDate = BDMOASEvidenceUtil.getDateAttrValue(
      sponsorshipEvidenceData, OASSponsorshipConstants.START_DATE);
    final Date sponsorshipEndDate = BDMOASEvidenceUtil.getDateAttrValue(
      sponsorshipEvidenceData, OASSponsorshipConstants.END_DATE);

    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);

    final Long participantID = descriptorDtls.participantID;

    // Read open application and integrated cases for the participant.
    final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(participantID);

    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderList) {
      final Long caseID = caseHeaderDtls.caseID;

      // Check if task already exists
      final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
      final Long existingTaskID =
        taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(caseID,
          BDMOASOVERRIDEEVDTASKTYPEEntry.SPONSORSHIP_AGREEMENT);

      if (existingTaskID != 0l) {
        continue;
      }

      // Read Eligibility Entitlement Override records for the concern role in
      // open application and integrated cases.
      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
        new CaseIDParticipantIDStatusCode();
      paramCaseIDParticipantIDStatusCode.caseID = caseID;
      paramCaseIDParticipantIDStatusCode.participantID = participantID;
      paramCaseIDParticipantIDStatusCode.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;
      final EvidenceDescriptorDtlsList allActiveEvdList =
        evidenceDescriptorObj.searchActiveByCaseIDParticipantID(
          paramCaseIDParticipantIDStatusCode);

      final List<EvidenceDescriptorDtls> overrideEvdEDList =
        allActiveEvdList.dtls.stream()
          .filter(ed -> ed.evidenceType
            .equals(CASEEVIDENCE.OAS_ELG_ENTITLEMNT_OVERRIDE))
          .collect(Collectors.toList());

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : overrideEvdEDList) {

        // Read override evidence data
        final EIEvidenceKey overrideEvidenceKey = new EIEvidenceKey();
        overrideEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
        overrideEvidenceKey.evidenceType =
          evidenceDescriptorDtls.evidenceType;
        final EIEvidenceReadDtls overrideEvidenceReadDtls =
          evidenceControllerInterface.readEvidence(overrideEvidenceKey);

        final DynamicEvidenceDataDetails overrideEvidenceData =
          (DynamicEvidenceDataDetails) overrideEvidenceReadDtls.evidenceObject;

        final Date overrideStartDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.START_DATE);
        final Date overrideEndDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.END_DATE);
        final Boolean isEntitlementAmountOverride = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
          .getValue().equals(BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);
        final String benefitType = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE)
          .getValue();
        final Boolean isOverrideForGisAlwAlwsBenefits = benefitType
          .equals(BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT)
          || benefitType.equals(BDMOASOVERRIDEBENEFITTYPE.ALLOWANCE)
          || benefitType
            .equals(BDMOASOVERRIDEBENEFITTYPE.ALLOWANCE_FOR_THE_SURVIVOR);

        // create Override Evidence Task
        if (isEntitlementAmountOverride && isOverrideForGisAlwAlwsBenefits
          && BDMOASEvidenceUtil.isDateRangeOverlap(sponsorshipStartDate,
            sponsorshipEndDate, overrideStartDate, overrideEndDate)) {

          caseIDList.add(caseID);
        }

      }
    }

    return caseIDList;
  }

  /**
   * Gets the override evidence task case list for fortieth override.
   *
   * @param evidenceKey the evidence key
   * @return the override evidence task case list for fortieth override
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private List<Long> getOverrideEvidenceTaskCaseListForFortiethOverride(
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final List<Long> caseIDList = new ArrayList<Long>();

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerInterface.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final Boolean isFortiethsOverride = evidenceData
      .getAttribute(
        BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
      .getValue().equals(BDMOASOVERRIDEVALUE.FORTIETHS);

    if (!isFortiethsOverride) {
      return caseIDList;
    }

    // get previous deferral months data of the same correctionSetID
    final DynamicEvidenceDataDetails activatedRecord =
      BDMOASEvidenceUtil.readActiveEvidenceRecordDataForCorrectionSetID(
        evidenceReadDtls.descriptor.correctionSetID);

    if (activatedRecord == null) {
      return caseIDList;
    }

    final String modifiedRecordFortieths = evidenceData
      .getAttribute(
        BDMOASEligibilityEntitlementOverrideConstants.NUMBER_AMOUNT)
      .getValue();

    final Boolean isActivatedRecordForiethsOverride = activatedRecord
      .getAttribute(
        BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
      .getValue().equals(BDMOASOVERRIDEVALUE.FORTIETHS);

    if (!isActivatedRecordForiethsOverride) {
      return caseIDList;
    }

    final String previousRecordFortieths = activatedRecord
      .getAttribute(
        BDMOASEligibilityEntitlementOverrideConstants.NUMBER_AMOUNT)
      .getValue();

    if (previousRecordFortieths.equals(modifiedRecordFortieths)) {
      return caseIDList;
    }

    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);

    final Long participantID = descriptorDtls.participantID;

    // Read open application and integrated cases for the participant.
    final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(participantID);

    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderList) {
      final Long caseID = caseHeaderDtls.caseID;

      // Check if task already exists
      final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
      final Long existingTaskID =
        taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(caseID,
          BDMOASOVERRIDEEVDTASKTYPEEntry.FORTIETH);

      if (existingTaskID != 0l) {
        continue;
      }

      // Read Eligibility Entitlement Override records for the concern role in
      // open application and integrated cases.
      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
        new CaseIDParticipantIDStatusCode();
      paramCaseIDParticipantIDStatusCode.caseID = caseID;
      paramCaseIDParticipantIDStatusCode.participantID = participantID;
      paramCaseIDParticipantIDStatusCode.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;
      final EvidenceDescriptorDtlsList allActiveEvdList =
        evidenceDescriptorObj.searchActiveByCaseIDParticipantID(
          paramCaseIDParticipantIDStatusCode);

      final List<EvidenceDescriptorDtls> overrideEvdEDList =
        allActiveEvdList.dtls.stream()
          .filter(ed -> ed.evidenceType
            .equals(CASEEVIDENCE.OAS_ELG_ENTITLEMNT_OVERRIDE))
          .collect(Collectors.toList());

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : overrideEvdEDList) {

        // Read override evidence data
        final EIEvidenceKey overrideEvidenceKey = new EIEvidenceKey();
        overrideEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
        overrideEvidenceKey.evidenceType =
          evidenceDescriptorDtls.evidenceType;
        final EIEvidenceReadDtls overrideEvidenceReadDtls =
          evidenceControllerInterface.readEvidence(overrideEvidenceKey);

        final DynamicEvidenceDataDetails overrideEvidenceData =
          (DynamicEvidenceDataDetails) overrideEvidenceReadDtls.evidenceObject;

        final Boolean isEntitlementAmountOverride = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
          .getValue().equals(BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);

        final Boolean isOverrideForGISBenefit = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE)
          .getValue()
          .equals(BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

        // create Override Evidence Task
        if (isEntitlementAmountOverride && isOverrideForGISBenefit) {

          caseIDList.add(caseID);
        }

      }
    }

    return caseIDList;
  }

  /**
   * Gets the override evidence task case list for deferral months override.
   *
   * @param evidenceKey the evidence key
   * @return the override evidence task case list for deferral months override
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private List<Long> getOverrideEvidenceTaskCaseListForDeferralMonthsOverride(
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final List<Long> caseIDList = new ArrayList<Long>();

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerInterface.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final Boolean isDeferralMonthsOverride = evidenceData
      .getAttribute(
        BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
      .getValue().equals(BDMOASOVERRIDEVALUE.DEFERRAL_MONTHS);

    if (!isDeferralMonthsOverride) {
      return caseIDList;
    }

    // get previous deferral months data of the same correctionSetID
    final DynamicEvidenceDataDetails activatedRecord =
      BDMOASEvidenceUtil.readActiveEvidenceRecordDataForCorrectionSetID(
        evidenceReadDtls.descriptor.correctionSetID);

    if (activatedRecord == null) {
      return caseIDList;
    }

    final String modifiedRecordDeferralMonths = evidenceData
      .getAttribute(
        BDMOASEligibilityEntitlementOverrideConstants.NUMBER_AMOUNT)
      .getValue();

    final Boolean isPreviousRecordDeferralMonthsOverride = activatedRecord
      .getAttribute(
        BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
      .getValue().equals(BDMOASOVERRIDEVALUE.DEFERRAL_MONTHS);

    if (!isPreviousRecordDeferralMonthsOverride) {
      return caseIDList;
    }

    final String previousRecordDeferralMonths = activatedRecord
      .getAttribute(
        BDMOASEligibilityEntitlementOverrideConstants.NUMBER_AMOUNT)
      .getValue();

    if (previousRecordDeferralMonths.equals(modifiedRecordDeferralMonths)) {
      return caseIDList;
    }

    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);

    final Long participantID = descriptorDtls.participantID;

    // Read open application and integrated cases for the participant.
    final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(participantID);

    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderList) {
      final Long caseID = caseHeaderDtls.caseID;

      // Check if task already exists
      final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
      final Long existingTaskID =
        taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(caseID,
          BDMOASOVERRIDEEVDTASKTYPEEntry.DEFERRAL_MONTHS);

      if (existingTaskID != 0l) {
        continue;
      }

      // Read Eligibility Entitlement Override records for the concern role in
      // open application and integrated cases.
      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
        new CaseIDParticipantIDStatusCode();
      paramCaseIDParticipantIDStatusCode.caseID = caseID;
      paramCaseIDParticipantIDStatusCode.participantID = participantID;
      paramCaseIDParticipantIDStatusCode.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;
      final EvidenceDescriptorDtlsList allActiveEvdList =
        evidenceDescriptorObj.searchActiveByCaseIDParticipantID(
          paramCaseIDParticipantIDStatusCode);

      final List<EvidenceDescriptorDtls> overrideEvdEDList =
        allActiveEvdList.dtls.stream()
          .filter(ed -> ed.evidenceType
            .equals(CASEEVIDENCE.OAS_ELG_ENTITLEMNT_OVERRIDE))
          .collect(Collectors.toList());

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : overrideEvdEDList) {

        // Read override evidence data
        final EIEvidenceKey overrideEvidenceKey = new EIEvidenceKey();
        overrideEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
        overrideEvidenceKey.evidenceType =
          evidenceDescriptorDtls.evidenceType;
        final EIEvidenceReadDtls overrideEvidenceReadDtls =
          evidenceControllerInterface.readEvidence(overrideEvidenceKey);

        final DynamicEvidenceDataDetails overrideEvidenceData =
          (DynamicEvidenceDataDetails) overrideEvidenceReadDtls.evidenceObject;

        final Boolean isEntitlementAmountOverride = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
          .getValue().equals(BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);

        final Boolean isOverrideForGISBenefit = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE)
          .getValue()
          .equals(BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

        // create Override Evidence Task
        if (isEntitlementAmountOverride && isOverrideForGISBenefit) {
          caseIDList.add(caseID);
        }

      }
    }

    return caseIDList;
  }

  /**
   * Gets the override evidence task case list for DOB change.
   *
   * @param evidenceKey the evidence key
   * @return the override evidence task case list for DOB change
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private List<Long> getOverrideEvidenceTaskCaseListForDOBChange(
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final List<Long> caseIDList = new ArrayList<Long>();

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerInterface.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    // get previous Date of Birth data of the same correctionSetID
    final DynamicEvidenceDataDetails activatedRecord =
      BDMOASEvidenceUtil.readActiveEvidenceRecordDataForCorrectionSetID(
        evidenceReadDtls.descriptor.correctionSetID);

    if (activatedRecord == null) {
      return caseIDList;
    }

    final String evidenceRecordDOB =
      evidenceData.getAttribute(BIRTH_AND_DEATH_DOB_ATTR).getValue();

    final String activatedRecordDOB =
      activatedRecord.getAttribute(BIRTH_AND_DEATH_DOB_ATTR).getValue();

    if (activatedRecordDOB.equals(evidenceRecordDOB)) {
      return caseIDList;
    }

    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);

    final Long participantID = descriptorDtls.participantID;

    // Read open application and integrated cases for the participant.
    final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(participantID);

    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderList) {
      final Long caseID = caseHeaderDtls.caseID;

      // Check if task already exists
      final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
      final Long existingTaskID =
        taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(caseID,
          BDMOASOVERRIDEEVDTASKTYPEEntry.AGE);

      if (existingTaskID != 0l) {
        continue;
      }

      // Read Eligibility Entitlement Override records for the concern role in
      // open application and integrated cases.
      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
        new CaseIDParticipantIDStatusCode();
      paramCaseIDParticipantIDStatusCode.caseID = caseID;
      paramCaseIDParticipantIDStatusCode.participantID = participantID;
      paramCaseIDParticipantIDStatusCode.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;
      final EvidenceDescriptorDtlsList allActiveEvdList =
        evidenceDescriptorObj.searchActiveByCaseIDParticipantID(
          paramCaseIDParticipantIDStatusCode);

      final List<EvidenceDescriptorDtls> overrideEvdEDList =
        allActiveEvdList.dtls.stream()
          .filter(ed -> ed.evidenceType
            .equals(CASEEVIDENCE.OAS_ELG_ENTITLEMNT_OVERRIDE))
          .collect(Collectors.toList());

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : overrideEvdEDList) {

        // Read override evidence data
        final EIEvidenceKey overrideEvidenceKey = new EIEvidenceKey();
        overrideEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
        overrideEvidenceKey.evidenceType =
          evidenceDescriptorDtls.evidenceType;
        final EIEvidenceReadDtls overrideEvidenceReadDtls =
          evidenceControllerInterface.readEvidence(overrideEvidenceKey);

        final DynamicEvidenceDataDetails overrideEvidenceData =
          (DynamicEvidenceDataDetails) overrideEvidenceReadDtls.evidenceObject;

        final Boolean isAgeOverride = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
          .getValue().equals(BDMOASOVERRIDEVALUE.SIXTY);
        final Boolean isEntitlementAmountOverride = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
          .getValue().equals(BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);
        final Boolean isOverrideForOASPensionBenefit = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE)
          .getValue()
          .equals(BDMOASOVERRIDEBENEFITTYPE.OLD_AGE_SECURITY_PENSION);

        final Date overrideStartDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.START_DATE);
        final Date overrideEndDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.END_DATE);

        // create Override Evidence Task
        if (isAgeOverride) {
          caseIDList.add(caseID);
        } else if (isEntitlementAmountOverride
          && isOverrideForOASPensionBenefit) {
          final Date newDOB = Date.fromISO8601(evidenceRecordDOB);

          final Calendar seventyFifthBirthdayCal = newDOB.getCalendar();
          seventyFifthBirthdayCal.add(Calendar.YEAR, 75);

          final Date seventyFifthBirthday = new Date(seventyFifthBirthdayCal);

          final Boolean isSeventyFifthBirthdayBetweenOverrideStartEnd =
            BDMOASEvidenceUtil.isDateLiesBetweenTwoDatesInclusive(
              seventyFifthBirthday, overrideStartDate, overrideEndDate);

          if (isSeventyFifthBirthdayBetweenOverrideStartEnd) {
            caseIDList.add(caseID);
          }
        }

      }
    }

    return caseIDList;
  }

  /**
   * Gets the override evidence task case list for residential address change.
   *
   * @param evidenceKey the evidence key
   * @return the override evidence task case list for residential address change
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private List<Long>
    getOverrideEvidenceTaskCaseListForResidentialAddressChange(
      final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    final List<Long> caseIDList = new ArrayList<Long>();

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerInterface.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final Boolean isResidentialAddress =
      evidenceData.getAttribute("addressType").getValue()
        .equals(CONCERNROLEADDRESSTYPE.PRIVATE);

    if (!isResidentialAddress) {
      return caseIDList;
    }

    final Date currentEvdStartDate =
      BDMOASEvidenceUtil.getDateAttrValue(evidenceData, "fromDate");
    final Date currentEvdEndDate =
      BDMOASEvidenceUtil.getDateAttrValue(evidenceData, "toDate");
    final DateRange currentDateRange =
      new DateRange(currentEvdStartDate, currentEvdEndDate);

    // Read this record address country
    final Address addressObj = AddressFactory.newInstance();
    final AddressKey addressKey = new AddressKey();
    addressKey.addressID =
      Long.valueOf(evidenceData.getAttribute("address").getValue());
    final AddressDtls addressDtls = addressObj.read(addressKey);
    final String currentCountryCode = addressDtls.countryCode;

    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);

    final Long participantID = descriptorDtls.participantID;

    // Read active residential address records for this participant as a
    // daterange country map.
    final Map<DateRange, String> dateRangeCountryMap = BDMOASEvidenceUtil
      .getResidentialAddressDateRangeCountryCodeMapByConcernRoleID(
        participantID);

    // Read open application and integrated cases for the participant.
    final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(participantID);

    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderList) {
      final Long caseID = caseHeaderDtls.caseID;

      // Check if task already exists
      final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
      final Long existingTaskID =
        taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(caseID,
          BDMOASOVERRIDEEVDTASKTYPEEntry.COUNTRY);

      if (existingTaskID != 0l) {
        continue;
      }

      // Read Eligibility Entitlement Override records for the concern role in
      // open application and integrated cases.
      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
        new CaseIDParticipantIDStatusCode();
      paramCaseIDParticipantIDStatusCode.caseID = caseID;
      paramCaseIDParticipantIDStatusCode.participantID = participantID;
      paramCaseIDParticipantIDStatusCode.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;
      final EvidenceDescriptorDtlsList allActiveEvdList =
        evidenceDescriptorObj.searchActiveByCaseIDParticipantID(
          paramCaseIDParticipantIDStatusCode);

      final List<EvidenceDescriptorDtls> overrideEvdEDList =
        allActiveEvdList.dtls.stream()
          .filter(ed -> ed.evidenceType
            .equals(CASEEVIDENCE.OAS_ELG_ENTITLEMNT_OVERRIDE))
          .collect(Collectors.toList());

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : overrideEvdEDList) {

        // Read override evidence data
        final EIEvidenceKey overrideEvidenceKey = new EIEvidenceKey();
        overrideEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
        overrideEvidenceKey.evidenceType =
          evidenceDescriptorDtls.evidenceType;
        final EIEvidenceReadDtls overrideEvidenceReadDtls =
          evidenceControllerInterface.readEvidence(overrideEvidenceKey);

        final DynamicEvidenceDataDetails overrideEvidenceData =
          (DynamicEvidenceDataDetails) overrideEvidenceReadDtls.evidenceObject;

        final Date overrideStartDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.START_DATE);
        final Date overrideEndDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.END_DATE);
        final DateRange overrideDateRange =
          new DateRange(overrideStartDate, overrideEndDate);

        final Boolean isEntitlementAmountOverride = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
          .getValue().equals(BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);

        final String benefitType = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE)
          .getValue();
        final Boolean isOverrideForGisAlwAlwsBenefits = benefitType
          .equals(BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT)
          || benefitType.equals(BDMOASOVERRIDEBENEFITTYPE.ALLOWANCE)
          || benefitType
            .equals(BDMOASOVERRIDEBENEFITTYPE.ALLOWANCE_FOR_THE_SURVIVOR);

        Boolean isResidentialAddressCntryChange = false;

        // Check for address country change and daterange overlap between
        // overrideEvidence, address evidence record and active addresses.
        for (final DateRange dr : dateRangeCountryMap.keySet()) {
          // Use tempDr to adjust the end date of the record which would have
          // been in effect prior to the insert of the current record.
          DateRange tempDr = new DateRange(dr.start(), dr.end());
          if (tempDr.end().equals(currentEvdStartDate.addDays(-1))) {
            tempDr = tempDr.newEndDate(Date.kZeroDate);
          }
          if (tempDr.overlapsWith(currentDateRange)
            && tempDr.overlapsWith(overrideDateRange)
            && !currentCountryCode.equals(dateRangeCountryMap.get(dr))) {
            isResidentialAddressCntryChange = true;
          }
        }

        // create Override Evidence Task
        if (isEntitlementAmountOverride && isOverrideForGisAlwAlwsBenefits
          && isResidentialAddressCntryChange) {

          caseIDList.add(caseID);
        }

      }
    }

    return caseIDList;
  }

  /**
   * Gets the override evidence task case list for marital status change.
   *
   * @param evidenceKey the evidence key
   * @return the override evidence task case list for marital status change
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private List<Long> getOverrideEvidenceTaskCaseListForMaritalStatusChange(
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final List<Long> caseIDList = new ArrayList<Long>();

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);

    final Long participantID = descriptorDtls.participantID;

    // Read active marital status records for this participant as a
    // daterange marital staus code map.
    final Map<DateRange, String> dateRangeMaritalStatusCodeMap =
      BDMOASEvidenceUtil
        .getMaritalStatusDateRangeMaritalStatusCodeMapByConcernRoleID(
          participantID);

    // If no current marital status, no need to process
    if (dateRangeMaritalStatusCodeMap.isEmpty()) {
      return caseIDList;
    }

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceControllerInterface.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    Date currentEvdStartDate =
      BDMOASEvidenceUtil.getDateAttrValue(evidenceData, "startDate");
    if (descriptorDtls.effectiveFrom != null
      && !descriptorDtls.effectiveFrom.isZero()) {
      currentEvdStartDate = descriptorDtls.effectiveFrom;
    }

    final Date currentEvdEndDate =
      BDMOASEvidenceUtil.getDateAttrValue(evidenceData, "endDate");
    final DateRange currentDateRange =
      new DateRange(currentEvdStartDate, currentEvdEndDate);

    // Read this record marital status code
    final String currentMaritalStatusCode =
      evidenceData.getAttribute("maritalStatus").getValue();

    // Read open application and integrated cases for the participant.
    final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(participantID);

    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderList) {
      final Long caseID = caseHeaderDtls.caseID;

      // Check if task already exists
      final BDMOASTaskUtil taskUtil = new BDMOASTaskUtil();
      final Long existingTaskID =
        taskUtil.getOverrideEvidenceTaskIDByCaseIDTaskType(caseID,
          BDMOASOVERRIDEEVDTASKTYPEEntry.MARITAL_STATUS);

      if (existingTaskID != 0l) {
        continue;
      }

      // Read Eligibility Entitlement Override records for the concern role in
      // open application and integrated cases.
      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final CaseIDParticipantIDStatusCode paramCaseIDParticipantIDStatusCode =
        new CaseIDParticipantIDStatusCode();
      paramCaseIDParticipantIDStatusCode.caseID = caseID;
      paramCaseIDParticipantIDStatusCode.participantID = participantID;
      paramCaseIDParticipantIDStatusCode.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;
      final EvidenceDescriptorDtlsList allActiveEvdList =
        evidenceDescriptorObj.searchActiveByCaseIDParticipantID(
          paramCaseIDParticipantIDStatusCode);

      final List<EvidenceDescriptorDtls> overrideEvdEDList =
        allActiveEvdList.dtls.stream()
          .filter(ed -> ed.evidenceType
            .equals(CASEEVIDENCE.OAS_ELG_ENTITLEMNT_OVERRIDE))
          .collect(Collectors.toList());

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : overrideEvdEDList) {

        // Read override evidence data
        final EIEvidenceKey overrideEvidenceKey = new EIEvidenceKey();
        overrideEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
        overrideEvidenceKey.evidenceType =
          evidenceDescriptorDtls.evidenceType;
        final EIEvidenceReadDtls overrideEvidenceReadDtls =
          evidenceControllerInterface.readEvidence(overrideEvidenceKey);

        final DynamicEvidenceDataDetails overrideEvidenceData =
          (DynamicEvidenceDataDetails) overrideEvidenceReadDtls.evidenceObject;

        final Date overrideStartDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.START_DATE);
        final Date overrideEndDate =
          BDMOASEvidenceUtil.getDateAttrValue(overrideEvidenceData,
            BDMOASEligibilityEntitlementOverrideConstants.END_DATE);
        final DateRange overrideDateRange =
          new DateRange(overrideStartDate, overrideEndDate);

        final Boolean isEntitlementAmountOverride = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.OVERRIDE_VALUE)
          .getValue().equals(BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);
        final Boolean isOverrideForGISBenefit = overrideEvidenceData
          .getAttribute(
            BDMOASEligibilityEntitlementOverrideConstants.BENEFIT_TYPE)
          .getValue()
          .equals(BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

        Boolean isMaritalStatusCodeChange = false;

        // Check for address country change and daterange overlap between
        // overrideEvidence, address evidence record and active addresses.
        for (final DateRange dr : dateRangeMaritalStatusCodeMap.keySet()) {
          // Use tempDr to adjust the end date of the record which would have
          // been in effect prior to the insert of the current record.
          DateRange tempDr = new DateRange(dr.start(), dr.end());
          if (tempDr.end().equals(currentEvdStartDate.addDays(-1))) {
            tempDr = tempDr.newEndDate(Date.kZeroDate);
          }
          if (tempDr.overlapsWith(currentDateRange)
            && tempDr.overlapsWith(overrideDateRange)
            && currentMaritalStatusCode.equals(BDMMARITALSTATUS.COMMONLAW)
            && dateRangeMaritalStatusCodeMap.get(dr)
              .equals(BDMMARITALSTATUS.MARRIED)) {
            isMaritalStatusCodeChange = true;
            break;
          }
        }

        // create Override Evidence Task
        if (isEntitlementAmountOverride && isOverrideForGISBenefit
          && isMaritalStatusCodeChange) {

          caseIDList.add(caseID);
        }

      }
    }
    return caseIDList;
  }

}
