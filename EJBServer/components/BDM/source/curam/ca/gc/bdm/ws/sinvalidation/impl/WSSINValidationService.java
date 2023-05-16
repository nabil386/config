package curam.ca.gc.bdm.ws.sinvalidation.impl;

import curam.codetable.impl.GENDEREntry;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import curam.util.webservices.StubFactory;
import curam.util.webservices.StubFactory.PooledStub;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang.StringUtils;
import org.tempuri.ValidationServiceStub;
import org.tempuri.ValidationServiceStub.ArrayOfValidationItem;
import org.tempuri.ValidationServiceStub.ValidateSecrets;
import org.tempuri.ValidationServiceStub.ValidateSecretsResponse;
import org.tempuri.ValidationServiceStub.ValidationItem;
import org.tempuri.ValidationServiceStub.ValidationRequest;
import org.tempuri.ValidationServiceStub.ValidationResponse;
import static curam.ca.gc.bdm.ws.impl.WSCommonUtils.logError;
import static curam.ca.gc.bdm.ws.impl.WSCommonUtils.throwAppRuntimeException;

/**
 * A wrapper/facade to call the external BDM validate SIN service via SOAP
 * using Cúram Axis code generation
 */
public class WSSINValidationService {

  /**
   * Validate a user's information, including their SIN, against an
   * externally-provided validation service to confirm that the SIN they
   * provided matches their known information.
   *
   * The boolean result indicates whether the match was valid or invalid.
   *
   * If any exception/error occurred during the call, an appropriate Cúram
   * exception will be thrown.
   *
   * @param requestDetails ExternalValidateSINRequestDetails user details
   * @return Boolean validity result (valid/invalid)
   *
   * @throws AppException
   */
  public Boolean validate(final WSSINValidationRequestDetails requestDetails)
    throws AppException {

    Boolean serviceResult = false;

    // Create the service from a pool or newly
    ValidationServiceStub stub = null;
    PooledStub<ValidationServiceStub> pooledStub = null;

    try {
      // TODO will the size of the pool matter? is it configurable?
      pooledStub = StubFactory.getPooledStub(ValidationServiceStub.class);
      stub = pooledStub.getStub();

      // set the service client options (incl. authentication protocol)
      setServiceClientOptions(stub);

      // Create the request payload
      final ValidationServiceStub.ValidateSecrets secrets =
        createValidateSecretsPayload(requestDetails);

      // Call out to the external service
      final ValidationServiceStub.ValidateSecretsResponse response =
        stub.validateSecrets(secrets);

      // Retrieve the simple result
      serviceResult = isValidationValid(response);

      // Return the pooled stub if available
      pooledStub.returnToPool();
    } catch (final Exception e) {
      // if we cannot get a pooled stub to use the service, the credentials
      // weren't retrieved correctly, or a fault has occured in the service
      // call, then we should just stop processing as something nasty has gone
      // wrong
      throwAppRuntimeException(e);
    }

    // Record the response result
    return serviceResult;

  }

  /**
   * Configures the options available on the service client access to the
   * validation
   * service
   *
   * @param stub ValidationServiceStub
   */
  private ValidationServiceStub
    setServiceClientOptions(final ValidationServiceStub stub)
      throws AppException, InformationalException {

    final ServiceClient client = stub._getServiceClient();

    Integer configuredTimeout = Configuration
      .getIntProperty(EnvVars.BDM_ENV_BDM_SIN_SERVICE_VALIDATION_TIMEOUT);
    if (configuredTimeout == null) {
      configuredTimeout =
        EnvVars.BDM_ENV_BDM_SIN_SERVICE_VALIDATION_TIMEOUT_DEFAULT;
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
   * Configures the external SIN validation authentication on the service client
   * if it has been implemented
   *
   * @param stub ValidationServiceStub
   * @return ValidationServiceStub
   */
  private ValidationServiceStub
    setServiceAuthentication(ValidationServiceStub stub) {

    try {
      final WSSINValidationServiceAuthentication wsAuthenciation =
        GuiceWrapper.getInjector()
          .getInstance(WSSINValidationServiceAuthentication.class);
      stub = wsAuthenciation.setServiceAuthentication(stub);
    } catch (final Exception e) {
      // It means there is no implementation. Log the event and use the standard
      // stub
      logError(e,
        "External SIN validation service doesn't have an implemented authentication module.");
    }
    return stub;
  }

  /**
   * Maps the {@linkplain ExternalValidateSINRequestDetails} object data to the
   * format of the payload for the SOAP envelope.
   *
   * @param requestDetails ExternalValidateSINRequestDetails
   * @return ValidationServiceStub.ValidateSecrets
   */
  private ValidationServiceStub.ValidateSecrets createValidateSecretsPayload(
    final WSSINValidationRequestDetails requestDetails) {

    // Create the request payload
    final ArrayOfValidationItem items =
      mapRequestDetailsToValidationItems(requestDetails);
    final ValidationRequest request = new ValidationRequest();
    request.setValidationItems(items);
    final ValidationServiceStub.ValidateSecrets secrets =
      new ValidateSecrets();
    secrets.setValidationRequest(request);
    return secrets;
  }

  /**
   * Checks the service's response to see if the call has deemed the request
   * details valid.
   *
   * @param response ValidateSecretsResponse service response
   * @return Boolean
   */
  Boolean isValidationValid(final ValidateSecretsResponse response) {

    if (response != null) {
      if (response.isValidateSecretsResultSpecified()) {
        final ValidationResponse validationResponse =
          response.getValidateSecretsResult();
        if (validationResponse.isValidationResultsSpecified()) {
          return validationResponse.getIsValidationValid();
        }
      }
    }
    return false;
  }

  /**
   * Maps the fields on the provided request details object to the service
   * request object.
   *
   * @param requestDetails ExternalValidateSINRequestDetails
   * @return ArrayOfValidationItem
   */
  ArrayOfValidationItem mapRequestDetailsToValidationItems(
    final WSSINValidationRequestDetails requestDetails) {

    final ArrayOfValidationItem items = new ArrayOfValidationItem();
    // TODO - should we even be accepting a request details without a SIN?
    // TODO - are any of these validation items optional?
    // TODO - do the item names need to be uppercased specifically?

    // SIN format is a specifically validated number with leading zeros
    if (!StringUtils.isEmpty(requestDetails.getSin())) {
      items.addValidationItem(
        createValidationItem("SIN", requestDetails.getSin()));

      // SIN_RESTRICTIONS format is the SIN
      // TODO confirm
      items.addValidationItem(createValidationItem("SIN_RESTRICTIONS",
        String.valueOf(requestDetails.getSin())));
    }

    // GIVEN_NAME format is a character string (accents are removed and length
    // limitations are enforced by the service)
    if (!StringUtils.isEmpty(requestDetails.getGivenName())) {
      items.addValidationItem(createValidationItem("GIVEN_NAME",
        requestDetails.getGivenName().toUpperCase()));
    }

    // SURNAME format is a character string (accents are removed and length
    // limitations are enforced by the service)
    if (!StringUtils.isEmpty(requestDetails.getSurname())) {
      items.addValidationItem(createValidationItem("SURNAME",
        requestDetails.getSurname().toUpperCase()));
    }

    // DATE_OF_BIRTH format is yyyyMMdd
    if (requestDetails.getDateOfBirth() != Date.kZeroDate) {
      items.addValidationItem(createValidationItem("DATE_OF_BIRTH",
        String.valueOf(requestDetails.getDateOfBirth()).replace("-", "")));
    }

    // GENDER format is 0 for Male and 1 for Female
    if (requestDetails.getGender() != null) {
      if (requestDetails.getGender().getCode()
        .equalsIgnoreCase(GENDEREntry.MALE.getCode())) {
        items.addValidationItem(
          createValidationItem("GENDER", String.valueOf(0)));
      } else {
        items.addValidationItem(
          createValidationItem("GENDER", String.valueOf(1)));
      }
    }

    return items;
  }

  /**
   * Creates a validation item for the service request.
   *
   * @param itemName String
   * @param value String
   * @return ValidationItem
   */
  ValidationItem createValidationItem(final String itemName,
    final String value) {

    final ValidationItem item = new ValidationItem();
    item.setItemName(itemName);
    item.setValue(value);
    item.setTolerance(false);
    item.setToleranceType(ValidationServiceStub.ToleranceType.NONE);
    return item;
  }

  /**
   * Set the external SIN validation service endpoint reference in the service
   * client
   *
   * @param stub ValidationServiceStub
   * @return ValidationServiceStub
   *
   * @throws AppException
   * @throws InformationalException
   */
  ValidationServiceStub setServiceEndpoint(final ValidationServiceStub stub)
    throws AppException, InformationalException {

    final String endpointURL =
      new WSSINValidationServiceUtils().getExtSINServiceEndpointURL();
    final EndpointReference endPointReference =
      new EndpointReference(endpointURL);
    stub._getServiceClient().getOptions().setTo(endPointReference);

    return stub;
  }

}
