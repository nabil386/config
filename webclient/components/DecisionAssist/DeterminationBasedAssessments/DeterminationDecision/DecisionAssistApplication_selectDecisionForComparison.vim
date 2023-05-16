<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007-2008, 2010 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to select decisions for comparison.          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <SERVER_INTERFACE
    CLASS="DeterminationExecution"
    NAME="SELECT_DECISION_FOR_COMPARISON"
    OPERATION="listDecisionsDetailsForComparison"
    PHASE="DISPLAY"
  />
  <SERVER_INTERFACE
    CLASS="DeterminationExecution"
    NAME="ACTION_COMPARE_DECISION"
    OPERATION="checkDecisionSelected"
    PHASE="ACTION"
  />
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="determinationDecisionID"
    />
    <TARGET
      NAME="SELECT_DECISION_FOR_COMPARISON"
      PROPERTY="key$key$determinationDecisionID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="determinationDeliveryID"
    />
    <TARGET
      NAME="SELECT_DECISION_FOR_COMPARISON"
      PROPERTY="key$key$determinationDeliveryID"
    />
  </CONNECT>
  <!-- BEGIN, CR00214223, PS -->
  <CLUSTER NUM_COLS="2">
    <!-- END, CR00214223 -->
    <FIELD LABEL="Field.Label.DecisionMadeBy">
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$decisionMadeByUserFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="SELECT_DECISION_FOR_COMPARISON"
            PROPERTY="result$dtls$dtls$decisionMadeBy"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtls$status"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ResultType">
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtls$resultType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DecisionType">
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtls$decisionType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DecisionDate">
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtls$decisionDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.EffectiveDate">
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtls$effectiveDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Applied">
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtls$appliedInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="25"
    TITLE="Cluster.Title.Contradiction"
  >
    <FIELD LABEL="Field.Label.Contradiction">
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtls$contradictionMessage"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.OutcomesAndScore"
  >
    <CONDITION>
      <IS_TRUE
        NAME="ASSESSMENTCONTEXT"
        PROPERTY="result$dtls$totalScoreInd"
      />
    </CONDITION>
    <FIELD
      LABEL="Field.Label.Outcomes"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtls$outcomes"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.TotalScore"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtls$totalScore"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <!-- IF THERE IS NO TOTAL SCORE ,TOTAL SCORE FIELD SHOULD NOT BE SHOWN -->
  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.OutcomesAndScore"
  >
    <CONDITION>
      <IS_FALSE
        NAME="ASSESSMENTCONTEXT"
        PROPERTY="result$dtls$totalScoreInd"
      />
    </CONDITION>
    <FIELD
      LABEL="Field.Label.Outcomes"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtls$outcomes"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <LIST TITLE="Cluster.Title.SelectDecision">
    <CONDITION>
      <IS_TRUE
        NAME="ASSESSMENTCONTEXT"
        PROPERTY="result$dtls$totalScoreInd"
      />
    </CONDITION>
    <CONTAINER
      ALIGNMENT="CENTER"
      LABEL="Widget.Label.Select"
      WIDTH="8"
    >
      <WIDGET TYPE="SINGLESELECT">
        <WIDGET_PARAMETER NAME="SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="SELECT_DECISION_FOR_COMPARISON"
              PROPERTY="result$dtls$dtlsList$dtls$determinationDecisionID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION_COMPARE_DECISION"
              PROPERTY="key$dtls$dtls$determinationDecisionID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>
    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$decisionDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EffectiveDate"
      WIDTH="13"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$effectiveDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.ResultType"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$resultType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Outcomes"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$outcomes"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.TotalScore"
      WIDTH="8"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$totalScore"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$status"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <!-- IF THERE IS NO TOTAL SCORE ,TOTAL SCORE COLUMN SHOULD NOT BE SHOWN -->
  <LIST TITLE="Cluster.Title.SelectDecision">
    <CONDITION>
      <IS_FALSE
        NAME="ASSESSMENTCONTEXT"
        PROPERTY="result$dtls$totalScoreInd"
      />
    </CONDITION>
    <!-- BEGIN, CR00217013, PS -->
    <CONTAINER
      ALIGNMENT="CENTER"
      LABEL="Widget.Label.Select"
      WIDTH="10"
    >
      <WIDGET TYPE="SINGLESELECT">
        <WIDGET_PARAMETER NAME="SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="SELECT_DECISION_FOR_COMPARISON"
              PROPERTY="result$dtls$dtlsList$dtls$determinationDecisionID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION_COMPARE_DECISION"
              PROPERTY="key$dtls$dtls$determinationDecisionID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>
    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$decisionDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EffectiveDate"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$effectiveDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.ResultType"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$resultType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Outcomes"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$outcomes"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="SELECT_DECISION_FOR_COMPARISON"
          PROPERTY="result$dtls$dtlsList$dtls$status"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00217013 -->
  </LIST>
</VIEW>
