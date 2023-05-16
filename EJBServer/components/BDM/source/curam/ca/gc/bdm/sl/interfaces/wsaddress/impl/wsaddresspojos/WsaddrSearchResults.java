
package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WsaddrSearchResults {

  @SerializedName("wsaddr:Information")
  @Expose
  private WsaddrInformation wsaddrInformation;

  @SerializedName("wsaddr:AddressMatches")
  @Expose
  private List<WsaddrAddressMatch> wsaddrAddressMatches = null;

  /**
   * No args constructor for use in serialization
   *
   */
  public WsaddrSearchResults() {

  }

  /**
   *
   * @param wsaddrInformation
   * @param wsaddrAddressMatches
   */
  public WsaddrSearchResults(final WsaddrInformation wsaddrInformation,
    final List<WsaddrAddressMatch> wsaddrAddressMatches) {

    super();
    this.wsaddrInformation = wsaddrInformation;
    this.wsaddrAddressMatches = wsaddrAddressMatches;
  }

  public WsaddrInformation getWsaddrInformation() {

    return wsaddrInformation;
  }

  public void
    setWsaddrInformation(final WsaddrInformation wsaddrInformation) {

    this.wsaddrInformation = wsaddrInformation;
  }

  public WsaddrSearchResults
    withWsaddrInformation(final WsaddrInformation wsaddrInformation) {

    this.wsaddrInformation = wsaddrInformation;
    return this;
  }

  public List<WsaddrAddressMatch> getWsaddrAddressMatches() {

    return wsaddrAddressMatches;
  }

  public void setWsaddrAddressMatches(
    final List<WsaddrAddressMatch> wsaddrAddressMatches) {

    this.wsaddrAddressMatches = wsaddrAddressMatches;
  }

  public WsaddrSearchResults withWsaddrAddressMatches(
    final List<WsaddrAddressMatch> wsaddrAddressMatches) {

    this.wsaddrAddressMatches = wsaddrAddressMatches;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(WsaddrSearchResults.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("wsaddrInformation");
    sb.append('=');
    sb.append(
      this.wsaddrInformation == null ? "<null>" : this.wsaddrInformation);
    sb.append(',');
    sb.append("wsaddrAddressMatches");
    sb.append('=');
    sb.append(this.wsaddrAddressMatches == null ? "<null>"
      : this.wsaddrAddressMatches);
    sb.append(',');
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.setCharAt(sb.length() - 1, ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

}
