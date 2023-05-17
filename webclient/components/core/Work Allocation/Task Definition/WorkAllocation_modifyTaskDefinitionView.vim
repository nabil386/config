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
<!-- The included view for the modify task definition pages.                -->
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
    CLASS="WorkAllocation"
    NAME="DISPLAY"
    OPERATION="readTaskDefinitionForModificationDetails"
  />


  <SERVER_INTERFACE
    CLASS="WorkAllocation"
    NAME="ACTION"
    OPERATION="modifyTaskDefinition"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="taskDefinitionID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="taskDefinitionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$dtls$taskDefinitionID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="taskDefinitionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="taskDefinitionID"
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
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.TaskDefinitionID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="modifyDtls$dtls$taskDefinitionID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Category">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="categoryCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="categoryCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ManualForwardAllowed">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allowForwardInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="allowForwardInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Name">
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


    <FIELD LABEL="Field.Label.AdministrationSID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="administrationSID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="administrationSID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Description.AllocationStrategy"
    NUM_COLS="2"
    TITLE="Cluster.Title.AllocationStrategy"
  >


    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allocationStrategyCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="allocationStrategyCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Description.SelectSystemStrategy"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.RuleSet">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="allocationRuleSetName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allocationRuleSetID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="allocationRuleSetID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AllocationTarget">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="allocationTargetName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allocationTargetID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="allocationTargetID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Function">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="allocationFunctionName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allocationFunctionID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="allocationFunctionID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.DeadlineStrategy"
  >


    <FIELD LABEL="Field.Label.Function">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="deadlineFunctionName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deadlineFunctionID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deadlineFunctionID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.TimeOverrideAllowed">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deadlineTimeOverrideInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deadlineTimeOverrideInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Description.DeadlineStrategy"
    NUM_COLS="3"
  >


    <FIELD
      LABEL="Field.Label.Days"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deadlineTimeoutDays"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deadLineTimeoutDays"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Hours"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deadlineTimeoutHours"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deadLineTimeoutHours"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Minutes"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deadlineTimeoutMinutes"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deadLineTimeoutMinutes"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="25"
    TITLE="Cluster.Title.ActionPage"
  >


    <FIELD LABEL="Field.Label.PageName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actionPageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="actionPageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Description"
      USE_BLANK="true"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actionPageNameCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="actionPageNameCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CaseIDParameterName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseIDParameterName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseIDParameterName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ParticipantIDParameterName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="participantIDParameterName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="participantIDParameterName"
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