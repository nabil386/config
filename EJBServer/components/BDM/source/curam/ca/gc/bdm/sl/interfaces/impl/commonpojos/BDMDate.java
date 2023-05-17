package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Date POJO
 */
public class BDMDate {

  @SerializedName(value = "date")
  private String date;

  /**
   * Getter - date
   */
  public String getDate() {

    return this.date;
  }

  /**
   * Setter - date
   */
  public void setDate(final String date) {

    this.date = date;
  }

}
