package curam.ca.gc.bdmoas.test.deductions.impl;

import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.facade.integratedcase.fact.BDMIntegratedCaseFactory;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMCurrentDeductionsForProductDeliveryList;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMDeductionItemDetail;
import curam.ca.gc.bdm.sl.financial.maintainliabilitycase.struct.ConcernRoleIDKey;
import curam.ca.gc.bdm.sl.struct.CreateCaseDeductionDetails;
import curam.ca.gc.bdm.vtw.deduction.entity.fact.BDMVTWDeductionFactory;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionDtls;
import curam.ca.gc.bdm.vtw.deduction.entity.struct.BDMVTWDeductionKey;
import curam.ca.gc.bdmoas.deductions.impl.BDMOASMaintainDeduction;
import curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRADataFactory;
import curam.ca.gc.bdmoas.entity.oasdeduction.fact.BDMOASDeductionFactory;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionDetails;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionDtls;
import curam.ca.gc.bdmoas.entity.oasdeduction.struct.BDMOASDeductionKey;
import curam.ca.gc.bdmoas.sl.cra.transaction.impl.BDMOASCRATransactionHelper;
import curam.ca.gc.bdmoas.test.rules.functions.CustomFunctionTestJunit4;
import curam.codetable.BUSINESSSTATUS;
import curam.codetable.DEDUCTIONNAME;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.RATEROWTYPE;
import curam.core.facade.fact.RateTableFactory;
import curam.core.facade.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.facade.struct.ListICProductDeliveryDeductionKey;
import curam.core.facade.struct.ReadRateCellValue;
import curam.core.facade.struct.ReadRateRowKey;
import curam.core.facade.struct.ReadRateTableDetails;
import curam.core.facade.struct.ReadRateTableKey;
import curam.core.facade.struct.ReadRowDetails;
import curam.core.facade.struct.ReadValueByColumnAndRowIndexKey;
import curam.core.fact.CaseDeductionItemFCLinkFactory;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.fact.PersonFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.RateHeaderFactory;
import curam.core.sl.entity.fact.RateRowFactory;
import curam.core.sl.entity.struct.RateHeaderByDateTypeIn;
import curam.core.sl.entity.struct.RateHeaderDtls;
import curam.core.sl.entity.struct.RateRowDtls;
import curam.core.sl.entity.struct.RateRowDtlsList;
import curam.core.sl.entity.struct.RateRowSearchByHeaderIDIn;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseDeductionItem;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemFCLinkDtls;
import curam.core.struct.CaseDeductionItemFCLinkDtlsList;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.Count;
import curam.core.struct.FinancialComponentID;
import curam.core.struct.IndicatorStruct;
import curam.dynamicevidence.util.impl.AdminCodetableUtil;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskAdminFactory;
import curam.util.workflow.impl.EnactmentService;
import curam.util.workflow.impl.ProcessInstanceAdmin;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The Class tests {@link BDMOASMaintainDeductionTest} custom
 * function.
 */

@RunWith(JMockit.class)
public class BDMOASMaintainDeductionTest extends CustomFunctionTestJunit4 {

  private static final long CASEID = 101l;

  private static final long VTWDEDUCTIONID = 121l;

  private static final long OASDEDUCTIONID = 122l;

  private static final long RATEHEADERID = 122l;

  private static final long CONCERNROLEID = 102l;

  private static final long CASEDEDUCTIONITEMID = 123l;

  @Tested
  BDMOASMaintainDeduction bdmOASMaintainDeduction;

  @Mocked
  EvidenceDescriptorFactory evidenceDescriptorFactory;

  @Mocked
  BizObjAssociationFactory bizObjAssociationFactory;

  @Mocked
  TaskAdminFactory taskAdminFactory;

  @Mocked
  ProcessInstanceAdmin processInstanceAdmin;

  @Mocked
  curam.util.events.impl.EventService eventService;

  @Mocked
  EnactmentService enactmentService;

  @Mocked
  EvidenceControllerFactory evidenceControllerFactory;

  @Mocked
  CaseHeaderFactory caseHeaderFactory;

  @Mocked
  curam.core.facade.fact.CaseHeaderFactory caseHeaderFactoryFacade;

  @Mocked
  curam.ca.gc.bdm.sl.maintaincasedeductions.impl.MaintainCaseDeductions maintainCaseDeductions;

  @Mocked
  ConcernRoleFactory concernRoleFactory;

  @Mocked
  BDMOASCRADataFactory bdmOASCRADataFactory;

  @Mocked
  curam.core.fact.AlternateNameFactory alternateNameFactory;

  @Mocked
  PersonFactory personFactory;

  @Mocked
  curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRATransactionFactory bdmOASCRATransactionFactoryENT;

  @Mocked
  curam.ca.gc.bdmoas.entity.cra.transaction.fact.BDMOASCRAIncomeDataFactory bdmOASCRAIncomeDataFactory;

  @Mocked
  BDMOASCRATransactionHelper bdmOASCRATransactionHelper;

  @Mocked
  BDMVTWDeductionFactory bdmVTWDeductionFactory;

  @Mocked
  BDMOASDeductionFactory bdmOASDeductionFactory;

  @Mocked
  RateHeaderFactory rateHeaderFactory;

  @Mocked
  RateTableFactory rateTableFactory;

  @Mocked
  RateRowFactory rateRowFactory;

  @Mocked
  BDMIntegratedCaseFactory bdmIntegratedCaseFactory;

  @Mocked
  CaseDeductionItemFCLinkFactory caseDeductionItemFCLinkFactory;

  @Mocked
  InstructionLineItemFactory instructionLineItemFactory;

  @Mocked
  CaseDeductionItemFactory caseDeductionItemFactory;

  @Mocked
  curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl.MaintainParticipantDeduction maintainParticipantDeduction;

  public BDMOASMaintainDeductionTest() {

    super();
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  @Test
  public void testRecordVTWDeductionByIC()
    throws AppException, InformationalException {

    new Expectations() {

      BDMVTWDeductionDtls bdmvtwDeductionDtls = new BDMVTWDeductionDtls();

      CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();

      CaseHeaderDtlsList caseHeadDtlsList = new CaseHeaderDtlsList();

      {
        bdmvtwDeductionDtls.caseID = CASEID;
        bdmvtwDeductionDtls.concernRoleID = CONCERNROLEID;
        bdmVTWDeductionFactory.read((BDMVTWDeductionKey) any);
        result = bdmvtwDeductionDtls;

        caseHeaderDtls.concernRoleID = CONCERNROLEID;
        caseHeaderDtls.integratedCaseType =
          PRODUCTCATEGORY.OAS_OLD_AGE_SECURITY;
        caseHeaderFactory.read((CaseHeaderKey) any);
        result = caseHeaderDtls;

        final CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();
        caseHeaderDtls.caseID = CASEID;
        caseHeadDtlsList.dtlsList.dtls.add(caseHeaderDtls);
        caseHeaderFactoryFacade.searchActiveCasesByTypeConcernRoleID(
          (ActiveCasesConcernRoleIDAndTypeKey) any);
        result = caseHeadDtlsList;

        maintainCaseDeductions.recordVTWDeductionByPDC(
          (curam.core.sl.struct.CaseIDKey) any, (BDMVTWDeductionKey) any);
      }
    };

    final BDMVTWDeductionKey key = new BDMVTWDeductionKey();
    key.vtwDeductionID = VTWDEDUCTIONID;
    bdmOASMaintainDeduction.recordVTWDeductionByIC(key);
  }

  @Test
  public void testRecordOASRecoveryTaxDeductions()
    throws AppException, InformationalException {

    new Expectations() {

      BDMOASDeductionDtls bdmOASDeductionDtls = new BDMOASDeductionDtls();

      CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();

      CaseHeaderDtlsList caseHeadDtlsList = new CaseHeaderDtlsList();

      BDMCaseDeductionItemKey bdmCaseDeductionItemKeyNew =
        new BDMCaseDeductionItemKey();
      {
        bdmOASDeductionDtls.caseID = CASEID;
        bdmOASDeductionDtls.concernRoleID = CONCERNROLEID;
        bdmOASDeductionFactory.read((BDMOASDeductionKey) any);
        result = bdmOASDeductionDtls;

        caseHeaderDtls.concernRoleID = CONCERNROLEID;
        caseHeaderDtls.integratedCaseType =
          PRODUCTCATEGORY.OAS_OLD_AGE_SECURITY;
        caseHeaderFactory.read((CaseHeaderKey) any);
        result = caseHeaderDtls;

        final CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();
        caseHeaderDtls.caseID = CASEID;
        caseHeadDtlsList.dtlsList.dtls.add(caseHeaderDtls);
        caseHeaderFactoryFacade.searchActiveCasesByTypeConcernRoleID(
          (ActiveCasesConcernRoleIDAndTypeKey) any);
        result = caseHeadDtlsList;

        bdmCaseDeductionItemKeyNew.caseDeductionItemID = CASEDEDUCTIONITEMID;
        maintainCaseDeductions.createThirdPartyCaseDeduction(
          (CreateCaseDeductionDetails) any, (BDMExternalLiabilityKey) any);
        result = bdmCaseDeductionItemKeyNew;

        maintainCaseDeductions
          .activateDeductionItem((CaseDeductionItemKey) any);

        bdmOASDeductionFactory.modify((BDMOASDeductionKey) any,
          (BDMOASDeductionDtls) any);
      }
    };

    final BDMOASDeductionKey key = new BDMOASDeductionKey();
    key.bdmOASDeductionID = OASDEDUCTIONID;
    final BDMOASDeductionDetails bdmOASDeductionDetails =
      new BDMOASDeductionDetails();
    bdmOASDeductionDetails.startDate = Date.getCurrentDate().addDays(-30);
    bdmOASDeductionDetails.amount = new Money("200");
    bdmOASDeductionDetails.rate = 5.25;
    bdmOASMaintainDeduction.recordOASRecoveryTaxDeductions(key,
      bdmOASDeductionDetails);
  }

  @Test
  public void testRecordOASNRTDeductions()
    throws AppException, InformationalException {

    final ConcernRoleIDKey concernRoleIDKey = new ConcernRoleIDKey();
    concernRoleIDKey.concernRoleID = CONCERNROLEID;
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = CASEID;
    final double nrtCorrectionPercentage = CuramConst.gkDoubleZero;
    final long nrtCorrectionEvidenceID = 133l;
    bdmOASMaintainDeduction.recordOASNRTDeductions(getYesterday(),
      Date.kZeroDate, caseHeaderKey, concernRoleIDKey, RATEROWTYPE.OAS_NRT_LB,
      Boolean.TRUE.booleanValue(), nrtCorrectionPercentage,
      nrtCorrectionEvidenceID);
  }

  @Test
  public void testRecordOASNRTDeductionsFALSE1()
    throws AppException, InformationalException {

    new Expectations() {

      RateHeaderDtls rateHeaderDtls = new RateHeaderDtls();

      ReadRateTableDetails readRateTableDetails = new ReadRateTableDetails();

      RateRowDtlsList rowDtlsList = new RateRowDtlsList();

      ReadRowDetails readRowDetails = new ReadRowDetails();

      ReadRateCellValue readRateCellValue = new ReadRateCellValue();

      BDMCaseDeductionItemKey bdmCaseDeductionItemKeyNew =
        new BDMCaseDeductionItemKey();

      {
        rateHeaderDtls.rateHeaderID = RATEHEADERID;
        rateHeaderFactory
          .readNearestByEffectiveDateType((RateHeaderByDateTypeIn) any);
        result = rateHeaderDtls;

        readRateTableDetails.readRateTableDetails.effectiveDate =
          Date.getCurrentDate().addDays(-500);
        rateTableFactory.readRateTable((ReadRateTableKey) any);
        result = readRateTableDetails;

        final RateRowDtls rateRowDtls = new RateRowDtls();
        rateRowDtls.rateRowType = AdminCodetableUtil
          .getCodetableCode(RATEROWTYPE.TABLENAME, RATEROWTYPE.OAS_NRT_LB);
        rateRowDtls.rateRowID = 134l;
        rowDtlsList.dtls.add(rateRowDtls);
        rateRowFactory.searchByHeaderID((RateRowSearchByHeaderIDIn) any);
        result = rowDtlsList;

        readRowDetails.readRowDetails.rateRowIndex = CuramConst.gkOne;
        rateTableFactory.readRateRow((ReadRateRowKey) any);
        result = readRowDetails;

        readRateCellValue.dtls.rateCellID = 136l;
        readRateCellValue.dtls.rateCellValue = 6.00;
        rateTableFactory
          .readRateCellValue((ReadValueByColumnAndRowIndexKey) any);
        result = readRateCellValue;

        bdmCaseDeductionItemKeyNew.caseDeductionItemID = CASEDEDUCTIONITEMID;
        maintainCaseDeductions.createThirdPartyCaseDeduction(
          (CreateCaseDeductionDetails) any, (BDMExternalLiabilityKey) any);
        result = bdmCaseDeductionItemKeyNew;

        maintainCaseDeductions
          .activateDeductionItem((CaseDeductionItemKey) any);

        bdmOASDeductionFactory.insert((BDMOASDeductionDtls) any);
      }
    };

    final ConcernRoleIDKey concernRoleIDKey = new ConcernRoleIDKey();
    concernRoleIDKey.concernRoleID = CONCERNROLEID;
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = CASEID;
    final double nrtCorrectionPercentage = 5.25;
    final long nrtCorrectionEvidenceID = 133l;
    bdmOASMaintainDeduction.recordOASNRTDeductions(getYesterday(),
      Date.kZeroDate, caseHeaderKey, concernRoleIDKey, RATEROWTYPE.OAS_NRT_LB,
      Boolean.FALSE.booleanValue(), nrtCorrectionPercentage,
      nrtCorrectionEvidenceID);
  }

  @Test
  public void testRecordOASNRTDeductionsFALSE2()
    throws AppException, InformationalException {

    new Expectations() {

      RateHeaderDtls rateHeaderDtls = new RateHeaderDtls();

      ReadRateTableDetails readRateTableDetails = new ReadRateTableDetails();

      RateRowDtlsList rowDtlsList = new RateRowDtlsList();

      ReadRowDetails readRowDetails = new ReadRowDetails();

      ReadRateCellValue readRateCellValue = new ReadRateCellValue();

      BDMCaseDeductionItemKey bdmCaseDeductionItemKeyNew =
        new BDMCaseDeductionItemKey();

      {
        rateHeaderDtls.rateHeaderID = RATEHEADERID;
        rateHeaderFactory
          .readNearestByEffectiveDateType((RateHeaderByDateTypeIn) any);
        result = rateHeaderDtls;

        readRateTableDetails.readRateTableDetails.effectiveDate =
          Date.getCurrentDate().addDays(-500);
        rateTableFactory.readRateTable((ReadRateTableKey) any);
        result = readRateTableDetails;

        final RateRowDtls rateRowDtls = new RateRowDtls();
        rateRowDtls.rateRowType = AdminCodetableUtil
          .getCodetableCode(RATEROWTYPE.TABLENAME, RATEROWTYPE.OAS_NRT_LB);
        rateRowDtls.rateRowID = 134l;
        rowDtlsList.dtls.add(rateRowDtls);
        rateRowFactory.searchByHeaderID((RateRowSearchByHeaderIDIn) any);
        result = rowDtlsList;

        readRowDetails.readRowDetails.rateRowIndex = CuramConst.gkOne;
        rateTableFactory.readRateRow((ReadRateRowKey) any);
        result = readRowDetails;

        readRateCellValue.dtls.rateCellID = 136l;
        readRateCellValue.dtls.rateCellValue = 5.00;
        rateTableFactory
          .readRateCellValue((ReadValueByColumnAndRowIndexKey) any);
        result = readRateCellValue;

        bdmCaseDeductionItemKeyNew.caseDeductionItemID = CASEDEDUCTIONITEMID;
        maintainCaseDeductions.createThirdPartyCaseDeduction(
          (CreateCaseDeductionDetails) any, (BDMExternalLiabilityKey) any);
        result = bdmCaseDeductionItemKeyNew;

        maintainCaseDeductions
          .activateDeductionItem((CaseDeductionItemKey) any);

        bdmOASDeductionFactory.insert((BDMOASDeductionDtls) any);
      }
    };

    final ConcernRoleIDKey concernRoleIDKey = new ConcernRoleIDKey();
    concernRoleIDKey.concernRoleID = CONCERNROLEID;
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = CASEID;
    final double nrtCorrectionPercentage = 5.25;
    final long nrtCorrectionEvidenceID = 133l;
    bdmOASMaintainDeduction.recordOASNRTDeductions(getYesterday(),
      Date.kZeroDate, caseHeaderKey, concernRoleIDKey, RATEROWTYPE.OAS_NRT_LB,
      Boolean.FALSE.booleanValue(), nrtCorrectionPercentage,
      nrtCorrectionEvidenceID);
  }

  @SuppressWarnings("synthetic-access")
  @Test
  public void testGetNRTOverlappingDeductions()
    throws AppException, InformationalException {

    new Expectations() {

      BDMCurrentDeductionsForProductDeliveryList bdmCurrentDeductionsForProductDeliveryList =
        new BDMCurrentDeductionsForProductDeliveryList();

      {
        final BDMDeductionItemDetail bdmDeductionItemDetail =
          new BDMDeductionItemDetail();
        bdmDeductionItemDetail.deductionName = DEDUCTIONNAME.OAS_NRT;
        bdmDeductionItemDetail.businessStatus = BUSINESSSTATUS.ACTIVE;
        bdmDeductionItemDetail.startDate = getYesterday();
        bdmCurrentDeductionsForProductDeliveryList.dtls
          .add(bdmDeductionItemDetail);
        bdmIntegratedCaseFactory.listCurrentProductDeliveryDeductions(
          (ListICProductDeliveryDeductionKey) any);
        result = bdmCurrentDeductionsForProductDeliveryList;

      }
    };

    final BDMCurrentDeductionsForProductDeliveryList bdmNRTDeductionsForProductDeliveryList =
      bdmOASMaintainDeduction.getNRTOverlappingDeductions(CASEID,
        getYesterday(), Date.kZeroDate);
    assertEquals(CuramConst.gkOne,
      bdmNRTDeductionsForProductDeliveryList.dtls.size());
    assertEquals(DEDUCTIONNAME.OAS_NRT,
      bdmNRTDeductionsForProductDeliveryList.dtls
        .get(CuramConst.gkZero).deductionName);
    assertEquals(BUSINESSSTATUS.ACTIVE,
      bdmNRTDeductionsForProductDeliveryList.dtls
        .get(CuramConst.gkZero).businessStatus);
  }

  @SuppressWarnings("synthetic-access")
  @Test
  public void testGetNRTOverlappingDeductions2()
    throws AppException, InformationalException {

    new Expectations() {

      BDMCurrentDeductionsForProductDeliveryList bdmCurrentDeductionsForProductDeliveryList =
        new BDMCurrentDeductionsForProductDeliveryList();

      {
        final BDMDeductionItemDetail bdmDeductionItemDetail =
          new BDMDeductionItemDetail();
        bdmDeductionItemDetail.deductionName = DEDUCTIONNAME.OAS_NRT;
        bdmDeductionItemDetail.businessStatus = BUSINESSSTATUS.ACTIVE;
        bdmDeductionItemDetail.startDate = getYesterday();
        bdmDeductionItemDetail.endDate = getToday();
        bdmCurrentDeductionsForProductDeliveryList.dtls
          .add(bdmDeductionItemDetail);
        bdmIntegratedCaseFactory.listCurrentProductDeliveryDeductions(
          (ListICProductDeliveryDeductionKey) any);
        result = bdmCurrentDeductionsForProductDeliveryList;

      }
    };

    final BDMCurrentDeductionsForProductDeliveryList bdmNRTDeductionsForProductDeliveryList =
      bdmOASMaintainDeduction.getNRTOverlappingDeductions(CASEID,
        getYesterday(), Date.kZeroDate);
    assertEquals(CuramConst.gkOne,
      bdmNRTDeductionsForProductDeliveryList.dtls.size());
    assertEquals(DEDUCTIONNAME.OAS_NRT,
      bdmNRTDeductionsForProductDeliveryList.dtls
        .get(CuramConst.gkZero).deductionName);
    assertEquals(BUSINESSSTATUS.ACTIVE,
      bdmNRTDeductionsForProductDeliveryList.dtls
        .get(CuramConst.gkZero).businessStatus);
  }

  @Test
  public void testIsNRTDeductionAppliedTRUE()
    throws AppException, InformationalException {

    new Expectations() {

      CaseDeductionItemFCLinkDtlsList caseDeductionItemFCLinkDtlsList =
        new CaseDeductionItemFCLinkDtlsList();

      Count count = new Count();

      {
        final CaseDeductionItemFCLinkDtls caseDeductionItemFCLinkDtls =
          new CaseDeductionItemFCLinkDtls();
        caseDeductionItemFCLinkDtls.financialComponentID = 141l;
        caseDeductionItemFCLinkDtlsList.dtls.add(caseDeductionItemFCLinkDtls);
        caseDeductionItemFCLinkFactory
          .searchByCaseDeductionItem((CaseDeductionItem) any);
        result = caseDeductionItemFCLinkDtlsList;

        count.numberOfRecords = CuramConst.gkOne;
        instructionLineItemFactory
          .countByFinancialCompID((FinancialComponentID) any);
        result = count;

      }
    };

    final BDMDeductionItemDetail bdmDeductionItemDetail =
      new BDMDeductionItemDetail();
    bdmDeductionItemDetail.caseDeductionItemID = CASEDEDUCTIONITEMID;
    final boolean isNRTDeductionApplied =
      bdmOASMaintainDeduction.isNRTDeductionApplied(bdmDeductionItemDetail);
    assertTrue(isNRTDeductionApplied);
  }

  @Test
  public void testIsNRTDeductionAppliedFALSE()
    throws AppException, InformationalException {

    new Expectations() {

      CaseDeductionItemFCLinkDtlsList caseDeductionItemFCLinkDtlsList =
        new CaseDeductionItemFCLinkDtlsList();

      Count count = new Count();

      {
        final CaseDeductionItemFCLinkDtls caseDeductionItemFCLinkDtls =
          new CaseDeductionItemFCLinkDtls();
        caseDeductionItemFCLinkDtls.financialComponentID = 141l;
        caseDeductionItemFCLinkDtlsList.dtls.add(caseDeductionItemFCLinkDtls);
        caseDeductionItemFCLinkFactory
          .searchByCaseDeductionItem((CaseDeductionItem) any);
        result = caseDeductionItemFCLinkDtlsList;

        count.numberOfRecords = CuramConst.gkZero;
        instructionLineItemFactory
          .countByFinancialCompID((FinancialComponentID) any);
        result = count;

      }
    };

    final BDMDeductionItemDetail bdmDeductionItemDetail =
      new BDMDeductionItemDetail();
    bdmDeductionItemDetail.caseDeductionItemID = CASEDEDUCTIONITEMID;
    final boolean isNRTDeductionApplied =
      bdmOASMaintainDeduction.isNRTDeductionApplied(bdmDeductionItemDetail);
    assertFalse(isNRTDeductionApplied);
  }

  @Test
  public void testProcessExistingNRTDeduction()
    throws AppException, InformationalException {

    new Expectations() {

      {
        maintainParticipantDeduction.deactivateSingleCaseDeduction(
          (CaseDeductionItemKey) any, (IndicatorStruct) any);
        times = 1;
      }
    };

    final BDMDeductionItemDetail bdmDeductionItemDetail =
      new BDMDeductionItemDetail();
    bdmDeductionItemDetail.caseDeductionItemID = CASEDEDUCTIONITEMID;
    bdmOASMaintainDeduction.processExistingNRTDeduction(
      Boolean.FALSE.booleanValue(), bdmDeductionItemDetail, getYesterday(),
      Date.kZeroDate);
  }

  @Test
  public void testProcessExistingNRTDeductionDeductionApplied()
    throws AppException, InformationalException {

    new Expectations() {

      CaseDeductionItemDtls caseDeductionItemDtls =
        new CaseDeductionItemDtls();

      {
        maintainParticipantDeduction.deactivateSingleCaseDeduction(
          (CaseDeductionItemKey) any, (IndicatorStruct) any);
        times = 0;

        caseDeductionItemDtls.businessStatus = BUSINESSSTATUS.ACTIVE;
        caseDeductionItemFactory.read((CaseDeductionItemKey) any);
        result = caseDeductionItemDtls;

        caseDeductionItemFactory.modify((CaseDeductionItemKey) any,
          (CaseDeductionItemDtls) any);
      }
    };

    final BDMDeductionItemDetail bdmDeductionItemDetail =
      new BDMDeductionItemDetail();
    bdmDeductionItemDetail.caseDeductionItemID = CASEDEDUCTIONITEMID;
    bdmOASMaintainDeduction.processExistingNRTDeduction(
      Boolean.TRUE.booleanValue(), bdmDeductionItemDetail, getYesterday(),
      Date.kZeroDate);
  }

  @Test
  public void testProcessExistingNRTDeductionDeductionApplied2()
    throws AppException, InformationalException {

    new Expectations() {

      CaseDeductionItemDtls caseDeductionItemDtls =
        new CaseDeductionItemDtls();

      {
        maintainParticipantDeduction.deactivateSingleCaseDeduction(
          (CaseDeductionItemKey) any, (IndicatorStruct) any);
        times = 0;

        caseDeductionItemDtls.businessStatus = BUSINESSSTATUS.ACTIVE;
        caseDeductionItemFactory.read((CaseDeductionItemKey) any);
        result = caseDeductionItemDtls;

        caseDeductionItemFactory.modify((CaseDeductionItemKey) any,
          (CaseDeductionItemDtls) any);
      }
    };

    final BDMDeductionItemDetail bdmDeductionItemDetail =
      new BDMDeductionItemDetail();
    bdmDeductionItemDetail.caseDeductionItemID = CASEDEDUCTIONITEMID;
    bdmOASMaintainDeduction.processExistingNRTDeduction(
      Boolean.TRUE.booleanValue(), bdmDeductionItemDetail,
      getYesterday().addDays(20), Date.kZeroDate);
  }

  @Test
  public void testProcessExistingNRTDeductionDeductionApplied3()
    throws AppException, InformationalException {

    new Expectations() {

      CaseDeductionItemDtls caseDeductionItemDtls =
        new CaseDeductionItemDtls();

      {
        maintainParticipantDeduction.deactivateSingleCaseDeduction(
          (CaseDeductionItemKey) any, (IndicatorStruct) any);
        times = 0;

        caseDeductionItemDtls.businessStatus = BUSINESSSTATUS.INACTIVE;
        caseDeductionItemFactory.read((CaseDeductionItemKey) any);
        result = caseDeductionItemDtls;

        caseDeductionItemFactory.modify((CaseDeductionItemKey) any,
          (CaseDeductionItemDtls) any);
        times = 0;
      }
    };

    final BDMDeductionItemDetail bdmDeductionItemDetail =
      new BDMDeductionItemDetail();
    bdmDeductionItemDetail.caseDeductionItemID = CASEDEDUCTIONITEMID;
    bdmOASMaintainDeduction.processExistingNRTDeduction(
      Boolean.TRUE.booleanValue(), bdmDeductionItemDetail,
      getYesterday().addDays(-CuramConst.gkOne), Date.kZeroDate);
  }
}
