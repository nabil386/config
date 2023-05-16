package curam.ca.gc.bdmoas.sl.cra.transaction.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdmoas.codetable.BDMOASCRAINCOMESTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASCRALINEITEMCATEGORY;
import curam.ca.gc.bdmoas.codetable.BDMOASCRALINEITEMTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASINCOMEBLOCK;
import curam.ca.gc.bdmoas.codetable.BDMOASINCOMEMAPPINGSTATUS;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRADataFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRAIncomeDataFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRATransactionFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.intf.BDMOASCRAData;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRADataDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataCRADataKey;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataDtlsList;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionKey;
import curam.ca.gc.bdmoas.entity.incomemapping.fact.BDMOASIncomeMappingFactory;
import curam.ca.gc.bdmoas.entity.incomemapping.fact.BDMOASIncomeMappingItemFactory;
import curam.ca.gc.bdmoas.entity.incomemapping.intf.BDMOASIncomeMapping;
import curam.ca.gc.bdmoas.entity.incomemapping.intf.BDMOASIncomeMappingItem;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingDtls;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingDtlsList;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingItemDtls;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingItemDtlsList;
import curam.ca.gc.bdmoas.entity.incomemapping.struct.BDMOASIncomeMappingKey;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASCraIncomeConstants;
import curam.ca.gc.bdmoas.util.impl.BDMOASEvidenceUtil;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.struct.CaseHeaderDtls;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.DatabaseException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BDMOASCRATransactionHelper {

  public static String getTransactionTaxYear(final long transactionID)
    throws AppException, InformationalException {

    final BDMOASCRATransactionKey key = new BDMOASCRATransactionKey();
    key.transactionID = transactionID;
    return BDMOASCRATransactionFactory.newInstance().read(key).taxYear;
  }

  public static BDMOASCRAIncomeDataDtlsList getIncomeDataForTransaction(
    final long transactionID) throws AppException, InformationalException {

    final BDMOASCRATransactionKey craTransactionKey =
      new BDMOASCRATransactionKey();
    craTransactionKey.transactionID = transactionID;

    final BDMOASCRAData craDataObj = BDMOASCRADataFactory.newInstance();
    final NotFoundIndicator craDataNfIndicator = new NotFoundIndicator();

    final BDMOASCRADataDtls craDataDtls =
      craDataObj.readByTransaction(craDataNfIndicator, craTransactionKey);

    if (craDataNfIndicator.isNotFound()
      || !BDMOASCRAINCOMESTATUS.FOUND.equals(craDataDtls.incomeStatus)) {

      return null;
    } else {

      final BDMOASCRAIncomeDataCRADataKey craDataKey =
        new BDMOASCRAIncomeDataCRADataKey();
      craDataKey.dataID = craDataDtls.dataID;
      return BDMOASCRAIncomeDataFactory.newInstance()
        .searchByCRAData(craDataKey);
    }
  }

  public static BDMOASIncomeMappingDtls getIncomeMappingForTaxYear(
    final String taxYear) throws AppException, InformationalException {

    final BDMOASIncomeMapping incomeMappingObj =
      BDMOASIncomeMappingFactory.newInstance();

    final BDMOASIncomeMappingDtlsList incomeMappingDtlsList =
      incomeMappingObj.readAll();

    // sort the income mapping list in the descending order of the year
    final List<BDMOASIncomeMappingDtls> incomeMappingList =
      incomeMappingDtlsList.dtls.stream()
        .filter(a -> BDMOASINCOMEMAPPINGSTATUS.ACTIVE.equals(a.status))
        .sorted(
          (b, c) -> Integer.parseInt(c.taxYear) - Integer.parseInt(b.taxYear))
        .collect(Collectors.toList());

    for (final BDMOASIncomeMappingDtls incomeMappingDtls : incomeMappingList) {
      if (Integer.parseInt(incomeMappingDtls.taxYear)
        - Integer.parseInt(taxYear) > 0)
        continue;

      return incomeMappingDtls;
    }

    return null;
  }

  public static Map<String, BDMOASIncomeMappingItemDtls>
    getBlockMappedIncomeItemsMap(final long mappingID)
      throws AppException, InformationalException {

    final Map<String, BDMOASIncomeMappingItemDtls> blockMappedIncomeItemsMap =
      new HashMap<>();

    final BDMOASIncomeMappingItem incomeMappingObj =
      BDMOASIncomeMappingItemFactory.newInstance();

    final BDMOASIncomeMappingKey incomeMappingKey =
      new BDMOASIncomeMappingKey();
    incomeMappingKey.mappingID = mappingID;
    final BDMOASIncomeMappingItemDtlsList incomeMappingItemDtlsList =
      incomeMappingObj.searchByMapping(incomeMappingKey);

    for (final BDMOASIncomeMappingItemDtls incomeMappingItemDtls : incomeMappingItemDtlsList.dtls) {
      if (!StringUtil.isNullOrEmpty(incomeMappingItemDtls.incomeBlock)) {
        blockMappedIncomeItemsMap.put(incomeMappingItemDtls.lineItem,
          incomeMappingItemDtls);
      }
    }

    return blockMappedIncomeItemsMap;
  }

  public static Map<String, BDMOASIncomeMappingItemDtls>
    getBlockMappedIncomeItemsMapForTaxYear(final String taxYear)
      throws AppException, InformationalException {

    Map<String, BDMOASIncomeMappingItemDtls> blockMappedIncomeItemsMap = null;

    final BDMOASIncomeMappingDtls incomeMappingDtls =
      getIncomeMappingForTaxYear(taxYear);

    if (incomeMappingDtls != null) {
      blockMappedIncomeItemsMap =
        getBlockMappedIncomeItemsMap(incomeMappingDtls.mappingID);
    }

    return blockMappedIncomeItemsMap;
  }

  public static Map<String, Money> calculateBlockTotalsForIncomeData(
    final BDMOASCRAIncomeDataDtlsList incomeDataDtlsList)
    throws DatabaseException, AppRuntimeException, AppException,
    InformationalException {

    final Map<String, Money> blockTotalsMap = new HashMap<>();

    for (final BDMOASCRAIncomeDataDtls incomeDataDtls : incomeDataDtlsList.dtls) {

      final String incomeBlock = incomeDataDtls.incomeBlock;

      final double factor =
        BDMOASCRALINEITEMCATEGORY.INCOME.equals(CodeTable.getParentCode(
          BDMOASCRALINEITEMTYPE.TABLENAME, incomeDataDtls.lineItem)) ? 1.0
            : -1.0;

      final Money incomeBlockTotal = new Money(
        blockTotalsMap.getOrDefault(incomeBlock, new Money(0.0)).getValue()
          + factor * incomeDataDtls.amount.getValue());

      blockTotalsMap.put(incomeBlock, incomeBlockTotal);
    }

    return blockTotalsMap;
  }

  public static List<CaseHeaderDtls> getCasesForCRAIncomeEvidenceMaintenance(
    final long concernRoleID) throws AppException, InformationalException {

    // Read open application and integrated cases for the participant.
    final List<CaseHeaderDtls> caseHeaderList = BDMOASEvidenceUtil
      .readOpenApplicationAndIntegratedCasesByConcernRoleID(concernRoleID);

    // check application and integrated case requirements
    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderList) {
      // TODO: check ITB application, etc.
    }

    return caseHeaderList;
  }

  public static long performCraIncomeEvidenceMaintenance(final long caseID,
    final BDMOASCRATransactionDtls transactionDtls,
    final BDMOASCRAIncomeDataDtlsList incomeDataDtlsList)
    throws DatabaseException, AppRuntimeException, AppException,
    InformationalException {

    // TODO: update existing evidence if exists for the given tax year.

    final long evidenceID = createCraIncomeEvidenceFromIncomeData(caseID,
      transactionDtls, incomeDataDtlsList);

    return evidenceID;
  }

  private static long createCraIncomeEvidenceFromIncomeData(final long caseID,
    final BDMOASCRATransactionDtls transactionDtls,
    final BDMOASCRAIncomeDataDtlsList incomeDataDtlsList)
    throws DatabaseException, AppRuntimeException, AppException,
    InformationalException {

    final Map<String, Money> blockTotalsMap =
      calculateBlockTotalsForIncomeData(incomeDataDtlsList);

    final BDMOASCRATransactionKey transactionKey =
      new BDMOASCRATransactionKey();
    transactionKey.transactionID = transactionDtls.transactionID;
    final BDMOASCRADataDtls craDataDtls =
      BDMOASCRADataFactory.newInstance().readByTransaction(transactionKey);

    return createCraIncomeEvidence(caseID, transactionDtls.concernRoleID,
      transactionDtls, craDataDtls, incomeDataDtlsList, blockTotalsMap);
  }

  public static long createCraIncomeEvidence(final long caseID,
    final long concernRoleID, final BDMOASCRATransactionDtls transactionDtls,
    final BDMOASCRADataDtls craDataDtls,
    final BDMOASCRAIncomeDataDtlsList incomeDataDtlsList,
    final Map<String, Money> blockTotalsMap)
    throws AppException, InformationalException {

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(caseID, concernRoleID);

    final Money total = setBlockDefaultsAndCalculateTotal(blockTotalsMap);

    final Map<String, Money> lineItemTypeIncomeAmountMap =
      getLineItemTypeIncomeAmountMap(incomeDataDtlsList);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(BDMOASCraIncomeConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(BDMOASCraIncomeConstants.INCOMEDATA_ID,
      String.valueOf(craDataDtls.dataID));
    attributes.put(BDMOASCraIncomeConstants.TAX_YEAR,
      transactionDtls.taxYear);
    attributes.put(BDMOASCraIncomeConstants.IS_PRE_POST_BANKRUPTCY,
      new Boolean(craDataDtls.prePostBankruptcyInd).toString());
    attributes.put(BDMOASCraIncomeConstants.SEQUENCE_NUMBER,
      String.valueOf(transactionDtls.sequenceNumber));
    attributes.put(BDMOASCraIncomeConstants.TRANSACTION_TYPE,
      transactionDtls.transactionSubCode);
    attributes.put(BDMOASCraIncomeConstants.REASSESSMENT,
      String.valueOf(craDataDtls.reassessment));
    attributes.put(BDMOASCraIncomeConstants.BLOCK_1,
      String.valueOf(blockTotalsMap.getOrDefault(BDMOASINCOMEBLOCK.CPP_QPP,
        new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.BLOCK_2,
      String.valueOf(blockTotalsMap.getOrDefault(
        BDMOASINCOMEBLOCK.OTHER_PENSION_INCOME, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.BLOCK_3,
      String.valueOf(blockTotalsMap
        .getOrDefault(BDMOASINCOMEBLOCK.EI_WORKERS_COMP, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.BLOCK_4,
      String.valueOf(blockTotalsMap.getOrDefault(
        BDMOASINCOMEBLOCK.INTEREST_INVESTMENTS, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.BLOCK_5,
      String.valueOf(blockTotalsMap.getOrDefault(
        BDMOASINCOMEBLOCK.DIVIDENDS_CAPITAL_GAINS, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.BLOCK_6,
      String.valueOf(blockTotalsMap
        .getOrDefault(BDMOASINCOMEBLOCK.RENTAL_INCOME, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.BLOCK_7,
      String.valueOf(blockTotalsMap
        .getOrDefault(BDMOASINCOMEBLOCK.EMPLOYMENT_INCOME, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.BLOCK_8,
      String.valueOf(blockTotalsMap.getOrDefault(
        BDMOASINCOMEBLOCK.SELF_EMPLOYMENT_INCOME, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.BLOCK_9,
      String.valueOf(blockTotalsMap
        .getOrDefault(BDMOASINCOMEBLOCK.OTHER_INCOME, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.TOTAL_INCOME,
      String.valueOf(total));
    attributes.put(BDMOASCraIncomeConstants.OTHER_CANADIAN_PENSION_INCOME,
      String.valueOf(lineItemTypeIncomeAmountMap.getOrDefault(
        BDMOASCRALINEITEMTYPE.OTHER_PENSION_INCOME, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.ELECTED_SPLIT_PENSION,
      String.valueOf(lineItemTypeIncomeAmountMap.getOrDefault(
        BDMOASCRALINEITEMTYPE.SPLIT_PENSION_AMT, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.EMPLOYMENT_INSURANCE,
      String.valueOf(lineItemTypeIncomeAmountMap
        .getOrDefault(BDMOASCRALINEITEMTYPE.EI_BENEFIT, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.WORKERS_COMPENSATION_BENEFITS,
      String.valueOf(lineItemTypeIncomeAmountMap.getOrDefault(
        BDMOASCRALINEITEMTYPE.WORKER_COMP_PAYMT_TOTAL, new Money(0.0))));
    attributes.put(
      BDMOASCraIncomeConstants.ELIGIBLE_AND_OTHER_THAN_ELIGIBLEDIVIDENDS,
      String.valueOf(lineItemTypeIncomeAmountMap.getOrDefault(
        BDMOASCRALINEITEMTYPE.DIVIDEND_INCOME, new Money(0.0))));
    attributes.put(BDMOASCraIncomeConstants.CAPITAL_GAINS,
      String.valueOf(lineItemTypeIncomeAmountMap.getOrDefault(
        BDMOASCRALINEITEMTYPE.CAPITAL_TOTAL_NET_CALC, new Money(0.0))));

    final EIEvidenceKey evidenceKey =
      BDMOASEvidenceUtil.createEvidence(caseID, concernRoleID,
        CASEEVIDENCEEntry.OAS_CRA_INCOME, attributes, Date.getCurrentDate());

    return evidenceKey.evidenceID;
  }

  public static Money setBlockDefaultsAndCalculateTotal(
    final Map<String, Money> blockTotalsMap) {

    blockTotalsMap.put(BDMOASINCOMEBLOCK.CPP_QPP,
      blockTotalsMap.getOrDefault(BDMOASINCOMEBLOCK.CPP_QPP, new Money(0.0)));
    blockTotalsMap.put(BDMOASINCOMEBLOCK.OTHER_PENSION_INCOME, blockTotalsMap
      .getOrDefault(BDMOASINCOMEBLOCK.OTHER_PENSION_INCOME, new Money(0.0)));
    blockTotalsMap.put(BDMOASINCOMEBLOCK.EI_WORKERS_COMP, blockTotalsMap
      .getOrDefault(BDMOASINCOMEBLOCK.EI_WORKERS_COMP, new Money(0.0)));
    blockTotalsMap.put(BDMOASINCOMEBLOCK.INTEREST_INVESTMENTS, blockTotalsMap
      .getOrDefault(BDMOASINCOMEBLOCK.INTEREST_INVESTMENTS, new Money(0.0)));
    blockTotalsMap.put(BDMOASINCOMEBLOCK.DIVIDENDS_CAPITAL_GAINS,
      blockTotalsMap.getOrDefault(BDMOASINCOMEBLOCK.DIVIDENDS_CAPITAL_GAINS,
        new Money(0.0)));
    blockTotalsMap.put(BDMOASINCOMEBLOCK.RENTAL_INCOME, blockTotalsMap
      .getOrDefault(BDMOASINCOMEBLOCK.RENTAL_INCOME, new Money(0.0)));
    blockTotalsMap.put(BDMOASINCOMEBLOCK.EMPLOYMENT_INCOME, blockTotalsMap
      .getOrDefault(BDMOASINCOMEBLOCK.EMPLOYMENT_INCOME, new Money(0.0)));
    blockTotalsMap.put(BDMOASINCOMEBLOCK.SELF_EMPLOYMENT_INCOME,
      blockTotalsMap.getOrDefault(BDMOASINCOMEBLOCK.SELF_EMPLOYMENT_INCOME,
        new Money(0.0)));
    blockTotalsMap.put(BDMOASINCOMEBLOCK.OTHER_INCOME, blockTotalsMap
      .getOrDefault(BDMOASINCOMEBLOCK.OTHER_INCOME, new Money(0.0)));

    return new Money(blockTotalsMap.get(BDMOASINCOMEBLOCK.CPP_QPP).getValue()
      + blockTotalsMap.get(BDMOASINCOMEBLOCK.OTHER_PENSION_INCOME).getValue()
      + blockTotalsMap.get(BDMOASINCOMEBLOCK.EI_WORKERS_COMP).getValue()
      + blockTotalsMap.get(BDMOASINCOMEBLOCK.INTEREST_INVESTMENTS).getValue()
      + blockTotalsMap.get(BDMOASINCOMEBLOCK.DIVIDENDS_CAPITAL_GAINS)
        .getValue()
      + blockTotalsMap.get(BDMOASINCOMEBLOCK.RENTAL_INCOME).getValue()
      + blockTotalsMap.get(BDMOASINCOMEBLOCK.EMPLOYMENT_INCOME).getValue()
      + blockTotalsMap.get(BDMOASINCOMEBLOCK.SELF_EMPLOYMENT_INCOME)
        .getValue()
      + blockTotalsMap.get(BDMOASINCOMEBLOCK.OTHER_INCOME).getValue());
  }

  public static Map<String, BDMOASCRAIncomeDataDtls>
    getLineItemTypeIncomeDataMap(
      final BDMOASCRAIncomeDataDtlsList incomeDataDtlsList) {

    final Map<String, BDMOASCRAIncomeDataDtls> incomeDataMap =
      new HashMap<>();
    // loop through income data records
    for (final BDMOASCRAIncomeDataDtls incomeDataDtls : incomeDataDtlsList.dtls) {
      final String lineItemType = incomeDataDtls.lineItem;
      if (!StringUtil.isNullOrEmpty(lineItemType)) {
        incomeDataMap.put(incomeDataDtls.lineItem, incomeDataDtls);
      }
    }
    return incomeDataMap;
  }

  public static Map<String, Money> getLineItemTypeIncomeAmountMap(
    final BDMOASCRAIncomeDataDtlsList incomeDataDtlsList) {

    final Map<String, BDMOASCRAIncomeDataDtls> lineItemTypeIncomeDataMap =
      getLineItemTypeIncomeDataMap(incomeDataDtlsList);

    return lineItemTypeIncomeDataMap.entrySet().stream()
      .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().amount));
  }

}
