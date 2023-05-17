package curam.ca.gc.bdm.lifeevent.impl;

import java.util.Comparator;

/**
 * @since
 *
 */
public class BDMNamesEvidenceVO {

  private String comments;

  private String suffix;

  private String title;

  private String firstName;

  private String initials;

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  public String getSuffix() {

    return this.suffix;
  }

  public void setSuffix(final String suffix) {

    this.suffix = suffix;
  }

  public String getTitle() {

    return this.title;
  }

  public void setTitle(final String title) {

    this.title = title;
  }

  public String getFirstName() {

    return this.firstName;
  }

  public void setFirstName(final String firstName) {

    this.firstName = firstName;
  }

  public String getInitials() {

    return this.initials;
  }

  public void setInitials(final String initials) {

    this.initials = initials;
  }

  public String getMiddleName() {

    return this.middleName;
  }

  public void setMiddleName(final String middleName) {

    this.middleName = middleName;
  }

  public String getNameType() {

    return this.nameType;
  }

  public void setNameType(final String nameType) {

    this.nameType = nameType;
  }

  public String getLastName() {

    return this.lastName;
  }

  public void setLastName(final String lastName) {

    this.lastName = lastName;
  }

  private String middleName;

  private String nameType;

  private String lastName;

  private long evidenceID;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  /**
   * added a comparator to check if any of the attributes has been modifed
   * 
   * @param otherNames
   * @return
   */
  public int compareTo(final BDMNamesEvidenceVO otherNames) {

    return Comparator.comparing(BDMNamesEvidenceVO::getFirstName)
      .thenComparing(BDMNamesEvidenceVO::getLastName)
      .thenComparing(BDMNamesEvidenceVO::getMiddleName)
      .compare(otherNames, this);

  }
}
