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
<!-- This page allows a user to display compare the evidence executed by    -->
<!-- an original decision for a period as opposed to a decision created     -->
<!-- as a result of a reassessment for that period. Only the differences    -->
<!-- between the two sets of evidence are displayed.                        -->
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
    OPERATION="compareChangedEvidenceOnly"
  />


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
          PROPERTY="result$decision$initReasonCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DecisionFromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decision$decisionFromDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decision$statusCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DecisionToDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decision$decisionToDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER SHOW_LABELS="false">
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$evidence$evidenceDifference"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
