package curam.ca.gc.bdm.sl.interfaces.impl.commonpojos;

import com.google.gson.annotations.SerializedName;

/**
 * Client POJO.
 */
public class BDMClient {

  @SerializedName(value = "Client")
  private BDMClientData clientData;

  /**
   * @return the clientData
   */
  public BDMClientData getClientData() {

    return this.clientData;
  }

  /**
   * @param clientData the clientData to set
   */
  public void setClientData(final BDMClientData clientData) {

    this.clientData = clientData;
  }

}
