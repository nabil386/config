<?xml version="1.0" encoding="UTF-8"?>

<!--
  Version  1.0 - Task 9082 by  Mohan 10/02/2022
-->
<ac:application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns:ac="http://www.curamsoftware.com/curam/util/client/application-config"
 id="BDMINTGAPP"
 logo="BDMIntegrityApp.logo"
 
 subtitle="BDMIntegrityApp.subtitle"
 user-message="BDMIntegrityApp.UserMessage">

  <ac:application-menu>
    <ac:preferences title="preferences.title"/>
    <ac:help title="help.title"/>
    <ac:logout title="logout.title"/>
  </ac:application-menu>

  <ac:application-search>
     <ac:smart-navigator initial-text="Application.Search.IntSearch.InitialText"
         description="Application.Search.IntSearch.Description" default="true" />
  </ac:application-search>
  
  <ac:section-ref id="BDMINTGAPPHOMEWorkspaceSection"/>
	<ac:section-ref id="BDMINTGAPPCLIENTINTEGRITYWorkspaceSection"/>
	<ac:section-ref id="BDMPROSAPPDefaultAppInboxSection"/>
  
  
</ac:application>