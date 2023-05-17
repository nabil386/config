package curam.ca.gc.bdm.lifeevent.impl;

import java.util.Objects;

/**
 * @since ADO-21129
 *
 * The POJO for the gender evidence.
 */
public class BDMGenderEvidenceVO {

  private String gender;

  private String comments;

  private long evidenceID;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getGender() {

    return this.gender;
  }

  public void setGender(final String gender) {

    this.gender = gender;
  }

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  /**
   * Determines the equality of the gender evidence object and returns true if
   * both are equal otherwise return false.
   *
   * @param genderVOObj gender evidence object to compare
   * @return true if both are equal otherwise false
   */
  @Override
  public boolean equals(final Object genderVOObj) {

    if (this == genderVOObj) {
      return true;
    }

    if (genderVOObj == null || getClass() != genderVOObj.getClass()) {
      return false;
    }

    final BDMGenderEvidenceVO genderVO = (BDMGenderEvidenceVO) genderVOObj;

    // checks if the gender is same on both objects.
    return Objects.equals(gender, genderVO.getGender());
  }

}
