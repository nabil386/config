package curam.ca.gc.bdm.communication.impl;

import curam.codetable.ENVPROPERTYCATEGORY;
import curam.codetable.LOCALE;
import curam.util.administration.fact.PropertyAdminFactory;
import curam.util.administration.intf.PropertyAdmin;
import curam.util.administration.struct.PropertiesSearchKey;
import curam.util.administration.struct.PropertyDescriptionDetails;
import curam.util.administration.struct.PropertyDescriptionDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.HashMap;
import java.util.Map;

public class BDMPropertiesUtil {

  PropertyAdmin propAdminObj = PropertyAdminFactory.newInstance();

  /**
   * Method to read property
   *
   * @param propertyName Property name
   * @param propertyCategory Property category
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public PropertyDescriptionDetailsList
    readPropertyList(final String propertyName, final String propertyCategory)
      throws AppException, InformationalException {

    final PropertyDescriptionDetailsList returnProplist =

      new PropertyDescriptionDetailsList();
    final PropertiesSearchKey searchKey = new PropertiesSearchKey();
    searchKey.locale = LOCALE.ENGLISH;
    searchKey.category = ENVPROPERTYCATEGORY.PROPLIST;
    final PropertyDescriptionDetailsList proplist =
      propAdminObj.searchPropertiesByNameDescCategory(searchKey);
    for (final PropertyDescriptionDetails propDetails : proplist.dtls
      .items()) {
      // is property value is property name, add it to returnPropList and return
      if (!propDetails.value.equalsIgnoreCase(propertyName))
        continue;
      else {
        searchKey.category = propertyCategory;
        searchKey.name = propertyName;
        final PropertyDescriptionDetailsList tempProplist =
          propAdminObj.searchPropertiesByNameDescCategory(searchKey);
        for (final PropertyDescriptionDetails propertyDetails : tempProplist.dtls
          .items()) {
          if (propertyDetails.name.contains(propertyName))
            returnProplist.dtls.add(propertyDetails);
        }
      }
    }
    return returnProplist;

  }

  /**
   * Add property list as a value to property details name
   *
   * @param proplist Property list
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public Map<String, PropertyDescriptionDetails>
    getPropertyMap(final PropertyDescriptionDetailsList proplist)
      throws AppException, InformationalException {

    final HashMap<String, PropertyDescriptionDetails> propMaopResult =
      new HashMap<String, PropertyDescriptionDetails>();

    for (final PropertyDescriptionDetails propDetails : proplist.dtls
      .items()) {
      propMaopResult.put(propDetails.name, propDetails);
    }
    return propMaopResult;
  }
}
