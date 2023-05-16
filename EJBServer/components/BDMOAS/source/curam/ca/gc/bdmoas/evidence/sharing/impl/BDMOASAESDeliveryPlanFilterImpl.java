package curam.ca.gc.bdmoas.evidence.sharing.impl;

import curam.aes.sl.deliveryplan.impl.AESDeliveryPlanFilter;
import curam.aes.sl.entity.struct.AESDeliveryPlanDtls;
import curam.aes.sl.entity.struct.AESShareItemDtls;
import curam.ca.gc.bdmoas.impl.BDMOASConstants;
import curam.codetable.CASETYPECODE;
import curam.commonintake.entity.fact.ApplicationCaseFactory;
import curam.commonintake.entity.intf.ApplicationCase;
import curam.commonintake.entity.struct.ApplicationCaseDtls;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.fact.CaseHeaderFactory;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class BDMOASAESDeliveryPlanFilterImpl.
 * Custom filter implementation for evidence sharing.
 *
 * 104240: DEV: Implement evidence brokering
 */
public class BDMOASAESDeliveryPlanFilterImpl
  implements AESDeliveryPlanFilter {

  /**
   * Custom filter implementation for evidence sharing.
   *
   * @param items the items
   * @return the list
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public final List<AESDeliveryPlanDtls>
    filter(final List<AESDeliveryPlanDtls> items)
      throws AppException, InformationalException {

    final List<AESDeliveryPlanDtls> deliveryPlansToIgnore =
      new ArrayList<AESDeliveryPlanDtls>();

    final BDMOASAESDeliveryPlanFilterHelper helperObj =
      new BDMOASAESDeliveryPlanFilterHelper();

    for (final AESDeliveryPlanDtls aesDeliveryPlanDtls : items) {

      final AESShareItemDtls aesShareItemDtls = helperObj
        .getAESShareItemDtlsByShareItemID(aesDeliveryPlanDtls.shareItemID);

      // get the source case header details
      final CaseHeaderDtls sourceCaseHeaderDtls =
        getCaseHeaderDtls(aesShareItemDtls.sourceCaseID);

      // get the target case header details
      final CaseHeaderDtls targetCaseHeaderDtls =
        getCaseHeaderDtls(aesDeliveryPlanDtls.targetCaseID);

      if (sourceCaseHeaderDtls.caseTypeCode
        .equals(CASETYPECODE.APPLICATION_CASE)) {
        // get the application case type
        final ApplicationCase appCaseObj =
          ApplicationCaseFactory.newInstance();
        final ApplicationCaseKey appCaseKey = new ApplicationCaseKey();
        appCaseKey.applicationCaseID = sourceCaseHeaderDtls.caseID;
        final ApplicationCaseDtls appCaseDtls = appCaseObj.read(appCaseKey);

        if (appCaseDtls.applicationCaseAdminID == BDMOASConstants.OAS_GIS_APP_ADMIN_ID
          && targetCaseHeaderDtls.caseTypeCode
            .equals(CASETYPECODE.INTEGRATEDCASE)) {

          if (helperObj
            .shouldIgnoreSharingBtwnSourceOasGisAppCaseTargetOasIntCase(
              aesShareItemDtls.concernRoleID, sourceCaseHeaderDtls.caseID,
              targetCaseHeaderDtls.caseID, aesShareItemDtls.evidenceID,
              aesShareItemDtls.evidenceType)) {

            deliveryPlansToIgnore.add(aesDeliveryPlanDtls);
          }
        }
      }
    }

    return deliveryPlansToIgnore;

  }

  /**
   * Gets the case header dtls.
   *
   * @param caseID the case ID
   * @return the case header dtls
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  private CaseHeaderDtls getCaseHeaderDtls(final long caseID)
    throws AppException, InformationalException {

    final CaseHeaderKey headerKey = new CaseHeaderKey();
    headerKey.caseID = caseID;
    return CaseHeaderFactory.newInstance().read(headerKey);
  }
}
