<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003, 2010-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows user to modify representatives.                       -->
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


  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="representativeID"/>


  <SERVER_INTERFACE
    CLASS="HearingRepresentative"
    NAME="DISPLAY"
    OPERATION="viewHearingRepresentative"
    PHASE="DISPLAY"
  />
  <SERVER_INTERFACE
    CLASS="HearingRepresentative"
    NAME="ACTION"
    OPERATION="modifyHearingRepresentative"
    PHASE="ACTION"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="representativeID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$hearingRepresentativeIDKey$hearingRepresentativeIDKey$hearingRepresentativeID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$readDetails$readDetails$hearingRepresentativeID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$modifyDetails$hearingRepresentativeIDKey$hearingRepresentativeID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$readDetails$readDetails$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$modifyDetails$modifyDetails$versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$readDetails$readDetails$behalfOfCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$modifyDetails$modifyDetails$behalfOfCode"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="35">
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$readDetails$readDetails$typeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dtls$modifyDetails$modifyDetails$typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Attendance">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$readDetails$readDetails$participatedCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dtls$modifyDetails$modifyDetails$participatedCode"
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
          PROPERTY="dtls$modifyDetails$modifyDetails$comments"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$readDetails$readDetails$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
