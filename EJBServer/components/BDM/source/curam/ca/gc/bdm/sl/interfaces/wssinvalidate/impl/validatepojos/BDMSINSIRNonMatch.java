
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BDMSINSIRNonMatch {

    @SerializedName("ss:GivenName")
    @Expose
    private Boolean ssGivenName;
    @SerializedName("ss:Surname")
    @Expose
    private Boolean ssSurname;
    @SerializedName("ss:DateOfBirth")
    @Expose
    private Boolean ssDateOfBirth;
    @SerializedName("ss:Parents")
    @Expose
    private Boolean ssParents;

    public Boolean getSsGivenName() {
        return ssGivenName;
    }

    public void setSsGivenName(Boolean ssGivenName) {
        this.ssGivenName = ssGivenName;
    }

    public Boolean getSsSurname() {
        return ssSurname;
    }

    public void setSsSurname(Boolean ssSurname) {
        this.ssSurname = ssSurname;
    }

    public Boolean getSsDateOfBirth() {
        return ssDateOfBirth;
    }

    public void setSsDateOfBirth(Boolean ssDateOfBirth) {
        this.ssDateOfBirth = ssDateOfBirth;
    }

    public Boolean getSsParents() {
        return ssParents;
    }

    public void setSsParents(Boolean ssParents) {
        this.ssParents = ssParents;
    }

}
