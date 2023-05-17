<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                       -->
<!-- This software is the confidential and proprietary information of Curam     -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose        -->
<!-- such Confidential Information and shall use it only in accordance with 	-->
<!-- the terms of the license agreement you entered into with Curam         	-->
<!-- Software.                                                              	-->
<!-- Description                                                            	-->
<!-- ===========                                                            	-->
<!-- This page allows the user to create an allegation role.     	        -->
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
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listCaseParticipantsDetails"
  />


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="ACTION"
    OPERATION="addContactLogAttendee"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="contactLogID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
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
    LABEL_WIDTH="20"
    NUM_COLS="1"
    SHOW_LABELS="true"
    TITLE="Cluster.Label.AttendeeDetails"
  >


    <CLUSTER
      DESCRIPTION="Cluster.Description.CaseParticipant"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        HEIGHT="4"
        LABEL="Field.Label.CaseParticipant"
        USE_BLANK="true"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="caseParticipantRoleID"
            NAME="DISPLAY"
            PROPERTY="name"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="caseParticipantRoleIDs"
          />
        </CONNECT>
      </FIELD>


      <FIELD CONTROL="SKIP"/>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.RegisteredParticipant"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <CONTAINER LABEL="Field.Label.Participant">
        <FIELD>
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


        <FIELD CONTROL="SKIP"/>
        <FIELD CONTROL="SKIP"/>


      </CONTAINER>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.UnRegisteredParticipant"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.ParticipantName">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="concernRoleName"
          />
        </CONNECT>
      </FIELD>


      <FIELD CONTROL="SKIP"/>


    </CLUSTER>


  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Description.User"
    NUM_COLS="2"
    SHOW_LABELS="true"
    TITLE="Cluster.Label.UserDetails"
  >


    <FIELD LABEL="Field.Label.User">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="userName"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


  </CLUSTER>


</VIEW>
