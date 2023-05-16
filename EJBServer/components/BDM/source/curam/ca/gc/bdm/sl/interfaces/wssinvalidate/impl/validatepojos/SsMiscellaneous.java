
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SsMiscellaneous {

  @SerializedName("ss:MiscType")
  @Expose
  private Integer ssMiscType;

  @SerializedName("nc:EffectiveDate")
  @Expose
  private String ncEffectiveDate;

  public Integer getSsMiscType() {

    return ssMiscType;
  }

  public void setSsMiscType(final Integer ssMiscType) {

    this.ssMiscType = ssMiscType;
  }

  public String getNcEffectiveDate() {

    return ncEffectiveDate;
  }

  public void setNcEffectiveDate(final String ncEffectiveDate) {

    this.ncEffectiveDate = ncEffectiveDate;
  }

}
