package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.util.type.Date;

/**
 * BDMOAS FEATURE 92921: Class Added
 * VO for Legal Status evidence
 *
 * @author SMisal
 *
 */
public class BDMOASLegalStatusEvidenceVO {

  private long evidenceID;

  private String legalStatus;

  private Date startDate;

  private Date endDate;

  private String otherLegalStatusDetails;

  private String comments;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getLegalStatus() {

    return this.legalStatus;
  }

  public void setLegalStatus(final String legalStatus) {

    this.legalStatus = legalStatus;
  }

  public Date getStartDate() {

    return this.startDate;
  }

  public void setStartDate(final Date startDate) {

    this.startDate = startDate;
  }

  public Date getEndDate() {

    return this.endDate;
  }

  public void setEndDate(final Date endDate) {

    this.endDate = endDate;
  }

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  public String getOtherLegalStatusDetails() {

    return this.otherLegalStatusDetails;
  }

  public void
    setOtherLegalStatusDetails(final String otherLegalStatusDetails) {

    this.otherLegalStatusDetails = otherLegalStatusDetails;
  }

}
