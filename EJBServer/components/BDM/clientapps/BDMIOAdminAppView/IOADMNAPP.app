<?xml version="1.0" encoding="UTF-8"?>

<!-- Version  1.0 - Task 80848 by  Katie 10/11/2022 -->

<ac:application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns:ac="http://www.curamsoftware.com/curam/util/client/application-config"
 id="IOADMNAPP"
 logo="IOAdminApp.logo"
 
 subtitle="IOAdminApp.subtitle"
 user-message="IOAdminApp.UserMessage">

 <ac:application-menu>
    <ac:preferences title="preferences.title"/>
    <ac:help title="help.title"/>
    <ac:logout title="logout.title"/>
  </ac:application-menu>


  <!-- <ac:application-search>
		<ac:smart-navigator initial-text="Application.Search.IntSearch.InitialText"	
		description="Application.Search.IntSearch.Description" default="true" />
	</ac:application-search> -->
	
  <ac:application-search default-search-page="Organization_resolveApplicationSearch" initial-text="Application.Search.All.InitialText"/>
	
  

  <ac:section-ref id="IOADMNAPPHOMEWorkspaceSection"/>
  <ac:section-ref id="IOADMNAPPIOADMINWorkspaceSection"/>
  <ac:section-ref id="IOADMNAPPDefaultAppInboxSection"/>
  <ac:section-ref id="IOADMNAPPDefaultAppCalendarSection"/>
  
  
</ac:application>