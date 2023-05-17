package curam.ca.gc.bdm.test.interfaces.sl.gcnotify.impl;

import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.BDMGCNotifyInterfaceImpl;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMGCNotifySMSTest extends CuramServerTestJUnit4 {

  /**
   * Instantiates a new BDMGCNotify test.
   */
  public BDMGCNotifySMSTest() {

    super();

  }

  BDMGCNotifyInterfaceImpl testClass = new BDMGCNotifyInterfaceImpl();

  @Ignore
  @Test
  public void smsTestEnglish()
    throws AppException, InformationalException, IOException {

    /*
     * dtls1 = new BDMGcNotifyRequestDataDetails();
     *
     * testClass = new BDMGCNotifyInterfaceImpl();
     * dtls1.recordID = 1;
     * dtls1.emailAddress = "azar.khan@hrsdc-rhdcc.gc.ca";
     * dtls1.payeeName = "azar khan";
     * dtls1.phoneNumber = "7802003585";
     * dtlsList.dtls.add(dtls1);
     */
    final String jsonObj = "{\r\n" + "  \"phone_number\": \"7802003585\",\r\n"
      + "  \"template_id\": \"bef02bdb-2e3e-49b9-ab81-c288379beee3\",\r\n"
      + "    \"personalisation\": {\r\n" + "  \"name\": \"Azar\"\r\n"
      + "  }\r\n" + " }";

    final String response = testClass.sendGCNotifySMSRequest(jsonObj);
    System.out.println(response);

    // add logic
    assertEquals(true,
      response.contains("There is new information/changes in your account."));

  }

  @Ignore
  @Test
  public void smsTestFrench()
    throws AppException, InformationalException, IOException {

    /*
     * dtls1 = new BDMGcNotifyRequestDataDetails();
     *
     * testClass = new BDMGCNotifyInterfaceImpl();
     * dtls1.recordID = 1;
     * dtls1.emailAddress = "azar.khan@hrsdc-rhdcc.gc.ca";
     * dtls1.payeeName = "azar khan";
     * dtls1.phoneNumber = "7802003585";
     * dtlsList.dtls.add(dtls1);
     */
    final String jsonObj = "{\r\n" + "  \"phone_number\": \"7802003585\",\r\n"
      + "  \"template_id\": \"a25b57d2-0d3c-4ff5-afc1-e93d151cf549\",\r\n"
      + "    \"personalisation\": {\r\n" + "  \"name\": \"Azar\"\r\n"
      + "  }\r\n" + " }";

    final String response = testClass.sendGCNotifySMSRequest(jsonObj);
    System.out.println(response);

    // add logic
    assertEquals(true, response.contains(
      "Il y a de nouvelles informations/modifications dans votre compte."));

  }
}
