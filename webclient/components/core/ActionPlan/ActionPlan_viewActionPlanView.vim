<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for viewing an action plan details.                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.ExpectedEndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$actionPlanDetails$expectedEndDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Outcome">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actionPlanDetails$outcome"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ActualEndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$actionPlanDetails$actualEndDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- BEGIN, CR00200707, AK -->
  <LIST TITLE="List.Title.SituationList">
    <FIELD
      LABEL="Field.Label.Action.SituationCategory"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="situationCategory"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00214099, AK -->
    <FIELD
      LABEL="Field.Label.Action.SituationReqAction"
      WIDTH="60"
    >
      <!-- END, CR00214099 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="situtaionReqAction"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00214099, AK -->
    <FIELD
      LABEL="Field.Label.Action.Actions"
      WIDTH="10"
    >
      <!-- END, CR00214099 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actionCnt"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Situation_listActions"
        SAVE_LINK="true"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="situationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="situationID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <!-- BEGIN, CR00214099, AK -->
    <FIELD
      LABEL="Field.Label.Action.Allegations"
      WIDTH="10"
    >
      <!-- END, CR00214099 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allegationCnt"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Situation_listAllegations"
        SAVE_LINK="true"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="situationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="situationID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </LIST>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- END, CR00200707 -->
</VIEW>
