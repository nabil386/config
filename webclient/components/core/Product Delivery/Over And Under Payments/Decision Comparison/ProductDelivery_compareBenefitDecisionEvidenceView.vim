<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

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
<!-- Included view allowing the comparison of the evidence executed by      -->
<!-- an original decision for a period as opposed to a decision created     -->
<!-- as a result of a reassessment for that period.                         -->
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
    CLASS="ProductDelivery"
    NAME="DISPLAY"
    OPERATION="compareDecisionEvidence"
  />


  <ACTION_SET ALIGNMENT="CENTER">
    <ACTION_CONTROL
      IMAGE="CompareEvidenceButton"
      LABEL="ActionControl.Label.CompareEvidenceDifference"
    >
      <LINK PAGE_ID="ProductDelivery_compareEvidenceDifferenceOnly">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="oldDecisionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="oldDecisionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="reassessedDecisionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="reassessedDecisionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="reassessmentInfoID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="reassessmentInfoID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="CompareRulesButton"
      LABEL="ActionControl.Label.CompareRules"
    >
      <LINK PAGE_ID="ProductDelivery_compareBenefitDecisionRules">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="oldDecisionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="oldDecisionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="reassessedDecisionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="reassessedDecisionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="reassessmentInfoID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="reassessmentInfoID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="ViewOriginalDecisionButton"
      LABEL="ActionControl.Label.ViewOriginalDecision"
    >
      <LINK PAGE_ID="ProductDelivery_viewDecision">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="oldDecisionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="decisionID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="ViewReassessedDecisionButton"
      LABEL="ActionControl.Label.ViewReassessedDecision"
    >
      <LINK PAGE_ID="ProductDelivery_viewDecision">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="reassessedDecisionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="decisionID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <PAGE_PARAMETER NAME="oldDecisionID"/>
  <PAGE_PARAMETER NAME="reassessedDecisionID"/>
  <PAGE_PARAMETER NAME="reassessmentInfoID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="reassessedDecisionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="newCaseDecisionID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="oldDecisionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseDecisionID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="initReasonCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DecisionFromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionFromDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DecisionToDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionToDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >
    <CLUSTER
      SHOW_LABELS="false"
      TITLE="Cluster.Title.OriginalDecision"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="originalEvidence"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <CLUSTER
      SHOW_LABELS="false"
      TITLE="Cluster.Title.NewDecision"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="newEvidence"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>


</VIEW>
