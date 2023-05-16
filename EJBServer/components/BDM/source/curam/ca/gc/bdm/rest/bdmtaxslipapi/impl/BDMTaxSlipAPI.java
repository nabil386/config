package curam.ca.gc.bdm.rest.bdmtaxslipapi.impl;

import curam.ca.gc.bdm.codetable.BDMTAXSLIPFORMTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPPROCSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPSTATUS;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataRL1Factory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataT4AFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataRL1;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataT4A;
import curam.ca.gc.bdm.entity.financial.struct.BDMConcernRoleYearStatusKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMModifyRL1TaxSlipStatusDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMModifyRL1TaxSlipStatusKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMModifyT4ATaxSlipStatusDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMModifyT4ATaxSlipStatusKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Dtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Key;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4ADtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4AKey;
import curam.ca.gc.bdm.rest.bdmtaxslipapi.struct.BDMTaxSlipUpdateProcStatus;
import curam.ca.gc.bdm.rest.bdmtaxslipapi.struct.BDMTaxSlipUpdateProcStatusList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;

/***
 * User story 6665 : Tax slips
 * API to update the processing status of Tax entities , This will be called
 * from ADF
 *
 * @author raghunath.govindaraj
 *
 */
public class BDMTaxSlipAPI
  extends curam.ca.gc.bdm.rest.bdmtaxslipapi.base.BDMTaxSlipAPI {

  public BDMTaxSlipAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void updateTaxSlipProcessingStatus(
    final BDMTaxSlipUpdateProcStatusList taxSlipUpdateList)
    throws AppException, InformationalException {

    Trace.kTopLevelLogger.info(" TAXSLIP  update called from ADF");
    Trace.kTopLevelLogger.info("Number of updateds to be processed :"
      + taxSlipUpdateList.taxSlipUpdateDetails.size());
    BDMTaxSlipDataT4AKey taxDataKeyObj = new BDMTaxSlipDataT4AKey();
    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
    final NotFoundIndicator nf = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Key taxSlipDataRL1KeyObj =
      new BDMTaxSlipDataRL1Key();

    for (final BDMTaxSlipUpdateProcStatus taxSlipDetails : taxSlipUpdateList.taxSlipUpdateDetails) {

      if (taxSlipDetails.taxSlipFormType != null
        && taxSlipDetails.taxSlipFormType
          .equalsIgnoreCase(BDMTAXSLIPFORMTYPE.T4A)) {

        taxDataKeyObj = new BDMTaxSlipDataT4AKey();
        taxDataKeyObj.taxSlipDataID = taxSlipDetails.taxSlipDataID;
        final BDMTaxSlipDataT4ADtls taxSlipDataT4ADtls =
          t4aObj.read(nf, taxDataKeyObj);

        // Search if any ISSUED records exists , and Change "taxSlipStatusCode"
        // to REPLACED
        final BDMConcernRoleYearStatusKey taxslipByStatusAndYearKey =
          new BDMConcernRoleYearStatusKey();
        taxslipByStatusAndYearKey.concernRoleID =
          taxSlipDataT4ADtls.concernRoleID;
        taxslipByStatusAndYearKey.taxYear = taxSlipDataT4ADtls.taxYear;
        taxslipByStatusAndYearKey.taxSlipStatusCode = BDMTAXSLIPSTATUS.ISSUED;
        final NotFoundIndicator readByConcernRoleYearStatusIndicator =
          new NotFoundIndicator();
        final BDMTaxSlipDataT4ADtls taxSlipDataT4ADtlsInIssuedStatus =
          t4aObj.readByConcernRoleYearStatus(
            readByConcernRoleYearStatusIndicator, taxslipByStatusAndYearKey);
        if (!readByConcernRoleYearStatusIndicator.isNotFound()) {
          final BDMModifyT4ATaxSlipStatusDtls modifyT4ATaxSlipStatusDtls =
            new BDMModifyT4ATaxSlipStatusDtls();
          modifyT4ATaxSlipStatusDtls.taxSlipStatusCode =
            BDMTAXSLIPSTATUS.REPLACED;
          modifyT4ATaxSlipStatusDtls.versionNo =
            taxSlipDataT4ADtlsInIssuedStatus.versionNo;
          final BDMModifyT4ATaxSlipStatusKey modifykey =
            new BDMModifyT4ATaxSlipStatusKey();
          modifykey.taxSlipDataID =
            taxSlipDataT4ADtlsInIssuedStatus.taxSlipDataID;
          t4aObj.modifyTaxSlipStatus(modifykey, modifyT4ATaxSlipStatusDtls);
        }

        // Update the current record's Status and ProcessingStatus
        if (taxSlipDataT4ADtls.duplicateInd) {
          taxSlipDataT4ADtls.processingStatus =
            BDMTAXSLIPPROCSTATUS.REPRINTED;
        } else {
          taxSlipDataT4ADtls.processingStatus = BDMTAXSLIPPROCSTATUS.PRINTED;
        }
        taxSlipDataT4ADtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.ISSUED;
        taxSlipDataT4ADtls.processingDateTime = DateTime.getCurrentDateTime();
        t4aObj.modify(taxDataKeyObj, taxSlipDataT4ADtls);

      } else if (taxSlipDetails.taxSlipFormType != null
        && taxSlipDetails.taxSlipFormType
          .equalsIgnoreCase(BDMTAXSLIPFORMTYPE.RL1)) {

        final BDMTaxSlipDataRL1 taxSlipDataRL1Obj =
          BDMTaxSlipDataRL1Factory.newInstance();
        taxSlipDataRL1KeyObj.taxSlipDataID = taxSlipDetails.taxSlipDataID;

        final BDMTaxSlipDataRL1Dtls taxSlipDataRL1DtlsObj =
          taxSlipDataRL1Obj.read(taxSlipDataRL1KeyObj);

        // Search if any ISSUED records exists , and Change "taxSlipStatusCode"
        // to REPLACED
        final BDMConcernRoleYearStatusKey taxslipByStatusAndYearKey =
          new BDMConcernRoleYearStatusKey();
        taxslipByStatusAndYearKey.concernRoleID =
          taxSlipDataRL1DtlsObj.concernRoleID;
        taxslipByStatusAndYearKey.taxYear = taxSlipDataRL1DtlsObj.taxYear;
        taxslipByStatusAndYearKey.taxSlipStatusCode = BDMTAXSLIPSTATUS.ISSUED;
        final NotFoundIndicator readByConcernRoleYearStatusIndicator =
          new NotFoundIndicator();

        final BDMTaxSlipDataRL1Dtls taxSlipDataRL1DtlsInIssuedStatus =
          taxSlipDataRL1Obj.readByConcernRoleYearStatus(
            readByConcernRoleYearStatusIndicator, taxslipByStatusAndYearKey);

        if (!readByConcernRoleYearStatusIndicator.isNotFound()) {
          final BDMModifyRL1TaxSlipStatusKey modifykey =
            new BDMModifyRL1TaxSlipStatusKey();
          modifykey.taxSlipDataID =
            taxSlipDataRL1DtlsInIssuedStatus.taxSlipDataID;
          final BDMModifyRL1TaxSlipStatusDtls modifyRL1TaxSlipStatusDtls =
            new BDMModifyRL1TaxSlipStatusDtls();
          modifyRL1TaxSlipStatusDtls.taxSlipStatusCode =
            BDMTAXSLIPSTATUS.REPLACED;
          modifyRL1TaxSlipStatusDtls.versionNo =
            taxSlipDataRL1DtlsInIssuedStatus.versionNo;
          taxSlipDataRL1Obj.modifyTaxSlipStatus(modifykey,
            modifyRL1TaxSlipStatusDtls);
        }

        if (taxSlipDataRL1DtlsObj.duplicateInd) {
          taxSlipDataRL1DtlsObj.processingStatus =
            BDMTAXSLIPPROCSTATUS.REPRINTED;
        } else {
          taxSlipDataRL1DtlsObj.processingStatus =
            BDMTAXSLIPPROCSTATUS.PRINTED;
        }
        taxSlipDataRL1DtlsObj.taxSlipStatusCode = BDMTAXSLIPSTATUS.ISSUED;
        taxSlipDataRL1DtlsObj.processingDateTime =
          DateTime.getCurrentDateTime();
        taxSlipDataRL1Obj.modify(taxSlipDataRL1KeyObj, taxSlipDataRL1DtlsObj);

      }

    }

  }

}
