
package curam.ca.gc.bdm.ws.address.impl;

import curam.core.impl.CuramConst;
import org.apache.commons.lang3.StringUtils;

/**
 * A POJO class to hold the WSAddress response object.
 *
 * This POJO only contains a select number of fields from the total address
 * response as is currently required by our internal consumers but can be
 * extended with extra fields as required.
 */
public class BDMWSAddressDetail {

  /**
   * Address Type
   *
   * TODO: consider using enumerated field type based on the different types of
   * categorised addresses by ESDC
   */
  private String addressType = "";

  /**
   * Street Name
   */
  private String streetName = "";

  /**
   * City
   */
  private String city = "";

  /**
   * Province.
   *
   * NOTE: We do not have exact steering on these codes but they look to be the
   * internationally approved postal and ISO abbreviations, e.g. Quebec = QC.
   * FIXME: This would be better represented as an enumerated datatype retrieved
   * from a codetable
   */
  private String province = "";

  /**
   * Country
   *
   * NOTE: We do not have exact steering on these codes but they are likely the
   * ISO Alpha 3 country codes, e.g. Canada = CAN
   */
  private String country = "";

  /**
   * Postal Code
   */
  private String postalCode = "";

  /**
   * Street Number Minimum
   */
  private String streetNumberMinimum = "";

  /**
   * Street Number Maximum
   */
  private String streetNumberMaximum = "";

  /**
   * Street Type
   */
  private String streetType = "";

  /**
   * Directory Area Name
   */
  private String directoryAreaName = "";

  /**
   * Delivery Installation Description
   */
  private String deliveryInstallationDescription = "";

  /**
   * Street Direction
   */
  private String streetDirection = "";

  /**
   * Street Address Sequence
   */
  private String streetAddressSequence = "";

  /**
   * Lockbox Number Minimum
   */
  private String lockboxNumberMinimum = "";

  /**
   * Lockbox Number Maximum
   */
  private String lockboxNumberMaximum = "";

  /**
   * Suite Number Minimum
   */
  private String suiteNumberMinimum = "";

  /**
   * Suite Number Maximum
   */
  private String suiteNumberMaximum = "";

  /**
   * Route Service Type
   */
  private String routeServiceType = "";

  /**
   * Route Service Number
   */
  private String routeServiceNumber = "";

  /**
   * Rural Route Service Type
   */
  private String ruralRouteServiceType = "";

  /**
   * Rural Route Service Number
   */
  private String ruralRouteServiceNumber = "";

  // ---------------------------------------------------------

  public String getStreetName() {

    return this.streetName;
  }

  public void setStreetName(final String streetName) {

    this.streetName = streetName;
  }

  public String getCity() {

    return this.city;
  }

  public void setCity(final String city) {

    this.city = city;
  }

  public String getProvince() {

    return this.province;
  }

  public void setProvince(final String province) {

    this.province = province;
  }

  public String getCountry() {

    return this.country;
  }

  public void setCountry(final String country) {

    this.country = country;
  }

  public String getPostalCode() {

    return this.postalCode;
  }

  /**
   * Returns the Postal Code with a space after the third character.
   *
   * e.g. "G1P4A3" -> "G1P 4A3"
   *
   * @return String
   */
  public String getMiddleSpacedPostalCode() {

    String formattedPostalCode = StringUtils.trimToEmpty(this.postalCode);
    if (!StringUtils.isEmpty(formattedPostalCode)) {

      // replace any space chars, assume we don't trust the post code returned
      // and saves another if statement for the existence of a space
      formattedPostalCode =
        StringUtils.replace(formattedPostalCode, CuramConst.gkSpace, "");
      formattedPostalCode = formattedPostalCode.substring(0, 3)
        + CuramConst.gkSpace + formattedPostalCode.substring(3);

    }

    return formattedPostalCode;
  }

  public void setPostalCode(final String postalCode) {

    this.postalCode = postalCode;
  }

  public String getStreetNumberMinimum() {

    return this.streetNumberMinimum;
  }

  public void setStreetNumberMinimum(final String streetNumberMinimum) {

    this.streetNumberMinimum = streetNumberMinimum;
  }

  public String getStreetNumberMaximum() {

    return this.streetNumberMaximum;
  }

  public void setStreetNumberMaximum(final String streetNumberMaximum) {

    this.streetNumberMaximum = streetNumberMaximum;
  }

  public String getStreetType() {

    return this.streetType;
  }

  public void setStreetType(final String streetType) {

    this.streetType = streetType;
  }

  public String getDirectoryAreaName() {

    return this.directoryAreaName;
  }

  public void setDirectoryAreaName(final String directoryAreaName) {

    this.directoryAreaName = directoryAreaName;
  }

  public String getAddressType() {

    return this.addressType;
  }

  public void setAddressType(final String addressType) {

    this.addressType = addressType;
  }

  public String getStreetDirection() {

    return this.streetDirection;
  }

  public void setStreetDirection(final String streetDirection) {

    this.streetDirection = streetDirection;
  }

  public String getLockboxNumberMinimum() {

    return this.lockboxNumberMinimum;
  }

  public void setLockboxNumberMinimum(final String lockboxMinimum) {

    this.lockboxNumberMinimum = lockboxMinimum;
  }

  public String getLockboxNumberMaximum() {

    return this.lockboxNumberMaximum;
  }

  public void setLockboxNumberMaximum(final String lockboxMaximum) {

    this.lockboxNumberMaximum = lockboxMaximum;
  }

  public String getSuiteNumberMinimum() {

    return this.suiteNumberMinimum;
  }

  public void setSuiteNumberMinimum(final String suiteNumberMinimum) {

    this.suiteNumberMinimum = suiteNumberMinimum;
  }

  public String getSuiteNumberMaximum() {

    return this.suiteNumberMaximum;
  }

  public void setSuiteNumberMaximum(final String suiteNumberMaximum) {

    this.suiteNumberMaximum = suiteNumberMaximum;
  }

  public String getRouteServiceType() {

    return this.routeServiceType;
  }

  public void setRouteServiceType(final String ruralRouteServiceType) {

    this.routeServiceType = ruralRouteServiceType;
  }

  public String getRouteServiceNumber() {

    return this.routeServiceNumber;
  }

  public void setRouteServiceNumber(final String ruralRouteServiceNumber) {

    this.routeServiceNumber = ruralRouteServiceNumber;
  }

  public String getDeliveryInstallationDescription() {

    return this.deliveryInstallationDescription;
  }

  public void setDeliveryInstallationDescription(
    final String deliveryInstallationDescription) {

    this.deliveryInstallationDescription = deliveryInstallationDescription;
  }

  public String getStreetAddressSequence() {

    return this.streetAddressSequence;
  }

  public void setStreetAddressSequence(final String streetAddressSequence) {

    this.streetAddressSequence = streetAddressSequence;
  }

  public String getRuralRouteServiceType() {

    return this.ruralRouteServiceType;
  }

  public void setRuralRouteServiceType(final String ruralRouteServiceType) {

    this.ruralRouteServiceType = ruralRouteServiceType;
  }

  public String getRuralRouteServiceNumber() {

    return this.ruralRouteServiceNumber;
  }

  public void
    setRuralRouteServiceNumber(final String ruralRouteServiceNumber) {

    this.ruralRouteServiceNumber = ruralRouteServiceNumber;
  }

}
