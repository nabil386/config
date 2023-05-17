package curam.ca.gc.bdm.test.facade.creole.rulesetadmin.impl;

import curam.ca.gc.bdm.facade.creole.rulesetadmin.fact.BDMCREOLERuleSetAdminFactory;
import curam.ca.gc.bdm.facade.creole.rulesetadmin.struct.BDMRuleSetSearchKey;
import curam.ca.gc.bdm.facade.creole.rulesetadmin.struct.BDMRuleSetSearchResultDetails;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMCREOLERuleSetAdminTest extends CuramServerTestJUnit4 {

  @Test
  public void testListAllRuleSetsForCategoryDefault()
    throws AppException, InformationalException {

    final BDMRuleSetSearchResultDetails ret = BDMCREOLERuleSetAdminFactory
      .newInstance().listAllRuleSetsForCategory(new BDMRuleSetSearchKey());

    assertEquals(ret.dtls.dtls.size(), countRuleSets(null));
  }

  @Test
  public void testListAllRuleSetsForCategoryMessae()
    throws AppException, InformationalException {

    final BDMRuleSetSearchKey key = new BDMRuleSetSearchKey();
    key.displayName = "???";
    final BDMRuleSetSearchResultDetails ret = BDMCREOLERuleSetAdminFactory
      .newInstance().listAllRuleSetsForCategory(key);

    assertTrue(ret.dtls.dtls.isEmpty());
    assertTrue(ret.msg.dtls.size() > 0);
  }

  private long countRuleSets(String query)
    throws AppException, InformationalException {

    if (query == null) {
      query =
        "SELECT COUNT(DISTINCT NAME) into :categoryID FROM CREOLERULESET";
    }

    final BDMRuleSetSearchKey ret = (BDMRuleSetSearchKey) DynamicDataAccess
      .executeNs(BDMRuleSetSearchKey.class, null, false, query);

    return ret.categoryID;
  }
}
