
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SsIndividual {

  @SerializedName("ss:SearchTable")
  @Expose
  private String ssSearchTable;

  @SerializedName("nc:PersonSexCode")
  @Expose
  private Integer ncPersonSexCode;

  @SerializedName("ss:TwinFlagIndicator")
  @Expose
  private Integer ssTwinFlagIndicator;

  @SerializedName("ss:MultipleFlagIndicator")
  @Expose
  private Integer ssMultipleFlagIndicator;

  @SerializedName("ss:DuplicateFlagIndicator")
  @Expose
  private Integer ssDuplicateFlagIndicator;

  @SerializedName("ss:SIN")
  @Expose
  private SsSIN ssSIN;

  public String getSsSearchTable() {

    return ssSearchTable;
  }

  public void setSsSearchTable(final String ssSearchTable) {

    this.ssSearchTable = ssSearchTable;
  }

  public Integer getNcPersonSexCode() {

    return ncPersonSexCode;
  }

  public void setNcPersonSexCode(final Integer ncPersonSexCode) {

    this.ncPersonSexCode = ncPersonSexCode;
  }

  public Integer getSsTwinFlagIndicator() {

    return ssTwinFlagIndicator;
  }

  public void setSsTwinFlagIndicator(final Integer ssTwinFlagIndicator) {

    this.ssTwinFlagIndicator = ssTwinFlagIndicator;
  }

  public Integer getSsMultipleFlagIndicator() {

    return ssMultipleFlagIndicator;
  }

  public void
    setSsMultipleFlagIndicator(final Integer ssMultipleFlagIndicator) {

    this.ssMultipleFlagIndicator = ssMultipleFlagIndicator;
  }

  public Integer getSsDuplicateFlagIndicator() {

    return ssDuplicateFlagIndicator;
  }

  public void
    setSsDuplicateFlagIndicator(final Integer ssDuplicateFlagIndicator) {

    this.ssDuplicateFlagIndicator = ssDuplicateFlagIndicator;
  }

  public SsSIN getSsSIN() {

    return ssSIN;
  }

  public void setSsSIN(final SsSIN ssSIN) {

    this.ssSIN = ssSIN;
  }

}
