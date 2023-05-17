package curam.ca.gc.bdmoas.rest.bdmoascraapi.impl;

import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMAUDITAPITYPE;
import curam.ca.gc.bdm.codetable.BDMInterfaceNameCode;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.impl.BDMInterfaceConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMClientData;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMIncomeDetail;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMPersonName;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMReferenceData;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMStatus;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails.BDMAPIAuditDetailsBuilder;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMInterfaceLogger;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSummaryDetails;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSummaryDetailsList;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.ca.gc.bdmoas.rest.bdmoascraapi.struct.BDMOASCRARestRequest;
import curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos.BDMOASCRAIndividualIncomeDetails;
import curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos.BDMOASDeathMatchInbound;
import curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos.BDMOASGISTakeUpInbound;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.DUPLICATESTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.message.INFRASTRUCTURE;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.DateTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.httpclient.HttpStatus;

public class BDMOASCRAAPI
  implements curam.ca.gc.bdmoas.rest.bdmoascraapi.intf.BDMOASCRAAPI {

  @Inject
  BDMInterfaceLogger logger;

  @Inject
  BDMAPIAuditUtil auditUtil;

  private final String kMethod_craDeathMatch =
    "BDMOASCRAAPI.processCRADeathmatch() ";

  private final String kMethod_craGISTakeUp =
    "BDMCRAAPI.processCRAGISTakeUp()";

  private final String kMethod_craGisAssessment =
    "BDMOASCRAAPI.processCRAGISAssessment";

  private BDMOASCRAHelper craHelper;

  // To Inject the logger
  public BDMOASCRAAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
    craHelper = new BDMOASCRAHelper();
  }

  /**
   * Setter for CRA helper class (to pass in dependency).
   *
   * @param service - BDMOASCRAHelper class
   */
  public void setCRAService(final BDMOASCRAHelper service) {

    this.craHelper = service;
  }

  /**
   * Process error to return a 400 response and update the summary report table.
   *
   * @param errorMessage - Message for the HTTP 400
   * @param isATask - Indicator for if a task has been created
   * @param incomeYear - Income tax year for incoming request
   * @param auditDetails - BDMAPIAuditDetails
   * @throws AppException
   * @throws InformationalException
   */
  private void createGISCRAErrorResponse(final String interfaceName,
    final String errorMessage, final boolean isATask, final Date incomeYear,
    final BDMAPIAuditDetails auditDetails)
    throws AppException, InformationalException {

    craHelper.updateSummaryReportTable(interfaceName, false, isATask,
      incomeYear);
    BDMRestUtil.throwHTTP400Status(errorMessage, auditDetails);

  }

  /**
   * Validate the elements from an incoming request to be used by the GIS Take-Up
   * interface.
   *
   * @param clientData - client data coming from incoming request
   * @param auditDetails - BDMAuditDetails object
   * @return Income Taxation year for request
   * @throws AppException
   * @throws InformationalException
   */
  public Date validateGISCRAAssessmentClientData(
    final BDMOASCRAIndividualIncomeDetails clientData,
    final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException, ParseException {

    Date incomeYear = null;

    // Extract income taxation year for Summary Report
    Trace.kTopLevelLogger.debug("Extract income taxation year");
    final String requestIncomeYear =
      clientData.getIncomeTaxationYear().getYearDate();
    if (StringUtil.isNullOrEmpty(requestIncomeYear)) {
      createGISCRAErrorResponse(BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
        "Income Taxation Year is missing for request", false, null,
        bdmapiAuditDetails);
    }

    // Convert Income Taxation Year
    try {
      incomeYear = Date.getFromJavaUtilDate(
        new SimpleDateFormat("yyyy").parse(requestIncomeYear));
    } catch (final ParseException e) {
      Trace.kTopLevelLogger.error(e);
      createGISCRAErrorResponse(BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
        "Income Taxation Year is invalid for request", false, incomeYear,
        bdmapiAuditDetails);
    }

    // Validate the SIN Format
    Trace.kTopLevelLogger.debug("Validate SIN format");
    if (!craHelper.validateSINFormat(
      clientData.getClient().getPersonSINIdentification().getSin())) {
      createGISCRAErrorResponse(BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
        "Missing or invalid format for SIN", false, incomeYear,
        bdmapiAuditDetails);
    }

    // Validate Year and Month from date of birth
    Trace.kTopLevelLogger.debug("Validate date of birth");
    if (!craHelper.validateDateFormat(
      clientData.getClient().getDateOfBirth().getDate(),
      BDMOASConstants.kDOB)) {
      createGISCRAErrorResponse(BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
        "Missing or invalid format for Date of Birth", false, incomeYear,
        bdmapiAuditDetails);
    }

    // Extract Person Name List from payload
    Trace.kTopLevelLogger.debug("Extract person surname");
    final List<BDMPersonName> requestPersonNameList =
      clientData.getClient().getPersonNameList();
    if (requestPersonNameList == null || requestPersonNameList.isEmpty()) {
      createGISCRAErrorResponse(BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
        "Missing or invalid Person Surname", false, incomeYear,
        bdmapiAuditDetails);
    }

    // Extract Person Name from Person Name List
    final BDMPersonName requestPersonName = requestPersonNameList.get(0);
    if (requestPersonName == null) {
      createGISCRAErrorResponse(BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
        "Missing or invalid Person Surname", false, incomeYear,
        bdmapiAuditDetails);
    }

    // Extract Person Surname from Person Name
    final String requestPersonSurname = requestPersonName.getPersonSurname();
    if (StringUtil.isNullOrEmpty(requestPersonSurname)
      || requestPersonSurname.trim().length() == 0) {
      createGISCRAErrorResponse(BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
        "Missing or invalid Person Surname", false, incomeYear,
        bdmapiAuditDetails);
    }

    // Extract CRA account status and validate
    Trace.kTopLevelLogger.debug("Extract cra account status");
    final String requestPersonStatus = clientData.getClient()
      .getPersonStatus().getStatusCode().getReferenceDataName();
    if (StringUtil.isNullOrEmpty(requestPersonStatus)
      || requestPersonStatus.trim().length() == 0) {
      createGISCRAErrorResponse(BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
        "Missing or invalid CRA Account Status", false, incomeYear,
        bdmapiAuditDetails);
    }

    // Extract request marital status code from payload
    Trace.kTopLevelLogger.debug("Extract marital status");
    final String requestMaritalStatus = clientData.getClient()
      .getMaritalStatus().getStatusCode().getReferenceDataID();
    if (StringUtil.isNullOrEmpty(requestMaritalStatus)
      || requestMaritalStatus.trim().length() == 0
      || craHelper.mapCRAMaritalStatusEntry(requestMaritalStatus) == null) {
      createGISCRAErrorResponse(BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
        "Missing or invalid Marital Status", false, incomeYear,
        bdmapiAuditDetails);
    }

    return incomeYear;

  }

  public BDMOASPersonSummaryDetails
    searchMatchingPersonDetailsForGISCRAAssessment(
      final String requestPersonSurname, final Date requestCRADOBDate,
      final String requestCRASin, final Date incomeYear,
      final BDMAPIAuditDetails bdmapiAuditDetails)
      throws AppException, InformationalException {

    // Search by Sin, Surname and Date of Birth
    final BDMOASPersonSummaryDetails curamPersonDtls =
      new BDMOASPersonSummaryDetails();
    final BDMOASPersonSummaryDetails requestCraPersonDtls =
      new BDMOASPersonSummaryDetails();
    requestCraPersonDtls.lastName = requestPersonSurname;
    requestCraPersonDtls.dateOfBirth = requestCRADOBDate;

    final BDMOASPersonSummaryDetailsList curamMatchingSinDtlsList =
      craHelper.searchPersonBySIN(requestCRASin);

    BDMOASPersonSummaryDetails matchingPersonDtls = null;
    for (int i = 0; i < curamMatchingSinDtlsList.dtls.size(); i++) {

      // Only records that match search criteria - see "BDM-IBM Curam Interface Design
      // Document - CRA GIS Take-UP , Assessment & Reassessment.docx"
      if (craHelper.doesRecordMatch(curamMatchingSinDtlsList.dtls.get(i),
        requestCRADOBDate, requestPersonSurname)) {

        if (matchingPersonDtls == null || matchingPersonDtls.duplicateStatus
          .equals(DUPLICATESTATUS.MARKED)) {
          // If no matching records found yet or if the previous matching record is a
          // duplicate
          matchingPersonDtls =
            curamMatchingSinDtlsList.dtls.get(i).deepClone();
        } else if (!curamMatchingSinDtlsList.dtls.get(i).duplicateStatus
          .equals(DUPLICATESTATUS.MARKED)) {
          // Multiple records have been found that are not marked as duplicates
          BDMRestUtil.throwHTTP400Status(
            "Duplicate records found. Specify a valid SIN, DOB, Surname",
            bdmapiAuditDetails);
        }
      }
    }

    return matchingPersonDtls;

  }

  /*
   * Receive income changes from CRA for clients that need to reassess for income
   * tested benefits in Curam system
   *
   * @param request - curam.ca.gc.bdm.interfaces.struct.BDMRestRequestIdentifier
   */
  @Override
  public void processCRAGISAssessment(final BDMOASCRARestRequest request)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.debug("POST /cra/gis_assessment");
    boolean isSuccessful = true;
    boolean isATask = false;
    Date incomeYear = null;

    final long startTime = System.currentTimeMillis();
    final BDMAPIAuditDetailsBuilder bdmapiAuditDetailsBuilder =
      new BDMAPIAuditDetailsBuilder();

    bdmapiAuditDetailsBuilder.setMethod(kMethod_craGisAssessment)
      .setRequestTransactionDateTime(DateTime.getCurrentDateTime())
      .setApiType(BDMAUDITAPITYPE.BDMINBOUND).setRequestObject(request);

    final BDMAPIAuditDetails bdmapiAuditDetails =
      bdmapiAuditDetailsBuilder.build();

    BDMOASGISTakeUpInbound requestPayload = null;

    try {
      // Conversion
      Trace.kTopLevelLogger.debug("Deserialize data");
      if (StringUtil.isNullOrEmpty(request.niemPayload)) {
        throw new NullPointerException();
      }

      requestPayload = BDMRestUtil.convertFromJSON(request.niemPayload,
        BDMOASGISTakeUpInbound.class);

      bdmapiAuditDetailsBuilder.setRelatedID(requestPayload.getId());

      incomeYear = validateGISCRAAssessmentClientData(
        requestPayload.getData().getIndividualIncome(), bdmapiAuditDetails);

    } catch (final JsonSyntaxException | NullPointerException
      | ParseException e) {
      logger.logRequest(kMethod_craGisAssessment, null, requestPayload);
      createGISCRAErrorResponse(BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
        "Invalid request body", false, incomeYear, bdmapiAuditDetails);
    } catch (final AppException e) {
      logger.logRequest(kMethod_craGisAssessment, requestPayload.getId(),
        requestPayload);
      throw e;
    }

    // Log the request against the uuid received in the request payload
    logger.logRequest(kMethod_craGisAssessment, requestPayload.getId(),
      requestPayload);

    try {
      final Date requestCRADOBDate =
        Date.getDate(BDMDateUtil.dateFormatter(requestPayload.getData()
          .getIndividualIncome().getClient().getDateOfBirth().getDate()));

      final String requestPersonSurname =
        requestPayload.getData().getIndividualIncome().getClient()
          .getPersonNameList().get(0).getPersonSurname();

      final String requestCRASin =
        requestPayload.getData().getIndividualIncome().getClient()
          .getPersonSINIdentification().getSin();

      Trace.kTopLevelLogger.debug("Searching curam for matching records");
      final BDMOASPersonSummaryDetails matchingPersonDtls =
        searchMatchingPersonDetailsForGISCRAAssessment(requestPersonSurname,
          requestCRADOBDate, requestCRASin, incomeYear, bdmapiAuditDetails);

      if (matchingPersonDtls != null) {
        Trace.kTopLevelLogger.debug("Found matching record");

        // If CRA account is Inactive, create Inactive Client Task
        Trace.kTopLevelLogger.debug("Check that the CRA account is active");
        if (requestPayload.getData().getIndividualIncome().getClient()
          .getPersonStatus().getStatusCode().getReferenceDataName()
          .equals(BDMOASConstants.BDM_OAS_CRA_ACCOUNT_STATUS_INACTIVE)) {
          isATask = true;
          Trace.kTopLevelLogger.debug("Creating inactive CRA account task");
          craHelper.createInactiveClientTask(requestCRASin, 0l);
        }

        // Extract and validate the date of death is not present
        Trace.kTopLevelLogger
          .debug("Extracting and validating date of death");
        if (requestPayload.getData().getIndividualIncome().getClient()
          .getDateOfDeath() != null) {

          Trace.kTopLevelLogger
            .debug("Check if date of death has been provided");
          final String requestDOD = requestPayload.getData()
            .getIndividualIncome().getClient().getDateOfDeath().getDate();
          if (!requestDOD.equals("0")) {
            if (!craHelper.validateDateFormat(requestDOD,
              BDMOASConstants.kDOD)) {
              createGISCRAErrorResponse(
                BDMInterfaceNameCode.CRA_GIS_REASSESSMENT,
                "Invalid Death Of Death", false, incomeYear,
                bdmapiAuditDetails);
            }

            // Convert String date of birth to Date object
            final String isoCRADOD = BDMDateUtil.dateFormatter(requestDOD);
            final Date requestCRADODDate = Date.getDate(isoCRADOD);

            // If date of death month and year does not match Curam records then update the
            // date Of death
            // and create Date Of Death Mismatch task
            Trace.kTopLevelLogger
              .debug("Check if date of death matches curam records");
            if (!craHelper.matchMonth(matchingPersonDtls.dateOfDeath,
              requestCRADODDate)
              || !craHelper.matchYear(matchingPersonDtls.dateOfDeath,
                requestCRADODDate)) {
              isATask = true;
              Trace.kTopLevelLogger.debug("Updating date of death");
              craHelper.updateDateOfDeath(matchingPersonDtls,
                requestCRADODDate);
              Trace.kTopLevelLogger
                .debug("Creating date of death mismatch task");
              craHelper.createDateOfDeathMismatchTask(matchingPersonDtls,
                requestCRASin);
            }

            final Map<String, String> attributes = new HashMap<>();

            attributes.put("dateOfBirth", requestCRADOBDate.toString());
            attributes.put("dateOfDeath", requestCRADODDate.toString());

            final BDMEvidenceUtil evidenceUtil = new BDMEvidenceUtil();
            final List<DynamicEvidenceDataDetails> evidenceDtls =
              evidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
                matchingPersonDtls.concernRoleID,
                CASEEVIDENCE.OAS_CRA_BIRTH_DEATH);
            if (evidenceDtls.size() == 0) {
              craHelper.createEvidence(matchingPersonDtls.concernRoleID,
                CASEEVIDENCE.OAS_CRA_BIRTH_DEATH,
                CASEEVIDENCEEntry.OAS_CRA_BIRTH_DEATH, attributes);
            } else {
              craHelper.modifyEvidences(evidenceDtls, attributes,
                CASEEVIDENCE.OAS_CRA_BIRTH_DEATH,
                matchingPersonDtls.concernRoleID);
            }
          }

        }

        // Extract request marital status code from payload
        Trace.kTopLevelLogger
          .debug("Extracting and validating marital status");
        final String incomingMappedMaritalStatus =
          craHelper.mapCRAMaritalStatusEntry(
            requestPayload.getData().getIndividualIncome().getClient()
              .getMaritalStatus().getStatusCode().getReferenceDataID());
        final Map<String, String> maritalEvidenceAttributes = new HashMap<>();
        maritalEvidenceAttributes.put(BDMOASConstants.kMaritalStatus,
          incomingMappedMaritalStatus);

        final BDMEvidenceUtil evidenceUtil = new BDMEvidenceUtil();
        final List<DynamicEvidenceDataDetails> evidenceDtls =
          evidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
            matchingPersonDtls.concernRoleID,
            CASEEVIDENCE.OAS_CRA_MARITAL_STATUS);
        if (evidenceDtls.size() == 0) {
          craHelper.createEvidence(matchingPersonDtls.concernRoleID,
            CASEEVIDENCE.OAS_CRA_MARITAL_STATUS,
            CASEEVIDENCEEntry.OAS_CRA_MARITAL_STATUS,
            maritalEvidenceAttributes);
        } else {
          craHelper.modifyEvidences(evidenceDtls, maritalEvidenceAttributes,
            CASEEVIDENCE.OAS_CRA_MARITAL_STATUS,
            matchingPersonDtls.concernRoleID);
        }

        Trace.kTopLevelLogger.debug("Performing income block calculations");
        final List<BDMIncomeDetail> incomeRecords = requestPayload.getData()
          .getIndividualIncome().getIndividualIncomeDetail();
        craHelper.createIncomeEvidence(requestPayload, matchingPersonDtls,
          incomeYear);

        // Extract bankruptcy category pre/post-bankruptcy
        Trace.kTopLevelLogger
          .debug("Extract bankruptcy category pre/post-bankruptcyh");
        final BDMReferenceData requestBankruptcyCategory = requestPayload
          .getData().getIndividualIncome().getIncomeBankruptcyCode();

        // Create Bankruptcy Flag task if Bankruptcy Category is present
        if (requestBankruptcyCategory != null) {
          if (!StringUtil.isNullOrEmpty(
            requestBankruptcyCategory.getReferenceDataName())) {
            isATask = true;
            Trace.kTopLevelLogger.debug("Creating bankruptcy flag task");
            craHelper.createBankruptcyFlagTask(requestCRASin, 0l);
          } else {
            isSuccessful = false;
            BDMRestUtil.throwHTTP400Status("Invalid request body",
              bdmapiAuditDetails);
          }
        }

      } else {
        isSuccessful = false;
        Trace.kTopLevelLogger.debug("Creating bankruptcy flag task");
        craHelper.createPersonMismatchTask(requestCRASin, 0l);
        BDMRestUtil.throwHTTP404Status("SIN, DOB, Surname",
          bdmapiAuditDetails);

      }

    } catch (final JsonSyntaxException | NullPointerException
      | IllegalArgumentException e) {
      isSuccessful = false;
      Trace.kTopLevelLogger.error(e);
      BDMRestUtil.throwHTTP400Status("Invalid request body",
        bdmapiAuditDetails);
    } finally {
      craHelper.updateSummaryReportTable(
        BDMInterfaceNameCode.CRA_GIS_REASSESSMENT, isSuccessful, isATask,
        incomeYear);
    }
  }

  /**
   * Validate the elements from an incoming request to be used by the GIS Take-Up
   * interface.
   *
   * @param clientData - client data coming from incoming request
   * @param auditDetails - BDMAuditDetails object
   * @return Income Taxation year for request
   * @throws AppException
   * @throws InformationalException
   */
  public Date validateGISTakeUpElements(
    final BDMOASCRAIndividualIncomeDetails clientData,
    final BDMAPIAuditDetails auditDetails)
    throws AppException, InformationalException {

    Date incomeYear = null;

    // Validate Income Taxation Year
    Trace.kTopLevelLogger.debug("Validate income taxation year");
    if (clientData.getIncomeTaxationYear() == null) {
      createGISTakeUpErrorResponse(
        "Income Taxation Year is missing for request", false, incomeYear,
        auditDetails);
    }

    // Convert Income Taxation Year
    try {
      incomeYear = Date.getFromJavaUtilDate(new SimpleDateFormat("yyyy")
        .parse(clientData.getIncomeTaxationYear().getYearDate()));
    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e);
      createGISTakeUpErrorResponse(
        "Income Taxation Year is invalid for request", false, incomeYear,
        auditDetails);
    }

    // Validate DOD
    Trace.kTopLevelLogger.debug("Check that there is no DOD");
    if (clientData.getClient().getDateOfDeath() != null) {
      createGISTakeUpErrorResponse("Date of Death provided for GIS Take-Up",
        false, incomeYear, auditDetails);
    }

    // Validate DOB format
    Trace.kTopLevelLogger.debug("Validate DOB");
    if (clientData.getClient().getDateOfBirth() == null || !craHelper
      .validateDateFormat(clientData.getClient().getDateOfBirth().getDate(),
        BDMConstants.kDOB)) {
      createGISTakeUpErrorResponse(
        "Missing or invalid format for Date of Birth", false, incomeYear,
        auditDetails);
    }

    // Validate SIN format
    Trace.kTopLevelLogger.debug("Validate SIN");
    if (clientData.getClient().getPersonSINIdentification() == null
      || !craHelper.validateSINFormat(
        clientData.getClient().getPersonSINIdentification().getSin())) {
      createGISTakeUpErrorResponse("Missing or invalid format for SIN", false,
        incomeYear, auditDetails);
    }

    final String clientSIN =
      clientData.getClient().getPersonSINIdentification().getSin();

    // Check for active CRA account
    Trace.kTopLevelLogger.debug("Check that the CRA account is active");
    if (clientData.getClient().getPersonStatus() == null || !clientData
      .getClient().getPersonStatus().getStatusCode().getReferenceDataName()
      .equals(BDMOASConstants.BDM_OAS_CRA_ACCOUNT_STATUS_ACTIVE)) {
      craHelper.createInactiveClientTask(clientSIN, 0l);
      createGISTakeUpErrorResponse(
        "Missing client active status or client provided in request is not active",
        true, incomeYear, auditDetails);
    }

    // Check CRA bankruptcy flag
    Trace.kTopLevelLogger.debug("Check that there is no CRA bankruptcy flag");
    if (clientData.getIncomeBankruptcyCode() != null) {
      craHelper.createBankruptcyFlagTask(clientSIN, 0l);
      createGISTakeUpErrorResponse(
        "Missing pre/post-bankruptcy flag or client has pre/post-bankruptcy flag set",
        true, incomeYear, auditDetails);
    }

    // Check Marital Status
    Trace.kTopLevelLogger.debug("Check Marital Status provided");
    final BDMStatus maritalStatus = clientData.getClient().getMaritalStatus();
    if (maritalStatus == null || craHelper.mapCRAMaritalStatusEntry(
      maritalStatus.getStatusCode().getReferenceDataID()) == null) {
      createGISTakeUpErrorResponse("Missing or invalid marital status", false,
        incomeYear, auditDetails);
    }

    // Check income details
    Trace.kTopLevelLogger
      .debug("Check that Income Details have been provided");
    if (clientData.getIndividualIncomeDetail() == null) {
      createGISTakeUpErrorResponse("Missing income details for client", false,
        incomeYear, auditDetails);
    }

    return incomeYear;
  }

  /**
   * Process error to return a 400 response and update the summary report table.
   *
   * @param errorMessage - Message for the HTTP 400
   * @param isATask - Indicator for if a task has been created
   * @param incomeYear - Income tax year for incoming request
   * @param auditDetails - BDMAPIAuditDetails
   * @throws AppException
   * @throws InformationalException
   */
  private void createGISTakeUpErrorResponse(final String errorMessage,
    final boolean isATask, final Date incomeYear,
    final BDMAPIAuditDetails auditDetails)
    throws AppException, InformationalException {

    craHelper.updateSummaryReportTable(BDMInterfaceNameCode.CRA_GIS_TAKE_UP,
      false, isATask, incomeYear);
    BDMRestUtil.throwHTTP400Status(errorMessage, auditDetails);

  }

  /**
   * Process CRA GIS Take-Up request.
   *
   * @param request - Incoming API request
   */
  @Override
  public void processCRAGISTakeUp(final BDMOASCRARestRequest request)
    throws AppException, InformationalException {

    BDMOASGISTakeUpInbound requestData = null;
    Date incomeYear = Date.getCurrentDate();
    final long startTime = System.currentTimeMillis();
    final BDMAPIAuditDetailsBuilder auditBuilder =
      new BDMAPIAuditDetailsBuilder();

    BDMAPIAuditDetails auditDetails =
      auditBuilder.setMethod(kMethod_craGISTakeUp)
        .setRequestTransactionDateTime(DateTime.getCurrentDateTime())
        .setRequestObject(request).setApiType(BDMAUDITAPITYPE.BDMINBOUND)
        .build();

    try {
      // Conversion
      Trace.kTopLevelLogger.debug("Deserialize data");
      if (request.niemPayload.isEmpty() || request.niemPayload == null) {
        throw new NullPointerException();
      }
      requestData = BDMRestUtil.convertFromJSON(request.niemPayload,
        BDMOASGISTakeUpInbound.class);
      auditDetails = auditBuilder.setRelatedID(requestData.getId()).build();

      incomeYear = validateGISTakeUpElements(
        requestData.getData().getIndividualIncome(), auditDetails);
    } catch (final JsonSyntaxException | NullPointerException e) {
      logger.logRequest(this.kMethod_craGISTakeUp, null, requestData);
      createGISTakeUpErrorResponse("Invalid request body", false, incomeYear,
        auditDetails);
    } catch (final AppException e) {
      logger.logRequest(this.kMethod_craGISTakeUp, requestData.getId(),
        requestData);
      throw e;
    }

    // Log the request against the uuid received in the request payload
    logger.logRequest(this.kMethod_craGISTakeUp, requestData.getId(),
      requestData);

    try {
      // Process
      processClientDataForGISTakeUp(requestData, incomeYear, auditDetails);

      // Report
      craHelper.updateSummaryReportTable(BDMInterfaceNameCode.CRA_GIS_TAKE_UP,
        true, false, incomeYear);
    } catch (final Exception e) {
      if (!e.getClass().getCanonicalName()
        .equals(AppException.class.getCanonicalName())) {
        BDMRestUtil.auditErrorResponse(
          new AppException(INFRASTRUCTURE.ID_UNHANDLED, e),
          HttpStatus.SC_INTERNAL_SERVER_ERROR, auditDetails);
        createGISTakeUpErrorResponse(e.getMessage(), false, incomeYear,
          auditDetails);
      } else {
        throw e;
      }

    }

    // log the performance time in milliseconds
    logger.logRestAPIPerf(this.kMethod_craGISTakeUp,
      System.currentTimeMillis() - startTime, "");
  }

  /**
   * Process the client data received for the CRA GIS Take-Up request.
   *
   * @param requestData - client data
   * @param incomeYear - year of income request
   * @throws AppException
   * @throws InformationalException
   */
  private void processClientDataForGISTakeUp(
    final BDMOASGISTakeUpInbound requestData, final Date incomeYear,
    final BDMAPIAuditDetails auditDetails)
    throws AppException, InformationalException {

    // Store client data in variable to avoid long chaining references
    final BDMClientData clientRequest =
      requestData.getData().getIndividualIncome().getClient();

    // Search for user in Curam
    Trace.kTopLevelLogger.debug(
      "Search for record in Curam tables (+ post-retrieval filtering)");

    // Look for records in Curam table
    final BDMOASPersonSummaryDetailsList records = craHelper
      .searchPersonBySIN(clientRequest.getPersonSINIdentification().getSin());

    // If there aren't any records found
    if (records.dtls.size() == 0) {
      craHelper.updateSummaryReportTable(BDMInterfaceNameCode.CRA_GIS_TAKE_UP,
        false, false, incomeYear);
      BDMRestUtil.throwHTTP404Status("client (SIN, DOB and Surname)",
        auditDetails);
    }

    // Convert request DOB to a date so it can be used for comparison w/ dates from
    // records
    Date requestDateOfBirth = null;
    try {
      final java.util.Date processedDateOfBirth =
        new SimpleDateFormat("yyyy-MM-dd")
          .parse(clientRequest.getDateOfBirth().getDate());
      requestDateOfBirth = Date.getFromJavaUtilDate(processedDateOfBirth);
    } catch (final ParseException e) {
      createGISTakeUpErrorResponse("Cannot parse DOB", false, incomeYear,
        auditDetails);
    }

    // Compare request data to records retrieved from Curam
    final BDMOASPersonSummaryDetails recordOfInterest =
      craHelper.findMatchingRecord(records, requestDateOfBirth,
        clientRequest.getPersonNameList().get(0).getPersonSurname());

    // If there isn't a matched filtered record (or duplicates found)
    if (recordOfInterest == null
      || recordOfInterest.duplicateStatus.equals(DUPLICATESTATUS.MARKED)) {
      if (recordOfInterest != null) {
        craHelper.createPersonMismatchTask(
          clientRequest.getPersonSINIdentification().getSin(), 0l);
      }
      craHelper.updateSummaryReportTable(BDMInterfaceNameCode.CRA_GIS_TAKE_UP,
        false, recordOfInterest != null, incomeYear);
      BDMRestUtil.throwHTTP404Status("client (SIN, DOB and Surname)",
        auditDetails);
    }

    // Update the marital status
    Trace.kTopLevelLogger.debug("Update the marital status");

    final String incomingMappedMaritalStatus =
      craHelper.mapCRAMaritalStatusEntry(clientRequest.getMaritalStatus()
        .getStatusCode().getReferenceDataID());

    if (incomingMappedMaritalStatus != null) {
      final Map<String, String> maritalEvidenceAttributes = new HashMap<>();
      maritalEvidenceAttributes.put(BDMOASConstants.kMaritalStatus,
        incomingMappedMaritalStatus);

      // Check if evidence exists and create/modify accordingly
      final BDMEvidenceUtil evidenceUtil = new BDMEvidenceUtil();
      final List<DynamicEvidenceDataDetails> evidenceDtls =
        evidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(
          recordOfInterest.concernRoleID,
          CASEEVIDENCE.OAS_CRA_MARITAL_STATUS);
      if (evidenceDtls.size() == 0) {
        craHelper.createEvidence(recordOfInterest.concernRoleID,
          CASEEVIDENCE.OAS_CRA_MARITAL_STATUS,
          CASEEVIDENCEEntry.OAS_CRA_MARITAL_STATUS,
          maritalEvidenceAttributes);
      } else {
        craHelper.modifyEvidences(evidenceDtls, maritalEvidenceAttributes,
          CASEEVIDENCE.OAS_CRA_MARITAL_STATUS,
          recordOfInterest.concernRoleID);
      }
    }

    // Save data in staging tables
    Trace.kTopLevelLogger.debug("Save data to staging tables");

    // Insert Transaction data into CM entities
    craHelper.createIncomeEvidence(requestData, recordOfInterest, incomeYear);
  }

  /**
   * Rest API to process any record sent by CRA.
   * It takes the data and matches with the existing participant record in Cúram
   * database.
   * It saves the not matched records in database and creates a task in an
   * unassigned workqueue.
   *
   * @param request
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void processCRADeathMatch(final BDMOASCRARestRequest request)
    throws AppException, InformationalException {

    boolean isSuccessfullMatch = true;

    try {
      // get the request in JSON format
      final BDMOASDeathMatchInbound requestPayload = BDMRestUtil
        .convertFromJSON(request.niemPayload, BDMOASDeathMatchInbound.class);

      // get the CRA Date of Death
      final String craDeathDate =
        requestPayload.getData().getClientData().getDateOfDeath().getDate();

      // get the CRA Date of Birth
      final String craBirthDate =
        requestPayload.getData().getClientData().getDateOfBirth().getDate();

      // get the CRA SIN Identification
      final String craSin = requestPayload.getData().getClientData()
        .getPersonSINIdentification().getSin();

      String invalidParam = null;

      // from the list of personNames search for the first person's lastName
      final List<BDMPersonName> personNameList =
        requestPayload.getData().getClientData().getPersonNameList();

      final String craPersonSurName =
        personNameList.get(0).getPersonSurname();

      // validate the Date of Death Format obtained from requestPayload - BDMCRADate
      if (!craHelper.validateDateFormat(craDeathDate, BDMConstants.kDOD)) {
        invalidParam = BDMConstants.kDOD;

      } else if (!craHelper.validateDateFormat(craBirthDate,
        BDMConstants.kDOB)) {
        // validate the Date of Birth Format obtained from requestPayload - BDMCRADate
        invalidParam = BDMConstants.kDOB;

      } else if (!craHelper.validateSINFormat(craSin)) {
        // validate the SIN Format obtained from requestPayload - BDMClientData POJO
        invalidParam = BDMConstants.kSIN;

      } else if (!craHelper.paramNotNullOrEmpty(craPersonSurName)) {
        // validate the SurName obtained from requestPayload - BDMClientData POJO
        invalidParam = BDMConstants.kSURNAME;

      } else {
        Trace.kTopLevelLogger.debug(BDMConstants.BDM_LOGS_PREFIX
          + BDMConstants.kValid + BDMConstants.kDOD + BDMConstants.kDOB
          + BDMConstants.kSIN + BDMConstants.kSURNAME);
      }

      if (invalidParam != null) {
        craHelper.errorTypeHandler(invalidParam);
      }

      final String isoCRABirthDate = BDMDateUtil.dateFormatter(craBirthDate);
      final String isoCRADeathDate = BDMDateUtil.dateFormatter(craDeathDate);

      // search from Curam database,clientSin is alernate_id and date is curam_date
      BDMOASPersonSummaryDetailsList personSINMatchDtlsList =
        new BDMOASPersonSummaryDetailsList();
      final BDMOASPersonSummaryDetailsList personAllCriteriaMatchDtlsList =
        new BDMOASPersonSummaryDetailsList();
      final BDMOASPersonSummaryDetails craPersonDtls =
        new BDMOASPersonSummaryDetails();
      final BDMOASPersonSummaryDetails curamPersonDtls =
        new BDMOASPersonSummaryDetails();
      BDMOASPersonSummaryDetails matchingPersonDtls = null;
      boolean isDODMatch = false;

      // Get a list of people whose sin matches
      personSINMatchDtlsList = craHelper.searchPersonBySIN(craSin);

      craPersonDtls.dateOfBirth = Date.getDate(isoCRABirthDate);
      craPersonDtls.lastName = craPersonSurName;

      // for Each person on the SIN match list check if the ,
      // first 5 alphabetic characters of Surname, DOB Month and DOB Year. If 3 out of
      // these 4 data elements match, system must consider this record to be a
      // successful match
      if (personSINMatchDtlsList.dtls.size() > 0) {

        for (int i = 0; i < personSINMatchDtlsList.dtls.size(); i++) {
          curamPersonDtls.dateOfBirth =
            personSINMatchDtlsList.dtls.get(i).dateOfBirth;
          curamPersonDtls.dateOfDeath =
            personSINMatchDtlsList.dtls.get(i).dateOfDeath;
          curamPersonDtls.lastName =
            personSINMatchDtlsList.dtls.get(i).lastName;
          curamPersonDtls.duplicateStatus =
            personSINMatchDtlsList.dtls.get(i).duplicateStatus;

          if (craHelper.isSuccessfulPersonMatch(curamPersonDtls,
            craPersonDtls)) {
            matchingPersonDtls =
              personSINMatchDtlsList.dtls.get(i).deepClone();

            personAllCriteriaMatchDtlsList.dtls.add(i, matchingPersonDtls);
          }
        }
      }

      // If personAllCriteriaMatchDtlsList returns zero or multiple matched records,
      // call BDMCRAHelper.createCRADeathMismatch() method of the helper class with
      // personNotMatchedInd set to true.
      switch (personAllCriteriaMatchDtlsList.dtls.size()) {
        case 1:
          // If the person matched is a duplicate create task for person Mismatch error
          // If it's a duplicate then it will have DS1, if it isn't null, if it is
          // unmarked, then DS2
          if (!personAllCriteriaMatchDtlsList.dtls.get(0).duplicateStatus
            .equals(DUPLICATESTATUS.MARKED)) {
            isDODMatch = craHelper.isDODMatched(
              personAllCriteriaMatchDtlsList.dtls.get(0).dateOfDeath,
              Date.getDate(isoCRADeathDate));
            // Match date of Death param1-dodNotMatchedInd, param2- personNotMatchedInd
            // if date of death matched and person
            if (!isDODMatch) {
              craHelper.createCRADeathMismatch(true, false, curamPersonDtls);
              Trace.kTopLevelLogger.debug(BDMConstants.BDM_LOGS_PREFIX
                + "person match found but dod did not match for concernRoleID"
                + personAllCriteriaMatchDtlsList.dtls.get(0).concernRoleID);
              isSuccessfullMatch = false;
            } else {
              // If person matched and dod matches the matching process must end
              isSuccessfullMatch = true;
              Trace.kTopLevelLogger.debug(BDMConstants.BDM_LOGS_PREFIX
                + "person match found and DOD match for concernRoleID"
                + personAllCriteriaMatchDtlsList.dtls.get(0).concernRoleID);
            }
          }
        break;

        case 2:
          Trace.kTopLevelLogger.debug(BDMConstants.BDM_LOGS_PREFIX
            + personAllCriteriaMatchDtlsList.dtls.get(0).duplicateStatus
              .equals(DUPLICATESTATUS.MARKED)
            + "for concernRoleID"
            + personAllCriteriaMatchDtlsList.dtls.get(0).concernRoleID);
          if (personAllCriteriaMatchDtlsList.dtls.get(0).duplicateStatus
            .equals(DUPLICATESTATUS.MARKED)) {

            // If person matched and is a duplicate create mismatch task
            isSuccessfullMatch = false;
            craHelper.createCRADeathMismatch(false, false, curamPersonDtls);

          } else {
            isSuccessfullMatch = false;
            craHelper.createCRADeathMismatch(isDODMatch, true, craPersonDtls);
          }

        break;

        default:
          isSuccessfullMatch = false;
          craHelper.createCRADeathMismatch(isDODMatch, true, craPersonDtls);
      }

      // update the database with a record
      craHelper.createSummaryData(BDMInterfaceNameCode.CRA_DEATH_MATCH,
        isSuccessfullMatch, Date.getDate(isoCRADeathDate),
        new Random().nextLong());

      // Log the request against the uuid received in the request payload
      logger.logRequest(kMethod_craDeathMatch, requestPayload.getId(),
        requestPayload);

    } catch (final AppException appException) {
      throw appException;
    } catch (final Exception ex) {
      final AppException appException =
        new AppException(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
      appException.arg(BDMInterfaceConstants.BDM_INVALID_REQUEST);
      throw appException;
    }
  }

}
