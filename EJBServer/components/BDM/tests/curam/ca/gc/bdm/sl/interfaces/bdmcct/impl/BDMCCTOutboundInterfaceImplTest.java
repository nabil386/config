package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCancelPrintRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetFolderTreeRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetWorkItemStatusRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetWorkItemStatusResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTUpdateCorrespondenceRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.stubs.BDMCCTOutboundInterfaceIOExceptionStub;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Base class for the tests for the BDMCCTOutboundInterfaceImpl class.
 * Test classes will extend this class.
 */
@Ignore
public class BDMCCTOutboundInterfaceImplTest extends CuramServerTestJUnit4 {

  protected BDMCCTCreateCorrespondenceRequest testTemplate;

  protected final BDMCCTOutboundInterfaceImpl interfaceObj;

  private final String updateCorrespondenceSampleRequestPath =
    "./components/BDM/tests/curam/ca/gc/bdm/sl/interfaces/bdmcct/impl/UpdateCorrespondenceSampleDataXML.xml";

  public BDMCCTOutboundInterfaceImplTest() {

    super();
    interfaceObj = new BDMCCTOutboundInterfaceImpl();
  }

  /**
   * Setup method to declare elements used in tests
   *
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

    // This create correspondence object is used for the CancelPrint tests
    // where a work item must be created in order to be deleted (so as to not
    // delete
    // other work items in the environment)
    testTemplate = new BDMCCTCreateCorrespondenceRequest();
    testTemplate.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    testTemplate.setUserID("testinguser");
    testTemplate.setTemplateID(759880);
    testTemplate
      .setTemplateFullPath("/Foreign-benefits_Prestations-étrangères");
    testTemplate.setDataMapName("PensionsDataMap");
    testTemplate.setDataXML(
      "<Data xmlns:n0='urn:goc.gc.ca:i:correspondence' xmlns:prx='urn:sap.com:proxy:K1L:/1SAI/TAS059B3FEEA5BF6F4B99F9:750'><LetterHeader><CorrespondenceKey>6601FA3627031EDCAF850D433DB5803D</CorrespondenceKey><StatementDate>2022-04-14</StatementDate><LetterID>B90911</LetterID><LastPaymentAmount currencyCode='   '>0.0</LastPaymentAmount><RecoverOfficerName><FormattedName>Y. St-Jacques</FormattedName></RecoverOfficerName><languageKey>fr-CA</languageKey><NCCToll_E><SubscriberID>1(866) 864-5823</SubscriberID></NCCToll_E><NCCToll_F><SubscriberID>1(866) 864-5823</SubscriberID></NCCToll_F></LetterHeader><ClientInfo><PartnerID>6100012488</PartnerID><ClientID>108416827</ClientID><FirstName>dialer</FirstName><LastName>Test#3</LastName><Address_1>374 rue Nobert</Address_1><Address_2>Gatineau QC  J8R 3P3</Address_2><DateOfBirth>1966-09-12</DateOfBirth></ClientInfo><RecoveryOffice><OfficeID>4030</OfficeID><FaxNumber><SubscriberID>1(613) 969-3814</SubscriberID></FaxNumber><PhoneNumber><SubscriberID>1(866) 864-5823</SubscriberID></PhoneNumber><OfficeName_E>CRA- Collection Office - Ontario</OfficeName_E><OfficeName_F>ARC - Centre de Traitement - Ontario</OfficeName_F><PostalCode>K8N 2S3</PostalCode><Street>11 STATION STREET</Street><City>BELLEVILLE</City><Province>ON</Province><Country>CA</Country><TollFree_E><SubscriberID>1(866) 864-5823</SubscriberID></TollFree_E><TollFree_F><SubscriberID>1(866) 864-5823</SubscriberID></TollFree_F></RecoveryOffice><ContactOffice><OfficeID>7462</OfficeID><PhoneNumber><SubscriberID>1-888-815-4514</SubscriberID></PhoneNumber><OfficeName_E>HRDC / DRHC</OfficeName_E><OfficeName_F>HRDC / DRHC</OfficeName_F><PostalCode>K1P 6C6</PostalCode><Street>NATNL STUDENT LOAN SERVICE CTR</Street><Street2>POB 2090 STND/CP 2090 SUCC D</Street2><City>OTTAWA</City><Province>ON</Province><Country>CA</Country><TollFree_E><SubscriberID>1-888-815-4514</SubscriberID></TollFree_E><TollFree_F><SubscriberID>1-888-815-4514</SubscriberID></TollFree_F></ContactOffice><PaymentOffice><OfficeID>5006</OfficeID><OfficeName_E>Pacific</OfficeName_E><OfficeName_F>Pacifique</OfficeName_F><PostalCode>G4W 4S8</PostalCode><Street>PO Box 1133</Street><City>Matane</City><Province>QC</Province><Country>CA</Country></PaymentOffice><Bankruptcy><DateFiled>2022-03-01</DateFiled></Bankruptcy><ProgramDebt><CAC>EI</CAC><PorgramDescription_E>Employment Insurance</PorgramDescription_E><ProgramDescription>Assurance-emploi</ProgramDescription><TotalRecoverable currencyCode='   '>1200.0</TotalRecoverable></ProgramDebt><ProgramDebt><CAC>SL</CAC><PorgramDescription_E>Canada Student Loans Program</PorgramDescription_E><ProgramDescription>Programme canadien de prÃªts aux Ã©tudiants</ProgramDescription><TotalRecoverable currencyCode='   '>10500.0</TotalRecoverable></ProgramDebt></Data>");
    testTemplate.setSubmitOnCreate(false);
    testTemplate.setInitialAssigneeName("unauthenticated");
    testTemplate.setDeliveryOptionName("LocalPrint");
    testTemplate.setCorrespondenceMode("edit");
    testTemplate.setRedirectToAwaitingDelivery(true);
    testTemplate.setAutoCloseEditor(false);
    testTemplate.setEditorRedirectURL("");
    testTemplate.setOnlyArchive(false);
  }

  /**
   * Test getAndSaveFolderTree() for a scenario where the inputs are valid and
   * as
   * such the operation should succeed.
   *
   * @throws Exception
   */
  @Test
  public void testGetAndSaveFolderTree_success() throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq.setUserID("test-bdm");
    templateReq.setCommunity("Pensions");
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(true);

    final boolean successInd =
      interfaceObj.getAndSaveFolderTree(templateReq, new Date());

    assertTrue(successInd);
  }

  /**
   * Test getAndSaveFolderTree for a scenario where the incorrect community name
   * is provided, causing the test to fail.
   *
   * @throws Exception
   */
  @Test
  public void testGetAndSaveFolderTree_wrongCommunity() throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq.setUserID("test-bdm");
    templateReq.setCommunity("wrong");
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(true);

    final boolean successInd =
      interfaceObj.getAndSaveFolderTree(templateReq, new Date());

    assertFalse(successInd);
  }

  /**
   * Test getAndSaveFolderTree() for a scenario where a userId is not provided
   * to
   * the request.
   *
   * @throws Exception
   */
  @Test
  public void testGetAndSaveFolderTree_userIDMissing() throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq.setUserID("");
    templateReq.setCommunity("Pensions");
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(true);

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getAndSaveFolderTree(templateReq, new Date()));

    assertEquals(exception.getLocalizedMessage(),
      BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED.toString());
  }

  /**
   * Test getAndSaveFolderTree() for a scenario where the community field
   * provided
   * is empty.
   *
   * @throws Exception
   */
  @Test
  public void testGetAndSaveFolderTree_communityMissing() throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq.setUserID("test-bdm");
    templateReq.setCommunity("");
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(true);

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getAndSaveFolderTree(templateReq, new Date()));

    assertEquals(exception.getLocalizedMessage(),
      BDMBPOCCT.ERR_COMMUNITY_MUST_BE_ENTERED.toString());
  }

  /**
   * Test getAndSaveFolderTree() for scenario where the includetemplates field
   * for
   * the request is false.
   *
   * NOTE: This has to be handled as currently it throws a
   * NPE.
   *
   * @throws Exception
   */
  @Test(expected = Exception.class)
  public void testGetAndSaveFolderTree_includeTemplatesIsFalse()
    throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq.setUserID("test-bdm");
    templateReq.setCommunity("Pensions");
    templateReq.setIncludeTemplatesInd(false);
    templateReq.setIncludeTemplateFieldsInd(true);

    interfaceObj.getAndSaveFolderTree(templateReq, new Date());
  }

  /**
   * Test getAndSaveFolderTree() for scenario where includetemplatefields is
   * false.
   *
   * NOTE: This has to be handled as currently it throws an NPE.
   *
   * @throws Exception
   */
  @Test(expected = Exception.class)
  public void testGetAndSaveFolderTree_includeTemplateFieldsIsFalse()
    throws Exception {

    // set the input parameters
    final BDMCCTGetFolderTreeRequest templateReq =
      new BDMCCTGetFolderTreeRequest();

    templateReq.setUserID("test-bdm");
    templateReq.setCommunity("Pensions");
    templateReq.setIncludeTemplatesInd(true);
    templateReq.setIncludeTemplateFieldsInd(false);

    interfaceObj.getAndSaveFolderTree(templateReq, new Date());
  }

  /**
   * Test getAndSaveFolderTree() for a scenario where an IOException is raised.
   *
   * @throws Exception
   */
  @Test
  public void testGetAndSaveFolderTree_raiseIOException() throws Exception {

    final BDMCCTGetFolderTreeRequest request =
      new BDMCCTGetFolderTreeRequest();

    request.setUserID("test-bdm");
    request.setCommunity("Pensions");
    request.setIncludeTemplatesInd(true);
    request.setIncludeTemplateFieldsInd(false);

    final BDMCCTOutboundInterfaceIOExceptionStub interfaceObj =
      new BDMCCTOutboundInterfaceIOExceptionStub();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getAndSaveFolderTree(request, new Date()));
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_CCT_NOT_AVAILABLE);
  }

  /**
   * Test getWorkItemStatus() for scenario where valid parameters are passed in.
   *
   * @throws Exception
   */
  @Test
  public void testGetWorkItemStatus_success() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("test-bdm");
    request.setWorkItemID(31);
    request.setCommunity("Pensions");

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
   * Test getWorkItemStatus() for scenario where an empty userid is provided for
   * the request.
   *
   * @throws Exception
   */
  @Test
  public void testGetWorkItemStatus_emptyUserId() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("");
    request.setWorkItemID(44207);
    request.setCommunity("Pensions");

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
  @Test
  public void testGetWorkItemStatus_emptyWorkItemId() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("tester");
    request.setCommunity("Pensions");

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
  @Test
  public void testGetWorkItemStatus_negativeWorkItemId() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("tester");
    request.setWorkItemID(-1);
    request.setCommunity("Pensions");

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
  @Test
  public void testGetWorkItemStatus_invalidWorkItemId() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("tester");
    request.setWorkItemID(99999);
    request.setCommunity("Pensions");

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
  @Test
  public void testGetWorkItemStatus_emptyCorrespondenceMode()
    throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("test-bdm");
    request.setWorkItemID(31);
    request.setCommunity("Pensions");
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
  @Test
  public void testGetWorkItemStatus_invalidCorrespondenceMode()
    throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("test-bdm");
    request.setWorkItemID(31);
    request.setCommunity("Pensions");
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
  @Test
  public void testGetWorkItemStatus_validCorrespondenceMode()
    throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("test-bdm");
    request.setWorkItemID(31);
    request.setCommunity("Pensions");
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
  @Test
  public void testGetWorkItemStatus_raiseIOException() throws Exception {

    final BDMCCTGetWorkItemStatusRequest request =
      new BDMCCTGetWorkItemStatusRequest();

    request.setUserID("test-bdm");
    request.setCommunity("Pensions");
    request.setCorrespondenceMode("1");
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceIOExceptionStub interfaceObj =
      new BDMCCTOutboundInterfaceIOExceptionStub();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getWorkItemStatus(request));
    assertEquals(exception.getCatEntry(), BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
  }

  /**
   * Test cancelPrint() for a scenario where the fields provided for the request
   * are valid.
   *
   * @throws Exception
   */
  @Test
  public void testCancelPrint_success() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    final BDMCCTCreateCorrespondenceResponse templateResponse =
      interfaceObj.createCorrespondence(testTemplate);

    request.setUserID("testinguser");
    request.setCommunity("Pensions");
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
  @Test
  public void testCancelPrint_emptyCommunityName() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    final BDMCCTCreateCorrespondenceResponse templateResponse =
      interfaceObj.createCorrespondence(testTemplate);

    request.setUserID("test-bdm");
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
  @Test
  public void testCancelPrint_incorrectCommunityName() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    final BDMCCTCreateCorrespondenceResponse templateResponse =
      interfaceObj.createCorrespondence(testTemplate);

    request.setUserID("test-bdm");
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
  @Test
  public void testCancelPrint_incorrectWorkItemId() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request.setUserID("test-bdm");
    request.setCommunity("Pensions");
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
  @Test
  public void testCancelPrint_nonPositiveWorkItemId() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request.setUserID("test-bdm");
    request.setCommunity("Pensions");
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
  @Test
  public void testCancelPrint_emptyUserName() throws Exception {

    // set the input parameters
    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request.setUserID("");
    request.setCommunity("Pensions");
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
   * Test getCompletedPDF() for a scenario where an IOException is raised.
   *
   * @throws Exception
   */
  @Test
  public void testCancelPrint_raiseIOException() throws Exception {

    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request.setUserID("test-bdm");
    request.setCommunity("Pensions");
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceIOExceptionStub interfaceObj =
      new BDMCCTOutboundInterfaceIOExceptionStub();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.cancelPrint(request));
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
  }

  /**
   * Helper function for cancelPrint() to delete the work item after test so as
   * to
   * not crowd the environment with work items. This is for tests where the test
   * is designed to fail and so clean up is required afterwards.
   *
   * @param workItemId - ID of work item to cancel/void
   * @throws Exception
   */
  protected void cleanUpDocument(final long workItemId) throws Exception {

    final BDMCCTCancelPrintRequest request = new BDMCCTCancelPrintRequest();

    request.setUserID("ibm-user");
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    request.setWorkItemID(workItemId);

    interfaceObj.cancelPrint(request);
  }

  /**
   * Test createCorrespondence() for a scenario where the templateID is invalid.
   *
   * @throws Exception
   */
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
   * Method to load sample update correspondence request.
   *
   * @param filePath - path of the sample XML file to be used for Data XML field
   *
   */
  public BDMCCTUpdateCorrespondenceRequest
    loadUpdateCorrespondenceTestRequest(final String filePath,
      final boolean omitXMLDeclaration) {

    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest =
      new BDMCCTUpdateCorrespondenceRequest();

    updateCorrespondenceRequest.setCommunity("PensionsQA");
    updateCorrespondenceRequest.setUserId("test-bdm");
    updateCorrespondenceRequest.setWorkItemId(101867);
    updateCorrespondenceRequest.setDataMapName("AdhocLetterMap");

    String xmlString = null;

    // Document Builder and DOM Transformer is used to remove xml declaration
    final DocumentBuilderFactory documentBuilderfactory =
      DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    final TransformerFactory transformerFactory =
      TransformerFactory.newInstance();
    Transformer transformer = null;
    try {
      builder = documentBuilderfactory.newDocumentBuilder();
      final Document xmlDocument = builder.parse(new File(filePath));

      transformer = transformerFactory.newTransformer();

      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
        omitXMLDeclaration ? "yes" : "no");

      final StringWriter writer = new StringWriter();

      transformer.transform(new DOMSource(xmlDocument),
        new StreamResult(writer));

      xmlString = writer.getBuffer().toString();
    } catch (final TransformerException e) {
      e.printStackTrace();
    } catch (final Exception e) {
      e.printStackTrace();
    }

    updateCorrespondenceRequest.setDataXML(xmlString);
    return updateCorrespondenceRequest;

  }

  /**
   * Test updateCorrespondence() for a scenario where the fields provided for
   * the
   * request are valid.
   *
   * @throws Exception
   */
  @Test
  public void testUpdateCorrespondence_success() throws Exception {

    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest =
      loadUpdateCorrespondenceTestRequest(
        updateCorrespondenceSampleRequestPath, true);

    final boolean actualResult =
      interfaceObj.updateCorrespondence(updateCorrespondenceRequest);

    assertTrue(actualResult);

  }

  /**
   * Test updateCorrespondence() for a scenario where the community provided for
   * the request is empty
   *
   * @throws Exception
   */
  @Test
  public void testUpdateCorrespondence_communityMissing() throws Exception {

    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest =
      loadUpdateCorrespondenceTestRequest(
        updateCorrespondenceSampleRequestPath, true);
    updateCorrespondenceRequest.setCommunity("");

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.updateCorrespondence(updateCorrespondenceRequest));
    assertEquals(BDMBPOCCT.ERR_MISSING_COMMUNITY_NAME,
      exception.getCatEntry());

  }

  /**
   * Test updateCorrespondence() for a scenario where the community provided for
   * the request is incorrect
   *
   * @throws Exception
   */
  @Test
  public void testUpdateCorrespondence_communityWrong() throws Exception {

    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest =
      loadUpdateCorrespondenceTestRequest(
        updateCorrespondenceSampleRequestPath, true);
    updateCorrespondenceRequest.setCommunity("wrong");

    final boolean actualResult =
      interfaceObj.updateCorrespondence(updateCorrespondenceRequest);

    assertFalse(actualResult);

  }

  /**
   * Test updateCorrespondence() for a scenario where the userId provided for
   * the
   * request is empty
   *
   * @throws Exception
   */
  @Test
  public void testUpdateCorrespondence_userIdMissing() throws Exception {

    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest =
      loadUpdateCorrespondenceTestRequest(
        updateCorrespondenceSampleRequestPath, true);
    updateCorrespondenceRequest.setUserId("");

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.updateCorrespondence(updateCorrespondenceRequest));
    assertEquals(BDMBPOCCT.ERR_MISSING_USER_NAME, exception.getCatEntry());

  }

  /**
   * Test updateCorrespondence() for a scenario where the dataXML provided for
   * the
   * request is empty
   *
   * @throws Exception
   */
  @Test
  public void testUpdateCorrespondence_dataXMLMissing() throws Exception {

    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest =
      loadUpdateCorrespondenceTestRequest(
        updateCorrespondenceSampleRequestPath, true);
    updateCorrespondenceRequest.setDataXML("");

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.updateCorrespondence(updateCorrespondenceRequest));
    assertEquals(BDMBPOCCT.ERR_MISSING_DATA_XML, exception.getCatEntry());
  }

  /**
   * Test updateCorrespondence() for a scenario where the dataMapName provided
   * for
   * the request is empty
   *
   * @throws Exception
   */
  @Test
  public void testUpdateCorrespondence_dataMapNameMissing() throws Exception {

    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest =
      loadUpdateCorrespondenceTestRequest(
        updateCorrespondenceSampleRequestPath, true);
    updateCorrespondenceRequest.setDataMapName("");

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.updateCorrespondence(updateCorrespondenceRequest));
    assertEquals(BDMBPOCCT.ERR_MISSING_DATA_MAP_NAME,
      exception.getCatEntry());

  }

  /**
   * Test updateCorrespondence() for a scenario where the templateId provided
   * for
   * the request is negative
   *
   * @throws Exception
   */
  @Test
  public void testUpdateCorrespondence_negativeWortkItemId()
    throws Exception {

    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest =
      loadUpdateCorrespondenceTestRequest(
        updateCorrespondenceSampleRequestPath, true);
    updateCorrespondenceRequest.setWorkItemId(-1);

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.updateCorrespondence(updateCorrespondenceRequest));
    assertEquals(BDMBPOCCT.ERR_MISSING_WORK_ITEM_ID, exception.getCatEntry());

  }

  /**
   * Test updateCorrespondence() for a scenario where an IOException is raised
   *
   * @throws Exception
   */
  @Test
  public void testUpdateCorrespondence_raiseIOException() throws Exception {

    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest =
      loadUpdateCorrespondenceTestRequest(
        updateCorrespondenceSampleRequestPath, true);

    final BDMCCTOutboundInterfaceIOExceptionStub interfaceObj =
      new BDMCCTOutboundInterfaceIOExceptionStub();

    assertThrows(IOException.class,
      () -> interfaceObj.updateCorrespondence(updateCorrespondenceRequest));
  }

  /**
   * Test getCompletedPDF() for a scenario where the fields provided for the
   * request are valid.
   *
   * @throws Exception
   */
  @Test
  public void testGetCompletedPDF_success() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request.setUserID("test-bdm");
    request.setCommunity("Pensions");
    request.setVaultLetterID(123456789);
    request.setWorkItemID(85152);

    final BDMCCTOutboundInterfaceImpl interfaceObj =
      new BDMCCTOutboundInterfaceImpl();

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
  @Test
  public void testGetCompletedPDF_missingCommunity() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request.setUserID("test-bdm");
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
  @Test
  public void testGetCompletedPDF_missingUserId() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request.setUserID("");
    request.setCommunity("Pensions");
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
  @Test
  public void testGetCompletedPDF_missingWorkItemId() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request.setUserID("test-bdm");
    request.setCommunity("Pensions");
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
  @Test
  public void testGetCompletedPDF_missingVaultLetterId() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request.setUserID("test-bdm");
    request.setCommunity("Pensions");
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
  @Test
  public void testGetCompletedPDF_nonExistentPDF() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request.setUserID("test-bdm");
    request.setCommunity("Pensions");
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
  @Test
  public void testGetCompletedPDF_multipleDocumentsReturned()
    throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request.setUserID("test-bdm");
    request.setCommunity("ccs-scm");
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
  @Test
  public void testGetCompletedPDF_raiseIOException() throws Exception {

    final BDMCCTGetCompletedPDFRequest request =
      new BDMCCTGetCompletedPDFRequest();

    request.setUserID("test-bdm");
    request.setCommunity("Pensions");
    request.setVaultLetterID(5);
    request.setWorkItemID(2);

    final BDMCCTOutboundInterfaceIOExceptionStub interfaceObj =
      new BDMCCTOutboundInterfaceIOExceptionStub();

    final AppException exception = assertThrows(AppException.class,
      () -> interfaceObj.getCompletedPDF(request));
    assertEquals(exception.getCatEntry(),
      BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

}
