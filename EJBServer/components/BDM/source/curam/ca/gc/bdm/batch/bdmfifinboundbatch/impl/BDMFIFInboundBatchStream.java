package curam.ca.gc.bdm.batch.bdmfifinboundbatch.impl;

import curam.ca.gc.bdm.batch.bdmfifinboundbatch.struct.BDMFIFBatchParameters;
import curam.ca.gc.bdm.entity.fact.BDMBankBranchFactory;
import curam.ca.gc.bdm.entity.fact.BDMFIFStageInboundFactory;
import curam.ca.gc.bdm.entity.intf.BDMBankBranch;
import curam.ca.gc.bdm.entity.intf.BDMFIFStageInbound;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchDtls;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchKey;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchSearchBySortCodeKey;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchSearchResultBySortCode;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchSearchResultBySortCodeList;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundDtls;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundDtlsStruct3;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundKey;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundKeyStruct2;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.BANKBRANCHSTATUS;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.COUNTRY;
import curam.codetable.LOCALE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.AdminBankBranchFactory;
import curam.core.fact.BankBranchFactory;
import curam.core.fact.BankFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.intf.Address;
import curam.core.intf.AddressData;
import curam.core.intf.AdminBankBranch;
import curam.core.intf.Bank;
import curam.core.intf.BankBranch;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.BankBranchDtls;
import curam.core.struct.BankBranchDtlsList;
import curam.core.struct.BankBranchKey;
import curam.core.struct.BankBranchKeyStruct;
import curam.core.struct.BankBranchStruct;
import curam.core.struct.BankDtls;
import curam.core.struct.BankNameStructRef;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.LayoutKey;
import curam.core.struct.SwiftBusinessIdentifierCode;
import curam.core.struct.UniqueIDKeySet;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;
import curam.util.type.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class BDMFIFInboundBatchStream extends
  curam.ca.gc.bdm.batch.bdmfifinboundbatch.base.BDMFIFInboundBatchStream {

  /** Default start date for newly inserted records. */
  public final Date defaultStartDate = Date.fromISO8601("20100101");

  /** An item count. */
  private final Set<String> bankBranchesInsertedCount = new HashSet<String>();

  /** An item count. */
  private final Set<String> bankBranchesModifiedCount = new HashSet<String>();

  /** An item count. */
  private final Set<Integer> bankBranchesInactiveCount =
    new HashSet<Integer>();

  /** An item count. */
  private final Set<Integer> bankBranchesDeactivatedCount =
    new HashSet<Integer>();

  /** An item count. */
  private final Set<Integer> bankBranchesDeactivateFailureCount =
    new HashSet<Integer>();

  /** An item count. */
  private int skippedItemsCount = 0;

  /** Bank Branch Verification struct. */
  private class BankBranchVerification {

    boolean bankBranchExists;

    boolean bankBranchActive;

    boolean bankBranchClosed;

    long bankBranchID;
  }

  /** The parameters which launched the program. */
  final BDMFIFBatchParameters batchParameters;

  public int processedInstrumentsCount = 0;

  /**
   * Constructor which keeps a reference to the stream which created us.
   *
   * @param streamObj The stream who created us.
   * @param params The parameters which were passed into the batch.
   */

  public BDMFIFInboundBatchStream() {

    this.batchParameters = new BDMFIFBatchParameters();

    // super();
  }

  BDMFIFInboundBatchStreamWrapper streamWrapper;

  /**
   * This is the entry point for running the stream standalone.
   */
  @Override
  public void process(final BatchProcessStreamKey streamKey)
    throws AppException, InformationalException {

    streamWrapper =
      new BDMFIFInboundBatchStreamWrapper(this, batchParameters);
    final BatchStreamHelper batchStreamHelper = new BatchStreamHelper();

    if (StringUtils.isEmpty(streamKey.instanceID)) {
      streamKey.instanceID = BATCHPROCESSNAME.BDM_FIF_INBOUND;
    }
    streamWrapper.setInstanceID(streamKey.instanceID);

    batchStreamHelper.runStream(streamKey, streamWrapper);

  }

  /**
   * Gets called by the stream wrapper, but only needed if the BPO needs
   * to be called from online app.
   */
  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    BatchProcessingSkippedRecord result = null;

    if (batchProcessingID == null) {
      result = new BatchProcessingSkippedRecord();
      result.errorMessage =
        "Error, null batchProcessingID '" + batchProcessingID + "'.";
      return result;
    }

    final BDMFIFStageInbound inboundObj =
      BDMFIFStageInboundFactory.newInstance();
    final BDMFIFStageInboundKey stageInboundKey = new BDMFIFStageInboundKey();
    BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    stageInboundKey.recordID = batchProcessingID.recordID;
    inboundDtls = inboundObj.read(stageInboundKey);

    final String financialInstNo = inboundDtls.financialInstNo;
    final String bankBranchNo = inboundDtls.bankBranchNo;
    final String branchName = trimBankName(constructBranchName(inboundDtls));
    final String BIC = inboundDtls.bankSortCode;

    // start address section //

    final curam.core.intf.UniqueID uniqueID = UniqueIDFactory.newInstance();
    final AddressData addressDataObj = AddressDataFactory.newInstance();
    final LayoutKey layoutKey = addressDataObj.getLayoutForLocale(null);
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = BDMConstants.kBDMFIFinboundKeySetName;
    final Address address = AddressFactory.newInstance();
    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressID = uniqueID.getNextIDFromKeySet(uniqueIDKeySet);

    if (inboundDtls.branchForeignCountry.isEmpty()) {
      addressDtls.countryCode = COUNTRY.DEFAULTCODE;
    } else {
      addressDtls.countryCode = inboundDtls.branchForeignCountry;
    }

    addressDtls.addressLayoutType = layoutKey.addressLayoutType;
    addressDtls.modifiableInd = true;

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType = layoutKey.addressLayoutType;

    // Assigning some default address if postalCode, branchCivicAddress, or
    // branchPOAddress are null

    if (inboundDtls.branchPOCode.isEmpty()) {
      addressFieldDetails.postalCode = BDMConstants.kPostCode;
    } else {
      addressFieldDetails.postalCode = inboundDtls.branchPOCode;
    }

    if (inboundDtls.branchCivicAddress.isEmpty()) {
      addressFieldDetails.addressLine1 = inboundDtls.branchPOAddress;

      if (addressFieldDetails.addressLine1.isEmpty()) {
        addressFieldDetails.addressLine1 = BDMConstants.kPOAddress;
      }

    } else {
      addressFieldDetails.addressLine1 = inboundDtls.branchCivicAddress;
    }

    // addressFieldDetails.addressLine2 = "";
    addressFieldDetails.city = inboundDtls.branchCity;
    addressFieldDetails.province = inboundDtls.branchProvinceCode;
    // addressFieldDetails.stateCode = "A";
    addressFieldDetails.stateProvince = inboundDtls.branchProvinceCode;
    addressFieldDetails.countryCode = addressDtls.countryCode;

    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    address.insert(addressDtls);

    // end address section //

    final BankBranchDtls dtlsNew = new BankBranchDtls();
    final Locale defaultLocale = new Locale(LOCALE.DEFAULTCODE);
    dtlsNew.name = branchName;
    dtlsNew.upperName = branchName.toUpperCase(defaultLocale);
    dtlsNew.bic = BIC;
    dtlsNew.upperBic = BIC.toUpperCase(defaultLocale);
    dtlsNew.bankSortCode = BIC; // different banks can have same transitCode.
    dtlsNew.upperBankSortCode = BIC.toUpperCase(defaultLocale);
    dtlsNew.statusCode = RECORDSTATUS.NORMAL;
    dtlsNew.bankBranchStatus = BANKBRANCHSTATUS.DEFAULTCODE;
    dtlsNew.startDate = Date.getCurrentDate();
    dtlsNew.addressID = addressDtls.addressID;

    // BEGIN, BUG-63417: FIF inbound processRecord() code refactoring //

    // determines if bankBranch (in)active in FIF inbound
    final String fifBranchStatus = inboundDtls.branchStatus;

    final boolean isActiveBranchInFIF =
      fifBranchStatus.equals(BDMConstants.kFIFbranchStatusActiveV);
    final boolean isClosedBranchInFIF =
      fifBranchStatus.equals(BDMConstants.kFIFbranchStatusClosedC)
        || fifBranchStatus.equals(BDMConstants.kFIFbranchStatusClosedX);

    // determines if bankBranch already exists in Curam
    final BankBranchVerification bbv = getBankBranchIDFromBIC(BIC);

    if (bbv == null) {
      result = new BatchProcessingSkippedRecord();
      result.recordID = batchProcessingID.recordID;
      result.errorMessage =
        "Error, invalid statusCode or bankBranchStatus in BANKBRANCH table, batchProcessingID = '"
          + batchProcessingID + "'.";
      return result;
    }

    // Check if any institution Number or Banch Number is active in FIF staging
    // table
    if (isActiveBranchInFIF) {

      // Check if Bank Branch already exists in Curam
      if (bbv.bankBranchExists) {

        if (bbv.bankBranchClosed) { // update status to open and remove cross reference if not provided in FIF
          final AdminBankBranch adminBankBranchObj =
            AdminBankBranchFactory.newInstance();
          final BankBranchKeyStruct bankBranchKeyStruct =
            new BankBranchKeyStruct();
          bankBranchKeyStruct.bankBranchID = bbv.bankBranchID;
          final BankBranchStruct bankBranchStruct =
            adminBankBranchObj.readBankBranch(bankBranchKeyStruct);

          bankBranchStruct.bankBranchStatus = BANKBRANCHSTATUS.DEFAULTCODE;
          bankBranchStruct.endDate = new Date();
          bankBranchStruct.startDate = Date.getCurrentDate();
          bankBranchStruct.statusCode = RECORDSTATUS.NORMAL;
          adminBankBranchObj.modifyBankBranch(bankBranchKeyStruct,
            bankBranchStruct);
        }

        final BankBranchKey bankBranchKey = new BankBranchKey();
        bankBranchKey.bankBranchID = bbv.bankBranchID;
        final BankBranch bankBranchObj = BankBranchFactory.newInstance();
        final BankBranchDtls dtlsExisting = bankBranchObj.read(bankBranchKey);
        // Update only the fields we control, everything else stays as it was.

        // Check if Cross reference numbers are provided
        final Boolean fifCrossReferenceInfoProvided =
          checkPSPCFIFCrossRefNumbers(inboundDtls.xRefCheckDigit,
            inboundDtls.xRefFinancialInstNo, inboundDtls.xRefFINInstBranchNo);

        if (checkForChanges(dtlsExisting, dtlsNew, BIC)
          || fifCrossReferenceInfoProvided) {
          // only modify if there's something to modify
          dtlsExisting.name = dtlsNew.name;
          dtlsExisting.upperName = dtlsNew.upperName;
          dtlsExisting.bic = BIC;
          dtlsExisting.upperBic = dtlsNew.upperBic;
          dtlsExisting.bankSortCode = dtlsNew.bankSortCode;
          dtlsExisting.upperBankSortCode = dtlsNew.upperBankSortCode;

          bankBranchObj.modify(bankBranchKey, dtlsExisting);
          bankBranchesModifiedCount.add(dtlsExisting.bankSortCode);
          updateBDMBankBranch(BIC);
        }

      } else { // bankBranch doesn't exist in Curam

        // We do not recognize this active bank branch, so it's an insert.
        final curam.core.intf.UniqueID uniqueIDObj =
          UniqueIDFactory.newInstance();
        final long bankID = getBankIDFromBank(BIC);
        dtlsNew.bankBranchID = uniqueIDObj.getNextID();
        dtlsNew.bankID = bankID;

        final BankBranch bankBranchObj = BankBranchFactory.newInstance();
        bankBranchObj.insert(dtlsNew);
        insertBDMBankBranch(dtlsNew);
        bankBranchesInsertedCount.add(dtlsNew.bankSortCode);

      }

    } else if (isClosedBranchInFIF) {
      if (bbv.bankBranchActive) { // Not cancelled. Cancel it now.
        final AdminBankBranch adminBankBranchObj =
          AdminBankBranchFactory.newInstance();
        final BankBranchKeyStruct bankBranchKeyStruct =
          new BankBranchKeyStruct();
        bankBranchKeyStruct.bankBranchID = bbv.bankBranchID;
        final BankBranchStruct bankBranchStruct =
          adminBankBranchObj.readBankBranch(bankBranchKeyStruct);

        Trace.kTopLevelLogger.info("Closing branch " + BIC);
        bankBranchStruct.endDate = Date.getCurrentDate();
        bankBranchStruct.statusCode = RECORDSTATUS.CANCELLED;
        bankBranchStruct.bankBranchStatus = BANKBRANCHSTATUS.CLOSED;

        try {
          adminBankBranchObj.modifyBankBranch(bankBranchKeyStruct,
            bankBranchStruct);
          bankBranchesDeactivatedCount.add((int) batchProcessingID.recordID);

        } catch (final AppException ae) {
          result = new BatchProcessingSkippedRecord();
          result.recordID = batchProcessingID.recordID;
          result.errorMessage = ae.getLocalizedMessage()
            + ", batchProcessingID = '" + batchProcessingID.recordID + "'.";

          // Should we also populate the stack trace field? No, we want this
          // message to fit on a single line.
          bankBranchesDeactivateFailureCount.add((int) inboundDtls.recordID);
        }

      } else { // Don't insert an inactive branch, just ignore it.
        bankBranchesInactiveCount.add((int) inboundDtls.recordID);

        updateBDMBankBranch(BIC);
      }

    } else {

      final String errorMessage =
        "Invalid branchStatus in BDMFIFSTAGEINBOUND table, branchStatus = "
          + fifBranchStatus + ", recordID = " + inboundDtls.recordID + ".";

      Trace.kTopLevelLogger.info(errorMessage);

      result = new BatchProcessingSkippedRecord();
      result.recordID = batchProcessingID.recordID;
      result.errorMessage = errorMessage;
    }

    // END, BUG-63417: FIF inbound processRecord() code refactoring //

    processedInstrumentsCount++;
    return result;
  }

  /*
   * Task-22009: Adding English and French version of branch name in
   * BDMBankBranch table
   */
  private void insertBDMBankBranch(final BankBranchDtls dtlsNew)
    throws AppException, InformationalException {

    final curam.core.intf.UniqueID uniqueIDObj =
      UniqueIDFactory.newInstance();

    final BankBranch bankBranchObj = BankBranchFactory.newInstance();

    final BankBranchKey branchKey = new BankBranchKey();
    branchKey.bankBranchID = dtlsNew.bankBranchID;
    BankBranchDtls bankBranchDtls = new BankBranchDtls();
    bankBranchDtls = bankBranchObj.read(branchKey);

    final BDMBankBranch bdmBankBranchObj = BDMBankBranchFactory.newInstance();
    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    BDMFIFStageInboundDtlsStruct3 fifInboundDtls =
      new BDMFIFStageInboundDtlsStruct3();

    // Insert English version of bank branch name
    bdmBankBranchDtls.recordID = uniqueIDObj.getNextID();
    bdmBankBranchDtls.bankBranchID = dtlsNew.bankBranchID;
    bdmBankBranchDtls.bankID = dtlsNew.bankID;
    bdmBankBranchDtls.bic = bankBranchDtls.bic;
    fifInboundDtls = getBankBranchNamesFromFiF(dtlsNew.bic);
    bdmBankBranchDtls.name = fifInboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;

    final Long xRefCheckDigit = fifInboundDtls.xRefCheckDigit;
    final Long xRefFinancialInstNo = fifInboundDtls.xRefFinancialInstNo;
    final Long xRefFINInstBranchNo = fifInboundDtls.xRefFINInstBranchNo;
    // Check if Cross reference numbers are provided
    final Boolean fifCrossReferenceInfoProvided = checkPSPCFIFCrossRefNumbers(
      xRefCheckDigit, xRefFinancialInstNo, xRefFINInstBranchNo);

    // if crossreference info is provided then update
    bdmBankBranchDtls.xRefCheckDigit =
      fifCrossReferenceInfoProvided ? fifInboundDtls.xRefCheckDigit : 0L;
    bdmBankBranchDtls.xRefFinancialInstNo =
      fifCrossReferenceInfoProvided ? fifInboundDtls.xRefFinancialInstNo : 0L;
    bdmBankBranchDtls.xRefFINInstBranchNo =
      fifCrossReferenceInfoProvided ? fifInboundDtls.xRefFINInstBranchNo : 0L;

    bdmBankBranchObj.insert(bdmBankBranchDtls);

    // Insert French version of bank branch name
    bdmBankBranchDtls.recordID = uniqueIDObj.getNextID();
    bdmBankBranchDtls.name = fifInboundDtls.branchNameFR;
    bdmBankBranchDtls.localeCode = LOCALE.FRENCH;
    // remaining bankbranch deatils are same as for english
    bdmBankBranchObj.insert(bdmBankBranchDtls);

  }

  private void updateBDMBankBranch(final String bic)
    throws AppException, InformationalException {

    final BDMBankBranch bdmBankBranchObj = BDMBankBranchFactory.newInstance();
    BDMFIFStageInboundDtlsStruct3 fifInboundDtls =
      new BDMFIFStageInboundDtlsStruct3();
    final BDMBankBranchDtls bdmBankBranchDtls = new BDMBankBranchDtls();
    final BDMBankBranchSearchBySortCodeKey key =
      new BDMBankBranchSearchBySortCodeKey();
    key.bic = bic;
    final BDMBankBranchSearchResultBySortCodeList bdmBankBranchList =
      bdmBankBranchObj.searchBySortCode(key);

    fifInboundDtls = getBankBranchNamesFromFiF(bic);
    final Long xRefCheckDigit = fifInboundDtls.xRefCheckDigit;
    final Long xRefFinancialInstNo = fifInboundDtls.xRefFinancialInstNo;
    final Long xRefFINInstBranchNo = fifInboundDtls.xRefFINInstBranchNo;
    // Check if Cross reference numbers are provided
    final Boolean fifCrossReferenceInfoProvided = checkPSPCFIFCrossRefNumbers(
      xRefCheckDigit, xRefFinancialInstNo, xRefFINInstBranchNo);

    for (final BDMBankBranchSearchResultBySortCode bdmBankBranch : bdmBankBranchList.dtls) {
      bdmBankBranchDtls.recordID = bdmBankBranch.recordID;
      bdmBankBranchDtls.bankBranchID = bdmBankBranch.bankBranchID;
      bdmBankBranchDtls.bic = bdmBankBranch.bic;
      bdmBankBranchDtls.bankID = bdmBankBranch.bankID;
      bdmBankBranchDtls.localeCode = bdmBankBranch.localeCode;

      // if crossreference info is provided then update

      bdmBankBranchDtls.xRefCheckDigit =
        fifCrossReferenceInfoProvided ? xRefCheckDigit : 0L;
      bdmBankBranchDtls.xRefFinancialInstNo =
        fifCrossReferenceInfoProvided ? xRefFinancialInstNo : 0L;
      bdmBankBranchDtls.xRefFINInstBranchNo =
        fifCrossReferenceInfoProvided ? xRefFINInstBranchNo : 0L;

      // update bdmBank branch with crossreference numbers
      if (bdmBankBranch.localeCode.equals(LOCALE.ENGLISH)) {
        bdmBankBranchDtls.name = fifInboundDtls.branchNameEN;
      }

      else if (bdmBankBranch.localeCode.equals(LOCALE.FRENCH)) {
        bdmBankBranchDtls.name = fifInboundDtls.branchNameFR;
      }

      final BDMBankBranchKey recordID = new BDMBankBranchKey();
      recordID.recordID = bdmBankBranch.recordID;
      bdmBankBranchObj.modify(recordID, bdmBankBranchDtls);
    }
  }

  /**
   * Truncates the name of a bank or branch.
   *
   * @param bankName The name.
   * @return The name truncated.
   */
  private String trimBankName(final String bankName) {

    final String result;
    if (bankName.length() < BDMConstants.kBankNamesLength) {
      result = bankName;
    } else {
      result = bankName.substring(0, BDMConstants.kBankNamesLength);
    }

    return result;
  }

  /**
   * Constructs a name for the branch using as much information as can be
   * obtained from the FIF. If the branch name is blank then we return part
   * of the address.
   *
   * @param parser The parser for the line from the text file.
   * @return The name of the branch, or part of its address if no name is
   * present.
   */

  private String
    constructBranchName(final BDMFIFStageInboundDtls inboundDtls) {

    String result = null;
    final String branchName = inboundDtls.branchNameEN;
    if (branchName.length() > 0) {
      // We have a name; return it.
      result =
        inboundDtls.branchNameEN + "(" + inboundDtls.bankBranchNo + ")";
    } else {
      // No name supplied, fallback to giving part of the address.
      result = inboundDtls.branchCivicAddress + "" + inboundDtls.branchPOCode;
      // inboundDtls.
    }
    return result;
  }

  /*
   * BUG-63417: getBankBranchIDFromBIC(), rewritten, searches for bank branches
   * by BIC, returns bankBranchVerification object with bankbranchID and status
   * of the first corresponding bank branch record found. Returns null object if
   * a record exists but has invalid status.
   */
  private BankBranchVerification getBankBranchIDFromBIC(final String bicValue)
    throws AppException, InformationalException {

    final BankBranch bpo = BankBranchFactory.newInstance();
    final SwiftBusinessIdentifierCode bicHolder =
      new SwiftBusinessIdentifierCode();
    bicHolder.bic = bicValue;
    final BankBranchDtlsList branches = bpo.searchByBic(bicHolder);

    if (branches.dtls.size() == 0) {
      return new BankBranchVerification();
    }

    for (final BankBranchDtls currentBranch : branches.dtls) {
      if (currentBranch.statusCode.equals(curam.codetable.RECORDSTATUS.NORMAL)
        && currentBranch.bankBranchStatus
          .equals(BANKBRANCHSTATUS.DEFAULTCODE)) {
        final BankBranchVerification bankBranchVerification =
          new BankBranchVerification();
        bankBranchVerification.bankBranchID = currentBranch.bankBranchID;
        bankBranchVerification.bankBranchExists = true;
        bankBranchVerification.bankBranchActive = true;
        return bankBranchVerification;
      }
    }

    for (final BankBranchDtls currentBranch : branches.dtls) {
      if (currentBranch.statusCode
        .equals(curam.codetable.RECORDSTATUS.CANCELLED)
        || currentBranch.bankBranchStatus.equals(BANKBRANCHSTATUS.CLOSED)) {
        final BankBranchVerification bankBranchVerification =
          new BankBranchVerification();
        bankBranchVerification.bankBranchID = currentBranch.bankBranchID;
        bankBranchVerification.bankBranchExists = true;
        bankBranchVerification.bankBranchClosed = true;
        return bankBranchVerification;
      }
    }

    return null; // error condition: statusCode or bankBranchStatus invalid
  }

  /*
   * Check If the CrossReference numbers are provided by PSPC.
   */
  private boolean checkPSPCFIFCrossRefNumbers(final long xRefCheckDigit,
    final long xRefFinancialInstNo, final long xRefFINInstBranchNo) {

    return xRefCheckDigit != 0L && xRefFinancialInstNo != 0L
      && xRefFINInstBranchNo != 0L;
  }

  private long getBankIDFromBank(final String bic)
    throws AppException, InformationalException {

    final BDMFIFStageInbound stageInbObj =
      BDMFIFStageInboundFactory.newInstance();

    final BDMFIFStageInboundKeyStruct2 keyStruct =
      new BDMFIFStageInboundKeyStruct2();
    BDMFIFStageInboundDtlsStruct3 dtlsStruct =
      new BDMFIFStageInboundDtlsStruct3();
    keyStruct.bankSortCode = bic;
    dtlsStruct = stageInbObj.readByBankSortCode(keyStruct);

    final Bank bank = BankFactory.newInstance();
    final BankNameStructRef ref = new BankNameStructRef();
    BankDtls bankDtls = new BankDtls();
    ref.name = dtlsStruct.branchNameEN;
    ref.statusCode = RECORDSTATUS.DEFAULTCODE;
    bankDtls = bank.readBankByName(ref);
    return bankDtls.bankID;

  }

  private BDMFIFStageInboundDtlsStruct3 getBankBranchNamesFromFiF(
    final String bic) throws AppException, InformationalException {

    final BDMFIFStageInbound stageInbObj =
      BDMFIFStageInboundFactory.newInstance();

    final BDMFIFStageInboundKeyStruct2 keyStruct =
      new BDMFIFStageInboundKeyStruct2();
    BDMFIFStageInboundDtlsStruct3 dtlsStruct =
      new BDMFIFStageInboundDtlsStruct3();
    keyStruct.bankSortCode = bic;
    dtlsStruct = stageInbObj.readByBankSortCode(keyStruct);

    return dtlsStruct;

  }

  /*
   * BUG-63417: checkForChanges() verifies that at least one difference
   * exists in inbound data with respect to the data already on record.
   */
  private boolean checkForChanges(final BankBranchDtls dtlsExisting,
    final BankBranchDtls dtlsNew, final String BIC) {

    return !dtlsExisting.name.equals(dtlsNew.name)
      || !dtlsExisting.upperName.equals(dtlsNew.upperName)
      || !dtlsExisting.bic.equals(BIC)
      || !dtlsExisting.upperBic.equals(dtlsNew.upperBic)
      || !dtlsExisting.bankSortCode.equals(dtlsNew.bankSortCode)
      || !dtlsExisting.upperBankSortCode.equals(dtlsNew.upperBankSortCode);
  }

  public void setWrapper(final BDMFIFInboundBatchStreamWrapper wrapper) {

    this.streamWrapper = wrapper;

  }

  @Override
  public String getChunkResult(int skippedCasesCount)
    throws AppException, InformationalException {

    final String chunkResult = processedInstrumentsCount
      + CuramConst.gkTabDelimiter + (skippedCasesCount + skippedCasesCount);
    processedInstrumentsCount = 0;
    skippedCasesCount = 0;
    return chunkResult;
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList skippedRecordList)
    throws AppException, InformationalException {

    skippedItemsCount += skippedRecordList.dtls.size();

    Trace.kTopLevelLogger
      .info("-- processSkippedCases " + skippedRecordList.dtls.size());
    for (int i = 0; i < skippedRecordList.dtls.size(); i++) {
      Trace.kTopLevelLogger
        .info(" - " + skippedRecordList.dtls.item(i).recordID);
    }
  }

  /**
   * Displays info about the work this stream did.
   */
  // public void displayExecutionSummary() {
  //
  // Trace.kTopLevelLogger
  // .info("Processed record:" + processedInstrumentsCount);
  // Trace.kTopLevelLogger.info("Skipped record: " + skippedItemsCount);
  // Trace.kTopLevelLogger
  // .info("Branches inserted:" + bankBranchesInsertedCount.size());
  // Trace.kTopLevelLogger
  // .info("Branches updated: " + bankBranchesModifiedCount.size());
  // Trace.kTopLevelLogger.info("Inactive branches skipped in FIF: "
  // + bankBranchesInactiveCount.size());
  // Trace.kTopLevelLogger.info("Branches cancelled successfully: "
  // + bankBranchesDeactivatedCount.size());
  // Trace.kTopLevelLogger.info("Branches which could not be cancelled: "
  // + bankBranchesDeactivateFailureCount.size());
  // }

}
