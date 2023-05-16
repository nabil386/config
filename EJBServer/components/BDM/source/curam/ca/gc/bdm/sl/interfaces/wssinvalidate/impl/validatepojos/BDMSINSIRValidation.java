
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BDMSINSIRValidation {

    @SerializedName("@context")
    @Expose
    private BDMSINSIRContext context;
    @SerializedName("ss:MatchFlag")
    @Expose
    private Boolean ssMatchFlag;
    @SerializedName("ss:Individual")
    @Expose
    private BDMSINSIRIndividual ssIndividual;
    @SerializedName("ss:NonMatch")
    @Expose
    private BDMSINSIRNonMatch ssNonMatch;
    @SerializedName("ss:Miscellaneous")
    @Expose
    private BDMSINSIRMiscellaneous ssMiscellaneous;
    @SerializedName("ss:SpecialAccounts")
    @Expose
    private BDMSINSIRSpecialAccounts ssSpecialAccounts;
    @SerializedName("ss:DormantUpdateDate")
    @Expose
    private String ssDormantUpdateDate;
    @SerializedName("ss:ExpiryDate")
    @Expose
    private BDMSINSIRExpiryDate ssExpiryDate;
    @SerializedName("ss:DeathIndividuals")
    @Expose
    private BDMSINSIRDeathIndividuals ssDeathIndividuals;
    @SerializedName("ss:ProvincialValidation")
    @Expose
    private BDMSINSIRProvincialValidation ssProvincialValidation;

    public BDMSINSIRContext getContext() {
        return context;
    }

    public void setContext(BDMSINSIRContext context) {
        this.context = context;
    }

    public Boolean getSsMatchFlag() {
        return ssMatchFlag;
    }

    public void setSsMatchFlag(Boolean ssMatchFlag) {
        this.ssMatchFlag = ssMatchFlag;
    }

    public BDMSINSIRIndividual getSsIndividual() {
        return ssIndividual;
    }

    public void setSsIndividual(BDMSINSIRIndividual ssIndividual) {
        this.ssIndividual = ssIndividual;
    }

    public BDMSINSIRNonMatch getSsNonMatch() {
        return ssNonMatch;
    }

    public void setSsNonMatch(BDMSINSIRNonMatch ssNonMatch) {
        this.ssNonMatch = ssNonMatch;
    }

    public BDMSINSIRMiscellaneous getSsMiscellaneous() {
        return ssMiscellaneous;
    }

    public void setSsMiscellaneous(BDMSINSIRMiscellaneous ssMiscellaneous) {
        this.ssMiscellaneous = ssMiscellaneous;
    }

    public BDMSINSIRSpecialAccounts getSsSpecialAccounts() {
        return ssSpecialAccounts;
    }

    public void setSsSpecialAccounts(BDMSINSIRSpecialAccounts ssSpecialAccounts) {
        this.ssSpecialAccounts = ssSpecialAccounts;
    }

    public String getSsDormantUpdateDate() {
        return ssDormantUpdateDate;
    }

    public void setSsDormantUpdateDate(String ssDormantUpdateDate) {
        this.ssDormantUpdateDate = ssDormantUpdateDate;
    }

    public BDMSINSIRExpiryDate getSsExpiryDate() {
        return ssExpiryDate;
    }

    public void setSsExpiryDate(BDMSINSIRExpiryDate ssExpiryDate) {
        this.ssExpiryDate = ssExpiryDate;
    }

    public BDMSINSIRDeathIndividuals getSsDeathIndividuals() {
        return ssDeathIndividuals;
    }

    public void setSsDeathIndividuals(BDMSINSIRDeathIndividuals ssDeathIndividuals) {
        this.ssDeathIndividuals = ssDeathIndividuals;
    }

    public BDMSINSIRProvincialValidation getSsProvincialValidation() {
        return ssProvincialValidation;
    }

    public void setSsProvincialValidation(BDMSINSIRProvincialValidation ssProvincialValidation) {
        this.ssProvincialValidation = ssProvincialValidation;
    }

}
