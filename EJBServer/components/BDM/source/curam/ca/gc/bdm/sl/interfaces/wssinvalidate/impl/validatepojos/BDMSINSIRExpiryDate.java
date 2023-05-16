
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BDMSINSIRExpiryDate {

    @SerializedName("ss:SINExpirationDate")
    @Expose
    private String ssSINExpirationDate;
    @SerializedName("ss:ImmigrationExpirationDate")
    @Expose
    private String ssImmigrationExpirationDate;

    public String getSsSINExpirationDate() {
        return ssSINExpirationDate;
    }

    public void setSsSINExpirationDate(String ssSINExpirationDate) {
        this.ssSINExpirationDate = ssSINExpirationDate;
    }

    public String getSsImmigrationExpirationDate() {
        return ssImmigrationExpirationDate;
    }

    public void setSsImmigrationExpirationDate(String ssImmigrationExpirationDate) {
        this.ssImmigrationExpirationDate = ssImmigrationExpirationDate;
    }

}
