package curam.ca.gc.bdm.test.ws.impl;

import curam.ca.gc.bdm.rest.bdmwsaddressapi.fact.BDMWSAddressAPIFactory;
import curam.ca.gc.bdm.rest.bdmwsaddressapi.struct.BDMWSAddressList;
import curam.ca.gc.bdm.rest.bdmwsaddressapi.struct.BDMWSAddressSearchKey;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMWSAddressRESTAPITest extends CuramServerTestJUnit4 {

  private BDMWSAddressSearchKey key;

  @Ignore
  @Test
  public void testSearchAddressesByPostalCode()
    throws AppException, InformationalException {

    // SETUP

    key = new BDMWSAddressSearchKey();
    key.postalCode = "T2Z0B5";

    // enable the wsaddress rest call
    Configuration.setProperty(
      EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT, CuramConst.gkTrue);

    // EXERCISE

    final BDMWSAddressList addressReturnList =
      BDMWSAddressAPIFactory.newInstance().searchAddress(key);

    // VERIFY
    assertEquals(addressReturnList.data.size() > 0, true);

  }

}
