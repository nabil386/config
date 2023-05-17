package curam.ca.gc.bdm.lifeevent.impl;

import curam.util.type.Date;


/**
 *
 * VO for incarceration evidence
 * BDM Project
 *
 *
 */
public class BDMIncarcerationEvidenceVO {

  private long evidenceID;

  private String institutionName;

  private Date startDate;

  private Date endDate;

  private String incarcerationChangeType;

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

  /**
   * @param bdmAgreeAttestation
   */
  public void setBdmAgreeAttestation(final String bdmAgreeAttestation) {

    this.bdmAgreeAttestation = bdmAgreeAttestation;
  }

  public String getIncarcerationChangeType() {

    return this.incarcerationChangeType;
  }

  public void
    setIncarcerationChangeType(final String incarcerationChangeType) {

    this.incarcerationChangeType = incarcerationChangeType;
  }

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getInstitutionName() {

    return this.institutionName;
  }

  public void setInstitutionName(final String institutionName) {

    this.institutionName = institutionName;
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

}
