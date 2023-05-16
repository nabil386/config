<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009, 2010 Curam Software Ltd.                                  -->
<!-- All rights reserved.                                                      -->
<!-- This software is the confidential and proprietary information of Curam    -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose       -->
<!-- such Confidential Information and shall use it only in accordance with    -->
<!-- the terms of the license agreement you entered into with Curam            -->
<!-- Software.                                                                 -->
<!-- Description                                                               -->
<!-- ===========                                                               -->
<!-- This page allows the user to create a contact log for an Incident         -->
<?curam-deprecated Since Curam 6.0, replaced with IncidentContactLogWizard_createContact.uim. As
  part of the change to convert the creation of contact logs using a wizard. See release note: CR00226335?>
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
    OPERATION="createIncidentContactLog1"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="ContactLog"
    NAME="DISPLAYPURPOSE"
    OPERATION="listPurpose"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="ContactLog"
    NAME="GETDEFAULTDETAILS"
    OPERATION="getDefaultContactLogDetails"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="incidentID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


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
      PROPERTY="incidentID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="linkID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >


    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Purpose"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="purposeCode"
          NAME="DISPLAYPURPOSE"
          PROPERTY="purposeName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="purpose"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Location"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="40"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="65"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactLogType"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <FIELD LABEL="Field.Label.LocationDescription">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Method"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="45"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="method"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="25"
    SHOW_LABELS="true"
    TITLE="Cluster.Label.ContactDetails"
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
      LABEL_WIDTH="25"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        HEIGHT="3"
        LABEL="Field.Label.IncidentParticipant"
        USE_BLANK="true"
        WIDTH="25"
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
      LABEL_WIDTH="25"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <CONTAINER LABEL="Field.Label.Participant">
        <FIELD WIDTH="25">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="concernRoleType"
            />
          </CONNECT>
        </FIELD>
        <FIELD WIDTH="50">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="attendeeDetails$concernRoleID"
            />
          </CONNECT>
        </FIELD>


      </CONTAINER>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.UnRegisteredParticipant"
      LABEL_WIDTH="25"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.ParticipantName"
        WIDTH="50"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="attendeeDetails$concernRoleName"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.User"
      LABEL_WIDTH="25"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.User"
        WIDTH="50"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="attendeeDetails$userName"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="FALSE"
    TITLE="Cluster.Label.NarrativeDetails"
  >
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="2"
      LABEL="Cluster.Label.NarrativeDetails"
    >
      <!-- END, CR00463142 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notesText"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
