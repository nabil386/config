package curam.ca.gc.bdmoas.sl.cra.transaction.impl;

import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRAIncomeDataFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRATransactionFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.intf.BDMOASCRAIncomeData;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataDtlsList;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataKey;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionKey;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingDtls;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingItemDtls;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASETYPECODE;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.struct.CaseHeaderDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BDMOASCRATransactionProcessor {

  public List<Long> processTransaction(final long transactionID)
    throws AppException, InformationalException {

    List<Long> evidenceIdList = new ArrayList<>();

    final boolean isIncomeDataPresent =
      updateBlockInfoForCRAIncomeData(transactionID);

    if (isIncomeDataPresent) {
      evidenceIdList = processCraIncomeData(transactionID);
    }

    return evidenceIdList;
  }

  private boolean updateBlockInfoForCRAIncomeData(final long transactionID)
    throws AppException, InformationalException {

    final BDMOASCRAIncomeDataDtlsList incomeDataDtlsList =
      BDMOASCRATransactionHelper.getIncomeDataForTransaction(transactionID);

    // if there is no income data for the transaction return false
    if (incomeDataDtlsList == null || incomeDataDtlsList.dtls.isEmpty()) {
      return false;
    }

    final String taxYear =
      BDMOASCRATransactionHelper.getTransactionTaxYear(transactionID);

    final BDMOASIncomeMappingDtls incomeMappingDtls =
      BDMOASCRATransactionHelper.getIncomeMappingForTaxYear(taxYear);

    // if there is no active income mapping for the tax year or any year before
    // tax year return false
    if (incomeMappingDtls == null) {
      return false;
    }

    final Map<String, BDMOASIncomeMappingItemDtls> blockMappedIncomeItemsMap =
      BDMOASCRATransactionHelper
        .getBlockMappedIncomeItemsMapForTaxYear(taxYear);

    final BDMOASCRAIncomeData craIncomeDataObj =
      BDMOASCRAIncomeDataFactory.newInstance();

    // loop through income data records and update the block for each
    for (final BDMOASCRAIncomeDataDtls incomeDataDtls : incomeDataDtlsList.dtls) {

      final String lineItemType = incomeDataDtls.lineItem;

      if (!StringUtil.isNullOrEmpty(lineItemType)) {

        // find the block for the line item from the blockMappedIncomeItemsMap
        final BDMOASIncomeMappingItemDtls incomeMappingItemDtls =
          blockMappedIncomeItemsMap.get(lineItemType);

        // if the line item is block mapped (found in the
        // blockMappedIncomeItemsMap)
        if (incomeMappingItemDtls != null) {
          // update the income data
          incomeDataDtls.incomeBlock = incomeMappingItemDtls.incomeBlock;

          final BDMOASCRAIncomeDataKey craIncomeDataKey =
            new BDMOASCRAIncomeDataKey();
          craIncomeDataKey.incomeDataID = incomeDataDtls.incomeDataID;

          craIncomeDataObj.modify(craIncomeDataKey, incomeDataDtls);
        }
      }
    }

    return true;
  }

  private List<Long> processCraIncomeData(final long transactionID)
    throws AppException, InformationalException {

    final List<Long> evidenceIDList = new ArrayList<>();

    final BDMOASCRATransactionKey transactionIDKey =
      new BDMOASCRATransactionKey();
    transactionIDKey.transactionID = transactionID;
    final BDMOASCRATransactionDtls transactionDtls =
      BDMOASCRATransactionFactory.newInstance().read(transactionIDKey);

    final BDMOASCRAIncomeDataDtlsList incomeDataDtlsList =
      BDMOASCRATransactionHelper.getIncomeDataForTransaction(transactionID);

    final List<CaseHeaderDtls> applicableCaseList = BDMOASCRATransactionHelper
      .getCasesForCRAIncomeEvidenceMaintenance(transactionDtls.concernRoleID);

    for (final CaseHeaderDtls caseHeader : applicableCaseList) {
      final long evidenceID =
        BDMOASCRATransactionHelper.performCraIncomeEvidenceMaintenance(
          caseHeader.caseID, transactionDtls, incomeDataDtlsList);

      if (caseHeader.caseTypeCode.equals(CASETYPECODE.INTEGRATEDCASE)) {
        // apply changes
        final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
          new RelatedIDAndEvidenceTypeKey();
        relatedIDAndTypeKey.evidenceType = CASEEVIDENCE.OAS_CRA_INCOME;
        relatedIDAndTypeKey.relatedID = evidenceID;
        final EvidenceDescriptorDtls evidenceDescriptorDtls =
          EvidenceDescriptorFactory.newInstance()
            .readByRelatedIDAndType(relatedIDAndTypeKey);
        BDMEvidenceUtil
          .applyInEditEvidenceToICDynamicEvidence(evidenceDescriptorDtls);
      }

      evidenceIDList.add(evidenceID);
    }

    return evidenceIDList;
  }
}
