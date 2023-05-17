package curam.ca.gc.bdm.test.facade.bdmcasedisplay.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.facade.bdmcasedisplay.fact.BDMCaseDisplayFactory;
import curam.ca.gc.bdm.facade.bdmcasedisplay.intf.BDMCaseDisplay;
import curam.ca.gc.bdm.facade.bdmcasedisplay.struct.BDMSearchCaseDetails1;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.person.fact.BDMClientMergeFactory;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonMergeValidations;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.fec.fact.BDMMaintainForeignEngagementCaseFactory;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.DUPLICATEREASON;
import curam.codetable.PROVINCETYPE;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.MarkDuplicateCreateDetails;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.SearchCaseKey_fo;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BDMCaseDisplayTest extends CuramServerTestJUnit4 {

  BDMCaseDisplay bdmCaseDisplay = BDMCaseDisplayFactory.newInstance();

  /***
   * Test search method displays all cases associated with person marked as
   * duplicate and original person record
   */
  @Test
  public void testPersonSearchCase1()
    throws AppException, InformationalException {

    final SearchCaseKey_fo key = new SearchCaseKey_fo();

    // Create Person
    final PersonRegistrationResult originalPersonRegistrationResult =
      registerAPersonWithSIN("OriginalPerson", "296113012");
    // Create FEC for US
    final CreateIntegratedCaseResultAndMessages fecCase =
      createAFECCaseforCountry(
        originalPersonRegistrationResult.registrationIDDetails.concernRoleID,
        BDMSOURCECOUNTRY.US);

    // Create Dup0licate person
    final PersonRegistrationResult dupPersonRegistrationResult =
      registerAPersonWithSIN("DuplicatePerson", "683206866");

    // Create FEC for US
    final CreateIntegratedCaseResultAndMessages dupFecCase =
      createAFECCaseforCountry(
        dupPersonRegistrationResult.registrationIDDetails.concernRoleID,
        BDMSOURCECOUNTRY.US);

    final MarkDuplicateCreateDetails markDuplicateCreateDetails =
      new MarkDuplicateCreateDetails();
    markDuplicateCreateDetails.markDuplicateCreateDetails.originalConcernRoleID =
      originalPersonRegistrationResult.registrationIDDetails.concernRoleID;
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateConcernRoleID =
      dupPersonRegistrationResult.registrationIDDetails.concernRoleID;

    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateComments =
      "Merge Unit Test";
    markDuplicateCreateDetails.markDuplicateCreateDetails.duplicateReason =
      DUPLICATEREASON.RECREATERR;

    // Mark Person as duplicate
    final BDMPersonMergeValidations bdmPersonMergeValidations =
      BDMClientMergeFactory.newInstance()
        .markDuplicate(markDuplicateCreateDetails);
    assertTrue(bdmPersonMergeValidations.validationMsgDtls.dtls.isEmpty());

    key.casesByConcernRoleIDKey.concernRoleID =
      originalPersonRegistrationResult.registrationIDDetails.concernRoleID;

    // Search for cases
    final BDMSearchCaseDetails1 bdmSearchCaseDetails =
      bdmCaseDisplay.personSearchCase1(key);

    assertTrue(
      !bdmSearchCaseDetails.caseHeaderConcernRoleDetailsList.dtls.isEmpty());
    assertTrue(
      bdmSearchCaseDetails.caseHeaderConcernRoleDetailsList.dtls.size() == 2);

    assertEquals(dupFecCase.createCaseResult.integratedCaseID,
      bdmSearchCaseDetails.caseHeaderConcernRoleDetailsList.dtls
        .get(1).dtls.caseID);
    assertEquals(fecCase.createCaseResult.integratedCaseID,
      bdmSearchCaseDetails.caseHeaderConcernRoleDetailsList.dtls
        .get(0).dtls.caseID);

  }

  /**
   * Register person
   *
   *
   * @throws AppException
   * @throws InformationalException
   */
  private long registerPerson(final String name)
    throws AppException, InformationalException {

    final RegisterPerson registerPersonObj = new RegisterPerson("");
    final PersonDtls personDtls =
      registerPersonObj.registerDefault(name, METHODOFDELIVERYEntry.CHEQUE);
    return personDtls.concernRoleID;

  }

  /**
   * This is a utility method to create a FE Integrated Case for Test.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private CreateIntegratedCaseResultAndMessages
    createAFECCaseforCountry(final long concernRoleID, final String country)
      throws AppException, InformationalException {

    // BEGIN: create FE IC for the person
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = country;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      BDMMaintainForeignEngagementCaseFactory.newInstance()
        .createFEIntegratedCase(details);
    // END: create FE IC for the person

    return createIntegratedCaseResultAndMessages;
  }

  /**
   * This is the utility method to register person for the test class.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private PersonRegistrationResult
    registerAPersonWithSIN(final String firstName, final String nineDigitSIN)
      throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = firstName;
    bdmPersonRegistrationDetails.dtls.surname = "Doe";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770102");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = nineDigitSIN;
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

    final BDMUtil bdmUtil = new BDMUtil();
    bdmUtil.createContactPreferenceEvidence(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMLANGUAGE.ENGLISHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    return registrationResult;

  }

}
