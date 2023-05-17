
package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Context {

  @SerializedName("nc")
  @Expose
  private String nc;

  @SerializedName("stat")
  @Expose
  private String stat;

  @SerializedName("can")
  @Expose
  private String can;

  @SerializedName("wsaddr")
  @Expose
  private String wsaddr;

  @SerializedName("fault")
  @Expose
  private String fault;

  /**
   * No args constructor for use in serialization
   *
   */
  public Context() {

  }

  /**
   *
   * @param can
   * @param stat
   * @param nc
   * @param fault
   * @param wsaddr
   */
  public Context(final String nc, final String stat, final String can,
    final String wsaddr, final String fault) {

    super();
    this.nc = nc;
    this.stat = stat;
    this.can = can;
    this.wsaddr = wsaddr;
    this.fault = fault;
  }

  public String getNc() {

    return nc;
  }

  public void setNc(final String nc) {

    this.nc = nc;
  }

  public Context withNc(final String nc) {

    this.nc = nc;
    return this;
  }

  public String getStat() {

    return stat;
  }

  public void setStat(final String stat) {

    this.stat = stat;
  }

  public Context withStat(final String stat) {

    this.stat = stat;
    return this;
  }

  public String getCan() {

    return can;
  }

  public void setCan(final String can) {

    this.can = can;
  }

  public Context withCan(final String can) {

    this.can = can;
    return this;
  }

  public String getWsaddr() {

    return wsaddr;
  }

  public void setWsaddr(final String wsaddr) {

    this.wsaddr = wsaddr;
  }

  public Context withWsaddr(final String wsaddr) {

    this.wsaddr = wsaddr;
    return this;
  }

  public String getFault() {

    return fault;
  }

  public void setFault(final String fault) {

    this.fault = fault;
  }

  public Context withFault(final String fault) {

    this.fault = fault;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(Context.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("nc");
    sb.append('=');
    sb.append(this.nc == null ? "<null>" : this.nc);
    sb.append(',');
    sb.append("stat");
    sb.append('=');
    sb.append(this.stat == null ? "<null>" : this.stat);
    sb.append(',');
    sb.append("can");
    sb.append('=');
    sb.append(this.can == null ? "<null>" : this.can);
    sb.append(',');
    sb.append("wsaddr");
    sb.append('=');
    sb.append(this.wsaddr == null ? "<null>" : this.wsaddr);
    sb.append(',');
    sb.append("fault");
    sb.append('=');
    sb.append(this.fault == null ? "<null>" : this.fault);
    sb.append(',');
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.setCharAt(sb.length() - 1, ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

}
