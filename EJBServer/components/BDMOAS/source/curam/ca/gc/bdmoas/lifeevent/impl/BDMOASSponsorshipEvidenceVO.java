package curam.ca.gc.bdmoas.lifeevent.impl;

/**
 * BDMOAS FEATURE 92921: Class Added
 * VO for Sponsorship evidence
 *
 * @author SMisal
 *
 */
public class BDMOASSponsorshipEvidenceVO {

  private long evidenceID;

  private String isSponsored;

  private String comments;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  public String getIsSponsored() {

    return this.isSponsored;
  }

  public void setIsSponsored(final String isSponsored) {

    this.isSponsored = isSponsored;
  }

}
