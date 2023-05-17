<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2007, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007-2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Views a Relationship Snapshot.                                         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="readRelationship"
  />


  <PAGE_PARAMETER NAME="concernRoleType"/>
  <PAGE_PARAMETER NAME="statusCode"/>
  <PAGE_PARAMETER NAME="concernRoleRelationshipID"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>


  <!-- BEGIN, CR00110570, MR -->
  <PAGE_PARAMETER NAME="caseID"/>
  <!-- END, CR00110570 -->


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleRelationshipID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="maintainConcernRoleRelationshipKey$concernRoleRelationshipID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="27"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.EndReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="relEndReasonCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Comments"
  >


    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
