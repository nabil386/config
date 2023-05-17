package curam.ca.gc.bdm.evidence.validation.impl;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * The contract for evidence validator classes.
 */
public abstract class BDMAbstractEvidenceValidator {

  public abstract boolean isValid()
    throws AppException, InformationalException;

}
