
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BDMSINSIRProvincialValidation {

    @SerializedName("ss:BirthCertificate")
    @Expose
    private BDMSINSIRBirthCertificate ssBirthCertificate;
    @SerializedName("ss:ProvincialValidationActionCode")
    @Expose
    private String ssProvincialValidationActionCode;
    @SerializedName("ss:OtherDocumentIdentification")
    @Expose
    private String ssOtherDocumentIdentification;
    @SerializedName("ss:DocumentType")
    @Expose
    private String ssDocumentType;
    @SerializedName("ss:DocumentCategoryCode")
    @Expose
    private String ssDocumentCategoryCode;
    @SerializedName("can:ProvinceCode")
    @Expose
    private Integer canProvinceCode;
    @SerializedName("ss:Father")
    @Expose
    private BDMSINSIRFather ssFather;
    @SerializedName("ss:Mother")
    @Expose
    private BDMSINSIRMother ssMother;

    public BDMSINSIRBirthCertificate getSsBirthCertificate() {
        return ssBirthCertificate;
    }

    public void setSsBirthCertificate(BDMSINSIRBirthCertificate ssBirthCertificate) {
        this.ssBirthCertificate = ssBirthCertificate;
    }

    public String getSsProvincialValidationActionCode() {
        return ssProvincialValidationActionCode;
    }

    public void setSsProvincialValidationActionCode(String ssProvincialValidationActionCode) {
        this.ssProvincialValidationActionCode = ssProvincialValidationActionCode;
    }

    public String getSsOtherDocumentIdentification() {
        return ssOtherDocumentIdentification;
    }

    public void setSsOtherDocumentIdentification(String ssOtherDocumentIdentification) {
        this.ssOtherDocumentIdentification = ssOtherDocumentIdentification;
    }

    public String getSsDocumentType() {
        return ssDocumentType;
    }

    public void setSsDocumentType(String ssDocumentType) {
        this.ssDocumentType = ssDocumentType;
    }

    public String getSsDocumentCategoryCode() {
        return ssDocumentCategoryCode;
    }

    public void setSsDocumentCategoryCode(String ssDocumentCategoryCode) {
        this.ssDocumentCategoryCode = ssDocumentCategoryCode;
    }

    public Integer getCanProvinceCode() {
        return canProvinceCode;
    }

    public void setCanProvinceCode(Integer canProvinceCode) {
        this.canProvinceCode = canProvinceCode;
    }

    public BDMSINSIRFather getSsFather() {
        return ssFather;
    }

    public void setSsFather(BDMSINSIRFather ssFather) {
        this.ssFather = ssFather;
    }

    public BDMSINSIRMother getSsMother() {
        return ssMother;
    }

    public void setSsMother(BDMSINSIRMother ssMother) {
        this.ssMother = ssMother;
    }

}
