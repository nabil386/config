package curam.ca.gc.bdmoas.util.impl;

import curam.ca.gc.bdm.sl.struct.BDMParticipantAndCaseEnactDetails;
import curam.ca.gc.bdmoas.application.impl.BDMOASApplicationUtil;
import curam.ca.gc.bdmoas.codetable.impl.BDMOASOVERRIDEEVDTASKTYPEEntry;
import curam.ca.gc.bdmoas.evidence.sl.struct.BDMOASOverrideEvidenceTaskDetails;
import curam.ca.gc.bdmoas.message.BDMOASTASKMESSAGE;
import curam.ca.gc.bdmoas.sl.verification.struct.BDMOASVerOutstandingTaskDetails;
import curam.ca.gc.bdmoas.workflow.impl.BDMOASWorkflowConstants;
import curam.codetable.BIZOBJASSOCIATION;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKSTATUS;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.EnvVars;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleKey;
import curam.events.TASK;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.resources.Configuration;
import curam.util.type.CodeTable;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.impl.ProcessInstanceAdmin;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.intf.TaskAdmin;
import curam.util.workflow.struct.BizObjAssocSearchDetails;
import curam.util.workflow.struct.BizObjAssocSearchDetailsList;
import curam.util.workflow.struct.BizObjectTypeKey;
import curam.util.workflow.struct.ProcInstData;
import curam.util.workflow.struct.ProcessInstanceFullDetails;
import curam.util.workflow.struct.ProcessInstanceID;
import curam.util.workflow.struct.ProcessInstanceSearchDetails;
import curam.util.workflow.struct.TaskDetailsWithSnapshot;
import curam.util.workflow.struct.TaskKey;
import curam.wizard.util.impl.CodetableUtil;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 95630 DEV: Implement Manage Override Evidence - R2
 * Utility Class for Task functionality
 *
 * @author guruvamshikrishna.va
 *
 */
public class BDMOASTaskUtil {

  // The following property should be set only in JUnits to test workflow
  public static final String TEST_ENACT_WORKFLOW_IN_SAME_TRANSACTION =
    "curam.ca.gc.bdmoas.test.enactWorkflow.inSameTransaction.enabled";

  /**
   * 95630 DEV: Implement Manage Override Evidence - R2
   * Get the Task ID for Open Evidence Approval task for a given evidence
   * descriptor ID
   * Returns 0 if no open task found.
   *
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public Long getEvidenceApprovalTaskIDByEvidenceDescriptorID(
    final EvidenceKey key) throws AppException, InformationalException {

    // read Case ID
    final EvidenceDescriptorKey evidenceDescriptorKey =
      new EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID = key.evidenceDescriptorID;
    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance().read(evidenceDescriptorKey);
    final long caseID = evidenceDescriptorDtls.caseID;

    // read bizObjAssociations for this case
    final BizObjAssociation bizObjAssoc =
      BizObjAssociationFactory.newInstance();
    final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
    bizObjectTypeKey.bizObjectType = BIZOBJASSOCIATION.CASE;
    bizObjectTypeKey.bizObjectID = caseID;
    final BizObjAssocSearchDetailsList bizObjAssocList =
      bizObjAssoc.searchByBizObjectTypeAndID(bizObjectTypeKey);
    if (bizObjAssocList.dtls.isEmpty()) {
      return 0l;
    }

    // Loop through bizObjAssociations
    for (final BizObjAssocSearchDetails bizObjSearchDetailsObj : bizObjAssocList.dtls) {

      final long taskID = bizObjSearchDetailsObj.taskID;

      // Read task wdo snapshot
      final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
      final TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        taskAdminObj.readDetailsWithSnapshot(taskID);

      // Skip closed task
      if (TASKSTATUS.CLOSED.equals(taskDetailsWithSnapshot.status)) {
        continue;
      }

      // Read Process details to check if the task is for Evidence Approval
      final TaskKey taskKey = new TaskKey();
      taskKey.taskID = taskID;
      final ProcessInstanceSearchDetails processInstanceSearchDetails =
        ProcessInstanceAdmin.searchByTaskID(taskKey);

      // If the task does not belong to Evidence Approval Workflow then continue
      // to next task
      if (!processInstanceSearchDetails.processName
        .equals(BDMOASWorkflowConstants.EVIDENCE_APPROVAL_WORKFLOW_NAME))
        continue;

      // Search the snapshot to read the evidencedescriptorID value
      final String wdoSnapshot = taskDetailsWithSnapshot.wdoSnapshot;

      final InputSource is = new InputSource(new StringReader(wdoSnapshot));
      final String evidenceDescriptorIDExpression =
        "wdo-snapshot/wdo/attribute[@name='evidenceDescriptorID']";
      final XPath xpath = XPathFactory.newInstance().newXPath();
      try {
        final DocumentBuilder builder =
          DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final Document dDoc = builder.parse(is);
        final XPathExpression expression =
          xpath.compile(evidenceDescriptorIDExpression);
        final NodeList nodes =
          (NodeList) expression.evaluate(dDoc, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
          final Node node = nodes.item(i);
          if (node.hasAttributes()) {
            final Attr attr =
              (Attr) node.getAttributes().getNamedItem("value");
            if (attr != null) {
              final String evidenceDescriptorIDValue = attr.getValue();

              // if evidenceDescriptorID value is same then we have found the
              // associated Evidence Approval Task, return the task details
              if (evidenceDescriptorIDValue
                .equals(String.valueOf(key.evidenceDescriptorID)))
                return taskID;
            }
          }
        }
      } catch (final Exception e) {
        // Moving to the next Task, further processing will not happen as nodes
        // is empty
      }
    }
    return 0l;
  }

  /**
   * 95630 DEV: Implement Manage Override Evidence - R2
   * Raises Event of type Closed of Task Event class
   *
   * @param taskID
   * @throws AppException
   * @throws InformationalException
   */
  public void raiseTaskCloseEvent(final Long taskID)
    throws AppException, InformationalException {

    final Event taskCloseEvent = new Event();
    taskCloseEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
    taskCloseEvent.eventKey.eventType = TASK.CLOSED.eventType;
    taskCloseEvent.primaryEventData = taskID;

    EventService.raiseEvent(taskCloseEvent);
  }

  /**
   * Gets the override evidence task ID by case ID task type.
   *
   * @param caseID the case ID
   * @param taskType the task type
   * @return the override evidence task ID by case ID task type
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public Long getOverrideEvidenceTaskIDByCaseIDTaskType(final Long caseID,
    final BDMOASOVERRIDEEVDTASKTYPEEntry taskType)
    throws AppException, InformationalException {

    // read bizObjAssociations for this case
    final BizObjAssociation bizObjAssoc =
      BizObjAssociationFactory.newInstance();
    final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
    bizObjectTypeKey.bizObjectType = BIZOBJASSOCIATION.CASE;
    bizObjectTypeKey.bizObjectID = caseID;
    final BizObjAssocSearchDetailsList bizObjAssocList =
      bizObjAssoc.searchByBizObjectTypeAndID(bizObjectTypeKey);
    if (bizObjAssocList.dtls.isEmpty()) {
      return 0l;
    }
    // Loop through bizObjAssociations
    for (final BizObjAssocSearchDetails bizObjSearchDetailsObj : bizObjAssocList.dtls) {

      final long taskID = bizObjSearchDetailsObj.taskID;

      // Read task wdo snapshot
      final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
      final TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        taskAdminObj.readDetailsWithSnapshot(taskID);

      // Skip closed task
      if (TASKSTATUS.CLOSED.equals(taskDetailsWithSnapshot.status)) {
        continue;
      }

      // Read Process details to check if the task is for Override Evidence
      final TaskKey taskKey = new TaskKey();
      taskKey.taskID = taskID;
      final ProcessInstanceSearchDetails processInstanceSearchDetails =
        ProcessInstanceAdmin.searchByTaskID(taskKey);

      // If the task does not belong to Override Evidence Task Workflow then
      // continue to next task
      if (!processInstanceSearchDetails.processName.equals(
        BDMOASWorkflowConstants.ELIG_ENT_OVERRIDE_EVIDENCE_TASK_WORKFLOW_NAME))
        continue;

      // Read the Process Instance WDO snapshot
      final ProcessInstanceID processInstanceIDObj = new ProcessInstanceID();
      processInstanceIDObj.processInstanceID =
        processInstanceSearchDetails.processInstanceID;
      final ProcessInstanceFullDetails processInstanceFullDetails =
        ProcessInstanceAdmin.readProcessInstance(processInstanceIDObj);

      // If snapshot data contains the input task type return the taskID
      for (final ProcInstData instData : processInstanceFullDetails.data) {
        if (instData.wdoAttributeName.equals("taskType")
          && instData.wdoAttributeValue.equals(taskType.getCode())) {
          return taskID;
        }
      }
    }
    return 0l;
  }

  /**
   * Creates the override evidence task.
   *
   * @param caseID the case ID
   * @param taskTypeEntry the task type entry
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public static void createOverrideEvidenceTask(final Long caseID,
    final BDMOASOVERRIDEEVDTASKTYPEEntry taskTypeEntry)
    throws AppException, InformationalException {

    final List<Object> enactmentStructs = new ArrayList<Object>();

    final BDMOASOverrideEvidenceTaskDetails taskCreateDetails =
      new BDMOASOverrideEvidenceTaskDetails();
    taskCreateDetails.caseID = caseID;
    final LocalisableString subject =
      new LocalisableString(BDMOASTASKMESSAGE.OVERRIDE_EVIDENCE_TASK_SUBJECT);
    subject.arg(CodetableUtil.getCachedCodetableDescription(
      BDMOASOVERRIDEEVDTASKTYPEEntry.TABLENAME, taskTypeEntry.getCode()));
    taskCreateDetails.subject = subject.getMessage();
    taskCreateDetails.taskType = taskTypeEntry.getCode();
    taskCreateDetails.assignedTo = Configuration
      .getProperty(EnvVars.BDM_ENV_BDM_DEFAULT_CASEOWNER_WORKQUEUE);
    taskCreateDetails.assigneeType = TARGETITEMTYPE.WORKQUEUE;
    enactmentStructs.add(taskCreateDetails);

    EnactmentService.startProcess(
      BDMOASWorkflowConstants.ELIG_ENT_OVERRIDE_EVIDENCE_TASK_WORKFLOW_NAME,
      enactmentStructs);

  }

  /**
   * Creates the outstanding verification task for case.
   *
   * @param caseID the case ID
   * @param evidenceKey the evidence key
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public void createOutstandingVerificationTaskForCase(final Long caseID,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final List<Object> enactmentStructs = new ArrayList<Object>();

    final BDMOASVerOutstandingTaskDetails taskCreateDetails =
      new BDMOASVerOutstandingTaskDetails();
    // Assigning values to the Enactment objects
    taskCreateDetails.caseID = caseID;
    final LocalisableString subject = new LocalisableString(
      BDMOASTASKMESSAGE.VERIFICATION_OUTSTANDING_SUBJECT_CASE);
    final CaseHeaderKey Caseherkey = new CaseHeaderKey();
    Caseherkey.caseID = caseID;
    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);
    final ConcernRoleKey concernrolKey = new ConcernRoleKey();
    final String evidence = CodeTable.getOneItem("EvidenceType",
      evidenceKey.evidenceType.toString(), "en");
    subject.arg(evidence);
    concernrolKey.concernRoleID = descriptorDtls.participantID;
    subject.arg(
      CaseHeaderFactory.newInstance().read(Caseherkey).caseReference + "");
    subject.arg(ConcernRoleFactory.newInstance()
      .readConcernRoleName(concernrolKey).concernRoleName);
    taskCreateDetails.subject = subject.getMessage();
    taskCreateDetails.concernRoleID = descriptorDtls.participantID;
    taskCreateDetails.evidenceID = evidenceKey.evidenceID;
    taskCreateDetails.assignedTo = Configuration
      .getProperty(EnvVars.BDM_ENV_BDM_DEFAULT_CASEOWNER_WORKQUEUE);
    taskCreateDetails.assigneeType = TARGETITEMTYPE.WORKQUEUE;
    enactmentStructs.add(taskCreateDetails);
    // Starting the EnactmentService
    EnactmentService.startProcess(
      BDMOASWorkflowConstants.OUTSTANDING_VERIFICATION_TASK_WORKFLOW_NAME,
      enactmentStructs);

  }

  /**
   * Creates the outstanding verification task for person.
   *
   * @param participantID the participant ID
   * @param evidenceKey the evidence key
   */
  public void createOutstandingVerificationTaskForPerson(
    final long participantID, final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerInterface =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final List<Object> enactmentStructs = new ArrayList<Object>();

    final BDMOASVerOutstandingTaskDetails taskCreateDetails =
      new BDMOASVerOutstandingTaskDetails();

    // Assigning values to the Enactment objects
    taskCreateDetails.concernRoleID = participantID;
    final LocalisableString subject = new LocalisableString(
      BDMOASTASKMESSAGE.VERIFICATION_OUTSTANDING_SUBJECT_PERSON);

    final RelatedIDAndEvidenceTypeKey readDescriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    readDescriptorKey.evidenceType = evidenceKey.evidenceType;
    readDescriptorKey.relatedID = evidenceKey.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls = evidenceControllerInterface
      .readEvidenceDescriptorByRelatedIDAndType(readDescriptorKey);
    final ConcernRoleKey concernrolKey = new ConcernRoleKey();
    final String evidence = CodeTable.getOneItem("EvidenceType",
      evidenceKey.evidenceType.toString(), "en");
    subject.arg(evidence);
    concernrolKey.concernRoleID = descriptorDtls.participantID;
    subject.arg(ConcernRoleFactory.newInstance()
      .readConcernRoleName(concernrolKey).concernRoleName);
    final String refNumber =
      BDMOASApplicationUtil.getActiveAlternateIDByConcernRoleIDAndType(
        descriptorDtls.participantID,
        CONCERNROLEALTERNATEID.REFERENCE_NUMBER);
    subject.arg(refNumber);
    taskCreateDetails.subject = subject.getMessage();
    taskCreateDetails.concernRoleID = descriptorDtls.participantID;
    taskCreateDetails.evidenceID = evidenceKey.evidenceID;
    taskCreateDetails.assignedTo = Configuration
      .getProperty(EnvVars.BDM_ENV_BDM_DEFAULT_CASEOWNER_WORKQUEUE);
    taskCreateDetails.assigneeType = TARGETITEMTYPE.WORKQUEUE;
    enactmentStructs.add(taskCreateDetails);
    // Starting the EnactmentService
    EnactmentService.startProcess(
      BDMOASWorkflowConstants.OUTSTANDING_VERIFICATION_TASK_PERAON_WORKFLOW_NAME,
      enactmentStructs);

  }

  /**
   * Check if outstanding verification task already exist a Person or Case .
   *
   * @param caseID the case ID
   * @param evidenceType the evidence type
   * @return the array list
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public ArrayList<Long> checkIfOutstandingVerTaskAlreadyExistForCase(
    final Long caseOrConcerRole, final String evidenceType, final String type)
    throws AppException, InformationalException {

    final ArrayList<Long> taskList = new ArrayList<>();
    final BizObjAssociation bizObjAssoc =
      BizObjAssociationFactory.newInstance();
    final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
    if (CASETYPECODE.INTEGRATEDCASE.equals(type)) {
      bizObjectTypeKey.bizObjectType = BIZOBJASSOCIATION.CASE;
      bizObjectTypeKey.bizObjectID = caseOrConcerRole;
    }
    if (CASETYPECODE.PARTICIPANTDATACASE.equals(type)) {
      bizObjectTypeKey.bizObjectType = BIZOBJASSOCIATION.PARTICIPANTROLE;
      bizObjectTypeKey.bizObjectID = caseOrConcerRole;
    }
    final BizObjAssocSearchDetailsList bizObjAssocList =
      bizObjAssoc.searchByBizObjectTypeAndID(bizObjectTypeKey);
    if (bizObjAssocList.dtls.isEmpty()) {
      return taskList;
    }
    // Loop through bizObjAssociations
    for (final BizObjAssocSearchDetails bizObjSearchDetailsObj : bizObjAssocList.dtls) {

      final long taskID = bizObjSearchDetailsObj.taskID;

      // Read task wdo snapshot
      final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
      final TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        taskAdminObj.readDetailsWithSnapshot(taskID);

      // Skip closed task
      if (TASKSTATUS.CLOSED.equals(taskDetailsWithSnapshot.status)) {
        continue;
      }

      // Read Process details to check if the task is for Override Evidence
      final TaskKey taskKey = new TaskKey();
      taskKey.taskID = taskID;
      final ProcessInstanceSearchDetails processInstanceSearchDetails =
        ProcessInstanceAdmin.searchByTaskID(taskKey);

      // If the task does not belong to Override Evidence Task Workflow then
      // continue to next task
      if (CASETYPECODE.INTEGRATEDCASE.equals(type)) {
        if (!processInstanceSearchDetails.processName.equals(
          BDMOASWorkflowConstants.OUTSTANDING_VERIFICATION_TASK_WORKFLOW_NAME))
          continue;
      }
      if (CASETYPECODE.PARTICIPANTDATACASE.equals(type)) {
        if (!processInstanceSearchDetails.processName.equals(
          BDMOASWorkflowConstants.OUTSTANDING_VERIFICATION_TASK_PERAON_WORKFLOW_NAME))
          continue;
      }

      // Read the Process Instance WDO snapshot
      final ProcessInstanceID processInstanceIDObj = new ProcessInstanceID();
      processInstanceIDObj.processInstanceID =
        processInstanceSearchDetails.processInstanceID;
      final ProcessInstanceFullDetails processInstanceFullDetails =
        ProcessInstanceAdmin.readProcessInstance(processInstanceIDObj);

      // If snapshot data contains the input task type return the taskID

      for (final ProcInstData instData : processInstanceFullDetails.data) {
        if (instData.wdoAttributeName.equals("evidenceID")) {

          final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey =
            new RelatedIDAndEvidenceTypeKey();
          relatedIDAndEvidenceTypeKey.relatedID =
            Long.parseLong(instData.wdoAttributeValue);
          relatedIDAndEvidenceTypeKey.evidenceType = evidenceType;
          final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
          final EvidenceDescriptorDtls evidenceDescriptorDtls =
            EvidenceDescriptorFactory.newInstance().readByRelatedIDAndType(
              notFoundIndicator, relatedIDAndEvidenceTypeKey);
          if (!notFoundIndicator.isNotFound())
            if (evidenceDescriptorDtls.statusCode
              .equals(EVIDENCEDESCRIPTORSTATUS.ACTIVE))
              taskList.add(taskID);
        }

      }

    }
    return taskList;

  }

  /**
   * Gets the task id from evidence ID.Evidence Id configured with
   * BizObjAssociation
   *
   * @param evidenceID the evidence ID
   * @return the task id from evidence ID
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public Long getTaskIdFromEvidenceID(final Long evidenceID)
    throws AppException, InformationalException {

    final BizObjAssociation bizObjAssoc =
      BizObjAssociationFactory.newInstance();
    final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
    bizObjectTypeKey.bizObjectType = BIZOBJASSOCIATION.OAS_EVIDENCEID;
    bizObjectTypeKey.bizObjectID = evidenceID;
    final BizObjAssocSearchDetailsList bizObjAssocList =
      bizObjAssoc.searchByBizObjectTypeAndID(bizObjectTypeKey);
    if (bizObjAssocList.dtls.isEmpty()) {
      return 0l;
    }
    // Loop through bizObjAssociations
    for (final BizObjAssocSearchDetails bizObjSearchDetailsObj : bizObjAssocList.dtls) {

      final long taskID = bizObjSearchDetailsObj.taskID;

      // Read task wdo snapshot
      final TaskAdmin taskAdminObj = TaskAdminFactory.newInstance();
      final TaskDetailsWithSnapshot taskDetailsWithSnapshot =
        taskAdminObj.readDetailsWithSnapshot(taskID);

      // Skip closed task
      if (TASKSTATUS.CLOSED.equals(taskDetailsWithSnapshot.status)) {
        continue;
      } else
        return taskID;

    }
    return 0l;
  }

  /**
   * Creates the verify authorized person task.
   *
   * @param caseID the case ID
   * @param participantName the participant name
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  public static void createVerifyAuthorizedPersonTask(final Long caseID,
    final String participantName)
    throws AppException, InformationalException {

    final List<Object> enactmentStructs = new ArrayList<Object>();

    final BDMParticipantAndCaseEnactDetails enactmentDetails =
      new BDMParticipantAndCaseEnactDetails();
    enactmentDetails.caseID = caseID;
    enactmentDetails.participantName = participantName;
    enactmentDetails.assignedTo = Configuration
      .getProperty(EnvVars.BDM_ENV_BDM_DEFAULT_CASEOWNER_WORKQUEUE);
    enactmentDetails.assigneeType = TARGETITEMTYPE.WORKQUEUE;

    enactmentStructs.add(enactmentDetails);

    EnactmentService.startProcess(
      BDMOASWorkflowConstants.VERIFY_AUTHORIZED_PERSON_TASK_WORKFLOW_NAME,
      enactmentStructs);
  }

}
