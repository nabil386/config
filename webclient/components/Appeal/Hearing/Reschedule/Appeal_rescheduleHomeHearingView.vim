<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
   
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software, Ltd. ("Confidential Information"). You shall not       -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to confirm home hearing.                     -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="readAddress"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="HearingSchedule"
    NAME="ACTION"
    OPERATION="rescheduleHomeHearing"
    PHASE="ACTION"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleAddressID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$readConcernRoleAddressKey$concernRoleAddressID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$rescheduleHomeHearingDetails$rescheduleHearingDetailsCommon$hearingID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="addressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="addressID"
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
    NUM_COLS="2"
    SHOW_LABELS="true"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Date">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="scheduledDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.HearingOfficial">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="userName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Location">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="formattedAddressData"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.OutOfOfficeFrom">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="outOfOfficeFromTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.OutOfOfficeTo">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="outOfOfficeToTime"
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
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
