package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.util.type.Date;

/**
 * BDMOAS FEATURE 92921: Class Added
 * VO for Marital Status evidence
 *
 * @author SMisal
 *
 */
public class BDMOASMaritalStatusEvidenceVO {

  private long evidenceID;

  private String maritalStatus;

  private Date startDate;

  private Date endDate;

  private String bdmReceivedFrom;

  private String bdmReceivedFromCountry;

  private String bdmModeOfReceipt;

  private String comments;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getMaritalStatus() {

    return this.maritalStatus;
  }

  public void setMaritalStatus(final String maritalStatus) {

    this.maritalStatus = maritalStatus;
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

  public String getBdmReceivedFrom() {

    return this.bdmReceivedFrom;
  }

  public void setBdmReceivedFrom(final String bdmReceivedFrom) {

    this.bdmReceivedFrom = bdmReceivedFrom;
  }

  public String getBdmReceivedFromCountry() {

    return this.bdmReceivedFromCountry;
  }

  public void setBdmReceivedFromCountry(final String bdmReceivedFromCountry) {

    this.bdmReceivedFromCountry = bdmReceivedFromCountry;
  }

  public String getBdmModeOfReceipt() {

    return this.bdmModeOfReceipt;
  }

  public void setBdmModeOfReceipt(final String bdmModeOfReceipt) {

    this.bdmModeOfReceipt = bdmModeOfReceipt;
  }

}
