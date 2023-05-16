package curam.ca.gc.bdm.ws.address.impl;

import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService.RequestResponseType;
import java.util.HashMap;
import java.util.Map;

public class BDMWSAddressResponse {

  private boolean isSuccessfulResponse = false;

  private final Map<RequestResponseType, BDMWSAddressTypedResponse> individualRequestResponses =
    new HashMap<RequestResponseType, BDMWSAddressTypedResponse>();

  public boolean isSuccessfulResponse() {

    return this.isSuccessfulResponse;
  }

  public void setSuccessfulResponse(final boolean isSuccessfulResponse) {

    this.isSuccessfulResponse = isSuccessfulResponse;
  }

  public BDMWSAddressSearchResponse getSearchResponse() {

    final BDMWSAddressSearchResponse searchResponse =
      (BDMWSAddressSearchResponse) individualRequestResponses
        .get(RequestResponseType.SEARCH);

    return searchResponse != null ? searchResponse
      : new BDMWSAddressSearchResponse();
  }

  public boolean hasSearchResponse() {

    return individualRequestResponses.get(RequestResponseType.SEARCH) != null;

  }

  public BDMWSAddressValidateResponse getValidateResponse() {

    final BDMWSAddressValidateResponse validateResponse =
      (BDMWSAddressValidateResponse) individualRequestResponses
        .get(RequestResponseType.VALIDATE);

    return validateResponse != null ? validateResponse
      : new BDMWSAddressValidateResponse();
  }

  public boolean hasValidateResponse() {

    return individualRequestResponses
      .get(RequestResponseType.VALIDATE) != null;

  }

  public Map<RequestResponseType, BDMWSAddressTypedResponse>
    getIndividualRequestResponses() {

    return this.individualRequestResponses;
  }

}
