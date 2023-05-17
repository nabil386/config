<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2005, 2010-2011 Curam Software Ltd.                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
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


  <PAGE_PARAMETER NAME="activityID"/>


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
  <CLUSTER LABEL_WIDTH="18">
    <!-- BEGIN, CR00217290, CW -->
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="meetingCreatedFromCaseInd"
      />
    </CONDITION>
    <FIELD LABEL="Field.Label.Subject">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
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
    </FIELD>
    <FIELD LABEL="Field.Label.CaseReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseReference"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00217290 -->
  </CLUSTER>
  <!-- BEGIN, CR00220191, CW -->
  <CLUSTER LABEL_WIDTH="18">
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="meetingCreatedFromCaseInd"
      />
    </CONDITION>
    <FIELD LABEL="Field.Label.Subject">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
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
    </FIELD>
  </CLUSTER>
  <!-- END, CR00220191 -->


  <CLUSTER
    LABEL_WIDTH="36"
    NUM_COLS="2"
    TITLE="Cluster.Title.Time"
  >
    <FIELD LABEL="Field.Label.Start">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.End">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.AllDay">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allDayInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Priority">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="priorityCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST TITLE="List.Title.InvitedAttendees">
    <FIELD WIDTH="6">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="meetingOrganizerInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attendeeNameEmail"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Response"
      WIDTH="34"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="invitationStatus"
        />
      </CONNECT>
    </FIELD>


  </LIST>


  <!-- BEGIN, CR00238040, ELG -->
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Agenda"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$meetingDtls$notes"
        />
      </CONNECT>


    </FIELD>
  </CLUSTER>
  <!-- END, CR00238040 -->


</VIEW>
