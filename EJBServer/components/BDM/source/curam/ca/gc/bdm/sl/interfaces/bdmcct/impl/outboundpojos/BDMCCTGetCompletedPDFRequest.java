package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters needed in the request for the GetCompletedPDF API call
 */
public class BDMCCTGetCompletedPDFRequest {

  @SerializedName(value = "userID")
  private String userID;

  @SerializedName(value = "community")
  private String community;

  @SerializedName(value = "vaultLetterID")
  private long vaultLetterID;

  @SerializedName(value = "workItemID")
  private long workItemID;

  /**
   * @return the userID
   */
  public String getUserID() {

    return this.userID;
  }

  /**
   * @return the community
   */
  public String getCommunity() {

    return this.community;
  }

  /**
   * @return the vaultLetterID
   */
  public long getVaultLetterID() {

    return this.vaultLetterID;
  }

  /**
   * @return the workItemID
   */
  public long getWorkItemID() {

    return this.workItemID;
  }

  /**
   * @param userID the userID to set
   */
  public void setUserID(final String userID) {

    this.userID = userID;
  }

  /**
   * @param community the community to set
   */
  public void setCommunity(final String community) {

    this.community = community;
  }

  /**
   * @param vaultLetterID the vaultLetterID to set
   */
  public void setVaultLetterID(final long vaultLetterID) {

    this.vaultLetterID = vaultLetterID;
  }

  /**
   * @param workItemID the workItemID to set
   */
  public void setWorkItemID(final long workItemID) {

    this.workItemID = workItemID;
  }
}
