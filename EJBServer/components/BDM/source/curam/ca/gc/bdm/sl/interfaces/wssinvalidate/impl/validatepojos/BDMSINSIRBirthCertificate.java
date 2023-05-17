
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BDMSINSIRBirthCertificate {

    @SerializedName("ss:BirthCertificateIdentification")
    @Expose
    private String ssBirthCertificateIdentification;
    @SerializedName("ss:DocumentEffectiveDate")
    @Expose
    private String ssDocumentEffectiveDate;

    public String getSsBirthCertificateIdentification() {
        return ssBirthCertificateIdentification;
    }

    public void setSsBirthCertificateIdentification(String ssBirthCertificateIdentification) {
        this.ssBirthCertificateIdentification = ssBirthCertificateIdentification;
    }

    public String getSsDocumentEffectiveDate() {
        return ssDocumentEffectiveDate;
    }

    public void setSsDocumentEffectiveDate(String ssDocumentEffectiveDate) {
        this.ssDocumentEffectiveDate = ssDocumentEffectiveDate;
    }

}
