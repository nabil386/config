package curam.ca.gc.bdm.test.facade.supervisor.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.entity.fact.BDMEscalationLevelFactory;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtlsStruct2;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelKeyStruct1;
import curam.ca.gc.bdm.facade.bdmworkallocation.fact.BDMMaintainSupervisorWorkQueuesFactory;
import curam.ca.gc.bdm.facade.bdmworkallocation.impl.BDMMaintainSupervisorWorkQueues;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMWorkQueueAssignedTasksSummaryDetailsList;
import curam.ca.gc.bdm.sl.organization.supervisor.fact.BDMMaintainSupervisorUsersFactory;
import curam.ca.gc.bdm.sl.struct.BDMWorkQueueAssignedTasksSummaryDetails;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.PRIORITY;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.struct.WorkQueueKey;
import curam.core.sl.supervisor.struct.WorkQueueAssignedTasksSummaryDetails;
import curam.supervisor.facade.struct.ReserveWorkQueueTasksToUserDetails;
import curam.supervisor.facade.struct.TaskFilterCriteriaDetails;
import curam.supervisor.facade.struct.UserNameAndWorkQueueIDKey;
import curam.supervisor.sl.fact.SupervisorApplicationPageContextDescriptionFactory;
import curam.supervisor.sl.impl.MaintainSupervisorWorkQueues;
import curam.supervisor.sl.struct.WorkQueueAssignedTasksSummaryDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMMaintainSupervisorWorkQueuesTest
  extends CuramServerTestJUnit4 {

  private static String ESCALATIONLEVEL_1 = "Escalation Level 1";

  final BDMMaintainSupervisorWorkQueues bdmExtPartyObj =
    (BDMMaintainSupervisorWorkQueues) BDMMaintainSupervisorWorkQueuesFactory
      .newInstance();

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
  }

  @Tested
  BDMMaintainSupervisorWorkQueues bdmMaintainSupervisorWorkQueues;

  /* MOCKED */
  @Mocked
  SupervisorApplicationPageContextDescriptionFactory supervisorApplicationPageContextDescriptionFactory;

  @Mocked
  BizObjAssociationFactory bizObjAssociationObj;

  @Mocked
  BDMMaintainSupervisorUsersFactory bdmMaintainSupervisorUsersFactory;

  WorkQueueAssignedTasksSummaryDetailsList workQueueAssignedTasksSummaryDetailsList;

  @Mocked
  BDMEscalationLevelFactory bdmEscalationLevelFactory;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmMaintainSupervisorWorkQueues =
      (BDMMaintainSupervisorWorkQueues) BDMMaintainSupervisorWorkQueuesFactory
        .newInstance();
    workQueueAssignedTasksSummaryDetailsList =
      new WorkQueueAssignedTasksSummaryDetailsList();

    expectReadPageContext();
    setUpWorkQueueAssignedTasksSummaryDetailsList();
    setUpBizObjAssociationList();
    // setUpReadEscalationLevelByTaskID();
    setUpBDMEscalationLevelDtlsStruct2();
  }

  /**
   * Dummy call to read page context. This is OOTB and we do not need this value
   * for any further processing
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void expectReadPageContext()
    throws AppException, InformationalException {

    new Expectations() {

      {
        SupervisorApplicationPageContextDescriptionFactory.newInstance()
          .readWorkQueuePageContextDescription((WorkQueueKey) any);

      }
    };
  }

  private void setUpWorkQueueAssignedTasksSummaryDetailsList() {

    new MockUp<MaintainSupervisorWorkQueues>() {

      @Mock
      public WorkQueueAssignedTasksSummaryDetailsList
        listAssignedTasks(@Mocked
      final curam.supervisor.sl.struct.TaskFilterCriteriaDetails criteria) {

        final WorkQueueAssignedTasksSummaryDetails workQueueAssignedTasksSummaryDetails =
          new WorkQueueAssignedTasksSummaryDetails();
        workQueueAssignedTasksSummaryDetails.taskID = 256;
        workQueueAssignedTasksSummaryDetails.taskSubject = "Escalation Task";
        workQueueAssignedTasksSummaryDetails.versionNo = 1;
        workQueueAssignedTasksSummaryDetails.priority = TASKPRIORITY.LOW;

        workQueueAssignedTasksSummaryDetailsList.assignedTaskDetails.dtls
          .add(workQueueAssignedTasksSummaryDetails);

        return workQueueAssignedTasksSummaryDetailsList;
      }
    };
  }

  private void setUpBizObjAssociationList()
    throws AppException, InformationalException {

    final BizObjAssociationDtlsList bizObjAssociationDtlsList =
      new BizObjAssociationDtlsList();

    final BizObjAssociationDtls obj1 = new BizObjAssociationDtls();
    obj1.bizObjAssocID = 80011;
    obj1.taskID = 512;
    obj1.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;

    BizObjAssociationFactory.newInstance().insert(obj1);

    new Expectations() {

      {

        bizObjAssociationDtlsList.dtls.addRef(obj1);
        BizObjAssociationFactory.newInstance()
          .searchByTaskID((curam.util.workflow.struct.TaskKey) any);

        result = bizObjAssociationDtlsList;

      }
    };

  }

  private void setUpBDMEscalationLevelDtlsStruct2()
    throws AppException, InformationalException {

    new Expectations() {

      {

        final BDMEscalationLevelDtlsStruct2 escalationLevelDtls =
          new BDMEscalationLevelDtlsStruct2();
        escalationLevelDtls.communicationID = 80000;
        escalationLevelDtls.escalationLevel =
          BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
        escalationLevelDtls.bdmEscalationLevelID = 80000;

        BDMEscalationLevelFactory.newInstance().readByCommunicationID(
          (NotFoundIndicator) any, (BDMEscalationLevelKeyStruct1) any);
        result = escalationLevelDtls;

      }
    };

  }

  @Test
  public void testListbdmTasksAssignedToWorkQueue()
    throws AppException, InformationalException {

    final TaskFilterCriteriaDetails taskFilterCriteriaDetails =
      new TaskFilterCriteriaDetails();

    taskFilterCriteriaDetails.details.assigneeType = TARGETITEMTYPE.WORKQUEUE;
    taskFilterCriteriaDetails.details.businessObjectType =
      BUSINESSOBJECTTYPE.BDMCOMMUNICATION;
    taskFilterCriteriaDetails.details.priority = PRIORITY.LOW;

    final BDMWorkQueueAssignedTasksSummaryDetailsList list =
      BDMMaintainSupervisorWorkQueuesFactory.newInstance()
        .listBDMTasksAssignedToWorkQueue(taskFilterCriteriaDetails);

    if (list.assignedTaskDetails.dtls.size() > 0) {

      BDMWorkQueueAssignedTasksSummaryDetails bdmWorkQueueAssignedTasksSummaryDetails =
        new BDMWorkQueueAssignedTasksSummaryDetails();
      bdmWorkQueueAssignedTasksSummaryDetails =
        list.assignedTaskDetails.dtls.get(0);

      assertEquals("Escalation Level 1",
        bdmWorkQueueAssignedTasksSummaryDetails.escalationLevelDesc);

    }

  }

  @Test
  public void testlistBDMDeferredTasksReservedByWorkQueueUser()
    throws AppException, InformationalException {

    final UserNameAndWorkQueueIDKey key = new UserNameAndWorkQueueIDKey();
    key.key.workQueueID = 8001;
    key.key.userName = "John Smith";

    bdmExtPartyObj.listBDMDeferredTasksReservedByWorkQueueUser(key);

  }

  @Test
  public void testreserveBDMWorkQueueAssignedTasksToUser()
    throws AppException, InformationalException {

    final ReserveWorkQueueTasksToUserDetails key =
      new ReserveWorkQueueTasksToUserDetails();
    key.actionIDProperty = "Save";
    key.criteria.assigneeType = TARGETITEMTYPE.USER;
    key.criteria.businessObjectID = CuramConst.kLongZero;
    key.criteria.businessObjectType = BUSINESSOBJECTTYPE.PERSON;
    key.criteria.creationFromDate = Date.getCurrentDate().addDays(20);
    key.criteria.creationToDate = Date.getCurrentDate().addDays(10);
    key.criteria.deadlineFromDate = Date.getCurrentDate().addDays(20);
    key.criteria.deadlineToDate = Date.getCurrentDate().addDays(10);

    bdmExtPartyObj.reserveBDMWorkQueueAssignedTasksToUser(key);

  }

}
