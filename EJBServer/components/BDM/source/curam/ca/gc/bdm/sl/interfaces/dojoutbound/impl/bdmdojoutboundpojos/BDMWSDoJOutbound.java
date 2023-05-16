
package curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BDMWSDoJOutbound {

  @SerializedName("Metadata")
  @Expose
  private Metadata metadata;

  @SerializedName("Obligation")
  @Expose
  private Obligation obligation;

  public Metadata getMetadata() {

    return metadata;
  }

  public void setMetadata(final Metadata metadata) {

    this.metadata = metadata;
  }

  public Obligation getObligation() {

    return obligation;
  }

  public void setObligation(final Obligation obligation) {

    this.obligation = obligation;
  }

  @Override
  public String toString() {

    return "BDMWSDoJOutbound [metadata=" + this.metadata + ", obligation="
      + this.obligation + "]";
  }

}
