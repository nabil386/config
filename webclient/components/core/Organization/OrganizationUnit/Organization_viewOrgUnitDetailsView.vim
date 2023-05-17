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
<!-- Description -->
<!-- =========== -->
<!-- This page allows a user to view an organization unit details.           -->
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


  <PAGE_PARAMETER NAME="organisationUnitID"/>
  <PAGE_PARAMETER NAME="organisationStructureID"/>


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="readOrganisationUnitDetailsAndUserList"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="organisationUnitID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$organisationUnitID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="organisationStructureID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$organisationStructureID"
    />
  </CONNECT>


  <CLUSTER NUM_COLS="2">
    <FIELD LABEL="Field.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$organisationUnitDetails$organisationUnitName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.DefaultLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$organisationUnitDetails$locationName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.BusinessType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$organisationUnitDetails$businessTypeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.UnitStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$organisationUnitDetails$statusCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST TITLE="List.Title">
    <FIELD
      LABEL="Field.Name"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$userDetailsList$fullName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Email"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$userDetailsList$email"
        />
      </CONNECT>
      <LINK
        URI_SOURCE_NAME="DISPLAY"
        URI_SOURCE_PROPERTY="result$userDetailsList$emailLink"
      />
    </FIELD>
    <FIELD
      LABEL="Field.Phone"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$userDetailsList$phoneNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Mobile"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$userDetailsList$mobileNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Fax"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$userDetailsList$faxNumber"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
