package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Reference Data POJO
 */
public class BDMReferenceData {

  @SerializedName(value = "ReferenceDataID")
  private String referenceDataID;

  @SerializedName(value = "ReferenceDataName")
  private String referenceDataName;

  /**
   * @return the referenceDataID
   */
  public String getReferenceDataID() {

    return this.referenceDataID;
  }

  /**
   * @param referenceDataID the referenceDataID to set
   */
  public void setReferenceDataID(final String referenceDataID) {

    this.referenceDataID = referenceDataID;
  }

  /**
   * @return the referenceDataName
   */
  public String getReferenceDataName() {

    return this.referenceDataName;
  }

  /**
   * @param referenceDataName the referenceDataName to set
   */
  public void setReferenceDataName(final String referenceDataName) {

    this.referenceDataName = referenceDataName;
  }
}
