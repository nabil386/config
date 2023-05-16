
package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WsaddrStatistics {

  @SerializedName("stat:LogEndDateTime")
  @Expose
  private String statLogEndDateTime;

  @SerializedName("stat:LogStartDateTime")
  @Expose
  private String statLogStartDateTime;

  /**
   * No args constructor for use in serialization
   * 
   */
  public WsaddrStatistics() {

  }

  /**
   * 
   * @param statLogEndDateTime
   * @param statLogStartDateTime
   */
  public WsaddrStatistics(final String statLogEndDateTime,
    final String statLogStartDateTime) {

    super();
    this.statLogEndDateTime = statLogEndDateTime;
    this.statLogStartDateTime = statLogStartDateTime;
  }

  public String getStatLogEndDateTime() {

    return statLogEndDateTime;
  }

  public void setStatLogEndDateTime(final String statLogEndDateTime) {

    this.statLogEndDateTime = statLogEndDateTime;
  }

  public WsaddrStatistics
    withStatLogEndDateTime(final String statLogEndDateTime) {

    this.statLogEndDateTime = statLogEndDateTime;
    return this;
  }

  public String getStatLogStartDateTime() {

    return statLogStartDateTime;
  }

  public void setStatLogStartDateTime(final String statLogStartDateTime) {

    this.statLogStartDateTime = statLogStartDateTime;
  }

  public WsaddrStatistics
    withStatLogStartDateTime(final String statLogStartDateTime) {

    this.statLogStartDateTime = statLogStartDateTime;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(WsaddrStatistics.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("statLogEndDateTime");
    sb.append('=');
    sb.append(
      this.statLogEndDateTime == null ? "<null>" : this.statLogEndDateTime);
    sb.append(',');
    sb.append("statLogStartDateTime");
    sb.append('=');
    sb.append(this.statLogStartDateTime == null ? "<null>"
      : this.statLogStartDateTime);
    sb.append(',');
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.setCharAt(sb.length() - 1, ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

}
