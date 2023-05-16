package curam.ca.gc.bdmoas.evidence.firstEntryIntoCanada.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.creole.impl.Statics;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdmoas.codetable.impl.BDMOASIMMIGRATIONDOCEntry;
import curam.ca.gc.bdmoas.evidence.constants.impl.OASFirstEntryIntoCanadaDetailsConstants;
import curam.ca.gc.bdmoas.evidence.test.util.impl.BDMOASCaseTest;
import curam.codetable.COUNTRY;
import curam.codetable.PROVINCETYPE;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.codetable.impl.EVIDENCECHANGEREASONEntry;
import curam.core.facade.struct.CreateIntegratedCaseResult;
import curam.core.facade.struct.PersonRegistrationResult;
import curam.core.sl.fact.AlternateNameFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtls;
import curam.core.sl.infrastructure.struct.ECActiveEvidenceDtlsList;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.intf.AlternateName;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.NameAndAgeStruct;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMOASFirstEntryIntoCanadaDetailsRuleSet.impl.BDMOASFirstEntryIntoCanadaDetails;
import curam.creole.ruleclass.BDMOASFirstEntryIntoCanadaDetailsRuleSet.impl.BDMOASFirstEntryIntoCanadaDetails_Factory;
import curam.creole.ruleclass.BDMOASFirstEntryIntoCanadaDetailsSummaryRuleSet.impl.SummaryInformation;
import curam.creole.ruleclass.BDMOASFirstEntryIntoCanadaDetailsSummaryRuleSet.impl.SummaryInformation_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

// 64796 DEV: Implement First Entry Into Canada Details
/**
 * Test configured rules-based validations for First Entry Into Canada Details
 * evidence.
 */
public class BDMOASFirstEntryIntoCanadaDetailsTest extends BDMOASCaseTest {

  private static final String EVD_ATTRIBUTE_DOD = "dateOfDeath";

  private static final String EVD_ATTRIBUTE_COUNTRY_OF_BIRTH =
    "countryOfBirth";

  private static final String EVD_ATTRIBUTE_DEATH_DATE_NOTIFIED =
    "dateNotifiedOfDeath";

  private static final String EVD_ATTRIBUTE_COUNTRY_OF_DEATH =
    "countryOfDeath";

  private Session session;

  @Override
  protected void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * PASS-IF Summary message contains arrival date value from evidence record.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void arrivalDateSummaryTest() {

    final String summaryMessage = "Date of arrival  1998-01-01";

    final BDMOASFirstEntryIntoCanadaDetails evidence = this.getEvidence();
    evidence.arrivalDate().specifyValue(Date.fromISO8601("19980101"));

    final SummaryInformation summary = this.getSummary(evidence);

    assertTrue(summary.summary().getValue().toLocale(Locale.ENGLISH)
      .equals(summaryMessage));
  }

  /**
   * PASS-IF First entry evidence cannot be created due to persons country of
   * birth being Canada from PDC Birth and Death Details evidence.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void firstEntryEvidenceForbiddenForCanadaBornFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String concernRoleName =
      Statics.getParticipantNameFromCaseParticipantRoleID(this.session,
        cprObj.caseParticipantRoleID);

    final String validationMessage =
      "A First Entry Into Canada Details record cannot exist as "
        + concernRoleName + " was born in Canada.";

    // Modify Birth Country on PDC Birth and Death Evidence
    final HashMap<String, Object> birthAndDeathAttributes =
      new HashMap<String, Object>();
    birthAndDeathAttributes.put(EVD_ATTRIBUTE_COUNTRY_OF_BIRTH,
      new CodeTableItem(COUNTRY.TABLENAME, "CA"));
    modifyPDCBirthAndDeathDetailsEvidence(
      person.registrationIDDetails.concernRoleID, birthAndDeathAttributes);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_DATE,
      "20010101");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_CITY,
      "Vancouver");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_PROVINCE,
      PROVINCETYPE.BRITISHCOLUMBIA);
    attributes.put(
      OASFirstEntryIntoCanadaDetailsConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FIRST_ENTRY_INTO_CANADA, attributes,
          getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * PASS-IF First entry evidence cannot be created due to persons date of death
   * from PDC Birth and Death Details evidence after the arrival date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void arrivalDateAfterDodFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final String validationMessage =
      "Date of Arrival cannot be after Date of Death.";

    // Modify Date of Death on PDC Birth and Death Evidence
    final HashMap<String, Object> birthAndDeathAttributes =
      new HashMap<String, Object>();
    birthAndDeathAttributes.put(EVD_ATTRIBUTE_DOD,
      Date.fromISO8601("20210102"));
    birthAndDeathAttributes.put(EVD_ATTRIBUTE_DEATH_DATE_NOTIFIED,
      Date.fromISO8601("20220716"));
    birthAndDeathAttributes.put(EVD_ATTRIBUTE_COUNTRY_OF_DEATH,
      new CodeTableItem(BDMMODEOFRECEIPT.TABLENAME, "CA"));
    modifyPDCBirthAndDeathDetailsEvidence(
      person.registrationIDDetails.concernRoleID, birthAndDeathAttributes);

    // Evidence Details
    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_DATE,
      "20210103");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_CITY,
      "Vancouver");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_PROVINCE,
      PROVINCETYPE.BRITISHCOLUMBIA);
    attributes.put(
      OASFirstEntryIntoCanadaDetailsConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FIRST_ENTRY_INTO_CANADA, attributes,
          getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * PASS-IF First entry evidence cannot be created due to arrival date later
   * than current date.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void arrivalDateInFutureFailure()
    throws AppException, InformationalException {

    final String validationMessage =
      "The Date of Arrival must be on or before the Current Date.";

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final LocalDate futureDate = LocalDate.now().plusMonths(1).plusDays(1);
    final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyyMMdd");

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_DATE,
      formatter.format(futureDate));
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_CITY,
      "Vancouver");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_PROVINCE,
      PROVINCETYPE.BRITISHCOLUMBIA);
    attributes.put(
      OASFirstEntryIntoCanadaDetailsConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FIRST_ENTRY_INTO_CANADA, attributes,
          getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * PASS-IF an informational exception thrown while creating second evidence
   * record of type First Entry Into Canada Details for the same participant.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void multipleEvidenceRecordFailure()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_DATE,
      "20010101");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_CITY,
      "Vancouver");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_PROVINCE,
      PROVINCETYPE.BRITISHCOLUMBIA);
    attributes.put(
      OASFirstEntryIntoCanadaDetailsConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));

    // create first evidence record
    this.createEvidence(integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_FIRST_ENTRY_INTO_CANADA, attributes, getToday());

    getConcernRoleNameAndAge(person.registrationIDDetails.concernRoleID);

    // create second evidene record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FIRST_ENTRY_INTO_CANADA, attributes,
          getToday());
      });

    final String validationMessage = "Evidence record with Participant as '"
      + getConcernRoleNameAndAge(person.registrationIDDetails.concernRoleID)
      + "' already exists.";

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * PASS-IF First entry evidence cannot be created due to missing other
   * immigration document details when the immigration documen type is 'Other'.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void OtherImmigrationDocumentDetailsMissingFailure()
    throws AppException, InformationalException {

    final String validationMessage =
      "'Other' Immigration Document Details must be entered if Immigration Document is 'Other'.";

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_DATE,
      "20010101");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_CITY,
      "Vancouver");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_PROVINCE,
      PROVINCETYPE.BRITISHCOLUMBIA);
    attributes.put(
      OASFirstEntryIntoCanadaDetailsConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.IMMIGRATION_DOC,
      BDMOASIMMIGRATIONDOCEntry.OTHER.getCode());

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FIRST_ENTRY_INTO_CANADA, attributes,
          getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * PASS-IF First entry evidence cannot be created due to immigration
   * document type not being 'Other' when Immigration Document details
   * are entered.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void ImmigrationDocumentTypeOtherExpectedFailure()
    throws AppException, InformationalException {

    final String validationMessage =
      "'Other' Immigration Document Details can only be entered if Immigration Document is 'Other'.";

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_DATE,
      "20010101");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_CITY,
      "Vancouver");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_PROVINCE,
      PROVINCETYPE.BRITISHCOLUMBIA);
    attributes.put(
      OASFirstEntryIntoCanadaDetailsConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(
      OASFirstEntryIntoCanadaDetailsConstants.OTHER_IMMIGRATION_DOCUMENT_DETAILS,
      "Test Immigration Document");

    // create evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        this.createEvidence(integratedCase.integratedCaseID,
          person.registrationIDDetails.concernRoleID,
          CASEEVIDENCEEntry.OAS_FIRST_ENTRY_INTO_CANADA, attributes,
          getToday());
      });

    assertTrue(validationMessage.equals(exception.getMessage()));
  }

  /**
   * PASS-IF First entry evidence record successfully created.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void firstEntryEvidenceCreationSuccess()
    throws AppException, InformationalException {

    final PersonRegistrationResult person = this.registerPerson();
    final CreateIntegratedCaseResult integratedCase =
      this.createIntegratedCase(person.registrationIDDetails.concernRoleID);

    final BDMUtil util = new BDMUtil();
    final CaseParticipantRoleIDStruct cprObj =
      util.getCaseParticipantRoleID(integratedCase.integratedCaseID,
        person.registrationIDDetails.concernRoleID);

    final Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_DATE,
      "20010101");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_CITY,
      "Vancouver");
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.ARRIVAL_PROVINCE,
      PROVINCETYPE.BRITISHCOLUMBIA);
    attributes.put(
      OASFirstEntryIntoCanadaDetailsConstants.CASE_PARTICIPANT_ROLE,
      String.valueOf(cprObj.caseParticipantRoleID));
    attributes.put(OASFirstEntryIntoCanadaDetailsConstants.IMMIGRATION_DOC,
      BDMOASIMMIGRATIONDOCEntry.OTHER.getCode());
    attributes.put(
      OASFirstEntryIntoCanadaDetailsConstants.OTHER_IMMIGRATION_DOCUMENT_DETAILS,
      "Test Immigration Document");

    // create evidence record
    final EIEvidenceKey evidenceKey = this.createEvidence(
      integratedCase.integratedCaseID,
      person.registrationIDDetails.concernRoleID,
      CASEEVIDENCEEntry.OAS_FIRST_ENTRY_INTO_CANADA, attributes, getToday());

    // Apply Evidence changes
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = integratedCase.integratedCaseID;

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    evidenceController.applyAllChanges(caseKey);

    // Check if the evidence record was activated
    final ECActiveEvidenceDtlsList activeEvdList =
      evidenceController.listActive(caseKey);

    // Assert first record activated
    final List<ECActiveEvidenceDtls> filteredList1 = activeEvdList.dtls
      .stream().filter((x) -> x.evidenceID == evidenceKey.evidenceID)
      .collect(Collectors.toList());
    assertTrue(filteredList1.size() == 1);
  }

  private String getConcernRoleNameAndAge(final Long concernRoleID)
    throws AppException, InformationalException {

    final AlternateName alternateName = AlternateNameFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleID;
    final NameAndAgeStruct nameAndAgeStruct =
      alternateName.getNameAndAge(concernRoleKey);

    return nameAndAgeStruct.nameAndAgeString;
  }

  private BDMOASFirstEntryIntoCanadaDetails getEvidence() {

    return BDMOASFirstEntryIntoCanadaDetails_Factory.getFactory()
      .newInstance(this.session);

  }

  private SummaryInformation
    getSummary(final BDMOASFirstEntryIntoCanadaDetails evidence) {

    final SummaryInformation summary =
      SummaryInformation_Factory.getFactory().newInstance(this.session);
    summary.evidence().specifyValue(evidence);

    return summary;
  }

  /**
   * Modify the PDC Birth and Death evidence based on evidence details
   * and map containing the evidence fields and corresponding data.
   *
   * @param details
   * @param modifyData
   * @throws AppException
   * @throws InformationalException
   */
  private void modifyPDCBirthAndDeathDetailsEvidence(final Long concernRoleID,
    final HashMap<String, Object> modifyData)
    throws AppException, InformationalException {

    // PDC case key
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);
    final CaseKey caseKey = new CaseKey();
    caseKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;

    final BDMEvidenceUtil evdUtil = new BDMEvidenceUtil();

    // Read PDC Birth and Death details evidences
    final RelatedIDAndEvidenceTypeKeyList pdcBirthAndDeathEvidenceList =
      evdUtil.getActiveEvidenceIDByEvidenceTypeAndCase(
        PDCConst.PDCBIRTHANDDEATH, caseKey);

    // Pick one evidence details from evidence list
    final RelatedIDAndEvidenceTypeKey evidenceDetails =
      pdcBirthAndDeathEvidenceList.dtls.get(0);

    // Read DynamicEvidenceDataDetails
    final EvidenceMap map =
      curam.core.sl.infrastructure.impl.EvidenceController.getEvidenceMap();
    final StandardEvidenceInterface standardEvidenceInterface =
      map.getEvidenceType(PDCConst.PDCBIRTHANDDEATH);

    final EIEvidenceKey evidenceKey = new EIEvidenceKey();
    evidenceKey.evidenceID = evidenceDetails.relatedID;
    evidenceKey.evidenceType = PDCConst.PDCBIRTHANDDEATH;

    final DynamicEvidenceDataDetails evidenceData =
      (DynamicEvidenceDataDetails) standardEvidenceInterface
        .readEvidence(evidenceKey);

    // Modify evidence data
    for (final Entry<String, Object> attributeEntry : modifyData.entrySet()) {

      final DynamicEvidenceDataAttributeDetails attributeObj =
        evidenceData.getAttribute(attributeEntry.getKey());

      DynamicEvidenceTypeConverter.setAttribute(attributeObj,
        attributeEntry.getValue());
    }

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // evidence descriptor details for this evidence
    final RelatedIDAndEvidenceTypeKey relatedIDAndTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndTypeKey.relatedID = evidenceDetails.relatedID;
    relatedIDAndTypeKey.evidenceType = PDCConst.PDCBIRTHANDDEATH;

    final EvidenceDescriptorDtls evidenceDescriptorDtls =
      EvidenceDescriptorFactory.newInstance()
        .readByRelatedIDAndType(relatedIDAndTypeKey);

    // modify evidence details
    final EIEvidenceModifyDtls modifyEvidenceDetails =
      new EIEvidenceModifyDtls();

    modifyEvidenceDetails.evidenceObject = evidenceData;
    modifyEvidenceDetails.descriptor.assign(evidenceDescriptorDtls);
    modifyEvidenceDetails.descriptor.versionNo =
      evidenceDescriptorDtls.versionNo;
    modifyEvidenceDetails.descriptor.changeReason =
      EVIDENCECHANGEREASONEntry.CORRECTION.getCode();
    modifyEvidenceDetails.descriptor.effectiveFrom = Date.kZeroDate;

    evidenceControllerObj.modifyEvidence(evidenceKey, modifyEvidenceDetails);
  }

}
