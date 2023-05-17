/**
 *
 */
package curam.ca.gc.bdm.sl.supervisor.impl;

import curam.ca.gc.bdm.sl.struct.BDMWorkQueueReservedTasksForUserDetailsList;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.core.sl.supervisor.struct.WorkQueueReservedTasksForUserKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;

/**
 * @author donghua.jin
 *
 */
public class BDMWorkQueueWorkspaceProcessTest extends BDMBaseTest {

  BDMWorkQueueWorkspaceProcess impl = new BDMWorkQueueWorkspaceProcess();

  @Test
  public void testGetBDMWorkQueueReservedTasksForUser()
    throws AppException, InformationalException {

    final WorkQueueReservedTasksForUserKey key =
      new WorkQueueReservedTasksForUserKey();

    key.username = "Unauthenticated";

    final BDMWorkQueueReservedTasksForUserDetailsList list =
      impl.getBDMWorkQueueReservedTasksForUser(key);
    // For now, make sure no error.
  }
}
