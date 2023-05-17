package curam.ca.gc.bdm.lifeevent.impl;

import curam.util.type.Date;
import java.util.Objects;

/**
 * The POJO for the Identification evidence.
 *
 */
public class BDMIdentificationEvidenceVO {

  private String alternateID;

  private String altIDType;

  private String bdmReceivedFrom;

  private String country;

  private Date fromDate;

  private Date toDate;

  private boolean preferredInd;

  private long evidenceID;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getAlternateID() {

    return this.alternateID;
  }

  public void setAlternateID(final String alternateID) {

    this.alternateID = alternateID;
  }

  public String getAltIDType() {

    return this.altIDType;
  }

  public void setAltIDType(final String altIDType) {

    this.altIDType = altIDType;
  }

  public Date getFromDate() {

    return this.fromDate;
  }

  public void setFromDate(final Date fromDate) {

    this.fromDate = fromDate;
  }

  public Date getToDate() {

    return this.toDate;
  }

  public void setToDate(final Date toDate) {

    this.toDate = toDate;
  }

  public boolean isPreferredInd() {

    return this.preferredInd;
  }

  public void setPreferredInd(final boolean preferredInd) {

    this.preferredInd = preferredInd;
  }

  public String getBdmReceivedFrom() {

    return this.bdmReceivedFrom;
  }

  public void setBdmReceivedFrom(final String bdmReceivedFrom) {

    this.bdmReceivedFrom = bdmReceivedFrom;
  }

  public String getCountry() {

    return this.country;
  }

  public void setCountry(final String country) {

    this.country = country;
  }

  // BEGIN TASK 21129 - map evidence information
  @Override
  public boolean equals(final Object identificationVOObj) {

    if (this == identificationVOObj) {
      return true;
    }

    if (identificationVOObj == null
      || getClass() != identificationVOObj.getClass()) {
      return false;
    }

    final BDMIdentificationEvidenceVO identificationVO =
      (BDMIdentificationEvidenceVO) identificationVOObj;

    return Objects.equals(altIDType, identificationVO.getAltIDType())
      && Objects.equals(alternateID, identificationVO.getAlternateID());
  }

  // END TASK 21129 - map evidence information
}
