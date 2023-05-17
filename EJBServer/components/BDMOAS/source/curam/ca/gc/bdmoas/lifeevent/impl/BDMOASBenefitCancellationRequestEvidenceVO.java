/**
 *
 */
package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.util.type.Date;

/**
 * BDMOAS FEATURE 92921:
 * Benefit Cancellation Request Evidence value object class
 *
 * @author abid.a.khan
 *
 */
public class BDMOASBenefitCancellationRequestEvidenceVO {

  private long evidenceID;

  private String cancellationStatus;

  private Date requestDate;

  private Date grantDate;

  private Date repaymentDueDateOverride;

  private String comments;

  /**
   * @return the evidenceID
   */
  public long getEvidenceID() {

    return this.evidenceID;
  }

  /**
   * @param evidenceID the evidenceID to set
   */
  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  /**
   * @return the cancellationStatus
   */
  public String getCancellationStatus() {

    return this.cancellationStatus;
  }

  /**
   * @param cancellationStatus the cancellationStatus to set
   */
  public void setCancellationStatus(final String cancellationStatus) {

    this.cancellationStatus = cancellationStatus;
  }

  /**
   * @return the requestDate
   */
  public Date getRequestDate() {

    return this.requestDate;
  }

  /**
   * @param requestDate the requestDate to set
   */
  public void setRequestDate(final Date requestDate) {

    this.requestDate = requestDate;
  }

  /**
   * @return the grantDate
   */
  public Date getGrantDate() {

    return this.grantDate;
  }

  /**
   * @param grantDate the grantDate to set
   */
  public void setGrantDate(final Date grantDate) {

    this.grantDate = grantDate;
  }

  /**
   * @return the repaymentDueDateOverride
   */
  public Date getRepaymentDueDateOverride() {

    return this.repaymentDueDateOverride;
  }

  /**
   * @param repaymentDueDateOverride the repaymentDueDateOverride to set
   */
  public void
    setRepaymentDueDateOverride(final Date repaymentDueDateOverride) {

    this.repaymentDueDateOverride = repaymentDueDateOverride;
  }

  /**
   * @return the comments
   */
  public String getComments() {

    return this.comments;
  }

  /**
   * @param comments the comments to set
   */
  public void setComments(final String comments) {

    this.comments = comments;
  }

}
