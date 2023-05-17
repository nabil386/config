package curam.ca.gc.bdm.test.ws.address.impl;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressRequest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMWSAddressRequestTest extends CuramServerTestJUnit4 {

  @Test
  public void test_setPostalCode_UpperCasesValues() {

    // SETUP
    final BDMWSAddressRequest request = new BDMWSAddressRequest();
    final String testPostalCodeStr = "abcdef";

    // EXERCISE
    request.setPostalCode(testPostalCodeStr);

    // VERIFY
    assertEquals(
      "BDMWSAddressRequest expects set Postal Codes to be uppercased",
      testPostalCodeStr.toUpperCase(), request.getPostalCode());
  }

  @Test
  public void test_setPostalCode_RemoveSpaces() {

    // SETUP
    final BDMWSAddressRequest request = new BDMWSAddressRequest();
    final String testPostalCodeStr = " AB CD EF ";
    final String expectedPostalCodeStr = "ABCDEF";

    // EXERCISE
    request.setPostalCode(testPostalCodeStr);

    // VERIFY
    assertEquals(
      "WSAddressRequest expects set Postal Codes to contain no spaces",
      expectedPostalCodeStr, request.getPostalCode());
  }

}
