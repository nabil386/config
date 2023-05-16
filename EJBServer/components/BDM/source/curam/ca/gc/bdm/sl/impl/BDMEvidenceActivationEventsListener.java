package curam.ca.gc.bdm.sl.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.facade.fec.struct.BDMCaseKey;
import curam.ca.gc.bdm.sl.struct.RecordVTWDeductionKey;
import curam.ca.gc.bdm.vtw.deduction.entity.fact.BDMVTWDeductionFactory;
import curam.ca.gc.bdm.vtw.deduction.entity.intf.BDMVTWDeduction;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionDtls;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionDtlsStruct2;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKey;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKeyStruct2;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.struct.TaskLinkDtls;
import curam.core.sl.entity.struct.TaskLinkDtlsList;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface.EvidenceActivationEvents;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.core.sl.struct.RecordCount;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;

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
      if (eiEvidenceKey.evidenceType.equals(CASEEVIDENCE.BDMVTW)) {

        if (CASETYPECODE.INTEGRATEDCASE.equals(caseTypeCode)) {

          final RecordVTWDeductionKey recordVTWDeductionKey =
            new RecordVTWDeductionKey();

          final EvidenceControllerInterface evidenceControllerObj =
            (EvidenceControllerInterface) EvidenceControllerFactory
              .newInstance();
          final EIEvidenceReadDtls eiEvidenceReadDtls =
            evidenceControllerObj.readEvidence(eiEvidenceKey);

          final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
            (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

          recordVTWDeductionKey.caseID = key.caseID;
          final String vtwType = dynamicEvidenceDataDetails
            .getAttribute("voluntaryTaxWithholdType").getValue();
          recordVTWDeductionKey.vtwType = vtwType;

          recordVTWDeductionKey.amount = !dynamicEvidenceDataDetails
            .getAttribute("amount").getValue().isEmpty()
              ? new Money(
                dynamicEvidenceDataDetails.getAttribute("amount").getValue())
              : new Money(0.0D);

          recordVTWDeductionKey.rate = !dynamicEvidenceDataDetails
            .getAttribute("percentageValue").getValue().isEmpty()
              ? Double.valueOf(dynamicEvidenceDataDetails
                .getAttribute("percentageValue").getValue())
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
            makeEntryIntoVTWDeductionTable(eiEvidenceKey.evidenceID,
              eiEvidenceReadDtls.descriptor.successionID, concernRoleID,
              recordVTWDeductionKey);

          final BDMVTWDeductionKey vtwDeductionKey = new BDMVTWDeductionKey();
          vtwDeductionKey.vtwDeductionID = vtwDeductionID;
          caseDeductions.recordVTWDeductionByIC(vtwDeductionKey);
        }
      } else if (eiEvidenceKey.evidenceType
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

}
