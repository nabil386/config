package curam.ca.gc.bdm.batch.bdmcitizenworkspacepurgeinprogressdataprocess.impl;

import curam.citizenworkspace.entity.struct.CitizenScriptInfoDtls;
import curam.core.sl.entity.fact.ExternalUserFactory;
import curam.core.sl.entity.struct.ExternalUserKey;
import curam.core.struct.BatchProcessingID;
import curam.core.struct.BatchProcessingIDList;
import curam.util.dataaccess.ReadmultiOperation;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;

/**
 * This is a helper class for the BDM Citizen Workspace Purge In Progress Data
 * Process Batch. It retrieves the batchProcessingID of the draft application
 * only if the user is an internal user.
 *
 * @author alim.maredia
 *
 */
public class BDMCitizenScriptInfoReadMultiOperation
  extends ReadmultiOperation<CitizenScriptInfoDtls> {

  private final BatchProcessingIDList batchProcessingIDList;

  public BDMCitizenScriptInfoReadMultiOperation(
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
    if (isUserExternal(dtls.userName)) {
      batchProcessingID.recordID = dtls.scriptExecutionID;
      this.batchProcessingIDList.dtls.add(batchProcessingID);
    }
    return true;
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
