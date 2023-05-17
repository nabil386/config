package curam.ca.gc.bdm.sl.application.impl;

import curam.commonintake.authorisation.facade.struct.AuthorisationDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface BDMProgramAuthorisation {

  public void authoriseProgram(final AuthorisationDetails details)
    throws AppException, InformationalException;
}
