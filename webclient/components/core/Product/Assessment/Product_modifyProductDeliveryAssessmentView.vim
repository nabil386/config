<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page modifies an assessment assignment for an integrated case.    -->
<VIEW WINDOW_OPTIONS="width=500" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1"/>
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE CLASS="Product" NAME="DISPLAY" OPERATION="readPDAssessmentConfiguration"/>


  <SERVER_INTERFACE CLASS="Product" NAME="ACTION" OPERATION="modifyPDAssessmentConfiguration" PHASE="ACTION"/>


  <PAGE_PARAMETER NAME="pdAssessmentConfigurationID"/>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="pdAssessmentConfigurationID"/>
    <TARGET NAME="DISPLAY" PROPERTY="key$key$pdAssessmentConfigurationID"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="pdAssessmentConfigurationID"/>
    <TARGET NAME="ACTION" PROPERTY="pdAssessmentConfigurationID"/>
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="versionNo"/>
    <TARGET NAME="ACTION" PROPERTY="versionNo"/>
  </CONNECT>
  <!-- <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="productID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="productID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="assessmentConfigurationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="assessmentConfigurationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>
-->


  <CLUSTER LABEL_WIDTH="42" NUM_COLS="1">


    <FIELD LABEL="Field.Label.Assessment">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="assessmentName"/>
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="recordStatus"/>
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="startDate"/>
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="startDate"/>
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="endDate"/>
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="endDate"/>
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>