package curam.ca.gc.bdm.evidence.events.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.ca.gc.bdm.sl.struct.RecordVTWDeductionKey;
import curam.ca.gc.bdm.vtw.deduction.entity.fact.BDMVTWDeductionFactory;
import curam.ca.gc.bdm.vtw.deduction.entity.intf.BDMVTWDeduction;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionCountDetails;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionCountKey;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionDtls;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionDtlsStruct2;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKey;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKeyStruct2;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.fact.CaseHeaderFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDStatusAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorModifyDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;

public class BDMVoluntaryTaxWithholdEventHandler
  extends BDMAbstractEvidenceEventHandler {

  @Inject
  curam.ca.gc.bdm.sl.maintaincasedeductions.intf.MaintainCaseDeductions caseDeductions;

  /**
   * Constructor
   */
  public BDMVoluntaryTaxWithholdEventHandler() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Case Evidence Type
   */
  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.BDMVTW;
  }

  /**
   * Subscribes this handler within the dispatched for evidence post-activation
   * events.
   *
   * @return
   */
  @Override
  public boolean subscribePostActivation() {

    return true;

  }

  /**
   * Subscribes this handler within the dispatched for evidence pre-modify
   * events.
   *
   * @return
   */
  @Override
  public boolean subscribePreModify() {

    return true;

  }

  /**
   * Evidence Post Activation Handler
   */
  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey key, final EIEvidenceKeyList evidenceLists)
    throws AppException, InformationalException {

    final EIEvidenceKeyList filteredEvidenceLists =
      this.filterEvidenceKeyList(evidenceLists);

    for (final EIEvidenceKey evidenceKey : filteredEvidenceLists.dtls
      .items()) {

      if (evidenceKey.evidenceType.equals(CASEEVIDENCE.BDMVTW)) {

        final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
        caseHeaderKey.caseID = key.caseID;
        final String caseTypeCode =
          CaseHeaderFactory.newInstance().read(caseHeaderKey).caseTypeCode;

        if (CASETYPECODE.INTEGRATEDCASE.equals(caseTypeCode)) {

          final RecordVTWDeductionKey recordVTWDeductionKey =
            new RecordVTWDeductionKey();

          final EvidenceControllerInterface evidenceControllerObj =
            (EvidenceControllerInterface) EvidenceControllerFactory
              .newInstance();
          final EIEvidenceReadDtls eiEvidenceReadDtls =
            evidenceControllerObj.readEvidence(evidenceKey);

          final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
            (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

          recordVTWDeductionKey.caseID = key.caseID;
          final String vtwType = dynamicEvidenceDataDetails
            .getAttribute("voluntaryTaxWithholdType").getValue();
          recordVTWDeductionKey.vtwType = vtwType;

          recordVTWDeductionKey.amount = !dynamicEvidenceDataDetails
            .getAttribute("voluntaryTaxWithholdAmount").getValue().isEmpty()
              ? new Money(dynamicEvidenceDataDetails
                .getAttribute("voluntaryTaxWithholdAmount").getValue())
              : new Money(0.0D);

          recordVTWDeductionKey.rate = !dynamicEvidenceDataDetails
            .getAttribute("voluntaryTaxWithholdPercentage").getValue()
            .isEmpty()
              ? Double.valueOf(dynamicEvidenceDataDetails
                .getAttribute("voluntaryTaxWithholdPercentage").getValue())
              : 0.0D;

          recordVTWDeductionKey.startDate = Date.getDate(
            dynamicEvidenceDataDetails.getAttribute("startDate").getValue());

          final String endDate =
            dynamicEvidenceDataDetails.getAttribute("endDate").getValue();
          if (!StringUtil.isNullOrEmpty(endDate)) {
            recordVTWDeductionKey.endDate = Date.getDate(endDate);
          } else {
            recordVTWDeductionKey.endDate = Date.kZeroDate;
          }

          final long concernRoleID = getConcernRoleID(
            eiEvidenceReadDtls.descriptor.evidenceDescriptorID);
          final long vtwDeductionID =
            makeEntryIntoVTWDeductionTable(evidenceKey.evidenceID,
              eiEvidenceReadDtls.descriptor.successionID, concernRoleID,
              recordVTWDeductionKey);

          final BDMVTWDeductionKey vtwDeductionKey = new BDMVTWDeductionKey();
          vtwDeductionKey.vtwDeductionID = vtwDeductionID;
          caseDeductions.recordVTWDeductionByIC(vtwDeductionKey);
        }
      }
    }
  }

  /**
   * This method gets concern role id using evidence descriptor id
   *
   * @param evidenceDescriptorID
   * @return concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  private long getConcernRoleID(final long evidenceDescriptorID)
    throws AppException, InformationalException {

    // Instantiate concern role id.
    long concernRoleID = 0l;

    // Read Evidence Descriptor table and read participant role id.

    final EvidenceDescriptorKey descriptorKey = new EvidenceDescriptorKey();
    descriptorKey.evidenceDescriptorID = evidenceDescriptorID;
    concernRoleID = EvidenceDescriptorFactory.newInstance()
      .read(descriptorKey).participantID;
    // Return concern role id.
    return concernRoleID;
  }

  /**
   * This method either create or update an entry into VTWDeduction database
   * table.
   *
   * @param evidenceID
   * @param successionID
   * @param concernRoleID
   * @param vtwDeductionKey
   * @throws AppException
   * @throws InformationalException
   */
  private long makeEntryIntoVTWDeductionTable(final long evidenceID,
    final long successionID, final long concernRoleID,
    final RecordVTWDeductionKey vtwDeductionKey)
    throws AppException, InformationalException {

    long vtwDeductionID = 0l;
    // Instantiate the variable and assign values.
    final BDMVTWDeductionKeyStruct2 bdmVTWDeductionKeyStruct2 =
      new BDMVTWDeductionKeyStruct2();
    bdmVTWDeductionKeyStruct2.successionID = successionID;
    bdmVTWDeductionKeyStruct2.recordStatusCode = RECORDSTATUS.NORMAL;

    final NotFoundIndicator nfInd = new NotFoundIndicator();
    final BDMVTWDeduction bdmvtwDeduction =
      BDMVTWDeductionFactory.newInstance();

    // Read the VTWDeduction table using succession id and try to find existing
    // record.
    final BDMVTWDeductionDtlsStruct2 dbmVTWDeductionDetails =
      BDMVTWDeductionFactory.newInstance().readBySuccessionID(nfInd,
        bdmVTWDeductionKeyStruct2);

    // If record does not exists then create a new entry into VTWDeduciton
    // table.
    if (nfInd.isNotFound() && successionID != 0) {
      // Instantiate the obejct and assign values to the struct.
      final BDMVTWDeductionDtls bdmVTWDeductionDtls =
        new BDMVTWDeductionDtls();
      bdmVTWDeductionDtls.concernRoleID = concernRoleID;
      bdmVTWDeductionDtls.caseID = vtwDeductionKey.caseID;
      bdmVTWDeductionDtls.amount = vtwDeductionKey.amount;
      if (!vtwDeductionKey.endDate.isZero()) {
        bdmVTWDeductionDtls.endDate = vtwDeductionKey.endDate;
      }
      bdmVTWDeductionDtls.startDate = vtwDeductionKey.startDate;
      bdmVTWDeductionDtls.vtwType = vtwDeductionKey.vtwType;
      bdmVTWDeductionDtls.rate = vtwDeductionKey.rate;
      bdmVTWDeductionDtls.recordStatusCode = RECORDSTATUS.NORMAL;
      bdmVTWDeductionDtls.evidenceID = evidenceID;
      bdmVTWDeductionDtls.successionID = successionID;
      bdmVTWDeductionDtls.vtwDeductionID = UniqueID.nextUniqueID();
      bdmVTWDeductionDtls.versionNo = 1;
      // Invoke insert operation and make an entry in to VTWDeduction table.
      bdmvtwDeduction.insert(bdmVTWDeductionDtls);
      vtwDeductionID = bdmVTWDeductionDtls.vtwDeductionID;
    }
    // If entry exists then update the existing entry with new values.
    else if (successionID != 0) {
      // Instantiate the obejct and assign values to the struct.
      final BDMVTWDeductionKey bdmvtwDeductionKey = new BDMVTWDeductionKey();
      bdmvtwDeductionKey.vtwDeductionID =
        dbmVTWDeductionDetails.vtwDeductionID;

      final BDMVTWDeductionDtls bdmvtwDeductionDtls =
        bdmvtwDeduction.read(bdmvtwDeductionKey);
      bdmvtwDeductionDtls.amount = vtwDeductionKey.amount;
      if (!vtwDeductionKey.endDate.isZero()) {
        bdmvtwDeductionDtls.endDate = vtwDeductionKey.endDate;
      }
      bdmvtwDeductionDtls.startDate = vtwDeductionKey.startDate;
      bdmvtwDeductionDtls.vtwType = vtwDeductionKey.vtwType;
      bdmvtwDeductionDtls.rate = vtwDeductionKey.rate;
      bdmvtwDeductionDtls.evidenceID = evidenceID;

      // Invoke modify operation and update an existing entry.
      bdmvtwDeduction.modify(bdmvtwDeductionKey, bdmvtwDeductionDtls);
      vtwDeductionID = bdmvtwDeductionDtls.vtwDeductionID;
    }
    return vtwDeductionID;
  }

  /**
   * Evidence Pre Modify Handler
   */
  @Override
  public void preModify(final EIEvidenceKey key,
    final EvidenceDescriptorModifyDtls descriptor,
    final Object evidenceObject, final EIEvidenceKey parentKey)
    throws AppException, InformationalException {

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final RelatedIDAndEvidenceTypeKey descriptorKey =
      new RelatedIDAndEvidenceTypeKey();
    descriptorKey.evidenceType = key.evidenceType;
    descriptorKey.relatedID = key.evidenceID;
    final EvidenceDescriptorDtls descriptorDtls =
      evidenceDescriptorObj.readByRelatedIDAndType(descriptorKey);

    validateModifyVTWEvidence(descriptorDtls.caseID, key.evidenceType,
      key.evidenceID, descriptorDtls.correctionSetID, evidenceObject);

  }

  /**
   * Validates that start date is not changed for a vtw evidence once a
   * deduction has been processed
   *
   * @param caseID
   * @param evidenceType
   * @param evidenceID
   */
  private void validateModifyVTWEvidence(final long caseID,
    final String evidenceType, final long evidenceID,
    final String correctionSetID, final Object evidenceObject)
    throws AppException, InformationalException {

    if (evidenceType.equals(CASEEVIDENCE.BDMVTW) && isICCase(caseID)) {

      // get the current active VTW evidence
      final CaseIDStatusAndEvidenceTypeKey caseIDStatusAndEvidenceTypeKey =
        new CaseIDStatusAndEvidenceTypeKey();

      caseIDStatusAndEvidenceTypeKey.caseID = caseID;
      caseIDStatusAndEvidenceTypeKey.evidenceType = evidenceType;
      caseIDStatusAndEvidenceTypeKey.statusCode =
        EVIDENCEDESCRIPTORSTATUS.ACTIVE;

      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();

      final RelatedIDAndEvidenceTypeKeyList currentActiveEvidenceList =
        evidenceDescriptorObj
          .searchByCaseIDTypeAndStatus(caseIDStatusAndEvidenceTypeKey);

      // if this is an add activation, there will be no current active
      // evidence that matches the correction set ID

      final EvidenceControllerInterface evidenceController =
        (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

      EIEvidenceReadDtls currentEvidence = null;
      long currentEvidenceID = 0;

      final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
      // iterate through all active evidences
      for (final RelatedIDAndEvidenceTypeKey activeEvidence : currentActiveEvidenceList.dtls) {
        // get the current evidence details
        eiEvidenceKey.evidenceID = activeEvidence.relatedID;

        eiEvidenceKey.evidenceType = evidenceType;
        final EIEvidenceReadDtls activeEvidenceDtls =
          evidenceController.readEvidence(eiEvidenceKey);

        // check correction set ID matches
        if (activeEvidenceDtls.descriptor.correctionSetID
          .equals(correctionSetID)) {
          currentEvidence = activeEvidenceDtls;
          currentEvidenceID = activeEvidence.relatedID;
          break;
        }
      }

      if (currentEvidence != null) {

        final DynamicEvidenceDataDetails currentDtls =
          (DynamicEvidenceDataDetails) currentEvidence.evidenceObject;

        final String currentStartDate = currentDtls
          .getAttribute(BDMConstants.kEvidenceAttrVTWStartDate).getValue();

        final String percentageToBeWithheld = currentDtls
          .getAttribute("voluntaryTaxWithholdPercentage").getValue();

        final String dollarAmount =
          currentDtls.getAttribute("voluntaryTaxWithholdAmount").getValue();

        // count ILIs related to the original active evidence
        final BDMVTWDeductionCountKey countKey =
          new BDMVTWDeductionCountKey();
        countKey.evidenceID = currentEvidenceID;
        final BDMVTWDeductionCountDetails countDetails =
          BDMVTWDeductionFactory.newInstance()
            .countVTWILIByEvidenceID(countKey);

        if (countDetails.numberOfRecords > 0) {

          final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
            (DynamicEvidenceDataDetails) evidenceObject;

          final Date startDate = Date.getDate(
            dynamicEvidenceDataDetails.getAttribute("startDate").getValue());

          final Money newDollarAmount = !dynamicEvidenceDataDetails
            .getAttribute("voluntaryTaxWithholdAmount").getValue().isEmpty()
              ? new Money(dynamicEvidenceDataDetails
                .getAttribute("voluntaryTaxWithholdAmount").getValue())
              : new Money(0.0D);

          final Double newVTWPercenatge = !dynamicEvidenceDataDetails
            .getAttribute("voluntaryTaxWithholdPercentage").getValue()
            .isEmpty()
              ? new Double(dynamicEvidenceDataDetails
                .getAttribute("voluntaryTaxWithholdPercentage").getValue())
              : new Double(0.0D);

          // if the start date has been changed, fail activation
          if (!Date.getDate(currentStartDate).equals(startDate)) {

            throw new AppException(
              BDMEVIDENCE.ERR_START_DATE_MODIFY_VTW_EVIDENCE);
          }

          if (!new Money(dollarAmount).equals(newDollarAmount)) {

            throw new AppException(
              BDMEVIDENCE.ERR_DOLLAR_AMOUNT_WITHHELD_MODIFY_VTW_EVIDENCE);
          }

          if (!new Double(percentageToBeWithheld).equals(newVTWPercenatge)) {

            throw new AppException(
              BDMEVIDENCE.ERR_PERCENTAGE_WITHHELD_MODIFY_VTW_EVIDENCE);
          }
        }
      }
    }
  }

  /**
   * This method verifies whether it is IC case or not.
   *
   * @param caseID
   * @return boolean
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isICCase(final long caseID)
    throws AppException, InformationalException {

    // Instantiate objects and assign values.
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    boolean result = false;
    caseHeaderKey.caseID = caseID;

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    // Read Case Header table and try to read the case type code.
    final CaseHeaderDtls caseHeaderDtls =
      CaseHeaderFactory.newInstance().read(notFoundIndicator, caseHeaderKey);

    if (!notFoundIndicator.isNotFound()
      && CASETYPECODE.INTEGRATEDCASE.equals(caseHeaderDtls.caseTypeCode)) {

      result = true;
    }

    // Return boolean result.
    return result;
  }
}
