package curam.ca.gc.bdm.test.creole.impl;

import curam.ca.gc.bdm.creole.impl.BDMVerificationStatics;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRY;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.EnvVars;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.creole.value.CodeTableItem;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.configuration.base.MethodRef;

/**
 * JUnit for BDMVerificationStatics.
 *
 *
 */
@RunWith(JMockit.class)
public class BDMVerificationStaticsTest extends CuramServerTestJUnit4 {

  @MethodRef(name = "getBenefitPerAddressLimit",
    signature = "(QSession;)QNumber;")
  @Test
  public void testGetBenefitPerAddressLimit() throws Exception {

    // default test
    final Number result =
      BDMVerificationStatics.getBenefitPerAddressLimit(null);
    Assert.assertEquals(
      Configuration.getIntProperty(EnvVars.BDM_ENV_BENEFITS_PER_ADDRESS),
      result);
  }

  @MethodRef(name = "getIndividualPerAddressLimit",
    signature = "(QSession;)QNumber;")
  @Test
  public void testGetIndividualPerAddressLimit() throws Exception {

    // default test
    final Number result =
      BDMVerificationStatics.getIndividualPerAddressLimit(null);

    Assert.assertEquals(
      Configuration.getIntProperty(EnvVars.BDM_ENV_INDIVIDUALS_PER_ADDRESS),
      result);
  }

  @MethodRef(name = "getIndividualPerAddressUseCount",
    signature = "(QSession;QString;)QNumber;")
  @Test
  public void testGetIndividualPerAddressUseCount() throws Exception {

    // default test
    final Number result = BDMVerificationStatics
      .getIndividualPerAddressUseCount(null, getAddressDataINTL(),
        new CodeTableItem(CONCERNROLEADDRESSTYPE.TABLENAME, "AT1"));

    Assert.assertTrue(result.intValue() >= 1);

  }

  private String _addressDataINTL = null;

  /**
   * To prepare test data.
   *
   * @return
   * @throws Exception
   */
  private String getAddressDataINTL() throws Exception {

    if (_addressDataINTL == null) {

      // set up address
      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      final curam.core.intf.AddressData addressDataObj =
        curam.core.fact.AddressDataFactory.newInstance();

      // must be INTL layout
      addressFieldDetails.addressLayoutType =
        curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
      addressFieldDetails.suiteNum = "suite";
      addressFieldDetails.addressLine1 = "add1";
      addressFieldDetails.addressLine2 = "add2";
      addressFieldDetails.province = "ON";
      addressFieldDetails.city = "city";
      addressFieldDetails.postalCode = "A9A 9A9";
      addressFieldDetails.countryCode = COUNTRY.CA;

      final OtherAddressData otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      // register person
      final RegisterPerson registerPersonObj = new RegisterPerson(getName());

      final PersonRegistrationDetails personRegistrationDetails =
        registerPersonObj.getPersonRegistrationDetails();

      personRegistrationDetails.addressData = otherAddressData.addressData;
      personRegistrationDetails.mailingAddressData =
        otherAddressData.addressData;

      final RegistrationIDDetails registrationIDDetails =
        PersonRegistrationFactory.newInstance()
          .registerPerson(personRegistrationDetails);

      // insert case header
      final CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();
      caseHeaderDtls.appealIndicator = false;
      caseHeaderDtls.caseReference = "caseReference";
      caseHeaderDtls.versionNo = 1;
      caseHeaderDtls.concernRoleID = registrationIDDetails.concernRoleID;
      caseHeaderDtls.startDate = Date.getCurrentDate();
      caseHeaderDtls.caseTypeCode = CASETYPECODE.PRODUCTDELIVERY;
      caseHeaderDtls.statusCode = CASESTATUS.ACTIVE;

      caseHeaderDtls.caseID = UniqueIDFactory.newInstance().getNextID();

      CaseHeaderFactory.newInstance().insert(caseHeaderDtls);

      // read address data
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID = registrationIDDetails.concernRoleID;

      final AddressKey addressKey = new AddressKey();
      addressKey.addressID = ConcernRoleFactory.newInstance()
        .readConcernRole(concernRoleKey).primaryAddressID;
      _addressDataINTL =
        AddressFactory.newInstance().read(addressKey).addressData;

    }

    return _addressDataINTL;

  }

}
