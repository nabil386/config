package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.util.type.Date;

/**
 * BDMOAS FEATURE 92921: Class Added
 * VO for Application Details evidence
 *
 * @author SMisal
 *
 */

public class BDMOASApplicationDetailsEvidenceVO {

  private long evidenceID;

  private String benefitType;

  private String methodOfApplication;

  private Date receiptDate;

  private Date requestedPensionStartDate;

  private Date withdrawalDate;

  private String applicationStatus;

  private String comments;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getBenefitType() {

    return this.benefitType;
  }

  public void setBenefitType(final String benefitType) {

    this.benefitType = benefitType;
  }

  public String getMethodOfApplication() {

    return this.methodOfApplication;
  }

  public void setMethodOfApplication(final String methodOfApplication) {

    this.methodOfApplication = methodOfApplication;
  }

  public Date getReceiptDate() {

    return this.receiptDate;
  }

  public void setReceiptDate(final Date receiptDate) {

    this.receiptDate = receiptDate;
  }

  public Date getWithdrawalDate() {

    return this.withdrawalDate;
  }

  public void setWithdrawalDate(final Date withdrawalDate) {

    this.withdrawalDate = withdrawalDate;
  }

  public Date getRequestedPensionStartDate() {

    return this.requestedPensionStartDate;
  }

  public void
    setRequestedPensionStartDate(final Date requestedPensionStartDate) {

    this.requestedPensionStartDate = requestedPensionStartDate;
  }

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  public String getApplicationStatus() {

    return this.applicationStatus;
  }

  public void setApplicationStatus(final String applicationStatus) {

    this.applicationStatus = applicationStatus;
  }

}
