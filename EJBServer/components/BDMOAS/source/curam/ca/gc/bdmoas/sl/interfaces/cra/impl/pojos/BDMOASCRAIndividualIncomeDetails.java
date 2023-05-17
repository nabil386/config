package curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos;

import com.google.gson.annotations.SerializedName;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMClientData;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMIncomeDetail;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMReferenceData;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMRelatedPerson;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMStatus;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMTaxYear;
import java.util.List;

/**
 * Parameters from the request- BDMCRAIndividualIncome.CRA Inbound
 * POJO.
 */
public class BDMOASCRAIndividualIncomeDetails {

  @SerializedName(value = "IndividualIncomeTaxationYear")
  private BDMTaxYear incomeTaxationYear;

  @SerializedName(value = "IndividualIncomeStatus")
  private BDMStatus individualIncomeStatus;

  @SerializedName(value = "Client")
  private BDMClientData client;

  @SerializedName(value = "RelatedPerson")
  private List<BDMRelatedPerson> relatedPerson;

  @SerializedName(value = "IndividualIncomeBankruptcyCategoryCode")
  private BDMReferenceData incomeBankruptcyCode;

  @SerializedName(value = "IndividualIncomeDetail")
  private List<BDMIncomeDetail> individualIncomeDetail;

  /**
   * @return the incomeTaxationYear
   */
  public BDMTaxYear getIncomeTaxationYear() {

    return this.incomeTaxationYear;
  }

  /**
   * @param incomeTaxationYear the incomeTaxationYear to set
   */
  public void setIncomeTaxationYear(final BDMTaxYear incomeTaxationYear) {

    this.incomeTaxationYear = incomeTaxationYear;
  }

  /**
   * @return the individualIncomeStatus
   */
  public BDMStatus getIndividualIncomeStatus() {

    return this.individualIncomeStatus;
  }

  /**
   * @param individualIncomeStatus the individualIncomeStatus to set
   */
  public void
    setIndividualIncomeStatus(final BDMStatus individualIncomeStatus) {

    this.individualIncomeStatus = individualIncomeStatus;
  }

  /**
   * @return the client
   */
  public BDMClientData getClient() {

    return this.client;
  }

  /**
   * @param client the client to set
   */
  public void setClient(final BDMClientData client) {

    this.client = client;
  }

  /**
   * @return the relatedPerson
   */
  public List<BDMRelatedPerson> getRelatedPerson() {

    return this.relatedPerson;
  }

  /**
   * @param relatedPerson the relatedPerson to set
   */
  public void setRelatedPerson(final List<BDMRelatedPerson> relatedPerson) {

    this.relatedPerson = relatedPerson;
  }

  /**
   * @return the incomeBankruptcyCode
   */
  public BDMReferenceData getIncomeBankruptcyCode() {

    return this.incomeBankruptcyCode;
  }

  /**
   * @param incomeBankruptcyCode the incomeBankruptcyCode to set
   */
  public void
    setIncomeBankruptcyCode(final BDMReferenceData incomeBankruptcyCode) {

    this.incomeBankruptcyCode = incomeBankruptcyCode;
  }

  /**
   * @return the individualIncomeDetail
   */
  public List<BDMIncomeDetail> getIndividualIncomeDetail() {

    return this.individualIncomeDetail;
  }

  /**
   * @param individualIncomeDetail the individualIncomeDetail to set
   */
  public void setIndividualIncomeDetail(
    final List<BDMIncomeDetail> individualIncomeDetail) {

    this.individualIncomeDetail = individualIncomeDetail;
  }

}
