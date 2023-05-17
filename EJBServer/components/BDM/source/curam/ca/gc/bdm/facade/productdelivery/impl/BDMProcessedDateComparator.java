package curam.ca.gc.bdm.facade.productdelivery.impl;

import curam.ca.gc.bdm.facade.productdelivery.struct.BDMViewCaseInstructionDetails;
import java.util.Comparator;

public class BDMProcessedDateComparator
  implements Comparator<BDMViewCaseInstructionDetails> {

  @Override
  public int compare(final BDMViewCaseInstructionDetails object1,
    final BDMViewCaseInstructionDetails object2) {

    // compares the items based off their effective date and multiplied by
    // negative 1 to enforce sorting in descending order
    return -1
      * object1.viewCaseInstructionDetails.finInstructDtls.effectiveDate
        .compareTo(
          object2.viewCaseInstructionDetails.finInstructDtls.effectiveDate);

  }
}
