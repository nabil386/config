<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  OCO Source Materials
  
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
<!-- Create new sporting activity evidence.                                 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="IntegratedCaseSampleSportingGrant"
    NAME="DISPLAY"
    OPERATION="readICPrimaryClientDetails"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="SampleMaintainSportingActivity"
    NAME="ACTION"
    OPERATION="createSampleSportingActivityEvidence"
    PHASE="ACTION"
  />


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


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="result$warnings$dtls$msg"
      />
    </CONNECT>
  </INFORMATIONAL>


  <CLUSTER
    LABEL_WIDTH="48"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD
      LABEL="Field.Label.HouseholdMember"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <INCLUDE FILE_NAME="Evidence_createHeader.vim"/>


  </CLUSTER>


  <CLUSTER NUM_COLS="2">


    <FIELD
      LABEL="Field.Label.PaymentAmount"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="paymentAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SportingActivityType">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sportingActivityType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EndDate"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AwardType">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sportingAwardType"
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
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
