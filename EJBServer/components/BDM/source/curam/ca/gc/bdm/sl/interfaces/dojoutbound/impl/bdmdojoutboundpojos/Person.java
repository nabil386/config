
package curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person {

  @SerializedName("PersonSINIdentification")
  @Expose
  private Integer PersonSINIdentification;

  @SerializedName("PersonObligationID")
  @Expose
  private String PersonObligationID;

  @SerializedName("PersonObligationIDSuffix")
  @Expose
  private String PersonObligationIDSuffix;

  public Integer getPersonSINIdentification() {

    return PersonSINIdentification;
  }

  public void
    setPersonSINIdentification(final Integer personSINIdentification) {

    this.PersonSINIdentification = personSINIdentification;
  }

  public String getPersonObligationID() {

    return PersonObligationID;
  }

  public void setPersonObligationID(final String personObligationID) {

    this.PersonObligationID = personObligationID;
  }

  public String getPersonObligationIDSuffix() {

    return PersonObligationIDSuffix;
  }

  public void
    setPersonObligationIDSuffix(final String personObligationIDSuffix) {

    this.PersonObligationIDSuffix = personObligationIDSuffix;
  }

}
