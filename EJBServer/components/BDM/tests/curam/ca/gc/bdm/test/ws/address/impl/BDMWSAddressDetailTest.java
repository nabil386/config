package curam.ca.gc.bdm.test.ws.address.impl;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressDetail;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMWSAddressDetailTest extends CuramServerTestJUnit4 {

  public String expectedPostalCode = "ABC DEF";

  @Test
  public void
    test_getMiddleSpacedPostCode_WhenPostCodeContainsNoSpace_ReturnExpected() {

    // SETUP
    final BDMWSAddressDetail wsAddressDetail = new BDMWSAddressDetail();
    wsAddressDetail.setPostalCode("ABCDEF");

    // EXERCISE
    final String middleSpacedPostalCode =
      wsAddressDetail.getMiddleSpacedPostalCode();

    // VERIFY
    assertEquals("Expected post code to contain a middle space",
      expectedPostalCode, middleSpacedPostalCode);
  }

  @Test
  public void
    test_getMiddleSpacedPostCode_WhenPostCodeContainsIrregularSpaces_ReturnExpected() {

    // SETUP
    final BDMWSAddressDetail wsAddressDetail = new BDMWSAddressDetail();
    wsAddressDetail.setPostalCode("A BCDE F");

    // EXERCISE
    final String middleSpacedPostalCode =
      wsAddressDetail.getMiddleSpacedPostalCode();

    // VERIFY
    assertEquals("Expected post code to contain a middle space",
      expectedPostalCode, middleSpacedPostalCode);
  }

  @Test
  public void
    test_getMiddleSpacedPostCode_WhenPostCodeContainsExtraPadding_ReturnExpected() {

    // SETUP
    final BDMWSAddressDetail wsAddressDetail = new BDMWSAddressDetail();
    wsAddressDetail.setPostalCode("  ABCDEF    ");

    // EXERCISE
    final String middleSpacedPostalCode =
      wsAddressDetail.getMiddleSpacedPostalCode();

    // VERIFY
    assertEquals("Expected post code to contain a middle space",
      expectedPostalCode, middleSpacedPostalCode);
  }

  @Test
  public void
    test_getMiddleSpacedPostCode_WhenPostCodeContainsMiddleSpace_ReturnExpected() {

    // SETUP
    final BDMWSAddressDetail wsAddressDetail = new BDMWSAddressDetail();
    wsAddressDetail.setPostalCode(expectedPostalCode);

    // EXERCISE
    final String middleSpacedPostalCode =
      wsAddressDetail.getMiddleSpacedPostalCode();

    // VERIFY
    assertEquals("Expected post code to contain a middle space",
      expectedPostalCode, middleSpacedPostalCode);
  }

  @Test
  public void
    test_getMiddleSpacedPostCode_WhenPostCodeContainsEmpty_ReturnEmpty() {

    // SETUP
    final BDMWSAddressDetail wsAddressDetail = new BDMWSAddressDetail();
    wsAddressDetail.setPostalCode("");

    // EXERCISE
    final String middleSpacedPostalCode =
      wsAddressDetail.getMiddleSpacedPostalCode();

    // VERIFY
    assertEquals("Expected post code to be empty", "",
      middleSpacedPostalCode);
  }

  @Test
  public void
    test_getMiddleSpacedPostCode_WhenPostCodeContainsNull_ReturnEmpty() {

    // SETUP
    final BDMWSAddressDetail wsAddressDetail = new BDMWSAddressDetail();
    wsAddressDetail.setPostalCode(null);

    // EXERCISE
    final String middleSpacedPostalCode =
      wsAddressDetail.getMiddleSpacedPostalCode();

    // VERIFY
    assertEquals("Expected post code to be empty", "",
      middleSpacedPostalCode);
  }

}
