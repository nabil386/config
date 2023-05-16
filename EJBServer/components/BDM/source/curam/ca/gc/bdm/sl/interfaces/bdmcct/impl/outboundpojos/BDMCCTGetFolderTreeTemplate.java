package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * parameters expected from data expected in the data parameter from the
 * response of the GetFolderTree API call
 */
public class BDMCCTGetFolderTreeTemplate {

  @SerializedName(value = "Id")
  private String ID;

  @SerializedName(value = "Name")
  private String name;

  @SerializedName(value = "Description")
  private String description;

  @SerializedName(value = "Path")
  private String path;

  @SerializedName(value = "Version")
  private String version;

  @SerializedName(value = "EffectiveDateSelection")
  private String effectiveDateSelection;

  @SerializedName(value = "fields")
  private List<String> fields;

  @SerializedName(value = "mandatoryFields")
  private List<String> mandatoryFields;

  /**
   * @return the ID
   */
  public String getID() {

    return this.ID;
  }

  /**
   * @param ID the ID to set
   */
  public void setID(final String ID) {

    this.ID = ID;
  }

  /**
   * @return the name
   */
  public String getName() {

    return this.name;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {

    this.name = name;
  }

  /**
   * @return the description
   */
  public String getDescription() {

    return this.description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(final String description) {

    this.description = description;
  }

  /**
   * @return the path
   */
  public String getPath() {

    return this.path;
  }

  /**
   * @param path the path to set
   */
  public void setPath(final String path) {

    this.path = path;
  }

  /**
   * @return the version
   */
  public String getVersion() {

    return this.version;
  }

  /**
   * @param version the version to set
   */
  public void setVersion(final String version) {

    this.version = version;
  }

  /**
   * @return the effectiveDateSelection
   */
  public String getEffectiveDateSelection() {

    return this.effectiveDateSelection;
  }

  /**
   * @param effectiveDateSelection the effectiveDateSelection to set
   */
  public void setEffectiveDateSelection(final String effectiveDateSelection) {

    this.effectiveDateSelection = effectiveDateSelection;
  }

  /**
   * @return the fields
   */
  public List<String> getFields() {

    return this.fields;
  }

  /**
   * @param fields the fields to set
   */
  public void setFields(final List<String> fields) {

    this.fields = fields;
  }

  /**
   * @return the mandatoryFields
   */
  public List<String> getMandatoryFields() {

    return this.mandatoryFields;
  }

  /**
   * @param mandatoryFields the mandatoryFields to set
   */
  public void setMandatoryFields(final List<String> mandatoryFields) {

    this.mandatoryFields = mandatoryFields;
  }

}
