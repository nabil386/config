package curam.ca.gc.bdm.sl.maintainparticipantdeduction.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMPARTICIPANTDEDUCTIONLINKSTATUS;
import curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityDtls;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.fact.BDMParticipantCaseDeductionItemLinkFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.fact.BDMParticipantDeductionCaseLinkFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.fact.BDMParticipantDeductionItemFactory;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.intf.BDMParticipantCaseDeductionItemLink;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.intf.BDMParticipantDeductionCaseLink;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.intf.BDMParticipantDeductionItem;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMExternalLiabilityLinkStatusKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantCaseDeductionItemLinkDtls;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantCaseDeductionItemLinkKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionActiveCaseDeductionDetails;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionActiveCaseDeductionDetailsList;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionActiveCaseDeductionsKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionCaseLinkDtls;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionCaseLinkDtlsList;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionCaseLinkKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemDtls;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemKey;
import curam.ca.gc.bdm.entity.financial.bdmparticipantdeduction.struct.BDMParticipantDeductionItemStatusKey;
import curam.ca.gc.bdm.sl.fact.MaintainFinancialComponentFactory;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionCaseLinkDetails;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionCaseLinkDetailsList;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionCaseLinkList;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionCreateDetails;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionLinkBenefitDetails;
import curam.ca.gc.bdm.sl.financial.maintainparticipantdeduction.struct.BDMParticipantDeductionModifyDetails;
import curam.ca.gc.bdm.sl.intf.MaintainFinancialComponent;
import curam.ca.gc.bdm.sl.productdelivery.impl.MaintainDeductionDetails;
import curam.codetable.BUSINESSSTATUS;
import curam.codetable.CASEDEDHISTORYSTATUS;
import curam.codetable.CASENOMINEESTATUS;
import curam.codetable.DEDUCTIONCATEGORYCODE;
import curam.codetable.DEDUCTIONITEMTYPE;
import curam.core.facade.struct.ThirdPartyDetails;
import curam.core.fact.CaseDeductionHistoryFactory;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.MaintainDeductionItemsFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.CaseDeductionItem;
import curam.core.intf.MaintainDeductionItems;
import curam.core.sl.entity.fact.DeductionFactory;
import curam.core.sl.entity.struct.DeductionDtls;
import curam.core.sl.entity.struct.DeductionKey;
import curam.core.sl.fact.CaseNomineeFactory;
import curam.core.sl.fact.CaseStatusModeFactory;
import curam.core.sl.struct.CaseNomineeAndStatusDetails;
import curam.core.sl.struct.CaseNomineeAndStatusDetailsList;
import curam.core.sl.struct.CaseNomineeCaseIDAndStatusKey;
import curam.core.struct.CancelDeductionItemKey;
import curam.core.struct.CaseDeductionHistoryDtls;
import curam.core.struct.CaseDeductionHistoryDtlsList;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemID;
import curam.core.struct.CaseDeductionItemIDVersionNo;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseStartDate;
import curam.core.struct.FCCaseID;
import curam.core.struct.FCCoverDate;
import curam.core.struct.IndicatorStruct;
import curam.core.struct.MaintainDeductionItemDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.StringList;
import curam.util.type.UniqueID;
import java.util.List;

public class MaintainParticipantDeduction implements
  curam.ca.gc.bdm.sl.maintainparticipantdeduction.intf.MaintainParticipantDeduction {

  @Inject
  MaintainDeductionDetails maintainDeductionDetailsObj;

  public MaintainParticipantDeduction() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Create a Participant Deduction Item and links it to selected benefit cases
   */
  @Override
  public BDMParticipantDeductionItemKey createParticipantDeduction(
    final BDMParticipantDeductionCreateDetails createDetails)
    throws AppException, InformationalException {

    // assign create details
    final BDMParticipantDeductionItemDtls participantDeductionDtls =
      new BDMParticipantDeductionItemDtls();
    participantDeductionDtls.assign(createDetails);

    participantDeductionDtls.bdmParticipantDeductionItemID =
      UniqueID.nextUniqueID();
    participantDeductionDtls.businessStatus = BUSINESSSTATUS.INACTIVE;

    // insert into BDMParticipantDeductionItem
    BDMParticipantDeductionItemFactory.newInstance()
      .insert(participantDeductionDtls);

    // iterate through selected benefits and create case link
    if (!StringUtil.isNullOrEmpty(createDetails.selBenefitIDs)) {

      final StringList benefitIDs = StringUtil.delimitedText2StringList(
        createDetails.selBenefitIDs, CuramConst.gkTabDelimiterChar);

      for (final String benefitID : benefitIDs) {
        final BDMParticipantDeductionLinkBenefitDetails benefitLinkDetails =
          new BDMParticipantDeductionLinkBenefitDetails();
        benefitLinkDetails.caseID = Long.parseLong(benefitID);
        benefitLinkDetails.bdmParticipantDeductionItemID =
          participantDeductionDtls.bdmParticipantDeductionItemID;

        linkBenefitCase(benefitLinkDetails);
      }
    }

    final BDMParticipantDeductionItemKey participantDeductionKey =
      new BDMParticipantDeductionItemKey();
    participantDeductionKey.bdmParticipantDeductionItemID =
      participantDeductionDtls.bdmParticipantDeductionItemID;

    return participantDeductionKey;
  }

  /**
   * Links the provided participant deduction to the benefit case and creates
   * the case deduction
   */
  @Override
  public BDMParticipantDeductionCaseLinkKey linkBenefitCase(
    final BDMParticipantDeductionLinkBenefitDetails benefitDetails)
    throws AppException, InformationalException {

    // assign details
    final BDMParticipantDeductionCaseLinkDtls caseLinkDtls =
      new BDMParticipantDeductionCaseLinkDtls();
    caseLinkDtls.assign(benefitDetails);

    caseLinkDtls.bdmParticipantDeductionCaseLinkID = UniqueID.nextUniqueID();
    caseLinkDtls.linkStatus = BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;

    // insert into BDMParticipantDeductionCaseLink
    BDMParticipantDeductionCaseLinkFactory.newInstance().insert(caseLinkDtls);

    final BDMParticipantDeductionCaseLinkKey caseLinkKey =
      new BDMParticipantDeductionCaseLinkKey();
    caseLinkKey.bdmParticipantDeductionCaseLinkID =
      caseLinkDtls.bdmParticipantDeductionCaseLinkID;

    // create the case deduction
    createCaseDeductions(caseLinkKey);

    final BDMParticipantDeductionItemKey bdmPDIKey =
      new BDMParticipantDeductionItemKey();
    bdmPDIKey.bdmParticipantDeductionItemID =
      caseLinkDtls.bdmParticipantDeductionItemID;

    // if the participant deduction is active, activate the case deductions
    final BDMParticipantDeductionItemDtls pdiDtls =
      BDMParticipantDeductionItemFactory.newInstance().read(bdmPDIKey);
    if (pdiDtls.businessStatus.equals(BUSINESSSTATUS.ACTIVE)) {
      activateCaseDeductions(caseLinkKey);
    }

    return caseLinkKey;
  }

  /**
   * Creates case deductions for a particular case based off the participant
   * deduction details
   */
  @Override
  public void
    createCaseDeductions(final BDMParticipantDeductionCaseLinkKey key)
      throws AppException, InformationalException {

    // get case link details
    final BDMParticipantDeductionCaseLinkDtls caseLinkDtls =
      BDMParticipantDeductionCaseLinkFactory.newInstance().read(key);
    final BDMParticipantDeductionItemKey participantDeductionItemKey =
      new BDMParticipantDeductionItemKey();

    // get participant deduction details
    participantDeductionItemKey.bdmParticipantDeductionItemID =
      caseLinkDtls.bdmParticipantDeductionItemID;
    final BDMParticipantDeductionItemDtls pcptDeductionDtls =
      BDMParticipantDeductionItemFactory.newInstance()
        .read(participantDeductionItemKey);

    final MaintainDeductionItemDetails deductionDtls =
      new MaintainDeductionItemDetails();
    deductionDtls.startDate = pcptDeductionDtls.startDate;

    // get the case start date
    final CaseHeaderKey chKey = new CaseHeaderKey();
    chKey.caseID = caseLinkDtls.caseID;
    final CaseStartDate caseStartDate =
      CaseHeaderFactory.newInstance().readStartDate(chKey);

    // if the user provided start date is before the case start date, set the
    // deduction start date to the case start date
    if (pcptDeductionDtls.startDate.before(caseStartDate.startDate)) {
      deductionDtls.startDate = caseStartDate.startDate;
    }

    deductionDtls.endDate = pcptDeductionDtls.endDate;

    // fill out deduction details
    deductionDtls.rate = pcptDeductionDtls.rate;
    deductionDtls.amount = pcptDeductionDtls.amount;
    deductionDtls.caseID = caseLinkDtls.caseID;
    deductionDtls.category = pcptDeductionDtls.category;
    deductionDtls.relatedCaseID = pcptDeductionDtls.internalLiabilityID;

    final DeductionKey deductionKey = new DeductionKey();
    deductionKey.deductionID = pcptDeductionDtls.deductionID;

    final DeductionDtls readDeductionDtls =
      DeductionFactory.newInstance().read(deductionKey);

    deductionDtls.actionType = readDeductionDtls.actionType;

    final CaseDeductionItemDtls cdiDtls = new CaseDeductionItemDtls();
    cdiDtls.assign(deductionDtls);
    cdiDtls.deductionName = readDeductionDtls.deductionName;

    deductionDtls.deductionName = readDeductionDtls.deductionName;

    // get third party details based off the deduction
    if (pcptDeductionDtls.category
      .equals(DEDUCTIONCATEGORYCODE.THIRDPARTYDEDUCTION)) {
      final ThirdPartyDetails thirdPartyDetails = maintainDeductionDetailsObj
        .determineThirdPartyAccountDetails(deductionDtls.deductionName);

      deductionDtls.assign(thirdPartyDetails);

      if (pcptDeductionDtls.externalLiabilityID != 0) {
        final BDMExternalLiabilityKey bdmExternalLiabilityKey =
          new BDMExternalLiabilityKey();
        bdmExternalLiabilityKey.externalLiabilityID =
          pcptDeductionDtls.externalLiabilityID;

        // use the external liability's ref number as the account number
        final BDMExternalLiabilityDtls liabilityDtls =
          BDMExternalLiabilityFactory.newInstance()
            .read(bdmExternalLiabilityKey);

        if (!StringUtil.isNullOrEmpty(liabilityDtls.externalRefNumber)) {
          deductionDtls.customerAccNumber = liabilityDtls.externalRefNumber;
        } else {
          deductionDtls.customerAccNumber = liabilityDtls.sysGenRefNumber;
        }
      }

    }

    // calculated the priority for the deduction
    deductionDtls.priority =
      maintainDeductionDetailsObj.calculatePriority(cdiDtls);

    final MaintainDeductionItems maintainDeductionItemsObj =
      MaintainDeductionItemsFactory.newInstance();
    final BDMParticipantCaseDeductionItemLink bdmParticipantCaseDeductionItemLinkObj =
      BDMParticipantCaseDeductionItemLinkFactory.newInstance();

    // if deduction is fixed amount, iterate through case nominees and add
    // deduction and deduction link
    if (!deductionDtls.amount.isZero()) {
      deductionDtls.deductionType = DEDUCTIONITEMTYPE.FIXED;

      final CaseNomineeCaseIDAndStatusKey caseNomineeKey =
        new CaseNomineeCaseIDAndStatusKey();
      caseNomineeKey.caseNomineeCaseIDAndStatusKey.caseID =
        deductionDtls.caseID;
      caseNomineeKey.caseNomineeCaseIDAndStatusKey.nomineeStatus =
        CASENOMINEESTATUS.OPERATIONAL;
      final CaseNomineeAndStatusDetailsList nomineeList = CaseNomineeFactory
        .newInstance().listOperationalNominee(caseNomineeKey);

      final BDMParticipantCaseDeductionItemLinkDtls bdmParticipantCDILinkDtls =
        new BDMParticipantCaseDeductionItemLinkDtls();
      for (final CaseNomineeAndStatusDetails nominee : nomineeList.caseNomineeAndStatusDetailsList) {
        // agaur, ADO#21023 - For fixed deduction, default Nominee at PDC level
        // is selected and passed when creating CDI
        // create deduction
        if (nominee.defaultNomInd) {
          deductionDtls.caseNomineeID = nominee.caseNomineeID;
          maintainDeductionItemsObj.createDeductionItem1(deductionDtls);

          // create the case deduction item link
          bdmParticipantCDILinkDtls.bdmParticipantCaseDeductionItemLinkID =
            UniqueID.nextUniqueID();
          bdmParticipantCDILinkDtls.bdmParticipantDeductionCaseLinkID =
            caseLinkDtls.bdmParticipantDeductionCaseLinkID;
          bdmParticipantCDILinkDtls.caseDeductionItemID =
            deductionDtls.caseDeductionItemID;
          bdmParticipantCDILinkDtls.linkStatus =
            BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;

          bdmParticipantCaseDeductionItemLinkObj
            .insert(bdmParticipantCDILinkDtls);
        }
      }
    }
    // if deduction is variable, add deduction and link
    else {
      // create deduction
      deductionDtls.deductionType = DEDUCTIONITEMTYPE.VARIABLE;
      maintainDeductionItemsObj.createDeductionItem1(deductionDtls);

      // create case deduction item link
      final BDMParticipantCaseDeductionItemLinkDtls bdmParticipantCDILinkDtls =
        new BDMParticipantCaseDeductionItemLinkDtls();
      bdmParticipantCDILinkDtls.bdmParticipantCaseDeductionItemLinkID =
        UniqueID.nextUniqueID();
      bdmParticipantCDILinkDtls.bdmParticipantDeductionCaseLinkID =
        caseLinkDtls.bdmParticipantDeductionCaseLinkID;
      bdmParticipantCDILinkDtls.caseDeductionItemID =
        deductionDtls.caseDeductionItemID;
      bdmParticipantCDILinkDtls.linkStatus =
        BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;

      bdmParticipantCaseDeductionItemLinkObj
        .insert(bdmParticipantCDILinkDtls);

    }

  }

  /**
   * lists BDMParticipantDeductionCaseLinks associated with a given external
   * liability
   */
  @Override
  public BDMParticipantDeductionCaseLinkList

    listLinkedBenefitsByExtLby(final BDMExternalLiabilityKey key)
      throws AppException, InformationalException {

    final BDMExternalLiabilityLinkStatusKey extLbyStatusKey =
      new BDMExternalLiabilityLinkStatusKey();
    extLbyStatusKey.externalLiabilityID = key.externalLiabilityID;
    extLbyStatusKey.linkStatus = BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;

    // gets any current benefits linked to the external lby
    final BDMParticipantDeductionCaseLinkDetailsList benefitsList =
      BDMParticipantDeductionItemFactory.newInstance()
        .searchBenefitsLinkedByExtLby(extLbyStatusKey);

    final BDMParticipantDeductionCaseLinkList bdmParticipantDeductionCaseLinkList =
      new BDMParticipantDeductionCaseLinkList();

    for (final BDMParticipantDeductionCaseLinkDetails benefitDetails : benefitsList.dtls) {

      final BDMParticipantDeductionCaseLinkDetails deductionCaseLinkDetails =
        new BDMParticipantDeductionCaseLinkDetails();

      deductionCaseLinkDetails.assign(benefitDetails);

      bdmParticipantDeductionCaseLinkList.dtls.add(deductionCaseLinkDetails);
    }
    return bdmParticipantDeductionCaseLinkList;

  }

  /**
   * Unlinks a participantDeduction from a case and deactivates the associated
   * case deductions
   */
  @Override
  public void unlinkBenefitCase(final BDMParticipantDeductionCaseLinkKey key)
    throws AppException, InformationalException {

    final BDMParticipantDeductionCaseLink bdmParticipantDeductionCaseLinkObj =
      BDMParticipantDeductionCaseLinkFactory.newInstance();

    final BDMParticipantDeductionCaseLinkDtls dtls =
      bdmParticipantDeductionCaseLinkObj.read(key);

    dtls.linkStatus = BDMPARTICIPANTDEDUCTIONLINKSTATUS.HISTORICAL;

    bdmParticipantDeductionCaseLinkObj.modify(key, dtls);

    // close any associated case deduction items
    closeCaseDeductions(key);

  }

  /**
   * Checks a case deduction item's history to see if it has ever been processed
   *
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public IndicatorStruct
    isCaseDeductionEverProcessed(final CaseDeductionItemKey key)
      throws AppException, InformationalException {

    final IndicatorStruct deductionAppliedInd = new IndicatorStruct();

    // CALL EN CaseDeductionItem.read(INPUT caseDeductionItemID) RETURN
    // cDedItemDtls
    final CaseDeductionItem caseDeductionItemObj =
      CaseDeductionItemFactory.newInstance();
    final CaseDeductionItemDtls caseDeductionItemDtls =
      caseDeductionItemObj.read(key);

    final MaintainFinancialComponent maintainFinancialComponentObj =
      MaintainFinancialComponentFactory.newInstance();
    final FCCaseID fcCaseID = new FCCaseID();
    fcCaseID.caseID = caseDeductionItemDtls.caseID;
    final FCCoverDate fcCoverDate =
      maintainFinancialComponentObj.getLatestCoverPeriodTo(fcCaseID);

    if (!fcCoverDate.coverDate.isZero()) {
      final CaseDeductionItemID cdiKey = new CaseDeductionItemID();
      cdiKey.assign(key);
      final CaseDeductionHistoryDtlsList caseDeductionHistoryDtlsList =
        CaseDeductionHistoryFactory.newInstance()
          .searchByCaseDeductionItemID(cdiKey);

      for (final CaseDeductionHistoryDtls deductionHistory : caseDeductionHistoryDtlsList.dtls) {
        if (deductionHistory.status.equals(CASEDEDHISTORYSTATUS.PROCESSED)
          || deductionHistory.status.equals(CASEDEDHISTORYSTATUS.SKIPPED)) {
          deductionAppliedInd.changeAllIndicator = true;
          break;
        }
      }
    }

    return deductionAppliedInd;
  }

  /**
   * Modifies participant deductions by deactivating deductions that are no
   * longer selected, modifying existing deductions that have been selected, and
   * creating new deductions for newly selected cases
   */
  @Override
  public void modifyParticipantDeduction(
    final BDMParticipantDeductionModifyDetails modifyDetails)
    throws AppException, InformationalException {

    allowMultipleCaseDeductionUpdates();

    final BDMParticipantDeductionItem bdmParticipantDeductionItemObj =
      BDMParticipantDeductionItemFactory.newInstance();

    // get the original deduction details
    final BDMParticipantDeductionItemKey bdmPDIKey =
      new BDMParticipantDeductionItemKey();
    bdmPDIKey.bdmParticipantDeductionItemID =
      modifyDetails.bdmParticipantDeductionItemID;

    final BDMParticipantDeductionItemDtls originalPDIDtls =
      bdmParticipantDeductionItemObj.read(bdmPDIKey);

    final BDMParticipantDeductionItemDtls modifiedPDIDtls =
      new BDMParticipantDeductionItemDtls();
    modifiedPDIDtls.assign(originalPDIDtls);

    // modify the participant deduction
    modifiedPDIDtls.amount = modifyDetails.amount;
    modifiedPDIDtls.rate = modifyDetails.rate;
    modifiedPDIDtls.startDate = modifyDetails.startDate;

    bdmParticipantDeductionItemObj.modify(bdmPDIKey, modifiedPDIDtls);

    // finds all currently linked case deductions
    final BDMParticipantDeductionItemStatusKey deductionItemStatusKey =
      new BDMParticipantDeductionItemStatusKey();
    deductionItemStatusKey.bdmParticipantDeductionItemID =
      bdmPDIKey.bdmParticipantDeductionItemID;
    deductionItemStatusKey.linkStatus =
      BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;

    final BDMParticipantDeductionCaseLinkDtlsList existingCases =
      BDMParticipantDeductionCaseLinkFactory.newInstance()
        .readActiveParticipantDeductions(deductionItemStatusKey);

    if (!StringUtil.isNullOrEmpty(modifyDetails.selBenefitIDs)) {

      // seperate tab delimited list of selected cases into an array
      final List<String> selectedCases = StringUtil.delimitedText2StringList(
        modifyDetails.selBenefitIDs, CuramConst.gkTabDelimiterChar);

      // iterate through existing cases to see which ones were selected again,
      // and which were unselected
      for (final BDMParticipantDeductionCaseLinkDtls existingCase : existingCases.dtls) {
        final String caseIDStr = Long.toString(existingCase.caseID);

        final BDMParticipantDeductionCaseLinkKey caseLinkKey =
          new BDMParticipantDeductionCaseLinkKey();
        caseLinkKey.bdmParticipantDeductionCaseLinkID =
          existingCase.bdmParticipantDeductionCaseLinkID;

        // if an existing case is not in the selected case, it has to be
        // unlinked and deactivated
        if (!selectedCases.contains(caseIDStr)) {

          unlinkBenefitCase(caseLinkKey);
        }
        // if it is in the selected case list, it must be modified
        else {

          // modify case deduction
          modifyCaseDeduction(caseLinkKey, originalPDIDtls);
          // remove from selected cases
          selectedCases.remove(caseIDStr);
        }
      }

      // create new deductions for newly selected cases
      for (final String selectedCase : selectedCases) {
        final BDMParticipantDeductionLinkBenefitDetails benefitDetails =
          new BDMParticipantDeductionLinkBenefitDetails();
        benefitDetails.bdmParticipantDeductionItemID =
          originalPDIDtls.bdmParticipantDeductionItemID;
        benefitDetails.caseID = Long.parseLong(selectedCase);

        linkBenefitCase(benefitDetails);
      }
    }

  }

  /**
   * Modifies case deduction items that are linked to the case
   */
  @Override
  public void modifyCaseDeduction(
    final BDMParticipantDeductionCaseLinkKey key,
    final BDMParticipantDeductionItemDtls originalParticipantDeductionItemDtls)
    throws AppException, InformationalException {

    final BDMParticipantDeductionCaseLinkDtls caseLinkDtls =
      BDMParticipantDeductionCaseLinkFactory.newInstance().read(key);

    final BDMParticipantDeductionItemKey bdmDeductionItemKey =
      new BDMParticipantDeductionItemKey();
    bdmDeductionItemKey.bdmParticipantDeductionItemID =
      caseLinkDtls.bdmParticipantDeductionItemID;

    final BDMParticipantDeductionItemDtls participantDeductionItemDtls =
      BDMParticipantDeductionItemFactory.newInstance()
        .read(bdmDeductionItemKey);

    boolean adjustExistingCaseDeductions = false;

    // compare the original and modified deductions to see how drastic the
    // change is. If the change is only in the rate (and was originally a rate)
    // or in the amount, then the deduction can be modified rather than created
    // new
    if (participantDeductionItemDtls.startDate
      .equals(originalParticipantDeductionItemDtls.startDate)
      && participantDeductionItemDtls.endDate
        .equals(originalParticipantDeductionItemDtls.endDate)
      && (!participantDeductionItemDtls.amount.isZero()
        && !originalParticipantDeductionItemDtls.amount.isZero()
        || participantDeductionItemDtls.rate != 0
          && originalParticipantDeductionItemDtls.rate != 0)) {
      adjustExistingCaseDeductions = true;
    }

    // search for all currently linked deductions on this case
    final BDMParticipantDeductionActiveCaseDeductionsKey activeCaseDeductionKey =
      new BDMParticipantDeductionActiveCaseDeductionsKey();
    activeCaseDeductionKey.bdmParticipantDeductionCaseLinkID =
      caseLinkDtls.bdmParticipantDeductionCaseLinkID;
    activeCaseDeductionKey.linkStatus =
      BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;

    final BDMParticipantDeductionActiveCaseDeductionDetailsList activeCaseDeductions =
      BDMParticipantCaseDeductionItemLinkFactory.newInstance()
        .searchActiveCaseDeductions(activeCaseDeductionKey);

    final MaintainDeductionItems maintainDeductionItemsObj =
      MaintainDeductionItemsFactory.newInstance();
    final CaseDeductionItem cdiObj = CaseDeductionItemFactory.newInstance();
    final CaseDeductionItemKey cdiKey = new CaseDeductionItemKey();

    // iterate through all active deductions
    for (final BDMParticipantDeductionActiveCaseDeductionDetails activeDeduction : activeCaseDeductions.dtls) {

      cdiKey.caseDeductionItemID = activeDeduction.caseDeductionItemID;

      // if the deduction can be adjusted, check it has never been processed as
      // well
      if (adjustExistingCaseDeductions) {

        final IndicatorStruct deductionAppliedInd =
          isCaseDeductionEverProcessed(cdiKey);
        final CaseDeductionItemID cdiIDKey = new CaseDeductionItemID();
        cdiIDKey.caseDeductionItemID = cdiKey.caseDeductionItemID;

        // set modification details
        final MaintainDeductionItemDetails deductionItemDetails =
          maintainDeductionItemsObj.readDeductionItem(cdiIDKey);
        deductionItemDetails.amount = participantDeductionItemDtls.amount;
        deductionItemDetails.rate = participantDeductionItemDtls.rate;

        // if a deduction has been applied, deactivate the old deduction and
        // create a new one
        if (deductionAppliedInd.changeAllIndicator) {

          // close and unlink the current deduction
          final BDMParticipantCaseDeductionItemLinkKey cdiLinkKey =
            new BDMParticipantCaseDeductionItemLinkKey();
          cdiLinkKey.bdmParticipantCaseDeductionItemLinkID =
            activeDeduction.bdmParticipantCaseDeductionItemLinkID;
          closeSingleCaseDeduction(cdiLinkKey);

          // create new deduction
          maintainDeductionItemsObj
            .createDeductionItem1(deductionItemDetails);

          // activate deductions if the participant is active
          if (originalParticipantDeductionItemDtls.businessStatus
            .equals(BUSINESSSTATUS.ACTIVE)) {
            cdiKey.caseDeductionItemID =
              deductionItemDetails.caseDeductionItemID;
            final CaseDeductionItemDtls cdiDtls = cdiObj.read(cdiKey);

            final CaseDeductionItemIDVersionNo cdiVersionNoKey =
              new CaseDeductionItemIDVersionNo();
            cdiVersionNoKey.caseDeductionItemID = cdiDtls.caseDeductionItemID;
            cdiVersionNoKey.versionNo = cdiDtls.versionNo;
            maintainDeductionItemsObj.activateDeduction(cdiVersionNoKey);
          }

          // create a new case deduction item link
          final BDMParticipantCaseDeductionItemLinkDtls bdmParticipantCDILinkDtls =
            new BDMParticipantCaseDeductionItemLinkDtls();

          bdmParticipantCDILinkDtls.bdmParticipantCaseDeductionItemLinkID =
            UniqueID.nextUniqueID();
          bdmParticipantCDILinkDtls.bdmParticipantDeductionCaseLinkID =
            caseLinkDtls.bdmParticipantDeductionCaseLinkID;
          bdmParticipantCDILinkDtls.caseDeductionItemID =
            deductionItemDetails.caseDeductionItemID;
          bdmParticipantCDILinkDtls.linkStatus =
            BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;

          BDMParticipantCaseDeductionItemLinkFactory.newInstance()
            .insert(bdmParticipantCDILinkDtls);
        }
        // modify the deduction if it is a simple enough change and hasn't been
        // processed
        else {
          maintainDeductionItemsObj.modifyDeduction(deductionItemDetails);
        }

      } else {
        // close and unlink the current deduction
        final BDMParticipantCaseDeductionItemLinkKey cdiLinkKey =
          new BDMParticipantCaseDeductionItemLinkKey();
        cdiLinkKey.bdmParticipantCaseDeductionItemLinkID =
          activeDeduction.bdmParticipantCaseDeductionItemLinkID;

        closeSingleCaseDeduction(cdiLinkKey);

      }
    }

    // create case deductions for the given case
    if (!adjustExistingCaseDeductions) {
      createCaseDeductions(key);

      // activate deductions if the participant is active
      if (originalParticipantDeductionItemDtls.businessStatus
        .equals(BUSINESSSTATUS.ACTIVE)) {
        activateCaseDeductions(key);
      }
    }
  }

  /**
   * Activates the participant deduction
   */
  @Override
  public void
    activateParticipantDeduction(final BDMParticipantDeductionItemKey key)
      throws AppException, InformationalException {

    allowMultipleCaseDeductionUpdates();

    // modify participant deduction
    final BDMParticipantDeductionItem bdmPDIObj =
      BDMParticipantDeductionItemFactory.newInstance();
    final BDMParticipantDeductionItemDtls pcptDtls = bdmPDIObj.read(key);

    pcptDtls.businessStatus = BUSINESSSTATUS.ACTIVE;

    bdmPDIObj.modify(key, pcptDtls);

    // gets the currently linked cases on the participant deduction
    final BDMParticipantDeductionItemStatusKey bdmPDIStatusKey =
      new BDMParticipantDeductionItemStatusKey();
    bdmPDIStatusKey.bdmParticipantDeductionItemID =
      pcptDtls.bdmParticipantDeductionItemID;
    bdmPDIStatusKey.linkStatus = BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;
    final BDMParticipantDeductionCaseLinkDtlsList caseLinkDtlsList =
      BDMParticipantDeductionCaseLinkFactory.newInstance()
        .readActiveParticipantDeductions(bdmPDIStatusKey);

    final BDMParticipantDeductionCaseLinkKey caseLinkKey =
      new BDMParticipantDeductionCaseLinkKey();

    // activate case deductions for each case
    for (final BDMParticipantDeductionCaseLinkDtls caseLinkDtls : caseLinkDtlsList.dtls) {
      caseLinkKey.bdmParticipantDeductionCaseLinkID =
        caseLinkDtls.bdmParticipantDeductionCaseLinkID;

      activateCaseDeductions(caseLinkKey);
    }

  }

  /**
   * Activates all current case deduction items associated with case link
   *
   * @param caseLinkKey
   */
  @Override
  public void activateCaseDeductions(
    final BDMParticipantDeductionCaseLinkKey caseLinkKey)
    throws AppException, InformationalException {

    // search for any current case deductions belong to the given case
    final BDMParticipantDeductionActiveCaseDeductionsKey activeDeductionsKey =
      new BDMParticipantDeductionActiveCaseDeductionsKey();
    activeDeductionsKey.bdmParticipantDeductionCaseLinkID =
      caseLinkKey.bdmParticipantDeductionCaseLinkID;
    activeDeductionsKey.linkStatus =
      BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;

    final BDMParticipantDeductionActiveCaseDeductionDetailsList activeCaseDeductions =
      BDMParticipantCaseDeductionItemLinkFactory.newInstance()
        .searchActiveCaseDeductions(activeDeductionsKey);

    final CaseDeductionItem cdiObj = CaseDeductionItemFactory.newInstance();
    final MaintainDeductionItems maintainDeductionItemsObj =
      MaintainDeductionItemsFactory.newInstance();

    final CaseDeductionItemKey cdiKey = new CaseDeductionItemKey();
    // iterate through all current CDIs and activate them
    for (final BDMParticipantDeductionActiveCaseDeductionDetails activeCaseDeduction : activeCaseDeductions.dtls) {
      cdiKey.caseDeductionItemID = activeCaseDeduction.caseDeductionItemID;
      final CaseDeductionItemDtls cdiDtls = cdiObj.read(cdiKey);

      final CaseDeductionItemIDVersionNo activateKey =
        new CaseDeductionItemIDVersionNo();

      activateKey.caseDeductionItemID = cdiDtls.caseDeductionItemID;
      activateKey.versionNo = cdiDtls.versionNo;
      maintainDeductionItemsObj.activateDeduction(activateKey);
    }

  }

  /**
   * Deactivates the participant deduction
   */
  @Override
  public void
    deactivateParticipantDeduction(final BDMParticipantDeductionItemKey key)
      throws AppException, InformationalException {

    allowMultipleCaseDeductionUpdates();

    // deactivate the participant deduction
    final BDMParticipantDeductionItem bdmPDIObj =
      BDMParticipantDeductionItemFactory.newInstance();
    final BDMParticipantDeductionItemDtls pcptDtls = bdmPDIObj.read(key);

    pcptDtls.businessStatus = BUSINESSSTATUS.INACTIVE;

    bdmPDIObj.modify(key, pcptDtls);

    // find all current case links
    final BDMParticipantDeductionItemStatusKey activeCaseDeductionsKey =
      new BDMParticipantDeductionItemStatusKey();
    activeCaseDeductionsKey.bdmParticipantDeductionItemID =
      key.bdmParticipantDeductionItemID;
    activeCaseDeductionsKey.linkStatus =
      BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;
    final BDMParticipantDeductionCaseLinkDtlsList activeParticipantDeductions =
      BDMParticipantDeductionCaseLinkFactory.newInstance()
        .readActiveParticipantDeductions(activeCaseDeductionsKey);

    final IndicatorStruct deletedUnusedInd = new IndicatorStruct();
    final BDMParticipantDeductionCaseLinkKey caseLinkKey =
      new BDMParticipantDeductionCaseLinkKey();

    for (final BDMParticipantDeductionCaseLinkDtls caseLinkDtls : activeParticipantDeductions.dtls) {
      caseLinkKey.bdmParticipantDeductionCaseLinkID =
        caseLinkDtls.bdmParticipantDeductionCaseLinkID;
      deactivateCaseDeductions(caseLinkKey, deletedUnusedInd);
    }

  }

  /**
   * Deactivates deduction associated with given case
   *
   * @param caseLinkDtls
   * @param deletedUnusedInd
   */
  @Override
  public void deactivateCaseDeductions(
    final BDMParticipantDeductionCaseLinkKey caseLinkKey,
    final IndicatorStruct deletedUnusedInd)
    throws AppException, InformationalException {

    // search for any current case deductions belong to the given case
    final BDMParticipantDeductionActiveCaseDeductionsKey activeDeductionsKey =
      new BDMParticipantDeductionActiveCaseDeductionsKey();
    activeDeductionsKey.bdmParticipantDeductionCaseLinkID =
      caseLinkKey.bdmParticipantDeductionCaseLinkID;
    activeDeductionsKey.linkStatus =
      BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;

    final BDMParticipantDeductionActiveCaseDeductionDetailsList activeCaseDeductions =
      BDMParticipantCaseDeductionItemLinkFactory.newInstance()
        .searchActiveCaseDeductions(activeDeductionsKey);

    final CaseDeductionItemKey cdiKey = new CaseDeductionItemKey();

    // iterate through all current CDIs and deactivate/cancel them
    for (final BDMParticipantDeductionActiveCaseDeductionDetails activeCaseDeduction : activeCaseDeductions.dtls) {

      cdiKey.caseDeductionItemID = activeCaseDeduction.caseDeductionItemID;

      deactivateSingleCaseDeduction(cdiKey, deletedUnusedInd);
    }
  }

  /**
   * Searches for all the case deduction items linked to the given case and
   * deactivates/cancels the deductions
   */
  @Override
  public void
    closeCaseDeductions(final BDMParticipantDeductionCaseLinkKey key)
      throws AppException, InformationalException {

    // search for all active case deduction items linked to the case
    final BDMParticipantDeductionActiveCaseDeductionsKey activeCaseDeductionsKey =
      new BDMParticipantDeductionActiveCaseDeductionsKey();
    activeCaseDeductionsKey.bdmParticipantDeductionCaseLinkID =
      key.bdmParticipantDeductionCaseLinkID;
    activeCaseDeductionsKey.linkStatus =
      BDMPARTICIPANTDEDUCTIONLINKSTATUS.CURRENT;
    final BDMParticipantDeductionActiveCaseDeductionDetailsList activeCaseDeductions =
      BDMParticipantCaseDeductionItemLinkFactory.newInstance()
        .searchActiveCaseDeductions(activeCaseDeductionsKey);

    // deactivate all current case deductions
    final IndicatorStruct deleteUnusedInd = new IndicatorStruct();
    deleteUnusedInd.changeAllIndicator = true;
    deactivateCaseDeductions(key, deleteUnusedInd);

    final BDMParticipantCaseDeductionItemLinkKey bdmCDIKey =
      new BDMParticipantCaseDeductionItemLinkKey();

    final BDMParticipantCaseDeductionItemLink bdmCDIObj =
      BDMParticipantCaseDeductionItemLinkFactory.newInstance();

    // change link status to HISTORICAL for all current case deductions
    for (final BDMParticipantDeductionActiveCaseDeductionDetails activeCaseDeduction : activeCaseDeductions.dtls) {
      bdmCDIKey.bdmParticipantCaseDeductionItemLinkID =
        activeCaseDeduction.bdmParticipantCaseDeductionItemLinkID;

      final BDMParticipantCaseDeductionItemLinkDtls bdmCDIDtls =
        bdmCDIObj.read(bdmCDIKey);

      bdmCDIDtls.linkStatus = BDMPARTICIPANTDEDUCTIONLINKSTATUS.HISTORICAL;

      bdmCDIObj.modify(bdmCDIKey, bdmCDIDtls);

    }
  }

  /**
   * Deactivates a single case deduction item
   */
  @Override
  public void deactivateSingleCaseDeduction(
    final CaseDeductionItemKey caseDeductionItemKey,
    final IndicatorStruct deletedUnusedInd)
    throws AppException, InformationalException {

    final CaseDeductionItem cdiObj = CaseDeductionItemFactory.newInstance();
    final MaintainDeductionItems maintainDeductionItemsObj =
      MaintainDeductionItemsFactory.newInstance();

    this.allowMultipleCaseDeductionUpdates();

    final CaseDeductionItemDtls cdiDtls = cdiObj.read(caseDeductionItemKey);

    // cancel deduction if indicator is true
    if (deletedUnusedInd.changeAllIndicator) {

      // check if deduction has been processed
      final IndicatorStruct deductionAppliedInd =
        isCaseDeductionEverProcessed(caseDeductionItemKey);

      // if the deduction has been processed, only deactivate, do not cancel
      if (deductionAppliedInd.changeAllIndicator) {
        final CaseDeductionItemIDVersionNo deactivateKey =
          new CaseDeductionItemIDVersionNo();

        deactivateKey.caseDeductionItemID = cdiDtls.caseDeductionItemID;
        deactivateKey.versionNo = cdiDtls.versionNo;

        maintainDeductionItemsObj.deactivateDeduction(deactivateKey);
      }
      // cancel deduction if it has not been processed before
      else {
        final CancelDeductionItemKey cancelKey = new CancelDeductionItemKey();

        cancelKey.caseDeductionItemID = cdiDtls.caseDeductionItemID;
        cancelKey.versionNo = cdiDtls.versionNo;

        maintainDeductionItemsObj.cancelDeductionItem(cancelKey);
      }

    }
    // else, deactivate deduction
    else {
      final CaseDeductionItemIDVersionNo deactivateKey =
        new CaseDeductionItemIDVersionNo();

      deactivateKey.caseDeductionItemID = cdiDtls.caseDeductionItemID;
      deactivateKey.versionNo = cdiDtls.versionNo;

      maintainDeductionItemsObj.deactivateDeduction(deactivateKey);
    }

  }

  /**
   * Closes a single case deduction item
   */
  @Override
  public void closeSingleCaseDeduction(
    final BDMParticipantCaseDeductionItemLinkKey caseDeductionItemLinkKey)
    throws AppException, InformationalException {

    final BDMParticipantCaseDeductionItemLinkKey bdmCDIKey =
      new BDMParticipantCaseDeductionItemLinkKey();

    final BDMParticipantCaseDeductionItemLink bdmCDIObj =
      BDMParticipantCaseDeductionItemLinkFactory.newInstance();

    bdmCDIKey.bdmParticipantCaseDeductionItemLinkID =
      caseDeductionItemLinkKey.bdmParticipantCaseDeductionItemLinkID;

    // read the deduction details
    final BDMParticipantCaseDeductionItemLinkDtls bdmCDIDtls =
      bdmCDIObj.read(bdmCDIKey);

    final CaseDeductionItemKey cdiKey = new CaseDeductionItemKey();
    cdiKey.caseDeductionItemID = bdmCDIDtls.caseDeductionItemID;

    final IndicatorStruct deleteUnusedInd = new IndicatorStruct();
    deleteUnusedInd.changeAllIndicator = true;

    // deactivate the deduction
    deactivateSingleCaseDeduction(cdiKey, deleteUnusedInd);

    // unlink the deduction item
    bdmCDIDtls.linkStatus = BDMPARTICIPANTDEDUCTIONLINKSTATUS.HISTORICAL;

    bdmCDIObj.modify(bdmCDIKey, bdmCDIDtls);

    maintainDeductionDetailsObj
      .resequencePriorities(cdiKey.caseDeductionItemID);

  }

  /**
   * Disables FC regeneration from checking if a case is in DPP status
   *
   * @throws AppException
   * @throws InformationalException
   */
  private void allowMultipleCaseDeductionUpdates()
    throws AppException, InformationalException {

    CaseStatusModeFactory.newInstance()
      .setDisableRegenFCsCaseStatusDPPCheck(true);

  }

}
