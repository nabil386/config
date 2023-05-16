package curam.ca.gc.bdm.batch.bdmpurgeinprogressrfrdata.impl;

import curam.citizenworkspace.entity.struct.CitizenScriptInfoDtls;
import curam.codetable.ONLINEAPPEALREQUESTSTATUS;
import curam.core.sl.entity.fact.ExternalUserFactory;
import curam.core.sl.entity.struct.ExternalUserKey;
import curam.core.sl.fact.OnlineAppealRequestFactory;
import curam.core.sl.struct.OnlineAppealRequestDtls;
import curam.core.sl.struct.OnlineAppealRequestKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.util.dataaccess.ReadmultiOperation;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.type.NotFoundIndicator;

/**
 * This is a helper class for the BDM RFR Purge In Progress Data
 * Process Batch. It retrieves the batchProcessingID of the draft application
 * only if the user is an internal user.
 *
 * @author Mahesh.Hadimani
 *
 */
public class BDMRFRCitizenScriptInfoReadMultiOperation
  extends ReadmultiOperation<CitizenScriptInfoDtls> {

  private final BatchProcessingIDList batchProcessingIDList;

  public BDMRFRCitizenScriptInfoReadMultiOperation(
    final BatchProcessingIDList batchProcessingIDList) {

    this.batchProcessingIDList = batchProcessingIDList;
  }

  /**
   * If the user that submitted the application form is external, add the
   * batchProcessingID to the list that will be processed.
   *
   * @return boolean
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public boolean operation(final CitizenScriptInfoDtls dtls)
    throws AppException, InformationalException {

    final BatchProcessingID batchProcessingID = new BatchProcessingID();
    if (isUserExternal(dtls.userName)
      && isAppealRFRAppInProgress(dtls.applicationRelatedID)) {
      batchProcessingID.recordID = dtls.scriptExecutionID;
      this.batchProcessingIDList.dtls.add(batchProcessingID);
    }
    return true;
  }

  /**
   * This method evaluates whether a Related Application is an Appeal
   * application in initiated or started state
   *
   *
   * @param username
   * @return boolean if the user is an external user
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isAppealRFRAppInProgress(final long applicationRelatedID)
    throws AppException, InformationalException {

    final OnlineAppealRequestKey onlineAppealRequestKey =
      new OnlineAppealRequestKey();
    onlineAppealRequestKey.onlineAppealRequestID = applicationRelatedID;

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    final OnlineAppealRequestDtls onlineAppealRequestDtls =
      OnlineAppealRequestFactory.newInstance().read(notFoundIndicator,
        onlineAppealRequestKey);

    if (!notFoundIndicator.isNotFound()
      && (ONLINEAPPEALREQUESTSTATUS.INITIATED
        .equals(onlineAppealRequestDtls.status)
        || ONLINEAPPEALREQUESTSTATUS.STARTED
          .equals(onlineAppealRequestDtls.status))) {
      return true;
    }

    return false;

  }

  /**
   * This method evaluates whether a person is a external user with the username
   * passed in.
   *
   * @param username
   * @return boolean if the user is an external user
   * @throws AppException
   * @throws InformationalException
   */
  private boolean isUserExternal(final String username)
    throws AppException, InformationalException {

    boolean result = false;
    final ExternalUserKey key = new ExternalUserKey();

    try {
      key.userName = username;
      ExternalUserFactory.newInstance().read(key);
      result = true;
    } catch (final RecordNotFoundException rnfe) {
    }

    return result;
  }

  public BatchProcessingIDList getCitizenScriptInfoIDList() {

    return this.batchProcessingIDList;
  }
}
