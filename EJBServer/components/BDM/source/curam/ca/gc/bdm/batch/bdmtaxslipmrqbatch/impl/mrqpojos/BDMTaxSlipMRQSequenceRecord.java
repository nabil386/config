package curam.ca.gc.bdm.batch.bdmtaxslipmrqbatch.impl.mrqpojos;

import curam.util.type.struct.Struct;

/**
 * Required to pass to Dynamic SQLs
 *
 */
public class BDMTaxSlipMRQSequenceRecord
  extends Struct<BDMTaxSlipMRQSequenceRecord> {

  private static final long serialVersionUID = 1L;

  public long count = 0L;

  public long seedID = 0L;

  public long getCount() {

    return this.count;
  }

  public void setCount(final long count) {

    this.count = count;
  }

  public long getseedID() {

    return this.seedID;
  }

  public void setseedID(final long seedID) {

    this.seedID = seedID;
  }
}
