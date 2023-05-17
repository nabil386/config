package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Tax Year POJO
 */
public class BDMTaxYear {

  @SerializedName(value = "YearDate")
  private String yearDate;

  /**
   * @return the yearDate
   */
  public String getYearDate() {

    return this.yearDate;
  }

  /**
   * @param yearDate the yearDate to set
   */
  public void setYearDate(final String yearDate) {

    this.yearDate = yearDate;
  }
}
