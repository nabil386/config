package curam.ca.gc.bdm.util.integrity.impl;

public class BDMSINIntegrityCheckConstants {

  public static final String EVIDENCE_ATTNAME_FIRSTNAME = "firstName";

  public static final String EVIDENCE_ATTNAME_MIDDLENAME = "middleName";

  public static final String EVIDENCE_ATTNAME_LASTNAME = "lastName";

  public static final String EVIDENCE_ATTNAME_MOTHERBIRTHLASTNAME =
    "mothersBirthLastName";

  public static final String EVIDENCE_ATTNAME_BIRTHLASTNAME = "birthLastName";

  public static final String EVIDENCE_ATTNAME_NAMETYPE = "nameType";

  public static final String EVIDENCE_ATTNAME_DATEOFBIRTH = "dateOfBirth";

  public static final String EVIDENCE_ATTNAME_ADDRESSTYPE = "addressType";

  public static final String EVIDENCE_ATTNAME_TODATE = "toDate";

  public static final int SIR_FLAG_FRAUD = 3;

  public static final int SIR_FLAG_FROZEN = 3;

  public static final int SIR_FLAG_DEATH = 1;

  public static final int SIR_FLAG_CANCELLED = 2;

  public static final int SIR_FLAG_VOID = 3;

  public static final int SIR_FLAG_PREVIOUS_INVESTIGATION = 4;

  public static final String SIR_FLAG_EXPIRED = "EXPIRED";

  public static final int SIR_FLAG_UNDER_REVIEW_FRAUD = 8;

  public static final int SIR_FLAG_DUPLICATE = 2;

  public static final int SIR_FLAG_DORMANT = 1;

  public static final int SIR_FLAG_DORMANT_REACTIVATION = 4;

  public static final int SIR_FLAG_DOCUMENT_RETURNED = 2;

  public static final int SIR_FLAG_CARD_RETURNED = 1;

  public static final int SIR_FLAG_CONFIRMATION_LETTER_RETURNED = 7;

  public static final String CHECK_RESULT_PASS = "PASS";

  public static final String CHECK_RESULT_REVIEW = "REVIEW";

  public static final String CHECK_RESULT_SINTOBEVERIFIED = "SINTOBEVERIFIED";

  public static final String CHECK_RESULT_ADDRESSTOBEVERIFIED =
    "ADDRESSTOBEVERIFIED";

  public static final String CHECK_RESULT_MISMATCH = "MISMATCH";

  public static final String CHECK_RESULT_UNAVAILABLE = "UNAVAILABLE";

  public static final String CHECK_RESULT_ERROR = "ERROR";

  public static final String CHECK_RESULT_NA = "NA";

  public static final String NAME_SPACE = " ";

  public static final String SUFFIX_DELIMITER = "\\|";

  public static final String SIN_TASKTYPE_INTEGRITY = "SINInegrityReview";

  public static final String SIN_TASKTYPE_INVESTIGATION = "SINInvestigation";

  public static final String SIN_TASKTYPE_MISMATCH = "SINMisMatch";
}
