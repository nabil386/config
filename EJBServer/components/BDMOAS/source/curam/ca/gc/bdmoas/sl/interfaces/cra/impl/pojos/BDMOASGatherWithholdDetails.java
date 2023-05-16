package curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos;

import com.google.gson.annotations.SerializedName;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMAdministrativeRegion;
import curam.ca.gc.bdm.sl.interfaces.impl.commonpojos.BDMClientData;

/**
 * Parameters from the request- BDMCRAAdministrativeRegion.CRA Inbound
 * POJO.
 */

public class BDMOASGatherWithholdDetails {

  @SerializedName(value = "Client")
  private BDMClientData clientData;

  @SerializedName(value = "AdministrativeRegion")
  private BDMAdministrativeRegion adminRegion;

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

  /**
   * @return the adminRegion
   */
  public BDMAdministrativeRegion getAdminRegion() {

    return this.adminRegion;
  }

  /**
   * @param adminRegion the adminRegion to set
   */
  public void setAdminRegion(final BDMAdministrativeRegion adminRegion) {

    this.adminRegion = adminRegion;
  }

}
