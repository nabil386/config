<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2005, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005, 2008 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Modify sporting activity evidence details.                             -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="SampleMaintainSportingActivity"
    NAME="DISPLAY"
    OPERATION="readSampleSportingActivityEvidence"
  />


  <SERVER_INTERFACE
    CLASS="SampleMaintainSportingActivity"
    NAME="ACTION"
    OPERATION="modifySampleSportingActivityEvidence"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="evidenceID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>


  <!-- BEGIN, CR00022287, TV -->
  <PAGE_PARAMETER NAME="evidenceType"/>
  <!-- END CR00022287 -->


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$sportingActivityID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$sportingActivityID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$sportingActivityID"
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
      PROPERTY="result$caseParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$caseParticipantRoleID"
    />
  </CONNECT>


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="result$warnings$dtls$msg"
      />
    </CONNECT>
  </INFORMATIONAL>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.HouseholdMember">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <INCLUDE FILE_NAME="Evidence_modifyHeader1.vim"/>


  </CLUSTER>


  <CLUSTER NUM_COLS="2">


    <FIELD LABEL="Field.Label.PaymentAmount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentAmount"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="paymentAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SportingActivityType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sportingActivityType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sportingActivityType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AwardType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sportingAwardType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sportingAwardType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EndDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="4" LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
