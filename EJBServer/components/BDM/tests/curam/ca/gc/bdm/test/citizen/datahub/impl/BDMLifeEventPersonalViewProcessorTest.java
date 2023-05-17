package curam.ca.gc.bdm.test.citizen.datahub.impl;

import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventPersonalViewProcessor;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidenceVO;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.citizen.datahub.impl.Citizen;
import curam.codetable.ALTERNATENAMETYPE;
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
public class BDMLifeEventPersonalViewProcessorTest
  extends CuramServerTestJUnit4 {

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
  BDMDateofBirthEvidence bdmDateofBirthEvidence;

  @Mocked
  BDMIdentificationEvidence bdmIdentificationEvidence;

  @Mocked
  BDMNamesEvidence bdmNamesEvidence;

  /** The validate function. */
  @Tested
  BDMLifeEventPersonalViewProcessor bdmLifeEventPersonalViewProcessor;

  public BDMLifeEventPersonalViewProcessorTest() {

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
   * Set DOB, SIN, and Name into datastore element
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private Element setUpPersonElement(final String lastNameAtBirth,
    final String parentsLastNameAtBirth, final Date dateOfBirth,
    final Long dobEvidenceID, final Long sinEvidenceID, final String sin,
    final String firstname, final String middlename, final String lastname,
    final Long nameEvidenceID) throws AppException, InformationalException {

    final Element expectedPersonElement =
      new Element(BDMLifeEventDatastoreConstants.PERSON);

    expectedPersonElement.setAttribute(
      BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH, lastNameAtBirth);
    expectedPersonElement.setAttribute(
      BDMLifeEventDatastoreConstants.PARENT_LAST_NAME_AT_BIRTH,
      parentsLastNameAtBirth);
    expectedPersonElement.setAttribute(
      BDMLifeEventDatastoreConstants.DATE_OF_BIRTH,
      DateTimeTools.formatDateISO(dateOfBirth));
    expectedPersonElement.setAttribute(
      BDMLifeEventDatastoreConstants.DOB_EVIDENCE_ID,
      dobEvidenceID.toString());
    expectedPersonElement.setAttribute(BDMLifeEventDatastoreConstants.SIN,
      sin);
    expectedPersonElement.setAttribute(
      BDMLifeEventDatastoreConstants.SIN_EVIDENCE_ID,
      sinEvidenceID.toString());
    expectedPersonElement
      .setAttribute(BDMLifeEventDatastoreConstants.FIRST_NAME, firstname);
    expectedPersonElement
      .setAttribute(BDMLifeEventDatastoreConstants.MIDDLE_NAME, middlename);
    expectedPersonElement
      .setAttribute(BDMLifeEventDatastoreConstants.LAST_NAME, lastname);
    expectedPersonElement.setAttribute(
      BDMLifeEventDatastoreConstants.NAME_EVIDENCE_ID,
      nameEvidenceID.toString());

    return expectedPersonElement;
  }

  /**
   * Expectations
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private void expectationDOBSINNameVOAttributes(final String lastNameAtBirth,
    final String parentsLastNameAtBirth, final Date dateOfBirth,
    final Long dobEvidenceID, final Long sinEvidenceID, final String sin,
    final String firstname, final String middlename, final String lastname,
    final Long nameEvidenceID)
    throws AppException, InformationalException, NoSuchSchemaException {

    // Set up DOB Value Object Expectations
    final BDMDateofBirthEvidenceVO bdmDateofBirthEvidenceVO =
      new BDMDateofBirthEvidenceVO();
    bdmDateofBirthEvidenceVO.setBirthLastName(lastNameAtBirth);
    bdmDateofBirthEvidenceVO.setMothersBirthLastName(parentsLastNameAtBirth);
    bdmDateofBirthEvidenceVO.setDateOfBirth(dateOfBirth);
    bdmDateofBirthEvidenceVO.setEvidenceID(dobEvidenceID);
    final List<BDMDateofBirthEvidenceVO> bdmDateofBirthEvidenceVOList =
      new ArrayList<BDMDateofBirthEvidenceVO>();
    bdmDateofBirthEvidenceVOList.add(bdmDateofBirthEvidenceVO);

    // Set up SIN Value Object Expectations
    final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVO =
      new BDMIdentificationEvidenceVO();
    bdmIdentificationEvidenceVO.setAlternateID(sin);
    bdmIdentificationEvidenceVO.setEvidenceID(sinEvidenceID);
    final List<BDMIdentificationEvidenceVO> bdmIdentificationEvidenceVOList =
      new ArrayList<BDMIdentificationEvidenceVO>();
    bdmIdentificationEvidenceVOList.add(bdmIdentificationEvidenceVO);

    // Set up Name Value Object Expectations
    final BDMNamesEvidenceVO bdmNamesEvidenceVO = new BDMNamesEvidenceVO();
    bdmNamesEvidenceVO.setNameType(ALTERNATENAMETYPE.REGISTERED);
    bdmNamesEvidenceVO.setFirstName(firstname);
    bdmNamesEvidenceVO.setMiddleName(middlename);
    bdmNamesEvidenceVO.setLastName(lastname);
    bdmNamesEvidenceVO.setEvidenceID(nameEvidenceID);
    final List<BDMNamesEvidenceVO> bdmNamesEvidenceVOList =
      new ArrayList<BDMNamesEvidenceVO>();
    bdmNamesEvidenceVOList.add(bdmNamesEvidenceVO);

    new Expectations() {

      {
        citizen.getIdentifier();
        result = VALID_IDENTIFIER;

        bdmDateofBirthEvidence.getDOBEvidenceValueObject(anyLong);
        result = bdmDateofBirthEvidenceVOList;

        bdmIdentificationEvidence.getSINEvidenceValueObject(anyLong);
        result = bdmIdentificationEvidenceVO;

        bdmNamesEvidence.getNamesEvidenceValueObject(anyLong);
        result = bdmNamesEvidenceVOList;
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
  private void verificationDOBSINNameElementAttributes(final Element expected,
    final Element result)
    throws AppException, InformationalException, NoSuchSchemaException {

    // DOB
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH),
      result.getAttributeValue(
        BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH));
    assertEquals(
      expected.getAttributeValue(
        BDMLifeEventDatastoreConstants.PARENT_LAST_NAME_AT_BIRTH),
      result.getAttributeValue(
        BDMLifeEventDatastoreConstants.PARENT_LAST_NAME_AT_BIRTH));
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.DATE_OF_BIRTH),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.DATE_OF_BIRTH));
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.DOB_EVIDENCE_ID),
      result
        .getAttributeValue(BDMLifeEventDatastoreConstants.DOB_EVIDENCE_ID));

    // SIN
    assertEquals(
      expected.getAttributeValue(BDMLifeEventDatastoreConstants.SIN),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.SIN));
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.SIN_EVIDENCE_ID),
      result
        .getAttributeValue(BDMLifeEventDatastoreConstants.SIN_EVIDENCE_ID));

    // Name
    assertEquals(
      expected.getAttributeValue(BDMLifeEventDatastoreConstants.FIRST_NAME),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.FIRST_NAME));
    assertEquals(
      expected.getAttributeValue(BDMLifeEventDatastoreConstants.MIDDLE_NAME),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.MIDDLE_NAME));
    assertEquals(
      expected.getAttributeValue(BDMLifeEventDatastoreConstants.LAST_NAME),
      result.getAttributeValue(BDMLifeEventDatastoreConstants.LAST_NAME));
    assertEquals(
      expected
        .getAttributeValue(BDMLifeEventDatastoreConstants.NAME_EVIDENCE_ID),
      result
        .getAttributeValue(BDMLifeEventDatastoreConstants.NAME_EVIDENCE_ID));
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
    final String lastNameAtBirth = "lastNameAtBirth";
    final String parentsLastNameAtBirth = "parentsLastNameAtBirth";
    final Date dateOfBirth = Date.getCurrentDate();
    final Long dobEvidenceID = 121l;
    final Long sinEvidenceID = 122l;
    final String sin = "865000111";
    final String firstname = "firstname";
    final String middlename = "middlename";
    final String lastname = "lastname";
    final Long nameEvidenceID = 123l;

    // Expectations
    expectationDOBSINNameVOAttributes(lastNameAtBirth, parentsLastNameAtBirth,
      dateOfBirth, dobEvidenceID, sinEvidenceID, sin, firstname, middlename,
      lastname, nameEvidenceID);
    final Element expectedPersonElement = setUpPersonElement(lastNameAtBirth,
      parentsLastNameAtBirth, dateOfBirth, dobEvidenceID, sinEvidenceID, sin,
      firstname, middlename, lastname, nameEvidenceID);

    // Test Private Method returned Element
    final Element resultPersonElement = Deencapsulation.invoke(
      bdmLifeEventPersonalViewProcessor, "setPersonEntityAttributes",
      new Object[]{personEl, citizenData, citizen });

    // Verifications
    verificationDOBSINNameElementAttributes(expectedPersonElement,
      resultPersonElement);
  }
}
