<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007-2008, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Modify the details for a user.                                         -->
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
    CLASS="ExternalUserAccess"
    NAME="DISPLAY"
    OPERATION="readUser"
  />


  <SERVER_INTERFACE
    CLASS="ExternalUserAccess"
    NAME="ACTION"
    OPERATION="modifyUser"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="userName"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="userName"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$userName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="userDtls$userName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$details$userName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="userDtls$userName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$details$userDtls$userName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="creationDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="creationDate"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="password"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="password"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="loginFailures"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="loginFailures"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="fullName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="fullName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="lastSuccessLogin"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="lastSuccessLogin"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="passwordChanged"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="passwordChanged"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="accountEnabled"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="accountEnabled"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="logsSincePWDChange"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="logsSincePWDChange"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="applicationCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="applicationCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="title"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="title"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="defaultLocale"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="defaultLocale"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="type"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="type"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="44"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="firstname"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
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
    <FIELD
      LABEL="Field.Label.Sensitivity"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sensitivity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sensitivity"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.LastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="surname"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
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
    LABEL_WIDTH="44"
    NUM_COLS="2"
    TITLE="Cluster.Title.Security"
  >
    <FIELD LABEL="Field.Label.UserName">
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="userName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PasswordNew">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="newPassword"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Application"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="applicationCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="applicationCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.PasswordExpiryDays"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="pwdChangeEveryXDay"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="pwdChangeEveryXDay"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.AccountExpiry"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="passwordExpiryDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="passwordExpiryDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RoleName">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="roleName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="roleName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="roleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PasswordConfirm">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="confirmPassword"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="confirmPassword"
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
    <FIELD
      LABEL="Field.Label.PasswordExpiryLogs"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="pwdChangeAfterXLog"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="pwdChangeAfterXLog"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER TITLE="Cluster.Title.AccessPeriods">


    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="3"
    >
      <FIELD LABEL="Field.Label.SetAccess">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginRestrictions"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginRestrictions"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.LoginDayWed">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginDayWed"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginDayWed"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.LoginDaySat">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginDaySat"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginDaySat"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.LoginDayMon">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginDayMon"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginDayMon"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.LoginDayThurs">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginDayThurs"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginDayThurs"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.LoginDaySun">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginDaySun"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginDaySun"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.LoginDayTues">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginDayTues"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginDayTues"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.LoginDayFri">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginDayFri"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginDayFri"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="3"
    >


      <FIELD LABEL="Field.Label.LoginTimeFrom">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginTimeFrom"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginTimeFrom"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.LoginTimeTo">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginTimeTo"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginTimeTo"
          />
        </CONNECT>
      </FIELD>


      <FIELD CONTROL="SKIP"/>
    </CLUSTER>


  </CLUSTER>
</VIEW>
