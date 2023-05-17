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
import curam.util.type.NotFoundIndicator;
import goc.servicecanada.softwarefactory.webservices.WSAddressStub;
import org.apache.axis2.transport.http.HttpTransportProperties;

/**
 * This enables basic authentication on the service client
 */
public class BDMWSAddressServiceAuthentication {

  @Inject
  private TargetSystemDAO targetSystemDAO;

  public BDMWSAddressServiceAuthentication() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  public WSAddressStub
    setServiceAuthentication(final WSAddressStub validationServiceStub)
      throws AppException, InformationalException {

    // This authentication pattern was written for Program Integrity by Billy
    // Dennigen and an example can be found in
    // curam.programintegrity.impl.IIWebServiceAuthenticationImpl
    // An internal wiki called IBM InfoSphere Identity Insight Update under the
    // SPM team community on Connections which describes it greater

    final TargetSystemServiceDtls targetSystemServiceDtls =
      getExtWsAddressServiceTargetSystemServiceDtls();
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

  /**
   * Retrieves the TargetSystemServiceDtls from the TargetSystemService entity
   * for the external SIN validation service.
   *
   * TODO: refactor into one common util method shared between WSAddress and
   * WSSin
   *
   * @return TargetSystemServiceDtls
   * @throws AppException
   * @throws InformationalException
   */
  TargetSystemServiceDtls getExtWsAddressServiceTargetSystemServiceDtls()
    throws AppException, InformationalException {

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

    return targetSystemServiceDtls;
  }

}
