<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003, 2010-2011 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for listing hearing attendees.  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00249680, SS -->
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>
  <!-- END, CR00249680 -->


  <PAGE_PARAMETER NAME="hearingID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="hearingKey$hearingID"
    />
  </CONNECT>


  <!-- BEGIN, CR00249680, SS -->
  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL
      IMAGE="InviteUserButton"
      LABEL="ActionControl.Label.InviteUser"
      TYPE="ACTION"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Appeal_inviteHearingAttendee"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="hearingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="hearingID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>
  <!-- END, CR00249680 -->


  <LIST>
    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="displayName"
        />
      </CONNECT>
      <!-- BEGIN, CR00198761, GP -->
      <!-- BEGIN, CR00246931, GP -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <!-- END, CR00198761 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <!-- END, CR00246931 -->


    <FIELD
      LABEL="Field.Title.Attendance"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="participatedCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Type"
      WIDTH="30"
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
