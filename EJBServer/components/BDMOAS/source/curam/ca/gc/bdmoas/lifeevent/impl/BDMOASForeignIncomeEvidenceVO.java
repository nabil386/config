package curam.ca.gc.bdmoas.lifeevent.impl;

/**
 * BDMOAS FEATURE 92921: Class Added
 * VO for Foreign Income evidence
 *
 * @author SMisal
 *
 */
public class BDMOASForeignIncomeEvidenceVO {

  private long evidenceID;

  private String amount;

  private String canadianAmount;

  private String year;

  private String incomeType;

  private String currency;

  private String country;

  private String otherDescription;

  private String comments;

  public String getIncomeType() {

    return this.incomeType;
  }

  public void setIncomeType(final String incomeType) {

    this.incomeType = incomeType;
  }

  public String getCurrency() {

    return this.currency;
  }

  public void setCurrency(final String currency) {

    this.currency = currency;
  }

  public String getCountry() {

    return this.country;
  }

  public void setCountry(final String country) {

    this.country = country;
  }

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  public String getOtherDescription() {

    return this.otherDescription;
  }

  public void setOtherDescription(final String otherDescription) {

    this.otherDescription = otherDescription;
  }

  public String getYear() {

    return this.year;
  }

  public void setYear(final String incomeYear) {

    this.year = incomeYear;
  }

  public String getCanadianAmount() {

    return this.canadianAmount;
  }

  public void setCanadianAmount(final String canadianAmount) {

    this.canadianAmount = canadianAmount;
  }

  public String getAmount() {

    return this.amount;
  }

  public void setAmount(final String amount) {

    this.amount = amount;
  }

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }
}
