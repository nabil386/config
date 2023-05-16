package curam.ca.gc.bdmoas.evidence.events.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.evidence.events.impl.BDMAbstractEvidenceEventHandler;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMCurrentDeductionsForProductDeliveryList;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMDeductionItemDetail;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ConcernRoleIDKey;
import curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl.MaintainParticipantDeduction;
import curam.ca.gc.bdmoas.codetable.BDMOASNRTCORRECTIONREASON;
import curam.ca.gc.bdmoas.deductions.impl.BDMOASMaintainDeduction;
import curam.ca.gc.bdmoas.entity.oasdeduction.fact.BDMOASDeductionFactory;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionCountDetails;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionCountKey;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionDtls;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionEvidenceIDRecordStatus;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionKey;
import curam.ca.gc.bdmoas.message.BDMOASEVIDENCEMESSAGE;
import curam.codetable.BUSINESSSTATUS;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.PRODUCTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.facade.fact.ProductDeliveryFactory;
import curam.core.facade.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
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
import curam.util.type.NotFoundIndicator;

public class BDMOASNRTCorrectionEventHandler
  extends BDMAbstractEvidenceEventHandler {

  @Inject
  MaintainParticipantDeduction maintainParticipantDeductionObj;

  /**
   * Constructor
   */
  public BDMOASNRTCorrectionEventHandler() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public CASEEVIDENCEEntry evidenceType() {

    return CASEEVIDENCEEntry.OAS_NRT_CORRECTION;
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
   * Subscribe the Pre Activation Event
   */
  @Override
  public boolean subscribePreActivation() {

    return true;
  }

  /**
   *
   */
  @Override
  public void preModify(final EIEvidenceKey key,
    final EvidenceDescriptorModifyDtls descriptor,
    final Object evidenceObject, final EIEvidenceKey parentKey)
    throws AppException, InformationalException {

    final BDMOASDeductionEvidenceIDRecordStatus bdmOASDeductionEvidenceIDRecordStatus =
      new BDMOASDeductionEvidenceIDRecordStatus();
    bdmOASDeductionEvidenceIDRecordStatus.evidenceID = key.evidenceID;
    bdmOASDeductionEvidenceIDRecordStatus.recordStatusCode =
      RECORDSTATUS.NORMAL;

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    final BDMOASDeductionDtls bdmOASDeductionDtls =
      BDMOASDeductionFactory.newInstance().readByEvidenceID(notFoundIndicator,
        bdmOASDeductionEvidenceIDRecordStatus);

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) evidenceObject;

    final Date startDate = Date.getDate(
      dynamicEvidenceDataDetails.getAttribute("startDate").getValue());

    // Get the case deduction item id and check if the record is processed.

    if (!notFoundIndicator.isNotFound()) {

      // Check if the deduction is already processed
      // If processed then throw App Exception.

      final BDMOASDeductionCountKey bdmOASDeductionCountKey =
        new BDMOASDeductionCountKey();
      bdmOASDeductionCountKey.evidenceID = key.evidenceID;

      CaseDeductionItemDtls caseDeductionItemDtls =
        new CaseDeductionItemDtls();

      final CaseDeductionItemKey caseDeductionItemKey =
        new CaseDeductionItemKey();
      caseDeductionItemKey.caseDeductionItemID =
        bdmOASDeductionDtls.caseDeductionItemID;
      caseDeductionItemDtls =
        CaseDeductionItemFactory.newInstance().read(caseDeductionItemKey);

      // Check if the BDMOASDeduction is already processed.
      final BDMOASDeductionCountDetails bdmOASDeductionCountDetails =
        BDMOASDeductionFactory.newInstance()
          .countILIByEvidenceID(bdmOASDeductionCountKey);

      if (!startDate.equals(caseDeductionItemDtls.startDate)
        && bdmOASDeductionCountDetails.numberOfRecords > CuramConst.gkZero)

        // If the deduction is already processed then throw the error.
        throw new AppException(
          BDMOASEVIDENCEMESSAGE.ERR_OAS_NRT_DEDUCTION_START_DATE_MODIFY);
    }
  }

  /**
   *
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

      if (evidenceKey.evidenceType.equals(CASEEVIDENCE.OAS_NRT_CORRECTION)) {

        // Read the OAS Product Delivery Case

        final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
        caseHeaderKey.caseID = caseKey.caseID;
        final CaseHeaderDtls caseHeaderDtls =
          CaseHeaderFactory.newInstance().read(caseHeaderKey);

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

            final EvidenceControllerInterface evidenceControllerObj =
              (EvidenceControllerInterface) EvidenceControllerFactory
                .newInstance();

            final EIEvidenceReadDtls eiEvidenceReadDtls =
              evidenceControllerObj.readEvidence(evidenceKey);

            final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
              (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

            final Date startDate = Date.getDate(dynamicEvidenceDataDetails
              .getAttribute("startDate").getValue());

            // End Date Can not be empty date
            final Date endDate = Date.getDate(
              dynamicEvidenceDataDetails.getAttribute("endDate").getValue());

            final Double nrtCorrectionPercentage =
              Double.valueOf(dynamicEvidenceDataDetails
                .getAttribute("percentage").getValue());

            final String correctionReason = dynamicEvidenceDataDetails
              .getAttribute("correctionReason").getValue();

            BDMCurrentDeductionsForProductDeliveryList bdmNRTDeductionsList =
              new BDMCurrentDeductionsForProductDeliveryList();

            final BDMOASMaintainDeduction caseDeductions =
              new BDMOASMaintainDeduction();

            bdmNRTDeductionsList = caseDeductions.getNRTOverlappingDeductions(
              productDeliveryKey.caseID, startDate, endDate);

            // Check if the NRT Deduction to be Deleted or Modified.
            for (final BDMDeductionItemDetail bdmDeductionItemDetails : bdmNRTDeductionsList.dtls
              .items()) {

              final boolean isDeductionApplied =
                caseDeductions.isNRTDeductionApplied(bdmDeductionItemDetails);

              processExistingNRTDeduction(isDeductionApplied,
                bdmDeductionItemDetails, startDate, endDate);

            }

            // If the correction reason is "Deemed Resident" then don't create
            // the deduction

            if (!BDMOASNRTCORRECTIONREASON.OAS_DEEMED_RESIDENT
              .equalsIgnoreCase(correctionReason)) {

              // Create the new NRT Deduction

              final BDMOASMaintainDeduction bdmOASMaintainDeduction =
                new BDMOASMaintainDeduction();

              final Date nrtStartDate = startDate.after(Date.getCurrentDate())
                ? startDate : Date.getCurrentDate();

              final ConcernRoleIDKey concernRoleIDKey =
                new ConcernRoleIDKey();
              concernRoleIDKey.concernRoleID = caseHeaderDtls.concernRoleID;

              final String country =
                dynamicEvidenceDataDetails.getAttribute("country").getValue();

              final boolean isNRTCorrectionChangeInd = true;

              caseHeaderKey.caseID = productDeliveryKey.caseID;

              bdmOASMaintainDeduction.recordOASNRTDeductions(nrtStartDate,
                endDate, caseHeaderKey, concernRoleIDKey, country,
                isNRTCorrectionChangeInd, nrtCorrectionPercentage,
                evidenceKey.evidenceID);

            }
          }
        }
      }
    }
  }

  /**
   *
   * @param isDeductionApplied
   * @param bdmDeductionItemDetails
   * @param startDate
   * @param endDate
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public void processExistingNRTDeduction(final boolean isDeductionApplied,
    final BDMDeductionItemDetail bdmDeductionItemDetail, final Date startDate,
    final Date endDate) throws AppException, InformationalException {

    final CaseDeductionItemKey caseDeductionItemKey =
      new CaseDeductionItemKey();

    caseDeductionItemKey.caseDeductionItemID =
      bdmDeductionItemDetail.caseDeductionItemID;

    // If the Deduction is not applied then delete the existing deduction.
    if (!isDeductionApplied) {

      final IndicatorStruct deletedUnusedInd = new IndicatorStruct();
      deletedUnusedInd.changeAllIndicator = true;
      this.maintainParticipantDeductionObj.deactivateSingleCaseDeduction(
        caseDeductionItemKey, deletedUnusedInd);

    } else {

      // new instance
      final curam.core.intf.CaseDeductionItem caseDeductionItemObj =
        curam.core.fact.CaseDeductionItemFactory.newInstance();
      // If the Deduction is applied then end date the existing deduction
      final CaseDeductionItemDtls caseDeductionItemDtls =
        caseDeductionItemObj.read(caseDeductionItemKey);
      // check for the business status if active then will in-activate it
      if (!BUSINESSSTATUS.INACTIVE
        .equalsIgnoreCase(caseDeductionItemDtls.businessStatus)) {
        // update End date
        caseDeductionItemDtls.endDate = startDate.addDays(-1)
          .compareTo(Date.getCurrentDate().addDays(-1)) > CuramConst.gkZero
            ? startDate.addDays(-1) : Date.getCurrentDate().addDays(-1);
        // calling the method to modify the case deduction item details
        caseDeductionItemObj.modify(caseDeductionItemKey,
          caseDeductionItemDtls);
      }
    }
  }

  /**
   * This Pre Activation method will check if the NRT Correction
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

      if (evidenceKey.evidenceType.equals(CASEEVIDENCE.OAS_NRT_CORRECTION)) {
        removeOASNRTDeduction(evidenceKey);
      }
    }
  }

  /**
   *
   *
   * @param evidenceKey
   * Contains the Evidence Key id.
   * @throws AppException
   * Application Exception
   * @throws InformationalException
   * Informational Exception.
   */
  public void removeOASNRTDeduction(final EvidenceKey evidenceKey)
    throws AppException, InformationalException {

    // Check if the Deduction is already used in the payment processing.

    // Read the BDMOASCaseDeduction details

    final BDMOASDeductionEvidenceIDRecordStatus bdmoasDeductionKeyStruct =
      new BDMOASDeductionEvidenceIDRecordStatus();
    bdmoasDeductionKeyStruct.evidenceID = evidenceKey.evidenceID;
    bdmoasDeductionKeyStruct.recordStatusCode = RECORDSTATUS.NORMAL;

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    final BDMOASDeductionDtls bdmOASDeductionDtls =
      BDMOASDeductionFactory.newInstance().readByEvidenceID(notFoundIndicator,
        bdmoasDeductionKeyStruct);

    if (!notFoundIndicator.isNotFound()) {

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
          BDMOASEVIDENCEMESSAGE.ERR_OAS_NRT_DEDUCTION_DELETE);
      }

      final IndicatorStruct deleteUnusedOASNRTDeduction =
        new IndicatorStruct();
      deleteUnusedOASNRTDeduction.changeAllIndicator = true;

      final CaseDeductionItemKey caseDeductionItemKey =
        new CaseDeductionItemKey();

      caseDeductionItemKey.caseDeductionItemID =
        bdmOASDeductionDtls.caseDeductionItemID;

      final MaintainParticipantDeduction maintainParticipantDeduction =
        new MaintainParticipantDeduction();

      maintainParticipantDeduction.deactivateSingleCaseDeduction(
        caseDeductionItemKey, deleteUnusedOASNRTDeduction);

      final BDMOASDeductionKey bdmOASDeductionKey = new BDMOASDeductionKey();
      bdmOASDeductionKey.bdmOASDeductionID =
        bdmOASDeductionDtls.bdmOASDeductionID;

      // Update the OAS Case Deduction record for the OAS NRT Deduction
      // as cancelled.
      bdmOASDeductionDtls.recordStatusCode = RECORDSTATUS.CANCELLED;
      BDMOASDeductionFactory.newInstance().modify(bdmOASDeductionKey,
        bdmOASDeductionDtls);

    }
  }

}
