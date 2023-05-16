package curam.ca.gc.bdm.batch.correspondences.bulkprint.impl;

import com.google.inject.Inject;
import com.lowagie.text.pdf.PdfReader;
import curam.attachment.impl.Attachment;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYINTERFACETYPE;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYNOTIFICATIONSTATUS;
import curam.ca.gc.bdm.codetable.BDMGCNotifyTemplateType;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDtls;
import curam.ca.gc.bdm.entity.fact.BDMBulkPrintMetaDataFactory;
import curam.ca.gc.bdm.entity.intf.BDMBulkPrintMetaData;
import curam.ca.gc.bdm.entity.struct.BDMBulkPrintMetaDataDtls;
import curam.ca.gc.bdm.entity.struct.BDMBulkPrintMetaDataDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMBulkPrintMetaDataKey;
import curam.ca.gc.bdm.entity.struct.BDMBulkPrintMetaDataKeyStruct1;
import curam.ca.gc.bdm.gcnotify.impl.BDMGCNotifyHelper;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.codetable.ATTACHMENTSTATUS;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.COMMUNICATIONMETHOD;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.DOCUMENTTYPE;
import curam.core.facade.fact.UniqueIDFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.impl.EnvVars;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.CommAttachmentLinkDtls;
import curam.core.struct.CommAttachmentLinkDtlsList;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.impl.AddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.DateTime;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;

public class BDMCorrespondenceBulkPrintBatchStream extends
  curam.ca.gc.bdm.batch.correspondences.bulkprint.base.BDMCorrespondenceBulkPrintBatchStream {

  private static final String kBulkPrintDefaultProductCode =
    StringUtil.isNullOrEmpty(
      Configuration.getProperty(EnvVars.BDM_BULK_PRINT_METADATA_PRODUCTCODE))
        ? EnvVars.BDM_BULK_PRINT_METADATA_PRODUCTCODE_DEFAULT : Configuration
          .getProperty(EnvVars.BDM_BULK_PRINT_METADATA_PRODUCTCODE);

  private static final String kBulkPrintDefaultOuterEnvelop =
    StringUtil.isNullOrEmpty(
      Configuration.getProperty(EnvVars.BDM_BULK_PRINT_METADATA_OUTERENVELOP))
        ? EnvVars.BDM_BULK_PRINT_METADATA_OUTERENVELOP_DEFAULT : Configuration
          .getProperty(EnvVars.BDM_BULK_PRINT_METADATA_OUTERENVELOP);

  private static final String kBulkPrintMetaDataDocumentType =
    StringUtil.isNullOrEmpty(
      Configuration.getProperty(EnvVars.BDM_BULK_PRINT_METADATA_DOCUMENTTYPE))
        ? EnvVars.BDM_BULK_PRINT_METADATA_DOCUMENTTYPE_DEFAULT : Configuration
          .getProperty(EnvVars.BDM_BULK_PRINT_METADATA_DOCUMENTTYPE);

  private static final char kBulkPrintProcessingStatusUnprocessed = 'U';

  private static final char kBulkPrintProcessingStatusRetry = 'R';

  private static final char kBulkPrintProcessingStatusError = 'E';

  @Inject
  protected CaseHeaderDAO caseHeaderDAO;

  @Inject
  protected Attachment attachment;

  @Inject
  private AddressDAO addressDAO;

  ConcernRoleCommunication commObj =
    ConcernRoleCommunicationFactory.newInstance();

  BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();

  public BDMCorrespondenceBulkPrintBatchStream() {

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

    final BDMCorrespondenceBulkPrintBatchStreamWrapper streamWrapper =
      new BDMCorrespondenceBulkPrintBatchStreamWrapper(this);

    if (StringUtils.isEmpty(batchProcessStreamKey.instanceID)) {
      batchProcessStreamKey.instanceID =
        BATCHPROCESSNAME.BDM_CORRESPONDENCE_BULK_PRINT_BATCH;
    }

    Trace.kTopLevelLogger.info(BDMConstants.kBatchProcessingStrmKey
      + batchProcessStreamKey.instanceID);
    new BatchStreamHelper().runStream(batchProcessStreamKey, streamWrapper);

  }

  /**
   * Get Chunk Result, not used
   *
   * @param Skipped count
   **/
  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    return null;
  }

  /**
   * Process the communication record for bulk printing and mark the record
   * as sent add the attachment Indicator for the citizen portal. Check the
   * personal contact preference for GC notify alert option and then create the
   * API Staging entry for the API Batch to send the daily alert
   *
   * @param Batch Proceses ID
   **/
  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    BatchProcessingSkippedRecord skippedRecord =
      new BatchProcessingSkippedRecord();
    skippedRecord.recordID = batchProcessingID.recordID;
    try {
      final ConcernRoleCommunicationKey commKey =
        new ConcernRoleCommunicationKey();
      commKey.communicationID = batchProcessingID.recordID;
      final ConcernRoleCommunicationDtls commDtls = commObj.read(commKey);

      if (commDtls.concernRoleID == 0l && commDtls.caseID != 0l) {
        commDtls.concernRoleID =
          caseHeaderDAO.get(commDtls.caseID).getConcernRole().getID();
      }

      // enable the stored attachment for the Citizen portal
      commDtls.attachmentInd = true;
      // enable the record on the citizen portal
      commDtls.communicationStatus = COMMUNICATIONSTATUS.SENT;
      commDtls.communicationDate = Date.getCurrentDate();
      // read preferred delivery mode
      final String deliveryMethod = bdmCommHelper
        .calculateDeliveryModes(commDtls.correspondentConcernRoleID);
      commDtls.methodTypeCode = deliveryMethod;
      commObj.modify(commKey, commDtls);

      // determine the send method from the preferred delivery mode
      if (StringUtil.isNullOrEmpty(deliveryMethod)
        || deliveryMethod.equals(COMMUNICATIONMETHOD.HARDCOPY)) {
        // HARDCOPY => create bulk print entry
        createPrintCommDetails(commDtls);
        // TODO: connect with print services to get print result
      } else if (deliveryMethod.equals(COMMUNICATIONMETHOD.DIGITAL)) {
        // DIGITAL => write API detail to the API staging table entry for the
        // API batch to send alert daily
        final BDMGCNotifyHelper notifyHelper = new BDMGCNotifyHelper();
        final ArrayList<DynamicEvidenceDataDetails> receiveAlertEvidList =
          notifyHelper
            .getReceiveAlertPreference(commDtls.correspondentConcernRoleID);
        if (receiveAlertEvidList.size() > 0)
          writeRecordToStagingTable(commDtls.correspondentConcernRoleID,
            BDMGCNotifyTemplateType.BDM_GC_COR, receiveAlertEvidList);
      }

      Trace.kTopLevelLogger
        .info(BDMConstants.kProcessed + batchProcessingID.recordID);
      skippedRecord = null;
    } catch (final Exception e) {
      e.printStackTrace();
      skippedRecord.errorMessage = e.getMessage();
      Trace.kTopLevelLogger
        .info(BDMConstants.kSkipped + batchProcessingID.recordID);
    }

    return skippedRecord;
  }

  /**
   * Create the print communication detail for the bulk print batch to populate
   * the result and details
   *
   * @param Communication Detail
   **/
  protected BDMBulkPrintMetaDataDtls
    createPrintCommDetails(final ConcernRoleCommunicationDtls commDtls)
      throws AppException, InformationalException, IOException {

    final BDMBulkPrintMetaData bulkPrintMetaDataObj =
      BDMBulkPrintMetaDataFactory.newInstance();
    BDMBulkPrintMetaDataDtls bulkPrintDtls = null;

    final AttachmentDtls commAttachmentObj =
      readCommAttachmentByStatusAndDocType(commDtls, DOCUMENTTYPE.LETTER,
        ATTACHMENTSTATUS.ACTIVE);
    final BDMBulkPrintMetaDataKeyStruct1 bulkPrintCommunicationIDKey =
      new BDMBulkPrintMetaDataKeyStruct1();
    bulkPrintCommunicationIDKey.communicationID = commDtls.communicationID;
    final BDMBulkPrintMetaDataDtlsList bulkPrintList = bulkPrintMetaDataObj
      .searchByCommunicationID(bulkPrintCommunicationIDKey);

    if (bulkPrintList.dtls.size() > 0) {
      bulkPrintDtls = bulkPrintList.dtls.get(0);
      if (bulkPrintDtls.processingStatus == kBulkPrintProcessingStatusError
        && commDtls.communicationStatus
          .equals(COMMUNICATIONSTATUS.RESUBMITTED)) {
        bulkPrintDtls.communicationDate =
          commDtls.communicationDate.getDateTime();
        // set bulk print meta data with communication attachment detail
        bulkPrintDtls.attachmentID = commAttachmentObj.attachmentID;
        bulkPrintDtls.attachmentName = commAttachmentObj.attachmentName;
        bulkPrintDtls.documentType = kBulkPrintMetaDataDocumentType;
        // set bulk print related meta data code
        bulkPrintDtls.destinationCountry =
          addressDAO.get(commDtls.addressID).getAddressDetails().countryCode;
        bulkPrintDtls.processingStatus = kBulkPrintProcessingStatusRetry;
        final BDMBulkPrintMetaDataKey bulkPrintMetaDataKey =
          new BDMBulkPrintMetaDataKey();
        bulkPrintMetaDataKey.recordID = bulkPrintDtls.recordID;
        // get the page count of the pdf
        final AttachmentKey attachmentKey = new AttachmentKey();
        attachmentKey.attachmentID = commAttachmentObj.attachmentID;
        final AttachmentDtls attachmentDtls = attachment.read(attachmentKey);
        final byte[] pdfByteArray =
          attachmentDtls.attachmentContents.copyBytes();
        try {
          final PdfReader reader = new PdfReader(pdfByteArray);
          bulkPrintDtls.pageCount = reader.getNumberOfPages();
          reader.close();
        } catch (final IOException e) {
          bulkPrintDtls.processingStatus = kBulkPrintProcessingStatusError;
        }

        bulkPrintDtls.destinationCountry =
          addressDAO.get(commDtls.addressID).getAddressDetails().countryCode;
        bulkPrintMetaDataObj.modify(bulkPrintMetaDataKey, bulkPrintDtls);
      }
    } else {
      bulkPrintDtls = new BDMBulkPrintMetaDataDtls();
      // add print communication details
      bulkPrintDtls.recordID =
        UniqueIDFactory.newInstance().getNextID().uniqueID;
      // set bulk print meta data with communication detail
      bulkPrintDtls.communicationID = commDtls.communicationID;
      bulkPrintDtls.concernRoleID = commDtls.correspondentConcernRoleID;
      bulkPrintDtls.caseID = commDtls.caseID;
      bulkPrintDtls.communicationDate =
        commDtls.communicationDate.getDateTime();
      // set bulk print meta data with communication attachment detail
      bulkPrintDtls.attachmentID = commAttachmentObj.attachmentID;
      bulkPrintDtls.attachmentName = commAttachmentObj.attachmentName;
      bulkPrintDtls.documentType = kBulkPrintMetaDataDocumentType;
      // set bulk print related meta data code
      bulkPrintDtls.destinationCountry =
        addressDAO.get(commDtls.addressID).getAddressDetails().countryCode;
      bulkPrintDtls.creationDate = DateTime.getCurrentDateTime();
      bulkPrintDtls.productCode = kBulkPrintDefaultProductCode;
      bulkPrintDtls.outerEnvelope = kBulkPrintDefaultOuterEnvelop;
      bulkPrintDtls.processingStatus = kBulkPrintProcessingStatusUnprocessed;
      // write and return the print communication detail

      // get the page count of the pdf
      final AttachmentKey attachmentKey = new AttachmentKey();
      attachmentKey.attachmentID = commAttachmentObj.attachmentID;
      final AttachmentDtls attachmentDtls = attachment.read(attachmentKey);
      final byte[] pdfByteArray =
        attachmentDtls.attachmentContents.copyBytes();

      try {
        final PdfReader reader = new PdfReader(pdfByteArray);
        bulkPrintDtls.pageCount = reader.getNumberOfPages();
        reader.close();
      } catch (final IOException e) {
        bulkPrintDtls.processingStatus = kBulkPrintProcessingStatusError;
      }
      bulkPrintDtls.destinationCountry =
        addressDAO.get(commDtls.addressID).getAddressDetails().countryCode;
      bulkPrintMetaDataObj.insert(bulkPrintDtls);
    }
    return bulkPrintDtls;
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

    // TODO Auto-generated method stub

  }

  /**
   * Write the record to the Staging Table
   *
   * @param reference Type
   * @param concern role Id
   **/
  private void writeRecordToStagingTable(final long concernRoleID,
    final String templateType,
    final ArrayList<DynamicEvidenceDataDetails> receiveAlertEvidList)
    throws AppException, InformationalException {

    BDMGcNotifyRequestDataDtls stagingDataDtls =
      new BDMGcNotifyRequestDataDtls();

    final BDMGCNotifyHelper notifyHelper = new BDMGCNotifyHelper();

    for (final DynamicEvidenceDataDetails receiveAlertEvid : receiveAlertEvidList) {

      // populate alert API data
      stagingDataDtls = notifyHelper.createAlertData(concernRoleID,
        templateType, receiveAlertEvid);
      stagingDataDtls.status = BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC;
      stagingDataDtls.interfaceType =
        BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH;
      if (notifyHelper
        .verifyInsertGCNotifyRequestDataForConcernRole(stagingDataDtls).dtls
          .size() == 0) {

        notifyHelper.insertGCNotifyRequestData(stagingDataDtls);
        Trace.kTopLevelLogger
          .info("Created GC Notify Data for " + concernRoleID);
      } else {
        Trace.kTopLevelLogger
          .info("GC Notify Data exists for " + concernRoleID);
      }
    }
  }

  private AttachmentDtls readCommAttachmentByStatusAndDocType(
    final ConcernRoleCommunicationDtls commDtls, final String documentType,
    final String attachmentStatus)
    throws AppException, InformationalException {

    final CommAttachmentLinkDtlsList commAttLnkList =
      bdmCommHelper.getCommAttachmentLinkList(commDtls.communicationID);
    for (final CommAttachmentLinkDtls commAttLnk : commAttLnkList.dtls
      .items()) {
      final AttachmentKey attKey = new AttachmentKey();
      attKey.attachmentID = commAttLnk.attachmentID;
      final AttachmentDtls attachmentDtls = attachment.read(attKey);
      if (attachmentDtls.attachmentStatus.equals(attachmentStatus)
        && attachmentDtls.documentType.equals(documentType)) {
        return attachmentDtls;
      }
    }

    return null;
  }

}
