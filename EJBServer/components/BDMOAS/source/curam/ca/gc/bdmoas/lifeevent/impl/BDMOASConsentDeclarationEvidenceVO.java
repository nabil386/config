package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.util.type.Date;

/**
 * BDMOAS FEATURE 92921: Class Added
 * VO for consent and declaration evidence
 *
 * @author SMisal
 *
 */
public class BDMOASConsentDeclarationEvidenceVO {

  private long evidenceID;

  private String participant;

  private String witness;

  private String signatureType;

  private String relationshipType;

  private Date dateSigned;

  private Date witnessDateSigned;

  private String comments;

  private boolean isSigningOnBehalf;

  private boolean isWitness;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getParticipant() {

    return participant;
  }

  public void setParticipant(final String participant) {

    this.participant = participant;
  }

  public String getWitness() {

    return witness;
  }

  public void setWitness(final String witness) {

    this.witness = witness;
  }

  public Date getDateSigned() {

    return this.dateSigned;
  }

  public void setDateSigned(final Date dateSigned) {

    this.dateSigned = dateSigned;
  }

  public Date getWitnessDateSigned() {

    return this.witnessDateSigned;
  }

  public void setWitnessDateSigned(final Date witnessDateSigned) {

    this.witnessDateSigned = witnessDateSigned;
  }

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  public String getSignatureType() {

    return signatureType;
  }

  public void setSignatureType(final String signatureType) {

    this.signatureType = signatureType;
  }

  public String getRelationshipType() {

    return relationshipType;
  }

  public void setRelationshipType(final String relationshipType) {

    this.relationshipType = relationshipType;
  }

  public boolean getIsSigningOnBehalfInd() {

    return isSigningOnBehalf;
  }

  public void setIsSigningOnBehalfInd(final boolean isSigningOnBehalf) {

    this.isSigningOnBehalf = isSigningOnBehalf;
  }

  public boolean getIsWitnessInd() {

    return isWitness;
  }

  public void setIsWitnessInd(final boolean isWitness) {

    this.isWitness = isWitness;
  }

}
