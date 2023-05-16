package curam.ca.gc.bdmoas.sl.bdmoasmaintainexternalliability.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMALERTCHOICE;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.codetable.BDMEXTERNALLIABILITYSTATUS;
import curam.ca.gc.bdm.codetable.BDMEXTERNALLIABILITYTYPE;
import curam.ca.gc.bdm.codetable.BDMOASOBCHANGEREASON;
import curam.ca.gc.bdm.codetable.BDMOASOBCREATEREASON;
import curam.ca.gc.bdm.codetable.BDMPARTICIPANTDEDUCTIONLINKSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSITUATION;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.fact.BDMCaseDeductionItemFactory;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemDtls;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory;
import curam.ca.gc.bdm.entity.bdmexternalliability.intf.BDMExternalLiability;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityDtls;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilitySearchDetails;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilitySearchDetailsList;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilitySearchKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.DOJDeductionForILIDetails;
import curam.ca.gc.bdm.entity.bdmexternalliabilityhistory.fact.BDMExternalLiabilityHistoryFactory;
import curam.ca.gc.bdm.entity.bdmexternalliabilityhistory.struct.BDMExternalLiabilityHistoryDtls;
import curam.ca.gc.bdm.entity.bdmexternalliabilityhistory.struct.BDMExternalLiabilityHistorySearchKey;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.intf.BDMDeduction;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDetails;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionKey;
import curam.ca.gc.bdm.entity.deduction.struct.BDMRecordStatusExternalLiabilityKey;
import curam.ca.gc.bdm.entity.fact.BDMFinancialComponentDAFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.fact.BDMParticipantCaseDeductionItemLinkFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.fact.BDMParticipantDeductionCaseLinkFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.fact.BDMParticipantDeductionItemFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.intf.BDMParticipantDeductionItem;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMPDIExternalLiabilityKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionActiveCaseDeductionDetails;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionActiveCaseDeductionDetailsList;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionActiveCaseDeductionsKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionCaseLinkDtls;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionCaseLinkDtlsList;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemDtls;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemStatusKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMInstructionLineItemFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMInstructionLineItem;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMInstructionLineItemKey;
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
import curam.ca.gc.bdm.facade.bdmcasedisplay.fact.BDMCaseDisplayFactory;
import curam.ca.gc.bdm.facade.bdmcasedisplay.struct.BDMCaseHeaderConcernRoleDetails1;
import curam.ca.gc.bdm.facade.bdmcasedisplay.struct.BDMSearchCaseDetails1;
import curam.ca.gc.bdm.facade.bdmexternalliability.impl.BDMExternalLiabilityConstants;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardIDKey;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardSaveStep1Details;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLbyWizardSaveStep2Details;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityBenefitDetails;
import curam.ca.gc.bdm.facade.bdmexternalliability.struct.BDMExternalLiabilityBenefitDetailsList;
import curam.ca.gc.bdm.message.BDMEXTERNALLIABILITY;
import curam.ca.gc.bdm.message.BDMFINANCIALS;
import curam.ca.gc.bdm.sl.financial.maintaininstructionlineitem.struct.CaseNomineeTaxAmountDetails;
import curam.ca.gc.bdm.sl.financial.maintaininstructionlineitem.struct.CaseNomineeTaxAmountList;
import curam.ca.gc.bdm.sl.financial.maintaininstructionlineitem.struct.CaseNomineeTaxComponentDetails;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionCreateDetails;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionModifyDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMCheckLiabilityExistDetails;
import curam.ca.gc.bdm.sl.financial.struct.GenILIDOJDeductionDetails;
import curam.ca.gc.bdm.sl.financial.struct.GenILITaxDeductionDetails;
import curam.ca.gc.bdm.sl.maintainexternalliability.impl.MaintainExternalLiability;
import curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl.MaintainParticipantDeduction;
import curam.ca.gc.bdmoas.entity.bdmexternalliabilityhistory.struct.BDMOASExternalLiabilityHistorySearchDtls;
import curam.ca.gc.bdmoas.entity.bdmexternalliabilityhistory.struct.BDMOASExternalLiabilityHistorySearchDtlsList;
import curam.ca.gc.bdmoas.entity.oaspenalties.fact.BDMOASPenaltyFactory;
import curam.ca.gc.bdmoas.entity.oaspenalties.intf.BDMOASPenalty;
import curam.ca.gc.bdmoas.entity.oaspenalties.struct.BDMOASPenaltyDtls;
import curam.ca.gc.bdmoas.entity.oaspenalties.struct.BDMOASPenaltyDtlsList;
import curam.ca.gc.bdmoas.entity.oaspenalties.struct.BDMOASPenaltyKey;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASExternalLbyWizardSaveStep1Details;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASExternalLbyWizardSaveStep2Details;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASExternalLiabilityDetails;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASExternalLiabilityDetailsList;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASExternalLiabilityHistoryDetails;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASExternalLiabilityHistoryDetailsList;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASModifyLiabilityDetails;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASOverPaymentCaseDetails;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASOverPaymentCaseDetailsList;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASOverPaymentHistoryDetails;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASOverPaymentHistoryDetailsList;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASReadLbyDetailsForEdit;
import curam.ca.gc.bdmoas.facade.bdmoasexternalliability.struct.BDMOASRegisterExternalLbyDetails;
import curam.ca.gc.bdmoas.message.BDMOASEXTERNALLIABILITY;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.BUSINESSSTATUS;
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
import curam.codetable.impl.REASSESSMENTRESULTEntry;
import curam.core.facade.struct.SearchCaseKey_fo;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.CaseRelationshipFactory;
import curam.core.fact.InstructionLineItemFactory;
import curam.core.fact.MaintainInstructionLineItemFactory;
import curam.core.fact.PersonFactory;
import curam.core.fact.ProductDeliveryFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.AddressElement;
import curam.core.intf.CaseDeductionItem;
import curam.core.intf.CaseHeader;
import curam.core.intf.Person;
import curam.core.intf.ProductDelivery;
import curam.core.sl.entity.fact.DeductionFactory;
import curam.core.sl.entity.fact.OverpaymentEvidenceFactory;
import curam.core.sl.entity.struct.DeductionDtls;
import curam.core.sl.entity.struct.DeductionKey;
import curam.core.sl.entity.struct.OverpaymentCaseTabDetails;
import curam.core.sl.fact.UserAccessFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.paymentcorrection.impl.PaymentCorrectionEvidence;
import curam.core.sl.infrastructure.paymentcorrection.impl.PaymentCorrectionEvidenceDAO;
import curam.core.sl.infrastructure.paymentcorrection.impl.PaymentCorrectionImpl;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.UserAccess;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.Amount;
import curam.core.struct.CaseByConcernRoleIDStatusAndTypeKey;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseRelationshipCaseIDKey;
import curam.core.struct.CaseRelationshipDtlsList;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.Count;
import curam.core.struct.DateStruct;
import curam.core.struct.FCAmount;
import curam.core.struct.FinCompCoverPeriod;
import curam.core.struct.FinancialComponentDtls;
import curam.core.struct.ILICaseID;
import curam.core.struct.ILITabDetail;
import curam.core.struct.ILITabDetailList;
import curam.core.struct.InformationalMsgDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.ProductDeliveryDtls;
import curam.core.struct.ProductDeliveryKey;
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
import curam.message.BPOMAINTAINDEDUCTIONITEMS;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.FrequencyPattern;
import curam.util.type.FrequencyPattern.PatternType;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a service layer class used to maintain OAS External Liabilities.
 *
 * @author pranav.agarwal
 *
 */
public class BDMOASMaintainExternalLiability extends
  curam.ca.gc.bdmoas.sl.bdmoasmaintainexternalliability.base.BDMOASMaintainExternalLiability {

  @Inject
  PaymentCorrectionImpl paymentCorrectionObj;

  @Inject
  MaintainParticipantDeduction maintainParticipantDeductionObj;

  @Inject
  protected PaymentCorrectionEvidenceDAO paymentCorrectionEvidenceDAO;

  @Inject
  MaintainExternalLiability maintainExternalLiabilityObj;

  /**
   * Constructor
   */
  public BDMOASMaintainExternalLiability() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Lists all the external liabilities for a concern role
   */
  @Override
  public BDMOASExternalLiabilityDetailsList listLiabilities(
    final ConcernRoleKey key) throws AppException, InformationalException {

    final BDMOASExternalLiabilityDetailsList bdmLiabilitiesList =
      new BDMOASExternalLiabilityDetailsList();

    // search for all non-deleted liabilities belonging to the participant
    final BDMExternalLiabilitySearchKey extLbyKey =
      new BDMExternalLiabilitySearchKey();
    extLbyKey.concernRoleID = key.concernRoleID;
    extLbyKey.recordStatusCode = RECORDSTATUS.NORMAL;
    // entity instance
    final BDMExternalLiability bdmExternalLiabilityObj =
      BDMExternalLiabilityFactory.newInstance();
    // calling the method to search liabilities by concern role ID
    final BDMExternalLiabilitySearchDetailsList liabilitiesList =
      bdmExternalLiabilityObj.searchLiabilities(extLbyKey);

    BDMOASExternalLiabilityDetails bdmLiabilityDetails;
    NotFoundIndicator nfIndicator;
    BDMPDIExternalLiabilityKey participantDeductionKey;
    final BDMParticipantDeductionItem bdmParticipantDeductionItemObj =
      BDMParticipantDeductionItemFactory.newInstance();
    // iterate the liabilities list
    for (final BDMExternalLiabilitySearchDetails extLiability : liabilitiesList.dtls) {
      bdmLiabilityDetails = new BDMOASExternalLiabilityDetails();
      bdmLiabilityDetails.dtls.assign(extLiability);

      final BDMExternalLiabilityKey bdmExtLbyKey =
        new BDMExternalLiabilityKey();
      bdmExtLbyKey.externalLiabilityID = extLiability.externalLiabilityID;

      // get the last non-cancelled ILI that was associated with this liability
      // to get the end date
      if (bdmLiabilityDetails.dtls.outstandingAmount.isZero()) {

        final BDMTotalAllocationByExtLiabilityKey bdmTotalAllocDateKey =
          new BDMTotalAllocationByExtLiabilityKey();
        bdmTotalAllocDateKey.externalLiabilityID =
          extLiability.externalLiabilityID;
        bdmTotalAllocDateKey.iliStatus = ILISTATUS.CANCELLED;
        final DateStruct endDate =
          BDMInstructionLineItemDAFactory.newInstance()
            .getBDMMaxAllocationDateByExtLiability(bdmTotalAllocDateKey);

        bdmLiabilityDetails.liabilityStatusCode =
          BDMEXTERNALLIABILITYSTATUS.REPAID;

        bdmLiabilityDetails.dtls.endDate = endDate.date;
        // check for the end date
        // 1. If end date is zero then it means that ILI is not created
        // 2. If liability amount is zero which means there is no initial
        // outstanding balance
        if (bdmLiabilityDetails.dtls.endDate.isZero()
          && extLiability.liabilityAmount.isZero()) {
          // set the end date to current date
          // new instance of input keys
          nfIndicator = new NotFoundIndicator();
          participantDeductionKey = new BDMPDIExternalLiabilityKey();
          // populate the external liability ID
          participantDeductionKey.externalLiabilityID =
            bdmLiabilityDetails.dtls.externalLiabilityID;
          final BDMParticipantDeductionItemKey bdmPDIKey =
            BDMParticipantDeductionItemFactory.newInstance()
              .readByExternalLiability(nfIndicator, participantDeductionKey);
          // check if record is found
          if (!nfIndicator.isNotFound()) {
            // calling the method to read the participant deduction details
            final BDMParticipantDeductionItemDtls originalPDIDtls =
              bdmParticipantDeductionItemObj.read(bdmPDIKey);
            // assign the end date here from the participant deduction
            bdmLiabilityDetails.dtls.endDate = originalPDIDtls.endDate;
          }
        }
      }

      // calling the method read the external liability record
      final BDMExternalLiabilityDtls liabilityDtls =
        bdmExternalLiabilityObj.read(bdmExtLbyKey);
      // assign the create reason here
      bdmLiabilityDetails.createReason = liabilityDtls.createReason;
      bdmLiabilityDetails.liabilityStatusCode =
        liabilityDtls.liabilityStatusCode;

      // modify the action indicator buttons
      if (extLiability.liabilityStatusCode
        .equals(BDMEXTERNALLIABILITYSTATUS.ACTIVE)) {
        bdmLiabilityDetails.dtls.showDeactivateIcon = true;
      } else if (extLiability.liabilityStatusCode
        .equals(BDMEXTERNALLIABILITYSTATUS.INACTIVE)) {
        bdmLiabilityDetails.dtls.showActivateIcon = true;
      }
      // if no deductions applied, show delete key
      bdmExtLbyKey.externalLiabilityID = extLiability.externalLiabilityID;
      final Count count = BDMCaseDeductionItemFactory.newInstance()
        .countDedILIsByExtLiability(bdmExtLbyKey);
      if (count.numberOfRecords == 0) {
        bdmLiabilityDetails.dtls.showDeleteIcon = true;
      }
      bdmLiabilityDetails.dtls.showEditIcon = true;

      // if it is deleted or a system managed liability, do not show any action
      // items
      if (!extLiability.recordStatusCode.equals(RECORDSTATUS.NORMAL)
        || bdmLiabilityDetails.liabilityStatusCode
          .equals(BDMEXTERNALLIABILITYSTATUS.REPAID)) {
        bdmLiabilityDetails.dtls.showActivateIcon = false;
        bdmLiabilityDetails.dtls.showDeactivateIcon = false;
        bdmLiabilityDetails.dtls.showDeleteIcon = true;
        bdmLiabilityDetails.dtls.showEditIcon = false;
      }

      // START - TASK 53598 - Manage Penalty
      // Non-Beneficiary
      if (BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
        .equalsIgnoreCase(extLiability.liabilityTypeCode)) {
        // hide all the buttons except delete
        bdmLiabilityDetails.dtls.showEditIcon = false;
        bdmLiabilityDetails.dtls.showActivateIcon = false;
        bdmLiabilityDetails.dtls.showDeactivateIcon = false;
      }
      // END - TASK 53598 - Manage Penalty

      // hide all the buttons for system generated liabilities
      if (BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS
        .equalsIgnoreCase(extLiability.liabilityTypeCode)
        || BDMEXTERNALLIABILITYTYPE.DOJ_FEES
          .equalsIgnoreCase(extLiability.liabilityTypeCode)) {
        bdmLiabilityDetails.dtls.showActivateIcon = false;
        bdmLiabilityDetails.dtls.showDeactivateIcon = false;
        bdmLiabilityDetails.dtls.showDeleteIcon = false;
        bdmLiabilityDetails.dtls.showEditIcon = false;
      }

      bdmLiabilitiesList.dtlsList.add(bdmLiabilityDetails);
    }

    return bdmLiabilitiesList;
  }

  /**
   * Temporarily save data from the first step of the external liability wizard
   */
  @Override
  public BDMExternalLbyWizardIDKey createLbySaveStep1Data(
    final BDMOASExternalLbyWizardSaveStep1Details externalLiabilityStep1Details)
    throws AppException, InformationalException {

    if (externalLiabilityStep1Details.dtls.liabilityAmount.isZero()) {
      throw new AppException(BDMEXTERNALLIABILITY.ERR_NO_LIABILITY_AMOUNT);
    }

    if (externalLiabilityStep1Details.dtls.liabilityAmount.isNegative()) {
      throw new AppException(BDMEXTERNALLIABILITY.ERR_NEGATIVE_AMOUNT);
    }

    if (externalLiabilityStep1Details.createReason
      .equals(BDMOASOBCREATEREASON.OTHER)
      && externalLiabilityStep1Details.dtls.comments.isEmpty()) {
      throw new AppException(BDMOASEXTERNALLIABILITY.ERR_OTHER_REASON);
    }

    // A user cannot create an outstanding record if the Liability Type is
    // Penalty - OAS OR Penalty â€“ OAS Non-Beneficiary
    // AND the Create Reason = Received Request.
    if ((externalLiabilityStep1Details.dtls.extLbyTypeCode
      .equalsIgnoreCase(BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS)
      || externalLiabilityStep1Details.dtls.extLbyTypeCode
        .equalsIgnoreCase(BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB))
      && externalLiabilityStep1Details.createReason
        .equals(BDMOASOBCREATEREASON.RECEIVED_REQUEST)) {
      final AppException ex = new AppException(
        BDMOASEXTERNALLIABILITY.ERR_RECEIVED_REQUEST_CREATE_REASON_CANNOT_BE_USED);
      ex.arg(CodeTable.getOneItem(BDMEXTERNALLIABILITYTYPE.TABLENAME,
        externalLiabilityStep1Details.dtls.extLbyTypeCode));
      // throw the validation message here
      throw ex;
    }

    // A user cannot select â€˜Create Reasonâ€™ of Minister Authorized, if the
    // Liability Type does not equal Penalty - OAS OR Penalty â€“ OAS
    // Non-Beneficiary
    if (!(externalLiabilityStep1Details.dtls.extLbyTypeCode
      .equalsIgnoreCase(BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS)
      || externalLiabilityStep1Details.dtls.extLbyTypeCode
        .equalsIgnoreCase(BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB))
      && externalLiabilityStep1Details.createReason
        .equals(BDMOASOBCREATEREASON.MINISTER_AUTHORIZED)) {
      final AppException ex = new AppException(
        BDMOASEXTERNALLIABILITY.ERR_MINISTER_AUTH_CREATE_REASON_CANNOT_BE_USED);
      ex.arg(CodeTable.getOneItem(BDMEXTERNALLIABILITYTYPE.TABLENAME,
        externalLiabilityStep1Details.dtls.extLbyTypeCode));
      // throw the validation message here
      throw ex;
    }

    final WizardPersistentState wizardPersistanceObj =
      new WizardPersistentState();
    final BDMExternalLbyWizardIDKey bdmExternalLbyWizardIDKey =
      new BDMExternalLbyWizardIDKey();
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // search for all non-deleted liabilities belonging to the participant
    final BDMExternalLiabilitySearchKey extLbyKey =
      new BDMExternalLiabilitySearchKey();
    extLbyKey.concernRoleID =
      externalLiabilityStep1Details.dtls.concernRoleID;
    extLbyKey.recordStatusCode = RECORDSTATUS.NORMAL;
    final BDMExternalLiabilitySearchDetailsList liabilitiesList =
      BDMExternalLiabilityFactory.newInstance().searchLiabilities(extLbyKey);

    // list liability
    final Set<String> libilityStatusSet = new HashSet<>();
    for (final BDMExternalLiabilitySearchDetails extLiability : liabilitiesList.dtls) {
      if (extLiability.liabilityTypeCode
        .equals(externalLiabilityStep1Details.dtls.extLbyTypeCode)
        && !(BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
          .equalsIgnoreCase(extLiability.liabilityTypeCode)
          || BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS
            .equalsIgnoreCase(extLiability.liabilityTypeCode))) {
        libilityStatusSet.add(extLiability.liabilityStatusCode);
      }
    }
    if (!libilityStatusSet.isEmpty()
      && libilityStatusSet.contains(BDMEXTERNALLIABILITYSTATUS.ACTIVE)) {
      final AppException e =
        new AppException(BDMEXTERNALLIABILITY.ERR_LIBILITY_TYPE_EXISTS);
      e.arg(CodeTable.getOneItem(BDMEXTERNALLIABILITYTYPE.TABLENAME,
        externalLiabilityStep1Details.dtls.extLbyTypeCode));
      informationalManager.addInformationalMsg(e, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    } else {
      if (externalLiabilityStep1Details.dtls.wizardStateID == 0) {
        final long wizardStateID =
          wizardPersistanceObj.create(externalLiabilityStep1Details);
        bdmExternalLbyWizardIDKey.wizardStateID = wizardStateID;
      } else {
        final BDMOASExternalLbyWizardSaveStep1Details details =
          (BDMOASExternalLbyWizardSaveStep1Details) wizardPersistanceObj
            .read(externalLiabilityStep1Details.dtls.wizardStateID);
        externalLiabilityStep1Details.step2Dtls.assign(details.step2Dtls);
        wizardPersistanceObj.modify(
          externalLiabilityStep1Details.dtls.wizardStateID,
          externalLiabilityStep1Details);
        bdmExternalLbyWizardIDKey.wizardStateID =
          externalLiabilityStep1Details.dtls.wizardStateID;
      }
    }

    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
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
    final BDMOASExternalLbyWizardSaveStep1Details step1Details =
      (BDMOASExternalLbyWizardSaveStep1Details) wizardPersistanceObj
        .read(wizardStateID.wizardStateID);

    final BDMRecordStatusExternalLiabilityKey bdmDeductionKey =
      new BDMRecordStatusExternalLiabilityKey();
    bdmDeductionKey.externalLiabilityType = step1Details.dtls.extLbyTypeCode;
    bdmDeductionKey.recordStatus = RECORDSTATUS.NORMAL;
    final BDMDeductionDetails bdmDeductionDetails =
      BDMDeductionFactory.newInstance()
        .readActiveDeductionLinkedToExternalLiabilityType(bdmDeductionKey);

    return getBenefitCasesForConcernRole(step1Details.dtls.concernRoleID,
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
  private BDMExternalLiabilityBenefitDetailsList
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
    final BDMOASExternalLbyWizardSaveStep2Details step2Details)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // START - TASK 53598 - Manage Penalty
    // get details from step 1
    final WizardPersistentState wizardPersistanceObj =
      new WizardPersistentState();
    final BDMOASExternalLbyWizardSaveStep1Details step1Details =
      (BDMOASExternalLbyWizardSaveStep1Details) wizardPersistanceObj
        .read(wizardStateID.wizardStateID);

    // skip the deduction validation for liability type Penalty - OAS
    // Non-Beneficiary
    if (!BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
      .equalsIgnoreCase(step1Details.dtls.extLbyTypeCode)) {
      // calling the method to validate the deduction details
      validateDeductionDetails(step2Details.dtls.selBenefitIDs,
        step2Details.dtls.deductionAmount, step2Details.dtls.deductionRate,
        informationalManager, step2Details.dtls.startDate,
        Date.getCurrentDate(), true);
    }
    // END - TASK 53598 - Manage Penalty

    validateRegisterLiability(step1Details.dtls, informationalManager);

    if (informationalManager.operationHasInformationals()) {

      informationalManager.failOperation();

    }

    // create the liability
    final BDMOASRegisterExternalLbyDetails lbyDetails =
      new BDMOASRegisterExternalLbyDetails();
    lbyDetails.dtls.assign(step1Details.dtls);
    // assign the create reason here
    lbyDetails.createReason = step1Details.createReason;
    // register the liability for recipient
    final BDMExternalLiabilityKey bdmExtLbyKey =
      registerLiability(lbyDetails);

    // create the deductions
    registerDeductions(bdmExtLbyKey, step2Details.dtls);

    // activate the liability
    final BDMExternalLiabilityDtls liabilityDtls =
      BDMExternalLiabilityFactory.newInstance().read(bdmExtLbyKey);

    final VersionNumberDetails versionNoDetails = new VersionNumberDetails();
    versionNoDetails.versionNo = liabilityDtls.versionNo;
    activateLiability(bdmExtLbyKey, versionNoDetails);

    // check if over payment case is selected
    if (CuramConst.kLongZero != step2Details.overPaymentCaseID) {
      // if yes then we need to link record in BDMOASPenalty table
      // and persist the selected over payment case ID in the wizard state
      // create new instance of details object
      final BDMOASPenaltyDtls BDMOASPenaltyDtls = new BDMOASPenaltyDtls();
      // populate the details object
      BDMOASPenaltyDtls.externalLiabilityID =
        bdmExtLbyKey.externalLiabilityID;
      BDMOASPenaltyDtls.currentOverPaymentCaseID =
        step2Details.overPaymentCaseID;
      BDMOASPenaltyDtls.recordStatusCode = RECORDSTATUS.NORMAL;
      BDMOASPenaltyDtls.penaltyID = UniqueID.nextUniqueID();
      // insert the record
      BDMOASPenaltyFactory.newInstance().insert(BDMOASPenaltyDtls);
    }

    return bdmExtLbyKey;
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
    registerLiability(final BDMOASRegisterExternalLbyDetails lbyDetails)
      throws AppException, InformationalException {

    final BDMExternalLiabilityDtls bdmLbyDetails =
      new BDMExternalLiabilityDtls();

    bdmLbyDetails.assign(lbyDetails.dtls);

    // set details
    bdmLbyDetails.createReason = lbyDetails.createReason;
    bdmLbyDetails.outstandingAmount = lbyDetails.dtls.liabilityAmount;
    bdmLbyDetails.creationDate = Date.getCurrentDate();
    bdmLbyDetails.recordStatusCode = RECORDSTATUS.NORMAL;
    bdmLbyDetails.liabilityStatusCode = BDMEXTERNALLIABILITYSTATUS.INACTIVE;
    // set the program type as default OAS for this benefit
    bdmLbyDetails.programType = BDMBENEFITPROGRAMTYPE.getDefaultCode();

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
        maintainExternalLiabilityObj.getLiabilityByExtRefNumber(checkLbyDtls);

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

    // START - TASK 53598 - Manage Penalty
    // deduction will not be created for liability type
    // Penalty - OAS Non-Beneficiary
    if (BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
      .equalsIgnoreCase(liabilityDtls.liabilityTypeCode)) {
      // skip the call
      return;
    }
    // END - TASK 53598 - Manage Penalty

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
  private void validateRegisterLiability(

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
  private void validateDeductionDetails(final String selBenefitIDs,
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

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // search for all non-deleted liabilities belonging to the participant
    final BDMExternalLiabilitySearchKey extLbyKey =
      new BDMExternalLiabilitySearchKey();
    extLbyKey.concernRoleID = dtls.concernRoleID;
    extLbyKey.recordStatusCode = RECORDSTATUS.NORMAL;
    final BDMExternalLiabilitySearchDetailsList liabilitiesList =
      BDMExternalLiabilityFactory.newInstance().searchLiabilities(extLbyKey);

    final Set<String> libilityStatusSet = new HashSet<>();
    for (final BDMExternalLiabilitySearchDetails extLiability : liabilitiesList.dtls) {
      if (extLiability.liabilityTypeCode.equals(dtls.liabilityTypeCode)
        && !(BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
          .equalsIgnoreCase(dtls.liabilityTypeCode)
          || BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS
            .equalsIgnoreCase(dtls.liabilityTypeCode))) {
        libilityStatusSet.add(extLiability.liabilityStatusCode);
      }
    }
    if (!libilityStatusSet.isEmpty()
      && libilityStatusSet.contains(BDMEXTERNALLIABILITYSTATUS.ACTIVE)) {
      final AppException e =
        new AppException(BDMEXTERNALLIABILITY.ERR_ACTIVE_TYPE_EXTSTS);
      e.arg(CodeTable.getOneItem(BDMEXTERNALLIABILITYTYPE.TABLENAME,
        dtls.liabilityTypeCode));
      informationalManager.addInformationalMsg(e, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    dtls.liabilityStatusCode = BDMEXTERNALLIABILITYSTATUS.ACTIVE;

    createLiabilityHistoryRecord(dtls, null);
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

    createLiabilityHistoryRecord(dtls, null);
    bdmExternalLiabilityObj.modify(key, dtls);

    // look for participant deduction linked to the ext lby
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMPDIExternalLiabilityKey participantDeductionKey =
      new BDMPDIExternalLiabilityKey();
    participantDeductionKey.externalLiabilityID = key.externalLiabilityID;
    final BDMParticipantDeductionItemKey bdmPDIKey =
      BDMParticipantDeductionItemFactory.newInstance()
        .readByExternalLiability(nfIndicator, participantDeductionKey);

    // deactivate participant deduction
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

    createLiabilityHistoryRecord(dtls, null);
    bdmExternalLiabilityObj.modify(key, dtls);

    // look for participant deduction linked to the ext lby
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMPDIExternalLiabilityKey participantDeductionKey =
      new BDMPDIExternalLiabilityKey();
    participantDeductionKey.externalLiabilityID = key.externalLiabilityID;
    final BDMParticipantDeductionItemKey bdmPDIKey =
      BDMParticipantDeductionItemFactory.newInstance()
        .readByExternalLiability(nfIndicator, participantDeductionKey);

    // delete participant deduction
    if (!nfIndicator.isNotFound()) {
      maintainParticipantDeductionObj.deleteParticipantDeduction(bdmPDIKey);
    }

  }

  /**
   * Reads details needed for edit screen for liability
   */
  @Override
  public BDMOASReadLbyDetailsForEdit
    readLbyDetailsForEdit(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    // initialize return struct
    final BDMOASReadLbyDetailsForEdit editDetails =
      new BDMOASReadLbyDetailsForEdit();

    final BDMExternalLiabilityDtls dtls =
      BDMExternalLiabilityFactory.newInstance().read(key);

    editDetails.dtls.assign(dtls);

    // START - TASK 53598 - Manage Penalty
    // We are not capturing the deduction for liability type Penalty - OAS
    // Non-Beneficiary. Skip the call to retrieve the deduction details for this
    // liability
    if (!BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
      .equalsIgnoreCase(dtls.liabilityTypeCode)) {

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
        editDetails.dtls.deductionAmount = pdiDtls.amount;
        editDetails.dtls.deductionRate = pdiDtls.rate;
        editDetails.dtls.startDate = pdiDtls.startDate;
      }
    }
    // END - TASK 53598 - Manage Penalty

    return editDetails;
  }

  /**
   * Modifies the liability amount, and if resetOutstandingAmountInd is true,
   * reset the outstanding amount
   */
  @Override
  public void
    modifyLiabilityDetails(final BDMOASModifyLiabilityDetails modifyDtls)
      throws AppException, InformationalException {

    final BDMExternalLiability bdmExternalLiabilityObj =
      BDMExternalLiabilityFactory.newInstance();
    final BDMExternalLiabilityKey bdmExtLbyKey =
      new BDMExternalLiabilityKey();
    bdmExtLbyKey.externalLiabilityID = modifyDtls.dtls.externalLiabilityID;
    final BDMExternalLiabilityDtls dtls =
      bdmExternalLiabilityObj.read(bdmExtLbyKey);

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (modifyDtls.dtls.liabilityAmount.isNegative()) {
      informationalManager.addInformationalMsg(
        new AppException(BDMEXTERNALLIABILITY.ERR_NEGATIVE_AMOUNT),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }
    if (dtls.recordStatusCode.equals(RECORDSTATUS.CANCELLED)) {
      informationalManager.addInformationalMsg(
        new AppException(BDMEXTERNALLIABILITY.ERR_MODIFY_DELETED_LBY),
        CuramConst.gkEmpty, InformationalElement.InformationalType.kError);
    }

    // START - TASK 53598 - Manage Penalty
    // skip the deduction validation for liability type Penalty - OAS
    // Non-Beneficiary
    if (!BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
      .equalsIgnoreCase(dtls.liabilityTypeCode)) {
      // calling the method to validate the deduction details
      validateDeductionDetails(modifyDtls.dtls.selBenefitIDs,
        modifyDtls.dtls.deductionAmount, modifyDtls.dtls.deductionRate,
        informationalManager, modifyDtls.dtls.startDate, dtls.creationDate,
        false);
    }
    // END - TASK 53598 - Manage Penalty

    // validate the details
    // if change reason is selected as OTHER, comments should be added
    if (modifyDtls.changeReason.equals(BDMOASOBCHANGEREASON.OTHER)
      && StringUtil.isNullOrEmpty(modifyDtls.dtls.comments)) {
      throw new AppException(BDMOASEXTERNALLIABILITY.ERR_CHANGE_REASON);
    }

    // The system should prevent a user from editing the liability record if the
    // Liability Type is Penalty - OAS OR Penalty – OAS Non-Beneficiary
    // AND the Change Reason = Received Request.
    if ((dtls.liabilityTypeCode
      .equalsIgnoreCase(BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS)
      || dtls.liabilityTypeCode
        .equalsIgnoreCase(BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB))
      && modifyDtls.changeReason
        .equals(BDMOASOBCHANGEREASON.RECEIVED_REQUEST)) {
      final AppException ex = new AppException(
        BDMOASEXTERNALLIABILITY.ERR_RECEIVED_REQUEST_CHANGE_REASON_CANNOT_BE_USED);
      ex.arg(CodeTable.getOneItem(BDMEXTERNALLIABILITYTYPE.TABLENAME,
        dtls.liabilityTypeCode));
      // throw the validation message here
      throw ex;
    }

    // The system shall prevent the user from modifying the record if the user
    // selects Change Reason of Undue Hardship, AND
    // the Liability Type does not equal Penalty - OAS OR Penalty – OAS
    // Non-Beneficiary
    if (!(dtls.liabilityTypeCode
      .equalsIgnoreCase(BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS)
      || dtls.liabilityTypeCode
        .equalsIgnoreCase(BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB))
      && modifyDtls.changeReason
        .equals(BDMOASOBCHANGEREASON.UNDUE_HARDSHIP)) {
      final AppException ex = new AppException(
        BDMOASEXTERNALLIABILITY.ERR_UNDUE_HARDSHIP_CHANGE_REASON_CANNOT_BE_USED);
      ex.arg(CodeTable.getOneItem(BDMEXTERNALLIABILITYTYPE.TABLENAME,
        dtls.liabilityTypeCode));
      // throw the validation message here
      throw ex;
    }

    if (informationalManager.operationHasInformationals()) {

      informationalManager.failOperation();

    }

    // if the liability amount has changed, then reset the outstanding amount
    if (!dtls.liabilityAmount.equals(modifyDtls.dtls.liabilityAmount)) {
      dtls.outstandingAmount = modifyDtls.dtls.liabilityAmount;
    }

    dtls.comments = modifyDtls.dtls.comments;
    dtls.liabilityAmount = modifyDtls.dtls.liabilityAmount;

    if (dtls.liabilityAmount.isZero()) {
      dtls.liabilityStatusCode = BDMEXTERNALLIABILITYSTATUS.REPAID;
    }

    createLiabilityHistoryRecord(dtls, modifyDtls.changeReason);
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

      modifyDeductionDetails.amount = modifyDtls.dtls.deductionAmount;
      modifyDeductionDetails.rate = modifyDtls.dtls.deductionRate;
      modifyDeductionDetails.selBenefitIDs = modifyDtls.dtls.selBenefitIDs;
      modifyDeductionDetails.startDate = modifyDtls.dtls.startDate;
      modifyDeductionDetails.bdmParticipantDeductionItemID =
        bdmPDIKey.bdmParticipantDeductionItemID;
      if (dtls.liabilityAmount.isZero()) {
        modifyDeductionDetails.endDate = Date.getCurrentDate();
      }
      maintainParticipantDeductionObj
        .modifyParticipantDeduction(modifyDeductionDetails);

      // inactive the active case deduction item if liability amount is 0
      if (modifyDtls.dtls.liabilityAmount.isZero()) {
        // calling the method to inactive case deduction item if liability
        // amount is 0
        inactiveCaseDeductionItem(modifyDeductionDetails);
      }
    }

    // check for the liability type code if related to penalty then will update
    // selected over payment case if there is any change
    if (BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
      .equalsIgnoreCase(dtls.liabilityTypeCode)
      || BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS
        .equalsIgnoreCase(dtls.liabilityTypeCode)) {
      // create new entity instance
      final BDMOASPenalty BDMOASPenaltyObj =
        BDMOASPenaltyFactory.newInstance();
      try {
        // calling the method to read active penalty record by liability id
        final BDMOASPenaltyDtls BDMOASPenaltyDtls = BDMOASPenaltyObj
          .readActivePenaltyByExternalLiabilityID(bdmExtLbyKey);
        // compare the selected and existing record
        if (BDMOASPenaltyDtls.currentOverPaymentCaseID != modifyDtls.overPaymentCaseID) {
          // new instance of key
          final BDMOASPenaltyKey BDMOASPenaltyKey = new BDMOASPenaltyKey();
          // populate the penalty ID
          BDMOASPenaltyKey.penaltyID = BDMOASPenaltyDtls.penaltyID;
          // cancel the existing record
          BDMOASPenaltyDtls.recordStatusCode = RECORDSTATUS.CANCELLED;
          BDMOASPenaltyDtls.previousOverPaymentCaseID =
            BDMOASPenaltyDtls.currentOverPaymentCaseID;
          BDMOASPenaltyDtls.currentOverPaymentCaseID =
            modifyDtls.overPaymentCaseID;
          BDMOASPenaltyDtls.externalLiabilityID =
            bdmExtLbyKey.externalLiabilityID;
          BDMOASPenaltyDtls.updatedBy = TransactionInfo.getProgramUser();
          BDMOASPenaltyDtls.updatedOn = Date.getCurrentDate();

          // modify the record
          BDMOASPenaltyObj.modify(BDMOASPenaltyKey, BDMOASPenaltyDtls);

          // check if a record is selected
          if (CuramConst.kLongZero != modifyDtls.overPaymentCaseID) {
            // insert a new record
            final BDMOASPenaltyDtls newBDMOASPenaltyDtls =
              new BDMOASPenaltyDtls();
            // populate the details object
            newBDMOASPenaltyDtls.externalLiabilityID =
              bdmExtLbyKey.externalLiabilityID;
            newBDMOASPenaltyDtls.currentOverPaymentCaseID =
              modifyDtls.overPaymentCaseID;
            newBDMOASPenaltyDtls.recordStatusCode = RECORDSTATUS.NORMAL;
            newBDMOASPenaltyDtls.penaltyID = UniqueID.nextUniqueID();
            // insert the record
            BDMOASPenaltyObj.insert(newBDMOASPenaltyDtls);
          }
        }

      } catch (final RecordNotFoundException recordNotFoundException) {
        // if record not available then insert the record
        // check if a record is selected
        if (CuramConst.kLongZero != modifyDtls.overPaymentCaseID) {
          // insert a new record
          final BDMOASPenaltyDtls newBDMOASPenaltyDtls =
            new BDMOASPenaltyDtls();
          // populate the details object
          newBDMOASPenaltyDtls.externalLiabilityID =
            bdmExtLbyKey.externalLiabilityID;
          newBDMOASPenaltyDtls.currentOverPaymentCaseID =
            modifyDtls.overPaymentCaseID;
          newBDMOASPenaltyDtls.recordStatusCode = RECORDSTATUS.NORMAL;
          newBDMOASPenaltyDtls.penaltyID = UniqueID.nextUniqueID();
          // insert the record
          BDMOASPenaltyObj.insert(newBDMOASPenaltyDtls);
        }
      }
    }
  }

  /**
   * This method will be used to inactive case deduction item if the liability
   * amount is reset to zero
   *
   * @param modifyDeductionDetails - This holds the updated deduction details
   * @throws AppException
   * @throws InformationalException
   */
  private void inactiveCaseDeductionItem(
    final BDMParticipantDeductionModifyDetails modifyDeductionDetails)
    throws AppException, InformationalException {

    // finds all currently linked case deductions
    final BDMParticipantDeductionItemStatusKey deductionItemStatusKey =
      new BDMParticipantDeductionItemStatusKey();
    deductionItemStatusKey.bdmParticipantDeductionItemID =
      modifyDeductionDetails.bdmParticipantDeductionItemID;
    deductionItemStatusKey.linkStatus =
      BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;
    // calling the method to read active participant deductions
    final BDMParticipantDeductionCaseLinkDtlsList existingCases =
      BDMParticipantDeductionCaseLinkFactory.newInstance()
        .readActiveParticipantDeductions(deductionItemStatusKey);

    // Separate tab delimited list of selected cases into an array
    final List<String> selectedCases = StringUtil.delimitedText2StringList(
      modifyDeductionDetails.selBenefitIDs, CuramConst.gkTabDelimiterChar);

    // new instance
    final CaseDeductionItem caseDeductionItemObj =
      CaseDeductionItemFactory.newInstance();
    // new reference
    BDMParticipantDeductionActiveCaseDeductionsKey activeCaseDeductionKey;
    // iterate through existing cases to see which ones were selected again,
    // and which were unselected
    for (final BDMParticipantDeductionCaseLinkDtls existingCase : existingCases.dtls) {

      // get the existing case ID
      final String caseIDStr = Long.toString(existingCase.caseID);

      // if it is in the selected case list, then deduction must be inactive
      if (selectedCases.contains(caseIDStr)) {
        // search for all currently linked deductions on this case
        activeCaseDeductionKey =
          new BDMParticipantDeductionActiveCaseDeductionsKey();
        // populate the key object
        activeCaseDeductionKey.bdmParticipantDeductionCaseLinkID =
          existingCase.bdmParticipantDeductionCaseLinkID;
        activeCaseDeductionKey.linkStatus =
          BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;
        // calling the method to search active case deductions
        final BDMParticipantDeductionActiveCaseDeductionDetailsList activeCaseDeductions =
          BDMParticipantCaseDeductionItemLinkFactory.newInstance()
            .searchActiveCaseDeductions(activeCaseDeductionKey);

        // new reference
        CaseDeductionItemKey cdiKey;
        // iterate through all active deductions
        for (final BDMParticipantDeductionActiveCaseDeductionDetails activeDeduction : activeCaseDeductions.dtls) {
          // new reference
          cdiKey = new CaseDeductionItemKey();
          // assign the case deduction item ID
          cdiKey.caseDeductionItemID = activeDeduction.caseDeductionItemID;
          // calling the method to read the case deductions
          final CaseDeductionItemDtls caseDeductionItemDtls =
            caseDeductionItemObj.read(cdiKey);
          // check for the business status if active then will in-activate it
          if (!BUSINESSSTATUS.INACTIVE
            .equalsIgnoreCase(caseDeductionItemDtls.businessStatus)) {
            // update the status to inactive
            caseDeductionItemDtls.businessStatus = BUSINESSSTATUS.INACTIVE;
            // calling the method to modify the case deduction item details
            caseDeductionItemObj.modify(cdiKey, caseDeductionItemDtls);
          }
        }
      }
    }
  }

  /**
   * Displays list of changes to outstanding amount for a given liability
   */
  @Override
  public BDMOASExternalLiabilityHistoryDetailsList
    listLiabilityHistory(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    final BDMOASExternalLiabilityHistoryDetailsList historyList =
      new BDMOASExternalLiabilityHistoryDetailsList();

    final BDMExternalLiabilityHistorySearchKey historyKey =
      new BDMExternalLiabilityHistorySearchKey();
    historyKey.externalLiabilityID = key.externalLiabilityID;

    final BDMOASExternalLiabilityHistorySearchDtlsList historyResultList =
      BDMExternalLiabilityHistoryFactory.newInstance()
        .readExternalLiabilityHistoryForRecipient(historyKey);

    BDMOASExternalLiabilityHistorySearchDtls historyResult;
    BDMOASExternalLiabilityHistoryDetails historyDetails;
    final UsersKey userKey = new UsersKey();
    final UserAccess userAccessObj = UserAccessFactory.newInstance();
    UserFullname userFullName;
    Calendar calendar;
    // list is sorted in ascending order, we will process it in descending order
    for (int i = historyResultList.dtls.size() - 1; i >= 0; i--) {
      historyResult = historyResultList.dtls.get(i);
      historyDetails = new BDMOASExternalLiabilityHistoryDetails();
      historyDetails.dtls.assign(historyResult);
      historyDetails.changeReason = historyResult.changeReason;
      historyDetails.liabilityStatusCode = historyResult.liabilityStatusCode;

      userKey.userName = historyResult.userName;

      userFullName = userAccessObj.getFullName(userKey);
      historyDetails.dtls.userFullName = userFullName.fullname;

      calendar = historyResult.creationDateTime.getCalendar();
      historyDetails.dtls.modifyDate = new Date(calendar);
      historyList.dtls.add(historyDetails);
    }

    return historyList;
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

    NotFoundIndicator nfIndicator = new NotFoundIndicator();

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
      // check for the outstanding balance if zero then return the control
      if (liabilityDtls.outstandingAmount.isZero()) {
        // return the control
        return;
      }

      // set the outstanding amount less the FC amount
      final double newOutstandingAmount =
        liabilityDtls.outstandingAmount.getValue()
          - iliAmount.amount.getValue();

      liabilityDtls.outstandingAmount = new Money(newOutstandingAmount);

      // check if the liability outstanding amount is zero then set the status
      // to repaid
      if (liabilityDtls.outstandingAmount.isZero()) {
        // set the status to repaid
        liabilityDtls.liabilityStatusCode = BDMEXTERNALLIABILITYSTATUS.REPAID;
      }

      // calling the method to modify the external liability record
      bdmExternalLiabilityObj.modify(extLbyKey, liabilityDtls);

      // check if the liability outstanding amount is zero then inactive the
      // case deduction item
      if (liabilityDtls.outstandingAmount.isZero()) {

        // look for participant deduction linked to the external liability
        final BDMParticipantDeductionItem bdmParticipantDeductionItemObj =
          BDMParticipantDeductionItemFactory.newInstance();
        // new instance of input keys
        nfIndicator = new NotFoundIndicator();
        final BDMPDIExternalLiabilityKey participantDeductionKey =
          new BDMPDIExternalLiabilityKey();
        participantDeductionKey.externalLiabilityID =
          bdmCDIDtls.externalLiabilityID;
        final BDMParticipantDeductionItemKey bdmPDIKey =
          bdmParticipantDeductionItemObj.readByExternalLiability(nfIndicator,
            participantDeductionKey);

        if (!nfIndicator.isNotFound()) {
          // read the original participant deduction
          final BDMParticipantDeductionItemDtls participantDeductionItemDetails =
            bdmParticipantDeductionItemObj.read(bdmPDIKey);
          // check for the end date if already end dated then don't update
          if (!participantDeductionItemDetails.endDate.isZero()) {
            // return the control
            return;
          }

          // modify participant deductions
          final BDMParticipantDeductionModifyDetails modifyDeductionDetails =
            new BDMParticipantDeductionModifyDetails();
          modifyDeductionDetails.amount =
            participantDeductionItemDetails.amount;
          modifyDeductionDetails.rate = participantDeductionItemDetails.rate;
          modifyDeductionDetails.selBenefitIDs =
            Long.toString(caseDeductionItemDtls.caseID);
          modifyDeductionDetails.startDate =
            participantDeductionItemDetails.startDate;
          modifyDeductionDetails.bdmParticipantDeductionItemID =
            bdmPDIKey.bdmParticipantDeductionItemID;
          // set the end date to current date
          // this is the only change in the input data
          modifyDeductionDetails.endDate = Date.getCurrentDate();

          // calling method to modify participant deductions
          maintainParticipantDeductionObj
            .modifyParticipantDeduction(modifyDeductionDetails);
        }

        // inactive case deduction
        // entity instance
        final CaseDeductionItem caseDeductionItemObj =
          CaseDeductionItemFactory.newInstance();
        // new reference
        final CaseDeductionItemKey cdiKey = new CaseDeductionItemKey();
        // assign the case deduction item ID
        cdiKey.caseDeductionItemID =
          caseDeductionItemDtls.caseDeductionItemID;
        // calling the method to read the case deductions
        final CaseDeductionItemDtls caseDeductionItemDetails =
          caseDeductionItemObj.read(cdiKey);
        // check for the business status if active then will in-activate it
        if (!BUSINESSSTATUS.INACTIVE
          .equalsIgnoreCase(caseDeductionItemDetails.businessStatus)) {
          // update the status to inactive
          caseDeductionItemDtls.businessStatus = BUSINESSSTATUS.INACTIVE;
          // calling the method to modify the case deduction item details
          caseDeductionItemObj.modify(cdiKey, caseDeductionItemDtls);
        }
      }
    }
  }

  /**
   * This method is used to derive deduction details for DOJ deductions.
   */
  @Override
  public GenILIDOJDeductionDetails getDOJDeductionDetails(
    final CaseDeductionItemDtls caseDeductionItemDtls,
    final FinancialComponentDtls fcDtls,
    final FinCompCoverPeriod fcCoverPeriod, final FCAmount fcAmount,
    final TotalDeductionAmount totalDeductionAmount)
    throws AppException, InformationalException {

    // new instance of return object
    final GenILIDOJDeductionDetails genILIDOJDeductionDetails =
      new GenILIDOJDeductionDetails();

    // retrieve the running net amount
    final Money runningNetAmount =
      MaintainInstructionLineItemFactory.newInstance().getAmountForComponent(
        totalDeductionAmount.deductibleAmountList,
        new RulesObjectiveID()).amount;

    // read the BDMCaseDeductionItem record using the CaseDeductionItemID to get
    // the linked ExternalLiabilityID
    final BDMExternalLiability bdmExternalLiabilityObj =
      BDMExternalLiabilityFactory.newInstance();
    BDMExternalLiabilityKey bdmExternalLiabilityKey =
      new BDMExternalLiabilityKey();
    final BDMCaseDeductionItemKey bdmCDIKey = new BDMCaseDeductionItemKey();
    bdmCDIKey.caseDeductionItemID = caseDeductionItemDtls.caseDeductionItemID;
    // calling the read method
    final BDMCaseDeductionItemDtls bdmCaseDeductionItemDtls =
      BDMCaseDeductionItemFactory.newInstance().read(bdmCDIKey);

    // red the input file received from DOJ using BDMExternalLiabilityDOJData
    // table which is linked with external liability ID
    bdmExternalLiabilityKey.externalLiabilityID =
      bdmCaseDeductionItemDtls.externalLiabilityID;
    final DOJDeductionForILIDetails bdmExternalLiabilityDOJData =
      bdmExternalLiabilityObj
        .readDOJDeductionDetails(bdmExternalLiabilityKey);

    // per payment deduction amount
    // Maximum dollar amount that can be deducted from the debtor’s running net
    // amount
    final Money perPaymentDeductionAmount =
      bdmExternalLiabilityDOJData.deductionAmount;

    // debtor fixed amount
    // Amount the debtor must receive of the running net amount
    final Money debtorFixedAmount =
      bdmExternalLiabilityDOJData.deductionMinPayAmount;

    // hold back percentage
    // Percent of payment amount the debtor must receive of the running net
    // amount
    final double holdBackPercentage =
      bdmExternalLiabilityDOJData.deductionRate;

    // read all unprocessed payment instructions for benefit case
    // This will contain all the benefit payments and high priority deductions
    // issued to CRA (Non DOJ Deductions) OR DOJ (DOJ Deductions)
    //
    // On the first run it will only have benefit payment and high priority non
    // DOJ deductions payment instructions
    final BDMInstructionLineItemDA bdmInstructionLineItemDA =
      BDMInstructionLineItemDAFactory.newInstance();
    final SearchAllUnLineItemsForDOJDedKey searchAllUnLineItemsForDOJDedKey =
      new SearchAllUnLineItemsForDOJDedKey();
    searchAllUnLineItemsForDOJDedKey.caseID = fcDtls.caseID;
    searchAllUnLineItemsForDOJDedKey.iliCategory =
      ILICATEGORY.PAYMENTINSTRUCTION;
    searchAllUnLineItemsForDOJDedKey.iliStatusUnprocess =
      ILISTATUS.UNPROCESSED;
    final SearchAllUnLineItemsForDOJDedDetailsList unprocessedPaymentInstructionILIList =
      bdmInstructionLineItemDA
        .searchAllUnLineItemsForDOJDed(searchAllUnLineItemsForDOJDedKey);

    // get total benefit payment for all the NOMINEES
    Money totalBenefitPaymentForAllNominees = Money.kZeroMoney;
    // iterate the unprocessed payment instruction ILIs
    for (final SearchAllUnLineItemsForDOJDedDetails unprocessedPaymentInstructionILIDetails : unprocessedPaymentInstructionILIList.dtls) {

      // check for ILI type and skip deduction and tax payment ILIs to calculate
      // the total benefit payment for all the NOMINEES
      if (!(ILITYPE.DEDUCTIONITEM
        .equals(unprocessedPaymentInstructionILIDetails.instructLineItemType)
        || ILITYPE.TAXPAYMENT.equals(
          unprocessedPaymentInstructionILIDetails.instructLineItemType))) {
        // add the total benefit payment here
        totalBenefitPaymentForAllNominees =
          new Money(totalBenefitPaymentForAllNominees.getValue()
            + unprocessedPaymentInstructionILIDetails.amount.getValue());
      }
    }

    // new reference
    BDMInstructionLineItemKey bdmInstructionLineItemKey;
    // new entity instance
    final BDMInstructionLineItem bdmInstructionLineItemObj =
      BDMInstructionLineItemFactory.newInstance();
    // iterate the unprocessed payment instruction ILIs and except DOJ
    // deductions set the deduction indicator to true
    for (final SearchAllUnLineItemsForDOJDedDetails details : unprocessedPaymentInstructionILIList.dtls) {

      // check for the deduction type
      if (details.preDOJDeductionInd == false
        && !(BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS
          .equals(details.deductionType)
          || BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_FEES
            .equals(details.deductionType))
        && CREDITDEBIT.CREDIT.equals(details.creditDebitType)) {

        // new instance of BDM ILI key
        bdmInstructionLineItemKey = new BDMInstructionLineItemKey();
        // populate the key object
        bdmInstructionLineItemKey.instructLineItemID =
          details.instructLineItemID;
        // calling the method to read the BDM ILI record
        final BDMInstructionLineItemDtls bdmInstructionLineItemDtls =
          bdmInstructionLineItemObj.read(bdmInstructionLineItemKey);
        // set the indicator and version
        bdmInstructionLineItemDtls.preDOJDeductionInd = Boolean.TRUE;
        // modify the record
        bdmInstructionLineItemObj.modify(bdmInstructionLineItemKey,
          bdmInstructionLineItemDtls);
      }
    }

    // In rare scenarios there could be multiple deduction FCs for same NOMINEE
    // but for different components when there are gaps in component assignment.
    // Check if there is at least one deduction ILI related to this DOJ
    // Liability
    // has been created for this NOMINEE and if there is one means that we did
    // try to max out that NOMINEE for this DOJ Case Deduction.
    //
    // Checking if the deduction already exists for the current NOMINEE for this
    // current liability
    boolean deductionExistsForSameNomineeForCurrentLiability = Boolean.FALSE;
    // iterate the unprocessed ILIs
    for (final SearchAllUnLineItemsForDOJDedDetails details : unprocessedPaymentInstructionILIList.dtls) {
      // check for the Case NOMINEE and External Liability ID
      if (details.caseNomineeID == fcDtls.caseNomineeID
        && details.externalLiabilityID == bdmExternalLiabilityDOJData.externalLiabilityID) {
        // set the boolean indicator to true
        deductionExistsForSameNomineeForCurrentLiability = Boolean.TRUE;
        // if record exists then break the loop
        break;
      }
    }

    // variable to hold derived deduction amount for the deduction DOJ liability
    Money derivedDeductionAmountForDOJLiability = Money.kZeroMoney;
    // variable to hold DEDUCTABLE ration for this deduction liability
    double deductableRatio = CuramConst.gkDoubleZero;
    // The rest of scenarios when there are multiple NOMINEES against multiple
    // components, lets calculate the DEDUCTABLE amount that can be applied for
    // this NOMINEE.
    //
    // Check for the indicator if the deduction exists for the same NOMINEE for
    // this current liability if no then will derive the deduction amount
    if (!deductionExistsForSameNomineeForCurrentLiability) {

      // instance to hold the amount deducted for other NOMINEE on this
      // liability
      Money amountDeductedForOtherNomineeForCurrentLiability =
        Money.kZeroMoney;
      // check if the amount is already deducted for Other NOMINEE for this
      // liability iterate the unprocessed ILIs
      for (final SearchAllUnLineItemsForDOJDedDetails unprocessedPaymentInstructionILIDetails : unprocessedPaymentInstructionILIList.dtls) {

        // compare the external liability ID only
        if (unprocessedPaymentInstructionILIDetails.externalLiabilityID == bdmExternalLiabilityDOJData.externalLiabilityID) {
          // sum the deducted amount for other NOMINEE on this liability
          amountDeductedForOtherNomineeForCurrentLiability = new Money(
            amountDeductedForOtherNomineeForCurrentLiability.getValue()
              + unprocessedPaymentInstructionILIDetails.amount.getValue());
        }
      }

      // read the BDMDeduction table to get the deduction type
      final BDMDeductionKey bdmDeductionKey = new BDMDeductionKey();
      // populate the deduction ID
      bdmDeductionKey.deductionID = caseDeductionItemDtls.deductionID;
      // calling the method to read the BDMDeduction record
      final BDMDeductionDetails bdmDeductionDetails =
        BDMDeductionFactory.newInstance().readByDeductionID(bdmDeductionKey);

      // There could be multiple DOJ Arrears liabilities applied on the same
      // case.
      //
      // Each liability should be given equal weightage so that there is enough
      // remaining amount left for others liabilities to have deductions applied
      // against them.
      //
      // Get list of DOJ Arrears deduction FCs that could be eligible for pay
      // run. Not an accurate way, but since we do have the controller continue
      // with this way.
      // Check for the deduction type
      if (BDMDEDUCTIONTYPE.DOJ_FOE_WITHHOLD_ARREARS
        .equals(bdmDeductionDetails.deductionType)) {

        // Read all Live Financial Components and Unprocessed ILIs for DOJ
        // Arrears and get distinct liability records which are ready for Pay
        // Run
        //
        // This will give the unprocessed records for DOJ Arrears deductions
        // which already passed this flow and amount has been added as per the
        // calculation
        // new instance of input key
        final SearchActiveDOJForPayRunKey searchActiveDOJForPayRunKey =
          new SearchActiveDOJForPayRunKey();
        // populate the key
        searchActiveDOJForPayRunKey.caseID = fcDtls.caseID;
        searchActiveDOJForPayRunKey.finCompStatusCode =
          FINCOMPONENTSTATUS.LIVE;
        searchActiveDOJForPayRunKey.iliStatusUnprocess =
          ILISTATUS.UNPROCESSED;
        searchActiveDOJForPayRunKey.liabilityTypeDOJArrears =
          BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS;
        // calling the method to read all external liability records
        final BDMLiabilityKeyList bdmLiabilityKeyList =
          BDMFinancialComponentDAFactory.newInstance()
            .searchActiveDOJForPayRun(searchActiveDOJForPayRunKey);

        // calculate the total DOJ Arrears outstanding liability on the case
        // variable to hold total DOJ Arrears outstanding liability amount on
        // the case
        Money totalDOJArrearsOutstandingLiabilityAmount = Money.kZeroMoney;
        // iterate the liability key list
        for (final BDMLiabilityKey bdmLiabilityKey : bdmLiabilityKeyList.dtls) {

          // new instance of key
          bdmExternalLiabilityKey = new BDMExternalLiabilityKey();
          // populate the external liability ID
          bdmExternalLiabilityKey.externalLiabilityID =
            bdmLiabilityKey.externalLiabilityID;
          // calling the method to read external liability record
          final BDMExternalLiabilityDtls bdmExternalLiabilityDtls =
            bdmExternalLiabilityObj.read(bdmExternalLiabilityKey);

          // add the outstanding amount here for all the DOJ Arrears liabilities
          // on the case
          totalDOJArrearsOutstandingLiabilityAmount =
            new Money(totalDOJArrearsOutstandingLiabilityAmount.getValue()
              + bdmExternalLiabilityDtls.outstandingAmount.getValue());

          // iterate the unprocessed ILIs
          for (final SearchAllUnLineItemsForDOJDedDetails unprocessedPaymentInstructionILIDetails : unprocessedPaymentInstructionILIList.dtls) {
            // check if the amount is already deducted for DOJ another liability
            if (unprocessedPaymentInstructionILIDetails.externalLiabilityID == bdmLiabilityKey.externalLiabilityID) {
              // if yes then add to the total outstanding amount
              totalDOJArrearsOutstandingLiabilityAmount =
                new Money(totalDOJArrearsOutstandingLiabilityAmount.getValue()
                  + unprocessedPaymentInstructionILIDetails.amount
                    .getValue());
            }
          }
        }

        // get the outstanding amount for this liability existed before this
        // transaction
        final Money prePayRunOutstandingLiabilityAmount =
          new Money(bdmExternalLiabilityDOJData.outstandingAmount.getValue()
            + amountDeductedForOtherNomineeForCurrentLiability.getValue());

        // check if total DOJ Arrears outstanding liability amount is not zero
        // If no zero then will calculate the ration which will be used for the
        // deduction
        if (!totalDOJArrearsOutstandingLiabilityAmount.isZero()) {
          // calculate the DEDUCTABLE ratio
          deductableRatio = prePayRunOutstandingLiabilityAmount.getValue()
            / totalDOJArrearsOutstandingLiabilityAmount.getValue();
        }

        // calculate the deduction amount using DEDUCTABLE ratio
        final Money deductionAmountByDeductableRatio = new Money(
          totalBenefitPaymentForAllNominees.getValue() * deductableRatio);

        // check if deduction amount by ratio is more than zero
        if (deductionAmountByDeductableRatio
          .getValue() > CuramConst.gkDoubleZero) {

          // check if debtor fixed amount exists which debtor must receive of
          // the running net amount
          if (debtorFixedAmount.getValue() > CuramConst.gkDoubleZero) {

            // deduct the debtor fixed amount from calculated deduction amount
            // by ratio and assign to final derived deduction amount for DOJ
            // liability
            derivedDeductionAmountForDOJLiability =
              new Money(deductionAmountByDeductableRatio.getValue()
                - debtorFixedAmount.getValue());

            // check if the derived deduction amount is negative then there will
            // be no deduction
            if (derivedDeductionAmountForDOJLiability.isNegative()) {
              // set the derived deduction amount to zero for DOJ liability
              derivedDeductionAmountForDOJLiability = Money.kZeroMoney;
            }

          } else {
            // check for per payment deduction amount and hold back percentage
            //
            // Per payment deduction is the maximum dollar amount that can be
            // deducted from the debtor’s running net amount
            //
            // Hold back percentage is the percentage of payment amount the
            // debtor must receive of the running net amount
            // calculate the deduction amount by hold back percentage
            final Money deductionAmountByHoldBackPercentage =
              new Money(deductionAmountByDeductableRatio.getValue()
                * holdBackPercentage / 100);

            // check if per payment deduction amount exists
            if (perPaymentDeductionAmount
              .getValue() > CuramConst.gkDoubleZero) {

              // check if per payment deduction which is maximum amount that can
              // be deducted is more than the deduction amount by hold back
              // percentage
              if (perPaymentDeductionAmount
                .getValue() < deductionAmountByHoldBackPercentage
                  .getValue()) {
                // then derived deduction amount for DOJ liability will be the
                // per payment deduction amount
                derivedDeductionAmountForDOJLiability =
                  perPaymentDeductionAmount;

              } else {
                // it will be derived from hold back percentage
                derivedDeductionAmountForDOJLiability =
                  deductionAmountByHoldBackPercentage;
              }

            } else {
              // it will be derived from hold back percentage
              derivedDeductionAmountForDOJLiability =
                deductionAmountByHoldBackPercentage;
            }
          }
        }

        // here the calculation is complete using the input three parameters
        // (Amount per Payment, Debtor Fixed Amount and Hold back Percentage)
        //
        // check if the derived deduction amount is more than the PREPAY run
        // outstanding liability amount then PREPAY run outstanding liability
        // amount will be used as a deduction amount
        if (derivedDeductionAmountForDOJLiability
          .getValue() > prePayRunOutstandingLiabilityAmount.getValue()) {
          // assign the PREPAY run outstanding liability amount as deduction
          // amount
          derivedDeductionAmountForDOJLiability =
            prePayRunOutstandingLiabilityAmount;
        }
      } else {
        // this is for DOJ Fees
        // here the liability amount will be used as a deduction amount. This
        // will not be derived like Arrears.
        derivedDeductionAmountForDOJLiability =
          bdmExternalLiabilityDOJData.liabilityAmount;
      }

      // check for the configured DOJ deduction threshold. If derived deduction
      // amount is less than the deduction threshold then there will be no
      // deduction
      final double minDOJDeductionThresholdAmount =
        Double.parseDouble(Configuration.getProperty(
          EnvVars.BDM_ENV_FINANCIAL_DOJMINIMUM_THRESHOLD_DEDUCTION_AMOUNT));

      // compare the derived deduction with configured threshold DOJ deduction
      // amount
      if (derivedDeductionAmountForDOJLiability
        .getValue() < minDOJDeductionThresholdAmount) {
        // set the deduction amount as zero
        derivedDeductionAmountForDOJLiability = Money.kZeroMoney;

      } else {
        // Once the total deduction amount get calculated, subtract the amount
        // that has already been deducted for other Case NOMINEES
        derivedDeductionAmountForDOJLiability =
          new Money(derivedDeductionAmountForDOJLiability.getValue()
            - amountDeductedForOtherNomineeForCurrentLiability.getValue());

        // compare the derived deduction amount with outstanding liability then
        // use the minimum one
        if (derivedDeductionAmountForDOJLiability
          .getValue() > bdmExternalLiabilityDOJData.outstandingAmount
            .getValue()) {
          // set the outstanding amount as deduction amount
          derivedDeductionAmountForDOJLiability =
            bdmExternalLiabilityDOJData.outstandingAmount;
        }

        // Reduce the amount further down based on running net amount for the
        // NOMINEE
        if (derivedDeductionAmountForDOJLiability
          .getValue() > runningNetAmount.getValue()) {
          // set the deduction amount as running net amount
          derivedDeductionAmountForDOJLiability = runningNetAmount;
        }
      }
    }

    // assign the derived deduction amount
    genILIDOJDeductionDetails.fcAmount =
      derivedDeductionAmountForDOJLiability;
    // assign the DEDUCTABLE ratio
    genILIDOJDeductionDetails.dojDebtRatio = deductableRatio;

    // return the derived deduction details
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
      final CaseNomineeTaxAmountList caseNomineeTaxAmountList =
        new CaseNomineeTaxAmountList();
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
  private long getParticipantDataCaseID(final long participantRoleID)
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

  /**
   * method to create history record
   *
   * @param dtls
   * @param changeReason
   * @throws AppException
   * @throws InformationalException
   */
  private void createLiabilityHistoryRecord(
    final BDMExternalLiabilityDtls dtls, final String changeReason)
    throws AppException, InformationalException {

    // insert history record
    final BDMExternalLiabilityHistoryDtls historyDtls =
      new BDMExternalLiabilityHistoryDtls();

    historyDtls.bdmExternalLiabilityHistoryID =
      UniqueIDFactory.newInstance().getNextID();
    historyDtls.externalLiabilityID = dtls.externalLiabilityID;
    historyDtls.liabilityAmount = dtls.liabilityAmount;
    historyDtls.originalLiabilityAmount = dtls.liabilityAmount;
    historyDtls.originalOutstandingAmount = dtls.outstandingAmount;
    historyDtls.comments = dtls.comments;
    historyDtls.creationDateTime = DateTime.getCurrentDateTime();
    historyDtls.userName = TransactionInfo.getProgramUser();
    historyDtls.liabilityStatusCode = dtls.liabilityStatusCode;
    historyDtls.changeReason = changeReason;
    BDMExternalLiabilityHistoryFactory.newInstance().insert(historyDtls);

  }

  /**
   * This method will be used get the list of over payment cases by external
   * liability ID.
   *
   * @param key - This holds the liability external ID.
   *
   * @return the over payment cases list.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMOASOverPaymentCaseDetailsList
    listOverPaymentCasesByExternalLiabilityID(
      final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    // instance of return object
    BDMOASOverPaymentCaseDetailsList bdmoasOverPaymentCaseDetailsList =
      new BDMOASOverPaymentCaseDetailsList();

    final BDMExternalLiabilityDtls dtls =
      readExternalLiabilityDetailsByID(key);

    // set the indicator to display the cluster based on liability type
    if (BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
      .equalsIgnoreCase(dtls.liabilityTypeCode)
      || BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS
        .equalsIgnoreCase(dtls.liabilityTypeCode)) {

      // read the liability cases by concern role ID
      bdmoasOverPaymentCaseDetailsList =
        listOverPaymentCasesByConcernRoleID(dtls.concernRoleID);

      // create new entity instance
      final BDMOASPenalty BDMOASPenaltyObj =
        BDMOASPenaltyFactory.newInstance();
      try {
        // calling the method to read active penalty record by liability id
        final BDMOASPenaltyDtls BDMOASPenaltyDtls =
          BDMOASPenaltyObj.readActivePenaltyByExternalLiabilityID(key);
        // if record present then it should be pre selected
        bdmoasOverPaymentCaseDetailsList.selectedOverPaymentCaseID =
          BDMOASPenaltyDtls.currentOverPaymentCaseID;

      } catch (final RecordNotFoundException rnfe) {
        // do nothing here
      }

      // set the indicator here to display the penalty list
      bdmoasOverPaymentCaseDetailsList.displayPenaltiesInd = Boolean.TRUE;

      // if OAS NB then will hide the PDC and recovery rate details
      if (BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
        .equalsIgnoreCase(dtls.liabilityTypeCode)) {
        // set the indicator to true
        bdmoasOverPaymentCaseDetailsList.penaltyOASNBInd = Boolean.TRUE;
      }
    }

    // return the over payment cases list
    return bdmoasOverPaymentCaseDetailsList;
  }

  /**
   * This method is used to read external liability details by liability ID.
   *
   * @param key - This holds the external liability ID.
   * @return the liability details.
   * @throws AppException
   * @throws InformationalException
   */
  private BDMExternalLiabilityDtls
    readExternalLiabilityDetailsByID(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    // read the external liability details
    final BDMExternalLiability bdmExternalLiabilityObj =
      curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory
        .newInstance();
    // new instance of key
    final BDMExternalLiabilityKey bdmExtLbyKey =
      new BDMExternalLiabilityKey();
    // populate the external liability ID
    bdmExtLbyKey.externalLiabilityID = key.externalLiabilityID;
    // call read method to read the details
    final BDMExternalLiabilityDtls bdmExternalLiabilityDtls =
      bdmExternalLiabilityObj.read(bdmExtLbyKey);

    // return the details
    return bdmExternalLiabilityDtls;
  }

  /**
   * This method is used to display over payment cases by Wizard State ID
   *
   * @param wizardStateID - This holds the wizard state ID.
   *
   * @return the list of over payment cases.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMOASOverPaymentCaseDetailsList
    listOverPaymentCasesByWizardStateID(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    // instance of return object
    BDMOASOverPaymentCaseDetailsList bdmoasOverPaymentCaseDetailsList =
      new BDMOASOverPaymentCaseDetailsList();

    // read the wizard details
    final WizardPersistentState wizardPersistanceObj =
      new WizardPersistentState();
    // calling the method to read the wizard details by wizard state ID
    final BDMOASExternalLbyWizardSaveStep1Details wizardStateDetails =
      (BDMOASExternalLbyWizardSaveStep1Details) wizardPersistanceObj
        .read(wizardStateID.wizardStateID);

    // read the liability cases by concern role ID
    bdmoasOverPaymentCaseDetailsList = listOverPaymentCasesByConcernRoleID(
      wizardStateDetails.dtls.concernRoleID);

    // return the over payment cases list
    return bdmoasOverPaymentCaseDetailsList;
  }

  /**
   * This method is used to display over payment cases by concern role ID
   *
   * @param concernRoleID - This holds the concern role ID.
   *
   * @return the list of over payment cases.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private BDMOASOverPaymentCaseDetailsList
    listOverPaymentCasesByConcernRoleID(final long concernRoleID)
      throws AppException, InformationalException {

    // instance of return object
    final BDMOASOverPaymentCaseDetailsList bdmoasOverPaymentCaseDetailsList =
      new BDMOASOverPaymentCaseDetailsList();

    // read the liability cases by concern role ID
    // new instance of key
    final SearchCaseKey_fo searchCaseKey = new SearchCaseKey_fo();
    // populate the concern role ID
    searchCaseKey.casesByConcernRoleIDKey.concernRoleID = concernRoleID;
    final BDMSearchCaseDetails1 bdmSearchCaseDetails1 =
      BDMCaseDisplayFactory.newInstance().personSearchCase1(searchCaseKey);

    // new reference
    BDMOASOverPaymentCaseDetails overPaymentCaseDetails;
    // iterate the case details list
    for (final BDMCaseHeaderConcernRoleDetails1 caseDetails : bdmSearchCaseDetails1.caseHeaderConcernRoleDetailsList.dtls) {
      // check for the case type code
      if (CASETYPECODE.LIABILITY
        .equalsIgnoreCase(caseDetails.dtls.caseTypeCode)) {
        // new instance
        overPaymentCaseDetails = new BDMOASOverPaymentCaseDetails();
        // populate the details
        // overPaymentCaseDetails.overPaymentType = caseDetails.caseType;
        overPaymentCaseDetails.caseReference = caseDetails.dtls.caseReference;
        overPaymentCaseDetails.overPaymentCaseID = caseDetails.dtls.caseID;
        overPaymentCaseDetails.startDate = caseDetails.dtls.startDate;
        overPaymentCaseDetails.overPaymentType =
          caseDetails.dtls.productTypeDesc;

        // calling the method to get the original liability amount
        overPaymentCaseDetails.originalAmount =
          getOriginalLiabilityAmount(caseDetails.dtls.caseID);

        // calling the method to get the total outstanding amount
        overPaymentCaseDetails.outstandingAmount = getTotalOutstandingAmount(
          caseDetails.dtls.caseID, overPaymentCaseDetails.originalAmount);

        // calling he method to get the benefit type
        overPaymentCaseDetails.benefitType = CodeTable.getOneItem(
          BDMBENEFITPROGRAMTYPE.TABLENAME, BDMBENEFITPROGRAMTYPE.OASBENEFIT);

        // add to the return list object
        bdmoasOverPaymentCaseDetailsList.dtls.add(overPaymentCaseDetails);
      }
    }

    // check for the size if not zero then will display the list else will
    // display a informational message
    if (bdmoasOverPaymentCaseDetailsList.dtls.size() == CuramConst.gkZero) {
      // set the informational message
      final InformationalManager informationalManager =
        TransactionInfo.getInformationalManager();
      // get the localized string
      final LocalisableString localisableString = new LocalisableString(
        BDMOASEXTERNALLIABILITY.ERR_OVERPAYMENT_CASES_DOES_NOT_EXIST_FOR_PENALTY);
      // add the informational message as warning
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        localisableString, CuramConst.gkEmpty,
        InformationalElement.InformationalType.kWarning,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne, 0);

      // get the warning message
      final String warnings[] =
        informationalManager.obtainInformationalAsString();
      // iterate the list
      for (int loop = 0; loop < warnings.length; loop++) {
        // new instance
        final InformationalMsgDtls informationalMsgDtls =
          new InformationalMsgDtls();
        // assign the message
        informationalMsgDtls.informationMsgTxt = warnings[loop];
        // add to the return list object
        bdmoasOverPaymentCaseDetailsList.msgDtls.dtls
          .addRef(informationalMsgDtls);
      }

    } else {

      // set the indicator to display the over payment cases list
      bdmoasOverPaymentCaseDetailsList.displayOverPaymentListInd =
        Boolean.TRUE;
    }

    // return the over payment cases list
    return bdmoasOverPaymentCaseDetailsList;
  }

  /**
   * This method is used to display over payment history details by external
   * liability ID.
   *
   * @param key - This holds the external liability ID
   *
   * @return the over payment history details for liability
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMOASOverPaymentHistoryDetailsList
    listOverPaymentHistoryByExternalLiabilityID(
      final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    // instance of return object
    final BDMOASOverPaymentHistoryDetailsList bdmoasOverPaymentHistoryDetailsList =
      new BDMOASOverPaymentHistoryDetailsList();

    // read the external liability details
    final BDMExternalLiability bdmExternalLiabilityObj =
      curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory
        .newInstance();
    final BDMExternalLiabilityKey bdmExtLbyKey =
      new BDMExternalLiabilityKey();
    bdmExtLbyKey.externalLiabilityID = key.externalLiabilityID;
    final BDMExternalLiabilityDtls dtls =
      bdmExternalLiabilityObj.read(bdmExtLbyKey);

    // entity instance
    final BDMOASPenalty BDMOASPenaltyObj = BDMOASPenaltyFactory.newInstance();
    // calling the method to read the penalty history details
    final BDMOASPenaltyDtlsList BDMOASPenaltyDtlsList =
      BDMOASPenaltyObj.readPenaltyHistoryByExternalLiabilityID(bdmExtLbyKey);
    // new reference
    BDMOASOverPaymentHistoryDetails bdmoasOverPaymentHistoryDetails;
    // iterate the list
    for (final BDMOASPenaltyDtls BDMOASPenaltyDtls : BDMOASPenaltyDtlsList.dtls) {
      // new instance
      bdmoasOverPaymentHistoryDetails = new BDMOASOverPaymentHistoryDetails();
      // assign the values
      bdmoasOverPaymentHistoryDetails.currentCase =
        readCaseReference(BDMOASPenaltyDtls.currentOverPaymentCaseID);
      bdmoasOverPaymentHistoryDetails.previousCase =
        readCaseReference(BDMOASPenaltyDtls.previousOverPaymentCaseID);
      bdmoasOverPaymentHistoryDetails.user = BDMOASPenaltyDtls.updatedBy;
      bdmoasOverPaymentHistoryDetails.overPaymentDate =
        BDMOASPenaltyDtls.updatedOn;
      // add to the list object
      bdmoasOverPaymentHistoryDetailsList.dtls
        .add(bdmoasOverPaymentHistoryDetails);
    }

    try {
      // read the current over payment case ID
      final BDMOASPenaltyDtls activeBDMOASPenaltyDtls =
        BDMOASPenaltyObj.readActivePenaltyByExternalLiabilityID(bdmExtLbyKey);
      // assign it here
      bdmoasOverPaymentHistoryDetailsList.overPaymentCaseRefName =
        getCaseTypeWithCaseReference(
          activeBDMOASPenaltyDtls.currentOverPaymentCaseID);
    } catch (final RecordNotFoundException rnfe) {
      // do nothing
    }
    // check for the liability type code if related to penalty then will display
    // the over payment details
    if (BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS_NB
      .equalsIgnoreCase(dtls.liabilityTypeCode)
      || BDMEXTERNALLIABILITYTYPE.PENALTIES_OAS
        .equalsIgnoreCase(dtls.liabilityTypeCode)) {
      // set the indicator to true
      bdmoasOverPaymentHistoryDetailsList.displayOverPaymentDetailsInd =
        Boolean.TRUE;
    }

    // return the over payment history details
    return bdmoasOverPaymentHistoryDetailsList;
  }

  /**
   * This method is used to format the case reference with product type.
   *
   * @param caseID - This holds the case ID
   * @return the formatted string
   * @throws AppException
   * @throws InformationalException
   */
  private String getCaseTypeWithCaseReference(final long caseID)
    throws AppException, InformationalException {

    // instance of return object
    final StringBuffer caseDetails = new StringBuffer();
    // read the PDC details
    final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
    // populate the case ID
    productDeliveryKey.caseID = caseID;
    // calling the method to read the PDC details
    final ProductDeliveryDtls productDeliveryDtls =
      ProductDeliveryFactory.newInstance().read(productDeliveryKey);

    // get the product type description and append
    caseDetails.append(CodeTable.getOneItem(PRODUCTTYPE.TABLENAME,
      productDeliveryDtls.productType));
    caseDetails.append(CuramConst.gkSpace);
    caseDetails.append(CuramConst.gkDash);
    caseDetails.append(CuramConst.gkSpace);

    // append case reference
    caseDetails.append(readCaseReference(caseID));

    // return the formatted string
    return caseDetails.toString();
  }

  /**
   * This method is used to read the case reference.
   *
   * @param caseID - This holds the case ID.
   * @return the case reference.
   * @throws AppException
   * @throws InformationalException
   */
  private String readCaseReference(final long caseID)
    throws AppException, InformationalException {

    // read case header for the case reference
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    // populate the case ID
    caseHeaderKey.caseID = caseID;
    // read case header details
    final CaseHeaderDtls caseHeaderDtls =
      CaseHeaderFactory.newInstance().read(caseHeaderKey);

    // return the case reference
    return caseHeaderDtls.caseReference;
  }

  /**
   * This method is used to get the original liability amount.
   *
   * @param overPaymentCaseID - This holds the over payment case ID.
   * @return the original liability amount
   *
   * @throws AppException
   * @throws InformationalException
   */
  private Money getOriginalLiabilityAmount(final long overPaymentCaseID)
    throws AppException, InformationalException {

    // read the product delivery case
    final ProductDelivery productDeliveryObj =
      ProductDeliveryFactory.newInstance();
    // new instance of key
    final ProductDeliveryKey productDeliveryKey = new ProductDeliveryKey();
    // populate the key
    productDeliveryKey.caseID = overPaymentCaseID;
    // read the PDC details
    final ProductDeliveryDtls productDeliveryDtls =
      productDeliveryObj.read(productDeliveryKey);

    // this will be used for original liability amount
    double originalLiabilityAmount = 0;
    // check for the type code
    if (PRODUCTTYPE.BENEFITOVERPAYMENT
      .equalsIgnoreCase(productDeliveryDtls.productType)) {

      // new instance of case relationship key
      final CaseRelationshipCaseIDKey relKey =
        new CaseRelationshipCaseIDKey();
      // populate the over payment case ID
      relKey.caseID = overPaymentCaseID;
      // calling the method to read the case relationship by case ID
      final CaseRelationshipDtlsList relationshipList =
        CaseRelationshipFactory.newInstance().searchByCaseID(relKey);

      // new instance of input key
      final CaseIDKey caseIDKey = new CaseIDKey();
      // populate the case ID
      caseIDKey.caseID = overPaymentCaseID;

      // If there are no related cases then this over payment was created
      // following the reversal of a payment received which had been refunded
      if (relationshipList.dtls.size() == 0) {
        // assign the liability amount here
        originalLiabilityAmount =
          readRefundOverpaymentCaseTabDetail(caseIDKey).overpaymentAmount
            .getValue();
      } else {
        // Use the normal read
        originalLiabilityAmount = OverpaymentEvidenceFactory.newInstance()
          .readOverpaymentTabDetail(caseIDKey).overpaymentAmount.getValue();
      }

    } else if (PRODUCTTYPE.PAYMENTCORRECTION
      .equalsIgnoreCase(productDeliveryDtls.productType)) {

      // new instance of case header key
      final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
      // assign the case ID
      caseHeaderKey.caseID = overPaymentCaseID;

      // calling the method to get the list of payment correction by case ID
      final List<PaymentCorrectionEvidence> pcEvidenceList =
        paymentCorrectionEvidenceDAO.searchByCaseID(caseHeaderKey);

      // iterate the payment correction evidence list
      for (final PaymentCorrectionEvidence pcEvidence : pcEvidenceList) {
        // check for the over payment
        if (pcEvidence.getReassessmentResult().getCode()
          .equals(REASSESSMENTRESULTEntry.OVERPAYMENT.getCode())) {
          // add and assign the amount
          originalLiabilityAmount =
            originalLiabilityAmount + pcEvidence.getAmount().getValue();
        }
      }
    }
    // return the amount here
    return new Money(originalLiabilityAmount);
  }

  /**
   * Reads the relevant information for display in the context panel of an
   * over payment case created as a result of reversing a payment received
   * which has been refunded.
   * This is private OOTB FinancialCaseTab.readRefundOverpaymentCaseTabDetail()
   * method so copied here to use it to calculate total and outstanding
   * liability amount
   *
   * @param key Contains details of the payment correction case.
   */
  private OverpaymentCaseTabDetails readRefundOverpaymentCaseTabDetail(
    final CaseIDKey key) throws AppException, InformationalException {

    final OverpaymentCaseTabDetails overpaymentCaseTabDtls =
      new OverpaymentCaseTabDetails();

    // Build SQL
    final StringBuffer finalBuf = new StringBuffer();
    final StringBuffer selectBuf = new StringBuffer();
    final StringBuffer intoBuf = new StringBuffer();
    final StringBuffer fromBuf = new StringBuffer();
    final StringBuffer whereBuf = new StringBuffer();

    selectBuf.append("SELECT CaseHeader.caseReference, ");
    selectBuf.append("CaseHeader.statusCode, ");
    selectBuf.append("Product.name, ");
    selectBuf
      .append("CaseHeader.concernRoleID, ConcernRole.concernRoleName, ");
    selectBuf.append("ConcernRole.primaryAlternateID, ");
    selectBuf.append("ConcernRole.primaryPhoneNumberID, ");
    selectBuf.append("EmailAddress.emailAddress, Address.addressData, ");
    selectBuf.append("OrgObjectLink.orgObjectLinkID, ");
    selectBuf.append("OverpaymentEvidence.overpaymentAmount, ");
    selectBuf.append("OverpaymentEvidence.fromDate, ");
    selectBuf.append("OverpaymentEvidence.toDate, ");
    selectBuf.append("OverpaymentEvidence.reassessmentDate ");

    intoBuf.append("INTO :caseReference, ");
    intoBuf.append(":caseStatus, ");
    intoBuf.append(":productName, :concernRoleID, ");
    intoBuf.append(":concernRoleName, :clientPrimaryAlternateID, ");
    intoBuf.append(":clientPhoneNumberID, :clientEmailAddress, ");
    intoBuf.append(":clientAddressData, :orgObjectLinkID, ");
    intoBuf.append(":overpaymentAmount, :fromDate, :toDate, ");
    intoBuf.append(":reassessmentDate ");

    fromBuf.append("FROM OVERPAYMENTEVIDENCE, ORGOBJECTLINK, ");
    fromBuf.append("PRODUCTDELIVERY, PRODUCT, ");
    fromBuf.append("CONCERNROLE LEFT OUTER JOIN EMAILADDRESS ON ");
    fromBuf.append("CONCERNROLE.PRIMARYEMAILADDRESSID= ");
    fromBuf.append("EMAILADDRESS.EMAILADDRESSID ");
    fromBuf.append("LEFT OUTER JOIN ADDRESS ON ");
    fromBuf.append("CONCERNROLE.PRIMARYADDRESSID= ADDRESS.ADDRESSID, ");
    fromBuf.append("CASEHEADER ");

    whereBuf.append("WHERE OVERPAYMENTEVIDENCE.CASEID= :caseID AND ");
    whereBuf.append("CASEHEADER.CASEID = OVERPAYMENTEVIDENCE.CASEID AND ");
    whereBuf
      .append("CONCERNROLE.CONCERNROLEID = CASEHEADER.CONCERNROLEID AND ");
    whereBuf.append("ORGOBJECTLINK.ORGOBJECTLINKID= ");
    whereBuf.append("CASEHEADER.OWNERORGOBJECTLINKID AND ");
    whereBuf.append("PRODUCTDELIVERY.CASEID= CASEHEADER.CASEID AND ");
    whereBuf.append("PRODUCT.PRODUCTID= PRODUCTDELIVERY.PRODUCTID");

    finalBuf.append(selectBuf);
    finalBuf.append(intoBuf);
    finalBuf.append(fromBuf);
    finalBuf.append(whereBuf);

    // Call dynamic SQL API to execute SQL
    final CuramValueList<OverpaymentCaseTabDetails> curamValueList =
      DynamicDataAccess.executeNsMulti(OverpaymentCaseTabDetails.class, key,
        false, true, finalBuf.toString());

    overpaymentCaseTabDtls.assign(curamValueList.item(0));

    return overpaymentCaseTabDtls;
  }

  /**
   * This method will be used to get the total outstanding amount for Correction
   * Cases.
   *
   * @param overPaymentCaseID - This holds the over payment case ID.
   * @param overPaidAmount - This holds the over paid amount.
   *
   * @return the total outstanding amount.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private Money getTotalOutstandingAmount(final long overPaymentCaseID,
    final Money overPaidAmount) throws AppException, InformationalException {

    // instance of return object
    Money totalOutstandingAmount = Money.kZeroMoney;
    // null check here
    if (overPaidAmount.isZero()) {
      // return the control
      return totalOutstandingAmount;
    }

    // Read the ILI related tab details
    // new instance of key
    final ILICaseID iLICaseID = new ILICaseID();
    // populate the over payment case ID
    iLICaseID.caseID = overPaymentCaseID;
    // calling the method to search by Over Payment case ID
    final ILITabDetailList iLITabDetailList =
      InstructionLineItemFactory.newInstance().searchByCaseID(iLICaseID);

    double allocationsTotal = CuramConst.gkDoubleZero;
    double reversalsTotal = CuramConst.gkDoubleZero;
    double writeOffsTotal = CuramConst.gkDoubleZero;
    boolean cancelledInd = Boolean.FALSE;

    // iterate the ILI list
    for (final ILITabDetail iLITabDetail : iLITabDetailList.dtls) {

      // get the ILI type and category
      final String iLIType = iLITabDetail.instructionLineItemType;
      final String iLICategory = iLITabDetail.instructLineItemCategory;

      // Check the ILI list for write offs
      if (iLICategory.equals(ILICATEGORY.WRITEOFFINSTRUCTION)
        && iLIType.equals(ILITYPE.WRITEOFF)) {

        writeOffsTotal += iLITabDetail.amount.getValue();

        // Check the ILI list for reversals

      } else if (iLICategory.equals(ILICATEGORY.REVERSALINSTRUCTION)
        && (iLIType.equals(ILITYPE.UNALLOCATEDREVERSAL)
          || iLIType.equals(ILITYPE.ALLOCATEDREVERSAL))) {

        reversalsTotal += iLITabDetail.amount.getValue();

        /*
         * Check the ILI list for allocations. Allocations are made up of
         * deductions and amounts allocated from payment received.
         */

      } else if (iLICategory
        .equals(ILICATEGORY.PAYMENTINSTRUCTIONFORDEDUCTION)
        && iLIType.equals(ILITYPE.DEDUCTIONPAYMENT)) {

        allocationsTotal += iLITabDetail.amount.getValue();

      } else if (iLICategory.equals(ILICATEGORY.PAYMENTRECEIVEDINSTRUCTION)
        && iLIType.equals(ILITYPE.ALLOCATEDPMTRECVD)) {

        allocationsTotal += iLITabDetail.amount.getValue();

      } else if (iLICategory.equals(ILICATEGORY.LIABILITYINSTRUCTION)
        && iLIType.equals(ILITYPE.BENEFITOVERPAYMENT)
        && iLITabDetail.statusCode.equals(ILISTATUS.REVERSED)) {

        cancelledInd = Boolean.TRUE;
        /*
         * We can stop this calculation if we know the liability was cancelled.
         * Set these totals to zero so we can display the correct water mark
         * later.
         */
        allocationsTotal = 0;
        reversalsTotal = 0;
        writeOffsTotal = 0;
        break;
      }
    }

    /*
     * The total payment is made up of any allocations (payments received
     * and deductions) plus any write offs, minus any reversals (reversals
     * are in effect additional liabilities on the case).
     */
    double totalPayments = allocationsTotal + writeOffsTotal - reversalsTotal;
    /*
     * If we have over allocated to this liability the total payments amount
     * will be larger than the original liability amount. In this situation
     * we should reduce the total payment to match the original amount.
     */
    if (totalPayments > overPaidAmount.getValue()) {
      totalPayments = overPaidAmount.getValue();
    }

    // Add these totals to the return object
    if (cancelledInd) {
      totalOutstandingAmount = new Money(0);
    } else {
      totalOutstandingAmount =
        new Money(overPaidAmount.getValue() - totalPayments);
    }

    // return the total outstanding amount
    return totalOutstandingAmount;
  }
}
