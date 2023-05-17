package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WsaddrValidationRequest {

  @SerializedName("nc:LanguageName")
  @Expose
  private String ncLanguageName;

  @SerializedName("nc:AddressFullText")
  @Expose
  private String ncAddressFullText;

  @SerializedName("nc:AddressCityName")
  @Expose
  private String ncAddressCityName;

  @SerializedName("can:ProvinceCode")
  @Expose
  private String canProvinceCode;

  @SerializedName("nc:AddressPostalCode")
  @Expose
  private String ncAddressPostalCode;

  @SerializedName("nc:CountryCode")
  @Expose
  private String ncCountryCode;

  /**
   * No args constructor for use in serialization
   * 
   */
  public WsaddrValidationRequest() {

  }

  /**
   * 
   * @param ncCountryCode
   * @param ncAddressFullText
   * @param ncAddressPostalCode
   * @param ncLanguageName
   * @param ncAddressCityName
   * @param canProvinceCode
   */
  public WsaddrValidationRequest(final String ncLanguageName,
    final String ncAddressFullText, final String ncAddressCityName,
    final String canProvinceCode, final String ncAddressPostalCode,
    final String ncCountryCode) {

    super();
    this.ncLanguageName = ncLanguageName;
    this.ncAddressFullText = ncAddressFullText;
    this.ncAddressCityName = ncAddressCityName;
    this.canProvinceCode = canProvinceCode;
    this.ncAddressPostalCode = ncAddressPostalCode;
    this.ncCountryCode = ncCountryCode;
  }

  public String getNcLanguageName() {

    return ncLanguageName;
  }

  public void setNcLanguageName(final String ncLanguageName) {

    this.ncLanguageName = ncLanguageName;
  }

  public WsaddrValidationRequest
    withNcLanguageName(final String ncLanguageName) {

    this.ncLanguageName = ncLanguageName;
    return this;
  }

  public String getNcAddressFullText() {

    return ncAddressFullText;
  }

  public void setNcAddressFullText(final String ncAddressFullText) {

    this.ncAddressFullText = ncAddressFullText;
  }

  public WsaddrValidationRequest
    withNcAddressFullText(final String ncAddressFullText) {

    this.ncAddressFullText = ncAddressFullText;
    return this;
  }

  public String getNcAddressCityName() {

    return ncAddressCityName;
  }

  public void setNcAddressCityName(final String ncAddressCityName) {

    this.ncAddressCityName = ncAddressCityName;
  }

  public WsaddrValidationRequest
    withNcAddressCityName(final String ncAddressCityName) {

    this.ncAddressCityName = ncAddressCityName;
    return this;
  }

  public String getCanProvinceCode() {

    return canProvinceCode;
  }

  public void setCanProvinceCode(final String canProvinceCode) {

    this.canProvinceCode = canProvinceCode;
  }

  public WsaddrValidationRequest
    withCanProvinceCode(final String canProvinceCode) {

    this.canProvinceCode = canProvinceCode;
    return this;
  }

  public String getNcAddressPostalCode() {

    return ncAddressPostalCode;
  }

  public void setNcAddressPostalCode(final String ncAddressPostalCode) {

    this.ncAddressPostalCode = ncAddressPostalCode;
  }

  public WsaddrValidationRequest
    withNcAddressPostalCode(final String ncAddressPostalCode) {

    this.ncAddressPostalCode = ncAddressPostalCode;
    return this;
  }

  public String getNcCountryCode() {

    return ncCountryCode;
  }

  public void setNcCountryCode(final String ncCountryCode) {

    this.ncCountryCode = ncCountryCode;
  }

  public WsaddrValidationRequest
    withNcCountryCode(final String ncCountryCode) {

    this.ncCountryCode = ncCountryCode;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(WsaddrValidationRequest.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("ncLanguageName");
    sb.append('=');
    sb.append(this.ncLanguageName == null ? "<null>" : this.ncLanguageName);
    sb.append(',');
    sb.append("ncAddressFullText");
    sb.append('=');
    sb.append(
      this.ncAddressFullText == null ? "<null>" : this.ncAddressFullText);
    sb.append(',');
    sb.append("ncAddressCityName");
    sb.append('=');
    sb.append(
      this.ncAddressCityName == null ? "<null>" : this.ncAddressCityName);
    sb.append(',');
    sb.append("canProvinceCode");
    sb.append('=');
    sb.append(this.canProvinceCode == null ? "<null>" : this.canProvinceCode);
    sb.append(',');
    sb.append("ncAddressPostalCode");
    sb.append('=');
    sb.append(
      this.ncAddressPostalCode == null ? "<null>" : this.ncAddressPostalCode);
    sb.append(',');
    sb.append("ncCountryCode");
    sb.append('=');
    sb.append(this.ncCountryCode == null ? "<null>" : this.ncCountryCode);
    sb.append(',');
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.setCharAt(sb.length() - 1, ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

}
