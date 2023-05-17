
package curam.bdm.test.ca.gc.bdm.facade.verifications.impl;

import com.google.inject.Inject;
import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.codetable.BDMACCEPTANCESTATUS;
import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.codetable.BDMATTACHREJECTIONREASON;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.fec.fact.BDMForeignEngagementCaseFactory;
import curam.ca.gc.bdm.facade.fec.struct.BDMFECaseDetails;
import curam.ca.gc.bdm.facade.participant.struct.BDMPersonRegistrationDetails;
import curam.ca.gc.bdm.facade.pdcperson.fact.BDMPDCPersonFactory;
import curam.ca.gc.bdm.facade.pdcperson.intf.BDMPDCPerson;
import curam.ca.gc.bdm.facade.verifications.impl.BDMVerificationApplication;
import curam.ca.gc.bdm.facade.verifications.struct.BDMCaseEvidenceVerificationDisplayDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMCaseEvidenceVerificationDisplayDetailsList;
import curam.ca.gc.bdm.facade.verifications.struct.BDMCreateVerificationAttachmentDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMModifyVerificationAttachmentDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMNewUserProvidedVerificationItemDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMReadVerificationAttachmentDetails;
import curam.ca.gc.bdm.facade.verifications.struct.BDMReadVerificationAttachmentLinkKey;
import curam.ca.gc.bdm.facade.verifications.struct.BDMUserProvidedVerificationItemKey;
import curam.ca.gc.bdm.facade.verifications.struct.BDMVerificationAttachmentLinkKey;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidenceVO;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.evidence.BDMIncomeEvidenceTest;
import curam.codetable.CASEPRIORITY;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.VERIFICATIONSTATUS;
import curam.codetable.VERIFICATIONTYPE;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.intf.IntegratedCase;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.CreateIntegratedCaseResultAndMessages;
import curam.core.facade.struct.ListMemberDetails;
import curam.core.facade.struct.ListMemberDetailsKey;
import curam.core.facade.struct.PersonRegistrationDetails;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.ParticipantKeyStruct;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.pdc.impl.PDCConst;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.type.Blob;
import curam.util.type.Date;
import curam.verification.facade.infrastructure.fact.VerificationApplicationFactory;
import curam.verification.facade.infrastructure.struct.IntegratedCaseVerificationDetailsList;
import curam.verification.facade.infrastructure.struct.NewUserProvidedVerificationItemDetails;
import curam.verification.sl.infrastructure.entity.fact.VDIEDLinkFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationFactory;
import curam.verification.sl.infrastructure.entity.fact.VerificationItemProvidedFactory;
import curam.verification.sl.infrastructure.entity.intf.VDIEDLink;
import curam.verification.sl.infrastructure.entity.intf.Verification;
import curam.verification.sl.infrastructure.entity.intf.VerificationItemProvided;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkDtls;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkIDAndDataItemIDDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VerificationDtls;
import curam.verification.sl.infrastructure.entity.struct.VerificationItemProvidedDtls;
import curam.verification.sl.infrastructure.struct.CaseEvidenceVerificationDetails;
import java.util.Iterator;
import mockit.Tested;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author amod.gole
 * 17664 Amod Gole 03/07/2022 Junit for User Views Items for Verification
 *
 * @Altered version 1.1
 * 23374 Siva 05/12/2022 Junit for Verifications Add Proof / Add Attachment /
 * ModiFy Attachment
 *
 * * 60494 nhamde 07/11/2022 Junit for Verifications / Add Attachment extension/
 * Add Attachment file size
 * Validate attachment extension and attachment file size
 */

public class BDMVerificationApplicationTest extends CuramServerTestJUnit4 {

  @Inject
  BDMIncomeEvidenceTest bdmIncomeEvidenceTest;

  @Tested
  BDMIdentificationEvidence bdmIdentificationEvidence;

  @Inject
  BDMVerificationApplication bdmVerificationApplication;

  BDMEvidenceUtilsTest bdmEvidenceUtilsTest = new BDMEvidenceUtilsTest();

  // @Mocked
  // VerificationConfigurationCacheUtils verificationConfigurationCacheUtils;

  public BDMVerificationApplicationTest() {

    bdmIdentificationEvidence = new BDMIdentificationEvidence();
    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Mar. 6, 2022
   *
   * @throws java.lang.Exception
   * void
   */
  @Before
  public void setUp() throws Exception {

  }

  /**
   * Mar. 7, 2022
   * Junit method to check for the new fields added on Verification Item List
   *
   * @throws AppException
   * @throws InformationalException
   * void
   */
  @Test
  public void testlistVerificationDetailsforCaseEvidence1()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails =
      bdmIncomeEvidenceTest.createPDCPerson();

    final ApplicationCaseKey appCaseKey = bdmIncomeEvidenceTest
      .createApplicationCase(pdcPersonDetails.concernRoleID);

    // create Income Evidence
    final EvidenceKey key = bdmIncomeEvidenceTest.createIncomeEvidence(
      appCaseKey, pdcPersonDetails.concernRoleID, "1000", "2020");
    // Check income evidence is created
    assertTrue(key.evidenceID != 0L);

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    // fetch the verification Item List
    caseKey.caseID = appCaseKey.applicationCaseID;
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);
    // Add proof for verification item
    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator =
      bdmCaseEvidenceVerificationDisplayDetailsList.dtls.iterator();
    while (iterator.hasNext()) {
      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator.next();
      final NewUserProvidedVerificationItemDetails newUserProvidedVerificationItemDetails =
        new NewUserProvidedVerificationItemDetails();
      // Get the caseParticipant RoleID
      final ListMemberDetailsKey listMemberDetailsKey =
        new ListMemberDetailsKey();
      listMemberDetailsKey.caseID = appCaseKey.applicationCaseID;

      final IntegratedCase integratedCase =
        curam.core.facade.fact.IntegratedCaseFactory.newInstance();
      final ListMemberDetails listMemberDetails =
        integratedCase.listCaseParticipantsWithAge(listMemberDetailsKey);

      // Map the data for Proof
      newUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseID =
        appCaseKey.applicationCaseID;

      newUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.vDIEDLinkID;
      newUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
        80000l;
      newUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus =
        "RST1";

      newUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived =
        Date.getCurrentDate();
      newUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded =
        Date.getCurrentDate();
      newUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseParticipantConcernRoleID =
        listMemberDetails.memberDetailsList.dtls.get(0).participantRoleID;
      newUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
        "RL1";
      newUserProvidedVerificationItemDetails.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;

      // Call OOTB method to update the proof
      bdmVerificationApplication.newUserProvidedVerificationItem(
        newUserProvidedVerificationItemDetails);

    }
    // Fetch the list after adding prrof to the case
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList1 =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);

    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator1 =
      bdmCaseEvidenceVerificationDisplayDetailsList1.dtls.iterator();
    while (iterator1.hasNext()) {
      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator1.next();
      // Check for Date Added
      assertTrue(bdmCaseEvidenceVerificationDisplayDetails.dateAdded
        .equals(Date.getCurrentDate()));
      // Check for Date Received
      assertTrue(bdmCaseEvidenceVerificationDisplayDetails.dateReceived
        .equals(Date.getCurrentDate()));
      // Check for Document Type
      assertTrue(bdmCaseEvidenceVerificationDisplayDetails.documentType
        .equals("Notice of Assessment"));
      // Check for Provided By
      assertTrue(bdmCaseEvidenceVerificationDisplayDetails.documentProvidedBy
        .equals("Unauthenticated Test User"));

    }
  }

  /**
   * Add Attachment
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCreateVerificationAttachment()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails =
      bdmIncomeEvidenceTest.createPDCPerson();

    final ApplicationCaseKey appCaseKey = bdmIncomeEvidenceTest
      .createApplicationCase(pdcPersonDetails.concernRoleID);

    // create Income Evidence
    final EvidenceKey key = bdmIncomeEvidenceTest.createIncomeEvidence(
      appCaseKey, pdcPersonDetails.concernRoleID, "1000", "2020");
    // Check income evidence is created
    assertTrue(key.evidenceID != 0L);

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    // fetch the verification Item List
    caseKey.caseID = appCaseKey.applicationCaseID;
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);
    // Add proof for verification item
    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator =
      bdmCaseEvidenceVerificationDisplayDetailsList.dtls.iterator();
    while (iterator.hasNext()) {
      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator.next();
      final BDMNewUserProvidedVerificationItemDetails newInput =
        new BDMNewUserProvidedVerificationItemDetails();
      // Get the caseParticipant RoleID
      final ListMemberDetailsKey listMemberDetailsKey =
        new ListMemberDetailsKey();
      listMemberDetailsKey.caseID = appCaseKey.applicationCaseID;

      final IntegratedCase integratedCase =
        curam.core.facade.fact.IntegratedCaseFactory.newInstance();
      final ListMemberDetails listMemberDetails =
        integratedCase.listCaseParticipantsWithAge(listMemberDetailsKey);

      // Map the data for Proof
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseID =
        appCaseKey.applicationCaseID;

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.vDIEDLinkID;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
        80000;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseParticipantConcernRoleID =
        listMemberDetails.memberDetailsList.dtls.get(0).participantRoleID;
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
        "RL1";

      newInput.acceptanceStatus = BDMACCEPTANCESTATUS.BDMVIS8000;

      newInput.rejectionReason = BDMATTACHREJECTIONREASON.BDMVRR8004;

      newInput.otherComments = "Other tetsing Comments";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.statusCode =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachedFileInd =
        false;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID =
        100000034;

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation =
        "SUB";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference =
        "subbbb2";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
        Date.getCurrentDate();

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.verificationAttachmentLinkID =
        10000003;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description =
        "FileName";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
        "RST1";

      final BDMUserProvidedVerificationItemKey out =
        bdmVerificationApplication.newUserProvidedVerificationItem(newInput);

      // Calling create attachment

      final BDMCreateVerificationAttachmentDetails inAttachmentData =
        new BDMCreateVerificationAttachmentDetails();
      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.verificationItemProvidedID =
        out.bdmUserProvidedVerificationItemKey.userProvidedVerificationItemKey.verificationItemProvidedID;
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.statusCode =
        "RST1";

      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.attachedFileInd =
        false;
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.attachmentID =
        100000035;

      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.fileLocation =
        "SUB";
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.fileReference =
        "subbbb2";

      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.receiptDate =
        Date.getCurrentDate();

      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.verificationAttachmentLinkID =
        10000004;
      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.description =
        "FileName";
      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.recordStatus =
        "RST1";

      inAttachmentData.bdmVerificationAttachmentLinkDetails.acceptanceStatus =
        BDMACCEPTANCESTATUS.BDMVIS8001;
      inAttachmentData.bdmVerificationAttachmentLinkDetails.rejectionReason =
        BDMATTACHREJECTIONREASON.BDMVRR8004;
      inAttachmentData.bdmVerificationAttachmentLinkDetails.otherComments =
        "Other Attachment Comments";

      bdmVerificationApplication
        .createVerificationAttachment(inAttachmentData);

    }
    // Fetch the list after adding prrof to the case
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList1 =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);

    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator1 =
      bdmCaseEvidenceVerificationDisplayDetailsList1.dtls.iterator();
    while (iterator1.hasNext()) {

      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator1.next();

      // Check for Verification Attachment Creation.
      assertTrue(
        bdmCaseEvidenceVerificationDisplayDetails.dtls.verificationStatus
          .equals(VERIFICATIONSTATUS.NOTVERIFIED));
    }

  }

  // task 60494 - validations/support for file size and type - test attachment
  // file extensions and attachment file size
  @Test
  public void testCreateVerificationAttachmentValidations()
    throws AppException, InformationalException {

    // final PDCPersonDetails pdcPersonDetails =
    // bdmIncomeEvidenceTest.createPDCPerson();

    final PersonRegistrationResult person = registerPerson();
    // final ApplicationCaseKey appCaseKey = bdmIncomeEvidenceTest
    // .createApplicationCase(pdcPersonDetails.concernRoleID);

    final CreateIntegratedCaseResultAndMessages icResult =
      createFEC(person.registrationIDDetails.concernRoleID);

    final long caseID = icResult.createCaseResult.integratedCaseID;

    // create Income Evidence
    // final EvidenceKey key = bdmIncomeEvidenceTest.createIncomeEvidence(
    // appCaseKey, pdcPersonDetails.concernRoleID, "1000", "2020");
    // Check income evidence is created
    // assertTrue(key.evidenceID != 0L);

    final String kSin = "344626361";
    // Create Concern Role PDC

    final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVO =
      new BDMIdentificationEvidenceVO();
    bdmIdentificationEvidenceVO
      .setAltIDType(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER);
    bdmIdentificationEvidenceVO.setAlternateID(kSin);
    bdmIdentificationEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmIdentificationEvidenceVO.setToDate(Date.kZeroDate);
    // create identification evidence
    bdmIdentificationEvidence.createIdentificationEvidence(
      person.registrationIDDetails.concernRoleID, bdmIdentificationEvidenceVO,
      EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT);

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    // fetch the verification Item List
    caseKey.caseID = caseID;
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);
    // Add proof for verification item
    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator =
      bdmCaseEvidenceVerificationDisplayDetailsList.dtls.iterator();
    while (iterator.hasNext()) {
      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator.next();
      final BDMNewUserProvidedVerificationItemDetails newInput =
        new BDMNewUserProvidedVerificationItemDetails();
      // Get the caseParticipant RoleID
      final ListMemberDetailsKey listMemberDetailsKey =
        new ListMemberDetailsKey();
      listMemberDetailsKey.caseID = caseID;

      final IntegratedCase integratedCase =
        curam.core.facade.fact.IntegratedCaseFactory.newInstance();
      final ListMemberDetails listMemberDetails =
        integratedCase.listCaseParticipantsWithAge(listMemberDetailsKey);

      // Map the data for Proof
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseID =
        caseID;

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.vDIEDLinkID;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
        80000;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseParticipantConcernRoleID =
        listMemberDetails.memberDetailsList.dtls.get(0).participantRoleID;
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
        "RL1";

      newInput.acceptanceStatus = BDMACCEPTANCESTATUS.BDMVIS8001;

      newInput.rejectionReason = BDMATTACHREJECTIONREASON.BDMVRR8004;

      newInput.otherComments = "Other tetsing Comments";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.statusCode =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachedFileInd =
        false;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID =
        100000034;

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
        Date.getCurrentDate();

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.verificationAttachmentLinkID =
        10000003;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description =
        "FileName";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
        "RST1";

      final BDMUserProvidedVerificationItemKey out =
        bdmVerificationApplication.newUserProvidedVerificationItem(newInput);

      // Calling create attachment

      final BDMCreateVerificationAttachmentDetails inAttachmentData =
        new BDMCreateVerificationAttachmentDetails();
      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.verificationItemProvidedID =
        out.bdmUserProvidedVerificationItemKey.userProvidedVerificationItemKey.verificationItemProvidedID;
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.statusCode =
        "RST1";

      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.attachedFileInd =
        false;
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.attachmentID =
        100000035;

      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.receiptDate =
        Date.getCurrentDate();

      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.verificationAttachmentLinkID =
        10000004;
      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.description =
        "FileName";
      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.recordStatus =
        "RST1";

      inAttachmentData.bdmVerificationAttachmentLinkDetails.acceptanceStatus =
        BDMACCEPTANCESTATUS.BDMVIS8001;
      inAttachmentData.bdmVerificationAttachmentLinkDetails.rejectionReason =
        BDMATTACHREJECTIONREASON.BDMVRR8004;
      inAttachmentData.bdmVerificationAttachmentLinkDetails.otherComments =
        "Other Attachment Comments";
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.attachmentContents =
        new Blob(new byte[1024]);
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.attachmentName =
        "file.zip";

      try {
        bdmVerificationApplication.newUserProvidedVerificationItem(newInput);
      } catch (final AppException e) {
        // Test invalid attachment extension
        assertTrue(e.getMessage().contains("Attachment File type must be"));
      }

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentContents =
        new Blob(new byte[10174706]);
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName =
        "test.png";
      try {
        bdmVerificationApplication.newUserProvidedVerificationItem(newInput);
      } catch (final AppException e) {
        // Test invalid attachment when attachment size in over 5MB
        assertTrue(e.getMessage()
          .contains("The uploaded file size cannot exceed 5MB"));
      }

    }

  }

  protected CreateIntegratedCaseResultAndMessages createFEC(
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

  protected PersonRegistrationResult registerPerson()
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

  /**
   * Add Proof
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testNewUserProvidedVerificationItem()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails =
      bdmIncomeEvidenceTest.createPDCPerson();

    final ApplicationCaseKey appCaseKey = bdmIncomeEvidenceTest
      .createApplicationCase(pdcPersonDetails.concernRoleID);

    // create Income Evidence
    final EvidenceKey key = bdmIncomeEvidenceTest.createIncomeEvidence(
      appCaseKey, pdcPersonDetails.concernRoleID, "1000", "2020");
    // Check income evidence is created
    assertTrue(key.evidenceID != 0L);

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    // fetch the verification Item List
    caseKey.caseID = appCaseKey.applicationCaseID;
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);
    // Add proof for verification item
    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator =
      bdmCaseEvidenceVerificationDisplayDetailsList.dtls.iterator();
    while (iterator.hasNext()) {
      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator.next();
      final BDMNewUserProvidedVerificationItemDetails newInput =
        new BDMNewUserProvidedVerificationItemDetails();
      // Get the caseParticipant RoleID
      final ListMemberDetailsKey listMemberDetailsKey =
        new ListMemberDetailsKey();
      listMemberDetailsKey.caseID = appCaseKey.applicationCaseID;

      final IntegratedCase integratedCase =
        curam.core.facade.fact.IntegratedCaseFactory.newInstance();
      final ListMemberDetails listMemberDetails =
        integratedCase.listCaseParticipantsWithAge(listMemberDetailsKey);

      // Map the data for Proof
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseID =
        appCaseKey.applicationCaseID;

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.vDIEDLinkID;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
        80000;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseParticipantConcernRoleID =
        listMemberDetails.memberDetailsList.dtls.get(0).participantRoleID;
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
        "RL1";

      newInput.acceptanceStatus = BDMACCEPTANCESTATUS.BDMVIS8001;

      newInput.rejectionReason = BDMATTACHREJECTIONREASON.BDMVRR8004;

      newInput.otherComments = "Other tetsing Comments";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.statusCode =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachedFileInd =
        false;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID =
        100000034;

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation =
        "SUB";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference =
        "subbbb2";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
        Date.getCurrentDate();

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.verificationAttachmentLinkID =
        10000003;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description =
        "FileName";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
        "RST1";

      bdmVerificationApplication.newUserProvidedVerificationItem(newInput);

    }
    // Fetch the list after adding prrof to the case
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList1 =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);

    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator1 =
      bdmCaseEvidenceVerificationDisplayDetailsList1.dtls.iterator();
    while (iterator1.hasNext()) {

      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator1.next();

      assertTrue(
        bdmCaseEvidenceVerificationDisplayDetails.dtls.verificationStatus
          .equals(VERIFICATIONSTATUS.NOTVERIFIED));

    }
  }

  // task 60494 - validations/support for file size and type - test attachment
  // file extension and attachment file size
  @Test
  public void testNewUserProvidedVerificationItemAttachmentValidations()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails =
      bdmIncomeEvidenceTest.createPDCPerson();

    final ApplicationCaseKey appCaseKey = bdmIncomeEvidenceTest
      .createApplicationCase(pdcPersonDetails.concernRoleID);

    // create Income Evidence
    final EvidenceKey key = bdmIncomeEvidenceTest.createIncomeEvidence(
      appCaseKey, pdcPersonDetails.concernRoleID, "1000", "2020");
    // Check income evidence is created
    assertTrue(key.evidenceID != 0L);

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    // fetch the verification Item List
    caseKey.caseID = appCaseKey.applicationCaseID;
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);
    // Add proof for verification item
    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator =
      bdmCaseEvidenceVerificationDisplayDetailsList.dtls.iterator();
    while (iterator.hasNext()) {
      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator.next();
      final BDMNewUserProvidedVerificationItemDetails newInput =
        new BDMNewUserProvidedVerificationItemDetails();
      // Get the caseParticipant RoleID
      final ListMemberDetailsKey listMemberDetailsKey =
        new ListMemberDetailsKey();
      listMemberDetailsKey.caseID = appCaseKey.applicationCaseID;

      final IntegratedCase integratedCase =
        curam.core.facade.fact.IntegratedCaseFactory.newInstance();
      final ListMemberDetails listMemberDetails =
        integratedCase.listCaseParticipantsWithAge(listMemberDetailsKey);

      // Map the data for Proof
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseID =
        appCaseKey.applicationCaseID;

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.vDIEDLinkID;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
        80000;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseParticipantConcernRoleID =
        listMemberDetails.memberDetailsList.dtls.get(0).participantRoleID;
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
        "RL1";

      newInput.acceptanceStatus = BDMACCEPTANCESTATUS.BDMVIS8001;

      newInput.rejectionReason = BDMATTACHREJECTIONREASON.BDMVRR8004;

      newInput.otherComments = "Other tetsing Comments";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.statusCode =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachedFileInd =
        false;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID =
        100000034;

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
        Date.getCurrentDate();

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.verificationAttachmentLinkID =
        10000003;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description =
        "FileName";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentContents =
        new Blob(new byte[1024]);
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName =
        "test.zip";
      try {
        bdmVerificationApplication.newUserProvidedVerificationItem(newInput);
      } catch (final AppException e) {
        // Test invalid attachment extension
        assertTrue(e.getMessage().contains("Attachment File type must be"));
      }
      newInput.acceptanceStatus = "RST1";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentContents =
        new Blob(new byte[10174706]);
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName =
        "test.png";
      try {
        bdmVerificationApplication.newUserProvidedVerificationItem(newInput);
      } catch (final AppException e) {
        // Test invalid attachment when attachment size in over 5MB
        assertTrue(e.getMessage()
          .contains("The uploaded file size cannot exceed 5MB"));
      }
    }
  }

  /**
   * Add Attachment
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testModifyVerificationAttachment()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails =
      bdmIncomeEvidenceTest.createPDCPerson();

    final ApplicationCaseKey appCaseKey = bdmIncomeEvidenceTest
      .createApplicationCase(pdcPersonDetails.concernRoleID);

    // create Income Evidence
    final EvidenceKey key = bdmIncomeEvidenceTest.createIncomeEvidence(
      appCaseKey, pdcPersonDetails.concernRoleID, "1000", "2020");
    // Check income evidence is created
    assertTrue(key.evidenceID != 0L);

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    // fetch the verification Item List
    caseKey.caseID = appCaseKey.applicationCaseID;
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);
    // Add proof for verification item
    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator =
      bdmCaseEvidenceVerificationDisplayDetailsList.dtls.iterator();
    while (iterator.hasNext()) {
      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator.next();
      final BDMNewUserProvidedVerificationItemDetails newInput =
        new BDMNewUserProvidedVerificationItemDetails();
      // Get the caseParticipant RoleID
      final ListMemberDetailsKey listMemberDetailsKey =
        new ListMemberDetailsKey();
      listMemberDetailsKey.caseID = appCaseKey.applicationCaseID;

      final IntegratedCase integratedCase =
        curam.core.facade.fact.IntegratedCaseFactory.newInstance();
      final ListMemberDetails listMemberDetails =
        integratedCase.listCaseParticipantsWithAge(listMemberDetailsKey);

      // Map the data for Proof
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseID =
        appCaseKey.applicationCaseID;

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.vDIEDLinkID;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
        80000;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseParticipantConcernRoleID =
        listMemberDetails.memberDetailsList.dtls.get(0).participantRoleID;
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
        "RL1";

      newInput.acceptanceStatus = BDMACCEPTANCESTATUS.BDMVIS8001;

      newInput.rejectionReason = BDMATTACHREJECTIONREASON.BDMVRR8004;

      newInput.otherComments = "Other tetsing Comments";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.statusCode =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachedFileInd =
        false;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID =
        100000034;

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation =
        "SUB";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference =
        "subbbb2";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
        Date.getCurrentDate();

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.verificationAttachmentLinkID =
        10000003;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description =
        "FileName";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
        "RST1";

      final BDMUserProvidedVerificationItemKey out =
        bdmVerificationApplication.newUserProvidedVerificationItem(newInput);

      // Calling modify attachment

      final BDMCreateVerificationAttachmentDetails inAttachmentData =
        new BDMCreateVerificationAttachmentDetails();
      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.verificationItemProvidedID =
        out.bdmUserProvidedVerificationItemKey.userProvidedVerificationItemKey.verificationItemProvidedID;
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.statusCode =
        "RST1";

      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.attachedFileInd =
        false;
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.attachmentID =
        100000035;

      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.fileLocation =
        "SUB";
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.fileReference =
        "subbbb2";

      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;
      inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.receiptDate =
        Date.getCurrentDate();

      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.verificationAttachmentLinkID =
        10000004;
      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.description =
        "FileName";
      inAttachmentData.createVerificationAttachmentDetails.details.createLinkDtls.recordStatus =
        "RST1";

      inAttachmentData.bdmVerificationAttachmentLinkDetails.acceptanceStatus =
        BDMACCEPTANCESTATUS.BDMVIS8001;
      inAttachmentData.bdmVerificationAttachmentLinkDetails.rejectionReason =
        BDMATTACHREJECTIONREASON.BDMVRR8004;
      inAttachmentData.bdmVerificationAttachmentLinkDetails.otherComments =
        "Other Attachment Comments";

      final BDMVerificationAttachmentLinkKey bdmVerificationAttachmentLinkKey =
        bdmVerificationApplication
          .createVerificationAttachment(inAttachmentData);

      if (inAttachmentData.createVerificationAttachmentDetails.details.createAttachmentDtls.attachmentID == 100000034) {
        final BDMModifyVerificationAttachmentDetails bdmModifyVerificationAttachmentDetails =
          new BDMModifyVerificationAttachmentDetails();
        bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyAttachmentDtls.attachmentID =
          100000034;
        bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyLinkDtls.verificationItemProvidedID =
          out.bdmUserProvidedVerificationItemKey.userProvidedVerificationItemKey.verificationItemProvidedID;
        bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyLinkDtls.verificationAttachmentLinkID =
          bdmVerificationAttachmentLinkKey.bdmVerificationAttachmentLinkKey.dtls.verificationAttachmentLinkID;

        Trace.kTopLevelLogger.info("  verificationAttachmentLinkID :  "
          + bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyLinkDtls.verificationAttachmentLinkID);

        Trace.kTopLevelLogger.info("  verificationItemProvidedID :  "
          + bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyLinkDtls.verificationItemProvidedID);
        bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyAttachmentDtls.fileLocation =
          "MODIFYSUB";
        bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyAttachmentDtls.fileReference =
          "Modifysubbbb2";

        bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyAttachmentDtls.documentType =
          DOCUMENTTYPE.DIRECT_DEPOSIT;
        bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyAttachmentDtls.receiptDate =
          Date.getCurrentDate();

        bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyLinkDtls.description =
          "ModifyFileName";
        bdmModifyVerificationAttachmentDetails.bdmDtls.details.modifyLinkDtls.recordStatus =
          "RST1";

        bdmModifyVerificationAttachmentDetails.bdmVerificationAttachmentLinkDetails.acceptanceStatus =
          BDMACCEPTANCESTATUS.BDMVIS8001;
        bdmModifyVerificationAttachmentDetails.bdmVerificationAttachmentLinkDetails.rejectionReason =
          BDMATTACHREJECTIONREASON.BDMVRR8004;
        bdmModifyVerificationAttachmentDetails.bdmVerificationAttachmentLinkDetails.otherComments =
          "Modify Other Attachment Comments";

        bdmVerificationApplication.modifyVerificationAttachment(
          bdmModifyVerificationAttachmentDetails);
      }

    }
    // Fetch the list after adding prrof to the case
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList1 =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);

    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator1 =
      bdmCaseEvidenceVerificationDisplayDetailsList1.dtls.iterator();
    while (iterator1.hasNext()) {

      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator1.next();

      // Check for Verification Attachment Creation.
      assertTrue(
        bdmCaseEvidenceVerificationDisplayDetails.dtls.verificationStatus
          .equals(VERIFICATIONSTATUS.NOTVERIFIED));
    }

  }

  /**
   * Add Proof
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testNewUserProvidedVerificationItem_PersonLevel()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();

    final EIEvidenceKey eiEvidenceKey =
      bdmEvidenceUtilsTest.createIdentificationEvidence(pdcPersonDetails,
        "366698942", CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER, true);

    assertTrue(eiEvidenceKey.evidenceID != 0L);

    // createVerification

    final boolean isPhoneEmailPreferred = false;
    final boolean isOnlyTaxCredit = false;

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EIEvidenceKey eiEvidenceKey2 = new EIEvidenceKey();
    eiEvidenceKey2.evidenceID = eiEvidenceKey.evidenceID;
    eiEvidenceKey2.evidenceType = PDCConst.PDCIDENTIFICATION;

    final EIEvidenceReadDtls eiEvidenceReadDtls =
      evidenceControllerObj.readEvidence(eiEvidenceKey2);

    final VDIEDLinkDtls vDIEDLinkDtls = createVDIEDLink(eiEvidenceReadDtls);

    createVerification(pdcPersonDetails, vDIEDLinkDtls);

    createVerificationItemProvided(vDIEDLinkDtls);

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();

    final ParticipantKeyStruct participantKeyStruct =
      new ParticipantKeyStruct();
    participantKeyStruct.participantID = pdcPersonDetails.concernRoleID;

    // fetch the verification Item List
    final IntegratedCaseVerificationDetailsList integratedCaseVerificationDetailsList =
      bdmVerificationApplication
        .listParticipantVerificationDetails(participantKeyStruct);

    assertTrue(!integratedCaseVerificationDetailsList.dtls.dtls.isEmpty());

    CaseEvidenceVerificationDetails caseEvidenceVerificationDetails = null;
    // Add proof for verification item
    final Iterator<CaseEvidenceVerificationDetails> iterator =
      integratedCaseVerificationDetailsList.dtls.dtls.iterator();
    while (iterator.hasNext()) {
      caseEvidenceVerificationDetails = iterator.next();
      final BDMNewUserProvidedVerificationItemDetails newInput =
        new BDMNewUserProvidedVerificationItemDetails();

      // Map the data for Proof

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
        caseEvidenceVerificationDetails.vDIEDLinkID;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
        80000;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded =
        Date.getCurrentDate();

      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
        "RL1";

      newInput.acceptanceStatus = BDMACCEPTANCESTATUS.BDMVIS8001;

      newInput.rejectionReason = BDMATTACHREJECTIONREASON.BDMVRR8004;

      newInput.otherComments = "Other tetsing Comments";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.statusCode =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachedFileInd =
        false;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID =
        100000034;

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation =
        "SUB";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference =
        "subbbb2";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
        Date.getCurrentDate();

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.verificationAttachmentLinkID =
        10000003;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description =
        "FileName";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
        "RST1";

      bdmVerificationApplication.newUserProvidedVerificationItem(newInput);

    }
    // Fetch the list after adding prrof to the case
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList1 =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);

    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator1 =
      bdmCaseEvidenceVerificationDisplayDetailsList1.dtls.iterator();
    while (iterator1.hasNext()) {

      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator1.next();

      assertTrue(
        bdmCaseEvidenceVerificationDisplayDetails.dtls.verificationStatus
          .equals(VERIFICATIONSTATUS.NOTVERIFIED));

    }
  }

  private void
    createVerificationItemProvided(final VDIEDLinkDtls vDIEDLinkDtls)
      throws AppException, InformationalException {

    final VerificationItemProvidedDtls verificationItemProvidedDtls =
      new VerificationItemProvidedDtls();
    verificationItemProvidedDtls.VDIEDLinkID = vDIEDLinkDtls.VDIEDLinkID;
    verificationItemProvidedDtls.verificationItemProvidedID = 100;
    verificationItemProvidedDtls.verificationItemUtilizationID = 80001;
    verificationItemProvidedDtls.addedByUser = "bill";
    verificationItemProvidedDtls.receivedFrom = "PersonName";
    verificationItemProvidedDtls.recordStatus = RECORDSTATUS.NORMAL;
    final VerificationItemProvided verificationItemProvidedFactory =
      VerificationItemProvidedFactory.newInstance();
    verificationItemProvidedFactory.insert(verificationItemProvidedDtls);
  }

  private void createVerification(final PDCPersonDetails pdcPersonDetails,
    final VDIEDLinkDtls vDIEDLinkDtls)
    throws AppException, InformationalException {

    final Verification verificationEntityFactory =
      VerificationFactory.newInstance();

    final VerificationDtls verificationDtls = new VerificationDtls();
    verificationDtls.VDIEDLinkID = vDIEDLinkDtls.VDIEDLinkID;
    verificationDtls.verificationID = 100;
    verificationDtls.verificationLinkedID = pdcPersonDetails.concernRoleID;
    verificationDtls.verificationStatus = VERIFICATIONSTATUS.NOTVERIFIED;
    verificationDtls.verificationLinkedType = VERIFICATIONTYPE.NONCASEDATA;
    verificationDtls.verificationRequirementID = 50004;

    verificationEntityFactory.insert(verificationDtls);
  }

  private VDIEDLinkDtls
    createVDIEDLink(final EIEvidenceReadDtls eiEvidenceReadDtls)
      throws AppException, InformationalException {

    final VDIEDLinkDtls vDIEDLinkDtls = new VDIEDLinkDtls();
    vDIEDLinkDtls.VDIEDLinkID = 100;
    vDIEDLinkDtls.verifiableDataItemID = 50004;
    vDIEDLinkDtls.evidenceDescriptorID =
      eiEvidenceReadDtls.descriptor.evidenceDescriptorID;
    final VDIEDLink vDIEDLink = VDIEDLinkFactory.newInstance();

    vDIEDLink.insert(vDIEDLinkDtls);
    return vDIEDLinkDtls;
  }

  // task 60494 - validations/support for file size and type - test attachment
  // file extension and attachment file size
  @Test
  public void
    testNewUserProvidedVerificationItemAttachmentValidations_person()
      throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails =
      bdmIncomeEvidenceTest.createPDCPerson();

    final ApplicationCaseKey appCaseKey = bdmIncomeEvidenceTest
      .createApplicationCase(pdcPersonDetails.concernRoleID);

    // create Income Evidence
    final EvidenceKey key = bdmIncomeEvidenceTest.createIncomeEvidence(
      appCaseKey, pdcPersonDetails.concernRoleID, "1000", "2020");
    // Check income evidence is created
    assertTrue(key.evidenceID != 0L);

    final curam.core.struct.CaseKey caseKey = new curam.core.struct.CaseKey();
    // fetch the verification Item List
    caseKey.caseID = appCaseKey.applicationCaseID;
    final BDMCaseEvidenceVerificationDisplayDetailsList bdmCaseEvidenceVerificationDisplayDetailsList =
      bdmVerificationApplication
        .listVerificationDetailsforCaseEvidence1(caseKey);
    // Add proof for verification item
    final Iterator<BDMCaseEvidenceVerificationDisplayDetails> iterator =
      bdmCaseEvidenceVerificationDisplayDetailsList.dtls.iterator();
    while (iterator.hasNext()) {
      final BDMCaseEvidenceVerificationDisplayDetails bdmCaseEvidenceVerificationDisplayDetails =
        iterator.next();
      final BDMNewUserProvidedVerificationItemDetails newInput =
        new BDMNewUserProvidedVerificationItemDetails();
      // Get the caseParticipant RoleID
      final ListMemberDetailsKey listMemberDetailsKey =
        new ListMemberDetailsKey();
      listMemberDetailsKey.caseID = appCaseKey.applicationCaseID;

      final IntegratedCase integratedCase =
        curam.core.facade.fact.IntegratedCaseFactory.newInstance();
      final ListMemberDetails listMemberDetails =
        integratedCase.listCaseParticipantsWithAge(listMemberDetailsKey);

      // Map the data for Proof
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseID =
        appCaseKey.applicationCaseID;

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
        bdmCaseEvidenceVerificationDisplayDetails.dtls.vDIEDLinkID;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
        80000;
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.recordStatus =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateReceived =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.dateAdded =
        Date.getCurrentDate();
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.caseParticipantConcernRoleID =
        listMemberDetails.memberDetailsList.dtls.get(0).participantRoleID;
      newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
        "RL1";

      newInput.acceptanceStatus = BDMACCEPTANCESTATUS.BDMVIS8001;

      newInput.rejectionReason = BDMATTACHREJECTIONREASON.BDMVRR8004;

      newInput.otherComments = "Other tetsing Comments";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.statusCode =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachedFileInd =
        false;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID =
        100000034;

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
        DOCUMENTTYPE.INCOME_RELATED;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
        Date.getCurrentDate();

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.verificationAttachmentLinkID =
        10000003;
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description =
        "FileName";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
        "RST1";

      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentContents =
        new Blob(new byte[1024]);
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName =
        "test.zip";
      try {
        bdmVerificationApplication.newUserProvidedVerificationItem(newInput);
      } catch (final AppException e) {
        // Test invalid attachment extension
        assertTrue(e.getMessage().contains("Attachment File type must be"));
      }
      newInput.acceptanceStatus = "RST1";
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentContents =
        new Blob(new byte[10174706]);
      newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentName =
        "test.png";
      try {
        bdmVerificationApplication.newUserProvidedVerificationItem(newInput);
      } catch (final AppException e) {
        // Test invalid attachment when attachment size in over 5MB
        assertTrue(e.getMessage()
          .contains("The uploaded file size cannot exceed 5MB"));
      }
    }
  }

  /**
   * Add Proof Not verfied test
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testNewUserProvidedVerificationItem_NotVerified()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails =
      bdmIncomeEvidenceTest.createPDCPerson();

    // ChangeIdentification
    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();

    final curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails identificationDetails =
      new curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails();

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.JANUARY, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);
    // Set up
    final String validSIN = "387013592";

    identificationDetails.dtls.concernRoleID = pdcPersonDetails.concernRoleID;
    identificationDetails.dtls.sinNumber = validSIN;
    identificationDetails.dtls.nameEvidenceID = 123l;
    identificationDetails.dtls.firstForename = " John";
    identificationDetails.dtls.lastName = "Deo";
    // identificationDetails.dtls.existingSINNumber = validSIN;
    identificationDetails.dtls.dateOfBirth = birthday;
    identificationDetails.dtls.sinIdentificationStartDate =
      Date.getCurrentDate();
    identificationDetails.dtls.attestationAgree = BDMAGREEATTESTATION.YES;
    identificationDetails.dtls.attestationDate = Date.getCurrentDate();
    identificationDetails.dtls.sinIdentificationStartDate =
      Date.getCurrentDate();
    identificationDetails.dtls.evidenceChangeReason = "ECR2";

    bdmPDCPersonObj.changeIdentificationInfoDetails(identificationDetails);

    // Get Evidecne ID
    final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVO =
      bdmIdentificationEvidence
        .getSINEvidenceValueObject(pdcPersonDetails.concernRoleID);

    final ReturnEvidenceDetails returnEvdDtls = new ReturnEvidenceDetails();
    returnEvdDtls.evidenceKey.evidenceID =
      bdmIdentificationEvidenceVO.getEvidenceID();
    returnEvdDtls.evidenceKey.evType = PDCConst.PDCIDENTIFICATION;

    // Get Evidence Descriptor
    final EvidenceDescriptorKey evidenceDescriptorKey = BDMEvidenceUtil
      .getEvidenceDescriptorKeyForReturnEvidenceDetails(returnEvdDtls);

    // Get VDIEDLInk ID
    final VDIEDLinkIDAndDataItemIDDetailsList vdiedLinkIDAndDataItemIDDetailsList =
      VDIEDLinkFactory.newInstance()
        .readByEvidenceDescriptorID(evidenceDescriptorKey);

    final BDMNewUserProvidedVerificationItemDetails newInput =
      new BDMNewUserProvidedVerificationItemDetails();
    // Map the data for Proof

    newInput.acceptanceStatus = BDMACCEPTANCESTATUS.BDMVIS8001;
    newInput.rejectionReason = BDMATTACHREJECTIONREASON.BDMVRR8000;

    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation =
      "File Location";
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference =
      "File Reference";
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
      Date.getCurrentDate();
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
      DOCUMENTTYPE.PERSONAL_EVIDENCE;

    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description =
      "description"; // newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.d
    newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
      "RL1";
    newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.providerName =
      "ProviderPerson";

    newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
      vdiedLinkIDAndDataItemIDDetailsList.dtls.get(0).vDIEDLinkID;
    newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
      50008;

    /// Add Proof with Rejected status
    bdmVerificationApplication.newUserProvidedVerificationItem(newInput);

    final ParticipantKeyStruct key = new ParticipantKeyStruct();
    key.participantID = pdcPersonDetails.concernRoleID;

    final IntegratedCaseVerificationDetailsList str =
      VerificationApplicationFactory.newInstance()
        .listParticipantVerificationDetails(key);

    assertEquals(VERIFICATIONSTATUS.NOTVERIFIED,
      str.dtls.dtls.get(0).verificationStatus);

  }

  /**
   * Add Proof Not verfied test
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testNewUserProvidedVerificationItem_Verified()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails =
      bdmIncomeEvidenceTest.createPDCPerson();

    // ChangeIdentification
    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();

    final curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails identificationDetails =
      new curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails();

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.JANUARY, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);
    // Set up
    final String validSIN = "387013592";

    identificationDetails.dtls.concernRoleID = pdcPersonDetails.concernRoleID;
    identificationDetails.dtls.sinNumber = validSIN;
    identificationDetails.dtls.nameEvidenceID = 123l;
    identificationDetails.dtls.firstForename = " John";
    identificationDetails.dtls.lastName = "Deo";
    // identificationDetails.dtls.existingSINNumber = validSIN;
    identificationDetails.dtls.dateOfBirth = birthday;
    identificationDetails.dtls.sinIdentificationStartDate =
      Date.getCurrentDate();
    identificationDetails.dtls.attestationAgree = BDMAGREEATTESTATION.YES;
    identificationDetails.dtls.attestationDate = Date.getCurrentDate();
    identificationDetails.dtls.sinIdentificationStartDate =
      Date.getCurrentDate();
    identificationDetails.dtls.evidenceChangeReason = "ECR2";

    bdmPDCPersonObj.changeIdentificationInfoDetails(identificationDetails);

    // Get Evidecne ID
    final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVO =
      bdmIdentificationEvidence
        .getSINEvidenceValueObject(pdcPersonDetails.concernRoleID);

    final ReturnEvidenceDetails returnEvdDtls = new ReturnEvidenceDetails();
    returnEvdDtls.evidenceKey.evidenceID =
      bdmIdentificationEvidenceVO.getEvidenceID();
    returnEvdDtls.evidenceKey.evType = PDCConst.PDCIDENTIFICATION;

    // Get Evidence Descriptor
    final EvidenceDescriptorKey evidenceDescriptorKey = BDMEvidenceUtil
      .getEvidenceDescriptorKeyForReturnEvidenceDetails(returnEvdDtls);

    // Get VDIEDLInk ID
    final VDIEDLinkIDAndDataItemIDDetailsList vdiedLinkIDAndDataItemIDDetailsList =
      VDIEDLinkFactory.newInstance()
        .readByEvidenceDescriptorID(evidenceDescriptorKey);

    final BDMNewUserProvidedVerificationItemDetails newInput =
      new BDMNewUserProvidedVerificationItemDetails();
    // Map the data for Proof

    newInput.acceptanceStatus = BDMACCEPTANCESTATUS.BDMVIS8000;
    // newInput.rejectionReason = BDMATTACHREJECTIONREASON.BDMVRR8000;

    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation =
      "File Location";
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference =
      "File Reference";
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
      Date.getCurrentDate();
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
      DOCUMENTTYPE.PERSONAL_EVIDENCE;

    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description =
      "description"; // newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.d
    newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
      "RL1";
    newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.providerName =
      "ProviderPerson";

    newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
      vdiedLinkIDAndDataItemIDDetailsList.dtls.get(0).vDIEDLinkID;
    newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
      50008;

    /// Add Proof with Rejected status
    bdmVerificationApplication.newUserProvidedVerificationItem(newInput);

    final ParticipantKeyStruct key = new ParticipantKeyStruct();
    key.participantID = pdcPersonDetails.concernRoleID;

    final IntegratedCaseVerificationDetailsList str =
      VerificationApplicationFactory.newInstance()
        .listParticipantVerificationDetails(key);

    // Asert Overall status is set to verified
    assertEquals(VERIFICATIONSTATUS.VERIFIED,
      str.dtls.dtls.get(0).verificationStatus);

  }

  // TASK 113604 - Manage Verifications - R2
  /**
   * Check that attachment link is disabled when attachment is cancelled
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testReadVerificationAttachment()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails =
      bdmIncomeEvidenceTest.createPDCPerson();

    // Change Identification
    final BDMPDCPerson bdmPDCPersonObj = BDMPDCPersonFactory.newInstance();

    final curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails identificationDetails =
      new curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails();

    final java.util.Calendar cal = new java.util.GregorianCalendar(1970,
      java.util.Calendar.JANUARY, 1, 0, 0, 0);
    final curam.util.type.Date birthday = new curam.util.type.Date(cal);
    // Set up
    final String validSIN = "387013592";

    identificationDetails.dtls.concernRoleID = pdcPersonDetails.concernRoleID;
    identificationDetails.dtls.sinNumber = validSIN;
    identificationDetails.dtls.nameEvidenceID = 123l;
    identificationDetails.dtls.firstForename = " John";
    identificationDetails.dtls.lastName = "Doe";
    identificationDetails.dtls.dateOfBirth = birthday;
    identificationDetails.dtls.sinIdentificationStartDate =
      Date.getCurrentDate();
    identificationDetails.dtls.attestationAgree = BDMAGREEATTESTATION.YES;
    identificationDetails.dtls.attestationDate = Date.getCurrentDate();
    identificationDetails.dtls.sinIdentificationStartDate =
      Date.getCurrentDate();
    identificationDetails.dtls.evidenceChangeReason = "ECR2";

    bdmPDCPersonObj.changeIdentificationInfoDetails(identificationDetails);

    // Get Evidence ID
    final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVO =
      bdmIdentificationEvidence
        .getSINEvidenceValueObject(pdcPersonDetails.concernRoleID);

    final ReturnEvidenceDetails returnEvdDtls = new ReturnEvidenceDetails();
    returnEvdDtls.evidenceKey.evidenceID =
      bdmIdentificationEvidenceVO.getEvidenceID();
    returnEvdDtls.evidenceKey.evType = PDCConst.PDCIDENTIFICATION;

    // Get Evidence Descriptor
    final EvidenceDescriptorKey evidenceDescriptorKey = BDMEvidenceUtil
      .getEvidenceDescriptorKeyForReturnEvidenceDetails(returnEvdDtls);

    // Get VDIEDLInk ID
    final VDIEDLinkIDAndDataItemIDDetailsList vdiedLinkIDAndDataItemIDDetailsList =
      VDIEDLinkFactory.newInstance()
        .readByEvidenceDescriptorID(evidenceDescriptorKey);

    final BDMNewUserProvidedVerificationItemDetails newInput =
      new BDMNewUserProvidedVerificationItemDetails();

    newInput.acceptanceStatus = BDMACCEPTANCESTATUS.BDMVIS8000;
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentContents =
      new Blob(new byte[1024]);
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachedFileInd =
      true;
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileLocation =
      "File Location";
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.fileReference =
      "File Reference";
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.receiptDate =
      Date.getCurrentDate();
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.documentType =
      DOCUMENTTYPE.PERSONAL_EVIDENCE;
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createAttachmentDtls.attachmentID =
      100000034;
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.description =
      "description";
    newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.participantType =
      "RL1";
    newInput.newUserProvidedVerificationItemDetails.itemProvidedDetailsdtls.providerName =
      "ProviderPerson";
    newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.VDIEDLinkID =
      vdiedLinkIDAndDataItemIDDetailsList.dtls.get(0).vDIEDLinkID;
    newInput.newUserProvidedVerificationItemDetails.newUserProvidedVerificationInfo.verificationItemUtilizationID =
      50008;
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.attachmentID =
      100000034;
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.verificationAttachmentLinkID =
      10000003;
    newInput.newUserProvidedVerificationItemDetails.createVerificationAttachmentLinkDetails.createLinkDtls.recordStatus =
      "RST2";

    final BDMUserProvidedVerificationItemKey verificationItemKey =
      bdmVerificationApplication.newUserProvidedVerificationItem(newInput);

    final BDMReadVerificationAttachmentDetails bdmReadVerificationAttachmentDetails;

    // read verification attachment details
    final BDMReadVerificationAttachmentLinkKey details =
      new BDMReadVerificationAttachmentLinkKey();

    details.dtlsReadVerificationAttachmentLinkKey.key.attachmentID =
      verificationItemKey.attachmentID;
    details.dtlsReadVerificationAttachmentLinkKey.key.verificationAttachmentLinkID =
      verificationItemKey.verificationAtachmentLinkID;

    // assert attachment link is disabled
    bdmReadVerificationAttachmentDetails =
      bdmVerificationApplication.readVerificationAttachment(details);
    assertNotEquals(
      bdmReadVerificationAttachmentDetails.bdmDtls.details.readLinkDtls.attachmentID,
      100000034);
  }
}
