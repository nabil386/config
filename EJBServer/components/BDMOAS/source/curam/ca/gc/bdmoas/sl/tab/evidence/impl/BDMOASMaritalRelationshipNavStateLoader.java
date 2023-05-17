package curam.ca.gc.bdmoas.sl.tab.evidence.impl;

import curam.codetable.CASEEVIDENCE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.ChildKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceRelationshipFactory;
import curam.core.sl.infrastructure.intf.EvidenceRelationship;
import curam.core.sl.infrastructure.struct.ChildList;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.tab.impl.DynamicNavStateLoader;
import curam.util.tab.impl.NavigationState;
import java.util.HashMap;
import java.util.Map;

public class BDMOASMaritalRelationshipNavStateLoader
  implements DynamicNavStateLoader {

  private static final String EVIDENCE_ID_PARAM = "evidenceID";

  private static final String LIVING_APART_FOR_REASONS_BEYOND_CONTROL_NAV_ID =
    "RelatedEvidenceOASDET0011";

  private static final Map<String, String> EVIDENCE_TYPE_NAV_ID_MAP =
    new HashMap<>();

  static {
    EVIDENCE_TYPE_NAV_ID_MAP.put(
      CASEEVIDENCE.OAS_LIVING_APART_FOR_REASONS_BEYOND_CONTROL,
      LIVING_APART_FOR_REASONS_BEYOND_CONTROL_NAV_ID);
  }

  @Override
  public NavigationState loadNavState(final NavigationState navState,
    final Map<String, String> pageParameters, final String[] idsToUpdate)
    throws AppException, InformationalException {

    final long evidenceID =
      Long.valueOf(pageParameters.get(EVIDENCE_ID_PARAM));

    final EvidenceRelationship evidenceRelationship =
      EvidenceRelationshipFactory.newInstance();
    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceID;
    evidenceKey.evidenceType = CASEEVIDENCE.OAS_MARITAL_RELATIONSHIP;

    final ChildList childList =
      evidenceRelationship.getChildKeyList(evidenceKey);

    for (final ChildKey childKey : childList.list.dtls) {

      if (EVIDENCE_TYPE_NAV_ID_MAP.containsKey(childKey.childType)) {
        this.maintainNavItemVisibility(childKey, navState);
      }

    }

    return navState;

  }

  private final void maintainNavItemVisibility(final ChildKey childKey,
    final NavigationState navState)
    throws AppException, InformationalException {

    final EvidenceDescriptor descriptor =
      EvidenceDescriptorFactory.newInstance();
    final RelatedIDAndEvidenceTypeKey relatedIDKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDKey.evidenceType = childKey.childType;
    relatedIDKey.relatedID = childKey.childID;
    final EvidenceDescriptorDtls evidenceDtls =
      descriptor.readByRelatedIDAndType(relatedIDKey);

    if (EVIDENCEDESCRIPTORSTATUS.ACTIVE.equals(evidenceDtls.statusCode)
      || EVIDENCEDESCRIPTORSTATUS.INEDIT.equals(evidenceDtls.statusCode)) {

      navState.setVisible(true,
        EVIDENCE_TYPE_NAV_ID_MAP.get(evidenceDtls.evidenceType));

    }

  }

}
