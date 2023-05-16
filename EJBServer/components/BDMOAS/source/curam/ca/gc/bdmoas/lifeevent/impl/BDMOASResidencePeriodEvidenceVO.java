package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.util.type.Date;

/**
 * BDMOAS FEATURE 92921: Class Added
 * VO for Residence Period evidence
 *
 * @author SMisal
 *
 */
public class BDMOASResidencePeriodEvidenceVO {

  private long evidenceID;

  private String country;

  private Date startDate;

  private Date endDate;

  private String residenceType;

  private String absenceReason;

  private String intentToReturnYesNo;

  private String comments;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getCountry() {

    return this.country;
  }

  public void setCountry(final String country) {

    this.country = country;
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

  public String getResidenceType() {

    return this.residenceType;
  }

  public void setResidenceType(final String residenceType) {

    this.residenceType = residenceType;
  }

  public String getAbsenceReason() {

    return this.absenceReason;
  }

  public void setAbsenceReason(final String absenceReason) {

    this.absenceReason = absenceReason;
  }

  public String getIntentToReturnYesNo() {

    return this.intentToReturnYesNo;
  }

  public void setIntentToReturnYesNo(final String intentToReturnYesNo) {

    this.intentToReturnYesNo = intentToReturnYesNo;
  }

}
