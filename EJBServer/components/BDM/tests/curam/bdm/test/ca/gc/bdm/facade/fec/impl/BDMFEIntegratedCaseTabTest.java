package curam.bdm.test.ca.gc.bdm.facade.fec.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.facade.fec.fact.BDMFEIntegratedCaseTabFactory;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.intf.BDMFEIntegratedCaseTab;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.impl.CuramConst;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.IntegratedCaseTabDetail;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BDMFEIntegratedCaseTabTest extends CuramServerTestJUnit4 {

  BDMFEIntegratedCaseTab bdmFEIntegratedCaseTab = null;

  @Override
  protected boolean shouldCommit() {

    return false;
  }

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmFEIntegratedCaseTab = BDMFEIntegratedCaseTabFactory.newInstance();
  }

  @Test
  public void testReadFEIntegratedCaseTabDetail()
    throws AppException, InformationalException {

    final PersonRegistrationResult personRegistrationResult =
      registerAPersonForTest("451775399");
    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      createFEC(personRegistrationResult.registrationIDDetails.concernRoleID);

    CaseIDKey key = new CaseIDKey();
    key.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    IntegratedCaseTabDetail integratedCaseTabDetail =
      bdmFEIntegratedCaseTab.readFEIntegratedCaseTabDetail(key);
    assertEquals(CuramConst.kSeparator + "Ireland",
      integratedCaseTabDetail.conditionalFormedPersonNameOpt);
    key = new CaseIDKey();
    key.caseID = 1;

    try {
      integratedCaseTabDetail = new IntegratedCaseTabDetail();
      integratedCaseTabDetail =
        bdmFEIntegratedCaseTab.readFEIntegratedCaseTabDetail(key);
    } catch (final Exception e) {

      assertEquals(CuramConst.gkEmpty,
        integratedCaseTabDetail.conditionalFormedPersonNameOpt);

    }
  }

  private CreateIntegratedCaseResultAndMessages createFEC(
    final long concernRoleID) throws AppException, InformationalException {

    // BEGIN: create FE IC for the person
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = BDMSOURCECOUNTRY.IRELAND;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages = BDMForeignEngagementCaseFactory
      .newInstance().createFEIntegratedCase(details);
    // END: create FE IC for the person

    return createIntegratedCaseResultAndMessages;
  }

  /**
   * This is the utility method to register person for the test class.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private PersonRegistrationResult registerAPersonForTest(
    final String nineDigitSIN) throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "John";
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
