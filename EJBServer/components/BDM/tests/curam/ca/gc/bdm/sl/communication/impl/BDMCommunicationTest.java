package curam.ca.gc.bdm.sl.communication.impl;

import curam.bdm.test.junit4.MockLogin;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMCCTSUBMITOPT;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTTemplateFactory;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMConcernRoleCommunication;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceSearchKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMCategorySubCategoryList;
import curam.ca.gc.bdm.facade.communication.struct.BDMCommunicationKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceWizardKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMFileNameAndDataDtls;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchCriteria;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchDetailsList;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMViewCorrespondenceDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory;
import curam.ca.gc.bdm.sl.communication.struct.BDMCommunicationStatusDetails;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceRequest;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.codetable.COMMUNICATIONDIRECTION;
import curam.codetable.COMMUNICATIONFORMAT;
import curam.codetable.COMMUNICATIONMETHOD;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.COMMUNICATIONTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CORRESPONDENT;
import curam.codetable.EMAILTYPE;
import curam.codetable.METHODOFDELIVERY;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.facade.struct.WizardStateID;
import curam.core.fact.ConcernRoleCommunicationFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.ConcernRoleCommunication;
import curam.core.intf.PersonRegistration;
import curam.core.sl.struct.CommunicationIDKey;
import curam.core.sl.struct.RecordedCommDetails1;
import curam.core.sl.struct.RecordedCommKey;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleCommKeyOut;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.pdc.struct.PDCPersonDetails;
import curam.struct.ConcernRoleIDCaseIDKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.UniqueID;
import curam.wizardpersistence.impl.WizardPersistentState;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for class BDMCommunication
 */
public class BDMCommunicationTest extends BDMBaseTest {

  curam.ca.gc.bdm.sl.communication.intf.BDMCommunication kBdmCommunication =
    BDMCommunicationFactory.newInstance();

  final curam.ca.gc.bdm.sl.communication.intf.BDMCommunication bdmCommunicationObj =
    curam.ca.gc.bdm.sl.communication.fact.BDMCommunicationFactory
      .newInstance();

  public BDMCommunicationTest() {

    super();
  }

  @Override
  public boolean shouldCommit() {

    return false;
  }

  @Test
  public void testCreateRecordedCommWithReturningIDForPerson()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();

    final RecordedCommDetails1 recordedCommDetails =
      new RecordedCommDetails1();

    recordedCommDetails.clientParticipantRoleID =
      person.registrationIDDetails.concernRoleID;

    recordedCommDetails.correspondentParticipantRoleID =
      person.registrationIDDetails.concernRoleID;

    recordedCommDetails.communicationDirection =
      COMMUNICATIONDIRECTION.INCOMING;

    recordedCommDetails.communicationText = "This is the Text Test";

    recordedCommDetails.subject = "This is the Subject Test";

    recordedCommDetails.methodTypeCode = COMMUNICATIONMETHOD.EMAIL;

    recordedCommDetails.correspondentType = CORRESPONDENT.CLIENT;

    recordedCommDetails.communicationTypeCode = COMMUNICATIONTYPE.ENQUIRY_REU;

    recordedCommDetails.communicationDate =
      Date.getCurrentDate().addDays(-10);

    final ConcernRoleCommKeyOut concernRoleCommKeyOut = kBdmCommunication
      .createRecordedCommWithReturningID(recordedCommDetails);

    assertEquals(
      concernRoleCommKeyOut.communicationID != CuramConst.kLongZero, true);
  }

  @Test
  public void testSaveCorrespondenceWizard() throws Exception {

    try {
      final BDMCorrespondenceDetails corrDetails =
        new BDMCorrespondenceDetails();

      corrDetails.concernRoleID = 101;
      corrDetails.toClientIsCorrespondent = true;

      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    } catch (final Exception e) {
      final AppException ae =
        new AppException(BDMBPOCCT.ERR_ADDRESS_IS_MISSING);
      ae.arg("James Smith");
      assertEquals(ae.getLocalizedMessage().toString(),
        e.getLocalizedMessage().toString());
    }
  }

  @Test
  public void testCreateCorrespondence() throws Exception {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 758302;
    corrDetails.templatePath = "/";

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
  public void testCreateCorrespondence_singleSignOnEnabled()
    throws Exception {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 758302;
    corrDetails.templatePath = "/";

    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    final BDMCorrespondenceWizardKey wizardKey =
      new BDMCorrespondenceWizardKey();
    wizardKey.wizardStateID = wizId.wizardStateID;

    wizardKey.submitOpt = BDMCCTSUBMITOPT.DEFAULTCODE;

    MockLogin.getInst().login("unauthenticated");

    Configuration.setProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED,
      "true");
    Configuration.setProperty(EnvVars.ENV_ALTERNATE_LOGIN_ID_ENABLED, "true");
    final BDMTemplateDetails bdmTemplateDetails =
      bdmCommunicationObj.createCorrespondence(wizardKey);
    Configuration.setProperty(EnvVars.BDM_ENV_ALTERNATE_LOGIN_ID_ENABLED,
      "false");
    Configuration.setProperty(EnvVars.ENV_ALTERNATE_LOGIN_ID_ENABLED,
      "false");
    assertTrue(bdmTemplateDetails.workItemID != 0);
  }

  @Test
  public void testCreateCorrespondence_ERR_SUBMIT_OPTION_MUST_BE_ENTERED()
    throws Exception {

    try {
      final BDMCorrespondenceDetails corrDetails =
        new BDMCorrespondenceDetails();

      final PDCPersonDetails personDtls = createPDCPerson();

      corrDetails.concernRoleID = personDtls.concernRoleID;
      corrDetails.toClientIsCorrespondent = true;

      corrDetails.templateID = 758302;
      corrDetails.templatePath = "/";

      final WizardStateID wizId =
        bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

      final BDMCorrespondenceWizardKey wizardKey =
        new BDMCorrespondenceWizardKey();
      wizardKey.wizardStateID = wizId.wizardStateID;

      wizardKey.submitOpt = BDMConstants.EMPTY_STRING;
      bdmCommunicationObj.createCorrespondence(wizardKey);
    } catch (final Exception e) {
      assertEquals(BDMBPOCCT.ERR_SUBMIT_OPTION_MUST_BE_ENTERED.toString(),
        e.getLocalizedMessage().toString());
    }
  }

  @Test
  public void
    testCreateCorrespondence_ERR_UNABLE_TO_CREATE_CORRESPONDENCE_BY_ID()
      throws Exception {

    try {
      final BDMCorrespondenceDetails corrDetails =
        new BDMCorrespondenceDetails();

      final PDCPersonDetails personDtls = createPDCPerson();

      corrDetails.concernRoleID = personDtls.concernRoleID;
      corrDetails.toClientIsCorrespondent = true;

      corrDetails.templateID = 0;
      corrDetails.templatePath = "/";

      final WizardStateID wizId =
        bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

      final BDMCorrespondenceWizardKey wizardKey =
        new BDMCorrespondenceWizardKey();

      wizardKey.wizardStateID = wizId.wizardStateID;
      wizardKey.submitOpt = BDMCCTSUBMITOPT.MODIFY;

      bdmCommunicationObj.createCorrespondence(wizardKey);
    } catch (final Exception e) {
      assertEquals(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE.toString(),
        e.getLocalizedMessage().toString());
    }
  }

  @Test
  public void testSearchTemplates() throws Exception {

    final BDMTemplateSearchKey searchKey = new BDMTemplateSearchKey();
    searchKey.categoryID = 0;

    final BDMTemplateSearchDetailsList templateList =
      bdmCommunicationObj.searchTemplates(searchKey);
    assertTrue(templateList.dtls.size() != 0);
  }

  @Test
  public void
    testValidateSearchTemplateInputs_ERR_TEMPLATE_CATEGORY_MUST_BE_SELECTED()
      throws Exception {

    try {
      final BDMTemplateSearchKey searchKey = new BDMTemplateSearchKey();
      searchKey.categoryID = 0;
      searchKey.subCategoryID = 1;
      bdmCommunicationObj.searchTemplates(searchKey);
    } catch (final Exception e) {
      assertEquals(
        BDMBPOCCT.ERR_TEMPLATE_CATEGORY_MUST_BE_SELECTED.toString(),
        e.getLocalizedMessage().toString());
    }
  }

  @Test
  public void
    testValidateSearchTemplateInputs_ERR_TEMPLATE_NAME_OR_CATEGORY_MUST_BE_ENTERED()
      throws Exception {

    try {
      final BDMTemplateSearchKey searchKey = new BDMTemplateSearchKey();
      searchKey.templateIDName = BDMConstants.EMPTY_STRING;
      searchKey.categoryID = 0;
      bdmCommunicationObj.searchTemplates(searchKey);
    } catch (final Exception e) {
      assertEquals(
        BDMBPOCCT.ERR_TEMPLATE_NAME_OR_CATEGORY_MUST_BE_ENTERED.toString(),
        e.getLocalizedMessage().toString());
    }
  }

  @Test
  public void testValidateSearchTemplateInputs() throws Exception {

    try {
      final BDMTemplateSearchKey searchKey = new BDMTemplateSearchKey();

      searchKey.templateIDName = "x";

      bdmCommunicationObj.searchTemplates(searchKey);

    } catch (final Exception e) {
      final AppException ae = new AppException(BDMBPOCCT.ERR_MINIMUM_LENGTH);
      ae.arg("2");
      assertEquals(e.getLocalizedMessage().toString(),
        ae.getLocalizedMessage().toString());
    }
  }

  @Test
  public void testGetCategoryAndSubCategoryList()
    throws AppException, InformationalException {

    final BDMCategorySubCategoryList categoryList =
      BDMCCTTemplateFactory.newInstance().searchCategorySubCategory();
    bdmCommunicationObj.getCategoryAndSubCategoryList();
    // categoryList.dtls.size();
    assertTrue(categoryList.dtls.size() > 0);
  }

  @Test
  public void testConstructJavaScriptObject() throws Exception {

    final BDMTemplateSearchCriteria criteria =
      bdmCommunicationObj.getSubCategorySearchCriteria();
    assertFalse(criteria.subCategoryArray.isEmpty());
  }

  @Test
  public void testValidateCorespondenceDetails() throws Exception {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 758302;
    corrDetails.templatePath = "/";

    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    assertTrue(wizId.wizardStateID != 0);
  }

  @Test
  public void testValidateCorespondenceDetails_ERR_USERID_MUST_BE_ENTERED()
    throws Exception {

    try {
      final BDMCorrespondenceDetails corrDetails =
        new BDMCorrespondenceDetails();

      corrDetails.concernRoleID = 0;

      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    } catch (final Exception e) {
      assertEquals(BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED.toString(),
        e.getLocalizedMessage().toString());
    }
  }

  @Test
  public void testValidateCorespondenceDetails_ERR_TO_RECIPIENT_MANDATORY()
    throws Exception {

    try {
      final BDMCorrespondenceDetails corrDetails =
        new BDMCorrespondenceDetails();

      final PDCPersonDetails personDtls = createPDCPerson();

      corrDetails.concernRoleID = personDtls.concernRoleID;
      corrDetails.toClientIsCorrespondent = false;
      corrDetails.toContactConcernRoleID = 0;
      corrDetails.toParticipantRoleID = 0;

      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    } catch (final Exception e) {
      assertEquals(BDMBPOCCT.ERR_TO_RECIPIENT_MANDATORY.toString(),
        e.getLocalizedMessage().toString());
    }
  }

  @Test
  public void
    testValidateCorespondenceDetails_ERR_ONLY_ONE_TO_RECIPIENT_ALLOWED()
      throws Exception {

    try {
      final BDMCorrespondenceDetails corrDetails =
        new BDMCorrespondenceDetails();

      final PDCPersonDetails personOne = createPDCPerson();
      final PDCPersonDetails personTwo = createPDCPerson();

      corrDetails.concernRoleID = personOne.concernRoleID;

      corrDetails.toClientIsCorrespondent = true;
      corrDetails.toParticipantRoleID = personTwo.concernRoleID;

      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    } catch (final Exception e) {
      assertEquals(BDMBPOCCT.ERR_ONLY_ONE_TO_RECIPIENT_ALLOWED.toString(),
        e.getLocalizedMessage().toString());
    }
  }

  @Test
  public void testPopulateNameDetails_Set_Client_Name() throws Exception {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();
    final PDCPersonDetails personDtlsOne = createPDCPerson();
    final PDCPersonDetails personDtlsTwo = createPDCPerson();

    corrDetails.concernRoleID = personDtlsOne.concernRoleID;
    corrDetails.toParticipantRoleID = personDtlsTwo.concernRoleID;

    bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    assertEquals("Test Person", corrDetails.clientName);
  }

  @Test
  public void testPopulateNameDetails_Set_CC_Name() throws Exception {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();
    final PDCPersonDetails personDtlsOne = createPDCPerson();
    final PDCPersonDetails personDtlsTwo = createPDCPerson();

    corrDetails.ccClientIsCorrespondent = true;
    corrDetails.concernRoleID = personDtlsOne.concernRoleID;
    corrDetails.toParticipantRoleID = personDtlsTwo.concernRoleID;

    bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    assertEquals("Test Person", corrDetails.clientName);
  }

  @Test
  public void testSetTOClientMandatoryFields_ERR_PREFERRED_LANGUAGE_MISSING()
    throws Exception {

    try {
      final BDMCorrespondenceDetails corrDetails =
        new BDMCorrespondenceDetails();
      final PDCPersonDetails personDtls = createPDCPerson();
      corrDetails.concernRoleID = personDtls.concernRoleID;
      corrDetails.toClientIsCorrespondent = true;
      corrDetails.toParticipantPreferredLanguage = BDMConstants.EMPTY_STRING;

      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    } catch (final Exception e) {
      final AppException ae =
        new AppException(BDMBPOCCT.ERR_PREFERRED_LANGUAGE_MISSING);
      assertEquals(e.getLocalizedMessage().toString(),
        ae.getLocalizedMessage().toString());
    }
  }

  @Test
  public void testSetTOClientMandatoryFields() throws Exception {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();
    final PDCPersonDetails personDtls = createPDCPerson();
    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 758302;
    corrDetails.templatePath = "/";

    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    assertTrue(wizId.wizardStateID != 0);
  }

  @Test
  public void testSetCCClientMandatoryFields() throws Exception {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personOne = createPDCPerson();
    final PDCPersonDetails personTwo = createPDCPerson();

    corrDetails.concernRoleID = personOne.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;
    corrDetails.ccParticipantRoleID = personTwo.concernRoleID;
    corrDetails.ccContactPreferredLanguage = null;

    bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    assertFalse(corrDetails.ccDetails.ccAddressLineOne.isEmpty());
    assertFalse(corrDetails.ccDetails.ccAddressLineThree.isEmpty());
    assertFalse(corrDetails.ccDetails.ccAddressLineFour.isEmpty());
  }

  @Test
  public void testSetClientMandatoryFields() throws Exception {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();
    final PDCPersonDetails person = createPDCPerson();
    corrDetails.concernRoleID = person.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;
    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);
    assertTrue(wizId.wizardStateID != 0);
  }

  @Test
  public void testGetViewCorrespondence() throws Exception {

    BDMViewCorrespondenceDetails viewDetails =
      new BDMViewCorrespondenceDetails();

    final BDMCommunicationKey key = new BDMCommunicationKey();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

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
    viewDetails = bdmCommunicationObj.getViewCorrespondence(key);

    assertFalse(viewDetails.toAddressData.isEmpty());
    assertFalse(viewDetails.correspondentName.isEmpty());
    assertTrue(viewDetails.communicationID != 0);
    assertFalse(viewDetails.methodTypeCode.isEmpty());
    assertFalse(viewDetails.correspondentType.isEmpty());
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

  @Test
  public void testDownloadCorrespondence() throws Exception {

    final CommunicationIDKey commKey = new CommunicationIDKey();
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

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

    commKey.communicationID = communicationID;
    BDMFileNameAndDataDtls fileNameAndDataDtls = new BDMFileNameAndDataDtls();

    final BDMConcernRoleCommunicationKey bdmKey =
      new BDMConcernRoleCommunicationKey();

    bdmKey.communicationID = communicationID;

    fileNameAndDataDtls = bdmCommunicationObj.downloadCorrespondence(commKey);
    assertTrue(fileNameAndDataDtls.fileContent.length() != 0);
  }

  @Test
  public void testModifyCommunicationStatus() throws Exception {

    final String value = String.valueOf(Math.random());

    final BDMCommunicationStatusDetails bdmCommStatusDtls =
      new BDMCommunicationStatusDetails();

    final BDMConcernRoleCommunication comm =
      BDMConcernRoleCommunicationFactory.newInstance();

    bdmCommStatusDtls.communicationID = createTestCommunication();

    bdmCommStatusDtls.statusCode = COMMUNICATIONSTATUS.RECEIVED;
    bdmCommStatusDtls.statusComments = value;

    bdmCommunicationObj.modifyCommunicationStatus(bdmCommStatusDtls);

    final BDMConcernRoleCommunicationKey key =
      new BDMConcernRoleCommunicationKey();

    key.communicationID = bdmCommStatusDtls.communicationID;
    final BDMConcernRoleCommunicationDtls bdmConcernRoleCommunicationDtls =
      comm.read(key);

    final ConcernRoleCommunication concernRoleCommunication =
      ConcernRoleCommunicationFactory.newInstance();
    final ConcernRoleCommunicationKey communicationKey =
      new ConcernRoleCommunicationKey();
    communicationKey.communicationID = bdmCommStatusDtls.communicationID;

    final ConcernRoleCommunicationDtls dtls =
      concernRoleCommunication.read(communicationKey);

    assertEquals(COMMUNICATIONSTATUS.RECEIVED, dtls.communicationStatus);
    assertEquals(value, bdmConcernRoleCommunicationDtls.statusComments);
  }

  @Test
  public void testmodifyRecordedComm() throws Exception {

    final RecordedCommKey Key = new RecordedCommKey();
    final PersonRegistrationResult person = registerPerson();
    final RecordedCommDetails1 recordedCommDetails =
      new RecordedCommDetails1();

    recordedCommDetails.clientParticipantRoleID =
      person.registrationIDDetails.concernRoleID;

    recordedCommDetails.correspondentParticipantRoleID =
      person.registrationIDDetails.concernRoleID;

    recordedCommDetails.communicationDirection =
      COMMUNICATIONDIRECTION.INCOMING;

    recordedCommDetails.communicationText = "This is the Text Test";

    recordedCommDetails.subject = "This is the Subject Test";

    recordedCommDetails.methodTypeCode = COMMUNICATIONMETHOD.EMAIL;

    recordedCommDetails.correspondentType = CORRESPONDENT.CLIENT;

    recordedCommDetails.communicationTypeCode = COMMUNICATIONTYPE.ENQUIRY_REU;

    recordedCommDetails.communicationDate =
      Date.getCurrentDate().addDays(-10);
    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

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
    Key.communicationID = communicationID;

    bdmCommunicationObj.modifyRecordedComm(Key, recordedCommDetails);

  }

  @Test
  public void testreadRecordedComm() throws Exception {

    final RecordedCommKey Key = new RecordedCommKey();

    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

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
    Key.communicationID = communicationID;

    bdmCommunicationObj.readRecordedComm(Key);

  }

  @Test
  public void testlistThirdPartyContactsForCommunication() throws Exception {

    final ConcernRoleIDCaseIDKey Key = new ConcernRoleIDCaseIDKey();

    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

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

    final PersonRegistrationResult person = registerPerson();
    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);
    final long caseID = icResult.createCaseResult.integratedCaseID;

    Key.caseID = caseID;
    Key.concernRoleID = person.registrationIDDetails.concernRoleID;

    bdmCommunicationObj.listThirdPartyContactsForCommunication(Key);

  }

  @Test
  public void testreadBDMConcernRoleCommunicationDetails() throws Exception {

    final BDMConcernRoleCommunicationKey Key =
      new BDMConcernRoleCommunicationKey();

    final WizardPersistentState wizardObj = new WizardPersistentState();
    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

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
    Key.communicationID = communicationID;

    bdmCommunicationObj.readBDMConcernRoleCommunicationDetails(Key);

  }

  @Test
  public void testModifyCommunicationStatus_ERR_STATUS_COMMENTS_MANDATORY()
    throws Exception {

    try {
      final BDMCommunicationStatusDetails bdmCommStatusDtls =
        new BDMCommunicationStatusDetails();

      bdmCommStatusDtls.statusComments = null;
      bdmCommunicationObj.modifyCommunicationStatus(bdmCommStatusDtls);

    } catch (final Exception e) {
      assertEquals(BDMBPOCCT.ERR_STATUS_COMMENTS_MANDATORY.toString(),
        e.getLocalizedMessage().toString());
    }
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

  public PersonRegistrationDetails getPersonRegistrationDetails()
    throws AppException, InformationalException {

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.APRIL, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);

    final PersonRegistrationDetails personRegistrationDetails =
      new PersonRegistrationDetails();

    personRegistrationDetails.firstForename = "Test";
    personRegistrationDetails.surname = "Person";
    personRegistrationDetails.sex = curam.codetable.GENDER.getDefaultCode();
    personRegistrationDetails.dateOfBirth = birthday;
    personRegistrationDetails.registrationDate =
      curam.util.type.Date.getCurrentDate();
    personRegistrationDetails.currentMaritalStatus =
      curam.codetable.MARITALSTATUS.MARRIED;
    personRegistrationDetails.nationality =
      curam.codetable.NATIONALITY.DEFAULTCODE;
    personRegistrationDetails.birthCountry = curam.codetable.COUNTRY.GB;
    personRegistrationDetails.preferredLanguage = BDMLANGUAGE.ENGLISHL;

    personRegistrationDetails.addressType =
      CONCERNROLEADDRESSTYPE.PRIVATE.toString();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;

    addressFieldDetails.suiteNum = "4994";
    addressFieldDetails.addressLine1 = "Heatherleigh";
    addressFieldDetails.addressLine2 = "Cooksville";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "L5A 1V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    personRegistrationDetails.addressData = otherAddressData.addressData;

    personRegistrationDetails.paymentFrequency = "100100100";
    personRegistrationDetails.methodOfPmtCode = METHODOFDELIVERY.CHEQUE;
    personRegistrationDetails.currencyType =
      curam.codetable.CURRENCY.DEFAULTCODE;

    personRegistrationDetails.contactEmailAddress = "test@gmail.com";
    personRegistrationDetails.contactEmailType = EMAILTYPE.PERSONAL;

    return personRegistrationDetails;
  }

  public long createTestCommunication()
    throws AppException, InformationalException {

    final BDMCorrespondenceDetails corrDetails =
      new BDMCorrespondenceDetails();

    final PDCPersonDetails personDtls = createPDCPerson();

    corrDetails.concernRoleID = personDtls.concernRoleID;
    corrDetails.toClientIsCorrespondent = true;

    corrDetails.templateID = 805577;
    corrDetails.templatePath = "/";

    final WizardStateID wizId =
      bdmCommunicationObj.saveCorrespondenceWizard(corrDetails);

    final BDMCorrespondenceWizardKey wizardKey =
      new BDMCorrespondenceWizardKey();
    wizardKey.wizardStateID = wizId.wizardStateID;

    wizardKey.submitOpt = BDMCCTSUBMITOPT.VIEW;

    final BDMConcernRoleCommunication comm =
      BDMConcernRoleCommunicationFactory.newInstance();

    final BDMTemplateDetails bdmTemplateDetails =
      bdmCommunicationObj.createCorrespondence(wizardKey);

    final BDMCorrespondenceSearchKey key = new BDMCorrespondenceSearchKey();
    key.workItemID = bdmTemplateDetails.workItemID;
    final BDMConcernRoleCommunicationDtls bdmConcernRoleCommunicationDtls =
      comm.getCorrespondenceByWorkItemID(key);

    return bdmConcernRoleCommunicationDtls.communicationID;
  }

  @Test
  public void testCreateRecordedCommWithReturningIDForFECase()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = registerPerson();

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    final RecordedCommDetails1 recordedCommDetails =
      new RecordedCommDetails1();

    recordedCommDetails.caseID = caseID;

    recordedCommDetails.clientParticipantRoleID =
      person.registrationIDDetails.concernRoleID;

    recordedCommDetails.correspondentParticipantRoleID =
      person.registrationIDDetails.concernRoleID;

    recordedCommDetails.communicationDirection =
      COMMUNICATIONDIRECTION.INCOMING;

    recordedCommDetails.communicationText = "This is the Text Test";

    recordedCommDetails.subject = "This is the Subject Test";

    recordedCommDetails.methodTypeCode = COMMUNICATIONMETHOD.EMAIL;

    recordedCommDetails.correspondentType = CORRESPONDENT.CLIENT;

    recordedCommDetails.communicationTypeCode = COMMUNICATIONTYPE.ENQUIRY_REU;

    recordedCommDetails.communicationDate =
      Date.getCurrentDate().addDays(-10);

    final ConcernRoleCommKeyOut concernRoleCommKeyOut = kBdmCommunication
      .createRecordedCommWithReturningID(recordedCommDetails);

    assertEquals(
      concernRoleCommKeyOut.communicationID != CuramConst.kLongZero, true);

    /*
     * TODO Need to uncomment it when able to run workflow deferred processing
     * using Junit test.
     * BDMConcernRoleCommRMKey bdmCncrCmmRmKey = new BDMConcernRoleCommRMKey();
     *
     * bdmCncrCmmRmKey.communicationID = concernRoleCommKeyOut.communicationID;
     *
     * BizObjAssocSearchDetails bizObjAssocSearchDetails =
     * getBizObjectCommunicationID(bdmCncrCmmRmKey);
     *
     * assertEquals(
     * bizObjAssocSearchDetails.bizObjAssocID != CuramConst.kLongZero, true);
     *
     * assertEquals(bizObjAssocSearchDetails.taskID != CuramConst.kLongZero,
     * true);
     */

  }

}
