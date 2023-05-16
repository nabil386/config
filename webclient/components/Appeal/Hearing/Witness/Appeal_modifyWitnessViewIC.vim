<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows user to modify witnesses for Integrated cases.        -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="HearingWitness"
    NAME="DISPLAY"
    OPERATION="viewHearingWitness"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="HearingWitness"
    NAME="ACTION"
    OPERATION="modifyHearingWitness"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="hearingWitnessID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingWitnessID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="hearingWitnessIDKeytls$hearingWitnessID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingWitnessID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="hearingWitnessIDKeytls$hearingWitnessID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="readHearingWitnessDetails$hearingWitnessID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="hearingWitnessIDKey$hearingWitnessID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="readHearingWitnessDetails$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="modifyHearingWitnessDetails$versionNo"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Attendance">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="participatedCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="participatedCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Voluntary">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="readHearingWitnessDetails$voluntaryIndicator"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="modifyHearingWitnessDetails$voluntaryIndicator"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BehalfOf">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="readHearingWitnessDetails$behalfOfCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="modifyHearingWitnessDetails$behalfOfCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00407812, RB -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00407812 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="readHearingWitnessDetails$comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="modifyHearingWitnessDetails$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
