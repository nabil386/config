<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2006, 2008 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- View Evidence Approval Request details.                                -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="DISPLAY"
    OPERATION="readApprovalRequestDetails"
  />


  <PAGE_PARAMETER NAME="approvalRequestID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="evidenceDescriptorID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="approvalRequestID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$approvalRequestID"
    />
  </CONNECT>


  <CLUSTER NUM_COLS="2">


    <FIELD LABEL="Field.Label.ApprovalRequestedBy">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approvalRequestedBy"
        />
      </CONNECT>


    </FIELD>


    <FIELD LABEL="Field.Label.ApprovalDecisionBy">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approvalDecisionBy"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ApprovalStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="status"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.RejectionReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rejectionReason"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ApprovalRequestedDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="requestedDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ApprovalDecisionDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approvalDecisionDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ManualApprovalRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="manualApprovalRequiredInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.RejectionComments"
  >


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rejectionComments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
