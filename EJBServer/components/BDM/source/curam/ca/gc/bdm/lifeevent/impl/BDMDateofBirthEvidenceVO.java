package curam.ca.gc.bdm.lifeevent.impl;

import curam.util.type.Date;
import java.util.Objects;

/**
 *
 * VO for DOB evidence
 * BDM Project
 *
 *
 */
public class BDMDateofBirthEvidenceVO {

  private String birthLastName;

  private String comments;

  private Date dateOfBirth;

  private Date dateOfDeath;

  private String mothersBirthLastName;

  private long evidenceID;

  private Date attestationDate;

  private String bdmAgreeAttestation;

  private long attesteeCaseParticipant;

  private long person;

  /* BEGIN FEATURE 52093: Added */
  private String countryOfBirth;

  public String getCountryOfBirth() {

    return this.countryOfBirth;
  }

  public void setCountryOfBirth(final String countryOfBirth) {

    this.countryOfBirth = countryOfBirth;
  }
  /* END FEATURE 52093: */

  public long getAttesteeCaseParticipant() {

    return this.attesteeCaseParticipant;
  }

  public void setAttesteeCaseParticipant(final long attesteeCaseParticipant) {

    this.attesteeCaseParticipant = attesteeCaseParticipant;
  }

  public long getPerson() {

    return this.person;
  }

  public void setPerson(final long person) {

    this.person = person;
  }

  public String getBdmAgreeAttestation() {

    return this.bdmAgreeAttestation;
  }

  public void setBdmAgreeAttestation(final String bdmAgreeAttestation) {

    this.bdmAgreeAttestation = bdmAgreeAttestation;
  }

  public Date getAttestationDate() {

    return this.attestationDate;
  }

  public void setAttestationDate(final Date attestationDate) {

    this.attestationDate = attestationDate;
  }

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getBirthLastName() {

    return this.birthLastName;
  }

  public void setBirthLastName(final String birthLastName) {

    this.birthLastName = birthLastName;
  }

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  public String getMothersBirthLastName() {

    return this.mothersBirthLastName;
  }

  public void setMothersBirthLastName(final String mothersBirthLastName) {

    this.mothersBirthLastName = mothersBirthLastName;
  }

  public Date getDateOfBirth() {

    return this.dateOfBirth;
  }

  public void setDateOfBirth(final Date dateOfBirth) {

    this.dateOfBirth = dateOfBirth;
  }

  public Date getDateOfDeath() {

    return this.dateOfDeath;
  }

  public void setDateOfDeath(final Date dateOfDeath) {

    this.dateOfDeath = dateOfDeath;
  }

  // BEGIN TASK 21129 - map evidence information
  @Override
  public boolean equals(final Object dobVO) {

    if (this == dobVO) {
      return true;
    }

    if (dobVO == null || getClass() != dobVO.getClass()) {
      return false;
    }

    final BDMDateofBirthEvidenceVO dateofBirthVO =
      (BDMDateofBirthEvidenceVO) dobVO;
    /* BEGIN FEATURE 52093: Added countryOfBirth in the following */
    return Objects.equals(birthLastName, dateofBirthVO.getBirthLastName())
      && Objects.equals(mothersBirthLastName,
        dateofBirthVO.getMothersBirthLastName())
      && dateOfBirth.equals(dateofBirthVO.getDateOfBirth())
      && Objects.equals(comments, dateofBirthVO.getComments())
      && Objects.equals(attestationDate, dateofBirthVO.getAttestationDate())
      && Objects.equals(bdmAgreeAttestation,
        dateofBirthVO.getBdmAgreeAttestation())
      && countryOfBirth.equals(dateofBirthVO.getCountryOfBirth());
    /* END FEATURE 52093: */
  }
  // END TASK 21129 - map evidence information
}
