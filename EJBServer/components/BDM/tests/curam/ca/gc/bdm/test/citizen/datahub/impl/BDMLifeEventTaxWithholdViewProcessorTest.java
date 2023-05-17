package curam.ca.gc.bdm.test.citizen.datahub.impl;

import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventTaxWithholdViewProcessor;
import curam.ca.gc.bdm.codetable.BDMDEDMETHOD;
import curam.ca.gc.bdm.lifeevent.impl.BDMVoluntaryTaxWithholdEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMVoluntaryTaxWithholdEvidenceVO;
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
public class BDMLifeEventTaxWithholdViewProcessorTest
  extends CustomFunctionTestJunit4 {

  /** A valid number. */
  private static final String VALID_NUMBER = "50";

  /** A valid number for money. */
  private static final String VALID_NUMBER_MONEY = "50.00";

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
  BDMVoluntaryTaxWithholdEvidence bdmVoluntaryTaxWithholdEvidence;

  /** The validate function. */
  @Tested
  BDMLifeEventTaxWithholdViewProcessor viewProcessor;

  public BDMLifeEventTaxWithholdViewProcessorTest() {

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
   * Set phone and evidenceID into datastore element
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private Element setUpTaxWithholdElement(final String amount,
    final String dollarPerct, final String dedMethod, final Date startDate,
    final Date endDate, final long evidenceID)
    throws AppException, InformationalException {

    final Element expectedElement =
      new Element(BDMLifeEventDatastoreConstants.TAX_INFO);

    expectedElement
      .setAttribute(BDMLifeEventDatastoreConstants.TAX_DOLLAR_AMOUNT, amount);
    expectedElement.setAttribute(
      BDMLifeEventDatastoreConstants.TAX_DOLLAR_PERCENT, dollarPerct);
    expectedElement
      .setAttribute(BDMLifeEventDatastoreConstants.TAX_DED_METHOD, dedMethod);
    expectedElement.setAttribute(
      BDMLifeEventDatastoreConstants.TAX_START_DATE,
      DateTimeTools.formatDateISO(startDate));
    expectedElement.setAttribute(BDMLifeEventDatastoreConstants.TAX_END_DATE,
      DateTimeTools.formatDateISO(endDate));
    expectedElement.setAttribute(
      BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_EVIDENCE_ID,
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
  private void expectationTaxWithholdVOAttributes(final String amount,
    final String dollarPerct, final String dedMethod, final Date startDate,
    final Date endDate, final long evidenceID)
    throws AppException, InformationalException, NoSuchSchemaException {

    final BDMVoluntaryTaxWithholdEvidenceVO evidenceVO =
      new BDMVoluntaryTaxWithholdEvidenceVO();
    evidenceVO.setAmount(amount);
    evidenceVO.setPercentageValue(dollarPerct);
    evidenceVO.setVoluntaryTaxWithholdType(dedMethod);
    evidenceVO.setStartDate(startDate);
    evidenceVO.setEndDate(endDate);
    evidenceVO.setEvidenceID(evidenceID);
    final List<BDMVoluntaryTaxWithholdEvidenceVO> evidenceList =
      new ArrayList<>();
    evidenceList.add(evidenceVO);
    new Expectations() {

      {
        citizen.getIdentifier();
        result = VALID_IDENTIFIER;

        bdmVoluntaryTaxWithholdEvidence
          .getVoluntaryTaxWithholdEvidenceValueObjectList(anyLong);
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
        .getAttributeValue(BDMLifeEventDatastoreConstants.TAX_DOLLAR_AMOUNT),
      result
        .getAttributeValue(BDMLifeEventDatastoreConstants.TAX_DOLLAR_AMOUNT));
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.TAX_START_DATE),
      result
        .getAttributeValue(BDMLifeEventDatastoreConstants.TAX_START_DATE));
    assertEquals(
      expected.getAttributeValue(BDMLifeEventDatastoreConstants.TAX_END_DATE),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.TAX_END_DATE));
    assertEquals(
      expected.getAttributeValue(
        BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_EVIDENCE_ID),
      result.getAttributeValue(
        BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_EVIDENCE_ID));
  }

  /**
   * Test setTaxWithholdEntityAttributes returns an element with all the
   * expected
   * attributes positive result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_setTaxWithholdEntityAttributes()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String amount = VALID_NUMBER_MONEY;
    final String dollarPerct = "";
    final String dedMethod = BDMDEDMETHOD.DOLLAR.toString();
    final Date startDate = Date.getCurrentDate().addDays(-20);
    final Date endDate = Date.getCurrentDate();
    final Long evidenceID = 1l;
    // Expectations
    expectationTaxWithholdVOAttributes(amount, dollarPerct, dedMethod,
      startDate, endDate, evidenceID);
    final Element expectedElement = setUpTaxWithholdElement(amount,
      dollarPerct, dedMethod, startDate, endDate, evidenceID);
    // Test Private Method returned Element
    final Element resultElement =
      Deencapsulation.invoke(viewProcessor, "setPersonEntityAttributes",
        new Object[]{personEl, citizenData, citizen });
    final Element resultTaxWithholdElement =
      resultElement.getChild(BDMLifeEventDatastoreConstants.TAX_INFO);
    // Verifications
    verifyElementAttributes(expectedElement, resultTaxWithholdElement);
  }

}
