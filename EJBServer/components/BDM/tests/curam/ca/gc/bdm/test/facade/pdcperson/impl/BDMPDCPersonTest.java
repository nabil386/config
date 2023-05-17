package curam.ca.gc.bdm.test.facade.pdcperson.impl;

import curam.ca.gc.bdm.codetable.BDMEDUCATIONLEVEL;
import curam.ca.gc.bdm.codetable.BDMIEGYESNOOPTIONAL;
import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.intf.BDMPerson;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonDtls;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonKey;
import curam.ca.gc.bdm.facade.pdcperson.fact.BDMPDCPersonFactory;
import curam.ca.gc.bdm.facade.pdcperson.intf.BDMPDCPerson;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonDemographicPageDetails;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMReadPersonDemographicDetails;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * Task-17494 - Junit class to manage Person demographics details
 *
 * @author rajat.soni
 *
 */
@RunWith(JMockit.class)
public class BDMPDCPersonTest extends CuramServerTestJUnit4 {

  private long concernRoleID = 0;

  @Before
  public void setUp() throws AppException, InformationalException {

    final RegisterPerson registerPersonObj = new RegisterPerson("");
    final PersonDtls personDtls = registerPersonObj
      .registerDefault("Demo Test", METHODOFDELIVERYEntry.CHEQUE);
    this.concernRoleID = personDtls.concernRoleID;

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = this.concernRoleID;
    BDMPersonDtls bdmPersonDtls = new BDMPersonDtls();

    bdmPersonDtls = new BDMPersonDtls();
    bdmPersonDtls.concernRoleID = bdmPersonKey.concernRoleID;
    bdmPersonDtls.educationLevel = BDMEDUCATIONLEVEL.COLLEGE;
    bdmPersonDtls.indigenousPersonType = BDMIEGYESNOOPTIONAL.YES;
    bdmPersonDtls.memberMinorityGrpType = BDMIEGYESNOOPTIONAL.NO;

    bdmPersonObj.insert(bdmPersonDtls);

  }

  /**
   * Junit method for readBDMNonPDCDemographicData()
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testReadBDMNonPDCDemographicData()
    throws AppException, InformationalException {

    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = this.concernRoleID;
    BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();
    bdmReadPersonDemographicDetails =
      bdmPDCPersonObj.readBDMNonPDCDemographicData(personKey);

    assertEquals(bdmReadPersonDemographicDetails.educationLevel,
      BDMEDUCATIONLEVEL.COLLEGE);
    assertEquals(bdmReadPersonDemographicDetails.indigenousPersonType,
      BDMIEGYESNOOPTIONAL.YES);
    assertEquals(bdmReadPersonDemographicDetails.memberMinorityGrpType,
      BDMIEGYESNOOPTIONAL.NO);
  }

  /**
   * Junit test method for modifyBDMNonPDCDemographicData()
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testModifyBDMNonPDCDemographicData()
    throws AppException, InformationalException {

    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();

    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = this.concernRoleID;
    BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();
    bdmReadPersonDemographicDetails =
      bdmPDCPersonObj.readBDMNonPDCDemographicData(personKey);

    final BDMPersonDemographicPageDetails bdmPersonDemographicPageDetails =
      new BDMPersonDemographicPageDetails();

    bdmPersonDemographicPageDetails.assign(bdmReadPersonDemographicDetails);

    bdmPersonDemographicPageDetails.comments = "Performing update";
    bdmPersonDemographicPageDetails.educationLevel =
      BDMEDUCATIONLEVEL.UNIVERSITY;

    bdmPDCPersonObj
      .modifyBDMNonPDCDemographicData(bdmPersonDemographicPageDetails);

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = this.concernRoleID;
    final BDMPersonDtls bdmPersonDtls = bdmPersonObj.read(bdmPersonKey);

    assertEquals(bdmPersonDtls.educationLevel, BDMEDUCATIONLEVEL.UNIVERSITY);
    assertEquals(bdmPersonDtls.indigenousPersonType, BDMIEGYESNOOPTIONAL.YES);
    assertEquals(bdmPersonDtls.memberMinorityGrpType, BDMIEGYESNOOPTIONAL.NO);

  }
}
