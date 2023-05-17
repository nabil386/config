package curam.ca.gc.bdm.batch.bdmtaxslipmrqbatch.impl.mrqpojos;

import curam.util.type.struct.Struct;

/**
 * Required to pass to Dynamic SQLs
 *
 */
public class BDMTaxSlipMRQRecord extends Struct<BDMTaxSlipMRQRecord> {

  private static final long serialVersionUID = 1L;

  public long count = 0L;

  public long taxSlipDataID = 0L;

  public long sequenceNumber = 0L;

  public long getCount() {

    return this.count;
  }

  public void setCount(final long count) {

    this.count = count;
  }

  public long getTaxSlipDataID() {

    return this.taxSlipDataID;
  }

  public void setTaxSlipDataID(final long taxSlipDataID) {

    this.taxSlipDataID = taxSlipDataID;
  }
}
