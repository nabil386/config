package curam.ca.gc.bdmoas.rest.bdmoascraapi.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMInterfaceNameCode;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.entity.bdmsummary.fact.BDMInterfaceSummaryFactory;
import curam.ca.gc.bdm.entity.bdmsummary.intf.BDMInterfaceSummary;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMCRADeathMatchSearchKey;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMInterfaceSummaryDtls;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMInterfaceSummaryDtlsList;
import curam.ca.gc.bdm.entity.bdmsummary.struct.BDMInterfaceSummaryKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINKey;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.impl.BDMInterfaceConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMIncomeDetail;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMRelatedPerson;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMStatus;
import curam.ca.gc.bdmoas.codetable.BDMOASCRAACCOUNTSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASCRAINCOMESTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASCRALINEITEMTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASCRAMARITALSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASCRAPERSONMATCH;
import curam.ca.gc.bdmoas.codetable.BDMOASCRATRANSACTIONCODE;
import curam.ca.gc.bdmoas.codetable.BDMOASCRATRANSACTIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASCRATRANSSUBCODE;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRADataFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRAIncomeDataFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRATransactionFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRADataDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionConcernRoleStatusKey;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionDtlsList;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionKey;
import curam.ca.gc.bdmoas.entity.fact.BDMOASPersonFactory;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSummaryDetails;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSummaryDetailsList;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASCraIncomeConstants;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.ca.gc.bdmoas.sl.cra.transaction.impl.BDMOASCRATransactionProcessor;
import curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos.BDMOASCRAIndividualIncomeDetails;
import curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos.BDMOASGISTakeUpInbound;
import curam.ca.gc.bdmoas.util.impl.BDMOASEvidenceUtil;
import curam.codetable.CASETYPECODE;
import curam.codetable.DUPLICATESTATUS;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.fact.PersonFactory;
import curam.core.fact.PersonSnapshotFactory;
import curam.core.intf.PersonSnapshot;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.TaskCreateDetails;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.PersonSnapshotDtls;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Money;
import curam.util.type.UniqueID;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * Helper class for all the CRA Interfaces
 *
 */

public class BDMOASCRAHelper {

  /**
   * This method is to search a person by SIN number,First name, Last name and
   * date of birth.
   * It returns the list of the matched persons details.
   *
   * @param sin
   * @return BDMPersonSummaryDetailsList
   *
   * @throws AppException
   * @throws InformationalException
   */
  public BDMOASPersonSummaryDetailsList searchPersonBySIN(final String sin)
    throws AppException, InformationalException {

    // return list
    BDMOASPersonSummaryDetailsList personDtlsList =
      new BDMOASPersonSummaryDetailsList();

    final BDMPersonSINKey bdmPersonSinKey = new BDMPersonSINKey();
    bdmPersonSinKey.sin = sin;
    // querying the database
    personDtlsList =
      BDMOASPersonFactory.newInstance().searchPersonBySIN(bdmPersonSinKey);
    Trace.kTopLevelLogger
      .debug(BDMConstants.BDM_LOGS_PREFIX + personDtlsList.dtls.size()
        + " match(es) found with the following criteria: sin");

    return personDtlsList;

  }

  /*
   * This method is to This method is to check the format of the SIN number
   * provided and return true if the SIN is in the numeric format and if it nine
   * digits long.
   *
   * @param SIN
   *
   * @return boolean
   *
   * @throws AppException
   *
   * @throws InformationalException
   */

  public boolean validateSINFormat(final String sin)
    throws AppException, InformationalException {

    if (!paramNotNullOrEmpty(sin))
      return false;

    final String trimmedVal = sin.trim();
    // check if SIN is in right format and 9 digits long
    try {
      Long.parseLong(trimmedVal);
      if (!trimmedVal.matches("[0-9]{9}")) {
        Trace.kTopLevelLogger.error("Invalid SIN Format");
        return false;
      } else
        return true;
    } catch (final NumberFormatException e) {
      Trace.kTopLevelLogger.error(e);
      return false;
    }
  }

  /*
   * This method is to This method is to check the format of the date.
   * It should be in the format yyyy-mm-dd
   *
   * @param date
   *
   * @return boolean
   *
   * @throws AppException
   *
   * @throws InformationalException
   */

  public boolean validateDateFormat(final String date, final String dateType)
    throws AppException, InformationalException {

    if (!paramNotNullOrEmpty(date))
      return false;

    final String trimmedVal = date.trim();

    final SimpleDateFormat sdf = new SimpleDateFormat(
      BDMConstants.YYYY_MM_DD_DATE_FORMAT_DASH_DELIMITER);
    sdf.setLenient(false);

    try {
      sdf.parse(trimmedVal);
      return true;
    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e);
      return false;
    }

  }

  /*
   * This method is to compare the date of death of an existing person in Curam
   * with that of the one received from CRA. It returns true if the month and year
   * match.
   *
   * @param curamDeathDate from person in curam Database
   *
   * @param craDeathDate from person in CRA response
   *
   * @return boolean
   *
   * @throws AppException
   *
   * @throws InformationalException
   */

  public boolean isDODMatched(final Date curamDeathDate,
    final Date craDeathDate) throws AppException, InformationalException {

    final boolean isMonthMatched = matchMonth(curamDeathDate, craDeathDate);
    final boolean isYearMatched = matchYear(curamDeathDate, craDeathDate);

    return isMonthMatched && isYearMatched;
  }

  /*
   * This method creates a task when date of death does not match or when a person
   * could not be found in Curam.
   *
   * @param dodNotMatchedInd
   *
   * @param personNotMatchedInd
   *
   * @param personDtls
   *
   * @return void
   *
   * @throws AppException
   *
   * @throws InformationalException
   */

  public void createCRADeathMismatch(final boolean dodNotMatchedInd,
    final boolean personNotMatchedInd,
    final BDMOASPersonSummaryDetails curamPersonDtls)
    throws AppException, InformationalException {

    // TO DO
    // populateTaskDetails(taskCreateDetails, dodNotMatchedInd,
    // personNotMatchedInd);
    // createTask(taskCreateDetailsd);
  }

  /*
   * This method is to populate the task details based on the passed-on indicator
   * and with the assigneeType as workqueue.
   *
   * @param taskCreateDetails
   *
   * @param dodNotMatchedInd
   *
   * @param personNotMatchedInd
   *
   * @return void
   *
   * @throws AppException
   *
   * @throws InformationalException
   */

  public void populateTaskDetails(final TaskCreateDetails taskCreateDetails,
    final boolean dodNotMatchedInd, final boolean personNotMatchedInd) {

    // tbd

  }

  /*
   * This method is to create the task
   *
   * @param taskCreateDetails
   *
   * @return void
   *
   * @throws AppException
   *
   * @throws InformationalException
   */

  public void createTask(final TaskCreateDetails taskCreateDetailsd) {

    // tbd

  }

  /**
   * Remove accented character
   *
   * @param input
   * @return
   */
  public String unaccent(final String input) {

    final String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
    final Pattern pattern =
      Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(temp).replaceAll("");
  }

  /**
   * this method compares the first 5 characters of a surname and returns true if
   * they are the same and false if they are not
   *
   * @param curamSurname
   * @param craSurname
   * @return a boolean indicating if the surnames are the same
   */
  public boolean firstFiveLettersSurnameMatch(final String curamSurname,
    final String craSurname) {

    String curamPrsSurname, craPrsSurname;
    // Replace accented characters with english characters
    curamPrsSurname = unaccent(curamSurname.toUpperCase());
    craPrsSurname = unaccent(craSurname.toUpperCase());

    // Remove all special characters
    curamPrsSurname = curamPrsSurname.replaceAll("[^A-Za-z]", "");
    craPrsSurname = craPrsSurname.replaceAll("[^A-Za-z]", "");

    // Compare the first 5 letters of both names, or the entire name if it is
    // shorter than 5 characters
    final int lengthCuramSurname = Math.min(curamPrsSurname.length(),
      BDMInterfaceConstants.BDM_SURNAME_MATCH_LENGTH);
    final int lengthCraSurname = Math.min(craPrsSurname.length(),
      BDMInterfaceConstants.BDM_SURNAME_MATCH_LENGTH);
    return curamPrsSurname.substring(0, lengthCuramSurname)
      .equalsIgnoreCase(craPrsSurname.substring(0, lengthCraSurname));
  }

  /**
   * this method compares two dates by only taking their months into consideration
   *
   *
   * @param curamPersonDate
   * @param craPersonDate
   * @return true if the dates have the same month, false otherwise
   */
  public boolean matchMonth(final Date curamPersonDate,
    final Date craPersonDate) {

    // extract month from date
    final int curamPersonMonth =
      curamPersonDate.getCalendar().get(Calendar.MONTH);
    final int craPersonMonth =
      craPersonDate.getCalendar().get(Calendar.MONTH);

    // compare month
    return curamPersonMonth == craPersonMonth;
  }

  /**
   * this method compares two dates by only taking their years into consideration
   *
   *
   * @param curamPersonDate
   * @param craPersonDate
   * @return true if the dates have the same year, false otherwise
   */
  public boolean matchYear(final Date curamPersonDate,
    final Date craPersonDate) {

    // extract year from date
    final int curamPersonYear =
      curamPersonDate.getCalendar().get(Calendar.YEAR);
    final int craPersonYear = craPersonDate.getCalendar().get(Calendar.YEAR);

    // compare year
    return curamPersonYear == craPersonYear;

  }

  /**
   * this method compares a curam person details with its details from CRA by
   * looking at the SIN, the month of the date of birth, the year of the date of
   * birth and the 5 first letters of the surname
   *
   * @param curamPersonDtls
   * @param craPersonDtls
   * @return true if 3/4 criteria match, false otherwise
   */
  public boolean isSuccessfulPersonMatch(
    final BDMOASPersonSummaryDetails curamPersonDtls,
    final BDMOASPersonSummaryDetails craPersonDtls) {

    // Count the number of matching fields
    int matchingFields = 0;

    // compare 5 first letters of surname
    if (firstFiveLettersSurnameMatch(curamPersonDtls.lastName,
      craPersonDtls.lastName))
      matchingFields++;

    // Check if the SINs match
    if (curamPersonDtls.concernRoleID == craPersonDtls.concernRoleID)
      matchingFields++;

    // compare DOB month
    if (matchMonth(curamPersonDtls.dateOfBirth, craPersonDtls.dateOfBirth))
      matchingFields++;

    // compare DOB month
    if (matchYear(curamPersonDtls.dateOfBirth, craPersonDtls.dateOfBirth))
      matchingFields++;

    // Return true if there are 2 or more matching fields
    return matchingFields >= 3;
  }

  /**
   * This method adds the records for producing the summary report in the database
   * and saves the number of records received, number successfula dn number of
   * tasks created.
   *
   * @param recordNumber
   * @param recordStatus
   * @throws AppException
   * @throws InformationalException
   */
  public void createSummaryData(final String interfaceName,
    final Boolean recordStatus, final Date date, final long interfaceID)
    throws AppException, InformationalException {

    final BDMInterfaceSummary interfaceSummaryObj =
      BDMInterfaceSummaryFactory.newInstance();

    final BDMInterfaceSummaryKey interfaceSummaryObjKey =
      new BDMInterfaceSummaryKey();

    BDMInterfaceSummaryDtls interfaceSummaryObjdtls =
      new BDMInterfaceSummaryDtls();

    final BDMCRADeathMatchSearchKey craDeathMatchSearchKey =
      new BDMCRADeathMatchSearchKey();

    BDMInterfaceSummaryDtlsList interfaceSummaryDtlsList =
      new BDMInterfaceSummaryDtlsList();

    try {
      craDeathMatchSearchKey.interfaceNameCode = interfaceName;
      craDeathMatchSearchKey.year = date.getCalendar().get(Calendar.YEAR);

      // read from table
      interfaceSummaryDtlsList =
        interfaceSummaryObj.searchByNameCodeAndYear(craDeathMatchSearchKey);

      // check for size if 0 then do an insert with successful record update or task
      if (interfaceSummaryDtlsList.dtls.size() == 0) {
        interfaceSummaryObjdtls.interfaceNameCode = interfaceName;
        interfaceSummaryObjdtls.totalRecords =
          interfaceSummaryDtlsList.dtls.size() + 1;
        interfaceSummaryObjdtls.year = date.getCalendar().get(Calendar.YEAR);
        interfaceSummaryObjdtls.interfaceSummaryID = interfaceID;
        if (recordStatus) {
          interfaceSummaryObjdtls.totalSuccessRecords =
            interfaceSummaryObjdtls.totalSuccessRecords + 1;
        } else {
          interfaceSummaryObjdtls.totalTasks =
            interfaceSummaryObjdtls.totalTasks + 1;
        }
        interfaceSummaryObj.insert(interfaceSummaryObjdtls);

      } else if (interfaceSummaryDtlsList.dtls.size() > 0) {

        // if more than 0 get the first one and modify
        interfaceSummaryObjdtls = interfaceSummaryDtlsList.dtls.get(0);

        interfaceSummaryObjdtls.totalRecords =
          interfaceSummaryDtlsList.dtls.size() + 1;
        interfaceSummaryObjdtls.year = date.getCalendar().get(Calendar.YEAR);
        if (recordStatus) {
          interfaceSummaryObjdtls.totalSuccessRecords =
            interfaceSummaryObjdtls.totalSuccessRecords + 1;
        } else {
          interfaceSummaryObjdtls.totalTasks =
            interfaceSummaryObjdtls.totalTasks + 1;
        }
        interfaceSummaryObjKey.interfaceSummaryID =
          interfaceSummaryDtlsList.dtls.get(0).interfaceSummaryID;

        interfaceSummaryObj.modify(interfaceSummaryObjKey,
          interfaceSummaryObjdtls);

      }
      Trace.kTopLevelLogger
        .debug(BDMConstants.BDM_LOGS_PREFIX + recordStatus);

    } catch (final Exception ex) {
      Trace.kTopLevelLogger.error(ex);

    }

  }

  /**
   * Validates the paramItem.
   *
   * @param paramItem
   * @param constVal the constant for corresponding item
   *
   * @throws AppException
   * @throws InformationalException
   */
  public boolean paramNotNullOrEmpty(final String paramItem)
    throws AppException, InformationalException {

    final Boolean paramCheck =
      paramItem == null || paramItem.trim().isEmpty() ? false : true;

    return paramCheck;

  }

  /**
   * Process the error response and throw the correct error
   *
   * @param response - constantName contains information which input is missing
   * @return AppException to throw
   */
  public void errorTypeHandler(final String constantName)
    throws AppException {

    final AppException appException =
      new AppException(BDMBPOCCT.ERR_HTTP_404_RESOURCE_NOT_FOUND);
    appException.arg(constantName);
    throw appException;
  }

  /**
   * Take records returned from Curam table and filter them by the date of birth
   * passed in the request.
   *
   * @param records - Curam records
   * @param requestDateOfBirth - client DOB (from request)
   * @return The Curam record that matches the DOB passed in
   */
  public BDMOASPersonSummaryDetails findMatchingRecord(
    final BDMOASPersonSummaryDetailsList records,
    final Date requestDateOfBirth, final String requestSurname)
    throws AppException, InformationalException {

    BDMOASPersonSummaryDetails recordOfInterest = null;
    // Go through records
    for (final BDMOASPersonSummaryDetails record : records.dtls) {
      if (doesRecordMatch(record, requestDateOfBirth, requestSurname)) {
        // If there is no current record found or the previous record found is a
        // duplicate, then replace recordOfInterest with current record being viewed
        if (recordOfInterest == null || recordOfInterest.duplicateStatus
          .equals(DUPLICATESTATUS.MARKED)) {
          recordOfInterest = record.deepClone();
        } else if (!record.duplicateStatus.equals(DUPLICATESTATUS.MARKED)) {
          recordOfInterest.duplicateStatus = DUPLICATESTATUS.MARKED;
          break;
        }
      }
    }

    return recordOfInterest;
  }

  /**
   * Compare incoming record data to data (surname and DOB) from request.
   *
   * @param record - Record to compare date and surname with
   * @param dateToMatch - Reference date
   * @param surnameToMatch - Reference surname
   * @return Indicator for if the record matches the reference data
   */
  public boolean doesRecordMatch(final BDMOASPersonSummaryDetails record,
    final Date dateToMatch, final String surnameToMatch) {

    final boolean doSurnamesMatch =
      firstFiveLettersSurnameMatch(record.lastName, surnameToMatch);
    final boolean doYearsMatch = matchYear(record.dateOfBirth, dateToMatch);
    final boolean doMonthsMatch = matchMonth(record.dateOfBirth, dateToMatch);

    return doSurnamesMatch && (doYearsMatch || doMonthsMatch)
      || doYearsMatch && doMonthsMatch;
  }

  /**
   * Map the marital status value provided in the CRA request to the Curam value
   *
   * @param maritalStatusRequestValue - Request marital status value
   * @return Corresponding Curam BDMMARITALSTATUS value
   */
  public String
    mapBDMMaritalStatusEntry(final String maritalStatusRequestValue) {

    switch (maritalStatusRequestValue) {
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_MARRIED:
        return BDMMARITALSTATUS.MARRIED;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_COMMONLAW:
        return BDMMARITALSTATUS.COMMONLAW;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_WIDOWED:
        return BDMMARITALSTATUS.WIDOWED;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_DIVORCED:
        return BDMMARITALSTATUS.DIVORCED;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SEPARATED:
        return BDMMARITALSTATUS.SEPARATED;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE:
        return BDMMARITALSTATUS.SINGLE;
      default:
        return null;
    }
  }

  /**
   * Map the marital status value provided in the CRA request to the Curam value
   * (for CM entities)
   *
   * @param maritalStatusRequestValue - Request marital status value
   * @return Corresponding Curam BDMOASCRAMARITALSTATUS value
   */
  public String mapCRAMaritalStatusEntry(final String requestMaritalStatus) {

    switch (requestMaritalStatus) {
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_UNKNOWN:
        return BDMOASCRAMARITALSTATUS.UNKNOWN;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_MARRIED:
        return BDMOASCRAMARITALSTATUS.MARRIED;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_COMMONLAW:
        return BDMOASCRAMARITALSTATUS.COMMON_LAW;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_WIDOWED:
        return BDMOASCRAMARITALSTATUS.WIDOWED;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_DIVORCED:
        return BDMOASCRAMARITALSTATUS.DIVORCED;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SEPARATED:
        return BDMOASCRAMARITALSTATUS.SEPARATED;
      case BDMOASConstants.BDM_OAS_CRA_MARITAL_STATUS_SINGLE:
        return BDMOASCRAMARITALSTATUS.SINGLE;
      default:
        return null;
    }
  }

  /**
   * Take income records and update evidence tables for person specified by the
   * concernRoleID.
   *
   * @param concernRoleID - Curam identifier for person
   * @param incomeRecords - Income records
   */
  public void storeIncomeDataRecords(final long dataID,
    final Map<String, String> lineItemEntries,
    final List<BDMIncomeDetail> incomeRecords)
    throws AppException, InformationalException {

    // Go through income records and store them into BDMOASCRAIncomeData
    for (final BDMIncomeDetail record : incomeRecords) {

      final BDMOASCRAIncomeDataDtls incomeDetails =
        new BDMOASCRAIncomeDataDtls();
      incomeDetails.amount = new Money(record.getIncomeAmount().getAmount());
      incomeDetails.dataID = dataID;

      // Map Line Item based on record's incomeName field
      if (record.getIncomeName()
        .equals("CPP QPP Lump Sum Tax Payable Calculated Amount")) {
        incomeDetails.lineItem = BDMOASCRALINEITEMTYPE.CPP_LUMP_SUM;
      } else {
        // Search lineItemEntries map for a key with the incomeLineNumber passed in
        incomeDetails.lineItem = lineItemEntries.entrySet().stream()
          .filter(entry -> record.getIncomeLineNumber() != null
            && entry.getKey().contains(record.getIncomeLineNumber()))
          .findAny()
          .orElse(new AbstractMap.SimpleEntry<String, String>("", null))
          .getValue();
      }

      if (incomeDetails.lineItem != null) {
        BDMOASCRAIncomeDataFactory.newInstance().insert(incomeDetails);
      } else {
        Trace.kTopLevelLogger
          .warn(record.getIncomeName() + " was not able to be mapped.");
      }

    }
  }

  /**
   * Prepare Bankruptcy Flag Task for provided SIN.
   *
   * @param sin - SIN
   */
  public void createBankruptcyFlagTask(final String sin, final long caseId) {

    final TaskCreateDetails taskDetails = new TaskCreateDetails();
    taskDetails.subject = "Pre/Post bankruptcy reported for Person " + sin;
    taskDetails.priority = "Medium";
    taskDetails.caseID = caseId;
    createTask(taskDetails);
  }

  /**
   * Prepare Inactive Client Task for provided SIN.
   *
   * @param sin - SIN
   */
  public void createInactiveClientTask(final String sin, final long caseId) {

    final TaskCreateDetails taskDetails = new TaskCreateDetails();
    taskDetails.subject = "Inactive CRA account reported for Person " + sin;
    taskDetails.priority = "Medium";
    taskDetails.caseID = caseId;
    createTask(taskDetails);

  }

  /**
   * Update summary report table with result of CRA operation.
   *
   * @param interfaceName - Interface whose record is being updated
   * @param isSuccessful - Indicator of successful operation
   * @param isATask - Indicator of task creation
   * @param date - date of record
   */
  public void updateSummaryReportTable(final String interfaceName,
    final boolean isSuccessful, final boolean isATask, final Date date) {

    try {
      final BDMInterfaceSummary interfaceSummaryObj =
        BDMInterfaceSummaryFactory.newInstance();

      final BDMCRADeathMatchSearchKey searchKey =
        new BDMCRADeathMatchSearchKey();
      searchKey.interfaceNameCode = interfaceName;
      searchKey.year =
        date == null ? 0 : date.getCalendar().get(Calendar.YEAR);

      final BDMInterfaceSummaryDtlsList records =
        interfaceSummaryObj.searchByNameCodeAndYear(searchKey);

      if (records.dtls.size() == 0) {
        final BDMInterfaceSummaryDtls newEntryDtls =
          new BDMInterfaceSummaryDtls();
        newEntryDtls.totalRecords = 1;
        newEntryDtls.totalSuccessRecords = isSuccessful ? 1 : 0;
        newEntryDtls.totalTasks = isATask ? 1 : 0;
        newEntryDtls.year = searchKey.year;
        newEntryDtls.interfaceNameCode = interfaceName;
        newEntryDtls.interfaceSummaryID = UniqueID.nextUniqueID();
        interfaceSummaryObj.insert(newEntryDtls);
      } else {
        final BDMInterfaceSummaryKey modifyKey = new BDMInterfaceSummaryKey();
        modifyKey.interfaceSummaryID = records.dtls.get(0).interfaceSummaryID;
        records.dtls.get(0).totalRecords += 1;
        records.dtls.get(0).totalSuccessRecords += isSuccessful ? 1 : 0;
        records.dtls.get(0).totalTasks += isATask ? 1 : 0;
        interfaceSummaryObj.modify(modifyKey, records.dtls.get(0));
      }
    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e);
    }

  }

  /**
   * Prepare Person Mismatch Task for provided SIN.
   *
   * @param sin - SIN
   */
  public void createPersonMismatchTask(final String sin, final long caseId) {

    final TaskCreateDetails taskDetails = new TaskCreateDetails();
    taskDetails.subject = "Person Mismatch for SIN " + sin;
    taskDetails.priority = "Medium";
    taskDetails.caseID = caseId;
    createTask(taskDetails);

  }

  /**
   * Take income data, store it in income staging tables and call Case Management
   * service layer function to activate evidence.
   *
   * @param requestData - incoming request data
   * @param record - Curam client record
   * @param incomeYear - request tax year
   * @return list of evidenceIds created
   */
  public List<Long> createIncomeEvidence(
    final BDMOASGISTakeUpInbound requestData,
    final BDMOASPersonSummaryDetails record, final Date incomeYear) {

    List<Long> evidenceIds = null;
    final String incomeYearValue =
      Integer.toString(incomeYear.getCalendar().get(Calendar.YEAR));

    try {
      // BDMOASCRATransaction -> BDMOASCRAData -> BDMOASCRAIncomeData

      // Insert into BDMOASCRATransaction
      addCRATransaction(record.concernRoleID, incomeYearValue,
        BDMInterfaceNameCode.CRA_GIS_TAKE_UP);

      // Retrieve transactionId for created BDMOASCRATransaction data
      final BDMOASCRATransactionDtls createdTransactionDtls =
        getCRATransaction(record.concernRoleID, incomeYearValue);

      if (createdTransactionDtls == null) {
        throw new NullPointerException(
          "Unable to retrieve BDMOASCRATransaction");
      }

      // Insert into BDMOASCRAData
      addCRAData(createdTransactionDtls.transactionID, record.dateOfBirth,
        requestData);

      // Retrieve created BDMOASCRAData row
      final BDMOASCRATransactionKey key = new BDMOASCRATransactionKey();
      key.transactionID = createdTransactionDtls.transactionID;
      final BDMOASCRADataDtls craData =
        BDMOASCRADataFactory.newInstance().readByTransaction(key);

      // Retrieve BDMOASCRALINEITEMTYPE entries and create map that maps descriptions
      // to code table values
      final Map<String, String> codeTableEntries = CodeTable.getAllItems(
        BDMOASCRALINEITEMTYPE.TABLENAME, TransactionInfo.getProgramLocale());
      final Map<String, String> lineItemEntries = codeTableEntries.entrySet()
        .stream().collect(Collectors.toMap(Entry::getValue, Entry::getKey));

      // Take income records from client data as well as line items map and store
      // income data into BDMOASCRAIncomeData
      storeIncomeDataRecords(craData.dataID, lineItemEntries, requestData
        .getData().getIndividualIncome().getIndividualIncomeDetail());

      // Call CM SL function to activate evidence
      evidenceIds = new BDMOASCRATransactionProcessor()
        .processTransaction(createdTransactionDtls.transactionID);
    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e);
    }

    return evidenceIds;
  }

  /**
   * Create BDMOASCRATransaction data entry.
   *
   * @param concernRoleID - Person's concernRoleId
   * @param year - Tax year for request
   * @param interfaceName - Request type
   * @throws AppException
   * @throws InformationalException
   */
  public void addCRATransaction(final long concernRoleID, final String year,
    final String interfaceName) throws AppException, InformationalException {

    final BDMOASCRATransactionDtls transactionDetails =
      new BDMOASCRATransactionDtls();

    if (interfaceName.equals(BDMInterfaceNameCode.CRA_GIS_TAKE_UP)) {
      transactionDetails.concernRoleID = concernRoleID;
      transactionDetails.taxYear = year;
      transactionDetails.status = BDMOASCRATRANSACTIONSTATUS.RECEIVED;
      transactionDetails.transactionCode = BDMOASCRATRANSACTIONCODE.ONE_WAY;
      transactionDetails.transactionSubCode = BDMOASCRATRANSSUBCODE.ONE_WAY;
      transactionDetails.personMatchStatus = BDMOASCRAPERSONMATCH.MATCHED;
    }

    BDMOASCRATransactionFactory.newInstance().insert(transactionDetails);
  }

  /**
   * Map income status from request to BDMOASCRAINCOMESTATUS
   *
   * @param incomeStatus - Incoming CRA Income status
   * @return Corresponding BDMOASCRAINCOMESTATUS
   */
  public String getCRAIncomeStatus(final String incomeStatus) {

    switch (incomeStatus) {
      case BDMOASConstants.BDM_OAS_CRA_INCOME_STATUS_FOUND:
        return BDMOASCRAINCOMESTATUS.FOUND;
      case BDMOASConstants.BDM_OAS_CRA_INCOME_STATUS_NOT_FOUND:
        return BDMOASCRAINCOMESTATUS.NOT_FOUND;
      default:
        return BDMOASCRAINCOMESTATUS.NOT_SET;
    }
  }

  /**
   * Retrieve BDMOASCRATransaction entry using concernRoleId and taxYear.
   *
   * @param concernRoleID - Person's Curam Id
   * @param taxYear - Request income year
   * @return BDMOASCRATransaction entry
   *
   * @throws AppException
   * @throws InformationalException
   */
  public BDMOASCRATransactionDtls getCRATransaction(final long concernRoleID,
    final String taxYear) throws AppException, InformationalException {

    // Retrieve BDMOASCRATransaction records w/ matching concernRoleId and RECEIVED
    // status
    final BDMOASCRATransactionConcernRoleStatusKey transactionSearchKey =
      new BDMOASCRATransactionConcernRoleStatusKey();
    transactionSearchKey.concernRoleID = concernRoleID;
    transactionSearchKey.status = BDMOASCRATRANSACTIONSTATUS.RECEIVED;
    final BDMOASCRATransactionDtlsList transactionRecords =
      BDMOASCRATransactionFactory.newInstance()
        .searchByConcernRoleAndStatus(transactionSearchKey);

    // Filter through records to find record w/ matching tax year
    return transactionRecords.dtls.stream()
      .filter(record -> record.taxYear.equals(taxYear)).findAny()
      .orElse(null);
  }

  /**
   * Map request account status to Curam account status.
   *
   * @param accountData - Object containing account status data from request
   * @return Code table value corresponding to status
   */
  public String mapCRAAccountStatus(final BDMStatus accountData) {

    if (accountData != null
      && accountData.getStatusCode().getReferenceDataName().equalsIgnoreCase(
        BDMOASConstants.BDM_OAS_CRA_ACCOUNT_STATUS_ACTIVE)) {
      return BDMOASCRAACCOUNTSTATUS.ACTIVE;
    }

    return BDMOASCRAACCOUNTSTATUS.INACTIVE;
  }

  /**
   * Update BDMOASCRAData table with new data entry.
   *
   * @param transactionID - foreign key from BDMOASCRATransaction
   * @param requestDateOfBirth - Request DOB
   * @param requestData - Request data
   * @throws AppException
   * @throws InformationalException
   */
  public void addCRAData(final long transactionID,
    final Date requestDateOfBirth, final BDMOASGISTakeUpInbound requestData)
    throws AppException, InformationalException {

    final BDMOASCRADataDtls craDataDtls = new BDMOASCRADataDtls();
    final BDMOASCRAIndividualIncomeDetails clientData =
      requestData.getData().getIndividualIncome();

    craDataDtls.transactionID = transactionID;

    craDataDtls.accountStatus =
      mapCRAAccountStatus(clientData.getClient().getPersonStatus());

    // recordDataDetails.craProcessDate = null;

    craDataDtls.dateOfBirth = requestDateOfBirth;

    craDataDtls.firstForename = clientData.getClient().getPersonNameList()
      .get(0).getPersonGivenName()[0];

    craDataDtls.incomeStatus = getCRAIncomeStatus(clientData
      .getIndividualIncomeStatus().getStatusCode().getReferenceDataID());

    craDataDtls.maritalStatus = mapCRAMaritalStatusEntry(clientData
      .getClient().getMaritalStatus().getStatusCode().getReferenceDataID());

    craDataDtls.prePostBankruptcyInd = false;

    craDataDtls.surname =
      clientData.getClient().getPersonNameList().get(0).getPersonSurname();

    craDataDtls.receivedDateTime = DateTime.getCurrentDateTime();

    craDataDtls.sin =
      clientData.getClient().getPersonSINIdentification().getSin();

    // Find Spouse from RelatedPerson data and add spouseSIN to struct data
    final List<BDMRelatedPerson> relatedPeople =
      clientData.getRelatedPerson();

    // Filter through related people for Spouse
    final BDMRelatedPerson spouseData = relatedPeople.stream()
      .filter(person -> person.getPersonRelationshipCode()
        .getReferenceDataName().equals("Spouse"))
      .findAny().orElse(null);

    // If there is a spouse, add their SIN
    if (spouseData != null) {
      craDataDtls.spouseSIN = spouseData.getSin().getSin();
    }

    BDMOASCRADataFactory.newInstance().insert(craDataDtls);
  }

  public void createDateOfDeathMismatchTask(
    final BDMOASPersonSummaryDetails matchingPersonDtls,
    final String requestCRASin) {

    // TODO Auto-generated method stub

  }

  public void updateDateOfDeath(
    final BDMOASPersonSummaryDetails matchingPersonDtls,
    final Date requestCRADODDate)
    throws AppException, InformationalException {

    final PersonKey personToUpdateKey = new PersonKey();
    personToUpdateKey.concernRoleID = matchingPersonDtls.concernRoleID;
    final curam.core.intf.Person personObj = PersonFactory.newInstance();
    final PersonDtls personToUpdateDtls = personObj.read(personToUpdateKey);

    // Update PersonSnapshot
    final PersonSnapshot personSnapshotObj =
      PersonSnapshotFactory.newInstance();
    final PersonSnapshotDtls personToUpdateSSDtls = new PersonSnapshotDtls();
    personToUpdateSSDtls.assign(personToUpdateDtls); // copy over PersonDtls retrieved
    personToUpdateSSDtls.creationDateTime = DateTime.getCurrentDateTime();
    personSnapshotObj.insert(personToUpdateSSDtls);

    // Update Person with new date of death
    personToUpdateDtls.dateOfDeath = requestCRADODDate;
    personObj.pdcModify(personToUpdateKey, personToUpdateDtls);
  }

  /**
   * Create and activate new evidence for the provided evidenceType.
   *
   * @param concernRoleID - ID for client for whom evidence is being created for
   * @param evidenceType - Evidence Type
   * @param evidenceEntry - CT entry for evidence type
   * @param attributes - attributes for evidence
   * @throws AppException
   * @throws InformationalException
   */
  public void createEvidence(final long concernRoleID,
    final String evidenceType, final CASEEVIDENCEEntry evidenceEntry,
    final Map<String, String> attributes)
    throws AppException, InformationalException {

    // Retrieve cases involving concernRoleID
    final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(concernRoleID);
    final BDMUtil util = new BDMUtil();

    for (final CaseHeaderDtls caseHeaderDetail : caseHeaderList) {
      // Get CaseParticipantRoleID and add to attributes
      final CaseParticipantRoleIDStruct cprObj =
        util.getCaseParticipantRoleID(caseHeaderDetail.caseID, concernRoleID);

      if (evidenceEntry.equals(CASEEVIDENCEEntry.OAS_CRA_MARITAL_STATUS)) {
        attributes.put(BDMOASCraIncomeConstants.PARTICIPANT,
          Long.toString(cprObj.caseParticipantRoleID));
      } else if (evidenceEntry
        .equals(CASEEVIDENCEEntry.OAS_CRA_BIRTH_DEATH)) {
        attributes.put(BDMOASConstants.kPerson,
          Long.toString(cprObj.caseParticipantRoleID));
      }

      // Create evidence
      final EIEvidenceKey evidence =
        BDMOASEvidenceUtil.createEvidence(caseHeaderDetail.caseID,
          concernRoleID, evidenceEntry, attributes, Date.getCurrentDate());

      // Apply evidence for Integrated Cases
      if (caseHeaderDetail.caseTypeCode.equals(CASETYPECODE.INTEGRATEDCASE)) {
        final RelatedIDAndEvidenceTypeKey key =
          new RelatedIDAndEvidenceTypeKey();
        key.evidenceType = evidenceType;
        key.relatedID = evidence.evidenceID;
        final EvidenceDescriptorDtls evidenceDescriptorDtls =
          EvidenceDescriptorFactory.newInstance().readByRelatedIDAndType(key);
        BDMEvidenceUtil
          .applyInEditEvidenceToICDynamicEvidence(evidenceDescriptorDtls);
      }
    }

  }

  /**
   * Modify evidence of given evidenceType with values provided in attributes.
   *
   * @param attributes - Evidence attributes to update
   * @param evidenceType - Type of evidence
   * @param concernRoleId - ID of client to update evidence for
   * @throws AppException
   * @throws InformationalException
   */
  public void modifyEvidences(
    final List<DynamicEvidenceDataDetails> evidenceDtls,
    final Map<String, String> attributes, final String evidenceType,
    final long concernRoleId) throws AppException, InformationalException {

    // Go through evidences and update
    for (final DynamicEvidenceDataDetails evidenceDtl : evidenceDtls) {
      boolean changedAttribute = false;

      // Check to see if the evidence attribute matches what is passed in
      for (final Map.Entry<String, String> attribute : attributes
        .entrySet()) {
        final DynamicEvidenceDataAttributeDetails currentAttribute =
          evidenceDtl.getAttribute(attribute.getKey());
        // If they do not match, we have a modified attribute -> need to modify evidence
        if (!currentAttribute.equals(null)
          && !currentAttribute.getValue().equals(attribute.getValue())) {
          changedAttribute = true;
          DynamicEvidenceTypeConverter.setAttribute(
            evidenceDtl.getAttribute(attribute.getKey()),
            attribute.getValue());
        }
      }

      if (changedAttribute) {
        BDMEvidenceUtil.modifyEvidence(evidenceDtl.getID(), evidenceType,
          evidenceDtl, EVIDENCECHANGEREASON.REPORTEDBYCLIENT);

        // Apply evidence for integrated cases
        final RelatedIDAndEvidenceTypeKey evdDescKey =
          new RelatedIDAndEvidenceTypeKey();
        evdDescKey.relatedID = evidenceDtl.getID();
        evdDescKey.evidenceType = evidenceType;
        final EvidenceDescriptorDtls evidenceDescDtls =
          EvidenceDescriptorFactory.newInstance()
            .readByRelatedIDAndType(evdDescKey);
        BDMEvidenceUtil
          .applyInEditEvidenceToICDynamicEvidence(evidenceDescDtls);
      }
    }
  }

}
