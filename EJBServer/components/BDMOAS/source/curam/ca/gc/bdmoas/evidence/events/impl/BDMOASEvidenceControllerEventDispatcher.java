package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMEvidenceControllerEventDispatcher;

/**
 * A central dispatcher for Evidence Controller Interface events. Event handlers
 * extend {@link BDMOASAbstractEvidenceEventHandler} and are registered within
 * {@link BDMOASEvidenceControllerEventRegistrar}
 */
public class BDMOASEvidenceControllerEventDispatcher
  extends BDMEvidenceControllerEventDispatcher {

  public BDMOASEvidenceControllerEventDispatcher() {

    this.handlers =
      new BDMOASEvidenceControllerEventRegistrar().getRegistry();

  }

}
