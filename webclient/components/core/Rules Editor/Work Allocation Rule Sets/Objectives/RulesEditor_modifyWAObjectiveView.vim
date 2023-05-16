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
<!-- The included view for the modify objective pages.                      -->
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
        PROPERTY="result$summaryDetails$objectiveDtls$name"
      />
    </CONNECT>
  </PAGE_TITLE>
  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="DISPLAY"
    OPERATION="readWAObjectiveSummary"
  />
  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="ACTION"
    OPERATION="modifyWAObjective"
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
      PROPERTY="ruleSetID"
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
          PROPERTY="result$summaryDetails$objectiveDtls$name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
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
          PROPERTY="result$summaryDetails$objectiveDtls$ruleID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ruleID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.RulesObjectiveID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rulesObjectiveID"
        />
      </CONNECT>
    </FIELD>
    <CONTAINER LABEL="Field.Label.TargetItem">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$tagDetails$typeCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="typeCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <INITIAL
            NAME="DISPLAY"
            PROPERTY="result$tagDetails$description"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$tagDetails$value"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="value"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
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
  </CLUSTER>
  <CLUSTER LABEL_WIDTH="22.5">
    <ACTION_SET BOTTOM="false">
      <ACTION_CONTROL LABEL="ActionControl.Label.RelatedReference">
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
      LABEL="Field.Label.RelatedReference"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="relatedReference"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="relatedReference"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="3" LABEL="Field.Label.Comments">
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
