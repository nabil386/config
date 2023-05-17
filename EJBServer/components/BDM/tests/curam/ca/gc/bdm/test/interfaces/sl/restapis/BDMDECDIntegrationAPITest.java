package curam.ca.gc.bdm.test.interfaces.sl.restapis;

import curam.ca.gc.bdm.rest.bdmdecdrestapi.impl.BDMDECDApplicationAPI;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

public class BDMDECDIntegrationAPITest extends CuramServerTestJUnit4 {

  public BDMDECDIntegrationAPITest() {

    super();
  }

  /**
   * Before.
   */
  @Before
  public void before() {

  }

  public void test_decd_submitted_application()
    throws AppException, InformationalException, IOException {

    final URL url = new URL(
      "http://localhost:8080/Rest/v1/interface/bdm/submitted_applications");
    final HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    // Verify the response code is what is expected
    assertEquals(200, conn.getResponseCode());

    final StringBuilder responseBuilder = new StringBuilder();
    final BufferedReader in =
      new BufferedReader(new InputStreamReader(conn.getInputStream()));
    while (in.ready()) {
      responseBuilder.append(in.readLine());
    }
    final String expectedResponse = "";
    assertEquals(expectedResponse, responseBuilder.toString());

    final BDMDECDApplicationAPI decdApplicationAPI =
      new BDMDECDApplicationAPI();
    // decdApplicationAPI.listSubmittedApplications();
  }

}
