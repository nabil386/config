package curam.ca.gc.bdm.ws.sinvalidation.impl;

import com.google.inject.Inject;
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

/**
 * Provides utility methods used for the external SIN validation service within
 * this package.
 */
public class WSSINValidationServiceUtils {

  @Inject
  private TargetSystemDAO targetSystemDAO;

  /** Constructor */
  public WSSINValidationServiceUtils() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Retrieves the TargetSystemServiceDtls from the TargetSystemService entity
   * for the external SIN validation service.
   *
   * @return TargetSystemServiceDtls
   * @throws AppException
   * @throws InformationalException
   */
  TargetSystemServiceDtls getExtSINServiceTargetSystemServiceDtls()
    throws AppException, InformationalException {

    final String targetSystemName = Configuration
      .getProperty(EnvVars.BDM_ENV_BDM_WSSINVALIDATION_TARGET_SYSTEM_NAME);
    final TargetSystem targetSystem =
      targetSystemDAO.readByTargetSystemNameAndStatus(
        TARGETSYSTEMSTATUSEntry.ACTIVE, targetSystemName);

    final NameSystemIDAndStatusKey nameAndStatusKey =
      new NameSystemIDAndStatusKey();
    nameAndStatusKey.name =
      SYSTEMSERVICENAMEEntry.SINVERIFICATIONSERVICE.getCode();
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

  /**
   * Retrieves the URL for the external SIN validation service.
   *
   * @return String
   * @throws AppException
   * @throws InformationalException
   */
  String getExtSINServiceEndpointURL()
    throws AppException, InformationalException {

    final String targetSystemName = Configuration
      .getProperty(EnvVars.BDM_ENV_BDM_WSSINVALIDATION_TARGET_SYSTEM_NAME);
    final TargetSystem targetSystem =
      targetSystemDAO.readByTargetSystemNameAndStatus(
        TARGETSYSTEMSTATUSEntry.ACTIVE, targetSystemName);

    final NameSystemIDAndStatusKey nameAndStatusKey =
      new NameSystemIDAndStatusKey();
    nameAndStatusKey.name =
      SYSTEMSERVICENAMEEntry.SINVERIFICATIONSERVICE.getCode();
    nameAndStatusKey.systemID = targetSystem.getID();
    nameAndStatusKey.status = TARGETSYSTEMSTATUSEntry.ACTIVE.getCode();

    final TargetSystemService targetSystemService =
      TargetSystemServiceFactory.newInstance();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final TargetSystemServiceDtls targetSystemServiceDtls =
      targetSystemService.readByNameSystemIDAndStatus(nfIndicator,
        nameAndStatusKey);

    return targetSystem.getRootURL() + targetSystemServiceDtls.extensionURL;
  }

}
