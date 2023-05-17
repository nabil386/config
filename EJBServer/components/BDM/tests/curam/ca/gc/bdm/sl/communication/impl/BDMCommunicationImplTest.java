package curam.ca.gc.bdm.sl.communication.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationImpl;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMViewCorrespondenceDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceRequest;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONMETHOD;
import curam.codetable.CORRESPONDENT;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.facade.struct.WizardStateID;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.EnvVars;
import curam.core.intf.CaseHeader;
import curam.core.intf.PersonRegistration;
import curam.core.sl.struct.CommunicationIDKey;
import curam.core.sl.struct.CreateMSWordCommunicationDetails1;
import curam.core.sl.struct.MSWordCommunicationDetails1;
import curam.core.sl.struct.PreviewProFormaKey;
import curam.core.sl.struct.ProFormaCommDetails1;
import curam.core.sl.struct.ReadMSWordCommunicationKey;
import curam.core.sl.struct.TemplateAndDocumentDataKey;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.CaseHeaderDtlsList;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.UniqueID;
import curam.wizardpersistence.impl.WizardPersistentState;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for class BDMCommunication
 */
public class BDMCommunicationImplTest extends BDMBaseTest {

  curam.ca.gc.bdm.sl.communication.intf.BDMCommunication kBdmCommunication =
    BDMCommunicationFactory.newInstance();

  final curam.ca.gc.bdm.sl.communication.intf.BDMCommunication bdmCommunicationObj =
    curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory
      .newInstance();

  BDMCommunicationImpl bdmCommunicationImplObJ = new BDMCommunicationImpl();

  public BDMCommunicationImplTest() {

    super();
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  @Test
  public void testpreviewProForma()
    throws AppException, InformationalException {

    final PreviewProFormaKey key = new PreviewProFormaKey();

    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizId.wizardStateID);

    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final long communicationID = insertCorrespondenceDetails(corresDetails);

    key.communicationID = communicationID;
    bdmCommunicationImplObJ.previewProForma(key);
    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());

  }

  @Test
  public void testpreviewMSWord()
    throws AppException, InformationalException {

    final ReadMSWordCommunicationKey readMSWordCommKey =
      new ReadMSWordCommunicationKey();
    final CommunicationIDKey communicationIDKey = new CommunicationIDKey();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizId.wizardStateID);

    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final long communicationID = insertCorrespondenceDetails(corresDetails);

    communicationIDKey.communicationID = communicationID;
    bdmCommunicationImplObJ.previewMSWord(communicationIDKey);
    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());

  }

  @Test
  public void testgetWordTemplateDocumentAndData()
    throws AppException, InformationalException {

    final TemplateAndDocumentDataKey key = new TemplateAndDocumentDataKey();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizId.wizardStateID);

    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final long communicationID = insertCorrespondenceDetails(corresDetails);

    key.communicationID = communicationID;
    key.templateID = "758302";
    bdmCommunicationImplObJ.getWordTemplateDocumentAndData(key);
    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());

  }

  @Test
  public void testcreateAutoCommunication()
    throws AppException, InformationalException {

    final ConcernRoleCommunicationDtls concernRoleCommDtls =
      new ConcernRoleCommunicationDtls();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizId.wizardStateID);

    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final long communicationID = insertCorrespondenceDetails(corresDetails);
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;
    concernRoleCommDtls.communicationID = communicationID;
    concernRoleCommDtls.concernRoleID = originalConcernRoleID;
    concernRoleCommDtls.communicationDate = Date.getCurrentDate();
    concernRoleCommDtls.communicationFormat = "CF4";

    concernRoleCommDtls.addressID = ConcernRoleFactory.newInstance()
      .readConcernRole(concernRoleKey).primaryAddressID;
    concernRoleCommDtls.caseID = pdcID;
    bdmCommunicationImplObJ.createAutoCommunication(concernRoleCommDtls);
    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());

  }

  @Test
  public void testcreateProFormaReturningID()
    throws AppException, InformationalException {

    final ProFormaCommDetails1 proFormaCommDetails1 =
      new ProFormaCommDetails1();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizId.wizardStateID);

    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final long communicationID = insertCorrespondenceDetails(corresDetails);

    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    proFormaCommDetails1.communicationID = communicationID;
    proFormaCommDetails1.caseParticipantRoleID = originalConcernRoleID;
    proFormaCommDetails1.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";

    proFormaCommDetails1.communicationDate = Date.getCurrentDate();

    proFormaCommDetails1.addressID =
      UniqueIDFactory.newInstance().getNextID();
    proFormaCommDetails1.caseID = pdcID;
    proFormaCommDetails1.communicationTypeCode = "CT8007";
    proFormaCommDetails1.correspondentType = CORRESPONDENT.CLIENT;
    proFormaCommDetails1.correspondentParticipantRoleID =
      originalConcernRoleID;
    bdmCommunicationImplObJ.createProFormaReturningID(proFormaCommDetails1);
    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());

  }

  @Test
  public void testcreateProFormaReturningIDNoAddressID()
    throws AppException, InformationalException {

    final ProFormaCommDetails1 proFormaCommDetails1 =
      new ProFormaCommDetails1();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizId.wizardStateID);

    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final long communicationID = insertCorrespondenceDetails(corresDetails);

    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    proFormaCommDetails1.communicationID = communicationID;
    proFormaCommDetails1.caseParticipantRoleID = originalConcernRoleID;
    proFormaCommDetails1.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";

    proFormaCommDetails1.communicationDate = Date.getCurrentDate();

    proFormaCommDetails1.addressID =
      UniqueIDFactory.newInstance().getNextID();
    proFormaCommDetails1.caseID = pdcID;
    proFormaCommDetails1.communicationTypeCode = "CT8007";
    proFormaCommDetails1.correspondentType = CORRESPONDENT.CLIENT;
    proFormaCommDetails1.correspondentParticipantRoleID =
      originalConcernRoleID;
    bdmCommunicationImplObJ
      .createProFormaReturningIDNoAddressID(proFormaCommDetails1);
    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());

  }

  @Test
  public void testcreateMSWordCommunication1()
    throws AppException, InformationalException {

    final CreateMSWordCommunicationDetails1 details =
      new CreateMSWordCommunicationDetails1();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizId.wizardStateID);

    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final long communicationID = insertCorrespondenceDetails(corresDetails);

    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    details.dtls.caseID = pdcID;
    details.dtls.communicationID = communicationID;
    details.dtls.correspondentName = "CRTP";
    details.dtls.caseParticipantRoleID = originalConcernRoleID;
    details.dtls.correspondentTypeCode = CORRESPONDENT.CLIENT;

    details.dtls.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";

    final TemplateAndDocumentDataKey key =
      bdmCommunicationImplObJ.createMSWordCommunication1(details);
    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());

  }

  @Test
  public void testcreateMSWordCommunication1NoAddressID()
    throws AppException, InformationalException {

    final CreateMSWordCommunicationDetails1 details =
      new CreateMSWordCommunicationDetails1();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizId.wizardStateID);

    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final long communicationID = insertCorrespondenceDetails(corresDetails);

    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    details.dtls.caseID = pdcID;
    details.dtls.communicationID = communicationID;
    details.dtls.correspondentName = "CRTP";
    details.dtls.caseParticipantRoleID = originalConcernRoleID;
    details.dtls.correspondentTypeCode = CORRESPONDENT.CLIENT;

    details.dtls.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";

    final TemplateAndDocumentDataKey key =
      bdmCommunicationImplObJ.createMSWordCommunication1NoAddressID(details);
    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());

  }

  @Test
  public void testsubmitProformaComm()
    throws AppException, InformationalException {

    final ProFormaCommDetails1 proFormaCommDetails1 =
      new ProFormaCommDetails1();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizId.wizardStateID);

    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final long communicationID = insertCorrespondenceDetails(corresDetails);

    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    proFormaCommDetails1.communicationID = communicationID;
    proFormaCommDetails1.caseParticipantRoleID = originalConcernRoleID;
    proFormaCommDetails1.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";

    proFormaCommDetails1.communicationDate = Date.getCurrentDate();

    proFormaCommDetails1.addressID =
      UniqueIDFactory.newInstance().getNextID();
    proFormaCommDetails1.caseID = pdcID;
    proFormaCommDetails1.communicationTypeCode = "CT8007";
    proFormaCommDetails1.correspondentType = CORRESPONDENT.CLIENT;
    proFormaCommDetails1.correspondentParticipantRoleID =
      originalConcernRoleID;
    bdmCommunicationImplObJ.submitProformaComm(proFormaCommDetails1);
    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());

  }

  @Test
  public void testsubmitMSWordComm()
    throws AppException, InformationalException {

    final MSWordCommunicationDetails1 proFormaCommDetails1 =
      new MSWordCommunicationDetails1();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    final BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    final BDMCorrespondenceDetails corresDetails =
      (BDMCorrespondenceDetails) wizardObj.read(wizId.wizardStateID);

    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final long communicationID = insertCorrespondenceDetails(corresDetails);

    final String name = "Original Person";
    final PDCPersonDetails pdcOriginalPersonDetails =
      createOriginalPDCPerson();
    final long originalConcernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcOriginalPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    proFormaCommDetails1.communicationID = communicationID;
    proFormaCommDetails1.caseParticipantRoleID = originalConcernRoleID;
    proFormaCommDetails1.addressData =
      "1\n0\nBDMINTL\nUS\n1\n0\nZIP=\nBDMSTPROVX=\nCITY=\nCOUNTRY=\nPOBOXNO=\nAPT=\nPOSTCODE=\nBDMZIPX=\nSTATEPROV=\nPROV=\nADD1=\nADD2=\n";

    proFormaCommDetails1.communicationDate = Date.getCurrentDate();

    proFormaCommDetails1.addressID =
      UniqueIDFactory.newInstance().getNextID();
    proFormaCommDetails1.caseID = pdcID;

    proFormaCommDetails1.correspondentParticipantRoleID =
      originalConcernRoleID;
    bdmCommunicationImplObJ.submitMSWordComm(proFormaCommDetails1);
    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());

  }

  public PDCPersonDetails createOriginalPDCPerson()
    throws AppException, InformationalException {

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    personRegistrationDetails.firstForename = "Original Person";
    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();
    pdcPersonDetails.assign(personRegistrationDetails);
    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  public PDCPersonDetails createDupPDCPerson()
    throws AppException, InformationalException {

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final curam.core.struct.PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    personRegistrationDetails.firstForename = "Duplicate Person";
    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();
    pdcPersonDetails.assign(personRegistrationDetails);
    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final BDMUtil bdmUtil = new BDMUtil();

    final RegisterPerson registerPerson =
      new RegisterPerson("RegisterPerson");

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    bdmUtil.createContactPreferenceEvidence(
      registrationIDDetails.concernRoleID, BDMLANGUAGE.ENGLISHL,
      BDMLANGUAGE.ENGLISHL, BDMConstants.EMPTY_STRING);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  /**
   * This method is to create concernRoleCorrespondence record in Curam.
   *
   * @param commDtls
   * @return long - communicationID
   *
   * @throws AppException
   * @throws InformationalException
   */
  private long
    insertCorrespondenceDetails(final BDMCorrespondenceDetails commDtls)
      throws AppException, InformationalException {

    final ConcernRoleCommunicationDtls concernRoleCommDtls =
      new ConcernRoleCommunicationDtls();

    // set the details
    // implement human-readable Unique ID
    concernRoleCommDtls.communicationID = UniqueID.nextUniqueID();
    concernRoleCommDtls.concernRoleID = commDtls.concernRoleID;
    concernRoleCommDtls.statusCode = RECORDSTATUS.NORMAL;
    concernRoleCommDtls.correspondentName = commDtls.toCorrespondentName;
    if (0 != commDtls.caseID) {
      concernRoleCommDtls.caseID = commDtls.caseID;
    }
    // TODO: need to check where it is populated
    concernRoleCommDtls.subjectText = commDtls.subject;
    concernRoleCommDtls.documentTemplateID =
      String.valueOf(commDtls.templateID);
    concernRoleCommDtls.communicationFormat =
      COMMUNICATIONFORMAT.CORRESPONDENCE;
    concernRoleCommDtls.communicationDate = Date.getCurrentDate();
    concernRoleCommDtls.userName = TransactionInfo.getProgramUser();

    // set the method type
    if (commDtls.isDigitalInd) {
      concernRoleCommDtls.methodTypeCode = COMMUNICATIONMETHOD.DIGITAL;
    } else {
      concernRoleCommDtls.methodTypeCode = COMMUNICATIONMETHOD.HARDCOPY;
    }

    // set the correspondentConcernRoleID and correspondenceTypeCode
    if (commDtls.toClientIsCorrespondent) {
      concernRoleCommDtls.correspondentConcernRoleID = commDtls.concernRoleID;
      concernRoleCommDtls.correspondentTypeCode = CORRESPONDENT.CLIENT;
    } else if (commDtls.toContactConcernRoleID != 0) {
      concernRoleCommDtls.correspondentConcernRoleID =
        commDtls.toContactConcernRoleID;
      concernRoleCommDtls.correspondentTypeCode = CORRESPONDENT.CONTACT;
    } else if (commDtls.toContactThirdPartyConcernRoleID != 0) {
      concernRoleCommDtls.correspondentConcernRoleID =
        commDtls.toContactThirdPartyConcernRoleID;
      concernRoleCommDtls.correspondentTypeCode = CORRESPONDENT.THIRDPARTY;
    } else {
      concernRoleCommDtls.correspondentConcernRoleID =
        commDtls.toParticipantRoleID;
      concernRoleCommDtls.correspondentTypeCode = CORRESPONDENT.PARTICIPANT;
    }
    concernRoleCommDtls.addressID = commDtls.toAddressID;
    // insert into ConcernRoleCommunication
    ConcernRoleCommunicationFactory.newInstance().insert(concernRoleCommDtls);

    final BDMConcernRoleCommunicationDtls bdmConcernRoleCommDtls =
      new BDMConcernRoleCommunicationDtls();

    // BUG-92596, Start
    // Set the correct field for concernRoleID
    // set the ccClientConcernRoleID
    if (commDtls.ccClientIsCorrespondent) {
      bdmConcernRoleCommDtls.ccClientConcernRoleID =
        concernRoleCommDtls.correspondentConcernRoleID;
    }
    // BUG-92596, End

    // set the BDM Correspondence details
    bdmConcernRoleCommDtls.communicationID =
      concernRoleCommDtls.communicationID;
    if (0 != commDtls.ccContactConcernRoleID) {
      bdmConcernRoleCommDtls.ccContactConcernRoleID =
        commDtls.ccContactConcernRoleID;
    }
    if (0 != commDtls.ccParticipantRoleID) {
      bdmConcernRoleCommDtls.ccParticipantConcernRoleID =
        commDtls.ccParticipantRoleID;
    }
    if (0 != commDtls.ccThirdPartyContactConcernRoleID) {
      bdmConcernRoleCommDtls.ccThirdPartyContactConcernRoleID =
        commDtls.ccThirdPartyContactConcernRoleID;
    }

    bdmConcernRoleCommDtls.digitalInd = commDtls.isDigitalInd;
    bdmConcernRoleCommDtls.ccAddressID = commDtls.ccDetails.ccAddressID;

    // TASK-99477, Start
    // Insert the template name used as well
    bdmConcernRoleCommDtls.templateName = commDtls.templateName;
    // TASK-99477, End

    // insert into BDMConcernRoleCommunication
    BDMConcernRoleCommunicationFactory.newInstance()
      .insert(bdmConcernRoleCommDtls);

    return concernRoleCommDtls.communicationID;
  }

}
