package curam.ca.gc.bdmoas.evidence.worldincome.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASTAXTHRESHOLD;
import curam.ca.gc.bdmoas.codetable.BDMOASYESNONA;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASWorldIncomeRuleSet.impl.BDMOASWorldIncome;
import curam.creole.ruleclass.BDMOASWorldIncomeRuleSet.impl.BDMOASWorldIncome_Factory;
import curam.creole.ruleclass.BDMOASWorldIncomeSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASWorldIncomeSummaryRuleSet.impl.SummaryInformation_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for dynamic evidence BDMOASWorldIncome
 */
public class BDMOASWorldIncomeTest extends BDMOASCaseTest {

  private Session session;

  private final CodeTableItem threshold =
    new CodeTableItem(BDMOASTAXTHRESHOLD.TABLENAME, BDMOASTAXTHRESHOLD.Y2021);

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * Test for summary when mandatory fields are missing values.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void missingValuesSummary() {

    final String summaryMessage = "Mandatory fields missing values";

    final BDMOASWorldIncome evidence = this.getEvidence();
    evidence.threshold().specifyValue(null);
    evidence.overThreshold().specifyValue(null);

    final SummaryInformation summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));

  }

  /**
   * Test for summary when client is over threshold.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void overThresholdSummary() {

    final String summaryMessage =
      "Over threshold for " + this.threshold.toLocale(Locale.ENGLISH);

    final BDMOASWorldIncome evidence = this.getEvidence();
    evidence.threshold().specifyValue(this.threshold);
    evidence.overThreshold()
      .specifyValue(this.getOverThresholdValue(BDMOASYESNONA.YES));

    final SummaryInformation summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));

  }

  /**
   * Test for summary when client is under threshold.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void underThresholdSummary() {

    final String summaryMessage =
      "Under threshold for " + this.threshold.toLocale(Locale.ENGLISH);

    final BDMOASWorldIncome evidence = this.getEvidence();
    evidence.threshold().specifyValue(this.threshold);
    evidence.overThreshold()
      .specifyValue(this.getOverThresholdValue(BDMOASYESNONA.NO));

    final SummaryInformation summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));

  }

  /**
   * Test for summary when the threshold is not applicable to the client.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void notApplicableSummary() {

    final String summaryMessage =
      "Not Applicable for " + this.threshold.toLocale(Locale.ENGLISH);

    final BDMOASWorldIncome evidence = this.getEvidence();
    evidence.threshold().specifyValue(this.threshold);
    evidence.overThreshold()
      .specifyValue(this.getOverThresholdValue(BDMOASYESNONA.NA));

    final SummaryInformation summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));

  }

  /**
   * Test that a client cannot have more than one record for a given threshold
   * year and amount.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void duplicateValidatorTest()
    throws AppException, InformationalException {

    final String validationMessage =
      "A World Income record already exists for "
        + this.threshold.toLocale(Locale.ENGLISH) + ".";

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct caseParticipantRoleStruct =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();

    attributes.put("participant",
      String.valueOf(caseParticipantRoleStruct.caseParticipantRoleID));
    attributes.put("threshold", this.threshold.code());
    attributes.put("overThreshold", BDMOASYESNONA.YES);

    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_WORLD_INCOME, attributes, getToday());

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_WORLD_INCOME, attributes, getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASWorldIncome getEvidence() {

    return BDMOASWorldIncome_Factory.getFactory().newInstance(this.session);

  }

  /**
   * Return summary information rule class.
   *
   * @param evidence
   * @return
   */
  private SummaryInformation getSummary(final BDMOASWorldIncome evidence) {

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;

  }

  private CodeTableItem getOverThresholdValue(final String code) {

    return new CodeTableItem(BDMOASYESNONA.TABLENAME, code);

  }

}
