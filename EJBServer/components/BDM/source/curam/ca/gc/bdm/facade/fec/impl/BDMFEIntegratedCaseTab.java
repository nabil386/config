package curam.ca.gc.bdm.facade.fec.impl;

import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.facade.integratedcase.fact.BDMIntegratedCaseTabFactory;
import curam.ca.gc.bdm.facade.integratedcase.intf.BDMIntegratedCaseTab;
import curam.core.impl.CuramConst;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.IntegratedCaseTabDetail;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.wizard.util.impl.CodetableUtil;

// Task 65861: DEV: Implement View FEC case

public class BDMFEIntegratedCaseTab
  extends curam.ca.gc.bdm.facade.fec.base.BDMFEIntegratedCaseTab {

  @Override
  public IntegratedCaseTabDetail readFEIntegratedCaseTabDetail(
    final CaseIDKey key) throws AppException, InformationalException {

    IntegratedCaseTabDetail integratedCaseTabDetail =
      new IntegratedCaseTabDetail();

    final BDMIntegratedCaseTab bdmIntegratedCaseTab =
      BDMIntegratedCaseTabFactory.newInstance();

    integratedCaseTabDetail =
      bdmIntegratedCaseTab.readIntegratedCaseTabDetail(key);

    // BEGIN: Task 65861: DEV: Implement View FEC case
    if (key.caseID != CuramConst.gkZero) {

      final BDMFECase bdmFECase = BDMFECaseFactory.newInstance();

      BDMFECaseDtls bdmFECaseDtls = new BDMFECaseDtls();

      final BDMFECaseKey bdmFECaseKey = new BDMFECaseKey();
      bdmFECaseKey.caseID = key.caseID;

      try {

        bdmFECaseDtls = bdmFECase.read(bdmFECaseKey);

          final String conditionalFormedPersonName =
          CuramConst.kSeparator + CodetableUtil.getCodetableDescription(
            BDMSOURCECOUNTRY.TABLENAME, bdmFECaseDtls.countryCode);

        integratedCaseTabDetail.conditionalFormedPersonNameOpt =
          conditionalFormedPersonName;

      } catch (final RecordNotFoundException rnfe) {

        integratedCaseTabDetail.conditionalFormedPersonNameOpt =
          CuramConst.gkEmpty;

      }

    }
    // END: Task 65861: DEV: Implement View FEC case

    return integratedCaseTabDetail;
  }

}
