<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010-2011 Curam Software Ltd.                        -->
<!-- All rights reserved.                                                     -->
<!-- This software is the confidential and proprietary information of Curam   -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose      -->
<!-- such Confidential Information and shall use it only in accordance with   -->
<!-- the terms of the license agreement you entered into with Curam           -->
<!-- Software.                                                                -->
<!-- Description                                                              -->
<!-- ===========                                                              -->
<!-- This page allows the user to create a contact participant.               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Incidents"
    NAME="DISPLAY"
    OPERATION="listIncidentActiveParticipantRole"
  />


  <SERVER_INTERFACE
    CLASS="Incidents"
    NAME="ACTION"
    OPERATION="addContactLogContact1"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="incidentID"/>
  <PAGE_PARAMETER NAME="contactLogID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="incidentID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="incidentID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="contactLogID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="contactLogID"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="true"
    TITLE="Cluster.Label.ContactDetails"
  >


    <CLUSTER LABEL_WIDTH="35">


      <FIELD LABEL="Field.Label.CurrentUserIsParticipant">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="currentUserIsAttendeeInd"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.IncidentParticipant"
      LABEL_WIDTH="35"
      NUM_COLS="1"
    >


      <FIELD
        CONTROL="CHECKBOXED_LIST"
        HEIGHT="3"
        LABEL="Field.Label.IncidentParticipant"
        USE_BLANK="true"
        WIDTH="50"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="incidentParticipantRoleID"
            NAME="DISPLAY"
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
      DESCRIPTION="Cluster.Description.RegisteredParticipant"
      LABEL_WIDTH="35"
      NUM_COLS="1"
      STYLE="cluster-cpr-no-border"
    >


      <CONTAINER LABEL="Field.Label.Participant">
        <FIELD WIDTH="50">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="concernRoleType"
            />
          </CONNECT>
        </FIELD>
        <FIELD>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
        </FIELD>


      </CONTAINER>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.UnRegisteredParticipant"
      LABEL_WIDTH="35"
      NUM_COLS="1"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.ParticipantName"
        WIDTH="50"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="contactDtls$concernRoleName"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.User"
      LABEL_WIDTH="35"
      NUM_COLS="1"
      SHOW_LABELS="true"
      TITLE="Cluster.Label.UserDetails"
    >


      <FIELD
        LABEL="Field.Label.User"
        WIDTH="50"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="userName"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


  </CLUSTER>


</VIEW>
