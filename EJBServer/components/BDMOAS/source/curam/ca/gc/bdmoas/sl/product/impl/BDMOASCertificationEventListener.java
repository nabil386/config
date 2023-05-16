package curam.ca.gc.bdmoas.sl.product.impl;

import curam.core.events.PERSON;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderDtlsList;
import curam.util.events.impl.EventFilter;
import curam.util.events.impl.EventHandler;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * Lists for modifications to Person data and invokes
 * {@link BDMOASProductManager#manageProduct()} to modify the OAS Benefit PDC
 * certification from date if necessary.
 */
public class BDMOASCertificationEventListener
  implements EventHandler, EventFilter {

  /**
   * Returns true if the event is {@link PERSON#MODIFY_PERSON}.
   */
  @Override
  public boolean accept(final Event event)
    throws AppException, InformationalException {

    if (PERSON.MODIFY_PERSON.equals(event.eventKey)) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * Invokes {@link BDMOASProductManager#manageProduct()} for each OAS IC to
   * maintain the certification start date for any associated OAS Benefit PDCs.
   */
  @Override
  public void eventRaised(final Event event)
    throws AppException, InformationalException {

    final CaseHeaderDtlsList integratedCaseHeaderDtlsList =
      BDMOASProductUtility
        .listOpenOASIntegratedCasesForConcernRole(event.primaryEventData);

    for (final CaseHeaderDtls caseHeaderDtls : integratedCaseHeaderDtlsList.dtls) {
      new BDMOASProductManager(caseHeaderDtls).manageProduct();
    }

  }

}
