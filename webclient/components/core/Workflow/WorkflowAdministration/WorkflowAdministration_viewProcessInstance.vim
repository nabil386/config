<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                                 -->
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
  <!-- BEGIN, CR00071552, GBA -->
  <!-- END, CR00071552 -->
  <SERVER_INTERFACE
    CLASS="WorkflowAdministration"
    NAME="DISPLAY"
    OPERATION="viewProcessInstance"
    PHASE="DISPLAY"
  />
  <PAGE_PARAMETER NAME="processInstanceID"/>
  <PAGE_PARAMETER NAME="processName"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="processInstanceID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$processInstanceID"
    />
  </CONNECT>
  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.ProcessID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$processInstanceDetails$processID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  <LIST TITLE="List.Title.CurrentActivity">
    <FIELD LABEL="Field.Label.CurrentActivity">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$processInstanceActivityList$processInstanceActivity$currentActivity"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ActivityType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$processInstanceActivityList$processInstanceActivity$currentActivityType"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <LIST TITLE="List.Title.WDOInstanceData">
    <FIELD LABEL="Field.Label.DataItem">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$processInstanceDataList$processInstanceData$dataItem"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DataValue">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$processInstanceDataList$processInstanceData$dataValue"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <LIST
    DESCRIPTION="List.Description"
    TITLE="List.Title.ListWDOInstanceData"
  >
    <FIELD LABEL="Field.Label.ListWDOName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$listwdos$wdoName"
        />
      </CONNECT>
      <LINK PAGE_ID="WorkflowAdministration_viewInstanceDataForOneListWDO">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="processInstanceID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processInstanceID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listwdos$wdoName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="listWDOName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$processInstanceDetails$processName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </LIST>
</VIEW>
