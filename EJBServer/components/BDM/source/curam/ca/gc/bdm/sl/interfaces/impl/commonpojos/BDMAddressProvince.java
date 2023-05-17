package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Address Province POJO
 */
public class BDMAddressProvince {

  @SerializedName(value = "ProvinceCode")
  private BDMReferenceData provinceCode;

  /**
   * Getter - provinceCode
   */
  public BDMReferenceData getProvinceCode() {

    return this.provinceCode;
  }

  /**
   * Setter - provinceCode
   */
  public void setProvinceCode(final BDMReferenceData provinceCode) {

    this.provinceCode = provinceCode;
  }

}
