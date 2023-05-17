<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to modify the the details a basic plan       -->
<!-- item                                                                   -->
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
    OPERATION="viewPlanItem"
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


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.PlanItemName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN CR00109001, GBA-->
    <FIELD LABEL="Field.Label.ConcerningName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concerningName"
        />
      </CONNECT>
      <!-- BEGIN CR00108989, GBA-->


      <!--BEGIN, CR00234759, MR-->
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
      <!--END, CR00234759-->


      <!-- END CR00108989-->
    </FIELD>
    <!-- END CR00109001-->


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


    <FIELD LABEL="Field.Label.GoodCause">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="goodCauseName"
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
          PROPERTY="result$plannedItemDetails$plannedItemDtls$guidanceURL"
        />
      </CONNECT>
      <LINK
        OPEN_NEW="true"
        URI_SOURCE_NAME="DISPLAY"
        URI_SOURCE_PROPERTY="result$plannedItemDetails$plannedItemDtls$guidanceURL"
      />
    </FIELD>
    <!-- END, CR00161962 -->
    <FIELD LABEL="Field.Label.SubGoal">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="subGoalName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Sensitivity">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sensitivityCode"
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


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="status"
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
        <!--BEGIN CR00108989, GBA-->
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
        <!--END CR00108989-->
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
    </FIELD>
    <!-- BEGIN, CR00161962, LJ -->
    <FIELD CONTROL="SKIP">
    </FIELD>
    <!-- END, CR00161962 -->


  </CLUSTER>
  <!-- BEGIN, CR00161962, LJ -->
  <LIST TITLE="List.Title.ApprovalCriteria">
    <FIELD
      LABEL="Field.Label.CriteriaName"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_APPROVALDETAILS"
          PROPERTY="result$dtls$criteriaName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.OccursWhen"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_APPROVALDETAILS"
          PROPERTY="result$dtls$occursWhen"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Priority"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_APPROVALDETAILS"
          PROPERTY="result$dtls$priority"
        />
      </CONNECT>
    </FIELD>


  </LIST>
  <!-- END, CR00161962 -->


  <!--BEGIN CR00144311, GBA-->
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Description"
  >
    <FIELD HEIGHT="3">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="plannedItemDtls$description"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!--END CR00144311-->


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
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
