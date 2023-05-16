<?xml version="1.0" encoding="UTF-8"?>

<ac:application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns:ac="http://www.curamsoftware.com/curam/util/client/application-config"
 id="BDMSAPP"
 logo="SupervisorApp.logo"
 
 subtitle="SupervisorApp.subtitle"
 user-message="SupervisorApp.UserMessage">

  <ac:application-menu>
    <ac:preferences title="preferences.title"/>
    <ac:help title="help.title"/>
    <ac:logout title="logout.title"/>
  </ac:application-menu>

  <ac:application-search>
     <ac:smart-navigator initial-text="Application.Search.IntSearch.InitialText"
         description="Application.Search.IntSearch.Description" default="true" />
  </ac:application-search>
  
  <ac:section-ref id="BDMSAPPHomeSection"/>
  <ac:section-ref id="BDMSAPPSection"/>
  <ac:section-ref id="BDMSAPPDefaultAppInboxSection"/>
  
  
</ac:application>