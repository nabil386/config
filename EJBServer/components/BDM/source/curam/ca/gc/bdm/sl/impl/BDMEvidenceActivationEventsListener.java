package curam.ca.gc.bdm.sl.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMVTWTYPE;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.fec.struct.BDMCaseKey;
import curam.ca.gc.bdm.facade.pdcperson.fact.BDMPDCPersonFactory;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.infrastructure.struct.ActiveEvdInstanceDtls;
import curam.core.facade.infrastructure.struct.ActiveEvdInstanceDtlsList;
import curam.core.facade.struct.CaseHeaderDetails;
import curam.core.facade.struct.CaseReference;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.AddressElement;
import curam.core.sl.entity.struct.TaskLinkDtls;
import curam.core.sl.entity.struct.TaskLinkDtlsList;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface.EvidenceActivationEvents;
import curam.core.sl.infrastructure.intf.EvidenceController;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.sl.struct.RecordCount;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.dynamicevidence.facade.fact.DynamicEvidenceMaintenanceFactory;
import curam.dynamicevidence.facade.intf.DynamicEvidenceMaintenance;
import curam.dynamicevidence.facade.struct.DynEvdModifyDetails;
import curam.dynamicevidence.facade.struct.DynamicEvidenceData;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.facade.struct.PDCCaseDetails;
import curam.pdc.facade.struct.PDCCaseDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import java.util.HashMap;

/**
 *
 * @author vipresh.sharma
 * @since task-17825
 */
public class BDMEvidenceActivationEventsListener
  implements EvidenceActivationEvents {

  @Inject
  curam.ca.gc.bdm.sl.maintaincasedeductions.intf.MaintainCaseDeductions caseDeductions;

  BDMUtil bdmUtil = new BDMUtil();

  public BDMEvidenceActivationEventsListener() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void postActivation(
    final EvidenceControllerInterface evidenceControllerInterface,
    final CaseKey key, final EIEvidenceKeyList list)
    throws AppException, InformationalException {

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseID;
    final String caseTypeCode =
      CaseHeaderFactory.newInstance().read(caseHeaderKey).caseTypeCode;
    for (final EIEvidenceKey eiEvidenceKey : list.dtls) {
      if (eiEvidenceKey.evidenceType
        .equals(CASEEVIDENCE.BDM_FOREIGN_RESIDENCE_PERIOD)) {

        final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
        final BDMCaseKey bdmCaseKey = new BDMCaseKey();
        bdmCaseKey.caseID = key.caseID;

        final EvidenceDescriptorDtls evidenceDescriptorDtls =
          new EvidenceDescriptorDtls();
        evidenceDescriptorDtls.caseID = key.caseID;
        evidenceDescriptorDtls.statusCode = EVIDENCEDESCRIPTORSTATUS.INEDIT;
        evidenceDescriptorDtls.evidenceType =
          CASEEVIDENCE.BDM_FOREIGN_RESIDENCE_PERIOD;

        final RecordCount count =
          bdmfeCase.countOfInEditEvidenceType(evidenceDescriptorDtls);

        TaskLinkDtlsList taskLinkDtlsList = new TaskLinkDtlsList();
        taskLinkDtlsList =
          bdmfeCase.getListOfOpenFRPEvidenceTasks(bdmCaseKey);

        final int taskListSize = taskLinkDtlsList.dtls.size();

        if (key.caseID != CuramConst.kLongZero
          && eiEvidenceKey.evidenceID != CuramConst.kLongZero
          && count.count == CuramConst.gkZero
          && taskListSize == CuramConst.gkOne) {

          for (TaskLinkDtls taskLinkDtls : taskLinkDtlsList.dtls) {

            bdmUtil.raiseAndCloseTask(taskLinkDtls.taskID);
          }

        }

      } else if (eiEvidenceKey.evidenceType
        .equals(CASEEVIDENCE.BDM_FOREIGN_CONTRIBUTION_PERIOD)) {

        final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
        final BDMCaseKey bdmCaseKey = new BDMCaseKey();
        bdmCaseKey.caseID = key.caseID;

        final EvidenceDescriptorDtls evidenceDescriptorDtls =
          new EvidenceDescriptorDtls();
        evidenceDescriptorDtls.caseID = key.caseID;
        evidenceDescriptorDtls.statusCode = EVIDENCEDESCRIPTORSTATUS.INEDIT;
        evidenceDescriptorDtls.evidenceType =
          CASEEVIDENCE.BDM_FOREIGN_CONTRIBUTION_PERIOD;

        final RecordCount count =
          bdmfeCase.countOfInEditEvidenceType(evidenceDescriptorDtls);

        TaskLinkDtlsList taskLinkDtlsList = new TaskLinkDtlsList();
        taskLinkDtlsList =
          bdmfeCase.getListOfOpenFCPEvidenceTasks(bdmCaseKey);

        final int taskListSize = taskLinkDtlsList.dtls.size();

        if (key.caseID != CuramConst.kLongZero
          && eiEvidenceKey.evidenceID != CuramConst.kLongZero
          && count.count == CuramConst.gkZero
          && taskListSize == CuramConst.gkOne) {

          for (TaskLinkDtls taskLinkDtls : taskLinkDtlsList.dtls) {

            bdmUtil.raiseAndCloseTask(taskLinkDtls.taskID);

          }

        }

      } else if (eiEvidenceKey.evidenceType.equals(CASEEVIDENCE.BDMADDRESS)) {

        updateVTWEvidenceAndDeductionDetails(eiEvidenceKey,
          evidenceControllerInterface);

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
    try {
      final EvidenceDescriptorKey descriptorKey = new EvidenceDescriptorKey();
      descriptorKey.evidenceDescriptorID = evidenceDescriptorID;
      concernRoleID = EvidenceDescriptorFactory.newInstance()
        .read(descriptorKey).participantID;
    } catch (final RecordNotFoundException rnfe) {
      concernRoleID = 0l;
    }

    // Return concern role id.
    return concernRoleID;
  }

  /**
   * This method will update end on the VTW Evidence of Type Provincial if the
   * end date value is added to the Residential Address evidence.
   *
   * @param eiEvidenceKey
   * @param evidenceControllerInterface
   * @throws AppException
   * @throws InformationalException
   */
  public void updateVTWEvidenceAndDeductionDetails(
    final EIEvidenceKey eiEvidenceKey,
    final EvidenceControllerInterface evidenceControllerInterface)
    throws AppException, InformationalException {

    // Check if the end date is added to the Residential Address Evidence
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

    final String addressIDStr =
      dynamicEvidenceDataDetails.getAttribute("address").getValue();

    final String addressType =
      dynamicEvidenceDataDetails.getAttribute("addressType").getValue();

    final String endDate =
      dynamicEvidenceDataDetails.getAttribute("toDate").getValue();

    final long addressID = Long.parseLong(addressIDStr);

    if (CONCERNROLEADDRESSTYPE.PRIVATE.equals(addressType)) {

      final AddressElement addressElementObj =
        AddressElementFactory.newInstance();

      final AddressKey addressKey = new AddressKey();
      addressKey.addressID = addressID;

      final AddressElementDtlsList addressElementDtlsList =
        addressElementObj.readAddressElementDetails(addressKey);

      boolean isQuebecAddress = false;

      for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {

        if (ADDRESSELEMENTTYPE.PROVINCE.equals(addressElementDtls.elementType)
          && PROVINCETYPE.QUEBEC.equals(addressElementDtls.elementValue)) {
          isQuebecAddress = true;
          break;
        }
      }

      if (isQuebecAddress && !StringUtil.isNullOrEmpty(endDate)) {

        // Check if the Concern Role has any active Provincial VTW evidence and
        // deductions
        // which needs to be end dated.
        // If already end dated then ignore the change.

        // Get the concernRole ID
        final EvidenceDescriptorKey evidenceDescriptorKey =
          new EvidenceDescriptorKey();
        evidenceDescriptorKey.evidenceDescriptorID =
          eiEvidenceReadDtls.descriptor.evidenceDescriptorID;

        final EvidenceDescriptorDtls evidenceDiscriptorDtls =
          EvidenceDescriptorFactory.newInstance().read(evidenceDescriptorKey);

        final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
        concernRoleKey.concernRoleID = evidenceDiscriptorDtls.participantID;

        // Get the list of IC Cases

        final PDCCaseDetailsList pdcCaseDetailsList =
          BDMPDCPersonFactory.newInstance().listCurrentCases(concernRoleKey);

        final CaseKey caseKey = new CaseKey();

        for (final PDCCaseDetails pdcCaseDetails : pdcCaseDetailsList.dtlsList
          .items()) {

          final CaseReference caseReference = new CaseReference();
          caseReference.dtls.caseReference = pdcCaseDetails.reference;
          final CaseHeaderDetails caseHeaderDetails =
            curam.core.facade.fact.CaseHeaderFactory.newInstance()
              .readByCaseReference(caseReference);

          if (CASETYPECODE.INTEGRATEDCASE
            .equals(caseHeaderDetails.dtls.caseTypeCode)) {

            caseKey.caseID = caseHeaderDetails.dtls.caseID;
            break;
          }
        }

        // Check if the IC has the VTW Evidence with the Type
        // as Provincial

        if (caseKey.caseID != CuramConst.gkZero) {

          final EvidenceController evidenceController =
            EvidenceControllerFactory.newInstance();
          final ActiveEvdInstanceDtlsList activeEvdInstanceDtlsList =
            evidenceController.listAllactiveEvdInstances(caseKey);

          // Check if the list has the VTW Evidences

          for (final ActiveEvdInstanceDtls activeEvdInstanceDtls : activeEvdInstanceDtlsList.dtls
            .items()) {

            if (CASEEVIDENCE.BDMVTW
              .equals(activeEvdInstanceDtls.evidenceType)) {

              // Update the end date and apply changes.

              updateVTWEvidenceAndApplyChanges(activeEvdInstanceDtls, endDate,
                caseKey);

            }
          }
        }
      }
    }
  }

  /**
   * Method will update the VTW evidence and Perform the apply changes
   * This will also update the VTW Deductions
   *
   * @param activeEvdInstanceDtls
   * @param endDate
   * @throws AppException
   * @throws InformationalException
   */
  public void updateVTWEvidenceAndApplyChanges(
    final ActiveEvdInstanceDtls activeEvdInstanceDtls, final String endDate,
    final CaseKey caseKey) throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    eiEvidenceKey.evidenceID = activeEvdInstanceDtls.evidenceID;
    eiEvidenceKey.evidenceType = CASEEVIDENCE.BDMVTW;

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey);

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

    final String endDateValue =
      dynamicEvidenceDataDetails.getAttribute("endDate").getValue();

    final String voluntaryTaxWithholdType = dynamicEvidenceDataDetails
      .getAttribute("voluntaryTaxWithholdType").getValue();

    if (StringUtil.isNullOrEmpty(endDateValue)
      && BDMVTWTYPE.PROVINCIAL.equals(voluntaryTaxWithholdType)) {

      // Update the end date of the VTW evidence

      final DynamicEvidenceMaintenance dynObj =
        DynamicEvidenceMaintenanceFactory.newInstance();
      final DynamicEvidenceData dynamicEvidenceData =
        new DynamicEvidenceData();
      final DynEvdModifyDetails dynMod = new DynEvdModifyDetails();

      final HashMap<String, String> evidenceData = new HashMap<>();
      evidenceData.put("endDate", endDate);

      dynamicEvidenceData.data =
        BDMEvidenceUtil.setDynamicEvidenceDetailsByEvidenceType(evidenceData,
          CASEEVIDENCE.BDMVTW);
      dynamicEvidenceData.caseIDKey.caseID = caseKey.caseID;
      dynamicEvidenceData.evidenceId = activeEvdInstanceDtls.evidenceID;
      dynamicEvidenceData.descriptor.evidenceType = CASEEVIDENCE.BDMVTW;
      dynamicEvidenceData.descriptor.receivedDate = Date.getCurrentDate();
      dynamicEvidenceData.descriptor.changeReason =
        EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
      dynMod.effectiveDateUsed =
        BDMDateUtil.dateFormatter(Date.getCurrentDate().toString());

      // modify the evidence
      final ReturnEvidenceDetails returnEvidenceDetails =
        dynObj.modifyEvidence(dynamicEvidenceData, dynMod);

      final curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor descriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey =
        new RelatedIDAndEvidenceTypeKey();
      relatedIDAndEvidenceTypeKey.evidenceType = CASEEVIDENCE.BDMVTW;
      relatedIDAndEvidenceTypeKey.relatedID =
        returnEvidenceDetails.evidenceKey.evidenceID;
      final EvidenceDescriptorDtls evidenceDescriptorDtls =
        descriptorObj.readByRelatedIDAndType(relatedIDAndEvidenceTypeKey);
      BDMEvidenceUtil
        .applyInEditEvidenceToICDynamicEvidence(evidenceDescriptorDtls);
    }
  }

}
