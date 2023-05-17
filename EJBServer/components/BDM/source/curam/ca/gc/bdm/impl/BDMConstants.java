package curam.ca.gc.bdm.impl;

import curam.codetable.CASEEVIDENCE;
import curam.util.type.Date;

/**
 * BDM COnstants used for Hard Coded Strings
 *
 * @author raghunath.govindaraj
 *
 */
public class BDMConstants {

  public static final String kXmlElement_emptyDecisionDetails =
    new String("<DecisionDetails/>");

  public static final String kDifferentFromPrev_DecisionDetails =
    "DifferentFromPrevDetermination";

  public static final String dependentsNamesResult = "dependentsNamesResult";

  public static final String validDependentsResults =
    "validDependentsResults";

  /** BUG 14146 Date format for Direct deposit letter and benefit letters */
  public static final String MMMM_d_YYYY = "MMMM d, YYYY";

  /*
   * This is going to be used in Benefit CER rule Eligibility check valid
   * SIN series
   */
  public static final String kCA_Accepted_9_SIN_Series = "9";

  public static final String kProvince = "province";

  public static final String kPostalCode = "postalCode";

  public static final String kCurrentDate = "currentDate";

  public static final String kCREATECORRESPONDENCEDP =
    "CREATECORRESPONDENCEDP";

  public static final String kSUBMITCORRESPONDENCEDP =
    "SUBMITCORRESPONDENCEDP";

  public static String kProgramName = "programName";

  public static String kNoMoney = "0.0";

  public static String kOfficeName = "officeName";

  public static String kOfficePostalCode = "officePostalCode";

  public static String kOfficeAddressLine = "officeAddressLine";

  public static String kAddressLine = "addressLine";

  public static String kPreferredName = "Preferred to be called: ";

  public static String kBDMCoreWorkQueueID = "80013";

  public static String kBDMMisdirectWorkQueueID = "80014";

  public static final String EMPTY_STRING = "";

  public static String CONTACT_LANGUAGE = "contactLanguage";

  public static String BDM_LOGS_PREFIX = "-- BDM -- ";

  public static String PERSON = "PERSON";

  // Begin Date Util formats
  public static final String YYYYMMDD_DATE_FORMAT = "yyyyMMdd";

  public static final String DD_MM_YYYY = "ddMMYYYY";

  public static final String DD_MONTH_YYYY_DATE_FORMAT = "dd MMMM yyyy";

  public static final String DD_MON_YYYY_DATE_FORMAT = "dd-MMM-yyyy";

  public static final String PR_DD_MN_YYYY_DATE_FORMAT_SLASH_DELIMITER =
    "dd/MM/yyyy";

  public static final String MONTH_DD_YYYY_DATE_FORMAT = "MMMMM dd,yyyy";

  public static final String MONTH_YYYY_DATE_FORMAT = "MMMMM yyyy";

  public static final String MONTH_DD_YYYY_HH_MM_DATE_FORMAT =
    "MMMMM dd,yyyy 'at' HH:mm a";

  public static final String MONTH_DD_YYYY_HH_MM_SS_DATE_FORMAT =
    "MM.dd.yyyy HH.mm.ss";

  public static final String DD_MONTH_YYYY_HH_MM_SS_DATE_FORMAT =
    "dd.MM.yyyy HH.mm.ss";

  public static final String YYYY_MM_DD_DATE_FORMAT_SLASH_DELIMITER =
    "yyyy/MM/dd";

  public static final String YYYY_MM_DD_DATE_FORMAT_DASH_DELIMITER =
    "yyyy-MM-dd";

  public static final String MM_DD_YYYY_DATE_FORMAT_SLASH_DELIMITER =
    "MM/dd/yyyy";

  public static final String MM_DD_YYYY_DATE_FORMAT_DASH_DELIMITER =
    "MM-dd-yyyy";

  public static final String MM_DD_YYYY_HH_MM_DATE_FORMAT_SLASH_DELIMITER =
    "MM/dd/yyyy HH:mm";

  public static final String MM_DD_YYYY_HH_MM_SS_DATE_FORMAT_SLASH_DELIMITER1 =
    "mm/dd/yyyy hh:mm:ss";

  public static final String MM_DD_YYYY_HH_MM_SS_DATE_FORMAT_SLASH_DELIMITER2 =
    "MM/dd/yyyy hh:mm:ss";

  public static final String YYYY_MM_DD_HH_MM_SS_SSS_DATE_FORMAT_DASH_DELIMITER =
    "yyyy-MM-dd HH:mm:ss:SSS";

  public static final String MONTH_FORMAT = "MMMM";

  public static final String YEAR_FORMAT = "yyyy";

  public static final String TWENTY_FOUR_HOUR_TIME_FORMAT = "HH:mm:ss";

  public static final String HH_MM_FORMAT = "hhmm";

  public static final String YYYY_MM_DD_HH_MM_SS_DATE_FORMAT =
    "yyyyMMdd:HH:mm:ss";
  // End Date Util formats

  // address element constants
  public static final String kCANADA_ADDRESSCCOUNTRY_Code = "CA";

  public static final String kADDRESSELEMENT_POBOX = "POBOXNO";

  public static final String kADDRESSELEMENT_STREETNUM = "ADD1";

  public static final String kADDRESSELEMENT_STREETNAME = "ADD2";

  public static final String kADDRESSELEMENT_POSTALCODE = "POSTCODE";

  public static final String kADDRESSELEMENT_PROVINCE = "PROV";

  public static final String kADDRESSELEMENT_STATEPROV = "STATEPROV";

  public static final String kADDRESSELEMENT_BDMSTPROVX = "BDMSTPROVX";

  public static final String kADDRESSELEMENT_ZIP = "ZIP";

  public static final String kADDRESSELEMENT_BDMZIPX = "BDMZIPX";

  public static final String kADDRESSELEMENT_CITY = "CITY";

  public static final String kADDRESSELEMENT_COUNTRY = "COUNTRY";

  public static final String kHyphen = "-";

  public static final String kADDRESSELEMENT_APTUNITNUM = "APT";

  public static final String kADDRESS_POBOX_STRING = "PO BOX";

  public static String kAddressPostalCode = "AddressPostalCode";

  public static String kReturnAddressLine = "ReturnAddressLine";

  public static String kAccept = "Accept";

  public static String kAccept_Language = "Accept-Language";

  public static String kApplication_json = "application/json";

  public static String kSubscription_Key = "Ocp-Apim-Subscription-Key";

  /** Start GCNotify Constants */
  public static final String kHttpProxyHostAttribute = "http.proxyHost";

  public static final String kHTTP_PROXY_HOST = "proxy.prv";

  public static final String kHttpProxyPortAttribute = "http.proxyPort";

  public static final String kHTTP_PROXY_PORT = "80";

  public static final String kHttpsProxyHostAttribute = "https.proxyHost";

  public static final String kHTTPS_PROXY_HOST = "proxy.prv";

  public static final String kHttpsProxyPortAttribute = "https.proxyPort";

  public static final String kHTTPS_PROXY_PORT = "443";

  public static final int kGCNotifyConnectionTimeout = 15000;

  public static final String kAuthorizationAttribute = "Authorization";

  public static final String kPOSTRequestAttribute = "POST";

  public static final String kGETRequestAttribute = "GET";

  public static final String kGCNotifyBulkSendSubjectAttribute =
    "BDM GCNotify Bulk";

  public static final String kEmailAtributeUpper = "EMAIL";

  public static final String kEmailAtributeLower = "email";

  public static final String kGCNotifySmsAtributeUpper = "SMS";

  public static final String kGCNotifySmsAtributeLower = "sms";

  public static final String kPhone_numberAtributeLower = "phone number";

  public static final String kEmailAddressAtributeLower = "email address";

  public static final String kNameAtributeLower = "name";

  public static final String kContent_TypeAtribute = "Content-Type";

  public static final String kRemoteAPIErrorMessage = "Remote API error";

  public static final String kBadRequestErrorMessage = "Bad request error";

  public static final String kForbiddenErrorMessage = "Forbidden error";

  public static final String kSuccessMessage = "Success";

  public static final String kGCNotifyAPIErrorMessage = "GCNotify API error";

  public static final String kRemoteGCNotifyAPIErrorMessage =
    "Remote GCNotify API error";

  /** End GCNotify Constants */

  /** The Constant for French language. */
  public static final String kLanguageFrench = "French";

  /** The Constant for English language. */
  public static final String kLanguageEnglish = "English";

  /**
   * The Constant for Address interface request header English Canada locale.
   */
  public static final String kAddress_Search_Locale_en_CA = "en-CA";

  /** The Constant for Address interface request header French Canada locale. */
  public static final String kAddress_Search_Locale_fr_CA = "fr-CA";

  /** The Constant for the Default Country. */
  public static final String kCountryCanada = "CAN";

  public static final String kAmbersand = "&";

  public static final int kHTTP_STATUS_SUCCESS_CODE_200 = 200;

  public static final int kHTTP_STATUS_SUCCESS_CODE_201 = 201; // GCNOTIFY BULK
  // Success
  // response

  public static String gkEquals = "=";

  public static final int kHTTP_STATUS_BAD_REQUEST_CODE_400 = 400;

  public static final int kHTTP_STATUS_BAD_REQUEST_CODE_403 = 403;

  public static final int kHTTP_STATUS_INTERNAL_SERVER_ERROR_500 = 500; // GCNOTIFY
  // BULK
  // Server
  // error
  // response

  public static final String kAlternateID = "alternateID";

  public static final String kTemplateID = "templateID";

  public static final String kPhoneNumEnquiries = "phoneNumEnquiries";

  public static final String kPhoneNumTTY = "phoneNumTTY";

  public static final String kPhoneNumINTL = "phoneNumINTL";

  public static String kAddressCityName = "AddressCityName";

  public static String kAddressFullText = "AddressFullText";

  public static String kProvinceCode = "ProvinceCode";

  public static final String kPDFFileExtensionString = ".pdf";

  public static final String internationalProcessLocation = "intl";

  public static final String gkLocaleEN = "en";

  public static final String kpreferredLanguage = "preferredLanguage";

  public static final String kpreferredWrittenLanguage =
    "preferredWrittenLanguage";

  public static final String kpreferredOralLanguage = "preferredOralLanguage";

  public static final String kpreferredCommunication =
    "preferredCommunication";

  public static final String kpreferredCommunicationMethod =
    "preferredCommunicationMethod";

  public static final String kphonePrefix = "+1";

  public static String kCommunicationFailedTaskDefinition =
    "BDMMANUALCOMMUNICATION";

  public static String kCommunicationStatusTaskDefinitionParticipant =
    "BDMMANUALCOMMUNICATIONPARTICIPANT";

  public static String kCommunicationStatusTaskDefinitionCase =
    "BDMMANUALCOMMUNICATIONCASE";

  public static final long kBenefitDenialLetterID = 2004l;

  public static final String kphonePrefixW = "1";

  // Begin Citizen Message constants
  public static String kCitizenMessageMyPayments = "CitizenMessageMyPayments";

  public static String kCitizenMessageCaseNameAttr = "Case.Name";

  public static String kPaymentMessagePaymentAmount = "Payment.Amount";

  public static String kPaymentMessagePaymentDueDate = "Payment.Due.Date";

  public static String kPaymentUnderThresholdMessage =
    "BDMMessage.UnderThreshold.Payment";

  public static String kPaymentUnderThresholdMessageTitle =
    "BDMMessage.UnderThreshold.Title";

  public static String kPaymentNilMessage = "BDMMessage.Nil.Payment";

  public static String kPaymentNilMessageTitle = "BDMMessage.Nil.Title";

  public static String kPaymentCancelledMessage = "Message.Cancelled.Payment";

  public static String kPaymentCancelledMessageTitle =
    "BDMMessage.Cancelled.Title";

  public static String kPaymentSuspendedMessage =
    "BDM.Notification.Benfit.Suspend.Body";

  public static String kPaymentSuspendedMessageTitle =
    "BDM.Notification.Benefit.Suspend.Title";

  public static String kPaymentUnsuspendedMessage =
    "Message.Unsuspended.Payment";

  public static String kPaymentMessagePaymentNextDate = "Payment.Next.Date";

  public static String kPaymentUnsuspendedMessageTitle =
    "BDMMessage.Unsuspended.Title";

  public static String kPaymentLatestMessage = "Message.Payment.Latest";

  public static String kPaymentLatestMessageTitle = "BDMMessage.Latest.Title";

  public static String kPaymentLastMessage = "Message.Last.Payment";

  public static String kPaymentReissueMessage = "Message.Reissue.Payment";

  public static String kPaymentDueMessage = "Message.First.Payment";

  public static String kPaymentDueMessageTitle = "Message.Title";
  // End Citizen Message constants

  public static final String kStringSpace = " ";

  public static final String kGCNotifyEmailJSONAttribute = "email_address";

  public static final String kGCNotifyPhoneJSONAttribute = "phone_number";

  public static final String kGCNotifyTemplateIDJSONAttribute = "template_id";

  public static final String kGCNotifyPersonaliseJSONAttribute =
    "personalisation";

  public static final String kGCNotifyFullNameJSONAttribute = "full_name";

  public static final String kEvidenceAttrReceiveAlert = "receiveAlert";

  public static final String kEvidenceAttrAlertFrequency = "alertFrequency";

  public static final String kEvidenceAttrEmailAddress = "emailAddress";

  public static final String kEvidenceAttrPreferredInd = "preferredInd";

  public static final String kEvidenceAttrUseForAlert = "useForAlertsInd";

  public static final String kEvidenceAttrPhoneCountryCode =
    "phoneCountryCode";

  public static final String kEvidenceAttrPhoneAreaCode = "phoneAreaCode";

  public static final String kEvidenceAttrPhoneNumber = "phoneNumber";

  public static final String kNotificationUnreadMailCountAttr =
    "unreadMail.Count";

  public static final String kMaxDateTime = "99991231T000000";

  public static final String kCREATEGCNOTIFYALERTDP = "CREATEGCNOTIFYALERTDP";

  public static final String kDPTICKET = "DPTICKET";

  // TASK 12784 -- Overall Eligibility CONSTANT
  public static final String kOVERALLELIGINILITY = "Overall Eligibility";

  // BEGIN TASK 3753 -- Constants for eligibility rules criteria.
  public static final String kAgeCriteria = "At least 18 years old";

  public static final String kSinCriteria = "Acceptable status in Canada";

  public static final String kApplicationDateCriteria =
    "Application submitted in 2021 OR 2022";

  public static final String kIncomeCriteria = "Income is $100,000 or less";

  // END TASK 3753 -- Constants for eligibility rules criteria.

  // TASK 12784 -- Claim Established CONSTANT
  public static final String kPASS = "Pass";

  // TASK 12784 -- Claim Denied CONSTANT
  public static final String kFAIL = "Fail";

  public static final String kPAYMENT = "PAYMENT";

  public static final String kDEDUCTION = "DEDUCTION";

  public static final String kWhiteSpaceRegEx = "\\s+";

  public static final String kRestContentTypeAll = "*/*";

  public static final String kValid = "Valid";

  // TASK 13823 -- Direct Deposit constant

  public static final String DIRECTDEPOSIT = "Direct Deposit";

  public static final String BY = " by ";

  // Used for deduction priority calculations
  public static final int kPriorityMultiplier = 1000;

  // Added for SIN/SIR Validation interface
  public static final String kSIN = "SIN";

  public static final String kGIVEN_NAME = "GivenName";

  public static final String kSURNAME = "Surname";

  public static final String kDOB = "DateOfBirth";

  public static final String kDOD = "DateOfDeath";

  public static final String kPARENTS = "Parents";

  public static final String kIncomeAttribute = "income";

  // Task 12381 - Cancelled Payments Task
  public static final String kBDMCancelledPaymentsTask =
    "BDMCancelledPaymentsTaskWorkflow";

  public static final String kBDMCancelledPaymentsPersonMatchTask =
    "BDMCancelledPaymentsPersonMatchTaskWorkflow";

  // BEGIN TASK 9424 Suspend case Notification
  public static final String kNotificationSuspendDateAttr = "suspend.date";

  public static final String kNotificationBenefitTypeAttr = "benefit.type";
  // END TASK 9424

  public static final String kNotificationUnsuspendDateAttr =
    "unsuspend.date";

  public static final int kREST_ERROR_CODE_500 = 500;

  public static final String kIncarcerationCriteria =
    "Incarceration Criteria";

  public static final String lastDayOfTheYear = "20221231";

  public static final String kELIGIBLE = "Eligible";

  public static final String kYes = "Yes";

  public static final String kNo = "No";
  // END TASK 9395:

  public static final String kGCNotifyStatusAttribute = null;

  public static final Object kGCNotifyDelieveredStatus = null;

  public static final String kGCNotifyPermanentFailureStatus = null;

  public static final String kGCNotifyTemporaryFailureStatus = null;

  // BEGIN TASK 17650: New constants..
  public static final String kAutoDDLeterBatch =
    "Correspondence Automated Direct Deposit Letter Batch";

  public static final String kNoOfRecordsToProcess =
    "No. of Records to Process: ";

  public static final String kUnprocessedRecords = "Unprocessed Records: ";

  public static final String kProcessedRecords = "Processed Records: ";

  public static final String kSkippedRecords = "Skipped Records: ";

  public static final String kBatchProcessingStrmKey =
    "Batch processing Stream Key: ";

  public static final String kProcessed = "Processed: ";

  public static final String kSkipped = "Skipped: ";

  public static final String kBatchInstanceID = "Batch instance ID: ";

  public static final String kIDList = " IDList: ";
  // END TASK 17650:

  public static final String JV_MESSAGE = "BDM";

  public static final String JV_HEADER_TEXT = "BDM Testing - CPP";

  public static final int SAMPLEBENEFITPROGRAMTYPEID = 80001;

  public static final int DEPENDANTROGRAMTYPEID = 80002;

  public static final String PREFERRED_WRITTEN_LANGUAGE =
    "preferredWrittenLanguage";

  public static final char PAYMENT_TYPE_CHEQUE = '1';

  public static final char PARTY_PAY_IND = '0';

  public static final char PAYMENT_TYPE_EFT = '2';

  public static final char LANGUAGE_CODE_ENGLISH = '1';

  public static final char LANGUAGE_CODE_FRENCH = '2';

  public static final String kPaymentFrequencyWeekly_Fri = "100101600";

  // Begin BDMFIFInboundBatch Constants
  /** Max length of a bank or branch name. */
  public static final int kBankNamesLength = 35;

  public static final String kBDMFIFinboundKeySetName = "BDMFIFIN";

  /** FIF inbound bank branch status codes */
  public static final String kFIFbranchStatusActiveV = "V";

  public static final String kFIFbranchStatusClosedC = "C";

  public static final String kFIFbranchStatusClosedX = "X";

  /** Default po address, postcode */
  public static final String kPOAddress = "PO 0000";

  public static final String kPostCode = "L7G 3S8";

  // Begin BDMFIFInboundBatch Constants

  // Bug21988 2022-05-30 Change label from program to benefit Mohan
  public static final String DEPENDANTBENEFITPROGRAMNAME =
    "Dependant Benefit";

  public static final String MM_DD_YYYY_HH_MM_SS_DATE_FORMAT_SLASH_DELIMITER =
    "MM/dd/yyyy HH24:MI:SS";

  public static final String kYearAttribute = "year";

  public static final String kGCNotifyBulk_Correspondence_Email_English =
    "BDM GCNotify Bulk Correspondence Email English";

  public static final String kGCNotifyBulk_Correspondence_Email_French =
    "BDM GCNotify Bulk Correspondence Email French";

  public static final String kGCNotifyBulk_Correspondence_SMS_English =
    "BDM GCNotify Bulk Correspondence SMS English";

  public static final String kGCNotifyBulk_Correspondence_SMS_French =
    "BDM GCNotify Bulk Correspondence SMS French";

  public static final String kGCNotifyBulk_Notifications_Email_English =
    "BDM GCNotify Bulk Notifications Email English";

  public static final String kGCNotifyBulk_Notifications_Email_French =
    "BDM GCNotify Bulk Notifications Email French";

  public static final String kGCNotifyBulk_Notifications_SMS_English =
    "BDM GCNotify Bulk Notifications SMS English";

  public static final String kGCNotifyBulk_Notifications_SMS_French =
    "BDM GCNotify Bulk Notifications SMS French";

  public static final String kREST_Request_Header_Accpet_JSON_NIEM_Aware =
    "application/vnd.niem-aware+json";

  public static final String kREST_API_VALIDATE_SIN_SIR = "Validate SIN/SIR";

  public static final String kREST_API_WSADDRESS = "WSAddress";

  public static final int kREST_ERROR_CODE_404 = 404;

  public static final String kCASH = "Cash";

  public static final String kGLAccount = "623000";

  public static final int kZero = 0;

  // BEGIN TASK 20655 - Attestation evidence comments update
  public static final String kCOMMENTS = "comments";

  public static final String ATTESTATION_EVIDENCE_TYPE = "DETBDM8006";

  public static final String ATTESTATION_PROP_RESOURCE_NAME =
    "DynEvd_BDMAttestation_20000101.properties";

  public static final String ATTESTATION_TYPE = "attestationType";

  // END TASK 20655
  public static final String PREV = "PREV_";

  public static final String RES = "RES_";

  public static final String kIncomeLessThan30k =
    "Income between $0 - $30,000";

  public static final String kIncomeBetween30And70k =
    "Income between $30,001 - $70,000";

  public static final String kIncomeBetween70And100k =
    "Income between $70,001 - $100,000";

  public static final String kentitlementRulesCriteria =
    "entitlementRulesCriteria";

  public static final String kentitlementRulesResults =
    "entitlementRulesResults";

  public static final String kentitlementDurationDayAmountTimeline =
    "entitlementDurationDayAmountTimeline";

  public static final String kweeklySupplementEntitlementTimeline =
    "weeklySupplementEntitlementTimeline";

  public static final String knumberOfEligibleDependents =
    "numberOfEligibleDependents";

  public static final String kentitlementAmountTimeline =
    "entitlementAmountTimeline";

  public static final String kincomeTimeline = "incomeTimeline";

  public static final String kRuleIncomeCriteriaAttribute = "income.criteria";

  public static final String kRuleAgeCriteriaAttribute = "age.criteria";

  public static final String kRuleSINCriteriaAttribute = "sin.criteria";

  public static final String kRuleApplicationCriteriaAttribute =
    "application.criteria";

  public static final String kRuleCriteriaPassAttribute = "criteria.Pass";

  public static final String kRuleCriteriaFailAttribute = "criteria.Fail";

  public static final long kBDMRecalculationLetterID = 2011;

  // Constants for saving Guid information

  public static final String kquote = "\"";

  public static final String kcomma = ",";

  public static final String kleftBrace = "{";

  public static final String krightBrace = "}";

  public static final String kcountry_of_residence = "country_of_residence";

  public static final String kcountry_of_residence_quote =
    kquote + kcountry_of_residence + kquote + ": ";

  public static final String kcurrent_marital_status =
    "current_marital_status";

  public static final String kcurrent_marital_status_quote =
    kquote + kcurrent_marital_status + kquote + ": ";

  public static final String knet_income = "net_income";

  public static final String knet_income_quote =
    kquote + knet_income + kquote + ": ";

  // Constants for BDMUAApplicationCardAPI
  public static final String knot_eligible = "Not Eligible";

  // BEGIN-Task- Constant for financialInstitutionNumber and transitNumber
  public static final String kFINANCIAL_INSTITUTION_NUMBER =
    "financialInstitutionNumber";

  public static final String kBRANCH_TRANSIT_NUMBER = "branchTransitNumber";
  // END-Task- Constant for financialInstitutionNumber and transitNumber

  public static final int kUSERNAME_LENGTH_LIMIT = 64;
  // BEGIN - CK - TASK 21373 - Tax Slips

  // Alternate Name evidences

  // first name
  public static final String kEvidenceAttrNameFirstName = "firstName";

  // last name
  public static final String kEvidenceAttrNameLastName = "lastName";

  // initials name
  public static final String kEvidenceAttrNameMiddleName = "middleName";

  // name type
  public static final String kEvidenceAttrNameType = "nameType";

  // Addresses

  // address type
  public static final String kEvidenceAttrAddressType = "addressType";

  // address ID
  public static final String kEvidenceAttrAddressID = "address";

  // Identifications

  // altIDType
  public static final String kEvidenceAttrIdentificationType = "altIDType";

  // alternateID
  public static final String kEvidenceAttrIdentificationAlternateID =
    "alternateID";

  // native status
  public static final String kEvidenceAttrTaxNativeStatus = "nativeStatus";

  public static final String kSaveAndIssueAction = "SAVEANDISSUE";

  public static final String kSaveAction = "SAVE";
  // END - CK - TASK 21373 - Tax Slips

  // Begin Task-20880: DoJ outbound
  public static final String kObligationITCCode = "0";

  public static final String kObligationExceptionCode = "00";

  public static final String kObligationOCONRegionalCode = "0110";

  public static final String kObligationCanadianProvinceCode = "AB";

  public static final String kObligationVendorCode = "1000010";

  public static final String kEventSource = "CURAM";

  public static final String kProgramId = "CPP";

  public static final String kEventDestination = "DOJ";

  public static final String kEventType = "Garnishment";

  public static final String kStatus = "sent";

  public static final String kDocumentRecipient = "CPP_";

  public static final String kZeroDate = "00010101";

  public static final int kfileControID = 1550;

  public static final String kdocumentFileWeekCode = "0000";

  public static final String kdocumentFileDayCode = "2";

  public static final String kdocumentSource = "CCP_";

  public static final String kdocumentFileEnvironmentType = "T";

  // End Task-20880: DoJ outbound

  // 21671 - Behavior and feedback to user when WS Address is down
  public static final int kHTTP_STATUS_TIME_OUT_CODE_504 = 504;

  // Begin Task 24142 CRA Sequence Number
  public static final String kCRASeqType = "CRA";

  // BEGIN - CK - TASK 23927 - Payment Destination
  public static final String kSelectAllCode = "ALL";

  public static final Date kMaxDate = Date.fromISO8601("99991231");
  // END - CK - TASK 23927 - Payment Destination

  // BEGIN - CK - TASK 24443 - DEPENDENT VALIDATION
  public static final String kDEPENDENT_MAX = "DependentMax";

  public static final String kDEPENDENT_MIN = "DependentMin";

  public static final int kDEPENDENT_MAX_VALUE = 20;

  public static final int kDEPENDENT_MIN_VALUE = 0;

  // END - CK - TASK 24443 - DEPENDENT VALIDATION
  public static final String kAttestationComments =
    "Attestation has been captured in the attestation evidence on the application case";

  public static final String INVOKED_FROM_EDIT = "InvokedFromEdit";

  // BEGIN - Task 13972 - Cancel Payment Batch
  public static final String kREQUISITION_TYPE_2 = "2";

  public static final String kREQUISITION_TYPE_4 = "4";
  // END - Task 13972 - Cancel Payment Batch

  // Begin, task- 26458 : Batch Error framework

  public static final String kRecordIdentifier = "RecordIdentifier";

  public static final String kEntityIdentifier = "EntityIdentifier";

  public static final String kRecordID = "RecordID";

  public static final String kInstanceID = "InstanceID";

  public static final String kProcessingStatus = "ProcessingStatus";

  public static final String kErrorMessage = "ErrorMessage";

  // End, task- 26458

  // BEGIN task-28696 : adding square bracket constants

  public static final String gkSquareOpeningBracket = "[";

  public static final char gkSquareOpeningBracketChar = '[';

  public static final String gkSquareClosingBracket = "]";

  public static final char gkSquareClosingBracketChar = ']';

  public static final String gkCaseReference = "caseReference";

  public static final String gkCaseReferenceDelimiter =
    "caseReferenceDelimiter";

  public static final String gkHypen = "-";

  // END task-28696

  public static final int kHTTP_STATUS_TIME_OUT_CODE_400 = 400;

  public static final String IS_MERGE_PROSPECT = "isMergeProspect";

  // BEGIN 67898 DEV: Implement Incarceration Evidence

  public static final String ALPHA_NUMERIC_PATTERN = "^[a-zA-Z0-9]+$";

  // END 67898 DEV: Implement Incarceration Evidence

  public static final String PREFERRED_EMAIL_ADDRESS = "preferredEmail";

  public static final String PREFERRED_PHONE_NUMBER = "preferredPhoneNumber";

  public static final String LANGUAGE_COMMUNICATION = "languageCommunication";

  public static final String DATA_NOT_PROVIDED = "Data not provided";

  public static final String REF_DATE_FOR_LATE_REQUEST =
    "referenceDateForLastRequest";

  public static final String kresIntegrityReviewFlagType =
    "resIntegrityReview";

  public static final String kresInvestigationReferralFlagType =
    "resInvestigationReferral";

  public static final int kSINMisMatchDeadlineDays = 15;

  public static final int kIntegrityReviewDeadlineDays = 15;

  public static final int kInvestigationReferralDeadlineDays = 30;

  /* BEGIN - financial coding processing types */
  public static final String kFinCodePaymentReceived = "PRV";

  public static final String kFinCodePayment = "PMT";

  public static final String kFinCodeLiability = "LBY";

  public static final String kFinCodeLiabilityReversal = "LBY_REV";

  public static final String kFinCodeLiabilityDeduction = "LBY_DED";

  public static final String kFinCodePaymentReversal = "PMT_REV";

  public static final String kFinCodePaymentReceivedAlloc = "PRV_ALOC";

  public static final String kApplicationPDF = "application/pdf";

  /* END - financial coding processing types */

  /* BEGIN - VTW evidence attributes */
  public static final String kEvidenceAttrVTWStartDate = "startDate";
  /* END - VTW evidence attributes */

  public static final String kCurrentDateStr = "CURRENT_DATE";

  public static final long kReturnCorrespondenceSkillTypeID = 80000l;

  public static final long kMisdirectCorrespondenceSkillTypeID = 80001l;

  public static final long kVoidCorrespondenceSkillTypeID = 80001l;

  // BEGIN TASK 74615 DEV: Implement Manually Reported Income

  public static final String NUMERIC_PATTERN = "^[0-9]{4}+$";

  // END TASK 74615 DEV: Implement Manually Reported Income

  public static final String kWorkItemID = "workitemid";

  public static final String kWorkItemStatus = "status";

  public static final String kCreateCorrespondenceWizardProperties =
    "BDMParticipantCreateCorrespondenceWizard.properties";

  public static final String kEdit = "edit";

  public static final String kReview = "review";

  public static final String kDeliveryOptionLocalPrint = "LocalPrint";

  public static final String kDeliveryOptionCentralPrint = "CentralPrint";

  public static final String kCCTStatusPendingDelivery = "PENDING_DELIVERY";

  public static final String kCCTStatusActiveComplete = "ACTIVE_COMPLETE";

  public static final String kCCTStatusActiveInComplete = "ACTIVE_INCOMPLETE";

  public static final String kCCTStatusFailedDelivery = "FAILED_DELIVERY";

  public static final String kCCTStatusFinished = "FINISHED";

  public static final String kCCTStatusCancelled = "CANCELLED";

  public static final String kCanada = "Canada";

  public static final String kSSACountryCreated = "SSA Country created by";

  public static final String kSSACountryCreated_fr = "Pays SSA créé par";

  public static final String kSSACountryModified = "SSA Country modified by";

  public static final String kSSACountryModified_fr = "Pays ASS modifié par";

  public static final String kOn = "on";

  public static final String kOn_fr = "le";

  public static final String kBDMAdmin = "BDMADMIN";

  public static final String kIOAdmin = "IOADMIN";

  public static final String kIOProcessing = "IOProcessing";

  public static final String kIOBenefitOfficer = "IOBenefitOfficer";

  public static final String kIOSupervisor = "IOSupervisor";

  public static final String kIOReadReview = "IOReadReview";

  public static final String kIOClientContact = "IOCLIENTCONTACT";

  public static final String kView = "View";

  public static final String kDetails = "Details";

  public static final String kAgreement = "Agreement";

  public static final String kBDMFA = "BDMFA";

  public static final String gkCommaSpace = ", ";

  public static final String gkBESSIndYes = "Yes";

  public static final String gkBESSIndNo = "No";

  public static final char kNewLine = '\n';

  public static final char bdmKComma = ',';

  public static final String kHiphen = " - ";

  public static final String kSaveAndClose = "saveandclose";

  public static final String kNext = "next";

  public static final String kSave = "save";

  public static final String kBack = "back";

  public static final String kStep1Next = "step1next";

  public static final String kStep2Next = "step2next";

  public static final String kStep1SaveAndClose = "step1saveandclose";

  public static final String kStep2SaveAndClose = "step2saveandclose";

  public static final String kStep3Back = "step3back";

  public static final String kStep2Back = "step2back";

  public static final String gkYes = "Yes";

  public static final String gkNo = "No";

  public static final String kBDMFLKEYSET = "BDMFL";

  public static final String kBDMDEFKEYSET = "BDMDEF";

  public static final String kCCTDataMapName = "PensionsDataMap";

  public static final String kClientGenderMale = "M";

  public static final String kClientGenderFemale = "W";

  public static final String kClientGenderX = "X";

  public static final String kClientGenderInvalid = "I";

  public static final String kClientGenderUnknown = "U";

  public static final String kClientPreferredLanguageEnglish = "en_ca";

  public static final String kClientPreferredLanguageFrench = "fr_ca";

  public static final String kGenerateCorrespondenceXMLVaultID = "VaultId";

  public static final String kGenerateCorrespondenceTrackingNumber =
    "Tracking Number";

  public static final String kGenerateCorrespondenceXMLPatronato =
    "Patronato";

  public static final String kGenerateCorrespondenceXMLClient = "Client";

  public static final String kGenerateCorrespondenceXMLIndividual =
    "Individual";

  public static final String kGenerateCorrespondenceXMLOrganization =
    "Organization";

  public static final String kGenerateCorrespondenceXMLTo = "To";

  public static final String kGenerateCorrespondenceXMLCC = "CC";

  public static final String kBdmTaskCreateDetails = "TaskCreateDetails";

  public static final int kBdm48Hours = 48;

  public static final int kBdm24Hours = 24;

  public static final String kCreateCaseCorrespondenceWizardProperties =
    "BDMParticipantCreateCorrespondenceCaseWizard.properties";

  public final static String kdeleteCorrespondencePageIdentifier =
    "BDMParticipant_deleteCorrespondencePage.do";

  // START: Task 91325: DEV: Manage Privacy Request Implementation
  public static final String gkLocaleFR = "FR";

  public static final String kReceived = "Received";

  public static final String kRequestFor = "Request for";

  public static final String kBDMPersonPrivacyRequestNotificationTask =
    "BDMPersonPrivacyRequestNotificationTask";

  public static final String kBDMPersonCommTyInterimAppReqTask =
    "BDMPersonCommTyInterimAppReqTask";

  public static final String kBDMIntegratedCasePrivacyRequestNotificationTask =
    "BDMIntegratedCasePrivacyRequestNotificationTask";

  public static final String kBDMIntegratedCaseCommTypeInterimRequestNotificationTask =
    "BDMIntegratedCaseCommTypeInterimRequestNotificationTask";

  public static final String kBDMFBApplicationAttachmentNotificationTask =
    "BDMFBApplicationAttachmentNotificationTask";

  public static final String kBDMForeignContributionEvidenceNotificationTask =
    "BDMForeignContributionEvidenceNotificationTask";

  public static final String kBDMForeignResidenceEvidenceNotificationTask =
    "BDMForeignResidenceEvidenceNotificationTask";

  public static final String gkBIL = "BIL";

  public static final String gkRequestForPerson = "Request for Person ";

  public static final String gkEnquiry = "Enquiry";

  public static final String gkLocaleUpperEN = "EN";

  public static final String gkLocaleUpperFR = "FR";

  public static final String gkLocaleLowerEN = "en";

  public static final String gkLocaleLowerFR = "fr";

  public static final String kBDMFECRecordCommunicationNotificationTask =
    "BDMFECRecordCommunicationNotificationTask";

  public static final String kBDMPersonRecordCommunicationNotificationTask =
    "BDMPersonRecordCommunicationNotificationTask";
  // END: Task 91325: DEV: Manage Privacy Request Implementation

  public static final String kBDMIORegisterPersonForPDCWizardProperties =
    "BDMIORegisterPersonForFECWizard.properties";

  public static final String kBDMIOAddProspectPersonForPDCWizardProperties =
    "BDMIOAddProspectPersonForPDCWizard.properties";

  public static final String kBDMUNPARSE = "BDMUNPARSE";

  public static final String kBDMRECEIVEDFROM_ATTR = "bdmReceivedFrom";

  public static final String kBDMRECEIVEDFROMCOUNTRY_ATTR =
    "bdmReceivedFromCountry";

  public static final String kBDMMODEOFRECEIPT_ATTR = "bdmModeOfReceipt";

  public static final String kMarkUpSingleQuote = "&#39;";

  public static final String kOneString = "1";

  // START: Task 94070: DEV: Person Attachment Task Implementation
  public static final String kBDMPersonContactAttachmentUploadTask =
    "BDMPersonContactAttachmentUploadTask";

  // END: Task 94070: DEV: Person Attachment Task Implementation
  public static final String kBDMEvidenceComments = "comments";

  public static final String kBDMAddEvidenceComments = "Merge Performed  ";

  public static final String kBDMvidencesForMerge =
    CASEEVIDENCE.BDM_MARITAL_STATUS + "," + CASEEVIDENCE.NAMES + ","
      + CASEEVIDENCE.BDMADDRESS + "," + CASEEVIDENCE.IDENTIFICATIONS;

  public static final String kBDMDynEvdAttrNameThirdPartyCaseParticipantRole =
    "thirdPartyCaseParticipantRole";

  public static final String kBDMDynEvdAttrNameRole = "role";

  public static final String kBDMDynEvdAttrNameRoleType = "roleType";

  public static final String kBDMDynEvdAttrNameFrom = "from";

  public static final String kBDMDynEvdAttrNameTo = "to";

  public static final int kCorrespondenceNameLengthMax = 40;

  public static final String kBDMvidencesToUpdate =
    CASEEVIDENCE.BDM_MARITAL_STATUS + "," + CASEEVIDENCE.BDMADDRESS + ","
      + CASEEVIDENCE.IDENTIFICATIONS + ","
      + CASEEVIDENCE.BIRTH_AND_DEATH_DETAILS + ","
      + CASEEVIDENCE.PHONE_NUMBERS;

  // Fix BUG-98040 Incorrect error message and N/A option on screen 2 and 3 when
  // adding an Organization as Third Party
  public static final String kNotApplicable = "N/A";

  public static final String kIndividualWithinOrg = "individualWithinOrg";

  public static final double kDummyDeductionRate = 0.01;

  // WORKFLOW NAMES
  // 101950 Updated: 247.3-202 Trigger task to update residency period
  public static final String BDMRESIDENCEPERIODUPDATEWORKFLOW =
    "BDMResidencePeriodUpdateForCountryChangeTask";

  public static final String gkResidentialAddress = "ResidentialAddress";

  public static final String gkMailingAddress = "MailingAddress";

  public static final String gkAddressType = "addressType";

  public static final int gkPhoneAreaLength3 = 3;

  public static final int gkPhoneAreaLength7 = 7;

  public static final String caseParticipantRoleAttr = "caseParticipantRole";

  public static final String participantRoleIDAttr = "participantRoleID";

  public static final String participantIDAttr = "participantID";

  public static final String thirdPartyCaseParticipantRoleAttr =
    "thirdPartyCaseParticipantRole";

  public static final String individualWithinOrgAttr = "individualWithinOrg";

  public static final String programAttr = "program";

  public static final String poaClassificationAttr = "poaClassification";

  public static final String poaTypeAttr = "poaType";

  public static final String relationshipToClientAttr =
    "relationshipToClient";

  public static final String languagePreferenceAttr = "languagePreference";

  public static final String commentsAttr = "comments";

  public static final String titleAttr = "title";

  public static final String ERR_ADDRESS_FILTER =
    " Error occured in Filtering address ";

  public static final String ERR_PHONE = "Error Occured in phone";

  public static final String ERR_EMAIL = "Error Occured in Email";

  public static final String ERR_PREFIX_LOCALE_DESC =
    "Couldn't find the User Locale description for codetable: ";

  public static final String defaulDesc =
    ". Providing default description, if found";

  public static final String provideEmptyStringDesc =
    ". Providing empty String";

  public static final String kHashSymbol = "#";

  public static final String kDoubleSpacesString = "  ";

  public static final long kZeroLongValue = 0l;

  public static final String kBDMMergeMaritalStatusEvdPage =
    "<page id=\"BDMParticipant_mergeMaritalStatusEvidence\" description=\"Marital Status\" initial=\"false\" disableback=\"false\" disableforward=\"false\" submitonnext=\"true\"/>";

  public static final String kBDMMergeMaritalStatusPage =
    "<page id=\"BDMParticipant_mergeMaritalStatus\" description=\"Marital Status\" initial=\"false\" disableback=\"false\" disableforward=\"false\" submitonnext=\"true\"/>";

  // START Bug 115707: Taxonomy - Incorrect french labels in Mark Duplicate
  // modal
  public static final String kBDMMergeMaritalStatusEvdPage1 =
    "<page id=\"BDMParticipant_mergeMaritalStatusEvidence\" description=\"";

  public static final String kBDMMergeMaritalStatusEvdPage2 =
    "\" initial=\"false\" disableback=\"false\" disableforward=\"false\" submitonnext=\"true\"/>";

  public static final String kBDMMergeMaritalStatusPage1 =
    "<page id=\"BDMParticipant_mergeMaritalStatus\" description=\"";

  public static final String kBDMMergeMaritalStatusPage2 =
    "\" initial=\"false\" disableback=\"false\" disableforward=\"false\" submitonnext=\"true\"/>";

  public static final String PAGE_DESC_MARTIAL_STATUS_FR = "État matrimonial";

  public static final String PAGE_DESC_MARTIAL_STATUS_EN = "Marital Status";

  // END Bug 115707: Taxonomy - Incorrect french labels in Mark Duplicate modal

  public static final String kSummary = "<summary";

  public static final String kCaseParticipantRoleID = "caseParticipantRoleID";

  public static final int kUnverifiedContactDefaultEndDate = 180;

  public static final int kminsPerHour = 60;

  public static final String kSingleQuote = "'";

  public static final String kBDMEvidencefromDate = "fromDate";

  public static final String kBDMEvidencetoDate = "toDate";

  public static final String kCorrespondenceID = "Correspondence ID";

  public static final String kUPDATECORRESPONDENCEDP =
    "UPDATEJOBCORRESPONDENCEDP";

  public static final String kJobID = "JobID";

  public static final String gkEnquiry_FR = "Demande de renseignements";

  public static final String gkMPEnquiry_EN = "MP Enquiry";

  public static final String gkMinistrialEnquiry_EN = "Ministrial Enquiry";

  public static final String gkMPEnquiry_FR =
    "demande de renseignements d’un député";

  public static final String gkMinistrialEnquiry_FR = "requ�te minist�rielle";

  public static final String kForwardSlash = "/";

  public static final int gkTwentyFour = 24;

  public static final String kNotApplicable_fr = "S.O.";

  // START: BUG 118183

  public static final String kNotificationSubject_en =
    "Additional documents received for ";

  public static final String kNotificationSubject_fr =
    "Documents supplémentaires reçus pour";

  // END: BUG 118183

  public static final String kConversionIdentifier =
    "ITRDS/Bulk - Data Migration";

}
