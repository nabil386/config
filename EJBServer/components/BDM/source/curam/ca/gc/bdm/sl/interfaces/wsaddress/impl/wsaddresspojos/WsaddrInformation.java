
package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WsaddrInformation {

  @SerializedName("wsaddr:StatusCode")
  @Expose
  private String wsaddrStatusCode;

  @SerializedName("nc:MessageText")
  @Expose
  private String ncMessageText;

  /**
   * No args constructor for use in serialization
   *
   */
  public WsaddrInformation() {

  }

  /**
   *
   * @param ncMessageText
   * @param wsaddrStatusCode
   */
  public WsaddrInformation(final String wsaddrStatusCode,
    final String ncMessageText) {

    super();
    this.wsaddrStatusCode = wsaddrStatusCode;
    this.ncMessageText = ncMessageText;
  }

  public String getWsaddrStatusCode() {

    return wsaddrStatusCode;
  }

  public void setWsaddrStatusCode(final String wsaddrStatusCode) {

    this.wsaddrStatusCode = wsaddrStatusCode;
  }

  public WsaddrInformation
    withWsaddrStatusCode(final String wsaddrStatusCode) {

    this.wsaddrStatusCode = wsaddrStatusCode;
    return this;
  }

  public String getNcMessageText() {

    return ncMessageText;
  }

  public void setNcMessageText(final String ncMessageText) {

    this.ncMessageText = ncMessageText;
  }

  public WsaddrInformation withNcMessageText(final String ncMessageText) {

    this.ncMessageText = ncMessageText;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(WsaddrInformation.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("wsaddrStatusCode");
    sb.append('=');
    sb.append(
      this.wsaddrStatusCode == null ? "<null>" : this.wsaddrStatusCode);
    sb.append(',');
    sb.append("ncMessageText");
    sb.append('=');
    sb.append(this.ncMessageText == null ? "<null>" : this.ncMessageText);
    sb.append(',');
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.setCharAt(sb.length() - 1, ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

}
