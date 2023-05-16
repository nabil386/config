<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page displays the Service Plan Home.                   -->
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
    CLASS="ServicePlan"
    NAME="DISPLAY"
    OPERATION="read"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="servicePlanID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="servicePlanID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$servicePlanID"
    />
  </CONNECT>

  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.SecurityIdentifiers"
  >


    <FIELD LABEL="Field.Label.CreateRightsSID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="createRights"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ApproveRightsSID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approveRights"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ViewRightsSID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="viewRights"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MaintainRightsSID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maintainRights"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CloneRightsSID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="cloneRights"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Description"
  >
    <!-- BEGIN, CR00227878, GP -->
    <CONTAINER>
      <FIELD HEIGHT="4">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
        </CONNECT>
      </FIELD>
      <ACTION_CONTROL
        IMAGE="LocalizableTextTranslation"
        LABEL="ActionControl.Label.TextTranslation"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanAdmin_listLocalizableServicePlanDescriptionText"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$details$plan$descriptionTextID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="localizableTextID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="servicePlanID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="servicePlanID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <!-- END,  CR00227878 -->
  </CLUSTER>


  <!--<ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >
    <ACTION_CONTROL
      IMAGE="EditButton"
      LABEL="ActionControl.Label.Edit"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ServicePlanAdmin_modifyServicePlan"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="servicePlanID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="servicePlanID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="DeleteButton"
      LABEL="ActionControl.Label.Delete"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ServicePlanAdmin_cancelServicePlan"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="servicePlanID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="servicePlanID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>-->


</VIEW>
