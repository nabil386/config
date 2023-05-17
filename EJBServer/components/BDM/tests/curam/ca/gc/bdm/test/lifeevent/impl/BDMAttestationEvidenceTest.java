package curam.ca.gc.bdm.test.lifeevent.impl;

import curam.ca.gc.bdm.codetable.BDMATTESTATIONTYPE;
import curam.ca.gc.bdm.lifeevent.impl.BDMAttestation;
import curam.ca.gc.bdm.lifeevent.impl.BDMAttestationVO;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.datastore.impl.NoSuchSchemaException;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.List;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/*
 * The Class tests {@link BDMAttestation}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class BDMAttestationEvidenceTest extends CustomFunctionTestJunit4 {

  BDMEvidenceUtilsTest bdmEvidenceUtilsTest = new BDMEvidenceUtilsTest();

  /** The validate function. */
  @Tested
  BDMAttestation bdmAttestation;

  public BDMAttestationEvidenceTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

    bdmAttestation = new BDMAttestation();
  }

  /**
   * test method getEvidenceValueObject
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testGetEvidenceValueObject()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final List<BDMAttestationVO> bdmAttestationVOList =
      bdmAttestation.getEvidenceValueObject(concernRoleID);
    assertEquals(0, bdmAttestationVOList.size());

  }

  /**
   * test method createAttestationEvidence
   * for scenario of creating Contact preference evidence
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testCreateAttestation()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final BDMAttestationVO bdmAttestationVO = new BDMAttestationVO();
    final List<BDMAttestationVO> bdmAttestationList =
      new ArrayList<BDMAttestationVO>();

    bdmAttestationVO.setAttestationPeriodStartDate(Date.getCurrentDate());
    bdmAttestationVO
      .setAttestationType(BDMATTESTATIONTYPE.AGENT_PORTAL_APPLICATION);
    bdmAttestationVO.setEligible(true);
    bdmAttestationList.add(bdmAttestationVO);

    // create attestation evidence
    bdmAttestation.createAttestationEvidence(pdcPersonDetails.concernRoleID,
      bdmAttestationList, EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);
    // retrieve the attestation evidence
    final List<BDMAttestationVO> bdmAttestationVOList =
      bdmAttestation.getEvidenceValueObject(pdcPersonDetails.concernRoleID);
    // assert the result
    assertEquals(0, bdmAttestationVOList.size());

  }

}
