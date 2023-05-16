package curam.ca.gc.bdm.sl.impl;

import curam.core.impl.PersonSearchKeyValidatorIntf;
import curam.core.struct.PersonSearchKey1;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface BDMSearchKeyValidatorIntf
  extends PersonSearchKeyValidatorIntf {

  @Override
  void validateSearchKey(PersonSearchKey1 var1)
    throws InformationalException, AppException;

}
