
package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WsaddrAddressMatch {

  @SerializedName("nc:AddressCategoryText")
  @Expose
  private String ncAddressCategoryText;

  @SerializedName("wsaddr:DirectoryAreaName")
  @Expose
  private String wsaddrDirectoryAreaName;

  @SerializedName("nc:AddressCityName")
  @Expose
  private String ncAddressCityName;

  @SerializedName("can:ProvinceCode")
  @Expose
  private String canProvinceCode;

  @SerializedName("nc:CountryCode")
  @Expose
  private String ncCountryCode;

  @SerializedName("nc:AddressPostalCode")
  @Expose
  private String ncAddressPostalCode;

  @SerializedName("wsaddr:StreetAddressSequence")
  @Expose
  private String wsaddrStreetAddressSequence;

  @SerializedName("can:StreetDirectionalCode")
  @Expose
  private String canStreetDirectionalCode;

  @SerializedName("nc:StreetName")
  @Expose
  private String ncStreetName;

  @SerializedName("wsaddr:StreetNumberMinimumText")
  @Expose
  private String wsaddrStreetNumberMinimumText;

  @SerializedName("wsaddr:StreetNumberMaximumText")
  @Expose
  private String wsaddrStreetNumberMaximumText;

  @SerializedName("wsaddr:StreetNumberSuffixMinimumText")
  @Expose
  private Object wsaddrStreetNumberSuffixMinimumText;

  @SerializedName("wsaddr:StreetNumberSuffixMaximumText")
  @Expose
  private Object wsaddrStreetNumberSuffixMaximumText;

  @SerializedName("can:StreetCategoryCode")
  @Expose
  private String canStreetCategoryCode;

  @SerializedName("wsaddr:SuiteNumberMinimumText")
  @Expose
  private String wsaddrSuiteNumberMinimumText;

  @SerializedName("wsaddr:SuiteNumberMaximumText")
  @Expose
  private String wsaddrSuiteNumberMaximumText;

  @SerializedName("nc:AddressFullText")
  @Expose
  private String ncAddressFullText;

  @SerializedName("wsaddr:LockBoxNumberMinimumText")
  @Expose
  private String wsaddrLockBoxNumberMinimumText;

  @SerializedName("wsaddr:LockBoxNumberMaximumText")
  @Expose
  private String wsaddrLockBoxNumberMaximumText;

  @SerializedName("wsaddr:DeliveryInstallationAreaName")
  @Expose
  private Object wsaddrDeliveryInstallationAreaName;

  @SerializedName("wsaddr:DeliveryInstallationDescription")
  @Expose
  private String wsaddrDeliveryInstallationDescription;

  @SerializedName("wsaddr:DeliveryInstallationQualifierName")
  @Expose
  private Object wsaddrDeliveryInstallationQualifierName;

  @SerializedName("wsaddr:RouteServiceCategory")
  @Expose
  private String wsaddrRouteServiceCategory;

  @SerializedName("wsaddr:RouteServiceNumber")
  @Expose
  private String wsaddrRouteServiceNumber;

  @SerializedName("wsaddr:RuralRouteServiceCategory")
  @Expose
  private String wsaddrRuralRouteServiceCategory;

  @SerializedName("wsaddr:RuralRouteServiceNumber")
  @Expose
  private String wsaddrRuralRouteServiceNumber;

  /**
   * No args constructor for use in serialization
   *
   */
  public WsaddrAddressMatch() {

  }

  /**
   *
   * @param wsaddrDeliveryInstallationQualifierName
   * @param wsaddrStreetNumberSuffixMinimumText
   * @param wsaddrStreetAddressSequence
   * @param wsaddrDeliveryInstallationDescription
   * @param wsaddrStreetNumberMaximumText
   * @param wsaddrSuiteNumberMinimumText
   * @param ncAddressFullText
   * @param ncAddressPostalCode
   * @param wsaddrRouteServiceCategory
   * @param wsaddrStreetNumberSuffixMaximumText
   * @param ncStreetName
   * @param wsaddrDeliveryInstallationAreaName
   * @param canStreetCategoryCode
   * @param wsaddrDirectoryAreaName
   * @param wsaddrRuralRouteServiceNumber
   * @param wsaddrRuralRouteServiceCategory
   * @param wsaddrLockBoxNumberMaximumText
   * @param ncAddressCityName
   * @param wsaddrLockBoxNumberMinimumText
   * @param wsaddrSuiteNumberMaximumText
   * @param canStreetDirectionalCode
   * @param ncCountryCode
   * @param ncAddressCategoryText
   * @param wsaddrStreetNumberMinimumText
   * @param canProvinceCode
   * @param wsaddrRouteServiceNumber
   */
  public WsaddrAddressMatch(final String ncAddressCategoryText,
    final String wsaddrDirectoryAreaName, final String ncAddressCityName,
    final String canProvinceCode, final String ncCountryCode,
    final String ncAddressPostalCode,
    final String wsaddrStreetAddressSequence,
    final String canStreetDirectionalCode, final String ncStreetName,
    final String wsaddrStreetNumberMinimumText,
    final String wsaddrStreetNumberMaximumText,
    final Object wsaddrStreetNumberSuffixMinimumText,
    final Object wsaddrStreetNumberSuffixMaximumText,
    final String canStreetCategoryCode,
    final String wsaddrSuiteNumberMinimumText,
    final String wsaddrSuiteNumberMaximumText, final String ncAddressFullText,
    final String wsaddrLockBoxNumberMinimumText,
    final String wsaddrLockBoxNumberMaximumText,
    final Object wsaddrDeliveryInstallationAreaName,
    final String wsaddrDeliveryInstallationDescription,
    final Object wsaddrDeliveryInstallationQualifierName,
    final String wsaddrRouteServiceCategory,
    final String wsaddrRouteServiceNumber,
    final String wsaddrRuralRouteServiceCategory,
    final String wsaddrRuralRouteServiceNumber) {

    super();
    this.ncAddressCategoryText = ncAddressCategoryText;
    this.wsaddrDirectoryAreaName = wsaddrDirectoryAreaName;
    this.ncAddressCityName = ncAddressCityName;
    this.canProvinceCode = canProvinceCode;
    this.ncCountryCode = ncCountryCode;
    this.ncAddressPostalCode = ncAddressPostalCode;
    this.wsaddrStreetAddressSequence = wsaddrStreetAddressSequence;
    this.canStreetDirectionalCode = canStreetDirectionalCode;
    this.ncStreetName = ncStreetName;
    this.wsaddrStreetNumberMinimumText = wsaddrStreetNumberMinimumText;
    this.wsaddrStreetNumberMaximumText = wsaddrStreetNumberMaximumText;
    this.wsaddrStreetNumberSuffixMinimumText =
      wsaddrStreetNumberSuffixMinimumText;
    this.wsaddrStreetNumberSuffixMaximumText =
      wsaddrStreetNumberSuffixMaximumText;
    this.canStreetCategoryCode = canStreetCategoryCode;
    this.wsaddrSuiteNumberMinimumText = wsaddrSuiteNumberMinimumText;
    this.wsaddrSuiteNumberMaximumText = wsaddrSuiteNumberMaximumText;
    this.ncAddressFullText = ncAddressFullText;
    this.wsaddrLockBoxNumberMinimumText = wsaddrLockBoxNumberMinimumText;
    this.wsaddrLockBoxNumberMaximumText = wsaddrLockBoxNumberMaximumText;
    this.wsaddrDeliveryInstallationAreaName =
      wsaddrDeliveryInstallationAreaName;
    this.wsaddrDeliveryInstallationDescription =
      wsaddrDeliveryInstallationDescription;
    this.wsaddrDeliveryInstallationQualifierName =
      wsaddrDeliveryInstallationQualifierName;
    this.wsaddrRouteServiceCategory = wsaddrRouteServiceCategory;
    this.wsaddrRouteServiceNumber = wsaddrRouteServiceNumber;
    this.wsaddrRuralRouteServiceCategory = wsaddrRuralRouteServiceCategory;
    this.wsaddrRuralRouteServiceNumber = wsaddrRuralRouteServiceNumber;
  }

  public String getNcAddressCategoryText() {

    return ncAddressCategoryText;
  }

  public void setNcAddressCategoryText(final String ncAddressCategoryText) {

    this.ncAddressCategoryText = ncAddressCategoryText;
  }

  public WsaddrAddressMatch
    withNcAddressCategoryText(final String ncAddressCategoryText) {

    this.ncAddressCategoryText = ncAddressCategoryText;
    return this;
  }

  public String getWsaddrDirectoryAreaName() {

    return wsaddrDirectoryAreaName;
  }

  public void
    setWsaddrDirectoryAreaName(final String wsaddrDirectoryAreaName) {

    this.wsaddrDirectoryAreaName = wsaddrDirectoryAreaName;
  }

  public WsaddrAddressMatch
    withWsaddrDirectoryAreaName(final String wsaddrDirectoryAreaName) {

    this.wsaddrDirectoryAreaName = wsaddrDirectoryAreaName;
    return this;
  }

  public String getNcAddressCityName() {

    return ncAddressCityName;
  }

  public void setNcAddressCityName(final String ncAddressCityName) {

    this.ncAddressCityName = ncAddressCityName;
  }

  public WsaddrAddressMatch
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

  public WsaddrAddressMatch
    withCanProvinceCode(final String canProvinceCode) {

    this.canProvinceCode = canProvinceCode;
    return this;
  }

  public String getNcCountryCode() {

    return ncCountryCode;
  }

  public void setNcCountryCode(final String ncCountryCode) {

    this.ncCountryCode = ncCountryCode;
  }

  public WsaddrAddressMatch withNcCountryCode(final String ncCountryCode) {

    this.ncCountryCode = ncCountryCode;
    return this;
  }

  public String getNcAddressPostalCode() {

    return ncAddressPostalCode;
  }

  public void setNcAddressPostalCode(final String ncAddressPostalCode) {

    this.ncAddressPostalCode = ncAddressPostalCode;
  }

  public WsaddrAddressMatch
    withNcAddressPostalCode(final String ncAddressPostalCode) {

    this.ncAddressPostalCode = ncAddressPostalCode;
    return this;
  }

  public String getWsaddrStreetAddressSequence() {

    return wsaddrStreetAddressSequence;
  }

  public void
    setWsaddrStreetAddressSequence(final String wsaddrStreetAddressSequence) {

    this.wsaddrStreetAddressSequence = wsaddrStreetAddressSequence;
  }

  public WsaddrAddressMatch withWsaddrStreetAddressSequence(
    final String wsaddrStreetAddressSequence) {

    this.wsaddrStreetAddressSequence = wsaddrStreetAddressSequence;
    return this;
  }

  public String getCanStreetDirectionalCode() {

    return canStreetDirectionalCode;
  }

  public void
    setCanStreetDirectionalCode(final String canStreetDirectionalCode) {

    this.canStreetDirectionalCode = canStreetDirectionalCode;
  }

  public WsaddrAddressMatch
    withCanStreetDirectionalCode(final String canStreetDirectionalCode) {

    this.canStreetDirectionalCode = canStreetDirectionalCode;
    return this;
  }

  public String getNcStreetName() {

    return ncStreetName;
  }

  public void setNcStreetName(final String ncStreetName) {

    this.ncStreetName = ncStreetName;
  }

  public WsaddrAddressMatch withNcStreetName(final String ncStreetName) {

    this.ncStreetName = ncStreetName;
    return this;
  }

  public String getWsaddrStreetNumberMinimumText() {

    return wsaddrStreetNumberMinimumText;
  }

  public void setWsaddrStreetNumberMinimumText(
    final String wsaddrStreetNumberMinimumText) {

    this.wsaddrStreetNumberMinimumText = wsaddrStreetNumberMinimumText;
  }

  public WsaddrAddressMatch withWsaddrStreetNumberMinimumText(
    final String wsaddrStreetNumberMinimumText) {

    this.wsaddrStreetNumberMinimumText = wsaddrStreetNumberMinimumText;
    return this;
  }

  public String getWsaddrStreetNumberMaximumText() {

    return wsaddrStreetNumberMaximumText;
  }

  public void setWsaddrStreetNumberMaximumText(
    final String wsaddrStreetNumberMaximumText) {

    this.wsaddrStreetNumberMaximumText = wsaddrStreetNumberMaximumText;
  }

  public WsaddrAddressMatch withWsaddrStreetNumberMaximumText(
    final String wsaddrStreetNumberMaximumText) {

    this.wsaddrStreetNumberMaximumText = wsaddrStreetNumberMaximumText;
    return this;
  }

  public Object getWsaddrStreetNumberSuffixMinimumText() {

    return wsaddrStreetNumberSuffixMinimumText;
  }

  public void setWsaddrStreetNumberSuffixMinimumText(
    final Object wsaddrStreetNumberSuffixMinimumText) {

    this.wsaddrStreetNumberSuffixMinimumText =
      wsaddrStreetNumberSuffixMinimumText;
  }

  public WsaddrAddressMatch withWsaddrStreetNumberSuffixMinimumText(
    final Object wsaddrStreetNumberSuffixMinimumText) {

    this.wsaddrStreetNumberSuffixMinimumText =
      wsaddrStreetNumberSuffixMinimumText;
    return this;
  }

  public Object getWsaddrStreetNumberSuffixMaximumText() {

    return wsaddrStreetNumberSuffixMaximumText;
  }

  public void setWsaddrStreetNumberSuffixMaximumText(
    final Object wsaddrStreetNumberSuffixMaximumText) {

    this.wsaddrStreetNumberSuffixMaximumText =
      wsaddrStreetNumberSuffixMaximumText;
  }

  public WsaddrAddressMatch withWsaddrStreetNumberSuffixMaximumText(
    final Object wsaddrStreetNumberSuffixMaximumText) {

    this.wsaddrStreetNumberSuffixMaximumText =
      wsaddrStreetNumberSuffixMaximumText;
    return this;
  }

  public String getCanStreetCategoryCode() {

    return canStreetCategoryCode;
  }

  public void setCanStreetCategoryCode(final String canStreetCategoryCode) {

    this.canStreetCategoryCode = canStreetCategoryCode;
  }

  public WsaddrAddressMatch
    withCanStreetCategoryCode(final String canStreetCategoryCode) {

    this.canStreetCategoryCode = canStreetCategoryCode;
    return this;
  }

  public String getWsaddrSuiteNumberMinimumText() {

    return wsaddrSuiteNumberMinimumText;
  }

  public void setWsaddrSuiteNumberMinimumText(
    final String wsaddrSuiteNumberMinimumText) {

    this.wsaddrSuiteNumberMinimumText = wsaddrSuiteNumberMinimumText;
  }

  public WsaddrAddressMatch withWsaddrSuiteNumberMinimumText(
    final String wsaddrSuiteNumberMinimumText) {

    this.wsaddrSuiteNumberMinimumText = wsaddrSuiteNumberMinimumText;
    return this;
  }

  public String getWsaddrSuiteNumberMaximumText() {

    return wsaddrSuiteNumberMaximumText;
  }

  public void setWsaddrSuiteNumberMaximumText(
    final String wsaddrSuiteNumberMaximumText) {

    this.wsaddrSuiteNumberMaximumText = wsaddrSuiteNumberMaximumText;
  }

  public WsaddrAddressMatch withWsaddrSuiteNumberMaximumText(
    final String wsaddrSuiteNumberMaximumText) {

    this.wsaddrSuiteNumberMaximumText = wsaddrSuiteNumberMaximumText;
    return this;
  }

  public String getNcAddressFullText() {

    return ncAddressFullText;
  }

  public void setNcAddressFullText(final String ncAddressFullText) {

    this.ncAddressFullText = ncAddressFullText;
  }

  public WsaddrAddressMatch
    withNcAddressFullText(final String ncAddressFullText) {

    this.ncAddressFullText = ncAddressFullText;
    return this;
  }

  public String getWsaddrLockBoxNumberMinimumText() {

    return wsaddrLockBoxNumberMinimumText;
  }

  public void setWsaddrLockBoxNumberMinimumText(
    final String wsaddrLockBoxNumberMinimumText) {

    this.wsaddrLockBoxNumberMinimumText = wsaddrLockBoxNumberMinimumText;
  }

  public WsaddrAddressMatch withWsaddrLockBoxNumberMinimumText(
    final String wsaddrLockBoxNumberMinimumText) {

    this.wsaddrLockBoxNumberMinimumText = wsaddrLockBoxNumberMinimumText;
    return this;
  }

  public String getWsaddrLockBoxNumberMaximumText() {

    return wsaddrLockBoxNumberMaximumText;
  }

  public void setWsaddrLockBoxNumberMaximumText(
    final String wsaddrLockBoxNumberMaximumText) {

    this.wsaddrLockBoxNumberMaximumText = wsaddrLockBoxNumberMaximumText;
  }

  public WsaddrAddressMatch withWsaddrLockBoxNumberMaximumText(
    final String wsaddrLockBoxNumberMaximumText) {

    this.wsaddrLockBoxNumberMaximumText = wsaddrLockBoxNumberMaximumText;
    return this;
  }

  public Object getWsaddrDeliveryInstallationAreaName() {

    return wsaddrDeliveryInstallationAreaName;
  }

  public void setWsaddrDeliveryInstallationAreaName(
    final Object wsaddrDeliveryInstallationAreaName) {

    this.wsaddrDeliveryInstallationAreaName =
      wsaddrDeliveryInstallationAreaName;
  }

  public WsaddrAddressMatch withWsaddrDeliveryInstallationAreaName(
    final Object wsaddrDeliveryInstallationAreaName) {

    this.wsaddrDeliveryInstallationAreaName =
      wsaddrDeliveryInstallationAreaName;
    return this;
  }

  public String getWsaddrDeliveryInstallationDescription() {

    return wsaddrDeliveryInstallationDescription;
  }

  public void setWsaddrDeliveryInstallationDescription(
    final String wsaddrDeliveryInstallationDescription) {

    this.wsaddrDeliveryInstallationDescription =
      wsaddrDeliveryInstallationDescription;
  }

  public WsaddrAddressMatch withWsaddrDeliveryInstallationDescription(
    final String wsaddrDeliveryInstallationDescription) {

    this.wsaddrDeliveryInstallationDescription =
      wsaddrDeliveryInstallationDescription;
    return this;
  }

  public Object getWsaddrDeliveryInstallationQualifierName() {

    return wsaddrDeliveryInstallationQualifierName;
  }

  public void setWsaddrDeliveryInstallationQualifierName(
    final Object wsaddrDeliveryInstallationQualifierName) {

    this.wsaddrDeliveryInstallationQualifierName =
      wsaddrDeliveryInstallationQualifierName;
  }

  public WsaddrAddressMatch withWsaddrDeliveryInstallationQualifierName(
    final Object wsaddrDeliveryInstallationQualifierName) {

    this.wsaddrDeliveryInstallationQualifierName =
      wsaddrDeliveryInstallationQualifierName;
    return this;
  }

  public String getWsaddrRouteServiceCategory() {

    return wsaddrRouteServiceCategory;
  }

  public void
    setWsaddrRouteServiceCategory(final String wsaddrRouteServiceCategory) {

    this.wsaddrRouteServiceCategory = wsaddrRouteServiceCategory;
  }

  public WsaddrAddressMatch
    withWsaddrRouteServiceCategory(final String wsaddrRouteServiceCategory) {

    this.wsaddrRouteServiceCategory = wsaddrRouteServiceCategory;
    return this;
  }

  public String getWsaddrRouteServiceNumber() {

    return wsaddrRouteServiceNumber;
  }

  public void
    setWsaddrRouteServiceNumber(final String wsaddrRouteServiceNumber) {

    this.wsaddrRouteServiceNumber = wsaddrRouteServiceNumber;
  }

  public WsaddrAddressMatch
    withWsaddrRouteServiceNumber(final String wsaddrRouteServiceNumber) {

    this.wsaddrRouteServiceNumber = wsaddrRouteServiceNumber;
    return this;
  }

  public String getWsaddrRuralRouteServiceCategory() {

    return wsaddrRuralRouteServiceCategory;
  }

  public void setWsaddrRuralRouteServiceCategory(
    final String wsaddrRuralRouteServiceCategory) {

    this.wsaddrRuralRouteServiceCategory = wsaddrRuralRouteServiceCategory;
  }

  public WsaddrAddressMatch withWsaddrRuralRouteServiceCategory(
    final String wsaddrRuralRouteServiceCategory) {

    this.wsaddrRuralRouteServiceCategory = wsaddrRuralRouteServiceCategory;
    return this;
  }

  public String getWsaddrRuralRouteServiceNumber() {

    return wsaddrRuralRouteServiceNumber;
  }

  public void setWsaddrRuralRouteServiceNumber(
    final String wsaddrRuralRouteServiceNumber) {

    this.wsaddrRuralRouteServiceNumber = wsaddrRuralRouteServiceNumber;
  }

  public WsaddrAddressMatch withWsaddrRuralRouteServiceNumber(
    final String wsaddrRuralRouteServiceNumber) {

    this.wsaddrRuralRouteServiceNumber = wsaddrRuralRouteServiceNumber;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(WsaddrAddressMatch.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("ncAddressCategoryText");
    sb.append('=');
    sb.append(this.ncAddressCategoryText == null ? "<null>"
      : this.ncAddressCategoryText);
    sb.append(',');
    sb.append("wsaddrDirectoryAreaName");
    sb.append('=');
    sb.append(this.wsaddrDirectoryAreaName == null ? "<null>"
      : this.wsaddrDirectoryAreaName);
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
    sb.append("ncCountryCode");
    sb.append('=');
    sb.append(this.ncCountryCode == null ? "<null>" : this.ncCountryCode);
    sb.append(',');
    sb.append("ncAddressPostalCode");
    sb.append('=');
    sb.append(
      this.ncAddressPostalCode == null ? "<null>" : this.ncAddressPostalCode);
    sb.append(',');
    sb.append("wsaddrStreetAddressSequence");
    sb.append('=');
    sb.append(this.wsaddrStreetAddressSequence == null ? "<null>"
      : this.wsaddrStreetAddressSequence);
    sb.append(',');
    sb.append("canStreetDirectionalCode");
    sb.append('=');
    sb.append(this.canStreetDirectionalCode == null ? "<null>"
      : this.canStreetDirectionalCode);
    sb.append(',');
    sb.append("ncStreetName");
    sb.append('=');
    sb.append(this.ncStreetName == null ? "<null>" : this.ncStreetName);
    sb.append(',');
    sb.append("wsaddrStreetNumberMinimumText");
    sb.append('=');
    sb.append(this.wsaddrStreetNumberMinimumText == null ? "<null>"
      : this.wsaddrStreetNumberMinimumText);
    sb.append(',');
    sb.append("wsaddrStreetNumberMaximumText");
    sb.append('=');
    sb.append(this.wsaddrStreetNumberMaximumText == null ? "<null>"
      : this.wsaddrStreetNumberMaximumText);
    sb.append(',');
    sb.append("wsaddrStreetNumberSuffixMinimumText");
    sb.append('=');
    sb.append(this.wsaddrStreetNumberSuffixMinimumText == null ? "<null>"
      : this.wsaddrStreetNumberSuffixMinimumText);
    sb.append(',');
    sb.append("wsaddrStreetNumberSuffixMaximumText");
    sb.append('=');
    sb.append(this.wsaddrStreetNumberSuffixMaximumText == null ? "<null>"
      : this.wsaddrStreetNumberSuffixMaximumText);
    sb.append(',');
    sb.append("canStreetCategoryCode");
    sb.append('=');
    sb.append(this.canStreetCategoryCode == null ? "<null>"
      : this.canStreetCategoryCode);
    sb.append(',');
    sb.append("wsaddrSuiteNumberMinimumText");
    sb.append('=');
    sb.append(this.wsaddrSuiteNumberMinimumText == null ? "<null>"
      : this.wsaddrSuiteNumberMinimumText);
    sb.append(',');
    sb.append("wsaddrSuiteNumberMaximumText");
    sb.append('=');
    sb.append(this.wsaddrSuiteNumberMaximumText == null ? "<null>"
      : this.wsaddrSuiteNumberMaximumText);
    sb.append(',');
    sb.append("ncAddressFullText");
    sb.append('=');
    sb.append(
      this.ncAddressFullText == null ? "<null>" : this.ncAddressFullText);
    sb.append(',');
    sb.append("wsaddrLockBoxNumberMinimumText");
    sb.append('=');
    sb.append(this.wsaddrLockBoxNumberMinimumText == null ? "<null>"
      : this.wsaddrLockBoxNumberMinimumText);
    sb.append(',');
    sb.append("wsaddrLockBoxNumberMaximumText");
    sb.append('=');
    sb.append(this.wsaddrLockBoxNumberMaximumText == null ? "<null>"
      : this.wsaddrLockBoxNumberMaximumText);
    sb.append(',');
    sb.append("wsaddrDeliveryInstallationAreaName");
    sb.append('=');
    sb.append(this.wsaddrDeliveryInstallationAreaName == null ? "<null>"
      : this.wsaddrDeliveryInstallationAreaName);
    sb.append(',');
    sb.append("wsaddrDeliveryInstallationDescription");
    sb.append('=');
    sb.append(this.wsaddrDeliveryInstallationDescription == null ? "<null>"
      : this.wsaddrDeliveryInstallationDescription);
    sb.append(',');
    sb.append("wsaddrDeliveryInstallationQualifierName");
    sb.append('=');
    sb.append(this.wsaddrDeliveryInstallationQualifierName == null ? "<null>"
      : this.wsaddrDeliveryInstallationQualifierName);
    sb.append(',');
    sb.append("wsaddrRouteServiceCategory");
    sb.append('=');
    sb.append(this.wsaddrRouteServiceCategory == null ? "<null>"
      : this.wsaddrRouteServiceCategory);
    sb.append(',');
    sb.append("wsaddrRouteServiceNumber");
    sb.append('=');
    sb.append(this.wsaddrRouteServiceNumber == null ? "<null>"
      : this.wsaddrRouteServiceNumber);
    sb.append(',');
    sb.append("wsaddrRuralRouteServiceCategory");
    sb.append('=');
    sb.append(this.wsaddrRuralRouteServiceCategory == null ? "<null>"
      : this.wsaddrRuralRouteServiceCategory);
    sb.append(',');
    sb.append("wsaddrRuralRouteServiceNumber");
    sb.append('=');
    sb.append(this.wsaddrRuralRouteServiceNumber == null ? "<null>"
      : this.wsaddrRuralRouteServiceNumber);
    sb.append(',');
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.setCharAt(sb.length() - 1, ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

}
