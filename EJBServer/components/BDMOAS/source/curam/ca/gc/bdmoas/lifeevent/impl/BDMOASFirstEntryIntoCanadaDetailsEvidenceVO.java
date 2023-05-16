package curam.ca.gc.bdmoas.lifeevent.impl;

import curam.util.type.Date;

/**
 * BDMOAS FEATURE 92921: Class Added
 * VO for First Entry Into Canada Details evidence
 *
 * @author SMisal
 *
 */
public class BDMOASFirstEntryIntoCanadaDetailsEvidenceVO {

  private long evidenceID;

  private String immigrationDoc;

  private String otherImmigrationDocDetails;

  private Date arrivalDate;

  private String arrivalCity;

  private String arrivalProvince;

  private String comments;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getImmigrationDoc() {

    return this.immigrationDoc;
  }

  public void setImmigrationDoc(final String immigrationDoc) {

    this.immigrationDoc = immigrationDoc;
  }

  public String getOtherImmigrationDocDetails() {

    return this.otherImmigrationDocDetails;
  }

  public void
    setOtherImmigrationDocDetails(final String otherImmigrationDocDetails) {

    this.otherImmigrationDocDetails = otherImmigrationDocDetails;
  }

  public Date getArrivalDate() {

    return this.arrivalDate;
  }

  public void setArrivalDate(final Date arrivalDate) {

    this.arrivalDate = arrivalDate;
  }

  public String getComments() {

    return this.comments;
  }

  public void setComments(final String comments) {

    this.comments = comments;
  }

  public String getArrivalCity() {

    return this.arrivalCity;
  }

  public void setArrivalCity(final String arrivalCity) {

    this.arrivalCity = arrivalCity;
  }

  public String getArrivalProvince() {

    return this.arrivalProvince;
  }

  public void setArrivalProvince(final String arrivalProvince) {

    this.arrivalProvince = arrivalProvince;
  }
}
