package curam.ca.gc.bdmoas.test.util.impl;

import curam.ca.gc.bdmoas.codetable.impl.BDMOASOVERRIDEEVDTASKTYPEEntry;
import curam.ca.gc.bdmoas.test.rules.functions.CustomFunctionTestJunit4;
import curam.ca.gc.bdmoas.util.impl.BDMOASTaskUtil;
import curam.ca.gc.bdmoas.workflow.impl.BDMOASWorkflowConstants;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.TASKSTATUS;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleKey;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.impl.ProcessInstanceAdmin;
import curam.util.workflow.struct.BizObjAssocSearchDetails;
import curam.util.workflow.struct.BizObjAssocSearchDetailsList;
import curam.util.workflow.struct.BizObjectTypeKey;
import curam.util.workflow.struct.ProcInstData;
import curam.util.workflow.struct.ProcessInstanceFullDetails;
import curam.util.workflow.struct.ProcessInstanceID;
import curam.util.workflow.struct.ProcessInstanceSearchDetails;
import curam.util.workflow.struct.TaskDetailsWithSnapshot;
import curam.util.workflow.struct.TaskKey;
import java.util.ArrayList;
import java.util.List;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link BDMOASTaskUtilTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class BDMOASTaskUtilTest extends CustomFunctionTestJunit4 {

  private static final long CASEID = 101l;

  private static final long CONCERNROLEID = 102l;

  private static final long CASEPARTICIPANTTOLEID = 103l;

  private static final String CORRECTIONSETID = "114";

  private static final long SUCCESSIONID = 113l;

  private static final long EVIDENCEDESCRIPTORID = 112l;

  private static final long EVIDENCEID = 111l;

  private static final long TASKID = 115l;

  private static final String PARTICIPANTNAME = "Junit Lee";

  @Tested
  BDMOASTaskUtil bdmOASTaskUtil;

  @Mocked
  EvidenceDescriptorFactory evidenceDescriptorFactory;

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
  CaseHeaderFactory caseHeaderFactory;

  @Mocked
  ConcernRoleFactory concernRoleFactory;

  public BDMOASTaskUtilTest() {

    super();
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  @Test
  public void testGetEvidenceApprovalTaskIDByEvidenceDescriptorIDEmpty()
    throws AppException, InformationalException {

    new Expectations() {

      EvidenceDescriptorDtls evidenceDescriptorDtls =
        new EvidenceDescriptorDtls();

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();

      {
        evidenceDescriptorDtls.caseID = CASEID;
        evidenceDescriptorFactory.read((EvidenceDescriptorKey) any);
        result = evidenceDescriptorDtls;

        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;
      }
    };

    final EvidenceKey key = new EvidenceKey();
    key.evidenceID = EVIDENCEID;
    key.evidenceDescriptorID = EVIDENCEDESCRIPTORID;
    key.successionID = SUCCESSIONID;
    key.correctionSetID = CORRECTIONSETID;
    final Long taskID =
      bdmOASTaskUtil.getEvidenceApprovalTaskIDByEvidenceDescriptorID(key);
    assertEquals(0l, taskID.longValue());
  }

  @Test
  public void testGetEvidenceApprovalTaskIDByEvidenceDescriptorIDCLOSED()
    throws AppException, InformationalException {

    new Expectations() {

      EvidenceDescriptorDtls evidenceDescriptorDtls =
        new EvidenceDescriptorDtls();

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();

      TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        new TaskDetailsWithSnapshot();
      {
        evidenceDescriptorDtls.caseID = CASEID;
        evidenceDescriptorFactory.read((EvidenceDescriptorKey) any);
        result = evidenceDescriptorDtls;

        final BizObjAssocSearchDetails bizObjAssocSearchDetails =
          new BizObjAssocSearchDetails();
        bizObjAssocSearchDetails.taskID = TASKID;
        bizObjAssocList.dtls.add(bizObjAssocSearchDetails);
        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;

        taskDetailsWithSnapshot.status = TASKSTATUS.CLOSED;
        taskAdminFactory.readDetailsWithSnapshot(TASKID);
        result = taskDetailsWithSnapshot;
      }
    };

    final EvidenceKey key = new EvidenceKey();
    key.evidenceID = EVIDENCEID;
    key.evidenceDescriptorID = EVIDENCEDESCRIPTORID;
    key.successionID = SUCCESSIONID;
    key.correctionSetID = CORRECTIONSETID;
    final Long taskID =
      bdmOASTaskUtil.getEvidenceApprovalTaskIDByEvidenceDescriptorID(key);
    assertEquals(0l, taskID.longValue());
  }

  @Test
  public void testGetEvidenceApprovalTaskIDByEvidenceDescriptorID()
    throws AppException, InformationalException {

    new Expectations() {

      EvidenceDescriptorDtls evidenceDescriptorDtls =
        new EvidenceDescriptorDtls();

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();

      TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        new TaskDetailsWithSnapshot();

      ProcessInstanceSearchDetails processInstanceSearchDetails =
        new ProcessInstanceSearchDetails();
      {
        evidenceDescriptorDtls.caseID = CASEID;
        evidenceDescriptorFactory.read((EvidenceDescriptorKey) any);
        result = evidenceDescriptorDtls;

        final BizObjAssocSearchDetails bizObjAssocSearchDetails =
          new BizObjAssocSearchDetails();
        bizObjAssocSearchDetails.taskID = TASKID;
        bizObjAssocList.dtls.add(bizObjAssocSearchDetails);
        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;

        taskDetailsWithSnapshot.status = TASKSTATUS.INPROGRESS;
        taskDetailsWithSnapshot.wdoSnapshot = "Test: wdoSnapshot";
        taskAdminFactory.readDetailsWithSnapshot(TASKID);
        result = taskDetailsWithSnapshot;

        processInstanceSearchDetails.processName =
          BDMOASWorkflowConstants.EVIDENCE_APPROVAL_WORKFLOW_NAME;
        ProcessInstanceAdmin.searchByTaskID((TaskKey) any);
        result = processInstanceSearchDetails;
      }
    };

    final EvidenceKey key = new EvidenceKey();
    key.evidenceID = EVIDENCEID;
    key.evidenceDescriptorID = EVIDENCEDESCRIPTORID;
    key.successionID = SUCCESSIONID;
    key.correctionSetID = CORRECTIONSETID;
    final Long taskID =
      bdmOASTaskUtil.getEvidenceApprovalTaskIDByEvidenceDescriptorID(key);
    assertEquals(0l, taskID.longValue());
  }

  @Test
  public void testRaiseTaskCloseEvent()
    throws AppException, InformationalException {

    new Expectations() {

      {
        EventService.raiseEvent((Event) any);
        times = 1;
      }
    };
    bdmOASTaskUtil.raiseTaskCloseEvent(TASKID);
  }

  @Test
  public void testGetOverrideEvidenceTaskIDByCaseIDTaskTypeEMPTY()
    throws AppException, InformationalException {

    new Expectations() {

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();
      {

        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;
        times = 1;
      }
    };
    final BDMOASOVERRIDEEVDTASKTYPEEntry taskType =
      BDMOASOVERRIDEEVDTASKTYPEEntry.COUNTRY;
    final Long taskID = bdmOASTaskUtil
      .getOverrideEvidenceTaskIDByCaseIDTaskType(CASEID, taskType);
  }

  @Test
  public void testGetOverrideEvidenceTaskIDByCaseIDTaskTypeCLOSED()
    throws AppException, InformationalException {

    new Expectations() {

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();

      TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        new TaskDetailsWithSnapshot();
      {

        final BizObjAssocSearchDetails bizObjAssocSearchDetails =
          new BizObjAssocSearchDetails();
        bizObjAssocSearchDetails.taskID = TASKID;
        bizObjAssocList.dtls.add(bizObjAssocSearchDetails);
        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;
        times = 1;

        taskDetailsWithSnapshot.status = TASKSTATUS.CLOSED;
        taskAdminFactory.readDetailsWithSnapshot(TASKID);
        result = taskDetailsWithSnapshot;
      }
    };
    final BDMOASOVERRIDEEVDTASKTYPEEntry taskType =
      BDMOASOVERRIDEEVDTASKTYPEEntry.COUNTRY;
    final Long taskID = bdmOASTaskUtil
      .getOverrideEvidenceTaskIDByCaseIDTaskType(CASEID, taskType);
  }

  @Test
  public void testGetOverrideEvidenceTaskIDByCaseIDTaskType()
    throws AppException, InformationalException {

    new Expectations() {

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();

      TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        new TaskDetailsWithSnapshot();

      ProcessInstanceSearchDetails processInstanceSearchDetails =
        new ProcessInstanceSearchDetails();

      ProcessInstanceFullDetails processInstanceFullDetails =
        new ProcessInstanceFullDetails();
      {

        final BizObjAssocSearchDetails bizObjAssocSearchDetails =
          new BizObjAssocSearchDetails();
        bizObjAssocSearchDetails.taskID = TASKID;
        bizObjAssocList.dtls.add(bizObjAssocSearchDetails);
        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;
        times = 1;

        taskDetailsWithSnapshot.status = TASKSTATUS.INPROGRESS;
        taskAdminFactory.readDetailsWithSnapshot(TASKID);
        result = taskDetailsWithSnapshot;

        processInstanceSearchDetails.processName =
          BDMOASWorkflowConstants.ELIG_ENT_OVERRIDE_EVIDENCE_TASK_WORKFLOW_NAME;
        ProcessInstanceAdmin.searchByTaskID((TaskKey) any);
        result = processInstanceSearchDetails;

        final ProcInstData procInstData = new ProcInstData();
        procInstData.wdoAttributeName = "taskType";
        procInstData.wdoAttributeValue =
          BDMOASOVERRIDEEVDTASKTYPEEntry.COUNTRY.getCode();
        processInstanceFullDetails.data.add(procInstData);
        ProcessInstanceAdmin.readProcessInstance((ProcessInstanceID) any);
        result = processInstanceFullDetails;
      }
    };
    final BDMOASOVERRIDEEVDTASKTYPEEntry taskType =
      BDMOASOVERRIDEEVDTASKTYPEEntry.COUNTRY;
    final Long taskID = bdmOASTaskUtil
      .getOverrideEvidenceTaskIDByCaseIDTaskType(CASEID, taskType);
    assertEquals(TASKID, taskID.longValue());
  }

  @Test
  public void testCreateOverrideEvidenceTask()
    throws AppException, InformationalException {

    new Expectations() {

      {
        EnactmentService.startProcess((String) any,
          (List<? extends Object>) any);
      }
    };
    final BDMOASOVERRIDEEVDTASKTYPEEntry taskType =
      BDMOASOVERRIDEEVDTASKTYPEEntry.COUNTRY;
    BDMOASTaskUtil.createOverrideEvidenceTask(CASEID, taskType);
  }

  @Test
  public void testCreateOutstandingVerificationTaskForCase()
    throws AppException, InformationalException {

    new Expectations() {

      EvidenceDescriptorDtls descriptorDtls = new EvidenceDescriptorDtls();

      curam.core.struct.ConcernRoleNameDetails concernRoleNameDetails =
        new curam.core.struct.ConcernRoleNameDetails();

      curam.core.struct.CaseHeaderDtls caseHeaderDtls =
        new curam.core.struct.CaseHeaderDtls();

      {

        descriptorDtls.participantID = CONCERNROLEID;
        evidenceControllerFactory.readEvidenceDescriptorByRelatedIDAndType(
          (RelatedIDAndEvidenceTypeKey) any);
        result = descriptorDtls;

        caseHeaderDtls.caseReference = "141";
        caseHeaderFactory.read((CaseHeaderKey) any);
        result = caseHeaderDtls;

        concernRoleNameDetails.concernRoleName = "Test: concernRoleName";
        concernRoleFactory.readConcernRoleName((ConcernRoleKey) any);
        result = concernRoleNameDetails;

        EnactmentService.startProcess((String) any,
          (List<? extends Object>) any);
        times = 1;
      }
    };

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceType = CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS;
    eiEvidenceKey.evidenceID = EVIDENCEID;
    bdmOASTaskUtil.createOutstandingVerificationTaskForCase(CASEID,
      eiEvidenceKey);
  }

  @Test
  public void testCheckIfOutstandingVerTaskAlreadyExistForCaseEMPTY()
    throws AppException, InformationalException {

    new Expectations() {

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();
      {

        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;

      }
    };

    final ArrayList<Long> taskList =
      bdmOASTaskUtil.checkIfOutstandingVerTaskAlreadyExistForCase(CASEID,
        CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS, CASETYPECODE.INTEGRATEDCASE);

    assertEquals(CuramConst.gkZero, taskList.size());
  }

  @Test
  public void testCheckIfOutstandingVerTaskAlreadyExistForCaseCLOSED()
    throws AppException, InformationalException {

    new Expectations() {

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();

      TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        new TaskDetailsWithSnapshot();
      {

        final BizObjAssocSearchDetails bizObjAssocSearchDetails =
          new BizObjAssocSearchDetails();
        bizObjAssocSearchDetails.taskID = TASKID;
        bizObjAssocList.dtls.add(bizObjAssocSearchDetails);
        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;
        times = 1;

        taskDetailsWithSnapshot.status = TASKSTATUS.CLOSED;
        taskDetailsWithSnapshot.wdoSnapshot = "Test: wdoSnapshot";
        taskAdminFactory.readDetailsWithSnapshot(TASKID);
        result = taskDetailsWithSnapshot;

      }
    };

    final ArrayList<Long> taskList =
      bdmOASTaskUtil.checkIfOutstandingVerTaskAlreadyExistForCase(CASEID,
        CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS, CASETYPECODE.INTEGRATEDCASE);

    assertEquals(CuramConst.gkZero, taskList.size());
  }

  @Test
  public void testCheckIfOutstandingVerTaskAlreadyExistForCaseNOTClosed()
    throws AppException, InformationalException {

    new Expectations() {

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();

      TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        new TaskDetailsWithSnapshot();

      ProcessInstanceSearchDetails processInstanceSearchDetails =
        new ProcessInstanceSearchDetails();

      ProcessInstanceFullDetails processInstanceFullDetails =
        new ProcessInstanceFullDetails();

      EvidenceDescriptorDtls evidenceDescriptorDtls =
        new EvidenceDescriptorDtls();
      {

        final BizObjAssocSearchDetails bizObjAssocSearchDetails =
          new BizObjAssocSearchDetails();
        bizObjAssocSearchDetails.taskID = TASKID;
        bizObjAssocList.dtls.add(bizObjAssocSearchDetails);
        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;
        times = 1;

        taskDetailsWithSnapshot.status = TASKSTATUS.INPROGRESS;
        taskDetailsWithSnapshot.wdoSnapshot = "Test: wdoSnapshot";
        taskAdminFactory.readDetailsWithSnapshot(TASKID);
        result = taskDetailsWithSnapshot;

        processInstanceSearchDetails.processName =
          BDMOASWorkflowConstants.OUTSTANDING_VERIFICATION_TASK_WORKFLOW_NAME;
        ProcessInstanceAdmin.searchByTaskID((TaskKey) any);
        result = processInstanceSearchDetails;

        final ProcInstData procInstData = new ProcInstData();
        procInstData.wdoAttributeName = "evidenceID";
        procInstData.wdoAttributeValue = String.valueOf(EVIDENCEID);
        processInstanceFullDetails.data.add(procInstData);
        ProcessInstanceAdmin.readProcessInstance((ProcessInstanceID) any);
        result = processInstanceFullDetails;

        evidenceDescriptorDtls.statusCode = EVIDENCEDESCRIPTORSTATUS.ACTIVE;
        evidenceDescriptorFactory.readByRelatedIDAndType(
          (NotFoundIndicator) any, (RelatedIDAndEvidenceTypeKey) any);
        result = evidenceDescriptorDtls;
      }
    };

    final ArrayList<Long> taskList =
      bdmOASTaskUtil.checkIfOutstandingVerTaskAlreadyExistForCase(CASEID,
        CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS, CASETYPECODE.INTEGRATEDCASE);

    assertEquals(CuramConst.gkOne, taskList.size());
    assertEquals(TASKID, taskList.get(CuramConst.gkZero).longValue());
  }

  @Test
  public void testGetTaskIdFromEvidenceIDEMPTY()
    throws AppException, InformationalException {

    new Expectations() {

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();
      {

        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;

      }
    };

    final Long taskID = bdmOASTaskUtil.getTaskIdFromEvidenceID(EVIDENCEID);
    assertEquals(0l, taskID.longValue());
  }

  @Test
  public void testGetTaskIdFromEvidenceIDCLOSED()
    throws AppException, InformationalException {

    new Expectations() {

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();

      TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        new TaskDetailsWithSnapshot();
      {

        final BizObjAssocSearchDetails bizObjAssocSearchDetails =
          new BizObjAssocSearchDetails();
        bizObjAssocSearchDetails.taskID = TASKID;
        bizObjAssocList.dtls.add(bizObjAssocSearchDetails);
        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;
        times = 1;

        taskDetailsWithSnapshot.status = TASKSTATUS.CLOSED;
        taskDetailsWithSnapshot.wdoSnapshot = "Test: wdoSnapshot";
        taskAdminFactory.readDetailsWithSnapshot(TASKID);
        result = taskDetailsWithSnapshot;

      }
    };

    final Long taskID = bdmOASTaskUtil.getTaskIdFromEvidenceID(EVIDENCEID);
    assertEquals(0l, taskID.longValue());
  }

  @Test
  public void testGetTaskIdFromEvidenceIDNOTClosed()
    throws AppException, InformationalException {

    new Expectations() {

      BizObjAssocSearchDetailsList bizObjAssocList =
        new BizObjAssocSearchDetailsList();

      TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        new TaskDetailsWithSnapshot();

      ProcessInstanceSearchDetails processInstanceSearchDetails =
        new ProcessInstanceSearchDetails();

      ProcessInstanceFullDetails processInstanceFullDetails =
        new ProcessInstanceFullDetails();

      EvidenceDescriptorDtls evidenceDescriptorDtls =
        new EvidenceDescriptorDtls();
      {

        final BizObjAssocSearchDetails bizObjAssocSearchDetails =
          new BizObjAssocSearchDetails();
        bizObjAssocSearchDetails.taskID = TASKID;
        bizObjAssocList.dtls.add(bizObjAssocSearchDetails);
        bizObjAssociationFactory
          .searchByBizObjectTypeAndID((BizObjectTypeKey) any);
        result = bizObjAssocList;
        times = 1;

        taskDetailsWithSnapshot.status = TASKSTATUS.INPROGRESS;
        taskDetailsWithSnapshot.wdoSnapshot = "Test: wdoSnapshot";
        taskAdminFactory.readDetailsWithSnapshot(TASKID);
        result = taskDetailsWithSnapshot;

        processInstanceSearchDetails.processName =
          BDMOASWorkflowConstants.OUTSTANDING_VERIFICATION_TASK_WORKFLOW_NAME;
        ProcessInstanceAdmin.searchByTaskID((TaskKey) any);
        result = processInstanceSearchDetails;
        times = 0;

        final ProcInstData procInstData = new ProcInstData();
        procInstData.wdoAttributeName = "evidenceID";
        procInstData.wdoAttributeValue = String.valueOf(EVIDENCEID);
        processInstanceFullDetails.data.add(procInstData);
        ProcessInstanceAdmin.readProcessInstance((ProcessInstanceID) any);
        result = processInstanceFullDetails;
        times = 0;

      }
    };

    final Long taskID = bdmOASTaskUtil.getTaskIdFromEvidenceID(EVIDENCEID);

    assertEquals(TASKID, taskID.longValue());
  }

  @Test
  public void testCreateVerifyAuthorizedPersonTask()
    throws AppException, InformationalException {

    new Expectations() {

      {

        EnactmentService.startProcess((String) any,
          (List<? extends Object>) any);
        times = 1;

      }
    };

    BDMOASTaskUtil.createVerifyAuthorizedPersonTask(CASEID, PARTICIPANTNAME);
  }
}
