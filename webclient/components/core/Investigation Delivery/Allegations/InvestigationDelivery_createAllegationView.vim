<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2010 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create an allegation.                     -->
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
    OPERATION="createAllegation"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


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
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="49"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Location"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00186307, DJ -->
    <FIELD LABEL="Field.Label.AllegationDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="allegationDateTime"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00186307 -->
  </CLUSTER>


  <!-- BEGIN,CR00143891,DJ -->
  <CLUSTER
    LABEL_WIDTH="49"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.Address"
  >
    <FIELD LABEL="Field.Label.AddressData">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="createAllegationDetails$dtls$addressData"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- END,CR00143891 -->


  <CLUSTER
    LABEL_WIDTH="24.5"
    NUM_COLS="1"
  >
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Description"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="27.5"
    NUM_COLS="1"
    SHOW_LABELS="true"
    TITLE="Cluster.Label.ParticipantDetails"
  >
    <CLUSTER
      DESCRIPTION="Cluster.Description.CaseParticipant"
      LABEL_WIDTH="32.5"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.CaseParticipant"
        USE_BLANK="true"
        WIDTH="45"
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
            PROPERTY="allegationRoleDetails$caseParticipantRoleID"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.RegisteredParticipant"
      LABEL_WIDTH="32.5"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <CONTAINER LABEL="Field.Label.Participant">
        <FIELD WIDTH="45">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="allegationRoleDetails$concernRoleType"
            />
          </CONNECT>
        </FIELD>
        <FIELD WIDTH="45">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="allegationRoleDetails$participantRoleID"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>
      <FIELD CONTROL="SKIP"/>
    </CLUSTER>


    <!-- BEGIN, CR00119662, MC -->
    <CLUSTER
      DESCRIPTION="Cluster.Description.Participant"
      LABEL_WIDTH="32.5"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD
        LABEL="Field.Label.ParticipantName"
        WIDTH="45"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="concernRoleName"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <!-- END, CR00119662 -->


    <CLUSTER
      LABEL_WIDTH="32.5"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        HEIGHT="1"
        LABEL="Field.Label.Role"
        WIDTH="45"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="allegationRoleDetails$roleType"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="32.5"
    NUM_COLS="1"
    SHOW_LABELS="true"
    TITLE="Cluster.Label.SourceDetails"
  >


    <CLUSTER
      DESCRIPTION="Cluster.Description.SourceCaseParticipant"
      LABEL_WIDTH="32.5"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.SourceCaseParticipant"
        USE_BLANK="true"
        USE_DEFAULT="false"
        WIDTH="45"
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
            PROPERTY="sourceDetails$caseParticipantRoleID"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.SourceRegisteredParticipant"
      LABEL_WIDTH="32.5"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <CONTAINER LABEL="Field.Label.SourceParticipant">
        <FIELD WIDTH="45">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="sourceDetails$concernRoleType"
            />
          </CONNECT>
        </FIELD>
        <FIELD WIDTH="45">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="sourceDetails$participantRoleID"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>
      <FIELD CONTROL="SKIP"/>
    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.SourceUnRegisteredParticipant"
      LABEL_WIDTH="32.5"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.SourceName"
        WIDTH="45"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="sourceName"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.SourceAnonymous"
      LABEL_WIDTH="32.5"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.Anonymous">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="allegationDtls$anonymousInd"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="32.5"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD
        LABEL="Field.Label.ReportedDate"
        USE_DEFAULT="true"
        WIDTH="45"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="reportedDateTime"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Method"
        USE_BLANK="true"
        WIDTH="21"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="method"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>


</VIEW>
