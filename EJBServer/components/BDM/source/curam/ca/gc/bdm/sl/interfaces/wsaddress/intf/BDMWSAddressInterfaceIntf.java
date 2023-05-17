package curam.ca.gc.bdm.sl.interfaces.wsaddress.intf;

import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos.WsaddrValidationRequest;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WSAddress;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WsaddrSearchRequest;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface BDMWSAddressInterfaceIntf {

  public WSAddress
    searchAddressesByPostalCode(final WsaddrSearchRequest addressSearchKey)
      throws AppException, InformationalException;

  public boolean
    validateAddress(final WsaddrValidationRequest addressSearchKey)
      throws AppException, InformationalException;

}
