package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;

public class BDMCCTUpdateCorrespondenceRequest {

  @SerializedName(value = "Community")
  private String community;

  @SerializedName(value = "UserId")
  private String userId;

  @SerializedName(value = "WorkItemId")
  private long workItemId;

  @SerializedName(value = "DataMapName")
  private String dataMapName;

  @SerializedName(value = "DataXML")
  private String dataXML;

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
   * @return the userId
   */
  public String getUserId() {

    return this.userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(final String userId) {

    this.userId = userId;
  }

  /**
   * @return the workItemId
   */
  public long getWorkItemId() {

    return this.workItemId;
  }

  /**
   * @param workItemId the workItemId to set
   */
  public void setWorkItemId(final long workItemId) {

    this.workItemId = workItemId;
  }

  /**
   * @return the dataMapName
   */
  public String getDataMapName() {

    return this.dataMapName;
  }

  /**
   * @param dataMapName the dataMapName to set
   */
  public void setDataMapName(final String dataMapName) {

    this.dataMapName = dataMapName;
  }

  /**
   * @return the dataXML
   */
  public String getDataXML() {

    return this.dataXML;
  }

  /**
   * @param dataXML the dataXML to set
   */
  public void setDataXML(final String dataXML) {

    this.dataXML = dataXML;
  }

}
