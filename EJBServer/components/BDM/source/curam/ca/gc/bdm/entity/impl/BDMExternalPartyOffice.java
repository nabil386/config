package curam.ca.gc.bdm.entity.impl;

import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.ExternalPartyOfficeMemberFactory;
import curam.core.sl.entity.struct.ExternalPartyOfficeDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeKey;
import curam.core.sl.entity.struct.ExternalPartyOfficeMemberDtlsList;
import curam.core.sl.struct.ExternalPartyOfficeAddressDetails;
import curam.core.sl.struct.ExternalPartyOfficeAddressDetailsList;
import curam.core.sl.struct.ExternalPartyOfficePhoneNumberDetails;
import curam.core.sl.struct.ExternalPartyOfficePhoneNumberDetailsList;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.message.BPOEXTERNALPARTYOFFICE;
import curam.message.GENERAL;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;

public class BDMExternalPartyOffice
  extends curam.ca.gc.bdm.entity.base.BDMExternalPartyOffice {

  @Override
  public void modify(final ExternalPartyOfficeKey key,
    final ExternalPartyOfficeDtls details)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    super.modify(key, details);
  }

  @Override
  protected void premodify(final ExternalPartyOfficeKey key,
    final ExternalPartyOfficeDtls details)
    throws AppException, InformationalException {

    // Create an informational manager
    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // BEGIN, CR00099568, NP
    // An office with status of cancelled can not be updated
    if (details.recordStatus.equals(RECORDSTATUS.CANCELLED)) {
      final AppException e =
        new AppException(GENERAL.ERR_GENERAL_FV_NO_MODIFY_RECORD_CANCELLED);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(e.arg(true), CuramConst.gkEmpty,
          InformationalElement.InformationalType.kFatalError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          6);
    }
    // END, CR00099568

    //
    // validate that the update doesn't impact on the
    // time line relationship with associated children
    //
    // BEGIN, CR00101906, BD

    //
    // Read the External Party Office Address for cross record validations.
    //
    final curam.core.sl.intf.ExternalPartyOfficeAddress externalPartyOfficeAddressObj =
      curam.core.sl.fact.ExternalPartyOfficeAddressFactory.newInstance();

    final ExternalPartyOfficeAddressDetailsList officeAddressList =
      externalPartyOfficeAddressObj.listOfficeAddress(key);

    boolean activeAddressRecordsFound = false;

    if (!officeAddressList.dtls.isEmpty()) {

      // Find the latest related address start date & earliest related address
      // end date
      Date latestAddressStartDate = Date.kZeroDate;
      Date earliestAddressEndDate = Date.kZeroDate;

      for (int i = 0; i < officeAddressList.dtls.size(); i++) {

        final ExternalPartyOfficeAddressDetails addressDetails =
          officeAddressList.dtls.item(i);

        // For each active record
        if (!addressDetails.officeAddressDtls.recordStatus
          .equals(RECORDSTATUS.CANCELLED)) {

          activeAddressRecordsFound = true;

          // Find the latest address start date
          if (addressDetails.officeAddressDtls.startDate
            .after(latestAddressStartDate)) {
            latestAddressStartDate =
              addressDetails.officeAddressDtls.startDate;
          }

          // Find the earliest address end date
          if (earliestAddressEndDate.isZero()
            || addressDetails.officeAddressDtls.endDate
              .before(earliestAddressEndDate)) {
            earliestAddressEndDate = addressDetails.officeAddressDtls.endDate;

          }
        }
      }

      // The Start Date of an external party office must not be later than the
      // external party office address Start Date
      if (activeAddressRecordsFound) {

        if (details.startDate.after(latestAddressStartDate)) {

          final AppException e = new AppException(
            BPOEXTERNALPARTYOFFICE.ERR_XRV_OFFICE_STARTDATE_LATER_THAN_ADDRESS_STARTDATE);

          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
              CuramConst.gkEmpty,
              InformationalElement.InformationalType.kError,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              0);

        }

        // BEGIN, CR00101840 ,BD
        // The End Date of an external party office must not be earlier than the
        // external party office address End Date
        if (!details.endDate.isZero()
          && earliestAddressEndDate.after(details.endDate)) {
          // END, CR00101840

          final AppException e = new AppException(
            BPOEXTERNALPARTYOFFICE.ERR_XRV_OFFICE_ENDDATE_EARLIER_THAN_ADDRESS_ENDDATE);

          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
              CuramConst.gkEmpty,
              InformationalElement.InformationalType.kError,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              0);

        }
      }
    }

    //
    // Read the External Party Phone Number for cross record validations.
    //
    final curam.core.sl.intf.ExternalPartyOfficePhoneNumber externalPartyOfficePhoneNumberObj =
      curam.core.sl.fact.ExternalPartyOfficePhoneNumberFactory.newInstance();
    final ExternalPartyOfficePhoneNumberDetailsList officePhoneNumberList =
      externalPartyOfficePhoneNumberObj.listOfficePhoneNumber(key);

    boolean activePhoneNumberRecordsFound = false;

    if (!officePhoneNumberList.dtls.isEmpty()) {

      // Find the latest related phone number start date & earliest related
      // phone number end date
      Date latestPhoneNumberStartDate = Date.kZeroDate;
      Date earliestPhoneNumberEndDate = Date.kZeroDate;

      for (int i = 0; i < officePhoneNumberList.dtls.size(); i++) {

        final ExternalPartyOfficePhoneNumberDetails phoneNumberDetails =
          officePhoneNumberList.dtls.item(i);

        // For each active record
        if (!phoneNumberDetails.officePhoneNumberDtls.recordStatus
          .equals(RECORDSTATUS.CANCELLED)) {

          activePhoneNumberRecordsFound = true;

          // Find the latest phone number start date
          if (phoneNumberDetails.officePhoneNumberDtls.startDate
            .after(latestPhoneNumberStartDate)) {
            latestPhoneNumberStartDate =
              phoneNumberDetails.officePhoneNumberDtls.startDate;
          }

          // Find the earliest phone number end date
          if (earliestPhoneNumberEndDate.isZero()
            || phoneNumberDetails.officePhoneNumberDtls.endDate
              .before(earliestPhoneNumberEndDate)) {
            earliestPhoneNumberEndDate =
              phoneNumberDetails.officePhoneNumberDtls.endDate;

          }
        }
      }

      if (activePhoneNumberRecordsFound) {

        // The Start Date of an external party office must not be later than the
        // external party office phone number Start Date
        if (details.startDate.after(latestPhoneNumberStartDate)) {

          final AppException e = new AppException(
            BPOEXTERNALPARTYOFFICE.ERR_XRV_OFFICE_STARTDATE_LATER_THAN_PHONE_STARTDATE);

          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
              CuramConst.gkEmpty,
              InformationalElement.InformationalType.kError,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              0);

        }

        // BEGIN, CR00101840 ,BD
        // The End Date of an external party office must not be earlier than the
        // external party office phone number End Date
        if (earliestPhoneNumberEndDate.isZero() && !details.endDate.isZero()
          || !details.endDate.isZero()
            && earliestPhoneNumberEndDate.after(details.endDate)) {
          // END, CR00101840

          final AppException e = new AppException(
            BPOEXTERNALPARTYOFFICE.ERR_XRV_OFFICE_ENDDATE_EARLIER_THAN_PHONE_ENDDATE);

          curam.core.sl.infrastructure.impl.ValidationManagerFactory
            .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
              CuramConst.gkEmpty,
              InformationalElement.InformationalType.kError,
              curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
              0);

        }
      }
    }

    // END, CR00101906

    //
    // Read the associated External Party Office Members for cross record
    // validations.
    // Member dates must be within the external party date range.
    //
    // read all associated members
    final ExternalPartyOfficeMemberDtlsList associatedMemberList =
      ExternalPartyOfficeMemberFactory.newInstance()
        .searchByExternalPartyOfficeID(key);

    Date latestMemberStartDate = Date.kZeroDate;
    Date earliestMemberEndDate = Date.kZeroDate;
    boolean activeMemberRecordsFound = false;

    for (int i = 0; i < associatedMemberList.dtls.size(); i++) {
      // consider only active records
      if (!associatedMemberList.dtls.item(i).recordStatus
        .equals(RECORDSTATUS.CANCELLED)) {

        activeMemberRecordsFound = true;

        // read the concern role record to get the member dates
        final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

        concernRoleKey.concernRoleID =
          associatedMemberList.dtls.item(i).concernRoleID;
        final ConcernRoleDtls concernRoleDtls =
          ConcernRoleFactory.newInstance().read(concernRoleKey);

        // get the earliest and latest dates
        if (latestMemberStartDate.before(concernRoleDtls.startDate)) {
          latestMemberStartDate = concernRoleDtls.startDate;
        }
        if (earliestMemberEndDate.isZero()
          || earliestMemberEndDate.before(concernRoleDtls.endDate)) {
          earliestMemberEndDate = concernRoleDtls.endDate;
        }
      }
    } // end for members loop

    // throw an error if date line is out
    if (activeMemberRecordsFound) {

      if (latestMemberStartDate.before(details.startDate)) {
        final AppException e = new AppException(
          BPOEXTERNALPARTYOFFICE.ERR_XRV_OFFICE_STARTDATE_LATER_THAN_MEMBER_STARTDATE);

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            0);
      }
      // The End Date of an external party office must not be earlier than the
      // external party office member End Date
      if (earliestMemberEndDate.isZero() && !details.endDate.isZero()
        || !details.endDate.isZero()
          && earliestMemberEndDate.after(details.endDate)) {

        final AppException e = new AppException(
          BPOEXTERNALPARTYOFFICE.ERR_XRV_OFFICE_ENDDATE_EARLIER_THAN_MEMBER_ENDDATE);

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().addInfoMgrExceptionWithLookup(e.arg(true),
            CuramConst.gkEmpty, InformationalElement.InformationalType.kError,
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            0);
      }

    }

    informationalManager.failOperation();

  }

  @Override
  protected void autovalidate(final ExternalPartyOfficeDtls details)
    throws AppException, InformationalException {

    // TODO Auto-generated method stub
    super.autovalidate(details);
  }

}
