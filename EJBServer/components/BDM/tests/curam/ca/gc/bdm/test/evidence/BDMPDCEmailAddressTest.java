package curam.ca.gc.bdm.test.evidence;

import com.google.inject.Inject;
import curam.bdm.test.junit4.CuramServerTestJUnit4;
import curam.ca.gc.bdm.codetable.BDMALERTOCCUR;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.codetable.EMAILTYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.PersonRegistration;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
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
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BDMPDCEmailAddressTest extends CuramServerTestJUnit4 {

  public BDMPDCEmailAddressTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  Session session;

  private final String ERR_ONE_EMAIL_ADDRESS_FOR_ALERTS =
    "Only one email address, either a business or a personal email address can be selected to receive alerts.";

  @Override
  public void setUpCuramServerTest() {

    super.setUpCuramServerTest();

    session =
      Session_Factory.getFactory().newInstance(new RecalculationsProhibited(),
        new InMemoryDataStorage(new StronglyTypedRuleObjectFactory()));
  }

  @Inject
  BDMPDCContactPreferenceEvidenceTest contactObj;

  @Inject
  protected EvidenceTypeVersionDefDAO etVerDefDAO;

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  private static final String kFromDateAttrName = "fromDate";

  private static final String kParticipant = "participant";

  private static final String kEmailAddressType = "emailAddressType";

  private static final String kEmailAddress = "emailAddress";

  private static final String ERR_EMAILADDRESS_INVALID =
    "Email address must be valid.";

  private static final String kPreferred = "preferredInd";

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  /**
   * Verify Email Address evidence is successfully created when valid details
   * are entered
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCreateEmailAddresssEvidence()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    final EIEvidenceKey key = createEmailAddressEvidence(pdcPersonDetails,
      "test1@test.test.com", EMAILTYPE.PERSONAL, false, false);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls currEIEvidenceReadDtls =
      evidenceControllerObj.readEvidence(key);

    final DynamicEvidenceDataDetails currDynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) currEIEvidenceReadDtls.evidenceObject;

    final DynamicEvidenceDataAttributeDetails email =
      currDynamicEvidenceDataDetails.getAttribute(kEmailAddress);
    assertEquals("test1@test.test.com", email.getValue());

  }

  /**
   * Verify Email Address evidence is successfully created when valid details
   * are entered and email is marked preffered
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCreateEmailAddresssEvidence2_prefered()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    // set preferred=true
    final EIEvidenceKey key = createEmailAddressEvidence(pdcPersonDetails,
      "test1@test.test.com", EMAILTYPE.PERSONAL, true, false);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls currEIEvidenceReadDtls =
      evidenceControllerObj.readEvidence(key);

    final DynamicEvidenceDataDetails currDynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) currEIEvidenceReadDtls.evidenceObject;

    final DynamicEvidenceDataAttributeDetails email =
      currDynamicEvidenceDataDetails.getAttribute(kEmailAddress);
    assertEquals("test1@test.test.com", email.getValue());

    final DynamicEvidenceDataAttributeDetails emailPreffered =
      currDynamicEvidenceDataDetails.getAttribute(kPreferred);
    assertEquals("true", emailPreffered.getValue());

  }

  /**
   * Verify Email Address evidence is not created when specified email address
   * does not match email address format
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCreateEmailAddresssEvidence_InvalidEmail()
    throws AppException, InformationalException {

    // Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    boolean caught = false;
    try {

      createEmailAddressEvidence(pdcPersonDetails, "invalid@.com",
        EMAILTYPE.BUSINESS, false, false);

    } catch (final InformationalException e) {
      caught = true;
      assertEquals(ERR_EMAILADDRESS_INVALID, e.getMessage());
    }

    assertTrue(caught);

  }

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

  // ___________________________________________________________________________
  /**
   * Inserts the Participant Email Address evidence .
   *
   * @param details The person details.
   * @param emailAddress
   * @param preferrredInd
   * @param paramAlerts
   *
   * @return The evidence key.
   * Participant Email Address evidence details to be inserted.
   */

  public EIEvidenceKey createEmailAddressEvidence(
    final PDCPersonDetails details, final String emailAddress,
    final String paramEmailType, final boolean paramPreferred,
    final boolean paramAlerts) throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = details.concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCEMAILADDRESS;

    final EvidenceTypeDef evidenceType =
      etDefDAO.readActiveEvidenceTypeDefByTypeCode(eType.evidenceType);

    final EvidenceTypeVersionDef evTypeVersion =
      etVerDefDAO.getActiveEvidenceTypeVersionAtDate(evidenceType,
        Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evTypeVersion);

    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute(kParticipant);

    DynamicEvidenceTypeConverter.setAttribute(participant,
      pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID);

    final DynamicEvidenceDataAttributeDetails preferred =
      dynamicEvidenceDataDetails.getAttribute(kPreferred);
    DynamicEvidenceTypeConverter.setAttribute(preferred, paramPreferred);

    final DynamicEvidenceDataAttributeDetails useForAlerts =
      dynamicEvidenceDataDetails
        .getAttribute(BDMConstants.kEvidenceAttrUseForAlert);
    DynamicEvidenceTypeConverter.setAttribute(useForAlerts, paramAlerts);

    assignEmailAddressEvidenceDetails(dynamicEvidenceDataDetails,
      emailAddress, paramEmailType);

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

  /**
   * Assigns participant's email to the dynamic evidence data
   * struct.
   *
   * @param details
   * Participant email
   * @PARAM paramEmailType
   * @param dynamicEvidenceDataDetails
   * Dynamic evidence details.
   */
  private void assignEmailAddressEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String emailAddress, final String paramEmailType)
    throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails emailType =
      dynamicEvidenceDataDetails.getAttribute(kEmailAddressType);
    DynamicEvidenceTypeConverter.setAttribute(emailType,
      new CodeTableItem(EMAILTYPE.TABLENAME, paramEmailType));

    final DynamicEvidenceDataAttributeDetails email =
      dynamicEvidenceDataDetails.getAttribute(kEmailAddress);
    DynamicEvidenceTypeConverter.setAttribute(email, emailAddress);

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute(kFromDateAttrName);
    DynamicEvidenceTypeConverter.setAttribute(fromDate,
      Date.getCurrentDate());
    // -- Added AJERTS FREQUENCY --->
    final DynamicEvidenceDataAttributeDetails alertFrequency1 =
      dynamicEvidenceDataDetails.getAttribute("alertFrequency");
    DynamicEvidenceTypeConverter.setAttribute(alertFrequency1,
      new CodeTableItem(BDMALERTOCCUR.TABLENAME, BDMALERTOCCUR.PERDAY));
    // -- Added AJERTS FREQUENCY --->
  }

  /**
   * Verify that two email address cannot be set to recieve alerts .Second email
   * evidence creation should throw ERR_ONE_EMAIL_ADDRESS_FOR_ALERTS
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testuseForAlertsEmailAddressExists3()
    throws AppException, InformationalException {

    /// Create Person record
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    createEmailAddressEvidence(pdcPersonDetails, "test1@test.test.com",
      EMAILTYPE.BUSINESS, false, true);

    boolean caught = false;

    try {

      createEmailAddressEvidence(pdcPersonDetails, "test1@test.test.com",
        EMAILTYPE.PERSONAL, false, true);

    } catch (final Exception e) {
      caught = true;
      assertEquals(ERR_ONE_EMAIL_ADDRESS_FOR_ALERTS, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Verify that two email address cannot be set to recieve alerts .Second email
   * evidence creation should throw ERR_ONE_EMAIL_ADDRESS_FOR_ALERTS
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testuseForAlertsEmailAddressExists4()
    throws AppException, InformationalException {

    /// Create Person
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    // Create email address evidence and set alerts= false
    EIEvidenceKey key = createEmailAddressEvidence(pdcPersonDetails,
      "test1@test.test.com", EMAILTYPE.PERSONAL, false, false);

    // Create another email address alerts= true
    key = createEmailAddressEvidence(pdcPersonDetails, "test2@test.test.com",
      EMAILTYPE.PERSONAL, false, true);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls currEIEvidenceReadDtls =
      evidenceControllerObj.readEvidence(key);

    final DynamicEvidenceDataDetails currDynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) currEIEvidenceReadDtls.evidenceObject;

    final DynamicEvidenceDataAttributeDetails alerts =
      currDynamicEvidenceDataDetails
        .getAttribute(BDMConstants.kEvidenceAttrUseForAlert);

    assertTrue("Second Email address successfully set to alerts ",
      new Boolean(alerts.getValue()));
  }

  /***
   * Uncomment idf conatct preference validation is implemented
   *
   * @Test
   * public void testCreatePDCContactPreferenceEvidence()
   * throws AppException, InformationalException {
   *
   * //Create Person record
   * final PDCPersonDetails pdcPersonDetails = createPDCPerson();
   *
   * final EIEvidenceKey key = contactObj
   * .createContactPreferenceEvidence(pdcPersonDetails, "YN1", "A02");
   *
   * boolean caught = false;
   * try {
   * createEmailAddressEvidence(pdcPersonDetails, "test@test.ca",
   * EMAILTYPE.BUSINESS, false, false);
   *
   *
   * } catch (final Exception e) {
   * caught = true;
   *
   * assertEquals(ERR_SELECT_CONTACT_PREFERENCE, e.getMessage());
   * }
   *
   * assertTrue(caught);
   *
   * }
   **/
}
