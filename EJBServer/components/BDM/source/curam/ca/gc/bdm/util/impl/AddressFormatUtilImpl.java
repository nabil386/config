
package curam.ca.gc.bdm.util.impl;

import curam.codetable.impl.ADDRESSELEMENTTYPEEntry;
import curam.core.struct.AddressElementStruct;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.ElementDetails;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;


/**
 * Implements address handling methods related to address format.
 */
public class AddressFormatUtilImpl implements AddressFormatUtil { 
 
  /**
   * {@inheritDoc}
   */
  public AddressElementStruct getElementByType(
    final OtherAddressData addressDataString,
    final ADDRESSELEMENTTYPEEntry addressElementType)
    throws AppException, InformationalException {

    // address data object
    final curam.core.intf.AddressData addressDataObj = curam.core.fact.AddressDataFactory.newInstance();

    final AddressElementStruct addressElementStruct = new AddressElementStruct();

    // convert address data string into <name><value> pairs vector
    final AddressMapList addressMapList = addressDataObj.parseDataToMap(
      addressDataString);

    final AddressMap addressMap = new AddressMap();

    addressMap.name = addressElementType.getCode();

    // get the first address line of other format address
    final ElementDetails elementDetails = addressDataObj.findElement(
      addressMapList, addressMap);

    // check if element was found
    if (elementDetails.elementFound && !elementDetails.elementValue.isEmpty()) {
      addressElementStruct.addressElementString = elementDetails.elementValue;
    }

    return addressElementStruct;
    
  }
    
}