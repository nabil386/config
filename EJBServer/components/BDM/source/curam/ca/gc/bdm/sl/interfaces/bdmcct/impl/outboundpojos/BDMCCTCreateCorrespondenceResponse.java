package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters expected in the response from the CreateCorrespondence API call
 */
public class BDMCCTCreateCorrespondenceResponse {

  @SerializedName(value = "WorkItemId")
  private long workItemID;

  @SerializedName(value = "WorkItemUrl")
  private String workItemURL;

  @SerializedName(value = "Submitted")
  private boolean submittedInd;

  @SerializedName(value = "Message")
  private String message;

  @SerializedName(value = "Status")
  private String status;

  /**
   * @return the workItemID
   */
  public long getWorkItemID() {

    return this.workItemID;
  }

  /**
   * @param workItemID the workItemID to set
   */
  public void setWorkItemID(final long workItemID) {

    this.workItemID = workItemID;
  }

  /**
   * @return the workItemURL
   */
  public String getWorkItemURL() {

    return this.workItemURL;
  }

  /**
   * @param workItemURL the workItemURL to set
   */
  public void setWorkItemURL(final String workItemURL) {

    this.workItemURL = workItemURL;
  }

  /**
   * @return the submittedInd
   */
  public boolean getSubmittedInd() {

    return this.submittedInd;
  }

  /**
   * @param submittedInd the submittedInd to set
   */
  public void setSubmittedInd(final boolean submittedInd) {

    this.submittedInd = submittedInd;
  }

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

  /**
   * @return the message
   */
  public String getStatus() {

    return this.status;
  }

  /**
   * @param message the message to set
   */
  public void setStatus(final String status) {

    this.status = status;
  }
}
