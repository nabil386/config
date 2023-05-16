package curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BDMGCNotifyRealTimePersonalizeData {

  @SerializedName("full_name")
  @Expose
  private String fullName;

  @SerializedName("alternate_id")
  @Expose
  private String alternateId;

  public String getFullName() {

    return this.fullName;
  }

  public void setFullName(final String fullName) {

    this.fullName = fullName;
  }

  public String getAlternateId() {

    return this.alternateId;
  }

  public void setAlternateId(final String alternateId) {

    this.alternateId = alternateId;
  }
}
