package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Telephone Number POJO
 */
public class BDMTelephoneNumber {

  @SerializedName(value = "TelephoneCountryCodeID")
  private String countryCode;

  @SerializedName(value = "TelephoneNationalDestinationCodeID")
  private String areaCode;

  @SerializedName(value = "TelephoneSubscriberNumberID")
  private String telephoneNumber;

  @SerializedName(value = "TelephoneSuffixID")
  private String extNumber;

  /**
   * Getter - telephoneNumber
   */
  public String getTelephoneNumber() {

    return this.telephoneNumber;
  }

  /**
   * Setter - telephoneNumber
   */
  public void setTelephoneNumber(final String telephoneNumber) {

    this.telephoneNumber = telephoneNumber;
  }

  /**
   * Getter - countryCode
   */
  public String getCountryCode() {

    return this.countryCode;
  }

  /**
   * Setter - countryCode
   */
  public void setCountryCode(final String countryCode) {

    this.countryCode = countryCode;
  }

  /**
   * Getter - areaCode
   */
  public String getAreaCode() {

    return this.areaCode;
  }

  /**
   * Setter - areaCode
   */
  public void setAreaCode(final String areaCode) {

    this.areaCode = areaCode;
  }

  /**
   * Getter - extNumber
   */
  public String getExtNumber() {

    return this.extNumber;
  }

  /**
   * Setter - extNumber
   */
  public void setExtNumber(final String extNumber) {

    this.extNumber = extNumber;
  }

}
