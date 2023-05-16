package curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * CRA GIS & (Re)Assessment Interface POJO.
 */
public class BDMOASGISTakeUpInbound {

  @SerializedName(value = "id")
  private String id;

  @SerializedName(value = "specversion")
  private String specversion;

  @SerializedName(value = "type")
  private String type;

  @SerializedName(value = "source")
  private String source;

  @SerializedName(value = "partitionKey")
  private String partitionKey;

  @SerializedName(value = "Data")
  private BDMOASCRAIndividualIncome data;

  /**
   * @return id
   */
  public String getId() {

    return this.id;
  }

  /**
   * @param id the id to set
   */

  public void setId(final String id) {

    this.id = id;
  }

  /**
   * @return specversion
   */

  public String getSpecversion() {

    return this.specversion;
  }

  /**
   * @param specversion the specversion to set
   */

  public void setSpecversion(final String specversion) {

    this.specversion = specversion;
  }

  /**
   * @return type
   */

  public String getType() {

    return this.type;
  }

  /**
   * @param type the type to set
   */

  public void setType(final String type) {

    this.type = type;
  }

  /**
   * @return source
   */

  public String getSource() {

    return this.source;
  }

  /**
   * @param source the source to set
   */

  public void setSource(final String source) {

    this.source = source;
  }

  /**
   * @param partitionKey the partitionKey to set
   */

  public void setPartitionKey(final String partitionKey) {

    this.source = partitionKey;
  }

  /**
   * @return partitionKey
   */

  public String getPartitionKey() {

    return this.partitionKey;
  }

  /**
   * @return data
   */
  public BDMOASCRAIndividualIncome getData() {

    return data;
  }

  /**
   * @param data the data to set
   */

  public void setData(final BDMOASCRAIndividualIncome data) {

    this.data = data;
  }

}
