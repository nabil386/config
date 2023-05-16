package curam.ca.gc.bdm.sl.impl;

import curam.codetable.CASESENSITIVITYEXCEPTIONS;
import curam.codetable.CASEUSERROLETYPE;
import curam.codetable.ORGOBJECTTYPE;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.DataBasedSecurity;
import curam.core.impl.EnvVars;
import curam.core.impl.SecurityImplementationFactory;
import curam.core.intf.CaseHeader;
import curam.core.sl.entity.fact.PositionHolderLinkFactory;
import curam.core.sl.entity.struct.CaseUserRoleDtls;
import curam.core.sl.entity.struct.CaseUserRoleKey;
import curam.core.sl.entity.struct.CurrentUserDetails;
import curam.core.sl.entity.struct.OrgObjectLinkDtls;
import curam.core.sl.entity.struct.OrgObjectLinkKey;
import curam.core.sl.entity.struct.OrgStructureUserDateAndStatusKey;
import curam.core.sl.entity.struct.OrganisationUnitKey;
import curam.core.sl.entity.struct.OrganisationUnitName;
import curam.core.sl.entity.struct.OrganisationUnitStatus;
import curam.core.sl.entity.struct.PositionKey;
import curam.core.sl.entity.struct.PositionName;
import curam.core.sl.entity.struct.PositionStatus;
import curam.core.sl.entity.struct.ReadByCaseStatusTypeKey;
import curam.core.sl.fact.UserAccessFactory;
import curam.core.sl.fact.UserRecentActionFactory;
import curam.core.sl.intf.UserAccess;
import curam.core.sl.intf.UserRecentAction;
import curam.core.sl.struct.CaseOwnerDetails;
import curam.core.sl.struct.ReasonEndDateComments;
import curam.core.sl.struct.RecordCount;
import curam.core.sl.struct.SendNotificationDetails;
import curam.core.sl.struct.SendNotificationInd;
import curam.core.sl.struct.UserRecentActionDetails;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseSecurityCheckKey;
import curam.core.struct.CaseTypeCode;
import curam.core.struct.DataBasedSecurityResult;
import curam.core.struct.SecurityResult;
import curam.core.struct.StatusCodeKeyStruct;
import curam.core.struct.UserFullname;
import curam.core.struct.UserNameKey;
import curam.events.CASE;
import curam.message.BPOCASEUSERROLE;
import curam.message.GENERALCASE;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.StringList;

/**
 * Business object for maintaining case user roles. Case user roles represent
 * the assignment of administration personnel to a case. An example would be
 * the owner of a case. The owner of a case will have the 'owner' case user
 * role.
 */
@curam.util.type.AccessLevel(curam.util.type.AccessLevelType.EXTERNAL)
public abstract class BDMCaseUserRole
  extends curam.ca.gc.bdm.sl.base.BDMCaseUserRole {

  /**
   * Modifies a case owner by closing the existing owner records and updating
   * them with new owner details. Notifications are sent to the previous and
   * new owner if specified and if the correct environment variables are set.
   *
   * @bowrite Organization
   * @boread CaseUserRole
   * @bowrite UserRecentAction
   * @bowrite CaseUserRole
   * @param key The unique identifier for the case.
   * @param ownerDtls The modified details of the case organization object or
   * user.
   * @param reasonEndDateComments Reason and comments associated with new owner.
   * @param sendNotificationInd If this indicator is set, a notification is sent
   * to the previous
   * and the new case owner, provided the correct environment variables are set.
   */
  @Override
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.EXTERNAL)
  public void modifyCaseOwner(final CaseHeaderKey key,
    final OrgObjectLinkDtls ownerDtls,
    final ReasonEndDateComments reasonEndDateComments,
    final SendNotificationInd sendNotificationInd)
    throws AppException, InformationalException {

    // END, CR00178555

    // orgObjectLinkKey struct
    OrgObjectLinkKey orgObjectLinkKey = new OrgObjectLinkKey();

    // caseUserRole entity and read key
    final curam.core.sl.entity.intf.CaseUserRole caseUserRoleObj =
      curam.core.sl.entity.fact.CaseUserRoleFactory.newInstance();
    final ReadByCaseStatusTypeKey readByCaseStatusTypeKey =
      new ReadByCaseStatusTypeKey();
    final CaseUserRoleDtls caseUserRoleDtls = new CaseUserRoleDtls();

    // struct to read the current owner
    CurrentUserDetails currentUserDetails = new CurrentUserDetails();

    // key of current owner CaseUserRole which we will cancel
    final CaseUserRoleKey cancelCaseUserRoleKey = new CaseUserRoleKey();

    // BEGIN, CR00075609, SPD
    // notifications struct
    final SendNotificationDetails sendNotificationDetails =
      new SendNotificationDetails();
    // END, CR00075609

    // BEGIN, CR00076062, SPD
    // UserRecentAction manipulation variables
    final UserRecentAction userRecentActionObj =
      UserRecentActionFactory.newInstance();
    final UserRecentActionDetails userRecentActionDetails =
      new UserRecentActionDetails();

    // END, CR00076062

    // BEGIN CR00100436
    if (key.caseID != 0) {

      final curam.core.struct.CaseKey caseKey =
        new curam.core.struct.CaseKey();

      caseKey.caseID = key.caseID;

      final SecurityResult isSensitivityException =
        checkSensitivityExceptions(caseKey);

      if (!isSensitivityException.result) {

        // BEGIN, CR00227042, PM
        final DataBasedSecurity dataBasedSecurity =
          SecurityImplementationFactory.get();
        final CaseSecurityCheckKey caseSecurityCheckKey =
          new CaseSecurityCheckKey();
        DataBasedSecurityResult dataBasedSecurityResult =
          new DataBasedSecurityResult();

        caseSecurityCheckKey.caseID = key.caseID;
        caseSecurityCheckKey.type = DataBasedSecurity.kMaintainSecurityCheck;

        // call security check
        try {
          dataBasedSecurityResult =
            dataBasedSecurity.checkCaseSecurity1(caseSecurityCheckKey);
        } catch (final curam.util.exception.RecordNotFoundException e) {// Databased
          // security
          // checks for
          // case
          // supervisor
          // and as
          // one has yet to be created the exception is ignored
        }

        // if the result is false, it means the user does not have access to
        // read this case
        if (!dataBasedSecurityResult.result) {
          if (dataBasedSecurityResult.restricted) {
            throw new AppException(GENERALCASE.ERR_CASESECURITY_CHECK_RIGHTS);
          } else {
            throw new AppException(
              GENERALCASE.ERR_CASESECURITY_CHECK_ACCESS_RIGHTS);
          }
        }
        // END, CR00227042
      }
    }
    // END, CR00100436

    String genCaseOwnerChange;

    validateCaseOwner(key, ownerDtls);

    // create OrgObjectLink which will be stored on the CaseUserRole
    orgObjectLinkKey = curam.core.sl.fact.CaseUserRoleFactory.newInstance()
      .createOrgObjectLink(ownerDtls);

    // set caseUserRoleDtls
    caseUserRoleDtls.caseID = key.caseID;
    caseUserRoleDtls.orgObjectLinkID = orgObjectLinkKey.orgObjectLinkID;
    caseUserRoleDtls.comments = reasonEndDateComments.comments;
    caseUserRoleDtls.reasonCode = reasonEndDateComments.reasonCode;

    // set the key to read the current owner role
    readByCaseStatusTypeKey.caseID = key.caseID;
    readByCaseStatusTypeKey.recordStatus = RECORDSTATUS.NORMAL;
    readByCaseStatusTypeKey.typeCode = CASEUSERROLETYPE.OWNER;

    // read the current owner role
    currentUserDetails =
      caseUserRoleObj.readActiveUserDetails(readByCaseStatusTypeKey);

    // populate key to cancel current case owner role
    cancelCaseUserRoleKey.caseUserRoleID = currentUserDetails.caseUserRoleID;

    // we need to cancel the current owner role
    curam.core.sl.fact.CaseUserRoleFactory.newInstance()
      .cancelUserRole(cancelCaseUserRoleKey);

    // create the new owner role
    curam.core.sl.fact.CaseUserRoleFactory.newInstance()
      .createOwnerCaseUserRole(caseUserRoleDtls);

    // If the sendNotification indicator is set,
    // send a notification to the previous owner
    if (sendNotificationInd.sendNotification) {

      // retrieve environment variable
      genCaseOwnerChange =
        Configuration.getProperty(EnvVars.ENV_GENCASEOWNERCHANGEDTICKET);

      // check that the variable has been populated, if not, use default
      // variable
      if (genCaseOwnerChange == null) {

        genCaseOwnerChange = EnvVars.ENV_GENCASEOWNERCHANGEDTICKET_DEFAULT;
      }

      // do we send notification to the old case owner(s)?
      if (genCaseOwnerChange.equalsIgnoreCase(EnvVars.ENV_VALUE_YES)) {

        // set details for notification sending to the existing owner
        // informing of the case owner change
        sendNotificationDetails.caseID = key.caseID;
        sendNotificationDetails.typeCode = CASEUSERROLETYPE.OWNER;
        sendNotificationDetails.comments = reasonEndDateComments.comments;
        sendNotificationDetails.userNameDtls.newOrgObjectLinkID =
          orgObjectLinkKey.orgObjectLinkID;
        sendNotificationDetails.userNameDtls.orgObjectLinkID =
          currentUserDetails.orgObjectLinkID;

        // send notification to the old case owners
        curam.core.sl.fact.CaseUserRoleFactory.newInstance()
          .sendOwnerChangeNotification(sendNotificationDetails,
            reasonEndDateComments);
      }
    }
    // modify CaseHeader to update the owner
    curam.core.sl.fact.CaseUserRoleFactory.newInstance().modifyCaseHeader(key,
      orgObjectLinkKey);

    // If the sendNotification indicator is set,
    // send a notification to the new owner
    if (sendNotificationInd.sendNotification) {

      // notify the new owner informing of the case owner change.
      // now that the case header has been updated it will be the new owner
      // that receives the notification

      // retrieve environment variable
      String genCaseOwnerCreate =
        Configuration.getProperty(EnvVars.ENV_GENCASEOWNERCREATEDTICKET);

      // check that the variable has been populated, if not, use default
      // variable
      if (genCaseOwnerCreate == null) {

        genCaseOwnerCreate = EnvVars.ENV_GENCASEOWNERCREATEDTICKET_DEFAULT;
      }

      // do we send notification to the new case owner(s)?
      if (genCaseOwnerCreate.equalsIgnoreCase(EnvVars.ENV_VALUE_YES)) {

        // set details for notification sending to the new owner(s)
        sendNotificationDetails.caseID = key.caseID;
        sendNotificationDetails.typeCode = CASEUSERROLETYPE.OWNER;

        // BEGIN, CR00178555, MR
        sendNotificationDetails.userNameDtls.newOrgObjectLinkID =
          currentUserDetails.orgObjectLinkID;
        // END, CR00178555
        sendNotificationDetails.userNameDtls.orgObjectLinkID =
          currentUserDetails.orgObjectLinkID;

        // send notification
        curam.core.sl.fact.CaseUserRoleFactory.newInstance()
          .sendNotification(sendNotificationDetails);
      }
    }

    // BEGIN, CR00076062, SPD
    // populate UserRecentAction details
    userRecentActionDetails.dtls.referenceNo = key.caseID;

    // create the UserRecentAction record
    userRecentActionObj.createCaseActionAssign(userRecentActionDetails);
    // END, CR00076062

    // BEGIN, CR00305960, HAR
    final Event event = new Event();

    event.eventKey.eventClass = CASE.CASE_OWNER_CHANGED.eventClass;
    event.eventKey.eventType = CASE.CASE_OWNER_CHANGED.eventType;
    event.primaryEventData = key.caseID;
    EventService.raiseEvent(event);
    // END, CR00305960
  }

  /**
   * Method to validate a case owner.
   *
   * @param key
   * the unique identifier of the case
   *
   * @param dtls
   * the details of the case organization object or user
   */
  @Override
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  protected void validateCaseOwner(final CaseHeaderKey key,
    final OrgObjectLinkDtls dtls)
    throws AppException, InformationalException {

    // BEGIN, CR00076203, CM
    // check if orgObjectType or username are blank
    if (dtls.orgObjectReference == 0 && dtls.userName.length() == 0) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(BPOCASEUSERROLE.ERR_CASEUSERROLE_FV_OWNER),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          1);
    }

    // Check if orgObjectReference and orgObjectType are empty
    if (dtls.orgObjectReference == 0 && dtls.orgObjectType.length() == 0) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(BPOCASEUSERROLE.ERR_CASEUSERROLE_FV_OWNER),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          2);
    }

    // Check if userName and orgObjectType are empty
    if (dtls.userName.length() == 0 && dtls.orgObjectType.length() == 0) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(BPOCASEUSERROLE.ERR_CASEUSERROLE_FV_OWNER),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);

    } else {
      // END, CR00076203
      if (dtls.orgObjectType.equals(ORGOBJECTTYPE.USER)) {

        validateUserOwner(key, dtls);

      } else if (dtls.orgObjectType.equals(ORGOBJECTTYPE.POSITION)) {

        validatePositionOwner(key, dtls);

      } else if (dtls.orgObjectType.equals(ORGOBJECTTYPE.ORGUNIT)) {

        validateOrgUnitOwner(key, dtls);

      } else if (dtls.orgObjectType.equals(ORGOBJECTTYPE.WORKQUEUE)) {

        validateWorkQueueOwner(key, dtls);
      }
    }

  }

  /**
   * Method to validate a work queue case owner.
   *
   * @param key
   * the unique identifier of the case
   *
   * @param dtls
   * the details of the case organization object
   */
  @Override
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  protected void validateWorkQueueOwner(final CaseHeaderKey key,
    final OrgObjectLinkDtls dtls)
    throws AppException, InformationalException {

    if (!PRODUCTCATEGORY.BDM_IC_FEC_PROGRAM
      .equals(CaseHeaderFactory.newInstance().read(key).integratedCaseType)) {

      // The new case owner cannot be the same as the existing case owner.

      // Read the existing case owner
      final CaseOwnerDetails caseOwnerDetails =
        curam.core.sl.fact.CaseUserRoleFactory.newInstance().readOwner(key);

      // If the new case owner is the same as the old case owner,
      // throw an error
      if (caseOwnerDetails != null
        && caseOwnerDetails.orgObjectType.equals(ORGOBJECTTYPE.WORKQUEUE)
        && caseOwnerDetails.orgObjectReference == dtls.orgObjectReference) {

        curam.core.sl.infrastructure.impl.ValidationManagerFactory
          .getManager().throwWithLookup(
            new AppException(
              BPOCASEUSERROLE.ERR_CASEUSERROLE_XFV_NEW_OLD_OWNER),
            curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
            2);
      }
    }
  }

  /**
   * Checks if this case type exists in the CaseSensitivityExceptions codetable
   *
   *
   * @param caseKey
   * containing the caseID.
   *
   * @return SecurityResult boolean indicator.
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  @Override
  protected SecurityResult
    checkSensitivityExceptions(final curam.core.struct.CaseKey caseKey)
      throws AppException, InformationalException {

    final SecurityResult isSensitivityExceptionType = new SecurityResult();

    // read the case details to determine what the case type is
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();

    final CaseTypeCode caseTypeCode = caseHeaderObj.readCaseTypeCode(caseKey);

    final StringList sensitivityExceptions =
      CodeTable.getAllCodesStringList(CASESENSITIVITYEXCEPTIONS.TABLENAME,
        TransactionInfo.getProgramLocale());

    // Ignore security checks if the case type is in the
    // CASESENSITIVITYEXCEPTIONS codetable as these have their own sensitivity
    // checking
    if (sensitivityExceptions.contains(caseTypeCode.caseTypeCode)) {
      isSensitivityExceptionType.result = true;
    } else {
      isSensitivityExceptionType.result = false;
    }
    // Case Client Security Variables

    return isSensitivityExceptionType;
  }

  /**
   * Method to validate a user case owner.
   *
   * @param key
   * the unique identifier of the case
   *
   * @param dtls
   * the details of the user
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  @Override
  protected void validateUserOwner(final CaseHeaderKey key,
    final OrgObjectLinkDtls dtls)
    throws AppException, InformationalException {

    // BEGIN, CR00089356, SPD
    // check if the new user holds a position in the organization

    // PositionHolderLinl entity object
    final curam.core.sl.entity.intf.PositionHolderLink positionHolderLinkObj =
      PositionHolderLinkFactory.newInstance();

    // Structs needed to count number of positions held by the user
    final OrgStructureUserDateAndStatusKey orgStructureUserDateAndStatusKey =
      new OrgStructureUserDateAndStatusKey();
    RecordCount recordCount;

    // populate key
    orgStructureUserDateAndStatusKey.organisationStructureID =
      curam.core.sl.fact.CaseUserRoleFactory.newInstance()
        .readOrganisationStructureID().organisationStructureID;
    orgStructureUserDateAndStatusKey.userName = dtls.userName;
    orgStructureUserDateAndStatusKey.recordStatus = RECORDSTATUS.NORMAL;
    orgStructureUserDateAndStatusKey.effectiveDate = Date.getCurrentDate();

    // count number of positions held for specified user
    recordCount = positionHolderLinkObj
      .countByOrgStructureUserDateAndStatus(orgStructureUserDateAndStatusKey);

    // BEGIN, CR00143348, ELG
    // struct to hold details to determine user closed status
    SecurityResult securityResult = new SecurityResult();

    final curam.core.struct.UsersKey usersKey =
      new curam.core.struct.UsersKey();
    final curam.core.intf.MaintainUsers maintainUsersObj =
      curam.core.fact.MaintainUsersFactory.newInstance();

    // If user is closed than not allowed to submit a case for approval
    usersKey.userName = dtls.userName;
    securityResult = maintainUsersObj.checkCloseUser(usersKey);

    // if the user is not closed they must be assigned to a position
    if (securityResult.result && recordCount.count == 0) {
      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(BPOCASEUSERROLE.ERR_CASEUSERROLE_FV_USER),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          1);
    }
    // END, CR00143348
    // END, CR00089356

    // A user that has been cancelled cannot be assigned as owner.

    // User entity object
    final curam.core.intf.Users usersObj =
      curam.core.fact.UsersFactory.newInstance();

    // Set the user name key
    final UserNameKey userNameKey = new UserNameKey();

    userNameKey.userName = dtls.userName;

    // Read the status of the user
    final StatusCodeKeyStruct statusCodeKeyStruct =
      usersObj.readStatus(userNameKey);

    // If the user has been cancelled throw an error
    if (statusCodeKeyStruct.statusCode.equals(RECORDSTATUS.CANCELLED)) {

      // Get Owner full name
      final UserAccess userAccessObj = UserAccessFactory.newInstance();

      usersKey.userName = userNameKey.userName;
      final UserFullname fullName = userAccessObj.getFullName(usersKey);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(BPOCASEUSERROLE.ERR_RV_USER_CANCELLED)
            .arg(fullName.fullname),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // BEGIN, CR00066601, NG
    // BEGIN, CR00081378, NG
    // validate close user
    if (!securityResult.result) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(
            BPOCASEUSERROLE.ERR_CASEUSERROLE_XRV_CASE_OWNER_USER_IS_CLOSED),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }
    // END, CR00081378
    // END, CR00066601

    // The new case owner cannot be the same as the existing case owner.

    // Read the existing case owner
    final CaseOwnerDetails caseOwnerDetails =
      curam.core.sl.fact.CaseUserRoleFactory.newInstance().readOwner(key);

    // If the new case owner is the same as the old case owner,
    // throw an error
    if (caseOwnerDetails != null
      && caseOwnerDetails.orgObjectType.equals(ORGOBJECTTYPE.USER)
      && caseOwnerDetails.userName.equals(dtls.userName)) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(
            BPOCASEUSERROLE.ERR_CASEUSERROLE_XFV_NEW_OLD_OWNER),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          1);
    }
  }

  /**
   * Method to validate a position case owner.
   *
   * @param key
   * the unique identifier of the case
   *
   * @param dtls
   * the details of the case organization object
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  @Override
  protected void validatePositionOwner(final CaseHeaderKey key,
    final OrgObjectLinkDtls dtls)
    throws AppException, InformationalException {

    // A position that has been cancelled cannot be assigned as owner.

    // Position entity object
    final curam.core.sl.entity.intf.Position positionObj =
      curam.core.sl.entity.fact.PositionFactory.newInstance();

    // Set the position key
    final PositionKey positionKey = new PositionKey();

    positionKey.positionID = dtls.orgObjectReference;

    // Read the status of the position
    final PositionStatus positionStatus = positionObj.readStatus(positionKey);

    // If the position has been cancelled throw an error
    if (positionStatus.recordStatus.equals(RECORDSTATUS.CANCELLED)) {

      // read the position name
      final PositionName positionName =
        positionObj.readPositionName(positionKey);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(BPOCASEUSERROLE.ERR_RV_POSITION_CANCELLED)
            .arg(positionName.name),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);

    }
  }

  /**
   * Method to validate an organization unit case owner.
   *
   * @param key
   * the unique identifier of the case
   *
   * @param dtls
   * the details of the case organization object
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  @Override
  protected void validateOrgUnitOwner(final CaseHeaderKey key,
    final OrgObjectLinkDtls dtls)
    throws AppException, InformationalException {

    // An organization unit that has been cancelled
    // cannot be assigned as owner.

    // organizationUnit entity object
    final curam.core.sl.entity.intf.OrganisationUnit organisationUnitObj =
      curam.core.sl.entity.fact.OrganisationUnitFactory.newInstance();

    // Set the organization unit key
    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();

    organisationUnitKey.organisationUnitID = dtls.orgObjectReference;

    // Read the status of the organization unit
    final OrganisationUnitStatus organisationUnitStatus =
      organisationUnitObj.readStatus(organisationUnitKey);

    // If the organization unit has been cancelled throw an error
    if (organisationUnitStatus.recordStatus.equals(RECORDSTATUS.CANCELLED)) {

      // read the organization unit name
      final OrganisationUnitName organisationUnitName =
        organisationUnitObj.readOrgUnitName(organisationUnitKey);

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(BPOCASEUSERROLE.ERR_RV_ORG_UNIT_CANCELLED)
            .arg(organisationUnitName.name),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    // The new case owner cannot be the same as the existing case owner.

    // Read the existing case owner
    final CaseOwnerDetails caseOwnerDetails =
      curam.core.sl.fact.CaseUserRoleFactory.newInstance().readOwner(key);

    // If the new case owner is the same as the old case owner,
    // throw an error
    if (caseOwnerDetails != null
      && caseOwnerDetails.orgObjectType.equals(ORGOBJECTTYPE.ORGUNIT)
      && caseOwnerDetails.orgObjectReference == dtls.orgObjectReference) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(
            BPOCASEUSERROLE.ERR_CASEUSERROLE_XFV_NEW_OLD_OWNER),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }
  }
}
