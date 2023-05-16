package curam.ca.gc.bdm.lifeevent.impl;

import curam.util.type.Date;

/**
 * @since ADO-21129
 *
 * The POJO for the Voluntary Tax Withhold Evidence.
 */
public class BDMVoluntaryTaxWithholdEvidenceVO {

  private String amount;

  private String caseParticipant;

  private String percentageValue;

  private String voluntaryTaxWithholdType;

  private Date startDate;

  private Date endDate;

  private String comments;

  private long evidenceID;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
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

  public String getAmount() {

    return amount;
  }

  public void setAmount(final String amount) {

    this.amount = amount;
  }

  public String getCaseParticipant() {

    return caseParticipant;
  }

  public void setCaseParticipant(final String caseParticipant) {

    this.caseParticipant = caseParticipant;
  }

  public String getPercentageValue() {

    return percentageValue;
  }

  public void setPercentageValue(final String percentageValue) {

    this.percentageValue = percentageValue;
  }

  public String getVoluntaryTaxWithholdType() {

    return voluntaryTaxWithholdType;
  }

  public void
    setVoluntaryTaxWithholdType(final String voluntaryTaxWithholdType) {

    this.voluntaryTaxWithholdType = voluntaryTaxWithholdType;
  }

}
