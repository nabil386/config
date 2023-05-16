<?xml version="1.0" encoding="ISO-8859-1"?>
<ac:application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:ac="http://www.curamsoftware.com/curam/util/client/application-config"
    id="BDMINTSAPP"
    logo="BDMINTSAPP.logo"
    
    subtitle="BDMINTSAPP.subtitle"
    user-message="BDMINTSAPP.UserMessage">
    
     <ac:application-menu>
        <ac:preferences title="preferences.title"/>
        <ac:help title="help.title"/>
        <ac:logout title="logout.title"/>
    </ac:application-menu>
     
    <!--ac:application-search default-search-page="Organization_resolveApplicationSearch" initial-text="Application.Search.InitialText"/-->
	<ac:application-search>
		<ac:smart-navigator initial-text="Application.Search.IntSearch.InitialText"	description="Application.Search.IntSearch.Description" default="true" />
	</ac:application-search>
   
    <ac:section-ref id="BDMINTSAPPHOMEWorkspaceSection"/>
	<ac:section-ref id="BDMINTSAPPTEAMWORKLOADWorkspaceSection"/>
	<!-- START Bug - 21083 and 21084 -Remove extra leaf nodes from Inbox section shortcut panel -->
	<ac:section-ref id="BDMSAPPDefaultAppInboxSection"/>
    
</ac:application>