package curam.ca.gc.bdm.facade.externalparty.impl;

import curam.ca.gc.bdm.entity.impl.BDMExternalPartyOffice;
import curam.ca.gc.bdm.sl.externalparty.struct.BDMExternalPartyOfficeList;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.RECORDSTATUS;
import curam.core.sl.entity.struct.ExternalPartyOfficeDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeKey;
import curam.core.sl.fact.ExternalPartyOfficeFactory;
import curam.message.GENERAL;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMExternalPartyOfficeTest extends BDMBaseTest {

  BDMExternalPartyOffice bdmExternalPartyFacadeoffice =
    new BDMExternalPartyOffice();

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
  }

  @Test
  public void testmodify() throws AppException, InformationalException {

    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      listExternalPartyOffice();

    final ExternalPartyOfficeKey externalPartyOfficeKey =
      new ExternalPartyOfficeKey();
    externalPartyOfficeKey.externalPartyOfficeID =
      bdmExternalPartyOfficeList.dtls.list
        .get(0).externalPartyOfficeDtls.externalPartyOfficeID;
    final ExternalPartyOfficeDtls externalPartyOfficeDtls =
      ExternalPartyOfficeFactory.newInstance()
        .readExternalPartyOffice(externalPartyOfficeKey);

    externalPartyOfficeDtls.type = "test";
    externalPartyOfficeDtls.concernRoleID = 80011L;
    externalPartyOfficeDtls.externalPartyOfficeID =
      bdmExternalPartyOfficeList.dtls.list
        .get(0).externalPartyOfficeDtls.externalPartyOfficeID;
    externalPartyOfficeDtls.primaryAddressID = 8322652111389073409L;
    externalPartyOfficeDtls.startDate = Date.getCurrentDate();

    bdmExternalPartyFacadeoffice.modify(externalPartyOfficeKey,
      externalPartyOfficeDtls);

  }

  @Test
  public void testmodify_recordStatus()
    throws AppException, InformationalException {

    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      listExternalPartyOffice();

    final ExternalPartyOfficeKey externalPartyOfficeKey =
      new ExternalPartyOfficeKey();
    externalPartyOfficeKey.externalPartyOfficeID =
      bdmExternalPartyOfficeList.dtls.list
        .get(0).externalPartyOfficeDtls.externalPartyOfficeID;
    final ExternalPartyOfficeDtls externalPartyOfficeDtls =
      ExternalPartyOfficeFactory.newInstance()
        .readExternalPartyOffice(externalPartyOfficeKey);

    externalPartyOfficeDtls.type = "test";
    externalPartyOfficeDtls.concernRoleID = 80011L;
    externalPartyOfficeDtls.externalPartyOfficeID =
      bdmExternalPartyOfficeList.dtls.list
        .get(0).externalPartyOfficeDtls.externalPartyOfficeID;
    externalPartyOfficeDtls.primaryAddressID = 8322652111389073409L;
    externalPartyOfficeDtls.startDate = Date.getCurrentDate();
    externalPartyOfficeDtls.recordStatus = RECORDSTATUS.CANCELLED;

    bdmExternalPartyFacadeoffice.modify(externalPartyOfficeKey,
      externalPartyOfficeDtls);
    assertEquals(GENERAL.ERR_GENERAL_FV_NO_MODIFY_RECORD_CANCELLED,
      externalPartyOfficeDtls.recordStatus);

  }
}
