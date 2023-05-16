<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2005 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software, Ltd. ("Confidential Information"). You                 -->
<!-- shall not disclose such Confidential Information and shall use it only -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the modify objective list group pages.           -->
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
        PROPERTY="name"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="DISPLAY"
    OPERATION="readObjectiveListGroup"
  />


  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="ACTION"
    OPERATION="modifyObjectiveListGroup"
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
      PROPERTY="readObjectListGroupKey$ruleSetID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="nodeID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readObjectListGroupKey$nodeID"
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
    NUM_COLS="2"
  >


    <FIELD
      HEIGHT="2"
      LABEL="Field.Label.Name"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Conjunction">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="conjunctionCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="conjunctionCode"
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


    <FIELD
      LABEL="Field.Label.Priority"
      WIDTH="3"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="priority"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priority"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ListRulesDataObject">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="listRulesDataObjectName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="listRulesDataObjectID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="listRulesDataObjectID"
        />
      </CONNECT>
      <LINK>
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
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.RuleID"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ruleID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="25"
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
