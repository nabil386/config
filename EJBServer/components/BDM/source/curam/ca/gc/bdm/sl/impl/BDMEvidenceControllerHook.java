package curam.ca.gc.bdm.sl.impl;

import com.google.inject.Inject;
import curam.advisor.facade.fact.AdvisorFactory;
import curam.advisor.facade.struct.AdviceKey;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMPaymentDestinationIDVersionNo;
import curam.ca.gc.bdm.bdmpaymentdestination.struct.BDMUpdateEFTDestinationDetails;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.fact.BDMBankAccountIntegrityCounterFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignApplicationFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory;
import curam.ca.gc.bdm.entity.fact.BDMWorkQueueCountryLinkFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentDestinationDtlsList;
import curam.ca.gc.bdm.entity.intf.BDMBankAccountIntegrityCounter;
import curam.ca.gc.bdm.entity.intf.BDMWorkQueueCountryLink;
import curam.ca.gc.bdm.entity.struct.BDMAccountNumberSortCodeKey;
import curam.ca.gc.bdm.entity.struct.BDMBankAccountIntegrityCounterDtls;
import curam.ca.gc.bdm.entity.struct.BDMBankAccountIntegrityCounterKey;
import curam.ca.gc.bdm.entity.struct.BDMFAKeyStruct3;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMReadFLByCaseIDKey;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMWorkQueueCountryLinkKey;
import curam.ca.gc.bdm.evidence.impl.BDMBankAccountValidation;
import curam.ca.gc.bdm.facade.fec.struct.BDMCaseKey;
import curam.ca.gc.bdm.facade.participant.fact.BDMParticipantFactory;
import curam.ca.gc.bdm.facade.participant.struct.BDMTasksForConcernAndCaseDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.struct.BDMUACaseWorkerVerificationEnactKey;
import curam.ca.gc.bdm.sl.fec.struct.BDMFECTaskCreateDetails;
import curam.ca.gc.bdm.sl.financial.intf.BDMFinancial;
import curam.ca.gc.bdm.sl.financial.maintainpaymentdestination.intf.BDMMaintainPaymentDestination;
import curam.ca.gc.bdm.sl.struct.BDMPostAddressChangeKey;
import curam.ca.gc.bdm.sl.struct.BDMTaskSearchDetails;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TASKPRIORITY;
import curam.core.facade.struct.ListParticipantTaskKey_eo;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.intf.CaseParticipantRole;
import curam.core.sl.entity.struct.CancelCaseParticipantRoleDetails;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.entity.struct.CaseParticipantRole_eoModifyParticipantIDToDate;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeCaseIDStatusesKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.SuccessionID;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.RecordCount;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.core.struct.ConcernRoleAlternateIDKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.sl.entity.fact.DynamicEvidenceDataAttributeFactory;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtls;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataIDAndAttrKey;
import curam.dynamicevidence.type.impl.DateConverter;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.events.TASK;
import curam.pdc.impl.PDCConst;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
import curam.util.workflow.struct.TaskKey;
import curam.wizard.util.impl.CodetableUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This is a hook class for evidence operations. It invokes methods before or
 * after an evidence is created/modified/deleted.
 *
 *
 */
public class BDMEvidenceControllerHook
  extends curam.ca.gc.bdm.sl.base.BDMEvidenceControllerHook {

  // BEGIN TASK 12976
  @Inject
  private BDMBankAccountValidation bankValidation;
  // END TASK 12976

  @Inject
  BDMFinancial bdmFInancialObj;

  @Inject
  BDMMaintainPaymentDestination maintainPaymentDestinationObj;

  private final BDMUtil bdmUtil = new BDMUtil();

  public BDMEvidenceControllerHook() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  public static final String kAltIDType = "altIDType";

  public static final String kAlternateID = "alternateID";

  public static final String kAddressType = "addressType";

  public static final String kToDate = "to";

  public static final String kCpr = "caseParticipantRole";

  public static final String kThdPtyCpr = "thirdPartyCaseParticipantRole";

  /**
   * This method is invoked when a user tries to delete an evidence. It has
   * custom logic that executes before removing the evidence.
   *
   * @param caseID
   * @param evidenceType
   * @return The active evidences
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void preRemoveEvidence(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    super.preRemoveEvidence(caseKey, evidenceKey);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;
    boolean isPhoneorEmailUsedForAlerts = false;

    if (evidenceKey.evidenceType.equals(PDCConst.PDCIDENTIFICATION)) {

      final DynamicEvidenceDataIDAndAttrKey attrKey =
        new DynamicEvidenceDataIDAndAttrKey();
      attrKey.evidenceID = evidenceKey.evidenceID;
      attrKey.name = kAltIDType;

      final DynamicEvidenceDataAttributeDtlsList attributes =
        DynamicEvidenceDataAttributeFactory.newInstance()
          .searchByEvidenceIDAndAttribute(attrKey);

      for (final DynamicEvidenceDataAttributeDtls evidenceAttrDtls : attributes.dtls) {
        if (evidenceAttrDtls.value
          .equals(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER)
          || evidenceAttrDtls.value
            .equals(CONCERNROLEALTERNATEID.REFERENCE_NUMBER)) {

          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(
              BDMEVIDENCE.ERR_IDENTIFICATION_CANNOT_BE_DELETED),
            null, InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
            0);

          TransactionInfo.getInformationalManager().failOperation();

        } else if (evidenceAttrDtls.value
          .equals(CONCERNROLEALTERNATEID.BDM_FOREIGN_IDENTIFIER)) {
          // START: BUG 107703 : Validation for Foreign Identifier Deletion.
          validateForeignIdentifierDeletion(caseKey,
            dynamicEvidenceDataDetails);
          // END: BUG 107703
        } // end if BDM_FOREIGN_IDENTIFIER
      }

    }

    if (evidenceKey.evidenceType.equals(PDCConst.PDCPHONENUMBER)) {

      isPhoneorEmailUsedForAlerts = (Boolean) DynamicEvidenceTypeConverter
        .convert(dynamicEvidenceDataDetails.getAttribute("useForAlertsInd"));

      if (isPhoneorEmailUsedForAlerts) {

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMEVIDENCE.ERR_PHOENUM_CANNOT_BE_DELETED), null,
          InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      }

    }
    // START :TASK 9845
    if (evidenceKey.evidenceType.equals(PDCConst.PDCEMAILADDRESS)) {

      isPhoneorEmailUsedForAlerts = (Boolean) DynamicEvidenceTypeConverter
        .convert(dynamicEvidenceDataDetails.getAttribute("useForAlertsInd"));

      if (isPhoneorEmailUsedForAlerts) {

        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMEVIDENCE.ERR_EMAIL_CANNOT_BE_DELETED), null,
          InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      }

    }
    // END :TASK 9845

    if (evidenceKey.evidenceType.equals(PDCConst.PDCADDRESS)) {

      // get addrestype for selected evidence for delete
      final String incomingAddressType =
        dynamicEvidenceDataDetails.getAttribute("addressType").getValue();

      final EvidenceDescriptorDtlsList evidenceDescriptorDtlsList =
        getActiveEvidenceByType(caseKey.caseID, evidenceKey.evidenceType);

      int count = 0;
      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : evidenceDescriptorDtlsList.dtls
        .items()) {

        final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
        eiEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
        eiEvidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;

        final EIEvidenceReadDtls eiEvidenceReadDtlsAddress =
          evidenceControllerObj.readEvidence(eiEvidenceKey);

        final DynamicEvidenceDataDetails dynamicEvidenceDataDetailsAddress =
          (DynamicEvidenceDataDetails) eiEvidenceReadDtlsAddress.evidenceObject;

        final String addressType = dynamicEvidenceDataDetailsAddress
          .getAttribute("addressType").getValue();

        if (incomingAddressType.equals(addressType)) {

          count++;
        }
      } // for

      if (count <= 1
        && incomingAddressType.equals(CONCERNROLEADDRESSTYPE.PRIVATE)) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMEVIDENCE.ERR_ATLEAST_1_RESIDENTIAL_MUST_BE_PRESENT),
          null, InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      }
      if (count <= 1
        && incomingAddressType.equals(CONCERNROLEADDRESSTYPE.MAILING)) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(BDMEVIDENCE.ERR_ATLEAST_1_MAILING_MUST_BE_PRESENT),
          null, InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);

        TransactionInfo.getInformationalManager().failOperation();

      }

    }

    if (evidenceKey.evidenceType.equals(PDCConst.PDCCONTACTPREFERENCES)) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEVIDENCE.ERR_CONTACT_PREFERENCE_CANNOT_BE_DELETED),
        null, InformationalType.kError,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine, 0);

      TransactionInfo.getInformationalManager().failOperation();

    }

    // BEGIN TASK 12976 - validate if the payment has been issued to the bank
    // account before removing it
    if (evidenceKey.evidenceType.equals(PDCConst.PDCBANKACCOUNT)) {

      bankValidation.validateRemovePDCBankAccountEvidence(caseKey,
        evidenceKey);
    }
    // END TASK 12976
  }

  /**
   * Gets the active evidences by Evidence Type.
   *
   * @param caseID
   * @param evidenceType
   * @return The active evidences
   * @throws AppException
   * @throws InformationalException
   */

  private EvidenceDescriptorDtlsList
    getActiveEvidenceByType(final long caseID, final String evidenceType)
      throws AppException, InformationalException {

    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    final EvidenceTypeCaseIDStatusesKey evidenceTypeCaseIDStatusesKey =
      new EvidenceTypeCaseIDStatusesKey();
    evidenceTypeCaseIDStatusesKey.caseID = caseID;
    evidenceTypeCaseIDStatusesKey.evidenceType = evidenceType;
    evidenceTypeCaseIDStatusesKey.statusCode1 =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;
    final EvidenceDescriptorDtlsList evidenceDescriptorDtlsList =
      evidenceDescriptorObj
        .searchByCaseIDEvidenceTypeAndStatus(evidenceTypeCaseIDStatusesKey);
    return evidenceDescriptorDtlsList;
  }

  /**
   * This method is invoked when a user tries to insert an evidence. It has
   * custom logic that executes after creating the evidence.
   *
   * @param case key
   * @param evidence key
   * @throws AppException
   * @throws Information
   */
  @Override
  public void postInsertEvidence(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    super.postInsertEvidence(caseKey, evidenceKey);
    // check for evidence type and call the required logic to be executed in
    // post create
    if (evidenceKey.evidenceType.equals(PDCConst.PDCBANKACCOUNT)) {
      // TASK 10932 : Bank Account Evidence Create/Modify Hookpoint
      handleBankAccountIntegrityReview(caseKey, evidenceKey);
    } else if (evidenceKey.evidenceType.equals(PDCConst.PDCADDRESS)) {

      handlePostAddressChange(evidenceKey);

    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.BDM_FOREIGN_RESIDENCE_PERIOD)) {

      // checkIfActiveTaskAvaible(caseKey.caseID);
      final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
      final BDMCaseKey bdmCaseKey = new BDMCaseKey();
      bdmCaseKey.caseID = caseKey.caseID;
      final RecordCount count =
        bdmfeCase.checkIfFRPTaskExistForThisCase(bdmCaseKey);

      if (caseKey.caseID != CuramConst.kLongZero
        && evidenceKey.evidenceID != CuramConst.kLongZero
        && count.count == CuramConst.gkZero) {
        enactFRPTaskWorkFlow(caseKey, evidenceKey);
      }

    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.BDM_FOREIGN_CONTRIBUTION_PERIOD)) {

      // checkIfActiveTaskAvaible(caseKey.caseID);
      final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
      final BDMCaseKey bdmCaseKey = new BDMCaseKey();
      bdmCaseKey.caseID = caseKey.caseID;
      final RecordCount count =
        bdmfeCase.checkIfFCPTaskExistForThisCase(bdmCaseKey);

      if (caseKey.caseID != CuramConst.kLongZero
        && evidenceKey.evidenceID != CuramConst.kLongZero
        && count.count == CuramConst.gkZero) {
        enactFCPTaskWorkFlow(caseKey, evidenceKey);
      }
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD)) {
      // 90018 DEV: Evidence- creditable residency period in Canada
      final AdviceKey adviceKey = new AdviceKey();
      adviceKey.adviceContextKeyName = "BDMFECIntegratedCase_home";
      adviceKey.parameters = "caseID=" + caseKey.caseID;
      AdvisorFactory.newInstance().getAdvice(adviceKey);
    }
  }

  /**
   * Task 85744: DEV: <Client Oral Communication> <Client Written Communication>
   * Evidence of type <Evidence Type> Foreign Engagement Case <Case Reference>
   * requires approval.
   *
   * Example Task Subject Line: EN Evidence of type Foreign Residence Evidence
   * Foreign Engagement Case 123 requires Approval
   *
   * Trigger: Foreign Residence Period Evidence added to a Foreign Engagenent
   * Case
   * Foreign Residency Period Evidence added to a Foreign Engagenent Case in "In
   * Edit" state for Person/Prospect Person who has a Canadian Benefit
   * application.
   *
   *
   *
   * @param caseKey
   * @param evidenceKey
   * @throws AppException
   * @throws InformationalException
   */
  private void enactFRPTaskWorkFlow(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseKey.caseID;
    final CaseHeaderDtls caseHeaderDtls =
      CaseHeaderFactory.newInstance().read(caseHeaderKey);

    final List<Object> enactmentStructs = new ArrayList<>();
    final BDMFECTaskCreateDetails bfmFECTaskCrtDetails =
      new BDMFECTaskCreateDetails();

    bfmFECTaskCrtDetails.participantRoleID = caseHeaderDtls.concernRoleID;
    bfmFECTaskCrtDetails.caseID = caseKey.caseID;
    bfmFECTaskCrtDetails.priority = TASKPRIORITY.NORMAL;

    final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
    bdmfeCaseKey.caseID = caseKey.caseID;
    final BDMFECaseDtls bdmfeCaseDtls = bdmfeCase.read(bdmfeCaseKey);

    final BDMWorkQueueCountryLink bdmWorkQueueCountryLink =
      BDMWorkQueueCountryLinkFactory.newInstance();
    final BDMWorkQueueCountryLinkKey bdmWorkQueueCountryLinkKey =
      new BDMWorkQueueCountryLinkKey();
    bdmWorkQueueCountryLinkKey.countryCode = bdmfeCaseDtls.countryCode;

    // START: Bug 99510: Cant create foreign contribution period/ foreign
    // residence period as ioClientContact. Unhandled exception
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    BDMWorkQueueCountryLinkDtls bdmWorkQueueCountryLinkDtls =
      new BDMWorkQueueCountryLinkDtls();

    bdmWorkQueueCountryLinkDtls =
      bdmWorkQueueCountryLink.read(nfIndicator, bdmWorkQueueCountryLinkKey);

    if (!nfIndicator.isNotFound()) {
      bfmFECTaskCrtDetails.workQueueID =
        bdmWorkQueueCountryLinkDtls.workQueueID;
    }
    // END: Bug 99510: Cant create foreign contribution period/ foreign
    // residence period as ioClientContact. Unhandled exception

    final String currentLoggedInUser = TransactionInfo.getProgramUser();

    if (bfmFECTaskCrtDetails.workQueueID == CuramConst.kLongZero) {
      bfmFECTaskCrtDetails.workQueueID =
        bdmUtil.getWorkQueueIDByProvOfLoggedInUser(currentLoggedInUser);
    }

    // <Client Oral Communication> <Client Written Communication> Evidence of
    // type <Evidence Type> Foreign Engagement Case <Case Reference> requires
    // approval.
    // Example: EN Evidence of type Foreign Residence Evidence Foreign
    // Engagement Case 123 requires Approval
    final StringBuffer taskSubject = new StringBuffer();

    String orallanuage = CuramConst.gkEmpty;
    String writtenlanuage = CuramConst.gkEmpty;
    StringBuffer oralWrittenLanguage = new StringBuffer();

    DynamicEvidenceDataAttributeDtlsList dynamicEvidenceDataAttributeDtlsList =
      new DynamicEvidenceDataAttributeDtlsList();

    final EvidenceDescriptorDtls evidenceDescriptorDtlsKey =
      new EvidenceDescriptorDtls();
    evidenceDescriptorDtlsKey.participantID =
      bfmFECTaskCrtDetails.participantRoleID;
    evidenceDescriptorDtlsKey.evidenceType = PDCConst.PDCCONTACTPREFERENCES;
    evidenceDescriptorDtlsKey.statusCode = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    dynamicEvidenceDataAttributeDtlsList =
      bdmfeCase.getContactPreferencesEvidenceList(evidenceDescriptorDtlsKey);

    DynamicEvidenceDataAttributeDtls dynamicEvidenceDataAttributeDtlsTemp =
      new DynamicEvidenceDataAttributeDtls();

    for (final DynamicEvidenceDataAttributeDtls dynamicEvidenceDataAttributeDtls : dynamicEvidenceDataAttributeDtlsList.dtls) {
      dynamicEvidenceDataAttributeDtlsTemp =
        new DynamicEvidenceDataAttributeDtls();

      dynamicEvidenceDataAttributeDtlsTemp
        .assign(dynamicEvidenceDataAttributeDtls);

      if (dynamicEvidenceDataAttributeDtlsTemp.name
        .equals(BDMConstants.kpreferredOralLanguage)) {

        if (BDMLANGUAGE.ENGLISHL
          .equals(dynamicEvidenceDataAttributeDtlsTemp.value)) {
          orallanuage = BDMConstants.gkLocaleUpperEN;
        } else if (BDMLANGUAGE.FRENCHL
          .equals(dynamicEvidenceDataAttributeDtlsTemp.value)) {
          orallanuage = BDMConstants.gkLocaleUpperFR;
        }

      } else if (dynamicEvidenceDataAttributeDtlsTemp.name
        .equals(BDMConstants.kpreferredWrittenLanguage)) {

        if (BDMLANGUAGE.ENGLISHL
          .equals(dynamicEvidenceDataAttributeDtlsTemp.value)) {
          writtenlanuage = BDMConstants.gkLocaleUpperEN;
        } else if (BDMLANGUAGE.FRENCHL
          .equals(dynamicEvidenceDataAttributeDtlsTemp.value)) {
          writtenlanuage = BDMConstants.gkLocaleUpperFR;
        }

      }

    }

    if (!StringUtil.isNullOrEmpty(orallanuage)
      && !StringUtil.isNullOrEmpty(writtenlanuage)
      && orallanuage.equals(writtenlanuage)) {
      oralWrittenLanguage = new StringBuffer();
      oralWrittenLanguage.append(orallanuage);
    } else if (!StringUtil.isNullOrEmpty(orallanuage)
      && !StringUtil.isNullOrEmpty(writtenlanuage)
      && !orallanuage.equals(writtenlanuage)) {
      oralWrittenLanguage = new StringBuffer();
      oralWrittenLanguage.append(BDMConstants.gkBIL);

    } else {
      oralWrittenLanguage.append(BDMConstants.gkLocaleUpperEN);
    }

    taskSubject.append(oralWrittenLanguage + CuramConst.gkSpace);

    final String evidenceTypeDesc = CodetableUtil.getCodetableDescription(
      CASEEVIDENCE.TABLENAME, evidenceKey.evidenceType);

    final String fecTypeDesc = CodetableUtil.getCodetableDescription(
      PRODUCTCATEGORY.TABLENAME, caseHeaderDtls.integratedCaseType);

    bfmFECTaskCrtDetails.evidenceType = evidenceKey.evidenceType;

    // START Bug 110431: When language switched to French via preference from
    // the user profile the Task subject line is still English
    bfmFECTaskCrtDetails.oralWrittenLanguage = oralWrittenLanguage.toString();
    bfmFECTaskCrtDetails.evidenceTypeDesc = evidenceTypeDesc;
    bfmFECTaskCrtDetails.feCaseTypeDesc = fecTypeDesc;
    bfmFECTaskCrtDetails.feCaseReference = caseHeaderDtls.caseReference;
    // END Bug 110431

    enactmentStructs.add(bfmFECTaskCrtDetails);

    // Task deadline
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();

    final int taskDeadline = 90;
    final long inputDateTimeInMills =
      Date.getCurrentDate().addDays(taskDeadline).asLong();
    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;
    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;
    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

    final long kProcessInstanceID =
      EnactmentService.startProcessInV3CompatibilityMode(
        BDMConstants.kBDMForeignResidenceEvidenceNotificationTask,
        enactmentStructs);

    bdmUtil.addBDMTask(kProcessInstanceID, bfmFECTaskCrtDetails.evidenceType,
      CuramConst.gkEmpty);

  }

  /**
   * Task 85744: DEV: <Client Oral Communication> <Client Written Communication>
   * Evidence of type <Evidence Type> Foreign Engagement Case <Case Reference>
   * requires approval.
   *
   * Example Task Subject Line: EN Evidence of type Foreign Residence Evidence
   * Foreign Engagement Case 123 requires Approval
   *
   * Trigger: Foreign Residence Period Evidence added to a Foreign Engagenent
   * Case
   * Foreign Residency Period Evidence added to a Foreign Engagenent Case in "In
   * Edit" state for Person/Prospect Person who has a Canadian Benefit
   * application.
   *
   *
   *
   * @param caseKey
   * @param evidenceKey
   * @throws AppException
   * @throws InformationalException
   */
  private void enactFCPTaskWorkFlow(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = caseKey.caseID;
    final CaseHeaderDtls caseHeaderDtls =
      CaseHeaderFactory.newInstance().read(caseHeaderKey);

    final List<Object> enactmentStructs = new ArrayList<>();
    final BDMFECTaskCreateDetails bfmFECTaskCrtDetails =
      new BDMFECTaskCreateDetails();

    bfmFECTaskCrtDetails.participantRoleID = caseHeaderDtls.concernRoleID;
    bfmFECTaskCrtDetails.caseID = caseKey.caseID;
    bfmFECTaskCrtDetails.priority = TASKPRIORITY.NORMAL;

    final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
    bdmfeCaseKey.caseID = caseKey.caseID;
    final BDMFECaseDtls bdmfeCaseDtls = bdmfeCase.read(bdmfeCaseKey);

    final BDMWorkQueueCountryLink bdmWorkQueueCountryLink =
      BDMWorkQueueCountryLinkFactory.newInstance();
    final BDMWorkQueueCountryLinkKey bdmWorkQueueCountryLinkKey =
      new BDMWorkQueueCountryLinkKey();
    bdmWorkQueueCountryLinkKey.countryCode = bdmfeCaseDtls.countryCode;

    // START: Bug 99510: Cant create foreign contribution period/ foreign
    // residence period as ioClientContact. Unhandled exception
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    BDMWorkQueueCountryLinkDtls bdmWorkQueueCountryLinkDtls =
      new BDMWorkQueueCountryLinkDtls();

    bdmWorkQueueCountryLinkDtls =
      bdmWorkQueueCountryLink.read(nfIndicator, bdmWorkQueueCountryLinkKey);

    if (!nfIndicator.isNotFound()) {
      bfmFECTaskCrtDetails.workQueueID =
        bdmWorkQueueCountryLinkDtls.workQueueID;
    }
    // END: Bug 99510: Cant create foreign contribution period/ foreign
    // residence period as ioClientContact. Unhandled exception

    final String currentLoggedInUser = TransactionInfo.getProgramUser();

    if (bfmFECTaskCrtDetails.workQueueID == CuramConst.kLongZero) {
      bfmFECTaskCrtDetails.workQueueID =
        bdmUtil.getWorkQueueIDByProvOfLoggedInUser(currentLoggedInUser);
    }

    // <Client Oral Communication> <Client Written Communication> Evidence of
    // type <Evidence Type> Foreign Engagement Case <Case Reference> requires
    // approval.
    // Example: EN Evidence of type Foreign Residence Evidence Foreign
    // Engagement Case 123 requires Approval
    final StringBuffer taskSubject = new StringBuffer();

    String orallanuage = CuramConst.gkEmpty;
    String writtenlanuage = CuramConst.gkEmpty;
    StringBuffer oralWrittenLanguage = new StringBuffer();

    DynamicEvidenceDataAttributeDtlsList dynamicEvidenceDataAttributeDtlsList =
      new DynamicEvidenceDataAttributeDtlsList();

    final EvidenceDescriptorDtls evidenceDescriptorDtlsKey =
      new EvidenceDescriptorDtls();
    evidenceDescriptorDtlsKey.participantID =
      bfmFECTaskCrtDetails.participantRoleID;
    evidenceDescriptorDtlsKey.evidenceType = PDCConst.PDCCONTACTPREFERENCES;
    evidenceDescriptorDtlsKey.statusCode = EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    dynamicEvidenceDataAttributeDtlsList =
      bdmfeCase.getContactPreferencesEvidenceList(evidenceDescriptorDtlsKey);

    // Introduce a new variable instead of reusing the parameter
    // "dynamicEvidenceDataAttributeDtls". to eliminate sonar error
    DynamicEvidenceDataAttributeDtls dynamicEvidenceDataAttributeDtlsTemp =
      null;

    for (final DynamicEvidenceDataAttributeDtls dynamicEvidenceDataAttributeDtls : dynamicEvidenceDataAttributeDtlsList.dtls) {
      dynamicEvidenceDataAttributeDtlsTemp =
        new DynamicEvidenceDataAttributeDtls();
      dynamicEvidenceDataAttributeDtlsTemp
        .assign(dynamicEvidenceDataAttributeDtls);

      if (dynamicEvidenceDataAttributeDtlsTemp.name
        .equals(BDMConstants.kpreferredOralLanguage)) {

        if (BDMLANGUAGE.ENGLISHL
          .equals(dynamicEvidenceDataAttributeDtlsTemp.value)) {
          orallanuage = BDMConstants.gkLocaleUpperEN;
        } else if (BDMLANGUAGE.FRENCHL
          .equals(dynamicEvidenceDataAttributeDtlsTemp.value)) {
          orallanuage = BDMConstants.gkLocaleUpperFR;
        }

      } else if (dynamicEvidenceDataAttributeDtlsTemp.name
        .equals(BDMConstants.kpreferredWrittenLanguage)) {

        if (BDMLANGUAGE.ENGLISHL
          .equals(dynamicEvidenceDataAttributeDtlsTemp.value)) {
          writtenlanuage = BDMConstants.gkLocaleUpperEN;
        } else if (BDMLANGUAGE.FRENCHL
          .equals(dynamicEvidenceDataAttributeDtlsTemp.value)) {
          writtenlanuage = BDMConstants.gkLocaleUpperFR;
        }

      }

    }

    if (!StringUtil.isNullOrEmpty(orallanuage)
      && !StringUtil.isNullOrEmpty(writtenlanuage)
      && orallanuage.equals(writtenlanuage)) {
      oralWrittenLanguage = new StringBuffer();
      oralWrittenLanguage.append(orallanuage);
    } else if (!StringUtil.isNullOrEmpty(orallanuage)
      && !StringUtil.isNullOrEmpty(writtenlanuage)
      && !orallanuage.equals(writtenlanuage)) {
      oralWrittenLanguage = new StringBuffer();
      oralWrittenLanguage.append(BDMConstants.gkBIL);

    } else {
      oralWrittenLanguage.append(BDMConstants.gkLocaleUpperEN);
    }

    taskSubject.append(oralWrittenLanguage + CuramConst.gkSpace);

    final String evidenceTypeDesc = CodetableUtil.getCodetableDescription(
      CASEEVIDENCE.TABLENAME, evidenceKey.evidenceType);

    final String fecTypeDesc = CodetableUtil.getCodetableDescription(
      PRODUCTCATEGORY.TABLENAME, caseHeaderDtls.integratedCaseType);

    bfmFECTaskCrtDetails.evidenceType = evidenceKey.evidenceType;
    // START Bug 110431: When language switched to French via preference from
    // the user profile the Task subject line is still English
    bfmFECTaskCrtDetails.oralWrittenLanguage = oralWrittenLanguage.toString();
    bfmFECTaskCrtDetails.evidenceTypeDesc = evidenceTypeDesc;
    bfmFECTaskCrtDetails.feCaseTypeDesc = fecTypeDesc;
    bfmFECTaskCrtDetails.feCaseReference = caseHeaderDtls.caseReference;
    // END Bug 110431

    enactmentStructs.add(bfmFECTaskCrtDetails);

    // Task deadline
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();

    final int taskDeadline = 90;
    final long inputDateTimeInMills =
      Date.getCurrentDate().addDays(taskDeadline).asLong();
    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;
    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;
    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);

    final long kProcessInstanceID =
      EnactmentService.startProcessInV3CompatibilityMode(
        BDMConstants.kBDMForeignContributionEvidenceNotificationTask,
        enactmentStructs);

    bdmUtil.addBDMTask(kProcessInstanceID, bfmFECTaskCrtDetails.evidenceType,
      CuramConst.gkEmpty);
  }

  /**
   *
   * This method is invoked when a user tries to modify an evidence. It has
   * custom logic that executes after modifying the evidence.
   *
   * @param case key
   * @param evidence key
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void postModifyEvidence(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    super.postModifyEvidence(caseKey, evidenceKey);
    // check for evidence type and call the required logic to be executed in
    // post modify
    if (evidenceKey.evidenceType.equals(PDCConst.PDCBANKACCOUNT)) {

      // TASK 10932 : Bank Account Evidence Create/Modify Hookpoint
      handleBankAccountIntegrityReview(caseKey, evidenceKey);

      try {
        modifyBankAccountPaymentDestination(evidenceKey);
      } catch (final Exception e) {
        // log the exception and continue
        Trace.kTopLevelLogger.error(BDMConstants.BDM_LOGS_PREFIX
          + " Error modifying associated payment destination for evidence updates for evidenceID: "
          + evidenceKey.evidenceID + " -- " + e.getMessage());
        e.printStackTrace();
      }

    } else if (evidenceKey.evidenceType.equals(PDCConst.PDCADDRESS)) {

      handlePostAddressChange(evidenceKey);
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.BDM_CANADIAN_RESIDENCE_PERIOD)) {
      // 90018 DEV: Evidence- creditable residency period in Canada
      final AdviceKey adviceKey = new AdviceKey();
      adviceKey.adviceContextKeyName = "BDMFECIntegratedCase_home";
      adviceKey.parameters = "caseID=" + caseKey.caseID;
      AdvisorFactory.newInstance().getAdvice(adviceKey);
    } else if (evidenceKey.evidenceType
      .equals(CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT)) {
      endDateCorrespondentCprsOnIC(evidenceKey);
    }
  }

  /**
   * TASK 10932 : Bank Account Evidence Create/Modify Hookpoint. Update the
   * overrideFlag to true on entity BDMBankAccountIntegrityCounter if the count
   * is more than the limit. This method must not be called for any other
   * evidence other than PDC Bank Account Evidence
   *
   * @param caseKey
   * @param evidenceKey
   * @throws AppException
   * @throws InformationalException
   */
  private void handleBankAccountIntegrityReview(final CaseKey caseKey,
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    // Get evidence details
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(evidenceKey);
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;
    // Get account number and sort code from the evidence details
    final String accountNumber =
      dynamicEvidenceDataDetails.getAttribute("accountNumber").getValue();

    // BEGIN TASK 16366 - Bank account evidence, concatenate the financial
    // institution number and branch transition number to set the value of sort
    // code.
    final String finInstitutionNumber = dynamicEvidenceDataDetails
      .getAttribute("financialInstitutionNumber").getValue();
    final String branchTransitNumber = dynamicEvidenceDataDetails
      .getAttribute("branchTransitNumber").getValue();

    final String sortCode = finInstitutionNumber + branchTransitNumber;
    // END TASK 16366 - Bank account evidence

    // set the key and call read method on the entity

    final BDMBankAccountIntegrityCounter bdmBankCounterObj =
      BDMBankAccountIntegrityCounterFactory.newInstance();
    final BDMAccountNumberSortCodeKey accountNumberSortCodeKey =
      new BDMAccountNumberSortCodeKey();
    accountNumberSortCodeKey.accountNumber = accountNumber;
    accountNumberSortCodeKey.bankSortCode = sortCode;
    final NotFoundIndicator nfi = new NotFoundIndicator();
    final BDMBankAccountIntegrityCounterDtls bdmBankAccountIntegrityCounterDtls =
      bdmBankCounterObj.readBySortCodeAccountNumber(nfi,
        accountNumberSortCodeKey);
    // if record is found and the flag is false
    if (!nfi.isNotFound()
      && !bdmBankAccountIntegrityCounterDtls.overrideFlag) {
      final short currentCount = bdmBankAccountIntegrityCounterDtls.count;
      final int inegrityReviewLimit =
        Integer.valueOf(Configuration.getProperty(
          EnvVars.BDM_ENV_INDIVIDUALS_PER_BANK_ACCOUNT_FOR_INTEGRITY_REVIEW));
      // check for the limit
      if (currentCount > inegrityReviewLimit) {
        // call modify on BDMBankAccountIntegrityCounter
        bdmBankAccountIntegrityCounterDtls.overrideFlag = true;
        final BDMBankAccountIntegrityCounterKey bdmBankAccountIntegrityCounterKey =
          new BDMBankAccountIntegrityCounterKey();
        bdmBankAccountIntegrityCounterKey.bdmBankAccountIntegrityCounterID =
          bdmBankAccountIntegrityCounterDtls.bdmBankAccountIntegrityCounterID;
        bdmBankCounterObj.modify(bdmBankAccountIntegrityCounterKey,
          bdmBankAccountIntegrityCounterDtls);
      } // end if (currentCount > inegrityReviewLimit)
    } // end if (!nfi.isNotFound() &&
      // !bdmBankAccountIntegrityCounterDtls.overrideFlag)
  }

  /**
   *
   * This method is invoked when a user tries to remove an evidence. It has
   * custom logic that executes after deleting the evidence.
   *
   * @param case key
   * @param evidence key
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void postRemoveEvidence(final CaseKey caseKey,
    final EIEvidenceKey evKey) throws AppException, InformationalException {

    super.postRemoveEvidence(caseKey, evKey);

    if (evKey.evidenceType.equals(PDCConst.PDCADDRESS)) {

      handlePostAddressChange(evKey);
    } else if (evKey.evidenceType.equals(PDCConst.PDCBANKACCOUNT)) {

      try {
        deleteBankAccountPaymentDestination(evKey);
      } catch (final Exception e) {
        // log the exception and continue to handle unforeseen scenarios with
        // payment destination updates.
        Trace.kTopLevelLogger.error(BDMConstants.BDM_LOGS_PREFIX
          + " Error removing associated payment destination for evidence with evidenceID: "
          + evKey.evidenceID + " -- " + e.getMessage());
        e.printStackTrace();
      }
    } else if (evKey.evidenceType
      .equals(CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT)) {
      removeThirdParyCprAndCorrespondentCprsOnIC(evKey);
    }

  }

  /**
   * This method soft delete the associated bank account payment destination.
   *
   * @param evKey bank account evidence identifier and type key
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  private void deleteBankAccountPaymentDestination(final EIEvidenceKey evKey)
    throws AppException, InformationalException {

    // get the payment destination for the bank account
    final BDMPaymentDestinationDtlsList paymenDestinationList =
      new BDMBankAccountValidation().getPaymentDestinations(evKey);

    // cancel the payment destinations
    for (final BDMPaymentDestinationDtls paymenDestinationDtls : paymenDestinationList.dtls) {

      final BDMPaymentDestinationIDVersionNo paymentDestinationKey =
        new BDMPaymentDestinationIDVersionNo();
      paymentDestinationKey.paymentDestinationID =
        paymenDestinationDtls.paymentDestinationID;
      paymentDestinationKey.versionNo = paymenDestinationDtls.versionNo;

      maintainPaymentDestinationObj
        .cancelEFTDestination(paymentDestinationKey);
    }
  }

  /**
   * This method updates the associated payment destination start and end dates
   * with evidence from and to dates if updated. The payment destination is not
   * updated if account number is updated.
   *
   * @param session
   * @param bankAccountEvd bank account evidence details
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  private void modifyBankAccountPaymentDestination(final EIEvidenceKey evKey)
    throws AppException, InformationalException {

    // get evidence data details for the updated evidence
    final DynamicEvidenceDataDetails updatedEvidenceDataDetails =
      new BDMBankAccountValidation().readDynamicEvidence(evKey);

    // get the evidence descriptor to get the first succession evidence to
    // compare the account number and dates changes
    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      new BDMBankAccountValidation().getEvidenceDescriptorDtls(evKey);

    final SuccessionID successionIDStruct = new SuccessionID();
    successionIDStruct.successionID = evidenceDescriptorDtls.successionID;

    final curam.core.sl.infrastructure.intf.EvidenceController evidenceController =
      EvidenceControllerFactory.newInstance();

    // get first evidence in succession list
    final EvidenceDescriptorDtls firstInSuccession =
      evidenceController.getFirstInSuccession(successionIDStruct);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = firstInSuccession.relatedID;
    evidenceKey.evidenceType = firstInSuccession.evidenceType;

    final DynamicEvidenceDataDetails successionEvidenceDataDetails =
      new BDMBankAccountValidation().readDynamicEvidence(evidenceKey);

    // update the associated payment destination dates if evidence from and to
    // dates are updated and account number is same on both succession and
    // updated evidence
    // No payment destination updates required for the new bank account number
    final String successionAcctNumber =
      successionEvidenceDataDetails.getAttribute("accountNumber").getValue();
    final String updatedAcctNumber =
      updatedEvidenceDataDetails.getAttribute("accountNumber").getValue();

    if (!successionAcctNumber.equalsIgnoreCase(updatedAcctNumber)) {
      return;
    }

    // get the payment destination for the bank account
    final BDMPaymentDestinationDtlsList paymenDestinationList =
      new BDMBankAccountValidation().getPaymentDestinations(evidenceKey);

    if (paymenDestinationList.dtls.isEmpty()) {
      return;
    }

    // get start and end dates on the evidence
    final Date evdStartDate = (Date) new DateConverter()
      .convert(updatedEvidenceDataDetails.getAttribute("fromDate"));

    final Date evdEndDate = (Date) new DateConverter()
      .convert(updatedEvidenceDataDetails.getAttribute("toDate"));

    // check if payment destination dates are different, if yes update dates.
    for (final BDMPaymentDestinationDtls paymenDestinationDtls : paymenDestinationList.dtls) {

      // if evidence dates are not updated
      if (!evdStartDate.after(paymenDestinationDtls.startDate)
        && !evdStartDate.before(paymenDestinationDtls.startDate)
        && !evdEndDate.before(paymenDestinationDtls.endDate)
        && !evdEndDate.after(paymenDestinationDtls.endDate)) {
        continue;
      }

      final BDMUpdateEFTDestinationDetails paymentDestinationUpdateDetails =
        new BDMUpdateEFTDestinationDetails();

      paymentDestinationUpdateDetails.paymentDestinationID =
        paymenDestinationDtls.paymentDestinationID;
      paymentDestinationUpdateDetails.startDate = evdStartDate;
      paymentDestinationUpdateDetails.endDate = evdEndDate;
      paymentDestinationUpdateDetails.versionNo =
        paymenDestinationDtls.versionNo;
      paymentDestinationUpdateDetails.selDestinationID =
        paymenDestinationDtls.destinationID;

      maintainPaymentDestinationObj
        .modifyEFTDestination(paymentDestinationUpdateDetails);
    }
  }

  /**
   * util method to invoke caseDeductions logic on address create/modify/delete
   *
   * @param evKey
   * @throws AppException
   * @throws InformationalException
   */
  private void handlePostAddressChange(final EIEvidenceKey evKey)
    throws AppException, InformationalException {

    try {

      final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey =
        new RelatedIDAndEvidenceTypeKey();
      relatedIDAndEvidenceTypeKey.relatedID = evKey.evidenceID;
      relatedIDAndEvidenceTypeKey.evidenceType = evKey.evidenceType;
      final EvidenceDescriptorDtls evidenceDescriptorDtls =
        EvidenceDescriptorFactory.newInstance()
          .readByRelatedIDAndType(relatedIDAndEvidenceTypeKey);
      final BDMPostAddressChangeKey bdmPostAddressChangeKey =
        new BDMPostAddressChangeKey();
      bdmPostAddressChangeKey.concernRoleID =
        evidenceDescriptorDtls.participantID;
      bdmPostAddressChangeKey.evidenceDescriptorID =
        evidenceDescriptorDtls.evidenceDescriptorID;

      bdmFInancialObj.postAddressChange(bdmPostAddressChangeKey);

      // START, BUG 98671, Fox for Close Task 06
      closeAttachmentTask(evidenceDescriptorDtls);
      // END, BUG 98671, Fox for Close Task 06

    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "Error occured during post address change : "
        + e.getLocalizedMessage());

    }

  }

  // START, BUG 98671, Fox for Close Task 06
  private void
    closeAttachmentTask(final EvidenceDescriptorDtls evidenceDescriptorDtls)
      throws AppException, InformationalException {

    final ListParticipantTaskKey_eo listParticipantTaskKey_eo =
      new ListParticipantTaskKey_eo();
    listParticipantTaskKey_eo.concernRoleTasksKey.concernRoleID =
      evidenceDescriptorDtls.participantID;
    final BDMTasksForConcernAndCaseDetails bdmTasksForConcernAndCaseDetails =
      BDMParticipantFactory.newInstance()
        .listParticipantTask(listParticipantTaskKey_eo);

    for (final BDMTaskSearchDetails bdmTaskSearchDetails : bdmTasksForConcernAndCaseDetails.dtls.dtls) {

      final TaskKey taskKey = new TaskKey();
      taskKey.taskID = bdmTaskSearchDetails.taskID;
      final BizObjAssociationDtlsList bizObjAssociationDtlsList =
        BizObjAssociationFactory.newInstance().searchByTaskID(taskKey);

      for (final BizObjAssociationDtls bizObjAssociationDtls : bizObjAssociationDtlsList.dtls) {

        if (bizObjAssociationDtls.bizObjectType
          .equals(BUSINESSOBJECTTYPE.BDMATTACHMENT)) {
          final Event closeTaskEvent = new Event();
          closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
          closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
          closeTaskEvent.primaryEventData = bdmTaskSearchDetails.taskID;
          EventService.raiseEvent(closeTaskEvent);
        }

      }
    }
  }
  // END, BUG 98671, Fox for Close Task 06

  /**
   * If Check if the third party contact evidence is end dated,
   * also end date all CPRs of type correspondent for this
   * contact on IC's.
   *
   * @param evidenceKey
   * @throws AppException
   * @throws InformationalException
   */
  protected void endDateCorrespondentCprsOnIC(final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

    final Date toDate = (Date) DynamicEvidenceTypeConverter
      .convert(dynamicEvidenceDataDetails.getAttribute(kToDate));

    if (!toDate.isZero()) {
      final CaseParticipantRole cprEntity =
        CaseParticipantRoleFactory.newInstance();

      final Long primaryCaseParticipantRoleID =
        (Long) DynamicEvidenceTypeConverter
          .convert(dynamicEvidenceDataDetails.getAttribute(kCpr));

      final Long thirdPartyCaseParticipantRoleID =
        (Long) DynamicEvidenceTypeConverter
          .convert(dynamicEvidenceDataDetails.getAttribute(kThdPtyCpr));

      final List<CaseParticipantRoleDtls> corCprList =
        bdmUtil.getAllActiveCorrespondentCprsOnICCasesByCrID(
          bdmUtil.getCaseParticipantRoleDtls(
            primaryCaseParticipantRoleID).participantRoleID,
          bdmUtil.getCaseParticipantRoleDtls(
            thirdPartyCaseParticipantRoleID).participantRoleID);

      for (final CaseParticipantRoleDtls corCprDtls : corCprList) {
        // end date if necessary
        if (!toDate.equals(corCprDtls.toDate)) {
          final CaseParticipantRoleKey corCprKey =
            new CaseParticipantRoleKey();
          corCprKey.caseParticipantRoleID = corCprDtls.caseParticipantRoleID;
          final CaseParticipantRole_eoModifyParticipantIDToDate modifyDtls =
            new CaseParticipantRole_eoModifyParticipantIDToDate();
          modifyDtls.participantRoleID = corCprDtls.participantRoleID;
          modifyDtls.toDate = toDate;
          modifyDtls.versionNo = corCprDtls.versionNo;
          cprEntity.modifyParticipantIDToDate(corCprKey, modifyDtls);
        }
      }
    }
  }

  /**
   * Check if the third party contact evidence is end dated,
   * also end date all CPRs of type correspondent for this
   * contact on IC's.
   *
   * @param evidenceKey
   * @throws AppException
   * @throws InformationalException
   */
  protected void removeThirdParyCprAndCorrespondentCprsOnIC(
    final EIEvidenceKey evidenceKey)
    throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(evidenceKey);

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;

    final CaseParticipantRole cprEntity =
      CaseParticipantRoleFactory.newInstance();

    final Long primaryCaseParticipantRoleID =
      (Long) DynamicEvidenceTypeConverter
        .convert(dynamicEvidenceDataDetails.getAttribute(kCpr));

    final Long thirdPartyCaseParticipantRoleID =
      (Long) DynamicEvidenceTypeConverter
        .convert(dynamicEvidenceDataDetails.getAttribute(kThdPtyCpr));

    // cancel the third party contact cpr if not already
    final CaseParticipantRoleKey thdPtyCprKey = new CaseParticipantRoleKey();
    thdPtyCprKey.caseParticipantRoleID = thirdPartyCaseParticipantRoleID;
    final CaseParticipantRoleDtls thdPrtyCprDtls =
      cprEntity.read(new NotFoundIndicator(), thdPtyCprKey);

    if (!RECORDSTATUS.CANCELLED.equals(thdPrtyCprDtls.recordStatus)) {
      final CancelCaseParticipantRoleDetails thdPtyModifyDtls =
        new CancelCaseParticipantRoleDetails();
      thdPtyModifyDtls.recordStatus = RECORDSTATUS.CANCELLED;
      thdPtyModifyDtls.versionNo = thdPrtyCprDtls.versionNo;
      cprEntity.modifyRecordStatus(thdPtyCprKey, thdPtyModifyDtls);
    }

    final List<CaseParticipantRoleDtls> corCprList =
      bdmUtil.getAllActiveCorrespondentCprsOnICCasesByCrID(
        bdmUtil.getCaseParticipantRoleDtls(
          primaryCaseParticipantRoleID).participantRoleID,
        bdmUtil.getCaseParticipantRoleDtls(
          thirdPartyCaseParticipantRoleID).participantRoleID);

    for (final CaseParticipantRoleDtls corCprDtls : corCprList) {

      final CaseParticipantRoleKey corCprKey = new CaseParticipantRoleKey();
      corCprKey.caseParticipantRoleID = corCprDtls.caseParticipantRoleID;
      final CancelCaseParticipantRoleDetails modifyDtls =
        new CancelCaseParticipantRoleDetails();
      modifyDtls.recordStatus = RECORDSTATUS.CANCELLED;
      modifyDtls.versionNo = corCprDtls.versionNo;
      cprEntity.modifyRecordStatus(corCprKey, modifyDtls);
    }
  }

  /**
   * First version created with Bug 107703
   * Error message to show when trying to delete the identifications related to
   * the business rule BR-01 "This record cannot be deleted as it is associated
   * to an Active Liaison or Foreign Application."
   *
   * @param caseKey
   * @param dynamicEvidenceDataDetails
   * @throws AppException
   * @throws InformationalException
   */
  private void validateForeignIdentifierDeletion(final CaseKey caseKey,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails)
    throws AppException, InformationalException {

    // Get the Foreign Identifier Alternate ID
    final String altidReference =
      dynamicEvidenceDataDetails.getAttribute(kAlternateID).getValue();
    if (!altidReference.isEmpty()) {
      // Read Case details to get concernroleID for client
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = caseKey.caseID;
      final CaseHeaderDtls caseHeaderDtls =
        CaseHeaderFactory.newInstance().read(caseHeaderKey);
      // Get list of FEC cases for Client
      final ActiveCasesConcernRoleIDAndTypeKey caseSearchKey =
        new ActiveCasesConcernRoleIDAndTypeKey();
      caseSearchKey.concernRoleID = caseHeaderDtls.concernRoleID;
      caseSearchKey.caseTypeCode = CASETYPECODE.INTEGRATEDCASE;
      caseSearchKey.statusCode = RECORDSTATUS.NORMAL;
      final CaseHeaderDtlsList caseList = CaseHeaderFactory.newInstance()
        .searchActiveCasesByTypeConcernRoleID(caseSearchKey);
      // Get List of Foreign Liaison and Foreign Application for each FEC cases
      // for client
      final curam.ca.gc.bdm.entity.intf.BDMForeignLiaison flObj =
        BDMForeignLiaisonFactory.newInstance();
      for (final CaseHeaderDtls icCaseDtls : caseList.dtls) {
        //// Check Foreign Liaison List for FEC Case
        final BDMReadFLByCaseIDKey readFLByCaseIDKey =
          new BDMReadFLByCaseIDKey();
        readFLByCaseIDKey.caseID = icCaseDtls.caseID;
        final BDMForeignLiaisonDtlsList flDtlsList =
          flObj.readForeignLiaisonByCaseID(readFLByCaseIDKey);
        // iterate over each active ForeignLiaison
        for (final BDMForeignLiaisonDtls foreignLiaisonDtls : flDtlsList.dtls) {
          // If field foreignIdntifier matches with altid , then delete is
          // not allowed.
          if (foreignLiaisonDtls.recordStatus.equals(RECORDSTATUS.NORMAL)
            && altidReference.equals(foreignLiaisonDtls.foreignIdntifier)) {
            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(new AppException(
                BDMEVIDENCE.ERR_BDM_FOREIGN_IDENTIFIER_IDENTIFICATION_CANNOT_BE_DELETED),
                null, InformationalType.kError,
                curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
                0);
            TransactionInfo.getInformationalManager().failOperation();
          } // end if
        } // end for flDtlsList
        // Check Foreign Application List for FEC Case
        final BDMFAKeyStruct3 bdmfaKeyStruct3 = new BDMFAKeyStruct3();
        bdmfaKeyStruct3.caseID = icCaseDtls.caseID;
        final BDMForeignApplicationDtlsList bdmfaDtlsList =
          BDMForeignApplicationFactory.newInstance()
            .readByCaseID(bdmfaKeyStruct3);
        final ConcernRoleAlternateIDKey concernRoleAlternateIDKey =
          new ConcernRoleAlternateIDKey();
        // iterate over each active ForeignLiaison
        for (final BDMForeignApplicationDtls foreignApplicationDtls : bdmfaDtlsList.dtls) {
          // If field foreignIdntifier matches with altid , then delete is
          // not allowed.
          if (foreignApplicationDtls.recordStatus
            .equals(RECORDSTATUS.NORMAL)) {
            // get foreign identifier, it is stored in concernrole alternate
            // table
            if (foreignApplicationDtls.fIdentifier != CuramConst.gkZero) {
              concernRoleAlternateIDKey.concernRoleAlternateID =
                foreignApplicationDtls.fIdentifier;
              final ConcernRoleAlternateIDDtls concernRoleAlternateIDDtls =
                ConcernRoleAlternateIDFactory.newInstance()
                  .read(concernRoleAlternateIDKey);
              // If field foreignIdntifier matches with altidReference , then
              // delete is not allowed.
              if (altidReference
                .equals(concernRoleAlternateIDDtls.alternateID)) {
                ValidationManagerFactory.getManager()
                  .addInfoMgrExceptionWithLookup(new AppException(
                    BDMEVIDENCE.ERR_BDM_FOREIGN_IDENTIFIER_IDENTIFICATION_CANNOT_BE_DELETED),
                    null, InformationalType.kError,
                    curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
                    0);
                TransactionInfo.getInformationalManager().failOperation();
              } // end if
            } // end if
          } // end if
        } // end for bdmfaDtlsList
      } // end for caseList
    } // end if (!altidReference.isEmpty())
  } // end of method validateForeignIdentifierDeletion

}
