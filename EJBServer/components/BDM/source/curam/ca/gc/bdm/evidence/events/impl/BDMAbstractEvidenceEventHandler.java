package curam.ca.gc.bdm.evidence.events.impl;

import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorModifyDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * The contract for Evidence Controller event handlers which may be invoked by
 * {@link BDMOASEvidenceControllerEventDispatcher}. Handlers are published to
 * the dispatcher by adding an instance in
 * {@link BDMOASEvidenceControllerEventRegistrar#getRegistry()}.
 *
 * In addition to implementing the available event handler methods,
 * implementations must advertise their interest in each implemented event to
 * the dispatcher by returning true from the associated subscribe method.
 *
 */
public abstract class BDMAbstractEvidenceEventHandler
  implements EvidenceControllerInterface.EvidencePreActivationEvents,
  EvidenceControllerInterface.EvidenceActivationEvents,
  EvidenceControllerInterface.EvidencePreModifyEvent,
  EvidenceControllerInterface.EvidenceValidationEvents {

  /**
   * Advertises the evidence type which this handler handles.
   *
   * @return
   */
  public abstract CASEEVIDENCEEntry evidenceType();

  /**
   * Subscribes this handler within the dispatched for validate evidence events.
   *
   * @return
   */
  public boolean subscribeValidateEvidence() {

    return false;

  }

  /**
   * Subscribes this handler within the dispatched for evidence pre-activation
   * events.
   *
   * @return
   */
  public boolean subscribePreActivation() {

    return false;

  }

  /**
   * Subscribes this handler within the dispatched for evidence post-activation
   * events.
   *
   * @return
   */
  public boolean subscribePostActivation() {

    return false;

  }

  /**
   * Subscribes this handler within the dispatched for evidence pre-modify
   * events.
   *
   * @return
   */
  public boolean subscribePreModify() {

    return false;

  }

  @Override
  public void validateEvidence(final EIEvidenceKey key,
    final boolean isReadOperation)
    throws AppException, InformationalException {

  }

  @Override
  public void preModify(final EIEvidenceKey key,
    final EvidenceDescriptorModifyDtls descriptor,
    final Object evidenceObject, final EIEvidenceKey parentKey)
    throws AppException, InformationalException {

  }

  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey key, final EIEvidenceKeyList list)
    throws AppException, InformationalException {

  }

  @Override
  public void preActivation(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey key, final ApplyChangesEvidenceLists evidenceLists)
    throws AppException, InformationalException {

  }

  /**
   * Returns an {@link ApplyChangesEvidenceLists} object that only contains
   * evidences of the type returned by {@link #evidenceType()}
   *
   * @param evidenceLists
   * @return
   */
  protected final ApplyChangesEvidenceLists
    filterEvidenceLists(final ApplyChangesEvidenceLists evidenceLists) {

    final ApplyChangesEvidenceLists filteredLists =
      new ApplyChangesEvidenceLists();

    for (final EvidenceKey key : evidenceLists.newAndUpdateList.dtls) {
      if (this.evidenceType().getCode().equals(key.evidenceType)) {
        filteredLists.newAndUpdateList.dtls.add(key);
      }
    }

    for (final EvidenceKey key : evidenceLists.removeList.dtls) {
      if (this.evidenceType().getCode().equals(key.evidenceType)) {
        filteredLists.removeList.dtls.add(key);
      }
    }

    return filteredLists;

  }

  /**
   * Returns an {@link EIEvidenceKeyList} object that only contains
   * evidences of the type returned by {@link #evidenceType()}
   *
   * @param evidenceKeyList
   * @return
   */
  protected final EIEvidenceKeyList
    filterEvidenceKeyList(final EIEvidenceKeyList evidenceKeyList) {

    final EIEvidenceKeyList filteredList = new EIEvidenceKeyList();

    for (final EIEvidenceKey evidenceKey : evidenceKeyList.dtls) {
      if (this.evidenceType().getCode().equals(evidenceKey.evidenceType)) {
        filteredList.dtls.add(evidenceKey);
      }
    }

    return filteredList;

  }

}
