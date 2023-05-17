package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Person Contact Information POJO.
 */
public class BDMContactInfo {

  @SerializedName(value = "ContactMailingAddress")
  private BDMAddress contactMailingAddress;

  @SerializedName(value = "ContactTelephoneNumber")
  private BDMTelephone contactTelephone;

  @SerializedName(value = "ContactInformationDescriptionText")
  private String contactInformationDescription;

  /**
   * Getter - contactMailingAddress
   */
  public BDMAddress getContactMailingAddress() {

    return this.contactMailingAddress;
  }

  /**
   * Setter - contactMailingAddress
   */
  public void
    setContactMailingAddress(final BDMAddress contactMailingAddress) {

    this.contactMailingAddress = contactMailingAddress;
  }

  /**
   * Getter - the contactTelephone
   */
  public BDMTelephone getContactTelephone() {

    return this.contactTelephone;
  }

  /**
   * Setter - the contactTelephone
   */
  public void setContactTelephone(final BDMTelephone contactTelephone) {

    this.contactTelephone = contactTelephone;
  }

  /**
   * Getter - contactInformationDescription
   */
  public String getContactInformationDescription() {

    return this.contactInformationDescription;
  }

  /**
   * Setter - contactInformationDescription
   */
  public void setContactInformationDescription(
    final String contactInformationDescription) {

    this.contactInformationDescription = contactInformationDescription;
  }

}
