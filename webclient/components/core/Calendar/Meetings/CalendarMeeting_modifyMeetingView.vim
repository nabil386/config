<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2007, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to modify an activity                        -->
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
    CLASS="MeetingManagement"
    NAME="DISPLAY"
    OPERATION="view"
  />
  <SERVER_INTERFACE
    CLASS="MeetingManagement"
    NAME="ACTION"
    OPERATION="update"
    PHASE="ACTION"
  />
  <SCRIPT
    ACTION="hideTimeFieldsOnLoad()"
    EVENT="ONLOAD"
    SCRIPT_FILE="CalendarMeetings.js"
  />
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>
  <PAGE_PARAMETER NAME="activityID"/>
  <PAGE_PARAMETER NAME="description"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="activityID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$activityID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="activityID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$activityID"
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
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CLUSTER
    LABEL_WIDTH="22"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Subject">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="subject"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="subject"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Location">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="locationName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="22"
    NUM_COLS="2"
  >
    <FIELD
      LABEL="Field.Label.Start"
      WIDTH="50"
    >
      <SCRIPT
        ACTION="updateMeetingTime()"
        EVENT="ONCHANGE"
        SCRIPT_FILE="CalendarMeetings.js"
      />
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDateTime"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AllDay">
      <SCRIPT
        ACTION="hideTimeFields()"
        EVENT="ONCLICK"
        SCRIPT_FILE="CalendarMeetings.js"
      />
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allDayInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="allDayInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.End"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDateTime"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Priority"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="priorityCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priorityCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Agenda"
  >
    <FIELD
      HEIGHT="5"
      LABEL="Cluster.Title.Agenda"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="notes"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notes"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
