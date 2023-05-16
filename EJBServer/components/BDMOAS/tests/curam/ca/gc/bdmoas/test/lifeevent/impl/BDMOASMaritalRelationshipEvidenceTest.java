package curam.ca.gc.bdmoas.test.lifeevent.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASMARITALCHANGETYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASMARITALRELATION;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASMaritalRelationshipEvidence;
import curam.ca.gc.bdmoas.lifeevent.impl.BDMOASMaritalRelationshipEvidenceVO;
import curam.ca.gc.bdmoas.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.CASEEVIDENCE;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mockit.Expectations;
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
public class BDMOASMaritalRelationshipEvidenceTest
  extends CuramServerTestJUnit4 {

  @Mocked
  BDMEvidenceUtil BDMEvidenceUtil;

  @Tested
  BDMOASMaritalRelationshipEvidence BDMOASMaritalRelationshipEvidence;

  /** The testing functions. */
  BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmEvidenceUtils = new BDMEvidenceUtilsTest();
    BDMEvidenceUtil = new BDMEvidenceUtil();

  }

  private List<BDMOASMaritalRelationshipEvidenceVO> setUpVOList(
    final long evidenceID) throws AppException, InformationalException {

    final List<BDMOASMaritalRelationshipEvidenceVO> maritalRelationshipEvidenceList =
      new ArrayList<BDMOASMaritalRelationshipEvidenceVO>();

    final BDMOASMaritalRelationshipEvidenceVO maritalRelationshipEvidence =
      new BDMOASMaritalRelationshipEvidenceVO();

    maritalRelationshipEvidence.setEvidenceID(evidenceID);
    maritalRelationshipEvidence.setEndDate(Date.getCurrentDate());
    maritalRelationshipEvidence.setRelationshipChangeType(
      BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_SEPARATION);
    maritalRelationshipEvidence.setRelationshipStatus(
      BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);
    maritalRelationshipEvidence.setStartDate(Date.getCurrentDate());
    maritalRelationshipEvidence.setComments("Test here");

    maritalRelationshipEvidenceList.add(maritalRelationshipEvidence);

    return maritalRelationshipEvidenceList;

  }

  private List<DynamicEvidenceDataDetails> setUpDataList(final long id)
    throws AppException {

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      new ArrayList<DynamicEvidenceDataDetails>();

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(
        CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP, Date.getCurrentDate());

    dynamicEvidenceDataDetails.setID(id);
    dynamicEvidenceDataDetails.getAttribute("relationshipChangeType")
      .setValue(BDMOASMARITALCHANGETYPE.OAS_MARITAL_CHANGE_TYPE_SEPARATION);
    dynamicEvidenceDataDetails.getAttribute("relationshipStatus")
      .setValue(BDMOASMARITALRELATION.OAS_MARITAL_RELATION_MARRIED);
    dynamicEvidenceDataDetails.getAttribute("comments").setValue("Test here");
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("startDate"),
      Date.getCurrentDate());
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("endDate"),
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
  public void testGetMaritalRelationshipEvidenceValueObject()
    throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASMaritalRelationshipEvidenceVO> testmaritalRelationshipEvidenceList =
      setUpVOList(-584395839);

    final List<DynamicEvidenceDataDetails> evidenceDataDetailsList =
      setUpDataList(-584395839);
    new Expectations() {

      {
        BDMEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceTypeInACOrIC(
          anyLong, CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP);
        result = evidenceDataDetailsList;

      }
    };

    final List<BDMOASMaritalRelationshipEvidenceVO> maritalRelationshipEvidenceList =
      BDMOASMaritalRelationshipEvidence
        .getMaritalStatusEvidenceValueObject(pdcPersonDetails.concernRoleID);

    final BDMOASMaritalRelationshipEvidenceVO expectedVO =
      maritalRelationshipEvidenceList.get(0);
    final BDMOASMaritalRelationshipEvidenceVO resultVO =
      testmaritalRelationshipEvidenceList.get(0);

    assertEquals("Created evidence list should match",
      expectedVO.getEndDate(), resultVO.getEndDate());
    assertEquals("Created evidence list should match",
      expectedVO.getStartDate(), resultVO.getStartDate());
    assertEquals("Created evidence list should match",
      expectedVO.getComments(), resultVO.getComments());
    assertEquals("Created evidence list should match",
      expectedVO.getEvidenceID(), resultVO.getEvidenceID());
    assertEquals("Created evidence list should match",
      expectedVO.getRelationshipChangeType(),
      resultVO.getRelationshipChangeType());
    assertEquals("Created evidence list should match",
      expectedVO.getRelationshipStatus(), resultVO.getRelationshipStatus());

  }

  /**
   * Verifies evidence is modified correctly if it exists
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testcreateMaritalStatusEvidence_Exists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASMaritalRelationshipEvidenceVO> maritalRelationshipEvidenceList =
      setUpVOList(-584395839);

    BDMOASMaritalRelationshipEvidence.createMaritalStatusEvidence(
      pdcPersonDetails.concernRoleID, maritalRelationshipEvidenceList,
      "Test Reason Here");

    verificationModifyEvidence();
  }

  /**
   * Verifies evidence is created correctly if it doesn't exist
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testcreateMaritalStatusEvidence_NotExists() throws Exception {

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtils.createPDCPerson();

    final List<BDMOASMaritalRelationshipEvidenceVO> maritalRelationshipEvidenceList =
      setUpVOList(0);

    BDMOASMaritalRelationshipEvidence.createMaritalStatusEvidence(
      pdcPersonDetails.concernRoleID, maritalRelationshipEvidenceList,
      "Test Reason Here");

    verificationCreateEvidence();
  }

  private void verificationModifyEvidence()
    throws AppException, InformationalException {

    try {

      new Verifications() {

        {
          curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil
            .modifyEvidenceForCase(anyLong, anyLong,
              CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP,
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
              CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP, anyString, 0);
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
