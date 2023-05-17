package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Client Payload POJO.
 */
public class BDMClientPayload {

  @SerializedName(value = "id")
  private String id;

  @SerializedName(value = "specversion")
  private String specVersion;

  @SerializedName(value = "type")
  private String type;

  @SerializedName(value = "source")
  private String source;

  @SerializedName(value = "time")
  private String time;

  @SerializedName(value = "datacontenttype")
  private String dataContentType;

  @SerializedName(value = "dataschema")
  private String dataSchema;

  @SerializedName(value = "data")
  private BDMClient data;

  /**
   * Getter - id
   */
  public String getID() {

    return this.id;
  }

  /**
   * Setter - id
   */

  public void setID(final String id) {

    this.id = id;
  }

  /**
   * Getter specVersion
   */

  public String getSpecVersion() {

    return this.specVersion;
  }

  /**
   * Setter - specVersion
   */

  public void setSpecVersion(final String specVersion) {

    this.specVersion = specVersion;
  }

  /**
   * Getter - type
   */

  public String getType() {

    return this.type;
  }

  /**
   * Setter - type
   */

  public void setType(final String type) {

    this.type = type;
  }

  /**
   * Getter - source
   */

  public String getSource() {

    return this.source;
  }

  /**
   * Setter - source
   */

  public void setSource(final String source) {

    this.source = source;
  }

  /**
   * Setter - time
   */

  public void setTime(final String time) {

    this.time = time;
  }

  /**
   * Getter - time
   */

  public String getTime() {

    return this.time;
  }

  /**
   * Getter - data
   */
  public BDMClient getData() {

    return data;
  }

  /**
   * Setter - data
   */

  public void setData(final BDMClient data) {

    this.data = data;
  }

  /**
   * Getter - dataContentType
   */
  public String getDataContentType() {

    return dataContentType;
  }

  /**
   * Setter - dataContentType
   */

  public void setDataContentType(final String dataContentType) {

    this.dataContentType = dataContentType;
  }

  /**
   * Getter - dataSchema
   */
  public String getDataSchema() {

    return dataSchema;
  }

  /**
   * Setter - dataSchema
   */

  public void setDataSchema(final String dataSchema) {

    this.dataSchema = dataSchema;
  }

}
