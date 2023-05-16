<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for user assigned tasks.                             -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="MaintainSupervisorCase"
    NAME="DISPLAYCHART"
    OPERATION="readCaseWorkspaceDetails"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="MaintainSupervisorCase"
    NAME="ACTION"
    OPERATION="readCaseWorkspaceDetails"
    PHASE="ACTION"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYCHART"
      PROPERTY="key$caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="taskOptionCode"
    />
    <TARGET
      NAME="DISPLAYCHART"
      PROPERTY="key$taskOption"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$caseID"
    />
  </CONNECT>


  <ACTION_SET>
    <ACTION_CONTROL LABEL="ActionControl.Label.ReassignCase">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Supervisor_reassignCase"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="supervisor.viewTaskOption"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      LABEL="ActionControl.Label.TasksDueNextWeek"
      TYPE="SUBMIT"
    >
      <LINK PAGE_ID="Supervisor_resolvercaseWorkspace">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="supervisor.viewTaskOption"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      LABEL="ActionControl.Label.TasksDueNextMonth"
      TYPE="SUBMIT"
    >
      <LINK PAGE_ID="Supervisor_resolvercaseWorkspace">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="supervisor.viewTaskOption2"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >
    <FIELD CONFIG="DueNextMonth.BarChart.Config">
      <CONNECT>
        <SOURCE
          NAME="DISPLAYCHART"
          PROPERTY="chartXMLString"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
