<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007-2008 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page displays details of a particular Process Instance            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title.StaticText"
      />
    </CONNECT>
  </PAGE_TITLE>


  <PAGE_PARAMETER NAME="processName"/>
  <PAGE_PARAMETER NAME="processInstanceID"/>
  <PAGE_PARAMETER NAME="activityInstanceID"/>
  <PAGE_PARAMETER NAME="eventClass"/>
  <PAGE_PARAMETER NAME="eventType"/>
  <PAGE_PARAMETER NAME="eventMatchData"/>
  <PAGE_PARAMETER NAME="eventIndex"/>
  <PAGE_PARAMETER NAME="eventOutputData"/>
  <PAGE_PARAMETER NAME="eventRaisedBy"/>


  <SERVER_INTERFACE
    CLASS="WorkflowAdministration"
    NAME="ACTION"
    OPERATION="overrideEventWaitForActivityInstance"
    PHASE="ACTION"
  />


  <CLUSTER
    LABEL_WIDTH="30"
    TITLE="Cluster.Title.EventData"
  >
    <FIELD
      LABEL="Field.Label.EventClass"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="eventClass"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EventType"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="eventType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EventMatchData"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="eventMatchData"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="eventType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="eventWaitOverrideDetails$eventType"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="eventClass"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="eventWaitOverrideDetails$eventClass"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="eventIndex"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="eventWaitOverrideDetails$eventIndex"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="activityInstanceID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="eventWaitOverrideDetails$activityInstanceID"
    />
  </CONNECT>
</VIEW>
