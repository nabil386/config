package curam.ca.gc.bdm.sl.pdcprospectperson.impl;

import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.facade.pdcprospectperson.struct.BDMContactPreferenceDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.core.facade.struct.ConcernRoleIDKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 *
 * @author hamed.mohammed
 *
 */
public class BDMMaintainPDCProspectPerson extends
  curam.ca.gc.bdm.sl.pdcprospectperson.base.BDMMaintainPDCProspectPerson {

  /**
   * Bug 103166: Fields Preferred Language of Written and Oral Communication
   * should be mandatory and pre-populated with the corresponding values
   */
  @Override
  public BDMContactPreferenceDetails readContactPreferencesEvidence(
    ConcernRoleIDKey key) throws AppException, InformationalException {

    BDMContactPreferenceDetails bdmContactPreferenceDetails =
      new BDMContactPreferenceDetails();

    BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();

    DynamicEvidenceDataAttributeDtlsList dynamicEvidenceDataAttributeDtlsList =
      new DynamicEvidenceDataAttributeDtlsList();

    final EvidenceDescriptorDtls evidenceDescriptorDtlsKey =
      new EvidenceDescriptorDtls();
    evidenceDescriptorDtlsKey.participantID = key.concernRoleID;
    evidenceDescriptorDtlsKey.evidenceType = PDCConst.PDCCONTACTPREFERENCES;
    evidenceDescriptorDtlsKey.statusCode = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    dynamicEvidenceDataAttributeDtlsList =
      bdmfeCase.getContactPreferencesEvidenceList(evidenceDescriptorDtlsKey);

    DynamicEvidenceDataAttributeDtls dynamicEvidenceDataAttributeDtls = null;

    final int listSize = dynamicEvidenceDataAttributeDtlsList.dtls.size();

    if (listSize > 0) {

      for (int i = 0; i < listSize; i++) {
        dynamicEvidenceDataAttributeDtls =
          new DynamicEvidenceDataAttributeDtls();

        dynamicEvidenceDataAttributeDtls =
          dynamicEvidenceDataAttributeDtlsList.dtls.item(i);

        if (dynamicEvidenceDataAttributeDtls.name
          .equals(BDMConstants.kpreferredOralLanguage)) {

          bdmContactPreferenceDetails.preferredOralLang =
            dynamicEvidenceDataAttributeDtls.value;

        } else if (dynamicEvidenceDataAttributeDtls.name
          .equals(BDMConstants.kpreferredWrittenLanguage)) {

          bdmContactPreferenceDetails.preferredWrittenLang =
            dynamicEvidenceDataAttributeDtls.value;

        }

      }

    }

    return bdmContactPreferenceDetails;
  }

}
