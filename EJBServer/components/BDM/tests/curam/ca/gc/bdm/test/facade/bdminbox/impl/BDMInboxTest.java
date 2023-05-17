/**
 *
 */
package curam.ca.gc.bdm.test.facade.bdminbox.impl;

import curam.ca.gc.bdm.facade.bdminbox.fact.BDMInboxFactory;
import curam.ca.gc.bdm.facade.bdminbox.intf.BDMInbox;
import curam.ca.gc.bdm.facade.bdminbox.struct.BDMLimitedListDeferredTaskDetails;
import curam.ca.gc.bdm.facade.bdminbox.struct.BDMLimitedListReservedTaskDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskQueryKey;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMAvailableTaskSearchResult;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMDeferredTaskDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMReservedByStatusTaskDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetailsList;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.TARGETITEMTYPE;
import curam.supervisor.sl.fact.SupervisorApplicationPageContextDescriptionFactory;
import curam.supervisor.sl.struct.SupervisorUserWithTaskCountDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * @author sivakumar.kalyanasun
 *
 */
@RunWith(JMockit.class)
public class BDMInboxTest extends CuramServerTestJUnit4 {

  /* OBJECT IN TEST */
  BDMInbox bdmInboxObj;

  /* MOCKED */
  @Mocked
  SupervisorApplicationPageContextDescriptionFactory supervisorApplicationPageContextDescriptionFactory;

  /* Global Variables */
  SupervisorUserWithTaskCountDetailsList supervisorUserWithTaskCountDetailsList;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmInboxObj = BDMInboxFactory.newInstance();

    supervisorUserWithTaskCountDetailsList =
      new SupervisorUserWithTaskCountDetailsList();

  }

  /**
   * Test method for Inbox Limited reserved tasks list.
   * {@link curam.ca.gc.bdm.facade.bdminbox.impl.BDMInbox#listBDMLimitedReservedTasks()}.
   */
  @Test
  public final void testListBDMLimitedReservedTasks()
    throws AppException, InformationalException {

    final BDMLimitedListReservedTaskDetails actualOut;

    actualOut = bdmInboxObj.listBDMLimitedReservedTasks();

    if (actualOut.taskDetailsList.taskDetailsList.size() > 0) {

      final BDMReservedByStatusTaskDetails bdmReservedByStatusTaskDetails =
        actualOut.taskDetailsList.taskDetailsList.get(0);

      assertEquals("Test", bdmReservedByStatusTaskDetails.caseUrgentFlagStr);
      assertEquals("1", bdmReservedByStatusTaskDetails.priority);
      assertEquals("SUBJECT", bdmReservedByStatusTaskDetails.subject);
    }

  }

  /**
   * Test method for
   * {@link curam.ca.gc.bdm.facade.bdminbox.impl.BDMInbox#listBDMLimitedDeferredTasks()}.
   */
  @Test
  public final void testListBDMLimitedDeferredTasks()
    throws AppException, InformationalException {

    final BDMLimitedListDeferredTaskDetails bdmLimitedListDeferredTaskDetails =
      bdmInboxObj.listBDMLimitedDeferredTasks();

    if (bdmLimitedListDeferredTaskDetails.taskDetailsList.taskDetailsList
      .size() > 0) {
      final BDMDeferredTaskDetails bdmDeferredTaskDetails =
        bdmLimitedListDeferredTaskDetails.taskDetailsList.taskDetailsList
          .get(0);

      assertEquals("Test", bdmDeferredTaskDetails.caseUrgentFlagStr);
      assertEquals("1", bdmDeferredTaskDetails.priority);
      assertEquals("Test2", bdmDeferredTaskDetails.subject);
    }
  }

  /**
   * Test method for
   * {@link curam.ca.gc.bdm.facade.bdminbox.impl.BDMInbox#searchAvailableTasksExt()}.
   */
  @Test
  public final void testSearchAvailableTasksExt()
    throws AppException, InformationalException {

    final BDMAvailableTaskSearchResult bdmAvailableTaskSearchResult =
      bdmInboxObj.searchAvailableTasksExt();

    if (bdmAvailableTaskSearchResult.resultList.taskDetailsList.size() > 0) {
      final BDMTaskQueryResultDetails bdmTaskQueryResultDetails =
        bdmAvailableTaskSearchResult.resultList.taskDetailsList.get(0);

      assertEquals("Test", bdmTaskQueryResultDetails.caseUrgentFlagStr);
      assertEquals("1", bdmTaskQueryResultDetails.priority);
      assertEquals("Test2", bdmTaskQueryResultDetails.subject);
    }
  }

  /**
   * Test method for
   * {@link curam.ca.gc.bdm.facade.bdminbox.impl.BDMInbox#searchBDMForTask()}.
   */
  @Test
  public void testSearchBDMForTasks()
    throws AppException, InformationalException {

    final BDMTaskQueryKey searchTaskKey = new BDMTaskQueryKey();
    searchTaskKey.assignedToID = "caseworker";
    searchTaskKey.assigneeType = TARGETITEMTYPE.USER;

    final BDMTaskQueryResultDetailsList taskSearchResult =
      bdmInboxObj.searchBDMForTasks(searchTaskKey);

    if (taskSearchResult.taskDetailsList.size() > 0) {
      final BDMTaskQueryResultDetails bdmTaskQueryResultDetails =
        taskSearchResult.taskDetailsList.get(0);

      assertEquals("Test", bdmTaskQueryResultDetails.caseUrgentFlagStr);
      assertEquals("Test2", bdmTaskQueryResultDetails.escalationLevelDesc);
    }
  }

}
