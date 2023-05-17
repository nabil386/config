package curam.ca.gc.bdm.batch.bdmgeneratetaxslipdatabatch.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.batch.bdmgeneratetaxslipdatabatch.struct.BDMGenerateTaxSlipDataBatchKey;
import curam.ca.gc.bdm.codetable.BDMDEDUCTIONTYPE;
import curam.ca.gc.bdm.codetable.BDMPMTRECONCILIATIONSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPCONFIGSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPCREATIONMETHOD;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPFORMTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPPROCSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPTYPE;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.fact.BDMPaymentInstrumentDAFactory;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMTaxSlipAmountsByClientDetails;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMTaxSlipAmountsByClientDetailsList;
import curam.ca.gc.bdm.entity.bdmgeneratepaymentstagingdata.struct.BDMTaxSlipAmountsByClientKey;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentInstrumentFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataPaymentLinkFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataRL1Factory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataT4AFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipProviderConfigFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentInstrument;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataPaymentLink;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataRL1;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataT4A;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMPaymentInstrumentKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadExistingSlipsDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadExistingSlipsDtlsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadExistingSlipsKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataPaymentLinkDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Dtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4ADtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipNearestProviderConfigKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipProviderConfigKey;
import curam.ca.gc.bdm.entity.struct.BDMTaxSlipMRQBatchSequenceKeyStruct1;
import curam.ca.gc.bdm.impl.BDMConstants;
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
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Money;
import curam.util.type.UniqueID;
import java.util.ArrayList;
import java.util.List;

public class BDMGenerateTaxSlipDataBatchStream extends
  curam.ca.gc.bdm.batch.bdmgeneratetaxslipdatabatch.base.BDMGenerateTaxSlipDataBatchStream {

  int totalProcessed = 0;

  private final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

  @Inject
  BDMManageTaxSlips manageTaxSlipsObj;

  public BDMGenerateTaxSlipDataBatchStream() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void
    process(final BDMGenerateTaxSlipDataBatchKey batchProcessStreamKey)
      throws AppException, InformationalException {

    if (batchProcessStreamKey.instanceID.isEmpty()) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_GENERATE_TAX_SLIP_DATA_BATCH;
    }

    final BDMGenerateTaxSlipDataBatchStreamWrapper streamWrapper =
      new BDMGenerateTaxSlipDataBatchStreamWrapper(this,
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

      final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
      final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

      final long concernRoleID = batchProcessingID.recordID;
      final Date yearStartDate = Date.fromISO8601(key.taxYear + "0101");
      final Date yearEndDate = Date.fromISO8601(key.taxYear + "1231");

      boolean t4aGenerated = false;
      boolean rl1Generated = false;

      boolean manualSlipIssued = false;
      boolean skipT4AProcessing = false;
      boolean skipRL1Processing = false;

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
        } else if (t4a.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.REQUESTED)) {
          skipT4AProcessing = true;
        }

      }
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
        } else if (rl1.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.REQUESTED)) {
          skipRL1Processing = true;
        }
      }

      BDMTaxSlipClientDetails clientDetails = new BDMTaxSlipClientDetails();

      if (!skipT4AProcessing || !skipRL1Processing) {
        // populate client details via their evidence
        clientDetails =
          manageTaxSlipsObj.populateClientDetails(concernRoleID, yearEndDate);
      }

      // search for eligible tax slip amounts
      final BDMTaxSlipAmountsByClientKey searchTaxAmountsKey =
        new BDMTaxSlipAmountsByClientKey();
      searchTaxAmountsKey.concernRoleID = concernRoleID;
      searchTaxAmountsKey.addToTaxSlipInd = true;
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

      final BDMTaxSlipAmountsByClientDetailsList allTaxSlipAmounts =
        BDMPaymentInstrumentDAFactory.newInstance()
          .searchTaxSlipAmountsByClient(searchTaxAmountsKey);

      final List<BDMTaxSlipAmountsByClientDetails> taxSlipAmounts =
        new ArrayList<>();

      final BDMPaymentInstrument bdmPIObj =
        BDMPaymentInstrumentFactory.newInstance();
      final BDMPaymentInstrumentKey bdmPaymentInstrumentKey =
        new BDMPaymentInstrumentKey();
      // iterate through all the tax slip amounts
      for (final BDMTaxSlipAmountsByClientDetails taxSlipDtls : allTaxSlipAmounts.dtls) {
        // if they are between the year start and end date, check that they have
        // a valid status
        if (taxSlipDtls.paymentDueDate.after(yearStartDate.addDays(-1))
          && taxSlipDtls.paymentDueDate.before(yearEndDate.addDays(1))) {
          if (taxSlipDtls.reconcilStatusCode
            .equals(BDMPMTRECONCILIATIONSTATUS.NIL)
            || taxSlipDtls.reconcilStatusCode
              .equals(BDMPMTRECONCILIATIONSTATUS.UNDERTHRESHOLD)
            || taxSlipDtls.reconcilStatusCode
              .equals(BDMPMTRECONCILIATIONSTATUS.ISSUED)
            || taxSlipDtls.reconcilStatusCode
              .equals(BDMPMTRECONCILIATIONSTATUS.CANCELED)) {
            taxSlipAmounts.add(taxSlipDtls);
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
            }
          }
        } else {
          if (taxSlipDtls.reconcilStatusCode
            .equals(BDMPMTRECONCILIATIONSTATUS.CANCELED)) {
            // Not being handled
          }
        }

      }
      // TODO: implement error handling if no SIN or name or address found

      if (!skipT4AProcessing) {

        double otherIncome = 0;
        double incomeTaxDeducted = 0;

        // sum up the income and deduction amounts
        for (final BDMTaxSlipAmountsByClientDetails taxAmount : taxSlipAmounts) {
          if (taxAmount.reconcilStatusCode
            .equals(BDMPMTRECONCILIATIONSTATUS.CANCELED)) {
            continue;
          }
          otherIncome =
            otherIncome + taxAmount.taxablePaymentAmount.getValue()
              - taxAmount.preTaxDeductionAmount.getValue();
          incomeTaxDeducted =
            incomeTaxDeducted + taxAmount.fedTaxAmount.getValue()
              + taxAmount.fedVTWTaxAmount.getValue();
        }

        if (otherIncome > 0) {
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
          t4aDtls.slipTypeCode = BDMTAXSLIPTYPE.ORIGINAL;
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
          t4aDtls.reportTypeCode = "O";
          t4aDtls.creationDateTime = DateTime.getCurrentDateTime();
          t4aDtls.statusIndianInd = clientDetails.statusIndianInd;

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

          BDMTaxSlipDataT4AFactory.newInstance().insert(t4aDtls);

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
          for (final BDMTaxSlipAmountsByClientDetails taxAmount : taxSlipAmounts) {

            // add a link between a tax slip and payment instrument
            paymentLinkDtls.taxSlipDataPaymentLinkID =
              UniqueID.nextUniqueID();
            paymentLinkDtls.taxSlipDataID = t4aDtls.taxSlipDataID;
            paymentLinkDtls.pmtInstrumentID = taxAmount.pmtInstrumentID;
            taxSlipPaymentLinkObj.insert(paymentLinkDtls);

          }

          t4aGenerated = true;
        }

      }

      if (!skipRL1Processing) {

        double otherIncome = 0;
        double incomeTaxDeducted = 0;
        // sum up the income and deduction amounts
        for (final BDMTaxSlipAmountsByClientDetails taxAmount : taxSlipAmounts) {
          if (taxAmount.reconcilStatusCode
            .equals(BDMPMTRECONCILIATIONSTATUS.CANCELED)) {
            continue;
          }
          otherIncome =
            otherIncome + taxAmount.taxablePaymentAmount.getValue()
              - taxAmount.preTaxDeductionAmount.getValue();
          incomeTaxDeducted =
            incomeTaxDeducted + taxAmount.provTaxAmount.getValue()
              + taxAmount.provVTWTaxAmount.getValue();

        }

        if (otherIncome > 0
          && (incomeTaxDeducted > 0 || clientDetails.quebecAddressInd)) {
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
          rl1Dtls.slipTypeCode = BDMTAXSLIPTYPE.ORIGINAL;
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
          rl1Dtls.reportTypeCode = "R";
          rl1Dtls.incomeTaxDeducted = new Money(incomeTaxDeducted);
          rl1Dtls.creationDateTime = DateTime.getCurrentDateTime();
          rl1Dtls.statusIndianInd = clientDetails.statusIndianInd;

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

          BDMTaxSlipDataRL1Factory.newInstance().insert(rl1Dtls);

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
          for (final BDMTaxSlipAmountsByClientDetails taxAmount : taxSlipAmounts) {

            // add a link between a tax slip and payment instrument
            paymentLinkDtls.taxSlipDataPaymentLinkID =
              UniqueID.nextUniqueID();
            paymentLinkDtls.taxSlipDataID = rl1Dtls.taxSlipDataID;
            paymentLinkDtls.pmtInstrumentID = taxAmount.pmtInstrumentID;
            taxSlipPaymentLinkObj.insert(paymentLinkDtls);

          }

          rl1Generated = true;

        }

      }

      // if we didn't skip processing or a manual slip was issued, we want to
      // update the payment instrument
      if (!skipT4AProcessing || !skipRL1Processing || manualSlipIssued) {
        for (final BDMTaxSlipAmountsByClientDetails taxAmountDtls : taxSlipAmounts) {
          // set taxSlipGenInd to true for each BDMPaymentInstrument
          // record with a tax slip generated
          bdmPaymentInstrumentKey.pmtInstrumentID =
            taxAmountDtls.pmtInstrumentID;
          final BDMPaymentInstrumentDtls bdmPIDtls =
            bdmPIObj.read(bdmPaymentInstrumentKey);
          if (t4aGenerated || rl1Generated || manualSlipIssued) {
            bdmPIDtls.taxSlipGenInd = true;
          }
          bdmPIDtls.addToTaxSlipInd = false;
          bdmPIObj.modify(bdmPaymentInstrumentKey, bdmPIDtls);
        }
      }

      totalProcessed++;

    } catch (

    final Exception e) {

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
