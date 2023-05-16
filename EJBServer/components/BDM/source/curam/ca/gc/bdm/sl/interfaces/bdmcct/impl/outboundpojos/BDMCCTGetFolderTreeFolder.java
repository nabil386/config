package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * data expected in the data parameter from the response of the GetFolderTree
 * API call
 */
public class BDMCCTGetFolderTreeFolder {

  @SerializedName(value = "Id")
  private String ID;

  @SerializedName(value = "Name")
  private String name;

  @SerializedName(value = "Description")
  private String description;

  @SerializedName(value = "Folders")
  private List<BDMCCTGetFolderTreeFolder> folders;

  @SerializedName(value = "Templates")
  private List<BDMCCTGetFolderTreeTemplate> templates;

  /**
   * @return the id
   */
  public String getID() {

    return this.ID;
  }

  /**
   * @param id the id to set
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
   * @return the folders
   */
  public List<BDMCCTGetFolderTreeFolder> getFolders() {

    return this.folders;
  }

  /**
   * @param folders the folders to set
   */
  public void setFolders(final List<BDMCCTGetFolderTreeFolder> folders) {

    this.folders = folders;
  }

  /**
   * @return the templates
   */
  public List<BDMCCTGetFolderTreeTemplate> getTemplates() {

    return this.templates;
  }

  /**
   * @param templates the templates to set
   */
  public void
    setTemplates(final List<BDMCCTGetFolderTreeTemplate> templates) {

    this.templates = templates;
  }
}
