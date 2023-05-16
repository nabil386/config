package curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BDMGCNotifyRealTimeSMSRequest {

  @SerializedName("phone_number")
  @Expose
  private String phoneNumber;

  @SerializedName("template_id")
  @Expose
  private String templateId;

  @SerializedName("personalisation")
  @Expose
  private BDMGCNotifyRealTimePersonalizeData personalisation = null;

  public String getPhoneNumber() {

    return this.phoneNumber;
  }

  public void setPhoneNumber(final String phoneNumber) {

    this.phoneNumber = phoneNumber;
  }

  public String getTemplateId() {

    return templateId;
  }

  public void setTemplateId(final String templateId) {

    this.templateId = templateId;
  }

  public BDMGCNotifyRealTimePersonalizeData getPersonalisation() {

    return personalisation;
  }

  public void setPersonalisation(
    final BDMGCNotifyRealTimePersonalizeData personalisation) {

    this.personalisation = personalisation;
  }

}
