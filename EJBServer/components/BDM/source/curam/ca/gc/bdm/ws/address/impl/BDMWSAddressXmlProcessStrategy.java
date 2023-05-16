package curam.ca.gc.bdm.ws.address.impl;

import com.google.inject.ImplementedBy;
import curam.ca.gc.bdm.ws.address.impl.BDMWSAddressService.RequestResponseType;
import curam.util.exception.AppException;

@ImplementedBy(BDMWSAddressXmlProcessJaxbImpl.class)
public interface BDMWSAddressXmlProcessStrategy {

  public String construct(BDMWSAddressRequest wsAddressRequest,
    RequestResponseType wsAddressRequestType) throws AppException;

  public BDMWSAddressResponse parse(String xml,
    RequestResponseType wsAddressRequestType) throws AppException;

}
