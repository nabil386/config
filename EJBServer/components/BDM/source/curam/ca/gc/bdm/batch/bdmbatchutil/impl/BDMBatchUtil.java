package curam.ca.gc.bdm.batch.bdmbatchutil.impl;

import curam.ca.gc.bdm.entity.fact.BDMBatchFilenameFactory;
import curam.ca.gc.bdm.entity.fact.BDMBatchHistoryFactory;
import curam.ca.gc.bdm.entity.intf.BDMBatchFilename;
import curam.ca.gc.bdm.entity.intf.BDMBatchHistory;
import curam.ca.gc.bdm.entity.struct.BDMBatchFileNameSchemaDtls;
import curam.ca.gc.bdm.entity.struct.BDMBatchFilenameDtls;
import curam.ca.gc.bdm.entity.struct.BDMBatchHistoryDtls;
import curam.core.fact.UniqueIDFactory;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.DateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BDMBatchUtil {

  /**
   * get the current schema name
   * @return String
   * @throws AppException
   * @throws InformationalException
   */
  public String getSchema() throws AppException, InformationalException {
    BDMBatchFileNameSchemaDtls schemaName = (BDMBatchFileNameSchemaDtls) DynamicDataAccess.executeNs(BDMBatchFileNameSchemaDtls.class, null, false, "SELECT sys_context('USERENV', 'CURRENT_SCHEMA') into :schemaName FROM dual");
    return schemaName.schemaName;
  }

  /**
   * insert batch filename record into the database
   * @param datetime
   * @param filename
   * @param instanceID
   * @throws AppException
   * @throws InformationalException
   */
  public void insertFilename(final DateTime datetime, final String filename,
    final String instanceID, long runID, String reqNumber) throws AppException, InformationalException {
    final BDMBatchFilename filenameObj = BDMBatchFilenameFactory.newInstance();
    final BDMBatchFilenameDtls filenameObjDetails = new BDMBatchFilenameDtls();
    filenameObjDetails.batchFilenameID = UniqueIDFactory.newInstance().getNextID();
    filenameObjDetails.createdOn = datetime;
    filenameObjDetails.filename = filename;
    filenameObjDetails.instanceID = instanceID;
    filenameObjDetails.runID = runID;
    filenameObjDetails.requisitionNumber = reqNumber;
    filenameObj.insert(filenameObjDetails);
  }
  
  
  public void insertBatchHistory(final DateTime datetime, final String instanceID, long runID) throws AppException, InformationalException {
    final BDMBatchHistory historyObj = BDMBatchHistoryFactory.newInstance();
    final BDMBatchHistoryDtls historyObjDetails = new BDMBatchHistoryDtls();
    historyObjDetails.batchHistoryID = UniqueIDFactory.newInstance().getNextID();
    historyObjDetails.createdOn = datetime;
    historyObjDetails.instanceID = instanceID;
    historyObjDetails.runID = runID;
    historyObj.insert(historyObjDetails);
  }
  
  /**
   * get current datetime in yyyyMMddHHmm format
   * @return String
   */
  public String getFormattedDateTime() {
    String pattern = "yyyyMMddHHmm";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    return formatter.format(LocalDateTime.now());
  }


}
