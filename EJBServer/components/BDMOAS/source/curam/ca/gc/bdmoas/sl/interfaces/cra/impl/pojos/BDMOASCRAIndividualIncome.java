package curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * CRA Interface POJO
 */
public class BDMOASCRAIndividualIncome {

  @SerializedName(value = "IndividualIncome")
  private BDMOASCRAIndividualIncomeDetails individualIncome;

  /**
   * @return the individualIncome
   */
  public BDMOASCRAIndividualIncomeDetails getIndividualIncome() {

    return this.individualIncome;
  }

  /**
   * @param individualIncome the individualIncome to set
   */
  public void setIndividualIncome(
    final BDMOASCRAIndividualIncomeDetails individualIncome) {

    this.individualIncome = individualIncome;
  }

}
