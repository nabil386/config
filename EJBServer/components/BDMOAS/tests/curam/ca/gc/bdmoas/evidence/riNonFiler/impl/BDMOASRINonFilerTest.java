package curam.ca.gc.bdmoas.evidence.riNonFiler.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

public class BDMOASRINonFilerTest extends BDMOASCaseTest {

  @Test
  public void testVerifyInsertRINonFilerDetails()
    throws AppException, InformationalException {

    // Register Person
    final PersonRegistrationResult person = this.registerPerson();

    // Submit OAS/GIS Application
    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      person.registrationIDDetails.concernRoleID, 920000);

    // Create RI Non-Filer Evidence
    final EIEvidenceKey eiEvidenceKey =
      createRINonFilerEvidence(person.registrationIDDetails.concernRoleID,
        applicationCaseKey.applicationCaseID, 1);

    // Assert evidence is created
    assertNotEquals(0, eiEvidenceKey.evidenceID);
  }

  private EIEvidenceKey createRINonFilerEvidence(final Long concernRoleID,
    final Long caseID, final int subtractYears)
    throws AppException, InformationalException {

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final Map<String, String> riNonFilerAttributes =
      new HashMap<String, String>();
    riNonFilerAttributes.put("caseParticipantRole",
      String.valueOf(cprObj.caseParticipantRoleID));
    riNonFilerAttributes.put("taxYear",
      formattedTaxYear(Date.getCurrentDate(), subtractYears));
    riNonFilerAttributes.put("nonFilerStatus", "NFST2");
    riNonFilerAttributes.put("nonFilerReason", "NFR5");

    return this.createEvidence(caseID, concernRoleID,
      CASEEVIDENCEEntry.OAS_RI_NON_FILER, riNonFilerAttributes, getToday());
  }

  private String formattedTaxYear(final Date date, final int years) {

    final Integer taxYear = date.getCalendar().get(Calendar.YEAR) - years;

    return taxYear.toString();

  }

}
