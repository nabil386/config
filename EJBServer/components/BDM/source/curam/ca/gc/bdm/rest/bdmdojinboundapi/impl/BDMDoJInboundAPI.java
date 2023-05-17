package curam.ca.gc.bdm.rest.bdmdojinboundapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMAUDITAPITYPE;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.codetable.BDMDOJRECORDSTATUS;
import curam.ca.gc.bdm.codetable.BDMRESTACTIONTYPE;
import curam.ca.gc.bdm.entity.bdmexternalliability.fact.BDMExternalLiabilityFactory;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityDtls;
import curam.ca.gc.bdm.entity.bdmexternalliability.struct.BDMExternalLiabilityKey;
import curam.ca.gc.bdm.entity.fact.BDMDoJInboundStagingFactory;
import curam.ca.gc.bdm.entity.intf.BDMDoJInboundStaging;
import curam.ca.gc.bdm.entity.struct.BDMDoJInboundStagingDtls;
import curam.ca.gc.bdm.message.BDMRESTAPIERRORMESSAGE;
import curam.ca.gc.bdm.rest.bdmdojinboundapi.struct.BDMObligationExchange;
import curam.ca.gc.bdm.sl.financial.struct.BDMCheckDOJLiabilityExistDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMModifyDOJLbyDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMRegisterDOJLbyDetails;
import curam.ca.gc.bdm.sl.maintaindojliability.intf.MaintainDOJLiability;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditDetails.BDMAPIAuditDetailsBuilder;
import curam.ca.gc.bdm.util.rest.impl.BDMAPIAuditUtil;
import curam.ca.gc.bdm.util.rest.impl.BDMInterfaceLogger;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.facade.intf.UniqueID;
import curam.core.impl.CuramConst;
import curam.core.struct.AlternateIDTypeCodeKey;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.message.INFRASTRUCTURE;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import org.apache.commons.httpclient.HttpStatus;

/**
 * REST API to Populate BDMDoJInboundStaging
 * tables from the DOJ Inbound
 * called by Interop team after processing FOE debts file from DOJ
 *
 */
public class BDMDoJInboundAPI
  extends curam.ca.gc.bdm.rest.bdmdojinboundapi.base.BDMDoJInboundAPI {

  public BDMDoJInboundAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  BDMInterfaceLogger logger;

  @Inject
  BDMAPIAuditUtil bdmapiAuditUtil;

  private final String kMethod_processDOJInbound =
    "BDMDoJInboundAPI.processDOJInbound";

  private final String kMethod_modifyDOJInbound =
    "BDMDoJInboundAPI.modifyDOJInbound";

  private final String kMethod_deleteDOJInbound =
    "BDMDoJInboundAPI.deleteDOJInbound";

  @Override
  /**
   * Insert new record into staging table with REST Action type "POST"
   *
   * @param dojInboundData
   * @return BDMDoJObligationResponse
   */
  public void processDOJInbound(final BDMObligationExchange dojInboundData)
    throws AppException, InformationalException {

    // BEGIN, ADO-55810, implementing gaps for DOJ inbound
    // record the time of the call
    final long startTime = System.currentTimeMillis();

    final String correlationID = dojInboundData.cloudEvent.id;
    final String source = dojInboundData.cloudEvent.source;
    Trace.kTopLevelLogger
      .info(kMethod_processDOJInbound + ": CorrelationID = " + correlationID);
    final BDMAPIAuditDetailsBuilder auditDetailsBuilder =
      new BDMAPIAuditDetailsBuilder();
    BDMAPIAuditDetails bdmapiAuditDetails = auditDetailsBuilder
      .setMethod(kMethod_processDOJInbound)
      .setRelatedID(dojInboundData.metadata.metadataIdentificationID)
      .setRequestObject(dojInboundData).setApiType(BDMAUDITAPITYPE.BDMINBOUND)
      .setRequestTransactionDateTime(DateTime.getCurrentDateTime())
      .setSource(source).setCorrelationID(correlationID).build();
    // END, ADO-55810, implementing gaps for DOJ inbound
    try {
      final BDMDoJInboundDetails dojInboundDetails = getDOJInboundDetails(
        dojInboundData, BDMRESTACTIONTYPE.BDM_REST_POST, bdmapiAuditDetails);

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
        // Call this for BDMEXTERNALLIABILITYTYPE.DOJ_FEES;
        dojLibility.registerLiability(registerDOJLbyDetailsObj);
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
      // SET this to false deliberately to ensure its not Fee type
      registerDOJLbyDetailsObj.isFeeTypeInd = false;
      // This is for BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS
      dojLibility.registerLiability(registerDOJLbyDetailsObj);

      processDOJDataIntoStagingTable(dojInboundData,
        BDMRESTACTIONTYPE.BDM_REST_POST,
        dojInboundDetails.getConcernRoleID());
      // BEGIN, ADO-55810, implementing gaps for DOJ inbound
      bdmapiAuditDetails = auditDetailsBuilder
        .setResponseTransactionDateTime(DateTime.getCurrentDateTime())
        .setStatusCode(HttpStatus.SC_NO_CONTENT).build();

      bdmapiAuditUtil.auditAPI(bdmapiAuditDetails);

    } catch (final Exception e) {
      if (!e.getClass().getCanonicalName()
        .equals(AppException.class.getCanonicalName())) {
        // If there are any other exceptions other than AppException which is
        // handled in APIs, then 500:Internal Server error will be
        // thrown by Curam REST infra and this logic will log that entry in
        // audit table. The exception caught is thrown after storing in auditing
        Trace.kTopLevelLogger.info(
          "DOJInboundAPI InboundData Error occurred while processing Transaction ID = "
            + dojInboundData.metadata.transactionControlIdentificationID);

        BDMRestUtil.auditErrorResponse(
          new AppException(INFRASTRUCTURE.ID_UNHANDLED, e),
          HttpStatus.SC_INTERNAL_SERVER_ERROR, bdmapiAuditDetails);

        logger.logRestAPIPerf(kMethod_processDOJInbound, startTime, "");
      }
      // ensure exception is thrown
      throw e;
    }

    // log the performance time in milliseconds
    logger.logRestAPIPerf(kMethod_processDOJInbound, startTime, "");
    // END, ADO-55810, implementing gaps for DOJ inbound
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

    // BEGIN, ADO-55810, implementing gaps for DOJ inbound
    // record the time of the call
    final long startTime = System.currentTimeMillis();

    final String correlationID = dojInboundData.cloudEvent.id;
    final String source = dojInboundData.cloudEvent.source;
    Trace.kTopLevelLogger
      .info(kMethod_modifyDOJInbound + ": CorrelationID = " + correlationID);
    final BDMAPIAuditDetailsBuilder auditDetailsBuilder =
      new BDMAPIAuditDetailsBuilder();
    BDMAPIAuditDetails bdmapiAuditDetails = auditDetailsBuilder
      .setMethod(kMethod_modifyDOJInbound)
      .setRelatedID(dojInboundData.metadata.metadataIdentificationID)
      .setRequestObject(dojInboundData).setApiType(BDMAUDITAPITYPE.BDMINBOUND)
      .setRequestTransactionDateTime(DateTime.getCurrentDateTime())
      .setSource(source).setCorrelationID(correlationID).build();
    // END, ADO-55810, implementing gaps for DOJ inbound

    final BDMDoJInboundDetails dojInboundDetails = getDOJInboundDetails(
      dojInboundData, BDMRESTACTIONTYPE.BDM_REST_PUT, bdmapiAuditDetails);

    final MaintainDOJLiability dojLibility =
      new curam.ca.gc.bdm.sl.maintaindojliability.impl.MaintainDOJLiability();

    try {
      BDMExternalLiabilityKey externalLiabilityKey;
      final BDMCheckDOJLiabilityExistDetails dojLbyDetails =
        new BDMCheckDOJLiabilityExistDetails();
      dojLbyDetails.concernRoleID = dojInboundDetails.getConcernRoleID();
      dojLbyDetails.sinIdentification =
        Integer.toString(dojInboundDetails.getPersonSINIdentification());
      dojLbyDetails.obligationIDSuffix =
        dojInboundDetails.getPersonObligationIDSuffix();
      // This is for BDMEXTERNALLIABILITYTYPE.DOJ_FEES;
      dojLbyDetails.isFeeTypeInd = true;
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
      // This is for BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS;
      dojLbyDetails.isFeeTypeInd = false;
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
        BDMRESTACTIONTYPE.BDM_REST_PUT, dojInboundDetails.getConcernRoleID());
      // BEGIN, ADO-55810, implementing gaps for DOJ inbound
      bdmapiAuditDetails = auditDetailsBuilder
        .setResponseTransactionDateTime(DateTime.getCurrentDateTime())
        .setResponseObject(dojInboundData).setStatusCode(HttpStatus.SC_OK)
        .build();

      bdmapiAuditUtil.auditAPI(bdmapiAuditDetails);
    } catch (final Exception e) {
      if (!e.getClass().getCanonicalName()
        .equals(AppException.class.getCanonicalName())) {
        // If there are any other exceptions other than AppException which is
        // handled in APIs, then 500:Internal Server error will be
        // thrown by Curam REST infra and this logic will log that entry in
        // audit table. The exception caught is thrown after storing in auditing
        Trace.kTopLevelLogger.info(
          "DOJInboundAPI InboundData Error occurred while processing Transaction ID = "
            + dojInboundData.metadata.transactionControlIdentificationID);
        BDMRestUtil.auditErrorResponse(
          new AppException(INFRASTRUCTURE.ID_UNHANDLED, e),
          HttpStatus.SC_INTERNAL_SERVER_ERROR, bdmapiAuditDetails);

        logger.logRestAPIPerf(kMethod_modifyDOJInbound, startTime, "");
      }
      // ensure exception is thrown
      throw e;
    }

    // log the performance time in milliseconds
    logger.logRestAPIPerf(kMethod_modifyDOJInbound, startTime, "");
    // END, ADO-55810, implementing gaps for DOJ inbound
    return dojInboundData;
  }

  @Override
  public void deleteDOJInbound(final BDMObligationExchange dojInboundData)
    throws AppException, InformationalException {

    // BEGIN, ADO-55810, implementing gaps for DOJ inbound
    // record the time of the call
    final long startTime = System.currentTimeMillis();

    final String correlationID = dojInboundData.cloudEvent.id;
    final String source = dojInboundData.cloudEvent.source;
    Trace.kTopLevelLogger
      .info(kMethod_deleteDOJInbound + ": CorrelationID = " + correlationID);
    final BDMAPIAuditDetailsBuilder auditDetailsBuilder =
      new BDMAPIAuditDetailsBuilder();
    BDMAPIAuditDetails bdmapiAuditDetails = auditDetailsBuilder
      .setMethod(kMethod_deleteDOJInbound)
      .setRelatedID(dojInboundData.metadata.metadataIdentificationID)
      .setRequestObject(dojInboundData).setApiType(BDMAUDITAPITYPE.BDMINBOUND)
      .setRequestTransactionDateTime(DateTime.getCurrentDateTime())
      .setSource(source).setCorrelationID(correlationID).build();
    // END, ADO-55810, implementing gaps for DOJ inbound
    try {
      final BDMDoJInboundDetails dojInboundDetails =
        getDOJInboundDetails(dojInboundData,
          BDMRESTACTIONTYPE.BDM_REST_DELETE, bdmapiAuditDetails);
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
      // This is for BDMEXTERNALLIABILITYTYPE.DOJ_FEES;
      dojLbyDetails.isFeeTypeInd = true;
      dojDeleteLbyKeyObj =
        dojLibility.getLiabilityByExtRefNumber(dojLbyDetails);
      if (dojDeleteLbyKeyObj != null) {
        dojLibility.deleteLiability(dojDeleteLbyKeyObj);
      }
      // This is for BDMEXTERNALLIABILITYTYPE.DOJ_ARREARS;
      dojLbyDetails.isFeeTypeInd = false;
      dojDeleteLbyKeyObj =
        dojLibility.getLiabilityByExtRefNumber(dojLbyDetails);
      if (dojDeleteLbyKeyObj != null) {
        dojLibility.deleteLiability(dojDeleteLbyKeyObj);
      }

      processDOJDataIntoStagingTable(dojInboundData,
        BDMRESTACTIONTYPE.BDM_REST_DELETE,
        dojInboundDetails.getConcernRoleID());
      // BEGIN, ADO-55810, implementing gaps for DOJ inbound
      bdmapiAuditDetails = auditDetailsBuilder
        .setResponseTransactionDateTime(DateTime.getCurrentDateTime())
        .setStatusCode(HttpStatus.SC_NO_CONTENT).build();

      bdmapiAuditUtil.auditAPI(bdmapiAuditDetails);

    } catch (final Exception e) {
      if (!e.getClass().getCanonicalName()
        .equals(AppException.class.getCanonicalName())) {
        // If there are any other exceptions other than AppException which is
        // handled in APIs, then 500:Internal Server error will be
        // thrown by Curam REST infra and this logic will log that entry in
        // audit table. The exception caught is thrown after storing in auditing
        Trace.kTopLevelLogger.info(
          "DOJInboundAPI InboundData Error occurred while processing Transaction ID = "
            + dojInboundData.metadata.transactionControlIdentificationID);
        BDMRestUtil.auditErrorResponse(
          new AppException(INFRASTRUCTURE.ID_UNHANDLED, e),
          HttpStatus.SC_INTERNAL_SERVER_ERROR, bdmapiAuditDetails);
        logger.logRestAPIPerf(kMethod_deleteDOJInbound, startTime, "");
      }
      // ensure exception is thrown
      throw e;
    }

    // log the performance time in milliseconds
    logger.logRestAPIPerf(kMethod_deleteDOJInbound, startTime, "");
    // END, ADO-55810, implementing gaps for DOJ Inbound
  }

  /**
   *
   * @param dojInboundData
   * @param restActionType
   * @return
   */
  BDMDoJInboundDetails getDOJInboundDetails(
    final BDMObligationExchange dojInboundData, final String restActionType,
    final BDMAPIAuditDetails bdmapiAuditDetails)
    throws AppException, InformationalException {

    final BDMDoJInboundDetails dojInboundDetailsObj =
      new BDMDoJInboundDetails();
    // BEGIN, ADO-55810, commenting out SIN details to log
    // Trace.kTopLevelLogger.info("processDOJInbound personSINIdentification="
    // + dojInboundData.obligation.person.personSINIdentification);
    // END, ADO-55810, commenting out SIN details to log
    Trace.kTopLevelLogger.info("processDOJInbound personObligationIDSuffix="
      + dojInboundData.obligation.person.personObligationIDSuffix);
    Trace.kTopLevelLogger
      .info("DOJInboundAPI InboundData Transaction Control ID = "
        + dojInboundData.metadata.transactionControlIdentificationID
        + " AND ActionType = " + restActionType);

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
          // + ", For Person SIN ="
          // + dojInboundData.obligation.person.personSINIdentification
          + "DOJInboundAPI InboundData Transaction Control ID = "
          + dojInboundData.metadata.transactionControlIdentificationID
          + " AND ActionType = " + restActionType);

      // BEGIN, ADO-55810, throw 404 error when no record found for the SIN sent
      final AppException appException = new AppException(
        BDMRESTAPIERRORMESSAGE.HTTP_404_RESOURCE_NOT_FOUND_SINERROR_DOJ);
      BDMRestUtil.throwHTTP404Status(appException, bdmapiAuditDetails);
      // END, ADO-55810, throw 404 error when no record found for the SIN sent
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
    final BDMObligationExchange dojInboundData, final String restActionType,
    final long concernroleID) throws InformationalException, AppException {

    // BEGIN, ADO-55810, commenting out SIN details to log
    // Trace.kTopLevelLogger.info("processDOJInbound personSINIdentification="
    // + dojInboundData.obligation.person.personSINIdentification);
    // END, ADO-55810, commenting out SIN details to log
    Trace.kTopLevelLogger.info("processDOJInbound personObligationIDSuffix="
      + dojInboundData.obligation.person.personObligationIDSuffix);
    Trace.kTopLevelLogger
      .info("DOJInboundAPI InboundData Transaction Control ID = "
        + dojInboundData.metadata.transactionControlIdentificationID
        + " AND ActionType = " + restActionType);

    final BDMDoJInboundStaging doJInboundStagingObj =
      BDMDoJInboundStagingFactory.newInstance();

    final BDMDoJInboundStagingDtls dojInboundStaginDtlsObj =
      new BDMDoJInboundStagingDtls();
    dojInboundStaginDtlsObj.creationDate = Date.getCurrentDate();
    dojInboundStaginDtlsObj.status = BDMDOJRECORDSTATUS.PENDING;
    dojInboundStaginDtlsObj.restActionType = restActionType;
    // BEGIN, ADO-55810,
    // removing the repeated code for getting concernroleID from SIN number, it is
    // already done in the start of the method using 'getDOJInboundDetails', so the
    // value is sent as input to the method and set in BDMDoJInboundStagingDtls

    dojInboundStaginDtlsObj.concernRoleID = concernroleID;
    // END, ADO-55810,
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

  }

}
