package curam.ca.gc.bdm.facade.organization.impl;

import curam.ca.gc.bdm.facade.organization.struct.BDMListDeferredTasksReservedByOrgUnitUserDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMListOrgUnitTasksDueByWeekDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMListOrgUnitTasksDueOnDateDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMOrgUnitAssignedTasksDetails;
import curam.ca.gc.bdm.facade.organization.struct.BDMOrgUnitAssignedTasksDetailsList;
import curam.ca.gc.bdm.facade.organization.struct.BDMOrgUnitReservedTasksByUserDueOnDateDetails;
import curam.ca.gc.bdm.sl.bdminbox.struct.BDMAssignedTaskDetails;
import curam.ca.gc.bdm.sl.supervisor.fact.BDMOrganizationUnitWorkspaceFactory;
import curam.ca.gc.bdm.sl.supervisor.intf.BDMOrganizationUnitWorkspace;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitReservedTasksByUserDueOnDateDetailsList;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDetailsList;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDueByWeekDetailsList;
import curam.ca.gc.bdm.sl.supervisor.struct.BDMOrgUnitTasksDueOnDateDetailsList;
import curam.ca.gc.bdm.util.impl.BDMTaskUtil;
import curam.core.sl.entity.struct.OrganisationUnitKey;
import curam.core.sl.fact.InboxFactory;
import curam.core.sl.struct.AssignedTaskDetails;
import curam.supervisor.facade.struct.OrgUnitAssignedTasksDetails;
import curam.supervisor.facade.struct.OrgUnitAssignedTasksKey;
import curam.supervisor.facade.struct.OrgUnitReservedTasksByUserDueOnDateKey;
import curam.supervisor.facade.struct.OrgUnitTasksDueOnDateKey;
import curam.supervisor.facade.struct.OrgUnitTasksKey;
import curam.supervisor.facade.struct.UserNameOrgUnitIDKey;
import curam.supervisor.sl.fact.SupervisorApplicationPageContextDescriptionFactory;
import curam.supervisor.sl.impl.SupervisorConst;
import curam.supervisor.sl.intf.SupervisorApplicationPageContextDescription;
import curam.supervisor.sl.struct.SupervisorApplicationPageContextDetails;
import curam.util.codetable.TASKRESERVEDSEARCHSTATUS;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

public class BDMMaintainSupervisorOrgUnits extends
  curam.ca.gc.bdm.facade.organization.base.BDMMaintainSupervisorOrgUnits {

  public BDMMaintainSupervisorOrgUnits() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  // _________________________________________________________________________
  /**
   * This method allows the supervisor to list the tasks that have been
   * reserved, and then deferred by members of the organization unit. *
   *
   * @param key -
   * UserNameOrgUnitIDKey
   * @return the BDMListDeferredTasksReservedByOrgUnitUserDetails object.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMListDeferredTasksReservedByOrgUnitUserDetails
    listDeferredTasksReservedByOrgUnitUser(final UserNameOrgUnitIDKey key)
      throws AppException, InformationalException {

    final curam.ca.gc.bdm.sl.organization.intf.BDMMaintainSupervisorOrgUnits maintainSupervisorOrgUnits =
      curam.ca.gc.bdm.sl.organization.fact.BDMMaintainSupervisorOrgUnitsFactory
        .newInstance();

    final BDMListDeferredTasksReservedByOrgUnitUserDetails listDeferredTasksReservedByOrgUnitUserDetails =
      new BDMListDeferredTasksReservedByOrgUnitUserDetails();

    listDeferredTasksReservedByOrgUnitUserDetails.details =
      maintainSupervisorOrgUnits
        .listDeferredTasksReservedByOrgUnitUser(key.key);

    return listDeferredTasksReservedByOrgUnitUserDetails;
  }

  // _________________________________________________________________________
  /**
   * This method allows the supervisor to list the tasks reserved by members
   * of the organization unit, which are open.
   *
   * @param key -
   * UserNameOrgUnitIDKey
   * @return the ListOpenTasksReservedByOrgUnitUserDetails object.
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public
    curam.ca.gc.bdm.facade.organization.struct.BDMListOpenTasksReservedByOrgUnitUserDetails
    listOpenTasksReservedByOrgUnitUser(final UserNameOrgUnitIDKey key)
      throws AppException, InformationalException {

    final curam.ca.gc.bdm.sl.organization.intf.BDMMaintainSupervisorOrgUnits maintainSupervisorOrgUnits =
      curam.ca.gc.bdm.sl.organization.fact.BDMMaintainSupervisorOrgUnitsFactory
        .newInstance();
    final curam.ca.gc.bdm.facade.organization.struct.BDMListOpenTasksReservedByOrgUnitUserDetails listOpenTasksReservedByOrgUnitUserDetails =
      new curam.ca.gc.bdm.facade.organization.struct.BDMListOpenTasksReservedByOrgUnitUserDetails();
    // taskDetailsList
    listOpenTasksReservedByOrgUnitUserDetails.details =
      maintainSupervisorOrgUnits.listOpenTasksReservedByOrgUnitUser(key.key);

    return listOpenTasksReservedByOrgUnitUserDetails;
  }

  // _________________________________________________________________________
  /**
   * This method allows the supervisor to list all the Assigned Tasks for a
   * User.
   *
   * @param key -
   * OrgUnitTasksByUserDueOnDateKey
   * @return OrgUnitTasksReservedByUserDetails
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMOrgUnitAssignedTasksDetails
    listOrgUnitAssignedTasksToUser(final OrgUnitAssignedTasksKey key)
      throws AppException, InformationalException {

    // Creating objects to call SL method
    final OrgUnitAssignedTasksDetails orgUnitAssignedTasksDetails =
      new OrgUnitAssignedTasksDetails();
    final OrgUnitAssignedTasksKey orgUnitAssignedTasksKey =
      new OrgUnitAssignedTasksKey();

    final BDMOrgUnitAssignedTasksDetails bdmorgUnitAssignedTasksDetails =
      new BDMOrgUnitAssignedTasksDetails();

    orgUnitAssignedTasksKey.key = key.key;
    final curam.core.sl.intf.Inbox inbox = InboxFactory.newInstance();

    // Calling Entity layer method
    orgUnitAssignedTasksDetails.taskDetails =
      inbox.listAssigned(orgUnitAssignedTasksKey.key);

    for (int i =
      0; i < orgUnitAssignedTasksDetails.taskDetails.taskDetailsList
        .size(); i++) {

      final AssignedTaskDetails ootb =
        orgUnitAssignedTasksDetails.taskDetails.taskDetailsList.get(i);

      final BDMAssignedTaskDetails bdm = new BDMAssignedTaskDetails();
      bdm.assign(ootb);
      bdm.caseUrgentFlagStr =
        BDMTaskUtil.getCaseUrgentFlagsByTaskID(ootb.taskID);
      bdm.escalationLevelDesc =
        BDMTaskUtil.getEscalationLevelsByTaskID(ootb.taskID);

      bdmorgUnitAssignedTasksDetails.taskDetails.taskDetailsList.addRef(bdm);

    }

    // To get page context
    final curam.supervisor.sl.intf.SupervisorApplicationPageContextDescription pageContextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();

    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();

    organisationUnitKey.organisationUnitID = key.orgUnitID;

    bdmorgUnitAssignedTasksDetails.pageContext.description =
      pageContextDescription
        .readOrgUnitNamePageContext(organisationUnitKey).description;

    return bdmorgUnitAssignedTasksDetails;
  }

  // _________________________________________________________________________
  /**
   * This method allows the supervisor to fetch the Deferred Reserved Tasks By
   * Week.
   *
   * @param key -
   * OrgUnitReservedTasksByUserDueOnDateKey
   * @return BDMOrgUnitReservedTasksByUserDueOnDateDetails
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public BDMOrgUnitReservedTasksByUserDueOnDateDetails
    listDeferredOrgUnitReservedTasksByUserDueByWeek(
      final OrgUnitReservedTasksByUserDueOnDateKey key)
      throws AppException, InformationalException {

    // OrganisationUnit objects to fetch the data

    final BDMOrganizationUnitWorkspace bdmorganisationUnit =
      BDMOrganizationUnitWorkspaceFactory.newInstance();

    final BDMOrgUnitReservedTasksByUserDueOnDateDetails orgUnitReservedTasksByUserDueOnDateDetails =
      new BDMOrgUnitReservedTasksByUserDueOnDateDetails();
    final OrgUnitReservedTasksByUserDueOnDateKey orgUnitReservedTasksByUserDueOnDateKey =
      new OrgUnitReservedTasksByUserDueOnDateKey();

    orgUnitReservedTasksByUserDueOnDateKey.key = key.key;

    // Call to Entity method
    orgUnitReservedTasksByUserDueOnDateDetails.dueOnDateDtls =
      bdmorganisationUnit.getDeferredOrgUnitReservedTasksByUserDueByWeek(
        orgUnitReservedTasksByUserDueOnDateKey.key);

    final SupervisorApplicationPageContextDescription supervisorapplicationpageContextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();
    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();

    // To retrieve Org Unit name from page context details
    organisationUnitKey.organisationUnitID = key.orgUnitID;
    orgUnitReservedTasksByUserDueOnDateDetails.pageContext =
      supervisorapplicationpageContextDescription
        .readOrgUnitNamePageContext(organisationUnitKey);

    return orgUnitReservedTasksByUserDueOnDateDetails;
  }

  // _________________________________________________________________________
  /**
   * This method allows the supervisor to fetch the Open Reserved Tasks By
   * Week.
   *
   * @param key -
   * OrgUnitReservedTasksByUserDueOnDateKey
   * @return BDMOrgUnitReservedTasksByUserDueOnDateDetails
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public BDMOrgUnitReservedTasksByUserDueOnDateDetails
    listOpenOrgUnitReservedTasksByUserDueByWeek(
      final OrgUnitReservedTasksByUserDueOnDateKey key)
      throws AppException, InformationalException {

    // OrganisationUnit objects to fetch the data
    final BDMOrganizationUnitWorkspace bdmorganisationUnit =
      BDMOrganizationUnitWorkspaceFactory.newInstance();

    final BDMOrgUnitReservedTasksByUserDueOnDateDetails orgUnitReservedTasksByUserDueOnDateDetails =
      new BDMOrgUnitReservedTasksByUserDueOnDateDetails();
    final OrgUnitReservedTasksByUserDueOnDateKey orgUnitReservedTasksByUserDueOnDateKey =
      new OrgUnitReservedTasksByUserDueOnDateKey();

    orgUnitReservedTasksByUserDueOnDateKey.key = key.key;

    // Call to Entity method
    orgUnitReservedTasksByUserDueOnDateDetails.dueOnDateDtls =
      bdmorganisationUnit.getOpenOrgUnitReservedTasksByUserDueByWeek(
        orgUnitReservedTasksByUserDueOnDateKey.key);

    final SupervisorApplicationPageContextDescription supervisorapplicationpageContextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();
    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();

    // To retrieve Org Unit name from page context details
    organisationUnitKey.organisationUnitID = key.orgUnitID;
    orgUnitReservedTasksByUserDueOnDateDetails.pageContext =
      supervisorapplicationpageContextDescription
        .readOrgUnitNamePageContext(organisationUnitKey);

    return orgUnitReservedTasksByUserDueOnDateDetails;
  }

  // _________________________________________________________________________
  /**
   * This method lists the open tasks reserved by a member of the
   * organization unit, due on a particular date.
   *
   * @param key -OrgUnitReservedTasksByUserDueOnDateKey
   * @return BDMOrgUnitReservedTasksByUserDueOnDateDetails
   * @throws AppException,InformationalException
   */
  @Override
  public BDMOrgUnitReservedTasksByUserDueOnDateDetails
    listOpenOrgUnitReservedTasksByUserDueOnDate(
      final OrgUnitReservedTasksByUserDueOnDateKey key)
      throws AppException, InformationalException {

    // Create Organization Unit objects
    final BDMOrgUnitReservedTasksByUserDueOnDateDetails orgUnitReservedTasksByUserDueOnDateDetails =
      new BDMOrgUnitReservedTasksByUserDueOnDateDetails();

    final BDMOrganizationUnitWorkspace organizationUnitWorkspace =
      BDMOrganizationUnitWorkspaceFactory.newInstance();

    BDMOrgUnitReservedTasksByUserDueOnDateDetailsList orgUnitReservedTasksByUserDueOnDateDetailsList =
      new BDMOrgUnitReservedTasksByUserDueOnDateDetailsList();
    final OrgUnitReservedTasksByUserDueOnDateKey orgUnitReservedTasksByUserDueOnDateKey =
      new OrgUnitReservedTasksByUserDueOnDateKey();

    // Assign key values
    orgUnitReservedTasksByUserDueOnDateKey.key = key.key;
    orgUnitReservedTasksByUserDueOnDateKey.orgUnitID = key.orgUnitID;

    // calling the entity layer method.
    orgUnitReservedTasksByUserDueOnDateDetailsList =
      organizationUnitWorkspace.getOpenOrgUnitReservedTasksByUserDueOnDate(
        orgUnitReservedTasksByUserDueOnDateKey.key);
    orgUnitReservedTasksByUserDueOnDateDetails.dueOnDateDtls =
      orgUnitReservedTasksByUserDueOnDateDetailsList;

    // To get page context
    final SupervisorApplicationPageContextDescription supervisorapplicationpageContextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();
    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();

    organisationUnitKey.organisationUnitID =
      orgUnitReservedTasksByUserDueOnDateKey.orgUnitID;
    orgUnitReservedTasksByUserDueOnDateDetails.pageContext.description =
      supervisorapplicationpageContextDescription
        .readOrgUnitNamePageContext(organisationUnitKey).description;

    return orgUnitReservedTasksByUserDueOnDateDetails;
  }

  // ___________________________________________________________________________
  /**
   * This listOrgUnitTasksDueOnDateAssignedTo lists the tasks due on date
   * Assigned To a user.
   *
   * @param key -
   * OrgUnitTasksDueOnDateKey,
   *
   * @return ListOrgUnitTasksDueOnDateDetails
   * @throws InformationalException
   * @throws AppException
   */
  @Override
  public BDMListOrgUnitTasksDueOnDateDetails
    listOrgUnitTasksDueOnDateAssignedTo(final OrgUnitTasksDueOnDateKey key)
      throws AppException, InformationalException {

    // OrganisationUnit objects to fetch the data
    final BDMOrganizationUnitWorkspace organisationUnit =
      BDMOrganizationUnitWorkspaceFactory.newInstance();
    BDMOrgUnitTasksDueOnDateDetailsList orgUnitTasksDueOnDateDetailsList =
      new BDMOrgUnitTasksDueOnDateDetailsList();

    final BDMListOrgUnitTasksDueOnDateDetails listOrgUnitTasksDueOnDateDetails =
      new BDMListOrgUnitTasksDueOnDateDetails();

    // Call to the Entity layer to fetch the records.
    final curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey orgUnitTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey();

    // Assign key values
    orgUnitTasksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    orgUnitTasksDueOnDateKey.userName = key.key.userName;
    if (key.key.taskReservationStatus
      .equals(SupervisorConst.kCaptionAssigned)) {
      orgUnitTasksDueOnDateKey.taskReservationStatus =
        TASKRESERVEDSEARCHSTATUS.UNRESERVED;
    } else {
      orgUnitTasksDueOnDateKey.taskReservationStatus =
        key.key.taskReservationStatus;
    }

    orgUnitTasksDueOnDateDetailsList = organisationUnit
      .getOrgUnitTasksByUserDueOnDate(orgUnitTasksDueOnDateKey);
    listOrgUnitTasksDueOnDateDetails.taskDetailsList =
      orgUnitTasksDueOnDateDetailsList;

    // To get page context
    final curam.supervisor.sl.intf.SupervisorApplicationPageContextDescription pageContextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();
    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();

    organisationUnitKey.organisationUnitID = key.orgUnitID;
    listOrgUnitTasksDueOnDateDetails.pageContext.description =
      pageContextDescription
        .readOrgUnitNamePageContext(organisationUnitKey).description;

    return listOrgUnitTasksDueOnDateDetails;
  }

  // _________________________________________________________________________
  /**
   * This method allows the supervisor to view the list of organization unit
   * tasks for a specified week.
   *
   * @param key -
   * OrgUnitTasksDueOnDateKey
   * @return BDMListOrgUnitTasksDueByWeekDetails
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMListOrgUnitTasksDueByWeekDetails
    listOrgUnitTasksByWeek(final OrgUnitTasksDueOnDateKey key)
      throws AppException, InformationalException {

    final BDMOrganizationUnitWorkspace organizationUnitWorkspace =
      BDMOrganizationUnitWorkspaceFactory.newInstance();

    final BDMListOrgUnitTasksDueByWeekDetails details =
      new BDMListOrgUnitTasksDueByWeekDetails();

    final curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey orgUnitTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey();

    // Assign key values
    orgUnitTasksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    orgUnitTasksDueOnDateKey.userName = key.key.userName;

    if (key.key.taskReservationStatus
      .equals(SupervisorConst.kCaptionAssigned)) {
      orgUnitTasksDueOnDateKey.taskReservationStatus =
        TASKRESERVEDSEARCHSTATUS.UNRESERVED;
    }

    final BDMOrgUnitTasksDueByWeekDetailsList detailsList =
      organizationUnitWorkspace
        .getOrgUnitTasksByUserDueByWeek(orgUnitTasksDueOnDateKey);

    details.tasksByWeekList = detailsList;

    return details;

  }

  // _________________________________________________________________________
  /**
   * This method allows the supervisor to list the organization unit task
   * records due on the specified date.
   *
   * @param key -
   * OrgUnitTasksDueOnDateKey
   * @return BDMListOrgUnitTasksDueOnDateDetails
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMListOrgUnitTasksDueOnDateDetails
    listOrgUnitTasksDueOnDate(final OrgUnitTasksDueOnDateKey key)
      throws AppException, InformationalException {

    // Create BDMListOrgUnitTasksDueOnDateDetails object
    final BDMListOrgUnitTasksDueOnDateDetails listOrgUnitTasksDueOnDateDetails =
      new BDMListOrgUnitTasksDueOnDateDetails();

    // Create OrganizationUnitWorkspace object to get the list of org unit
    // tasks
    final BDMOrganizationUnitWorkspace orgUnitWorkspace =
      BDMOrganizationUnitWorkspaceFactory.newInstance();

    final curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey orgUnitTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey();

    // Assign key values
    orgUnitTasksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    orgUnitTasksDueOnDateKey.userName = key.key.userName;

    if (key.key.taskReservationStatus
      .equals(SupervisorConst.kCaptionAssigned)) {
      orgUnitTasksDueOnDateKey.taskReservationStatus =
        TASKRESERVEDSEARCHSTATUS.UNRESERVED;
    } else {
      orgUnitTasksDueOnDateKey.taskReservationStatus =
        key.key.taskReservationStatus;
    }

    listOrgUnitTasksDueOnDateDetails.taskDetailsList = orgUnitWorkspace
      .getOrgUnitTasksByUserDueOnDate(orgUnitTasksDueOnDateKey);

    return listOrgUnitTasksDueOnDateDetails;
  }

  // _________________________________________________________________________
  /**
   * This listOrgUnitTasksDueOnDateReservedBy lists the tasks due on date
   * Reserved By a user.
   *
   * @param key -
   * OrgUnitTasksDueOnDateKey,
   *
   * @return BDMListOrgUnitTasksDueOnDateDetails
   * @throws InformationalException
   * @throws AppException
   */
  @Override
  public BDMListOrgUnitTasksDueOnDateDetails
    listOrgUnitTasksDueOnDateReservedBy(final OrgUnitTasksDueOnDateKey key)
      throws AppException, InformationalException {

    // OrganisationUnit objects to fetch the data
    final BDMOrganizationUnitWorkspace organisationUnit =
      BDMOrganizationUnitWorkspaceFactory.newInstance();
    BDMOrgUnitTasksDueOnDateDetailsList orgUnitTasksDueOnDateDetailsList =
      new BDMOrgUnitTasksDueOnDateDetailsList();
    final BDMListOrgUnitTasksDueOnDateDetails listOrgUnitTasksDueOnDateDetails =
      new BDMListOrgUnitTasksDueOnDateDetails();

    // Call to the Entity layer to fetch the records.
    final curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey orgUnitTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey();

    // Assign key values
    orgUnitTasksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    orgUnitTasksDueOnDateKey.userName = key.key.userName;

    if (key.key.taskReservationStatus
      .equals(SupervisorConst.kCaptionAssigned)) {
      orgUnitTasksDueOnDateKey.taskReservationStatus =
        TASKRESERVEDSEARCHSTATUS.UNRESERVED;
    } else {
      orgUnitTasksDueOnDateKey.taskReservationStatus =
        key.key.taskReservationStatus;
    }

    orgUnitTasksDueOnDateDetailsList = organisationUnit
      .getOrgUnitTasksByUserDueOnDate(orgUnitTasksDueOnDateKey);
    listOrgUnitTasksDueOnDateDetails.taskDetailsList =
      orgUnitTasksDueOnDateDetailsList;

    // To get page context
    final curam.supervisor.sl.intf.SupervisorApplicationPageContextDescription pageContextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();
    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();

    organisationUnitKey.organisationUnitID = key.orgUnitID;
    listOrgUnitTasksDueOnDateDetails.pageContext.description =
      pageContextDescription
        .readOrgUnitNamePageContext(organisationUnitKey).description;

    return listOrgUnitTasksDueOnDateDetails;
  }

  // _________________________________________________________________________
  /**
   * The method listOrgUnitTasksDueByWeek helps the supervisor manages the
   * task due in a specified week by displaying the assigned and reserved
   * task details separately.
   *
   * @param key -
   * OrgUnitTasksDueOnDateKey
   * @return ListOrgUnitTasksDueByWeekDetails
   * @throws AppException
   * @throws InformationalException
   */

  @Override
  public BDMListOrgUnitTasksDueByWeekDetails
    listOrgUnitTasksDueByWeek(final OrgUnitTasksDueOnDateKey key)
      throws AppException, InformationalException {

    final BDMListOrgUnitTasksDueByWeekDetails listOrgUnitTasksDueByWeekDetails =
      new BDMListOrgUnitTasksDueByWeekDetails();

    final BDMOrganizationUnitWorkspace organisationUnit =
      BDMOrganizationUnitWorkspaceFactory.newInstance();

    final curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey orgUnitTasksDueOnDateKey =
      new curam.core.sl.supervisor.struct.OrgUnitTasksDueOnDateKey();

    // Assign key values
    orgUnitTasksDueOnDateKey.deadlineDate = key.key.deadlineDate;
    orgUnitTasksDueOnDateKey.userName = key.key.userName;

    if (key.key.taskReservationStatus
      .equals(SupervisorConst.kCaptionAssigned)) {
      orgUnitTasksDueOnDateKey.taskReservationStatus =
        TASKRESERVEDSEARCHSTATUS.UNRESERVED;
    } else {
      orgUnitTasksDueOnDateKey.taskReservationStatus =
        key.key.taskReservationStatus;
    }

    // calling the entity layer method
    listOrgUnitTasksDueByWeekDetails.tasksByWeekList = organisationUnit
      .getOrgUnitTasksByUserDueByWeek(orgUnitTasksDueOnDateKey);

    // To get page context
    final SupervisorApplicationPageContextDescription supervisorapplicationpageContextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();
    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();

    organisationUnitKey.organisationUnitID = key.orgUnitID;
    listOrgUnitTasksDueByWeekDetails.pageContext.description =
      supervisorapplicationpageContextDescription
        .readOrgUnitNamePageContext(organisationUnitKey).description;

    return listOrgUnitTasksDueByWeekDetails;
  }

  // _________________________________________________________________________
  /**
   * This method allows the supervisor to list the tasks assigned to members
   * of the organization unit, that have not yet been reserved.
   *
   * @param key -
   * OrgUnitTasksKey
   * @return OrgUnitAssignedTasksDetailsList
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMOrgUnitAssignedTasksDetailsList listOrgUnitAssignedTasks(
    final OrgUnitTasksKey key) throws AppException, InformationalException {

    // Create OrgUnitAssignedTasksDetailsList object
    final BDMOrgUnitAssignedTasksDetailsList orgUnitAssignedTasksDetailsList =
      new BDMOrgUnitAssignedTasksDetailsList();

    // Create OrganizationUnitWorkspace object
    final BDMOrganizationUnitWorkspace organizationUnitWorkspace =
      BDMOrganizationUnitWorkspaceFactory.newInstance();

    // Create OrgUnitTasksDetailsList to populate the details list
    BDMOrgUnitTasksDetailsList orgUnitTasksDetailsList =
      new BDMOrgUnitTasksDetailsList();

    final curam.core.sl.supervisor.struct.OrgUnitTasksKey orgUnitTasksKey =
      new curam.core.sl.supervisor.struct.OrgUnitTasksKey();

    final SupervisorApplicationPageContextDetails pageContextDetails =
      new SupervisorApplicationPageContextDetails();

    final curam.supervisor.sl.intf.SupervisorApplicationPageContextDescription pageContextDescription =
      SupervisorApplicationPageContextDescriptionFactory.newInstance();

    // Assign key values
    orgUnitTasksKey.organizationStructureID = key.key.organizationStructureID;
    orgUnitTasksKey.organizationUnitID = key.key.organizationUnitID;
    orgUnitTasksKey.taskReservationStatus =
      TASKRESERVEDSEARCHSTATUS.UNRESERVED;

    orgUnitTasksDetailsList =
      organizationUnitWorkspace.getOrgUnitTasks(orgUnitTasksKey);

    orgUnitAssignedTasksDetailsList.detailsList = orgUnitTasksDetailsList;

    // To pass organisationUnitID
    final OrganisationUnitKey organisationUnitKey = new OrganisationUnitKey();

    // assign key values
    organisationUnitKey.organisationUnitID = key.key.organizationUnitID;

    pageContextDetails.description = pageContextDescription
      .readOrgUnitNamePageContext(organisationUnitKey).description;

    orgUnitAssignedTasksDetailsList.pageContext = pageContextDetails;

    return orgUnitAssignedTasksDetailsList;
  }

}
