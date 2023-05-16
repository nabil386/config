<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2018. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp. -->                    
<!-- 
	This page allows the user to create a new task, which is defined as an 
	instruction to carry out some item of work for a case. 
-->
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
    OPERATION="listCaseParticipantsDetails"
  />


  <SERVER_INTERFACE
    CLASS="WorkAllocation"
    NAME="ACTION"
    OPERATION="createManualTask"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>
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
  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Subject">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="subject"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Container.Label.Concerning"
      USE_BLANK="TRUE"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="caseParticipantRoleID"
          NAME="DISPLAY"
          PROPERTY="nameAndAgeOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseParticipantRoleID"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Priority"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priority"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Deadline"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deadlineTime"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER LABEL_WIDTH="17.5">
    <FIELD LABEL="Field.Label.ReserveToMe">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reserveToMeInd"
        />
      </CONNECT>
    </FIELD>
    <CONTAINER LABEL="Container.Label.AssignedTo">
      <FIELD WIDTH="35">
        <SCRIPT
          ACTION="clearAssignedTo()"
          EVENT="ONCHANGE"
          SCRIPT_FILE="ClearAssignedTo.js"
        />
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="assignType"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="60">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="assignmentID"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
  </CLUSTER>
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
