package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCancelPrintRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.stubs.BDMCCTOutboundInterfaceHttpStatusStub;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.stubs.BDMCCTOutboundInterfaceIOExceptionStub;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.resources.Configuration;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Suite of tests for the cancelPrint() function in the
 * BDMCCTOutboundInterfaceImpl class.
 */
@Ignore
public class BDMCCTCancelPrintTests extends BDMCCTOutboundInterfaceImplTest {

  /**
   * Test cancelPrint() for a scenario where the fields provided for the request
   * are valid.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCancelPrint_success() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    final BDMCCTCreateCorrespondenceResponse templateResponse =
      interfaceObj.createCorrespondence(testTemplate);

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setWorkItemID(templateResponse.getWorkItemID());

    interfaceObj.cancelPrint(request);

    // assertTrue(cancelPrintIndicator.getResponseCode() == 200);
  }

  /**
   * Test cancelPrint() for a scenario where the community provided for the
   * request is empty.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCancelPrint_emptyCommunityName() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    final BDMCCTCreateCorrespondenceResponse templateResponse =
      interfaceObj.createCorrespondence(testTemplate);

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity("");
    request.setWorkItemID(templateResponse.getWorkItemID());

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.cancelPrint(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_COMMUNITY_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
    // BUG 92734
    cleanUpDocument(templateResponse.getWorkItemID());
  }

  /**
   * Test cancelPrint() for a scenario where the community provided for the
   * request
   * is invalid (it is non-existent).
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCancelPrint_incorrectCommunityName() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    final BDMCCTCreateCorrespondenceResponse templateResponse =
      interfaceObj.createCorrespondence(testTemplate);

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity("unit-test-community");
    request.setWorkItemID(templateResponse.getWorkItemID());

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.cancelPrint(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_CCT_HTTP_401_UNAUTHORIZED);
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
    // BUG 92734

    cleanUpDocument(templateResponse.getWorkItemID());
  }

  /**
   * Test cancelPrint() for a scenario where the workItemId provided for the
   * request is invalid (it is non-existent within the provided community).
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCancelPrint_incorrectWorkItemId() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setWorkItemID(99999);

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.cancelPrint(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
    // BUG 92734
  }

  /**
   * Test cancelPrint() for a scenario where the workItemId provided for the
   * request is invalid (it is a value <= 0).
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCancelPrint_nonPositiveWorkItemId() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setWorkItemID(-1L);

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.cancelPrint(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_ID_NON_ZERO_POSITIVE_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
    // BUG 92734
  }

  /**
   * Test cancelPrint() for a scenario where the userId provided for the
   * request is empty.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCancelPrint_emptyUserName() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request.setUserID("");
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setWorkItemID(31);

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.cancelPrint(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
    // BUG 92734
  }

  /**
   * Test cancelPrint() for a scenario where an IOException is raised.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCancelPrint_raiseIOException() throws Exception {

    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceIOExceptionStub interfaceObj =
      new BDMCCTOutboundInterfaceIOExceptionStub();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.cancelPrint(request));
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
  }

  /**
   * Test cancelPrint() to check that it handles a HTTP 500 error and
   * returns an AppException.
   *
   * @throws Exception
   */
  @Test
  public void testCancelPrint_internalServerError() throws Exception {

    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceHttpStatusStub(Integer
        .toString(BDMConstants.kHTTP_STATUS_INTERNAL_SERVER_ERROR_500));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.cancelPrint(request));
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
  }

  /**
   * Test cancelPrint()to check that it handles a HTTP 503 error and
   * returns an AppException.
   *
   * @throws Exception
   */
  @Test
  public void testCancelPrint_serviceUnavailable() throws Exception {

    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceHttpStatusStub(
        Integer.toString(HttpStatus.SC_SERVICE_UNAVAILABLE));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.cancelPrint(request));
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
  }
}
