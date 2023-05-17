package curam.ca.gc.bdm.test.evidence;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMALERTCHOICE;
import curam.ca.gc.bdm.codetable.BDMTAXSITUATION;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.PersonRegistration;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.creole.execution.session.RecalculationsProhibited;
import curam.creole.execution.session.Session;
import curam.creole.execution.session.Session_Factory;
import curam.creole.execution.session.StronglyTypedRuleObjectFactory;
import curam.creole.storage.inmemory.InMemoryDataStorage;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Junit for Tax Credit Evidence Test
 *
 * @author afzal.patel
 *
 */
@Ignore
public class BDMTaxCreditEvidenceTest extends CuramServerTestJUnit4 {

  public BDMTaxCreditEvidenceTest() {

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

  @Inject
  protected EvidenceTypeVersionDefDAO etVerDefDAO;

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  private final String kcaseParticipant = "caseParticipant";

  private final String kNativeStatus = "nativeStatus";

  private final String kTaxCredit = "taxCredit";

  private final String kIncomeTaxFromPay = "incomeTaxFromPay";

  private final String kIncomeTaxOnlyPart = "incomeTaxOnlyPart";

  private final String kNumberOfDependant = "numberOfdependant";

  private static final String kFromDateAttrName = "fromDate";

  private final String ERR_TAX_CREDIT_NOT_VALID =
    "‘Employers Deduct Income Tax from Pay‘ is selected as ‘No‘, so ‘Employers Deduct Income Tax from Only Part of Pay‘ must be blank.";

  private final String ERR_NOT_VALID_INCOME_TAX_PAY =
    "‘Native Status‘ is selected as ‘No‘, so ‘Employers Deduct Income Tax from Pay‘ must be blank.";

  private final String ERR_OVERLAPPING_TAX_CREDIT =
    "Tax record already exists for the period. Please review the existing evidence record.";

  private final String ERR_NUM_DEPENDANTS_MUST_BE_BLANK =
    "Province of residence is outside of Saskatchewan, so 'Number of Dependants Under 18' must be left blank.";

  private final String ERR_NOT_VALID_DEPENDANTS_SK =
    "Please enter ‘Number of Dependants‘.";

  /**
   * Verify Tax Credit evidence is successfully created when valid details
   * are entered
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCreateTaxCreditEvidence()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    // create Tax Credit Evidence
    final EIEvidenceKey key =
      createTaxCreditEvidence(pdcPersonDetails, BDMTAXSITUATION.BASIC,
        BDMALERTCHOICE.YES, BDMALERTCHOICE.YES, BDMALERTCHOICE.YES, 0);

    assertTrue(key.evidenceID != 0L);

  }

  /**
   * Test class to validate Income Part Pay Must be blank
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testPartPayMustBeBlankValidation()
    throws AppException, InformationalException {

    boolean caught = false;

    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    try {

      createTaxCreditEvidence(pdcPersonDetails, BDMTAXSITUATION.BASIC,
        BDMALERTCHOICE.YES, BDMALERTCHOICE.NO, BDMALERTCHOICE.YES, 0);
    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_TAX_CREDIT_NOT_VALID, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test class to validate number of dependants must be blank
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testNumOfDependantsMustBeBlankValidation()
    throws AppException, InformationalException {

    boolean caught = false;

    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    try {

      createTaxCreditEvidence(pdcPersonDetails, BDMTAXSITUATION.BASIC,
        BDMALERTCHOICE.YES, BDMALERTCHOICE.NO, "", 1);
    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_NUM_DEPENDANTS_MUST_BE_BLANK, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test class to validate Income Tax Pay not valid when Native Status is No
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testNotValidIncomeTaxPay()
    throws AppException, InformationalException {

    boolean caught = false;

    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    try {

      createTaxCreditEvidence(pdcPersonDetails, BDMTAXSITUATION.BASIC,
        BDMALERTCHOICE.NO, BDMALERTCHOICE.NO, "", 0);
    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_NOT_VALID_INCOME_TAX_PAY, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test class to validate Income Tax Pay not valid when Native Status is No
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testNotValidDependants() throws IllegalStateException,
    SecurityException, HeuristicMixedException, HeuristicRollbackException,
    RollbackException, SystemException, Exception {

    boolean caught = false;

    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPersonFor_SK();

    try {

      createTaxCreditEvidence(pdcPersonDetails, BDMTAXSITUATION.BASIC,
        BDMALERTCHOICE.YES, BDMALERTCHOICE.YES, BDMALERTCHOICE.YES, 0);
    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_NOT_VALID_DEPENDANTS_SK, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Test class to validate overlapping tax credit evidence
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testOverlapTaxCreditEvidenceValidation()
    throws AppException, InformationalException {

    boolean caught = false;
    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    createTaxCreditEvidence(pdcPersonDetails, BDMTAXSITUATION.BASIC,
      BDMALERTCHOICE.YES, BDMALERTCHOICE.YES, BDMALERTCHOICE.YES, 0);

    try {

      createTaxCreditEvidence(pdcPersonDetails, BDMTAXSITUATION.BASIC,
        BDMALERTCHOICE.YES, BDMALERTCHOICE.YES, BDMALERTCHOICE.YES, 0);

    } catch (final InformationalException e) {

      caught = true;
      assertEquals(ERR_OVERLAPPING_TAX_CREDIT, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   *
   * @param appcaseKey
   * @param concernroleID
   * @param dollarAmount
   * @param percentage
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private EIEvidenceKey createTaxCreditEvidence(
    final PDCPersonDetails details, final String credit, final String status,
    final String incomeTaxFromPay, final String incomeTaxOnlyPart,
    final int numOfdependant) throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = details.concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDMTAX;

    final EvidenceTypeDef evidenceType =
      etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);

    final EvidenceTypeVersionDef evTypeVersion =
      etVerDefDAO.getActiveEvidenceTypeVersionAtDate(evidenceType,
        Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);

    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute(kcaseParticipant);

    DynamicEvidenceTypeConverter.setAttribute(participant,
      pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID);

    assignTaxCreditEvidenceDetails(dynamicEvidenceDataDetails, credit, status,
      incomeTaxFromPay, incomeTaxOnlyPart, numOfdependant);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Call the EvidenceController object and insert evidence
    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();

    evidenceDescriptorInsertDtls.participantID = details.concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.effectiveFrom = Date.getCurrentDate();

    evidenceDescriptorInsertDtls.caseID =
      pdcCaseIDCaseParticipantRoleID.caseID;

    // Evidence Interface details
    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();

    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = details.concernRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

    return evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);
  }

  private void assignTaxCreditEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String credit, final String status, final String incomeTaxFromPay,
    final String incomeTaxOnlyPart, final int numOfdependant)
    throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails taxCred =
      dynamicEvidenceDataDetails.getAttribute(kTaxCredit);
    DynamicEvidenceTypeConverter.setAttribute(taxCred,
      new CodeTableItem(BDMTAXSITUATION.TABLENAME, credit));

    final DynamicEvidenceDataAttributeDetails natStatus =
      dynamicEvidenceDataDetails.getAttribute(kNativeStatus);
    DynamicEvidenceTypeConverter.setAttribute(natStatus,
      new CodeTableItem(BDMALERTCHOICE.TABLENAME, status));

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute(kFromDateAttrName);
    DynamicEvidenceTypeConverter.setAttribute(fromDate,
      Date.getCurrentDate());

    final DynamicEvidenceDataAttributeDetails incomeTaxPay =
      dynamicEvidenceDataDetails.getAttribute(kIncomeTaxFromPay);
    DynamicEvidenceTypeConverter.setAttribute(incomeTaxPay,
      new CodeTableItem(BDMALERTCHOICE.TABLENAME, incomeTaxFromPay));

    final DynamicEvidenceDataAttributeDetails incomeTaxPartPay =
      dynamicEvidenceDataDetails.getAttribute(kIncomeTaxOnlyPart);
    DynamicEvidenceTypeConverter.setAttribute(incomeTaxPartPay,
      new CodeTableItem(BDMALERTCHOICE.TABLENAME, incomeTaxOnlyPart));

    final DynamicEvidenceDataAttributeDetails numberOfDependants =
      dynamicEvidenceDataDetails.getAttribute(kNumberOfDependant);
    DynamicEvidenceTypeConverter.setAttribute(numberOfDependants,
      numOfdependant);
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

  /**
   * Create a person that is from Saskatchewan to test dependants validation
   *
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public PDCPersonDetails createPDCPersonFor_SK()
    throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetailsForSK();

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
