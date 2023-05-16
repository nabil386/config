package curam.ca.gc.bdm.rest.bdmdojinboundapi.impl;

import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.codetable.BDMDOJRECORDSTATUS;
import curam.ca.gc.bdm.codetable.BDMRESTACTIONTYPE;
import curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityDtls;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.fact.BDMDoJInboundStagingFactory;
import curam.ca.gc.bdm.entity.intf.BDMDoJInboundStaging;
import curam.ca.gc.bdm.entity.struct.BDMDoJInboundStagingDtls;
import curam.ca.gc.bdm.rest.bdmdojinboundapi.struct.BDMObligationExchange;
import curam.ca.gc.bdm.sl.financial.struct.BDMCheckDOJLiabilityExistDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMModifyDOJLbyDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMRegisterDOJLbyDetails;
import curam.ca.gc.bdm.sl.maintaindojliability.intf.MaintainDOJLiability;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.facade.intf.UniqueID;
import curam.core.impl.CuramConst;
import curam.core.struct.AlternateIDTypeCodeKey;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;

/**
 * REST API to Populate BDMDoJInboundStaging
 * tables from the DOJ Inbound
 * called by Interop team after processing FOE debts file from DOJ
 *
 */
public class BDMDoJInboundAPI
  extends curam.ca.gc.bdm.rest.bdmdojinboundapi.base.BDMDoJInboundAPI {

  @Override
  /**
   * Insert new record into staging table with REST Action type "POST"
   *
   * @param dojInboundData
   * @return BDMDoJObligationResponse
   */
  public void processDOJInbound(final BDMObligationExchange dojInboundData)
    throws AppException, InformationalException {

    final BDMDoJInboundDetails dojInboundDetails =
      getDOJInboundDetails(dojInboundData, BDMRESTACTIONTYPE.BDM_REST_POST);

    /***
     * Call Payment API
     */
    final MaintainDOJLiability dojLibility =
      new curam.ca.gc.bdm.sl.maintaindojliability.impl.MaintainDOJLiability();
    final BDMRegisterDOJLbyDetails registerDOJLbyDetailsObj =
      new BDMRegisterDOJLbyDetails();

    registerDOJLbyDetailsObj.concernRoleID =
      dojInboundDetails.getConcernRoleID();
    registerDOJLbyDetailsObj.sinIdentification =
      dojInboundDetails.getPersonSINIdentification() + "";
    registerDOJLbyDetailsObj.obligationIDSuffix =
      dojInboundDetails.getPersonObligationIDSuffix();
    registerDOJLbyDetailsObj.obligationID =
      dojInboundDetails.getPersonObligationID();
    registerDOJLbyDetailsObj.programType =
      BDMBENEFITPROGRAMTYPE.getDefaultCode();

    if (dojInboundDetails
      .getObligation().obligationOutstandingFeeBalance != null
      && dojInboundDetails.getObligation().obligationOutstandingFeeBalance
        .getValue() > CuramConst.kLongZero) {
      registerDOJLbyDetailsObj.liabilityAmount =
        dojInboundDetails.getObligation().obligationOutstandingFeeBalance;
      registerDOJLbyDetailsObj.isFeeTypeInd = true;
      dojLibility.registerLiability(registerDOJLbyDetailsObj); // Call this
                                                               // for //
                                                               // BDMEXTERNALLIABILITYTYPE.DOJ_FEES;
    }

    registerDOJLbyDetailsObj.liabilityAmount =
      dojInboundDetails.getObligation().obligationArrearsBalance;
    registerDOJLbyDetailsObj.debtorFixedAmount =
      dojInboundDetails.getObligation().obligationFixedAmount;
    registerDOJLbyDetailsObj.holdbackDeductionRate =
      100 - dojInboundDetails.getObligation().obligationDebtPercentage;
    registerDOJLbyDetailsObj.fixedAmountInd =
      dojInboundDetails.getObligation().obligationFixedAmountIndicator;
    registerDOJLbyDetailsObj.perPayDeductionAmount =
      dojInboundDetails.getObligation().obligationPaymentAmount;
    registerDOJLbyDetailsObj.isFeeTypeInd = false; // SET this to false
                                                   // deliberately to ensure
                                                   // its not Fee type
    dojLibility.registerLiability(registerDOJLbyDetailsObj); // This is
                                                             // for
                                                             // BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS

    processDOJDataIntoStagingTable(dojInboundData,
      BDMRESTACTIONTYPE.BDM_REST_POST);

  }

  /**
   * Insert new record into staging table with REST Action type "PUT"
   *
   * @param dojInboundData
   * @return BDMDoJObligationResponse
   */
  @Override
  public BDMObligationExchange
    modifyDOJInbound(final BDMObligationExchange dojInboundData)
      throws AppException, InformationalException {

    final BDMDoJInboundDetails dojInboundDetails =
      getDOJInboundDetails(dojInboundData, BDMRESTACTIONTYPE.BDM_REST_PUT);

    final MaintainDOJLiability dojLibility =
      new curam.ca.gc.bdm.sl.maintaindojliability.impl.MaintainDOJLiability();

    BDMExternalLiabilityKey externalLiabilityKey;
    final BDMCheckDOJLiabilityExistDetails dojLbyDetails =
      new BDMCheckDOJLiabilityExistDetails();
    dojLbyDetails.concernRoleID = dojInboundDetails.getConcernRoleID();
    dojLbyDetails.sinIdentification =
      Integer.toString(dojInboundDetails.getPersonSINIdentification());
    dojLbyDetails.obligationIDSuffix =
      dojInboundDetails.getPersonObligationIDSuffix();

    dojLbyDetails.isFeeTypeInd = true; // This is for
                                       // BDMEXTERNALLIABILITYTYPE.DOJ_FEES;
    externalLiabilityKey =
      dojLibility.getLiabilityByExtRefNumber(dojLbyDetails);

    NotFoundIndicator nfIndicator = new NotFoundIndicator();
    BDMExternalLiabilityDtls externalLiabilityDtls =
      BDMExternalLiabilityFactory.newInstance().read(nfIndicator,
        externalLiabilityKey);

    if (!nfIndicator.isNotFound()) {
      final BDMModifyDOJLbyDetails modifyDOJLbyDetailsObj =
        new BDMModifyDOJLbyDetails();
      modifyDOJLbyDetailsObj.externalLiabilityID =
        externalLiabilityDtls.externalLiabilityID;
      modifyDOJLbyDetailsObj.liabilityAmount =
        dojInboundDetails.getObligation().obligationOutstandingFeeBalance;
      modifyDOJLbyDetailsObj.versionNo = externalLiabilityDtls.versionNo;
      dojLibility.modifyLiability(modifyDOJLbyDetailsObj);
    }

    dojLbyDetails.isFeeTypeInd = false; // This is for
    // BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS;
    externalLiabilityKey =
      dojLibility.getLiabilityByExtRefNumber(dojLbyDetails);

    nfIndicator = new NotFoundIndicator();
    externalLiabilityDtls = BDMExternalLiabilityFactory.newInstance()
      .read(nfIndicator, externalLiabilityKey);

    if (!nfIndicator.isNotFound()) {
      final BDMModifyDOJLbyDetails modifyDOJLbyDetailsObj =
        new BDMModifyDOJLbyDetails();
      modifyDOJLbyDetailsObj.externalLiabilityID =
        externalLiabilityDtls.externalLiabilityID;
      modifyDOJLbyDetailsObj.liabilityAmount =
        dojInboundDetails.getObligation().obligationArrearsBalance;
      modifyDOJLbyDetailsObj.debtorFixedAmount =
        dojInboundDetails.getObligation().obligationFixedAmount;
      modifyDOJLbyDetailsObj.holdbackDeductionRate =
        100 - dojInboundDetails.getObligation().obligationDebtPercentage;
      modifyDOJLbyDetailsObj.fixedAmountInd =
        dojInboundDetails.getObligation().obligationFixedAmountIndicator;
      modifyDOJLbyDetailsObj.perPayDeductionAmount =
        dojInboundDetails.getObligation().obligationPaymentAmount;
      modifyDOJLbyDetailsObj.versionNo = externalLiabilityDtls.versionNo;
      dojLibility.modifyLiability(modifyDOJLbyDetailsObj);
    }

    processDOJDataIntoStagingTable(dojInboundData,
      BDMRESTACTIONTYPE.BDM_REST_PUT);
    return dojInboundData;
  }

  @Override
  public void deleteDOJInbound(final BDMObligationExchange dojInboundData)
    throws AppException, InformationalException {

    final BDMDoJInboundDetails dojInboundDetails =
      getDOJInboundDetails(dojInboundData, BDMRESTACTIONTYPE.BDM_REST_DELETE);
    final MaintainDOJLiability dojLibility =
      new curam.ca.gc.bdm.sl.maintaindojliability.impl.MaintainDOJLiability();

    BDMExternalLiabilityKey dojDeleteLbyKeyObj;
    final BDMCheckDOJLiabilityExistDetails dojLbyDetails =
      new BDMCheckDOJLiabilityExistDetails();
    dojLbyDetails.concernRoleID = dojInboundDetails.getConcernRoleID();
    dojLbyDetails.sinIdentification =
      Integer.toString(dojInboundDetails.getPersonSINIdentification());
    dojLbyDetails.obligationIDSuffix =
      dojInboundDetails.getPersonObligationIDSuffix();

    dojLbyDetails.isFeeTypeInd = true; // This is for
                                       // BDMEXTERNALLIABILITYTYPE.DOJ_FEES;
    dojDeleteLbyKeyObj =
      dojLibility.getLiabilityByExtRefNumber(dojLbyDetails);
    if (dojDeleteLbyKeyObj != null) {
      dojLibility.deleteLiability(dojDeleteLbyKeyObj);
    }

    dojLbyDetails.isFeeTypeInd = false; // This is for
    // BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS;
    dojDeleteLbyKeyObj =
      dojLibility.getLiabilityByExtRefNumber(dojLbyDetails);
    if (dojDeleteLbyKeyObj != null) {
      dojLibility.deleteLiability(dojDeleteLbyKeyObj);
    }

    processDOJDataIntoStagingTable(dojInboundData,
      BDMRESTACTIONTYPE.BDM_REST_DELETE);
  }

  /**
   *
   * @param dojInboundData
   * @param restActionType
   * @return
   */
  BDMDoJInboundDetails getDOJInboundDetails(
    final BDMObligationExchange dojInboundData, final String restActionType) {

    final BDMDoJInboundDetails dojInboundDetailsObj =
      new BDMDoJInboundDetails();
    Trace.kTopLevelLogger.info("processDOJInbound personSINIdentification="
      + dojInboundData.obligation.person.personSINIdentification);
    Trace.kTopLevelLogger.info("processDOJInbound personObligationIDSuffix="
      + dojInboundData.obligation.person.personObligationIDSuffix);
    Trace.kTopLevelLogger
      .info("DOJInboundAPI InboundData Transaction Control ID = "
        + dojInboundData.metadata.transactionControlIdentificationID
        + " AND ActionType = " + restActionType);

    try {
      dojInboundDetailsObj.setCreationDate(Date.getCurrentDate());
      dojInboundDetailsObj.setStatus(BDMDOJRECORDSTATUS.PENDING);
      dojInboundDetailsObj.setRestActionType(restActionType);

      // Get concernRoleID of the person based on SIN Number
      final curam.core.intf.ConcernRoleAlternateID concernRoleAlternateIDObj =
        curam.core.fact.ConcernRoleAlternateIDFactory.newInstance();
      final AlternateIDTypeCodeKey alternateIDTypeCodeKey =
        new AlternateIDTypeCodeKey();
      alternateIDTypeCodeKey.alternateID = Integer
        .toString(dojInboundData.obligation.person.personSINIdentification);
      alternateIDTypeCodeKey.typeCode =
        CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;
      alternateIDTypeCodeKey.statusCode = RECORDSTATUS.NORMAL;

      curam.core.struct.ConcernRoleAlternateIDDtls conernRoleAlternateIDDtls =
        new ConcernRoleAlternateIDDtls();

      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

      conernRoleAlternateIDDtls = concernRoleAlternateIDObj
        .readByAltIDTypeCode(notFoundIndicator, alternateIDTypeCodeKey);

      if (notFoundIndicator.isNotFound()) {
        Trace.kTopLevelLogger.info(
          "DOJInboundAPI Person Not found for DOJ Obligation Data for DebtorID: "
            + dojInboundData.obligation.person.personObligationID
            + " AND DebtorSuffix = "
            + dojInboundData.obligation.person.personObligationIDSuffix
            + ", For Person SIN ="
            + dojInboundData.obligation.person.personSINIdentification);
      } else {
        dojInboundDetailsObj
          .setConcernRoleID(conernRoleAlternateIDDtls.concernRoleID);
      }

      dojInboundDetailsObj.setMetadata(dojInboundData.metadata);
      dojInboundDetailsObj.setObligation(dojInboundData.obligation);
      dojInboundDetailsObj.setPersonObligationID(
        dojInboundData.obligation.person.personObligationID);
      dojInboundDetailsObj.setPersonObligationIDSuffix(
        dojInboundData.obligation.person.personObligationIDSuffix);
      dojInboundDetailsObj.setPersonSINIdentification(
        dojInboundData.obligation.person.personSINIdentification);

    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(
        "DOJInboundAPI InboundData Error occurred while processing Transaction ID = "
          + dojInboundData.metadata.transactionControlIdentificationID);
      e.printStackTrace();
    }
    return dojInboundDetailsObj;

  }

  /**
   * Store data in DOJDataIntoStagingTable for audit purpose and to be used in
   * DOJOutbound
   *
   * @param dojInboundData
   * @return BDMDoJObligationResponse
   */
  void processDOJDataIntoStagingTable(
    final BDMObligationExchange dojInboundData, final String restActionType)
    throws InformationalException {

    Trace.kTopLevelLogger.info("processDOJInbound personSINIdentification="
      + dojInboundData.obligation.person.personSINIdentification);
    Trace.kTopLevelLogger.info("processDOJInbound personObligationIDSuffix="
      + dojInboundData.obligation.person.personObligationIDSuffix);
    Trace.kTopLevelLogger
      .info("DOJInboundAPI InboundData Transaction Control ID = "
        + dojInboundData.metadata.transactionControlIdentificationID
        + " AND ActionType = " + restActionType);

    try {
      final BDMDoJInboundStaging doJInboundStagingObj =
        BDMDoJInboundStagingFactory.newInstance();

      final BDMDoJInboundStagingDtls dojInboundStaginDtlsObj =
        new BDMDoJInboundStagingDtls();
      dojInboundStaginDtlsObj.creationDate = Date.getCurrentDate();
      dojInboundStaginDtlsObj.status = BDMDOJRECORDSTATUS.PENDING;
      dojInboundStaginDtlsObj.restActionType = restActionType;

      // Get concernRoleID of the person based on SIN Number
      final curam.core.intf.ConcernRoleAlternateID concernRoleAlternateIDObj =
        curam.core.fact.ConcernRoleAlternateIDFactory.newInstance();
      final AlternateIDTypeCodeKey alternateIDTypeCodeKey =
        new AlternateIDTypeCodeKey();
      alternateIDTypeCodeKey.alternateID = Integer
        .toString(dojInboundData.obligation.person.personSINIdentification);
      alternateIDTypeCodeKey.typeCode =
        CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;
      alternateIDTypeCodeKey.statusCode = RECORDSTATUS.NORMAL;

      curam.core.struct.ConcernRoleAlternateIDDtls conernRoleAlternateIDDtls =
        new ConcernRoleAlternateIDDtls();

      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

      conernRoleAlternateIDDtls = concernRoleAlternateIDObj
        .readByAltIDTypeCode(notFoundIndicator, alternateIDTypeCodeKey);

      if (notFoundIndicator.isNotFound()) {
        Trace.kTopLevelLogger.info(
          "DOJInboundAPI Person Not found for DOJ Obligation Data for DebtorID: "
            + dojInboundData.obligation.person.personObligationID
            + " AND DebtorSuffix = "
            + dojInboundData.obligation.person.personObligationIDSuffix
            + ", For Person SIN ="
            + dojInboundData.obligation.person.personSINIdentification);
      } else {
        dojInboundStaginDtlsObj.concernRoleID =
          conernRoleAlternateIDDtls.concernRoleID;
      }

      dojInboundStaginDtlsObj.assign(dojInboundData.metadata);
      dojInboundStaginDtlsObj.assign(dojInboundData.obligation);
      dojInboundStaginDtlsObj.personObligationID =
        dojInboundData.obligation.person.personObligationID;
      dojInboundStaginDtlsObj.personObligationIDSuffix =
        dojInboundData.obligation.person.personObligationIDSuffix;
      dojInboundStaginDtlsObj.personSINIdentification =
        dojInboundData.obligation.person.personSINIdentification;

      final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
      dojInboundStaginDtlsObj.dojInboundRecordID =
        uniqueIDObj.getNextID().uniqueID;
      doJInboundStagingObj.insert(dojInboundStaginDtlsObj);

    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(
        "DOJInboundAPI InboundData Error occurred while processing Transaction ID = "
          + dojInboundData.metadata.transactionControlIdentificationID);
      e.printStackTrace();
    }

  }

}
