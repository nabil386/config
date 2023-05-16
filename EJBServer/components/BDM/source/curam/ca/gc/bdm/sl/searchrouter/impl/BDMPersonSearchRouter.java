package curam.ca.gc.bdm.sl.searchrouter.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.sl.impl.BDMSearchKeyValidator;
import curam.ca.gc.bdm.sl.impl.BDMSearchKeyValidatorIntf;
import curam.codetable.CONCERNROLETYPE;
import curam.core.impl.CuramConst;
import curam.core.impl.PersonSearchKeyValidatorIntf;
import curam.core.impl.SearchKeyValidator;
import curam.core.sl.entity.fact.NicknameFactory;
import curam.core.sl.entity.intf.Nickname;
import curam.core.sl.entity.struct.NicknameDtlsList;
import curam.core.sl.entity.struct.NicknameSearchKey;
import curam.core.struct.AddressMap;
import curam.core.struct.AddressMapList;
import curam.core.struct.OtherAddressData;
import curam.core.struct.PersonSearchKey1;
import curam.core.struct.PersonSearchResult1;
import curam.message.GENERALSEARCH;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.transaction.TransactionInfo;
import java.util.Map;
import java.util.regex.Pattern;

public class BDMPersonSearchRouter
  extends curam.ca.gc.bdm.sl.searchrouter.base.BDMPersonSearchRouter {

  @Inject
  private Map<String, SearchKeyValidator> searchKeyValidator;

  @Override
  public PersonSearchResult1 search1(final PersonSearchKey1 key)
    throws AppException, InformationalException {

    // BEGIN, CR00457404, CR00458133, JAY
    final curam.core.intf.AddressData addressDataObj =
      curam.core.fact.AddressDataFactory.newInstance();

    // Trim space characters from the beginning and end of search criteria which
    // may be accidentally entered
    key.birthSurname = key.birthSurname.trim();
    key.forename = key.forename.trim();
    key.surname = key.surname.trim();
    if (!key.addressDtls.addressSearchData.isEmpty()) {
      OtherAddressData otherAddressData = new OtherAddressData();

      otherAddressData.addressData = key.addressDtls.addressSearchData;
      final AddressMapList addressMapList =
        addressDataObj.parseDataToMap(otherAddressData);

      for (final AddressMap addressMap : addressMapList.dtls) {
        addressMap.name = addressMap.name.trim();
        addressMap.value = addressMap.value.trim();
      }
      otherAddressData = addressDataObj.parseMapToData(addressMapList);
      key.addressDtls.addressSearchData = otherAddressData.addressData;
    } else {
      key.addressDtls.addressLine1 = key.addressDtls.addressLine1.trim();
      key.addressDtls.addressLine2 = key.addressDtls.addressLine2.trim();
      key.addressDtls.city = key.addressDtls.city.trim();
    }
    // BEGIN, CR00461880, JAY
    // BEGIN, CR00461880, JAY
    final PersonSearchKeyValidatorIntf personSearchKeyValidator = (PersonSearchKeyValidatorIntf) searchKeyValidator.get(
            CONCERNROLETYPE.PERSON);

    // END, CR00461880, JAY

    BDMSearchKeyValidator bdmSearchKeyValidator = new BDMSearchKeyValidator();
    bdmSearchKeyValidator.validateSearchKey(key);
//    personSearchKeyValidator.validateSearchKey(key);
    // END, CR00457404, CR00458133, JAY
    // First search by the given first name
    final PersonSearchResult1 personSearchResult =
      getPersonSearchObj().search1(key);

    // Nickname search is ON.
    if (key.forename.length() > 0 && key.nicknameInd == true) {

      final String searchName = key.forename;

      // Return a list of associated names from the Nickname mapping table.
      final NicknameSearchKey nicknameKey = new NicknameSearchKey();

      nicknameKey.nickname = searchName.toUpperCase();
      final Nickname nicknameObj = NicknameFactory.newInstance();
      final NicknameDtlsList nicknameList =
        nicknameObj.searchByNickname(nicknameKey);

      // Search by any related names using the list returned from Nicknames
      // table
      Pattern searchString;

      for (int i = 0; i < nicknameList.dtls.size(); i++) {

        final String propername =
          nicknameList.dtls.item(i).properName.toUpperCase();

        // Remove duplicate search terms before searching...
        // Search returns <searchName>*, exclude any proper names that match
        // this pattern
        // e.g. Pat==Pat*==Patrick
        searchString = Pattern.compile(
          searchName.toUpperCase() + CuramConst.gkRegexWildcardChar,
          Pattern.CASE_INSENSITIVE);

        final boolean nicknameIsSubString =
          searchString.matcher(propername).matches();

        if (!nicknameIsSubString) {

          key.forename = propername;

          final PersonSearchResult1 nicknameSearchResults =
            getPersonSearchObj().search1(key);

          // Add the results to the return list.
          personSearchResult.dtlsList.addAll(nicknameSearchResults.dtlsList);
        }
      }
    }

    // If no search results are found, alert user
    // <216167>
    if (!key.bypassInfoMsgGeneration
      && personSearchResult.dtlsList.isEmpty()) {

      final InformationalManager informationalManager =
        TransactionInfo.getInformationalManager();

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(
          new AppException(GENERALSEARCH.INF_SEARCH_NORECORDSFOUND),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          5);

      informationalManager.failOperation();

    }

    return personSearchResult;
  }

}
