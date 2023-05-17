package curam.ca.gc.bdmoas.sl.tab.evidence.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.CaseHeader;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.tab.impl.DynamicMenuStateLoader;
import curam.util.tab.impl.MenuState;
import java.util.Map;

public class BDMOASApplicationDetailsMenuStateLoader
  implements DynamicMenuStateLoader {

  private static final String EVIDENCE_ID = "evidenceID";

  private static final String BENEFIT_CANCELLATION = "CreateChildOASDET0007";

  private static final String DEEMED_DATE = "CreateChildOASDET0009";

  @Override
  public MenuState loadMenuState(final MenuState menu,
    final Map<String, String> pageParameters, final String[] idsToUpdate)
    throws AppException, InformationalException {

    final String caseIDParameter =
      pageParameters.get(CuramConst.gkCaseIDParameter);
    final long caseID = Long.valueOf(caseIDParameter);

    final CaseHeader caseHeader = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseID;
    final CaseHeaderDtls caseHeaderDtls = caseHeader.read(caseHeaderKey);

    if (CASETYPECODE.APPLICATION_CASE.equals(caseHeaderDtls.caseTypeCode)) {
      this.maintainMenuForApplicationCase(menu, pageParameters, idsToUpdate);
    }

    if (CASETYPECODE.INTEGRATEDCASE.equals(caseHeaderDtls.caseTypeCode)) {
      this.maintainMenuForIntegratedCase(menu, pageParameters, idsToUpdate);
    }

    return menu;

  }

  private void maintainMenuForApplicationCase(final MenuState menu,
    final Map<String, String> pageParameters, final String[] idsToUpdate) {

    for (final String id : idsToUpdate) {
      if (!id.equals(DEEMED_DATE)) {
        menu.setEnabled(false, id);
        menu.setVisible(false, id);
      }
    }

  }

  private void maintainMenuForIntegratedCase(final MenuState menu,
    final Map<String, String> pageParameters, final String[] idsToUpdate)
    throws AppException, InformationalException {

    final long evidenceID = Long.valueOf(pageParameters.get(EVIDENCE_ID));

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceKey readEvidenceKey = new EIEvidenceKey();
    readEvidenceKey.evidenceID = evidenceID;
    readEvidenceKey.evidenceType = CASEEVIDENCE.OAS_APPLICATION_DETAILS;

    final EIEvidenceReadDtls evidenceReadDtls =
      evidenceController.readEvidence(readEvidenceKey);

    final DynamicEvidenceDataDetails evidence =
      (DynamicEvidenceDataDetails) evidenceReadDtls.evidenceObject;

    final String benefitType =
      evidence.getAttribute(BDMOASApplicationDetailsConstants.BENEFIT_TYPE)
        .getValue();

    if (BDMOASBENEFITTYPE.ALLOWANCE.equals(benefitType)) {
      menu.setVisible(false, BENEFIT_CANCELLATION);
    }

  }

}
