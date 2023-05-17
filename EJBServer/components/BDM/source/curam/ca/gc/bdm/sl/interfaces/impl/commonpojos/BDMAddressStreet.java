package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Address Street POJO
 */
public class BDMAddressStreet {

  @SerializedName(value = "StreetNumberText")
  private String streetNumber;

  @SerializedName(value = "StreetName")
  private String streetName;

  /**
   * Getter - streetNumer
   */
  public String getStreetNumber() {

    return this.streetNumber;
  }

  /**
   * Setter - streetNumer
   */
  public void setStreetNumber(final String streetNumber) {

    this.streetNumber = streetNumber;
  }

  /**
   * Getter - streetName
   */
  public String getStreetName() {

    return this.streetName;
  }

  /**
   * Setter - streetName
   */
  public void setStreetName(final String streetName) {

    this.streetName = streetName;
  }

}
