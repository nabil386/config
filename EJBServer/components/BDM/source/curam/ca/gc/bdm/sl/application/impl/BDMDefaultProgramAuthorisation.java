package curam.ca.gc.bdm.sl.application.impl;

import curam.commonintake.authorisation.facade.struct.AuthorisationDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMDefaultProgramAuthorisation
  implements BDMProgramAuthorisation {

  @Override
  public void authoriseProgram(final AuthorisationDetails details)
    throws AppException, InformationalException {

    // default implementation is invoked, the binding for the application case
    // admin identifier has not been added.

    // throw new UnimplementedException();
  }

}
