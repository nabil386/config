/**
 *
 */
package curam.bdm.test.ca.gc.bdm.sl.fec.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.address.impl.BDMAddressFormatINTL;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPDELETEREASON;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPINTERIM;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPSTATUS;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.codetable.BDM_FILE_SOURCE;
import curam.ca.gc.bdm.entity.fact.BDMForeignApplicationFactory;
import curam.ca.gc.bdm.entity.fact.BDMTaskFactory;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.intf.BDMForeignApplication;
import curam.ca.gc.bdm.entity.struct.BDMFAHistoryKey;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.entity.struct.BDMTaskDtls;
import curam.ca.gc.bdm.facade.fact.BDMCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMAttachmentIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMCaseKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMCreateModifyFA;
import curam.ca.gc.bdm.facade.fec.struct.BDMDeleteFADetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetailsForRead;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAConcernRoleKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFACountryCodeKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFADetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAList;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAViewAttachmentDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECReadICDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfFAApplicationTypes;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfForeignIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfOffices;
import curam.ca.gc.bdm.facade.fec.struct.BDMModifyFECaseDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMReadFECaseDetails;
import curam.ca.gc.bdm.facade.integratedcase.struct.BDMListICAttachmentDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.struct.BDMReadCaseAttachmentDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMFEC;
import curam.ca.gc.bdm.sl.fec.fact.BDMMaintainForeignEngagementCaseFactory;
import curam.ca.gc.bdm.sl.fec.intf.BDMMaintainForeignEngagementCase;
import curam.ca.gc.bdm.sl.fec.struct.BDMFAInterimCodeDetailsList;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.codetable.BDMTASKTYPE;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.TARGETITEMTYPE;
import curam.codetable.TASKPRIORITY;
import curam.codetable.TASKSTATUS;
import curam.core.facade.fact.ExternalPartyFactory;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CancelCaseAttachmentKey;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.ExternalPartyRegistrationDetails;
import curam.core.facade.struct.ExternalPartyRegistrationResult;
import curam.core.facade.struct.IntegratedCaseIDKey;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.ReadCaseAttachmentKey;
import curam.core.fact.AttachmentFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.PersonRegistration;
import curam.core.sl.entity.fact.TaskAssignmentFactory;
import curam.core.sl.entity.struct.ExternalPartyOfficeKey;
import curam.core.sl.entity.struct.TaskAssignmentDtls;
import curam.core.sl.fact.ExternalPartyOfficeFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.ExternalPartyOfficeDetails;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.RegistrationIDDetails;
import curam.pdc.struct.PDCPersonDetails;
import curam.supervisor.facade.struct.ReserveTaskDetailsForUser;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.UniqueID;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.fact.TaskFactory;
import curam.util.workflow.fact.WorkflowDeadlineFactory;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.TaskDtls;
import curam.util.workflow.struct.TaskKey;
import curam.util.workflow.struct.WorkflowDeadlineDtls;
import curam.wizard.util.impl.CodetableUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author
 *
 */
public class BDMMaintainForeignEngagementCaseTest
  extends CuramServerTestJUnit4 {

  BDMMaintainForeignEngagementCase kBdmMaintainForeignEngagementCase =
    BDMMaintainForeignEngagementCaseFactory.newInstance();

  public BDMMaintainForeignEngagementCaseTest() {

    super();
  }

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  public static final String EMPTY_STRING = "";

  private static String CASEOBJECTIVE_FINANCIALSUPPORT = "CO5";

  private static String CASEOUTCOME = "CO4";

  private static String FILE_LOCATION = "FILE LOCATION";

  private static String FILE_REFERENCE = "FILE REFERENCE";

  private static String FILE_DESCRIPTION = "FILE DESCRIPTION";

  private static String FA_COMMENTS =
    "This comments entered on the New Foreign Application screen";

  private static String YES = "Yes";

  @Test
  public void testGetListOfFAApplicationTypesByCountryCode()
    throws AppException, InformationalException {

    final BDMFACountryCodeKey key = new BDMFACountryCodeKey();
    key.countryCode = BDMSOURCECOUNTRY.JAPAN;

    final BDMListOfFAApplicationTypes list = kBdmMaintainForeignEngagementCase
      .getListOfFAApplicationTypesByCountryCode(key);

    assertTrue(!list.bdmFaApplType.isEmpty());

  }

  @Test
  public void testGetListOfFAApplicationTypesByCountryCode_AF()
    throws AppException, InformationalException {

    final BDMFACountryCodeKey key = new BDMFACountryCodeKey();
    key.countryCode = BDMSOURCECOUNTRY.AFGHANISTAN;

    final BDMListOfFAApplicationTypes list = kBdmMaintainForeignEngagementCase
      .getListOfFAApplicationTypesByCountryCode(key);

    assertTrue(list.bdmFaApplType.isEmpty());

  }

  /**
   * This BPO is used for deleting/canceling a foreign application.
   */
  @Test
  public void testdeleteForeignApplication()
    throws AppException, InformationalException {

    final BDMDeleteFADetails deleteFAData = new BDMDeleteFADetails();

    final BDMForeignApplicationKey bdmForeignApplicationKey = createFA();

    final BDMFAKey bdmBDMFAKey = new BDMFAKey();
    bdmBDMFAKey.fApplicationID = bdmForeignApplicationKey.fApplicationID;

    final BDMFADetails BDMFADetails =
      kBdmMaintainForeignEngagementCase.readForeignApplication(bdmBDMFAKey);

    deleteFAData.bdmDeleteFAReason =
      BDMFOREIGNAPPDELETEREASON.CLIENT_WITHDRAWN;

  }

  /** Read FEC case when Fec is created for SSA country */
  @Test
  public void testReadFEIntegratedCase()
    throws AppException, InformationalException {

    final BDMCaseKey bdmCaseKey = new BDMCaseKey();
    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    final BDMForeignApplicationKey bdmfaKey =
      createFAforCountry(BDMSOURCECOUNTRY.JAPAN);
    final BDMForeignApplicationDtls bdmfaDtls = bdmfa.read(bdmfaKey);

    bdmCaseKey.caseID = bdmfaDtls.caseID;

    final IntegratedCaseIDKey key = new IntegratedCaseIDKey();
    key.caseID = bdmCaseKey.caseID;

    final BDMReadFECaseDetails bdmReadFECaseDetails =
      kBdmMaintainForeignEngagementCase.readFEIntegratedCase(key);

    assertNotNull(bdmReadFECaseDetails);
    assertEquals(BDMSOURCECOUNTRY.JAPAN, bdmReadFECaseDetails.countryCode);
  }

  /** Throw wxception : Read FEC case when no country is associated with case */
  // TODO: Check the aassertion ,

  @Test
  public void testReadFEIntegratedCase_NoCountry()
    throws AppException, InformationalException {

    boolean caught = false;
    BDMReadFECaseDetails bdmReadFECaseDetails = null;
    try {
      final CreateIntegratedCaseResultAndMessages cCreateIntegratedCaseResultAndMessages =
        registerPersonAndcreateFECCaseforCountry(CuramConst.gkEmpty);
      final IntegratedCaseIDKey key = new IntegratedCaseIDKey();
      key.caseID =
        cCreateIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

      bdmReadFECaseDetails =
        kBdmMaintainForeignEngagementCase.readFEIntegratedCase(key);
    } catch (final Exception e) {
      caught = true;

    }

  }

  /**
   * unlinking of a foreign application with an
   * attachment.
   */
  @Test
  public void testunlinkFAFromAttachment()
    throws AppException, InformationalException {

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Create Attachemnt of type Foreign Application

    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      "c:\\Test\\Test.pdf";
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      FILE_DESCRIPTION;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.DIRECT_DEPOSIT;

    attachmentIDs = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment details from Step1
    bdmFAAttachmentDetails = kBdmMaintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Set data for foreign application
    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    setBDMCreateModifyFA(bdmCreateModifyFA);

    // Create FA
    final BDMForeignApplicationKey bdmForeignApplicationKey =
      kBdmMaintainForeignEngagementCase
        .createForeignApplication(bdmCreateModifyFA);

    // Link Attachment to FA
    bdmFAAttachmentDetails.selectedFAList =
      bdmForeignApplicationKey.fApplicationID + "";

    // Click Save linking to FA
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kSave;

    boolean caught = false;
    try {

      attachmentIDs = kBdmMaintainForeignEngagementCase
        .createFECaseAttachmentLinkingFA(bdmFAAttachmentDetails);

    } catch (final Exception e) {
      caught = true;

      assertEquals(BDMFEC.ERR_SELECT_FA_TO_LINK.getMessageText(),
        e.getMessage());
    }
    assertFalse(caught);

    assertTrue(attachmentIDs.feAttachmentLinkID != 0);

    final BDMFAAttachmentKey bdmFAAttachmentKey = new BDMFAAttachmentKey();

    bdmFAAttachmentKey.attachmentID = attachmentIDs.attachmentID;
    bdmFAAttachmentKey.feAttachmentLinkID = attachmentIDs.feAttachmentLinkID;

    kBdmMaintainForeignEngagementCase
      .unlinkFAFromAttachment(bdmFAAttachmentKey);

  }

  /** Modify FEC case when Fec is created for SSA country */
  @Test
  public void testModifyForeignApplication()
    throws AppException, InformationalException {

    // Create FEC and FA
    final BDMForeignApplicationKey bdmForeignApplicationKey =
      createFAforCountry(BDMSOURCECOUNTRY.JAPAN);

    final BDMFAKey bdmFAKey = new BDMFAKey();
    bdmFAKey.fApplicationID = bdmForeignApplicationKey.fApplicationID;

    // Read FA
    BDMFADetails bdmFADetails =
      kBdmMaintainForeignEngagementCase.readForeignApplication(bdmFAKey);

    // Assert
    assertEquals(FA_COMMENTS, bdmFADetails.comments);
    assertEquals(BDMFOREIGNAPPTYPE.INVALIDITY, bdmFADetails.typeCode);
    assertEquals(Date.getCurrentDate(), bdmFADetails.receiveDate);
    assertEquals(BDMYESNO.NO, bdmFADetails.consent);

    // CREATE NEW data for modify
    BDMCreateModifyFA modifiedFADetails = new BDMCreateModifyFA();
    modifiedFADetails.caseID = bdmFADetails.caseID;
    modifiedFADetails.typeCode = BDMFOREIGNAPPTYPE.SURVIVORS;
    modifiedFADetails.comments = "Modified Comments";
    modifiedFADetails.fApplicationID = bdmFADetails.fApplicationID;
    // Execute Modify
    kBdmMaintainForeignEngagementCase
      .modifyForeignApplication(modifiedFADetails);

    // Read Foreign Application to verify modified data
    bdmFADetails =
      kBdmMaintainForeignEngagementCase.readForeignApplication(bdmFAKey);

    // assert
    assertEquals(BDMFOREIGNAPPTYPE.SURVIVORS, bdmFADetails.typeCode);
    assertEquals("Modified Comments", bdmFADetails.comments);
    assertEquals(EMPTY_STRING, bdmFADetails.consent);

    // Set new modified data
    modifiedFADetails = new BDMCreateModifyFA();
    modifiedFADetails.caseID = bdmFADetails.caseID;
    modifiedFADetails.typeCode = BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM;
    modifiedFADetails.comments = "Modified 2 Comments";
    modifiedFADetails.consent = BDMYESNO.YES;
    modifiedFADetails.fApplicationID = bdmFADetails.fApplicationID;
    // Execute Modify
    kBdmMaintainForeignEngagementCase
      .modifyForeignApplication(modifiedFADetails);

    // Read Foreign Application to verify modified data
    bdmFADetails =
      kBdmMaintainForeignEngagementCase.readForeignApplication(bdmFAKey);

    // assert
    assertEquals(BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM,
      bdmFADetails.typeCode);
    assertEquals("Modified 2 Comments", bdmFADetails.comments);
    assertEquals(BDMYESNO.YES, bdmFADetails.consent);

  }

  /** Read FEC Deatils for display on Home page for SSA Country */
  @Test
  public void testReadFECDetailsForHome()
    throws AppException, InformationalException {

    final CreateIntegratedCaseResultAndMessages cCreateIntegratedCaseResultAndMessages =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAPAN);

    final IntegratedCaseIDKey key = new IntegratedCaseIDKey();
    key.caseID =
      cCreateIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMFECReadICDetails bdmFECReadICDetails =
      kBdmMaintainForeignEngagementCase.readFECDetailsForHome(key);

    assertNotNull(bdmFECReadICDetails);
    assertEquals(BDMSOURCECOUNTRY.JAPAN, bdmFECReadICDetails.countryCode);
    assertTrue(bdmFECReadICDetails.displayCountryLinkInd);

  }

  /**
   * Read FEC Deatils for display on Home page for non SSA Country .verify SSA
   * Country link is disabled
   */
  @Test
  public void testReadFECDetailsForHome_nonSSACountry()
    throws AppException, InformationalException {

    final CreateIntegratedCaseResultAndMessages cCreateIntegratedCaseResultAndMessages =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.IRELAND);

    final IntegratedCaseIDKey key = new IntegratedCaseIDKey();
    key.caseID =
      cCreateIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMFECReadICDetails bdmFECReadICDetails =
      kBdmMaintainForeignEngagementCase.readFECDetailsForHome(key);

    assertNotNull(bdmFECReadICDetails);
    assertEquals(BDMSOURCECOUNTRY.IRELAND, bdmFECReadICDetails.countryCode);
    assertTrue(!bdmFECReadICDetails.displayCountryLinkInd);

  }

  /**
   * List foreign Application
   */

  @Test
  public void testListForeignApplications()
    throws AppException, InformationalException {

    final BDMCaseKey bdmCaseKey = new BDMCaseKey();
    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    final BDMForeignApplicationKey bdmfaKey = createFA();
    final BDMForeignApplicationDtls bdmfaDtls = bdmfa.read(bdmfaKey);

    bdmCaseKey.caseID = bdmfaDtls.caseID;

    final BDMFAList bdmfaList =
      kBdmMaintainForeignEngagementCase.listForeignApplications(bdmCaseKey);

    assertTrue(bdmfaList.bdmFADetails.size() == 1);

  }

  /** Create Foreign Application for SSA Country */
  @Test
  public void testCreateForeignApplicationUS()
    throws AppException, InformationalException {

    final CreateIntegratedCaseResultAndMessages cCreateIntegratedCaseResultAndMessages =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.US);

    final IntegratedCaseIDKey key = new IntegratedCaseIDKey();
    key.caseID =
      cCreateIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMFECReadICDetails bdmFECReadICDetails =
      kBdmMaintainForeignEngagementCase.readFECDetailsForHome(key);

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      cCreateIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";

    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();
    bdmCreateModifyFA.consent = BDMYESNO.NO;
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM;
    bdmCreateModifyFA.countryCode = BDMSOURCECOUNTRY.US;

    final BDMForeignApplicationKey bdmForeignApplicationKey =
      kBdmMaintainForeignEngagementCase
        .createForeignApplication(bdmCreateModifyFA);
    final BDMFAKey bdmFAkey = new BDMFAKey();
    bdmFAkey.fApplicationID = bdmForeignApplicationKey.fApplicationID;

    final BDMFADetails bdmFADetails =
      kBdmMaintainForeignEngagementCase.readForeignApplication(bdmFAkey);

    assertEquals(bdmFADetails.caseID,
      cCreateIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID);
    assertEquals(BDMSOURCECOUNTRY.US, bdmFECReadICDetails.countryCode);
    assertTrue(bdmFECReadICDetails.displayCountryLinkInd);

    assertEquals(bdmFADetails.status, BDMFOREIGNAPPSTATUS.COMPLETED);

  }

  /** Create Foreign Application for non SSA Country */
  @Test
  public void testCreateForeignApplicationIRELAND()
    throws AppException, InformationalException {

    final CreateIntegratedCaseResultAndMessages cCreateIntegratedCaseResultAndMessages =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.IRELAND);

    final IntegratedCaseIDKey key = new IntegratedCaseIDKey();
    key.caseID =
      cCreateIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    final BDMFECReadICDetails bdmFECReadICDetails =
      kBdmMaintainForeignEngagementCase.readFECDetailsForHome(key);

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      cCreateIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();
    bdmCreateModifyFA.consent = BDMYESNO.NO;
    bdmCreateModifyFA.externalPartyOfficeID = 100;

    final BDMForeignApplicationKey bdmForeignApplicationKey =
      kBdmMaintainForeignEngagementCase
        .createForeignApplication(bdmCreateModifyFA);
    final BDMFAKey bdmFAkey = new BDMFAKey();
    bdmFAkey.fApplicationID = bdmForeignApplicationKey.fApplicationID;

    final BDMFADetails bdmFADetails =
      kBdmMaintainForeignEngagementCase.readForeignApplication(bdmFAkey);

    assertEquals(bdmFADetails.caseID,
      cCreateIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID);
    assertEquals(BDMSOURCECOUNTRY.IRELAND, bdmFECReadICDetails.countryCode);
    assertTrue(!bdmFECReadICDetails.displayCountryLinkInd);

  }

  // TODO: nfrastructure:ID_RECORD_CHANGED: This record has been changed by
  // another user. Please start again.
  /** Modify Foreign Application from SSA to non SSA Country */
  @Test
  public void testModifyFEIntegratedCase()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerAPersonForTest("717342935");

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    // BEGIN: read FE IC Details
    BDMReadFECaseDetails bdmReadFECaseDetails = new BDMReadFECaseDetails();

    final IntegratedCaseIDKey integratedCaseIDKey = new IntegratedCaseIDKey();
    integratedCaseIDKey.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    bdmReadFECaseDetails = kBdmMaintainForeignEngagementCase
      .readFEIntegratedCase(integratedCaseIDKey);
    // END: read FE IC Details

    final BDMModifyFECaseDetails bdmModifyFECaseDetails =
      new BDMModifyFECaseDetails();

    bdmModifyFECaseDetails.countryCode = BDMSOURCECOUNTRY.JAMAICA;

    bdmModifyFECaseDetails.modifyICDetails.comments =
      "This comments entered on Edit Foreign Engagement Case - Screen";

    bdmModifyFECaseDetails.modifyICDetails.caseID =
      bdmReadFECaseDetails.readICDetails.details.caseID;
    bdmModifyFECaseDetails.modifyICDetails.concernRoleID =
      bdmReadFECaseDetails.readICDetails.details.concernRoleID;
    bdmModifyFECaseDetails.modifyICDetails.ownerOrgObjectLinkID =
      bdmReadFECaseDetails.readICDetails.details.orgObjectLinkID;
    bdmModifyFECaseDetails.modifyICDetails.versionNo =
      bdmReadFECaseDetails.readICDetails.details.versionNo;
    bdmModifyFECaseDetails.modifyICDetails.registrationDate =
      bdmReadFECaseDetails.readICDetails.details.registrationDate;
    bdmModifyFECaseDetails.modifyICDetails.objectiveCode =
      bdmReadFECaseDetails.readICDetails.details.objectiveCode;
    bdmModifyFECaseDetails.modifyICDetails.expectedEndDate =
      bdmReadFECaseDetails.readICDetails.details.expectedEndDate;
    bdmModifyFECaseDetails.modifyICDetails.priorityCode =
      bdmReadFECaseDetails.readICDetails.details.priorityCode;
    bdmModifyFECaseDetails.modifyICDetails.receiptDate =
      bdmReadFECaseDetails.readICDetails.details.receiptDate;
    bdmModifyFECaseDetails.modifyICDetails.outcomeCode =
      bdmReadFECaseDetails.readICDetails.details.outcomeCode;

    kBdmMaintainForeignEngagementCase
      .modifyFEIntegratedCase(bdmModifyFECaseDetails);

    bdmReadFECaseDetails = new BDMReadFECaseDetails();

    bdmReadFECaseDetails = kBdmMaintainForeignEngagementCase
      .readFEIntegratedCase(integratedCaseIDKey);

    assertTrue(
      bdmReadFECaseDetails.countryCode.equals(BDMSOURCECOUNTRY.JAMAICA));

    assertTrue(bdmReadFECaseDetails.readICDetails.details.priorityCode
      .equals(CASEPRIORITY.HIGH));

    assertTrue(bdmReadFECaseDetails.readICDetails.details.integratedCaseType
      .equals(PRODUCTCATEGORY.BDM_IC_FEC_PROGRAM));

    assertTrue(bdmReadFECaseDetails.readICDetails.details.comments
      .equals(bdmModifyFECaseDetails.modifyICDetails.comments));

  }

  /** Delete Foreign Application for SSA Country */
  @Test
  public void testDeleteForeignApplication()
    throws AppException, InformationalException {

    // Create Foreign Application
    final BDMForeignApplicationKey bdmForeignApplicationKey1 = createFA();

    final BDMFAKey bdmFAkey = new BDMFAKey();
    bdmFAkey.fApplicationID = bdmForeignApplicationKey1.fApplicationID;

    // Read FA
    BDMFADetails bdmFADetails =
      kBdmMaintainForeignEngagementCase.readForeignApplication(bdmFAkey);
    assertEquals(RECORDSTATUS.DEFAULTCODE, bdmFADetails.recordStatus);

    final BDMDeleteFADetails bdmDeleteFADetails = new BDMDeleteFADetails();
    bdmDeleteFADetails.fApplicationID = bdmFADetails.fApplicationID;
    bdmDeleteFADetails.bdmDeleteFAReason =
      BDMFOREIGNAPPDELETEREASON.ENTERED_IN_ERROR;

    // Delete FA
    kBdmMaintainForeignEngagementCase
      .deleteForeignApplication(bdmDeleteFADetails);

    // Read FA
    bdmFADetails =
      kBdmMaintainForeignEngagementCase.readForeignApplication(bdmFAkey);

    assertEquals(RECORDSTATUS.CANCELLED, bdmFADetails.recordStatus);

  }

  /** Create Foreign Application for SSA Country */
  @Test
  public void testCreateFECaseAttachment()
    throws AppException, InformationalException {

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);
    // Create Attachemnt
    attachmentIDs = createFECAttachment(
      icResult.createCaseResult.integratedCaseID, BDMConstants.kSaveAndClose,
      Date.getCurrentDate(), DOCUMENTTYPE.DIRECT_DEPOSIT);

    // Read attachment
    bdmFAAttachmentDetails = kBdmMaintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    final ReadCaseAttachmentKey readCaseAttachmentKey =
      new ReadCaseAttachmentKey();
    readCaseAttachmentKey.readCaseAttachmentIn.attachmentID =
      attachmentIDs.attachmentID;
    readCaseAttachmentKey.readCaseAttachmentIn.caseAttachmentLinkID =
      attachmentIDs.caseAttachmentLinkID;
    final BDMReadCaseAttachmentDetails readAttachment =
      BDMCaseFactory.newInstance().readCaseAttachment(readCaseAttachmentKey);

    assertEquals(BDM_FILE_SOURCE.CLIENT, readAttachment.fileSource);

  }

  /** test Read FEC Attachment Wizard For Modify */
  @Test
  public void testReadFECAttachmentWizardForModify()
    throws AppException, InformationalException {

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;
    BDMFAAttachmentDetailsForRead bdmFAAttachmentDetailsForRead =
      new BDMFAAttachmentDetailsForRead();
    // Create Attachemnt
    attachmentIDs = createFECAttachment(
      icResult.createCaseResult.integratedCaseID, BDMConstants.kSaveAndClose,
      Date.getCurrentDate(), DOCUMENTTYPE.DIRECT_DEPOSIT);

    attachmentIDs.caseID = icResult.createCaseResult.integratedCaseID;
    // Read attachment
    bdmFAAttachmentDetailsForRead = kBdmMaintainForeignEngagementCase
      .readFECAttachmentWizardForModify(attachmentIDs);

    assertEquals(FILE_REFERENCE,
      bdmFAAttachmentDetailsForRead.bdmReadAttachmentDetails.dtls.attachmentDetails.readCaseAttachmentOut.fileReference);

    assertEquals(BDM_FILE_SOURCE.CLIENT,
      bdmFAAttachmentDetailsForRead.bdmReadAttachmentDetails.fileSource);

  }

  /** Create Foreign Application for SSA Country */
  @Test
  public void testCreateFECaseAttachment_Next()
    throws AppException, InformationalException {

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Create Attachemnt

    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.EMPLOYER;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      icResult.createCaseResult.integratedCaseID;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      FILE_DESCRIPTION;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.DIRECT_DEPOSIT;

    attachmentIDs = kBdmMaintainForeignEngagementCase
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment
    bdmFAAttachmentDetails = kBdmMaintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    assertEquals(FILE_DESCRIPTION,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description);

    assertEquals(FILE_REFERENCE,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference);

    assertEquals(FILE_LOCATION,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation);

    assertEquals(BDM_FILE_SOURCE.EMPLOYER,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource);

  }

  /** Create Foreign Application for SSA Country */
  @Test
  public void testCreateFECaseAttachmentLinkingFA_Back()
    throws AppException, InformationalException {

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Create Attachemnt

    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.EMPLOYER;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      icResult.createCaseResult.integratedCaseID;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      FILE_DESCRIPTION;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.DIRECT_DEPOSIT;

    attachmentIDs = kBdmMaintainForeignEngagementCase
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment
    bdmFAAttachmentDetails = kBdmMaintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    assertEquals(FILE_DESCRIPTION,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description);

    assertEquals(FILE_REFERENCE,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference);

    assertEquals(FILE_LOCATION,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation);

    assertEquals(BDM_FILE_SOURCE.EMPLOYER,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource);

    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kBack;

    attachmentIDs = kBdmMaintainForeignEngagementCase
      .createFECaseAttachmentLinkingFA(bdmFAAttachmentDetails);

    // Read attachment
    bdmFAAttachmentDetails = kBdmMaintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    assertEquals(FILE_DESCRIPTION,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description);

    assertEquals(FILE_REFERENCE,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference);

    assertEquals(FILE_LOCATION,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation);

    assertEquals(BDM_FILE_SOURCE.EMPLOYER,
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource);

  }

  /** Create Foreign Application for SSA Country */
  @Test
  public void testCreateFECaseAttachment_exception_Decription()
    throws AppException, InformationalException {

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    final BDMFAAttachmentDetails bdmFAAttachmentDetails = null;
    // Create Attachemnt without descritpion

    final BDMFAAttachmentDetails createFAAttachment =
      new BDMFAAttachmentDetails();

    createFAAttachment.actionIDProperty = BDMConstants.kSaveAndClose;
    createFAAttachment.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      icResult.createCaseResult.integratedCaseID;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();

    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;

    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.DIRECT_DEPOSIT;

    boolean caught = false;
    try {

      attachmentIDs = BDMMaintainForeignEngagementCaseFactory.newInstance()
        .createFECaseAttachment(createFAAttachment);

    } catch (final Exception e) {
      caught = true;

      assertEquals(BDMFEC.ERR_DESCRIPTION_MUST_BE_ENTERED.getMessageText(),
        e.getMessage());
    }
    assertTrue(caught);

  }

  /** Create Foreign Application for SSA Country */
  @Test
  public void testCreateFECaseAttachment_exceptionFA_kSaveAndClose()
    throws AppException, InformationalException {

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    final BDMFAAttachmentDetails bdmFAAttachmentDetails = null;
    // Create Attachemnt without descritpion

    final BDMFAAttachmentDetails createFAAttachment =
      new BDMFAAttachmentDetails();

    createFAAttachment.actionIDProperty = BDMConstants.kSaveAndClose;
    createFAAttachment.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      icResult.createCaseResult.integratedCaseID;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();

    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Descritpion";

    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.FOREIGN_APPLICATION;

    boolean caught = false;
    try {

      attachmentIDs = BDMMaintainForeignEngagementCaseFactory.newInstance()
        .createFECaseAttachment(createFAAttachment);

    } catch (final Exception e) {
      caught = true;

      assertEquals(BDMFEC.ERR_CLICK_NEXT_TO_LINK_FA.getMessageText(),
        e.getMessage());
    }
    assertTrue(caught);

  }

  /**
   * Display error when creating Foreign Application attachment without linking
   * to Foreign Application
   */
  @Test
  public void testCreateFECaseAttachmentLinkingFA_exceptionFA_kSave()
    throws AppException, InformationalException {

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Create Attachemnt of type Foreign Application

    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      icResult.createCaseResult.integratedCaseID;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      FILE_DESCRIPTION;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.FOREIGN_APPLICATION;

    attachmentIDs = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment details from Step1
    bdmFAAttachmentDetails = kBdmMaintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);
    // Click Save without linking to FA
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kSave;

    boolean caught = false;
    try {

      attachmentIDs = kBdmMaintainForeignEngagementCase
        .createFECaseAttachmentLinkingFA(bdmFAAttachmentDetails);

    } catch (final Exception e) {
      caught = true;

      assertEquals(BDMFEC.ERR_SELECT_FA_TO_LINK.getMessageText(),
        e.getMessage());
    }
    assertTrue(caught);

  }

  /**
   * n creating Foreign Application attachment linking
   * to Foreign Application
   */
  @Test
  public void testCreateFECaseAttachmentLinkingFA_kSave()
    throws AppException, InformationalException {

    // Register person Create FEc case and create Fpreign Application

    final BDMForeignApplicationKey faKey = createFA();

    final BDMFAKey bdmFAKey = new BDMFAKey();
    bdmFAKey.fApplicationID = faKey.fApplicationID;

    // Read Foreign Application details
    final BDMFADetails readFADetails =
      kBdmMaintainForeignEngagementCase.readForeignApplication(bdmFAKey);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Create Attachemnt of type Foreign Application and click next
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      readFADetails.caseID;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      FILE_DESCRIPTION;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.FOREIGN_APPLICATION_ADDITIONAL_DOCUMENTS;

    attachmentIDs = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment details from Step1
    bdmFAAttachmentDetails = kBdmMaintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Click Save without linking to FA
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kSave;
    attachmentIDs.caseID =
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;

    final BDMFAList fAList = kBdmMaintainForeignEngagementCase
      .listFANotLinkedWithAttachment(attachmentIDs);
    bdmFAAttachmentDetails.selectedFAList =
      String.valueOf(fAList.bdmFADetails.get(0).fApplicationID);

    boolean caught = false;
    try {

      attachmentIDs = kBdmMaintainForeignEngagementCase
        .createFECaseAttachmentLinkingFA(bdmFAAttachmentDetails);

    } catch (final Exception e) {
      caught = true;

      assertEquals(BDMFEC.ERR_SELECT_FA_TO_LINK.getMessageText(),
        e.getMessage());
    }
    assertFalse(caught);

  }

  @Test
  public void testReserveTask() throws AppException, InformationalException {

    // register person
    final PersonRegistrationResult registrationResult =
      registerAPersonForTest("717342935");

    // create FE IC for the person
    final CreateIntegratedCaseResultAndMessages integratedCase =
      createAFE_IC_forTest(
        registrationResult.registrationIDDetails.concernRoleID);
    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      integratedCase.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();
    bdmCreateModifyFA.consent = BDMYESNO.NO;

    final BDMForeignApplicationKey fAKey = kBdmMaintainForeignEngagementCase
      .createForeignApplication(bdmCreateModifyFA);
    kBdmMaintainForeignEngagementCase
      .createForeignApplication(bdmCreateModifyFA);

    final TaskDtls taskDtls = createTaskData("",
      registrationResult.registrationIDDetails.concernRoleID,
      integratedCase.createCaseResult.integratedCaseID);

    final ReserveTaskDetailsForUser details = new ReserveTaskDetailsForUser();
    details.reserveDetails.comments = "Reserve for caseworker";
    details.reserveDetails.taskID = taskDtls.taskID;
    details.reserveDetails.userName = "caseworker";

    kBdmMaintainForeignEngagementCase.reserveTask(details);
    final TaskKey taskKey = new TaskKey();
    taskKey.taskID = taskDtls.taskID;
    final TaskDtls readTask = TaskFactory.newInstance().read(taskKey);

    assertEquals("caseworker", readTask.reservedBy);

  }

  /**
   * n creating Foreign Application attachment linking
   * to Foreign Application
   */
  @Test
  public void testModifyFECaseAttachment_Next()
    throws AppException, InformationalException {

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);
    // Create Attachemnt
    attachmentIDs = createFECAttachment(
      icResult.createCaseResult.integratedCaseID, BDMConstants.kSaveAndClose,
      Date.getCurrentDate(), DOCUMENTTYPE.DIRECT_DEPOSIT);

    final BDMFAAttachmentDetailsForRead bdmFAAttachmentDetailsForRead =
      kBdmMaintainForeignEngagementCase
        .readFECAttachmentWizardForModify(attachmentIDs);

    // Read attachment
    bdmFAAttachmentDetails = kBdmMaintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Modified Attachment";
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      "Modified File Location";

    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    // Modify attachment Save and close
    attachmentIDs = kBdmMaintainForeignEngagementCase
      .modifyFECaseAttachment(bdmFAAttachmentDetails);

    // read
    final ReadCaseAttachmentKey readCaseAttachmentKey =
      new ReadCaseAttachmentKey();
    readCaseAttachmentKey.readCaseAttachmentIn.attachmentID =
      attachmentIDs.attachmentID;
    readCaseAttachmentKey.readCaseAttachmentIn.caseAttachmentLinkID =
      attachmentIDs.caseAttachmentLinkID;
    final BDMReadCaseAttachmentDetails readAttachment =
      BDMCaseFactory.newInstance().readCaseAttachment(readCaseAttachmentKey);
    assertEquals("Modified File Location",
      readAttachment.readCaseAttchDtls.readCaseAttachmentOut.fileLocation);
    assertEquals("Modified Attachment",
      readAttachment.readCaseAttchDtls.readCaseAttachmentOut.description);
    assertEquals(BDM_FILE_SOURCE.CLIENT, readAttachment.fileSource);

  }

  // TODO: no implementation for the method in class
  @Test
  public void testreadFAAttachmentDetails()
    throws AppException, InformationalException {

    BDMFAViewAttachmentDetails bdmfaViewAttachmentDetails =
      new BDMFAViewAttachmentDetails();

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    final BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);
    // Create Attachemnt
    attachmentIDs = createFECAttachment(
      icResult.createCaseResult.integratedCaseID, BDMConstants.kSaveAndClose,
      Date.getCurrentDate(), DOCUMENTTYPE.DIRECT_DEPOSIT);

    final BDMFAAttachmentKey key = new BDMFAAttachmentKey();
    key.attachmentID = attachmentIDs.attachmentID;
    key.caseAttachmentLinkID = attachmentIDs.caseAttachmentLinkID;

    bdmfaViewAttachmentDetails =
      kBdmMaintainForeignEngagementCase.readFAAttachmentDetails(key);

    assertEquals("",
      bdmfaViewAttachmentDetails.bdmCaseAttachmentDetails.fileSource);
  }

  @Test
  public void testlistFAInterimCodeDetails()
    throws AppException, InformationalException {

    final BDMFAInterimCodeDetailsList list =
      kBdmMaintainForeignEngagementCase.listFAInterimCodeDetails();

    assertTrue(!list.bdmFAInterimCodeDetails.isEmpty());

  }

  // TODO: no implementation for the method in class
  @Test
  public void testlistFAAttachments()
    throws AppException, InformationalException {

    BDMListICAttachmentDetails bdmListICAttachmentDetails =
      new BDMListICAttachmentDetails();

    final BDMCaseKey key = new BDMCaseKey();

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    final BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);
    // Create Attachemnt
    attachmentIDs = createFECAttachment(
      icResult.createCaseResult.integratedCaseID, BDMConstants.kSaveAndClose,
      Date.getCurrentDate(), DOCUMENTTYPE.DIRECT_DEPOSIT);

    key.caseID = attachmentIDs.caseID;

    bdmListICAttachmentDetails =
      kBdmMaintainForeignEngagementCase.listFAAttachments(key);

  }

  @Test
  public void testViewForeignApplication()
    throws AppException, InformationalException {

    BDMFADetails bdmFADetails = new BDMFADetails();
    final BDMFAKey bdmFAKey = new BDMFAKey();
    // Register External Party
    final ExternalPartyRegistrationDetails externalPartyRegistrationDetails =
      getExternalPartyRegistrationDetails_Default("Ireland");

    final ExternalPartyRegistrationResult externalPartyRegistrationResult =
      registerExternalPartyRegistrationDetails(
        externalPartyRegistrationDetails);

    // Register External Party Office
    final ExternalPartyOfficeKey externalPartyOfficeKey =
      createExternalPartyOffice(
        externalPartyRegistrationResult.externalPartyRegistrationIDDetails.concernRoleID);

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerAPersonForTest("139553556");

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFECCaseforCountry(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMSOURCECOUNTRY.IRELAND);

    // Set data for foreign application
    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    setBDMCreateModifyFA(bdmCreateModifyFA);

    bdmCreateModifyFA.externalPartyOfficeID =
      externalPartyOfficeKey.externalPartyOfficeID;
    // Create FA
    final BDMForeignApplicationKey bdmForeignApplicationKey =
      kBdmMaintainForeignEngagementCase
        .createForeignApplication(bdmCreateModifyFA);

    bdmFAKey.fApplicationID = bdmForeignApplicationKey.fApplicationID;

    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();
    // Read FA
    bdmFADetails =
      kBdmMaintainForeignEngagementCase.viewForeignApplication(bdmFAKey);

    final StringBuffer interimChkStrList = new StringBuffer();
    interimChkStrList.append(BDMFOREIGNAPPINTERIM.RETIREMENT_OR_OLDAGE)
      .append(CuramConst.gkTabDelimiter)
      .append(BDMFOREIGNAPPINTERIM.DISABILITY)
      .append(CuramConst.gkTabDelimiter)
      .append(BDMFOREIGNAPPINTERIM.SURVIVORS)
      .append(CuramConst.gkTabDelimiter).append(BDMFOREIGNAPPINTERIM.OTHER);

    assertEquals(FA_COMMENTS, bdmFADetails.comments);
    assertEquals(BDMFOREIGNAPPTYPE.INVALIDITY, bdmFADetails.typeCode);
    assertEquals(Date.getCurrentDate(), bdmFADetails.receiveDate);
    assertEquals(BDMYESNO.NO, bdmFADetails.consent);

    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.FOREIGN_BEN_INTERIM;
    bdmCreateModifyFA.bessInd = true;
    bdmCreateModifyFA.consent = BDMYESNO.YES;
    bdmCreateModifyFA.interimOther = "All benefits";
    bdmCreateModifyFA.interimChkStrList = interimChkStrList.toString();

    bdmCreateModifyFA.fApplicationID =
      bdmForeignApplicationKey.fApplicationID;

    kBdmMaintainForeignEngagementCase
      .modifyForeignApplication(bdmCreateModifyFA);

    // Read FA
    bdmFADetails =
      kBdmMaintainForeignEngagementCase.viewForeignApplication(bdmFAKey);

    assertEquals(YES, bdmFADetails.chkRetirementStr);
    assertEquals(BDMYESNO.YES, bdmFADetails.consent);
    assertEquals(YES, bdmFADetails.chkDisabilityStr);
    assertEquals(YES, bdmFADetails.chkSurvivorStr);
    assertEquals("All benefits", bdmFADetails.chkOtherStr);

  }

  /**
   * Creates test data for task with deadline date set
   *
   * @throws AppException
   * @throws InformationalException
   */
  private TaskDtls createTaskData(final String username,
    final long concernRoleID, final long caseID)
    throws AppException, InformationalException {

    // create task object
    final TaskDtls taskCreateDetail = createTaskDtls(username);
    final BDMTaskDtls bdmTaskDtls =
      createBDMTaskDtls(taskCreateDetail.taskID);

    // create task assignment
    createTaskAssigmentDtls(username, taskCreateDetail);

    // Create task deadline
    createWorkFlowDeadlineDtls(taskCreateDetail);

    // Create Biz Obje assocuation
    BizObjAssociationDtls bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = UniqueID.nextUniqueID();
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = concernRoleID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.PERSON;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);

    bizObjAssociationDtls = new BizObjAssociationDtls();
    bizObjAssociationDtls.bizObjAssocID = taskCreateDetail.taskID;
    bizObjAssociationDtls.taskID = taskCreateDetail.taskID;
    bizObjAssociationDtls.bizObjectID = caseID;
    bizObjAssociationDtls.bizObjectType = BUSINESSOBJECTTYPE.CASE;

    BizObjAssociationFactory.newInstance().insert(bizObjAssociationDtls);
    return taskCreateDetail;

  }

  /**
   * @param taskCreateDetail
   * @throws AppException
   * @throws InformationalException
   */
  private void createWorkFlowDeadlineDtls(final TaskDtls taskCreateDetail)
    throws AppException, InformationalException {

    final WorkflowDeadlineDtls workflowDeadlineDtls =
      new WorkflowDeadlineDtls();
    workflowDeadlineDtls.deadlineID = UniqueID.nextUniqueID();
    workflowDeadlineDtls.taskID = taskCreateDetail.taskID;
    workflowDeadlineDtls.suspended = false;
    workflowDeadlineDtls.deadlineTime =
      DateTime.getCurrentDateTime().addTime(24, 0, 0);
    workflowDeadlineDtls.deadlineType = "WDT1";
    workflowDeadlineDtls.versionNo = 1;

    WorkflowDeadlineFactory.newInstance().insert(workflowDeadlineDtls);
  }

  /**
   * @param username
   * @param taskCreateDetail
   * @throws AppException
   * @throws InformationalException
   */
  private void createTaskAssigmentDtls(final String username,
    final TaskDtls taskCreateDetail)
    throws AppException, InformationalException {

    final TaskAssignmentDtls taskAssignmentDtls = new TaskAssignmentDtls();
    taskAssignmentDtls.taskID = taskCreateDetail.taskID;
    taskAssignmentDtls.relatedName = username;
    taskAssignmentDtls.assigneeType = TARGETITEMTYPE.USER;

    TaskAssignmentFactory.newInstance().insert(taskAssignmentDtls);
  }

  private TaskDtls createTaskDtls(final String username)
    throws AppException, InformationalException {

    final TaskDtls taskCreateDetail = new TaskDtls();

    taskCreateDetail.taskID = UniqueID.nextUniqueID();
    taskCreateDetail.creationTime = DateTime.getCurrentDateTime();
    taskCreateDetail.status = TASKSTATUS.NOTSTARTED;
    taskCreateDetail.wdoSnapshot = "";
    taskCreateDetail.priority = TASKPRIORITY.NORMAL;
    taskCreateDetail.versionNo = 1;
    taskCreateDetail.reservedBy = username;

    TaskFactory.newInstance().insert(taskCreateDetail);
    return taskCreateDetail;
  }

  private void
    setBDMCreateModifyFA(final BDMCreateModifyFA bdmCreateModifyFA) {

    // Set data for foreign application

    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();
    bdmCreateModifyFA.consent = BDMYESNO.NO;

  }

  /** Utility method to create wizard attachment data */

  private BDMAttachmentIDs createFECAttachment(final long caseID,
    final String actionIDProperty, final Date receiptDate,
    final String documentType) throws AppException, InformationalException {

    final BDMFAAttachmentDetails createFAAttachment =
      new BDMFAAttachmentDetails();

    createFAAttachment.actionIDProperty = actionIDProperty;
    createFAAttachment.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      caseID;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      receiptDate;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Test FEC attachment";
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      caseID;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;

    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      FILE_DESCRIPTION;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      documentType;

    return BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(createFAAttachment);
  }

  /**
   * This Utility method helps creating a foreign application.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private BDMForeignApplicationKey createFA()
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerAPersonForTest("721440030");

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFE_IC_forTest(
      registrationResult.registrationIDDetails.concernRoleID);

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();

    return kBdmMaintainForeignEngagementCase
      .createForeignApplication(bdmCreateModifyFA);
  }

  /**
   * This Utility method helps registering person and create FEC case for given
   * Country
   *
   * @throws AppException
   * @throws InformationalException
   */
  private CreateIntegratedCaseResultAndMessages
    registerPersonAndcreateFECCaseforCountry(final String country)
      throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();
    registrationResult = registerAPersonForTest("103666400");

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID =
      registrationResult.registrationIDDetails.concernRoleID;

    details.countryCode = country;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmMaintainForeignEngagementCase.createFEIntegratedCase(details);
    // register person

    return createIntegratedCaseResultAndMessages;

  }

  /**
   * canceling/deleting an foreign engagement case
   * attachment.
   */
  @Test
  public void testcancelFECaseAttachment()
    throws AppException, InformationalException {

    final curam.core.facade.struct.CancelCaseAttachmentKey key =
      new curam.core.facade.struct.CancelCaseAttachmentKey();

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages icResult =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);
    // Create Attachemnt
    attachmentIDs = createFECAttachment(
      icResult.createCaseResult.integratedCaseID, BDMConstants.kSaveAndClose,
      Date.getCurrentDate(), DOCUMENTTYPE.DIRECT_DEPOSIT);

    // Read attachment
    bdmFAAttachmentDetails = kBdmMaintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    final ReadCaseAttachmentKey readCaseAttachmentKey =
      new ReadCaseAttachmentKey();
    readCaseAttachmentKey.readCaseAttachmentIn.attachmentID =
      attachmentIDs.attachmentID;
    readCaseAttachmentKey.readCaseAttachmentIn.caseAttachmentLinkID =
      attachmentIDs.caseAttachmentLinkID;
    final BDMReadCaseAttachmentDetails readAttachment =
      BDMCaseFactory.newInstance().readCaseAttachment(readCaseAttachmentKey);

    assertEquals(BDM_FILE_SOURCE.CLIENT, readAttachment.fileSource);

    final CancelCaseAttachmentKey cancelCaseAttachmentKey =
      new CancelCaseAttachmentKey();
    cancelCaseAttachmentKey.cancelCaseAttachmentKey.attachmentID =
      readCaseAttachmentKey.readCaseAttachmentIn.attachmentID;
    cancelCaseAttachmentKey.cancelCaseAttachmentKey.caseAttachmentLinkID =
      readCaseAttachmentKey.readCaseAttachmentIn.caseAttachmentLinkID;
    cancelCaseAttachmentKey.cancelCaseAttachmentKey.caseID =
      icResult.createCaseResult.integratedCaseID;

    kBdmMaintainForeignEngagementCase
      .cancelFECaseAttachment(cancelCaseAttachmentKey);

    final curam.core.intf.Attachment attachment =
      AttachmentFactory.newInstance();
    final AttachmentKey attachmentKey = new AttachmentKey();
    attachmentKey.attachmentID = attachmentIDs.attachmentID;
    final AttachmentDtls attachmentDtls = attachment.read(attachmentKey);

    assertTrue(attachmentDtls.statusCode.equals(RECORDSTATUS.CANCELLED));

  }

  /**
   * This Utility method to create person , FEC case and foreign application.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private BDMForeignApplicationKey createFAforCountry(final String country)
    throws AppException, InformationalException {

    PersonRegistrationResult registrationResult =
      new PersonRegistrationResult();

    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    // register person
    registrationResult = registerAPersonForTest("669831711");

    // create FE IC for the person
    createIntegratedCaseResultAndMessages = createAFECCaseforCountry(
      registrationResult.registrationIDDetails.concernRoleID, country);

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();
    bdmCreateModifyFA.consent = BDMYESNO.NO;

    return kBdmMaintainForeignEngagementCase
      .createForeignApplication(bdmCreateModifyFA);
  }

  /**
   * This is the utility method to register person for the test class.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private PersonRegistrationResult registerAPersonForTest(
    final String nineDigitSIN) throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "John";
    bdmPersonRegistrationDetails.dtls.surname = "Doe";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770102");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = nineDigitSIN;
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "207";
    addressFieldDetails.addressLine1 = "6511";
    addressFieldDetails.addressLine2 = "GILBER RD";
    addressFieldDetails.province = PROVINCETYPE.BRITISHCOLUMBIA;
    addressFieldDetails.city = "RICHMOND";
    addressFieldDetails.postalCode = "V7C 3V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    final BDMUtil bdmUtil = new BDMUtil();
    bdmUtil.createContactPreferenceEvidence(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMLANGUAGE.ENGLISHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    return registrationResult;

  }

  /**
   * test fetching the list of foreign offices for the selected SSA
   * country.
   */
  @Test
  public void getListOfForeignOffices()
    throws AppException, InformationalException {

    final BDMFACountryCodeKey key = new BDMFACountryCodeKey();
    key.countryCode = BDMSOURCECOUNTRY.US;

    final BDMListOfOffices list =
      kBdmMaintainForeignEngagementCase.getListOfForeignOffices(key);

    assertTrue(!list.bdmFO.isEmpty());

  }

  /**
   * Test fetch list of linked foreign applications with an
   * attachment.
   */
  @Test
  public void testgetFAListLinkedToAttachment()
    throws AppException, InformationalException {

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      kBdmMaintainForeignEngagementCase
        .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Create Attachemnt of type Foreign Application

    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kNext;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      Date.getCurrentDate();

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.filelocation =
      FILE_LOCATION;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.fileReference =
      FILE_REFERENCE;
    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      FILE_DESCRIPTION;

    bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      DOCUMENTTYPE.DIRECT_DEPOSIT;

    attachmentIDs = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment details from Step1
    bdmFAAttachmentDetails = kBdmMaintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Set data for foreign application
    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;
    setBDMCreateModifyFA(bdmCreateModifyFA);

    // Create FA
    final BDMForeignApplicationKey bdmForeignApplicationKey =
      kBdmMaintainForeignEngagementCase
        .createForeignApplication(bdmCreateModifyFA);

    // Link Attachment to FA
    bdmFAAttachmentDetails.selectedFAList =
      bdmForeignApplicationKey.fApplicationID + "";

    // Click Save linking to FA
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kSave;

    attachmentIDs = kBdmMaintainForeignEngagementCase
      .createFECaseAttachmentLinkingFA(bdmFAAttachmentDetails);

    BDMFAList list = new BDMFAList();
    final BDMFAAttachmentKey key = new BDMFAAttachmentKey();
    ////////
    key.attachmentID = attachmentIDs.attachmentID;

    list = kBdmMaintainForeignEngagementCase.getFAListLinkedToAttachment(key);

    assertTrue(!list.bdmFADetails.isEmpty());

    assertTrue(list.bdmFADetails.size() == 1);
    assertEquals(list.bdmFADetails.get(0).fApplicationID,
      bdmForeignApplicationKey.fApplicationID);

  }

  /**
   * Test getting country for a foreign engagement case.
   */
  @Test
  public void readCountryCode() throws AppException, InformationalException {

    BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();
    final CaseHeaderKey key = new CaseHeaderKey();

    // Register person Create FEc case
    final CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      registerPersonAndcreateFECCaseforCountry(BDMSOURCECOUNTRY.JAMAICA);

    key.caseID =
      createIntegratedCaseResultAndMessages.createCaseResult.integratedCaseID;

    bdmfeCaseDtls = kBdmMaintainForeignEngagementCase.readCountryCode(key);

    assertEquals(BDMSOURCECOUNTRY.JAMAICA, bdmfeCaseDtls.countryCode);

  }

  @Test
  public void listFAInterimCodeDetails()
    throws AppException, InformationalException {

    final BDMFAInterimCodeDetailsList list =
      kBdmMaintainForeignEngagementCase.listFAInterimCodeDetails();

    assertTrue(!list.bdmFAInterimCodeDetails.isEmpty());

  }

  /**
   * Test getting the foreign application history records.
   */
  @Test

  public void testListFAHistory()
    throws AppException, InformationalException {

    BDMFAList list = new BDMFAList();

    final BDMFAKey key = new BDMFAKey();

    final BDMForeignApplicationKey bdmForeignApplicationKey = createFA();

    key.fApplicationID = bdmForeignApplicationKey.fApplicationID;

    list = kBdmMaintainForeignEngagementCase.listFAHistory(key);

    assertTrue(list.bdmFADetails.size() == 1);

    assertEquals(
      CodetableUtil.getCodetableDescription(BDMFOREIGNAPPTYPE.TABLENAME,
        BDMFOREIGNAPPTYPE.INVALIDITY) + BDMConstants.kHiphen
        + list.bdmFADetails.get(0).faReferenceNumber,
      list.bdmFADetails.get(0).faApplTypeAndRefNumStr);
  }

  /**
   * Test getting the foreign application history records.
   */
  @Test

  public void testViewForeignApplicationHistorylistFAHistory()
    throws AppException, InformationalException {

    BDMFAList list = new BDMFAList();

    final BDMFAKey key = new BDMFAKey();

    final BDMFAHistoryKey bdmFAHistoryKey = new BDMFAHistoryKey();
    BDMFADetails bdmFADetails = new BDMFADetails();

    final BDMForeignApplicationKey bdmForeignApplicationKey = createFA();

    key.fApplicationID = bdmForeignApplicationKey.fApplicationID;

    list = kBdmMaintainForeignEngagementCase.listFAHistory(key);

    assertTrue(list.bdmFADetails.size() == 1);

    assertEquals(
      CodetableUtil.getCodetableDescription(BDMFOREIGNAPPTYPE.TABLENAME,
        BDMFOREIGNAPPTYPE.INVALIDITY) + BDMConstants.kHiphen
        + list.bdmFADetails.get(0).faReferenceNumber,
      list.bdmFADetails.get(0).faApplTypeAndRefNumStr);

    bdmFAHistoryKey.fAppHistoryID = list.bdmFADetails.get(0).fAppHistoryID;

    bdmFADetails = kBdmMaintainForeignEngagementCase
      .viewForeignApplicationHistory(bdmFAHistoryKey);

    assertEquals(bdmFADetails.fApplicationID,
      list.bdmFADetails.get(0).fApplicationID);

  }

  /**
   * list of foreign identifier for the
   * selected person concern role ID.
   */
  @Test
  public void testGetListOfForeignIdentifiers()
    throws AppException, InformationalException {

    final BDMEvidenceUtilsTest evidenceUtilsTest = new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    // create new Identification evidence with some data ,
    final EIEvidenceKey str =
      evidenceUtilsTest.createForeignIdentificationEvidence(pdcPersonDetails,
        "F1234567", CONCERNROLEALTERNATEID.BDM_FOREIGN_IDENTIFIER, false,
        BDMSOURCECOUNTRY.US);

    final BDMFAConcernRoleKey key = new BDMFAConcernRoleKey();
    key.concernRoleID = pdcPersonDetails.concernRoleID;

    final BDMListOfForeignIDs bdmListOfForeignIDs =
      kBdmMaintainForeignEngagementCase.getListOfForeignIdentifiers(key);

    assertTrue(!bdmListOfForeignIDs.bdmFId.isEmpty());

    assertEquals("F1234567", bdmListOfForeignIDs.bdmFId.get(0).alternateID);
  }

  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();
    pdcPersonDetails.assign(personRegistrationDetails);
    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  /**
   * This is the utility method to register person with Foreign ID the test
   * class.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private PersonRegistrationResult registerAPersonwithForeignID()
    throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "John";
    bdmPersonRegistrationDetails.dtls.surname = "Doe";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770102");
    bdmPersonRegistrationDetails.dtls.foreignResidencyCountryCode = "IE";
    bdmPersonRegistrationDetails.dtls.alternateIDTypeCodeOpt =
      CONCERNROLEALTERNATEID.BDM_FOREIGN_IDENTIFIER;

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "207";
    addressFieldDetails.addressLine1 = "6511";
    addressFieldDetails.addressLine2 = "GILBER RD";
    addressFieldDetails.province = PROVINCETYPE.BRITISHCOLUMBIA;
    addressFieldDetails.city = "RICHMOND";
    addressFieldDetails.postalCode = "V7C 3V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    bdmPersonRegistrationDetails.dtls.addressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.mailingAddressData =
      otherAddressData.addressData;

    bdmPersonRegistrationDetails.dtls.registrationDate =
      Date.getCurrentDate();

    final PersonRegistrationDetails details = new PersonRegistrationDetails();
    details.personRegistrationDetails = bdmPersonRegistrationDetails.dtls;

    final PersonRegistrationResult registrationResult =
      PersonFactory.newInstance().register(details);

    final BDMUtil bdmUtil = new BDMUtil();
    bdmUtil.createContactPreferenceEvidence(
      registrationResult.registrationIDDetails.concernRoleID,
      BDMLANGUAGE.ENGLISHL, BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    return registrationResult;

  }

  /**
   * This is a utility method to create a FE Integrated Case for Test.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private CreateIntegratedCaseResultAndMessages createAFE_IC_forTest(
    final long concernRoleID) throws AppException, InformationalException {

    // BEGIN: create FE IC for the person
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = BDMSOURCECOUNTRY.IRELAND;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmMaintainForeignEngagementCase.createFEIntegratedCase(details);
    // END: create FE IC for the person

    return createIntegratedCaseResultAndMessages;
  }

  /**
   * This is a utility method to create a FE Integrated Case for Test.
   *
   * @throws AppException
   * @throws InformationalException
   */
  private CreateIntegratedCaseResultAndMessages
    createAFECCaseforCountry(final long concernRoleID, final String country)
      throws AppException, InformationalException {

    // BEGIN: create FE IC for the person
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = country;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages =
      kBdmMaintainForeignEngagementCase.createFEIntegratedCase(details);
    // END: create FE IC for the person

    return createIntegratedCaseResultAndMessages;
  }

  /**
   * Method to get the registration details for registering the external
   * party(Non SSACountry).
   *
   * @return Returns registration details for default registration
   */
  public ExternalPartyRegistrationResult
    registerExternalPartyRegistrationDetails(
      final ExternalPartyRegistrationDetails externalPartyRegistrationDetails)
      throws AppException, InformationalException {

    final ExternalPartyRegistrationDetails details =
      new ExternalPartyRegistrationDetails();

    final ExternalPartyRegistrationResult registrationResult =
      ExternalPartyFactory.newInstance()
        .registerExternalParty(externalPartyRegistrationDetails);
    return registrationResult;
  }

  /**
   * Method to get the registration details for registering the external party.
   *
   * @return Returns registration details for default registration
   */
  public ExternalPartyRegistrationDetails
    getExternalPartyRegistrationDetails_Default(final String countryName)
      throws AppException, InformationalException {

    final ExternalPartyRegistrationDetails extPartyRegistrationDetails =
      new ExternalPartyRegistrationDetails();

    extPartyRegistrationDetails.externalPartyRegistrationDetails.externalPartyDtls.name =
      countryName;

    extPartyRegistrationDetails.externalPartyRegistrationDetails.externalPartyDtls.type =
      EXTERNALPARTYTYPE.SSACOUNTRY;
    extPartyRegistrationDetails.externalPartyRegistrationDetails.registrationDate =
      Date.getCurrentDate();

    final OtherAddressData otherAddressData =
      this.getInternationAddressForExtParty();

    extPartyRegistrationDetails.externalPartyRegistrationDetails.addressData =
      otherAddressData.addressData;

    return extPartyRegistrationDetails;
  }

  /**
   * Method to get the address data for registering the service supplier.
   *
   * @return Returns address data.
   */
  public OtherAddressData getInternationAddressForExtParty()
    throws AppException, InformationalException {

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final BDMAddressFormatINTL bdmAddressFormatINTLobj =
      new BDMAddressFormatINTL();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "116";
    addressFieldDetails.addressLine1 = "8614";
    addressFieldDetails.addressLine2 = "Catalpa Ave";
    addressFieldDetails.stateProvince = "IL";
    addressFieldDetails.city = "Chicago";
    addressFieldDetails.zipCode = "60656";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.IRELAND;
    final OtherAddressData otherAddressData =
      bdmAddressFormatINTLobj.parseFieldsToData(addressFieldDetails);
    return otherAddressData;
  }

  protected ExternalPartyOfficeKey createExternalPartyOffice(
    final long concernRoleId) throws AppException, InformationalException {

    final ExternalPartyOfficeDetails officeDetails =
      new ExternalPartyOfficeDetails();
    officeDetails.externalPartyOfficeDtls.concernRoleID = concernRoleId;
    officeDetails.externalPartyOfficeDtls.name = "office";
    officeDetails.externalPartyOfficeDtls.recordStatus = RECORDSTATUS.NORMAL;
    officeDetails.externalPartyOfficeDtls.startDate = Date.getCurrentDate();
    officeDetails.addressDtls.addressData =
      "1\n" + "1\n" + "BDMINTL\n" + "IE\n" + "1\n" + "1\n" + "POBOXNO=\n"
        + "APT=\n" + "ADD1=1153\n" + "ADD2=Mole Street\n" + "CITY=Midway\n"
        + "PROV=\n" + "STATEPROV=\n" + "BDMSTPROVX=UT\n" + "COUNTRY=IE\n"
        + "POSTCODE=\n" + "ZIP=\n" + "BDMZIPX=60656\n" + "BDMUNPARSE=";

    // Create the external party office

    return ExternalPartyOfficeFactory.newInstance()
      .createExternalPartyOffice(officeDetails);

  }

  private BDMTaskDtls createBDMTaskDtls(final long taskID)
    throws AppException, InformationalException {

    final BDMTaskDtls bdmTaskCreateDetails = new BDMTaskDtls();

    bdmTaskCreateDetails.taskID = taskID;
    bdmTaskCreateDetails.type = BDMTASKTYPE.BDMFBADDRESS;
    bdmTaskCreateDetails.versionNo = 1;

    BDMTaskFactory.newInstance().insert(bdmTaskCreateDetails);
    return bdmTaskCreateDetails;
  }

}
