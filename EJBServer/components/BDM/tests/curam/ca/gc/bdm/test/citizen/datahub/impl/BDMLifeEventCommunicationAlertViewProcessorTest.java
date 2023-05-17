package curam.ca.gc.bdm.test.citizen.datahub.impl;

import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventCommunicationAlertViewProcessor;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.citizen.datahub.impl.Citizen;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.core.impl.EnvVars;
import curam.datastore.impl.NoSuchSchemaException;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.PersonDAO;
import curam.piwrapper.participantmanager.impl.ConcernRoleAddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/*
 * The Class tests {@link BDMEvidenceUtil}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class BDMLifeEventCommunicationAlertViewProcessorTest
  extends CuramServerTestJUnit4 {

  @Mocked
  Citizen citizen;

  Element personEl;

  Element rootElement;

  Document citizenData;

  BDMLifeEventTestUtil bdmLifeEventTestUtil;

  @Mocked
  PersonDAO personDAO;

  @Mocked
  ConcernRoleAddressDAO concernRoleAddressDAO;

  @Mocked
  ConcernRoleDAO concernRoleDAO;

  @Mocked
  BDMPhoneEvidence bdmPhoneEvidence;

  @Mocked
  BDMEmailEvidence bdmEmailEvidence;

  @Mocked
  BDMContactPreferenceEvidence bdmContactPreferenceEvidence;

  /** The validate function. */
  @Tested
  BDMLifeEventCommunicationAlertViewProcessor bdmLifeEventCommunicationAlertViewProcessor;

  /** A valid citizen identifier. */
  private static final long VALID_IDENTIFIER = 200l;

  /** A valid citizen name. */
  private static final String VALID_NAME = "JOE SMITH";

  public BDMLifeEventCommunicationAlertViewProcessorTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws AppException, InformationalException {

    // Set concern information
    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "NO");
    bdmLifeEventTestUtil = new BDMLifeEventTestUtil();
    bdmLifeEventTestUtil.createConcernRole(VALID_IDENTIFIER, VALID_NAME);

    // set citizen element information
    citizenData = new Document();
    rootElement = new Element(BDMLifeEventDatastoreConstants.APPLICATION);
    personEl = new Element(BDMLifeEventDatastoreConstants.PERSON);
    rootElement.addContent(personEl);
    citizenData.setRootElement(rootElement);
  }

  @After
  public void after() {

    Configuration.setProperty(EnvVars.ENV_PDC_ENABLED, "YES");
  }

  /**
   * Set Email, Phone, Communication and Alert into datastore element
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private Element setUpCommunicationAndAlertElement()
    throws AppException, InformationalException {

    // final Element expectedPersonElement =
    // new Element(BDMLifeEventDatastoreConstants.PERSON);

    Element communicationDetails = null;
    communicationDetails = new Element("CommunicationDetails");

    // Set last name at birth, parents last name at birth, DOB and
    // evidenceID into datastore
    communicationDetails.setAttribute(
      BDMLifeEventDatastoreConstants.EMAIL_ADDRESS, "abc@abc.com");
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.EMAIL_TYPE, "WORK");
    communicationDetails.setAttribute(
      BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION, "true");
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL, "true");
    communicationDetails.setAttribute(
      BDMLifeEventDatastoreConstants.EMAIL_EVIDENCE_ID, "10001");

    personEl.addContent(communicationDetails);

    // Set phone and evidenceID final from dyn evidence

    communicationDetails = new Element("CommunicationDetails");

    // Set phone and evidenceID into datastore
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.COUNTRY_CODE, "1");
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.PHONE_AREA_CODE, "123");
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.PHONE_NUMBER, "456789");
    communicationDetails.setAttribute(
      BDMLifeEventDatastoreConstants.FULL_PHONE_NUMBER, "123456789");
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.PHONE_EXT, "001");
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.PHONE_TYPE, "WORK");
    communicationDetails.setAttribute(
      BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION, "true");
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.PHONE_ALT_PREF, "true");
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.PHONE_IS_MOR, "true");
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.PHONE_IS_AFTNOON, "false");
    communicationDetails
      .setAttribute(BDMLifeEventDatastoreConstants.PHONE_IS_EVE, "false");
    communicationDetails.setAttribute(
      BDMLifeEventDatastoreConstants.PHONE_EVIDENCE_ID, "10002");
    // add phone to datastore
    personEl.addContent(communicationDetails);

    // Set CommunicationPref and evidenceID final from dyn evidence

    Element communicationPref = null;
    communicationPref = new Element("CommunicationPref");

    // Set CommunicationPref into datastore
    communicationPref.setAttribute(
      BDMLifeEventDatastoreConstants.RECEIVE_CORRESPONDENCE, "DIGITAL");
    communicationPref.setAttribute(
      BDMLifeEventDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE, "EN");
    communicationPref.setAttribute(
      BDMLifeEventDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE, "EN");
    communicationPref.setAttribute(
      BDMLifeEventDatastoreConstants.COMM_PREF_EVIDENCE_ID, "10003");

    // Set AlertPreferences from dyn evidence AlertPreferences entity
    Element alertPreferences = null;
    alertPreferences = new Element("AlertPreferences");

    // Set AlertPreferences into datastore
    alertPreferences
      .setAttribute(BDMLifeEventDatastoreConstants.RECEIVE_ALERT, "YES");
    alertPreferences.setAttribute(BDMLifeEventDatastoreConstants.ALERT_OCCUR,
      "DAILY");
    alertPreferences.setAttribute(
      BDMLifeEventDatastoreConstants.ALERT_PREF_EVIDENCE_ID, "10004");

    personEl.addContent(communicationPref);
    personEl.addContent(alertPreferences);

    return personEl;
  }

  @Test
  public void test_valid_communication_and_alerts_attributes()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Private Method returned Element

    try {
      final Element actualPersonElement =
        Deencapsulation.invoke(bdmLifeEventCommunicationAlertViewProcessor,
          "setPersonEntityAttributes",
          new Object[]{personEl, citizenData, citizen });

      final Element expectedPersonElement =
        setUpCommunicationAndAlertElement();

      assertEquals(expectedPersonElement, actualPersonElement);
    } catch (final Exception e) {
      assertFalse(false);
    }
  }

}
