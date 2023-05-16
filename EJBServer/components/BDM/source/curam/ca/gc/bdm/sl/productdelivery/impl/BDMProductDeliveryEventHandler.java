/**
 *
 */
package curam.ca.gc.bdm.sl.productdelivery.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.sl.maintaincasedeductions.intf.MaintainCaseDeductions;
import curam.events.PRODUCTDELIVERY;
import curam.util.events.impl.EventFilter;
import curam.util.events.impl.EventHandler;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

/**
 * @author jigar.shah
 *
 */
public class BDMProductDeliveryEventHandler
  implements EventHandler, EventFilter {

  @Inject
  private MaintainCaseDeductions maintainCaseDeductions;

  public BDMProductDeliveryEventHandler() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public boolean accept(final Event event)
    throws AppException, InformationalException {

    boolean acceptEventInd = false;
    if (PRODUCTDELIVERY.INSERT.eventClass.equals(event.eventKey.eventClass)
      && PRODUCTDELIVERY.INSERT.eventType.equals(event.eventKey.eventType)) {
      acceptEventInd = true;
    }

    return acceptEventInd;
  }

  @Override
  public void eventRaised(final Event event)
    throws AppException, InformationalException {

    if (PRODUCTDELIVERY.INSERT.eventClass.equals(event.eventKey.eventClass)
      && PRODUCTDELIVERY.INSERT.eventType.equals(event.eventKey.eventType)) {
      // DO NOTHING - this can be used if any action needs to be taken when Case
      // gets created. Not compatible for any action which needs regenerate
      // Financials.
    }
  }
}
