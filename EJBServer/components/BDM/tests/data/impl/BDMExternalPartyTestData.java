package data.impl;

import curam.ca.gc.bdm.address.impl.BDMAddressFormatINTL;
import curam.ca.gc.bdm.codetable.BDMCOSLOCATION;
import curam.ca.gc.bdm.codetable.BDMSTATUSOFAGREEMENT;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.facade.externalparty.fact.BDMExternalPartyFactory;
import curam.ca.gc.bdm.facade.externalparty.intf.BDMExternalParty;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyRegistrationDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartySearchWizardKey;
import curam.ca.gc.bdm.test.data.impl.BDMExternalPartyTestDataDetails;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.facade.struct.ExternalPartyRegistrationDetails;
import curam.core.facade.struct.ParticipantRegistrationWizardResult;
import curam.core.impl.CuramConst;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressFieldDetails;
import curam.core.struct.OtherAddressData;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;

public class BDMExternalPartyTestData {

  final BDMExternalParty bdmExtPartyObj =
    BDMExternalPartyFactory.newInstance();

  public static final String kExtPartyName = "United States";

  /**
   * Method to create the wizard state for registering the external party.
   *
   * @return External Party test data details.
   */
  public BDMExternalPartyTestDataDetails
    getRegisterExtPartySearchCriteria_Empty()
      throws AppException, InformationalException {

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      new BDMExternalPartyTestDataDetails();

    final WizardStateID wizardStateID = new WizardStateID();

    // Pass just empty object and it should create wizard and return the wizard
    // state ID.
    final BDMExternalPartySearchWizardKey wizardKey =
      bdmExtPartyObj.getRegisterExternalPartySearchCriteria(wizardStateID);

    extPartyTestDataDetails.wizardStateID =
      wizardKey.key.stateID.wizardStateID;

    return extPartyTestDataDetails;
  }

  /**
   * Method to get the external party search criteria on the registration
   * wizard.
   *
   * @return wizard key.
   */
  public BDMExternalPartySearchWizardKey
    getRegisterExtPartySearchCriteria(final WizardStateID ipWizardStateID)
      throws AppException, InformationalException {

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = ipWizardStateID.wizardStateID;

    final BDMExternalPartySearchWizardKey wizardKey =
      bdmExtPartyObj.getRegisterExternalPartySearchCriteria(wizardStateID);

    return wizardKey;
  }

  /**
   * Method to set the external party search criteria details on the
   * registration wizard.
   *
   * @return external party test data details.
   */
  public BDMExternalPartyTestDataDetails
    setRegisterServiceSupplierSearchCriteriaDetails_Next()
      throws AppException, InformationalException {

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      this.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kNextPageAction;

    final BDMExternalPartySearchWizardKey searchWizardKey =
      new BDMExternalPartySearchWizardKey();
    searchWizardKey.key.searchKey.key.key.concernRoleName = kExtPartyName;

    searchWizardKey.key.searchKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;

    bdmExtPartyObj.setRegisterExternalPartySearchCriteriaDetails(
      searchWizardKey, wizardStateID, actionIDProperty);

    return extPartyTestDataDetails;
  }

  /**
   * Method to register the external party.
   *
   * @return external party test data details.
   * @throws AppException
   * @throws InformationalException
   */
  public BDMExternalPartyTestDataDetails setRegisterExternalPartyDetails()
    throws AppException, InformationalException {

    final BDMExternalPartyTestDataDetails extPartyTestDataDetails =
      this.getRegisterExtPartySearchCriteria_Empty();

    final WizardStateID wizardStateID = new WizardStateID();
    wizardStateID.wizardStateID = extPartyTestDataDetails.wizardStateID;

    final ActionIDProperty actionIDProperty = new ActionIDProperty();
    actionIDProperty.actionIDProperty = CuramConst.kSaveAction;

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      this.getExternalPartyRegistrationCustomDetails();

    final ExternalPartyRegistrationDetails registrationDtls =
      this.getExternalPartyRegistrationDetails_Default();

    extPartyTestDataDetails.extPartyName =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name;

    final ParticipantRegistrationWizardResult wizardResult =
      bdmExtPartyObj.setRegisterExternalPartyDetails(registrationDtls,
        wizardStateID, actionIDProperty, bdmextPartyRegDetails);

    extPartyTestDataDetails.extPartyCRoleID =
      wizardResult.registrationResult.concernRoleID;

    return extPartyTestDataDetails;
  }

  /**
   * Method to get the registration details for registering the external party.
   *
   * @return Returns registration details for default registration
   */
  public ExternalPartyRegistrationDetails
    getExternalPartyRegistrationDetails_Default()
      throws AppException, InformationalException {

    final ExternalPartyRegistrationDetails extPartyRegistrationDetails =
      new ExternalPartyRegistrationDetails();

    extPartyRegistrationDetails.externalPartyRegistrationDetails.externalPartyDtls.name =
      "Angola";

    extPartyRegistrationDetails.externalPartyRegistrationDetails.externalPartyDtls.type =
      EXTERNALPARTYTYPE.SSACOUNTRY;
    extPartyRegistrationDetails.externalPartyRegistrationDetails.registrationDate =
      Date.getCurrentDate();

    final OtherAddressData otherAddressData =
      this.getInternationAddressForExtParty();

    extPartyRegistrationDetails.externalPartyRegistrationDetails.addressData =
      otherAddressData.addressData;

    return extPartyRegistrationDetails;
  }

  /**
   * Method to get the registration details for registering the external party.
   *
   * @return Returns registration details for default registration
   */
  public BDMExternalPartyRegistrationDetails
    getExternalPartyRegistrationCustomDetails()
      throws AppException, InformationalException {

    final BDMExternalPartyRegistrationDetails bdmextPartyRegDetails =
      new BDMExternalPartyRegistrationDetails();
    bdmextPartyRegDetails.centerOfSpeclizn = BDMCOSLOCATION.ALBERTA;
    bdmextPartyRegDetails.totalizatnCountry = BDMYESNO.YES;
    bdmextPartyRegDetails.isProtectiveDate = BDMYESNO.YES;
    bdmextPartyRegDetails.statusOfAgreement = BDMSTATUSOFAGREEMENT.INFORCE;
    bdmextPartyRegDetails.ssaLink =
      "https://www.canada.ca/en/employment-social-development.html";

    return bdmextPartyRegDetails;
  }

  /**
   * Method to get the address data for registering the service supplier.
   *
   * @return Returns address data.
   */
  public OtherAddressData getInternationAddressForExtParty()
    throws AppException, InformationalException {

    final AddressFieldDetails addressFieldDetails = new AddressFieldDetails();
    final BDMAddressFormatINTL bdmAddressFormatINTLobj =
      new BDMAddressFormatINTL();
    addressFieldDetails.addressLayoutType =
      curam.codetable.ADDRESSLAYOUTTYPE.BDMINTL;
    addressFieldDetails.suiteNum = "116";
    addressFieldDetails.addressLine1 = "8614";
    addressFieldDetails.addressLine2 = "Catalpa Ave";
    addressFieldDetails.stateProvince = "IL";
    addressFieldDetails.city = "Chicago";
    addressFieldDetails.zipCode = "60656";
    addressFieldDetails.countryCode = "AO";
    final OtherAddressData otherAddressData =
      bdmAddressFormatINTLobj.parseFieldsToData(addressFieldDetails);
    return otherAddressData;
  }

}
