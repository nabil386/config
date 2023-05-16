<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003-2004, 2010 - 2011 Curam Software Ltd.               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- A view which, when included, allows the user to invite an attendee to  -->
<!-- a standard activity.                                                   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title.InviteAttendee"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Activity"
    NAME="DISPLAY"
    OPERATION="readActivityAttendeeList"
  />


  <SERVER_INTERFACE
    CLASS="ProductDelivery"
    NAME="DISPLAYPARTICIPANTS"
    OPERATION="readCaseEventInvitedAttendeeParticipants"
  />


  <SERVER_INTERFACE
    CLASS="Activity"
    NAME="ACTION"
    OPERATION="inviteAttendeeToStandardActivity"
    PHASE="ACTION"
  />


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


  <!-- BEGIN, CR00357198, AC-->
  <CLUSTER LABEL_WIDTH="50">
    <CONTAINER LABEL="Container.Label.Attendee">
      <FIELD WIDTH="50">


        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="activityAttendeeType"
            NAME="DISPLAYPARTICIPANTS"
            PROPERTY="activityAttendeeDescription"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="activityAttendeeType"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="activityAttendeeID"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00358440, AC-->
    </CONTAINER>
    <FIELD
      LABEL="Field.Label.IgnoreConflict"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ignoreConflictsIndOpt"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00357198-->
    <!-- END, CR00358440-->
  </CLUSTER>


  <LIST
    
    TITLE="List.Title.Attendees"
  >
    <FIELD LABEL="Field.Label.Attendee">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attendeeName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="invitationStatus"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
