
package curam.ca.gc.bdm.sl.interfaces.wssinvalidate.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Context {

  @SerializedName("can")
  @Expose
  private String can;

  @SerializedName("ss")
  @Expose
  private String ss;

  @SerializedName("nc")
  @Expose
  private String nc;

  @SerializedName("fault")
  @Expose
  private String fault;

  @SerializedName("per")
  @Expose
  private String per;

  public String getCan() {

    return can;
  }

  public void setCan(final String can) {

    this.can = can;
  }

  public String getSs() {

    return ss;
  }

  public void setSs(final String ss) {

    this.ss = ss;
  }

  public String getNc() {

    return nc;
  }

  public void setNc(final String nc) {

    this.nc = nc;
  }

  public String getFault() {

    return fault;
  }

  public void setFault(final String fault) {

    this.fault = fault;
  }

  public String getPer() {

    return per;
  }

  public void setPer(final String per) {

    this.per = per;
  }

}
