<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows a user to modify a milestone delivery                 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY"
    OPERATION="readMilestone"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="ACTIONMW"
    OPERATION="modifyMilestoneForWaiver"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="milestoneDeliveryID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="milestoneDeliveryID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="milestoneDeliveryID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="expectedStartDate"
    />
    <TARGET
      NAME="ACTIONMW"
      PROPERTY="expectedStartDate"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="expectedEndDate"
    />
    <TARGET
      NAME="ACTIONMW"
      PROPERTY="expectedEndDate"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="milestoneDeliveryID"
    />
    <TARGET
      NAME="ACTIONMW"
      PROPERTY="dtls$milestoneDeliveryID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="status"
    />
    <TARGET
      NAME="ACTIONMW"
      PROPERTY="status"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTIONMW"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="milestoneConfigurationID"
    />
    <TARGET
      NAME="ACTIONMW"
      PROPERTY="milestoneConfigurationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTIONMW"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.MilestoneDetails"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ExpectedStartDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expectedStartDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ActualStartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actualStartDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTIONMW"
          PROPERTY="actualStartDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="status"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ExpectedEndDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expectedEndDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ActualEndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actualEndDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTIONMW"
          PROPERTY="actualEndDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Label.Owner"
  >
    <FIELD LABEL="Field.Label.User">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="ownerFullName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ownerUserName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTIONMW"
          PROPERTY="ownerUserName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >

    <!-- BEGIN, CR00377016, DJ -->
    <FIELD HEIGHT="4" LABEL="Field.Label.Comments">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTIONMW"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00377016 -->
  </CLUSTER>


</VIEW>
