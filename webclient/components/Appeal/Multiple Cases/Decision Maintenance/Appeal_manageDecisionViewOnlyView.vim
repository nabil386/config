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
<!-- Software, Ltd. (&quot;Confidential Information&quot;). You shall not   -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!--  accordance with the terms of the license agreement you entered into   -->
<!-- with Curam Software.                                                   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <FIELD LABEL="Field.Label.DecisionResolution">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionResolutionCode"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.DecisionStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionStatus"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST TITLE="List.Title.Resolutions">

    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseReference"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AppealedCaseType"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appealedCaseTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Resolution"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decisionDetails$appealResolutionsList$dtls$resolutionCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>


  <!-- BEGIN, CR00052221, RKi -->
  <LIST TITLE="List.Title.AppealedIssueResolution">
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_resolveIssueAppealedCaseResolution">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$issueCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="issueCaseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$appealRelationshipID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealRelationshipID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$priorAppealIndicator"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="priorAppealIndicator"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>

    <FIELD
      LABEL="Field.Label.ReferenceNumber"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$referenceNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AppealedIssueType"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$issueType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Resolution"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$resolutionCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <!-- END, CR00052221 -->


  <LIST TITLE="List.Title.DecisionAttachments">


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_resolveViewDecisionAttachment">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="attachmentLinkID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="attachmentLinkID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="decisionAttachmentType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="decisionAttachmentType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealCaseID"
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
            PROPERTY="decisionStatus"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="decisionStatus"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>

    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attachmentDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AttachmentType"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionAttachmentType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>


  </LIST>
</VIEW>
