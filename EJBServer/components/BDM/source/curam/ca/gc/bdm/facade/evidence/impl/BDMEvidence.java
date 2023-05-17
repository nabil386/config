package curam.ca.gc.bdm.facade.evidence.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.evidence.struct.BDMCaseKey;
import curam.ca.gc.bdm.facade.evidence.struct.BDMEvdInstanceChangeDtlsList;
import curam.ca.gc.bdm.facade.evidence.struct.BDMEvidenceWarningStruct;
import curam.ca.gc.bdm.facade.evidence.struct.BDMEvidenceWarningStructList;
import curam.ca.gc.bdm.facade.evidence.struct.BDMListAllEvidenceDtls;
import curam.ca.gc.bdm.facade.evidence.struct.BDMSuccessionID;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.codetable.CASEEVIDENCE;
import curam.core.facade.infrastructure.fact.EvidenceFactory;
import curam.core.facade.infrastructure.struct.CaseKey;
import curam.core.facade.infrastructure.struct.EvdInstanceChangeDtls;
import curam.core.facade.infrastructure.struct.EvdInstanceChangeDtlsList;
import curam.core.facade.infrastructure.struct.EvidenceParticipantDtls;
import curam.core.facade.infrastructure.struct.ListAllEvidenceDtls;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.List;

public class BDMEvidence
  extends curam.ca.gc.bdm.facade.evidence.base.BDMEvidence {

  public BDMEvidence() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  private static final String preferredInd = "preferredInd";

  /**
   * Display informational message when user attempts to delete Prefered Phone
   * Number or Email address Evidence
   *
   * @param evidenceDescriptorKey
   * @return BDMEvidenceWarningStructList
   *
   **/
  @Override
  public BDMEvidenceWarningStructList displayWarningForPreferedPhoneAndEmail(
    final EvidenceDescriptorKey evidenceDescriptorKey)
    throws AppException, InformationalException {

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance().read(evidenceDescriptorKey);

    boolean isPhoneEmailPreferred = false;
    boolean isOnlyTaxCredit = false;

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
    eiEvidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

    // START : BUG 4453 JP
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();
    // Check if Email evidence
    if (evidenceDescriptorDtls.evidenceType
      .equals(PDCConst.PDCEMAILADDRESS)) {

      // Check if prefered BUG 4453
      isPhoneEmailPreferred = (Boolean) DynamicEvidenceTypeConverter
        .convert(dynamicEvidenceDataDetails.getAttribute(preferredInd));

      if (isPhoneEmailPreferred) {

        final LocalisableString localisableString = new LocalisableString(
          BDMEVIDENCE.ERR_PREFERED_EMAIL_WILL_BE_DELETED);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kWarning);

      }

    }

    // END : BUG 4453 JP
    // START TASK 5344 : JP
    if (evidenceDescriptorDtls.evidenceType.equals(PDCConst.PDCPHONENUMBER)) {

      // Check if prefered BUG 4453
      isPhoneEmailPreferred = (Boolean) DynamicEvidenceTypeConverter
        .convert(dynamicEvidenceDataDetails.getAttribute(preferredInd));

      if (isPhoneEmailPreferred) {

        final LocalisableString localisableString = new LocalisableString(
          BDMEVIDENCE.ERR_PREFERED_PHONE_WILL_BE_DELETED);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kWarning);

      }
    }

    // END TASK 5344 : JP

    if (evidenceDescriptorDtls.evidenceType.equals(CASEEVIDENCE.BDMTAX)) {

      final List<Long> list =
        BDMEvidenceUtil.retrieveEvidence(evidenceDescriptorDtls.caseID,
          Date.getCurrentDate(), evidenceDescriptorDtls.evidenceType,
          evidenceDescriptorDtls.participantID);

      if (list.size() == 1) {
        isOnlyTaxCredit = true;
        final LocalisableString localisableString =
          new LocalisableString(BDMEVIDENCE.WARNING_ONLY_TAX_CREDIT_EVIDENCE);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kWarning);

      }
    }

    final BDMEvidenceWarningStructList bdmEvidenceWarningStructList =
      new BDMEvidenceWarningStructList();

    if (isPhoneEmailPreferred || isOnlyTaxCredit) {

      final String warnings[] = TransactionInfo.getInformationalManager()
        .obtainInformationalAsString();
      for (int i = 0; i < warnings.length; i++) {
        final BDMEvidenceWarningStruct bdmEvidenceWarningStruct =
          new BDMEvidenceWarningStruct();
        bdmEvidenceWarningStruct.warningMessage = warnings[i];
        bdmEvidenceWarningStructList.dtls.addRef(bdmEvidenceWarningStruct);

      }

    }
    return bdmEvidenceWarningStructList;
  }

  /**
   * Retrieves the list of ALL evidence (active and in edit) associated with the
   * case.
   * There is another version of this method - listAllEvidence1. Whenever there
   * is any change made to this method, developer is requested to visit -
   * listAllEvidence1
   * as well.
   *
   * @param key Contains a case identifier.
   *
   * @return result A list of 'In Edit' and 'Active' evidence for the Case.
   *
   * @param key
   * @return BDMListAllEvidenceDtls
   *
   **/
  @Override
  public BDMListAllEvidenceDtls listAllEvidence(final BDMCaseKey key)
    throws AppException, InformationalException {

    final BDMListAllEvidenceDtls bdmListAllEvidenceDtls =
      new BDMListAllEvidenceDtls();
    final CaseKey caseKey = new CaseKey();
    caseKey.assign(key);

    final ListAllEvidenceDtls listAllEvidenceDtls =
      EvidenceFactory.newInstance().listAllEvidence(caseKey);
    // disable the edit and delete link on the action menu for attestation
    // evidence
    if (listAllEvidenceDtls.caseUpdatesAllowed) {
      for (final EvidenceParticipantDtls evidenceParticipantDtls : listAllEvidenceDtls.evidenceParticipantDtlsList.dtls) {
        if (evidenceParticipantDtls.evidenceType
          .equals(CASEEVIDENCE.BDMATTS)) {
          evidenceParticipantDtls.readOnlyInd = true;
          evidenceParticipantDtls.activeWithSingleInstanceInd = false;
        }
      }
    }
    bdmListAllEvidenceDtls.assign(listAllEvidenceDtls);

    return bdmListAllEvidenceDtls;
  }

  /**
   * Lists all succession evidence changes on an Evidence instance.
   *
   * @boread Evidence
   *
   * @boread EvidenceController
   *
   * @param key Contains a case identifier, participant identifier and evidence
   * type.
   *
   * @return List of evidence instance changes.
   *
   * @throws AppException
   * Generic Exception Signature.
   * @throws InformationalException
   * Generic Exception Signature.
   */

  @Override
  public BDMEvdInstanceChangeDtlsList listEvdInstanceChanges(
    final BDMSuccessionID key) throws AppException, InformationalException {

    final BDMEvdInstanceChangeDtlsList bdmEvdInstanceChangeDtlsList =
      new BDMEvdInstanceChangeDtlsList();
    final curam.core.sl.infrastructure.entity.struct.SuccessionID successionID =
      new curam.core.sl.infrastructure.entity.struct.SuccessionID();
    successionID.assign(key);
    final EvdInstanceChangeDtlsList evdInstanceChangeDtlsList =
      EvidenceFactory.newInstance().listEvdInstanceChanges(successionID);
    // disable the edit and delete link on the action menu for attestation
    // evidence
    if (evdInstanceChangeDtlsList.caseUpdatesAllowed) {
      for (final EvdInstanceChangeDtls evdInstanceChangeDtls : evdInstanceChangeDtlsList.dtlsList) {
        if (evdInstanceChangeDtls.evidenceType.equals(CASEEVIDENCE.BDMATTS)) {
          evdInstanceChangeDtls.activeNotReadOnlyInd = false;
          evdInstanceChangeDtls.activeInd = false;
        }
      }
    }
    bdmEvdInstanceChangeDtlsList.assign(evdInstanceChangeDtlsList);
    return bdmEvdInstanceChangeDtlsList;
  }

}
