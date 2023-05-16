package curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters from the request- BDMCRAGatherWitholdRequest.CRA Inbound
 * POJO.
 */

public class BDMOASGatherWithholdOutboundData {

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

  @SerializedName(value = "data")
  private BDMOASGatherWithholdOutbound data;

  /**
   * @return the id
   */
  public String getId() {

    return this.id;
  }

  /**
   * @param uuid the id to set
   */
  public void setId(final String id) {

    this.id = id;
  }

  /**
   * @return the specversion 
   */
  public String getSpecversion() {

    return this.specversion;
  }

  /**
   * @param specversion the specversion to set
   */
  public void setSpecversion(final String specversion) {

    this.specversion = specversion;
  }

  /**
   * @return the type
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
   * @return the source
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
   * @return the partitionKey
   */
  public String getPartitionKey() {

    return this.partitionKey;
  }

  /**
   * @param partitionKey the partitionKey to set
   */
  public void setPartitionKey(final String partitionKey) {

    this.partitionKey = partitionKey;
  }

  /**
   * @return the data
   */
  public BDMOASGatherWithholdOutbound getData() {

    return this.data;
  }

  /**
   * @param data the data to set
   */
  public void setData(final BDMOASGatherWithholdOutbound data) {

    this.data = data;
  }
}
