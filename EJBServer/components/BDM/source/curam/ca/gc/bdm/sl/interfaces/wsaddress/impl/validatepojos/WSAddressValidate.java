
package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.validatepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WSAddressValidate {

  @SerializedName("@context")
  @Expose
  private Context context;

  @SerializedName("wsaddr:ValidationRequest")
  @Expose
  private WsaddrValidationRequest wsaddrValidationRequest;

  @SerializedName("wsaddr:ValidationResults")
  @Expose
  private WsaddrValidationResults wsaddrValidationResults;

  @SerializedName("wsaddr:FunctionalMessages")
  @Expose
  private List<Object> wsaddrFunctionalMessages = null;

  @SerializedName("wsaddr:Messages")
  @Expose
  private WsaddrMessages wsaddrMessages;

  @SerializedName("wsaddr:Statistics")
  @Expose
  private WsaddrStatistics wsaddrStatistics;

  /**
   * No args constructor for use in serialization
   *
   */
  public WSAddressValidate() {

  }

  /**
   *
   * @param wsaddrValidationResults
   * @param wsaddrFunctionalMessages
   * @param wsaddrStatistics
   * @param context
   * @param wsaddrMessages
   * @param wsaddrValidationRequest
   */
  public WSAddressValidate(final Context context,
    final WsaddrValidationRequest wsaddrValidationRequest,
    final WsaddrValidationResults wsaddrValidationResults,
    final List<Object> wsaddrFunctionalMessages,
    final WsaddrMessages wsaddrMessages,
    final WsaddrStatistics wsaddrStatistics) {

    super();
    this.context = context;
    this.wsaddrValidationRequest = wsaddrValidationRequest;
    this.wsaddrValidationResults = wsaddrValidationResults;
    this.wsaddrFunctionalMessages = wsaddrFunctionalMessages;
    this.wsaddrMessages = wsaddrMessages;
    this.wsaddrStatistics = wsaddrStatistics;
  }

  public Context getContext() {

    return context;
  }

  public void setContext(final Context context) {

    this.context = context;
  }

  public WSAddressValidate withContext(final Context context) {

    this.context = context;
    return this;
  }

  public WsaddrValidationRequest getWsaddrValidationRequest() {

    return wsaddrValidationRequest;
  }

  public void setWsaddrValidationRequest(
    final WsaddrValidationRequest wsaddrValidationRequest) {

    this.wsaddrValidationRequest = wsaddrValidationRequest;
  }

  public WSAddressValidate withWsaddrValidationRequest(
    final WsaddrValidationRequest wsaddrValidationRequest) {

    this.wsaddrValidationRequest = wsaddrValidationRequest;
    return this;
  }

  public WsaddrValidationResults getWsaddrValidationResults() {

    return wsaddrValidationResults;
  }

  public void setWsaddrValidationResults(
    final WsaddrValidationResults wsaddrValidationResults) {

    this.wsaddrValidationResults = wsaddrValidationResults;
  }

  public WSAddressValidate withWsaddrValidationResults(
    final WsaddrValidationResults wsaddrValidationResults) {

    this.wsaddrValidationResults = wsaddrValidationResults;
    return this;
  }

  public List<Object> getWsaddrFunctionalMessages() {

    return wsaddrFunctionalMessages;
  }

  public void
    setWsaddrFunctionalMessages(final List<Object> wsaddrFunctionalMessages) {

    this.wsaddrFunctionalMessages = wsaddrFunctionalMessages;
  }

  public WSAddressValidate withWsaddrFunctionalMessages(
    final List<Object> wsaddrFunctionalMessages) {

    this.wsaddrFunctionalMessages = wsaddrFunctionalMessages;
    return this;
  }

  public WsaddrMessages getWsaddrMessages() {

    return wsaddrMessages;
  }

  public void setWsaddrMessages(final WsaddrMessages wsaddrMessages) {

    this.wsaddrMessages = wsaddrMessages;
  }

  public WSAddressValidate
    withWsaddrMessages(final WsaddrMessages wsaddrMessages) {

    this.wsaddrMessages = wsaddrMessages;
    return this;
  }

  public WsaddrStatistics getWsaddrStatistics() {

    return wsaddrStatistics;
  }

  public void setWsaddrStatistics(final WsaddrStatistics wsaddrStatistics) {

    this.wsaddrStatistics = wsaddrStatistics;
  }

  public WSAddressValidate
    withWsaddrStatistics(final WsaddrStatistics wsaddrStatistics) {

    this.wsaddrStatistics = wsaddrStatistics;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(WSAddressValidate.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("context");
    sb.append('=');
    sb.append(this.context == null ? "<null>" : this.context);
    sb.append(',');
    sb.append("wsaddrValidationRequest");
    sb.append('=');
    sb.append(this.wsaddrValidationRequest == null ? "<null>"
      : this.wsaddrValidationRequest);
    sb.append(',');
    sb.append("wsaddrValidationResults");
    sb.append('=');
    sb.append(this.wsaddrValidationResults == null ? "<null>"
      : this.wsaddrValidationResults);
    sb.append(',');
    sb.append("wsaddrFunctionalMessages");
    sb.append('=');
    sb.append(this.wsaddrFunctionalMessages == null ? "<null>"
      : this.wsaddrFunctionalMessages);
    sb.append(',');
    sb.append("wsaddrMessages");
    sb.append('=');
    sb.append(this.wsaddrMessages == null ? "<null>" : this.wsaddrMessages);
    sb.append(',');
    sb.append("wsaddrStatistics");
    sb.append('=');
    sb.append(
      this.wsaddrStatistics == null ? "<null>" : this.wsaddrStatistics);
    sb.append(',');
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.setCharAt(sb.length() - 1, ']');
    } else {
      sb.append(']');
    }
    return sb.toString();
  }

}
