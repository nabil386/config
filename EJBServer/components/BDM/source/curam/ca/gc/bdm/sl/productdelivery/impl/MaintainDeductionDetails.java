package curam.ca.gc.bdm.sl.productdelivery.impl;

import curam.ca.gc.bdm.entity.bdmcasedeductionitem.fact.BDMCaseDeductionItemFactory;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.intf.BDMCaseDeductionItem;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemDtls;
import curam.ca.gc.bdm.entity.bdmcasedeductionitem.struct.BDMCaseDeductionItemKey;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDetails;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMExternalLiabilityDOJDataFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMExternalLiabilityDOJData;
import curam.ca.gc.bdm.entity.financial.struct.BDMExternalLiabilityDOJDataDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMExternalLiabilityDOJDataKey;
import curam.ca.gc.bdm.entity.subclass.casedeductionitem.fact.BDMCaseDeductionItemDAFactory;
import curam.ca.gc.bdm.entity.subclass.casedeductionitem.struct.BDMCaseIDNameStatusKey;
import curam.ca.gc.bdm.facade.productdelivery.struct.BDMDOJDeductionDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMEXTERNALLIABILITY;
import curam.ca.gc.bdm.sl.struct.BDMCaseDeductionItemObligationIDSuffixDetails;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.DEDUCTIONNAME;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.struct.ReadDeductionKey;
import curam.core.facade.struct.ThirdPartyDeductionActivationStruct;
import curam.core.facade.struct.ThirdPartyDetails;
import curam.core.fact.CaseDeductionItemFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.CaseDeductionItem;
import curam.core.sl.entity.fact.DeductionFactory;
import curam.core.sl.entity.fact.DeductionProductLinkFactory;
import curam.core.sl.entity.struct.DeductionDtls;
import curam.core.sl.entity.struct.DeductionIDAndProductIDKey;
import curam.core.sl.entity.struct.DeductionNameStatus;
import curam.core.sl.entity.struct.Priority;
import curam.core.struct.CaseDeductionItemDtls;
import curam.core.struct.CaseDeductionItemDtlsList;
import curam.core.struct.CaseDeductionItemKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ProductDeliveryKey;
import curam.core.struct.ProductIDDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.NotFoundIndicator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class MaintainDeductionDetails implements
  curam.ca.gc.bdm.sl.productdelivery.intf.MaintainDeductionDetails {

  /**
   * Calculates the priority of the given deduction
   *
   * @param deductionName
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public int calculatePriority(final CaseDeductionItemDtls details)
    throws AppException, InformationalException {

    // the priority of the deduction on the product, multiplied by the given
    // multiplier
    final int basePriority =
      getBasePriority(details.caseID, details.deductionName);

    final int finalPriority = calculateSubPriority(details, basePriority, 0L);

    return finalPriority;
  }

  /**
   * Calculates the priority of the given deduction
   *
   * @param deductionName
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public int calculatePriority(final CaseDeductionItemDtls details,
    final BDMExternalLiabilityKey extLbyKey)
    throws AppException, InformationalException {

    // the priority of the deduction on the product, multiplied by the given
    // multiplier
    final int basePriority =
      getBasePriority(details.caseID, details.deductionName);

    final int finalPriority = calculateSubPriority(details, basePriority,
      extLbyKey.externalLiabilityID);

    return finalPriority;
  }

  @Override
  public int getBasePriority(final long caseID, final String deductionName)
    throws AppException, InformationalException {

    // get the product ID details based off the case
    final ProductDeliveryKey pdKey = new ProductDeliveryKey();
    pdKey.caseID = caseID;
    final ProductIDDetails productIDDetails =
      curam.core.fact.ProductDeliveryFactory.newInstance()
        .readProductID(pdKey);

    // get the deduction details based off the deduction name
    final DeductionNameStatus deductionNameStatusKey =
      new DeductionNameStatus();
    deductionNameStatusKey.deductionName = deductionName;
    deductionNameStatusKey.recordStatus = RECORDSTATUS.NORMAL;
    final DeductionDtls deductionDtls = DeductionFactory.newInstance()
      .readActiveDeductionByName(deductionNameStatusKey);

    // determine the priority based off deduction and product ID
    final DeductionIDAndProductIDKey dplKey =
      new DeductionIDAndProductIDKey();
    dplKey.deductionID = deductionDtls.deductionID;
    dplKey.productID = productIDDetails.productID;
    final Priority priorityDtls = DeductionProductLinkFactory.newInstance()
      .readPriorityByDeductionAndProductID(dplKey);

    return priorityDtls.priority * BDMConstants.kPriorityMultiplier;
  }

  /**
   * Given the base priority of the deduction, compare it to the other existing
   * deductions on the case
   *
   * @param deductionName
   * @param caseID
   * @param basePriority
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private int calculateSubPriority(final CaseDeductionItemDtls details,
    final int basePriority, final long externalLiabilityID)
    throws AppException, InformationalException {

    int finalPriority = basePriority;

    // lists all current deductions of the same type on the case, in ascending
    // priority order
    final BDMCaseIDNameStatusKey bdmCaseIDNameStatusKey =
      new BDMCaseIDNameStatusKey();
    bdmCaseIDNameStatusKey.caseID = details.caseID;
    bdmCaseIDNameStatusKey.deductionName = details.deductionName;
    bdmCaseIDNameStatusKey.statusCode = RECORDSTATUS.DEFAULTCODE;

    // returns list of case deduction items sorted by priority (ascending)
    final CaseDeductionItemDtlsList caseDeductionItemDtlsList =
      BDMCaseDeductionItemDAFactory.newInstance()
        .searchByCaseIDNameStatus(bdmCaseIDNameStatusKey);

    // before iterating through, remove the current deduction if this is a
    // modify action
    if (details.caseDeductionItemID != 0) {
      final Predicate<CaseDeductionItemDtls> condition =
        dtls -> dtls.caseDeductionItemID == details.caseDeductionItemID;
      caseDeductionItemDtlsList.dtls.removeIf(condition);
    }

    // transform deduction item to use for comparison
    final BDMCaseDeductionItemObligationIDSuffixDetails deduction1 =
      new BDMCaseDeductionItemObligationIDSuffixDetails();

    deduction1.dtls = details;

    // if the deduction is DOJ, add the obligation ID suffix to the struct as it
    // is used for priority ordering
    if (details.deductionName.equals(DEDUCTIONNAME.DOJ_ARREARS)
      || details.deductionName.equals(DEDUCTIONNAME.DOJ_FEES)) {
      final BDMExternalLiabilityDOJData dojDataObj =
        BDMExternalLiabilityDOJDataFactory.newInstance();

      final BDMExternalLiabilityDOJDataKey dojKey =
        new BDMExternalLiabilityDOJDataKey();
      dojKey.externalLiabilityID = externalLiabilityID;

      final BDMExternalLiabilityDOJDataDtls deduction1DOJDtls =
        dojDataObj.read(dojKey);

      deduction1.obligationIDSuffix = deduction1DOJDtls.obligationIDSuffix;
    }

    // transform the list of case deduction items to use for comparison
    final List<BDMCaseDeductionItemObligationIDSuffixDetails> transformCaseDeductionItemList =
      transformCaseDeductionItemList(caseDeductionItemDtlsList);

    final BDMCaseDeductionItemPriorityComparator comparator =
      new BDMCaseDeductionItemPriorityComparator();

    for (final BDMCaseDeductionItemObligationIDSuffixDetails existingDeductionDtls : transformCaseDeductionItemList) {

      // if the deduction is higher priority than the current one in the list
      // comparator will return negative value if its priority should be a lower
      // integer value (ie. is higher priority)
      if (comparator.compare(deduction1, existingDeductionDtls) < 0) {

        return finalPriority;
      }
      // if the deduction is lower priority or equal, continue
      else {
        finalPriority = existingDeductionDtls.dtls.priority + 1;
      }

    }

    return finalPriority;
  }

  /**
   * Given a particular deduction, resequence all of the deductions in the same
   * category so that there are no gaps in priorities
   *
   * @param caseDeductionitemID
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void resequencePriorities(final long caseDeductionitemID)
    throws AppException, InformationalException {

    final CaseDeductionItem cdiObj = CaseDeductionItemFactory.newInstance();

    final CaseDeductionItemKey key = new CaseDeductionItemKey();
    key.caseDeductionItemID = caseDeductionitemID;
    final CaseDeductionItemDtls dtls = cdiObj.read(key);

    // get list of all deductions on this case with the same name
    final BDMCaseIDNameStatusKey bdmCaseIDNameStatusKey =
      new BDMCaseIDNameStatusKey();
    bdmCaseIDNameStatusKey.caseID = dtls.caseID;
    bdmCaseIDNameStatusKey.deductionName = dtls.deductionName;
    bdmCaseIDNameStatusKey.statusCode = RECORDSTATUS.DEFAULTCODE;
    final CaseDeductionItemDtlsList caseDeductionItemDtlsList =
      BDMCaseDeductionItemDAFactory.newInstance()
        .searchByCaseIDNameStatus(bdmCaseIDNameStatusKey);

    // starting priority
    int priorityToSet;
    final int basePriority =
      priorityToSet = getBasePriority(dtls.caseID, dtls.deductionName);
    final int maxPriority =
      basePriority + BDMConstants.kPriorityMultiplier - 1;

    for (final CaseDeductionItemDtls cdiDtls : caseDeductionItemDtlsList.dtls) {

      // if the priority is the max priority or the casedeductionitem has
      // reached the max priority, break out of the for loop
      if (priorityToSet == maxPriority || cdiDtls.priority == maxPriority) {
        break;
      }
      // if the priority is not correct, then modify its priority
      if (cdiDtls.priority != priorityToSet) {
        key.caseDeductionItemID = cdiDtls.caseDeductionItemID;
        cdiDtls.priority = priorityToSet;
        cdiObj.modify(key, cdiDtls);
      }
      priorityToSet++;
    }

    // no further action required if we have maximized all priorities
    if (priorityToSet == maxPriority) {
      return;
    }

    // filter the list so only case deduction items with max priority exist
    final Predicate<CaseDeductionItemDtls> predicate =
      cdiDtls -> cdiDtls.priority != maxPriority;
    caseDeductionItemDtlsList.dtls.removeIf(predicate);

    final List<BDMCaseDeductionItemObligationIDSuffixDetails> transformCaseDeductionItemList =
      transformCaseDeductionItemList(caseDeductionItemDtlsList);

    Collections.sort(transformCaseDeductionItemList,
      new BDMCaseDeductionItemPriorityComparator());

    // set the priorities for each item (to optimize use of every priority) and
    // stop when complete or when we reach max priority
    for (final BDMCaseDeductionItemObligationIDSuffixDetails cdiDtls : transformCaseDeductionItemList) {

      if (priorityToSet == maxPriority) {
        break;
      }
      key.caseDeductionItemID = cdiDtls.dtls.caseDeductionItemID;
      cdiDtls.dtls.priority = priorityToSet;
      cdiObj.modify(key, cdiDtls.dtls);
      priorityToSet++;
    }

  }

  /**
   * Transforms a list of case deduction items to a list of
   * BDMCaseDeductionItemObligationIDSuffixDetails for use in sorting
   *
   * @param caseDeductionItemDtlsList
   */
  private List<BDMCaseDeductionItemObligationIDSuffixDetails>
    transformCaseDeductionItemList(
      final CaseDeductionItemDtlsList caseDeductionItemDtlsList)
      throws AppException, InformationalException {

    final List<BDMCaseDeductionItemObligationIDSuffixDetails> transformedList =
      new ArrayList<>();

    // initialize
    final BDMExternalLiabilityDOJData dojDataObj =
      BDMExternalLiabilityDOJDataFactory.newInstance();
    final BDMCaseDeductionItem bdmCDIObj =
      BDMCaseDeductionItemFactory.newInstance();
    final BDMCaseDeductionItemKey bdmCDIKey = new BDMCaseDeductionItemKey();
    final BDMExternalLiabilityDOJDataKey dojKey =
      new BDMExternalLiabilityDOJDataKey();

    for (final CaseDeductionItemDtls dtls : caseDeductionItemDtlsList.dtls) {

      final BDMCaseDeductionItemObligationIDSuffixDetails transformedDetails =
        new BDMCaseDeductionItemObligationIDSuffixDetails();

      transformedDetails.dtls = dtls;

      // find the obligationIDSuffix and add it to the struct if the deduction
      // is DOJ
      if (dtls.deductionName.equals(DEDUCTIONNAME.DOJ_ARREARS)
        || dtls.deductionName.equals(DEDUCTIONNAME.DOJ_FEES)) {
        // read the external liability ID
        bdmCDIKey.caseDeductionItemID = dtls.caseDeductionItemID;
        final BDMCaseDeductionItemDtls bdmCDIDtls = bdmCDIObj.read(bdmCDIKey);

        // read the DOJ details
        dojKey.externalLiabilityID = bdmCDIDtls.externalLiabilityID;
        final BDMExternalLiabilityDOJDataDtls deduction2DOJDtls =
          dojDataObj.read(dojKey);
        transformedDetails.obligationIDSuffix =
          deduction2DOJDtls.obligationIDSuffix;
      }

      transformedList.add(transformedDetails);
    }

    return transformedList;
  }

  /**
   * Retrieves the third party account details
   */
  @Override
  public ThirdPartyDetails determineThirdPartyAccountDetails(
    final String deductionName) throws AppException, InformationalException {

    final ThirdPartyDetails thirdPartyDetails = new ThirdPartyDetails();

    final DeductionNameStatus deductionNameKey = new DeductionNameStatus();
    deductionNameKey.deductionName = deductionName;
    // get the deductionID based off the deduction name
    final DeductionDtls deductionDtls = DeductionFactory.newInstance()
      .readActiveDeductionByName(deductionNameKey);

    // get the third party concern role ID from BDMDeduction based off deduction
    // ID
    final BDMDeductionKey bdmDeductionKey = new BDMDeductionKey();
    bdmDeductionKey.deductionID = deductionDtls.deductionID;
    final BDMDeductionDetails bdmDeductionDetails =
      BDMDeductionFactory.newInstance().readByDeductionID(bdmDeductionKey);

    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = bdmDeductionDetails.thirdPartyConcernRoleID;

    final ConcernRoleDtls concernRoleDtls =
      ConcernRoleFactory.newInstance().read(nfIndicator, crKey);

    // if application property does not link to an actual concern role, throw
    // an error
    if (nfIndicator.isNotFound()) {
      throw new AppException(BDMEXTERNALLIABILITY.ERR_THIRD_PARTY_CONFIG);
    }

    // TODO: adjust once design is finalized
    thirdPartyDetails.thirdPartyConcernRoleID =
      bdmDeductionDetails.thirdPartyConcernRoleID;
    thirdPartyDetails.customerAccName = concernRoleDtls.concernRoleName;
    thirdPartyDetails.customerAccNumber = concernRoleDtls.primaryAlternateID;

    return thirdPartyDetails;
  }

  /**
   * Used to set third party account details and deduction client details
   */
  @Override
  public void determineThirdPartyDetails(
    final ThirdPartyDeductionActivationStruct details)
    throws AppException, InformationalException {

    details.thirdPartyDtls =
      determineThirdPartyAccountDetails(details.deductionDtls.deductionName);

    details.deductionClientDetails.concernRoleType =
      CONCERNROLETYPE.EXTERNALPARTY;
    details.deductionClientDetails.dtls.clientConcernRoleID =
      details.thirdPartyDtls.thirdPartyConcernRoleID;

  }

  public static class BDMCaseDeductionItemPriorityComparator
    implements Comparator<BDMCaseDeductionItemObligationIDSuffixDetails> {

    private final List<String> obligationIDSpecialCharacterSuffixOrder;

    public BDMCaseDeductionItemPriorityComparator() {

      final String dojSuffixStr = Configuration
        .getProperty(EnvVars.BDM_ENV_DOJOBLIGATIONIDSUFFIX_PRIORITY);

      obligationIDSpecialCharacterSuffixOrder =
        Arrays.asList(dojSuffixStr.split(","));

    }

    /**
     * Compares 2 deduction items according to which should have a lower
     * priority value (hence higher priority)
     */
    @Override
    public int compare(
      final BDMCaseDeductionItemObligationIDSuffixDetails deduction1,
      final BDMCaseDeductionItemObligationIDSuffixDetails deduction2) {

      // CRA benefit repayment prioritizes by oldest creation date
      if (deduction1.dtls.deductionName
        .equals(DEDUCTIONNAME.CRA_BENEFIT_REPAYMENT)) {

        if (!deduction1.dtls.createdDate.isZero()
          && deduction1.dtls.createdDate
            .before(deduction2.dtls.createdDate)) {
          return -1;
        } else if (!deduction1.dtls.createdDate
          .equals(deduction2.dtls.createdDate)) {
          return 0;
        } else {
          return 1;
        }

      } else if (deduction1.dtls.deductionName
        .equals(DEDUCTIONNAME.DOJ_ARREARS)
        || deduction1.dtls.deductionName.equals(DEDUCTIONNAME.DOJ_FEES)) {

        final char[] deduction1Suffix =
          deduction1.obligationIDSuffix.toCharArray();
        final char[] deduction2Suffix =
          deduction2.obligationIDSuffix.toCharArray();

        if (deduction1Suffix.length < deduction2Suffix.length) {
          return -1;
        } else if (deduction1Suffix.length > deduction2Suffix.length) {
          return 1;
        }

        int index = 0;

        while (index < deduction1Suffix.length) {
          // if they are both alphabetic, compare the characters simply by the
          // character
          if (Character.isAlphabetic(deduction1Suffix[index])
            && Character.isAlphabetic(deduction2Suffix[index])) {
            if (deduction1Suffix[index] < deduction2Suffix[index]) {
              return -1;
            } else if (deduction1Suffix[index] > deduction2Suffix[index]) {
              return 1;
            } else {
              index++;
              continue;
            }
          }

          // if the first deduction is alphabetic and the second deduction is
          // not, the first deduction should have a lower priority number
          else if (Character.isAlphabetic(deduction1Suffix[index])
            && !Character.isAlphabetic(deduction2Suffix[index])) {
            return -1;
          }

          // if the first deduction is non-alphabetic and the second deduction
          // is alphabetic, the first deduction should have a higher priority
          // number
          else if (!Character.isAlphabetic(deduction1Suffix[index])
            && Character.isAlphabetic(deduction2Suffix[index])) {
            return 1;
          } else {
            // get the index of the suffix character's order in the
            // predetermined list
            final int deduction1SuffixOrder =
              obligationIDSpecialCharacterSuffixOrder
                .indexOf(String.valueOf(deduction1Suffix[index]));
            final int deduction2SuffixOrder =
              obligationIDSpecialCharacterSuffixOrder
                .indexOf(String.valueOf(deduction2Suffix[index]));

            if (deduction1SuffixOrder < deduction2SuffixOrder) {
              return -1;
            } else if (deduction1SuffixOrder > deduction2SuffixOrder) {
              return 1;
            } else {
              index++;
              continue;
            }
          }
        }

        return 0;

      }
      // for any other deductions, order by start date (oldest first), and then
      // by
      // deduction creation date if start dates are the same
      else {

        if (deduction1.dtls.startDate.before(deduction2.dtls.startDate)) {

          return -1;

        } else if (deduction1.dtls.startDate
          .equals(deduction2.dtls.startDate)) {

          if (!deduction1.dtls.createdDate.isZero()
            && deduction1.dtls.createdDate
              .before(deduction2.dtls.createdDate)) {

            return -1;

          } else if (deduction1.dtls.createdDate
            .equals(deduction2.dtls.createdDate)) {
            return 0;
          } else {
            return 1;

          }
        }
      }

      return 0;
    }

  }

  /**
   * Reads DOJ arrear deduction details
   */
  @Override
  public BDMDOJDeductionDetails readDOJDeductionDetails(
    final ReadDeductionKey key) throws AppException, InformationalException {

    final BDMDOJDeductionDetails details = new BDMDOJDeductionDetails();

    // read case deduction item
    final CaseDeductionItemKey cdiKey = new CaseDeductionItemKey();
    cdiKey.caseDeductionItemID = key.caseDeductionItemID;
    final CaseDeductionItemDtls cdiDtls =
      CaseDeductionItemFactory.newInstance().read(cdiKey);

    // if it is not a DOJ arrear deduction, no further processing needed
    if (cdiDtls.deductionName.equals(DEDUCTIONNAME.DOJ_ARREARS)) {
      details.isDOJArrearsDeduction = true;

      // find the corresponding external liability
      final BDMCaseDeductionItemKey bdmCDIKey = new BDMCaseDeductionItemKey();
      bdmCDIKey.caseDeductionItemID = key.caseDeductionItemID;
      final BDMCaseDeductionItemDtls bdmCDIDtls =
        BDMCaseDeductionItemFactory.newInstance().read(bdmCDIKey);

      if (bdmCDIDtls.externalLiabilityID != 0) {
        // read the DOJ details
        final BDMExternalLiabilityDOJDataKey dojKey =
          new BDMExternalLiabilityDOJDataKey();
        dojKey.externalLiabilityID = bdmCDIDtls.externalLiabilityID;
        final BDMExternalLiabilityDOJDataDtls dojDtls =
          BDMExternalLiabilityDOJDataFactory.newInstance().read(dojKey);

        if (dojDtls.deductionFirstPayInd) {
          details.fixedAmountFlag = 1;
        } else {
          details.fixedAmountFlag = 0;
        }

        details.holdbackDeductionRate = dojDtls.deductionRate;
        details.debtorFixedAmount = dojDtls.deductionMinPayAmount;
        details.perPayDeductionAmount = dojDtls.deductionAmount;
      }
    }
    return details;
  }
}
