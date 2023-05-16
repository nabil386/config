package curam.ca.gc.bdm.lifeevent.impl;

import curam.util.type.Date;

public class BDMAttestationVO {

  Date attestationPeriodStartDate = Date.getCurrentDate();

  String attestationType = "";

  boolean isEligible = false;

  public boolean isEligible() {

    return this.isEligible;
  }

  public void setEligible(final boolean isEligible) {

    this.isEligible = isEligible;
  }

  public Date getAttestationPeriodStartDate() {

    return this.attestationPeriodStartDate;
  }

  public void
    setAttestationPeriodStartDate(final Date attestationPeriodStartDate) {

    this.attestationPeriodStartDate = attestationPeriodStartDate;
  }

  public String getAttestationType() {

    return this.attestationType;
  }

  public void setAttestationType(final String attestationType) {

    this.attestationType = attestationType;
  }

}
