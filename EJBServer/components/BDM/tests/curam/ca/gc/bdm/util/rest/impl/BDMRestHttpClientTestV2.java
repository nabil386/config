package curam.ca.gc.bdm.util.rest.impl;

import curam.ca.gc.bdm.codetable.BDMAUDITAPITYPE;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.DateTime;
import java.io.IOException;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)

public class BDMRestHttpClientTestV2 {

  @Tested
  BDMRestHttpClient bdmRestHttpClient;

  @Before
  public void setUp() throws AppException, InformationalException {

  }

  @Test
  public void testMakeRequest(@Mocked
  final HttpClient httpClient, @Mocked
  final BDMRestResponse bdmRestResponse, @Mocked
  final HttpMethod httpMethod, @Mocked
  final BDMAPIAuditUtil bdmapiAuditUtil) throws Exception {

    new Expectations() {

      {
        bdmRestResponse.getStatusLine().getStatusCode();
        result = 200;

        httpMethod.releaseConnection();

        bdmapiAuditUtil.auditAPI((BDMAPIAuditDetails) any);

        new MockUp<BDMRestHttpClient>() {

          @Mock
          public BDMRestResponse getRestResponse(final HttpMethod httpMethod)
            throws IOException {

            final BDMRestResponse bdmRestResponse = new BDMRestResponse();

            return bdmRestResponse;
          }
        };
      }

    };
    final BDMRestRequest request = new BDMRestRequest();
    request.setInvokingMethod("TestMethod");
    final DateTime requestTime = DateTime.getCurrentDateTime();
    request.setRequestTransactionDateTime(requestTime);
    request.setRelatedID("1000");
    request.setSource("Source");
    request.setCorrelationID("100-100");
    bdmRestHttpClient.makeRequest(httpMethod, request);

    new Verifications() {

      {
        bdmRestResponse.getStatusLine().getStatusCode();
        times = 1;

        final BDMAPIAuditDetails bdmapiAuditDetails;
        bdmapiAuditUtil.auditAPI(bdmapiAuditDetails = withCapture());

        assertEquals(bdmapiAuditDetails.getApiType(),
          BDMAUDITAPITYPE.BDMOUTBOUND);
        assertEquals(bdmapiAuditDetails.getStatusCode(), 200);
        assertEquals(bdmapiAuditDetails.getInvokingMethod(), "TestMethod");
        assertEquals(bdmapiAuditDetails.getRequestTransactionDateTime(),
          requestTime);
        assertEquals(bdmapiAuditDetails.getSource(), "Source");
        assertEquals(bdmapiAuditDetails.getRelatedID(), "1000");
        assertEquals(bdmapiAuditDetails.getCorrelationID(), "100-100");

      }
    };

  }

}
