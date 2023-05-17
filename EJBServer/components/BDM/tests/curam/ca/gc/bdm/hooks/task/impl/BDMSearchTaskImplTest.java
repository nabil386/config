/**
 *
 */
package curam.ca.gc.bdm.hooks.task.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskQueryKey;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMTaskQueryResultDetailsList;
import curam.ca.gc.bdm.sl.taskquery.struct.BDMTaskQueryCriteria;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.ASSIGNEETYPE;
import curam.core.sl.struct.ReadMultiOperationDetails;
import curam.core.struct.Count;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * JUnit Test class for BDMSearchTaskImpl.
 *
 * @author donghua.jin
 *
 */
public class BDMSearchTaskImplTest extends BDMBaseTest {

  BDMSearchTaskImpl bdmSearchTaskImpl = new BDMSearchTaskImpl();

  @Test
  public void testValidateSearchTask()
    throws AppException, InformationalException {

    final BDMTaskQueryKey bdmSearchTaskKey = new BDMTaskQueryKey();

    bdmSearchTaskKey.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    // bdmSearchTaskKey.

    bdmSearchTaskImpl.validateSearchTask(bdmSearchTaskKey);

    // just make not getting no criteria entered exception.
    try {
      TransactionInfo.getInformationalManager().failOperation();
      fail("expect an exception before this line");
    } catch (final InformationalException ie) {
      assertTrue(ie.getMessage().contains("One of \"Assigned To\""));
      TransactionInfo.setInformationalManager();
    }
  }

  @Test
  public void testSearchTask() throws AppException, InformationalException {

    final BDMTaskQueryKey bdmSearchTaskKey = new BDMTaskQueryKey();
    final ReadMultiOperationDetails readMultiDetails =
      new ReadMultiOperationDetails();

    bdmSearchTaskKey.escalationLevel = BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    bdmSearchTaskKey.assigneeType = ASSIGNEETYPE.USER;
    bdmSearchTaskKey.assignedToID = "caseworker";

    final BDMTaskQueryResultDetailsList list =
      bdmSearchTaskImpl.searchTask(bdmSearchTaskKey, readMultiDetails);

    assertTrue(list.taskDetailsList.isEmpty());
  }

  @Test
  public void testCountTasks() throws AppException, InformationalException {

    final BDMTaskQueryCriteria bdmTaskQueryCriteria =
      new BDMTaskQueryCriteria();

    bdmTaskQueryCriteria.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    bdmTaskQueryCriteria.criteria.assigneeType = ASSIGNEETYPE.USER;
    bdmTaskQueryCriteria.criteria.assignedToID = "caseworker";

    final Count count = bdmSearchTaskImpl.countTasks(bdmTaskQueryCriteria);

    assertEquals(0, count.numberOfRecords);
  }

}
