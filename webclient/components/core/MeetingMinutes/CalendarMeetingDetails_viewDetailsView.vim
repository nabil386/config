<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010-2011 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to view the meeting details-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="MeetingMinutesManagement"
    NAME="DISPLAY"
    OPERATION="readMeetingDetailsForCase"
    PHASE="DISPLAY"
  />
  <SERVER_INTERFACE
    CLASS="MeetingMinutesManagement"
    NAME="DISPLAY_DECISIONS"
    OPERATION="readMeetingDecisions"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="MeetingMinutesManagement"
    NAME="DISPLAY_MODE"
    OPERATION="readMeetingMinutesPageIndicators"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="meetingID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="meetingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="meetingKey$meetingID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="meetingID"
    />
    <TARGET
      NAME="DISPLAY_DECISIONS"
      PROPERTY="key$meetingID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="meetingID"
    />
    <TARGET
      NAME="DISPLAY_MODE"
      PROPERTY="meetingKey$meetingID"
    />
  </CONNECT>


  <!-- Meeting fields -->
  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    STYLE="outer-cluster-borderless"
  >
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
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    STYLE="outer-cluster-borderless"
  >
    <FIELD LABEL="Field.Label.StartTime">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Duration">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="duration"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Organizer">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="organizerFullName"
        />
      </CONNECT>
      <!--
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="CalendarMeetingMinutes_resolveViewParticipantOrUserForMinutes"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="organizer"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="meetingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="meetingID"
          />
        </CONNECT>
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
      </LINK>
      -->
    </FIELD>


    <FIELD LABEL="Field.Label.MeetingType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="meetingType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndTime">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Sensitivity">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sensitivityCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MinutedBy">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="minutesTakerFullName"
        />
      </CONNECT>
      <!--
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="CalendarMeetingMinutes_resolveViewParticipantOrUserForMinutes"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="minuteTaker"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="meetingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="meetingID"
          />
        </CONNECT>
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
      </LINK>
      -->
    </FIELD>


    <FIELD LABEL="Field.Label.LastIssued">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="lastIssued"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CalendarMeetingMinutes_listIssueHistory"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="meetingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="meetingID"
          />
        </CONNECT>
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
      </LINK>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Agenda"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$meetingDtls$meetingAgenda"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Decisions"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_DECISIONS"
          PROPERTY="result$decisions"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
