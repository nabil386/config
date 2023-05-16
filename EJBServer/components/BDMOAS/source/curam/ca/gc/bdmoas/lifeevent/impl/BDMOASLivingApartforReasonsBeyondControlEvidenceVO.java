package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.util.type.Date;

/**
 * BDMOAS FEATURE 92921: Class Added
 * VO for Living Apart for Reasons Beyond Control evidence
 *
 * @author SMisal
 *
 */
public class BDMOASLivingApartforReasonsBeyondControlEvidenceVO {

  private long evidenceID;

  private String reasonForLivingApart;

  private Date startDate;

  private Date endDate;

  private String livingApartReasonDescription;

  private String comments;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getReasonForLivingApart() {

    return this.reasonForLivingApart;
  }

  public void setReasonForLivingApart(final String reasonForLivingApart) {

    this.reasonForLivingApart = reasonForLivingApart;
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

  public String getLivingApartReasonDescription() {

    return this.livingApartReasonDescription;
  }

  public void setLivingApartReasonDescription(
    final String livingApartReasonDescription) {

    this.livingApartReasonDescription = livingApartReasonDescription;
  }

}
