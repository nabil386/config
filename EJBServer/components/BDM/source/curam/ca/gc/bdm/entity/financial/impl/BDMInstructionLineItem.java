package curam.ca.gc.bdm.entity.financial.impl;

import curam.ca.gc.bdm.codetable.BDMEXTERNALPROCSTATUSTYPE;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMInstructionLineItem
  extends curam.ca.gc.bdm.entity.financial.base.BDMInstructionLineItem {

  @Override
  protected void preinsert(final BDMInstructionLineItemDtls details)
    throws AppException, InformationalException {

    details.extProcStatusTypeCode = BDMEXTERNALPROCSTATUSTYPE.UNPROCESSED;
  }

}
