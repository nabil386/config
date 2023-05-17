<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003, 2009-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software, Ltd. ("Confidential Information"). You shall not       -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to reschedule location hearing.              -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="HearingSchedule"
    NAME="ACTION"
    OPERATION="rescheduleLocationHearing"
    PHASE="ACTION"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <PAGE_PARAMETER NAME="userName"/>
  <PAGE_PARAMETER NAME="fullName"/>
  <PAGE_PARAMETER NAME="locationName"/>
  <PAGE_PARAMETER NAME="locationID"/>
  <PAGE_PARAMETER NAME="slotName"/>
  <PAGE_PARAMETER NAME="slotID"/>
  <PAGE_PARAMETER NAME="date"/>
  <PAGE_PARAMETER NAME="startTime"/>
  <PAGE_PARAMETER NAME="positionID"/>
  <PAGE_PARAMETER NAME="numberOfWorkUnits"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$rescheduleLocationHearingDetails$rescheduleHearingDetailsCommon$hearingID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="locationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="locationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="userName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="userName"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="startTime"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="scheduledDateTime"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="slotID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="slotID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="positionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="positionID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="numberOfWorkUnits"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="numberOfWorkUnits"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    SHOW_LABELS="true"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.HearingOfficial">
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="fullName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Location">
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="locationName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Date">
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="date"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Slot">
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="slotName"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="25"
    TITLE="Cluster.Title.RescheduleDetails"
  >
    <FIELD LABEL="Field.Title.ReasonCode">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="postponeReasonCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.ReasonText">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="postponeReasonText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00407812, RB -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00407812 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
