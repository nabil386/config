package curam.ca.gc.bdmoas.evidencemapping.util.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.codetable.BDMSIGNATURETYPE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.codetable.impl.BDMMARITALSTATUSEntry;
import curam.ca.gc.bdm.codetable.impl.BDMRECEIVEDFROMEntry;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMAddressEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMLifeEventUtil;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONFORM;
import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASIMMIGRATIONDOC;
import curam.ca.gc.bdmoas.codetable.BDMOASLEGALSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASLIVINGAPARTREASON;
import curam.ca.gc.bdmoas.codetable.BDMOASMARITALCHANGETYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASMARITALRELATION;
import curam.ca.gc.bdmoas.codetable.BDMOASMETHODOFAPPLICATION;
import curam.ca.gc.bdmoas.codetable.BDMOASOPTIONABLEEVENT;
import curam.ca.gc.bdmoas.codetable.OASRESIDENCETYPE;
import curam.ca.gc.bdmoas.codetable.impl.BDMOASLIVINGAPARTREASONEntry;
import curam.ca.gc.bdmoas.codetable.impl.BDMOASTAXTHRESHOLDEntry;
import curam.ca.gc.bdmoas.codetable.impl.OASRESIDENCETYPEEntry;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASApplicationDetailsEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASConsentDeclarationEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASFirstEntryIntoCanadaDetailsEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASForeignIncomeEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASLegalStatusEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASManuallyEnteredIncomeEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASMaritalRelationshipEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASNatureOfAbsencesEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASResidencePeriodEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASRetirementPensionReductionEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASSponsorshipEvidenceVO;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASWorldIncomeEvidenceVO;
import curam.ca.gc.bdmoas.util.impl.BDMOASEvidenceUtil;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESEARCHSTATUS;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.COUNTRY;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.IEGYESNO;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.ALTERNATENAMETYPEEntry;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.codetable.impl.CASEPARTICIPANTROLETYPEEntry;
import curam.codetable.impl.CONCERNROLEALTERNATEIDEntry;
import curam.codetable.impl.COUNTRYEntry;
import curam.core.facade.fact.CaseHeaderFactory;
import curam.core.facade.struct.ConcernRoleIDStatusCodeKey;
import curam.core.fact.AddressDataFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.AddressData;
import curam.core.sl.entity.struct.CaseParticipantRoleNameDetails;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.sl.struct.CaseIDTypeAndStatusKey;
import curam.core.sl.struct.CaseParticipantRoleIDAndNameDtlsList;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.LayoutKey;
import curam.core.struct.LocaleStruct;
import curam.core.struct.OtherAddressData;
import curam.creole.value.CodeTableItem;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.datastore.impl.EntityType;
import curam.dynamicevidence.facade.fact.DynamicEvidenceMaintenanceFactory;
import curam.dynamicevidence.facade.intf.DynamicEvidenceMaintenance;
import curam.dynamicevidence.facade.struct.DynEvdModifyDetails;
import curam.dynamicevidence.facade.struct.DynamicEvidenceData;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.StringHelper;
import curam.workspaceservices.applicationprocessing.impl.AddressMappingStrategy;
import curam.workspaceservices.applicationprocessing.impl.DataStoreCEFMappings;
import curam.workspaceservices.util.impl.DatastoreHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

/**
 * BDMOAS FEATURE 92921: Class Added.
 * Utility class to provide functionality related to mapping evidences.
 *
 * @author SMisal
 *
 */
public class BDMOASEvidenceMappingUtil {

  private static final String ATTESTEE = "attestee";

  private static final String CASE_PARTICIPANT_ROLE_ID =
    "caseParticipantRoleID";

  private static final String CASE_PARTICIPANT = "caseParticipant";

  private static final String CASE_PARTICIPANT_ROLE = "caseParticipantRole";

  private static final String PARTICIPANT = "participant";

  private static final String LOG_FORMATTER =
    "{}{} | concernRoleID: {} | evidence mapping failed: {}";

  private static final String PRIMARY_PARTICIPANT_BOOLEAN_EXP =
    "isPrimaryParticipant==true";

  private static final String NON_PRIMARY_PARTICIPANT_BOOLEAN_EXP =
    "isPrimaryParticipant==false";

  private static final String START_DATE = "startDate";

  private static final String MARITAL_STATUS = "maritalStatus";

  public String intakeScriptID = "";

  @Inject
  private AddressMappingStrategy addressMappingStrategy;

  public BDMOASEvidenceMappingUtil() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Map and create application details evidence
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapApplicationDetailsEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createApplicationDetailsEvidence(concernRoleID, caseID,
        applicationEntity, schemaName, primaryParticipantInd);
    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS);

    }

  }

  /**
   * Utility method to create Application Details Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createApplicationDetailsEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);

    final Entity personEntityPrimary = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    long casePtcptRoleID = 0;

    // 1: Create application details evidence for OAS
    if (primaryParticipantInd && intakeScriptID.equals(
      BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)) {
      casePtcptRoleID = BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(
        caseID, CASEPARTICIPANTROLETYPE.PRIMARY);

      final BDMOASApplicationDetailsEvidenceVO applicationDetailsVO =
        this.populateAppDetailsEvdDataForOAS(dataStore, applicationEntity,
          personEntityPrimary);

      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(applicationDetailsVO);

      evidenceData.put(BDMOASEvidenceMappingConstants.kCaseParticipantRole,
        String.valueOf(casePtcptRoleID));

      BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID, evidenceData,
        CASEEVIDENCE.OAS_APPLICATION_DETAILS, EVIDENCECHANGEREASON.INITIAL,
        caseID);
    }

    // 2: Create application details evidence for GIS
    boolean createEvdForGISInd = false;

    if (intakeScriptID.equals(
      BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)) {
      final Entity[] gisEntityList = personEntityPrimary.getChildEntities(
        dataStore.getEntityType(BDMOASDatastoreConstants.GIS));

      if (gisEntityList != null && gisEntityList.length > 0) {
        final Entity gisEntity = gisEntityList[0];
        if (gisEntity
          .getAttribute(BDMOASDatastoreConstants.GIS_APPLYGISIND_ATTR)
          .equals(IEGYESNO.YES)
          || gisEntity
            .getAttribute(BDMOASDatastoreConstants.GIS_GISSECCOMPLETED_ATTR)
            .equals(IEGYESNO.YES)) {
          createEvdForGISInd = true;
        }
      }
    } else if (intakeScriptID
      .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_AP_SCRIPT)
      || intakeScriptID
        .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_CP_SCRIPT)) {
      createEvdForGISInd = true;
    }

    if (createEvdForGISInd) {
      final BDMOASApplicationDetailsEvidenceVO applicationDetailsGISVO =
        this.populateAppDetailsEvdDataForGIS(applicationEntity);

      final HashMap<String, String> evidenceDataGIS =
        new BDMLifeEventUtil().getEvidenceData(applicationDetailsGISVO);

      if (primaryParticipantInd) {
        evidenceDataGIS.put(
          BDMOASEvidenceMappingConstants.kCaseParticipantRole,
          String.valueOf(casePtcptRoleID));

        createACOrICDynamicEvidence(concernRoleID, evidenceDataGIS,
          CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS,
          EVIDENCECHANGEREASON.INITIAL, caseID,
          CASEPARTICIPANTROLETYPEEntry.PRIMARY);
      } else {
        casePtcptRoleID = BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(
          caseID, CASEPARTICIPANTROLETYPE.MEMBER);

        evidenceDataGIS.put(
          BDMOASEvidenceMappingConstants.kCaseParticipantRole,
          String.valueOf(casePtcptRoleID));

        createACOrICDynamicEvidence(concernRoleID, evidenceDataGIS,
          CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS,
          EVIDENCECHANGEREASON.INITIAL, caseID,
          CASEPARTICIPANTROLETYPEEntry.MEMBER);
      }
    }
  }

  /**
   * Method to populate Application Details Evidence Data for OAS
   *
   * @param dataStore
   * @param applicationEntity
   * @param personEntityPrimary
   */
  private BDMOASApplicationDetailsEvidenceVO populateAppDetailsEvdDataForOAS(
    final Datastore dataStore, final Entity applicationEntity,
    final Entity personEntityPrimary)
    throws AppException, InformationalException {

    final BDMOASApplicationDetailsEvidenceVO applicationDetailsVO =
      new BDMOASApplicationDetailsEvidenceVO();

    // Populate application details evidence for OAS
    final Entity oasEntity = personEntityPrimary.getChildEntities(
      dataStore.getEntityType(BDMOASDatastoreConstants.OASPENSION_ENTITY))[0];

    final String oasPensionStartDate = oasEntity
      .getAttribute(BDMOASDatastoreConstants.OASPENSION_STARTDATE_ATTR);

    if (!StringUtil.isNullOrEmpty(oasPensionStartDate)) {
      applicationDetailsVO
        .setRequestedPensionStartDate(Date.getDate(oasPensionStartDate));
    }

    applicationDetailsVO.setReceiptDate(Date.getDate(applicationEntity
      .getAttribute(BDMOASDatastoreConstants.APPLICATION_RECEIPTDATE_ATTR)));

    applicationDetailsVO.setBenefitType(BDMOASBENEFITTYPE.OAS_PENSION);
    applicationDetailsVO.setApplicationStatus(BDMOASAPPLICATIONSTATUS.ACTIVE);
    applicationDetailsVO
      .setMethodOfApplication(BDMOASMETHODOFAPPLICATION.PAPER);

    applicationDetailsVO.setComments(
      BDMOASEvidenceMappingConstants.kApplicationDetailsEvidenceComments);

    return applicationDetailsVO;
  }

  /**
   * Method to populate Application Details Evidence Data for GIS.
   *
   * @param Entity applicationEntity
   */
  private BDMOASApplicationDetailsEvidenceVO
    populateAppDetailsEvdDataForGIS(final Entity applicationEntity)
      throws AppException, InformationalException {

    final BDMOASApplicationDetailsEvidenceVO applicationDetailsGISVO =
      new BDMOASApplicationDetailsEvidenceVO();

    applicationDetailsGISVO.setRequestedPensionStartDate(Date.kZeroDate);

    applicationDetailsGISVO
      .setBenefitType(BDMOASBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);
    applicationDetailsGISVO
      .setApplicationStatus(BDMOASAPPLICATIONSTATUS.ACTIVE);

    if (intakeScriptID
      .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_CP_SCRIPT)) {
      applicationDetailsGISVO
        .setMethodOfApplication(BDMOASMETHODOFAPPLICATION.ONLINE);

      applicationDetailsGISVO.setReceiptDate(Date.getCurrentDate());
    } else {
      applicationDetailsGISVO
        .setMethodOfApplication(BDMOASMETHODOFAPPLICATION.PAPER);

      applicationDetailsGISVO
        .setReceiptDate(Date.getDate(applicationEntity.getAttribute(
          BDMOASDatastoreConstants.APPLICATION_RECEIPTDATE_ATTR)));
    }

    applicationDetailsGISVO.setComments(
      BDMOASEvidenceMappingConstants.kApplicationDetailsEvidenceCommentsGIS);

    return applicationDetailsGISVO;
  }

  /**
   * Creates dynamic evidence on AC or IC
   *
   * @param participantRoleID
   *
   * @param evidenceData
   */
  public static void createACOrICDynamicEvidence(final long participantRoleID,
    final HashMap<String, String> evidenceData,
    final CASEEVIDENCEEntry evidenceType, final String evidenceChangeReason,
    final long caseID, final CASEPARTICIPANTROLETYPEEntry participantType)
    throws AppException, InformationalException {

    boolean activateEvidence = false;
    Long acOrICcaseID = 0L;
    if (caseID != 0) {
      acOrICcaseID = caseID;
    } else {
      // find active AC case, or IC case
      final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
        new ConcernRoleIDStatusCodeKey();
      concernRoleIDStatusCodeKey.dtls.concernRoleID = participantRoleID;
      concernRoleIDStatusCodeKey.dtls.statusCode = CASESEARCHSTATUS.OPEN;
      final CaseHeaderDtlsList caseheaderDtlsList = CaseHeaderFactory
        .newInstance().searchByConcernRoleID(concernRoleIDStatusCodeKey);

      if (!caseheaderDtlsList.dtlsList.dtls.isEmpty()) {
        for (final CaseHeaderDtls dtl : caseheaderDtlsList.dtlsList.dtls) {
          if (dtl.caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)
            && dtl.statusCode.equals(CASESTATUS.OPEN)) {
            acOrICcaseID = dtl.caseID;
            break;
          } else if (dtl.caseTypeCode.equals(CASETYPECODE.INTEGRATEDCASE)) {
            acOrICcaseID = dtl.caseID;
            activateEvidence = true;
          }
        }
      }
    }
    if (acOrICcaseID != 0L) {

      final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
        new CaseIDTypeAndStatusKey();
      caseIDTypeAndStatusKey.key.caseID = acOrICcaseID;
      caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
      caseIDTypeAndStatusKey.key.typeCode = participantType.getCode();
      final CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleNameDetails =
        CaseParticipantRoleFactory.newInstance()
          .listCaseParticipantRolesByTypeCaseIDAndStatus(
            caseIDTypeAndStatusKey);

      if (evidenceType.getCode().equals(CASEEVIDENCE.BDMATTS)) {
        evidenceData.put(ATTESTEE,
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));
      } else if (evidenceType.getCode().equals(CASEEVIDENCE.BDMINCOME)
        || evidenceType.getCode().equals(CASEEVIDENCE.BDMINCARCERATION)) {
        evidenceData.put(CASE_PARTICIPANT_ROLE_ID,
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));
      } else if (evidenceType.getCode().equals(CASEEVIDENCE.BDMVTW)) {
        evidenceData.put(CASE_PARTICIPANT,
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));
      } else if (evidenceType.getCode()
        .equals(CASEEVIDENCE.OAS_SPONSORSHIP)) {
        evidenceData.put(CASE_PARTICIPANT_ROLE,
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));

      } else {
        evidenceData.put(PARTICIPANT,
          String.valueOf(caseParticipantRoleNameDetails.dtls.dtls
            .get(0).caseParticipantRoleID));
      }

      final DynamicEvidenceMaintenance dynObj =
        DynamicEvidenceMaintenanceFactory.newInstance();
      final DynamicEvidenceData dynamicEvidenceData =
        new DynamicEvidenceData();
      final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

      dynamicEvidenceData.data =
        BDMEvidenceUtil.setDynamicEvidenceDetailsByEvidenceType(evidenceData,
          evidenceType.getCode());
      dynamicEvidenceData.caseIDKey.caseID = acOrICcaseID;
      dynamicEvidenceData.descriptor.evidenceType = evidenceType.getCode();
      dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
      dynamicEvidenceData.descriptor.participantID =
        caseParticipantRoleNameDetails.dtls.dtls.get(0).participantRoleID;
      dynamicEvidenceData.descriptor.changeReason = evidenceChangeReason;
      dynMod.effectiveDateUsed =
        BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

      // create the evidence
      final ReturnEvidenceDetails returnEvidenceDetails =
        dynObj.createEvidence(dynamicEvidenceData, dynMod);

      final EvidenceDescriptorDtls evidenceDescriptorDtls =
        updateChangeReasonAndParticipant(evidenceType.getCode(),
          evidenceChangeReason, returnEvidenceDetails,
          caseParticipantRoleNameDetails.dtls.dtls.get(0).participantRoleID);

      // apply activation code to the newly created IC dynamic Evidence
      if (activateEvidence) {
        BDMEvidenceUtil
          .applyInEditEvidenceToICDynamicEvidence(evidenceDescriptorDtls);
      }

    }

  }

  // OOTB code does not update the Change reason even if we pass the
  // change reason to OOTB Code.
  // Therefore this code is written to update the change reason.
  private static EvidenceDescriptorDtls updateChangeReasonAndParticipant(
    final String evidenceType, final String evidenceChangeReason,
    final ReturnEvidenceDetails returnEvidenceDetails,
    final long participantID) throws AppException, InformationalException {

    final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndEvidenceTypeKey.relatedID =
      returnEvidenceDetails.evidenceKey.evidenceID;
    relatedIDAndEvidenceTypeKey.evidenceType = evidenceType;
    final curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor descriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      descriptorObj.readByRelatedIDAndType(relatedIDAndEvidenceTypeKey);

    evidenceDescriptorDtls.participantID = participantID;
    final EvidenceDescriptorKey descriptorKey = new EvidenceDescriptorKey();
    descriptorKey.evidenceDescriptorID =
      evidenceDescriptorDtls.evidenceDescriptorID;
    evidenceDescriptorDtls.changeReason = evidenceChangeReason;
    descriptorObj.modify(descriptorKey, evidenceDescriptorDtls);
    return evidenceDescriptorDtls;
  }

  /**
   * Map and create Legal Status evidence
   *
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapLegalStatusEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createLegalStatusEvidence(concernRoleID, caseID, applicationEntity,
        schemaName, primaryParticipantInd);
    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.OAS_LEGAL_STATUS);

    }

  }

  /**
   * Utility method to create Legal Status Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createLegalStatusEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    final BDMOASLegalStatusEvidenceVO legalStatusVO =
      new BDMOASLegalStatusEvidenceVO();

    final Entity legalStatusEntity = personEntity.getChildEntities(dataStore
      .getEntityType(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY))[0];

    final String inCanadaLegalStatus = legalStatusEntity
      .getAttribute(BDMOASDatastoreConstants.LEGAL_STATUS_INCALEGSTATUS_ATTR);

    final String outOfCALegalStatus = legalStatusEntity
      .getAttribute(BDMOASDatastoreConstants.OUT_OF_CANADA_LEGAL_STATUS_ATTR);

    if (!StringUtil.isNullOrEmpty(inCanadaLegalStatus)) {

      legalStatusVO.setLegalStatus(inCanadaLegalStatus);

    }

    if (!StringUtil.isNullOrEmpty(outOfCALegalStatus)) {

      legalStatusVO.setLegalStatus(outOfCALegalStatus);

    }

    if (!StringUtil.isNullOrEmpty(legalStatusVO.getLegalStatus())) {

      final String countryOfBirth =
        personEntity.getAttribute(BDMOASDatastoreConstants.COUNTRY_OF_BIRTH);

      final String legalStatus_Other = legalStatusEntity.getAttribute(
        BDMOASDatastoreConstants.LEGAL_STATUS_INCALEGSTATUSOTHR_ATTR);

      if (!StringUtil.isNullOrEmpty(legalStatus_Other)) {
        legalStatusVO.setOtherLegalStatusDetails(legalStatus_Other);
      } else {
        legalStatusVO.setOtherLegalStatusDetails(BDMConstants.EMPTY_STRING);
      }

      if (inCanadaLegalStatus.equals(BDMOASLEGALSTATUS.CANADIAN_CITIZEN)
        && countryOfBirth.equals(COUNTRY.CA)) {
        legalStatusVO.setStartDate(Date.getDate(
          personEntity.getAttribute(BDMDatastoreConstants.DATE_OF_BIRTH)));
      } else {
        legalStatusVO.setStartDate(new Date());
      }

      legalStatusVO.setComments(
        BDMOASEvidenceMappingConstants.kLegalStatusEvidenceComments);

      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(legalStatusVO);

      long casePtcptRoleID = 0;
      if (primaryParticipantInd) {
        casePtcptRoleID = BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(
          caseID, CASEPARTICIPANTROLETYPE.PRIMARY);
      }

      evidenceData.put(BDMOASEvidenceMappingConstants.kCaseParticipantRoleID,
        String.valueOf(casePtcptRoleID));

      BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID, evidenceData,
        CASEEVIDENCE.OAS_LEGAL_STATUS, EVIDENCECHANGEREASON.INITIAL, caseID);

    }

  }

  /**
   * Map and create First Entry Into Canada evidence
   *
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapFirstEntryIntoCanadaEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createFirstEntryIntoCanadaEvidence(concernRoleID, caseID,
        applicationEntity, schemaName, primaryParticipantInd);
    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.OAS_FIRST_ENTRY_INTO_CANADA);
    }

  }

  /**
   * Utility method to create First Entry Into Canada Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createFirstEntryIntoCanadaEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    final Entity legalStatusEntity = personEntity.getChildEntities(dataStore
      .getEntityType(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY))[0];

    final String isBornOutsideOfCanada = legalStatusEntity
      .getAttribute(BDMOASDatastoreConstants.OUT_OF_CA_BORNED_ATTRIBUTE);

    if (!StringUtil.isNullOrEmpty(isBornOutsideOfCanada)
      && isBornOutsideOfCanada.equals(IEGYESNO.YES)) {
      final BDMOASFirstEntryIntoCanadaDetailsEvidenceVO firstEntryIntoCanadaVO =
        new BDMOASFirstEntryIntoCanadaDetailsEvidenceVO();

      final String canadaFirstEntryDateStr = legalStatusEntity.getAttribute(
        BDMOASDatastoreConstants.LEGAL_STATUS_CA_FIRSTENTRYDATE_ATTR);

      final Date firstEntryDate =
        StringUtil.isNullOrEmpty(canadaFirstEntryDateStr) ? Date.kZeroDate
          : Date.getDate(canadaFirstEntryDateStr);

      final String immigrationDocType = legalStatusEntity.getAttribute(
        BDMOASDatastoreConstants.LEGAL_STATUS_IMMIGRNDOCTYPE_ATTR);

      boolean firstEntryDateInd = false;

      if (!firstEntryDate.isZero()) {
        firstEntryDateInd = true;
      }

      firstEntryIntoCanadaVO.setArrivalDate(firstEntryDate);

      final String placeOfEntryInCanada =
        legalStatusEntity.getAttribute("placeOfEntryInCanada");
      firstEntryIntoCanadaVO.setArrivalCity(placeOfEntryInCanada);

      boolean immigrnDocTypeInd = false;
      if (!StringUtil.isNullOrEmpty(immigrationDocType)) {
        firstEntryIntoCanadaVO.setImmigrationDoc(immigrationDocType);
        immigrnDocTypeInd = true;

        if (immigrationDocType.equals(BDMOASIMMIGRATIONDOC.OTHER)) {
          final String immigrationDocType_Other =
            legalStatusEntity.getAttribute(
              BDMOASDatastoreConstants.LEGAL_STATUS_IMMIGRNDOCTYPEOTHR_ATTR);

          if (!StringUtil.isNullOrEmpty(immigrationDocType_Other)) {
            firstEntryIntoCanadaVO
              .setOtherImmigrationDocDetails(immigrationDocType_Other);
          }
        }
      }

      if (firstEntryDateInd || immigrnDocTypeInd) {
        firstEntryIntoCanadaVO.setComments(
          BDMOASEvidenceMappingConstants.kFirstEntryEvidenceComments);

        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(firstEntryIntoCanadaVO);

        long casePtcptRoleID = 0;
        if (primaryParticipantInd) {
          casePtcptRoleID = BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(
            caseID, CASEPARTICIPANTROLETYPE.PRIMARY);
        }

        evidenceData.put(BDMOASEvidenceMappingConstants.kCaseParticipantRole,
          String.valueOf(casePtcptRoleID));

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA,
          EVIDENCECHANGEREASON.INITIAL, caseID);
      }
    }
  }

  /**
   * Map and create Sponsorship evidence
   *
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapSponsorshipEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createSponsorshipEvidence(concernRoleID, caseID, applicationEntity,
        schemaName, primaryParticipantInd);
    } catch (final Exception e) {

      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.OAS_SPONSORSHIP);
    }

  }

  /**
   * Utility method to create Sponsorship Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createSponsorshipEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);

    final Entity personEntityPrimary = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    BDMOASSponsorshipEvidenceVO sponshorshipVO = null;

    if (intakeScriptID.equals(
      BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)) {
      final Entity legalStatusEntity =
        personEntityPrimary.getChildEntities(dataStore
          .getEntityType(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY))[0];

      final String isSponseredInCA = legalStatusEntity
        .getAttribute(BDMOASDatastoreConstants.LEGAL_STATUS_CASPONSORED_ATTR);

      if (!StringUtil.isNullOrEmpty(isSponseredInCA)) {
        sponshorshipVO = new BDMOASSponsorshipEvidenceVO();
        if (isSponseredInCA.equals(IEGYESNO.YES)) {
          sponshorshipVO.setIsSponsored(BDMYESNO.YES);
        } else if (isSponseredInCA.equals(IEGYESNO.NO)) {
          sponshorshipVO.setIsSponsored(BDMYESNO.NO);
        }
        sponshorshipVO.setComments(
          BDMOASEvidenceMappingConstants.kSponsorshipEvidenceComments);

        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(sponshorshipVO);

        if (primaryParticipantInd) {
          createACOrICDynamicEvidence(concernRoleID, evidenceData,
            CASEEVIDENCEEntry.OAS_SPONSORSHIP, EVIDENCECHANGEREASON.INITIAL,
            caseID, CASEPARTICIPANTROLETYPEEntry.PRIMARY);
        } else {
          createACOrICDynamicEvidence(concernRoleID, evidenceData,
            CASEEVIDENCEEntry.OAS_SPONSORSHIP, EVIDENCECHANGEREASON.INITIAL,
            caseID, CASEPARTICIPANTROLETYPEEntry.MEMBER);
        }

      }
    } else if (intakeScriptID
      .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_AP_SCRIPT)) {
      if (primaryParticipantInd) {
        final Entity[] sponsorshipInfoEntities =
          personEntityPrimary.getChildEntities(dataStore
            .getEntityType(BDMOASDatastoreConstants.SPONSORSHIPINFO_ENTITY));

        if (sponsorshipInfoEntities.length > 0) {
          final Entity sponsorshipInfoEntity = sponsorshipInfoEntities[0];
          final String isSponseredInCA = sponsorshipInfoEntity
            .getAttribute(BDMOASDatastoreConstants.IS_APLCNT_SPONSORED_ATTR);
          if (!StringUtil.isNullOrEmpty(isSponseredInCA)) {
            sponshorshipVO = new BDMOASSponsorshipEvidenceVO();
            sponshorshipVO.setIsSponsored(isSponseredInCA);

            sponshorshipVO.setComments(
              BDMOASEvidenceMappingConstants.kSponsorshipEvidenceComments);

            final HashMap<String, String> evidenceData =
              new BDMLifeEventUtil().getEvidenceData(sponshorshipVO);

            createACOrICDynamicEvidence(concernRoleID, evidenceData,
              CASEEVIDENCEEntry.OAS_SPONSORSHIP, EVIDENCECHANGEREASON.INITIAL,
              caseID, CASEPARTICIPANTROLETYPEEntry.PRIMARY);
          }
        }
      } else {
        final Entity personEntity = applicationEntity.getChildEntities(
          dataStore.getEntityType(BDMDatastoreConstants.PERSON),
          NON_PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

        final Entity[] sponsorshipInfoEntities =
          personEntity.getChildEntities(dataStore
            .getEntityType(BDMOASDatastoreConstants.SPONSORSHIPINFO_ENTITY));

        if (sponsorshipInfoEntities.length > 0) {
          final Entity sponsorshipInfoEntity = sponsorshipInfoEntities[0];
          final String isSponseredInCA = sponsorshipInfoEntity.getAttribute(
            BDMOASDatastoreConstants.IS_SPOUSE_OR_CLP_SPONSORED_ATTR);
          if (!StringUtil.isNullOrEmpty(isSponseredInCA)) {
            sponshorshipVO = new BDMOASSponsorshipEvidenceVO();
            sponshorshipVO.setIsSponsored(isSponseredInCA);

            sponshorshipVO.setComments(
              BDMOASEvidenceMappingConstants.kSponsorshipEvidenceComments);

            final HashMap<String, String> evidenceData =
              new BDMLifeEventUtil().getEvidenceData(sponshorshipVO);

            createACOrICDynamicEvidence(concernRoleID, evidenceData,
              CASEEVIDENCEEntry.OAS_SPONSORSHIP, EVIDENCECHANGEREASON.INITIAL,
              caseID, CASEPARTICIPANTROLETYPEEntry.MEMBER);
          }
        }
      }
    } else if (intakeScriptID
      .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_CP_SCRIPT)) {
      final Entity[] sponsorshipInfoEntities =
        personEntityPrimary.getChildEntities(dataStore
          .getEntityType(BDMOASDatastoreConstants.SPONSORSHIPINFO_ENTITY));
      if (sponsorshipInfoEntities.length > 0) {
        final Entity sponsorshipInfoEntity = sponsorshipInfoEntities[0];
        if (primaryParticipantInd) {
          final String isSponseredInCA = sponsorshipInfoEntity
            .getAttribute(BDMOASDatastoreConstants.IS_APLCNT_SPONSORED_ATTR);
          if (!StringUtil.isNullOrEmpty(isSponseredInCA)) {
            sponshorshipVO = new BDMOASSponsorshipEvidenceVO();
            sponshorshipVO.setIsSponsored(isSponseredInCA);

            sponshorshipVO.setComments(
              BDMOASEvidenceMappingConstants.kSponsorshipEvidenceComments);

            final HashMap<String, String> evidenceData =
              new BDMLifeEventUtil().getEvidenceData(sponshorshipVO);

            createACOrICDynamicEvidence(concernRoleID, evidenceData,
              CASEEVIDENCEEntry.OAS_SPONSORSHIP, EVIDENCECHANGEREASON.INITIAL,
              caseID, CASEPARTICIPANTROLETYPEEntry.PRIMARY);
          }
        } else {
          final String isSponseredInCA = sponsorshipInfoEntity.getAttribute(
            BDMOASDatastoreConstants.IS_SPOUSE_OR_CLP_SPONSORED_ATTR);
          if (!StringUtil.isNullOrEmpty(isSponseredInCA)) {
            sponshorshipVO = new BDMOASSponsorshipEvidenceVO();
            sponshorshipVO.setIsSponsored(isSponseredInCA);

            sponshorshipVO.setComments(
              BDMOASEvidenceMappingConstants.kSponsorshipEvidenceComments);

            final HashMap<String, String> evidenceData =
              new BDMLifeEventUtil().getEvidenceData(sponshorshipVO);

            createACOrICDynamicEvidence(concernRoleID, evidenceData,
              CASEEVIDENCEEntry.OAS_SPONSORSHIP, EVIDENCECHANGEREASON.INITIAL,
              caseID, CASEPARTICIPANTROLETYPEEntry.MEMBER);
          }
        }
      }
    }

  }

  /**
   * Map and create Foreign Income evidence
   *
   * @param applicationEntity
   * @param caseID
   * @param dataStoreCEFMappings
   * @param schemaName
   * @param forPrimary
   */
  public void mapForeignIncomeEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createForeignIncomeEvidence(concernRoleID, caseID, applicationEntity,
        schemaName, primaryParticipantInd);
    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.OAS_FOREIGN_INCOME);

    }

  }

  /**
   * Utility method to create Foreign Income Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createForeignIncomeEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity = primaryParticipantInd
      ? applicationEntity.getChildEntities(
        dataStore.getEntityType(BDMDatastoreConstants.PERSON),
        PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0]
      : applicationEntity.getChildEntities(
        dataStore.getEntityType(BDMDatastoreConstants.PERSON),
        NON_PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    final Entity[] foreignIncmEntities =
      personEntity.getChildEntities(dataStore
        .getEntityType(BDMOASDatastoreConstants.FOREIGN_INCOME_ENTITY));

    if (foreignIncmEntities.length > 0) {
      BDMOASForeignIncomeEvidenceVO foreignIncomeVO = null;

      long casePtcptRoleID = 0;

      if (primaryParticipantInd) {
        casePtcptRoleID = BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(
          caseID, CASEPARTICIPANTROLETYPE.PRIMARY);
      } else {
        casePtcptRoleID = BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(
          caseID, CASEPARTICIPANTROLETYPE.MEMBER);
      }

      if (intakeScriptID.equals(
        BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)
        || intakeScriptID
          .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_AP_SCRIPT)) {
        final Entity foreignIncomeEntity = foreignIncmEntities[0];

        final String isReceivingForeignIncome =
          foreignIncomeEntity.getAttribute(
            BDMOASDatastoreConstants.FOREIGN_INCOME_ISRECEIVING_ATTR);

        if (!StringUtil.isNullOrEmpty(isReceivingForeignIncome)
          && isReceivingForeignIncome.equals(IEGYESNO.YES)) {
          foreignIncomeVO =
            populateForeignIncomeEvdDetailsForOASGISApplication(
              foreignIncomeEntity);

          final HashMap<String, String> evidenceData =
            new BDMLifeEventUtil().getEvidenceData(foreignIncomeVO);
          evidenceData.put(
            BDMOASEvidenceMappingConstants.kCaseParticipantRoleID,
            String.valueOf(casePtcptRoleID));

          if (primaryParticipantInd) {
            createACOrICDynamicEvidence(concernRoleID, evidenceData,
              CASEEVIDENCEEntry.OAS_FOREIGN_INCOME,
              EVIDENCECHANGEREASON.INITIAL, caseID,
              CASEPARTICIPANTROLETYPEEntry.PRIMARY);
          } else {
            createACOrICDynamicEvidence(concernRoleID, evidenceData,
              CASEEVIDENCEEntry.OAS_FOREIGN_INCOME,
              EVIDENCECHANGEREASON.INITIAL, caseID,
              CASEPARTICIPANTROLETYPEEntry.MEMBER);
          }
        }
      } else if (intakeScriptID
        .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_CP_SCRIPT)) {
        for (final Entity foreignIncomeEntity : foreignIncmEntities) {
          foreignIncomeVO =
            populateForeignIncomeEvdDetailsForClientPortalGISApplication(
              foreignIncomeEntity);

          final HashMap<String, String> evidenceData =
            new BDMLifeEventUtil().getEvidenceData(foreignIncomeVO);
          evidenceData.put(
            BDMOASEvidenceMappingConstants.kCaseParticipantRoleID,
            String.valueOf(casePtcptRoleID));

          if (primaryParticipantInd) {
            createACOrICDynamicEvidence(concernRoleID, evidenceData,
              CASEEVIDENCEEntry.OAS_FOREIGN_INCOME,
              EVIDENCECHANGEREASON.INITIAL, caseID,
              CASEPARTICIPANTROLETYPEEntry.PRIMARY);
          } else {
            createACOrICDynamicEvidence(concernRoleID, evidenceData,
              CASEEVIDENCEEntry.OAS_FOREIGN_INCOME,
              EVIDENCECHANGEREASON.INITIAL, caseID,
              CASEPARTICIPANTROLETYPEEntry.MEMBER);
          }

        }
      }
    }
  }

  /**
   * Method to populate Foreign Income Evidence Data for OAS GIS Combined
   * Application.
   *
   * @param Entity foreignIncomeEntity
   */
  private BDMOASForeignIncomeEvidenceVO
    populateForeignIncomeEvdDetailsForOASGISApplication(
      final Entity foreignIncomeEntity) {

    final BDMOASForeignIncomeEvidenceVO foreignIncomeVO =
      new BDMOASForeignIncomeEvidenceVO();
    foreignIncomeVO.setAmount(BDMConstants.EMPTY_STRING);

    foreignIncomeVO.setCanadianAmount(BDMConstants.EMPTY_STRING);

    foreignIncomeVO.setIncomeType(BDMConstants.EMPTY_STRING);

    foreignIncomeVO.setOtherDescription(BDMConstants.EMPTY_STRING);

    foreignIncomeVO.setCurrency(BDMConstants.EMPTY_STRING);

    foreignIncomeVO.setCountry(BDMConstants.EMPTY_STRING);

    final String annualFrgnIncmAmtAndCurrency = foreignIncomeEntity
      .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCOME_ANNUAL_ATTR);

    if (!StringUtil.isNullOrEmpty(annualFrgnIncmAmtAndCurrency)) {
      foreignIncomeVO.setComments(annualFrgnIncmAmtAndCurrency);
    } else {
      foreignIncomeVO.setComments(BDMConstants.EMPTY_STRING);
    }

    return foreignIncomeVO;
  }

  /**
   * Method to populate Foreign Income Evidence Data for Client Portal GIS
   * Application.
   *
   * @param Entity foreignIncomeEntity
   */
  private BDMOASForeignIncomeEvidenceVO
    populateForeignIncomeEvdDetailsForClientPortalGISApplication(
      final Entity foreignIncomeEntity) {

    final BDMOASForeignIncomeEvidenceVO foreignIncomeVO =
      new BDMOASForeignIncomeEvidenceVO();

    String incomeAmount = foreignIncomeEntity
      .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCOME_AMOUNT_ATTR);
    if (!StringUtil.isNullOrEmpty(incomeAmount)) {
      incomeAmount = incomeAmount.replace(",", "");
      foreignIncomeVO.setAmount(incomeAmount);
    }

    foreignIncomeVO.setCanadianAmount(BDMConstants.EMPTY_STRING);

    final String incomeType = foreignIncomeEntity
      .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCM_TYPE_ATTR);

    if (!StringUtil.isNullOrEmpty(incomeType)) {
      foreignIncomeVO.setIncomeType(incomeType);
    } else {
      foreignIncomeVO.setIncomeType(BDMConstants.EMPTY_STRING);
    }

    final String incomeYear = foreignIncomeEntity
      .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCOME_YEAR_ATTR);

    if (!StringUtil.isNullOrEmpty(incomeYear)) {
      foreignIncomeVO.setYear(incomeYear);
    } else {
      foreignIncomeVO.setYear(BDMConstants.EMPTY_STRING);
    }

    final String incomeTpOthrDesc = foreignIncomeEntity.getAttribute(
      BDMOASDatastoreConstants.FOREIGN_INCOME_TP_OTHR_DESC_ATTR);

    if (!StringUtil.isNullOrEmpty(incomeTpOthrDesc)) {
      foreignIncomeVO.setOtherDescription(incomeTpOthrDesc);
    } else {
      foreignIncomeVO.setOtherDescription(BDMConstants.EMPTY_STRING);
    }

    final String incomeCurrency = foreignIncomeEntity
      .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCOME_CURRENCY_ATTR);

    if (!StringUtil.isNullOrEmpty(incomeCurrency)) {
      foreignIncomeVO.setCurrency(incomeCurrency);
    } else {
      foreignIncomeVO.setCurrency(BDMConstants.EMPTY_STRING);
    }

    final String incomeCountry = foreignIncomeEntity
      .getAttribute(BDMOASDatastoreConstants.FOREIGN_INCOME_COUNTRY_ATTR);

    if (!StringUtil.isNullOrEmpty(incomeCountry)) {
      foreignIncomeVO.setCountry(incomeCountry);
    } else {
      foreignIncomeVO.setCountry(BDMConstants.EMPTY_STRING);
    }

    return foreignIncomeVO;
  }

  /**
   * Map and create Residence Period evidence
   *
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapResidencePeriodEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createResidencePeriodEvidence(concernRoleID, caseID, applicationEntity,
        schemaName, primaryParticipantInd);
    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD);

    }

  }

  /**
   * Utility method to create Residence Period Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createResidencePeriodEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    BDMOASResidencePeriodEvidenceVO residencePeriodVO =
      new BDMOASResidencePeriodEvidenceVO();

    final Entity legalStatusEntity = personEntity.getChildEntities(dataStore
      .getEntityType(BDMOASDatastoreConstants.LEGAL_STATUS_ENTITY))[0];

    boolean residHistEvdMapInd = false;

    long casePtcptRoleID = 0;
    if (primaryParticipantInd) {
      casePtcptRoleID = BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(
        caseID, CASEPARTICIPANTROLETYPE.PRIMARY);
    } else {
      casePtcptRoleID = BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(
        caseID, CASEPARTICIPANTROLETYPE.MEMBER);
    }

    if (intakeScriptID.equals(
      BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)) {
      final String isLivedInCASinceAge18 = legalStatusEntity.getAttribute(
        BDMOASDatastoreConstants.LEGAL_STATUS_LIVEDINCASINCE18_ATTR);

      if (isLivedInCASinceAge18.equals(IEGYESNO.YES)) {

        residencePeriodVO.setCountry(COUNTRY.CA);

        final Date dateOfBirth = Date.getDate(
          personEntity.getAttribute(BDMDatastoreConstants.DATE_OF_BIRTH));

        final Calendar cal = dateOfBirth.getCalendar();
        cal.add(Calendar.YEAR, 18);

        residencePeriodVO
          .setStartDate(Date.getFromJavaUtilDate(cal.getTime()));

        residencePeriodVO.setResidenceType(OASRESIDENCETYPE.RESIDENCE);

        residencePeriodVO.setComments(
          BDMOASEvidenceMappingConstants.kResidencePeriodEvidenceComments);

        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(residencePeriodVO);

        evidenceData.put(BDMOASEvidenceMappingConstants.kCaseParticipantRole,
          String.valueOf(casePtcptRoleID));

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_RESIDENCE_PERIOD,
          EVIDENCECHANGEREASON.INITIAL, caseID);
      } else {
        residHistEvdMapInd = true;
      }
    } else if (intakeScriptID
      .equals(BDMOASDatastoreConstants.BDM_GIS_APPLICATION_AP_SCRIPT)) {
      residHistEvdMapInd = true;
    }

    if (residHistEvdMapInd) {
      final Entity[] residencePeriodEntities = personEntity.getChildEntities(
        dataStore.getEntityType(BDMOASDatastoreConstants.RESIDENCE_HISTORY));

      for (final Entity residencePeriodEntity : residencePeriodEntities) {

        residencePeriodVO = new BDMOASResidencePeriodEvidenceVO();
        residencePeriodVO.setCountry(residencePeriodEntity.getAttribute(
          BDMOASDatastoreConstants.RESIDENCE_HISTORY_COUNTRY_ATTR));

        final String residenceHistoryStartDate =
          residencePeriodEntity.getAttribute(
            BDMOASDatastoreConstants.RESIDENCE_HISTORY_START_ATTR);

        final String residenceHistoryEndDate = residencePeriodEntity
          .getAttribute(BDMOASDatastoreConstants.RESIDENCE_HISTORY_END_ATTR);
        final Date residencePeriodStartDate =
          StringUtil.isNullOrEmpty(residenceHistoryStartDate) ? Date.kZeroDate
            : Date.getDate(residenceHistoryStartDate);

        final Date residencePeriodEndDate =
          StringUtil.isNullOrEmpty(residenceHistoryEndDate) ? Date.kZeroDate
            : Date.getDate(residenceHistoryEndDate);
        residencePeriodVO.setStartDate(residencePeriodStartDate);
        residencePeriodVO.setEndDate(residencePeriodEndDate);

        residencePeriodVO
          .setResidenceType(OASRESIDENCETYPEEntry.NOT_SPECIFIED.getCode());

        residencePeriodVO.setComments(
          BDMOASEvidenceMappingConstants.kResidencePeriodEvidenceComments);

        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(residencePeriodVO);

        evidenceData.put(BDMOASEvidenceMappingConstants.kCaseParticipantRole,
          String.valueOf(casePtcptRoleID));

        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_RESIDENCE_PERIOD,
          EVIDENCECHANGEREASON.INITIAL, caseID);

      }
    }

  }

  /**
   * Map and create Marital Relationship evidence
   *
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapMaritalRelationshipEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName) throws AppException, InformationalException {

    try {
      createMaritalRelationshipEvidence(caseID, applicationEntity, schemaName,
        dataStoreCEFMappings);
    } catch (final Exception e) {
      logEvidenceException(
        dataStoreCEFMappings.getPrimaryClient().getConcernRoleID(), e,
        CASEEVIDENCEEntry.OAS_MARITAL_RELATIONSHIP);
    }

  }

  /**
   * Utility method to create Marital Relationship Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createMaritalRelationshipEvidence(final long caseID,
    final Entity applicationEntity, final String schemaName,
    final DataStoreCEFMappings dataStoreCEFMappings)
    throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();

    final String isSpouseOrCLPDetailsEneredInd =
      personEntity.getAttribute(BDMOASDatastoreConstants.SPOUSE_OR_CLP_IND);

    final long memCasePtcptRoleID = BDMOASEvidenceUtil
      .getCaseParticipantRoleIDByType(caseID, CASEPARTICIPANTROLETYPE.MEMBER);

    final Entity[] maritalStatusEntities =
      personEntity.getChildEntities(dataStore
        .getEntityType(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY));

    if (maritalStatusEntities.length > 0) {
      final Entity maritalStatusEntity = maritalStatusEntities[0];

      final String currentMaritalStatus = maritalStatusEntity.getAttribute(
        BDMOASDatastoreConstants.MARITAL_STATUS_INFO_CURNTSTATUS_ATTR);

      final Entity rootEntity = personEntity.getParentEntity();
      final String applicationForm =
        rootEntity.getAttribute("applicationForm");
      String comments = ". ";
      if (BDMOASAPPLICATIONFORM.ISP5054.equals(applicationForm)) {
        final String spouseFullName =
          maritalStatusEntity.getAttribute("spouseFullName");
        final String spouseDateOfBirth =
          maritalStatusEntity.getAttribute("spouseDateOfBirth");
        final String spouseSIN =
          maritalStatusEntity.getAttribute("spouseSIN");
        if (!spouseFullName.isEmpty()) {
          comments = comments + "Full Name: " + spouseFullName;
        }
        if (!spouseDateOfBirth.isEmpty()) {
          final Date dob = Date.getDate(spouseDateOfBirth);
          comments = comments + ", " + "Date of Birth: " + dob;
        }
        if (!spouseSIN.isEmpty()) {
          comments = comments + ", " + "Canadian SIN: " + spouseSIN;
        }
      }

      if (!currentMaritalStatus.equals(BDMMARITALSTATUS.SINGLE)) {
        // && isSpouseOrCLPDetailsEneredInd.equals("true")) {
        BDMOASMaritalRelationshipEvidenceVO maritalRelationshipVO = null;

        boolean mapLivngAprtForReasnBeyondCntrlEv = false;
        if (currentMaritalStatus.equals(BDMMARITALSTATUS.MARRIED)) {
          maritalRelationshipVO = new BDMOASMaritalRelationshipEvidenceVO();

          maritalRelationshipVO.setRelationshipStatus(
            BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

          final String maritalDateStr = maritalStatusEntity.getAttribute(
            BDMOASDatastoreConstants.MARITAL_STATUS_INFO_MARITALDATE_ATTR);

          if (maritalDateStr != null && !maritalDateStr.isEmpty()) {
            maritalRelationshipVO.setStartDate(Date.getDate(maritalDateStr));
          }

          maritalRelationshipVO.setRelatedParticipantID(memCasePtcptRoleID);

          if (BDMOASAPPLICATIONFORM.ISP5054.equals(applicationForm)) {
            maritalRelationshipVO.setComments(
              BDMOASEvidenceMappingConstants.kMaritalRelationshipEvidenceComments
                + comments);
          } else {
            maritalRelationshipVO.setComments(
              BDMOASEvidenceMappingConstants.kMaritalRelationshipEvidenceComments);
          }
          mapLivngAprtForReasnBeyondCntrlEv = true;

        } else if (currentMaritalStatus.equals(BDMMARITALSTATUS.COMMONLAW)) {
          maritalRelationshipVO = new BDMOASMaritalRelationshipEvidenceVO();
          maritalRelationshipVO.setRelationshipStatus(
            BDMOASMARITALRELATION.OAS_MARITAL_RELATION_COMMON_LAW);

          final String maritalDateStr = maritalStatusEntity.getAttribute(
            BDMOASDatastoreConstants.MARITAL_STATUS_INFO_MARITALDATE_ATTR);

          if (maritalDateStr != null && !maritalDateStr.isEmpty()) {
            maritalRelationshipVO.setStartDate(Date.getDate(maritalDateStr));
          }

          maritalRelationshipVO.setRelatedParticipantID(memCasePtcptRoleID);

          if (BDMOASAPPLICATIONFORM.ISP5054.equals(applicationForm)) {
            maritalRelationshipVO.setComments(
              BDMOASEvidenceMappingConstants.kMaritalRelationshipEvidenceComments
                + comments);
          } else {
            maritalRelationshipVO.setComments(
              BDMOASEvidenceMappingConstants.kMaritalRelationshipEvidenceComments);
          }
          mapLivngAprtForReasnBeyondCntrlEv = true;
        } else if (currentMaritalStatus.equals(BDMMARITALSTATUS.DIVORCED)) {
          maritalRelationshipVO = new BDMOASMaritalRelationshipEvidenceVO();
          maritalRelationshipVO.setRelationshipStatus(
            BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);

          maritalRelationshipVO.setRelationshipChangeType(
            BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_DIVORCE);

          final String maritalDateStr = maritalStatusEntity.getAttribute(
            BDMOASDatastoreConstants.MARITAL_STATUS_INFO_MARITALDATE_ATTR);

          if (maritalDateStr != null && !maritalDateStr.isEmpty()) {
            maritalRelationshipVO.setEndDate(Date.getDate(maritalDateStr));
          }

          maritalRelationshipVO.setRelatedParticipantID(memCasePtcptRoleID);

          if (BDMOASAPPLICATIONFORM.ISP5054.equals(applicationForm)) {
            maritalRelationshipVO.setComments(
              BDMOASEvidenceMappingConstants.kMaritalRelationshipEvidenceComments
                + comments);
          } else {
            maritalRelationshipVO.setComments(
              BDMOASEvidenceMappingConstants.kMaritalRelationshipEvidenceComments);
          }

        } else if (currentMaritalStatus.equals(BDMMARITALSTATUS.WIDOWED)) {
          maritalRelationshipVO = new BDMOASMaritalRelationshipEvidenceVO();

          maritalRelationshipVO.setRelationshipChangeType(
            BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_DEATH);

          final String maritalDateStr = maritalStatusEntity.getAttribute(
            BDMOASDatastoreConstants.MARITAL_STATUS_INFO_MARITALDATE_ATTR);

          if (maritalDateStr != null && !maritalDateStr.isEmpty()) {
            maritalRelationshipVO.setEndDate(Date.getDate(maritalDateStr));
          }

          maritalRelationshipVO.setRelatedParticipantID(memCasePtcptRoleID);

          if (BDMOASAPPLICATIONFORM.ISP5054.equals(applicationForm)) {
            maritalRelationshipVO.setComments(
              BDMOASEvidenceMappingConstants.kMaritalRelationshipEvidenceComments
                + comments);
          } else {
            maritalRelationshipVO.setComments(
              BDMOASEvidenceMappingConstants.kMaritalRelationshipEvidenceComments);
          }

        } else if (currentMaritalStatus.equals(BDMMARITALSTATUS.SEPARATED)) {
          maritalRelationshipVO = new BDMOASMaritalRelationshipEvidenceVO();
          maritalRelationshipVO.setRelationshipChangeType(
            BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_SEPARATION);

          maritalRelationshipVO.setRelatedParticipantID(memCasePtcptRoleID);

          String separationDateStr = maritalStatusEntity
            .getAttribute(BDMOASDatastoreConstants.SEPERATION_DATE_ATTR);

          if (separationDateStr == null || separationDateStr.isEmpty()) {
            separationDateStr = maritalStatusEntity.getAttribute(
              BDMOASDatastoreConstants.MARITAL_STATUS_INFO_MARITALDATE_ATTR);
          }

          final Date separationDate = Date.getDate(separationDateStr);

          if (BDMOASAPPLICATIONFORM.ISP5054.equals(applicationForm)) {
            maritalRelationshipVO
              .setComments("Separated as of " + separationDate + comments);
          } else {
            maritalRelationshipVO
              .setComments("Separated as of " + separationDate);
          }
          mapLivngAprtForReasnBeyondCntrlEv = true;
        }

        if (maritalRelationshipVO != null) {
          final HashMap<String, String> evidenceData =
            new BDMLifeEventUtil().getEvidenceData(maritalRelationshipVO);

          final long casePtcptRoleID =
            BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(caseID,
              CASEPARTICIPANTROLETYPE.PRIMARY);

          evidenceData.put(
            BDMOASEvidenceMappingConstants.kCaseParticipantRoleID,
            String.valueOf(casePtcptRoleID));

          final ReturnEvidenceDetails retunEvidenceDetails =
            BDMOASEvidenceUtil.createACDynamicEvidence(concernRoleID,
              evidenceData, CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP,
              EVIDENCECHANGEREASON.INITIAL, caseID);

          if (mapLivngAprtForReasnBeyondCntrlEv
            && retunEvidenceDetails.evidenceKey.evidenceID != 0L
            && intakeScriptID.equals(
              BDMOASDatastoreConstants.BDMOASGIS_COMBINED_APPLICATION_AP_SCRIPT)) {
            this.mapLivingApartforReasonsBeyondControlEvidence(
              applicationEntity, caseID, dataStoreCEFMappings, schemaName,
              retunEvidenceDetails.evidenceKey.evidenceID);
          }
        }
      }
    }
  }

  /**
   * Map and create Marital Status Evidence
   *
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public static void mapMaritalStatusEvidence(final long concernRoleID,
    final BDMMARITALSTATUSEntry maritalStatus)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final Date startDate = Date.getCurrentDate();

    try {

      createMaritalStatusEvidence(pdcCaseIDCaseParticipantRoleID.caseID,
        pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID, concernRoleID,
        startDate, maritalStatus, EVIDENCECHANGEREASON.INITIAL);

    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.BDM_MARITAL_STATUS);
    }

  }

  private static void createMaritalStatusEvidence(final Long caseID,
    final Long cprID, final long concernroleID, final Date startDate,
    final BDMMARITALSTATUSEntry maritalStatus,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(CASE_PARTICIPANT_ROLE_ID),
      cprID);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(START_DATE), startDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(MARITAL_STATUS),
      new CodeTableItem(BDMMARITALSTATUS.TABLENAME, maritalStatus.getCode()));

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;
    descriptor.changeReason = evidenceChangeReason;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(cprID);
    genericDtls.addRelCp(CASE_PARTICIPANT_ROLE_ID, cpAdaptor);

    evidenceServiceInterface.createEvidence(genericDtls);
  }

  public static void logEvidenceException(final long concernRoleID,
    final Exception e, final CASEEVIDENCEEntry caseEvidenceEntry)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(LOG_FORMATTER,
      BDMOASEvidenceMappingConstants.BDM_OAS_LOGS_PREFIX,
      CodeTable.getOneItemForUserLocale(CASEEVIDENCE.TABLENAME,
        caseEvidenceEntry.getCode()),
      concernRoleID, e.getMessage());

    if (Trace.atLeast(Trace.kTraceOn)) {
      Trace.kTopLevelLogger.log(Level.INFO, e);
    }
  }

  public static void logPDCEvidenceException(final long concernRoleID,
    final Exception e, final String pdcEvidence)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(LOG_FORMATTER,
      BDMOASEvidenceMappingConstants.BDM_OAS_LOGS_PREFIX,
      CodeTable.getOneItemForUserLocale(CASEEVIDENCE.TABLENAME, pdcEvidence),
      concernRoleID, e.getMessage());

    if (Trace.atLeast(Trace.kTraceVerbose)) {
      Trace.kTopLevelLogger.log(Level.INFO, e);
    }
  }

  /**
   * Maps Marital Relationship Evidence from submitted OASGI application
   * data.
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  public void mapNatureOfAbsencesEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreToCEFMappings,
    final String schemaName) throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreToCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createNatureOfAbsencesEvidence(concernRoleID, caseID, applicationEntity,
        schemaName);
    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.OAS_NATURE_OF_ABSENCES);
    }

  }

  /**
   * Creates Marital Relationship Evidence.
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createNatureOfAbsencesEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName) throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    final Entity[] absenceEntities = personEntity.getChildEntities(dataStore
      .getEntityType(BDMOASDatastoreConstants.NATURE_OF_ABSENCES_ENTITY));

    if (absenceEntities.length > 0) {
      final BDMOASNatureOfAbsencesEvidenceVO absencesVO =
        new BDMOASNatureOfAbsencesEvidenceVO();

      final Entity absencesEntity = absenceEntities[0];

      final String natureOfAbsences = absencesEntity
        .getAttribute(BDMOASDatastoreConstants.NATURE_OF_ABSENCES_ATTR);

      if (StringUtils.isNotBlank(natureOfAbsences)) {

        absencesVO.setAbsenceDetails(natureOfAbsences);

        absencesVO.setComments(
          BDMOASEvidenceMappingConstants.kNatureOfAbsencesEvidenceComments);

        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(absencesVO);
        BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID,
          evidenceData, CASEEVIDENCE.OAS_NATURE_OF_ABSENCES,
          EVIDENCECHANGEREASON.INITIAL, caseID);
      }
    }

  }

  /**
   * Maps World Income Evidence from submitted OASGIS application
   * data.
   *
   * @param applicationEntity
   * @param caseID
   * @param dataStoreToCEFMappings
   * @param schemaName
   */
  public void mapWorldIncomeEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreToCEFMappings,
    final String schemaName) throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreToCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createWorldIncomeEvidence(concernRoleID, caseID, applicationEntity,
        schemaName);
    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.OAS_WORLD_INCOME);
    }

  }

  /**
   * Creates World Income Evidence.
   *
   * @param concernRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createWorldIncomeEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName) throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    final BDMOASWorldIncomeEvidenceVO worldIncomeVO =
      new BDMOASWorldIncomeEvidenceVO();

    final Entity worldIncomeEntity = personEntity.getChildEntities(dataStore
      .getEntityType(BDMOASDatastoreConstants.WORLD_INCOME_ENTITY))[0];

    final String yearAndThresholdAmount = worldIncomeEntity
      .getAttribute(BDMOASDatastoreConstants.WORLD_INCOME_THRESHOLD_AMT_ATTR);

    worldIncomeVO.setThreshold(yearAndThresholdAmount);

    final String isNonResidentAndIncomeLimitThreshold = worldIncomeEntity
      .getAttribute(BDMOASDatastoreConstants.WORLD_INCOME_ATTR);

    if (!StringUtil.isNullOrEmpty(isNonResidentAndIncomeLimitThreshold)) {
      worldIncomeVO.setOverThreshold(isNonResidentAndIncomeLimitThreshold);
    } else {
      worldIncomeVO
        .setOverThreshold(BDMOASTAXTHRESHOLDEntry.NOT_SPECIFIED.getCode());
    }

    worldIncomeVO.setComments(
      BDMOASEvidenceMappingConstants.kWorldIncomeEvidenceComments);

    final HashMap<String, String> evidenceData =
      new BDMLifeEventUtil().getEvidenceData(worldIncomeVO);
    BDMEvidenceUtil.createACOrICDynamicEvidence(concernRoleID, evidenceData,
      CASEEVIDENCE.OAS_WORLD_INCOME, EVIDENCECHANGEREASON.INITIAL, caseID);

  }

  /**
   * Maps Retirement Pension Reduction Evidence from submitted OASGIS
   * application
   * data.
   *
   * @param applicationEntity
   * @param caseID
   * @param dataStoreToCEFMappings
   * @param schemaName
   * @param primaryParticipantInd
   */
  public void mapRetirementOrPensionReductionEvidence(
    final Entity applicationEntity, final long caseID,
    final DataStoreCEFMappings dataStoreToCEFMappings,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreToCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createRetirementPensionReductionEvidence(concernRoleID, caseID,
        applicationEntity, schemaName, primaryParticipantInd);
    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.OAS_RETIREMENT_PENSION_REDUCTION);
    }

  }

  /**
   * Creates Retirement Pension Reduction Evidence.
   *
   * @param concernRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   * @param primaryParticipantInd
   */
  private void createRetirementPensionReductionEvidence(
    final long concernRoleID, final long caseID,
    final Entity applicationEntity, final String schemaName,
    final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity = primaryParticipantInd
      ? applicationEntity.getChildEntities(
        dataStore.getEntityType(BDMDatastoreConstants.PERSON),
        PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0]
      : applicationEntity.getChildEntities(
        dataStore.getEntityType(BDMDatastoreConstants.PERSON),
        NON_PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    final Entity[] retIncmEntities =
      personEntity.getChildEntities(dataStore.getEntityType(
        BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_ENTITY));

    if (retIncmEntities.length > 0) {
      final BDMOASRetirementPensionReductionEvidenceVO retirementPensionReductionVO =
        new BDMOASRetirementPensionReductionEvidenceVO();

      final Entity retirementIncomeEntity = retIncmEntities[0];

      final String isRetiredOrRetiringInNext2years =
        retirementIncomeEntity.getAttribute(
          BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIRED_ORRETIRING_ATTR);

      final String isPensionReduced2years =
        retirementIncomeEntity.getAttribute(
          BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_REDUCED_ATTR);

      if (!StringUtil.isNullOrEmpty(isRetiredOrRetiringInNext2years)
        && isRetiredOrRetiringInNext2years.equals(IEGYESNO.YES)) {

        final String retirementDae = retirementIncomeEntity.getAttribute(
          BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_RETIREDATE_ATTR);

        retirementPensionReductionVO
          .setEventDate(Date.getDate(retirementDae));

        retirementPensionReductionVO
          .setEventType(BDMOASOPTIONABLEEVENT.RETIREMENT);

        retirementPensionReductionVO.setComments(
          BDMOASEvidenceMappingConstants.kRetirementPensReductionEvidenceComments);

        final HashMap<String, String> evidenceData = new BDMLifeEventUtil()
          .getEvidenceData(retirementPensionReductionVO);

        if (primaryParticipantInd) {
          createACOrICDynamicEvidence(concernRoleID, evidenceData,
            CASEEVIDENCEEntry.OAS_RETIREMENT_PENSION_REDUCTION,
            EVIDENCECHANGEREASON.INITIAL, caseID,
            CASEPARTICIPANTROLETYPEEntry.PRIMARY);
        } else {
          createACOrICDynamicEvidence(concernRoleID, evidenceData,
            CASEEVIDENCEEntry.OAS_RETIREMENT_PENSION_REDUCTION,
            EVIDENCECHANGEREASON.INITIAL, caseID,
            CASEPARTICIPANTROLETYPEEntry.MEMBER);
        }

      }

      if (!StringUtil.isNullOrEmpty(isPensionReduced2years)
        && isPensionReduced2years.equals(IEGYESNO.YES)) {
        final String pensionReductionDate =
          retirementIncomeEntity.getAttribute(
            BDMOASDatastoreConstants.RETIREMENT_PENSION_REDUCTION_DATE_ATTR);

        retirementPensionReductionVO
          .setEventDate(Date.getDate(pensionReductionDate));
        retirementPensionReductionVO
          .setEventType(BDMOASOPTIONABLEEVENT.RETIREMENT);
        retirementPensionReductionVO
          .setEventType(BDMOASOPTIONABLEEVENT.PENSION_REDUCTION);

        retirementPensionReductionVO.setComments(
          BDMOASEvidenceMappingConstants.kRetirementPensReductionEvidenceComments);

        final HashMap<String, String> evidenceData = new BDMLifeEventUtil()
          .getEvidenceData(retirementPensionReductionVO);

        if (primaryParticipantInd) {
          createACOrICDynamicEvidence(concernRoleID, evidenceData,
            CASEEVIDENCEEntry.OAS_RETIREMENT_PENSION_REDUCTION,
            EVIDENCECHANGEREASON.INITIAL, caseID,
            CASEPARTICIPANTROLETYPEEntry.PRIMARY);
        } else {
          createACOrICDynamicEvidence(concernRoleID, evidenceData,
            CASEEVIDENCEEntry.OAS_RETIREMENT_PENSION_REDUCTION,
            EVIDENCECHANGEREASON.INITIAL, caseID,
            CASEPARTICIPANTROLETYPEEntry.MEMBER);
        }

      }
    }

  }

  /**
   * Maps Manually Reported/Entered Evidence from submitted OASGIS
   * application data.
   *
   * @param applicationEntity
   * @param caseID
   * @param dataStoreToCEFMappings
   * @param schemaName
   * @param primaryParticipantInd
   */
  public void mapManuallyEnteredIncomeEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreToCEFMappings,
    final String schemaName, final boolean primaryParticipantInd)
    throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreToCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createManuallyEnteredIncomeEvidence(concernRoleID, caseID,
        applicationEntity, schemaName, primaryParticipantInd);
    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME);
    }

  }

  /**
   * Creates Manually Reported/Entered Evidence.
   *
   * @param concernRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   * @param primaryParticipantInd
   */
  private void createManuallyEnteredIncomeEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName, final boolean primaryParticipantInd)
    throws InformationalException, AppException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);

    Entity personEntity = null;

    if (primaryParticipantInd) {
      personEntity = applicationEntity.getChildEntities(
        dataStore.getEntityType(BDMDatastoreConstants.PERSON),
        PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];
    } else {
      personEntity = applicationEntity.getChildEntities(
        dataStore.getEntityType(BDMDatastoreConstants.PERSON),
        NON_PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];
    }

    final Entity[] incomeEntities = personEntity.getChildEntities(
      dataStore.getEntityType(BDMOASDatastoreConstants.INCOME));

    if (incomeEntities.length > 0) {
      final BDMOASManuallyEnteredIncomeEvidenceVO manuallyReportedIncomeVO =
        new BDMOASManuallyEnteredIncomeEvidenceVO();

      final Entity incomeEntity = incomeEntities[0];

      manuallyReportedIncomeVO.setYear(
        incomeEntity.getAttribute(BDMOASDatastoreConstants.INCOME_YEAR_ATTR));

      // TODO: It is mandatory field in the evidence but mapping is missing in
      // the FDD.
      manuallyReportedIncomeVO.setPreferred(BDMYESNO.YES);

      String cppOrQPPAmount =
        incomeEntity.getAttribute(BDMOASDatastoreConstants.kCppOrQPPAmount);
      if (!StringUtil.isNullOrEmpty(cppOrQPPAmount)) {
        cppOrQPPAmount = cppOrQPPAmount.replace(",", "");
        manuallyReportedIncomeVO
          .setCanadaPensionPlanOrQuebecPensionBenefit(cppOrQPPAmount);
      }

      String othrCAPensionAmount = incomeEntity
        .getAttribute(BDMOASDatastoreConstants.kOthrCAPensionAmount);
      if (!StringUtil.isNullOrEmpty(othrCAPensionAmount)) {
        othrCAPensionAmount = othrCAPensionAmount.replace(",", "");
        manuallyReportedIncomeVO
          .setOtherCanadianPensionIncome(othrCAPensionAmount);
      }

      String frgnPensionAmount = incomeEntity
        .getAttribute(BDMOASDatastoreConstants.kFrgnPensionAmount);
      if (!StringUtil.isNullOrEmpty(frgnPensionAmount)) {
        frgnPensionAmount = frgnPensionAmount.replace(",", "");
        manuallyReportedIncomeVO.setForeignPensionIncome(frgnPensionAmount);
      }

      String eiAmount =
        incomeEntity.getAttribute(BDMOASDatastoreConstants.kEiAmount);
      if (!StringUtil.isNullOrEmpty(eiAmount)) {
        eiAmount = eiAmount.replace(",", "");
        manuallyReportedIncomeVO.setEmploymentInsurance(eiAmount);
      }

      String wcbAmount =
        incomeEntity.getAttribute(BDMOASDatastoreConstants.kWcbAmount);
      if (!StringUtil.isNullOrEmpty(wcbAmount)) {
        wcbAmount = wcbAmount.replace(",", "");
        manuallyReportedIncomeVO.setWorkersCompensationBenefits(wcbAmount);
      }

      String interestAndInvstAmount = incomeEntity
        .getAttribute(BDMOASDatastoreConstants.kInterestAndInvstAmount);
      if (!StringUtil.isNullOrEmpty(interestAndInvstAmount)) {
        interestAndInvstAmount = interestAndInvstAmount.replace(",", "");
        manuallyReportedIncomeVO
          .setInterestAndOtherInvestmentIncome(interestAndInvstAmount);
      }

      String dividendsAmount =
        incomeEntity.getAttribute(BDMOASDatastoreConstants.kDividendsAmount);
      if (!StringUtil.isNullOrEmpty(dividendsAmount)) {
        dividendsAmount = dividendsAmount.replace(",", "");
        manuallyReportedIncomeVO
          .setEligibleAndOtherThanEligibleDividends(dividendsAmount);
      }

      String capitalGainsAmount = incomeEntity
        .getAttribute(BDMOASDatastoreConstants.kCapitalGainsAmount);
      if (!StringUtil.isNullOrEmpty(capitalGainsAmount)) {
        capitalGainsAmount = capitalGainsAmount.replace(",", "");
        manuallyReportedIncomeVO.setCapitalGains(capitalGainsAmount);
      }

      String netRentalAmount =
        incomeEntity.getAttribute(BDMOASDatastoreConstants.kNetRentalAmount);
      if (!StringUtil.isNullOrEmpty(netRentalAmount)) {
        netRentalAmount = netRentalAmount.replace(",", "");
        manuallyReportedIncomeVO.setNetRentalIncome(netRentalAmount);
      }

      String netEmploymentAmount = incomeEntity
        .getAttribute(BDMOASDatastoreConstants.kNetEmploymentAmount);
      if (!StringUtil.isNullOrEmpty(netEmploymentAmount)) {
        netEmploymentAmount = netEmploymentAmount.replace(",", "");
        manuallyReportedIncomeVO.setNetEmploymentIncome(netEmploymentAmount);
      }

      String netSelfEmploymentAmount = incomeEntity
        .getAttribute(BDMOASDatastoreConstants.kNetSelfEmploymentAmount);
      if (!StringUtil.isNullOrEmpty(netSelfEmploymentAmount)) {
        netSelfEmploymentAmount = netSelfEmploymentAmount.replace(",", "");
        manuallyReportedIncomeVO
          .setNetSelfEmploymentIncome(netSelfEmploymentAmount);
      }

      String otherIncmAmount =
        incomeEntity.getAttribute(BDMOASDatastoreConstants.kOtherIncmAmount);
      if (!StringUtil.isNullOrEmpty(otherIncmAmount)) {
        otherIncmAmount = otherIncmAmount.replace(",", "");
        manuallyReportedIncomeVO.setOtherIncome(otherIncmAmount);
      }

      manuallyReportedIncomeVO.setComments(
        BDMOASEvidenceMappingConstants.kManuallyEnteredEvidenceComments);

      final HashMap<String, String> evidenceData =
        new BDMLifeEventUtil().getEvidenceData(manuallyReportedIncomeVO);

      long casePtcptRoleID = 0;

      if (primaryParticipantInd) {
        casePtcptRoleID = BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(
          caseID, CASEPARTICIPANTROLETYPE.PRIMARY);

        evidenceData.put(BDMOASEvidenceMappingConstants.kCaseParticipantRole,
          String.valueOf(casePtcptRoleID));

        createACOrICDynamicEvidence(concernRoleID, evidenceData,
          CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME,
          EVIDENCECHANGEREASON.INITIAL, caseID,
          CASEPARTICIPANTROLETYPEEntry.PRIMARY);
      } else {
        casePtcptRoleID = BDMOASEvidenceUtil.getCaseParticipantRoleIDByType(
          caseID, CASEPARTICIPANTROLETYPE.MEMBER);

        evidenceData.put(BDMOASEvidenceMappingConstants.kCaseParticipantRole,
          String.valueOf(casePtcptRoleID));

        createACOrICDynamicEvidence(concernRoleID, evidenceData,
          CASEEVIDENCEEntry.OAS_MANUALLY_ENTERED_INCOME,
          EVIDENCECHANGEREASON.INITIAL, caseID,
          CASEPARTICIPANTROLETYPEEntry.MEMBER);
      }
    }

  }

  /**
   * Map and create Marital Relationship evidence
   *
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapLivingApartforReasonsBeyondControlEvidence(
    final Entity applicationEntity, final long caseID,
    final DataStoreCEFMappings dataStoreCEFMappings, final String schemaName,
    final long parentEvidenceID) throws AppException, InformationalException {

    try {
      createLivingApartforReasonsBeyondControlEvidence(caseID,
        applicationEntity, schemaName, dataStoreCEFMappings,
        parentEvidenceID);
    } catch (final Exception e) {
      logEvidenceException(
        dataStoreCEFMappings.getPrimaryClient().getConcernRoleID(), e,
        CASEEVIDENCEEntry.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL);
    }

  }

  /**
   * Utility method to create Marital Relationship Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createLivingApartforReasonsBeyondControlEvidence(
    final long caseID, final Entity applicationEntity,
    final String schemaName, final DataStoreCEFMappings dataStoreCEFMappings,
    final long parentEvidenceID) throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);
    final Entity personEntity = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMDatastoreConstants.PERSON),
      PRIMARY_PARTICIPANT_BOOLEAN_EXP)[0];

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();

    final Entity[] maritalStatusEntities =
      personEntity.getChildEntities(dataStore
        .getEntityType(BDMOASDatastoreConstants.MARITAL_STATUS_INFO_ENTITY));

    if (maritalStatusEntities.length > 0) {
      final Entity maritalStatusEntity = maritalStatusEntities[0];

      final String isSeparated = maritalStatusEntity.getAttribute(
        BDMOASDatastoreConstants.MARITAL_STATUS_IS_SEPERATED_ATTR);

      if (!StringUtil.isNullOrEmpty(isSeparated)
        && isSeparated.equals(IEGYESNO.YES)) {
        final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();
        evidenceTypeKey.evidenceType =
          CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL;

        final GenericSLDataDetails genericDtls = new GenericSLDataDetails();

        final EvidenceKey evidenceKey = new EvidenceKey();
        evidenceKey.evidenceID = parentEvidenceID;
        evidenceKey.evType = CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP;

        genericDtls.addParent(CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP,
          evidenceKey);

        final EvidenceServiceInterface evidenceServiceInterface =
          EvidenceGenericSLFactory.instance(evidenceTypeKey,
            Date.getCurrentDate());

        final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
          DynamicEvidenceDataDetailsFactory
            .newInstance(evidenceTypeKey.evidenceType, Date.getCurrentDate());

        DynamicEvidenceTypeConverter.setAttribute(
          dynamicEvidencedataDetails.getAttribute(
            BDMOASEvidenceMappingConstants.kReasonForLivingApart),
          new CodeTableItem(BDMOASLIVINGAPARTREASON.TABLENAME,
            BDMOASLIVINGAPARTREASONEntry.NOT_SPECIFIED.getCode()));

        DynamicEvidenceTypeConverter.setAttribute(
          dynamicEvidencedataDetails.getAttribute(
            BDMOASEvidenceMappingConstants.klivingApartReasonDescription),
          BDMConstants.EMPTY_STRING);

        final String seperationDate = maritalStatusEntity
          .getAttribute(BDMOASDatastoreConstants.SEPERATION_DATE_ATTR);

        if (!StringUtil.isNullOrEmpty(seperationDate)) {
          DynamicEvidenceTypeConverter.setAttribute(
            dynamicEvidencedataDetails
              .getAttribute(BDMOASEvidenceMappingConstants.kStartDate),
            Date.getDate(seperationDate));
        } else {
          DynamicEvidenceTypeConverter.setAttribute(dynamicEvidencedataDetails
            .getAttribute(BDMOASEvidenceMappingConstants.kStartDate),
            new Date());
        }

        DynamicEvidenceTypeConverter.setAttribute(dynamicEvidencedataDetails
          .getAttribute(BDMOASEvidenceMappingConstants.kComments), "");

        final EvidenceDescriptorDetails descriptor =
          new EvidenceDescriptorDetails();
        descriptor.evidenceType = evidenceTypeKey.evidenceType;
        descriptor.caseID = caseID;
        descriptor.receivedDate = Date.getCurrentDate();
        descriptor.participantID = concernRoleID;
        descriptor.changeReason = EVIDENCECHANGEREASON.INITIAL;

        genericDtls.setData(dynamicEvidencedataDetails);
        genericDtls.setDescriptor(descriptor);
        genericDtls.setCaseIdKey(descriptor.caseID);

        evidenceServiceInterface.createEvidence(genericDtls);
      }
    }

  }

  /**
   * Map and consent and declaration evidence.
   *
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public void mapConsentDeclarationEvidence(final Entity applicationEntity,
    final long caseID, final DataStoreCEFMappings dataStoreCEFMappings,
    final String schemaName) throws AppException, InformationalException {

    final long concernRoleID =
      dataStoreCEFMappings.getPrimaryClient().getConcernRoleID();
    try {
      createConsentDeclarationEvidence(concernRoleID, caseID,
        applicationEntity, schemaName);
    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.BDM_CONSENT_DECLARATION);

    }

  }

  /**
   * Utility method to create Residence Period Evidence
   *
   * @param caseParticipantRoleID
   * @param caseID
   * @param applicationEntity
   * @param schemaName
   */
  private void createConsentDeclarationEvidence(final long concernRoleID,
    final long caseID, final Entity applicationEntity,
    final String schemaName) throws AppException, InformationalException {

    final Datastore dataStore = DatastoreHelper.openDatastore(schemaName);

    final Entity[] declarationEntities = applicationEntity.getChildEntities(
      dataStore.getEntityType(BDMOASDatastoreConstants.DECLARATION_ENTITY));

    if (declarationEntities != null && declarationEntities.length > 0) {
      final Entity declarationEntity =
        applicationEntity.getChildEntities(dataStore
          .getEntityType(BDMOASDatastoreConstants.DECLARATION_ENTITY))[0];

      boolean applicantSignedInd = false;
      boolean applicantSignedWithMarkInd = false;
      boolean partnerSignedInd = false;
      boolean partnerSignedWithMarkInd = false;

      final BDMOASConsentDeclarationEvidenceVO consentAndDeclarationVO =
        new BDMOASConsentDeclarationEvidenceVO();
      consentAndDeclarationVO.setComments(
        BDMOASEvidenceMappingConstants.kConsentAndDeclarationComments);
      consentAndDeclarationVO.setSignatureType(BDMSIGNATURETYPE.SIGNED);

      final String didApplicantSigned = declarationEntity
        .getAttribute(BDMOASDatastoreConstants.kDidAplicntSigned);

      final String didAplicntSignedWithMark = declarationEntity
        .getAttribute(BDMOASDatastoreConstants.kDidAplicntSignedWithMark);

      if (!StringUtil.isNullOrEmpty(didApplicantSigned)
        && IEGYESNO.YES.equals(didApplicantSigned)) {
        applicantSignedInd = true;
      } else if (!StringUtil.isNullOrEmpty(didAplicntSignedWithMark)
        && IEGYESNO.YES.equals(didAplicntSignedWithMark)) {
        applicantSignedWithMarkInd = true;
      }

      if (applicantSignedInd || applicantSignedWithMarkInd) {
        final String dateSignedStr = declarationEntity
          .getAttribute(BDMOASDatastoreConstants.kAplicntSignedDate);

        final Date dateSigned = StringUtil.isNullOrEmpty(dateSignedStr)
          ? Date.kZeroDate : Date.getDate(dateSignedStr);

        consentAndDeclarationVO.setDateSigned(dateSigned);
      }

      final String didSpouseOrCLPSigned = declarationEntity
        .getAttribute(BDMOASDatastoreConstants.kDidSpouseOrCLPSigned);

      final String didSpouseOrCLPSignedWithMark = declarationEntity
        .getAttribute(BDMOASDatastoreConstants.kDidSpouseOrCLPSignedWithMark);

      if (!StringUtil.isNullOrEmpty(didSpouseOrCLPSigned)
        && IEGYESNO.YES.equals(didSpouseOrCLPSigned)) {
        partnerSignedInd = true;
      } else if (!StringUtil.isNullOrEmpty(didSpouseOrCLPSignedWithMark)
        && IEGYESNO.YES.equals(didSpouseOrCLPSignedWithMark)) {
        partnerSignedWithMarkInd = true;
      }

      if (partnerSignedInd || partnerSignedWithMarkInd) {
        final String dateSpouseOrCLPSignedStr = declarationEntity
          .getAttribute(BDMOASDatastoreConstants.kSpouseOrCLPSignedDate);

        final Date dateSpouseOrCLPSigned =
          StringUtil.isNullOrEmpty(dateSpouseOrCLPSignedStr) ? Date.kZeroDate
            : Date.getDate(dateSpouseOrCLPSignedStr);

        consentAndDeclarationVO.setDateSigned(dateSpouseOrCLPSigned);
      }

      boolean tpSignedInd = false;
      boolean tpSignedBehalfOfAplcntInd = false;
      boolean tpSignedBehalfOfPartnrInd = false;

      final String didTPSignedAsWitness = declarationEntity
        .getAttribute(BDMOASDatastoreConstants.kDidTPSignedAsWitness);

      final String didTPSignedBehalfOfAplcnt = declarationEntity
        .getAttribute(BDMOASDatastoreConstants.kDidTPSignedBehalfOfAplcnt);

      if (!StringUtil.isNullOrEmpty(didTPSignedAsWitness)
        && IEGYESNO.YES.equals(didTPSignedAsWitness)) {
        tpSignedInd = true;
      }

      if (!StringUtil.isNullOrEmpty(didTPSignedBehalfOfAplcnt)
        && IEGYESNO.YES.equals(didTPSignedBehalfOfAplcnt)) {
        tpSignedBehalfOfAplcntInd = true;
      }

      if (tpSignedInd || tpSignedBehalfOfAplcntInd) {

        final String dateTPSignedStr = declarationEntity
          .getAttribute(BDMOASDatastoreConstants.kThirdPartySignedDate);

        final Date dateTPSigned = StringUtil.isNullOrEmpty(dateTPSignedStr)
          ? Date.kZeroDate : Date.getDate(dateTPSignedStr);

        consentAndDeclarationVO.setIsWitnessInd(true);

        consentAndDeclarationVO.setWitnessDateSigned(dateTPSigned);
      }

      final String didTPSignedBehalfOfSpouse = declarationEntity
        .getAttribute(BDMOASDatastoreConstants.kDidTPSignedBehalfOfSpouse);

      if (!StringUtil.isNullOrEmpty(didTPSignedBehalfOfSpouse)
        && IEGYESNO.YES.equals(didTPSignedBehalfOfSpouse)) {
        tpSignedBehalfOfPartnrInd = true;
        final String tpSignedDateBehalfOfSpouseStr = declarationEntity
          .getAttribute(BDMOASDatastoreConstants.kTpSignedDateBehalfOfSpouse);

        final Date tpSignedDateBehalfOfSpouse =
          StringUtil.isNullOrEmpty(tpSignedDateBehalfOfSpouseStr)
            ? Date.kZeroDate : Date.getDate(tpSignedDateBehalfOfSpouseStr);

        consentAndDeclarationVO.setIsWitnessInd(true);

        consentAndDeclarationVO
          .setWitnessDateSigned(tpSignedDateBehalfOfSpouse);
      }

      if (applicantSignedInd || applicantSignedWithMarkInd || tpSignedInd
        || tpSignedBehalfOfAplcntInd) {
        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(consentAndDeclarationVO);

        final CaseParticipantRoleNameDetails caseParticipantRoleNameDetails =
          this.getCaseParticipantRoleDetailsByType(caseID,
            CASEPARTICIPANTROLETYPE.PRIMARY);

        evidenceData.put(BDMOASEvidenceMappingConstants.kParticipant, String
          .valueOf(caseParticipantRoleNameDetails.caseParticipantRoleID));

        createDynamicEvidenceOnCase(
          caseParticipantRoleNameDetails.participantRoleID, evidenceData,
          CASEEVIDENCEEntry.BDM_CONSENT_DECLARATION,
          EVIDENCECHANGEREASON.INITIAL, caseID);

      } else if (partnerSignedInd || partnerSignedWithMarkInd
        || tpSignedBehalfOfPartnrInd) {
        final HashMap<String, String> evidenceData =
          new BDMLifeEventUtil().getEvidenceData(consentAndDeclarationVO);

        final CaseParticipantRoleNameDetails caseParticipantRoleNameDetails =
          this.getCaseParticipantRoleDetailsByType(caseID,
            CASEPARTICIPANTROLETYPE.MEMBER);

        if (caseParticipantRoleNameDetails.caseParticipantRoleID != BDMConstants.kZeroLongValue) {
          evidenceData.put(BDMOASEvidenceMappingConstants.kParticipant, String
            .valueOf(caseParticipantRoleNameDetails.caseParticipantRoleID));

          createDynamicEvidenceOnCase(
            caseParticipantRoleNameDetails.participantRoleID, evidenceData,
            CASEEVIDENCEEntry.BDM_CONSENT_DECLARATION,
            EVIDENCECHANGEREASON.INITIAL, caseID);
        }
      }
    }
  }

  /**
   * Process Address Evidence updates from entity
   *
   * @param personEntity
   * @param concernRole
   */
  public void processAddressEvd(final Entity personEntity,
    final curam.participant.impl.ConcernRole concernRole)
    throws AppException, InformationalException {

    try {

      final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();

      final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
        bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
          concernRole.getID(), PDCConst.PDCADDRESS);

      // filter out evidence which are enddated
      final List<DynamicEvidenceDataDetails> existingAddressList =
        evidenceDataDetailsList.stream()
          .filter(addressDtls -> addressDtls
            .getAttribute(BDMOASDatastoreConstants.kToDate).getValue()
            .isEmpty())
          .collect(Collectors.toList());

      final List<DynamicEvidenceDataDetails> existingAddressEvdDtls;
      final List<DynamicEvidenceDataDetails> existingMailingAddressEvdDtls;

      new ArrayList<BDMAddressEvidenceVO>();

      final BDMAddressEvidenceVO residentialDSAddress =
        new BDMAddressEvidenceVO();
      final BDMAddressEvidenceVO residentialEvdAddress =
        new BDMAddressEvidenceVO();

      // private address
      final String residentialAddressData = getAddress(personEntity);

      residentialDSAddress.setAddressData(residentialAddressData);

      final OtherAddressData otherAddressDataObj = new OtherAddressData();
      otherAddressDataObj.addressData = residentialAddressData;
      final String country = new BDMUtil().getAddressDetails(
        otherAddressDataObj, BDMConstants.kADDRESSELEMENT_COUNTRY);

      existingAddressEvdDtls = !existingAddressList.isEmpty()
        ? existingAddressList.stream()
          .filter(addressEvdDtls -> addressEvdDtls
            .getAttribute(BDMOASDatastoreConstants.kAddressType).getValue()
            .equals(CONCERNROLEADDRESSTYPE.PRIVATE))
          .collect(Collectors.toList())
        : new ArrayList<DynamicEvidenceDataDetails>();

      if (!existingAddressEvdDtls.isEmpty()) {
        new BDMLifeEventUtil().setEvidenceData(residentialEvdAddress,
          existingAddressEvdDtls.get(0));
        if (0 != residentialDSAddress.compareTo(residentialEvdAddress)) {
          // if there is an existing address modify it
          // BEGIN TASK-28473 Change Reason Field Is missing on Edit Evidence
          // added Country to create a task if country changes .
          // 101950 Updated: 247.3-202 Trigger task to update residency period
          bdmEvidenceUtil.modifyAddressEvidence(concernRole.getID(),
            residentialAddressData, Date.getCurrentDate(),
            CONCERNROLEADDRESSTYPE.PRIVATE, Date.kZeroDate,
            residentialEvdAddress.isPreferredInd(), "",
            residentialEvdAddress.getEvidenceID(), null, CuramConst.gkEmpty,
            CuramConst.gkEmpty, CuramConst.gkEmpty, country);
          // if required change null to valid change reason default is set to
          // reported by client
          // END TASK-28473
        }
      }

      final String isPrimaryParticipant = personEntity
        .getAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT);

      if (!StringUtil.isNullOrEmpty(isPrimaryParticipant)
        && isPrimaryParticipant.equals("true")) {
        final BDMAddressEvidenceVO mailingDSAddress =
          new BDMAddressEvidenceVO();
        final BDMAddressEvidenceVO mailingEvdAddress =
          new BDMAddressEvidenceVO();

        String mailingAddressData;
        // if same mailing address is not the same as residential
        if (personEntity
          .getTypedAttribute(
            BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT)
          .equals(IEGYESNO.YES)) {
          // set to application input mailing address
          mailingAddressData = getMailingAddress(personEntity);
        } else {
          // set to residential
          mailingAddressData = residentialAddressData;
        }

        mailingDSAddress.setAddressData(mailingAddressData);

        existingMailingAddressEvdDtls = !existingAddressList.isEmpty()
          ? existingAddressList.stream()
            .filter(addressEvdDtls -> addressEvdDtls
              .getAttribute(BDMOASDatastoreConstants.kAddressType).getValue()
              .equals(CONCERNROLEADDRESSTYPE.MAILING))
            .collect(Collectors.toList())
          : new ArrayList<DynamicEvidenceDataDetails>();
        // Begin Task-61496 bug fix 61322
        // is new mailing address is entered
        final Boolean isMailingAddressUpdated = personEntity
          .getTypedAttribute(BDMDatastoreConstants.IS_MAILING_ADDRESS_UPDATE)
          .equals(IEGYESNO.YES);

        if (!existingMailingAddressEvdDtls.isEmpty()) {
          new BDMLifeEventUtil().setEvidenceData(mailingEvdAddress,
            existingMailingAddressEvdDtls.get(0));

          if (isMailingAddressUpdated) {

            final Date endDate = Date.getCurrentDate().addDays(-1);
            final long evidenceID =
              existingMailingAddressEvdDtls.get(0).getID();

            // end date the current existing identification evidence
            BDMEvidenceUtil.endDateEvidence(evidenceID, endDate,
              PDCConst.PDCADDRESS, EVIDENCECHANGEREASON.REPORTEDBYCLIENT);

            // create a new address evidence
            new BDMEvidenceUtil().createAddressEvidence(concernRole.getID(),
              mailingAddressData, Date.getCurrentDate(),
              CONCERNROLEADDRESSTYPE.MAILING, Date.kZeroDate, false, "");

          } else {
            // End Task-61496
            if (0 != mailingDSAddress.compareTo(mailingEvdAddress)) {
              // if there is an existing address modify it
              // BEGIN TASK-28473 Change Reason Field Is missing on Edit
              // Evidence
              bdmEvidenceUtil.modifyAddressEvidence(concernRole.getID(),
                mailingAddressData, Date.getCurrentDate(),
                CONCERNROLEADDRESSTYPE.MAILING, Date.kZeroDate,
                mailingEvdAddress.isPreferredInd(), "",
                mailingEvdAddress.getEvidenceID(), null, CuramConst.gkEmpty,
                CuramConst.gkEmpty, CuramConst.gkEmpty, CuramConst.gkEmpty);
              // if required change null to valid change reason default is set
              // to
              // reported by client.
              // END TASK-28473
            }
          }
        }
      }
    } catch (final Exception e) {
      logEvidenceException(concernRole.getID(), e,
        CASEEVIDENCEEntry.BDMADDRESS);
    }

  }

  /**
   * Get Residential Address String
   *
   * @param personEntity
   * @param entityType
   */
  String getAddress(final Entity personEntity)
    throws AppException, InformationalException {

    String address = "";

    final Entity addressEntity;

    final Entity resAddrEntity = BDMApplicationEventsUtil.retrieveChildEntity(
      personEntity, BDMOASDatastoreConstants.kResidentialAddress);
    if (!resAddrEntity.getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY)
      .equals(COUNTRYEntry.CA.getCode())) {
      addressEntity =
        getAddress(personEntity, BDMOASDatastoreConstants.kIntlResAddress);

    } else {
      addressEntity = getAddress(personEntity,
        BDMOASDatastoreConstants.kResidentialAddress);
    }

    if (null != addressEntity) {
      final Map<String, Object> fieldMap =
        DatastoreHelper.convertEntityToFieldMap(addressEntity);
      address = this.addressMappingStrategy.getAddressData(fieldMap);
    }
    if (!StringHelper.isEmpty(address))
      return address;
    final AddressData addressData = AddressDataFactory.newInstance();
    final LocaleStruct localeStruct = new LocaleStruct();
    final LayoutKey layoutKey = addressData.getLayoutForLocale(localeStruct);
    final OtherAddressData otherAddressData =
      addressData.getDefaultAddressDataForLayout(layoutKey);
    return otherAddressData.addressData;
  }

  /**
   * Get Mailing Address String
   *
   * @param personEntity
   * @param entityType
   */
  String getMailingAddress(final Entity personEntity)
    throws AppException, InformationalException {

    String address = "";
    final Entity addressEntity =
      getAddress(personEntity, BDMOASDatastoreConstants.kMailingAddress);
    if (null != addressEntity) {
      final Map<String, Object> fieldMap =
        DatastoreHelper.convertEntityToFieldMap(addressEntity);
      address = this.addressMappingStrategy.getAddressData(fieldMap);
    }
    return address;
  }

  /**
   * Get Address Entity
   *
   * @param personEntity
   * @param entityType
   */
  Entity getAddress(final Entity personEntity, final String entityType) {

    final Datastore dataStore = personEntity.getDatastore();
    final EntityType addressType = dataStore.getEntityType(entityType);
    if (addressType == null)
      return null;
    final Entity[] addresses = personEntity.getChildEntities(addressType);
    if (addresses.length == 0)
      return null;
    return addresses[0];
  }

  /**
   * Map and create Identifications Evidence
   *
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public static void mapIdentificationsEvidence(final long concernRoleID,
    final String alternateID, final String country)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    try {

      createIdentificationsEvidence(pdcCaseIDCaseParticipantRoleID.caseID,
        pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID, concernRoleID,
        alternateID, country, EVIDENCECHANGEREASON.INITIAL);

    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e,
        CASEEVIDENCEEntry.IDENTIFICATIONS);
    }

  }

  private static void createIdentificationsEvidence(final long caseID,
    final long cprID, final long concernroleID, final String alternateID,
    final String country, final String evidenceChangeReason)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.IDENTIFICATIONS;

    final Date fromDate = Date.getCurrentDate();

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("participant"), cprID);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("altIDType"),
      new CodeTableItem(CONCERNROLEALTERNATEID.TABLENAME,
        CONCERNROLEALTERNATEIDEntry.BDM_FOREIGN_IDENTIFIER.getCode()));
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("alternateID"), alternateID);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("country"),
      new CodeTableItem(BDMSOURCECOUNTRY.TABLENAME, country));
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("bdmReceivedFrom"),
      new CodeTableItem(BDMRECEIVEDFROM.TABLENAME,
        BDMRECEIVEDFROMEntry.CLIENT_REPORTED.getCode()));
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("fromDate"), fromDate);

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;
    descriptor.changeReason = evidenceChangeReason;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(cprID);
    genericDtls.addRelCp("participant", cpAdaptor);

    evidenceServiceInterface.createEvidence(genericDtls);
  }

  /**
   * Map and create Names Evidence
   *
   *
   * @param personEntites
   * @param caseID
   * @param dataStoreCEFMappings
   */
  public static void mapNamesEvidence(final long concernRoleID,
    final String firstName, final String lastName)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    try {

      createNamesEvidence(pdcCaseIDCaseParticipantRoleID.caseID,
        pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID, concernRoleID,
        firstName, lastName, EVIDENCECHANGEREASON.INITIAL);

    } catch (final Exception e) {
      logEvidenceException(concernRoleID, e, CASEEVIDENCEEntry.NAMES);
    }

  }

  private static void createNamesEvidence(final long caseID, final long cprID,
    final long concernroleID, final String firstName, final String lastName,
    final String evidenceChangeReason)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.NAMES;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("participant"), cprID);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("nameType"), new CodeTableItem(
        ALTERNATENAMETYPE.TABLENAME, ALTERNATENAMETYPEEntry.ALIAS.getCode()));
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("firstName"), firstName);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute("lastName"), lastName);

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;
    descriptor.changeReason = evidenceChangeReason;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(cprID);
    genericDtls.addRelCp("participant", cpAdaptor);

    evidenceServiceInterface.createEvidence(genericDtls);
  }

  /**
   * Method to get the case participant role details
   *
   * @param caseID
   * @return caseParticipantRoleID
   */
  public CaseParticipantRoleNameDetails getCaseParticipantRoleDetailsByType(
    final long caseID, final String typeCode)
    throws AppException, InformationalException {

    CaseParticipantRoleNameDetails caseParticipantRoleNameDetails =
      new CaseParticipantRoleNameDetails();
    final CaseIDTypeAndStatusKey caseIDTypeAndStatusKey =
      new CaseIDTypeAndStatusKey();
    caseIDTypeAndStatusKey.key.caseID = caseID;
    caseIDTypeAndStatusKey.key.recordStatus = RECORDSTATUS.NORMAL;
    caseIDTypeAndStatusKey.key.typeCode = typeCode;
    final CaseParticipantRoleIDAndNameDtlsList casePtcptRoleNameList =
      CaseParticipantRoleFactory.newInstance()
        .listCaseParticipantRolesByTypeCaseIDAndStatus(
          caseIDTypeAndStatusKey);

    if (casePtcptRoleNameList.dtls.dtls.size() > 0) {
      caseParticipantRoleNameDetails = casePtcptRoleNameList.dtls.dtls.get(0);
    }

    return caseParticipantRoleNameDetails;
  }

  /**
   * Creates dynamic evidence on AC or IC
   *
   * @param participantRoleID
   *
   * @param evidenceData
   */
  public void createDynamicEvidenceOnCase(final long participantRoleID,
    final HashMap<String, String> evidenceData,
    final CASEEVIDENCEEntry evidenceType, final String evidenceChangeReason,
    final long caseID) throws AppException, InformationalException {

    final DynamicEvidenceMaintenance dynObj =
      DynamicEvidenceMaintenanceFactory.newInstance();
    final DynamicEvidenceData dynamicEvidenceData = new DynamicEvidenceData();
    final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

    dynamicEvidenceData.data =
      BDMEvidenceUtil.setDynamicEvidenceDetailsByEvidenceType(evidenceData,
        evidenceType.getCode());
    dynamicEvidenceData.caseIDKey.caseID = caseID;
    dynamicEvidenceData.descriptor.evidenceType = evidenceType.getCode();
    dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
    dynamicEvidenceData.descriptor.participantID = participantRoleID;
    dynamicEvidenceData.descriptor.changeReason = evidenceChangeReason;
    dynMod.effectiveDateUsed =
      BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

    // create the evidence
    final ReturnEvidenceDetails returnEvidenceDetails =
      dynObj.createEvidence(dynamicEvidenceData, dynMod);
    // OOTB code does not update the Change reason even if we pass the change
    // reason to OOTB Code. Therefore this code is written to update the change
    // reason.
    updateChangeReasonAndParticipant(evidenceType.getCode(),
      evidenceChangeReason, returnEvidenceDetails, participantRoleID);

  }
}
