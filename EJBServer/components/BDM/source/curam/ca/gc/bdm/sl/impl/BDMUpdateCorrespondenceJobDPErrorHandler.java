package curam.ca.gc.bdm.sl.impl;

import curam.util.deferredprocessing.impl.DPCallback;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMUpdateCorrespondenceJobDPErrorHandler implements DPCallback {

  /**
   * Handles DP Process error when correspondence job Id is unable to be updated
   * successfully
   *
   * @param process name
   * @param instanceDataID
   */
  @Override
  public void dpHandleError(final String processName,
    final long instanceDataID) throws AppException, InformationalException {

    // Do Nothing for now.

  }

}
