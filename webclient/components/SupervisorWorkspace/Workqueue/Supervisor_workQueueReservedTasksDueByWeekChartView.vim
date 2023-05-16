<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (c) 2010 Curam Software Ltd.                                   -->
<!-- All rights reserved.                                                     -->
<!-- This software is the confidential and proprietary information of Curam   -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose      -->
<!-- such Confidential Information and shall use it only in accordance with   -->
<!-- the terms of the license agreement you entered into with Curam           -->
<!-- Software.                                                                -->
<!-- Description                                                              -->
<!-- ===========                                                                -->
<!-- This page displays a list of tasks associated, that have been              -->
<!--  reserved by a particular user in a barchart format. This page is opened   -->
<!--  when a supervisor clicks on the portion of a horizontal bar that represents   -->
<!--  reserved tasks.                                                           -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="MaintainSupervisorWorkQueues"
    NAME="DISPLAYCHART"
    OPERATION="readWorkqueueReservedTasksByUserDueByWeek"
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


    <FIELD CONFIG="WorkQueueReservedTasksByWeek.BarChart.Config">
      <CONNECT>
        <SOURCE
          NAME="DISPLAYCHART"
          PROPERTY="barChartXML"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
