
package curam.ca.gc.bdm.ws.address.impl;

import curam.ca.gc.bdm.ws.address.gen.impl.ApplicationEntityType;
import curam.ca.gc.bdm.ws.address.gen.impl.GenericEntityType;
import curam.ca.gc.bdm.ws.address.gen.impl.GenericEntityType.Identity;
import curam.ca.gc.bdm.ws.address.gen.impl.InputParameters;
import curam.ca.gc.bdm.ws.address.gen.impl.Parm;
import curam.ca.gc.bdm.ws.address.gen.impl.SecurityContextType;
import curam.ca.gc.bdm.ws.address.gen.impl.SecurityContextType.Authentication;
import curam.ca.gc.bdm.ws.address.gen.impl.SecurityContextType.Authorization;
import curam.ca.gc.bdm.ws.address.gen.impl.SecurityContextType.Authorization.TimeStamp;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.ApplicationIDs;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.Input;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.Input.AllRequests;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.Input.AllRequests.Requests;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.Input.AllRequests.Requests.Request;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.Input.Settings;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.TransactionIDs;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService.RequestResponseType;
import curam.util.exception.AppException;
import curam.util.resources.Trace;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import static curam.ca.gc.bdm.ws.impl.WSCommonUtils.throwAppRuntimeException;

/**
 * This is responsible for constructing and parsing the Xml strings sent
 * to and received from the ESDC BDMWSAddress web service.
 *
 * The BDMWSAddress service sends a Xml payload as a string in it's request and
 * returns an Xml payload as a string in it's response. To consume the
 * BDMWSAddress
 * service, we need to be able to construct these Xml payloads from
 * {@link BDMWSAddressRequest} objects and return the results as
 * {@link BDMWSAddressResponse} or {@link BDMWSAddressResponseList} objects.
 *
 * This implementation class uses JAXB to marshal and unmarshal the Xml strings.
 * The JAXB java classes were generated from a handcrafted XSD and
 * source-controlled. These classes are not regenerated at build time and need
 * to be explicitly generated when change is required. The XSD was created from
 * sample request/responses and is subject to volatile change.
 *
 * The XSD definition file is called WSMessage.xsd and can be found in
 * EJBServer/components/custom/axis/address/XSDs
 */
public class BDMWSAddressXmlProcessJaxbImpl
  implements BDMWSAddressXmlProcessStrategy {

  BDMWSAddressXmlPayloadProcessJaxbStrategy requestTypeHandler;

  @Override
  public String construct(final BDMWSAddressRequest wsAddressRequest,
    final RequestResponseType requestType) throws AppException {

    WSMessage wsMessage = new WSMessage();
    wsMessage = populateServiceInformationElements(wsMessage);
    wsMessage = populateInputParameterElements(wsMessage, wsAddressRequest,
      requestType);

    // The BDMWSAddress service requires the payload to be sent as a String.
    // Previously we wrapped the payload string in [CDATA] tags as looked to be
    // expected in sample payloads. However on testing against the real service,
    // we received an error noting "The request received is not a valid XML
    // message". Therefore, we just marshal the payload as a String and send
    // with no
    // edits to which the server handles the rest
    final String marshalledRequestPayload =
      marshalWSMessageToXMLString(wsMessage);
    final String wsMessageXmlString = marshalledRequestPayload;

    if (Trace.atLeast(Trace.kTraceVerbose)) {
      // If tracing is enabled, we include the payload XML
      Trace.kTopLevelLogger
        .debug("WEBSERVICE: BDMWSAddress constructed XML payload");
      Trace.kTopLevelLogger.debug(wsMessageXmlString);
    }

    return wsMessageXmlString;
  }

  @Override
  public BDMWSAddressResponse parse(final String xml,
    final RequestResponseType requestType) throws AppException {

    if (Trace.atLeast(Trace.kTraceVerbose)) {
      // If tracing is enabled, we include the payload XML
      Trace.kTopLevelLogger
        .debug("WEBSERVICE: BDMWSAddress response XML payload");
      Trace.kTopLevelLogger.debug(xml);
    }

    final BDMWSAddressResponse response = new BDMWSAddressResponse();

    // clean the Xml string from any CDATA tags
    // TODO unsure if required
    final String baseXml = xml.replace("<![CDATA[", "").replace("]]>", "");

    final WSMessage parsedResponse = unmarshalXMLStringToWSMessage(baseXml);

    // Different request types will concern themselves with different XML (e.g.
    // Search request cares about AddressMatches element on the response which
    // won't appear in a Validate request). As such, we use a JAXB Xml payload
    // handler Strategy that implements a parser for each specific request based
    // on JAXB implementation

    if (requestType.equals(RequestResponseType.SEARCH)) {
      requestTypeHandler = new BDMWSAddressSearchPayloadJaxbProcessImpl();
      final BDMWSAddressTypedResponse wsAddressResults =
        requestTypeHandler.parse(parsedResponse);
      response.getIndividualRequestResponses().put(RequestResponseType.SEARCH,
        wsAddressResults);
      response.setSuccessfulResponse(true);
    } else if (requestType.equals(RequestResponseType.VALIDATE)) {
      requestTypeHandler = new BDMWSAddressValidatePayloadJaxbProcessImpl();
      final BDMWSAddressTypedResponse wsValidateResults =
        requestTypeHandler.parse(parsedResponse);
      response.getIndividualRequestResponses()
        .put(RequestResponseType.VALIDATE, wsValidateResults);
      response.setSuccessfulResponse(true);

    }

    return response;
  }

  /**
   * Create the Parm element
   *
   * @param parmName String
   * @param parmValue String
   * @return Parm
   */
  private static Parm createParm(final String parmName,
    final String parmValue, final boolean isBase64encoded) {

    final Parm parm = new Parm();
    parm.setBase64Encoded(isBase64encoded ? BDMWSAddressConstants.kTrue
      : BDMWSAddressConstants.kFalse);
    parm.setName(parmName);
    if (!parmValue.isEmpty()) {
      parm.setContent(parmValue);
    }
    return parm;

  }

  /**
   * Marshal the constructed {@linkplain WSMessage} object to an XML string.
   *
   * If any exception is thrown, we wrap it in an AppException and rethrow to
   * the caller.
   *
   * @param wsMessage {@link WSMessage}
   * @return String
   * @throws AppException
   */
  static String marshalWSMessageToXMLString(final WSMessage wsMessage)
    throws AppException {

    String xmlString = "";
    try {
      final JAXBContext context =
        JAXBContext.newInstance(wsMessage.getClass());
      final Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      final StringWriter sw = new StringWriter();
      m.marshal(wsMessage, sw);
      xmlString = sw.toString();
    } catch (final JAXBException e) {
      throwAppRuntimeException(e);
    }
    return xmlString;
  }

  /**
   * Unmarshal the XML payload string to a {@link WSMessage} Java object.
   *
   * If any exception is thrown, we wrap it in an AppException and rethrow to
   * the caller.
   *
   * @param xml String
   * @return {@link WSMessage}
   * @throws AppException
   */
  public static WSMessage unmarshalXMLStringToWSMessage(final String xml)
    throws AppException {

    WSMessage wsMessage = new WSMessage();
    try {
      final JAXBContext jaxbContext =
        JAXBContext.newInstance(WSMessage.class);
      Unmarshaller jaxbUnmarshaller;
      jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      wsMessage =
        (WSMessage) jaxbUnmarshaller.unmarshal(new StringReader(xml));
    } catch (final JAXBException e) {
      throwAppRuntimeException(e);
    }
    return wsMessage;
  }

  /**
   * Populates the XML payload elements for information related to the service
   * interaction, not the input parameters themselves.
   *
   * Discovered from trial and error, all elements must be present but are
   * allowed to be empty. e.g. <Role/>
   *
   * Quoting an answer from ESDC:
   * <blockquote>
   * The actual values of these element more or less belong to you (in a way).
   * They are used to track down the transactions applications make to our
   * services in our audit database (every call is audited).
   *
   * ApplicationId should be something to allow for your application to be
   * identified amongst others (usually the application name is enough).
   *
   * TransactionId is the most important one, the only "rule" is that it should
   * be unique (to be able to track it down) but the meaning is up to you.
   * Applications usually put a GUID in there.
   *
   * TopTransactionId can be [equal] to TransactionId, you can use it if you
   * want to
   * group transactions together (for example, if 2 calls to BDMWSAddress are
   * part
   * of a bigger transaction on your side), otherwise same as TransactionId is
   * fine."
   * </blockquote>
   *
   * @param wsMessage {@link WSMessage} generated Axis stub
   * @return {@link WSMessage} generated Axis stub
   */
  private WSMessage
    populateServiceInformationElements(final WSMessage wsMessage) {

    // Note: Each transaction requires a unique ID string to be passed. For now,
    // we're generating a UUID rather than pull any internal Curam ident numbers
    // TODO: as they'd be used for tracing/auditing, we should be storing every
    // UUID we generate to help that
    final String transId = UUID.randomUUID().toString();

    // <ApplicationIDs>
    final ApplicationIDs appIds = new ApplicationIDs();
    appIds.setTopApplicationID(BDMWSAddressConstants.kCuramSPMApplicationId);
    appIds.setParentApplicationID("");
    appIds
      .setCurrentApplicationID(BDMWSAddressConstants.kCuramSPMApplicationId);
    wsMessage.setApplicationIDs(appIds);

    // <TransactionIDs>
    final TransactionIDs transIds = new TransactionIDs();
    transIds.setTopTransactionID(transId);
    transIds.setParentTransactionID("");
    transIds.setCurrentTransactionID(transId);
    wsMessage.setTransactionIDs(transIds);

    // <Authentication><Client>
    final GenericEntityType client = new GenericEntityType();
    final Identity clientIdent = new Identity();
    clientIdent.setID("");
    clientIdent.setIPAddress("");
    clientIdent.setMachineName("");
    clientIdent.setName("");
    client.setIdentity(clientIdent);
    client.setRole("");

    // <Authentication><Employee>
    final GenericEntityType employee = new GenericEntityType();
    final Identity empIdent = new Identity();
    empIdent.setID("");
    empIdent.setName("");
    empIdent.setIPAddress("");
    empIdent.setMachineName("");
    employee.setIdentity(empIdent);
    employee.setRole("");

    // <Authentication><Application>
    final ApplicationEntityType application = new ApplicationEntityType();
    final curam.ca.gc.bdm.ws.address.gen.impl.ApplicationEntityType.Identity appIdent =
      new curam.ca.gc.bdm.ws.address.gen.impl.ApplicationEntityType.Identity();
    appIdent.setID("");
    appIdent.setName(BDMWSAddressConstants.kWSAddressApplicationName);
    appIdent.setVersion("");
    appIdent.setIPAddress("");
    appIdent.setMachineName("");
    appIdent.setSessionID("");
    application.setIdentity(appIdent);
    application.setRole("");

    // <Authentication>
    final Authentication authent = new Authentication();
    authent.setClient(client);
    authent.setEmployee(employee);
    authent.setApplication(application);
    authent.setAssuranceLevel("");

    // <Authorize>
    final Authorization authorize = new Authorization();
    authorize.setRequestDescription("");
    authorize.setRequest("");
    authorize.setTransactionID("");
    // <Authorize><TimeStamp>
    final TimeStamp timestamp = new TimeStamp();
    timestamp.setBeginDateTime("");
    timestamp.setEndDateTime("");
    authorize.setTimeStamp(timestamp);

    // <SecurityContext>
    final SecurityContextType securityContext = new SecurityContextType();
    securityContext.setAuthentication(authent);
    securityContext.setAuthorization(authorize);
    securityContext.setProxySettings(null);
    securityContext.setTransactionID(transId);
    wsMessage.setSecurityContext(securityContext);

    return wsMessage;

  }

  /**
   * Populates the XML payload elements for the required Input Parameters and
   * the Request type
   *
   * @param wsMessage {@link WSMessage} generated Axis stub
   * @return {@link WSMessage} generated Axis stub
   */
  private WSMessage populateInputParameterElements(final WSMessage wsMessage,
    final BDMWSAddressRequest wsAddressRequest,
    final RequestResponseType requestType) {

    // Note: we are presuming that the input parameters do not change between
    // BDMWSAddress subservices like Search, Validate, Format, etc. If they do
    // we
    // need to implement a Strategy pattern to process based on request type,
    // like the parse()

    // <Input><AllRequests><Requests><Request><InputParameters>
    final InputParameters inputParameters = new InputParameters();
    final boolean isB64EncodingEnabled =
      wsAddressRequest.isB64EncodingEnabledOnFields();
    inputParameters.getParm()
      .add(createParm(BDMWSAddressConstants.kAddressLine,
        wsAddressRequest.getAddressLine(), isB64EncodingEnabled));
    inputParameters.getParm().add(createParm(BDMWSAddressConstants.kCity,
      wsAddressRequest.getCity(), isB64EncodingEnabled));
    inputParameters.getParm().add(createParm(BDMWSAddressConstants.kProvince,
      wsAddressRequest.getProvince(), isB64EncodingEnabled));
    inputParameters.getParm().add(createParm(BDMWSAddressConstants.kCountry,
      wsAddressRequest.getCountry(), isB64EncodingEnabled));
    inputParameters.getParm()
      .add(createParm(BDMWSAddressConstants.kPostalCode,
        wsAddressRequest.getPostalCode(), isB64EncodingEnabled));
    inputParameters.getParm().add(createParm(BDMWSAddressConstants.kLanguage,
      wsAddressRequest.getLanguage().toString(), isB64EncodingEnabled));

    // <Input><AllRequests><Requests><Request>
    final Request request = new Request();
    request.setName(requestType.toString());
    request.setInputParameters(inputParameters);

    // <Input><AllRequests><Requests>
    final Requests requests = new Requests();
    requests.getRequest().add(request);

    // <Input><AllRequests>
    final AllRequests allRequests = new AllRequests();
    allRequests.setRequests(requests);

    // <Input>
    final Input input = new Input();
    input.setAllRequests(allRequests);
    // <Input><Settings>
    input.setSettings(new Settings());

    wsMessage.setInput(input);
    return wsMessage;

  }

}
