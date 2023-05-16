
package curam.ca.gc.bdm.sl.interfaces.wsaddress.impl.wsaddresspojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class WSAddress {

  @SerializedName("@context")
  @Expose
  private Context context;

  @SerializedName("wsaddr:SearchRequest")
  @Expose
  private WsaddrSearchRequest wsaddrSearchRequest;

  @SerializedName("wsaddr:SearchResults")
  @Expose
  private WsaddrSearchResults wsaddrSearchResults;

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
  public WSAddress() {

  }

  /**
   *
   * @param wsaddrStatistics
   * @param context
   * @param wsaddrSearchRequest
   * @param wsaddrMessages
   * @param wsaddrSearchResults
   */
  public WSAddress(final Context context,
    final WsaddrSearchRequest wsaddrSearchRequest,
    final WsaddrSearchResults wsaddrSearchResults,
    final WsaddrMessages wsaddrMessages,
    final WsaddrStatistics wsaddrStatistics) {

    super();
    this.context = context;
    this.wsaddrSearchRequest = wsaddrSearchRequest;
    this.wsaddrSearchResults = wsaddrSearchResults;
    this.wsaddrMessages = wsaddrMessages;
    this.wsaddrStatistics = wsaddrStatistics;
  }

  public Context getContext() {

    return context;
  }

  public void setContext(final Context context) {

    this.context = context;
  }

  public WSAddress withContext(final Context context) {

    this.context = context;
    return this;
  }

  public WsaddrSearchRequest getWsaddrSearchRequest() {

    return wsaddrSearchRequest;
  }

  public void
    setWsaddrSearchRequest(final WsaddrSearchRequest wsaddrSearchRequest) {

    this.wsaddrSearchRequest = wsaddrSearchRequest;
  }

  public WSAddress
    withWsaddrSearchRequest(final WsaddrSearchRequest wsaddrSearchRequest) {

    this.wsaddrSearchRequest = wsaddrSearchRequest;
    return this;
  }

  public WsaddrSearchResults getWsaddrSearchResults() {

    return wsaddrSearchResults;
  }

  public void
    setWsaddrSearchResults(final WsaddrSearchResults wsaddrSearchResults) {

    this.wsaddrSearchResults = wsaddrSearchResults;
  }

  public WSAddress
    withWsaddrSearchResults(final WsaddrSearchResults wsaddrSearchResults) {

    this.wsaddrSearchResults = wsaddrSearchResults;
    return this;
  }

  public WsaddrMessages getWsaddrMessages() {

    return wsaddrMessages;
  }

  public void setWsaddrMessages(final WsaddrMessages wsaddrMessages) {

    this.wsaddrMessages = wsaddrMessages;
  }

  public WSAddress withWsaddrMessages(final WsaddrMessages wsaddrMessages) {

    this.wsaddrMessages = wsaddrMessages;
    return this;
  }

  public WsaddrStatistics getWsaddrStatistics() {

    return wsaddrStatistics;
  }

  public void setWsaddrStatistics(final WsaddrStatistics wsaddrStatistics) {

    this.wsaddrStatistics = wsaddrStatistics;
  }

  public WSAddress
    withWsaddrStatistics(final WsaddrStatistics wsaddrStatistics) {

    this.wsaddrStatistics = wsaddrStatistics;
    return this;
  }

  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append(WSAddress.class.getName()).append('@')
      .append(Integer.toHexString(System.identityHashCode(this))).append('[');
    sb.append("context");
    sb.append('=');
    sb.append(this.context == null ? "<null>" : this.context);
    sb.append(',');
    sb.append("wsaddrSearchRequest");
    sb.append('=');
    sb.append(
      this.wsaddrSearchRequest == null ? "<null>" : this.wsaddrSearchRequest);
    sb.append(',');
    sb.append("wsaddrSearchResults");
    sb.append('=');
    sb.append(
      this.wsaddrSearchResults == null ? "<null>" : this.wsaddrSearchResults);
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
