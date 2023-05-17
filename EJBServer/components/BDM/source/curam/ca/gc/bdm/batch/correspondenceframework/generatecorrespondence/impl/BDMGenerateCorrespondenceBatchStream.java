package curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.correspondenceframework.impl.BDMCorrespondenceTriggerUtil;
import curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateTombstoneData;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.fact.BDMCorrespondenceStagingFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMCorrespondenceStaging;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingPayloadStatusCommunicationID;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.communication.impl.BDMCommunication;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.BATCHPROCESSRESULTSTATUS;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.CORRESPONDENCEJOBSTATUS;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.CuramConst;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.type.CodeTable;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class BDMGenerateCorrespondenceBatchStream extends
  curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.base.BDMGenerateCorrespondenceBatchStream {

  protected int processedRecordsCount = 0;

  protected int skippedRecordsCount = 0;

  private final String recordIdentifier = "correspondenceStagingID";

  private final String entityIdentifier = "BDMCorrespondenceStaging";

  HashMap<String, String> logErrorMap = new HashMap<String, String>();

  @Inject
  BDMCommunicationHelper helper;

  public BDMGenerateCorrespondenceBatchStream() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * process the batch stream method and run the stream process
   *
   * @param Batch Proceses Stream KEy
   **/
  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    final BDMGenerateCorrespondenceBatchStreamWrapper streamWrapper =
      new BDMGenerateCorrespondenceBatchStreamWrapper(this);

    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_GENERATE_CORRESPONDENCE_BATCH;
    }

    Trace.kTopLevelLogger.info(BDMConstants.kBatchProcessingStrmKey
      + batchProcessStreamKey.instanceID);
    new BatchStreamHelper().runStream(batchProcessStreamKey, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedCount)
    throws AppException, InformationalException {

    final String chunkResult = processedRecordsCount
      + CuramConst.gkTabDelimiter + (skippedCount + skippedRecordsCount);
    processedRecordsCount = 0;
    skippedRecordsCount = 0;
    return chunkResult;

  }

  /**
   *
   *
   * @param Batch Proceses ID
   **/
  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    final BatchProcessingSkippedRecord skippedRecord = null;
    new BatchProcessingSkippedRecord();

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();

    final BDMCorrespondenceStagingKey correspondenceStagingKey =
      new BDMCorrespondenceStagingKey();
    correspondenceStagingKey.correspondenceStagingID =
      batchProcessingID.recordID;
    final BDMCorrespondenceStagingDtls correspondenceStagingDtls =
      correspondenceStagingObj.read(correspondenceStagingKey);

    if (correspondenceStagingDtls.communicationID == 0) {

      // Create correspondence
      final BDMCommunication bdmCommunication = new BDMCommunication();

      final BDMCorrespondenceDetails commDtls =
        new BDMCorrespondenceDetails();
      commDtls.toClientIsCorrespondent = true;
      commDtls.toParticipantRoleID =
        correspondenceStagingDtls.recipientConcernRoleID;
      commDtls.concernRoleID = correspondenceStagingDtls.clientConcernRoleID;

      commDtls.toCorrespondentName = helper
        .getConcernRoleName(correspondenceStagingDtls.clientConcernRoleID);
      BDMCorrespondenceTriggerUtil.setTemplateDetailsAndSubject(
        correspondenceStagingDtls.templateName, commDtls);
      commDtls.caseID = correspondenceStagingDtls.caseID;

      // TODO Based on the business requirement this has to be retrieved from
      // preferences?
      // If not set then the default is COMMUNICATIONMETHOD.HARDCOPY in
      // ConcernRoleCommunication.methodTypeCode
      // commDtls.isDigitalInd = ??;

      commDtls.toAddressID = helper.getAddressIDforConcern(
        correspondenceStagingDtls.clientConcernRoleID, false);

      // This is the default being set. When the over arching requirement to select
      // the
      // recipient is implemented then the correct code should be set here
      commDtls.toRecipientContactCode =
        BDMConstants.kGenerateCorrespondenceXMLClient;

      // Insert the record in ConcernRoleCommunication and BDMConcernRoleCommunication
      final long communicationID = bdmCommunication
        .insertCorrespondenceDetails(commDtls, COMMUNICATIONSTATUS.SUBMITTED);

      final BDMConcernRoleCommunicationKey bdmCCKey =
        new BDMConcernRoleCommunicationKey();
      bdmCCKey.communicationID = communicationID;
      final BDMConcernRoleCommunicationDtls bdmCCDtls =
        BDMConcernRoleCommunicationFactory.newInstance().read(bdmCCKey);

      // update the xml
      final BDMLetterTemplateTombstoneData tombstoneData =
        new BDMLetterTemplateTombstoneData();
      BDMCorrespondenceTriggerUtil.setTombstoneDataForDocumentIdentification(
        bdmCCDtls.trackingNumber, tombstoneData);
      final String stagedXML = BDMCorrespondenceTriggerUtil
        .mapToXML(tombstoneData, correspondenceStagingDtls.payLoad);

      correspondenceStagingObj.modify(correspondenceStagingKey,
        correspondenceStagingDtls);

      final BDMCorrespondenceStagingPayloadStatusCommunicationID dtls =
        new BDMCorrespondenceStagingPayloadStatusCommunicationID();
      // Update the table
      dtls.communicationID = communicationID;
      dtls.payLoad = stagedXML;
      dtls.status = CORRESPONDENCEJOBSTATUS.SUBMITTED;

      correspondenceStagingObj
        .modifyPayLoadStatusCommunicationID(correspondenceStagingKey, dtls);

    }

    Trace.kTopLevelLogger
      .info(BDMConstants.kProcessed + batchProcessingID.recordID);

    processedRecordsCount++;
    return skippedRecord;
  }

  /**
   * Process Skipped Cases, not used
   *
   * @param Batch Processing Skipped Record List
   **/
  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    for (final BatchProcessingSkippedRecord batchProcessingSkippedRecord : batchProcessingSkippedRecordList.dtls) {

      logErrorMap.put(BDMConstants.kRecordIdentifier, recordIdentifier);

      logErrorMap.put(BDMConstants.kEntityIdentifier, entityIdentifier);

      logErrorMap.put(BDMConstants.kRecordID,
        String.valueOf(batchProcessingSkippedRecord.recordID));

      logErrorMap.put(BDMConstants.kInstanceID,
        CodeTable.getOneItem(BATCHPROCESSNAME.TABLENAME,
          BATCHPROCESSNAME.BDM_GENERATE_CORRESPONDENCE_BATCH));

      logErrorMap.put(BDMConstants.kProcessingStatus, CodeTable.getOneItem(
        BATCHPROCESSNAME.TABLENAME, BATCHPROCESSRESULTSTATUS.FAILED));

      logErrorMap.put(BDMConstants.kErrorMessage,
        batchProcessingSkippedRecord.errorMessage);

    }

    // Iterating log through forEach
    logErrorMap.forEach(
      (key, value) -> Trace.kTopLevelLogger.error(key + " = " + value));

    /*
     * Output example
     *
     * EntityIdentifier = CaseParticipantRole
     * RecordIdentifier = participantRoleID
     * RecordID = 1090434059777081344
     * InstanceID = BDM Generate Payment Staging Data Batch
     * ProcessingStatus = Failed
     * ErrorMessage = Record Not found
     */

  }

}
