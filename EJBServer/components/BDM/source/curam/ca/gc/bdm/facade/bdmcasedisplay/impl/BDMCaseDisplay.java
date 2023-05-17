package curam.ca.gc.bdm.facade.bdmcasedisplay.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.bdmcasedisplay.struct.BDMCaseHeaderConcernRoleDetails1;
import curam.ca.gc.bdm.facade.bdmcasedisplay.struct.BDMCaseSearchResultList;
import curam.ca.gc.bdm.facade.bdmcasedisplay.struct.BDMICProductDeliveryCaseRelationshipDetails;
import curam.ca.gc.bdm.facade.bdmcasedisplay.struct.BDMListICProductDeliveryCaseRelationship;
import curam.ca.gc.bdm.facade.bdmcasedisplay.struct.BDMSearchCaseDetails;
import curam.ca.gc.bdm.facade.bdmcasedisplay.struct.BDMSearchCaseDetails1;
import curam.ca.gc.bdm.message.BDMCASEDISPLAY;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.DUPLICATESTATUS;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.CaseFactory;
import curam.core.facade.fact.IntegratedCaseFactory;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.CaseSearchResultList;
import curam.core.facade.struct.ICProductDeliveryCaseRelationshipDetails;
import curam.core.facade.struct.ListICProductDeliveryCaseRelationship;
import curam.core.facade.struct.ListICProductDeliveryCaseRelationshipKey;
import curam.core.facade.struct.SearchCaseDetails1;
import curam.core.facade.struct.SearchCaseKey_fo;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.MaintainDeductionItemsFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.MaintainDeductionItems;
import curam.core.sl.entity.fact.CaseParticipantRoleFactory;
import curam.core.sl.entity.fact.ConcernRoleDuplicateFactory;
import curam.core.sl.entity.struct.CaseIDTypeCodeKey;
import curam.core.sl.entity.struct.DuplicateForConcernRoleDtls;
import curam.core.sl.infrastructure.paymentcorrection.impl.PaymentCorrection;
import curam.core.sl.struct.CaseParticipantRoleFullDetails1;
import curam.core.struct.CaseHeaderConcernRoleDetails1;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseSearchCriteria1;
import curam.core.struct.CaseSearchDetails1;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.DeductionLiabilityAmountDetails;
import curam.core.struct.RelatedCaseID;
import curam.message.BPOCASETAB;
import curam.message.GENERAL;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTableItemIdentifier;

public class BDMCaseDisplay
  extends curam.ca.gc.bdm.facade.bdmcasedisplay.base.BDMCaseDisplay {

  @Inject
  protected PaymentCorrection paymentCorrection;

  public BDMCaseDisplay() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Performs a typical search of a Person's cases (used on Person - Care and
   * Protection - Cases). Modifies the results to add more specification to
   * Payment Corrections and displays whether a liability payment correction is
   * fully repaid.
   */
  @Override
  public BDMSearchCaseDetails1 personSearchCase1(final SearchCaseKey_fo key)
    throws AppException, InformationalException {

    // initialize return struct
    final BDMSearchCaseDetails1 bdmSearchCaseDetails =
      new BDMSearchCaseDetails1();

    // call OOTB search
    final SearchCaseDetails1 searchCaseDetails =
      PersonFactory.newInstance().searchCase1(key);

    // BUG 91793 - Duplicate Person's FEC is not listed in Master Person`s case
    // after merge
    // Start - Get duplication person of the original person, if any...
    final curam.core.sl.entity.intf.ConcernRoleDuplicate concernRoleDuplicateObj =
      ConcernRoleDuplicateFactory.newInstance();

    // Set an indicator if this concern has duplicates
    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();

    concernRoleIDStatusCodeKey.concernRoleID =
      key.casesByConcernRoleIDKey.concernRoleID;
    concernRoleIDStatusCodeKey.statusCode = DUPLICATESTATUS.UNMARKED;

    // Check if this concern role has any duplicates
    final curam.core.sl.entity.struct.DuplicateForConcernRoleDtlsList duplicateForConcernRoleDtlsList =
      concernRoleDuplicateObj
        .searchByOriginalConcernRoleIDNotStatus(concernRoleIDStatusCodeKey);

    SearchCaseDetails1 duplicatePersonSearchCaseDetails;

    if (!duplicateForConcernRoleDtlsList.dtls.isEmpty()) {

      for (final DuplicateForConcernRoleDtls duplicateForConcernRoleDtls : duplicateForConcernRoleDtlsList.dtls) {
        final SearchCaseKey_fo dupPersonSearchKey = new SearchCaseKey_fo();
        dupPersonSearchKey.casesByConcernRoleIDKey.concernRoleID =
          duplicateForConcernRoleDtls.duplicateConcernRoleID;
        dupPersonSearchKey.casesByConcernRoleIDKey.statusCode =
          key.casesByConcernRoleIDKey.statusCode;

        duplicatePersonSearchCaseDetails =
          PersonFactory.newInstance().searchCase1(dupPersonSearchKey);

        for (int i =
          0; i < duplicatePersonSearchCaseDetails.caseHeaderConcernRoleDetailsList.dtls
            .size(); i++) {
          searchCaseDetails.caseHeaderConcernRoleDetailsList.dtls.addRef(
            duplicatePersonSearchCaseDetails.caseHeaderConcernRoleDetailsList.dtls
              .get(i));
        }
      }
    }

    // assign details
    bdmSearchCaseDetails.assign(searchCaseDetails);

    // iterate through list items and add modified case type and liability
    // status
    BDMCaseHeaderConcernRoleDetails1 bdmCaseDetails;
    CaseHeaderConcernRoleDetails1 caseDetails;
    for (int i =
      0; i < bdmSearchCaseDetails.caseHeaderConcernRoleDetailsList.dtls
        .size(); i++) {

      bdmCaseDetails =
        bdmSearchCaseDetails.caseHeaderConcernRoleDetailsList.dtls.get(i);
      caseDetails =
        searchCaseDetails.caseHeaderConcernRoleDetailsList.dtls.get(i);

      bdmCaseDetails.dtls = caseDetails;

      bdmCaseDetails.caseType =
        getCaseType(caseDetails.productTypeDesc, caseDetails.caseID);

      bdmCaseDetails.status =
        getLiabilityStatus(new CodeTableItemIdentifier(CASESTATUS.TABLENAME,
          caseDetails.statusCode), caseDetails.caseID);

      // 111068 Start
      final ConcernRoleDtls concernRoleDtls =
        getConcernRoleDetails(caseDetails);

      bdmCaseDetails.participantConcernRoleID = concernRoleDtls.concernRoleID;
      bdmCaseDetails.participantConcernRoleType =
        concernRoleDtls.concernRoleType;
      bdmCaseDetails.participantConcernRoleName =
        concernRoleDtls.concernRoleName;
    }
    // 111068 End

    return bdmSearchCaseDetails;
  }

  /**
   * Performs a typical search of a concernrole to get the concenroleID and
   * concernroleType using the case id and case participant type code as
   * primary.
   */
  private ConcernRoleDtls
    getConcernRoleDetails(final CaseHeaderConcernRoleDetails1 caseDetails)
      throws AppException, InformationalException {

    final CaseIDTypeCodeKey caseIDTypeCodeKey = new CaseIDTypeCodeKey();
    caseIDTypeCodeKey.caseID = caseDetails.caseID;
    caseIDTypeCodeKey.typeCode = CASEPARTICIPANTROLETYPE.PRIMARY;
    final CaseParticipantRoleFullDetails1 caseParticipantRoleFullDetails1 =
      CaseParticipantRoleFactory.newInstance()
        .readByCaseIDAndTypeCode(caseIDTypeCodeKey);

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID =
      caseParticipantRoleFullDetails1.dtls.participantRoleID;
    final ConcernRoleDtls ConcernRoleDtls =
      ConcernRoleFactory.newInstance().read(concernRoleKey);
    return ConcernRoleDtls;
  }

  /**
   * Performs a typical search of a cases (used on Case Search). Modifies the
   * results to add more specification to Payment Corrections and displays
   * whether a liability payment correction is fully repaid.
   */
  @Override
  public BDMCaseSearchResultList caseSearch2(final CaseSearchCriteria1 key)
    throws AppException, InformationalException {

    // call OOTB search
    final CaseSearchResultList caseSearchResultList =
      CaseFactory.newInstance().caseSearch2(key);

    // initialize return struct
    final BDMCaseSearchResultList bdmCaseSearchResultList =
      new BDMCaseSearchResultList();

    // assign details
    bdmCaseSearchResultList.assign(caseSearchResultList);

    BDMSearchCaseDetails bdmSearchCaseDetails;
    CaseSearchDetails1 caseSearchDetails;
    for (int i = 0; i < bdmCaseSearchResultList.listDtls.searchDtls
      .size(); i++) {
      bdmSearchCaseDetails =
        bdmCaseSearchResultList.listDtls.searchDtls.get(i);
      caseSearchDetails = caseSearchResultList.listDtls.searchDtls.get(i);

      bdmSearchCaseDetails.dtls = caseSearchDetails;

      bdmSearchCaseDetails.caseType = getCaseType(
        caseSearchDetails.caseCatTypeCode, caseSearchDetails.caseID);

      bdmSearchCaseDetails.status =
        getLiabilityStatus(new CodeTableItemIdentifier(CASESTATUS.TABLENAME,
          caseSearchDetails.statusCode), caseSearchDetails.caseID);
    }

    return bdmCaseSearchResultList;
  }

  /**
   * Performs a typical search of a Person's cases (used on Product Delivery
   * Case - Administration - Related Cases). Modifies the results to add more
   * specification to Payment Corrections and displays whether a liability
   * payment correction is fully repaid.
   */
  @Override
  public BDMListICProductDeliveryCaseRelationship
    listProductDeliveryCaseRelationship(
      final ListICProductDeliveryCaseRelationshipKey key)
      throws AppException, InformationalException {

    // initialize return struct
    final BDMListICProductDeliveryCaseRelationship bdmListICProductDeliveryCaseRelationship =
      new BDMListICProductDeliveryCaseRelationship();

    // call OOTB list
    final ListICProductDeliveryCaseRelationship listProductDeliveryCaseRelationship =
      IntegratedCaseFactory.newInstance()
        .listProductDeliveryCaseRelationship(key);

    // assign details
    bdmListICProductDeliveryCaseRelationship
      .assign(listProductDeliveryCaseRelationship);

    // iterate through list items and add modified case type and liability
    // status
    BDMICProductDeliveryCaseRelationshipDetails bdmCaseRelationshipDetails;
    ICProductDeliveryCaseRelationshipDetails caseRelationshipDetails;
    for (int i = 0; i < bdmListICProductDeliveryCaseRelationship.dtls
      .size(); i++) {

      bdmCaseRelationshipDetails =
        bdmListICProductDeliveryCaseRelationship.dtls.get(i);

      caseRelationshipDetails =
        listProductDeliveryCaseRelationship.dtls.get(i);

      bdmCaseRelationshipDetails.dtls = caseRelationshipDetails;

      bdmCaseRelationshipDetails.caseType =
        getCaseType(caseRelationshipDetails.relatedCaseNameOpt,
          caseRelationshipDetails.relatedCaseID);

      bdmCaseRelationshipDetails.status = getLiabilityStatus(
        new CodeTableItemIdentifier(RECORDSTATUS.TABLENAME,
          caseRelationshipDetails.statusCode),
        caseRelationshipDetails.relatedCaseID);
    }

    return bdmListICProductDeliveryCaseRelationship;
  }

  /**
   * Checks if case list item is an overpayment that has been fully repaid and
   * modify status if so
   *
   * @param statusCode
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getLiabilityStatus(final CodeTableItemIdentifier statusCode,
    final long caseID) throws AppException, InformationalException {

    final CaseHeaderKey chKey = new CaseHeaderKey();
    chKey.caseID = caseID;
    // initialize liability status with the original status
    final LocalisableString liabilityStatus =
      new LocalisableString(BDMCASEDISPLAY.LIABILITY_STATUS);

    liabilityStatus.arg(statusCode);

    // if it's an overpayment case check to see if it is fully repaid in order
    // to add a Fully Repaid suffix to the status
    if (paymentCorrection.isOverPaymentPaymentCorrection(chKey)) {

      final MaintainDeductionItems maintainDeductionItemsObj =
        MaintainDeductionItemsFactory.newInstance();

      final RelatedCaseID relatedCaseID = new RelatedCaseID();
      relatedCaseID.relatedCaseID = caseID;
      final DeductionLiabilityAmountDetails liabilityAmount =
        maintainDeductionItemsObj.readLiabilityAmounts(relatedCaseID);

      // if amount is 0, add - Fully Repaid
      if (liabilityAmount.outstandingAmount.isZero()) {

        liabilityStatus.arg(CuramConst.gkSpace + CuramConst.gkDash
          + CuramConst.gkSpace + BPOCASETAB.INF_FULLY_REPAID_TEXT
            .getMessageText(TransactionInfo.getProgramLocale()));
      } else {
        liabilityStatus.arg("");
      }

    } else {
      liabilityStatus.arg("");
    }

    return liabilityStatus.toClientFormattedText();
  }

  /**
   * Check if case list item is overpayment or underpayment and modify case type
   * if so
   *
   * @param caseCatTypeCode
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String getCaseType(final String caseCatTypeCode, final long caseID)
    throws AppException, InformationalException {

    final CaseHeaderKey chKey = new CaseHeaderKey();
    // initialize case type with the original type
    final LocalisableString caseType =
      new LocalisableString(BDMCASEDISPLAY.CASE_TYPE);

    caseType.arg(caseCatTypeCode);

    chKey.caseID = caseID;

    if (paymentCorrection.isOverPaymentPaymentCorrection(chKey)) {
      // add - Overpaid suffix to case type
      caseType.arg(CuramConst.gkSpace + CuramConst.gkDash + CuramConst.gkSpace
        + GENERAL.INF_OVERPAID
          .getMessageText(TransactionInfo.getProgramLocale()));

    } else if (paymentCorrection.isUnderPaymentPaymentCorrection(chKey)) {
      // add - Underpaid suffix to case type
      caseType.arg(CuramConst.gkSpace + CuramConst.gkDash + CuramConst.gkSpace
        + GENERAL.INF_UNDERPAID
          .getMessageText(TransactionInfo.getProgramLocale()));

    } else {
      caseType.arg("");

    }

    return caseType.toClientFormattedText();
  }

}
