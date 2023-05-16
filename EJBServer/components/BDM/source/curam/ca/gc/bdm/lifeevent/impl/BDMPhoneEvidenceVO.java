package curam.ca.gc.bdm.lifeevent.impl;

import curam.util.type.Date;
import java.util.Comparator;
import java.util.Objects;

public class BDMPhoneEvidenceVO {

  private String phoneCountryCode;

  private String phoneAreaCode;

  private String phoneNumber;

  private String phoneExtension;

  private Date fromDate;

  private Date toDate;

  private String phoneType;

  private boolean preferredInd;

  private boolean useForAlertsInd;

  private boolean morningInd;

  private boolean afternoonInd;

  private boolean eveningInd;

  private long evidenceID;

  private String alertPreference;

  private String phoneNumberChangeType;

  public String getPhoneNumberChangeType() {

    return this.phoneNumberChangeType;
  }

  public void setPhoneNumberChangeType(final String phoneNumberChangeType) {

    this.phoneNumberChangeType = phoneNumberChangeType;
  }

  public String getAlertPreference() {

    return this.alertPreference;
  }

  public void setAlertPreference(final String alertPreference) {

    this.alertPreference = alertPreference;
  }

  private String alertFrequency;

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getPhoneCountryCode() {

    return this.phoneCountryCode;
  }

  public void setPhoneCountryCode(final String phoneCountryCode) {

    this.phoneCountryCode = phoneCountryCode;
  }

  public String getPhoneAreaCode() {

    return this.phoneAreaCode;
  }

  public void setPhoneAreaCode(final String phoneAreaCode) {

    this.phoneAreaCode = phoneAreaCode;
  }

  public String getPhoneNumber() {

    return this.phoneNumber;
  }

  public void setPhoneNumber(final String phoneNumber) {

    this.phoneNumber = phoneNumber;
  }

  public String getPhoneExtension() {

    return this.phoneExtension;
  }

  public void setPhoneExtension(final String phoneExtension) {

    this.phoneExtension = phoneExtension;
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

  public String getPhoneType() {

    return this.phoneType;
  }

  public void setPhoneType(final String phoneType) {

    this.phoneType = phoneType;
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

  public boolean isMorningInd() {

    return this.morningInd;
  }

  public void setMorningInd(final boolean morningInd) {

    this.morningInd = morningInd;
  }

  public boolean isAfternoonInd() {

    return this.afternoonInd;
  }

  public void setAfternoonInd(final boolean afternoonInd) {

    this.afternoonInd = afternoonInd;
  }

  public boolean isEveningInd() {

    return this.eveningInd;
  }

  public void setEveningInd(final boolean eveningInd) {

    this.eveningInd = eveningInd;
  }

  /**
   * added a comparator to check if any of the attributes has been modifed
   *
   * @param otherNames
   * @return
   */
  public int compareTo(final BDMPhoneEvidenceVO otherPhone) {

    return Comparator.comparing(BDMPhoneEvidenceVO::getPhoneAreaCode)
      .thenComparing(BDMPhoneEvidenceVO::getPhoneExtension,
        Comparator.nullsFirst(Comparator.naturalOrder()))
      .thenComparing(BDMPhoneEvidenceVO::getAlertFrequency,
        Comparator.nullsFirst(Comparator.naturalOrder()))
      .thenComparing(BDMPhoneEvidenceVO::isUseForAlertsInd,
        Comparator.nullsFirst(Comparator.naturalOrder()))
      .thenComparing(BDMPhoneEvidenceVO::isAfternoonInd,
        Comparator.nullsFirst(Comparator.naturalOrder()))
      .thenComparing(BDMPhoneEvidenceVO::isMorningInd,
        Comparator.nullsFirst(Comparator.naturalOrder()))
      .thenComparing(BDMPhoneEvidenceVO::isEveningInd,
        Comparator.nullsFirst(Comparator.naturalOrder()))
      .thenComparing(BDMPhoneEvidenceVO::getPhoneCountryCode)
      .thenComparing(BDMPhoneEvidenceVO::getPhoneNumber)
      .compare(otherPhone, this);
  }

  @Override
  public boolean equals(final Object otherPhone) {

    if (this == otherPhone) {
      return true;
    }

    if (otherPhone == null || getClass() != otherPhone.getClass()) {
      return false;
    }

    final BDMPhoneEvidenceVO otherPhoneVO = (BDMPhoneEvidenceVO) otherPhone;

    return Objects.equals(phoneNumber, otherPhoneVO.getPhoneNumber())
      && Objects.equals(phoneAreaCode, otherPhoneVO.getPhoneAreaCode())
      && Objects.equals(phoneCountryCode, otherPhoneVO.getPhoneCountryCode())
      && Objects.equals(phoneExtension, otherPhoneVO.getPhoneExtension())
      && Objects.equals(phoneType, otherPhoneVO.getPhoneType())
      && Objects.equals(preferredInd, otherPhoneVO.isPreferredInd())
      && Objects.equals(useForAlertsInd,
        otherPhoneVO.isUseForAlertsInd()
          && Objects.equals(alertFrequency, otherPhoneVO.getAlertFrequency())
          && Objects.equals(morningInd, otherPhoneVO.isMorningInd())
          && Objects.equals(afternoonInd, otherPhoneVO.isAfternoonInd())
          && Objects.equals(eveningInd, otherPhoneVO.isEveningInd()));
  }

  public String getAlertFrequency() {

    return alertFrequency;
  }

  public void setAlertFrequency(final String alertFrequency) {

    this.alertFrequency = alertFrequency;
  }

}
