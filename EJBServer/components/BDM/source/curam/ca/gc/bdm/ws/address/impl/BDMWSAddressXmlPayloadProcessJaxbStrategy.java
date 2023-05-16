package curam.ca.gc.bdm.ws.address.impl;

import curam.ca.gc.bdm.ws.address.gen.impl.WSMessage;
import curam.util.exception.AppException;

public interface BDMWSAddressXmlPayloadProcessJaxbStrategy {

  public BDMWSAddressTypedResponse parse(WSMessage wsMessage)
    throws AppException;

}
