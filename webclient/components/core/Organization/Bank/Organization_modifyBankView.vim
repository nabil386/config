<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the modify bank pages.                           -->
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
    OPERATION="readBank"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyBank"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="bankID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="bankID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="bankKeyStruct$bankID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="bankID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="bankStruct$bankID"
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


  <CLUSTER LABEL_WIDTH="35">
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Website">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="websiteAddress"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="websiteAddress"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankStatus"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="bankStatus"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
