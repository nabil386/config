
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SsDeathIndividuals {

  @SerializedName("nc:PersonLivingIndicator")
  @Expose
  private Integer ncPersonLivingIndicator;

  @SerializedName("nc:PersonDeathDate")
  @Expose
  private String ncPersonDeathDate;

  @SerializedName("can:ProvinceCode")
  @Expose
  private String canProvinceCode;

  @SerializedName("ss:DeathCertificationFlag")
  @Expose
  private Integer ssDeathCertificationFlag;

  @SerializedName("ss:DeathCertificateIdentification")
  @Expose
  private String ssDeathCertificateIdentification;

  public Integer getNcPersonLivingIndicator() {

    return ncPersonLivingIndicator;
  }

  public void
    setNcPersonLivingIndicator(final Integer ncPersonLivingIndicator) {

    this.ncPersonLivingIndicator = ncPersonLivingIndicator;
  }

  public String getNcPersonDeathDate() {

    return ncPersonDeathDate;
  }

  public void setNcPersonDeathDate(final String ncPersonDeathDate) {

    this.ncPersonDeathDate = ncPersonDeathDate;
  }

  public String getCanProvinceCode() {

    return canProvinceCode;
  }

  public void setCanProvinceCode(final String canProvinceCode) {

    this.canProvinceCode = canProvinceCode;
  }

  public Integer getSsDeathCertificationFlag() {

    return ssDeathCertificationFlag;
  }

  public void
    setSsDeathCertificationFlag(final Integer ssDeathCertificationFlag) {

    this.ssDeathCertificationFlag = ssDeathCertificationFlag;
  }

  public String getSsDeathCertificateIdentification() {

    return ssDeathCertificateIdentification;
  }

  public void setSsDeathCertificateIdentification(
    final String ssDeathCertificateIdentification) {

    this.ssDeathCertificateIdentification = ssDeathCertificateIdentification;
  }

}
