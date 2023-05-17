package curam.ca.gc.bdm.sl.concernroleaddress.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf.BDMMaintainPaymentDestination;
import curam.core.events.CONCERNROLEADDRESS;
import curam.core.struct.ConcernRoleAddressKey;
import curam.util.events.impl.EventFilter;
import curam.util.events.impl.EventHandler;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

/**
 * Event Handler for when the concern role address has been
 * added/modified/removed
 */
public class BDMConcernRoleAddressEventHandler
  implements EventHandler, EventFilter {

  @Inject
  private BDMMaintainPaymentDestination maintainPaymentDestinations;

  public BDMConcernRoleAddressEventHandler() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public boolean accept(final Event event)
    throws AppException, InformationalException {

    return true;
  }

  @Override
  public void eventRaised(final Event event)
    throws AppException, InformationalException {

    final ConcernRoleAddressKey addressKey = new ConcernRoleAddressKey();
    addressKey.concernRoleAddressID = event.primaryEventData;

    // if it is insert/modify sync the destinations
    if (CONCERNROLEADDRESS.INSERT_CONCERN_ROLE_ADDRESS.eventType
      .equals(event.eventKey.eventType)
      || CONCERNROLEADDRESS.MODIFY_CONCERN_ROLE_ADDRESS.eventType
        .equals(event.eventKey.eventType)) {
      maintainPaymentDestinations.syncDestinationsOnAddressChange(addressKey);

    }
    // if it is delete, remove the destinations
    else if (CONCERNROLEADDRESS.REMOVE_CONCERN_ROLE_ADDRESS.eventType
      .equals(event.eventKey.eventType)) {
      maintainPaymentDestinations.deleteOnAddressDeletion(addressKey);
    }

  }

}
