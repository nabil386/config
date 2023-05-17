
package curam.ca.gc.bdm.ws.address.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * A POJO class that contains a list of BDMWSAddress Search response.
 *
 * The return from the web service is a list of possible address matches. To
 * represent this, we collect a list of the possible address matches as
 * {@link BDMWSAddressDetail} objects.
 */
public class BDMWSAddressSearchResponse extends BDMWSAddressTypedResponse {

  private List<BDMWSAddressDetail> searchResults =
    new ArrayList<BDMWSAddressDetail>();

  // ---------------------------------------------------------

  public List<BDMWSAddressDetail> getAddressMatchResults() {

    return this.searchResults;
  }

  public void
    setAddressMatchResults(final List<BDMWSAddressDetail> searchResults) {

    this.searchResults = searchResults;
  }

}
