
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SsProvincialValidation {

  @SerializedName("ss:BirthCertificate")
  @Expose
  private SsBirthCertificate ssBirthCertificate;

  @SerializedName("ss:ProvincialValidationActionCode")
  @Expose
  private String ssProvincialValidationActionCode;

  @SerializedName("ss:OtherDocumentIdentification")
  @Expose
  private String ssOtherDocumentIdentification;

  @SerializedName("ss:DocumentType")
  @Expose
  private Integer ssDocumentType;

  @SerializedName("ss:DocumentCategoryCode")
  @Expose
  private String ssDocumentCategoryCode;

  @SerializedName("can:ProvinceCode")
  @Expose
  private Integer canProvinceCode;

  @SerializedName("ss:Father")
  @Expose
  private SsFather ssFather;

  @SerializedName("ss:Mother")
  @Expose
  private SsMother ssMother;

  public SsBirthCertificate getSsBirthCertificate() {

    return ssBirthCertificate;
  }

  public void
    setSsBirthCertificate(final SsBirthCertificate ssBirthCertificate) {

    this.ssBirthCertificate = ssBirthCertificate;
  }

  public String getSsProvincialValidationActionCode() {

    return ssProvincialValidationActionCode;
  }

  public void setSsProvincialValidationActionCode(
    final String ssProvincialValidationActionCode) {

    this.ssProvincialValidationActionCode = ssProvincialValidationActionCode;
  }

  public String getSsOtherDocumentIdentification() {

    return ssOtherDocumentIdentification;
  }

  public void setSsOtherDocumentIdentification(
    final String ssOtherDocumentIdentification) {

    this.ssOtherDocumentIdentification = ssOtherDocumentIdentification;
  }

  public Integer getSsDocumentType() {

    return ssDocumentType;
  }

  public void setSsDocumentType(final Integer ssDocumentType) {

    this.ssDocumentType = ssDocumentType;
  }

  public String getSsDocumentCategoryCode() {

    return ssDocumentCategoryCode;
  }

  public void setSsDocumentCategoryCode(final String ssDocumentCategoryCode) {

    this.ssDocumentCategoryCode = ssDocumentCategoryCode;
  }

  public Integer getCanProvinceCode() {

    return canProvinceCode;
  }

  public void setCanProvinceCode(final Integer canProvinceCode) {

    this.canProvinceCode = canProvinceCode;
  }

  public SsFather getSsFather() {

    return ssFather;
  }

  public void setSsFather(final SsFather ssFather) {

    this.ssFather = ssFather;
  }

  public SsMother getSsMother() {

    return ssMother;
  }

  public void setSsMother(final SsMother ssMother) {

    this.ssMother = ssMother;
  }

}
