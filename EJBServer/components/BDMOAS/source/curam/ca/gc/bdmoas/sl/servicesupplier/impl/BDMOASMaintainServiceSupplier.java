package curam.ca.gc.bdmoas.sl.servicesupplier.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdmoas.entity.servicesupplier.fact.BDMOASServiceSupplierFactory;
import curam.ca.gc.bdmoas.entity.servicesupplier.intf.BDMOASServiceSupplier;
import curam.ca.gc.bdmoas.entity.servicesupplier.struct.BDMOASServiceSupplierDtls;
import curam.ca.gc.bdmoas.entity.servicesupplier.struct.BDMOASServiceSupplierKey;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppSearchDetails;
import curam.ca.gc.bdmoas.facade.servicesupplier.struct.BDMOASServSuppSearchKey;
import curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASReadServSuppTypeKey;
import curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppSearchResult;
import curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppTypeDetails;
import curam.codetable.ADDRESSELEMENTTYPE;
import curam.codetable.ADDRESSSTATE;
import curam.codetable.PROVINCETYPE;
import curam.core.facade.fact.ServiceSupplierFactory;
import curam.core.facade.struct.ParticipantSearchDetails;
import curam.core.facade.struct.ParticipantSearchKey;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.intf.AddressElement;
import curam.core.intf.ConcernRoleAddress;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleKey;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.NotFoundIndicator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Class to create, read and modify service supplier type details.
 *
 */
public class BDMOASMaintainServiceSupplier extends
  curam.ca.gc.bdmoas.sl.servicesupplier.base.BDMOASMaintainServiceSupplier {

  /**
   * Method to create service supplier type details.
   *
   * @param Service supplier type details
   *
   * @return void
   */
  @Override
  public void createServiceSupplierType(final BDMOASServSuppTypeDetails key)
    throws AppException, InformationalException {

    final BDMOASServiceSupplier srvcSuppObj =
      BDMOASServiceSupplierFactory.newInstance();

    final BDMOASServiceSupplierDtls serviceSupplierTypeDtls =
      new BDMOASServiceSupplierDtls();

    serviceSupplierTypeDtls.typeCode = key.typeCode;
    serviceSupplierTypeDtls.concernRoleID = key.concernRoleID;

    srvcSuppObj.insert(serviceSupplierTypeDtls);

  }

  /**
   * Method to modify service supplier type details.
   *
   * @param Service supplier modify details.
   *
   * @return void
   */
  @Override
  public void
    modifyServiceSupplierType(final BDMOASServSuppTypeDetails details)
      throws AppException, InformationalException {

    final BDMOASServiceSupplier srvcSuppObj =
      BDMOASServiceSupplierFactory.newInstance();

    final BDMOASServiceSupplierKey srvcSuppKey =
      new BDMOASServiceSupplierKey();
    srvcSuppKey.concernRoleID = details.concernRoleID;

    final BDMOASServiceSupplierDtls serviceSupplierTypeDtls =
      srvcSuppObj.read(srvcSuppKey);

    serviceSupplierTypeDtls.typeCode = details.typeCode;

    srvcSuppObj.modify(srvcSuppKey, serviceSupplierTypeDtls);

  }

  /**
   * Method to read service supplier type details.
   *
   * @param Service supplier read key
   *
   * @return Service Supplier Type details.
   */
  @Override
  public BDMOASServSuppTypeDetails
    readServiceSupplierType(final BDMOASReadServSuppTypeKey key)
      throws AppException, InformationalException {

    final BDMOASServSuppTypeDetails servSuppTypeDetails =
      new BDMOASServSuppTypeDetails();
    final BDMOASServiceSupplier srvcSuppObj =
      BDMOASServiceSupplierFactory.newInstance();

    final BDMOASServiceSupplierKey srvcSuppKey =
      new BDMOASServiceSupplierKey();
    srvcSuppKey.concernRoleID = key.concernRoleID;

    final BDMOASServiceSupplierDtls serviceSupplierTypeDtls =
      srvcSuppObj.read(srvcSuppKey);

    servSuppTypeDetails.assign(serviceSupplierTypeDtls);

    return servSuppTypeDetails;
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

    final BDMOASServSuppSearchDetails servSuppSearchDetails =
      new BDMOASServSuppSearchDetails();

    final ParticipantSearchKey participantSearchKey =
      new ParticipantSearchKey();

    participantSearchKey.key.assign(searchKey.key);

    final ParticipantSearchDetails participantSearchDetails =
      ServiceSupplierFactory.newInstance()
        .searchServiceSupplier(participantSearchKey);

    servSuppSearchDetails.assign(participantSearchDetails);

    final BDMOASServSuppSearchResult searchResultToFilter =
      servSuppSearchDetails.dtls;

    // Populate custom details.
    final BDMOASServiceSupplier srvcSuppObj =
      BDMOASServiceSupplierFactory.newInstance();

    final BDMOASServiceSupplierKey srvcSuppKey =
      new BDMOASServiceSupplierKey();

    BDMOASServiceSupplierDtls serviceSupplierTypeDtls = null;
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();
    for (final curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppSearchDetails servSuppSearchDetailsItem : searchResultToFilter.dtlsList) {
      srvcSuppKey.concernRoleID = servSuppSearchDetailsItem.concernRoleID;

      serviceSupplierTypeDtls = srvcSuppObj.read(nfIndicator, srvcSuppKey);

      if (!nfIndicator.isNotFound()) {
        servSuppSearchDetailsItem.typeCode = serviceSupplierTypeDtls.typeCode;
      }
    }

    servSuppSearchDetails.dtls = this
      .filterServiceSupplierSearchResult(searchKey.key, searchResultToFilter);

    return servSuppSearchDetails;
  }

  /**
   * Method to filter the service supplier search results.
   *
   * @param Service supplier search key, service supplier search results to
   * filter.
   *
   * @return Service Supplier search filtered results.
   */
  private BDMOASServSuppSearchResult filterServiceSupplierSearchResult(
    final curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppSearchKey searchKey,
    final BDMOASServSuppSearchResult searchResultToFilter)
    throws AppException, InformationalException {

    BDMOASServSuppSearchResult returnSearchResult =
      new BDMOASServSuppSearchResult();

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

      for (final curam.ca.gc.bdmoas.sl.servicesupplier.struct.BDMOASServSuppSearchDetails servSuppSearchDetailsItem : searchResultToFilter.dtlsList) {
        boolean isProvinceMatched = false;
        boolean isCountryMatched = false;
        boolean isPostalCodeMatched = false;
        boolean isPOBoxMatched = false;
        boolean isTypeCodeMatched = false;

        // get AddressID of current ConcernroleID
        concernroleKey.concernRoleID =
          servSuppSearchDetailsItem.concernRoleID;
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

        // Filter for type code.
        if (!StringUtil.isNullOrEmpty(searchKey.typeCode)
          && servSuppSearchDetailsItem.typeCode.equals(searchKey.typeCode)) {
          isTypeCodeMatched = true;
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

        if (!searchKey.typeCode.isEmpty() && !isTypeCodeMatched) {
          continue;
        }

        if (uniqueConcernRoleIDs
          .contains(servSuppSearchDetailsItem.concernRoleID)) {
          continue;
        } else {
          uniqueConcernRoleIDs.add(servSuppSearchDetailsItem.concernRoleID);
        }

        returnSearchResult.dtlsList.addRef(servSuppSearchDetailsItem);
      }

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + "Error occured in Filtering address " + e);
      // if there is any exception then return the original list
      returnSearchResult = searchResultToFilter;
    }

    return returnSearchResult;
  }
}
