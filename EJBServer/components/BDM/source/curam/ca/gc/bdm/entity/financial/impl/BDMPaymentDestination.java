package curam.ca.gc.bdm.entity.financial.impl;

import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentDestinationFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

public class BDMPaymentDestination
  extends curam.ca.gc.bdm.entity.financial.base.BDMPaymentDestination {

  /**
   * Set sync indicator to true if the destination end date is in the future
   */
  @Override
  protected void preinsert(final BDMPaymentDestinationDtls details)
    throws AppException, InformationalException {

    if (!details.endDate.before(Date.getCurrentDate())
      || details.endDate.isZero())
      details.syncDestinationInd = true;

  }

  /**
   * Set the sync indicator if the destination has been changed or if the end
   * date is now in the future (when it wasn't previously)
   */
  @Override
  protected void premodify(final BDMPaymentDestinationKey key,
    final BDMPaymentDestinationDtls details)
    throws AppException, InformationalException {

    final BDMPaymentDestinationDtls originalDetails =
      BDMPaymentDestinationFactory.newInstance().read(key);

    // if the destination ID has changed, it will have to be synced
    if (originalDetails.destinationID != details.destinationID) {
      details.syncDestinationInd = true;
    }
    // if the end date has changed so that now the destination
    // end date is past the current date when it wasn't before
    if ((!details.endDate.before(Date.getCurrentDate())
      || details.endDate.isZero())
      && originalDetails.endDate.before(Date.getCurrentDate())
      && !originalDetails.endDate.isZero()) {

      details.syncDestinationInd = true;

    }

  }

}
