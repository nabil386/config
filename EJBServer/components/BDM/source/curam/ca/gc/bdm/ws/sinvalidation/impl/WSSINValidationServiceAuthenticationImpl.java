package curam.ca.gc.bdm.ws.sinvalidation.impl;

import curam.ca.gc.bdm.ws.impl.WSCommonUtils;
import curam.ctm.sl.entity.struct.TargetSystemServiceDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.tempuri.ValidationServiceStub;

/**
 * Default implementation class for ExternalValidateSINServiceAuthentication
 * interface.
 *
 * This enables basic authentication on the service client
 */
public class WSSINValidationServiceAuthenticationImpl
  implements WSSINValidationServiceAuthentication {

  @Override
  public ValidationServiceStub setServiceAuthentication(
    final ValidationServiceStub validationServiceStub)
    throws AppException, InformationalException {

    // This authentication pattern was written for Program Integrity by Billy
    // Dennigen and an example can be found in
    // curam.programintegrity.impl.IIWebServiceAuthenticationImpl
    // An internal wiki called IBM InfoSphere Identity Insight Update under the
    // SPM team community on Connections which describes it greater

    final TargetSystemServiceDtls targetSystemServiceDtls =
      new WSSINValidationServiceUtils()
        .getExtSINServiceTargetSystemServiceDtls();
    final HttpTransportProperties.Authenticator basicAuthentication =
      new HttpTransportProperties.Authenticator();
    basicAuthentication.setUsername(targetSystemServiceDtls.username);
    basicAuthentication.setPassword(
      WSCommonUtils.getPlainTextPassword(targetSystemServiceDtls.password));
    basicAuthentication.setPreemptiveAuthentication(true);
    validationServiceStub._getServiceClient().getOptions().setProperty(
      org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE,
      basicAuthentication);

    return validationServiceStub;

  }

}
