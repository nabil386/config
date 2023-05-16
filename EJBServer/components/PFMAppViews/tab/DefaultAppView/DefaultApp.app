<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  Copyright IBM Corporation 2012-2018. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
  Copyright 2010 Curam Software Ltd.
  All rights reserved.

  This software is the confidential and proprietary information of Curam
  Software, Ltd. ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only in accordance with the
  terms of the license agreement you entered into with Curam Software.
-->
<ac:application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns:ac="http://www.curamsoftware.com/curam/util/client/application-config"
 id="DefaultApp"
 logo="DefaultApp.logo"
 title="DefaultApp.title"
 subtitle="DefaultApp.subtitle"
 user-message="DefaultApp.UserMessage">

  <ac:application-menu>
    <ac:preferences title="preferences.title"/>
    <ac:help title="help.title"/>
    <ac:logout title="logout.title"/>
  </ac:application-menu>

  <ac:application-search>
     <ac:smart-navigator initial-text="Application.Search.IntSearch.InitialText"
         description="Application.Search.IntSearch.Description" default="true" />
  </ac:application-search>
  
  <ac:section-ref id="DefaultAppHomeSection"/>
  <ac:section-ref id="DefaultAppSection"/>
  <ac:section-ref id="DefaultAppInboxSection"/>
  <ac:section-ref id="DefaultAppCalendarSection"/>

</ac:application>
