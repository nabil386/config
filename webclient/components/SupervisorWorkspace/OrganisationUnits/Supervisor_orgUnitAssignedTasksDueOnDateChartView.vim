<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (c) 2007 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!--This page helps the supervisor manage assigned tasks due on the specified  date.-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="MaintainSupervisorOrgUnits"
    NAME="DISPLAYCHART"
    OPERATION="readOrgUnitAssignedTasksByUserDueOnDate"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="orgUnitID"
    />
    <TARGET
      NAME="DISPLAYCHART"
      PROPERTY="key$key$key$organizationUnitID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="orgStructureID"
    />
    <TARGET
      NAME="DISPLAYCHART"
      PROPERTY="key$key$key$organizationStructureID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="deadlineDate"
    />
    <TARGET
      NAME="DISPLAYCHART"
      PROPERTY="key$key$key$deadlineDate"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="false"
  >
    <FIELD CONFIG="OrgUnitAssignedTasksByDate.BarChart.Config">
      <CONNECT>
        <SOURCE
          NAME="DISPLAYCHART"
          PROPERTY="barchartXML"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
