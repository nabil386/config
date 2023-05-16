package curam.ca.gc.bdmoas.evidence.sponsorship.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASSponsorshipRuleSet.impl.BDMOASSponsorship;
import curam.creole.ruleclass.BDMOASSponsorshipRuleSet.impl.BDMOASSponsorship_Factory;
import curam.creole.ruleclass.BDMOASSponsorshipSummaryRuleSet.impl.BDMOASSponsorshipSummary;
import curam.creole.ruleclass.BDMOASSponsorshipSummaryRuleSet.impl.BDMOASSponsorshipSummary_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class BDMOASSponsorshipTest extends BDMOASCaseTest {

  private Session session;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  public static final String CASEPARTICIPANTROLE = "caseParticipantRole";

  public static final String ISSPONSORED = "isSponsored";

  /**
   * PASS-IF Exception thrown on multiple Sponsorship evidence records
   * creation.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void sponsorshipDuplicateTest()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(CASEPARTICIPANTROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(ISSPONSORED, BDMYESNO.YES);

    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_SPONSORSHIP, attributes, getToday());

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_SPONSORSHIP, attributes, getToday());
      });

    final String validationMessage =
      "A client may have only one Sponsorship evidence.";

    assertTrue(validationMessage.equals(exception.getMessage()));

  }

  /**
   * Test for summary with Yes for Sponsorship.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void bdmSponsorshipEvidenceYesSummary() {

    final String summaryMessage =
      "Entered Canada under a Sponsorship Agreement";

    final curam.creole.ruleclass.BDMOASSponsorshipRuleSet.impl.BDMOASSponsorship bdmOASSponsorship =
      this.getEvidence();

    bdmOASSponsorship.isSponsored()
      .specifyValue(new CodeTableItem("BDMYesNo", "BOASYN001"));

    final BDMOASSponsorshipSummary summary =
      this.getSummary(bdmOASSponsorship);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Test for summary with No for Sponsorship.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void bdmSponsorshipEvidenceNoSummary() {

    final String summaryMessage =
      "Did not enter Canada under a Sponsorship Agreement";

    final curam.creole.ruleclass.BDMOASSponsorshipRuleSet.impl.BDMOASSponsorship bdmOASSponsorship =
      this.getEvidence();

    bdmOASSponsorship.isSponsored()
      .specifyValue(new CodeTableItem("BDMYesNo", "BOASYN002"));

    final BDMOASSponsorshipSummary summary =
      this.getSummary(bdmOASSponsorship);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * Instantiates and returns a summary rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private BDMOASSponsorshipSummary
    getSummary(final BDMOASSponsorship evidence) {

    final BDMOASSponsorshipSummary summary =
      BDMOASSponsorshipSummary_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;

  }

  /**
   * Return the evidence for the session
   *
   * @return
   */
  private BDMOASSponsorship getEvidence() {

    return BDMOASSponsorship_Factory.getFactory().newInstance(this.session);

  }

}
