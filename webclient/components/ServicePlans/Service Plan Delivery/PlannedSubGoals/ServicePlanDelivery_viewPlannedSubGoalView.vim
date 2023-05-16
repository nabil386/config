<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page to view the details of a planned sub-goal                         -->
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
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY"
    OPERATION="viewPlannedSubGoalWithPlannedItems"
    PHASE="DISPLAY"
  />

  <PAGE_PARAMETER NAME="plannedSubGoalID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="plannedSubGoalID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="plannedSubGoalKey$plannedSubGoalKey$key$plannedSubGoalID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Title.Sensitivity">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sensitivityCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="subGoalType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.Owner">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster_Title_Comments"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
