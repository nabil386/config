package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Administrative Region POJO
 */

public class BDMAdministrativeRegion {

  @SerializedName(value = "AdministrativeRegionCode")
  private BDMReferenceData adminRegionCode;

  /**
   * Getter - adminRegionCode
   */
  public BDMReferenceData getadminRegionCode() {

    return this.adminRegionCode;
  }

  /**
   * Setter - adminRegionCode
   */
  public void setadminRegionCode(final BDMReferenceData adminRegionCode) {

    this.adminRegionCode = adminRegionCode;
  }

}
