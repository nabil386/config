package curam.ca.gc.bdm.sl.address.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.facade.address.fact.BDMAddressFactory;
import curam.ca.gc.bdm.facade.address.intf.BDMAddress;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.ca.gc.bdm.sl.address.fact.BDMPDCAddressFactory;
import curam.ca.gc.bdm.sl.address.intf.BDMPDCAddress;
import curam.ca.gc.bdm.sl.address.struct.BDMParticipantAddressDetails;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchResult;
import curam.ca.gc.bdm.test.data.BDMPersonTestData;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.intf.Address;
import curam.core.intf.ConcernRole;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.MaintainAddressKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PrimaryAddressDetails;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.PDCPersonDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import jdk.nashorn.internal.ir.annotations.Ignore;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
@Ignore
public class BDMPDCAddressTest extends CuramServerTestJUnit4 {

  @Inject
  private EvidenceTypeDefDAO evidenceTypeDefDAO;

  @Inject
  private EvidenceTypeVersionDefDAO evidenceTypeVersionDefDAO;

  private static final String PARTICIPANT = "participant";

  private static final String COMMENTS = "comments";

  private static final String ADDRESS = "address";

  private static final String FROM_DATE = "fromDate";

  private static final String TO_DATE = "toDate";

  private static final String ADDRESS_TYPE = "addressType";

  private static final String PREFERRED_IND = "preferredInd";

  public static final String RECEIVED_FROM = "bdmReceivedFrom";

  public static final String RECEIVED_FROM_COUNTRY = "bdmReceivedFromCountry";

  public static final String MODE_OF_RECEIPT = "bdmModeOfReceipt";

  public BDMPDCAddressTest() {

    super();
  }

  /**
   * PASS-IF a validation fails when additional required source fields are
   * not entered when the Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void mandatorySourceFieldFailure()
    throws AppException, InformationalException {

    final BDMPDCAddress address = BDMPDCAddressFactory.newInstance();
    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final MaintainAddressKey addressKey1 = new MaintainAddressKey();
    addressKey1.concernRoleID = concernRoleID;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = pdcPersonDetails.concernRoleID;

    final BDMParticipantAddressDetails addressDetails =
      new BDMParticipantAddressDetails();

    final BDMAddress bdmAddressObj = BDMAddressFactory.newInstance();
    final BDMAddressSearchResult result = new BDMAddressSearchResult();

    final BDMPersonTestData bdmPersonDataObj = new BDMPersonTestData();

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    addressDetails.bdmReceivedFrom = BDMRECEIVEDFROM.FOREIGN_GOVERNMENT;
    addressDetails.addressData = otherAddressData.addressData;
    addressDetails.comments = "Address test";
    addressDetails.bdmModeOfReceipt = BDMMODEOFRECEIPT.CERTIFIED_APP;
    addressDetails.caseID = UniqueIDFactory.newInstance().getNextID();
    addressDetails.concernRoleAddressID =
      UniqueIDFactory.newInstance().getNextID();
    addressDetails.concernRoleID = pdcPersonDetails.concernRoleID;
    addressDetails.endDate = Date.getDate("20220101");
    addressDetails.startDate = Date.getDate("20200101");
    addressDetails.statusCode = RECORDSTATUS.NORMAL;
    addressDetails.typeCode = "R";

    final Exception exception =
      Assert.assertThrows(AppException.class, () -> {
        address.validateSourceFields(addressDetails);
      });

    assertTrue(BDMEVIDENCE.ERR_FOREIGN_GOVT_REQUIRED_FIELDS.getMessageText()
      .equals(exception.getMessage()));

  }

  @Test
  public void testcreateAddress()
    throws AppException, InformationalException {

    final BDMPDCAddress address = BDMPDCAddressFactory.newInstance();
    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final long concernRoleID = pdcPersonDetails.concernRoleID;
    final MaintainAddressKey addressKey1 = new MaintainAddressKey();
    addressKey1.concernRoleID = concernRoleID;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = pdcPersonDetails.concernRoleID;

    final BDMParticipantAddressDetails addressDetails =
      new BDMParticipantAddressDetails();

    final BDMAddress bdmAddressObj = BDMAddressFactory.newInstance();
    final BDMAddressSearchResult result = new BDMAddressSearchResult();

    final BDMPersonTestData bdmPersonDataObj = new BDMPersonTestData();

    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;

    addressFieldDetails.suiteNum = "5789";
    addressFieldDetails.addressLine1 = "Cruise Ave";
    addressFieldDetails.addressLine2 = "Train street";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Ontario";
    addressFieldDetails.postalCode = "K5M 1G7";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;

    OtherAddressData otherAddressData = null;

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    addressDetails.bdmReceivedFrom = BDMRECEIVEDFROM.FOREIGN_GOVERNMENT;
    addressDetails.addressData = otherAddressData.addressData;
    addressDetails.comments = "Address test";
    addressDetails.bdmModeOfReceipt = BDMMODEOFRECEIPT.CERTIFIED_APP;
    addressDetails.caseID = UniqueIDFactory.newInstance().getNextID();
    addressDetails.concernRoleAddressID =
      UniqueIDFactory.newInstance().getNextID();
    addressDetails.concernRoleID = pdcPersonDetails.concernRoleID;
    addressDetails.endDate = Date.getDate("20220101");
    addressDetails.startDate = Date.getDate("20200101");
    addressDetails.statusCode = RECORDSTATUS.NORMAL;
    addressDetails.typeCode = "R";

    final Exception exception =
      Assert.assertThrows(AppException.class, () -> {
        address.createAddress(addressDetails);
      });
    final BDMParticipantAddressDetails details =
      new BDMParticipantAddressDetails();

    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);
    // final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
    // this.getDynamicEvidenceDataDetails();
    // final AddressDtls addressDtls = this.insertAddress(details);
    //
    // assignEvidenceDetails(pdcCaseIDCaseParticipantRoleID, details,
    // dynamicEvidenceDataDetails, addressDtls);
    //
    // this.insertEvidence(dynamicEvidenceDataDetails, details,
    // pdcCaseIDCaseParticipantRoleID);

    assertTrue(BDMEVIDENCE.ERR_FOREIGN_GOVT_REQUIRED_FIELDS.getMessageText()
      .equals(exception.getMessage()));

  }

  private void insertEvidence(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final BDMParticipantAddressDetails details,
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID)
    throws AppException, InformationalException {

    final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();
    evidenceTypeKey.evidenceType = PDCConst.PDCADDRESS;

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();

    evidenceDescriptorInsertDtls.participantID = details.concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = evidenceTypeKey.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.caseID =
      pdcCaseIDCaseParticipantRoleID.caseID;

    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();

    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = details.concernRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

    evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);

  }

  private void assignEvidenceDetails(
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID,
    final BDMParticipantAddressDetails details,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final AddressDtls addressDtls)
    throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute(PARTICIPANT);

    DynamicEvidenceTypeConverter.setAttribute(participant,
      Long.valueOf(pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID));

    final DynamicEvidenceDataAttributeDetails address =
      dynamicEvidenceDataDetails.getAttribute(ADDRESS);

    DynamicEvidenceTypeConverter.setAttribute(address,
      Long.valueOf(addressDtls.addressID));

    final DynamicEvidenceDataAttributeDetails startDate =
      dynamicEvidenceDataDetails.getAttribute(FROM_DATE);

    if (!details.startDate.isZero()) {
      DynamicEvidenceTypeConverter.setAttribute(startDate, details.startDate);
    } else {
      startDate.setValue("");
    }

    final DynamicEvidenceDataAttributeDetails endDate =
      dynamicEvidenceDataDetails.getAttribute(TO_DATE);

    if (!details.endDate.isZero()) {
      DynamicEvidenceTypeConverter.setAttribute(endDate, details.endDate);
    } else {
      endDate.setValue("");
    }

    final DynamicEvidenceDataAttributeDetails addressType =
      dynamicEvidenceDataDetails.getAttribute(ADDRESS_TYPE);
    DynamicEvidenceTypeConverter.setAttribute(addressType,
      new CodeTableItem(CONCERNROLEADDRESSTYPE.TABLENAME, details.typeCode));

    final DynamicEvidenceDataAttributeDetails bdmReceivedFrom =
      dynamicEvidenceDataDetails.getAttribute(RECEIVED_FROM);
    bdmReceivedFrom.setValue(details.bdmReceivedFrom);

    final DynamicEvidenceDataAttributeDetails bdmReceivedFromCountry =
      dynamicEvidenceDataDetails.getAttribute(RECEIVED_FROM_COUNTRY);
    bdmReceivedFromCountry.setValue(details.bdmReceivedFromCountry);

    final DynamicEvidenceDataAttributeDetails bdmModeOfReceipt =
      dynamicEvidenceDataDetails.getAttribute(MODE_OF_RECEIPT);
    bdmModeOfReceipt.setValue(details.bdmModeOfReceipt);

    final DynamicEvidenceDataAttributeDetails comments =
      dynamicEvidenceDataDetails.getAttribute(COMMENTS);
    DynamicEvidenceTypeConverter.setAttribute(comments, details.comments);

    final DynamicEvidenceDataAttributeDetails preferredInd =
      dynamicEvidenceDataDetails.getAttribute(PREFERRED_IND);
    DynamicEvidenceTypeConverter.setAttribute(preferredInd,
      Boolean.valueOf(false));

    if (details.primaryAddressInd) {

      DynamicEvidenceTypeConverter.setAttribute(preferredInd,
        Boolean.valueOf(true));

    } else if (details.concernRoleAddressID != 0L) {

      final ConcernRoleAddressKey concernRoleAddressKey =
        new ConcernRoleAddressKey();

      concernRoleAddressKey.concernRoleAddressID =
        details.concernRoleAddressID;

      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

      final ConcernRoleAddressDtls storedConcernRoleAddressDtls =
        ConcernRoleAddressFactory.newInstance().read(notFoundIndicator,
          concernRoleAddressKey);

      if (!notFoundIndicator.isNotFound()) {

        final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

        concernRoleKey.concernRoleID = details.concernRoleID;
        final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
        final PrimaryAddressDetails primaryAddressDetails =
          concernRoleObj.readPrimaryAddress(concernRoleKey);

        if (primaryAddressDetails.primaryAddressID == storedConcernRoleAddressDtls.addressID) {
          DynamicEvidenceTypeConverter.setAttribute(preferredInd,
            Boolean.valueOf(true));
        }

      }

    }

  }

  private AddressDtls
    insertAddress(final BDMParticipantAddressDetails details)
      throws AppException, InformationalException {

    final Address addressObj = AddressFactory.newInstance();
    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressData = details.addressData;
    addressObj.insert(addressDtls);
    return addressDtls;

  }

  private DynamicEvidenceDataDetails getDynamicEvidenceDataDetails() {

    final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();
    evidenceTypeKey.evidenceType = PDCConst.PDCADDRESS;

    final EvidenceTypeDef evidenceTypeDef = this.evidenceTypeDefDAO
      .readActiveEvidenceTypeDefByTypeCode(evidenceTypeKey.evidenceType);

    final EvidenceTypeVersionDef evidenceTypeVersionDef =
      this.evidenceTypeVersionDefDAO.getActiveEvidenceTypeVersionAtDate(
        evidenceTypeDef, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evidenceTypeVersionDef);

    return dynamicEvidenceDataDetails;

  }

  /**
   * PASS-IF a validation pass when additional required source fields are
   * are entered when the Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void mandatorySourceFieldPass()
    throws AppException, InformationalException {

    final BDMPDCAddress address = BDMPDCAddressFactory.newInstance();
    final BDMParticipantAddressDetails addressDetails =
      new BDMParticipantAddressDetails();
    addressDetails.bdmReceivedFrom = BDMRECEIVEDFROM.FOREIGN_GOVERNMENT;
    addressDetails.bdmReceivedFromCountry = BDMSOURCECOUNTRY.AUSTRIA;
    addressDetails.bdmModeOfReceipt = BDMMODEOFRECEIPT.CERTIFIED_APP;

    address.validateSourceFields(addressDetails);

  }

  /**
   * PASS-IF a validation fails when additional source fields are
   * entered and the Received From is not Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void disallowedSourceFieldFailure()
    throws AppException, InformationalException {

    final BDMPDCAddress address = BDMPDCAddressFactory.newInstance();
    final BDMParticipantAddressDetails addressDetails =
      new BDMParticipantAddressDetails();
    addressDetails.bdmReceivedFrom = BDMRECEIVEDFROM.CLIENT_REPORTED;
    addressDetails.bdmReceivedFromCountry = BDMSOURCECOUNTRY.AUSTRIA;
    addressDetails.bdmModeOfReceipt = BDMMODEOFRECEIPT.CERTIFIED_APP;

    final Exception exception =
      Assert.assertThrows(AppException.class, () -> {
        address.validateSourceFields(addressDetails);
      });

    assertTrue(BDMEVIDENCE.ERR_FOREIGN_GOVT_EXCLUSIVE_FIELDS.getMessageText()
      .equals(exception.getMessage()));

  }

  /**
   * PASS-IF a validation passes when additional source fields are
   * entered and the Received From is Foreign Government.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void disallowedSourceFieldPass()
    throws AppException, InformationalException {

    final BDMPDCAddress address = BDMPDCAddressFactory.newInstance();
    final BDMParticipantAddressDetails addressDetails =
      new BDMParticipantAddressDetails();
    addressDetails.bdmReceivedFrom = BDMRECEIVEDFROM.FOREIGN_GOVERNMENT;
    addressDetails.bdmReceivedFromCountry = BDMSOURCECOUNTRY.AUSTRIA;
    addressDetails.bdmModeOfReceipt = BDMMODEOFRECEIPT.CERTIFIED_APP;

    address.validateSourceFields(addressDetails);

  }

  /**
   * PASS-IF a validation fails when an Received From is Foreign
   * Government, Mode of Receipt is Liaison and the Received From Country is one
   * of the restricted countries.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void restrictedCountryFailure()
    throws AppException, InformationalException {

    final BDMPDCAddress address = BDMPDCAddressFactory.newInstance();
    final BDMParticipantAddressDetails addressDetails =
      new BDMParticipantAddressDetails();
    addressDetails.bdmReceivedFrom = BDMRECEIVEDFROM.FOREIGN_GOVERNMENT;
    addressDetails.bdmReceivedFromCountry = BDMSOURCECOUNTRY.US;
    addressDetails.bdmModeOfReceipt = BDMMODEOFRECEIPT.LIAISON;

    final Exception exception =
      Assert.assertThrows(AppException.class, () -> {
        address.validateSourceFields(addressDetails);
      });

    assertTrue(BDMEVIDENCE.ERR_RESTRICTED_COUNTRY.getMessageText()
      .equals(exception.getMessage()));

  }

  /**
   * PASS-IF a validation passes when an Received From is Foreign
   * Government, Mode of Receipt is Liaison and the Received From Country is not
   * one of the restricted countries.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void restrictedCountryPass()
    throws AppException, InformationalException {

    final BDMPDCAddress address = BDMPDCAddressFactory.newInstance();
    final BDMParticipantAddressDetails addressDetails =
      new BDMParticipantAddressDetails();
    addressDetails.bdmReceivedFrom = BDMRECEIVEDFROM.FOREIGN_GOVERNMENT;
    addressDetails.bdmReceivedFromCountry = BDMSOURCECOUNTRY.AUSTRIA;
    addressDetails.bdmModeOfReceipt = BDMMODEOFRECEIPT.CERTIFIED_APP;

    address.validateSourceFields(addressDetails);

  }

}
