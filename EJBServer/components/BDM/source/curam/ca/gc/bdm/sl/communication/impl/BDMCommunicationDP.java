package curam.ca.gc.bdm.sl.communication.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationImpl;
import curam.ca.gc.bdm.entity.struct.BDMWMInstanceDataDtls;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.intf.WMInstanceData;
import curam.core.sl.struct.MSWordCommunicationDetails1;
import curam.core.sl.struct.ProFormaCommDetails1;
import curam.core.sl.struct.ProFormaCommKey;
import curam.core.sl.struct.ReadMSWordCommunicationKey;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.WMInstanceDataDtls;
import curam.core.struct.WMInstanceDataKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;

public class BDMCommunicationDP
  extends curam.ca.gc.bdm.sl.communication.base.BDMCommunicationDP {

  /** Constructor */
  public BDMCommunicationDP() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  protected BDMCommunicationImpl bdmCommunicationImpl;

  /**
   * Creates Communication in DP process asynchronize mode
   *
   * @param ticketID
   * @param inst_data_ID
   * @param flag
   */
  @Override
  public void createCommunicationDP(final long ticketID,
    final long inst_data_id, final boolean flag)
    throws AppException, InformationalException {

    final WMInstanceDataKey wmInstanceDataKey = new WMInstanceDataKey();
    wmInstanceDataKey.wm_instDataID = inst_data_id;

    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();
    final WMInstanceDataDtls wmInstanceDataDtls =
      wmInstanceDataObj.read(wmInstanceDataKey);

    final BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();

    final BDMWMInstanceDataDtls bdmWMInstanceDataDtls =
      bdmCommHelper.readBDMWMInstanceDatabyID(inst_data_id);

    // create struct to hold the essential information for Auto create
    // communication
    final ConcernRoleCommunicationDtls commDtls =
      new ConcernRoleCommunicationDtls();
    commDtls.caseID = wmInstanceDataDtls.caseID;
    commDtls.correspondentConcernRoleID = wmInstanceDataDtls.concernRoleID;
    commDtls.proFormaID = bdmWMInstanceDataDtls.proformaID;

    commDtls.communicationFormat = bdmWMInstanceDataDtls.communicationFormat;
    commDtls.proFormaInd =
      commDtls.communicationFormat.equals(COMMUNICATIONFORMAT.PROFORMA);

    // call the auto create communication
    final ConcernRoleCommunicationKey commKey =
      bdmCommunicationImpl.createAutoCommunication(commDtls);

    // Check if the auto create communication successfully created the
    // communication record
    if (commKey.communicationID != 0l) {
      // if success submit the communication using DP Submit communication
      bdmCommHelper.createDPSubmitCommunication(commKey.communicationID);
    }
  }

  /**
   * Submit Communication in DP process asynchronize mode
   *
   * @param ticketID
   * @param inst_data_ID
   * @param flag
   */
  @Override
  public void submitCommunicationDP(final long ticketID,
    final long inst_data_id, final boolean flag)
    throws AppException, InformationalException {

    // create WMInstanceData Struct and related data
    final WMInstanceDataKey wmInstanceDataKey = new WMInstanceDataKey();
    wmInstanceDataKey.wm_instDataID = inst_data_id;

    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();
    final WMInstanceDataDtls wmInstanceDataDtls =
      wmInstanceDataObj.read(wmInstanceDataKey);

    final BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();

    final BDMWMInstanceDataDtls bdmWMInstanceDataDtls =
      bdmCommHelper.readBDMWMInstanceDatabyID(inst_data_id);

    final ConcernRoleCommunicationKey crcKey =
      new ConcernRoleCommunicationKey();

    // get the communicationID from the comment field
    crcKey.communicationID = bdmWMInstanceDataDtls.communicationID;

    final ConcernRoleCommunication crcOjb =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationDtls crcDtls = crcOjb.read(crcKey);

    // check the communication type
    if (crcDtls.communicationFormat.equals(COMMUNICATIONFORMAT.PROFORMA)) {
      // if communication is proforma, submit the communicatio with the proforma
      // specific methods
      final ProFormaCommKey proformaKey = new ProFormaCommKey();
      proformaKey.communicationID = crcDtls.communicationID;
      ProFormaCommDetails1 modifyProformaDetails = new ProFormaCommDetails1();
      modifyProformaDetails = bdmCommunicationImpl.readProForma1(proformaKey);

      bdmCommunicationImpl.submitProformaComm(modifyProformaDetails);

      modifyProformaDetails.communicationStatus =
        COMMUNICATIONSTATUS.SUBMITTED;
      modifyProformaDetails.communicationDate = Date.getCurrentDate();
      bdmCommHelper.updateProFormaCommunicationStatus(modifyProformaDetails);
    } else if (crcDtls.communicationFormat
      .equals(COMMUNICATIONFORMAT.MSWORD)) {
      // if communication is MS Word, submit the communicatio with the MS Word
      // specific methods

      final ReadMSWordCommunicationKey msWordKey =
        new ReadMSWordCommunicationKey();
      msWordKey.communicationID = crcDtls.communicationID;
      final MSWordCommunicationDetails1 modifyMSWordDetails =
        bdmCommunicationImpl.readMSWordCommunication1(msWordKey);
      modifyMSWordDetails.communicationStatus = COMMUNICATIONSTATUS.SUBMITTED;
      bdmCommHelper.updateMSWordCommunicationStatus(modifyMSWordDetails);
      bdmCommunicationImpl.submitMSWordComm(modifyMSWordDetails);
    }

  }

}
