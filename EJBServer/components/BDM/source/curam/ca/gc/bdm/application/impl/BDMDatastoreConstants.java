
package curam.ca.gc.bdm.application.impl;

public class BDMDatastoreConstants {

  public static final String BDM_STIMULUS_SCHEMA = "BDMStimulusApplication";

  public static final String APPLICATION = "Application";

  public static final String ROOT_ENTITY = "Application";

  public static final String EXTERNAL_USER_NAME = "userName";

  public static final String EXTERNAL_USER_TYPE = "externalUserType";

  public static final String USER_TYPE = "userType";

  public static final String UNAUTHED_USER = "unauthenticated";

  public static final String INTERNAL = "INTERNAL";

  public static final String LINKED_CITIZEN_ROLE = "LINKEDCITIZENROLE";

  public static final String REGISTERED_CITIZEN_ROLE = "CITIZENROLE";

  public static final String PERSON = "Person";

  public static final String FIRST_NAME = "firstName";

  public static final String LAST_NAME = "lastName";

  public static final String MIDDLE_NAME = "middleName";

  public static final String GENDER = "gender";

  public static final String PARENT_LAST_NAME = "ParentLastName";

  public static final String PREFERRED_NAME = "PreferredName";

  public static final String PREFERRED_LANGUAGE = "CommunicationPref";

  public static final String PREFERRED_WRITTEN_LANGUAGE = "languageW";

  public static final String PREFERRED_SPOKEN_LANGUAGE = "languageS";

  public static final String PAYMENT = "Payment";

  public static final String SIN = "sinRaw";

  public static final String SOCIAL_INSURANCE_NUMBER = "SIN";

  public static final String SSN = "ssn";

  public static final String POSTAL_CODE = "postalCode";

  public static final String ZIP_CODE = "zipCode";

  public static final String CITY = "city";

  public static final String PROVINCE = "province";

  public static final String STATE = "state";

  public static final String ADDRESS_LINE1 = "addressLine1";

  public static final String ADDRESS_LINE2 = "addressLine2";

  public static final String ADDRESS_LINE3 = "addressLine3";

  public static final String ADDRESS_LINE4 = "addressLine4";

  public static final String SUITE_NUM = "suiteNum";

  public static final String CONTACT_INFORMATION = "ContactInformation";

  public static final String PAYMENT_METHOD = "preferredPaymentMethod";

  public static final String IS_MAILING_ADDRESS_DIFFERENT = "resMail";

  public static final String IS_MAILING_ADDRESS_UPDATE =
    "isMailingAddressUpdate";

  public static final String RESIDENTIAL_ADDRESS = "ResidentialAddress";

  public static final String RESIDENTIAL_COUNTRY = "residentialCountry";

  public static final String MAILING_ADDRESS = "MailingAddress";

  public static final String MAILING_COUNTRY = "mailingCountry";

  public static final String INTL_RESIDENTIAL_ADDRESS = "IntlResAddress";

  // BEGIN TASK -26500 -->
  public static final String INTL_MAILING_ADDRESS = "IntlMailAddress";

  // END TASK -26500
  public static final String ADDRESS_COUNTRY = "country";

  public static final String DEPENDANT_AT_SAME_RESIDENCE = "atSameResidence";

  public static final String START_LIVING_SAME_RES_DATE =
    "startLivingAtSameResidenceDate";

  public static final String IS_PRIMARY_PARTICIPANT = "isPrimaryParticipant";

  public static final String ADDRESS_DATA = "addressData";

  public static final String ADDRESS = "address";

  public static final String LAST_NAME_AT_BIRTH = "LastNameAtBirth";

  public static final String PARENT_LAST_NAME_AT_BIRTH = "ParentLastName";

  public static final String MARITAL_STATUS = "maritalStatus";

  public static final String DATE_OF_BIRTH = "dateOfBirth";

  public static final String DATE_OF_APPLICATION = "dateOfApplication";

  public static final String INTAKE_APPLICATION = "IntakeApplication";

  public static final String INTAKE_APPLICATION_TYPE =
    "IntakeApplicationType";

  public static final String INTAKE_APPLICATION_TYPE_ID =
    "intakeApplicationTypeID";

  public static final String FRENCH = "French";

  public static final String ENGLISH = "English";

  public static final String ALERT_PREFERENCES = "AlertPreferences";

  public static final String ALERT_OCCUR = "alertOccur";

  public static final String RECEIVE_ALERT = "receiveAlerts";

  public static final String COMMUNICATION_DETAILS = "CommunicationDetails";

  public static final String INCOME_ENTITY = "Income";

  public static final String INCOME_AMOUNT = "incomeAmount";

  public static String COUNTRY_CODE = "countryCode";

  public static String PHONE_AREA_CODE = "phoneAreaCode";

  public static String PHONE_NUMBER = "phoneNumber";

  public static String IS_PRIMARY_COMM = "isPrimaryCommunication";

  public static String PHONE_TYPE = "phoneType";

  public static String PHONE_EXT = "phoneExt";

  public static String PHONE_ALT_PREF = "alertPrefPhone";

  public static String PHONE_IS_MOR = "isMor";

  public static String PHONE_IS_AFTNOON = "isAftr";

  public static String PHONE_IS_EVE = "isEve";

  // TASK 28598 - best time to call values for display on summary page.
  public static String IS_MOR_DISPLAY_VALUE = "isMorDisplayValue";

  public static String IS_AFTNOON_DISPLAY_VALUE = "isAftrDisplayValue";

  public static String IS_EVE_DISPLAY_VALUE = "isEveDisplayValue";
  // END TASK 28598

  public static String EMAIL_ADDRESS = "emailAdr";

  public static String EMAIL_TYPE = "emailType";

  public static String ALT_PREF_EMAIL = "alertPrefEmail";

  public static String EMAIL_ADD_TYPE = "emailAddressType";

  public static final String EMAIL_ADR_VALID = "emailAdrValid";

  public static final String PREFERRED_COMMUNICATION =
    "isPreferredCommunication";

  public static final String WS_RES_CONTINUE = "wsResValidOrOverride";

  public static final String WS_MAIL_CONTINUE = "wsMailValidOrOverride";

  public static final String BANK_ACCT_NUM = "accountNumber";

  public static final String BANK_SORT_CODE = "sortCode";

  public static final String BANK_BRANCH_NUM = "bankBranchNum";

  public static final String BANK_TRANSIT_NUM = "branchTransitNumber";

  public static final String BANK_INSTITUTION_CODE = "bankInstNum";

  public static final String BANK_ACCOUNT_NAME = "bankAccountName";

  public static final String ACCOUNT_NAME = "accountName";

  public static final String RECEIVE_CORRESPONDENCE = "recieveCorrespondPref";

  // BEGIN TASK 17611 - IEG Mapping to child table

  public static final String MINORITY_GROUP = "MinorityGroup";

  public static final String EDUCATION_LEVEL = "EducationLevel";

  public static final String INDIGENOUS_PERSON = "IndigenousPerson";

  // END TASK 17611 - IEG Mapping to child table

  public static final String VALID_COMMUNICATION = "isValidCommunication";

  // BEGIN TASK 21129 - map evidence information
  public static final String COMMUNICATION_PREF = "CommunicationPref";
  // END TASK 21129 - map evidence information

  public static String EMAIL = "email";

  // BEGIN TASK 9418 - input application for client
  public static final String ONE_LINE_ADDRESS = "oneLineAddress";

  public static final String ADDRESS_DATA1 = "AddressData";

  public static final String RESIDENTIAL = "Residential";

  public static final String MAILING = "Mailing";

  public static final String SEARCH_ADDRESS = "SearchAddress";

  public static final String IS_ACTIVE = "isActive";

  public static final String FORMATTED_ADDRESS_DATA = "formatedAddressData";

  public static final String ID_MAILING_ADDRESS = "idMailingAddress";

  public static final String IS_RESIDENTIAL_ADDRESS = "isResidentialAddress";

  public static final String IS_MAILING_ADDRESS = "isMailingAddress";

  public static final String IS_SELECTED = "isSelected";

  public static final String TRUE = "true";

  public static final String STEP_1 = "STEP1";

  public static final String STEP_2 = "STEP2";
  // END TASK 9418 - input application for client

  public static final String PHONE_NUMBER_SELECTED = "phoneNumberSelected";

  public static final String EMAIL_ADR_SELECTED = "emailAdrSelected";

}
