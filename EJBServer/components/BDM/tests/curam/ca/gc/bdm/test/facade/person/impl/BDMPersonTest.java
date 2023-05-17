package curam.ca.gc.bdm.test.facade.person.impl;

import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.facade.person.intf.BDMPerson;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchDetailsResult;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchKey1;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.impl.EnvVars;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
@Ignore
public class BDMPersonTest extends CuramServerTestJUnit4 {

  @Before
  public void setUp() throws AppException, InformationalException {

  }

  /**
   * 8914 person search extension Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchPerson() throws AppException, InformationalException {

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "smith";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);

    assertTrue(
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() > 0);

  }

  /**
   * register and person search Ext Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchPersonExtRegister()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Maverick";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
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
    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "YES");
    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Mave";
    bdmPersonSearchKey1.postalCode = "K5M";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonExt(bdmPersonSearchKey1);

    assertTrue(
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() == 1);
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "Tom");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Maverick");

  }

  /**
   * 8914 register person and search Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchPersonForPopupExt()
    throws AppException, InformationalException {

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Tom";
    bdmPersonRegistrationDetails.dtls.surname = "Maverick";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
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
    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "YES");
    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    final BDMPersonSearchKey1 bdmPersonSearchKey1 = new BDMPersonSearchKey1();
    bdmPersonSearchKey1.dtls.personSearchKey.surname = "Mave";
    bdmPersonSearchKey1.postalCode = "K5M";

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();

    bdmPersonSearchDetailsResult =
      bdmPersonObj.searchPersonForPopupExt(bdmPersonSearchKey1);

    assertTrue(
      bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() == 1);
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).firstForename, "Tom");
    assertEquals(bdmPersonSearchDetailsResult.personSearchResult.dtlsList
      .get(0).lastName, "Maverick");

  }

}
