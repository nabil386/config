/**
 *
 */
package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.util.type.Date;

/**
 * BDMOAS FEATURE 92921:
 * Retirement Pension Reduction value object class
 *
 * @author abid.a.khan
 *
 */
public class BDMOASRetirementPensionReductionEvidenceVO {

  private long evidenceID;

  private String eventType;

  private Date eventDate;

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
   * @return the eventType
   */
  public String getEventType() {

    return this.eventType;
  }

  /**
   * @param eventType the eventType to set
   */
  public void setEventType(final String eventType) {

    this.eventType = eventType;
  }

  /**
   * @return the eventDate
   */
  public Date getEventDate() {

    return this.eventDate;
  }

  /**
   * @param eventDate the eventDate to set
   */
  public void setEventDate(final Date eventDate) {

    this.eventDate = eventDate;
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
