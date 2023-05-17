<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- View the details for a user.                                           -->
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


  <PAGE_PARAMETER NAME="userName"/>


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="readUserRoleAndContactDetails1"
    PHASE="DISPLAY"
  />


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


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="firstName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.LastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="lastName"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="isExternalUser"
      />
    </CONDITION>
    <FIELD LABEL="Field.Label.Role">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="roleName"
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
    TITLE="Cluster.Title.ContactDetails"
  >
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="isExternalUser"
      />
    </CONDITION>
    <FIELD LABEL="Field.Label.BusinessPhone">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="businessPhoneNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.BusinessEmail">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="businessEmail"
        />
      </CONNECT>
      <LINK
        URI_SOURCE_NAME="DISPLAY"
        URI_SOURCE_PROPERTY="businessEmailLink"
      />
    </FIELD>
    <FIELD LABEL="Field.Label.Mobile">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mobileNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Pager">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="pagerNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PersonalPhone">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="personalPhoneNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PersonalEmail">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="personalEmail"
        />
      </CONNECT>
      <LINK
        URI_SOURCE_NAME="DISPLAY"
        URI_SOURCE_PROPERTY="personalEmailLink"
      />
    </FIELD>
    <FIELD LABEL="Field.Label.Fax">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="faxNumber"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
