package curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.impl;

import curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.struct.BDMAdhocGenerateCorrespondenceBatchKey;
import curam.ca.gc.bdm.correspondenceframework.impl.BDMCorrespondenceMasterInput;
import curam.ca.gc.bdm.correspondenceframework.impl.BDMCorrespondenceRecipientClientInput;
import curam.ca.gc.bdm.correspondenceframework.impl.BDMCorrespondenceTriggerUtil;
import curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.FREQUENCYCODE;
import curam.core.impl.CuramConst;
import curam.util.dataaccess.CuramValueList;
import curam.util.dataaccess.DynamicDataAccess;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

/**
 * Batch class to trigger creation of staging records for ad hoc templates and
 * to be picked up by BDMGenerateCorrespondenceBatch
 */
public class BDMAdhocGenerateCorrespondenceBatch extends
  curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.base.BDMAdhocGenerateCorrespondenceBatch {

  /* Process to create the staged records */

  @Override
  public void process(final BDMAdhocGenerateCorrespondenceBatchKey key)
    throws AppException, InformationalException {

    if (StringUtils.isEmpty(key.instanceID)) {
      key.instanceID =
        BATCHPROCESSNAME.BDM_ADHOC_GENERATE_CORRESPONDENCE_BATCH;
    }
    if (key.processingDate.isZero()) {
      key.processingDate = Date.getCurrentDate();
    }

    if (StringUtils.isEmpty(key.frequency)) {
      key.frequency = FREQUENCYCODE.DAILY;
    }

    // If the inputFileNameWithPath is empty then just read the adhoc SQL from the
    // configuration
    if (key.inputFileNameWithPath.isEmpty()) {
      final BDMLetterTemplateConfig configDtls = BDMCorrespondenceTriggerUtil
        .getTemplateConfiguration(key.templateName);

      if (configDtls == null) {
        Trace.kTopLevelLogger.info(
          "The template " + key.templateName + " has not been configured.");
      } else if (StringUtil.isNullOrEmpty(configDtls.adhocSQL)) {
        Trace.kTopLevelLogger.info("The adhoc SQL should not be empty");
      } else {
        generateCorrespondence(configDtls.adhocSQL, key);
      }

    } else {

      // inputFileNameWithPath can be used to over ride the adhoc SQL that has been
      // configured.

      boolean sqlQuery = false;
      final ArrayList<String> inputLines = new ArrayList<>();
      try {
        final BufferedReader inputFileReader = new BufferedReader(
          new java.io.FileReader(key.inputFileNameWithPath));

        String lineFromInputFile;

        // Store the input file in memory
        int k = 0;

        while ((lineFromInputFile = inputFileReader.readLine()) != null) {
          inputLines.add(lineFromInputFile);
          if (k == 0
            && lineFromInputFile.toUpperCase().startsWith("SELECT")) {
            sqlQuery = true;
          }
          k++;
        }

        inputFileReader.close();
      } catch (final IOException e) {
        Trace.kTopLevelLogger.info(Trace.objectAsTraceString(e));
        throw new AppRuntimeException(e);
      }

      // form the query
      if (sqlQuery) {
        final StringBuilder query = new StringBuilder();
        for (int i = 0; i < inputLines.size(); i++) {
          if (i > 0)
            query.append(" ");

          query.append(inputLines.get(i));
        }

        generateCorrespondence(query.toString(), key);

      } else {
        Trace.kTopLevelLogger.info("The input SQL file is not valid");
      }
    }
  }

  /**
   * The query parameter will hold the sql that will fetch the list of clients and
   * other details for which the correspondence has to be generated.
   *
   * @param query
   * @param key
   * @throws AppException
   * @throws InformationalException
   */
  private void generateCorrespondence(final String query,
    final BDMAdhocGenerateCorrespondenceBatchKey key)
    throws AppException, InformationalException {

    // Execute the query
    final CuramValueList<BDMCorrespondenceMasterInput> curamValueList =
      DynamicDataAccess.executeNsMulti(BDMCorrespondenceMasterInput.class,
        null, false, true, query.toString());

    for (final BDMCorrespondenceMasterInput correspondenceMasterInput : curamValueList) {

      final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput =
        new BDMCorrespondenceRecipientClientInput();
      correspondenceRecipientClientInput.clientConcernRoleID =
        correspondenceMasterInput.clientConcernRoleID;
      correspondenceRecipientClientInput.toRecipientConcernRoleID =
        correspondenceMasterInput.toRecipientConcernRoleID;
      // BUG-122552, Start
      // Populate caseID in to input struct
      if (CuramConst.gkZero != correspondenceMasterInput.caseID) {
        correspondenceRecipientClientInput.caseID =
          correspondenceMasterInput.caseID;
      }
      // BUG-122552, End

      final BDMCorrespondenceTriggerUtil correspondenceTriggerUtil =
        new BDMCorrespondenceTriggerUtil();
      correspondenceTriggerUtil.generateCorrespondenceForBatch(
        key.templateName, key.frequency, correspondenceRecipientClientInput,
        correspondenceMasterInput);
    }

  }
}
