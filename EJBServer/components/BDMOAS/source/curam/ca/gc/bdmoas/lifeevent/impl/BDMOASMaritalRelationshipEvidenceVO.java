package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.util.type.Date;

/**
 * BDMOAS FEATURE 92921: Class Added
 * VO for Marital Relationship evidence
 *
 * @author SMisal
 *
 */
public class BDMOASMaritalRelationshipEvidenceVO {

  private long evidenceID;

  private long relatedCaseParticipantRoleID;

  private String relationshipStatus;

  private Date startDate;

  private Date endDate;

  private String relationshipChangeType;

  private String comments;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getRelationshipStatus() {

    return this.relationshipStatus;
  }

  public void setRelationshipStatus(final String relationshipStatus) {

    this.relationshipStatus = relationshipStatus;
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

  public String getRelationshipChangeType() {

    return this.relationshipChangeType;
  }

  public void setRelationshipChangeType(final String relationshipChangeType) {

    this.relationshipChangeType = relationshipChangeType;
  }

  public long getRelatedParticipantID() {

    return this.relatedCaseParticipantRoleID;
  }

  public void
    setRelatedParticipantID(final long relatedCaseParticipantRoleID) {

    this.relatedCaseParticipantRoleID = relatedCaseParticipantRoleID;
  }
}
