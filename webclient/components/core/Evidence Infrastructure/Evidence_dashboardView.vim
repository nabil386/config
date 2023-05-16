<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence dashboard details.                                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00230133, CD -->
  <PAGE_PARAMETER NAME="caseID"/>

  <!-- BEGIN, CR00457538, DM -->
  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="DISPLAY"
    OPERATION="getEvidenceDashboardDataWithRelationshipAndFilter"
  />
  <!-- END, CR00457538 -->


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>
 

  <!-- BEGIN, CR00457538, DM -->
  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="ACTION"
    OPERATION="getEvidenceDashboardDataWithRelationshipAndFilter"
    PHASE="ACTION"
  />
 <!-- END, CR00457538--> 


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>
  

  
  
  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="DISPLAYPARTICIPANTS"
    OPERATION="getEvidenceParticipantListForCase"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYPARTICIPANTS"
      PROPERTY="caseID"
    />
  </CONNECT>
  <!-- END, CR00230133, CD -->


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="GETPARTICIPANT"
    OPERATION="getPrimaryCaseParticipantDetails"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="GETPARTICIPANT"
      PROPERTY="caseID"
    />
  </CONNECT>


  <!-- Commenting out for CEF-016 as it does not work
  <CLUSTER
    LABEL_WIDTH="20"
    NUM_COLS="1"
    SHOW_LABELS="true"
  >
    <FIELD
      LABEL="Field.Label.Participant"
      USE_BLANK="false"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="participantID"
          NAME="DISPLAYPARTICIPANTS"
          PROPERTY="participantName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="participantID"
        />
      </CONNECT>
      <SCRIPT
        ACTION="enactHiddenActionControl(this)"
        EVENT="ONCHANGE"
        SCRIPT_FILE="evidenceTypeWorkspace.js"
      />


    </FIELD>
  </CLUSTER>
  -->


  <INCLUDE FILE_NAME="Evidence_IncomingEvidenceMsgView.vim"/>


  <!-- BEGIN, CR00243407, PMD -->
  <FIELD>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="evidenceTypes"
      />
    </CONNECT>
  </FIELD>
  <!-- END, CR00243407 -->


  <CLUSTER STYLE="evidence-dashboard-cluster">
    <CONTAINER STYLE="inner-span-action-control-visibility-hidden">
      <ACTION_CONTROL
        IMAGE="SearchButton"
        LABEL="ActionControl.Label.List"
        TYPE="SUBMIT"
      >
        <LINK PAGE_ID="THIS"/>
      </ACTION_CONTROL>
    </CONTAINER>
  </CLUSTER>
</VIEW>
