<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2011, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This is a view for adding a new request for appeal to an existing      -->
<!-- hearing review case.                                                   -->
<!--                                                                        -->
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
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="contextDescription"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="HearingReview"
    NAME="ACTION"
    OPERATION="addAppealedCase"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="implCaseID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="caseReference"/>
  <PAGE_PARAMETER NAME="hearingReviewID"/>
  <PAGE_PARAMETER NAME="priorAppealCaseID"/>
  <PAGE_PARAMETER NAME="appealObjectsDelimitedList"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="implCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="implCaseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingReviewID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="appealCaseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="priorAppealCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="priorAppealCaseID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.AppealedCaseDetails"
  >


    <FIELD LABEL="Field.Label.CaseReference">
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="caseReference"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateReceived">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateReceived"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ResolutionApprovalDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReceiptMethod">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptMethod"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Emergency">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="emergencyCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ContinueBenefits">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="continueBenefitsIndicator"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReceiptNotice">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptNoticeIndicator"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00407812, RB -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00407812 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
