package curam.ca.gc.bdm.test.evidence;

import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.codetable.CASEEVIDENCE;
import curam.commonintake.codetable.METHODOFAPPLICATION;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.commonintake.facade.fact.ApplicationCaseFactory;
import curam.commonintake.facade.intf.ApplicationCase;
import curam.commonintake.facade.struct.ApplicationCaseCreateDetails;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.PersonRegistration;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.execution.session.StronglyTypedRuleObjectFactory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.message.ENTVERIFICATIONCONTROLLER;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.Money;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Junit for Income Test
 *
 * @author teja.konda
 *
 */
@Ignore
public class BDMIncomeEvidenceTest extends CuramServerTestJUnit4 {

  public BDMIncomeEvidenceTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  Session session;

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
  }

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  private final String kcaseParticipant = "caseParticipant";

  private final String kAttesteeCaseParticipant = "attesteeCaseParticipant";

  private final String kBDMAgreeAttestation = "bdmAgreeAttestation";

  private final String kAttestationDate = "attestationDate";

  private final String kIncome = "income";

  private final String kyear = "year";

  private final BDMUtil bdmUtil = new BDMUtil();

  private final String INVALID_TAX_YEAR =
    "Income record already exists for the tax year";

  /**
   * Verify Email Address evidence is successfully created when valid details
   * are entered
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCreateIncomeEvidence()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    final ApplicationCaseKey appCaseKey =
      this.createApplicationCase(pdcPersonDetails.concernRoleID);

    // create Income Evidence
    final EvidenceKey key = createIncomeEvidence(appCaseKey,
      pdcPersonDetails.concernRoleID, "1000", "2020");

    assertTrue(key.evidenceID != 0L);

  }

  /**
   * Test class to validate tax year validation
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testIncomeTaxYearValidation()
    throws AppException, InformationalException {

    boolean caught = false;
    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    final ApplicationCaseKey appCaseKey =
      this.createApplicationCase(pdcPersonDetails.concernRoleID);

    // create Income Evidence
    createIncomeEvidence(appCaseKey, pdcPersonDetails.concernRoleID, "1000",
      "2020");

    final AppException verException = new AppException(
      ENTVERIFICATIONCONTROLLER.ERR_VERIFICATIONCONTROLLER_INSERT_VERIFICATION_VERIFIABLE_DATA_ITEM_INFORMATIONAL);
    TransactionInfo.getInformationalManager().acceptWarning("", verException);
    try {

      createIncomeEvidence(appCaseKey, pdcPersonDetails.concernRoleID, "1000",
        "2020");
      TransactionInfo.getInformationalManager().acceptWarning("",
        verException);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(INVALID_TAX_YEAR + " 2020", e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Util method to create Application Case
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ApplicationCaseKey createApplicationCase(final long concernRoleID)
    throws AppException, InformationalException {

    // create Application case
    final ApplicationCase appCase = ApplicationCaseFactory.newInstance();

    final ApplicationCaseCreateDetails appCaseDetails =
      new ApplicationCaseCreateDetails();
    appCaseDetails.concernRoleID = concernRoleID;
    appCaseDetails.dtls.applicationCaseAdminID = 80001l;
    appCaseDetails.dtls.applicationDate = TransactionInfo.getSystemDate();
    appCaseDetails.dtls.methodOfApplication = METHODOFAPPLICATION.INPERSON;

    final curam.commonintake.entity.struct.ApplicationCaseKey appCaseKey =
      appCase.createApplicationCaseForConcernRole(appCaseDetails);

    return appCaseKey;
  }

  /**
   *
   * @param appcaseKey
   * @param concernroleID
   * @param income
   * @param incomeYear
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public curam.core.sl.struct.EvidenceKey createIncomeEvidence(
    final ApplicationCaseKey appcaseKey, final long concernroleID,
    final String income, final String incomeYear)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDMINCOME;

    // get caseParticipantROleID of the person
    final long caseParticipantRoleid =
      bdmUtil.getCaseParticipantRoleID(appcaseKey.applicationCaseID,
        concernroleID).caseParticipantRoleID;

    // get Latest Version of income Evidecne
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kcaseParticipant),
      caseParticipantRoleid);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kIncome),
      Money.doConversion(income));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kyear), incomeYear);

    // BEGIN - User Story 21834 - Added Attestation for Income evidence
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kAttesteeCaseParticipant),
      caseParticipantRoleid);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kBDMAgreeAttestation),
      new CodeTableItem(BDMAGREEATTESTATION.TABLENAME,
        BDMAGREEATTESTATION.YES));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kAttestationDate),
      Date.getCurrentDate());
    // END - User Story 21834 - Added Attestation for Income evidence
    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = appcaseKey.applicationCaseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(caseParticipantRoleid);
    genericDtls.addRelCp("caseParticipant", cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

  /**
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();

    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

}
