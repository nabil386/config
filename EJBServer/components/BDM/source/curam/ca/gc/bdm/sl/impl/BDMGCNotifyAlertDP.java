package curam.ca.gc.bdm.sl.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYINTERFACETYPE;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYNOTIFICATIONSTATUS;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDtls;
import curam.ca.gc.bdm.entity.struct.BDMWMInstanceDataDtls;
import curam.ca.gc.bdm.gcnotify.impl.BDMGCNotifyHelper;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManagerImpl;
import curam.core.fact.WMInstanceDataFactory;
import curam.core.intf.WMInstanceData;
import curam.core.struct.WMInstanceDataDtls;
import curam.core.struct.WMInstanceDataKey;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.participant.impl.ConcernRoleDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import java.util.ArrayList;

public class BDMGCNotifyAlertDP
  extends curam.ca.gc.bdm.sl.base.BDMGCNotifyAlertDP {

  @Inject
  private CitizenWorkspaceAccountManagerImpl citizenWorkspaceAccountManager;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  protected BDMGCNotifyAlertDP() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  BDMGCNotifyHelper gcNotifyHelper = new BDMGCNotifyHelper();

  final BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();

  @Override
  public void createGCNotifyAlertDP(final long ticketID,
    final long inst_data_id, final boolean flag)
    throws AppException, InformationalException {

    final WMInstanceDataKey wmInstanceDataKey = new WMInstanceDataKey();
    wmInstanceDataKey.wm_instDataID = inst_data_id;

    final WMInstanceData wmInstanceDataObj =
      WMInstanceDataFactory.newInstance();
    final WMInstanceDataDtls wmInstanceDataDtls =
      wmInstanceDataObj.read(wmInstanceDataKey);

    final Long correspondentConcernRoleID = wmInstanceDataDtls.concernRoleID;

    final BDMWMInstanceDataDtls bdmWMInstanceDataDtls = bdmCommHelper
      .readBDMWMInstanceDatabyID(wmInstanceDataDtls.wm_instDataID);
    final String gcNotifyTemplateType =
      bdmWMInstanceDataDtls.gcNotifyTemplateType;

    try {
      if (citizenWorkspaceAccountManager
        .hasLinkedAccount(concernRoleDAO.get(correspondentConcernRoleID))) {
        final ArrayList<DynamicEvidenceDataDetails> receiveAlertEvidList =
          gcNotifyHelper
            .getReceiveAlertPreference(correspondentConcernRoleID);
        for (final DynamicEvidenceDataDetails receiveAlertEvid : receiveAlertEvidList) {
          final BDMGcNotifyRequestDataDtls gcNotifyRequestData =
            gcNotifyHelper.createAlertData(correspondentConcernRoleID,
              gcNotifyTemplateType, receiveAlertEvid);
          // BEGIN TASK 16487 Modified For Notification Message
          // if (!StringUtil.isNullOrEmpty(gcNotifyRequestData.interfaceType)) {
          // END TASK 16487 Modified For Notification Message
          if (gcNotifyRequestData.interfaceType
            .equals(BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_REALTIME)) {
            gcNotifyHelper.sendGCNotifyRequest(gcNotifyRequestData);
          } else if (gcNotifyRequestData.interfaceType
            .equals(BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH)) {
            gcNotifyRequestData.status =
              BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC;
            gcNotifyHelper.insertGCNotifyRequestData(gcNotifyRequestData);
          }
          // }
        }
      }
    } catch (

    final Exception e1) {
      e1.printStackTrace();
    }

  }

}
