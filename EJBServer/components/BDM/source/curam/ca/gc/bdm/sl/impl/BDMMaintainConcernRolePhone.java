package curam.ca.gc.bdm.sl.impl;

import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.entity.fact.BDMPhoneNumberFactory;
import curam.ca.gc.bdm.entity.intf.BDMPhoneNumber;
import curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberDetails;
import curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberKey;
import curam.ca.gc.bdm.sl.pdc.fact.BDMPDCPhoneNumberFactory;
import curam.ca.gc.bdm.sl.pdc.intf.BDMPDCPhoneNumber;
import curam.codetable.LOCATIONACCESSTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.impl.CuramConst;
import curam.core.impl.DataBasedSecurity;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.sl.struct.ParticipantSecurityCheckKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.ConcernRolePhoneDetails;
import curam.core.struct.ConcernRolePhoneKey;
import curam.core.struct.ConcernRolePhoneRMKey;
import curam.core.struct.DataBasedSecurityResult;
import curam.core.struct.InformationalMsgDtlsList;
import curam.core.struct.MaintainPhoneNumberKey;
import curam.core.struct.PhoneRMDtls;
import curam.core.struct.PhoneRMDtlsList;
import curam.core.struct.ReadMultiByConcernRoleIDPhoneResult;
import curam.message.BPOMAINTAINCONCERNROLEPHONE;
import curam.message.GENERALCASE;
import curam.message.GENERALCONCERN;
import curam.pdc.struct.ParticipantPhoneDetails;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.transaction.TransactionInfo;

public class BDMMaintainConcernRolePhone
  extends curam.ca.gc.bdm.sl.base.BDMMaintainConcernRolePhone {

  protected static final String kSpace = CuramConst.gkSpace;

  public BDMMaintainConcernRolePhone() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Retrieves a list of phone number details for the specified concern role.
   *
   *
   * @return concern role phone number details list and list of informational
   * messages
   */
  @Override
  public ReadMultiByConcernRoleIDPhoneResult
    readmultiByConcernRole(final MaintainPhoneNumberKey key)
      throws AppException, InformationalException {

    // list of phones
    final ReadMultiByConcernRoleIDPhoneResult readMultiByConcernRoleIDPhoneResult =
      new ReadMultiByConcernRoleIDPhoneResult();

    final curam.util.exception.InformationalManager informationalManager =
      curam.util.transaction.TransactionInfo.getInformationalManager();

    // Concern role phone number readmulti key
    final ConcernRolePhoneRMKey concernRolePhoneRMKey =
      new ConcernRolePhoneRMKey();

    // Phone number readmulti details list
    PhoneRMDtlsList phoneRMDtlsList;

    // Phone number readmulti details
    PhoneRMDtls phoneRMDtls;

    final curam.util.type.Date todaysDate =
      curam.util.type.Date.getCurrentDate();

    final curam.core.intf.ConcernRolePhoneNumber cnrRolePhoneNumberObj =
      curam.core.fact.ConcernRolePhoneNumberFactory.newInstance();

    // concern role variables
    final curam.core.intf.ConcernRole concernRoleObj =
      curam.core.fact.ConcernRoleFactory.newInstance();
    ConcernRoleDtls concernRoleDtls;
    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

    // Set the key
    concernRoleKey.concernRoleID = key.concernRoleID;

    // Read concern role using supplied parameter
    concernRoleDtls = concernRoleObj.read(concernRoleKey);

    // BEGIN, CR00227042, PM
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
    // END, CR00227042

    // Set the key
    concernRolePhoneRMKey.assign(key);

    phoneRMDtlsList =
      cnrRolePhoneNumberObj.searchPhonesByConcernRole(concernRolePhoneRMKey);

    readMultiByConcernRoleIDPhoneResult.details.dtls
      .ensureCapacity(phoneRMDtlsList.dtls.size());

    // Iterate through returned details

    for (int i = 0; i < phoneRMDtlsList.dtls.size(); i++) {
      // Task 21121 - populate non-cancelled phone number
      if (phoneRMDtlsList.dtls.item(i).statusCode
        .equals(RECORDSTATUS.NORMAL)) {

        phoneRMDtls = new PhoneRMDtls();

        phoneRMDtls.concernRolePhoneNumberID =
          phoneRMDtlsList.dtls.item(i).concernRolePhoneNumberID;
        phoneRMDtls.phoneAreaCode =
          phoneRMDtlsList.dtls.item(i).phoneAreaCode;
        phoneRMDtls.phoneNumber = phoneRMDtlsList.dtls.item(i).phoneNumber;
        phoneRMDtls.typeCode = phoneRMDtlsList.dtls.item(i).typeCode;
        phoneRMDtls.startDate = phoneRMDtlsList.dtls.item(i).startDate;
        phoneRMDtls.endDate = phoneRMDtlsList.dtls.item(i).endDate;
        phoneRMDtls.statusCode = phoneRMDtlsList.dtls.item(i).statusCode;
        phoneRMDtls.phoneExtension =
          phoneRMDtlsList.dtls.item(i).phoneExtension;
        phoneRMDtls.phoneNumberID =
          phoneRMDtlsList.dtls.item(i).phoneNumberID;

        // Task 21121 - BEGIN - Phone number with phone country code (code table
        // value)
        final String phoneCountryCodeDesc =
          getPhoneCountryCodeDesc(phoneRMDtls.phoneNumberID);
        phoneRMDtls.phoneCountryCode = phoneCountryCodeDesc;
        phoneRMDtls.phoneNumberString = phoneRMDtls.phoneCountryCode + kSpace
          + phoneRMDtls.phoneAreaCode + kSpace + phoneRMDtls.phoneNumber;
        // Task 21121 - END - Phone number with phone country code (code table
        // value)

        phoneRMDtls.phoneNumberVersionNo =
          phoneRMDtlsList.dtls.item(i).phoneNumberVersionNo;
        phoneRMDtls.concernRolePhoneNumberVersionNo =
          phoneRMDtlsList.dtls.item(i).concernRolePhoneNumberVersionNo;

        phoneRMDtls.primaryInd = phoneRMDtlsList.dtls
          .item(i).phoneNumberID == concernRoleDtls.primaryPhoneNumberID;

        if (phoneRMDtls.primaryInd) {

          if (!phoneRMDtlsList.dtls.item(i).endDate.isZero()
            && phoneRMDtlsList.dtls.item(i).endDate.before(todaysDate)) {

            // The end date has passed on the primary record, display an
            // informational saying as such.
            final AppException e = new AppException(
              BPOMAINTAINCONCERNROLEPHONE.ERR_CONCERNROLEEPHONE_FV_DATE_PASSED);

            curam.core.sl.infrastructure.impl.ValidationManagerFactory
              .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
                CuramConst.gkEmpty,
                curam.util.exception.InformationalElement.InformationalType.kWarning,
                curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
                0);

          }
        }
        readMultiByConcernRoleIDPhoneResult.details.dtls.addRef(phoneRMDtls);
      }

    }

    return readMultiByConcernRoleIDPhoneResult;
  }

  private String getPhoneCountryCodeDesc(final long phoneNumberID)
    throws AppException, InformationalException {

    final BDMPhoneNumber bdmphoneNumberObj =
      BDMPhoneNumberFactory.newInstance();
    final ReadBDMPhoneNumberKey readBDMPhoneNumberKey =
      new ReadBDMPhoneNumberKey();
    readBDMPhoneNumberKey.phoneNumberID = phoneNumberID;
    final ReadBDMPhoneNumberDetails readBDMPhoneNumberDetails =
      bdmphoneNumberObj.readBDMPhoneNumber(readBDMPhoneNumberKey);

    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();
    final CodeTableItemUniqueKey codeTableItemUniqueKey =
      new CodeTableItemUniqueKey();

    codeTableItemUniqueKey.code = readBDMPhoneNumberDetails.phoneCountryCode;
    codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
    codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;
    final String phoneCountryCodeDesc = codeTableAdminObj
      .readCTIDetailsForLocaleOrLanguage(codeTableItemUniqueKey).description;
    return phoneCountryCodeDesc;
  }

  // Task 99317 - resolve 99317 unable to create Phone Number for
  // repressentative - JP
  /**
   * Creates a new phone number entry for the specified concern role.
   *
   * @param key
   * maintain phone number key, contains concernRoleID
   * @param details
   * concern role phone details
   *
   * @return list of informational messages
   */
  @Override
  public InformationalMsgDtlsList createPhoneNumber(
    final MaintainPhoneNumberKey key, final ConcernRolePhoneDetails details)
    throws AppException, InformationalException {

    // BEGIN, CR00305948, SK
    return createPhoneNumberAndConcernRolePhoneNumber(key, details).message;
    // END, CR00305948

  }

  protected ConcernRolePhoneKey createPhoneNumberAndConcernRolePhoneNumber(
    final MaintainPhoneNumberKey maintainPhoneNumberKey,
    final ConcernRolePhoneDetails concernRolePhoneDetails)
    throws AppException, InformationalException {

    // BEGIN, CR00346264, JMA
    final ConcernRolePhoneKey concernRolePhoneKey = new ConcernRolePhoneKey();

    final BDMPDCPhoneNumber pdcPhoneNumberObj =
      BDMPDCPhoneNumberFactory.newInstance();
    final ParticipantPhoneDetails participantPhoneDetails =
      new ParticipantPhoneDetails();

    participantPhoneDetails.assign(concernRolePhoneDetails);
    participantPhoneDetails.assign(maintainPhoneNumberKey);

    concernRolePhoneKey.key.concernRolePhoneNumberID = pdcPhoneNumberObj
      .insert(participantPhoneDetails).concernRolePhoneNumberID;

    return concernRolePhoneKey;
    // END, CR00346264

  }

  // ___________________________________________________________________________
  /**
   * Modifies an existing phone number of the specified concern role.
   *
   * @param key
   * concern role phone number key details concern role phone number
   * details
   */
  @Override
  public void modifyPhoneNumber(final MaintainPhoneNumberKey key,
    final ConcernRolePhoneDetails details)
    throws AppException, InformationalException {

    // BEGIN, CR00346264, JMA
    final BDMPDCPhoneNumber pdcConcernRolePhoneNumberObj =
      BDMPDCPhoneNumberFactory.newInstance();
    final ParticipantPhoneDetails participantPhoneDetails =
      new ParticipantPhoneDetails();

    participantPhoneDetails.assign(details);
    participantPhoneDetails.assign(key);

    pdcConcernRolePhoneNumberObj.modify(participantPhoneDetails);
    // END, CR00346264, JMA
  }

  // END Task 99317 - resolve 99317 unable to create Phone Number for
  // repressentative - JP
}
