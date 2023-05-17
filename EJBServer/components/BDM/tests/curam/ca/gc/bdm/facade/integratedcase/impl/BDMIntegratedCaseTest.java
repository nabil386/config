package curam.ca.gc.bdm.facade.integratedcase.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMURGENTFLAGTYPE;
import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.fact.BDMCaseUrgentFlagFactory;
import curam.ca.gc.bdm.facade.bdmcaseurgentflag.struct.BDMCaseUrgentFlagDetails;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.integratedcase.fact.BDMIntegratedCaseFactory;
import curam.ca.gc.bdm.facade.integratedcase.intf.BDMIntegratedCase;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMListICTaskDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.BDMTASKTYPE;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CommunicationFilterKey;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.ListICAttachmentsKey;
import curam.core.facade.struct.ListICProductDeliveryAttachmentKey;
import curam.core.facade.struct.ListICProductDeliveryDeductionKey;
import curam.core.facade.struct.ListICTaskKey_eo;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.WorkflowDeadlineFactory;
import curam.util.workflow.impl.LocalizableStringResolver;
import curam.util.workflow.impl.LocalizableStringResolver.TaskStringResolver;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.TaskWDOSnapshotDetails;
import curam.util.workflow.struct.WorkflowDeadlineDtls;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMIntegratedCaseTest extends CuramServerTestJUnit4 {

  @Mocked
  LocalizableStringResolver localizableStringResolver;

  private static String taskSubject = "Test Task Subject";

  private static String CASEWORKER = "caseworker";

  @Mocked
  TaskStringResolver taskResolver;

  private curam.ca.gc.bdm.facade.integratedcase.intf.BDMIntegratedCase bdmIntegratedCase =
    null;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmIntegratedCase = BDMIntegratedCaseFactory.newInstance();
    taskSubjectResolverExpectation();
  }

  /**
   * Expectation for task Subject
   */
  private void taskSubjectResolverExpectation() {

    try {
      new Expectations() {

        {
          taskResolver.getSubjectForTask((TaskWDOSnapshotDetails) any);
          result = taskSubject;
        }
      };
    } catch (final AppException e) {

      e.printStackTrace();
    } catch (final InformationalException e) {

      e.printStackTrace();
    }
  }

  @Test
  public void testListTask() throws AppException, InformationalException {

    final PersonRegistrationResult perosn =
      registerAPersonForTest("711256743");

    final CreateIntegratedCaseResultAndMessages fecCase =
      createFEC(perosn.registrationIDDetails.concernRoleID);
    final ListICTaskKey_eo key = new ListICTaskKey_eo();
    key.caseTasksKey.caseID = fecCase.createCaseResult.integratedCaseID;

    setUpUrgentFlagData(fecCase.createCaseResult.integratedCaseID,
      BDMURGENTFLAGTYPE.DIRENEED, Date.getCurrentDate());

    createTaskData(CASEWORKER, perosn.registrationIDDetails.concernRoleID,
      fecCase.createCaseResult.integratedCaseID);

    final BDMListICTaskDetails bdmListICTaskDetails =
      bdmIntegratedCase.listTask(key);

    assertTrue(bdmListICTaskDetails.detailsList.dtls.size() == 1);

    // returnTaskDetails.

    assertEquals(taskSubject,
      bdmListICTaskDetails.detailsList.dtls.get(0).subject);
    assertEquals(TASKPRIORITY.NORMAL,
      bdmListICTaskDetails.detailsList.dtls.get(0).priority);
    assertEquals(DateTime.getCurrentDateTime().addTime(24, 0, 0),
      bdmListICTaskDetails.detailsList.dtls.get(0).dueDateTime);
    assertEquals(CASEWORKER,
      bdmListICTaskDetails.detailsList.dtls.get(0).reservedBy);

    assertEquals(
      CodeTable.getOneItem(BDMURGENTFLAGTYPE.TABLENAME,
        BDMURGENTFLAGTYPE.DIRENEED, TransactionInfo.getProgramLocale()),
      bdmListICTaskDetails.detailsList.dtls.get(0).caseUrgentFlagStr);

  }

  /**
   * This is the utility method to register person for the test class.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private PersonRegistrationResult registerAPersonForTest(
    final String nineDigitSIN) throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "John";
    bdmPersonRegistrationDetails.dtls.surname = "Doe";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770102");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = nineDigitSIN;
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

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
   * Creates test data for task with deadline date set
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void createTaskData(final String username, final long concernRoleID,
    final long caseID) throws AppException, InformationalException {

    // create task object
    final TaskDtls taskCreateDetail = createTaskDtls(username);
    final BDMTaskDtls bdmTaskDtls =
      createBDMTaskDtls(taskCreateDetail.taskID);

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

  /**
   * Setup urgent flag data
   *
   * @param caseID
   * @param urgentFlagType
   * @param startDate
   * @throws AppException
   * @throws InformationalException
   */
  private void setUpUrgentFlagData(final long caseID,
    final String urgentFlagType, final Date startDate)
    throws AppException, InformationalException {

    final BDMCaseUrgentFlagDetails flagDtls = new BDMCaseUrgentFlagDetails();
    flagDtls.caseID = caseID;
    TransactionInfo.getInfo();
    flagDtls.createdBy = CASEWORKER;
    TransactionInfo.getInfo();
    flagDtls.createdByFullName = TransactionInfo.getProgramUser();
    flagDtls.recordStatus = RECORDSTATUS.NORMAL;
    flagDtls.startDate = startDate;
    flagDtls.type = urgentFlagType;
    BDMCaseUrgentFlagFactory.newInstance().createCaseUrgentFlag(flagDtls);

  }

  private BDMTaskDtls createBDMTaskDtls(final long taskID)
    throws AppException, InformationalException {

    final BDMTaskDtls bdmTaskCreateDetails = new BDMTaskDtls();

    bdmTaskCreateDetails.taskID = taskID;
    bdmTaskCreateDetails.type = BDMTASKTYPE.BDMFBADDRESS;
    bdmTaskCreateDetails.versionNo = 1;

    BDMTaskFactory.newInstance().insert(bdmTaskCreateDetails);
    return bdmTaskCreateDetails;
  }

  @Test
  public void testlistCurrentProductDeliveryDeductions()
    throws AppException, InformationalException {

    final ListICProductDeliveryDeductionKey key =
      new ListICProductDeliveryDeductionKey();

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.caseID = caseID;

    final BDMIntegratedCase integaratedCase =
      BDMIntegratedCaseFactory.newInstance();
    integaratedCase.listCurrentProductDeliveryDeductions(key);
    assertTrue(key.caseID != 0);

  }

  @Test
  public void testlistProductDeliveryAttachment()
    throws AppException, InformationalException {

    final ListICProductDeliveryAttachmentKey key =
      new ListICProductDeliveryAttachmentKey();

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.caseID = caseID;

    final BDMIntegratedCase integaratedCase =
      BDMIntegratedCaseFactory.newInstance();
    integaratedCase.listProductDeliveryAttachment(key);
    assertTrue(key.caseID != 0);

  }

  @Test
  public void testlistTask() throws AppException, InformationalException {

    final ListICTaskKey_eo key = new ListICTaskKey_eo();

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.caseTasksKey.caseID = caseID;

    final BDMIntegratedCase integaratedCase =
      BDMIntegratedCaseFactory.newInstance();
    integaratedCase.listTask(key);
    assertTrue(key.caseTasksKey.caseID != 0);

  }

  @Test
  public void testlistFilteredCommunication()
    throws AppException, InformationalException {

    final CommunicationFilterKey key = new CommunicationFilterKey();

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.caseID = caseID;
    key.concernRoleID = person.registrationIDDetails.concernRoleID;
    key.concernRoleName = "CRM";

    final BDMIntegratedCase integaratedCase =
      BDMIntegratedCaseFactory.newInstance();
    integaratedCase.listFilteredCommunication(key);
    assertTrue(key.caseID != 0);

  }

  @Test
  public void testlistAttachment()
    throws AppException, InformationalException {

    final ListICAttachmentsKey key = new ListICAttachmentsKey();

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.caseID = caseID;

    final BDMIntegratedCase integaratedCase =
      BDMIntegratedCaseFactory.newInstance();
    integaratedCase.listAttachment(key);
    assertTrue(key.caseID != 0);

  }

  protected CreateIntegratedCaseResultAndMessages createFEC(
    final long concernRoleID) throws AppException, InformationalException {

    // BEGIN: create FE IC for the person
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = BDMSOURCECOUNTRY.IRELAND;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages = BDMForeignEngagementCaseFactory
      .newInstance().createFEIntegratedCase(details);
    // END: create FE IC for the person

    return createIntegratedCaseResultAndMessages;
  }

  protected PersonRegistrationResult registerPerson()
    throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Homer";
    bdmPersonRegistrationDetails.dtls.surname = "Simpson";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "519132708";
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

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

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    assertTrue(registrationResult.registrationIDDetails.concernRoleID != 0);

    return registrationResult;
  }

  public void mockUserObjects() throws AppException, InformationalException {

    new MockUp<TransactionInfo>() {

      @Mock
      public String getProgramUser()
        throws AppException, InformationalException {

        return "caseworker";

      }

    };

  }

}
