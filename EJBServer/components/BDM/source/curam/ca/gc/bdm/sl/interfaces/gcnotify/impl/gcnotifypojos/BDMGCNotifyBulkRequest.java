package curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BDMGCNotifyBulkRequest {

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("template_id")
  @Expose
  private String templateId;

  @SerializedName("rows")
  @Expose
  private List<List<String>> rows = null;

  public String getName() {

    return name;
  }

  public void setName(final String name) {

    this.name = name;
  }

  public String getTemplateId() {

    return templateId;
  }

  public void setTemplateId(final String templateId) {

    this.templateId = templateId;
  }

  public List<List<String>> getRows() {

    return rows;
  }

  public void setRows(final List<List<String>> rows) {

    this.rows = rows;
  }

}
