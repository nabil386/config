package curam.ca.gc.bdm.sl.maintainexternalliability.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMALERTCHOICE;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.codetable.BDMEXTERNALLIABILITYSTATUS;
import curam.ca.gc.bdm.codetable.BDMEXTERNALLIABILITYTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSITUATION;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.fact.BDMCaseDeductionItemFactory;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemDtls;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory;
import curam.ca.gc.bdm.entity.bdmexternalliability.intf.BDMExternalLiability;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityConcernRoleTypeKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityDtls;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilitySearchDetails;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilitySearchDetailsList;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilitySearchKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityVersionNumber;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.DOJDeductionForILIDetails;
import curam.ca.gc.bdm.entity.bdmexternalliabilityhistory.fact.BDMExternalLiabilityHistoryFactory;
import curam.ca.gc.bdm.entity.bdmexternalliabilityhistory.struct.BDMExternalLiabilityHistorySearchDtls;
import curam.ca.gc.bdm.entity.bdmexternalliabilityhistory.struct.BDMExternalLiabilityHistorySearchDtlsList;
import curam.ca.gc.bdm.entity.bdmexternalliabilityhistory.struct.BDMExternalLiabilityHistorySearchKey;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.intf.BDMDeduction;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDetails;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionKey;
import curam.ca.gc.bdm.entity.deduction.struct.BDMRecordStatusExternalLiabilityKey;
import curam.ca.gc.bdm.entity.fact.BDMFinancialComponentDAFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.fact.BDMParticipantDeductionItemFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.intf.BDMParticipantDeductionItem;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMPDIExternalLiabilityKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemDtls;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMInstructionLineItemFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMInstructionLineItem;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemKey;
import curam.ca.gc.bdm.entity.intf.BDMFinancialComponentDA;
import curam.ca.gc.bdm.entity.struct.BDMLiabilityKey;
import curam.ca.gc.bdm.entity.struct.BDMLiabilityKeyList;
import curam.ca.gc.bdm.entity.struct.SearchActiveDOJForPayRunKey;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.fact.BDMInstructionLineItemDAFactory;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.intf.BDMInstructionLineItemDA;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.BDMTotalAllocationByExtLiabilityKey;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.SearchAllUnLineItemsForDOJDedDetails;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.SearchAllUnLineItemsForDOJDedDetailsList;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.SearchAllUnLineItemsForDOJDedKey;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.SearchAllUnLineItemsForTaxDedDetails;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.SearchAllUnLineItemsForTaxDedDetailsList;
import curam.ca.gc.bdm.entity.subclass.instructionlineitem.struct.SearchAllUnLineItemsForTaxDedKey;
import curam.ca.gc.bdm.entity.subclass.productdelivery.fact.BDMProductDeliveryDAFactory;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.BDMSearchEligBenefitsForExternalLbyDtls;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.BDMSearchEligBenefitsForExternalLbyDtlsList;
import curam.ca.gc.bdm.entity.subclass.productdelivery.struct.BDMSearchEligBenefitsForExternalLbyKey;
import curam.ca.gc.bdm.facade.bdmexternalliability.impl.BDMExternalLiabilityConstants;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardIDKey;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardSaveStep1Details;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardSaveStep2Details;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityBenefitDetails;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityBenefitDetailsList;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityDetails;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityDetailsList;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityHistoryDetails;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityHistoryDetailsList;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMModifyLiabilityDetails;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMReadLbyDetailsForEdit;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMRegisterExternalLbyDetails;
import curam.ca.gc.bdm.message.BDMEXTERNALLIABILITY;
import curam.ca.gc.bdm.message.BDMFINANCIALS;
import curam.ca.gc.bdm.sl.financial.maintaininstructionlineitem.struct.CaseNomineeTaxAmountDetails;
import curam.ca.gc.bdm.sl.financial.maintaininstructionlineitem.struct.CaseNomineeTaxAmountList;
import curam.ca.gc.bdm.sl.financial.maintaininstructionlineitem.struct.CaseNomineeTaxComponentDetails;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionCaseLinkDetails;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionCaseLinkList;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionCreateDetails;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionModifyDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMCheckLiabilityExistDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMModifyDOJLbyDetailsKey;
import curam.ca.gc.bdm.sl.financial.struct.GenILIDOJDeductionDetails;
import curam.ca.gc.bdm.sl.financial.struct.GenILITaxDeductionDetails;
import curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl.MaintainParticipantDeduction;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CREDITDEBIT;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.FINCOMPONENTSTATUS;
import curam.codetable.ILICATEGORY;
import curam.codetable.ILISTATUS;
import curam.codetable.ILITYPE;
import curam.codetable.PRODUCTTYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.MaintainInstructionLineItemFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.AddressElement;
import curam.core.intf.CaseHeader;
import curam.core.intf.MaintainInstructionLineItem;
import curam.core.intf.Person;
import curam.core.sl.entity.fact.DeductionFactory;
import curam.core.sl.entity.struct.DeductionDtls;
import curam.core.sl.entity.struct.DeductionKey;
import curam.core.sl.fact.UserAccessFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.paymentcorrection.impl.PaymentCorrectionImpl;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.UserAccess;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.Amount;
import curam.core.struct.CaseByConcernRoleIDStatusAndTypeKey;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseID;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.Count;
import curam.core.struct.DateStruct;
import curam.core.struct.DeductibleInd;
import curam.core.struct.FCAmount;
import curam.core.struct.FinCompCoverPeriod;
import curam.core.struct.FinancialComponentDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.RulesObjectiveID;
import curam.core.struct.TotalDeductionAmount;
import curam.core.struct.UniqueIDKeySet;
import curam.core.struct.UserFullname;
import curam.core.struct.UsersKey;
import curam.core.struct.VersionNumberDetails;
import curam.creole.execution.RuleObject;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleitem.RuleSet;
import curam.creole.storage.database.RuleSetManager;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DateConverter;
import curam.dynamicevidence.type.impl.IntegerConverter;
import curam.message.BPOCASETAB;
import curam.message.BPOMAINTAINDEDUCTIONITEMS;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.CodeTableItemIdentifier;
import curam.util.type.Date;
import curam.util.type.FrequencyPattern;
import curam.util.type.FrequencyPattern.PatternType;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class MaintainExternalLiability implements
  curam.ca.gc.bdm.sl.maintainexternalliability.intf.MaintainExternalLiability {

  @Inject
  PaymentCorrectionImpl paymentCorrectionObj;

  @Inject
  MaintainParticipantDeduction maintainParticipantDeductionObj;

  public MaintainExternalLiability() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Lists all the external liabilities for a concern role
   */
  @Override
  public BDMExternalLiabilityDetailsList listLiabilities(
    final ConcernRoleKey key) throws AppException, InformationalException {

    final BDMExternalLiabilityDetailsList bdmLiabilitiesList =
      new BDMExternalLiabilityDetailsList();

    // search for all non-deleted liabilities belonging to the participant
    final BDMExternalLiabilitySearchKey extLbyKey =
      new BDMExternalLiabilitySearchKey();
    extLbyKey.concernRoleID = key.concernRoleID;
    extLbyKey.recordStatusCode = RECORDSTATUS.NORMAL;
    final BDMExternalLiabilitySearchDetailsList liabilitiesList =
      BDMExternalLiabilityFactory.newInstance().searchLiabilities(extLbyKey);

    BDMExternalLiabilityDetails bdmLiabilityDetails;
    for (final BDMExternalLiabilitySearchDetails extLiability : liabilitiesList.dtls) {
      bdmLiabilityDetails = new BDMExternalLiabilityDetails();
      bdmLiabilityDetails.assign(extLiability);

      String repaidStatus = "";
      final BDMExternalLiabilityKey bdmExtLbyKey =
        new BDMExternalLiabilityKey();
      bdmExtLbyKey.externalLiabilityID = extLiability.externalLiabilityID;

      // get the last non-cancelled ILI that was associated with this liability
      // to get the end date
      if (bdmLiabilityDetails.outstandingAmount.isZero()) {

        final BDMTotalAllocationByExtLiabilityKey bdmTotalAllocDateKey =
          new BDMTotalAllocationByExtLiabilityKey();
        bdmTotalAllocDateKey.externalLiabilityID =
          extLiability.externalLiabilityID;
        bdmTotalAllocDateKey.iliStatus = ILISTATUS.CANCELLED;
        final DateStruct endDate =
          BDMInstructionLineItemDAFactory.newInstance()
            .getBDMMaxAllocationDateByExtLiability(bdmTotalAllocDateKey);

        bdmLiabilityDetails.endDate = endDate.date;

        repaidStatus = CuramConst.gkSpace + CuramConst.gkDash
          + CuramConst.gkSpace + BPOCASETAB.INF_FULLY_REPAID_TEXT
            .getMessageText(TransactionInfo.getProgramLocale());
      }

      final LocalisableString liabilityStatus =
        new LocalisableString(BDMEXTERNALLIABILITY.LIABILITY_STATUS);
      liabilityStatus
        .arg(new CodeTableItemIdentifier(BDMEXTERNALLIABILITYSTATUS.TABLENAME,
          extLiability.liabilityStatusCode));
      liabilityStatus.arg(repaidStatus);
      bdmLiabilityDetails.liabilityStatusText =
        liabilityStatus.toClientFormattedText();

      // modify the action indicator buttons
      if (extLiability.liabilityStatusCode
        .equals(BDMEXTERNALLIABILITYSTATUS.ACTIVE)) {
        bdmLiabilityDetails.showDeactivateIcon = true;
      } else if (extLiability.liabilityStatusCode
        .equals(BDMEXTERNALLIABILITYSTATUS.INACTIVE)) {
        bdmLiabilityDetails.showActivateIcon = true;
      }
      // if no deductions applied, show delete key
      bdmExtLbyKey.externalLiabilityID = extLiability.externalLiabilityID;
      final Count count = BDMCaseDeductionItemFactory.newInstance()
        .countDedILIsByExtLiability(bdmExtLbyKey);
      if (count.numberOfRecords == 0) {
        bdmLiabilityDetails.showDeleteIcon = true;
      }
      bdmLiabilityDetails.showEditIcon = true;

      // if it is deleted or a system managed liability, do not show any action
      // items
      if (!extLiability.recordStatusCode.equals(RECORDSTATUS.NORMAL)
        || !extLiability.liabilityTypeCode
          .equals(BDMEXTERNALLIABILITYTYPE.CRA_SET_OFF)) {
        bdmLiabilityDetails.showActivateIcon = false;
        bdmLiabilityDetails.showDeactivateIcon = false;
        bdmLiabilityDetails.showDeleteIcon = false;
        bdmLiabilityDetails.showEditIcon = false;
      }

      bdmLiabilitiesList.dtls.add(bdmLiabilityDetails);
    }

    return bdmLiabilitiesList;
  }

  /**
   * Temporarily save data from the first step of the external liability wizard
   */
  @Override
  public BDMExternalLbyWizardIDKey
    createLbySaveStep1Data(final BDMExternalLbyWizardSaveStep1Details key)
      throws AppException, InformationalException {

    if (key.liabilityAmount.isZero()) {
      throw new AppException(BDMEXTERNALLIABILITY.ERR_NO_LIABILITY_AMOUNT);
    }

    if (key.liabilityAmount.isNegative()) {
      throw new AppException(BDMEXTERNALLIABILITY.ERR_NEGATIVE_AMOUNT);
    }

    final WizardPersistentState wizardPersistanceObj =
      new WizardPersistentState();
    final BDMExternalLbyWizardIDKey bdmExternalLbyWizardIDKey =
      new BDMExternalLbyWizardIDKey();
    if (key.wizardStateID == 0) {
      final long wizardStateID = wizardPersistanceObj.create(key);
      bdmExternalLbyWizardIDKey.wizardStateID = wizardStateID;
    } else {
      final BDMExternalLbyWizardSaveStep1Details details =
        (BDMExternalLbyWizardSaveStep1Details) wizardPersistanceObj
          .read(key.wizardStateID);
      key.dtls.assign(details.dtls);
      wizardPersistanceObj.modify(key.wizardStateID, key);
      bdmExternalLbyWizardIDKey.wizardStateID = key.wizardStateID;
    }

    return bdmExternalLbyWizardIDKey;
  }

  /**
   * List any benefit cases and underpayment cases associated with the concern
   * role
   */
  @Override
  public BDMExternalLiabilityBenefitDetailsList
    listBenefitCases(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    // get details from step 1
    final WizardPersistentState wizardPersistanceObj =
      new WizardPersistentState();
    final BDMExternalLbyWizardSaveStep1Details step1Details =
      (BDMExternalLbyWizardSaveStep1Details) wizardPersistanceObj
        .read(wizardStateID.wizardStateID);

    final BDMRecordStatusExternalLiabilityKey bdmDeductionKey =
      new BDMRecordStatusExternalLiabilityKey();
    bdmDeductionKey.externalLiabilityType = step1Details.extLbyTypeCode;
    bdmDeductionKey.recordStatus = RECORDSTATUS.NORMAL;
    final BDMDeductionDetails bdmDeductionDetails =
      BDMDeductionFactory.newInstance()
        .readActiveDeductionLinkedToExternalLiabilityType(bdmDeductionKey);

    return getBenefitCasesForConcernRole(step1Details.concernRoleID,
      bdmDeductionDetails.deductionType);
  }

  /**
   * Gets all non closed benefit cases given a concern role ID
   *
   * @param bdmEligBenefitsList
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected BDMExternalLiabilityBenefitDetailsList
    getBenefitCasesForConcernRole(final long concernRoleID,
      final String deductionType)
      throws AppException, InformationalException {

    // initialize return struct
    final BDMExternalLiabilityBenefitDetailsList bdmEligBenefitsList =
      new BDMExternalLiabilityBenefitDetailsList();

    // look for any benefit or payment correction cases that are not closed
    final BDMSearchEligBenefitsForExternalLbyKey pdKey =
      new BDMSearchEligBenefitsForExternalLbyKey();
    pdKey.caseStatus = CASESTATUS.CLOSED;
    pdKey.concernRoleID = concernRoleID;
    pdKey.deductionType = deductionType;
    final BDMSearchEligBenefitsForExternalLbyDtlsList eligBenefitsList =
      BDMProductDeliveryDAFactory.newInstance()
        .searchEligBenefitsForExternalLby(pdKey);

    BDMExternalLiabilityBenefitDetails bdmBenefitDetails;
    for (final BDMSearchEligBenefitsForExternalLbyDtls benefitDetails : eligBenefitsList.dtls) {

      bdmBenefitDetails = new BDMExternalLiabilityBenefitDetails();

      // create case display string
      bdmBenefitDetails.caseDisplay =
        CodeTable.getOneItem(PRODUCTTYPE.TABLENAME,
          benefitDetails.productType) + " " + benefitDetails.caseReference;
      bdmBenefitDetails.caseID = benefitDetails.caseID;

      bdmEligBenefitsList.dtls.add(bdmBenefitDetails);

    }
    return bdmEligBenefitsList;
  }

  /**
   * gather details for the deductions to be created
   */
  @Override
  public BDMExternalLiabilityKey createLbySaveStep2Data(
    final WizardStateID wizardStateID,
    final BDMExternalLbyWizardSaveStep2Details step2Details)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    validateDeductionDetails(step2Details.selBenefitIDs,
      step2Details.deductionAmount, step2Details.deductionRate,
      informationalManager, step2Details.startDate, Date.getCurrentDate(),
      true);

    // get details from step 1
    final WizardPersistentState wizardPersistanceObj =
      new WizardPersistentState();
    final BDMExternalLbyWizardSaveStep1Details step1Details =
      (BDMExternalLbyWizardSaveStep1Details) wizardPersistanceObj
        .read(wizardStateID.wizardStateID);

    validateRegisterLiability(step1Details, informationalManager);

    if (informationalManager.operationHasInformationals()) {

      informationalManager.failOperation();

    }

    // create the liability
    final BDMRegisterExternalLbyDetails lbyDetails =
      new BDMRegisterExternalLbyDetails();
    lbyDetails.assign(step1Details);
    final BDMExternalLiabilityKey bdmExtLbyKey =
      registerLiability(lbyDetails);

    // create the deductions
    registerDeductions(bdmExtLbyKey, step2Details);

    // activate the liability
    final BDMExternalLiabilityDtls liabilityDtls =
      BDMExternalLiabilityFactory.newInstance().read(bdmExtLbyKey);

    final VersionNumberDetails versionNoDetails = new VersionNumberDetails();
    versionNoDetails.versionNo = liabilityDtls.versionNo;
    activateLiability(bdmExtLbyKey, versionNoDetails);

    return bdmExtLbyKey;
  }

  /**
   * Creates the deductions for each selected case
   *
   * @param bdmExtLbyKey
   * @param step2Details
   * @throws AppException
   * @throws InformationalException
   */
  private void registerDeductions(final BDMExternalLiabilityKey bdmExtLbyKey,
    final BDMExternalLbyWizardSaveStep2Details step2Details)
    throws AppException, InformationalException {

    final BDMExternalLiabilityDtls liabilityDtls =
      BDMExternalLiabilityFactory.newInstance().read(bdmExtLbyKey);

    // set create details
    final BDMParticipantDeductionCreateDetails createDeductionDetails =
      new BDMParticipantDeductionCreateDetails();
    createDeductionDetails.amount = step2Details.deductionAmount;
    createDeductionDetails.rate = step2Details.deductionRate;
    createDeductionDetails.externalLiabilityID =
      bdmExtLbyKey.externalLiabilityID;
    createDeductionDetails.selBenefitIDs = step2Details.selBenefitIDs;
    createDeductionDetails.concernRoleID = liabilityDtls.concernRoleID;
    createDeductionDetails.startDate = step2Details.startDate;

    final BDMRecordStatusExternalLiabilityKey bdmDeductionKey =
      new BDMRecordStatusExternalLiabilityKey();
    bdmDeductionKey.externalLiabilityType = liabilityDtls.liabilityTypeCode;
    bdmDeductionKey.recordStatus = RECORDSTATUS.NORMAL;

    // get the deduction associated with the liability type
    final BDMDeductionDetails bdmDeductionDetails =
      BDMDeductionFactory.newInstance()
        .readActiveDeductionLinkedToExternalLiabilityType(bdmDeductionKey);

    final DeductionKey deductionKey = new DeductionKey();
    deductionKey.deductionID = bdmDeductionDetails.deductionID;
    final DeductionDtls deductionDtls =
      DeductionFactory.newInstance().read(deductionKey);

    createDeductionDetails.category = deductionDtls.category;
    createDeductionDetails.deductionID = deductionDtls.deductionID;

    maintainParticipantDeductionObj
      .createParticipantDeduction(createDeductionDetails);

  }

  /**
   * Validate the details that were provided in step 1. If there are any errors,
   * fail the operation
   *
   * @param informationalManager
   * @param details
   * @throws InformationalException
   */
  protected void validateRegisterLiability(

    final BDMExternalLbyWizardSaveStep1Details details,
    final InformationalManager informationalManager)
    throws InformationalException {

    if (details.liabilityAmount.isZero()) {
      informationalManager.addInformationalMsg(
        new AppException(BDMEXTERNALLIABILITY.ERR_NO_AMOUNT),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    if (details.liabilityAmount.isNegative()) {
      informationalManager.addInformationalMsg(
        new AppException(BDMEXTERNALLIABILITY.ERR_NEGATIVE_AMOUNT),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    if (StringUtil.isNullOrEmpty(details.extLbyTypeCode)) {
      informationalManager.addInformationalMsg(
        new AppException(BDMEXTERNALLIABILITY.ERR_NO_LIABILITY_TYPE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
  }

  /**
   * validates deduction details
   *
   * @param selBenefitIDs
   * @param deductionAmount
   * @param deductionRate
   * @param informationalManager
   * @throws InformationalException
   */
  public void validateDeductionDetails(final String selBenefitIDs,
    final Money deductionAmount, final double deductionRate,
    final InformationalManager informationalManager,
    final Date deductionStartDate, final Date lbyStartDate,
    final boolean isCreate) throws InformationalException {

    // Deduction start date cannot be before liability start
    if (deductionStartDate.before(lbyStartDate)) {
      if (isCreate) {
        informationalManager.addInformationalMsg(
          new AppException(BDMFINANCIALS.ERR_CRA_SETOFF_DEDUCTION_START_DATE),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      } else {
        informationalManager.addInformationalMsg(
          new AppException(
            BDMFINANCIALS.ERR_CRA_SETOFF_DEDUCTION_START_DATE_MODIFY),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
      }
    }
    // validate that at least one PDC has been selected
    if (selBenefitIDs.isEmpty()) {
      informationalManager.addInformationalMsg(
        new AppException(BDMEXTERNALLIABILITY.ERR_NO_PDC_SELECTED),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }

    // validate that one of amount or rate has been entered, and not both
    if (!deductionAmount.isZero() && deductionRate != 0) {
      informationalManager.addInformationalMsg(
        new AppException(
          BDMEXTERNALLIABILITY.ERR_DEDUCTION_AMOUNT_RATE_SELECTED),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }

    if (deductionAmount.isZero() && deductionRate == 0) {
      informationalManager.addInformationalMsg(
        new AppException(
          BDMEXTERNALLIABILITY.ERR_DEDUCTION_AMOUNT_RATE_SELECTED),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }

    // validate that deduction rate is within a valid percentage range
    if (deductionRate < 0 || deductionRate > 100) {
      informationalManager.addInformationalMsg(new AppException(
        BPOMAINTAINDEDUCTIONITEMS.ERR_DEDUCTION_FV_PERCENTAGE_OUTSIDE_RANGE),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
  }

  /**
   * Registers the external liability
   *
   * @param lbyDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMExternalLiabilityKey
    registerLiability(final BDMRegisterExternalLbyDetails lbyDetails)
      throws AppException, InformationalException {

    final BDMExternalLiabilityDtls bdmLbyDetails =
      new BDMExternalLiabilityDtls();

    bdmLbyDetails.assign(lbyDetails);

    // set details
    bdmLbyDetails.outstandingAmount = lbyDetails.liabilityAmount;
    bdmLbyDetails.creationDate = Date.getCurrentDate();
    bdmLbyDetails.recordStatusCode = RECORDSTATUS.NORMAL;
    bdmLbyDetails.liabilityStatusCode = BDMEXTERNALLIABILITYSTATUS.INACTIVE;

    // create the reference id
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName =
      BDMExternalLiabilityConstants.kExternalLiabilityKeySet;

    bdmLbyDetails.sysGenRefNumber = String.valueOf(
      UniqueIDFactory.newInstance().getNextIDFromKeySet(uniqueIDKeySet));

    if (!bdmLbyDetails.liabilityTypeCode
      .equals(BDMEXTERNALLIABILITYTYPE.CRA_SET_OFF)) {

      final BDMCheckLiabilityExistDetails checkLbyDtls =
        new BDMCheckLiabilityExistDetails();
      checkLbyDtls.assign(lbyDetails);

      final BDMExternalLiabilityKey liabilityKey =
        getLiabilityByExtRefNumber(checkLbyDtls);

      if (liabilityKey.externalLiabilityID != 0L) {
        throw new AppException(BDMEXTERNALLIABILITY.ERR_DOJ_DEDUCTION_EXISTS);
      }
    }

    bdmLbyDetails.externalLiabilityID = UniqueID.nextUniqueID();

    BDMExternalLiabilityFactory.newInstance().insert(bdmLbyDetails);

    final BDMExternalLiabilityKey bdmExtLbyKey =
      new BDMExternalLiabilityKey();
    bdmExtLbyKey.externalLiabilityID = bdmLbyDetails.externalLiabilityID;

    return bdmExtLbyKey;

  }

  /**
   * Checks if a liability already exists based off the external reference
   * number
   */
  @Override
  public BDMExternalLiabilityKey
    getLiabilityByExtRefNumber(final BDMCheckLiabilityExistDetails lbyDetails)
      throws AppException, InformationalException {

    final BDMExternalLiabilityConcernRoleTypeKey concernRoleTypeKey =
      new BDMExternalLiabilityConcernRoleTypeKey();
    concernRoleTypeKey.assign(lbyDetails);

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMExternalLiabilityDtls liabilityDtls =
      BDMExternalLiabilityFactory.newInstance()
        .readByConcernRoleTypeDetails(nfIndicator, concernRoleTypeKey);

    final BDMExternalLiabilityKey key = new BDMExternalLiabilityKey();

    if (!nfIndicator.isNotFound()
      && liabilityDtls.recordStatusCode.equals(RECORDSTATUS.NORMAL)) {
      key.externalLiabilityID = liabilityDtls.externalLiabilityID;
    }

    return key;

  }

  /**
   * Activates the liability
   */
  @Override
  public void activateLiability(final BDMExternalLiabilityKey key,
    final VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException {

    final BDMExternalLiability bdmExternalLiabilityObj =
      BDMExternalLiabilityFactory.newInstance();

    final BDMExternalLiabilityDtls dtls = bdmExternalLiabilityObj.read(key);

    // check to make sure liability is not already active and not-deleted
    if (dtls.liabilityStatusCode.equals(BDMEXTERNALLIABILITYSTATUS.ACTIVE)) {
      throw new AppException(BDMEXTERNALLIABILITY.ERR_ACTIVATE_ACTIVE_LBY);
    }
    if (dtls.recordStatusCode.equals(RECORDSTATUS.CANCELLED)) {
      throw new AppException(BDMEXTERNALLIABILITY.ERR_ACTIVATE_DELETED_LBY);
    }

    dtls.liabilityStatusCode = BDMEXTERNALLIABILITYSTATUS.ACTIVE;
    dtls.versionNo = versionNumberDetails.versionNo;

    bdmExternalLiabilityObj.modify(key, dtls);

    // look for participant deduction linked to the ext lby
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMPDIExternalLiabilityKey participantDeductionKey =
      new BDMPDIExternalLiabilityKey();
    participantDeductionKey.externalLiabilityID = key.externalLiabilityID;
    final BDMParticipantDeductionItemKey bdmPDIKey =
      BDMParticipantDeductionItemFactory.newInstance()
        .readByExternalLiability(nfIndicator, participantDeductionKey);

    // activate participant deduction
    if (!nfIndicator.isNotFound()) {
      maintainParticipantDeductionObj.activateParticipantDeduction(bdmPDIKey);
    }
  }

  /**
   * Deactivates the liability
   */
  @Override
  public void deactivateLiability(final BDMExternalLiabilityKey key,
    final VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException {

    final BDMExternalLiability bdmExternalLiabilityObj =
      BDMExternalLiabilityFactory.newInstance();

    final BDMExternalLiabilityDtls dtls = bdmExternalLiabilityObj.read(key);

    // check to make sure liability is not already inactive and not-deleted
    if (dtls.liabilityStatusCode
      .equals(BDMEXTERNALLIABILITYSTATUS.INACTIVE)) {
      throw new AppException(
        BDMEXTERNALLIABILITY.ERR_DEACTIVATE_INACTIVE_LBY);
    }
    if (dtls.recordStatusCode.equals(RECORDSTATUS.CANCELLED)) {
      throw new AppException(BDMEXTERNALLIABILITY.ERR_DEACTIVATE_DELETED_LBY);
    }

    dtls.liabilityStatusCode = BDMEXTERNALLIABILITYSTATUS.INACTIVE;
    dtls.versionNo = versionNumberDetails.versionNo;

    bdmExternalLiabilityObj.modify(key, dtls);

    // look for participant deduction linked to the ext lby
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMPDIExternalLiabilityKey participantDeductionKey =
      new BDMPDIExternalLiabilityKey();
    participantDeductionKey.externalLiabilityID = key.externalLiabilityID;
    final BDMParticipantDeductionItemKey bdmPDIKey =
      BDMParticipantDeductionItemFactory.newInstance()
        .readByExternalLiability(nfIndicator, participantDeductionKey);

    // activate participant deduction
    if (!nfIndicator.isNotFound()) {
      maintainParticipantDeductionObj
        .deactivateParticipantDeduction(bdmPDIKey);
    }

  }

  /**
   * Deletes the liability
   */
  @Override
  public void deleteLiability(final BDMExternalLiabilityKey key,
    final VersionNumberDetails versionNumberDetails)
    throws AppException, InformationalException {

    // checks to see if the deductions associated with the ext lby have already
    // been processed
    final Count count = BDMCaseDeductionItemFactory.newInstance()
      .countDedILIsByExtLiability(key);

    final BDMExternalLiability bdmExternalLiabilityObj =
      BDMExternalLiabilityFactory.newInstance();

    final BDMExternalLiabilityDtls dtls = bdmExternalLiabilityObj.read(key);

    if (count.numberOfRecords != 0) {
      throw new AppException(
        BDMEXTERNALLIABILITY.ERR_DELETED_LBY_WITH_DEDUCTIONS);
    }
    if (dtls.recordStatusCode.equals(RECORDSTATUS.CANCELLED)) {
      throw new AppException(BDMEXTERNALLIABILITY.ERR_DELETE_DELETED_LBY);
    }
    dtls.recordStatusCode = RECORDSTATUS.CANCELLED;
    dtls.versionNo = versionNumberDetails.versionNo;

    bdmExternalLiabilityObj.modify(key, dtls);

    // look for participant deduction linked to the ext lby
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMPDIExternalLiabilityKey participantDeductionKey =
      new BDMPDIExternalLiabilityKey();
    participantDeductionKey.externalLiabilityID = key.externalLiabilityID;
    final BDMParticipantDeductionItemKey bdmPDIKey =
      BDMParticipantDeductionItemFactory.newInstance()
        .readByExternalLiability(nfIndicator, participantDeductionKey);

    // activate participant deduction
    if (!nfIndicator.isNotFound()) {
      maintainParticipantDeductionObj
        .deactivateParticipantDeduction(bdmPDIKey);
    }

  }

  /**
   * Reads details needed for edit screen for liability
   */
  @Override
  public BDMReadLbyDetailsForEdit
    readLbyDetailsForEdit(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    // initialize return struct
    final BDMReadLbyDetailsForEdit editDetails =
      new BDMReadLbyDetailsForEdit();

    final BDMExternalLiabilityDtls dtls =
      BDMExternalLiabilityFactory.newInstance().read(key);

    editDetails.assign(dtls);

    // retrieve participant deduction details
    final BDMPDIExternalLiabilityKey bdmPDIExtLbyKey =
      new BDMPDIExternalLiabilityKey();
    bdmPDIExtLbyKey.externalLiabilityID = key.externalLiabilityID;
    final BDMParticipantDeductionItem pdiObj =
      BDMParticipantDeductionItemFactory.newInstance();

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMParticipantDeductionItemKey pdiKey =
      pdiObj.readByExternalLiability(bdmPDIExtLbyKey);

    final BDMParticipantDeductionItemDtls pdiDtls =
      pdiObj.read(nfIndicator, pdiKey);

    if (!nfIndicator.isNotFound()) {
      editDetails.deductionAmount = pdiDtls.amount;
      editDetails.deductionRate = pdiDtls.rate;
      editDetails.startDate = pdiDtls.startDate;
    }

    return editDetails;
  }

  /**
   * Modifies the liability amount, and if resetOutstandingAmountInd is true,
   * reset the outstanding amount
   */
  @Override
  public void modifyLiabilityDetails(
    final BDMModifyLiabilityDetails modifyDtls,
    final boolean resetOutstandingAmountInd)
    throws AppException, InformationalException {

    final BDMExternalLiability bdmExternalLiabilityObj =
      BDMExternalLiabilityFactory.newInstance();
    final BDMExternalLiabilityKey bdmExtLbyKey =
      new BDMExternalLiabilityKey();
    bdmExtLbyKey.externalLiabilityID = modifyDtls.externalLiabilityID;
    final BDMExternalLiabilityDtls dtls =
      bdmExternalLiabilityObj.read(bdmExtLbyKey);

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (modifyDtls.liabilityAmount.isNegative()) {
      informationalManager.addInformationalMsg(
        new AppException(BDMEXTERNALLIABILITY.ERR_NEGATIVE_AMOUNT),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    if (dtls.recordStatusCode.equals(RECORDSTATUS.CANCELLED)) {
      informationalManager.addInformationalMsg(
        new AppException(BDMEXTERNALLIABILITY.ERR_MODIFY_DELETED_LBY),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }

    validateDeductionDetails(modifyDtls.selBenefitIDs,
      modifyDtls.deductionAmount, modifyDtls.deductionRate,
      informationalManager, modifyDtls.startDate, dtls.creationDate, false);

    if (informationalManager.operationHasInformationals()) {

      informationalManager.failOperation();

    }

    // if the liability amount has changed, then reset the outstanding amount
    if (resetOutstandingAmountInd
      && !dtls.liabilityAmount.equals(modifyDtls.liabilityAmount)) {
      dtls.outstandingAmount = modifyDtls.liabilityAmount;
    }

    dtls.versionNo = modifyDtls.versionNo;
    dtls.comments = modifyDtls.comments;
    dtls.liabilityAmount = modifyDtls.liabilityAmount;

    bdmExternalLiabilityObj.modify(bdmExtLbyKey, dtls);

    // look for participant deduction linked to the ext lby
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMPDIExternalLiabilityKey participantDeductionKey =
      new BDMPDIExternalLiabilityKey();
    participantDeductionKey.externalLiabilityID = dtls.externalLiabilityID;
    final BDMParticipantDeductionItemKey bdmPDIKey =
      BDMParticipantDeductionItemFactory.newInstance()
        .readByExternalLiability(nfIndicator, participantDeductionKey);

    // modify liability
    if (!nfIndicator.isNotFound()) {
      final BDMParticipantDeductionModifyDetails modifyDeductionDetails =
        new BDMParticipantDeductionModifyDetails();

      modifyDeductionDetails.amount = modifyDtls.deductionAmount;
      modifyDeductionDetails.rate = modifyDtls.deductionRate;
      modifyDeductionDetails.selBenefitIDs = modifyDtls.selBenefitIDs;
      modifyDeductionDetails.startDate = modifyDtls.startDate;
      modifyDeductionDetails.bdmParticipantDeductionItemID =
        bdmPDIKey.bdmParticipantDeductionItemID;

      maintainParticipantDeductionObj
        .modifyParticipantDeduction(modifyDeductionDetails);
    }

  }

  /**
   * Displays list of changes to outstanding amount for a given liability
   */
  @Override
  public BDMExternalLiabilityHistoryDetailsList
    listLiabilityHistory(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    final BDMExternalLiabilityHistoryDetailsList historyList =
      new BDMExternalLiabilityHistoryDetailsList();

    final BDMExternalLiabilityHistorySearchKey historyKey =
      new BDMExternalLiabilityHistorySearchKey();
    historyKey.externalLiabilityID = key.externalLiabilityID;

    final BDMExternalLiabilityHistorySearchDtlsList historyResultList =
      BDMExternalLiabilityHistoryFactory.newInstance()
        .readExternalLiabilityHistory(historyKey);

    BDMExternalLiabilityHistorySearchDtls historyResult;
    BDMExternalLiabilityHistoryDetails historyDetails;
    final UsersKey userKey = new UsersKey();
    final UserAccess userAccessObj = UserAccessFactory.newInstance();
    UserFullname userFullName;
    Calendar calendar;
    // list is sorted in ascending order, we will process it in descending order
    for (int i = historyResultList.dtls.size() - 1; i >= 0; i--) {
      historyResult = historyResultList.dtls.get(i);
      historyDetails = new BDMExternalLiabilityHistoryDetails();
      historyDetails.assign(historyResult);

      userKey.userName = historyResult.userName;

      userFullName = userAccessObj.getFullName(userKey);
      historyDetails.userFullName = userFullName.fullname;

      calendar = historyResult.creationDateTime.getCalendar();
      historyDetails.modifyDate = new Date(calendar);

      historyList.dtls.add(historyDetails);
    }

    return historyList;
  }

  /**
   * Reads the version number for the liability
   */
  @Override
  public VersionNumberDetails
    readLiabilityVersion(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    final VersionNumberDetails versionNumberDetails =
      new VersionNumberDetails();

    final BDMExternalLiabilityVersionNumber readLiabilityVersion =
      BDMExternalLiabilityFactory.newInstance().readLiabilityVersion(key);
    versionNumberDetails.versionNo = readLiabilityVersion.versionNo;
    return versionNumberDetails;
  }

  /**
   * Determines if the FC amount needs to be reduced to the value of the
   * outstanding amount if the external liability outstanding amount is less
   * than the current FC amount
   */
  @Override
  public void determineDeductionILIAmount(
    final CaseDeductionItemDtls caseDeductionItemDtls,
    final FCAmount fcAmount) throws AppException, InformationalException {

    final BDMCaseDeductionItemKey bdmCDIKey = new BDMCaseDeductionItemKey();
    bdmCDIKey.caseDeductionItemID = caseDeductionItemDtls.caseDeductionItemID;

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final BDMCaseDeductionItemDtls bdmCDIDtls =
      BDMCaseDeductionItemFactory.newInstance().read(nfIndicator, bdmCDIKey);

    // if the deduction is linked to an external liability
    if (!nfIndicator.isNotFound() && bdmCDIDtls.externalLiabilityID != 0) {

      final BDMExternalLiabilityKey extLbyKey = new BDMExternalLiabilityKey();
      extLbyKey.externalLiabilityID = bdmCDIDtls.externalLiabilityID;

      final BDMExternalLiabilityDtls liabilityDtls =
        BDMExternalLiabilityFactory.newInstance().read(extLbyKey);

      // if the outstanding amount is less than the FC amount, set the FC
      // amount
      // to the remaining outstanding amount on the liability
      if (liabilityDtls.outstandingAmount.compareTo(fcAmount.amount) == -1) {
        fcAmount.amount = liabilityDtls.outstandingAmount;

        if (fcAmount.amount.isNegative()) {
          fcAmount.amount = Money.kZeroMoney;
        }
      }
    }

  }

  /**
   * Reduce the external liability amount based off the FC amount
   */
  @Override
  public void postDeductionReduceOutstandingBalanceAmount(
    final CaseDeductionItemDtls caseDeductionItemDtls, final Amount iliAmount)
    throws AppException, InformationalException {

    final BDMCaseDeductionItemKey bdmCDIKey = new BDMCaseDeductionItemKey();
    bdmCDIKey.caseDeductionItemID = caseDeductionItemDtls.caseDeductionItemID;

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final BDMCaseDeductionItemDtls bdmCDIDtls =
      BDMCaseDeductionItemFactory.newInstance().read(nfIndicator, bdmCDIKey);

    // if the deduction is linked to an external liability
    if (!nfIndicator.isNotFound() && bdmCDIDtls.externalLiabilityID != 0) {
      final BDMExternalLiability bdmExternalLiabilityObj =
        BDMExternalLiabilityFactory.newInstance();

      final BDMExternalLiabilityKey extLbyKey = new BDMExternalLiabilityKey();
      extLbyKey.externalLiabilityID = bdmCDIDtls.externalLiabilityID;

      final BDMExternalLiabilityDtls liabilityDtls =
        bdmExternalLiabilityObj.read(extLbyKey);

      // set the outstanding amount less the FC amount
      final double newOutstandingAmount =
        liabilityDtls.outstandingAmount.getValue()
          - iliAmount.amount.getValue();

      liabilityDtls.outstandingAmount = new Money(newOutstandingAmount);

      bdmExternalLiabilityObj.modify(extLbyKey, liabilityDtls);
    }

  }

  /**
   * lists benefit cases to be modified, and the currently selected cases
   */
  @Override
  public BDMExternalLiabilityBenefitDetailsList
    listBenefitCasesForModify(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    final BDMExternalLiabilityDtls liabilityDtls =
      BDMExternalLiabilityFactory.newInstance().read(key);

    final BDMRecordStatusExternalLiabilityKey bdmDeductionKey =
      new BDMRecordStatusExternalLiabilityKey();
    bdmDeductionKey.externalLiabilityType = liabilityDtls.liabilityTypeCode;
    bdmDeductionKey.recordStatus = RECORDSTATUS.NORMAL;
    final BDMDeductionDetails bdmDeductionDetails =
      BDMDeductionFactory.newInstance()
        .readActiveDeductionLinkedToExternalLiabilityType(bdmDeductionKey);

    // get the benefit cases for the concern role
    final BDMExternalLiabilityBenefitDetailsList benefitDetailsList =
      getBenefitCasesForConcernRole(liabilityDtls.concernRoleID,
        bdmDeductionDetails.deductionType);

    // find current linked benefits to the liability
    final BDMParticipantDeductionCaseLinkList linkedBenefits =
      maintainParticipantDeductionObj.listLinkedBenefitsByExtLby(key);

    String selBenefitIDs = "";

    // format current benefits into a tab delimited list
    for (final BDMParticipantDeductionCaseLinkDetails linkedBenefit : linkedBenefits.dtls) {
      selBenefitIDs += linkedBenefit.caseID + CuramConst.gkTabDelimiter;
    }

    selBenefitIDs = StringUtil.rtrim(selBenefitIDs);

    benefitDetailsList.selBenefitIDs = selBenefitIDs;

    return benefitDetailsList;
  }

  /**
   * Modifies a DOJ liability
   */
  @Override
  public void
    modifyDOJLiabilityDetails(final BDMModifyDOJLbyDetailsKey modifyDetails)
      throws AppException, InformationalException {

    final BDMExternalLiability externalLiabilityObj =
      BDMExternalLiabilityFactory.newInstance();

    final BDMExternalLiabilityKey key = new BDMExternalLiabilityKey();
    key.externalLiabilityID = modifyDetails.externalLiabilityID;

    final BDMExternalLiabilityDtls liabilityDtls =
      externalLiabilityObj.read(key);

    if (!liabilityDtls.recordStatusCode.equals(RECORDSTATUS.NORMAL)) {
      throw new AppException(BDMEXTERNALLIABILITY.ERR_MODIFY_DELETED_LBY);
    }

    liabilityDtls.outstandingAmount = modifyDetails.liabilityAmount;
    liabilityDtls.liabilityAmount = modifyDetails.liabilityAmount;
    liabilityDtls.comments = modifyDetails.comments;
    liabilityDtls.externalLiabilityID = modifyDetails.externalLiabilityID;
    liabilityDtls.versionNo = modifyDetails.versionNo;

    externalLiabilityObj.modify(key, liabilityDtls);

  }

  @Override
  public GenILIDOJDeductionDetails getDOJDeductionDetails(
    final CaseDeductionItemDtls caseDeductionItemDtls,
    final FinancialComponentDtls fcDtls,
    final FinCompCoverPeriod fcCoverPeriod, final FCAmount fcAmount,
    final TotalDeductionAmount totalDeductionAmount)
    throws AppException, InformationalException {

    final BDMExternalLiability bdmExternalLiabilityObj =
      BDMExternalLiabilityFactory.newInstance();
    final BDMExternalLiabilityKey bdmExternalLiabilityKey =
      new BDMExternalLiabilityKey();
    final BDMCaseDeductionItemKey bdmCDIKey = new BDMCaseDeductionItemKey();
    bdmCDIKey.caseDeductionItemID = caseDeductionItemDtls.caseDeductionItemID;
    final BDMCaseDeductionItemDtls bdmCDIDtls =
      BDMCaseDeductionItemFactory.newInstance().read(bdmCDIKey);

    final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
    final BDMDeductionKey bdmDeductionKey = new BDMDeductionKey();
    bdmDeductionKey.deductionID = caseDeductionItemDtls.deductionID;
    final BDMDeductionDetails bdmDeductionDetails =
      bdmDeductionObj.readByDeductionID(bdmDeductionKey);

    bdmExternalLiabilityKey.externalLiabilityID =
      bdmCDIDtls.externalLiabilityID;
    final DOJDeductionForILIDetails dedLbyDetails = bdmExternalLiabilityObj
      .readDOJDeductionDetails(bdmExternalLiabilityKey);
    final BDMInstructionLineItemDA bdmInstructionLineItemDA =
      BDMInstructionLineItemDAFactory.newInstance();
    final SearchAllUnLineItemsForDOJDedKey searchAllUnLineItemsForDOJDedKey =
      new SearchAllUnLineItemsForDOJDedKey();
    searchAllUnLineItemsForDOJDedKey.caseID = fcDtls.caseID;
    searchAllUnLineItemsForDOJDedKey.iliCategory =
      ILICATEGORY.PAYMENTINSTRUCTION;
    searchAllUnLineItemsForDOJDedKey.iliStatusUnprocess =
      ILISTATUS.UNPROCESSED;
    final SearchAllUnLineItemsForDOJDedDetailsList searchAllUnLineItemsForDOJDedDetailsList =
      bdmInstructionLineItemDA
        .searchAllUnLineItemsForDOJDed(searchAllUnLineItemsForDOJDedKey);
    final BDMInstructionLineItem bdmInstructionLineItemObj =
      BDMInstructionLineItemFactory.newInstance();
    final BDMInstructionLineItemKey bdmInstructionLineItemKey =
      new BDMInstructionLineItemKey();

    // Set pre DOJ indicator to all deduction ILIs.
    for (final SearchAllUnLineItemsForDOJDedDetails details : searchAllUnLineItemsForDOJDedDetailsList.dtls) {
      if (details.preDOJDeductionInd == false
        && !(BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS
          .equals(details.deductionType)
          || BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_FEES
            .equals(details.deductionType))
        && CREDITDEBIT.CREDIT.equals(details.creditDebitType)) {
        bdmInstructionLineItemKey.instructLineItemID =
          details.instructLineItemID;
        BDMInstructionLineItemDtls bdmInstructionLineItemDtls = null;
        bdmInstructionLineItemDtls =
          bdmInstructionLineItemObj.read(bdmInstructionLineItemKey);
        bdmInstructionLineItemDtls.preDOJDeductionInd = true;
        bdmInstructionLineItemDtls.versionNo = details.bILIVersionNo;
        bdmInstructionLineItemObj.modify(bdmInstructionLineItemKey,
          bdmInstructionLineItemDtls);
      }
    }

    // In rare scenarios there could be multiple deduction FCs for same nominee
    // but for different components when there are gaps in component assignment.
    // Check if there is atleast one deduction ILI related to this DOJ Liability
    // has been created for this nominee and if there is one means that we did
    // try to max out that nominee for this DOJ Case Deduction.
    boolean deductionExistsForNominee = false;
    for (final SearchAllUnLineItemsForDOJDedDetails details : searchAllUnLineItemsForDOJDedDetailsList.dtls) {
      if (details.caseNomineeID == fcDtls.caseNomineeID
        && details.externalLiabilityID == dedLbyDetails.externalLiabilityID) {
        deductionExistsForNominee = true;
        break;
      }
    }

    // The rest of scenarios when there are multiple nominees against multiple
    // components, lets calculate the deductable amount that can be applied for
    // this nominee.
    Money deductedAmount = Money.kZeroMoney;
    double deductableRatio = 0.0;
    Money deductionAmount = Money.kZeroMoney;
    if (deductionExistsForNominee == false) {
      // Get amount that was already deducted while procesing other nominee's
      // deductions for for this liability.
      for (final SearchAllUnLineItemsForDOJDedDetails details : searchAllUnLineItemsForDOJDedDetailsList.dtls) {
        if (details.externalLiabilityID == dedLbyDetails.externalLiabilityID) {
          deductedAmount =
            new Money(deductedAmount.getValue() + details.amount.getValue());
        }
      }

      // There could be multiple DOJ liabilities applied for deductions on same
      // case.
      // Each liability should be given equal weightage so that there is enough
      // reminaing amount left for others liabilities to have deductions applied
      // against them.
      // Get list of DOJ Arrear deduction FCs that could be eligible for pay
      // run. Not an accurate way, but since we do have the controller continue
      // with this way.
      if (BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS
        .equals(bdmDeductionDetails.deductionType)) {
        final BDMFinancialComponentDA bdmFinancialComponentDA =
          BDMFinancialComponentDAFactory.newInstance();
        final SearchActiveDOJForPayRunKey searchActiveDOJForPayRunKey =
          new SearchActiveDOJForPayRunKey();
        searchActiveDOJForPayRunKey.caseID = fcDtls.caseID;
        searchActiveDOJForPayRunKey.finCompStatusCode =
          FINCOMPONENTSTATUS.LIVE;
        searchActiveDOJForPayRunKey.iliStatusUnprocess =
          ILISTATUS.UNPROCESSED;
        searchActiveDOJForPayRunKey.liabilityTypeDOJArrears =
          BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS;
        BDMExternalLiabilityDtls bdmExternalLiabilityDtls = null;
        final BDMLiabilityKeyList bdmLiabilityKeyList =
          bdmFinancialComponentDA
            .searchActiveDOJForPayRun(searchActiveDOJForPayRunKey);
        Money dojLbysTotal = Money.kZeroMoney;
        for (final BDMLiabilityKey bdmLiabilityKey : bdmLiabilityKeyList.dtls) {
          bdmExternalLiabilityKey.externalLiabilityID =
            bdmLiabilityKey.externalLiabilityID;
          bdmExternalLiabilityDtls =
            bdmExternalLiabilityObj.read(bdmExternalLiabilityKey);
          dojLbysTotal = new Money(dojLbysTotal.getValue()
            + bdmExternalLiabilityDtls.outstandingAmount.getValue());
          for (final SearchAllUnLineItemsForDOJDedDetails details : searchAllUnLineItemsForDOJDedDetailsList.dtls) {
            if (details.externalLiabilityID == bdmLiabilityKey.externalLiabilityID) {
              dojLbysTotal = new Money(
                dojLbysTotal.getValue() + details.amount.getValue());
            }
          }
        }
        // calculate the ratio of this liability. Add already deducted amount so
        // that we get the oustanding amount that existed before we started this
        // transaction.
        final Money prePayRunLbyOustandingAmount =
          new Money(dedLbyDetails.outstandingAmount.getValue()
            + deductedAmount.getValue());

        if (!dojLbysTotal.isZero()) {
          deductableRatio =
            prePayRunLbyOustandingAmount.getValue() / dojLbysTotal.getValue();
        }

        // Get total payment amount from all nominees.
        Money paymentTotal = Money.kZeroMoney;

        for (final SearchAllUnLineItemsForDOJDedDetails details : searchAllUnLineItemsForDOJDedDetailsList.dtls) {
          if (!(ILITYPE.DEDUCTIONITEM.equals(details.instructLineItemType)
            || ILITYPE.TAXPAYMENT.equals(details.instructLineItemType))) {
            paymentTotal =
              new Money(paymentTotal.getValue() + details.amount.getValue());
          }
        }

        final Money paymentTotalByRatio =
          new Money(paymentTotal.getValue() * deductableRatio);
        if (paymentTotalByRatio.getValue() > 0) {
          // If minimum payment amount (debtor fixed amount) exists then use
          // that.
          if (dedLbyDetails.deductionMinPayAmount.getValue() > 0) {
            deductionAmount = new Money(paymentTotalByRatio.getValue()
              - dedLbyDetails.deductionMinPayAmount.getValue());
            if (deductionAmount.isNegative()) {
              deductionAmount = Money.kZeroMoney;
            }
          } else {
            final Money deductionByAmount = dedLbyDetails.deductionAmount;
            final Money deductionByRate =
              new Money(paymentTotalByRatio.getValue()
                * dedLbyDetails.deductionRate / 100);
            // If dojDeductionAmount exist then get minimum of
            // dojDeductionAmount
            // and dojDeductionRate
            if (deductionByAmount.getValue() > 0) {
              if (deductionByAmount.getValue() < deductionByRate.getValue()) {
                deductionAmount = new Money(deductionByAmount.getValue());
              } else {
                deductionAmount = new Money(deductionByRate.getValue());
              }
            } else {
              deductionAmount = new Money(deductionByRate.getValue());
            }
          }
        }

        if (deductionAmount.getValue() > prePayRunLbyOustandingAmount
          .getValue()) {
          deductionAmount = prePayRunLbyOustandingAmount;
        }
      } else {
        deductionAmount = dedLbyDetails.liabilityAmount;
      }
      // Is less than DOJ Deduction Threshold. Do not set minimum deduction
      // amount on Deduction entity as there is still amount reduction
      // further.
      final String minDOJDeductionThresholdAmountStr =
        Configuration.getProperty(
          EnvVars.BDM_ENV_FINANCIAL_DOJMINIMUM_THRESHOLD_DEDUCTION_AMOUNT);
      final double minDOJDeductionThresholdAmount =
        Double.parseDouble(minDOJDeductionThresholdAmountStr);
      if (deductionAmount.getValue() < minDOJDeductionThresholdAmount) {
        deductionAmount = Money.kZeroMoney;
      } else {
        // Once the total deduction amount get calculated, substract the
        // amount
        // that has already been deducted for other nominees.
        deductionAmount =
          new Money(deductionAmount.getValue() - deductedAmount.getValue());
        // Get minimum of deduction amount and liability of remaining amount.
        if (deductionAmount.getValue() > dedLbyDetails.outstandingAmount
          .getValue()) {
          deductionAmount = dedLbyDetails.outstandingAmount;
        }
        // Reduce the amount further down based on remaining deductable amount
        // for this nominee.
        final MaintainInstructionLineItem maintainInstructionLineItemObj =
          MaintainInstructionLineItemFactory.newInstance();

        final Amount deductableAmount = maintainInstructionLineItemObj
          .getAmountForComponent(totalDeductionAmount.deductibleAmountList,
            new RulesObjectiveID());

        if (deductionAmount.getValue() > deductableAmount.amount.getValue()) {
          deductionAmount = deductableAmount.amount;
        }
      }
    }
    final GenILIDOJDeductionDetails genILIDOJDeductionDetails =
      new GenILIDOJDeductionDetails();

    genILIDOJDeductionDetails.fcAmount = deductionAmount;
    genILIDOJDeductionDetails.dojDebtRatio = deductableRatio;
    return genILIDOJDeductionDetails;
  }

  @Override
  public GenILITaxDeductionDetails getTaxDeductionDetails(
    final CaseDeductionItemDtls caseDeductionItemDtls,
    final FinancialComponentDtls fcDtls,
    final FinCompCoverPeriod fcCoverPeriod, final FCAmount fcAmount,
    final TotalDeductionAmount totalDeductionAmount)
    throws AppException, InformationalException {

    final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
    final BDMDeductionKey bdmDeductionKey = new BDMDeductionKey();
    bdmDeductionKey.deductionID = caseDeductionItemDtls.deductionID;
    final BDMDeductionDetails bdmDeductionDetails =
      bdmDeductionObj.readByDeductionID(bdmDeductionKey);

    final BDMInstructionLineItemDA bdmInstructionLineItemDA =
      BDMInstructionLineItemDAFactory.newInstance();
    final SearchAllUnLineItemsForTaxDedKey searchAllUnLineItemsForTaxDedKey =
      new SearchAllUnLineItemsForTaxDedKey();
    searchAllUnLineItemsForTaxDedKey.caseID = fcDtls.caseID;
    searchAllUnLineItemsForTaxDedKey.iliCategory =
      ILICATEGORY.PAYMENTINSTRUCTION;
    searchAllUnLineItemsForTaxDedKey.iliStatusUnprocess =
      ILISTATUS.UNPROCESSED;
    final GenILITaxDeductionDetails genILITaxDeductionDetails =
      new GenILITaxDeductionDetails();
    final SearchAllUnLineItemsForTaxDedDetailsList searchAllUnLineItemsForTaxDedDetailsList =
      bdmInstructionLineItemDA
        .searchBDMAllUnLineItemsForTaxDed(searchAllUnLineItemsForTaxDedKey);
    final BDMInstructionLineItem bdmInstructionLineItemObj =
      BDMInstructionLineItemFactory.newInstance();
    final BDMInstructionLineItemKey bdmInstructionLineItemKey =
      new BDMInstructionLineItemKey();
    BDMInstructionLineItemDtls bdmInstructionLineItemDtls = null;
    // Set pre-tax deduction indicator to all deduction ILIs.
    for (final SearchAllUnLineItemsForTaxDedDetails searchAllUnLineItemsForTaxDedDetails : searchAllUnLineItemsForTaxDedDetailsList.dtls) {
      if (searchAllUnLineItemsForTaxDedDetails.preTaxDeductionInd == false
        && (searchAllUnLineItemsForTaxDedDetails.instructLineItemType
          .equals(ILITYPE.DEDUCTIONITEM)
          || searchAllUnLineItemsForTaxDedDetails.instructLineItemType
            .equals(ILITYPE.TAXPAYMENT))
        && !(BDMDEDUCTIONTYPE.BASIC_FEDERAL_TAX
          .equals(searchAllUnLineItemsForTaxDedDetails.deductionType)
          || BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX
            .equals(searchAllUnLineItemsForTaxDedDetails.deductionType))) {
        bdmInstructionLineItemKey.instructLineItemID =
          searchAllUnLineItemsForTaxDedDetails.instructLineItemID;
        bdmInstructionLineItemDtls =
          bdmInstructionLineItemObj.read(bdmInstructionLineItemKey);
        bdmInstructionLineItemDtls.preTaxDeductionInd = true;
        bdmInstructionLineItemObj.modify(bdmInstructionLineItemKey,
          bdmInstructionLineItemDtls);
      }
    }

    // In rare scenario there could be multiple deduction FCs for same nominee
    // but for different components when there are gaps in component assignment.
    // Check if there is atleast one deduction ILI related to this Tax has been
    // created for this nominee and if there is one means that we did try to max
    // out of that nominee for this tax CaseDeduction.
    boolean deductionExistForNominee = false;

    for (final SearchAllUnLineItemsForTaxDedDetails searchAllUnLineItemsForTaxDedDetails : searchAllUnLineItemsForTaxDedDetailsList.dtls) {
      if (searchAllUnLineItemsForTaxDedDetails.caseNomineeID == fcDtls.caseNomineeID
        && searchAllUnLineItemsForTaxDedDetails.deductionType
          .equals(bdmDeductionDetails.deductionType)) {
        deductionExistForNominee = true;
        break;
      }
    }

    // The rest of scenarios when there are multiple nominees against multiple
    // components, lets calculate the deductable amount that can be applied for
    // this nominee.
    if (deductionExistForNominee == false) {
      // Build an array of data with nominees, component they receive, amount
      // elgible for tax etc. We cannot use OOTB deductable amount list and
      // CaseNomineeAmountList could be a better one but it doesn't exist as
      // input.
      final CaseID caseID = new CaseID();
      final RulesObjectiveID rulesObjectiveID = new RulesObjectiveID();
      final CaseNomineeTaxAmountList caseNomineeTaxAmountList =
        new CaseNomineeTaxAmountList();
      final DeductibleInd deductibleInd = null;
      // Ideally it would be better to use batch processingDate but it is
      // visible as input param.
      final Date processingDate = Date.getCurrentDate();
      final Date yearStartDate = Date.getDate(
        String.valueOf(processingDate.getCalendar().get(Calendar.YEAR))
          + "0101");
      final Date yearEndDate = Date.getDate(
        String.valueOf(processingDate.getCalendar().get(Calendar.YEAR))
          + "1231");
      for (final SearchAllUnLineItemsForTaxDedDetails searchAllUnLineItemsForTaxDedDetails : searchAllUnLineItemsForTaxDedDetailsList.dtls) {
        /*
         * For now exclude this logic since there are no componnents excluding
         * from deduction and use all un-processed payment ILIs.
         */
        /*
         * final MaintainFinancialComponent maintainFinancialComponentObj =
         * MaintainFinancialComponentFactory.newInstance();
         * caseID.caseID = fcDtls.caseID;
         * rulesObjectiveID.rulesObjectiveID =
         * searchAllUnLineItemsForTaxDedDetails.rulesObjectiveID;
         * deductibleInd =
         * maintainFinancialComponentObj.isComponentDeductible(caseID,
         * rulesObjectiveID);
         *
         * // If we add this then we also better flag such ILIs as
         * // excludeFromDeductionInd=TRUE and substract such ILIs from Gross
         * minus
         * // higher deductions for tax slip gross income amount.
         * if(deductibleInd.deductibleInd == true) {
         * // Skip components are not eligible for deductions.
         * continue;
         * }
         */
        if (searchAllUnLineItemsForTaxDedDetails.taxReportInd == true) { // go
                                                                         // through
                                                                         // payment
                                                                         // ILIs
                                                                         // first.
          // For now exclude the ILIs that belongs to previous tax year as per
          // FDD. This rule should be revisited in future and remove if
          // possible.
          if (searchAllUnLineItemsForTaxDedDetails.coverPeriodTo
            .before(yearStartDate)) {
            continue;
          }
          // Handle onetime payments including underpayments that can span for
          // multiple years by adjusting
          // its amount and start date for this year.
          if (new FrequencyPattern(
            searchAllUnLineItemsForTaxDedDetails.fcFrequency)
              .isZeroPattern()) {
            if (searchAllUnLineItemsForTaxDedDetails.coverPeriodFrom
              .before(yearStartDate)) {
              final int totalDays =
                searchAllUnLineItemsForTaxDedDetails.coverPeriodTo.subtract(
                  searchAllUnLineItemsForTaxDedDetails.coverPeriodFrom) + 1;
              final double oneTimeAmount =
                searchAllUnLineItemsForTaxDedDetails.amount.getValue();
              final double oneTimePerDay = oneTimeAmount / totalDays;
              final int excessDays = yearStartDate.subtract(
                searchAllUnLineItemsForTaxDedDetails.coverPeriodFrom);
              final double excessAmount = oneTimePerDay * excessDays;
              final double newOneTimeAmount = oneTimeAmount - excessAmount;
              searchAllUnLineItemsForTaxDedDetails.amount =
                new Money(newOneTimeAmount);
              searchAllUnLineItemsForTaxDedDetails.coverPeriodFrom =
                yearStartDate;
            }
          }
          boolean nomineeExists = false;
          for (final CaseNomineeTaxAmountDetails caseNomineeTaxAmountDetails : caseNomineeTaxAmountList.nominees) {
            if (caseNomineeTaxAmountDetails.caseNomineeID == searchAllUnLineItemsForTaxDedDetails.caseNomineeID) {
              nomineeExists = true;
              break;
            }
          }

          if (nomineeExists == false) {
            final CaseNomineeTaxAmountDetails tempCaseNomineeTaxAmountDetails =
              new CaseNomineeTaxAmountDetails();
            tempCaseNomineeTaxAmountDetails.caseNomineeID =
              searchAllUnLineItemsForTaxDedDetails.caseNomineeID;
            caseNomineeTaxAmountList.nominees
              .add(tempCaseNomineeTaxAmountDetails);
          }

          for (final CaseNomineeTaxAmountDetails caseNomineeTaxAmountDetails : caseNomineeTaxAmountList.nominees) {
            if (caseNomineeTaxAmountDetails.caseNomineeID == searchAllUnLineItemsForTaxDedDetails.caseNomineeID) {
              boolean componentExist = false;
              for (final CaseNomineeTaxComponentDetails caseNomineeTaxComponentDetails : caseNomineeTaxAmountDetails.components) {
                if (caseNomineeTaxComponentDetails.rulesObjectiveID.equals(
                  searchAllUnLineItemsForTaxDedDetails.rulesObjectiveID)) {
                  caseNomineeTaxComponentDetails.amount =
                    new Money(caseNomineeTaxComponentDetails.amount.getValue()
                      + searchAllUnLineItemsForTaxDedDetails.amount
                        .getValue());
                  componentExist = true;
                  break;
                }
              }
              if (componentExist == false) {
                final CaseNomineeTaxComponentDetails tempMNomineeTaxComponentDetails =
                  new CaseNomineeTaxComponentDetails();
                tempMNomineeTaxComponentDetails.rulesObjectiveID =
                  searchAllUnLineItemsForTaxDedDetails.rulesObjectiveID;
                tempMNomineeTaxComponentDetails.amount =
                  searchAllUnLineItemsForTaxDedDetails.amount;
                caseNomineeTaxAmountDetails.components
                  .add(tempMNomineeTaxComponentDetails);
              }
              break;
            }
          }

        }

      }
      Money totalTaxableIncome = Money.kZeroMoney;
      for (final CaseNomineeTaxAmountDetails caseNomineeTaxAmountDetails : caseNomineeTaxAmountList.nominees) {
        // If there exists deduction ILIs then set the periodicity type as Daily
        // for all components.
        // Its because we do not know against which payment ILIs the deduction
        // ILI is applied against and cannot deduct on random from recurring
        // payments.
        boolean deductionILIExists = false;
        Money deductionAmount = Money.kZeroMoney;
        for (final SearchAllUnLineItemsForTaxDedDetails searchAllUnLineItemsForTaxDedDetails : searchAllUnLineItemsForTaxDedDetailsList.dtls) {
          if (searchAllUnLineItemsForTaxDedDetails.caseNomineeID == caseNomineeTaxAmountDetails.caseNomineeID
            && ILITYPE.DEDUCTIONITEM.equals(
              searchAllUnLineItemsForTaxDedDetails.instructLineItemType)
            && !(BDMDEDUCTIONTYPE.BASIC_FEDERAL_TAX
              .equals(searchAllUnLineItemsForTaxDedDetails.deductionType)
              || BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX.equals(
                searchAllUnLineItemsForTaxDedDetails.deductionType))) {
            deductionILIExists = true;
            deductionAmount = new Money(deductionAmount.getValue()
              + searchAllUnLineItemsForTaxDedDetails.amount.getValue());

          }
        }

        for (final CaseNomineeTaxComponentDetails caseNomineeTaxComponentDetails : caseNomineeTaxAmountDetails.components) {
          int numberOfILIs = 0;
          int payDays = 0;
          String fcFrequency = "";
          int periodDuration = 0;
          int periodicityNumber = 0;
          FrequencyPattern.PatternType periodicityType;
          // If there are multiple payments for same component then this
          // contains retro payments and for simplicity use periodicity type as
          // Daily.
          for (final SearchAllUnLineItemsForTaxDedDetails searchAllUnLineItemsForTaxDedDetails : searchAllUnLineItemsForTaxDedDetailsList.dtls) {
            if (searchAllUnLineItemsForTaxDedDetails.rulesObjectiveID
              .equals(caseNomineeTaxComponentDetails.rulesObjectiveID)) {
              if (searchAllUnLineItemsForTaxDedDetails.coverPeriodTo
                .before(yearStartDate)) {
                continue;
              }
              if (searchAllUnLineItemsForTaxDedDetails.taxReportInd == true) {
                numberOfILIs = numberOfILIs + 1;
                payDays = payDays
                  + searchAllUnLineItemsForTaxDedDetails.coverPeriodTo
                    .subtract(
                      searchAllUnLineItemsForTaxDedDetails.coverPeriodFrom)
                  + 1;
                fcFrequency =
                  searchAllUnLineItemsForTaxDedDetails.fcFrequency;
              }
            }
          }

          if (deductionILIExists == true || numberOfILIs > 1) {
            periodDuration = payDays;
            periodicityType = PatternType.kDaily;
            periodicityNumber = 365;
          } else if (numberOfILIs == 1) {
            periodicityType =
              new FrequencyPattern(fcFrequency).getPatternType();
            if (PatternType.kDaily.equals(periodicityType)) {
              periodDuration = payDays;
              periodicityNumber = 365;
            } else {
              periodDuration = 1;
              // ideally it would be better to use batch processingDate but it
              // is visible as input param.

              periodicityNumber = new FrequencyPattern(fcFrequency)
                .getAllOccurrencesIncludeEndDate(yearStartDate,
                  yearEndDate).length;
            }
          }

          if (deductionAmount.getValue() > 0.0) {
            if (caseNomineeTaxComponentDetails.amount
              .getValue() > deductionAmount.getValue()) {
              caseNomineeTaxComponentDetails.amount =
                new Money(caseNomineeTaxComponentDetails.amount.getValue()
                  - deductionAmount.getValue());
              deductionAmount = Money.kZeroMoney;
            } else {
              deductionAmount = new Money(deductionAmount.getValue()
                - caseNomineeTaxComponentDetails.amount.getValue());
              caseNomineeTaxComponentDetails.amount = Money.kZeroMoney;
            }
          }

          // component amount can be zero if it was fully allocated to pre-tax
          // deductions.
          if (caseNomineeTaxComponentDetails.amount.getValue() > 0.0) {
            caseNomineeTaxComponentDetails.periodDuration = periodDuration;
            caseNomineeTaxComponentDetails.periodicityNumber =
              periodicityNumber;
            caseNomineeTaxComponentDetails.annualAmount =
              new Money(caseNomineeTaxComponentDetails.amount.getValue()
                / periodDuration * periodicityNumber);
            caseNomineeTaxAmountDetails.annualAmount =
              new Money(caseNomineeTaxAmountDetails.annualAmount.getValue()
                + caseNomineeTaxComponentDetails.annualAmount.getValue());
          }
        }
        totalTaxableIncome = new Money(totalTaxableIncome.getValue()
          + caseNomineeTaxAmountDetails.annualAmount.getValue());
        for (final CaseNomineeTaxComponentDetails caseNomineeTaxComponentDetails : caseNomineeTaxAmountDetails.components) {
          caseNomineeTaxComponentDetails.annualAmountRatio =
            caseNomineeTaxComponentDetails.annualAmount.getValue()
              / caseNomineeTaxAmountDetails.annualAmount.getValue();
        }
      }

      for (final CaseNomineeTaxAmountDetails caseNomineeTaxAmountDetails : caseNomineeTaxAmountList.nominees) {
        caseNomineeTaxAmountDetails.annualAmountRatio =
          caseNomineeTaxAmountDetails.annualAmount.getValue()
            / totalTaxableIncome.getValue();
      }

      // Call Ruleset to calculate the tax.
      // Get address province type. Should this be from evidence and can this be
      // ready by Ruleset using propagators!!
      final EvidenceDescriptor evidenceDescriptorObj =
        EvidenceDescriptorFactory.newInstance();
      final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      caseHeaderKey.caseID = fcDtls.caseID;
      final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);

      final CaseIDAndEvidenceTypeKey caseIDAndEvidenceTypeKey =
        new CaseIDAndEvidenceTypeKey();
      caseIDAndEvidenceTypeKey.caseID =
        getParticipantDataCaseID(caseHeaderDtls.concernRoleID);
      caseIDAndEvidenceTypeKey.evidenceType = CASEEVIDENCE.BDMADDRESS;
      final EvidenceDescriptorDtlsList evidenceDescriptorDtlsList =
        evidenceDescriptorObj
          .readByCaseIDAndEvidenceType(caseIDAndEvidenceTypeKey);
      String provinceType = "";
      final AddressElement addressElementObj =
        AddressElementFactory.newInstance();

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : evidenceDescriptorDtlsList.dtls) {
        if (EVIDENCEDESCRIPTORSTATUS.ACTIVE
          .equals(evidenceDescriptorDtls.statusCode)) {
          // Get Dynamic Evidence Data.
          final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
          eiEvidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;
          eiEvidenceKey.evidenceType = evidenceDescriptorDtls.evidenceType;
          final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
            readDynamicEvidenceDataDetails(eiEvidenceKey);

          final String addressIDStr =
            dynamicEvidenceDataDetails.getAttribute("address").getValue();
          final String addressType =
            dynamicEvidenceDataDetails.getAttribute("addressType").getValue();

          final long addressID = Long.parseLong(addressIDStr);
          final Date startDate = (Date) new DateConverter()
            .convert(dynamicEvidenceDataDetails.getAttribute("fromDate"));
          final Date endDate = (Date) new DateConverter()
            .convert(dynamicEvidenceDataDetails.getAttribute("toDate"));
          // Read Address Element
          if (CONCERNROLEADDRESSTYPE.PRIVATE.equals(addressType)
            && !startDate.after(processingDate)
            && (endDate.isZero() || !endDate.before(processingDate))) {
            provinceType = getProvincialAddressElement(addressID);
          }
        }
      }
      // Get date of birth. Should this be from evidence and can this be read by
      // Ruleset using propagators!!
      final Person personObj = PersonFactory.newInstance();
      final PersonKey personKey = new PersonKey();
      personKey.concernRoleID = caseHeaderDtls.concernRoleID;
      final curam.core.struct.ReadDateOfBirthDetails readDateOfBirthDetails =
        personObj.readDateOfBirth(personKey);

      final RuleSet ruleSet = getRuleSet();
      final Session session = Session_Factory.getFactory().newInstance(
        new RecalculationsProhibited(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));
      final RuleObject taxCalculator =
        session.createRuleObject(ruleSet.findClass("BDMTaxCalculator"));

      taxCalculator.getAttributeValue("effectiveDate")
        .specifyValue(processingDate);
      taxCalculator.getAttributeValue("province").specifyValue(
        new CodeTableItem(PROVINCETYPE.TABLENAME, provinceType));
      taxCalculator.getAttributeValue("age")
        .specifyValue(getAge(readDateOfBirthDetails.dateOfBirth));
      taxCalculator.getAttributeValue("A_annualTaxableIncome")
        .specifyValue(totalTaxableIncome.getValue());
      // TBD: Status Indian and Total Claim Amount comes from UA and stored as
      // evidence not yet developed. For now use defaults.
      // Can this evidence be read by Ruleset using propagators!!
      final CaseByConcernRoleIDStatusAndTypeKey caseByConcernRoleIDStatusAndTypeKey =
        new CaseByConcernRoleIDStatusAndTypeKey();
      caseByConcernRoleIDStatusAndTypeKey.concernRoleID =
        caseHeaderDtls.concernRoleID;
      caseByConcernRoleIDStatusAndTypeKey.caseTypeCode =
        CASETYPECODE.PARTICIPANTDATACASE;
      caseByConcernRoleIDStatusAndTypeKey.statusCode = CASESTATUS.OPEN;

      long dependentChildrenCount = 0L;
      boolean clientStatusRegisteredAsIndian = Boolean.FALSE;
      boolean anyEmployersDeductedIncomeFromPay = Boolean.FALSE;
      boolean anyEmployersDeductedIncomeFromPartOfPay = Boolean.FALSE;
      boolean dependentSpouseDeclaredInTaxReturn = Boolean.FALSE;
      final CaseHeaderDtlsList caseHeaderDtlsList =
        caseHeaderObj.searchByConcernRoleIDStatusAndType(
          caseByConcernRoleIDStatusAndTypeKey);
      if (caseHeaderDtlsList.dtls.size() > 0) {
        caseIDAndEvidenceTypeKey.caseID =
          caseHeaderDtlsList.dtls.get(0).caseID;
        caseIDAndEvidenceTypeKey.evidenceType = CASEEVIDENCE.BDMTAX;
        final EvidenceDescriptorDtlsList bdmTaxEvidenceList =
          evidenceDescriptorObj
            .readByCaseIDAndEvidenceType(caseIDAndEvidenceTypeKey);
        final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
        for (final EvidenceDescriptorDtls bdmTaxEvidence : bdmTaxEvidenceList.dtls) {
          if (EVIDENCEDESCRIPTORSTATUS.ACTIVE
            .equals(bdmTaxEvidence.statusCode)) {
            // Get Dynamic Evidence Data.
            eiEvidenceKey.evidenceID = bdmTaxEvidence.relatedID;
            eiEvidenceKey.evidenceType = bdmTaxEvidence.evidenceType;
            final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
              readDynamicEvidenceDataDetails(eiEvidenceKey);
            final Date taxCreditFromDate = (Date) new DateConverter()
              .convert(dynamicEvidenceDataDetails.getAttribute("fromDate"));
            final Date taxCreditToDate = (Date) new DateConverter()
              .convert(dynamicEvidenceDataDetails.getAttribute("toDate"));

            if (!taxCreditFromDate.after(Date.getCurrentDate())
              && (taxCreditToDate.isZero()
                || !taxCreditToDate.before(Date.getCurrentDate()))) {
              dependentChildrenCount = (Long) new IntegerConverter().convert(
                dynamicEvidenceDataDetails.getAttribute("numberOfdependant"));
              final String nativeStatusStr = dynamicEvidenceDataDetails
                .getAttribute("nativeStatus").getValue();
              final String incomeTaxFromPayStr = dynamicEvidenceDataDetails
                .getAttribute("incomeTaxFromPay").getValue();
              final String incomeTaxOnlyPartStr = dynamicEvidenceDataDetails
                .getAttribute("incomeTaxOnlyPart").getValue();
              final String taxCreditStr = dynamicEvidenceDataDetails
                .getAttribute("taxCredit").getValue();
              if (BDMALERTCHOICE.YES.equals(nativeStatusStr)) {
                clientStatusRegisteredAsIndian = true;
              }

              if (BDMALERTCHOICE.YES.equals(incomeTaxFromPayStr)) {
                anyEmployersDeductedIncomeFromPay = true;
              }

              if (BDMALERTCHOICE.YES.equals(incomeTaxOnlyPartStr)) {
                anyEmployersDeductedIncomeFromPartOfPay = true;
              }

              if (BDMTAXSITUATION.SPOUSE.equals(taxCreditStr)) {
                dependentSpouseDeclaredInTaxReturn = true;
              }

            }
          }
        }
      }

      taxCalculator.getAttributeValue("clientStatusRegisteredAsIndian")
        .specifyValue(Boolean.valueOf(clientStatusRegisteredAsIndian));
      taxCalculator.getAttributeValue("anyEmployersDeductedIncomeFromPay")
        .specifyValue(Boolean.valueOf(anyEmployersDeductedIncomeFromPay));
      taxCalculator
        .getAttributeValue("anyEmployersDeductedIncomeFromPartOfPay")
        .specifyValue(
          Boolean.valueOf(anyEmployersDeductedIncomeFromPartOfPay));
      taxCalculator.getAttributeValue("dependentSpouseDeclaredInTaxReturn")
        .specifyValue(Boolean.valueOf(dependentSpouseDeclaredInTaxReturn));
      taxCalculator.getAttributeValue("dependentChildrenCount")
        .specifyValue(new Integer(String.valueOf(dependentChildrenCount)));

      Money annualTax = Money.kZeroMoney;

      if (BDMDEDUCTIONTYPE.BASIC_FEDERAL_TAX
        .equals(bdmDeductionDetails.deductionType)) {
        annualTax = new Money(((Number) taxCalculator
          .getAttributeValue("T1_annualBasicFederalTaxDeduction").getValue())
            .doubleValue()
          + ((Number) taxCalculator
            .getAttributeValue("T2_annualProvincialTaxDeduction").getValue())
              .doubleValue());
      } else {
        annualTax = new Money(((Number) taxCalculator
          .getAttributeValue("QY_annualIncomeTaxDeduction").getValue())
            .doubleValue());
      }
      double nomineePerPayTax = 0.0d;

      if (annualTax.getValue() > 0) {
        // Distribute the annual tax back into per payment amount tax.
        for (final CaseNomineeTaxAmountDetails caseNomineeTaxAmountDetails : caseNomineeTaxAmountList.nominees) {
          if (caseNomineeTaxAmountDetails.caseNomineeID == fcDtls.caseNomineeID) {
            final Money nomineeAnnualTax = new Money(annualTax.getValue()
              * caseNomineeTaxAmountDetails.annualAmountRatio);
            for (final CaseNomineeTaxComponentDetails caseNomineeTaxComponentDetails : caseNomineeTaxAmountDetails.components) {
              if (!nomineeAnnualTax.isZero()
                && caseNomineeTaxComponentDetails.periodicityNumber != 0
                && caseNomineeTaxComponentDetails.periodDuration != 0) {
                final double componentAnnualTax = nomineeAnnualTax.getValue()
                  * caseNomineeTaxComponentDetails.annualAmountRatio;
                nomineePerPayTax = nomineePerPayTax + componentAnnualTax
                  / caseNomineeTaxComponentDetails.periodicityNumber
                  * caseNomineeTaxComponentDetails.periodDuration;
              }
            }
            break;
          }
        }
      }
      genILITaxDeductionDetails.fcAmount = new Money(nomineePerPayTax);
    } else {
      genILITaxDeductionDetails.fcAmount = Money.kZeroMoney;
    }
    return genILITaxDeductionDetails;
  }

  private RuleSet getRuleSet() {

    final RuleSetManager ruleSetManager =
      GuiceWrapper.getInjector().getInstance(RuleSetManager.class);

    final RuleSet ruleSet =
      ruleSetManager.readRuleSet("BDMTaxCalculatorRuleSet");
    return ruleSet;
  }

  /**
   * This method will return the PARTICIPANTDATACASE caseID based on
   * participantRoleID and case type.
   *
   * @param participantRoleID
   * @return caseID
   *
   * @throws AppException
   * - Generic Curam Exception
   * @throws InformationalException
   * - Generic Curam Exception
   */
  public long getParticipantDataCaseID(final long participantRoleID)
    throws AppException, InformationalException {

    final CaseByConcernRoleIDStatusAndTypeKey caseByConcernRoleIDStatusAndTypeKey =
      new CaseByConcernRoleIDStatusAndTypeKey();
    caseByConcernRoleIDStatusAndTypeKey.caseTypeCode =
      CASETYPECODE.PARTICIPANTDATACASE;
    caseByConcernRoleIDStatusAndTypeKey.concernRoleID = participantRoleID;
    caseByConcernRoleIDStatusAndTypeKey.statusCode = CASESTATUS.OPEN;

    final CaseHeaderDtlsList caseHeaderDtlsList =
      CaseHeaderFactory.newInstance().searchByConcernRoleIDStatusAndType(
        caseByConcernRoleIDStatusAndTypeKey);
    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderDtlsList.dtls) {

      return caseHeaderDtls.caseID;
    }

    return 0l;
  }

  private DynamicEvidenceDataDetails
    readDynamicEvidenceDataDetails(final EIEvidenceKey evidenceKey)
      throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(evidenceKey);
    return (DynamicEvidenceDataDetails) eiEvidenceReadDtls.evidenceObject;
  }

  private String getProvincialAddressElement(final long addressID)
    throws AppException, InformationalException {

    final AddressElement addressElementObj =
      AddressElementFactory.newInstance();
    final AddressKey addressKey = new AddressKey();
    addressKey.addressID = addressID;
    final String provincialElement = "";
    final AddressElementDtlsList addressElementDtlsList =
      addressElementObj.readAddressElementDetails(addressKey);
    final boolean isQuebecAddress = false;
    for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {
      if (ADDRESSELEMENTTYPE.PROVINCE
        .equals(addressElementDtls.elementType)) {
        return addressElementDtls.elementValue;
      }
    }
    return provincialElement;
  }

  private int getAge(final Date dateOfBirth) {

    final Calendar ageCal = dateOfBirth.getCalendar();
    final int year = ageCal.get(Calendar.YEAR);
    final int month = ageCal.get(Calendar.MONTH);
    final int dayOfMonth = ageCal.get(Calendar.DAY_OF_MONTH);
    final LocalDate localDate = LocalDate.of(year, month + 1, dayOfMonth);

    final Period p = Period.between(localDate, LocalDate.now());
    return p.getYears();
  }
}
