
package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WsaddrMessages {

  @SerializedName("wsaddr:MessageErrors")
  @Expose
  private List<Object> wsaddrMessageErrors = null;

  @SerializedName("wsaddr:MessageWarnings")
  @Expose
  private List<Object> wsaddrMessageWarnings = null;

  @SerializedName("wsaddr:MessageInformation")
  @Expose
  private List<Object> wsaddrMessageInformation = null;

  /**
   * No args constructor for use in serialization
   *
   */
  public WsaddrMessages() {

  }

  /**
   *
   * @param wsaddrMessageErrors
   * @param wsaddrMessageInformation
   * @param wsaddrMessageWarnings
   */
  public WsaddrMessages(final List<Object> wsaddrMessageErrors,
    final List<Object> wsaddrMessageWarnings,
    final List<Object> wsaddrMessageInformation) {

    super();
    this.wsaddrMessageErrors = wsaddrMessageErrors;
    this.wsaddrMessageWarnings = wsaddrMessageWarnings;
    this.wsaddrMessageInformation = wsaddrMessageInformation;
  }

  public List<Object> getWsaddrMessageErrors() {

    return wsaddrMessageErrors;
  }

  public void setWsaddrMessageErrors(final List<Object> wsaddrMessageErrors) {

    this.wsaddrMessageErrors = wsaddrMessageErrors;
  }

  public WsaddrMessages
    withWsaddrMessageErrors(final List<Object> wsaddrMessageErrors) {

    this.wsaddrMessageErrors = wsaddrMessageErrors;
    return this;
  }

  public List<Object> getWsaddrMessageWarnings() {

    return wsaddrMessageWarnings;
  }

  public void
    setWsaddrMessageWarnings(final List<Object> wsaddrMessageWarnings) {

    this.wsaddrMessageWarnings = wsaddrMessageWarnings;
  }

  public WsaddrMessages
    withWsaddrMessageWarnings(final List<Object> wsaddrMessageWarnings) {

    this.wsaddrMessageWarnings = wsaddrMessageWarnings;
    return this;
  }

  public List<Object> getWsaddrMessageInformation() {

    return wsaddrMessageInformation;
  }

  public void
    setWsaddrMessageInformation(final List<Object> wsaddrMessageInformation) {

    this.wsaddrMessageInformation = wsaddrMessageInformation;
  }

  public WsaddrMessages withWsaddrMessageInformation(
    final List<Object> wsaddrMessageInformation) {

    this.wsaddrMessageInformation = wsaddrMessageInformation;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(WsaddrMessages.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("wsaddrMessageErrors");
    sb.append('=');
    sb.append(
      this.wsaddrMessageErrors == null ? "<null>" : this.wsaddrMessageErrors);
    sb.append(',');
    sb.append("wsaddrMessageWarnings");
    sb.append('=');
    sb.append(this.wsaddrMessageWarnings == null ? "<null>"
      : this.wsaddrMessageWarnings);
    sb.append(',');
    sb.append("wsaddrMessageInformation");
    sb.append('=');
    sb.append(this.wsaddrMessageInformation == null ? "<null>"
      : this.wsaddrMessageInformation);
    sb.append(',');
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.setCharAt(sb.length() - 1, ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

}
