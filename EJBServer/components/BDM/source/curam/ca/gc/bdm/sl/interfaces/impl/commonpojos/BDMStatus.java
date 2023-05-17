package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Status POJO
 */
public class BDMStatus {

  @SerializedName(value = "StatusCode")
  private BDMReferenceData statusCode;

  @SerializedName(value = "StatusReasonCode")
  private BDMReferenceData statusReasonCode;

  /**
   * @return the statusCode
   */
  public BDMReferenceData getStatusCode() {

    return this.statusCode;
  }

  /**
   * @param statusCode the statusCode to set
   */
  public void setStatusCode(final BDMReferenceData statusCode) {

    this.statusCode = statusCode;
  }

  /**
   * @return the statusReasonCode
   */
  public BDMReferenceData getStatusReasonCode() {

    return this.statusReasonCode;
  }

  /**
   * @param statusReasonCode the statusReasonCode to set
   */
  public void setStatusReasonCode(final BDMReferenceData statusReasonCode) {

    this.statusReasonCode = statusReasonCode;
  }
}
