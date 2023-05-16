package curam.ca.gc.bdmoas.sl.cra.transaction.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdmoas.codetable.BDMOASCRATRANSACTIONCODE;
import curam.ca.gc.bdmoas.codetable.BDMOASCRATRANSACTIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASINCOMEBLOCK;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRADataFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRAIncomeDataFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRATransactionFactory;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRADataDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataCRADataKey;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRAIncomeDataDtlsList;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionConcernRoleStatusKey;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionDtls;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionDtlsList;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionKey;
import curam.ca.gc.bdmoas.entity.cra.transaction.struct.BDMOASCRATransactionKeyStruct1;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRADataDetailsCP;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRAHistoryDtls;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRAHistoryDtlsList;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRAIBLineItems;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRAIBLineItemsList;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestCreateDetails;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestCreateOptions;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestMember;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestSummary;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRARequestSummaryList;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRATaxYearSummary;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASCRATaxYearSummaryList;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASTaxYearDetails;
import curam.ca.gc.bdmoas.facade.cra.transaction.struct.BDMOASTaxYearDetailsKey;
import curam.ca.gc.bdmoas.impl.BDMOASUtil;
import curam.ca.gc.bdmoas.message.BDMOASAPPLICATIONCASE;
import curam.commonintake.message.APPLICATIONCASE;
import curam.core.fact.AlternateNameFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.PersonFactory;
import curam.core.struct.AlternateNameDtls;
import curam.core.struct.AlternateNameKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRoleNameDetails;
import curam.core.struct.ParticipantAndCaseTypeDetails;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.impl.Individual;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.casemanager.impl.CaseParticipantRole;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Money;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.map.HashedMap;

public class BDMOASCRATransaction
  implements curam.ca.gc.bdmoas.sl.cra.transaction.intf.BDMOASCRATransaction {

  @Inject
  CaseHeaderDAO CaseHeaderDAO;

  @Inject
  ConcernRoleDAO concernRoleDao;
  /*
   * @Inject
   * AlternatenameDAO AlternatenameDAO;
   */

  public BDMOASCRATransaction() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * List in progress for case.
   *
   * @param key the key
   * @return the BDMOASCRA request summary list
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASCRARequestSummaryList listInProgressForCase(final CaseKey key)
    throws AppException, InformationalException {

    ParticipantAndCaseTypeDetails partCaseTypedtls =
      new ParticipantAndCaseTypeDetails();
    final BDMOASCRARequestSummaryList requSummaryList =
      new BDMOASCRARequestSummaryList();
    BDMOASCRARequestSummary requSummary = null;
    final CaseHeaderKey key1 = new CaseHeaderKey();
    key1.caseID = key.caseID;
    partCaseTypedtls =
      CaseHeaderFactory.newInstance().readParticipantAndCaseTypeDetails(key1);

    final ConcernRoleKey concernrolKey = new ConcernRoleKey();
    concernrolKey.concernRoleID = partCaseTypedtls.concernRoleID;
    final BDMOASCRATransactionDtlsList dtlsList = BDMOASCRATransactionFactory
      .newInstance().searchByConcernRole(concernrolKey);
    for (final BDMOASCRATransactionDtls dtls : dtlsList.dtls) {
      if (BDMOASCRATRANSACTIONSTATUS.PENDING.equals(dtls.status)) {
        requSummary = new BDMOASCRARequestSummary();
        concernrolKey.concernRoleID = dtls.concernRoleID;
        requSummary.concernRoleID = dtls.concernRoleID;
        requSummary.concernRoleName = ConcernRoleFactory.newInstance()
          .readConcernRoleName(concernrolKey).concernRoleName;
        requSummary.taxYear = dtls.taxYear;
        requSummary.transactionSubCode = dtls.transactionSubCode;
        requSummary.status = dtls.status;
        requSummary.requestedBy = TransactionInfo.getProgramUser();
        requSummary.requestDateTime = dtls.createdOn;
        requSummary.sentDateTime = dtls.createdOn;

        requSummaryList.dtls.add(requSummary);
      }

    }
    requSummaryList.isRequestEnabled = true;
    return requSummaryList;
  }

  /**
   * List tax years for case.
   *
   * @param key the key
   * @return the BDMOASCRA tax year summary list
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASCRATaxYearSummaryList listTaxYearsForCase(final CaseKey key)
    throws AppException, InformationalException {

    ParticipantAndCaseTypeDetails partCaseTypedtls =
      new ParticipantAndCaseTypeDetails();
    final BDMOASCRATaxYearSummaryList taxSummaryList =
      new BDMOASCRATaxYearSummaryList();
    BDMOASCRATaxYearSummary taxSummary = null;
    final CaseHeaderKey key1 = new CaseHeaderKey();
    key1.caseID = key.caseID;
    partCaseTypedtls =
      CaseHeaderFactory.newInstance().readParticipantAndCaseTypeDetails(key1);

    final ConcernRoleKey concernrolKey = new ConcernRoleKey();
    concernrolKey.concernRoleID = partCaseTypedtls.concernRoleID;
    final BDMOASCRATransactionDtlsList dtlsList = BDMOASCRATransactionFactory
      .newInstance().searchByConcernRole(concernrolKey);
    final BDMOASCRATransactionKey trasactionKey =
      new BDMOASCRATransactionKey();
    BDMOASCRADataDtls dataDtls = new BDMOASCRADataDtls();
    final Map<String, DateTime> trackUprequest = new HashedMap<>();

    for (final BDMOASCRATransactionDtls dtls : dtlsList.dtls) {
      if (BDMOASCRATRANSACTIONSTATUS.RECEIVED.equals(dtls.status)) {
        trasactionKey.transactionID = dtls.transactionID;
        dataDtls =
          BDMOASCRADataFactory.newInstance().readByTransaction(trasactionKey);

        if (!trackUprequest.containsKey(dtls.taxYear)) {
          trackUprequest.put(dtls.taxYear, dtls.createdOn);
          concernrolKey.concernRoleID = dtls.concernRoleID;
          final ConcernRoleNameDetails conRoleDtls = ConcernRoleFactory
            .newInstance().readConcernRoleName(concernrolKey);
          taxSummary = new BDMOASCRATaxYearSummary();
          taxSummary.transactionID = dtls.transactionID;
          taxSummary.concernRoleID = dtls.concernRoleID;
          taxSummary.concernRoleName = conRoleDtls.concernRoleName;
          taxSummary.taxYear = dtls.taxYear;
          taxSummary.transactionSubCode = dtls.transactionSubCode;
          taxSummary.status = dtls.status;
          taxSummary.responseStatus = dataDtls.responseStatus;
          taxSummary.incomeStatus = dataDtls.incomeStatus;
          taxSummary.lastUpdatedOn = dtls.createdOn;
          taxSummaryList.dtls.add(taxSummary);
        }
      }

      // break;
    }

    return taxSummaryList;

  }

  /**
   * Creates the request.
   *
   * @param details the details
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public void createRequest(final BDMOASCRARequestCreateDetails details)
    throws AppException, InformationalException {

    final BDMOASCRATransactionConcernRoleStatusKey conStatusKey =
      new BDMOASCRATransactionConcernRoleStatusKey();
    conStatusKey.concernRoleID = details.concernRoleID;
    conStatusKey.status = BDMOASCRATRANSACTIONSTATUS.PENDING;
    final BDMOASCRATransactionDtlsList tranList = BDMOASCRATransactionFactory
      .newInstance().searchByConcernRoleAndStatus(conStatusKey);

    final String[] taxYears = details.taxYearList.split("\t");
    for (final String taxYear : taxYears) {

      for (final BDMOASCRATransactionDtls dtls : tranList.dtls) {
        if (dtls.taxYear.equals(taxYear)
          && dtls.transactionSubCode.equals(details.transactionSubCode)) {
          final AppException appException =
            new AppException(BDMOASAPPLICATIONCASE.TRASANCTION_EXIST);
          appException.arg(CodeTable.getOneItem("BDMOASCRATransactionCode",
            details.transactionSubCode.toString()));
          final curam.participant.impl.ConcernRole concernRole =
            concernRoleDao.get(details.concernRoleID);
          appException.arg(concernRole.getName());
          appException.arg(taxYear);
          throw appException;
        }

        final BDMOASCRATransactionDtls tranDtls =
          new BDMOASCRATransactionDtls();
        tranDtls.concernRoleID = details.concernRoleID;
        tranDtls.transactionSubCode = details.transactionSubCode;
        tranDtls.transactionCode = BDMOASCRATRANSACTIONCODE.ONE_WAY;
        tranDtls.taxYear = taxYear;
        tranDtls.status = BDMOASCRATRANSACTIONSTATUS.PENDING;
        tranDtls.createdBy = TransactionInfo.getProgramUser();
        tranDtls.createdOn = TransactionInfo.getSystemDateTime();
        tranDtls.lastUpdatedBy = TransactionInfo.getProgramUser();
        tranDtls.lastUpdatedOn = TransactionInfo.getSystemDateTime();
        BDMOASCRATransactionFactory.newInstance().insert(tranDtls);
        final PersonKey personKey = new PersonKey();
        personKey.concernRoleID = details.concernRoleID;
        final PersonDtls personDtls =
          PersonFactory.newInstance().read(personKey);
        final AlternateNameKey nameKey = new AlternateNameKey();
        nameKey.alternateNameID = personDtls.primaryAlternateNameID;
        final AlternateNameDtls nameDtls =
          AlternateNameFactory.newInstance().read(nameKey);
        final BDMOASCRADataDtls dataDtls = new BDMOASCRADataDtls();
        dataDtls.firstForename = nameDtls.firstForename;
        dataDtls.surname = nameDtls.surname;
        dataDtls.dateOfBirth = personDtls.dateOfBirth;
        dataDtls.transactionID = tranDtls.transactionID;
        dataDtls.prePostBankruptcyInd = false;
        System.out.println("tudhs");
        BDMOASCRADataFactory.newInstance().insert(dataDtls);
      }
    }

  }

  /**
   * Gets the request options for case.
   *
   * @param key the key
   * @return the request options for case
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASCRARequestCreateOptions getRequestOptionsForCase(
    final CaseKey key) throws AppException, InformationalException {

    final BDMOASCRARequestCreateOptions list =
      new BDMOASCRARequestCreateOptions();
    // Member populate Implementation
    final List<CaseParticipantRole> clients =
      CaseHeaderDAO.get(key.caseID).listActiveCaseMembers();
    for (final CaseParticipantRole casepartipantRole : clients) {

      if (casepartipantRole.isMember()) {
        final BDMOASCRARequestMember member = new BDMOASCRARequestMember();
        final curam.participant.impl.ConcernRole concernRole =
          casepartipantRole.getConcernRole();
        final LocalisableString concernRoleNameAndAge =
          getNameAndAge(concernRole);
        member.concernRoleID = concernRole.getID().longValue();
        member.concernRoleName = concernRole.getName();// concernRoleNameAndAge.getMessage();
        list.members.add(member);

      }

    }
    // Tax Year list Implementation
    list.taxListRequest = BDMOASUtil.GetFinacilatxYearList();

    return list;
  }

  private LocalisableString getNameAndAge(final ConcernRole concernRole) {

    final LocalisableString nameAndAge =
      new LocalisableString(APPLICATIONCASE.INF_CLIENT_NAME_AND_AGE);
    nameAndAge.arg(concernRole.getName());
    if (concernRole instanceof Individual) {
      final Individual individual = (Individual) concernRole;
      if (individual.isDeceased()) {
        nameAndAge.arg(APPLICATIONCASE.INF_DECEASED_TEXT
          .getMessageText(TransactionInfo.getProgramLocale()));
      } else {
        nameAndAge.arg(individual.getAge(Date.getCurrentDate())
          .getMessage(TransactionInfo.getProgramLocale()));
      }
    }
    return nameAndAge;
  }

  /**
   * Gets the tax year details.
   *
   * @param key the key
   * @return the tax year details
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASTaxYearDetails
    getTaxYearDetails(final BDMOASTaxYearDetailsKey key)
      throws AppException, InformationalException {

    final BDMOASTaxYearDetails taxYearDetails = new BDMOASTaxYearDetails();
    BDMOASCRAIBLineItems ibLineItems;

    final BDMOASCRAIncomeDataCRADataKey craDataKey =
      new BDMOASCRAIncomeDataCRADataKey();
    final BDMOASCRATransactionKey transKey = new BDMOASCRATransactionKey();
    transKey.transactionID = key.transactionID;
    craDataKey.dataID =
      BDMOASCRADataFactory.newInstance().readByTransaction(transKey).dataID;
    final BDMOASCRAIncomeDataDtlsList dtlsList =
      BDMOASCRAIncomeDataFactory.newInstance().searchByCRAData(craDataKey);
    final Map<String, Money> blockIncomeData =
      BDMOASCRATransactionHelper.calculateBlockTotalsForIncomeData(dtlsList);

    for (final String blockItemName : blockIncomeData.keySet()) {
      if (BDMOASINCOMEBLOCK.CPP_QPP.equals(blockItemName))
        taxYearDetails.incomeBlk1 = blockIncomeData.get(blockItemName);
      if (BDMOASINCOMEBLOCK.OTHER_PENSION_INCOME.equals(blockItemName))
        taxYearDetails.incomeBlk2 = blockIncomeData.get(blockItemName);
      if (BDMOASINCOMEBLOCK.EI_WORKERS_COMP.equals(blockItemName))
        taxYearDetails.incomeBlk3 = blockIncomeData.get(blockItemName);
      if (BDMOASINCOMEBLOCK.INTEREST_INVESTMENTS.equals(blockItemName))
        taxYearDetails.incomeBlk4 = blockIncomeData.get(blockItemName);
      if (BDMOASINCOMEBLOCK.DIVIDENDS_CAPITAL_GAINS.equals(blockItemName))
        taxYearDetails.incomeBlk5 = blockIncomeData.get(blockItemName);
      if (BDMOASINCOMEBLOCK.RENTAL_INCOME.equals(blockItemName))
        taxYearDetails.incomeBlk6 = blockIncomeData.get(blockItemName);
      if (BDMOASINCOMEBLOCK.EMPLOYMENT_INCOME.equals(blockItemName))
        taxYearDetails.incomeBlk7 = blockIncomeData.get(blockItemName);
      if (BDMOASINCOMEBLOCK.SELF_EMPLOYMENT_INCOME.equals(blockItemName))
        taxYearDetails.incomeBlk8 = blockIncomeData.get(blockItemName);
      if (BDMOASINCOMEBLOCK.OTHER_INCOME.equals(blockItemName))
        taxYearDetails.incomeBlk9 = blockIncomeData.get(blockItemName);
    }
    taxYearDetails.total = new Money(taxYearDetails.incomeBlk1.getValue()
      + taxYearDetails.incomeBlk2.getValue()
      + taxYearDetails.incomeBlk3.getValue()
      + taxYearDetails.incomeBlk4.getValue()
      + taxYearDetails.incomeBlk5.getValue()
      + taxYearDetails.incomeBlk6.getValue()
      + taxYearDetails.incomeBlk7.getValue()
      + taxYearDetails.incomeBlk8.getValue()
      + taxYearDetails.incomeBlk9.getValue());

    /*
     * final String taxYear =
     * BDMOASCRATransactionFactory.newInstance().read(transKey).taxYear;
     * Map<String, BDMOASIncomeMappingItemDtls> itemdtlsmap=
     * BDMOASCRATransactionHelper.getBlockMappedIncomeItemsMapForTaxYear(taxYear
     * );
     */
    int i = 0;
    final BDMOASCRAIBLineItemsList ibLineItemsLList =
      new BDMOASCRAIBLineItemsList();
    final BDMOASCRAIBLineItemsList ibLineItemsRList =
      new BDMOASCRAIBLineItemsList();
    for (final BDMOASCRAIncomeDataDtls dtla : dtlsList.dtls) {
      i++;
      ibLineItems = new BDMOASCRAIBLineItems();
      ibLineItems.amount = dtla.amount;
      ibLineItems.itemName = CodeTable.getOneItem("BDMOASCRALineItemType",
        dtla.lineItem.toString());

      ibLineItems.block = dtla.incomeBlock;
      if (i % 2 != 0)
        ibLineItemsLList.blklist.add(ibLineItems);
      else
        ibLineItemsRList.blklist.add(ibLineItems);

    }
    taxYearDetails.leftList = ibLineItemsLList;
    taxYearDetails.rightList = ibLineItemsRList;
    return taxYearDetails;
  }

  /**
   * Gets the data details.
   *
   * @param key the key
   * @return the data details
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Override
  public BDMOASCRADataDetailsCP
    getDataDetails(final BDMOASCRATransactionKey key)
      throws AppException, InformationalException {

    final BDMOASCRADataDetailsCP cpDetails = new BDMOASCRADataDetailsCP();
    final BDMOASCRATransactionKeyStruct1 taxkyearKey =
      new BDMOASCRATransactionKeyStruct1();

    final BDMOASCRATransactionKey transKey = new BDMOASCRATransactionKey();
    transKey.transactionID = key.transactionID;
    final BDMOASCRATransactionDtls trancDtls =
      BDMOASCRATransactionFactory.newInstance().read(transKey);
    final BDMOASCRADataDtls datadtls =
      BDMOASCRADataFactory.newInstance().readByTransaction(transKey);
    final ConcernRoleKey concernrolKey = new ConcernRoleKey();
    concernrolKey.concernRoleID = trancDtls.concernRoleID;
    cpDetails.concernRoleName = ConcernRoleFactory.newInstance()
      .readConcernRoleName(concernrolKey).concernRoleName;
    cpDetails.sequenceNumber = trancDtls.sequenceNumber;
    cpDetails.reassessment = datadtls.reassessment;

    cpDetails.transactionType = trancDtls.transactionSubCode;
    cpDetails.requestedBy = trancDtls.createdBy;
    cpDetails.requestDateTime = trancDtls.createdOn;
    cpDetails.receivedDateTime = datadtls.receivedDateTime;
    cpDetails.prePostBankruptcyInd = datadtls.prePostBankruptcyInd;
    cpDetails.sin = datadtls.sin;
    cpDetails.surname = datadtls.surname;
    cpDetails.dateOfBirth = datadtls.dateOfBirth;
    cpDetails.dateOfDeath = datadtls.dateOfDeath;
    cpDetails.maritalStatus = datadtls.maritalStatus;
    cpDetails.spouseSIN = datadtls.spouseSIN;
    cpDetails.taxYear = trancDtls.taxYear;
    return cpDetails;
  }

  @Override
  public BDMOASCRAHistoryDtlsList
    getHistoryDetails(final BDMOASCRATransactionKey key)
      throws AppException, InformationalException {

    final BDMOASCRAHistoryDtlsList histDlList =
      new BDMOASCRAHistoryDtlsList();
    BDMOASCRAHistoryDtls histDetails = null;

    final BDMOASCRATransactionDtls dtls =
      BDMOASCRATransactionFactory.newInstance().read(key);
    final BDMOASCRATransactionKeyStruct1 taxkyearKey =
      new BDMOASCRATransactionKeyStruct1();
    final BDMOASCRATransactionKey transKey = new BDMOASCRATransactionKey();
    transKey.transactionID = key.transactionID;
    taxkyearKey.concernRoleID = dtls.concernRoleID;
    taxkyearKey.taxYear = dtls.taxYear;
    final BDMOASCRATransactionDtlsList tranList = BDMOASCRATransactionFactory
      .newInstance().searchByConcernRoleTaxyear(taxkyearKey);

    // tranList.dtls.sort(null);

    final List<BDMOASCRATransactionDtls> sortedtranslist =
      tranList.dtls.stream().sorted((b, c) -> {
        if (b.createdOn.equals(c.createdOn)) {
          return 0;
        } else {
          return b.createdOn.before(c.createdOn) ? 1 : -1;
        }
      }).collect(Collectors.toList());
    for (final BDMOASCRATransactionDtls transdtls : tranList.dtls) {
      histDetails = new BDMOASCRAHistoryDtls();
      transKey.transactionID = transdtls.transactionID;
      final BDMOASCRADataDtls datadtls =
        BDMOASCRADataFactory.newInstance().readByTransaction(transKey);
      histDetails.transactionID = transdtls.transactionID;
      histDetails.reassessment = datadtls.reassessment;
      histDetails.transactionType = transdtls.transactionSubCode;
      histDetails.responseStatus = datadtls.responseStatus;
      histDetails.incomeStatus = datadtls.incomeStatus;
      histDetails.lastUpdatedOn = transdtls.createdOn;
      histDlList.dtlsHistory.add(histDetails);

    }

    return histDlList;
  }

}
