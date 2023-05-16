package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters needed in the request for the CreateCorrespondence API call
 */
public class BDMCCTCreateCorrespondenceRequest {

  @SerializedName(value = "Community")
  private String community;

  @SerializedName(value = "UserId")
  private String userID;

  @SerializedName(value = "TemplateId")
  private long templateID;

  @SerializedName(value = "TemplateFullPath")
  private String templateFullPath;

  @SerializedName(value = "DataMapName")
  private String dataMapName;

  @SerializedName(value = "DataXML")
  private String dataXML;

  @SerializedName(value = "SubmitOnCreate")
  private boolean submitOnCreate;

  @SerializedName(value = "IsComplete")
  private boolean isComplete;

  @SerializedName(value = "InitialAssigneeName")
  private String initialAssigneeName;

  @SerializedName(value = "DeliveryOptionName")
  private String deliveryOptionName;

  @SerializedName(value = "CorrespondenceMode")
  private String correspondenceMode;

  @SerializedName(value = "RedirectToAwaitingDelivery")
  private boolean redirectToAwaitingDelivery;

  @SerializedName(value = "AutoCloseEditor")
  private boolean autoCloseEditor;

  @SerializedName(value = "EditorRedirectUrl")
  private String editorRedirectURL;

  @SerializedName(value = "OnlyArchive")
  private boolean onlyArchive;

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
   * @return the userID
   */
  public String getUserID() {

    return this.userID;
  }

  /**
   * @param userID the userID to set
   */
  public void setUserID(final String userID) {

    this.userID = userID;
  }

  /**
   * @return the templateID
   */
  public long getTemplateID() {

    return this.templateID;
  }

  /**
   * @param templateID the templateID to set
   */
  public void setTemplateID(final long templateID) {

    this.templateID = templateID;
  }

  /**
   * @return the templateFullPath
   */
  public String getTemplateFullPath() {

    return this.templateFullPath;
  }

  /**
   * @param templateFullPath the templateFullPath to set
   */
  public void setTemplateFullPath(final String templateFullPath) {

    this.templateFullPath = templateFullPath;
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

  /**
   * @return the submitOnCreate
   */
  public boolean isSubmitOnCreate() {

    return this.submitOnCreate;
  }

  /**
   * @return the isComplete
   */
  public boolean getIsComplete() {

    return this.isComplete;
  }

  /**
   * @param isComplete the isComplete to set
   */
  public void setIsComplete(final boolean isComplete) {

    this.isComplete = isComplete;
  }

  /**
   * @param submitOnCreate the submitOnCreate to set
   */
  public void setSubmitOnCreate(final boolean submitOnCreate) {

    this.submitOnCreate = submitOnCreate;
  }

  /**
   * @return the initialAssigneeName
   */
  public String getInitialAssigneeName() {

    return this.initialAssigneeName;
  }

  /**
   * @param initialAssigneeName the initialAssigneeName to set
   */
  public void setInitialAssigneeName(final String initialAssigneeName) {

    this.initialAssigneeName = initialAssigneeName;
  }

  /**
   * @return the deliveryOptionName
   */
  public String getDeliveryOptionName() {

    return this.deliveryOptionName;
  }

  /**
   * @param deliveryOptionName the deliveryOptionName to set
   */
  public void setDeliveryOptionName(final String deliveryOptionName) {

    this.deliveryOptionName = deliveryOptionName;
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
   * @return the redirectToAwaitingDelivery
   */
  public boolean isRedirectToAwaitingDelivery() {

    return this.redirectToAwaitingDelivery;
  }

  /**
   * @param redirectToAwaitingDelivery the redirectToAwaitingDelivery to set
   */
  public void
    setRedirectToAwaitingDelivery(final boolean redirectToAwaitingDelivery) {

    this.redirectToAwaitingDelivery = redirectToAwaitingDelivery;
  }

  /**
   * @return the autoCloseEditor
   */
  public boolean isAutoCloseEditor() {

    return this.autoCloseEditor;
  }

  /**
   * @param autoCloseEditor the autoCloseEditor to set
   */
  public void setAutoCloseEditor(final boolean autoCloseEditor) {

    this.autoCloseEditor = autoCloseEditor;
  }

  /**
   * @return the editorRedirectUrl
   */
  public String getEditorRedirectURL() {

    return this.editorRedirectURL;
  }

  /**
   * @param editorRedirectUrl the editorRedirectUrl to set
   */
  public void setEditorRedirectURL(final String editorRedirectURL) {

    this.editorRedirectURL = editorRedirectURL;
  }

  /**
   * @return the onlyArchive
   */
  public boolean isOnlyArchive() {

    return this.onlyArchive;
  }

  /**
   * @param onlyArchive the onlyArchive to set
   */
  public void setOnlyArchive(final boolean onlyArchive) {

    this.onlyArchive = onlyArchive;
  }

}
