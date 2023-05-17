package curam.ca.gc.bdm.test.ws.address.impl;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressRequest;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService.RequestResponseType;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressServiceImpl;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressXmlProcessJaxbImpl;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import goc.servicecanada.softwarefactory.webservices.WSAddressStub;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import org.apache.axis2.AxisFault;
import org.junit.Test;

public class BDMWSAddressServiceImplTest extends CuramServerTestJUnit4 {

  @Tested
  BDMWSAddressServiceImpl wsAddressService;

  /**
   * Given a call to the webservice public API is made for a Search
   * When the method is executed
   * Then the expectation is the execution of functions responsible for the
   * high-level processes of:
   * - Constructing an XML payload request
   * - Configuring the Axis client for auth and endpoint
   * - Calling the external webservice
   * - Deconstructing the XML payload response
   *
   * @param jaxbProcessor BDMWSAddressXmlProcessJaxbImpl Mocked
   * @throws AppException
   * @throws AxisFault
   * @throws InformationalException
   */
  @Test
  public void doCallServiceWithSearchRequest(@Mocked
  final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor)
    throws AppException, AxisFault, InformationalException {

    // SETUP
    new Expectations(wsAddressService) {

      {
        // mock the actual call to the webservice
        wsAddressService.callWSAddress(anyString, (WSAddressStub) any);
        result = "SomeString";

      }
    };

    // EXERCISE
    wsAddressService.search(new BDMWSAddressRequest());

    // VERIFY
    new Verifications() {
      // we expect the following high-level methods to be executed

      {
        jaxbProcessor.construct((BDMWSAddressRequest) any,
          (RequestResponseType) any);
        times = 1;
        wsAddressService.setServiceAuthentication((WSAddressStub) any);
        times = 1;
        wsAddressService.setServiceEndpoint((WSAddressStub) any);
        times = 1;
        wsAddressService.callWSAddress(anyString, (WSAddressStub) any);
        times = 1;
        jaxbProcessor.parse(anyString, (RequestResponseType) any);
        times = 1;
      }
    };

  }

  /**
   * Given a call to the webservice public API is made for a Validate
   * When the method is executed
   * Then the expectation is the execution of functions responsible for the
   * high-level processes of:
   * - Constructing an XML payload request
   * - Configuring the Axis client for auth and endpoint
   * - Calling the external webservice
   * - Deconstructing the XML payload response
   *
   * @param jaxbProcessor BDMWSAddressXmlProcessJaxbImpl Mocked
   * @throws AppException
   * @throws AxisFault
   * @throws InformationalException
   */
  @Test
  public void doCallServiceWithValidateRequest(@Mocked
  final BDMWSAddressXmlProcessJaxbImpl jaxbProcessor)
    throws AppException, AxisFault, InformationalException {

    // SETUP
    new Expectations(wsAddressService) {

      {
        // mock the actual call to the webservice
        wsAddressService.callWSAddress(anyString, (WSAddressStub) any);
        result = "SomeString";

      }
    };

    // EXERCISE
    wsAddressService.validate(new BDMWSAddressRequest());

    // VERIFY
    new Verifications() {
      // we expect the following high-level methods to be executed

      {
        jaxbProcessor.construct((BDMWSAddressRequest) any,
          (RequestResponseType) any);
        times = 1;

        wsAddressService.setServiceAuthentication((WSAddressStub) any);
        times = 1;
        wsAddressService.setServiceEndpoint((WSAddressStub) any);
        times = 1;
        wsAddressService.callWSAddress(anyString, (WSAddressStub) any);
        times = 1;
        jaxbProcessor.parse(anyString, (RequestResponseType) any);
        times = 1;
      }
    };

  }

}
