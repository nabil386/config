package curam.ca.gc.bdm.batch.correspondences.automated.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.facade.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.facade.communication.intf.BDMCommunication;
import curam.ca.gc.bdm.facade.participant.fact.BDMParticipantFactory;
import curam.ca.gc.bdm.facade.participant.intf.BDMParticipant;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountInfo;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.core.facade.struct.ReadParticipantAddressListKey;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.PrintedCommunicationDetailsFactory;
import curam.core.impl.BatchStreamHelper;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.intf.PrintedCommunicationDetails;
import curam.core.sl.fact.CommunicationFactory;
import curam.core.sl.intf.Communication;
import curam.core.sl.struct.ProFormaCommDetails1;
import curam.core.struct.BatchProcessStreamKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingSkippedRecord;
import curam.core.struct.BatchProcessingSkippedRecordList;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleKey;
import curam.participant.impl.ConcernRole;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Trace;

public class BDMDirectDepositLetterBatchStream extends
  curam.ca.gc.bdm.batch.correspondences.automated.base.BDMDirectDepositLetterBatchStream {

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  PrintedCommunicationDetails printCommObj =
    PrintedCommunicationDetailsFactory.newInstance();

  ConcernRoleCommunication crCommObj =
    ConcernRoleCommunicationFactory.newInstance();

  BDMConcernRoleCommunication bdmCRCommObj =
    BDMConcernRoleCommunicationFactory.newInstance();

  BDMParticipant bdmParticipantObj = BDMParticipantFactory.newInstance();

  Communication commObj = CommunicationFactory.newInstance();

  BDMCommunication bdmCommObj = BDMCommunicationFactory.newInstance();

  final static long kDirectDepositTemplateID = 2003l;

  /**
   * Main method for direct deposit batch stream.
   *
   * @param BatchProcessStreamKey main key that is used to process the batch
   */
  @Override
  public void process(final BatchProcessStreamKey batchProcessStreamKey)
    throws AppException, InformationalException {

    final BDMDirectDepositLetterBatchStreamWrapper streamWrapper =
      new BDMDirectDepositLetterBatchStreamWrapper(this);

    new BatchStreamHelper().runStream(batchProcessStreamKey, streamWrapper);

  }

  @Override
  public String getChunkResult(final int skippedCasesCount)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Method to process record for the bulk print stream
   */
  @Override
  public BatchProcessingSkippedRecord
    processRecord(final BatchProcessingID batchProcessingID)
      throws AppException, InformationalException {

    BatchProcessingSkippedRecord skippedRecord =
      new BatchProcessingSkippedRecord();
    skippedRecord.recordID = batchProcessingID.recordID;
    try {

      final ReadParticipantAddressListKey participantAddressKey =
        new ReadParticipantAddressListKey();
      participantAddressKey.maintainAddressKey.concernRoleID =
        batchProcessingID.recordID;

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = batchProcessingID.recordID;

      final ProFormaCommDetails1 proformaDetails = new ProFormaCommDetails1();

      proformaDetails.correspondentParticipantRoleID =
        batchProcessingID.recordID;
      proformaDetails.correspondentConcernRoleID = batchProcessingID.recordID;
      proformaDetails.proFormaID = kDirectDepositTemplateID;

      final ConcernRoleCommKeyOut commReturnKey =
        commObj.createProFormaReturningID(proformaDetails);

      final ConcernRoleCommunicationDtls commDetails =
        new ConcernRoleCommunicationDtls();
      commDetails.communicationID = commReturnKey.communicationID;
      commDetails.communicationStatus = COMMUNICATIONSTATUS.SUBMITTED;
      bdmCommObj.modifyCommunication(commDetails);

      Trace.kTopLevelLogger.info("Processed: " + batchProcessingID.recordID);
      skippedRecord = null;
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info("Skipped: " + batchProcessingID.recordID);
    }

    return skippedRecord;
  }

  @Override
  public void processSkippedCases(
    final BatchProcessingSkippedRecordList batchProcessingSkippedRecordList)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub

  }

  /**
   * Method to check if account is linked or not
   *
   * @param userName
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public boolean isLinkedUser(final String userName)
    throws AppException, InformationalException {

    // if account is linked
    final CitizenWorkspaceAccountInfo cwAccountInfo =
      this.citizenWorkspaceAccountManager.readAccountBy(userName);
    final ConcernRole userConcernRole = cwAccountInfo.getConcernRole();
    if (userConcernRole != null) {
      return true;
    }
    return false;
  }

}
