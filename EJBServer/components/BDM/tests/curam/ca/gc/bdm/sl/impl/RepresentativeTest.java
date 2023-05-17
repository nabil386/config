/**
 *
 */
package curam.ca.gc.bdm.sl.impl;

import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchResult;
import curam.ca.gc.bdm.sl.fact.RepresentativeFactory;
import curam.ca.gc.bdm.sl.intf.Representative;
import curam.ca.gc.bdm.sl.struct.BDMThirdPartyParticipantSearchKey;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.ca.gc.bdm.test.data.BDMPersonTestData;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.ConcernRoleFactory;
import curam.core.sl.struct.RepresentativeRegistrationDetails;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;

/**
 * JUnit test for
 * <code>curam.ca.gc.bdm.sl.impl.BDMThirdPartyParticipantSearchProcess</code>.
 *
 * @author donghua.jin
 *
 */
public class RepresentativeTest extends BDMBaseTest {

  /**
   * Test search by reference number.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testRegisterRepresentative()
    throws AppException, InformationalException {

    final PersonRegistrationResult regPersonDetails = this.registerPerson();

    final ConcernRoleKey crKey = new ConcernRoleKey();

    crKey.concernRoleID =
      regPersonDetails.registrationIDDetails.concernRoleID;

    final Representative repObj = RepresentativeFactory.newInstance();

    final ConcernRoleDtls crDtls =
      ConcernRoleFactory.newInstance().read(crKey);

    final BDMThirdPartyParticipantSearchKey key =
      new BDMThirdPartyParticipantSearchKey();

    key.referenceNumber = crDtls.primaryAlternateID;

    final RepresentativeRegistrationDetails details =
      new RepresentativeRegistrationDetails();
    details.representativeDtls.concernRoleID =
      regPersonDetails.registrationIDDetails.concernRoleID;
    details.representativeDtls.representativeName = "John";
    details.representativeDtls.upperRepresentativeName = "JOHN";
    details.representativeDtls.paymentFrequency = "100100100";
    details.representativeDtls.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
    details.representativeDtls.representativeType =
      curam.codetable.REPRESENTATIVETYPE.CONTACT;

    final BDMAddressSearchResult result = new BDMAddressSearchResult();

    final BDMPersonTestData bdmPersonDataObj = new BDMPersonTestData();

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

    details.representativeRegistrationDetails.addressData =
      otherAddressData.addressData;
    details.representativeRegistrationDetails.phoneAreaCode = "123";
    details.representativeRegistrationDetails.phoneCountryCode = "1";

    details.representativeRegistrationDetails.phoneNumber = "2323232";
    details.representativeRegistrationDetails.phoneExtension = "123";
    details.representativeRegistrationDetails.registrationDate =
      curam.util.type.Date.getCurrentDate();
    details.representativeRegistrationDetails.startDate =
      curam.util.type.Date.getCurrentDate();
    repObj.registerRepresentative(details);

    // assertEquals(1, searchResults.dtlsList.size());
  }

}
