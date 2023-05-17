<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2022. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2005,2007,2009-2010 Curam Software Ltd.             -->
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
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="readUser"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
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
      PROPERTY="key$userKeyStruct$userName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$userDetails$userName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="userName"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="loginID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="loginID"
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
      PROPERTY="oldPassword"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="oldPassword"
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
      PROPERTY="contactVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="contactVersionNo"
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
      PROPERTY="businessPhoneID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="businessPhoneID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="personalPhoneNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="personalPhoneNumberID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="businessEmailID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="businessEmailID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="personalEmailID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="personalEmailID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="mobilePhoneID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="mobilePhoneID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="pagerID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="pagerID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="faxID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="faxID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="lastSuccessLoginDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="lastSuccessLoginDate"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="busPhoneVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="busPhoneVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="personalPhoneVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="personalPhoneVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="businessEmailVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="businessEmailVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="personalEmailVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="personalEmailVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="mobileVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="mobileVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="pagerVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="pagerVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="faxVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="faxVersionNo"
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
      PROPERTY="endDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="endDate"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <FIELD
      LABEL="Field.Label.Title"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="title"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="title"
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


    <!-- BEGIN, CR00159851, NP -->
    <FIELD 
    	LABEL="Field.Label.DefaultLocale"
    	USE_DEFAULT="true"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="defaultLocale"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="defaultLocale"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="defaultLocale"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00159851 -->


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
    <FIELD LABEL="Field.Label.Location">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="locationName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="locationID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RedirectTasks">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="assigneeFullName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="1"
    TITLE="Cluster.Title.Contact"
  >


    <CONTAINER LABEL="Container.Label.BusinessPhone">
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.BusinessCountryCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="businessCountryCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="businessCountryCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.BusinessAreaCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="businessAreaCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="businessAreaCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.BusinessNumber"
        WIDTH="7"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="businessNumber"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="businessNumber"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.BusinessPhoneExtn"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="businessPhoneExtn"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="businessPhoneExtn"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <CONTAINER LABEL="Container.Label.PersonalPhone">
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PersonalCountryCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="personalCountryCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="personalCountryCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PersonalAreaCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="personalAreaCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="personalAreaCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PersonalNumber"
        WIDTH="7"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="personalNumber"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="personalNumber"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.BusinessEmail"
      WIDTH="49"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="businessEMail"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="businessEMail"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.PersonalEmail"
      WIDTH="49"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="personalEMail"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personalEMail"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Container.Label.Fax">


      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.FaxCountryCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="faxCountryCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="faxCountryCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.FaxAreaCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="faxAreaCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="faxAreaCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.FaxNumber"
        WIDTH="7"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="faxNumber"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="faxNumber"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <CONTAINER LABEL="Container.Label.Mobile">


      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.MobileCountryCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="mobileCountryCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="mobileCountryCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.MobileAreaCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="mobileAreaCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="mobileAreaCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.MobileNumber"
        WIDTH="7"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="mobileNumber"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="mobileNumber"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <CONTAINER LABEL="Container.Label.Pager">
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PagerCountryCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="pagerCountryCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="pagerCountryCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PagerAreaCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="pagerAreaCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="pagerAreaCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PagerNumber"
        WIDTH="7"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="pagerNumber"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="pagerNumber"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
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
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="password"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="password"
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
          PROPERTY="passwordExpiry"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="passwordExpiry"
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
      LABEL="Field.Label.AccountExpiry"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="passwordExpiresOn"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="passwordExpiresOn"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CallCentreUser">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ctiEnabled"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ctiEnabled"
        />
      </CONNECT>
    </FIELD>
    <CLUSTER>
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$userDetails$alternateLoginEnabled"
        />
      </CONDITION>
      <FIELD
        LABEL="Field.Label.LoginID"
        USE_BLANK="true"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="loginID"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="loginID"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <FIELD LABEL="Field.Label.PasswordConfirm">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="passwordConfirm"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="passwordConfirm"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.PasswordExpiryLogin"
      USE_DEFAULT="false"
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
    <CONTAINER LABEL="Field.Label.AccountEnabled">
      <!-- BEGIN, CR00050298, MR -->
      <!-- BEGIN, HARP 64908, SP -->
      <FIELD LABEL="Field.Label.AccountEnabled">
        <!-- END, HARP 64908 -->
        <!-- END, CR00050298 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="accountEnabled"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.AccessPeriods"
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
  </CLUSTER>
</VIEW>
