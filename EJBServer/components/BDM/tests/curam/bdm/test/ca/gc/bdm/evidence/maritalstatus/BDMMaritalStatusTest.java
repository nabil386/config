
package curam.bdm.test.ca.gc.bdm.evidence.maritalstatus;

import curam.bdm.test.ca.gc.bdm.foreignsource.impl.ForeignCountryValidationHelperTest;
import curam.ca.gc.bdm.codetable.BDMMARITALSTATUS;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.MARITALSTATUS;
import curam.core.fact.CaseHeaderFactory;
import curam.core.intf.CaseHeader;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.sl.struct.CaseIDAndParticipantRoleIDKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.struct.CaseHeaderDtlsList;
import curam.creole.execution.session.InterpretedRuleObjectFactory;
import curam.creole.execution.session.ManualRecalculation;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.ruleclass.BDMMaritalStatusRuleSet.impl.BDMMaritalStatus;
import curam.creole.ruleclass.BDMMaritalStatusRuleSet.impl.BDMMaritalStatus_Factory;
import curam.creole.ruleclass.BDMMaritalStatusValidationRuleSet.impl.ValidationResult;
import curam.creole.ruleclass.BDMMaritalStatusValidationRuleSet.impl.ValidationResult_Factory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Task 58899 DEV: Implement Manage Client Marital Status
/**
 * Test configured rules-based validations for BDMMaritalStatus evidence.
 */
public class BDMMaritalStatusTest
  extends curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest {

  private static final String CASE_PARTICIPANT_ROLE = "caseParticipantRoleID";

  private static final String START_DATE = "startDate";

  private static final String END_DATE = "endDate";

  private static final String MARITAL_STATUS = "maritalStatus";

  private static final String ERR_ONLY_ONE_RECORD =
    "A client cannot have more than one Marital Status record.";

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
   * PASS-IF a validation fails when additional required source fields are
   * not entered when the Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void mandatorySourceFieldFailure()
    throws AppException, InformationalException {

    final BDMMaritalStatus evidence = this.getEvidence();
    BDMMaritalStatus_Factory.getFactory().newInstance(this.session);

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

    final BDMMaritalStatus evidence = this.getEvidence();
    BDMMaritalStatus_Factory.getFactory().newInstance(this.session);

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

    final BDMMaritalStatus evidence = this.getEvidence();
    BDMMaritalStatus_Factory.getFactory().newInstance(this.session);

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

    final BDMMaritalStatus evidence = this.getEvidence();
    BDMMaritalStatus_Factory.getFactory().newInstance(this.session);

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

    final BDMMaritalStatus evidence = this.getEvidence();
    BDMMaritalStatus_Factory.getFactory().newInstance(this.session);

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

    final BDMMaritalStatus evidence = this.getEvidence();
    BDMMaritalStatus_Factory.getFactory().newInstance(this.session);

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
   * PASS-IF a validation fails when second Marital Status evidence record is
   * added.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void maritalStatusSingletonFailure()
    throws AppException, InformationalException {

    // Register Person
    final PDCPersonDetails pdcPersonDetails = this.createPDCPerson();

    // Participant Data Case
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final ActiveCasesConcernRoleIDAndTypeKey caseHeaderKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    caseHeaderKey.caseTypeCode = CASETYPECODE.PARTICIPANTDATACASE;
    caseHeaderKey.statusCode = CASESTATUS.ACTIVE;
    caseHeaderKey.concernRoleID = pdcPersonDetails.concernRoleID;
    final CaseHeaderDtlsList pdcList =
      caseHeaderObj.searchByConcernRoleIDType(caseHeaderKey);
    final Long pdcID = pdcList.dtls.get(0).caseID;

    // Case Participant Role
    final CaseParticipantRole cprObj =
      CaseParticipantRoleFactory.newInstance();
    final CaseIDAndParticipantRoleIDKey cprKey =
      new CaseIDAndParticipantRoleIDKey();
    cprKey.caseID = pdcID;
    cprKey.participantRoleID = pdcPersonDetails.concernRoleID;
    final CaseParticipantRoleIDStruct cprStruct =
      cprObj.readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(cprKey);
    final Long cprID = cprStruct.caseParticipantRoleID;

    // Create first evidence record
    createMaritalStatusEvidence(pdcID, cprID, pdcPersonDetails.concernRoleID,
      Date.fromISO8601("20200101"), Date.fromISO8601("20201231"));

    // Create another evidence record
    final Exception exception =
      Assert.assertThrows(InformationalException.class, () -> {
        createMaritalStatusEvidence(pdcID, cprID,
          pdcPersonDetails.concernRoleID, Date.fromISO8601("20210101"),
          Date.fromISO8601("20210530"));
      });

    // Assert valid exception message thrown
    assertTrue(exception.getMessage().contains(ERR_ONLY_ONE_RECORD));
  }

  /**
   * Instantiates and returns the evidence rule object.
   *
   * @return
   */
  private BDMMaritalStatus getEvidence() {

    return BDMMaritalStatus_Factory.getFactory().newInstance(this.session);

  }

  /**
   * Instatiates and returns a validator rule object for the given evidence.
   *
   * @param evidence
   * @return
   */
  private ValidationResult getValidator(final BDMMaritalStatus evidence) {

    final ValidationResult validator =
      ValidationResult_Factory.getFactory().newInstance(session);

    validator.evidence().specifyValue(evidence);

    return validator;

  }

  public CodeTableItem getMaritalStatus(final String code) {

    return new CodeTableItem(BDMMARITALSTATUS.TABLENAME, code);

  }

  private curam.core.sl.struct.EvidenceKey createMaritalStatusEvidence(
    final Long caseID, final Long cprID, final long concernroleID,
    final Date startDate, final Date endDate)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDM_MARITAL_STATUS;

    // get Latest Version of Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(CASE_PARTICIPANT_ROLE), cprID);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(START_DATE), startDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(END_DATE), endDate);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(MARITAL_STATUS),
      new CodeTableItem(BDMMARITALSTATUS.TABLENAME, MARITALSTATUS.MARRIED));

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = caseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(cprID);
    genericDtls.addRelCp(CASE_PARTICIPANT_ROLE, cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

}
