package curam.ca.gc.bdm.test.evidence;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMALERTCHOICE;
import curam.ca.gc.bdm.codetable.BDMCORRESDELIVERY;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.sl.fact.BDMEvidenceControllerHookFactory;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.LANGUAGE;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.intf.PersonRegistration;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.CaseKey;
import curam.core.struct.ConcernRoleDtls;
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
import curam.pdc.impl.PDCContactPreferencesEvidencePopulator;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * Test case class for the BDMContactPreferenceEvidence class.
 *
 */
public class BDMPDCContactPreferenceEvidenceTest
  extends CuramServerTestJUnit4 {

  public BDMPDCContactPreferenceEvidenceTest() {

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

  private static final String kParticipant = "participant";

  private static final String kpreferedOralLang = "preferredOralLanguage";

  private static final String kpreferredWrittenLanguage =
    "preferredWrittenLanguage";

  private static final String ERR_DELETE_CONTACT_PREF =
    "A Contact Preferences record must exist. This record cannot be deleted.";

  @Inject
  private Set<PDCContactPreferencesEvidencePopulator> pdcContactPreferencesEvidencePopulators;

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  /**
   * create contact preference evidence
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCreatePDCContactPreferenceEvidence()
    throws AppException, InformationalException {

    // createPDCPerson();
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    final EIEvidenceKey key = createContactPreferenceEvidence(
      pdcPersonDetails, BDMALERTCHOICE.YES, "A02");

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls currEIEvidenceReadDtls =
      evidenceControllerObj.readEvidence(key);

    final DynamicEvidenceDataDetails currDynamicEvidenceDataDetails =
      (DynamicEvidenceDataDetails) currEIEvidenceReadDtls.evidenceObject;

    // Oral Lang
    final DynamicEvidenceDataAttributeDetails preferedLang =
      currDynamicEvidenceDataDetails.getAttribute(kpreferedOralLang);
    assertEquals("BDMLANG800", preferedLang.getValue());

    // Written Lang
    final DynamicEvidenceDataAttributeDetails preferedwritten =
      currDynamicEvidenceDataDetails.getAttribute(kpreferredWrittenLanguage);
    assertEquals("BDMLANG800", preferedwritten.getValue());

    assertEquals("BDMLANG800", preferedwritten.getValue());

  }

  /**
   * Test delete contact pref.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testCreatePDCContactPreferenceEvidence_remove()
    throws AppException, InformationalException {

    // createPDCPerson();
    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    boolean caught = false;
    try {

      final CaseKey caseKey = new CaseKey();
      caseKey.caseID = pdcPersonDetails.caseID;

      final EIEvidenceKey evidenceKey = new EIEvidenceKey();
      evidenceKey.evidenceType = PDCConst.PDCCONTACTPREFERENCES;

      evidenceKey.evidenceID =
        createContactPreferenceEvidence(pdcPersonDetails, BDMALERTCHOICE.YES,
          "AO2").evidenceID;

      BDMEvidenceControllerHookFactory.newInstance()
        .preRemoveEvidence(caseKey, evidenceKey);

    } catch (final InformationalException e) {
      caught = true;
      assertEquals(ERR_DELETE_CONTACT_PREF, e.getMessage());
    }

    assertTrue(caught);

  }

  /**
   * Creates a Person
   *
   * @return person details.
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

  // ___________________________________________________________________________
  /**
   * Inserts the Participant Contact Pref Address evidence .
   *
   * @param details The person details.
   * @param receiveAlert
   * @param alertFrequency
   * @return The evidence key.
   *
   */
  public EIEvidenceKey createContactPreferenceEvidence(
    final PDCPersonDetails details, final String receiveAlert,
    final String alertFrequency) throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = details.concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCCONTACTPREFERENCES;

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

    assignContactPreferencesEvidenceDetails(pdcCaseIDCaseParticipantRoleID,
      details, dynamicEvidenceDataDetails, receiveAlert, alertFrequency);

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
   * Assigns participant's contact preferences to the dynamic evidence data
   * struct.
   *
   * @param pdcCaseIDCaseParticipantRoleID
   * @param details
   * @param dynamicEvidenceDataDetails
   * @param alert
   * @param frequency
   * @throws AppException
   * @throws InformationalException
   */
  private void assignContactPreferencesEvidenceDetails(
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID,
    final PDCPersonDetails details,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String alert, final String frequency)
    throws AppException, InformationalException {

    final String commMethod = !details.prefCommMethod.isEmpty()
      ? details.prefCommMethod : BDMCORRESDELIVERY.POSTALDIG;

    final DynamicEvidenceDataAttributeDetails preferedLanguage =
      dynamicEvidenceDataDetails.getAttribute("preferredLanguage");
    DynamicEvidenceTypeConverter.setAttribute(preferedLanguage,
      new CodeTableItem(LANGUAGE.TABLENAME, details.preferredLanguage));
    final DynamicEvidenceDataAttributeDetails preferedCommunication =
      dynamicEvidenceDataDetails.getAttribute("preferredCommunicationMethod");
    DynamicEvidenceTypeConverter.setAttribute(preferedCommunication,
      new CodeTableItem(BDMCORRESDELIVERY.TABLENAME, commMethod));

    // ADA-9375 Start

    final String preferredWrittenLang = "BDMLANG800";

    final String preferredOralLang = "BDMLANG800";

    // prefered oral lang
    final DynamicEvidenceDataAttributeDetails preferredOralLanguage =
      dynamicEvidenceDataDetails.getAttribute("preferredOralLanguage");
    DynamicEvidenceTypeConverter.setAttribute(preferredOralLanguage,
      new CodeTableItem(BDMLANGUAGE.TABLENAME, preferredOralLang));

    // prefered written lang
    final DynamicEvidenceDataAttributeDetails preferredWrittenLanguage =
      dynamicEvidenceDataDetails.getAttribute("preferredWrittenLanguage");
    DynamicEvidenceTypeConverter.setAttribute(preferredWrittenLanguage,
      new CodeTableItem(BDMLANGUAGE.TABLENAME, preferredWrittenLang));

    // ADA-9375End

    final DynamicEvidenceDataAttributeDetails comments =
      dynamicEvidenceDataDetails.getAttribute("comments");
    DynamicEvidenceTypeConverter.setAttribute(comments, details.comments);
    if (!this.pdcContactPreferencesEvidencePopulators.isEmpty()) {
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
      concernRoleKey.concernRoleID = details.concernRoleID;
      final CaseIDKey caseIDKey = new CaseIDKey();
      caseIDKey.caseID = pdcCaseIDCaseParticipantRoleID.caseID;
      final ConcernRoleDtls concernRoleDtls = new ConcernRoleDtls();
      concernRoleDtls.assign(details);
      for (final PDCContactPreferencesEvidencePopulator pdcContactPreferencesEvidencePopulator : this.pdcContactPreferencesEvidencePopulators)
        pdcContactPreferencesEvidencePopulator.populate(concernRoleKey,
          caseIDKey, concernRoleDtls, dynamicEvidenceDataDetails);
    }
  }

}
