package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdmoas.sl.product.impl.BDMOASProductEventListener;
import java.util.HashSet;
import java.util.Set;

/**
 * A registrar for event handlers that extend
 * {@link BDMOASAbstractEvidenceEventHandler}.
 */
public class BDMOASEvidenceControllerEventRegistrar {

  public Set<BDMAbstractEvidenceEventHandler> getRegistry() {

    final Set<BDMAbstractEvidenceEventHandler> registry = new HashSet<>();

    // Task 61872 DEV: Implement Sponsorship Evidence
    registry.add(new BDMOASSponsorshipEventHandler());

    // 64790: Residence Period
    registry.add(new BDMOASResidencePeriodEventHandler());

    // 76342: Application Life Cycle Evidence
    registry.add(new BDMOASApplicationDetailsEventHandler());

    // 76342: Application Life Cycle Evidence
    registry.add(new BDMOASBenefitCancellationRequestEventHandler());

    // 86193 : Marital Relationship and Living Apart for Reasons Beyond Control
    // Evidence
    registry.add(new BDMOASMaritalRelationshipEventHandler());

    // 95630 : DEV: Implement Manage Override Evidence - R2
    registry.add(new BDMOASEligibilityEntitlementOverrideEventHandler());

    // Feature 99651: Manage OAS RCV Tax
    registry.add(new BDMOASRecoveryTaxEventHandler());

    // 104301: DEV: Implement Manage Consent and Declaration Evidence - R2
    registry.add(new BDMOASConsentDeclarationEventHandler());

    // Feature 99661 : Volunatary Tax Withhold Evidence
    registry.add(new BDMOASVoluntaryTaxWithholdEventHandler());

    // Feature 106244 : NRT Correction Evidence
    registry.add(new BDMOASNRTCorrectionEventHandler());

    // Feature 106244 : NRT Correction Evidence
    registry.add(new BDMOASAddressEventHandler());

    // 116400: DEV: Implement Manage Creditable Periods for OAS under IA - R2
    registry.add(new BDMOASForeignResidencePeriodEventHandler());

    registry.add(new BDMOASProductEventListener());

    return registry;

  }

}
