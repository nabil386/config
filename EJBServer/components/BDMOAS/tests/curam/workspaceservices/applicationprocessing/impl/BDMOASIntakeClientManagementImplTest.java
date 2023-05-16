package curam.workspaceservices.applicationprocessing.impl;

import com.google.inject.Provider;
import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetails;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonSearchResultDetailsList;
import curam.ca.gc.bdmoas.application.impl.BDMOASDatastoreConstants;
import curam.ca.gc.bdmoas.application.impl.BDMOASPersonMatchImpl;
import curam.ca.gc.bdmoas.test.concern.person.RegisterPerson;
import curam.codetable.IEGYESNO;
import curam.core.facade.intf.ProspectPerson;
import curam.core.facade.struct.ProspectPersonRegistrationDetails;
import curam.core.facade.struct.ProspectPersonRegistrationResult;
import curam.core.struct.PersonRegistrationDetails;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.workspaceservices.intake.impl.IntakeApplication;
import curam.workspaceservices.intake.impl.IntakeApplicationConcernRoleLink;
import curam.workspaceservices.intake.impl.IntakeApplicationConcernRoleLinkDAO;
import curam.workspaceservices.intake.impl.IntakeApplicationDAO;
import curam.workspaceservices.mappingbeans.impl.ProspectPersonMappingBean;
import java.util.List;
import mockit.Capturing;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class BDMOASIntakeClientManagementImplTest
  extends CuramServerTestJUnit4 {

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  // BDMEvidenceUtilsTest bdmEvidenceUtils;

  @Tested
  private BDMOASIntakeClientManagementImpl bdmoasIntakeClientManagementImpl;

  @Injectable
  private ConcernRoleDAO concernRoleDAO;

  @Injectable
  private IntakeApplicationConcernRoleLinkDAO intakeApplicationConcernRoleLinkDAO;

  @Injectable
  private IntakeApplicationDAO intakeApplicationDAO;

  @Injectable
  private Provider<MappedProspectPersonInternal> mappedPersonProvider;

  @Mocked
  IntakeApplication intakeApplication;

  @Mocked
  List<IntakeApplicationConcernRoleLink> existingLinks;

  @Mocked
  BDMIntakeClientManagementImpl BDMIntakeClientManagementImpl;

  @Mocked
  BDMOASPersonMatchImpl BDMOASPersonMatchImpl;

  @Capturing
  ProspectPerson prospectPerson;

  ProspectPersonMappingBean intakeClient;

  BDMPersonSearchResultDetailsList personSearchResultDtlsList;

  PersonRegistrationDetails personRegistrationDetails;

  Datastore datastore;

  Entity applicationEntity;

  Entity personEntity;

  /* PDCPersonDetails pdcPersonDetails; */

  ConcernRole concernRole;

  String addressData;

  @Before
  public void setUp()
    throws AppException, InformationalException, NoSuchSchemaException {

    GuiceWrapper.getInjector().injectMembers(this);

    personSearchResultDtlsList = new BDMPersonSearchResultDetailsList();
    personRegistrationDetails = new PersonRegistrationDetails();

    bdmoasIntakeClientManagementImpl = new BDMOASIntakeClientManagementImpl();

    // prospectPerson = ProspectPersonFactory.newInstance();

    datastore = DatastoreFactory.newInstance()
      .openDatastore("BDMOASGISApplicationSchema");

    addressData = "1\r\n" + "0\r\n" + "BDMINTL\r\n" + "CA\r\n" + "1\r\n"
      + "2\r\n" + "POBOXNO=\r\n" + "APT=4324\r\n" + "ADD1=434\r\n"
      + "ADD2=NLfante Plaza\r\n" + "CITY=Montreal\r\n" + "PROV=QC\r\n"
      + "STATEPROV=\r\n" + "BDMSTPROVX=\r\n" + "COUNTRY=CA\r\n"
      + "POSTCODE=B1B1B1\r\n" + "ZIP=\r\n" + "BDMZIPX=\r\n" + "";

  }

  private BDMPersonSearchResultDetails setUpPersonSearchResultDetailsList(
    final String sin, final String firstName, final String lastName,
    final Date dateOfBirth) throws AppException, InformationalException {

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

    applicationEntity =
      datastore.newEntity(BDMDatastoreConstants.APPLICATION);
    datastore.addRootEntity(applicationEntity);

    personEntity = datastore.newEntity(BDMDatastoreConstants.PERSON);

    personEntity.setTypedAttribute("firstName", firstName);
    personEntity.setTypedAttribute("lastName", lastName);
    personEntity.setTypedAttribute("dateOfBirth", dateOfBirth);
    personEntity.setAttribute("ssn", sin);
    personEntity.setAttribute(BDMDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    personEntity.setAttribute("localID",
      "" + personRegistrationDetails.concernID);
    personEntity.setTypedAttribute(
      BDMDatastoreConstants.IS_MAILING_ADDRESS_DIFFERENT, IEGYESNO.YES);
    applicationEntity.addChildEntity(personEntity);

    final Entity residentialEntity =
      datastore.newEntity(BDMOASDatastoreConstants.kResidentialAddress);
    residentialEntity.setAttribute(BDMDatastoreConstants.SUITE_NUM, "4324");
    residentialEntity.setAttribute(BDMDatastoreConstants.CITY, "Montreal");
    residentialEntity.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY,
      "CA");
    residentialEntity.setAttribute(BDMDatastoreConstants.PROVINCE, "QC");
    residentialEntity.setAttribute(BDMDatastoreConstants.POSTAL_CODE,
      "B1B1B1");
    residentialEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1,
      "434");
    residentialEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
      "NLfante Plaza");
    residentialEntity.setAttribute(BDMDatastoreConstants.ONE_LINE_ADDRESS,
      "test string here");
    personEntity.addChildEntity(residentialEntity);

    final Entity mailingEntity =
      datastore.newEntity(BDMOASDatastoreConstants.kMailingAddress);
    mailingEntity.setAttribute(BDMDatastoreConstants.SUITE_NUM, "4324");
    mailingEntity.setAttribute(BDMDatastoreConstants.CITY, "Montreal");
    mailingEntity.setAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY, "CA");
    mailingEntity.setAttribute(BDMDatastoreConstants.PROVINCE, "QC");
    mailingEntity.setAttribute(BDMDatastoreConstants.POSTAL_CODE, "B1B1B1");
    mailingEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE1, "434");
    mailingEntity.setAttribute(BDMDatastoreConstants.ADDRESS_LINE2,
      "NLfante Plaza");
    mailingEntity.setAttribute(BDMDatastoreConstants.ONE_LINE_ADDRESS,
      "test string here");
    personEntity.addChildEntity(mailingEntity);

    mailingEntity.update();
    residentialEntity.update();

    personEntity.update();
    applicationEntity.update();

    intakeClient = new ProspectPersonMappingBean(datastore, personEntity);

    return personDtls;
  }

  /**
   * Tests call to BDM person match if external user.
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testfindPersonMatch_externalUser()
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());

    applicationEntity.setAttribute(BDMDatastoreConstants.EXTERNAL_USER_NAME,
      "testadmin");
    applicationEntity.update();

    final MappedProspectPersonInternal mappedProspectPersonInternal =
      bdmoasIntakeClientManagementImpl.findPersonMatch(intakeClient);

    new Verifications() {

      {
        BDMIntakeClientManagementImpl.findPersonMatch(intakeClient);
        times = 1;
      }
    };

  }

  /**
   * Tests call to person match
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testfindPersonMatch_exactMatch_Register()
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());

    final ProspectPersonRegistrationResult registrationResult =
      new ProspectPersonRegistrationResult();
    registrationResult.registrationIDDetails.concernRoleID =
      dtls1.concernRoleID;
    registrationResult.registrationIDDetails.alternateID =
      personRegistrationDetails.socialSecurityNumber;

    new Expectations() {

      {
        BDMOASPersonMatchImpl.performExactMatch(intakeClient);
        result = personSearchResultDtlsList;

        concernRoleDAO.get(anyLong);
        result = concernRole;

        intakeApplicationDAO.readByRootEntityID(anyLong);
        result = intakeApplication;

        intakeApplicationConcernRoleLinkDAO
          .listByConcernRoleIntakeApplication(concernRole, intakeApplication);
        result = existingLinks;

        existingLinks.isEmpty();
        result = false;

        BDMIntakeClientManagementImpl.getStringAttribute((Entity) any,
          anyString);
        result = new Delegate<String>() {

          String attr(final Entity entity, final String attributeName) {

            return entity.getAttribute(attributeName);
          }
        };

        BDMIntakeClientManagementImpl.getAddress((Entity) any);
        result = addressData;

        BDMIntakeClientManagementImpl.getMailingAddress((Entity) any);
        result = addressData;

        prospectPerson
          .registerProspectPerson((ProspectPersonRegistrationDetails) any);
        result = registrationResult;
      }
    };

    final MappedProspectPersonInternal mappedProspectPersonInternal =
      bdmoasIntakeClientManagementImpl.findPersonMatch(intakeClient);

    final int x = 1;
  }

  /**
   * Tests call to person match
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testfindPersonMatch_exactMatch_NoRegister()
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());

    new Expectations() {

      {
        BDMOASPersonMatchImpl.performExactMatch(intakeClient);
        result = personSearchResultDtlsList;

        concernRoleDAO.get(anyLong);
        result = concernRole;

        intakeApplicationDAO.readByRootEntityID(anyLong);
        result = intakeApplication;

        intakeApplicationConcernRoleLinkDAO
          .listByConcernRoleIntakeApplication(concernRole, intakeApplication);
        result = existingLinks;

        existingLinks.isEmpty();
        result = true;

      }
    };

    final MappedProspectPersonInternal mappedProspectPersonInternal =
      bdmoasIntakeClientManagementImpl.findPersonMatch(intakeClient);

  }

  /**
   * Tests call to person match
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testfindPersonMatch_partialMatch()
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());

    final BDMPersonSearchResultDetailsList personSearchResultDtlsListEmpty =
      new BDMPersonSearchResultDetailsList();

    final ProspectPersonRegistrationResult registrationResult =
      new ProspectPersonRegistrationResult();
    registrationResult.registrationIDDetails.concernRoleID =
      dtls1.concernRoleID;
    registrationResult.registrationIDDetails.alternateID =
      personRegistrationDetails.socialSecurityNumber;

    new Expectations() {

      {
        BDMOASPersonMatchImpl.performExactMatch(intakeClient);
        result = personSearchResultDtlsListEmpty;

        BDMOASPersonMatchImpl.isPartialMatchFound(intakeClient);
        result = true;

        prospectPerson
          .registerProspectPerson((ProspectPersonRegistrationDetails) any);
        result = registrationResult;

      }
    };

    final MappedProspectPersonInternal mappedProspectPersonInternal =
      bdmoasIntakeClientManagementImpl.findPersonMatch(intakeClient);
  }

  /**
   * Tests call to person match
   *
   * @throws AppException
   * @throws InformationalException
   */

  @Test
  public void testfindPersonMatch_noMatch()
    throws AppException, InformationalException {

    final BDMPersonSearchResultDetails dtls1 =
      setUpPersonSearchResultDetailsList("111111111", "Jane", "Doe",
        Date.getCurrentDate());

    final BDMPersonSearchResultDetailsList personSearchResultDtlsListEmpty =
      new BDMPersonSearchResultDetailsList();

    new Expectations() {

      {
        BDMOASPersonMatchImpl.performExactMatch(intakeClient);
        result = personSearchResultDtlsListEmpty;

        BDMOASPersonMatchImpl.isPartialMatchFound(intakeClient);
        result = false;

      }
    };

    final MappedProspectPersonInternal mappedProspectPersonInternal =
      bdmoasIntakeClientManagementImpl.findPersonMatch(intakeClient);
  }

}
