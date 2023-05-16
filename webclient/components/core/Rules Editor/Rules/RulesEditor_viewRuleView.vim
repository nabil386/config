<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software, Ltd. ("Confidential Information"). You                 -->
<!-- shall not disclose such Confidential Information and shall use it only -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page displaying the details of a rule.                                 -->
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
    OPERATION="readRuleSummary"
  />
  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="DISPLAY1"
    OPERATION="listTranslation"
  />
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="ruleSetID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="ruleSetNodeKey$ruleSetID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="nodeID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="ruleSetNodeKey$nodeID"
    />
  </CONNECT>
  
  <PAGE_PARAMETER NAME="ruleSetID"/>
  <PAGE_PARAMETER NAME="nodeID"/>
  
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="ruleSetID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$ruleSetID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="nodeID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$nodeID"
    />
  </CONNECT>
  
  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ruleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.RuleID"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ruleID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Highlight">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="highlightID"
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
    </FIELD>
    <FIELD LABEL="Field.Label.Summary">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summaryInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Expression">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expressionText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="20"
    TITLE="Cluster.Title.ResultText"
  >
    <FIELD LABEL="Field.Label.Success">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="successText"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Failure">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="failureText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Exclusions"
  >
    <FIELD LABEL="Field.Label.FromSimulation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="excludeFromSimulationInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.FromReassessment">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="excludeFromReassessmentInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  <LIST TITLE="List.Title.Translations">
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="RulesEditor_viewRuleTranslation">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="ruleSetID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="ruleSetID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="nodeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="nodeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="languageCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="languageCode"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ruleName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
    
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="RulesEditor_modifyRuleTranslation"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="nodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nodeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="languageCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="languageCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="ruleName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      
      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="RulesEditor_cancelRuleTranslation"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="nodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nodeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="languageCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="languageCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="ruleName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    
    <FIELD
      LABEL="Field.Title.Language"
      WIDTH="100"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="languageCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
