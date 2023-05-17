
package curam.bdm.test.ca.gc.bdm.evidence.pdcidentification;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMPDCIdentificationSummaryRuleSet.impl.BDMPDCIdentificationSummary;
import curam.creole.ruleclass.BDMPDCIdentificationSummaryRuleSet.impl.BDMPDCIdentificationSummary_Factory;
import curam.creole.ruleclass.PDCIdentificationRuleSet.impl.PDCIdentification;
import curam.creole.ruleclass.PDCIdentificationRuleSet.impl.PDCIdentification_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.Locale;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Task 63026 DEV: Implement SIN Masking Via Summary Rule Set
/**
 * Test SIN Masking in .
 */
public class BDMPDCIdentificationSummaryRuleSet
  extends CuramServerTestJUnit4 {

  private Session session;

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * PASS-IF the SIN in the summary starts with 5 asterisks.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void sinMaskTest() throws AppException, InformationalException {

    final PDCIdentification evidence = this.getEvidence();
    final BDMPDCIdentificationSummary summary = this.getSummary(evidence);

    evidence.altIDType().specifyValue(
      this.getAltIDType(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER));
    evidence.alternateID().specifyValue("828278788");

    final String summaryMessage =
      summary.summary().getValue().toLocale(Locale.ENGLISH);
    final String identifier =
      summaryMessage.replace("Social Insurance Number", "").trim();

    final Pattern pattern = Pattern.compile("[\\*]{5}[0-9]{4}");
    assertTrue(pattern.matcher(identifier).matches());

  }

  /**
   * PASS-IF the non-SIN summary does not contain asterisks.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void doNotMaskNonSINTest()
    throws AppException, InformationalException {

    final PDCIdentification evidence = this.getEvidence();
    final BDMPDCIdentificationSummary summary = this.getSummary(evidence);

    evidence.altIDType()
      .specifyValue(this.getAltIDType(CONCERNROLEALTERNATEID.BDM_CIDN));
    evidence.alternateID().specifyValue("828278788");

    assertFalse(
      summary.summary().getValue().toLocale(Locale.ENGLISH).contains("*"));

  }

  /**
   * Instantiates and returns the evidence rule object.
   *
   * @return
   */
  private PDCIdentification getEvidence() {

    return PDCIdentification_Factory.getFactory().newInstance(this.session);

  }

  /**
   * Instatiates and returns a summary rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private BDMPDCIdentificationSummary
    getSummary(final PDCIdentification evidence) {

    final BDMPDCIdentificationSummary summary =
      BDMPDCIdentificationSummary_Factory.getFactory().newInstance(session);

    summary.evidence().specifyValue(evidence);

    return summary;

  }

  public CodeTableItem getAltIDType(final String code) {

    return new CodeTableItem(CONCERNROLEALTERNATEID.TABLENAME, code);

  }

}
