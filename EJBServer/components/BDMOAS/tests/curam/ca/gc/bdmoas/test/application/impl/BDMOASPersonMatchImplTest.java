package curam.ca.gc.bdmoas.test.application.impl;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.application.impl.BDMPersonMatchConstants;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetails;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch;
import curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatchImpl;
import curam.ca.gc.bdmoas.test.concern.person.RegisterPerson;
import curam.testframework.CuramServerTestJUnit4;
import curam.core.struct.PersonRegistrationDetails;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.workspaceservices.mappingbeans.impl.ProspectPersonMappingBean;
import java.util.HashMap;
import java.util.Map;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMOASPersonMatchImplTest extends CuramServerTestJUnit4 {

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  @Tested
  private BDMOASPersonMatchImpl bdmoasPersonMatchImpl;

  @Mocked
  BDMOASPersonMatch BDMOASPersonMatch;

  @Mocked
  BDMUtil BDMUtil;

  ProspectPersonMappingBean intakeClient;

  // @Mocked
  // BDMOASPersonFactory BDMOASpersonFactory;

  BDMPersonSearchResultDetailsList personSearchResultDtlsList;

  PersonRegistrationDetails personRegistrationDetails;

  Datastore datastore;

  @Before
  public void setUp()
    throws AppException, InformationalException, NoSuchSchemaException {

    bdmoasPersonMatchImpl = new BDMOASPersonMatchImpl();

    BDMOASPersonMatch = new BDMOASPersonMatch();

    personSearchResultDtlsList = new BDMPersonSearchResultDetailsList();
    personRegistrationDetails = new PersonRegistrationDetails();

    datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");
  }

  private BDMPersonSearchResultDetails setUpPersonSearchResultDetailsList(
    final String sin, final String firstName, final String lastName,
    final Date dateOfBirth) throws AppException, InformationalException {

    final String addressData = "1\r\n" + "0\r\n" + "BDMINTL\r\n" + "CA\r\n"
      + "1\r\n" + "2\r\n" + "POBOXNO=\r\n" + "APT=4324\r\n" + "ADD1=434\r\n"
      + "ADD2=NLfante Plaza\r\n" + "CITY=Montreal\r\n" + "PROV=QC\r\n"
      + "STATEPROV=\r\n" + "BDMSTPROVX=\r\n" + "COUNTRY=CA\r\n"
      + "POSTCODE=B1B1B1\r\n" + "ZIP=\r\n" + "BDMZIPX=\r\n" + "";

    personRegistrationDetails = registerPerson.getPersonRegistrationDetails();

    personRegistrationDetails.socialSecurityNumber = sin;
    personRegistrationDetails.firstForename = firstName;
    personRegistrationDetails.surname = lastName;
    personRegistrationDetails.dateOfBirth = dateOfBirth;
    personRegistrationDetails.addressData = addressData;
    final BDMPersonSearchResultDetails personDtls =
      new BDMPersonSearchResultDetails();
    personDtls.assign(personRegistrationDetails);

    // return list
    personSearchResultDtlsList.dtls.add(personDtls);

    // Creates a ProspectPersonMappingBean to pass into performExactPersonMatch

    final Entity personEntity =
      datastore.newEntity(BDMDatastoreConstants.PERSON);

    personEntity.setTypedAttribute("firstName", firstName);
    personEntity.setTypedAttribute("lastName", lastName);
    personEntity.setTypedAttribute("dateOfBirth", dateOfBirth);
    personEntity.setAttribute("ssn", sin);

    intakeClient = new ProspectPersonMappingBean(datastore, personEntity);

    /*
     * final ProspectPersonRegistrationDtls searchDtls =
     * new ProspectPersonRegistrationDtls();
     * searchDtls.socialSecurityNumber = sin;
     * searchDtls.surname = lastName;
     * searchDtls.firstForename = firstName;
     * searchDtls.dateOfBirth = dateOfBirth;
     * intakeClient.setProspectPersonDetails(searchDtls);
     */

    return personDtls;
  }

  private Map<String, String> getAddressMap()
    throws AppException, InformationalException {

    final Map<String, String> dummyAddrMap = new HashMap<String, String>();
    dummyAddrMap.put("ZIP", "");
    dummyAddrMap.put("BDMSTPROVX", "");
    dummyAddrMap.put("COUNTRY", "");
    dummyAddrMap.put("CITY", "Montreal");
    dummyAddrMap.put("APT", "4324");
    dummyAddrMap.put("POBOXNO", "");
    dummyAddrMap.put("POSTCODE", "B1B1B1");
    dummyAddrMap.put("BDMZIPX", "");
    dummyAddrMap.put("STATEPROV", "");
    dummyAddrMap.put("PROV", "QC");
    dummyAddrMap.put("ADD1", "434");
    dummyAddrMap.put("ADD2", "NLfante Plaza");

    return dummyAddrMap;
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
  // mocking intakeClient as it is not necessary for this function testing
  public void testDeterminePersonMatchType(@Mocked
  final ProspectPersonMappingBean intakeClient2)
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());

    new Expectations() {

      {

        intakeClient2.getPersonEntity().getAttribute(anyString);
        result = "111111111";
        intakeClient2.getProspectPersonDetails().dateOfBirth =
          Date.getCurrentDate();
        intakeClient2.getPersonEntity().getAttribute(anyString);
        result = "Jane";
        intakeClient2.getPersonEntity().getAttribute(anyString);
        result = "Doe";

        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchBySINDOBFirstNameLastName(anyString, anyString, anyString,
            null);
        result = personSearchResultDtlsList;

      }
    };

    final String personMatchType = bdmoasPersonMatchImpl
      .determinePersonMatchType(personRegistrationDetails);
    assertEquals(personMatchType, BDMPersonMatchConstants.EXACT_PERSON_MATCH);

  }

  @Test
  public void testPerformExactMatch()
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());
    final BDMPersonSearchResultDetails dtls2 =
      setUpPersonSearchResultDetailsList("222222222", "John", "Black",
        Date.getCurrentDate());

    new Expectations() {

      {
        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchBySINDOBFirstNameLastName(anyString, anyString, anyString,
            Date.getCurrentDate());
        result = personSearchResultDtlsList;
      }
    };

    final BDMPersonSearchResultDetailsList searchResultDtlsList =
      bdmoasPersonMatchImpl.performExactMatch(intakeClient);

    assertEquals(dtls1, searchResultDtlsList.dtls.get(0));
  }

  /**
   * Tests to determine Partial Match #1 - return False for exact match
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testIsPartialMatch_ExactMatch()
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());
    final BDMPersonSearchResultDetails dtls2 =
      setUpPersonSearchResultDetailsList("222222222", "John", "Black",
        Date.getCurrentDate());

    new Expectations() {

      {

        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchBySINDOBFirstNameLastName(anyString, anyString, anyString,
            Date.getCurrentDate());
        result = personSearchResultDtlsList;

      }
    };
    assertEquals(bdmoasPersonMatchImpl.isPartialMatchFound(intakeClient),
      false);

  }

  /**
   * Tests to determine Partial Match #2 - return True if SIN match is found
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testIsPartialMatch_SIN()
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());
    final BDMPersonSearchResultDetails dtls2 =
      setUpPersonSearchResultDetailsList("222222222", "John", "Black",
        Date.getCurrentDate());

    // empty result
    final BDMPersonSearchResultDetailsList emptyList =
      new BDMPersonSearchResultDetailsList();
    new Expectations() {

      {

        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchBySINDOBFirstNameLastName(anyString, anyString, anyString,
            Date.getCurrentDate());
        result = emptyList;

        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchPersonBySIN(anyString);
        result = personSearchResultDtlsList;

      }
    };
    assertEquals(bdmoasPersonMatchImpl.isPartialMatchFound(intakeClient),
      true);

  }

  /**
   * Tests to determine Partial Match #3 - return True if address match is found
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testIsPartialMatch_Address()
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());
    final BDMPersonSearchResultDetails dtls2 =
      setUpPersonSearchResultDetailsList("222222222", "John", "Black",
        Date.getCurrentDate());

    // empty result
    final BDMPersonSearchResultDetailsList emptyList =
      new BDMPersonSearchResultDetailsList();

    final Map<String, String> dummyAddrMap = getAddressMap();

    new Expectations() {

      {

        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchBySINDOBFirstNameLastName(anyString, anyString, anyString,
            Date.getCurrentDate());
        result = emptyList;

        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchPersonBySIN(anyString);
        result = emptyList;

        curam.ca.gc.bdm.application.impl.BDMUtil.getAddressDataMap(anyString);
        result = dummyAddrMap;

        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchByDOBNamePostalZipCountry(anyString, anyString, null, null,
            anyString, anyString);
        result = personSearchResultDtlsList;

      }
    };
    assertEquals(bdmoasPersonMatchImpl.isPartialMatchFound(intakeClient),
      true);

  }

  /**
   * Tests to determine Partial Match #4 - return False if no partial matches
   * are found
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testIsPartialMatch_False()
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());
    final BDMPersonSearchResultDetails dtls2 =
      setUpPersonSearchResultDetailsList("222222222", "John", "Black",
        Date.getCurrentDate());

    final Map<String, String> dummyAddrMap = getAddressMap();

    // empty result
    final BDMPersonSearchResultDetailsList emptyList =
      new BDMPersonSearchResultDetailsList();

    new Expectations() {

      {

        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchBySINDOBFirstNameLastName(anyString, anyString, anyString,
            Date.getCurrentDate());
        result = emptyList;

        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchPersonBySIN(anyString);
        result = emptyList;

        curam.ca.gc.bdm.application.impl.BDMUtil.getAddressDataMap(anyString);
        result = dummyAddrMap;

        curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch
          .searchByDOBNamePostalZipCountry(anyString, anyString, null, null,
            anyString, anyString);
        result = emptyList;

      }
    };
    assertEquals(bdmoasPersonMatchImpl.isPartialMatchFound(intakeClient),
      false);

  }
}
