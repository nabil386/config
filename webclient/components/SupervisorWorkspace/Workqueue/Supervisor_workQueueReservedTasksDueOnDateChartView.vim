<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (c) 2010 Curam Software Ltd.                                   -->
<!-- All rights reserved.                                                     -->
<!-- This software is the confidential and proprietary information of Curam   -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose      -->
<!-- such Confidential Information and shall use it only in accordance with   -->
<!-- the terms of the license agreement you entered into with Curam           -->
<!-- Software.                                                                -->
<!-- Description                                                              -->
<!-- ===========                                                              -->
<!-- This page helps the supervisor manage the reserved tasks due on the      -->
<!--  specified date.                                                         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="MaintainSupervisorWorkQueues"
    NAME="DISPLAYCHART"
    OPERATION="readWorkqueueReservedTasksByUserDueOnDate"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="workQueueID"/>
  <PAGE_PARAMETER NAME="deadlineDate"/>
  <PAGE_PARAMETER NAME="taskOptionCode"/>
  <PAGE_PARAMETER NAME="workQueueName"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="workQueueID"
    />
    <TARGET
      NAME="DISPLAYCHART"
      PROPERTY="key$dueDateKey$dueKey$workQueueID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="deadlineDate"
    />
    <TARGET
      NAME="DISPLAYCHART"
      PROPERTY="key$dueDateKey$dueKey$deadlineDate"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >


    <FIELD CONFIG="WorkQueueReserveTasksDueOnDate.BarChart.Config">
      <CONNECT>
        <SOURCE
          NAME="DISPLAYCHART"
          PROPERTY="barChartXML"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
