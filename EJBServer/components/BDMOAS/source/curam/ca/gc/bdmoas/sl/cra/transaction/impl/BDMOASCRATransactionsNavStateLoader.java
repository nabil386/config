package curam.ca.gc.bdmoas.sl.cra.transaction.impl;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.tab.impl.DynamicNavStateLoader;
import curam.util.tab.impl.NavigationState;
import java.util.Map;

public class BDMOASCRATransactionsNavStateLoader
  implements DynamicNavStateLoader {

  @Override
  public NavigationState loadNavState(final NavigationState navState,
    final Map<String, String> pageParameters, final String[] idsToUpdate)
    throws AppException, InformationalException {

    final long transactionID =
      Long.valueOf(pageParameters.get("transactionID"));
    final long reassessmentID =
      Long.valueOf(pageParameters.get("reassessment"));
    /*
     * final long reassementID =
     * Long.valueOf(pageParameters.get("reassementID"));
     */

    if (reassessmentID > 0) {
      navState.setVisible(false, "Current");
      navState.setVisible(true, "History");
    } else {
      navState.setVisible(true, "History");
      navState.setVisible(true, "Current");
    }

    return navState;

  }

}
