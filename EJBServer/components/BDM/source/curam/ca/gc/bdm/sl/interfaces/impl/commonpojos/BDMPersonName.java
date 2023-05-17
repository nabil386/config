package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Person Name Details POJO.
 */
public class BDMPersonName {

  @SerializedName(value = "PersonGivenName")
  private String[] personGivenName;

  @SerializedName(value = "PersonSurName")
  private String personSurname;

  @SerializedName(value = "PersonNameInitialsText")
  private String personInitials;

  @SerializedName(value = "PersonFullName")
  private String personFullName;

  @SerializedName(value = "PersonNamePrefixText")
  private String personNamePrefixText;

  @SerializedName(value = "PersonMiddleName")
  private String[] personMiddleName;

  @SerializedName(value = "PersonNameCategoryCode")
  private BDMReferenceData personNameCategoryCode;

  private BDMPersonBirthName personBirthName;

  /**
   * @return the personGivenName list
   */
  public String[] getPersonGivenName() {

    return this.personGivenName;
  }

  /**
   * @param personGivenName the personGivenName list to set
   */
  public void setPersonGivenName(final String[] personGivenName) {

    this.personGivenName = personGivenName;
  }

  /**
   * @return the personSurname
   */
  public String getPersonSurname() {

    return this.personSurname;
  }

  /**
   * @param personSurname the personSurname to set
   */
  public void setPersonSurname(final String personSurname) {

    this.personSurname = personSurname;
  }

  /**
   * @return the person Initials
   */
  public String getPersonInitials() {

    return this.personInitials;
  }

  /**
   * @param personInitials the person initials to set
   */
  public void setPersonInitials(final String personInitials) {

    this.personInitials = personInitials;
  }

  /**
   * @return person FullName
   */
  public String getPersonFullName() {

    return this.personFullName;
  }

  /**
   * @param personFullname the personFullname to set
   */
  public void setPersonFullName(final String personFullName) {

    this.personFullName = personFullName;
  }

  /**
   * @return personNamePrefixText
   */
  public String getPersonNamePrefixText() {

    return this.personNamePrefixText;
  }

  /**
   * @param personNamePrefixText
   */
  public void setPersonNamePrefixText(final String personNamePrefixText) {

    this.personNamePrefixText = personNamePrefixText;
  }

  /**
   * @return personBirthName
   */
  public BDMPersonBirthName getPersonBirthName() {

    return this.personBirthName;
  }

  /**
   * @param personBirthName the personBirthName to set
   */
  public void setPersonBirthName(final BDMPersonBirthName personBirthName) {

    this.personBirthName = personBirthName;
  }

  /**
   * @return personMiddleName
   */
  public String[] getPersonMiddleName() {

    return this.personMiddleName;
  }

  /**
   * @param personMiddleName
   */
  public void setPersonMiddleName(final String[] personMiddleName) {

    this.personMiddleName = personMiddleName;
  }

  /**
   * @return personNameCategoryCode
   */
  public BDMReferenceData getPersonNameCategoryCode() {

    return this.personNameCategoryCode;
  }

  /**
   * @param personNameCategoryCode
   */
  public void
    setPersonNameCategoryCode(final BDMReferenceData personNameCategoryCode) {

    this.personNameCategoryCode = personNameCategoryCode;
  }

}
