/**
 *
 */
package curam.ca.gc.bdm.sl.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.ca.gc.bdm.sl.fact.BDMDatabaseParticipantSearchFactory;
import curam.ca.gc.bdm.sl.struct.BDMThirdPartyParticipantSearchKey;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.fact.ParticipantSearchFactory;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.intf.ParticipantSearch;
import curam.core.sl.struct.AllParticipantSearchDetails;
import curam.core.sl.struct.AllParticipantSearchKey;
import curam.core.sl.struct.AllParticipantSearchResult;
import curam.core.sl.struct.CharCount;
import curam.core.sl.struct.SearchCriteriaString;
import curam.message.BPOPARTICIPANTSEARCH;
import curam.message.GENERALSEARCH;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Customized version of all participant search.
 *
 * This has more address elements in the search key than the OOTB version.
 *
 * @author donghua.jin
 *
 */
public class BDMThirdPartyParticipantSearchProcess
  extends curam.ca.gc.bdm.sl.base.BDMThirdPartyParticipantSearchProcess {

  /**
   * Do all participant search.
   *
   * @param key
   */
  @Override
  public AllParticipantSearchResult
    searchParticipant(final BDMThirdPartyParticipantSearchKey key)
      throws AppException, InformationalException {

    cleanseSearchKey(key);

    validateKey(key);

    AllParticipantSearchResult participantSearchResult =
      performOOTBAllParticipantSearch(key);

    participantSearchResult =
      BDMUtil.filterAddressForAllParticipant(participantSearchResult, key);

    if (participantSearchResult.dtlsList.isEmpty()) {

      final InformationalManager informationalManager =
        TransactionInfo.getInformationalManager();

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(
          new AppException(GENERALSEARCH.INF_SEARCH_NORECORDSFOUND),
          CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          8);

      informationalManager.failOperation();

    } else {
      // Sorting the list by concern role name.
      Collections.sort(participantSearchResult.dtlsList,
        new Comparator<AllParticipantSearchDetails>() {

          @Override
          public int compare(final AllParticipantSearchDetails obj1,
            final AllParticipantSearchDetails obj2) {

            return obj1.concernRoleName.compareTo(obj2.concernRoleName);
          }
        });
    }

    return getPerticipantSearchResult(participantSearchResult,
      Configuration.getIntProperty(EnvVars.ENV_PARTICIPANT_SEARCH_MAXIMUM));
  }

  /**
   * Call OOTB all participant search and pass in all supported
   * parameters.
   *
   * @param key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected AllParticipantSearchResult performOOTBAllParticipantSearch(
    final BDMThirdPartyParticipantSearchKey key)
    throws AppException, InformationalException {

    final AllParticipantSearchResult uniqueResult =
      new AllParticipantSearchResult();

    final Set<Long> crIDs = new HashSet<>();

    final curam.ca.gc.bdm.sl.intf.BDMDatabaseParticipantSearch participantSearchObj =
      BDMDatabaseParticipantSearchFactory.newInstance();

    final AllParticipantSearchKey allPartSearchKey =
      new AllParticipantSearchKey();

    allPartSearchKey.referenceNumber = key.referenceNumber;
    allPartSearchKey.concernRoleName = key.concernRoleName;
    allPartSearchKey.addressDtls.addressLine1 = key.addressLine1;
    allPartSearchKey.addressDtls.addressLine2 = key.addressLine2;
    allPartSearchKey.addressDtls.city = key.city;

    final AllParticipantSearchResult searchResult =
      participantSearchObj.searchAll(allPartSearchKey);

    for (final AllParticipantSearchDetails details : searchResult.dtlsList) {
      if (!crIDs.contains(details.concernRoleID)) {
        uniqueResult.dtlsList.addRef(details);
        crIDs.add(details.concernRoleID);
      }
    }

    return uniqueResult;
  }

  /**
   * Clean up the search key data.
   *
   * @param key
   */
  protected void
    cleanseSearchKey(final BDMThirdPartyParticipantSearchKey key) {

    key.referenceNumber = key.referenceNumber.trim();
    key.concernRoleName = key.concernRoleName.trim();
    key.unitNumber = key.unitNumber.trim();
    key.addressLine1 = key.addressLine1.trim();
    key.addressLine2 = key.addressLine2.trim();
    key.city = key.city.trim();
    key.stateProvince = key.stateProvince.trim();
    key.countryCode = key.countryCode.trim();
    key.postalCode = key.postalCode.trim();
  }

  /**
   * Validate search key.
   *
   * @param key
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateKey(final BDMThirdPartyParticipantSearchKey key)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();
    final ParticipantSearch participantSearchObj =
      ParticipantSearchFactory.newInstance();
    final SearchCriteriaString searchCriteriaString =
      new SearchCriteriaString();
    if (key.referenceNumber.length() == 0 && key.concernRoleName.length() == 0
      && key.unitNumber.length() == 0 && key.addressLine1.length() == 0
      && key.addressLine2.length() == 0 && key.city.length() == 0
      && key.stateProvince.length() == 0 && key.countryCode.length() == 0
      && key.postalCode.length() == 0) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(GENERALSEARCH.ERR_FV_SEARCH_CRITERIA_MISSING), "",
        InformationalType.kError, "a", 9);
    }

    informationalManager.failOperation();

    if (key.referenceNumber.isEmpty() && key.concernRoleName.isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_SEARCH_MANDATORY_MISSING), "",
        InformationalType.kError);
    }

    informationalManager.failOperation();

    CharCount count;

    if (!key.concernRoleName.isEmpty()) {
      searchCriteriaString.string = key.concernRoleName;
      count = participantSearchObj.countAlphaNumChar(searchCriteriaString);
      if (count.count < 2L) {
        informationalManager.addInformationalMsg(
          new AppException(
            BPOPARTICIPANTSEARCH.ERR_PARTICIPANTSEARCH_FV_NAME_SHORT).arg(2L),
          "", InformationalType.kError);
      }
    }

    if (!key.city.isEmpty() && key.city.length() < 2) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_CITY_2CHARS), "",
        InformationalType.kError);
    }

    if (!key.stateProvince.isEmpty() && key.stateProvince.length() < 3) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_PROVINCE_3CHARS), "",
        InformationalType.kError);
    }

    if (!key.postalCode.isEmpty() && key.postalCode.length() < 3) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_POSTALCODE_3CHARS), "",
        InformationalType.kError);
    }

    informationalManager.failOperation();
  }

  /**
   * Method which returns participant search search result according to the list
   * value set by the user
   *
   * @param key data on which the searched will be based and list size set by
   * the uses
   *
   * @return The sublist of participant search list
   */
  protected AllParticipantSearchResult getPerticipantSearchResult(
    final AllParticipantSearchResult participantSearchResult,
    final int userConfiguredListViewSize) {

    final AllParticipantSearchResult maximumParticipantSearchResults =
      new AllParticipantSearchResult();

    if (participantSearchResult.dtlsList
      .size() < userConfiguredListViewSize) {
      return participantSearchResult;
    } else {
      maximumParticipantSearchResults.dtlsList
        .addAll(participantSearchResult.dtlsList.subList(0,
          userConfiguredListViewSize));
      return maximumParticipantSearchResults;
    }
  }

}
