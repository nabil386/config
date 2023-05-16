<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010, 2012 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a recurring activity for a selected  -->
<!-- user.                                                                  -->
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
    CLASS="Activity"
    NAME="DISPLAY"
    OPERATION="viewReadOnlyRecurringActivityDetails"
    PHASE="DISPLAY"
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


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
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
          PROPERTY="locationName"
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


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatusCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
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


    <FIELD LABEL="Field.Label.ShowAs">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="timeStatusCode"
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
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Concerning"
  >
    <FIELD LABEL="Field.Label.Client">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
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


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Recurrence"
  >


    <FIELD LABEL="Field.Label.Frequency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="frequencyPattern"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.RecurrenceDuration"
  >


    <FIELD LABEL="Field.Label.NumOccurences">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="occurrencesStr"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.To">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recurrenceEndDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST TITLE="List.Title.Attendees">


    <FIELD
      LABEL="Field.Label.Attendee"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attendeeName"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00306738, VT -->
    <FIELD
      LABEL="Field.Label.AttendeeStatus"
      WIDTH="50"
    >
      <!-- END, CR00306738 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="invitationStatus"
        />
      </CONNECT>
    </FIELD>


  </LIST>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="notes"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
