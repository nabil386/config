package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFResponse;
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
import static org.junit.Assert.assertThrows;

/**
 * Suite of tests for the getCompletedPDF() function in the
 * BDMCCTOutboundInterfaceImpl class.
 */
@Ignore
public class BDMCCTGetCompletedPDFTests
  extends BDMCCTOutboundInterfaceImplTest {

  /**
   * Test getCompletedPDF() for a scenario where the fields provided for the
   * request are valid.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetCompletedPDF_success() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setVaultLetterID(123456789);
    request.setWorkItemID(85152);

    final BDMCCTGetCompletedPDFResponse response =
      interfaceObj.getCompletedPDF(request);

    assertNotNull(response);
    assertNotNull(response.getDocumentBytes());
  }

  /**
   * Test getCompletedPDF() for a scenario where the community provided for
   * the request is empty
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetCompletedPDF_missingCommunity() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity("");
    request.setVaultLetterID(123456789);
    request.setWorkItemID(42640);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceImpl();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getCompletedPDF(request));
    // BUG 93427
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_COMMUNITY_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
    // BUG 93427
  }

  /**
   * Test getCompletedPDF() for a scenario where the userId provided for the
   * request is empty
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetCompletedPDF_missingUserId() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request.setUserID("");
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setVaultLetterID(123456789);
    request.setWorkItemID(42640);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceImpl();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getCompletedPDF(request));

    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
    // BUG 93427
  }

  /**
   * Test getCompletedPDF() for a scenario where the workItemId provided for
   * the request is empty
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetCompletedPDF_missingWorkItemId() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setVaultLetterID(123456789);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceImpl();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getCompletedPDF(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_ID_NON_ZERO_POSITIVE_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
    // BUG 93427
  }

  /**
   * Test getCompletedPDF() for a scenario where the vaultLetterId provided for
   * the request is missing
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetCompletedPDF_missingVaultLetterId() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setWorkItemID(42640);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceImpl();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getCompletedPDF(request));

    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_VAULT_LETTER_ID_MUST_BE_ENTERED);
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
    // BUG 92734
  }

  /**
   * Test getCompletedPDF() for a scenario where the vaultLetterId and
   * workItemId
   * provided do not exist.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetCompletedPDF_nonExistentPDF() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setVaultLetterID(24315);
    request.setWorkItemID(42640);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceImpl();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getCompletedPDF(request));
    // BUG 92734
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
    // assertEquals(exception.getMessage(),
    // "Unable to find vault document or workitem");
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
    // BUG 92734
  }

  /**
   * Test getCompletedPDF() for a scenario where the vaultLetterId and
   * workItemId
   * provided returns multiple documents.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetCompletedPDF_multipleDocumentsReturned()
    throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setVaultLetterID(5);
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceImpl();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getCompletedPDF(request));
    // BUG 92734
    // //assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
    // assertEquals(exception.getMessage(), "Multiple Documents Returned");
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
    // BUG 92734
  }

  /**
   * Test getCompletedPDF() for a scenario where an IOException is raised.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testGetCompletedPDF_raiseIOException() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setVaultLetterID(5);
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceIOExceptionStub interfaceObj =
      new BDMCCTOutboundInterfaceIOExceptionStub();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getCompletedPDF(request));
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
  }

  /**
   * Test getCompletedPDF() to check that it handles a HTTP 500 error and
   * returns
   * an AppException.
   *
   * @throws Exception
   */
  @Test
  public void testGetCompletedPDF_internalServerError() throws Exception {

    // set the input parameters
    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setVaultLetterID(123456789);
    request.setWorkItemID(85152);

    final BDMCCTOutboundInterfaceImpl interfaceStub =
      new BDMCCTOutboundInterfaceHttpStatusStub(Integer
        .toString(BDMConstants.kHTTP_STATUS_INTERNAL_SERVER_ERROR_500));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceStub.getCompletedPDF(request));
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
  }

  /**
   * Test getCompletedPDF() to check that it handles a HTTP 503 error and
   * returns
   * an AppException.
   *
   * @throws Exception
   */
  @Test
  public void testGetCompletedPDF_serviceUnavailable() throws Exception {

    // set the input parameters
    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setVaultLetterID(123456789);
    request.setWorkItemID(85152);

    final BDMCCTOutboundInterfaceImpl interfaceStub =
      new BDMCCTOutboundInterfaceHttpStatusStub(
        Integer.toString(HttpStatus.SC_SERVICE_UNAVAILABLE));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceStub.getCompletedPDF(request));
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
  }

}
