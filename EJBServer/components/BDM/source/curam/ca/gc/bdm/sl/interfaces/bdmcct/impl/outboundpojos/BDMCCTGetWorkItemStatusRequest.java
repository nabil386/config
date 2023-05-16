package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters needed in the request for the GetWorkItemStatus API call
 */
public class BDMCCTGetWorkItemStatusRequest {

  @SerializedName(value = "userID")
  private String userID;

  @SerializedName(value = "community")
  private String community;

  @SerializedName(value = "workItemID")
  private long workItemID;

  @SerializedName(value = "mode")
  private String correspondenceMode;

  @SerializedName(value = "getData")
  private boolean getData;

  @SerializedName(value = "archiveOnly")
  private boolean archiveOnly;

  @SerializedName(value = "autoCloseEditor")
  private boolean autoCloseEditor;

  @SerializedName(value = "redirectToPrint")
  private boolean redirectToPrint;

  /**
   * @return the userID
   */
  public String getUserID() {

    return this.userID;
  }

  /**
   * @param userId the userID to set
   */
  public void setUserID(final String userID) {

    this.userID = userID;
  }

  /**
   * @return the community
   */
  public String getCommunity() {

    return this.community;
  }

  /**
   * @param community the community to set
   */
  public void setCommunity(final String community) {

    this.community = community;
  }

  /**
   * @return the workItemID
   */
  public long getWorkItemId() {

    return this.workItemID;
  }

  /**
   * @param workItemID the workItemID to set
   */
  public void setWorkItemID(final long workItemID) {

    this.workItemID = workItemID;
  }

  /**
   * @return the correspondenceMode
   */
  public String getCorrespondenceMode() {

    return this.correspondenceMode;
  }

  /**
   * @param correspondenceMode the correspondenceMode to set
   */
  public void setCorrespondenceMode(final String correspondenceMode) {

    this.correspondenceMode = correspondenceMode;
  }

  /**
   * @return the getData
   */
  public boolean getGetData() {

    return this.getData;
  }

  /**
   * @param getData the getData to set
   */
  public void setGetData(final boolean getData) {

    this.getData = getData;
  }

  /**
   * @return the archiveOnly
   */
  public boolean getArchiveOnly() {

    return this.archiveOnly;
  }

  /**
   * @param archiveOnly the archiveOnly to set
   */
  public void setArchiveOnly(final boolean archiveOnly) {

    this.archiveOnly = archiveOnly;
  }

  /**
   * @return the autoCloseEditor
   */
  public boolean getAutoCloseEditor() {

    return this.autoCloseEditor;
  }

  /**
   * @param autoCloseEditor the autoCloseEditor to set
   */
  public void setAutoCloseEditor(final boolean autoCloseEditor) {

    this.autoCloseEditor = autoCloseEditor;
  }

  /**
   * @return the redirectToPrint
   */
  public boolean getRedirectToPrint() {

    return this.redirectToPrint;
  }

  /**
   * @param redirectToPrint the redirectToPrint to set
   */
  public void setRedirectToPrint(final boolean redirectToPrint) {

    this.redirectToPrint = redirectToPrint;
  }

}
