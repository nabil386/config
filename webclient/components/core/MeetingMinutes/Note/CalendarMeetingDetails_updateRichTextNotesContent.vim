<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007, 2010 - 2011 Curam Software Ltd.                    -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to update the meeting notes-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="MeetingMinutesManagement"
    NAME="DISPLAY"
    OPERATION="readMeetingNotes"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="MeetingMinutesManagement"
    NAME="ACTION"
    OPERATION="updateMeetingNotes"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="meetingID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="meetingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$meetingID"
    />
  </CONNECT>


  <!-- pass rest of attributes to modify -->
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$notesDtls$meetingNotesID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$meetingNotesID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$notesDtls$meetingID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$meetingID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$notesDtls$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$versionNo"
    />
  </CONNECT>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Notes"
  >
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="221"
      LABEL="Cluster.Title.Notes"
    >
      <!-- END, CR00463142 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="notes"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="richTextMeetingNotes"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
