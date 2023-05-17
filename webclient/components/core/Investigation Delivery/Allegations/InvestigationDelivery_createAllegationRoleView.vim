<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.

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
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create an allegation role.                -->
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
    CLASS="InvestigationDelivery"
    NAME="DISPLAY"
    OPERATION="listCaseParticipant"
  />


  <SERVER_INTERFACE
    CLASS="InvestigationDelivery"
    NAME="ACTION"
    OPERATION="createAllegationRole"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="allegationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


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
      PROPERTY="allegationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="allegationID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="1"
    SHOW_LABELS="true"
  >


    <CLUSTER
      DESCRIPTION="Cluster.Description.CaseParticipant"
      LABEL_WIDTH="35"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.CaseParticipant"
        USE_BLANK="true"
        WIDTH="40"
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
      DESCRIPTION="Cluster.Description.RegisteredParticipant"
      LABEL_WIDTH="35"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <CONTAINER LABEL="Field.Label.Participant">
        <FIELD WIDTH="40">
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
              PROPERTY="participantRoleID"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>
      <FIELD CONTROL="SKIP"/>
    </CLUSTER>


    <!-- BEGIN, CR00119662, MC -->
    <CLUSTER
      DESCRIPTION="Cluster.Description.Participant"
      LABEL_WIDTH="35"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <CONTAINER LABEL="Field.Label.ParticipantName">
        <!-- BEGIN, CR00463142, EC -->
        <FIELD
          LABEL="Field.Label.ParticipantName"
          WIDTH="50"
        >
          <!-- END, CR00463142 -->
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="concernRoleName"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>
    </CLUSTER>
    <!-- END, CR00119662 -->


    <CLUSTER
      LABEL_WIDTH="35"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD
        HEIGHT="1"
        LABEL="Field.Label.Role"
        WIDTH="50"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="roleType"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


  </CLUSTER>


</VIEW>
