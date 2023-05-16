<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005-2007, 2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page to modify planned sub-goal                                        -->
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
    OPERATION="viewPlannedSubGoal"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="ACTION"
    OPERATION="modifyPlannedSubGoal"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="plannedSubGoalID"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="plannedSubGoalModifyDetails$versionNo"
    />
  </CONNECT>


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


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="plannedSubGoalID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="modifyPlannedSubGoalDetails$plannedSubGoalModifyDetails$plannedSubGoalID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="36"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Title.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="subGoalName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Sensitivity"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sensitivityCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sensitivityCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Outcome"
      USE_BLANK="true"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="outcomeAchieved"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="outcomeAchieved"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.Owner">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="fullName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ownerID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ownerID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster_Title_Comments"
  >
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="4"
      LABEL="Cluster_Title_Comments"
    >
      <!-- END, CR00463142 -->
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
