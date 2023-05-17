package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Client Identification POJO
 */
public class BDMClientIdentification {

  @SerializedName(value = "IdentificationID")
  private String identificationID;

  @SerializedName(value = "IdentificationCategoryText")
  private String categoryText;

  /**
   * Getter - identificationID
   */
  public String getIdentificationID() {

    return this.identificationID;
  }

  /**
   * Setter - identificationID
   */
  public void setIdentificationID(final String identificationID) {

    this.identificationID = identificationID;
  }

  /**
   * Getter - categoryText
   */
  public String getCategoryText() {

    return this.categoryText;
  }

  /**
   * Setter - categoryText
   */
  public void setCategoryText(final String categoryText) {

    this.categoryText = categoryText;
  }

}
