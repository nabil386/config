<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This is the included view to create a new appeal event     .           -->
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
    CLASS="Case"
    NAME="ACTION"
    OPERATION="createAppeal"
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
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="47"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.ServiceSupplier">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="serviceSupplierID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentReference">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="documentReferenceNo"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Outcome"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="60"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="outcomeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EndDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AppealType"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileReferenceNo"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Contact">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contact"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ExpectedEndDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="expectedCompletionDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.NotificationDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notificationDate"
        />
      </CONNECT>
    </FIELD>


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
          PROPERTY="reasonText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
