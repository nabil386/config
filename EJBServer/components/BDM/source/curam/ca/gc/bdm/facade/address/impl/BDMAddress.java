package curam.ca.gc.bdm.facade.address.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.communication.impl.BDMCorrespondenceProcessingCenters;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.bdmaddress.fact.BDMAddressSearchFactory;
import curam.ca.gc.bdm.sl.bdmaddress.intf.BDMAddressSearch;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressDetailsStruct;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMAddressSearchResult;
import curam.ca.gc.bdm.sl.bdmaddress.struct.BDMRetrieveAddressKey;
import curam.codetable.LOCATIONACCESSTYPE;
import curam.core.impl.CuramConst;
import curam.core.impl.DataBasedSecurity;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.sl.struct.ParticipantSecurityCheckKey;
import curam.core.struct.AddressElementStruct;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressRMDtls;
import curam.core.struct.AddressReadMultiDtlsList;
import curam.core.struct.AddressReadMultiKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.DataBasedSecurityResult;
import curam.core.struct.InformationalMsgDtls;
import curam.core.struct.LocaleStruct;
import curam.core.struct.MaintainAddressKey;
import curam.core.struct.OtherAddressData;
import curam.core.struct.ReadMultiByConcRoleIDResult;
import curam.message.BPOADDRESS;
import curam.message.GENERALCASE;
import curam.message.GENERALCONCERN;
import curam.piwrapper.impl.Address;
import curam.piwrapper.impl.AddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.StringList;
import java.util.Map;

/**
 * Task 5155 - CKwong - Skeleton implementation of Address search
 * Implements address search functionality.
 * >
 */
public class BDMAddress
  extends curam.ca.gc.bdm.facade.address.base.BDMAddress {

  public BDMAddress() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Inject
  public AddressDAO addressDAO;

  @Inject
  BDMCorrespondenceProcessingCenters processingCenters;

  /**
   * Search address details. Returns list of addresses retrieved from
   * search system that match the entered post code.
   *
   * @param details
   * Contains the address details which will hold the address
   * search criteria such as postCode entered in the search pop up screen.
   *
   * @return AddressSearchResultDetails structure containing the search
   * address result list.
   *
   * @throws InformationalException
   * Generic exception signature.
   * @throws AppException
   * Generic Exception Signature.
   */
  @Override
  public BDMAddressSearchResult
    searchAddress(final BDMAddressDetailsStruct details)
      throws AppException, InformationalException {

    // init objects
    BDMAddressSearchResult addressSearchResult = new BDMAddressSearchResult();

    final BDMAddressSearch addressSearchObj =
      BDMAddressSearchFactory.newInstance();
    addressSearchResult = addressSearchObj.searchAddress(details);

    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (final String warningItem : warnings) {
      final InformationalMsgDtls informationalMsgDtls =
        new InformationalMsgDtls();
      informationalMsgDtls.informationMsgTxt = warningItem;
      addressSearchResult.dtls.addRef(informationalMsgDtls);
    }

    // TODO: search implementation

    /*
     * Commented out but this is the approach that should be taken to populate
     * the return struct once search has been implemented
     */
    /*
     * final AddressSearchDetails searchDtls = new AddressSearchDetails();
     * final AddressData addressDataObj = AddressDataFactory.newInstance();
     * final Address addressObj = AddressFactory.newInstance();
     * final AddressFieldDetails addressFieldDetails = new
     * AddressFieldDetails();
     * final OtherAddressData otherAddressData =
     * addressDataObj.parseFieldsToData(addressFieldDetails);
     * final OtherAddressData orderedAddressData =
     * addressObj.reorderAddressDataTags(otherAddressData);
     * final OtherAddressData formattedAddressData =
     * addressObj.getShortFormat(otherAddressData);
     * searchDtls.addressData = orderedAddressData.addressData;
     * searchDtls.formattedAddressData = formattedAddressData.addressData;
     * addressSearchResult.searchAddresses.detailsList.add(searchDtls);
     */

    return addressSearchResult;

  }

  /**
   * Retrieves address details from the passed in pipe separated list.
   *
   * @param key Contains address details in pipe separated list.
   * @return Structure containing address details.
   *
   * @throws InformationalException
   * Generic exception signature.
   * @throws AppException
   * Generic exception signature.
   */
  @Override
  public BDMAddressDetailsStruct
    retrieveAddress(final BDMRetrieveAddressKey key)
      throws AppException, InformationalException {

    // Return structure
    final BDMAddressDetailsStruct addressDetailsStruct =
      new BDMAddressDetailsStruct();

    /*
     * Commented out but this is the approach that should be taken to populate
     * the return struct once retrieval has been implemented
     */
    /*
     * final AddressData addressDataObj = AddressDataFactory.newInstance();
     * final Address addressObj = AddressFactory.newInstance();
     * final AddressFieldDetails addressFieldDetails = new
     * AddressFieldDetails();
     * final OtherAddressData otherAddressData =
     * addressDataObj.parseFieldsToData(addressFieldDetails);
     * final OtherAddressData orderedAddressData =
     * addressObj.reorderAddressDataTags(otherAddressData);
     * addressDetailsStruct.addressData = orderedAddressData.addressData;
     * addressDetailsStruct.postCode = addressFieldDetails.postalCode;
     */

    return addressDetailsStruct;

  }

  /**
   * Returns item from the string list for the list index specified.
   * If there is no item for a given index an empty string is returned.
   *
   * @param stringList String list.
   * @param itemIndex Item index.
   *
   * @return String item from the list.
   */
  private String getItemFromStringList(final StringList stringList,
    final int itemIndex) {

    String stringItem = new String();
    if (stringList.size() > itemIndex) {
      stringItem = stringList.item(itemIndex);
    } else {
      stringItem = "";
    }
    return stringItem;
  }

  @Override
  public OtherAddressData getCanadaPostAddressFormat(
    final AddressKey addressKey, final LocaleStruct locale)
    throws AppException, InformationalException {

    final OtherAddressData addressData = new OtherAddressData();
    final Map<String, String> addressElmMap =
      processingCenters.getAddressElementMap(addressKey.addressID);
    final Address address = addressDAO.get(addressKey.addressID);
    final String countryCode =
      addressDAO.get(addressKey.addressID).getAddressDetails().countryCode;
    if (addressElmMap.get(BDMConstants.kADDRESSELEMENT_COUNTRY) == null) {
      addressElmMap.put(BDMConstants.kADDRESSELEMENT_COUNTRY, countryCode);
    }

    addressData.addressData = processingCenters
      .mapAddrElmToCanadaPostAddressFormat(addressElmMap, locale);
    return addressData;
  }

  @Override
  public ReadMultiByConcRoleIDResult
    readmultiByConcernRoleID(final MaintainAddressKey key)
      throws AppException, InformationalException {

    final ReadMultiByConcRoleIDResult readMultiByConcRoleIDResult =
      new ReadMultiByConcRoleIDResult();

    // concern role address entity, struct, and key
    final curam.core.intf.ConcernRoleAddress concernRoleAddressObj =
      curam.core.fact.ConcernRoleAddressFactory.newInstance();
    final AddressReadMultiKey addressReadMultiKey = new AddressReadMultiKey();
    AddressReadMultiDtlsList addressReadMultiDtlsList;

    AddressRMDtls addressRMDtls;

    // concern role entity, struct, and key
    final curam.core.intf.ConcernRole concernRoleObj =
      curam.core.fact.ConcernRoleFactory.newInstance();
    ConcernRoleDtls concernRoleDtls;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    // based on domain CURAM_DATE
    final curam.util.type.Date todaysDate =
      curam.util.type.Date.getCurrentDate();

    // address variables
    final curam.core.intf.Address addressObj =
      curam.core.fact.AddressFactory.newInstance();
    // other address data is reused
    final OtherAddressData otherAddressData = new OtherAddressData();
    AddressElementStruct addressElementStruct;
    AddressElementStruct addressElementStructFirstLine;

    // Set the key for reading concernRole
    concernRoleKey.concernRoleID = key.concernRoleID;
    // Read concernRole from database
    concernRoleDtls = concernRoleObj.read(concernRoleKey);

    // BEGIN, CR00226619, PM
    final DataBasedSecurity dataBasedSecurity =
      SecurityImplementationFactory.get();
    final ParticipantSecurityCheckKey participantSecurityCheckKey =
      new ParticipantSecurityCheckKey();

    participantSecurityCheckKey.participantID = key.concernRoleID;
    participantSecurityCheckKey.type = LOCATIONACCESSTYPE.READ;
    final DataBasedSecurityResult dataBasedSecurityResult =
      dataBasedSecurity.checkParticipantSecurity(participantSecurityCheckKey);

    if (!dataBasedSecurityResult.result) {
      if (dataBasedSecurityResult.readOnly) {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_READONLY_RIGHTS);
      } else if (dataBasedSecurityResult.restricted) {
        throw new AppException(GENERALCONCERN.ERR_CONCERNROLE_FV_SENSITIVE);
      } else {
        throw new AppException(
          GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
      }
    }
    // END, CR00226619

    // Set the key for reading
    addressReadMultiKey.assign(key);
    // Read concernRoleAddress from database
    addressReadMultiDtlsList =
      concernRoleAddressObj.searchAddressesByConcernRole(addressReadMultiKey);

    // Reserve space
    readMultiByConcRoleIDResult.details.dtls
      .ensureCapacity(addressReadMultiDtlsList.dtls.size());

    // perform iteration
    for (int i = 0; i < addressReadMultiDtlsList.dtls.size(); i++) {

      addressRMDtls = new AddressRMDtls();

      // map structure
      addressRMDtls.assign(addressReadMultiDtlsList.dtls.item(i));
      // get first address line
      otherAddressData.addressData =
        addressReadMultiDtlsList.dtls.item(i).addressData;
      // BEGIN, CR00285272, ZV
      addressElementStructFirstLine = new AddressElementStruct();
      addressElementStructFirstLine.addressElementString =
        addressObj.getShortFormat(otherAddressData).addressData;
      // END, CR00285272

      addressRMDtls.addressLine1 =
        addressElementStructFirstLine.addressElementString;
      // BEGIN CR00096360, PN
      // BEGIN, CR00190258, CL
      // BEGIN, CR00214655, PB
      // BEGIN, CR00340652, KRK
      if (BPOADDRESS.TEXT_ADDRESS_UNAVAILABLE
        .getMessageText(TransactionInfo.getProgramLocale())
        .equals(addressRMDtls.addressLine1)) // END, CR00214655
      {
        // END, CR00340652
        // END, CR00190258
        // BEGIN, CR00222190, ELG
        addressRMDtls.addressLine1 =
          curam.message.BPOADDRESS.TEXT_ADDRESS_UNAVAILABLE// BEGIN,
            // CR00163471,
            // JC
            .getMessageText(TransactionInfo.getProgramLocale());
        // END, CR00163471, JC
        // END, CR00222190
      }
      // END CR00096360
      // get city
      addressElementStruct = addressObj.getCity(otherAddressData);
      addressRMDtls.city = addressElementStruct.addressElementString;

      // BEGIN, CR00190258, CL
      // BEGIN, CR00214655, PB
      // BEGIN, CR00340652, KRK
      if (CuramConst.gkDELocale.equals(TransactionInfo.getProgramLocale())
        && BPOADDRESS.TEXT_ADDRESS_UNAVAILABLE
          .getMessageText(TransactionInfo.getProgramLocale())
          .equals(addressRMDtls.city)) {
        // END, CR00340652
        // END, CR00214655
        addressRMDtls.city = CuramConst.gkSpace;
      }
      // END, CR00190258

      // if returned addressID and primary address ID are the same
      if (addressReadMultiDtlsList.dtls
        .item(i).addressID == concernRoleDtls.primaryAddressID) {
        addressRMDtls.primaryInd = true;

        if (!addressReadMultiDtlsList.dtls.item(i).endDate.isZero()
          && addressReadMultiDtlsList.dtls.item(i).endDate
            .before(todaysDate)) {

          // The end date has passed on the primary record, display an
          // informational saying as such.
          final AppException e = new AppException(
            curam.message.GENERALCONCERN.ERR_CONCERNROLE_FV_ADDRESS_DATE_PASSED);

          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
              CuramConst.gkEmpty,
              curam.util.exception.InformationalElement.InformationalType.kWarning,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              0);
        }

      } else {
        addressRMDtls.primaryInd = false;
      }

      if (!StringUtil.isNullOrEmpty(addressRMDtls.city)) {
        readMultiByConcRoleIDResult.details.dtls.addRef(addressRMDtls);
      }

    }

    return readMultiByConcRoleIDResult;
  }

}
