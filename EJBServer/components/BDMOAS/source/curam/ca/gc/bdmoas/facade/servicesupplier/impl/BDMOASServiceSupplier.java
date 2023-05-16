package curam.ca.gc.bdmoas.facade.servicesupplier.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASSERVICESUPPLIERTYPE;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASModifyServSuppDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASReadServSuppDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASReadServSuppHomeDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASRegisterServiceSupplierState;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppRegistrationWizardSearchDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppSearchDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppSearchKey;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppSearchWizardKey;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppTypeDetails;
import curam.ca.gc.bdmoas.message.BDMOASSERVICESUPPLIER;
import curam.ca.gc.bdmoas.sl.servicesupplier.fact.BDMOASMaintainServiceSupplierFactory;
import curam.ca.gc.bdmoas.sl.servicesupplier.intf.BDMOASMaintainServiceSupplier;
import curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASReadServSuppTypeKey;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.COUNTRY;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.ServiceSupplierFactory;
import curam.core.facade.intf.ServiceSupplier;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.ModifyServiceSupplierReturnDetails;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.facade.struct.ReadServiceSupplierDetails;
import curam.core.facade.struct.ReadServiceSupplierDetailsKey;
import curam.core.facade.struct.ReadServiceSupplierHomeKey;
import curam.core.facade.struct.ReadServiceSupplierHomePageDetails;
import curam.core.facade.struct.ServiceSupplierRegistrationResult;
import curam.core.facade.struct.ServiceSupplierRegistrationWithTextBankAccountSortCodeDetails;
import curam.core.fact.AddressDataFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.AddressData;
import curam.core.intf.ConcernRoleAddress;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressConcernRoleTypeStatusDtlsList;
import curam.core.struct.AddressConcernRoleTypeStatusKey;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.AddressSearchKey;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.LayoutKey;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.transaction.TransactionInfo;
import curam.wizardpersistence.impl.WizardPersistentState;

/**
 *
 * Wrapper class to create, read and modify service supplier details.
 *
 */
public class BDMOASServiceSupplier extends
  curam.ca.gc.bdmoas.facade.servicesupplier.base.BDMOASServiceSupplier {

  /**
   * Method to read service supplier search criteria for the service supplier
   * registration.
   *
   * @param registrationDtls, wizardStateID, actionID, type
   *
   * @return Service Supplier Registration details.
   */
  @Override
  public ParticipantRegistrationWizardResult
    setRegisterServiceSupplierDetails(
      final ServiceSupplierRegistrationWithTextBankAccountSortCodeDetails registrationDtls,
      final WizardStateID stateID, final ActionIDProperty actionID,
      final BDMOASServSuppTypeDetails type)
      throws AppException, InformationalException {

    // OOTB CODE START:
    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    final WizardStateID wizardStateID = new WizardStateID();

    wizardStateID.assign(stateID);

    final ParticipantRegistrationWizardResult participantRegistrationWizardResult =
      new ParticipantRegistrationWizardResult();

    participantRegistrationWizardResult.wizardStateID = wizardStateID;

    if (actionID.actionIDProperty.equalsIgnoreCase(CuramConst.kSaveAction)) {
      final curam.core.facade.intf.ServiceSupplier serviceSupplierObj =
        ServiceSupplierFactory.newInstance();

      final ServiceSupplierRegistrationResult registrationResult =
        serviceSupplierObj
          .registerWithTextBankAccountSortCode(registrationDtls);

      participantRegistrationWizardResult.registrationResult =
        registrationResult.registrationIDDetails;

      wizardPersistentState.remove(stateID.wizardStateID);
      // OOTB CODE END:

      // BDMOAS CUSTOMIZATION START
      // Address maintenance object
      if (type.typeCode
        .equals(BDMOASSERVICESUPPLIERTYPE.BDMOASFEDERALINCARCERATIONINST)) {
        final ConcernRoleAddress concernRoleAddressObj =
          ConcernRoleAddressFactory.newInstance();
        AddressConcernRoleTypeStatusDtlsList addressConcernRoleTypeStatusDtlsList;

        final AddressConcernRoleTypeStatusKey addressConcernRoleTypeStatusKey =
          new AddressConcernRoleTypeStatusKey();
        addressConcernRoleTypeStatusKey.concernRoleID =
          participantRegistrationWizardResult.registrationResult.concernRoleID;
        addressConcernRoleTypeStatusKey.typeCode =
          CONCERNROLEADDRESSTYPE.BUSINESS;
        addressConcernRoleTypeStatusKey.statusCode = RECORDSTATUS.NORMAL;

        addressConcernRoleTypeStatusDtlsList =
          concernRoleAddressObj.searchAddressByConcernRoleTypeStatus(
            addressConcernRoleTypeStatusKey);
        // Assumption at this point is, there is always going to be record.

        final ConcernRoleAddressKey concernRoleAddressKey =
          new ConcernRoleAddressKey();
        concernRoleAddressKey.concernRoleAddressID =
          addressConcernRoleTypeStatusDtlsList.dtls
            .item(0).concernRoleAddressID;

        final ConcernRoleAddressDtls concernRoleAddressDtls =
          concernRoleAddressObj.read(concernRoleAddressKey);

        concernRoleAddressDtls.typeCode = CONCERNROLEADDRESSTYPE.MAILING;
        concernRoleAddressObj.modify(concernRoleAddressKey,
          concernRoleAddressDtls);
      }

      // Custom table modify.
      final BDMOASMaintainServiceSupplier maintainSrvcSuppObj =
        BDMOASMaintainServiceSupplierFactory.newInstance();

      final curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppTypeDetails servSuppTypeDetails =
        new curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppTypeDetails();
      servSuppTypeDetails.concernRoleID =
        participantRegistrationWizardResult.registrationResult.concernRoleID;
      servSuppTypeDetails.typeCode = type.typeCode;

      maintainSrvcSuppObj.createServiceSupplierType(servSuppTypeDetails);
      // BDMOAS CUSTOMIZATION END
    }

    return participantRegistrationWizardResult;
  }

  /**
   * Method to read service supplier details.
   *
   * @param key wizardStateID
   *
   * @return Service Supplier details.
   */
  @Override
  public BDMOASReadServSuppHomeDetails
    readHomePageDetails(final ReadServiceSupplierHomeKey key)
      throws AppException, InformationalException {

    final BDMOASReadServSuppHomeDetails readServSuppHomeDetails =
      new BDMOASReadServSuppHomeDetails();
    // Call to OOTB method.
    final ServiceSupplier srvcSuppObj = ServiceSupplierFactory.newInstance();

    final ReadServiceSupplierHomePageDetails srvcSuppHomePageDetails =
      srvcSuppObj.readHomePageDetails(key);
    readServSuppHomeDetails.srvcSuppDetails = srvcSuppHomePageDetails;

    // Call to custom function.
    final BDMOASMaintainServiceSupplier maintainSrvcSuppObj =
      BDMOASMaintainServiceSupplierFactory.newInstance();

    final BDMOASReadServSuppTypeKey servSuppTypeKey =
      new BDMOASReadServSuppTypeKey();

    servSuppTypeKey.concernRoleID = key.concernRoleHomePageKey.concernRoleID;

    final curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppTypeDetails servSuppTypeDetails =
      maintainSrvcSuppObj.readServiceSupplierType(servSuppTypeKey);
    readServSuppHomeDetails.typeCode = servSuppTypeDetails.typeCode;

    return readServSuppHomeDetails;
  }

  /**
   * Method to read service supplier search criteria for the service supplier
   * registration.
   *
   * @param key wizardStateID
   *
   * @return Service supplier modify details.
   */
  @Override
  public ModifyServiceSupplierReturnDetails
    modifyServiceSupplierDetails(final BDMOASModifyServSuppDetails details)
      throws AppException, InformationalException {

    final ServiceSupplier srvcSuppObj = ServiceSupplierFactory.newInstance();

    // Call to OOTB modify.
    final ModifyServiceSupplierReturnDetails modifySrvcSuppReturnDetails =
      srvcSuppObj.modifyServiceSupplierDetails(details.srvcSuppModifyDetails);

    // Call to custom modify.
    final BDMOASMaintainServiceSupplier maintainSrvcSuppObj =
      BDMOASMaintainServiceSupplierFactory.newInstance();

    final curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppTypeDetails servSuppTypeDetails =
      new curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppTypeDetails();
    servSuppTypeDetails.concernRoleID =
      details.srvcSuppModifyDetails.serviceSupplierDetails.concernRoleID;
    servSuppTypeDetails.typeCode = details.typeCode;

    maintainSrvcSuppObj.modifyServiceSupplierType(servSuppTypeDetails);

    return modifySrvcSuppReturnDetails;
  }

  /**
   * Method to read service supplier details.
   *
   * @param key wizardStateID
   *
   * @return Service Supplier details.
   */
  @Override
  public BDMOASReadServSuppDetails
    readServiceSupplierDetails(final ReadServiceSupplierDetailsKey key)
      throws AppException, InformationalException {

    final BDMOASReadServSuppDetails readServSuppDetails =
      new BDMOASReadServSuppDetails();
    // Call to OOTB method.
    final ServiceSupplier srvcSuppObj = ServiceSupplierFactory.newInstance();

    final ReadServiceSupplierDetails srvcSuppReadDetails =
      srvcSuppObj.readServiceSupplierDetails(key);
    readServSuppDetails.srvcSuppReadDetails = srvcSuppReadDetails;

    // Call to custom function.
    final BDMOASMaintainServiceSupplier maintainSrvcSuppObj =
      BDMOASMaintainServiceSupplierFactory.newInstance();

    final BDMOASReadServSuppTypeKey servSuppTypeKey =
      new BDMOASReadServSuppTypeKey();

    servSuppTypeKey.concernRoleID = key.maintainConcernRoleKey.concernRoleID;

    final curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppTypeDetails servSuppTypeDetails =
      maintainSrvcSuppObj.readServiceSupplierType(servSuppTypeKey);
    readServSuppDetails.typeCode = servSuppTypeDetails.typeCode;

    return readServSuppDetails;
  }

  /**
   * Get the Service Supplier Search Criteria details.
   *
   * @param wizardStateID The identifier for the serialized wizard persistence
   * struct.
   *
   * @return ParticipantSearchWizardKey
   */
  @Override
  public BDMOASServSuppSearchWizardKey
    getRegisterServiceSupplierSearchCriteria(
      final WizardStateID wizardStateID)
      throws AppException, InformationalException {

    final BDMOASServSuppSearchWizardKey participantSearchWizardKey =
      new BDMOASServSuppSearchWizardKey();

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    // If the wizard state is specified then read the record, otherwise
    // create a wizard record.
    if (wizardStateID.wizardStateID != 0) {

      final BDMOASRegisterServiceSupplierState registerServiceSupplierState =
        (BDMOASRegisterServiceSupplierState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      participantSearchWizardKey.searchKey =
        registerServiceSupplierState.searchKey.searchKey;
      participantSearchWizardKey.stateID.wizardStateID =
        wizardStateID.wizardStateID;

      participantSearchWizardKey.addressData = getAddressDataFromFields(
        participantSearchWizardKey.searchKey.addressDtls).addressData;
    } else {
      participantSearchWizardKey.stateID.wizardStateID = wizardPersistentState
        .create(new BDMOASRegisterServiceSupplierState());
    }

    return participantSearchWizardKey;
  }

  /**
   * Set the service supplier details for given service supplier search wizard.
   *
   * @param participantSearchWizardKey contains service supplier search key
   * @param wizardStateID contains wizard state Id
   * @param actionIDProperty contains action Id property details
   *
   * @return participant service supplier details
   */
  @Override
  public BDMOASServSuppRegistrationWizardSearchDetails
    setRegisterServiceSupplierSearchCriteriaDetails(
      final BDMOASServSuppSearchWizardKey participantSearchWizardKey,
      final WizardStateID wizardStateID,
      final ActionIDProperty actionIDProperty)
      throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    final WizardStateID wizardStateIDObj = new WizardStateID();

    wizardStateIDObj.assign(wizardStateID);

    final BDMOASServSuppRegistrationWizardSearchDetails servSuppRegistrationWizardSearchDetails =
      new BDMOASServSuppRegistrationWizardSearchDetails();
    BDMOASServSuppSearchKey participantSearchKey = null;

    servSuppRegistrationWizardSearchDetails.wizardStateID = wizardStateID;

    if (CuramConst.kNextPageAction
      .equalsIgnoreCase(actionIDProperty.actionIDProperty)) {

      final BDMOASRegisterServiceSupplierState registerServiceSupplierState =
        (BDMOASRegisterServiceSupplierState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      registerServiceSupplierState.searchKey = participantSearchWizardKey;
      registerServiceSupplierState.searchKey.stateID = wizardStateID;

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        registerServiceSupplierState);
    } else if (CuramConst.kSearchAction
      .equalsIgnoreCase(actionIDProperty.actionIDProperty)) {
      participantSearchKey = new BDMOASServSuppSearchKey();
      participantSearchKey.key = participantSearchWizardKey.searchKey;

      // search service supplier.
      servSuppRegistrationWizardSearchDetails.searchResult =
        searchServiceSupplier(participantSearchKey);
    } else if (CuramConst.kResetAction
      .equalsIgnoreCase(actionIDProperty.actionIDProperty)) {

      final BDMOASRegisterServiceSupplierState registerServiceSupplierState =
        (BDMOASRegisterServiceSupplierState) wizardPersistentState
          .read(wizardStateID.wizardStateID);

      registerServiceSupplierState.searchKey =
        new BDMOASServSuppSearchWizardKey();

      wizardPersistentState.modify(wizardStateID.wizardStateID,
        registerServiceSupplierState);
    }

    return servSuppRegistrationWizardSearchDetails;
  }

  /**
   * Method to search the service suppliers.
   *
   * @param Service supplier search key
   *
   * @return Service Supplier search details.
   */
  @Override
  public BDMOASServSuppSearchDetails
    searchServiceSupplier(final BDMOASServSuppSearchKey searchKey)
      throws AppException, InformationalException {

    // Validations
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();
    searchKey.key.referenceNumber = searchKey.key.referenceNumber.trim();
    searchKey.key.concernRoleName = searchKey.key.concernRoleName.trim();
    searchKey.key.addressDtls.city = searchKey.key.addressDtls.city.trim();
    searchKey.key.addressDtls.addressLine1 =
      searchKey.key.addressDtls.addressLine1.trim();
    searchKey.key.addressDtls.addressLine2 =
      searchKey.key.addressDtls.addressLine2.trim();
    searchKey.key.stateProvince = searchKey.key.stateProvince.trim();
    searchKey.key.postalCode = searchKey.key.postalCode.trim();
    searchKey.key.poBox = searchKey.key.poBox.trim();

    if (searchKey.key.referenceNumber.isEmpty()
      && searchKey.key.concernRoleName.isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMOASSERVICESUPPLIER.ERR_SEARCH_MANDATORY_MISSING),
        "", InformationalType.kError);
    }

    if (!searchKey.key.addressDtls.city.isEmpty()
      && searchKey.key.addressDtls.city.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMOASSERVICESUPPLIER.ERR_SEARCH_CITY), "",
        InformationalType.kError);
    }

    if (!searchKey.key.addressDtls.addressLine1.isEmpty()
      && searchKey.key.addressDtls.addressLine1.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMOASSERVICESUPPLIER.ERR_SEARCH_STREET_NUMBER_OR_STREET_ONE),
        "", InformationalType.kError);
    }

    if (!searchKey.key.addressDtls.addressLine2.isEmpty()
      && searchKey.key.addressDtls.addressLine2.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMOASSERVICESUPPLIER.ERR_SEARCH_STREET_NAME_OR_STREET_TWO),
        "", InformationalType.kError);
    }

    if (!searchKey.key.stateProvince.isEmpty()
      && searchKey.key.stateProvince.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMOASSERVICESUPPLIER.ERR_SEARCH_PROVINCE), "",
        InformationalType.kError);
    }

    if (!searchKey.key.postalCode.isEmpty()
      && searchKey.key.postalCode.length() < 3) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMOASSERVICESUPPLIER.ERR_SEARCH_POSTALCODE), "",
        InformationalType.kError);
    }

    if (!searchKey.key.poBox.isEmpty() && searchKey.key.poBox.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMOASSERVICESUPPLIER.ERR_SEARCH_PO_RESIDENTIAL), "",
        InformationalType.kError);
    }

    informationalManager.failOperation();

    final BDMOASMaintainServiceSupplier maintainSrvcSuppObj =
      BDMOASMaintainServiceSupplierFactory.newInstance();

    final BDMOASServSuppSearchDetails servSuppSearchDetails =
      maintainSrvcSuppObj.searchServiceSupplier(searchKey);

    return servSuppSearchDetails;
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

}
