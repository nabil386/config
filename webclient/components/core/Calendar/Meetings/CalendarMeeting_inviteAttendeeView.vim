<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012-2016. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2004, 2010-2011 Curam Software Ltd.                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Allows the user to invite an attendee to a standard activity when      -->
<!-- coming from the view page.                                             -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.InviteAttendee"
      />
    </CONNECT>
  </PAGE_TITLE>
  <SERVER_INTERFACE
    CLASS="MeetingManagement"
    NAME="DISPLAY"
    OPERATION="listAttendeesForInvite"
  />
  <SERVER_INTERFACE
    CLASS="MeetingManagement"
    NAME="ACTION"
    OPERATION="inviteAttendeeInformationalMessage"
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
  
  <PAGE_PARAMETER NAME="activityID"/>
  <PAGE_PARAMETER NAME="description"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="activityID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="activityID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="activityID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="activityID"
    />
  </CONNECT>


  <CLUSTER NUM_COLS="2">


    <CLUSTER
      DESCRIPTION="Cluster.AttendeeSearch.Description"
      LABEL_WIDTH="24"
    >
      <CONTAINER LABEL="Container.Label.Attendee">
        <FIELD WIDTH="30">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="type"
            />
          </CONNECT>
        </FIELD>
        <FIELD>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="relatedID"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>
    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.AttendeeEmail.Description"
      LABEL_WIDTH="24"
    >
      <FIELD LABEL="Field.Label.EmailAddress">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="emailAddress"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >


    <ACTION_SET
      ALIGNMENT="CENTER"
      BOTTOM="false"
    >
      <ACTION_CONTROL
        DEFAULT="true"
        IMAGE="AddButton"
        LABEL="ActionControl.Label.Add"
        TYPE="SUBMIT"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="THIS"
          SAVE_LINK="false"
        >
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="1"
    STYLE="outer-cluster-borderless"
  >


    <LIST TITLE="List.Title.Attendees">
      <FIELD WIDTH="5">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="meetingOrganizerInd"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Name"
        WIDTH="50"
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
        WIDTH="45"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="invitationStatus"
          />
        </CONNECT>
      </FIELD>
    </LIST>


  </CLUSTER>


  <ACTION_SET ALIGNMENT="RIGHT">
    <ACTION_CONTROL
      IMAGE="CloseButton"
      LABEL="ActionControl.Label.Close"
      TYPE="ACTION"
    />
  </ACTION_SET>


</VIEW>
