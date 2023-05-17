<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010-2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to list all the Approval Request for a Planned Item. -->
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
    OPERATION="listPlanItemApprovalRequest"
    PHASE="DISPLAY"
  />
  <PAGE_PARAMETER NAME="plannedItemID"/>
  <PAGE_PARAMETER NAME="description"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="plannedItemID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="approvalRequestPlannedItemIDKey$plannedItemID"
    />
  </CONNECT>


  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.NewButton.label"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ServicePlanDelivery_submitServicePlanPlanItem"
      >
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
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>
  <LIST>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanDelivery_modifyPlanPlanItemApprovalRequest"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="plannedItemApprovalRequestID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="plannedItemApprovalRequestID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      
      <!-- BEGIN, CR00236218, MR -->
      <ACTION_CONTROL
        IMAGE="ApproveButton"
        LABEL="ActionControl.Label.Approve"
        TYPE="ACTION"
        >
        
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanDelivery_approveServicePlanPlanItem"
          >
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
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="plannedItemApprovalRequestID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="plannedItemApprovalRequestID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL
        IMAGE="RejectButton"
        LABEL="ActionControl.Label.Reject"
        TYPE="ACTION"
        >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanDelivery_rejectServicePlanPlanItem"
          >
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
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- END, CR00236218 -->
    </ACTION_SET>
    <FIELD LABEL="Field.Label.RequestedBy">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="requestedByUserFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="requestedByUser"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.RequestDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="requestedDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DecisionMadeBy">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="requestedDecisionByUserFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
        WINDOW_OPTIONS="width=900"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="approvalDecisionByUser"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.DecisonDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$approvalDecisionDate"
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
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="ServicePlanDelivery_viewPlanPlanItemApprovalRequest">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
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
            PROPERTY="plannedItemApprovalRequestID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="plannedItemApprovalRequestID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
</VIEW>
