<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2011, 2012, 2015. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to either add or modify focus area findings  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="CaseAuditFindings"
    NAME="DISPLAY"
    OPERATION="viewFocusAreaFinding"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="CaseAuditFindings"
    NAME="ACTION"
    OPERATION="modifyFocusAreaFinding"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="focusAreaFindingID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="focusAreaFindingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$focusAreaFindingID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="focusAreaFindingID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$focusAreaFindingID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$versionNo"
    />
  </CONNECT>


  <!-- BEGIN, CR00332924, SSK -->
  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="1"
  >
    <!-- END, CR00332924 -->
    <FIELD LABEL="Field.Label.FocusArea">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$focusAreaText"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.FocusAreaSatisfied"
      USE_BLANK="TRUE"
      USE_DEFAULT="FALSE"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$focusAreaSatisfied"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$focusAreaSatisfied"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="FALSE"
  >
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="200"
      LABEL="Field.Label.FindingsText"
      WIDTH="101"
    >
      <!-- END, CR00463142 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$findingsText"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$findingsText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >
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


</VIEW>
