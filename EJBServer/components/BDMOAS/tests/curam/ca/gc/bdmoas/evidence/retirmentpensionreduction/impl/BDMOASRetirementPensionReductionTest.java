package curam.ca.gc.bdmoas.evidence.retirmentpensionreduction.impl;

import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASRetirementPensionReductionRuleSet.impl.BDMOASRetirementPensionReduction;
import curam.creole.ruleclass.BDMOASRetirementPensionReductionRuleSet.impl.BDMOASRetirementPensionReduction_Factory;
import curam.creole.ruleclass.BDMOASRetirementPensionReductionValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.BDMOASRetirementPensionReductionValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.util.type.Date;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for dynamic evidence BDMOASRetirementPensionReduction
 */
public class BDMOASRetirementPensionReductionTest extends BDMOASCaseTest {

  private Session session;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * Test that event must be the first of a month.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void fullSummary() {

    final BDMOASRetirementPensionReduction evidence = this.getEvidence();
    evidence.eventDate().specifyValue(Date.fromISO8601("20210102"));

    final ValidationResult validator = this.getValidator(evidence);
    assertTrue(validator.eventDateFailure().getValue());

  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASRetirementPensionReduction getEvidence() {

    return BDMOASRetirementPensionReduction_Factory.getFactory()
      .newInstance(this.session);

  }

  /**
   * Return summary information rule class.
   *
   * @param evidence
   * @return
   */
  private ValidationResult
    getValidator(final BDMOASRetirementPensionReduction evidence) {

    final ValidationResult validator =
      ValidationResult_Factory.getFactory().newInstance(this.session);
    validator.evidence().specifyValue(evidence);

    return validator;

  }

}
