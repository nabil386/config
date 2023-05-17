package curam.bdm.test.ca.gc.bdm.facade.fec.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMATTACHMENTLINKTYPE;
import curam.ca.gc.bdm.codetable.BDMCREDITABLEPERIODSOURCE;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPDELETEREASON;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPINTERIM;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPSTATUS;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.codetable.BDM_FILE_SOURCE;
import curam.ca.gc.bdm.entity.fact.BDMFAHistoryFactory;
import curam.ca.gc.bdm.entity.fact.BDMFEAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignApplicationFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.intf.BDMFAHistory;
import curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink;
import curam.ca.gc.bdm.entity.intf.BDMForeignApplication;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryKey;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMFAKeyStruct3;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.facade.fact.BDMCaseFactory;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.intf.BDMForeignEngagementCase;
import curam.ca.gc.bdm.facade.fec.struct.BDMAttachmentIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMCaseKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMCreateModifyFA;
import curam.ca.gc.bdm.facade.fec.struct.BDMDeleteFADetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetailsForRead;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAConcernRoleKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFACountryCodeKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFADetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAList;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAViewAttachmentDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECReadICDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfFAApplicationTypes;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfForeignIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfOffices;
import curam.ca.gc.bdm.facade.fec.struct.BDMModifyFECaseDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMReadFECaseDetails;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMListICAttachmentDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.struct.BDMReadCaseAttachmentDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMFEC;
import curam.ca.gc.bdm.sl.fec.fact.BDMMaintainForeignEngagementCaseFactory;
import curam.ca.gc.bdm.sl.fec.struct.BDMFAInterimCodeDetailsList;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CancelCaseAttachmentKey;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.IntegratedCaseIDKey;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.ReadCaseAttachmentKey;
import curam.core.fact.AttachmentFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.PersonRegistration;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.fact.ClientMergeFactory;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.CuramInd;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.RegistrationIDDetails;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
import curam.supervisor.facade.struct.ReserveTaskDetailsForUser;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.WorkflowDeadlineFactory;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.TaskKey;
import curam.util.workflow.struct.WorkflowDeadlineDtls;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for class BDMForeignEngagementCaseTest
 */
@RunWith(JMockit.class)
public class BDMForeignEngagementCaseTest extends CuramServerTestJUnit4 {

  BDMForeignEngagementCase kBdmForeignEngagementCaseObj = null;

  public static final String EMPTY_STRING = "";

  private static String CASEOBJECTIVE_FINANCIALSUPPORT = "CO5";

  private static String CASEOUTCOME = "CO4";

  private static String FILE_LOCATION = "FILE LOCATION";

  private static String FILE_REFERENCE = "FILE REFERENCE";

  private static String FILE_DESCRIPTION = "FILE DESCRIPTION";

  private static String FA_COMMENTS =
    "This comments entered on the New Foreign Application screen";

  private static String YES = "Yes";

  private static final String ERR_YOU_CANNOT_DELETE_THIS_RECORD_ALREADY_DELETED =
    "This record has already been deleted.";

  private static final String ERR_YOU_CANNOT_DELETE_THIS_RECORD_SINCE_COMPLETED =
    "You cannot delete this record as it has been Completed.";

  private static final String ERR_RECEIVED_DATE_CANNOT_BE_IN_FUTURE =
    "'Received Date' must not be a future date.";

  private static final String ERR_INTERIM_CHK_BOX_SELECTED_BESS_CANNOT_BE_SELECTED =
    "If ‘Interim’ is entered then 'BESS' must not be entered.";

  private static final String ERR_INTERIM_CHK_BOX_SELECTED_SPECIFY_NOT_ENTERED =
    "If ‘Interim’ and ‘Other’ are selected, then ‘Specify’ must be entered.";

  private static final String WAR_DUPLICATE_PERSON_FEC_CASE =
    "This case belongs to a Duplicate Person. Do not perform any updates on this case. Find the Master Person and make updates to the Master Person’s FEC.";

  private static final String CASE_PARTICIPANT_ROLE = "caseParticipantRole";

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  private static final String COUNTRY = "country";

  private static final String CREDITABLE_PERIOD_SOURCE =
    "creditablePeriodsource";

  @Mocked
  ClientMergeFactory clientMergeFactory;

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  public BDMForeignEngagementCaseTest() {

    super();
  }

  CuramInd duplicateInd = null;

  @Before
  public void setUp() throws AppException, InformationalException {

    kBdmForeignEngagementCaseObj =
      BDMForeignEngagementCaseFactory.newInstance();
    duplicateInd = new CuramInd();

  }

  /**
   * Dummy call to ClientMergeFactory - isConcernRoleDuplicate()
   *
   * @throws AppException
   *
   * @throws InformationalException
   */
  private void
    expectationIsConcernRoleDuplicate(final ConcernRoleKey concernRoleKey)
      throws AppException, InformationalException {

    new Expectations() {

      {
        ClientMergeFactory.newInstance()
          .isConcernRoleDuplicate((ConcernRoleKey) any);

        duplicateInd.statusInd = true;

        result = duplicateInd;
      }
    };
  }

  @Test
  public void testCreateFEIntegratedCase()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    registrationResult = registerPersonWithContactPref();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID =
      registrationResult.registrationIDDetails.concernRoleID;

    details.countryCode = BDMSOURCECOUNTRY.IRELAND;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);

    assertTrue(
      createIntegratedCaseResultAndMessages.createCaseResult.homePageName
        .equals("BDMFECIntegratedCase_home"));

    assertTrue(
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID != 0);

  }

  @Test
  public void testReadFEIntegratedCase()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    // register person
    registrationResult = registerPersonWithContactPref();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    BDMReadFECaseDetails bdmReadFECaseDetails = new BDMReadFECaseDetails();

    final IntegratedCaseIDKey integratedCaseIDKey = new IntegratedCaseIDKey();
    integratedCaseIDKey.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    bdmReadFECaseDetails =
      kBdmForeignEngagementCaseObj.readFEIntegratedCase(integratedCaseIDKey);

    assertTrue(
      bdmReadFECaseDetails.countryCode.equals(BDMSOURCECOUNTRY.IRELAND));

    assertTrue(bdmReadFECaseDetails.readICDetails.details.priorityCode
      .equals(CASEPRIORITY.HIGH));

    assertTrue(bdmReadFECaseDetails.readICDetails.details.integratedCaseType
      .equals(PRODUCTCATEGORY.BDM_IC_FEC_PROGRAM));

  }

  @Test
  public void testModifyFEIntegratedCase()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerPersonWithContactPref();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    // BEGIN: read FE IC Details
    BDMReadFECaseDetails bdmReadFECaseDetails = new BDMReadFECaseDetails();

    final IntegratedCaseIDKey integratedCaseIDKey = new IntegratedCaseIDKey();
    integratedCaseIDKey.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    bdmReadFECaseDetails =
      kBdmForeignEngagementCaseObj.readFEIntegratedCase(integratedCaseIDKey);
    // END: read FE IC Details

    final BDMModifyFECaseDetails bdmModifyFECaseDetails =
      new BDMModifyFECaseDetails();

    bdmModifyFECaseDetails.countryCode = BDMSOURCECOUNTRY.JAMAICA;

    bdmModifyFECaseDetails.modifyICDetails.comments =
      "This comments entered on Edit Foreign Engagement Case - Screen";

    bdmModifyFECaseDetails.modifyICDetails.caseID =
      bdmReadFECaseDetails.readICDetails.details.caseID;
    bdmModifyFECaseDetails.modifyICDetails.concernRoleID =
      bdmReadFECaseDetails.readICDetails.details.concernRoleID;
    bdmModifyFECaseDetails.modifyICDetails.ownerOrgObjectLinkID =
      bdmReadFECaseDetails.readICDetails.details.orgObjectLinkID;
    bdmModifyFECaseDetails.modifyICDetails.versionNo =
      bdmReadFECaseDetails.readICDetails.details.versionNo;
    bdmModifyFECaseDetails.modifyICDetails.registrationDate =
      bdmReadFECaseDetails.readICDetails.details.registrationDate;
    bdmModifyFECaseDetails.modifyICDetails.objectiveCode =
      bdmReadFECaseDetails.readICDetails.details.objectiveCode;
    bdmModifyFECaseDetails.modifyICDetails.expectedEndDate =
      bdmReadFECaseDetails.readICDetails.details.expectedEndDate;
    bdmModifyFECaseDetails.modifyICDetails.priorityCode =
      bdmReadFECaseDetails.readICDetails.details.priorityCode;
    bdmModifyFECaseDetails.modifyICDetails.receiptDate =
      bdmReadFECaseDetails.readICDetails.details.receiptDate;
    bdmModifyFECaseDetails.modifyICDetails.outcomeCode =
      bdmReadFECaseDetails.readICDetails.details.outcomeCode;

    kBdmForeignEngagementCaseObj
      .modifyFEIntegratedCase(bdmModifyFECaseDetails);

    bdmReadFECaseDetails = new BDMReadFECaseDetails();

    bdmReadFECaseDetails =
      kBdmForeignEngagementCaseObj.readFEIntegratedCase(integratedCaseIDKey);

    assertTrue(
      bdmReadFECaseDetails.countryCode.equals(BDMSOURCECOUNTRY.JAMAICA));

    assertTrue(bdmReadFECaseDetails.readICDetails.details.priorityCode
      .equals(CASEPRIORITY.HIGH));

    assertTrue(bdmReadFECaseDetails.readICDetails.details.integratedCaseType
      .equals(PRODUCTCATEGORY.BDM_IC_FEC_PROGRAM));

    assertTrue(bdmReadFECaseDetails.readICDetails.details.comments
      .equals(bdmModifyFECaseDetails.modifyICDetails.comments));

  }

  @Test
  public void testReadFECDetailsForHome()
    throws AppException, InformationalException {

    // register person
    final PersonRegistrationResult registrationResult =
      registerPersonWithContactPref();

    // create FE IC for the person
    final CreateIntegratedCaseResultAndMessages integratedCaseResult =
      createAFE_IC_forTest(
        registrationResult.registrationIDDetails.concernRoleID);
    final long integratedCaseID =
      integratedCaseResult.createCaseResult.integratedCaseID;

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCaseID,
        registrationResult.registrationIDDetails.concernRoleID);

    // Create first evidence record
    final EvidenceKey evidenceKey1 = createForeignContributionPeriodEvidence(
      integratedCaseID, cprObj.caseParticipantRoleID,
      registrationResult.registrationIDDetails.concernRoleID,
      Date.fromISO8601("20200101"), Date.fromISO8601("20201231"));

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Check if the two foreign contribution period records are activated
    final ECActiveEvidenceDtlsList activeEvdList =
      evidenceController.listActive(caseKey);

    final IntegratedCaseIDKey integratedCaseIDKey = new IntegratedCaseIDKey();
    integratedCaseIDKey.caseID = integratedCaseID;
    BDMFECReadICDetails bdmfecReadICDetails = new BDMFECReadICDetails();

    bdmfecReadICDetails =
      kBdmForeignEngagementCaseObj.readFECDetailsForHome(integratedCaseIDKey);

    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
    bdmfeCaseKey.caseID = integratedCaseIDKey.caseID;

    assertTrue(bdmfecReadICDetails.readFEICDetails.details.integratedCaseType
      .equals(PRODUCTCATEGORY.BDM_IC_FEC_PROGRAM));

    assertTrue(BDMFECaseFactory.newInstance().read(bdmfeCaseKey).countryCode
      .equals(BDMSOURCECOUNTRY.IRELAND));

    assertTrue(
      bdmfecReadICDetails.totalForeignContributionPeriod.contains("year"));

  }

  @Test
  public void testReadFECDetailsForHome_Dup()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerPersonWithContactPref();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    final IntegratedCaseIDKey integratedCaseIDKey = new IntegratedCaseIDKey();
    integratedCaseIDKey.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    BDMFECReadICDetails bdmfecReadICDetails = new BDMFECReadICDetails();

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID =
      registrationResult.registrationIDDetails.concernRoleID;
    boolean caught = false;
    expectationIsConcernRoleDuplicate(concernRoleKey);

    try {
      bdmfecReadICDetails = kBdmForeignEngagementCaseObj
        .readFECDetailsForHome(integratedCaseIDKey);
    } catch (final Exception e) {
      caught = true;
      assertEquals(WAR_DUPLICATE_PERSON_FEC_CASE, e.getMessage());
    }
    assertTrue(caught);

  }

  /**
   * unlinking of a foreign application with an
   * attachment.
   */
  @Test
  public void testunlinkFAFromAttachment()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerPersonWithContactPref();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmForeignEngagementCaseObj
        .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Create Attachemnt of type Foreign Application

    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      FILE_DESCRIPTION;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.DIRECT_DEPOSIT;

    attachmentIDs = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment details from Step1
    bdmFAAttachmentDetails = kBdmForeignEngagementCaseObj
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Set data for foreign application
    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();
    bdmCreateModifyFA.consent = BDMYESNO.NO;

    // Create FA
    final BDMForeignApplicationKey bdmForeignApplicationKey =
      kBdmForeignEngagementCaseObj
        .createForeignApplication(bdmCreateModifyFA);

    // Link Attachment to FA
    bdmFAAttachmentDetails.selectedFAList =
      bdmForeignApplicationKey.fApplicationID + "";

    // Click Save linking to FA
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kSave;

    boolean caught = false;
    try {

      attachmentIDs = kBdmForeignEngagementCaseObj
        .createFECaseAttachmentLinkingFA(bdmFAAttachmentDetails);

    } catch (final Exception e) {
      caught = true;

      assertEquals(BDMFEC.ERR_SELECT_FA_TO_LINK.getMessageText(),
        e.getMessage());
    }
    assertFalse(caught);

    assertTrue(attachmentIDs.feAttachmentLinkID != 0);

    final BDMFAAttachmentKey bdmFAAttachmentKey = new BDMFAAttachmentKey();

    bdmFAAttachmentKey.attachmentID = attachmentIDs.attachmentID;
    bdmFAAttachmentKey.feAttachmentLinkID = attachmentIDs.feAttachmentLinkID;

    kBdmForeignEngagementCaseObj.unlinkFAFromAttachment(bdmFAAttachmentKey);

  }

  /**
   * This is the utility method to register person for the test class.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private PersonRegistrationResult registerPersonWithContactPref()
    throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "HAMED";
    bdmPersonRegistrationDetails.dtls.surname = "MOHAMMED";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");
    // bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "472723303";
    // bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
    // CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "207";
    addressFieldDetails.addressLine1 = "6511";
    addressFieldDetails.addressLine2 = "GILBER RD";
    addressFieldDetails.province = PROVINCETYPE.BRITISHCOLUMBIA;
    addressFieldDetails.city = "RICHMOND";
    addressFieldDetails.postalCode = "V7C 3V9";
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

    final BDMUtil bdmUtil = new BDMUtil();
    bdmUtil.createContactPreferenceEvidence(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMLANGUAGE.ENGLISHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);
    return registrationResult;

  }

  /**
   * This is a utility method to create a FE Integrated Case for Test.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private CreateIntegratedCaseResultAndMessages createAFE_IC_forTest(
    final long concernRoleID) throws AppException, InformationalException {

    // BEGIN: create FE IC for the person
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = BDMSOURCECOUNTRY.IRELAND;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmForeignEngagementCaseObj.createFEIntegratedCase(details);
    // END: create FE IC for the person

    return createIntegratedCaseResultAndMessages;
  }

  @Test
  public void testCreateForeignApplication()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerPersonWithContactPref();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();

    kBdmForeignEngagementCaseObj.createForeignApplication(bdmCreateModifyFA);

    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    final BDMFAKeyStruct3 bdmfaKeyStruct3 = new BDMFAKeyStruct3();
    bdmfaKeyStruct3.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    final BDMForeignApplicationDtlsList bdmfaDtlsList =
      bdmfa.readByCaseID(bdmfaKeyStruct3);

    if (bdmfaDtlsList.dtls.size() == 1) {

      assertTrue(!bdmfaDtlsList.dtls.item(0).bessInd);
      assertTrue(bdmfaDtlsList.dtls.item(0).comments
        .equals(bdmCreateModifyFA.comments));
      assertTrue(bdmfaDtlsList.dtls.item(0).typeCode
        .equals(bdmCreateModifyFA.typeCode));
      assertTrue(!StringUtil
        .isNullOrEmpty(bdmfaDtlsList.dtls.item(0).faReferenceNumber));
      assertTrue(
        bdmfaDtlsList.dtls.item(0).status.equals(BDMFOREIGNAPPSTATUS.OPEN));
      assertTrue(
        bdmfaDtlsList.dtls.item(0).receiveDate.equals(Date.getCurrentDate()));

      final BDMFAHistory bdmfaHistory = BDMFAHistoryFactory.newInstance();
      final BDMFAHistoryKeyStruct1 bdmfaHistoryKeyStruct1 =
        new BDMFAHistoryKeyStruct1();
      bdmfaHistoryKeyStruct1.fApplicationID =
        bdmfaDtlsList.dtls.item(0).fApplicationID;
      final BDMFAHistoryDtlsList bdmfaHistoryDtlsList =
        bdmfaHistory.readByForeignApplicationID(bdmfaHistoryKeyStruct1);

      if (bdmfaHistoryDtlsList.dtls.size() == 1) {

        assertTrue(!bdmfaHistoryDtlsList.dtls.item(0).bessInd);
        assertTrue(bdmfaHistoryDtlsList.dtls.item(0).comments
          .equals(bdmCreateModifyFA.comments));
        assertTrue(bdmfaHistoryDtlsList.dtls.item(0).typeCode
          .equals(bdmCreateModifyFA.typeCode));
        assertTrue(!StringUtil.isNullOrEmpty(
          bdmfaHistoryDtlsList.dtls.item(0).faReferenceNumber));
        assertTrue(bdmfaHistoryDtlsList.dtls.item(0).status
          .equals(BDMFOREIGNAPPSTATUS.OPEN));
        assertTrue(bdmfaHistoryDtlsList.dtls.item(0).receiveDate
          .equals(Date.getCurrentDate()));

      }

    }

  }

  @Test
  public void testModifyForeignApplication()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerPersonWithContactPref();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();

    kBdmForeignEngagementCaseObj.createForeignApplication(bdmCreateModifyFA);

    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    final BDMFAKeyStruct3 bdmfaKeyStruct3 = new BDMFAKeyStruct3();
    bdmfaKeyStruct3.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    BDMForeignApplicationDtlsList bdmfaDtlsList =
      bdmfa.readByCaseID(bdmfaKeyStruct3);

    if (bdmfaDtlsList.dtls.size() == 1) {

      bdmCreateModifyFA = new BDMCreateModifyFA();
      bdmCreateModifyFA.caseID = bdmfaDtlsList.dtls.item(0).caseID;
      bdmCreateModifyFA.fApplicationID =
        bdmfaDtlsList.dtls.item(0).fApplicationID;
      bdmCreateModifyFA.comments =
        "This comments entered on the Edit Foreign Application screen";
      bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.GUARDIANS_PAYMENT;
      bdmCreateModifyFA.bessInd = true;
      bdmCreateModifyFA.receiveDate = Date.getCurrentDate().addDays(-1);

      kBdmForeignEngagementCaseObj
        .modifyForeignApplication(bdmCreateModifyFA);

      bdmfaDtlsList = bdmfa.readByCaseID(bdmfaKeyStruct3);

      assertTrue(bdmfaDtlsList.dtls.item(0).bessInd);
      assertTrue(bdmfaDtlsList.dtls.item(0).comments.equals(
        "This comments entered on the Edit Foreign Application screen"));
      assertTrue(bdmfaDtlsList.dtls.item(0).typeCode
        .equals(BDMFOREIGNAPPTYPE.GUARDIANS_PAYMENT));
      assertTrue(!StringUtil
        .isNullOrEmpty(bdmfaDtlsList.dtls.item(0).faReferenceNumber));
      assertTrue(
        bdmfaDtlsList.dtls.item(0).status.equals(BDMFOREIGNAPPSTATUS.OPEN));
      assertTrue(bdmfaDtlsList.dtls.item(0).receiveDate
        .equals(Date.getCurrentDate().addDays(-1)));

      final BDMFAHistory bdmfaHistory = BDMFAHistoryFactory.newInstance();
      final BDMFAHistoryKeyStruct1 bdmfaHistoryKeyStruct1 =
        new BDMFAHistoryKeyStruct1();
      bdmfaHistoryKeyStruct1.fApplicationID =
        bdmfaDtlsList.dtls.item(0).fApplicationID;
      final BDMFAHistoryDtlsList bdmfaHistoryDtlsList =
        bdmfaHistory.readByForeignApplicationID(bdmfaHistoryKeyStruct1);

      if (bdmfaHistoryDtlsList.dtls.size() == 2) {

        assertTrue(bdmfaHistoryDtlsList.dtls.item(1).bessInd);
        assertTrue(bdmfaHistoryDtlsList.dtls.item(1).comments.equals(
          "This comments entered on the Edit Foreign Application screen"));
        assertTrue(bdmfaHistoryDtlsList.dtls.item(1).typeCode
          .equals(BDMFOREIGNAPPTYPE.GUARDIANS_PAYMENT));
        assertTrue(!StringUtil.isNullOrEmpty(
          bdmfaHistoryDtlsList.dtls.item(1).faReferenceNumber));
        assertTrue(bdmfaHistoryDtlsList.dtls.item(1).status
          .equals(BDMFOREIGNAPPSTATUS.OPEN));
        assertTrue(bdmfaHistoryDtlsList.dtls.item(1).receiveDate
          .equals(Date.getCurrentDate().addDays(-1)));

      }

    }

  }

  @Test
  public void testReadForeignApplication()
    throws AppException, InformationalException {

    final BDMForeignApplicationKey bdmfaKey = createFA();

    final curam.ca.gc.bdm.facade.fec.struct.BDMFAKey bdmfaKey2 =
      new curam.ca.gc.bdm.facade.fec.struct.BDMFAKey();
    bdmfaKey2.assign(bdmfaKey);
    final BDMFADetails bdmfaDetails =
      kBdmForeignEngagementCaseObj.readForeignApplication(bdmfaKey2);

    assertTrue(bdmfaDetails.fApplicationID != CuramConst.gkZero);
    assertTrue(!StringUtil.isNullOrEmpty(bdmfaDetails.faReferenceNumber));
    assertTrue(bdmfaDetails.recordStatus.equals(RECORDSTATUS.NORMAL));
    assertTrue(bdmfaDetails.typeCode.equals(BDMFOREIGNAPPTYPE.INVALIDITY));
    assertTrue(bdmfaDetails.receiveDate.equals(Date.getCurrentDate()));
    assertTrue(bdmfaDetails.comments
      .equals("This comments entered on the New Foreign Application screen"));

  }

  /**
   * This Utility method helps creating a foreign application.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private BDMForeignApplicationKey createFA()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerPersonWithContactPref();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();

    return kBdmForeignEngagementCaseObj
      .createForeignApplication(bdmCreateModifyFA);
  }

  @Test
  public void testListForeignApplications()
    throws AppException, InformationalException {

    final BDMCaseKey bdmCaseKey = new BDMCaseKey();
    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    final BDMForeignApplicationKey bdmfaKey = createFA();
    final BDMForeignApplicationDtls bdmfaDtls = bdmfa.read(bdmfaKey);

    bdmCaseKey.caseID = bdmfaDtls.caseID;

    final BDMFAList bdmfaList =
      kBdmForeignEngagementCaseObj.listForeignApplications(bdmCaseKey);

    assertTrue(bdmfaList.bdmFADetails.size() == 1);

  }

  @Test
  public void testViewForeignApplication()
    throws AppException, InformationalException {

    final BDMForeignApplicationKey bdmfaKey = createFA();

    final curam.ca.gc.bdm.facade.fec.struct.BDMFAKey bdmfaKey2 =
      new curam.ca.gc.bdm.facade.fec.struct.BDMFAKey();
    bdmfaKey2.fApplicationID = bdmfaKey.fApplicationID;

    final BDMFADetails bdmfaDetails =
      kBdmForeignEngagementCaseObj.viewForeignApplication(bdmfaKey2);

    assertTrue(!bdmfaDetails.bessInd);
    assertTrue(bdmfaDetails.bessIndStr.equals("No"));
    assertTrue(!StringUtil.isNullOrEmpty(bdmfaDetails.faReferenceNumber));
    assertTrue(bdmfaDetails.recordStatus.equals(RECORDSTATUS.NORMAL));
    assertTrue(bdmfaDetails.typeCode.equals(BDMFOREIGNAPPTYPE.INVALIDITY));
    assertTrue(bdmfaDetails.status.equals(BDMFOREIGNAPPSTATUS.OPEN));
    assertTrue(bdmfaDetails.receiveDate.equals(Date.getCurrentDate()));
    assertTrue(bdmfaDetails.comments
      .equals("This comments entered on the New Foreign Application screen"));
  }

  @Test
  public void testDeleteForeignApplication()
    throws AppException, InformationalException {

    final BDMForeignApplicationKey bdmfaKey = createFA();

    final BDMDeleteFADetails bdmDeleteFADetails = new BDMDeleteFADetails();
    bdmDeleteFADetails.fApplicationID = bdmfaKey.fApplicationID;
    bdmDeleteFADetails.bdmDeleteFAReason =
      BDMFOREIGNAPPDELETEREASON.CLIENT_WITHDRAWN;

    kBdmForeignEngagementCaseObj.deleteForeignApplication(bdmDeleteFADetails);

    final curam.ca.gc.bdm.facade.fec.struct.BDMFAKey bdmfaKey2 =
      new curam.ca.gc.bdm.facade.fec.struct.BDMFAKey();
    bdmfaKey2.fApplicationID = bdmfaKey.fApplicationID;

    final BDMFADetails bdmfaDetails =
      kBdmForeignEngagementCaseObj.viewForeignApplication(bdmfaKey2);

    assertTrue(bdmfaDetails.recordStatus.equals(RECORDSTATUS.CANCELLED));
    assertTrue(bdmfaDetails.faDeleteReason
      .equals(BDMFOREIGNAPPDELETEREASON.CLIENT_WITHDRAWN));

  }

  @Test
  public void testDeleteForeignApplication_exception()
    throws AppException, InformationalException {

    final BDMForeignApplicationKey bdmfaKey = createFA();

    final BDMDeleteFADetails bdmDeleteFADetails = new BDMDeleteFADetails();
    bdmDeleteFADetails.fApplicationID = bdmfaKey.fApplicationID;
    bdmDeleteFADetails.bdmDeleteFAReason =
      BDMFOREIGNAPPDELETEREASON.CLIENT_WITHDRAWN;

    kBdmForeignEngagementCaseObj.deleteForeignApplication(bdmDeleteFADetails);

    final curam.ca.gc.bdm.facade.fec.struct.BDMFAKey bdmfaKey2 =
      new curam.ca.gc.bdm.facade.fec.struct.BDMFAKey();
    bdmfaKey2.fApplicationID = bdmfaKey.fApplicationID;

    final BDMFADetails bdmfaDetails =
      kBdmForeignEngagementCaseObj.viewForeignApplication(bdmfaKey2);

    assertTrue(bdmfaDetails.recordStatus.equals(RECORDSTATUS.CANCELLED));
    assertTrue(bdmfaDetails.faDeleteReason
      .equals(BDMFOREIGNAPPDELETEREASON.CLIENT_WITHDRAWN));

    boolean caught = false;
    try {
      kBdmForeignEngagementCaseObj
        .deleteForeignApplication(bdmDeleteFADetails);
    } catch (final Exception e) {
      caught = true;
      assertEquals(ERR_YOU_CANNOT_DELETE_THIS_RECORD_ALREADY_DELETED,
        e.getMessage());
    }

    assertTrue(caught);

  }

  @Test
  public void testListFAHistory()
    throws AppException, InformationalException {

    final BDMForeignApplicationKey bdmfaKey = createFA();

    final curam.ca.gc.bdm.facade.fec.struct.BDMFAKey bdmfaKey2 =
      new curam.ca.gc.bdm.facade.fec.struct.BDMFAKey();

    bdmfaKey2.assign(bdmfaKey);

    final BDMFAList bdmfaList =
      kBdmForeignEngagementCaseObj.listFAHistory(bdmfaKey2);

    assertTrue(
      bdmfaList.bdmFADetails.item(0).fAppHistoryID != CuramConst.gkZero);
    assertTrue(bdmfaList.bdmFADetails.size() == 1);

  }

  @Test
  public void testCreateFECaseAttachment()
    throws AppException, InformationalException {

    final BDMForeignApplicationKey bdmfaKey = createFA();

    final BDMFAAttachmentDetails bdmfaAttachmentDetails =
      new BDMFAAttachmentDetails();

    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    final BDMForeignApplicationDtls bdmfaDtls = bdmfa.read(bdmfaKey);

    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      bdmfaDtls.caseID;

    bdmfaAttachmentDetails.actionIDProperty = BDMConstants.kSaveAndClose;
    bdmfaAttachmentDetails.selectedFAList = bdmfaKey.fApplicationID + "\t";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      "Ottawa-File-Archieve";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      "Ottawa/Shelf001/FileName001";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Description for Ottawa/Shelf001/FileName001";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.DIRECT_DEPOSIT;
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Description";
    BDMAttachmentIDs bdmAttachmentIDs = kBdmForeignEngagementCaseObj
      .createFECaseAttachment(bdmfaAttachmentDetails);

    // bdmfaAttachmentDetails.actionIDProperty = BDMConstants.kSave;
    bdmfaAttachmentDetails.wizardStateID = bdmAttachmentIDs.wizardStateID;

    bdmAttachmentIDs = kBdmForeignEngagementCaseObj
      .createFECaseAttachment(bdmfaAttachmentDetails);

    assertTrue(bdmAttachmentIDs.attachmentID != 0);
    assertTrue(bdmAttachmentIDs.caseAttachmentLinkID != 0);

    final ReadCaseAttachmentKey readCaseAttachmentKey =
      new ReadCaseAttachmentKey();
    readCaseAttachmentKey.readCaseAttachmentIn.attachmentID =
      bdmAttachmentIDs.attachmentID;
    readCaseAttachmentKey.readCaseAttachmentIn.caseAttachmentLinkID =
      bdmAttachmentIDs.caseAttachmentLinkID;
    final BDMReadCaseAttachmentDetails readAttachment =
      BDMCaseFactory.newInstance().readCaseAttachment(readCaseAttachmentKey);

    assertEquals(BDM_FILE_SOURCE.CLIENT, readAttachment.fileSource);
    assertEquals("Ottawa-File-Archieve",
      readAttachment.readCaseAttchDtls.readCaseAttachmentOut.fileLocation);
    assertEquals("Ottawa/Shelf001/FileName001",
      readAttachment.readCaseAttchDtls.readCaseAttachmentOut.fileReference);
    assertEquals(DOCUMENTTYPE.DIRECT_DEPOSIT,
      readAttachment.readCaseAttchDtls.readCaseAttachmentOut.documentType);
    assertEquals("Description",
      readAttachment.readCaseAttchDtls.readCaseAttachmentOut.description);

  }

  @Test
  public void testModifyFECaseAttachment()
    throws AppException, InformationalException {

    final BDMForeignApplicationKey bdmfaKey = createFA();

    BDMFAAttachmentDetails bdmfaAttachmentDetails =
      new BDMFAAttachmentDetails();

    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    final BDMForeignApplicationDtls bdmfaDtls = bdmfa.read(bdmfaKey);

    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      bdmfaDtls.caseID;

    bdmfaAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmfaAttachmentDetails.selectedFAList = bdmfaKey.fApplicationID + "\t";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      "Ottawa-File-Archieve";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      "Ottawa/Shelf001/FileName001";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Description for Ottawa/Shelf001/FileName001";

    BDMAttachmentIDs bdmAttachmentIDs = kBdmForeignEngagementCaseObj
      .createFECaseAttachmentLinkingFA(bdmfaAttachmentDetails);

    bdmfaAttachmentDetails.actionIDProperty = BDMConstants.kSave;
    bdmfaAttachmentDetails.wizardStateID = bdmAttachmentIDs.wizardStateID;

    bdmAttachmentIDs = kBdmForeignEngagementCaseObj
      .createFECaseAttachment(bdmfaAttachmentDetails);

    bdmAttachmentIDs.caseID = bdmfaDtls.caseID;

    final BDMFAAttachmentDetailsForRead attachmentDetailsForRead =
      kBdmForeignEngagementCaseObj
        .readFECAttachmentWizardForModify(bdmAttachmentIDs);

    bdmfaAttachmentDetails = new BDMFAAttachmentDetails();

    bdmfaAttachmentDetails.assign(attachmentDetailsForRead);
    bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails
      .assign(
        attachmentDetailsForRead.bdmReadAttachmentDetails.dtls.attachmentDetails.readCaseAttachmentOut);

    bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.caseID =
      bdmfaDtls.caseID;
    bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.attachmentID =
      bdmAttachmentIDs.attachmentID;
    bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.caseAttachmentLinkID =
      bdmAttachmentIDs.caseAttachmentLinkID;

    bdmfaAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmfaAttachmentDetails.selectedFAList = bdmfaKey.fApplicationID + "\t";
    bdmfaAttachmentDetails.bdmModifyAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.fileLocation =
      "Ottawa-File-Archieve-For-Edit-Attachment";
    bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.fileReference =
      "Shelf001/ForEditAttachment001";
    bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.description =
      "Description for Ottawa/Shelf001/FileName001/ForEditAttachment001";

    bdmAttachmentIDs = new BDMAttachmentIDs();

    bdmAttachmentIDs = kBdmForeignEngagementCaseObj
      .modifyFECaseAttachment(bdmfaAttachmentDetails);

    bdmfaAttachmentDetails.actionIDProperty = BDMConstants.kSave;
    bdmfaAttachmentDetails.wizardStateID = bdmAttachmentIDs.wizardStateID;

    bdmAttachmentIDs = kBdmForeignEngagementCaseObj
      .modifyFECaseAttachment(bdmfaAttachmentDetails);

    assertTrue(bdmAttachmentIDs.attachmentID != CuramConst.gkZero);
    assertTrue(bdmAttachmentIDs.caseAttachmentLinkID != CuramConst.gkZero);
    assertTrue(bdmAttachmentIDs.feAttachmentLinkID != CuramConst.gkZero);

    final BDMFEAttachmentLink bdmfeAttachmentLink =
      BDMFEAttachmentLinkFactory.newInstance();
    final BDMFEAttachmentLinkKey bdmfeAttachmentLinkKey =
      new BDMFEAttachmentLinkKey();
    bdmfeAttachmentLinkKey.feAttachmentLinkID =
      bdmAttachmentIDs.feAttachmentLinkID;

    final BDMFEAttachmentLinkDtls bdmfeAttachmentLinkDtls =
      bdmfeAttachmentLink.read(bdmfeAttachmentLinkKey);

    assertTrue(bdmfeAttachmentLinkDtls.attachmentLinkTypCd
      .equals(BDMATTACHMENTLINKTYPE.FAATTACHMENTLINK));

    final curam.core.intf.Attachment attachment =
      AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();
    attachmentKey.attachmentID = bdmAttachmentIDs.attachmentID;
    final AttachmentDtls attachmentDtls = attachment.read(attachmentKey);

    assertTrue(attachmentDtls.fileLocation.equals(
      bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.fileLocation));
    assertTrue(attachmentDtls.fileReference.equals(
      bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.fileReference));
    assertTrue(bdmfeAttachmentLinkDtls.description.equals(
      bdmfaAttachmentDetails.bdmModifyAttachmentDetails.dtls.modifyAttachmentDetails.description));

  }

  @Test
  public void testReadFECAttachmentWizardForModify()
    throws AppException, InformationalException {

    final BDMForeignApplicationKey bdmfaKey = createFA();

    final BDMFAAttachmentDetails bdmfaAttachmentDetails =
      new BDMFAAttachmentDetails();

    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    final BDMForeignApplicationDtls bdmfaDtls = bdmfa.read(bdmfaKey);

    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      bdmfaDtls.caseID;

    bdmfaAttachmentDetails.actionIDProperty = BDMConstants.kSaveAndClose;
    bdmfaAttachmentDetails.selectedFAList = bdmfaKey.fApplicationID + "\t";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      "Ottawa-File-Archieve";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      "Ottawa/Shelf001/FileName001";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Description for Ottawa/Shelf001/FileName001";
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.DIRECT_DEPOSIT;
    bdmfaAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Description";
    BDMAttachmentIDs bdmAttachmentIDs = kBdmForeignEngagementCaseObj
      .createFECaseAttachment(bdmfaAttachmentDetails);

    // bdmfaAttachmentDetails.actionIDProperty = BDMConstants.kSave;
    bdmfaAttachmentDetails.wizardStateID = bdmAttachmentIDs.wizardStateID;

    bdmAttachmentIDs = kBdmForeignEngagementCaseObj
      .createFECaseAttachment(bdmfaAttachmentDetails);

    BDMFAAttachmentDetailsForRead bdmFAAttachmentDetailsForRead =
      new BDMFAAttachmentDetailsForRead();
    // Create Attachemnt
    bdmAttachmentIDs =
      createFECAttachment(bdmfaDtls.caseID, BDMConstants.kSaveAndClose,
        Date.getCurrentDate(), DOCUMENTTYPE.DIRECT_DEPOSIT);

    bdmAttachmentIDs.caseID = bdmfaDtls.caseID;
    // Read attachment
    bdmFAAttachmentDetailsForRead = kBdmForeignEngagementCaseObj
      .readFECAttachmentWizardForModify(bdmAttachmentIDs);

    assertEquals(FILE_REFERENCE,
      bdmFAAttachmentDetailsForRead.bdmReadAttachmentDetails.dtls.attachmentDetails.readCaseAttachmentOut.fileReference);

    assertEquals(BDM_FILE_SOURCE.CLIENT,
      bdmFAAttachmentDetailsForRead.bdmReadAttachmentDetails.fileSource);

  }

  @Test
  public void testViewForeignApplicationHistory()
    throws AppException, InformationalException {

    final BDMForeignApplicationKey bdmfaKey = createFA();

    final BDMFAHistoryKeyStruct1 bdmfaHistoryKeyStruct1 =
      new BDMFAHistoryKeyStruct1();
    bdmfaHistoryKeyStruct1.fApplicationID = bdmfaKey.fApplicationID;

    final BDMFAHistory bdmfaHistory = BDMFAHistoryFactory.newInstance();
    final BDMFAHistoryDtlsList bdmfaHistoryDtlsList =
      bdmfaHistory.readByForeignApplicationID(bdmfaHistoryKeyStruct1);

    if (bdmfaHistoryDtlsList.dtls.size() == 1) {

      final BDMFAHistoryKey bdmfaHistoryKey = new BDMFAHistoryKey();
      bdmfaHistoryKey.fAppHistoryID =
        bdmfaHistoryDtlsList.dtls.item(0).fAppHistoryID;

      final BDMFADetails bdmfaDetails = kBdmForeignEngagementCaseObj
        .viewForeignApplicationHistory(bdmfaHistoryKey);

      assertTrue(!bdmfaDetails.bessInd);
      assertTrue(bdmfaDetails.bessIndStr.equals("No"));
      assertTrue(!StringUtil.isNullOrEmpty(bdmfaDetails.faReferenceNumber));
      assertTrue(bdmfaDetails.recordStatus.equals(RECORDSTATUS.NORMAL));
      assertTrue(bdmfaDetails.typeCode.equals(BDMFOREIGNAPPTYPE.INVALIDITY));
      assertTrue(bdmfaDetails.status.equals(BDMFOREIGNAPPSTATUS.OPEN));
      assertTrue(bdmfaDetails.receiveDate.equals(Date.getCurrentDate()));
      assertTrue(bdmfaDetails.comments.equals(
        "This comments entered on the New Foreign Application screen"));

    }

  }

  /**
   * Test fetch list of linked foreign applications with an
   * attachment.
   */
  @Test
  public void testgetFAListLinkedToAttachment()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPersonWithContactPref();
    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createAFE_IC_forTest(person.registrationIDDetails.concernRoleID);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmForeignEngagementCaseObj
        .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Create Attachemnt of type Foreign Application

    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      FILE_DESCRIPTION;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.DIRECT_DEPOSIT;

    attachmentIDs = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment details from Step1
    bdmFAAttachmentDetails = kBdmForeignEngagementCaseObj
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Set data for foreign application
    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    bdmCreateModifyFA.comments = "FEC Comments";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();
    bdmCreateModifyFA.consent = BDMYESNO.NO;

    // Create FA
    final BDMForeignApplicationKey bdmForeignApplicationKey =
      kBdmForeignEngagementCaseObj
        .createForeignApplication(bdmCreateModifyFA);

    // Link Attachment to FA
    bdmFAAttachmentDetails.selectedFAList =
      bdmForeignApplicationKey.fApplicationID + "";

    // Click Save linking to FA
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kSave;

    attachmentIDs = kBdmForeignEngagementCaseObj
      .createFECaseAttachmentLinkingFA(bdmFAAttachmentDetails);

    BDMFAList list = new BDMFAList();
    final BDMFAAttachmentKey key = new BDMFAAttachmentKey();
    ////////
    key.attachmentID = attachmentIDs.attachmentID;

    list = kBdmForeignEngagementCaseObj.getFAListLinkedToAttachment(key);

    assertTrue(!list.bdmFADetails.isEmpty());

    assertTrue(list.bdmFADetails.size() == 1);
    assertEquals(list.bdmFADetails.get(0).fApplicationID,
      bdmForeignApplicationKey.fApplicationID);

  }

  @Test
  public void testReadCountryCode()
    throws AppException, InformationalException {

    final CaseHeaderKey key = new CaseHeaderKey();

    final PersonRegistrationResult person = registerPersonWithContactPref();
    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createAFE_IC_forTest(person.registrationIDDetails.concernRoleID);

    key.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();

    bdmfeCaseDtls = kBdmForeignEngagementCaseObj.readCountryCode(key);

    assertEquals("IE", bdmfeCaseDtls.countryCode);
  }

  /**
   * test fetching the list of foreign offices for the selected SSA
   * country.
   */
  @Test
  public void testGetListOfForeignOffices()
    throws AppException, InformationalException {

    final BDMFACountryCodeKey key = new BDMFACountryCodeKey();
    key.countryCode = BDMSOURCECOUNTRY.US;

    final BDMListOfOffices list =
      kBdmForeignEngagementCaseObj.getListOfForeignOffices(key);

    assertTrue(!list.bdmFO.isEmpty());

  }

  @Test
  public void testGetListOfFAApplicationTypesByCountryCode()
    throws AppException, InformationalException {

    final BDMFACountryCodeKey key = new BDMFACountryCodeKey();
    key.countryCode = BDMSOURCECOUNTRY.JAPAN;

    final BDMListOfFAApplicationTypes list = kBdmForeignEngagementCaseObj
      .getListOfFAApplicationTypesByCountryCode(key);

    assertTrue(!list.bdmFaApplType.isEmpty());

  }

  // TODO: no implementation for the method in class
  @Test
  public void testreadFAAttachmentDetails()
    throws AppException, InformationalException {

    BDMFAViewAttachmentDetails bdmfaViewAttachmentDetails =
      new BDMFAViewAttachmentDetails();

    final BDMFAAttachmentKey key = new BDMFAAttachmentKey();

    bdmfaViewAttachmentDetails =
      kBdmForeignEngagementCaseObj.readFAAttachmentDetails(key);

  }

  // TODO: no implementation for the method in class
  @Test
  public void testlistFAAttachments()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPersonWithContactPref();
    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createAFE_IC_forTest(person.registrationIDDetails.concernRoleID);

    BDMListICAttachmentDetails list = new BDMListICAttachmentDetails();
    final BDMCaseKey key = new BDMCaseKey();
    key.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    list = kBdmForeignEngagementCaseObj.listFAAttachments(key);

  }

  @Test
  public void testCreateForeignApplication_exception_recievedDate()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerPersonWithContactPref();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    // receiveDate in future
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate().addDays(1);

    boolean caught = false;

    try {
      kBdmForeignEngagementCaseObj
        .createForeignApplication(bdmCreateModifyFA);

    } catch (final Exception e) {
      caught = true;
      assertEquals(ERR_RECEIVED_DATE_CANNOT_BE_IN_FUTURE, e.getMessage());
    }
    assertTrue(caught);
  }

  @Test
  public void testCreateForeignApplication_exception_interim()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerPersonWithContactPref();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.interimChkStrList = BDMFOREIGNAPPINTERIM.OTHER;
    bdmCreateModifyFA.interimOther = CuramConst.gkEmpty;
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM;

    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();

    boolean caught = false;

    try {
      kBdmForeignEngagementCaseObj
        .createForeignApplication(bdmCreateModifyFA);

    } catch (final Exception e) {
      caught = true;
      assertEquals(ERR_INTERIM_CHK_BOX_SELECTED_SPECIFY_NOT_ENTERED,
        e.getMessage());
    }
    assertTrue(caught);

  }

  @Test
  public void testCreateForeignApplication_exception_Interim_Other()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerPersonWithContactPref();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.interimChkStrList = BDMFOREIGNAPPINTERIM.OTHER;
    bdmCreateModifyFA.interimOther = CuramConst.gkEmpty;
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();

    boolean caught = false;

    try {
      kBdmForeignEngagementCaseObj
        .createForeignApplication(bdmCreateModifyFA);

    } catch (final Exception e) {
      caught = true;
      assertEquals(ERR_INTERIM_CHK_BOX_SELECTED_SPECIFY_NOT_ENTERED,
        e.getMessage());
    }
    assertTrue(caught);

  }

  @Test
  public void testCreateForeignApplication_exception_Bess()
    throws AppException, InformationalException {

    final PersonRegistrationResult registrationResult =
      registerPersonWithContactPref();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";

    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM;
    bdmCreateModifyFA.bessInd = true;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();

    boolean caught = false;

    try {
      kBdmForeignEngagementCaseObj
        .createForeignApplication(bdmCreateModifyFA);

    } catch (final Exception e) {
      caught = true;
      assertEquals(ERR_INTERIM_CHK_BOX_SELECTED_BESS_CANNOT_BE_SELECTED,
        e.getMessage());
    }
    assertTrue(caught);

  }

  @Test
  public void testlistFAInterimCodeDetails()
    throws AppException, InformationalException {

    final BDMFAInterimCodeDetailsList list =
      kBdmForeignEngagementCaseObj.listFAInterimCodeDetails();

    assertTrue(!list.bdmFAInterimCodeDetails.isEmpty());

  }

  /**
   * list of foreign identifier for the
   * selected person concern role ID.
   */
  @Test
  public void getListOfForeignIdentifiers()
    throws AppException, InformationalException {

    final BDMEvidenceUtilsTest evidenceUtilsTest = new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    // create new Identification evidence with some data ,
    final EIEvidenceKey str =
      evidenceUtilsTest.createForeignIdentificationEvidence(pdcPersonDetails,
        "F1234567", CONCERNROLEALTERNATEID.BDM_FOREIGN_IDENTIFIER, false,
        BDMSOURCECOUNTRY.US);

    final BDMFAConcernRoleKey key = new BDMFAConcernRoleKey();
    key.concernRoleID = pdcPersonDetails.concernRoleID;

    final BDMListOfForeignIDs bdmListOfForeignIDs =
      kBdmForeignEngagementCaseObj.getListOfForeignIdentifiers(key);

    assertTrue(!bdmListOfForeignIDs.bdmFId.isEmpty());

    assertEquals("F1234567", bdmListOfForeignIDs.bdmFId.get(0).alternateID);
  }

  /**
   * canceling/deleting an foreign engagement case
   * attachment.
   */
  @Test
  public void testcancelFECaseAttachment()
    throws AppException, InformationalException {

    final curam.core.facade.struct.CancelCaseAttachmentKey key =
      new curam.core.facade.struct.CancelCaseAttachmentKey();

    final PersonRegistrationResult registrationResult =
      registerPersonWithContactPref();

    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createAFE_IC_forTest(
        registrationResult.registrationIDDetails.concernRoleID);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmForeignEngagementCaseObj
        .readFECaseAttachmentWizardDetails(attachmentIDs);
    // Create Attachemnt
    attachmentIDs = createFECAttachment(
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID,
      BDMConstants.kSaveAndClose, Date.getCurrentDate(),
      DOCUMENTTYPE.DIRECT_DEPOSIT);

    // Read attachment
    bdmFAAttachmentDetails = kBdmForeignEngagementCaseObj
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    final ReadCaseAttachmentKey readCaseAttachmentKey =
      new ReadCaseAttachmentKey();
    readCaseAttachmentKey.readCaseAttachmentIn.attachmentID =
      attachmentIDs.attachmentID;
    readCaseAttachmentKey.readCaseAttachmentIn.caseAttachmentLinkID =
      attachmentIDs.caseAttachmentLinkID;
    final BDMReadCaseAttachmentDetails readAttachment =
      BDMCaseFactory.newInstance().readCaseAttachment(readCaseAttachmentKey);

    assertEquals(BDM_FILE_SOURCE.CLIENT, readAttachment.fileSource);

    final CancelCaseAttachmentKey cancelCaseAttachmentKey =
      new CancelCaseAttachmentKey();
    cancelCaseAttachmentKey.cancelCaseAttachmentKey.attachmentID =
      readCaseAttachmentKey.readCaseAttachmentIn.attachmentID;
    cancelCaseAttachmentKey.cancelCaseAttachmentKey.caseAttachmentLinkID =
      readCaseAttachmentKey.readCaseAttachmentIn.caseAttachmentLinkID;
    cancelCaseAttachmentKey.cancelCaseAttachmentKey.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    kBdmForeignEngagementCaseObj
      .cancelFECaseAttachment(cancelCaseAttachmentKey);

    final curam.core.intf.Attachment attachment =
      AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();
    attachmentKey.attachmentID = attachmentIDs.attachmentID;
    final AttachmentDtls attachmentDtls = attachment.read(attachmentKey);

    assertTrue(attachmentDtls.statusCode.equals(RECORDSTATUS.CANCELLED));

  }

  /**
   * n creating Foreign Application attachment linking
   * to Foreign Application
   */
  @Test
  public void testCreateFECaseAttachmentLinkingFA_kSave()
    throws AppException, InformationalException {

    // Register person Create FEc case and create Fpreign Application

    final BDMForeignApplicationKey faKey = createFA();

    final BDMFAKey bdmFAKey = new BDMFAKey();
    bdmFAKey.fApplicationID = faKey.fApplicationID;

    // Read Foreign Application details
    final BDMFADetails readFADetails =
      kBdmForeignEngagementCaseObj.readForeignApplication(bdmFAKey);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmForeignEngagementCaseObj
        .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Create Attachemnt of type Foreign Application and click next
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      readFADetails.caseID;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      FILE_DESCRIPTION;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.FOREIGN_APPLICATION_ADDITIONAL_DOCUMENTS;

    attachmentIDs = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment details from Step1
    bdmFAAttachmentDetails = kBdmForeignEngagementCaseObj
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Click Save without linking to FA
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kSave;
    attachmentIDs.caseID =
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;

    final BDMFAList fAList = kBdmForeignEngagementCaseObj
      .listFANotLinkedWithAttachment(attachmentIDs);
    bdmFAAttachmentDetails.selectedFAList =
      String.valueOf(fAList.bdmFADetails.get(0).fApplicationID);

    boolean caught = false;
    try {

      attachmentIDs = kBdmForeignEngagementCaseObj
        .createFECaseAttachmentLinkingFA(bdmFAAttachmentDetails);

    } catch (final Exception e) {
      caught = true;

      assertEquals(BDMFEC.ERR_SELECT_FA_TO_LINK.getMessageText(),
        e.getMessage());
    }
    assertFalse(caught);

  }

  @Test
  public void testReserveTask() throws AppException, InformationalException {

    // register person
    final PersonRegistrationResult registrationResult =
      registerPersonWithContactPref();

    // create FE IC for the person
    final CreateIntegratedCaseResultAndMessages integratedCase =
      createAFE_IC_forTest(
        registrationResult.registrationIDDetails.concernRoleID);
    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      integratedCase.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();
    bdmCreateModifyFA.consent = BDMYESNO.NO;

    final BDMForeignApplicationKey fAKey = kBdmForeignEngagementCaseObj
      .createForeignApplication(bdmCreateModifyFA);

    final TaskDtls craetedTask = createTaskData("",
      registrationResult.registrationIDDetails.concernRoleID,
      integratedCase.createCaseResult.integratedCaseID);

    final ReserveTaskDetailsForUser details = new ReserveTaskDetailsForUser();
    details.reserveDetails.comments = "Reserve for caseworker";
    details.reserveDetails.taskID = craetedTask.taskID;
    details.reserveDetails.userName = "caseworker";

    kBdmForeignEngagementCaseObj.reserveTask(details);

    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = craetedTask.taskID;

    final TaskDtls readTask = TaskFactory.newInstance().read(taskKey);

    assertEquals("caseworker", readTask.reservedBy);

  }

  @Test
  public void testcancelFECaseAttachment_exception()
    throws AppException, InformationalException {

    final curam.core.facade.struct.CancelCaseAttachmentKey key =
      new curam.core.facade.struct.CancelCaseAttachmentKey();

    final PersonRegistrationResult registrationResult =
      registerPersonWithContactPref();

    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createAFE_IC_forTest(
        registrationResult.registrationIDDetails.concernRoleID);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    final BDMForeignApplicationKey bdmForeignApplicationKey = createFA();

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmForeignEngagementCaseObj
        .readFECaseAttachmentWizardDetails(attachmentIDs);
    // Create Attachemnt
    attachmentIDs = createFECAttachment(
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID,
      BDMConstants.kSaveAndClose, Date.getCurrentDate(),
      DOCUMENTTYPE.DIRECT_DEPOSIT);

    // Read attachment
    bdmFAAttachmentDetails = kBdmForeignEngagementCaseObj
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    final ReadCaseAttachmentKey readCaseAttachmentKey =
      new ReadCaseAttachmentKey();
    readCaseAttachmentKey.readCaseAttachmentIn.attachmentID =
      attachmentIDs.attachmentID;
    readCaseAttachmentKey.readCaseAttachmentIn.caseAttachmentLinkID =
      attachmentIDs.caseAttachmentLinkID;
    final BDMReadCaseAttachmentDetails readAttachment =
      BDMCaseFactory.newInstance().readCaseAttachment(readCaseAttachmentKey);

    assertEquals(BDM_FILE_SOURCE.CLIENT, readAttachment.fileSource);

    final CancelCaseAttachmentKey cancelCaseAttachmentKey =
      new CancelCaseAttachmentKey();
    cancelCaseAttachmentKey.cancelCaseAttachmentKey.attachmentID =
      readCaseAttachmentKey.readCaseAttachmentIn.attachmentID;
    cancelCaseAttachmentKey.cancelCaseAttachmentKey.caseAttachmentLinkID =
      readCaseAttachmentKey.readCaseAttachmentIn.caseAttachmentLinkID;
    cancelCaseAttachmentKey.cancelCaseAttachmentKey.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    kBdmForeignEngagementCaseObj
      .cancelFECaseAttachment(cancelCaseAttachmentKey);

    final curam.core.intf.Attachment attachment =
      AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();
    attachmentKey.attachmentID = attachmentIDs.attachmentID;
    final AttachmentDtls attachmentDtls = attachment.read(attachmentKey);

    assertTrue(attachmentDtls.statusCode.equals(RECORDSTATUS.CANCELLED));

  }

  /** Utility method to create wizard attachment data */

  private BDMAttachmentIDs createFECAttachment(final long caseID,
    final String actionIDProperty, final Date receiptDate,
    final String documentType) throws AppException, InformationalException {

    final BDMFAAttachmentDetails createFAAttachment =
      new BDMFAAttachmentDetails();

    createFAAttachment.actionIDProperty = actionIDProperty;
    createFAAttachment.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      caseID;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      receiptDate;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Test FEC attachment";
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      caseID;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;

    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;

    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      documentType;

    return BDMForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(createFAAttachment);
  }

  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();
    pdcPersonDetails.assign(personRegistrationDetails);
    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  /** Create Foreign Contribution Period Evidence */

  public curam.core.sl.struct.EvidenceKey
    createForeignContributionPeriodEvidence(final Long caseID,
      final Long cprID, final long concernroleID, final Date startDate,
      final Date endDate) throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_FOREIGN_CONTRIBUTION_PERIOD;

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
      dynamicEvidencedataDetails.getAttribute(CREDITABLE_PERIOD_SOURCE),
      new CodeTableItem(BDMCREDITABLEPERIODSOURCE.TABLENAME,
        BDMCREDITABLEPERIODSOURCE.LIAISON));

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

  /** create Foreign Residence Period Evidence */
  public curam.core.sl.struct.EvidenceKey
    createForeignResidencePeriodEvidence(final Long caseID, final Long cprID,
      final long concernroleID, final Date startDate, final Date endDate)
      throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_FOREIGN_RESIDENCE_PERIOD;

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
      dynamicEvidencedataDetails.getAttribute(CREDITABLE_PERIOD_SOURCE),
      new CodeTableItem(BDMCREDITABLEPERIODSOURCE.TABLENAME,
        BDMCREDITABLEPERIODSOURCE.LIAISON));

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

  /**
   * Creates test data for task with deadline date set
   *
   * @throws AppException
   * @throws InformationalException
   */
  private TaskDtls createTaskData(final String username,
    final long concernRoleID, final long caseID)
    throws AppException, InformationalException {

    // create task object
    final TaskDtls taskCreateDetail = createTaskDtls(username);

    // create task assignment
    createTaskAssigmentDtls(username, taskCreateDetail);

    // Create task deadline
    createWorkFlowDeadlineDtls(taskCreateDetail);

    // Create Biz Obje assocuation
    BizObjAssociationDtls bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = UniqueID.nextUniqueID();
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = concernRoleID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.PERSON;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

    bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = taskCreateDetail.taskID;
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = caseID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.CASE;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);
    return taskCreateDetail;

  }

  /**
   * @param taskCreateDetail
   * @throws AppException
   * @throws InformationalException
   */
  private void createWorkFlowDeadlineDtls(final TaskDtls taskCreateDetail)
    throws AppException, InformationalException {

    final WorkflowDeadlineDtls workflowDeadlineDtls =
      new WorkflowDeadlineDtls();
    workflowDeadlineDtls.deadlineID = UniqueID.nextUniqueID();
    workflowDeadlineDtls.taskID = taskCreateDetail.taskID;
    workflowDeadlineDtls.suspended = false;
    workflowDeadlineDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(24, 0, 0);
    workflowDeadlineDtls.deadlineType = "WDT1";
    workflowDeadlineDtls.versionNo = 1;

    WorkflowDeadlineFactory.newInstance().insert(workflowDeadlineDtls);
  }

  /**
   * @param username
   * @param taskCreateDetail
   * @throws AppException
   * @throws InformationalException
   */
  private void createTaskAssigmentDtls(final String username,
    final TaskDtls taskCreateDetail)
    throws AppException, InformationalException {

    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = username;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.USER;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);
  }

  private TaskDtls createTaskDtls(final String username)
    throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.NOTSTARTED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = username;

    TaskFactory.newInstance().insert(taskCreateDetail);
    return taskCreateDetail;
  }
}
