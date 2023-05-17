package curam.ca.gc.bdm.test.interfaces.sl.wsaddress;

import curam.ca.gc.bdm.facade.address.impl.BDMAddress;
import curam.ca.gc.bdm.message.BDMBPOADDRESSVALIDATOR;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressDetailsStruct;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchResult;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import mockit.Expectations;
import mockit.Tested;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for class BDMAddress
 */
public class BDMAddressSearchTest extends CuramServerTestJUnit4 {

  private static final boolean WS_SERVICE_ENABLED = true;

  /** The address API. */
  @Tested
  BDMAddress bdmAddress;

  /**
   * Instantiates a new BDM address test.
   */
  public BDMAddressSearchTest() {

    super();
  }

  /**
   * Before.
   */
  @Before
  public void before() {

    bdmAddress = new BDMAddress();
  }

  /**
   * After each test.
   */
  @After
  public void after() {

    Configuration.setProperty(
      EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT, CuramConst.gkTrue);
  }

  /**
   * Test search address with no post code
   *
   * @throws InformationalException the informational exception
   */
  // @Test
  @Ignore
  public void test_searchAddress_noPostCode()
    throws InformationalException, AppException {

    new Expectations(Configuration.class) {

      {
        Configuration.getBooleanProperty(
          EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT);
        result = WS_SERVICE_ENABLED;
      }
    };
    // Postal code not present
    final BDMAddressDetailsStruct addressDetailsStruct =
      new BDMAddressDetailsStruct();
    bdmAddress.searchAddress(addressDetailsStruct);

    InformationalException searchInformationalException = null;
    try {
      TransactionInfo.getInformationalManager().failOperation();
    } catch (final InformationalException informationalException) {
      searchInformationalException = informationalException;
    }

    // Assert
    assertEquals(
      new LocalisableString(
        BDMBPOADDRESSVALIDATOR.INF_NO_SEARCH_ADDRESS_FOUND),
      searchInformationalException.getLocalisedException());

  }

  /**
   * Test search address with no post code
   *
   * @throws InformationalException the informational exception
   */
  @Ignore
  public void test_searchAddress_wrongPostCode()
    throws InformationalException, AppException {

    new Expectations(Configuration.class) {

      {
        Configuration.getBooleanProperty(
          EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT);
        result = WS_SERVICE_ENABLED;
      }
    };

    final BDMAddressDetailsStruct addressDetailsStruct =
      new BDMAddressDetailsStruct();
    // Postal code present -
    addressDetailsStruct.postCode = "XXXXXX";
    bdmAddress.searchAddress(addressDetailsStruct);

    InformationalException searchInformationalException = null;
    try {
      TransactionInfo.getInformationalManager().failOperation();
    } catch (final InformationalException informationalException) {
      searchInformationalException = informationalException;
    }

    // Assert
    assertEquals(
      new LocalisableString(
        BDMBPOADDRESSVALIDATOR.INF_NO_SEARCH_ADDRESS_FOUND),
      searchInformationalException.getLocalisedException());

  }

  /**
   * Test search address where the post code includes an Urban Address post
   * code.
   *
   */
  @Ignore
  public void test_searchAddress_urbanAddressTypeFormat()
    throws InformationalException, AppException {

    new Expectations(Configuration.class) {

      {
        Configuration.getBooleanProperty(
          EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT);
        result = WS_SERVICE_ENABLED;
      }
    };

    // Postal code not present
    final BDMAddressDetailsStruct addressDetailsStruct =
      new BDMAddressDetailsStruct();

    // Postal code present
    addressDetailsStruct.postCode = "A1C5B9";
    final BDMAddressSearchResult searchResults =
      bdmAddress.searchAddress(addressDetailsStruct);

    // Assert
    assertEquals(0, searchResults.dtls.size());
    assertEquals(1, searchResults.searchAddresses.detailsList.size());
    assertEquals("1-31 CORONATION ST,ST. JOHN'S,NL,A1C 5B9",
      searchResults.searchAddresses.detailsList.item(0).formattedAddressData);

  }

  /**
   * Test search address where the post code includes an Urban Address post
   * code.
   *
   */
  @Ignore
  public void test_searchAddress_urbanRouteTypeFormat()
    throws InformationalException, AppException {

    new Expectations(Configuration.class) {

      {
        Configuration.getBooleanProperty(
          EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT);
        result = WS_SERVICE_ENABLED;
      }
    };

    // Postal code not present
    final BDMAddressDetailsStruct addressDetailsStruct =
      new BDMAddressDetailsStruct();

    // Postal code present
    addressDetailsStruct.postCode = "K0J1V0";
    final BDMAddressSearchResult searchResults =
      bdmAddress.searchAddress(addressDetailsStruct);

    // Assert
    assertEquals(0, searchResults.dtls.size());
    assertEquals("1073 ACRES RD,FORESTERS FALLS,ON,K0J 1V0",
      searchResults.searchAddresses.detailsList.item(0).formattedAddressData);
    assertEquals("537-561 BENNETT RD,FORESTERS FALLS,ON,K0J 1V0",
      searchResults.searchAddresses.detailsList.item(1).formattedAddressData);

  }

  /**
   * Test search address where the post code includes an Rural General Delivery
   * address, a Rural Route address and a Rural Lock Box address.
   *
   */
  @Ignore
  public void test_searchAddress_RR_RGD_RLB_TypesFormat()
    throws InformationalException, AppException {

    new Expectations(Configuration.class) {

      {
        Configuration.getBooleanProperty(
          EnvVars.BDM_ENV_BDM_WSADDRESS_SERVICE_ENABLEMENT);
        result = WS_SERVICE_ENABLED;
      }
    };

    // Postal code not present
    final BDMAddressDetailsStruct addressDetailsStruct =
      new BDMAddressDetailsStruct();

    // Postal code present
    addressDetailsStruct.postCode = "T0H1A0";
    final BDMAddressSearchResult searchResults =
      bdmAddress.searchAddress(addressDetailsStruct);

    // Assert
    assertEquals(0, searchResults.dtls.size());
    assertEquals(4, searchResults.searchAddresses.detailsList.size());
    assertEquals("PO 1-120,DEADWOOD,AB,T0H 1A0",
      searchResults.searchAddresses.detailsList.item(0).formattedAddressData);
    assertEquals("PO 127-193,DEADWOOD,AB,T0H 1A0",
      searchResults.searchAddresses.detailsList.item(1).formattedAddressData);
    assertEquals("RR 1,DEADWOOD,AB,T0H 1A0",
      searchResults.searchAddresses.detailsList.item(2).formattedAddressData);
    assertEquals("GD ,DEADWOOD,AB,T0H 1A0",
      searchResults.searchAddresses.detailsList.item(3).formattedAddressData);

  }

}
