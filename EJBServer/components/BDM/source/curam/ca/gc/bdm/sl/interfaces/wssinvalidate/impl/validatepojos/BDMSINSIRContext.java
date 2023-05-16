
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BDMSINSIRContext {

    @SerializedName("can")
    @Expose
    private String can;
    @SerializedName("nc")
    @Expose
    private String nc;
    @SerializedName("ss")
    @Expose
    private String ss;
    @SerializedName("per")
    @Expose
    private String per;
    @SerializedName("fault")
    @Expose
    private String fault;

    public String getCan() {
        return can;
    }

    public void setCan(String can) {
        this.can = can;
    }

    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

}
