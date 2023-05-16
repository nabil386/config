package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters expected in the response from the GetWorkItemStatus API call
 */
public class BDMCCTGetWorkItemStatusResponse {

  @SerializedName(value = "WorkItemStatusId")
  private long workItemStatusID;

  @SerializedName(value = "WorkItemStatusName")
  private String workItemStatusName;

  @SerializedName(value = "WorkItemStatusUrl")
  private String workItemStatusURL;

  @SerializedName(value = "WorkItemData")
  private String workItemData;

  /**
   * @return the workItemStatusID
   */
  public long getWorkItemStatusID() {

    return this.workItemStatusID;
  }

  /**
   * @param workItemStatusID the workItemStatusID to set
   */
  public void setWorkItemStatusID(final long workItemStatusID) {

    this.workItemStatusID = workItemStatusID;
  }

  /**
   * @return the workItemStatusName
   */
  public String getWorkItemStatusName() {

    return this.workItemStatusName;
  }

  /**
   * @param workItemStatusName the workItemStatusName to set
   */
  public void setWorkItemStatusName(final String workItemStatusName) {

    this.workItemStatusName = workItemStatusName;
  }

  /**
   * @return the workItemStatusURL
   */
  public String getWorkItemStatusURL() {

    return this.workItemStatusURL;
  }

  /**
   * @param workItemStatusURL the workItemStatusURL to set
   */
  public void setWorkItemStatusURL(final String workItemStatusURL) {

    this.workItemStatusURL = workItemStatusURL;
  }

  /**
   * @return the workItemData
   */
  public String getWorkItemData() {

    return this.workItemData;
  }

  /**
   * @param workItemData the workItemData to set
   */
  public void setWorkItemData(final String workItemData) {

    this.workItemData = workItemData;
  }

}
