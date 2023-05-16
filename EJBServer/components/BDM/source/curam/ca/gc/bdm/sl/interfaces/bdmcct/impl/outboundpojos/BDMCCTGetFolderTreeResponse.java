package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters expected in the response from the GetFolderTree API call
 */
public class BDMCCTGetFolderTreeResponse {

  @SerializedName(value = "Status")
  private String status;

  @SerializedName(value = "Data")
  private BDMCCTGetFolderTreeFolder data;

  /**
   * @return the status
   */
  public String getStatus() {

    return this.status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(final String status) {

    this.status = status;
  }

  /**
   * @return the data
   */
  public BDMCCTGetFolderTreeFolder getData() {

    return this.data;
  }

  /**
   * @param data the data to set
   */
  public void setData(final BDMCCTGetFolderTreeFolder data) {

    this.data = data;
  }
}
