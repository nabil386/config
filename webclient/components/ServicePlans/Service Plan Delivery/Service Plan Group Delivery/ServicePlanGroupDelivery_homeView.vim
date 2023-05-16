<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Home page for the service plan group delivery.                         -->

<!-- BEGIN, CR00246725, MR -->
<?curam-deprecated Since Curam 6.0, replaced by ServicePlanGroupDelivery_homeView1.vim.
  This page does not take caseID as the page parameter which is required to an
  add attachment functionality which is present at the service plan group tab level. 
?>
<!-- END, CR00246725 -->
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
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Manage"
  >
    <CONTAINER>
      <ACTION_CONTROL
        IMAGE="AddServicePlan"
        LABEL="Field.StaticText.NewServicePlanDelivery"
      >
        <LINK PAGE_ID="ServicePlanGroupDelivery_addServicePlanDelivery">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
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
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.StaticText.NewServicePlanDelivery">
        <LINK PAGE_ID="ServicePlanGroupDelivery_addServicePlanDelivery">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
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
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <CONTAINER>
      <ACTION_CONTROL
        IMAGE="CloseIcon"
        LABEL="Field.StaticText.Close"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanGroupDelivery_close"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.StaticText.Close">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanGroupDelivery_close"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
  </CLUSTER>


  <CLUSTER SHOW_LABELS="false">


    <CONTAINER>


      <ACTION_CONTROL LABEL="Action.Label.Active">
        <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="activeOnly"
          />
        </CONDITION>
        <LINK PAGE_ID="ServicePlanGroupDelivery_homeActiveOnly">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="activeInd.true"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="activeInd"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="Action.Label.NonActive">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="activeOnly"
          />
        </CONDITION>
        <LINK PAGE_ID="ServicePlanGroupDelivery_home">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="servicePlanGroupDeliveryId"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="activeInd.false"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="activeInd"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </CONTAINER>


    <CLUSTER
      NUM_COLS="2"
      TITLE="Cluster.Title.Head"
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



    <LIST TITLE="Container.Title.Label">


      <CONTAINER
        LABEL="Container.Label.Action"
        SEPARATOR="Container.Separator"
        WIDTH="15"
      >
        <ACTION_CONTROL LABEL="ActionControl.Label.View">
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
        </ACTION_CONTROL>
        <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="ServicePlanDelivery_modifyDetails"
          >
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
        </ACTION_CONTROL>
      </CONTAINER>


      <FIELD LABEL="Field.Label.Reference">
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


      <FIELD LABEL="Field.Label.ServicePlan">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="servicePlanType"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.Goal">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="goal"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.Outcome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="outcome"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.EstimatedCosts">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="servicePlanDeliveries$estimatedCost"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.ActualCosts">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="servicePlanDeliveries$actualCost"
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
    </LIST>


  </CLUSTER>


</VIEW>
