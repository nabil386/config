package curam.ca.gc.bdm.notification.impl;

import curam.util.deferredprocessing.impl.DPCallback;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMCreateNotificationDPErrorHandler implements DPCallback {

  /**
   * Create Notification Alert error handler
   */
  @Override
  public void dpHandleError(final String processName,
    final long instanceDataID) throws AppException, InformationalException {

    // TODO TBD- Need to create task to notify agent
  }

}
