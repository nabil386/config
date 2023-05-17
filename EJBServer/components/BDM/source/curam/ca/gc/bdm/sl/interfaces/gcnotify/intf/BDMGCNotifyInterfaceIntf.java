package curam.ca.gc.bdm.sl.interfaces.gcnotify.intf;

import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDetailsList;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.io.IOException;

public interface BDMGCNotifyInterfaceIntf {

  public String sendGCNotifyBulkEmailRequest(
    final BDMGcNotifyRequestDataDetailsList rowsList)
    throws AppException, InformationalException, IOException;

  public String sendGCNotifyBulkSMSRequest(
    final BDMGcNotifyRequestDataDetailsList rowsList)
    throws AppException, InformationalException, IOException;

  public String sendGCNotifyEmailRequest(final String jsonObj)
    throws AppException, InformationalException, IOException;

  public String sendGCNotifySMSRequest(final String jsonObj)
    throws AppException, InformationalException, IOException;

  public String sendGCNotifyRTEmailRequest(
    final BDMGcNotifyRequestDataDtls requestDataDtls)
    throws AppException, InformationalException, IOException;

  public String
    sendGCNotifyRTSMSRequest(final BDMGcNotifyRequestDataDtls requestDataDtls)
      throws AppException, InformationalException, IOException;

}
