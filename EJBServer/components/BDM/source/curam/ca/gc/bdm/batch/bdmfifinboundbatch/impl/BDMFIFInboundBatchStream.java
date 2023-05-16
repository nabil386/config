package curam.ca.gc.bdm.batch.bdmfifinboundbatch.impl;

import curam.ca.gc.bdm.batch.bdmfifinboundbatch.struct.BDMFIFBatchParameters;
import curam.ca.gc.bdm.entity.fact.BDMBankBranchFactory;
import curam.ca.gc.bdm.entity.fact.BDMFIFStageInboundFactory;
import curam.ca.gc.bdm.entity.intf.BDMBankBranch;
import curam.ca.gc.bdm.entity.intf.BDMFIFStageInbound;
import curam.ca.gc.bdm.entity.struct.BDMBankBranchDtls;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundDtls;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundDtlsStruct3;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundKey;
import curam.ca.gc.bdm.entity.struct.BDMFIFStageInboundKeyStruct2;
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
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class BDMFIFInboundBatchStream extends
  curam.ca.gc.bdm.batch.bdmfifinboundbatch.base.BDMFIFInboundBatchStream {

  /** Max length of a bank or branch name. */
  private static final int kBankNamesLength = 35;

  /** A status value of inactive on FIF. */
  private static final String kFIFStatusInactiveC = "C";

  /** A status value of inactive on FIF. */
  private static final String kFIFStatusInactiveX = "X";

  /** Default postcode, po address */
  private static final String kPostCode = "L7G 3S8";

  private static final String kPOAddress = "PO 0000";

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
      result.errorMessage = "Error '" + batchProcessingID + "'.";
      return result;
    }

    final BDMFIFStageInbound inboundObj =
      BDMFIFStageInboundFactory.newInstance();
    final BDMFIFStageInboundKey stageInboundKey = new BDMFIFStageInboundKey();
    BDMFIFStageInboundDtls inboundDtls = new BDMFIFStageInboundDtls();
    stageInboundKey.recordID = batchProcessingID.recordID;
    inboundDtls = inboundObj.read(stageInboundKey);

    final String bankStatus = inboundDtls.branchStatus;
    final boolean isActiveBranchInFIF = isActive(bankStatus);

    final String financialInstNo = inboundDtls.financialInstNo;
    final String bankBranchNo = inboundDtls.bankBranchNo;
    final String branchName = trimBankName(constructBranchName(inboundDtls));
    final String BIC = inboundDtls.bankSortCode;
    // final String BIC = financialInstNo + bankBranchNo;
    // start address
    final curam.core.intf.UniqueID uniqueID = UniqueIDFactory.newInstance();
    final AddressData addressDataObj = AddressDataFactory.newInstance();
    final LayoutKey layoutKey = addressDataObj.getLayoutForLocale(null);
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = "BDMFIFIN";
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

    // Assigning some default address if branchCivicAddress,postalCode and
    // branchPOAddress is null

    if (addressFieldDetails.postalCode.isEmpty()) {
      addressFieldDetails.postalCode = kPostCode;
    } else {
      addressFieldDetails.postalCode = inboundDtls.branchPOCode;
    }

    if (inboundDtls.branchCivicAddress.isEmpty()) {
      addressFieldDetails.addressLine1 = inboundDtls.branchPOAddress;
      if (addressFieldDetails.addressLine1.isEmpty()) {
        addressFieldDetails.addressLine1 = kPOAddress;
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

    // end address

    final BankBranchDtls dtlsNew = new BankBranchDtls();
    dtlsNew.name = branchName;
    dtlsNew.upperName = dtlsNew.name;
    dtlsNew.bic = BIC;
    dtlsNew.upperBic = dtlsNew.bic;
    dtlsNew.bankSortCode = BIC; // NB different banks can have same
    // transitCode.
    dtlsNew.upperBankSortCode = dtlsNew.bankSortCode;
    dtlsNew.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    dtlsNew.bankBranchStatus = curam.codetable.BANKBRANCHSTATUS.DEFAULTCODE;

    dtlsNew.startDate = Date.getCurrentDate();
    // BEGIN, issues/846, BD
    // dtlsNew.addressID = defaultBankBranchAddressID; // point to dummy
    // address.
    dtlsNew.addressID = addressDtls.addressID;
    // END, issues/846

    // Create or update the BankBranch record.

    final long bankBranchID = getBankBranchIDFromBIC(BIC);
    final BankBranch bankBranchObj = BankBranchFactory.newInstance();
    if (bankBranchID != 0) {
      // We recognise this branch. This is a modify or deactivate.
      final BankBranchKey bankBranchKey = new BankBranchKey();
      bankBranchKey.bankBranchID = bankBranchID;
      if (isActiveBranchInFIF) {
        /// if
        final BankBranchDtls dtlsExisting = bankBranchObj.read(bankBranchKey);
        // Update only the fields we control; everything else stays as it was.
        dtlsExisting.name = dtlsNew.name;
        dtlsExisting.upperName = dtlsNew.upperName;
        dtlsExisting.bic = BIC;
        dtlsExisting.upperBic = dtlsNew.upperBic;
        dtlsExisting.bankSortCode = dtlsNew.bankSortCode;
        dtlsExisting.upperBankSortCode = dtlsNew.upperBankSortCode;
        bankBranchObj.modify(bankBranchKey, dtlsExisting);
        bankBranchesModifiedCount.add(dtlsExisting.bankSortCode);
      } else {
        // the FIF says it is inactive. Do we also need to deactivate it now?
        final AdminBankBranch adminBankBranchObj =
          AdminBankBranchFactory.newInstance();
        final BankBranchKeyStruct bankBranchKeyStruct =
          new BankBranchKeyStruct();
        bankBranchKeyStruct.bankBranchID = bankBranchID;
        final BankBranchStruct bankBranchStruct =
          adminBankBranchObj.readBankBranch(bankBranchKeyStruct);
        if (bankBranchStruct.statusCode
          .equals(curam.codetable.RECORDSTATUS.CANCELLED)
          || bankBranchStruct.bankBranchStatus
            .equals(curam.codetable.BANKBRANCHSTATUS.CLOSED)) {
          // already cancelled/closed. Nothing more to do.

        } else {
          // Not cancelled yet. Cancel it now.
          Trace.kTopLevelLogger.info("Closing branch " + BIC);
          bankBranchStruct.bankBranchStatus =
            curam.codetable.BANKBRANCHSTATUS.CLOSED;
          bankBranchStruct.endDate = Date.getCurrentDate();
          bankBranchStruct.statusCode = RECORDSTATUS.CANCELLED;
          try {
            adminBankBranchObj.modifyBankBranch(bankBranchKeyStruct,
              bankBranchStruct);
            bankBranchesDeactivatedCount
              .add((int) batchProcessingID.recordID);
          } catch (final AppException ae) {

            result = new BatchProcessingSkippedRecord();
            result.recordID = batchProcessingID.recordID;
            result.errorMessage = ae.getLocalizedMessage() + " "
              + batchProcessingID.recordID + "'.";

            // Should we also populate the stack trace field? No, because we
            // want this message to fit on a single line.
            bankBranchesDeactivateFailureCount
              .add((int) inboundDtls.recordID);
          }

        } // ... else, was not cancelled.
      } // ..else is inactive in FIF

    } else {
      if (isActiveBranchInFIF) {
        // We do not recognise this active bank branch. So this is an insert.
        final curam.core.intf.UniqueID uniqueIDObj =
          UniqueIDFactory.newInstance();
        final long bankID1 = getBankIDFromBank(BIC);
        dtlsNew.bankBranchID = uniqueIDObj.getNextID();
        dtlsNew.bankID = bankID1;

        bankBranchObj.insert(dtlsNew);

        insertBDMBankBranch(dtlsNew);

        bankBranchesInsertedCount.add(dtlsNew.bankSortCode);

      } else {
        // Don't insert an inactive branch, just ignore it.
        bankBranchesInactiveCount.add((int) inboundDtls.recordID);
      }
    }
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
    fifInboundDtls = getBankBranchNameFromFiF(dtlsNew.bic);
    bdmBankBranchDtls.name = fifInboundDtls.branchNameEN;
    bdmBankBranchDtls.localeCode = LOCALE.ENGLISH;
    bdmBankBranchObj.insert(bdmBankBranchDtls);

    // Insert French version of bank branch name
    bdmBankBranchDtls.recordID = uniqueIDObj.getNextID();
    bdmBankBranchDtls.bankBranchID = dtlsNew.bankBranchID;
    bdmBankBranchDtls.bankID = dtlsNew.bankID;
    bdmBankBranchDtls.bic = bankBranchDtls.bic;
    bdmBankBranchDtls.name = fifInboundDtls.branchNameFR;
    bdmBankBranchDtls.localeCode = LOCALE.FRENCH;
    bdmBankBranchObj.insert(bdmBankBranchDtls);

  }

  /**
   * Indicates whether the status code denotes an inactive bank branch.
   *
   * @param statusCode The status character from the FIF.
   * @return True if the status code indicates an active branch.
   */
  private boolean isActive(final String statusCode) {

    final boolean isNotActive = kFIFStatusInactiveC.equals(statusCode)
      || kFIFStatusInactiveX.equals(statusCode);
    return !isNotActive;
  }

  /**
   * Truncates the name of a bank or branch.
   *
   * @param bankName The name.
   * @return The name truncated.
   */
  private String trimBankName(final String bankName) {

    final String result;
    if (bankName.length() < kBankNamesLength) {
      result = bankName;
    } else {
      result = bankName.substring(0, kBankNamesLength);
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

  /**
   * Gets the primary key ID for a bank branch using its institution and
   * branch code which are stored in the BIC field. We simply search for
   * matching records and return the first one with a status of NORMAL
   * ie active.
   *
   * @param bIC the value in the BIC field.
   *
   * @return The bank branch ID value, or zero if not found.
   */
  private long getBankBranchIDFromBIC(final String bICvalue)
    throws AppException, InformationalException {

    final BankBranch bpo = BankBranchFactory.newInstance();
    final SwiftBusinessIdentifierCode bicHolder =
      new SwiftBusinessIdentifierCode();
    bicHolder.bic = bICvalue;
    final BankBranchDtlsList branches = bpo.searchByBic(bicHolder);

    long result = 0;
    for (int i = 0; i < branches.dtls.size(); i++) {
      final BankBranchDtls currentBranch = branches.dtls.get(i);
      if (currentBranch.statusCode
        .equals(curam.codetable.RECORDSTATUS.NORMAL)) {
        result = currentBranch.bankBranchID;
        break;
      }
    }
    return result;
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

  private BDMFIFStageInboundDtlsStruct3 getBankBranchNameFromFiF(
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
  /*
   * public void displayExecutionSummary() {
   *
   * Trace.kTopLevelLogger
   * .info("Processed record:" + processedInstrumentsCount);
   * Trace.kTopLevelLogger.info("Skipped record: " + skippedItemsCount);
   * Trace.kTopLevelLogger
   * .info("Branches inserted:" + bankBranchesInsertedCount.size());
   * Trace.kTopLevelLogger
   * .info("Branches updated: " + bankBranchesModifiedCount.size());
   * Trace.kTopLevelLogger.info("Inactive branches skipped in FIF:      "
   * + bankBranchesInactiveCount.size());
   * Trace.kTopLevelLogger.info("Branches cancelled successfully:       "
   * + bankBranchesDeactivatedCount.size());
   * Trace.kTopLevelLogger.info("Branches which could not be cancelled: "
   * + bankBranchesDeactivateFailureCount.size());
   * }
   */

}
