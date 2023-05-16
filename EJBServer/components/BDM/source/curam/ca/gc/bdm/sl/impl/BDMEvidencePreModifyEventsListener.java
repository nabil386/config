package curam.ca.gc.bdm.sl.impl;

import curam.advisor.entity.fact.AdviceCaseIssueFactory;
import curam.advisor.entity.intf.AdviceCaseIssue;
import curam.advisor.entity.struct.AdviceCaseIssueDtlsList;
import curam.ca.gc.bdm.message.impl.BDMEVIDENCEExceptionCreator;
import curam.codetable.CASEEVIDENCE;
import curam.core.facade.infrastructure.struct.CaseIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorModifyDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.sl.entity.struct.EvidenceIDDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;

public class BDMEvidencePreModifyEventsListener
  implements EvidenceControllerInterface.EvidencePreModifyEvent {

  private static final String CANADIAN_RESIDENCE_PERIOD_RESOLUTION_ATTRIBUTE =
    "resolution";

  @Override
  public void preModify(final EIEvidenceKey key,
    final EvidenceDescriptorModifyDtls descriptor,
    final Object evidenceObject, final EIEvidenceKey parentKey)
    throws AppException, InformationalException {

    if (key.evidenceType.equals(CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD)) {
      final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();
      evidenceIDDetails.evidenceID = key.evidenceID;

      final String resolution =
        getAttributeValue((DynamicEvidenceDataDetails) evidenceObject,
          CANADIAN_RESIDENCE_PERIOD_RESOLUTION_ATTRIBUTE);

      if (!StringUtil.isNullOrEmpty(resolution)) {

        final EvidenceDescriptor evidenceDescriptorObj =
          EvidenceDescriptorFactory.newInstance();
        final RelatedIDAndEvidenceTypeKey descriptorKey =
          new RelatedIDAndEvidenceTypeKey();
        descriptorKey.evidenceType = key.evidenceType;
        descriptorKey.relatedID = key.evidenceID;
        final EvidenceDescriptorDtls descriptorDtls =
          evidenceDescriptorObj.readByRelatedIDAndType(descriptorKey);

        final AdviceCaseIssue adviceCaseIssueObj =
          AdviceCaseIssueFactory.newInstance();

        final CaseIDAndEvidenceTypeKey adviceCaseIssueKey =
          new CaseIDAndEvidenceTypeKey();
        adviceCaseIssueKey.caseID = descriptorDtls.caseID;
        adviceCaseIssueKey.evidenceType = key.evidenceType;
        final AdviceCaseIssueDtlsList adviceCaseIssueDtls = adviceCaseIssueObj
          .searchByCaseIDAndEvidenceType(adviceCaseIssueKey);

        final Boolean issueExistsForEvidenceID = adviceCaseIssueDtls.dtls
          .stream().anyMatch(x -> x.evidenceID == key.evidenceID);

        if (!issueExistsForEvidenceID) {
          throw BDMEVIDENCEExceptionCreator.ERR_NO_ASSOCIATED_ISSUE();
        }
      }
    }

  }

  /**
   *
   * @param newEvidenceObject
   * @param attributeName
   * @return
   */
  private String getAttributeValue(
    final DynamicEvidenceDataDetails newEvidenceObject,
    final String attributeName) {

    final DynamicEvidenceDataAttributeDetails attribute =
      newEvidenceObject.getAttribute(attributeName);
    String value = "";

    if (attribute != null) {

      value = attribute.getValue();
    }

    return value;
  }

}
