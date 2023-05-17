package curam.ca.gc.bdm.batch.bdmgenerateamendtaxslipdatabatch.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.batch.bdmgeneratetaxslipdatabatch.struct.BDMGenerateTaxSlipDataBatchKey;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.codetable.BDMPMTRECONCILIATIONSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPATTRTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPCONFIGSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPCREATIONMETHOD;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPFORMTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPPROCSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPTYPE;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentInstrumentDAFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.intf.BDMPaymentInstrumentDA;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentsFlaggedForAmendTaxSlipDetails;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentsFlaggedForAmendTaxSlipDetailsList;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentsFlaggedForAmendTaxSlipKey;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMPaymentsNotFlaggedForAmendTaxSlipKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataPaymentLinkFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataRL1Factory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataT4AFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipProviderConfigFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataPaymentLink;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataRL1;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataT4A;
import curam.ca.gc.bdm.entity.financial.struct.BDMConcernRoleYearStatusKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadByTaxSlipDataIDKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadExistingSlipsDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadExistingSlipsDtlsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadExistingSlipsKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSpecificAttributeTextKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSpecificAttributeValueKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataPaymentLinkDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataPaymentLinkDtlsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Dtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4ADtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipNearestProviderConfigKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipProviderConfigKey;
import curam.ca.gc.bdm.entity.struct.BDMTaxSlipMRQBatchSequenceKeyStruct1;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMTAXSLIPS;
import curam.ca.gc.bdm.sl.financial.managetaxslips.intf.BDMManageTaxSlips;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipClientDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipStoreAttrDataKey;
import curam.ca.gc.bdm.sl.financial.struct.BDMUpdateNextTaxSlipKey;
import curam.ca.gc.bdm.sl.interfaces.bdmtaxslipmrq.impl.BDMTaxSlipMrqBatchImpl;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.ILICATEGORY;
import curam.codetable.ILITYPE;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.sl.struct.RecordCount;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BDMGenerateAmendTaxSlipDataBatchStream extends
  curam.ca.gc.bdm.batch.bdmgenerateamendtaxslipdatabatch.base.BDMGenerateAmendTaxSlipDataBatchStream {

  private final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

  int totalProcessed = 0;

  @Inject
  BDMManageTaxSlips manageTaxSlipsObj;

  public BDMGenerateAmendTaxSlipDataBatchStream() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void
    process(final BDMGenerateTaxSlipDataBatchKey batchProcessStreamKey)
      throws AppException, InformationalException {

    if (batchProcessStreamKey.instanceID.isEmpty()) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_GENERATE_AMEND_TAX_SLIP_DATA_BATCH;
    }

    final BDMGenerateAmendTaxSlipDataBatchStreamWrapper streamWrapper =
      new BDMGenerateAmendTaxSlipDataBatchStreamWrapper(this,
        batchProcessStreamKey);

    final BatchProcessStreamKey key = new BatchProcessStreamKey();
    key.instanceID = batchProcessStreamKey.instanceID;
    batchStreamHelper.runStream(key, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    final String chunkResult =
      totalProcessed + CuramConst.gkTabDelimiter + skippedCasesCount;

    // reset count
    totalProcessed = 0;

    return chunkResult;
  }

  @Override
  public BatchProcessingSkippedRecord processRecord(
    final BatchProcessingID batchProcessingID,
    final BDMGenerateTaxSlipDataBatchKey key)
    throws AppException, InformationalException {

    BatchProcessingSkippedRecord skippedRecord = null;
    try {
      final long concernRoleID = batchProcessingID.recordID;
      final Date yearStartDate = Date.fromISO8601(key.taxYear + "0101");
      final Date yearEndDate = Date.fromISO8601(key.taxYear + "1231");
      final int taxYear = key.taxYear;
      final BDMPaymentInstrumentDA bdmPaymentInstrumentDA =
        BDMPaymentInstrumentDAFactory.newInstance();

      // search for tax payments for the given concern role
      final BDMPaymentsFlaggedForAmendTaxSlipKey searchTaxAmountsKey =
        new BDMPaymentsFlaggedForAmendTaxSlipKey();
      searchTaxAmountsKey.addToTaxSlipInd = true;
      searchTaxAmountsKey.concernRoleID = concernRoleID;
      searchTaxAmountsKey.reconcilStatusCancel =
        BDMPMTRECONCILIATIONSTATUS.CANCELED;
      searchTaxAmountsKey.reconcilStatusIssued =
        BDMPMTRECONCILIATIONSTATUS.ISSUED;
      searchTaxAmountsKey.iliCategory = ILICATEGORY.PAYMENTINSTRUCTION;
      searchTaxAmountsKey.iliTypePmtDed = ILITYPE.DEDUCTIONITEM;
      searchTaxAmountsKey.preTaxDeductionInd = true;
      searchTaxAmountsKey.taxReportInd = true;
      searchTaxAmountsKey.deductionTypeFed =
        BDMDEDUCTIONTYPE.BASIC_FEDERAL_TAX;
      searchTaxAmountsKey.deductionTypeFedVTW =
        BDMDEDUCTIONTYPE.VOLUNTARY_TAX_WITHHOLD_FED;
      searchTaxAmountsKey.deductionTypeProv =
        BDMDEDUCTIONTYPE.BASIC_PROVINCIAL_TAX;
      searchTaxAmountsKey.deductionTypeProvVTW =
        BDMDEDUCTIONTYPE.VOLUNTARY_TAX_WITHHOLD_Prov;

      final BDMPaymentsFlaggedForAmendTaxSlipDetailsList allTaxSlipAmounts =
        bdmPaymentInstrumentDA
          .searchPaymentsFlaggedForAmendTaxSlip(searchTaxAmountsKey);

      final List<BDMPaymentsFlaggedForAmendTaxSlipDetails> taxSlipAmounts =
        new ArrayList<>();
      final Set<Long> taxSlipPmtInstrumentIDs = new HashSet<>();

      final BDMPaymentInstrument bdmPIObj =
        BDMPaymentInstrumentFactory.newInstance();
      final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
        new BDMPaymentInstrumentKey();
      // filter all the tax slip
      for (final BDMPaymentsFlaggedForAmendTaxSlipDetails taxSlipDtls : allTaxSlipAmounts.dtls) {
        // if they are between the year start and end date, check that they have
        // a valid status
        if (taxSlipDtls.paymentDueDate.after(yearStartDate.addDays(-1))
          && taxSlipDtls.paymentDueDate.before(yearEndDate.addDays(1))) {
          if (taxSlipDtls.reconcilStatusCode
            .equals(BDMPMTRECONCILIATIONSTATUS.CANCELED)) {
            taxSlipAmounts.add(taxSlipDtls);
            taxSlipPmtInstrumentIDs.add(taxSlipDtls.pmtInstrumentID);
          }
        }
        // if they were due after the year end date, check if there was a
        // cancellation in the prior year
        else if (taxSlipDtls.paymentDueDate.after(yearEndDate)) {

          if (taxSlipDtls.reconcilStatusCode
            .equals(BDMPMTRECONCILIATIONSTATUS.ISSUED)
            && taxSlipDtls.cancelPmtInstrumentID != 0) {
            // read the cancelled payment instrument details
            bdmPaymentInstrumentKey.pmtInstrumentID =
              taxSlipDtls.cancelPmtInstrumentID;
            final BDMPaymentInstrumentDtls bdmPIDtls =
              bdmPIObj.read(bdmPaymentInstrumentKey);
            // check the cancellation date was between the tax year start and
            // end date
            if (bdmPIDtls.paymentDueDate.after(yearStartDate.addDays(-1))
              && bdmPIDtls.paymentDueDate.before(yearEndDate.addDays(1))) {
              taxSlipAmounts.add(taxSlipDtls);
              taxSlipPmtInstrumentIDs.add(taxSlipDtls.pmtInstrumentID);
            }

          }
        }
      }

      boolean skipT4AProcessing = false;
      boolean skipRL1Processing = false;
      RecordCount taxSlipsNotTaggedForAmend = new RecordCount();
      // if there are no tax slips still eligible, then skip amendment
      // processing
      if (taxSlipAmounts.size() == 0) {
        skipT4AProcessing = true;
        skipRL1Processing = true;
      } else {
        // look for payments that are not flagged for amendment (ie. could be
        // waiting to be processed)
        final BDMPaymentsNotFlaggedForAmendTaxSlipKey notFlaggedForAmendKey =
          new BDMPaymentsNotFlaggedForAmendTaxSlipKey();
        notFlaggedForAmendKey.addToTaxSlipInd = true;
        notFlaggedForAmendKey.concernRoleID = concernRoleID;
        notFlaggedForAmendKey.yearEndDate = yearEndDate;
        notFlaggedForAmendKey.yearStartDate = yearStartDate;
        notFlaggedForAmendKey.reconcilStatusCancel =
          BDMPMTRECONCILIATIONSTATUS.CANCELED;
        notFlaggedForAmendKey.reconcilStatusIssued =
          BDMPMTRECONCILIATIONSTATUS.ISSUED;
        taxSlipsNotTaggedForAmend = bdmPaymentInstrumentDA
          .countPaymentsFlaggedNotForAmendTaxSlip(notFlaggedForAmendKey);

      }

      final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
      final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

      boolean manualSlipIssued = false;

      if (!skipT4AProcessing) {
        final BDMReadExistingSlipsKey existingSlipsKey =
          new BDMReadExistingSlipsKey();
        existingSlipsKey.concernRoleID = concernRoleID;
        existingSlipsKey.taxYear = key.taxYear;
        existingSlipsKey.taxSlipStatusCode1 = BDMTAXSLIPSTATUS.REQUESTED;
        existingSlipsKey.taxSlipStatusCode2 = BDMTAXSLIPSTATUS.ISSUED;

        // check if system created tax slip that has been deleted by user
        existingSlipsKey.creationMethodType = BDMTAXSLIPCREATIONMETHOD.SYSTEM;
        existingSlipsKey.taxSlipStatusCode3 = BDMTAXSLIPSTATUS.DELETED;

        // check for existing T4A tax slips that may have been created manually,
        // created by the system and then deleted manually, or are in requested
        // status
        final BDMReadExistingSlipsDtlsList existingT4AList =
          t4aObj.readExistingSlipsByYearStatus(existingSlipsKey);

        for (final BDMReadExistingSlipsDtls t4a : existingT4AList.dtls) {
          if (t4a.creationMethodType.equals(BDMTAXSLIPCREATIONMETHOD.MANUAL)
            && t4a.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.ISSUED)) {
            manualSlipIssued = true;
            skipT4AProcessing = true;
          } else if (t4a.creationMethodType
            .equals(BDMTAXSLIPCREATIONMETHOD.SYSTEM)
            && t4a.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.DELETED)) {
            manualSlipIssued = true;
            skipT4AProcessing = true;
          } else if (t4a.taxSlipStatusCode
            .equals(BDMTAXSLIPSTATUS.REQUESTED)) {
            skipT4AProcessing = true;
          }
        }
      }

      if (!skipRL1Processing) {
        final BDMReadExistingSlipsKey existingSlipsKey =
          new BDMReadExistingSlipsKey();
        existingSlipsKey.concernRoleID = concernRoleID;
        existingSlipsKey.taxYear = key.taxYear;
        existingSlipsKey.taxSlipStatusCode1 = BDMTAXSLIPSTATUS.REQUESTED;
        existingSlipsKey.taxSlipStatusCode2 = BDMTAXSLIPSTATUS.ISSUED;

        // check if system created tax slip that has been deleted by user
        existingSlipsKey.creationMethodType = BDMTAXSLIPCREATIONMETHOD.SYSTEM;
        existingSlipsKey.taxSlipStatusCode3 = BDMTAXSLIPSTATUS.DELETED;
        // check for existing RL1 tax slips that may have been created manually,
        // created by the system and then deleted manually, or are in requested
        // status
        final BDMReadExistingSlipsDtlsList existingRL1List =
          rl1Obj.readExistingSlipsByYearStatus(existingSlipsKey);

        for (final BDMReadExistingSlipsDtls rl1 : existingRL1List.dtls) {
          if (rl1.creationMethodType.equals(BDMTAXSLIPCREATIONMETHOD.MANUAL)
            && rl1.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.ISSUED)) {
            manualSlipIssued = true;
            skipRL1Processing = true;
          } else if (rl1.creationMethodType
            .equals(BDMTAXSLIPCREATIONMETHOD.SYSTEM)
            && rl1.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.DELETED)) {
            manualSlipIssued = true;
            skipRL1Processing = true;
          } else if (rl1.taxSlipStatusCode
            .equals(BDMTAXSLIPSTATUS.REQUESTED)) {
            skipRL1Processing = true;
          }
        }

      }

      if (!skipT4AProcessing) {
        final NotFoundIndicator t4aNfIndicator = new NotFoundIndicator();

        // look for an existing tax slip
        final BDMConcernRoleYearStatusKey t4aKey =
          new BDMConcernRoleYearStatusKey();
        t4aKey.concernRoleID = concernRoleID;
        t4aKey.taxSlipStatusCode = BDMTAXSLIPSTATUS.ISSUED;
        t4aKey.taxYear = taxYear;

        t4aObj.readByConcernRoleYearStatus(t4aNfIndicator, t4aKey);

        if (t4aNfIndicator.isNotFound()) {
          // if there is no existing tax slip and there are payment instruments
          // that still require processing, skip
          if (taxSlipsNotTaggedForAmend.count > 0) {
            skipT4AProcessing = true;

          }
        }
      }

      if (!skipRL1Processing) {
        final NotFoundIndicator rl1NfIndicator = new NotFoundIndicator();

        final BDMConcernRoleYearStatusKey rl1Key =
          new BDMConcernRoleYearStatusKey();
        rl1Key.concernRoleID = concernRoleID;
        rl1Key.taxSlipStatusCode = BDMTAXSLIPSTATUS.ISSUED;
        rl1Key.taxYear = taxYear;
        rl1Obj.readByConcernRoleYearStatus(rl1NfIndicator, rl1Key);

        // look for existing tax slip
        if (rl1NfIndicator.isNotFound()) {

          if (taxSlipsNotTaggedForAmend.count > 0) {
            // if there is no existing tax slip and there are payment
            // instruments that still require processing, skip
            skipRL1Processing = true;

          }
        }

      }

      BDMTaxSlipClientDetails clientDetails = new BDMTaxSlipClientDetails();

      // if either t4a or rl1 needs processing, retrieve the client details for
      // names, SIN, and address
      if (!skipT4AProcessing || !skipRL1Processing) {

        clientDetails =
          manageTaxSlipsObj.populateClientDetails(concernRoleID, yearEndDate);

      }

      if (!skipT4AProcessing) {
        double otherIncome = 0;
        double incomeTaxDeducted = 0;

        for (final BDMPaymentsFlaggedForAmendTaxSlipDetails taxAmount : taxSlipAmounts) {
          // if it is a cancelled payment, subtract the amount instead to find
          // the change
          if (taxAmount.reconcilStatusCode
            .equals(BDMPMTRECONCILIATIONSTATUS.CANCELED)) {
            otherIncome =
              otherIncome - (taxAmount.taxablePaymentAmount.getValue()
                - taxAmount.preTaxDeductionAmount.getValue());
            incomeTaxDeducted =
              incomeTaxDeducted - (taxAmount.fedTaxAmount.getValue()
                + taxAmount.fedVTWTaxAmount.getValue());
          } else {
            otherIncome =
              otherIncome + taxAmount.taxablePaymentAmount.getValue()
                - taxAmount.preTaxDeductionAmount.getValue();
            incomeTaxDeducted =
              incomeTaxDeducted + taxAmount.fedTaxAmount.getValue()
                + taxAmount.fedVTWTaxAmount.getValue();
          }

        }

        if (otherIncome != 0 || incomeTaxDeducted != 0) {

          // populate details
          final BDMTaxSlipDataT4ADtls t4aDtls = new BDMTaxSlipDataT4ADtls();
          // generate CRA sequence number
          final BDMPaymentUtil payUtil = new BDMPaymentUtil();
          final int sequenceNumber =
            payUtil.getSeqNumber(BDMConstants.kCRASeqType);

          t4aDtls.taxSlipDataID = UniqueID.nextUniqueID();
          t4aDtls.concernRoleID = concernRoleID;
          t4aDtls.taxYear = key.taxYear;
          t4aDtls.sequenceNumber = sequenceNumber;
          t4aDtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.REQUESTED;
          t4aDtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.SYSTEM;
          t4aDtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
          t4aDtls.processingDateTime = DateTime.getCurrentDateTime();
          t4aDtls.recipientSurName = clientDetails.recipientSurName;
          t4aDtls.recipientFirstName = clientDetails.recipientFirstName;
          t4aDtls.recipientInitial = clientDetails.recipientInitial;
          t4aDtls.recipientSIN = clientDetails.recipientSIN;
          t4aDtls.recipientAddressLine1 =
            clientDetails.recipientAddressLine1_T4A;
          t4aDtls.recipientAddressLine2 =
            clientDetails.recipientAddressLine2_T4A;
          t4aDtls.recipientCity = clientDetails.recipientCity;
          t4aDtls.recipientProvince = clientDetails.recipientProv;
          t4aDtls.recipientCountryCode = clientDetails.recipientCountryCode;
          t4aDtls.recipientPostalCode = clientDetails.recipientPostalCode;
          t4aDtls.creationDateTime = DateTime.getCurrentDateTime();
          t4aDtls.statusIndianInd = clientDetails.statusIndianInd;

          final NotFoundIndicator t4aNfIndicator = new NotFoundIndicator();
          final BDMConcernRoleYearStatusKey t4aKey =
            new BDMConcernRoleYearStatusKey();
          t4aKey.concernRoleID = concernRoleID;
          t4aKey.taxSlipStatusCode = BDMTAXSLIPSTATUS.ISSUED;
          t4aKey.taxYear = taxYear;

          final BDMTaxSlipDataT4ADtls t4aActiveDtls =
            t4aObj.readByConcernRoleYearStatus(t4aNfIndicator, t4aKey);

          // if there is no existing tax slip, then this is an original tax slip
          if (t4aNfIndicator.isNotFound()) {
            if (otherIncome < 0) {
              throw new AppException(
                BDMTAXSLIPS.ERR_TAX_SLIP_NEGATIVE_AMOUNT);
            }
            t4aDtls.slipTypeCode = BDMTAXSLIPTYPE.ORIGINAL;
            t4aDtls.reportTypeCode = "O"; // hardcoded for now

          } else {

            // get the amended income by adding the calculated change to the
            // original t4a income
            if (t4aActiveDtls.statusIndianInd) {
              // read the original income for the attribute table
              final BDMSpecificAttributeTextKey attrKey =
                new BDMSpecificAttributeTextKey();
              attrKey.taxSlipDataID = t4aDtls.taxSlipDataID;
              attrKey.attrType = BDMTAXSLIPATTRTYPE.T4A_INDIAN_OTHER_INCOME;
              final BDMSpecificAttributeValueKey valueKey =
                manageTaxSlipsObj.readSpecificAttributeText(attrKey);

              double originalIncome = 0;

              if (!StringUtil.isNullOrEmpty(valueKey.attrValue)) {
                originalIncome = Double.parseDouble(valueKey.attrValue);
              }

              otherIncome = originalIncome + otherIncome;
            } else {
              final BDMSpecificAttributeTextKey attrKey =
                new BDMSpecificAttributeTextKey();
              attrKey.taxSlipDataID = t4aActiveDtls.taxSlipDataID;
              attrKey.attrType = BDMTAXSLIPATTRTYPE.T4A_OTHER_INCOME;
              final BDMSpecificAttributeValueKey valueKey =
                manageTaxSlipsObj.readSpecificAttributeText(attrKey);

              double originalIncome = 0;

              if (!StringUtil.isNullOrEmpty(valueKey.attrValue)) {
                originalIncome = Double.parseDouble(valueKey.attrValue);
              }

              otherIncome = originalIncome + otherIncome;
            }
            final BDMSpecificAttributeTextKey attrKey =
              new BDMSpecificAttributeTextKey();
            attrKey.taxSlipDataID = t4aActiveDtls.taxSlipDataID;
            attrKey.attrType = BDMTAXSLIPATTRTYPE.T4A_TAX_DEDUCTED;
            final BDMSpecificAttributeValueKey valueKey =
              manageTaxSlipsObj.readSpecificAttributeText(attrKey);

            double originalIncomeTaxDeducted = 0;

            if (!StringUtil.isNullOrEmpty(valueKey.attrValue)) {
              originalIncomeTaxDeducted =
                Double.parseDouble(valueKey.attrValue);
            }

            incomeTaxDeducted = incomeTaxDeducted + originalIncomeTaxDeducted;

            // if only cancelled payments are part of amendment, then it is a
            // cancelled slip type, otherwise amended
            if (otherIncome == 0 && incomeTaxDeducted == 0) {
              t4aDtls.slipTypeCode = BDMTAXSLIPTYPE.CANCELLED;
              t4aDtls.reportTypeCode = "C";
            } else if (t4aActiveDtls.slipTypeCode
              .equals(BDMTAXSLIPTYPE.CANCELLED)) {
              t4aDtls.slipTypeCode = BDMTAXSLIPTYPE.ORIGINAL;
              t4aDtls.reportTypeCode = "O";
            } else {
              t4aDtls.slipTypeCode = BDMTAXSLIPTYPE.AMENDED;
              t4aDtls.reportTypeCode = "A";
            }
          }

          if (clientDetails.statusIndianInd) {
            t4aDtls.indianOtherIncome = new Money(otherIncome);
          } else {
            t4aDtls.otherIncome = new Money(otherIncome);
          }
          t4aDtls.incomeTaxDeducted = new Money(incomeTaxDeducted);

          // get the provider details
          final BDMTaxSlipNearestProviderConfigKey providerKey =
            new BDMTaxSlipNearestProviderConfigKey();
          providerKey.currentDate = Date.getCurrentDate();
          providerKey.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
          providerKey.statusCode = BDMTAXSLIPCONFIGSTATUS.ACTIVE;
          final BDMTaxSlipProviderConfigKey providerConfigKey =
            BDMTaxSlipProviderConfigFactory.newInstance()
              .getNearestConfig(providerKey);

          t4aDtls.taxSlipProviderConfigID =
            providerConfigKey.taxSlipProviderConfigID;

          manageTaxSlipsObj.formatT4AClientDetails(t4aDtls);

          // update next tax slip data id for predecessor
          final BDMUpdateNextTaxSlipKey t4aUpdateKey =
            new BDMUpdateNextTaxSlipKey();
          t4aUpdateKey.concernRoleID = concernRoleID;
          t4aUpdateKey.nextTaxSlipDataID = t4aDtls.taxSlipDataID;
          t4aUpdateKey.taxYear = key.taxYear;
          manageTaxSlipsObj.t4AUpdateNextTaxSlipID(t4aUpdateKey);

          t4aObj.insert(t4aDtls);

          final BDMTaxSlipStoreAttrDataKey storeAttrDataKey =
            new BDMTaxSlipStoreAttrDataKey();
          storeAttrDataKey.clientDetails = clientDetails;
          storeAttrDataKey.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
          storeAttrDataKey.taxSlipDataID = t4aDtls.taxSlipDataID;
          storeAttrDataKey.taxYear = key.taxYear;
          storeAttrDataKey.reportTypeCode = t4aDtls.reportTypeCode;
          storeAttrDataKey.taxSlipProviderConfigID =
            t4aDtls.taxSlipProviderConfigID;
          storeAttrDataKey.incomeTaxDeducted =
            Double.toString(incomeTaxDeducted);
          if (clientDetails.statusIndianInd) {
            storeAttrDataKey.indianOtherIncome = Double.toString(otherIncome);
          } else {
            storeAttrDataKey.otherIncome = Double.toString(otherIncome);
          }
          storeAttrDataKey.sequenceNumber =
            Long.toString(t4aDtls.sequenceNumber);

          manageTaxSlipsObj.storeAttributeData(storeAttrDataKey);

          // initialize objects/structs
          final BDMTaxSlipDataPaymentLink taxSlipPaymentLinkObj =
            BDMTaxSlipDataPaymentLinkFactory.newInstance();
          final BDMTaxSlipDataPaymentLinkDtls paymentLinkDtls =
            new BDMTaxSlipDataPaymentLinkDtls();

          // iterate through payment instruments
          for (final BDMPaymentsFlaggedForAmendTaxSlipDetails taxAmount : taxSlipAmounts) {

            // add a link between a tax slip and payment instrument
            paymentLinkDtls.taxSlipDataPaymentLinkID =
              UniqueID.nextUniqueID();
            paymentLinkDtls.taxSlipDataID = t4aDtls.taxSlipDataID;
            paymentLinkDtls.pmtInstrumentID = taxAmount.pmtInstrumentID;
            taxSlipPaymentLinkObj.insert(paymentLinkDtls);

          }

          if (!t4aNfIndicator.isNotFound()) {
            final BDMReadByTaxSlipDataIDKey taxSlipDataKey =
              new BDMReadByTaxSlipDataIDKey();
            taxSlipDataKey.taxSlipDataID = t4aActiveDtls.taxSlipDataID;
            final BDMTaxSlipDataPaymentLinkDtlsList paymentLinkDtlsList =
              taxSlipPaymentLinkObj.readByTaxSlipDataID(taxSlipDataKey);

            for (final BDMTaxSlipDataPaymentLinkDtls paymentLink : paymentLinkDtlsList.dtls) {
              // if there was a payment instrument that was linked to the old
              // tax slip data ID but was not processed by the amendment batch,
              // we still want to create a link to the new tax slip data ID
              if (!taxSlipPmtInstrumentIDs
                .contains(paymentLink.pmtInstrumentID)) {
                paymentLinkDtls.taxSlipDataPaymentLinkID =
                  UniqueID.nextUniqueID();
                paymentLinkDtls.taxSlipDataID = t4aDtls.taxSlipDataID;
                paymentLinkDtls.pmtInstrumentID = paymentLink.pmtInstrumentID;
                taxSlipPaymentLinkObj.insert(paymentLinkDtls);
              }
            }

          }
        }
      }
      if (!skipRL1Processing) {

        double otherIncome = 0;
        double incomeTaxDeducted = 0;

        for (final BDMPaymentsFlaggedForAmendTaxSlipDetails taxAmount : taxSlipAmounts) {
          // if it is a cancelled payment, subtract the amount instead to find
          // the change
          if (taxAmount.reconcilStatusCode
            .equals(BDMPMTRECONCILIATIONSTATUS.CANCELED)) {
            otherIncome =
              otherIncome - (taxAmount.taxablePaymentAmount.getValue()
                - taxAmount.preTaxDeductionAmount.getValue());
            incomeTaxDeducted =
              incomeTaxDeducted - (taxAmount.provTaxAmount.getValue()
                + taxAmount.provVTWTaxAmount.getValue());
          } else {
            otherIncome =
              otherIncome + taxAmount.taxablePaymentAmount.getValue()
                - taxAmount.preTaxDeductionAmount.getValue();
            incomeTaxDeducted =
              incomeTaxDeducted + taxAmount.provTaxAmount.getValue()
                + taxAmount.provVTWTaxAmount.getValue();
          }

        }

        if (otherIncome != 0 || incomeTaxDeducted != 0
          || clientDetails.quebecAddressInd) {
          // populate details
          final BDMTaxSlipDataRL1Dtls rl1Dtls = new BDMTaxSlipDataRL1Dtls();

          // generate sequence number
          final BDMTaxSlipMRQBatchSequenceKeyStruct1 sequenceKey =
            new BDMTaxSlipMRQBatchSequenceKeyStruct1();
          sequenceKey.concernRoleID = concernRoleID;
          sequenceKey.taxYear = key.taxYear;
          final long sequenceNumber =
            BDMTaxSlipMrqBatchImpl.getSequence(sequenceKey);

          rl1Dtls.taxSlipDataID = UniqueID.nextUniqueID();
          rl1Dtls.concernRoleID = concernRoleID;
          rl1Dtls.taxYear = key.taxYear;
          rl1Dtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.REQUESTED;
          rl1Dtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.SYSTEM;
          rl1Dtls.sequenceNumber = sequenceNumber;
          rl1Dtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
          rl1Dtls.processingDateTime = DateTime.getCurrentDateTime();
          rl1Dtls.recipientLastName = clientDetails.recipientSurName;
          rl1Dtls.recipientFirstName = clientDetails.recipientFirstName;
          rl1Dtls.recipientInitial = clientDetails.recipientInitial;
          rl1Dtls.recipientSIN = clientDetails.recipientSIN;
          rl1Dtls.recipientAddressLine1 =
            clientDetails.recipientAddressLine1_RL1;
          rl1Dtls.recipientCity = clientDetails.recipientCity;
          rl1Dtls.recipientProvince = clientDetails.recipientProv;
          rl1Dtls.recipientPostalCode = clientDetails.recipientPostalCode;
          rl1Dtls.creationDateTime = DateTime.getCurrentDateTime();
          rl1Dtls.statusIndianInd = clientDetails.statusIndianInd;

          final NotFoundIndicator rl1NfIndicator = new NotFoundIndicator();
          final BDMConcernRoleYearStatusKey rl1Key =
            new BDMConcernRoleYearStatusKey();
          rl1Key.concernRoleID = concernRoleID;
          rl1Key.taxSlipStatusCode = BDMTAXSLIPSTATUS.ISSUED;
          rl1Key.taxYear = taxYear;
          final BDMTaxSlipDataRL1Dtls rl1ActiveDtls =
            rl1Obj.readByConcernRoleYearStatus(rl1NfIndicator, rl1Key);

          // if there is no existing tax slip, then this is an original tax slip
          if (rl1NfIndicator.isNotFound()) {
            if (otherIncome < 0) {
              throw new AppException(
                BDMTAXSLIPS.ERR_TAX_SLIP_NEGATIVE_AMOUNT);
            }
            rl1Dtls.slipTypeCode = BDMTAXSLIPTYPE.ORIGINAL;
            rl1Dtls.reportTypeCode = "R"; // hardcoded for now

          } else {
            // get the amended income by adding the calculated change to the
            // original rl1 income
            if (rl1ActiveDtls.statusIndianInd) {
              // read the original income for the attribute table
              final BDMSpecificAttributeTextKey attrKey =
                new BDMSpecificAttributeTextKey();
              attrKey.taxSlipDataID = rl1ActiveDtls.taxSlipDataID;
              attrKey.attrType =
                BDMTAXSLIPATTRTYPE.RL1_INDIAN_INCOME_ON_RESERVE;
              final BDMSpecificAttributeValueKey valueKey =
                manageTaxSlipsObj.readSpecificAttributeText(attrKey);

              double originalIncome = 0;

              if (!StringUtil.isNullOrEmpty(valueKey.attrValue)) {
                originalIncome = Double.parseDouble(valueKey.attrValue);
              }

              otherIncome = originalIncome + otherIncome;
            } else {
              // read the original income for the attribute table
              final BDMSpecificAttributeTextKey attrKey =
                new BDMSpecificAttributeTextKey();
              attrKey.taxSlipDataID = rl1ActiveDtls.taxSlipDataID;
              attrKey.attrType = BDMTAXSLIPATTRTYPE.RL1_OTHER_INCOME;
              final BDMSpecificAttributeValueKey valueKey =
                manageTaxSlipsObj.readSpecificAttributeText(attrKey);

              double originalIncome = 0;

              if (!StringUtil.isNullOrEmpty(valueKey.attrValue)) {
                originalIncome = Double.parseDouble(valueKey.attrValue);
              }

              otherIncome = originalIncome + otherIncome;
            }
            // read the tax deducted for the attribute table
            final BDMSpecificAttributeTextKey attrKey =
              new BDMSpecificAttributeTextKey();
            attrKey.taxSlipDataID = rl1ActiveDtls.taxSlipDataID;
            attrKey.attrType = BDMTAXSLIPATTRTYPE.RL1_TAX_DEDUCTED;
            final BDMSpecificAttributeValueKey valueKey =
              manageTaxSlipsObj.readSpecificAttributeText(attrKey);

            double originalIncomeTaxDeducted = 0;

            if (!StringUtil.isNullOrEmpty(valueKey.attrValue)) {
              originalIncomeTaxDeducted =
                Double.parseDouble(valueKey.attrValue);
            }

            incomeTaxDeducted = incomeTaxDeducted + originalIncomeTaxDeducted;

            // if only cancelled payments are part of amendment, then it is a
            // cancelled slip type, otherwise amended
            if (otherIncome == 0 && incomeTaxDeducted == 0) {
              rl1Dtls.slipTypeCode = BDMTAXSLIPTYPE.CANCELLED;
              rl1Dtls.reportTypeCode = "A";
            } else if (rl1ActiveDtls.slipTypeCode
              .equals(BDMTAXSLIPTYPE.CANCELLED)) {
              // if prev slip is cancelled, the new one will be the original
              rl1Dtls.slipTypeCode = BDMTAXSLIPTYPE.ORIGINAL;
              rl1Dtls.reportTypeCode = "R";
            } else {
              rl1Dtls.slipTypeCode = BDMTAXSLIPTYPE.AMENDED;
              rl1Dtls.reportTypeCode = "M";
            }
          }

          rl1Dtls.incomeTaxDeducted = new Money(incomeTaxDeducted);

          if (clientDetails.statusIndianInd) {
            rl1Dtls.indianIncomeOnReserve = new Money(otherIncome);
          } else {
            rl1Dtls.otherIncomeAmount = new Money(otherIncome);
          }
          if (rl1Dtls.otherIncomeAmount.isPositive()) {
            rl1Dtls.otherIncomeSource = "RS"; // TODO: hardcoded for now
          }

          // get the provider details
          final BDMTaxSlipNearestProviderConfigKey providerKey =
            new BDMTaxSlipNearestProviderConfigKey();
          providerKey.currentDate = Date.getCurrentDate();
          providerKey.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
          providerKey.statusCode = BDMTAXSLIPCONFIGSTATUS.ACTIVE;
          final BDMTaxSlipProviderConfigKey providerConfigKey =
            BDMTaxSlipProviderConfigFactory.newInstance()
              .getNearestConfig(providerKey);

          rl1Dtls.taxSlipProviderConfigID =
            providerConfigKey.taxSlipProviderConfigID;

          manageTaxSlipsObj.formatRL1ClientDetails(rl1Dtls, concernRoleID);

          // update next tax slip data id for predecessor
          final BDMUpdateNextTaxSlipKey rl1UpdateKey =
            new BDMUpdateNextTaxSlipKey();
          rl1UpdateKey.concernRoleID = concernRoleID;
          rl1UpdateKey.nextTaxSlipDataID = rl1Dtls.taxSlipDataID;
          rl1UpdateKey.taxYear = key.taxYear;
          manageTaxSlipsObj.rl1UpdateNextTaxSlipID(rl1UpdateKey);

          rl1Obj.insert(rl1Dtls);

          final BDMTaxSlipStoreAttrDataKey storeAttrDataKey =
            new BDMTaxSlipStoreAttrDataKey();
          storeAttrDataKey.clientDetails = clientDetails;
          storeAttrDataKey.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
          storeAttrDataKey.taxSlipDataID = rl1Dtls.taxSlipDataID;
          storeAttrDataKey.taxYear = key.taxYear;
          storeAttrDataKey.reportTypeCode = rl1Dtls.reportTypeCode;
          storeAttrDataKey.taxSlipProviderConfigID =
            rl1Dtls.taxSlipProviderConfigID;
          storeAttrDataKey.incomeTaxDeducted =
            Double.toString(incomeTaxDeducted);
          if (clientDetails.statusIndianInd) {
            storeAttrDataKey.indianOtherIncome = Double.toString(otherIncome);
          } else {
            storeAttrDataKey.otherIncome = Double.toString(otherIncome);
          }
          storeAttrDataKey.sequenceNumber =
            Long.toString(rl1Dtls.sequenceNumber);

          manageTaxSlipsObj.storeAttributeData(storeAttrDataKey);

          // initialize objects/structs
          final BDMTaxSlipDataPaymentLink taxSlipPaymentLinkObj =
            BDMTaxSlipDataPaymentLinkFactory.newInstance();
          final BDMTaxSlipDataPaymentLinkDtls paymentLinkDtls =
            new BDMTaxSlipDataPaymentLinkDtls();

          // iterate through payment instruments
          for (final BDMPaymentsFlaggedForAmendTaxSlipDetails taxAmount : taxSlipAmounts) {

            // add a link between a tax slip and payment instrument
            paymentLinkDtls.taxSlipDataPaymentLinkID =
              UniqueID.nextUniqueID();
            paymentLinkDtls.taxSlipDataID = rl1Dtls.taxSlipDataID;
            paymentLinkDtls.pmtInstrumentID = taxAmount.pmtInstrumentID;
            taxSlipPaymentLinkObj.insert(paymentLinkDtls);

          }

          if (!rl1NfIndicator.isNotFound()) {
            final BDMReadByTaxSlipDataIDKey taxSlipDataKey =
              new BDMReadByTaxSlipDataIDKey();
            taxSlipDataKey.taxSlipDataID = rl1ActiveDtls.taxSlipDataID;
            final BDMTaxSlipDataPaymentLinkDtlsList paymentLinkDtlsList =
              taxSlipPaymentLinkObj.readByTaxSlipDataID(taxSlipDataKey);

            for (final BDMTaxSlipDataPaymentLinkDtls paymentLink : paymentLinkDtlsList.dtls) {
              // if there was a payment instrument that was linked to the old
              // tax slip data ID but was not processed by the amendment batch,
              // we still want to create a link to the new tax slip data ID
              if (!taxSlipPmtInstrumentIDs
                .contains(paymentLink.pmtInstrumentID)) {
                paymentLinkDtls.taxSlipDataPaymentLinkID =
                  UniqueID.nextUniqueID();
                paymentLinkDtls.taxSlipDataID = rl1Dtls.taxSlipDataID;
                paymentLinkDtls.pmtInstrumentID = paymentLink.pmtInstrumentID;
                taxSlipPaymentLinkObj.insert(paymentLinkDtls);
              }
            }

          }

        }

      }

      if (!skipT4AProcessing || !skipRL1Processing || manualSlipIssued) {
        for (final BDMPaymentsFlaggedForAmendTaxSlipDetails taxAmountDtls : taxSlipAmounts) {
          // set taxSlipGenInd to true for each BDMPaymentInstrument
          // record with a tax slip generated
          bdmPaymentInstrumentKey.pmtInstrumentID =
            taxAmountDtls.pmtInstrumentID;
          final BDMPaymentInstrumentDtls bdmPIDtls =
            bdmPIObj.read(bdmPaymentInstrumentKey);
          bdmPIDtls.taxSlipGenInd = true;
          bdmPIDtls.addToTaxSlipInd = false;
          bdmPIObj.modify(bdmPaymentInstrumentKey, bdmPIDtls);
        }

        totalProcessed++;
      }

    } catch (final Exception e) {
      skippedRecord = new BatchProcessingSkippedRecord();
      skippedRecord.recordID = batchProcessingID.recordID;
      skippedRecord.errorMessage = e.getMessage();

      e.printStackTrace();
    }
    return skippedRecord;
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

}
