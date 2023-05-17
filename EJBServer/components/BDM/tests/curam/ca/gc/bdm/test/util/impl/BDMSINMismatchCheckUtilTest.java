package curam.ca.gc.bdm.test.util.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMApplicationTaskSearchImpl;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRRestResponse;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINIntegrityCheckResult;
import curam.ca.gc.bdm.util.integrity.impl.BDMSINMismatchCheckUtil;
import curam.util.resources.StringUtil;
import org.junit.Test;

public class BDMSINMismatchCheckUtilTest extends CuramServerTestJUnit4 {

  @Inject
  BDMSINMismatchCheckUtil sinMismatchCheckUtil;

  @Test
  public void testmismatchCheck() throws Exception {

    final BDMSINIntegrityCheckResult result =
      new BDMSINIntegrityCheckResult(null, null, null);
    final BDMSINSIRRestResponse sirResponse = result.getSIRResponse();

    BDMSINMismatchCheckUtil.mismatchCheck(result);
    final BDMSINMismatchCheckUtil sinMismatchCheckUtil =
      new BDMSINMismatchCheckUtil();
    BDMSINMismatchCheckUtil.mismatchCheck(result);

  }

  @Test
  public void testcalculateIdentityMatchResult() throws Exception {

    final BDMSINIntegrityCheckResult checkResult =
      new BDMSINIntegrityCheckResult(null, null, null);
    final BDMSINSIRRestResponse sirResponse = checkResult.getSIRResponse();

    final BDMSINMismatchCheckUtil sinMismatchCheckUtil =
      new BDMSINMismatchCheckUtil();
    BDMSINMismatchCheckUtil.calculateIdentityMatchResult(checkResult);

  }

  @Test
  public void testisTaskSearchByApplicationEnabled() throws Exception {

    final BDMSINIntegrityCheckResult checkResult =
      new BDMSINIntegrityCheckResult(null, null, null);
    final BDMSINSIRRestResponse sirResponse = checkResult.getSIRResponse();

    final BDMApplicationTaskSearchImpl sinMismatchCheckUtil =
      new BDMApplicationTaskSearchImpl();
    sinMismatchCheckUtil.isTaskSearchByApplicationEnabled();

  }

  private static String getFirstResponse(final String s) {

    final String s2 = convertNull(s);
    final String[] sl = s2.split(",");
    return sl[0];
  }

  private static String convertNull(final String s) {

    if (StringUtil.isNullOrEmpty(s)) {
      return "";
    }
    return s.trim();
  }
}
