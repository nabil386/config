package curam.ca.gc.bdm.ws.address.impl;

import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService.RequestResponseType;

public abstract class BDMWSAddressTypedResponse {

  private RequestResponseType responseType;

  RequestResponseType getResponseType() {

    return this.responseType;
  }

  void setResponseType(final RequestResponseType responseType) {

    this.responseType = responseType;
  }

}
