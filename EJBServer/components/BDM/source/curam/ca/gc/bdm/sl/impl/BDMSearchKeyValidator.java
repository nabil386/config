package curam.ca.gc.bdm.sl.impl;

import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.core.impl.EnvVars;
import curam.core.impl.SearchKeyValidator;
import curam.core.sl.fact.ParticipantSearchFactory;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
/*     */ import curam.core.sl.intf.ParticipantSearch;
import curam.core.sl.struct.CharCount;
import curam.core.sl.struct.SearchCriteriaString;
import curam.core.struct.AddressSearchKey;
import curam.core.struct.PersonSearchKey1;
import curam.message.BDMGENERALSEARCH;
import curam.message.BPOPERSONSEARCH;
import curam.message.GENERALSEARCH;
/*     */ import curam.util.exception.AppException;
/*     */ import curam.util.exception.InformationalElement.InformationalType;
/*     */ import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;

public class BDMSearchKeyValidator extends SearchKeyValidator
  implements BDMSearchKeyValidatorIntf {

  @Override
  public void validateSearchKey(final PersonSearchKey1 key)
    throws InformationalException, AppException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final ParticipantSearch participantSearchObj =
      ParticipantSearchFactory.newInstance();
    final SearchCriteriaString searchCriteriaString =
      new SearchCriteriaString();

    if (key.birthSurname.length() == 0 && key.forename.length() == 0
      && key.surname.length() == 0 && key.dateOfBirth.isZero()
      && key.referenceNumber.length() == 0 && key.gender.length() == 0
      && key.addressDtls.addressLine1.length() == 0
      && key.addressDtls.addressLine2.length() == 0
      && key.addressDtls.city.length() == 0) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(GENERALSEARCH.ERR_FV_SEARCH_CRITERIA_MISSING), "",
        InformationalType.kError, "a", 1);

    }

    CharCount count;
    if (0 < key.referenceNumber.length()) {
      searchCriteriaString.string = key.referenceNumber;
      count = participantSearchObj.countAlphaNumChar(searchCriteriaString);

      if (1L > count.count) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(GENERALSEARCH.ERR_SEARCH_FV_REFERENCE_NUMBER_SHORT)
            .arg(1L),
          "", InformationalType.kError, "a", 0);

      }
    }

    if (key.forename.length() > 0) {

      searchCriteriaString.string = key.forename;
      count = participantSearchObj.countAlphaNumChar(searchCriteriaString);

      if (count.count < 1L) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BPOPERSONSEARCH.ERR_PERSONSEARCH_FV_FIRST_NAME_SHORT).arg(1L),
          "", InformationalType.kError, "a", 0);

      } else if (count.count == 1L && count.containsWildcardIndOpt
        && key.surname.isEmpty()) {

        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BPOPERSONSEARCH.ERR_PERSONSEARCH_FV_FIRST_NAME_SHORT_PARTIAL_MATCH)
              .arg(2L),
            "", InformationalType.kError, "a", 0);

      }
    }

    if (key.surname.length() > 0) {

      searchCriteriaString.string = key.surname;
      count = participantSearchObj.countAlphaNumChar(searchCriteriaString);

      if (count.count < 1L) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BPOPERSONSEARCH.ERR_PERSONSEARCH_FV_LAST_NAME_SHORT).arg(1L),
          "", InformationalType.kError, "a", 0);

      } else if (count.count == 1L && count.containsWildcardIndOpt) {
        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BPOPERSONSEARCH.ERR_PERSONSEARCH_FV_LAST_NAME_SHORT_PARTIAL_MATCH)
              .arg(2L),
            "", InformationalType.kError, "a", 0);

      }
    }

    if (key.birthSurname.length() > 0) {

      searchCriteriaString.string = key.birthSurname;
      count = participantSearchObj.countAlphaNumChar(searchCriteriaString);

      if (count.count < 1L) {
        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BPOPERSONSEARCH.ERR_PERSONSEARCH_FV_BIRTH_LAST_NAME_SHORT)
              .arg(1L),
            "", InformationalType.kError, "a", 0);

      } else if (count.count == 1L && count.containsWildcardIndOpt) {
        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BPOPERSONSEARCH.ERR_PERSONSEARCH_FV_BIRTH_LAST_NAME_SHORT_PARTIAL_MATCH)
              .arg(2L),
            "", InformationalType.kError, "a", 0);

      }
    }

    this.validateAddressSearchKey(key.addressDtls, informationalManager);

    if (key.birthSurname.length() != 0 && key.forename.length() == 0
      && key.surname.length() == 0 && key.dateOfBirth.isZero()
      && key.referenceNumber.length() == 0
      && key.addressDtls.addressLine1.length() == 0
      && key.addressDtls.addressLine2.length() == 0) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BPOPERSONSEARCH.ERR_PERSONSEARCH_XFV_ADDITIONAL_CRITERIA),
        "", InformationalType.kError, "a", 2);

    }

    if (key.forename.length() == 0 && key.surname.length() == 0
      && key.dateOfBirth.isZero() && key.referenceNumber.length() == 0
      && key.gender.length() != 0
      && key.addressDtls.addressLine1.length() == 0
      && key.addressDtls.addressLine2.length() == 0) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BPOPERSONSEARCH.ERR_PERSONSEARCH_XFV_ADDITIONAL_CRITERIA),
        "", InformationalType.kError, "a", 1);

    }

    if (key.forename.length() == 0 && key.surname.length() == 0
      && key.dateOfBirth.isZero() && key.referenceNumber.length() == 0
      && key.addressDtls.addressLine1.length() == 0
      && key.addressDtls.addressLine2.length() == 0
      && key.addressDtls.city.length() != 0) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BPOPERSONSEARCH.ERR_PERSONSEARCH_XFV_ADDITIONAL_CRITERIA),
        "", InformationalType.kError, "a", 0);

    }

    informationalManager.failOperation();

    if (key.forename.length() == 1 && key.surname.isEmpty()
      || key.surname.length() == 1 || key.birthSurname.length() == 1) {

      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BPOPERSONSEARCH.INF_PERSONSEARCH_EXACT_MATCH), "",
        InformationalType.kWarning, "a", 0);

    }

  }

  @Override
  public void validateAddressSearchKey(final AddressSearchKey key,
    final InformationalManager informationalManager)
    throws AppException, InformationalException {

    final SearchCriteriaString searchCriteriaString =
      new SearchCriteriaString();

    final ParticipantSearch participantSearchObj =
      ParticipantSearchFactory.newInstance();

    String addressLayout =
      Configuration.getProperty(EnvVars.ENV_ADDRESS_LAYOUT);

    if (addressLayout == null || addressLayout.isEmpty()) {
      addressLayout = "US";

    }

    CharCount count;
    if (key.addressLine1.length() > 0) {

      final int addressLine2Minimum =
        addressLayout.equals(ADDRESSLAYOUTTYPE.CA_CIVIC) ? 1 : 2;

      searchCriteriaString.string = key.addressLine1;
      count = participantSearchObj.countAlphaNumChar(searchCriteriaString);

      if (count.count < addressLine2Minimum) {
        final AppException ae =
          addressLayout.equals(ADDRESSLAYOUTTYPE.CA_CIVIC)
            ? new AppException(
              GENERALSEARCH.ERR_SEARCH_FV_STREET_NUMBER_SHORT)
            : new AppException(
              BDMGENERALSEARCH.ERR_SEARCH_FV_ADDRESS_LINE_2_SHORT);

        informationalManager.addInformationalMsg(ae.arg(addressLine2Minimum),
          "", InformationalType.kError);

      }
    }

    if (key.addressLine2.length() > 0) {

      searchCriteriaString.string = key.addressLine2;
      count = participantSearchObj.countAlphaNumChar(searchCriteriaString);

      if (count.count < 2L) {
        final AppException ae =
          addressLayout.equals(ADDRESSLAYOUTTYPE.CA_CIVIC)
            ? new AppException(GENERALSEARCH.ERR_SEARCH_FV_STREET_NAME_SHORT)
            : new AppException(
              BDMGENERALSEARCH.ERR_SEARCH_FV_ADDRESS_LINE_3_SHORT);

        informationalManager.addInformationalMsg(ae.arg(2L), "",
          InformationalType.kError);

      }
    }

    if (key.city.length() > 0) {

      searchCriteriaString.string = key.city;
      count = participantSearchObj.countAlphaNumChar(searchCriteriaString);

      if (count.count < 2L) {
        informationalManager.addInformationalMsg(
          new AppException(GENERALSEARCH.ERR_SEARCH_FV_CITY_SHORT).arg(2L),
          "", InformationalType.kError);

      }
    }

  }

}
