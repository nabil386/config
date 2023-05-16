<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
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
         <CONNECT>
            <SOURCE
              NAME="DISPLAY"
                PROPERTY="contextDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
                PROPERTY="contextDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
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
    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="20%"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="TEXT"
            PROPERTY="Container.Field.StaticText.View"
          />
        </CONNECT>
        <LINK PAGE_ID="Appeal_resolveAppealedCaseResolution">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
                PROPERTY="contextDescription"
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
      </FIELD>


      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="TEXT"
            PROPERTY="Container.Field.StaticText.Edit"
          />
        </CONNECT>
        <LINK PAGE_ID="Appeal_resolveModifyAppealedCaseResolution">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
                PROPERTY="contextDescription"
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
      </FIELD>
    </CONTAINER>


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


  <LIST TITLE="List.Title.DecisionAttachments">
    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="20%"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="TEXT"
            PROPERTY="Container.Field.StaticText.View"
          />
        </CONNECT>


        <LINK PAGE_ID="Appeal_resolveViewDecisionAttachment">
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
                PROPERTY="contextDescription"
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
        </LINK>
      </FIELD>


      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="TEXT"
            PROPERTY="Container.Field.StaticText.Edit"
          />
        </CONNECT>
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
                PROPERTY="contextDescription"
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
      </FIELD>
    </CONTAINER>


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