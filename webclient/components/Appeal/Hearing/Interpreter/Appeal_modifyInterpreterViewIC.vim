<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows user to modify interpreters for an integrated cases.  -->
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
    CLASS="HearingInterpreter"
    NAME="ACTION"
    OPERATION="modify"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="HearingInterpreter"
    NAME="DISPLAY"
    OPERATION="read"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="hearingServiceSupplierID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingServiceSupplierID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="hearingServiceSupplierID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingServiceSupplierID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="hearingServiceSupplierID"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="37">
    <FIELD
      LABEL="Field.Label.Attendance"
      WIDTH="75"
    >
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
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
