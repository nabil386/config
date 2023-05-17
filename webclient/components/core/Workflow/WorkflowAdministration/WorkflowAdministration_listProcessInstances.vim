<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005, 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display a list of appeals for a product delivery  -->
<!-- on an integrated case.                                                 -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <LIST TITLE="List.Title.SearchResults">


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="Field.Label.Suspend" TYPE="ACTION">
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_suspendProcessInstance" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$processInstanceID"/>
            <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$processName"/>
            <TARGET NAME="PAGE" PROPERTY="processName"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.Label.Resume" TYPE="ACTION">
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_resumeProcessInstance" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$processInstanceID"/>
            <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$processName"/>
            <TARGET NAME="PAGE" PROPERTY="processName"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.Label.Abort" TYPE="ACTION">
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_abortProcessInstance" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$processInstanceID"/>
            <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$processName"/>
            <TARGET NAME="PAGE" PROPERTY="processName"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD LABEL="Field.Label.ProcessInstanceID" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$processInstanceID"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ProcessDisplayName" WIDTH="35">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$processDisplayName"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$status"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.StartDateTime" WIDTH="25">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$startDateTime"/>
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="WorkflowAdministration_viewProcessInstance">
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$processInstanceID"/>
          <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$processInstanceSearchDetails$processName"/>
          <TARGET NAME="PAGE" PROPERTY="processName"/>
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>