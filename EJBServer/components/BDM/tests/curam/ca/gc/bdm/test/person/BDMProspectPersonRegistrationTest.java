package curam.ca.gc.bdm.test.person;

import curam.ca.gc.bdm.facade.participant.fact.BDMParticipantRegistrationDetailsFactory;
import curam.ca.gc.bdm.facade.participant.intf.BDMParticipantRegistrationDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMProspectPersonRegistrationDetails;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.AddProspectPersonState;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.facade.struct.PersonSearchWizardKey;
import curam.core.facade.struct.RegisterPersonWizardSearchDetails;
import curam.core.impl.CuramConst;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.execution.session.StronglyTypedRuleObjectFactory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.wizardpersistence.impl.WizardPersistentState;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class BDMProspectPersonRegistrationTest extends CuramServerTestJUnit4 {

  public BDMProspectPersonRegistrationTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  Session session;

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
  }

  /**
   * create prospect Person registration
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testProspectRegistration()
    throws AppException, InformationalException {

    final long concernroleID = createPDCProspectPerson();

    assertTrue(concernroleID != 0);

  }

  @Test
  public void testProspectSearch()
    throws AppException, InformationalException {

    final long concernroleID = createPDCProspectPerson();

    final BDMParticipantRegistrationDetails participantRegistrationDetails =
      BDMParticipantRegistrationDetailsFactory.newInstance();

    final PersonSearchWizardKey searchKey = new PersonSearchWizardKey();

    final WizardStateID stateID = new WizardStateID();
    final ActionIDProperty actionID = new ActionIDProperty();
    actionID.actionIDProperty = CuramConst.kSearchAction;

    searchKey.searchKey.forename = "Test";
    searchKey.searchKey.surname = "Test";

    final RegisterPersonWizardSearchDetails registerPersonWizardSearchDetails =
      participantRegistrationDetails
        .setAddProspectPersonSearchCriteriaDetails(searchKey, stateID,
          actionID);

    assertTrue(
      !registerPersonWizardSearchDetails.searchResult.personSearchResult.dtlsList
        .isEmpty());

  }

  public long createPDCProspectPerson()
    throws AppException, InformationalException {

    final BDMParticipantRegistrationDetails participantRegistrationDetails =
      BDMParticipantRegistrationDetailsFactory.newInstance();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    final BDMProspectPersonRegistrationDetails bdmPersonReg =
      new BDMProspectPersonRegistrationDetails();
    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID =
      wizardPersistentState.create(new AddProspectPersonState());
    final ActionIDProperty actionID = new ActionIDProperty();
    actionID.actionIDProperty = CuramConst.kSaveAction;

    bdmPersonReg.dtls.prospectPersonRegistrationDtls.firstForename = "test";
    bdmPersonReg.dtls.prospectPersonRegistrationDtls.surname = "test";
    bdmPersonReg.dtls.prospectPersonRegistrationDtls.registrationDate =
      TransactionInfo.getSystemDate();

    bdmPersonReg.dtls.prospectPersonRegistrationDtls.addressType =
      CONCERNROLEADDRESSTYPE.PRIVATE.toString();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    // BEGIN, CR00272990 , KRK
    addressFieldDetails.suiteNum = "4994";
    addressFieldDetails.addressLine1 = "Heatherleigh";
    addressFieldDetails.addressLine2 = "Cooksville";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    // BEGIN, CR00380472, MV
    addressFieldDetails.postalCode = "L5A 1V9";
    // END, CR00380472
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    addressFieldDetails.zipCode = "12345";
    // addressFieldDetails.stateProvince = "AK";
    // END, CR00272990
    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonReg.dtls.prospectPersonRegistrationDtls.addressData =
      otherAddressData.addressData;

    bdmPersonReg.dtls.prospectPersonRegistrationDtls.mailingAddressData =
      otherAddressData.addressData;

    final ParticipantRegistrationWizardResult participantRegistrationWizardResult =
      participantRegistrationDetails.setAddProspectPersonDetails(bdmPersonReg,
        wizardStateID, actionID);

    return participantRegistrationWizardResult.registrationResult.concernRoleID;

  }

}
