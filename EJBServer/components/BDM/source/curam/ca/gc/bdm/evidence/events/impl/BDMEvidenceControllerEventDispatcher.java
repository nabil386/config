package curam.ca.gc.bdm.evidence.events.impl;

import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorModifyDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.HashSet;
import java.util.Set;

/**
 * A central dispatcher for Evidence Controller Interface events. Event handlers
 * extend {@link BDMOASAbstractEvidenceEventHandler} and are registered within
 * {@link BDMOASEvidenceControllerEventRegistrar}
 */
public class BDMEvidenceControllerEventDispatcher
  implements EvidenceControllerInterface.EvidencePreActivationEvents,
  EvidenceControllerInterface.EvidenceActivationEvents,
  EvidenceControllerInterface.EvidencePreModifyEvent,
  EvidenceControllerInterface.EvidenceValidationEvents {

  protected Set<BDMAbstractEvidenceEventHandler> handlers;

  public BDMEvidenceControllerEventDispatcher() {

    this.handlers = new BDMEvidenceControllerEventRegistrar().getRegistry();

  }

  @Override
  public void validateEvidence(final EIEvidenceKey key,
    final boolean isReadOperation)
    throws AppException, InformationalException {

    for (final BDMAbstractEvidenceEventHandler handler : this.handlers) {
      if (key.evidenceType.equals(handler.evidenceType().getCode())
        && handler.subscribeValidateEvidence()) {
        handler.validateEvidence(key, isReadOperation);
      }
    }

  }

  @Override
  public void preModify(final EIEvidenceKey key,
    final EvidenceDescriptorModifyDtls descriptor,
    final Object evidenceObject, final EIEvidenceKey parentKey)
    throws AppException, InformationalException {

    for (final BDMAbstractEvidenceEventHandler handler : this.handlers) {
      if (key.evidenceType.equals(handler.evidenceType().getCode())
        && handler.subscribePreModify()) {
        handler.preModify(key, descriptor, evidenceObject, parentKey);
      }
    }

  }

  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey key, final EIEvidenceKeyList list)
    throws AppException, InformationalException {

    final Set<String> evidenceTypeList = new HashSet<String>();

    for (final EIEvidenceKey evidenceKey : list.dtls) {
      evidenceTypeList.add(evidenceKey.evidenceType);
    }

    for (final BDMAbstractEvidenceEventHandler handler : this.handlers) {
      if (evidenceTypeList.contains(handler.evidenceType().getCode())
        && handler.subscribePostActivation()) {
        handler.postActivation(evidenceControllerInterface, key, list);
      }
    }

  }

  @Override
  public void preActivation(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey key, final ApplyChangesEvidenceLists evidenceLists)
    throws AppException, InformationalException {

    final Set<String> evidenceTypeList = new HashSet<>();

    for (final EvidenceKey evidenceKey : evidenceLists.newAndUpdateList.dtls) {
      evidenceTypeList.add(evidenceKey.evidenceType);
    }

    for (final EvidenceKey evidenceKey : evidenceLists.removeList.dtls) {
      evidenceTypeList.add(evidenceKey.evidenceType);
    }

    for (final BDMAbstractEvidenceEventHandler handler : this.handlers) {
      if (evidenceTypeList.contains(handler.evidenceType().getCode())
        && handler.subscribePreActivation()) {
        handler.preActivation(evidenceControllerInterface, key,
          evidenceLists);
      }
    }

  }

}
