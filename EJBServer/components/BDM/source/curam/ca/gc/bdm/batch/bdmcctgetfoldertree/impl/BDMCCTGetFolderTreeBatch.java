package curam.ca.gc.bdm.batch.bdmcctgetfoldertree.impl;

import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.BDMCCTOutboundInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetFolderTreeRequest;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.type.Date;

public abstract class BDMCCTGetFolderTreeBatch extends
  curam.ca.gc.bdm.batch.bdmcctgetfoldertree.base.BDMCCTGetFolderTreeBatch {

  /**
   * Execute batch process to retrieve templates from CCT and store them in Cúram
   * database.
   *
   * @param key - BDM Batch Process key
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void process() throws AppException, InformationalException {

    // Instantiate outbound interface then call getFolderTree
    final BDMCCTOutboundInterfaceImpl cctInterfaceObj =
      new BDMCCTOutboundInterfaceImpl();

    // set the Request
    final BDMCCTGetFolderTreeRequest request =
      new BDMCCTGetFolderTreeRequest();

    request
      .setUserID(Configuration.getProperty(EnvVars.BDM_CCT_API_USERID_VALUE));
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));
    // request.setUserID("test-bdm");
    // request.setCommunity("Pensions");
    request.setIncludeTemplatesInd(true);
    request.setIncludeTemplateFieldsInd(true);

    try {
      final boolean successInd =
        cctInterfaceObj.getAndSaveFolderTree(request, Date.getCurrentDate());

      if (successInd) {
        Trace.kTopLevelLogger
          .info("BDMCCTGetFolderTreeBatch has successfully completed.");
      } else {
        Trace.kTopLevelLogger.info("BDMCCTGetFolderTreeBatch has failed.");
      }

    } catch (final Exception ex) {
      Trace.kTopLevelLogger.error(ex);
    }
  }
}
