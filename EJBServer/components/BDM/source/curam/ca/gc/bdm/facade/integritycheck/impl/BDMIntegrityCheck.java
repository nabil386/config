package curam.ca.gc.bdm.facade.integritycheck.impl;

import curam.ca.gc.bdm.facade.integritycheck.struct.BDMIntegrityCheckKey;
import curam.ca.gc.bdm.sl.integritycheck.fact.BDMIntegrityCheckFactory;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMIntegrityCheck
  extends curam.ca.gc.bdm.facade.integritycheck.base.BDMIntegrityCheck {

  @Override
  public void sinIntegrityCheck(final BDMIntegrityCheckKey key)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.sl.integritycheck.struct.BDMIntegrityCheckKey checkKey =
      new curam.ca.gc.bdm.sl.integritycheck.struct.BDMIntegrityCheckKey();

    checkKey.assign(key);

    BDMIntegrityCheckFactory.newInstance().sinIntegrityCheck(checkKey);

  }

  @Override
  public void sinIntegrityCheckOnPerson(final BDMIntegrityCheckKey key)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.sl.integritycheck.struct.BDMIntegrityCheckKey checkKey =
      new curam.ca.gc.bdm.sl.integritycheck.struct.BDMIntegrityCheckKey();

    checkKey.assign(key);

    BDMIntegrityCheckFactory.newInstance()
      .sinIntegrityCheckOnPerson(checkKey);

  }

}
