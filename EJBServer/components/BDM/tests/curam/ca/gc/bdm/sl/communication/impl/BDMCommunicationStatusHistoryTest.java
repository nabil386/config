package curam.ca.gc.bdm.sl.communication.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.communication.fact.BDMCommunicationStatusHistoryFactory;
import curam.ca.gc.bdm.facade.communication.intf.BDMCommunicationStatusHistory;
import curam.ca.gc.bdm.facade.communication.struct.BDMCommunicationStatusHistoryDetailsList;
import curam.ca.gc.bdm.sl.commstatushistory.impl.BDMCommStatusHistoryDAO;
import curam.ca.gc.bdm.sl.communication.struct.BDMCommunicationStatusDetails;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.core.sl.struct.CommunicationIDKey;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit test for class BDMCommunicationStatusHistory
 */
public class BDMCommunicationStatusHistoryTest extends BDMBaseTest {

  @Inject
  BDMCommStatusHistoryDAO bdmStatusHistoryDAO;

  BDMCommunicationStatusHistory bdmCommunicationStatusHistoryObj =
    BDMCommunicationStatusHistoryFactory.newInstance();

  final curam.ca.gc.bdm.sl.communication.intf.BDMCommunication bdmCommunicationObj =
    curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory
      .newInstance();

  public BDMCommunicationStatusHistoryTest() {

    super();
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  @Test
  public void testReadHistoryByCommunicationID() throws Exception {

    final BDMCommunicationTest bdmCommunicationTest =
      new BDMCommunicationTest();

    // input param
    final CommunicationIDKey key = new CommunicationIDKey();
    key.communicationID = bdmCommunicationTest.createTestCommunication();

    final BDMCommunicationStatusDetails bdmCommStatusDtls =
      new BDMCommunicationStatusDetails();

    bdmCommStatusDtls.communicationID = key.communicationID;
    bdmCommStatusDtls.statusCode = COMMUNICATIONSTATUS.MISDIRECTED;
    bdmCommStatusDtls.statusComments = String.valueOf(Math.random());

    bdmCommunicationObj.modifyCommunicationStatus(bdmCommStatusDtls);

    final BDMCommunicationStatusHistoryDetailsList bdmCommunicationStatusHistoryDetailsList =
      bdmCommunicationStatusHistoryObj.readHistoryByCommunicationID(key);

    assertFalse(
      bdmCommunicationStatusHistoryDetailsList.statusHistoryList.isEmpty());

  }

}
