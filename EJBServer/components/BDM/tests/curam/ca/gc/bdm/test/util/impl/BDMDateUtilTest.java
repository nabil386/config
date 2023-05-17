package curam.ca.gc.bdm.test.util.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMDateUtilTest extends CuramServerTestJUnit4 {

  @Test
  public void testDateFormatter() throws Exception {

    final Date date = new Date();

    // Date format : yyyy/MM/dd
    final SimpleDateFormat input1 = new SimpleDateFormat(
      BDMConstants.MM_DD_YYYY_DATE_FORMAT_SLASH_DELIMITER);

    // Date format : MM-dd-yyyy
    final SimpleDateFormat input2 = new SimpleDateFormat(
      BDMConstants.MM_DD_YYYY_DATE_FORMAT_DASH_DELIMITER);

    // Date format : yyyy-MM-dd
    final SimpleDateFormat input3 = new SimpleDateFormat(
      BDMConstants.YYYY_MM_DD_DATE_FORMAT_DASH_DELIMITER);

    // Date format : yyyyMMdd
    final String expectedResult =
      new SimpleDateFormat(BDMConstants.YYYYMMDD_DATE_FORMAT).format(date);

    assertEquals(BDMDateUtil.dateFormatter(input1.format(date)),
      expectedResult);
    assertEquals(BDMDateUtil.dateFormatter(input2.format(date)),
      expectedResult);
    assertEquals(BDMDateUtil.dateFormatter(input3.format(date)),
      expectedResult);

  }
}
