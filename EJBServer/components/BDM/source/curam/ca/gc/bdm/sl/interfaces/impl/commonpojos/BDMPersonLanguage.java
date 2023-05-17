package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Person Language Details POJO
 */

public class BDMPersonLanguage {

  @SerializedName(value = "LanguageCode")
  private BDMReferenceData languageCode;

  @SerializedName(value = "CommunicationCategoryCode")
  private BDMReferenceData commCategoryCode;

  @SerializedName(value = "PreferredIndicator")
  private String preferredIndicator;

  /**
   * @return the languageCode
   */
  public BDMReferenceData getLanguageCode() {

    return this.languageCode;
  }

  /**
   * @param languageCode the languageCode to set
   */
  public void setLanguageCode(final BDMReferenceData languageCode) {

    this.languageCode = languageCode;
  }

  /**
   * @return the commCategoryCode
   */
  public BDMReferenceData getCommCategoryCode() {

    return this.commCategoryCode;
  }

  /**
   * @param commCategoryCode the commCategoryCode to set
   */
  public void setCommCategoryCode(final BDMReferenceData commCategoryCode) {

    this.commCategoryCode = commCategoryCode;
  }

  /**
   * @return the preferredIndicator
   */
  public String getPreferredIndicator() {

    return this.preferredIndicator;
  }

  /**
   * @param preferredIndicator the preferredIndicator to set
   */
  public void setPreferredIndicator(final String preferredIndicator) {

    this.preferredIndicator = preferredIndicator;
  }

}
