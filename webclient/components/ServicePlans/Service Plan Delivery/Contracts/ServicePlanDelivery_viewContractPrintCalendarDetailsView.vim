<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page displays contract print details to the user                  -->
<PAGE
  PAGE_ID="ServicePlanDelivery_viewContractPrintCalendarDetails"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >


    <ACTION_CONTROL
      IMAGE="ViewContract"
      LABEL="ActionControl.Label.ViewContract"
    >


      <LINK
        PAGE_ID="ServicePlanDelivery_previewContract"
        WINDOW_OPTIONS="width=400,height=150"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="servicePlanContractID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="servicePlanContractID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CloseButton"
      LABEL="ActionControl.Label.Close"
    >


      <LINK PAGE_ID="ServicePlanDelivery_eventCalendar">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="startDate"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="startDate"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="calendarViewType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="calendarViewType"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="startDate"/>
  <PAGE_PARAMETER NAME="calendarViewType"/>


  <INCLUDE FILE_NAME="ServicePlanDelivery_viewContractPrintDetailsView.vim"/>


</PAGE>
