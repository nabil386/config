package curam.ca.gc.bdm.sl.externalparty.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMIEGYESNOOPTIONAL;
import curam.ca.gc.bdm.codetable.BDMSSACNTRYTRANSCTNEVENTS;
import curam.ca.gc.bdm.entity.fact.BDMExternalPartyHistoryFactory;
import curam.ca.gc.bdm.entity.fact.BDMSSACountryAgreementFactory;
import curam.ca.gc.bdm.entity.intf.BDMExternalPartyHistory;
import curam.ca.gc.bdm.entity.intf.BDMSSACountryAgreement;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyHistSearchKey;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyHistoryDetails;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyHistoryDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMExtPartyTpAndStatusKey;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyDtls;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyHistoryDtls;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyHistoryKey;
import curam.ca.gc.bdm.entity.struct.BDMExternalPartyKey;
import curam.ca.gc.bdm.entity.struct.BDMReadSSACntryAgrmntKey;
import curam.ca.gc.bdm.entity.struct.BDMSSACntryAgrmntDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartyModifyDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMExternalPartySearchKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMReadExternalPartyHistoryKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMReadExternalPartyKey;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMSSACountryDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMSSACountryDetailsList;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMSSACountryHistoryDetails;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMSSACountryHistoryDetailsList;
import curam.ca.gc.bdm.facade.externalparty.struct.BDMSSACountryViewHistoryDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.externalparty.struct.BDMCountryCodeAndNameKey;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.ADDRESSSTATE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLEALTERNATEID;
import curam.codetable.CONCERNROLESTATUS;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.EXTERNALPARTYSTATUS;
import curam.codetable.EXTERNALPARTYTYPE;
import curam.codetable.LOCATIONSTATUS;
import curam.codetable.ORGOBJECTTYPE;
import curam.codetable.PROVINCETYPE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SENSITIVITY;
import curam.core.facade.struct.CodeTableItemDetails;
import curam.core.facade.struct.ExternalPartyRegistrationDetails;
import curam.core.facade.struct.ExternalPartySearchKey1;
import curam.core.facade.struct.ListItemsInCodeTable;
import curam.core.facade.struct.ParticipantSearchDetails;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.fact.ConcernRolePhoneNumberFactory;
import curam.core.fact.LocationFactory;
import curam.core.fact.MaintainAdminConcernRoleFactory;
import curam.core.fact.PhoneNumberFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.fact.UsersFactory;
import curam.core.impl.AllocateParticipantID;
import curam.core.impl.CuramConst;
import curam.core.intf.Address;
import curam.core.intf.AddressElement;
import curam.core.intf.Concern;
import curam.core.intf.ConcernRole;
import curam.core.intf.ConcernRoleAddress;
import curam.core.intf.ConcernRoleAlternateID;
import curam.core.intf.ConcernRolePhoneNumber;
import curam.core.intf.Location;
import curam.core.intf.MaintainAdminConcernRole;
import curam.core.intf.PhoneNumber;
import curam.core.intf.UniqueID;
import curam.core.sl.entity.fact.ExternalPartyFactory;
import curam.core.sl.entity.struct.ExternalPartyDtls;
import curam.core.sl.entity.struct.ExternalPartyKey;
import curam.core.sl.fact.ParticipantSearchRouterFactory;
import curam.core.sl.impl.Constants;
import curam.core.sl.infrastructure.impl.ValidationManager;
import curam.core.sl.intf.ParticipantSearchRouter;
import curam.core.sl.struct.ExternalPartyDetails;
import curam.core.sl.struct.ExternalPartyRegistrationIDDetails;
import curam.core.struct.AddressDtls;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.AdminConcernRoleDetails;
import curam.core.struct.AlternateIDTypeCodeKey;
import curam.core.struct.ConcernDtls;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.core.struct.ConcernRoleAlternateReadKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRolePhoneNumberDtls;
import curam.core.struct.DupConcernRoleAltIDDetailsList;
import curam.core.struct.InformationalMsgDtls;
import curam.core.struct.InformationalMsgDtlsList;
import curam.core.struct.LocationKey;
import curam.core.struct.MaintainAdminConcernRoleKey;
import curam.core.struct.PhoneNumberDtls;
import curam.core.struct.SearchCRTypeAltIDType;
import curam.core.struct.UserRoleDetails;
import curam.core.struct.UsersKey;
import curam.message.BPOMAINTAINCONCERNROLEALTID;
import curam.message.BPOMAINTAINCONCERNROLEPHONE;
import curam.message.GENERAL;
import curam.message.GENERALEXTERNALPARTY;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.MultipleRecordException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.FrequencyPattern;
import curam.util.type.NotFoundIndicator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BDMMaintainExternalParty
  extends curam.ca.gc.bdm.sl.externalparty.base.BDMMaintainExternalParty {

  // BEGIN, CR00312286, SG
  @Inject
  private Map<String, AllocateParticipantID> allocateParticipantIDMap;

  /**
   * Add injection.
   */
  public BDMMaintainExternalParty() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Method to create external party details.
   *
   * @param External party SSA country details.
   *
   * @return void
   */
  @Override
  public void createExternalParty(
    final ExternalPartyRegistrationDetails registrationDtls,
    final BDMExternalPartyDetails details)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.intf.BDMExternalParty externalPartyObj =
      curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory.newInstance();

    final BDMExternalPartyDtls extPartyDtls = new BDMExternalPartyDtls();

    extPartyDtls.assign(details);

    externalPartyObj.insert(extPartyDtls);

    // Create history record for creation of external party..
    final BDMExternalPartyHistory historyObj =
      BDMExternalPartyHistoryFactory.newInstance();

    final BDMExternalPartyHistoryDtls extPartyHistoryDtls =
      new BDMExternalPartyHistoryDtls();
    extPartyHistoryDtls.assign(details);

    // Unique id generator class
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();

    extPartyHistoryDtls.extPartyHistoryID = uniqueIDObj.getNextID();
    extPartyHistoryDtls.eventType =
      BDMSSACNTRYTRANSCTNEVENTS.SSACOUNTRYCREATED;
    extPartyHistoryDtls.comments =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.comments;
    extPartyHistoryDtls.name =
      registrationDtls.externalPartyRegistrationDetails.externalPartyDtls.name;

    historyObj.insert(extPartyHistoryDtls);
  }

  /**
   * Method to modify external party details.
   *
   * @param External party SSA country details.
   *
   * @return void
   */
  @Override
  public void modifyExternalParty(final BDMExternalPartyModifyDetails details)
    throws AppException, InformationalException {

    final BDMExternalPartyDetails bdmExternalPartyDetails =
      new BDMExternalPartyDetails();
    bdmExternalPartyDetails.assign(details);

    bdmExternalPartyDetails.concernRoleID =
      details.details.externalPartyDtls.concernRoleID;

    final curam.ca.gc.bdm.entity.intf.BDMExternalParty externalPartyObj =
      curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory.newInstance();

    final BDMExternalPartyKey extPartyKey = new BDMExternalPartyKey();
    extPartyKey.concernRoleID = bdmExternalPartyDetails.concernRoleID;

    final BDMExternalPartyDtls extPartyDtls =
      externalPartyObj.read(extPartyKey);

    extPartyDtls.assign(details);

    externalPartyObj.modify(extPartyKey, extPartyDtls);

    // Create history record for modification of external party..
    final BDMExternalPartyHistory historyObj =
      BDMExternalPartyHistoryFactory.newInstance();

    final BDMExternalPartyHistoryDtls extPartyHistoryDtls =
      new BDMExternalPartyHistoryDtls();
    extPartyHistoryDtls.assign(bdmExternalPartyDetails);

    // Unique id generator class
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();

    extPartyHistoryDtls.extPartyHistoryID = uniqueIDObj.getNextID();
    extPartyHistoryDtls.eventType =
      BDMSSACNTRYTRANSCTNEVENTS.SSACOUNTRYMODIFIED;
    extPartyHistoryDtls.comments = details.details.externalPartyDtls.comments;
    extPartyHistoryDtls.name = details.details.externalPartyDtls.name;

    historyObj.insert(extPartyHistoryDtls);
  }

  /**
   * Method to read external party details.
   *
   * @param External party read key
   *
   * @return External party details.
   */
  @Override
  public BDMExternalPartyDetails
    readExternalParty(final BDMReadExternalPartyKey key)
      throws AppException, InformationalException {

    final BDMExternalPartyDetails externalPartyDetails =
      new BDMExternalPartyDetails();

    final curam.ca.gc.bdm.entity.intf.BDMExternalParty externalPartyObj =
      curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory.newInstance();

    final BDMExternalPartyKey extPartyKey = new BDMExternalPartyKey();
    extPartyKey.concernRoleID = key.concernRoleID;

    final BDMExternalPartyDtls extPartyDtls =
      externalPartyObj.read(extPartyKey);

    externalPartyDetails.assign(extPartyDtls);

    return externalPartyDetails;
  }

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

    final ParticipantSearchRouter participantSearchRouter =
      ParticipantSearchRouterFactory.newInstance();

    ParticipantSearchDetails participantSearchDetails =
      new ParticipantSearchDetails();

    searchKey.key.key.concernRoleType = CONCERNROLETYPE.EXTERNALPARTY;
    searchKey.key.key.subtype = searchKey.type;

    participantSearchDetails.dtls =
      participantSearchRouter.search(searchKey.key.key);

    collectInformationalMsgs(participantSearchDetails.informationalMsgDtls);

    participantSearchDetails = this.filterExternalPartySearchResult(
      bdmSearchKey, participantSearchDetails);

    return participantSearchDetails;
  }

  /**
   * Collects the list of informations from the InformationalManager and add
   * them to the msgDtlsList parameter passed in.
   *
   * @param msgDtlsList
   * contains informational message details.
   */
  public void
    collectInformationalMsgs(final InformationalMsgDtlsList msgDtlsList) {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final String[] infos = informationalManager.obtainInformationalAsString();

    for (final String message : infos) {

      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();

      informationalMsgDtls.informationMsgTxt = message;

      msgDtlsList.dtls.addRef(informationalMsgDtls);
    }
  }

  /**
   * Method to filter the External Party search results.
   *
   * @param External Party search key, External Party search results to
   * filter.
   *
   * @return External Party search filtered results.
   */
  private ParticipantSearchDetails filterExternalPartySearchResult(
    final BDMExternalPartySearchKey searchKey,
    final ParticipantSearchDetails searchResultToFilter)
    throws AppException, InformationalException {

    ParticipantSearchDetails returnSearchResult =
      new ParticipantSearchDetails();

    final String province = searchKey.stateProvince.trim().toUpperCase();
    final String country = searchKey.countryCode.toUpperCase();
    final String postalCode = searchKey.postalCode.trim().toUpperCase();
    final String poBox = searchKey.poBox.trim().toUpperCase();

    final Set<Long> uniqueConcernRoleIDs = new HashSet<Long>();
    final ConcernRoleAddress concernroleAddressObj =
      ConcernRoleAddressFactory.newInstance();
    final ConcernRoleAddressKey concernRoleAddressKey =
      new ConcernRoleAddressKey();
    final ConcernRoleKey concernroleKey = new ConcernRoleKey();

    final AddressElement addressElement = AddressElementFactory.newInstance();
    final AddressKey addressKey = new AddressKey();
    AddressElementDtlsList addressElementDtlsList =
      new AddressElementDtlsList();
    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();
    try {

      for (final curam.core.sl.struct.ParticipantSearchDetails searchDetailsItem : searchResultToFilter.dtls.dtlsList) {
        boolean isProvinceMatched = false;
        boolean isCountryMatched = false;
        boolean isPostalCodeMatched = false;
        boolean isPOBoxMatched = false;

        // get AddressID of current ConcernroleID
        concernroleKey.concernRoleID = searchDetailsItem.concernRoleID;
        concernRoleAddressKey.concernRoleAddressID =
          concernroleAddressObj.readConcernRoleAddressDetailsByConcernRoleID(
            concernroleKey).concernRoleAddressID;
        addressKey.addressID =
          concernroleAddressObj.read(concernRoleAddressKey).addressID;

        // Read Address Elements for a given Address ID
        addressElementDtlsList =
          addressElement.readAddressElementDetails(addressKey);

        // Search Results
        // for (int j = 0; j < addressElementDtlsList.dtls.size(); j++) {
        for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {
          if (!country.isEmpty()
            && addressElementDtls.elementType
              .equals(ADDRESSELEMENTTYPE.COUNTRY)
            && addressElementDtls.upperElementValue.equals(country)) {
            isCountryMatched = true;
          }

          if (!postalCode.isEmpty()) {
            if (addressElementDtls.elementType
              .equals(ADDRESSELEMENTTYPE.POSTCODE)) {

              if (addressElementDtls.upperElementValue.contains(postalCode)) {
                isPostalCodeMatched = true;
              }

            } else if (addressElementDtls.elementType
              .equals(ADDRESSELEMENTTYPE.ZIP)) {
              if (addressElementDtls.upperElementValue.contains(postalCode)) {
                isPostalCodeMatched = true;
              }
            }
          }

          if (!poBox.isEmpty()
            && addressElementDtls.elementType
              .equals(ADDRESSELEMENTTYPE.POBOXNO)
            && addressElementDtls.upperElementValue.equals(poBox)) {
            isPOBoxMatched = true;
          }

          // Province search
          if (!province.isEmpty()) {
            // search by code table description for province and
            // stateprov
            if (addressElementDtls.elementType
              .equals(ADDRESSELEMENTTYPE.PROVINCE)) {
              // Search province name in code table description
              final CodeTableItemDetailsList list =
                codeTableAdminObj.searchByCodeTableItemDescription(
                  PROVINCETYPE.TABLENAME, "%" + province);

              for (int count = 0; count < list.dtls.size(); count++) {
                if (addressElementDtls.upperElementValue
                  .equals(list.dtls.get(count).code)) {
                  isProvinceMatched = true;
                  break;
                }
              } // end for
            } else if (addressElementDtls.elementType
              .equals(ADDRESSELEMENTTYPE.STATEPROV)) {

              final CodeTableItemDetailsList list =
                codeTableAdminObj.searchByCodeTableItemDescription(
                  ADDRESSSTATE.TABLENAME, "%" + province);

              for (int count = 0; count < list.dtls.size(); count++) {
                if (addressElementDtls.upperElementValue
                  .equals(list.dtls.get(count).code)) {
                  isProvinceMatched = true;
                  break;
                }
              } // end for
            } else if (addressElementDtls.elementType
              .equals(ADDRESSELEMENTTYPE.BDMSTPROV_X)) {
              // Added for international addresses
              if (addressElementDtls.upperElementValue.contains(province)) {
                isProvinceMatched = true;
                break;
              }
            }
          }
        }

        if (!province.isEmpty() && !isProvinceMatched) {
          continue;
        }
        if (!country.isEmpty() && !isCountryMatched) {
          continue;
        }
        if (!postalCode.isEmpty() && !isPostalCodeMatched) {
          continue;
        }

        if (!poBox.isEmpty() && !isPOBoxMatched) {
          continue;
        }

        if (uniqueConcernRoleIDs.contains(searchDetailsItem.concernRoleID)) {
          continue;
        } else {
          uniqueConcernRoleIDs.add(searchDetailsItem.concernRoleID);
        }

        returnSearchResult.dtls.dtlsList.addRef(searchDetailsItem);
      }

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "Error occured in Filtering address " + e);
      // if there is any exception then return the original list
      returnSearchResult = searchResultToFilter;
    }

    return returnSearchResult;
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

    final BDMSSACountryDetailsList ssaCountryDetailsList =
      new BDMSSACountryDetailsList();
    BDMSSACountryDetails ssACountryDetails = null;
    BDMSSACntryAgrmntDetails agrmntDetails = null;

    final curam.ca.gc.bdm.entity.intf.BDMExternalParty externalPartyObj =
      curam.ca.gc.bdm.entity.fact.BDMExternalPartyFactory.newInstance();

    final BDMExtPartyTpAndStatusKey typeAndStatusKey =
      new BDMExtPartyTpAndStatusKey();
    typeAndStatusKey.type = EXTERNALPARTYTYPE.SSACOUNTRY;
    typeAndStatusKey.status = EXTERNALPARTYSTATUS.ACTIVE;

    final curam.ca.gc.bdm.entity.struct.BDMSSACountryDetailsList cntryDetailsList =
      externalPartyObj.searchExternalPartiesByTypeAndStatus(typeAndStatusKey);

    for (final curam.ca.gc.bdm.entity.struct.BDMSSACountryDetails cntryDetails : cntryDetailsList.dtls) {
      ssACountryDetails = new BDMSSACountryDetails();
      ssACountryDetails.assign(cntryDetails);

      if (!cntryDetails.qcEntryStartDate.isZero()
        && cntryDetails.qcEntryEndDate.isZero()) {
        ssACountryDetails.entryWithQuebec = BDMIEGYESNOOPTIONAL.YES;
      } else {
        ssACountryDetails.entryWithQuebec = BDMIEGYESNOOPTIONAL.NO;
      }

      agrmntDetails = this.readSSACountryAgreementDetails(cntryDetails.name);
      ssACountryDetails.countryOfAgreement = agrmntDetails.agreementNumber;

      ssaCountryDetailsList.dtls.addRef(ssACountryDetails);
    }

    // Set dynamic indicators.
    final boolean isBDMAdminUser = this.isBDMAdminUser();

    ssaCountryDetailsList.newInd = isBDMAdminUser;

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

    final ListItemsInCodeTable listItemsInCodeTable =
      new ListItemsInCodeTable();

    final boolean isBDMAdmin = this.isBDMAdminUser();

    // Codetable maintenance business process object
    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();

    // CodeTableItemDetails struct
    CodeTableItemDetails codeTableItemDetails = null;

    final String codetableName = EXTERNALPARTYTYPE.TABLENAME;

    final String locale = TransactionInfo.getProgramLocale();
    final CodeTableItemDetailsList codeTableItemList = codeTableAdminObj
      .listAllItemsForLocaleAndLanguage(codetableName, locale);

    for (final curam.util.administration.struct.CodeTableItemDetails ctDetails : codeTableItemList.dtls
      .items()) {
      codeTableItemDetails = new CodeTableItemDetails();
      codeTableItemDetails.assign(ctDetails);
      codeTableItemDetails.selectableInd = ctDetails.isEnabled;
      codeTableItemDetails.languageCode = ctDetails.locale;

      // Add SSA country code only when user role is bdm admin.
      if (codeTableItemDetails.code.equals(EXTERNALPARTYTYPE.SSACOUNTRY)) {
        if (isBDMAdmin) {
          listItemsInCodeTable.codeTableItemDetails
            .addRef(codeTableItemDetails);
        }
      } else {
        listItemsInCodeTable.codeTableItemDetails
          .addRef(codeTableItemDetails);
      }
    }

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

    final BDMSSACountryHistoryDetailsList histtoryList =
      new BDMSSACountryHistoryDetailsList();
    BDMSSACountryHistoryDetails historyDetails = null;
    StringBuilder description = null;

    final BDMExternalPartyHistory historyObj =
      BDMExternalPartyHistoryFactory.newInstance();

    final BDMExtPartyHistSearchKey historyKey =
      new BDMExtPartyHistSearchKey();
    historyKey.concernRoleID = key.concernRoleID;

    final BDMExtPartyHistoryDetailsList extPartyHistoryDetailsList =
      historyObj.readHistoryByExtPartyID(historyKey);

    for (final BDMExtPartyHistoryDetails extPartyHistoryDetails : extPartyHistoryDetailsList.dtls) {
      historyDetails = new BDMSSACountryHistoryDetails();
      historyDetails.assign(extPartyHistoryDetails);

      // description.
      description = new StringBuilder();
      final String lang = TransactionInfo.getProgramLocale();
      if (extPartyHistoryDetails.eventType
        .equals(BDMSSACNTRYTRANSCTNEVENTS.SSACOUNTRYCREATED)) {

        if ("en".equalsIgnoreCase(lang)) {
          description.append(BDMConstants.kSSACountryCreated);
          description.append(Constants.kSpace);
          description.append(extPartyHistoryDetails.createdBy);
          description.append(Constants.kSpace);
          description.append(BDMConstants.kOn);
          description.append(Constants.kSpace);
          description.append(extPartyHistoryDetails.createdOn.toString());
        } else if ("fr".equalsIgnoreCase(lang)) {
          description.append(BDMConstants.kSSACountryCreated_fr);
          description.append(Constants.kSpace);
          description.append(extPartyHistoryDetails.createdBy);
          description.append(Constants.kSpace);
          description.append(BDMConstants.kOn_fr);
          description.append(Constants.kSpace);
          description.append(extPartyHistoryDetails.createdOn.toString());
        }
      } else {
        if ("en".equalsIgnoreCase(lang)) {
          description.append(BDMConstants.kSSACountryModified);
          description.append(Constants.kSpace);
          description.append(extPartyHistoryDetails.createdBy);
          description.append(Constants.kSpace);
          description.append(BDMConstants.kOn);
          description.append(Constants.kSpace);
          description.append(extPartyHistoryDetails.createdOn.toString());
        } else if ("fr".equalsIgnoreCase(lang)) {
          description.append(BDMConstants.kSSACountryModified_fr);
          description.append(Constants.kSpace);
          description.append(extPartyHistoryDetails.createdBy);
          description.append(Constants.kSpace);
          description.append(BDMConstants.kOn_fr);
          description.append(Constants.kSpace);
          description.append(extPartyHistoryDetails.createdOn.toString());
        }
      }

      historyDetails.description = description.toString();
      histtoryList.dtls.addRef(historyDetails);
    }

    return histtoryList;
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

    final BDMSSACountryViewHistoryDetails viewHistoryDetails =
      new BDMSSACountryViewHistoryDetails();

    final BDMExternalPartyHistory historyObj =
      BDMExternalPartyHistoryFactory.newInstance();

    final BDMExternalPartyHistoryKey extPartyHistoryKey =
      new BDMExternalPartyHistoryKey();

    extPartyHistoryKey.extPartyHistoryID = key.extPartyHistoryID;

    final BDMExternalPartyHistoryDtls extPartyHistoryDtls =
      historyObj.read(extPartyHistoryKey);

    viewHistoryDetails.assign(extPartyHistoryDtls);

    viewHistoryDetails.ssaLinkTitle =
      viewHistoryDetails.name + Constants.kSpace + BDMConstants.kAgreement;

    final BDMSSACntryAgrmntDetails agrmntDetails =
      this.readSSACountryAgreementDetails(viewHistoryDetails.name);
    viewHistoryDetails.countryOfAgreement = agrmntDetails.agreementNumber;

    return viewHistoryDetails;
  }

  /**
   * Method to check logged-in users role is BDMAdmin.
   *
   * @return boolean
   */
  private boolean isBDMAdminUser()
    throws AppException, InformationalException {

    boolean isBDMIOAdmin = false;

    final String user = TransactionInfo.getProgramUser();

    final UsersKey usersKey = new UsersKey();
    usersKey.userName = user;

    final UserRoleDetails userRoleDetails =
      UsersFactory.newInstance().readUserRole(usersKey);

    if (userRoleDetails.roleName.equalsIgnoreCase(BDMConstants.kIOAdmin)
      || userRoleDetails.roleName.contains(BDMConstants.kIOAdmin)) {
      isBDMIOAdmin = true;
    }

    return isBDMIOAdmin;
  }

  /**
   * Method to read SSA Country Agreement Details.
   *
   * @param Country Name
   * @return BDMSSACntryAgrmntDetails
   */
  private BDMSSACntryAgrmntDetails readSSACountryAgreementDetails(
    final String countryName) throws AppException, InformationalException {

    final BDMSSACountryAgreement ssaCntryObj =
      BDMSSACountryAgreementFactory.newInstance();

    final BDMReadSSACntryAgrmntKey agrmntKey = new BDMReadSSACntryAgrmntKey();
    agrmntKey.countryName = countryName.toUpperCase();
    agrmntKey.recordStatus = RECORDSTATUS.NORMAL;

    // Expectation here is only one active record should be present for a
    // country..
    BDMSSACntryAgrmntDetails ssaCntryAgrmntDetails = null;

    try {
      ssaCntryAgrmntDetails =
        ssaCntryObj.readAgrmntByNameAndStatus(agrmntKey);
    } catch (final RecordNotFoundException recNotFnExc) {
      // Do nothing.
      ssaCntryAgrmntDetails = new BDMSSACntryAgrmntDetails();
    } catch (final MultipleRecordException multiExc) {
      // Do nothing.
      ssaCntryAgrmntDetails = new BDMSSACntryAgrmntDetails();
    }

    return ssaCntryAgrmntDetails;
  }

  /**
   * Method to read SSA Country Agreement Details.
   *
   * @param Country Name
   * @return BDMSSACntryAgrmntDetails
   */
  @Override
  public BDMSSACntryAgrmntDetails
    readSSACountryAgrmntDetails(final BDMCountryCodeAndNameKey key)
      throws AppException, InformationalException {

    // Expectation here is only one active record should be present for a
    // country..
    final BDMSSACntryAgrmntDetails ssaCntryAgrmntDetails =
      this.readSSACountryAgreementDetails(key.countryName);

    return ssaCntryAgrmntDetails;
  }

  // BEGIN, CR00100137, KH
  // BEGIN, CR00099051, CW
  // BEGIN, CR00099223, CW
  // BEGIN, CR00099821, CW
  // ___________________________________________________________________________
  /**
   * This method registers an external party with the organization.
   *
   * @param externalPartyRegistrationDetails The external party details for
   * registration.
   *
   * @return The external party ID.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  public ExternalPartyRegistrationIDDetails registerExternalParty(
    final ExternalPartyDetails externalPartyRegistrationDetails)
    throws AppException, InformationalException {

    // END, CR00099223

    // BEGIN, CR00100412, KH
    // Create an informational manager
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();
    // END, CR00100412

    // BEGIN, CR00099173, CW
    // Initialize return object
    final ExternalPartyRegistrationIDDetails registrationIDDetails =
      new ExternalPartyRegistrationIDDetails();

    // Unique id generator class
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();

    // Current date used for validation
    final Date currentDate = Date.getCurrentDate();

    // Variables for concern registration
    final Concern concernObj = ConcernFactory.newInstance();
    final ConcernDtls concernDtls = new ConcernDtls();
    long concernID = 0;

    // Variables to prevent duplicate alternative ID's
    final AlternateIDTypeCodeKey alternateIDTypeCodeKey =
      new AlternateIDTypeCodeKey();
    boolean altIDExist = true;

    // Variables for concern role manipulation
    final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
    ConcernRoleDtls concernRoleDtls = new ConcernRoleDtls();

    // Concern role id (local)
    long lConcernRoleID = 0;
    // Concern role primary address id
    long primaryAddressID = 0;

    // Variables for concern role alternate id
    final ConcernRoleAlternateID concernRoleAlternateIDObj =
      ConcernRoleAlternateIDFactory.newInstance();

    ConcernRoleAlternateIDDtls concernRoleAlternateIDDtls =
      new ConcernRoleAlternateIDDtls();

    // Read concern role by alternate id key
    final ConcernRoleAlternateReadKey concernRoleAlternateReadKey =
      new ConcernRoleAlternateReadKey();

    // Concern role internal id
    long concernRoleInternalID = 0;

    // BEGIN, CR00099218, CW
    // Alternate external party reference number
    String alternateExternalPartyRefNo = CuramConst.gkEmpty;
    // END, CR00099218

    // Variables for External Party registration
    final ExternalPartyDtls externalPartyDtls = new ExternalPartyDtls();

    // Variables for concern role address
    final ConcernRoleAddress concernRoleAddressObj =
      ConcernRoleAddressFactory.newInstance();
    final ConcernRoleAddressDtls concernRoleAddressDtls =
      new ConcernRoleAddressDtls();
    long concernRoleAddressID = 0;

    // Variables for address registration
    final Address addressObj = AddressFactory.newInstance();
    final AddressDtls addressDtls = new AddressDtls();

    addressDtls.modifiableInd = true;

    // Variables for phone number
    final ConcernRolePhoneNumber concernRolePhoneNumberObj =
      ConcernRolePhoneNumberFactory.newInstance();
    final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
      new ConcernRolePhoneNumberDtls();
    long concernRolePhoneNumberID = 0;
    final PhoneNumber phoneNumberObj = PhoneNumberFactory.newInstance();
    final PhoneNumberDtls phoneNumberDtls = new PhoneNumberDtls();
    long phoneNumberID = 0;

    // Maintain admin concern role details
    final MaintainAdminConcernRole maintainAdminConcernRoleObj =
      MaintainAdminConcernRoleFactory.newInstance();
    final MaintainAdminConcernRoleKey maintainAdminConcernRoleKey =
      new MaintainAdminConcernRoleKey();

    // Validate the entry details
    validateExternalPartyDetails(externalPartyRegistrationDetails);

    // Generate the mandatory unique IDs required

    // Create new unique id for concern
    concernID = uniqueIDObj.getNextID();

    lConcernRoleID = uniqueIDObj.getNextID();

    // Validation to prevent duplicate alternative ID's
    // BEGIN, CR00096396, CW
    boolean recordNotFound = false;

    // END, CR00096396

    // BEGIN, CR00099218, CW
    // BEGIN, CR00099373, CW
    // If the entry details have specified a reference number then validate it
    if (externalPartyRegistrationDetails.externalPartyDtls.primaryAlternateID
      .length() != 0) {

      //
      // Reference number should be unique per external party
      //

      // Reference number on registration details
      alternateExternalPartyRefNo =
        externalPartyRegistrationDetails.externalPartyDtls.primaryAlternateID;
      alternateIDTypeCodeKey.typeCode =
        CONCERNROLEALTERNATEID.EXTERNALPARTY_REFERENCE_NUMBER;

      alternateIDTypeCodeKey.alternateID = alternateExternalPartyRefNo;
      alternateIDTypeCodeKey.statusCode = curam.codetable.RECORDSTATUS.NORMAL;

      // BEGIN, CR00394323, PMD
      final ValidationManager validationManagerObj =
        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager();

      if (validationManagerObj.enabled(
        BPOMAINTAINCONCERNROLEALTID.ERR_CONCERNROLEALTID_XRV_ID_TYPE_CONCERNTYPE_OVERLAP,
        curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
        10)) {

        final ConcernRoleAlternateID concernRoleAltIDObj =
          ConcernRoleAlternateIDFactory.newInstance();

        final SearchCRTypeAltIDType searchCRTypeAltIDType =
          new SearchCRTypeAltIDType();

        searchCRTypeAltIDType.alternateID = alternateExternalPartyRefNo;
        searchCRTypeAltIDType.alternateIDType =
          CONCERNROLEALTERNATEID.EXTERNALPARTY_REFERENCE_NUMBER;
        searchCRTypeAltIDType.statusCode = RECORDSTATUS.NORMAL;
        searchCRTypeAltIDType.concernRoleType = CONCERNROLETYPE.EXTERNALPARTY;

        final DupConcernRoleAltIDDetailsList dupConcernRoleAltIDDetailsList =
          concernRoleAltIDObj
            .searchByCRTypeAltIDAndType(searchCRTypeAltIDType);

        if (dupConcernRoleAltIDDetailsList.dtls.size() > 0) {

          final AppException ae = new AppException(
            BPOMAINTAINCONCERNROLEALTID.ERR_CONCERNROLEALTID_XRV_ID_TYPE_CONCERNTYPE_OVERLAP);

          ae.arg(CodeTable.getOneItem(CONCERNROLETYPE.TABLENAME,
            CONCERNROLETYPE.EXTERNALPARTY));
          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().throwWithLookup(ae,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              10);
        } else {
          recordNotFound = true;
        }
      } else {
        recordNotFound = true;
      }
      // END, CR00394323

      try {
        // Read concernRoleAternateID to see if this reference number already
        // exists for an external party
        concernRoleAlternateIDDtls = concernRoleAlternateIDObj
          .readByAltIDTypeCode(alternateIDTypeCodeKey);

        if (externalPartyRegistrationDetails.externalPartyDtls.primaryAlternateID
          .length() > 0
          && externalPartyRegistrationDetails.externalPartyDtls.primaryAlternateID
            .equals(concernRoleAlternateIDDtls.alternateID)) {

          // BEGIN, CR00100412, KH
          // External party already registered with this reference number
          final AppException ae = new AppException(
            GENERALEXTERNALPARTY.ERR_EXTERNALPARTY_REFERENCE_NUM_ALREADY_EXISTS);

          ae.arg(
            externalPartyRegistrationDetails.externalPartyDtls.primaryAlternateID);

          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().addInfoMgrExceptionWithLookup(ae.arg(true),
              CuramConst.gkEmpty,
              InformationalElement.InformationalType.kFatalError,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              2);
        }
      } catch (final MultipleRecordException e) {

        // BEGIN, CR00100412, KH
        // External party already registered with this reference number
        final AppException ae = new AppException(
          GENERALEXTERNALPARTY.ERR_EXTERNALPARTY_REFERENCE_NUM_ALREADY_EXISTS);

        ae.arg(
          externalPartyRegistrationDetails.externalPartyDtls.primaryAlternateID);
        // END, CR00099373

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(ae.arg(true),
            CuramConst.gkEmpty,
            InformationalElement.InformationalType.kFatalError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            0);
      } catch (final RecordNotFoundException e) {
        recordNotFound = true;
      }
      // BEGIN, CR00096396, CW
      if (recordNotFound) {
        concernRoleAlternateReadKey.primaryAlternateID =
          alternateExternalPartyRefNo.trim();

        concernRoleAlternateReadKey.statusCode =
          curam.codetable.CONCERNROLESTATUS.DEFAULTCODE;
        try {
          concernRoleDtls =
            concernRoleObj.readByAlternateID(concernRoleAlternateReadKey);

          if (concernRoleAlternateReadKey.primaryAlternateID
            .equals(concernRoleDtls.primaryAlternateID)) {

            // BEGIN, CR00100412, KH
            // BEGIN, CR00103091, MC
            // Two different messages were being returned for the same thing.
            final AppException e = new AppException(
              GENERALEXTERNALPARTY.ERR_EXTERNALPARTY_REFERENCE_NUM_ALREADY_EXISTS);

            e.arg(concernRoleAlternateReadKey.primaryAlternateID);

            curam.core.sl.infrastructure.impl.ValidationManagerFactory
              .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
                CuramConst.gkEmpty,
                InformationalElement.InformationalType.kFatalError,
                curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
                1);
          }
        } catch (final RecordNotFoundException e) {
          altIDExist = false;
        }
      }
      // END, CR00096396

    } else {
      //
      // Generate a new external party reference number
      //

      while (altIDExist) {

        // Generate unique id as reference number
        // BEGIN, CR00312286, SG
        alternateExternalPartyRefNo =
          allocateParticipantIDMap.get("EXTERNALPARTY").getNextID();
        // END, CR00312286

        alternateIDTypeCodeKey.typeCode =
          CONCERNROLEALTERNATEID.EXTERNALPARTY_REFERENCE_NUMBER;
        alternateIDTypeCodeKey.alternateID = alternateExternalPartyRefNo;
        alternateIDTypeCodeKey.statusCode =
          curam.codetable.RECORDSTATUS.NORMAL;

        recordNotFound = false;

        try {

          // Ensure the generated reference number hasn't been registered
          // against an external party
          concernRoleAlternateIDDtls = concernRoleAlternateIDObj
            .readByAltIDTypeCode(alternateIDTypeCodeKey);

          if (alternateExternalPartyRefNo
            .equals(concernRoleAlternateIDDtls.alternateID)) {
            // if found continue to generate the next number
            continue;
          }
        } catch (final MultipleRecordException e) {// BEGIN, CR00158767, LD
          /*
           * If you find possibility of more than one records in the database
           * and if you have any specific
           * exception message to display then implement it. In this case, we
           * are making sure system generated alternate ID is unique.
           * There could be an existing identical alternate id record, if any
           * then we are continuing until we get a unique one generated.
           */// END, CR00158767
        } catch (final RecordNotFoundException e) {
          recordNotFound = true;
        }

        if (recordNotFound) {

          // Read the concern role entity to ensure the alternate id hasn't been
          // registered
          concernRoleAlternateReadKey.primaryAlternateID =
            alternateExternalPartyRefNo;
          concernRoleAlternateReadKey.statusCode =
            CONCERNROLESTATUS.DEFAULTCODE;

          try {
            concernRoleDtls =
              concernRoleObj.readByAlternateID(concernRoleAlternateReadKey);
          } catch (final RecordNotFoundException e) {
            altIDExist = false;
          }
        } // end if recordNotFound
      } // end while altIDExist
    }
    // END, CR00099218

    // BEGIN, CR00101254, CW
    //
    // Create new concern (if it was not specified)
    //

    if (externalPartyRegistrationDetails.externalPartyDtls.concernRoleID == 0) {
      // Fill in the concernDetails
      concernDtls.concernID = concernID;
      concernDtls.creationDate = TransactionInfo.getSystemDate();

      // BEGIN, CR00099223, CW
      // BEGIN, CR00099271, CW
      // BEGIN, CR00099373, CW
      concernDtls.name =
        externalPartyRegistrationDetails.externalPartyDtls.name;
      // END, CR00099373
      // END, CR00099271
      concernDtls.comments = CuramConst.gkEmpty;

      // Insert concern record
      concernObj.insert(concernDtls);
    } else {

      // Concern was specified so read concernID
      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

      concernRoleKey.concernRoleID =
        externalPartyRegistrationDetails.externalPartyDtls.concernRoleID;
      concernRoleDtls = concernRoleObj.read(concernRoleKey);

      // Set concern ID in registration details
      concernID = concernRoleDtls.concernID;
    }
    // END, CR00101254

    //
    // Create address
    //

    // Fill in the data
    // Assign external party registration address details
    addressDtls.addressData = externalPartyRegistrationDetails.addressData;
    // END, CR00099223

    // Insert address record
    addressObj.insert(addressDtls);
    primaryAddressID = addressDtls.addressID;

    //
    // Create phone number
    //

    if (externalPartyRegistrationDetails.phoneType.length() > 0
      && externalPartyRegistrationDetails.phoneNumber.length() > 0) {

      // Fill in the data structure
      phoneNumberDtls.phoneNumber =
        externalPartyRegistrationDetails.phoneNumber;
      phoneNumberDtls.phoneAreaCode =
        externalPartyRegistrationDetails.phoneAreaCode;
      phoneNumberDtls.phoneCountryCode =
        externalPartyRegistrationDetails.phoneCountryCode;
      phoneNumberDtls.phoneExtension =
        externalPartyRegistrationDetails.phoneExtension;
      phoneNumberDtls.comments = CuramConst.gkEmpty;

      // Insert phone number record
      phoneNumberObj.insert(phoneNumberDtls);

      // BEGIN, CR00277540, ELG
      // Phone number iD is generated in the PhoneNumber.preinsert()
      // operation using range aware key set.
      phoneNumberID = phoneNumberDtls.phoneNumberID;
      // END, CR00277540

    }

    //
    // Create concern role
    //

    // Fill in the concern role details
    concernRoleDtls.concernRoleID = lConcernRoleID;

    // Use generated concernID
    concernRoleDtls.concernID = concernID;

    // BEGIN, CR00099218, CW
    concernRoleDtls.concernRoleType = CONCERNROLETYPE.EXTERNALPARTY;
    // END, CR00099218
    // BEGIN, CR00263967, MC
    concernRoleDtls.creationDate = TransactionInfo.getSystemDate();
    concernRoleDtls.registrationDate =
      externalPartyRegistrationDetails.registrationDate;
    concernRoleDtls.startDate =
      externalPartyRegistrationDetails.registrationDate;
    // END, CR00263967
    // BEGIN, CR00099223, CW
    // BEGIN, CR00099271, CW
    // BEGIN, CR00099373, CW
    concernRoleDtls.concernRoleName =
      externalPartyRegistrationDetails.externalPartyDtls.name;
    // END, CR00099373
    // END, CR00099271
    // END, CR00099223
    concernRoleDtls.primaryAddressID = primaryAddressID;
    // BEGIN, CR00099218, CW
    concernRoleDtls.primaryAlternateID = alternateExternalPartyRefNo;
    // END, CR00099218

    if (externalPartyRegistrationDetails.phoneType.length() > 0
      && externalPartyRegistrationDetails.phoneNumber.length() > 0) {
      concernRoleDtls.primaryPhoneNumberID = phoneNumberID;
    } else {
      concernRoleDtls.primaryPhoneNumberID = 0;
    }

    concernRoleDtls.primaryEmailAddressID = 0;
    concernRoleDtls.regUserName = TransactionInfo.getProgramUser();
    concernRoleDtls.comments = CuramConst.gkEmpty;
    // BEGIN, CR00099223, CW
    concernRoleDtls.prefPublicOfficeID =
      externalPartyRegistrationDetails.preferredPublicOfficeID;
    // END, CR00099223
    concernRoleDtls.preferredLanguage =
      externalPartyRegistrationDetails.preferredLanguage;
    concernRoleDtls.prefCommMethod =
      externalPartyRegistrationDetails.prefCommMethod;
    concernRoleDtls.sensitivity = SENSITIVITY.DEFAULTCODE;
    // BEGIN, CR00099490, SD
    // Set the initial status to Active
    concernRoleDtls.statusCode = CONCERNROLESTATUS.CURRENT;
    // END, CR00099490

    if (externalPartyRegistrationDetails.prefCommMethod.length() > 0) {
      concernRoleDtls.prefCommFromDate =
        externalPartyRegistrationDetails.registrationDate;
    }

    // Insert the concern role record to the database
    concernRoleObj.insert(concernRoleDtls);

    // Insert the External Party details
    externalPartyDtls.concernRoleID = lConcernRoleID;
    // BEGIN, CR00099373, CW
    externalPartyDtls.currencyType =
      externalPartyRegistrationDetails.externalPartyDtls.currencyType;
    externalPartyDtls.methodOfPmtCode =
      externalPartyRegistrationDetails.externalPartyDtls.methodOfPmtCode;
    // BEGIN, CR00099223, CW
    externalPartyDtls.name =
      externalPartyRegistrationDetails.externalPartyDtls.name;
    // END, CR00099223
    externalPartyDtls.paymentFrequency =
      externalPartyRegistrationDetails.externalPartyDtls.paymentFrequency;
    externalPartyDtls.type =
      externalPartyRegistrationDetails.externalPartyDtls.type;
    externalPartyDtls.verificationInd =
      externalPartyRegistrationDetails.externalPartyDtls.verificationInd;
    // END, CR00099373
    // BEGIN, CR00099218, CW
    externalPartyDtls.primaryAlternateID = alternateExternalPartyRefNo;
    // END, CR00099218

    externalPartyDtls.comments = CuramConst.gkEmpty;

    // Calculate the next payment date if payment frequency has been entered
    if (externalPartyDtls.paymentFrequency.length() != 0) {
      String frequencyPattern = CuramConst.gkEmpty;

      frequencyPattern = externalPartyDtls.paymentFrequency;
      externalPartyDtls.nextPaymentDate =
        new FrequencyPattern(frequencyPattern).getNextOccurrence(currentDate);
    }

    // Insert external party record
    ExternalPartyFactory.newInstance().insert(externalPartyDtls);

    // Generate unique id for the concern role internal ID
    concernRoleInternalID = uniqueIDObj.getNextID();

    // Generate unique id for concernRoleAddress
    concernRoleAddressID = uniqueIDObj.getNextID();

    // Generate unique id for concern role phone number
    if (externalPartyRegistrationDetails.phoneType.length() > 0
      && externalPartyRegistrationDetails.phoneNumber.length() > 0) {

      concernRolePhoneNumberID = uniqueIDObj.getNextID();
    }

    //
    // Create concern role alternate ID
    //

    // Fill in the data structure
    concernRoleAlternateIDDtls.concernRoleAlternateID = concernRoleInternalID;
    concernRoleAlternateIDDtls.concernRoleID = lConcernRoleID;
    // BEGIN, CR00099218, CW
    concernRoleAlternateIDDtls.alternateID = alternateExternalPartyRefNo;
    // BEGIN, CR00096396, CW
    // Set the type to EXTERNAL_PARTY_REFERENCE_NUMBER if the user entered
    // a reference number
    if (externalPartyRegistrationDetails.externalPartyDtls.primaryAlternateID
      .length() != 0) {
      // User entered a reference number so save the concernRoleAlternateID as
      // type ExternalPartyReferenceNumber
      concernRoleAlternateIDDtls.typeCode =
        CONCERNROLEALTERNATEID.EXTERNALPARTY_REFERENCE_NUMBER;
    } else {
      // System generated the reference number so save the
      // concernRoleAlternateID as type ReferenceNumber
      concernRoleAlternateIDDtls.typeCode =
        CONCERNROLEALTERNATEID.REFERENCE_NUMBER;
    }
    // END, CR00096396
    // END, CR00099218
    concernRoleAlternateIDDtls.startDate =
      externalPartyRegistrationDetails.registrationDate;
    concernRoleAlternateIDDtls.endDate = curam.util.type.Date.kZeroDate;
    concernRoleAlternateIDDtls.statusCode = RECORDSTATUS.NORMAL;
    concernRoleAlternateIDDtls.comments = CuramConst.gkEmpty;

    // Insert concernRoleAlternateID record to database
    concernRoleAlternateIDObj.insert(concernRoleAlternateIDDtls);

    //
    // Create concern role address
    //

    // Fill in the data
    concernRoleAddressDtls.concernRoleAddressID = concernRoleAddressID;
    concernRoleAddressDtls.concernRoleID = lConcernRoleID;
    concernRoleAddressDtls.addressID = primaryAddressID;
    concernRoleAddressDtls.startDate =
      externalPartyRegistrationDetails.registrationDate;
    concernRoleAddressDtls.endDate = Date.kZeroDate;
    concernRoleAddressDtls.statusCode = RECORDSTATUS.NORMAL;
    // BEGIN, CR00099223, CW
    // BEGIN, CR00099964, CW
    // Set the addressType to business
    concernRoleAddressDtls.typeCode = CONCERNROLEADDRESSTYPE.BUSINESS;
    // END, CR00099964
    // END, CR00099223

    // Insert concern role address record
    concernRoleAddressObj.insert(concernRoleAddressDtls);

    //
    // Create concern role contact
    //

    if (externalPartyRegistrationDetails.phoneType.length() > 0
      && externalPartyRegistrationDetails.phoneNumber.length() > 0) {

      // Fill in the concern role phone number data structure
      concernRolePhoneNumberDtls.concernRolePhoneNumberID =
        concernRolePhoneNumberID;
      concernRolePhoneNumberDtls.concernRoleID = lConcernRoleID;
      concernRolePhoneNumberDtls.phoneNumberID = phoneNumberID;
      concernRolePhoneNumberDtls.startDate =
        externalPartyRegistrationDetails.registrationDate;
      concernRolePhoneNumberDtls.endDate = Date.kZeroDate;
      concernRolePhoneNumberDtls.typeCode =
        externalPartyRegistrationDetails.phoneType;

      // Insert concernRolePhoneNumber to database
      concernRolePhoneNumberObj.insert(concernRolePhoneNumberDtls);

    }
    // BEGIN, CR00099218, CW
    registrationIDDetails.alternateID = alternateExternalPartyRefNo;
    // END, CR00099218
    registrationIDDetails.concernRoleID = lConcernRoleID;

    //
    // Create new concern role owner
    //

    // Set the key
    maintainAdminConcernRoleKey.concernRoleID = lConcernRoleID;

    // BEGIN, CR00176534, RPB
    final AdminConcernRoleDetails adminConcernRoleDetails =
      new AdminConcernRoleDetails();

    adminConcernRoleDetails.userName =
      curam.util.transaction.TransactionInfo.getProgramUser();
    adminConcernRoleDetails.startDate = currentDate;
    adminConcernRoleDetails.orgObjectType = ORGOBJECTTYPE.USER;
    maintainAdminConcernRoleObj.createConcernRoleOwner(
      maintainAdminConcernRoleKey, adminConcernRoleDetails);
    // END, CR00176534

    // BEGIN, CR00099174, SD
    // Set the key to send a notification to the external party.
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    concernRoleKey.concernRoleID = lConcernRoleID;

    // Send the registration notification.
    // Bug 112565: SMOKE TEST: IO Users not able to Register External Party
    // (SSA)
    // sendRegisterCompleteNotification(concernRoleKey);
    // END, CR00099174

    // Return the id details of created external party
    return registrationIDDetails;
  }

  // END, CR00099173
  // END, CR00099051

  // BEGIN, CR00100412, KH
  // BEGIN, CR00099173, CW
  // BEGIN, CR00099223, CW
  // BEGIN, CR00099363, CW
  // ___________________________________________________________________________
  /**
   * This method performs some business validations on the external party
   * details.
   *
   * @param externalPartyDetails Contains the external party details.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  protected void validateExternalPartyDetails(
    final ExternalPartyDetails externalPartyDetails)
    throws AppException, InformationalException {

    // END, CR00099223

    // Create an informational manager
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // BEGIN, CR00099424, CW
    // Registration date must be entered
    if (externalPartyDetails.registrationDate.isZero()) {

      final AppException e = new AppException(
        GENERALEXTERNALPARTY.ERR_EXTERNALPARTY_FV_REGISTRATION_DATE);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(e.arg(true), CuramConst.gkEmpty,
          InformationalElement.InformationalType.kFatalError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Registration date cannot be after current date
    if (externalPartyDetails.registrationDate.after(Date.getCurrentDate())) {

      final AppException e = new AppException(
        GENERALEXTERNALPARTY.ERR_EXTERNALPARTY_XFV_REG_DATE_LARGE);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(e.arg(true), CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Perform end date validations if an end date is entered
    if (!externalPartyDetails.endDate.isZero()) {

      // End date cannot be after current date
      if (externalPartyDetails.endDate.after(Date.getCurrentDate())) {

        final AppException e =
          new AppException(GENERAL.ERR_GENERAL_XFV_END_DATE_LARGE);

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            0);
      }
      // End date cannot be before registration date
      if (externalPartyDetails.endDate
        .before(externalPartyDetails.registrationDate)) {

        final AppException e = new AppException(
          GENERALEXTERNALPARTY.ERR_EXTERNALPARTY_XFV_REG_DATE_END_DATE);

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            0);
      }
    }
    // END, CR00099424

    // BEGIN, CR00099373, CW
    // Validate verification indicator
    if (externalPartyDetails.externalPartyDtls.type
      .equalsIgnoreCase(EXTERNALPARTYTYPE.COMMUNITYBASEDORG)
      && externalPartyDetails.externalPartyDtls.verificationInd
        .length() == 0) {

      // Verification indicator must be Yes or No when External party type of
      // CBO is selected
      final AppException e = new AppException(
        GENERALEXTERNALPARTY.ERR_EXTERNALPARTY_XFV_VERIFICATION_IND_TYPE);

      // BEGIN, CR00190241, NP
      final String externalParty =
        CodeTable.getOneItem(EXTERNALPARTYTYPE.TABLENAME,
          externalPartyDetails.externalPartyDtls.type,
          TransactionInfo.getProgramLocale());

      e.arg(externalParty);
      // END, CR00190241

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(e.arg(true), CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }
    // END, CR00099373

    // Validate the phone details
    validateExternalPartyPhoneDetails(externalPartyDetails);

    // Validate public office details
    validateExternalPartyPublicOffice(externalPartyDetails);

    // BEGIN, CR00099490, SD
    // Read the external party details if the record is being modified
    final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    // BEGIN, CR00099373, CW
    externalPartyKey.concernRoleID =
      externalPartyDetails.externalPartyDtls.concernRoleID;
    // END, CR00099373

    final ExternalPartyDtls externalPartyDtls =
      ExternalPartyFactory.newInstance().read(nfIndicator, externalPartyKey);

    // If a record was returned then the record is being modified
    if (!nfIndicator.isNotFound()) {

      // A closed external party cannot be modified
      if (externalPartyDtls.status.equals(EXTERNALPARTYSTATUS.CLOSED)
        && !externalPartyDetails.endDate.isZero()) {

        final AppException e =
          new AppException(GENERALEXTERNALPARTY.ERR_EXTERNALPARTY_CLOSED);

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            0);
      }
    }
    // END, CR00099490

    informationalManager.failOperation();
  }

  // ___________________________________________________________________________

  // ___________________________________________________________________________
  /**
   * This method performs some business validations on the external party
   * phone details.
   *
   * @param externalPartyDetails Contains the external party details.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  protected void validateExternalPartyPhoneDetails(
    final ExternalPartyDetails externalPartyDetails)
    throws AppException, InformationalException {

    // Create an informational manager
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // Phone details must contain a number and type if any other details
    // are entered
    if ((externalPartyDetails.phoneAreaCode.length() > 0
      || externalPartyDetails.phoneCountryCode.length() > 0
      || externalPartyDetails.phoneExtension.length() > 0)
      && externalPartyDetails.phoneNumber.length() == 0
      && externalPartyDetails.phoneType.length() == 0) {

      final AppException e = new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERNROLEPHONE_XFV_PHONENUMBER_PHONETYPE_EMPTY);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(e.arg(true), CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Phone type must be entered if number is entered
    if (externalPartyDetails.phoneType.length() == 0
      && externalPartyDetails.phoneNumber.length() != 0) {

      final AppException e = new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERNROLEPHONE_XFV_PHONETYPE_PHONENUMBER);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(e.arg(true), CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Phone number must be entered if type is entered
    if (externalPartyDetails.phoneNumber.length() == 0
      && externalPartyDetails.phoneType.length() != 0) {

      final AppException e = new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERNROLEPHONE_XFV_PHONENUMBER_PHONETYPE);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(e.arg(true), CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // Phone area code must be entered if number and type are entered
    if (externalPartyDetails.phoneNumber.length() != 0
      && externalPartyDetails.phoneType.length() != 0
      && externalPartyDetails.phoneAreaCode.length() == 0) {

      final AppException e = new AppException(
        BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERNROLEPHONE_XFV_PHONEAREACODE_EMPTY);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(e.arg(true), CuramConst.gkEmpty,
          InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }
  }

  // END, CR00100412

  // ___________________________________________________________________________
  /**
   * This method performs some business validations on the external party
   * public office details.
   *
   * @param externalPartyDetails Contains the external party details.
   *
   * @throws AppException Generic Exception Signature.
   * @throws InformationalException Generic Exception Signature.
   */
  @Override
  protected void validateExternalPartyPublicOffice(
    final ExternalPartyDetails externalPartyDetails)
    throws AppException, InformationalException {

    // Create an informational manager
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // Validation to prevent the user from selecting a canceled office location
    if (externalPartyDetails.preferredPublicOfficeID != 0) {

      // Location business process object
      final Location locationObj = LocationFactory.newInstance();
      final LocationKey locationKey = new LocationKey();

      locationKey.locationID = externalPartyDetails.preferredPublicOfficeID;

      // Read the public office status details
      final String locationStatus =
        locationObj.read(locationKey).locationStatus;

      // If the public office is closed
      if (locationStatus.equals(LOCATIONSTATUS.CLOSED)) {

        final AppException e =
          new AppException(GENERALEXTERNALPARTY.ERR_PUBLIC_OFFICE_CLOSED);

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            0);
      }

      // If the public office is deleted
      if (locationStatus.equals(RECORDSTATUS.CANCELLED)) {

        final AppException e =
          new AppException(GENERALEXTERNALPARTY.ERR_PUBLIC_OFFICE_CANCELED);

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            0);
      }
    }
  }

}
