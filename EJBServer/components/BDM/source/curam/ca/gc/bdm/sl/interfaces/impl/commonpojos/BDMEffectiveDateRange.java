package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Effective Date Range POJO
 */
public class BDMEffectiveDateRange {

  @SerializedName(value = "StartDate")
  private BDMDate startDate;

  @SerializedName(value = "EndDate")
  private BDMDate endDate;

  /**
   * Getter - startDate
   */
  public BDMDate getStartDate() {

    return this.startDate;
  }

  /**
   * Setter - startDate
   */
  public void setStartDate(final BDMDate startDate) {

    this.startDate = startDate;
  }

  /**
   * Getter - endDate
   */
  public BDMDate getEndDate() {

    return this.endDate;
  }

  /**
   * Setter - endDate
   */
  public void setEndDate(final BDMDate endDate) {

    this.endDate = endDate;
  }

}
