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
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- List Actions for the meeting -->
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


  <PAGE_PARAMETER NAME="meetingID"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <SERVER_INTERFACE
    CLASS="MeetingMinutesManagement"
    NAME="DISPLAY"
    OPERATION="listActionsForMeeting"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="meetingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="meetingID"
    />
  </CONNECT>


  <LIST>
    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$dtls$displayActionLinks"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="CalendarMeetingMinutes_editActionFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$taskID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="taskID"
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
      </ACTION_CONTROL>


      <ACTION_CONTROL
        LABEL="ActionControl.Label.AddComment"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$dtls$displayAddCommentLink"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingMinutes_addActionComment"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$taskID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="taskID"
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
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.CloseAction">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$dtls$displayTaskClosureLink"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingMinutes_closeAction"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$taskID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="taskID"
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
      </ACTION_CONTROL>


    </ACTION_SET>


    <FIELD
      ALIGNMENT="LEFT"
      LABEL="Field.Label.Subject"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$subject"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="LEFT"
      LABEL="Field.Label.AssignedTo"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$assignedTo"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="LEFT"
      LABEL="Field.Label.DueDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$dueDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="LEFT"
      LABEL="Field.Label.Status"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$status"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00221236, CW -->
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="CalendarMeetingMinutes_viewAction">
        <!-- END, CR00221236 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$taskID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskID"
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
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
