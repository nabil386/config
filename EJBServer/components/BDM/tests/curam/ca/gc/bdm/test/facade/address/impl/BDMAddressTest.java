package curam.ca.gc.bdm.test.facade.address.impl;

import curam.ca.gc.bdm.facade.address.fact.BDMAddressFactory;
import curam.ca.gc.bdm.facade.address.intf.BDMAddress;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressDetailsStruct;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchResult;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMRetrieveAddressKey;
import curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos.WsaddrSearchRequest;
import curam.ca.gc.bdm.test.data.BDMPersonTestData;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.fact.AddressFactory;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.InformationalMsgDtls;
import curam.core.struct.LocaleStruct;
import curam.core.struct.MaintainAddressKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.ReadMultiByConcRoleIDResult;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.StringList;
import jdk.nashorn.internal.ir.annotations.Ignore;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
@Ignore
public class BDMAddressTest extends CuramServerTestJUnit4 {

  public BDMAddressTest() {

    super();
  }

  @Before
  public void setUp() throws AppException, InformationalException {

  }

  private String _addressDataINTL = null;

  /**
   * Search address details extension Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testSearchAddress()
    throws AppException, InformationalException {

    final BDMAddressDetailsStruct details = new BDMAddressDetailsStruct();

    final BDMAddress bdmAddressObj = BDMAddressFactory.newInstance();
    BDMAddressSearchResult result = new BDMAddressSearchResult();

    final BDMPersonTestData bdmPersonDataObj = new BDMPersonTestData();

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

    details.addressData = otherAddressData.addressData;

    details.postCode = "M2N 1X9";

    result = bdmAddressObj.searchAddress(details);
    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      result.dtls.addRef(informationalMsgDtls);
    }

    assertTrue(result.searchAddresses.detailsList.size() > 0);

  }

  /**
   * Retrieves address details Ext Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testRetrieveAddress()
    throws AppException, InformationalException {

    final BDMRetrieveAddressKey key = new BDMRetrieveAddressKey();
    key.addressDetails = "Unit 234 200, Hollywood Road, LA";

    final BDMAddress bdmAddressObj = BDMAddressFactory.newInstance();

    BDMAddressDetailsStruct addressDetailsStruct =
      new BDMAddressDetailsStruct();
    addressDetailsStruct = bdmAddressObj.retrieveAddress(key);

    assertTrue(addressDetailsStruct.addressData.isEmpty());

  }

  /**
   * 8914 Retrieves address details and search Junit
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testgetCanadaPostAddressFormat()
    throws AppException, InformationalException {

    // final AddressKey addressKey = new AddressKey();
    // addressKey.addressID = 8322652111389073408L;

    final WsaddrSearchRequest requestDetails = new WsaddrSearchRequest();
    requestDetails.setNcAddressPostalCode("M2N7G7");
    requestDetails.setNcCountryCode(BDMConstants.kCountryCanada);
    requestDetails
      .setNcLanguageName(BDMConstants.kAddress_Search_Locale_en_CA);

    final LocaleStruct locale = new LocaleStruct();

    locale.localeIdentifier = TransactionInfo.getProgramLocale();
    final BDMAddress bdmAddressObj = BDMAddressFactory.newInstance();

    final AddressKey addressKey = new AddressKey();
    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final MaintainAddressKey addressKey1 = new MaintainAddressKey();
    addressKey1.concernRoleID = concernRoleID;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = pdcPersonDetails.concernRoleID;

    addressKey.addressID = ConcernRoleFactory.newInstance()
      .readConcernRole(concernRoleKey).primaryAddressID;
    _addressDataINTL =
      AddressFactory.newInstance().read(addressKey).addressData;

    final OtherAddressData otherAddData =
      bdmAddressObj.getCanadaPostAddressFormat(addressKey, locale);

    assertTrue(otherAddData.addressData.length() > 0);

  }

  @Test
  public void testReadmultiByConcernRoleID()
    throws AppException, InformationalException {

    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final MaintainAddressKey addressKey = new MaintainAddressKey();
    addressKey.concernRoleID = concernRoleID;

    ReadMultiByConcRoleIDResult readMultiByConcRoleIDResult =
      new ReadMultiByConcRoleIDResult();

    final BDMAddress bdmAddressObj = BDMAddressFactory.newInstance();

    readMultiByConcRoleIDResult =
      bdmAddressObj.readmultiByConcernRoleID(addressKey);

    assertTrue(readMultiByConcRoleIDResult.details.dtls.size() > 0);

  }

  private String getItemFromStringList(final StringList stringList,
    final int itemIndex) throws AppException, InformationalException {

    String stringItem = new String();
    if (stringList.size() > itemIndex) {
      stringItem = stringList.item(itemIndex);
    } else {
      stringItem = "";
    }
    return stringItem;
  }

}
