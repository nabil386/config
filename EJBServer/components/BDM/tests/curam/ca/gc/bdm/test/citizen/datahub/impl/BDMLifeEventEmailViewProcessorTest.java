package curam.ca.gc.bdm.test.citizen.datahub.impl;

import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventEmailViewProcessor;
import curam.ca.gc.bdm.codetable.BDMALERTOCCUR;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidenceVO;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.citizen.datahub.impl.Citizen;
import curam.codetable.EMAILTYPE;
import curam.codetable.IEGYESNO;
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
public class BDMLifeEventEmailViewProcessorTest
  extends CustomFunctionTestJunit4 {

  /** A valid email address. */
  private static final String VALID_EMAIL = "joe_smith@email.com";

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
  BDMEmailEvidence bdmEmailEvidence;

  /** The validate function. */
  @Tested
  BDMLifeEventEmailViewProcessor bdmLifeEventEmailViewProcessor;

  public BDMLifeEventEmailViewProcessorTest() {

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
   * Set email and evidenceID into datastore element
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private Element setUpCommunicationDetailsElement(final String emailAdr,
    final String emailType, final Boolean isPreferredCommunication,
    final Boolean alertPrefEmail, final Long emailEvidenceID)
    throws AppException, InformationalException {

    final Element expectedCommunicationDetailsElement =
      new Element(BDMLifeEventDatastoreConstants.COMMUNICATION_DETAILS);

    expectedCommunicationDetailsElement
      .setAttribute(BDMLifeEventDatastoreConstants.EMAIL_ADDRESS, emailAdr);
    expectedCommunicationDetailsElement
      .setAttribute(BDMLifeEventDatastoreConstants.EMAIL_TYPE, emailType);
    if (isPreferredCommunication) {
      expectedCommunicationDetailsElement.setAttribute(
        BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION,
        IEGYESNO.YES);
    }
    if (alertPrefEmail) {
      expectedCommunicationDetailsElement.setAttribute(
        BDMLifeEventDatastoreConstants.RECEIVE_ALERT, IEGYESNO.YES);
      expectedCommunicationDetailsElement.setAttribute(
        BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL, IEGYESNO.YES);
      expectedCommunicationDetailsElement.setAttribute(
        BDMLifeEventDatastoreConstants.ALERT_FREQUENCY,
        BDMALERTOCCUR.PERINFO);
    }
    expectedCommunicationDetailsElement.setAttribute(
      BDMLifeEventDatastoreConstants.EMAIL_EVIDENCE_ID,
      emailEvidenceID.toString());

    return expectedCommunicationDetailsElement;
  }

  /**
   * Expectations
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private void expectationEmailVOAttributes(final String email,
    final String emailType, final Boolean preferredInd,
    final Boolean useForAlertsInd, final Long emailEvidenceID)
    throws AppException, InformationalException, NoSuchSchemaException {

    // Set up Email Value Object Expectations
    final BDMEmailEvidenceVO bdmEmailEvidenceVO = new BDMEmailEvidenceVO();
    bdmEmailEvidenceVO.setEmailAddress(email);
    bdmEmailEvidenceVO.setEmailAddressType(emailType);
    bdmEmailEvidenceVO.setEvidenceID(emailEvidenceID);
    bdmEmailEvidenceVO.setFromDate(Date.getCurrentDate());
    bdmEmailEvidenceVO.setPreferredInd(preferredInd);
    bdmEmailEvidenceVO.setUseForAlertsInd(useForAlertsInd);
    if (useForAlertsInd) {
      bdmEmailEvidenceVO.setAlertFrequency(BDMALERTOCCUR.PERINFO);
    }
    final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
      new ArrayList<BDMEmailEvidenceVO>();
    bdmEmailEvidenceVOList.add(bdmEmailEvidenceVO);

    new Expectations() {

      {
        citizen.getIdentifier();
        result = VALID_IDENTIFIER;

        bdmEmailEvidence.getEvidenceValueObject(anyLong);
        result = bdmEmailEvidenceVOList;
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
  private void verificationEmailElementAttributes(final Element expected,
    final Element result)
    throws AppException, InformationalException, NoSuchSchemaException {

    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.EMAIL_ADDRESS),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.EMAIL_ADDRESS));
    assertEquals(
      expected.getAttributeValue(BDMLifeEventDatastoreConstants.EMAIL_TYPE),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.EMAIL_TYPE));
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
        .getAttributeValue(BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL),
      result
        .getAttributeValue(BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL));
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.EMAIL_EVIDENCE_ID),
      result
        .getAttributeValue(BDMLifeEventDatastoreConstants.EMAIL_EVIDENCE_ID));
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
    final String emailAdr = VALID_EMAIL;
    final String emailType = EMAILTYPE.PERSONAL;
    final Boolean isPreferredCommunication = true;
    final Boolean alertPrefEmail = true;
    final Long emailEvidenceID = 0l;
    final String resultMessage = "valid";

    // Expectations
    expectationEmailVOAttributes(emailAdr, emailType,
      isPreferredCommunication, alertPrefEmail, emailEvidenceID);
    final Element expectedCommunicationDetailsElement =
      setUpCommunicationDetailsElement(emailAdr, emailType,
        isPreferredCommunication, alertPrefEmail, emailEvidenceID);

    // Test Private Method returned Element
    final Element resultElement = Deencapsulation.invoke(
      bdmLifeEventEmailViewProcessor, "setPersonEntityAttributes",
      new Object[]{personEl, citizenData, citizen });
    final Element resultCommunicationDetailsElement = resultElement
      .getChild(BDMLifeEventDatastoreConstants.COMMUNICATION_DETAILS);

    // Verifications
    verificationEmailElementAttributes(expectedCommunicationDetailsElement,
      resultCommunicationDetailsElement);
  }
}
