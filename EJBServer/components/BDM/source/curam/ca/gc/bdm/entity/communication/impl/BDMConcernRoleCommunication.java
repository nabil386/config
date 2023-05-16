package curam.ca.gc.bdm.entity.communication.impl;

import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.NotFoundIndicator;

public class BDMConcernRoleCommunication extends
  curam.ca.gc.bdm.entity.communication.base.BDMConcernRoleCommunication {

  /*
   * Pre modify if record not found insert new record.
   *
   * @param BDM ConcernRole Communication Entity Key
   *
   * @param BDM ConcernRole Communication Entity Details
   */
  @Override
  protected void premodify(final BDMConcernRoleCommunicationKey key,
    final BDMConcernRoleCommunicationDtls details)
    throws AppException, InformationalException {

    createNewInstanceNotFound(key);
  }

  /*
   * insert new record if read by the primary key is not found
   *
   * @param BDM ConcernRole Communication Entity Key
   *
   */
  protected void
    createNewInstanceNotFound(final BDMConcernRoleCommunicationKey key)
      throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication bdmCommObj =
      BDMConcernRoleCommunicationFactory.newInstance();
    final NotFoundIndicator nfi = new NotFoundIndicator();
    bdmCommObj.read(nfi, key);
    if (nfi.isNotFound()) {
      final BDMConcernRoleCommunicationDtls newDetails =
        new BDMConcernRoleCommunicationDtls();
      newDetails.communicationID = key.communicationID;
      bdmCommObj.insert(newDetails);
    }
  }

}
