package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters needed in the request for the GetFolderTree API call
 */
public class BDMCCTGetFolderTreeRequest {

  @SerializedName(value = "community")
  private String community;

  @SerializedName(value = "userid")
  private String userID;

  @SerializedName(value = "includetemplates")
  private boolean includeTemplatesInd;

  @SerializedName(value = "includetemplatefields")
  private boolean includeTemplateFieldsInd;

  @SerializedName(value = "getmandatoryfields")
  private boolean getMandatoryFieldsInd;

  /**
   * @return the community
   */
  public String getCommunity() {

    return this.community;
  }

  /**
   * @param community
   */
  public void setCommunity(final String community) {

    this.community = community;
  }

  /**
   * @return the userID
   */
  public String getUserID() {

    return this.userID;
  }

  /**
   * @param userID
   */
  public void setUserID(final String userID) {

    this.userID = userID;
  }

  /**
   * @return the includeTemplatesInd
   */
  public boolean getIncludeTemplatesInd() {

    return this.includeTemplatesInd;
  }

  /**
   * @param includeTemplatesInd
   */
  public void setIncludeTemplatesInd(final boolean includeTemplatesInd) {

    this.includeTemplatesInd = includeTemplatesInd;
  }

  /**
   * @return the includeTemplateFieldsInd
   */
  public boolean getIncludeTemplateFieldsInd() {

    return this.includeTemplateFieldsInd;
  }

  /**
   * @param includeTemplateFieldsInd
   */
  public void
    setIncludeTemplateFieldsInd(final boolean includeTemplateFieldsInd) {

    this.includeTemplateFieldsInd = includeTemplateFieldsInd;
  }

  /**
   * @return the getMandatoryFieldsInd
   */
  public boolean isGetMandatoryFieldsInd() {

    return this.getMandatoryFieldsInd;
  }

  /**
   * @param getMandatoryFieldsInd the getMandatoryFieldsInd to set
   */
  public void setGetMandatoryFieldsInd(final boolean getMandatoryFieldsInd) {

    this.getMandatoryFieldsInd = getMandatoryFieldsInd;
  }
}
