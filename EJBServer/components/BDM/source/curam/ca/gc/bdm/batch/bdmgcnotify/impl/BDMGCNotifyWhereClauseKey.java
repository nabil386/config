package curam.ca.gc.bdm.batch.bdmgcnotify.impl;

import curam.util.type.struct.Struct;

/**
 * Required to pass to Dynamic SQLs
 *
 */
public class BDMGCNotifyWhereClauseKey
  extends Struct<BDMGCNotifyWhereClauseKey> {

  private static final long serialVersionUID = 1L;

  public curam.util.type.Date processingDate;

  public curam.util.type.Date getApplicationDate() {

    return this.processingDate;
  }

  public void setApplicationDate(final curam.util.type.Date processingDate) {

    this.processingDate = processingDate;
  }

}
