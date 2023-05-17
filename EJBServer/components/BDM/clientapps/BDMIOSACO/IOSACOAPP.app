<?xml version="1.0" encoding="ISO-8859-1"?>

<ac:application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:ac="http://www.curamsoftware.com/curam/util/client/application-config"
    id="IOSACOAPP"
    logo="IOSACOAPP.logo"
   
    subtitle="IOSACOAPP.subtitle"
    user-message="IOSACOAPP.UserMessage">
    
     <ac:application-menu>
        <ac:preferences title="preferences.title"/>
        <ac:help title="help.title"/>
        <ac:logout title="logout.title"/>
    </ac:application-menu>
     
	
  <!-- <ac:application-search>
		<ac:smart-navigator initial-text="Application.Search.IntSearch.InitialText"	
		description="Application.Search.IntSearch.Description" default="true" />
	</ac:application-search> -->
	
  <ac:application-search default-search-page="Organization_resolveApplicationSearch" initial-text="Application.Search.InitialText"/>
	
   
    <ac:section-ref id="IOSACOAPPHOMEWorkspaceSection"/>
	<ac:section-ref id="IOSACOAPPSWorkspaceSection"/>
	<ac:section-ref id="IOSACOAPPDefaultAppInboxSection"/>
	<ac:section-ref id="IOSACOAPPDefaultAppCalendarSection"/>
    
</ac:application>