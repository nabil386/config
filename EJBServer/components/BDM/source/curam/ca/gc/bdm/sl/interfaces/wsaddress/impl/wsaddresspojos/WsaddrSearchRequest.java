
package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WsaddrSearchRequest {

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

  @SerializedName("nc:LanguageName")
  @Expose
  private String ncLanguageName;

  @SerializedName("wsaddr:ReturnAddressLineText")
  @Expose
  private String wsaddrReturnAddressLineText;

  /**
   * No args constructor for use in serialization
   *
   */
  public WsaddrSearchRequest() {

  }

  /**
   *
   * @param ncCountryCode
   * @param ncAddressFullText
   * @param wsaddrReturnAddressLineText
   * @param ncAddressPostalCode
   * @param ncAddressCityName
   * @param ncLanguageName
   * @param canProvinceCode
   */
  public WsaddrSearchRequest(final String ncAddressFullText,
    final String ncAddressCityName, final String canProvinceCode,
    final String ncAddressPostalCode, final String ncCountryCode,
    final String ncLanguageName, final String wsaddrReturnAddressLineText) {

    super();
    this.ncAddressFullText = ncAddressFullText;
    this.ncAddressCityName = ncAddressCityName;
    this.canProvinceCode = canProvinceCode;
    this.ncAddressPostalCode = ncAddressPostalCode;
    this.ncCountryCode = ncCountryCode;
    this.ncLanguageName = ncLanguageName;
    this.wsaddrReturnAddressLineText = wsaddrReturnAddressLineText;
  }

  public String getNcAddressFullText() {

    return ncAddressFullText;
  }

  public void setNcAddressFullText(final String ncAddressFullText) {

    this.ncAddressFullText = ncAddressFullText;
  }

  public WsaddrSearchRequest
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

  public WsaddrSearchRequest
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

  public WsaddrSearchRequest
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

  public WsaddrSearchRequest
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

  public WsaddrSearchRequest withNcCountryCode(final String ncCountryCode) {

    this.ncCountryCode = ncCountryCode;
    return this;
  }

  public String getNcLanguageName() {

    return ncLanguageName;
  }

  public void setNcLanguageName(final String ncLanguageName) {

    this.ncLanguageName = ncLanguageName;
  }

  public WsaddrSearchRequest withNcLanguageName(final String ncLanguageName) {

    this.ncLanguageName = ncLanguageName;
    return this;
  }

  public String getWsaddrReturnAddressLineText() {

    return wsaddrReturnAddressLineText;
  }

  public void
    setWsaddrReturnAddressLineText(final String wsaddrReturnAddressLineText) {

    this.wsaddrReturnAddressLineText = wsaddrReturnAddressLineText;
  }

  public WsaddrSearchRequest withWsaddrReturnAddressLineText(
    final String wsaddrReturnAddressLineText) {

    this.wsaddrReturnAddressLineText = wsaddrReturnAddressLineText;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(WsaddrSearchRequest.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
    sb.append("ncLanguageName");
    sb.append('=');
    sb.append(this.ncLanguageName == null ? "<null>" : this.ncLanguageName);
    sb.append(',');
    sb.append("wsaddrReturnAddressLineText");
    sb.append('=');
    sb.append(this.wsaddrReturnAddressLineText == null ? "<null>"
      : this.wsaddrReturnAddressLineText);
    sb.append(',');
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.setCharAt(sb.length() - 1, ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

}
