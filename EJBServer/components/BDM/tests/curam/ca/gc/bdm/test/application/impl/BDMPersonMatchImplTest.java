package curam.ca.gc.bdm.test.application.impl;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMPersonMatch;
import curam.ca.gc.bdm.application.impl.BDMPersonMatchImpl;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetails;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.sl.struct.ProspectPersonRegistrationDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.workspaceservices.mappingbeans.impl.ProspectPersonMappingBean;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
public class BDMPersonMatchImplTest extends CuramServerTestJUnit4 {

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  @Tested
  private BDMPersonMatchImpl bdmPersonMatchImpl;

  @Mocked
  BDMPersonMatch personMatch;

  @Mocked
  ProspectPersonMappingBean intakeClient;

  BDMPersonSearchResultDetailsList personSearchResultDtlsList;

  PersonRegistrationDetails personRegistrationDetails;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmPersonMatchImpl = new BDMPersonMatchImpl();

    personMatch = new BDMPersonMatch();

    personSearchResultDtlsList = new BDMPersonSearchResultDetailsList();
    personRegistrationDetails = new PersonRegistrationDetails();
    intakeClient = new ProspectPersonMappingBean();
  }

  private void setUpPersonSearchResultDetailsList(final String sin,
    final String lastNameAtBirth, final Date dateOfBirth)
    throws AppException, InformationalException {

    personRegistrationDetails = registerPerson.getPersonRegistrationDetails();

    personRegistrationDetails.socialSecurityNumber = sin;
    personRegistrationDetails.birthName = lastNameAtBirth;
    personRegistrationDetails.dateOfBirth = dateOfBirth;

    final BDMPersonSearchResultDetails personDtls =
      new BDMPersonSearchResultDetails();
    personDtls.assign(personRegistrationDetails);

    // return list
    personSearchResultDtlsList.dtls.add(personDtls);

    // Creates a ProspectPersonMappingBean to pass into performExactPersonMatch
    final ProspectPersonRegistrationDtls searchDtls =
      new ProspectPersonRegistrationDtls();
    searchDtls.socialSecurityNumber = sin;
    searchDtls.personBirthName = lastNameAtBirth;
    searchDtls.dateOfBirth = dateOfBirth;
    intakeClient.setProspectPersonDetails(searchDtls);
  }

  /**
   * Tests to determine if the person registration data entered, matches
   * an existing person in the system.
   * We will either get an exact match, a partial match or neither.
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testDeterminePersonMatchType()
    throws AppException, InformationalException {

    setUpPersonSearchResultDetailsList("111111111", "Test1",
      Date.getCurrentDate());

    new Expectations() {

      {

        intakeClient.getPersonEntity()
          .getAttribute(BDMDatastoreConstants.SOCIAL_INSURANCE_NUMBER);
        result = "111111111";
        intakeClient.getPersonEntity()
          .getAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH);
        result = "Test1";
        intakeClient.getProspectPersonDetails().dateOfBirth =
          Date.getCurrentDate();

        // BDMPersonMatch.searchBySINDOBLastNameAtBirth(anyString, anyString,
        // Date.getCurrentDate());
        // result = personSearchResultDtlsList;

      }
    };

    try {
      final String personMatchType = bdmPersonMatchImpl
        .determinePersonMatchType(personRegistrationDetails);
      assertEquals(personMatchType, "Exact");
    } catch (final Exception e) {
      assertFalse(false);
    }

  }

  @Test
  public void testPerformExactMatch()
    throws AppException, InformationalException {

    setUpPersonSearchResultDetailsList("111111111", "Test1",
      Date.getCurrentDate());
    setUpPersonSearchResultDetailsList("222222222", "Test2",
      Date.getCurrentDate());

    new Expectations() {

      {

        intakeClient.getPersonEntity()
          .getAttribute(BDMDatastoreConstants.SOCIAL_INSURANCE_NUMBER);
        result = "111111111";
        intakeClient.getPersonEntity()
          .getAttribute(BDMDatastoreConstants.LAST_NAME_AT_BIRTH);
        result = "Test1";
        intakeClient.getProspectPersonDetails().dateOfBirth =
          Date.getCurrentDate();

        BDMPersonMatch.searchBySINDOBLastNameAtBirth(anyString, anyString,
          Date.getCurrentDate());
        result = personSearchResultDtlsList;

      }
    };

    final BDMPersonSearchResultDetailsList searchResultDtlsList =
      bdmPersonMatchImpl.performExactMatch(intakeClient);

    assertEquals(2, searchResultDtlsList.dtls.size());
  }

  /**
   * Tests to determine Partial Match #1 - SIN, DOB
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testIsPartialMatchFoundSINDOB()
    throws AppException, InformationalException {

    setUpPersonSearchResultDetailsList("111111111", "Test1",
      Date.getCurrentDate());
    setUpPersonSearchResultDetailsList("222222222", "Test2",
      Date.getCurrentDate());

    new Expectations() {

      {

        intakeClient.getProspectPersonDetails().socialSecurityNumber =
          "111111111";
        intakeClient.getProspectPersonDetails().dateOfBirth =
          Date.getCurrentDate();

        // BDMPersonMatch.searchBySINDOB(anyString, Date.getCurrentDate());
        // result = personSearchResultDtlsList;

      }
    };

    try {
      final boolean isPartialMatchFound =
        bdmPersonMatchImpl.isPartialMatchFound(intakeClient);
      assertTrue(isPartialMatchFound);
    } catch (final Exception e) {
      assertFalse(false);
    }
  }

  /**
   * Tests to determine Partial Match #2 - SIN, Last name at Birth
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testIsPartialMatchFoundSINLastNameAtBirth()
    throws AppException, InformationalException {

    setUpPersonSearchResultDetailsList("111111111", "Test1",
      Date.getCurrentDate());
    setUpPersonSearchResultDetailsList("222222222", "Test2",
      Date.getCurrentDate());

    new Expectations() {

      {

        intakeClient.getProspectPersonDetails().socialSecurityNumber =
          "111111111";
        intakeClient.getProspectPersonDetails().personBirthName = "Test1";

        BDMPersonMatch.searchBySINLastNameAtBirth(anyString, anyString);
        result = personSearchResultDtlsList;

      }
    };

    final boolean isPartialMatchFound =
      bdmPersonMatchImpl.isPartialMatchFound(intakeClient);

    assertTrue(isPartialMatchFound);
  }

  /**
   * Tests to determine Partial Match #3 - SIN
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testIsPartialMatchFoundSIN()
    throws AppException, InformationalException {

    setUpPersonSearchResultDetailsList("111111111", "Test1",
      Date.getCurrentDate());
    setUpPersonSearchResultDetailsList("222222222", "Test2",
      Date.getCurrentDate());

    new Expectations() {

      {

        intakeClient.getProspectPersonDetails().socialSecurityNumber =
          "111111111";

        // BDMPersonMatch.searchPersonBySIN(anyString);
        // result = personSearchResultDtlsList;

      }
    };

    try {
      final boolean isPartialMatchFound =
        bdmPersonMatchImpl.isPartialMatchFound(intakeClient);
      assertTrue(isPartialMatchFound);
    } catch (final Exception e) {
      assertFalse(false);
    }
  }

  /**
   * Tests to determine Partial Match #4 - Last name at Birth, DOB
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testIsPartialMatchFoundDOBLastNameAtBirth()
    throws AppException, InformationalException {

    setUpPersonSearchResultDetailsList("111111111", "Test1",
      Date.getCurrentDate());
    setUpPersonSearchResultDetailsList("222222222", "Test2",
      Date.getCurrentDate());

    new Expectations() {

      {

        intakeClient.getProspectPersonDetails().personBirthName = "Test1";
        intakeClient.getProspectPersonDetails().dateOfBirth =
          Date.getCurrentDate();

        BDMPersonMatch.searchByDOBLastNameAtBirth(anyString,
          Date.getCurrentDate());
        result = personSearchResultDtlsList;

      }
    };

    final boolean isPartialMatchFound =
      bdmPersonMatchImpl.isPartialMatchFound(intakeClient);

    assertTrue(isPartialMatchFound);
  }

  /**
   * Tests to determine No Match is found
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testIsPartialMatchFoundFalse()
    throws AppException, InformationalException {

    setUpPersonSearchResultDetailsList("111111111", "Test1",
      Date.getCurrentDate());
    setUpPersonSearchResultDetailsList("222222222", "Test2",
      Date.getCurrentDate());

    new Expectations() {

      {

        intakeClient.getProspectPersonDetails().personBirthName = "NoMatch";
        intakeClient.getProspectPersonDetails().socialSecurityNumber =
          "000000000";

      }
    };

    final boolean isPartialMatchFound =
      bdmPersonMatchImpl.isPartialMatchFound(intakeClient);

    assertFalse(isPartialMatchFound);
  }

}
