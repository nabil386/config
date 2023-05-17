package curam.ca.gc.bdm.test.citizen.datahub.impl;

import curam.ca.gc.bdm.address.impl.BDMAddressFormatINTL;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventAddressViewProcessor;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventDatastoreConstants;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.citizen.datahub.impl.Citizen;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRY;
import curam.codetable.PROVINCETYPE;
import curam.codetable.impl.COUNTRYEntry;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.core.fact.AddressFactory;
import curam.core.impl.EnvVars;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.datastore.impl.NoSuchSchemaException;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import curam.util.type.UniqueID;
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
import org.junit.tools.configuration.base.MethodRef;
import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class BDMLifeEventAddressViewProcessorTest
  extends CuramServerTestJUnit4 {

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

  AddressFieldDetails addrDetails;

  List<DynamicEvidenceDataDetails> currentAddressEvidence;

  @Mocked
  BDMEmailEvidence bdmEmailEvidence;

  @Mocked
  BDMUtil bdmUtil;

  @Mocked
  BDMEvidenceUtil bdmEvidenceUtil;

  /** The validate function. */
  @Tested
  BDMLifeEventAddressViewProcessor bdmLifeEventAddressViewProcessor;

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
   * Set dynamic Evidence attribute details.
   *
   * @param dynamicEvidenceDataDetails
   * @param attributeName
   * - Dynamic Attribute Name
   * @param value
   * - Dynamic Attribute value
   */
  public void setDynamicEvidenceAttribute(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String attributeName, final String value) {

    final DynamicEvidenceDataAttributeDetails dynamicEvidenceDataAttributeDetails =
      dynamicEvidenceDataDetails.getAttribute(attributeName);

    if (dynamicEvidenceDataAttributeDetails != null) {
      dynamicEvidenceDataAttributeDetails.setValue(value);
    }
  }

  /**
   * Expectations
   *
   * @throws AppException the app exception
   * @throws InformationalException the informational exception
   * @throws NoSuchSchemaException the no such schema exception
   */
  private void expectationAddressAttributes()
    throws AppException, InformationalException, NoSuchSchemaException {

    currentAddressEvidence = new ArrayList<>();

    addrDetails = new AddressFieldDetails();
    addrDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addrDetails.addressLine1 = "123456ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    addrDetails.addressLine2 = "Cornelius Cummingham Georgiana Street";
    addrDetails.city = "Windsor Malcolm Arizona Manitoba";
    addrDetails.postalCode = "M5V 1A1";
    addrDetails.countryCode = COUNTRY.CA;
    addrDetails.province = PROVINCETYPE.NEWFOUNDLANDANDLABRADOR;

    final long addressID = UniqueID.nextUniqueID();
    addrDetails.addressID = addressID;

    final OtherAddressData otherAddressData =
      new BDMAddressFormatINTL().parseFieldsToData(addrDetails);

    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressData = otherAddressData.addressData;
    addressDtls.addressID = addressID;
    addressDtls.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressDtls.countryCode = COUNTRY.CA;

    AddressFactory.newInstance().insert(addressDtls);

    final DynamicEvidenceDataDetails details =
      DynamicEvidenceDataDetailsFactory.newInstance(CASEEVIDENCE.BDMADDRESS,
        Date.getCurrentDate());
    setDynamicEvidenceAttribute(details, "addressType",
      CONCERNROLEADDRESSTYPE.PRIVATE);
    setDynamicEvidenceAttribute(details, "address",
      Long.toString(addressDtls.addressID));
    setDynamicEvidenceAttribute(details, "fromDate",
      Date.getCurrentDate().toString());
    setDynamicEvidenceAttribute(details, "participant",
      Long.toString(VALID_IDENTIFIER));

    currentAddressEvidence.add(details);

    new Expectations() {

      {
        citizen.getIdentifier();
        result = VALID_IDENTIFIER;
        bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(200l,
          PDCConst.PDCADDRESS);
        result = currentAddressEvidence;
        bdmEvidenceUtil.getExistingOtherAddressData(anyLong);
        result = otherAddressData;
        bdmUtil.getAddressDetails((OtherAddressData) any, (String) any);
        result = COUNTRYEntry.CA.getCode().toString();

      }
    };

  }

  @MethodRef(name = "setPersonEntityAttributes",
    signature = "(QElement;QDocument;QCitizen;)QElement;")
  @Test
  public void testSetPersonEntityAttributes() throws Exception {

    final BDMLifeEventAddressViewProcessor testSubject;

    expectationAddressAttributes();

    final Element resultElement = Deencapsulation.invoke(
      bdmLifeEventAddressViewProcessor, "setPersonEntityAttributes",
      new Object[]{personEl, citizenData, citizen });
    assertEquals(
      resultElement.getAttributeValue(
        BDMLifeEventDatastoreConstants.RESIDENTIAL_COUNTRY),
      COUNTRYEntry.CA.getCode().toString());

  }
}
