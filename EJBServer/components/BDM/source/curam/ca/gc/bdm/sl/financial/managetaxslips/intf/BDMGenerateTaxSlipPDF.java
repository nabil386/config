package curam.ca.gc.bdm.sl.financial.managetaxslips.intf;

import com.google.inject.ImplementedBy;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipDataKey;
import curam.ca.gc.bdm.rest.bdmpaymentapi.struct.BDMUATaxSlipPDFData;
import curam.ca.gc.bdm.sl.financial.managetaxslips.impl.BDMGenerateTaxSlipPDFImpl;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipDataForPrint;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

@ImplementedBy(BDMGenerateTaxSlipPDFImpl.class)
public interface BDMGenerateTaxSlipPDF {

  public BDMUATaxSlipPDFData getRL1PDF(BDMUATaxSlipDataKey key)
    throws AppException, InformationalException;

  public BDMUATaxSlipPDFData getT4APDF(BDMUATaxSlipDataKey key)
    throws AppException, InformationalException;

  Blob getPopulatedPDFForm(Blob formTemplate,
    BDMTaxSlipDataForPrint taxSlipDataForPrint) throws AppException;
}
