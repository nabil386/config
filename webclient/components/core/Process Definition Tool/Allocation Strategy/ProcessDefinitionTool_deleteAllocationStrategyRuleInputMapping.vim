<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  PAGE_ID="ProcessDefinitionTool_deleteAllocationRuleInputMapping"
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
    CLASS="AllocationStrategyAdmin"
    NAME="ACTION"
    OPERATION="deleteRuleInputMapping"
    PHASE="ACTION"
  />
  <CLUSTER
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Question.Description"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <PAGE_PARAMETER NAME="processID"/>
  <PAGE_PARAMETER NAME="processVersionNo"/>
  <PAGE_PARAMETER NAME="activityID"/>
  <PAGE_PARAMETER NAME="rdoAttribute"/>
  <PAGE_PARAMETER NAME="versionNo"/>
  <PAGE_PARAMETER NAME="strategyParent"/>
  <ACTION_SET>
    <ACTION_CONTROL
      LABEL="ActionControl.Label.Yes"
      TYPE="SUBMIT"
    />
    <ACTION_CONTROL LABEL="ActionControl.Label.No"/>
  </ACTION_SET>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="processID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="processID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="processVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="processVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="activityID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="activityID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="rdoAttribute"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="rdoAttribute"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="strategyParent"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="strategyParent"
    />
  </CONNECT>
</VIEW>
