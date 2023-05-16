package curam.ca.gc.bdmoas.test.util.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.util.impl.BDMOASEvidenceUtil;
import curam.codetable.CASECATTYPECODE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.ConcernRoleIDStatusCodeKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.struct.CaseParticipantRoleNameDetails;
import curam.core.sl.entity.struct.ParticipantRoleIDTypeCodesStatusKey;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.CaseIDParticipantIDStatusCode;
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
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.sl.struct.CaseIDTypeAndStatusKey;
import curam.core.sl.struct.CaseParticipantRoleIDAndNameDtlsList;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseHeaderKeyList;
import curam.dynamicevidence.facade.fact.DynamicEvidenceMaintenanceFactory;
import curam.dynamicevidence.facade.struct.DynEvdModifyDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.pdc.impl.PDCUtil;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.impl.ProcessInstanceAdmin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The Class tests {@link BDMOASTaskUtilTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class BDMOASEvidenceUtilTest extends BDMOASCaseTest {

  private static final long CASEID = 101l;

  private static final long CONCERNROLEID = 102l;

  private static final long CASEPARTICIPANTTOLEID = 103l;

  private static final String CORRECTIONSETID = "114";

  private static final long SUCCESSIONID = 113l;

  private static final long EVIDENCEDESCRIPTORID = 112l;

  private static final long EVIDENCEID = 111l;

  private static final long TASKID = 115l;

  private static final long RELATEDID = 116l;

  private static final String PARTICIPANTNAME = "Junit Lee";

  @Tested
  BDMOASEvidenceUtil bdmOASEvidenceUtil;

  @Mocked
  CaseParticipantRoleFactory caseParticipantRoleFactory;

  @Mocked
  DynamicEvidenceMaintenanceFactory dynamicEvidenceMaintenanceFactory;

  @Mocked
  curam.core.sl.fact.CaseParticipantRoleFactory caseParticipantRoleFactorySL;

  @Mocked
  CaseHeaderFactory caseHeaderFactory;

  @Mocked
  curam.core.facade.fact.CaseHeaderFactory caseHeaderFactoryFacade;

  @Mocked
  EvidenceDescriptorFactory evidenceDescriptorFactory;

  @Mocked
  curam.core.sl.infrastructure.impl.EvidenceController evidenceController;

  @Mocked
  BizObjAssociationFactory bizObjAssociationFactory;

  @Mocked
  TaskAdminFactory taskAdminFactory;

  @Mocked
  ProcessInstanceAdmin processInstanceAdmin;

  @Mocked
  curam.util.events.impl.EventService eventService;

  @Mocked
  EnactmentService enactmentService;

  @Mocked
  EvidenceControllerFactory evidenceControllerFactory;

  @Mocked
  ConcernRoleFactory concernRoleFactory;

  @Mocked
  PDCUtil pdcUtil;

  @Mocked
  curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil bdmEvidenceUtil;

  @Mocked
  curam.ca.gc.bdm.util.impl.BDMDateUtil bdmDateUtil;

  @Mocked
  DynamicEvidenceDataDetailsFactory dynamicEvidenceDataDetailsFactory;

  public BDMOASEvidenceUtilTest() {

    super();
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  @Test
  public void testReadCaseIDsForActivePriAndMemRolesByParticipantRoleID()
    throws AppException, InformationalException {

    new Expectations() {

      CaseHeaderKeyList caseHeaderKeyList = new CaseHeaderKeyList();

      {
        final CaseHeaderKey e = new CaseHeaderKey();
        e.caseID = CASEID;
        caseHeaderKeyList.dtls.add(e);
        caseParticipantRoleFactory.searchCaseIDByPRIDTypeCodesAndStatus(
          (ParticipantRoleIDTypeCodesStatusKey) any);
        result = caseHeaderKeyList;
      }
    };

    final CaseHeaderKeyList caseHeaderKeyList = BDMOASEvidenceUtil
      .readCaseIDsForActivePriAndMemRolesByParticipantRoleID(CONCERNROLEID);
    assertEquals(CuramConst.gkOne, caseHeaderKeyList.dtls.size());
  }

  @Test
  public void
    testReadOpenApplicationAndIntegratedCasesByConcernRoleIDAPPLICATION_CASE()
      throws AppException, InformationalException {

    new Expectations() {

      CaseHeaderKeyList caseHeaderKeyList = new CaseHeaderKeyList();

      CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();
      {
        final CaseHeaderKey e = new CaseHeaderKey();
        e.caseID = CASEID;
        caseHeaderKeyList.dtls.add(e);
        caseParticipantRoleFactory.searchCaseIDByPRIDTypeCodesAndStatus(
          (ParticipantRoleIDTypeCodesStatusKey) any);
        result = caseHeaderKeyList;

        caseHeaderDtls.caseTypeCode = CASETYPECODE.APPLICATION_CASE;
        caseHeaderDtls.statusCode = CASESTATUS.OPEN;
        caseHeaderFactory.read(e);
        result = caseHeaderDtls;
      }
    };

    final List<CaseHeaderDtls> caseHeaderDtlsList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(CONCERNROLEID);
    assertEquals(CuramConst.gkOne, caseHeaderDtlsList.size());
  }

  @Test
  public void
    testReadOpenApplicationAndIntegratedCasesByConcernRoleIDINTEGRATEDCASE()
      throws AppException, InformationalException {

    new Expectations() {

      CaseHeaderKeyList caseHeaderKeyList = new CaseHeaderKeyList();

      CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();
      {
        final CaseHeaderKey e = new CaseHeaderKey();
        e.caseID = CASEID;
        caseHeaderKeyList.dtls.add(e);
        caseParticipantRoleFactory.searchCaseIDByPRIDTypeCodesAndStatus(
          (ParticipantRoleIDTypeCodesStatusKey) any);
        result = caseHeaderKeyList;

        caseHeaderDtls.caseTypeCode = CASETYPECODE.INTEGRATEDCASE;
        caseHeaderDtls.integratedCaseType =
          CASECATTYPECODE.OAS_OLD_AGE_SECURITY;
        caseHeaderDtls.statusCode = CASESTATUS.OPEN;
        caseHeaderFactory.read(e);
        result = caseHeaderDtls;
      }
    };

    final List<CaseHeaderDtls> caseHeaderDtlsList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(CONCERNROLEID);
    assertEquals(CuramConst.gkOne, caseHeaderDtlsList.size());
  }

  @Test
  public void testIsDateRangeOverlap()
    throws AppException, InformationalException {

    final Boolean isDateRangeOverlap =
      BDMOASEvidenceUtil.isDateRangeOverlap(getYesterday(), getToday(),
        getTomorrow(), getTomorrow().addDays(CuramConst.gkOne));
    assertFalse(isDateRangeOverlap);
  }

  @Test
  public void testIsDateRangeOverlapTRUE()
    throws AppException, InformationalException {

    final Boolean isDateRangeOverlap = BDMOASEvidenceUtil.isDateRangeOverlap(
      getYesterday(), getToday(), getYesterday(), getToday());
    assertTrue(isDateRangeOverlap);
  }

  @Test
  public void testIsDateLiesBetweenTwoDatesInclusive()
    throws AppException, InformationalException {

    final Boolean isDateLiesBetweenTwoDatesInclusive =
      BDMOASEvidenceUtil.isDateLiesBetweenTwoDatesInclusive(getToday(),
        getYesterday(), getToday());
    assertTrue(isDateLiesBetweenTwoDatesInclusive);
  }

  @Test
  public void testIsDateLiesBetweenTwoDatesInclusiveFALSE()
    throws AppException, InformationalException {

    final Boolean isDateLiesBetweenTwoDatesInclusive =
      BDMOASEvidenceUtil.isDateLiesBetweenTwoDatesInclusive(getYesterday(),
        getToday(), Date.kZeroDate);
    assertFalse(isDateLiesBetweenTwoDatesInclusive);
  }

  @Test
  public void testCreateACDynamicEvidence()
    throws AppException, InformationalException {

    new Expectations() {

      CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleIDAndNameDtlsList =
        new CaseParticipantRoleIDAndNameDtlsList();

      String data = "Test: data";

      String effectiveDateUsed = "Test: effectiveDateUsed";

      ReturnEvidenceDetails returnEvidenceDetails =
        new ReturnEvidenceDetails();

      {
        final CaseParticipantRoleNameDetails caseParticipantRoleNameDetails =
          new CaseParticipantRoleNameDetails();
        caseParticipantRoleNameDetails.caseParticipantRoleID =
          CASEPARTICIPANTTOLEID;
        caseParticipantRoleIDAndNameDtlsList.dtls.dtls
          .add(caseParticipantRoleNameDetails);
        caseParticipantRoleFactorySL
          .listCaseParticipantRolesByTypeCaseIDAndStatus(
            (CaseIDTypeAndStatusKey) any);
        result = caseParticipantRoleIDAndNameDtlsList;

        BDMEvidenceUtil.setDynamicEvidenceDetailsByEvidenceType(
          (HashMap<String, String>) any, (String) any);
        result = data;

        BDMDateUtil.dateFormatter((String) any);
        result = effectiveDateUsed;

        returnEvidenceDetails.evidenceKey.evidenceID = 124l;
        dynamicEvidenceMaintenanceFactory.createEvidence(
          (curam.dynamicevidence.facade.struct.DynamicEvidenceData) any,
          (DynEvdModifyDetails) any);
        result = returnEvidenceDetails;
      }
    };

    final HashMap<String, String> evidenceData =
      new HashMap<String, String>();
    final ReturnEvidenceDetails returnEvidenceDetails =
      BDMOASEvidenceUtil.createACDynamicEvidence(CONCERNROLEID, evidenceData,
        CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP,
        EVIDENCECHANGEREASON.CORRECTION, CASEID);
    assertEquals(124l, returnEvidenceDetails.evidenceKey.evidenceID);
  }

  @Test
  public void testCreateACDynamicEvidenceNOCASEID()
    throws AppException, InformationalException {

    new Expectations() {

      CaseParticipantRoleIDAndNameDtlsList caseParticipantRoleIDAndNameDtlsList =
        new CaseParticipantRoleIDAndNameDtlsList();

      String data = "Test: data";

      String effectiveDateUsed = "Test: effectiveDateUsed";

      ReturnEvidenceDetails returnEvidenceDetails =
        new ReturnEvidenceDetails();

      CaseHeaderDtlsList caseheaderDtlsList = new CaseHeaderDtlsList();

      {
        final CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();
        caseHeaderDtls.caseID = CASEID;
        caseHeaderDtls.statusCode = CASESTATUS.OPEN;
        caseHeaderDtls.caseTypeCode = CASETYPECODE.APPLICATION_CASE;
        caseheaderDtlsList.dtlsList.dtls.add(caseHeaderDtls);
        caseHeaderFactoryFacade
          .searchByConcernRoleID((ConcernRoleIDStatusCodeKey) any);
        result = caseheaderDtlsList;

        final CaseParticipantRoleNameDetails caseParticipantRoleNameDetails =
          new CaseParticipantRoleNameDetails();
        caseParticipantRoleNameDetails.caseParticipantRoleID =
          CASEPARTICIPANTTOLEID;
        caseParticipantRoleIDAndNameDtlsList.dtls.dtls
          .add(caseParticipantRoleNameDetails);
        caseParticipantRoleFactorySL
          .listCaseParticipantRolesByTypeCaseIDAndStatus(
            (CaseIDTypeAndStatusKey) any);
        result = caseParticipantRoleIDAndNameDtlsList;

        BDMEvidenceUtil.setDynamicEvidenceDetailsByEvidenceType(
          (HashMap<String, String>) any, (String) any);
        result = data;

        BDMDateUtil.dateFormatter((String) any);
        result = effectiveDateUsed;

        returnEvidenceDetails.evidenceKey.evidenceID = 124l;
        dynamicEvidenceMaintenanceFactory.createEvidence(
          (curam.dynamicevidence.facade.struct.DynamicEvidenceData) any,
          (DynEvdModifyDetails) any);
        result = returnEvidenceDetails;
      }
    };

    final HashMap<String, String> evidenceData =
      new HashMap<String, String>();
    final ReturnEvidenceDetails returnEvidenceDetails =
      BDMOASEvidenceUtil.createACDynamicEvidence(CONCERNROLEID, evidenceData,
        CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS, EVIDENCECHANGEREASON.CORRECTION,
        0l);
    assertEquals(124l, returnEvidenceDetails.evidenceKey.evidenceID);
  }

  @Test
  public void testCreateEvidence()
    throws AppException, InformationalException {

    new Expectations() {

      EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();

      {
        eiEvidenceKey.evidenceID = EVIDENCEID;
        evidenceControllerFactory.insertEvidence((EIEvidenceInsertDtls) any);
        result = eiEvidenceKey;
      }
    };

    final Map<String, String> attributes = new HashMap<String, String>();
    final EIEvidenceKey eIEvidenceKey =
      BDMOASEvidenceUtil.createEvidence(CASEID, CONCERNROLEID,
        CASEEVIDENCEEntry.BIRTH_AND_DEATH_DETAILS, attributes, getToday());
    assertEquals(EVIDENCEID, eIEvidenceKey.evidenceID);
  }

  @Test
  public void testGetParticipantIDForEvidence()
    throws AppException, InformationalException {

    new Expectations() {

      EvidenceDescriptorDtls descriptorDtls = new EvidenceDescriptorDtls();

      {
        descriptorDtls.participantID = CONCERNROLEID;
        evidenceControllerFactory.readEvidenceDescriptorByRelatedIDAndType(
          (RelatedIDAndEvidenceTypeKey) any);
        result = descriptorDtls;
      }
    };

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = EVIDENCEID;
    eiEvidenceKey.evidenceType = CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS;
    final long participantID =
      BDMOASEvidenceUtil.getParticipantIDForEvidence(eiEvidenceKey);
    assertEquals(CONCERNROLEID, participantID);
  }

  @Test
  public void
    testReadActiveEvidenceDescriptorListByCaseIDParticipantIDEvidenceType()
      throws AppException, InformationalException {

    new Expectations() {

      EvidenceDescriptorDtlsList allActiveEvdList =
        new EvidenceDescriptorDtlsList();

      {
        final EvidenceDescriptorDtls evidenceDescriptorDtls =
          new EvidenceDescriptorDtls();
        evidenceDescriptorDtls.evidenceDescriptorID = EVIDENCEDESCRIPTORID;
        evidenceDescriptorDtls.evidenceType =
          CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS;
        allActiveEvdList.dtls.add(evidenceDescriptorDtls);
        evidenceDescriptorFactory.searchActiveByCaseIDParticipantID(
          (CaseIDParticipantIDStatusCode) any);
        result = allActiveEvdList;
      }
    };

    final List<EvidenceDescriptorDtls> evidenceDescriptorDtlsList =
      BDMOASEvidenceUtil
        .readActiveEvidenceDescriptorListByCaseIDParticipantIDEvidenceType(
          CASEID, CASEPARTICIPANTTOLEID,
          CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS);
    assertEquals(CuramConst.gkOne, evidenceDescriptorDtlsList.size());
  }

  @Test
  public void testIsLegalStatusVerificationRequiredTRUE()
    throws AppException, InformationalException {

    new Expectations() {

      EvidenceDescriptorDtls targetEvidenceDescriptorDtls =
        new EvidenceDescriptorDtls();

      {
        evidenceDescriptorFactory.read((EvidenceDescriptorKey) any);
        result = targetEvidenceDescriptorDtls;
      }
    };

    final Boolean isLegalStatusVerificationRequired = BDMOASEvidenceUtil
      .isLegalStatusVerificationRequired(EVIDENCEDESCRIPTORID);
    assertTrue(isLegalStatusVerificationRequired);
  }

  @Test
  public void testIsLegalStatusVerificationRequiredTRUE2()
    throws AppException, InformationalException {

    new Expectations() {

      EvidenceDescriptorDtls targetEvidenceDescriptorDtls =
        new EvidenceDescriptorDtls();

      EvidenceDescriptorDtlsList sourceEvidenceDescriptorList =
        new EvidenceDescriptorDtlsList();

      EIEvidenceReadDtls sourceEvidenceReadDtls = new EIEvidenceReadDtls();

      {
        targetEvidenceDescriptorDtls.sharedInstanceID = 131l;
        targetEvidenceDescriptorDtls.relatedID = EVIDENCEID;
        targetEvidenceDescriptorDtls.evidenceType =
          CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS;
        targetEvidenceDescriptorDtls.sourceCaseID = CASEID;
        evidenceDescriptorFactory.read((EvidenceDescriptorKey) any);
        result = targetEvidenceDescriptorDtls;

        evidenceDescriptorFactory.searchBySharedInstanceIDCaseIDStatusCode(
          (SharedInstanceIDCaseIDStatusCode) any);
        result = sourceEvidenceDescriptorList;

      }
    };

    final Boolean isLegalStatusVerificationRequired = BDMOASEvidenceUtil
      .isLegalStatusVerificationRequired(EVIDENCEDESCRIPTORID);
    assertTrue(isLegalStatusVerificationRequired);
  }

  @Test
  public void testReadActiveEvidenceRecordDataForCorrectionSetIDEMPTY()
    throws AppException, InformationalException {

    new Expectations() {

      EvidenceDescriptorIDRelatedIDAndEvidenceTypeList edList =
        new EvidenceDescriptorIDRelatedIDAndEvidenceTypeList();

      {
        evidenceDescriptorFactory.searchByCorrectionSetIDAndStatus(
          (curam.core.sl.infrastructure.entity.struct.CorrectionSetIDAndStatusKey) any);
        result = edList;
      }
    };

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      BDMOASEvidenceUtil
        .readActiveEvidenceRecordDataForCorrectionSetID("131");
    // assertEquals(CuramConst.gkOne, caseHeaderKeyList.dtls.size());
  }

  @Test
  public void testReadActiveEvidenceRecordDataForCorrectionSetIDNOTEMPTY()
    throws AppException, InformationalException {

    new Expectations() {

      EvidenceDescriptorIDRelatedIDAndEvidenceTypeList edList =
        new EvidenceDescriptorIDRelatedIDAndEvidenceTypeList();

      EIEvidenceReadDtls evidenceReadDtls = new EIEvidenceReadDtls();

      {
        final EvidenceDescriptorIDRelatedIDAndEvidenceType evidenceDescriptorIDRelatedIDAndEvidenceType =
          new EvidenceDescriptorIDRelatedIDAndEvidenceType();
        evidenceDescriptorIDRelatedIDAndEvidenceType.relatedID = EVIDENCEID;
        evidenceDescriptorIDRelatedIDAndEvidenceType.evidenceType =
          CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS;
        edList.dtls.add(evidenceDescriptorIDRelatedIDAndEvidenceType);
        evidenceDescriptorFactory.searchByCorrectionSetIDAndStatus(
          (curam.core.sl.infrastructure.entity.struct.CorrectionSetIDAndStatusKey) any);
        result = edList;

        evidenceReadDtls.evidenceObject = new String("Test: evidenceObject");
        evidenceController.readEvidence((EIEvidenceKey) any);
        result = evidenceReadDtls;
        times = 0;

      }
    };

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      BDMOASEvidenceUtil
        .readActiveEvidenceRecordDataForCorrectionSetID("131");
    // assertEquals(CuramConst.gkOne, caseHeaderKeyList.dtls.size());
  }
}
