<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd..                                    -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create a contact log for a case.          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    BEHAVIOR="NONE"
    LABEL_WIDTH="25"
    NUM_COLS="1"
    SHOW_LABELS="true"
  >


    <FIELD LABEL="Field.Label.CurrentUserIsParticipant">
      <CONNECT>
        <SOURCE
          NAME="GETDEFAULTDETAILS"
          PROPERTY="currentUserIsAttendeeInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="currentUserIsAttendeeInd"
        />
      </CONNECT>
    </FIELD>


    <CLUSTER
      DESCRIPTION="Cluster.Description.IncidentParticipant"
      NUM_COLS="2"
    >
      <FIELD
        CONTROL="CHECKBOXED_LIST"
        HEIGHT="3"
        LABEL="Field.Label.IncidentParticipant"
        USE_BLANK="false"
        WIDTH="90"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="incidentParticipantRoleIDTabList"
          />
        </CONNECT>
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="incidentParticipantRoleID"
            NAME="DISPLAYPARTICIPANTS"
            PROPERTY="participantName"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="incidentParticipantRoleIDTabList"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      BEHAVIOR="NONE"
      DESCRIPTION="Cluster.Description.RegisteredParticipant"
      LABEL_WIDTH="25"
      NUM_COLS="1"
      SHOW_LABELS="true"
    >
      <CONTAINER LABEL="Field.Label.RegisteredParticipant">
        <FIELD WIDTH="25">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleType"
            />
          </CONNECT>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="concernRoleType"
            />
          </CONNECT>
        </FIELD>
        <FIELD WIDTH="40">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <INITIAL
              NAME="DISPLAY"
              PROPERTY="participantName"
            />
          </CONNECT>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>
      <FIELD CONTROL="SKIP"/>
    </CLUSTER>


    <CLUSTER
      BEHAVIOR="NONE"
      DESCRIPTION="Cluster.Description.UnRegisteredParticipant"
      LABEL_WIDTH="50"
      NUM_COLS="2"
      SHOW_LABELS="true"
    >
      <FIELD
        LABEL="Field.Label.ParticipantName"
        WIDTH="82"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleName"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="concernRoleName"
          />
        </CONNECT>
      </FIELD>
      <FIELD CONTROL="SKIP"/>
    </CLUSTER>


    <CLUSTER
      BEHAVIOR="NONE"
      DESCRIPTION="Cluster.Description.User"
      LABEL_WIDTH="50"
      NUM_COLS="2"
      SHOW_LABELS="true"
    >
      <FIELD
        HEIGHT="1"
        LABEL="Field.Label.User"
        WIDTH="108"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userName"
          />
        </CONNECT>
        <CONNECT>
          <INITIAL
            NAME="DISPLAY"
            PROPERTY="userName"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="userName"
          />
        </CONNECT>
      </FIELD>
      <FIELD CONTROL="SKIP"/>
    </CLUSTER>
  </CLUSTER>
</VIEW>
