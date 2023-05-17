package curam.ca.gc.bdm.test.util.impl;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.util.impl.BDMCacheUtil;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BDMCacheUtilTest extends CuramServerTestJUnit4 {

  @Test
  public void testsetWorkItemIDUpdatedThruAPI() throws Exception {

    BDMCacheUtil.setWorkItemIDUpdatedThruAPI(1234l);

    assertTrue(BDMCacheUtil.isWorkItemIDUpdatedThruAPI(1234l));

  }

  @Test
  public void testsetWorkItemIDUpdatedThruAPI_false() throws Exception {

    BDMCacheUtil.setWorkItemIDUpdatedThruAPI(1234l);

    assertFalse(BDMCacheUtil.isWorkItemIDUpdatedThruAPI(1235l));

  }
}
