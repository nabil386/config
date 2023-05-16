
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BDMSINSIRSearchTable {

    @SerializedName("nc:PersonBirthDate")
    @Expose
    private String ncPersonBirthDate;
    @SerializedName("nc:PersonGivenName")
    @Expose
    private String ncPersonGivenName;
    @SerializedName("nc:PersonMiddleName")
    @Expose
    private String ncPersonMiddleName;
    @SerializedName("nc:PersonSurName")
    @Expose
    private String ncPersonSurName;
    @SerializedName("ss:ParentMaidenName")
    @Expose
    private String ssParentMaidenName;
    @SerializedName("per:PersonSINIdentification")
    @Expose
    private String perPersonSINIdentification;

    public String getNcPersonBirthDate() {
        return ncPersonBirthDate;
    }

    public void setNcPersonBirthDate(String ncPersonBirthDate) {
        this.ncPersonBirthDate = ncPersonBirthDate;
    }

    public String getNcPersonGivenName() {
        return ncPersonGivenName;
    }

    public void setNcPersonGivenName(String ncPersonGivenName) {
        this.ncPersonGivenName = ncPersonGivenName;
    }

    public String getNcPersonMiddleName() {
        return ncPersonMiddleName;
    }

    public void setNcPersonMiddleName(String ncPersonMiddleName) {
        this.ncPersonMiddleName = ncPersonMiddleName;
    }

    public String getNcPersonSurName() {
        return ncPersonSurName;
    }

    public void setNcPersonSurName(String ncPersonSurName) {
        this.ncPersonSurName = ncPersonSurName;
    }

    public String getSsParentMaidenName() {
        return ssParentMaidenName;
    }

    public void setSsParentMaidenName(String ssParentMaidenName) {
        this.ssParentMaidenName = ssParentMaidenName;
    }

    public String getPerPersonSINIdentification() {
        return perPersonSINIdentification;
    }

    public void setPerPersonSINIdentification(String perPersonSINIdentification) {
        this.perPersonSINIdentification = perPersonSINIdentification;
    }

}
