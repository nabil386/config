package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Person Data POJO.
 */
public class BDMClientData {

  @SerializedName(value = "PersonName")
  private List<BDMPersonName> personNameList;

  @SerializedName(value = "PersonBirthDate")
  private BDMDate dateOfBirth;

  @SerializedName(value = "PersonDeathDate")
  private BDMDate dateOfDeath;

  @SerializedName(value = "PersonSINIdentification")
  private BDMSINData personSINIdentification;

  @SerializedName(value = "PersonGenderCode")
  private BDMReferenceData personGenderCode;

  @SerializedName(value = "ClientStatus")
  private BDMStatus personStatus;

  @SerializedName(value = "PersonContactInformation")
  private List<BDMContactInfo> personContactInfoList;

  @SerializedName(value = "PersonMaritalStatus")
  private BDMStatus maritalStatus;

  @SerializedName(value = "PersonLanguage")
  private List<BDMPersonLanguage> personLanguageList;

  @SerializedName(value = "ClientIdentification")
  private List<BDMClientIdentification> clientIdentificationList;

  /**
   * @return the personGenderText
   */
  public BDMReferenceData getPersonGenderCode() {

    return this.personGenderCode;
  }

  /**
   * @param personGenderText
   */
  public void setPersonGenderCode(final BDMReferenceData personGenderCode) {

    this.personGenderCode = personGenderCode;
  }

  /**
   * @return the personStatus
   */
  public BDMStatus getPersonStatus() {

    return this.personStatus;
  }

  /**
   * @param personStatus
   */
  public void setPersonStatus(final BDMStatus personStatus) {

    this.personStatus = personStatus;
  }

  /**
   * @return the personContactInfoList
   */
  public List<BDMContactInfo> getPersonContactInfoList() {

    return this.personContactInfoList;
  }

  /**
   * @param personContactInfoList
   */
  public void
    setPersonContactInfo(final List<BDMContactInfo> personContactInfoList) {

    this.personContactInfoList = personContactInfoList;
  }

  /**
   * @return the maritalStatusCode
   */
  public BDMStatus getMaritalStatus() {

    return this.maritalStatus;
  }

  /**
   * @param maritalStatusCode
   */
  public void setMaritalStatus(final BDMStatus maritalStatus) {

    this.maritalStatus = maritalStatus;
  }

  /**
   * @return personNameList
   */
  public List<BDMPersonName> getPersonNameList() {

    return this.personNameList;
  }

  /**
   * @param personNameList
   */
  public void setPersonNameList(final List<BDMPersonName> personNameList) {

    this.personNameList = personNameList;
  }

  /**
   * @return dateOfBirth
   */
  public BDMDate getDateOfBirth() {

    return this.dateOfBirth;
  }

  /**
   * @param dateOfBirth
   */
  public void setDateOfBirth(final BDMDate dateOfBirth) {

    this.dateOfBirth = dateOfBirth;
  }

  /**
   * @return dateOfDeath
   */
  public BDMDate getDateOfDeath() {

    return this.dateOfDeath;
  }

  /**
   * @param dateOfDeath
   */
  public void setDateOfDeath(final BDMDate dateOfDeath) {

    this.dateOfDeath = dateOfDeath;
  }

  /**
   * @return personSINIdentification
   */
  public BDMSINData getPersonSINIdentification() {

    return this.personSINIdentification;
  }

  /**
   * @param personSINIdentification
   */
  public void
    setPersonSINIdentification(final BDMSINData personSINIdentification) {

    this.personSINIdentification = personSINIdentification;
  }

  /**
   * @return the personLanguageList
   */
  public List<BDMPersonLanguage> getPersonLanguageList() {

    return this.personLanguageList;
  }

  /**
   * @param personLanguageList
   */
  public void
    setPersonLanguageList(final List<BDMPersonLanguage> personLanguageList) {

    this.personLanguageList = personLanguageList;
  }

  /**
   * @return clientIdentificationList
   */
  public List<BDMClientIdentification> getClientIdentificationList() {

    return this.clientIdentificationList;
  }

  /**
   * @return clientIdentification
   */
  public void setClientIdentificationList(
    final List<BDMClientIdentification> clientIdentificationList) {

    this.clientIdentificationList = clientIdentificationList;
  }

}
