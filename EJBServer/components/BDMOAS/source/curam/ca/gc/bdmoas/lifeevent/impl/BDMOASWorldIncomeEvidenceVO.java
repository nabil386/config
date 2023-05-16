/**
 *
 */
package curam.ca.gc.bdmoas.lifeevent.impl;

/**
 * BDMOAS FEATURE 92921:
 * World Income Evidence value object class
 *
 * @author abid.a.khan
 *
 */
public class BDMOASWorldIncomeEvidenceVO {

  private long evidenceID;

  private String threshold;

  private String overThreshold;

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
   * @return the threshold
   */
  public String getThreshold() {

    return this.threshold;
  }

  /**
   * @param threshold the threshold to set
   */
  public void setThreshold(final String threshold) {

    this.threshold = threshold;
  }

  /**
   * @return the overThreshold
   */
  public String getOverThreshold() {

    return this.overThreshold;
  }

  /**
   * @param overThreshold the overThreshold to set
   */
  public void setOverThreshold(final String overThreshold) {

    this.overThreshold = overThreshold;
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
