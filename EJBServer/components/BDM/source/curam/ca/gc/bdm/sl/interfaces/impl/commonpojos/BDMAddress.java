package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Address Details POJO
 */
public class BDMAddress {

  @SerializedName(value = "AddressProvince")
  private BDMAddressProvince addressProvince;

  @SerializedName(value = "AddressCountry")
  private BDMAddressCountry addressCountry;

  @SerializedName(value = "AddressFullText ")
  private String[] addressFullText;

  @SerializedName(value = "AddressPostalCode ")
  private String addressPostalCode;

  @SerializedName(value = "AddressCategoryCode")
  private BDMReferenceData addressCategoryCode;

  @SerializedName(value = "AddressSecondaryUnitText ")
  private String addressSecondaryUnit;

  @SerializedName(value = "AddressCityName ")
  private String addressCityName;

  @SerializedName(value = "AddressPrivateMailboxText ")
  private String addressPrivateMailbox;

  @SerializedName(value = "AddressStreet ")
  private BDMAddressStreet addressStreet;

  @SerializedName(value = "AddressEffectiveDateRange ")
  private BDMEffectiveDateRange effectiveDateRange;

  /**
   * Getter - addressProvince
   */
  public BDMAddressProvince getAddressProvince() {

    return this.addressProvince;
  }

  /**
   * Setter - addressProvince
   */
  public void setAddressProvince(final BDMAddressProvince addressProvince) {

    this.addressProvince = addressProvince;
  }

  /**
   * Getter - addressCountry
   */
  public BDMAddressCountry getAddressCountry() {

    return this.addressCountry;
  }

  /**
   * Setter - addressCountry
   */
  public void setAddressCountry(final BDMAddressCountry addressCountry) {

    this.addressCountry = addressCountry;
  }

  /**
   * Getter - addressFullText
   */
  public String[] getAddressFullText() {

    return this.addressFullText;
  }

  /**
   * Setter - addressFullText
   */
  public void setAddressFullText(final String[] addressFullText) {

    this.addressFullText = addressFullText;
  }

  /**
   * Getter - addressPostalCode
   */
  public String getAddressPostalCode() {

    return this.addressPostalCode;
  }

  /**
   * Setter - addressPostalCode
   */
  public void setAddressPostalCode(final String addressPostalCode) {

    this.addressPostalCode = addressPostalCode;
  }

  /**
   * Getter - addressCategoryCode
   */
  public BDMReferenceData getAddressCategoryCode() {

    return this.addressCategoryCode;
  }

  /**
   * Setter - addressCategoryCode
   */
  public void
    setAddressCategoryCode(final BDMReferenceData addressCategoryCode) {

    this.addressCategoryCode = addressCategoryCode;
  }

  /**
   * Getter - addressSecondaryUnit
   */
  public String getAddressSecondaryUnit() {

    return this.addressSecondaryUnit;
  }

  /**
   * Setter - addressSecondaryUnit
   */
  public void setAddressSecondaryUnit(final String addressSecondaryUnit) {

    this.addressSecondaryUnit = addressSecondaryUnit;
  }

  /**
   * Getter - addressCityName
   */
  public String getAddressCityName() {

    return this.addressCityName;
  }

  /**
   * Setter - addressCityName
   */
  public void setAddressCityName(final String addressCityName) {

    this.addressCityName = addressCityName;
  }

  /**
   * Getter - addressPrivateMailbox
   */
  public String getAddressPrivateMailbox() {

    return this.addressPrivateMailbox;
  }

  /**
   * Setter - addressPrivateMailbox
   */
  public void setAddressPrivateMailbox(final String addressPrivateMailbox) {

    this.addressPrivateMailbox = addressPrivateMailbox;
  }

  /**
   * Getter - addressStreet
   */
  public BDMAddressStreet getAddressStreet() {

    return this.addressStreet;
  }

  /**
   * Setter - addressStreet
   */
  public void setAddressStreet(final BDMAddressStreet addressStreet) {

    this.addressStreet = addressStreet;
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
    setEffectiveDateRanget(final BDMEffectiveDateRange effectiveDateRange) {

    this.effectiveDateRange = effectiveDateRange;
  }
}
