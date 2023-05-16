package curam.ca.gc.bdmoas.application.impl;

import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.ca.gc.bdmoas.entity.fact.BDMOASPersonFactory;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSearchKey;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.cefwidgets.sl.impl.CuramConst;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.impl.ADDRESSELEMENTTYPEEntry;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;

/**
 *
 * This class is responsible for executing all of the different partial match
 * queries and running the exact match query.
 *
 * @author abid.a.khan
 *
 */
@SuppressWarnings("restriction")
public class BDMOASPersonMatch {

  private static final String SIN = "sin";

  private static final String SIN_NAME_DOB =
    "sin, first name, last name, dob";

  private static final String DOB_NAME_POSTAL_COUNTRY =
    "DOB, first name, last name, postal/zip, country";

  private static final String MATCHES_FOUND_WITH_CRITERIA =
    " match(es) found with the following criteria: ";

  private static final String WHITE_SPACE_REGEXP = "\\s";

  private static final String BDM_OAS_PERSON_MATCH_LOG_FORMATTER = "{}{}{}{}";

  /**
   * Exact Match
   * Executes search based on the parameters passed in. <br>
   * This is Exact Match criteria for BDM OAS.
   * <br>
   * <br>
   * Match criteria:
   * <OL>
   * <LI>First Name</LI>
   * <LI>Last Name</LI>
   * <LI>Date of Birth</LI>
   * <LI>SIN</LI>
   * </OL>
   *
   * @param sin
   * @param firstName
   * @param lastName
   * @param dateOfBirth
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  public static BDMPersonSearchResultDetailsList
    searchBySINDOBFirstNameLastName(final String sin, final String firstName,
      final String lastName, final Date dateOfBirth)
      throws AppException, InformationalException {

    final BDMPersonSearchKey bdmOasPersonSINDOBNameKey =
      new BDMPersonSearchKey();
    bdmOasPersonSINDOBNameKey.dateOfBirth = dateOfBirth;
    bdmOasPersonSINDOBNameKey.firstName = firstName.toUpperCase();
    bdmOasPersonSINDOBNameKey.lastName = lastName.toUpperCase();
    bdmOasPersonSINDOBNameKey.sin = sin;

    final BDMPersonSearchResultDetailsList personList = BDMOASPersonFactory
      .newInstance().searchBySINDOBName(bdmOasPersonSINDOBNameKey);

    if (Trace.atLeast(Trace.kTraceOn)) {
      Trace.kTopLevelLogger.info(BDM_OAS_PERSON_MATCH_LOG_FORMATTER,
        BDMOASConstants.BDM_OAS_LOGS_PREFIX, personList.dtls.size(),
        MATCHES_FOUND_WITH_CRITERIA, SIN_NAME_DOB);
    }
    return personList;

  }

  /**
   * Partial Match #1 - SIN
   * This will determine if the person registered closely matches an existing
   * person in the system.
   * <br>
   * <br>
   * Match criteria:
   * <OL>
   * <LI>SIN</LI>
   * </OL>
   *
   * @param sin
   * @return List of partial match(es) if any found, otherwise returns an empty
   * list
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static BDMPersonSearchResultDetailsList searchPersonBySIN(
    final String sin) throws AppException, InformationalException {

    // return list
    BDMPersonSearchResultDetailsList personList =
      new BDMPersonSearchResultDetailsList();

    if (!StringUtil.isNullOrEmpty(sin)) {

      final BDMPersonSINKey bdmPersonSINKey = new BDMPersonSINKey();
      bdmPersonSINKey.sin = sin;

      // querying the database
      personList =
        BDMPersonFactory.newInstance().searchBySIN(bdmPersonSINKey);
    }

    if (Trace.atLeast(Trace.kTraceOn)) {
      Trace.kTopLevelLogger.info(BDM_OAS_PERSON_MATCH_LOG_FORMATTER,
        BDMOASConstants.BDM_OAS_LOGS_PREFIX, personList.dtls.size(),
        MATCHES_FOUND_WITH_CRITERIA, SIN);
    }
    return personList;
  }

  /**
   * Partial Match #2 - First Name, Last name, Date of Birth, Postal/Zip code,
   * Country code. <br>
   * This meets partial match criteria for BDMOAS iff SIN is not matched
   * or not provided.
   * <br>
   * <br>
   * Match criteria:
   * <OL>
   * <LI>First Name</LI>
   * <LI>Last Name</LI>
   * <LI>Date of Birth</LI>
   * <LI>Address Element Type</LI>
   * <LI>Upper Address Element Value</LI>
   * <LI>Country Code</LI>
   * </OL>
   *
   * @param firstName
   * @param lastName
   * @param dateOfBirth
   * @param addressElementType
   * @param upperAddrElemValue
   * @param country
   * @return A matching details struct with person concernroleID and Name.
   * @throws AppException
   * @throws InformationalException
   */
  public static BDMPersonSearchResultDetailsList
    searchByDOBNamePostalZipCountry(final String firstName,
      final String lastName, final Date dateOfBirth,
      final ADDRESSELEMENTTYPEEntry addressElementType,
      final String upperAddrElemValue, final String country)
      throws AppException, InformationalException {

    final BDMOASPersonSearchKey bdmOASPersonDOBNameZipCountryKey =
      new BDMOASPersonSearchKey();

    bdmOASPersonDOBNameZipCountryKey.addressLayoutType =
      ADDRESSLAYOUTTYPE.BDMINTL;
    bdmOASPersonDOBNameZipCountryKey.addressTypeCode =
      CONCERNROLEADDRESSTYPE.PRIVATE;
    bdmOASPersonDOBNameZipCountryKey.firstName = firstName;
    bdmOASPersonDOBNameZipCountryKey.lastName = lastName;
    bdmOASPersonDOBNameZipCountryKey.countryCode = country;
    bdmOASPersonDOBNameZipCountryKey.dateOfBirth = dateOfBirth;
    bdmOASPersonDOBNameZipCountryKey.nameType = ALTERNATENAMETYPE.REGISTERED;
    bdmOASPersonDOBNameZipCountryKey.upperElementValue =
      upperAddrElemValue.replaceAll(WHITE_SPACE_REGEXP, CuramConst.gkEmpty);
    bdmOASPersonDOBNameZipCountryKey.elementType =
      addressElementType.getCode();

    // querying the database
    final BDMPersonSearchResultDetailsList personList =
      BDMOASPersonFactory.newInstance()
        .searchByDOBNamePostalZipCountry(bdmOASPersonDOBNameZipCountryKey);

    if (Trace.atLeast(Trace.kTraceOn)) {
      Trace.kTopLevelLogger.info(BDM_OAS_PERSON_MATCH_LOG_FORMATTER,
        BDMOASConstants.BDM_OAS_LOGS_PREFIX, personList.dtls.size(),
        MATCHES_FOUND_WITH_CRITERIA, DOB_NAME_POSTAL_COUNTRY);
    }

    return personList;
  }

}
