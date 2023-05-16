package curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos;

import com.google.gson.annotations.SerializedName;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMClientData;

/**
 * Parameters from the request- BDMCRAData.CRA Inbound
 * POJO.
 */
public class BDMOASCRAData {

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
