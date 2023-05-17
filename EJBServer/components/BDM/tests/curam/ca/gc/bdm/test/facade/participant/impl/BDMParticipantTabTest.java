package curam.ca.gc.bdm.test.facade.participant.impl;

import curam.ca.gc.bdm.facade.participant.impl.BDMParticipantTab;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.struct.MaintainConcernRoleKey;
import curam.core.facade.struct.ParticipantTabXML;
import curam.core.struct.AlternateNameDtls;
import curam.pdc.fact.PDCAlternateNameFactory;
import curam.pdc.intf.PDCAlternateName;
import curam.pdc.struct.PDCPersonDetails;
import curam.pdc.struct.ParticipantAlternateNameDetails;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMParticipantTabTest extends CuramServerTestJUnit4 {

  private BDMParticipantTab createTestSubject() {

    return new BDMParticipantTab();
  }

  @Test
  public void testReadPerson() throws Exception {

    final BDMEvidenceUtilsTest BDMutil = new BDMEvidenceUtilsTest();
    final MaintainConcernRoleKey key = new MaintainConcernRoleKey();
    // key.maintainConcernRoleKey.concernRoleID =
    // UniqueIDFactory.newInstance().getNextID();
    final BDMParticipantTab tab = new BDMParticipantTab();
    ParticipantTabXML result = new ParticipantTabXML();
    final PDCPersonDetails pdcPersonDetails = BDMutil.createPDCPerson();
    key.maintainConcernRoleKey.concernRoleID = pdcPersonDetails.concernRoleID;

    pdcPersonDetails.preferredName = "Jon Snow";
    // referring to testReadPerson_firstLastName test case
    final PDCAlternateName pdcAlternamename =
      PDCAlternateNameFactory.newInstance();
    final AlternateNameDtls alternateNameDtls = new AlternateNameDtls();
    alternateNameDtls.concernRoleID = pdcPersonDetails.concernRoleID;
    alternateNameDtls.nameType = ALTERNATENAMETYPE.PREFERRED;
    alternateNameDtls.nameStatus = RECORDSTATUS.NORMAL;
    alternateNameDtls.firstForename = "Jacky";
    alternateNameDtls.surname = "Murray";
    // alternateNameDtls.fullName = "Jacky Murray";

    final ParticipantAlternateNameDetails altDetails =
      new ParticipantAlternateNameDetails();
    altDetails.assign(alternateNameDtls);
    pdcAlternamename.insert(altDetails);

    try {
      result = tab.readPerson(key);
    } catch (final Exception e) {
      // could not read person
      System.out.println("could not read person");
    }

    // actual name
    assertEquals("Test otherName Person", result.description);
    // preferred name in correct format
    assertTrue("Preferred name found with correct format ",
      result.xmlPanelData.contains("Preferred to be called: Jacky Murray"));

    // preferred name in incorrect format
    assertFalse("Preferred name with incorrect format ",
      result.xmlPanelData.contains("Preferred Name: Jacky Murray"));

  }

}
