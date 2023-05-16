package curam.ca.gc.bdm.sl.productdelivery.intf;

import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.facade.productdelivery.struct.BDMDOJDeductionDetails;
import curam.core.facade.struct.ReadDeductionKey;
import curam.core.facade.struct.ThirdPartyDeductionActivationStruct;
import curam.core.facade.struct.ThirdPartyDetails;
import curam.core.struct.CaseDeductionItemDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public interface MaintainDeductionDetails {

  public int calculatePriority(final CaseDeductionItemDtls details)
    throws AppException, InformationalException;

  public int calculatePriority(final CaseDeductionItemDtls details,
    final BDMExternalLiabilityKey extLbyKey)
    throws AppException, InformationalException;

  public void resequencePriorities(final long caseDeductionitemID)
    throws AppException, InformationalException;

  public int getBasePriority(final long caseID, final String deductionName)
    throws AppException, InformationalException;

  public ThirdPartyDetails determineThirdPartyAccountDetails(
    final String deductionName) throws AppException, InformationalException;

  public void determineThirdPartyDetails(
    final ThirdPartyDeductionActivationStruct details)
    throws AppException, InformationalException;

  public BDMDOJDeductionDetails readDOJDeductionDetails(
    final ReadDeductionKey key) throws AppException, InformationalException;
}
