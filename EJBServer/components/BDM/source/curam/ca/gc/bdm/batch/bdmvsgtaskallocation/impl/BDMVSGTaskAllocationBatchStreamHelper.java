package curam.ca.gc.bdm.batch.bdmvsgtaskallocation.impl;

import curam.core.impl.BatchStreamHelper;

public class BDMVSGTaskAllocationBatchStreamHelper extends BatchStreamHelper {

  private final BDMVSGTaskAllocationBatchStreamWrapper kStreamWrapper;

  public BDMVSGTaskAllocationBatchStreamHelper(
    final BDMVSGTaskAllocationBatchStreamWrapper streamWrapper) {

    this.kStreamWrapper = streamWrapper;
  }

}
