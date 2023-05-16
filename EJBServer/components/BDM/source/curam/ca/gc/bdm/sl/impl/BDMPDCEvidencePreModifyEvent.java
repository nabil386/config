package curam.ca.gc.bdm.sl.impl;

import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorModifyDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.sl.entity.fact.DynamicEvidenceDataAttributeFactory;
import curam.dynamicevidence.sl.entity.intf.DynamicEvidenceDataAttribute;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataIDAndAttrKey;
import curam.dynamicevidence.sl.entity.struct.EvidenceIDDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;

/**
 *
 * @author teja.konda
 * Event listner class for Premodify method
 *
 */
public class BDMPDCEvidencePreModifyEvent
  implements EvidenceControllerInterface.EvidencePreModifyEvent {

  private final String preferredCommunication = "preferredCommunication";

  public static final String kAltID = "alternateID";

  public static final String kAltIDType = "altIDType";

  /**
   *
   * @param key
   * @param modifyDtls
   * @param newEvidenceObject
   * @param parentKey
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void preModify(final EIEvidenceKey key,
    final EvidenceDescriptorModifyDtls modifyDtls,
    final Object newEvidenceObject, final EIEvidenceKey parentKey)
    throws AppException, InformationalException {

    if (key.evidenceType.equals(PDCConst.PDCCONTACTPREFERENCES)) {

      final DynamicEvidenceDataAttribute dynamicEvidence =
        DynamicEvidenceDataAttributeFactory.newInstance();
      final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();
      evidenceIDDetails.evidenceID = key.evidenceID;

      final DynamicEvidenceDataAttributeDtlsList attributeList =
        dynamicEvidence.searchByEvidenceID(evidenceIDDetails);

      final String previousCommMethod = attributeList.dtls.stream()
        .filter(attribute -> attribute.name.equals(preferredCommunication))
        .findFirst().get().value;

      final String newCommMethod =
        getAttributeValue((DynamicEvidenceDataDetails) newEvidenceObject,
          preferredCommunication);

      if (!previousCommMethod.equalsIgnoreCase(newCommMethod)) {

        final AppException appexception =
          new AppException(BDMEVIDENCE.ERR_CONTACT_PREFERENCE_COMM_MODIFY);

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          appexception, "a", InformationalElement.InformationalType.kWarning);

      }

    }
    // START TASK: 14757 - VMAIDCHETTY
    else if (key.evidenceType.equals(PDCConst.PDCIDENTIFICATION)) {

      final DynamicEvidenceDataAttribute dynamicEvidence =
        DynamicEvidenceDataAttributeFactory.newInstance();
      final EvidenceIDDetails evidenceIDDetails = new EvidenceIDDetails();
      evidenceIDDetails.evidenceID = key.evidenceID;

      final DynamicEvidenceDataAttributeDtlsList attributeList =
        dynamicEvidence.searchByEvidenceID(evidenceIDDetails);

      final String previousCommMethod = attributeList.dtls.stream()
        .filter(attribute -> attribute.name.equals(kAltID)).findFirst()
        .get().value;

      final String newCommMethod = getAttributeValue(
        (DynamicEvidenceDataDetails) newEvidenceObject, kAltID);

      final DynamicEvidenceDataIDAndAttrKey attrKey =
        new DynamicEvidenceDataIDAndAttrKey();
      attrKey.evidenceID = key.evidenceID;
      attrKey.name = kAltIDType;
      final DynamicEvidenceDataAttributeDtlsList obj =
        DynamicEvidenceDataAttributeFactory.newInstance()
          .searchByEvidenceIDAndAttribute(attrKey);
      for (final DynamicEvidenceDataAttributeDtls list : obj.dtls) {

        if (list.value.equals(CONCERNROLEALTERNATEID.REFERENCE_NUMBER)
          && !previousCommMethod.equalsIgnoreCase(newCommMethod)) {
          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(BDMEVIDENCE.ERR_REF_NUM_MODIFIED), null,
            InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
            0);

          TransactionInfo.getInformationalManager().failOperation();

        }
      }
    }
    // END TASK : 14757
    else if (key.evidenceType.equals(PDCConst.PDCBIRTHANDDEATH)) {
      boolean result = false;

      if (TransactionInfo
        .getFacadeScopeObject(BDMConstants.IS_MERGE_PROSPECT) != null) {
        result = (Boolean) TransactionInfo
          .getFacadeScopeObject(BDMConstants.IS_MERGE_PROSPECT);
      }
      if (!result) {
        if (EVIDENCECHANGEREASON.REPORTEDBYEXTERNALPARTY
          .equals(modifyDtls.changeReason)
          || EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT
            .equals(modifyDtls.changeReason)
          || EVIDENCECHANGEREASON.REPORTEDBYCLIENT
            .equals(modifyDtls.changeReason)) {

          final String bdmAgreeAttestation =
            getAttributeValue((DynamicEvidenceDataDetails) newEvidenceObject,
              "bdmAgreeAttestation");
          final String attestee =
            getAttributeValue((DynamicEvidenceDataDetails) newEvidenceObject,
              "attesteeCaseParticipant");
          if (StringUtil.isNullOrEmpty(bdmAgreeAttestation)
            || !BDMAGREEATTESTATION.YES.equals(bdmAgreeAttestation)) {
            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(
                new AppException(
                  BDMEVIDENCE.ERR_ATTESTATION_CHECKBOX_MANDATORY),
                null, InformationalType.kError,
                curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
                0);
            TransactionInfo.getInformationalManager().failOperation();
          } else if (StringUtil.isNullOrEmpty(attestee)) {
            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(
                new AppException(BDMEVIDENCE.ERR_ATTESTEE_IS_MANDATORY), null,
                InformationalType.kError,
                curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
                0);
            TransactionInfo.getInformationalManager().failOperation();
          } else {
            DynamicEvidenceTypeConverter
              .setAttribute(((DynamicEvidenceDataDetails) newEvidenceObject)
                .getAttribute("attestationDate"), Date.getCurrentDate());
          }

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
