package curam.ca.gc.bdm.sl.financial.managetaxslips.impl;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipDataKey;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipPDFData;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipDataForPrint;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipDataForPrintKey;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipPrintAttrDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.internal.resources.struct.ResourceDetails;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Blob;
import curam.workspaceservices.message.MAPPING;
import curam.workspaceservices.util.impl.ResourceStoreHelper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BDMGenerateTaxSlipPDFImpl implements
  curam.ca.gc.bdm.sl.financial.managetaxslips.intf.BDMGenerateTaxSlipPDF {

  @Inject
  private ResourceStoreHelper resourceStoreHelper;

  @Inject
  BDMManageTaxSlips bdmManageTaxSlips;

  public static final String t4aTaxSlipFileName = "t4a-21e";

  public static final String rl1TaxSlipFileName = "RL-1(2021-10)DXI";

  public static final String boxPrefix = "box_";

  public static final String boxCasePostfix = "_case";

  public static final String otherIncomeBoxRef = "box_028";

  public static final String statusIndianIncome = "144";

  public BDMGenerateTaxSlipPDFImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public BDMUATaxSlipPDFData getT4APDF(final BDMUATaxSlipDataKey key)
    throws AppException, InformationalException {

    final BDMUATaxSlipPDFData pdfForm = new BDMUATaxSlipPDFData();
    final ResourceDetails resourceDetail =
      resourceStoreHelper.getResource(t4aTaxSlipFileName);
    pdfForm.data = resourceDetail.content;
    pdfForm.contentType = pdfForm.fileName =
      resourceDetail.name + BDMConstants.kPDFFileExtensionString;
    final BDMTaxSlipDataForPrintKey pdfPrintkey =
      new BDMTaxSlipDataForPrintKey();
    pdfPrintkey.taxSlipDataID = key.taxSlipDataID;

    final BDMTaxSlipDataForPrint taxSlipPrintData =
      bdmManageTaxSlips.getTaxSlipDataForPrint(pdfPrintkey);

    pdfForm.data = getPopulatedPDFForm(pdfForm.data, taxSlipPrintData);

    return pdfForm;
  }

  @Override
  public BDMUATaxSlipPDFData getRL1PDF(final BDMUATaxSlipDataKey key)
    throws AppException, InformationalException {

    final BDMUATaxSlipPDFData pdfForm = new BDMUATaxSlipPDFData();
    final ResourceDetails resourceDetail =
      resourceStoreHelper.getResource(rl1TaxSlipFileName);
    pdfForm.data = resourceDetail.content;
    pdfForm.fileName =
      resourceDetail.name + BDMConstants.kPDFFileExtensionString;

    final BDMTaxSlipDataForPrintKey pdfPrintkey =
      new BDMTaxSlipDataForPrintKey();
    pdfPrintkey.taxSlipDataID = key.taxSlipDataID;

    final BDMTaxSlipDataForPrint taxSlipPrintData =
      bdmManageTaxSlips.getTaxSlipDataForPrint(pdfPrintkey);

    pdfForm.data = getPopulatedPDFForm(pdfForm.data, taxSlipPrintData);

    return pdfForm;
  }

  @Override
  public Blob getPopulatedPDFForm(final Blob formTemplate,
    final BDMTaxSlipDataForPrint taxSlipDataForPrint) throws AppException {

    try {
      final byte[] template = formTemplate.copyBytes();

      final PdfReader reader = new PdfReader(template);
      final ByteArrayOutputStream outputForm = new ByteArrayOutputStream();
      final PdfStamper stamper = new PdfStamper(reader, outputForm);
      final AcroFields form = stamper.getAcroFields();
      final Object[] fieldObjs = form.getFields().keySet().toArray();

      for (final BDMTaxSlipPrintAttrDetails taxSlipDataPrintAttrDetail : taxSlipDataForPrint.dtls
        .items()) {
        for (final Object field : fieldObjs) {
          final String fieldKey = (String) field;
          // taxSlipDataPrintAttrDetail.boxID;
          if (fieldKey.equalsIgnoreCase(
            taxSlipDataPrintAttrDetail.boxID.toUpperCase())) {
            final boolean result =
              form.setField(fieldKey, taxSlipDataPrintAttrDetail.attrValue);
          } else if ((boxPrefix.toUpperCase()
            + taxSlipDataPrintAttrDetail.boxID.toUpperCase())
              .equalsIgnoreCase(fieldKey.toUpperCase())) {
            if (!StringUtil
              .isNullOrEmpty(taxSlipDataPrintAttrDetail.attrValue)) {

              final boolean result =
                form.setField(fieldKey, taxSlipDataPrintAttrDetail.attrValue);
              for (final Object fieldLookup : fieldObjs) {
                final String fieldLookupKey = (String) fieldLookup;
                if ((boxPrefix.toUpperCase()
                  + taxSlipDataPrintAttrDetail.boxID.toUpperCase()
                  + boxCasePostfix.toUpperCase())
                    .equalsIgnoreCase(fieldLookupKey.toUpperCase())) {
                  final boolean result2 = form.setField(fieldLookupKey,
                    taxSlipDataPrintAttrDetail.boxID);
                }
              }

            }
          }

          if (taxSlipDataPrintAttrDetail.boxID.equals(statusIndianIncome)
            && !StringUtil
              .isNullOrEmpty(taxSlipDataPrintAttrDetail.attrValue)) {
            final boolean result = form.setField(otherIncomeBoxRef,
              taxSlipDataPrintAttrDetail.attrValue);
            final boolean result2 =
              form.setField(otherIncomeBoxRef + boxCasePostfix,
                taxSlipDataPrintAttrDetail.boxID);
          }
        }
      }

      stamper.close();
      reader.close();
      final Blob pdfFile = new Blob(outputForm.toByteArray());

      outputForm.close();
      return pdfFile;
    } catch (final DocumentException var3) {
      throw new AppException(MAPPING.ERR_XML_CREATION, var3);
    } catch (final IOException var4) {
      throw new AppException(MAPPING.ERR_XML_CREATION, var4);
    }
  }

}
