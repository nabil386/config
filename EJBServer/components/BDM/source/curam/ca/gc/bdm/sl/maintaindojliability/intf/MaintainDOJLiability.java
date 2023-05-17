package curam.ca.gc.bdm.sl.maintaindojliability.intf;

import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.sl.financial.struct.BDMCheckDOJLiabilityExistDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMModifyDOJLbyDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMRegisterDOJLbyDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface MaintainDOJLiability {

  public BDMExternalLiabilityKey
    registerLiability(BDMRegisterDOJLbyDetails details)
      throws AppException, InformationalException;

  public void createDeduction(BDMExternalLiabilityKey key)
    throws AppException, InformationalException;

  public void modifyLiability(BDMModifyDOJLbyDetails modifyDetails)
    throws AppException, InformationalException;

  public void deleteLiability(BDMExternalLiabilityKey key)
    throws AppException, InformationalException;

  public BDMExternalLiabilityKey
    getLiabilityByExtRefNumber(final BDMCheckDOJLiabilityExistDetails details)
      throws AppException, InformationalException;

}
