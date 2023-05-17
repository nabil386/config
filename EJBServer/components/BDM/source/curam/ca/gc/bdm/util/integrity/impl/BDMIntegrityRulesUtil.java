package curam.ca.gc.bdm.util.integrity.impl;

import curam.ca.gc.bdm.codetable.BDMSIRCHECKRESULT;
import curam.ca.gc.bdm.codetable.BDMSIRMATCHRESULT;
import curam.ca.gc.bdm.entity.fact.BDMBankAccountFactory;
import curam.ca.gc.bdm.entity.struct.BDMBankAccountConcernRoleDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMBankAccountIntegrityDetails;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeKey;
import curam.ca.gc.bdm.facade.bdmworkallocation.struct.BDMTaskSkillTypeList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmuacaseworkerverificationtaskmanagerapi.struct.BDMUACaseWorkerVerificationEnactKey;
import curam.ca.gc.bdm.sl.bdmworkallocation.fact.BDMWorkAllocationTaskFactory;
import curam.ca.gc.bdm.sl.bdmworkallocation.intf.BDMWorkAllocationTask;
import curam.ca.gc.bdm.sl.bdmworkallocation.struct.BDMWorkQueueID;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRBirthCertificate;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRCertifications;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRDeathIndividuals;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRExpiryDate;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRFather;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRIndividual;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRMiscellaneous;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRMother;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRNonMatch;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRProvincialValidation;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRSearchTable;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRSin;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRSpecialAccounts;
import curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos.BDMSINSIRValidation;
import curam.ca.gc.bdm.util.impl.BDMBankAccountDetails;
import curam.ca.gc.bdm.util.impl.BDMBankAccountUtil;
import curam.codetable.BANKACCOUNTSTATUS;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SKILLTYPE;
import curam.core.facade.struct.ProcessInstanceKey;
import curam.core.sl.entity.struct.CaseParticipantRoleDtls;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.BatchProcessingIDList;
import curam.core.struct.SortCodeAccountNumStruct;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.workflow.impl.EnactmentService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BDMIntegrityRulesUtil {

  /**
   * Process flow to create task for Bank Account Integrity review.
   */
  private static final String kBankAccountIntegrityProcess =
    "BDMIntegrityTaskWorkflow";

  /**
   * Get the count of unique concernroleids which are using the given account
   * number and banksort code. This is required for the Bank Account evidence
   * verification.
   *
   * @param session
   * @param accountNumber
   * @param bankSortCode
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static int getBankAccountNumberUseCount(final String accountNumber,
    final String bankSortCode) throws AppException, InformationalException {

    // Search unique count of concernroleids which has this account number and
    // the bank sort code and also the client is getting active benefit or has
    // an open application, check for not closed PDC or not closed application
    // case
    // set the search key struct
    final SortCodeAccountNumStruct searchCountBankAccountKey =
      new SortCodeAccountNumStruct();
    searchCountBankAccountKey.accountNumber = accountNumber;
    searchCountBankAccountKey.bankSortCode = bankSortCode;
    searchCountBankAccountKey.bankAccountStatus = BANKACCOUNTSTATUS.OPEN;
    searchCountBankAccountKey.statusCode = RECORDSTATUS.NORMAL;
    final BDMBankAccountConcernRoleDetailsList concernroleIDList =
      BDMBankAccountFactory.newInstance()
        .searchConcernRoleIDListByAccountNumberAndSortCode(
          searchCountBankAccountKey);
    // list will have the distinct concernrole ids(handled in SQL), return the
    // size of the list
    return concernroleIDList.dtls.size();
  }

  /**
   * Create Task for Bank Account Integrity Review process for specified account
   * number and bank sort code.
   *
   * @param accountNumber
   * @param bankSortCode
   * @throws AppException
   * @throws InformationalException
   */
  public static long createTaskForBankAccountIntegrity(
    final String accountNumber, final String bankSortCode)
    throws AppException, InformationalException {

    // Create the task struct for Bank Account Integrity process
    final BDMBankAccountIntegrityDetails taskDetails =
      new BDMBankAccountIntegrityDetails();
    // Set Account number and BankSortCode
    taskDetails.accountNumber = accountNumber;
    // Call utility to break bankSortCode into two fields.
    final BDMBankAccountDetails sortCodeDetails =
      BDMBankAccountUtil.getInstitutionNumberAndTransitNumber(bankSortCode);
    taskDetails.financialInstitutionNumber =
      sortCodeDetails.getInstitutionNumber();
    taskDetails.transitNumber = sortCodeDetails.getTransitNumber();

    // Task 61139 - BEGIN - Link task to skill type
    taskDetails.skillType = SKILLTYPE.VSG10;

    final BDMTaskSkillTypeKey bdmTaskSkillTypeKey = new BDMTaskSkillTypeKey();
    BDMTaskSkillTypeList bdmTaskSkillTypeList = new BDMTaskSkillTypeList();
    final BDMWorkAllocationTask bdmWorkAllocationTaskObj =
      BDMWorkAllocationTaskFactory.newInstance();

    // Get skillType by documentType
    bdmTaskSkillTypeKey.skillType = SKILLTYPE.VSG10;

    // Get workQueueID by skillType
    BDMWorkQueueID bdmWorkQueueID = new BDMWorkQueueID();

    bdmWorkQueueID =
      bdmWorkAllocationTaskObj.getWorkQueueIDBySkillType(bdmTaskSkillTypeKey);

    taskDetails.workQueueID = bdmWorkQueueID.workQueueID;

    // Get bdmTaskClassificationID by skillType
    bdmTaskSkillTypeList =
      bdmWorkAllocationTaskObj.searchBDMTaskSkillType(bdmTaskSkillTypeKey);

    if (bdmTaskSkillTypeList.dtls.size() > 0) {
      taskDetails.bdmTaskClassificationID =
        bdmTaskSkillTypeList.dtls.get(0).bdmTaskClassificationID;
    }

    // Task deadline
    final long currentDateTimeInMills = Date.getCurrentDate().asLong();
    final int taskDeadline = BDMConstants.kInvestigationReferralDeadlineDays;
    // final int taskDeadline = 30;
    final long inputDateTimeInMills =
      Date.getCurrentDate().addDays(taskDeadline).asLong();
    final long durationMills = inputDateTimeInMills - currentDateTimeInMills;
    final long durationSeconds = durationMills / DateTime.kMilliSecsInSecond;
    final BDMUACaseWorkerVerificationEnactKey bdmuaCaseWorkerVerificationEnactKey =
      new BDMUACaseWorkerVerificationEnactKey();
    bdmuaCaseWorkerVerificationEnactKey.deadLineDurationInSec =
      (int) durationSeconds;

    // Task 61139 - END

    // Create the list we will pass to the enactment service.
    final List<Object> enactmentStructs = new ArrayList<Object>();
    enactmentStructs.add(taskDetails);
    enactmentStructs.add(bdmuaCaseWorkerVerificationEnactKey);
    // Enact work flow to create the appeal deadline Task.
    return EnactmentService.startProcessInV3CompatibilityMode(
      kBankAccountIntegrityProcess, enactmentStructs);
  }

  /**
   * Retrieves the bank account IDs for the unique combination of bank account
   * number and sort code.
   *
   * @return BatchProcessingIDList
   * @throws AppException
   * @throws InformationalException
   */
  public static BatchProcessingIDList
    getBankAccountIntegrityBatchProcessingIDs()
      throws AppException, InformationalException {

    // Call searchAllUniqueBankAccountDetails on Bank Acccount Entity to execute
    // the chunker sql for Bank Account Integrity Batch Job
    return BDMBankAccountFactory.newInstance()
      .searchAllUniqueBankAccountDetails();
  }

  /**
   * Find taskID from processInstanceID for the task created by workflow
   * BDMIntegrityTaskWorkflow. Call DynamicSQL as the factory class for the
   * entity activityinstance is restricted.
   *
   * @param processInstanceID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static long
    getTaskIDFromProcessInstanceID(final long processInstanceID)
      throws AppException, InformationalException {

    // Call dynamic sql on Activity Instance to get the taskID
    final StringBuilder taskIDFetchQuery = new StringBuilder();
    final ProcessInstanceKey processInstancekey = new ProcessInstanceKey();
    processInstancekey.processInstanceID = processInstanceID;
    // SQL Builder
    taskIDFetchQuery
      .append("select taskid into :taskID from activityinstance ");
    taskIDFetchQuery.append(" where PROCESSINSTANCEID = :processInstanceID ");
    taskIDFetchQuery.append(" and taskid is not null ");
    // Return list
    final CuramValueList<TaskKey> valueList =
      DynamicDataAccess.executeNsMulti(TaskKey.class, processInstancekey,
        false, taskIDFetchQuery.toString());
    if (!valueList.isEmpty()) {
      return valueList.get(0).taskID;
    }
    return 0l;
  }

  /**
   * Sets the calculated flad in the SIR Response
   *
   * @param checkResult
   * @param dynamicEvidenceDataDetails
   * @throws AppException
   * @throws InformationalException
   */
  private static void setCalculatedFlagsFromResponseData(
    final BDMSINIntegrityCheckResult checkResult,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails)
    throws AppException, InformationalException {

    // following 3 flags will be set even the SIR Response is unsuccessful
    // mismatchResult
    boolean misMatchFlag = true;
    if (checkResult.getMismatchCheckResult()
      .equals(BDMSINIntegrityCheckConstants.CHECK_RESULT_PASS)) {
      misMatchFlag = false;
    }
    setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
      "mismatchResult", misMatchFlag);

    // identityMatchResult
    final DynamicEvidenceDataAttributeDetails identityMatchResult =
      dynamicEvidenceDataDetails.getAttribute("identityMatchResult");

    final String identityCheckResult =
      BDMSINMismatchCheckUtil.calculateIdentityMatchResult(checkResult);

    DynamicEvidenceTypeConverter.setAttribute(identityMatchResult,
      new CodeTableItem(BDMSIRMATCHRESULT.TABLENAME, identityCheckResult));

    // set default flag as failed flagReviewResult
    setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
      "flagReviewResult", true);

    // check response before setting all these flags below
    if (checkResult.getSIRResponse() == null
      || !checkResult.getSIRResponse().isSINSIRValidatonSuccess()) {

      // Task 55325 Set summary result
      setSummaryResultDetails(dynamicEvidenceDataDetails, false);
      return;
    }
    // SIN flag review
    final BDMSINFlagAndStatusReviewUtil flagReviewUtil =
      new BDMSINFlagAndStatusReviewUtil();
    flagReviewUtil.setSINFlagsAndStatuses(checkResult.getSIRResponse());

    final boolean integrityReviewFlagExist =
      flagReviewUtil.isSINIntegrityReviewFlagExists();

    final boolean confirmIdentityFlagExist =
      flagReviewUtil.isSINConfirmIdentityFlagExists();

    final boolean addressFlagExist = flagReviewUtil.isSINAddressFlagExists();

    boolean flagReviewResult = false;

    if (integrityReviewFlagExist || confirmIdentityFlagExist
      || addressFlagExist) {
      flagReviewResult = true;
    }
    // flagReviewResult
    setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
      "flagReviewResult", flagReviewResult);
    // integrityReviewFlags
    setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
      "integrityReviewFlags", integrityReviewFlagExist);
    // identityReviewFlag
    setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
      "identityReviewFlag", confirmIdentityFlagExist);
    // addressReviewFlag
    setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
      "addressReviewFlag", addressFlagExist);
    // date validation flags
    final BDMSINIntegrityDateCheck dateCheckUtil =
      new BDMSINIntegrityDateCheck();
    final BDMSIRResponseDateCheckFlags dateValidationFalgs =
      dateCheckUtil.calculateSINDateValidationFlags(checkResult);
    // valiadtionResult
    setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
      "valiadtionResult", dateValidationFalgs.isValiadtionFlag());
    // appDateCheckResult
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("appDateCheckResult"),
      dateValidationFalgs.isAppDateCheckFlag());
    // reactivationDateCheckResult
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("reactivationDateCheckResult"),
      dateValidationFalgs.isReactivationDateCheckFlag());
    // dobDateCheckResult
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("dobDateCheckResult"),
      dateValidationFalgs.isDobDateCheckFlag());

    // Task 55325 Set summary result
    if (identityCheckResult.equals(BDMSIRMATCHRESULT.NOTMATCHED)
      || flagReviewResult || dateValidationFalgs.isValiadtionFlag()) {
      setSummaryResultDetails(dynamicEvidenceDataDetails, false);
    } else {
      setSummaryResultDetails(dynamicEvidenceDataDetails, true);
    }
  }

  private static void setSummaryResultDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final boolean fieldValue) throws AppException {

    String resultCode =
      curam.ca.gc.bdm.codetable.BDMSIRCHECKSUMMARYRESULT.SIN_IDENTITY_VERIFICATION_FAIL;
    if (fieldValue) {
      resultCode =
        curam.ca.gc.bdm.codetable.BDMSIRCHECKSUMMARYRESULT.SIN_IDENTITY_VERIFICATION_PASS;
    }
    final DynamicEvidenceDataAttributeDetails flagFieldResult =
      dynamicEvidenceDataDetails.getAttribute("summaryResult");

    DynamicEvidenceTypeConverter.setAttribute(flagFieldResult,
      new CodeTableItem(
        curam.ca.gc.bdm.codetable.BDMSIRCHECKSUMMARYRESULT.TABLENAME,
        resultCode));
  }

  private static void setFlagInDynamicEvidenceDataAttributedetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String fieldName, final boolean fieldValue) throws AppException {

    // calculate code table value from boolean fieldValue
    String resultCode = BDMSIRCHECKRESULT.PASS;
    if (fieldValue) {
      resultCode = BDMSIRCHECKRESULT.FAIL;
    }
    final DynamicEvidenceDataAttributeDetails flagFieldResult =
      dynamicEvidenceDataDetails.getAttribute(fieldName);
    DynamicEvidenceTypeConverter.setAttribute(flagFieldResult,
      new CodeTableItem(BDMSIRCHECKRESULT.TABLENAME, resultCode));
  }

  /**
   * Creates the SIR Response evidence based on the response.
   *
   * @param cprPDC
   * @param checkResult
   * @throws AppException
   * @throws InformationalException
   */
  public static void createSINSIRResponseEvidence(
    final CaseParticipantRoleDtls cprPDC,
    final BDMSINIntegrityCheckResult checkResult)
    throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDMSINIDENTIFICATIONSTATUS;

    final EvidenceTypeDefDAO etDefDAO =
      GuiceWrapper.getInjector().getInstance(EvidenceTypeDefDAO.class);

    final EvidenceTypeVersionDefDAO etVerDefDAO =
      GuiceWrapper.getInjector().getInstance(EvidenceTypeVersionDefDAO.class);

    final EvidenceTypeDef evidenceType =
      etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);

    final EvidenceTypeVersionDef evTypeVersion =
      etVerDefDAO.getActiveEvidenceTypeVersionAtDate(evidenceType,
        Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);

    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute("participant");
    DynamicEvidenceTypeConverter.setAttribute(participant,
      Long.valueOf(cprPDC.caseParticipantRoleID));

    // set all calculated flags from resposne data
    setCalculatedFlagsFromResponseData(checkResult,
      dynamicEvidenceDataDetails);

    // set all request and response evidence data attributes
    setData(checkResult, dynamicEvidenceDataDetails);

    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();
    evidenceDescriptorInsertDtls.participantID = cprPDC.participantRoleID;
    evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.caseID = cprPDC.caseID;

    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();
    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = cprPDC.participantRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.CASEREVIEW;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

    final EIEvidenceKey sirEvidenceKey =
      evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);
    checkResult.setSirResponseEvidenceID(sirEvidenceKey.evidenceID);
  }

  /**
   *
   * @param checkResult
   * @param dynamicEvidenceDataDetails
   * @throws AppException
   */
  private static void setData(final BDMSINIntegrityCheckResult checkResult,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails)
    throws AppException {

    try {
      setRequestData(checkResult, dynamicEvidenceDataDetails);
      setResponseData(checkResult, dynamicEvidenceDataDetails);
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "SIN SIR Validation Response Data Map Error - " + e.getMessage());
      e.printStackTrace();
    }

  }

  /**
   *
   * @param checkResult
   * @param dynamicEvidenceDataDetails
   * @throws AppException
   */
  private static void setResponseData(
    final BDMSINIntegrityCheckResult checkResult,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails)
    throws AppException {

    final BDMSINSIRValidation validationResult =
      checkResult.getSIRResponse().getValidatedSINResults();

    if (null == validationResult) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "SIN SIR Validation Response - validated SIN results is null.");
      return;
    }

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resMatchFlag"),
      validationResult.getSsMatchFlag());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resDormantUpdateDate"),
      validationResult.getSsDormantUpdateDate());

    final BDMSINSIRIndividual ssIndividual =
      validationResult.getSsIndividual();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resSexCode"),
      ssIndividual.getNcPersonSexCode());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resTwinsInd"),
      ssIndividual.getSsTwinFlagIndicator());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resMultipleFlagInd"),
      ssIndividual.getSsMultipleFlagIndicator());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resDuplicateFlagInd"),
      ssIndividual.getSsDuplicateFlagIndicator());

    // SearchTable
    final BDMSINSIRSearchTable ssSearchTable =
      ssIndividual.getSsSearchTable();

    if (ssSearchTable != null) {
      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidenceDataDetails.getAttribute("resGivenName"),
        ssSearchTable.getNcPersonGivenName());

      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidenceDataDetails.getAttribute("resMiddleName"),
        ssSearchTable.getNcPersonMiddleName());
      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidenceDataDetails.getAttribute("resSurName"),
        ssSearchTable.getNcPersonSurName());
      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidenceDataDetails.getAttribute("resParentsMaidenName"),
        ssSearchTable.getSsParentMaidenName());

      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidenceDataDetails.getAttribute("resDateOfBirth"),
        Date.fromISO8601(ssSearchTable.getNcPersonBirthDate()));

      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidenceDataDetails.getAttribute("resSINIdentification"),
        ssSearchTable.getPerPersonSINIdentification());
    }

    // SSSIN
    final BDMSINSIRSin ssSINSIRSIN = ssIndividual.getSsSIN();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resSINIdentificationStatus"),
      ssSINSIRSIN.getNcIdentificationStatus());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resSINIssueDate"),
      ssSINSIRSIN.getSsIssueDate());

    if (null != ssSINSIRSIN.getSsSINRestrictions()
      && !ssSINSIRSIN.getSsSINRestrictions().isEmpty()) {

      final List<String> restrictionsList =
        ssSINSIRSIN.getSsSINRestrictions();

      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidenceDataDetails.getAttribute("resSINRestrictions"),
        restrictionsList.stream().collect(Collectors.joining(",")));
    }

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resDormantFlag"),
      ssSINSIRSIN.getSsDormantFlag() == null ? ""
        : ssSINSIRSIN.getSsDormantFlag());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resDormantEffectiveDate"),
      ssSINSIRSIN.getSsDateDormantEffective());

    // SSSIN
    final BDMSINSIRCertifications ssCertifications =
      ssSINSIRSIN.getSsSINCertifications();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resCitizenshipCertifiedInd"),
      ssCertifications.getSsCitizenshipCertificationFlag());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resCPPCertifiedInd"),
      ssCertifications.getSsCPPCertificationFlag());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resFossCertifiedInd"),
      ssCertifications.getSsFOSSCertificationFlag());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resBDMCertifiedInd"),
      ssCertifications.getSsOASCertificationFlag());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resOtherCertifiedInd"),
      ssCertifications.getSsOtherCertificationFlag());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resProvincialCertifiedInd"),
      ssCertifications.getSsProvincialCertificationFlag());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resRRQCertifiedInd"),
      ssCertifications.getSsRRQCertificationFlag());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resSINCertifiedInd"),
      ssCertifications.getSsSINCertificationFlag());

    final BDMSINSIRNonMatch ssNonMatch = validationResult.getSsNonMatch();

    if (ssNonMatch != null) {

      setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
        "resSINIdentificationMatch", false);

      setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
        "resGivenNameMatch", ssNonMatch.getSsGivenName());

      setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
        "resSurnameMatch", ssNonMatch.getSsSurname());

      setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
        "resDateOfBirthMatch", ssNonMatch.getSsDateOfBirth());

      setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
        "resParentsMatch", ssNonMatch.getSsParents());

    } else {

      setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
        "resSINIdentificationMatch", false);

      setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
        "resGivenNameMatch", false);

      setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
        "resSurnameMatch", false);

      setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
        "resDateOfBirthMatch", false);

      setFlagInDynamicEvidenceDataAttributedetails(dynamicEvidenceDataDetails,
        "resParentsMatch", false);
    }

    final BDMSINSIRMiscellaneous ssMiscellaneous =
      validationResult.getSsMiscellaneous();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resMiscType"),
      ssMiscellaneous.getSsMiscType() == null ? ""
        : ssMiscellaneous.getSsMiscType());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resMiscEffectiveDate"),
      ssMiscellaneous.getNcEffectiveDate());

    final BDMSINSIRSpecialAccounts ssSpecAccount =
      validationResult.getSsSpecialAccounts();

    final String ssPecialAccountCodeType =
      ssSpecAccount.getSsSpecialAccountCodeType() == null ? ""
        : ssSpecAccount.getSsSpecialAccountCodeType();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resSpecialAccountCodeType"),
      ssPecialAccountCodeType);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails
        .getAttribute("resSpecialAccountEffectiveDate"),
      ssSpecAccount.getSsSpecialAccountEffectiveDate());

    final BDMSINSIRExpiryDate ssExpiryDate =
      validationResult.getSsExpiryDate();

    if (ssExpiryDate.getSsSINExpirationDate() != null)
      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidenceDataDetails.getAttribute("resSINExpiryDate"),
        Date.fromISO8601(ssExpiryDate.getSsSINExpirationDate()));

    if (ssExpiryDate.getSsImmigrationExpirationDate() != null)
      DynamicEvidenceTypeConverter.setAttribute(
        dynamicEvidenceDataDetails.getAttribute("resImmigrationExpiryDate"),
        Date.fromISO8601(ssExpiryDate.getSsImmigrationExpirationDate()));

    final BDMSINSIRDeathIndividuals ssDeathIndividuals =
      validationResult.getSsDeathIndividuals();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resPersonLivingInd"),
      ssDeathIndividuals.getNcPersonLivingIndicator());

    final String stopAppFlag = getStopApplicationFlag(ssPecialAccountCodeType,
      ssDeathIndividuals.getNcPersonLivingIndicator(),
      ssSINSIRSIN.getNcIdentificationStatus());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resStopApplication"),
      stopAppFlag);

    final String integrityReviewFlag = getIntegrityReviewFlag(
      ssPecialAccountCodeType, ssExpiryDate.getSsSINExpirationDate());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resIntegrityReview"),
      integrityReviewFlag);

    final String investigationReferralFlag =
      getInvestigationFlag(ssPecialAccountCodeType);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resInvestigationReferral"),
      investigationReferralFlag);

    // externalize text and real locale specific value
    final String dormantFlag = ssSINSIRSIN.getSsDormantFlag() == null ? ""
      : ssSINSIRSIN.getSsDormantFlag();
    final String confirmIdentityFlag =
      flagCheckInResponseData(BDMSINIntegrityCheckConstants.SIR_FLAG_DORMANT,
        dormantFlag) ? "Dormant" : "";

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resConfirmIdentity"),
      confirmIdentityFlag);

    final String ssMiscType = ssMiscellaneous.getSsMiscType() == null ? ""
      : ssMiscellaneous.getSsMiscType();
    final String confirmAddressFlag = getConfirmAddressFlag(ssMiscType);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resConfirmAddress"),
      confirmAddressFlag);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resDeathDate"),
      ssDeathIndividuals.getNcPersonDeathDate());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resProvinceCode"),
      ssDeathIndividuals.getCanProvinceCode());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resDeathCertifiedInd"),
      ssDeathIndividuals.getSsDeathCertificationFlag());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails
        .getAttribute("resDeathCertificateIdentification"),
      ssDeathIndividuals.getSsDeathCertificateIdentification());

    final BDMSINSIRProvincialValidation ssProvincialValidation =
      validationResult.getSsProvincialValidation();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resProvValActionCode"),
      ssProvincialValidation.getSsProvincialValidationActionCode());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails
        .getAttribute("resProvValDocumentIdentification"),
      ssProvincialValidation.getSsOtherDocumentIdentification());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resProvValDocumentType"),
      ssProvincialValidation.getSsDocumentType());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails
        .getAttribute("resProvValDocumentCategoryCode"),
      ssProvincialValidation.getSsDocumentCategoryCode());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resCanProvinceCode"),
      ssProvincialValidation.getCanProvinceCode());

    final BDMSINSIRFather ssFather = ssProvincialValidation.getSsFather();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resFatherGivenName"),
      ssFather.getNcPersonGivenName());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resFatherSurName"),
      ssFather.getNcPersonSurName());

    final BDMSINSIRMother ssMother = ssProvincialValidation.getSsMother();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resMothersGivenName"),
      ssMother.getNcPersonGivenName());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("resMothersSurName"),
      ssMother.getNcPersonSurName());

    final BDMSINSIRBirthCertificate ssBirthCert =
      ssProvincialValidation.getSsBirthCertificate();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails
        .getAttribute("resBirthCertificateIdentification"),
      ssBirthCert.getSsBirthCertificateIdentification());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails
        .getAttribute("resBirthCertificateEffectiveDate"),
      ssBirthCert.getSsDocumentEffectiveDate());
  }

  /**
   *
   * @param checkResult
   * @param dynamicEvidenceDataDetails
   * @throws AppException
   */
  private static void setRequestData(
    final BDMSINIntegrityCheckResult checkResult,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails)
    throws AppException {

    final BDMSINSIRSearchTable request = checkResult.getSIRRequest();

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("reqSIN"),
      request.getPerPersonSINIdentification());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("reqFirstName"),
      request.getNcPersonGivenName());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("reqMiddleName"),
      request.getNcPersonMiddleName());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("reqLastName"),
      request.getNcPersonSurName());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("reqParentsLastNameAtBirth"),
      request.getSsParentMaidenName());

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidenceDataDetails.getAttribute("reqDateOfBirth"),
      request.getNcPersonBirthDate());
  }

  /**
   *
   * @param specialAccountCodeTypeValue
   * @param deathStatusValue
   * @param sinStatusCodeValue
   */
  private static String getStopApplicationFlag(
    final String specialAccountCodeTypeValue, final Integer deathStatusValue,
    final Integer sinStatusCodeValue) {

    // externalize and read values for locales
    final String fraudStr =
      flagCheckInResponseData(BDMSINIntegrityCheckConstants.SIR_FLAG_FRAUD,
        specialAccountCodeTypeValue) ? "Fraud" : "";
    final String frozenStr =
      flagCheckInResponseData(BDMSINIntegrityCheckConstants.SIR_FLAG_FROZEN,
        specialAccountCodeTypeValue) ? "Frozen" : "";
    final String deathStr =
      BDMSINIntegrityCheckConstants.SIR_FLAG_DEATH == deathStatusValue
        ? "Death" : "";
    final String cancelledStr =
      BDMSINIntegrityCheckConstants.SIR_FLAG_CANCELLED == sinStatusCodeValue
        ? "Cancelled" : "";
    final String voidStr =
      BDMSINIntegrityCheckConstants.SIR_FLAG_VOID == sinStatusCodeValue
        ? "Void" : "";

    final String stopApplicationStatus =
      Stream.of(fraudStr, frozenStr, deathStr, cancelledStr, voidStr)
        .filter(s -> s != null && !s.isEmpty())
        .collect(Collectors.joining(","));

    return stopApplicationStatus;
  }

  /**
   *
   * @param specialAccountCodeTypeValue
   * @param ssSINExpirationDate
   * @return
   */
  private static String getIntegrityReviewFlag(
    final String specialAccountCodeTypeValue,
    final String ssSINExpirationDate) {

    // Task 61464 - The flag name must be Previous investigation
    // stringValue = "Previous Investigation Fraud";
    final String prevInvestigation = flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_PREVIOUS_INVESTIGATION,
      specialAccountCodeTypeValue) ? "Previous Investigation " : "";
    // Expired
    final String expired = !StringUtil.isNullOrEmpty(ssSINExpirationDate)
      && Date.getDate(ssSINExpirationDate).before(Date.getCurrentDate())
        ? "Expired" : "";

    final String integrityReviewStatus = Stream.of(prevInvestigation, expired)
      .filter(s -> s != null && !s.isEmpty())
      .collect(Collectors.joining(","));

    return integrityReviewStatus;
  }

  /**
   *
   * @param specialAccountCodeTypeValue
   * @return
   */
  private static String

    getInvestigationFlag(final String specialAccountCodeTypeValue) {

    // externalize and read values for locales
    final String underReview = flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_UNDER_REVIEW_FRAUD,
      specialAccountCodeTypeValue) ? "Under Review Fraud" : "";
    final String duplicate = flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_DUPLICATE,
      specialAccountCodeTypeValue) ? "Duplicate" : "";

    final String investigationStatus =
      Stream.of(underReview, duplicate).filter(s -> s != null && !s.isEmpty())
        .collect(Collectors.joining(","));

    return investigationStatus;
  }

  /**
   *
   * @param miscellaneousTypeFlagValue
   * @return
   */
  private static String
    getConfirmAddressFlag(final String miscellaneousTypeFlagValue) {

    // externalize and read values for locales
    final String documentReturned = flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_DOCUMENT_RETURNED,
      miscellaneousTypeFlagValue) ? "Document Returned" : "";

    final String cardReturned = flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_CARD_RETURNED,
      miscellaneousTypeFlagValue) ? "Card Returned" : "";

    final String letterReturned = flagCheckInResponseData(
      BDMSINIntegrityCheckConstants.SIR_FLAG_CONFIRMATION_LETTER_RETURNED,
      miscellaneousTypeFlagValue) ? "Confirmation Letter Returned" : "";

    final String addressStatus =
      Stream.of(documentReturned, cardReturned, letterReturned)
        .filter(s -> s != null && !s.isEmpty())
        .collect(Collectors.joining(","));

    return addressStatus;
  }

  /**
   * Checks if the given flag is present in the list of response flag or not.
   * Return false if the response flag is null or empty.
   *
   * @param responseFlag
   * @param flag
   * @return
   */
  public static boolean flagCheckInResponseData(final int flag,
    final String responseFlag) {

    if (!StringUtil.isNullOrEmpty(responseFlag)
      && responseFlag.contains("" + flag)) {
      return true;
    }
    return false;
  }
}
