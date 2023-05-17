package curam.bdm.test.ca.gc.bdm.facade.taskmanagement.impl;

import curam.ca.gc.bdm.facade.taskmanagement.fact.BDMTaskManagementFactory;
import curam.ca.gc.bdm.facade.taskmanagement.intf.BDMTaskManagement;
import curam.ca.gc.bdm.facade.taskmanagement.struct.BizObjectIDKey;
import curam.ca.gc.bdm.facade.taskmanagement.struct.TaskIDAndBizObjectTypeKey;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.TASKSTATUS;
import curam.core.facade.struct.DeferTaskDetails;
import curam.core.facade.struct.TaskContextPanelDetails;
import curam.core.facade.struct.TaskManagementTaskKey;
import curam.core.facade.struct.ViewTaskDetails;
import curam.core.impl.CuramConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author hamed.mohammed
 *
 */
public class BDMTaskManagementTest extends BDMBaseTest {

  BDMTaskManagement kBdmTaskManagement =
    BDMTaskManagementFactory.newInstance();

  @Test
  public void testCloseTask() throws AppException, InformationalException {

    subscribeLoggedInUserToWorkqueues();

    long taskID = getATaskID();

    TaskManagementTaskKey taskManagementTaskKey = new TaskManagementTaskKey();

    taskManagementTaskKey.taskKey.taskID = taskID;

    ViewTaskDetails viewTaskDetails =
      kBdmTaskManagement.viewTaskHome(taskManagementTaskKey);

    kBdmTaskManagement.close(taskManagementTaskKey);

    viewTaskDetails = new ViewTaskDetails();

    viewTaskDetails = kBdmTaskManagement.viewTaskHome(taskManagementTaskKey);

    assertEquals(viewTaskDetails.taskDetails.status, TASKSTATUS.CLOSED);

  }

  @Test
  public void testDeferTask() throws AppException, InformationalException {

    subscribeLoggedInUserToWorkqueues();

    long taskID = getATaskID();

    TaskManagementTaskKey taskManagementTaskKey = new TaskManagementTaskKey();

    taskManagementTaskKey.taskKey.taskID = taskID;

    ViewTaskDetails viewTaskDetails =
      kBdmTaskManagement.viewTaskHome(taskManagementTaskKey);

    DeferTaskDetails deferTaskDetails = new DeferTaskDetails();

    deferTaskDetails.deferTaskDetails.taskID = taskID;

    deferTaskDetails.deferTaskDetails.restartDate =
      Date.getCurrentDate().addDays(90);

    deferTaskDetails.deferTaskDetails.comments =
      "This is the comments entered on deferring the task to a later day";

    kBdmTaskManagement.defer(deferTaskDetails);

    viewTaskDetails = new ViewTaskDetails();

    viewTaskDetails = kBdmTaskManagement.viewTaskHome(taskManagementTaskKey);

    assertEquals(viewTaskDetails.taskDetails.status, TASKSTATUS.DEFERRED);

  }

  @Test
  public void testGetBizObjIDByBizObjectTypeAndTaskID()
    throws AppException, InformationalException {

    long taskID = getATaskID();

    TaskIDAndBizObjectTypeKey idAndBizObjectTypeKey =
      new TaskIDAndBizObjectTypeKey();
    idAndBizObjectTypeKey.taskID = taskID;
    idAndBizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.TASKSKILLTYPE;

    BizObjectIDKey bizObjectIDKey = kBdmTaskManagement
      .getBizObjIDByBizObjectTypeAndTaskID(idAndBizObjectTypeKey);

    assertTrue(bizObjectIDKey.bizObjectID != CuramConst.kLongZero);

  }

  @Test
  public void testGetBDMTaskContextPanelDetails()
    throws AppException, InformationalException {

    long taskID = getATaskID();

    TaskManagementTaskKey taskManagementTaskKey = new TaskManagementTaskKey();
    taskManagementTaskKey.taskKey.taskID = taskID;

    TaskContextPanelDetails contextPanelDetails =
      kBdmTaskManagement.getBDMTaskContextPanelDetails(taskManagementTaskKey);

    assertTrue(contextPanelDetails.xmlPanelData.length() > CuramConst.gkZero);

    assertTrue(
      contextPanelDetails.xmlPanelData.indexOf("task-container-panel") != -1);

    assertTrue(contextPanelDetails.xmlPanelData
      .indexOf("task-context-panel-details") != -1);

    assertTrue(contextPanelDetails.xmlPanelData
      .indexOf("Organization_viewUserDetailsPage.do") != -1);

  }

}
