package curam.ca.gc.bdmoas.evidence.events.impl;

import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl.MaintainParticipantDeduction;
import curam.ca.gc.bdmoas.deductions.impl.BDMOASMaintainDeduction;
import curam.ca.gc.bdmoas.entity.oasdeduction.fact.BDMOASDeductionFactory;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionCountDetails;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionCountKey;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionDetails;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionDtls;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionEvidenceIDRecordStatus;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionKey;
import curam.ca.gc.bdmoas.message.BDMOASEVIDENCEMESSAGE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.PRODUCTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorModifyDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.ApplyChangesEvidenceLists;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.sl.infrastructure.struct.EvidenceKey;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.IndicatorStruct;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.ProductDeliveryTypeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;

public class BDMOASRecoveryTaxEventHandler
  extends BDMAbstractEvidenceEventHandler {

  /**
   * Constructor
   */
  public BDMOASRecoveryTaxEventHandler() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.OAS_RECOVERY_TAX;
  }

  /**
   * Subscribe the PreModify event
   */
  @Override
  public boolean subscribePreModify() {

    return true;
  }

  /**
   * Subscribe the Post Activation Event
   */
  @Override
  public boolean subscribePostActivation() {

    return true;
  }

  /**
   * Subscribe the PreActivation Event
   */
  @Override
  public boolean subscribePreActivation() {

    return true;
  }

  /**
   * This method will be called during the post activation of the evidence.
   * Post activation OAS Recovery Tax evidence the record is inserted into the
   * BDMOASDeduction table and subsequently operations are called to make the
   * entry into the OOTB methods to record the deduction.
   * Once the Deduction is recorded in the OOTB CaseDeductionItem entity, the
   * caseDeductionItemID is record against the BDMOASDeduction record to connect
   * the evidence record and deduction record.
   *
   * @param evidenceControllerInterface
   * Evidence Controller Interface
   *
   * @param key
   * Case Key
   *
   * @param evidenceLists
   * List of evidences for the activation.
   *
   * @throws AppException
   * Application Exception
   *
   * @throws InformationalException
   * Informational Exception
   *
   */
  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey caseKey, final EIEvidenceKeyList evidenceLists)
    throws AppException, InformationalException {

    final EIEvidenceKeyList filteredEvidenceLists =
      this.filterEvidenceKeyList(evidenceLists);

    for (final EIEvidenceKey evidenceKey : filteredEvidenceLists.dtls
      .items()) {

      if (evidenceKey.evidenceType.equals(CASEEVIDENCE.OAS_RECOVERY_TAX)) {

        final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
        caseHeaderKey.caseID = caseKey.caseID;
        final CaseHeaderDtls caseHeaderDtls =
          CaseHeaderFactory.newInstance().read(caseHeaderKey);

        if (PRODUCTCATEGORY.OAS_OLD_AGE_SECURITY
          .equals(caseHeaderDtls.integratedCaseType)) {

          final EvidenceControllerInterface evidenceControllerObj =
            (EvidenceControllerInterface) EvidenceControllerFactory
              .newInstance();

          final EIEvidenceReadDtls eiEvidenceReadDtls =
            evidenceControllerObj.readEvidence(evidenceKey);

          final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
            (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

          final BDMOASDeductionDetails bdmOASDeductionDetails =
            new BDMOASDeductionDetails();

          bdmOASDeductionDetails.amount = new Money(dynamicEvidenceDataDetails
            .getAttribute("monthlyRecoveryTaxAmount").getValue());

          bdmOASDeductionDetails.startDate = Date.getDate(
            dynamicEvidenceDataDetails.getAttribute("startDate").getValue());

          // End Date Can not be empty date
          bdmOASDeductionDetails.endDate = Date.getDate(
            dynamicEvidenceDataDetails.getAttribute("endDate").getValue());

          // Read the Active Product Delivery Case
          final ActiveCasesConcernRoleIDAndTypeKey activeCasesConcernRoleIDAndTypeKey =
            new ActiveCasesConcernRoleIDAndTypeKey();
          activeCasesConcernRoleIDAndTypeKey.dtls.caseTypeCode =
            CASETYPECODE.PRODUCTDELIVERY;
          activeCasesConcernRoleIDAndTypeKey.dtls.concernRoleID =
            caseHeaderDtls.concernRoleID;
          activeCasesConcernRoleIDAndTypeKey.dtls.statusCode =
            CASESTATUS.ACTIVE;

          final CaseHeaderDtlsList caseHeadDtlsList =
            curam.core.facade.fact.CaseHeaderFactory.newInstance()
              .searchActiveCasesByTypeConcernRoleID(
                activeCasesConcernRoleIDAndTypeKey);

          // There should be only one ACTIVE OAS Benefit Case can exists.
          if (caseHeadDtlsList.dtlsList.dtls.size() > CuramConst.gkZero) {

            // Check if it is OAS Product Delivery Case

            final ProductDeliveryKey productDeliveryKey =
              new ProductDeliveryKey();
            productDeliveryKey.caseID =
              caseHeadDtlsList.dtlsList.dtls.get(0).caseID;

            final ProductDeliveryTypeDetails productDeliveryTypeDetails =
              ProductDeliveryFactory.newInstance()
                .readProductType(productDeliveryKey);

            if (PRODUCTTYPE.OAS_BENEFITS
              .equals(productDeliveryTypeDetails.productType)) {

              // Create/Modify the OAS RCV Deduction
              final BDMOASDeductionKey bdmOASDeductionKey =
                recordBDMOASDeduction(evidenceKey, evidenceControllerObj,
                  caseKey, bdmOASDeductionDetails);

              final BDMOASMaintainDeduction caseDeductions =
                new BDMOASMaintainDeduction();

              caseDeductions.recordOASRecoveryTaxDeductions(
                bdmOASDeductionKey, bdmOASDeductionDetails);
            }
          }
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

    // Read Evidence Descriptor table and read participant role id.

    final EvidenceDescriptorKey descriptorKey = new EvidenceDescriptorKey();
    descriptorKey.evidenceDescriptorID = evidenceDescriptorID;
    return EvidenceDescriptorFactory.newInstance()
      .read(descriptorKey).participantID;

  }

  /**
   * This method either create or update an entry into BDMOASDeduction database
   * table.
   *
   * @param evidenceID
   * @param successionID
   * @param concernRoleID
   * @param vtwDeductionKey
   *
   * @throws AppException
   * Application Exception
   *
   * @throws InformationalException
   * Informational Exception
   */
  private BDMOASDeductionKey recordBDMOASDeduction(
    final EIEvidenceKey evidenceKey,
    final EvidenceControllerInterface evidenceControllerObj,
    final CaseKey caseKey,
    final BDMOASDeductionDetails bdmOASDeductionDetails)
    throws AppException, InformationalException {

    // Check if the there is already any existing OAS RCV deduction present for
    // the evidence

    final BDMOASDeductionEvidenceIDRecordStatus bdmOASDeductionKeyStruct1 =
      new BDMOASDeductionEvidenceIDRecordStatus();
    bdmOASDeductionKeyStruct1.evidenceID = evidenceKey.evidenceID;
    bdmOASDeductionKeyStruct1.recordStatusCode = RECORDSTATUS.NORMAL;

    final NotFoundIndicator nfInd = new NotFoundIndicator();

    BDMOASDeductionFactory.newInstance().readByEvidenceID(nfInd,
      bdmOASDeductionKeyStruct1);

    BDMOASDeductionKey bdmOASDeductionKey = new BDMOASDeductionKey();

    if (nfInd.isNotFound()) {

      bdmOASDeductionKey = createBDMOASDeductionForOASRecoveryTax(evidenceKey,
        evidenceControllerObj, caseKey, bdmOASDeductionDetails);

    }

    return bdmOASDeductionKey;
  }

  /**
   * This method will create the BDM OAS Recovery Tax Deduction
   *
   * @param evidenceID
   * @param successionID
   * @param concernRoleID
   * @param vtwDeductionKey
   * @return
   * @throws AppException
   * Application Exception
   *
   * @throws InformationalException
   * Informational Exception
   */
  private BDMOASDeductionKey createBDMOASDeductionForOASRecoveryTax(
    final EIEvidenceKey evidenceKey,
    final EvidenceControllerInterface evidenceControllerObj,
    final CaseKey caseKey,
    final BDMOASDeductionDetails bdmOASDeductionDetails)
    throws AppException, InformationalException {

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(evidenceKey);

    final BDMOASDeductionDtls bdmOASDeductionDtls = new BDMOASDeductionDtls();
    bdmOASDeductionDtls.caseID = caseKey.caseID;
    bdmOASDeductionDtls.deductionType = BDMDEDUCTIONTYPE.OAS_RCV_TAX;
    bdmOASDeductionDtls.concernRoleID =
      getConcernRoleID(eiEvidenceReadDtls.descriptor.evidenceDescriptorID);
    bdmOASDeductionDtls.recordStatusCode = RECORDSTATUS.NORMAL;
    bdmOASDeductionDtls.evidenceID = evidenceKey.evidenceID;

    final BDMOASDeductionKey bdmOASDeductionKey = new BDMOASDeductionKey();
    BDMOASDeductionFactory.newInstance().insert(bdmOASDeductionDtls);

    bdmOASDeductionKey.bdmOASDeductionID =
      bdmOASDeductionDtls.bdmOASDeductionID;

    return bdmOASDeductionKey;

  }

  /**
   * PreModify activation event method to check if the
   * OAS Recovery Tax Deduction is already processed and if as part
   * of the modify evidence the monthly recovery tax amount and
   * start date is modified then throw the validation message to the
   * user that Start Date and Monthly Recovery Tax amount can not be
   * modified.
   *
   * @param key
   * Contains the evidence id details.
   * @param descriptor
   * EvidenceDescriptorModifyDtls details.
   * @param evidenceObject
   * Contains the evidence modification details.
   * @param parentKey
   * Parent Evidence evidence id.
   * @throws AppException
   * Application Exception
   * @throws InformationalException
   * Informational Exception
   *
   */
  @Override
  public void preModify(final EIEvidenceKey key,
    final EvidenceDescriptorModifyDtls descriptor,
    final Object evidenceObject, final EIEvidenceKey parentKey)
    throws AppException, InformationalException {

    if (key.evidenceType.equals(CASEEVIDENCE.OAS_RECOVERY_TAX)) {

      // If the Start Date and Monthly Recovery Tax Amount is updated
      // then check if the Deduction is already used in the financial processing
      // If already used in the financial processing then throw the error
      // message.

      // Read the BDMOASDeduction table if deduction is already considered for
      // the
      // payment processing
      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
      final BDMOASDeductionEvidenceIDRecordStatus bdmOASDeductionKey =
        new BDMOASDeductionEvidenceIDRecordStatus();
      bdmOASDeductionKey.evidenceID = key.evidenceID;
      bdmOASDeductionKey.recordStatusCode = RECORDSTATUS.NORMAL;

      final BDMOASDeductionDtls bdmOASDeductionDtls =
        BDMOASDeductionFactory.newInstance()
          .readByEvidenceID(notFoundIndicator, bdmOASDeductionKey);

      // Check if the existing OAS RCV tax evidence is already processed during
      // the financial
      // processing.

      CaseDeductionItemDtls caseDeductionItemDtls =
        new CaseDeductionItemDtls();

      if (!notFoundIndicator.isNotFound()) {

        // Check if the case deduction item present for the evidence
        // If yes, read the details of case deduction item.
        if (bdmOASDeductionDtls.caseDeductionItemID != CuramConst.gkZero) {

          final CaseDeductionItemKey caseDeductionItemKey =
            new CaseDeductionItemKey();
          caseDeductionItemKey.caseDeductionItemID =
            bdmOASDeductionDtls.caseDeductionItemID;
          caseDeductionItemDtls =
            CaseDeductionItemFactory.newInstance().read(caseDeductionItemKey);

        }

        final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
          (DynamicEvidenceDataDetails) evidenceObject;

        final Money monthlyRecoveryTaxAmount = !dynamicEvidenceDataDetails
          .getAttribute("monthlyRecoveryTaxAmount").getValue().isEmpty()
            ? new Money(dynamicEvidenceDataDetails
              .getAttribute("monthlyRecoveryTaxAmount").getValue())
            : new Money(0.0D);

        final Date startDate = Date.getDate(
          dynamicEvidenceDataDetails.getAttribute("startDate").getValue());

        if (!startDate.equals(caseDeductionItemDtls.startDate)
          || !monthlyRecoveryTaxAmount.equals(caseDeductionItemDtls.amount)) {

          final BDMOASDeductionCountKey bdmOASDeductionCountKey =
            new BDMOASDeductionCountKey();
          bdmOASDeductionCountKey.evidenceID = key.evidenceID;

          // Check if the BDMOASDeduction is already processed.
          final BDMOASDeductionCountDetails bdmOASDeductionCountDetails =
            BDMOASDeductionFactory.newInstance()
              .countILIByEvidenceID(bdmOASDeductionCountKey);

          // If the deduction is already processed then throw the error.
          if (bdmOASDeductionCountDetails.numberOfRecords > CuramConst.gkZero) {

            throw new AppException(
              BDMOASEVIDENCEMESSAGE.ERR_OAS_RECOVERY_TAX_DEDUCTION_ALREADY_APPLIED);
          }
        }
      }
    }
  }

  /**
   * This Pre Activation method will check if the OAS Recovery Tax
   * evidence is marked for the deletion, is already used in the
   * payment calculation. If its already used the payment calculation
   * then it will throw the error message.
   *
   * @param evidenceControllerInterface
   * Evidence Controller Interface.
   * @param key
   * Contains the case key.
   * @param evidenceLists
   * Contains the evidence change lists.
   *
   * @throws AppException
   * Application Exception
   * @throws InformationalException
   * Informational Exception.
   *
   */
  @Override
  public void preActivation(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey key, final ApplyChangesEvidenceLists evidenceLists)
    throws AppException, InformationalException {

    for (final EvidenceKey evidenceKey : evidenceLists.removeList.dtls) {

      if (evidenceKey.evidenceType.equals(CASEEVIDENCE.OAS_RECOVERY_TAX)) {
        removeOASRCVDeduction(evidenceKey);
      }
    }
  }

  /**
   * This method will remove the OAS RCV deduction record
   * if the deduction record is not used in the processing and
   * OAS RCV evidence is marked for the deletion.
   *
   * @param evidenceKey
   * Contains the Evidence Key id.
   * @throws AppException
   * Application Exception
   * @throws InformationalException
   * Informational Exception.
   */
  public void removeOASRCVDeduction(final EvidenceKey evidenceKey)
    throws AppException, InformationalException {

    // Check if the Deduction is already used in the payment processing.

    // Read the BDMOASCaseDeduction details

    final BDMOASDeductionEvidenceIDRecordStatus bdmoasDeductionKeyStruct =
      new BDMOASDeductionEvidenceIDRecordStatus();
    bdmoasDeductionKeyStruct.evidenceID = evidenceKey.evidenceID;
    bdmoasDeductionKeyStruct.recordStatusCode = RECORDSTATUS.NORMAL;

    final BDMOASDeductionDtls bdmOASDeductionDtls = BDMOASDeductionFactory
      .newInstance().readByEvidenceID(bdmoasDeductionKeyStruct);

    final BDMOASDeductionCountKey bdmOASDeductionCountKey =
      new BDMOASDeductionCountKey();
    bdmOASDeductionCountKey.evidenceID = evidenceKey.evidenceID;

    // Check if the BDMOASDeduction is already processed.
    final BDMOASDeductionCountDetails bdmOASDeductionCountDetails =
      BDMOASDeductionFactory.newInstance()
        .countILIByEvidenceID(bdmOASDeductionCountKey);

    // If the deduction is already processed then throw the error.
    if (bdmOASDeductionCountDetails.numberOfRecords > CuramConst.gkZero) {

      throw new AppException(
        BDMOASEVIDENCEMESSAGE.ERR_OAS_RECOVERY_TAX_DEDUCTION_DELETE);
    }

    final IndicatorStruct deleteUnusedOASRCVDeduction = new IndicatorStruct();
    deleteUnusedOASRCVDeduction.changeAllIndicator = true;

    final CaseDeductionItemKey caseDeductionItemKey =
      new CaseDeductionItemKey();

    caseDeductionItemKey.caseDeductionItemID =
      bdmOASDeductionDtls.caseDeductionItemID;

    final MaintainParticipantDeduction maintainParticipantDeduction =
      new MaintainParticipantDeduction();

    maintainParticipantDeduction.deactivateSingleCaseDeduction(
      caseDeductionItemKey, deleteUnusedOASRCVDeduction);

    final BDMOASDeductionKey bdmOASDeductionKey = new BDMOASDeductionKey();
    bdmOASDeductionKey.bdmOASDeductionID =
      bdmOASDeductionDtls.bdmOASDeductionID;

    // Update the OAS Case Deduction record for the OAS RCV Deduction
    // as cancelled.
    bdmOASDeductionDtls.recordStatusCode = RECORDSTATUS.CANCELLED;
    BDMOASDeductionFactory.newInstance().modify(bdmOASDeductionKey,
      bdmOASDeductionDtls);

  }
}
