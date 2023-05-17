package curam.ca.gc.bdmoas.application.impl;

import curam.ca.gc.bdm.application.impl.BDMApplicationEventsUtil;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMPersonMatchConstants;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.cefwidgets.sl.impl.CuramConst;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.impl.ADDRESSELEMENTTYPEEntry;
import curam.core.sl.struct.ProspectPersonRegistrationDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.workspaceservices.mappingbeans.impl.ProspectPersonMappingBean;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

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
public class BDMOASPersonMatchImpl {

  public BDMOASPersonMatchImpl() {

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

    // Creates a ProspectPersonMappingBean to pass into performExactPersonMatch
    final ProspectPersonMappingBean intakeClient =
      new ProspectPersonMappingBean();
    final ProspectPersonRegistrationDtls searchDtls =
      new ProspectPersonRegistrationDtls();
    searchDtls.socialSecurityNumber = registrationDtls.socialSecurityNumber;
    // Birth name is not a requirement for OAS
    searchDtls.dateOfBirth = registrationDtls.dateOfBirth;
    searchDtls.firstForename = registrationDtls.firstForename;
    searchDtls.surname = registrationDtls.surname;
    searchDtls.addressData = registrationDtls.addressData;
    intakeClient.setProspectPersonDetails(searchDtls);

    // Perform exact match check for OAS
    final BDMPersonSearchResultDetailsList exactMatchResults =
      this.performExactMatch(intakeClient);

    if (!exactMatchResults.dtls.isEmpty()) {
      // It means it is exact match.
      personMatchType = BDMPersonMatchConstants.EXACT_PERSON_MATCH;
    } else { // If no exact match is found, then check conditions for partial
             // match
      if (this.isPartialMatchFound(intakeClient)) {
        personMatchType = BDMPersonMatchConstants.PARTIAL_PERSON_MATCH;
      }
    }

    // Sets return variable for which type of person match this is
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

    final Entity personEntity = intakeClient.getPersonEntity();

    final ProspectPersonRegistrationDtls client =
      intakeClient.getProspectPersonDetails();

    final String sin = personEntity
      .getAttribute(BDMDatastoreConstants.SOCIAL_INSURANCE_NUMBER);
    final Date dob = client.dateOfBirth;

    final String firstName =
      personEntity.getAttribute(BDMDatastoreConstants.FIRST_NAME);
    final String lastName =
      personEntity.getAttribute(BDMDatastoreConstants.LAST_NAME);

    // Exact match on all categories
    return BDMOASPersonMatch.searchBySINDOBFirstNameLastName(sin, firstName,
      lastName, dob);

  }

  /**
   * Determines whether or not
   * a person passed in at intake is a partial match according to OAS matching
   * criteria.
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

    final String firstName = client.firstForename.toUpperCase();
    final String lastName = client.surname.toUpperCase();

    // Exact match on all categories
    final BDMPersonSearchResultDetailsList bdmOASPersonFullMatchSearchDetailsList =
      BDMOASPersonMatch.searchBySINDOBFirstNameLastName(sin, firstName,
        lastName, dob);

    if (!bdmOASPersonFullMatchSearchDetailsList.dtls.isEmpty()) {
      // Full match criteria satisfied.
      return false;
    }

    // Partial Match - SIN - if full match check has failed, but SIN matches,
    // OAS partial match condition is satisfied.
    BDMPersonSearchResultDetailsList partialMatchList =
      BDMOASPersonMatch.searchPersonBySIN(sin);

    if (partialMatchList.dtls != null && !partialMatchList.dtls.isEmpty()) {

      return true;
    }

    final Entity personEntity = intakeClient.getPersonEntity();
    final Map<String, String> addressDataMap =
      BDMUtil.getAddressDataMap(client.addressData);

    String zipCode = addressDataMap.get(ADDRESSELEMENTTYPE.ZIP);

    Entity intlResAddrEntity = null;
    final String country =
      personEntity.hasAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY)
        ? personEntity.getAttribute(BDMDatastoreConstants.RESIDENTIAL_COUNTRY)
        : addressDataMap.get(ADDRESSELEMENTTYPE.COUNTRY);

    if (addressDataMap.isEmpty()) {

      intlResAddrEntity = BDMApplicationEventsUtil.retrieveChildEntity(
        personEntity, BDMDatastoreConstants.INTL_RESIDENTIAL_ADDRESS);

      zipCode = intlResAddrEntity.hasAttribute(BDMDatastoreConstants.ZIP_CODE)
        ? intlResAddrEntity.getAttribute(BDMDatastoreConstants.ZIP_CODE)
        : CuramConst.gkEmpty;
    }

    final String postalCode = addressDataMap.get(ADDRESSELEMENTTYPE.POSTCODE);

    final ADDRESSELEMENTTYPEEntry addressElementType =
      StringUtils.isNotEmpty(postalCode) ? ADDRESSELEMENTTYPEEntry.POSTCODE
        : ADDRESSELEMENTTYPEEntry.ZIP;

    // Either postal or zip code must be non empty at this point.
    final String upperaddressElementValue =
      StringUtils.isNotEmpty(postalCode) ? postalCode : zipCode;

    // Partial Match
    partialMatchList = BDMOASPersonMatch.searchByDOBNamePostalZipCountry(
      firstName, lastName, dob, addressElementType,
      upperaddressElementValue.trim().toUpperCase(), country);

    return partialMatchList.dtls != null && !partialMatchList.dtls.isEmpty();

  }

}
