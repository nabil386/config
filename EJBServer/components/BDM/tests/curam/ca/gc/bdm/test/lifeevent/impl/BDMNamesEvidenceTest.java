package curam.ca.gc.bdm.test.lifeevent.impl;

import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidenceVO;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.datastore.impl.NoSuchSchemaException;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import java.util.List;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/*
 * The Class tests {@link BDMNamesEvidence}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class BDMNamesEvidenceTest extends CustomFunctionTestJunit4 {

  final String kFirstName = "First";

  final String kLastName = "Last";

  BDMEvidenceUtilsTest bdmEvidenceUtilsTest = new BDMEvidenceUtilsTest();

  /** The validate function. */
  @Tested
  BDMNamesEvidence bdmNamesEvidence;

  public BDMNamesEvidenceTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    bdmNamesEvidence = new BDMNamesEvidence();
  }

  /**
   * test method getNamesEvidenceValueObject
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testGetNamesEvidenceValueObject()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final List<BDMNamesEvidenceVO> bdmNamesEvidenceList =
      bdmNamesEvidence.getNamesEvidenceValueObject(concernRoleID);

    assertEquals(1, bdmNamesEvidenceList.size());
    assertEquals(pdcPersonDetails.firstForename,
      bdmNamesEvidenceList.get(0).getFirstName());

  }

  /*  *//**
         * test method createNamesEvidence
         * for scenario of creating names evidence
         *
         * @throws AppException the app exception
         * @throws InformationalException the informational exception
         *//*
            * @Test
            * public void testCreateNamesEvidence()
            * throws AppException, InformationalException {
            *
            * // Create Concern Role PDC
            * final PDCPersonDetails pdcPersonDetails =
            * bdmEvidenceUtilsTest.createPDCPerson();
            *
            * final BDMNamesEvidenceVO bdmNamesEvidenceVO = new
            * BDMNamesEvidenceVO();
            * bdmNamesEvidenceVO.setNameType(ALTERNATENAMETYPE.PREFERRED);
            * bdmNamesEvidenceVO.setFirstName(kFirstName);
            * bdmNamesEvidenceVO.setLastName(kLastName);
            * // create name evidence
            * bdmNamesEvidence.createNamesEvidence(pdcPersonDetails.
            * concernRoleID,
            * bdmNamesEvidenceVO);
            * // retrieve name evidence
            * final List<BDMNamesEvidenceVO> returnNamesList = bdmNamesEvidence
            * .getNamesEvidenceValueObject(pdcPersonDetails.concernRoleID);
            * // assert result
            * assertEquals(2, returnNamesList.size());
            * assertTrue(pdcPersonDetails.firstForename
            * .equals(returnNamesList.get(0).getFirstName())
            * || pdcPersonDetails.firstForename
            * .equals(returnNamesList.get(1).getFirstName()));
            *
            * }
            */

  /**
   * test method createNamesEvidence
   * for scenario of modifying names evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateNamesEvidence_modifyEvidence()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final List<BDMNamesEvidenceVO> bdmNamesEvidenceList =
      bdmNamesEvidence.getNamesEvidenceValueObject(concernRoleID);

    assertEquals(1, bdmNamesEvidenceList.size());
    assertEquals(pdcPersonDetails.firstForename,
      bdmNamesEvidenceList.get(0).getFirstName());

    final BDMNamesEvidenceVO bdmNamesEvidenceVO = new BDMNamesEvidenceVO();
    bdmNamesEvidenceVO
      .setEvidenceID(bdmNamesEvidenceList.get(0).getEvidenceID());
    bdmNamesEvidenceVO.setNameType(ALTERNATENAMETYPE.PREFERRED);
    bdmNamesEvidenceVO.setFirstName(kFirstName);
    bdmNamesEvidenceVO.setLastName(kLastName);
    // create name evidence
    bdmNamesEvidence.createNamesEvidence(pdcPersonDetails.concernRoleID,
      bdmNamesEvidenceVO, EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    // retrieve name evidence
    final List<BDMNamesEvidenceVO> returnNamesList = bdmNamesEvidence
      .getNamesEvidenceValueObject(pdcPersonDetails.concernRoleID);
    // assert result
    assertEquals(1, returnNamesList.size());
    assertEquals(kFirstName, returnNamesList.get(0).getFirstName());

  }
}
