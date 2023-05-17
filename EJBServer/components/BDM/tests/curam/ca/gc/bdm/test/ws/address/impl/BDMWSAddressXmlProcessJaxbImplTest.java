package curam.ca.gc.bdm.test.ws.address.impl;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.ws.address.gen.impl.Parm;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressConstants;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressRequest;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressRequest.Language;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService.RequestResponseType;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressXmlProcessJaxbImpl;
import curam.util.exception.AppException;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class BDMWSAddressXmlProcessJaxbImplTest
  extends CuramServerTestJUnit4 {

  @Test
  public void
    test_construct_WhenCalledWithMinimumParams_PayloadIsConstructed()
      throws AppException {

    // SETUP
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    assertNotNull("Constructed XML payload string should never be null",
      xmlString);
    assertFalse("Constructed XML payload string should never be empty",
      xmlString.isEmpty());

  }

  @Test
  public void test_construct_WhenCalledWithSearch_OneRequestPerPayload()
    throws AppException {

    // SETUP
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);

    assertEquals(
      "Current implementation has only one BDMWSAddress request per payload",
      1, xmlPayload.getInput().getAllRequests().getRequests().getRequest()
        .size());

  }

  @Test
  public void test_construct_WhenCalledWithSearch_SearchRequestInPayload()
    throws AppException {

    // SETUP
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);

    assertEquals("WSAddress Request name must be \"Search\"",
      RequestResponseType.SEARCH.toString(), xmlPayload.getInput()
        .getAllRequests().getRequests().getRequest().get(0).getName());

  }

  @Test
  public void test_construct_WhenCalledWithSearch_ValidateRequestInPayload()
    throws AppException {

    // SETUP
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);

    assertEquals("WSAddress Request name must be \"Validate\"",
      RequestResponseType.SEARCH.toString(), xmlPayload.getInput()
        .getAllRequests().getRequests().getRequest().get(0).getName());

  }

  @Test
  public void test_construct_WhenCalledWithValidate_OneRequestPerPayload()
    throws AppException {

    // SETUP
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    final RequestResponseType requestType = RequestResponseType.VALIDATE;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);

    assertEquals(
      "Current implementation has only one BDMWSAddress request per payload",
      1, xmlPayload.getInput().getAllRequests().getRequests().getRequest()
        .size());

  }

  @Test
  public void test_construct_WhenCalledWithValidate_ValidateRequestInPayload()
    throws AppException {

    // SETUP
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    final RequestResponseType requestType = RequestResponseType.VALIDATE;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);

    assertEquals("WSAddress Request name must be \"Validate\"",
      RequestResponseType.VALIDATE.toString(), xmlPayload.getInput()
        .getAllRequests().getRequests().getRequest().get(0).getName());

  }

  @Test
  public void
    test_construct_WhenCalledWithAddressLine_AddressLineRequestInPayload()
      throws AppException {

    // SETUP
    final String expectedAddressLine = "MyAddressLine";
    final Boolean expectedB64EncodingFlag =
      Boolean.valueOf(BDMWSAddressConstants.kFalse);
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    wsAddressRequest.setAddressLine(expectedAddressLine);
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);
    final Parm addressLineInputParam =
      getInputParameter(BDMWSAddressConstants.kAddressLine, xmlPayload);

    assertEquals(
      "WSAddress payload AddressLine input param must contain "
        + expectedAddressLine,
      expectedAddressLine, addressLineInputParam.getContent());
    assertEquals(
      "WSAddress payload AddressLine input param Base64Encoding should be  "
        + expectedB64EncodingFlag,
      expectedB64EncodingFlag,
      Boolean.valueOf(addressLineInputParam.getBase64Encoded()));
  }

  @Test
  public void test_construct_WhenCalledWithCity_CityRequestInPayload()
    throws AppException {

    // SETUP
    final String expectedCity = "MyCity";
    final Boolean expectedB64EncodingFlag =
      Boolean.valueOf(BDMWSAddressConstants.kFalse);
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    wsAddressRequest.setCity(expectedCity);
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);
    final Parm cityInputParam =
      getInputParameter(BDMWSAddressConstants.kCity, xmlPayload);

    assertEquals(
      "WSAddress payload City input param must contain " + expectedCity,
      expectedCity, cityInputParam.getContent());
    assertEquals(
      "WSAddress payload City input param Base64Encoding should be  "
        + expectedB64EncodingFlag,
      expectedB64EncodingFlag,
      Boolean.valueOf(cityInputParam.getBase64Encoded()));
  }

  @Test
  public void test_construct_WhenCalledWithProvince_ProvinceRequestInPayload()
    throws AppException {

    // SETUP
    final String expectedProvince = "MyProvince";
    final Boolean expectedB64EncodingFlag =
      Boolean.valueOf(BDMWSAddressConstants.kFalse);
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    wsAddressRequest.setProvince(expectedProvince);
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);
    final Parm provinceInputParam =
      getInputParameter(BDMWSAddressConstants.kProvince, xmlPayload);

    assertEquals(
      "WSAddress payload Province input param must contain "
        + expectedProvince,
      expectedProvince, provinceInputParam.getContent());
    assertEquals(
      "WSAddress payload Province input param Base64Encoding should be  "
        + expectedB64EncodingFlag,
      expectedB64EncodingFlag,
      Boolean.valueOf(provinceInputParam.getBase64Encoded()));
  }

  @Test
  public void test_construct_WhenCalledWithCountry_CountryRequestInPayload()
    throws AppException {

    // SETUP
    final String expectedCountry = "MyCountry";
    final Boolean expectedB64EncodingFlag =
      Boolean.valueOf(BDMWSAddressConstants.kFalse);
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    wsAddressRequest.setCountry(expectedCountry);
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);
    final Parm countryInputParam =
      getInputParameter(BDMWSAddressConstants.kCountry, xmlPayload);

    assertEquals(
      "WSAddress payload Country input param must contain " + expectedCountry,
      expectedCountry, countryInputParam.getContent());
    assertEquals(
      "WSAddress payload Country input param Base64Encoding should be  "
        + expectedB64EncodingFlag,
      expectedB64EncodingFlag,
      Boolean.valueOf(countryInputParam.getBase64Encoded()));
  }

  @Test
  public void
    test_construct_WhenCalledWithDefaultLanguage_EnglishLanguageRequestInPayload()
      throws AppException {

    // SETUP
    final String expectedLanguage =
      BDMWSAddressRequest.Language.ENGLISH.toString();
    final Boolean expectedB64EncodingFlag =
      Boolean.valueOf(BDMWSAddressConstants.kFalse);
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(new BDMWSAddressRequest(), requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);
    final Parm languageInputParam =
      getInputParameter(BDMWSAddressConstants.kLanguage, xmlPayload);

    assertEquals(
      "WSAddress payload Language input param must contain "
        + expectedLanguage,
      expectedLanguage, languageInputParam.getContent());
    assertEquals(
      "WSAddress payload Language input param Base64Encoding should be  "
        + expectedB64EncodingFlag,
      expectedB64EncodingFlag,
      Boolean.valueOf(languageInputParam.getBase64Encoded()));
  }

  @Test
  public void test_construct_WhenCalledWithLanguage_LanguageRequestInPayload()
    throws AppException {

    // SETUP
    final Language expectedLanguage = BDMWSAddressRequest.Language.FRENCH;
    final Boolean expectedB64EncodingFlag =
      Boolean.valueOf(BDMWSAddressConstants.kFalse);
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    wsAddressRequest.setLanguage(expectedLanguage);
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);
    final Parm languageInputParam =
      getInputParameter(BDMWSAddressConstants.kLanguage, xmlPayload);

    assertEquals(
      "WSAddress payload Language input param must contain "
        + expectedLanguage,
      expectedLanguage.toString(), languageInputParam.getContent());
    assertEquals(
      "WSAddress payload Language input param Base64Encoding should be  "
        + expectedB64EncodingFlag,
      expectedB64EncodingFlag,
      Boolean.valueOf(languageInputParam.getBase64Encoded()));
  }

  @Test
  public void
    test_construct_WhenCalledWithPostalCode_PostalCodeRequestInPayload()
      throws AppException {

    // SETUP
    final String expectedPostalCode = "MyPostalCode".toUpperCase();
    final Boolean expectedB64EncodingFlag =
      Boolean.valueOf(BDMWSAddressConstants.kFalse);
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    wsAddressRequest.setPostalCode(expectedPostalCode);
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);
    final Parm postalCodeInputParam =
      getInputParameter(BDMWSAddressConstants.kPostalCode, xmlPayload);

    assertEquals(
      "WSAddress payload PostalCode input param must contain "
        + expectedPostalCode,
      expectedPostalCode, postalCodeInputParam.getContent());
    assertEquals(
      "WSAddress payload PostalCode input param Base64Encoding should be  "
        + expectedB64EncodingFlag,
      expectedB64EncodingFlag,
      Boolean.valueOf(postalCodeInputParam.getBase64Encoded()));
  }

  @Test
  public void
    test_construct_WhenCalledWithB64Encoding_B64EncodingEnabledInPayload()
      throws AppException {

    // SETUP
    final String expectedAddressLine = "MyAddressLine";
    final Boolean expectedB64EncodingFlag =
      Boolean.valueOf(BDMWSAddressConstants.kTrue);
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    wsAddressRequest.setAddressLine(expectedAddressLine);
    wsAddressRequest.setEnableB64EncodingOnFields(expectedB64EncodingFlag);
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);

    for (final Parm inputParam : xmlPayload.getInput().getAllRequests()
      .getRequests().getRequest().get(0).getInputParameters().getParm()) {
      assertEquals("WSAddress payload " + inputParam.getName()
        + " input param Base64Encoding should be " + expectedB64EncodingFlag,
        expectedB64EncodingFlag,
        Boolean.valueOf(inputParam.getBase64Encoded()));
    }

  }

  private Parm getInputParameter(final String inputParamName,
    final WSMessage xmlPayload) {

    Parm inputParameter = null;
    try {
      inputParameter = xmlPayload.getInput().getAllRequests().getRequests()
        .getRequest().get(0).getInputParameters().getParm().stream()
        .filter(
          parameter -> parameter.getName().equalsIgnoreCase(inputParamName))
        .findFirst().get();

    } catch (final NoSuchElementException nsee) {
      fail("No Input Parameter with name of " + inputParamName);

    }
    return inputParameter;

  }

  /**
   * Test BDMWSAddressXmlProcessJaxbImpl.construct
   *
   * The payload to the BDMWSAddress system must contain specific elements
   * (which
   * can be null in most cases, i.e. <Element /> ).
   * This test ensures these mandatory elements appear in the payload.
   */
  @Test
  public void test_construct_WhenCalled_ServiceRequiredInformationInPayload()
    throws AppException {

    // SETUP
    final BDMWSAddressRequest wsAddressRequest = new BDMWSAddressRequest();
    final RequestResponseType requestType = RequestResponseType.SEARCH;

    // EXERCISE
    final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor =
      new BDMWSAddressXmlProcessJaxbImpl();
    final String xmlString =
      jaxbProcessor.construct(wsAddressRequest, requestType);

    // VERIFY
    final WSMessage xmlPayload =
      BDMWSAddressXmlProcessJaxbImpl.unmarshalXMLStringToWSMessage(xmlString);

    // <ApplicationIDs>
    assertNotNull("XML payload must contain the element <ApplicationIDs>",
      xmlPayload.getApplicationIDs());
    assertEquals(
      "XML payload must contain an expected current application ID for <ApplicationIDs><CurrentApplicationID>",
      BDMWSAddressConstants.kCuramSPMApplicationId,
      xmlPayload.getApplicationIDs().getCurrentApplicationID());
    assertEquals(
      "XML payload must contain an expected top application ID for <ApplicationIDs><TopApplicationID>",
      BDMWSAddressConstants.kCuramSPMApplicationId,
      xmlPayload.getApplicationIDs().getTopApplicationID());
    assertNotNull(
      "XML payload must contain the element <ApplicationIDs><ParentApplicationID>",
      xmlPayload.getApplicationIDs().getParentApplicationID());

    // <TransactionIDs>
    assertNotNull("XML payload must contain the element <TransactionIDs>",
      xmlPayload.getTransactionIDs());
    assertFalse(
      "XML payload must contain a generated current transaction ID for <TransactionIDs><CurrentApplicationID>",
      xmlPayload.getTransactionIDs().getCurrentTransactionID().isEmpty());
    assertFalse(
      "XML payload must contain a generated top transaction ID for <TransactionIDs><TopApplicationID>",
      xmlPayload.getTransactionIDs().getTopTransactionID().isEmpty());
    assertNotNull(
      "XML payload must contain the element <TransactionIDs><ParentTransactionID>",
      xmlPayload.getTransactionIDs().getParentTransactionID());

    // <SecurityContext>
    assertNotNull("XML payload must contain the element <SecurityContext>",
      xmlPayload.getSecurityContext());
    assertFalse("XML payload must contain a generated current transaction ID",
      xmlPayload.getSecurityContext().getTransactionID().isEmpty());

    // <SecurityContext><Authentication>
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication>",
      xmlPayload.getSecurityContext().getAuthentication());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><AssuranceLevel>",
      xmlPayload.getSecurityContext().getAuthentication()
        .getAssuranceLevel());

    // <SecurityContext><Authentication><Client>
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Client>",
      xmlPayload.getSecurityContext().getAuthentication().getClient());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Client><Role>",
      xmlPayload.getSecurityContext().getAuthentication().getClient()
        .getRole());

    // <SecurityContext><Authentication><Client><Identity>
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Client><Identity>",
      xmlPayload.getSecurityContext().getAuthentication().getClient()
        .getIdentity());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Client><Identity><ID>",
      xmlPayload.getSecurityContext().getAuthentication().getClient()
        .getIdentity().getID());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Client><Identity><IPAddress>",
      xmlPayload.getSecurityContext().getAuthentication().getClient()
        .getIdentity().getIPAddress());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Client><Identity><MachineName>",
      xmlPayload.getSecurityContext().getAuthentication().getClient()
        .getIdentity().getMachineName());

    // <SecurityContext><Authentication><Employee>
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Employee>",
      xmlPayload.getSecurityContext().getAuthentication().getEmployee());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Employee><Role>",
      xmlPayload.getSecurityContext().getAuthentication().getEmployee()
        .getRole());

    // <SecurityContext><Authentication><Employee><Identity>
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Employee><Identity>",
      xmlPayload.getSecurityContext().getAuthentication().getEmployee()
        .getIdentity());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Employee><Identity><ID>",
      xmlPayload.getSecurityContext().getAuthentication().getEmployee()
        .getIdentity().getID());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Employee><Identity><IPAddress>",
      xmlPayload.getSecurityContext().getAuthentication().getEmployee()
        .getIdentity().getIPAddress());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Employee><Identity><MachineName>",
      xmlPayload.getSecurityContext().getAuthentication().getEmployee()
        .getIdentity().getMachineName());

    // <SecurityContext><Authentication><Application>
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Application>",
      xmlPayload.getSecurityContext().getAuthentication().getApplication());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Application><Role>",
      xmlPayload.getSecurityContext().getAuthentication().getApplication()
        .getRole());

    // <SecurityContext><Authentication><Application><Identity>
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Application><Identity>",
      xmlPayload.getSecurityContext().getAuthentication().getApplication()
        .getIdentity());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Application><Identity><ID>",
      xmlPayload.getSecurityContext().getAuthentication().getApplication()
        .getIdentity().getID());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Application><Identity><IPAddress>",
      xmlPayload.getSecurityContext().getAuthentication().getApplication()
        .getIdentity().getIPAddress());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Application><Identity><MachineName>",
      xmlPayload.getSecurityContext().getAuthentication().getApplication()
        .getIdentity().getMachineName());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authentication><Application><Identity><SessionID>",
      xmlPayload.getSecurityContext().getAuthentication().getApplication()
        .getIdentity().getSessionID());
    assertEquals(
      "XML payload element <SecurityContext><Authentication><Application><Identity><Name> must contain "
        + BDMWSAddressConstants.kWSAddressApplicationName,
      BDMWSAddressConstants.kWSAddressApplicationName,
      xmlPayload.getSecurityContext().getAuthentication().getApplication()
        .getIdentity().getName());

    // <SecurityContext><Authorization>
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authorization>",
      xmlPayload.getSecurityContext().getAuthorization());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authorization><Request>",
      xmlPayload.getSecurityContext().getAuthorization().getRequest());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authorization><RequestDescription>",
      xmlPayload.getSecurityContext().getAuthorization()
        .getRequestDescription());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authorization><TransactionID>",
      xmlPayload.getSecurityContext().getAuthorization().getTransactionID());

    // <SecurityContext><Authorization><TimeStamp>
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authorization><TimeStamp>",
      xmlPayload.getSecurityContext().getAuthorization().getTimeStamp());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authorization><TimeStamp><BeginDateTime>",
      xmlPayload.getSecurityContext().getAuthorization().getTimeStamp()
        .getBeginDateTime());
    assertNotNull(
      "XML payload must contain the element <SecurityContext><Authorization><TimeStamp><EndDateTime>",
      xmlPayload.getSecurityContext().getAuthorization().getTimeStamp()
        .getEndDateTime());

  }

}
