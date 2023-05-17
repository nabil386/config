
package curam.ca.gc.bdm.util.impl;

import com.google.inject.ImplementedBy;
import curam.codetable.impl.ADDRESSELEMENTTYPEEntry;
import curam.core.struct.AddressElementStruct;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;


/**
 * Address format handling interface.
 */
@ImplementedBy(AddressFormatUtilImpl.class)
public interface AddressFormatUtil { 
 
 /**
   * Method takes an addressData string and returns value of address element. 
   *
   * @param addressDataString Contains address data string.
   * @param addressElementType Contains address element for which value must be retrieved.
   *
   * @return contains address element value retrieved.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  public AddressElementStruct getElementByType(
    final OtherAddressData addressDataString,
    final ADDRESSELEMENTTYPEEntry addressElementType) 
    throws AppException, InformationalException;
    
}