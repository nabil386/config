package curam.ca.gc.bdmoas.test.evidence;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMALERTOCCUR;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdmoas.test.concern.person.RegisterPerson;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.BANKACCOUNTSTATUS;
import curam.codetable.BANKACCOUNTTYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.EMAILTYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.PHONETYPE;
import curam.codetable.PROVINCETYPE;
import curam.commonintake.entity.struct.ApplicationCaseKey;
import curam.core.fact.AddressFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.PersonRegistrationFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.intf.Address;
import curam.core.intf.PersonRegistration;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.struct.EvidenceDescriptorDetails;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonRegistrationDetails;
import curam.core.struct.RegistrationIDDetails;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.sl.impl.CpDetailsAdaptor;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.GenericSLDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.pdc.struct.PDCPersonDetails;
import curam.testframework.CuramServerTestJUnit4;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.Money;

/**
 *
 * 01/26/2022 10847 agole createIdentificationEvidence Method modified to
 * generate Identification EVidence using incoming parameter
 *
 * 01/28/2022 10844 agole New Method createNameEvidence added to
 * generate Name EVidence using incoming parameter
 *
 */
public class BDMEvidenceUtilsTest extends CuramServerTestJUnit4 {

  public BDMEvidenceUtilsTest() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  protected EvidenceTypeVersionDefDAO etVerDefDAO;

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  private final String kNominee = "nominee";

  private final String kPercentageValue = "percentageValue";

  private final String kAmount = "amount";

  protected static final String kBankAccountNumber = "accountNumber";

  private static final String kToDateAttrName = "fromDate";

  private static final String kAddressType = "addressType";

  protected static final String kBankSortCode = "sortCode";

  protected static final String kAccountType = "accountType";

  protected static final String kAccountStatus = "accountStatus";

  protected static final String kJointAccountInd = "jointAccountInd";

  protected static final String kAccountName = "accountName";

  protected static final String kFromDateAttrName = "fromDate";

  protected static final String kParticipant = "participant";

  protected static final String kEmailAddressType = "emailAddressType";

  protected static final String kEmailAddress = "emailAddress";

  protected static final String ERR_EMAILADDRESS_INVALID =
    "Email address must be valid.";

  protected static final String kPreferred = "preferredInd";

  protected static final String knameType = "nameType";

  protected static final String klastName = "lastName";

  protected static final String kfirstName = "firstName";

  protected static final String kphoneType = "phoneType";

  protected static final String kphoneAreaCode = "phoneAreaCode";

  protected static final String kphoneCountryCode = "phoneCountryCode";

  protected static final String kphoneNumber = "phoneNumber";

  protected static final String kphoneExtension = "phoneExtension";

  protected static final String kAltIDTypeAttrName = "altIDType";

  protected static final String kAlternateID = "alternateID";

  protected final String kcaseParticipant = "caseParticipant";

  protected final String kStartDate = "startDate";

  protected final String kInstitutionName = "institutionName";

  RegisterPerson registerPerson = new RegisterPerson("RegisterPerson");

  private final BDMUtil bdmUtil = new BDMUtil();

  public PDCPersonDetails createPDCPerson()
    throws AppException, InformationalException {

    final PersonRegistrationDetails personRegistrationDetails =
      registerPerson.getPersonRegistrationDetails();

    final PersonRegistration personRegistration =
      PersonRegistrationFactory.newInstance();

    final RegistrationIDDetails registrationIDDetails =
      personRegistration.registerPerson(personRegistrationDetails);

    final PersonDtls personDtls =
      registerPerson.readPerson(registrationIDDetails.concernRoleID);

    final PDCPersonDetails pdcPersonDetails = new PDCPersonDetails();
    pdcPersonDetails.assign(personRegistrationDetails);
    pdcPersonDetails.assign(personDtls);
    return pdcPersonDetails;

  }

  // ___________________________________________________________________________
  /**
   * Inserts the Participant Email Address evidence .
   *
   * @param details The person details.
   * @param emailAddress
   * @return The evidence key.
   * Participant Email Address evidence details to be inserted.
   */

  // createEmailAddressEvidence(pdcPersonDetails,
  // "test1@test.test.com", EMAILTYPE.PERSONAL, true, true);
  public EIEvidenceKey createEmailAddressEvidence(
    final PDCPersonDetails details, final String emailAddress,
    final String emailType, final boolean preferredInd, final boolean alerts,
    final String alertFrequency) throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = details.concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCEMAILADDRESS;

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

    final DynamicEvidenceDataAttributeDetails preferred =
      dynamicEvidenceDataDetails.getAttribute(kPreferred);
    DynamicEvidenceTypeConverter.setAttribute(preferred, preferredInd);

    final DynamicEvidenceDataAttributeDetails useForAlerts =
      dynamicEvidenceDataDetails.getAttribute("useForAlertsInd");
    DynamicEvidenceTypeConverter.setAttribute(useForAlerts, alerts);

    assignEmailAddressEvidenceDetails(dynamicEvidenceDataDetails,
      emailAddress, emailType, alertFrequency);

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
   * Assigns participant's email to the dynamic evidence data
   * struct.
   *
   * @param details
   * Participant email
   * @param dynamicEvidenceDataDetails
   * Dynamic evidence details.
   */
  private void assignEmailAddressEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String emailAddress, final String paramEmailType,
    final String alertFrequency) throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails emailType =
      dynamicEvidenceDataDetails.getAttribute(kEmailAddressType);
    DynamicEvidenceTypeConverter.setAttribute(emailType,
      new CodeTableItem(EMAILTYPE.TABLENAME, paramEmailType));

    final DynamicEvidenceDataAttributeDetails email =
      dynamicEvidenceDataDetails.getAttribute(kEmailAddress);
    DynamicEvidenceTypeConverter.setAttribute(email, emailAddress);

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute(kFromDateAttrName);
    DynamicEvidenceTypeConverter.setAttribute(fromDate,
      Date.getCurrentDate());
    // -- Added AJERTS FREQUENCY --->
    final DynamicEvidenceDataAttributeDetails alertFrequency1 =
      dynamicEvidenceDataDetails.getAttribute("alertFrequency");
    DynamicEvidenceTypeConverter.setAttribute(alertFrequency1,
      new CodeTableItem(BDMALERTOCCUR.TABLENAME, alertFrequency));
    //// -- Added AJERTS FREQUENCY ---
  }

  // PHONE NUMBER EVIDNECE

  // ___________________________________________________________________________
  /**
   * Inserts the Participant Phone Address evidence .
   *
   * @param details The person details.
   * @param countryCode
   * @param paramPhoneNumber
   * @param paramAreaCode
   * @param paramExtension
   * @param paramPhoneType
   * @param preferredInd
   * @param useForAlertsInd
   *
   * @return The evidence key.
   * Participant Phone evidence details to be inserted.
   */
  public EIEvidenceKey createPhoneEvidence(final PDCPersonDetails details,
    final String countryCode, final String paramPhoneNumber,
    final String paramAreaCode, final String paramExtension,
    final String paramPhoneType, final boolean preferredInd,
    final boolean useForAlertsInd)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = details.concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCPHONENUMBER;

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

    final DynamicEvidenceDataAttributeDetails preferred =
      dynamicEvidenceDataDetails.getAttribute(kPreferred);
    DynamicEvidenceTypeConverter.setAttribute(preferred, preferredInd);
    final DynamicEvidenceDataAttributeDetails useforAlerts =
      dynamicEvidenceDataDetails.getAttribute("useForAlertsInd");
    DynamicEvidenceTypeConverter.setAttribute(useforAlerts, useForAlertsInd);

    assignPhoneEvidenceDetails(dynamicEvidenceDataDetails, countryCode,
      paramPhoneNumber, paramAreaCode, paramExtension, paramPhoneType);

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
   * Assigns participant's phone to the dynamic evidence data
   * struct. * @param details
   * Participant phone
   *
   * @param dynamicEvidenceDataDetails
   * Dynamic evidence details.
   */
  private void assignPhoneEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String countryCode, final String paramPhoneNumber,
    final String paramAreaCode, final String paramExtension,
    final String paramPhoneType) throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails phoneType =
      dynamicEvidenceDataDetails.getAttribute(kphoneType);
    DynamicEvidenceTypeConverter.setAttribute(phoneType,
      new CodeTableItem(PHONETYPE.TABLENAME, paramPhoneType));

    final DynamicEvidenceDataAttributeDetails phoneNum =
      dynamicEvidenceDataDetails.getAttribute(kphoneNumber);
    DynamicEvidenceTypeConverter.setAttribute(phoneNum, paramPhoneNumber);

    DynamicEvidenceDataAttributeDetails areaCode;
    if (!StringUtil.isNullOrEmpty(paramAreaCode)) {
      areaCode = dynamicEvidenceDataDetails.getAttribute(kphoneAreaCode);
      DynamicEvidenceTypeConverter.setAttribute(areaCode, paramAreaCode);
    }
    DynamicEvidenceDataAttributeDetails ext;
    if (!StringUtil.isNullOrEmpty(paramExtension)) {
      ext = dynamicEvidenceDataDetails.getAttribute(kphoneExtension);
      DynamicEvidenceTypeConverter.setAttribute(ext, paramExtension);
    }

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute(kFromDateAttrName);
    DynamicEvidenceTypeConverter.setAttribute(fromDate,
      Date.getCurrentDate());

    final DynamicEvidenceDataAttributeDetails country =
      dynamicEvidenceDataDetails.getAttribute("phoneCountryCode");
    DynamicEvidenceTypeConverter.setAttribute(country,
      new CodeTableItem(BDMPHONECOUNTRY.TABLENAME, countryCode));
    // -- Added AJERTS FREQUENCY --->
    final DynamicEvidenceDataAttributeDetails alertOccur =
      dynamicEvidenceDataDetails.getAttribute("alertFrequency");
    DynamicEvidenceTypeConverter.setAttribute(alertOccur,
      new CodeTableItem(BDMALERTOCCUR.TABLENAME, BDMALERTOCCUR.PERDAY));
    // -- Added AJERTS FREQUENCY --->

  }

  /**
   * @date: Jan. 26, 2022
   * @decription: Method modified to generate Identification EVidence using
   * incoming parameter
   *
   * @param @param details
   * @param @param idNumber
   * @param @param idType
   * @param @param preferredInd
   * @param @return
   * @param @throws AppException
   * @param @throws InformationalException
   * @reEIEvidenceKey
   * @throws
   */
  public EIEvidenceKey createIdentificationEvidence(
    final PDCPersonDetails details, final String idNumber,
    final String idType, final boolean preferredInd)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = details.concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCIDENTIFICATION;

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

    final DynamicEvidenceDataAttributeDetails preferred =
      dynamicEvidenceDataDetails.getAttribute(kPreferred);
    DynamicEvidenceTypeConverter.setAttribute(preferred, preferredInd);

    assignIDNumberEvidenceDetails(dynamicEvidenceDataDetails, idNumber,
      idType);

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
   * @date: Jan. 26, 2022
   * @decription: Method modified to generate Identification EVidence using
   * incoming parameter
   *
   * @param @param dynamicEvidenceDataDetails
   * @param @param IdNumber
   * @param @param idType
   * @param @throws AppException
   * @param @throws InformationalException
   * @revoid
   * @throws
   */
  private void assignIDNumberEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String IdNumber, final String idType)
    throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails altIDType =
      dynamicEvidenceDataDetails.getAttribute(kAltIDTypeAttrName);
    DynamicEvidenceTypeConverter.setAttribute(altIDType, new CodeTableItem(
      curam.codetable.CONCERNROLEALTERNATEID.TABLENAME, idType));

    final DynamicEvidenceDataAttributeDetails sinNum =
      dynamicEvidenceDataDetails.getAttribute(kAlternateID);
    DynamicEvidenceTypeConverter.setAttribute(sinNum, IdNumber);

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute(kFromDateAttrName);
    DynamicEvidenceTypeConverter.setAttribute(fromDate,
      Date.getCurrentDate());

  }

  /**
   * @date: Jan. 28, 2022
   * @decription: Method modified to generate Name EVidence using
   * incoming parameter
   *
   * @param @param details
   * @param @param idNumber
   * @param @param idType
   * @param @param preferredInd
   * @param @return
   * @param @throws AppException
   * @param @throws InformationalException
   * @reEIEvidenceKey
   * @throws
   */
  protected EIEvidenceKey createNameEvidence(final PDCPersonDetails details,
    final String firstName, final String lastName, final String preferredInd)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = details.concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCNAME;

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

    final DynamicEvidenceDataAttributeDetails preferred =
      dynamicEvidenceDataDetails.getAttribute(knameType);
    DynamicEvidenceTypeConverter.setAttribute(preferred,
      new CodeTableItem(ALTERNATENAMETYPE.TABLENAME, preferredInd));

    assignNameEvidenceDetails(dynamicEvidenceDataDetails, firstName,
      lastName);

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
   * @date: Jan. 28, 2022
   * @decription: Method modified to generate name EVidence using
   * incoming parameter
   *
   * @param @param dynamicEvidenceDataDetails
   * @param @param IdNumber
   * @param @param idType
   * @param @throws AppException
   * @param @throws InformationalException
   * @revoid
   * @throws
   */
  private void assignNameEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String firstName, final String lastName)
    throws AppException, InformationalException {

    /*
     * final DynamicEvidenceDataAttributeDetails altIDType =
     * dynamicEvidenceDataDetails.getAttribute(kAltIDTypeAttrName);
     * DynamicEvidenceTypeConverter.setAttribute(altIDType,
     * new CodeTableItem(CONCERNROLEALTERNATEID.TABLENAME, idType));
     */

    final DynamicEvidenceDataAttributeDetails dFirstName =
      dynamicEvidenceDataDetails.getAttribute(kfirstName);
    DynamicEvidenceTypeConverter.setAttribute(dFirstName, firstName);

    final DynamicEvidenceDataAttributeDetails dLastName =
      dynamicEvidenceDataDetails.getAttribute(klastName);
    DynamicEvidenceTypeConverter.setAttribute(dLastName, lastName);

  }

  /**
   * Inserts the Participant Bank Account evidence .
   *
   * @param concernRoleID
   * @param bankAccountNumber
   * @param bankSortCode
   * @return
   * @throws AppException
   * @throws InformationalException
   */

  private EIEvidenceKey createBankAccountEvidence(final long concernRoleID,
    final String bankAccountNumber, final String bankSortCode)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = concernRoleID;

    // Get the PDC case id and primary case participant role for that case.
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = PDCConst.PDCBANKACCOUNT;

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

    final DynamicEvidenceDataAttributeDetails preferred =
      dynamicEvidenceDataDetails.getAttribute(kPreferred);
    DynamicEvidenceTypeConverter.setAttribute(preferred, Boolean.FALSE);
    final DynamicEvidenceDataAttributeDetails useForAlerts =
      dynamicEvidenceDataDetails.getAttribute(kJointAccountInd);
    DynamicEvidenceTypeConverter.setAttribute(useForAlerts, Boolean.FALSE);
    assignBankAccountEvidenceDetails(dynamicEvidenceDataDetails,
      bankAccountNumber, bankSortCode);
    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // Call the EvidenceController object and insert evidence
    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();

    evidenceDescriptorInsertDtls.participantID = concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = eType.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.effectiveFrom = Date.getCurrentDate();

    evidenceDescriptorInsertDtls.caseID =
      pdcCaseIDCaseParticipantRoleID.caseID;

    // Evidence Interface details
    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();

    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = concernRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

    return evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);
  }

  /**
   * Assigns participant's Bank Account to the dynamic evidence data
   * struct.
   *
   * @param dynamicEvidenceDataDetails
   * @param bankAccountNumber
   * @param bankSortCode
   * @throws AppException
   * @throws InformationalException
   */
  private void assignBankAccountEvidenceDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final String bankAccountNumber, final String bankSortCode)
    throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails accountType =
      dynamicEvidenceDataDetails.getAttribute(kAccountType);
    DynamicEvidenceTypeConverter.setAttribute(accountType, new CodeTableItem(
      BANKACCOUNTTYPE.TABLENAME, BANKACCOUNTTYPE.PERSONALCURR));
    final DynamicEvidenceDataAttributeDetails accountStatus =
      dynamicEvidenceDataDetails.getAttribute(kAccountStatus);
    DynamicEvidenceTypeConverter.setAttribute(accountStatus,
      new CodeTableItem(BANKACCOUNTSTATUS.TABLENAME, BANKACCOUNTSTATUS.OPEN));

    final DynamicEvidenceDataAttributeDetails accountNumber =
      dynamicEvidenceDataDetails.getAttribute(kBankAccountNumber);
    DynamicEvidenceTypeConverter.setAttribute(accountNumber,
      bankAccountNumber);
    final DynamicEvidenceDataAttributeDetails sortCode =
      dynamicEvidenceDataDetails.getAttribute(kBankSortCode);
    DynamicEvidenceTypeConverter.setAttribute(sortCode, bankSortCode);

    final DynamicEvidenceDataAttributeDetails accountName =
      dynamicEvidenceDataDetails.getAttribute(kAccountName);
    DynamicEvidenceTypeConverter.setAttribute(accountName, "Test Account");

    final DynamicEvidenceDataAttributeDetails fromDate =
      dynamicEvidenceDataDetails.getAttribute(kFromDateAttrName);
    DynamicEvidenceTypeConverter.setAttribute(fromDate,
      Date.getCurrentDate());
  }

  /**
   * Create test data
   *
   * @param bankAccountNumber
   * @param bankSortCode
   * @return
   * @throws Exception
   */
  public EIEvidenceKey createPersonWithBankAccount(
    final String bankAccountNumber, final String bankSortCode)
    throws Exception {

    // register person
    final RegisterPerson registerPersonObj = new RegisterPerson(getName());

    final PersonRegistrationDetails personRegistrationDetails =
      registerPersonObj.getPersonRegistrationDetails();
    final RegistrationIDDetails registrationIDDetails =
      PersonRegistrationFactory.newInstance()
        .registerPerson(personRegistrationDetails);
    // create Person Level Bank Account Evidence
    final EIEvidenceKey _returnKey = createBankAccountEvidence(
      registrationIDDetails.concernRoleID, bankAccountNumber, bankSortCode);
    // insert case header
    final CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();
    caseHeaderDtls.appealIndicator = false;
    caseHeaderDtls.caseReference = "caseReference";
    caseHeaderDtls.versionNo = 1;
    caseHeaderDtls.concernRoleID = registrationIDDetails.concernRoleID;
    caseHeaderDtls.startDate = Date.getCurrentDate();
    caseHeaderDtls.caseTypeCode = CASETYPECODE.PRODUCTDELIVERY;
    caseHeaderDtls.statusCode = CASESTATUS.ACTIVE;

    caseHeaderDtls.caseID = UniqueIDFactory.newInstance().getNextID();

    CaseHeaderFactory.newInstance().insert(caseHeaderDtls);
    return _returnKey;
  }

  public curam.core.sl.struct.EvidenceKey createIncarcerationEvidence(
    final ApplicationCaseKey appcaseKey, final long concernroleID,
    final Date startDate, final String institutionName)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDMINCARCERATION;

    // get caseParticipantROleID of the person
    final long caseParticipantRoleid =
      bdmUtil.getCaseParticipantRoleID(appcaseKey.applicationCaseID,
        concernroleID).caseParticipantRoleID;

    // get Latest Version of incarceration Evidence
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kcaseParticipant),
      caseParticipantRoleid);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kStartDate), startDate);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kInstitutionName),
      institutionName);

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = appcaseKey.applicationCaseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(caseParticipantRoleid);
    genericDtls.addRelCp("caseParticipant", cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

  public EIEvidenceKey createAddressEvidence1(final PDCPersonDetails details,
    final String addresType, final Date fromDate, final Date toDate)
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

  /**
   *
   * @param appcaseKey
   * @param concernroleID
   * @param dollarAmount
   * @param percentage
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public curam.core.sl.struct.EvidenceKey createVTWEvidence(
    final ApplicationCaseKey appcaseKey, final long concernroleID,
    final String dollarAmount, final int percentage)
    throws AppException, InformationalException {

    final EvidenceTypeKey eType = new EvidenceTypeKey();
    eType.evidenceType = CASEEVIDENCE.BDMVTW;

    // get caseParticipantROleID of the person
    final long caseParticipantRoleid =
      bdmUtil.getCaseParticipantRoleID(appcaseKey.applicationCaseID,
        concernroleID).caseParticipantRoleID;

    // get Latest Version of VTW Evidecne
    final GenericSLDataDetails genericDtls = new GenericSLDataDetails();
    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(eType, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidencedataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(eType.evidenceType,
        Date.getCurrentDate());
    // assigning all the required attributes
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kcaseParticipant),
      caseParticipantRoleid);
    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kNominee),
      caseParticipantRoleid);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kAmount),
      Money.doConversion(dollarAmount));

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kPercentageValue), percentage);

    DynamicEvidenceTypeConverter.setAttribute(
      dynamicEvidencedataDetails.getAttribute(kStartDate),
      Date.getCurrentDate());

    // set descriptor attributes to call OOTB Evidence creation logic
    final EvidenceDescriptorDetails descriptor =
      new EvidenceDescriptorDetails();
    descriptor.evidenceType = eType.evidenceType;
    descriptor.caseID = appcaseKey.applicationCaseID;
    descriptor.receivedDate = Date.getCurrentDate();
    descriptor.participantID = concernroleID;

    genericDtls.setData(dynamicEvidencedataDetails);
    genericDtls.setDescriptor(descriptor);
    genericDtls.setCaseIdKey(descriptor.caseID);

    final CpDetailsAdaptor cpAdaptor = new CpDetailsAdaptor();
    cpAdaptor.setCaseParticipantRoleID(caseParticipantRoleid);
    genericDtls.addRelCp("caseParticipant", cpAdaptor);

    return evidenceServiceInterface.createEvidence(genericDtls).evidenceKey;
  }

}
