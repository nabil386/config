package curam.ca.gc.bdmoas.test.facade.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMACCEPTANCESTATUS;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.codetable.BDMSIGNATURETYPE;
import curam.ca.gc.bdm.facade.verifications.fact.BDMVerificationApplicationFactory;
import curam.ca.gc.bdm.facade.verifications.intf.BDMVerificationApplication;
import curam.ca.gc.bdm.facade.verifications.struct.BDMNewUserProvidedVerificationItemDetails;
import curam.ca.gc.bdmoas.codetable.BDMOASAPPLICATIONSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASBENEFITTYPE;
import curam.ca.gc.bdmoas.codetable.BDMOASLEGALSTATUS;
import curam.ca.gc.bdmoas.codetable.BDMOASMETHODOFAPPLICATION;
import curam.ca.gc.bdmoas.codetable.OASRESIDENCETYPE;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASApplicationDetailsConstants;
import curam.ca.gc.bdmoas.evidence.constants.impl.BDMOASLegalStatusConstants;
import curam.ca.gc.bdmoas.evidence.constants.impl.OASResidencePeriodConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.ca.gc.bdmoas.facade.commonintake.fact.BDMOASApplicationCaseFactory;
import curam.ca.gc.bdmoas.facade.fact.BDMOASProgramAuthorisationFactory;
import curam.ca.gc.bdmoas.facade.intf.BDMOASProgramAuthorisation;
import curam.ca.gc.bdmoas.test.util.impl.BDMOASAESSimulator;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.COUNTRY;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.VERIFICATIONSTATUS;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.commonintake.authorisation.facade.struct.AuthorisationDetails;
import curam.commonintake.codetable.APPLICATIONCASESTATUS;
import curam.commonintake.entity.struct.ApplicationCaseDtls;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseFactory;
import curam.commonintake.facade.struct.ApplicationCaseProgramDetails;
import curam.commonintake.facade.struct.ApplicationRelatedCaseDetails;
import curam.commonintake.facade.struct.ApplicationRelatedCaseList;
import curam.commonintake.facade.struct.ProgramDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.CaseHeader;
import curam.core.sl.entity.struct.CaseIDAndStatusKey;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKeyList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtls;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.ParticipantKeyStruct;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.workflow.impl.EnactmentService;
import curam.verification.facade.infrastructure.fact.VerificationApplicationFactory;
import curam.verification.facade.infrastructure.intf.VerificationApplication;
import curam.verification.facade.infrastructure.struct.CaseEvidenceVerificationDisplayDetails;
import curam.verification.facade.infrastructure.struct.CaseEvidenceVerificationDisplayDetailsList;
import curam.verification.facade.infrastructure.struct.IntegratedCaseVerificationDetailsList;
import curam.verification.facade.infrastructure.struct.ListVerificationItemNameDetails;
import curam.verification.facade.infrastructure.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.struct.CaseEvidenceVerificationDetails;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class is used to validate the OAS Authorization functionality.
 *
 * @author pranav.agarwal
 *
 */
public class BDMOASAuthorizePDCTest extends BDMOASCaseTest {

  @Override
  protected boolean shouldCommit() {

    return Boolean.FALSE;
  }

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();
    // mock enactment service
    mockEnactmentService();
    mockUserObjects();
  }

  // dynamic evidence attributes
  public static final String SIGNATURE_TYPE = "signatureType";

  public static final String DATE_SIGNED = "dateSigned";

  public static final String PARTICIPANT = "participant";

  public static final String IS_SIGNED_ON_BEHALF = "isSigningOnBehalf";

  public static final String IS_WITNESS = "isWitness";

  public static final long OAS_PROG_ID = 920000;

  public static final long APP_CASE_ADMIN_ID = 920000;

  /**
   * This test method will check if the NRT Deduction is created on the
   * OAS Product Activation.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void test_authorizePDC()
    throws AppException, InformationalException {

    // register the person
    final PersonRegistrationResult primary = this.registerPerson();

    // create preferred language
    final BDMUtil bdmUtil = new BDMUtil();
    // create ContactPreference
    bdmUtil.createContactPreferenceEvidence(
      primary.registrationIDDetails.concernRoleID, BDMLANGUAGE.ENGLISHL,
      BDMLANGUAGE.ENGLISHL, CuramConst.gkEmpty);

    // calling the method to create application case
    final ApplicationCaseKey applicationCaseKey = this.createApplicationCase(
      primary.registrationIDDetails.concernRoleID, APP_CASE_ADMIN_ID);
    // log the message
    Trace.kTopLevelLogger
      .info("Application Case ID - " + applicationCaseKey.applicationCaseID);

    // add evidences
    // get the CPR record
    final CaseParticipantRoleIDStruct cprObj =
      bdmUtil.getCaseParticipantRoleID(applicationCaseKey.applicationCaseID,
        primary.registrationIDDetails.concernRoleID);

    // get current date in YYYYMMDD format using the current date
    final String currentDateString =
      formattedDate(Date.getCurrentDate(), "yyyyMMdd");

    // create Consent and Declaration Evidence
    final Map<String, String> attributes = new HashMap<>();
    // populate the attributes
    attributes.put(SIGNATURE_TYPE, BDMSIGNATURETYPE.SIGNED);
    attributes.put(DATE_SIGNED, currentDateString);
    attributes.put(PARTICIPANT, String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(IS_SIGNED_ON_BEHALF, "false");
    attributes.put(IS_WITNESS, "false");
    // calling the method to create evidence
    this.createEvidence(applicationCaseKey.applicationCaseID,
      primary.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.BDM_CONSENT_DECLARATION, attributes, getToday());

    // create Legal Status Evidence
    final Map<String, String> legalAttributes = new HashMap<String, String>();
    // populate the attributes
    legalAttributes.put(BDMOASLegalStatusConstants.LEGAL_STATUS,
      BDMOASLEGALSTATUS.CANADIAN_CITIZEN);
    legalAttributes.put(BDMOASLegalStatusConstants.START_DATE, "19500101");
    legalAttributes.put(BDMOASLegalStatusConstants.PARTICIPANT,
      String.valueOf(cprObj.caseParticipantRoleID));
    // calling the method to create evidence
    this.createEvidence(applicationCaseKey.applicationCaseID,
      primary.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_LEGAL_STATUS, legalAttributes, getToday());

    // create Application Details evidence
    final Map<String, String> appAttributes = new HashMap<String, String>();
    // populate the attributes
    appAttributes.put(BDMOASApplicationDetailsConstants.BENEFIT_TYPE,
      BDMOASBENEFITTYPE.OAS_PENSION);
    appAttributes.put(BDMOASApplicationDetailsConstants.APPLICATION_STATUS,
      BDMOASAPPLICATIONSTATUS.ACTIVE);
    appAttributes.put(BDMOASApplicationDetailsConstants.METHOD_OF_APPLICATION,
      BDMOASMETHODOFAPPLICATION.PAPER);
    appAttributes.put(BDMOASApplicationDetailsConstants.RECEIPT_DATE,
      formattedDate(Date.getCurrentDate(), "yyyyMMdd"));
    appAttributes.put(OASResidencePeriodConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    // calling the method to create evidence
    this.createEvidence(applicationCaseKey.applicationCaseID,
      primary.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_APPLICATION_DETAILS, appAttributes, getToday());

    // create Residence Period evidence
    final Map<String, String> residenceAttributes =
      new HashMap<String, String>();
    // populate the attributes
    residenceAttributes.put(OASResidencePeriodConstants.COUNTRY, COUNTRY.CA);
    residenceAttributes.put(OASResidencePeriodConstants.RESIDENCE_TYPE,
      OASRESIDENCETYPE.RESIDENCE);
    residenceAttributes.put(OASResidencePeriodConstants.START_DATE,
      "19500101");
    residenceAttributes.put(OASResidencePeriodConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    // calling the method to create evidence
    this.createEvidence(applicationCaseKey.applicationCaseID,
      primary.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_RESIDENCE_PERIOD, residenceAttributes,
      getToday());

    // read concern role
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    // populate the concern role ID
    concernRoleKey.concernRoleID =
      primary.registrationIDDetails.concernRoleID;
    // calling the method to read the concern role details
    final ConcernRoleDtls concernRoleDtls =
      ConcernRoleFactory.newInstance().read(concernRoleKey);

    // Update the Verification to mark as Verified.
    final VerificationApplication verificationApplication =
      VerificationApplicationFactory.newInstance();
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = applicationCaseKey.applicationCaseID;
    // calling the method to list outstanding verification for application case
    final CaseEvidenceVerificationDisplayDetailsList caseEvidenceVerificationDisplayDetailsList =
      verificationApplication
        .listOutstandingVerificationDetailsForCaseEvidence(caseKey);

    // new instance
    final BDMVerificationApplication bdmVerificationApplication =
      BDMVerificationApplicationFactory.newInstance();
    // new reference
    VDIEDLinkKey vdiedLinkKey;
    BDMNewUserProvidedVerificationItemDetails userProvidedVerificationItemDetails;
    // iterate the list
    for (final CaseEvidenceVerificationDisplayDetails details : caseEvidenceVerificationDisplayDetailsList.dtls) {

      // new instance
      vdiedLinkKey = new VDIEDLinkKey();
      // assign the VDIED Link ID
      vdiedLinkKey.dtls.VDIEDLinkID = details.vDIEDLinkID;
      // read all user provided verification items
      final ListVerificationItemNameDetails listVerificationItemNameDetails =
        verificationApplication
          .readAllUserProvidedVerificationItems(vdiedLinkKey);

      // new instance
      userProvidedVerificationItemDetails =
        new BDMNewUserProvidedVerificationItemDetails();
      // populate the details
      userProvidedVerificationItemDetails.acceptanceStatus =
        BDMACCEPTANCESTATUS.BDMVIS8000;
      userProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
        CONCERNROLETYPE.PERSON;
      userProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.providerName =
        concernRoleDtls.concernRoleName;
      userProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
        details.vDIEDLinkID;
      userProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
        listVerificationItemNameDetails.listVerificationItemNameDetailsList.listVerificationItemNameDetails
          .get(0).verificationItemUtilizationID;

      // calling the method to clear the verifications
      bdmVerificationApplication
        .newUserProvidedVerificationItem(userProvidedVerificationItemDetails);
    }
    // skip the errors
    TransactionInfo.setInformationalManager();

    // check if the verification items are verified
    final ParticipantKeyStruct key = new ParticipantKeyStruct();
    // populate the concern role ID
    key.participantID = primary.registrationIDDetails.concernRoleID;
    // get the list of participant verification details
    final IntegratedCaseVerificationDetailsList caseVerificationDetailsList =
      VerificationApplicationFactory.newInstance()
        .listParticipantVerificationDetails(key);
    // iterate the list
    for (final CaseEvidenceVerificationDetails caseEvidenceVerificationDetails : caseVerificationDetailsList.dtls.dtls) {
      // check for the status
      assertEquals(VERIFICATIONSTATUS.VERIFIED,
        caseEvidenceVerificationDetails.verificationStatus);
    }

    // assign to current user
    final curam.commonintake.facade.struct.ApplicationCaseKey appCaseKey =
      new curam.commonintake.facade.struct.ApplicationCaseKey();
    // assign the application case ID
    appCaseKey.caseID = applicationCaseKey.applicationCaseID;
    // calling the method to assign to current user
    // this will assign and close the task
    ApplicationCaseFactory.newInstance().assignToCurrentUser(appCaseKey);

    // Add Benefit Program
    final ApplicationCaseProgramDetails det =
      new ApplicationCaseProgramDetails();
    // populate the details
    det.applicationCaseID = applicationCaseKey.applicationCaseID;
    det.dateProgramAdded = Date.getCurrentDate();
    // OAS
    det.programTypeID = OAS_PROG_ID;
    // calling the method to add program
    ApplicationCaseFactory.newInstance().addProgram(det);

    // Authorize the Benefit
    final AuthorisationDetails authorisationDetails =
      new AuthorisationDetails();
    // populate the details
    authorisationDetails.applicationCaseID =
      applicationCaseKey.applicationCaseID;
    // retrieve the program ID
    final Iterator<ProgramDetails> iterProgDtls = BDMOASApplicationCaseFactory
      .newInstance().listPrograms(applicationCaseKey).dtls.dtls.iterator();
    while (iterProgDtls.hasNext()) {
      final ProgramDetails dtl = iterProgDtls.next();
      // assign the program ID
      authorisationDetails.intakeProgramApplicationID = dtl.programID;
    }
    authorisationDetails.createNewInd = Boolean.TRUE;
    authorisationDetails.primaryClientID =
      primary.registrationIDDetails.concernRoleID;

    // calling the method to authorize the benefit
    final BDMOASProgramAuthorisation programAuth =
      BDMOASProgramAuthorisationFactory.newInstance();
    programAuth.authorise(authorisationDetails);

    // set the properties to true to run in the same transaction
    Configuration.setProperty("curam.test.stubdeferredprocessing", "true");
    Configuration
      .setProperty("curam.test.stubdeferredprocessinsametransaction", "true");
    // enact deferred process
    TransactionInfo.enactStubbedDeferredProcessCalls();

    // enact AES
    enactAES(applicationCaseKey.applicationCaseID);
    // set the properties to true to run in the same transaction
    Configuration.setProperty("curam.test.stubdeferredprocessing", "true");
    Configuration
      .setProperty("curam.test.stubdeferredprocessinsametransaction", "true");
    // enact deferred process
    TransactionInfo.enactStubbedDeferredProcessCalls();

    // read application case
    final ApplicationCaseDtls applicationCaseDtls =
      curam.commonintake.entity.fact.ApplicationCaseFactory.newInstance()
        .read(applicationCaseKey);
    // application case status should be closed
    assertEquals(APPLICATIONCASESTATUS.CLOSED, applicationCaseDtls.status);

    // search cases associated with the application
    final ApplicationRelatedCaseList applicationRelatedCaseList =
      ApplicationCaseFactory.newInstance()
        .listClientCases(applicationCaseKey);
    // check the size of related cases for the application case
    // it should be two
    assertEquals(2, applicationRelatedCaseList.caseDtls.size());

    // entity instance
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    CaseHeaderKey caseHeaderKey;
    // iterate the list
    for (final ApplicationRelatedCaseDetails caseDetails : applicationRelatedCaseList.caseDtls) {
      // new instance
      caseHeaderKey = new CaseHeaderKey();
      // assign the case ID
      caseHeaderKey.caseID = caseDetails.caseID;
      // calling the method to read the case header details
      final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);

      // check for the case type code
      if (CASETYPECODE.INTEGRATEDCASE
        .equalsIgnoreCase(caseHeaderDtls.caseTypeCode)) {
        // IC case status should be open
        assertEquals(CASESTATUS.OPEN, caseHeaderDtls.statusCode);

      } else if (CASETYPECODE.PRODUCTDELIVERY
        .equalsIgnoreCase(caseHeaderDtls.caseTypeCode)) {
        // IC case status should be open
        assertEquals(CASESTATUS.ACTIVE, caseHeaderDtls.statusCode);

      } else {
        // fail the scenario
        fail();
      }
    }
  }

  /**
   * This method will be used to format the date in passed format.
   *
   * @param date - This holds the input date.
   * @param format - This holds the date format.
   * @return the formatted date.
   */
  private String formattedDate(final Date date, final String format) {

    final SimpleDateFormat dateFormat = new SimpleDateFormat(format);

    return dateFormat.format(date.getCalendar().getTime());
  }

  /*
   * This will be used to mock enactment service.
   */
  private void mockEnactmentService() {

    // Mock the Enactment Service to enact the workflow in the same transaction
    new MockUp<EnactmentService>() {

      @Mock
      public long startProcess(final String processName,
        final List<? extends Object> enactmentStructs)
        throws AppException, InformationalException {

        return EnactmentService.startProcessInV3CompatibilityMode(processName,
          enactmentStructs);
      }
    };
  }

  /**
   * This will be used to mock user in the same transaction.
   */
  public void mockUserObjects() {

    // Mock the transaction user
    new MockUp<TransactionInfo>() {

      @Mock
      public String getProgramUser()
        throws AppException, InformationalException {

        // return transaction worker as case worker
        return "caseworker";
      }
    };
  }

  /**
   * This method will be used to enact AES workflow.
   *
   * @param applicationCaseID - This holds the application case ID.
   * @throws AppException
   * @throws InformationalException
   */
  public void enactAES(final long applicationCaseID)
    throws AppException, InformationalException {

    // new instance of case key
    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    // assign the application case ID
    caseKey.caseID = applicationCaseID;

    // calling the method to get the list of active evidences
    final ECActiveEvidenceDtlsList ecActiveEvidenceDtlsList =
      EvidenceControllerFactory.newInstance().listActive(caseKey);

    // instance of input object
    final EvidenceDescriptorKeyList list = new EvidenceDescriptorKeyList();
    // new reference
    EvidenceDescriptorKey evidenceDescriptorKey;
    // iterate the list of active evidences
    for (final ECActiveEvidenceDtls activeEvidenceDtls : ecActiveEvidenceDtlsList.dtls) {
      // new instance of evidence descriptor key
      evidenceDescriptorKey = new EvidenceDescriptorKey();
      // assign the evidence descriptor ID
      evidenceDescriptorKey.evidenceDescriptorID =
        activeEvidenceDtls.evidenceDescriptorID;
      // add to the input list object
      list.dtls.add(evidenceDescriptorKey);
    }

    // search for in edit evidences
    final CaseIDAndStatusKey caseIDAndStatusKey = new CaseIDAndStatusKey();
    // populate the application case ID
    caseIDAndStatusKey.caseID = applicationCaseID;
    // pass the status as In Edit
    caseIDAndStatusKey.statusCode = EVIDENCEDESCRIPTORSTATUS.INEDIT;
    // calling the method to search In Edit evidences for application case
    final EvidenceDescriptorDtlsList descriptorDtlsList =
      EvidenceDescriptorFactory.newInstance()
        .searchByCaseIDAndStatus(caseIDAndStatusKey);

    // iterate the list of in edit evidences
    for (final EvidenceDescriptorDtls descriptorDtls : descriptorDtlsList.dtls) {
      // new instance
      evidenceDescriptorKey = new EvidenceDescriptorKey();
      // assign the evidence descriptor ID
      evidenceDescriptorKey.evidenceDescriptorID =
        descriptorDtls.evidenceDescriptorID;
      // add to the input list object
      list.dtls.add(evidenceDescriptorKey);
    }

    // calling the method to enact AES workflow for the passed evidences
    new BDMOASAESSimulator().shareEvidence(list, applicationCaseID);
  }
}
