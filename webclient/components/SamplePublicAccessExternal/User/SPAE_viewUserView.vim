<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007, 2011 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- External users homepage.                                               -->
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


  <SHORTCUT_TITLE>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="fullName"
      />
    </CONNECT>
  </SHORTCUT_TITLE>


  <SERVER_INTERFACE
    CLASS="ExternalUserAccess"
    NAME="DISPLAY"
    OPERATION="readCurrentUser"
  />


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Label.UserDetails"
  >
    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="firstname"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CreationDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="creationDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Sensitivity">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sensitivity"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Surname">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="surname"
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
    NUM_COLS="2"
    TITLE="Cluster.Title.Security"
  >
    <FIELD LABEL="Field.Label.UserName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="userDtls$userName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Application">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="applicationCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.LoginRestrictions">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="loginRestrictions"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PasswordExpiryDays">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="pwdChangeEveryXDay"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PasswordExpiryLogs">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="pwdChangeAfterXLog"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PasswordExpiryDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="passwordExpiryDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Role">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="roleName"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.AccountEnabled">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="accountEnabled"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.LoginFailures">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="loginFailures"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PasswordExpiryRemainingDays">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="passwordDaysExpiryDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PasswordExpiryRemainingLogins">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="passwordLogsExpiryNum"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
