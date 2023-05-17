package curam.ca.gc.bdm.test.sl.interfaces.bdmtaxslipcra.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import org.junit.Test;

public class BDMTaxSlipCRASequenceTest extends CuramServerTestJUnit4 {

  @Test
  public void getSequenceTest() throws AppException, InformationalException {

    final BDMPaymentUtil seq = new BDMPaymentUtil();

    final int seq1 = seq.getSeqNumber(BDMConstants.kCRASeqType);
    final int seq2 = seq.getSeqNumber(BDMConstants.kCRASeqType);
    int seq3 = 0;
    for (int i = 0; i < 100; i++) {
      seq3 = seq.getSeqNumber(BDMConstants.kCRASeqType);
    }
    System.out.println(seq1);
    System.out.println(seq2);
    System.out.println(seq3);
  }

}
