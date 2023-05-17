package curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.impl;

import curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.fact.BDMAdhocGenerateCorrespondenceBatchFactory;
import curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.intf.BDMAdhocGenerateCorrespondenceBatch;
import curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.struct.BDMAdhocGenerateCorrespondenceBatchKey;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.correspondenceframework.impl.BDMCorrespondenceTriggerUtilTest;
import curam.ca.gc.bdm.entity.communication.fact.BDMCorrespondenceStagingFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMCorrespondenceStaging;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingDtlsList;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingStatusAndTemplateName;
import curam.ca.gc.bdm.sl.communication.impl.BDMCommunicationTest;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.CORRESPONDENCEJOBSTATUS;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.io.IOException;
import java.net.URL;
import org.junit.Test;

public class BDMAdhocGenerateCorrespondenceBatchTest extends BDMBaseTest {

  @Override
  public boolean shouldCommit() {

    return false;
  }

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();
  }

  @Test
  public void testInvokeBDMAdhocGenerateCorrespondenceBatch_Success()
    throws AppException, InformationalException, IOException {

    // Set up the config and template
    new BDMCorrespondenceTriggerUtilTest()
      .deleteAppresource("ISS-6587_CONFIG");
    new BDMCorrespondenceTriggerUtilTest()
      .deleteAppresource("ISS-6587_TEMPLATE");
    new BDMCorrespondenceTriggerUtilTest()
      .insertApprersouce("ISS-6587_CONFIG", "ISS-6587_CONFIG.JSON");
    new BDMCorrespondenceTriggerUtilTest()
      .insertApprersouce("ISS-6587_TEMPLATE", "ISS-6587_TEMPLATE.XML");

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    new BDMCorrespondenceTriggerUtilTest().createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);

    final BDMAdhocGenerateCorrespondenceBatch adhocCorrespondenceBatch =
      BDMAdhocGenerateCorrespondenceBatchFactory.newInstance();
    final BDMAdhocGenerateCorrespondenceBatchKey key =
      new BDMAdhocGenerateCorrespondenceBatchKey();
    final URL url =
      getClass().getResource("AdhocCorrespondencBatchSampleISS6587Input.sql");
    key.inputFileNameWithPath = url.getPath();
    // "\\AdhocCorrespondencBatchSampleISS6587Input.sql";
    key.templateName = "ISS-6587";
    adhocCorrespondenceBatch.process(key);

    // Assert if staging table has the details for that template name in
    // PENDING status

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName stagingStatusAndTemplateName =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    stagingStatusAndTemplateName.templateName = "ISS-6587";
    stagingStatusAndTemplateName.status = CORRESPONDENCEJOBSTATUS.PENDING;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj
        .searchByTemplateAndStatus(stagingStatusAndTemplateName);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() > 0;

  }

  @Test
  public void
    testInvokeBDMAdhocGenerateCorrespondenceBatchWithConfiguredSQL_Success()
      throws AppException, InformationalException, IOException {

    // Set up the config and template
    new BDMCorrespondenceTriggerUtilTest()
      .deleteAppresource("ISS-6587_CONFIG");
    new BDMCorrespondenceTriggerUtilTest()
      .deleteAppresource("ISS-6587_TEMPLATE");
    new BDMCorrespondenceTriggerUtilTest()
      .insertApprersouce("ISS-6587_CONFIG", "ISS-6587_CONFIG.JSON");
    new BDMCorrespondenceTriggerUtilTest()
      .insertApprersouce("ISS-6587_TEMPLATE", "ISS-6587_TEMPLATE.XML");

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    new BDMCorrespondenceTriggerUtilTest().createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);

    final BDMAdhocGenerateCorrespondenceBatch adhocCorrespondenceBatch =
      BDMAdhocGenerateCorrespondenceBatchFactory.newInstance();
    final BDMAdhocGenerateCorrespondenceBatchKey key =
      new BDMAdhocGenerateCorrespondenceBatchKey();
    key.inputFileNameWithPath = "";
    key.templateName = "ISS-6587";
    adhocCorrespondenceBatch.process(key);

    // Assert if staging table has the details for that template name in
    // PENDING status

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName stagingStatusAndTemplateName =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    stagingStatusAndTemplateName.templateName = "ISS-6587";
    stagingStatusAndTemplateName.status = CORRESPONDENCEJOBSTATUS.PENDING;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj
        .searchByTemplateAndStatus(stagingStatusAndTemplateName);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() > 0;

  }

  @Test
  public void
    testInvokeBDMAdhocGenerateCorrespondenceBatchWithConfiguredEmptySQL_Success()
      throws AppException, InformationalException, IOException {

    // Set up the config and template
    new BDMCorrespondenceTriggerUtilTest()
      .deleteAppresource("ISS-SAMPLE_NO_ADHOC_SQL_CONFIG");
    new BDMCorrespondenceTriggerUtilTest()
      .deleteAppresource("ISS-SAMPLE_NO_ADHOC_SQL_TEMPLATE");
    new BDMCorrespondenceTriggerUtilTest().insertApprersouce(
      "ISS-SAMPLE_NO_ADHOC_SQL_CONFIG",
      "ISS-SAMPLE_NO_ADHOC_SQL_CONFIG.JSON");
    new BDMCorrespondenceTriggerUtilTest().insertApprersouce(
      "ISS-SAMPLE_NO_ADHOC_SQL_TEMPLATE",
      "ISS-SAMPLE_NO_ADHOC_SQL_TEMPLATE.XML");

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    new BDMCorrespondenceTriggerUtilTest().createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);

    final BDMAdhocGenerateCorrespondenceBatch adhocCorrespondenceBatch =
      BDMAdhocGenerateCorrespondenceBatchFactory.newInstance();
    final BDMAdhocGenerateCorrespondenceBatchKey key =
      new BDMAdhocGenerateCorrespondenceBatchKey();
    key.inputFileNameWithPath = "";
    key.templateName = "ISS-SAMPLE_NO_ADHOC_SQL";
    adhocCorrespondenceBatch.process(key);

    // Assert if staging table has the details for that template name in
    // PENDING status

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName stagingStatusAndTemplateName =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    stagingStatusAndTemplateName.templateName = "ISS-SAMPLE_NO_ADHOC_SQL";
    stagingStatusAndTemplateName.status = CORRESPONDENCEJOBSTATUS.PENDING;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj
        .searchByTemplateAndStatus(stagingStatusAndTemplateName);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() == 0;

  }

  @Test
  public void
    testInvokeBDMAdhocGenerateCorrespondenceBatchWithNOConfiguration()
      throws AppException, InformationalException, IOException {

    // Set up the config and template
    new BDMCorrespondenceTriggerUtilTest()
      .deleteAppresource("ISS-SAMPLE_NO_ADHOC_SQL_CONFIG");
    new BDMCorrespondenceTriggerUtilTest()
      .deleteAppresource("ISS-SAMPLE_NO_ADHOC_SQL_TEMPLATE");

    final BDMAdhocGenerateCorrespondenceBatch adhocCorrespondenceBatch =
      BDMAdhocGenerateCorrespondenceBatchFactory.newInstance();
    final BDMAdhocGenerateCorrespondenceBatchKey key =
      new BDMAdhocGenerateCorrespondenceBatchKey();
    key.inputFileNameWithPath = "";
    key.templateName = "ISS-SAMPLE_NO_ADHOC_SQL";
    adhocCorrespondenceBatch.process(key);

    // Assert

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName stagingStatusAndTemplateName =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    stagingStatusAndTemplateName.templateName = "ISS-SAMPLE_NO_ADHOC_SQL";
    stagingStatusAndTemplateName.status = CORRESPONDENCEJOBSTATUS.PENDING;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj
        .searchByTemplateAndStatus(stagingStatusAndTemplateName);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() == 0;

  }

  @Test
  public void
    testInvokeBDMAdhocGenerateCorrespondenceBatch_WithMissingMandatoryInfo()
      throws AppException, InformationalException, IOException {

    // Set up the config and template
    new BDMCorrespondenceTriggerUtilTest()
      .deleteAppresource("ISS-6587_CONFIG");
    new BDMCorrespondenceTriggerUtilTest()
      .deleteAppresource("ISS-6587_TEMPLATE");
    new BDMCorrespondenceTriggerUtilTest()
      .insertApprersouce("ISS-6587_CONFIG", "ISS-6587_CONFIG.JSON");
    new BDMCorrespondenceTriggerUtilTest()
      .insertApprersouce("ISS-6587_TEMPLATE", "ISS-6587_TEMPLATE.XML");

    PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    // Second person
    personDtls = new BDMCommunicationTest().createPDCPerson();

    final BDMAdhocGenerateCorrespondenceBatch adhocCorrespondenceBatch =
      BDMAdhocGenerateCorrespondenceBatchFactory.newInstance();
    final BDMAdhocGenerateCorrespondenceBatchKey key =
      new BDMAdhocGenerateCorrespondenceBatchKey();
    final URL url =
      getClass().getResource("AdhocCorrespondencBatchSampleISS6587Input.sql");
    key.inputFileNameWithPath = url.getPath();
    /*
     * key.inputFileNameWithPath =
     * "C:\\dev\\repositories\\dev-v2\\bdc-curam-foundations\\EJBServer\\components\\BDM\\tests\\curam\\ca\\gc\\bdm\\batch\\correspondenceframework\\generatecorrespondence\\impl\\AdhocCorrespondencBatchSampleISS6587Input.sql";
     */
    key.templateName = "ISS-6587";
    adhocCorrespondenceBatch.process(key);

    // Assert if staging table has the details for that template name in
    // MISSINGMANDATORYINFO status

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName stagingStatusAndTemplateName =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    stagingStatusAndTemplateName.templateName = "ISS-6587";
    stagingStatusAndTemplateName.status =
      CORRESPONDENCEJOBSTATUS.MISSINGMANDATORYINFO;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj
        .searchByTemplateAndStatus(stagingStatusAndTemplateName);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() > 0;

  }

  @Test
  public void testInvokeBDMAdhocGenerateCorrespondenceBatch_ISS6588()
    throws AppException, InformationalException, IOException {

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    // createFA(
    // createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
    // BDMFOREIGNAPPTYPE.SURVIVORS);
    //
    // // Second person
    // personDtls = new BDMCommunicationTest().createPDCPerson();
    //
    // createFA(
    // createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
    // BDMFOREIGNAPPTYPE.SURVIVORS);

    final BDMAdhocGenerateCorrespondenceBatch adhocCorrespondenceBatch =
      BDMAdhocGenerateCorrespondenceBatchFactory.newInstance();
    final BDMAdhocGenerateCorrespondenceBatchKey key =
      new BDMAdhocGenerateCorrespondenceBatchKey();
    final URL url =
      getClass().getResource("AdhocCorrespondencBatchISS6588Input.sql");
    key.inputFileNameWithPath = url.getPath();
    /*
     * key.inputFileNameWithPath =
     * "C:\\dev\\repositories\\dev-v2\\bdc-curam-foundations\\EJBServer\\components\\BDM\\tests\\curam\\ca\\gc\\bdm\\sl\\communication\\impl\\AdhocCorrespondencBatchISS6588Input.sql";
     */
    // key.frequency = FREQUENCYCODE.DAILY;
    key.templateName = "ISS-6588";
    adhocCorrespondenceBatch.process(key);

  }

  @Test
  public void testbatchProcess() throws AppException, InformationalException {

    final curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.intf.BDMGenerateCorrespondenceBatch batchStreamObj =
      curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.fact.BDMGenerateCorrespondenceBatchFactory
        .newInstance();

    final curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.struct.BDMGenerateCorrespondenceBatchKey batchChunkKey =
      new curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.struct.BDMGenerateCorrespondenceBatchKey();
    batchChunkKey.instanceID =
      BATCHPROCESSNAME.BDM_GENERATE_CORRESPONDENCE_BATCH;
    batchStreamObj.process(batchChunkKey);
  }

}
