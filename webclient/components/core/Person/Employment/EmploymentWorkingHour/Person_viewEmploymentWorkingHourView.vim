<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2007, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007,2009, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Views an Employment Working Hour Snapshot.                             -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="readEmploymentWorkingHour"
  />


  <PAGE_PARAMETER NAME="employmentWorkingHourID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="employmentWorkingHourID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$employmentWorkingHourID"
    />
  </CONNECT>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Comments"
  >


    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
