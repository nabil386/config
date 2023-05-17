package curam.ca.gc.bdmoas.evidence.craBirthAndDeath.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.struct.ConcernRoleKey;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

public class BDMOASCRABirthDeathTest extends BDMOASCaseTest {

  @Test
  public void testVerifyInsertCRABirthAndDeathDetails()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();

    final EIEvidenceKey createEvidence =
      createCRABirthDeathEvidence(person.registrationIDDetails.concernRoleID);

    assertNotEquals(0, createEvidence.evidenceID);

  }

  private EIEvidenceKey createCRABirthDeathEvidence(final long concernRoleID)
    throws AppException, InformationalException {

    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final long caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    // Create CRA Birth and Death evidence
    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleKey.concernRoleID);

    final Map<String, String> craBirthDeathAttributes = new HashMap<>();
    craBirthDeathAttributes.put("person",
      String.valueOf(cprObj.caseParticipantRoleID));
    craBirthDeathAttributes.put("dateOfBirth",
      formattedDate(Date.getCurrentDate()));
    craBirthDeathAttributes.put("dateOfDeath",
      formattedDate(Date.getCurrentDate()));

    return this.createEvidence(caseID, concernRoleKey.concernRoleID,
      CASEEVIDENCEEntry.OAS_CRA_BIRTH_DEATH, craBirthDeathAttributes,
      getToday());

  }

  private String formattedDate(final Date date) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    return dateFormat.format(date.getCalendar().getTime());
  }

}
