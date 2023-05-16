<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 Curam Software.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software, Ltd. ("Confidential Information"). You  -->
<!-- shall not disclose such Confidential Information and shall use it only -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- with Curam Software.                                                   -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to reject a decision                         -->
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
    CLASS="DecisionApproval"
    NAME="ACTION"
    OPERATION="rejectDecision"
    PHASE="ACTION"
  />


  <ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    />


    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />
  </ACTION_SET>


  <PAGE_PARAMETER NAME="hearingDecisionID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="versionNo"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingDecisionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="hearingDecisionID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="25"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonCode"
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
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonText"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
