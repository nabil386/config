package curam.ca.gc.bdm.facade.externalparty.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMSTATUSOFAGREEMENT;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory;
import curam.ca.gc.bdm.entity.fact.BDMExternalPartyOfficeHistoryFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtlsStruct1;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtlsStruct1List;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.fec.struct.BDMReadByCountryCodeKey;
import curam.ca.gc.bdm.entity.intf.BDMExternalPartyOfficeHistory;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyNameAndTypeKey;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyNameDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyTypeAndAddrKey;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyOfficeHistoryDtls;
import curam.ca.gc.bdm.entity.struct.BDMSSACntryAgrmntDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyHomePageDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyModifyDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyRegistrationDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartySearchKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartySearchWizardKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyTaskDetailsList;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExtlPartyAddrDetailsList;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMReadExternalPartyHistoryKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMReadExternalPartyKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMRegisterExternalPartyState;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMSSACountryDetailsList;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMSSACountryHistoryDetailsList;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMSSACountryViewHistoryDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMEXTERNALPARTY;
import curam.ca.gc.bdm.message.BDMTHIRDPARTYCONTACT;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMEscalationLevelString;
import curam.ca.gc.bdm.sl.bdminbox.struct.CaseUrgentFlagStringDetails;
import curam.ca.gc.bdm.sl.externalparty.fact.BDMMaintainExternalPartyFactory;
import curam.ca.gc.bdm.sl.externalparty.intf.BDMMaintainExternalParty;
import curam.ca.gc.bdm.sl.externalparty.struct.BDMCountryCodeAndNameKey;
import curam.ca.gc.bdm.sl.externalparty.struct.BDMExternalPartyOfficeAddressDetailsList;
import curam.ca.gc.bdm.sl.externalparty.struct.BDMExternalPartyOfficeList;
import curam.ca.gc.bdm.sl.struct.BDMTaskSearchDetails;
import curam.ca.gc.bdm.util.impl.BDMAddressUtil;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRY;
import curam.codetable.COUNTRYCODE;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.codetable.RECORDSTATUS;
import curam.common.util.xml.dom.Element;
import curam.common.util.xml.dom.output.XMLOutputter;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.facade.fact.ExternalPartyFactory;
import curam.core.facade.fact.ParticipantContextFactory;
import curam.core.facade.fact.ParticipantFactory;
import curam.core.facade.intf.ConcernRole;
import curam.core.facade.intf.ExternalParty;
import curam.core.facade.intf.Participant;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.ExternalPartyHomePageDetails;
import curam.core.facade.struct.ExternalPartyRegistrationDetails;
import curam.core.facade.struct.ExternalPartySearchKey1;
import curam.core.facade.struct.ExternalPartyTaskDetailsList;
import curam.core.facade.struct.InformationMsgDtlsList;
import curam.core.facade.struct.ListItemsInCodeTable;
import curam.core.facade.struct.MaintainParticipantAddressDetails;
import curam.core.facade.struct.MenuDataDetails;
import curam.core.facade.struct.ModifiedAddressDetails;
import curam.core.facade.struct.ParticipantContextDescriptionDetails;
import curam.core.facade.struct.ParticipantContextDescriptionKey;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.facade.struct.ParticipantRegistrationWizardSearchDetails;
import curam.core.facade.struct.ParticipantSearchDetails;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.Address;
import curam.core.intf.AddressData;
import curam.core.intf.ConcernRoleAddress;
import curam.core.intf.UniqueID;
import curam.core.sl.entity.fact.ExternalPartyOfficeAddressFactory;
import curam.core.sl.entity.fact.ExternalPartyOfficeMemberFactory;
import curam.core.sl.entity.intf.ExternalPartyOffice;
import curam.core.sl.entity.intf.ExternalPartyOfficeAddress;
import curam.core.sl.entity.struct.ExternalPartyDtls;
import curam.core.sl.entity.struct.ExternalPartyKey;
import curam.core.sl.entity.struct.ExternalPartyOfficeAddressDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeAddressDtlsList;
import curam.core.sl.entity.struct.ExternalPartyOfficeAddressKey;
import curam.core.sl.entity.struct.ExternalPartyOfficeDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeKey;
import curam.core.sl.entity.struct.ExternalPartyOfficeMemberDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeMemberKey;
import curam.core.sl.entity.struct.MaintainExternalPartyOfficeAddressDtls;
import curam.core.sl.entity.struct.ReadExternalPartyHomePageDetails;
import curam.core.sl.entity.struct.TaskKey;
import curam.core.sl.fact.ExternalPartyOfficeFactory;
import curam.core.sl.impl.Constants;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.struct.ExternalPartyDetails;
import curam.core.sl.struct.ExternalPartyOfficeAddressDetails;
import curam.core.sl.struct.ExternalPartyOfficeAddressDetailsList;
import curam.core.sl.struct.ExternalPartyOfficeDetails;
import curam.core.sl.struct.ExternalPartyOfficeList;
import curam.core.sl.struct.ExternalPartyRegistrationIDDetails;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressConcernRoleTypeStatusDtlsList;
import curam.core.struct.AddressConcernRoleTypeStatusKey;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressSearchKey;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.Count;
import curam.core.struct.InformationalMsgDtls;
import curam.core.struct.LayoutKey;
import curam.core.struct.OtherAddressData;
import curam.message.GENERALEXTERNALPARTY;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import static curam.core.sl.infrastructure.impl.UimConst.kExternalPartyHome;
import static curam.core.sl.infrastructure.impl.UimConst.kExternalPartyOfficeHome;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kDesc;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kItem;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kName;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kNavigationMenu;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kPageID;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kParam;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kParamConcernRoleID;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kParamExternalPartyOfficeID;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kType;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kTypeExternalParty;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kTypeExternalPartyOffice;
import static curam.core.sl.infrastructure.impl.XmlMetaDataConst.kValue;

public class BDMExternalParty
  extends curam.ca.gc.bdm.facade.externalparty.base.BDMExternalParty {

  BDMUtil bdmUtil = new BDMUtil();

  public static final int PARSED_ADDRESS_LENGTH = 7;

  public static final int BDM_PARSED_INDEX = 5;

  public BDMExternalParty() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.impl.BDMMaintainSupervisorUsers workspace;

  @Inject
  curam.ca.gc.bdm.sl.organization.supervisor.intf.BDMMaintainSupervisorUsers bdmMaintainSupervisorUsers;

  /**
   * Method to search the external parties.
   *
   * @param External Party search key
   *
   * @return External Party search details.
   */
  @Override
  public ParticipantSearchDetails searchExternalPartyDetails(
    final ExternalPartySearchKey1 searchKey,
    final BDMExternalPartySearchKey bdmSearchKey)
    throws AppException, InformationalException {

    // Validations
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();
    searchKey.key.key.referenceNumber =
      searchKey.key.key.referenceNumber.trim();
    searchKey.key.key.concernRoleName =
      searchKey.key.key.concernRoleName.trim();
    searchKey.key.key.addressDtls.city =
      searchKey.key.key.addressDtls.city.trim();
    searchKey.key.key.addressDtls.addressLine1 =
      searchKey.key.key.addressDtls.addressLine1.trim();
    searchKey.key.key.addressDtls.addressLine2 =
      searchKey.key.key.addressDtls.addressLine2.trim();
    bdmSearchKey.stateProvince = bdmSearchKey.stateProvince.trim();
    bdmSearchKey.postalCode = bdmSearchKey.postalCode.trim();
    bdmSearchKey.poBox = bdmSearchKey.poBox.trim();

    if (searchKey.key.key.referenceNumber.isEmpty()
      && searchKey.key.key.concernRoleName.isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEXTERNALPARTY.ERR_SEARCH_MANDATORY_MISSING), "",
        InformationalType.kError);
    }

    if (!searchKey.key.key.addressDtls.city.isEmpty()
      && searchKey.key.key.addressDtls.city.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEXTERNALPARTY.ERR_SEARCH_CITY), "",
        InformationalType.kError);
    }

    if (!searchKey.key.key.addressDtls.addressLine1.isEmpty()
      && searchKey.key.key.addressDtls.addressLine1.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_SEARCH_STREET_NUMBER_OR_STREET_ONE),
        "", InformationalType.kError);
    }

    if (!searchKey.key.key.addressDtls.addressLine2.isEmpty()
      && searchKey.key.key.addressDtls.addressLine2.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_SEARCH_STREET_NAME_OR_STREET_TWO),
        "", InformationalType.kError);
    }

    if (!bdmSearchKey.stateProvince.isEmpty()
      && bdmSearchKey.stateProvince.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEXTERNALPARTY.ERR_SEARCH_PROVINCE), "",
        InformationalType.kError);
    }

    if (!bdmSearchKey.postalCode.isEmpty()
      && bdmSearchKey.postalCode.length() < 3) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEXTERNALPARTY.ERR_SEARCH_POSTALCODE), "",
        InformationalType.kError);
    }

    if (!bdmSearchKey.poBox.isEmpty() && bdmSearchKey.poBox.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEXTERNALPARTY.ERR_SEARCH_PO_RESIDENTIAL), "",
        InformationalType.kError);
    }

    informationalManager.failOperation();

    final BDMMaintainExternalParty maintainExternalPartyObj =
      BDMMaintainExternalPartyFactory.newInstance();

    final ParticipantSearchDetails participantSearchDetails =
      maintainExternalPartyObj.searchExternalPartyDetails(searchKey,
        bdmSearchKey);

    return participantSearchDetails;
  }

  /**
   * Get the External Party Search Criteria details.
   *
   * @param wizardStateID The identifier for the serialized wizard persistence
   * struct.
   *
   * @return ParticipantSearchWizardKey
   */
  @Override
  public BDMExternalPartySearchWizardKey
    getRegisterExternalPartySearchCriteria(final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    BDMExternalPartySearchWizardKey externalPartySearchWizardKey =
      new BDMExternalPartySearchWizardKey();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    // If the wizard state is specified then read the record, otherwise
    // create a wizard record.
    if (wizardStateID.wizardStateID != 0) {

      final BDMRegisterExternalPartyState registerExternalPartyState =
        (BDMRegisterExternalPartyState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      externalPartySearchWizardKey = registerExternalPartyState.searchKey;
      externalPartySearchWizardKey.key.stateID.wizardStateID =
        wizardStateID.wizardStateID;

      externalPartySearchWizardKey.key.addressData = getAddressDataFromFields(
        externalPartySearchWizardKey.key.searchKey.key.key.addressDtls).addressData;
    } else {
      externalPartySearchWizardKey.key.stateID.wizardStateID =
        wizardPersistentState.create(new BDMRegisterExternalPartyState());
    }

    return externalPartySearchWizardKey;
  }

  /**
   * Utility method to parse the address fields into an AddressData string.
   *
   * @param AddressSearchKey the address elements used in the search
   *
   * @return OtherAddressData A formatted string representation of an address.
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  protected OtherAddressData
    getAddressDataFromFields(final AddressSearchKey addressSearchKey)
      throws AppException, InformationalException {

    OtherAddressData otherAddressData = new OtherAddressData();
    final AddressData addressDataObj = AddressDataFactory.newInstance();
    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();

    final LayoutKey layoutKey = addressDataObj.getLayoutForLocale(null);

    addressFieldDetails.addressLayoutType = layoutKey.addressLayoutType;

    addressFieldDetails.addressLine1 = addressSearchKey.addressLine1;
    addressFieldDetails.addressLine2 = addressSearchKey.addressLine2;
    addressFieldDetails.city = addressSearchKey.city;
    addressFieldDetails.stateCode = addressSearchKey.state;

    addressFieldDetails.countryCode = COUNTRY.getDefaultCode();

    otherAddressData = addressDataObj.parseFieldsToData(addressFieldDetails);

    return otherAddressData;
  }

  /**
   * Method to read External Party search criteria for the External Party
   * registration.
   *
   * @param registrationDtls, wizardStateID, actionID, type
   *
   * @return External Party Registration details.
   */
  @Override
  public ParticipantRegistrationWizardResult setRegisterExternalPartyDetails(
    final ExternalPartyRegistrationDetails registrationDtls,
    final WizardStateID stateID, final ActionIDProperty actionID,
    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails)
    throws AppException, InformationalException {

    // BEGIN OOTB logic.
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    final WizardStateID wizardStateID = new WizardStateID();

    wizardStateID.assign(stateID);

    final ParticipantRegistrationWizardResult participantRegistrationWizardResult =
      new ParticipantRegistrationWizardResult();

    participantRegistrationWizardResult.wizardStateID = wizardStateID;

    if (actionID.actionIDProperty.equalsIgnoreCase(CuramConst.kSaveAction)) {

      // START : TASK-118752 : Set the BDMUNPARSE INDICATOR for intl address
      registrationDtls.externalPartyRegistrationDetails.addressData =
        bdmUtil.setBDMUnparsedIndForINTLAddress(
          registrationDtls.externalPartyRegistrationDetails.addressData);
      // END TASK-118752

      // validate input details.
      if (registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.type
        .equals(EXTERNALPARTYTYPE.SSACOUNTRY)) {
        this.validateRegisterExternalPartyDetails(registrationDtls,
          bdmextPartyRegDetails);
      }

      final ExternalPartyRegistrationDetails details =
        new ExternalPartyRegistrationDetails();

      details.externalPartyRegistrationDetails =
        registrationDtls.externalPartyRegistrationDetails;

      final ExternalPartyRegistrationIDDetails registrationResult =
        BDMMaintainExternalPartyFactory.newInstance()
          .registerExternalParty(details.externalPartyRegistrationDetails);

      participantRegistrationWizardResult.registrationResult.concernRoleID =
        registrationResult.concernRoleID;

      participantRegistrationWizardResult.registrationResult.alternateID =
        registrationResult.alternateID;
      // OOTB logic END.

      // BDM CUSTOMIZATION START
      if (registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.type
        .equals(EXTERNALPARTYTYPE.SSACOUNTRY)) {
        defaultAddressTypeToMailing(registrationResult);
        // Call to custom external party create.
        final BDMMaintainExternalParty maintainExternalPartyObj =
          BDMMaintainExternalPartyFactory.newInstance();

        final BDMExternalPartyDetails extPartyDetails =
          new BDMExternalPartyDetails();
        extPartyDetails.assign(bdmextPartyRegDetails);

        extPartyDetails.concernRoleID =
          participantRegistrationWizardResult.registrationResult.concernRoleID;

        maintainExternalPartyObj.createExternalParty(registrationDtls,
          extPartyDetails);

        // Link external party with Foreign Engagement Case.
        this.linkSSACountryWithFECase(
          registrationDtls.externalPartyRegistrationDetails.addressData,
          extPartyDetails.concernRoleID);
      }
      // BDM CUSTOMIZATION END
      // Default address to Mailing Address when you create Third Party Record
      else if (registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.type
        .equals(EXTERNALPARTYTYPE.THIRDPARTY)) {
        defaultAddressTypeToMailing(registrationResult);
      }

      wizardPersistentState.remove(stateID.wizardStateID);
    }

    return participantRegistrationWizardResult;
  }

  /*
   * Default address to Mailing Address when you create Third Party Record/SSA
   * Country Record
   *
   */

  private void defaultAddressTypeToMailing(
    final ExternalPartyRegistrationIDDetails registrationResult)
    throws AppException, InformationalException {

    final ConcernRoleAddress concernRoleAddressObj =
      ConcernRoleAddressFactory.newInstance();
    AddressConcernRoleTypeStatusDtlsList addressConcernRoleTypeStatusDtlsList;

    final AddressConcernRoleTypeStatusKey addressConcernRoleTypeStatusKey =
      new AddressConcernRoleTypeStatusKey();
    addressConcernRoleTypeStatusKey.concernRoleID =
      registrationResult.concernRoleID;
    addressConcernRoleTypeStatusKey.typeCode =
      CONCERNROLEADDRESSTYPE.BUSINESS;
    addressConcernRoleTypeStatusKey.statusCode = RECORDSTATUS.NORMAL;

    addressConcernRoleTypeStatusDtlsList = concernRoleAddressObj
      .searchAddressByConcernRoleTypeStatus(addressConcernRoleTypeStatusKey);
    // Assumption at this point is, there is always going to be record.

    final ConcernRoleAddressKey concernRoleAddressKey =
      new ConcernRoleAddressKey();
    concernRoleAddressKey.concernRoleAddressID =
      addressConcernRoleTypeStatusDtlsList.dtls.item(0).concernRoleAddressID;

    final ConcernRoleAddressDtls concernRoleAddressDtls =
      concernRoleAddressObj.read(concernRoleAddressKey);

    concernRoleAddressDtls.typeCode = CONCERNROLEADDRESSTYPE.MAILING;
    concernRoleAddressObj.modify(concernRoleAddressKey,
      concernRoleAddressDtls);

  }

  /**
   * Method to validate External Party details.
   *
   * @param registrationDtls, bdmextPartyRegDetails
   * @return void
   */
  void validateRegisterExternalPartyDetails(
    final ExternalPartyRegistrationDetails registrationDtls,
    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name
        .trim();

    // ‘Centre of Specialization (COS)' must be entered.
    if (StringUtil.isNullOrEmpty(bdmextPartyRegDetails.centerOfSpeclizn)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_CENTER_OF_SPCLZN_MUST_BE_ENTERED),
        "", InformationalType.kError);
    }

    // 'Totalization with 3rd Country’ must be entered.
    if (StringUtil.isNullOrEmpty(bdmextPartyRegDetails.totalizatnCountry)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_TOTALIZATION_WITH_3RD_CNTRY_MUST_BE_ENTERED),
        "", InformationalType.kError);
    }

    // ‘Status of Agreement' must be entered.
    if (StringUtil.isNullOrEmpty(bdmextPartyRegDetails.statusOfAgreement)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_STATUS_OF_AGREEMENT_MUST_BE_ENTERED),
        "", InformationalType.kError);
    }

    // ‘Is Protective Date associated with this Country' must be entered.
    if (StringUtil.isNullOrEmpty(bdmextPartyRegDetails.isProtectiveDate)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_PROTECTIVE_DATE_WITH_CNTRY_MUST_BE_ENTERED),
        "", InformationalType.kError);
    }

    // ‘SSA link' must be entered.
    if (StringUtil.isNullOrEmpty(bdmextPartyRegDetails.ssaLink)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEXTERNALPARTY.ERR_SSA_LINK_MUST_BE_ENTERED), "",
        InformationalType.kError);
    }

    // Fail for business validations.
    informationalManager.failOperation();

    // Canada cannot be selected as a Foreign Country
    final String extPartyName =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name;

    if (extPartyName.toUpperCase()
      .equals(BDMConstants.kCanada.toUpperCase())) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_CANADA_CAN_NOT_BE_SELECTED_AS_A_FOREIGN_COUNTRY),
          "", InformationalType.kError);
    }

    final String countryCode = this.getCountryCodeFromAddressData(
      registrationDtls.externalPartyRegistrationDetails.addressData);

    if (countryCode.equals(COUNTRY.CA)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_CANADA_CAN_NOT_BE_SELECTED_AS_A_FOREIGN_COUNTRY),
          "", InformationalType.kError);
    }

    // ‘External Party Name’ does not match ‘Country’ selected.
    final String countryName =
      CodeTable.getOneItemForUserLocale(COUNTRY.TABLENAME, countryCode);

    if (!extPartyName.equalsIgnoreCase(countryName)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_EXT_PARTY_NAME_DOES_NOT_MATCH_WITH_SELCTD_CNTRY),
          "", InformationalType.kError);
    }

    // Check if external party for the SSA Country already exists.
    final boolean extlPrtyForSSACntryAlreadyExists =
      this.isExtPartyForSSACountryAlreadyExists(countryCode);

    if (extlPrtyForSSACntryAlreadyExists) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_EXT_PARTY_NAME_COUNTRY_ALREADY_EXISTS),
        "", InformationalType.kError);
    }

    // Check if external party with name entered already exists.
    final boolean extlPartyAlreadyExists = this.isExternalPartyAlreadyExists(
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name);

    if (extlPartyAlreadyExists) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_EXT_PARTY_NAME_COUNTRY_ALREADY_EXISTS),
        "", InformationalType.kError);
    }

    // ‘Date of Revised Agreement’ must be after ‘Date of Entry into Force’
    if (!bdmextPartyRegDetails.revisedAgrmntDate.isZero()
      && !bdmextPartyRegDetails.forceEntryDate.isZero()
      && bdmextPartyRegDetails.revisedAgrmntDate
        .before(bdmextPartyRegDetails.forceEntryDate)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_DATE_OF_REVISED_AGREEMENT_MUST_BE_AFTER_DATE_OF_ENTRY_INTO_FORCE),
          "", InformationalType.kError);
    }

    // ‘End Date’ must be after ‘Date of Entry into Force’
    if (!bdmextPartyRegDetails.ssaEndDate.isZero()
      && !bdmextPartyRegDetails.forceEntryDate.isZero()
      && bdmextPartyRegDetails.ssaEndDate
        .before(bdmextPartyRegDetails.forceEntryDate)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_DATE_OF_ENTRY_INTO_FORCE),
          "", InformationalType.kError);
    }

    // ‘End Date’ must be after ‘Date of Revised Agreement’
    if (!bdmextPartyRegDetails.revisedAgrmntDate.isZero()
      && !bdmextPartyRegDetails.ssaEndDate.isZero()
      && bdmextPartyRegDetails.ssaEndDate
        .before(bdmextPartyRegDetails.revisedAgrmntDate)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_THE_DATE_OF_REVISED_AGREEMENT),
          "", InformationalType.kError);
    }

    // ‘End Date’ must be after the ‘Entry with Quebec (QC) Start Date’
    if (!bdmextPartyRegDetails.ssaEndDate.isZero()
      && !bdmextPartyRegDetails.qcEntryStartDate.isZero()
      && bdmextPartyRegDetails.ssaEndDate
        .before(bdmextPartyRegDetails.qcEntryStartDate)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_QUBEC_START_DATE),
        "", InformationalType.kError);
    }

    // ‘Date of Entry into Force’ must remain blank if ‘Status of Agreement’ is
    // ‘Not in Force’
    if (!bdmextPartyRegDetails.forceEntryDate.isZero()
      && bdmextPartyRegDetails.statusOfAgreement
        .equals(BDMSTATUSOFAGREEMENT.NOTINFORCE)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_DATE_OF_ENTRY_INTO_FORCE_CAN_NOT_BE_ENTERED),
        "", InformationalType.kError);
    }

    // ‘Entry with Quebec (QC) Start Date’ cannot be empty if ‘Entry with Quebec
    // (QC) End Date’ is entered
    if (!bdmextPartyRegDetails.qcEntryEndDate.isZero()
      && bdmextPartyRegDetails.qcEntryStartDate.isZero()) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_QUBEC_START_DATE_CAN_NOT_BE_EMPTY_IF_QUBEC_END_DATE_ENTERED),
          "", InformationalType.kError);
    }

    // ‘Entry with Quebec (QC) End Date’ must be after ‘Entry with Quebec (QC)
    // Start Date’
    if (!bdmextPartyRegDetails.qcEntryEndDate.isZero()
      && !bdmextPartyRegDetails.qcEntryStartDate.isZero()
      && bdmextPartyRegDetails.qcEntryEndDate
        .before(bdmextPartyRegDetails.qcEntryStartDate)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_QUBEC_END_DATE_MUST_BE_AFTER_THE_QUBEC_START_DATE),
          "", InformationalType.kError);
    }

    // ‘Totalization with 3rd Country’ cannot be ‘Yes’
    if (bdmextPartyRegDetails.statusOfAgreement
      .equals(BDMSTATUSOFAGREEMENT.INFORMATIONONLY)
      && bdmextPartyRegDetails.totalizatnCountry.equals(BDMYESNO.YES)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_TOTALIZATION_WITH_THIRD_COUNTRY_CAN_NOT_BE_YES),
          "", InformationalType.kError);
    }

    // ‘‘Totalization with 3rd Country’ cannot be blank
    // this is not possible. As there is already check on totalizatnCountry not
    // empty
    /*
     * if (bdmextPartyRegDetails.statusOfAgreement
     * .equals(BDMSTATUSOFAGREEMENT.INFORCE)
     * && StringUtil.isNullOrEmpty(bdmextPartyRegDetails.totalizatnCountry)) {
     * ValidationManagerFactory.getManager()
     * .addInfoMgrExceptionWithLookup(new AppException(
     * BDMEXTERNALPARTY.ERR_TOTALIZATION_WITH_THIRD_COUNTRY_CAN_NOT_BE_BLANK),
     * "", InformationalType.kError);
     * }
     */

    // Fail for business validations.
    informationalManager.failOperation();
  }

  /**
   * Method to validate External Party details.
   *
   * @param registrationDtls, bdmextPartyRegDetails
   * @return void
   */
  private boolean isExternalPartyAlreadyExists(final String extPartyName)
    throws AppException, InformationalException {

    boolean extlPartyAlreadyExists = false;

    final BDMExternalPartyDetailsList extPartyDetailsList =
      this.getExternalPartiesByNameAndType(extPartyName);

    if (extPartyDetailsList.dtls.size() > 0) {
      extlPartyAlreadyExists = true;
    }

    return extlPartyAlreadyExists;
  }

  /**
   * Method to validate External Party details.
   *
   * @param registrationDtls, bdmextPartyRegDetails
   * @return void
   */
  private BDMExternalPartyDetailsList getExternalPartiesByNameAndType(
    final String extPartyName) throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.intf.BDMExternalParty externalPartyObj =
      curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory.newInstance();

    final BDMExtPartyNameAndTypeKey typeAndStatusKey =
      new BDMExtPartyNameAndTypeKey();
    typeAndStatusKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;
    typeAndStatusKey.name = extPartyName.toUpperCase();

    final BDMExternalPartyDetailsList extPartyDetailsList =
      externalPartyObj.searchExternalPartiesByNameAndType(typeAndStatusKey);

    return extPartyDetailsList;

  }

  /**
   * Method to validate External Party details.
   *
   * @param registrationDtls, bdmextPartyRegDetails
   * @return void
   */
  private boolean isExternalPartyAlreadyExists_Modify(
    final String extPartyName, final long concernRoleID)
    throws AppException, InformationalException {

    boolean extlPartyAlreadyExists = false;

    final BDMExternalPartyDetailsList extPartyDetailsList =
      this.getExternalPartiesByNameAndType(extPartyName);

    for (final curam.ca.gc.bdm.entity.struct.BDMExternalPartyDetails details : extPartyDetailsList.dtls) {
      if (details.concernRoleID != concernRoleID) {
        extlPartyAlreadyExists = true;
        break;
      }
    }

    return extlPartyAlreadyExists;
  }

  /**
   * Set the external party details for given external party search wizard.
   *
   * @param participantSearchWizardKey contains external party search key
   * @param wizardStateID contains wizard state Id
   * @param actionIDProperty contains action Id property details
   *
   * @return participant external party details
   */
  @Override
  public ParticipantRegistrationWizardSearchDetails
    setRegisterExternalPartySearchCriteriaDetails(
      final BDMExternalPartySearchWizardKey searchKey,
      final WizardStateID wizardStateID,
      final ActionIDProperty actionIDProperty)
      throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    final WizardStateID wizardStateIDObj = new WizardStateID();

    wizardStateIDObj.assign(wizardStateID);

    final ParticipantRegistrationWizardSearchDetails registrationWizSearchDetails =
      new ParticipantRegistrationWizardSearchDetails();

    registrationWizSearchDetails.wizardStateID = wizardStateID;

    if (CuramConst.kNextPageAction
      .equalsIgnoreCase(actionIDProperty.actionIDProperty)) {

      final BDMRegisterExternalPartyState registerExternalPartyState =
        (BDMRegisterExternalPartyState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      registerExternalPartyState.searchKey = searchKey;
      registerExternalPartyState.searchKey.key.stateID = wizardStateID;

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        registerExternalPartyState);
    } else if (CuramConst.kSearchAction
      .equalsIgnoreCase(actionIDProperty.actionIDProperty)) {
      final BDMExternalPartySearchKey bdmsearchKey =
        new BDMExternalPartySearchKey();
      bdmsearchKey.assign(searchKey);

      registrationWizSearchDetails.searchResult =
        searchExternalPartyDetails(searchKey.key.searchKey, bdmsearchKey);
    } else if (CuramConst.kResetAction
      .equalsIgnoreCase(actionIDProperty.actionIDProperty)) {

      final BDMRegisterExternalPartyState externalPartyWizardState =
        (BDMRegisterExternalPartyState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      externalPartyWizardState.searchKey =
        new BDMExternalPartySearchWizardKey();

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        externalPartyWizardState);
    }
    return registrationWizSearchDetails;
  }

  /**
   * Method to read external party details.
   *
   * @param External party details.
   *
   * @return void
   */
  @Override
  public BDMExternalPartyHomePageDetails
    readExternalPartyHomePageDetails(final ExternalPartyKey externalPartyKey)
      throws AppException, InformationalException {

    final BDMExternalPartyHomePageDetails bdmExternalPartyHomePageDetails =
      new BDMExternalPartyHomePageDetails();

    final ExternalParty externalPartyObj = ExternalPartyFactory.newInstance();

    final ExternalPartyHomePageDetails externalPartyHomePageDetails =
      externalPartyObj.readExternalPartyHomePageDetails(externalPartyKey);
    bdmExternalPartyHomePageDetails.details = externalPartyHomePageDetails;

    // BDM CUSTOMIZATION START
    if (bdmExternalPartyHomePageDetails.details.externalPartyDetails.externalPartyDtls.type
      .equals(EXTERNALPARTYTYPE.SSACOUNTRY)) {
      final BDMMaintainExternalParty maintainExternalPartyObj =
        BDMMaintainExternalPartyFactory.newInstance();

      final BDMReadExternalPartyKey readExtPartyKey =
        new BDMReadExternalPartyKey();

      readExtPartyKey.concernRoleID = externalPartyKey.concernRoleID;

      final BDMExternalPartyDetails extPartyDetails =
        maintainExternalPartyObj.readExternalParty(readExtPartyKey);

      bdmExternalPartyHomePageDetails.ssaCountryDetailsInd = true;
      bdmExternalPartyHomePageDetails.assign(extPartyDetails);
      bdmExternalPartyHomePageDetails.ssaLinkTitle =
        bdmExternalPartyHomePageDetails.details.externalPartyDetails.externalPartyDtls.name
          + Constants.kSpace + BDMConstants.kAgreement;

      // Get country of agreement.
      final BDMCountryCodeAndNameKey countryNameKey =
        new BDMCountryCodeAndNameKey();
      countryNameKey.countryName =
        externalPartyHomePageDetails.externalPartyDetails.externalPartyDtls.name;

      final BDMSSACntryAgrmntDetails ssaCntryAgrmntDetails =
        maintainExternalPartyObj.readSSACountryAgrmntDetails(countryNameKey);

      bdmExternalPartyHomePageDetails.countryOfAgreement =
        ssaCntryAgrmntDetails.agreementNumber;
    }

    // BDM CUSTOMIZATION END

    final List<String> addressAttributes = Collections
      .list(new StringTokenizer(
        bdmExternalPartyHomePageDetails.details.externalPartyDetails.formattedAddressData,
        "\n"))
      .stream().map(token -> (String) token).collect(Collectors.toList());
    if (addressAttributes.size() == PARSED_ADDRESS_LENGTH) {
      addressAttributes.remove(BDM_PARSED_INDEX);
      final String addressValue = addressAttributes.stream()
        .map(s -> s.concat("\n")).collect(Collectors.joining());
      bdmExternalPartyHomePageDetails.details.externalPartyDetails.formattedAddressData =
        addressValue;
    }

    return bdmExternalPartyHomePageDetails;
  }

  /**
   * Method to modify external party details.
   *
   * @param External party details.
   *
   * @return void
   */
  @Override
  public InformationMsgDtlsList
    modifyExternalParty(final BDMExternalPartyModifyDetails details)
      throws AppException, InformationalException {

    // Validat modify details.
    if (details.details.externalPartyDtls.type
      .equals(EXTERNALPARTYTYPE.SSACOUNTRY)) {
      this.validateModifyExternalPartyDetails(details);
    }

    final ExternalParty externalPartyObj = ExternalPartyFactory.newInstance();

    ExternalPartyDetails externalPartyDetails = new ExternalPartyDetails();
    externalPartyDetails = details.details;

    final InformationMsgDtlsList informationMsgDtlsList =
      externalPartyObj.modifyExternalParty(externalPartyDetails);

    // BEGIN BDM custom logic.
    if (details.details.externalPartyDtls.type
      .equals(EXTERNALPARTYTYPE.SSACOUNTRY)) {
      final BDMMaintainExternalParty maintainExternalPartyObj =
        BDMMaintainExternalPartyFactory.newInstance();

      maintainExternalPartyObj.modifyExternalParty(details);
    }
    // END BDM custom logic.

    return informationMsgDtlsList;
  }

  /**
   * Method to get the list of SSA Countries.
   *
   * @param N/A
   *
   * @return BDMSSACountryDetailsList
   */
  @Override
  public BDMSSACountryDetailsList listSSACountries()
    throws AppException, InformationalException {

    final BDMMaintainExternalParty maintainExternalPartyObj =
      BDMMaintainExternalPartyFactory.newInstance();

    final BDMSSACountryDetailsList ssaCountryDetailsList =
      maintainExternalPartyObj.listSSACountries();
    return ssaCountryDetailsList;
  }

  /**
   * Method to get filtered list of external party type codetable items.
   *
   * @return External party type codetable items list.
   */
  @Override
  public ListItemsInCodeTable listExternalPartyTypeCodetableItems()
    throws AppException, InformationalException {

    final BDMMaintainExternalParty maintainExternalPartyObj =
      BDMMaintainExternalPartyFactory.newInstance();

    final ListItemsInCodeTable listItemsInCodeTable =
      maintainExternalPartyObj.listExternalPartyTypeCodetableItems();

    return listItemsInCodeTable;
  }

  /**
   * Method to get the list of creation/modification history of external
   * parties.
   *
   * @param BDMReadExternalPartyKey
   *
   * @return BDMSSACountryHistoryDetailsList
   */
  @Override
  public BDMSSACountryHistoryDetailsList
    listExternalPartyHistory(final BDMReadExternalPartyKey key)
      throws AppException, InformationalException {

    final BDMMaintainExternalParty maintainExternalPartyObj =
      BDMMaintainExternalPartyFactory.newInstance();

    final BDMSSACountryHistoryDetailsList extPartyHistoryDetailsList =
      maintainExternalPartyObj.listExternalPartyHistory(key);

    return extPartyHistoryDetailsList;
  }

  /**
   * Method to get the external party history details.
   *
   * @param BDMReadExternalPartyHistoryKey
   *
   * @return BDMSSACountryViewHistoryDetails
   */
  @Override
  public BDMSSACountryViewHistoryDetails
    readExternalPartyHistoryDetails(final BDMReadExternalPartyHistoryKey key)
      throws AppException, InformationalException {

    final BDMMaintainExternalParty maintainExternalPartyObj =
      BDMMaintainExternalPartyFactory.newInstance();

    final BDMSSACountryViewHistoryDetails extPartyHistoryDetails =
      maintainExternalPartyObj.readExternalPartyHistoryDetails(key);

    return extPartyHistoryDetails;
  }

  /**
   * Method to validate External Party details.
   *
   * @param External Party Modify Details
   * @return void
   */
  void validateModifyExternalPartyDetails(
    final BDMExternalPartyModifyDetails details)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    details.details.externalPartyDtls.name =
      details.details.externalPartyDtls.name.trim();

    // ‘Centre of Specialization (COS)' must be entered.
    if (StringUtil.isNullOrEmpty(details.centerOfSpeclizn)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_CENTER_OF_SPCLZN_MUST_BE_ENTERED),
        "", InformationalType.kError);
    }

    // 'Totalization with 3rd Country’ must be entered.
    if (StringUtil.isNullOrEmpty(details.totalizatnCountry)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_TOTALIZATION_WITH_3RD_CNTRY_MUST_BE_ENTERED),
        "", InformationalType.kError);
    }

    // ‘Status of Agreement' must be entered.
    if (StringUtil.isNullOrEmpty(details.statusOfAgreement)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_STATUS_OF_AGREEMENT_MUST_BE_ENTERED),
        "", InformationalType.kError);
    }

    // ‘Is Protective Date associated with this Country' must be entered.
    if (StringUtil.isNullOrEmpty(details.isProtectiveDate)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_PROTECTIVE_DATE_WITH_CNTRY_MUST_BE_ENTERED),
        "", InformationalType.kError);
    }

    // ‘SSA link' must be entered.
    if (StringUtil.isNullOrEmpty(details.ssaLink)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEXTERNALPARTY.ERR_SSA_LINK_MUST_BE_ENTERED), "",
        InformationalType.kError);
    }

    // Fail for mandatory validations.
    informationalManager.failOperation();

    // Canada cannot be selected as a Foreign Country
    final String extPartyName = details.details.externalPartyDtls.name;

    if (extPartyName.toUpperCase()
      .equals(BDMConstants.kCanada.toUpperCase())) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_CANADA_CAN_NOT_BE_SELECTED_AS_A_FOREIGN_COUNTRY),
          "", InformationalType.kError);
    }

    // Check if external party with name entered already exists.
    final boolean extlPartyAlreadyExists =
      this.isExternalPartyAlreadyExists_Modify(
        details.details.externalPartyDtls.name,
        details.details.externalPartyDtls.concernRoleID);

    if (extlPartyAlreadyExists) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_EXT_PARTY_NAME_COUNTRY_ALREADY_EXISTS),
        "", InformationalType.kError);
    }

    // ‘Date of Revised Agreement’ must be after ‘Date of Entry into Force’
    if (!details.revisedAgrmntDate.isZero()
      && !details.forceEntryDate.isZero()
      && details.revisedAgrmntDate.before(details.forceEntryDate)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_DATE_OF_REVISED_AGREEMENT_MUST_BE_AFTER_DATE_OF_ENTRY_INTO_FORCE),
          "", InformationalType.kError);
    }

    // ‘End Date’ must be after ‘Date of Entry into Force’
    if (!details.ssaEndDate.isZero() && !details.forceEntryDate.isZero()
      && details.ssaEndDate.before(details.forceEntryDate)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_DATE_OF_ENTRY_INTO_FORCE),
          "", InformationalType.kError);
    }

    // ‘End Date’ must be after ‘Date of Revised Agreement’
    if (!details.revisedAgrmntDate.isZero() && !details.ssaEndDate.isZero()
      && details.ssaEndDate.before(details.revisedAgrmntDate)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_THE_DATE_OF_REVISED_AGREEMENT),
          "", InformationalType.kError);
    }

    // ‘End Date’ must be after the ‘Entry with Quebec (QC) Start Date’
    if (!details.ssaEndDate.isZero() && !details.qcEntryStartDate.isZero()
      && details.ssaEndDate.before(details.qcEntryStartDate)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_END_DATE_MUST_BE_AFTER_QUBEC_START_DATE),
        "", InformationalType.kError);
    }

    // ‘Date of Entry into Force’ must remain blank if ‘Status of Agreement’ is
    // ‘Not in Force’
    if (!details.forceEntryDate.isZero()
      && details.statusOfAgreement.equals(BDMSTATUSOFAGREEMENT.NOTINFORCE)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMEXTERNALPARTY.ERR_DATE_OF_ENTRY_INTO_FORCE_CAN_NOT_BE_ENTERED),
        "", InformationalType.kError);
    }

    // ‘Entry with Quebec (QC) Start Date’ cannot be empty if ‘Entry with Quebec
    // (QC) End Date’ is entered
    if (!details.qcEntryEndDate.isZero()
      && details.qcEntryStartDate.isZero()) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_QUBEC_START_DATE_CAN_NOT_BE_EMPTY_IF_QUBEC_END_DATE_ENTERED),
          "", InformationalType.kError);
    }

    // ‘Entry with Quebec (QC) End Date’ must be after ‘Entry with Quebec (QC)
    // Start Date’
    if (!details.qcEntryEndDate.isZero() && !details.qcEntryStartDate.isZero()
      && details.qcEntryEndDate.before(details.qcEntryStartDate)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_QUBEC_END_DATE_MUST_BE_AFTER_THE_QUBEC_START_DATE),
          "", InformationalType.kError);
    }

    // ‘Totalization with 3rd Country’ cannot be ‘Yes’
    if (details.statusOfAgreement.equals(BDMSTATUSOFAGREEMENT.INFORMATIONONLY)
      && details.totalizatnCountry.equals(BDMYESNO.YES)) {
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMEXTERNALPARTY.ERR_TOTALIZATION_WITH_THIRD_COUNTRY_CAN_NOT_BE_YES),
          "", InformationalType.kError);
    }

    // ‘‘Totalization with 3rd Country’ cannot be blank
    // This is not possible as there is already a check on totalizatnCountry is
    // blank
    /*
     * if (details.statusOfAgreement.equals(BDMSTATUSOFAGREEMENT.INFORCE)
     * && StringUtil.isNullOrEmpty(details.totalizatnCountry)) {
     * ValidationManagerFactory.getManager()
     * .addInfoMgrExceptionWithLookup(new AppException(
     * BDMEXTERNALPARTY.ERR_TOTALIZATION_WITH_THIRD_COUNTRY_CAN_NOT_BE_BLANK),
     * "", InformationalType.kError);
     * }
     */

    // Fail for business validation.
    informationalManager.failOperation();
  }

  /**
   * Method to validate External Party details.
   *
   * @param registrationDtls, bdmextPartyRegDetails
   * @return void
   */
  private boolean isExtPartyForSSACountryAlreadyExists(
    final String countryCode) throws AppException, InformationalException {

    boolean extlPartyForSSACntAlreadyExists = false;

    final BDMExtPartyNameDetailsList extPartyDetailsList =
      this.getExternalPartiesByTypeAndAddrEle(countryCode);

    if (extPartyDetailsList.dtls.size() > 0) {
      extlPartyForSSACntAlreadyExists = true;
    }

    return extlPartyForSSACntAlreadyExists;
  }

  /**
   * Method to validate External Party details.
   *
   * @param registrationDtls, bdmextPartyRegDetails
   * @return void
   */
  private BDMExtPartyNameDetailsList getExternalPartiesByTypeAndAddrEle(
    final String countryCode) throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.intf.BDMExternalParty externalPartyObj =
      curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory.newInstance();

    final BDMExtPartyTypeAndAddrKey typeAndStatusKey =
      new BDMExtPartyTypeAndAddrKey();
    typeAndStatusKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;
    typeAndStatusKey.addrEleType = ADDRESSELEMENTTYPE.COUNTRY;
    typeAndStatusKey.addrEleValue = countryCode;

    final BDMExtPartyNameDetailsList extPartyDetailsList =
      externalPartyObj.searchExtPartyByTypeAndAddrEle(typeAndStatusKey);

    return extPartyDetailsList;

  }

  /**
   * Modifies an address record for an external party.
   *
   * @param details
   * The concern role ID and concern role address details.
   *
   * @return ModifiedAddressDetails modified address details
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public ModifiedAddressDetails
    modifyAddress(final MaintainParticipantAddressDetails details)
      throws AppException, InformationalException {

    // BEGIN BDM FEATURE 52455
    final ReadExternalPartyHomePageDetails readDetails =
      this.getExternalPartyTypeDetails(details.addressDetails.concernRoleID);

    if (readDetails.type.equals(EXTERNALPARTYTYPE.SSACOUNTRY)) {
      final String countryCode = this
        .getCountryCodeFromAddressData(details.addressDetails.addressData);

      if (countryCode.equals(COUNTRY.CA)) {
        throw new AppException(
          BDMEXTERNALPARTY.ERR_CANADA_CAN_NOT_BE_SELECTED_AS_A_FOREIGN_COUNTRY);
      }
    }
    // END BDM FEATURE 52455
    // START TASK 118752 : Set the BDMUNPARSE to 1 for the INTL Address
    details.addressDetails.addressData = bdmUtil
      .setBDMUnparsedIndForINTLAddress(details.addressDetails.addressData);
    // END TASK 118752
    // OOTB Call
    final Participant delegate = ParticipantFactory.newInstance();

    final ModifiedAddressDetails modifiedAddressDetails =
      delegate.modifyAddress(details);
    return modifiedAddressDetails;

  }

  /**
   * @param officeKey
   * @throws AppException
   * @throws InformationalException
   * This method deletes the external part office by setting its recordstatus to
   * cancelled
   * and validates that the external party office is not link to an FCE case
   */
  @Override
  public void
    cancelExternalPartyOffice(final ExternalPartyOfficeKey officeKey)
      throws AppException, InformationalException {

    final ExternalPartyOffice externalPartyOfficeObj =
      curam.core.sl.entity.fact.ExternalPartyOfficeFactory.newInstance();

    final ExternalPartyOfficeDtls details =
      externalPartyOfficeObj.read(officeKey);

    if (isSSACountry(details.concernRoleID)) {
      ValidateOfficeLinkToFecApplication(officeKey, details.concernRoleID);
    }

    externalPartyOfficeObj.cancel(officeKey, details);

  }

  /**
   * @param officeDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   * This method creates the external party office and validates that the
   * external party office
   * is not duplicated and sets the address type to Mailing for SSA country
   */
  @Override
  public InformationMsgDtlsList
    createExternalPartyOffice(final ExternalPartyOfficeDetails officeDetails)
      throws AppException, InformationalException {

    // Create return object
    final InformationMsgDtlsList informationMsgDtlsList =
      new InformationMsgDtlsList();

    // Flush out the informational manager
    TransactionInfo.setInformationalManager();

    final Address address = AddressFactory.newInstance();
    final AddressKey addressKey = new AddressKey();
    addressKey.addressID = officeDetails.addressDtls.addressID;
    AddressDtls addressDtls = new AddressDtls();
    if (addressKey.addressID != 0) {
      addressDtls = address.read(addressKey);
    }
    // START TASK 118752 : Set the BDMUNPARSE to 1 for the INTL Address
    officeDetails.addressDtls.addressData = bdmUtil
      .setBDMUnparsedIndForINTLAddress(officeDetails.addressDtls.addressData);
    // END TASK 118752
    final boolean isSsaCountry =
      isSSACountry(officeDetails.externalPartyOfficeDtls.concernRoleID);

    if (isSsaCountry) {
      validateExternalOffice(
        officeDetails.externalPartyOfficeDtls.concernRoleID,
        officeDetails.externalPartyOfficeDtls.name);
      if (addressKey.addressID != 0) {
        validateExternalOfficeAddress(
          officeDetails.externalPartyOfficeDtls.concernRoleID,
          addressDtls.addressData, CuramConst.kLongZero);
      } else {
        validateExternalOfficeAddress(
          officeDetails.externalPartyOfficeDtls.concernRoleID,
          officeDetails.addressDtls.addressData, CuramConst.kLongZero);
      }
    }
    // Create the external party office
    final ExternalPartyOfficeKey externalPartyOffice =
      ExternalPartyOfficeFactory.newInstance()
        .createExternalPartyOffice(officeDetails);

    if (isSsaCountry) {
      final ExternalPartyOfficeAddress externalPartyOfficeAddress =
        ExternalPartyOfficeAddressFactory.newInstance();
      final ExternalPartyOfficeAddressDtlsList externalPartyOfficeAddressDtlsList =
        externalPartyOfficeAddress
          .searchAddressesByExternalPartyOffice(externalPartyOffice);

      final ExternalPartyOfficeAddressDtls externalPartyOfficeAddressDtls =
        externalPartyOfficeAddressDtlsList.dtls.get(0);

      externalPartyOfficeAddressDtls.type = CONCERNROLEADDRESSTYPE.MAILING;

      final ExternalPartyOfficeAddressKey externalPartyOfficeAddressKey =
        new ExternalPartyOfficeAddressKey();
      externalPartyOfficeAddressKey.officeAddressID =
        externalPartyOfficeAddressDtls.officeAddressID;

      externalPartyOfficeAddress.modify(externalPartyOfficeAddressKey,
        externalPartyOfficeAddressDtls);

    }

    // Handle messages from the informational manager
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {

      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = warnings[i];
      informationMsgDtlsList.informationalMsgDtlsList.dtls
        .addRef(informationalMsgDtls);
    }
    return informationMsgDtlsList;
  }

  /**
   * @param officeDtls
   * @return
   * @throws AppException
   * @throws InformationalException
   * this method modifies the external party office and validates that the
   * external party office
   * is not duplicated for ssa country and saves a copy of the old record the
   * externalPartyOfficeHistory table
   */
  @Override
  public InformationMsgDtlsList
    modifyExternalPartyOffice(final ExternalPartyOfficeDtls officeDtls)
      throws AppException, InformationalException {

    // Create return object
    final InformationMsgDtlsList informationMsgDtlsList =
      new InformationMsgDtlsList();

    // Flush out the informational manager
    TransactionInfo.setInformationalManager();

    final boolean ssaCountry = isSSACountry(officeDtls.concernRoleID);
    if (ssaCountry) {
      final curam.core.sl.intf.ExternalPartyOffice externalPartyOffice =
        ExternalPartyOfficeFactory.newInstance();
      final ExternalPartyOfficeKey externalPartyOfficeKey =
        new ExternalPartyOfficeKey();
      externalPartyOfficeKey.externalPartyOfficeID =
        officeDtls.externalPartyOfficeID;
      final ExternalPartyOfficeDtls externalPartyOfficeDtlsBeforeModify =
        externalPartyOffice.readExternalPartyOffice(externalPartyOfficeKey);
      if (!externalPartyOfficeDtlsBeforeModify.name
        .equalsIgnoreCase(officeDtls.name)) {
        validateExternalOffice(officeDtls.concernRoleID, officeDtls.name);
        validateExternalOfficeAddress(officeDtls.concernRoleID, null,
          CuramConst.kLongZero);
      }
    }

    // Modify the external party office details
    ExternalPartyOfficeFactory.newInstance()
      .modifyExternalPartyOffice(officeDtls);

    // Handle messages from the informational manager
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {

      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = warnings[i];
      informationMsgDtlsList.informationalMsgDtlsList.dtls
        .addRef(informationalMsgDtls);
    }

    if (ssaCountry) {
      final BDMExternalPartyOfficeHistory bdmExternalPartyOfficeHistory =
        BDMExternalPartyOfficeHistoryFactory.newInstance();
      final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
      final BDMExternalPartyOfficeHistoryDtls bdmExternalPartyOfficeHistoryDtls =
        new BDMExternalPartyOfficeHistoryDtls();
      bdmExternalPartyOfficeHistoryDtls.assign(officeDtls);
      bdmExternalPartyOfficeHistoryDtls.externalPartyOfficeHistoryID =
        uniqueIDObj.getNextID();
      bdmExternalPartyOfficeHistory.insert(bdmExternalPartyOfficeHistoryDtls);
    }

    return informationMsgDtlsList;
  }

  /**
   * @param details
   * @return
   * @throws AppException
   * @throws InformationalException
   *
   * This method modifies the external party office address and validates that
   * the address
   * is duplicated in the external part offices for SSA country
   */
  @Override
  public InformationMsgDtlsList
    modifyOfficeAddress(final ExternalPartyOfficeAddressDetails details)
      throws AppException, InformationalException {

    // END, CR00178447

    // Create return object
    final InformationMsgDtlsList informationMsgDtlsList =
      new InformationMsgDtlsList();

    // Flush out the informational manager
    TransactionInfo.setInformationalManager();

    final curam.core.sl.intf.ExternalPartyOffice externalPartyOffice =
      ExternalPartyOfficeFactory.newInstance();
    final ExternalPartyOfficeKey externalPartyOfficeKey =
      new ExternalPartyOfficeKey();
    externalPartyOfficeKey.externalPartyOfficeID =
      details.officeAddressDtls.externalPartyOfficeID;
    final ExternalPartyOfficeDtls externalPartyOfficeDtls =
      externalPartyOffice.readExternalPartyOffice(externalPartyOfficeKey);

    if (isSSACountry(externalPartyOfficeDtls.concernRoleID)) {
      final Address address = AddressFactory.newInstance();
      final AddressKey addressKey = new AddressKey();
      addressKey.addressID = details.addressDtls.addressID;
      final AddressDtls addressDtls = address.read(addressKey);
      final Map<String, String> originalAddressDataMap =
        BDMUtil.getAddressDataMap(addressDtls.addressData);
      final Map<String, String> modifiedAddressDataMap =
        BDMUtil.getAddressDataMap(details.addressDtls.addressData);

      if (!originalAddressDataMap.equals(modifiedAddressDataMap)) {
        validateExternalOfficeAddress(externalPartyOfficeDtls.concernRoleID,
          details.addressDtls.addressData, CuramConst.kLongZero);
      }
    }

    // START TASK 118752 : Set the BDMUNPARSE to 1 for the INTL Address
    details.addressDtls.addressData = bdmUtil
      .setBDMUnparsedIndForINTLAddress(details.addressDtls.addressData);
    // END TASK 118752
    // Set up the parameters to service layer modify
    final MaintainExternalPartyOfficeAddressDtls dtls =
      new MaintainExternalPartyOfficeAddressDtls();

    dtls.assign(details.officeAddressDtls);
    dtls.assign(details.addressDtls);
    // BEGIN, CR00100045, CW
    dtls.primaryAddressInd = details.primaryAddressInd;
    dtls.versionNo = details.officeAddressDtls.versionNo;
    // END, CR00100045

    final ExternalPartyOfficeKey key = new ExternalPartyOfficeKey();

    key.externalPartyOfficeID =
      details.officeAddressDtls.externalPartyOfficeID;

    // BEGIN, CR00100045, CW
    // Modify the external party office address
    curam.core.sl.fact.ExternalPartyOfficeAddressFactory.newInstance()
      .modifyOfficeAddress(key, dtls);
    // END, CR00100045

    // Handle messages from the informational manager
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {

      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = warnings[i];
      informationMsgDtlsList.informationalMsgDtlsList.dtls
        .addRef(informationalMsgDtls);
    }

    return informationMsgDtlsList;
  }

  /**
   * @param externalPartyKey
   * @return
   * @throws AppException
   * @throws InformationalException
   *
   * This method list the external party offices and filters out the records
   * with cancelled status
   */
  @Override
  public BDMExternalPartyOfficeList
    listExternalPartyOffice(final ExternalPartyKey externalPartyKey)
      throws AppException, InformationalException {

    // Read external party office list
    final ExternalPartyOfficeList externalPartyOfficeList =
      ExternalPartyOfficeFactory.newInstance()
        .listExternalPartyOffice(externalPartyKey);
    final BDMExternalPartyOfficeList bdmExternalPartyOfficeList =
      new BDMExternalPartyOfficeList();
    bdmExternalPartyOfficeList.displayAdminInd = BDMUtil.isIOAdminUser();

    bdmExternalPartyOfficeList.dtls.assign(externalPartyOfficeList);
    externalPartyOfficeList.description =
      getExternalPartyContextDescription(externalPartyKey).description;
    externalPartyOfficeList.menuData =
      getExternalPartyMenuData(externalPartyKey);

    bdmExternalPartyOfficeList.dtls.list.removeIf(e -> RECORDSTATUS.CANCELLED
      .equals(e.externalPartyOfficeDtls.recordStatus));

    return bdmExternalPartyOfficeList;
  }

  /**
   * Method to get external party address list.
   *
   * @param officeKey
   * @return officeAddressDetailsList
   */
  @Override
  public BDMExternalPartyOfficeAddressDetailsList
    listOfficeAddress(final ExternalPartyOfficeKey officeKey)
      throws AppException, InformationalException {

    // END, CR00178447

    // BEGIN, CR00102719, KH
    final ExternalPartyOfficeAddressDetailsList officeAddressDetailsList =
      curam.core.sl.fact.ExternalPartyOfficeAddressFactory.newInstance()
        .listOfficeAddress(officeKey);

    officeAddressDetailsList.menuData =
      getExternalPartyOfficeMenuData(officeKey);
    final BDMExternalPartyOfficeAddressDetailsList bdmExternalPartyOfficeAddressDetailsList =
      new BDMExternalPartyOfficeAddressDetailsList();
    bdmExternalPartyOfficeAddressDetailsList.dtls
      .assign(officeAddressDetailsList);
    bdmExternalPartyOfficeAddressDetailsList.displayAdminInd =
      BDMUtil.isIOAdminUser();

    return bdmExternalPartyOfficeAddressDetailsList;
  }

  /**
   * Method to get external party details.
   *
   * @param long concernRoleID
   * @return ReadExternalPartyHomePageDetails
   */
  private ReadExternalPartyHomePageDetails getExternalPartyTypeDetails(
    final long concernRoleID) throws AppException, InformationalException {

    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    externalPartyKey.concernRoleID = concernRoleID;

    // Retrieve the external party information from the database
    final ReadExternalPartyHomePageDetails readExternalPartyHomePageDetails =
      curam.core.sl.entity.fact.ExternalPartyFactory.newInstance()
        .readExternalPartyHomePageDetails(externalPartyKey);

    return readExternalPartyHomePageDetails;
  }

  /**
   * Method to get countryCode from address data.
   *
   * @param String addressData
   * @return String countryCode
   */
  String getCountryCodeFromAddressData(final String addressData)
    throws AppException, InformationalException {

    final BDMAddressUtil addressUtil = new BDMAddressUtil();
    final OtherAddressData otherAddressData = new OtherAddressData();
    otherAddressData.addressData = addressData;

    final String countryCode = addressUtil.getAddressDetails(otherAddressData,
      ADDRESSELEMENTTYPE.COUNTRY);

    return countryCode;
  }

  /**
   * Method to link SSACountry with FECase
   *
   * @param String addressData
   * @return long concernRoleID
   */
  private void linkSSACountryWithFECase(final String addressData,
    final long concernRoleID) throws AppException, InformationalException {

    final String countryCode =
      this.getCountryCodeFromAddressData(addressData);

    final BDMReadByCountryCodeKey readByCountryCodeKey =
      new BDMReadByCountryCodeKey();
    readByCountryCodeKey.countryCode = countryCode;

    final BDMFECase feCaseObj = BDMFECaseFactory.newInstance();
    final BDMFECaseDtlsStruct1List bdmfeCaseDtlsStruct1List =
      feCaseObj.readMultipleFECaseByCountryCode(readByCountryCodeKey);

    for (final BDMFECaseDtlsStruct1 dtlsStruct1 : bdmfeCaseDtlsStruct1List.dtls) {
      final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
      final BDMFECaseDtls feCaseDtls = new BDMFECaseDtls();
      bdmfeCaseKey.caseID = dtlsStruct1.caseID;
      feCaseDtls.assign(dtlsStruct1);
      feCaseDtls.ssaCountryID = concernRoleID;
      feCaseObj.modify(bdmfeCaseKey, feCaseDtls);
    }
  }

  /**
   * Method to get the external party description
   *
   * @param String externalPartyKey
   * @return long participantContextDescriptionDetails
   */
  private ParticipantContextDescriptionDetails
    getExternalPartyContextDescription(
      final ExternalPartyKey externalPartyKey)
      throws AppException, InformationalException {

    final ParticipantContextDescriptionKey key =
      new ParticipantContextDescriptionKey();

    key.concernRoleID = externalPartyKey.concernRoleID;

    return ParticipantContextFactory.newInstance()
      .readContextDescription(key);
  }

  /**
   * Method to get the external party menu details
   *
   * @param String externalPartyKey
   * @return long menuDataDetails
   */
  private MenuDataDetails
    getExternalPartyMenuData(final ExternalPartyKey externalPartyKey)
      throws AppException, InformationalException {

    // Create return object
    final MenuDataDetails menuDataDetails = new MenuDataDetails();

    // Create Root Node
    final Element navigationMenuElement = new Element(kNavigationMenu);

    // Retrieve the external party details
    final ExternalPartyDtls externalPartyDtls =
      curam.core.sl.entity.fact.ExternalPartyFactory.newInstance()
        .read(externalPartyKey);

    // Build the child node and add it to the root node
    navigationMenuElement
      .addContent(buildExternalPartyNode(externalPartyDtls));

    // Output the XML as a string and assign it to the return object
    final XMLOutputter xmlOutputter = new XMLOutputter();

    menuDataDetails.menuData =
      xmlOutputter.outputString(navigationMenuElement);

    return menuDataDetails;
  }

  /**
   * This method generates the dynamic menu data for an external party office.
   *
   * @param officeKey Contains the external party office identifier.
   *
   * @return Menu data for the external party office.
   *
   * @throws InformationalException Generic Exception Signature.
   * @throws AppException Generic Exception Signature.
   */
  private MenuDataDetails
    getExternalPartyOfficeMenuData(final ExternalPartyOfficeKey officeKey)
      throws AppException, InformationalException {

    // Create return object
    final MenuDataDetails menuDataDetails = new MenuDataDetails();

    // Create Root Node
    final Element navigationMenuElement = new Element(kNavigationMenu);

    // Retrieve the external party office details
    final ExternalPartyOfficeDtls officeDtls =
      curam.core.sl.entity.fact.ExternalPartyOfficeFactory.newInstance()
        .read(officeKey);

    // Retrieve the external party details
    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();

    externalPartyKey.concernRoleID = officeDtls.concernRoleID;
    final ExternalPartyDtls externalPartyDtls =
      curam.core.sl.entity.fact.ExternalPartyFactory.newInstance()
        .read(externalPartyKey);

    // Build the child nodes and add them to the root node
    navigationMenuElement
      .addContent(buildExternalPartyNode(externalPartyDtls));
    navigationMenuElement
      .addContent(buildExternalPartyOfficeNode(officeDtls));

    // Output the XML as a string and assign it to the return object
    final XMLOutputter outputter = new XMLOutputter();

    menuDataDetails.menuData = outputter.outputString(navigationMenuElement);

    return menuDataDetails;
  }

  /**
   * Method to build the external party node
   *
   * @param String externalPartyDtls
   * @return long linkElement
   */
  private Element
    buildExternalPartyNode(final ExternalPartyDtls externalPartyDtls)
      throws AppException, InformationalException {

    final Element linkElement = new Element(kItem);

    final LocalisableString description = new LocalisableString(
      GENERALEXTERNALPARTY.INF_DYNAMIC_MENU_DESCRIPTION_DOUBLE);

    description.arg(externalPartyDtls.name);
    description.arg(externalPartyDtls.primaryAlternateID);

    // Setting the link page ID, description and type
    linkElement.setAttribute(kPageID, kExternalPartyHome);
    linkElement.setAttribute(kDesc, description.toClientFormattedText());
    linkElement.setAttribute(kType, kTypeExternalParty);

    // Create parameter
    final Element paramElement = new Element(kParam);

    // Set the parameters name and value
    paramElement.setAttribute(kName, kParamConcernRoleID);
    paramElement.setAttribute(kValue,
      String.valueOf(externalPartyDtls.concernRoleID));

    // Add the parameter elements to the node
    linkElement.addContent(paramElement);

    return linkElement;
  }

  /**
   * This method creates the external party office node of the dynamic menu.
   *
   * @param officeDtls
   * Contains the details of the external party office.
   *
   * @return The external party office node.
   *
   * @throws InformationalException
   * Generic Exception Signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  protected Element
    buildExternalPartyOfficeNode(final ExternalPartyOfficeDtls officeDtls)
      throws AppException, InformationalException {

    // Create Node (link elements)
    final Element linkElement = new Element(kItem);

    final LocalisableString description = new LocalisableString(
      GENERALEXTERNALPARTY.INF_DYNAMIC_MENU_DESCRIPTION_SINGLE);

    description.arg(officeDtls.name);

    // Setting the link page ID, description and type
    linkElement.setAttribute(kPageID, kExternalPartyOfficeHome);
    linkElement.setAttribute(kDesc, description.toClientFormattedText());
    linkElement.setAttribute(kType, kTypeExternalPartyOffice);

    // Create parameter
    final Element paramElement = new Element(kParam);

    // Set the parameters name and value
    paramElement.setAttribute(kName, kParamExternalPartyOfficeID);
    paramElement.setAttribute(kValue,
      String.valueOf(officeDtls.externalPartyOfficeID));

    // Add the parameter elements to the node
    linkElement.addContent(paramElement);

    return linkElement;
  }

  /**
   * Method to validate the external party office
   *
   * @param long concernRoleId
   * @param String name
   *
   */
  void validateExternalOffice(final long concernRoleId, final String name)
    throws AppException, InformationalException {

    if (isOfficeNameDuplicate(name, concernRoleId)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMEXTERNALPARTY.ERR_EXT_PARTY_OFFICE_NAME_EXISTS),
        "", InformationalType.kError);
    }

  }

  /**
   * Method to validate the external party office address
   *
   * @param long concernRoleId
   * @param String addressData
   *
   */
  void validateExternalOfficeAddress(final long concernRoleId,
    final String addressData, final long addressID)
    throws AppException, InformationalException {

    if (isSSACountry(concernRoleId)) {
      if (Objects.nonNull(addressData)
        && isExternalPartyOfficeAddressExists(concernRoleId, addressData)) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMEXTERNALPARTY.ERR_EXT_PARTY_OFFICE_ADDRESS_EXISTS),
          "", InformationalType.kError);
      }

      if (Objects.nonNull(addressData) && COUNTRYCODE.CACODE
        .equals(BDMUtil.getAddressDataMap(addressData).get("COUNTRY"))) {
        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BDMEXTERNALPARTY.ERR_CANADA_CAN_NOT_BE_SELECTED_AS_A_FOREIGN_COUNTRY),
            "", InformationalType.kError);
      }
      if (Objects.nonNull(addressData)
        && !isForeignOfficeAddressCountryValid(concernRoleId, addressData,
          addressID)) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMEXTERNALPARTY.ERR_EXT_PARTY_OFFICE_ADDRESS_COUNTRY_EXISTS),
          "", InformationalType.kError);
      }
    }

  }

  /**
   * Method to validate the external party office link to FCE application
   *
   * @param ExternalPartyOfficeKey officeKey
   * @param long concernRoleId
   *
   */
  private void ValidateOfficeLinkToFecApplication(
    final ExternalPartyOfficeKey officeKey, final long concernRoleId)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.intf.BDMExternalParty bdmExternalParty =
      BDMExternalPartyFactory.newInstance();

    final ExternalPartyOfficeKey externalPartyOfficeKey =
      new ExternalPartyOfficeKey();

    final ExternalPartyOfficeAddress externalPartyOfficeAddress =
      ExternalPartyOfficeAddressFactory.newInstance();

    final ExternalPartyOfficeAddressDtlsList externalPartyOfficeAddressDtlsList =
      externalPartyOfficeAddress
        .searchAddressesByExternalPartyOffice(officeKey);

    for (int i = 0; i < externalPartyOfficeAddressDtlsList.dtls.size(); i++) {

      final long externalPrtyOfficeID =
        externalPartyOfficeAddressDtlsList.dtls.item(i).externalPartyOfficeID;

      final Address address = AddressFactory.newInstance();

      final AddressKey addressKey = new AddressKey();
      addressKey.addressID =
        externalPartyOfficeAddressDtlsList.dtls.get(0).addressID;

      final AddressDtls addressDtls = address.read(addressKey);

      final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();

      final BDMReadByCountryCodeKey bdmReadByCountryCodeKey =
        new BDMReadByCountryCodeKey();
      bdmReadByCountryCodeKey.countryCode = addressDtls.countryCode;

      final BDMFECaseDtlsStruct1List bdmfeCaseDtlsStruct1List =
        bdmfeCase.readMultipleFECaseByCountryCode(bdmReadByCountryCodeKey);

      if (bdmfeCaseDtlsStruct1List.dtls.size() > CuramConst.gkZero) {

        externalPartyOfficeKey.externalPartyOfficeID = externalPrtyOfficeID;

        final Count count = bdmExternalParty.getFLRecordCount(officeKey);

        final Count count2 = bdmExternalParty.getFARecordCount(officeKey);

        if (count.numberOfRecords > CuramConst.gkZero
          || count2.numberOfRecords > CuramConst.gkZero) {

          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(
              BDMEXTERNALPARTY.ERR_EXT_PARTY_OFFICE_APP_LINK_EXISTS),
            "", InformationalType.kError);

        }

      }
    }
  }

  /**
   * Method checks if the external party office name is a duplicate
   *
   * @param String newName
   * @param long concernRoleId
   * returns true or false
   */
  private boolean isOfficeNameDuplicate(final String newName,
    final long concernRoleId) throws AppException, InformationalException {

    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    externalPartyKey.concernRoleID = concernRoleId;
    final ExternalPartyOfficeList externalPartyOfficeList =
      ExternalPartyOfficeFactory.newInstance()
        .listExternalPartyOffice(externalPartyKey);
    return externalPartyOfficeList.list.stream()
      .filter(i -> i.externalPartyOfficeDtls.recordStatus
        .equals(RECORDSTATUS.NORMAL))
      .anyMatch(
        i -> i.externalPartyOfficeDtls.name.equalsIgnoreCase(newName));
  }

  /**
   * Method checks if the external party type is an SSA country
   *
   * @param long concernRoleId
   * returns true or false
   */
  private boolean isSSACountry(final long concernRoleId)
    throws AppException, InformationalException {

    final curam.core.sl.entity.intf.ExternalParty externalPartyObj =
      curam.core.sl.entity.fact.ExternalPartyFactory.newInstance();
    final ExternalPartyKey extPartyKey = new ExternalPartyKey();
    extPartyKey.concernRoleID = concernRoleId;
    final ExternalPartyDtls extPartyDtls = externalPartyObj.read(extPartyKey);

    return extPartyDtls.type.equals(EXTERNALPARTYTYPE.SSACOUNTRY);
  }

  /**
   * Method checks if the external party office address exists
   *
   * @param long concernRoleId
   * @param String addressData
   * returns true or false
   */
  private boolean isExternalPartyOfficeAddressExists(final long concernRoleId,
    final String addressData) throws AppException, InformationalException {

    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    externalPartyKey.concernRoleID = concernRoleId;
    final BDMExternalPartyOfficeList BDMExternalPartyOfficeList =
      listExternalPartyOffice(externalPartyKey);

    final Map<String, String> currentAddressDataMap =
      BDMUtil.getAddressDataMap(addressData);

    return BDMExternalPartyOfficeList.dtls.list.stream()
      .map(externalPartyOfficeDetails -> BDMUtil.getAddressDataMap(
        externalPartyOfficeDetails.addressDtls.addressData))
      .anyMatch(currentAddressDataMap::equals);
  }

  private boolean isForeignOfficeAddressCountryValid(final long concernRoleId,
    final String addressData, final long addressID)
    throws AppException, InformationalException {

    final ConcernRole concernRole = ConcernRoleFactory.newInstance();
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = concernRoleId;
    final ConcernRoleDtls concernRoleDtls =
      concernRole.readConcernRole(concernRoleKey);

    final Address address = AddressFactory.newInstance();
    AddressKey addressKey = new AddressKey();
    addressKey.addressID = concernRoleDtls.primaryAddressID;
    AddressDtls addressDtls = address.read(addressKey);

    Map<String, String> currentAddressDataMap =
      BDMUtil.getAddressDataMap(addressData);

    final Map<String, String> externalPartyAddressData =
      BDMUtil.getAddressDataMap(addressDtls.addressData);

    boolean countryValueFoundInd = false;

    // START: Bug 102938

    if (!currentAddressDataMap.isEmpty()
      && !externalPartyAddressData.isEmpty() && !externalPartyAddressData
        .get("COUNTRY").equals(currentAddressDataMap.get("COUNTRY"))) {
      countryValueFoundInd = true;
      return false;
    }

    if (!countryValueFoundInd && addressID != CuramConst.kLongZero
      && currentAddressDataMap.isEmpty()) {

      addressKey = new AddressKey();
      addressKey.addressID = addressID;
      addressDtls = address.read(addressKey);

      currentAddressDataMap =
        BDMUtil.getAddressDataMap(addressDtls.addressData);

      if (!currentAddressDataMap.isEmpty()
        && !externalPartyAddressData.isEmpty() && !externalPartyAddressData
          .get("COUNTRY").equals(currentAddressDataMap.get("COUNTRY"))) {
        countryValueFoundInd = true;
        return false;
      }

    }
    // END: Bug 102938

    return true;
  }

  /**
   * Wrapper method to OOTB list external party address method.
   * Contains logic to set the indicators for enabling or disabling the list
   * action links.
   *
   * @param ExternalPartyKey
   *
   * @return BDMExtlPartyAddrDetailsList
   */
  @Override
  public BDMExtlPartyAddrDetailsList listExternalPartyAddress(
    final ExternalPartyKey key) throws AppException, InformationalException {

    final BDMExtlPartyAddrDetailsList extPartyAddressList =
      new BDMExtlPartyAddrDetailsList();
    // OOTB Call
    extPartyAddressList.listDetails =
      ExternalPartyFactory.newInstance().listExternalPartyAddress(key);

    // BEGIN BDM logic

    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    externalPartyKey.concernRoleID = key.concernRoleID;

    final ExternalPartyDtls externalPartyDtls =
      curam.core.sl.entity.fact.ExternalPartyFactory.newInstance()
        .read(externalPartyKey);

    final boolean isIOAdminUser = BDMUtil.isIOAdminUser();

    if (externalPartyDtls.type.equals(EXTERNALPARTYTYPE.SSACOUNTRY)
      && !isIOAdminUser) {
      extPartyAddressList.disableInd = true;
    }

    // END BDM logic

    return extPartyAddressList;
  }

  @Override
  public BDMExternalPartyTaskDetailsList listExternalPartyTask(
    final ExternalPartyKey key) throws AppException, InformationalException {

    final BDMExternalPartyTaskDetailsList bdmExternalPartyTaskDetailsList =
      new BDMExternalPartyTaskDetailsList();

    ExternalPartyTaskDetailsList externalPartyTaskDetailsList =
      new ExternalPartyTaskDetailsList();

    final ExternalParty externalParty = ExternalPartyFactory.newInstance();
    externalPartyTaskDetailsList = externalParty.listExternalPartyTask(key);

    bdmExternalPartyTaskDetailsList.assign(externalPartyTaskDetailsList);

    TaskKey taskKey = null;
    CaseUrgentFlagStringDetails caseUrgentFlagStringDetails = null;
    BDMTaskSearchDetails bdmTaskSearchDetails = null;
    BDMEscalationLevelString bdmEscalationLevel = null;

    for (int i = 0; i < bdmExternalPartyTaskDetailsList.dtls.dtls.dtls
      .size(); i++) {

      bdmTaskSearchDetails = new BDMTaskSearchDetails();

      bdmTaskSearchDetails =
        bdmExternalPartyTaskDetailsList.dtls.dtls.dtls.get(i);

      taskKey = new TaskKey();
      caseUrgentFlagStringDetails = new CaseUrgentFlagStringDetails();
      taskKey.taskID = bdmTaskSearchDetails.taskID;

      caseUrgentFlagStringDetails =
        workspace.getCaseUrgentFlagsByTaskID(taskKey);

      bdmTaskSearchDetails.caseUrgentFlagStr =
        caseUrgentFlagStringDetails.caseUrgentFlagStr;

      bdmEscalationLevel = new BDMEscalationLevelString();
      bdmEscalationLevel =
        bdmMaintainSupervisorUsers.readEscalationLevelByTaskID(taskKey);

      bdmTaskSearchDetails.escalationLevelDesc =
        bdmEscalationLevel.escalationLevelDesc;

    }

    return bdmExternalPartyTaskDetailsList;
  }

  /**
   * This method cancels the external party office member.
   * Bug 99516: User should be prevented from deleting an Office member that was
   * specified as Third Party Contact
   *
   * @param officeMemberKey The ID of the external party office member.
   *
   * @throws InformationalException Generic Exception Signature.
   * @throws AppException Generic Exception Signature.
   */
  @Override
  public void
    cancelOfficeMember(final ExternalPartyOfficeMemberKey officeMemberKey)
      throws AppException, InformationalException {

    // custom validation
    validateCancelOfficeMember(officeMemberKey);

    // call code method
    ExternalPartyFactory.newInstance().cancelOfficeMember(officeMemberKey);

  }

  /**
   * Bug 99516: User should be prevented from deleting an Office member that was
   * specified as Third Party Contact
   *
   * @param officeMemberKey
   * @throws AppException
   * @throws InformationalException
   */
  public void validateCancelOfficeMember(
    final ExternalPartyOfficeMemberKey officeMemberKey)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final ExternalPartyOfficeMemberKey externalPartyOfficeMemberKey =
      new ExternalPartyOfficeMemberKey();

    externalPartyOfficeMemberKey.officeMemberID =
      officeMemberKey.officeMemberID;

    final ExternalPartyOfficeMemberDtls externalPartyOfficeMemberDtls =
      ExternalPartyOfficeMemberFactory.newInstance()
        .read(externalPartyOfficeMemberKey);

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID =
      externalPartyOfficeMemberDtls.concernRoleID;
    final ConcernRoleDtls concernRoleDtls =
      curam.core.fact.ConcernRoleFactory.newInstance().read(concernRoleKey);

    if (bdmUtil
      .isOfficeMemberThirdPartyContact(concernRoleDtls.concernRoleName)) {

      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_CANNOT_DELETE_OFFICE_MEMBER_WHO_IS_THIRD_PARTY_CONTACT),
          "", InformationalType.kError);

    }
    // Fail for business validations.
    informationalManager.failOperation();
  }

  @Override
  public InformationMsgDtlsList
    createOfficeAddress(final ExternalPartyOfficeAddressDetails details)
      throws AppException, InformationalException {

    // START TASK 118752 : Set the BDMUNPARSE to 1 for the INTL Address
    details.addressDtls.addressData = bdmUtil
      .setBDMUnparsedIndForINTLAddress(details.addressDtls.addressData);
    return ExternalPartyFactory.newInstance().createOfficeAddress(details);
    // END TASK 118752
  }

}
