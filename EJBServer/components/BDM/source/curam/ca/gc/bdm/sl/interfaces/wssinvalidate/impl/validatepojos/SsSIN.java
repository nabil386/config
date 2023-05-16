
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SsSIN {

  @SerializedName("nc:IdentificationStatus")
  @Expose
  private Integer ncIdentificationStatus;

  @SerializedName("ss:IssueDate")
  @Expose
  private String ssIssueDate;

  @SerializedName("ss:SINRestrictions")
  @Expose
  private List<String> ssSINRestrictions = null;

  @SerializedName("ss:DormantFlag")
  @Expose
  private Integer ssDormantFlag;

  @SerializedName("ss:DateDormantEffective")
  @Expose
  private String ssDateDormantEffective;

  @SerializedName("ss:SINCertifications")
  @Expose
  private SsSINCertifications ssSINCertifications;

  public Integer getNcIdentificationStatus() {

    return ncIdentificationStatus;
  }

  public void
    setNcIdentificationStatus(final Integer ncIdentificationStatus) {

    this.ncIdentificationStatus = ncIdentificationStatus;
  }

  public String getSsIssueDate() {

    return ssIssueDate;
  }

  public void setSsIssueDate(final String ssIssueDate) {

    this.ssIssueDate = ssIssueDate;
  }

  public List<String> getSsSINRestrictions() {

    return ssSINRestrictions;
  }

  public void setSsSINRestrictions(final List<String> ssSINRestrictions) {

    this.ssSINRestrictions = ssSINRestrictions;
  }

  public Integer getSsDormantFlag() {

    return ssDormantFlag;
  }

  public void setSsDormantFlag(final Integer ssDormantFlag) {

    this.ssDormantFlag = ssDormantFlag;
  }

  public String getSsDateDormantEffective() {

    return ssDateDormantEffective;
  }

  public void setSsDateDormantEffective(final String ssDateDormantEffective) {

    this.ssDateDormantEffective = ssDateDormantEffective;
  }

  public SsSINCertifications getSsSINCertifications() {

    return ssSINCertifications;
  }

  public void
    setSsSINCertifications(final SsSINCertifications ssSINCertifications) {

    this.ssSINCertifications = ssSINCertifications;
  }

}
