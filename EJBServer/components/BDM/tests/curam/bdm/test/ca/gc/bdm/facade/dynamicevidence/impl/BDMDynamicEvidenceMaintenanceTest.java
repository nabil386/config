/**
 *
 */
package curam.bdm.test.ca.gc.bdm.facade.dynamicevidence.impl;

import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.MARITALSTATUS;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.CaseHeader;
import curam.core.intf.PersonRegistration;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.sl.struct.CaseIDAndParticipantRoleIDKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceCaseKey;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.sl.struct.SearchCaseParticipantDetailsList;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.PersonDtls;
import curam.core.struct.RegistrationIDDetails;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.facade.struct.EvidenceCPInfo;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

/**
 *
 * Test cases for BDMConcernRoleAttachmentLink class
 *
 * @author jalpa.patel
 *
 */

@RunWith(JMockit.class)
public class BDMDynamicEvidenceMaintenanceTest extends CuramServerTestJUnit4 {

  curam.ca.gc.bdm.sl.attachment.intf.BDMConcernRoleAttachment bdmConcernrRoleAttachmentObj;

  curam.ca.gc.bdm.facade.communication.intf.BDMConcernRoleAttachmentLink bdmConcernRoleAttachmentLinkObj;

  curam.ca.gc.bdm.facade.dynamicevidence.intf.BDMDynamicEvidenceMaintenance dynamicEvidenceobj;

  @Before
  public void setUp() throws AppException, InformationalException {

    super.setUpCuramServerTest();

    dynamicEvidenceobj =
      curam.ca.gc.bdm.facade.dynamicevidence.fact.BDMDynamicEvidenceMaintenanceFactory
        .newInstance();

  }

  private static final String CASE_PARTICIPANT_ROLE = "caseParticipantRoleID";

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  private static final String MARITAL_STATUS = "maritalStatus";

  /** Test Create concernRole attachment */

  @Test
  public void testSearchCaseParticipantDetailsList()
    throws AppException, InformationalException {

    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;
    final CaseIDAndParticipantRoleIDKey cprKey =
      new CaseIDAndParticipantRoleIDKey();
    cprKey.caseID = pdcID;
    cprKey.participantRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseParticipantRole cprObj =
      CaseParticipantRoleFactory.newInstance();
    final CaseParticipantRoleIDStruct cprStruct =
      cprObj.readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(cprKey);
    final Long cprID = cprStruct.caseParticipantRoleID;
    final curam.core.sl.struct.EvidenceKey origPersonMaritalKey =
      createMaritalStatusEvidence(pdcID, cprID,
        pdcOriginalPersonDetails.concernRoleID, Date.fromISO8601("20230101"),
        Date.fromISO8601("20230102"));
    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;
    final EvidenceCaseKey key = new EvidenceCaseKey();
    key.caseIDKey.caseID = pdcID;
    key.evidenceKey.evidenceID = origPersonMaritalKey.evidenceID;
    key.evidenceKey.evType = eType.evidenceType;
    final EvidenceCPInfo info = new EvidenceCPInfo();
    info.relCpName = "Hello wlorld";
    info.date = "2022-01-01";
    info.relEmpName = "Test";

    final SearchCaseParticipantDetailsList readBDMConcernRoleAttachmentDetails =
      dynamicEvidenceobj.listCaseParticipant(key, info);

    assertTrue(pdcID != 0);
    assertTrue(origPersonMaritalKey.evidenceID != 0);

  }

  private curam.core.sl.struct.EvidenceKey createMaritalStatusEvidence(
    final Long caseID, final Long cprID, final long concernroleID,
    final Date startDate, final Date endDate)
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
      dynamicEvidencedataDetails.getAttribute(CASE_PARTICIPANT_ROLE), cprID);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(START_DATE), startDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(END_DATE), endDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(MARITAL_STATUS),
      new CodeTableItem(BDMMARITALSTATUS.TABLENAME, MARITALSTATUS.MARRIED));

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(cprID);
    genericDtls.addRelCp(CASE_PARTICIPANT_ROLE, cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

  public PDCPersonDetails createOriginalPDCPerson()
    throws AppException, InformationalException {

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    personRegistrationDetails.firstForename = "Original Person";
    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();
    pdcPersonDetails.assign(personRegistrationDetails);
    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

}
