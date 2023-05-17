<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

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
<!-- Home page for the service plan group delivery.                         -->
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
    CLASS="ServicePlanGroupDelivery"
    NAME="DISPLAY"
    OPERATION="readHomePage"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="servicePlanGroupDeliveryId"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="servicePlanGroupDeliveryId"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="servicePlanGroupDeliveryId"
    />
  </CONNECT>


    <CLUSTER
      NUM_COLS="2"
    >
      <FIELD LABEL="Field.Label.GroupEstimated">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="highLevelCosts$estimatedCost"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.GroupActual">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="highLevelCosts$actualCost"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>

  <CLUSTER SHOW_LABELS="false">
    <LIST TITLE="List.Title.Label">

      <FIELD LABEL="Field.Label.Reference" WIDTH="10">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseReference"
          />
        </CONNECT>
        <LINK PAGE_ID="ServicePlanDelivery_home1">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="servicePlanDeliveryId"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
        </LINK>
      </FIELD>


      <FIELD LABEL="Field.Label.ServicePlan" WIDTH="20">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="servicePlanType"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.Goal" WIDTH="22">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="goal"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.Outcome" WIDTH="10">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="outcome"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.EstimatedCosts" WIDTH="15">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="servicePlanDeliveries$estimatedCost"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.ActualCosts" WIDTH="13">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="servicePlanDeliveries$actualCost"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.Status" WIDTH="10">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="status"
          />
        </CONNECT>
      </FIELD>
    </LIST>


  </CLUSTER>

</VIEW>
