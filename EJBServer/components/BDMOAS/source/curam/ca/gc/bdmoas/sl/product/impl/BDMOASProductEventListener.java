package curam.ca.gc.bdmoas.sl.product.impl;

import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.fact.CaseHeaderFactory;
import curam.core.intf.CaseHeader;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * Listens for evidence activation events and invokes
 * {@link BDMOASProductManager#manageProduct()} to create an OAS Benefits PDC if
 * necessary.
 */
public class BDMOASProductEventListener
  extends BDMAbstractEvidenceEventHandler {

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS;

  }

  @Override
  public boolean subscribePostActivation() {

    return true;

  }

  /**
   * If the present case is an OAS IC, calls
   * {@link BDMOASProductManager#manageProduct()} to create an OAS
   * Benefits PDC if necessary.
   */
  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey key, final EIEvidenceKeyList list)
    throws AppException, InformationalException {

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseID;

    final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);

    if (BDMOASProductUtility.isOpenOASIntegratedCase(caseHeaderDtls)) {
      new BDMOASProductManager(caseHeaderDtls).manageProduct();
    }

  }

}
