package curam.ca.gc.bdm.ws.sinvalidation.impl;

import curam.codetable.impl.GENDEREntry;
import curam.util.type.Date;

/**
 * A POJO class to assign the user details used in
 * {@linkplain ExternalValidateSINService#validate(ExternalValidateSINRequestDetails)}.
 */
public class WSSINValidationRequestDetails {

  // These come from the spec document @
  // https://ibm.ent.box.com/file/643784532100

  /** Individual's Social Insurance Number */
  private String sin = "";

  /** Individual's surname */
  private String surname = "";

  /** Individual's first name */
  private String givenName = "";

  /** Individual's parent's maiden surname */
  private String parentMaidenName = "";

  /** Individual's date of birth */
  private Date dateOfBirth = Date.kZeroDate;

  /** Individual's gender */
  private GENDEREntry gender = null;

  // -------

  public String getSin() {

    return this.sin;
  }

  public void setSin(final String sin) {

    this.sin = sin;
  }

  public String getSurname() {

    return this.surname;
  }

  public void setSurname(final String surname) {

    this.surname = surname;
  }

  public String getGivenName() {

    return this.givenName;
  }

  public void setGivenName(final String givenName) {

    this.givenName = givenName;
  }

  public Date getDateOfBirth() {

    return this.dateOfBirth;
  }

  public void setDateOfBirth(final Date dateOfBirth) {

    this.dateOfBirth = dateOfBirth;
  }

  public GENDEREntry getGender() {

    return this.gender;
  }

  public void setGender(final GENDEREntry gender) {

    this.gender = gender;
  }

  public String getParentMaidenName() {

    return this.parentMaidenName;
  }

  public void setParentMaidenName(final String parentMaidenName) {

    this.parentMaidenName = parentMaidenName;
  }

}
