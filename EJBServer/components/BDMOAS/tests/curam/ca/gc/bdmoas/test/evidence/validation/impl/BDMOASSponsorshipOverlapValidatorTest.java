package curam.ca.gc.bdmoas.test.evidence.validation.impl;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdmoas.evidence.constants.impl.OASSponsorshipConstants;
import curam.ca.gc.bdmoas.evidence.validation.impl.BDMOASSponsorshipOverlapValidator;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdmoas.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.CASEEVIDENCE;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.struct.CaseKey;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * The Class tests {@link BDMOASSponsorshipOverlapValidator} custom
 * function.
 */

@RunWith(JMockit.class)
public class BDMOASSponsorshipOverlapValidatorTest
  extends CustomFunctionTestJunit4 {

  /** The entity functions. */
  @Mocked
  BDMEvidenceUtil bdmEvidenceUtil;

  @Mocked
  BDMApplicationEventsUtil BDMApplicationEventsUtil;

  @Mocked
  BDMPhoneEvidence bdmPhoneEvidence;

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  /** The testing functions. */

  BDMEvidenceUtilsTest bdmEvidenceUtils;

  /** The validated function. */
  @Tested
  BDMOASSponsorshipOverlapValidator BDMOASSponsorshipOverlapValidator;

  @Mocked
  EvidenceControllerInterface evidenceController;

  @Mocked
  CaseKey caseKey;

  ApplyChangesEvidenceLists evidenceLists;

  /**
   * Before each test, init the tested class and other objects.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    BDMOASSponsorshipOverlapValidator = new BDMOASSponsorshipOverlapValidator(
      evidenceController, caseKey, evidenceLists);
    bdmEvidenceUtils = new BDMEvidenceUtilsTest();

  }

  /**
   * Test that the getEndDate when breakdown is set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_getEndDate_Breakdown()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_SPONSORSHIP_AGREEMENT, Date.getCurrentDate());
    dynamicEvidenceDataDetails.setID(pdcPersonDetails.concernRoleID);
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidenceDataDetails
      .getAttribute(OASSponsorshipConstants.END_DATE),
      Date.getDate("20220505"));
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidenceDataDetails
      .getAttribute(OASSponsorshipConstants.BREAKDOWN_DATE),
      Date.getDate("20210505"));

    final Date endDate = BDMOASSponsorshipOverlapValidator
      .getEndDate(dynamicEvidenceDataDetails);

    assertEquals("End date retrieved incorrectly.", endDate,
      Date.getDate("20210505"));

  }

  /**
   * Test that the getEndDate when breakdown is not set.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_getEndDate_EndDate()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_SPONSORSHIP_AGREEMENT, Date.getCurrentDate());
    dynamicEvidenceDataDetails.setID(pdcPersonDetails.concernRoleID);
    DynamicEvidenceTypeConverter.setAttribute(dynamicEvidenceDataDetails
      .getAttribute(OASSponsorshipConstants.END_DATE),
      Date.getDate("20220505"));

    final Date endDate = BDMOASSponsorshipOverlapValidator
      .getEndDate(dynamicEvidenceDataDetails);

    assertEquals("End date retrieved incorrectly.", endDate,
      Date.getDate("20220505"));

  }

  /**
   * Test that the getEndDate when is empty.
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_getEndDate_Empty()
    throws AppException, InformationalException, NoSuchSchemaException {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_SPONSORSHIP_AGREEMENT, Date.getCurrentDate());
    dynamicEvidenceDataDetails.setID(pdcPersonDetails.concernRoleID);

    final Date endDate = BDMOASSponsorshipOverlapValidator
      .getEndDate(dynamicEvidenceDataDetails);

    assertEquals("End date retrieved incorrectly.", endDate, Date.kZeroDate);

  }

}
