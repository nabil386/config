package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.stubs.BDMCCTOutboundInterfaceHttpStatusStub;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.stubs.BDMCCTOutboundInterfaceIOExceptionStub;
import curam.util.exception.AppException;
import java.io.IOException;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Suite of tests for the createCorrespondence() function in the
 * BDMCCTOutboundInterfaceImpl class.
 */
@Ignore
public class BDMCCTCreateCorrespondenceTests
  extends BDMCCTOutboundInterfaceImplTest {

  /**
   * Test createCorrespondence() for a scenario where the templateID is invalid.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCreateCorrespondence_invalidTemplateId() throws Exception {

    testTemplate.setTemplateID(999999);
    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));
    // BUG 93427
    // assertEquals(exception.getCatEntry(),
    // BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    // BUG 93427

  }

  /**
   * Test createCorrespondence() for a scenario where the fields provided for
   * the
   * request are valid.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCreateCorrespondence_success() throws Exception {

    final BDMCCTCreateCorrespondenceResponse response =
      interfaceObj.createCorrespondence(testTemplate);
    assertNotNull(response);
    assertNotNull(response.getWorkItemID());
    assertEquals(response.getSubmittedInd(), false);

  }

  /**
   * Test createCorrespondence() for a scenario where the community provided for
   * the request is empty
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCreateCorrespondence_communityMissing() throws Exception {

    testTemplate.setCommunity("");
    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));

    // BUG 92734
    // assertEquals(exception.getLocalizedMessage(),
    // BDMBPOCCT.ERR_MISSING_COMMUNITY_NAME.toString());
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    // BUG 92734

  }

  /**
   * Test createCorrespondence() for a scenario where the community provided for
   * the request is incorrect
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCreateCorrespondence_communityWrong() throws Exception {

    testTemplate.setCommunity("wrong");
    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));
    // BUG 93427
    // assertEquals(exception.getLocalizedMessage(),
    // BDMBPOCCT.ERR_CCT_HTTP_401_UNAUTHORIZED.toString());

    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    // BUG 93427

  }

  /**
   * Test createCorrespondence() for a scenario where the userId provided for
   * the
   * request is empty
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCreateCorrespondence_userIdMissing() throws Exception {

    testTemplate.setUserID("");
    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));
    // BUG 93427
    // assertEquals(exception.getLocalizedMessage(),
    // BDMBPOCCT.ERR_MISSING_USER_NAME.toString());

    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    // BUG 93427

  }

  /**
   * Test createCorrespondence() for a scenario where the dataXML provided for
   * the
   * request is empty
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCreateCorrespondence_dataXMLMissing() throws Exception {

    testTemplate.setDataXML("");
    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));

    // BUG 93427
    // assertEquals(exception.getLocalizedMessage(),
    // BDMBPOCCT.ERR_MISSING_DATA_XML.toString());
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    // BUG 93427
  }

  /**
   * Test createCorrespondence() for a scenario where the dataMapName provided
   * for
   * the request is empty
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCreateCorrespondence_dataMapNameMissing()
    throws IOException {

    testTemplate.setDataMapName("");
    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));
    // BUG 93427
    // assertEquals(exception.getLocalizedMessage(),
    // BDMBPOCCT.ERR_MISSING_DATA_MAP_NAME.toString());
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    // BUG 93427

  }

  /**
   * Test createCorrespondence() for a scenario where the templateId provided
   * for
   * the request is negative
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCreateCorrespondence_negativeTemplateId() throws Exception {

    testTemplate.setTemplateID(-1);
    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));
    // BUG 93427
    // assertEquals(exception.getLocalizedMessage(),
    // BDMBPOCCT.ERR_INCORRECT_TEMPLATE_ID_VALUE.toString());
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    // BUG 93427

  }

  /**
   * Test createCorrespondence() for a scenario where the templateFullPath
   * provided for the request is empty
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCreateCorrespondence_templateFullPathMissing()
    throws Exception {

    testTemplate.setTemplateFullPath("");
    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));
    // BUG 93427
    // assertEquals(exception.getLocalizedMessage(),
    // BDMBPOCCT.ERR_MISSING_TEMPLATE_FULL_PATH.toString());
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    // BUG 93427
  }

  /**
   * Test createCorrespondence() for a scenario where an IOException is raised.
   *
   * @throws Exception
   */
  @Override
  @Test
  public void testCreateCorrespondence_raiseIOException() throws Exception {

    final BDMCCTOutboundInterfaceIOExceptionStub interfaceObj =
      new BDMCCTOutboundInterfaceIOExceptionStub();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
  }

  /**
   * Test createCorrespondence() to check that it handles a HTTP 500 error and
   * returns
   * an AppException.
   *
   * @throws Exception
   */
  @Test
  public void testCreateCorrespondence_internalServerError()
    throws Exception {

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceHttpStatusStub(Integer
        .toString(BDMConstants.kHTTP_STATUS_INTERNAL_SERVER_ERROR_500));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
  }

  /**
   * Test createCorrespondence() to check that it handles a HTTP 503 error and
   * returns
   * an AppException.
   *
   * @throws Exception
   */
  @Test
  public void testCreateCorrespondence_serviceUnavailable() throws Exception {

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceHttpStatusStub(
        Integer.toString(HttpStatus.SC_SERVICE_UNAVAILABLE));

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.createCorrespondence(testTemplate));
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
  }

}
