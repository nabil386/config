<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. (&quot;Confidential Information&quot;). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <CONTAINER LABEL="Field.Label.DecisionResolution">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="decisionResolutionCode"
          />
        </CONNECT>
      </FIELD>


      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK PAGE_ID="Appeal_resolveViewAppealDecision">
          <!-- BEGIN, CR0009669, RKi -->
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
          <!-- END, CR00096692 -->
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
              PROPERTY="decisionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="hearingDecisionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK PAGE_ID="Appeal_resolveModifyAppealDecision">
          <!-- BEGIN, CR0009669, RKi -->
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
          <!-- END, CR00096692 -->
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
              PROPERTY="decisionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="hearingDecisionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </CONTAINER>
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
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_resolveAppealedCaseResolution">
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
            PROPERTY="result$decisionDetails$appealResolutionsList$dtls$appealRelationshipID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealRelationshipID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$decisionDetails$appealResolutionsList$dtls$priorAppealIndicator"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="priorAppealIndicator"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <!-- BEGIN, CR00098431, RKi -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_resolveModifyAppealedCaseResolution"
        >
          <!-- END, CR00098431 -->
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
              PROPERTY="result$decisionDetails$appealResolutionsList$dtls$appealRelationshipID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appealRelationshipID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$decisionDetails$appealResolutionsList$dtls$priorAppealIndicator"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="priorAppealIndicator"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD LABEL="Field.Label.CaseReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$decisionDetails$appealResolutionsList$dtls$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.AppealedCaseType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appealedCaseTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Resolution">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decisionDetails$appealResolutionsList$dtls$resolutionCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>


  <!-- BEGIN,  CR00022431, LK -->
  <LIST TITLE="List.Title.AppealedIssueResolution">
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_resolveIssueAppealedCaseResolution">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$caseID"
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
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <!-- BEGIN, CR00098431, RKi -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_resolveModifyIssueAppealedCaseResolution"
        >
          <!-- END, CR00098431 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$caseID"
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
          <!-- TODO -->
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
          <!-- TODO -->
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$referenceNumber"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.AppealedIssueType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$issueType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Resolution">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$decisionDetails$AppealManageDecisionDetailsList$dtls$resolutionCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>
  <!-- END,  CR00022431, LK -->


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


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK PAGE_ID="Appeal_resolveModifyDecisionAttachment">
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
              NAME="CONSTANT"
              PROPERTY="SearchPageID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="searchPage"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD LABEL="Field.Label.Date">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attachmentDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AttachmentType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionAttachmentType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>


  </LIST>
</VIEW>
