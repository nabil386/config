package curam.ca.gc.bdm.rest.bdmcitizenaccountapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.citizen.datahub.impl.BDMLifeEventCustomProcessorUtil;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.codetable.impl.BDMALERTOCCUREntry;
import curam.ca.gc.bdm.codetable.impl.BDMCORRESDELIVERYEntry;
import curam.ca.gc.bdm.codetable.impl.BDMEDUCATIONLEVELEntry;
import curam.ca.gc.bdm.codetable.impl.BDMIEGYESNOOPTIONALEntry;
import curam.ca.gc.bdm.codetable.impl.BDMLANGUAGEEntry;
import curam.ca.gc.bdm.codetable.impl.BDMPHONECOUNTRYEntry;
import curam.ca.gc.bdm.codetable.impl.BDMVTWTYPEEntry;
import curam.ca.gc.bdm.entity.fact.BDMProspectPersonFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMCodeTableComboFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMPaymentDestinationFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMCodeTableCombo;
import curam.ca.gc.bdm.entity.financial.intf.BDMPaymentDestination;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadGovernCodeBySubOrdTableDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadGovernCodeBySubOrdTableKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMSearchEFTDestinationKey;
import curam.ca.gc.bdm.entity.intf.BDMProspectPerson;
import curam.ca.gc.bdm.entity.person.fact.BDMPersonFactory;
import curam.ca.gc.bdm.entity.person.intf.BDMPerson;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonDtls;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonKey;
import curam.ca.gc.bdm.entity.struct.BDMProspectPersonDtls;
import curam.ca.gc.bdm.entity.struct.BDMProspectPersonKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMVoluntaryTaxWithholdEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMVoluntaryTaxWithholdEvidenceVO;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAAddress;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAAddressDetails;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAAlert;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAEmailAddress;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAIdentification;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAPhoneNumber;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAProfile;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAProfileContactDetails;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAProfilePayment;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUAProfileTaxWithholds;
import curam.ca.gc.bdm.rest.bdmcitizenaccountapi.struct.BDMUARegisteredPersonDetails;
import curam.ca.gc.bdm.util.impl.BDMBankAccountDetails;
import curam.ca.gc.bdm.util.impl.BDMBankAccountUtil;
import curam.citizenaccount.configuration.impl.ContactInformationConfig;
import curam.citizenaccount.impl.CitizenAccountHelper;
import curam.citizenworkspace.applications.fact.IntakeAppCWAccLinkFactory;
import curam.citizenworkspace.applications.intf.IntakeAppCWAccLink;
import curam.citizenworkspace.applications.struct.IntakeAppCWAccLinkDtls;
import curam.citizenworkspace.applications.struct.IntakeAppCWAccLinkDtlsList;
import curam.citizenworkspace.applications.struct.SearchActiveByCWAccountKey;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountInfo;
import curam.citizenworkspace.security.impl.CitizenWorkspaceAccountManager;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.DESTINATIONTYPECODE;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASESTATUSEntry;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.codetable.impl.EMAILTYPEEntry;
import curam.codetable.impl.GENDEREntry;
import curam.codetable.impl.INTAKEMARITALSTATUSEntry;
import curam.codetable.impl.MARITALSTATUSEntry;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.codetable.impl.PHONETIMEEntry;
import curam.codetable.impl.PHONETYPEEntry;
import curam.codetable.impl.RECORDSTATUSEntry;
import curam.core.facade.fact.CaseHeaderFactory;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.facade.fact.IntegratedCaseFactory;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.fact.ProspectPersonFactory;
import curam.core.facade.intf.CaseHeader;
import curam.core.facade.intf.IntegratedCase;
import curam.core.facade.intf.ProspectPerson;
import curam.core.facade.struct.CaseHeaderDetails;
import curam.core.facade.struct.CaseIDDetails;
import curam.core.facade.struct.ICProductDeliveryDetails;
import curam.core.facade.struct.IntegratedCaseIDKey;
import curam.core.facade.struct.ListICProductDeliveryDetails;
import curam.core.facade.struct.ReadPersonDetails;
import curam.core.facade.struct.ReadPersonKey;
import curam.core.facade.struct.ReadProspectPersonHomeDetails;
import curam.core.facade.struct.ReadProspectPersonHomeKey;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.ConcernRoleBankAccountFactory;
import curam.core.fact.ConcernRoleImageFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.ConcernRoleAlternateID;
import curam.core.intf.ConcernRoleBankAccount;
import curam.core.intf.ConcernRoleImage;
import curam.core.intf.Person;
import curam.core.struct.AlternateIDReadmultiDtls;
import curam.core.struct.AlternateIDReadmultiDtlsList;
import curam.core.struct.BankAccountRMDtls;
import curam.core.struct.BankAccountRMDtlsList;
import curam.core.struct.ConcernRoleAlternateIDRMKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.core.struct.ConcernRoleImageDtlsList;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.piwrapper.impl.Address;
import curam.piwrapper.impl.AddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;
import curam.workspaceservices.intake.fact.IntakeProgramAppCaseLinkFactory;
import curam.workspaceservices.intake.fact.IntakeProgramApplicationFactory;
import curam.workspaceservices.intake.intf.IntakeProgramAppCaseLink;
import curam.workspaceservices.intake.intf.IntakeProgramApplication;
import curam.workspaceservices.intake.struct.IntakeApplicationKey;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtls;
import curam.workspaceservices.intake.struct.IntakeProgramAppCaseLinkDtlsList;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationDtlsList;
import curam.workspaceservices.intake.struct.IntakeProgramApplicationKey;
import java.util.ArrayList;
import java.util.List;

public class BDMCitizenAccountAPI implements
  curam.ca.gc.bdm.rest.bdmcitizenaccountapi.intf.BDMCitizenAccountAPI {

  public static final String PHOTO_URL = "/ua/profile_image/";

  @Inject
  private AddressDAO addressDAO;

  @Inject
  CaseHeaderDAO caseHeaderDAO;

  @Inject
  private CitizenAccountHelper citizenAccountHelper;

  @Inject
  private CitizenWorkspaceAccountManager citizenWorkspaceAccountManager;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private ContactInformationConfig contactInformationConfig;

  @Inject
  BDMContactPreferenceEvidence bdmContactPreferenceEvidence;

  @Inject
  BDMLifeEventCustomProcessorUtil bdmLifeEventCustomProcessorUtil;

  @Inject
  BDMEmailEvidence bdmEmailEvidence;

  @Inject
  BDMPhoneEvidence bdmPhoneEvidence;

  @Inject
  BDMNamesEvidence bdmNamesEvidence;

  @Inject
  BDMVoluntaryTaxWithholdEvidence bdmVoluntaryTaxWithholdEvidence;

  public BDMCitizenAccountAPI() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Reads the bdm Profile information for display on the UA
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMUAProfile readProfile()
    throws AppException, InformationalException {

    final CitizenWorkspaceAccountInfo citizenWorkspaceAccountInfo =
      this.citizenWorkspaceAccountManager.getLoggedInUserCWAccountInfo();
    final BDMUAProfile profile = new BDMUAProfile();
    ReadPersonDetails personDetails = new ReadPersonDetails();
    final ProspectPerson prospectPersonObj =
      ProspectPersonFactory.newInstance();

    long concernRoleID = 0l;
    if (!citizenWorkspaceAccountInfo.isLinkedToInternalCuramSystem()
      .booleanValue()) {

      concernRoleID =
        readProspectPersonConcernRole(citizenWorkspaceAccountInfo);
      if (concernRoleID == 0) {
        // Account for the case that no application has been submitted
        profile.fullName = citizenWorkspaceAccountInfo.getUserFullName();
        return profile;
      }

      profile.person_id = concernRoleID;

      final ReadProspectPersonHomeKey readProspectPersonHomeKey =
        new ReadProspectPersonHomeKey();
      readProspectPersonHomeKey.concernRoleHomePageKey.concernRoleID =
        concernRoleID;
      final ReadProspectPersonHomeDetails readProspectPersonHomeDetails =
        prospectPersonObj.readHomePageDetails(readProspectPersonHomeKey);
      final String middleName = getOtherName(concernRoleID);
      if (middleName != null) {
        profile.fullName =
          readProspectPersonHomeDetails.prospectPersonHomeDetails.firstForename
            + " " + middleName + " "
            + readProspectPersonHomeDetails.prospectPersonHomeDetails.surname;
      } else {
        profile.fullName =
          readProspectPersonHomeDetails.prospectPersonHomeDetails.firstForename
            + " "
            + readProspectPersonHomeDetails.prospectPersonHomeDetails.surname;
      }
      profile.dateOfBirth =
        readProspectPersonHomeDetails.prospectPersonHomeDetails.dateOfBirth;
      profile.marital_status_code = BDMUtil
        .getCodeTableDescriptionForUserLocale(MARITALSTATUSEntry.TABLENAME,
          readProspectPersonHomeDetails.prospectPersonHomeDetails.maritalStatusCode);
      profile.gender =
        BDMUtil.getCodeTableDescriptionForUserLocale(GENDEREntry.TABLENAME,
          readProspectPersonHomeDetails.prospectPersonHomeDetails.gender);
      profile.last_name_at_birth =
        readProspectPersonHomeDetails.prospectPersonHomeDetails.personBirthName;
      profile.parent_last_name =
        readProspectPersonHomeDetails.prospectPersonHomeDetails.motherBirthSurname;
      profile.pref_name = getPreferredName(concernRoleID);
    } else {
      final ConcernRole concernRole =
        this.citizenAccountHelper.getLoggedInConcernRole();
      concernRoleID = concernRole.getID().longValue();
      final ReadPersonKey readPersonKey = new ReadPersonKey();
      readPersonKey.maintainConcernRoleKey.concernRoleID = concernRoleID;
      personDetails = PersonFactory.newInstance().readPerson(readPersonKey);
      if (personDetails.personFurtherDetails.otherForename != null) {
        profile.fullName =
          personDetails.personFurtherDetails.firstForename.concat(" ")
            .concat(personDetails.personFurtherDetails.otherForename)
            .concat(" ").concat(personDetails.personFurtherDetails.surname);
      } else {
        profile.fullName = personDetails.personFurtherDetails.firstForename
          .concat(" ").concat(personDetails.personFurtherDetails.surname);
      }
      profile.person_id = concernRoleID;
      profile.dateOfBirth = personDetails.personFurtherDetails.dateOfBirth;
      profile.last_name_at_birth =
        personDetails.personFurtherDetails.birthName;
      profile.parent_last_name =
        personDetails.personFurtherDetails.motherBirthSurname;
      profile.pref_name = getPreferredName(concernRoleID);
    }

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    // Get gender and marital status from personDtls
    final Person personObj = curam.core.fact.PersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = concernRoleID;
    final PersonDtls personDtls =
      personObj.read(notFoundIndicator, personKey);
    if (!notFoundIndicator.isNotFound()) {
      profile.gender =
        GENDEREntry.get(personDtls.gender).toUserLocaleString();
      profile.marital_status_code = INTAKEMARITALSTATUSEntry
        .get(personDtls.maritalStatusCode).toUserLocaleString();
    }

    // Get Education, Minority, and Indigenous indicators from bdmPersonDtls
    // Look in BDM Prospect Person for Prospect People
    if (!citizenWorkspaceAccountInfo.isLinkedToInternalCuramSystem()
      .booleanValue()) {
      // Look in BDM Prospect Person for Prospect People
      final BDMProspectPerson bdmProspectPersonObj =
        BDMProspectPersonFactory.newInstance();
      final BDMProspectPersonKey bdmProspectPersonKey =
        new BDMProspectPersonKey();
      bdmProspectPersonKey.concernRoleID = concernRoleID;
      final BDMProspectPersonDtls bdmProspectPersonDtls =
        bdmProspectPersonObj.read(notFoundIndicator, bdmProspectPersonKey);
      if (!notFoundIndicator.isNotFound()) {
        profile.education_level =
          BDMUtil.getCodeTableDescriptionForUserLocale(
            BDMEDUCATIONLEVELEntry.TABLENAME,
            bdmProspectPersonDtls.educationLevel);
        profile.minority_person_ind = BDMIEGYESNOOPTIONALEntry
          .get(bdmProspectPersonDtls.memberMinorityGrpType)
          .toUserLocaleString();
        profile.indigenous_person_ind = BDMIEGYESNOOPTIONALEntry
          .get(bdmProspectPersonDtls.indigenousPersonType)
          .toUserLocaleString();
      }
    } else {
      // Look in BDMPerson for Registered Person
      final BDMPerson bdmPersonObj = BDMPersonFactory.newInstance();
      final BDMPersonKey bdmPersonKey = new BDMPersonKey();
      bdmPersonKey.concernRoleID = concernRoleID;
      final BDMPersonDtls bdmPersonDtls =
        bdmPersonObj.read(notFoundIndicator, bdmPersonKey);
      if (!notFoundIndicator.isNotFound()) {
        profile.education_level =
          BDMUtil.getCodeTableDescriptionForUserLocale(
            BDMEDUCATIONLEVELEntry.TABLENAME, bdmPersonDtls.educationLevel);
        profile.minority_person_ind = BDMIEGYESNOOPTIONALEntry
          .get(bdmPersonDtls.memberMinorityGrpType).toUserLocaleString();
        profile.indigenous_person_ind = BDMIEGYESNOOPTIONALEntry
          .get(bdmPersonDtls.indigenousPersonType).toUserLocaleString();
      }
    }

    // Get photo
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    final ConcernRoleImage concernRoleImage =
      ConcernRoleImageFactory.newInstance();
    concernRoleKey.concernRoleID = concernRoleID;
    final ConcernRoleImageDtlsList concernRoleImageDtlsList =
      concernRoleImage.searchLatestImageByConcernRoleID(concernRoleKey);
    if (!concernRoleImageDtlsList.dtls.isEmpty())
      profile.photo = "/ua/profile_image/"
        + concernRoleImageDtlsList.dtls.get(0).concernRoleImageID;

    // Add Identifications
    for (final BDMUAIdentification identification : getIdentification(
      concernRoleID)) {
      profile.registeredPersonDetails.identifications.addRef(identification);
    }

    // Add profilePayment to registeredPersonDetails
    profile.registeredPersonDetails.paymentDetails
      .addAll(getProfilePaymentDetails(concernRoleID));

    // Add Alert
    final List<BDMContactPreferenceEvidenceVO> bdmContactPreferenceEvidenceVOList =
      bdmContactPreferenceEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmContactPreferenceEvidenceVOList.isEmpty()) {
      final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
        bdmEmailEvidence.getEvidenceValueObject(concernRoleID);
      final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceVOList =
        bdmPhoneEvidence.getEvidenceValueObject(concernRoleID);
      String alertFreq = "";
      Boolean recieveAlert = null;
      for (final BDMEmailEvidenceVO bdmEmailEvidenceV0 : bdmEmailEvidenceVOList) {
        if (bdmEmailEvidenceV0.isUseForAlertsInd()) {
          alertFreq = bdmEmailEvidenceV0.getAlertFrequency();
          recieveAlert = true;
        } else {
          recieveAlert = false;
        }
      }
      for (final BDMPhoneEvidenceVO bdmPhoneEvidenceV0 : bdmPhoneEvidenceVOList) {
        if (bdmPhoneEvidenceV0.isUseForAlertsInd()) {
          alertFreq = bdmPhoneEvidenceV0.getAlertFrequency();
          recieveAlert = true;
        } else {
          recieveAlert = false;
        }
      }
      profile.registeredPersonDetails.alerts.addRef(getAlertDetails(
        bdmContactPreferenceEvidenceVOList.get(0), alertFreq, recieveAlert));
    }

    // Add addresses, phones, and emails
    if (this.contactInformationConfig.showClientDetails().booleanValue()) {
      final BDMUAProfileContactDetails profileContactDetails =
        getProfileContactDetails(concernRoleID);
      profile.registeredPersonDetails.addresses
        .addAll(profileContactDetails.addresses);
      profile.registeredPersonDetails.phoneNumbers
        .addAll(profileContactDetails.phoneNumbers);
      profile.registeredPersonDetails.emailAddresses
        .addAll(profileContactDetails.emailAddresses);
    }

    // Add voluntary tax withholds
    try {
      profile.registeredPersonDetails.profileTaxWithholds
        .addAll(getProfileTaxWithholds(concernRoleID).profileTaxWithholds);
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "Review voluntary tax withholds to add to person profile");
    }
    return profile;
  }

  /**
   * Gets the ConcernRoleID for a Prospect Person
   *
   * @param CitizenWorkspaceAccountInfo
   * @throws AppException
   * @throws InformationalException
   */
  private long readProspectPersonConcernRole(
    final CitizenWorkspaceAccountInfo citizenWorkspaceAccountInfo)
    throws InformationalException, AppException {

    long concernRoleID = 0l;
    final long accountID =
      citizenWorkspaceAccountInfo.getCitizenWorkspaceAccountID();

    final IntakeAppCWAccLink intakeAppCWAccLink =
      IntakeAppCWAccLinkFactory.newInstance();
    final SearchActiveByCWAccountKey searchActiveByCWAccountKey =
      new SearchActiveByCWAccountKey();
    searchActiveByCWAccountKey.citizenWorkspaceAccountID = accountID;
    searchActiveByCWAccountKey.recordStatus =
      RECORDSTATUSEntry.NORMAL.getCode();
    final IntakeAppCWAccLinkDtlsList intakeAppCWAccLinkDtlsList =
      intakeAppCWAccLink.searchActiveByCWAccount(searchActiveByCWAccountKey);

    if (!intakeAppCWAccLinkDtlsList.dtls.isEmpty()) {
      IntakeAppCWAccLinkDtls intakeAppCWAccLinkDtls =
        new IntakeAppCWAccLinkDtls();
      intakeAppCWAccLinkDtls = intakeAppCWAccLinkDtlsList.dtls.get(0);

      final IntakeApplicationKey intakeApplicationKey =
        new IntakeApplicationKey();
      intakeApplicationKey.intakeApplicationID =
        intakeAppCWAccLinkDtls.intakeApplicationID;

      final IntakeProgramApplication intakeProgramApplicationObj =
        IntakeProgramApplicationFactory.newInstance();

      final IntakeProgramApplicationDtlsList intakeProgramApplicationDtlsList =
        intakeProgramApplicationObj
          .searchByIntakeApplication(intakeApplicationKey);

      if (!intakeProgramApplicationDtlsList.dtls.isEmpty()) {
        final IntakeProgramAppCaseLink intakeProgramAppCaseLinkObj =
          IntakeProgramAppCaseLinkFactory.newInstance();
        final IntakeProgramApplicationKey intakeProgramApplicationKey =
          new IntakeProgramApplicationKey();

        intakeProgramApplicationKey.intakeProgramApplicationID =
          intakeProgramApplicationDtlsList.dtls
            .get(0).intakeProgramApplicationID;
        final IntakeProgramAppCaseLinkDtlsList intakeProgramAppCaseLinkDtlsList =
          intakeProgramAppCaseLinkObj
            .searchByIntakeProgramApplicationID(intakeProgramApplicationKey);

        for (final IntakeProgramAppCaseLinkDtls intakeProgramAppCaseLinkDtls : intakeProgramAppCaseLinkDtlsList.dtls) {
          final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();

          final CaseIDDetails caseIDDetails = new CaseIDDetails();
          caseIDDetails.caseID = intakeProgramAppCaseLinkDtls.caseID;
          final CaseHeaderDetails caseHeaderDetails =
            caseHeaderObj.readCaseHeader(caseIDDetails);

          final curam.core.facade.intf.ConcernRole concernRole =
            ConcernRoleFactory.newInstance();

          final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
          concernRoleKey.concernRoleID = caseHeaderDetails.dtls.concernRoleID;
          final ConcernRoleDtls concernRoleDtls =
            concernRole.readConcernRole(concernRoleKey);

          if (concernRoleDtls.concernRoleType
            .equals(CONCERNROLETYPEEntry.PROSPECTPERSON.getCode())) {
            concernRoleID = concernRoleDtls.concernRoleID;
          }
        }
      }
    }
    return concernRoleID;
  }

  /**
   * Gets the IDs for the Profile
   *
   * @param ConcernRole
   * @throws AppException
   * @throws InformationalException
   */
  private ArrayList<BDMUAIdentification> getIdentification(
    final long concernRoleID) throws AppException, InformationalException {

    final ArrayList<BDMUAIdentification> identificationList =
      new ArrayList<>();
    final ConcernRoleAlternateID concerntRoleAlternateIDObj =
      ConcernRoleAlternateIDFactory.newInstance();
    final ConcernRoleAlternateIDRMKey key = new ConcernRoleAlternateIDRMKey();
    key.concernRoleID = concernRoleID;
    final AlternateIDReadmultiDtlsList alternateIDList =
      concerntRoleAlternateIDObj.searchByConcernRole(key);
    if (null != alternateIDList.dtls)
      for (final AlternateIDReadmultiDtls alternateID : alternateIDList.dtls) {
        final BDMUAIdentification identification = new BDMUAIdentification();
        if (!alternateID.endDate.after(Date.getDate(BDMConstants.kZeroDate))
          || alternateID.endDate == null) {
          identification.identificationType = alternateID.typeCode;
          identification.identificationNumber =
            "*****" + alternateID.alternateID.substring(5);
          identificationList.add(identification);
        }

      }
    return identificationList;
  }

  /**
   * Gets the Addresses for the Profile
   *
   * @param ConcernRole
   * @throws AppException
   * @throws InformationalException
   */
  private BDMUAAddress getProfileAddressDetails(final ConcernRole concernRole,
    final Address address) throws AppException, InformationalException {

    final BDMUAAddress profileAddress = new BDMUAAddress();
    profileAddress.address_id = address.getID().longValue();
    profileAddress.address = address.getOneLineAddressString();
    profileAddress.type = address.getAddressType(concernRole).getCode();
    final StringBuilder addressJSON = new StringBuilder();
    final String addressData = address.getAddressData();
    if (null != addressData) {
      addressJSON.append("{ ");
      final String[] addressDataLines = addressData.split("\n");
      for (int i = 0; i < addressDataLines.length; i++) {
        final String currentLine = addressDataLines[i];
        if (currentLine.contains("=")) {
          final String[] pairValue = currentLine.split("=");
          addressJSON.append("\"" + pairValue[0] + "\"");
          addressJSON.append(" : ");
          if (pairValue.length > 1) {
            addressJSON.append("\"" + pairValue[1] + "\"");
          } else {
            addressJSON.append("\"\"");
          }
          if (i < addressDataLines.length - 1)
            addressJSON.append(", ");
        }
      }
      addressJSON.append(" }");
    }
    final BDMUAAddressDetails addressDetails = new BDMUAAddressDetails();
    addressDetails.addressLayoutType =
      Configuration.getProperty(EnvVars.ENV_ADDRESS_LAYOUT);
    addressDetails.addressJSON = addressJSON.toString();
    profileAddress.addressDetails = addressDetails;

    // address.
    return profileAddress;
  }

  /**
   * Adds the addresses, emails, and phones to the Profile
   *
   * @param ConcernRole
   * @throws AppException
   * @throws InformationalException
   */
  protected BDMUAProfileContactDetails getProfileContactDetails(
    final long concernRoleID) throws AppException, InformationalException {

    final BDMUAProfileContactDetails profileContactDetails =
      new BDMUAProfileContactDetails();
    // Add Addresses
    final ConcernRole concernRole = concernRoleDAO.get(concernRoleID);

    final List<Address> addressList =
      this.addressDAO.listActiveAddressesByConcernRole(concernRole);
    for (final Address address : addressList)
      profileContactDetails.addresses
        .addRef(getProfileAddressDetails(concernRole, address));
    // Add Emails
    final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
      bdmEmailEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmEmailEvidenceVOList.isEmpty()) {
      for (final BDMEmailEvidenceVO bdmEmailEvidenceVO : bdmEmailEvidenceVOList) {
        profileContactDetails.emailAddresses
          .addRef(getProfileEmailAddressDetails(bdmEmailEvidenceVO));
      }
    }
    // Add Phones
    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceVOList =
      bdmPhoneEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmPhoneEvidenceVOList.isEmpty()) {
      for (final BDMPhoneEvidenceVO bdmPhoneEvidenceVO : bdmPhoneEvidenceVOList) {
        profileContactDetails.phoneNumbers
          .addRef(getProfilePhoneNumberDetails(bdmPhoneEvidenceVO));
      }
    }
    return profileContactDetails;
  }

  /**
   * Gets the emails for the Profile
   *
   * @param BDMEmailEvidenceVO
   * @throws AppException
   * @throws InformationalException
   */
  private BDMUAEmailAddress
    getProfileEmailAddressDetails(final BDMEmailEvidenceVO bdmEmailEvidenceVO)
      throws AppException, InformationalException {

    final BDMUAEmailAddress emailAddress = new BDMUAEmailAddress();
    emailAddress.email_address_id = bdmEmailEvidenceVO.getEvidenceID();
    emailAddress.email = bdmEmailEvidenceVO.getEmailAddress();
    emailAddress.preferred_ind = bdmEmailEvidenceVO.isPreferredInd();
    emailAddress.type =
      EMAILTYPEEntry.get(bdmEmailEvidenceVO.getEmailAddressType()).getCode();
    emailAddress.use_for_alerts_ind = bdmEmailEvidenceVO.isUseForAlertsInd();

    return emailAddress;
  }

  /**
   * Gets the phones for the Profile
   *
   * @param BDMPhoneEvidenceVO
   * @throws AppException
   * @throws InformationalException
   */
  private BDMUAPhoneNumber
    getProfilePhoneNumberDetails(final BDMPhoneEvidenceVO bdmPhoneEvidenceVO)
      throws AppException, InformationalException {

    final BDMUAPhoneNumber phoneNumber = new BDMUAPhoneNumber();
    phoneNumber.phone_number_id = bdmPhoneEvidenceVO.getEvidenceID();
    phoneNumber.areaCode = bdmPhoneEvidenceVO.getPhoneAreaCode();
    phoneNumber.countryCode =
      BDMPHONECOUNTRYEntry.get(bdmPhoneEvidenceVO.getPhoneCountryCode())
        .toUserLocaleString().replaceAll("[^0-9.-]", "");
    phoneNumber.number = bdmPhoneEvidenceVO.getPhoneNumber();
    phoneNumber.type =
      PHONETYPEEntry.get(bdmPhoneEvidenceVO.getPhoneType()).getCode();
    phoneNumber.extension = bdmPhoneEvidenceVO.getPhoneExtension();
    phoneNumber.afternoon_ind = bdmPhoneEvidenceVO.isAfternoonInd()
      ? PHONETIMEEntry.AFTERNOONS.toUserLocaleString() : "";
    phoneNumber.evening_ind = bdmPhoneEvidenceVO.isEveningInd()
      ? PHONETIMEEntry.EVENINGS.toUserLocaleString() : "";
    phoneNumber.morning_ind = bdmPhoneEvidenceVO.isMorningInd()
      ? PHONETIMEEntry.MORNINGS.toUserLocaleString() : "";
    phoneNumber.preferred_ind = bdmPhoneEvidenceVO.isPreferredInd();
    phoneNumber.use_for_alerts_ind = bdmPhoneEvidenceVO.isUseForAlertsInd();
    return phoneNumber;
  }

  /**
   * Gets the payment info for the Profile
   *
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  private ArrayList<BDMUAProfilePayment> getProfilePaymentDetails(
    final long concernRoleID) throws AppException, InformationalException {

    final ArrayList<BDMUAProfilePayment> profilePaymentList =
      new ArrayList<BDMUAProfilePayment>();
    final IntegratedCase integratedcase = IntegratedCaseFactory.newInstance();
    IntegratedCaseIDKey integratedCaseIDKey = null;
    final BDMCodeTableCombo bdmCodeTableComboObj =
      BDMCodeTableComboFactory.newInstance();
    final ConcernRoleBankAccount concernRoleBankAccountObj =
      ConcernRoleBankAccountFactory.newInstance();

    final ConcernRole primaryConcernRole =
      this.concernRoleDAO.get(Long.valueOf(concernRoleID));
    final List<curam.piwrapper.caseheader.impl.CaseHeader> clientCaseList =
      this.caseHeaderDAO.searchByParticipant(primaryConcernRole);

    for (final curam.piwrapper.caseheader.impl.CaseHeader caseDetails : clientCaseList) {

      if (caseDetails.getCaseType().equals(CASETYPECODEEntry.INTEGRATEDCASE)
        && !caseDetails.getStatus().equals(CASESTATUSEntry.CLOSED)) {
        boolean existsActiveBenefts = false;
        integratedCaseIDKey = new IntegratedCaseIDKey();
        integratedCaseIDKey.caseID = caseDetails.getID();
        final ListICProductDeliveryDetails listICProductDeliveryDetails =
          integratedcase.listProduct(integratedCaseIDKey);
        for (final ICProductDeliveryDetails icProductDeliveryDetails : listICProductDeliveryDetails.dtls) {
          if (icProductDeliveryDetails.statusCode.equals(CASESTATUS.ACTIVE)) {
            existsActiveBenefts = true;
            break;
          }
        }

        if (existsActiveBenefts) {
          final BDMUAProfilePayment profilePayment =
            new BDMUAProfilePayment();
          profilePayment.payment_method =
            METHODOFDELIVERYEntry.CHEQUE.toUserLocaleString();
          profilePayment.account_program_name = caseDetails
            .getAdminCaseConfiguration().getCaseConfigurationName();
          final BDMReadGovernCodeBySubOrdTableKey bdmReadGovernCodeBySubOrdTableKey =
            new BDMReadGovernCodeBySubOrdTableKey();
          bdmReadGovernCodeBySubOrdTableKey.governTableName =
            BDMBENEFITPROGRAMTYPE.TABLENAME;
          bdmReadGovernCodeBySubOrdTableKey.recordStatusCode =
            RECORDSTATUS.NORMAL;
          bdmReadGovernCodeBySubOrdTableKey.subOrdTableName =
            PRODUCTCATEGORY.TABLENAME;
          bdmReadGovernCodeBySubOrdTableKey.subOrdCode =
            caseDetails.getIntegratedCaseType().getCode();
          final BDMReadGovernCodeBySubOrdTableDetails bdmReadGovernCodeBySubOrdTableDetails =
            bdmCodeTableComboObj
              .readGovernCodeBySubOrdTable(bdmReadGovernCodeBySubOrdTableKey);
          // look for payment method and account info for this program by
          // the associated BDMPaymentDestination records
          final long concernRoleBankAccountID =
            getCurrentBankAccountIDForProgram(concernRoleID,
              bdmReadGovernCodeBySubOrdTableDetails.governCode);
          if (concernRoleBankAccountID != 0) {

            final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
              new ConcernRoleIDStatusCodeKey();
            concernRoleIDStatusCodeKey.concernRoleID = concernRoleID;
            concernRoleIDStatusCodeKey.statusCode = RECORDSTATUS.NORMAL;
            final BankAccountRMDtlsList bankAccountRMDtlsList =
              concernRoleBankAccountObj
                .searchByConcernRoleIDAndStatus(concernRoleIDStatusCodeKey);

            for (final BankAccountRMDtls bankAccountRMDtls : bankAccountRMDtlsList.dtls) {
              if (bankAccountRMDtls.concernRoleBankAccountID == concernRoleBankAccountID) {

                profilePayment.account_nick_name = bankAccountRMDtls.name;
                profilePayment.account_number =
                  bankAccountRMDtls.accountNumber;
                profilePayment.payment_method =
                  METHODOFDELIVERYEntry.EFT.toUserLocaleString();
                profilePayment.sort_code = bankAccountRMDtls.bankSortCode;
                // Call utility to break bankSortCode into two fields.
                final BDMBankAccountDetails sortCodeDetails =
                  BDMBankAccountUtil.getInstitutionNumberAndTransitNumber(
                    bankAccountRMDtls.bankSortCode);
                profilePayment.account_inst_number =
                  sortCodeDetails.getInstitutionNumber();
                profilePayment.account_transit_number =
                  sortCodeDetails.getTransitNumber();
                break;
              }
            }
          }
          profilePaymentList.add(profilePayment);
        }
      }
    }
    return profilePaymentList;

  }

  private long getCurrentBankAccountIDForProgram(final long concernRoleID,
    final String programTypeCode)
    throws AppException, InformationalException {

    final BDMPaymentDestination bdmPaymentDestinationObj =
      BDMPaymentDestinationFactory.newInstance();
    final BDMSearchEFTDestinationKey searchEFTDestinationKey =
      new BDMSearchEFTDestinationKey();
    searchEFTDestinationKey.concernRoleID = concernRoleID;
    searchEFTDestinationKey.destinationType = DESTINATIONTYPECODE.BANKACCOUNT;
    searchEFTDestinationKey.recordStatusCode = RECORDSTATUS.NORMAL;
    final BDMSearchEFTDestinationDetailsList paymentDestinationList =
      bdmPaymentDestinationObj
        .searchEFTPaymentDestination(searchEFTDestinationKey);
    for (final BDMSearchEFTDestinationDetails paymentDestination : paymentDestinationList.dtls) {
      if (paymentDestination.programType.equals(programTypeCode)
        && !paymentDestination.startDate.after(Date.getCurrentDate())
        && paymentDestination.endDate.isZero()
        || paymentDestination.endDate.after(Date.getCurrentDate())) {
        return paymentDestination.destinationID;
      }
    }
    return 0;
  }

  /**
   * Gets the Alert info for the Profile
   *
   * @param BDMContactPreferenceEvidenceVO
   * @throws AppException
   * @throws InformationalException
   */
  private BDMUAAlert getAlertDetails(
    final BDMContactPreferenceEvidenceVO bdmContactPreferenceEvidenceVO,
    final String alertFreq, final Boolean recieveAlert)
    throws AppException, InformationalException {

    final BDMUAAlert alert = new BDMUAAlert();
    alert.pref_comm_method = BDMCORRESDELIVERYEntry
      .get(bdmContactPreferenceEvidenceVO.getPreferredCommunication())
      .toUserLocaleString();
    alert.pref_oral_language = BDMLANGUAGEEntry
      .get(bdmContactPreferenceEvidenceVO.getPreferredOralLanguage())
      .toUserLocaleString();
    alert.pref_written_language = BDMLANGUAGEEntry
      .get(bdmContactPreferenceEvidenceVO.getPreferredWrittenLanguage())
      .toUserLocaleString();

    // Gather Alert Info
    if (recieveAlert != null) {
      // A user has selected alert information
      if (recieveAlert) {
        alert.receive_alert = BDMConstants.kYes;
        alert.alert_frequency =
          BDMALERTOCCUREntry.get(alertFreq).toUserLocaleString();
      } else {
        alert.receive_alert = BDMConstants.kNo;
      }
    }

    return alert;
  }

  /**
   * Gets the preferred name info for the Profile
   *
   * @param BDMContactPreferenceEvidenceVO
   * @throws AppException
   * @throws InformationalException
   */
  private String getPreferredName(final long concernRoleID)
    throws AppException, InformationalException {

    final List<BDMNamesEvidenceVO> bdmNamesEvidenceVOList =
      bdmNamesEvidence.getNamesEvidenceValueObject(concernRoleID);

    if (!bdmNamesEvidenceVOList.isEmpty()) {
      for (final BDMNamesEvidenceVO bdmNamesEvidenceVO : bdmNamesEvidenceVOList) {
        if (bdmNamesEvidenceVO.getNameType()
          .equals(ALTERNATENAMETYPE.PREFERRED)) {
          return bdmNamesEvidenceVO.getFirstName();
        }
      }
    }
    return "";
  }

  /**
   * Gets the preferred name info for the Profile
   *
   * @param long concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  private String getOtherName(final long concernRoleID)
    throws AppException, InformationalException {

    final List<BDMNamesEvidenceVO> bdmNamesEvidenceVOList =
      bdmNamesEvidence.getNamesEvidenceValueObject(concernRoleID);

    if (!bdmNamesEvidenceVOList.isEmpty()) {
      for (final BDMNamesEvidenceVO bdmNamesEvidenceVO : bdmNamesEvidenceVOList) {
        return bdmNamesEvidenceVO.getMiddleName();
      }
    }
    return "";
  }

  /**
   * Gets the voluntary tax withhold info for the Profile
   *
   * @param BDMVoluntaryTaxWithholdEvidenceVO
   * @throws AppException
   * @throws InformationalException
   */
  private BDMUARegisteredPersonDetails getProfileTaxWithholds(
    final long concernRoleID) throws AppException, InformationalException {

    final List<BDMVoluntaryTaxWithholdEvidenceVO> bdmVoluntaryTaxWithholdEvidenceVOList =
      bdmVoluntaryTaxWithholdEvidence
        .getVoluntaryTaxWithholdEvidenceValueObjectList(concernRoleID);
    final BDMUARegisteredPersonDetails bdmUARegisteredPersonDetails =
      new BDMUARegisteredPersonDetails();
    if (!bdmVoluntaryTaxWithholdEvidenceVOList.isEmpty()) {
      for (final BDMVoluntaryTaxWithholdEvidenceVO bdmVTWEvidenceVO : bdmVoluntaryTaxWithholdEvidenceVOList) {
        final long icCaseID = BDMUtil.getCaseIDfromCaseParticipantRoleID(
          Long.parseLong(bdmVTWEvidenceVO.getCaseParticipant()));
        if (BDMUtil.integratedCasehasActivePDC(icCaseID)) {
          final BDMUAProfileTaxWithholds taxWithhold =
            new BDMUAProfileTaxWithholds();
          final StringBuffer taxInfoPeriodDesc = new StringBuffer();

          if (bdmVTWEvidenceVO.getAmount() != null) {
            final Double amountNumber =
              Double.valueOf(bdmVTWEvidenceVO.getAmount());
            final String percentage = bdmVTWEvidenceVO.getPercentageValue();

            if (amountNumber != null && amountNumber == 0) {
              // Account for case that withhold is created on SPM side (it
              // comes
              // in as 0.0)

              taxInfoPeriodDesc.append(percentage);
              taxInfoPeriodDesc.append(CuramConst.gkPercentage);
            } else {
              taxInfoPeriodDesc.append(CuramConst.gkDollar);
              final Money amount = new Money(bdmVTWEvidenceVO.getAmount());
              taxInfoPeriodDesc.append(amount.toString());
            }

          } else {
            final String percentage = bdmVTWEvidenceVO.getPercentageValue();
            taxInfoPeriodDesc.append(percentage);
            taxInfoPeriodDesc.append(CuramConst.gkPercentage);
          }

          taxWithhold.deduction = taxInfoPeriodDesc.toString();
          taxWithhold.effective_date =
            bdmVTWEvidenceVO.getStartDate().toString();
          taxWithhold.end_date = bdmVTWEvidenceVO.getEndDate() == null ? ""
            : bdmVTWEvidenceVO.getEndDate().toString();
          taxWithhold.tax_withhold_id =
            String.valueOf(bdmVTWEvidenceVO.getEvidenceID());
          bdmVTWEvidenceVO.getCaseParticipant();
          taxWithhold.program = this.caseHeaderDAO.get(icCaseID)
            .getIntegratedCaseType().toUserLocaleString();
          // String date =
          if (!bdmVTWEvidenceVO.getVoluntaryTaxWithholdType()
            .equals(BDMVTWTYPEEntry.PROVINCIAL.getCode())) {
            if (taxWithhold.end_date.isEmpty()) {
              bdmUARegisteredPersonDetails.profileTaxWithholds
                .addRef(taxWithhold);
            } else if (!bdmVTWEvidenceVO.getEndDate()
              .before(Date.getCurrentDate())
              || bdmVTWEvidenceVO.getEndDate()
                .equals(Date.getCurrentDate())) {
              bdmUARegisteredPersonDetails.profileTaxWithholds
                .addRef(taxWithhold);
            }
          }

        }
      }
    }
    final ConcernRole concernRole = this.concernRoleDAO.get(concernRoleID);
    try {
      // search for all the programs (integrated case). Only displays programs
      // that have at least one active benefit. Only add the program if the
      // program isnt already in the returned list, ie a program without a tax
      // withhold
      final List<curam.piwrapper.caseheader.impl.CaseHeader> clientCaseList =
        this.caseHeaderDAO.searchByParticipant(concernRole);
      for (final curam.piwrapper.caseheader.impl.CaseHeader caseDetails : clientCaseList) {
        if (caseDetails.getCaseType().equals(CASETYPECODEEntry.INTEGRATEDCASE)
          && !caseDetails.getStatus().equals(CASESTATUSEntry.CLOSED)) {
          final long integratedCaseID = caseDetails.getID();
          if (BDMUtil.integratedCasehasActivePDC(integratedCaseID)) {
            boolean programAlreadyAdded = false;
            final String icProgramName =
              this.caseHeaderDAO.get(integratedCaseID).getIntegratedCaseType()
                .toUserLocaleString();
            for (final BDMUAProfileTaxWithholds profileTaxWithhold : bdmUARegisteredPersonDetails.profileTaxWithholds) {
              if (profileTaxWithhold.program.equals(icProgramName)) {
                programAlreadyAdded = true;
                break;
              }
            }
            if (!programAlreadyAdded) {
              final BDMUAProfileTaxWithholds taxWithhold =
                new BDMUAProfileTaxWithholds();
              taxWithhold.program = this.caseHeaderDAO.get(integratedCaseID)
                .getIntegratedCaseType().toUserLocaleString();
              if (!taxWithhold.deduction.isEmpty()) {
                bdmUARegisteredPersonDetails.profileTaxWithholds
                  .addRef(taxWithhold);
              }

              break;
            }
          }
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return bdmUARegisteredPersonDetails;
  }

}
