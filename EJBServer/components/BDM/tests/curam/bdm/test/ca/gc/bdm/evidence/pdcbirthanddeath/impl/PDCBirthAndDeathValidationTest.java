package curam.bdm.test.ca.gc.bdm.evidence.pdcbirthanddeath.impl;

import curam.bdm.test.ca.gc.bdm.foreignsource.impl.ForeignCountryValidationHelperTest;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.codetable.impl.EVIDENCECHANGEREASONEntry;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKeyList;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceModifyDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.impl.EvidenceMap;
import curam.core.sl.infrastructure.impl.StandardEvidenceInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleKey;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.PDCBirthAndDeathRuleSet.impl.PDCBirthAndDeath;
import curam.creole.ruleclass.PDCBirthAndDeathRuleSet.impl.PDCBirthAndDeath_Factory;
import curam.creole.ruleclass.PDCBirthAndDeathValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.PDCBirthAndDeathValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Task 57387 DEV: Implement 'Birth and Death Details' evidence

/**
 * Test configured rules-based validations for PDCBirthAndDeath evidence.
 */
public class PDCBirthAndDeathValidationTest
  extends curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest {

  private static final String EVD_ATTRIBUTE_DOD = "dateOfDeath";

  private static final String EVD_ATTRIBUTE_DEATH_DATE_NOTIFIED =
    "dateNotifiedOfDeath";

  private static final String EVD_ATTRIBUTE_COUNTRY_OF_DEATH =
    "countryOfDeath";

  private static final String ERR_DATE_NOTIFIED_OF_DEATH_EMPTY_FOR_DOD =
    "‘Date Notified of Death’ must be entered if ‘Date of Death’ is entered.";

  private static final String ERR_DATE_NOTIFIED_OF_DEATH_MUST_BE_ON_OR_AFTER_DOD =
    "‘Date of Death Notified’ must not be earlier than ‘Date of Death’.";

  private static final String ERR_COUNTRY_OF_DEATH_EMPTY_FOR_DOD =
    "‘Country of Death’ must be entered if ‘Date of Death’ is entered.";

  private Session session;

  private final ForeignCountryValidationHelperTest helper =
    new ForeignCountryValidationHelperTest();

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    this.session =
      Session_Factory.getFactory().newInstance(new ManualRecalculation(),
        new InMemoryDataStorage(new InterpretedRuleObjectFactory()));

  }

  /**
   * Instantiates and returns the evidence rule object.
   *
   * @return
   */
  private PDCBirthAndDeath getEvidence() {

    return PDCBirthAndDeath_Factory.getFactory().newInstance(this.session);

  }

  /**
   * Instatiates and returns a validator rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private ValidationResult getValidator(final PDCBirthAndDeath evidence) {

    final ValidationResult validator =
      ValidationResult_Factory.getFactory().newInstance(session);

    validator.evidence().specifyValue(evidence);

    return validator;

  }

  /**
   * PASS-IF a validation fails when additional required source fields are
   * not entered when the Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void mandatorySourceFieldFailure()
    throws AppException, InformationalException {

    final PDCBirthAndDeath evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmReceivedFromCountry().specifyValue(null);
    evidence.bdmModeOfReceipt().specifyValue(null);

    assertTrue(validator.mandatorySourceFieldFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when additional required source fields
   * are entered when the Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void mandatorySourceFieldPass()
    throws AppException, InformationalException {

    final PDCBirthAndDeath evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);
    final CodeTableItem receivedFromCountry =
      this.helper.getReceivedFromCountry(BDMSOURCECOUNTRY.US);
    final CodeTableItem modeOfReceipt =
      this.helper.getModeOfReceipt(BDMMODEOFRECEIPT.CERTIFIED_APP);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmReceivedFromCountry().specifyValue(receivedFromCountry);
    evidence.bdmModeOfReceipt().specifyValue(modeOfReceipt);

    assertFalse(validator.mandatorySourceFieldFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when additional source fields are
   * entered and the Received From is not Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void disallowedSourceFieldFailure()
    throws AppException, InformationalException {

    final PDCBirthAndDeath evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.CLIENT_REPORTED);
    final CodeTableItem receivedFromCountry =
      this.helper.getReceivedFromCountry(BDMSOURCECOUNTRY.US);
    final CodeTableItem modeOfReceipt =
      this.helper.getModeOfReceipt(BDMMODEOFRECEIPT.CERTIFIED_APP);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmReceivedFromCountry().specifyValue(receivedFromCountry);
    evidence.bdmModeOfReceipt().specifyValue(modeOfReceipt);

    assertTrue(validator.disallowedSourceFieldFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when additional source fields are
   * entered and the Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void disallowedSourceFieldPass()
    throws AppException, InformationalException {

    final PDCBirthAndDeath evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);
    final CodeTableItem receivedFromCountry =
      this.helper.getReceivedFromCountry(BDMSOURCECOUNTRY.US);
    final CodeTableItem modeOfReceipt =
      this.helper.getModeOfReceipt(BDMMODEOFRECEIPT.CERTIFIED_APP);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmModeOfReceipt().specifyValue(modeOfReceipt);
    evidence.bdmReceivedFromCountry().specifyValue(receivedFromCountry);

    assertFalse(validator.disallowedSourceFieldFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when an Received From is Foreign
   * Government, Mode of Receipt is Liaison and the Received From Country is one
   * of the restricted countries.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void restrictedCountryFailure()
    throws AppException, InformationalException {

    final PDCBirthAndDeath evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);
    final CodeTableItem receivedFromCountry =
      this.helper.getReceivedFromCountry(BDMSOURCECOUNTRY.US);
    final CodeTableItem modeOfReceipt =
      this.helper.getModeOfReceipt(BDMMODEOFRECEIPT.LIAISON);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmReceivedFromCountry().specifyValue(receivedFromCountry);
    evidence.bdmModeOfReceipt().specifyValue(modeOfReceipt);

    assertTrue(validator.restrictedCountryFailure().getValue());

  }

  /**
   * PASS-IF a validation passes when an Received From is Foreign
   * Government, Mode of Receipt is Liaison and the Received From Country is not
   * one of the restricted countries.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void restrictedCountryPass()
    throws AppException, InformationalException {

    final PDCBirthAndDeath evidence = this.getEvidence();

    final ValidationResult validator = this.getValidator(evidence);

    validator.evidence().specifyValue(evidence);

    final CodeTableItem receivedFrom =
      this.helper.getReceivedFrom(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT);
    final CodeTableItem receivedFromCountry =
      this.helper.getReceivedFromCountry(BDMSOURCECOUNTRY.IRELAND);
    final CodeTableItem modeOfReceipt =
      this.helper.getModeOfReceipt(BDMMODEOFRECEIPT.LIAISON);

    evidence.bdmReceivedFrom().specifyValue(receivedFrom);
    evidence.bdmReceivedFromCountry().specifyValue(receivedFromCountry);
    evidence.bdmModeOfReceipt().specifyValue(modeOfReceipt);

    assertFalse(validator.restrictedCountryFailure().getValue());

  }

  /**
   * PASS-IF a validation fails when death date received is before death date
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Ignore
  @Test
  public void deathDateReceivedAfterDeathDateFailure()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();
    final HashMap<String, Object> attributes = new HashMap<String, Object>();

    attributes.put(EVD_ATTRIBUTE_DOD, Date.fromISO8601("20220715"));
    attributes.put(EVD_ATTRIBUTE_DEATH_DATE_NOTIFIED,
      Date.fromISO8601("20220714"));
    attributes.put(EVD_ATTRIBUTE_COUNTRY_OF_DEATH,
      new CodeTableItem(BDMMODEOFRECEIPT.TABLENAME, "CA"));

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        modifyPDCBirthAndDeathDetailsEvidence(pdcPersonDetails, attributes);
      });

    assertTrue(exception.getMessage()
      .contains(ERR_DATE_NOTIFIED_OF_DEATH_MUST_BE_ON_OR_AFTER_DOD));
  }

  /**
   * PASS-IF a validation fails when dod is entered but not notified date
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void deathDateNotifiedMissingFailure()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();
    final HashMap<String, Object> attributes = new HashMap<String, Object>();

    attributes.put(EVD_ATTRIBUTE_DOD, Date.fromISO8601("20220715"));
    attributes.put(EVD_ATTRIBUTE_COUNTRY_OF_DEATH,
      new CodeTableItem(BDMMODEOFRECEIPT.TABLENAME, "CA"));

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        modifyPDCBirthAndDeathDetailsEvidence(pdcPersonDetails, attributes);
      });

    assertTrue(exception.getMessage()
      .contains(ERR_DATE_NOTIFIED_OF_DEATH_EMPTY_FOR_DOD));
  }

  /**
   * PASS-IF a validation fails when dod is entered but country of death
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void CountryOfDeathMissingFailure()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();
    final HashMap<String, Object> attributes = new HashMap<String, Object>();

    attributes.put(EVD_ATTRIBUTE_DOD, Date.fromISO8601("20220715"));
    attributes.put(EVD_ATTRIBUTE_DEATH_DATE_NOTIFIED,
      Date.fromISO8601("20220716"));

    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        modifyPDCBirthAndDeathDetailsEvidence(pdcPersonDetails, attributes);
      });

    assertTrue(
      exception.getMessage().contains(ERR_COUNTRY_OF_DEATH_EMPTY_FOR_DOD));
  }

  /**
   * PASS-IF no validation fails when all death fields are entered
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void allDeathFieldsEnteredPass()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();
    final HashMap<String, Object> attributes = new HashMap<String, Object>();

    attributes.put(EVD_ATTRIBUTE_DOD, Date.fromISO8601("20220715"));
    attributes.put(EVD_ATTRIBUTE_DEATH_DATE_NOTIFIED,
      Date.fromISO8601("20220716"));
    attributes.put(EVD_ATTRIBUTE_COUNTRY_OF_DEATH,
      new CodeTableItem(BDMMODEOFRECEIPT.TABLENAME, "CA"));

    modifyPDCBirthAndDeathDetailsEvidence(pdcPersonDetails, attributes);

    assertEquals(TransactionInfo.getInformationalManager()
      .obtainInformationalAsString().length, 0);
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
  private void modifyPDCBirthAndDeathDetailsEvidence(
    final PDCPersonDetails details, final HashMap<String, Object> modifyData)
    throws AppException, InformationalException {

    // PDC case key
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    concernRoleKey.concernRoleID = details.concernRoleID;
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
