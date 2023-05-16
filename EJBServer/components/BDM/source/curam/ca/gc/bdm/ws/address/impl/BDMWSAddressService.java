package curam.ca.gc.bdm.ws.address.impl;

import com.google.inject.ImplementedBy;
import curam.util.exception.AppException;

@ImplementedBy(BDMWSAddressServiceImpl.class)
public interface BDMWSAddressService {

  public BDMWSAddressResponse
    search(final BDMWSAddressRequest wsAddressRequest) throws AppException;

  public BDMWSAddressResponse
    validate(final BDMWSAddressRequest wsAddressRequest) throws AppException;

  public static enum RequestResponseType {

    SEARCH {

      @Override
      public String toString() {

        return BDMWSAddressConstants.kSearch;
      }
    },
    VALIDATE {

      @Override
      public String toString() {

        return BDMWSAddressConstants.kValidate;
      }
    }
  }

}
