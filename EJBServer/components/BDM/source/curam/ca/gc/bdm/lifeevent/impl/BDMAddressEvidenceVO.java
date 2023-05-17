package curam.ca.gc.bdm.lifeevent.impl;

import curam.util.type.Date;
import java.util.Comparator;

public class BDMAddressEvidenceVO {

  private String addressType;

  private String addressData;

  private Date fromDate;

  private Date toDate;

  private boolean preferredInd;

  private long evidenceID;

  private String poBox;

  private String province;

  private String countryCode;

  private String streetNumber;

  private String streetName;

  private String unitAptSuite;

  private String city;

  private String usState;

  private String state;

  private String zipCode;

  private String postalCode;

  private long addressID;

  public String getAddressType() {

    return this.addressType;
  }

  public void setAddressType(final String addressType) {

    this.addressType = addressType;
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

  public long getEvidenceID() {

    return this.evidenceID;
  }

  public void setEvidenceID(final long evidenceID) {

    this.evidenceID = evidenceID;
  }

  public String getPoBox() {

    return this.poBox;
  }

  public void setPoBox(final String poBox) {

    this.poBox = poBox;
  }

  public String getProvince() {

    return this.province;
  }

  public void setProvince(final String province) {

    this.province = province;
  }

  public String getCountryCode() {

    return this.countryCode;
  }

  public void setCountryCode(final String countryCode) {

    this.countryCode = countryCode;
  }

  public String getStreetNumber() {

    return this.streetNumber;
  }

  public void setStreetNumber(final String streetNumber) {

    this.streetNumber = streetNumber;
  }

  public String getStreetName() {

    return this.streetName;
  }

  public void setStreetName(final String streetName) {

    this.streetName = streetName;
  }

  public String getUnitAptSuite() {

    return this.unitAptSuite;
  }

  public void setUnitAptSuite(final String unitAptSuite) {

    this.unitAptSuite = unitAptSuite;
  }

  public String getCity() {

    return this.city;
  }

  public void setCity(final String city) {

    this.city = city;
  }

  public String getUsState() {

    return this.usState;
  }

  public void setUsState(final String usState) {

    this.usState = usState;
  }

  public String getState() {

    return this.state;
  }

  public void setState(final String state) {

    this.state = state;
  }

  public String getZipCode() {

    return this.zipCode;
  }

  public void setZipCode(final String zipCode) {

    this.zipCode = zipCode;
  }

  public String getPostalCode() {

    return this.postalCode;
  }

  public void setPostalCode(final String postalCode) {

    this.postalCode = postalCode;
  }

  public boolean isPreferredInd() {

    return preferredInd;
  }

  public void setPreferredInd(final boolean preferredInd) {

    this.preferredInd = preferredInd;
  }

  public String getAddressData() {

    return this.addressData;
  }

  public void setAddressData(final String addressData) {

    this.addressData = addressData;
  }

  public long getAddressID() {

    return addressID;
  }

  public void setAddressID(final long addressID) {

    this.addressID = addressID;
  }

  /**
   * added a comparator to check if any of the attributes has been modifed
   *
   * @param otherNames
   * @return
   */
  public int compareTo(final BDMAddressEvidenceVO otherAddress) {

    return Comparator
      .comparing(BDMAddressEvidenceVO::getAddressData,
        Comparator.nullsFirst(Comparator.naturalOrder()))
      .compare(otherAddress, this);

  }

}
