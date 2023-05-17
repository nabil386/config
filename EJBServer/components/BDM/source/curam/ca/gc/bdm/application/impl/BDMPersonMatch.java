package curam.ca.gc.bdm.application.impl;

import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonDOBLastNameAtBirthKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINDOBKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINDOBLastNameAtBirthKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINLastNameAtBirthKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.ca.gc.bdm.impl.BDMConstants;
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
 * @author NLynch
 *
 */
@SuppressWarnings("restriction")
public class BDMPersonMatch {

  /**
   * Exact Match
   * Method that executes a query based on the parameters passed in. <br>
   * This is the exact match criteria for BDM.
   * <br>
   * <br>
   * Match criteria:
   * <OL>
   * <LI>Last Name at Birth</LI>
   * <LI>Date of Birth</LI>
   * <LI>SIN</LI>
   * </OL>
   *
   * @param sin
   * Holds person SIN
   * @param lastNameAtBirth
   * Holds last name at birth
   * @param dateOfBirth
   * Holds date of birth
   *
   * @return List of exact match(es)
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static BDMPersonSearchResultDetailsList
    searchBySINDOBLastNameAtBirth(final String sin,
      final String lastNameAtBirth, final Date dateOfBirth)
      throws AppException, InformationalException {

    // return list
    BDMPersonSearchResultDetailsList personList =
      new BDMPersonSearchResultDetailsList();

    if (!StringUtil.isNullOrEmpty(sin)) {

      final BDMPersonSINDOBLastNameAtBirthKey bdmPersonSINDOBLastNameAtBirthKey =
        new BDMPersonSINDOBLastNameAtBirthKey();
      bdmPersonSINDOBLastNameAtBirthKey.lastNameAtBirth = lastNameAtBirth;
      bdmPersonSINDOBLastNameAtBirthKey.dateOfBirth = dateOfBirth;
      bdmPersonSINDOBLastNameAtBirthKey.sin = sin;

      // querying the database
      personList = BDMPersonFactory.newInstance()
        .searchBySINDOBLastNameAtBirth(bdmPersonSINDOBLastNameAtBirthKey);
    }

    Trace.kTopLevelLogger.debug(BDMConstants.BDM_LOGS_PREFIX
      + personList.dtls.size()
      + " match(es) found with the following criteria: sin, last name at birth, dob");

    return personList;
  }

  /**
   * Partial Match #1 - SIN, DOB
   * Method that executes a query based on the parameters passed in. <br>
   * This is the exact match criteria for BDM.
   * <br>
   * <br>
   * Match criteria:
   * <OL>
   * <LI>Date of Birth</LI>
   * <LI>SIN</LI>
   * </OL>
   *
   * @param sin
   * @param dateOfBirth
   *
   * @return List of exact match(es)
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static BDMPersonSearchResultDetailsList
    searchBySINDOB(final String sin, final Date dateOfBirth)
      throws AppException, InformationalException {

    // return list
    BDMPersonSearchResultDetailsList personList =
      new BDMPersonSearchResultDetailsList();

    if (!StringUtil.isNullOrEmpty(sin)) {

      final BDMPersonSINDOBKey bdmPersonSINDOBkey = new BDMPersonSINDOBKey();
      bdmPersonSINDOBkey.dateOfBirth = dateOfBirth;
      bdmPersonSINDOBkey.sin = sin;

      // querying the database
      personList =
        BDMPersonFactory.newInstance().searchBySINDOB(bdmPersonSINDOBkey);
    }

    Trace.kTopLevelLogger
      .debug(BDMConstants.BDM_LOGS_PREFIX + personList.dtls.size()
        + " match(es) found with the following criteria: sin, dob");

    return personList;
  }

  /**
   * Partial Match #2 - SIN, Last name at Birth
   * Method that executes a query based on the parameters passed in. <br>
   * This is the exact match criteria for BDM.
   * <br>
   * <br>
   * Match criteria:
   * <OL>
   * <LI>Last Name at Birth</LI>
   * <LI>SIN</LI>
   * </OL>
   *
   * @param sin
   * @param lastNameAtBirth
   *
   * @return List of exact match(es)
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static BDMPersonSearchResultDetailsList
    searchBySINLastNameAtBirth(final String sin, final String lastNameAtBirth)
      throws AppException, InformationalException {

    // return list
    BDMPersonSearchResultDetailsList personList =
      new BDMPersonSearchResultDetailsList();

    if (!StringUtil.isNullOrEmpty(sin)) {

      final BDMPersonSINLastNameAtBirthKey bdmPersonSINLastNameAtBirthKey =
        new BDMPersonSINLastNameAtBirthKey();
      bdmPersonSINLastNameAtBirthKey.lastNameAtBirth = lastNameAtBirth;

      bdmPersonSINLastNameAtBirthKey.sin = sin;

      // querying the database
      personList = BDMPersonFactory.newInstance()
        .searchBySINLastNameAtBirth(bdmPersonSINLastNameAtBirthKey);
    }

    Trace.kTopLevelLogger.debug(BDMConstants.BDM_LOGS_PREFIX
      + personList.dtls.size()
      + " match(es) found with the following criteria: sin, last name at birth");

    return personList;
  }

  /**
   * Partial Match #3 - SIN
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

    Trace.kTopLevelLogger
      .debug(BDMConstants.BDM_LOGS_PREFIX + personList.dtls.size()
        + " match(es) found with the following criteria: sin");

    return personList;
  }

  /**
   * Partial Match #4 - SIN, Last name at Birth
   * Method that executes a query based on the parameters passed in. <br>
   * This is the exact match criteria for BDM.
   * <br>
   * <br>
   * Match criteria:
   * <OL>
   * <LI>Last Name at Birth</LI>
   * <LI>Date of Birth</LI>
   * </OL>
   *
   * @param lastNameAtBirth
   * @param dateOfBirth
   *
   * @return List of exact match(es)
   *
   * @throws AppException
   * @throws InformationalException
   */
  public static BDMPersonSearchResultDetailsList searchByDOBLastNameAtBirth(
    final String lastNameAtBirth, final Date dateOfBirth)
    throws AppException, InformationalException {

    // return list
    BDMPersonSearchResultDetailsList personList =
      new BDMPersonSearchResultDetailsList();

    final BDMPersonDOBLastNameAtBirthKey bdmPersonDOBLastNameAtBirthKey =
      new BDMPersonDOBLastNameAtBirthKey();
    bdmPersonDOBLastNameAtBirthKey.lastNameAtBirth = lastNameAtBirth;
    bdmPersonDOBLastNameAtBirthKey.dateOfBirth = dateOfBirth;

    // querying the database
    personList = BDMPersonFactory.newInstance()
      .searchByDOBLastNameAtBirth(bdmPersonDOBLastNameAtBirthKey);

    Trace.kTopLevelLogger.debug(BDMConstants.BDM_LOGS_PREFIX
      + personList.dtls.size()
      + " match(es) found with the following criteria: last name at birth, dob");

    return personList;
  }

}
