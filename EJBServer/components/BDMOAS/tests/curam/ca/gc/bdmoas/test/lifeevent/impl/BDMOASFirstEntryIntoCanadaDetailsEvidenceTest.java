package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.impl.BDMOASIMMIGRATIONDOCEntry;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASFirstEntryIntoCanadaDetailsEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASFirstEntryIntoCanadaDetailsEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.PROVINCETYPE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMOASFirstEntryIntoCanadaDetailsEvidenceTest
  extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASFirstEntryIntoCanadaDetailsEvidence BDMOASFirstEntryIntoCanadaDetailsEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();


  }

  private List<BDMOASFirstEntryIntoCanadaDetailsEvidenceVO> setUpVOList(
    final long evidenceID) throws AppException, InformationalException {

    final List<BDMOASFirstEntryIntoCanadaDetailsEvidenceVO> firstEntryIntoCanadaDetailsEvidenceList =
      new ArrayList<BDMOASFirstEntryIntoCanadaDetailsEvidenceVO>();

    final BDMOASFirstEntryIntoCanadaDetailsEvidenceVO firstEntryIntoCanadaDetailsEvidence =
      new BDMOASFirstEntryIntoCanadaDetailsEvidenceVO();

    firstEntryIntoCanadaDetailsEvidence.setEvidenceID(evidenceID);
    firstEntryIntoCanadaDetailsEvidence.setArrivalCity("Vancouver");
    firstEntryIntoCanadaDetailsEvidence.setArrivalDate(Date.getCurrentDate());
    firstEntryIntoCanadaDetailsEvidence
      .setArrivalProvince(PROVINCETYPE.BRITISHCOLUMBIA);
    firstEntryIntoCanadaDetailsEvidence
      .setImmigrationDoc(BDMOASIMMIGRATIONDOCEntry.OTHER.getCode());
    firstEntryIntoCanadaDetailsEvidence
      .setOtherImmigrationDocDetails("Test Immigration Document");
    firstEntryIntoCanadaDetailsEvidence.setComments("Test here");

    firstEntryIntoCanadaDetailsEvidenceList
      .add(firstEntryIntoCanadaDetailsEvidence);

    return firstEntryIntoCanadaDetailsEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("arrivalCity")
      .setValue("Vancouver");
    dynamicEvidenceDataDetails.getAttribute("arrivalProvince")
      .setValue(PROVINCETYPE.BRITISHCOLUMBIA);
    dynamicEvidenceDataDetails.getAttribute("immigrationDoc")
      .setValue(BDMOASIMMIGRATIONDOCEntry.OTHER.getCode());
    dynamicEvidenceDataDetails.getAttribute("otherImmigrationDocDetails")
      .setValue("Test Immigration Document");
    dynamicEvidenceDataDetails.getAttribute("comments").setValue("Test here");
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("arrivalDate"),
      Date.getCurrentDate());
    evidenceDataDetailsList.add(dynamicEvidenceDataDetails);

    return evidenceDataDetailsList;

  }

  /**
   * Verifies evidence is retrieved correctly.
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testGetFirstEntryIntoCanadaDetailsEvidenceValueObject()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASFirstEntryIntoCanadaDetailsEvidenceVO> testfirstEntryIntoCanadaDetailsEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASFirstEntryIntoCanadaDetailsEvidenceVO> firstEntryIntoCanadaDetailsEvidenceList =
      BDMOASFirstEntryIntoCanadaDetailsEvidence
        .getFirstEntryIntoCanadaDetailsEvidenceValueObject(
          pdcPersonDetails.concernRoleID);

    final BDMOASFirstEntryIntoCanadaDetailsEvidenceVO expectedVO =
      testfirstEntryIntoCanadaDetailsEvidenceList.get(0);
    final BDMOASFirstEntryIntoCanadaDetailsEvidenceVO resultVO =
      firstEntryIntoCanadaDetailsEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getArrivalCity(), resultVO.getArrivalCity());
    assertEquals("Created evidence list should match",
      expectedVO.getArrivalDate(), resultVO.getArrivalDate());
    assertEquals("Created evidence list should match",
      expectedVO.getComments(), resultVO.getComments());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
    assertEquals("Created evidence list should match",
      expectedVO.getImmigrationDoc(), resultVO.getImmigrationDoc());
    assertEquals("Created evidence list should match",
      expectedVO.getOtherImmigrationDocDetails(),
      resultVO.getOtherImmigrationDocDetails());

  }

  /**
   * Verifies evidence is modified correctly if it exists
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testCreateFirstEntryIntoCanadaDetailsEvidence_Exists()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASFirstEntryIntoCanadaDetailsEvidenceVO> firstEntryIntoCanadaDetailsEvidenceList =
      setUpVOList(-584395839);

    BDMOASFirstEntryIntoCanadaDetailsEvidence
      .createFirstEntryIntoCanadaDetailsEvidence(
        pdcPersonDetails.concernRoleID,
        firstEntryIntoCanadaDetailsEvidenceList, "Test Reason Here");

    verificationModifyEvidence();
  }

  /**
   * Verifies evidence is created correctly if it doesn't exist
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testCreateFirstEntryIntoCanadaDetailsEvidence_NotExists()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASFirstEntryIntoCanadaDetailsEvidenceVO> firstEntryIntoCanadaDetailsEvidenceList =
      setUpVOList(0);

    BDMOASFirstEntryIntoCanadaDetailsEvidence
      .createFirstEntryIntoCanadaDetailsEvidence(
        pdcPersonDetails.concernRoleID,
        firstEntryIntoCanadaDetailsEvidenceList, "Test Reason Here");

    verificationCreateEvidence();
  }

  private void verificationModifyEvidence()
    throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil
            .modifyEvidenceForCase(anyLong, anyLong,
              CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA,
              (HashMap<String, String>) any, anyString);
          times = 1;
        }
      };
    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should have been modified.",
        false);
    }

  }

  private void verificationCreateEvidence()
    throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil
            .createACOrICDynamicEvidence(anyLong,
              (HashMap<String, String>) any,
              CASEEVIDENCE.OAS_FIRST_ENTRY_INTO_CANADA, anyString, 0);
          times = 1;
        }
      };
    } catch (final mockit.internal.MissingInvocation e) {
      assertTrue(
        "With given attribute information, evidence should have been created.",
        false);
    }

  }
}
