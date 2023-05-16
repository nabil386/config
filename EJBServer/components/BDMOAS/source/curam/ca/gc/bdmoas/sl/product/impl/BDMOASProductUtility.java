package curam.ca.gc.bdmoas.sl.product.impl;

import curam.codetable.CASECATTYPECODE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.core.fact.CaseHeaderFactory;
import curam.core.intf.CaseHeader;
import curam.core.struct.CaseHeaderByConcernRoleIDKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderDtlsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;

public class BDMOASProductUtility {

  public static CaseHeaderDtlsList listOpenOASIntegratedCasesForConcernRole(
    final long concernRoleID) throws AppException, InformationalException {

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderByConcernRoleIDKey caseHeaderKey =
      new CaseHeaderByConcernRoleIDKey();
    caseHeaderKey.concernRoleID = concernRoleID;
    caseHeaderKey.statusCode = CASESTATUS.OPEN;

    final CaseHeaderDtlsList integratedCaseHeaderDtlsList =
      caseHeaderObj.searchByConcernRoleID(caseHeaderKey);

    final CaseHeaderDtlsList caseHeaderDtlsList = new CaseHeaderDtlsList();

    for (final CaseHeaderDtls caseHeaderDtls : integratedCaseHeaderDtlsList.dtls) {

      if (isOpenOASIntegratedCase(caseHeaderDtls)) {
        caseHeaderDtlsList.dtls.addRef(caseHeaderDtls);
      }

    }

    return caseHeaderDtlsList;

  }

  public static boolean
    isOpenOASIntegratedCase(final CaseHeaderDtls caseHeaderDtls) {

    return CASESTATUS.OPEN.equals(caseHeaderDtls.statusCode)
      && CASECATTYPECODE.OAS_OLD_AGE_SECURITY
        .equals(caseHeaderDtls.integratedCaseType)
      && CASETYPECODE.INTEGRATEDCASE.equals(caseHeaderDtls.caseTypeCode);

  }

}
