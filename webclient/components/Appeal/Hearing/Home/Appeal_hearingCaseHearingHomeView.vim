<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!--                                                                        -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows user to view hearing cases.                           -->
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


  <PAGE_PARAMETER NAME="hearingID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="hearingID"
    />
  </CONNECT>


  <!-- BEGIN, CR00249680, SS -->
  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >
    <!-- END, CR00249680 -->
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Official">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fullName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartTime">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actualFormattedStartTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Location">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="locationName"
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


    <FIELD LABEL="Field.Label.HearingDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="scheduledDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.NotificationDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="noticeDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndTime">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actualFormattedEndTime"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
    TITLE="Cluster.Title.Continuance"
  >


    <FIELD LABEL="Field.Label.ContinuanceReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="postponeReasonCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ContinuanceComments">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="postponeReasonText"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ContinuanceDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="postponeDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="20"
    NUM_COLS="1"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <FIELD LABEL="Field.Label.Comments">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
