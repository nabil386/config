package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Amount POJO
 */
public class BDMAmount {

  @SerializedName(value = "Amount")
  private String amount;

  /**
   * Getter - amount
   */
  public String getAmount() {

    return this.amount;
  }

  /**
   * Setter - amount
   */
  public void setAmount(final String amount) {

    this.amount = amount;
  }
}
