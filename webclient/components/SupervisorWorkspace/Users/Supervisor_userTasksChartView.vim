<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright 2010-2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for user assigned tasks.                             -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <CLUSTER NUM_COLS="1" SHOW_LABELS="false" STYLE="outer-cluster-borderless">
    <!-- END, CR00123088 --><!-- BEGIN, CR00291093, DJ -->
    <FIELD CONFIG="UserWorkspace.BarChart.Config">
      <CONNECT>
        <SOURCE NAME="DISPLAYCHART" PROPERTY="userWorkspaceChartXMLString"/>
      </CONNECT>
    </FIELD>
  
    <!-- END, CR00291093 -->
  </CLUSTER>


</VIEW>