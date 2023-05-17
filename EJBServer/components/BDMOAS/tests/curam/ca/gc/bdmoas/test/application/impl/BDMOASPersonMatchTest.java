package curam.ca.gc.bdmoas.test.application.impl;

import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetails;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatch;
import curam.ca.gc.bdmoas.entity.fact.BDMOASPersonFactory;
import curam.ca.gc.bdmoas.entity.struct.BDMOASPersonSearchKey;
import curam.ca.gc.bdmoas.test.concern.person.RegisterPerson;
import curam.testframework.CuramServerTestJUnit4;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.impl.ADDRESSELEMENTTYPEEntry;
import curam.core.intf.PersonRegistration;
import curam.core.struct.PersonRegistrationDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMOASPersonMatchTest extends CuramServerTestJUnit4 {

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  /* OBJECT IN TEST */
  BDMOASPersonMatch bdmoasPersonMatchObj;

  @Mocked
  BDMOASPersonFactory BDMOASpersonFactory;

  @Mocked
  BDMPersonFactory BDMpersonFactory;

  @Mocked
  PersonRegistration personRegistration;

  BDMPersonSearchResultDetailsList personSearchResultDtlsList;

  // Trace trace;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmoasPersonMatchObj = new BDMOASPersonMatch();

    personSearchResultDtlsList = new BDMPersonSearchResultDetailsList();

    Configuration.setProperty("curam.trace", "trace_on");
  }

  /*
   * Dummy call to BDMPersonFactory - SearchBySINDOBFirstNameLastName()
   *
   * @throws AppException
   *
   * @throws InformationalException
   */

  private void expectationSearchBySINDOBName()
    throws AppException, InformationalException {

    new Expectations() {

      {
        BDMOASPersonFactory.newInstance()
          .searchBySINDOBName((BDMPersonSearchKey) any);
        result = personSearchResultDtlsList;
      }
    };
  }

  /*
   * Dummy call to BDMPersonFactory - searchBySINDOB()
   *
   * @throws AppException
   *
   * @throws InformationalException
   */
  private void expectationSearchBySINDOB()
    throws AppException, InformationalException {

    new Expectations() {

      {
        BDMOASPersonFactory.newInstance()
          .searchByDOBNamePostalZipCountry((BDMOASPersonSearchKey) any);
        result = personSearchResultDtlsList;
      }
    };
  }

  /*
   * Dummy call to BDMPersonFactory - searchBySIN()
   *
   * @throws AppException
   *
   * @throws InformationalException
   */
  private void expectationSearchPersonBySIN()
    throws AppException, InformationalException {

    new Expectations() {

      {
        BDMPersonFactory.newInstance().searchBySIN((BDMPersonSINKey) any);
        result = personSearchResultDtlsList;

      }
    };
  }

  private BDMPersonSearchResultDetails setUpPersonSearchResultDetailsList(
    final String sin, final String firstName, final String lastName,
    final Date dateOfBirth) throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    personRegistrationDetails.socialSecurityNumber = sin;
    personRegistrationDetails.firstForename = firstName;
    personRegistrationDetails.surname = lastName;
    personRegistrationDetails.dateOfBirth = dateOfBirth;

    // return list
    final BDMPersonSearchResultDetails personDtls =
      new BDMPersonSearchResultDetails();
    personDtls.assign(personRegistrationDetails);

    personSearchResultDtlsList.dtls.add(personDtls);
    return personDtls;

  }

  // version with other parameters
  private BDMPersonSearchResultDetails setUpPersonSearchResultDetailsList(
    final String firstName, final String lastName, final Date dateOfBirth,
    final ADDRESSELEMENTTYPEEntry addressElementType,
    final String upperAddrElemValue, final String country)
    throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    personRegistrationDetails.addressType = CONCERNROLEADDRESSTYPE.PRIVATE;
    personRegistrationDetails.firstForename = firstName;
    personRegistrationDetails.surname = lastName;
    personRegistrationDetails.dateOfBirth = dateOfBirth;
    // return list
    final BDMPersonSearchResultDetails personDtls =
      new BDMPersonSearchResultDetails();
    personDtls.assign(personRegistrationDetails);

    personSearchResultDtlsList.dtls.add(personDtls);
    return personDtls;

  }

  @Test
  public void testSearchBySINDOBFirstNameLastName() throws Exception {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());
    final BDMPersonSearchResultDetails dtls2 =
      setUpPersonSearchResultDetailsList("222222222", "John", "Black",
        Date.getCurrentDate());

    expectationSearchBySINDOBName();

    final BDMPersonSearchResultDetailsList testResult =
      BDMOASPersonMatch.searchBySINDOBFirstNameLastName("111111111", "Jane",
        "Doe", Date.getCurrentDate());

    assertEquals("The correct list of person details should be returned.",
      dtls1, testResult.dtls.get(0));
  }

  @Test
  public void testSearchPersonBySIN() throws Exception {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "", "", null);
    final BDMPersonSearchResultDetails dtls2 =
      setUpPersonSearchResultDetailsList("222222222", "", "", null);

    expectationSearchPersonBySIN();

    final BDMPersonSearchResultDetailsList testResult =
      BDMOASPersonMatch.searchPersonBySIN("111111111");

    assertEquals("The correct list of person details should be returned.",
      dtls1, testResult.dtls.get(0));
  }

  @Test
  public void testSearchPersonBySIN_Null() throws Exception {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "", "", null);
    final BDMPersonSearchResultDetails dtls2 =
      setUpPersonSearchResultDetailsList("222222222", "", "", null);

    final BDMPersonSearchResultDetails dtls3 =
      new BDMPersonSearchResultDetails();

    final BDMPersonSearchResultDetailsList testResult =
      BDMOASPersonMatch.searchPersonBySIN(null);

    assertEquals("No person details should be returned.", true,
      testResult.dtls.isEmpty());
  }

  @Test
  public void testsearchByDOBNamePostalZipCountry() throws Exception {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("Jane", "Doe", Date.getCurrentDate(),
        ADDRESSELEMENTTYPEEntry.BDMSTPROV_X, "", "CACODE");
    final BDMPersonSearchResultDetails dtls2 =
      setUpPersonSearchResultDetailsList("John", "Doe", Date.getCurrentDate(),
        ADDRESSELEMENTTYPEEntry.BDMSTPROV_X, "", "CACODE");

    expectationSearchBySINDOB();

    final BDMPersonSearchResultDetailsList testResult = BDMOASPersonMatch
      .searchByDOBNamePostalZipCountry("Jane", "Doe", Date.getCurrentDate(),
        ADDRESSELEMENTTYPEEntry.BDMSTPROV_X, "", "CACODE");

    assertEquals("The correct list of person details should be returned.",
      dtls1, testResult.dtls.get(0));
  }

}
