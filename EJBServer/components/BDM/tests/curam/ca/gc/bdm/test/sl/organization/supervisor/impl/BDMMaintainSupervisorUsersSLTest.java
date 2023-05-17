package curam.ca.gc.bdm.test.sl.organization.supervisor.impl;

import curam.ca.gc.bdm.codetable.BDMESCALATIONLEVELS;
import curam.ca.gc.bdm.entity.fact.BDMEscalationLevelFactory;
import curam.ca.gc.bdm.entity.intf.BDMEscalationLevel;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtls;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelDtlsStruct2;
import curam.ca.gc.bdm.entity.struct.BDMEscalationLevelKeyStruct1;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMEscalationLevelString;
import curam.ca.gc.bdm.sl.organization.supervisor.fact.BDMMaintainSupervisorUsersFactory;
import curam.ca.gc.bdm.sl.organization.supervisor.impl.BDMMaintainSupervisorUsers;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
import curam.util.workflow.struct.TaskKey;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMMaintainSupervisorUsersSLTest extends CuramServerTestJUnit4 {

  // Object to be tested
  @Tested
  BDMMaintainSupervisorUsers bdmMaintainSupervisorUsers;

  @Mocked
  BizObjAssociationFactory bizObjAssociationObj;

  @Mocked
  BDMEscalationLevelFactory bdmEscalationLevelFactory;

  private void setUpBizObjAssociationList()
    throws AppException, InformationalException {

    final BizObjAssociationDtlsList bizObjAssociationDtlsList =
      new BizObjAssociationDtlsList();

    final BizObjAssociationDtls obj1 = new BizObjAssociationDtls();
    obj1.bizObjAssocID = 80000;
    obj1.taskID = 256;
    obj1.bizObjectType = BUSINESSOBJECTTYPE.BDMCOMMUNICATION;

    BizObjAssociationFactory.newInstance().insert(obj1);

    new Expectations() {

      {

        bizObjAssociationDtlsList.dtls.addRef(obj1);
        BizObjAssociationFactory.newInstance().searchByTaskID((TaskKey) any);

        result = bizObjAssociationDtlsList;

      }
    };

  }

  // Test escalation level is set and retrieved
  @Test
  public void testReadEscalationLevelByTaskID() throws Exception {

    setUpBizObjAssociationList();

    final curam.core.sl.entity.struct.TaskKey key =
      new curam.core.sl.entity.struct.TaskKey();
    key.taskID = 10;

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

    final BDMEscalationLevelString str = BDMMaintainSupervisorUsersFactory
      .newInstance().readEscalationLevelByTaskID(key);

    assertEquals("Escalation Level 1", str.escalationLevelDesc);

  }

  /***
   *
   *
   * Create BDMTAskEscalationLevel Object
   */
  private void createTaskEscalationLevel(final long commID)
    throws AppException, InformationalException {

    final BDMEscalationLevel bdmEscalationLevelObj =
      BDMEscalationLevelFactory.newInstance();

    final BDMEscalationLevelDtls escalationLevelDtls =
      new BDMEscalationLevelDtls();
    escalationLevelDtls.communicationID = commID;
    escalationLevelDtls.escalationLevel =
      BDMESCALATIONLEVELS.ESCALATION_LEVEL_1;
    escalationLevelDtls.versionNo = 1;
    BDMEscalationLevelFactory.newInstance().insert(escalationLevelDtls);

  }

}
