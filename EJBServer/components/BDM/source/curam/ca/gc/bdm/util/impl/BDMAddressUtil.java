
package curam.ca.gc.bdm.util.impl;

import curam.ca.gc.bdm.application.impl.BDMDatastoreConstants;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.COUNTRY;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AddressFactory;
import curam.core.intf.Address;
import curam.core.intf.AddressData;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.ElementDetails;
import curam.core.struct.OtherAddressData;
import curam.datastore.impl.Entity;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

/**
 * @author amod.gole
 *
 * Task Date Name Description
 * 9418 05/09/2022 AGOLE New Class for Address Utility function defined for AG
 * script
 *
 */
public class BDMAddressUtil {

  /**
   * Method to Return Empty Address Data
   *
   * @return String
   * @throws AppException
   * @throws InformationalException
   */
  public String getEmptyAddressData()
    throws AppException, InformationalException {

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.countryCode = COUNTRY.CA;

    addressFieldDetails.suiteNum = "";
    addressFieldDetails.addressLine1 = "";
    addressFieldDetails.addressLine2 = "";
    addressFieldDetails.province = "";
    addressFieldDetails.city = "";
    addressFieldDetails.postalCode = "";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    final OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);

    return otherAddressData.addressData;
  }

  /**
   *
   * Util Method to convert Address Data
   *
   * @param otherAddressData
   * @param addressElementType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public String getAddressDetails(final OtherAddressData otherAddressData,
    final String addressElementType)
    throws AppException, InformationalException {

    final AddressData addressDataObj = AddressDataFactory.newInstance();

    final AddressMap addressMap = new AddressMap();

    addressMap.name = addressElementType;

    final AddressMapList addressMapList =
      addressDataObj.parseDataToMap(otherAddressData);

    final ElementDetails elementDetails =
      addressDataObj.findElement(addressMapList, addressMap);

    if (elementDetails.elementFound) {

      return elementDetails.elementValue;
    }

    return "";

  }

  /**
   *
   * Util Method to Address Data in oneline from Address Entity
   *
   * @param addressEntity
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  public String getOneLineAddress(final Entity addressEntity)
    throws AppException, InformationalException {

    String oneLineAddress = "";
    if (addressEntity != null) {

      final AddressData addressDataObj = AddressDataFactory.newInstance();
      final AddressFieldDetails addressFieldDetails =
        new AddressFieldDetails();
      // Read Address Fields from entity
      final String Country =
        addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_COUNTRY);

      addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
      addressFieldDetails.countryCode = Country;

      addressFieldDetails.suiteNum =
        addressEntity.getAttribute(BDMDatastoreConstants.SUITE_NUM);
      addressFieldDetails.addressLine1 =
        addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_LINE1);
      addressFieldDetails.addressLine2 =
        addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_LINE2);
      addressFieldDetails.addressLine3 =
        addressEntity.getAttribute(BDMDatastoreConstants.ADDRESS_LINE3);
      addressFieldDetails.city =
        addressEntity.getAttribute(BDMDatastoreConstants.CITY);
      // assigning special attributes based on country
      if (curam.codetable.COUNTRY.CA.equalsIgnoreCase(Country)) {

        addressFieldDetails.province =
          addressEntity.getAttribute(BDMDatastoreConstants.PROVINCE);
        addressFieldDetails.postalCode =
          addressEntity.getAttribute(BDMDatastoreConstants.POSTAL_CODE);

      } else if (curam.codetable.COUNTRY.US.equalsIgnoreCase(Country)
        || "USA".equalsIgnoreCase(Country)) {

        addressFieldDetails.stateProvince =
          addressEntity.getAttribute(BDMDatastoreConstants.STATE);
        addressFieldDetails.zipCode =
          addressEntity.getAttribute(BDMDatastoreConstants.ZIP_CODE);

      } else {
        // For International stateProvinance is suppose to be Region as per
        // document
        addressFieldDetails.stateProvince =
          addressEntity.getAttribute(BDMDatastoreConstants.STATE);
        addressFieldDetails.zipCode =
          addressEntity.getAttribute(BDMDatastoreConstants.ZIP_CODE);

      }
      // Parse Address Fields
      final OtherAddressData otherAddressData =
        addressDataObj.parseFieldsToData(addressFieldDetails);

      final Address addressObj = AddressFactory.newInstance();
      // Format Address
      final OtherAddressData formattedAddressData =
        addressObj.getShortFormat(otherAddressData);
      oneLineAddress = formattedAddressData.addressData;

      addressEntity.setAttribute(BDMDatastoreConstants.ONE_LINE_ADDRESS,
        oneLineAddress);
    }

    return oneLineAddress;
  }
}
