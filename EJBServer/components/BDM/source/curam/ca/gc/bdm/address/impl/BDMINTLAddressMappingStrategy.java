package curam.ca.gc.bdm.address.impl;

import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.core.fact.AddressDataFactory;
import curam.core.intf.AddressData;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.workspaceservices.applicationprocessing.impl.NationalAddressMappingStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * This class handles mapping of the address
 *
 */
public class BDMINTLAddressMappingStrategy
  extends NationalAddressMappingStrategy {

  @Override
  public String getCountryCode() {

    return ADDRESSLAYOUTTYPE.CA;
  }

  /**
   * @return The address or empty string if address empty
   * @param address Fields map object
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public String getAddressData(final Map<String, Object> addressFields)
    throws AppException, InformationalException {

    new OtherAddressData();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();

    final AddressData addressData = AddressDataFactory.newInstance();

    addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;

    addressFieldDetails.countryCode =
      this.getFieldIfExistsAsString(addressFields, "country");

    addressFieldDetails.suiteNum =
      this.getFieldIfExistsAsString(addressFields, "suiteNum");

    addressFieldDetails.addressLine1 =
      this.getFieldIfExistsAsString(addressFields, "addressLine1");

    addressFieldDetails.addressLine2 =
      this.getFieldIfExistsAsString(addressFields, "addressLine2");

    addressFieldDetails.addressLine3 =
      this.getFieldIfExistsAsString(addressFields, "addressLine3");

    addressFieldDetails.addressLine4 =
      this.getFieldIfExistsAsString(addressFields, "addressLine4");

    addressFieldDetails.city =
      this.getFieldIfExistsAsString(addressFields, "city");

    addressFieldDetails.province =
      this.getFieldIfExistsAsString(addressFields, "province");

    addressFieldDetails.stateProvince =
      this.getFieldIfExistsAsString(addressFields, "state");

    addressFieldDetails.zipCode =
      this.getFieldIfExistsAsString(addressFields, "zipCode");

    addressFieldDetails.postalCode =
      this.getFieldIfExistsAsString(addressFields, "postalCode");
    // BEGIN TASK - 26583 BDAT - Mailing address provided on application not
    // listed under Evidence & Demographics in Participant Case modified
    // condition for RR Address Type
    if (!StringUtil.isNullOrEmpty(addressFieldDetails.addressLine1)
      || !StringUtil.isNullOrEmpty(addressFieldDetails.city)) {
      // END TASK -26583
      final OtherAddressData otherAddressData =
        addressData.parseFieldsToData(addressFieldDetails);
      return otherAddressData.addressData;

    } else {
      return "";
    }
  }

  /**
   *
   * @return a list object with the address information
   */
  @Override
  public List<String> getAddressFields() {

    final List<String> returnObj = new ArrayList();

    returnObj.add("suiteNum");
    returnObj.add("city");
    returnObj.add("stateProvince");
    returnObj.add("postalCode");
    returnObj.add("addressLine1");
    returnObj.add("addressLine2");
    returnObj.add("addressLine3");
    returnObj.add("addressLine4");
    returnObj.add("countryCode");

    return returnObj;
  }

}
