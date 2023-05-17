
package curam.ca.gc.bdm.ws.address.impl;

import curam.ca.gc.bdm.ws.address.gen.impl.Parm;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.Output.AllResults.Results.Result;
import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage.Output.AllResults.Results.Result.FunctionalMessages.FunctionalMessage;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService.RequestResponseType;
import curam.util.exception.AppException;

public class BDMWSAddressValidatePayloadJaxbProcessImpl
  implements BDMWSAddressXmlPayloadProcessJaxbStrategy {

  @Override
  public BDMWSAddressValidateResponse parse(final WSMessage wsMessage)
    throws AppException {

    final BDMWSAddressValidateResponse validateResponse =
      new BDMWSAddressValidateResponse();

    validateResponse.setResponseType(RequestResponseType.VALIDATE);

    for (final Result result : wsMessage.getOutput().getAllResults()
      .getResults().getResult()) {

      for (final Parm outputParm : result.getOutputParameters().getParm()) {
        if (outputParm.getName()
          .equalsIgnoreCase(BDMWSAddressConstants.kStatusCode)) {
          validateResponse.setIsAddressValid(outputParm.getContent()
            .equalsIgnoreCase(BDMWSAddressConstants.kValid));
        }
      }

      for (final FunctionalMessage functionalMessage : result
        .getFunctionalMessages().getFunctionalMessage()) {
        if (functionalMessage.getAction()
          .equalsIgnoreCase(BDMWSAddressConstants.kCorrectionError)) {
          validateResponse.getValidationErrorMessages()
            .add(functionalMessage.getContent());
        }

      }

    }
    return validateResponse;
  }

}
