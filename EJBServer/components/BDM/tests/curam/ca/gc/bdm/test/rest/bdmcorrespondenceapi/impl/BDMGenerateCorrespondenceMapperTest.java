package curam.ca.gc.bdm.test.rest.bdmcorrespondenceapi.impl;

import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.impl.BDMGenerateCorrespondenceMapper;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import mockit.Tested;
import org.junit.After;
import org.junit.Before;

/**
 * Unit test for class BDMGenerateCorrespondenceMapper
 */
public class BDMGenerateCorrespondenceMapperTest
  extends CuramServerTestJUnit4 {

  /** The Coreespondence API helper. */
  @Tested
  BDMGenerateCorrespondenceMapper generateCorrespondence;

  /**
   * Instantiates a new BDM correspondence test.
   */
  public BDMGenerateCorrespondenceMapperTest() {

    super();
  }

  /**
   * Before.
   */
  @Before
  public void before() {

    generateCorrespondence = new BDMGenerateCorrespondenceMapper();
  }

  /**
   * After each test.
   */
  @After
  public void after() {

  }

  /**
   * Test search address with no post code
   *
   * @throws InformationalException the informational exception
   */
  // @Test
  // public void test_correspondence_mapping()
  // throws InformationalException, AppException {
  //
  // // Postal code not present
  //
  // // setting input parameter struct
  // final BDMCorrespondenceDetails bdmCorrespondenceDetails =
  // new BDMCorrespondenceDetails();
  // bdmCorrespondenceDetails.clientName = "Robert Redford";
  // bdmCorrespondenceDetails.toDetails.addressLineOne =
  // "10-123 1/2 MAIN ST NW";
  // bdmCorrespondenceDetails.toDetails.addressLineThree =
  // "MONTREAL QC H32 2Y6";
  // bdmCorrespondenceDetails.toDetails.addressLineFour = "CANADA";
  // bdmCorrespondenceDetails.clientDtls.sin = 800754213;
  // bdmCorrespondenceDetails.clientDtls.gender = "M";
  // bdmCorrespondenceDetails.clientDtls.preferredLanguage = "en-ca";
  //
  // bdmCorrespondenceDetails.ccDetails.clientAddressLineOne =
  // "445 1/3 YONGE ST SE";
  // bdmCorrespondenceDetails.ccDetails.clientAddressLineThree =
  // "NEW YORK CITY NJ H7J 4N8";
  // bdmCorrespondenceDetails.ccDetails.clientAddressLineFour = "CANADA";
  //
  // try {
  // final String correspondencemapped =
  // generateCorrespondence.correspondenceMapper(bdmCorrespondenceDetails);
  //
  // // convert returned string to xml document
  // final Document xmlDocument =
  // convertStringToXMLDocument(correspondencemapped);
  //
  // // extract different nodes from xml doc
  // xmlDocument.getDocumentElement().normalize();
  // final NodeList personNameList =
  // xmlDocument.getElementsByTagName("PersonName");
  // final NodeList addressList =
  // xmlDocument.getElementsByTagName("AddressFullText");
  // final NodeList sinList =
  // xmlDocument.getElementsByTagName("sp:PersonSINIdentification");
  // final NodeList langList =
  // xmlDocument.getElementsByTagName("sp:PersonPreferredLanguage");
  //
  // // Assert
  // assertEquals("Robert Redford",
  // personNameList.item(0).getTextContent().toString().trim());
  // assertEquals("10-123 1/2 MAIN ST NW",
  // addressList.item(0).getTextContent());
  // // assertEquals("6135550234",
  // // telephoneList.item(0).getTextContent().trim());
  // assertEquals("800754213", sinList.item(0).getTextContent().trim());
  // assertEquals("E", langList.item(0).getTextContent().trim());
  //
  // } catch (final JAXBException e) {
  //
  // e.printStackTrace();
  // }
  //
  // InformationalException searchInformationalException = null;
  //
  // try {
  // TransactionInfo.getInformationalManager().failOperation();
  // } catch (final InformationalException informationalException) {
  // searchInformationalException = informationalException;
  // }
  // }
  //
  // private Document
  // convertStringToXMLDocument(final String correspondencemapped) {
  //
  // try {
  // final DocumentBuilderFactory factory =
  // DocumentBuilderFactory.newInstance();
  //
  // // create document builder with default config
  // final DocumentBuilder builder = factory.newDocumentBuilder();
  //
  // // parse the content of the string to xml document
  // final Document doc = builder
  // .parse(new InputSource(new StringReader(correspondencemapped)));
  //
  // return doc;
  //
  // } catch (final Exception e) {
  // e.printStackTrace();
  // }
  // return null;
  // }
  //
}
