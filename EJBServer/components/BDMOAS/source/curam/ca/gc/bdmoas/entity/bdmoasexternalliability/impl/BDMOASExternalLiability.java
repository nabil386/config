package curam.ca.gc.bdmoas.entity.bdmoasexternalliability.impl;

import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityDtls;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMOASExternalLiability extends
  curam.ca.gc.bdm.entity.bdmexternalliability.base.BDMExternalLiability {

  @Override
  protected void premodify(final BDMExternalLiabilityKey key,
    final BDMExternalLiabilityDtls details)
    throws AppException, InformationalException {

    // do nothing
  }
}
