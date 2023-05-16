package curam.ca.gc.bdmoas.sl.interfaces.cra.impl.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Parameters from the request- BDMCRAGatherWithholdData.CRA Inbound
 * POJO.
 */

public class BDMOASGatherWithholdOutbound {

  @SerializedName(value = "gather-withhold-outbound")
  private BDMOASGatherWithholdDetails gatherWithholdOutbound;

  /**
   * @return the gatherWithholdOutbound
   */
  public BDMOASGatherWithholdDetails getGatherWithholdOutbound() {

    return this.gatherWithholdOutbound;
  }

  /**
   * @param gatherWithholdOutbound the gatherWithholdOutbound to set
   */
  public void setGatherWithholdOutbound(
    final BDMOASGatherWithholdDetails gatherWithholdOutbound) {

    this.gatherWithholdOutbound = gatherWithholdOutbound;
  }

}
