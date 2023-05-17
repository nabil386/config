package curam.ca.gc.bdm.sl.bdmintakeapplicationsin.impl;

import curam.ca.gc.bdm.entity.bdmintakeapplicationsin.fact.BDMIntakeApplicationSINFactory;
import curam.ca.gc.bdm.entity.bdmintakeapplicationsin.struct.BDMIntakeApplicationSINDtlsList;
import curam.ca.gc.bdm.entity.bdmintakeapplicationsin.struct.BDMIntakeApplicationSINIdentifierKey;
import curam.ca.gc.bdm.sl.bdmintakeapplicationsin.struct.IsSINDuplicatedResult;
import curam.ca.gc.bdm.sl.bdmintakeapplicationsin.struct.SINKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * Service layer operations for the BDMIntakeApplicationSIN table
 */
public class BDMIntakeApplicationSIN extends
  curam.ca.gc.bdm.sl.bdmintakeapplicationsin.base.BDMIntakeApplicationSIN {

  /** Convenience object to access the IntakeApplicationSIN table **/
  private final curam.ca.gc.bdm.entity.bdmintakeapplicationsin.intf.BDMIntakeApplicationSIN iaSinTable =
    BDMIntakeApplicationSINFactory.newInstance();

  @Override
  public IsSINDuplicatedResult isSINDuplicated(final SINKey sinKey)
    throws AppException, InformationalException {

    final BDMIntakeApplicationSINIdentifierKey iaSinIdKey =
      new BDMIntakeApplicationSINIdentifierKey();
    iaSinIdKey.sin = sinKey.sin;
    iaSinIdKey.intakeApplicationTypeID = sinKey.intakeApplicationTypeID;
    final BDMIntakeApplicationSINDtlsList searchResult =
      iaSinTable.searchBySINAndType(iaSinIdKey);

    final IsSINDuplicatedResult isSinDuplicatedResultStruct =
      new IsSINDuplicatedResult();
    if (searchResult.dtls.size() > 0) {
      isSinDuplicatedResultStruct.result = true;
    } else {
      isSinDuplicatedResultStruct.result = false;
    }
    return isSinDuplicatedResultStruct;

  }

}
