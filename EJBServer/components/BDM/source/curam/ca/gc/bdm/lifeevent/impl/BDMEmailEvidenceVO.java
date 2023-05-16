package curam.ca.gc.bdm.lifeevent.impl;

import curam.util.type.Date;
import java.util.Comparator;
import java.util.Objects;

public class BDMEmailEvidenceVO {

  private String emailAddress;

  private String emailAddressType;

  private Date fromDate;

  private Date toDate;

  private boolean preferredInd;

  private boolean useForAlertsInd;

  private String alertFrequency;

  private long evidenceID;

  private String emailAddressChangeType;

  public String getEmailAddressChangeType() {

    return this.emailAddressChangeType;
  }

  public void setEmailAddressChangeType(final String emailAddressChangeType) {

    this.emailAddressChangeType = emailAddressChangeType;
  }

  public String getEmailAddress() {

    return this.emailAddress;
  }

  public void setEmailAddress(final String emailAddress) {

    this.emailAddress = emailAddress;
  }

  public String getEmailAddressType() {

    return this.emailAddressType;
  }

  public void setEmailAddressType(final String emailAddressType) {

    this.emailAddressType = emailAddressType;
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

  public boolean isUseForAlertsInd() {

    return this.useForAlertsInd;
  }

  public void setUseForAlertsInd(final boolean useForAlertsInd) {

    this.useForAlertsInd = useForAlertsInd;
  }

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
  public int compareTo(final BDMEmailEvidenceVO otherEmail) {

    return Comparator.comparing(BDMEmailEvidenceVO::getEmailAddress)
      .thenComparing(BDMEmailEvidenceVO::getAlertFrequency,
        Comparator.nullsFirst(Comparator.naturalOrder()))
      .thenComparing(BDMEmailEvidenceVO::isUseForAlertsInd,
        Comparator.nullsFirst(Comparator.naturalOrder()))
      .thenComparing(BDMEmailEvidenceVO::isPreferredInd,
        Comparator.nullsFirst(Comparator.naturalOrder()))
      .compare(otherEmail, this);

  }

  @Override
  public boolean equals(final Object otherEmail) {

    if (this == otherEmail) {
      return true;
    }

    if (otherEmail == null || getClass() != otherEmail.getClass()) {
      return false;
    }

    final BDMEmailEvidenceVO otherEmailVO = (BDMEmailEvidenceVO) otherEmail;

    return Objects.equals(emailAddress, otherEmailVO.getEmailAddress())
      && Objects.equals(emailAddressType, otherEmailVO.getEmailAddressType())
      && Objects.equals(alertFrequency, otherEmailVO.getAlertFrequency())
      && Objects.equals(useForAlertsInd, otherEmailVO.isUseForAlertsInd()
        && Objects.equals(preferredInd, otherEmailVO.isPreferredInd()));
  }

  public String getAlertFrequency() {

    return alertFrequency;
  }

  public void setAlertFrequency(final String alertFrequency) {

    this.alertFrequency = alertFrequency;
  }

}
