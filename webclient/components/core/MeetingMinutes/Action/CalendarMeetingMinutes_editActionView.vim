<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Edit actions for the meeting -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="MeetingMinutesManagement"
    NAME="DISPLAY"
    OPERATION="viewMeetingActionDetails"
    PHASE="DISPLAY"
  />
  <SERVER_INTERFACE
    CLASS="MeetingMinutesManagement"
    NAME="ACTION"
    OPERATION="editMeetingAction"
    PHASE="ACTION"
  />
  <SERVER_INTERFACE
    CLASS="MeetingMinutesManagement"
    NAME="DISPLAY_ATTENDEES"
    OPERATION="listPotentialActionAssignees"
    PHASE="DISPLAY"
  />
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="taskID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="meetingActionKey$taskID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="taskID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="meetingActionDetails$taskID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="meetingID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="meetingID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="status"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="status"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="meetingID"
    />
    <TARGET
      NAME="DISPLAY_ATTENDEES"
      PROPERTY="meetingID"
    />
  </CONNECT>


  <PAGE_PARAMETER NAME="meetingID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="taskID"/>


  <CLUSTER LABEL_WIDTH="16.5">
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
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="33"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.DueDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dueDateTime"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dueDateTime"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST TITLE="List.Title.AssignedTo">
    <CONTAINER
      LABEL="Field.Label.Select"
      WIDTH="10"
    >
      <WIDGET TYPE="MULTISELECT">
        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY_ATTENDEES"
              PROPERTY="userName"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="assigneeList"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_INITIAL">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="assigneeList"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.AssigneeName"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_ATTENDEES"
          PROPERTY="fullName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Role"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_ATTENDEES"
          PROPERTY="role"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Attended"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_ATTENDEES"
          PROPERTY="attendedInd"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
