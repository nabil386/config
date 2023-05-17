<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page retrieves calendar events from an investigation case.        -->
<?curam-deprecated Since Curam 6.0, replaced by ICInvestigationDelivery_eventCalendarView1.vim?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$contextDtls$description"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="searchInvestigationEventAndActivity"
  />


  <MENU MODE="INTEGRATED_CASE">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="menuData"
      />
    </CONNECT>
  </MENU>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="startDate"/>
  <PAGE_PARAMETER NAME="calendarViewType"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="startDate"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="startDate"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="calendarViewType"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="calendarViewType"
    />
  </CONNECT>


  <CLUSTER SHOW_LABELS="false">
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="calendarXMLString"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
