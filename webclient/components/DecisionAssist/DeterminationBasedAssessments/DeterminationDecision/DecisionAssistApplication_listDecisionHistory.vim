<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a list of Decision History.          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <LIST>


    <!-- BEGIN, CR00213213, PS -->
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <!-- END, CR00213213 -->
      <ACTION_CONTROL
        LABEL="ActionControl.Label.CompareDecisions"
        TYPE="ACTION"
      >
        <!-- BEGIN, CR00217013, PS -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="DecisionAssistApplication_selectDecisionFromListDecisionHistory"
        >
          <!-- END, CR00217013 -->
          <CONNECT>
            <SOURCE
              NAME="LIST_DECISION_HISTORY"
              PROPERTY="result$list$list$dtls$determinationDecisionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="determinationDecisionID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="LIST_DECISION_HISTORY"
              PROPERTY="result$list$list$dtls$determinationDeliveryID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="determinationDeliveryID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- BEGIN, CR00213213, PS -->
    </ACTION_SET>
    <!-- END, CR00213213 -->


    <!-- BEGIN, CR00217013, PS -->
    <FIELD LABEL="Field.Label.Date">
      <CONNECT>
        <SOURCE
          NAME="LIST_DECISION_HISTORY"
          PROPERTY="result$list$list$dtls$decisionDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="LIST_DECISION_HISTORY"
          PROPERTY="result$list$list$dtls$decisionType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ResultType">
      <CONNECT>
        <SOURCE
          NAME="LIST_DECISION_HISTORY"
          PROPERTY="result$list$list$dtls$resultType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Outcome">
      <CONNECT>
        <SOURCE
          NAME="LIST_DECISION_HISTORY"
          PROPERTY="result$list$list$dtls$outcomeName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.TotalScore">
      <CONNECT>
        <SOURCE
          NAME="LIST_DECISION_HISTORY"
          PROPERTY="result$list$list$dtls$totalScore"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="LIST_DECISION_HISTORY"
          PROPERTY="result$list$list$dtls$status"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00217013 -->
    <!-- BEGIN, CR00213213, PS -->
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="DecisionAssistApplication_viewDecisionDetails">
        <CONNECT>
          <SOURCE
            NAME="LIST_DECISION_HISTORY"
            PROPERTY="result$list$list$dtls$determinationDecisionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="determinationDecisionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="LIST_DECISION_HISTORY"
            PROPERTY="result$list$list$dtls$determinationDeliveryID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="determinationDeliveryID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
    <!-- END, CR00213213 -->
  </LIST>
</VIEW>
