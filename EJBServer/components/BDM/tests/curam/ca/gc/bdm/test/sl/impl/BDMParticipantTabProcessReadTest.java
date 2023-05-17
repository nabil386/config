package curam.ca.gc.bdm.test.sl.impl;

import curam.ca.gc.bdm.sl.impl.BDMParticipantTabProcess;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.sl.struct.ParticipantContentDetailsXML;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.MaintainConcernRoleKey;
import curam.pdc.fact.PDCAlternateNameFactory;
import curam.pdc.intf.PDCAlternateName;
import curam.pdc.struct.PDCPersonDetails;
import curam.pdc.struct.ParticipantAlternateNameDetails;
import javax.annotation.Generated;
import org.junit.Test;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Generated(value = "org.junit-tools-1.1.0")
public class BDMParticipantTabProcessReadTest extends CuramServerTestJUnit4 {

  private BDMParticipantTabProcess createTestSubject() {

    return new BDMParticipantTabProcess();
  }

  @MethodRef(name = "readPerson",
    signature = "(QMaintainConcernRoleKey;)QParticipantContentDetailsXML;")
  @Test
  public void testReadPerson() throws Exception {

    // referring to testReadPerson_firstLastName test case
    final BDMEvidenceUtilsTest BDMutil = new BDMEvidenceUtilsTest();
    final BDMParticipantTabProcess process = new BDMParticipantTabProcess();
    // Register person

    final PDCAlternateName pdcAlternamename =
      PDCAlternateNameFactory.newInstance();
    final PDCPersonDetails pdcPersonDetails = BDMutil.createPDCPerson();
    // BEGIN : Set alternate name (Preferred name)
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

    final MaintainConcernRoleKey key = new MaintainConcernRoleKey();
    key.concernRoleID = pdcPersonDetails.concernRoleID;

    ParticipantContentDetailsXML participantContentDetailsXML =
      new ParticipantContentDetailsXML();

    participantContentDetailsXML = process.readPerson(key);

    assertEquals("Test otherName Person",
      participantContentDetailsXML.participantName);
    assertTrue("Preferred name found with correct format ",
      participantContentDetailsXML.xmlPanelData
        .contains("Preferred to be called: Jacky Murray"));

    // preferred name in incorrect format
    assertFalse("Preferred name with incorrect format ",
      participantContentDetailsXML.xmlPanelData
        .contains("Preferred Name: Jacky Murray"));
  }
}
