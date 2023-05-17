/**
 *
 */
package curam.ca.gc.bdm.test.facade.participant.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMCCTSUBMITOPT;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceSearchKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceWizardKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateDetails;
import curam.ca.gc.bdm.facade.participant.impl.BDMParticipant;
import curam.ca.gc.bdm.facade.participant.struct.BDMCommunicationDetailList;
import curam.ca.gc.bdm.facade.participant.struct.BDMConcernRolePhoneDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMCreateContactFromUnregParticipant;
import curam.ca.gc.bdm.facade.participant.struct.BDMDate;
import curam.ca.gc.bdm.facade.participant.struct.BDMListParticipantFinancials1;
import curam.ca.gc.bdm.facade.participant.struct.BDMMaintainParticipantPhoneDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMReadParticipantAddressDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.LANGUAGE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PHONETYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.intf.UniqueID;
import curam.core.facade.struct.CreateParticipantEmailAddressDetails;
import curam.core.facade.struct.CreateParticipantPhoneDetails;
import curam.core.facade.struct.ListParticipantFinancialsKey;
import curam.core.facade.struct.MaintainParticipantEmailAddressDetails;
import curam.core.facade.struct.ParticipantCommunicationKey;
import curam.core.facade.struct.ReadParticipantAddressList;
import curam.core.facade.struct.ReadParticipantAddressListKey;
import curam.core.facade.struct.ReadParticipantPhoneNumberDetails;
import curam.core.facade.struct.ReadParticipantPhoneNumberKey;
import curam.core.facade.struct.ReadParticipantPhoneNumberList;
import curam.core.facade.struct.ReadParticipantPhoneNumberListKey;
import curam.core.facade.struct.WizardStateID;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.AlternateName;
import curam.core.intf.PersonRegistration;
import curam.core.sl.struct.SearchAlternateIDDetailsKey;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.AlternateNameDtlsList;
import curam.core.struct.AlternateNameKey;
import curam.core.struct.AlternateNameReadByTypeKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.core.struct.SearchForAlternateNameKey;
import curam.datastore.impl.NoSuchSchemaException;
import curam.pdc.fact.PDCAlternateNameFactory;
import curam.pdc.intf.PDCAlternateName;
import curam.pdc.struct.PDCPersonDetails;
import curam.pdc.struct.ParticipantAlternateNameDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * @author amod.gole
 *
 * Test Class for BDMParticipant
 *
 */
public class BDMParticipantTest extends CuramServerTestJUnit4 {

  private final UniqueID uniqueIDObj;

  curam.ca.gc.bdm.sl.communication.intf.BDMCommunication kBdmCommunication =
    BDMCommunicationFactory.newInstance();

  final curam.ca.gc.bdm.sl.communication.intf.BDMCommunication bdmCommunicationObj =
    curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory
      .newInstance();

  final String kFirstName = "First";

  final String kMiddleName = "Middle";

  final String kLastName = "Last";

  BDMEvidenceUtilsTest bdmEvidenceUtilsTest = new BDMEvidenceUtilsTest();

  @Inject
  BDMParticipant bdmParticipant;

  public BDMParticipantTest() {

    this.uniqueIDObj = null;
    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws NoSuchSchemaException {

  }

  /**
   * test method createPreferredName
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testcreatePreferredName()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    // Map Data
    final SearchForAlternateNameKey searchForAlternateNameKey =
      new SearchForAlternateNameKey();
    searchForAlternateNameKey.concernRoleID = concernRoleID;
    searchForAlternateNameKey.firstForename = kFirstName;
    searchForAlternateNameKey.otherForename = kMiddleName;
    searchForAlternateNameKey.surname = kLastName;
    // Call Method to test
    bdmParticipant.createPreferredName(searchForAlternateNameKey);

    // Read Data from the tables that record is created
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = concernRoleID;
    final curam.core.intf.Person personObj = PersonFactory.newInstance();

    final PersonDtls personDtls = personObj.read(personKey, true);

    final AlternateName alternateNameObj = AlternateNameFactory.newInstance();
    final AlternateNameKey alternateNameKey = new AlternateNameKey();
    alternateNameKey.alternateNameID = personDtls.primaryAlternateNameID;

    final ParticipantAlternateNameDetails nameDetails =
      new ParticipantAlternateNameDetails();

    // Check if an preferred name already exists
    final AlternateNameReadByTypeKey alternateNameReadByTypeKey =
      new AlternateNameReadByTypeKey();
    alternateNameReadByTypeKey.concernRoleID = concernRoleID;
    alternateNameReadByTypeKey.nameStatus = RECORDSTATUS.NORMAL;
    alternateNameReadByTypeKey.nameType = ALTERNATENAMETYPE.PREFERRED;
    final AlternateNameDtlsList alternateNameDtlsList =
      alternateNameObj.searchByType(alternateNameReadByTypeKey);

    final PDCAlternateName pdcAlternateNameObj =
      PDCAlternateNameFactory.newInstance();
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    // Assert for Names
    if (!alternateNameDtlsList.dtls.isEmpty()) {
      final AlternateNameDtls prefAlternateNameDtls =
        alternateNameDtlsList.dtls.get(0);
      assertTrue(kFirstName.equals(prefAlternateNameDtls.firstForename));
      assertTrue(kMiddleName.equals(prefAlternateNameDtls.otherForename));
      assertTrue(kLastName.equals(prefAlternateNameDtls.surname));

    } else {
      assertTrue(false);
    }

  }

  /**
   * test method readPreferredName
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testreadPreferredName()
    throws AppException, InformationalException {

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    // Map data
    final SearchForAlternateNameKey searchForAlternateNameKey =
      new SearchForAlternateNameKey();
    searchForAlternateNameKey.concernRoleID = concernRoleID;
    searchForAlternateNameKey.firstForename = kFirstName;
    searchForAlternateNameKey.otherForename = kMiddleName;
    searchForAlternateNameKey.surname = kLastName;
    // Insert REcord
    bdmParticipant.createPreferredName(searchForAlternateNameKey);

    final SearchAlternateIDDetailsKey details =
      new SearchAlternateIDDetailsKey();

    details.concernRoleID = concernRoleID;
    // Call Method to test
    final SearchForAlternateNameKey readSearchForAlternateNameKey =
      bdmParticipant.readPreferredName(details);

    assertTrue(
      kFirstName.equals(readSearchForAlternateNameKey.firstForename));
    assertTrue(
      kMiddleName.equals(readSearchForAlternateNameKey.otherForename));
    assertTrue(kLastName.equals(readSearchForAlternateNameKey.surname));

  }

  @Test
  public void testlistCommunication()
    throws AppException, InformationalException {

    final ParticipantCommunicationKey key = new ParticipantCommunicationKey();

    // key.participantCommKey = 32332333l;
    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    // Map data
    final SearchForAlternateNameKey searchForAlternateNameKey =
      new SearchForAlternateNameKey();
    searchForAlternateNameKey.concernRoleID = concernRoleID;
    searchForAlternateNameKey.firstForename = kFirstName;
    searchForAlternateNameKey.otherForename = kMiddleName;
    searchForAlternateNameKey.surname = kLastName;
    // Insert REcord
    bdmParticipant.createPreferredName(searchForAlternateNameKey);

    final SearchAlternateIDDetailsKey details =
      new SearchAlternateIDDetailsKey();

    details.concernRoleID = concernRoleID;
    // Call Method to test
    final BDMCommunicationDetailList readSearchForAlternateNameKey =
      bdmParticipant.listCommunication(key);

  }

  @Test
  public void testreadMailingAddress()
    throws AppException, InformationalException {

    final ReadParticipantAddressListKey key =
      new ReadParticipantAddressListKey();
    final BDMReadParticipantAddressDetails addressDetails =
      new BDMReadParticipantAddressDetails();

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    addressDetails.addressID = pdcPersonDetails.concernRoleID;
    addressDetails.dtls.addressDetails.concernRoleID =
      pdcPersonDetails.concernRoleID;
    addressDetails.dtls.addressDetails.addressData =
      otherAddressData.addressData;
    addressDetails.dtls.addressDetails.caseID =
      uniqueIDObj.getNextID().uniqueID;

    // addressDetails.dtls.addressDetails.concernRoleAddressID =
    // uniqueIDObj.getNextID().uniqueID;

    // Map data
    final SearchForAlternateNameKey searchForAlternateNameKey =
      new SearchForAlternateNameKey();
    searchForAlternateNameKey.concernRoleID = concernRoleID;
    searchForAlternateNameKey.firstForename = kFirstName;
    searchForAlternateNameKey.otherForename = kMiddleName;
    searchForAlternateNameKey.surname = kLastName;
    // Insert REcord
    bdmParticipant.createPreferredName(searchForAlternateNameKey);

    final SearchAlternateIDDetailsKey details =
      new SearchAlternateIDDetailsKey();

    details.concernRoleID = concernRoleID;
    // Call Method to test
    final BDMReadParticipantAddressDetails bmbReadParticipantAddressDetails =
      bdmParticipant.readMailingAddress(key);

    assertTrue(details.concernRoleID != 0);

  }

  /**
   * test method readPreferredName
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testlistParticipantFinancial1()
    throws AppException, InformationalException {

    final ListParticipantFinancialsKey key =
      new ListParticipantFinancialsKey();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final long concernRoleID = pdcPersonDetails.concernRoleID;

    key.concernRoleID = pdcPersonDetails.concernRoleID;
    // Create Concern Role PDC

    // Map data
    final SearchForAlternateNameKey searchForAlternateNameKey =
      new SearchForAlternateNameKey();
    searchForAlternateNameKey.concernRoleID = concernRoleID;
    searchForAlternateNameKey.firstForename = kFirstName;
    searchForAlternateNameKey.otherForename = kMiddleName;
    searchForAlternateNameKey.surname = kLastName;
    // Insert REcord
    bdmParticipant.createPreferredName(searchForAlternateNameKey);

    final SearchAlternateIDDetailsKey details =
      new SearchAlternateIDDetailsKey();

    details.concernRoleID = concernRoleID;
    // Call Method to test
    final BDMListParticipantFinancials1 bdmListParticipantFinancials1 =
      bdmParticipant.listParticipantFinancial1(key);
    assertTrue(details.concernRoleID != 0);
  }

  /**
   * test method readPreferredName
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testmodifyPhoneNumber()
    throws AppException, InformationalException {

    final BDMMaintainParticipantPhoneDetails details =
      new BDMMaintainParticipantPhoneDetails();

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final long concernRoleID = pdcPersonDetails.concernRoleID;
    details.dtls.concernRolePhoneDetails.concernRoleID =
      pdcPersonDetails.concernRoleID;

    details.dtls.concernRolePhoneDetails.phoneAreaCode = "123";
    details.dtls.concernRolePhoneDetails.phoneNumber = "4567890";
    details.dtls.concernRolePhoneDetails.phoneExtension = "123";
    details.dtls.concernRolePhoneDetails.typeCode = PHONETYPE.PERSONAL_MOBILE;
    // details.dtls.concernRolePhoneDetails.startDate =
    curam.util.type.Date.getCurrentDate();

    bdmParticipant.modifyPhoneNumber(details);
    assertTrue(details.dtls.concernRolePhoneDetails.concernRoleID != 0);

  }

  @Test
  public void testcreateContactFromUnregisteredParticipant()
    throws AppException, InformationalException {

    final BDMCreateContactFromUnregParticipant details =
      new BDMCreateContactFromUnregParticipant();

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    details.preferredLanguage = LANGUAGE.ENGLISH;

    details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeDtls.concernRoleID =
      pdcPersonDetails.concernRoleID;
    details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeDtls.representativeName =
      "John";
    details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeDtls.upperRepresentativeName =
      "JOHN";
    details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeDtls.paymentFrequency =
      "100100100";
    details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeDtls.methodOfPmtCode =
      METHODOFDELIVERY.CHEQUE;
    details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeDtls.representativeType =
      curam.codetable.REPRESENTATIVETYPE.CONTACT;

    details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeDtls.concernRoleID =
      pdcPersonDetails.concernRoleID;

    bdmParticipant.createContactFromUnregisteredParticipant(details);
    assertTrue(
      details.dtls.representativeRegistrationDetails.representativeRegistrationDetails.representativeDtls.concernRoleID != 0);

  }

  @Test
  public void testdisplayDefaultEndDate()
    throws AppException, InformationalException {

    final BDMDate defaultEndDate = new BDMDate();
    defaultEndDate.date = Date.getCurrentDate()
      .addDays(BDMConstants.kUnverifiedContactDefaultEndDate);
    final BDMDate date = bdmParticipant.displayDefaultEndDate();

  }

  @Test
  public void testisParticipantDuplicateOrRdRv()
    throws AppException, InformationalException {

    final ConcernRoleKey key = new ConcernRoleKey();

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    key.concernRoleID = pdcPersonDetails.concernRoleID;

    bdmParticipant.isParticipantDuplicateOrRdRv(key);

  }

  /**
   * test method readPreferredName
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void testlistAddress() throws AppException, InformationalException {

    final ReadParticipantAddressListKey key =
      new ReadParticipantAddressListKey();
    final BDMReadParticipantAddressDetails addressDetails =
      new BDMReadParticipantAddressDetails();

    // Create Concern Role PDC
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    addressDetails.addressID = pdcPersonDetails.concernRoleID;
    addressDetails.dtls.addressDetails.concernRoleID =
      pdcPersonDetails.concernRoleID;
    addressDetails.dtls.addressDetails.addressData =
      otherAddressData.addressData;

    // Map data
    final SearchForAlternateNameKey searchForAlternateNameKey =
      new SearchForAlternateNameKey();
    searchForAlternateNameKey.concernRoleID = concernRoleID;
    searchForAlternateNameKey.firstForename = kFirstName;
    searchForAlternateNameKey.otherForename = kMiddleName;
    searchForAlternateNameKey.surname = kLastName;
    // Insert REcord
    bdmParticipant.createPreferredName(searchForAlternateNameKey);
    key.maintainAddressKey.concernRoleID = concernRoleID;

    final SearchAlternateIDDetailsKey details =
      new SearchAlternateIDDetailsKey();

    details.concernRoleID = concernRoleID;
    // Call Method to test
    final ReadParticipantAddressList readParticipantAddressList =
      bdmParticipant.listAddress(key);

    // assertTrue("123l".equals(bmbReadParticipantAddressDetails.addressID));

  }

  @Test
  public void testcreateEmailAddress()
    throws AppException, InformationalException {

    final MaintainParticipantEmailAddressDetails details =
      new MaintainParticipantEmailAddressDetails();

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final long concernRoleID = pdcPersonDetails.concernRoleID;
    details.concernRoleEmailDetails.concernRoleID =
      pdcPersonDetails.concernRoleID;

    details.concernRoleEmailDetails.emailAddress = "test@gmail.com";
    details.concernRoleEmailDetails.concernRoleID =
      pdcPersonDetails.concernRoleID;
    details.concernRoleEmailDetails.concernRoleEmailAddressID = 1234l;
    details.concernRoleEmailDetails.startDate = Date.getCurrentDate();
    details.concernRoleEmailDetails.typeCode = PHONETYPE.PERSONAL_MOBILE;

    final CreateParticipantEmailAddressDetails createParticipantEmailAddressDetails =
      bdmParticipant.createEmailAddress(details);

  }

  @Test
  public void testmodifyEmailAddress()
    throws AppException, InformationalException {

    final MaintainParticipantEmailAddressDetails details =
      new MaintainParticipantEmailAddressDetails();

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    // final long concernRoleID = pdcPersonDetails.concernRoleID;
    details.concernRoleEmailDetails.concernRoleID =
      pdcPersonDetails.concernRoleID;

    details.concernRoleEmailDetails.emailAddress = "test123@gmail.com";
    details.concernRoleEmailDetails.emailAddressID = 1345678;
    details.concernRoleEmailDetails.statusCode = RECORDSTATUS.NORMAL;
    details.concernRoleEmailDetails.typeCode = PHONETYPE.PERSONAL_MOBILE;
    details.concernRoleEmailDetails.startDate = Date.getCurrentDate();

    // Set values for email address dtls values.
    details.concernRoleEmailDetails.concernRoleEmailAddressID = 1233l;

    bdmParticipant.modifyEmailAddress(details);

  }

  @Test
  public void testlistPhoneNumber()
    throws AppException, InformationalException {

    final ReadParticipantPhoneNumberListKey key =
      new ReadParticipantPhoneNumberListKey();

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    key.maintainPhoneNumberKey.concernRoleID = pdcPersonDetails.concernRoleID;

    final ReadParticipantPhoneNumberList readParticipantPhoneNumberList =
      bdmParticipant.listPhoneNumber(key);

  }

  @Test
  public void testreadPhoneNumber()
    throws AppException, InformationalException {

    final ReadParticipantPhoneNumberKey key =
      new ReadParticipantPhoneNumberKey();

    key.readConcernRolePhoneKey.concernRolePhoneNumberID = 234343;

    final ReadParticipantPhoneNumberDetails readParticipantPhoneNumberDetails =
      bdmParticipant.readPhoneNumber(key);
    assertTrue(key.readConcernRolePhoneKey.concernRolePhoneNumberID != 0);
  }

  @Test
  public void testCreatePhoneDetails()
    throws AppException, InformationalException {

    final BDMConcernRolePhoneDetails details =
      new BDMConcernRolePhoneDetails();

    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    details.concernRoleID = pdcPersonDetails.concernRoleID;
    details.phoneAreaCode = "123";
    details.phoneNumber = "4567890";
    details.phoneExtension = "123";
    details.phoneCountryCode = BDMPHONECOUNTRY.CANADA;
    details.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    details.startDate = curam.util.type.Date.getCurrentDate();

    final CreateParticipantPhoneDetails createParticipantPhoneDetails =
      bdmParticipant.createPhoneNumber(details);

  }

  private long createTestCommunication()
    throws AppException, InformationalException {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";

    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    final BDMCorrespondenceWizardKey wizardKey =
      new BDMCorrespondenceWizardKey();
    wizardKey.wizardStateID = wizId.wizardStateID;

    wizardKey.submitOpt = BDMCCTSUBMITOPT.VIEW;

    final BDMConcernRoleCommunication comm =
      BDMConcernRoleCommunicationFactory.newInstance();

    final BDMTemplateDetails bdmTemplateDetails =
      bdmCommunicationObj.createCorrespondence(wizardKey);

    final BDMCorrespondenceSearchKey key = new BDMCorrespondenceSearchKey();
    key.workItemID = bdmTemplateDetails.workItemID;
    final BDMConcernRoleCommunicationDtls bdmConcernRoleCommunicationDtls =
      comm.getCorrespondenceByWorkItemID(key);

    return bdmConcernRoleCommunicationDtls.communicationID;
  }

  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final BDMUtil bdmUtil = new BDMUtil();

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    bdmUtil.createContactPreferenceEvidence(
      registrationIDDetails.concernRoleID, BDMLANGUAGE.ENGLISHL,
      BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

}
