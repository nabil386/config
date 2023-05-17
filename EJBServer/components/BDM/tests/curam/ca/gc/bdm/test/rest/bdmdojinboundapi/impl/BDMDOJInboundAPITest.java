package curam.ca.gc.bdm.test.rest.bdmdojinboundapi.impl;

import curam.ca.gc.bdm.codetable.BDMDOJRECORDSTATUS;
import curam.ca.gc.bdm.entity.fact.BDMDoJInboundStagingFactory;
import curam.ca.gc.bdm.entity.intf.BDMDoJInboundStaging;
import curam.ca.gc.bdm.entity.struct.BDMDoJInboundStagingDtls;
import curam.ca.gc.bdm.entity.struct.BDMDoJInboundStagingKey;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import mockit.integration.junit4.JMockit;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for class BDMDOJInboundAPITest.
 */
@RunWith(JMockit.class)
public class BDMDOJInboundAPITest extends CuramServerTestJUnit4 {

  public BDMDOJInboundAPITest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
  }

  /**
   * Test whether json is valid
   *
   *
   * @throws InformationalException the informational exception
   */
  @Ignore
  @Test
  public void test_doj_inbound() throws InformationalException, AppException {

    URL url;
    try {
      url = new URL(
        "http://localhost:8080/Rest/v1/interface/bdm/obligation-exchange");
      final HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "*/*");
      con.setRequestProperty("Connection", "keep-alive");
      con.setRequestProperty("Referer", "curam://foundational.app");

      con.setDoOutput(true);// essential for POST operation

      final File jsonSampleFile = new File(new URI(BDMDOJInboundAPITest.class
        .getResource("resources/BDM_DOJ_Inbound_Sample.json").toString()));

      final FileInputStream fis =
        new FileInputStream(jsonSampleFile.getPath());
      final String doj_Inbound_Json_Sample =
        org.apache.commons.io.IOUtils.toString(fis, "UTF-8");

      try (OutputStream os = con.getOutputStream()) {
        final byte[] input = doj_Inbound_Json_Sample.getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      final BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), "utf-8"));
      final StringBuilder response = new StringBuilder();
      String responseLine = null;
      while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
      }
      Trace.kTopLevelLogger.info("Response : " + response.toString());

      final CuramValueList<BDMDoJInboundStagingDtls> dojInboundList =
        readDOJInboundDetailsByStatus(BDMDOJRECORDSTATUS.PENDING);
      final BDMDoJInboundStaging doJInboundStagingEntityObj =
        BDMDoJInboundStagingFactory.newInstance();
      final BDMDoJInboundStagingKey dojInboundKey =
        new BDMDoJInboundStagingKey();
      dojInboundKey.dojInboundRecordID =
        dojInboundList.get(0).dojInboundRecordID;
      BDMDoJInboundStagingDtls dojInboundStagingDtls =
        new BDMDoJInboundStagingDtls();
      dojInboundStagingDtls = doJInboundStagingEntityObj.read(dojInboundKey);

      assertEquals(dojInboundStagingDtls.personSINIdentification + "",
        "800782410");
      assertEquals(dojInboundStagingDtls.personObligationIDSuffix, "A");

      // cleanup
      doJInboundStagingEntityObj.remove(dojInboundKey);

    } catch (final MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  /**
   *
   * @param status
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  CuramValueList<BDMDoJInboundStagingDtls> readDOJInboundDetailsByStatus(
    final String status) throws AppException, InformationalException {

    final StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(
      " SELECT  DIS.DOJINBOUNDRECORDID into :dojInboundRecordID  FROM BDMDoJInboundStaging DIS  ");
    sqlBuilder.append("  WHERE DIS.STATUS = '" + status + "' ");
    // sqlBuilder.append(" AND DIS.PERSONSINIDENTIFICATION = '800782410' ");
    // sqlBuilder.append(" AND DIS.PERSONOBLIGATIONIDSUFFIX = 'A' ");

    Trace.kTopLevelLogger.info(
      "BDMDoJInboundStaging  SQL to get  details : " + sqlBuilder.toString());
    final CuramValueList<BDMDoJInboundStagingDtls> dojInboundDetailsList =
      DynamicDataAccess.executeNsMulti(BDMDoJInboundStagingDtls.class, null,
        false, sqlBuilder.toString());

    return dojInboundDetailsList;
  }

}
