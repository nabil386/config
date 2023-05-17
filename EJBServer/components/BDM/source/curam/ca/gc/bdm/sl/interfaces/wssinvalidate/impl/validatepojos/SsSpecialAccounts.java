
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SsSpecialAccounts {

  @SerializedName("ss:SpecialAccountCodeType")
  @Expose
  private Integer ssSpecialAccountCodeType;

  @SerializedName("ss:SpecialAccountEffectiveDate")
  @Expose
  private String ssSpecialAccountEffectiveDate;

  public Integer getSsSpecialAccountCodeType() {

    return ssSpecialAccountCodeType;
  }

  public void
    setSsSpecialAccountCodeType(final Integer ssSpecialAccountCodeType) {

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
