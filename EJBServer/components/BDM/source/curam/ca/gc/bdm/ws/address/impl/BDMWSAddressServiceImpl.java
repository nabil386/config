package curam.ca.gc.bdm.ws.address.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.ws.impl.WSCommonUtils;
import curam.codetable.impl.SYSTEMSERVICENAMEEntry;
import curam.codetable.impl.TARGETSYSTEMSTATUSEntry;
import curam.core.impl.EnvVars;
import curam.ctm.sl.entity.fact.TargetSystemServiceFactory;
import curam.ctm.sl.entity.intf.TargetSystemService;
import curam.ctm.sl.entity.struct.NameSystemIDAndStatusKey;
import curam.ctm.sl.entity.struct.TargetSystemServiceDtls;
import curam.ctm.targetsystem.impl.TargetSystem;
import curam.ctm.targetsystem.impl.TargetSystemDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.type.NotFoundIndicator;
import curam.util.webservices.StubFactory;
import curam.util.webservices.StubFactory.PooledStub;
import goc.servicecanada.softwarefactory.webservices.WSAddressStub;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.lang.StringUtils;
import static curam.ca.gc.bdm.ws.impl.WSCommonUtils.throwAppRuntimeException;

/**
 * A wrapper/facade to call the external ESDC Address Service via SOAP
 * using Cúram Axis code generation
 *
 * The public APIs define the type of requests a consumer can make on the
 * BDMWSAddress service (e.g. search, validate. format, etc)
 */
public class BDMWSAddressServiceImpl implements BDMWSAddressService {

  @Inject
  BDMWSAddressXmlProcessStrategy xmlPayloadHandler;

  @Override
  public BDMWSAddressResponse
    search(final BDMWSAddressRequest wsAddressRequest) throws AppException {

    // TODO possibly implement some validation:
    // According to ESDC documentation for BDMWSAddress
    // At a minimum an AdderssLine with a combination of other parameters is
    // required or a PostalCode to receive any matches.

    return execute(wsAddressRequest, RequestResponseType.SEARCH);

  }

  @Override
  public BDMWSAddressResponse
    validate(final BDMWSAddressRequest wsAddressRequest) throws AppException {

    return execute(wsAddressRequest, RequestResponseType.VALIDATE);

  }

  public BDMWSAddressServiceImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Call the address service
   *
   * @param requestDetails {@link BDMWSAddressRequest}
   * @return {@link BDMWSAddressResponseList}
   * @throws AppException
   */
  BDMWSAddressResponse execute(final BDMWSAddressRequest wsAddressRequest,
    final RequestResponseType type) throws AppException {

    // Construct a defined XML string for the payload from the request object
    // given and the request response type
    final String constructedXmlPaylod =
      xmlPayloadHandler.construct(wsAddressRequest, type);

    // Retreive the response XML string from the webservice.
    final String wsAddressResponseXmlString =
      readyBDMWSAddressCallWithStubPool(constructedXmlPaylod);

    // the XML response string shouldn't be null or empty because if at any
    // stage in it's retrieval we encountered an error, we bubble that error
    // upwards including this method. However, for the interest of NPE's, we
    // check anyway and return a blank object if empty
    BDMWSAddressResponse wsAddressResponse = new BDMWSAddressResponse();
    if (!StringUtils.isEmpty(wsAddressResponseXmlString)) {
      // Current WSDL has the response contain a string XML payload. This
      // needs to be parsed from the response. Like before, we use the binded
      // xml payload handler defined to parse the XML payload. OOTB this is JAXB
      wsAddressResponse =
        xmlPayloadHandler.parse(wsAddressResponseXmlString, type);
    }

    return wsAddressResponse;
  }

  private String readyBDMWSAddressCallWithStubPool(
    final String constructedXmlPaylod) throws AppException {

    // we initialize an empty string for the return, but this should be end up
    // having a proper value or will be thrown out as an exception
    String wsAddressResponseXmlString = "";

    WSAddressStub stub = null;
    PooledStub<WSAddressStub> pooledStub = null;
    try {
      // Create the service from a pool or newly
      pooledStub = StubFactory.getPooledStub(WSAddressStub.class);
      stub = pooledStub.getStub();

      // FIXME we should remove these two inner try-catch blocks and wrap in a
      // single
      // try-catch-finally block with the finally returning the stub to the
      // pool. This will be wrapped in the outer try-catch which handles the
      // stub pool errors which if can't be resolved, we are limited to only
      // returning back to the caller or logging
      try {
        // set the service client options (e.g.
        // endpoint/timeout/authentication)
        setServiceClientOptions(stub);
      } catch (final Exception e) {
        // Something has gone wrong in the webservice configuration, we must
        // still attempt to return the stub to the pool before continuing to
        // throw the error upwards to the next catch block to prevent hanging
        pooledStub.returnToPool();
        throwAppRuntimeException(e);
      }

      try {
        // call to the external service
        wsAddressResponseXmlString =
          callWSAddress(constructedXmlPaylod, stub);
      } catch (final Exception e) {
        // Something has gone wrong in the call to the webservice, we must
        // still attempt to return the stub to the pool before continuing to
        // throw the error upwards to the next catch block to prevent hanging
        pooledStub.returnToPool();
        throwAppRuntimeException(e);
      }

      // Everything processed correctly, return the stub to the pool.
      pooledStub.returnToPool();

    } catch (final Exception e) {
      // Something has gone wrong in getting the stub from the pool, returning
      // the stub to the pool, the configuration of the webservice, or the
      // webservice call itself. We've done our best to return the stub to the
      // pool when errors have occured to release the thread but there isn't
      // much more we can do but return the error upwards to the next error
      // handler
      throwAppRuntimeException(e);

    }

    return wsAddressResponseXmlString;

  }

  public String callWSAddress(final String constructedXmlPaylod,
    final WSAddressStub stub) throws AppException {

    // we initialize an empty string for the return, but this should be end up
    // having a proper value or will be thrown out as an exception
    String wsAddressResponseXmlString = "";

    try {
      // Create the request payload
      final WSAddressStub.Execute wsAddressRequestPayload =
        new WSAddressStub.Execute();
      // Current WSDL has the request contain a string XML payload. This needs
      // to be constructed from the initial request. We use the binded xml
      // payload handler defined for the BDMWSAddressService which concerns with
      // how the String XML payload is constructed and information retrieved.
      // OOTB the Strategy will use JAXB
      wsAddressRequestPayload.setStrXMLInput(constructedXmlPaylod);

      // Call out to the external service
      WSAddressStub.ExecuteResponse wsAddressResponsePayload;
      wsAddressResponsePayload = stub.execute(wsAddressRequestPayload);

      // Retrieve the result
      wsAddressResponseXmlString =
        wsAddressResponsePayload.getExecuteResult();

    } catch (final Exception e) {
      // if any number of faults or exceptions have occurred in the service
      // call, then we should just stop processing as something nasty has gone
      // wrong
      throwAppRuntimeException(e);

    }

    return wsAddressResponseXmlString;
  }

  /**
   * Configures the options available on the service client access to the
   * validation
   * service
   *
   * @param stub ValidationServiceStub
   */
  WSAddressStub setServiceClientOptions(final WSAddressStub stub)
    throws AppException, InformationalException {

    final ServiceClient client = stub._getServiceClient();

    Integer configuredTimeout = Configuration
      .getIntProperty(EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_TIMEOUT);
    if (configuredTimeout == null) {
      configuredTimeout =
        EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_TIMEOUT_DEFAULT;
    }
    // set configurable timeout on the service client options
    client.getOptions().setProperty(HTTPConstants.SO_TIMEOUT,
      configuredTimeout);
    client.getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT,
      configuredTimeout);

    // set the client endpoint
    setServiceEndpoint(stub);

    // set the client authentication protocol
    setServiceAuthentication(stub);

    return stub;
  }

  /**
   * Configures the external WSAddress authentication on the service client
   * if it has been implemented
   *
   * @param stub ValidationServiceStub
   * @return ValidationServiceStub
   */
  public WSAddressStub setServiceAuthentication(final WSAddressStub stub)
    throws AppException, InformationalException {

    // This authentication pattern was written for Program Integrity by Billy
    // Dennigen and an example can be found in
    // curam.programintegrity.impl.IIWebServiceAuthenticationImpl
    // An internal wiki called IBM InfoSphere Identity Insight Update under the
    // SPM team community on Connections which describes it greater

    final TargetSystemServiceDtls targetSystemServiceDtls =
      new BDMWSAddressServiceAuthentication()
        .getExtWsAddressServiceTargetSystemServiceDtls();
    final HttpTransportProperties.Authenticator basicAuthentication =
      new HttpTransportProperties.Authenticator();
    basicAuthentication.setUsername(targetSystemServiceDtls.username);
    basicAuthentication.setPassword(
      WSCommonUtils.getPlainTextPassword(targetSystemServiceDtls.password));
    basicAuthentication.setPreemptiveAuthentication(true);
    stub._getServiceClient().getOptions().setProperty(
      org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE,
      basicAuthentication);

    return stub;
  }

  /**
   * Set the address service endpoint reference in the service
   * client
   *
   * @param stub {@link WSAddressStub}
   * @return {@link WSAddressStub}
   *
   * @throws AppException
   * @throws InformationalException
   */
  public WSAddressStub setServiceEndpoint(final WSAddressStub stub)
    throws AppException, InformationalException {

    final String endpointURL = getBDMWSAddressServiceEndpointURL();
    final EndpointReference endPointReference =
      new EndpointReference(endpointURL);
    stub._getServiceClient().getOptions().setTo(endPointReference);

    return stub;
  }

  /**
   * Retrieves the URL for the external BDMWSAddress service.
   *
   * @return String url
   *
   * @throws AppException
   * @throws InformationalException
   */
  private String getBDMWSAddressServiceEndpointURL()
    throws AppException, InformationalException {

    // The URL for the endpoint is contained on two tables;
    // The TargetSystem and TargetSystemService.
    // TargetSystem will contain the root URL while the TargetSystemService
    // will
    // contain the extension URL (i.e. the path on the hosting machine)
    // The lookup on the TargetSystem table is found using a string tag
    // defined
    // as an application config property

    final TargetSystemDAO targetSystemDAO =
      GuiceWrapper.getInjector().getInstance(TargetSystemDAO.class);

    final String targetSystemName = Configuration
      .getProperty(EnvVars.BDM_ENV_BDM_WSADDRESS_TARGET_SYSTEM_NAME);
    final TargetSystem targetSystem =
      targetSystemDAO.readByTargetSystemNameAndStatus(
        TARGETSYSTEMSTATUSEntry.ACTIVE, targetSystemName);

    final NameSystemIDAndStatusKey nameAndStatusKey =
      new NameSystemIDAndStatusKey();
    nameAndStatusKey.name = SYSTEMSERVICENAMEEntry.WSADDRESSSERVICE.getCode();
    nameAndStatusKey.systemID = targetSystem.getID();
    nameAndStatusKey.status = TARGETSYSTEMSTATUSEntry.ACTIVE.getCode();

    final TargetSystemService targetSystemService =
      TargetSystemServiceFactory.newInstance();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final TargetSystemServiceDtls targetSystemServiceDtls =
      targetSystemService.readByNameSystemIDAndStatus(nfIndicator,
        nameAndStatusKey);

    final String endpointUrl =
      targetSystem.getRootURL() + targetSystemServiceDtls.extensionURL;

    if (Trace.atLeast(Trace.kTraceOn)) {
      Trace.kTopLevelLogger
        .debug("WEBSERVICE: BDMWSAddress endpoint URL: " + endpointUrl);
    }
    return endpointUrl;

  }

}
