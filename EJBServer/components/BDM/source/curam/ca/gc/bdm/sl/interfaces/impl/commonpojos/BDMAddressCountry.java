package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Address Country POJO.
 */
public class BDMAddressCountry {

  @SerializedName(value = "CountryCode")
  private BDMReferenceData countryCode;

  /**
   * Getter - countryCode
   */
  public BDMReferenceData getcountryCode() {

    return this.countryCode;
  }

  /**
   * Setter - countryCode
   */
  public void setcountryCode(final BDMReferenceData countryCode) {

    this.countryCode = countryCode;
  }

}
