package curam.ca.gc.bdm.test.util.financial.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPPROCSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPTYPE;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataRL1Factory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataT4AFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Dtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4ADtls;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipDataKey;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipPDFData;
import curam.ca.gc.bdm.sl.financial.managetaxslips.intf.BDMGenerateTaxSlipPDF;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.DateTime;
import curam.util.type.Money;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

public class BDMGenerateTaxSlipPDFTest extends CuramServerTestJUnit4 {

  @Inject
  private BDMGenerateTaxSlipPDF bdmGenerateTaxSlipPDF;

  public BDMGenerateTaxSlipPDFTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Test
  public void testGetT4APDF() throws Exception {

    final BDMTaxSlipDataT4ADtls taxSlipDataT4ADtls = createTaxSlipDataT4A();

    final BDMUATaxSlipDataKey taxSlipDataKey = new BDMUATaxSlipDataKey();
    taxSlipDataKey.taxSlipDataID = taxSlipDataT4ADtls.taxSlipDataID;
    final BDMUATaxSlipPDFData pdfData =
      bdmGenerateTaxSlipPDF.getT4APDF(taxSlipDataKey);

    assertNotEquals(pdfData.data.copyBytes().length, 0);
  }

  @Test
  public void testGetRL1PDF() throws Exception {

    final BDMTaxSlipDataRL1Dtls taxSlipDataRL1Dtls = createTaxSlipDataRL1();

    final BDMUATaxSlipDataKey taxSlipDataKey = new BDMUATaxSlipDataKey();
    taxSlipDataKey.taxSlipDataID = taxSlipDataRL1Dtls.taxSlipDataID;
    final BDMUATaxSlipPDFData pdfData =
      bdmGenerateTaxSlipPDF.getRL1PDF(taxSlipDataKey);
    assertNotEquals(pdfData.data.copyBytes().length, 0);
  }

  public BDMTaxSlipDataRL1Dtls createTaxSlipDataRL1()
    throws AppException, InformationalException {

    final BDMTaxSlipDataRL1Dtls taxSlipDataRL1Dtls =
      new BDMTaxSlipDataRL1Dtls();
    taxSlipDataRL1Dtls.taxSlipDataID = 2l;
    taxSlipDataRL1Dtls.concernRoleID = 101l;
    taxSlipDataRL1Dtls.taxSlipConfigID = 91001l;
    taxSlipDataRL1Dtls.taxSlipProviderConfigID = 91001l;
    taxSlipDataRL1Dtls.versionNo = 1;
    taxSlipDataRL1Dtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.REQUESTED;
    taxSlipDataRL1Dtls.reportTypeCode = "R";
    taxSlipDataRL1Dtls.otherIncomeSource = "RS";
    taxSlipDataRL1Dtls.otherIncomeAmount = new Money(171.4);
    taxSlipDataRL1Dtls.incomeTaxDeducted = new Money(0);
    taxSlipDataRL1Dtls.indianIncomeOnReserve = new Money(0);
    taxSlipDataRL1Dtls.taxYear = 2022;
    taxSlipDataRL1Dtls.slipTypeCode = BDMTAXSLIPTYPE.ORIGINAL;
    taxSlipDataRL1Dtls.processingStatus =
      BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
    taxSlipDataRL1Dtls.sequenceNumber = 200000006;
    taxSlipDataRL1Dtls.processingDateTime = DateTime.getCurrentDateTime();
    taxSlipDataRL1Dtls.recipientPostalCode = "G1R3Z2";
    taxSlipDataRL1Dtls.recipientFirstName = "testone";
    taxSlipDataRL1Dtls.recipientLastName = "onelast";
    taxSlipDataRL1Dtls.recipientSIN = "440722148";
    taxSlipDataRL1Dtls.recipientProvince = "QC";
    taxSlipDataRL1Dtls.recipientAddressLine1 = "35";
    // taxSlipDataRL1Dtls.recipientAddressLine2 = "SAINT-LOUIS RUE";
    taxSlipDataRL1Dtls.recipientCity = "Quebec";
    taxSlipDataRL1Dtls.reportTypeCode = "R";

    BDMTaxSlipDataRL1Factory.newInstance().insert(taxSlipDataRL1Dtls);
    return taxSlipDataRL1Dtls;
  }

  public BDMTaxSlipDataT4ADtls createTaxSlipDataT4A()
    throws AppException, InformationalException {

    final BDMTaxSlipDataT4ADtls taxSlipDataT4ADtls =
      new BDMTaxSlipDataT4ADtls();
    taxSlipDataT4ADtls.taxSlipDataID = 1l;
    taxSlipDataT4ADtls.concernRoleID = 101l;
    taxSlipDataT4ADtls.taxSlipConfigID = 90001l;
    taxSlipDataT4ADtls.taxSlipProviderConfigID = 90001l;
    taxSlipDataT4ADtls.versionNo = 1;
    taxSlipDataT4ADtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.REQUESTED;
    taxSlipDataT4ADtls.indianOtherIncome = new Money(0);
    taxSlipDataT4ADtls.otherIncome = new Money(171.4);
    taxSlipDataT4ADtls.taxYear = 2022;
    taxSlipDataT4ADtls.slipTypeCode = BDMTAXSLIPTYPE.ORIGINAL;
    taxSlipDataT4ADtls.processingStatus =
      BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
    taxSlipDataT4ADtls.sequenceNumber = 1;
    taxSlipDataT4ADtls.processingDateTime = DateTime.getCurrentDateTime();
    taxSlipDataT4ADtls.recipientSIN = "440722148";
    taxSlipDataT4ADtls.recipientPostalCode = "G1R 3Z2";
    taxSlipDataT4ADtls.recipientFirstName = "testone";
    taxSlipDataT4ADtls.recipientSurName = "onelast";
    taxSlipDataT4ADtls.recipientProvince = "QC";
    taxSlipDataT4ADtls.recipientAddressLine1 = "35";
    taxSlipDataT4ADtls.recipientAddressLine2 = "SAINT-LOUIS RUE";
    taxSlipDataT4ADtls.recipientCity = "Quebec";
    taxSlipDataT4ADtls.reportTypeCode = "O";

    BDMTaxSlipDataT4AFactory.newInstance().insert(taxSlipDataT4ADtls);
    return taxSlipDataT4ADtls;
  }
}
