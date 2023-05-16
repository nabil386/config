package curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BDMGCNotifyRealTimeEmailRequest {

  @SerializedName("email_address")
  @Expose
  private String emailAddress;

  @SerializedName("template_id")
  @Expose
  private String templateId;

  @SerializedName("personalisation")
  @Expose
  private BDMGCNotifyRealTimePersonalizeData personalisation = null;

  public String getEmailAddress() {

    return emailAddress;
  }

  public void setEmailAddress(final String emailAddress) {

    this.emailAddress = emailAddress;
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
