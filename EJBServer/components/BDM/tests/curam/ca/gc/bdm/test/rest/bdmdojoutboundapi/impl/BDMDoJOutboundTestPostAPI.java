package curam.ca.gc.bdm.test.rest.bdmdojoutboundapi.impl;

import curam.ca.gc.bdm.entity.struct.BDMDoJOutboundStageDtls;
import curam.ca.gc.bdm.sl.interfaces.bdmdojoutbound.impl.BDMDoJOutboundInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos.BDMWSDoJOutbound;
import curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos.Metadata;
import curam.ca.gc.bdm.sl.interfaces.dojoutbound.impl.bdmdojoutboundpojos.Obligation;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.util.type.Date;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class BDMDoJOutboundTestPostAPI extends CuramServerTestJUnit4 {

  @Test
  public void testListSubmittedApplicationsNotEligible() throws Exception {

    final BDMDoJOutboundInterfaceImpl dojImpl =
      new BDMDoJOutboundInterfaceImpl();
    final BDMDoJOutboundStageDtls bdmDoJDtls = new BDMDoJOutboundStageDtls();
    bdmDoJDtls.documentFileControlID = 1;
    bdmDoJDtls.documentFileDateTime = Date.getCurrentDate().getDateTime();
    bdmDoJDtls.documentFileDayCode = 1;
    bdmDoJDtls.documentFileEnvironmentType = "T";
    bdmDoJDtls.documentFileName = "FOE.DEBTORS.D20211201.T1550";
    bdmDoJDtls.documentFileWeekCode = "2";
    bdmDoJDtls.documentSource = "Curam";

    bdmDoJDtls.metadataIdentificationID = "MD1";

    final int amt = 100;

    final Byte byteValue = new Byte((byte) 5);
    bdmDoJDtls.obligationDebtPercentage = byteValue;
    bdmDoJDtls.obligationDebtSeqNumber = 1;

    bdmDoJDtls.obligationExceptionCode = "00";

    bdmDoJDtls.obligationFixedAmountIND = Boolean.TRUE;

    bdmDoJDtls.obligationITCCode = "0";
    bdmDoJDtls.obligationOCONRegionalCode = "0110";

    bdmDoJDtls.obligationPaymentDate = Date.getCurrentDate();
    bdmDoJDtls.obligationVendorCode = "00";
    bdmDoJDtls.personObligationID = "100";
    bdmDoJDtls.personObligationIDSuffix = "SUFF";
    bdmDoJDtls.personSINIdentification = 883638382;
    bdmDoJDtls.transactionControlIdentificationID = "T001";
    bdmDoJDtls.recordID = 100L;
    bdmDoJDtls.obligationCanadianProvinceCode = "AB";
    bdmDoJDtls.processingDateTime = Date.getCurrentDate().getDateTime();

    dojImpl.createObligationRequest(bdmDoJDtls, 1);

  }

  public BDMWSDoJOutbound mypojo() {

    final BDMWSDoJOutbound wObj = new BDMWSDoJOutbound();

    final Metadata metadata = new Metadata();
    metadata.setDocumentFileControlID(123);
    metadata.setDocumentFileDate(Date.getCurrentDate().toString());
    metadata.setDocumentFileEnvironmentType("P");
    metadata.setDocumentRecipient("test");
    wObj.setMetadata(metadata);

    final Obligation obligation = new Obligation();

    obligation.setMetadataIdentificationID("Testdata");

    return wObj;

  }

}
