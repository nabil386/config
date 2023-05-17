package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Telephone Details POJO
 */
public class BDMTelephone {

  @SerializedName(value = "IPTNPTelephoneNumber")
  private BDMTelephoneNumber telephoneNumber;

  @SerializedName(value = "TelephoneNumberCategoryCode")
  private BDMReferenceData telephoneNumCategoryCode;

  @SerializedName(value = "TelephoneNumberEffectiveDateRange")
  private BDMEffectiveDateRange effectiveDateRange;

  /**
   * Getter - telephoneNumber
   */
  public BDMTelephoneNumber getTelephoneNumber() {

    return this.telephoneNumber;
  }

  /**
   * Setter - telephoneNumber
   */
  public void setTelephoneNumber(final BDMTelephoneNumber telephoneNumber) {

    this.telephoneNumber = telephoneNumber;
  }

  /**
   * Getter - telephoneNumCategoryCode
   */
  public BDMReferenceData getTelephoneNumCategoryCode() {

    return this.telephoneNumCategoryCode;
  }

  /**
   * Setter - telephoneNumCategoryCode
   */
  public void setTelephoneNumCategoryCoder(
    final BDMReferenceData telephoneNumCategoryCode) {

    this.telephoneNumCategoryCode = telephoneNumCategoryCode;
  }

  /**
   * Getter - effectiveDateRange
   */
  public BDMEffectiveDateRange getEffectiveDateRange() {

    return this.effectiveDateRange;
  }

  /**
   * Setter - effectiveDateRange
   */
  public void
    setEffectiveDateRange(final BDMEffectiveDateRange effectiveDateRange) {

    this.effectiveDateRange = effectiveDateRange;
  }

}
