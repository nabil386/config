/**
 *
 */
package curam.ca.gc.bdmoas.sl.organization.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdmoas.entity.organization.fact.BDMOASWorkqueueProvinceLinkFactory;
import curam.ca.gc.bdmoas.entity.organization.struct.BDMOASWorkqueueProvinceLinkKey;
import curam.ca.gc.bdmoas.sl.organization.struct.BDMOASPostCodeCountryKey;
import curam.codetable.COUNTRYCODE;
import curam.core.facade.fact.OrganizationFactory;
import curam.core.facade.intf.Organization;
import curam.core.facade.struct.ReadLocationDetails;
import curam.core.facade.struct.ReadLocationKey;
import curam.core.fact.AddressElementFactory;
import curam.core.fact.UsersFactory;
import curam.core.intf.AddressElement;
import curam.core.intf.Users;
import curam.core.struct.AddressElementDtls;
import curam.core.struct.AddressElementDtlsList;
import curam.core.struct.AddressKey;
import curam.core.struct.UserNameKey;
import curam.core.struct.UsersDtls;
import curam.core.struct.UsersKey;
import curam.supervisor.facade.struct.WorkQueueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.NotFoundIndicator;

/**
 * Maps postal code letters to work queues.
 * TODO Note: Needs to be revisited for work queue mapping IDs.
 *
 * @author abid.a.khan
 *
 */
public class BDMOASManageOfficeOfControl extends
  curam.ca.gc.bdmoas.sl.organization.base.BDMOASManageOfficeOfControl {

  private static final String LETTER_X = "X";

  /**
   * Holds mapping with key as the first n letter(s) of the
   * post code (n ∈ {1,3} ) and value as the designated work queue
   * corresponding to the region code for the province.
   *
   * Future design consideration: Make this property-driven if
   * changes to the work queue mapping is going to be required on the fly.
   */
  public static enum PostCodeToWorkQueue {

    A(80050), B(80051), C(80052), E(80053), G(80054), H(80054), J(80054),
    K(80055),
    // Timmins
    P(80055), L(80056), M(80056), N(80056),
    // O(80057),
    R(80058), S(80059), T(80060), X0A(80061), X0B(80061), X0C(80061),
    X0E(80062), X1A(80062), X0G(80062), V(80063), Y(80064), DEF(80012);

    private final long value;

    private PostCodeToWorkQueue(final long value) {

      this.value = value;
    }

    public long getValue() {

      return value;
    }

    public static PostCodeToWorkQueue findByName(final String name) {

      PostCodeToWorkQueue result = null;
      for (final PostCodeToWorkQueue workQueue : values()) {
        if (workQueue.name().equalsIgnoreCase(name)) {
          result = workQueue;
          break;
        }
      }
      return result;
    }
  }

  /**
   * Retrieves mapped work queue by user's location.
   * TODO: Change required based on new feature enhancement
   * as online client submissions need to be sorted out.
   */
  @Override
  public WorkQueueKey getWorkQueueByUser(final UserNameKey userKey)
    throws AppException, InformationalException {

    final WorkQueueKey workQueueKey = new WorkQueueKey();

    final AddressElement addressElement = AddressElementFactory.newInstance();

    BDMOASWorkqueueProvinceLinkKey bdmOASWorkqueueProvinceLinkKey = null;

    final Organization organization = OrganizationFactory.newInstance();

    final ReadLocationKey readLocationKey = new ReadLocationKey();

    final Users usersObj = UsersFactory.newInstance();

    final UsersKey usersKey = new UsersKey();
    usersKey.userName = userKey.userName;
    final NotFoundIndicator nfIndicator = new NotFoundIndicator();

    final UsersDtls usersDtls = usersObj.read(nfIndicator, usersKey);

    if (!nfIndicator.isNotFound()) {

      readLocationKey.locationKeyRef.locationID = usersDtls.locationID;

      final ReadLocationDetails readLocationDetails =
        organization.readLocation(readLocationKey);

      final AddressKey addressKey = new AddressKey();

      addressKey.addressID = readLocationDetails.locationDetails.addressID;

      final AddressElementDtlsList addressElementDtlsList =
        addressElement.readAddressElementDetails(addressKey);

      for (final AddressElementDtls addressElementDtls : addressElementDtlsList.dtls) {

        if (addressElementDtls.elementType
          .equals(BDMConstants.kADDRESSELEMENT_PROVINCE)
          || addressElementDtls.elementType
            .equals(BDMConstants.kADDRESSELEMENT_STATEPROV)) {

          bdmOASWorkqueueProvinceLinkKey =
            new BDMOASWorkqueueProvinceLinkKey();
          bdmOASWorkqueueProvinceLinkKey.provinceTypeCd =
            addressElementDtls.upperElementValue;

          final long workQueueID = BDMOASWorkqueueProvinceLinkFactory
            .newInstance().read(bdmOASWorkqueueProvinceLinkKey).workQueueID;
          workQueueKey.key.workQueueID = workQueueID;
          break;

        }

      }

    }

    return workQueueKey;
  }

  @Override
  public WorkQueueKey getWorkQueueByPostCodeOrCountry(
    final BDMOASPostCodeCountryKey postCodeCountryKey)
    throws AppException, InformationalException {

    final WorkQueueKey mappedWorkQueue = new WorkQueueKey();

    if (postCodeCountryKey.countryCode.equals(COUNTRYCODE.CACODE)
      && BDMOASOfficeOfControlHelper
        .isValidPostalCode(postCodeCountryKey.postCode)) {

      return getMappedWorkQByPostCode(postCodeCountryKey.postCode);
    }

    if (!postCodeCountryKey.countryCode.equals(COUNTRYCODE.CACODE)) {

      return getMappedWorkQByCountry(postCodeCountryKey.countryCode);
    }

    return mappedWorkQueue;
  }

  private WorkQueueKey getMappedWorkQByCountry(final String countryCode) {

    final WorkQueueKey mappedWorkQueue = new WorkQueueKey();

    return mappedWorkQueue;
  }

  private WorkQueueKey getMappedWorkQByPostCode(final String postCode) {

    final WorkQueueKey mappedWorkQueue = new WorkQueueKey();

    final String upperCasePostCode = postCode.toUpperCase();
    final String searchByChars =
      upperCasePostCode.substring(0, 1).startsWith(LETTER_X)
        ? upperCasePostCode.substring(0, 3)
        : upperCasePostCode.substring(0, 1);
    final PostCodeToWorkQueue postCodeWQInstance =
      PostCodeToWorkQueue.findByName(searchByChars);

    mappedWorkQueue.key.workQueueID =
      postCodeWQInstance != null ? postCodeWQInstance.getValue() : 0;

    return mappedWorkQueue;
  }

}
