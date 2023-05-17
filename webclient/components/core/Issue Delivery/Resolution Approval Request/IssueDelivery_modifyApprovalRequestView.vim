<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007 Curam Software Ltd.																											-->
<!-- All rights reserved.                                                                   -->
<!-- This software is the confidential and proprietary information of Curam									-->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose										-->
<!-- such Confidential Information and shall use it only in accordance with									-->
<!-- the terms of the license agreement you entered into with Curam				  								-->
<!-- Software.                                                                              -->
<!-- Description                                                                            -->
<!-- ===========                                                                            -->
<!-- The included view to modify an issue approval request.																	-->
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
    CLASS="IssueDelivery"
    NAME="DISPLAY"
    OPERATION="readRejectDetails"
  />


  <SERVER_INTERFACE
    CLASS="IssueDelivery"
    NAME="ACTION"
    OPERATION="modifyApprovalRequest"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="issueApprovalRequestID"/>
  <PAGE_PARAMETER NAME="description"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="issueApprovalRequestID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="issueApprovalRequestID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="issueApprovalRequestID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="issueApprovalRequestID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="1"
    TITLE="Cluster.Label.Details"
  >


    <FIELD LABEL="Field.Label.RejectionReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rejectionReason"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rejectionReason"
        />
      </CONNECT>


    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Label.RejectionComments"
  >
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="4"
      LABEL="Cluster.Label.RejectionComments"
    >
      <!-- END, CR00463142 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rejectionComments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rejectionComments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
