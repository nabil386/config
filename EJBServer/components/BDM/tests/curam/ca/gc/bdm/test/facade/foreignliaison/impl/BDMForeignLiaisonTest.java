package curam.ca.gc.bdm.test.facade.foreignliaison.impl;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.address.impl.BDMAddressFormatINTL;
import curam.ca.gc.bdm.codetable.BDMATTACHMENTLINKTYPE;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPDELETEREASON;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.codetable.BDMLIAISONCHECKLIST;
import curam.ca.gc.bdm.codetable.BDMLIAISONDELREASON;
import curam.ca.gc.bdm.codetable.BDMLIAISONDIRECTION;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.codetable.BDM_FILE_SOURCE;
import curam.ca.gc.bdm.entity.fact.BDMFEAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonHistFactory;
import curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonHistDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonHistDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonKey;
import curam.ca.gc.bdm.entity.struct.BDMReadByFrgnLiasnIDKey;
import curam.ca.gc.bdm.entity.struct.BDMReadFLByCaseIDKey;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMAttachmentIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMCreateModifyFA;
import curam.ca.gc.bdm.facade.fec.struct.BDMDeleteFADetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAAttachmentDetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFADetails;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAList;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.foreignliaison.fact.BDMForeignLiaisonFactory;
import curam.ca.gc.bdm.facade.foreignliaison.intf.BDMForeignLiaison;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMFEC;
import curam.ca.gc.bdm.sl.fec.fact.BDMMaintainForeignEngagementCaseFactory;
import curam.ca.gc.bdm.sl.foreignliaison.impl.BDMMaintainForeignLiaison;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFECaseIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFLAttachmentDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMForeignLiaisonDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnAppDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnAttLnkIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnDeleteKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnHistIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnRefDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnViewDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnViewDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnWizardDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMLiasnChecklistCodeDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMReadDetailsKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMReadDispDetails;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.ExternalPartyFactory;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.ExternalPartyRegistrationDetails;
import curam.core.facade.struct.ExternalPartyRegistrationResult;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.AttachmentFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.intf.Attachment;
import curam.core.intf.UniqueID;
import curam.core.sl.entity.struct.ExternalPartyOfficeKey;
import curam.core.sl.fact.ExternalPartyOfficeFactory;
import curam.core.sl.struct.ExternalPartyOfficeDetails;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AttachmentDtls;
import curam.core.struct.AttachmentKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.UniqueIDKeySet;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.wizardpersistence.impl.WizardPersistentState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BDMForeignLiaisonTest extends CuramServerTestJUnit4 {

  BDMForeignLiaison bdmForeignLiaisonFacade = null;

  BDMMaintainForeignLiaison maintainForeignLiaisonObj = null;

  curam.ca.gc.bdm.sl.fec.intf.BDMMaintainForeignEngagementCase maintainForeignEngagementCase =
    null;

  curam.ca.gc.bdm.facade.fec.intf.BDMForeignEngagementCase foreignEngagementCaseObj =
    null;

  private static String FILE_LOCATION = "FILE LOCATION";

  private static String FILE_REFERENCE = "FILE REFERENCE";

  private static String FILE_DESCRIPTION = "FILE DESCRIPTION";

  @Before
  public void setUp() throws AppException, InformationalException {

    bdmForeignLiaisonFacade = BDMForeignLiaisonFactory.newInstance();
    maintainForeignLiaisonObj = new BDMMaintainForeignLiaison();
    maintainForeignEngagementCase =
      BDMMaintainForeignEngagementCaseFactory.newInstance();
    foreignEngagementCaseObj = BDMForeignEngagementCaseFactory.newInstance();

  }

  @Test
  public void testReadForeignLiaisonWizardDetails()
    throws AppException, InformationalException {

    BDMFrgnLiasnWizardDetails details = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    assertNotEquals(0, details.wizardStateID);

    final long wizID = details.wizardStateID;

    final WizardStateID existingWizID = new WizardStateID();
    existingWizID.wizardStateID = wizID;

    details =
      bdmForeignLiaisonFacade.readForeignLiaisonWizardDetails(existingWizID);

    assertEquals(wizID, details.wizardStateID);
  }

  @Test
  public void testCreateForeignLiaisonFromWizard_Step1SaveClose()
    throws AppException, InformationalException {

    // Prepare a new FEC case
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    // load wizard Step 1
    final BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final long wizID = readWizDetails.wizardStateID;

    assertNotEquals(0, wizID);

    // add details in Step 1 and click save and close
    readWizDetails.dtls.caseID = icResult.createCaseResult.integratedCaseID;
    readWizDetails.dtls.receiveDate = Date.getCurrentDate();
    readWizDetails.dtls.direction = BDMLIAISONDIRECTION.INCOMING;
    readWizDetails.dtls.foreignIdntifier = "1000012";
    readWizDetails.dtls.fOfficeID = 123000;
    readWizDetails.dtls.frgnLiasnIDTabList = "";
    readWizDetails.dtls.frgnAppIDTabList = "";
    readWizDetails.dtls.comments = "Test creating a foreign liaison";

    readWizDetails.actionIDProperty = BDMConstants.kStep1SaveAndClose;

    bdmForeignLiaisonFacade.createForeignLiaisonFromWizard(readWizDetails);

    // check result
    final BDMReadFLByCaseIDKey caseIDKey = new BDMReadFLByCaseIDKey();
    caseIDKey.caseID = icResult.createCaseResult.integratedCaseID;
    final BDMForeignLiaisonDtlsList flList =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .readForeignLiaisonByCaseID(caseIDKey);

    assertEquals(1, flList.dtls.size());

    assertEquals(icResult.createCaseResult.integratedCaseID,
      flList.dtls.get(0).caseID);
    assertEquals(BDMLIAISONDIRECTION.INCOMING, flList.dtls.get(0).direction);

    final BDMReadByFrgnLiasnIDKey flIDKey = new BDMReadByFrgnLiasnIDKey();
    flIDKey.foreignLiaisonID = flList.dtls.get(0).foreignLiaisonID;
    final BDMForeignLiaisonHistDtlsList flHistList =
      BDMForeignLiaisonHistFactory.newInstance()
        .readByForeignLiaisonID(flIDKey);

    assertEquals(1, flHistList.dtls.size());
  }

  @Test
  public void testCreateForeignLiaisonFromWizard_Step1Next()
    throws AppException, InformationalException {

    // Prepare a new FEC case
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    // load wizard Step 1
    final BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final long wizID = readWizDetails.wizardStateID;

    assertNotEquals(0, wizID);

    // add details in Step 1 and click save and close
    readWizDetails.dtls.caseID = icResult.createCaseResult.integratedCaseID;
    readWizDetails.dtls.receiveDate = Date.getCurrentDate();
    readWizDetails.dtls.direction = BDMLIAISONDIRECTION.INCOMING;
    readWizDetails.dtls.foreignIdntifier = "1000012";
    readWizDetails.dtls.fOfficeID = 123000;
    readWizDetails.dtls.frgnLiasnIDTabList = "";
    readWizDetails.dtls.frgnAppIDTabList = "";
    readWizDetails.dtls.comments = "Test creating a foreign liaison";

    readWizDetails.actionIDProperty = BDMConstants.kStep1Next;

    bdmForeignLiaisonFacade.createForeignLiaisonFromWizard(readWizDetails);

    // check result
    final BDMReadFLByCaseIDKey caseIDKey = new BDMReadFLByCaseIDKey();
    caseIDKey.caseID = icResult.createCaseResult.integratedCaseID;
    final BDMForeignLiaisonDtlsList flList =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .readForeignLiaisonByCaseID(caseIDKey);

    assertEquals(0, flList.dtls.size());
  }

  @Test
  public void testCreateFLChecklistFromWizard_Step2Next()
    throws AppException, InformationalException {

    // Prepare a new FEC case
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    // load wizard Step 1
    final BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final long wizID = readWizDetails.wizardStateID;

    assertNotEquals(0, wizID);

    // add details in Step 1 and click next
    readWizDetails.dtls.caseID = icResult.createCaseResult.integratedCaseID;
    readWizDetails.dtls.receiveDate = Date.getCurrentDate();
    readWizDetails.dtls.direction = BDMLIAISONDIRECTION.INCOMING;
    readWizDetails.dtls.foreignIdntifier = "1000012";
    readWizDetails.dtls.fOfficeID = 123000;
    readWizDetails.dtls.frgnLiasnIDTabList = "";
    readWizDetails.dtls.frgnAppIDTabList = "";
    readWizDetails.dtls.comments = "Test creating a foreign liaison";

    readWizDetails.actionIDProperty = BDMConstants.kStep1Next;

    bdmForeignLiaisonFacade.createForeignLiaisonFromWizard(readWizDetails);

    // on Step 2, click Next
    final BDMFrgnLiasnWizardDetails step2Key =
      new BDMFrgnLiasnWizardDetails();
    step2Key.wizardStateID = wizID;
    step2Key.actionIDProperty = BDMConstants.kStep2Next;
    step2Key.dtls.flChkLstCdTabList = "LC01\tLC05\tLC16";
    // step2Key.dtls.lisnChkLstOthrDesc = "Test";
    bdmForeignLiaisonFacade.createFLChecklistFromWizard(step2Key);

    // check result
    final BDMReadFLByCaseIDKey caseIDKey = new BDMReadFLByCaseIDKey();
    caseIDKey.caseID = icResult.createCaseResult.integratedCaseID;
    final BDMForeignLiaisonDtlsList flList =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .readForeignLiaisonByCaseID(caseIDKey);

    assertEquals(0, flList.dtls.size());
  }

  @Test
  public void testCreateFLChecklistFromWizard_Step2SaveAndClose()
    throws AppException, InformationalException {

    // Prepare a new FEC case
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    // load wizard Step 1
    final BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final long wizID = readWizDetails.wizardStateID;

    assertNotEquals(0, wizID);

    // add details in Step 1 and click next
    readWizDetails.dtls.caseID = icResult.createCaseResult.integratedCaseID;
    readWizDetails.dtls.receiveDate = Date.getCurrentDate();
    readWizDetails.dtls.direction = BDMLIAISONDIRECTION.INCOMING;
    readWizDetails.dtls.foreignIdntifier = "1000012";
    readWizDetails.dtls.fOfficeID = 123000;
    readWizDetails.dtls.frgnLiasnIDTabList = "";
    readWizDetails.dtls.frgnAppIDTabList = "";
    readWizDetails.dtls.comments = "Test creating a foreign liaison";

    readWizDetails.actionIDProperty = BDMConstants.kStep1Next;

    bdmForeignLiaisonFacade.createForeignLiaisonFromWizard(readWizDetails);

    // on Step 2, click Save and Close
    final BDMFrgnLiasnWizardDetails step2Key =
      new BDMFrgnLiasnWizardDetails();
    step2Key.wizardStateID = wizID;
    step2Key.actionIDProperty = BDMConstants.kStep2SaveAndClose;
    step2Key.dtls.flChkLstCdTabList = "LC01\tLC05\tLC16";

    // step2Key.dtls.lisnChkLstOthrDesc = "Test";

    bdmForeignLiaisonFacade.createFLChecklistFromWizard(step2Key);

    // check result
    final BDMReadFLByCaseIDKey caseIDKey = new BDMReadFLByCaseIDKey();
    caseIDKey.caseID = icResult.createCaseResult.integratedCaseID;
    final BDMForeignLiaisonDtlsList flList =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .readForeignLiaisonByCaseID(caseIDKey);

    assertEquals(1, flList.dtls.size());
    assertEquals(step2Key.dtls.flChkLstCdTabList,
      flList.dtls.get(0).liaisonChkStrList);
  }

  @Test
  public void testCreateFLAttachmentsFromWizard_Step3Back()
    throws AppException, InformationalException {

    // Prepare a new FEC case
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final BDMAttachmentIDs attachmentIDs = this.createFECAttachment(
      icResult.createCaseResult.integratedCaseID, getToday());

    // load wizard page 1
    final BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final long wizID = readWizDetails.wizardStateID;

    assertNotEquals(0, wizID);

    // add details in Step 1 and click next
    readWizDetails.dtls.caseID = icResult.createCaseResult.integratedCaseID;
    readWizDetails.dtls.receiveDate = Date.getCurrentDate();
    readWizDetails.dtls.direction = BDMLIAISONDIRECTION.INCOMING;
    readWizDetails.dtls.foreignIdntifier = "1000012";
    readWizDetails.dtls.fOfficeID = 123000;
    readWizDetails.dtls.frgnLiasnIDTabList = "";
    readWizDetails.dtls.frgnAppIDTabList = "";
    readWizDetails.dtls.comments = "Test creating a foreign liaison";

    readWizDetails.actionIDProperty = BDMConstants.kStep1Next;

    bdmForeignLiaisonFacade.createForeignLiaisonFromWizard(readWizDetails);

    // on Step 2, click Next
    final BDMFrgnLiasnWizardDetails step2Key =
      new BDMFrgnLiasnWizardDetails();
    step2Key.wizardStateID = wizID;
    step2Key.actionIDProperty = BDMConstants.kStep2Next;
    step2Key.dtls.flChkLstCdTabList = "LC01\tLC05\tLC16";
    // step2Key.dtls.lisnChkLstOthrDesc = "Test";
    bdmForeignLiaisonFacade.createFLChecklistFromWizard(step2Key);

    // on Step 3, click Back
    final BDMFrgnLiasnWizardDetails step3Key =
      new BDMFrgnLiasnWizardDetails();
    step3Key.wizardStateID = wizID;
    step3Key.actionIDProperty = BDMConstants.kStep3Back;
    step3Key.dtls.flSelectedAttIDList = "" + attachmentIDs.attachmentID;
    bdmForeignLiaisonFacade.createFLAttachmentsFromWizard(step3Key);

    // check result
    final BDMReadFLByCaseIDKey caseIDKey = new BDMReadFLByCaseIDKey();
    caseIDKey.caseID = icResult.createCaseResult.integratedCaseID;
    final BDMForeignLiaisonDtlsList flList =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .readForeignLiaisonByCaseID(caseIDKey);

    assertEquals(0, flList.dtls.size());
  }

  @Test
  public void testCreateFLAttachmentsFromWizard_Step3Save()
    throws AppException, InformationalException {

    // Prepare a new FEC case
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final BDMAttachmentIDs attachmentIDs = this.createFECAttachment(
      icResult.createCaseResult.integratedCaseID, getToday());

    // load wizard page 1
    final BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final long wizID = readWizDetails.wizardStateID;

    assertNotEquals(0, wizID);

    // add details in Step 1 and click next
    readWizDetails.dtls.caseID = icResult.createCaseResult.integratedCaseID;
    readWizDetails.dtls.receiveDate = Date.getCurrentDate();
    readWizDetails.dtls.direction = BDMLIAISONDIRECTION.INCOMING;
    readWizDetails.dtls.foreignIdntifier = "1000012";
    readWizDetails.dtls.fOfficeID = 123000;
    readWizDetails.dtls.frgnLiasnIDTabList = "";
    readWizDetails.dtls.frgnAppIDTabList = "";
    readWizDetails.dtls.comments = "Test creating a foreign liaison";

    readWizDetails.actionIDProperty = BDMConstants.kStep1Next;

    bdmForeignLiaisonFacade.createForeignLiaisonFromWizard(readWizDetails);

    // on Step 2, click Next
    final BDMFrgnLiasnWizardDetails step2Key =
      new BDMFrgnLiasnWizardDetails();
    step2Key.wizardStateID = wizID;
    step2Key.actionIDProperty = BDMConstants.kStep2Next;
    step2Key.dtls.flChkLstCdTabList = "LC01\tLC05\tLC16";
    // step2Key.dtls.lisnChkLstOthrDesc = "Test";
    bdmForeignLiaisonFacade.createFLChecklistFromWizard(step2Key);

    // on Step 3, click Back
    final BDMFrgnLiasnWizardDetails step3Key =
      new BDMFrgnLiasnWizardDetails();
    step3Key.wizardStateID = wizID;
    step3Key.actionIDProperty = BDMConstants.kSave;
    step3Key.dtls.flSelectedAttIDList = "" + attachmentIDs.attachmentID;
    bdmForeignLiaisonFacade.createFLAttachmentsFromWizard(step3Key);

    // check result
    final BDMReadFLByCaseIDKey caseIDKey = new BDMReadFLByCaseIDKey();
    caseIDKey.caseID = icResult.createCaseResult.integratedCaseID;
    final BDMForeignLiaisonDtlsList flList =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .readForeignLiaisonByCaseID(caseIDKey);

    assertEquals(1, flList.dtls.size());

    final BDMFEAttachmentLinkKeyStruct1 attachmentLinkKeyStruct1 =
      new BDMFEAttachmentLinkKeyStruct1();
    attachmentLinkKeyStruct1.relatedID = flList.dtls.get(0).foreignLiaisonID;

    assertEquals(1, BDMFEAttachmentLinkFactory.newInstance()
      .readByRelatedID(attachmentLinkKeyStruct1).dtls.size());
  }

  @Test
  public void testModifyForeignLiaisonFromWizard_Step1Next()
    throws AppException, InformationalException {

    // Prepare a new FL
    final BDMForeignLiaisonDtls flDtls = this.createForeignLiaison();

    // load wizard page 1
    final BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final long wizID = readWizDetails.wizardStateID;

    assertNotEquals(0, wizID);

    // Edit details in page 1 and click next
    final BDMFrgnLiasnWizardDetails key = new BDMFrgnLiasnWizardDetails();
    key.wizardStateID = wizID;
    key.dtls.caseID = flDtls.caseID;
    key.dtls.foreignLiaisonID = flDtls.foreignLiaisonID;
    key.dtls.receiveDate = flDtls.receiveDate;
    key.dtls.direction = flDtls.direction;
    key.dtls.foreignIdntifier = flDtls.foreignIdntifier;
    key.dtls.fOfficeID = flDtls.fOfficeID;
    key.dtls.frgnLiasnIDTabList = flDtls.liaisonChkStrList;
    key.dtls.frgnAppIDTabList = flDtls.foreignAppIDs;
    key.dtls.bessInd = flDtls.bessInd;
    key.dtls.comments = "Test editing a foreign liaison";

    key.actionIDProperty = BDMConstants.kStep1Next;

    bdmForeignLiaisonFacade.modifyForeignLiaisonFromWizard(key);

    // check result
    final BDMForeignLiaisonKey flKey = new BDMForeignLiaisonKey();
    flKey.foreignLiaisonID = flDtls.foreignLiaisonID;
    final BDMForeignLiaisonDtls newFlDtls =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .read(flKey);

    assertNotEquals("Test editing a foreign liaison", newFlDtls.comments);

  }

  @Test
  public void testModifyForeignLiaisonFromWizard_Step1SaveClose()
    throws AppException, InformationalException {

    // Prepare a new FL
    final BDMForeignLiaisonDtls flDtls = this.createForeignLiaison();

    // load wizard page 1
    final BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final long wizID = readWizDetails.wizardStateID;

    assertNotEquals(0, wizID);

    // Edit details in page 1 and click next
    final BDMFrgnLiasnWizardDetails key = new BDMFrgnLiasnWizardDetails();
    key.wizardStateID = wizID;
    key.dtls.caseID = flDtls.caseID;
    key.dtls.foreignLiaisonID = flDtls.foreignLiaisonID;
    key.dtls.receiveDate = flDtls.receiveDate;
    key.dtls.direction = flDtls.direction;
    key.dtls.foreignIdntifier = flDtls.foreignIdntifier;
    key.dtls.fOfficeID = flDtls.fOfficeID;
    key.dtls.frgnLiasnIDTabList = flDtls.liaisonChkStrList;
    key.dtls.frgnAppIDTabList = flDtls.foreignAppIDs;
    key.dtls.bessInd = flDtls.bessInd;
    key.dtls.comments = "Test editing a foreign liaison";

    key.actionIDProperty = BDMConstants.kStep1SaveAndClose;

    bdmForeignLiaisonFacade.modifyForeignLiaisonFromWizard(key);

    // check result
    final BDMForeignLiaisonKey flKey = new BDMForeignLiaisonKey();
    flKey.foreignLiaisonID = flDtls.foreignLiaisonID;
    final BDMForeignLiaisonDtls newFlDtls =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .read(flKey);

    assertEquals("Test editing a foreign liaison", newFlDtls.comments);
  }

  @Test
  public void testDeleteForeignLiaison_HappyPath()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls foreignLiaisonDtls = createForeignLiaison();

    final BDMFrgnLiasnDeleteKey key = new BDMFrgnLiasnDeleteKey();
    key.foreignLiaisonID = foreignLiaisonDtls.foreignLiaisonID;
    key.versionNo = foreignLiaisonDtls.versionNo;

    bdmForeignLiaisonFacade.deleteForeignLiaison(key);

    final BDMForeignLiaisonKey foreignLiaisonKey = new BDMForeignLiaisonKey();
    foreignLiaisonKey.foreignLiaisonID = key.foreignLiaisonID;

    // assert the status change
    final BDMForeignLiaisonDtls dtlsInDB =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .read(foreignLiaisonKey);

    assertEquals(RECORDSTATUS.CANCELLED, dtlsInDB.recordStatus);
    assertEquals(BDMLIAISONDELREASON.ENTEREDINERROR, dtlsInDB.deleteReason);
  }

  @Test
  public void testDeleteForeignLiaison_FailOnAlradyDeleted()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls foreignLiaisonDtls = createForeignLiaison();

    final BDMFrgnLiasnDeleteKey key = new BDMFrgnLiasnDeleteKey();
    key.foreignLiaisonID = foreignLiaisonDtls.foreignLiaisonID;
    key.versionNo = foreignLiaisonDtls.versionNo;

    bdmForeignLiaisonFacade.deleteForeignLiaison(key);

    // delete again

    try {
      bdmForeignLiaisonFacade.deleteForeignLiaison(key);
      fail("Should get a validation error");
    } catch (final InformationalException ae) {
      assertEquals(
        BDMFEC.ERR_FRGN_LIASN_RECORD_ALREADY_DELETED.getMessageText(),
        ae.getMessage());
    }

  }

  @Test
  public void testDeleteForeignLiaison_FailOnRelatedFl()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls foreignLiaisonDtls =
      createForeignLiaisonWithRelatedFl();

    final BDMFrgnLiasnDeleteKey key = new BDMFrgnLiasnDeleteKey();
    key.foreignLiaisonID = foreignLiaisonDtls.foreignLiaisonID;
    key.versionNo = foreignLiaisonDtls.versionNo;

    try {
      bdmForeignLiaisonFacade.deleteForeignLiaison(key);
      fail("Should get a validation error");
    } catch (final InformationalException ae) {
      assertEquals(
        BDMFEC.ERR_FRGN_LIASN_RECORD_CANNOT_BE_DELETED.getMessageText(),
        ae.getMessage());
    }

  }

  @Test
  public void testDeleteForeignLiaison_SuccessOnCanceledRelatedFl()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls foreignLiaisonDtls =
      createForeignLiaisonWithCanceledRelatedFl();

    final BDMFrgnLiasnDeleteKey key = new BDMFrgnLiasnDeleteKey();
    key.foreignLiaisonID = foreignLiaisonDtls.foreignLiaisonID;
    key.versionNo = foreignLiaisonDtls.versionNo;

    bdmForeignLiaisonFacade.deleteForeignLiaison(key);

    final BDMForeignLiaisonKey foreignLiaisonKey = new BDMForeignLiaisonKey();
    foreignLiaisonKey.foreignLiaisonID = key.foreignLiaisonID;

    // assert the status change
    final BDMForeignLiaisonDtls dtlsInDB =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .read(foreignLiaisonKey);

    assertEquals(RECORDSTATUS.CANCELLED, dtlsInDB.recordStatus);
    assertEquals(BDMLIAISONDELREASON.ENTEREDINERROR, dtlsInDB.deleteReason);
  }

  private BDMForeignLiaisonDtls createForeignLiaison()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final BDMForeignApplicationKey faKey =
      createFA(icResult.createCaseResult.integratedCaseID);

    final BDMForeignLiaisonDtls flDtls = new BDMForeignLiaisonDtls();
    flDtls.foreignLiaisonID = UniqueIDFactory.newInstance().getNextID();
    flDtls.bessInd = false;
    flDtls.caseID = icResult.createCaseResult.integratedCaseID;
    flDtls.comments = "Test creating a foreign liaison";
    flDtls.direction = BDMLIAISONDIRECTION.INCOMING;
    flDtls.receiveDate = Date.getCurrentDate();
    flDtls.recordStatus = RECORDSTATUS.NORMAL;
    flDtls.liaisonChkStrList = "LC01\tLC05\tLC16";
    flDtls.foreignAppIDs = "" + faKey.fApplicationID;
    curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
      .insert(flDtls);

    return flDtls;
  }

  private BDMForeignLiaisonDtls createForeignLiaisonUS(final String country)
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFECforCountry(person.registrationIDDetails.concernRoleID,
        country);

    final BDMForeignApplicationKey faKey =
      createFA(icResult.createCaseResult.integratedCaseID);

    final ExternalPartyRegistrationDetails externalPartyRegistrationDetails =
      getExternalPartyRegistrationDetails_Default(country);

    final ExternalPartyRegistrationResult externalPartyRegistrationResult =
      registerExternalPartyRegistrationDetails(
        externalPartyRegistrationDetails);

    final ExternalPartyOfficeKey externalPartyOfficeKey =
      createExternalPartyOffice(
        externalPartyRegistrationResult.externalPartyRegistrationIDDetails.concernRoleID);
    return createForeignLiaisonDtls(
      icResult.createCaseResult.integratedCaseID, faKey,
      externalPartyOfficeKey.externalPartyOfficeID);
  }

  private BDMForeignLiaisonDtls createForeignLiaisonDtls(final long caseID,
    final BDMForeignApplicationKey faKey, final long officeID)
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls flDtls = new BDMForeignLiaisonDtls();
    flDtls.foreignLiaisonID = UniqueIDFactory.newInstance().getNextID();
    flDtls.bessInd = true;
    flDtls.caseID = caseID;
    flDtls.comments = "Test creating a foreign liaison";
    flDtls.direction = BDMLIAISONDIRECTION.INCOMING;
    flDtls.receiveDate = Date.getCurrentDate();
    flDtls.recordStatus = RECORDSTATUS.NORMAL;
    flDtls.liaisonChkStrList = "LC01\tLC05\tLC16";
    flDtls.foreignAppIDs = "" + faKey.fApplicationID;
    flDtls.fOfficeID = officeID;

    curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
      .insert(flDtls);

    return flDtls;
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
    officeDetails.externalPartyOfficeDtls.externalPartyOfficeID =
      UniqueIDFactory.newInstance().getNextID();

    // Create the external party office

    return ExternalPartyOfficeFactory.newInstance()
      .createExternalPartyOffice(officeDetails);

  }

  private BDMForeignLiaisonHistDtls createForeignLiaisonHistory(
    final Long foreignLiaisonID) throws AppException, InformationalException {

    final BDMForeignLiaisonKey key = new BDMForeignLiaisonKey();
    key.foreignLiaisonID = foreignLiaisonID;
    final BDMForeignLiaisonDtls liasnDtls =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .read(key);

    final curam.ca.gc.bdm.entity.intf.BDMForeignLiaisonHist flHistObj =
      BDMForeignLiaisonHistFactory.newInstance();

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = BDMConstants.kBDMDEFKEYSET;

    final BDMForeignLiaisonHistDtls flHistDtls =
      new BDMForeignLiaisonHistDtls();
    flHistDtls.assign(liasnDtls);
    flHistDtls.frgnLsnHstID = uniqueIDObj.getNextIDFromKeySet(uniqueIDKeySet);

    flHistObj.insert(flHistDtls);

    return flHistDtls;
  }

  private CreateIntegratedCaseResultAndMessages createFEC(
    final long concernRoleID) throws AppException, InformationalException {

    // BEGIN: create FE IC for the person
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = BDMSOURCECOUNTRY.IRELAND;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages = BDMForeignEngagementCaseFactory
      .newInstance().createFEIntegratedCase(details);
    // END: create FE IC for the person

    return createIntegratedCaseResultAndMessages;
  }

  private CreateIntegratedCaseResultAndMessages
    createFECforCountry(final long concernRoleID, final String countryCode)
      throws AppException, InformationalException {

    // BEGIN: create FE IC for the person
    CreateIntegratedCaseResultAndMessages createIntegratedCaseResultAndMessages =
      new CreateIntegratedCaseResultAndMessages();

    final BDMFECaseDetails details = new BDMFECaseDetails();
    details.concernRoleID = concernRoleID;

    details.countryCode = countryCode;

    details.priorityCode = CASEPRIORITY.HIGH;

    createIntegratedCaseResultAndMessages = BDMForeignEngagementCaseFactory
      .newInstance().createFEIntegratedCase(details);
    // END: create FE IC for the person

    return createIntegratedCaseResultAndMessages;
  }

  private PersonRegistrationResult registerPerson()
    throws AppException, InformationalException {

    // BEGIN: REGISTER PERSON
    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = "Save";

    final BDMPersonRegistrationDetails bdmPersonRegistrationDetails =
      new BDMPersonRegistrationDetails();
    bdmPersonRegistrationDetails.dtls.firstForename = "Homer";
    bdmPersonRegistrationDetails.dtls.surname = "Simpson";
    bdmPersonRegistrationDetails.dtls.sex = "SX1";
    bdmPersonRegistrationDetails.dtls.dateOfBirth = Date.getDate("19770101");
    bdmPersonRegistrationDetails.dtls.socialSecurityNumber = "519132708";
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

    assertTrue(
      registrationResult.registrationIDDetails.alternateID.length() > 0);

    assertTrue(registrationResult.registrationIDDetails.concernRoleID != 0);

    return registrationResult;

  }

  private BDMForeignLiaisonDtls createForeignLiaisonWithRelatedFl()
    throws AppException, InformationalException {

    // create the main liaison record

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final BDMForeignLiaisonDtls flDtls = new BDMForeignLiaisonDtls();
    flDtls.foreignLiaisonID = UniqueIDFactory.newInstance().getNextID();
    flDtls.bessInd = false;
    flDtls.caseID = icResult.createCaseResult.integratedCaseID;
    flDtls.comments = "Test creating a foreign liaison 2";
    flDtls.direction = BDMLIAISONDIRECTION.INCOMING;
    flDtls.receiveDate = Date.getCurrentDate();
    flDtls.recordStatus = RECORDSTATUS.NORMAL;
    curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
      .insert(flDtls);

    // create a related liaison record
    final BDMForeignLiaisonDtls relatedFlDtls = new BDMForeignLiaisonDtls();
    relatedFlDtls.foreignLiaisonID =
      UniqueIDFactory.newInstance().getNextID();
    relatedFlDtls.bessInd = false;
    relatedFlDtls.caseID = icResult.createCaseResult.integratedCaseID;
    relatedFlDtls.comments = "Test creating a related foreign liaison";
    relatedFlDtls.direction = BDMLIAISONDIRECTION.OUTGOING;
    relatedFlDtls.receiveDate = Date.getCurrentDate();
    relatedFlDtls.recordStatus = RECORDSTATUS.NORMAL;
    // make this FL reference the main FL
    relatedFlDtls.relatedFrgnLisnIDs = "" + flDtls.foreignLiaisonID;
    curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
      .insert(relatedFlDtls);

    // return the main FL
    return flDtls;
  }

  private BDMForeignLiaisonDtls createForeignLiaisonWithCanceledRelatedFl()
    throws AppException, InformationalException {

    // create the main liaison record

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final BDMForeignLiaisonDtls flDtls = new BDMForeignLiaisonDtls();
    flDtls.foreignLiaisonID = UniqueIDFactory.newInstance().getNextID();
    flDtls.bessInd = false;
    flDtls.caseID = icResult.createCaseResult.integratedCaseID;
    flDtls.comments = "Test creating a foreign liaison 2";
    flDtls.direction = BDMLIAISONDIRECTION.INCOMING;
    flDtls.receiveDate = Date.getCurrentDate();
    flDtls.recordStatus = RECORDSTATUS.NORMAL;
    curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
      .insert(flDtls);

    // create a related liaison record
    final BDMForeignLiaisonDtls relatedFlDtls = new BDMForeignLiaisonDtls();
    relatedFlDtls.foreignLiaisonID =
      UniqueIDFactory.newInstance().getNextID();
    relatedFlDtls.bessInd = false;
    relatedFlDtls.caseID = icResult.createCaseResult.integratedCaseID;
    relatedFlDtls.comments = "Test creating a related foreign liaison";
    relatedFlDtls.direction = BDMLIAISONDIRECTION.OUTGOING;
    relatedFlDtls.receiveDate = Date.getCurrentDate();
    relatedFlDtls.recordStatus = RECORDSTATUS.CANCELLED;
    relatedFlDtls.deleteReason = BDMLIAISONDELREASON.ENTEREDINERROR;
    // make this FL reference the main FL
    relatedFlDtls.relatedFrgnLisnIDs = "" + flDtls.foreignLiaisonID;
    curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
      .insert(relatedFlDtls);

    // return the main FL
    return flDtls;
  }

  private BDMAttachmentIDs createFECAttachment(final long caseID,
    final Date receiptDate) throws AppException, InformationalException {

    return createFECAttachment(caseID, receiptDate,
      DOCUMENTTYPE.FOREIGN_LIAISON);
  }

  private BDMAttachmentIDs createFECAttachment(final long caseID,
    final Date receiptDate, final String docType)
    throws AppException, InformationalException {

    final BDMFAAttachmentDetails createFAAttachment =
      new BDMFAAttachmentDetails();

    createFAAttachment.actionIDProperty = BDMConstants.kSaveAndClose;
    createFAAttachment.bdmCreateAttachmentDetails.fileSource =
      BDM_FILE_SOURCE.CLIENT;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID =
      caseID;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.receiptDate =
      receiptDate;
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.description =
      "Test FEC attachment";
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.newCaseAttachmentName =
      "TestFecAttachment.pdf";
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.newCaseAttachmentContents =
      new Blob("Test".getBytes());
    createFAAttachment.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.documentType =
      docType;

    return BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(createFAAttachment);
  }

  private BDMFEAttachmentLinkDtls insertFLAttLnk(final long foreignLiaisonID,
    final long attachmentID) throws AppException, InformationalException {

    final BDMFEAttachmentLink flAttLnkObj =
      BDMFEAttachmentLinkFactory.newInstance();

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = BDMConstants.kBDMDEFKEYSET;

    final BDMFEAttachmentLinkDtls flAttLnkDtls =
      new BDMFEAttachmentLinkDtls();
    flAttLnkDtls.feAttachmentLinkID =
      uniqueIDObj.getNextIDFromKeySet(uniqueIDKeySet);
    flAttLnkDtls.relatedID = foreignLiaisonID;
    flAttLnkDtls.recordStatus = RECORDSTATUS.NORMAL;
    flAttLnkDtls.attachmentID = attachmentID;
    flAttLnkDtls.attachmentLinkTypCd = BDMATTACHMENTLINKTYPE.FLATTACHMENTLINK;
    flAttLnkObj.insert(flAttLnkDtls);

    return flAttLnkDtls;
  }

  private BDMForeignApplicationKey createFA(final long caseID)
    throws AppException, InformationalException {

    final BDMCreateModifyFA bdmCreateModifyFA = new BDMCreateModifyFA();
    bdmCreateModifyFA.caseID = caseID;
    bdmCreateModifyFA.comments =
      "This comments entered on the New Foreign Application screen";
    bdmCreateModifyFA.typeCode = BDMFOREIGNAPPTYPE.INVALIDITY;
    bdmCreateModifyFA.receiveDate = Date.getCurrentDate();

    return BDMForeignEngagementCaseFactory.newInstance()
      .createForeignApplication(bdmCreateModifyFA);
  }

  /**
   * listForeignLiaisons
   * viewForeignLiaisonDetails
   * viewForeignLiaisonHistoryDetails
   * listForeignLiaisonHistAtchmnts
   * listForeignLiaisonAttachments
   * listFoerignLiaisonHistory
   */

  @Test
  public void testListForeignLiaisons()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls foreignLiaisonDtls = createForeignLiaison();

    final BDMFECaseIDKey bdmfeCaseIDKey = new BDMFECaseIDKey();
    bdmfeCaseIDKey.caseID = foreignLiaisonDtls.caseID;

    final BDMFrgnLiasnViewDetailsList bdmFrgnLiasnViewDetailsList =
      bdmForeignLiaisonFacade.listForeignLiaisons(bdmfeCaseIDKey);

    assertTrue(bdmFrgnLiasnViewDetailsList.dtls.size() == 1);
  }

  @Test
  public void testViewForeignLiaisonDetails()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls foreignLiaisonDtls = createForeignLiaison();

    final BDMFECaseIDKey bdmfeCaseIDKey = new BDMFECaseIDKey();
    bdmfeCaseIDKey.caseID = foreignLiaisonDtls.caseID;

    final BDMFrgnLiasnIDKey bdmFrgnLiasnIDKey = new BDMFrgnLiasnIDKey();

    bdmFrgnLiasnIDKey.foreignLiaisonID = foreignLiaisonDtls.foreignLiaisonID;

    final BDMFrgnLiasnViewDetails bdmFrgnLiasnViewDetails =
      bdmForeignLiaisonFacade.viewForeignLiaisonDetails(bdmFrgnLiasnIDKey);

    assertNotEquals(0, bdmFrgnLiasnViewDetails.foreignLiaisonID);
    assertEquals(BDMLIAISONDIRECTION.INCOMING,
      bdmFrgnLiasnViewDetails.direction);
    assertEquals("Test creating a foreign liaison",
      bdmFrgnLiasnViewDetails.comments);
  }

  @Test
  public void testViewForeignLiaisonDetails_OfficeDetails()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls foreignLiaisonDtls =
      createForeignLiaisonUS(BDMSOURCECOUNTRY.US);

    final BDMFECaseIDKey bdmfeCaseIDKey = new BDMFECaseIDKey();
    bdmfeCaseIDKey.caseID = foreignLiaisonDtls.caseID;

    final BDMFrgnLiasnIDKey bdmFrgnLiasnIDKey = new BDMFrgnLiasnIDKey();

    bdmFrgnLiasnIDKey.foreignLiaisonID = foreignLiaisonDtls.foreignLiaisonID;

    final BDMFrgnLiasnViewDetails bdmFrgnLiasnViewDetails =
      bdmForeignLiaisonFacade.viewForeignLiaisonDetails(bdmFrgnLiasnIDKey);

    assertNotEquals(0, bdmFrgnLiasnViewDetails.foreignLiaisonID);
    assertEquals(BDMLIAISONDIRECTION.INCOMING,
      bdmFrgnLiasnViewDetails.direction);
    assertEquals("Test creating a foreign liaison",
      bdmFrgnLiasnViewDetails.comments);
  }

  @Test
  public void testUnlinkFrgnLiasnAttachment()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls liaisonDtls = this.createForeignLiaison();

    final BDMAttachmentIDs attachmentIDs =
      this.createFECAttachment(liaisonDtls.caseID, getToday());

    final BDMFEAttachmentLinkDtls linkDtls = this.insertFLAttLnk(
      liaisonDtls.foreignLiaisonID, attachmentIDs.attachmentID);

    assertNotEquals(0, linkDtls.feAttachmentLinkID);

    final BDMFrgnLiasnAttLnkIDKey unlinkKey = new BDMFrgnLiasnAttLnkIDKey();
    unlinkKey.feAttachmentLinkID = linkDtls.feAttachmentLinkID;

    bdmForeignLiaisonFacade.unlinkFrgnLiasnAttachment(unlinkKey);

    final BDMFEAttachmentLinkKey linkKey = new BDMFEAttachmentLinkKey();

    linkKey.feAttachmentLinkID = unlinkKey.feAttachmentLinkID;
    final NotFoundIndicator nfInd = new NotFoundIndicator();
    BDMFEAttachmentLinkFactory.newInstance().read(nfInd, linkKey);

    assertTrue(nfInd.isNotFound());
  }

  @Test
  public void testReadForeignLiaisonDetails()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls flDtls = this.createForeignLiaison();
    flDtls.foreignAppIDs = "12345\t23456";
    flDtls.relatedFrgnLisnIDs = "28416\t28417";
    final BDMForeignLiaisonKey flKey = new BDMForeignLiaisonKey();
    flKey.foreignLiaisonID = flDtls.foreignLiaisonID;
    curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
      .modify(flKey, flDtls);

    final BDMFrgnLiasnIDKey key = new BDMFrgnLiasnIDKey();
    key.foreignLiaisonID = flDtls.foreignLiaisonID;

    final BDMForeignLiaisonDetails flReadDetails =
      bdmForeignLiaisonFacade.readForeignLiaisonDetails(key);

    assertEquals("12345\t23456", flReadDetails.frgnAppIDTabList);
    assertEquals("LC01\tLC05\tLC16", flReadDetails.flChkLstCdTabList);
    assertEquals("28416\t28417", flReadDetails.frgnLiasnIDTabList);
  }

  @Test
  public void testListFoerignLiaisonHistory()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls flDtls = this.createForeignLiaison();

    createForeignLiaisonHistory(flDtls.foreignLiaisonID);

    final BDMFrgnLiasnIDKey key = new BDMFrgnLiasnIDKey();
    key.foreignLiaisonID = flDtls.foreignLiaisonID;

    final BDMFrgnLiasnViewDetailsList histList =
      bdmForeignLiaisonFacade.listFoerignLiaisonHistory(key);

    assertEquals(1, histList.dtls.size());
  }

  @Test
  public void testReadDetailsForForeignLiaison()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls liasonDtls =
      this.createForeignLiaisonWithRelatedFl();

    final BDMReadDetailsKey key = new BDMReadDetailsKey();
    key.fromEditInd = true;
    key.foreignLiaisonID = liasonDtls.foreignLiaisonID;
    key.caseID = liasonDtls.caseID;

    BDMReadDispDetails details =
      bdmForeignLiaisonFacade.readDetailsForForeignLiaison(key);

    assertEquals(BDMSOURCECOUNTRY.IRELAND, details.countryCode);

    key.fromEditInd = false;
    details = bdmForeignLiaisonFacade.readDetailsForForeignLiaison(key);

    assertEquals(BDMSOURCECOUNTRY.IRELAND, details.countryCode);
  }

  @Test
  public void testReadDetailsForForeignLiaison_FAList_1()
    throws AppException, InformationalException {

    final long caseID = this.registerPersonAndCreateFECase();

    final BDMForeignApplicationKey faKey = this.createFA(caseID);

    final BDMReadDetailsKey key = new BDMReadDetailsKey();
    key.caseID = caseID;

    final BDMReadDispDetails details =
      bdmForeignLiaisonFacade.readDetailsForForeignLiaison(key);

    final int size = details.faDetails.dtls.size();
    boolean success = false;
    for (int i = 0; i < size; i++) {
      if (details.faDetails.dtls
        .item(i).fApplicationID == faKey.fApplicationID) {
        success = true;
        break;
      }

    }

    assertTrue(success);
  }

  @Test
  public void testReadDetailsForForeignLiaison_FAList_2()
    throws AppException, InformationalException {

    final long caseID = this.registerPersonAndCreateFECase();

    final BDMForeignApplicationKey faKey = this.createFA(caseID);

    final BDMDeleteFADetails bdmDeleteFADetails = new BDMDeleteFADetails();
    bdmDeleteFADetails.fApplicationID = faKey.fApplicationID;
    bdmDeleteFADetails.bdmDeleteFAReason =
      BDMFOREIGNAPPDELETEREASON.CLIENT_WITHDRAWN;

    foreignEngagementCaseObj.deleteForeignApplication(bdmDeleteFADetails);

    final BDMReadDetailsKey key = new BDMReadDetailsKey();
    key.caseID = caseID;

    final BDMReadDispDetails details =
      bdmForeignLiaisonFacade.readDetailsForForeignLiaison(key);

    final int size = details.faDetails.dtls.size();
    boolean success = true;
    for (int i = 0; i < size; i++) {
      if (details.faDetails.dtls
        .item(i).fApplicationID == faKey.fApplicationID) {
        success = false;
        break;
      }
    }
    assertTrue(success);
  }

  @SuppressWarnings("unused")
  @Test
  public void testListAttachmentsForFLByCaseID()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls flDtls = createForeignLiaison();

    final BDMAttachmentIDs faAttachmentIDs = this.createFECAttachment(
      flDtls.caseID, getToday(), DOCUMENTTYPE.FOREIGN_LIAISON);

    final BDMAttachmentIDs fLAdditionalAttachmentIDs =
      this.createFECAttachment(flDtls.caseID, getToday(),
        DOCUMENTTYPE.FOREIGN_LIAISON_ADDITIONAL_DOCUMENTS);

    final BDMAttachmentIDs fLFAAdditionalAttachmentIDs =
      this.createFECAttachmentForFA(Long.parseLong(flDtls.foreignAppIDs),
        DOCUMENTTYPE.FOREIGN_APPLICATION_ADDITIONAL_DOCUMENTS);

    final BDMFECaseIDKey caseIDKey = new BDMFECaseIDKey();
    caseIDKey.caseID = flDtls.caseID;
    assertEquals(3,
      bdmForeignLiaisonFacade.listAttachmentsForFLByCaseID(caseIDKey).dtls
        .size());

    // delete 1 record
    final Attachment attObj = AttachmentFactory.newInstance();
    final AttachmentKey attKey = new AttachmentKey();
    attKey.attachmentID = faAttachmentIDs.attachmentID;
    final AttachmentDtls attDtls = attObj.read(attKey);
    attDtls.statusCode = RECORDSTATUS.CANCELLED;
    attObj.modify(attKey, attDtls);

    assertEquals(2,
      bdmForeignLiaisonFacade.listAttachmentsForFLByCaseID(caseIDKey).dtls
        .size());

  }

  @SuppressWarnings("unused")
  @Test
  public void testListAttachmentsForFLByCaseID_FAException()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls flDtls = createForeignLiaison();

    final BDMAttachmentIDs faAttachmentIDs =
      this.createFECAttachment(flDtls.caseID, getToday());

    final BDMFECaseIDKey caseIDKey = new BDMFECaseIDKey();
    caseIDKey.caseID = flDtls.caseID;

    final BDMFLAttachmentDetailsList list =
      bdmForeignLiaisonFacade.listAttachmentsForFLByCaseID(caseIDKey);

    assertTrue(list.dtls.size() == 1);

  }

  @Test
  public void testListForeignLiaisonAttachments()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls flDtls = createForeignLiaison();

    final BDMFrgnLiasnIDKey key = new BDMFrgnLiasnIDKey();
    key.foreignLiaisonID = flDtls.foreignLiaisonID;

    final BDMAttachmentIDs faAttachmentIDs = this.createFECAttachment(
      flDtls.caseID, getToday(), DOCUMENTTYPE.FOREIGN_LIAISON);

    this.insertFLAttLnk(flDtls.foreignLiaisonID,
      faAttachmentIDs.attachmentID);

    BDMFLAttachmentDetailsList list =
      bdmForeignLiaisonFacade.listForeignLiaisonAttachments(key);

    assertTrue(list.dtls.size() == 1);

    final BDMAttachmentIDs faAttachmentID2 = this.createFECAttachment(
      flDtls.caseID, getToday(), DOCUMENTTYPE.FOREIGN_LIAISON);

    this.insertFLAttLnk(flDtls.foreignLiaisonID,
      faAttachmentID2.attachmentID);
    list = bdmForeignLiaisonFacade.listForeignLiaisonAttachments(key);

    assertTrue(list.dtls.size() == 2);

  }

  @Test
  public void testListAllLiasnChecklistCodes()
    throws AppException, InformationalException {

    final BDMLiasnChecklistCodeDetailsList chklistCdDetailsList =
      bdmForeignLiaisonFacade.listAllLiasnChecklistCodes();

    assertTrue(!chklistCdDetailsList.dtls.isEmpty());
  }

  @Test
  public void testCreateAndPopulateFrgnLiasnWizard()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls flDtls = createForeignLiaison();

    final BDMFrgnLiasnIDKey key = new BDMFrgnLiasnIDKey();
    key.foreignLiaisonID = flDtls.foreignLiaisonID;

    final WizardStateID wizardStateID =
      bdmForeignLiaisonFacade.createAndPopulateFrgnLiasnWizard(key);

    assertTrue(wizardStateID.wizardStateID != 0);

  }

  /**
   * Test to list the foreign applications.
   */
  @Test
  public void testListForeignAppsByCaseID()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls flDtls = createForeignLiaison();

    final BDMFECaseIDKey key = new BDMFECaseIDKey();
    key.caseID = flDtls.caseID;

    BDMFrgnAppDetailsList bdmFrgnAppDetailsList =
      bdmForeignLiaisonFacade.listForeignAppsByCaseID(key);

    assertTrue(bdmFrgnAppDetailsList.dtls.size() == 1);
    createFA(flDtls.caseID);

    bdmFrgnAppDetailsList =
      bdmForeignLiaisonFacade.listForeignAppsByCaseID(key);
    assertTrue(bdmFrgnAppDetailsList.dtls.size() == 2);

  }

  /**
   * Test to list all the foreign liaisons of the foreign benefit.
   */
  @Test
  public void testListLiaisonReferencesByCaseID()
    throws AppException, InformationalException {

    final BDMForeignLiaisonDtls flDtls = createForeignLiaison();

    final BDMFECaseIDKey key = new BDMFECaseIDKey();
    key.caseID = flDtls.caseID;

    BDMFrgnLiasnRefDetailsList listLiaisonReferencesByCaseID =
      bdmForeignLiaisonFacade.listLiaisonReferencesByCaseID(key);

    assertTrue(listLiaisonReferencesByCaseID.dtls.size() == 1);

    // Create 2nd Foreign liaison
    final BDMForeignLiaisonDtls secondFL = new BDMForeignLiaisonDtls();
    secondFL.foreignLiaisonID = UniqueIDFactory.newInstance().getNextID();
    secondFL.bessInd = false;
    secondFL.caseID = flDtls.caseID;
    secondFL.comments = "Test creating a foreign liaison";
    secondFL.direction = BDMLIAISONDIRECTION.INCOMING;
    secondFL.receiveDate = Date.getCurrentDate();
    secondFL.recordStatus = RECORDSTATUS.NORMAL;
    secondFL.liaisonChkStrList = "LC01\tLC05\tLC16";
    secondFL.foreignAppIDs = "" + flDtls.foreignAppIDs;
    curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
      .insert(secondFL);

    listLiaisonReferencesByCaseID =
      bdmForeignLiaisonFacade.listLiaisonReferencesByCaseID(key);
    assertTrue(listLiaisonReferencesByCaseID.dtls.size() == 2);

    final BDMFrgnLiasnDeleteKey bdmFrgnLiasnDeleteKey =
      new BDMFrgnLiasnDeleteKey();
    bdmFrgnLiasnDeleteKey.foreignLiaisonID = flDtls.foreignLiaisonID;
    bdmFrgnLiasnDeleteKey.versionNo = 1;

    bdmForeignLiaisonFacade.deleteForeignLiaison(bdmFrgnLiasnDeleteKey);

    final BDMForeignLiaisonKey foreignLiaisonKey = new BDMForeignLiaisonKey();
    foreignLiaisonKey.foreignLiaisonID =
      bdmFrgnLiasnDeleteKey.foreignLiaisonID;

    // assert the status change
    final BDMForeignLiaisonDtls dtlsInDB =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .read(foreignLiaisonKey);

    assertEquals(RECORDSTATUS.CANCELLED, dtlsInDB.recordStatus);
    assertEquals(BDMLIAISONDELREASON.ENTEREDINERROR, dtlsInDB.deleteReason);

    listLiaisonReferencesByCaseID =
      bdmForeignLiaisonFacade.listLiaisonReferencesByCaseID(key);

    assertTrue(listLiaisonReferencesByCaseID.dtls.size() == 1);

  }

  @Test
  public void testListForeignLiaisonHistAtchmnts()
    throws AppException, InformationalException {

    // Prepare a new FEC case
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);
    final BDMAttachmentIDs faAttachmentIDs =
      this.createFECAttachment(icResult.createCaseResult.integratedCaseID,
        getToday(), DOCUMENTTYPE.FOREIGN_LIAISON);

    // load wizard Step 1
    final BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = readWizDetails.wizardStateID;

    assertNotEquals(0, readWizDetails.wizardStateID);

    // add details in Step 1 and click save and close
    readWizDetails.dtls.caseID = icResult.createCaseResult.integratedCaseID;
    readWizDetails.dtls.receiveDate = Date.getCurrentDate();
    readWizDetails.dtls.direction = BDMLIAISONDIRECTION.INCOMING;
    readWizDetails.dtls.foreignIdntifier = "1000012";
    readWizDetails.dtls.fOfficeID = 123000;
    readWizDetails.dtls.frgnLiasnIDTabList = "";
    readWizDetails.dtls.frgnAppIDTabList = "";
    readWizDetails.dtls.comments = "Test creating a foreign liaison";
    readWizDetails.dtls.flChkLstCdTabList = "LC01\tLC05\tLC16";
    readWizDetails.dtls.flSelectedAttIDList =
      "" + faAttachmentIDs.attachmentID;

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    wizardPersistentState.modify(wizardStateID.wizardStateID, readWizDetails);

    readWizDetails.actionIDProperty = BDMConstants.kSave;
    bdmForeignLiaisonFacade.createFLAttachmentsFromWizard(readWizDetails);

    // check result - Read Foreign Liaison
    final BDMReadFLByCaseIDKey caseIDKey = new BDMReadFLByCaseIDKey();
    caseIDKey.caseID = icResult.createCaseResult.integratedCaseID;
    final BDMForeignLiaisonDtlsList flList =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .readForeignLiaisonByCaseID(caseIDKey);

    assertEquals(1, flList.dtls.size());

    assertEquals(icResult.createCaseResult.integratedCaseID,
      flList.dtls.get(0).caseID);
    assertEquals(BDMLIAISONDIRECTION.INCOMING, flList.dtls.get(0).direction);

    /// read Foreign Liaison History record
    final BDMReadByFrgnLiasnIDKey flIDKey = new BDMReadByFrgnLiasnIDKey();
    flIDKey.foreignLiaisonID = flList.dtls.get(0).foreignLiaisonID;
    final BDMForeignLiaisonHistDtlsList flHistList =
      BDMForeignLiaisonHistFactory.newInstance()
        .readByForeignLiaisonID(flIDKey);

    assertEquals(1, flHistList.dtls.size());

    final BDMFrgnLiasnHistIDKey bdmFrgnLiasnHistIDKey =
      new BDMFrgnLiasnHistIDKey();
    bdmFrgnLiasnHistIDKey.frgnLsnHstID = flHistList.dtls.get(0).frgnLsnHstID;

    final BDMFLAttachmentDetailsList list = bdmForeignLiaisonFacade
      .listForeignLiaisonHistAtchmnts(bdmFrgnLiasnHistIDKey);

    assertTrue(list.dtls.size() == 1);

  }

  @Test
  public void testViewForeignLiaisonHistoryDetails()
    throws AppException, InformationalException {

    BDMFrgnLiasnViewDetails flViewHistDetails = new BDMFrgnLiasnViewDetails();
    final BDMFrgnLiasnHistIDKey key = new BDMFrgnLiasnHistIDKey();

    // Prepare a new FEC case
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);
    final BDMAttachmentIDs faAttachmentIDs =
      this.createFECAttachment(icResult.createCaseResult.integratedCaseID,
        getToday(), DOCUMENTTYPE.FOREIGN_LIAISON);

    // load wizard Step 1
    final BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final long wizID = readWizDetails.wizardStateID;

    assertNotEquals(0, wizID);

    // add details in Step 1 and click save and close
    readWizDetails.dtls.caseID = icResult.createCaseResult.integratedCaseID;
    readWizDetails.dtls.receiveDate = Date.getCurrentDate();
    readWizDetails.dtls.direction = BDMLIAISONDIRECTION.INCOMING;
    readWizDetails.dtls.foreignIdntifier = "1000012";
    readWizDetails.dtls.fOfficeID = 123000;
    readWizDetails.dtls.frgnLiasnIDTabList = "";
    readWizDetails.dtls.frgnAppIDTabList = "";
    readWizDetails.dtls.comments = "Test creating a foreign liaison";
    readWizDetails.dtls.flChkLstCdTabList = "LC01\tLC05\tLC16";
    readWizDetails.dtls.flSelectedAttIDList =
      "" + faAttachmentIDs.caseAttachmentLinkID;

    readWizDetails.actionIDProperty = BDMConstants.kStep1SaveAndClose;
    bdmForeignLiaisonFacade.createForeignLiaisonFromWizard(readWizDetails);

    // check result - Read Foreign Liaison
    final BDMReadFLByCaseIDKey caseIDKey = new BDMReadFLByCaseIDKey();
    caseIDKey.caseID = icResult.createCaseResult.integratedCaseID;
    final BDMForeignLiaisonDtlsList flList =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .readForeignLiaisonByCaseID(caseIDKey);

    assertEquals(1, flList.dtls.size());

    /// read Foreign Liaison History record
    final BDMReadByFrgnLiasnIDKey flIDKey = new BDMReadByFrgnLiasnIDKey();
    flIDKey.foreignLiaisonID = flList.dtls.get(0).foreignLiaisonID;
    final BDMForeignLiaisonHistDtlsList flHistList =
      BDMForeignLiaisonHistFactory.newInstance()
        .readByForeignLiaisonID(flIDKey);

    assertEquals(1, flHistList.dtls.size());

    key.frgnLsnHstID = flHistList.dtls.get(0).frgnLsnHstID;
    flViewHistDetails =
      bdmForeignLiaisonFacade.viewForeignLiaisonHistoryDetails(key);

    assertEquals("Test creating a foreign liaison",
      flViewHistDetails.comments);
    assertEquals(BDMLIAISONDIRECTION.INCOMING, flViewHistDetails.direction);
    assertEquals("1000012", flViewHistDetails.foreignIdntifier);

  }

  @Test
  public void testModifyFLChecklistFromWizard()
    throws AppException, InformationalException {

    // Prepare a new FEC case
    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);
    final BDMAttachmentIDs faAttachmentIDs =
      this.createFECAttachment(icResult.createCaseResult.integratedCaseID,
        getToday(), DOCUMENTTYPE.FOREIGN_LIAISON);

    // load wizard Step 1
    BDMFrgnLiasnWizardDetails readWizDetails = bdmForeignLiaisonFacade
      .readForeignLiaisonWizardDetails(new WizardStateID());

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = readWizDetails.wizardStateID;

    assertNotEquals(0, readWizDetails.wizardStateID);

    // add details in Step 1 and click save and close
    readWizDetails.dtls.caseID = icResult.createCaseResult.integratedCaseID;
    readWizDetails.dtls.receiveDate = Date.getCurrentDate();
    readWizDetails.dtls.direction = BDMLIAISONDIRECTION.INCOMING;
    readWizDetails.dtls.foreignIdntifier = "1000012";
    readWizDetails.dtls.fOfficeID = 123000;
    readWizDetails.dtls.frgnLiasnIDTabList = "";
    readWizDetails.dtls.frgnAppIDTabList = "";
    readWizDetails.dtls.comments = "Test creating a foreign liaison";
    readWizDetails.dtls.flChkLstCdTabList = "LC01\tLC05\tLC16";
    readWizDetails.dtls.flSelectedAttIDList =
      "" + faAttachmentIDs.attachmentID;

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    wizardPersistentState.modify(wizardStateID.wizardStateID, readWizDetails);

    readWizDetails.actionIDProperty = BDMConstants.kSave;
    bdmForeignLiaisonFacade.createFLAttachmentsFromWizard(readWizDetails);

    // check result - Read Foreign Liaison
    final BDMReadFLByCaseIDKey caseIDKey = new BDMReadFLByCaseIDKey();
    caseIDKey.caseID = icResult.createCaseResult.integratedCaseID;
    final BDMForeignLiaisonDtlsList flList =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .readForeignLiaisonByCaseID(caseIDKey);

    assertEquals(1, flList.dtls.size());

    assertEquals("", readWizDetails.dtls.lisnChkLstOthrDesc);
    assertEquals(BDMLIAISONDIRECTION.INCOMING, readWizDetails.dtls.direction);

    readWizDetails =
      bdmForeignLiaisonFacade.readForeignLiaisonWizardDetails(wizardStateID);
    readWizDetails.dtls.foreignLiaisonID =
      flList.dtls.get(0).foreignLiaisonID;
    wizardPersistentState.modify(wizardStateID.wizardStateID, readWizDetails);
    // Edit details in page 1 and click next
    readWizDetails.dtls.flChkLstCdTabList = BDMLIAISONCHECKLIST.OTHER;
    readWizDetails.dtls.lisnChkLstOthrDesc = "Selected Other";

    readWizDetails.actionIDProperty = BDMConstants.kStep2SaveAndClose;

    bdmForeignLiaisonFacade.modifyFLChecklistFromWizard(readWizDetails);

    final BDMForeignLiaisonKey frgnLiasnKey = new BDMForeignLiaisonKey();
    frgnLiasnKey.foreignLiaisonID = readWizDetails.dtls.foreignLiaisonID;

    final BDMForeignLiaisonDtls flDtls =
      curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory.newInstance()
        .read(frgnLiasnKey);

    assertEquals(BDMLIAISONCHECKLIST.OTHER, flDtls.liaisonChkStrList);
    assertEquals("Selected Other", flDtls.lisnChkLstOthrDesc);

  }

  private long registerPersonAndCreateFECase()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    createFA(icResult.createCaseResult.integratedCaseID);

    return icResult.createCaseResult.integratedCaseID;
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
   * n creating Foreign Application attachment linking
   * to Foreign Application
   */

  private BDMAttachmentIDs createFECaseAttachmentLinkingFA_kSave(
    final long appID) throws AppException, InformationalException {

    // Register person Create FEc case and create Fpreign Application

    final BDMFAKey bdmFAKey = new BDMFAKey();
    bdmFAKey.fApplicationID = appID;

    // Read Foreign Application details
    final BDMFADetails readFADetails =
      maintainForeignEngagementCase.readForeignApplication(bdmFAKey);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      maintainForeignEngagementCase
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

    attachmentIDs = maintainForeignEngagementCase
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment details from Step1
    bdmFAAttachmentDetails = maintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Click Save without linking to FA
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kSave;
    attachmentIDs.caseID =
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;

    final BDMFAList fAList = maintainForeignEngagementCase
      .listFANotLinkedWithAttachment(attachmentIDs);
    bdmFAAttachmentDetails.selectedFAList =
      String.valueOf(fAList.bdmFADetails.get(0).fApplicationID);

    attachmentIDs = maintainForeignEngagementCase
      .createFECaseAttachmentLinkingFA(bdmFAAttachmentDetails);

    return attachmentIDs;
  }

  private BDMAttachmentIDs createFECAttachmentForFA(final long foreignAppID,
    final String documentTypeFA) throws AppException, InformationalException {

    // Register person Create FEc case and create Fpreign Application

    final BDMFAKey bdmFAKey = new BDMFAKey();
    bdmFAKey.fApplicationID = foreignAppID;

    // Read Foreign Application details
    final BDMFADetails readFADetails =
      maintainForeignEngagementCase.readForeignApplication(bdmFAKey);

    BDMAttachmentIDs attachmentIDs = new BDMAttachmentIDs();
    attachmentIDs.wizardStateID = 0;

    // Load wizard
    BDMFAAttachmentDetails bdmFAAttachmentDetails =
      maintainForeignEngagementCase
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
      documentTypeFA;

    attachmentIDs = BDMMaintainForeignEngagementCaseFactory.newInstance()
      .createFECaseAttachment(bdmFAAttachmentDetails);

    // Read attachment details from Step1
    bdmFAAttachmentDetails = maintainForeignEngagementCase
      .readFECaseAttachmentWizardDetails(attachmentIDs);

    // Click Save without linking to FA w
    bdmFAAttachmentDetails.actionIDProperty = BDMConstants.kSave;
    attachmentIDs.caseID =
      bdmFAAttachmentDetails.bdmCreateAttachmentDetails.dtls.createCaseAttachmentDetails.caseID;

    bdmFAAttachmentDetails.selectedFAList = String.valueOf(foreignAppID);

    attachmentIDs = maintainForeignEngagementCase
      .createFECaseAttachmentLinkingFA(bdmFAAttachmentDetails);
    return attachmentIDs;

  }

}
