<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2005, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page is a view for creating a new hearing case where multiple     -->
<!-- cases can be appealed.                                                 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="HearingCase"
    NAME="DISPLAY"
    OPERATION="readForCreate"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="HearingCase"
    NAME="ACTION"
    OPERATION="create"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="parentCaseID"/>
  <PAGE_PARAMETER NAME="implCaseID"/>
  <PAGE_PARAMETER NAME="participantRoleID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="priorAppealCaseID"/>
  <PAGE_PARAMETER NAME="appellantTypeCode"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="implCaseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="participantRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="participantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="priorAppealCaseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="priorAppealCaseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="appellantTypeCode"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="appellantTypeCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="implCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="priorAppealCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="priorAppealCaseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="appellantTypeCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="appellantTypeCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="participantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="participantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="parentCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="parentCaseID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.HearingCaseDetails"
  >


    <FIELD LABEL="Field.Label.Appellant">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appellantDisplayName"
        />
      </CONNECT>


      <LINK PAGE_ID="Participant_resolveRoleHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="appellantParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="participantRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.Difficulty">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="difficultyCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.AppealedCaseDetails"
  >


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.DateReceived">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateReceived"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ResolutionApprovalDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReceiptMethod">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptMethod"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Emergency">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="emergencyCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ContinueBenefits">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="continueBenefitsIndicator"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReceiptNotice">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptNoticeIndicator"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00407812, RB -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00407812 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
