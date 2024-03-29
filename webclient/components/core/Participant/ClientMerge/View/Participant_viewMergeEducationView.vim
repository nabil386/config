<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Views an Education Snapshot.                                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="educationID"/>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="readEducation"
  />


  <PAGE_PARAMETER NAME="educationID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="educationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="educationReadKey$educationID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Course"
  >
    <FIELD LABEL="Field.Label.Institution">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="institution"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="courseType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Qualification">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="qualification"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="toDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Address"
  >
    <FIELD LABEL="Field.Label.Address">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="formattedAddressdata"
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
