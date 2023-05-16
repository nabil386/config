<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This is the included view containing the details of a case decision.   -->
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
    CLASS="Screening"
    NAME="DISPLAY"
    OPERATION="viewAssessmentDecision"
  />
  <PAGE_PARAMETER NAME="assessmentID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="assessmentID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$assessmentID"
    />
  </CONNECT>
  <LIST TITLE="List.Title.Objectives">
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maintainAssessmentObjectiveList$dtls$name"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maintainAssessmentObjectiveList$dtls$type"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Value">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maintainAssessmentObjectiveList$dtls$value"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <CLUSTER
    NUM_COLS="2"
    STYLE="outer-cluster-borderless"
  >
    <CLUSTER
      SHOW_LABELS="false"
      TITLE="Cluster.Title.Rules"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="assessmentResultText"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <CLUSTER
      SHOW_LABELS="false"
      TITLE="Cluster.Title.Evidence"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceUsed"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>
</VIEW>
