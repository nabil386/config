package curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BDMGCNotifyResponse {

  @SerializedName("response")
  @Expose
  private String response;

  private int responseCode;

  public BDMGCNotifyResponse() {

  }

  public String getResponse() {

    return response;
  }

  public void setResponse(String response) {

    this.response = response;
  }

  public int getResponseCode() {

    return responseCode;
  }

  public void setResponseCode(int responseCode) {

    this.responseCode = responseCode;
  }
}
