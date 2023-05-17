package curam.ca.gc.bdm.test.citizen.datahub.impl;

import com.ibm.icu.util.Calendar;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventIncomeViewProcessor;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncomeEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncomeEvidenceVO;
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
public class BDMLifeEventIncomeViewProcessorTest
  extends CustomFunctionTestJunit4 {

  /** A valid income amount. */
  private static final String VALID_INCOME = "10.00";

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
  BDMIncomeEvidence bdmIncomeEvidence;

  /** The validate function. */
  @Tested
  BDMLifeEventIncomeViewProcessor viewProcessor;

  public BDMLifeEventIncomeViewProcessorTest() {

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
  private Element setUpIncomeElement(final String incomeAmount,
    final String incomeYear, final long evidenceID)
    throws AppException, InformationalException {

    final Element expectedElement =
      new Element(BDMLifeEventDatastoreConstants.INCOME);

    expectedElement.setAttribute(BDMLifeEventDatastoreConstants.INCOME_AMOUNT,
      incomeAmount);
    expectedElement.setAttribute(BDMLifeEventDatastoreConstants.INCOME_YEAR,
      incomeYear);
    expectedElement.setAttribute(
      BDMLifeEventDatastoreConstants.INCOME_EVIDENCE_ID,
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
  private void expectationIncomeVOAttributes(final String incomeAmount,
    final String incomeYear, final long evidenceID)
    throws AppException, InformationalException, NoSuchSchemaException {

    final BDMIncomeEvidenceVO evidenceVO = new BDMIncomeEvidenceVO();
    evidenceVO.setIncomeAmount(incomeAmount);
    evidenceVO.setIncomeYear(incomeYear);
    evidenceVO.setEvidenceID(evidenceID);
    final List<BDMIncomeEvidenceVO> evidenceList = new ArrayList<>();
    evidenceList.add(evidenceVO);
    new Expectations() {

      {
        citizen.getIdentifier();
        result = VALID_IDENTIFIER;

        bdmIncomeEvidence.getEvidenceValueObject(anyLong);
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
        .getAttributeValue(BDMLifeEventDatastoreConstants.INCOME_AMOUNT),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.INCOME_AMOUNT));
    assertEquals(
      expected.getAttributeValue(BDMLifeEventDatastoreConstants.INCOME_YEAR),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.INCOME_YEAR));
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.INCOME_EVIDENCE_ID),
      result.getAttributeValue(
        BDMLifeEventDatastoreConstants.INCOME_EVIDENCE_ID));
  }

  /**
   * Test setIncomeEntityAttributes returns an element with all the expected
   * attributes positive result
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   */
  @Test
  public void test_valid_setIncomeEntityAttributes()
    throws AppException, InformationalException, NoSuchSchemaException {

    // Test Data
    final String incomeAmount = VALID_INCOME;
    final String incomeYear = previousYear();
    final Long evidenceID = 1l;
    // Expectations
    expectationIncomeVOAttributes(incomeAmount, incomeYear, evidenceID);
    final Element expectedElement =
      setUpIncomeElement(incomeAmount, incomeYear, evidenceID);
    // Test Private Method returned Element
    final Element resultElement =
      Deencapsulation.invoke(viewProcessor, "setIncomeEntityAttributes",
        new Object[]{personEl, citizenData, citizen });
    final Element resultCommunicationDetailsElement =
      resultElement.getChild(BDMLifeEventDatastoreConstants.INCOME);
    // Verifications
    verifyElementAttributes(expectedElement,
      resultCommunicationDetailsElement);
  }

  private String previousYear() {

    return Integer
      .toString(Date.getCurrentDate().getCalendar().get(Calendar.YEAR) - 1);
  }
}
