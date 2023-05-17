
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BDMSINSIRSpecialAccounts {

  @SerializedName("ss:SpecialAccountCodeType")
  @Expose
  private String ssSpecialAccountCodeType;

  @SerializedName("ss:SpecialAccountEffectiveDate")
  @Expose
  private String ssSpecialAccountEffectiveDate;

  public String getSsSpecialAccountCodeType() {

    return ssSpecialAccountCodeType;
  }

  public void
    setSsSpecialAccountCodeType(final String ssSpecialAccountCodeType) {

    this.ssSpecialAccountCodeType = ssSpecialAccountCodeType;
  }

  public String getSsSpecialAccountEffectiveDate() {

    return ssSpecialAccountEffectiveDate;
  }

  public void setSsSpecialAccountEffectiveDate(
    final String ssSpecialAccountEffectiveDate) {

    this.ssSpecialAccountEffectiveDate = ssSpecialAccountEffectiveDate;
  }

}
