package curam.ca.gc.bdm.util.impl;

import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;

/**
 * Handles caching of data within a transaction using facade scope object
 *
 * @author develop.kranthi.ga
 *
 */
public class BDMCacheUtil {

  private static final String kWorkItemUpdatedKey =
    "BDMCacheUtil.WorkItemUpdated.fso";

  /**
   * Sets the facade scope object if a workitem id is updated thru API
   *
   * @param workItemID
   * @throws AppException
   * @throws InformationalException
   */
  public static void setWorkItemIDUpdatedThruAPI(final long communicationID)
    throws AppException, InformationalException {

    TransactionInfo.setFacadeScopeObject(
      kWorkItemUpdatedKey + communicationID, communicationID);
  }

  /**
   * returns true if API called the update method.
   *
   * @param workItemID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static boolean isWorkItemIDUpdatedThruAPI(final long communicationID)
    throws AppException, InformationalException {

    return null != TransactionInfo
      .getFacadeScopeObject(kWorkItemUpdatedKey + communicationID);
  }

}
