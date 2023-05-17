
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BDMSINSIRMiscellaneous {

  @SerializedName("ss:MiscType")
  @Expose
  private String ssMiscType;

  @SerializedName("nc:EffectiveDate")
  @Expose
  private String ncEffectiveDate;

  public String getSsMiscType() {

    return ssMiscType;
  }

  public void setSsMiscType(final String ssMiscType) {

    this.ssMiscType = ssMiscType;
  }

  public String getNcEffectiveDate() {

    return ncEffectiveDate;
  }

  public void setNcEffectiveDate(final String ncEffectiveDate) {

    this.ncEffectiveDate = ncEffectiveDate;
  }

}
