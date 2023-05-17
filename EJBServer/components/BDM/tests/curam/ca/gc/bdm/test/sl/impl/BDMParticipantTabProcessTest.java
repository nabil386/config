package curam.ca.gc.bdm.test.sl.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.sl.impl.BDMParticipantTabProcess;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.PersonRegistration;
import curam.core.sl.struct.ParticipantContentDetailsXML;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.MaintainConcernRoleKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.pdc.fact.PDCAlternateNameFactory;
import curam.pdc.intf.PDCAlternateName;
import curam.pdc.struct.PDCPersonDetails;
import curam.pdc.struct.ParticipantAlternateNameDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BDMParticipantTabProcessTest extends CuramServerTestJUnit4 {

  @Inject
  BDMParticipantTabProcess bDMParticipantTabProcessTest;

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  public BDMParticipantTabProcessTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);

    return pdcPersonDetails;

  }

  @Test
  public void testReadPerson_firstLastName()
    throws AppException, InformationalException {

    // Register person
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    final PDCAlternateName pdcAlternamename =
      PDCAlternateNameFactory.newInstance();

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

    participantContentDetailsXML =
      bDMParticipantTabProcessTest.readPerson(key);

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

  @Test
  public void testReadPerson_firstName()
    throws AppException, InformationalException {

    // Register person
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    final PDCAlternateName pdcAlternamename =
      PDCAlternateNameFactory.newInstance();

    // BEGIN : Set alternate name (Preferred name)
    final AlternateNameDtls alternateNameDtls = new AlternateNameDtls();
    alternateNameDtls.concernRoleID = pdcPersonDetails.concernRoleID;
    alternateNameDtls.nameType = ALTERNATENAMETYPE.PREFERRED;
    alternateNameDtls.nameStatus = RECORDSTATUS.NORMAL;
    alternateNameDtls.firstForename = "Johnny";
    alternateNameDtls.surname = "Smith";
    // alternateNameDtls.fullName = "Jacky Murray";

    final ParticipantAlternateNameDetails altDetails =
      new ParticipantAlternateNameDetails();
    altDetails.assign(alternateNameDtls);
    pdcAlternamename.insert(altDetails);

    final MaintainConcernRoleKey key = new MaintainConcernRoleKey();
    key.concernRoleID = pdcPersonDetails.concernRoleID;

    ParticipantContentDetailsXML participantContentDetailsXML =
      new ParticipantContentDetailsXML();

    participantContentDetailsXML =
      bDMParticipantTabProcessTest.readPerson(key);

    assertTrue("Preferred name - Firstname only",
      participantContentDetailsXML.xmlPanelData
        .contains("Preferred to be called: Johnny"));

  }
}
