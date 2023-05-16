
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

// Created manually as part of passing request to Rest API
// @author : Chandan Kumar

public class WSSearchTableRequest {

  private String ncPersonBirthDate;

  private String ncPersonGivenName;

  private String ncPersonMiddleName;

  private String ncPersonSurName;

  private String ssParentMaidenName;

  private String perPersonSINIdentification;

  public String getNcPersonBirthDate() {

    return ncPersonBirthDate;
  }

  public void setNcPersonBirthDate(final String ncPersonBirthDate) {

    this.ncPersonBirthDate = ncPersonBirthDate;
  }

  public String getNcPersonGivenName() {

    return ncPersonGivenName;
  }

  public void setNcPersonGivenName(final String ncPersonGivenName) {

    this.ncPersonGivenName = ncPersonGivenName;
  }

  public String getNcPersonMiddleName() {

    return ncPersonMiddleName;
  }

  public void setNcPersonMiddleName(final String ncPersonMiddleName) {

    this.ncPersonMiddleName = ncPersonMiddleName;
  }

  public String getNcPersonSurName() {

    return ncPersonSurName;
  }

  public void setNcPersonSurName(final String ncPersonSurName) {

    this.ncPersonSurName = ncPersonSurName;
  }

  public String getSsParentMaidenName() {

    return ssParentMaidenName;
  }

  public void setSsParentMaidenName(final String ssParentMaidenName) {

    this.ssParentMaidenName = ssParentMaidenName;
  }

  public String getPerPersonSINIdentification() {

    return perPersonSINIdentification;
  }

  public void
    setPerPersonSINIdentification(final String perPersonSINIdentification) {

    this.perPersonSINIdentification = perPersonSINIdentification;
  }

}
