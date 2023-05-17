/**
 *
 */
package curam.ca.gc.bdm.facade.bdmcommonintake.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMMyApplications;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMMyApplicationsList;
import curam.ca.gc.bdm.facade.bdmcommonintake.struct.BDMSelectInternalIntakeApplication;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.commonintake.codetable.APPLICATIONFORMSTATUS;
import curam.commonintake.codetable.impl.APPLICATIONCASESTATUSEntry;
import curam.commonintake.facade.impl.ApplicationForm;
import curam.commonintake.facade.struct.ApplicationCaseSearchCriteria;
import curam.commonintake.facade.struct.ApplicationFormDetails;
import curam.commonintake.facade.struct.ApplicationFormDetailsList;
import curam.commonintake.facade.struct.ReferralCaseSearchCriteria;
import curam.commonintake.impl.ApplicationCaseAdmin;
import curam.commonintake.impl.ApplicationCaseAdminDAO;
import curam.commonintake.message.COMMONINTAKEGENERAL;
import curam.commonintake.message.impl.APPLICATIONCASESEARCHExceptionCreator;
import curam.commonintake.message.impl.REFERRALCASESEARCHExceptionCreator;
import curam.core.impl.EnvVars;
import curam.core.sl.struct.SQLStatement;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.InformationalMsgDtls;
import curam.message.impl.GENERALSEARCHExceptionCreator;
import curam.piwrapper.impl.ClientURI;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.ValidationHelper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.StringHelper;
import curam.util.type.StringList;
import curam.workspaceservices.codetable.impl.ApplicationChannelEntry;
import curam.workspaceservices.codetable.impl.ProcessingSystemTypeEntry;
import curam.workspaceservices.intake.impl.IntakeApplicationType;
import curam.workspaceservices.intake.impl.IntakeApplicationTypeDAO;
import curam.workspaceservices.intake.impl.ProgramType;
import curam.workspaceservices.localization.impl.LocalizableTextHandler;
import curam.workspaceservices.localization.impl.LocalizableTextHandlerDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sivakumar.kalyanasun
 * @version 1.0
 * @description : 20th Jan 2022
 * This BDMMyApplicationsHelperImpl class created for the Application Search Ext
 * related to Tasks
 * 9092 and 12510
 *
 */
public class BDMMyApplicationsHelperImpl {

  private boolean filterList;

  @Inject
  private LocalizableTextHandlerDAO localizableTextHandlerDAO;

  @Inject
  private IntakeApplicationTypeDAO intakeApplicationTypeDAO;

  @Inject
  private ApplicationCaseAdminDAO applicationCaseAdminDAO;

  @Inject
  private ApplicationForm applicationForm;

  public BDMMyApplicationsHelperImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   *
   * @param ApplicationCaseSearchCriteria applicationCaseSearchCriteria
   * @return BDMMyApplicationsList
   * @throws AppException
   * @throws InformationalException
   */
  public BDMMyApplicationsList getSearchedApplications(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria)
    throws AppException, InformationalException {

    StringBuffer whereBuffer = new StringBuffer();
    final SQLStatement sqlStatement = new SQLStatement();

    new BDMMyApplicationsList();
    this.validateSearchCriteria(applicationCaseSearchCriteria);

    if (applicationCaseSearchCriteria.caseReference.length() != 0) {

      whereBuffer.append("Caseheader.caseReference = :caseReference");

    } else if (applicationCaseSearchCriteria.applicationReference
      .length() != 0) {

      whereBuffer.append(
        "ApplicationCase.applicationReference = :applicationReference");

    } else if (applicationCaseSearchCriteria.concernRoleID != 0L) {

      whereBuffer = this
        .buildConcernRoleEnteredWhereClause(applicationCaseSearchCriteria);

    } else if (!applicationCaseSearchCriteria.applicationDate.isZero()) {

      whereBuffer =
        this.buildDateEnteredWhereClause(applicationCaseSearchCriteria);

    }

    if (whereBuffer.toString().isEmpty()) {

      ValidationHelper
        .addValidationError(APPLICATIONCASESEARCHExceptionCreator
          .ERR_FV_SEARCH_CRITERIA_MUST_BE_ENTERED());

      ValidationHelper.failIfErrorsExist();

    }

    sqlStatement.sqlStatement = this.buildApplicationSearchQuery(whereBuffer);

    final CuramValueList<BDMMyApplications> curamValueList =
      this.executeApplicationSearchSQL(applicationCaseSearchCriteria,
        sqlStatement);

    // 19582 - Do not add search result to Set,
    // as it impacts the order of search result
    // final Set<BDMMyApplications> dtlsList = new HashSet(curamValueList);

    final ArrayList<BDMMyApplications> searchResults = new ArrayList();

    // searchResults.addAll(dtlsList);
    searchResults.addAll(curamValueList);

    // Collections.sort(searchResults, new ApplicationComparator());

    this.filterList = true;

    final BDMMyApplicationsList myApplicationsList =
      this.aggregateList(this.buildListOfSearchReults(searchResults));

    if (myApplicationsList.dtls.size() == 0) {

      ValidationHelper
        .addValidationError(APPLICATIONCASESEARCHExceptionCreator
          .ERR_FV_NO_SEARCH_RESULTS_FOUND());

      ValidationHelper.failIfErrorsExist();

    }

    return myApplicationsList;
  }

  private CuramValueList<BDMMyApplications> executeApplicationSearchSQL(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria,
    final SQLStatement sqlStatement)
    throws AppException, InformationalException {

    CuramValueList<BDMMyApplications> curamValueList = null;

    curamValueList = DynamicDataAccess.executeNsMulti(BDMMyApplications.class,
      applicationCaseSearchCriteria, false, true, sqlStatement.sqlStatement);

    return curamValueList;
  }

  private StringBuffer buildDateEnteredWhereClause(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria) {

    final StringBuffer whereBuffer = new StringBuffer();

    whereBuffer.append("ApplicationCase.applicationDate >= :applicationDate");

    if (!applicationCaseSearchCriteria.statusList.isEmpty()) {

      whereBuffer
        .append(this.getStatusWhereClause(applicationCaseSearchCriteria));
    }

    if (!applicationCaseSearchCriteria.caseTypeList.isEmpty()) {

      whereBuffer
        .append(this.getCaseTypeWhereClause(applicationCaseSearchCriteria));

    }

    return whereBuffer;
  }

  private String buildApplicationSearchQuery(final StringBuffer whereBuffer) {

    String sql = new String();

    sql = sql + "SELECT ";
    // 19582 - Add surname column
    sql = sql + "Alternatename.surname, ";
    sql = sql + "Caseheader.caseid, ";
    sql = sql + "Caseheader.caseReference, ";
    sql = sql + "Applicationcase.applicationReference, ";
    sql = sql + "Applicationcase.applicationCaseID, ";
    sql = sql + "Applicationcase.status, ";
    sql = sql + "Applicationcase.applicationDate, ";
    sql = sql + "ApplicationCaseAdmin.nametextid caseType, ";
    sql = sql + "Programtype.nametextid caseProgram, ";
    sql = sql + "Alternatename.fullname caseClients, ";
    sql = sql + "ApplicationCaseAdmin.homepage, ";
    sql = sql + "Caseparticipantrole.participantRoleId, ";
    sql = sql + "TO_DATE(Applicationcase.SUBMITTEDDATETIME), ";
    sql = sql + "concernrolealternateid.ALTERNATEID ";

    sql = sql + "INTO ";
    // 19582 - Add surname column
    sql = sql + ":lastName,";
    sql = sql + ":caseID,";
    sql = sql + ":caseReference,";
    sql = sql + ":applicationReference,";
    sql = sql + ":applicationCaseID,";
    sql = sql + ":status,";
    sql = sql + ":applicationDate, ";
    sql = sql + ":caseType,";
    sql = sql + ":casePrograms,";
    sql = sql + ":caseClients, ";
    sql = sql + ":applicationHomeURL, ";
    sql = sql + ":concernRoleID, ";
    sql = sql + ":submissionDate, ";
    sql = sql + ":sin ";

    sql = sql + "FROM ";

    sql = sql + "applicationcase ";
    sql = sql
      + "JOIN Caseheader ON Caseheader.caseid = Applicationcase.applicationcaseid ";

    sql = sql
      + "JOIN ApplicationCaseAdmin ON ApplicationCaseAdmin.Applicationcaseadminid = Applicationcase.Applicationcaseadminid ";

    sql = sql
      + "LEFT JOIN IntakeProgramAppCaseLink ON IntakeProgramAppCaseLink.caseid = Caseheader.caseid ";

    sql = sql
      + "LEFT JOIN Intakeprogramapplication ON Intakeprogramapplication.intakeprogramapplicationid = IntakeProgramappcaselink.intakeprogramapplicationid ";

    sql = sql
      + "LEFT JOIN Programtype ON programtype.programtypeid = IntakeProgramApplication.programtypeid ";

    sql = sql
      + "JOIN Caseparticipantrole ON Caseparticipantrole.caseid = Caseheader.caseid ";

    sql = sql
      + "JOIN Alternatename ON alternatename.concernroleid = Caseparticipantrole.participantroleid ";

    sql = sql
      + "LEFT  JOIN concernrolealternateid ON concernrolealternateid.concernroleid=alternatename.concernroleid AND concernrolealternateid.TYPECODE='BDMCA80002'";

    sql = sql + "WHERE ";
    sql = sql + whereBuffer;

    // 19582 - Order by client last name and application type
    sql = sql
      + " ORDER BY ALTERNATENAME.SURNAME, ApplicationCaseAdmin.nametextid ASC";

    return sql;
  }

  private BDMMyApplicationsList buildListOfSearchReults(
    final ArrayList<BDMMyApplications> searchResults) {

    final BDMMyApplicationsList myApplicationsList =
      new BDMMyApplicationsList();

    String applicationHomePage =
      Configuration.getProperty(EnvVars.ENV_CASE_APPLICATION_CASE_HOME);

    if (StringHelper.isEmpty(applicationHomePage)) {
      applicationHomePage = "CommonIntake_applicationCaseHome";
    }

    final Iterator var4 = searchResults.iterator();
    while (var4.hasNext()) {
      final BDMMyApplications applicationSearchResults =
        (BDMMyApplications) var4.next();
      final BDMMyApplications searchDtls = new BDMMyApplications();
      searchDtls.applicationDate = applicationSearchResults.applicationDate;
      searchDtls.applicationReference =
        applicationSearchResults.applicationReference;

      searchDtls.caseType = applicationSearchResults.caseType;
      searchDtls.status = applicationSearchResults.status;
      searchDtls.caseReference = applicationSearchResults.caseReference;
      searchDtls.caseClients = applicationSearchResults.caseClients;
      searchDtls.casePrograms = applicationSearchResults.casePrograms;
      searchDtls.caseID = applicationSearchResults.caseID;
      searchDtls.concernRoleID = applicationSearchResults.concernRoleID;
      searchDtls.applicationCaseID =
        applicationSearchResults.applicationCaseID;
      searchDtls.submissionDate = applicationSearchResults.submissionDate;
      searchDtls.sin = applicationSearchResults.sin;
      searchDtls.lastName = applicationSearchResults.lastName;

      ClientURI contextPanelURI;
      if (!StringHelper
        .isEmpty(applicationSearchResults.applicationHomeURL)) {

        contextPanelURI =
          new ClientURI(applicationSearchResults.applicationHomeURL);

        contextPanelURI.appendParam("caseID",
          String.valueOf(applicationSearchResults.caseID));

        searchDtls.applicationHomeURL = contextPanelURI.getInternalURI();

      } else {
        contextPanelURI = new ClientURI(applicationHomePage);
        contextPanelURI.appendParam("caseID",
          String.valueOf(applicationSearchResults.caseID));

        searchDtls.applicationHomeURL = contextPanelURI.getInternalURI();

      }

      contextPanelURI =
        new ClientURI("CommonIntake_applicationCaseHomeContextPanel");

      contextPanelURI.appendParam("caseID",
        String.valueOf(applicationSearchResults.caseID));

      searchDtls.applicationContextPanelURI =
        contextPanelURI.getInternalURI();

      myApplicationsList.dtls.add(searchDtls);
    }

    return myApplicationsList;
  }

  private BDMMyApplicationsList
    aggregateList(final BDMMyApplicationsList myApplicationsList)
      throws InformationalException {

    final BDMMyApplicationsList newMyApplicationsList =
      new BDMMyApplicationsList();

    int applicationCaseCount = 0;

    // 19582 - Using LinkedHashMap instead of HashMap to retain default order
    // final Map<Long, List<BDMMyApplications>> applicationSearchResultsMap =
    // new HashMap();
    final Map<Long, List<BDMMyApplications>> applicationSearchResultsMap =
      new LinkedHashMap();

    this.processApplications(myApplicationsList, applicationSearchResultsMap);

    final Iterator var5 = applicationSearchResultsMap.values().iterator();

    while (var5.hasNext()) {
      final List<BDMMyApplications> applicationSearchResultsList2 =
        (List) var5.next();
      final BDMMyApplications results =
        this.aggregateSet(applicationSearchResultsList2);

      ++applicationCaseCount;

      if (applicationCaseCount > this.getMaxSearchResults()
        && this.filterList) {

        this.addInformationalException(newMyApplicationsList,
          GENERALSEARCHExceptionCreator
            .INF_SEARCH_MAXIMUM_LIMIT_EXCEEDED(this.getMaxSearchResults()));

        break;

      }

      if (results != null) {
        newMyApplicationsList.dtls.add(results);

      }
    }

    return newMyApplicationsList;
  }

  private void addInformationalException(
    final BDMMyApplicationsList myApplicationsList, final AppException e)
    throws InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    informationalManager.addInformationalMsg(e, "",
      InformationalType.kWarning);

    final String[] infos = informationalManager.obtainInformationalAsString();
    final String[] var5 = infos;
    final int var6 = infos.length;
    for (int var7 = 0; var7 < var6; ++var7) {
      final String message = var5[var7];
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = message;
      myApplicationsList.informationalDtls.dtls.addRef(informationalMsgDtls);
    }

  }

  int getMaxSearchResults() {

    final Integer lowerThreshold =
      Configuration.getIntProperty(EnvVars.ENV_APPLICATION_SEARCH_MAXIMUM);

    return lowerThreshold != null ? lowerThreshold : 100;
  }

  private BDMMyApplications
    aggregateSet(final List<BDMMyApplications> myApplicationsList) {

    BDMMyApplications result = new BDMMyApplications();
    final ArrayList<String> displayProgramRequested = new ArrayList();
    final ArrayList<String> displayClients = new ArrayList();
    final ArrayList<Long> concernRoles = new ArrayList();
    LocalizableTextHandler localizableCasePrograms = null;
    LocalizableTextHandler localizableTextCaseType = null;

    if (!myApplicationsList.isEmpty()) {

      final Iterator var8 = myApplicationsList.iterator();
      while (var8.hasNext()) {
        final BDMMyApplications applicationSearchResults =
          (BDMMyApplications) var8.next();
        if (applicationSearchResults.casePrograms.trim().length() > 0) {
          localizableCasePrograms = this.localizableTextHandlerDAO
            .get(Long.parseLong(applicationSearchResults.casePrograms));

          applicationSearchResults.casePrograms =
            localizableCasePrograms.getValue();
        }
        if (applicationSearchResults.caseType.trim().length() > 0) {
          localizableTextCaseType = this.localizableTextHandlerDAO
            .get(Long.parseLong(applicationSearchResults.caseType));

          applicationSearchResults.caseType =
            localizableTextCaseType.getValue();
        }
        if (applicationSearchResults.caseID != 0L) {

          if (!concernRoles
            .contains(applicationSearchResults.concernRoleID)) {

            displayClients.add(applicationSearchResults.caseClients);
            concernRoles.add(applicationSearchResults.concernRoleID);
          }

          if (!displayProgramRequested
            .contains(applicationSearchResults.casePrograms)) {

            displayProgramRequested
              .add(applicationSearchResults.casePrograms);

          }
        }
      }

      new BDMMyApplications();
      final BDMMyApplications applicationToReturn = myApplicationsList.get(0);
      applicationToReturn.caseClients = this.formatForDisplay(displayClients);

      applicationToReturn.casePrograms =
        this.formatForDisplay(displayProgramRequested);
      result = applicationToReturn;
    }

    return result;
  }

  private String formatForDisplay(final ArrayList<String> itemsToFormat) {

    String nameString = "";
    final LocalisableString localisableString =
      new LocalisableString(COMMONINTAKEGENERAL.CLIENT_NAME_SEPARATOR);
    final Iterator var4 = itemsToFormat.iterator();
    while (var4.hasNext()) {
      final String itemToDisplay = (String) var4.next();

      if (nameString.length() == 0) {
        nameString = nameString + itemToDisplay;

      } else {
        nameString = nameString
          + localisableString.getMessage(TransactionInfo.getProgramLocale())
          + itemToDisplay;
      }
    }

    return nameString;
  }

  private void validateSearchCriteria(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria)
    throws InformationalException {

    this.validateSearchCriteriaEntered(applicationCaseSearchCriteria);

    if (applicationCaseSearchCriteria.applicationDate.isZero()
      && applicationCaseSearchCriteria.applicationReference.isEmpty()
      && applicationCaseSearchCriteria.caseReference.isEmpty()
      && !applicationCaseSearchCriteria.caseTypeList.isEmpty()
      && !applicationCaseSearchCriteria.statusList.isEmpty()
      && applicationCaseSearchCriteria.concernRoleID == 0L) {

      ValidationHelper
        .addValidationError(APPLICATIONCASESEARCHExceptionCreator
          .ERR_FV_PLEASE_SPECIFY_ADDITIONAL_SEARCH_CRITERIA());

      ValidationHelper.failIfErrorsExist();

    }

    this.validateCaseTypeMinimumSearchCriteria(applicationCaseSearchCriteria);

    this.validateStatusMinimumSearchCriteria(applicationCaseSearchCriteria);

    this.validateDateMinimumSearchCriteria(applicationCaseSearchCriteria);
  }

  private void validateReferralSearchCriteria(
    final ReferralCaseSearchCriteria referralCaseSearchCriteria)
    throws InformationalException {

    this.validateAtleasetOneReferralSearchCriteriaEntered(
      referralCaseSearchCriteria);

    if (referralCaseSearchCriteria.caseReference.isEmpty()
      && referralCaseSearchCriteria.referralReference.isEmpty()
      && referralCaseSearchCriteria.concernRoleID == 0L) {

      this.validateMinimumReferralSearchCriteria(referralCaseSearchCriteria);
      this.validateCaseTypeAndStatusMinimumSearchCriteria(
        referralCaseSearchCriteria);

    }

  }

  private void processApplications(
    final BDMMyApplicationsList myApplicationsList,
    final Map<Long, List<BDMMyApplications>> applicationSearchResultsMap) {

    long applicationBeingProcessed = 0L;

    List<BDMMyApplications> applicationResultsList = new ArrayList();

    BDMMyApplications results;
    for (final Iterator var6 = myApplicationsList.dtls.iterator(); var6
      .hasNext(); applicationResultsList.add(results)) {
      results = (BDMMyApplications) var6.next();
      if (results.caseID != applicationBeingProcessed) {

        if (!applicationResultsList.isEmpty()
          || applicationBeingProcessed != 0L) {

          applicationSearchResultsMap.put(applicationBeingProcessed,
            applicationResultsList);

        }

        applicationBeingProcessed = results.caseID;
        applicationResultsList = new ArrayList();

      }
    }

    if (!applicationResultsList.isEmpty()
      || applicationBeingProcessed != 0L) {
      applicationSearchResultsMap.put(applicationBeingProcessed,
        applicationResultsList);
    }

  }

  private void validateMinimumReferralSearchCriteria(
    final ReferralCaseSearchCriteria referralCaseSearchCriteria)
    throws InformationalException {

    int countSearch = 0;

    final ArrayList<String> referralSearchDetails = new ArrayList();
    referralSearchDetails.add(referralCaseSearchCriteria.caseTypeList);
    referralSearchDetails.add(referralCaseSearchCriteria.statusList);
    if (!referralCaseSearchCriteria.referralDate.isZero()) {
      referralSearchDetails.add("" + referralCaseSearchCriteria.referralDate);
    }

    referralSearchDetails.add(referralCaseSearchCriteria.referralSource);
    referralSearchDetails.add(referralCaseSearchCriteria.referralReason);

    final Iterator var4 = referralSearchDetails.iterator();
    while (var4.hasNext()) {
      final String element = (String) var4.next();
      if (!element.isEmpty()) {

        ++countSearch;
      }
    }

    if (countSearch == 1) {

      ValidationHelper.addValidationError(REFERRALCASESEARCHExceptionCreator
        .ERR_RF_PLEASE_SPECIFY_ADDITIONAL_SEARCH_CRITERIA());

      ValidationHelper.failIfErrorsExist();

    }

  }

  private void validateCaseTypeAndStatusMinimumSearchCriteria(
    final ReferralCaseSearchCriteria referralCaseSearchCriteria)
    throws InformationalException {

    if (!referralCaseSearchCriteria.caseTypeList.isEmpty()
      && !referralCaseSearchCriteria.statusList.isEmpty()
      && referralCaseSearchCriteria.referralDate.isZero()
      && referralCaseSearchCriteria.referralSource.isEmpty()
      && referralCaseSearchCriteria.referralReason.isEmpty()) {

      ValidationHelper.addValidationError(REFERRALCASESEARCHExceptionCreator
        .ERR_RF_PLEASE_SPECIFY_ADDITIONAL_SEARCH_CRITERIA());

      ValidationHelper.failIfErrorsExist();

    }

  }

  private void validateCaseTypeMinimumSearchCriteria(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria)
    throws InformationalException {

    if (applicationCaseSearchCriteria.applicationDate.isZero()
      && applicationCaseSearchCriteria.applicationReference.isEmpty()
      && applicationCaseSearchCriteria.caseReference.isEmpty()
      && !applicationCaseSearchCriteria.caseTypeList.isEmpty()
      && applicationCaseSearchCriteria.statusList.isEmpty()
      && applicationCaseSearchCriteria.concernRoleID == 0L) {

      ValidationHelper
        .addValidationError(APPLICATIONCASESEARCHExceptionCreator
          .ERR_FV_PLEASE_SPECIFY_ADDITIONAL_SEARCH_CRITERIA());

      ValidationHelper.failIfErrorsExist();
    }

  }

  private void validateStatusMinimumSearchCriteria(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria)
    throws InformationalException {

    if (applicationCaseSearchCriteria.applicationDate.isZero()
      && applicationCaseSearchCriteria.applicationReference.isEmpty()
      && applicationCaseSearchCriteria.caseReference.isEmpty()
      && applicationCaseSearchCriteria.caseTypeList.isEmpty()
      && !applicationCaseSearchCriteria.statusList.isEmpty()
      && applicationCaseSearchCriteria.concernRoleID == 0L) {

      ValidationHelper
        .addValidationError(APPLICATIONCASESEARCHExceptionCreator
          .ERR_FV_PLEASE_SPECIFY_ADDITIONAL_SEARCH_CRITERIA());

      ValidationHelper.failIfErrorsExist();
    }

  }

  private void validateDateMinimumSearchCriteria(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria)
    throws InformationalException {

    if (!applicationCaseSearchCriteria.applicationDate.isZero()
      && applicationCaseSearchCriteria.applicationReference.isEmpty()
      && applicationCaseSearchCriteria.caseReference.isEmpty()
      && applicationCaseSearchCriteria.caseTypeList.isEmpty()
      && applicationCaseSearchCriteria.statusList.isEmpty()
      && applicationCaseSearchCriteria.concernRoleID == 0L) {

      ValidationHelper
        .addValidationError(APPLICATIONCASESEARCHExceptionCreator
          .ERR_FV_PLEASE_SPECIFY_ADDITIONAL_SEARCH_CRITERIA());

      ValidationHelper.failIfErrorsExist();
    }

  }

  private void validateSearchCriteriaEntered(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria)
    throws InformationalException {

    if (applicationCaseSearchCriteria.applicationDate.isZero()
      && applicationCaseSearchCriteria.applicationReference.isEmpty()
      && applicationCaseSearchCriteria.caseReference.isEmpty()
      && applicationCaseSearchCriteria.caseTypeList.isEmpty()
      && applicationCaseSearchCriteria.statusList.isEmpty()
      && applicationCaseSearchCriteria.concernRoleID == 0L) {

      ValidationHelper
        .addValidationError(APPLICATIONCASESEARCHExceptionCreator
          .ERR_FV_SEARCH_CRITERIA_MUST_BE_ENTERED());

      ValidationHelper.failIfErrorsExist();
    }

  }

  private void validateAtleasetOneReferralSearchCriteriaEntered(
    final ReferralCaseSearchCriteria referralCaseSearchCriteria)
    throws InformationalException {

    int countSearch = 0;

    final ArrayList<String> referralSearchDetails = new ArrayList();
    referralSearchDetails.add(referralCaseSearchCriteria.caseReference);
    referralSearchDetails.add(referralCaseSearchCriteria.referralReference);

    if (!referralCaseSearchCriteria.referralDate.isZero()) {
      referralSearchDetails.add("" + referralCaseSearchCriteria.referralDate);
    }

    if (referralCaseSearchCriteria.concernRoleID != 0L) {
      referralSearchDetails
        .add(" " + referralCaseSearchCriteria.concernRoleID);
    }

    referralSearchDetails.add(referralCaseSearchCriteria.caseTypeList);
    referralSearchDetails.add(referralCaseSearchCriteria.statusList);
    referralSearchDetails.add(referralCaseSearchCriteria.referralSource);
    referralSearchDetails.add(referralCaseSearchCriteria.referralReason);

    final Iterator var4 = referralSearchDetails.iterator();
    while (var4.hasNext()) {
      final String element = (String) var4.next();
      if (!element.isEmpty()) {

        ++countSearch;
      }
    }

    if (countSearch == 0) {

      ValidationHelper.addValidationError(REFERRALCASESEARCHExceptionCreator
        .ERR_RF_SEARCH_CRITERIA_MUST_BE_ENTERED());

      ValidationHelper.failIfErrorsExist();

    }

  }

  private StringBuffer buildConcernRoleEnteredWhereClause(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria) {

    final StringBuffer whereBuffer = new StringBuffer();
    whereBuffer
      .append("CaseParticipantRole.participantRoleID = :concernRoleID");
    if (!applicationCaseSearchCriteria.statusList.isEmpty()) {
      whereBuffer
        .append(this.getStatusWhereClause(applicationCaseSearchCriteria));
    }
    if (!applicationCaseSearchCriteria.caseTypeList.isEmpty()) {
      whereBuffer
        .append(this.getCaseTypeWhereClause(applicationCaseSearchCriteria));
    }
    if (!applicationCaseSearchCriteria.applicationDate.isZero()) {
      whereBuffer
        .append(" AND ApplicationCase.applicationDate >= :applicationDate");
    }
    return whereBuffer;
  }

  private StringBuffer getStatusWhereClause(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria) {

    StringBuffer whereBuffer = new StringBuffer();
    final StringList formattedStringList = StringUtil
      .tabText2StringListWithTrim(applicationCaseSearchCriteria.statusList);
    whereBuffer.append(" AND ApplicationCase.status IN ( ");
    for (int i = 0; i < formattedStringList.size(); ++i) {
      final APPLICATIONCASESTATUSEntry status =
        APPLICATIONCASESTATUSEntry.get(formattedStringList.item(i));
      whereBuffer.append("'");
      whereBuffer.append(status.getCode());
      whereBuffer.append("',");

    }
    if (formattedStringList.size() > 0) {
      whereBuffer =
        new StringBuffer(whereBuffer.substring(0, whereBuffer.length() - 1));
    }
    whereBuffer.append(")");
    return whereBuffer;
  }

  private StringBuffer getCaseTypeWhereClause(
    final ApplicationCaseSearchCriteria applicationCaseSearchCriteria) {

    StringBuffer whereBuffer = new StringBuffer();
    final StringList formattedStringList = StringUtil
      .tabText2StringListWithTrim(applicationCaseSearchCriteria.caseTypeList);
    whereBuffer
      .append(" AND ApplicationCaseAdmin.applicationCaseAdminID IN ( ");
    for (int i = 0; i < formattedStringList.size(); ++i) {
      whereBuffer.append(formattedStringList.item(i));
      whereBuffer.append(",");
    }
    if (formattedStringList.size() > 0) {
      whereBuffer =
        new StringBuffer(whereBuffer.substring(0, whereBuffer.length() - 1));
    }
    whereBuffer.append(")");
    return whereBuffer;
  }

  public boolean allowApplicationFormCreationInd() {

    final List<IntakeApplicationType> internalIntakeApplicationTypes =
      this.intakeApplicationTypeDAO
        .listActiveByChannel(ApplicationChannelEntry.INTERNAL);

    if (internalIntakeApplicationTypes.size() > 0) {
      final Iterator<IntakeApplicationType> intakeApplicationTypeIterator =
        internalIntakeApplicationTypes.iterator();
      while (intakeApplicationTypeIterator.hasNext()) {
        final IntakeApplicationType applicationType =
          intakeApplicationTypeIterator.next();
        final List<ProgramType> programTypeList =
          applicationType.listActiveProgramTypes();
        final Iterator<ProgramType> programTypeIterator =
          programTypeList.iterator();
        while (programTypeIterator.hasNext()) {
          final ProgramType programType = programTypeIterator.next();
          // if there is already an in progress application, then dont show

          if (!ProcessingSystemTypeEntry.CURAM.getCode()
            .equals(programType.getIntakeSystemType().getCode())) {
            return true;
          }
          if (CASETYPECODEEntry.APPLICATION_CASE.getCode()
            .equals(programType.getCaseType().getCode())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean allowApplicationFormCreationForConcernRoleInd(
    final ConcernRoleKey key) throws AppException, InformationalException {

    final ApplicationFormDetailsList applicationFormDetailsList =
      applicationForm.listApplicationFormsForConcernRole(key);
    ApplicationFormDetails applicationFormDetails;
    final Iterator<ApplicationFormDetails> applicationFormDetailsListIterator =
      applicationFormDetailsList.dtls.iterator();
    while (applicationFormDetailsListIterator.hasNext()) {
      applicationFormDetails = applicationFormDetailsListIterator.next();
      if (applicationFormDetails.status
        .equals(APPLICATIONFORMSTATUS.INPROGRESS)) {
        return false;
      }
    }
    return this.allowApplicationFormCreationInd();
  }

  public boolean allowApplicationFormCreationForConcernRoleInd(
    final BDMSelectInternalIntakeApplication key)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = key.concernRoleID;
    final ApplicationFormDetailsList applicationFormDetailsList =
      applicationForm.listApplicationFormsForConcernRole(concernRoleKey);
    ApplicationFormDetails applicationFormDetails;
    final Iterator<ApplicationFormDetails> applicationFormDetailsListIterator =
      applicationFormDetailsList.dtls.iterator();
    while (applicationFormDetailsListIterator.hasNext()) {
      applicationFormDetails = applicationFormDetailsListIterator.next();
      if (applicationFormDetails.status
        .equals(APPLICATIONFORMSTATUS.INPROGRESS)) {
        return false;
      }
    }
    return true;
  }

  public boolean allowDirectCreationInd() {

    final List<ApplicationCaseAdmin> applicationCaseAdminList =
      this.applicationCaseAdminDAO.listActive();
    for (final ApplicationCaseAdmin applicationCaseAdmin : applicationCaseAdminList) {
      if (applicationCaseAdmin.isDirectCreationAllowed())
        return true;
    }
    return false;
  }

}
