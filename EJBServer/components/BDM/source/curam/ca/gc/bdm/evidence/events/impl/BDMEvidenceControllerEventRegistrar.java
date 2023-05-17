package curam.ca.gc.bdm.evidence.events.impl;

import java.util.HashSet;
import java.util.Set;

/**
 * A registrar for event handlers that extend
 * {@link BDMAbstractEvidenceEventHandler}.
 */
public class BDMEvidenceControllerEventRegistrar {

  public Set<BDMAbstractEvidenceEventHandler> getRegistry() {

    final Set<BDMAbstractEvidenceEventHandler> registry = new HashSet<>();

    // Task 67898 DEV: Implement Incarceration Evidence
    registry.add(new BDMIncarcerationEventHandler());

    // 99661 : Volunatary Tax Withhold Evidence
    registry.add(new BDMVoluntaryTaxWithholdEventHandler());

    return registry;

  }

}
