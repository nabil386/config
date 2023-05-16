package curam.ca.gc.bdm.sl.interfaces.bdmdojoutbound.intf;

import curam.ca.gc.bdm.entity.struct.BDMDoJOutboundStageDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.io.IOException;
import java.text.ParseException;

public interface BDMDoJOutboundInterfaceIntf {

  public String sendPayload(final String apiURL, final String apiKEY,
    final String requestObj)
    throws AppException, InformationalException, IOException, ParseException;

  public void createSystemEventRequest(final int transactionCount)
    throws AppException, InformationalException, IOException, ParseException;

  public void createObligationRequest(
    final BDMDoJOutboundStageDtls payloadDtls, int processedInstrumentsCount)
    throws ParseException, AppException, InformationalException, IOException;

}
