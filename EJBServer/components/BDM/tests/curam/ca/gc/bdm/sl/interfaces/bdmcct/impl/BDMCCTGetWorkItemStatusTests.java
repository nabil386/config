package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetWorkItemStatusRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetWorkItemStatusResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.stubs.BDMCCTOutboundInterfaceHttpStatusStub;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.stubs.BDMCCTOutboundInterfaceIOExceptionStub;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.resources.Configuration;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Suite of tests for the getWorkItemStatus() function in the
 * BDMCCTOutboundInterfaceImpl class.
 */
@Ignore
public class BDMCCTGetWorkItemStatusTests
  extends BDMCCTOutboundInterfaceImplTest {

  /**
   * Test getWorkItemStatus() for scenario where valid parameters are passed in.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_success() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setWorkItemID(31);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    final BDMCCTGetWorkItemStatusResponse response =
      interfaceObj.getWorkItemStatus(request);

    assertNotNull(response);
    assertEquals(response.getWorkItemStatusID(), 3L);
    assertEquals(response.getWorkItemStatusName(), "ACTIVE_INCOMPLETE");
    assertTrue(response.getWorkItemStatusURL().contains("workitemid=31"));
    assertTrue(
      response.getWorkItemStatusURL().contains("community=Pensions"));
  }

  /**
   * Test getWorkItemStatus() for scenario where valid parameters are passed in.
   *
   * @throws Exception
   */
  @Test
  public void testGetWorkItemStatus_changeProperties() throws Exception {

    final String previousSubKey =
      Configuration.getProperty(EnvVars.BDM_OCP_APIM_SUBSCRIPTION_KEY);

    Configuration.setProperty(EnvVars.BDM_OCP_APIM_SUBSCRIPTION_KEY, "");

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setWorkItemID(31);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));

    Configuration.setProperty(EnvVars.BDM_OCP_APIM_SUBSCRIPTION_KEY,
      previousSubKey);
  }

  /**
   * Test getWorkItemStatus() for scenario where an empty userid is provided for
   * the request.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_emptyUserId() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("");
    request.setWorkItemID(44207);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
    // BUG 93427

  }

  /**
   * Test getWorkItemStatus() for a scenario where the community field for the
   * request is empty.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_emptyCommunity() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("tester");
    request.setWorkItemID(44207);
    request.setCommunity("");

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_COMMUNITY_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
    // BUG 92734
  }

  /**
   * Test getWorkItemStatus() for a scenario where the workItemId field for the
   * request is empty.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_emptyWorkItemId() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("tester");
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_ID_NON_ZERO_POSITIVE_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
    // BUG 92734
  }

  /**
   * Test getWorkItemStatus() for a scenario where the workItemId provided for
   * the
   * request is a value that is <= 0.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_negativeWorkItemId() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("tester");
    request.setWorkItemID(-1);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_ID_NON_ZERO_POSITIVE_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
    // BUG 92734

  }

  /**
   * Test getWorkItemStatus() for a scenario where the community field for the
   * request is invalid (non-existent or isn't the parent of the workItemId
   * provided).
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_invalidCommunity() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("tester");
    request.setWorkItemID(44207);
    request.setCommunity("test-community");

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_CCT_HTTP_401_UNAUTHORIZED);
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
    // BUG 92734
  }

  /**
   * Test getWorkItemStatus() for a scenario where the workItemId field for the
   * request is one that does not exist (meaning there is no work item in the
   * community with the id provided).
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_invalidWorkItemId() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("tester");
    request.setWorkItemID(99999);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
    // BUG 92734
  }

  /**
   * Test getWorkItemStatus() for a scenario where the correspondence mode value
   * is empty. Due to it being optional, the call will still succeed.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_emptyCorrespondenceMode()
    throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setWorkItemID(31);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setCorrespondenceMode("");

    final BDMCCTGetWorkItemStatusResponse response =
      interfaceObj.getWorkItemStatus(request);

    assertNotNull(response);
    assertEquals(response.getWorkItemStatusID(), 3L);
    assertEquals(response.getWorkItemStatusName(), "ACTIVE_INCOMPLETE");
    assertTrue(response.getWorkItemStatusURL().contains("workitemid=31"));
    assertTrue(
      response.getWorkItemStatusURL().contains("community=Pensions"));
    assertNull(response.getWorkItemData());
  }

  /**
   * Test getWorkItemStatus() for a scenario where the correspondence mode value
   * is invalid. Due to it being optional, the call will still succeed.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_invalidCorrespondenceMode()
    throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setWorkItemID(31);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setCorrespondenceMode("review");

    final BDMCCTGetWorkItemStatusResponse response =
      interfaceObj.getWorkItemStatus(request);

    assertNotNull(response);
    assertEquals(response.getWorkItemStatusID(), 3L);
    assertEquals(response.getWorkItemStatusName(), "ACTIVE_INCOMPLETE");
    assertTrue(response.getWorkItemStatusURL().contains("workitemid=31"));
    assertTrue(
      response.getWorkItemStatusURL().contains("community=Pensions"));
    assertNull(response.getWorkItemData());
  }

  /**
   * Test getWorkItemStatus() for a scenario where the correspondence mode value
   * is valid.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_validCorrespondenceMode()
    throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setWorkItemID(31);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setCorrespondenceMode("0");

    final BDMCCTGetWorkItemStatusResponse response =
      interfaceObj.getWorkItemStatus(request);

    assertNotNull(response);
    assertEquals(response.getWorkItemStatusID(), 3L);
    assertEquals(response.getWorkItemStatusName(), "ACTIVE_INCOMPLETE");
    assertTrue(response.getWorkItemStatusURL().contains("workitemid=31"));
    assertTrue(
      response.getWorkItemStatusURL().contains("community=Pensions"));
    assertNull(response.getWorkItemData());
  }

  /**
   * Test getWorkItemStatus() for a scenario where an IOException is raised.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetWorkItemStatus_raiseIOException() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setCorrespondenceMode("1");
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceIOExceptionStub interfaceObj =
      new BDMCCTOutboundInterfaceIOExceptionStub();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
  }

  /**
   * Test getWorkItemStatus() check that it handles a HTTP 500 error and
   * returns an AppException.
   *
   * @throws Exception
   */
  @Test
  public void testGetWorkItemStatus_internalServerError() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setCorrespondenceMode("1");
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceHttpStatusStub(Integer
        .toString(BDMConstants.kHTTP_STATUS_INTERNAL_SERVER_ERROR_500));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
  }

  /**
   * Test getWorkItemStatus() check that it handles a HTTP 503 error and
   * returns an AppException.
   *
   * @throws Exception
   */
  @Test
  public void testGetWorkItemStatus_serviceUnavailable() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setCorrespondenceMode("1");
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceHttpStatusStub(
        Integer.toString(HttpStatus.SC_SERVICE_UNAVAILABLE));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
  }
}
