package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.intf;

import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRRestResponse;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRSearchTable;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.WSSearchTableRequest;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.WSSinValidation;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.io.IOException;

public interface BDMWSSINValidateInterfaceIntf {

  public WSSinValidation validatePersonBySIN(WSSearchTableRequest req)
    throws AppException, InformationalException, IOException;

  public BDMSINSIRRestResponse validatePersonBySIN(BDMSINSIRSearchTable req)
    throws AppException, InformationalException, IOException;

}
