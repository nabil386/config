<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2011, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows a user to view the details for a milestone delivery   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <MENU MODE="IN_PAGE_NAVIGATION">


    <ACTION_CONTROL
      LABEL="ActionControl.Label.ViewMilestone"
      STYLE="in-page-current-link"
      TYPE="ACTION"
    >
      <LINK
        PAGE_ID="ProductDelivery_viewMilestoneModal"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="milestoneDeliveryID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="milestoneDeliveryID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      LABEL="ActionControl.Label.WaiverRequest"
      STYLE="in-page-link"
      TYPE="ACTION"
    >


      <LINK
        PAGE_ID="ICInvestigationDelivery_listMilestoneWaiverRequestModal"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="milestoneDeliveryID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="milestoneDeliveryID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>


      </LINK>
    </ACTION_CONTROL>
  </MENU>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="ProductDelivery"
    NAME="DISPLAY"
    OPERATION="readMilestone"
  />


  <PAGE_PARAMETER NAME="milestoneDeliveryID"/>
  <PAGE_PARAMETER NAME="description"/>
  <PAGE_PARAMETER NAME="caseID"/>


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


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="createdBySystem"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.CreatedBySystem">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="createdBySystem"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AssociatedTo">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="associateTo"
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
        PROPERTY="createdBySystem"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.CreatedBySystem">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="createdBySystem"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.AssociatedTo">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="associateTo"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
