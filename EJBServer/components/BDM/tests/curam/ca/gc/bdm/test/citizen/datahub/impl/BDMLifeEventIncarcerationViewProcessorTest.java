package curam.ca.gc.bdm.test.citizen.datahub.impl;

import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventIncarcerationViewProcessor;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncarcerationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncarcerationEvidenceVO;
import curam.ca.gc.bdm.test.rules.functions.CustomFunctionTestJunit4;
import curam.citizen.datahub.impl.Citizen;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.core.impl.EnvVars;
import curam.datastore.impl.NoSuchSchemaException;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import curam.workspaceservices.util.impl.DateTimeTools;
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
public class BDMLifeEventIncarcerationViewProcessorTest
  extends CustomFunctionTestJunit4 {

  /** A valid institution name. */
  private static final String VALID_INSTITUTION = "testInstitution";

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
  BDMIncarcerationEvidence bdmIncarcerationEvidence;

  /** The validate function. */
  @Tested
  BDMLifeEventIncarcerationViewProcessor viewProcessor;

  public BDMLifeEventIncarcerationViewProcessorTest() {

    super();
  }

  /**
   * Before each test, init the tested class.
   */
  @Before
  public void before() throws AppException, InformationalException {

    // Set concern information - create person with given ID
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
  private Element setUpIncarcerationElement(final String instName,
    final Date startDateInca, final Date endDateInca, final long evidenceID)
    throws AppException, InformationalException {

    final Element expectedElement =
      new Element(BDMLifeEventDatastoreConstants.INCARCERATION);

    expectedElement.setAttribute(
      BDMLifeEventDatastoreConstants.INCARCERATION_Name, instName);
    expectedElement.setAttribute(
      BDMLifeEventDatastoreConstants.INCARCERATION_START_DATE,
      DateTimeTools.formatDateISO(startDateInca));
    expectedElement.setAttribute(
      BDMLifeEventDatastoreConstants.INCARCERATION_END_DATE,
      DateTimeTools.formatDateISO(endDateInca));
    expectedElement.setAttribute(
      BDMLifeEventDatastoreConstants.INCARCERATION_EVIDENCE_ID,
      Long.toString(evidenceID));

    return expectedElement;
  }

  /**
   * Expectations
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private void expectationIncarcerationVOAttributes(final String instName,
    final Date startDateInca, final Date endDateInca, final long evidenceID)
    throws AppException, InformationalException, NoSuchSchemaException {

    final BDMIncarcerationEvidenceVO evidenceVO =
      new BDMIncarcerationEvidenceVO();
    evidenceVO.setInstitutionName(instName);
    evidenceVO.setStartDate(startDateInca);
    evidenceVO.setEndDate(endDateInca);
    evidenceVO.setEvidenceID(evidenceID);
    final List<BDMIncarcerationEvidenceVO> evidenceList = new ArrayList<>();
    evidenceList.add(evidenceVO);
    new Expectations() {

      {
        citizen.getIdentifier();
        result = VALID_IDENTIFIER;

        bdmIncarcerationEvidence.getEvidenceValueObject(anyLong);
        result = evidenceList;
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
  private void verifyElementAttributes(final Element expected,
    final Element result)
    throws AppException, InformationalException, NoSuchSchemaException {

    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.INCARCERATION_Name),
      result.getAttributeValue(
        BDMLifeEventDatastoreConstants.INCARCERATION_Name));
    assertEquals(
      expected.getAttributeValue(
        BDMLifeEventDatastoreConstants.INCARCERATION_START_DATE),
      result.getAttributeValue(
        BDMLifeEventDatastoreConstants.INCARCERATION_START_DATE));
    assertEquals(
      expected.getAttributeValue(
        BDMLifeEventDatastoreConstants.INCARCERATION_END_DATE),
      result.getAttributeValue(
        BDMLifeEventDatastoreConstants.INCARCERATION_END_DATE));
    assertEquals(
      expected.getAttributeValue(
        BDMLifeEventDatastoreConstants.INCARCERATION_EVIDENCE_ID),
      result.getAttributeValue(
        BDMLifeEventDatastoreConstants.INCARCERATION_EVIDENCE_ID));
  }

  /**
   * Test setIncomeEntityAttributes returns an element with all the expected
   * attributes positive result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_setIncarcerationEntityAttributes()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String instName = VALID_INSTITUTION;
    final Date startDate = Date.getCurrentDate().addDays(-20);
    final Date endDate = Date.getCurrentDate();
    final Long evidenceID = 1l;
    // Expectations
    expectationIncarcerationVOAttributes(instName, startDate, endDate,
      evidenceID);
    final Element expectedElement =
      setUpIncarcerationElement(instName, startDate, endDate, evidenceID);
    // Test Private Method returned Element
    final Element resultElement = Deencapsulation.invoke(viewProcessor,
      "setIncarcerationEntityAttributes",
      new Object[]{personEl, citizenData, citizen });
    final Element resultIncarcerationElement =
      resultElement.getChild(BDMLifeEventDatastoreConstants.INCARCERATION);
    // Verifications
    verifyElementAttributes(expectedElement, resultIncarcerationElement);
  }

}
