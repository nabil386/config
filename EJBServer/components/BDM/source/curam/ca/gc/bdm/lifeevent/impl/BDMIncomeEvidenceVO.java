package curam.ca.gc.bdm.lifeevent.impl;

import curam.util.type.Date;

/**
 *
 * VO for income evidence
 * BDM Project
 *
 *
 */
public class BDMIncomeEvidenceVO {

  private long evidenceID;

  private String income;

  private String year;

  private Date attestationDate;

  private String bdmAgreeAttestation;

  private String comments;

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  public Date getAttestationDate() {

    return this.attestationDate;
  }

  public void setAttestationDate(final Date attestationDate) {

    this.attestationDate = attestationDate;
  }

  public String getBdmAgreeAttestation() {

    return this.bdmAgreeAttestation;
  }

  public void setBdmAgreeAttestation(final String bdmAgreeAttestation) {

    this.bdmAgreeAttestation = bdmAgreeAttestation;
  }

  public String getIncomeYear() {

    return this.year;
  }

  public void setIncomeYear(final String incomeYear) {

    this.year = incomeYear;
  }

  public String getIncomeAmount() {

    return this.income;
  }

  public void setIncomeAmount(final String incomeAmount) {

    this.income = incomeAmount;
  }

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }
}
