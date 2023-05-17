package curam.ca.gc.bdm.correspondenceframework.impl;

import curam.bdm.test.junit4.MockLogin;
import curam.ca.gc.bdm.codetable.BDMCCTSUBMITOPT;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.entity.communication.fact.BDMCorrespondenceStagingFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMCorrespondenceStaging;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingDtlsList;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingStatusAndTemplateName;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceWizardKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateDetails;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMCreateModifyFA;
import curam.ca.gc.bdm.message.BDMCORRESPONDENCEFRAMEWORK;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.BenefitApplicationType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.ClientType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.CodeType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.CorrespondenceContentType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.CorrespondenceType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.CountryType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.IdentificationType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.ObjectFactory;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.TextType;
import curam.ca.gc.bdm.sl.communication.impl.BDMCommunicationTest;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.codetable.BATCHPROCESSNAME;
import curam.codetable.CORRESPONDENCEJOBSTATUS;
import curam.codetable.FREQUENCYCODE;
import curam.core.facade.struct.WizardStateID;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.EnvVars;
import curam.core.struct.ConcernRoleKey;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.internal.resources.fact.AppResourceFactory;
import curam.util.internal.resources.struct.AppResourceDtls;
import curam.util.internal.resources.struct.ResourceName;
import curam.util.resources.Configuration;
import curam.util.type.Blob;
import curam.util.type.Date;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test
 */
public class BDMCorrespondenceTriggerUtilTest extends BDMBaseTest {

  final curam.ca.gc.bdm.sl.communication.intf.BDMCommunication bdmCommunicationObj =
    curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory
      .newInstance();

  public BDMCorrespondenceTriggerUtilTest() throws RemoteException {

    super();
    MockLogin.getInst().login("caseworker");
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  /**
   * Upload a sample config and template
   *
   * @throws Exception
   */
  @Test
  public void testInsertApprersouce() throws Exception {

    deleteAppresource("ISP-SAMPLE_WITH_ADHOC_SQL_CONFIG");
    deleteAppresource("ISP-SAMPLE_WITH_ADHOC_SQL_TEMPLATE");
    insertApprersouce("ISP-SAMPLE_WITH_ADHOC_SQL_CONFIG",
      "ISP-SAMPLE_WITH_ADHOC_SQL_CONFIG.JSON");
    insertApprersouce("ISP-SAMPLE_WITH_ADHOC_SQL_TEMPLATE",
      "ISP-SAMPLE_WITH_ADHOC_SQL_TEMPLATE.XML");

  }

  /**
   * Test to create a person
   *
   * @throws Exception
   */
  @Test
  public void testCreatePerson() throws Exception {

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = personDtls.concernRoleID;

    System.out.println("The created person reference is: "
      + ConcernRoleFactory.newInstance().read(key).primaryAlternateID);
  }

  /**
   * Test to create a person with Application
   *
   * @throws Exception
   */
  @Test
  public void testCreatePersonWithApplication() throws Exception {

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    final ConcernRoleKey key = new ConcernRoleKey();
    key.concernRoleID = personDtls.concernRoleID;

    createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);

    System.out.println("The created person reference is: "
      + ConcernRoleFactory.newInstance().read(key).primaryAlternateID);
  }

  /**
   * Upload a sample config and template and test generateCorrespondenceForBatch
   * if it creates the staging record.
   *
   * @throws Exception
   */
  @Test
  public void testGenerateCorrespondenceForBatchSample_ISP3175B()
    throws Exception {

    deleteAppresource("ISP-3175B_CONFIG");
    deleteAppresource("ISP-3175B_TEMPLATE");
    insertApprersouce("ISP-3175B_CONFIG", "ISP-3175B_CONFIG.JSON");
    insertApprersouce("ISP-3175B_TEMPLATE", "ISP-3175B_TEMPLATE.XML");

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();
    // Create the application
    createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);

    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput =
      new BDMCorrespondenceRecipientClientInput();
    correspondenceRecipientClientInput.clientConcernRoleID =
      personDtls.concernRoleID;

    correspondenceRecipientClientInput.toRecipientConcernRoleID =
      personDtls.concernRoleID;

    final BDMCorrespondenceMasterInput bdmCorrespondenceMasterInput =
      new BDMCorrespondenceMasterInput();
    bdmCorrespondenceMasterInput.concernRoleID = personDtls.concernRoleID;
    final BDMCorrespondenceTriggerUtil correspondenceTriggerUtil =
      new BDMCorrespondenceTriggerUtil();
    correspondenceTriggerUtil.generateCorrespondenceForBatch("ISP-3175B",
      FREQUENCYCODE.DAILY, correspondenceRecipientClientInput,
      bdmCorrespondenceMasterInput);

    // Assert if staging table has the details for that template name in pending
    // status

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName key =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    key.templateName = "ISP-3175B";
    key.status = CORRESPONDENCEJOBSTATUS.PENDING;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj.searchByTemplateAndStatus(key);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() > 0;

  }

  /**
   * Upload a sample config and template and test if createCorrespondence(which is
   * called from wizard)
   * succeeds.
   *
   * @throws Exception
   */
  @Test
  public void testCreateCorrespondenceSample_Realtime() throws Exception {

    deleteAppresource("ISS-SAMPLE_CONFIG");
    deleteAppresource("ISS-SAMPLE_TEMPLATE");
    insertApprersouce("ISS-SAMPLE_CONFIG", "ISS-SAMPLE_CONFIG.JSON");
    insertApprersouce("ISS-SAMPLE_TEMPLATE", "ISS-SAMPLE_TEMPLATE.XML");

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 882346;
    corrDetails.templateName = "ISS-SAMPLE";
    corrDetails.templatePath =
      "/Foreign-benefits_Prestations-étrangères/Application-request_Demande_d'application";

    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    final BDMCorrespondenceWizardKey wizardKey =
      new BDMCorrespondenceWizardKey();
    wizardKey.wizardStateID = wizId.wizardStateID;

    wizardKey.submitOpt = BDMCCTSUBMITOPT.DEFAULTCODE;

    final BDMTemplateDetails bdmTemplateDetails =
      bdmCommunicationObj.createCorrespondence(wizardKey);
    assertTrue(bdmTemplateDetails.workItemID != 0);
  }

  @Test
  /**
   * Upload a sample config and template and test generateCorrespondenceForBatch
   * if it creates the staging record.
   *
   * @throws Exception
   */
  public void testGenerateCorrespondenceForBatchSample() throws Exception {

    deleteAppresource("ISS-SAMPLE_CONFIG");
    deleteAppresource("ISS-SAMPLE_TEMPLATE");
    insertApprersouce("ISS-SAMPLE_CONFIG", "ISS-SAMPLE_CONFIG.JSON");
    insertApprersouce("ISS-SAMPLE_TEMPLATE", "ISS-SAMPLE_TEMPLATE.XML");

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();
    // Create the application
    createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);

    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput =
      new BDMCorrespondenceRecipientClientInput();
    correspondenceRecipientClientInput.clientConcernRoleID =
      personDtls.concernRoleID;

    correspondenceRecipientClientInput.toRecipientConcernRoleID =
      personDtls.concernRoleID;

    final BDMCorrespondenceMasterInput bdmCorrespondenceMasterInput =
      new BDMCorrespondenceMasterInput();
    bdmCorrespondenceMasterInput.concernRoleID = personDtls.concernRoleID;
    final BDMCorrespondenceTriggerUtil correspondenceTriggerUtil =
      new BDMCorrespondenceTriggerUtil();
    correspondenceTriggerUtil.generateCorrespondenceForBatch("ISS-SAMPLE",
      FREQUENCYCODE.DAILY, correspondenceRecipientClientInput,
      bdmCorrespondenceMasterInput);

    // Assert if staging table has the details for that template name in pending
    // status

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName key =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    key.templateName = "ISS-SAMPLE";
    key.status = CORRESPONDENCEJOBSTATUS.PENDING;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj.searchByTemplateAndStatus(key);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() > 0;

  }

  @Test
  /**
   * Upload a sample config and template and test generateCorrespondenceForBatch
   * if it creates the staging record.
   *
   * @throws Exception
   */
  public void testGenerateCorrespondenceForBatchSample1() throws Exception {

    deleteAppresource("ISS-SAMPLE1_CONFIG");
    deleteAppresource("ISS-SAMPLE1_TEMPLATE");
    insertApprersouce("ISS-SAMPLE1_CONFIG", "ISS-SAMPLE1_CONFIG.JSON");
    insertApprersouce("ISS-SAMPLE1_TEMPLATE", "ISS-SAMPLE1_TEMPLATE.XML");

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();
    // Create the application
    createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);

    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput =
      new BDMCorrespondenceRecipientClientInput();
    correspondenceRecipientClientInput.clientConcernRoleID =
      personDtls.concernRoleID;

    correspondenceRecipientClientInput.toRecipientConcernRoleID =
      personDtls.concernRoleID;

    final BDMCorrespondenceMasterInput bdmCorrespondenceMasterInput =
      new BDMCorrespondenceMasterInput();
    bdmCorrespondenceMasterInput.concernRoleID = personDtls.concernRoleID;
    final BDMCorrespondenceTriggerUtil correspondenceTriggerUtil =
      new BDMCorrespondenceTriggerUtil();
    correspondenceTriggerUtil.generateCorrespondenceForBatch("ISS-SAMPLE1",
      FREQUENCYCODE.DAILY, correspondenceRecipientClientInput,
      bdmCorrespondenceMasterInput);

    // Assert if staging table has the details for that template name in pending
    // status

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName key =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    key.templateName = "ISS-SAMPLE1";
    key.status = CORRESPONDENCEJOBSTATUS.PENDING;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj.searchByTemplateAndStatus(key);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() > 0;

  }

  @Test
  /**
   * Upload a sample config and template and test generateCorrespondenceForBatch
   * if it creates the staging record.
   *
   * @throws Exception
   */
  public void testGenerateCorrespondenceForBatchSample2() throws Exception {

    deleteAppresource("ISS-SAMPLE2_CONFIG");
    deleteAppresource("ISS-SAMPLE2_TEMPLATE");
    insertApprersouce("ISS-SAMPLE2_CONFIG", "ISS-SAMPLE2_CONFIG.JSON");
    insertApprersouce("ISS-SAMPLE2_TEMPLATE", "ISS-SAMPLE2_TEMPLATE.XML");

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();
    // Create the application
    createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);

    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput =
      new BDMCorrespondenceRecipientClientInput();
    correspondenceRecipientClientInput.clientConcernRoleID =
      personDtls.concernRoleID;

    correspondenceRecipientClientInput.toRecipientConcernRoleID =
      personDtls.concernRoleID;

    final BDMCorrespondenceMasterInput bdmCorrespondenceMasterInput =
      new BDMCorrespondenceMasterInput();
    bdmCorrespondenceMasterInput.concernRoleID = personDtls.concernRoleID;
    final BDMCorrespondenceTriggerUtil correspondenceTriggerUtil =
      new BDMCorrespondenceTriggerUtil();
    correspondenceTriggerUtil.generateCorrespondenceForBatch("ISS-SAMPLE2",
      FREQUENCYCODE.DAILY, correspondenceRecipientClientInput,
      bdmCorrespondenceMasterInput);

    // Assert if staging table has the details for that template name in pending
    // status

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName key =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    key.templateName = "ISS-SAMPLE2";
    key.status = CORRESPONDENCEJOBSTATUS.MISSINGMANDATORYINFO;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj.searchByTemplateAndStatus(key);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() > 0;

  }

  /**
   * Test with the Appresource which is already present in DB
   *
   * @throws Exception
   */
  @Test
  public void testCreateCorrespondence_realtime() throws Exception {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 882346;
    corrDetails.templateName = "ISS-6588";
    corrDetails.templatePath =
      "/Foreign-benefits_Prestations-étrangères/Application-request_Demande_d'application";

    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    final BDMCorrespondenceWizardKey wizardKey =
      new BDMCorrespondenceWizardKey();
    wizardKey.wizardStateID = wizId.wizardStateID;

    wizardKey.submitOpt = BDMCCTSUBMITOPT.DEFAULTCODE;

    final BDMTemplateDetails bdmTemplateDetails =
      bdmCommunicationObj.createCorrespondence(wizardKey);
    assertTrue(bdmTemplateDetails.workItemID != 0);
  }

  @Test
  /**
   * Test generateCorrespondenceForBatch
   *
   * @throws Exception
   */
  public void testGenerateCorrespondenceForBatch() throws Exception {

    // final PersonRegistrationResult person = registerPerson();

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput =
      new BDMCorrespondenceRecipientClientInput();
    correspondenceRecipientClientInput.clientConcernRoleID =
      personDtls.concernRoleID;

    correspondenceRecipientClientInput.toRecipientConcernRoleID =
      personDtls.concernRoleID;

    final BDMCorrespondenceMasterInput bdmCorrespondenceMasterInput =
      new BDMCorrespondenceMasterInput();
    bdmCorrespondenceMasterInput.concernRoleID = personDtls.concernRoleID;

    final BDMCorrespondenceTriggerUtil correspondenceTriggerUtil =
      new BDMCorrespondenceTriggerUtil();
    correspondenceTriggerUtil.generateCorrespondenceForBatch("ISS-6588",
      FREQUENCYCODE.DAILY, correspondenceRecipientClientInput,
      bdmCorrespondenceMasterInput);

    // Assert if staging table has the details for that template name in pending
    // status

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName key =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    key.templateName = "ISS-6588";
    key.status = CORRESPONDENCEJOBSTATUS.PENDING;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj.searchByTemplateAndStatus(key);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() > 0;
  }

  @Test
  /**
   * Set up sample and test if mandatory fields missing validation is thrown.
   *
   * @throws Exception
   */
  public void testGenerateCorrespondenceForRealTime_FailureWithMandatoryInfo()
    throws Exception {

    // Set up the config and template
    deleteAppresource("ISS-6587_CONFIG");
    deleteAppresource("ISS-6587_TEMPLATE");
    insertApprersouce("ISS-6587_CONFIG", "ISS-6587_CONFIG.JSON");
    insertApprersouce("ISS-6587_TEMPLATE", "ISS-6587_TEMPLATE.XML");

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    // corrDetails.templateID = 882346;
    corrDetails.templateName = "ISS-6587";
    // corrDetails.templatePath =
    // "/Foreign-benefits_Prestations-étrangères/Application-request_Demande_d'application";

    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    final BDMCorrespondenceWizardKey wizardKey =
      new BDMCorrespondenceWizardKey();
    wizardKey.wizardStateID = wizId.wizardStateID;

    wizardKey.submitOpt = BDMCCTSUBMITOPT.DEFAULTCODE;

    // final BDMTemplateDetails bdmTemplateDetails =
    // bdmCommunicationObj.createCorrespondence(wizardKey);
    // get the correspondence details from the wizard state
    final WizardPersistentState wizardObj = new WizardPersistentState();

    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizardKey.wizardStateID);

    final BDMCorrespondenceMasterInput inputData =
      new BDMCorrespondenceMasterInput();
    inputData.concernRoleID = corresDetails.concernRoleID;

    // Set up correspondence details and test
    final AppException ae = new AppException(
      BDMCORRESPONDENCEFRAMEWORK.ERR_MANDATORY_FIELDS_MISSING);
    ae.arg("[Program Code, Application Received Date]");
    boolean isExceptionAsserted = false;
    try {
      BDMCorrespondenceTriggerUtil.generateCorrespondenceForRealTime(
        corresDetails.templateName, corresDetails, inputData);
    } catch (final Exception e) {

      assertEquals(ae.getLocalizedMessage().toString(),
        e.getLocalizedMessage().toString());
      isExceptionAsserted = true;
    }

    assertTrue(isExceptionAsserted);
  }

  @Test
  /**
   * This test is with calling the framework main method directly.
   *
   * @throws Exception
   */
  public void testGenerateCorrespondenceForRealTime_Success()
    throws Exception {

    // Set up the config and template
    deleteAppresource("ISS-6587_CONFIG");
    deleteAppresource("ISS-6587_TEMPLATE");
    insertApprersouce("ISS-6587_CONFIG", "ISS-6587_CONFIG.JSON");
    insertApprersouce("ISS-6587_TEMPLATE", "ISS-6587_TEMPLATE.XML");

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();

    createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);

    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 862191;
    corrDetails.templateName = "ISS-6587";
    corrDetails.templatePath =
      "/Foreign-benefits_Prestations-étrangères/Application-request_Demande_d'application";

    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    final BDMCorrespondenceWizardKey wizardKey =
      new BDMCorrespondenceWizardKey();
    wizardKey.wizardStateID = wizId.wizardStateID;

    wizardKey.submitOpt = BDMCCTSUBMITOPT.DEFAULTCODE;

    // get the correspondence details from the wizard state
    final WizardPersistentState wizardObj = new WizardPersistentState();

    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizardKey.wizardStateID);

    final BDMCorrespondenceMasterInput inputData =
      new BDMCorrespondenceMasterInput();
    inputData.concernRoleID = corresDetails.concernRoleID;

    // Set up correspondence details and test

    final BDMTemplateDetails bdmTemplateDetails =
      BDMCorrespondenceTriggerUtil.generateCorrespondenceForRealTime(
        corresDetails.templateName, corresDetails, inputData);
    assertTrue(
      bdmTemplateDetails != null && bdmTemplateDetails.workItemID != 0);
  }

  @Test
  public void testInvokeBDMGenerateCorrespondenceBatch()
    throws AppException, InformationalException {

    Configuration.setProperty(
      EnvVars.BDM_GENERATE_CORRESPONDENCE_BATCH_DONT_RUN_STREAM, "false");

    final curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.intf.BDMGenerateCorrespondenceBatch batchStreamObj =
      curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.fact.BDMGenerateCorrespondenceBatchFactory
        .newInstance();

    final curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.struct.BDMGenerateCorrespondenceBatchKey batchChunkKey =
      new curam.ca.gc.bdm.batch.correspondenceframework.generatecorrespondence.struct.BDMGenerateCorrespondenceBatchKey();
    batchChunkKey.instanceID =
      BATCHPROCESSNAME.BDM_GENERATE_CORRESPONDENCE_BATCH;
    batchStreamObj.process(batchChunkKey);

  }

  // @Test
  // public void testModifyStatusByJobID() throws Exception {
  //
  // testGenerateCorrespondenceForBatch();
  // testInvokeBDMGenerateCorrespondenceBatch();
  //
  // final BDMCorrespondenceStaging correspondenceStagingObj =
  // BDMCorrespondenceStagingFactory.newInstance();
  //
  // final BDMConcernRoleCommunicationSub concernRoleCommunicationSub =
  // BDMConcernRoleCommunicationSubFactory.newInstance();
  //
  // final BDMConcernRoleCommunication bdmConcernRoleCommunication =
  // BDMConcernRoleCommunicationFactory.newInstance();
  //
  // final BDMCorrespondenceStagingFrequencyAndStatus frequencyAndStatus =
  // new BDMCorrespondenceStagingFrequencyAndStatus();
  // frequencyAndStatus.status = CORRESPONDENCEJOBSTATUS.SUBMITTED;
  // frequencyAndStatus.frequency = FREQUENCYCODE.DAILY;
  // final BDMCorrespondenceStagingDtlsList stagingDtlsList =
  // correspondenceStagingObj.searchByFrequencyAndStatus(frequencyAndStatus);
  //
  // if (stagingDtlsList.dtls.size() > 0) {
  // final BDMCorrespondenceStagingDtls stagingDtls =
  // stagingDtlsList.dtls.get(0);
  //
  // final java.lang.String originalStatus = stagingDtls.status;
  //
  // final BDMCorrespondenceStagingJobID key =
  // new BDMCorrespondenceStagingJobID();
  // final BDMCorrespondenceStagingStatus dtls =
  // new BDMCorrespondenceStagingStatus();
  // key.jobID = stagingDtls.jobID;
  // dtls.status = CORRESPONDENCEJOBSTATUS.PRINTED;
  // correspondenceStagingObj.modifyStatusByJobID(key, dtls);
  //
  // final BDMCorrespondenceStatusJobID statusAndJobID =
  // new BDMCorrespondenceStatusJobID();
  // statusAndJobID.jobID = stagingDtls.jobID;
  // statusAndJobID.status = COMMUNICATIONSTATUS.SENT;
  //
  // concernRoleCommunicationSub
  // .modifyCommunicationStatusByJobID(statusAndJobID);
  //
  // statusAndJobID.jobID = stagingDtls.jobID;
  // statusAndJobID.status = BDMCCTCOMMUNICATIONSTATUS.FINISHED;
  //
  // bdmConcernRoleCommunication.modifyCCTStatusByJobID(statusAndJobID);
  //
  // // Assert
  //
  // final BDMCorrespondenceStagingKey stagingKey =
  // new BDMCorrespondenceStagingKey();
  // stagingKey.correspondenceStagingID =
  // stagingDtls.correspondenceStagingID;
  // final BDMCorrespondenceStagingDtls bdmCorrespondenceStagingDtls =
  // correspondenceStagingObj.read(stagingKey);
  // assertEquals(CORRESPONDENCEJOBSTATUS.PRINTED,
  // bdmCorrespondenceStagingDtls.status);
  // assert !originalStatus
  // .equalsIgnoreCase(bdmCorrespondenceStagingDtls.status);
  //
  // final ConcernRoleCommunicationKey concernroleCommunicationKey =
  // new ConcernRoleCommunicationKey();
  // concernroleCommunicationKey.communicationID =
  // bdmCorrespondenceStagingDtls.communicationID;
  //
  // final ConcernRoleCommunicationDtls communicationDtls =
  // concernRoleCommunicationSub.read(concernroleCommunicationKey);
  // assertEquals(COMMUNICATIONSTATUS.SENT,
  // communicationDtls.communicationStatus);
  //
  // }
  // }

  @Test
  public void
    testReTriggerGenerateCorrespondenceForMissingMandatoryInfoRecords()
      throws Exception {

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName key =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    key.templateName = "ISS-6588";
    key.status = CORRESPONDENCEJOBSTATUS.MISSINGMANDATORYINFO;

    BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj.searchByTemplateAndStatus(key);

    for (int i = 0; i < bdmoasCorrespondenceStagingDtlsList.dtls
      .size(); i++) {
      final BDMCorrespondenceTriggerUtil correspondenceTriggerUtil =
        new BDMCorrespondenceTriggerUtil();
      correspondenceTriggerUtil.reTriggerGenerateCorrespondence(
        bdmoasCorrespondenceStagingDtlsList.dtls
          .get(i).correspondenceStagingID);
    }

    // Assert
    key.templateName = "ISS-6588";
    key.status = CORRESPONDENCEJOBSTATUS.REPROCESS;

    bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj.searchByTemplateAndStatus(key);

    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() == 0;
  }

  @Test
  public void testReTriggerGenerateCorrespondenceForPendingRecords()
    throws Exception {

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName key =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    key.templateName = "ISS-6588";
    key.status = CORRESPONDENCEJOBSTATUS.PENDING;

    BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj.searchByTemplateAndStatus(key);

    for (int i = 0; i < bdmoasCorrespondenceStagingDtlsList.dtls
      .size(); i++) {
      final BDMCorrespondenceTriggerUtil correspondenceTriggerUtil =
        new BDMCorrespondenceTriggerUtil();
      correspondenceTriggerUtil.reTriggerGenerateCorrespondence(
        bdmoasCorrespondenceStagingDtlsList.dtls
          .get(i).correspondenceStagingID);
    }

    // Assert
    key.templateName = "ISS-6588";
    key.status = CORRESPONDENCEJOBSTATUS.FAILED;

    bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj.searchByTemplateAndStatus(key);

    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() == 0;
  }

  @Test
  public void testGenerateTemplateXML()
    throws AppException, InformationalException, JAXBException {

    // root element
    final CorrespondenceType correspondenceType =
      new ObjectFactory().createCorrespondenceType();

    final CorrespondenceContentType documentContent =
      new CorrespondenceContentType();

    // Client
    final ClientType client = new ClientType();

    final IdentificationType clientIdentification1 = new IdentificationType();
    final curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String identificationID1 =
      new String();
    identificationID1.setValue("@@identificationID");
    clientIdentification1.getIdentificationID().add(identificationID1);
    final TextType identificationCategoryText1 = new TextType();
    identificationCategoryText1.setValue("@@identificationCategoryText");
    clientIdentification1.getIdentificationCategoryText()
      .add(identificationCategoryText1);

    client.getClientIdentification().add(clientIdentification1);

    // BenefitApplication
    final BenefitApplicationType benefitApplicationType =
      new BenefitApplicationType();

    // BenefitApplicationCountry
    final CountryType countryType2 = new CountryType();
    final CodeType countryCode = new CodeType();
    countryCode.setValue("@@countryCode");
    countryType2.getCountryCode().add(countryCode);
    final TextType countryName = new TextType();
    countryName.setValue("@@countryName");
    countryType2.getCountryName().add(countryName);
    benefitApplicationType.setBenefitApplicationCountry(countryType2);

    // CountryOfAgrrementCode
    final TextType countryOfAgreementCode = new TextType();
    countryOfAgreementCode.setValue("@@countryOfAgreementCode");
    benefitApplicationType.setCountryOfAgreementCode(countryOfAgreementCode);

    documentContent.setBenefitApplication(benefitApplicationType);

    // set the document content
    correspondenceType.setDocumentContent(documentContent);

    // BDMCorrespondenceTriggerUtil.generateCorrespondence("ISS-6587",
    // FREQUENCYCODE.REALTIME, correspondenceType, 1, 0);

    JAXBContext context;
    java.lang.String mappedData = new java.lang.String();
    context = JAXBContext.newInstance(CorrespondenceType.class);
    final Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

    final StringWriter sw = new StringWriter();
    marshaller.marshal(correspondenceType, sw);
    mappedData = sw.toString();

    System.out.println("Generated XML: " + mappedData);
  }

  public BDMForeignApplicationKey createFA(final long caseID,
    final java.lang.String type) throws AppException, InformationalException {

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID = caseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = type;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();

    return BDMForeignEngagementCaseFactory.newInstance()
      .createForeignApplication(bdmCreateModifyFA);
  }

  /**
   * Utility method to delete the appresource
   *
   * @param resourceName
   * @throws AppException
   * @throws InformationalException
   */
  public void deleteAppresource(final java.lang.String resourceName)
    throws AppException, InformationalException {

    final ResourceName resoureceName = new ResourceName();
    resoureceName.name = resourceName;
    try {
      AppResourceFactory.newInstance().removeByName(resoureceName);
    } catch (final Exception e) {
      // Do nothing
    }
  }

  /**
   * Utility method to insert the appresource
   *
   * @param resourceName
   * @param fileName
   * @throws AppException
   * @throws InformationalException
   * @throws IOException
   */
  public void insertApprersouce(final java.lang.String resourceName,
    final java.lang.String fileName)
    throws AppException, InformationalException, IOException {

    final AppResourceDtls dtls = new AppResourceDtls();

    final URL path =
      BDMCorrespondenceTriggerUtilTest.class.getResource(fileName);
    final File f = new File(path.getFile());
    dtls.resourceID =
      curam.core.fact.UniqueIDFactory.newInstance().getNextID();
    dtls.name = resourceName;
    final byte[] bytes = FileUtils.readFileToByteArray(f);
    dtls.content = new Blob(bytes);
    AppResourceFactory.newInstance().insert(dtls);

  }

  @Test
  public void testMissingMandatoryInfoAndReTrigger() throws Exception {

    deleteAppresource("ISS-6587_CONFIG");
    deleteAppresource("ISS-6587_TEMPLATE");
    insertApprersouce("ISS-6587_CONFIG", "ISS-6587_CONFIG.JSON");
    insertApprersouce("ISS-6587_TEMPLATE", "ISS-6587_TEMPLATE.XML");

    final PDCPersonDetails personDtls =
      new BDMCommunicationTest().createPDCPerson();
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput =
      new BDMCorrespondenceRecipientClientInput();
    correspondenceRecipientClientInput.clientConcernRoleID =
      personDtls.concernRoleID;

    correspondenceRecipientClientInput.toRecipientConcernRoleID =
      personDtls.concernRoleID;

    final BDMCorrespondenceMasterInput bdmCorrespondenceMasterInput =
      new BDMCorrespondenceMasterInput();
    bdmCorrespondenceMasterInput.concernRoleID = personDtls.concernRoleID;

    final BDMCorrespondenceTriggerUtil correspondenceTriggerUtil =
      new BDMCorrespondenceTriggerUtil();
    final List<Long> correspondenceStagingIDList = correspondenceTriggerUtil
      .generateCorrespondenceForBatch("ISS-6587", FREQUENCYCODE.DAILY,
        correspondenceRecipientClientInput, bdmCorrespondenceMasterInput);

    // Assert
    long correspondenceStagingID = 0;
    final BDMCorrespondenceStagingKey key = new BDMCorrespondenceStagingKey();
    if (correspondenceStagingIDList.size() > 0) {
      correspondenceStagingID = correspondenceStagingIDList.get(0);
    }
    key.correspondenceStagingID = correspondenceStagingID;
    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();

    BDMCorrespondenceStagingDtls correspondenceStagingDtls =
      correspondenceStagingObj.read(key);

    assert correspondenceStagingDtls.status
      .equalsIgnoreCase(CORRESPONDENCEJOBSTATUS.MISSINGMANDATORYINFO);

    // Fix the issue with missing fields

    createFA(
      createFEC(personDtls.concernRoleID).createCaseResult.integratedCaseID,
      BDMFOREIGNAPPTYPE.SURVIVORS);

    correspondenceTriggerUtil
      .reTriggerGenerateCorrespondence(correspondenceStagingID);

    // Assert
    key.correspondenceStagingID = correspondenceStagingID;

    correspondenceStagingDtls = correspondenceStagingObj.read(key);

    assert correspondenceStagingDtls.status
      .equalsIgnoreCase(CORRESPONDENCEJOBSTATUS.PENDING);
  }

  /**
   * Upload a sample config and template and test generateCorrespondenceForBatch
   * if it creates the staging record.
   *
   * @throws Exception
   */
  @Test
  public void
    testGenerateCorrespondenceForBatchSample_ISS6587_Recipient_Selection()
      throws Exception {

    // Set up the config and template
    deleteAppresource("ISS-6587_CONFIG");
    deleteAppresource("ISS-6587_TEMPLATE");
    insertApprersouce("ISS-6587_CONFIG", "ISS-6587_CONFIG.JSON");
    insertApprersouce("ISS-6587_TEMPLATE", "ISS-6587_TEMPLATE.XML");

    // TODO set up a concernrole
    final long clientConcernRoleID = 2015246;

    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput =
      new BDMCorrespondenceRecipientClientInput();
    correspondenceRecipientClientInput.clientConcernRoleID =
      clientConcernRoleID;

    // correspondenceRecipientClientInput.toRecipientConcernRoleID = 0;

    final BDMCorrespondenceMasterInput bdmCorrespondenceMasterInput =
      new BDMCorrespondenceMasterInput();
    bdmCorrespondenceMasterInput.concernRoleID = clientConcernRoleID;
    final BDMCorrespondenceTriggerUtil correspondenceTriggerUtil =
      new BDMCorrespondenceTriggerUtil();
    correspondenceTriggerUtil.generateCorrespondenceForBatch("ISS-6587",
      FREQUENCYCODE.DAILY, correspondenceRecipientClientInput,
      bdmCorrespondenceMasterInput);

    // Assert if staging table has the details for that template name in pending
    // status

    final BDMCorrespondenceStaging correspondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();
    final BDMCorrespondenceStagingStatusAndTemplateName key =
      new BDMCorrespondenceStagingStatusAndTemplateName();
    key.templateName = "ISS-6587";
    key.status = CORRESPONDENCEJOBSTATUS.PENDING;

    final BDMCorrespondenceStagingDtlsList bdmoasCorrespondenceStagingDtlsList =
      correspondenceStagingObj.searchByTemplateAndStatus(key);
    assert bdmoasCorrespondenceStagingDtlsList.dtls.size() > 0;

  }
}
