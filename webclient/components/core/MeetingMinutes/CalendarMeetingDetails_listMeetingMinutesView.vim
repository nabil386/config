<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010-2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page displays the list of meeting details for a case. -->
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
    OPERATION="listMeetingsForCase"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>


  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL LABEL="ActionControl.Label.Record">
      <!-- Do not remove windows options from this link, as this may distort -->
      <!-- meeting minutes agenda wizard screens. -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CalendarMeetingDetails_create"
        WINDOW_OPTIONS="width=900,height=600"
      >
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="ListMeetingMinutesForCasePage"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="meetingOriginPage"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <LIST>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Edit"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$dtls$displayActionLink"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingDetails_updateRichTextDetails"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="meetingDetailsID"
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
        LABEL="ActionControl.Label.EditNotes"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="dtls$displayActionLink"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingDetails_updateRichTextNotes"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="meetingDetailsID"
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
        LABEL="ActionControl.Label.EditAgenda"
        TYPE="ACTION"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingDetails_editAgenda"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="meetingDetailsID"
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
        LABEL="ActionControl.Label.EditDecisions"
        TYPE="ACTION"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingDetails_editDecisions"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="meetingDetailsID"
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


      <SEPARATOR/>


      <ACTION_CONTROL
        LABEL="ActionControl.Label.AddInvitation"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="dtls$displayActionLink"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingDetails_addAttendee"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="meetingDetailsID"
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
        LABEL="ActionControl.Label.NewAction"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="dtls$displayActionLink"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingMinutes_createAction"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="meetingDetailsID"
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
        LABEL="ActionControl.Label.AddAttachment"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="dtls$displayActionLink"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingMinutes_addAttachmentsFromView"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="meetingDetailsID"
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


      <SEPARATOR/>


      <ACTION_CONTROL
        LABEL="ActionControl.Label.GeneratePDF"
        TYPE="FILE_DOWNLOAD"
      >
        <LINK>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="meetingDetailsID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="meetingID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL
        LABEL="ActionControl.Label.IssueMinutes"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$dtls$displayActionLink"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingMinutes_issueMinutes"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="meetingDetailsID"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <SEPARATOR/>


      <ACTION_CONTROL
        LABEL="ActionControl.Label.Delete"
        TYPE="ACTION"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingDetails_remove"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="meetingDetailsID"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
        </LINK>
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$dtls$displayActionLink"
          />
        </CONDITION>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Subject"
      WIDTH="30"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$subject"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Location"
      WIDTH="30"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartTime"
      WIDTH="20"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Duration"
      WIDTH="20"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="duration"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <!-- BEGIN, CR00238467, ELG -->
      <INLINE_PAGE PAGE_ID="CalendarMeetingDetails_viewDetailsInline">
        <!-- END, CR00238467 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="meetingDetailsID"
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
