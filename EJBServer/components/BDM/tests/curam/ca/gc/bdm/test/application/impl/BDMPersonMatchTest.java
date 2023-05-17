package curam.ca.gc.bdm.test.application.impl;

import curam.ca.gc.bdm.application.impl.BDMPersonMatch;
import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonDOBLastNameAtBirthKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINDOBKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINDOBLastNameAtBirthKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSINLastNameAtBirthKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetails;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.intf.PersonRegistration;
import curam.core.struct.PersonRegistrationDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMPersonMatchTest extends CuramServerTestJUnit4 {

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  /* OBJECT IN TEST */
  BDMPersonMatch bdmPersonMatchObj;

  @Mocked
  BDMPersonFactory personFactory;

  @Mocked
  PersonRegistration personRegistration;

  BDMPersonSearchResultDetailsList personSearchResultDtlsList;

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmPersonMatchObj = new BDMPersonMatch();

    personSearchResultDtlsList = new BDMPersonSearchResultDetailsList();
  }

  /*
   * Dummy call to BDMPersonFactory - searchBySINDOBLastNameAtBirth()
   *
   * @throws AppException
   *
   * @throws InformationalException
   */

  private void expectationSearchBySINDOBLastNameAtBirth()
    throws AppException, InformationalException {

    new Expectations() {

      {
        BDMPersonFactory.newInstance().searchBySINDOBLastNameAtBirth(
          (BDMPersonSINDOBLastNameAtBirthKey) any);
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
        BDMPersonFactory.newInstance()
          .searchBySINDOB((BDMPersonSINDOBKey) any);
        result = personSearchResultDtlsList;
      }
    };
  }

  /*
   * Dummy call to BDMPersonFactory - searchBySINLastNameAtBirth()
   *
   * @throws AppException
   *
   * @throws InformationalException
   */
  private void expectationSearchBySINLastNameAtBirth()
    throws AppException, InformationalException {

    new Expectations() {

      {
        BDMPersonFactory.newInstance()
          .searchBySINLastNameAtBirth((BDMPersonSINLastNameAtBirthKey) any);
        result = personSearchResultDtlsList;

      }
    };
  }

  /*
   * Dummy call to BDMPersonFactory - searchByDOBLastNameAtBirth()
   *
   * @throws AppException
   *
   * @throws InformationalException
   */
  private void expectationSearchByDOBLastNameAtBirth()
    throws AppException, InformationalException {

    new Expectations() {

      {
        BDMPersonFactory.newInstance()
          .searchByDOBLastNameAtBirth((BDMPersonDOBLastNameAtBirthKey) any);
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

  private void setUpPersonSearchResultDetailsList(final String sin,
    final String lastNameAtBirth, final Date dateOfBirth)
    throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    personRegistrationDetails.socialSecurityNumber = sin;
    personRegistrationDetails.birthName = lastNameAtBirth;
    personRegistrationDetails.dateOfBirth = dateOfBirth;

    // return list
    final BDMPersonSearchResultDetails personDtls =
      new BDMPersonSearchResultDetails();
    personDtls.assign(personRegistrationDetails);

    personSearchResultDtlsList.dtls.add(personDtls);

  }

  @Test
  public void testSearchBySINDOBLastNameAtBirth() throws Exception {

    setUpPersonSearchResultDetailsList("111111111", "Test1",
      Date.getCurrentDate());
    setUpPersonSearchResultDetailsList("222222222", "Test2",
      Date.getCurrentDate());

    expectationSearchBySINDOBLastNameAtBirth();

    final BDMPersonSearchResultDetailsList result =
      BDMPersonMatch.searchBySINDOBLastNameAtBirth("111111111", "Test1",
        Date.getCurrentDate());

    final BDMPersonSearchResultDetails dtls1 = result.dtls.get(0);

    assertEquals(2, result.dtls.size());
  }

  @Test
  public void testSearchBySINDOB() throws Exception {

    setUpPersonSearchResultDetailsList("111111111", "",
      Date.getCurrentDate());
    setUpPersonSearchResultDetailsList("222222222", "",
      Date.getCurrentDate());

    expectationSearchBySINDOB();

    BDMPersonSearchResultDetailsList result;

    result =
      BDMPersonMatch.searchBySINDOB("111111111", Date.getCurrentDate());

    assertEquals(2, result.dtls.size());
  }

  @Test
  public void testSearchBySINLastNameAtBirth() throws Exception {

    setUpPersonSearchResultDetailsList("111111111", "Test1", null);
    setUpPersonSearchResultDetailsList("222222222", "Test2", null);

    expectationSearchBySINLastNameAtBirth();

    BDMPersonSearchResultDetailsList result;

    result = BDMPersonMatch.searchBySINLastNameAtBirth("111111111", "Test1");
    assertEquals(2, result.dtls.size());
  }

  @Test
  public void testSearchPersonBySIN() throws Exception {

    setUpPersonSearchResultDetailsList("111111111", "", null);
    setUpPersonSearchResultDetailsList("222222222", "", null);

    expectationSearchPersonBySIN();

    BDMPersonSearchResultDetailsList result;

    result = BDMPersonMatch.searchPersonBySIN("111111111");
    assertEquals(2, result.dtls.size());
  }

  @Test
  public void testSearchByDOBLastNameAtBirth() throws Exception {

    setUpPersonSearchResultDetailsList("", "Test1", Date.getCurrentDate());
    setUpPersonSearchResultDetailsList("", "Test2", Date.getCurrentDate());

    expectationSearchByDOBLastNameAtBirth();

    BDMPersonSearchResultDetailsList result;

    result = BDMPersonMatch.searchByDOBLastNameAtBirth("Test1",
      Date.getCurrentDate());
    assertEquals(2, result.dtls.size());
  }
}
