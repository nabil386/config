package curam.ca.gc.bdm.sl.interfaces.bdmtaxslipmrq.impl;

import curam.ca.gc.bdm.batch.bdmtaxslipmrqbatch.impl.mrqpojos.BDMTaxSlipMRQSequenceRecord;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPPROCSTATUS;
import curam.ca.gc.bdm.entity.fact.BDMTaxSlipMRQBatchSequenceFactory;
import curam.ca.gc.bdm.entity.fact.BDMTaxSlipMRQStagingDataFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataRL1Factory;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataRL1;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Dtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Key;
import curam.ca.gc.bdm.entity.intf.BDMTaxSlipMRQBatchSequence;
import curam.ca.gc.bdm.entity.intf.BDMTaxSlipMRQStagingData;
import curam.ca.gc.bdm.entity.struct.BDMTaxSlipMRQBatchSequenceDtls;
import curam.ca.gc.bdm.entity.struct.BDMTaxSlipMRQBatchSequenceKey;
import curam.ca.gc.bdm.entity.struct.BDMTaxSlipMRQBatchSequenceKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMTaxSlipMRQStagingDataDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMGENERATETAXSLIPBATCH;
import curam.ca.gc.bdm.sl.interfaces.bdmtaxslipmrq.intf.BDMTaxSlipMrqBatchIntf;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.impl.EnvVars;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.type.DateTime;

/**
 * @author azar.khan
 *
 */
public class BDMTaxSlipMrqBatchImpl implements BDMTaxSlipMrqBatchIntf {

  private static final long kTaxSlipMRQInitialSeedNumber = Long.parseLong(
    Configuration.getProperty(EnvVars.BDM_TAX_SLIP_MRQ_INITIAL_SEED_NUMBER));

  private static final long kTaxSlipMRQFinalSeedNumber = Long.parseLong(
    Configuration.getProperty(EnvVars.BDM_TAX_SLIP_MRQ_FINAL_SEED_NUMBER));

  private static final int kTaxSlipMRQDiviserNumber = Configuration
    .getIntProperty(EnvVars.BDM_TAX_SLIP_MRQ_DIVISOR_NUMBER).intValue();

  private static final long kTaxSlipMRQInitialLength = Configuration
    .getIntProperty(EnvVars.BDM_TAX_SLIP_MRQ_INITIAL_LENGTH).intValue();

  private static final long kTaxSlipMRQFinalLength = Configuration
    .getIntProperty(EnvVars.BDM_TAX_SLIP_MRQ_FINAL_LENGTH).intValue();

  static Long finalValue = 0l;

  static Double tempVal = 0.00;

  // static short divisor = 7, initLength = 8, finalLength = 9;

  BatchProcessingIDList recordList = new BatchProcessingIDList();

  public static long getMod7Sin(final Long seed)
    throws IllegalArgumentException {

    if (seed.toString().length() != kTaxSlipMRQInitialLength)
      throw new IllegalArgumentException(
        String.format("Seed value '%d' is not of length '%d'", seed,
          kTaxSlipMRQInitialLength));

    Trace.kTopLevelLogger.info("Converting value: " + seed);
    // First number in the assigned series (8 digits

    // Divide the eight-digit number by 7
    tempVal = (double) seed / (double) kTaxSlipMRQDiviserNumber;

    // Multiply the first two decimal places (0.42) by 7
    tempVal -= tempVal.longValue();
    // System.out.printf("%f\n", tempVal);
    Trace.kTopLevelLogger.info("%f\n", tempVal);

    // Round up to the nearest whole number
    finalValue = (long) (tempVal * 100 * kTaxSlipMRQDiviserNumber);
    // System.out.printf("%d\n", finalValue);
    Trace.kTopLevelLogger.info("%d\n", finalValue);

    // Incrementing by 1 achieves the same goal, when converted to integers
    // (integer divide by 100 to restore append value)
    ++finalValue;
    finalValue /= 100;

    final String tempValStr = "" + seed + finalValue;
    if (tempValStr.length() != kTaxSlipMRQFinalLength)
      throw new IllegalArgumentException(
        String.format("Final value '%s' is not of length '%d'", tempValStr,
          kTaxSlipMRQInitialLength));
    else
      finalValue = Long.parseLong(tempValStr);

    return finalValue;
  }

  /**
   * Populate Sequence entity with sequence numbers
   *
   * @throws AppException
   * @throws InformationalException
   */
  /*
   * public static void populateSequence()
   * throws AppException, InformationalException {
   *
   * final long[] seedValues = LongStream
   * .rangeClosed(kTaxSlipMRQInitialSeedNumber, kTaxSlipMRQFinalSeedNumber)
   * .toArray();
   *
   * for (final long seed : seedValues) {
   * final curam.ca.gc.bdm.entity.intf.BDMTaxSlipMRQBatchSequence
   * bdmTaxSlipMRQSequenceOBJ =
   * BDMTaxSlipMRQBatchSequenceFactory.newInstance();
   * final BDMTaxSlipMRQBatchSequenceDtls dtls =
   * new BDMTaxSlipMRQBatchSequenceDtls();
   * // Create Sequence records
   * try {
   * dtls.recordID = UniqueIDFactory.newInstance().getNextID().uniqueID;
   * System.out.println(dtls.recordID);
   * } catch (final AppException e1) {
   * e1.printStackTrace();
   * } catch (final InformationalException e1) {
   * e1.printStackTrace();
   * }
   * dtls.sequenceNumber = seed;
   * dtls.sequenceNumber = getMod7Sin(seed);
   * dtls.processingStatus = SEQUENCESTATUS.UNUSED;
   * dtls.taxYear = 2000;
   * dtls.sequenceNumber = getMod7Sin(seed);
   * dtls.creationDate = DateTime.getCurrentDateTime();
   *
   * try {
   * bdmTaxSlipMRQSequenceOBJ.insert(dtls);
   *
   * Trace.kTopLevelLogger
   * .info("recordID " + dtls.recordID + " SequenceNumber: "
   * + dtls.sequenceNumber + " Status: " + dtls.processingStatus
   * + "Time: " + dtls.creationDate + " inserted");
   *
   * } catch (AppException | InformationalException e) {
   * e.printStackTrace();
   * }
   * }
   * TransactionInfo.getInfo().commit();
   * }
   */

  /**
   * Find/create seed records in BDMTaxSlipMRQBatchSequence entity and retrun
   * generated sequenceNumber
   *
   * @param key
   * @return long sequenceNumber
   * @throws AppException
   * @throws InformationalException
   */
  public static long
    getSequence(final BDMTaxSlipMRQBatchSequenceKeyStruct1 key)
      throws AppException, InformationalException {

    // Query BDMTAXSLIPMRQBATCHSEQUENCE for existing record
    final StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(
      "SELECT SBS.concernRoleID, SBS.creationDate, SBS.processingDateTime, SBS.recordID, SBS.seedID, ");
    sqlBuilder.append(
      "SBS.taxYear, SBS.sequenceNumber INTO :concernRoleID, :creationDate, :processingDateTime, ");
    sqlBuilder.append(":recordID, :seedID, :taxYear, :sequenceNumber");
    sqlBuilder.append(" FROM BDMTAXSLIPMRQBATCHSEQUENCE SBS ");
    sqlBuilder.append("WHERE SBS.CONCERNROLEID = " + key.concernRoleID);
    sqlBuilder.append(" AND SBS.taxYear = " + key.taxYear);

    Trace.kTopLevelLogger
      .info("BDMTaxSlipMRQSequence SQL to get   record count : "
        + sqlBuilder.toString());

    final CuramValueList<BDMTaxSlipMRQBatchSequenceDtls> details =
      DynamicDataAccess.executeNsMulti(BDMTaxSlipMRQBatchSequenceDtls.class,
        null, false, sqlBuilder.toString());
    long sequenceNumber = 0L;
    if (details.size() > BDMConstants.kZero) {

      // Generate sequence Number for return
      // sequenceNumber = getMod7Sin(details.get(0).seedID);
      sequenceNumber = details.get(0).sequenceNumber;

      // Update entity records with processingDateTime.
      final BDMTaxSlipMRQBatchSequence bdmTaxSlipMRQBatchSequenceObj =
        BDMTaxSlipMRQBatchSequenceFactory.newInstance();

      final BDMTaxSlipMRQBatchSequenceKey updateKey =
        new BDMTaxSlipMRQBatchSequenceKey();
      updateKey.recordID = details.get(0).recordID;

      final BDMTaxSlipMRQBatchSequenceDtls dtls =
        new BDMTaxSlipMRQBatchSequenceDtls();
      dtls.processingDateTime = DateTime.getCurrentDateTime();
      dtls.creationDate = details.get(0).creationDate;
      dtls.concernRoleID = details.get(0).concernRoleID;
      dtls.recordID = details.get(0).recordID;
      dtls.seedID = details.get(0).seedID;
      dtls.sequenceNumber = details.get(0).sequenceNumber;
      dtls.taxYear = key.taxYear;
      bdmTaxSlipMRQBatchSequenceObj.modify(updateKey, dtls);

    } else {
      // Create new seedID and generate sequence number
      sequenceNumber = createSeedSequence(key);
    }
    return sequenceNumber;
  }

  /**
   * Creates new seed record in BDMTaxSlipMRQBatchSequence entity
   *
   * @param key
   * @return long sequeneceNumber
   * @throws AppException
   * @throws InformationalException
   */
  public static long
    createSeedSequence(final BDMTaxSlipMRQBatchSequenceKeyStruct1 key)
      throws AppException, InformationalException {

    long seedID = 0L;
    long sequeneceNumber = 0L;

    final StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(
      "SELECT MAX(SBS.seedID) INTO :seedID FROM BDMTAXSLIPMRQBATCHSEQUENCE SBS");

    Trace.kTopLevelLogger
      .info("BDMTaxSlipMRQSequence SQL to get   max value of seed : "
        + sqlBuilder.toString());
    final CuramValueList<BDMTaxSlipMRQSequenceRecord> maxSeed =
      DynamicDataAccess.executeNsMulti(BDMTaxSlipMRQSequenceRecord.class,
        null, false, sqlBuilder.toString());

    final BDMTaxSlipMRQBatchSequence bdmTaxSlipMRQBatchSequenceObj =
      BDMTaxSlipMRQBatchSequenceFactory.newInstance();
    final BDMTaxSlipMRQBatchSequenceDtls dtls =
      new BDMTaxSlipMRQBatchSequenceDtls();

    if (maxSeed.get(0).getseedID() <= 0L) {

      dtls.seedID = kTaxSlipMRQInitialSeedNumber;
      seedID = dtls.seedID;
      dtls.recordID = UniqueIDFactory.newInstance().getNextID().uniqueID;
      dtls.concernRoleID = key.concernRoleID;
      dtls.creationDate = DateTime.getCurrentDateTime();
      dtls.processingDateTime = DateTime.getCurrentDateTime();
      dtls.sequenceNumber = getMod7Sin(dtls.seedID);
      dtls.taxYear = key.taxYear;
      bdmTaxSlipMRQBatchSequenceObj.insert(dtls);
      // Assign sequeneceNumber for return
      sequeneceNumber = dtls.sequenceNumber;

    } else if (maxSeed.get(0).getseedID() >= kTaxSlipMRQInitialSeedNumber
      & maxSeed.get(0).getseedID() < kTaxSlipMRQFinalSeedNumber) {
      dtls.seedID = maxSeed.get(0).getseedID() + 1L;
      seedID = dtls.seedID;
      dtls.recordID = UniqueIDFactory.newInstance().getNextID().uniqueID;
      dtls.concernRoleID = key.concernRoleID;
      dtls.creationDate = DateTime.getCurrentDateTime();
      dtls.processingDateTime = DateTime.getCurrentDateTime();
      dtls.sequenceNumber = getMod7Sin(dtls.seedID);
      dtls.taxYear = key.taxYear;
      bdmTaxSlipMRQBatchSequenceObj.insert(dtls);
      // Assign sequeneceNumber for return
      sequeneceNumber = dtls.sequenceNumber;

    } else {
      if (maxSeed.get(0).getseedID() >= kTaxSlipMRQFinalSeedNumber) {
        final AppException seedOutOfRangeExceptionErr =
          new AppException(BDMGENERATETAXSLIPBATCH.ERR_SEED_OUT_OF_RANGE);
        Trace.kTopLevelLogger.debug("MRQ Sequence Number creation Error : "
          + String.format(seedOutOfRangeExceptionErr.getMessage(),
            maxSeed.get(0).getseedID(), kTaxSlipMRQInitialSeedNumber,
            kTaxSlipMRQFinalSeedNumber));
        throw seedOutOfRangeExceptionErr;
      }
    }

    return sequeneceNumber;
  }

  /**
   * Get record details from BDMTaxSlipDataRL1 entity
   * Insert records into BDMTaxSlipMRQStagingData entity
   * Update record status in BDMTaxSlipDataRL1 entity
   *
   * @param batchProcessingID
   * @throws AppException
   * @throws InformationalException
   */
  public void processRecords(final BatchProcessingID batchProcessingID)
    throws AppException, InformationalException {

    final BDMTaxSlipDataRL1 BDMTaxSlipDataRL1Obj =
      BDMTaxSlipDataRL1Factory.newInstance();
    BDMTaxSlipDataRL1Dtls bdmTaxSlipDataRL1Dtls = new BDMTaxSlipDataRL1Dtls();
    final BDMTaxSlipDataRL1Key bdmTaxSlipDataRL1Key =
      new BDMTaxSlipDataRL1Key();
    bdmTaxSlipDataRL1Key.taxSlipDataID = batchProcessingID.recordID;

    // Get record details from BDMTaxSlipDataRL1 entity

    bdmTaxSlipDataRL1Dtls = BDMTaxSlipDataRL1Obj.read(bdmTaxSlipDataRL1Key);

    final BDMTaxSlipMRQStagingData BDMTaxSlipMRQStagingDataObj =
      BDMTaxSlipMRQStagingDataFactory.newInstance();
    final BDMTaxSlipMRQStagingDataDtls bdmTaxSlipMRQStagingDataDetails =
      new BDMTaxSlipMRQStagingDataDtls();
    bdmTaxSlipMRQStagingDataDetails.assign(bdmTaxSlipDataRL1Dtls);
    bdmTaxSlipMRQStagingDataDetails.recordID =
      UniqueIDFactory.newInstance().getNextID().uniqueID;
    bdmTaxSlipMRQStagingDataDetails.creationDate =
      DateTime.getCurrentDateTime();
    // Insert records into BDMTaxSlipMRQStagingData entity

    BDMTaxSlipMRQStagingDataObj.insert(bdmTaxSlipMRQStagingDataDetails);

    // Update record status in BDMTaxSlipDataRL1 entity
    bdmTaxSlipDataRL1Dtls.processingStatus = BDMTAXSLIPPROCSTATUS.TRANSFERRED;

    BDMTaxSlipDataRL1Obj.modify(bdmTaxSlipDataRL1Key, bdmTaxSlipDataRL1Dtls);

  }
}
