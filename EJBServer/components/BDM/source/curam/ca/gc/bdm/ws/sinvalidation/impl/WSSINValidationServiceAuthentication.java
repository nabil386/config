package curam.ca.gc.bdm.ws.sinvalidation.impl;

import com.google.inject.ImplementedBy;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.AccessLevel;
import curam.util.type.AccessLevelType;
import curam.util.type.Implementable;
import org.tempuri.ValidationServiceStub;

/**
 * Responsible for adding extra Authorization/Authentication settings to the
 * external SIN validation service used for BDM in their BAD Foundation
 * application
 */
@AccessLevel(AccessLevelType.EXTERNAL)
@ImplementedBy(WSSINValidationServiceAuthenticationImpl.class)
@Implementable
public interface WSSINValidationServiceAuthentication {

  /**
   * Set the authentication settings on the client used to connect outbound to
   * the external SIN valiadtion service
   *
   * @param validationServiceStub ValidationServiceStub - Generated Java class
   * from the BDMValidateSINService WSDL
   * @return ValidationServiceStub
   * @throws AppException
   * @throws InformationalException
   */
  ValidationServiceStub setServiceAuthentication(
    final ValidationServiceStub validationServiceStub)
    throws AppException, InformationalException;
}
