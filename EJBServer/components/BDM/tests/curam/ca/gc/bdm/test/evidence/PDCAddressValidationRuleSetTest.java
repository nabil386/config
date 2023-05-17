package curam.ca.gc.bdm.test.evidence;

import com.google.inject.Inject;
import curam.ca.gc.bdm.message.BDMBPOADDRESS;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRY;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.PROVINCETYPE;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.intf.Address;
import curam.core.intf.AddressData;
import curam.core.intf.ConcernRoleAddress;
import curam.core.intf.UniqueID;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressKey;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ElementDetails;
import curam.core.struct.OtherAddressData;
import curam.core.struct.ValidateAddressResult;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PDCAddressValidationRuleSetTest extends BDMEvidenceUtilsTest {

  public PDCAddressValidationRuleSetTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  protected ConcernRoleDAO concernRoleDAO;

  @Inject
  protected EvidenceTypeVersionDefDAO etVerDefDAO;

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  private static final String kFromDateAttrName = "fromDate";

  private static final String kParticipant = "participant";

  private static final String kToDateAttrName = "fromDate";

  private static final String kAddressType = "addressType";

  /**
   * Test that the address of type Residential is not allow to enter PO Boxes
   * number
   * addressLine3 =POBOX
   *
   */
  @Test
  public void test_validateResidentialAddressNotAllowedPOBoxes()
    throws InformationalException, AppException {

    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    // create an address
    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressID = UniqueIDFactory.newInstance().getNextID();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final AddressData addressDataObj = AddressDataFactory.newInstance();
    final curam.util.message.CatEntry expectedException =
      BDMBPOADDRESS.ERR_RESIDENTIALADDRESS_POBOX_SHALL_NOT_BE_ENTERED;

    // Create address with no city entered.
    addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "15";
    addressFieldDetails.addressLine1 = "PO ";
    addressFieldDetails.addressLine2 = "Cliffs avenue";
    addressFieldDetails.addressLine3 = "PO 123";
    addressFieldDetails.city = "City";
    addressFieldDetails.province = PROVINCETYPE.ALBERTA;
    addressFieldDetails.postalCode = "T6T 8U8";
    addressFieldDetails.countryCode = COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    addressObj.insert(addressDtls);

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    // based on domain CONCERN_ROLE_ADDRESS_ID
    long concernRoleAddressID = 0;
    // generate unique key for new concernRoleAddress record
    concernRoleAddressID = uniqueIDObj.getNextID();

    // create concern role address record
    final ConcernRoleAddress concernRoleAddressObj =
      ConcernRoleAddressFactory.newInstance();
    final ConcernRoleAddressDtls concernRoleAddressDtls =
      new ConcernRoleAddressDtls();
    concernRoleAddressDtls.assign(addressDtls);
    concernRoleAddressDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    concernRoleAddressDtls.concernRoleAddressID = concernRoleAddressID;
    concernRoleAddressDtls.addressID = addressDtls.addressID;
    concernRoleAddressDtls.typeCode = CONCERNROLEADDRESSTYPE.PRIVATE;
    concernRoleAddressDtls.concernRoleAddressID = concernRoleAddressID;
    concernRoleAddressDtls.concernRoleID = pdcPersonDetails.concernRoleID;

    // Test 1: validate fails because PO box numbers are not alloed for
    // residential address.
    try {
      concernRoleAddressObj.pdcInsert(concernRoleAddressDtls);
    } catch (final AppException ae) {
      assertEquals(expectedException, ae.getCatEntry());
    }

  }

  /**
   * Test that the address of type Residential is not allow to enter PO Boxes
   * number
   * addressLine3 =POBOX
   *
   */
  @Test
  public void test_validateMAILINGAddressAllowedPOBoxes()
    throws InformationalException, AppException {

    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    // create an address
    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressID = 1000L;
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final AddressData addressDataObj = AddressDataFactory.newInstance();

    // Create address with no city entered.
    addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "15";
    addressFieldDetails.addressLine1 = "PO ";
    addressFieldDetails.addressLine2 = "Cliffs avenue";
    addressFieldDetails.addressLine3 = "PO 123";
    addressFieldDetails.city = "City";
    addressFieldDetails.province = PROVINCETYPE.ALBERTA;
    addressFieldDetails.postalCode = "T6T 8U8";
    addressFieldDetails.countryCode = COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    addressObj.insert(addressDtls);

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    // based on domain CONCERN_ROLE_ADDRESS_ID
    final long concernRoleAddressID = uniqueIDObj.getNextID();

    // create concern role address record
    final ConcernRoleAddress concernRoleAddressObj =
      ConcernRoleAddressFactory.newInstance();
    final ConcernRoleAddressDtls concernRoleAddressDtls =
      new ConcernRoleAddressDtls();
    concernRoleAddressDtls.assign(addressDtls);
    concernRoleAddressDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    concernRoleAddressDtls.concernRoleAddressID = concernRoleAddressID;
    concernRoleAddressDtls.addressID = addressDtls.addressID;
    concernRoleAddressDtls.typeCode = CONCERNROLEADDRESSTYPE.MAILING;
    concernRoleAddressDtls.concernRoleAddressID = concernRoleAddressID;
    concernRoleAddressDtls.concernRoleID = pdcPersonDetails.concernRoleID;
    concernRoleAddressDtls.startDate = Date.getCurrentDate();

    // Addres insert must be successfull
    concernRoleAddressObj.pdcInsert(concernRoleAddressDtls);

    final ConcernRoleAddressKey concernRoleAddressKey =
      new ConcernRoleAddressKey();
    concernRoleAddressKey.concernRoleAddressID =
      concernRoleAddressDtls.concernRoleAddressID;

    final ConcernRoleAddressDtls craddressDtls =
      concernRoleAddressObj.read(concernRoleAddressKey);

    final AddressKey addressKey = new AddressKey();
    addressKey.addressID = craddressDtls.addressID;

    final AddressDtls createdAddressDtls = addressObj.read(addressKey);

    assertEquals("PO 123", getAddressDataElement(
      createdAddressDtls.addressData, ADDRESSELEMENTTYPE.POBOXNO));

  }

  /**
   *
   * Method to retrieve required address data elemnt from the address data.
   *
   * @param addressData The address data.
   * @return Returns a string
   * @throws AppException
   * @throws InformationalException
   */
  private final String getAddressDataElement(final String addressData,
    final String addressElementType)
    throws AppException, InformationalException {

    String poBox = "";
    final AddressData addressDataObj = AddressDataFactory.newInstance();
    final OtherAddressData otherAddressData = new OtherAddressData();
    otherAddressData.addressData = addressData;

    final ValidateAddressResult validateAddressResult =
      new ValidateAddressResult();
    validateAddressResult.addressMapList =
      addressDataObj.parseDataToMap(otherAddressData);

    final curam.core.struct.AddressMap addressMap =
      new curam.core.struct.AddressMap();

    addressMap.name = addressElementType;
    final ElementDetails elementPOBOX = addressDataObj
      .findElement(validateAddressResult.addressMapList, addressMap);

    poBox = elementPOBOX.elementValue;
    return poBox;
  }

  /* Successfull Adresss creation test */
  @Test
  public void testAddressOverLap_samedates()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    final String ERR_ADDRESSTYPE_WITH_OVERLAPPING_TIME_NOT_ALLOWED =
      "An address of the same type and overlapping time period is not allowed.";

    final Date fromDate = Date.fromISO8601("20010101");
    final Date toDate = Date.fromISO8601("20010101");

    // create new adress evidence with same data toDate // fromDate , should
    // throw overlapping error message
    try {
      createAddressEvidence(pdcPersonDetails, CONCERNROLEADDRESSTYPE.PRIVATE,
        fromDate, toDate);
    } catch (final Exception e) {
      assertEquals(ERR_ADDRESSTYPE_WITH_OVERLAPPING_TIME_NOT_ALLOWED,
        e.getMessage());
    }
  }

  @Test
  public void testEndDatecCannotBeSpecified()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails = createPDCPerson();
    final String ERR_ENDDATE_CANNOTBE_SPECIFIED =
      "An end date cannot be specified, if there is only one address of the same type.";
    // create an address
    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressID = 1000L;
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final AddressData addressDataObj = AddressDataFactory.newInstance();

    // Create address with no city entered.
    addressFieldDetails.addressLayoutType = ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "15";
    addressFieldDetails.addressLine1 = "PO ";
    addressFieldDetails.addressLine2 = "Cliffs avenue";
    addressFieldDetails.city = "City";
    addressFieldDetails.province = PROVINCETYPE.ALBERTA;
    addressFieldDetails.postalCode = "T6T 8U8";
    addressFieldDetails.countryCode = COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    addressObj.insert(addressDtls);

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    // based on domain CONCERN_ROLE_ADDRESS_ID
    final long concernRoleAddressID = uniqueIDObj.getNextID();

    // create concern role address record
    final ConcernRoleAddress concernRoleAddressObj =
      ConcernRoleAddressFactory.newInstance();
    final ConcernRoleAddressDtls concernRoleAddressDtls =
      new ConcernRoleAddressDtls();
    concernRoleAddressDtls.assign(addressDtls);
    concernRoleAddressDtls.statusCode = curam.codetable.RECORDSTATUS.NORMAL;
    concernRoleAddressDtls.concernRoleAddressID = concernRoleAddressID;
    concernRoleAddressDtls.addressID = addressDtls.addressID;
    concernRoleAddressDtls.typeCode = CONCERNROLEADDRESSTYPE.MAILING;
    concernRoleAddressDtls.concernRoleAddressID = concernRoleAddressID;
    concernRoleAddressDtls.concernRoleID = pdcPersonDetails.concernRoleID;

    // Start date
    concernRoleAddressDtls.startDate = Date.fromISO8601("20000101");

    // Addres insert must be successfull
    concernRoleAddressObj.pdcInsert(concernRoleAddressDtls);

    final ConcernRoleAddressKey concernRoleAddressKey =
      new ConcernRoleAddressKey();
    concernRoleAddressKey.concernRoleAddressID =
      concernRoleAddressDtls.concernRoleAddressID;

    final ConcernRoleAddressDtls craddressDtls =
      concernRoleAddressObj.read(concernRoleAddressKey);

    final AddressKey addressKey = new AddressKey();
    addressKey.addressID = craddressDtls.addressID;

    final AddressDtls createdAddressDtls = addressObj.read(addressKey);

    assertEquals("City", getAddressDataElement(createdAddressDtls.addressData,
      ADDRESSELEMENTTYPE.CITY));

    // Update address to enter endate
    // Start date

    concernRoleAddressDtls.endDate = Date.fromISO8601("20000101");

    // Address modify shiulg throw exception
    try {
      concernRoleAddressObj.pdcModify(concernRoleAddressKey,
        concernRoleAddressDtls);
    } catch (final Exception e) {
      assertEquals(ERR_ENDDATE_CANNOTBE_SPECIFIED, e.getMessage());
    }

  }

  // @Test
  public void testEndDatePrecedingStartdate()
    throws AppException, InformationalException {

    final PDCPersonDetails pdcPersonDetails = createPDCPerson();

    final String ERR_ENDDATE_1DAY_PRECEDING_STARTDATE =
      "End date should be 1 day preceding the start date of the new address under the same address type.";
    final Date fromDate = Date.fromISO8601("20010101");
    final Date toDate = Date.fromISO8601("20010105");

    final EIEvidenceKey key = createAddressEvidence(pdcPersonDetails,
      CONCERNROLEADDRESSTYPE.PRIVATE, fromDate, toDate);

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();
    final EIEvidenceReadDtls currEIEvidenceReadDtls =
      evidenceControllerObj.readEvidence(key);

    final Date fromDate1 = Date.fromISO8601("20001201");
    final Date toDate1 = Date.fromISO8601("20010101");

    // create new adress evidence with toDate and fromDate
    try {
      createAddressEvidence(pdcPersonDetails, CONCERNROLEADDRESSTYPE.PRIVATE,
        fromDate1, toDate1);
    } catch (final Exception e) {
      assertEquals(ERR_ENDDATE_1DAY_PRECEDING_STARTDATE, e.getMessage());
    }

  }

  /**
   * Inserts the Participant Email Address evidence .
   *
   * @param details The person details.
   * @param emailAddress
   * @return The evidence key.
   * Participant Email Address evidence details to be inserted.
   */
  protected EIEvidenceKey createAddressEvidence(
    final PDCPersonDetails details, final String addresType,
    final Date fromDate, final Date toDate)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = details.concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCADDRESS;

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

    // TODO:
    assignAddressEvidenceDetails(dynamicEvidenceDataDetails, addresType,
      fromDate, toDate);

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
   * Assigns participant's address to the dynamic evidence data
   * struct.
   *
   * @param details
   * Participant email
   * @param dynamicEvidenceDataDetails
   * Dynamic evidence details.
   */
  private void assignAddressEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String paramAddressType, final Date paramFromDate,
    final Date paramToDate) throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails addressType =
      dynamicEvidenceDataDetails.getAttribute(kAddressType);
    DynamicEvidenceTypeConverter.setAttribute(addressType,
      new CodeTableItem(CONCERNROLEADDRESSTYPE.TABLENAME, paramAddressType));

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute(kFromDateAttrName);
    DynamicEvidenceTypeConverter.setAttribute(fromDate, paramFromDate);

    final DynamicEvidenceDataAttributeDetails toDate =
      dynamicEvidenceDataDetails.getAttribute(kToDateAttrName);
    DynamicEvidenceTypeConverter.setAttribute(toDate, paramToDate);

    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressID = UniqueIDFactory.newInstance().getNextID();
    final Address addressObj = AddressFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // modify person address details
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "Unit 123";
    addressFieldDetails.addressLine1 = "180";
    addressFieldDetails.addressLine2 = "Richmond Road";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "City";
    addressFieldDetails.postalCode = "L5A 1V9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    addressDtls.addressData =
      addressDataObj.parseFieldsToData(addressFieldDetails).addressData;

    addressObj.insert(addressDtls);

    final DynamicEvidenceDataAttributeDetails address =
      dynamicEvidenceDataDetails.getAttribute("address");
    DynamicEvidenceTypeConverter.setAttribute(address,
      Long.valueOf(addressDtls.addressID));

  }

  protected void addAddressEvidence(final String addressType,
    final Date receivedDate, final String province) throws Exception {

    final Address addressObj = AddressFactory.newInstance();
    final AddressDtls addressDtls = new AddressDtls();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "23";
    addressFieldDetails.addressLine1 = "Oakville";
    addressFieldDetails.addressLine2 = "Cooksville";
    addressFieldDetails.province = province;
    addressFieldDetails.city = "Maple";
    addressFieldDetails.postalCode = "L5A 2Y7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    addressDtls.addressData = otherAddressData.addressData;

    addressDtls.countryCode = curam.codetable.COUNTRY.CA;
    addressDtls.modifiableInd = false;
    addressObj.insert(addressDtls);

  }

  /*
   *
   *
   * ERR_ADDRESS_POBOX_RESIDENTIAL=P.O. Box shall not be entered for a
   * residential address.
   * ERR_PREFERRED_FOR_RESIDENTIAL=The preferred flag can be set for residential
   * addresses only.
   * ERR_ADDRESSTYPE_WITH_OVERLAPPING_TIME_NOT_ALLOWED=An address of the same
   * type and overlapping time period is not allowed.
   *
   *
   * ERR_ENDDATE_NO_LATER_STARTDATE=End date should not be later than existing
   * start date of the new address under the same address type.
   * ERR_ENDDATE_1DAY_PRECEDING_STARTDATE=End date should be 1 day preceding the
   * start date of the new address under the same address type.
   * ERR_ENDDATE_CANNOTBE_SPECIFIED=An end date cannot be specified, if there is
   * only one address of the same type.
   * ERR_ATLEAST_1RESIDENTIAL=At least one residential address must be present
   * in the system.
   * ERR_ATLEAST_1MAILING=At least one mailing address must be present in the
   * system.
   */

}
