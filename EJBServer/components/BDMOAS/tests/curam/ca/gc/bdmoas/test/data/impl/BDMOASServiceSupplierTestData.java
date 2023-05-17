package curam.ca.gc.bdmoas.test.data.impl;

import curam.ca.gc.bdmoas.codetable.BDMOASSERVICESUPPLIERTYPE;
import curam.ca.gc.bdmoas.facade.servicesupplier.fact.BDMOASServiceSupplierFactory;
import curam.ca.gc.bdmoas.facade.servicesupplier.intf.BDMOASServiceSupplier;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppSearchWizardKey;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppTypeDetails;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.facade.struct.ServiceSupplierRegistrationWithTextBankAccountSortCodeDetails;
import curam.core.impl.CuramConst;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.DateTime;

public class BDMOASServiceSupplierTestData {

  public static final String kServSuppName = "TestServSupp";

  /**
   * Get current date time string to append in to the names, so that service
   * supplier
   * registration will always have unique name.
   *
   * @return date time string.
   */
  public String getCurrentDateTimeString()
    throws AppException, InformationalException {

    final String dateTimeStr =
      DateTime.getCurrentDateTime().toString().replaceAll(":", "")
        .replaceAll("-", "").replaceAll("/", "").replaceAll(" ", "");

    return dateTimeStr;
  }

  /**
   * Method to create the wizard state for registering the service supplier.
   *
   * @return Service Supplier test data details.
   */
  public BDMOASServiceSupplierTestDataDetails
    getRegisterServiceSupplierSearchCriteria_Empty()
      throws AppException, InformationalException {

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      new BDMOASServiceSupplierTestDataDetails();

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final WizardStateID wizardStateID = new WizardStateID();

    // Pass just empty object and it should create wizard and return the wizard
    // state ID.
    final BDMOASServSuppSearchWizardKey wizardKey = bdmOASServSupObj
      .getRegisterServiceSupplierSearchCriteria(wizardStateID);

    servSuppTestDataDetails.wizardStateID = wizardKey.stateID.wizardStateID;

    return servSuppTestDataDetails;
  }

  /**
   * Method to get the service supplier search criteria on the registration
   * wizard.
   *
   * @return wizard key.
   */
  public BDMOASServSuppSearchWizardKey
    getRegisterServiceSupplierSearchCriteria(
      final WizardStateID ipWizardStateID)
      throws AppException, InformationalException {

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = ipWizardStateID.wizardStateID;

    final BDMOASServSuppSearchWizardKey wizardKey = bdmOASServSupObj
      .getRegisterServiceSupplierSearchCriteria(wizardStateID);

    return wizardKey;
  }

  /**
   * Method to set the service supplier search criteria details on the
   * registration
   * wizard.
   *
   * @return Service Supplier test data details.
   */
  public BDMOASServiceSupplierTestDataDetails
    setRegisterServiceSupplierSearchCriteriaDetails_Next()
      throws AppException, InformationalException {

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      this.getRegisterServiceSupplierSearchCriteria_Empty();

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = servSuppTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kNextPageAction;

    final String dateTimeStr = this.getCurrentDateTimeString();

    final BDMOASServSuppSearchWizardKey searchWizardKey =
      new BDMOASServSuppSearchWizardKey();
    searchWizardKey.searchKey.concernRoleName = kServSuppName + dateTimeStr;
    searchWizardKey.searchKey.typeCode =
      BDMOASSERVICESUPPLIERTYPE.BDMOASFEDERALINCARCERATIONINST;

    bdmOASServSupObj.setRegisterServiceSupplierSearchCriteriaDetails(
      searchWizardKey, wizardStateID, actionIDProperty);

    return servSuppTestDataDetails;
  }

  /**
   * Method to register the service supplier.
   *
   * @return Service Supplier test data details.
   * @throws AppException
   * @throws InformationalException
   */
  public BDMOASServiceSupplierTestDataDetails setRegisterServiceSupplierDetails()
    throws AppException, InformationalException {

    final BDMOASServiceSupplierTestData servSuppTestDataObj =
      new BDMOASServiceSupplierTestData();

    final BDMOASServiceSupplier bdmOASServSupObj =
      BDMOASServiceSupplierFactory.newInstance();

    final BDMOASServiceSupplierTestDataDetails servSuppTestDataDetails =
      servSuppTestDataObj.getRegisterServiceSupplierSearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = servSuppTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMOASServSuppTypeDetails type = new BDMOASServSuppTypeDetails();
    type.typeCode = BDMOASSERVICESUPPLIERTYPE.BDMOASFEDERALINCARCERATIONINST;

    final ServiceSupplierRegistrationWithTextBankAccountSortCodeDetails registrationDtls =
      servSuppTestDataObj.getServiceSupplierRegistrationDetails_Dafault();

    servSuppTestDataDetails.kServSuppName =
      registrationDtls.servSuppRegistrationDetails.name;

    final ParticipantRegistrationWizardResult wizardResult =
      bdmOASServSupObj.setRegisterServiceSupplierDetails(registrationDtls,
        wizardStateID, actionIDProperty, type);

    servSuppTestDataDetails.servSuppCRoleID =
      wizardResult.registrationResult.concernRoleID;

    return servSuppTestDataDetails;
  }

  /**
   * Method to get the registration details for registering the sercice
   * supplier.
   *
   * @return Returns registration details for default registration
   */
  public ServiceSupplierRegistrationWithTextBankAccountSortCodeDetails
    getServiceSupplierRegistrationDetails_Dafault()
      throws AppException, InformationalException {

    final ServiceSupplierRegistrationWithTextBankAccountSortCodeDetails servSuppRegistrationDetails =
      new ServiceSupplierRegistrationWithTextBankAccountSortCodeDetails();

    final String dateTimeStr = this.getCurrentDateTimeString();

    servSuppRegistrationDetails.servSuppRegistrationDetails.name =
      kServSuppName + dateTimeStr;
    servSuppRegistrationDetails.servSuppRegistrationDetails.registrationDate =
      Date.getCurrentDate();

    final OtherAddressData otherAddressData = this.getHomeAddress();

    servSuppRegistrationDetails.servSuppRegistrationDetails.addressData =
      otherAddressData.addressData;

    return servSuppRegistrationDetails;
  }

  /**
   * Method to get the address data for registering the service supplier.
   *
   * @return Returns address data.
   */
  public OtherAddressData getHomeAddress()
    throws AppException, InformationalException {

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.CA;
    addressFieldDetails.suiteNum = "116";
    addressFieldDetails.addressLine1 = "55 Ellerslie Ave";
    addressFieldDetails.addressLine2 = "North West";
    addressFieldDetails.province = PROVINCETYPE.ONTARIO;
    addressFieldDetails.city = "Toronto";
    addressFieldDetails.postalCode = "M2N 1X9";
    addressFieldDetails.countryCode = curam.codetable.COUNTRY.CA;
    final OtherAddressData otherAddressData =
      addressDataObj.parseFieldsToData(addressFieldDetails);
    return otherAddressData;
  }

}
