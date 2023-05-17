package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Income Details POJO
 */
public class BDMIncomeDetail {

  @SerializedName(value = "IncomeAmount")
  private BDMAmount incomeAmount;

  @SerializedName(value = "IncomeName")
  private String incomeName;

  @SerializedName(value = "IncomeLineNumber")
  private String incomeLineNumber;

  @SerializedName(value = "IncomeCalculatedIndicator")
  private Boolean incomeCalculatedIndicator;

  /**
   * @return the incomeAmount
   */
  public BDMAmount getIncomeAmount() {

    return this.incomeAmount;
  }

  /**
   * @param incomeAmount the incomeAmount to set
   */
  public void setIncomeAmount(final BDMAmount incomeAmount) {

    this.incomeAmount = incomeAmount;
  }

  /**
   * @return the incomeName
   */
  public String getIncomeName() {

    return this.incomeName;
  }

  /**
   * @param incomeName the incomeName to set
   */
  public void setIncomeName(final String incomeName) {

    this.incomeName = incomeName;
  }

  /**
   * @return the incomeLineNumber
   */
  public String getIncomeLineNumber() {

    return this.incomeLineNumber;
  }

  /**
   * @param incomeLineNumber the incomeLineNumber to set
   */
  public void setIncomeLineNumber(final String incomeLineNumber) {

    this.incomeLineNumber = incomeLineNumber;
  }

  /**
   * @return the incomeCalculatedIndicator
   */
  public Boolean isIncomeCalculatedIndicator() {

    return this.incomeCalculatedIndicator;
  }

  /**
   * @param incomeCalculatedIndicator the incomeCalculatedIndicator to set
   */
  public void
    setIncomeCalculatedIndicator(final Boolean incomeCalculatedIndicator) {

    this.incomeCalculatedIndicator = incomeCalculatedIndicator;
  }
}
