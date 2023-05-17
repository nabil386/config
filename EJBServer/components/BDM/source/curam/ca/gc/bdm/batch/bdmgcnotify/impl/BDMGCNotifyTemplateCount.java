package curam.ca.gc.bdm.batch.bdmgcnotify.impl;

import curam.util.type.struct.Struct;

/**
 * Required to pass to Dynamic SQLs
 *
 */
public class BDMGCNotifyTemplateCount
  extends Struct<BDMGCNotifyTemplateCount> {

  private static final long serialVersionUID = 1L;

  public long count = 0L;

  public String templateID = "";

  public long getCount() {

    return this.count;
  }

  public void setCount(final long count) {

    this.count = count;
  }

  public String getTemplateID() {

    return this.templateID;
  }

  public void setTemplateID(final String templateID) {

    this.templateID = templateID;
  }
}
