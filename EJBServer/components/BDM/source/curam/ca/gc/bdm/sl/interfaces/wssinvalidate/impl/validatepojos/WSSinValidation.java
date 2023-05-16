
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WSSinValidation {

  @SerializedName("@context")
  @Expose
  private Context context;

  @SerializedName("ss:MatchFlag")
  @Expose
  private Boolean ssMatchFlag;

  @SerializedName("ss:Individual")
  @Expose
  private SsIndividual ssIndividual;

  @SerializedName("ss:NonMatch")
  @Expose
  private String ssNonMatch;

  @SerializedName("ss:Miscellaneous")
  @Expose
  private SsMiscellaneous ssMiscellaneous;

  @SerializedName("ss:SpecialAccounts")
  @Expose
  private SsSpecialAccounts ssSpecialAccounts;

  @SerializedName("ss:DormantUpdateDate")
  @Expose
  private String ssDormantUpdateDate;

  @SerializedName("ss:ExpiryDate")
  @Expose
  private SsExpiryDate ssExpiryDate;

  @SerializedName("ss:DeathIndividuals")
  @Expose
  private SsDeathIndividuals ssDeathIndividuals;

  @SerializedName("ss:ProvincialValidation")
  @Expose
  private SsProvincialValidation ssProvincialValidation;

  public Context getContext() {

    return context;
  }

  public void setContext(final Context context) {

    this.context = context;
  }

  public Boolean getSsMatchFlag() {

    return ssMatchFlag;
  }

  public void setSsMatchFlag(final Boolean ssMatchFlag) {

    this.ssMatchFlag = ssMatchFlag;
  }

  public SsIndividual getSsIndividual() {

    return ssIndividual;
  }

  public void setSsIndividual(final SsIndividual ssIndividual) {

    this.ssIndividual = ssIndividual;
  }

  public String getSsNonMatch() {

    return ssNonMatch;
  }

  public void setSsNonMatch(final String ssNonMatch) {

    this.ssNonMatch = ssNonMatch;
  }

  public SsMiscellaneous getSsMiscellaneous() {

    return ssMiscellaneous;
  }

  public void setSsMiscellaneous(final SsMiscellaneous ssMiscellaneous) {

    this.ssMiscellaneous = ssMiscellaneous;
  }

  public SsSpecialAccounts getSsSpecialAccounts() {

    return ssSpecialAccounts;
  }

  public void
    setSsSpecialAccounts(final SsSpecialAccounts ssSpecialAccounts) {

    this.ssSpecialAccounts = ssSpecialAccounts;
  }

  public String getSsDormantUpdateDate() {

    return ssDormantUpdateDate;
  }

  public void setSsDormantUpdateDate(final String ssDormantUpdateDate) {

    this.ssDormantUpdateDate = ssDormantUpdateDate;
  }

  public SsExpiryDate getSsExpiryDate() {

    return ssExpiryDate;
  }

  public void setSsExpiryDate(final SsExpiryDate ssExpiryDate) {

    this.ssExpiryDate = ssExpiryDate;
  }

  public SsDeathIndividuals getSsDeathIndividuals() {

    return ssDeathIndividuals;
  }

  public void
    setSsDeathIndividuals(final SsDeathIndividuals ssDeathIndividuals) {

    this.ssDeathIndividuals = ssDeathIndividuals;
  }

  public SsProvincialValidation getSsProvincialValidation() {

    return ssProvincialValidation;
  }

  public void setSsProvincialValidation(
    final SsProvincialValidation ssProvincialValidation) {

    this.ssProvincialValidation = ssProvincialValidation;
  }

}
