package curam.ca.gc.bdm.application.impl;

import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.core.sl.struct.ProspectPersonRegistrationDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.workspaceservices.mappingbeans.impl.ProspectPersonMappingBean;

/**
 *
 * This class will be used to determine whether a person registering is an exact
 * match in the system,
 * a partial match in the system, or neither
 *
 * Added additional partial matches to isPartialMatchFound() based on PR
 * requirements
 *
 */
public class BDMPersonMatchImpl {

  public BDMPersonMatchImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Method used to determine if the person registration data entered, matches
   * an existing person in the system.
   * We will either get an exact match, a partial match or neither.
   *
   * @param PersonRegistrationDetails
   * registrationDtls - The person registration details that we need to check
   * against the database. This includes all OOTB search criteria, as well as
   * middle
   * name, and second last name
   *
   * @return A type of match or an empty string if no match exists. If a match
   * exists, the match type will be one of the
   * following:
   * <OL>
   * <LI>Exact - The person entered is an exact match in the system.</LI>
   * <LI>Partial - The person entered matches most details for someone in the
   * system.</LI>
   * </OL>
   *
   * @throws AppException
   * Generic Exception Signature.
   * @throws InformationalException
   * Generic Exception Signature.
   */
  public String
    determinePersonMatchType(final PersonRegistrationDetails registrationDtls)
      throws AppException, InformationalException {

    String personMatchType = "";
    boolean isExactMatch = false;
    boolean isPartialMatch = false;

    // Creates a ProspectPersonMappingBean to pass into performExactPersonMatch
    final ProspectPersonMappingBean intakeClient =
      new ProspectPersonMappingBean();
    final ProspectPersonRegistrationDtls searchDtls =
      new ProspectPersonRegistrationDtls();
    searchDtls.socialSecurityNumber = registrationDtls.socialSecurityNumber;
    searchDtls.personBirthName = registrationDtls.birthName;
    searchDtls.dateOfBirth = registrationDtls.dateOfBirth;
    intakeClient.setProspectPersonDetails(searchDtls);

    // Perform exact match check
    final BDMPersonSearchResultDetailsList exactMatchResults =
      this.performExactMatch(intakeClient);

    if (exactMatchResults.dtls.size() >= 1) {
      isExactMatch = true;
    } else { // If no exact match is found, then check conditions for partial
             // match
      isPartialMatch = this.isPartialMatchFound(intakeClient);
    }

    // Sets return variable for which type of person match this is
    if (isExactMatch) {
      personMatchType = BDMPersonMatchConstants.EXACT_PERSON_MATCH;
    } else if (isPartialMatch) {
      personMatchType = BDMPersonMatchConstants.PARTIAL_PERSON_MATCH;
    }

    return personMatchType;
  }

  /**
   * This will determine if the person registered already matches an existing
   * person in the system. <br>
   * This method extracts data from the {@code ProspectPersonMappingBean} struct
   * coming in and then uses this extracted
   * data to call a helper method which executes a query to check for an exact
   * match.
   *
   * @param intakeClient
   * @return List of exact match(es)
   * @throws AppException
   * @throws InformationalException
   */
  public BDMPersonSearchResultDetailsList
    performExactMatch(final ProspectPersonMappingBean intakeClient)
      throws AppException, InformationalException {

    final ProspectPersonRegistrationDtls client =
      intakeClient.getProspectPersonDetails();

    final String sin = intakeClient.getPersonEntity()
      .getAttribute(BDMDatastoreConstants.SOCIAL_INSURANCE_NUMBER);
    final Date dob = client.dateOfBirth;
    final String lastNameAtBirth = intakeClient.getPersonEntity()
      .getAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH).toUpperCase();

    // Exact match on all categories
    final BDMPersonSearchResultDetailsList bdmPersonSearchResultDetailsList =
      BDMPersonMatch.searchBySINDOBLastNameAtBirth(sin, lastNameAtBirth, dob);
    if (bdmPersonSearchResultDetailsList.dtls.size() > 0) {
      return bdmPersonSearchResultDetailsList;
    }

    return bdmPersonSearchResultDetailsList;
  }

  /**
   * This will determine if the person registered already matches an existing
   * person in the system. <br>
   * This method extracts data from the {@code ProspectPersonMappingBean} struct
   * coming in and then uses this extracted
   * data to call a helper method which executes a query to check for an exact
   * match.
   *
   * @param intakeClient
   * @return List of exact match(es)
   * @throws AppException
   * @throws InformationalException
   */
  public BDMPersonSearchResultDetailsList
    performUnlinkedExactMatch(final ProspectPersonMappingBean intakeClient)
      throws AppException, InformationalException {

    final ProspectPersonRegistrationDtls client =
      intakeClient.getProspectPersonDetails();

    final String sin = intakeClient.getPersonEntity()
      .getAttribute(BDMDatastoreConstants.SOCIAL_INSURANCE_NUMBER);
    final Date dob = client.dateOfBirth;
    final String lastNameAtBirth = intakeClient.getPersonEntity()
      .getAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH).toUpperCase();

    // Exact match on all categories
    BDMPersonSearchResultDetailsList bdmPersonSearchResultDetailsList =
      BDMPersonMatch.searchBySINDOBLastNameAtBirth(sin, lastNameAtBirth, dob);
    if (bdmPersonSearchResultDetailsList.dtls.size() > 0) {
      return bdmPersonSearchResultDetailsList;
    }

    // Exact match on SIN and DOB
    bdmPersonSearchResultDetailsList =
      BDMPersonMatch.searchBySINDOB(sin, dob);
    if (bdmPersonSearchResultDetailsList.dtls.size() > 0) {
      return bdmPersonSearchResultDetailsList;
    }

    // Exact match on SIN, Last name at Birth
    bdmPersonSearchResultDetailsList =
      BDMPersonMatch.searchBySINLastNameAtBirth(sin, lastNameAtBirth);
    if (bdmPersonSearchResultDetailsList.dtls.size() > 0) {
      return bdmPersonSearchResultDetailsList;
    }

    return bdmPersonSearchResultDetailsList;
  }

  /**
   * This is the method which is going to handle the logic for determining if
   * somebody is a partial match.
   * This method will call helper methods which will execute various queries
   * related to partial match conditions.
   * If a partial match query returns some result, then we flag this person as a
   * partial match.
   *
   * @param prospectPersonMappingBean
   * Object that represents the details about the intake client.
   *
   * @return True if a partial match is found. False if a partial match is not
   * found.
   *
   * @throws AppException
   * @throws InformationalException
   */
  public boolean
    isPartialMatchFound(final ProspectPersonMappingBean intakeClient)
      throws AppException, InformationalException {

    final ProspectPersonRegistrationDtls client =
      intakeClient.getProspectPersonDetails();

    final String sin = client.socialSecurityNumber;
    final Date dob = client.dateOfBirth;
    // TASK ADO-14550 updated last name to read from DS
    final String lastNameAtBirth = intakeClient.getPersonEntity()
      .hasAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH)
        ? intakeClient.getPersonEntity().getAttribute("LastNameAtBirth")
          .toUpperCase()
        : client.personBirthName.toUpperCase();
    // TASK ADO-14550 updated last name to read from DS
    BDMPersonSearchResultDetailsList partialMatchList =
      new BDMPersonSearchResultDetailsList();

    // BEGIN - different partial match checks. Each search
    // method runs a different query depending on the data provided.

    // Partial Match #1 - SIN, DOB
    partialMatchList = BDMPersonMatch.searchBySINDOB(sin, dob);
    if (partialMatchList.dtls != null) {
      if (!partialMatchList.dtls.isEmpty()) {
        return true;
      }
    }

    // Partial Match #2 - SIN, Last name at Birth
    partialMatchList =
      BDMPersonMatch.searchBySINLastNameAtBirth(sin, lastNameAtBirth);
    if (partialMatchList.dtls != null) {
      if (!partialMatchList.dtls.isEmpty()) {
        return true;
      }
    }

    // Partial Match - SIN
    partialMatchList = BDMPersonMatch.searchPersonBySIN(sin);
    if (partialMatchList.dtls != null) {
      if (!partialMatchList.dtls.isEmpty()) {
        return true;
      }
    }

    // Partial Match - Last name at Birth, DOB
    partialMatchList =
      BDMPersonMatch.searchByDOBLastNameAtBirth(lastNameAtBirth, dob);
    if (partialMatchList.dtls != null) {
      if (!partialMatchList.dtls.isEmpty()) {
        return true;
      }
    }

    // no partial match found, so return false
    return false;
  }

  /**
   * This is the method which is going to handle the logic for determining if
   * somebody is a partial match.
   * This method will call helper methods which will execute various queries
   * related to partial match conditions.
   * If a partial match query returns some result, then we flag this person as a
   * partial match.
   *
   * @param prospectPersonMappingBean
   * Object that represents the details about the intake client.
   *
   * @return True if a partial match is found. False if a partial match is not
   * found.
   *
   * @throws AppException
   * @throws InformationalException
   */
  public boolean
    isRelaxedPartialMatchFound(final ProspectPersonMappingBean intakeClient)
      throws AppException, InformationalException {

    final ProspectPersonRegistrationDtls client =
      intakeClient.getProspectPersonDetails();

    final String sin = client.socialSecurityNumber;
    final Date dob = client.dateOfBirth;
    final String lastNameAtBirth = intakeClient.getPersonEntity()
      .hasAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH)
        ? intakeClient.getPersonEntity().getAttribute("LastNameAtBirth")
          .toUpperCase()
        : client.personBirthName.toUpperCase();
    BDMPersonSearchResultDetailsList partialMatchList =
      new BDMPersonSearchResultDetailsList();

    // BEGIN - different partial match checks. Each search
    // method runs a different query depending on the data provided.

    // Partial Match #3 - SIN
    partialMatchList = BDMPersonMatch.searchPersonBySIN(sin);
    if (partialMatchList.dtls != null) {
      if (!partialMatchList.dtls.isEmpty()) {
        return true;
      }
    }

    // Partial Match #4 - Last name at Birth, DOB
    partialMatchList =
      BDMPersonMatch.searchByDOBLastNameAtBirth(lastNameAtBirth, dob);
    if (partialMatchList.dtls != null) {
      if (!partialMatchList.dtls.isEmpty()) {
        return true;
      }
    }

    // no partial match found, so return false
    return false;
  }

}
