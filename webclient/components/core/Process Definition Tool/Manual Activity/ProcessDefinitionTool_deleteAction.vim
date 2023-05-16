<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  PAGE_ID="ProcessDefinitionTool_deleteAction"
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
    CLASS="ActivityActionAdmin"
    NAME="ACTION"
    OPERATION="deleteActivityAction"
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
  <PAGE_PARAMETER NAME="versionNo"/>
  <PAGE_PARAMETER NAME="activityID"/>
  <PAGE_PARAMETER NAME="actionID"/>
  <PAGE_PARAMETER NAME="actionText"/>
  <PAGE_PARAMETER NAME="actionType"/>
  <PAGE_PARAMETER NAME="activityType"/>
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
      PROPERTY="actionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="actionID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="actionType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="actionType"
    />
  </CONNECT>
</VIEW>
