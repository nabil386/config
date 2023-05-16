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
<!-- The included view for work queue assigned tasks.                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER SHOW_LABELS="false">
    <FIELD CONFIG="WorkQueueWorkspace.BarChart.Config">
      <CONNECT>
        <SOURCE
          NAME="DISPLAYWQ"
          PROPERTY="barChartXML"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
