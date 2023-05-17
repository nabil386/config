package curam.ca.gc.bdmoas.evidence.eligibilityentitlementoverride.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEREASON;
import curam.ca.gc.bdmoas.codetable.BDMOASOVERRIDEVALUE;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASEligibilityEntitlementOverrideRuleSet.impl.BDMOASEligibilityEntitlementOverride;
import curam.creole.ruleclass.BDMOASEligibilityEntitlementOverrideRuleSet.impl.BDMOASEligibilityEntitlementOverride_Factory;
import curam.creole.ruleclass.BDMOASEligibilityEntitlementOverrideSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASEligibilityEntitlementOverrideSummaryRuleSet.impl.SummaryInformation_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableAdminListAllItemsAndDefaultOut;
import curam.util.administration.struct.CodeTableItemDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for dynamic evidence BDMOASEligibilityEntitlementOverride
 */
public class BDMOASEligibilityEntitlementOverrideSummaryTest
  extends BDMOASCaseTest {

  private Session session;

  private final CodeTableItem oasPension =
    new CodeTableItem(BDMOASOVERRIDEBENEFITTYPE.TABLENAME,
      BDMOASOVERRIDEBENEFITTYPE.OLD_AGE_SECURITY_PENSION);

  private final CodeTableItem gis =
    new CodeTableItem(BDMOASOVERRIDEBENEFITTYPE.TABLENAME,
      BDMOASOVERRIDEBENEFITTYPE.GUARANTEED_INCOME_SUPPLEMENT);

  private static final String MINISTER_OVERRIDE_REASON =
    " (Minister Override)";

  private static final String FROM_DATE_STRING = "2021-01-01";

  private static final Date FROM_DATE = Date.fromISO8601("20210101");

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * Test for summary of value Considered 60-years-old.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void considerd60Summary() {

    final String summaryMessage = "Considered 60-years-old  "
      + FROM_DATE_STRING + MINISTER_OVERRIDE_REASON;

    final BDMOASEligibilityEntitlementOverride evidence = this.getEvidence();
    evidence.overrideValue()
      .specifyValue(this.getOverrideValue(BDMOASOVERRIDEVALUE.SIXTY));
    evidence.fromDate().specifyValue(FROM_DATE);

    final SummaryInformation summary = this.getSummary(evidence);

    final String summaryMessageResult =
      summary.summary().getValue().toLocale(Locale.ENGLISH);

    assertTrue(summaryMessageResult.equals(summaryMessage));

  }

  /**
   * Test for summary of value Country of Residence.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void countryOfResidenceSummary() {

    final CodeTableItem value =
      this.getOverrideValue(BDMOASOVERRIDEVALUE.COUNTRY_OF_RESIDENCE);

    final String summaryMessage =
      value.toLocale(Locale.ENGLISH) + MINISTER_OVERRIDE_REASON;

    final BDMOASEligibilityEntitlementOverride evidence = this.getEvidence();
    evidence.overrideValue().specifyValue(value);

    final SummaryInformation summary = this.getSummary(evidence);

    final String summaryMessageResult =
      summary.summary().getValue().toLocale(Locale.ENGLISH);

    assertTrue(summaryMessageResult.equals(summaryMessage));

  }

  /**
   * Test for summary of value Historical Residence.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void historicalResidenceSummary() {

    final CodeTableItem value =
      this.getOverrideValue(BDMOASOVERRIDEVALUE.HISTORICAL_RESIDENCE);

    final String summaryMessage =
      value.toLocale(Locale.ENGLISH) + MINISTER_OVERRIDE_REASON;

    final BDMOASEligibilityEntitlementOverride evidence = this.getEvidence();
    evidence.overrideValue().specifyValue(value);

    final SummaryInformation summary = this.getSummary(evidence);

    final String summaryMessageResult =
      summary.summary().getValue().toLocale(Locale.ENGLISH);

    assertTrue(summaryMessageResult.equals(summaryMessage));

  }

  /**
   * Test for summary of value Is Eligible.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void eligibleSummary() {

    final CodeTableItem value =
      this.getOverrideValue(BDMOASOVERRIDEVALUE.IS_ELIGIBLE);

    final String summaryMessage =
      "Eligible for " + this.oasPension + MINISTER_OVERRIDE_REASON;

    final BDMOASEligibilityEntitlementOverride evidence = this.getEvidence();
    evidence.overrideValue().specifyValue(value);
    evidence.benefitType().specifyValue(this.oasPension);

    final SummaryInformation summary = this.getSummary(evidence);

    final String summaryMessageResult =
      summary.summary().getValue().toLocale(Locale.ENGLISH);

    assertTrue(summaryMessageResult.equals(summaryMessage));

  }

  /**
   * Test for summary of value Is Not Eligible.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void ineligibleSummary() {

    final CodeTableItem value =
      this.getOverrideValue(BDMOASOVERRIDEVALUE.IS_NOT_ELIGIBLE);

    final String summaryMessage =
      "Ineligible for " + this.oasPension + MINISTER_OVERRIDE_REASON;

    final BDMOASEligibilityEntitlementOverride evidence = this.getEvidence();
    evidence.overrideValue().specifyValue(value);
    evidence.benefitType().specifyValue(this.oasPension);

    final SummaryInformation summary = this.getSummary(evidence);

    final String summaryMessageResult =
      summary.summary().getValue().toLocale(Locale.ENGLISH);

    assertTrue(summaryMessageResult.equals(summaryMessage));

  }

  /**
   * Test for summary of value Entitlement Amount.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void entitlementAmountSummary() {

    final CodeTableItem value =
      this.getOverrideValue(BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);

    final String summaryMessage = "Entitled to 1,000 "
      + this.oasPension.toLocale(Locale.ENGLISH) + MINISTER_OVERRIDE_REASON;

    final BDMOASEligibilityEntitlementOverride evidence = this.getEvidence();
    evidence.overrideValue().specifyValue(value);
    evidence.dollarAmount().specifyValue(1000);
    evidence.benefitType().specifyValue(this.oasPension);

    final SummaryInformation summary = this.getSummary(evidence);

    final String summaryMessageResult =
      summary.summary().getValue().toLocale(Locale.ENGLISH);

    assertTrue(summaryMessageResult.equals(summaryMessage));

  }

  /**
   * Test for summary of value 40ths.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void fortiethsSummary() {

    final CodeTableItem value =
      this.getOverrideValue(BDMOASOVERRIDEVALUE.FORTIETHS);

    final String summaryMessage =
      "3 " + value.toLocale(Locale.ENGLISH) + MINISTER_OVERRIDE_REASON;

    final BDMOASEligibilityEntitlementOverride evidence = this.getEvidence();
    evidence.overrideValue().specifyValue(value);
    evidence.numberAmount().specifyValue(3);
    evidence.benefitType().specifyValue(this.oasPension);

    final SummaryInformation summary = this.getSummary(evidence);

    final String summaryMessageResult =
      summary.summary().getValue().toLocale(Locale.ENGLISH);

    assertTrue(summaryMessageResult.equals(summaryMessage));

  }

  /**
   * Test for summary of value Deferral Months.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void deferralMonthsSummary() {

    final CodeTableItem value =
      this.getOverrideValue(BDMOASOVERRIDEVALUE.DEFERRAL_MONTHS);

    final String summaryMessage =
      "3 " + value.toLocale(Locale.ENGLISH) + MINISTER_OVERRIDE_REASON;

    final BDMOASEligibilityEntitlementOverride evidence = this.getEvidence();
    evidence.overrideValue().specifyValue(value);
    evidence.numberAmount().specifyValue(3);
    evidence.benefitType().specifyValue(this.oasPension);

    final SummaryInformation summary = this.getSummary(evidence);

    final String summaryMessageResult =
      summary.summary().getValue().toLocale(Locale.ENGLISH);

    assertTrue(summaryMessageResult.equals(summaryMessage));

  }

  /**
   * Test for summary of value Special Qualifying Factor.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void sqfSummary() {

    final CodeTableItem value =
      this.getOverrideValue(BDMOASOVERRIDEVALUE.SQF);

    final String summaryMessage = value.toLocale(Locale.ENGLISH) + " 3 for "
      + this.gis.toLocale(Locale.ENGLISH) + MINISTER_OVERRIDE_REASON;

    final BDMOASEligibilityEntitlementOverride evidence = this.getEvidence();
    evidence.overrideValue().specifyValue(value);
    evidence.numberAmount().specifyValue(3);
    evidence.benefitType().specifyValue(this.gis);

    final SummaryInformation summary = this.getSummary(evidence);

    final String summaryMessageResult =
      summary.summary().getValue().toLocale(Locale.ENGLISH);

    assertTrue(summaryMessageResult.equals(summaryMessage));

  }

  /**
   * Test for summary of value Payment Income.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void paymentIncomeSummary() {

    final CodeTableItem value =
      this.getOverrideValue(BDMOASOVERRIDEVALUE.PAYMENT_INCOME);

    final String summaryMessage = "1,000 Payment Income for "
      + this.gis.toLocale(Locale.ENGLISH) + MINISTER_OVERRIDE_REASON;

    final BDMOASEligibilityEntitlementOverride evidence = this.getEvidence();
    evidence.overrideValue().specifyValue(value);
    evidence.dollarAmount().specifyValue(1000);
    evidence.benefitType().specifyValue(this.gis);

    final SummaryInformation summary = this.getSummary(evidence);

    final String summaryMessageResult =
      summary.summary().getValue().toLocale(Locale.ENGLISH);

    assertTrue(summaryMessageResult.equals(summaryMessage));

  }

  /**
   * Test that all other values display a generic summary.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void genericSummary() throws AppException, InformationalException {

    final CodeTableAdmin codeTableAdminObject =
      CodeTableAdminFactory.newInstance();
    final CodeTableAdminListAllItemsAndDefaultOut codeTableItems =
      codeTableAdminObject.listAllItems(BDMOASOVERRIDEVALUE.TABLENAME);

    final Set<String> specialSummaryCodes = this.getSpecialSummaryCodes();

    for (final CodeTableItemDetails valueDetails : codeTableItems.dtls) {

      if (!specialSummaryCodes.contains(valueDetails.code)) {

        final CodeTableItem value = this.getOverrideValue(valueDetails.code);

        final String summaryMessage = value.toLocale(Locale.ENGLISH) + " for "
          + this.gis.toLocale(Locale.ENGLISH) + MINISTER_OVERRIDE_REASON;

        final BDMOASEligibilityEntitlementOverride evidence =
          this.getEvidence();
        evidence.overrideValue().specifyValue(value);
        evidence.benefitType().specifyValue(this.gis);

        final SummaryInformation summary = this.getSummary(evidence);

        final String summaryMessageResult =
          summary.summary().getValue().toLocale(Locale.ENGLISH);

        assertTrue(summaryMessageResult.equals(summaryMessage));

      }

    }

  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASEligibilityEntitlementOverride getEvidence() {

    final BDMOASEligibilityEntitlementOverride evidence =
      BDMOASEligibilityEntitlementOverride_Factory.getFactory()
        .newInstance(this.session);

    evidence.reason().specifyValue(new CodeTableItem(
      BDMOASOVERRIDEREASON.TABLENAME, BDMOASOVERRIDEREASON.MINISTER));

    return evidence;

  }

  /**
   * Return summary information rule class.
   *
   * @param evidence
   * @return
   */
  private SummaryInformation
    getSummary(final BDMOASEligibilityEntitlementOverride evidence) {

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;

  }

  private CodeTableItem getOverrideValue(final String code) {

    return new CodeTableItem(BDMOASOVERRIDEVALUE.TABLENAME, code);

  }

  private Set<String> getSpecialSummaryCodes() {

    final Set<String> codes = new HashSet<>();
    codes.add(BDMOASOVERRIDEVALUE.SIXTY);
    codes.add(BDMOASOVERRIDEVALUE.COUNTRY_OF_RESIDENCE);
    codes.add(BDMOASOVERRIDEVALUE.HISTORICAL_RESIDENCE);
    codes.add(BDMOASOVERRIDEVALUE.IS_ELIGIBLE);
    codes.add(BDMOASOVERRIDEVALUE.IS_NOT_ELIGIBLE);
    codes.add(BDMOASOVERRIDEVALUE.ENTITLEMENT_AMOUNT);
    codes.add(BDMOASOVERRIDEVALUE.FORTIETHS);
    codes.add(BDMOASOVERRIDEVALUE.DEFERRAL_MONTHS);
    codes.add(BDMOASOVERRIDEVALUE.SQF);
    codes.add(BDMOASOVERRIDEVALUE.PAYMENT_INCOME);
    return codes;

  }

}
