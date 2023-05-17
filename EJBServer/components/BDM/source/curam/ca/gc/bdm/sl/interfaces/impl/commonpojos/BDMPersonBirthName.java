package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Person Birth Name POJO
 */
public class BDMPersonBirthName {

  @SerializedName(value = "PersonNameCategoryCode")
  private BDMReferenceData personNameCategoryCode;

  @SerializedName(value = "PersonSurName")
  private String personSurName;

  /**
   * Getter - personNameCategoryCode
   */
  public BDMReferenceData getPersonNameCategoryCode() {

    return this.personNameCategoryCode;
  }

  /**
   * Setter - personNameCategoryCode
   */
  public void
    setPersonNameCategoryCode(final BDMReferenceData personNameCategoryCode) {

    this.personNameCategoryCode = personNameCategoryCode;
  }

  /**
   * Getter - personSurName
   */
  public String getPersonSurName() {

    return this.personSurName;
  }

  /**
   * Setter - personSurName
   */
  public void setPersonSurName(final String personSurName) {

    this.personSurName = personSurName;
  }

}
