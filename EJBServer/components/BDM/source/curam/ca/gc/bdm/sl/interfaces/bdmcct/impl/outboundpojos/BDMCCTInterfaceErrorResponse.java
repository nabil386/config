package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters expected from an error response to an API call
 */
public class BDMCCTInterfaceErrorResponse {

  @SerializedName(value = "Message")
  private String message;

  /**
   * @return the message
   */
  public String getMessage() {

    return this.message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(final String message) {

    this.message = message;
  }
}
