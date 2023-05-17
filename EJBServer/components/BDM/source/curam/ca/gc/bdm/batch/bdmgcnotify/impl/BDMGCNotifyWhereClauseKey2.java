package curam.ca.gc.bdm.batch.bdmgcnotify.impl;

import curam.util.type.DateTime;
import curam.util.type.struct.Struct;

/**
 * Required to pass to Dynamic SQLs
 *
 */
public class BDMGCNotifyWhereClauseKey2
  extends Struct<BDMGCNotifyWhereClauseKey2> {

  private static final long serialVersionUID = 1L;

  public curam.util.type.DateTime processingDateTime;

  public DateTime getApplicationDateTime() {

    return this.processingDateTime;
  }

  public void setApplicationDateTime(final DateTime processingDateTime) {

    this.processingDateTime = processingDateTime;
  }

}
