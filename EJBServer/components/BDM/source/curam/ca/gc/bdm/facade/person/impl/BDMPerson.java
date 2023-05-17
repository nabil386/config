package curam.ca.gc.bdm.facade.person.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchDetailsResult;
import curam.ca.gc.bdm.facade.person.struct.BDMPersonSearchKey1;
import curam.ca.gc.bdm.message.BDMPERSON;
import curam.core.facade.fact.PersonFactory;
import curam.core.facade.intf.Person;
import curam.core.facade.struct.PersonSearchDetailsResult;
import curam.core.facade.struct.PersonSearchKey1;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.message.GENERALSEARCH;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.transaction.TransactionInfo;

import java.util.Objects;

import static curam.ca.gc.bdm.util.impl.BDMSinNumberUtil.isValidSearchCombination;
import static curam.ca.gc.bdm.util.impl.BDMSinNumberUtil.isValidSearchCriteria;
import static curam.ca.gc.bdm.util.impl.BDMSinNumberUtil.maskSinNumber;

/**
 * @since AD0-7089
 * @author teja.konda
 *
 * custom Facade Wrapper class for Person Registration
 *
 */
public class BDMPerson extends curam.ca.gc.bdm.facade.person.base.BDMPerson {

  private final BDMUtil bdmUtil = new BDMUtil();

  /**
   *
   * Facade class for OOTB person Search as OOTB Person Search is hardcoded to
   * Street Number and Street Name Search
   */
  @Override
  public PersonSearchDetailsResult searchPerson(final PersonSearchKey1 key)
    throws AppException, InformationalException {

    // Unsetting Apt value so that OOTB can search the result
    String aptUnit = "";
    PersonSearchDetailsResult personSearchDetailsResult =
      new PersonSearchDetailsResult();
    if (!key.personSearchKey.addressDtls.addressLine2.isEmpty()) {

      aptUnit = key.personSearchKey.addressDtls.addressLine2;
      key.personSearchKey.addressDtls.addressLine2 = "";

    }

    // OOTB Logic
    final Person personObj = PersonFactory.newInstance();
    personSearchDetailsResult = personObj.searchPerson(key);

    // call filtering logic
    if (!aptUnit.isEmpty()
      && personSearchDetailsResult.personSearchResult.dtlsList.size() > 0) {

      personSearchDetailsResult = bdmUtil.filterAddressForPerson(
        personSearchDetailsResult, aptUnit.toUpperCase());

    }

    return personSearchDetailsResult;
  }

  /**
   * Person search - 8914
   * This method calls the OOTB person search and then filters search result by
   * using additional search criteria of province, postal code and country
   */
  @Override
  public BDMPersonSearchDetailsResult
    searchPersonExt(final BDMPersonSearchKey1 key)
      throws AppException, InformationalException {

    if (!isValidSearchCriteria(key.dtls.personSearchKey.referenceNumber,
      key.corrTrackingNumber, key.dtls.personSearchKey.forename,
      key.dtls.personSearchKey.surname,
      key.dtls.personSearchKey.birthSurname)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_INVALID_PERSON_SEARCH_CRITERIA), "",
        InformationalType.kError);
    }
    if (!isValidSearchCombination(key.dtls.personSearchKey.referenceNumber,
      key.corrTrackingNumber, key.dtls.personSearchKey.forename,
      key.dtls.personSearchKey.surname, key.dtls.personSearchKey.dateOfBirth,
      key.stateProvince, key.postalCode, key.countryCode,
      key.dtls.personSearchKey.birthSurname)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_SEARCH_ADDITIONAL_MANDATORY_MISSING),
        "", InformationalType.kError);
    }
    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final PersonSearchKey1 personSearchKey1 = new PersonSearchKey1();
    personSearchKey1.assign(key.dtls);

    // Unsetting Apt value so that OOTB can search the result

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (key.dtls.personSearchKey.referenceNumber.isEmpty()
      && key.corrTrackingNumber.isEmpty()
      && key.dtls.personSearchKey.forename.isEmpty()
      && key.dtls.personSearchKey.surname.isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_SEARCH_MANDATORY_MISSING), "",
        InformationalType.kError);
    }
    if (!key.dtls.personSearchKey.addressDtls.city.isEmpty()
      && key.dtls.personSearchKey.addressDtls.city.length() < 2) {
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
    String altId = null;

    // Perform search when both reference number and tracking number are
    // provided
    if (!key.corrTrackingNumber.isEmpty()
      && !key.dtls.personSearchKey.referenceNumber.isEmpty()) {

      // Perform OOTB serach for reference Number and additional search criteria
      final BDMPersonSearchDetailsResult refSearchResult =
        performOOTBSearch(key);

      // Perform serach by tracking Number
      altId = BDMUtil.getAlternateIDByTrackingNumber(key);
      if (!Objects.isNull(altId)) {
        key.dtls.personSearchKey.referenceNumber = altId;
      }

      // Perform serach by tracking Number and additional criteria
      final BDMPersonSearchDetailsResult trackingNumberSearchResult =
        performOOTBSearch(key);

      // comapre 2 results
      bdmPersonSearchDetailsResult =
        compareResults(refSearchResult, trackingNumberSearchResult);

    } else {
      // Perform search when either reference number or tracking number is
      // provided
      if (!key.corrTrackingNumber.isEmpty()
        && key.dtls.personSearchKey.referenceNumber.isEmpty()) {
        altId = BDMUtil.getAlternateIDByTrackingNumber(key);
      }

      // When only tracking number is specified
      if (!Objects.isNull(altId)) {
        key.dtls.personSearchKey.referenceNumber = altId;
      }
      // OOTB Logic
      bdmPersonSearchDetailsResult = performOOTBSearch(key);
    }

    bdmPersonSearchDetailsResult =
      BDMUtil.getPersonPrimaryAlternateID(bdmPersonSearchDetailsResult);

    if (bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(GENERALSEARCH.INF_SEARCH_NORECORDSFOUND), "",
        InformationalType.kError);
    }

    informationalManager.failOperation();
    maskSinNumber(bdmPersonSearchDetailsResult);
    return bdmPersonSearchDetailsResult;
  }

  /**
   * Person search popup - 8914
   * This method calls the OOTB person search and then filters search result by
   * using additional search criteria of province, postal code and country
   */
  @Override
  public BDMPersonSearchDetailsResult
    searchPersonForPopupExt(final BDMPersonSearchKey1 key)
      throws AppException, InformationalException {

    if (!isValidSearchCriteria(key.dtls.personSearchKey.referenceNumber,
      key.corrTrackingNumber, key.dtls.personSearchKey.forename,
      key.dtls.personSearchKey.surname,
      key.dtls.personSearchKey.birthSurname)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_INVALID_PERSON_SEARCH_CRITERIA), "",
        InformationalType.kError);
    }
    if (!isValidSearchCombination(key.dtls.personSearchKey.referenceNumber,
      key.corrTrackingNumber, key.dtls.personSearchKey.forename,
      key.dtls.personSearchKey.surname, key.dtls.personSearchKey.dateOfBirth,
      key.stateProvince, key.postalCode, key.countryCode,
      key.dtls.personSearchKey.birthSurname)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_SEARCH_ADDITIONAL_MANDATORY_MISSING),
        "", InformationalType.kError);
    }
    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();

    final PersonSearchKey1 personSearchKey1 = new PersonSearchKey1();
    personSearchKey1.assign(key.dtls);

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (key.dtls.personSearchKey.referenceNumber.isEmpty()
      && key.corrTrackingNumber.isEmpty()
      && key.dtls.personSearchKey.forename.isEmpty()
      && key.dtls.personSearchKey.surname.isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMPERSON.ERR_SEARCH_MANDATORY_MISSING), "",
        InformationalType.kError);
    }

    if (!key.dtls.personSearchKey.addressDtls.city.isEmpty()
      && key.dtls.personSearchKey.addressDtls.city.length() < 2) {
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

    String altId = null;

    // Perform search when both reference number and tracking number are
    // provided
    if (!key.corrTrackingNumber.isEmpty()
      && !key.dtls.personSearchKey.referenceNumber.isEmpty()) {

      // Perform OOTB serach for reference Number and additional search criteria
      final BDMPersonSearchDetailsResult refSearchResult =
        performOOTBSearch(key);

      // Perform serach by tracking Number
      altId = BDMUtil.getAlternateIDByTrackingNumber(key);
      if (!Objects.isNull(altId)) {
        key.dtls.personSearchKey.referenceNumber = altId;
      }

      // Perform serach by tracking Number and additional criteria
      final BDMPersonSearchDetailsResult trackingNumberSearchResult =
        performOOTBSearch(key);

      // comapre 2 results
      bdmPersonSearchDetailsResult =
        compareResults(refSearchResult, trackingNumberSearchResult);
    } else {// Perform search when either reference number or tracking number is
      // provided
      if (!key.corrTrackingNumber.isEmpty()
        && key.dtls.personSearchKey.referenceNumber.isEmpty()) {
        altId = BDMUtil.getAlternateIDByTrackingNumber(key);
      }

      // When only tracking number is specified
      if (!Objects.isNull(altId)) {
        key.dtls.personSearchKey.referenceNumber = altId;
      }

      // OOTB Logic
      bdmPersonSearchDetailsResult = performOOTBSearch(key);

    }

    if (bdmPersonSearchDetailsResult.personSearchResult.dtlsList.isEmpty()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(GENERALSEARCH.INF_SEARCH_NORECORDSFOUND), "",
        InformationalType.kError);
    }

    informationalManager.failOperation();
    maskSinNumber(bdmPersonSearchDetailsResult);
    return bdmPersonSearchDetailsResult;

  }

  /**
   * Compare person search result list
   */

  private BDMPersonSearchDetailsResult compareResults(
    final BDMPersonSearchDetailsResult refSearchResult,
    final BDMPersonSearchDetailsResult trackingNumberSearchResult) {

    final BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();
    if (refSearchResult.personSearchResult.dtlsList
      .size() == trackingNumberSearchResult.personSearchResult.dtlsList
        .size()) {

      if (trackingNumberSearchResult.personSearchResult.dtlsList
        .get(0).concernRoleID == refSearchResult.personSearchResult.dtlsList
          .get(0).concernRoleID) {

        bdmPersonSearchDetailsResult.assign(trackingNumberSearchResult);
      }

    }
    return bdmPersonSearchDetailsResult;
  }

  /**
   * Performs OOTB serach and filter results based on addition search criteria
   * Comment : Extractd OOTB search code to reduce Complexity of method
   */
  private BDMPersonSearchDetailsResult
    performOOTBSearch(final BDMPersonSearchKey1 personSearchKey1)
      throws AppException, InformationalException {

    BDMPersonSearchDetailsResult bdmPersonSearchDetailsResult =
      new BDMPersonSearchDetailsResult();
    // OOTB Logic
    final Person personObj = PersonFactory.newInstance();
    final PersonSearchDetailsResult personSearchDetailsResult =
      personObj.searchPerson(personSearchKey1.dtls);

    bdmPersonSearchDetailsResult.assign(personSearchDetailsResult);

    // call filtering logic
    if (bdmPersonSearchDetailsResult.personSearchResult.dtlsList.size() > 0) {

      bdmPersonSearchDetailsResult = bdmUtil.filterAddressForPerson(
        bdmPersonSearchDetailsResult, personSearchKey1);

    }
    return bdmPersonSearchDetailsResult;
  }

}
