<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software, Ltd. ("Confidential Information"). You                 -->
<!-- shall not disclose such Confidential Information and shall use it only -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the modify eligibility rule set pages.           -->
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
    CLASS="RulesEditor"
    NAME="DISPLAY"
    OPERATION="viewRuleSet"
  />


  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="ACTION"
    OPERATION="modifyRuleSet"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="ruleSetID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="ruleSetID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="ruleSetIDKey$ruleSetID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="ruleSetID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="ruleSetID"
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
      PROPERTY="ruleSetCategory"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="ruleSetCategory"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.RuleSetID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$ruleSetID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LegislationBase">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="legislationBase"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="legislationBase"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Highlight">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="highLightInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="highLightInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ruleSetName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ruleSetName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LegislationID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="legislationID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="legislationID"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="22.5"
    TITLE="Cluster.Title.ResultText"
  >
    <FIELD
      HEIGHT="5"
      LABEL="Field.Label.Success"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="successText"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="successText"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      HEIGHT="5"
      LABEL="Field.Label.Failure"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="failureText"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="failureText"
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
