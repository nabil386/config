
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BDMSINSIRDeathIndividuals {

    @SerializedName("nc:PersonLivingIndicator")
    @Expose
    private Integer ncPersonLivingIndicator;
    @SerializedName("nc:PersonDeathDate")
    @Expose
    private String ncPersonDeathDate;
    @SerializedName("can:ProvinceCode")
    @Expose
    private Integer canProvinceCode;
    @SerializedName("ss:DeathCertificationFlag")
    @Expose
    private Integer ssDeathCertificationFlag;
    @SerializedName("ss:DeathCertificateIdentification")
    @Expose
    private String ssDeathCertificateIdentification;

    public Integer getNcPersonLivingIndicator() {
        return ncPersonLivingIndicator;
    }

    public void setNcPersonLivingIndicator(Integer ncPersonLivingIndicator) {
        this.ncPersonLivingIndicator = ncPersonLivingIndicator;
    }

    public String getNcPersonDeathDate() {
        return ncPersonDeathDate;
    }

    public void setNcPersonDeathDate(String ncPersonDeathDate) {
        this.ncPersonDeathDate = ncPersonDeathDate;
    }

    public Integer getCanProvinceCode() {
        return canProvinceCode;
    }

    public void setCanProvinceCode(Integer canProvinceCode) {
        this.canProvinceCode = canProvinceCode;
    }

    public Integer getSsDeathCertificationFlag() {
        return ssDeathCertificationFlag;
    }

    public void setSsDeathCertificationFlag(Integer ssDeathCertificationFlag) {
        this.ssDeathCertificationFlag = ssDeathCertificationFlag;
    }

    public String getSsDeathCertificateIdentification() {
        return ssDeathCertificateIdentification;
    }

    public void setSsDeathCertificateIdentification(String ssDeathCertificateIdentification) {
        this.ssDeathCertificateIdentification = ssDeathCertificateIdentification;
    }

}
