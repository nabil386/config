<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2010  Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                         -->
<!-- This software is the confidential and proprietary information of Curam       -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose          -->
<!-- such Confidential Information and shall use it only in accordance with       -->
<!-- the terms of the license agreement you entered into with Curam               -->
<!-- Software.                                                                    -->
<!-- Description                                                                  -->
<!-- ===========                                                                  -->
<!-- The included view to modify an issue approval request.                       -->
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
    NAME="ACTION"
    OPERATION="modifyAllegation"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="InvestigationDelivery"
    NAME="DISPLAY"
    OPERATION="readAllegationForModify"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="allegationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="allegationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$allegationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="allegationDtls$allegationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="allegationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="creationDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="creationDate"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="49"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
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
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="location"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00186307,  DJ -->
    <FIELD LABEL="Field.Label.AllegationDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allegationDateTime"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="allegationDateTime"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00186307 -->
  </CLUSTER>
  <!-- BEGIN, CR00143891, DJ -->
  <CLUSTER
    LABEL_WIDTH="49"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.Address"
  >
    <FIELD LABEL="Field.Label.AddressData">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$allegationDtls$dtls$addressData"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="modifyAllegationDetails$dtls$addressData"
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
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="description"
        />
      </CONNECT>
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
            HIDDEN_PROPERTY="searchCaseParticipantDetails$caseParticipantRoleID"
            NAME="DISPLAY"
            PROPERTY="name"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="sourceCaseParticipantRoleID"
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
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="sourceName"
          />
        </CONNECT>
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
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="allegationDtls$anonymousInd"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="sourceDetails$anonymousInd"
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
        USE_DEFAULT="false"
        WIDTH="45"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reportedDateTime"
          />
        </CONNECT>
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
        USE_DEFAULT="false"
        WIDTH="21"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="method"
          />
        </CONNECT>
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
