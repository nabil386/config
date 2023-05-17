package curam.ca.gc.bdm.lifeevent.impl;

import java.util.Objects;

/**
 * @since
 *
 */
public class BDMContactPreferenceEvidenceVO {

  private long participant;

  private String comments;

  private String preferredCommunicationMethod;

  private String preferredWrittenLanguage;

  private String preferredOralLanguage;

  private String preferredLanguage;

  public long getParticipant() {

    return this.participant;
  }

  public void setParticipant(final long participant) {

    this.participant = participant;
  }

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  public String getPreferredCommunication() {

    return this.preferredCommunicationMethod;
  }

  public void
    setPreferredCommunication(final String preferredCommunicationMethod) {

    this.preferredCommunicationMethod = preferredCommunicationMethod;
  }

  public String getPreferredWrittenLanguage() {

    return this.preferredWrittenLanguage;
  }

  public void
    setPreferredWrittenLanguage(final String preferredWrittenLanguage) {

    this.preferredWrittenLanguage = preferredWrittenLanguage;
  }

  public String getPreferredOralLanguage() {

    return this.preferredOralLanguage;
  }

  public void setPreferredOralLanguage(final String preferredOralLanguage) {

    this.preferredOralLanguage = preferredOralLanguage;
  }

  public String getPreferredLanguage() {

    return this.preferredLanguage;
  }

  public void setPreferredLanguage(final String preferredLanguage) {

    this.preferredLanguage = preferredLanguage;
  }

  private long evidenceID;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  // BEGIN TASK 21129 - map evidence information
  @Override
  public boolean equals(final Object contactPrefVOObj) {

    if (this == contactPrefVOObj) {
      return true;
    }

    if (contactPrefVOObj == null
      || getClass() != contactPrefVOObj.getClass()) {
      return false;
    }

    final BDMContactPreferenceEvidenceVO contactPrefVO =
      (BDMContactPreferenceEvidenceVO) contactPrefVOObj;

    // compare communication preference, preferred oral and written languages
    // for the object equality
    return Objects.equals(preferredCommunicationMethod,
      contactPrefVO.getPreferredCommunication())
      && Objects.equals(preferredOralLanguage,
        contactPrefVO.getPreferredOralLanguage())
      && Objects.equals(preferredWrittenLanguage,
        contactPrefVO.getPreferredWrittenLanguage());
  }
  // END TASK 21129 - map evidence information
}
