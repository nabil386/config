<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="AppealedCase"
    NAME="DISPLAY"
    OPERATION="read"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="appealRelationshipID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="appealRelationshipID"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    LABEL_WIDTH="35"
  >
    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ReceiptMethod">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receiptMethod"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.EffectiveDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appealedDecisionDate"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.Timely">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="timelyIndicator"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ReceiptAcknowledged">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receiptNoticeIndicator"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.PriorAppealCase">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="priorCaseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="readSummaryDetails$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    
    <FIELD LABEL="Field.Label.Emergency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="emergencyCode"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.DeadlineDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deadlineDate"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ContinueBenefits">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="continueBenefitsIndicator"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.AppealedStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>
    
  </CLUSTER>


  <!-- BEGIN, CR00094229, RKi -->
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Description"
  >
    <FIELD LABEL="Field.Label.Comments">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- END, CR00094229 -->


</VIEW>
