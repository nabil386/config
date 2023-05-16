<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This is a view for adding a new request for appeal to an existing      -->
<!-- judicial review case.                                                  -->
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
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="JudicialReview"
    NAME="ACTION"
    OPERATION="addAppealedCase"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="implCaseID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="caseReference"/>
  <PAGE_PARAMETER NAME="judicialReviewID"/>


  <PAGE_PARAMETER NAME="priorAppealCaseID"/>


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
      PROPERTY="judicialReviewID"
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
    LABEL_WIDTH="50"
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
      <!-- BEGIN, CR00095199, RKi -->
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="implCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
      <!-- END, CR00095199 -->
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


    <FIELD LABEL="Field.Label.EffectiveDate">
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


    <FIELD LABEL="Field.Label.CourtPetition">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="courtPetitionInd"
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
