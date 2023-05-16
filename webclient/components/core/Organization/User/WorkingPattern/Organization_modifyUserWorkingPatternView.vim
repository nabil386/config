<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to modify a user working pattern.            -->
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
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="readUserWorkingPattern"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyUserWorkingPattern"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="workingPatternID"/>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="userName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="userName"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="defaultRecordStatusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="defaultRecordStatusCode"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="workingPatternID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="maintainWorkingPatternKey$workingPatternID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="workingPatternID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="workingPatternID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.StartDate">


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>


    </FIELD>


    <FIELD LABEL="Field.Label.StartTime">


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="activePatternStartTime"
        />
      </CONNECT>


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="activePatternStartTime"
        />
      </CONNECT>


    </FIELD>


    <FIELD LABEL="Field.Label.EndDate">


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>


    </FIELD>


    <FIELD LABEL="Field.Label.EndTime">


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="activePatternEndTime"
        />
      </CONNECT>


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="activePatternEndTime"
        />
      </CONNECT>


    </FIELD>


  </CLUSTER>


</VIEW>
