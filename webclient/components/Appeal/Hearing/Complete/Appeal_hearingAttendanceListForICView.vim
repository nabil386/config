<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003, 2010 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                    -->
<!--                                                                         -->
<!-- This software is the confidential and proprietary information of Curam  -->
<!-- Software, Ltd. ("Confidential Information").  You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with  -->
<!-- the terms of the license agreement you entered into with Curam Software.-->
<!-- Description                                                             -->
<!-- ===========                                                             -->
<!-- This page allows user to list hearing attendance.                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Hearing"
    NAME="DISPLAY"
    OPERATION="listAttendance"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="hearingID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$hearingKey_bo$hearingKey$hearingID"
    />
  </CONNECT>


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


  <LIST TITLE="List.Label.HearingAttendance">


    <CONTAINER
      ALIGNMENT="CENTER"
      LABEL="Container.Title.Attendance"
      WIDTH="10"
    >


      <WIDGET TYPE="MULTISELECT">


        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseParticipantRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attendanceCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="hearingAttendanceKey"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="hearingAttendanceVersionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="userName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="displayName"
            />
          </CONNECT>
        </WIDGET_PARAMETER>


        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="attendeeList"
            />
          </CONNECT>
        </WIDGET_PARAMETER>


      </WIDGET>


    </CONTAINER>


    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="45"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="displayName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Type"
      WIDTH="45"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attendanceCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
