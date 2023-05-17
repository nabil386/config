package curam.ca.gc.bdm.facade.integritycheck.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.facade.externalparty.fact.BDMExternalPartyFactory;
import curam.ca.gc.bdm.facade.externalparty.intf.BDMExternalParty;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.sl.integritycheck.fact.BDMIntegrityCheckFactory;
import curam.ca.gc.bdm.sl.integritycheck.intf.BDMIntegrityCheck;
import curam.ca.gc.bdm.sl.integritycheck.struct.BDMIntegrityCheckKey;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)

public class BDMIntegrityCheckTest extends CuramServerTestJUnit4 {

  final BDMExternalParty bdmExtPartyObj =
    BDMExternalPartyFactory.newInstance();

  @Inject
  private EvidenceTypeDefDAO evidenceTypeDefDAO;

  @Inject
  private EvidenceTypeVersionDefDAO evidenceTypeVersionDefDAO;

  public BDMIntegrityCheckTest() {

    super();

  }

  @Test
  public void testsinIntegrityCheck()
    throws AppException, InformationalException {

    final BDMIntegrityCheckKey key = new BDMIntegrityCheckKey();

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.concernRoleID = person.registrationIDDetails.concernRoleID;
    key.caseID = caseID;

    final BDMIntegrityCheck check = BDMIntegrityCheckFactory.newInstance();
    check.sinIntegrityCheck(key);
    assertTrue(key.caseID != 0);

  }

  @Test
  public void testsinIntegrityCheckOnPerson()
    throws AppException, InformationalException {

    final BDMIntegrityCheckKey key = new BDMIntegrityCheckKey();

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    key.concernRoleID = person.registrationIDDetails.concernRoleID;
    key.caseID = caseID;

    final BDMIntegrityCheck check = BDMIntegrityCheckFactory.newInstance();
    check.sinIntegrityCheckOnPerson(key);
    assertTrue(key.caseID != 0);

  }

  protected CreateIntegratedCaseResultAndMessages createFEC(
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

  protected PersonRegistrationResult registerPerson()
    throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Homer";
    bdmPersonRegistrationDetails.dtls.surname = "Simpson";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "519132708";
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

    assertTrue(registrationResult.registrationIDDetails.concernRoleID != 0);

    return registrationResult;
  }

  public void mockUserObjects() throws AppException, InformationalException {

    new MockUp<TransactionInfo>() {

      @Mock
      public String getProgramUser()
        throws AppException, InformationalException {

        return "caseworker";

      }

    };

  }

}
