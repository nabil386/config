
-- script  to setup security access information
-- this script must be run AFTER the following tables have been loaded
-- - FUNCTIONIDENTIFIER
-- - SECURITYFIDSID
-- - SECURITYIDENTIFIER

insert into securityfidsid(sidname,fidname) select fidname,fidname from functionidentifier;
insert into securityidentifier(sidname,sidtype, versionNo) select sidname, 'FUNCTION', 0 from securityfidsid;
insert into securitygroupsid(groupname, sidname) select 'SUPERGROUP', sidname from securityidentifier;
update securitygroupsid set GROUPNAME='SYSTEMGROUP' where SIDNAME like '%DPQueueHandler%' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'WorkflowQueueHandler.handleActivityMessageRMI' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'WorkflowQueueHandler.handleWorkflowEnactmentMessage' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'WorkflowQueueHandler.handleWorkflowEnactmentMessageRMI' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'WorkflowQueueHandler.handleWorkflowErrorRMI' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='SYSTEMGROUP' where SIDNAME like '%WorkflowQueueHandler%' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='SYSTEMGROUP' where SIDNAME like '%ClusterSafeReloadCache%' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'WorkflowActivityQueueHandler.handleActivityMessage' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'WorkflowActivityQueueHandler.handleActivityMessageRMI' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='SYSTEMGROUP' where SIDNAME like '%WorkflowActivityQueueHandler%' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'FieldLevelSecurityRuntime.isSIDAuthorised' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'FieldLevelSecurityRuntime.getAllSecuredFields' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'LoginProcess.login' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'CodeTable.getOneCodeTable' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'CodeTable.getOneItem' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'CodeTable.getOneItemForUserLocale' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'CodeTable.getOneTimestamp' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'CodeTable.getChildCodesForItem' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'CodeTable.getCodeTableWithHierarchyDtls' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'CodeTable.getSelectedItemPath' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'UserPreferenceEditor.getAllUserPreferencesForCurrentUser' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'UserPreferenceEditor.getAllUserPreferencesForSetId' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'UserPreferenceEditor.getUserPreferenceForCurrentUser' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'UserPreferenceEditor.updateUserPreferencesForCurrentUser' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'UserPreferenceLoader.insertUserPreferencesToDatabase' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'UserPreferenceEditor.getPreferencesAndUserTypeForCurrentUser' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'ServerTimeZoneInformation.getServerTimeZone' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'ServerTimeZoneInformation.getUserTimeZone' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'ResourcesStore.getResource' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'ClientResourcesStore.getResource' and GROUPNAME='SUPERGROUP';


update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'NavigationTree.getTree' and GROUPNAME='SUPERGROUP';

update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'PropAdmin.getProperties' and GROUPNAME='SUPERGROUP';

update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'ResourcesStore.setResource' and GROUPNAME='SUPERGROUP';

update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'StateController.getState' and GROUPNAME='SUPERGROUP';

update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'TabResourceAccess.getConfig' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'TabResourceAccess.getPageAssociations' and GROUPNAME='SUPERGROUP';

update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'UserLocaleApi.getLocale' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'UserLocaleApi.setLocale' and GROUPNAME='SUPERGROUP';

update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'UserPreferenceEditor.isCurrentUserExternal' and GROUPNAME='SUPERGROUP';

update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'TabSessionManager.setTabSessionData' and GROUPNAME='SUPERGROUP';

update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'TabSessionManager.getTabSessionData' and GROUPNAME='SUPERGROUP';

update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'AutoRecoveryManager.readAutoRecovery' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'AutoRecoveryManager.saveAutoRecovery' and GROUPNAME='SUPERGROUP';
update securitygroupsid set GROUPNAME='BASESECURITYGROUP' where SIDNAME = 'AutoRecoveryManager.removeAutoRecovery' and GROUPNAME='SUPERGROUP';