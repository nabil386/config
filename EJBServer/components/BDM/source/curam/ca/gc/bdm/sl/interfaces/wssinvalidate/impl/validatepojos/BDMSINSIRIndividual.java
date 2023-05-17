
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BDMSINSIRIndividual {

    @SerializedName("ss:SearchTable")
    @Expose
    private BDMSINSIRSearchTable ssSearchTable;
    @SerializedName("nc:PersonSexCode")
    @Expose
    private String ncPersonSexCode;
    @SerializedName("ss:TwinFlagIndicator")
    @Expose
    private Integer ssTwinFlagIndicator;
    @SerializedName("ss:MultipleFlagIndicator")
    @Expose
    private Integer ssMultipleFlagIndicator;
    @SerializedName("ss:DuplicateFlagIndicator")
    @Expose
    private Integer ssDuplicateFlagIndicator;
    @SerializedName("ss:SIN")
    @Expose
    private BDMSINSIRSin ssSIN;

    public BDMSINSIRSearchTable getSsSearchTable() {
        return ssSearchTable;
    }

    public void setSsSearchTable(BDMSINSIRSearchTable ssSearchTable) {
        this.ssSearchTable = ssSearchTable;
    }

    public String getNcPersonSexCode() {
        return ncPersonSexCode;
    }

    public void setNcPersonSexCode(String ncPersonSexCode) {
        this.ncPersonSexCode = ncPersonSexCode;
    }

    public Integer getSsTwinFlagIndicator() {
        return ssTwinFlagIndicator;
    }

    public void setSsTwinFlagIndicator(Integer ssTwinFlagIndicator) {
        this.ssTwinFlagIndicator = ssTwinFlagIndicator;
    }

    public Integer getSsMultipleFlagIndicator() {
        return ssMultipleFlagIndicator;
    }

    public void setSsMultipleFlagIndicator(Integer ssMultipleFlagIndicator) {
        this.ssMultipleFlagIndicator = ssMultipleFlagIndicator;
    }

    public Integer getSsDuplicateFlagIndicator() {
        return ssDuplicateFlagIndicator;
    }

    public void setSsDuplicateFlagIndicator(Integer ssDuplicateFlagIndicator) {
        this.ssDuplicateFlagIndicator = ssDuplicateFlagIndicator;
    }

    public BDMSINSIRSin getSsSIN() {
        return ssSIN;
    }

    public void setSsSIN(BDMSINSIRSin ssSIN) {
        this.ssSIN = ssSIN;
    }

}
