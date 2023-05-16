/**
 *
 */
package curam.ca.gc.bdmoas.lifeevent.impl;

/**
 * BDMOAS FEATURE 92921:
 * Nature of Absences evidence value object class
 *
 * @author abid.a.khan
 *
 */
public class BDMOASNatureOfAbsencesEvidenceVO {

  private long evidenceID;

  private String absenceDetails;

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
   * @return the absenceDetails
   */
  public String getAbsenceDetails() {

    return this.absenceDetails;
  }

  /**
   * @param absenceDetails the absenceDetails to set
   */
  public void setAbsenceDetails(final String absenceDetails) {

    this.absenceDetails = absenceDetails;
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
