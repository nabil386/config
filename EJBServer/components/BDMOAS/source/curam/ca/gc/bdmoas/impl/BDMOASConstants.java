package curam.ca.gc.bdmoas.impl;

/**
 * BDMOAS Constants used for hard coded strings
 *
 */
public class BDMOASConstants {

  public static final String kCanada = "Canada";

  public static final String kSSACountryCreated = "SSA Country created by";

  public static final String kSSACountryModified = "SSA Country modified by";

  public static final String kOn = "on";

  public static final String kBDMAdmin = "BDMADMIN";

  public static final String kIOAdmin = "IOADMIN";

  public static final String kView = "View";

  public static final String kDetails = "Details";

  public static final String kAgreement = "Agreement";

  public static final String kBDMOASFA = "BDMOASFA";

  public static final String gkCommaSpace = ", ";

  public static final String gkBESSIndYes = "Yes";

  public static final String gkBESSIndNo = "No";

  public static final char kNewLine = '\n';

  public static final char kComma = ',';

  public static final String kHiphen = " - ";

  public static final String kSaveAndClose = "saveandclose";

  public static final String kNext = "next";

  public static final String kSave = "save";

  public static final String kBack = "back";

  // BEGIN TASK 74615 DEV: Implement Manually Reported Income

  public static final String kDOB = "dateOfBirth";

  public static final String kDOD = "dateOfDeath";

  public static final int kTwo = 2;

  public static final int kThirty = 30;

  // END TASK 74615 DEV: Implement Manually Reported Income

  public static final String kStep1Next = "step1next";

  public static final String kStep2Next = "step2next";

  public static final String kStep1SaveAndClose = "step1saveandclose";

  public static final String kStep2SaveAndClose = "step2saveandclose";

  public static final String kStep3Back = "step3back";

  public static final String kStep2Back = "step2back";

  public static final String gkYes = "Yes";

  public static final String gkNo = "No";

  public static final String kBDMOASFLKEYSET = "BDMOASFL";

  public static final String kBDMOASDEFKEYSET = "BDMOASDEF";

  // BEGIN DEV TASK 90155: Foreign Income Changes
  public static final int kThree = 3;
  // END DEV TASK 90155: Foreign Income Changes

  public static final String BDM_OAS_APPLICATIONCASE_HOME_PAGE_ID =
    "CommonIntake_applicationCaseHome";

  public static final String BDM_OAS_IC_CASE_HOME_PAGE_ID =
    "BDMOASIntegratedCase_home";

  public static final long OAS_PENSION_PROGRAM_TYPE_ID = 920000l;

  public static final long GIS_PROGRAM_TYPE_ID = 920001l;

  public static final long OAS_GIS_APP_ADMIN_ID = 920000l;

  public static String BDM_OAS_LOGS_PREFIX = "-- BDMOAS -- ";

  public static String BDM_OAS_LOG_FORMATTER = "{}{}";

  public static String BDM_OAS_LOG_FORMATTER_3 = "{}{}{}";

  public static String BDM_OAS_LOG_FORMATTER_4 = "{}{}{}{}";

  public static String BDM_OAS_LOG_FORMATTER_5 = "{}{}{}{}{}";

  public static String BDM_OAS_EQUALS = " = ";

  // START: Task 93502: CRA GIS Takeup
  public static final String BDM_OAS_CRA_MARITAL_STATUS_SINGLE = "6";

  public static final String BDM_OAS_CRA_MARITAL_STATUS_SEPARATED = "5";

  public static final String BDM_OAS_CRA_MARITAL_STATUS_DIVORCED = "4";

  public static final String BDM_OAS_CRA_MARITAL_STATUS_WIDOWED = "3";

  public static final String BDM_OAS_CRA_MARITAL_STATUS_COMMONLAW = "2";

  public static final String BDM_OAS_CRA_MARITAL_STATUS_MARRIED = "1";

  public static final String BDM_OAS_CRA_MARITAL_STATUS_UNKNOWN = "0";

  public static final String BDM_OAS_CRA_ACCOUNT_STATUS_ACTIVE = "Active";

  public static final String BDM_OAS_CRA_ACCOUNT_STATUS_INACTIVE = "Inactive";

  public static final String BDM_OAS_CRA_INCOME_STATUS_FOUND = "01";

  public static final String BDM_OAS_CRA_INCOME_STATUS_NOT_FOUND = "50";

  // END: Task 93502: CRA GIS Takeup

  // BEGIN: Bug 118884: Income Status Update
  public static final String BDM_OAS_CRA_INCOME_STATUS_NOT_SET = "00";
  // END: Bug 118884: Income Status Update

  public static final String CASE_ID = "caseID=";

  public static final String INCOMPLETE_DATA = "Incomplete Data";

  public static final int OASGISPROGRAMTYPEID = 920001;

  public static final String GIS_PROGRM_TYP_REF =
    "GuaranteedIncomeSupplementProgram";

  public static String ERROR_SETING_PARTNER_DETAILS =
    "ERROR while prepopulating partner details ";

  public static final String OAS_PROGRM_TYP_REF = "OASPensionProgram";

  public static final String ERROR_PHONE_EVIDENCE_MAPPING =
    "Error occured while mapping phone evidence for concern role: ";

  public static final String ERROR_PREPOPULATING_INTAKE_DETAILS =
    "Error occured while pre-populating details: ";

  public static final String ERROR_POST_SUBMIT_INTAKE_APP =
    "Error occured while during post submission of intake application: ";

  public static final String ERROR_PREPOPULATING_PART_ADDRESS =
    " ERROR while prepopulating partner address details - ";

  public static final String ERROR_PREPOPULATING_WITNESS_ADDRESS =
    " ERROR while prepopulating witness address details - ";

  public static final String ERROR_OCCURED_SETTING_ADDRESS =
    " ERROR while setting address data - ";

  public static final String ERROR_OCCURED_VALIDATING_ADDRESS =
    " ERROR while validating address data - ";

  public static final String kAddNew = "Add New";

  public static final int kIncmYrToCompare = 1967;

  public static final long OAS_GIS_COMBINED_INTAKE_APP_TYPE_ID = 910002L;

  public static final String ERROR_PREPOPULATING_ROOT_ENTITY =
    "Could not update root entity for application with pre-population information: ";

  // BEGIN TASK 119303: Implement evidences for GIS

  public static final String kMaritalStatus = "maritalStatus";

  public static final String kPerson = "person";

  // END TASK 119303: Implement evidences for GIS
}
