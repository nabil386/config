package curam.ca.gc.bdm.test.facade.person.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMSearchByTrackingNumberKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNum;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultByTrackingNumList;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.person.fact.BDMClientMergeFactory;
import curam.ca.gc.bdm.facade.person.intf.BDMClientMerge;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonMergeValidations;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchDetailsResult;
import curam.ca.gc.bdm.facade.person.struct.BDMSearchNonDuplicatePersonKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.CORRESPONDENT;
import curam.codetable.DUPLICATEREASON;
import curam.codetable.MARITALSTATUS;
import curam.codetable.PROVINCETYPE;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.facade.fact.ClientMergeFactory;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.intf.ClientMerge;
import curam.core.facade.struct.ClientMergeAgendaKey;
import curam.core.facade.struct.ClientMergeParticipantKey;
import curam.core.facade.struct.ConcernRoleDetailsForMerge;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.MarkDuplicateCreateDetails;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.CaseHeader;
import curam.core.intf.PersonRegistration;
import curam.core.sl.entity.fact.ConcernRoleMergeFactory;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.intf.ConcernRoleMerge;
import curam.core.sl.entity.struct.ConcernRoleMergeDtls;
import curam.core.sl.entity.struct.ConcernRoleMergeDtlsList;
import curam.core.sl.entity.struct.ReadByConcernRoleDuplicateIDKey;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.sl.struct.CaseIDAndParticipantRoleIDKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.sl.struct.MergeTabList;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.RegistrationIDDetails;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
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
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.WorkflowDeadlineFactory;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.WorkflowDeadlineDtls;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMClientMergeTest extends CuramServerTestJUnit4 {

  public BDMClientMergeTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  protected EvidenceTypeVersionDefDAO etVerDefDAO;

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  @Mocked
  curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory personFactory;

  final BDMClientMerge bdmClientMerge = BDMClientMergeFactory.newInstance();

  BDMPersonSearchResultByTrackingNumList bdmPersonSearchResultByTrackingNumList =
    null;

  private static final String kParticipant = "participant";

  private static final String kPreferred = "preferredInd";

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmPersonSearchResultByTrackingNumList =
      new BDMPersonSearchResultByTrackingNumList();
  }

  /**
   * Dummy call to BDMPersonFactory - SearchByTrackingNumber()
   *
   * @throws AppException
   *
   * @throws InformationalException
   */
  private void expectationSearchByTrackingNumber(final long concernRoleID,
    final String alternateID) throws AppException, InformationalException {

    new Expectations() {

      {
        curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory.newInstance()
          .searchByTrackingNumber((BDMSearchByTrackingNumberKey) any);
        final BDMPersonSearchResultByTrackingNum resultNum =
          new BDMPersonSearchResultByTrackingNum();
        resultNum.concernRoleID = concernRoleID;
        resultNum.primaryAlternateID = alternateID;

        bdmPersonSearchResultByTrackingNumList.dtls.add(resultNum);

        result = bdmPersonSearchResultByTrackingNumList;
      }
    };
  }

  /**
   * register and person merge Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchNonDuplicatePersonDetailsExt()
    throws AppException, InformationalException {

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Maverick";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.surname = "Mave";
    bdmPersonSearchKey1.personSearchKey.postalCode = "K5M";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    bdmPersonSearchDetailsResult =
      bdmClientMerge.searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);

    assertTrue(
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() == 1);
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "Tom");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Maverick");

  }

  /**
   * validate person(s) before marking duplicate
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testMarkDuplicate()
    throws AppException, InformationalException {

    // Register Original person - test for without SIN
    final String name = "Original Person";
    final long originalConcernRoleID = registerPerson(name);

    // Register Original person
    final String dupName = "Duplicate Person";
    final long dupConcernRoleID = registerPerson(dupName);

    final MarkDuplicateCreateDetails markDuplicateCreateDetails =
      new MarkDuplicateCreateDetails();
    markDuplicateCreateDetails.markDuplicateCreateDetails.originalConcernRoleID =
      originalConcernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateConcernRoleID =
      dupConcernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateComments =
      "Merge Unit Test";
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateReason =
      DUPLICATEREASON.RECREATERR;

    final BDMPersonMergeValidations bdmPersonMergeValidations =
      bdmClientMerge.markDuplicate(markDuplicateCreateDetails);
    assertTrue(bdmPersonMergeValidations.validationMsgDtls.dtls.isEmpty());

    // Test if none of the concernRoldID is passed
    final PersonRegistrationResult originalPerson1 =
      registerPersonwithSIN("Original Person with SIN", "081975187");
    final String dupName1 = "Duplicate Person1";
    final long dupConcernRoleID1 = registerPerson(dupName1);

    final MarkDuplicateCreateDetails markDuplicateCreateDetails1 =
      new MarkDuplicateCreateDetails();
    markDuplicateCreateDetails1.markDuplicateCreateDetails.originalConcernRoleID =
      originalPerson1.registrationIDDetails.concernRoleID;
    markDuplicateCreateDetails1.markDuplicateCreateDetails.duplicateConcernRoleID =
      dupConcernRoleID1;
    markDuplicateCreateDetails1.markDuplicateCreateDetails.duplicateComments =
      "Merge Unit Test";
    markDuplicateCreateDetails1.markDuplicateCreateDetails.duplicateReason =
      DUPLICATEREASON.RECREATERR;

    final BDMPersonMergeValidations bdmPersonMergeValidations1 =
      bdmClientMerge.markDuplicate(markDuplicateCreateDetails1);
    assertTrue(!bdmPersonMergeValidations1.validationMsgDtls.dtls.isEmpty());

    // Register Original person - test with SIN
    final PersonRegistrationResult originalPerson2 =
      registerPersonwithSIN("Original Person with SIN 2", "081975187");

    // Register Original person

    final PersonRegistrationResult dupPerson2 =
      registerPersonwithSIN("Duplicate Person  with SIN 2", "039413539");

    final MarkDuplicateCreateDetails markDuplicateCreateDetails2 =
      new MarkDuplicateCreateDetails();
    markDuplicateCreateDetails2.markDuplicateCreateDetails.originalConcernRoleID =
      originalPerson2.registrationIDDetails.concernRoleID;
    markDuplicateCreateDetails2.markDuplicateCreateDetails.duplicateConcernRoleID =
      BDMConstants.kZeroLongValue;
    markDuplicateCreateDetails2.markDuplicateCreateDetails.duplicateComments =
      "Merge Unit Test";
    markDuplicateCreateDetails2.markDuplicateCreateDetails.duplicateReason =
      DUPLICATEREASON.RECREATERR;

    final BDMPersonMergeValidations bdmPersonMergeValidations2 =
      bdmClientMerge.markDuplicate(markDuplicateCreateDetails2);
    assertTrue(!bdmPersonMergeValidations2.validationMsgDtls.dtls.isEmpty());

    /*
     * // Register Original person - test with SIN
     * final PersonRegistrationResult originalPerson3 =
     * registerPersonwithSIN("Original Person with SIN 1", "081975187");
     *
     * // Register Original person
     *
     * final PersonRegistrationResult dupPerson2 =
     * registerPersonwithSIN("Duplicate Person  with SIN 1", "039413539");
     *
     * final MarkDuplicateCreateDetails markDuplicateCreateDetails3 =
     * new MarkDuplicateCreateDetails();
     * markDuplicateCreateDetails3.markDuplicateCreateDetails.
     * originalConcernRoleID =
     * originalPerson3.registrationIDDetails.concernRoleID;
     * markDuplicateCreateDetails3.markDuplicateCreateDetails.
     * duplicateConcernRoleID =
     * dupPerson2.registrationIDDetails.concernRoleID;
     * markDuplicateCreateDetails3.markDuplicateCreateDetails.duplicateComments
     * =
     * "Merge Unit Test";
     * markDuplicateCreateDetails3.markDuplicateCreateDetails.duplicateReason =
     * DUPLICATEREASON.RECREATERR;
     *
     * final BDMPersonMergeValidations bdmPersonMergeValidations3 =
     * bdmClientMerge.markDuplicate(markDuplicateCreateDetails3);
     * assertTrue(!bdmPersonMergeValidations3.validationMsgDtls.dtls.isEmpty());
     */

  }

  /**
   * validate if duplicate person has any task exists
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testIsTaskExistsForDuplicatePerson()
    throws AppException, InformationalException {

    // Register person
    final String name = "Original Person";
    final long originalConcernRoleID = registerPerson(name);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(originalConcernRoleID);

    // Create task 1
    createTaskData("susan", createCase.createCaseResult.integratedCaseID);

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = originalConcernRoleID;

    assertTrue(bdmClientMerge.isTaskExistsForDuplicatePerson(
      concernRoleKey).informationalMsgDtls.dtls.size() == 0);
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

  public PDCPersonDetails createDupPDCPerson()
    throws AppException, InformationalException {

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    personRegistrationDetails.firstForename = "Duplicate Person";
    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();
    pdcPersonDetails.assign(personRegistrationDetails);
    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  /**
   * Register person
   *
   *
   * @throws AppException
   * @throws InformationalException
   */
  private long registerPerson(final String name)
    throws AppException, InformationalException {

    final RegisterPerson registerPersonObj = new RegisterPerson("");
    final PersonDtls personDtls =
      registerPersonObj.registerDefault(name, METHODOFDELIVERYEntry.CHEQUE);
    return personDtls.concernRoleID;

  }

  /*** Create FEc case for person */
  protected CreateIntegratedCaseResultAndMessages createFEC(
    final long concernRoleID) throws AppException, InformationalException {

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;
    details.countryCode = BDMSOURCECOUNTRY.IRELAND;
    details.priorityCode = CASEPRIORITY.HIGH;

    return BDMForeignEngagementCaseFactory.newInstance()
      .createFEIntegratedCase(details);

  }

  /**
   * Creates test data for task with deadline date set
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createTaskData(final String username, final long caseID)
    throws AppException, InformationalException {

    // create task object
    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.NOTSTARTED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = username;
    TaskFactory.newInstance().insert(taskCreateDetail);

    // create task assignment
    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = username;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.USER;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);

    // Create task deadline
    final WorkflowDeadlineDtls workflowDeadlineDtls =
      new WorkflowDeadlineDtls();
    workflowDeadlineDtls.deadlineID = UniqueID.nextUniqueID();
    workflowDeadlineDtls.taskID = taskCreateDetail.taskID;
    workflowDeadlineDtls.suspended = false;
    workflowDeadlineDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(72, 0, 0);
    workflowDeadlineDtls.deadlineType = "WDT1";
    workflowDeadlineDtls.versionNo = 1;

    WorkflowDeadlineFactory.newInstance().insert(workflowDeadlineDtls);
  }

  /**
   * validate person(s) before marking duplicate and get the merge wizard
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testGetAgenda() throws AppException, InformationalException {

    // Register Original person
    final String name = "Original Person";
    final long originalConcernRoleID = registerPerson(name);

    // Register Original person
    final String dupName = "Duplicate Person";
    final long dupConcernRoleID = registerPerson(dupName);

    final MarkDuplicateCreateDetails markDuplicateCreateDetails =
      new MarkDuplicateCreateDetails();
    markDuplicateCreateDetails.markDuplicateCreateDetails.originalConcernRoleID =
      originalConcernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateConcernRoleID =
      dupConcernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateComments =
      "Merge Unit Test";
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateReason =
      DUPLICATEREASON.RECREATERR;

    final BDMPersonMergeValidations bdmPersonMergeValidations =
      bdmClientMerge.markDuplicate(markDuplicateCreateDetails);

    final ClientMergeAgendaKey agendaKey = new ClientMergeAgendaKey();
    agendaKey.originalConcernRoleID = originalConcernRoleID;
    agendaKey.duplicateConcernRoleID = dupConcernRoleID;
    agendaKey.concernRoleDuplicateID =
      bdmPersonMergeValidations.dtls.markDuplicateDetails.concernRoleDuplicateID;

    assertTrue(!bdmClientMerge.getAgenda(agendaKey).agenda.isEmpty());

  }

  /**
   * validate person(s) before marking duplicate and get the merge wizard
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testGetAgenda1() throws AppException, InformationalException {

    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;

    final PDCPersonDetails pdcDupPersonDetails = createDupPDCPerson();
    final long dupConcernRoleID = pdcDupPersonDetails.concernRoleID;

    final MarkDuplicateCreateDetails markDuplicateCreateDetails =
      new MarkDuplicateCreateDetails();
    markDuplicateCreateDetails.markDuplicateCreateDetails.originalConcernRoleID =
      originalConcernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateConcernRoleID =
      dupConcernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateComments =
      "Merge Unit Test";
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateReason =
      DUPLICATEREASON.RECREATERR;

    // Original Person
    // Participant Data Case
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    // Case Participant Role
    final CaseParticipantRole cprObj =
      CaseParticipantRoleFactory.newInstance();
    final CaseIDAndParticipantRoleIDKey cprKey =
      new CaseIDAndParticipantRoleIDKey();
    cprKey.caseID = pdcID;
    cprKey.participantRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseParticipantRoleIDStruct cprStruct =
      cprObj.readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(cprKey);
    final Long cprID = cprStruct.caseParticipantRoleID;

    // original person evidence
    // Create first evidence record
    final curam.core.sl.struct.EvidenceKey origPersonMaritalKey =
      createMaritalStatusEvidence(pdcID, cprID,
        pdcOriginalPersonDetails.concernRoleID, Date.fromISO8601("20230101"),
        Date.fromISO8601("20230102"));

    // Duplicate Person
    // Participant Data Case
    final CaseHeader dupCaseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey dupCaseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    dupCaseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    dupCaseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    dupCaseHeaderKey.concernRoleID = pdcDupPersonDetails.concernRoleID;
    final CaseHeaderDtlsList dupPDCList =
      dupCaseHeaderObj.searchByConcernRoleIDType(dupCaseHeaderKey);
    final Long pdcID1 = dupPDCList.dtls.get(0).caseID;

    // Case Participant Role
    final CaseParticipantRole cprObj1 =
      CaseParticipantRoleFactory.newInstance();
    final CaseIDAndParticipantRoleIDKey cprKey1 =
      new CaseIDAndParticipantRoleIDKey();
    cprKey1.caseID = pdcID1;
    cprKey1.participantRoleID = pdcDupPersonDetails.concernRoleID;
    final CaseParticipantRoleIDStruct cprStruct1 =
      cprObj1.readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(cprKey1);
    final Long dupCPRID = cprStruct1.caseParticipantRoleID;

    // duplicate person evidence
    // Create first evidence record
    final curam.core.sl.struct.EvidenceKey dupPersonMaritalKey =
      createMaritalStatusEvidence(pdcID1, dupCPRID,
        pdcDupPersonDetails.concernRoleID, Date.fromISO8601("20230103"),
        Date.fromISO8601("20230104"));

    final BDMPersonMergeValidations bdmPersonMergeValidations =
      bdmClientMerge.markDuplicate(markDuplicateCreateDetails);

    final ClientMergeAgendaKey agendaKey = new ClientMergeAgendaKey();
    agendaKey.originalConcernRoleID = originalConcernRoleID;
    agendaKey.duplicateConcernRoleID = dupConcernRoleID;
    agendaKey.concernRoleDuplicateID =
      bdmPersonMergeValidations.dtls.markDuplicateDetails.concernRoleDuplicateID;

    assertTrue(!bdmClientMerge.getAgenda(agendaKey).agenda.isEmpty());

  }

  /**
   * validate person(s) before marking duplicate and get the merge wizard
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testListMaritalStatusEvidenceForMerge()
    throws AppException, InformationalException {

    // Register Original person
    final String name = "Original Person";
    final long originalConcernRoleID = registerPerson(name);

    // Register Original person
    final String dupName = "Duplicate Person";
    final long dupConcernRoleID = registerPerson(dupName);

    final MarkDuplicateCreateDetails markDuplicateCreateDetails =
      new MarkDuplicateCreateDetails();
    markDuplicateCreateDetails.markDuplicateCreateDetails.originalConcernRoleID =
      originalConcernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateConcernRoleID =
      dupConcernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateComments =
      "Merge Unit Test";
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateReason =
      DUPLICATEREASON.RECREATERR;

    // final BDMPersonMergeValidations bdmPersonMergeValidations =
    // Merger the persons.
    bdmClientMerge.markDuplicate(markDuplicateCreateDetails);

    final ClientMergeParticipantKey clientMergeParticipantKey =
      new ClientMergeParticipantKey();

    clientMergeParticipantKey.key.originalConcernRoleID =
      originalConcernRoleID;
    clientMergeParticipantKey.key.duplicateConcernRoleID = dupConcernRoleID;

    assertTrue(bdmClientMerge.listMaritalStatusEvidenceForMerge(
      clientMergeParticipantKey).evidenceList.origList.isEmpty());
    assertTrue(bdmClientMerge.listMaritalStatusEvidenceForMerge(
      clientMergeParticipantKey).evidenceList.dupList.isEmpty());

  }

  /**
   * Search perosn by Correct tracking number and correct additional Criteria
   * should display person result
   */
  @Test
  public void
    testSearchNonDuplicatePersonDetailsExt_trackingNumberAndAdditionalCriteria()
      throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      createPersonRecord("Jane", "Doe");

    // Assert person is registered
    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "Jane Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    // create Search criteris with tracking number only
    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();
    bdmPersonSearchKey1.personSearchKey.corrTrackingNumber = "1234";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.forename =
      "Jane";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.surname = "Doe";
    bdmPersonSearchKey1.personSearchKey.postalCode = "T6K 3M1";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    bdmPersonSearchDetailsResult =
      bdmClientMerge.searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);

    assertTrue(
      !bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "Jane");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Doe");

    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).postalOrZipCode, "T6K 3M1");

  }

  /**
   * Search perosn by Correct tracking number and Incorrect reference should not
   * display person result
   */

  @Test
  public void
    testSearchNonDuplicatePersonDetailsExt_trackingNumberAndReferenceNumber()
      throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      createPersonRecord("Jane", "Doe");

    // Register perosn 2 with SIN number
    final PersonRegistrationResult registrationResult2 =
      registerPersonwithSIN("Test", "599447182");
    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "Jane Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    // create Search criteris with tracking number only
    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();
    bdmPersonSearchKey1.personSearchKey.corrTrackingNumber = "1234";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.referenceNumber =
      registrationResult2.registrationIDDetails.alternateID;

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    // Assert no records are found with given criteria
    try {
      bdmPersonSearchDetailsResult = bdmClientMerge
        .searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);
    } catch (final Exception ex) {
      assertTrue(
        bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());
    }

  }

  /**
   * Search person by Correct tracking number and Incorrect additional Criteria
   * should not display any result
   *
   */
  @Test
  public void
    testSearchNonDuplicatePersonDetailsExt_trackingNumberAndWronAdditionalCriteria()
      throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      createPersonRecord("Jane", "Doe");

    // Assert person is registered
    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "Jane Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    // create Search criteris with tracking number only
    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();
    // Set correct Trackinh number
    bdmPersonSearchKey1.personSearchKey.corrTrackingNumber = "1234";
    // Incorrect Name
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.forename =
      "Test";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.surname = "Doe";
    bdmPersonSearchKey1.personSearchKey.postalCode = "T6K 3M1";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    try {
      bdmPersonSearchDetailsResult = bdmClientMerge
        .searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);
    } catch (final Exception e) {
      assertEquals(
        "There are no matching items based on the Search Criteria entered.",
        e.getLocalizedMessage());
      assertTrue(
        bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());
    }

  }

  /**
   * Search perosn by Correct tracking number and correct reference should
   * display person result
   */

  @Test
  public void
    testSearchNonDuplicatePersonDetailsExt_correcttrackingNumberAndReferenceNumber()
      throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      createPersonRecord("Jane", "Doe");

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "Jane Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    // create Search criteris with tracking number only
    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();
    bdmPersonSearchKey1.personSearchKey.corrTrackingNumber = "1234";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.referenceNumber =
      registrationResult.registrationIDDetails.alternateID;

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    // Assert no records are found with given criteria

    bdmPersonSearchDetailsResult =
      bdmClientMerge.searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);

    assertTrue(
      !bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "Jane");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Doe");

    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).postalOrZipCode, "T6K 3M1");

  }

  /**
   * Search Person by Tracking Number only
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchNonDuplicatePersonDetailsExt_trackingNumberOnly()
    throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      createPersonRecord("John", "Doe");

    // Assert person is registered
    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "John Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    expectationSearchByTrackingNumber(
      registrationResult.registrationIDDetails.concernRoleID,
      registrationResult.registrationIDDetails.alternateID);

    // create Search criteris with tracking number only
    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();
    bdmPersonSearchKey1.personSearchKey.corrTrackingNumber = "1234";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    bdmPersonSearchDetailsResult =
      bdmClientMerge.searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);

    assertTrue(
      !bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "John");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Doe");

    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).dateOfBirth, Date.getDate("19800101"));

  }

  /**
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private PersonRegistrationResult createPersonRecord(final String firstName,
    final String lastName) throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = firstName;
    bdmPersonRegistrationDetails.dtls.surname = lastName;
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19800101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // Person Address Details
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "T6K 3M1";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;
    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    // Register Person
    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);
    return registrationResult;
  }

  /**
   * Search Person by Tracking Number when Tracking number provided is Alpha
   * Numeric
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void
    testSearchNonDuplicatePersonDetailsExt_trackingNumberOnly_AlphaNum()
      throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      createPersonRecord("John", "Doe");

    // Assert person is registered
    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    // Create FEC case
    final CreateIntegratedCaseResultAndMessages createCase =
      createFEC(registrationResult.registrationIDDetails.concernRoleID);

    // Created recorded CXommunication
    final ConcernRoleCommKeyOut commID =
      createRecordedCommunicationwithEscalation(
        createCase.createCaseResult.integratedCaseID, "John Doe",
        registrationResult.registrationIDDetails.concernRoleID);

    // Create BDM concernRole Communication with Tracking number
    createBDMConcernRoleCommunication(commID.communicationID);

    // create Search criteris with tracking number only
    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();
    bdmPersonSearchKey1.personSearchKey.corrTrackingNumber = "Abc123%";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();
    try {
      bdmPersonSearchDetailsResult = bdmClientMerge
        .searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);
    } catch (final Exception e) {
      assertTrue(
        bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty());
      assertEquals(
        "There are no matching items based on the Search Criteria entered.",
        e.getMessage());
    }

  }

  /**
   * Search Person by Tracking Number only
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchNonDuplicatePersonDetailsExt_NoCriteria()
    throws AppException, InformationalException {

    // create Search criteris with tracking number only
    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();
    boolean caught = false;
    try {
      bdmPersonSearchDetailsResult = bdmClientMerge
        .searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);
    } catch (final Exception ex) {
      caught = true;
    }

    assertTrue(caught);

  }

  /**
   * Search Person by firstname last name and partial city name
   * Should throw exception
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchNonDuplicatePersonDetailsExt_City()
    throws AppException, InformationalException {

    // create Search criteris with tracking number only
    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();

    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.addressDtls.city =
      "O";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.surname =
      "Cruise";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();
    boolean caught = false;
    try {
      bdmPersonSearchDetailsResult = bdmClientMerge
        .searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);
    } catch (final Exception ex) {
      caught = true;
      ex.printStackTrace();
    }

    assertTrue(caught);

  }

  /**
   * Search Person by firstname last name and partial province name
   * Should throw exception
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchNonDuplicatePersonDetailsExt_StateProvince()
    throws AppException, InformationalException {

    // create Search criteris with tracking number only
    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();

    bdmPersonSearchKey1.personSearchKey.stateProvince = "On";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.surname =
      "Cruise";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();
    boolean caught = false;
    try {
      bdmPersonSearchDetailsResult = bdmClientMerge
        .searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);
    } catch (final Exception ex) {
      caught = true;
      ex.printStackTrace();
    }

    assertTrue(caught);

  }

  /**
   * Search Person by firstname last name and partial postalalcode
   * Should throw exception
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchNonDuplicatePersonDetailsExt_postalcode()
    throws AppException, InformationalException {

    // create Search criteris with tracking number only
    final BDMSearchNonDuplicatePersonKey bdmPersonSearchKey1 =
      new BDMSearchNonDuplicatePersonKey();

    bdmPersonSearchKey1.personSearchKey.postalCode = "K5";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.forename = "Tom";
    bdmPersonSearchKey1.personSearchKey.dtls.personSearchKey.surname =
      "Cruise";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();
    boolean caught = false;
    try {
      bdmPersonSearchDetailsResult = bdmClientMerge
        .searchNonDuplicatePersonDetailsExt(bdmPersonSearchKey1);
    } catch (final Exception ex) {
      caught = true;
      ex.printStackTrace();
    }

    assertTrue(caught);

  }

  /** Create recorded Communication with escaltion level */
  ConcernRoleCommKeyOut createRecordedCommunicationwithEscalation(
    final long caseID, final String concernRoleName, final long concernRoleID)
    throws AppException, InformationalException {

    final RecordedCommDetails1 recordedCommDetails =
      new RecordedCommDetails1();

    recordedCommDetails.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";
    recordedCommDetails.caseID = caseID;
    recordedCommDetails.communicationDirection = "CD2";
    recordedCommDetails.communicationText = "Test communication";
    recordedCommDetails.communicationTypeCode = "CT8007"; // requires attendtion
    recordedCommDetails.correspondentName = concernRoleName;
    recordedCommDetails.correspondentParticipantRoleID = concernRoleID;
    recordedCommDetails.correspondentType = CORRESPONDENT.CLIENT;
    recordedCommDetails.methodTypeCode = "CM1";
    recordedCommDetails.subject = "Test comm 2";
    recordedCommDetails.communicationFormat = "CF4"; // recorded Communication

    return BDMCommunicationFactory.newInstance()
      .createRecordedCommWithReturningID(recordedCommDetails);
  }

  /**
   * Create BDM ConcernRole communication Object with Tracking number
   */
  private void createBDMConcernRoleCommunication(final long communicationID)
    throws AppException, InformationalException {

    final BDMConcernRoleCommunicationDtls bdmConcernRoleCommunicationDtls =
      new BDMConcernRoleCommunicationDtls();
    bdmConcernRoleCommunicationDtls.submittedInd = true;
    bdmConcernRoleCommunicationDtls.digitalInd = true;
    bdmConcernRoleCommunicationDtls.trackingNumber = 1234;
    bdmConcernRoleCommunicationDtls.communicationID = communicationID;
    BDMConcernRoleCommunicationFactory.newInstance()
      .insert(bdmConcernRoleCommunicationDtls);

  }

  /** register a person **/

  private PersonRegistrationResult
    registerPersonwithSIN(final String lastName, final String sinNumber)
      throws AppException, InformationalException {

    // Person Details
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Test";
    bdmPersonRegistrationDetails.dtls.surname = lastName;
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19800101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = sinNumber;
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // Person Address Details
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "T6K 3M1";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;
    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    // Register Person
    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    return PersonFactory.newInstance().register(details);

  }

  /**
   * Verify evidence is successfully created when valid details
   * are entered
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testMergeMaritalStatusEvidence()
    throws AppException, InformationalException {

    // Register Original person
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;

    final PDCPersonDetails pdcDupPersonDetails = createDupPDCPerson();
    final long dupConcernRoleID = pdcDupPersonDetails.concernRoleID;
    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createOriginalPDCPerson();

    // create address evidence for original person
    final Date fromDate = Date.fromISO8601("20010101");
    final Date toDate = Date.fromISO8601("20010102");

    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final EIEvidenceKey eiEvidenceKey =
      bdmEvidenceUtilsTest.createAddressEvidence1(pdcPersonDetails,
        CONCERNROLEADDRESSTYPE.REGISTERED, fromDate, toDate);

    final Date dupFromDate = Date.fromISO8601("20010103");
    final Date dupToDate = null;
    bdmEvidenceUtilsTest.createAddressEvidence1(pdcDupPersonDetails,
      CONCERNROLEADDRESSTYPE.REGISTERED, dupFromDate, dupToDate);
    // end creating of address evidence

    final MarkDuplicateCreateDetails markDuplicateCreateDetails =
      new MarkDuplicateCreateDetails();
    markDuplicateCreateDetails.markDuplicateCreateDetails.originalConcernRoleID =
      originalConcernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateConcernRoleID =
      dupConcernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateComments =
      "Merge Unit Test";
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateReason =
      DUPLICATEREASON.RECREATERR;

    // Participant Data Case
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    // Case Participant Role
    final CaseParticipantRole cprObj =
      CaseParticipantRoleFactory.newInstance();
    final CaseIDAndParticipantRoleIDKey cprKey =
      new CaseIDAndParticipantRoleIDKey();
    cprKey.caseID = pdcID;
    cprKey.participantRoleID = pdcPersonDetails.concernRoleID;
    final CaseParticipantRoleIDStruct cprStruct =
      cprObj.readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(cprKey);
    final Long cprID = cprStruct.caseParticipantRoleID;

    // original person evidence
    // Create first evidence record
    final curam.core.sl.struct.EvidenceKey origPersonMaritalKey =
      createMaritalStatusEvidence(pdcID, cprID,
        pdcPersonDetails.concernRoleID, Date.fromISO8601("20230201"),
        Date.fromISO8601("20230202"));

    // Duplicate Person
    // Participant Data Case
    final CaseHeader dupCaseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey dupCaseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    dupCaseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    dupCaseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    dupCaseHeaderKey.concernRoleID = pdcDupPersonDetails.concernRoleID;
    final CaseHeaderDtlsList dupPDCList =
      dupCaseHeaderObj.searchByConcernRoleIDType(dupCaseHeaderKey);
    final Long pdcID1 = dupPDCList.dtls.get(0).caseID;

    // Case Participant Role
    final CaseParticipantRole cprObj1 =
      CaseParticipantRoleFactory.newInstance();
    final CaseIDAndParticipantRoleIDKey cprKey1 =
      new CaseIDAndParticipantRoleIDKey();
    cprKey1.caseID = pdcID1;
    cprKey1.participantRoleID = pdcDupPersonDetails.concernRoleID;
    final CaseParticipantRoleIDStruct cprStruct1 =
      cprObj1.readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(cprKey1);
    final Long dupCPRID = cprStruct1.caseParticipantRoleID;

    // duplicate person evidence
    // Create first evidence record
    final curam.core.sl.struct.EvidenceKey dupPersonMaritalKey =
      createMaritalStatusEvidence(pdcID1, dupCPRID,
        pdcDupPersonDetails.concernRoleID, Date.fromISO8601("20230103"),
        Date.fromISO8601("20230104"));

    final BDMPersonMergeValidations bdmPersonMergeValidations =
      bdmClientMerge.markDuplicate(markDuplicateCreateDetails);

    final ClientMergeAgendaKey agendaKey = new ClientMergeAgendaKey();
    agendaKey.originalConcernRoleID = originalConcernRoleID;
    agendaKey.duplicateConcernRoleID = dupConcernRoleID;
    agendaKey.concernRoleDuplicateID =
      bdmPersonMergeValidations.dtls.markDuplicateDetails.concernRoleDuplicateID;

    final ConcernRoleMerge concernRoleMerge =
      ConcernRoleMergeFactory.newInstance();

    // insert the record in CONCERNROLEMERGE table

    final ConcernRoleMergeDtls concernRoleMergeDtls =
      new ConcernRoleMergeDtls();
    concernRoleMergeDtls.concernRoleDuplicateID = dupConcernRoleID;
    concernRoleMergeDtls.mergeStartDate = TransactionInfo.getSystemDateTime();
    concernRoleMergeDtls.mergeStatus = "MS2";

    concernRoleMerge.insert(concernRoleMergeDtls);

    final ReadByConcernRoleDuplicateIDKey readByConcernRoleDuplicateIDKey =
      new ReadByConcernRoleDuplicateIDKey();

    readByConcernRoleDuplicateIDKey.concernRoleDuplicateID = dupConcernRoleID;

    final ConcernRoleMergeDtlsList concernRoleMergeDtlsList = concernRoleMerge
      .searchByConcernRoleDuplicateID(readByConcernRoleDuplicateIDKey);
    final ConcernRoleMergeDtls concernRoleMergeDtls1 =
      concernRoleMergeDtlsList.dtls.get(0);

    final ConcernRoleDetailsForMerge concernRoleDetailsForMerge =
      new ConcernRoleDetailsForMerge();

    concernRoleDetailsForMerge.dtls.concernRoleMergeID =
      concernRoleMergeDtls1.concernRoleMergeID;
    concernRoleDetailsForMerge.dtls.originalConcernRoleID =
      originalConcernRoleID;

    final MergeTabList tabList = new MergeTabList();
    tabList.mergeTabList = String.valueOf(origPersonMaritalKey.evidenceID);

    bdmClientMerge.mergeMaritalStatusEvidence(concernRoleDetailsForMerge,
      tabList);

    final ClientMerge clientMerge = ClientMergeFactory.newInstance();
    tabList.mergeTabList = String.valueOf(eiEvidenceKey.evidenceID);

    // clientMerge.mergeAddresses(concernRoleDetailsForMerge, tabList);

    bdmClientMerge.completeMerge(concernRoleDetailsForMerge);

    // , final MergeTabList tabList

  }

  private static final String CASE_PARTICIPANT_ROLE = "caseParticipantRoleID";

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  private static final String MARITAL_STATUS = "maritalStatus";

  private static final String ERR_ONLY_ONE_RECORD =
    "A client cannot have more than one Marital Status record.";

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

}
