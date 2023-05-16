<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008 Curam Software Ltd.                                         -->
<!-- All rights reserved.                                                       -->
<!-- This software is the confidential and proprietary information of Curam     -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose        -->
<!-- such Confidential Information and shall use it only in accordance with     -->
<!-- the terms of the license agreement you entered into with Curam             -->
<!-- Software.                                                                  -->
<!-- Description                                                                -->
<!-- ===========                                                                -->
<!-- This page allows the user to create an action.                             -->
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
    CLASS="Action"
    NAME="ACTION"
    OPERATION="createAction"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="parentLinkID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="parentLinkID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="parentLinkID"
    />
  </CONNECT>
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


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Label.Details"
  >


    <FIELD
      LABEL="Field.Label.SituationCategory"
      USE_BLANK="false"
      USE_DEFAULT="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="situationCategory"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SituationRequiringAction">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="situationRequiringAction"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Action">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="action"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Outcome"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="outcome"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ExpectedEndDate"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="expectedEndDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ActualEndDate"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="actualEndDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="1"
    SHOW_LABELS="true"
    TITLE="Cluster.Label.OwnerDetails"
  >


    <CLUSTER
      DESCRIPTION="Cluster.Description.CaseParticipant"
      LABEL_WIDTH="45"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
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
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.User"
      LABEL_WIDTH="45"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        HEIGHT="1"
        LABEL="Field.Label.User"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="ownerUserName"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="1"
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Comments"
  >


    <!-- BEGIN, CR00417389, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00417389 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
