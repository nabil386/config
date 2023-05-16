<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

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
<!-- The included view for the modify rule  pages.                          -->
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
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="ruleName"
      />
    </CONNECT>
  </PAGE_TITLE>
  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="DISPLAY"
    OPERATION="readRule"
  />
  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="ACTION"
    OPERATION="modifyWARule"
    PHASE="ACTION"
  />
  <PAGE_PARAMETER NAME="ruleSetID"/>
  <PAGE_PARAMETER NAME="nodeID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="ruleSetID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readRuleKey$ruleSetID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="nodeID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readRuleKey$nodeID"
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
      NAME="PAGE"
      PROPERTY="nodeID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="nodeID"
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
  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <FIELD
      HEIGHT="2"
      LABEL="Field.Label.Name"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ruleName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
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
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ruleID"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER LABEL_WIDTH="22.5">
    <ACTION_SET BOTTOM="false">
      <ACTION_CONTROL LABEL="ActionControl.Label.ExpressionHelper">
        <LINK
          OPEN_NEW="true"
          PAGE_ID="RulesEditor_searchRulesDataItemToCopy"
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
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Expression"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expressionText"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="expressionText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
