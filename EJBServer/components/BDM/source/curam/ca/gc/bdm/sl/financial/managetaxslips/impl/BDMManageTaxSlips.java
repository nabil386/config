package curam.ca.gc.bdm.sl.financial.managetaxslips.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMALERTCHOICE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPATTRDOMAINTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPATTRTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPCONFIGSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPCREATIONMETHOD;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPFORMTYPE;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPPROCSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPSTATUS;
import curam.ca.gc.bdm.codetable.BDMTAXSLIPTYPE;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipAttrConfigFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipAttrDataFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipConfigFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataRL1Factory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipDataT4AFactory;
import curam.ca.gc.bdm.entity.financial.fact.BDMTaxSlipProviderConfigFactory;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipAttrConfig;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipAttrData;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataRL1;
import curam.ca.gc.bdm.entity.financial.intf.BDMTaxSlipDataT4A;
import curam.ca.gc.bdm.entity.financial.struct.BDMConcernRoleYearStatusKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMLatestTaxSlipByYearStatusKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMModifyRL1TaxSlipStatusDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMModifyRL1TaxSlipStatusKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMModifyT4ATaxSlipStatusDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMModifyT4ATaxSlipStatusKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMNextTaxSlipDataKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMNextTaxSlipDataVersionNoDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMSpecificAttributeTextKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMSpecificAttributeValueKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipAttrConfigDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipAttrConfigDtlsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipAttrConfigKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipAttrDataDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipAttrDataDtlsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipAttrDataKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipAttrDataKeyStruct1;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipAttrDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipAttrDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipConfigKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipConfigRecordStatusKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Dtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataRL1Key;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4ADtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataT4AKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipDataVersionKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipHistoryDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipHistoryDetailsList;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipHistoryKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipNearestConfigKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipNearestProviderConfigKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipProviderConfigDtls;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipProviderConfigKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMTaxSlipsForDisplayKey;
import curam.ca.gc.bdm.entity.financial.struct.BDMViewTaxSlipDetails;
import curam.ca.gc.bdm.entity.struct.BDMTaxSlipMRQBatchSequenceKeyStruct1;
import curam.ca.gc.bdm.facade.struct.BDMEditTaxSlipDetails;
import curam.ca.gc.bdm.facade.struct.BDMEditTaxSlipDisplayDetails;
import curam.ca.gc.bdm.facade.struct.BDMReadVersionNo;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipDataIDVersionNoKey;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipDataKey;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipEditAttrDetails;
import curam.ca.gc.bdm.facade.struct.BDMTaxSlipHistoryList;
import curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetailsList;
import curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipInlineDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMTAXSLIPS;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipClientDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipDataForPrint;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipDataForPrintKey;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipPrintAttrDetails;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipStoreAttrDataDuplKey;
import curam.ca.gc.bdm.sl.financial.struct.BDMTaxSlipStoreAttrDataKey;
import curam.ca.gc.bdm.sl.financial.struct.BDMUpdateNextTaxSlipKey;
import curam.ca.gc.bdm.sl.interfaces.bdmtaxslipmrq.impl.BDMTaxSlipMrqBatchImpl;
import curam.ca.gc.bdm.util.payment.impl.BDMPaymentUtil;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.LANGUAGE;
import curam.codetable.LOCALE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.contextviewer.constant.impl.CuramConst;
import curam.core.facade.fact.ConcernRoleFactory;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.facade.infrastructure.struct.SearchParticipantEvidenceByRangeKey;
import curam.core.facade.struct.ActionIDProperty;
import curam.core.fact.AddressDataFactory;
import curam.core.intf.AddressData;
import curam.core.sl.infrastructure.entity.struct.StartDateEndDate;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceReadDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ElementDetails;
import curam.core.struct.OtherAddressData;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.struct.PDCEvidenceDetails;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.piwrapper.impl.AddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.AppRuntimeException;
import curam.util.exception.DatabaseException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.CodeTableItemIdentifier;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Money;
import curam.util.type.NotFoundIndicator;
import curam.util.type.UniqueID;
import curam.workspaceservices.localization.impl.LocalizableTextHandlerDAO;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.time.DateFormatUtils;

public class BDMManageTaxSlips implements
  curam.ca.gc.bdm.sl.financial.managetaxslips.intf.BDMManageTaxSlips {

  // character limits for T4A tax slips

  private static final int kT4AFirstNameCharLimit = 12;

  private static final int kT4ALastNameCharLimit = 20;

  private static final int kT4AInitialCharLimit = 1;

  private static final int kT4AAddLine1CharLimit = 30;

  private static final int kT4AAddLine2CharLimit = 30;

  private static final int kT4ACityCharLimit = 28;

  // character limits for RL1 tax slips

  private static final int kRL1FirstNameCharLimit = 30;

  private static final int kRL1LastNameCharLimit = 30;

  private static final int kRL1InitialCharLimit = 1;

  private static final int kRL1AddLine1CharLimit = 30;

  private static final int kRL1CityCharLimit = 28;

  private static final int kRL1ProvinceCharLimit = 20;

  private static final int kRL1PostalCodeCharLimit = 6;

  @Inject
  private LocalizableTextHandlerDAO localizableTextHandlerDAO;

  public BDMManageTaxSlips() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Gets the current evidences for the client Name, SIN, and Mailing Address to
   * populate the client details for tax slips
   *
   * @param clientDetails
   * @param concernRoleID
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMTaxSlipClientDetails
    populateClientDetails(final long concernRoleID, final Date yearEndDate)
      throws AppException, InformationalException {

    final BDMTaxSlipClientDetails clientDetails =
      new BDMTaxSlipClientDetails();

    // check for quebec address at the end of the tax year
    clientDetails.quebecAddressInd =
      checkQuebecAddressForEndOfTaxYear(concernRoleID, yearEndDate);

    // find name evidence
    final PersonAndEvidenceTypeList evidenceKey =
      new PersonAndEvidenceTypeList();
    evidenceKey.concernRoleID = concernRoleID;
    evidenceKey.evidenceTypeList = CASEEVIDENCE.NAMES;

    final List<DynamicEvidenceDataDetails> nameEvidences =
      getListOfCurrentEvidenceDetails(evidenceKey);

    for (final DynamicEvidenceDataDetails name : nameEvidences) {
      // only use the registered name to populate name fields - there should
      // only be one valid evidence
      if (name.getAttribute(BDMConstants.kEvidenceAttrNameType).getValue()
        .equals(ALTERNATENAMETYPE.REGISTERED)) {

        // set name details
        clientDetails.recipientFirstName = name
          .getAttribute(BDMConstants.kEvidenceAttrNameFirstName).getValue();
        clientDetails.recipientSurName = name
          .getAttribute(BDMConstants.kEvidenceAttrNameLastName).getValue();
        clientDetails.recipientInitial = name
          .getAttribute(BDMConstants.kEvidenceAttrNameMiddleName).getValue();
      }
    }

    // find identification evidence
    evidenceKey.evidenceTypeList = CASEEVIDENCE.IDENTIFICATIONS;

    final List<DynamicEvidenceDataDetails> identificationEvidences =
      getListOfCurrentEvidenceDetails(evidenceKey);

    for (final DynamicEvidenceDataDetails identification : identificationEvidences) {
      // only get the SIN evidence
      if (identification
        .getAttribute(BDMConstants.kEvidenceAttrIdentificationType).getValue()
        .equals(CONCERNROLEALTERNATEID.SOCIAL_INSURANCE_NUMBER)) {
        clientDetails.recipientSIN = identification
          .getAttribute(BDMConstants.kEvidenceAttrIdentificationAlternateID)
          .getValue();
      }
    }

    // find address evidence
    evidenceKey.evidenceTypeList = CASEEVIDENCE.BDMADDRESS;

    final List<DynamicEvidenceDataDetails> addressEvidences =
      getListOfCurrentEvidenceDetails(evidenceKey);

    final AddressDAO addressDAO =
      GuiceWrapper.getInjector().getInstance(AddressDAO.class);
    final AddressData addressDataObj = AddressDataFactory.newInstance();
    for (final DynamicEvidenceDataDetails addressEv : addressEvidences) {
      // get the preferred mailing address
      if (addressEv.getAttribute(BDMConstants.kEvidenceAttrAddressType)
        .getValue().equals(CONCERNROLEADDRESSTYPE.MAILING)) {

        // get the address data
        final long addressID = Long.parseLong(addressEv
          .getAttribute(BDMConstants.kEvidenceAttrAddressID).getValue());
        final OtherAddressData otherAddressData = new OtherAddressData();
        otherAddressData.addressData =
          addressDAO.get(addressID).getAddressData();

        // convert address data into a map
        final AddressMapList addressMapList =
          addressDataObj.parseDataToMap(otherAddressData);

        // look for the elements and assign them to the correct fields
        final AddressMap addressMap = new AddressMap();

        addressMap.name = ADDRESSELEMENTTYPE.APT;
        ElementDetails addressElem =
          addressDataObj.findElement(addressMapList, addressMap);
        clientDetails.recipientApt = addressElem.elementValue;

        addressMap.name = ADDRESSELEMENTTYPE.LINE1;
        addressElem = addressDataObj.findElement(addressMapList, addressMap);
        clientDetails.recipientAddressLine1 = addressElem.elementValue;

        addressMap.name = ADDRESSELEMENTTYPE.LINE2;
        addressElem = addressDataObj.findElement(addressMapList, addressMap);
        clientDetails.recipientAddressLine2 = addressElem.elementValue;

        addressMap.name = ADDRESSELEMENTTYPE.POBOXNO;
        addressElem = addressDataObj.findElement(addressMapList, addressMap);
        clientDetails.recipientPOBox = addressElem.elementValue;

        addressMap.name = ADDRESSELEMENTTYPE.CITY;
        addressElem = addressDataObj.findElement(addressMapList, addressMap);
        clientDetails.recipientCity = addressElem.elementValue;

        addressMap.name = ADDRESSELEMENTTYPE.PROVINCE;
        addressElem = addressDataObj.findElement(addressMapList, addressMap);
        clientDetails.recipientProv = addressElem.elementValue;

        addressMap.name = ADDRESSELEMENTTYPE.COUNTRY;
        addressElem = addressDataObj.findElement(addressMapList, addressMap);
        clientDetails.recipientCountryCode = addressElem.elementValue;

        addressMap.name = ADDRESSELEMENTTYPE.POSTCODE;
        addressElem = addressDataObj.findElement(addressMapList, addressMap);
        clientDetails.recipientPostalCode = addressElem.elementValue;

        // format t4a line 1

        // add apartment if it exists
        if (!StringUtil.isNullOrEmpty(clientDetails.recipientApt)) {
          clientDetails.recipientAddressLine1_T4A =
            clientDetails.recipientApt + curam.core.impl.CuramConst.gkDash;
        }

        clientDetails.recipientAddressLine1_T4A =
          (clientDetails.recipientAddressLine1_T4A
            + clientDetails.recipientAddressLine1
            + curam.core.impl.CuramConst.gkSpace
            + clientDetails.recipientAddressLine2).trim();

        // format t4a line 2
        clientDetails.recipientAddressLine2_T4A =
          clientDetails.recipientPOBox;

        // format rl1 line 1

        // add apartment if it exists
        if (!StringUtil.isNullOrEmpty(clientDetails.recipientApt)) {
          clientDetails.recipientAddressLine1_RL1 =
            clientDetails.recipientApt + curam.core.impl.CuramConst.gkDash;
        }
        clientDetails.recipientAddressLine1_RL1 =
          (clientDetails.recipientAddressLine1_RL1
            + clientDetails.recipientAddressLine1
            + curam.core.impl.CuramConst.gkSpace
            + clientDetails.recipientAddressLine2
            + curam.core.impl.CuramConst.gkSpace
            + clientDetails.recipientPOBox).trim();

      }
    }

    // determine indian status
    evidenceKey.evidenceTypeList = CASEEVIDENCE.BDMTAX;

    final List<DynamicEvidenceDataDetails> taxEvidences =
      getListOfCurrentEvidenceDetails(evidenceKey);

    for (final DynamicEvidenceDataDetails tax : taxEvidences) {
      // find the native status
      final String nativeStatusStr = tax
        .getAttribute(BDMConstants.kEvidenceAttrTaxNativeStatus).getValue();

      if (BDMALERTCHOICE.YES.equals(nativeStatusStr)) {
        clientDetails.statusIndianInd = true;
      }

    }

    return clientDetails;

  }

  /**
   * Given an evidence key, retrieves a list of evidence details that are
   * currently active for the concern role
   *
   * @param evidenceKey
   * @throws AppException
   * @throws InformationalException
   */
  private List<DynamicEvidenceDataDetails> getListOfCurrentEvidenceDetails(
    final PersonAndEvidenceTypeList evidenceKey)
    throws AppException, InformationalException {

    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    // get list of all current evidences of the given type for a person
    final PDCEvidenceDetailsList currentEvidenceList = PDCPersonFactory
      .newInstance().listCurrentParticipantEvidenceByTypes(evidenceKey);

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    final List<DynamicEvidenceDataDetails> evidenceDetailsList =
      new ArrayList<>();
    for (final PDCEvidenceDetails currentEvidence : currentEvidenceList.list) {

      // get the details for the evidence
      eiEvidenceKey.evidenceType = currentEvidence.evidenceType;
      eiEvidenceKey.evidenceID = currentEvidence.evidenceID;
      final EIEvidenceReadDtls evidenceDtls =
        evidenceController.readEvidence(eiEvidenceKey);
      final DynamicEvidenceDataDetails dynamicEvidenceDetails =
        (DynamicEvidenceDataDetails) evidenceDtls.evidenceObject;
      evidenceDetailsList.add(dynamicEvidenceDetails);
    }

    return evidenceDetailsList;
  }

  /**
   * Formats t4a client details according to the character limits for each field
   *
   * @param t4aDtls
   */
  @Override
  public void formatT4AClientDetails(final BDMTaxSlipDataT4ADtls t4aDtls) {

    if (t4aDtls.recipientSurName.length() > kT4ALastNameCharLimit) {
      t4aDtls.recipientSurName =
        t4aDtls.recipientSurName.substring(0, kT4ALastNameCharLimit);
    }
    if (t4aDtls.recipientFirstName.length() > kT4AFirstNameCharLimit) {
      t4aDtls.recipientFirstName =
        t4aDtls.recipientFirstName.substring(0, kT4AFirstNameCharLimit);
    }
    if (t4aDtls.recipientInitial.length() > kT4AInitialCharLimit) {
      t4aDtls.recipientInitial =
        t4aDtls.recipientInitial.substring(0, kT4AInitialCharLimit);
    }
    if (t4aDtls.recipientAddressLine1.length() > kT4AAddLine1CharLimit) {
      t4aDtls.recipientAddressLine1 =
        t4aDtls.recipientAddressLine1.substring(0, kT4AAddLine1CharLimit);
    }
    if (t4aDtls.recipientAddressLine2.length() > kT4AAddLine2CharLimit) {
      t4aDtls.recipientAddressLine2 =
        t4aDtls.recipientAddressLine2.substring(0, kT4AAddLine2CharLimit);
    }
    if (t4aDtls.recipientCity.length() > kT4ACityCharLimit) {
      t4aDtls.recipientCity =
        t4aDtls.recipientCity.substring(0, kT4ACityCharLimit);
    }
  }

  /**
   * Formats t4a client details according to the character limits for each field
   *
   * @param t4aDtls
   */
  @Override
  public void formatRL1ClientDetails(final BDMTaxSlipDataRL1Dtls rl1Dtls,
    final long concernRoleID) throws AppException, InformationalException {

    // make sure all fields fit the character limit
    if (rl1Dtls.recipientLastName.length() > kRL1LastNameCharLimit) {
      rl1Dtls.recipientLastName =
        rl1Dtls.recipientLastName.substring(0, kRL1LastNameCharLimit);
    }
    if (rl1Dtls.recipientFirstName.length() > kRL1FirstNameCharLimit) {
      rl1Dtls.recipientFirstName =
        rl1Dtls.recipientFirstName.substring(0, kRL1FirstNameCharLimit);
    }
    if (rl1Dtls.recipientInitial.length() > kRL1InitialCharLimit) {
      rl1Dtls.recipientInitial =
        rl1Dtls.recipientInitial.substring(0, kRL1InitialCharLimit);
    }
    if (rl1Dtls.recipientAddressLine1.length() > kRL1AddLine1CharLimit) {
      rl1Dtls.recipientAddressLine1 =
        rl1Dtls.recipientAddressLine1.substring(0, kRL1AddLine1CharLimit);
    }
    if (rl1Dtls.recipientCity.length() > kRL1CityCharLimit) {
      rl1Dtls.recipientCity =
        rl1Dtls.recipientCity.substring(0, kRL1CityCharLimit);
    }
    // get concern role details to determine preferred language
    final ConcernRoleKey crKey = new ConcernRoleKey();
    crKey.concernRoleID = concernRoleID;
    final ConcernRoleDtls concernRoleDtls =
      ConcernRoleFactory.newInstance().readConcernRole(crKey);
    // get the french translation for province if the preferred
    // language is french
    if (concernRoleDtls.preferredLanguage.equals(LANGUAGE.FRENCH)) {
      rl1Dtls.recipientProvince = CodeTable.getOneItem(PROVINCETYPE.TABLENAME,
        rl1Dtls.recipientProvince, LOCALE.FRENCH);
    } else {
      rl1Dtls.recipientProvince = CodeTable.getOneItem(PROVINCETYPE.TABLENAME,
        rl1Dtls.recipientProvince, LOCALE.ENGLISH);
    }
    if (rl1Dtls.recipientProvince.length() > kRL1ProvinceCharLimit) {
      rl1Dtls.recipientProvince =
        rl1Dtls.recipientProvince.substring(0, kRL1ProvinceCharLimit);
    }
    if (rl1Dtls.recipientPostalCode.length() > kRL1PostalCodeCharLimit) {
      rl1Dtls.recipientPostalCode = rl1Dtls.recipientPostalCode
        .replaceAll(" ", "").substring(0, kRL1PostalCodeCharLimit);
    }
  }

  /**
   * Checks if an individual had a quebec address on the year end dates
   *
   * @param concernRoleID
   * @param yearEndDate
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public boolean checkQuebecAddressForEndOfTaxYear(final long concernRoleID,
    final Date yearEndDate) throws AppException, InformationalException {

    // search for address evidences that were active on Dec 31st of the
    // previous year(tax year)
    final SearchParticipantEvidenceByRangeKey evidenceSearchKey =
      new SearchParticipantEvidenceByRangeKey();
    evidenceSearchKey.concernRoleID = concernRoleID;
    evidenceSearchKey.dateRange = new StartDateEndDate();
    evidenceSearchKey.dateRange.endDate = yearEndDate.addDays(1);
    evidenceSearchKey.evidenceTypeCode = CASEEVIDENCE.BDMADDRESS;

    final PDCEvidenceDetailsList addressEvidenceList = PDCPersonFactory
      .newInstance().searchParticipantEvidenceByDateRange(evidenceSearchKey);

    final EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
    final EvidenceControllerInterface evidenceController =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final AddressDAO addressDAO =
      GuiceWrapper.getInjector().getInstance(AddressDAO.class);
    final AddressData addressDataObj = AddressDataFactory.newInstance();

    for (final PDCEvidenceDetails addressEvidence : addressEvidenceList.list) {

      // get the details for the address evidence
      eiEvidenceKey.evidenceType = addressEvidence.evidenceType;
      eiEvidenceKey.evidenceID = addressEvidence.evidenceID;
      final EIEvidenceReadDtls evidenceDtls =
        evidenceController.readEvidence(eiEvidenceKey);
      final DynamicEvidenceDataDetails addressDtls =
        (DynamicEvidenceDataDetails) evidenceDtls.evidenceObject;

      // if there is a residential address in Quebec on the given day
      if (addressDtls.getAttribute(BDMConstants.kEvidenceAttrAddressType)
        .getValue().equals(CONCERNROLEADDRESSTYPE.PRIVATE)) {

        // get the address data
        final long addressID = Long.parseLong(addressDtls
          .getAttribute(BDMConstants.kEvidenceAttrAddressID).getValue());
        final OtherAddressData otherAddressData = new OtherAddressData();
        otherAddressData.addressData =
          addressDAO.get(addressID).getAddressData();

        // convert address data into a map
        final AddressMapList addressMapList =
          addressDataObj.parseDataToMap(otherAddressData);

        // look for the province
        final AddressMap addressMap = new AddressMap();
        addressMap.name = ADDRESSELEMENTTYPE.PROVINCE;

        final ElementDetails elementDetails =
          addressDataObj.findElement(addressMapList, addressMap);

        // if it is Quebec, then set to true
        if (elementDetails.elementFound
          && elementDetails.elementValue.equals(PROVINCETYPE.QUEBEC)) {
          return true;

        }
      }
    }
    return false;
  }

  @Override
  public BDMViewTaxSlipDetailsList listTaxSlips(final ConcernRoleKey key)
    throws AppException, InformationalException {

    final BDMViewTaxSlipDetailsList taxSlipList =
      new BDMViewTaxSlipDetailsList();

    final BDMTaxSlipsForDisplayKey displayKey =
      new BDMTaxSlipsForDisplayKey();
    displayKey.concernRoleID = key.concernRoleID;
    displayKey.taxSlipStatusDeleted = BDMTAXSLIPSTATUS.DELETED;

    // search for T4A tax slips
    final curam.ca.gc.bdm.entity.financial.struct.BDMViewTaxSlipDetailsList t4aTaxSlips =
      BDMTaxSlipDataT4AFactory.newInstance()
        .searchTaxSlipsForDisplay(displayKey);

    for (final BDMViewTaxSlipDetails t4a : t4aTaxSlips.dtls) {
      if (t4a.nextTaxSlipDataID == 0L) {
        final curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetails taxSlipDetails =
          new curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetails();

        // assigns common details
        taxSlipDetails.assign(t4a);

        taxSlipDetails.taxSlipStatusDesc = CodeTable
          .getOneItem(BDMTAXSLIPSTATUS.TABLENAME, t4a.taxSlipStatusCode);

        // format status description
        if (taxSlipDetails.slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
          taxSlipDetails.taxSlipStatusDesc +=
            " (" + CodeTable.getOneItem(BDMTAXSLIPTYPE.TABLENAME,
              taxSlipDetails.slipTypeCode) + ")";
        }
        if (t4a.duplicateInd) {
          final String duplicate =
            new LocalisableString(BDMTAXSLIPS.TAX_SLIP_DUPLICATE)
              .getMessage(TransactionInfo.getProgramLocale());
          taxSlipDetails.taxSlipStatusDesc += " (" + duplicate + ")";
        }

        // convert date time to date
        final String dateString =
          DateFormatUtils.format(t4a.processingDateTime.getCalendar(),
            BDMConstants.YYYYMMDD_DATE_FORMAT);
        taxSlipDetails.processingDate = Date.getDate(dateString);

        taxSlipDetails.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
        taxSlipList.dtls.add(taxSlipDetails);
      }
    }

    final curam.ca.gc.bdm.entity.financial.struct.BDMViewTaxSlipDetailsList rl1TaxSlips =
      BDMTaxSlipDataRL1Factory.newInstance()
        .searchTaxSlipsForDisplay(displayKey);

    for (final BDMViewTaxSlipDetails rl1 : rl1TaxSlips.dtls) {
      if (rl1.nextTaxSlipDataID == 0L) {
        final curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetails taxSlipDetails =
          new curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetails();

        // assigns common details
        taxSlipDetails.assign(rl1);

        taxSlipDetails.taxSlipStatusDesc = CodeTable
          .getOneItem(BDMTAXSLIPSTATUS.TABLENAME, rl1.taxSlipStatusCode);

        // format status description
        if (taxSlipDetails.slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
          taxSlipDetails.taxSlipStatusDesc +=
            " (" + CodeTable.getOneItem(BDMTAXSLIPTYPE.TABLENAME,
              taxSlipDetails.slipTypeCode) + ")";
        }
        if (rl1.duplicateInd) {
          final String duplicate =
            new LocalisableString(BDMTAXSLIPS.TAX_SLIP_DUPLICATE)
              .getMessage(TransactionInfo.getProgramLocale());
          taxSlipDetails.taxSlipStatusDesc += " (" + duplicate + ")";
        }

        // convert date time to date
        final String dateString =
          DateFormatUtils.format(rl1.processingDateTime.getCalendar(),
            BDMConstants.YYYYMMDD_DATE_FORMAT);
        taxSlipDetails.processingDate = Date.getDate(dateString);

        taxSlipDetails.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
        taxSlipList.dtls.add(taxSlipDetails);
      }
    }

    taxSlipList.dtls.sort(
      new Comparator<curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetails>() {

        @Override
        public int compare(
          final curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetails o1,
          final curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetails o2) {

          //
          if (o1.taxYear == o2.taxYear) {
            // want comparison to sort in descending order
            return o1.creationDateTime.compareTo(o2.creationDateTime) * -1;
          } else {
            // compare years if they are not the same
            return o2.taxYear - o1.taxYear;
          }

        }
      });

    for (final curam.ca.gc.bdm.facade.struct.BDMViewTaxSlipDetails taxSlip : taxSlipList.dtls) {
      if ((taxSlip.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.DRAFT)
        || taxSlip.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.REQUESTED))
        && !taxSlip.slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)
        && !taxSlip.duplicateInd) {
        taxSlip.showEditInd = true;
      }
      if (taxSlip.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.DRAFT)) {
        taxSlip.showIssueInd = true;
      }
      if (taxSlip.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.DRAFT)
        || taxSlip.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.REQUESTED)) {
        taxSlip.showDeleteInd = true;
      }
      if (taxSlip.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.ISSUED)
        && !taxSlip.slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
        taxSlip.showAmendInd = true;
      }
      if (taxSlip.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.ISSUED)
        && !taxSlip.slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
        taxSlip.showCancelInd = true;
      }
      if (taxSlip.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.ISSUED)
        && taxSlip.slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
        taxSlip.showCreateInd = true;
      }
      if (taxSlip.taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.ISSUED)) {
        taxSlip.showDuplicateInd = true;
      }
    }

    return taxSlipList;
  }

  /**
   * Gets the inline details for a given tax slip
   */
  @Override
  public BDMViewTaxSlipInlineDetails viewTaxSlipInline(
    final BDMTaxSlipDataKey key) throws AppException, InformationalException {

    final BDMViewTaxSlipInlineDetails details =
      new BDMViewTaxSlipInlineDetails();

    NotFoundIndicator nfIndicator = new NotFoundIndicator();

    // Search for the tax slip in T4A
    final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
    t4aKey.taxSlipDataID = key.taxSlipDataID;
    final BDMTaxSlipDataT4ADtls t4aDtls =
      BDMTaxSlipDataT4AFactory.newInstance().read(nfIndicator, t4aKey);

    // set client details
    if (!nfIndicator.isNotFound()) {
      details.recipientFirstName = t4aDtls.recipientFirstName;
      details.recipientSurName = t4aDtls.recipientSurName;
      details.recipientInitial = t4aDtls.recipientInitial;
      details.mailingAddress = formatAddressForDisplay(
        t4aDtls.recipientAddressLine1, t4aDtls.recipientAddressLine2,
        t4aDtls.recipientCity, t4aDtls.recipientProvince,
        t4aDtls.recipientCountryCode, t4aDtls.recipientPostalCode);
      details.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
    }

    nfIndicator = new NotFoundIndicator();

    // Search for tax slip in RL1
    final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
    rl1Key.taxSlipDataID = key.taxSlipDataID;
    final BDMTaxSlipDataRL1Dtls rl1Dtls =
      BDMTaxSlipDataRL1Factory.newInstance().read(nfIndicator, rl1Key);

    // set client details
    if (!nfIndicator.isNotFound()) {
      details.recipientFirstName = rl1Dtls.recipientFirstName;
      details.recipientSurName = rl1Dtls.recipientLastName;
      details.recipientInitial = rl1Dtls.recipientInitial;
      details.mailingAddress = formatAddressForDisplay(
        rl1Dtls.recipientAddressLine1, "", rl1Dtls.recipientCity,
        rl1Dtls.recipientProvince, "", rl1Dtls.recipientPostalCode);
      details.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
    }

    final BDMTaxSlipAttrDetailsList attrList =
      BDMTaxSlipAttrDataFactory.newInstance().searchAttributesBySlipID(key);

    // iterate through attributes and add them to the return struct
    for (final BDMTaxSlipAttrDetails attr : attrList.dtls) {
      final curam.ca.gc.bdm.facade.struct.BDMTaxSlipAttrDetails attrDetails =
        new curam.ca.gc.bdm.facade.struct.BDMTaxSlipAttrDetails();

      attrDetails.taxSlipAttrDataID = attr.taxSlipAttrDataID;
      attrDetails.nameTextID = attr.nameTextID;
      // get the localizable box name
      attrDetails.boxName =
        localizableTextHandlerDAO.get(attr.nameTextID).getValue();
      attrDetails.boxID = attr.boxID;
      // format the attribute value
      attrDetails.attrValue =
        formatAttrValueForDisplay(attr.attrValue, attr.domainType);

      if (attr.attrType.equals(BDMTAXSLIPATTRTYPE.SIN)) {
        details.recipientSIN = attr.attrValue;
      }
      // if an agent can view the box, add it to the list
      if (attr.agentViewAsBoxInd == true) {
        details.dtls.add(attrDetails);
      }

    }

    return details;
  }

  @Override
  public BDMTaxSlipHistoryList listHistoryByTaxSlip(
    final BDMTaxSlipDataKey key) throws AppException, InformationalException {

    final BDMTaxSlipHistoryList historyList = new BDMTaxSlipHistoryList();

    // find existing tax slip and its details
    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

    final NotFoundIndicator t4aNfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
    t4aKey.taxSlipDataID = key.taxSlipDataID;

    final BDMTaxSlipDataT4ADtls t4aDtls = t4aObj.read(t4aNfIndicator, t4aKey);

    if (!t4aNfIndicator.isNotFound()) {

      final BDMTaxSlipHistoryKey historyKey = new BDMTaxSlipHistoryKey();
      historyKey.concernRoleID = t4aDtls.concernRoleID;
      historyKey.taxYear = t4aDtls.taxYear;
      historyKey.taxSlipStatusCode = BDMTAXSLIPSTATUS.DELETED;

      final BDMTaxSlipHistoryDetailsList t4aHistoryList =
        t4aObj.searchTaxSlipHistoryByDataID(historyKey);

      for (final BDMTaxSlipHistoryDetails history : t4aHistoryList.dtls) {
        if (history.taxSlipDataID == t4aDtls.taxSlipDataID) {
          continue;
        }
        final curam.ca.gc.bdm.facade.struct.BDMTaxSlipHistoryDetails historyDetails =
          new curam.ca.gc.bdm.facade.struct.BDMTaxSlipHistoryDetails();

        historyDetails.taxSlipDataID = history.taxSlipDataID;
        historyDetails.slipTypeCode = history.slipTypeCode;
        historyDetails.taxSlipStatusCode = CodeTable
          .getOneItem(BDMTAXSLIPSTATUS.TABLENAME, history.taxSlipStatusCode);
        if (history.slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
          historyDetails.taxSlipStatusCode += " (" + CodeTable
            .getOneItem(BDMTAXSLIPTYPE.TABLENAME, history.slipTypeCode) + ")";
        }
        if (history.duplicateInd) {
          historyDetails.taxSlipStatusCode +=
            " (" + new LocalisableString(BDMTAXSLIPS.TAX_SLIP_DUPLICATE)
              .getMessage(TransactionInfo.getProgramLocale()) + ")";
        }
        historyDetails.processingStatus = history.processingStatus;
        historyDetails.creationMethodType = history.creationMethodType;
        historyDetails.creationDateTime = history.creationDateTime;
        historyDetails.createdBy = history.createdBy;

        final String dateString =
          DateFormatUtils.format(history.processingDateTime.getCalendar(),
            BDMConstants.YYYYMMDD_DATE_FORMAT);
        historyDetails.processingDate = Date.getDate(dateString);

        historyList.dtls.add(historyDetails);
      }

    }

    final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
    rl1Key.taxSlipDataID = key.taxSlipDataID;

    final NotFoundIndicator rl1NfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rl1Dtls = rl1Obj.read(rl1NfIndicator, rl1Key);

    if (!rl1NfIndicator.isNotFound()) {

      final BDMTaxSlipHistoryKey historyKey = new BDMTaxSlipHistoryKey();
      historyKey.concernRoleID = rl1Dtls.concernRoleID;
      historyKey.taxYear = rl1Dtls.taxYear;
      historyKey.taxSlipStatusCode = BDMTAXSLIPSTATUS.DELETED;

      final BDMTaxSlipHistoryDetailsList rl1HistoryList =
        rl1Obj.searchTaxSlipHistoryByDataID(historyKey);

      for (final BDMTaxSlipHistoryDetails history : rl1HistoryList.dtls) {
        if (history.taxSlipDataID == rl1Dtls.taxSlipDataID) {
          continue;
        }
        final curam.ca.gc.bdm.facade.struct.BDMTaxSlipHistoryDetails historyDetails =
          new curam.ca.gc.bdm.facade.struct.BDMTaxSlipHistoryDetails();

        historyDetails.taxSlipDataID = history.taxSlipDataID;
        historyDetails.slipTypeCode = history.slipTypeCode;
        historyDetails.taxSlipStatusCode = CodeTable
          .getOneItem(BDMTAXSLIPSTATUS.TABLENAME, history.taxSlipStatusCode);
        if (history.slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
          historyDetails.taxSlipStatusCode += " (" + CodeTable
            .getOneItem(BDMTAXSLIPTYPE.TABLENAME, history.slipTypeCode) + ")";
        }
        if (history.duplicateInd) {
          historyDetails.taxSlipStatusCode +=
            " (" + new LocalisableString(BDMTAXSLIPS.TAX_SLIP_DUPLICATE)
              .getMessage(TransactionInfo.getProgramLocale()) + ")";
        }
        historyDetails.processingStatus = history.processingStatus;
        historyDetails.creationMethodType = history.creationMethodType;
        historyDetails.creationDateTime = history.creationDateTime;
        historyDetails.createdBy = history.createdBy;

        final String dateString =
          DateFormatUtils.format(history.processingDateTime.getCalendar(),
            BDMConstants.YYYYMMDD_DATE_FORMAT);
        historyDetails.processingDate = Date.getDate(dateString);

        historyList.dtls.add(historyDetails);
      }

    }

    return historyList;
  }

  @Override
  public BDMEditTaxSlipDisplayDetails readEditTaxSlipDetails(
    final BDMTaxSlipDataKey key) throws AppException, InformationalException {

    final BDMEditTaxSlipDisplayDetails displayDetails =
      new BDMEditTaxSlipDisplayDetails();

    // find existing tax slip and its details
    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

    NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
    t4aKey.taxSlipDataID = key.taxSlipDataID;

    final BDMTaxSlipDataT4ADtls t4aDtls = t4aObj.read(nfIndicator, t4aKey);

    if (!nfIndicator.isNotFound()) {
      displayDetails.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
      displayDetails.mailingAddress = formatAddressForDisplay(
        t4aDtls.recipientAddressLine1, t4aDtls.recipientAddressLine2,
        t4aDtls.recipientCity, t4aDtls.recipientProvince,
        t4aDtls.recipientCountryCode, t4aDtls.recipientPostalCode);
      displayDetails.recipientFirstName = t4aDtls.recipientFirstName;
      displayDetails.recipientSurName = t4aDtls.recipientSurName;
      displayDetails.recipientInitial = t4aDtls.recipientInitial;
      displayDetails.taxYear = t4aDtls.taxYear;
      displayDetails.versionNo = t4aDtls.versionNo;

    }

    final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
    rl1Key.taxSlipDataID = key.taxSlipDataID;

    nfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rl1Dtls = rl1Obj.read(nfIndicator, rl1Key);

    if (!nfIndicator.isNotFound()) {
      displayDetails.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
      displayDetails.mailingAddress = formatAddressForDisplay(
        rl1Dtls.recipientAddressLine1, "", rl1Dtls.recipientCity,
        rl1Dtls.recipientProvince, "", rl1Dtls.recipientPostalCode);
      displayDetails.recipientFirstName = rl1Dtls.recipientFirstName;
      displayDetails.recipientSurName = rl1Dtls.recipientLastName;
      displayDetails.recipientInitial = rl1Dtls.recipientInitial;
      displayDetails.taxYear = rl1Dtls.taxYear;
      displayDetails.versionNo = rl1Dtls.versionNo;

    }

    final BDMTaxSlipAttrDetailsList attrList =
      BDMTaxSlipAttrDataFactory.newInstance().searchAttributesBySlipID(key);

    // iterate through attributes and add them to the return struct
    for (final BDMTaxSlipAttrDetails attr : attrList.dtls) {
      final curam.ca.gc.bdm.facade.struct.BDMTaxSlipAttrDetails attrDetails =
        new curam.ca.gc.bdm.facade.struct.BDMTaxSlipAttrDetails();

      attrDetails.taxSlipAttrDataID = attr.taxSlipAttrDataID;
      attrDetails.nameTextID = attr.nameTextID;
      // get the localizable box name
      attrDetails.boxName =
        localizableTextHandlerDAO.get(attr.nameTextID).getValue();
      attrDetails.boxID = attr.boxID;
      attrDetails.versionNo = attr.versionNo;
      // format the attribute value
      attrDetails.attrValue =
        formatAttrValueForDisplay(attr.attrValue, attr.domainType);

      if (attr.attrType.equals(BDMTAXSLIPATTRTYPE.SIN)) {
        displayDetails.recipientSIN = attr.attrValue;
      }
      // if an agent can view the box, add it to the list
      if (attr.agentViewAsBoxInd == true) {
        // For now use same editable indicator to determine if it shuld be
        // displayed under boxes.
        // In future we can use agentViewAsBoxInd to still display the record
        // but do not allow editing the field using list checkbox control.
        displayDetails.dtls.add(attrDetails);
      }
    }

    return displayDetails;
  }

  /**
   * Modifies the tax slip
   */
  @Override
  public void modifyTaxSlipDetails(final BDMEditTaxSlipDetails details,
    final ActionIDProperty actionIDProperty)
    throws AppException, InformationalException {

    String taxSlipStatusCode = "";
    String slipTypeCode = "";
    Boolean duplicateInd = false;

    // find existing tax slip and its details
    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

    final NotFoundIndicator t4aNfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
    t4aKey.taxSlipDataID = details.taxSlipDataID;

    final BDMTaxSlipDataT4ADtls t4aDtls = t4aObj.read(t4aNfIndicator, t4aKey);

    if (!t4aNfIndicator.isNotFound()) {
      taxSlipStatusCode = t4aDtls.taxSlipStatusCode;
      slipTypeCode = t4aDtls.slipTypeCode;
      duplicateInd = t4aDtls.duplicateInd;
    }

    final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
    rl1Key.taxSlipDataID = details.taxSlipDataID;

    final NotFoundIndicator rl1NfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rl1Dtls = rl1Obj.read(rl1NfIndicator, rl1Key);

    if (!rl1NfIndicator.isNotFound()) {
      taxSlipStatusCode = rl1Dtls.taxSlipStatusCode;
      slipTypeCode = rl1Dtls.slipTypeCode;
      duplicateInd = rl1Dtls.duplicateInd;

    }

    // perform validation
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // only allow modifications on draft or requested tax slips
    if (!taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.DRAFT)
      && !taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.REQUESTED)) {

      final AppException exception =
        new AppException(BDMTAXSLIPS.ERR_MODIFY_TAX_SLIP_STATUS);
      exception.arg(new CodeTableItemIdentifier(BDMTAXSLIPSTATUS.TABLENAME,
        taxSlipStatusCode));
      informationalManager.addInformationalMsg(exception,
        curam.core.impl.CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }
    // do not allow cancelled tax slips to be modified
    if (slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
      final AppException exception =
        new AppException(BDMTAXSLIPS.ERR_MODIFY_TAX_SLIP_TYPE);
      exception.arg(
        new CodeTableItemIdentifier(BDMTAXSLIPTYPE.TABLENAME, slipTypeCode));
      informationalManager.addInformationalMsg(exception,
        curam.core.impl.CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }
    // do not allow duplicate tax slips to be modified
    if (duplicateInd) {
      informationalManager.addInformationalMsg(
        new AppException(BDMTAXSLIPS.ERR_MODIFY_TAX_SLIP_DUPLICATE),
        curam.core.impl.CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

    final BDMTaxSlipAttrConfig attrConfigObj =
      BDMTaxSlipAttrConfigFactory.newInstance();
    final BDMTaxSlipAttrData attrObj =
      BDMTaxSlipAttrDataFactory.newInstance();

    Money otherIncome = new Money(0.0);
    Money incomeOnReserve = new Money(0.0);
    Money incomeTaxDeducted = new Money(0.0);

    for (final BDMTaxSlipEditAttrDetails attr : details.dtls) {

      final BDMTaxSlipAttrDataKey attrKey = new BDMTaxSlipAttrDataKey();
      attrKey.taxSlipAttrDataID = attr.taxSlipAttrDataID;
      // read the attribute data
      final BDMTaxSlipAttrDataDtls attrData = attrObj.read(attrKey);

      final BDMTaxSlipAttrConfigKey attrConfigKey =
        new BDMTaxSlipAttrConfigKey();
      attrConfigKey.taxSlipAttrConfigID = attrData.taxSlipAttrConfigID;

      // read the attribute config
      final BDMTaxSlipAttrConfigDtls configDtls =
        attrConfigObj.read(attrConfigKey);

      final String formattedAttr = formatAttrValueForSave(configDtls.boxID,
        attr.attrValue, configDtls.domainType);

      // check that only 1 of other income or income on reserve exists
      if (configDtls.attrType.equals(BDMTAXSLIPATTRTYPE.T4A_OTHER_INCOME)
        || configDtls.attrType.equals(BDMTAXSLIPATTRTYPE.RL1_OTHER_INCOME)) {

        if (!StringUtil.isNullOrEmpty(formattedAttr)) {

          otherIncome = new Money(Double.parseDouble(formattedAttr));
        }
      }
      if (configDtls.attrType
        .equals(BDMTAXSLIPATTRTYPE.T4A_INDIAN_OTHER_INCOME)
        || configDtls.attrType
          .equals(BDMTAXSLIPATTRTYPE.RL1_INDIAN_INCOME_ON_RESERVE)) {

        if (!StringUtil.isNullOrEmpty(formattedAttr)) {

          incomeOnReserve = new Money(Double.parseDouble(formattedAttr));
        }
      }

      if (configDtls.attrType.equals(BDMTAXSLIPATTRTYPE.T4A_TAX_DEDUCTED)
        || configDtls.attrType.equals(BDMTAXSLIPATTRTYPE.RL1_TAX_DEDUCTED)) {

        if (!StringUtil.isNullOrEmpty(formattedAttr)) {

          incomeTaxDeducted = new Money(Double.parseDouble(formattedAttr));
        }

      }

      attrData.attrValue = formattedAttr;
      attrData.versionNo = attr.versionNo;
      attrObj.modify(attrKey, attrData);

    }

    if (!t4aNfIndicator.isNotFound()) {
      t4aDtls.otherIncome = otherIncome;
      t4aDtls.indianOtherIncome = incomeOnReserve;
      t4aDtls.incomeTaxDeducted = incomeTaxDeducted;
      t4aDtls.versionNo = details.versionNo;

      if (t4aDtls.creationMethodType
        .equals(BDMTAXSLIPCREATIONMETHOD.SYSTEM)) {
        t4aDtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.MANUAL;
      }
      if (actionIDProperty.actionIDProperty
        .equalsIgnoreCase(BDMConstants.kSaveAction)
        && taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.REQUESTED)) {

        t4aDtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;
      }
      t4aObj.modify(t4aKey, t4aDtls);
    }
    if (!rl1NfIndicator.isNotFound()) {
      rl1Dtls.otherIncomeAmount = otherIncome;
      rl1Dtls.indianIncomeOnReserve = incomeOnReserve;
      rl1Dtls.incomeTaxDeducted = incomeTaxDeducted;
      rl1Dtls.versionNo = details.versionNo;

      if (rl1Dtls.creationMethodType
        .equals(BDMTAXSLIPCREATIONMETHOD.SYSTEM)) {
        rl1Dtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.MANUAL;
      }
      if (actionIDProperty.actionIDProperty
        .equalsIgnoreCase(BDMConstants.kSaveAction)
        && taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.REQUESTED)) {

        rl1Dtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;
      }
      rl1Obj.modify(rl1Key, rl1Dtls);
    }

    // if the user clicked "SAVEANDISSUE", issue the tax slip
    if (actionIDProperty.actionIDProperty
      .equalsIgnoreCase(BDMConstants.kSaveAndIssueAction)
      && taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.DRAFT)) {
      final BDMTaxSlipDataIDVersionNoKey taxSlipKey =
        new BDMTaxSlipDataIDVersionNoKey();
      taxSlipKey.taxSlipDataID = details.taxSlipDataID;

      // read the version number
      final BDMTaxSlipDataKey bdmTaxSlipDataKey = new BDMTaxSlipDataKey();
      bdmTaxSlipDataKey.taxSlipDataID = taxSlipKey.taxSlipDataID;
      final BDMReadVersionNo readVersionNo = readVersionNo(bdmTaxSlipDataKey);

      taxSlipKey.versionNo = readVersionNo.versionNo;
      issueTaxSlip(taxSlipKey);
    }

  }

  /**
   * Removes any formatting done before saving an attribute
   *
   * @param boxID
   * @param attrValue
   * @param domainType
   * @return
   */
  protected String formatAttrValueForSave(final String boxID,
    final String attrValue, final String domainType) throws AppException {

    String newAttrValue = "";

    if (!StringUtil.isNullOrEmpty(attrValue)) {
      if (domainType.equals(BDMTAXSLIPATTRDOMAINTYPE.AMOUNT)
        || domainType.equals(BDMTAXSLIPATTRDOMAINTYPE.NUMBER)
        || domainType.equals(BDMTAXSLIPATTRDOMAINTYPE.DECIMAL)) {

        // remove any dollar signs or commas
        newAttrValue = attrValue
          .replace(curam.core.impl.CuramConst.gkDollar,
            curam.core.impl.CuramConst.gkEmpty)
          .replace(curam.core.impl.CuramConst.gkComma,
            curam.core.impl.CuramConst.gkEmpty);

        // if it is not a number, throw an error
        try {
          final double val = Double.parseDouble(newAttrValue);

          // make sure it's not negative
          if (val < 0) {
            if (!StringUtil.isNullOrEmpty(boxID)) {
              final AppException appException =
                new AppException(BDMTAXSLIPS.ERR_BOX_NEGATIVE_NUMBER);
              appException.arg(boxID);

              throw appException;
            } else {
              final AppException appException =
                new AppException(BDMTAXSLIPS.ERR_ATTRIBUTE_NEGATIVE_NUMBER);
              appException.arg(attrValue);

              throw appException;
            }
          }
        } catch (final NumberFormatException e) {
          if (!StringUtil.isNullOrEmpty(boxID)) {
            final AppException appException =
              new AppException(BDMTAXSLIPS.ERR_BOX_NUMBER_FORMAT);
            appException.arg(boxID);

            throw appException;
          } else {
            final AppException appException =
              new AppException(BDMTAXSLIPS.ERR_ATTRIBUTE_NUMBER_FORMAT);
            appException.arg(attrValue);

            throw appException;
          }

        }

      } else if (domainType.equals(BDMTAXSLIPATTRDOMAINTYPE.BOOLEAN)) {

        // if it is not "Yes" or "No" throw an error
        if (!attrValue.equalsIgnoreCase(BDMConstants.kYes)
          && !attrValue.equalsIgnoreCase(BDMConstants.kNo)) {
          if (!StringUtil.isNullOrEmpty(boxID)) {
            final AppException appException =
              new AppException(BDMTAXSLIPS.ERR_BOX_BOOLEAN_FORMAT);
            appException.arg(boxID);

            throw appException;
          } else {
            final AppException appException =
              new AppException(BDMTAXSLIPS.ERR_ATTRIBUTE_BOOLEAN_FORMAT);
            appException.arg(attrValue);

            throw appException;
          }
        }
        if (attrValue.equalsIgnoreCase(BDMConstants.kYes)) {
          newAttrValue = Boolean.TRUE.toString();
        } else {
          newAttrValue = curam.core.impl.CuramConst.gkEmpty;
        }
      }
    }

    return newAttrValue;
  }

  /**
   * Reads the version number for the tax slip
   */
  @Override
  public BDMReadVersionNo readVersionNo(final BDMTaxSlipDataKey key)
    throws AppException, InformationalException {

    final BDMReadVersionNo versionNo = new BDMReadVersionNo();

    // find existing tax slip and its details
    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

    final NotFoundIndicator t4aNfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
    t4aKey.taxSlipDataID = key.taxSlipDataID;

    final BDMTaxSlipDataT4ADtls t4aDtls = t4aObj.read(t4aNfIndicator, t4aKey);

    if (!t4aNfIndicator.isNotFound()) {
      versionNo.versionNo = t4aDtls.versionNo;
    }

    final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
    rl1Key.taxSlipDataID = key.taxSlipDataID;

    final NotFoundIndicator rl1NfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rl1Dtls = rl1Obj.read(rl1NfIndicator, rl1Key);

    if (!rl1NfIndicator.isNotFound()) {
      versionNo.versionNo = rl1Dtls.versionNo;
    }
    return versionNo;
  }

  @Override
  public BDMTaxSlipDataKey amendTaxSlip(final BDMTaxSlipDataKey key)
    throws AppException, InformationalException {

    final BDMTaxSlipDataKey newTaxSlipKey = new BDMTaxSlipDataKey();

    String taxSlipStatusCode = "";
    String slipTypeCode = "";
    int taxYear = 0;
    Date yearEndDate = Date.kZeroDate;
    long concernRoleID = 0L;

    // find existing tax slip and its details
    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

    final NotFoundIndicator t4aNfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
    t4aKey.taxSlipDataID = key.taxSlipDataID;

    final BDMTaxSlipDataT4ADtls t4aDtls = t4aObj.read(t4aNfIndicator, t4aKey);

    if (!t4aNfIndicator.isNotFound()) {
      taxSlipStatusCode = t4aDtls.taxSlipStatusCode;
      slipTypeCode = t4aDtls.slipTypeCode;
      taxYear = t4aDtls.taxYear;
      concernRoleID = t4aDtls.concernRoleID;
    }

    final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
    rl1Key.taxSlipDataID = key.taxSlipDataID;

    final NotFoundIndicator rl1NfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rl1Dtls = rl1Obj.read(rl1NfIndicator, rl1Key);

    if (!rl1NfIndicator.isNotFound()) {
      taxSlipStatusCode = rl1Dtls.taxSlipStatusCode;
      slipTypeCode = rl1Dtls.slipTypeCode;
      taxYear = rl1Dtls.taxYear;
      concernRoleID = rl1Dtls.concernRoleID;

    }

    yearEndDate = Date.getDate(taxYear + "1231");

    // perform validation
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // only allow amendments on issued tax slips
    if (!taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.ISSUED)) {

      final AppException exception =
        new AppException(BDMTAXSLIPS.ERR_AMEND_TAX_SLIP_STATUS);
      exception.arg(new CodeTableItemIdentifier(BDMTAXSLIPSTATUS.TABLENAME,
        taxSlipStatusCode));
      informationalManager.addInformationalMsg(exception,
        curam.core.impl.CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }
    // do not allow cancelled tax slips to be amended
    if (slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
      final AppException exception =
        new AppException(BDMTAXSLIPS.ERR_AMEND_TAX_SLIP_TYPE);
      exception.arg(
        new CodeTableItemIdentifier(BDMTAXSLIPTYPE.TABLENAME, slipTypeCode));
      informationalManager.addInformationalMsg(exception,
        curam.core.impl.CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

    validateDraftExists(key);

    final BDMTaxSlipClientDetails clientDetails =
      populateClientDetails(concernRoleID, yearEndDate);

    if (!t4aNfIndicator.isNotFound()) {
      // populate details
      final BDMTaxSlipDataT4ADtls newT4ADtls = new BDMTaxSlipDataT4ADtls();
      // generate CRA sequence number
      final BDMPaymentUtil payUtil = new BDMPaymentUtil();
      final int sequenceNumber =
        payUtil.getSeqNumber(BDMConstants.kCRASeqType);

      newT4ADtls.taxSlipDataID = UniqueID.nextUniqueID();
      newT4ADtls.concernRoleID = concernRoleID;
      newT4ADtls.taxYear = taxYear;
      newT4ADtls.sequenceNumber = sequenceNumber;
      newT4ADtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;
      newT4ADtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.MANUAL;
      newT4ADtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
      newT4ADtls.recipientSurName = clientDetails.recipientSurName;
      newT4ADtls.recipientFirstName = clientDetails.recipientFirstName;
      newT4ADtls.recipientInitial = clientDetails.recipientInitial;
      newT4ADtls.recipientSIN = clientDetails.recipientSIN;
      newT4ADtls.recipientAddressLine1 =
        clientDetails.recipientAddressLine1_T4A;
      newT4ADtls.recipientAddressLine2 =
        clientDetails.recipientAddressLine2_T4A;
      newT4ADtls.recipientCity = clientDetails.recipientCity;
      newT4ADtls.recipientProvince = clientDetails.recipientProv;
      newT4ADtls.recipientCountryCode = clientDetails.recipientCountryCode;
      newT4ADtls.recipientPostalCode = clientDetails.recipientPostalCode;
      newT4ADtls.creationDateTime = DateTime.getCurrentDateTime();
      newT4ADtls.processingDateTime = DateTime.getCurrentDateTime();
      newT4ADtls.slipTypeCode = BDMTAXSLIPTYPE.AMENDED;
      newT4ADtls.reportTypeCode = "A";

      newT4ADtls.statusIndianInd = clientDetails.statusIndianInd;
      newT4ADtls.indianOtherIncome = t4aDtls.indianOtherIncome;
      newT4ADtls.otherIncome = t4aDtls.otherIncome;
      newT4ADtls.incomeTaxDeducted = t4aDtls.incomeTaxDeducted;

      // get the provider details
      final BDMTaxSlipNearestProviderConfigKey providerKey =
        new BDMTaxSlipNearestProviderConfigKey();
      providerKey.currentDate = Date.getCurrentDate();
      providerKey.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
      providerKey.statusCode = BDMTAXSLIPCONFIGSTATUS.ACTIVE;
      final BDMTaxSlipProviderConfigKey providerConfigKey =
        BDMTaxSlipProviderConfigFactory.newInstance()
          .getNearestConfig(providerKey);

      newT4ADtls.taxSlipProviderConfigID =
        providerConfigKey.taxSlipProviderConfigID;

      final BDMSpecificAttributeTextKey attrKey =
        new BDMSpecificAttributeTextKey();
      attrKey.attrType = BDMTAXSLIPATTRTYPE.T4A_TAX_DEDUCTED;
      attrKey.taxSlipDataID = t4aDtls.taxSlipDataID;
      final BDMSpecificAttributeValueKey taxDeducted =
        readSpecificAttributeText(attrKey);

      // get the existing income values from the original t4a tax slip
      attrKey.attrType = BDMTAXSLIPATTRTYPE.T4A_INDIAN_OTHER_INCOME;
      attrKey.taxSlipDataID = t4aDtls.taxSlipDataID;
      final BDMSpecificAttributeValueKey indianOtherIncome =
        readSpecificAttributeText(attrKey);

      attrKey.attrType = BDMTAXSLIPATTRTYPE.T4A_OTHER_INCOME;
      attrKey.taxSlipDataID = t4aDtls.taxSlipDataID;
      final BDMSpecificAttributeValueKey otherIncome =
        readSpecificAttributeText(attrKey);

      formatT4AClientDetails(newT4ADtls);

      // update next tax slip data id for predecessor
      final BDMUpdateNextTaxSlipKey t4aUpdateKey =
        new BDMUpdateNextTaxSlipKey();
      t4aUpdateKey.concernRoleID = concernRoleID;
      t4aUpdateKey.nextTaxSlipDataID = newT4ADtls.taxSlipDataID;
      t4aUpdateKey.taxYear = taxYear;
      t4AUpdateNextTaxSlipID(t4aUpdateKey);

      t4aObj.insert(newT4ADtls);

      // store attributes
      final BDMTaxSlipStoreAttrDataKey storeAttrDataKey =
        new BDMTaxSlipStoreAttrDataKey();
      storeAttrDataKey.clientDetails = clientDetails;
      storeAttrDataKey.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
      storeAttrDataKey.taxSlipDataID = newT4ADtls.taxSlipDataID;
      storeAttrDataKey.taxYear = taxYear;
      storeAttrDataKey.reportTypeCode = newT4ADtls.reportTypeCode;
      storeAttrDataKey.taxSlipProviderConfigID =
        newT4ADtls.taxSlipProviderConfigID;
      storeAttrDataKey.incomeTaxDeducted = taxDeducted.attrValue;
      storeAttrDataKey.otherIncome = otherIncome.attrValue;
      storeAttrDataKey.indianOtherIncome = indianOtherIncome.attrValue;
      storeAttrDataKey.sequenceNumber =
        Long.toString(newT4ADtls.sequenceNumber);

      storeAttributeData(storeAttrDataKey);

      newTaxSlipKey.taxSlipDataID = newT4ADtls.taxSlipDataID;

    }

    if (!rl1NfIndicator.isNotFound()) {
      if (clientDetails.quebecAddressInd) {
        // populate details
        final BDMTaxSlipDataRL1Dtls newRL1Dtls = new BDMTaxSlipDataRL1Dtls();
        // generate sequence number
        final BDMTaxSlipMRQBatchSequenceKeyStruct1 sequenceKey =
          new BDMTaxSlipMRQBatchSequenceKeyStruct1();
        sequenceKey.concernRoleID = concernRoleID;
        sequenceKey.taxYear = taxYear;
        final long sequenceNumber =
          BDMTaxSlipMrqBatchImpl.getSequence(sequenceKey);

        newRL1Dtls.taxSlipDataID = UniqueID.nextUniqueID();
        newRL1Dtls.concernRoleID = concernRoleID;
        newRL1Dtls.taxYear = taxYear;
        newRL1Dtls.sequenceNumber = sequenceNumber;
        newRL1Dtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;
        newRL1Dtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.MANUAL;
        newRL1Dtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
        newRL1Dtls.recipientLastName = clientDetails.recipientSurName;
        newRL1Dtls.recipientFirstName = clientDetails.recipientFirstName;
        newRL1Dtls.recipientInitial = clientDetails.recipientInitial;
        newRL1Dtls.recipientSIN = clientDetails.recipientSIN;
        newRL1Dtls.recipientAddressLine1 =
          clientDetails.recipientAddressLine1_RL1;
        newRL1Dtls.recipientCity = clientDetails.recipientCity;
        newRL1Dtls.recipientProvince = clientDetails.recipientProv;
        newRL1Dtls.recipientPostalCode = clientDetails.recipientPostalCode;
        newRL1Dtls.creationDateTime = DateTime.getCurrentDateTime();
        newRL1Dtls.processingDateTime = DateTime.getCurrentDateTime();
        newRL1Dtls.slipTypeCode = BDMTAXSLIPTYPE.AMENDED;
        newRL1Dtls.reportTypeCode = "M";

        newRL1Dtls.statusIndianInd = clientDetails.statusIndianInd;
        newRL1Dtls.indianIncomeOnReserve = rl1Dtls.indianIncomeOnReserve;
        newRL1Dtls.otherIncomeAmount = rl1Dtls.otherIncomeAmount;
        newRL1Dtls.incomeTaxDeducted = rl1Dtls.incomeTaxDeducted;

        if (newRL1Dtls.otherIncomeAmount.isPositive()) {
          newRL1Dtls.otherIncomeSource = "RS";
        }

        // get the provider details
        final BDMTaxSlipNearestProviderConfigKey providerKey =
          new BDMTaxSlipNearestProviderConfigKey();
        providerKey.currentDate = Date.getCurrentDate();
        providerKey.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
        providerKey.statusCode = BDMTAXSLIPCONFIGSTATUS.ACTIVE;
        final BDMTaxSlipProviderConfigKey providerConfigKey =
          BDMTaxSlipProviderConfigFactory.newInstance()
            .getNearestConfig(providerKey);

        newRL1Dtls.taxSlipProviderConfigID =
          providerConfigKey.taxSlipProviderConfigID;

        final BDMSpecificAttributeTextKey attrKey =
          new BDMSpecificAttributeTextKey();
        attrKey.attrType = BDMTAXSLIPATTRTYPE.RL1_TAX_DEDUCTED;
        attrKey.taxSlipDataID = rl1Dtls.taxSlipDataID;
        final BDMSpecificAttributeValueKey taxDeducted =
          readSpecificAttributeText(attrKey);

        // get the existing income values from the original rl1 tax slip
        attrKey.attrType = BDMTAXSLIPATTRTYPE.RL1_INDIAN_INCOME_ON_RESERVE;
        attrKey.taxSlipDataID = rl1Dtls.taxSlipDataID;
        final BDMSpecificAttributeValueKey indianOtherIncome =
          readSpecificAttributeText(attrKey);
        attrKey.attrType = BDMTAXSLIPATTRTYPE.RL1_OTHER_INCOME;
        attrKey.taxSlipDataID = rl1Dtls.taxSlipDataID;
        final BDMSpecificAttributeValueKey otherIncome =
          readSpecificAttributeText(attrKey);

        formatRL1ClientDetails(newRL1Dtls, concernRoleID);

        // update next tax slip data id for predecessor
        final BDMUpdateNextTaxSlipKey rl1UpdateKey =
          new BDMUpdateNextTaxSlipKey();
        rl1UpdateKey.concernRoleID = concernRoleID;
        rl1UpdateKey.nextTaxSlipDataID = newRL1Dtls.taxSlipDataID;
        rl1UpdateKey.taxYear = taxYear;
        rl1UpdateNextTaxSlipID(rl1UpdateKey);

        rl1Obj.insert(newRL1Dtls);

        // store attributes
        final BDMTaxSlipStoreAttrDataKey storeAttrDataKey =
          new BDMTaxSlipStoreAttrDataKey();
        storeAttrDataKey.clientDetails = clientDetails;
        storeAttrDataKey.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
        storeAttrDataKey.taxSlipDataID = newRL1Dtls.taxSlipDataID;
        storeAttrDataKey.taxYear = taxYear;
        storeAttrDataKey.reportTypeCode = newRL1Dtls.reportTypeCode;
        storeAttrDataKey.taxSlipProviderConfigID =
          newRL1Dtls.taxSlipProviderConfigID;
        storeAttrDataKey.incomeTaxDeducted = taxDeducted.attrValue;
        storeAttrDataKey.otherIncome = otherIncome.attrValue;
        storeAttrDataKey.indianOtherIncome = indianOtherIncome.attrValue;
        storeAttrDataKey.sequenceNumber =
          Long.toString(newRL1Dtls.sequenceNumber);

        storeAttributeData(storeAttrDataKey);

        newTaxSlipKey.taxSlipDataID = newRL1Dtls.taxSlipDataID;

      } else {
        final AppException exception =
          new AppException(BDMTAXSLIPS.ERR_NO_QUEBEC_ADDRESS);
        exception.arg(taxYear);
        throw exception;
      }
    }

    return newTaxSlipKey;
  }

  @Override
  public BDMTaxSlipDataKey createNewTaxSlipFromExisting(
    final BDMTaxSlipDataKey key) throws AppException, InformationalException {

    final BDMTaxSlipDataKey newTaxSlipKey = new BDMTaxSlipDataKey();

    String taxSlipStatusCode = "";
    String slipTypeCode = "";
    int taxYear = 0;
    Date yearEndDate = Date.kZeroDate;
    long concernRoleID = 0L;

    // find existing tax slip and its details
    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

    final NotFoundIndicator t4aNfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
    t4aKey.taxSlipDataID = key.taxSlipDataID;

    final BDMTaxSlipDataT4ADtls t4aDtls = t4aObj.read(t4aNfIndicator, t4aKey);

    if (!t4aNfIndicator.isNotFound()) {
      taxSlipStatusCode = t4aDtls.taxSlipStatusCode;
      slipTypeCode = t4aDtls.slipTypeCode;
      taxYear = t4aDtls.taxYear;
      concernRoleID = t4aDtls.concernRoleID;
    }

    final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
    rl1Key.taxSlipDataID = key.taxSlipDataID;

    final NotFoundIndicator rl1NfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rl1Dtls = rl1Obj.read(rl1NfIndicator, rl1Key);

    if (!rl1NfIndicator.isNotFound()) {
      taxSlipStatusCode = rl1Dtls.taxSlipStatusCode;
      slipTypeCode = rl1Dtls.slipTypeCode;
      taxYear = rl1Dtls.taxYear;
      concernRoleID = rl1Dtls.concernRoleID;

    }

    yearEndDate = Date.getDate(taxYear + "1231");

    // perform validation
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // only allow create on issued tax slips
    if (!taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.ISSUED)) {

      final AppException exception =
        new AppException(BDMTAXSLIPS.ERR_CREATE_TAX_SLIP_STATUS);
      exception.arg(new CodeTableItemIdentifier(BDMTAXSLIPSTATUS.TABLENAME,
        taxSlipStatusCode));
      informationalManager.addInformationalMsg(exception,
        curam.core.impl.CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }
    // do not allow non-cancelled tax slips create option
    if (!slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
      final AppException exception =
        new AppException(BDMTAXSLIPS.ERR_CREATE_TAX_SLIP_TYPE);
      exception.arg(
        new CodeTableItemIdentifier(BDMTAXSLIPTYPE.TABLENAME, slipTypeCode));
      informationalManager.addInformationalMsg(exception,
        curam.core.impl.CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

    validateDraftExists(key);

    final BDMTaxSlipClientDetails clientDetails =
      populateClientDetails(concernRoleID, yearEndDate);

    if (!t4aNfIndicator.isNotFound()) {
      // populate details
      final BDMTaxSlipDataT4ADtls newT4ADtls = new BDMTaxSlipDataT4ADtls();
      // generate CRA sequence number
      final BDMPaymentUtil payUtil = new BDMPaymentUtil();
      final int sequenceNumber =
        payUtil.getSeqNumber(BDMConstants.kCRASeqType);

      newT4ADtls.taxSlipDataID = UniqueID.nextUniqueID();
      newT4ADtls.concernRoleID = concernRoleID;
      newT4ADtls.taxYear = taxYear;
      newT4ADtls.sequenceNumber = sequenceNumber;
      newT4ADtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;
      newT4ADtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.MANUAL;
      newT4ADtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
      newT4ADtls.recipientSurName = clientDetails.recipientSurName;
      newT4ADtls.recipientFirstName = clientDetails.recipientFirstName;
      newT4ADtls.recipientInitial = clientDetails.recipientInitial;
      newT4ADtls.recipientSIN = clientDetails.recipientSIN;
      newT4ADtls.recipientAddressLine1 =
        clientDetails.recipientAddressLine1_T4A;
      newT4ADtls.recipientAddressLine2 =
        clientDetails.recipientAddressLine2_T4A;
      newT4ADtls.recipientCity = clientDetails.recipientCity;
      newT4ADtls.recipientProvince = clientDetails.recipientProv;
      newT4ADtls.recipientCountryCode = clientDetails.recipientCountryCode;
      newT4ADtls.recipientPostalCode = clientDetails.recipientPostalCode;
      newT4ADtls.creationDateTime = DateTime.getCurrentDateTime();
      newT4ADtls.processingDateTime = DateTime.getCurrentDateTime();
      newT4ADtls.slipTypeCode = BDMTAXSLIPTYPE.ORIGINAL;
      newT4ADtls.reportTypeCode = "O";

      newT4ADtls.statusIndianInd = clientDetails.statusIndianInd;

      // get the provider details
      final BDMTaxSlipNearestProviderConfigKey providerKey =
        new BDMTaxSlipNearestProviderConfigKey();
      providerKey.currentDate = Date.getCurrentDate();
      providerKey.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
      providerKey.statusCode = BDMTAXSLIPCONFIGSTATUS.ACTIVE;
      final BDMTaxSlipProviderConfigKey providerConfigKey =
        BDMTaxSlipProviderConfigFactory.newInstance()
          .getNearestConfig(providerKey);

      newT4ADtls.taxSlipProviderConfigID =
        providerConfigKey.taxSlipProviderConfigID;

      formatT4AClientDetails(newT4ADtls);

      // update next tax slip data id for predecessor
      final BDMUpdateNextTaxSlipKey t4aUpdateKey =
        new BDMUpdateNextTaxSlipKey();
      t4aUpdateKey.concernRoleID = concernRoleID;
      t4aUpdateKey.nextTaxSlipDataID = newT4ADtls.taxSlipDataID;
      t4aUpdateKey.taxYear = taxYear;
      t4AUpdateNextTaxSlipID(t4aUpdateKey);

      t4aObj.insert(newT4ADtls);

      // store attributes
      final BDMTaxSlipStoreAttrDataKey storeAttrDataKey =
        new BDMTaxSlipStoreAttrDataKey();
      storeAttrDataKey.clientDetails = clientDetails;
      storeAttrDataKey.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
      storeAttrDataKey.taxSlipDataID = newT4ADtls.taxSlipDataID;
      storeAttrDataKey.taxYear = taxYear;
      storeAttrDataKey.reportTypeCode = newT4ADtls.reportTypeCode;
      storeAttrDataKey.taxSlipProviderConfigID =
        newT4ADtls.taxSlipProviderConfigID;
      storeAttrDataKey.sequenceNumber =
        Long.toString(newT4ADtls.sequenceNumber);

      storeAttributeData(storeAttrDataKey);

      newTaxSlipKey.taxSlipDataID = newT4ADtls.taxSlipDataID;

    }

    if (!rl1NfIndicator.isNotFound()) {
      if (clientDetails.quebecAddressInd) {
        // populate details
        final BDMTaxSlipDataRL1Dtls newRL1Dtls = new BDMTaxSlipDataRL1Dtls();
        // generate sequence number
        final BDMTaxSlipMRQBatchSequenceKeyStruct1 sequenceKey =
          new BDMTaxSlipMRQBatchSequenceKeyStruct1();
        sequenceKey.concernRoleID = concernRoleID;
        sequenceKey.taxYear = taxYear;
        final long sequenceNumber =
          BDMTaxSlipMrqBatchImpl.getSequence(sequenceKey);

        newRL1Dtls.taxSlipDataID = UniqueID.nextUniqueID();
        newRL1Dtls.concernRoleID = concernRoleID;
        newRL1Dtls.taxYear = taxYear;
        newRL1Dtls.sequenceNumber = sequenceNumber;
        newRL1Dtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;
        newRL1Dtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.MANUAL;
        newRL1Dtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
        newRL1Dtls.recipientLastName = clientDetails.recipientSurName;
        newRL1Dtls.recipientFirstName = clientDetails.recipientFirstName;
        newRL1Dtls.recipientInitial = clientDetails.recipientInitial;
        newRL1Dtls.recipientSIN = clientDetails.recipientSIN;
        newRL1Dtls.recipientAddressLine1 =
          clientDetails.recipientAddressLine1_RL1;
        newRL1Dtls.recipientCity = clientDetails.recipientCity;
        newRL1Dtls.recipientProvince = clientDetails.recipientProv;
        newRL1Dtls.recipientPostalCode = clientDetails.recipientPostalCode;
        newRL1Dtls.creationDateTime = DateTime.getCurrentDateTime();
        newRL1Dtls.processingDateTime = DateTime.getCurrentDateTime();
        newRL1Dtls.slipTypeCode = BDMTAXSLIPTYPE.ORIGINAL;
        newRL1Dtls.reportTypeCode = "R";

        newRL1Dtls.statusIndianInd = clientDetails.statusIndianInd;

        // get the provider details
        final BDMTaxSlipNearestProviderConfigKey providerKey =
          new BDMTaxSlipNearestProviderConfigKey();
        providerKey.currentDate = Date.getCurrentDate();
        providerKey.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
        providerKey.statusCode = BDMTAXSLIPCONFIGSTATUS.ACTIVE;
        final BDMTaxSlipProviderConfigKey providerConfigKey =
          BDMTaxSlipProviderConfigFactory.newInstance()
            .getNearestConfig(providerKey);

        newRL1Dtls.taxSlipProviderConfigID =
          providerConfigKey.taxSlipProviderConfigID;

        // update next tax slip data id for predecessor
        final BDMUpdateNextTaxSlipKey rl1UpdateKey =
          new BDMUpdateNextTaxSlipKey();
        rl1UpdateKey.concernRoleID = concernRoleID;
        rl1UpdateKey.nextTaxSlipDataID = newRL1Dtls.taxSlipDataID;
        rl1UpdateKey.taxYear = taxYear;
        rl1UpdateNextTaxSlipID(rl1UpdateKey);

        formatRL1ClientDetails(newRL1Dtls, concernRoleID);

        rl1Obj.insert(newRL1Dtls);

        // store attributes
        final BDMTaxSlipStoreAttrDataKey storeAttrDataKey =
          new BDMTaxSlipStoreAttrDataKey();
        storeAttrDataKey.clientDetails = clientDetails;
        storeAttrDataKey.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
        storeAttrDataKey.taxSlipDataID = newRL1Dtls.taxSlipDataID;
        storeAttrDataKey.taxYear = taxYear;
        storeAttrDataKey.reportTypeCode = newRL1Dtls.reportTypeCode;
        storeAttrDataKey.taxSlipProviderConfigID =
          newRL1Dtls.taxSlipProviderConfigID;
        storeAttrDataKey.sequenceNumber =
          Long.toString(newRL1Dtls.sequenceNumber);

        storeAttributeData(storeAttrDataKey);

        newTaxSlipKey.taxSlipDataID = newRL1Dtls.taxSlipDataID;

      } else {
        final AppException exception =
          new AppException(BDMTAXSLIPS.ERR_NO_QUEBEC_ADDRESS);
        exception.arg(taxYear);
        throw exception;
      }
    }

    return newTaxSlipKey;

  }

  private BDMTaxSlipDataKey taxSlipDataKey() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void createCancelTaxSlipFromExisting(final BDMTaxSlipDataKey key)
    throws AppException, InformationalException {

    final BDMTaxSlipDataIDVersionNoKey newTaxSlipKey =
      new BDMTaxSlipDataIDVersionNoKey();

    String taxSlipStatusCode = "";
    String slipTypeCode = "";
    int taxYear = 0;
    Date yearEndDate = Date.kZeroDate;
    long concernRoleID = 0L;

    // find existing tax slip and its details
    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

    final NotFoundIndicator t4aNfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
    t4aKey.taxSlipDataID = key.taxSlipDataID;

    final BDMTaxSlipDataT4ADtls t4aDtls = t4aObj.read(t4aNfIndicator, t4aKey);

    if (!t4aNfIndicator.isNotFound()) {
      taxSlipStatusCode = t4aDtls.taxSlipStatusCode;
      slipTypeCode = t4aDtls.slipTypeCode;
      taxYear = t4aDtls.taxYear;
      concernRoleID = t4aDtls.concernRoleID;
    }

    final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
    rl1Key.taxSlipDataID = key.taxSlipDataID;

    final NotFoundIndicator rl1NfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rl1Dtls = rl1Obj.read(rl1NfIndicator, rl1Key);

    if (!rl1NfIndicator.isNotFound()) {
      taxSlipStatusCode = rl1Dtls.taxSlipStatusCode;
      slipTypeCode = rl1Dtls.slipTypeCode;
      taxYear = rl1Dtls.taxYear;
      concernRoleID = rl1Dtls.concernRoleID;

    }

    yearEndDate = Date.getDate(taxYear + "1231");

    // perform validation
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // only allow amendments on issued tax slips
    if (!taxSlipStatusCode.equals(BDMTAXSLIPSTATUS.ISSUED)) {

      final AppException exception =
        new AppException(BDMTAXSLIPS.ERR_CREATE_TAX_SLIP_STATUS);
      exception.arg(new CodeTableItemIdentifier(BDMTAXSLIPSTATUS.TABLENAME,
        taxSlipStatusCode));
      informationalManager.addInformationalMsg(exception,
        curam.core.impl.CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }
    // do not allow cancelled tax slips to be amended
    if (slipTypeCode.equals(BDMTAXSLIPTYPE.CANCELLED)) {
      final AppException exception =
        new AppException(BDMTAXSLIPS.ERR_CREATE_TAX_SLIP_TYPE);
      exception.arg(
        new CodeTableItemIdentifier(BDMTAXSLIPTYPE.TABLENAME, slipTypeCode));
      informationalManager.addInformationalMsg(exception,
        curam.core.impl.CuramConst.gkEmpty,
        InformationalElement.InformationalType.kError);
    }

    if (informationalManager.operationHasInformationals()) {
      informationalManager.failOperation();
    }

    validateDraftExists(key);

    final BDMTaxSlipClientDetails clientDetails =
      populateClientDetails(concernRoleID, yearEndDate);

    if (!t4aNfIndicator.isNotFound()) {
      // populate details
      final BDMTaxSlipDataT4ADtls newT4ADtls = new BDMTaxSlipDataT4ADtls();
      // generate CRA sequence number
      final BDMPaymentUtil payUtil = new BDMPaymentUtil();
      final int sequenceNumber =
        payUtil.getSeqNumber(BDMConstants.kCRASeqType);

      newT4ADtls.taxSlipDataID = UniqueID.nextUniqueID();
      newT4ADtls.concernRoleID = concernRoleID;
      newT4ADtls.taxYear = taxYear;
      newT4ADtls.sequenceNumber = sequenceNumber;
      newT4ADtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;
      newT4ADtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.MANUAL;
      newT4ADtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
      newT4ADtls.recipientSurName = clientDetails.recipientSurName;
      newT4ADtls.recipientFirstName = clientDetails.recipientFirstName;
      newT4ADtls.recipientInitial = clientDetails.recipientInitial;
      newT4ADtls.recipientSIN = clientDetails.recipientSIN;
      newT4ADtls.recipientAddressLine1 =
        clientDetails.recipientAddressLine1_T4A;
      newT4ADtls.recipientAddressLine2 =
        clientDetails.recipientAddressLine2_T4A;
      newT4ADtls.recipientCity = clientDetails.recipientCity;
      newT4ADtls.recipientProvince = clientDetails.recipientProv;
      newT4ADtls.recipientCountryCode = clientDetails.recipientCountryCode;
      newT4ADtls.recipientPostalCode = clientDetails.recipientPostalCode;
      newT4ADtls.creationDateTime = DateTime.getCurrentDateTime();
      newT4ADtls.processingDateTime = DateTime.getCurrentDateTime();
      newT4ADtls.slipTypeCode = BDMTAXSLIPTYPE.CANCELLED;
      newT4ADtls.reportTypeCode = "C";

      newT4ADtls.statusIndianInd = clientDetails.statusIndianInd;

      // get the provider details
      final BDMTaxSlipNearestProviderConfigKey providerKey =
        new BDMTaxSlipNearestProviderConfigKey();
      providerKey.currentDate = Date.getCurrentDate();
      providerKey.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
      providerKey.statusCode = BDMTAXSLIPCONFIGSTATUS.ACTIVE;
      final BDMTaxSlipProviderConfigKey providerConfigKey =
        BDMTaxSlipProviderConfigFactory.newInstance()
          .getNearestConfig(providerKey);

      newT4ADtls.taxSlipProviderConfigID =
        providerConfigKey.taxSlipProviderConfigID;

      formatT4AClientDetails(newT4ADtls);

      // update next tax slip data id for predecessor
      final BDMUpdateNextTaxSlipKey t4aUpdateKey =
        new BDMUpdateNextTaxSlipKey();
      t4aUpdateKey.concernRoleID = concernRoleID;
      t4aUpdateKey.nextTaxSlipDataID = newT4ADtls.taxSlipDataID;
      t4aUpdateKey.taxYear = taxYear;
      t4AUpdateNextTaxSlipID(t4aUpdateKey);

      t4aObj.insert(newT4ADtls);

      // store attributes
      final BDMTaxSlipStoreAttrDataKey storeAttrDataKey =
        new BDMTaxSlipStoreAttrDataKey();
      storeAttrDataKey.clientDetails = clientDetails;
      storeAttrDataKey.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
      storeAttrDataKey.taxSlipDataID = newT4ADtls.taxSlipDataID;
      storeAttrDataKey.taxYear = taxYear;
      storeAttrDataKey.reportTypeCode = newT4ADtls.reportTypeCode;
      storeAttrDataKey.taxSlipProviderConfigID =
        newT4ADtls.taxSlipProviderConfigID;
      storeAttrDataKey.sequenceNumber =
        Long.toString(newT4ADtls.sequenceNumber);

      storeAttributeData(storeAttrDataKey);

      newTaxSlipKey.taxSlipDataID = newT4ADtls.taxSlipDataID;

    }

    if (!rl1NfIndicator.isNotFound()) {
      if (clientDetails.quebecAddressInd) {
        // populate details
        final BDMTaxSlipDataRL1Dtls newRL1Dtls = new BDMTaxSlipDataRL1Dtls();
        // generate sequence number
        final BDMTaxSlipMRQBatchSequenceKeyStruct1 sequenceKey =
          new BDMTaxSlipMRQBatchSequenceKeyStruct1();
        sequenceKey.concernRoleID = concernRoleID;
        sequenceKey.taxYear = taxYear;
        final long sequenceNumber =
          BDMTaxSlipMrqBatchImpl.getSequence(sequenceKey);

        newRL1Dtls.taxSlipDataID = UniqueID.nextUniqueID();
        newRL1Dtls.concernRoleID = concernRoleID;
        newRL1Dtls.taxYear = taxYear;
        newRL1Dtls.sequenceNumber = sequenceNumber;
        newRL1Dtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;
        newRL1Dtls.creationMethodType = BDMTAXSLIPCREATIONMETHOD.MANUAL;
        newRL1Dtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGTRANSFER;
        newRL1Dtls.recipientLastName = clientDetails.recipientSurName;
        newRL1Dtls.recipientFirstName = clientDetails.recipientFirstName;
        newRL1Dtls.recipientInitial = clientDetails.recipientInitial;
        newRL1Dtls.recipientSIN = clientDetails.recipientSIN;
        newRL1Dtls.recipientAddressLine1 =
          clientDetails.recipientAddressLine1_RL1;
        newRL1Dtls.recipientCity = clientDetails.recipientCity;
        newRL1Dtls.recipientProvince = clientDetails.recipientProv;
        newRL1Dtls.recipientPostalCode = clientDetails.recipientPostalCode;
        newRL1Dtls.creationDateTime = DateTime.getCurrentDateTime();
        newRL1Dtls.processingDateTime = DateTime.getCurrentDateTime();
        newRL1Dtls.slipTypeCode = BDMTAXSLIPTYPE.CANCELLED;
        newRL1Dtls.reportTypeCode = "A";

        newRL1Dtls.statusIndianInd = clientDetails.statusIndianInd;

        // get the provider details
        final BDMTaxSlipNearestProviderConfigKey providerKey =
          new BDMTaxSlipNearestProviderConfigKey();
        providerKey.currentDate = Date.getCurrentDate();
        providerKey.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
        providerKey.statusCode = BDMTAXSLIPCONFIGSTATUS.ACTIVE;
        final BDMTaxSlipProviderConfigKey providerConfigKey =
          BDMTaxSlipProviderConfigFactory.newInstance()
            .getNearestConfig(providerKey);

        newRL1Dtls.taxSlipProviderConfigID =
          providerConfigKey.taxSlipProviderConfigID;

        // update next tax slip data id for predecessor
        final BDMUpdateNextTaxSlipKey rl1UpdateKey =
          new BDMUpdateNextTaxSlipKey();
        rl1UpdateKey.concernRoleID = concernRoleID;
        rl1UpdateKey.nextTaxSlipDataID = newRL1Dtls.taxSlipDataID;
        rl1UpdateKey.taxYear = taxYear;
        rl1UpdateNextTaxSlipID(rl1UpdateKey);

        formatRL1ClientDetails(newRL1Dtls, concernRoleID);

        rl1Obj.insert(newRL1Dtls);

        // store attributes
        final BDMTaxSlipStoreAttrDataKey storeAttrDataKey =
          new BDMTaxSlipStoreAttrDataKey();
        storeAttrDataKey.clientDetails = clientDetails;
        storeAttrDataKey.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
        storeAttrDataKey.taxSlipDataID = newRL1Dtls.taxSlipDataID;
        storeAttrDataKey.taxYear = taxYear;
        storeAttrDataKey.reportTypeCode = newRL1Dtls.reportTypeCode;
        storeAttrDataKey.taxSlipProviderConfigID =
          newRL1Dtls.taxSlipProviderConfigID;
        storeAttrDataKey.sequenceNumber =
          Long.toString(newRL1Dtls.sequenceNumber);

        storeAttributeData(storeAttrDataKey);

        newTaxSlipKey.taxSlipDataID = newRL1Dtls.taxSlipDataID;

      } else {
        final AppException exception =
          new AppException(BDMTAXSLIPS.ERR_NO_QUEBEC_ADDRESS);
        exception.arg(taxYear);
        throw exception;
      }
    }
    key.taxSlipDataID = newTaxSlipKey.taxSlipDataID;
    final BDMReadVersionNo readVersionNo = readVersionNo(key);

    newTaxSlipKey.versionNo = readVersionNo.versionNo;
    issueTaxSlip(newTaxSlipKey);

  }

  @Override
  public void deleteTaxSlip(final BDMTaxSlipDataIDVersionNoKey key)
    throws AppException, InformationalException {

    final BDMTaxSlipDataT4A bdmTaxSlipDataT4AObj =
      BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataT4AKey bdmTaxSlipDataT4AKey =
      new BDMTaxSlipDataT4AKey();
    bdmTaxSlipDataT4AKey.taxSlipDataID = key.taxSlipDataID;
    final NotFoundIndicator nfIndicatorT4A = new NotFoundIndicator();
    final BDMTaxSlipDataT4ADtls t4AActiveDtls =
      bdmTaxSlipDataT4AObj.read(nfIndicatorT4A, bdmTaxSlipDataT4AKey);

    String taxSlipStatusCode = null;

    if (!nfIndicatorT4A.isNotFound()) {
      taxSlipStatusCode = t4AActiveDtls.taxSlipStatusCode;
    }

    final BDMTaxSlipDataRL1 bdmTaxSlipDataRL1Obj =
      BDMTaxSlipDataRL1Factory.newInstance();
    final BDMTaxSlipDataRL1Key bdmTaxSlipDataRL1Key =
      new BDMTaxSlipDataRL1Key();
    bdmTaxSlipDataRL1Key.taxSlipDataID = key.taxSlipDataID;

    final NotFoundIndicator nfIndicatorRL1 = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rL1ActiveDtls =
      bdmTaxSlipDataRL1Obj.read(nfIndicatorRL1, bdmTaxSlipDataRL1Key);

    if (!nfIndicatorRL1.isNotFound()) {
      taxSlipStatusCode = rL1ActiveDtls.taxSlipStatusCode;
    }

    if (!(BDMTAXSLIPSTATUS.DRAFT.equals(taxSlipStatusCode)
      || BDMTAXSLIPSTATUS.REQUESTED.equals(taxSlipStatusCode))) {
      final AppException exceptionErr =
        new AppException(BDMTAXSLIPS.TAX_SLIP_CANNOT_BE_DELETED);
      exceptionErr.arg(
        CodeTable.getOneItem(BDMTAXSLIPSTATUS.TABLENAME, taxSlipStatusCode));
      throw exceptionErr;
    }

    if (!nfIndicatorT4A.isNotFound()) {
      final BDMModifyT4ATaxSlipStatusKey bdmModifyT4ATaxSlipStatusKey =
        new BDMModifyT4ATaxSlipStatusKey();
      bdmModifyT4ATaxSlipStatusKey.taxSlipDataID = key.taxSlipDataID;

      final BDMModifyT4ATaxSlipStatusDtls bdmModifyT4ATaxSlipStatusDtls =
        new BDMModifyT4ATaxSlipStatusDtls();
      bdmModifyT4ATaxSlipStatusDtls.taxSlipStatusCode =
        BDMTAXSLIPSTATUS.DELETED;
      bdmModifyT4ATaxSlipStatusDtls.versionNo = key.versionNo;
      bdmTaxSlipDataT4AObj.modifyTaxSlipStatus(bdmModifyT4ATaxSlipStatusKey,
        bdmModifyT4ATaxSlipStatusDtls);

      // delete next tax slip data ID from predecessor
      final NotFoundIndicator nextTaxSlipNFIndicator =
        new NotFoundIndicator();
      final BDMNextTaxSlipDataKey nextTaxSlipKey =
        new BDMNextTaxSlipDataKey();
      nextTaxSlipKey.nextTaxSlipDataID = t4AActiveDtls.taxSlipDataID;
      final BDMTaxSlipDataT4ADtls predecessorT4ADtls = bdmTaxSlipDataT4AObj
        .readByNextTaxSlipID(nextTaxSlipNFIndicator, nextTaxSlipKey);

      if (!nextTaxSlipNFIndicator.isNotFound()) {
        final BDMTaxSlipDataT4AKey predecessorT4AKey =
          new BDMTaxSlipDataT4AKey();
        predecessorT4AKey.taxSlipDataID = predecessorT4ADtls.taxSlipDataID;

        predecessorT4ADtls.nextTaxSlipDataID = 0L;

        bdmTaxSlipDataT4AObj.modify(predecessorT4AKey, predecessorT4ADtls);
      }

    }

    if (!nfIndicatorRL1.isNotFound()) {
      final BDMModifyRL1TaxSlipStatusKey bdmModifyRL1TaxSlipStatusKey =
        new BDMModifyRL1TaxSlipStatusKey();
      bdmModifyRL1TaxSlipStatusKey.taxSlipDataID = key.taxSlipDataID;

      final BDMModifyRL1TaxSlipStatusDtls bdmModifyRL1TaxSlipStatusDtls =
        new BDMModifyRL1TaxSlipStatusDtls();
      bdmModifyRL1TaxSlipStatusDtls.taxSlipStatusCode =
        BDMTAXSLIPSTATUS.DELETED;
      bdmModifyRL1TaxSlipStatusDtls.versionNo = key.versionNo;
      bdmTaxSlipDataRL1Obj.modifyTaxSlipStatus(bdmModifyRL1TaxSlipStatusKey,
        bdmModifyRL1TaxSlipStatusDtls);

      // delete next tax slip data ID from predecessor
      final NotFoundIndicator nextTaxSlipNFIndicator =
        new NotFoundIndicator();
      final BDMNextTaxSlipDataKey nextTaxSlipKey =
        new BDMNextTaxSlipDataKey();
      nextTaxSlipKey.nextTaxSlipDataID = rL1ActiveDtls.taxSlipDataID;
      final BDMTaxSlipDataRL1Dtls predecessorRL1Dtls =
        bdmTaxSlipDataRL1Obj.readByNextTaxSlipID(nextTaxSlipKey);

      if (!nextTaxSlipNFIndicator.isNotFound()) {
        final BDMTaxSlipDataRL1Key predecessorRL1Key =
          new BDMTaxSlipDataRL1Key();
        predecessorRL1Key.taxSlipDataID = predecessorRL1Dtls.taxSlipDataID;

        predecessorRL1Dtls.nextTaxSlipDataID = 0L;

        bdmTaxSlipDataRL1Obj.modify(predecessorRL1Key, predecessorRL1Dtls);
      }
    }
  }

  @Override
  public void issueTaxSlip(final BDMTaxSlipDataIDVersionNoKey key)
    throws AppException, InformationalException {

    final BDMTaxSlipDataT4A bdmTaxSlipDataT4AObj =
      BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataT4AKey bdmTaxSlipDataT4AKey =
      new BDMTaxSlipDataT4AKey();
    bdmTaxSlipDataT4AKey.taxSlipDataID = key.taxSlipDataID;
    final NotFoundIndicator nfIndicatorT4A = new NotFoundIndicator();
    final BDMTaxSlipDataT4ADtls t4AActiveDtls =
      bdmTaxSlipDataT4AObj.read(nfIndicatorT4A, bdmTaxSlipDataT4AKey);

    String taxSlipStatusCode = null;
    boolean requestedSlipExists = false;

    if (!nfIndicatorT4A.isNotFound()) {
      taxSlipStatusCode = t4AActiveDtls.taxSlipStatusCode;
      final BDMConcernRoleYearStatusKey bdmConcernRoleYearStatusKey =
        new BDMConcernRoleYearStatusKey();
      bdmConcernRoleYearStatusKey.concernRoleID = t4AActiveDtls.concernRoleID;
      bdmConcernRoleYearStatusKey.taxYear = t4AActiveDtls.taxYear;
      bdmConcernRoleYearStatusKey.taxSlipStatusCode =
        BDMTAXSLIPSTATUS.REQUESTED;
      final NotFoundIndicator nfIndicatorT4ACRStatus =
        new NotFoundIndicator();
      bdmTaxSlipDataT4AObj.readByConcernRoleYearStatus(nfIndicatorT4ACRStatus,
        bdmConcernRoleYearStatusKey);
      if (!nfIndicatorT4ACRStatus.isNotFound()) {
        requestedSlipExists = true;
      }
    }

    final BDMTaxSlipDataRL1 bdmTaxSlipDataRL1Obj =
      BDMTaxSlipDataRL1Factory.newInstance();
    final BDMTaxSlipDataRL1Key bdmTaxSlipDataRL1Key =
      new BDMTaxSlipDataRL1Key();
    bdmTaxSlipDataRL1Key.taxSlipDataID = key.taxSlipDataID;

    final NotFoundIndicator nfIndicatorRL1 = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rL1ActiveDtls =
      bdmTaxSlipDataRL1Obj.read(nfIndicatorRL1, bdmTaxSlipDataRL1Key);

    if (!nfIndicatorRL1.isNotFound()) {
      taxSlipStatusCode = rL1ActiveDtls.taxSlipStatusCode;
      final NotFoundIndicator nfIndicatorRL1CRStatus =
        new NotFoundIndicator();
      final BDMConcernRoleYearStatusKey bdmConcernRoleYearStatusKey =
        new BDMConcernRoleYearStatusKey();
      bdmConcernRoleYearStatusKey.concernRoleID = rL1ActiveDtls.concernRoleID;
      bdmConcernRoleYearStatusKey.taxYear = rL1ActiveDtls.taxYear;
      bdmConcernRoleYearStatusKey.taxSlipStatusCode =
        BDMTAXSLIPSTATUS.REQUESTED;
      bdmTaxSlipDataRL1Obj.readByConcernRoleYearStatus(nfIndicatorRL1CRStatus,
        bdmConcernRoleYearStatusKey);
      if (!nfIndicatorRL1CRStatus.isNotFound()) {
        requestedSlipExists = true;
      }
    }

    if (!BDMTAXSLIPSTATUS.DRAFT.equals(taxSlipStatusCode)) {
      final AppException exceptionErr = new AppException(
        BDMTAXSLIPS.TAX_SLIP_CANNOT_BE_ISSUED_IF_NOT_IN_DRAFT_STATUS);
      exceptionErr.arg(
        CodeTable.getOneItem(BDMTAXSLIPSTATUS.TABLENAME, taxSlipStatusCode));
      throw exceptionErr;
    } else if (requestedSlipExists) {
      final AppException exceptionErr = new AppException(
        BDMTAXSLIPS.TAX_SLIP_ALREADY_EXISTS_IN_REQUESTED_MODE);
      exceptionErr.arg(CodeTable.getOneItem(BDMTAXSLIPSTATUS.TABLENAME,
        BDMTAXSLIPSTATUS.REQUESTED));
      throw exceptionErr;
    }

    if (!nfIndicatorT4A.isNotFound()) {
      final BDMModifyT4ATaxSlipStatusKey bdmModifyT4ATaxSlipStatusKey =
        new BDMModifyT4ATaxSlipStatusKey();
      bdmModifyT4ATaxSlipStatusKey.taxSlipDataID = key.taxSlipDataID;

      final BDMModifyT4ATaxSlipStatusDtls bdmModifyT4ATaxSlipStatusDtls =
        new BDMModifyT4ATaxSlipStatusDtls();
      bdmModifyT4ATaxSlipStatusDtls.taxSlipStatusCode =
        BDMTAXSLIPSTATUS.REQUESTED;
      bdmModifyT4ATaxSlipStatusDtls.versionNo = key.versionNo;
      bdmTaxSlipDataT4AObj.modifyTaxSlipStatus(bdmModifyT4ATaxSlipStatusKey,
        bdmModifyT4ATaxSlipStatusDtls);
    }

    if (!nfIndicatorRL1.isNotFound()) {
      final BDMModifyRL1TaxSlipStatusKey bdmModifyRL1TaxSlipStatusKey =
        new BDMModifyRL1TaxSlipStatusKey();
      bdmModifyRL1TaxSlipStatusKey.taxSlipDataID = key.taxSlipDataID;

      final BDMModifyRL1TaxSlipStatusDtls bdmModifyRL1TaxSlipStatusDtls =
        new BDMModifyRL1TaxSlipStatusDtls();
      bdmModifyRL1TaxSlipStatusDtls.taxSlipStatusCode =
        BDMTAXSLIPSTATUS.REQUESTED;
      bdmModifyRL1TaxSlipStatusDtls.versionNo = key.versionNo;
      bdmTaxSlipDataRL1Obj.modifyTaxSlipStatus(bdmModifyRL1TaxSlipStatusKey,
        bdmModifyRL1TaxSlipStatusDtls);
    }
  }

  @Override
  public void createDuplicateTaxSlip(final BDMTaxSlipDataKey key)
    throws AppException, InformationalException {

    final BDMTaxSlipDataT4A bdmTaxSlipDataT4AObj =
      BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataT4AKey bdmTaxSlipDataT4AKey =
      new BDMTaxSlipDataT4AKey();
    bdmTaxSlipDataT4AKey.taxSlipDataID = key.taxSlipDataID;
    final NotFoundIndicator nfIndicatorT4A = new NotFoundIndicator();
    final BDMTaxSlipDataT4ADtls t4AActiveDtls =
      bdmTaxSlipDataT4AObj.read(nfIndicatorT4A, bdmTaxSlipDataT4AKey);

    String taxSlipStatusCode = null;

    if (!nfIndicatorT4A.isNotFound()) {
      taxSlipStatusCode = t4AActiveDtls.taxSlipStatusCode;
    }

    final BDMTaxSlipDataRL1 bdmTaxSlipDataRL1Obj =
      BDMTaxSlipDataRL1Factory.newInstance();
    final BDMTaxSlipDataRL1Key bdmTaxSlipDataRL1Key =
      new BDMTaxSlipDataRL1Key();
    bdmTaxSlipDataRL1Key.taxSlipDataID = key.taxSlipDataID;

    final NotFoundIndicator nfIndicatorRL1 = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rL1ActiveDtls =
      bdmTaxSlipDataRL1Obj.read(nfIndicatorRL1, bdmTaxSlipDataRL1Key);

    if (!nfIndicatorRL1.isNotFound()) {
      taxSlipStatusCode = rL1ActiveDtls.taxSlipStatusCode;
    }

    if (!BDMTAXSLIPSTATUS.ISSUED.equals(taxSlipStatusCode)) {
      final AppException exceptionErr = new AppException(
        BDMTAXSLIPS.TAX_SLIP_ALREADY_EXISTS_IN_REQUESTED_MODE);
      exceptionErr.arg(
        CodeTable.getOneItem(BDMTAXSLIPSTATUS.TABLENAME, taxSlipStatusCode));
      throw exceptionErr;
    }

    long new_taxSlipDataID = 0L;
    if (!nfIndicatorT4A.isNotFound()) {
      final BDMTaxSlipDataT4ADtls t4ADtls = new BDMTaxSlipDataT4ADtls();
      t4ADtls.assign(t4AActiveDtls);
      t4ADtls.taxSlipDataID = UniqueID.nextUniqueID();
      t4ADtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;
      t4ADtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGREPRINT;
      t4ADtls.processingDateTime = DateTime.getCurrentDateTime();
      t4ADtls.creationDateTime = DateTime.getCurrentDateTime();
      t4ADtls.duplicateInd = true;

      final Date yearEndDate = Date.getDate(t4ADtls.taxYear + "1231");

      final BDMTaxSlipClientDetails clientDetails =
        populateClientDetails(t4ADtls.concernRoleID, yearEndDate);

      t4ADtls.recipientSurName = clientDetails.recipientSurName;
      t4ADtls.recipientFirstName = clientDetails.recipientFirstName;
      t4ADtls.recipientInitial = clientDetails.recipientInitial;
      t4ADtls.recipientAddressLine1 = clientDetails.recipientAddressLine1_T4A;
      t4ADtls.recipientAddressLine2 = clientDetails.recipientAddressLine2_T4A;
      t4ADtls.recipientCity = clientDetails.recipientCity;
      t4ADtls.recipientProvince = clientDetails.recipientProv;
      t4ADtls.recipientCountryCode = clientDetails.recipientCountryCode;
      t4ADtls.recipientPostalCode = clientDetails.recipientPostalCode;

      formatT4AClientDetails(t4ADtls);

      // update next tax slip data id for predecessor
      final BDMUpdateNextTaxSlipKey t4aUpdateKey =
        new BDMUpdateNextTaxSlipKey();
      t4aUpdateKey.concernRoleID = t4ADtls.concernRoleID;
      t4aUpdateKey.nextTaxSlipDataID = t4ADtls.taxSlipDataID;
      t4aUpdateKey.taxYear = t4ADtls.taxYear;
      t4AUpdateNextTaxSlipID(t4aUpdateKey);

      bdmTaxSlipDataT4AObj.insert(t4ADtls);

      final BDMTaxSlipStoreAttrDataDuplKey duplAttrKey =
        new BDMTaxSlipStoreAttrDataDuplKey();
      duplAttrKey.dtls = clientDetails;
      duplAttrKey.oldTaxSlipDataID = t4AActiveDtls.taxSlipDataID;
      duplAttrKey.taxSlipDataID = t4ADtls.taxSlipDataID;
      storeAttributeDataForDuplicate(duplAttrKey);

      new_taxSlipDataID = t4ADtls.taxSlipDataID;
    }

    if (!nfIndicatorRL1.isNotFound()) {
      final BDMTaxSlipDataRL1Dtls rL1Dtls = new BDMTaxSlipDataRL1Dtls();
      rL1Dtls.assign(rL1ActiveDtls);
      rL1Dtls.taxSlipDataID = UniqueID.nextUniqueID();
      rL1Dtls.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;
      rL1Dtls.processingStatus = BDMTAXSLIPPROCSTATUS.PENDINGREPRINT;
      rL1Dtls.processingDateTime = DateTime.getCurrentDateTime();
      rL1Dtls.creationDateTime = DateTime.getCurrentDateTime();
      rL1Dtls.duplicateInd = true;

      final Date yearEndDate = Date.getDate(rL1Dtls.taxYear + "1231");

      final BDMTaxSlipClientDetails clientDetails =
        populateClientDetails(rL1Dtls.concernRoleID, yearEndDate);

      rL1Dtls.recipientLastName = clientDetails.recipientSurName;
      rL1Dtls.recipientFirstName = clientDetails.recipientFirstName;
      rL1Dtls.recipientInitial = clientDetails.recipientInitial;
      rL1Dtls.recipientAddressLine1 = clientDetails.recipientAddressLine1_RL1;
      rL1Dtls.recipientCity = clientDetails.recipientCity;
      rL1Dtls.recipientProvince = clientDetails.recipientProv;
      rL1Dtls.recipientPostalCode = clientDetails.recipientPostalCode;

      formatRL1ClientDetails(rL1Dtls, rL1Dtls.concernRoleID);

      // update next tax slip data id for predecessor
      final BDMUpdateNextTaxSlipKey rl1UpdateKey =
        new BDMUpdateNextTaxSlipKey();
      rl1UpdateKey.concernRoleID = rL1Dtls.concernRoleID;
      rl1UpdateKey.nextTaxSlipDataID = rL1Dtls.taxSlipDataID;
      rl1UpdateKey.taxYear = rL1Dtls.taxYear;
      rl1UpdateNextTaxSlipID(rl1UpdateKey);

      bdmTaxSlipDataRL1Obj.insert(rL1Dtls);

      final BDMTaxSlipStoreAttrDataDuplKey duplAttrKey =
        new BDMTaxSlipStoreAttrDataDuplKey();
      duplAttrKey.dtls = clientDetails;
      duplAttrKey.oldTaxSlipDataID = rL1ActiveDtls.taxSlipDataID;
      duplAttrKey.taxSlipDataID = rL1Dtls.taxSlipDataID;
      storeAttributeDataForDuplicate(duplAttrKey);

      new_taxSlipDataID = rL1Dtls.taxSlipDataID;
    }

    key.taxSlipDataID = new_taxSlipDataID;
    final BDMReadVersionNo readVersionNo = readVersionNo(key);

    final BDMTaxSlipDataIDVersionNoKey bdmTaxSlipDataKey =
      new BDMTaxSlipDataIDVersionNoKey();
    bdmTaxSlipDataKey.taxSlipDataID = new_taxSlipDataID;
    bdmTaxSlipDataKey.versionNo = readVersionNo.versionNo;
    this.issueTaxSlip(bdmTaxSlipDataKey);
  }

  /**
   * Copies attributes from the old tax slip to the new tax slip, only updating
   * the address
   *
   * @param key
   * @throws AppException
   * @throws InformationalException
   */
  protected void
    storeAttributeDataForDuplicate(final BDMTaxSlipStoreAttrDataDuplKey key)
      throws AppException, InformationalException {

    // find old tax slip and its details
    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

    NotFoundIndicator t4aNfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
    t4aKey.taxSlipDataID = key.oldTaxSlipDataID;

    final BDMTaxSlipDataT4ADtls oldT4ADtls =
      t4aObj.read(t4aNfIndicator, t4aKey);

    long taxSlipConfigID = 0L;
    long oldTaxSlipID = 0L;

    if (!t4aNfIndicator.isNotFound()) {
      taxSlipConfigID = oldT4ADtls.taxSlipConfigID;
      oldTaxSlipID = oldT4ADtls.taxSlipDataID;
    }

    final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
    rl1Key.taxSlipDataID = key.oldTaxSlipDataID;

    NotFoundIndicator rl1NfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls oldRL1Dtls =
      rl1Obj.read(rl1NfIndicator, rl1Key);

    if (!rl1NfIndicator.isNotFound()) {
      taxSlipConfigID = oldRL1Dtls.taxSlipConfigID;
      oldTaxSlipID = oldRL1Dtls.taxSlipDataID;

    }

    if (taxSlipConfigID != 0L) {
      t4aNfIndicator = new NotFoundIndicator();
      rl1NfIndicator = new NotFoundIndicator();

      t4aKey.taxSlipDataID = key.taxSlipDataID;
      rl1Key.taxSlipDataID = key.taxSlipDataID;

      final BDMTaxSlipDataT4ADtls t4aDtls =
        t4aObj.read(t4aNfIndicator, t4aKey);
      final BDMTaxSlipDataRL1Dtls rl1Dtls =
        rl1Obj.read(rl1NfIndicator, rl1Key);

      long newTaxSlipDataID = 0L;

      // update the tax slip config id for the new tax slips
      if (!t4aNfIndicator.isNotFound()) {

        t4aDtls.taxSlipConfigID = taxSlipConfigID;
        t4aObj.modify(t4aKey, t4aDtls);
        newTaxSlipDataID = t4aDtls.taxSlipDataID;
      }

      if (!rl1NfIndicator.isNotFound()) {
        rl1Obj.modify(rl1Key, rl1Dtls);
        newTaxSlipDataID = rl1Dtls.taxSlipDataID;
      }

      final BDMTaxSlipAttrData attrObj =
        BDMTaxSlipAttrDataFactory.newInstance();

      final BDMTaxSlipAttrDataKeyStruct1 attrKey =
        new BDMTaxSlipAttrDataKeyStruct1();
      attrKey.taxSlipDataID = oldTaxSlipID;
      final BDMTaxSlipAttrDataDtlsList oldAttrDataList =
        attrObj.readByTaxSlipDataID(attrKey);

      final BDMTaxSlipAttrConfig attrConfigObj =
        BDMTaxSlipAttrConfigFactory.newInstance();

      // copy the attributes
      for (final BDMTaxSlipAttrDataDtls oldAttrData : oldAttrDataList.dtls) {
        final BDMTaxSlipAttrDataDtls newAttrDtls =
          new BDMTaxSlipAttrDataDtls();
        newAttrDtls.assign(oldAttrData);
        newAttrDtls.taxSlipAttrDataID = UniqueID.nextUniqueID();
        newAttrDtls.taxSlipDataID = newTaxSlipDataID;

        final BDMTaxSlipAttrConfigKey attrConfigKey =
          new BDMTaxSlipAttrConfigKey();
        attrConfigKey.taxSlipAttrConfigID = newAttrDtls.taxSlipAttrConfigID;
        final BDMTaxSlipAttrConfigDtls attrConfigDtls =
          attrConfigObj.read(attrConfigKey);

        if (attrConfigDtls.attrType
          .equals(BDMTAXSLIPATTRTYPE.T4A_RECIPIENT_ADDRESS)) {
          newAttrDtls.attrValue = formatAttrT4AAddress(key.dtls);

        } else if (attrConfigDtls.attrType
          .equals(BDMTAXSLIPATTRTYPE.RL1_RECIPIENT_NAME_ADDRESS)) {
          newAttrDtls.attrValue = formatAttrRL1Address(key.dtls);
        }

        if (!newAttrDtls.attrValue.equals(oldAttrData.attrValue)) {
          if (attrConfigDtls.boxTextSize != 0
            && newAttrDtls.attrValue.length() > attrConfigDtls.boxTextSize) {
            newAttrDtls.attrValue =
              newAttrDtls.attrValue.substring(0, attrConfigDtls.boxTextSize);
          }
        }

        attrObj.insert(newAttrDtls);

      }

    }

  }

  protected void validateDraftExists(final BDMTaxSlipDataKey key)
    throws AppException, InformationalException {

    // find existing tax slip and its details
    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

    final NotFoundIndicator t4aNfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
    t4aKey.taxSlipDataID = key.taxSlipDataID;

    final BDMTaxSlipDataT4ADtls t4aDtls = t4aObj.read(t4aNfIndicator, t4aKey);

    if (!t4aNfIndicator.isNotFound()) {

      final NotFoundIndicator draftNfIndicator = new NotFoundIndicator();
      final BDMConcernRoleYearStatusKey draftSearchKey =
        new BDMConcernRoleYearStatusKey();
      draftSearchKey.concernRoleID = t4aDtls.concernRoleID;
      draftSearchKey.taxYear = t4aDtls.taxYear;
      draftSearchKey.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;

      // look for draft records
      t4aObj.readByConcernRoleYearStatus(draftNfIndicator, draftSearchKey);

      if (!draftNfIndicator.isNotFound()) {
        final AppException exception = new AppException(
          BDMTAXSLIPS.ERR_MULTIPLE_TAX_SLIPS_IN_DRAFT_STATUS);
        exception.arg(new CodeTableItemIdentifier(BDMTAXSLIPSTATUS.TABLENAME,
          BDMTAXSLIPSTATUS.DRAFT));

        throw exception;
      }
    }

    final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
    rl1Key.taxSlipDataID = key.taxSlipDataID;

    final NotFoundIndicator rl1NfIndicator = new NotFoundIndicator();

    final BDMTaxSlipDataRL1Dtls rl1Dtls = rl1Obj.read(rl1NfIndicator, rl1Key);

    if (!rl1NfIndicator.isNotFound()) {
      final NotFoundIndicator draftNfIndicator = new NotFoundIndicator();
      final BDMConcernRoleYearStatusKey draftSearchKey =
        new BDMConcernRoleYearStatusKey();
      draftSearchKey.concernRoleID = rl1Dtls.concernRoleID;
      draftSearchKey.taxYear = rl1Dtls.taxYear;
      draftSearchKey.taxSlipStatusCode = BDMTAXSLIPSTATUS.DRAFT;

      // look for draft slips
      rl1Obj.readByConcernRoleYearStatus(draftNfIndicator, draftSearchKey);

      if (!draftNfIndicator.isNotFound()) {
        final AppException exception = new AppException(
          BDMTAXSLIPS.ERR_MULTIPLE_TAX_SLIPS_IN_DRAFT_STATUS);
        exception.arg(new CodeTableItemIdentifier(BDMTAXSLIPSTATUS.TABLENAME,
          BDMTAXSLIPSTATUS.DRAFT));

        throw exception;
      }
    }
  }

  /**
   * Formats an attribute based off its domain type
   */
  @Override
  public String formatAttrValueForDisplay(String attrValue,
    final String domainType) throws AppException, InformationalException {

    if (!StringUtil.isNullOrEmpty(attrValue)) {

      // if it's an amount, format it to 2 decimals and seperated by commas
      if (domainType.equals(BDMTAXSLIPATTRDOMAINTYPE.AMOUNT)) {
        final double amount = Double.parseDouble(attrValue);

        final DecimalFormat df = new DecimalFormat("0.00");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);

        attrValue = df.format(amount);

      }
      // if it's a boolean, convert true/false to yes/no
      else if (domainType.equals(BDMTAXSLIPATTRDOMAINTYPE.BOOLEAN)) {
        if (Boolean.valueOf(attrValue)) {
          attrValue = BDMConstants.kYes;
        } else {
          attrValue = BDMConstants.kNo;
        }
      }
    }
    return attrValue;
  }

  /**
   * Stores attribute data for a tax slip
   */
  @Override
  public void storeAttributeData(final BDMTaxSlipStoreAttrDataKey key)
    throws AppException, InformationalException {

    final BDMTaxSlipNearestConfigKey configKey =
      new BDMTaxSlipNearestConfigKey();
    configKey.effectiveYear = key.taxYear;
    configKey.formTypeCode = key.formTypeCode;
    configKey.statusCode = BDMTAXSLIPCONFIGSTATUS.ACTIVE;

    BDMTaxSlipConfigKey config = null;
    try {
      config =
        BDMTaxSlipConfigFactory.newInstance().getNearestConfig(configKey);
    } catch (final RecordNotFoundException e) {
      // do nothing
    }

    if (config != null) {
      // if config is found, modify the tax slip and add the config
      final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();
      final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

      NotFoundIndicator nfIndicator = new NotFoundIndicator();

      final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
      t4aKey.taxSlipDataID = key.taxSlipDataID;

      final BDMTaxSlipDataT4ADtls t4aDtls = t4aObj.read(nfIndicator, t4aKey);

      if (!nfIndicator.isNotFound()) {
        t4aDtls.taxSlipConfigID = config.taxSlipConfigID;

        t4aObj.modify(t4aKey, t4aDtls);
      }

      final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
      rl1Key.taxSlipDataID = key.taxSlipDataID;

      nfIndicator = new NotFoundIndicator();

      final BDMTaxSlipDataRL1Dtls rl1Dtls = rl1Obj.read(nfIndicator, rl1Key);

      if (!nfIndicator.isNotFound()) {
        rl1Dtls.taxSlipConfigID = config.taxSlipConfigID;

        rl1Obj.modify(rl1Key, rl1Dtls);
      }

      final BDMTaxSlipProviderConfigKey providerKey =
        new BDMTaxSlipProviderConfigKey();
      providerKey.taxSlipProviderConfigID = key.taxSlipProviderConfigID;

      // read the provider details
      final BDMTaxSlipProviderConfigDtls providerDtls =
        BDMTaxSlipProviderConfigFactory.newInstance().read(providerKey);

      // get the attributes that need to be populated
      final BDMTaxSlipConfigRecordStatusKey readByConfigIDKey =
        new BDMTaxSlipConfigRecordStatusKey();
      readByConfigIDKey.recordStatusCode = RECORDSTATUS.NORMAL;
      readByConfigIDKey.taxSlipConfigID = config.taxSlipConfigID;
      final BDMTaxSlipAttrConfigDtlsList attrConfigList =
        BDMTaxSlipAttrConfigFactory.newInstance()
          .readByConfigID(readByConfigIDKey);

      // populate all the attributes
      for (final BDMTaxSlipAttrConfigDtls attrConfig : attrConfigList.dtls) {
        final BDMTaxSlipAttrDataDtls attr = new BDMTaxSlipAttrDataDtls();
        attr.taxSlipAttrConfigID = attrConfig.taxSlipAttrConfigID;
        attr.taxSlipDataID = key.taxSlipDataID;
        attr.taxSlipAttrDataID = UniqueID.nextUniqueID();

        String value = "";

        // determine the value based off the attribute type
        if (attrConfig.attrType.equals(BDMTAXSLIPATTRTYPE.SIN)) {
          value = key.clientDetails.recipientSIN;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.T4A_TAX_DEDUCTED)) {
          value = key.incomeTaxDeducted;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.T4A_OTHER_INCOME)) {
          value = key.otherIncome;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.T4A_INDIAN_OTHER_INCOME)) {
          value = key.indianOtherIncome;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.T4A_PAYER_NAME)) {
          value = providerDtls.payerNameLine1;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.T4A_PAYER_BUSINESS_NUMBER)) {
          value = providerDtls.businessNumber;
        }

        else if (attrConfig.attrType.equals(BDMTAXSLIPATTRTYPE.TAX_YEAR)) {
          value = Integer.toString(key.taxYear);
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.T4A_RECIPIENT_LAST_NAME)) {
          value = key.clientDetails.recipientSurName;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.T4A_RECIPIENT_FIRST_NAME)) {
          value = key.clientDetails.recipientFirstName;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.T4A_RECIPIENT_INITIAL)) {
          value = key.clientDetails.recipientInitial;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.T4A_RECIPIENT_ADDRESS)) {
          value = formatAttrT4AAddress(key.clientDetails);
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.RL1_TAX_DEDUCTED)) {
          value = key.incomeTaxDeducted;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.RL1_OTHER_INCOME)) {
          value = key.otherIncome;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.RL1_INDIAN_INCOME_ON_RESERVE)) {
          value = key.indianOtherIncome;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.TAX_SLIP_REPORT_CODE)) {
          value = key.reportTypeCode;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.RL1_SEQUENCE_NUMBER)) {
          value = key.sequenceNumber;
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.RL1_RECIPIENT_NAME_ADDRESS)) {
          value = formatAttrRL1Address(key.clientDetails);
        }

        else if (attrConfig.attrType
          .equals(BDMTAXSLIPATTRTYPE.RL1_PAYER_NAME_ADDRESS)) {
          value = providerDtls.payerNameLine1;
        }

        // truncate value if required
        if (attrConfig.boxTextSize != 0
          && value.length() > attrConfig.boxTextSize) {
          value = value.substring(0, attrConfig.boxTextSize);
        }

        attr.attrValue = value;

        // insert attribute
        BDMTaxSlipAttrDataFactory.newInstance().insert(attr);
      }
    }
  }

  /**
   * Finds a value for a specific attribute for a tax slip
   */
  @Override
  public BDMSpecificAttributeValueKey
    readSpecificAttributeText(final BDMSpecificAttributeTextKey key)
      throws AppException, InformationalException {

    try {
      final BDMSpecificAttributeValueKey valueKey = BDMTaxSlipAttrDataFactory
        .newInstance().readSpecificAttributeText(key);

      return valueKey;
    } catch (final RecordNotFoundException e) {
      final BDMSpecificAttributeValueKey valueKey =
        new BDMSpecificAttributeValueKey();

      return valueKey;
    }
  }

  /**
   * Formats a T4A Address as it appears on a tax slip
   *
   * @param details
   * @return
   */
  protected String
    formatAttrT4AAddress(final BDMTaxSlipClientDetails details) {

    String addrText = "";

    if (!StringUtil.isNullOrEmpty(details.recipientAddressLine1_T4A)) {
      addrText = details.recipientAddressLine1_T4A;
    }
    if (!StringUtil.isNullOrEmpty(details.recipientAddressLine2_T4A)) {
      addrText =
        addrText + CuramConst.gkNewLine + details.recipientAddressLine2_T4A;
    }
    if (!StringUtil.isNullOrEmpty(details.recipientCity)) {
      addrText = addrText + CuramConst.gkNewLine + details.recipientCity;
    }
    if (!StringUtil.isNullOrEmpty(details.recipientProv)) {
      addrText = addrText + CuramConst.gkCommaSpace + details.recipientProv;
    }
    if (!StringUtil.isNullOrEmpty(details.recipientCountryCode)) {
      addrText =
        addrText + CuramConst.gkCommaSpace + details.recipientCountryCode;
    }
    if (!StringUtil.isNullOrEmpty(details.recipientPostalCode)) {
      addrText =
        addrText + CuramConst.gkCommaSpace + details.recipientPostalCode;
    }

    if (addrText.startsWith(CuramConst.gkNewLine)) {
      addrText = addrText.replaceFirst(CuramConst.gkNewLine, "");
    }
    if (addrText.startsWith(CuramConst.gkCommaSpace)) {
      addrText = addrText.replaceFirst(CuramConst.gkCommaSpace, "");
    }

    return addrText;
  }

  /**
   * Formats an RL1 Address as it appears on a tax slip
   *
   * @param details
   * @return
   */
  protected String formatAttrRL1Address(final BDMTaxSlipClientDetails details)
    throws DatabaseException, AppRuntimeException, AppException,
    InformationalException {

    String addrText = details.recipientSurName + CuramConst.gkCommaSpace
      + details.recipientFirstName;

    if (!StringUtil.isNullOrEmpty(details.recipientAddressLine1_RL1)) {
      addrText =
        addrText + CuramConst.gkNewLine + details.recipientAddressLine1_RL1;
    }
    if (!StringUtil.isNullOrEmpty(details.recipientCity)) {
      addrText = addrText + CuramConst.gkNewLine + details.recipientCity;
    }
    if (!StringUtil.isNullOrEmpty(details.recipientProv)) {
      addrText = addrText + CuramConst.gkCommaSpace
        + CodeTable.getOneItem(PROVINCETYPE.TABLENAME, details.recipientProv);
    }
    if (!StringUtil.isNullOrEmpty(details.recipientPostalCode)) {
      addrText =
        addrText + CuramConst.gkCommaSpace + details.recipientPostalCode;
    }

    if (addrText.startsWith(CuramConst.gkNewLine)) {
      addrText = addrText.replaceFirst(CuramConst.gkNewLine, "");
    }
    if (addrText.startsWith(CuramConst.gkCommaSpace)) {
      addrText = addrText.replaceFirst(CuramConst.gkCommaSpace, "");
    }

    return addrText;
  }

  /**
   * Formats a text for displaying address
   *
   * @param addLine1
   * @param addLine2
   * @param city
   * @param prov
   * @param countryCode
   * @param postalCode
   * @return
   */
  protected String formatAddressForDisplay(final String addLine1,
    final String addLine2, final String city, final String prov,
    final String countryCode, final String postalCode) {

    String addrText = "";

    if (!StringUtil.isNullOrEmpty(addLine1)) {
      addrText = addLine1;
    }
    if (!StringUtil.isNullOrEmpty(addLine2)) {
      addrText = addrText + CuramConst.gkCommaSpace + addLine2;
    }
    if (!StringUtil.isNullOrEmpty(city)) {
      addrText = addrText + CuramConst.gkCommaSpace + city;
    }
    if (!StringUtil.isNullOrEmpty(prov)) {
      addrText = addrText + CuramConst.gkCommaSpace + prov;
    }
    if (!StringUtil.isNullOrEmpty(countryCode)) {
      addrText = addrText + CuramConst.gkCommaSpace + countryCode;
    }
    if (!StringUtil.isNullOrEmpty(postalCode)) {
      addrText = addrText + CuramConst.gkCommaSpace + postalCode;
    }

    if (addrText.startsWith(CuramConst.gkCommaSpace)) {
      addrText = addrText.replaceFirst(CuramConst.gkCommaSpace, "");
    }

    return addrText;
  }

  @Override
  public BDMTaxSlipDataForPrint
    getTaxSlipDataForPrint(final BDMTaxSlipDataForPrintKey key)
      throws AppException, InformationalException {

    final BDMTaxSlipDataT4A bdmTaxSlipDataT4AObj =
      BDMTaxSlipDataT4AFactory.newInstance();
    final BDMTaxSlipDataT4AKey bdmTaxSlipDataT4AKey =
      new BDMTaxSlipDataT4AKey();
    bdmTaxSlipDataT4AKey.taxSlipDataID = key.taxSlipDataID;
    NotFoundIndicator nfIndicator = new NotFoundIndicator();
    final BDMTaxSlipDataT4ADtls t4AActiveDtls =
      bdmTaxSlipDataT4AObj.read(nfIndicator, bdmTaxSlipDataT4AKey);

    final BDMTaxSlipDataForPrint details = new BDMTaxSlipDataForPrint();

    if (!nfIndicator.isNotFound()) {
      details.formTypeCode = BDMTAXSLIPFORMTYPE.T4A;
      details.taxYear = t4AActiveDtls.taxYear;
    }

    final BDMTaxSlipDataRL1 bdmTaxSlipDataRL1Obj =
      BDMTaxSlipDataRL1Factory.newInstance();
    final BDMTaxSlipDataRL1Key bdmTaxSlipDataRL1Key =
      new BDMTaxSlipDataRL1Key();
    bdmTaxSlipDataRL1Key.taxSlipDataID = key.taxSlipDataID;
    nfIndicator = new NotFoundIndicator();
    final BDMTaxSlipDataRL1Dtls rL1ActiveDtls =
      bdmTaxSlipDataRL1Obj.read(nfIndicator, bdmTaxSlipDataRL1Key);

    if (!nfIndicator.isNotFound()) {
      details.formTypeCode = BDMTAXSLIPFORMTYPE.RL1;
      details.taxYear = rL1ActiveDtls.taxYear;
    }

    final BDMTaxSlipAttrData bdmTaxSlipAttrDataObj =
      BDMTaxSlipAttrDataFactory.newInstance();
    final BDMTaxSlipDataKey bdmTaxSlipDataKey = new BDMTaxSlipDataKey();
    bdmTaxSlipDataKey.taxSlipDataID = key.taxSlipDataID;
    final BDMTaxSlipAttrDetailsList bdmTaxSlipAttrDetailsList =
      bdmTaxSlipAttrDataObj.searchAttributesBySlipID(bdmTaxSlipDataKey);

    BDMTaxSlipPrintAttrDetails attrDetails = null;

    for (final BDMTaxSlipAttrDetails bdmTaxSlipAttrDetails : bdmTaxSlipAttrDetailsList.dtls) {
      attrDetails = new BDMTaxSlipPrintAttrDetails();
      attrDetails.boxID = bdmTaxSlipAttrDetails.boxID;
      // Format the attribute value based on domain type for display
      attrDetails.attrValue = this.formatAttrValueForDisplay(
        bdmTaxSlipAttrDetails.attrValue, bdmTaxSlipAttrDetails.domainType);

      if (key.decimalSplitInd && BDMTAXSLIPATTRDOMAINTYPE.AMOUNT
        .equals(bdmTaxSlipAttrDetails.domainType)) {
        final String stringValue =
          Double.valueOf(attrDetails.attrValue).toString();

        if (stringValue.indexOf(".") != -1) {
          attrDetails.attrValue =
            stringValue.substring(0, stringValue.indexOf("."));
          details.dtls.add(attrDetails);
          final BDMTaxSlipPrintAttrDetails attrDetails1 =
            new BDMTaxSlipPrintAttrDetails();
          attrDetails1.boxID = bdmTaxSlipAttrDetails.boxID + "_fraction";
          attrDetails1.attrValue =
            stringValue.substring(stringValue.indexOf("."));
          details.dtls.add(attrDetails1);
        } else {
          attrDetails.attrValue = stringValue;
          details.dtls.add(attrDetails);
          final BDMTaxSlipPrintAttrDetails attrDetails1 =
            new BDMTaxSlipPrintAttrDetails();
          attrDetails1.boxID = bdmTaxSlipAttrDetails.boxID + "_fraction";
          attrDetails1.attrValue = ".00";
          details.dtls.add(attrDetails1);
        }
      } else {
        details.dtls.add(attrDetails);
      }
    }

    return details;
  }

  /**
   * Updates the nextTaxSlipDataID for the predecessor record is possible
   */
  @Override
  public void t4AUpdateNextTaxSlipID(final BDMUpdateNextTaxSlipKey key)
    throws AppException, InformationalException {

    final BDMTaxSlipDataT4A t4aObj = BDMTaxSlipDataT4AFactory.newInstance();

    final BDMLatestTaxSlipByYearStatusKey latestTaxSlipKey =
      new BDMLatestTaxSlipByYearStatusKey();
    latestTaxSlipKey.concernRoleID = key.concernRoleID;
    latestTaxSlipKey.taxSlipStatusDeleted = BDMTAXSLIPSTATUS.DELETED;
    latestTaxSlipKey.taxYear = key.taxYear;

    try {
      final BDMTaxSlipDataVersionKey taxSlipVersionNoKey =
        t4aObj.readLatestTaxSlipByYear(latestTaxSlipKey);

      final BDMNextTaxSlipDataVersionNoDtls nextTaxSlipDtls =
        new BDMNextTaxSlipDataVersionNoDtls();
      nextTaxSlipDtls.nextTaxSlipDataID = key.nextTaxSlipDataID;
      nextTaxSlipDtls.versionNo = taxSlipVersionNoKey.versionNo;

      final BDMTaxSlipDataT4AKey t4aKey = new BDMTaxSlipDataT4AKey();
      t4aKey.taxSlipDataID = taxSlipVersionNoKey.taxSlipDataID;

      // update next tax slip data
      t4aObj.modifyNextTaxSlipDataID(t4aKey, nextTaxSlipDtls);
    } catch (final RecordNotFoundException e) {
      // do nothing if not found
    }

  }

  /**
   * Updates the nextTaxSlipDataID for the predecessor record is possible
   */
  @Override
  public void rl1UpdateNextTaxSlipID(final BDMUpdateNextTaxSlipKey key)
    throws AppException, InformationalException {

    final BDMTaxSlipDataRL1 rl1Obj = BDMTaxSlipDataRL1Factory.newInstance();

    final BDMLatestTaxSlipByYearStatusKey latestTaxSlipKey =
      new BDMLatestTaxSlipByYearStatusKey();
    latestTaxSlipKey.concernRoleID = key.concernRoleID;
    latestTaxSlipKey.taxSlipStatusDeleted = BDMTAXSLIPSTATUS.DELETED;
    latestTaxSlipKey.taxYear = key.taxYear;

    try {
      final BDMTaxSlipDataVersionKey taxSlipVersionNoKey =
        rl1Obj.readLatestTaxSlipByYear(latestTaxSlipKey);

      final BDMNextTaxSlipDataVersionNoDtls nextTaxSlipDtls =
        new BDMNextTaxSlipDataVersionNoDtls();
      nextTaxSlipDtls.nextTaxSlipDataID = key.nextTaxSlipDataID;
      nextTaxSlipDtls.versionNo = taxSlipVersionNoKey.versionNo;

      final BDMTaxSlipDataRL1Key rl1Key = new BDMTaxSlipDataRL1Key();
      rl1Key.taxSlipDataID = taxSlipVersionNoKey.taxSlipDataID;

      // update next tax slip data
      rl1Obj.modifyNextTaxSlipDataID(rl1Key, nextTaxSlipDtls);
    } catch (final RecordNotFoundException e) {
      // do nothing if not found
    }

  }

}
