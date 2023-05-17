/**
 *
 */
package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Related Person Details POJO
 */
public class BDMRelatedPerson {

  @SerializedName(value = "PersonRelationshipCode")
  private BDMReferenceData personRelationshipCode;

  @SerializedName(value = "PersonSINIdentification")
  private BDMSINData sin;

  /**
   * @return the personRelationshipCode
   */
  public BDMReferenceData getPersonRelationshipCode() {

    return this.personRelationshipCode;
  }

  /**
   * @param personRelationshipCode the personRelationshipCode to set
   */
  public void
    setPersonRelationshipCode(final BDMReferenceData personRelationshipCode) {

    this.personRelationshipCode = personRelationshipCode;
  }

  /**
   * @return the sin
   */
  public BDMSINData getSin() {

    return this.sin;
  }

  /**
   * @param sin the sin to set
   */
  public void setSin(final BDMSINData sin) {

    this.sin = sin;
  }

}
