
package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WsaddrValidationResults {

  @SerializedName("wsaddr:Information")
  @Expose
  private WsaddrInformation wsaddrInformation;

  /**
   * No args constructor for use in serialization
   *
   */
  public WsaddrValidationResults() {

  }

  /**
   *
   * @param wsaddrInformation
   */
  public WsaddrValidationResults(final WsaddrInformation wsaddrInformation) {

    super();
    this.wsaddrInformation = wsaddrInformation;
  }

  public WsaddrInformation getWsaddrInformation() {

    return wsaddrInformation;
  }

  public void
    setWsaddrInformation(final WsaddrInformation wsaddrInformation) {

    this.wsaddrInformation = wsaddrInformation;
  }

  public WsaddrValidationResults
    withWsaddrInformation(final WsaddrInformation wsaddrInformation) {

    this.wsaddrInformation = wsaddrInformation;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(WsaddrValidationResults.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("wsaddrInformation");
    sb.append('=');
    sb.append(
      this.wsaddrInformation == null ? "<null>" : this.wsaddrInformation);
    sb.append(',');
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.setCharAt(sb.length() - 1, ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

}
