<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<ac:application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:ac="http://www.curamsoftware.com/curam/util/client/application-config"
    id="CIAPP"
    logo="CIAPP.logo"
    title="CIAPP.title"
    subtitle="CIAPP.subtitle"
    user-message="CIAPP.UserMessage">

    <ac:application-menu>
        <ac:preferences title="preferences.title"/>
        <ac:help title="help.title"/>
        <ac:logout title="logout.title"/>
    </ac:application-menu>

    <ac:application-search default-search-page="Organization_resolveApplicationSearch" initial-text="Application.Search.InitialText"/>

     <ac:section-ref id="CIAPPHomeSection"/>

     <ac:section-ref id="CIAPPWorkspaceSection"/>

     <ac:section-ref id="DefaultAppInboxSection"/>
     
      <ac:section-ref id="DefaultAppCalendarSection"/>



</ac:application>



