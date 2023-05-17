package curam.ca.gc.bdm.test.citizen.datahub.impl;

import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventPhoneNumberViewProcessor;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.citizen.datahub.impl.Citizen;
import curam.codetable.COUNTRYCODE;
import curam.codetable.IEGYESNO;
import curam.codetable.PHONETYPE;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.core.impl.EnvVars;
import curam.datastore.impl.NoSuchSchemaException;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import java.util.ArrayList;
import java.util.List;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/*
 * The Class tests {@link BDMEvidenceUtil}
 * custom
 * function.
 */
@RunWith(JMockit.class)
public class BDMLifeEventPhoneNumberViewProcessorTest
  extends CustomFunctionTestJunit4 {

  /** A valid Phone Number. */
  private static final String VALID_PHONE = "1234567";

  /** A valid citizen identifier. */
  private static final long VALID_IDENTIFIER = 200l;

  /** A valid citizen name. */
  private static final String VALID_NAME = "JOE SMITH";

  @Mocked
  Citizen citizen;

  Element personEl;

  Element rootElement;

  Document citizenData;

  BDMLifeEventTestUtil bdmLifeEventTestUtil;

  @Mocked
  BDMPhoneEvidence bdmPhoneEvidence;

  /** The validate function. */
  @Tested
  BDMLifeEventPhoneNumberViewProcessor bdmLifeEventPhoneNumberViewProcessor;

  public BDMLifeEventPhoneNumberViewProcessorTest() {

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
    bdmLifeEventTestUtil.createConcernRole(VALID_IDENTIFIER, "JOE SMITH");
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
   * Set phone and evidenceID into datastore element
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private Element setUpCommunicationDetailsElement(final String countryCode,
    final String phoneAreaCode, final String phoneNumber,
    final String phoneType, final String phoneExtension,
    final Boolean isPreferredCommunication, final Boolean alertPrefPhone,
    final Long phoneEvidenceID) throws AppException, InformationalException {

    final Element expectedCommunicationDetailsElement =
      new Element(BDMLifeEventDatastoreConstants.COMMUNICATION_DETAILS);
    expectedCommunicationDetailsElement
      .setAttribute(BDMLifeEventDatastoreConstants.COUNTRY_CODE, countryCode);
    expectedCommunicationDetailsElement.setAttribute(
      BDMLifeEventDatastoreConstants.PHONE_AREA_CODE, phoneAreaCode);
    expectedCommunicationDetailsElement
      .setAttribute(BDMLifeEventDatastoreConstants.PHONE_NUMBER, phoneNumber);
    expectedCommunicationDetailsElement
      .setAttribute(BDMLifeEventDatastoreConstants.PHONE_TYPE, phoneType);
    if (isPreferredCommunication) {
      expectedCommunicationDetailsElement.setAttribute(
        BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION,
        IEGYESNO.YES);
    }
    if (alertPrefPhone) {
      expectedCommunicationDetailsElement.setAttribute(
        BDMLifeEventDatastoreConstants.RECEIVE_ALERT, IEGYESNO.YES);
      expectedCommunicationDetailsElement.setAttribute(
        BDMLifeEventDatastoreConstants.PHONE_ALT_PREF, IEGYESNO.YES);
    }
    expectedCommunicationDetailsElement.setAttribute(
      BDMLifeEventDatastoreConstants.PHONE_EVIDENCE_ID,
      phoneEvidenceID.toString());
    return expectedCommunicationDetailsElement;
  }

  /**
   * Expectations
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private void expectationPhoneVOAttributes(final String countryCode,
    final String phoneAreaCode, final String phoneNumber,
    final String phoneType, final String phoneExtension,
    final Boolean preferredInd, final Boolean useForAlertsInd,
    final Long phoneEvidenceID)
    throws AppException, InformationalException, NoSuchSchemaException {

    // Set up Email Value Object Expectations
    final BDMPhoneEvidenceVO bdmPhoneEvidenceVO = new BDMPhoneEvidenceVO();
    bdmPhoneEvidenceVO.setPhoneCountryCode(countryCode);
    bdmPhoneEvidenceVO.setPhoneAreaCode(phoneAreaCode);
    bdmPhoneEvidenceVO.setPhoneNumber(phoneNumber);
    bdmPhoneEvidenceVO.setPhoneExtension(phoneExtension);
    bdmPhoneEvidenceVO.setPhoneType(phoneType);
    bdmPhoneEvidenceVO.setEvidenceID(phoneEvidenceID);
    bdmPhoneEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmPhoneEvidenceVO.setPreferredInd(preferredInd);
    bdmPhoneEvidenceVO.setUseForAlertsInd(useForAlertsInd);
    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceVOList =
      new ArrayList<BDMPhoneEvidenceVO>();
    bdmPhoneEvidenceVOList.add(bdmPhoneEvidenceVO);
    new Expectations() {

      {
        citizen.getIdentifier();
        result = VALID_IDENTIFIER;
        bdmPhoneEvidence.getEvidenceValueObject(anyLong);
        result = bdmPhoneEvidenceVOList;
      }
    };
  }

  /**
   * Verifications
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private void verificationPhoneElementAttributes(final Element expected,
    final Element result)
    throws AppException, InformationalException, NoSuchSchemaException {

    assertEquals(
      expected.getAttributeValue(BDMLifeEventDatastoreConstants.COUNTRY_CODE),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.COUNTRY_CODE));
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.PHONE_AREA_CODE),
      result
        .getAttributeValue(BDMLifeEventDatastoreConstants.PHONE_AREA_CODE));
    assertEquals(
      expected.getAttributeValue(BDMLifeEventDatastoreConstants.PHONE_NUMBER),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.PHONE_NUMBER));
    assertEquals(
      expected.getAttributeValue(BDMLifeEventDatastoreConstants.PHONE_TYPE),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.PHONE_TYPE));
    assertEquals(
      expected.getAttributeValue(
        BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION),
      result.getAttributeValue(
        BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION));
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.RECEIVE_ALERT),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.RECEIVE_ALERT));
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.PHONE_EVIDENCE_ID),
      result
        .getAttributeValue(BDMLifeEventDatastoreConstants.PHONE_EVIDENCE_ID));
  }

  /**
   * Test setPersonEntityAttributes returns an element with all the expected
   * attributes positive result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_setPersonEntityAttributes()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String countryCode = COUNTRYCODE.CACODE;
    final String phoneAreaCode = "919";
    final String phoneNumber = VALID_PHONE;
    final String phoneExtension = "";
    final String phoneType = PHONETYPE.PERSONAL;
    final Boolean isPreferredCommunication = true;
    final Boolean alertPrefPhone = true;
    final Long phoneEvidenceID = 0l;
    final String resultMessage = "valid";
    // Expectations
    expectationPhoneVOAttributes(countryCode, phoneAreaCode, phoneNumber,
      phoneType, phoneExtension, isPreferredCommunication, alertPrefPhone,
      phoneEvidenceID);
    final Element expectedCommunicationDetailsElement =
      setUpCommunicationDetailsElement(countryCode, phoneAreaCode,
        phoneNumber, phoneType, phoneExtension, isPreferredCommunication,
        alertPrefPhone, phoneEvidenceID);
    // Test Private Method returned Element
    final Element resultElement =
      Deencapsulation.invoke(bdmLifeEventPhoneNumberViewProcessor,
        "setPersoncommunicationDetailsEntityAttributes",
        new Object[]{personEl, citizenData, citizen });
    final Element resultCommunicationDetailsElement = resultElement
      .getChild(BDMLifeEventDatastoreConstants.COMMUNICATION_DETAILS);
    // Verifications
    verificationPhoneElementAttributes(expectedCommunicationDetailsElement,
      resultCommunicationDetailsElement);
  }
}
