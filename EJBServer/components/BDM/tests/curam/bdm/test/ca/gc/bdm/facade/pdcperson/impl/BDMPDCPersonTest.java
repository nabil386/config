package curam.bdm.test.ca.gc.bdm.facade.pdcperson.impl;

import com.google.inject.Inject;
import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.codetable.BDMEDUCATIONLEVEL;
import curam.ca.gc.bdm.codetable.BDMIEGYESNOOPTIONAL;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.intf.BDMPerson;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonDtls;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonKey;
import curam.ca.gc.bdm.facade.address.fact.BDMAddressEvidenceWizardFactory;
import curam.ca.gc.bdm.facade.address.intf.BDMAddressEvidenceWizard;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceWizardDetails;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceWizardKey;
import curam.ca.gc.bdm.facade.address.struct.AddressEvidenceWizardResult;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.pdcperson.fact.BDMPDCPersonFactory;
import curam.ca.gc.bdm.facade.pdcperson.intf.BDMPDCPerson;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMConcernRoleIDAndWizardStateIDKey;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMPDCEvidenceDetailsList;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonDemographicPageDetails;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMReadPersonDemographicDetails;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchDetailsResult;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchKey1;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.ws.sinvalidation.impl.SINValidationMod10;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.IEGYESNO;
import curam.codetable.PROVINCETYPE;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.PersonRegistration;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.RegistrationIDDetails;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.pdc.facade.struct.ReadPersonKey;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.UniqueID;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Task-17494 - Junit class to manage Person demographics details
 *
 * @author rajat.soni
 *
 */
@RunWith(JMockit.class)
public class BDMPDCPersonTest extends CuramServerTestJUnit4 {

  private long concernRoleID = 0;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Before
  public void setUp() throws AppException, InformationalException {

    final RegisterPerson registerPersonObj = new RegisterPerson("");
    final PersonDtls personDtls = registerPersonObj
      .registerDefault("Demo Test", METHODOFDELIVERYEntry.CHEQUE);
    this.concernRoleID = personDtls.concernRoleID;

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = this.concernRoleID;
    BDMPersonDtls bdmPersonDtls = new BDMPersonDtls();

    bdmPersonDtls = new BDMPersonDtls();
    bdmPersonDtls.concernRoleID = bdmPersonKey.concernRoleID;
    bdmPersonDtls.educationLevel = BDMEDUCATIONLEVEL.COLLEGE;
    bdmPersonDtls.indigenousPersonType = BDMIEGYESNOOPTIONAL.YES;
    bdmPersonDtls.memberMinorityGrpType = BDMIEGYESNOOPTIONAL.NO;

    bdmPersonObj.insert(bdmPersonDtls);

  }

  /**
   * Junit method for readBDMNonPDCDemographicData()
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testReadBDMNonPDCDemographicData()
    throws AppException, InformationalException {

    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = this.concernRoleID;
    BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();
    bdmReadPersonDemographicDetails =
      bdmPDCPersonObj.readBDMNonPDCDemographicData(personKey);

    assertEquals(bdmReadPersonDemographicDetails.educationLevel,
      BDMEDUCATIONLEVEL.COLLEGE);
    assertEquals(bdmReadPersonDemographicDetails.indigenousPersonType,
      BDMIEGYESNOOPTIONAL.YES);
    assertEquals(bdmReadPersonDemographicDetails.memberMinorityGrpType,
      BDMIEGYESNOOPTIONAL.NO);
  }

  /**
   * Junit test method for modifyBDMNonPDCDemographicData()
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testModifyBDMNonPDCDemographicData()
    throws AppException, InformationalException {

    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();

    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = this.concernRoleID;
    BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();
    bdmReadPersonDemographicDetails =
      bdmPDCPersonObj.readBDMNonPDCDemographicData(personKey);

    final BDMPersonDemographicPageDetails bdmPersonDemographicPageDetails =
      new BDMPersonDemographicPageDetails();

    bdmPersonDemographicPageDetails.assign(bdmReadPersonDemographicDetails);

    bdmPersonDemographicPageDetails.comments = "Performing update";
    bdmPersonDemographicPageDetails.educationLevel =
      BDMEDUCATIONLEVEL.UNIVERSITY;

    bdmPDCPersonObj
      .modifyBDMNonPDCDemographicData(bdmPersonDemographicPageDetails);

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = this.concernRoleID;
    final BDMPersonDtls bdmPersonDtls = bdmPersonObj.read(bdmPersonKey);

    assertEquals(bdmPersonDtls.educationLevel, BDMEDUCATIONLEVEL.UNIVERSITY);
    assertEquals(bdmPersonDtls.indigenousPersonType, BDMIEGYESNOOPTIONAL.YES);
    assertEquals(bdmPersonDtls.memberMinorityGrpType, BDMIEGYESNOOPTIONAL.NO);

  }

  /**
   * Task 59306: DEV; Implement Mask SIN- Evidence List page
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Ignore
  @Test
  public void testListEvidenceForParticipant()
    throws AppException, InformationalException {

    final BDMConcernRoleIDAndWizardStateIDKey key =
      new BDMConcernRoleIDAndWizardStateIDKey();

    key.concernRoleID = this.concernRoleID;
    key.wizardStateID = UniqueID.nextUniqueID();
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";
    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "HAMED";
    bdmPersonRegistrationDetails.dtls.surname = "MOHAMMED";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "472723303";
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "207";
    addressFieldDetails.addressLine1 = "6511";
    addressFieldDetails.addressLine2 = "GILBER RD";
    addressFieldDetails.province = PROVINCETYPE.BRITISHCOLUMBIA;
    addressFieldDetails.city = "RICHMOND";
    addressFieldDetails.postalCode = "V7C 3V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "MOHAMMED";
    bdmPersonSearchKey1.postalCode = "V7C";

    final BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final PDCEvidenceDetailsList pdcEvidenceDetailsList =
      new PDCEvidenceDetailsList();

    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID =
      registrationResult.registrationIDDetails.concernRoleID;

    final BDMPDCPerson bdmPdcPersonObj = BDMPDCPersonFactory.newInstance();

    // BEGIN: Task 60485: DEV: Implement- Informational message - Needed to
    // update the Junit Test, since I have to change the input and return
    // struct.
    BDMPDCEvidenceDetailsList bdmpdcEvidenceDetailsList =
      new BDMPDCEvidenceDetailsList();
    final BDMConcernRoleIDAndWizardStateIDKey bdmConcernRoleIDAndWizardStateIDKey =
      new BDMConcernRoleIDAndWizardStateIDKey();

    bdmConcernRoleIDAndWizardStateIDKey.concernRoleID =
      registrationResult.registrationIDDetails.concernRoleID;
    //
    // bdmpdcEvidenceDetailsList = bdmPdcPersonObj
    // .listEvidenceForParticipant(bdmConcernRoleIDAndWizardStateIDKey);

    final int listSize = bdmpdcEvidenceDetailsList.pdcEvdnDetails.list.size();

    assertTrue(listSize == 6);

    // BEGIN: Task 60485: DEV: Implement- Informational message
    final BDMAddressEvidenceWizard bdmAddressEvidenceWizard =
      BDMAddressEvidenceWizardFactory.newInstance();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = 0;

    final ActionIDProperty actionID = new ActionIDProperty();
    actionID.actionIDProperty = "SAVENCLOSE";

    final AddressEvidenceWizardDetails wizardDtls =
      new AddressEvidenceWizardDetails();

    AddressEvidenceWizardResult addressEvidenceWizardResult =
      new AddressEvidenceWizardResult();

    final AddressEvidenceWizardKey wizardStateKey =
      new AddressEvidenceWizardKey();
    // END: Task 60485: DEV: Implement- Informational message

    for (int i = 0; i < listSize; i++) {

      if (bdmpdcEvidenceDetailsList.pdcEvdnDetails.list.get(i).evidenceType
        .equals(CASEEVIDENCE.IDENTIFICATIONS)
        && bdmpdcEvidenceDetailsList.pdcEvdnDetails.list.get(i).description
          .indexOf("Social Insurance Number") != -1) {

        assertTrue(
          bdmpdcEvidenceDetailsList.pdcEvdnDetails.list.get(i).description
            .contains("Social Insurance Number"));

        assertTrue(
          bdmpdcEvidenceDetailsList.pdcEvdnDetails.list.get(i).description
            .contains("*"));

        assertTrue(
          bdmpdcEvidenceDetailsList.pdcEvdnDetails.list.get(i).description
            .contains("3303"));

      }
    }
    // END: Task 60485: DEV: Implement- Informational message - Needed to
    // update
    // the Junit Test, since I have to change the input and return struct.

    // BEGIN: Task 60485: DEV: Implement- Informational message
    for (int i = 0; i < listSize; i++) {
      if (bdmpdcEvidenceDetailsList.pdcEvdnDetails.list.get(i).evidenceType
        .equals(CASEEVIDENCE.BDMADDRESS)
        && bdmpdcEvidenceDetailsList.pdcEvdnDetails.list.get(i).description
          .indexOf("Residential address") != -1) {

        wizardStateKey.evidenceID =
          bdmpdcEvidenceDetailsList.pdcEvdnDetails.list.get(i).evidenceID;

        addressEvidenceWizardResult = bdmAddressEvidenceWizard
          .getAddressEvidenceWizardDetails(wizardStateKey);

        wizardDtls.evidenceID =
          bdmpdcEvidenceDetailsList.pdcEvdnDetails.list.get(i).evidenceID;
        wizardDtls.step1DataDtls.addressType = CONCERNROLEADDRESSTYPE.PRIVATE;
        wizardDtls.step1DataDtls.receiveDate =
          addressEvidenceWizardResult.dtls.step1DataDtls.receiveDate;
        wizardDtls.step1DataDtls.fromDate =
          addressEvidenceWizardResult.dtls.step1DataDtls.fromDate;
        wizardDtls.step1DataDtls.toDate = Date.getCurrentDate().addDays(20);
        wizardDtls.step1DataDtls.editInd = IEGYESNO.NO;
        wizardDtls.currentStepNum = 1;

        wizardStateID.wizardStateID =
          addressEvidenceWizardResult.wizardKey.wizardStateID;

        bdmAddressEvidenceWizard.setAddressEvidenceWizardDetails(
          wizardStateID, actionID, wizardDtls);

      }

    }

    bdmpdcEvidenceDetailsList = bdmPdcPersonObj
      .listEvidenceForParticipant(bdmConcernRoleIDAndWizardStateIDKey);

    assertTrue(bdmpdcEvidenceDetailsList.msgDtls.dtls.size() == 1);
    assertTrue(
      bdmpdcEvidenceDetailsList.msgDtls.dtls.item(0).message.contains(
        "ERR_CURRRENT_RESIDENTIAL_ADDR_EMPTY_ADD_NEW_RESIDENTIAL_ADDRESS"));

    // END: Task 60485: DEV: Implement- Informational message

  }

  /**
   * Junit method for readBDMNonPDCDemographicData()
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testreadPersonIdentificationDetails()
    throws AppException, InformationalException {

    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();
    final curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonKey personKey =
      new curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonKey();
    // final PersonKey personKey = new PersonKey();
    personKey.dtls.concernRoleID = this.concernRoleID;

    final BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();
    curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails identificationDetails =
      new curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails();
    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.APRIL, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);

    identificationDetails =
      bdmPDCPersonObj.readPersonIdentificationDetails(personKey);

    // assertEquals(identificationDetails.dtls.attestationDate,
    // Date.getCurrentDate());
    /*
     * assertEquals(identificationDetails.dtls.attestationAgree,
     * BDMAGREEATTESTATION.YES);
     */
    identificationDetails.dtls.firstForename = "John";
    identificationDetails.dtls.lastName = "Don";
    identificationDetails.dtls.dateOfBirth = birthday;
    identificationDetails.dtls.dobEvidenceID = 121l;
    identificationDetails.dtls.nameEvidenceID = 123l;
    identificationDetails.dtls.concernRoleID = this.concernRoleID;
    // assertEquals(identificationDetails.dtls.firstForename, "John");
    // assertEquals(identificationDetails.dtls.lastName, "Don");
    // assertEquals(identificationDetails.dtls.dateOfBirth, birthday);
    // assertEquals(identificationDetails.dtls.concernRoleName, "CRA");
    // assertEquals(identificationDetails.dtls.concernRoleID,
    // this.concernRoleID);
    // assertEquals(identificationDetails.dtls.dobEvidenceID, "121l");
    // assertEquals(identificationDetails.dtls.existingSINNumber, "121l");

    assertTrue(identificationDetails.dtls.concernRoleID != 0);
  }

  /**
   * Junit test method for modifyBDMNonPDCDemographicData()
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testchangeIdentificationInfoDetails()
    throws AppException, InformationalException {

    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();

    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = this.concernRoleID;
    final BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();
    final curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails identificationDetails =
      new curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails();

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.APRIL, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);
    // Set up
    final String validSIN = "123456789";
    final String invalidSin = "987654321";
    final String noSIN = "";
    final String nullSIN = null;

    identificationDetails.dtls.concernRoleID = this.concernRoleID;
    identificationDetails.dtls.sinNumber = "714600475";
    identificationDetails.dtls.nameEvidenceID = 123l;
    identificationDetails.dtls.firstForename = " John";
    identificationDetails.dtls.lastName = "Deo";
    identificationDetails.dtls.existingSINNumber = validSIN;
    identificationDetails.dtls.dateOfBirth = birthday;
    identificationDetails.dtls.sinIdentificationStartDate =
      Date.getCurrentDate();

    final BDMPersonDemographicPageDetails bdmPersonDemographicPageDetails =
      new BDMPersonDemographicPageDetails();

    bdmPersonDemographicPageDetails.assign(bdmReadPersonDemographicDetails);

    bdmPersonDemographicPageDetails.comments = "Performing update";
    bdmPersonDemographicPageDetails.concernRoleID = this.concernRoleID;

    bdmPersonDemographicPageDetails.educationLevel =
      BDMEDUCATIONLEVEL.UNIVERSITY;

    bdmPDCPersonObj.changeIdentificationInfoDetails(identificationDetails);

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = this.concernRoleID;
    // final BDMPersonDtls bdmPersonDtls = bdmPersonObj.read(bdmPersonKey);

  }

  public String generateValidSIN() {

    String sinNumber = "";

    do {
      final long nanoTime = System.nanoTime();

      final double randMathNum = Math.random() * 1000;

      final long randomNumer = (long) (nanoTime * randMathNum);

      final String s = randomNumer + "";
      sinNumber = s.substring(0, 9);

    } while (!SINValidationMod10.isValidMOD10(sinNumber));

    return sinNumber;
  }

  @Test
  public void testreadHomePageDetailsForModify()
    throws AppException, InformationalException {

    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();

    final ReadPersonKey personKey = new ReadPersonKey();
    personKey.dtls.concernRoleID = this.concernRoleID;
    final BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();
    final ReadPersonKey identificationDetails = new ReadPersonKey();
    identificationDetails.dtls.concernRoleID = this.concernRoleID;
    final BDMPersonDemographicPageDetails bdmPersonDemographicPageDetails =
      new BDMPersonDemographicPageDetails();

    bdmPersonDemographicPageDetails.assign(bdmReadPersonDemographicDetails);

    bdmPersonDemographicPageDetails.comments = "Performing update";
    bdmPersonDemographicPageDetails.educationLevel =
      BDMEDUCATIONLEVEL.UNIVERSITY;

    bdmPDCPersonObj.readHomePageDetailsForModify(identificationDetails);

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = this.concernRoleID;
    // final BDMPersonDtls bdmPersonDtls = bdmPersonObj.read(bdmPersonKey);

  }

  /**
   * Creates a Person
   *
   * @return person details.
   * @throws AppException
   * @throws InformationalException
   */
  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");
    final curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  @Test
  public void testlistCurrentCases()
    throws AppException, InformationalException {

    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();

    final ReadPersonKey personKey = new ReadPersonKey();
    personKey.dtls.concernRoleID = this.concernRoleID;
    final BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();
    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = this.concernRoleID;

    final BDMPersonDemographicPageDetails bdmPersonDemographicPageDetails =
      new BDMPersonDemographicPageDetails();
    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
    bdmfeCaseKey.caseID = 0;
    bdmPersonDemographicPageDetails.assign(bdmReadPersonDemographicDetails);

    bdmPersonDemographicPageDetails.comments = "Performing update";
    bdmPersonDemographicPageDetails.educationLevel =
      BDMEDUCATIONLEVEL.UNIVERSITY;

    bdmPDCPersonObj.listCurrentCases(key);

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
    final BDMPersonKey bdmPersonKey = new BDMPersonKey();
    bdmPersonKey.concernRoleID = this.concernRoleID;
    // final BDMPersonDtls bdmPersonDtls = bdmPersonObj.read(bdmPersonKey);

  }

  /**
   * Method checks for the Prospect Person
   *
   * @param concernRoleID
   * @return
   */
  private boolean isProspectPerson(final long concernRoleID) {

    return concernRoleDAO.get(concernRoleID).getConcernRoleType().getCode()
      .equals(CONCERNROLETYPE.PROSPECTPERSON) ? true : false;
  }

  /**
   * Test the changeIdentificationInfoDetails Validation Message
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testchangeIdentificationInfoDetailsValidation()
    throws AppException, InformationalException {

    try {

      final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();

      final PersonKey personKey = new PersonKey();
      personKey.concernRoleID = this.concernRoleID;
      final BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
        new BDMReadPersonDemographicDetails();
      final curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails identificationDetails =
        new curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails();

      final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
        java.util.Calendar.APRIL, 1, 0, 0, 0);
      final curam.util.type.Date birthday = new curam.util.type.Date(cal);

      identificationDetails.dtls.concernRoleID = this.concernRoleID;
      identificationDetails.dtls.sinNumber = "714600475";
      identificationDetails.dtls.nameEvidenceID = 123l;
      identificationDetails.dtls.firstForename = " John";
      identificationDetails.dtls.lastName = "Deo";
      identificationDetails.dtls.dateOfBirth = birthday;
      identificationDetails.dtls.sinIdentificationStartDate =
        Date.getCurrentDate().addDays(-1);

      identificationDetails.dtls.sinIdentificationExistingStartDate =
        Date.getCurrentDate();
      identificationDetails.dtls.existingSINNumber = "590001517";

      final BDMPersonDemographicPageDetails bdmPersonDemographicPageDetails =
        new BDMPersonDemographicPageDetails();

      bdmPersonDemographicPageDetails.assign(bdmReadPersonDemographicDetails);

      bdmPersonDemographicPageDetails.comments = "Performing update";
      bdmPersonDemographicPageDetails.concernRoleID = this.concernRoleID;

      bdmPersonDemographicPageDetails.educationLevel =
        BDMEDUCATIONLEVEL.UNIVERSITY;

      bdmPDCPersonObj.changeIdentificationInfoDetails(identificationDetails);
    } catch (final Exception e) {

      final String expectedMessage =
        "Start Date of the New SIN must be after the End Date of the prior SIN";
      assertTrue(
        expectedMessage.equals(TransactionInfo.getInformationalManager()
          .obtainInformationalAsException().getMessage()));

    }

  }

}
