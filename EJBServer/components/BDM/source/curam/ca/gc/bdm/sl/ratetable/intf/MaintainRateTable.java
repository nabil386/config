package curam.ca.gc.bdm.sl.ratetable.intf;

import curam.ca.gc.bdm.facade.ratetable.struct.BDMRateHeaderDetailsList;
import curam.core.facade.struct.RateHeaderDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

public interface MaintainRateTable {

  public void validateCreateNewRateTableHeader(Date effectiveDate,
    String rateTableType) throws AppException, InformationalException;

  public void validateCloneNewRateTableHeader(Date effectiveDate,
    long rateHeaderID) throws AppException, InformationalException;

  public BDMRateHeaderDetailsList
    addEditRestrictions(RateHeaderDetailsList detailsList)
      throws AppException, InformationalException;
}
