<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2008, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to add tasks to a contract.                  -->
<VIEW
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
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listTasksDueDuringContractPeriod"
  />


  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="ACTION"
    OPERATION="addTaskToContract"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="contractID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <!-- BEGIN CR00103727 SAI -->
  <PAGE_PARAMETER NAME="caseID"/>
  <!-- END CR00103727 -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="contractID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseContractID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="contractID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseContractID"
    />
  </CONNECT>


  <CLUSTER
    DESCRIPTION="Cluster.Tasks.Description"
    SHOW_LABELS="false"
  >


    <FIELD
      HEIGHT="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="taskID"
          NAME="DISPLAY"
          PROPERTY="taskDescription"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseContractTaskList"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
