package curam.ca.gc.bdmoas.evidence.natureofabsences.impl;

import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASNatureOfAbsencesRuleSet.impl.BDMOASNatureOfAbsences;
import curam.creole.ruleclass.BDMOASNatureOfAbsencesRuleSet.impl.BDMOASNatureOfAbsences_Factory;
import curam.creole.ruleclass.BDMOASNatureOfAbsencesSummaryRuleSet.impl.BDMOASNatureOfAbsencesSummary;
import curam.creole.ruleclass.BDMOASNatureOfAbsencesSummaryRuleSet.impl.BDMOASNatureOfAbsencesSummary_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for dynamic evidence BDMOASNatureOfAbsences
 */
public class BDMOASNatureOfAbsencesTest extends BDMOASCaseTest {

  private Session session;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * Test for summary greater than 25 characters truncated to 25 characters with
   * an ellipsis appended. The summary message must begin with a double quote.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void truncatedSummary() {

    final String absenceDetails = "12345678901234567890123456";
    final String summaryMessage = "\"1234567890123456789012345 ...";

    final BDMOASNatureOfAbsences evidence = this.getEvidence();
    evidence.absenceDetails().specifyValue(absenceDetails);

    final BDMOASNatureOfAbsencesSummary summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));

  }

  /**
   * Test for summary less than 25 characters is not truncated. The summary
   * message must be enclosed by double quotes.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void nonTruncatedSummary() {

    final String absenceDetails = "123456789012345678901234";
    final String summaryMessage = "\"" + absenceDetails + "\"";

    final BDMOASNatureOfAbsences evidence = this.getEvidence();
    evidence.absenceDetails().specifyValue(absenceDetails);

    final BDMOASNatureOfAbsencesSummary summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));

  }

  /**
   * Test for summary of 25 characters is not truncated. The summary
   * message must be enclosed by double quotes.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void fullSummary() {

    final String absenceDetails = "1234567890123456789012345";
    final String summaryMessage = "\"" + absenceDetails + "\"";

    final BDMOASNatureOfAbsences evidence = this.getEvidence();
    evidence.absenceDetails().specifyValue(absenceDetails);

    final BDMOASNatureOfAbsencesSummary summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));

  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASNatureOfAbsences getEvidence() {

    return BDMOASNatureOfAbsences_Factory.getFactory()
      .newInstance(this.session);

  }

  /**
   * Return summary information rule class.
   *
   * @param evidence
   * @return
   */
  private BDMOASNatureOfAbsencesSummary
    getSummary(final BDMOASNatureOfAbsences evidence) {

    final BDMOASNatureOfAbsencesSummary summary =
      BDMOASNatureOfAbsencesSummary_Factory.getFactory()
        .newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;

  }

}
