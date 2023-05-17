package curam.ca.gc.bdmoas.evidence.residenceperiod.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.OASABSENCEREASON;
import curam.ca.gc.bdmoas.codetable.OASRESIDENCETYPE;
import curam.ca.gc.bdmoas.evidence.constants.impl.OASResidencePeriodConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.message.BDMOASEVIDENCEMESSAGE;
import curam.codetable.COUNTRY;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

// 64790: Residence Period
/**
 * Test validations for OASLegalStatus evidence.
 */
public class OASResidencePeriodTest extends BDMOASCaseTest {

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

  }

  /**
   * PASS-IF a validation fails when overlapping evidences are applied.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void overlappingEvidenceFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASResidencePeriodConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(OASResidencePeriodConstants.COUNTRY, COUNTRY.CA);
    attributes.put(OASResidencePeriodConstants.RESIDENCE_TYPE,
      OASRESIDENCETYPE.RESIDENCE);
    attributes.put(OASResidencePeriodConstants.START_DATE, "19980101");

    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, attributes, getToday());

    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, attributes, getToday());

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final Exception exception =
      Assert.assertThrows(AppException.class, () -> {
        evidenceController.applyAllChanges(caseKey);
      });

    assertTrue(BDMOASEVIDENCEMESSAGE.ERR_OVERLAPPING_RESIDENCE_PERIOD
      .getMessageText().equals(exception.getMessage()));

  }

  /**
   * PASS-IF non-overlapping evidences can be applied.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void overlappingEvidencePass()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASResidencePeriodConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(OASResidencePeriodConstants.COUNTRY, COUNTRY.CA);
    attributes.put(OASResidencePeriodConstants.RESIDENCE_TYPE,
      OASRESIDENCETYPE.RESIDENCE);
    attributes.put(OASResidencePeriodConstants.START_DATE, "19980101");
    attributes.put(OASResidencePeriodConstants.END_DATE, "20170101");

    final EIEvidenceKey firstEvidenceKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, attributes, getToday());

    attributes.put(OASResidencePeriodConstants.START_DATE, "20180101");
    attributes.put(OASResidencePeriodConstants.END_DATE, "20200101");

    final EIEvidenceKey secondEvidenceKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, attributes, getToday());

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    final EIEvidenceReadDtls firstEvidence =
      evidenceController.readEvidence(firstEvidenceKey);
    final EIEvidenceReadDtls secondEvidence =
      evidenceController.readEvidence(secondEvidenceKey);

    assertTrue(firstEvidence.descriptor.statusCode
      .equals(EVIDENCEDESCRIPTORSTATUS.ACTIVE)
      && secondEvidence.descriptor.statusCode
        .equals(EVIDENCEDESCRIPTORSTATUS.ACTIVE));

  }

  /**
   * PASS-IF a validation fails when Residence Type is not entered.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void residenceTypeRequiredFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASResidencePeriodConstants.COUNTRY, COUNTRY.CA);
    attributes.put(OASResidencePeriodConstants.START_DATE, "19980101");

    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, attributes, getToday());

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final Exception exception =
      Assert.assertThrows(AppException.class, () -> {
        evidenceController.applyAllChanges(caseKey);
      });

    assertTrue(BDMOASEVIDENCEMESSAGE.ERR_RESIDENCE_TYPE_REQUIRED
      .getMessageText().equals(exception.getMessage()));

  }

  /**
   * PASS-IF a validation passes when Residence Type is entered.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void residenceTypeRequiredPass()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASResidencePeriodConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(OASResidencePeriodConstants.COUNTRY, COUNTRY.CA);
    attributes.put(OASResidencePeriodConstants.RESIDENCE_TYPE,
      OASRESIDENCETYPE.RESIDENCE);
    attributes.put(OASResidencePeriodConstants.START_DATE, "19980101");

    final EIEvidenceKey evidenceKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, attributes, getToday());

    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    final EIEvidenceReadDtls evidence =
      evidenceController.readEvidence(evidenceKey);

    assertTrue(
      evidence.descriptor.statusCode.equals(EVIDENCEDESCRIPTORSTATUS.ACTIVE));

  }

  /**
   * PASS-IF a validation fails when Reason for Absent or Intent to Return is
   * entered and the Country is not Canada.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void absenceDataForbiddenFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASResidencePeriodConstants.RESIDENCE_TYPE,
      OASRESIDENCETYPE.PRESENCE);
    attributes.put(OASResidencePeriodConstants.COUNTRY, COUNTRY.CA);
    attributes.put(OASResidencePeriodConstants.START_DATE, "19980101");
    attributes.put(OASResidencePeriodConstants.ABSENCE_REASON,
      OASABSENCEREASON.EDUCATION);

    Assert.assertThrows(InformationalException.class,
      () -> this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, attributes, getToday()));

  }

  /**
   * PASS-IF a validation passes when Reason for Absent or Intent to Return is
   * entered and the Country is not Canada.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void absenceDataForbiddenPass()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASResidencePeriodConstants.COUNTRY, COUNTRY.IRELAND);
    attributes.put(OASResidencePeriodConstants.RESIDENCE_TYPE,
      OASRESIDENCETYPE.PRESENCE);
    attributes.put(OASResidencePeriodConstants.START_DATE, "19980101");
    attributes.put(OASResidencePeriodConstants.ABSENCE_REASON,
      OASABSENCEREASON.EDUCATION);

    final EIEvidenceKey evidenceKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, attributes, getToday());

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls evidence =
      evidenceController.readEvidence(evidenceKey);

    assertTrue(
      evidence.descriptor.statusCode.equals(EVIDENCEDESCRIPTORSTATUS.INEDIT));

  }

  /**
   * PASS-IF a validation fails when End Date is not after the Start Date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void endDateAfterStartDateFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASResidencePeriodConstants.RESIDENCE_TYPE,
      OASRESIDENCETYPE.PRESENCE);
    attributes.put(OASResidencePeriodConstants.COUNTRY, COUNTRY.CA);
    attributes.put(OASResidencePeriodConstants.START_DATE, "19980101");
    attributes.put(OASResidencePeriodConstants.END_DATE, "19970101");

    Assert.assertThrows(InformationalException.class,
      () -> this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, attributes, getToday()));

  }

  /**
   * PASS-IF a validation passes when the End Date is after the Start Date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void endDateAfterStartDatePass()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASResidencePeriodConstants.COUNTRY, COUNTRY.IRELAND);
    attributes.put(OASResidencePeriodConstants.RESIDENCE_TYPE,
      OASRESIDENCETYPE.PRESENCE);
    attributes.put(OASResidencePeriodConstants.START_DATE, "19980101");
    attributes.put(OASResidencePeriodConstants.END_DATE, "19990101");

    final EIEvidenceKey evidenceKey =
      this.createEvidence(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID,
        CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, attributes, getToday());

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls evidence =
      evidenceController.readEvidence(evidenceKey);

    assertTrue(
      evidence.descriptor.statusCode.equals(EVIDENCEDESCRIPTORSTATUS.INEDIT));

  }

}
