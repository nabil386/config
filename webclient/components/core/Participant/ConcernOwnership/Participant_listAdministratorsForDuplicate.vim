<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a list of administrators             -->
<!-- for a duplicate concern.                                               -->
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
        PROPERTY="description"
      />
    </CONNECT>
  </SHORTCUT_TITLE>


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="listAdministratorsForDuplicateParticipant"
  />


  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="maintainAdminConcernRoleKey$concernRoleID"
    />
  </CONNECT>


  <CLUSTER
    SHOW_LABELS="false"
    STYLE="tab-renderer"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="data"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST>


    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="participantOwnerName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.OwnerType"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="orgObjectType"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
