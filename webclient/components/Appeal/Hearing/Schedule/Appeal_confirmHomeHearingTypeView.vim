<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2011, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
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
    OPERATION="scheduleHomeHearing"
    PHASE="ACTION"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <PAGE_PARAMETER NAME="addressID"/>
  <PAGE_PARAMETER NAME="date"/>
  <PAGE_PARAMETER NAME="userName"/>
  <PAGE_PARAMETER NAME="concernRoleAddressID"/>
  <PAGE_PARAMETER NAME="hearingType"/>


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
      PROPERTY="date"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="scheduledDateTime"
    />
  </CONNECT>


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
      PROPERTY="userName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="userName"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    SHOW_LABELS="true"
    TITLE="Cluster.Title.Details"
  >
    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="45"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="date"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>
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
