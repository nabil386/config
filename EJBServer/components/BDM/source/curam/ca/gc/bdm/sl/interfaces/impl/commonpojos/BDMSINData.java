package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Person SIN POJO
 */
public class BDMSINData {

  @SerializedName(value = "IdentificationID")
  private String sin;

  /**
   * @return the sin
   */
  public String getSin() {

    return this.sin;
  }

  /**
   * @param sin the sin to set
   */
  public void setSin(final String sin) {

    this.sin = sin;
  }

}
