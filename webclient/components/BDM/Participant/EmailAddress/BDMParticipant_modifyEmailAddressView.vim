<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2002, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for the modify email address pages. -->
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
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="readEmailAddress"
  />


  <SERVER_INTERFACE
    CLASS="BDMParticipant"
    NAME="ACTION"
    OPERATION="modifyEmailAddress"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="concernRoleEmailAddressID"/>


  <!-- Map concernRoleEmailAddressID parameter to DISPLAY-phase bean  -->
  <!-- in order to retrieve details -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleEmailAddressID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readConcernRoleEmailKey$concernRoleEmailAddressID"
    />
  </CONNECT>
  <!-- Map concernRoleEmailAddressID parameter to ACTION bean -->
  <!-- in order to modify details -->
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="readConcernRoleEmailKey$concernRoleEmailAddressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRoleEmailAddressID"
    />
  </CONNECT>
  <!-- Map versionNo retrieved from DISPLAY Bean to ACTION bean -->
  <!-- in order to modify details -->
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
  <!-- Map statusCode retrieved from DISPLAY Bean to ACTION bean -->
  <!-- in order to modify details -->
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
  <!-- Map concernRoleID to ACTION bean in order to modify details -->
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <!-- BEGIN, CR00141707, MC -->
  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="1"
  >
    <!-- END, CR00141707 -->


    <!-- Map field values from DISPLAY bean to ACTION bean in -->
    <!-- order to modify details -->
    <FIELD LABEL="Field.Label.Address">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="emailAddress"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="emailAddress"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00141707, MC -->
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="50"
    >
      <!-- END, CR00141707 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.From">
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


    <FIELD LABEL="Field.Label.To">
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


    <FIELD LABEL="Field.Label.Primary">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryEmailInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="primaryEmailInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
