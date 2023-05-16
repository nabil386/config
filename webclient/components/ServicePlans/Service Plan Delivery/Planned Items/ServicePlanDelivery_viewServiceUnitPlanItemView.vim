<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2009, 2011 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                    -->
<!-- This software is the confidential and proprietary information of Curam  -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose     -->
<!-- such Confidential Information and shall use it only in accordance with  -->
<!-- the terms of the license agreement you entered into with Curam          -->
<!-- Software.                                                               -->
<!-- Description                                                             -->
<!-- ===========                                                             -->
<!-- This page allows the user to view the details of a service unit deliver -->
<!-- plan item                                                               -->
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
    OPERATION="viewServiceUnitPlannedItemDetails"
    PHASE="DISPLAY"
  />
  <!-- BEGIN, CR00161962, LJ -->
  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY_APPROVALDETAILS"
    OPERATION="getListOfApprovalCriteriaForPlannedItem"
    PHASE="DISPLAY"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>
  <!-- END, CR00161962 -->


  <PAGE_PARAMETER NAME="plannedItemID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="plannedItemID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="plannedItemIDKey$plannedItemID"
    />
  </CONNECT>
  <!-- BEGIN, CR00161962, LJ -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="plannedItemID"
    />
    <TARGET
      NAME="DISPLAY_APPROVALDETAILS"
      PROPERTY="key$plannedItemIDKey$plannedItemIDKey$plannedItemID"
    />
  </CONNECT>
  <!-- END, CR00161962 -->


  <!-- BEGIN, CR00235316, SS -->
  <CLUSTER NUM_COLS="2">
    <!-- END, CR00235316 -->
    <FIELD LABEL="Field.Label.PlanItemName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN CR00124520, GBA-->
    <FIELD LABEL="Field.Label.ConcerningName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concerningName"
        />
      </CONNECT>
      <!-- BEGIN, CR00235316, SS -->
      <LINK PAGE_ID="Participant_resolveConcernRoleTypeHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concerningID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>


      </LINK>
      <!-- END, CR00235316 -->
    </FIELD>
    <!-- END CR00124520-->
    <FIELD LABEL="Field.Label.ExpectedStartDate">
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
    </FIELD>
    <FIELD LABEL="Field.Label.ExpectedOutcome">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="outcomeName"
        />
      </CONNECT>
    </FIELD>
    <CONTAINER LABEL="Field.Label.AuthorizedUnits">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="authorizedUnits"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00235316, SS -->
      <ACTION_CONTROL
        APPEND_ELLIPSIS="false"
        LABEL="ActionControl.Label.ViewChanges"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanDelivery_listAuthorizationHistory"
        >
          <!-- END, CR00235316-->
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="plannedItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="plannedItemID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$description$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD LABEL="Field.Label.UnitType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$details$serviceUnitPlannedItemDetails$unitType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.EstimatedCost">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="estimatedCost"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Owner">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="userFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerUserName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <!-- BEGIN, CR00161962, LJ -->
    <FIELD LABEL="Field.Label.GuidanceURL">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$details$serviceUnitPlannedItemDetails$viewPlannedItemDetails$plannedItemDtls$guidanceURL"
        />
      </CONNECT>
      <LINK
        OPEN_NEW="true"
        URI_SOURCE_NAME="DISPLAY"
        URI_SOURCE_PROPERTY="result$details$serviceUnitPlannedItemDetails$viewPlannedItemDetails$plannedItemDtls$guidanceURL"
      />
    </FIELD>
    <!-- END, CR00161962 -->
    <FIELD LABEL="Field.Label.Sensitivity">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sensitivityCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.SubGoal">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="subGoalName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ExpectedEndDate">
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
    </FIELD>
    <FIELD LABEL="Field.Label.Outcome">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="outcomeAchieved"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.UnitsReceivedToDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$details$serviceUnitPlannedItemDetails$unitsReceivedToDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.GoodCause">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="goodCauseName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ActualCost">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actualCost"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Responsibility">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="planItemRespUserFullName"
        />
      </CONNECT>
      <!--BEGIN CR00124520, GBA-->
      <LINK PAGE_ID="ServicePlanDelivery_resolveResponsibilityHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="responsibilityID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="respSetToClientInd"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="respSetToClientInd"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="respSetToParticipant"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="respSetToParticipant"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="respUserName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="respUserName"
          />
        </CONNECT>
      </LINK>
      <!--END CR00124520-->
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="status"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST TITLE="Cluster.Label.ServiceUnitsReceived">
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="ServicePlanDelivery_viewServiceUnitDelivery">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="serviceUnitDeliveryID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="serviceUnitDeliveryID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="plannedItemID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="plannedItemID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$description$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanDelivery_modifyServiceUnitDelivery"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="serviceUnitDeliveryID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="serviceUnitDeliveryID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="plannedItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="plannedItemID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$description$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- BEGIN, CR00235316, SS -->
      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanDelivery_resolveCancelServiceUnitDelivery"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="serviceUnitDeliveryID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="serviceUnitDeliveryID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="plannedItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="plannedItemID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- END, CR00235316 -->
    </ACTION_SET>


    <FIELD
      LABEL="Field.Title.Units"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="numberofUnits"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.DeliveryDate"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deliveryDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.RecordedBy"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordedByFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="recordedBy"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Status"
      WIDTH="20"
    >
      <!--BEGIN, CR00146503, GBA-->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtlsList$dtls$recordStatus"
        />
      </CONNECT>
      <!--END, CR00146503-->
    </FIELD>
  </LIST>


  <!-- BEGIN, CR00161962, LJ -->
  <LIST TITLE="List.Title.ApprovalCriteria">
    <FIELD LABEL="Field.Label.CriteriaName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_APPROVALDETAILS"
          PROPERTY="result$dtls$criteriaName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.OccursWhen">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_APPROVALDETAILS"
          PROPERTY="result$dtls$occursWhen"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Priority">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_APPROVALDETAILS"
          PROPERTY="result$dtls$priority"
        />
      </CONNECT>
    </FIELD>


  </LIST>
  <!-- END, CR00161962 -->
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="plannedItemDtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
