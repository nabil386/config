package curam.ca.gc.bdm.test.rest.bdmdecdtouaapplicationapi.impl;

import curam.ca.gc.bdm.rest.bdmdecdtouaapplicationapi.impl.BDMDecdToUAApplicationAPI;
import curam.ca.gc.bdm.rest.bdmdecdtouaapplicationapi.struct.BDMDecdToUAApplicationRequestDtls;
import curam.ca.gc.bdm.rest.bdmdecdtouaapplicationapi.struct.BDMDecdToUAApplicationResponseDtls;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMDecdToUAApplicationAPITest extends CuramServerTestJUnit4 {

  @Tested
  BDMDecdToUAApplicationAPI bdmDecdToUAApplicationAPI;

  public BDMDecdToUAApplicationAPITest() {

    super();
  }

  public BDMDecdToUAApplicationRequestDtls requestDetailsForInTable() {

    final BDMDecdToUAApplicationRequestDtls bdmDecdToUAApplicationRequestDtls =
      new BDMDecdToUAApplicationRequestDtls();
    bdmDecdToUAApplicationRequestDtls.application_type = "80001";
    bdmDecdToUAApplicationRequestDtls.country_of_residence = "CA";
    bdmDecdToUAApplicationRequestDtls.current_marital_status = "Single";
    bdmDecdToUAApplicationRequestDtls.decd_guid = "123456789";
    bdmDecdToUAApplicationRequestDtls.net_income = "10000";

    return bdmDecdToUAApplicationRequestDtls;

  }

  public BDMDecdToUAApplicationRequestDtls requestDetailsForNotInTable() {

    final BDMDecdToUAApplicationRequestDtls bdmDecdToUAApplicationRequestDtls =
      new BDMDecdToUAApplicationRequestDtls();
    bdmDecdToUAApplicationRequestDtls.application_type = "80001";
    bdmDecdToUAApplicationRequestDtls.country_of_residence = "CA";
    bdmDecdToUAApplicationRequestDtls.current_marital_status = "Single";
    bdmDecdToUAApplicationRequestDtls.decd_guid = "123450000";
    bdmDecdToUAApplicationRequestDtls.net_income = "10000";

    return bdmDecdToUAApplicationRequestDtls;

  }

  @MethodRef(name = "redirectToUAApplication",
    signature = "(QBDMDecdToUAApplicationRequestDtls;)QBDMDecdToUAApplicationResponseDtls;")
  @Test
  public void testRedirectToUAApplicationNotInTable() throws Exception {

    BDMDecdToUAApplicationAPI testSubject;
    final BDMDecdToUAApplicationRequestDtls requestDtls =
      requestDetailsForNotInTable();
    BDMDecdToUAApplicationResponseDtls result;

    // default test
    testSubject = bdmDecdToUAApplicationAPI;
    result = testSubject.redirectToUAApplication(requestDtls);
    assertEquals(result.dtls.get(0).application_type, "80001");
    assertEquals(result.dtls.get(0).country_of_residence, "CA");
    assertEquals(result.dtls.get(0).current_marital_status, "Single");
    assertEquals(result.dtls.get(0).decd_guid, "123450000");
    assertEquals(result.dtls.get(0).net_income, "10000");

  }
}
